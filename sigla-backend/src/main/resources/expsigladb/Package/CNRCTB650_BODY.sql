--------------------------------------------------------
--  DDL for Package Body CNRCTB650
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB650" AS


-- =================================================================================================
-- Verifica ammissibibilita del conguaglio per un dato soggetto anagrafico. Se il conguaglio è
-- ammesso si recuperano i dati esterni dell'ultimo conguaglio registrato e si riportano sul corrente.
-- =================================================================================================
PROCEDURE abilitaConguaglio
   (
    inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
    inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE
   ) IS

   esisteCompenso INTEGER;
   isStorico CHAR(1);
   isTemporaneo CHAR(1);
   imDetrazioniPerAnag NUMBER(15,2);
   eseguiLock CHAR(1);

   aRecConguaglioUltimo CONGUAGLIO%ROWTYPE;
   aRecCompensoConguaglioBase V_COMPENSO_CONGUAGLIO_BASE%ROWTYPE;
   aRecRateizzaClassificCoriC0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriP0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriR0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

    isChiavePrimariaRiduzioneCuneo CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'RIDUZIONE_CUNEO_DL_3_2020';
    isChiaveSecondariaDataInizio CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'DATA_INIZIO';
    dataInizioRiduzioneCuneo date;
BEGIN
   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   dataOdierna:=sysdate;
   aDataIniEsercizio:=TO_DATE('0101' || inEsercizioConguaglio,'DDMMYYYY');
   aDataFinEsercizio:=TO_DATE('3112' || inEsercizioConguaglio,'DDMMYYYY');
   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Eliminazione delle associazioni al compenso congualio presenti in ASS_COMPENSO_CONGUAGLIO e in
   -- RATEIZZA_CLASSIFIC_CORI all'ingresso della peocedura di abilita conguaglio. La cancellazione
   -- serve in caso di rilancio, dall'on-line, del conguaglio stesso

   eliminaAssCompensoConguaglio(inCdsConguaglio,
                                inUoConguaglio,
                                inEsercizioConguaglio,
                                inPgConguaglio);

   -------------------------------------------------------------------------------------------------
   -- Lettura dati di base legati al conguaglio in calcolo, testata conguaglio e anagrafico.
   -- I dati sono memorizzati rispettivamente in aRecConguaglio e aRecAnagrafico

   getDatiBaseConguaglio(inCdsConguaglio,
                         inUoConguaglio,
                         inEsercizioConguaglio,
                         inPgConguaglio,
                         eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Attivazione della gestione dell'accantonamento delle addizionali territorio

   -- Lettura con lock della tabella RATEIZZA_CLASSIFIC_CORI (record definitivo)

   isStorico:='N';
   isTemporaneo:='N';
   getRateizzaAddTerritorio(inEsercizioConguaglio,
                            inCdsConguaglio,
                            inUoConguaglio,
                            inPgConguaglio,
                            aRecAnagrafico.cd_anag,
                            isStorico,
                            isTemporaneo,
                            eseguiLock,
                            aRecRateizzaClassificCoriC0,
                            aRecRateizzaClassificCoriP0,
                            aRecRateizzaClassificCoriR0);

   -------------------------------------------------------------------------------------------------
   -- Verifica che esistono compensi che possono essere inclusi in un conguaglio

   -- Non esistono compensi riferiti al soggetto anagrafico in elaborazione che risultano pagati
   -- nell'esercizio del conguaglio

   BEGIN


      SELECT COUNT(*) INTO esisteCompenso
      FROM   DUAL
      WHERE  EXISTS
             (SELECT 1
              FROM   V_COMPENSO_CONGUAGLIO_BASE A
              WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
                     A.esercizio_compenso <= inEsercizioConguaglio AND
                     A.dt_emissione_mandato >= aDataIniEsercizio AND
                     A.dt_emissione_mandato <= aDataFinEsercizio AND
                     A.stato_cofi = 'P');

      IF esisteCompenso = 0 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Non è stato trovato alcun compenso eleggibile a conguaglio per anagrafico ' ||
             aRecAnagrafico.cd_anag );
      END IF;

   END;

   -- Non esistono compensi non associati a precedenti conguagli che risultano pagati nell'esercizio
   -- del conguaglio

   BEGIN

      SELECT COUNT(*) INTO esisteCompenso
      FROM   DUAL
      WHERE  EXISTS
             (SELECT 1
              FROM   V_COMPENSO_CONGUAGLIO_BASE A
              WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
                     A.esercizio_compenso <= inEsercizioConguaglio AND
                     A.dt_emissione_mandato >= aDataIniEsercizio AND
                     A.dt_emissione_mandato <= aDataFinEsercizio AND
                     A.is_associato_conguaglio = 'N' AND
                     A.stato_cofi = 'P');

      IF esisteCompenso = 0 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Tutti i compensi eleggibili a conguaglio risultano essere già stati conguagliati per anagrafico ' ||
             aRecAnagrafico.cd_anag);
      END IF;

   END;

   -- Verifica che esistono compensi non pagati

   -- Non devono esistere compensi riferiti al soggetto anagrafico in elaborazione non ancora pagati
   -- nell'esercizio del conguaglio

   BEGIN

      SELECT COUNT(*) INTO esisteCompenso
      FROM   DUAL
      WHERE  EXISTS
             (SELECT 1
              FROM   V_COMPENSO_CONGUAGLIO_BASE A
              WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
                     A.esercizio_compenso <= inEsercizioConguaglio AND
                     A.stato_cofi Not In ('A','P') And
                     A.is_compenso_conguaglio = 'N');

      IF esisteCompenso > 0 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Esistono Compensi non ancora pagati per anagrafico ' ||
             aRecAnagrafico.cd_anag );
      END IF;

   END;

   -- Verifico se esistono compensi con trattamento avente fl_agevolazioni_cervelli = 'Y'

   BEGIN

      SELECT COUNT(*) INTO esisteCompenso
      FROM   DUAL
      WHERE  EXISTS
             (SELECT 1
              FROM   V_COMPENSO_CONGUAGLIO_BASE A, TIPO_TRATTAMENTO T
              WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
                     A.esercizio_compenso <= inEsercizioConguaglio AND
                     A.dt_emissione_mandato >= aDataIniEsercizio AND
                     A.dt_emissione_mandato <= aDataFinEsercizio AND
                     A.is_associato_conguaglio = 'N' AND
                     A.stato_cofi = 'P' And
                     A.cd_trattamento = T.cd_trattamento And
                     T.fl_agevolazioni_cervelli ='Y');

      If esisteCompenso = 0 Then
          glbEsisteCompensoCervelli := 'N';
      Else
          glbEsisteCompensoCervelli := 'Y';
      End If;

   END;

   -------------------------------------------------------------------------------------------------
   -- Recupero del valore utente su ANAGRAFICO_ESERCIZIO delle detrazioni personali

   imDetrazioniPerAnag:=CNRCTB080.getAnagImDetrazionePer(inEsercizioConguaglio,
                                                         aRecAnagrafico.cd_anag);

   -------------------------------------------------------------------------------------------------
   -- Verifico esistenza di un precedente conguaglio. Se esiste si recuperano i dati esterni di questo
   -- altrimenti si propongono azzerati

   BEGIN

      SELECT * INTO aRecCompensoConguaglioBase
      FROM   V_COMPENSO_CONGUAGLIO_BASE A
      WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
             A.esercizio_compenso = inEsercizioConguaglio AND
             A.esercizio_conguaglio = inEsercizioConguaglio AND
             A.is_compenso_conguaglio = 'Y' AND
             A.stato_cofi != 'A' AND
             A.dacr_conguaglio =
                (SELECT MAX(B.dacr_conguaglio)
                 FROM   V_COMPENSO_CONGUAGLIO_BASE B
                 WHERE  B.cd_anag = A.cd_anag AND
                        B.esercizio_compenso = A.esercizio_compenso AND
                        B.esercizio_conguaglio = A.esercizio_conguaglio AND
                        B.is_compenso_conguaglio = A.is_compenso_conguaglio AND
                        B.stato_cofi != 'A');

      BEGIN

         -- Se il compenso associato all'ultimo conguaglio non risulta pagato allora impedisco
         -- l'esecuzione di un nuovo conguaglio

         IF aRecCompensoConguaglioBase.stato_cofi != 'P' THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Non è possibile eseguire un conguaglio se il precedente non risulta essere in stato pagato');
         END IF;

         aRecConguaglioUltimo:=CNRCTB545.getConguaglio(aRecCompensoConguaglioBase.cd_cds_conguaglio,
                                                       aRecCompensoConguaglioBase.cd_uo_conguaglio,
                                                       aRecCompensoConguaglioBase.esercizio_conguaglio,
                                                       aRecCompensoConguaglioBase.pg_conguaglio,
                                                       eseguiLock);

         UPDATE CONGUAGLIO
         SET    codice_fiscale_esterno=aRecConguaglioUltimo.codice_fiscale_esterno,
                dt_da_competenza_esterno=aRecConguaglioUltimo.dt_da_competenza_esterno,
                dt_a_competenza_esterno=aRecConguaglioUltimo.dt_a_competenza_esterno,
                imponibile_fiscale_esterno=aRecConguaglioUltimo.imponibile_fiscale_esterno,
                im_irpef_esterno=aRecConguaglioUltimo.im_irpef_esterno,
                im_addreg_esterno=aRecConguaglioUltimo.im_addreg_esterno,
                im_addprov_esterno=aRecConguaglioUltimo.im_addprov_esterno,
                im_addcom_esterno=aRecConguaglioUltimo.im_addcom_esterno,
                detrazioni_la_esterno=aRecConguaglioUltimo.detrazioni_la_esterno,
                detrazioni_pe_esterno=aRecConguaglioUltimo.detrazioni_pe_esterno,
                detrazioni_co_esterno=aRecConguaglioUltimo.detrazioni_co_esterno,
                detrazioni_fi_esterno=aRecConguaglioUltimo.detrazioni_fi_esterno,
                detrazioni_al_esterno=aRecConguaglioUltimo.detrazioni_al_esterno,
                detrazione_rid_cuneo_esterno=aRecConguaglioUltimo.detrazione_rid_cuneo_esterno,
                im_detrazione_personale_anag=imDetrazioniPerAnag
         WHERE  cd_cds = aRecConguaglio.cd_cds AND
                cd_unita_organizzativa = aRecConguaglio.cd_unita_organizzativa AND
                esercizio = aRecConguaglio.esercizio AND
                pg_conguaglio = aRecConguaglio.pg_conguaglio;

      END;

   EXCEPTION

      WHEN no_data_found THEN

           UPDATE CONGUAGLIO
           SET    codice_fiscale_esterno=NULL,
                  dt_da_competenza_esterno=NULL,
                  dt_a_competenza_esterno=NULL,
                  imponibile_fiscale_esterno=0,
                  im_irpef_esterno=0,
                  im_addreg_esterno=0,
                  im_addprov_esterno=0,
                  im_addcom_esterno=0,
                  detrazioni_la_esterno=0,
                  detrazioni_pe_esterno=0,
                  detrazioni_co_esterno=0,
                  detrazioni_fi_esterno=0,
                  detrazioni_al_esterno=0,
                  detrazione_rid_cuneo_esterno=0,
                  im_detrazione_personale_anag=imDetrazioniPerAnag
           WHERE  cd_cds = aRecConguaglio.cd_cds AND
                  cd_unita_organizzativa = aRecConguaglio.cd_unita_organizzativa AND
                  esercizio = aRecConguaglio.esercizio AND
                  pg_conguaglio = aRecConguaglio.pg_conguaglio;

   END;

END abilitaConguaglio;

-- =================================================================================================
-- Elaborazione del compenso per conguaglio
-- =================================================================================================
PROCEDURE creaCompensoConguaglio
   (
    inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
    inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE
   ) IS

   aImDetrazioniPe NUMBER(15,2);
   aImDetrazioniLa NUMBER(15,2);
   --aImDetrazioniCo NUMBER(15,2);
   --aImDetrazioniFi NUMBER(15,2);
   --aImDetrazioniAl NUMBER(15,2);
   aImTotDetrazioniPe NUMBER(15,2);
   aImTotDetrazioniLa NUMBER(15,2);
   aImTotDetrazioniCo NUMBER(15,2);
   aImTotDetrazioniFi NUMBER(15,2);
   aImTotDetrazioniFiS NUMBER(15,2);
   aImTotDetrazioniAl NUMBER(15,2);
   aImTotDetrazioniNettoPe NUMBER(15,2);
   aImTotDetrazioniNettoLa NUMBER(15,2);
   aImTotDetrazioniNettoCo NUMBER(15,2);
   aImTotDetrazioniNettoFi NUMBER(15,2);
   aImTotDetrazioniNettoAl NUMBER(15,2);
   aImTotDetrazioniCuneo NUMBER(15,2) := 0;
   aImTotDetrazioniNettoCuneo NUMBER(15,2) := 0;

   aImTotaleDetrazioni NUMBER(15,2);
   aImTotaleNetto NUMBER(15,2);

   aImNettoIrpefDovuto NUMBER(15,2);
   aImNettoIrpefGoduto NUMBER(15,2);

   aImCompCongIrpefNetto NUMBER(15,2);
   aImCompCongFamilyNetto NUMBER(15,2);
   aImCompCongAddRegNetto NUMBER(15,2);
   aImCompCongAddProNetto NUMBER(15,2);
   aImCompCongAddComNetto NUMBER(15,2);
   aImCompCongCreditoIrpef NUMBER(15,2);
   aImponibileCompCong NUMBER(15,2);
   aAmmontareCompCong NUMBER(15,2);
   aAmmontareCompCongNetto NUMBER(15,2);
   aImSospesoCompCong NUMBER(15,2);

   aValoreMinScaglioneIrpef NUMBER(15,2);
   aValoreMaxScaglioneIrpef NUMBER(15,2);

   aImportoRifDetrazioniPer NUMBER(15,2);
   aImportoRifDetrazioniFam NUMBER(15,2);
   -- non serve distinguere aImportoRifDetrazioniPer e aImportoRifDetrazioniFam
   aImportoRifDetrazioni NUMBER(15,2);
   aTotRedditoComplessivo   NUMBER(15,2);

   isStorico CHAR(1);
   isTemporaneo CHAR(1);
   eseguiLock CHAR(1);
   numeroGG INTEGER;
   --numero_mesi_totali INTEGER;
   --aDifferenzaMesi INTEGER;
   --numero_mesi_anno_prec_validi INTEGER;
   aContaFiSenzaConiuge INTEGER;
   esisteFiSenzaConiuge CHAR(1);

   i BINARY_INTEGER;
   k BINARY_INTEGER;

   aRecCompenso COMPENSO%ROWTYPE;
   aRecCoriConguaglio V_CORI_CONGUAGLIO%ROWTYPE;
   aRecRateizzaClassificCoriC0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriP0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriR0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

  isChiavePrimariaConguaglio CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'CONGUAGLIO';
   isLimiteImportoEmisMandato CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'IMPORTO_LIMITE_EMISSIONE_MANDATO';

   importoLimite CONFIGURAZIONE_CNR.im01%Type;

   gen_cur_cori GenericCurTyp;

   aDataSospensioneMin  date;
   aDataSospensioneMax  date;

   aDataMin date;
   aDataMax date;
   i_credito number := 0;
   dataInizioGestioneCuneoFiscale date;

BEGIN

   dataInizioGestioneCuneoFiscale := CNRCTB015.getDt01PerChiave('0', 'RIDUZIONE_CUNEO_DL_3_2020', 'DATA_INIZIO');
   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   tabCreditoIrpef.DELETE;
   dataOdierna:=sysdate;
   aDataIniEsercizio:=TO_DATE('0101' || inEsercizioConguaglio,'DDMMYYYY');
   aDataFinEsercizio:=TO_DATE('3112' || inEsercizioConguaglio,'DDMMYYYY');
   eseguiLock:='Y';

   glbImponibileLordoPercipiente:=0;
   glbTotalePrevidInail:=0;

   glbImponibileLordoIrpef:=0;
   glbImponibileLordoDetFam:=0;
   glbImponibileLordoDetPer:=0;
   glbImponibileNettoIrpef:=0;
   glbImponibileNettoDetFam:=0;
   glbImponibileNettoDetPer:=0;
   glbDeduzioneIrpef:=0;
   glbDeduzioneFamily:=0;

   glbDataMinCompetenza:=NULL;
   glbDataMaxCompetenza:=NULL;

   glbNumeroGiorni:=0;

   aImDetrazioniPe:=0;
   aImDetrazioniLa:=0;
   --aImDetrazioniCo:=0;
   --aImDetrazioniFi:=0;
   --aImDetrazioniAl:=0;
   aImTotDetrazioniPe:=0;
   aImTotDetrazioniLa:=0;
   aImTotDetrazioniCo:=0;
   aImTotDetrazioniFi:=0;
   aImTotDetrazioniFiS:=0;
   aImTotDetrazioniAl:=0;
   aImTotDetrazioniCuneo :=0;
   aValoreMinScaglioneIrpef:=0;
   aValoreMaxScaglioneIrpef:=0;

   aImportoRifDetrazioniPer:=0;
   aImportoRifDetrazioniFam:=0;
   aImportoRifDetrazioni:=0;

   tabella_date.DELETE;
   tabella_date_ok.DELETE;
   tabella_date_fam.DELETE;
   tabella_date_fam_ok.DELETE;
   tabella_date_fam_ok_unite.DELETE;
   tabella_date_per.DELETE;
   tabella_date_per_ok.DELETE;
   tabella_date_neg.DELETE;
   tabella_date_neg_ok.DELETE;
   tabella_date_fam_neg.DELETE;
   tabella_date_fam_neg_ok.DELETE;
   tabella_date_per_neg.DELETE;
   tabella_date_per_neg_ok.DELETE;


   aDataSospensioneMin := Trunc(CNRCTB015.getDt01PerChiave('SOSPENSIONE_IRPEF', 'PERIODO_VALIDITA'));
   aDataSospensioneMax := Trunc(CNRCTB015.getDt02PerChiave('SOSPENSIONE_IRPEF', 'PERIODO_VALIDITA'));
   -------------------------------------------------------------------------------------------------
   -- Lettura dati di base legati al conguaglio in calcolo, testata conguaglio e anagrafico.
   -- I dati sono memorizzati rispettivamente in aRecConguaglio e aRecAnagrafico

   getDatiBaseConguaglio(inCdsConguaglio,
                         inUoConguaglio,
                         inEsercizioConguaglio,
                         inPgConguaglio,
                         eseguiLock);

   -- Recupero dati del tipo trattamento indicato nel conguaglio

   aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento (aRecConguaglio.cd_trattamento,
                                                      aRecConguaglio.dt_registrazione);

   -------------------------------------------------------------------------------------------------
   -- Attivazione della gestione dell'accantonamento delle addizionali territorio

   -- Lettura con lock della tabella RATEIZZA_CLASSIFIC_CORI. I record sono sempre letti anche in caso
   -- di conguaglio che non prevede l'attivazione dell'accantonamento delle addizionali territorio

   isStorico:='N';
   isTemporaneo:='N';
   getRateizzaAddTerritorio(inEsercizioConguaglio,
                            inCdsConguaglio,
                            inUoConguaglio,
                            inPgConguaglio,
                            aRecAnagrafico.cd_anag,
                            isStorico,
                            isTemporaneo,
                            eseguiLock,
                            aRecRateizzaClassificCoriC0,
                            aRecRateizzaClassificCoriP0,
                            aRecRateizzaClassificCoriR0);

   -------------------------------------------------------------------------------------------------
   -- Determinazione del goduto del nuovo conguaglio. Si attivano i seguenti passi:
   -- 1) Si elabora, se esiste, l'ultimo conguaglio presente nel sistema per il soggetto anagrafico in
   --    elaborazione. Le informazioni definite nelle variabili _DOVUTO sono portate in _GODUTO del
   --    nuovo conguaglio.
   -- 2) Si elaborano tutti i compensi registrati per il soggetto anagrafico in elaborazione. Da questa
   --    elaborazione si estraggono gli intervalli temporali per il ricalcolo delle detrazioni familiari e
   --    personali, gli imponibili di riferimento per l'elaborazione. Solo i compensi mai compresi in
   --    un conguaglio concorrono alla formazione del _GODUTO

   -- Elaborazione, se esiste, dell'ultimo conguaglio

   getUltimoConguaglio(inEsercizioConguaglio);

   -- Lettura di tutti i compensi per la determinazione del conguaglio e scritura della tabella di
   -- relazione

   leggiCompensiPerConguaglio;

   -------------------------------------------------------------------------------------------------
   -- Aggiungo i dati esterni

   glbImponibileLordoIrpef:=glbImponibileLordoIrpef + aRecConguaglio.imponibile_fiscale_esterno;
   glbImponibileNettoIrpef:=glbImponibileNettoIrpef + aRecConguaglio.imponibile_fiscale_esterno;

   glbImponibileLordoPercipiente:=glbImponibileLordoPercipiente + aRecConguaglio.imponibile_fiscale_esterno;

   -------------------------------------------------------------------------------------------------
   -- Elaborazione date di competenza dei compensi in calcolo

   -- Se esistono dati esterni, aggiungo le date di competenza esterne alla tabella per le detrazioni familiari
   If aRecConguaglio.dt_da_competenza_esterno Is Not Null And aRecConguaglio.dt_a_competenza_esterno Is Not Null Then
        CNRCTB545.componiMatriceDate(tabella_date_fam,
                                     aRecConguaglio.dt_da_competenza_esterno,
                                     aRecConguaglio.dt_a_competenza_esterno,
                                     'F',
                                     'Y');
        CNRCTB545.componiMatriceDate(tabella_date_per,
                                     aRecConguaglio.dt_da_competenza_esterno,
                                     aRecConguaglio.dt_a_competenza_esterno,
                                     'L',
                                     'Y');
   End If;

   -- Azzeramento tabella date negative per recupero rate se non ci sono intervalli ordinari

   IF tabella_date.COUNT = 0 THEN
      tabella_date_neg.DELETE;
   END IF;
   IF tabella_date_fam.COUNT = 0 THEN
      tabella_date_fam_neg.DELETE;
   END IF;
   IF tabella_date_per.COUNT = 0 THEN
      tabella_date_per_neg.DELETE;
   END IF;

   -- Normalizzazione delle date per includere il recupero delle rate ed eliminare periodi di eventuale
   -- sovrapposizione

   IF tabella_date.COUNT > 0 THEN
      scaricaRecuperoRate(tabella_date,
                          tabella_date_neg);
      normalizzaMatriceDate(tabella_date,
                            tabella_date_ok);
      chkSpuriRecuperoRate(tabella_date_neg);
   END IF;
   IF tabella_date_fam.COUNT > 0 THEN
      scaricaRecuperoRate(tabella_date_fam,
                          tabella_date_fam_neg);
      normalizzaMatriceDate(tabella_date_fam,
                            tabella_date_fam_ok);
      unisciMatriceDate(inEsercizioCompenso,
                        tabella_date_fam_ok,
                        tabella_date_fam_ok_unite);
      chkSpuriRecuperoRate(tabella_date_fam_neg);
   END IF;
   IF tabella_date_per.COUNT > 0 THEN
      scaricaRecuperoRate(tabella_date_per,
                          tabella_date_per_neg);
      normalizzaMatriceDate(tabella_date_per,
                            tabella_date_per_ok);
      chkSpuriRecuperoRate(tabella_date_per_neg);
   END IF;

   -- Calcolo del numero di giorni per date competenza compensi (tutti)

   glbNumeroGiorni:=CNRCTB545.getGiorniMatriceDate(tabella_date_ok);
   aDataMax:=CNRCTB545.getMassimaMatriceDate(tabella_date_ok);
   aDataMin:=CNRCTB545.getMinimaMatriceDate(tabella_date_ok);

   -------------------------------------------------------------------------------------------------
   -- Inserimento ed elaborazione del compenso

   aRecCompenso:=NULL;
   aRecCompenso.cd_cds:=inCdsCompenso;
   aRecCompenso.cd_unita_organizzativa:=inUoCompenso;
   aRecCompenso.esercizio:=inEsercizioCompenso;
   aRecCompenso.pg_compenso:=inPgCompenso;
   aRecCompenso.cd_cds_origine:=inCdsCompenso;
   aRecCompenso.cd_uo_origine:=inUoCompenso;
   aRecCompenso.dt_registrazione:=aRecConguaglio.dt_registrazione;
   aRecCompenso.ds_compenso:='COMPENSO DA CONGUAGLIO N. ' || substr(aRecConguaglio.ds_conguaglio,1,270);
   aRecCompenso.ti_anagrafico:=aRecConguaglio.ti_anagrafico;
   aRecCompenso.cd_terzo:=aRecConguaglio.cd_terzo;
   aRecCompenso.ragione_sociale:=aRecConguaglio.ragione_sociale;
   aRecCompenso.nome:=aRecConguaglio.nome;
   aRecCompenso.cognome:=aRecConguaglio.cognome;
   aRecCompenso.codice_fiscale:=aRecConguaglio.codice_fiscale;
   aRecCompenso.partita_iva:=aRecConguaglio.partita_iva;
   aRecCompenso.cd_termini_pag:=aRecConguaglio.cd_termini_pag;
   aRecCompenso.cd_modalita_pag:=aRecConguaglio.cd_modalita_pag;
   aRecCompenso.pg_banca:=aRecConguaglio.pg_banca;
   aRecCompenso.cd_tipo_rapporto:=aRecConguaglio.cd_tipo_rapporto;
   aRecCompenso.cd_trattamento:=aRecConguaglio.cd_trattamento;
   aRecCompenso.fl_senza_calcoli:='N';
   aRecCompenso.fl_diaria:='N';
   aRecCompenso.stato_cofi:='C';
   aRecCompenso.stato_coge:='N';
   aRecCompenso.stato_coan:='N';
   aRecCompenso.ti_associato_manrev:='N';
   aRecCompenso.dt_da_competenza_coge:=glbDataMinCompetenza;
   aRecCompenso.dt_a_competenza_coge:=glbDataMaxCompetenza;
   aRecCompenso.stato_pagamento_fondo_eco:='N';
   aRecCompenso.im_totale_compenso:=0;

   If aRecTipoTrattamento.fl_agevolazioni_cervelli = 'Y' Or glbEsisteCompensoCervelli = 'Y' or
      --aRecTipoTrattamento.cd_trattamento in ('T162','T163','CONGL20','CONGL30')
      aRecTipoTrattamento.fl_agevolazioni_rientro_lav = 'Y' Then
      aRecCompenso.im_lordo_percipiente:=glbImponibileLordoPercipiente;
      aRecCompenso.aliquota_fiscale := '100';
   Else
      aRecCompenso.im_lordo_percipiente:=glbImponibileLordoIrpef;
   End If;
   aRecCompenso.im_netto_percipiente:=0;

   If aRecTipoTrattamento.fl_agevolazioni_cervelli = 'Y' Or glbEsisteCompensoCervelli = 'Y' or
      --aRecTipoTrattamento.cd_trattamento in ('T162','T163','CONGL20','CONGL30')
      aRecTipoTrattamento.fl_agevolazioni_rientro_lav = 'Y' Then
      aRecCompenso.im_cr_percipiente:=glbTotalePrevidInail;
   Else
      aRecCompenso.im_cr_percipiente:=0;
   End If;

   aRecCompenso.im_cr_ente:=0;
   aRecCompenso.quota_esente:=0;
   aRecCompenso.quota_esente_no_iva:=0;
   aRecCompenso.im_no_fiscale:=0;
   aRecCompenso.imponibile_fiscale:=0;
   aRecCompenso.imponibile_iva:=0;
   aRecCompenso.pg_comune_add:=aRecConguaglio.pg_comune;
   aRecCompenso.cd_provincia_add:=aRecConguaglio.cd_provincia;
   aRecCompenso.cd_regione_add:=aRecConguaglio.cd_regione;
   aRecCompenso.dacr:=aRecConguaglio.dacr;
   aRecCompenso.utcr:=aRecConguaglio.utcr;
   aRecCompenso.duva:=aRecConguaglio.duva;
   aRecCompenso.utuv:=aRecConguaglio.utuv;
   aRecCompenso.pg_ver_rec:=aRecConguaglio.pg_ver_rec;
   aRecCompenso.detrazioni_personali:=0;
   aRecCompenso.detrazioni_la:=0;
   aRecCompenso.detrazione_coniuge:=0;
   aRecCompenso.detrazione_figli:=0;
   aRecCompenso.detrazione_altri:=0;
   aRecCompenso.detrazione_riduzione_cuneo:=0;
   aRecCompenso.detrazioni_personali_netto:=0;
   aRecCompenso.detrazioni_la_netto:=0;
   aRecCompenso.detrazione_coniuge_netto:=0;
   aRecCompenso.detrazione_figli_netto:=0;
   aRecCompenso.detrazione_altri_netto:=0;
   aRecCompenso.detrazione_rid_cuneo_netto:=0;
   aRecCompenso.imponibile_inail:=0;
   aRecCompenso.fl_generata_fattura:='N';
   IF aRecTipoTrattamento.ti_commerciale = 'Y' THEN
      aRecCompenso.ti_istituz_commerc:='C';
   ELSE
      aRecCompenso.ti_istituz_commerc:='I';
   END IF;
   aRecCompenso.fl_compenso_stipendi:='N';
   aRecCompenso.fl_compenso_conguaglio:='Y';
   aRecCompenso.fl_compenso_minicarriera:='N';
   aRecCompenso.aliquota_irpef_da_missione:=0;
   aRecCompenso.fl_compenso_mcarriera_tassep:='N';
   aRecCompenso.aliquota_irpef_tassep:=0;
   aRecCompenso.im_deduzione_irpef:=0;
   aRecCompenso.imponibile_fiscale_netto:=0;
   aRecCompenso.numero_giorni:=glbNumeroGiorni;
   aRecCompenso.fl_escludi_qvaria_deduzione:=aRecConguaglio.fl_escludi_qvaria_deduzione;
   aRecCompenso.fl_intera_qfissa_deduzione:=aRecConguaglio.fl_intera_qfissa_deduzione;
   aRecCompenso.im_detrazione_personale_anag:=aRecConguaglio.im_detrazione_personale_anag;
   aRecCompenso.fl_recupero_rate:='N';
   aRecCompenso.fl_accantona_add_terr:=aRecConguaglio.fl_accantona_add_terr;

   CNRCTB545.insCompenso(aRecCompenso);

   CNRCTB550.elaboraCompenso(inCdsCompenso,
                             inUoCompenso,
                             inEsercizioCompenso,
                             inPgCompenso,
                             NULL,
                             NULL,
                             TO_NUMBER(NULL),
                             TO_NUMBER(NULL),
                             NULL,
                             NULL,
                             TO_NUMBER(NULL),
                             TO_NUMBER(NULL),
                             NULL,
                             NULL,
                             TO_NUMBER(NULL),
                             TO_NUMBER(NULL));

   -------------------------------------------------------------------------------------------------
   -- Rilettura del compenso per il recupero dell'imponibile fiscale netto e delle deduzioni godute
   -- dopo il calcolo del compenso

   -- Inserimento della prima associazione compenso conguaglio

   componiAssCompensoConguaglio(inCdsCompenso,
                                inUoCompenso,
                                inEsercizioCompenso,
                                inPgCompenso);

   aRecCompenso:=CNRCTB545.getCompenso(inCdsCompenso,
                                       inUoCompenso,
                                       inEsercizioCompenso,
                                       inPgCompenso,
                                       eseguiLock);

   glbImponibileLordoIrpef:=aRecCompenso.imponibile_fiscale;
   glbImponibileNettoIrpef:=aRecCompenso.imponibile_fiscale_netto;
   glbDeduzioneIrpef:=aRecCompenso.im_deduzione_irpef;

   -------------------------------------------------------------------------------------------------
   -- Determinazione dei valori di riferimento alle detrazioni personali o familiari
   -- è compreso anche l'eventuale imponibile esterno
   aImportoRifDetrazioniPer:=glbImponibileLordoIrpef;
   aImportoRifDetrazioniFam:=glbImponibileLordoIrpef;
   aImportoRifDetrazioni:=glbImponibileLordoIrpef;

   --Calcolo il totale del reddito complessivo
   --Aggiungo i redditi presenti nelle anagrafiche e quelli eventualmente come DIP
--pipe.send_message('aImportoRifDetrazioni = '||aImportoRifDetrazioni);
--pipe.send_message('glbRedditoComplessivo = '||glbRedditoComplessivo);
--pipe.send_message('glbImponibilePagatoDip = '||glbImponibilePagatoDip);

   aTotRedditoComplessivo := aImportoRifDetrazioni
                           + glbRedditoComplessivo
                           + glbImponibilePagatoDip;

--pipe.send_message('aTotRedditoComplessivo = '||aTotRedditoComplessivo);
   -------------------------------------------------------------------------------------------------
   -- Calcolo detrazioni familiari
   Begin
    If glbFlNoDetrazioniFamily != 'Y' And
       aRecCompenso.ti_anagrafico = 'A' Then
      IF tabella_date_fam_ok_unite.COUNT > 0 THEN
         --controllo se esiste un figlio in assenza di coniuge
         Begin
            Select Count(1)
            Into aContaFiSenzaConiuge
            From CARICO_FAMILIARE_ANAG
            Where  cd_anag = aRecAnagrafico.cd_anag And
                   ti_persona = 'F' And
                   fl_primo_figlio_manca_con = 'Y' And
                   (
                       (dt_ini_validita <= dataOdierna AND
                        dt_fin_validita >= dataOdierna)
                    OR
                       (dt_ini_validita <= dataOdierna AND
                        dt_fin_validita >= dataOdierna)
                    ) And
                    Not Exists
                    (Select '1'
                     From CARICO_FAMILIARE_ANAG
                     Where cd_anag = aRecAnagrafico.cd_anag And
                           ti_persona = 'C' And
                           (
                             (dt_ini_validita <= dataOdierna AND
                              dt_fin_validita >= dataOdierna)
                            OR
                             (dt_ini_validita <= dataOdierna AND
                              dt_fin_validita >= dataOdierna)
                             )
                           );
         Exception
             When Others Then
                 aContaFiSenzaConiuge := 0;
         End;
         -- Se non ci sono figli senza coniuge oppure ce ne sono più di uno, calcolo le detrazioni normalmente
         If aContaFiSenzaConiuge != 1 Then --cioè se non ci sono figli senza coniuge oppure ce ne sono più di uno
            esisteFiSenzaConiuge := 'N';
            calcolaDetFam(inEsercizioConguaglio,
                          aTotRedditoComplessivo,
                          aRecAnagrafico.cd_anag,
                          aImTotDetrazioniCo,
                          aImTotDetrazioniFi,
                          aImTotDetrazioniAl,
                          aImTotDetrazioniFiS,
                          esisteFiSenzaConiuge);

         Else
            esisteFiSenzaConiuge := 'Y';
            --inserisco coniuge fittizio al 100%
            Begin
               Insert into carico_familiare_anag(CD_ANAG,PG_CARICO_ANAG,DT_INI_VALIDITA,TI_PERSONA,CODICE_FISCALE,
                                                 PRC_CARICO,DT_FIN_VALIDITA,FL_PRIMO_FIGLIO,DT_FINE_FIGLIO_HA_TREANNI,
                                                 FL_HANDICAP,DACR,UTCR,DUVA,UTUV,PG_VER_REC,DT_NASCITA_FIGLIO,FL_PRIMO_FIGLIO_MANCA_CON)
                                         Values (aRecAnagrafico.cd_anag,999999999,aDataIniEsercizio,'G','.',
                                                 100,aDataFinEsercizio,'N',Null,
                                                 'N',dataOdierna,'FITTIZIO',dataOdierna,'FITTIZIO',1,Null,'N');
            Exception
                When Dup_Val_On_Index Then
                    Null;
            End;
            calcolaDetFam(inEsercizioConguaglio,
                          aTotRedditoComplessivo,
                          aRecAnagrafico.cd_anag,
                          aImTotDetrazioniCo,
                          aImTotDetrazioniFi,
                          aImTotDetrazioniAl,
                          aImTotDetrazioniFiS,
                          esisteFiSenzaConiuge);

            --Gestiti i seguenti importi
            --aImTotDetrazioniFi          Detrazioni per tutti i figli
            --aImTotDetrazioniFiS         Detrazioni per il figlio S
            --aImTotDetrazioniCo    Detrazione per il coniuge fittizio
            If aImTotDetrazioniFiS >= aImTotDetrazioniCo Then
                aImTotDetrazioniFi := aImTotDetrazioniFi;
            Else
                aImTotDetrazioniFi := aImTotDetrazioniFi - aImTotDetrazioniFiS + aImTotDetrazioniCo;
            End If;

            aImTotDetrazioniCo:=0;
            --elimino coniuge fittizio
            Delete carico_familiare_anag
            Where CD_ANAG = aRecAnagrafico.cd_anag
              And PG_CARICO_ANAG = 999999999
              And TI_PERSONA = 'G';

         End If;
      END IF;
    End If;
   END;

   -------------------------------------------------------------------------------------------------
   -- Calcolo detrazioni personali
   Declare
      maggiorazione     NUMBER(15,2):=0;
      detrazione_minima NUMBER(15,2):=0;
   BEGIN
    If glbFlNoDetrazioniAltre != 'Y' And
       aRecCompenso.ti_anagrafico = 'A' Then

      IF tabella_date_per_ok.COUNT > 0 THEN

         aImDetrazioniPe:=0;

         If glbNumeroGiorni >= 365 then
            CNRCTB550.calcolaDetrazioniPer(aTotRedditoComplessivo,
                                           aRecAnagrafico.cd_anag,
                                           TO_DATE('0101' || aRecCompenso.esercizio,'DDMMYYYY'),
                                           TO_DATE('3112' || aRecCompenso.esercizio,'DDMMYYYY'),
                                           aRecConguaglio.dt_registrazione,
                                           aRecConguaglio.esercizio,
                                           aImDetrazioniPe);

            aImTotDetrazioniPe:=aImTotDetrazioniPe + aImDetrazioniPe;
         else

            FOR i IN tabella_date_per_ok.FIRST .. tabella_date_per_ok.LAST

            LOOP

               CNRCTB550.calcolaDetrazioniPer(aTotRedditoComplessivo,   --aImportoRifDetrazioniPer,
                                           aRecAnagrafico.cd_anag,
                                           tabella_date_per_ok(i).tDataDa,
                                           tabella_date_per_ok(i).tDataA,
--                                           glbDataMinCompetenza,
--                                           glbDataMinCompetenza + numeroGG - 1,
                                           aRecConguaglio.dt_registrazione,
                                           aRecConguaglio.esercizio,
                                           aImDetrazioniPe);

               aImTotDetrazioniPe:=aImTotDetrazioniPe + aImDetrazioniPe;
            END LOOP;
         end if;
         --DEVO AGGIUNGERE L'EVENTUALE MAGGIORAZIONE
         Begin
            Select Nvl(im_maggiorazione,0), Nvl(im_detrazione_minima,0)
            Into maggiorazione, detrazione_minima
            From DETRAZIONI_LAVORO
            Where  ti_lavoro = 'D' AND
                   dt_inizio_validita <= aRecConguaglio.dt_registrazione AND
                   dt_fine_validita >= aRecConguaglio.dt_registrazione AND
                   im_inferiore <= aTotRedditoComplessivo AND
                   im_superiore >= aTotRedditoComplessivo;
         Exception
              When No_Data_Found Then
                  maggiorazione:=0;
                  detrazione_minima:=0;
         End;

         aImTotDetrazioniPe:=aImTotDetrazioniPe + maggiorazione;

         --se il reddito è inferiore a 8000 euro e se il terzo lo ha richiesto,
         --gli viene applicata la detrazione massima
         If aTotRedditoComplessivo <= 8000 And glbFlApplicaDetrPersMax = 'Y' Then
            If aImTotDetrazioniPe < detrazione_minima Then
               aImTotDetrazioniPe := detrazione_minima;
            End If;
         End If;
      END IF;
    End If;
   END;
   -------------------------------------------------------------------------------------------------
   -- Calcolo detrazioni altri tipi (assegni alimentari)

   Begin
    If glbFlDetrazioniAltriTipi = 'Y' And
       aRecCompenso.ti_anagrafico = 'A' Then

      IF tabella_date_per_ok.COUNT > 0 THEN
            aImDetrazioniLa:=0;
            /*Non vanno rapportate al periodo effettivamente pagato - questo solo nel conguaglio*/
            CNRCTB550.calcolaDetrazioniAltriTipi(aTotRedditoComplessivo,   --aImportoRifDetrazioniPer,
                                           aRecAnagrafico.cd_anag,
                                           TO_DATE('0101' || aRecConguaglio.esercizio,'DDMMYYYY'),
                                           TO_DATE('3112' || aRecConguaglio.esercizio,'DDMMYYYY'),
                                           aRecConguaglio.dt_registrazione,
                                           aRecConguaglio.esercizio,
                                           aImDetrazioniLa);

               aImTotDetrazioniLa:=aImTotDetrazioniLa + aImDetrazioniLa;

      END IF;

    End If;

   END;

   -------------------------------------------------------------------------------------------------
   -- Lettura dei dettagli del compenso conguaglio per il recupero del dovuto

   BEGIN

      glbImportoIrpefDovuto:=0;
      glbImportoFamilyDovuto:=0;
      glbImportoAddRegDovuto:=0;
      glbImportoAddProDovuto:=0;
      glbImportoAddComDovuto:=0;
      glbDetrazioniPeDovuto:=0;
      glbDetrazioniLaDovuto:=0;
      glbDetrazioniCoDovuto:=0;
      glbDetrazioniFiDovuto:=0;
      glbDetrazioniAlDovuto:=0;
      glbDeduzioneIrpefDovuto:=0;
      glbDeduzioneFamilyDovuto:=0;
      glbImportoCreditoIrpefDovuto:=0;
      glbImportoCredIrpParDetDovuto:=0;
      glbImportoBonusIrpefDovuto:=0;

      glbImpAddRegRateEseprec:=0;
      glbImpAddProRateEseprec:=0;
      glbImpAddComRateEseprec:=0;

      OPEN gen_cur_cori FOR

           SELECT *
           FROM   V_CORI_CONGUAGLIO A
           WHERE  A.cd_cds = inCdsCompenso AND
                  A.cd_unita_organizzativa = inUoCompenso AND
                  A.esercizio = inEsercizioCompenso AND
                  A.pg_compenso = inPgCompenso
                  Order By A.cd_contributo_ritenuta Desc;     -- le addizionali devono essere processate per ultimo

      LOOP

         FETCH gen_cur_cori INTO
               aRecCoriConguaglio;

         EXIT WHEN gen_cur_cori%NOTFOUND;
--pipe.send_message('aRecCoriConguaglio.CD_CONTRIBUTO_RITENUTA '||aRecCoriConguaglio.CD_CONTRIBUTO_RITENUTA);
         IF CNRCTB545.IsCoriCreditoIrpef(aRecCoriConguaglio.cd_contributo_ritenuta) = 'Y'  then
            --per ora passo glbNumeroGiorni che è anche il numero gg del cud
--pipe.send_message('3 ');
   if aDataMin < to_date('01/01/'||aRecCompenso.Esercizio,'dd/mm/yyyy') then
    aDataMin := to_date('01/01/'||aRecCompenso.Esercizio,'dd/mm/yyyy');
   end if;
   if aDataMax > to_date('31/12/'||aRecCompenso.Esercizio,'dd/mm/yyyy') then
    aDataMax := to_date('31/12/'||aRecCompenso.Esercizio,'dd/mm/yyyy');
   end if;

            calcolaCreditoIrpefDovuto(glbImponibileNettoIrpef,
                                      glbNumeroGiorni,
                                      aRecCompenso,
                                      aDataMin,
                                      aDataMax,
                                      aRecCoriConguaglio.cd_contributo_ritenuta,
                                      aRecCoriConguaglio.dt_ini_validita,
                                      aImTotDetrazioniCuneo);
      FOR i_credito IN tabCreditoIrpef.FIRST .. tabCreditoIrpef.LAST LOOP
        if dataInizioGestioneCuneoFiscale <= tabCreditoIrpef(i_credito).tDtIniValCori then
          if tabCreditoIrpef(i_credito).tImCreditoIrpefDovuto > tabCreditoIrpef(i_credito).tImCreditoMaxDovuto then
            tabCreditoIrpef(i_credito).tImCreditoIrpefDovuto := tabCreditoIrpef(i_credito).tImCreditoMaxDovuto;
          end if;
        else
          if tabCreditoIrpef(i_credito).tImCreditoIrpefDovuto > tabCreditoIrpef(i_credito).tImCreditoMaxDovuto then
            tabCreditoIrpef(i_credito).tImCreditoIrpefDovuto := tabCreditoIrpef(i_credito).tImCreditoMaxDovuto;
          end if;
        end if;
      END LOOP;
--            creditoGiaCalcolato := 'S';
--pipe.send_message('glbImportoCreditoIrpefDovuto '||glbImportoCreditoIrpefDovuto);
         END IF;

         -- IRPEF ----------------------------------------------------------------------------------
         aImTotDetrazioniNettoCuneo:=aImTotDetrazioniCuneo;
         glbDetrazioniCuneoDovuto:=aImTotDetrazioniNettoCuneo;
         IF CNRCTB545.getIsIrpefScaglioni(aRecCoriConguaglio.cd_classificazione_cori,
                                          aRecCoriConguaglio.pg_classificazione_montanti,
                                          aRecCoriConguaglio.fl_scrivi_montanti) = 'Y' Then

            glbImportoIrpefDovuto:=aRecCoriConguaglio.ammontare_lordo;
            glbDeduzioneIrpefDovuto:=aRecCoriConguaglio.deduzione;
            glbDeduzioneFamilyDovuto:=aRecCoriConguaglio.deduzione_family;
            glbImportoFamilyDovuto:=aRecCoriConguaglio.deduzione_family;

            -- Eventuale nettizzazione delle detrazioni

            aImTotaleDetrazioni:=aImTotDetrazioniCo + aImTotDetrazioniFi + aImTotDetrazioniAl +
                                 aImTotDetrazioniPe + aImTotDetrazioniLa + aImTotDetrazioniCuneo;

            aImTotDetrazioniNettoPe:=aImTotDetrazioniPe;
            aImTotDetrazioniNettoLa:=aImTotDetrazioniLa;
            aImTotDetrazioniNettoCo:=aImTotDetrazioniCo;
            aImTotDetrazioniNettoFi:=aImTotDetrazioniFi;
            aImTotDetrazioniNettoAl:=aImTotDetrazioniAl;

            IF glbImportoIrpefDovuto < aImTotaleDetrazioni THEN
               aImTotaleNetto:=(glbImportoIrpefDovuto - aImTotaleDetrazioni) * -1;
               k := 0;
               IF tabCreditoIrpef.COUNT > 0 THEN
                  FOR K IN tabCreditoIrpef.FIRST .. tabCreditoIrpef.LAST LOOP
                     IF tabCreditoIrpef(K).PAREGGIO_DETRAZIONI = 'Y' THEN
                        IF aImTotaleNetto <= tabCreditoIrpef(K).tImCreditoIrpefDovuto * -1 THEN
                           tabCreditoIrpef(K).tImCreditoIrpefDovuto := aImTotaleNetto * -1;
                        END IF;
                     END IF;
                  END LOOP;
               END IF;

               nettizzaDetrazioni(aImTotaleNetto,
                                  aImTotDetrazioniNettoPe,
                                  aImTotDetrazioniNettoLa,
                                  aImTotDetrazioniNettoCo,
                                  aImTotDetrazioniNettoFi,
                                  aImTotDetrazioniNettoAl,
                                  aImTotDetrazioniNettoCuneo);

            ELSE
    -- SEGNA DI EVITARE DI SCRIVERE IL CUNEO PER IL PAREGGIO DETRAZIONI
              k := 0;
              IF tabCreditoIrpef.COUNT > 0 THEN
                  FOR K IN tabCreditoIrpef.FIRST .. tabCreditoIrpef.LAST LOOP
                    IF tabCreditoIrpef(K).PAREGGIO_DETRAZIONI = 'Y' THEN
                       tabCreditoIrpef(K).tImCreditoIrpefDovuto := 0;
                    END IF;
                  END LOOP;
              END IF;
            END IF;

            glbDetrazioniPeDovuto:=aImTotDetrazioniNettoPe;
            glbDetrazioniLaDovuto:=aImTotDetrazioniNettoLa;
            glbDetrazioniCoDovuto:=aImTotDetrazioniNettoCo;
            glbDetrazioniFiDovuto:=aImTotDetrazioniNettoFi;
            glbDetrazioniAlDovuto:=aImTotDetrazioniNettoAl;
            glbDetrazioniCuneoDovuto:=aImTotDetrazioniNettoCuneo;

         END IF;
         -- Addizionali regionali, provinciali e comunali ------------------------------------------

         IF    aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddReg Then
               If glbImportoIrpefDovuto > aImTotaleDetrazioni Then
                   glbImportoAddRegDovuto:=aRecCoriConguaglio.ammontare_lordo;
               End If;
         ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddPro Then
               If glbImportoIrpefDovuto > aImTotaleDetrazioni Then
                   glbImportoAddProDovuto:=aRecCoriConguaglio.ammontare_lordo;
               End If;
         ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddCom Then
               If glbImportoIrpefDovuto > aImTotaleDetrazioni Then
                   glbImportoAddComDovuto:=aRecCoriConguaglio.ammontare_lordo;
               End If;
         ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddRegRecRate THEN
               glbImpAddRegRateEseprec:=aRecCoriConguaglio.ammontare_lordo;
         ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddProRecRate THEN
               glbImpAddProRateEseprec:=aRecCoriConguaglio.ammontare_lordo;
         ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddComRecRate THEN
               glbImpAddComRateEseprec:=aRecCoriConguaglio.ammontare_lordo;
         END IF;

      END LOOP;

      CLOSE gen_cur_cori;
      FOR i_credito IN tabCreditoIrpef.FIRST .. tabCreditoIrpef.LAST LOOP
        if dataInizioGestioneCuneoFiscale <= tabCreditoIrpef(i_credito).tDtIniValCori then
           IF tabCreditoIrpef(i_credito).PAREGGIO_DETRAZIONI = 'Y' THEN
                glbImportoCredIrpParDetDovuto := glbImportoCredIrpParDetDovuto + tabCreditoIrpef(i_credito).tImCreditoIrpefDovuto;
                IF tabCreditoIrpef(i_credito).tImCreditoMaxDovuto IS NOT NULL AND
                   glbImportoCredIrpParDetDovuto > tabCreditoIrpef(i_credito).tImCreditoMaxDovuto THEN
                   glbImportoCredIrpParDetDovuto := tabCreditoIrpef(i_credito).tImCreditoMaxDovuto;
                END IF;
           else
                glbImportoCreditoIrpefDovuto := glbImportoCreditoIrpefDovuto + tabCreditoIrpef(i_credito).tImCreditoIrpefDovuto;
           end if;
        else
            glbImportoBonusIrpefDovuto := glbImportoBonusIrpefDovuto + tabCreditoIrpef(i_credito).tImCreditoIrpefDovuto;
        end if;
      END LOOP;

   END;

   Begin
      importoLimite:=CNRCTB015.getIm01PerChiave(0,
                                                isChiavePrimariaConguaglio,
                                                isLimiteImportoEmisMandato);

      -- Calcolo netto IRPEF e addizionali da portare sul COMPENSO

      aImNettoIrpefDovuto:=glbImportoIrpefDovuto - (glbDetrazioniPeDovuto + glbDetrazioniLaDovuto +
                                                    glbDetrazioniCoDovuto + glbDetrazioniFiDovuto +
                                                    glbDetrazioniAlDovuto + glbDetrazioniCuneoDovuto);

      aImNettoIrpefGoduto:=glbImportoIrpefGoduto - (glbDetrazioniPeGoduto + glbDetrazioniLaGoduto +
                                                    glbDetrazioniCoGoduto + glbDetrazioniFiGoduto +
                                                    glbDetrazioniAlGoduto + glbDetrazioniCuneoGoduto);

      aImCompCongIrpefNetto:=aImNettoIrpefDovuto - aImNettoIrpefGoduto - Nvl(aRecConguaglio.im_irpef_esterno,0);
      /* Se aImCompCongIrpefNetto  è maggiore di -0.49 e minore di -0.01 (cioè verrebbe emesso un mandato
         con un importo < 0.50) occorre annullare questa differenza facendo attenzione alle detrazioni */
      If  aImCompCongIrpefNetto < 0 And Abs(aImCompCongIrpefNetto) < Nvl(importoLimite,999999999999) Then
         --SE NON CI SONO DETRAZIONI (NETTO = LORDO), AZZERO ANCHE LA DIFFERENZA DELLE IMPOSTE LORDE CHE VANNO SUL CONGUAGLIO
         If aImNettoIrpefDovuto = glbImportoIrpefDovuto And aImNettoIrpefGoduto = glbImportoIrpefGoduto Then
            glbImportoIrpefDovuto := glbImportoIrpefGoduto + Nvl(aRecConguaglio.im_irpef_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_pe_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_la_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_co_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_fi_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_al_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazione_rid_cuneo_esterno,0);
         Else
            --ALTRIMENTI PER IL DOVUTO DEVO SEMPRE FARE IN MODO CHE IMPOSTA LORDA - DETRAZIONI = IMPOSTA NETTA
            glbImportoIrpefDovuto := glbImportoIrpefDovuto + Nvl(aRecConguaglio.im_irpef_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_pe_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_la_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_co_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_fi_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazioni_al_esterno,0) +
                                                             Nvl(aRecConguaglio.detrazione_rid_cuneo_esterno,0)
                                     + Abs(aImCompCongIrpefNetto);
         End If;
         aImCompCongIrpefNetto := 0;        -- viene poi aggiornato su CONTRIBUTO_RITENUTA
         aImNettoIrpefDovuto := aImNettoIrpefGoduto + Nvl(aRecConguaglio.im_irpef_esterno,0);
      End If;

      IF aImNettoIrpefDovuto >= 0 THEN
       	 glbImportoCredIrpParDetDovuto := 0;
      ELSIF glbImportoCredIrpParDetDovuto>0 AND glbImportoCredIrpParDetDovuto >= (aImNettoIrpefDovuto * -1) THEN
   	 	 glbImportoCredIrpParDetDovuto := aImNettoIrpefDovuto * -1;
      END IF;

      aImCompCongFamilyNetto:=glbImportoFamilyDovuto - glbImportoFamilyGoduto;

      aImCompCongAddRegNetto:=glbImportoAddRegDovuto - glbImportoAddRegGoduto - Nvl(aRecConguaglio.im_addreg_esterno,0);
      If aImCompCongAddRegNetto < 0 And Abs(aImCompCongAddRegNetto) < Nvl(importoLimite,999999999999) Then
         aImCompCongAddRegNetto := 0;
         glbImportoAddRegDovuto := glbImportoAddRegGoduto + Nvl(aRecConguaglio.im_addreg_esterno,0);
      End If;

      aImCompCongAddProNetto:=glbImportoAddProDovuto - glbImportoAddProGoduto - Nvl(aRecConguaglio.im_addprov_esterno,0);
      If aImCompCongAddProNetto < 0 And Abs(aImCompCongAddProNetto) < Nvl(importoLimite,999999999999) Then
         aImCompCongAddProNetto := 0;
         glbImportoAddProDovuto := glbImportoAddProGoduto + Nvl(aRecConguaglio.im_addprov_esterno,0);
      End If;

      aImCompCongAddComNetto:=glbImportoAddComDovuto - glbImportoAddComGoduto - Nvl(aRecConguaglio.im_addcom_esterno,0);
      If aImCompCongAddComNetto < 0 And Abs(aImCompCongAddComNetto) < Nvl(importoLimite,999999999999) Then
         aImCompCongAddComNetto := 0;
         glbImportoAddComDovuto := glbImportoAddComGoduto + Nvl(aRecConguaglio.im_addcom_esterno,0);
      End If;

      -- Calcolo credito irpef da portare sul COMPENSO
      --glbImportoCreditoIrpefGoduto e glbImportoCreditoIrpefDovuto sono entrambi positivi
--pipe.send_message('glbImportoCreditoIrpefDovuto = '||glbImportoCreditoIrpefDovuto);
--pipe.send_message('glbImportoCreditoIrpefGoduto = '||glbImportoCreditoIrpefGoduto);
      aImCompCongCreditoIrpef := -(glbImportoCreditoIrpefDovuto + glbImportoCredIrpParDetDovuto + glbImportoBonusIrpefDovuto - glbImportoCreditoIrpefGoduto - glbImportoCredIrpParDetGoduto - glbImportoBonusIrpefGoduto);
   End;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento del conguaglio
--pipe.send_message('glbFlNoDetrazioniAltre = '||glbFlNoDetrazioniAltre);
--pipe.send_message('glbFlNoDetrazioniFamily = '||glbFlNoDetrazioniFamily);
--pipe.send_message('glbFlDetrazioniAltriTipi = '||glbFlDetrazioniAltriTipi);
   BEGIN
      UPDATE CONGUAGLIO
      SET    im_irpef_goduto = glbImportoIrpefGoduto,
             im_family_goduto = glbImportoFamilyGoduto,
             im_addreg_goduto = glbImportoAddRegGoduto,
             im_addprov_goduto = glbImportoAddProGoduto,
             im_addcom_goduto = glbImportoAddComGoduto,
             detrazioni_pe_goduto = glbDetrazioniPeGoduto,
             detrazioni_la_goduto = glbDetrazioniLaGoduto,
             detrazioni_co_goduto = glbDetrazioniCoGoduto,
             detrazioni_fi_goduto = glbDetrazioniFiGoduto,
             detrazioni_al_goduto = glbDetrazioniAlGoduto,
             detrazione_rid_cuneo_goduto = glbDetrazioniCuneoGoduto,
             im_deduzione_goduto = glbDeduzioneIrpefGoduto,
             im_deduzione_family_goduto = glbDeduzioneFamilyGoduto,
             im_credito_irpef_goduto = glbImportoCreditoIrpefGoduto,
             im_cred_irpef_par_det_goduto = glbImportoCredIrpParDetGoduto,
             im_bonus_irpef_goduto = glbImportoBonusIrpefGoduto,
             im_irpef_dovuto = glbImportoIrpefDovuto,
             im_family_dovuto = glbImportoFamilyDovuto,
             im_addreg_dovuto = glbImportoAddRegDovuto,
             im_addprov_dovuto = glbImportoAddProDovuto,
             im_addcom_dovuto = glbImportoAddComDovuto,
             detrazioni_pe_dovuto = glbDetrazioniPeDovuto,
             detrazioni_la_dovuto = glbDetrazioniLaDovuto,
             detrazioni_co_dovuto = glbDetrazioniCoDovuto,
             detrazioni_fi_dovuto = glbDetrazioniFiDovuto,
             detrazioni_al_dovuto = glbDetrazioniAlDovuto,
             detrazione_rid_cuneo_dovuto = glbDetrazioniCuneoDovuto,
             im_deduzione_dovuto = glbDeduzioneIrpefDovuto,
             im_deduzione_family_dovuto = glbDeduzioneFamilyDovuto,
             im_credito_irpef_dovuto = glbImportoCreditoIrpefDovuto,
             im_cred_irpef_par_det_dovuto = glbImportoCredIrpParDetDovuto,
             im_bonus_irpef_dovuto = glbImportoBonusIrpefDovuto,
             imponibile_irpef_lordo = glbImponibileLordoIrpef,
             imponibile_irpef_netto = glbImponibileNettoIrpef,
             numero_giorni = glbNumeroGiorni,
             cd_cds_compenso = inCdsCompenso,
             cd_uo_compenso = inUoCompenso,
             esercizio_compenso = inEsercizioCompenso,
             pg_compenso = inPgCompenso,
             im_addreg_rate_eseprec = glbImpAddRegRateEseprec,
             im_addprov_rate_eseprec = glbImpAddProRateEseprec,
             im_addcom_rate_eseprec = glbImpAddComRateEseprec,
             im_cori_sospeso = glbImportoIrpefSospesoGoduto,
             fl_no_detrazioni_altre = glbFlNoDetrazioniAltre,
             fl_no_detrazioni_family = glbFlNoDetrazioniFamily,
             fl_detrazioni_altri_tipi = glbFlDetrazioniAltriTipi,
             fl_no_credito_irpef = glbFlNoCreditoIrpef,
             fl_no_credito_cuneo_irpef = glbFlNoCreditoCuneoIrpef,
             fl_no_detr_cuneo_irpef = glbFlNoDetrCuneoIrpef
      WHERE  cd_cds = aRecConguaglio.cd_cds AND
             cd_unita_organizzativa = aRecConguaglio.cd_unita_organizzativa AND
             esercizio = aRecConguaglio.esercizio AND
             pg_conguaglio = aRecConguaglio.pg_conguaglio;

   END;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento del compenso

   BEGIN

      -- Ciclo di lettura dei cori del compenso conguaglio

      OPEN gen_cur_cori FOR

           SELECT *
           FROM   V_CORI_CONGUAGLIO A
           WHERE  A.cd_cds = inCdsCompenso AND
                  A.cd_unita_organizzativa = inUoCompenso AND
                  A.esercizio = inEsercizioCompenso AND
                  A.pg_compenso = inPgCompenso;

      LOOP

         FETCH gen_cur_cori INTO
               aRecCoriConguaglio;

         EXIT WHEN gen_cur_cori%NOTFOUND;

         aImponibileCompCong:=0;
         aAmmontareCompCong:=0;
         aAmmontareCompCongNetto:=0;
         aImSospesoCompCong:=0;

         -- Se il conguaglio prevede l'accantonamento delle addizionali si memorizzano queste nella
         -- tabella RATEIZZA_CLASSIFIC_CORI eliminandole dal compenso stesso.
         -- Se il conguaglio prevede il pagamento delle addizionali allora si somma al netto
         -- dovuto/goduto l'eventuale importo precedentemente accantonato. Se questo risulta già in parte
         -- pagato il conguaglio non è ammesso

         IF CNRCTB545.getIsIrpefScaglioni(aRecCoriConguaglio.cd_classificazione_cori,
                                             aRecCoriConguaglio.pg_classificazione_montanti,
                                             aRecCoriConguaglio.fl_scrivi_montanti) = 'Y' THEN
               aImponibileCompCong:=aRecCoriConguaglio.imponibile;
               aAmmontareCompCong:=aImCompCongIrpefNetto;
               aAmmontareCompCongNetto:=aImCompCongIrpefNetto;

               -- Verifico se devo sospendere l'imposta netta
               -- Solo se l'ammontare netto è positivo lo sospendo
               Declare
                  FL_SOSP VARCHAR2(1):='N';
               Begin
                 If aAmmontareCompCongNetto > 0 Then
                    If aRecAnagrafico.fl_sospensione_irpef = 'Y' And
                       dataOdierna >= aDataSospensioneMin And
                       dataOdierna <= aDataSospensioneMax Then

                       Select FL_SOSPENSIONE_IRPEF
                       Into FL_SOSP
                       From V_TIPO_TRATTAMENTO_TIPO_CORI
                       Where cd_trattamento = aRecTipoTrattamento.cd_trattamento AND
                              dt_ini_val_trattamento = aRecTipoTrattamento.dt_ini_validita AND
                              dt_fin_val_trattamento = aRecTipoTrattamento.dt_fin_validita And
                              cd_cori = aRecCoriConguaglio.cd_contributo_ritenuta And
                              ((TI_CASSA_COMPETENZA = 'CA' AND
                              dt_ini_val_tratt_cori <= aRecCompenso.dt_registrazione AND
                              dt_fin_val_tratt_cori >= aRecCompenso.dt_registrazione AND
                              dt_ini_val_tipo_cori <= aRecCompenso.dt_registrazione AND
                              dt_fin_val_tipo_cori >= aRecCompenso.dt_registrazione ) OR
                              (TI_CASSA_COMPETENZA = 'CO' AND
                               dt_ini_val_tratt_cori <= aRecCompenso.dt_da_competenza_coge AND
                               dt_fin_val_tratt_cori >= aRecCompenso.dt_a_competenza_coge AND
                               dt_ini_val_tipo_cori <= aRecCompenso.dt_da_competenza_coge AND
                               dt_fin_val_tipo_cori >= aRecCompenso.dt_a_competenza_coge ));

                       If  FL_SOSP Is Not Null And fl_sosp = 'Y' Then
                            aImSospesoCompCong := aAmmontareCompCongNetto;
                            aAmmontareCompCongNetto:=aAmmontareCompCongNetto - aImSospesoCompCong;
                            aImCompCongIrpefNetto:=aAmmontareCompCongNetto;
                            aImSospesoCompCong := aImSospesoCompCong + glbImportoIrpefSospesoGoduto;
                       End If;
                    Else  -- non devo applicare la sospensione quindi me la valorizzo con quella della testata che è la precedente
                       aImSospesoCompCong := glbImportoIrpefSospesoGoduto;
                    End If;
                 Elsif aAmmontareCompCongNetto < 0 Then
                    -- devo restituire i soldi al percipiente quindi non applico la sospensione
                    -- però ridulo la sospensione già calcolata
                    If glbImportoIrpefSospesoGoduto > 0 Then
                        If Abs(aAmmontareCompCongNetto) >= glbImportoIrpefSospesoGoduto Then
                              aAmmontareCompCongNetto := aAmmontareCompCongNetto + glbImportoIrpefSospesoGoduto;
                              aImSospesoCompCong := 0;
                              aImCompCongIrpefNetto:=aAmmontareCompCongNetto;
                        Elsif Abs(aAmmontareCompCongNetto) < glbImportoIrpefSospesoGoduto Then
                              aImSospesoCompCong := aAmmontareCompCongNetto + glbImportoIrpefSospesoGoduto;
                              aAmmontareCompCongNetto := 0;
                              aImCompCongIrpefNetto:=aAmmontareCompCongNetto;
                        End If;
                    End If;
                 Else
                    aImSospesoCompCong := glbImportoIrpefSospesoGoduto;
                 End If;
               End;
         ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddReg THEN
               IF aRecConguaglio.fl_accantona_add_terr = 'Y' THEN
                  CNRCTB545.generaRateizzaClassificCori(aRecConguaglio,
                                                        aImCompCongAddRegNetto,
                                                        aRecRateizzaClassificCoriR0);
                  aRecCoriConguaglio.imponibile:=0;
                  aImCompCongAddRegNetto:=0;
               ELSE
                  IF (aRecRateizzaClassificCoriR0.im_da_rateizzare > 0 AND
                      aRecRateizzaClassificCoriR0.im_rateizzato > 0) THEN
                     IBMERR001.RAISE_ERR_GENERICO
                        ('Conguaglio non ammissibile. Si chiede il pagamento di ADDIZIONALI REGIONALI quando le stesse ' ||
                         'risultano essere state accantonate e, in esercizio successivo, anche trattenute');
                  END IF;
                  aImCompCongAddRegNetto:=aImCompCongAddRegNetto + aRecRateizzaClassificCoriR0.im_da_rateizzare;
               END IF;
               aImponibileCompCong:=aRecCoriConguaglio.imponibile;
               aAmmontareCompCong:=aImCompCongAddRegNetto;
               aAmmontareCompCongNetto:=aImCompCongAddRegNetto;
         ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddPro THEN
               IF aRecConguaglio.fl_accantona_add_terr = 'Y' THEN
                  CNRCTB545.generaRateizzaClassificCori(aRecConguaglio,
                                                        aImCompCongAddProNetto,
                                                        aRecRateizzaClassificCoriP0);
                  aRecCoriConguaglio.imponibile:=0;
                  aImCompCongAddProNetto:=0;
               ELSE
                  IF (aRecRateizzaClassificCoriP0.im_da_rateizzare > 0 AND
                      aRecRateizzaClassificCoriP0.im_rateizzato > 0) THEN
                     IBMERR001.RAISE_ERR_GENERICO
                        ('Conguaglio non ammissibile. Si chiede il pagamento di ADDIZIONALI PROVINCIALI quando le stesse ' ||
                         'risultano essere state accantonate e, in esercizio successivo, anche trattenute');
                  END IF;
                  aImCompCongAddProNetto:=aImCompCongAddProNetto + aRecRateizzaClassificCoriP0.im_da_rateizzare;
               END IF;
               aImponibileCompCong:=aRecCoriConguaglio.imponibile;
               aAmmontareCompCong:=aImCompCongAddProNetto;
               aAmmontareCompCongNetto:=aImCompCongAddProNetto;
         ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddCom THEN
               IF aRecConguaglio.fl_accantona_add_terr = 'Y' THEN
                  CNRCTB545.generaRateizzaClassificCori(aRecConguaglio,
                                                        aImCompCongAddComNetto,
                                                        aRecRateizzaClassificCoriC0);
                  aRecCoriConguaglio.imponibile:=0;
                  aImCompCongAddComNetto:=0;
               ELSE
                  IF (aRecRateizzaClassificCoriC0.im_da_rateizzare > 0 AND
                      aRecRateizzaClassificCoriC0.im_rateizzato > 0) THEN
                     IBMERR001.RAISE_ERR_GENERICO
                        ('Conguaglio non ammissibile. Si chiede il pagamento di ADDIZIONALI COMUNALI quando le stesse ' ||
                         'risultano essere state accantonate e, in esercizio successivo, anche trattenute');
                  END IF;
                  aImCompCongAddComNetto:=aImCompCongAddComNetto + aRecRateizzaClassificCoriC0.im_da_rateizzare;
               END IF;
               aImponibileCompCong:=aRecCoriConguaglio.imponibile;
               aAmmontareCompCong:=aImCompCongAddComNetto;
               aAmmontareCompCongNetto:=aImCompCongAddComNetto;
         ELSIF CNRCTB545.IsCoriCreditoIrpef(aRecCoriConguaglio.cd_contributo_ritenuta) = 'Y' THEN
              aAmmontareCompCong := -(glbImportoCreditoIrpefDovuto - glbImportoCreditoIrpefGoduto) - (glbImportoCredIrpParDetDovuto - glbImportoCredIrpParDetGoduto);
              aAmmontareCompCongNetto:=aAmmontareCompCong;
         ELSE
               aImponibileCompCong:=aRecCoriConguaglio.imponibile;
               aAmmontareCompCong:=aRecCoriConguaglio.ammontare;
               aAmmontareCompCongNetto:=aRecCoriConguaglio.ammontare;
         END IF;

         UPDATE CONTRIBUTO_RITENUTA
         SET    imponibile = 0,
                imponibile_lordo = 0,
                im_deduzione_irpef = 0,
                im_deduzione_family = 0,
                ammontare_lordo = aAmmontareCompCong,
                ammontare = aAmmontareCompCongNetto,    --aAmmontareCompCong - aImSospesoCompCong,
                im_cori_sospeso = aImSospesoCompCong
         WHERE  cd_cds = aRecCoriConguaglio.cd_cds AND
                cd_unita_organizzativa = aRecCoriConguaglio.cd_unita_organizzativa AND
                esercizio = aRecCoriConguaglio.esercizio AND
                pg_compenso = aRecCoriConguaglio.pg_compenso AND
                cd_contributo_ritenuta = aRecCoriConguaglio.cd_contributo_ritenuta AND
                ti_ente_percipiente = aRecCoriConguaglio.ti_ente_percipiente;

      END LOOP;

      CLOSE gen_cur_cori;

      DELETE FROM CONTRIBUTO_RITENUTA_DET
      WHERE  cd_cds = inCdsCompenso AND
             cd_unita_organizzativa = inUoCompenso AND
             esercizio = inEsercizioCompenso AND
             pg_compenso = inPgCompenso;

      UPDATE COMPENSO
      SET    im_lordo_percipiente = 0,
             im_totale_compenso = 0,
             im_netto_percipiente = 0 - (aImCompCongIrpefNetto + aImCompCongAddRegNetto +
                                         aImCompCongAddProNetto + aImCompCongAddComNetto +
                                         aImCompCongCreditoIrpef),
             im_cr_percipiente = aImCompCongIrpefNetto + aImCompCongAddRegNetto +
                                 aImCompCongAddProNetto + aImCompCongAddComNetto +
                                 aImCompCongCreditoIrpef,
             im_cr_ente = 0,
             detrazioni_personali = 0,
             detrazioni_personali_netto = 0,
             detrazioni_la = 0,
             detrazioni_la_netto = 0,
             detrazione_coniuge = 0,
             detrazione_coniuge_netto = 0,
             detrazione_figli = 0,
             detrazione_figli_netto = 0,
             detrazione_altri = 0,
             detrazione_altri_netto = 0,
             detrazione_riduzione_cuneo = 0,
             detrazione_rid_cuneo_netto = 0,
             imponibile_fiscale = glbImponibileLordoIrpef,
             imponibile_fiscale_netto = glbImponibileNettoIrpef,
             im_deduzione_irpef = glbDeduzioneIrpef,
             aliquota_fiscale = Null,
             im_tot_reddito_complessivo = aTotRedditoComplessivo
       WHERE cd_cds = aRecCompenso.cd_cds AND
             cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa AND
             esercizio = aRecCompenso.esercizio AND
             pg_compenso = aRecCompenso.pg_compenso;

   END;
--pipe.send_message('aTotRedditoComplessivo nel CONG= '||aTotRedditoComplessivo);
END creaCompensoConguaglio;

procedure calcolaCreditoEDetrazioni( aRecCreditoIrpef in credito_irpef%rowtype,
                                     aRecCompenso in COMPENSO%ROWTYPE,
                                     aImportoRiferimento in NUMBER,
                                     numeroGiorni in number,
                                      importoCredito in out number,
                                        importoDetrazioni in out number) is
    aCoefficiente   NUMBER(15,4) := 1;
    aGGTotaliCredito INTEGER;
    dataInizioGestioneCuneoFiscale date;
    aCreditoBase NUMBER(15,2) := 0;
begin
    IF aRecCreditoIrpef.im_credito != 0 then
        IF ((aRecCompenso.dt_da_competenza_coge >= aRecCreditoIrpef.dt_inizio_validita
             AND
             aRecCompenso.dt_da_competenza_coge <= aRecCreditoIrpef.dt_fine_validita)
           OR
            (aRecCompenso.dt_a_competenza_coge <= aRecCreditoIrpef.dt_fine_validita
             AND
             aRecCompenso.dt_a_competenza_coge >= aRecCreditoIrpef.dt_inizio_validita)
            OR
            (aRecCompenso.dt_da_competenza_coge <= aRecCreditoIrpef.dt_fine_validita
             AND
             aRecCompenso.dt_a_competenza_coge >= aRecCreditoIrpef.dt_inizio_validita)) THEN

             dataInizioGestioneCuneoFiscale := CNRCTB015.getDt01PerChiave('0', 'RIDUZIONE_CUNEO_DL_3_2020', 'DATA_INIZIO');
             aGGTotaliCredito:=IBMUTL001.getDaysBetween(aRecCreditoIrpef.dt_inizio_validita, aRecCreditoIrpef.dt_fine_validita);
             --Calcolo coefficiente
             If aRecCreditoIrpef.fl_applica_formula = 'Y' then
                 aCoefficiente := TRUNC(((aRecCreditoIrpef.im_superiore - aImportoRiferimento)/(aRecCreditoIrpef.im_superiore - aRecCreditoIrpef.im_inferiore)),4);
             end if;
--pipe.send_message('aCoefficiente = '||aCoefficiente);
             -- calcolo il credito totale rapportato agli effettivi giorni che lavorerà nell'anno
             importoCredito := ROUND(((aRecCreditoIrpef.im_credito/aGGTotaliCredito)* numeroGiorni),2);
--pipe.send_message('aCreditoTotAnno = '||aCreditoTotAnno);
             -- applico il coefficiente
             importoCredito := importoCredito * aCoefficiente;
             if aRecCreditoIrpef.im_credito_base != 0 then
                aCreditoBase := ROUND(((aRecCreditoIrpef.im_credito_base /aGGTotaliCredito) * numeroGiorni),2);
             end if;
             importoCredito := importoCredito + aCreditoBase;

        if aRecCreditoIrpef.fl_detrazione = 'Y' THEN
            if glbFlNoDetrCuneoIrpef != 'Y' then
                importoDetrazioni := importoDetrazioni + importoCredito;
            end if;
            importoCredito := 0;
        else
           If ((aRecCompenso.dt_a_competenza_coge < dataInizioGestioneCuneoFiscale and glbFlNoCreditoIrpef = 'Y') or
                (aRecCompenso.dt_a_competenza_coge >= dataInizioGestioneCuneoFiscale and glbFlNoCreditoCuneoIrpef = 'Y') )Then
               importoCredito := 0;
           END IF;
        END IF;

        END IF;
    end if;
end;

-- =================================================================================================
-- Calcolo del credito irpef
-- =================================================================================================
procedure calcolaCreditoIrpefDovuto
   (
    aImportoRiferimento in NUMBER,
    inNumGGTotMinPerCredito in INTEGER,
    aRecCompenso in COMPENSO%ROWTYPE,
    aDataMin in date,
    aDataMax in date,
    aCdCori in tipo_contributo_ritenuta.cd_contributo_ritenuta%type,
    dt_ini_val_cori in tipo_contributo_ritenuta.dt_ini_validita%type,
    totImportoDetrazioni in out number
)  IS

   aCreditoTotAnno NUMBER(15,2);
   aCoefficiente    NUMBER(15,4);

   aEsercizioRif COMPENSO.esercizio%TYPE;
   aRecCreditoIrpef CREDITO_IRPEF%ROWTYPE;

   aGGAnno INTEGER;
   aCreditoBase NUMBER(15,2) := 0;
   giorniCompetenzaCredito INTEGER;
   importoCredito number := 0;
   importoDetrazioni number := 0;
   credito GenericCurTyp;
   conta number := 0;
   trovatoCredito char(1) := 'N';
   mess varchar2(2000) := '';
BEGIN
   aCreditoTotAnno:=0;
   aCoefficiente:=1;

 BEGIN

--pipe.send_message('aImportoRiferimento = '||aImportoRiferimento);
--pipe.send_message('glbFlNoCreditoIrpef = '||glbFlNoCreditoIrpef);
   --è prevista la gestione ed il terzo non ha chiesto che non sia applicato

        -------------------------------------------------------------------------------------------------
        -- Determinazione giorni
        aEsercizioRif:=aRecCompenso.Esercizio;
        IF aEsercizioRif = TO_NUMBER(TO_CHAR(aRecCompenso.dt_registrazione,'YYYY')) THEN
            -------------------------------------------------------------------------------------------------
            -- Leggo parametri per credito
              OPEN credito FOR
               SELECT *
                FROM   CREDITO_IRPEF
                where ((aDataMin between dt_inizio_validita  and dt_fine_validita ) or
                      (aDataMax between dt_inizio_validita  and dt_fine_validita )) AND
                       im_inferiore <= aImportoRiferimento AND
                       im_superiore >= aImportoRiferimento AND
                       cd_cori = aCdCori AND
                       DT_INI_VALIDITA_CORI = DT_INI_VAL_CORI
                order by dt_inizio_validita;
          LOOP

             FETCH credito INTO aRecCreditoIrpef;

             EXIT WHEN credito%NOTFOUND;
             if aDataMin between aRecCreditoIrpef.dt_inizio_validita  and aRecCreditoIrpef.dt_fine_validita and
                aDataMax between aRecCreditoIrpef.dt_inizio_validita  and aRecCreditoIrpef.dt_fine_validita then
                giorniCompetenzaCredito := inNumGGTotMinPerCredito;
             else
                  giorniCompetenzaCredito := 0;
                  FOR i IN tabella_date_ok.FIRST .. tabella_date_ok.LAST
                  LOOP
                    if tabella_date_ok(i).tDataDa between aRecCreditoIrpef.dt_inizio_validita  and aRecCreditoIrpef.dt_fine_validita then
                      if tabella_date_ok(i).tDataA between aRecCreditoIrpef.dt_inizio_validita  and aRecCreditoIrpef.dt_fine_validita then
                        giorniCompetenzaCredito := giorniCompetenzaCredito + IBMUTL001.getDaysBetween(tabella_date_ok(i).tDataDa, tabella_date_ok(i).tDataA);
                      else
                        giorniCompetenzaCredito := giorniCompetenzaCredito + IBMUTL001.getDaysBetween(tabella_date_ok(i).tDataDa, aRecCreditoIrpef.dt_fine_validita);
                      end if;
                    elsif tabella_date_ok(i).tDataA between aRecCreditoIrpef.dt_inizio_validita  and aRecCreditoIrpef.dt_fine_validita then
                      giorniCompetenzaCredito := giorniCompetenzaCredito + IBMUTL001.getDaysBetween(aRecCreditoIrpef.dt_inizio_validita, tabella_date_ok(i).tDataA);
                    elsif tabella_date_ok(i).tDataDa < aRecCreditoIrpef.dt_inizio_validita and tabella_date_ok(i).tDataA > aRecCreditoIrpef.dt_inizio_validita then
                      giorniCompetenzaCredito := giorniCompetenzaCredito + IBMUTL001.getDaysBetween(aRecCreditoIrpef.dt_inizio_validita, aRecCreditoIrpef.dt_fine_validita);
                    end if;
                  END LOOP;
             end if;
             importoCredito := 0;
             importoDetrazioni := 0;
             calcolaCreditoEDetrazioni(aRecCreditoIrpef,
                                          aRecCompenso,
                                          aImportoRiferimento,
                                          giorniCompetenzaCredito,
                                          importoCredito,
                                          importoDetrazioni);
--             IF importoCredito > 0 THEN
--RAISE_APPLICATION_ERROR(-20100,giorniCompetenzaCredito||' '||importoCredito||' '||importoDetrazioni||' '||inNumGGTotMinPerCredito||' '||aDataMin||' '||aDataMax||' '||aRecCreditoIrpef.dt_inizio_validita);
--            END IF;
              IF tabCreditoIrpef.COUNT > 0 THEN
                FOR conta IN tabCreditoIrpef.FIRST .. tabCreditoIrpef.LAST LOOP
                  if tabCreditoIrpef(conta).tCdCori = aCdCori and tabCreditoIrpef(conta).tDtIniValCori = DT_INI_VAL_CORI and
                      aRecCreditoIrpef.fl_pareggio_detrazioni = tabCreditoIrpef(conta).pareggio_detrazioni then
                   tabCreditoIrpef(conta).tImCreditoMaxDovuto := aRecCreditoIrpef.im_credito + aRecCreditoIrpef.im_credito_base;
                   tabCreditoIrpef(conta).tImCreditoIrpefDovuto := tabCreditoIrpef(conta).tImCreditoIrpefDovuto + importoCredito;
                   trovatoCredito := 'S';
                  end if;
                  mess := mess||tabCreditoIrpef(conta).tCdCori||' '||tabCreditoIrpef(conta).tDtIniValCori||' '||tabCreditoIrpef(conta).tImCreditoIrpefDovuto||' '||tabCreditoIrpef(conta).tImCreditoIrpefGoduto;
                END LOOP;
              END IF;
--              if aCdCori = 'BONUSDL66' THEN
--              RAISE_APPLICATION_ERROR(-20100,mess);
--              END IF;
                IF trovatoCredito = 'N' then
                  conta:=tabCreditoIrpef.COUNT + 1;
                  tabCreditoIrpef(conta).tCdCori := aCdCori;
                  tabCreditoIrpef(conta).tDtIniValCori := DT_INI_VAL_CORI;
                  tabCreditoIrpef(conta).tImCreditoIrpefGoduto := 0;
                  tabCreditoIrpef(conta).tImCreditoIrpefDovuto := importoCredito;
                  tabCreditoIrpef(conta).tImCreditoMaxDovuto := aRecCreditoIrpef.im_credito + aRecCreditoIrpef.im_credito_base;
                  tabCreditoIrpef(conta).pareggio_detrazioni := aRecCreditoIrpef.fl_pareggio_detrazioni;
                end if;

             totImportoDetrazioni := totImportoDetrazioni + importoDetrazioni;
          end loop;
        end if;
  END;
END calcolaCreditoIrpefDovuto;



-- =================================================================================================
-- Lettura dati di base legati al conguaglio in calcolo, testata conguaglio e anagrafico.
-- I dati sono memorizzati rispettivamente in aRecConguaglio e aRecAnagrafico
-- =================================================================================================
PROCEDURE getDatiBaseConguaglio
   (
    inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
    inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
    eseguiLock CHAR
   ) IS

BEGIN

   -- Lettura conguaglio in calcolo

   aRecConguaglio:=CNRCTB545.getConguaglio(inCdsConguaglio,
                                           inUoConguaglio,
                                           inEsercizioConguaglio,
                                           inPgConguaglio,
                                           eseguiLock);

   -- Recupero dei dati dell'anagrafico associato al terzo del conguaglio

   aRecAnagrafico:=CNRCTB080.getAnag(aRecConguaglio.cd_terzo);

   -- Recupero delle gestioni detrazioni per il soggetto anagrafico in elaborazione

   glbFlNoDetrazioniAltre:=CNRCTB080.getAnagFlNoDetrazioniAltre(inEsercizioConguaglio,
                                                                aRecAnagrafico.cd_anag);
   glbFlNoDetrazioniFamily:=CNRCTB080.getAnagFlNoDetrazioniFamily(inEsercizioConguaglio,
                                                                  aRecAnagrafico.cd_anag);
   glbFlDetrazioniAltriTipi:=CNRCTB080.getAnagFlDetrazioniAltriTipi(inEsercizioConguaglio,
                                                                  aRecAnagrafico.cd_anag);
   glbFlNoCreditoIrpef:=CNRCTB080.getAnagFlNoCreditoIrpef(inEsercizioConguaglio,
                                                          aRecAnagrafico.cd_anag);
   glbFlNoCreditoCuneoIrpef:=CNRCTB080.getAnagFlNoCreditoCuneoIrpef(inEsercizioConguaglio,
                                                          aRecAnagrafico.cd_anag);
   glbFlNoDetrCuneoIrpef:=CNRCTB080.getAnagFlNoDetrCuneoIrpef(inEsercizioConguaglio,
                                                          aRecAnagrafico.cd_anag);
   glbRedditoComplessivo:=cnrctb080.getAnagRedditoComplessivo(inEsercizioConguaglio,
                                                              aRecAnagrafico.cd_anag);
   glbRedditoAbitazPrincipale:=cnrctb080.getAnagRedditoAbitazPrincipale(inEsercizioConguaglio,
                                                              aRecAnagrafico.cd_anag);
   glbImponibilePagatoDip := cnrctb080.getImponibilePagatoDip(inEsercizioConguaglio,
                                                              aRecAnagrafico.cd_anag);
   glbFlApplicaDetrPersMax:=cnrctb080.getAnagFlApplicaDetrPersMax(inEsercizioConguaglio,
                                                              aRecAnagrafico.cd_anag);
END getDatiBaseConguaglio;


-- =================================================================================================
-- Lettura, se esiste, dell'ultimo conguaglio presente nel sistema per il soggetto anagrafico in
-- elaborazione. Le informazioni definite nelle variabili _DOVUTO sono portate in _GODUTO del
-- nuovo conguaglio.
-- =================================================================================================
PROCEDURE getUltimoConguaglio
   (
    inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE
   ) IS
   eseguiLock CHAR(1);
   aRecCompensoConguaglioBase V_COMPENSO_CONGUAGLIO_BASE%ROWTYPE;
   aRecConguaglioUltimo CONGUAGLIO%ROWTYPE;

BEGIN

   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Recupero dell'identificativo dell'ultimo conguaglio (in base ai compensi) presente nel sistema.

   BEGIN

      SELECT * INTO aRecCompensoConguaglioBase
      FROM   V_COMPENSO_CONGUAGLIO_BASE A
      WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
             A.esercizio_compenso = inEsercizioConguaglio AND
             A.esercizio_conguaglio = inEsercizioConguaglio AND
             A.is_compenso_conguaglio = 'Y' AND
             A.stato_cofi != 'A' AND
             A.dacr_conguaglio =
                (SELECT MAX(B.dacr_conguaglio)
                 FROM   V_COMPENSO_CONGUAGLIO_BASE B
                 WHERE  B.cd_anag = A.cd_anag AND
                        B.esercizio_compenso = A.esercizio_compenso AND
                        B.esercizio_conguaglio = A.esercizio_conguaglio AND
                        B.is_compenso_conguaglio = A.is_compenso_conguaglio AND
                        B.stato_cofi != 'A');

      -- Leggo il conguaglio. Le informazioni definite nelle variabili _DOVUTO sono portate in _GODUTO del
      -- nuovo conguaglio se trovate altrimenti gli attributi sono azzerati.

      BEGIN

         aRecConguaglioUltimo:=CNRCTB545.getConguaglio(aRecCompensoConguaglioBase.cd_cds_conguaglio,
                                                       aRecCompensoConguaglioBase.cd_uo_conguaglio,
                                                       aRecCompensoConguaglioBase.esercizio_conguaglio,
                                                       aRecCompensoConguaglioBase.pg_conguaglio,
                                                       eseguiLock);
         glbImportoIrpefGoduto:=aRecConguaglioUltimo.im_irpef_dovuto;
         glbImportoFamilyGoduto:=aRecConguaglioUltimo.im_family_dovuto;
         glbImportoAddRegGoduto:=aRecConguaglioUltimo.im_addreg_dovuto;
         glbImportoAddProGoduto:=aRecConguaglioUltimo.im_addprov_dovuto;
         glbImportoAddComGoduto:=aRecConguaglioUltimo.im_addcom_dovuto;
         glbDetrazioniLaGoduto:=aRecConguaglioUltimo.detrazioni_la_dovuto;
         glbDetrazioniPeGoduto:=aRecConguaglioUltimo.detrazioni_pe_dovuto;
         glbDetrazioniCoGoduto:=aRecConguaglioUltimo.detrazioni_co_dovuto;
         glbDetrazioniFiGoduto:=aRecConguaglioUltimo.detrazioni_fi_dovuto;
         glbDetrazioniAlGoduto:=aRecConguaglioUltimo.detrazioni_al_dovuto;
         glbDetrazioniCuneoGoduto:=aRecConguaglioUltimo.detrazione_rid_cuneo_dovuto;
         glbDeduzioneIrpefGoduto:=aRecConguaglioUltimo.im_deduzione_dovuto;
         glbDeduzioneFamilyGoduto:=aRecConguaglioUltimo.im_deduzione_family_dovuto;
         glbImportoCreditoIrpefGoduto := aRecConguaglioUltimo.im_credito_irpef_dovuto;
         glbImportoCredIrpParDetGoduto := aRecConguaglioUltimo.im_cred_irpef_par_det_dovuto;
         glbImportoBonusIrpefGoduto := aRecConguaglioUltimo.im_bonus_irpef_dovuto;
         glbDataMinCompetenza:=aRecConguaglioUltimo.dt_da_competenza_coge;
         glbDataMaxCompetenza:=aRecConguaglioUltimo.dt_a_competenza_coge;

   -- Sospeso goduto letto dalla testata dell'ultimo conguaglio (contiene il sospeso di tutti
   -- i compensi conguagliati ma non quello del conguaglio stesso)
   --glbImportoIrpefSospesoGoduto:=aRecConguaglioUltimo.im_cori_sospeso;
   -- Devo aggiungere il sospeso dell'ultimo conguaglio
   Declare
       imSospUltimoCong  NUMBER;
   Begin
       Select Nvl(Sum(im_cori_sospeso),0)
       Into imSospUltimoCong
       From contributo_ritenuta
       Where cd_cds = aRecConguaglioUltimo.cd_cds_compenso
         And cd_unita_organizzativa = aRecConguaglioUltimo.cd_uo_compenso
         And esercizio = aRecConguaglioUltimo.esercizio_compenso
         And pg_compenso = aRecConguaglioUltimo.pg_compenso;

         glbImportoIrpefSospesoGoduto:= imSospUltimoCong;
   Exception
      When No_Data_Found Then
        Null;
   End;


      END;

   EXCEPTION

      WHEN no_data_found THEN
           glbImportoIrpefGoduto:=0;
           glbImportoFamilyGoduto:=0;
           glbImportoAddRegGoduto:=0;
           glbImportoAddProGoduto:=0;
           glbImportoAddComGoduto:=0;
           glbDetrazioniLaGoduto:=0;
           glbDetrazioniPeGoduto:=0;
           glbDetrazioniCoGoduto:=0;
           glbDetrazioniFiGoduto:=0;
           glbDetrazioniAlGoduto:=0;
           glbDetrazioniCuneoGoduto:=0;
           glbDeduzioneIrpefGoduto:=0;
           glbDeduzioneFamilyGoduto:=0;
           glbImportoIrpefSospesoGoduto:=0;
           glbImportoCreditoIrpefGoduto:=0;
		   glbImportoCredIrpParDetGoduto:=0;
		   glbImportoBonusIrpefGoduto:=0;
           glbDataMinCompetenza:=NULL;
           glbDataMaxCompetenza:=NULL;

   END;

END getUltimoConguaglio;

-- =================================================================================================
-- Lettura di tutti i compensi registrati per il soggetto anagrafico in elaborazione, sono esclusi i
-- soli compensi da conguaglio. Da questa elaborazione si estraggono gli intervalli temporali per il
-- ricalcolo delle detrazioni familiari e personali e gli imponibili di riferimento per l'elaborazione.
-- I valori portati dai compensi mai compresi in un precedente conguaglio concorrono alla formazione del _GODUTO
-- =================================================================================================
PROCEDURE leggiCompensiPerConguaglio
   IS
   eseguiLock CHAR(1);
   aRecCompensoConguaglioBase V_COMPENSO_CONGUAGLIO_BASE%ROWTYPE;
   aRecCompensoC COMPENSO%ROWTYPE;
   aRecCoriConguaglio V_CORI_CONGUAGLIO%ROWTYPE;

   gen_cur GenericCurTyp;
   gen_cur_cori GenericCurTyp;
   i_credito number := 0;
   trovatoCredito char(1):= 'N';
   dataInizioGestioneCuneoFiscale date;

BEGIN

   dataInizioGestioneCuneoFiscale := CNRCTB015.getDt01PerChiave('0', 'RIDUZIONE_CUNEO_DL_3_2020', 'DATA_INIZIO');

   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura di tutti i compensi definiti per il soggetto anagrafico in elaborazione e che non siano
   -- compensi da conguaglio.

   BEGIN

      OPEN gen_cur FOR

           SELECT *
           FROM   V_COMPENSO_CONGUAGLIO_BASE A
           WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
                  A.esercizio_compenso <= aRecConguaglio.esercizio AND
                  A.dt_emissione_mandato >= aDataIniEsercizio AND
                  A.dt_emissione_mandato <= aDataFinEsercizio AND
                  A.is_compenso_conguaglio = 'N' AND
                  A.stato_cofi = 'P'
           Order By a.dt_da_competenza_coge;

      LOOP

         FETCH gen_cur INTO
               aRecCompensoConguaglioBase;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Inserimento delle associazioni compenso conguaglio per i compensi che non risultano essere
         -- stati associati a precedenti conguagli

         IF aRecCompensoConguaglioBase.is_associato_conguaglio = 'N' THEN
            componiAssCompensoConguaglio(aRecCompensoConguaglioBase.cd_cds_compenso,
                                         aRecCompensoConguaglioBase.cd_uo_compenso,
                                         aRecCompensoConguaglioBase.esercizio_compenso,
                                         aRecCompensoConguaglioBase.pg_compenso);
         END IF;

         BEGIN

            ----------------------------------------------------------------------------------------
            -- Gestione delle date di competenza dei compensi inclusi in conguaglio

            -- Memorizzazione della data di competenza economica del conguaglio. Valore minimo e
            -- massimo delle date di competenza economica dei compensi in elaborazione

            IF (glbDataMinCompetenza IS NULL OR
                aRecCompensoConguaglioBase.dt_da_competenza_coge < glbDataMinCompetenza) THEN
               glbDataMinCompetenza:=aRecCompensoConguaglioBase.dt_da_competenza_coge;
            END IF;

            IF (glbDataMaxCompetenza IS NULL OR
                aRecCompensoConguaglioBase.dt_a_competenza_coge > glbDataMaxCompetenza) THEN
               glbDataMaxCompetenza:=aRecCompensoConguaglioBase.dt_a_competenza_coge;
            END IF;

            ----------------------------------------------------------------------------------------
            -- Lettura e lock dei compensi da comprendersi nel conguaglio ed elaborazione delle
            -- detrazioni

            -- Lettura testata compenso

            aRecCompensoC:=CNRCTB545.getCompenso(aRecCompensoConguaglioBase.cd_cds_compenso,
                                                 aRecCompensoConguaglioBase.cd_uo_compenso,
                                                 aRecCompensoConguaglioBase.esercizio_compenso,
                                                 aRecCompensoConguaglioBase.pg_compenso,
                                                 eseguiLock);

      If aRecTipoTrattamento.fl_agevolazioni_cervelli = 'Y' Or glbEsisteCompensoCervelli = 'Y' or
         --aRecTipoTrattamento.cd_trattamento in ('T162','T163','CONGL20','CONGL30')
         aRecTipoTrattamento.fl_agevolazioni_rientro_lav = 'Y' Then
          glbImponibileLordoPercipiente := glbImponibileLordoPercipiente + aRecCompensoC.im_lordo_percipiente - aRecCompensoC.quota_esente;
      End If;

            -- Composizione della matrice delle date per il calcolo dei giorni reali di competenza
            -- del conguaglio
            -- i compensi da missione non concorrono al calcolo dei giorni
            IF aRecCompensoC.fl_recupero_rate = 'N' THEN
               If aRecCompensoC.pg_missione is null then
                  CNRCTB545.componiMatriceDate(tabella_date,
                                               aRecCompensoC.dt_da_competenza_coge,
                                               aRecCompensoC.dt_a_competenza_coge,
                                               'N',
                                               'Y');
               End If;
            ELSE
               If aRecCompensoC.pg_missione is null then
                  CNRCTB545.componiMatriceDate(tabella_date_neg,
                                               aRecCompensoC.dt_da_competenza_coge,
                                               aRecCompensoC.dt_a_competenza_coge,
                                               'N',
                                               'Y');
               End If;
            END IF;

            -- Filtro per compensi che prevedono detrazioni personali o di lavoro.
            -- Si memorizzano i relativi intervalli di competenza economica e, se il compenso in
            -- elaborazione non è stato precedentemente incluso in alcun conguaglio, si incrementa il
            -- goduto del conguaglio della relativa detrazione

            IF (aRecCompensoConguaglioBase.fl_detrazioni_familiari = 'Y' OR
                aRecCompensoConguaglioBase.fl_detrazioni_lavoro = 'Y' Or
                aRecCompensoConguaglioBase.fl_detrazioni_altre = 'Y') THEN

               IF aRecCompensoConguaglioBase.is_associato_conguaglio = 'N' THEN
                  glbDetrazioniLaGoduto:=glbDetrazioniLaGoduto + aRecCompensoC.detrazioni_la_netto;
                  glbDetrazioniPeGoduto:=glbDetrazioniPeGoduto + aRecCompensoC.detrazioni_personali_netto;
                  glbDetrazioniCoGoduto:=glbDetrazioniCoGoduto + aRecCompensoC.detrazione_coniuge_netto;
                  glbDetrazioniFiGoduto:=glbDetrazioniFiGoduto + aRecCompensoC.detrazione_figli_netto;
                  glbDetrazioniAlGoduto:=glbDetrazioniAlGoduto + aRecCompensoC.detrazione_altri_netto;
                  glbDetrazioniCuneoGoduto:=glbDetrazioniCuneoGoduto + aRecCompensoC.detrazione_rid_cuneo_netto;
               END IF;

               IF aRecCompensoConguaglioBase.fl_detrazioni_familiari = 'Y' THEN
                  IF aRecCompensoC.fl_recupero_rate = 'N' THEN
                     If aRecCompensoC.pg_missione is null then
                        CNRCTB545.componiMatriceDate(tabella_date_fam,
                                                     aRecCompensoC.dt_da_competenza_coge,
                                                     aRecCompensoC.dt_a_competenza_coge,
                                                     'F',
                                                     'Y');
                     End If;
                  ELSE
                     If aRecCompensoC.pg_missione is null then
                        CNRCTB545.componiMatriceDate(tabella_date_fam_neg,
                                                     aRecCompensoC.dt_da_competenza_coge,
                                                     aRecCompensoC.dt_a_competenza_coge,
                                                     'F',
                                                     'Y');
                     End If;
                  END IF;
               END IF;

               IF aRecCompensoConguaglioBase.fl_detrazioni_lavoro = 'Y'  Or aRecCompensoConguaglioBase.fl_detrazioni_altre = 'Y' THEN
                  IF aRecCompensoC.fl_recupero_rate = 'N' THEN
                     If aRecCompensoC.pg_missione is null then
                        CNRCTB545.componiMatriceDate(tabella_date_per,
                                                     aRecCompensoC.dt_da_competenza_coge,
                                                     aRecCompensoC.dt_a_competenza_coge,
                                                     'L',
                                                     'Y');
                     End If;
                  ELSE
                     If aRecCompensoC.pg_missione is null then
                        CNRCTB545.componiMatriceDate(tabella_date_per_neg,
                                                     aRecCompensoC.dt_da_competenza_coge,
                                                     aRecCompensoC.dt_a_competenza_coge,
                                                     'L',
                                                     'Y');
                     End If;
                  END IF;
               END IF;

            END IF;

            -- Lettura dei record di CONTRIBUTO_RITENUTA che sono da recuperare per il conguaglio;
            -- IRPEF a scaglioni e addizionali comunali, provinciali e regionali
            OPEN gen_cur_cori FOR

                 SELECT *
                 FROM   V_CORI_CONGUAGLIO A
                 WHERE  A.cd_cds = aRecCompensoConguaglioBase.cd_cds_compenso AND
                        A.cd_unita_organizzativa = aRecCompensoConguaglioBase.cd_uo_compenso AND
                        A.esercizio = aRecCompensoConguaglioBase.esercizio_compenso AND
                        A.pg_compenso = aRecCompensoConguaglioBase.pg_compenso;

            LOOP

               FETCH gen_cur_cori INTO
                     aRecCoriConguaglio;

               EXIT WHEN gen_cur_cori%NOTFOUND;

               -- Memorizzo imponibile IRPEF e importi goduti di IRPEF e addizionali
               -- Memorizzo anche l'imponibile di riferimento per le detrazioni familiari o personali
               -- se previste nel trattamento

               -- IRPEF

               IF CNRCTB545.getIsIrpefScaglioni(aRecCoriConguaglio.cd_classificazione_cori,
                                                aRecCoriConguaglio.pg_classificazione_montanti,
                                                aRecCoriConguaglio.fl_scrivi_montanti) = 'Y' THEN

                  glbImponibileLordoIrpef:=glbImponibileLordoIrpef + aRecCoriConguaglio.imponibile_lordo;
                  glbImponibileNettoIrpef:=glbImponibileNettoIrpef + aRecCoriConguaglio.imponibile;
                  glbDeduzioneIrpef:=glbDeduzioneIrpef + aRecCoriConguaglio.deduzione;
                  glbDeduzioneFamily:=glbDeduzioneFamily + aRecCoriConguaglio.deduzione_family;

                  IF aRecCompensoConguaglioBase.fl_detrazioni_familiari = 'Y' THEN
                     glbImponibileLordoDetFam:=glbImponibileLordoDetFam + aRecCoriConguaglio.imponibile_lordo;
                     glbImponibileNettoDetFam:=glbImponibileNettoDetFam + aRecCoriConguaglio.imponibile;
                  END IF;

                  IF aRecCompensoConguaglioBase.fl_detrazioni_lavoro = 'Y' Or aRecCompensoConguaglioBase.fl_detrazioni_altre = 'Y' THEN
                     glbImponibileLordoDetPer:=glbImponibileLordoDetPer + aRecCoriConguaglio.imponibile_lordo;
                     glbImponibileNettoDetPer:=glbImponibileNettoDetPer + aRecCoriConguaglio.imponibile;
                  END IF;

                  IF aRecCompensoConguaglioBase.is_associato_conguaglio = 'N' THEN
                     glbImportoIrpefGoduto:=glbImportoIrpefGoduto + aRecCoriConguaglio.ammontare_lordo;
                     glbImportoFamilyGoduto:=glbDeduzioneFamily;
                     glbDeduzioneIrpefGoduto:=glbDeduzioneIrpefGoduto + aRecCoriConguaglio.deduzione;
                     glbDeduzioneFamilyGoduto:=glbDeduzioneFamilyGoduto + aRecCoriConguaglio.deduzione_family;
                     glbImportoIrpefSospesoGoduto:=Nvl(glbImportoIrpefSospesoGoduto,0) + Nvl(aRecCoriConguaglio.cori_sospeso,0);
                  END IF;

               END IF;

               -- Addizionali regionali, provinciali e comunali

               IF aRecCompensoConguaglioBase.is_associato_conguaglio = 'N' THEN
                  IF    aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddReg THEN
                        glbImportoAddRegGoduto:=glbImportoAddRegGoduto + aRecCoriConguaglio.ammontare_lordo;
                  ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddPro THEN
                        glbImportoAddProGoduto:=glbImportoAddProGoduto + aRecCoriConguaglio.ammontare_lordo;
                  ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddCom THEN
                        glbImportoAddComGoduto:=glbImportoAddComGoduto + aRecCoriConguaglio.ammontare_lordo;
                  --anche l'acconto aggiorna il goduto add com
                  ELSIF aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriAddComAcconto THEN
                        glbImportoAddComGoduto:=glbImportoAddComGoduto + aRecCoriConguaglio.ammontare_lordo;
                        glbImportoAddComAccGoduto:=Nvl(glbImportoAddComAccGoduto,0) + aRecCoriConguaglio.ammontare_lordo;
                  END IF;
               END IF;

               -- anche se ho fatto il cong prendo dai compensi le previd
               If aRecCoriConguaglio.ti_ente_percipiente = 'P' And
                         (aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriPrevid Or
                          aRecCoriConguaglio.cd_classificazione_cori = CNRCTB545.isCoriInail) THEN
                        glbTotalePrevidInail:=glbTotalePrevidInail + aRecCoriConguaglio.ammontare_lordo;
               END IF;

               IF CNRCTB545.IsCoriCreditoIrpef(aRecCoriConguaglio.cd_contributo_ritenuta) = 'Y' then
                  IF aRecCompensoConguaglioBase.is_associato_conguaglio = 'N' THEN
                     i_credito := 0;
                     trovatoCredito := 'N';

                      if tabCreditoIrpef.count > 0 then
                       FOR i_credito IN tabCreditoIrpef.FIRST .. tabCreditoIrpef.LAST LOOP
                          if tabCreditoIrpef(i_credito).tCdCori = aRecCoriConguaglio.cd_contributo_ritenuta and tabCreditoIrpef(i_credito).tDtIniValCori = aRecCoriConguaglio.dt_ini_validita and
                          ((tabCreditoIrpef(i_credito).pareggio_detrazioni = aRecCoriConguaglio.fl_credito_pareggio_detrazioni) or (aRecCoriConguaglio.fl_credito_pareggio_detrazioni is null and tabCreditoIrpef(i_credito).pareggio_detrazioni = 'N')) then
                           tabCreditoIrpef(i_credito).tImCreditoIrpefGoduto := tabCreditoIrpef(i_credito).tImCreditoIrpefGoduto + (-aRecCoriConguaglio.ammontare_lordo);
                           trovatoCredito := 'S';
                           if dataInizioGestioneCuneoFiscale <= aRecCoriConguaglio.dt_ini_validita then
                               IF tabCreditoIrpef(i_credito).PAREGGIO_DETRAZIONI = 'Y' THEN
                                    glbImportoCredIrpParDetGoduto := glbImportoCredIrpParDetGoduto + (-aRecCoriConguaglio.ammontare_lordo);
                               else
                                    glbImportoCreditoIrpefGoduto:=glbImportoCreditoIrpefGoduto + (-aRecCoriConguaglio.ammontare_lordo);
                               end if;
                           else
                             glbImportoBonusIrpefGoduto:=glbImportoBonusIrpefGoduto + (-aRecCoriConguaglio.ammontare_lordo);
                           end if;
                          end if;
                       end loop;
                      end if;
                      IF trovatoCredito = 'N' then
                         i_credito:=tabCreditoIrpef.COUNT + 1;
                         tabCreditoIrpef(i_credito).tCdCori := aRecCoriConguaglio.cd_contributo_ritenuta;
                         tabCreditoIrpef(i_credito).tDtIniValCori := aRecCoriConguaglio.dt_ini_validita;
                         tabCreditoIrpef(i_credito).tImCreditoIrpefGoduto := -aRecCoriConguaglio.ammontare_lordo;
                         tabCreditoIrpef(i_credito).tImCreditoIrpefDovuto := 0;
                         if aRecCoriConguaglio.fl_credito_pareggio_detrazioni is null then
                             tabCreditoIrpef(i_credito).PAREGGIO_DETRAZIONI := 'N';
                         else
                             tabCreditoIrpef(i_credito).PAREGGIO_DETRAZIONI := aRecCoriConguaglio.fl_credito_pareggio_detrazioni;
                         end if;
                         if dataInizioGestioneCuneoFiscale <= aRecCoriConguaglio.dt_ini_validita then
                            IF tabCreditoIrpef(i_credito).PAREGGIO_DETRAZIONI = 'Y' THEN
                              glbImportoCredIrpParDetGoduto := glbImportoCredIrpParDetGoduto + (-aRecCoriConguaglio.ammontare_lordo);
                            else
                              glbImportoCreditoIrpefGoduto:=glbImportoCreditoIrpefGoduto + (-aRecCoriConguaglio.ammontare_lordo);
                            end if;
                         else
                           glbImportoBonusIrpefGoduto:=glbImportoBonusIrpefGoduto + (-aRecCoriConguaglio.ammontare_lordo);
                         end if;
                      end if;
                  END IF;
               END IF;

            END LOOP;

            CLOSE gen_cur_cori;
         END;

      END LOOP;

      CLOSE gen_cur;

   END;

END leggiCompensiPerConguaglio;

-- =================================================================================================
-- Inserimento dei record in ASS_COMPENSO_CONGUAGLIO
-- =================================================================================================
PROCEDURE componiAssCompensoConguaglio
   (
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE
   ) IS
   aRecAssCompensoConguaglio ASS_COMPENSO_CONGUAGLIO%ROWTYPE;

BEGIN

   aRecAssCompensoConguaglio:=NULL;
   aRecAssCompensoConguaglio.cd_cds_conguaglio:=aRecConguaglio.cd_cds;
   aRecAssCompensoConguaglio.cd_uo_conguaglio:=aRecConguaglio.cd_unita_organizzativa;
   aRecAssCompensoConguaglio.esercizio_conguaglio:=aRecConguaglio.esercizio;
   aRecAssCompensoConguaglio.pg_conguaglio:=aRecConguaglio.pg_conguaglio;
   aRecAssCompensoConguaglio.cd_cds_compenso:=inCdsCompenso;
   aRecAssCompensoConguaglio.cd_uo_compenso:=inUoCompenso;
   aRecAssCompensoConguaglio.esercizio_compenso:=inEsercizioCompenso;
   aRecAssCompensoConguaglio.pg_compenso:=inPgCompenso;
   aRecAssCompensoConguaglio.dacr:=dataOdierna;
   aRecAssCompensoConguaglio.utcr:=aRecConguaglio.utcr;
   aRecAssCompensoConguaglio.duva:=dataOdierna;
   aRecAssCompensoConguaglio.utuv:=aRecConguaglio.utuv;
   aRecAssCompensoConguaglio.pg_ver_rec:=1;
   CNRCTB545.insAssCompensoConguaglio(aRecAssCompensoConguaglio);

END componiAssCompensoConguaglio;

-- =================================================================================================
-- Scarico intervalli temporali per recupero rate
-- =================================================================================================
PROCEDURE scaricaRecuperoRate
   (
    intervallo_date IN OUT CNRCTB545.intervalloDateTab,
    intervallo_date_neg IN OUT CNRCTB545.intervalloDateTab
   ) IS

   flEsisteSovrapposto CHAR(1);
   aDataTmp DATE;

   i BINARY_INTEGER;
   j BINARY_INTEGER;
   k BINARY_INTEGER;

   intervallo_date_ok CNRCTB545.intervalloDateTab;
   intervallo_date_neg_ok CNRCTB545.intervalloDateTab;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Esco se non ci sono rate da recuperare

   IF intervallo_date_neg.COUNT = 0 THEN
      RETURN;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Azzeramento matrice comodo memorizzazione intervalli rate al netto del recupero

   intervallo_date_ok.DELETE;
   intervallo_date_neg_ok.DELETE;

   -------------------------------------------------------------------------------------------------
   -- Scarico degli intervalli temporali da recuperare. Nel primo passaggio si cercano i soli intervalli
   -- uguali tra loro. Gestione fatta per prestazioni in quanto normalmente questa gestione dovreabbe
   -- risolvere tutti i casi di corrispondenza

   BEGIN

      -- Ciclo principale sugli intervalli date da recuperare --------------------------------------

      FOR i IN intervallo_date_neg.FIRST .. intervallo_date_neg.LAST

      LOOP

         -- Ciclo sugli intervalli temporali ordinari (da stornare) --------------------------------

         -- Se la collezione delle date da stornare è vuota allora esco dal ciclo

         IF intervallo_date.COUNT = 0 THEN
            EXIT;
         END IF;

         -- Attivazione ciclo per storno

         FOR j IN intervallo_date.FIRST .. intervallo_date.LAST

         LOOP

            IF intervallo_date_neg(i).tDataDa IS NOT NULL THEN
               IF (intervallo_date_neg(i).tDataDa = intervallo_date(j).tDataDa AND
                   intervallo_date_neg(i).tDataA = intervallo_date(j).tDataA) THEN
                  intervallo_date_neg(i).tDataDa:=NULL;
                  intervallo_date_neg(i).tDataA:=NULL;
                  intervallo_date(j).tDataDa:=NULL;
                  intervallo_date(j).tDataA:=NULL;
               END IF;
            END IF;

            -- Scrittura intervallo non stornato su matrice copia

            IF intervallo_date(j).tDataDa IS NOT NULL THEN
               k:=intervallo_date_ok.COUNT +1;
               intervallo_date_ok(k).tDataDa:=intervallo_date(j).tDataDa;
               intervallo_date_ok(k).tDataA:=intervallo_date(j).tDataA;
            END IF;

         END LOOP;

         -- Per ogni record di intervallo_date_neg ricostruisco la matrice intervallo_date

         IF intervallo_date_ok.COUNT > 0 THEN
            intervallo_date:=intervallo_date_ok;
            intervallo_date_ok.DELETE;
         ELSE
            intervallo_date.DELETE;
            intervallo_date_ok.DELETE;
         END IF;

         IF intervallo_date_neg(i).tDataDa IS NOT NULL THEN
            k:=intervallo_date_neg_ok.COUNT +1;
            intervallo_date_neg_ok(k).tDataDa:=intervallo_date_neg(i).tDataDa;
            intervallo_date_neg_ok(k).tDataA:=intervallo_date_neg(i).tDataA;
         END IF;

      END LOOP;

      -- Esco se la collezione delle date da stornare è vuota ed azzero inoltre entrambe le matrici

      IF intervallo_date.COUNT = 0 THEN
         intervallo_date.DELETE;
         intervallo_date_neg.DELETE;
         RETURN;
      END IF;

      -- Esco se la matrice degli intervalli da stornare è vuota

      IF intervallo_date_neg_ok.COUNT = 0 THEN
         intervallo_date_neg.DELETE;
         RETURN;
      END IF;

      intervallo_date_neg:=intervallo_date_neg_ok;
      intervallo_date_neg_ok.DELETE;

   END;

   -------------------------------------------------------------------------------------------------
   -- Scarico degli intervalli temporali da recuperare. Nel secondo passaggio si cercano le altre
   -- sovrapposizioni

   intervallo_date_ok.DELETE;
   intervallo_date_neg_ok.DELETE;

   BEGIN

      -- Ciclo principale da eseguirsi fino a quando trovo sovrapposizioni o uno dei due array risulta vuoto

      LOOP

         flEsisteSovrapposto:='N';

         -- Ciclo principale sugli intervalli date da recuperare -----------------------------------

         FOR i IN intervallo_date_neg.FIRST .. intervallo_date_neg.LAST

         LOOP

            -- Ciclo sugli intervalli temporali ordinari (da stornare) -----------------------------

            -- Se la collezione delle date da stornare è vuota allora esco dal ciclo

            IF intervallo_date.COUNT = 0 THEN
               EXIT;
            END IF;

            -- Attivazione ciclo per storno

            FOR j IN intervallo_date.FIRST .. intervallo_date.LAST

            LOOP

               IF intervallo_date_neg(i).tDataDa IS NOT NULL THEN

                  -- I due intervalli sono uguali

                  IF    (intervallo_date_neg(i).tDataDa = intervallo_date(j).tDataDa AND
                         intervallo_date_neg(i).tDataA = intervallo_date(j).tDataA) THEN
                        flEsisteSovrapposto:='Y';
                        intervallo_date_neg(i).tDataDa:=NULL;
                        intervallo_date_neg(i).tDataA:=NULL;
                        intervallo_date(j).tDataDa:=NULL;
                        intervallo_date(j).tDataA:=NULL;

                  -- L'intervallo da recuperare è contenuto in quello da stornare

                  ELSIF (intervallo_date_neg(i).tDataDa >= intervallo_date(j).tDataDa AND
                         intervallo_date_neg(i).tDataA <= intervallo_date(j).tDataA) THEN
                        flEsisteSovrapposto:='Y';
                        IF    intervallo_date_neg(i).tDataDa = intervallo_date(j).tDataDa THEN
                              intervallo_date(j).tDataDa:=intervallo_date_neg(i).tDataA + 1;
                              intervallo_date_neg(i).tDataDa:=NULL;
                              intervallo_date_neg(i).tDataA:=NULL;
                        ELSIF intervallo_date_neg(i).tDataA = intervallo_date(j).tDataA THEN
                              intervallo_date(j).tDataA:=intervallo_date_neg(i).tDataDa - 1;
                              intervallo_date_neg(i).tDataDa:=NULL;
                              intervallo_date_neg(i).tDataA:=NULL;
                        ELSE
                              k:=intervallo_date_ok.COUNT +1;
                              intervallo_date_ok(k).tDataDa:=intervallo_date(j).tDataDa;
                              intervallo_date_ok(k).tDataA:=intervallo_date_neg(i).tDataDa - 1;
                              intervallo_date(j).tDataDa:=intervallo_date_neg(i).tDataA + 1;
                              intervallo_date_neg(i).tDataDa:=NULL;
                              intervallo_date_neg(i).tDataA:=NULL;
                        END IF;

                  -- L'intervallo da recuperare e quello da stornare hanno intersezione (data A recupero > data A storno)

                  ELSIF (intervallo_date_neg(i).tDataDa >= intervallo_date(j).tDataDa AND
                         intervallo_date_neg(i).tDataDa <= intervallo_date(j).tDataA AND
                         intervallo_date_neg(i).tDataA > intervallo_date(j).tDataA) THEN
                        flEsisteSovrapposto:='Y';
                        IF intervallo_date_neg(i).tDataDa = intervallo_date(j).tDataDa THEN
                           intervallo_date_neg(i).tDataDa:=intervallo_date(j).tDataA + 1;
                           intervallo_date(j).tDataDa:=NULL;
                           intervallo_date(j).tDataA:=NULL;
                        ELSE
                           aDataTmp:=intervallo_date(j).tDataA;
                           intervallo_date(j).tDataA:=intervallo_date_neg(i).tDataDa - 1;
                           intervallo_date_neg(i).tDataDa:=aDataTmp + 1;
                        END IF;

                  -- L'intervallo da recuperare e quello da stornare hanno intersezione (data Da recupero < data Da storno)

                  ELSIF (intervallo_date_neg(i).tDataA >= intervallo_date(j).tDataDa AND
                         intervallo_date_neg(i).tDataA <= intervallo_date(j).tDataA AND
                         intervallo_date_neg(i).tDataDa < intervallo_date(j).tDataDa) THEN
                        flEsisteSovrapposto:='Y';
                        IF intervallo_date_neg(i).tDataA = intervallo_date(j).tDataA THEN
                           intervallo_date_neg(i).tDataA:=intervallo_date(j).tDataDa - 1;
                           intervallo_date(j).tDataDa:=NULL;
                           intervallo_date(j).tDataA:=NULL;
                        ELSE
                           aDataTmp:=intervallo_date(j).tDataDa;
                           intervallo_date(j).tDataDa:=intervallo_date_neg(i).tDataA + 1;
                           intervallo_date_neg(i).tDataA:=aDataTmp - 1;
                        END IF;

                  -- L'intervallo da recuperare contiene quello da stornare (estremi non compresi)

                  ELSIF (intervallo_date_neg(i).tDataDa < intervallo_date(j).tDataDa AND
                         intervallo_date_neg(i).tDataA > intervallo_date(j).tDataA) THEN
                        flEsisteSovrapposto:='Y';
                        k:=intervallo_date_neg_ok.COUNT + 1;
                        intervallo_date_neg_ok(k).tDataDa:=intervallo_date_neg(i).tDataDa;
                        intervallo_date_neg_ok(k).tDataA:=intervallo_date(j).tDataDa - 1;
                        intervallo_date_neg(i).tDataDa:=intervallo_date(j).tDataA + 1;
                        intervallo_date(j).tDataDa:=NULL;
                        intervallo_date(j).tDataA:=NULL;

                  END IF;

               END IF;

               -- Scrittura intervallo non stornato su matrice copia

               IF intervallo_date(j).tDataDa IS NOT NULL THEN
                  k:=intervallo_date_ok.COUNT +1;
                  intervallo_date_ok(k).tDataDa:=intervallo_date(j).tDataDa;
                  intervallo_date_ok(k).tDataA:=intervallo_date(j).tDataA;
               END IF;

            END LOOP;

            -- Per ogni record di intervallo_date_neg ricostruisco la matrice intervallo_date

            IF intervallo_date_ok.COUNT > 0 THEN
               intervallo_date:=intervallo_date_ok;
               intervallo_date_ok.DELETE;
            ELSE
               intervallo_date.DELETE;
               intervallo_date_ok.DELETE;
            END IF;

            IF intervallo_date_neg(i).tDataDa IS NOT NULL THEN
               k:=intervallo_date_neg_ok.COUNT +1;
               intervallo_date_neg_ok(k).tDataDa:=intervallo_date_neg(i).tDataDa;
               intervallo_date_neg_ok(k).tDataA:=intervallo_date_neg(i).tDataA;
            END IF;

         END LOOP;

         -- Esco se la collezione delle date da stornare è vuota

         IF intervallo_date.COUNT = 0 THEN
            EXIT;
         END IF;

         -- Esco se la matrice degli intervalli da stornare è vuota

         IF intervallo_date_neg_ok.COUNT = 0 THEN
            intervallo_date_neg.DELETE;
            intervallo_date_neg_ok.DELETE;
            EXIT;
         END IF;

         intervallo_date_neg:=intervallo_date_neg_ok;
         intervallo_date_neg_ok.DELETE;

         -- Esco se non ho trovato alcuna sovrapposizione

         IF flEsisteSovrapposto='N' THEN
            EXIT;
         END IF;

      END LOOP;

      -- Esco se la collezione delle date da stornare è vuota ed azzero inoltre entrambe le matrici

      IF intervallo_date.COUNT = 0 THEN
         intervallo_date.DELETE;
         intervallo_date_neg.DELETE;
      END IF;

      -- Esco se la matrice degli intervalli da stornare è vuota

      IF intervallo_date_neg.COUNT = 0 THEN
         intervallo_date_neg.DELETE;
      END IF;

   END;

END scaricaRecuperoRate;


-- =================================================================================================
-- Eliminazione degli intervalli sovrapposti sulla matrice date
-- =================================================================================================
PROCEDURE normalizzaMatriceDate
   (
    intervallo_date IN OUT CNRCTB545.intervalloDateTab,
    intervallo_date_ok IN OUT CNRCTB545.intervalloDateTab
   ) IS
   i BINARY_INTEGER;
   j BINARY_INTEGER;
   k BINARY_INTEGER;

   numeroGG INTEGER;
   scritto CHAR(1);

BEGIN

   -- Esco se la collezione delle date è vuota

   IF intervallo_date.COUNT = 0 THEN
      intervallo_date_ok.DELETE;
      RETURN;
   END IF;

   -- Verifico che non esistano intervalli di date con sovrapposizione

   IF intervallo_date.COUNT = 1 THEN
      intervallo_date_ok:=intervallo_date;
   ELSE
      LOOP
         intervallo_date_ok.DELETE;

         FOR i IN intervallo_date.FIRST .. intervallo_date.LAST

         LOOP

            scritto:='N';

            IF intervallo_date_ok.FIRST IS NULL THEN
               intervallo_date_ok(1).tDataDa:=intervallo_date(1).tDataDa;
               intervallo_date_ok(1).tDataA:=intervallo_date(1).tDataA;
               scritto:='Y';
            ELSE
               FOR j IN intervallo_date_ok.FIRST .. intervallo_date_ok.LAST

               LOOP

                  IF    (intervallo_date(i).tDataDa >= intervallo_date_ok(j).tDataDa AND
                         intervallo_date(i).tDataDa <= intervallo_date_ok(j).tDataA) THEN
                        IF intervallo_date(i).tDataA > intervallo_date_ok(j).tDataA THEN
                           intervallo_date_ok(j).tDataA:=intervallo_date(i).tDataA;
                           scritto:='Y';
                           EXIT;
                        ELSE
                           scritto:='Y';
                           EXIT;
                        END IF;
                  ELSIF (intervallo_date(i).tDataA >= intervallo_date_ok(j).tDataDa AND
                         intervallo_date(i).tDataA <= intervallo_date_ok(j).tDataA) THEN
                        IF intervallo_date(i).tDataDa < intervallo_date_ok(j).tDataDa THEN
                           intervallo_date_ok(j).tDataDa:=intervallo_date(i).tDataDa;
                           scritto:='Y';
                           EXIT;
                        ELSE
                           scritto:='Y';
                           EXIT;
                        END IF;
                  ELSIF (intervallo_date(i).tDataDa < intervallo_date_ok(j).tDataDa AND
                         intervallo_date(i).tDataA > intervallo_date_ok(j).tDataA) THEN
                        intervallo_date_ok(j).tDataDa:=intervallo_date(i).tDataDa;
                        intervallo_date_ok(j).tDataA:=intervallo_date(i).tDataA;
                        scritto:='Y';
                        EXIT;
                  END IF;
               END LOOP;
            END IF;

            -- Inserisco un nuovo intervallo solo se non esistono intersezioni
            -- con le date precedentemente memorizzate

            IF scritto = 'N' THEN
               k:=intervallo_date_ok.COUNT +1;
               intervallo_date_ok(k).tDataDa:=intervallo_date(i).tDataDa;
               intervallo_date_ok(k).tDataA:=intervallo_date(i).tDataA;
            END IF;

         END LOOP;

         -- Verifica che il numero di record tra le due matrici sia identico

         IF intervallo_date_ok.COUNT = intervallo_date.COUNT THEN
            EXIT;
         ELSE
            intervallo_date:=intervallo_date_ok;
         END IF;

      END LOOP;

   END IF;

END normalizzaMatriceDate;

-- =================================================================================================
-- Unione di intervalli di date e separazione degli intervalli dell'anno del conguaglio da quelli
-- dell'anno precedente e successivo
-- =================================================================================================
PROCEDURE unisciMatriceDate
   (
    inEsercizioCompenso In NUMBER,
    intervallo_date_ok IN OUT CNRCTB545.intervalloDateTab,               --date senza sovrapposizioni
    intervallo_date_ok_unite IN OUT CNRCTB545.intervalloDateTab          --date in cui ho separato gli intervalli dell'anno precedente dal corrente e dal successivo
   ) IS
   i BINARY_INTEGER;
   j BINARY_INTEGER;
   k BINARY_INTEGER;

   numeroGG INTEGER;
   salta   CHAR(1);
   intervallo_date_appoggio CNRCTB545.intervalloDateTab;                 --date unite (cioè raggruppo gli intervalli contigui)
   intervallo_date_annoprec CNRCTB545.intervalloDateTab;                --date in cui ho separato gli intervalli dell'anno precedente dal resto
BEGIN

   -- Esco se la collezione delle date è vuota
   intervallo_date_appoggio.delete;

   IF intervallo_date_ok.COUNT = 0 THEN
      intervallo_date_ok_unite.DELETE;
      RETURN;
   END IF;

   -- Già so che non esistano intervalli di date con sovrapposizione
   -- unisco le date (leggo da intervallo_date_ok e riempio intervallo_date_appoggio)

   IF intervallo_date_ok.COUNT = 1 THEN
      intervallo_date_appoggio:=intervallo_date_ok;
   ELSE
         intervallo_date_appoggio.DELETE;
         FOR i IN intervallo_date_ok.FIRST .. intervallo_date_ok.LAST
         Loop
            IF intervallo_date_appoggio.FIRST IS NULL Then
               j:=1;
               intervallo_date_appoggio(j).tDataDa:=intervallo_date_ok(1).tDataDa;
               intervallo_date_appoggio(j).tDataA:=intervallo_date_ok(1).tDataA;
            Else
               If intervallo_date_appoggio(j).tDataA = intervallo_date_ok(i).tDataDa - 1 Then
                    intervallo_date_appoggio(j).tDataA := intervallo_date_ok(i).tDataA;
               Else
                    -- Inserisco un nuovo intervallo solo se non esistono intersezioni
                  -- con le date precedentemente memorizzate
                  j:=j+1;
                    intervallo_date_appoggio(j).tDataDa:=intervallo_date_ok(i).tDataDa;
                    intervallo_date_appoggio(j).tDataA:=intervallo_date_ok(i).tDataA;
               End If;
            End If;
         END LOOP;
   END IF;

   --separo l'esercizio precedente (leggo da intervallo_date_appoggio e riempio intervallo_date_annoprec)

   salta:='N';
   FOR i IN intervallo_date_appoggio.FIRST .. intervallo_date_appoggio.LAST

         LOOP

            numero_mesi_anno_precedente:=0;
            If intervallo_date_annoprec.FIRST IS NULL Then
            --sto leggendo il primo intervallo
         If To_Char(intervallo_date_appoggio(1).tDataDa,'yyyy') = inEsercizioCompenso Then
            --ho solo date dell'esercizio del conguaglio
            j:=1;
            intervallo_date_annoprec(j).tDataDa:=intervallo_date_appoggio(1).tDataDa;
                  intervallo_date_annoprec(j).tDataA:=intervallo_date_appoggio(1).tDataA;

                  salta:='Y';
               Else
                  --devo separare le date dell'esercizio precedente da quelle dell'esercizio corrente
      j:=1;
                  If To_Char(intervallo_date_appoggio(1).tDataDa,'yyyy') = To_Char(intervallo_date_appoggio(1).tDataA,'yyyy') Then
                     --tutto l'intervallo è nell'anno precedente
                     intervallo_date_annoprec(j).tDataDa:=intervallo_date_appoggio(1).tDataDa;
                     intervallo_date_annoprec(j).tDataA:=intervallo_date_appoggio(1).tDataA;
                     numero_mesi_anno_precedente:= numero_mesi_anno_precedente
                                                   +
                                                   Ceil(MONTHS_BETWEEN(intervallo_date_annoprec(1).tDataA,intervallo_date_annoprec(1).tDataDa));
                     salta:='N';
                  Else
                     --la prima data è nell'esercizio precedente, la seconda è nell'esercizio del conguaglio
                     intervallo_date_annoprec(j).tDataDa:=intervallo_date_appoggio(1).tDataDa;
                     intervallo_date_annoprec(j).tDataA:=To_Date('3112'||To_Char(intervallo_date_appoggio(1).tDataDa,'yyyy'),'ddmmyyyy');
                     numero_mesi_anno_precedente:= numero_mesi_anno_precedente
                                                   +
                                                   Ceil(MONTHS_BETWEEN(intervallo_date_annoprec(j).tDataA,intervallo_date_annoprec(j).tDataDa));
                     salta:='Y';
                     j:=j+1;
                     intervallo_date_annoprec(j).tDataDa:=To_Date('0101'||To_Char(intervallo_date_appoggio(1).tDataA,'yyyy'),'ddmmyyyy');
                     intervallo_date_annoprec(j).tDataA:=intervallo_date_appoggio(1).tDataA;
                  End If;
               End If;
      Else
      --non è il primo intervallo
        If salta = 'Y' Then
           -- da questo punto in poi carico intervallo_date_annoprec uguale a intervallo_date_appoggio
           j:=j+1;
           intervallo_date_annoprec(j).tDataDa:=intervallo_date_appoggio(i).tDataDa;
                 intervallo_date_annoprec(j).tDataA:=intervallo_date_appoggio(i).tDataA;
        Else
           -- devo ancora separare le date dell'esercizio precedente da quelle dell'esercizio corrente
           j:=j+1;
           If To_Char(intervallo_date_appoggio(i).tDataDa,'yyyy') = To_Char(intervallo_date_appoggio(i).tDataA,'yyyy') Then
                     --tutto l'intervallo è nell'anno precedente
                     intervallo_date_annoprec(j).tDataDa:=intervallo_date_appoggio(i).tDataDa;
                     intervallo_date_annoprec(j).tDataA:=intervallo_date_appoggio(i).tDataA;
                     numero_mesi_anno_precedente:= numero_mesi_anno_precedente
                                                   +
                                                   Ceil(MONTHS_BETWEEN(intervallo_date_annoprec(j).tDataA,intervallo_date_annoprec(j).tDataDa));
                     salta:='N';
                  Else
                     --la prima data è nell'esercizio precedente, la seconda è nell'esercizio del conguaglio
                     intervallo_date_annoprec(j).tDataDa:=intervallo_date_appoggio(i).tDataDa;
                     intervallo_date_annoprec(j).tDataA:=To_Date('3112'||To_Char(intervallo_date_appoggio(i).tDataDa,'yyyy'),'ddmmyyyy');
                     numero_mesi_anno_precedente:= numero_mesi_anno_precedente
                                                   +
                                                   Ceil(MONTHS_BETWEEN(intervallo_date_annoprec(j).tDataA,intervallo_date_annoprec(j).tDataDa));
                     salta:='Y';
                     j:=j+1;
                     intervallo_date_annoprec(j).tDataDa:=To_Date('0101'||To_Char(intervallo_date_appoggio(i).tDataA,'yyyy'),'ddmmyyyy');
                     intervallo_date_annoprec(j).tDataA:=intervallo_date_appoggio(i).tDataA;
                  End If;
        End If;
      End If;

         END LOOP;

   --separo l'esercizio successivo (leggo da intervallo_date_annoprec e riempio intervallo_date_ok_unite)

   salta:='N';
   FOR i IN intervallo_date_annoprec.FIRST .. intervallo_date_annoprec.LAST

         LOOP

            numero_mesi_anno_successivo:=0;
            If intervallo_date_ok_unite.FIRST IS NULL Then
            --sto leggendo il primo intervallo
               If To_Char(intervallo_date_annoprec(1).tDataDa,'yyyy') <= inEsercizioCompenso Then
                  If To_Char(intervallo_date_annoprec(1).tDataA,'yyyy') <= inEsercizioCompenso Then
                       --non è l'esercizio successivo
                       j:=1;
                 intervallo_date_ok_unite(j).tDataDa:=intervallo_date_annoprec(1).tDataDa;
                       intervallo_date_ok_unite(j).tDataA:=intervallo_date_annoprec(1).tDataA;
                  Else
                       --a cavallo tra i due esercizi
                       j:=1;
                 intervallo_date_ok_unite(j).tDataDa:=intervallo_date_annoprec(1).tDataDa;
                       intervallo_date_ok_unite(j).tDataA:=To_Date('3112'||To_Char(intervallo_date_annoprec(1).tDataDa,'yyyy'),'ddmmyyyy');

                       j:=j+1;
                       intervallo_date_ok_unite(j).tDataDa:=To_Date('0101'||To_Char(intervallo_date_annoprec(1).tDataA,'yyyy'),'ddmmyyyy');
                       intervallo_date_ok_unite(j).tDataA:=intervallo_date_annoprec(1).tDataA;

                       numero_mesi_anno_successivo:= numero_mesi_anno_successivo
                                                   +
                                                   Ceil(MONTHS_BETWEEN(intervallo_date_ok_unite(j).tDataA,intervallo_date_ok_unite(j).tDataDa));
                       salta:='Y';
                  End If;
               Else
                  --è già tutto anno successivo
                  j:=1;
            intervallo_date_ok_unite(j).tDataDa:=intervallo_date_annoprec(1).tDataDa;
                  intervallo_date_ok_unite(j).tDataA:=intervallo_date_annoprec(1).tDataA;

                  numero_mesi_anno_successivo:= numero_mesi_anno_successivo
                                                   +
                                                   Ceil(MONTHS_BETWEEN(intervallo_date_ok_unite(j).tDataA,intervallo_date_ok_unite(j).tDataDa));
                  salta:='Y';
               End If;
            Else
            --non è il primo intervallo
               If salta = 'Y' Then
           -- da questo punto in poi carico intervallo_date_ok_unite uguale a intervallo_date_annoprec
           j:=j+1;
           intervallo_date_ok_unite(j).tDataDa:=intervallo_date_annoprec(i).tDataDa;
                 intervallo_date_ok_unite(j).tDataA:=intervallo_date_annoprec(i).tDataA;
                 numero_mesi_anno_successivo:= numero_mesi_anno_successivo
                                                   +
                                                   Ceil(MONTHS_BETWEEN(intervallo_date_ok_unite(j).tDataA,intervallo_date_ok_unite(j).tDataDa));
         Else
           -- devo ancora separare le date dell'esercizio successivo da quelle dell'esercizio corrente
           If To_Char(intervallo_date_annoprec(i).tDataDa,'yyyy') <= inEsercizioCompenso Then
                    If To_Char(intervallo_date_annoprec(i).tDataA,'yyyy') <= inEsercizioCompenso Then
                         --non è l'esercizio successivo
                         j:=j+1;
                   intervallo_date_ok_unite(j).tDataDa:=intervallo_date_annoprec(i).tDataDa;
                         intervallo_date_ok_unite(j).tDataA:=intervallo_date_annoprec(i).tDataA;
                    Else
                         --a cavallo tra i due esercizi
                         j:=j+1;
                   intervallo_date_ok_unite(j).tDataDa:=intervallo_date_annoprec(i).tDataDa;
                         intervallo_date_ok_unite(j).tDataA:=To_Date('3112'||To_Char(intervallo_date_annoprec(i).tDataDa,'yyyy'),'ddmmyyyy');

                         j:=j+1;
                         intervallo_date_ok_unite(j).tDataDa:=To_Date('0101'||To_Char(intervallo_date_annoprec(i).tDataA,'yyyy'),'ddmmyyyy');
                         intervallo_date_ok_unite(j).tDataA:=intervallo_date_annoprec(i).tDataA;

                         numero_mesi_anno_successivo:= numero_mesi_anno_successivo
                                                     +
                                                     Ceil(MONTHS_BETWEEN(intervallo_date_ok_unite(j).tDataA,intervallo_date_ok_unite(j).tDataDa));
                         salta:='Y';
                    End If;
                 Else
                    --è tutto anno successivo
                    j:=j+1;
              intervallo_date_ok_unite(j).tDataDa:=intervallo_date_annoprec(i).tDataDa;
                    intervallo_date_ok_unite(j).tDataA:=intervallo_date_annoprec(i).tDataA;

                    numero_mesi_anno_successivo:= numero_mesi_anno_successivo
                                                     +
                                                     Ceil(MONTHS_BETWEEN(intervallo_date_ok_unite(j).tDataA,intervallo_date_ok_unite(j).tDataDa));
                    salta:='Y';
                 End If;
         End If;

            End If;
         END LOOP;

END unisciMatriceDate;

-- =================================================================================================
-- Controlla esistenza di intervalli spuri di recupero rate
-- =================================================================================================
PROCEDURE chkSpuriRecuperoRate
   (
    intervallo_date_neg CNRCTB545.intervalloDateTab
   ) IS

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Segnalo errore se la collezione delle date non è vuota

   IF intervallo_date_neg.COUNT > 0 THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Impossibile completare il conguaglio. Trovati intervalli di competenza per recupero rate ' ||
          'che non hanno trovato corrispondenza nei periodi dei compensi ordinari');
   END IF;

END chkSpuriRecuperoRate;

-- =================================================================================================
-- Determina il netto delle detrazioni personali e familiari se IRPEF risulta inferiore al totale
-- calcolato di queste
-- =================================================================================================
PROCEDURE nettizzaDetrazioni
   (
    aImporto NUMBER,
    aImDetrazioniPeNetto IN OUT NUMBER,
    aImDetrazioniLaNetto IN OUT NUMBER,
    aImDetrazioniCoNetto IN OUT NUMBER,
    aImDetrazioniFiNetto IN OUT NUMBER,
    aImDetrazioniAlNetto IN OUT NUMBER,
    aImDetrazioniCuneoNetto IN OUT NUMBER
   ) IS
   k BINARY_INTEGER;
   aImportoResto NUMBER(15,2);

BEGIN

   -- Riporto il totale delle detrazioni

   aImportoResto:=aImporto;

   BEGIN

      FOR k IN 1 .. 6

      LOOP

         IF    k = 1 THEN
               IF aImDetrazioniPeNetto > 0 THEN
                  aImDetrazioniPeNetto:=aImDetrazioniPeNetto - aImportoResto;
                  IF aImDetrazioniPeNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniPeNetto;
                     aImportoResto:=aImDetrazioniPeNetto * -1;
                     aImDetrazioniPeNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF k = 2 THEN
               IF aImDetrazioniLaNetto > 0 THEN
                  aImDetrazioniLaNetto:=aImDetrazioniLaNetto - aImportoResto;
                  IF aImDetrazioniLaNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniLaNetto;
                     aImportoResto:=aImDetrazioniLaNetto * -1;
                     aImDetrazioniLaNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF k = 3 THEN
               IF aImDetrazioniCoNetto > 0 THEN
                  aImDetrazioniCoNetto:=aImDetrazioniCoNetto - aImportoResto;
                  IF aImDetrazioniCoNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniCoNetto;
                     aImportoResto:=aImDetrazioniCoNetto * -1;
                     aImDetrazioniCoNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF k = 4 THEN
               IF aImDetrazioniFiNetto > 0 THEN
                  aImDetrazioniFiNetto:=aImDetrazioniFiNetto - aImportoResto;
                  IF aImDetrazioniFiNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniFiNetto;
                     aImportoResto:=aImDetrazioniFiNetto * -1;
                     aImDetrazioniFiNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF k = 5 THEN
               IF aImDetrazioniAlNetto > 0 THEN
                  aImDetrazioniAlNetto:=aImDetrazioniAlNetto - aImportoResto;
                  IF aImDetrazioniAlNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniAlNetto;
                     aImportoResto:=aImDetrazioniAlNetto * -1;
                     aImDetrazioniAlNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF k = 6 THEN
               IF aImDetrazioniCuneoNetto > 0 THEN
                  aImDetrazioniCuneoNetto:=aImDetrazioniCuneoNetto - aImportoResto;
                  IF aImDetrazioniCuneoNetto < 0 THEN
                     aImportoResto:=aImDetrazioniCuneoNetto * -1;
                     aImDetrazioniCuneoNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         END IF;

      END LOOP;

   END;

END nettizzaDetrazioni;

-- =================================================================================================
-- Rinumerazione con le chiavi definitive di conguaglio e compenso associato nella tabella
-- ASS_COMPENSO_CONGUAGLIO.
-- =================================================================================================
PROCEDURE upgKeyAssCompensoConguaglio
   (
    inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
    inPgConguaglioOld CONGUAGLIO.pg_conguaglio%TYPE,
    inPgConguaglioNew CONGUAGLIO.pg_conguaglio%TYPE,
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompensoOld COMPENSO.pg_compenso%TYPE,
    inPgCompensoNew COMPENSO.pg_compenso%TYPE
   ) IS
   tiInserimentoCancellazione CHAR(1);
   isTemporaneo CHAR(1);
   eseguiLock CHAR(1);
   aSaldoDeduzione NUMBER(15,2);
   aRecConguaglio CONGUAGLIO%ROWTYPE;
   aRecAnagrafico ANAGRAFICO%ROWTYPE;
   aRecMontanti MONTANTI%ROWTYPE;

BEGIN

   eseguiLock:='Y';
   isTemporaneo:='N';
   tiInserimentoCancellazione:='I';

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento della chiave conguaglio nella associazione compenso conguaglio

   UPDATE ASS_COMPENSO_CONGUAGLIO
   SET    pg_conguaglio = inPgConguaglioNew
   WHERE  cd_cds_conguaglio = inCdsConguaglio AND
          cd_uo_conguaglio = inUoConguaglio AND
          esercizio_conguaglio = inEsercizioConguaglio AND
          pg_conguaglio = inPgConguaglioOld;

   UPDATE ASS_COMPENSO_CONGUAGLIO
   SET    pg_compenso = inPgCompensoNew
   WHERE  cd_cds_compenso = inCdsCompenso AND
          cd_uo_compenso = inUoCompenso AND
          esercizio_compenso = inEsercizioCompenso AND
          pg_compenso = inPgCompensoOld AND
          cd_cds_conguaglio = inCdsConguaglio AND
          cd_uo_conguaglio = inUoConguaglio AND
          esercizio_conguaglio = inEsercizioConguaglio AND
          pg_conguaglio = inPgConguaglioNew;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dei montanti per il delta goduto, dovuto della deduzione IRPEF.
   -- Gestione, con la chiave definitiva del conguaglio, della tabella RATEIZZA_CLASSIFIC_CORI

   BEGIN

      -- Recupero dei dati del conguaglio e dell'anagrafico associato

      aRecConguaglio:=CNRCTB545.getConguaglio(inCdsConguaglio,
                                              inUoConguaglio,
                                              inEsercizioConguaglio,
                                              inPgConguaglioNew,
                                              eseguiLock);

      aRecAnagrafico:=CNRCTB080.getAnag(aRecConguaglio.cd_terzo);

      ----------------------------------------------------------------------------------------------
      -- Aggiornamento dell'importo della deduzione IRPEF su montanti

      -- Calcolo del saldo della deduzione, se diverso da zero si aggiornano i montanti

      aSaldoDeduzione:=(aRecConguaglio.im_deduzione_dovuto - aRecConguaglio.im_deduzione_goduto);

      -- Se diverso da zero eseguo l'aggiornamento dei montanti
      IF aSaldoDeduzione != 0 Or
         aRecTipoTrattamento.fl_agevolazioni_cervelli = 'Y' Or
         glbEsisteCompensoCervelli = 'Y' or
         --aRecTipoTrattamento.cd_trattamento in ('T162','T163','CONGL20','CONGL30')
         aRecTipoTrattamento.fl_agevolazioni_rientro_lav = 'Y'Then

         aRecMontanti:=CNRCTB080.getMontanti(inEsercizioConguaglio,
                                             aRecAnagrafico.cd_anag,
                                             eseguiLock);
         aRecMontanti.deduzione_irpef_altri:=aRecMontanti.deduzione_irpef_altri + aSaldoDeduzione;

         If aRecTipoTrattamento.fl_agevolazioni_cervelli = 'Y' Or glbEsisteCompensoCervelli = 'Y' or
            --aRecTipoTrattamento.cd_trattamento in ('T162','T163','CONGL20','CONGL30')
            aRecTipoTrattamento.fl_agevolazioni_rientro_lav = 'Y' Then
              aRecMontanti.irpef_lordo_altri:= aRecConguaglio.imponibile_irpef_lordo;
         End If;

         aRecMontanti.irpef_altri:=aRecMontanti.irpef_lordo_altri - aRecMontanti.deduzione_irpef_altri;

         CNRCTB080.upgMontantiAltri(aRecMontanti);

      END IF;

      ----------------------------------------------------------------------------------------------
      -- Aggiornamento del riferimento al conguaglio su tabella RATEIZZA_CLASSIFIC_CORI e creazione
      -- dello storico

      -- Creazione dello storico

      upgStrRateizzaAddTerritorio(tiInserimentoCancellazione,
                                  aRecConguaglio,
                                  aRecAnagrafico.cd_anag,
                                  isTemporaneo);

      -- Cancellazione dei record definitivi in RATEIZZA_CLASSIFIC_CORI

      BEGIN

         DELETE FROM RATEIZZA_CLASSIFIC_CORI
         WHERE  esercizio = aRecConguaglio.esercizio AND
                cd_anag = aRecAnagrafico.cd_anag AND
                fl_temporaneo = 'N';

      END;

      -- Se il conguaglio prevede l'accantonamento delle addizionali territorio allora aggiorno il
      -- riferimento definitivo al conguaglio altrimenti elimino

      IF aRecConguaglio.fl_accantona_add_terr = 'Y' THEN

         BEGIN

            UPDATE RATEIZZA_CLASSIFIC_CORI
            SET    pg_conguaglio = aRecConguaglio.pg_conguaglio,
                   fl_temporaneo = 'N'
            WHERE  esercizio = aRecConguaglio.esercizio AND
                   cd_anag = aRecAnagrafico.cd_anag AND
                   cd_cds_conguaglio = aRecConguaglio.cd_cds AND
                   cd_uo_conguaglio = aRecConguaglio.cd_unita_organizzativa AND
                   pg_conguaglio = inPgConguaglioOld;

         END;

      END IF;

      ----------------------------------------------------------------------------------------------
      -- Aggiornamento dell'importo rateizzato su tabella RATEIZZA_CLASSIFIC_CORI

      IF (aRecConguaglio.im_addreg_rate_eseprec > 0 OR
          aRecConguaglio.im_addprov_rate_eseprec > 0 OR
          aRecConguaglio.im_addcom_rate_eseprec > 0) THEN

         upgImRateizzatoAddTerritorio(tiInserimentoCancellazione,
                                      aRecConguaglio,
                                      aRecAnagrafico.cd_anag,
                                      isTemporaneo);

      END IF;

      -- Se per il terzo in esame è previsto l'acconto aggiorno il riferimento del conguaglio
      -- su ACCONTO_CLASSIFIC_CORI
      If Nvl(glbImportoAddComAccGoduto,0) != 0 Then
            Update ACCONTO_CLASSIFIC_CORI
            Set cd_cds_conguaglio = aRecConguaglio.cd_cds,
                cd_uo_conguaglio = aRecConguaglio.cd_unita_organizzativa,
                pg_conguaglio = aRecConguaglio.pg_conguaglio,
                duva = dataOdierna,
                utuv = aRecConguaglio.utuv,
                pg_ver_rec = pg_ver_rec + 1
            Where esercizio = aRecConguaglio.esercizio
              And cd_anag = aRecAnagrafico.cd_anag
              And cd_classificazione_cori = cnrctb545.isCoriAddCom
              And pg_conguaglio Is Null;
      End If;
   END;

END upgKeyAssCompensoConguaglio;

-- =================================================================================================
-- Elimina le associazioni al compenso congualio presenti in ASS_COMPENSO_CONGUAGLIO e in RATEIZZA_CLASSIFIC_CORI
-- =================================================================================================
PROCEDURE eliminaAssCompensoConguaglio
   (
    inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
    inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE
   ) IS

BEGIN

   -- Cancellazione righe da tabella ASS_COMPENSO_CONGUAGLIO

   DELETE FROM ASS_COMPENSO_CONGUAGLIO
   WHERE  cd_cds_conguaglio = inCdsConguaglio AND
          cd_uo_conguaglio = inUoConguaglio AND
          esercizio_conguaglio = inEsercizioConguaglio AND
          pg_conguaglio = inPgConguaglio;

   -- Cancellazione righe da tabella RATEIZZA_CLASSIFIC_CORI

   DELETE FROM RATEIZZA_CLASSIFIC_CORI
   WHERE  cd_cds_conguaglio = inCdsConguaglio AND
          cd_uo_conguaglio = inUoConguaglio AND
          esercizio = inEsercizioConguaglio AND
          pg_conguaglio = inPgConguaglio;

END eliminaAssCompensoConguaglio;

-- =================================================================================================
-- Main eliminazione di un conguaglio
-- =================================================================================================
PROCEDURE eseguiDelConguaglio
   (
    esercizioScrivania CONGUAGLIO.esercizio%TYPE,
    inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
    inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
    inUtente CONGUAGLIO.utuv%TYPE,
    statoCancella IN OUT NUMBER
   ) IS

BEGIN

   statoCancella:=cancellaConguaglio(esercizioScrivania,
                                     inCdsConguaglio,
                                     inUoConguaglio,
                                     inEsercizioConguaglio,
                                     inPgConguaglio,
                                     inUtente);

END  eseguiDelConguaglio;

-- =================================================================================================
-- Controllo ed esecuzione della cancellazione di un conguaglio
-- =================================================================================================
FUNCTION cancellaConguaglio
   (
    esercizioScrivania CONGUAGLIO.esercizio%TYPE,
    inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
    inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
    inUtente CONGUAGLIO.utuv%TYPE
   ) RETURN NUMBER IS
   tiInserimentoCancellazione CHAR(1);
   isTemporaneo CHAR(1);
   eseguiLock CHAR(1);
   dataOdierna DATE;
   dataCancellazioneLocica DATE;
   aTiOrigineEliminaLogico CHAR(2);
   aSaldoDeduzione NUMBER(15,2);

   aRecCompensoConguaglioBase V_COMPENSO_CONGUAGLIO_BASE%ROWTYPE;
   aRecCompenso COMPENSO%ROWTYPE;
   aRecMontanti MONTANTI%ROWTYPE;

BEGIN
   -------------------------------------------------------------------------------------------------
   -- Valorizzazione variabili

   eseguiLock:='Y';
   dataOdierna := sysdate;
   tiInserimentoCancellazione:='C';
   isTemporaneo:='N';

   BEGIN
      -------------------------------------------------------------------------------------------------
      -- Lettura dati di base legati al conguaglio in calcolo, testata conguaglio e anagrafico.
      -- I dati sono memorizzati rispettivamente in aRecConguaglio e aRecAnagrafico

      getDatiBaseConguaglio(inCdsConguaglio,
                            inUoConguaglio,
                            inEsercizioConguaglio,
                            inPgConguaglio,
                            eseguiLock);
      -- Controllo che il conguaglio sia l'ultimo per il soggetto anagrafico

      SELECT * INTO aRecCompensoConguaglioBase
      FROM   V_COMPENSO_CONGUAGLIO_BASE A
      WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
             A.is_compenso_conguaglio = 'Y' AND
             A.stato_cofi != 'A' AND
             A.dacr_conguaglio =
                (SELECT MAX(B.dacr_conguaglio)
                 FROM   V_COMPENSO_CONGUAGLIO_BASE B
                 WHERE  B.cd_anag = A.cd_anag AND
                        B.is_compenso_conguaglio = A.is_compenso_conguaglio AND
                        B.stato_cofi != 'A');

      IF (aRecConguaglio.cd_cds != aRecCompensoConguaglioBase.cd_cds_conguaglio OR
          aRecConguaglio.cd_unita_organizzativa != aRecCompensoConguaglioBase.cd_uo_conguaglio OR
          aRecConguaglio.esercizio != aRecCompensoConguaglioBase.esercizio_conguaglio OR
          aRecConguaglio.pg_conguaglio != aRecCompensoConguaglioBase.pg_conguaglio) THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Impossibile eliminare un conguaglio che non è l''ultimo generato per il codice anagrafico ' ||
             aRecAnagrafico.cd_anag);
      END IF;

      -------------------------------------------------------------------------------------------------
      -- Lettura del compenso associato al conguaglio

      aRecCompenso:=CNRCTB545.getCompenso(aRecCompensoConguaglioBase.cd_cds_compenso,
                                          aRecCompensoConguaglioBase.cd_uo_compenso,
                                          aRecCompensoConguaglioBase.esercizio_compenso,
                                          aRecCompensoConguaglioBase.pg_compenso,
                                          eseguiLock);

      -- Il compenso risulta essere già in stato annullato
      IF aRecCompenso.stato_cofi = 'A' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Il compenso conguaglio risulta essere già stato annullato in data ' ||
             TO_CHAR(aRecCompenso.dt_cancellazione,'DD/MM/YYYY'));
      END IF;

      -- Il compenso risulta essere associato a mandati e/o reversali

      IF aRecCompenso.stato_cofi = 'P' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Impossibile eliminare un compenso conguaglio che risulta pagato. ' ||
             'Occorre prima annullare il mandato o la reversale principale associata');
      END IF;

      -- Il compenso risulta essere associato ad una spesa del fondo economale

      IF aRecCompenso.stato_pagamento_fondo_eco = 'R' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Impossibile eliminare un compenso conguaglio che risulta associato ad una spesa su fondo economale');
      END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Cancellazione degli elementi associati al conguaglio

   BEGIN
      DELETE FROM RATEIZZA_CLASSIFIC_CORI
      WHERE  cd_cds_conguaglio = aRecConguaglio.cd_cds AND
             cd_uo_conguaglio = aRecConguaglio.cd_unita_organizzativa AND
             esercizio = aRecConguaglio.esercizio And
             pg_conguaglio = aRecConguaglio.pg_conguaglio;

      -- Eventuale migrazione dallo storico a tabella corrente se l'ultimo conguaglio prevede accantonamento

      upgStrRateizzaAddTerritorio(tiInserimentoCancellazione,
                                  aRecConguaglio,
                                  aRecAnagrafico.cd_anag,
                                  isTemporaneo);

      -- Aggiornamento dell'importo rateizzato su tabella RATEIZZA_CLASSIFIC_CORI

      IF (aRecConguaglio.im_addreg_rate_eseprec > 0 OR
          aRecConguaglio.im_addprov_rate_eseprec > 0 OR
          aRecConguaglio.im_addcom_rate_eseprec > 0) THEN

         upgImRateizzatoAddTerritorio(tiInserimentoCancellazione,
                                      aRecConguaglio,
                                      aRecAnagrafico.cd_anag,
                                      isTemporaneo);

      END IF;

      -- Eliminazione dei riferimenti del conguaglio da ACCONTO_CLASSIFIC_CORI
      Begin
            Update ACCONTO_CLASSIFIC_CORI
            Set cd_cds_conguaglio = Null,
                cd_uo_conguaglio = Null,
                pg_conguaglio = Null,
                duva = dataOdierna,
                utuv = aRecConguaglio.utuv,
                pg_ver_rec = pg_ver_rec + 1
            Where esercizio = aRecConguaglio.esercizio
              And cd_anag = aRecAnagrafico.cd_anag
              And cd_classificazione_cori = cnrctb545.isCoriAddCom
              And cd_cds_conguaglio = aRecConguaglio.cd_cds
              And cd_uo_conguaglio = aRecConguaglio.cd_unita_organizzativa
              And pg_conguaglio = aRecConguaglio.pg_conguaglio;
      End;

      -- Aggiornamento dei montanti (solo deduzione......)

      -- Recupero dati del tipo trattamento indicato nel conguaglio
      aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento (aRecConguaglio.cd_trattamento,
                                                         aRecConguaglio.dt_registrazione);
      -- Verifico se esistono compensi associati al conguaglio con trattamento avente fl_agevolazioni_cervelli = 'Y'
      Declare
         esisteCompenso  NUMBER:=0;
      Begin
        SELECT COUNT(*) INTO esisteCompenso
        FROM   DUAL
        WHERE  EXISTS
             (SELECT 1
              FROM   ASS_COMPENSO_CONGUAGLIO A, COMPENSO C, TIPO_TRATTAMENTO T
              WHERE  A.cd_cds_compenso = C.cd_cds AND
                     A.cd_uo_compenso = C.cd_unita_organizzativa AND
                 A.esercizio_compenso = C.esercizio AND
                 A.pg_compenso = C.pg_compenso And
                     C.cd_trattamento = T.cd_trattamento And
         A.cd_cds_conguaglio = aRecConguaglio.cd_cds AND
                 A.cd_uo_conguaglio = aRecConguaglio.cd_unita_organizzativa AND
                 A.esercizio_conguaglio = aRecConguaglio.esercizio AND
                 A.pg_conguaglio = aRecConguaglio.pg_conguaglio And
                     T.fl_agevolazioni_cervelli ='Y');

        If esisteCompenso = 0 Then
            glbEsisteCompensoCervelli := 'N';
        Else
            glbEsisteCompensoCervelli := 'Y';
        End If;
      End;

      aSaldoDeduzione:=((aRecConguaglio.im_deduzione_dovuto - aRecConguaglio.im_deduzione_goduto) * -1);

      IF aSaldoDeduzione != 0 Or
         aRecTipoTrattamento.fl_agevolazioni_cervelli = 'Y' Or
         glbEsisteCompensoCervelli = 'Y' or
         --aRecTipoTrattamento.cd_trattamento in ('T162','T163','CONGL20','CONGL30')
         aRecTipoTrattamento.fl_agevolazioni_rientro_lav = 'Y' Then

         aRecMontanti:=CNRCTB080.getMontanti(inEsercizioConguaglio,
                                             aRecAnagrafico.cd_anag,
                                             eseguiLock);
         aRecMontanti.deduzione_irpef_altri:=aRecMontanti.deduzione_irpef_altri + aSaldoDeduzione;

         Declare
             totImponibileFiscale  NUMBER:=0;
             totImponibileUltCong  NUMBER:=0;
             aRecUltimoConguaglio CONGUAGLIO%ROWTYPE;
         Begin
          If aRecTipoTrattamento.fl_agevolazioni_cervelli = 'Y' Or glbEsisteCompensoCervelli = 'Y' Or
             --aRecTipoTrattamento.cd_trattamento in ('T162','T163','CONGL20','CONGL30')
             aRecTipoTrattamento.fl_agevolazioni_rientro_lav = 'Y' Then
            --devo riprendere l'imponibile lordo fiscale dai compensi legati al conguaglio (compenso.imponibile_fiscale)
            -- Verifico se esistono compensi associati al conguaglio con trattamento avente fl_agevolazioni_cervelli = 'Y'

            Select Nvl(Sum(C.imponibile_fiscale),0)
            Into   totImponibileFiscale
            From   ASS_COMPENSO_CONGUAGLIO A, COMPENSO C
            Where  A.cd_cds_compenso = C.cd_cds AND
                   A.cd_uo_compenso = C.cd_unita_organizzativa AND
               A.esercizio_compenso = C.esercizio AND
               A.pg_compenso = C.pg_compenso And
       A.cd_cds_conguaglio = aRecConguaglio.cd_cds AND
               A.cd_uo_conguaglio = aRecConguaglio.cd_unita_organizzativa AND
               A.esercizio_conguaglio = aRecConguaglio.esercizio AND
               A.pg_conguaglio = aRecConguaglio.pg_conguaglio And
               C.fl_compenso_conguaglio = 'N';

            aRecMontanti.irpef_lordo_altri:= totImponibileFiscale;

            --a questo devo aggiungere l'imponibile irpef lordo del'ultimo conguaglio (se esiste)
            Begin
             Select * --imponibile_irpef_lordo
             Into   aRecUltimoConguaglio --totImponibileUltCong
             From   CONGUAGLIO C
             Where  (c.cd_cds, c.cd_unita_organizzativa, c.esercizio, c.pg_conguaglio)
                   In
                   (Select b.cd_cds_conguaglio, b.cd_uo_conguaglio, b.esercizio_conguaglio, b.pg_conguaglio
                    From V_COMPENSO_CONGUAGLIO_BASE b
                    Where c.cd_cds = b.cd_cds_conguaglio
                      And c.cd_unita_organizzativa = b.cd_uo_conguaglio
                      And c.esercizio = b.esercizio_conguaglio
                      And c.pg_conguaglio = b.pg_conguaglio
                      And b.cd_anag = aRecAnagrafico.cd_anag
                    And b.is_compenso_conguaglio = 'Y'
                    And b.stato_cofi != 'A'
                    And b.dacr_conguaglio =
                  (SELECT MAX(e.dacr_conguaglio)
                   FROM   V_COMPENSO_CONGUAGLIO_BASE e
                   WHERE  e.cd_anag = b.cd_anag AND
                          e.is_compenso_conguaglio = b.is_compenso_conguaglio AND
                          e.stato_cofi != 'A' And
                          (aRecConguaglio.cd_cds != e.cd_cds_conguaglio
                          OR
                  aRecConguaglio.cd_unita_organizzativa != e.cd_uo_conguaglio
                  OR
                  aRecConguaglio.esercizio != e.esercizio_conguaglio
                  OR
                  aRecConguaglio.pg_conguaglio != e.pg_conguaglio)
                  ));

                          totImponibileUltCong := aRecUltimoConguaglio.imponibile_irpef_lordo;
            Exception
              When No_Data_Found Then
                 totImponibileUltCong := 0;
            End;
            aRecMontanti.irpef_lordo_altri := aRecMontanti.irpef_lordo_altri + totImponibileUltCong;
          End If;
         End;

         aRecMontanti.irpef_altri:=aRecMontanti.irpef_lordo_altri - aRecMontanti.deduzione_irpef_altri;
   ----------------------
         CNRCTB080.upgMontantiAltri(aRecMontanti);
      END IF;
      -- Cancellazione delle associazioni compenso_conguaglio

      eliminaAssCompensoConguaglio(aRecConguaglio.cd_cds,
                                   aRecConguaglio.cd_unita_organizzativa,
                                   aRecConguaglio.esercizio,
                                   aRecConguaglio.pg_conguaglio);

      -- Cancellazione logica o fisica del compenso e corrispondente ritorno per il conguaglio
      IF (aRecCompenso.ti_associato_manrev = 'N' AND
            (
               (aRecCompenso.stato_coge = 'N' OR
                aRecCompenso.stato_coge = 'X') AND
               (aRecCompenso.stato_coan = 'N' OR
                aRecCompenso.stato_coan = 'X')
             )
         ) THEN

         UPDATE CONGUAGLIO
         SET    cd_cds_compenso = NULL,
                cd_uo_compenso = NULL,
                esercizio_compenso = NULL,
                pg_compenso = NULL
         WHERE  cd_cds = aRecConguaglio.cd_cds AND
                cd_unita_organizzativa = aRecConguaglio.cd_unita_organizzativa AND
                esercizio = aRecConguaglio.esercizio AND
                pg_conguaglio = aRecConguaglio.pg_conguaglio;

         CNRCTB545.eliminaFisicoCompenso(aRecCompenso.cd_cds,
                                         aRecCompenso.cd_unita_organizzativa,
                                         aRecCompenso.esercizio,
                                         aRecCompenso.pg_compenso);

         RETURN CNRCTB545.isCancellazioneFisica;
      ELSE
         if (TO_CHAR(dataOdierna, 'YYYY') <> esercizioScrivania) then
            dataCancellazioneLocica:=trunc(CNRCTB008.getTimestampContabile(aRecConguaglio.esercizio, dataOdierna));
         else
            dataCancellazioneLocica:=trunc(dataOdierna);
   end if;

         UPDATE CONGUAGLIO
         SET    dt_cancellazione = dataCancellazioneLocica,
                pg_ver_rec = pg_ver_rec + 1,
                duva = dataOdierna,
                utuv = inUtente
         WHERE  cd_cds = aRecConguaglio.cd_cds AND
                cd_unita_organizzativa = aRecConguaglio.cd_unita_organizzativa AND
                esercizio = aRecConguaglio.esercizio AND
                pg_conguaglio = aRecConguaglio.pg_conguaglio;

         IF (
               (aRecCompenso.stato_coge = 'N' OR
                aRecCompenso.stato_coge = 'X') AND
               (aRecCompenso.stato_coan = 'N' OR
                aRecCompenso.stato_coan = 'X')
            ) THEN
            aTiOrigineEliminaLogico:='GN';
         ELSE
            IF    (aRecCompenso.stato_coan = 'N' OR
                   aRecCompenso.stato_coan = 'X') THEN
                  aTiOrigineEliminaLogico:='G1';
            ELSIF (aRecCompenso.stato_coge = 'N' OR
                   aRecCompenso.stato_coge = 'X') THEN
                  aTiOrigineEliminaLogico:='G2';
            ELSE
                  aTiOrigineEliminaLogico:='GY';
            END IF;
         END IF;

         CNRCTB545.eliminaLogicoCompenso(aRecCompenso.cd_cds,
                                         aRecCompenso.cd_unita_organizzativa,
                                         aRecCompenso.esercizio,
                                         aRecCompenso.pg_compenso,
                                         inUtente,
                                         aTiOrigineEliminaLogico);

         RETURN CNRCTB545.isCancellazioneLogica;

      END IF;

   END;
End cancellaConguaglio;

-- ==================================================================================================
-- Ritorna i record di RATEIZZA_CLASSIFIC_CORI o RATEIZZA_CLASSIFIC_CORI_S per addizionali territorio
-- ==================================================================================================
PROCEDURE getRateizzaAddTerritorio
   (
    aEsercizio NUMBER,
    aCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    aUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    aPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
    aCdAnag NUMBER,
    isStorico CHAR,
    isTemporaneo CHAR,
    eseguiLock CHAR,
    aRecRateizzaClassificCoriC0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    aRecRateizzaClassificCoriP0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    aRecRateizzaClassificCoriR0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE
   ) IS

   aCdClassificazioneCoriBase CLASSIFICAZIONE_CORI.cd_classificazione_cori%TYPE;
   j BINARY_INTEGER;

   aRecRateizzaClassificCoriBase RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

BEGIN

   FOR j IN 1 .. 3

   LOOP

      IF    j = 1 THEN
            aCdClassificazioneCoriBase:=CNRCTB545.isCoriAddCom;
      ELSIF j = 2 THEN
            aCdClassificazioneCoriBase:=CNRCTB545.isCoriAddPro;
      ELSIF j = 3 THEN
            aCdClassificazioneCoriBase:=CNRCTB545.isCoriAddReg;
      END IF;

      IF isStorico = 'N' THEN

         aRecRateizzaClassificCoriBase:=CNRCTB545.getRateizzaClassificCori(aEsercizio,
                                                                           aCdAnag,
                                                                           isTemporaneo,
                                                                           aCdClassificazioneCoriBase,
                                                                           eseguiLock);

      ELSE

         aRecRateizzaClassificCoriBase:=CNRCTB545.getRateizzaClassificCoriStr(aEsercizio,
                                                                              aCdsConguaglio,
                                                                              aUoConguaglio,
                                                                              aPgConguaglio,
                                                                              aCdAnag,
                                                                              isTemporaneo,
                                                                              aCdClassificazioneCoriBase,
                                                                              eseguiLock);

      END IF;

      IF    j = 1 THEN
            aRecRateizzaClassificCoriC0:=aRecRateizzaClassificCoriBase;
      ELSIF j = 2 THEN
            aRecRateizzaClassificCoriP0:=aRecRateizzaClassificCoriBase;
      ELSIF j = 3 THEN
            aRecRateizzaClassificCoriR0:=aRecRateizzaClassificCoriBase;
      END IF;

   END LOOP;

END getRateizzaAddTerritorio;

-- ==================================================================================================
-- Aggiornamento della tabella RATEIZZA_CLASSIFIC_CORI_S per inserimento o cancellazione di conguaglio
-- ==================================================================================================
PROCEDURE upgStrRateizzaAddTerritorio
   (
    tiInserimentoCancellazione CHAR,
    aRecConguaglio CONGUAGLIO%ROWTYPE,
    aCdAnag NUMBER,
    isTemporaneo CHAR
   ) IS

   eseguiLock CHAR(1);
   isStorico CHAR(1);

   j BINARY_INTEGER;

   aRecRateizzaClassificCoriBase RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriC0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriP0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriR0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aTmpRecCompensoConguaglioBase V_COMPENSO_CONGUAGLIO_BASE%ROWTYPE;

BEGIN

   eseguiLock:='Y';

   BEGIN

      IF tiInserimentoCancellazione = 'I' THEN

         isStorico:='N';

         getRateizzaAddTerritorio(aRecConguaglio.esercizio,
                                  aRecConguaglio.cd_cds,
                                  aRecConguaglio.cd_unita_organizzativa,
                                  aRecConguaglio.pg_conguaglio,
                                  aCdAnag,
                                  isStorico,
                                  isTemporaneo,
                                  eseguiLock,
                                  aRecRateizzaClassificCoriC0,
                                  aRecRateizzaClassificCoriP0,
                                  aRecRateizzaClassificCoriR0);

         isStorico:='Y';

         FOR j IN 1 .. 3

         LOOP

            IF    j = 1 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriC0;
            ELSIF j = 2 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriP0;
            ELSIF j = 3 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriR0;
            END IF;

            IF aRecRateizzaClassificCoriBase.cd_cds_conguaglio IS NOT NULL THEN
               CNRCTB545.insRateizzaClassificCori(aRecRateizzaClassificCoriBase,
                                                  isStorico);
            END IF;

         END LOOP;

      ELSE

         -- Estrazione dell'ultimo conguaglio precedente a quello che si intende cancellare
         BEGIN

            SELECT * INTO aTmpRecCompensoConguaglioBase
            FROM   V_COMPENSO_CONGUAGLIO_BASE A
            WHERE  A.cd_anag = aRecAnagrafico.cd_anag AND
                   A.is_compenso_conguaglio = 'Y' AND
                   A.stato_cofi != 'A' AND
                   A.dacr_conguaglio =
                      (SELECT MAX(B.dacr_conguaglio)
                       FROM   V_COMPENSO_CONGUAGLIO_BASE B
                       WHERE  B.cd_anag = A.cd_anag AND
                              B.is_compenso_conguaglio = A.is_compenso_conguaglio AND
                              B.stato_cofi != 'A' AND
                              (B.cd_cds_conguaglio || B.cd_uo_conguaglio || TO_CHAR(B.esercizio_conguaglio) || TO_CHAR(B.pg_conguaglio)) !=
                              (aRecConguaglio.cd_cds || aRecConguaglio.cd_unita_organizzativa || TO_CHAR(aRecConguaglio.esercizio) || TO_CHAR(aRecConguaglio.pg_conguaglio))
                      );

         EXCEPTION

            WHEN no_data_found THEN
                 RETURN;

         END;

         -- Verifico se questo ha una immagine storica da riportare

         isStorico:='Y';

         getRateizzaAddTerritorio(aTmpRecCompensoConguaglioBase.esercizio_conguaglio,
                                  aTmpRecCompensoConguaglioBase.cd_cds_conguaglio,
                                  aTmpRecCompensoConguaglioBase.cd_uo_conguaglio,
                                  aTmpRecCompensoConguaglioBase.pg_conguaglio,
                                  aCdAnag,
                                  isStorico,
                                  isTemporaneo,
                                  eseguiLock,
                                  aRecRateizzaClassificCoriC0,
                                  aRecRateizzaClassificCoriP0,
                                  aRecRateizzaClassificCoriR0);

         -- Copia da storico a attuale

         isStorico:='N';

         FOR j IN 1 .. 3

         LOOP

            IF    j = 1 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriC0;
            ELSIF j = 2 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriP0;
            ELSIF j = 3 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriR0;
            END IF;
            IF aRecRateizzaClassificCoriBase.cd_cds_conguaglio IS NOT NULL THEN
               CNRCTB545.insRateizzaClassificCori(aRecRateizzaClassificCoriBase,
                                                  isStorico);
            END IF;
         END LOOP;

         -- Cancellazione dello storico da tabella RATEIZZA_CLASSIFIC_CORI_S

         DELETE FROM RATEIZZA_CLASSIFIC_CORI_S
         WHERE  cd_cds_conguaglio = aTmpRecCompensoConguaglioBase.cd_cds_conguaglio AND
                cd_uo_conguaglio = aTmpRecCompensoConguaglioBase.cd_uo_conguaglio AND
                esercizio = aTmpRecCompensoConguaglioBase.esercizio_conguaglio AND
                pg_conguaglio = aTmpRecCompensoConguaglioBase.pg_conguaglio;

      END IF;

   END;

END upgStrRateizzaAddTerritorio;

-- ==================================================================================================
-- Aggiornamento della tabella RATEIZZA_CLASSIFIC_CORI per modifica dell'importo rateizzato
-- ==================================================================================================
PROCEDURE upgImRateizzatoAddTerritorio
   (
    tiInserimentoCancellazione CHAR,
    aRecConguaglio CONGUAGLIO%ROWTYPE,
    aCdAnag NUMBER,
    isTemporaneo CHAR
   ) IS

   eseguiLock CHAR(1);
   isStorico CHAR(1);
   aStringaTmp VARCHAR2(50);

   imAddregRateEseprec NUMBER(15,2);
   imAddprovRateEseprec NUMBER(15,2);
   imAddcomRateEseprec NUMBER(15,2);
   aImportoBase NUMBER(15,2);

   j BINARY_INTEGER;

   aRecRateizzaClassificCoriBase RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriC0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriP0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriR0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

BEGIN

   eseguiLock:='Y';

   BEGIN

      -----------------------------------------------------------------------------------------------
      -- Aggiornamento dell'importo rateizzato sui record di RATEIZZA_CLASSIFIC_CORI

      -- Normalizzazione importi per inserimento o cancellazione

      IF tiInserimentoCancellazione = 'I' THEN
         imAddregRateEseprec:=aRecConguaglio.im_addreg_rate_eseprec;
         imAddprovRateEseprec:=aRecConguaglio.im_addprov_rate_eseprec;
         imAddcomRateEseprec:=aRecConguaglio.im_addcom_rate_eseprec;
      ELSE
         imAddregRateEseprec:=(aRecConguaglio.im_addreg_rate_eseprec * -1);
         imAddprovRateEseprec:=(aRecConguaglio.im_addprov_rate_eseprec * -1);
         imAddcomRateEseprec:=(aRecConguaglio.im_addcom_rate_eseprec * -1);
      END IF;

      -- Recupero dei record di RATEIZZA_CLASSIFIC_CORI dell'esercizio precedente per aggiornamento
      -- dell'importo rateizzato

      isStorico:='N';

      getRateizzaAddTerritorio(aRecConguaglio.esercizio - 1,
                               aRecConguaglio.cd_cds,
                               aRecConguaglio.cd_unita_organizzativa,
                               aRecConguaglio.pg_conguaglio,
                               aCdAnag,
                               isStorico,
                               isTemporaneo,
                               eseguiLock,
                               aRecRateizzaClassificCoriC0,
                               aRecRateizzaClassificCoriP0,
                               aRecRateizzaClassificCoriR0);

      -- Ciclo di aggiornamento dell'importo rateizzato

      FOR j IN 1 .. 3

      LOOP

         IF    j = 1 THEN
               aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriC0;
               aImportoBase:=imAddcomRateEseprec;
               aStringaTmp:='ADDIZIONALI COMUNALI. ';
         ELSIF j = 2 THEN
               aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriP0;
               aImportoBase:=imAddprovRateEseprec;
               aStringaTmp:='ADDIZIONALI PROVINCIALI. ';
         ELSIF j = 3 THEN
               aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriR0;
               aImportoBase:=imAddregRateEseprec;
               aStringaTmp:='ADDIZIONALI REGIONALI. ';
         END IF;

         IF aRecRateizzaClassificCoriBase.cd_cds_conguaglio IS NULL THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Impossibile procedere con l''aggiornamento dell''importo rateizzato per ' || aStringaTmp ||
                'Non esiste record definitivo in tabella RATEIZZA_CLASSIFIC_CORI');
         ELSE

            IF (aRecRateizzaClassificCoriBase.im_rateizzato + aImportoBase > aRecRateizzaClassificCoriBase.im_da_rateizzare OR
                aRecRateizzaClassificCoriBase.im_rateizzato + aImportoBase < 0) THEN
               IBMERR001.RAISE_ERR_GENERICO
                  ('Inconsistenza nella valorizzazione dell''importo rateizzato in aggiornamento di RATEIZZA_CLASSIFIC_CORI');
            ELSE

               UPDATE RATEIZZA_CLASSIFIC_CORI
               SET    im_rateizzato = im_rateizzato + aImportoBase,
                      duva = aRecConguaglio.duva,
                      utuv = aRecConguaglio.utuv,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  esercizio = aRecRateizzaClassificCoriBase.esercizio AND
                      cd_anag = aRecRateizzaClassificCoriBase.cd_anag AND
                      cd_classificazione_cori = aRecRateizzaClassificCoriBase.cd_classificazione_cori AND
                      fl_temporaneo = aRecRateizzaClassificCoriBase.fl_temporaneo;

            END IF;

         END IF;

      END LOOP;

   END;

END upgImRateizzatoAddTerritorio;

Procedure calcolaDetFam(inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
                        aTotRedditoComplessivo NUMBER,
                       anag ANAGRAFICO.cd_anag%TYPE,
                       aImTotDetrazioniCo IN OUT NUMBER,
                       aImTotDetrazioniFi IN OUT NUMBER,
                       aImTotDetrazioniAl IN OUT NUMBER,
                       aImTotDetrazioniFiS IN OUT NUMBER,
                       esisteFiSenzaConiuge CHAR)  Is
   numero_mesi_totali INTEGER;
   aImDetrazioniCo NUMBER(15,2);
   aImDetrazioniFi NUMBER(15,2);
   aImDetrazioniAl NUMBER(15,2);
   aImDetrazioniFiS NUMBER(15,2);
   aDifferenzaMesi INTEGER;
   numero_mesi_anno_prec_validi INTEGER;
   numero_mesi_anno_succ_validi INTEGER;
Begin
   numero_mesi_totali := 0;
   aImDetrazioniCo:=0;
   aImDetrazioniFi:=0;
   aImDetrazioniAl:=0;
   aImDetrazioniFiS:=0;

         --se sul conguaglio ho scelto di applicare le detrazioni familiari per l'intero anno
         --non leggo da tabella_date_fam_ok_unite
         If aRecConguaglio.fl_detrazioni_fam_intero_anno = 'Y' Then

             CNRCTB550.calcolaDetrazioniFam(aTotRedditoComplessivo,   --aImportoRifDetrazioniFam,
                                            anag,
                                            IBMUTL001.getFirstDayOfMonth(TO_DATE('0101'||(inEsercizioConguaglio), 'DDMMYYYY')),
                                            IBMUTL001.getLastDayOfMonth(TO_DATE('3112'||(inEsercizioConguaglio), 'DDMMYYYY')),
                                            aRecConguaglio.dt_registrazione,
                                            aImDetrazioniCo,
                                            aImDetrazioniFi,
                                            aImDetrazioniAl,
                                            aImDetrazioniFiS,
                                            esisteFiSenzaConiuge);

             aImTotDetrazioniCo:=aImTotDetrazioniCo + aImDetrazioniCo;
             aImTotDetrazioniFi:=aImTotDetrazioniFi + aImDetrazioniFi;
             aImTotDetrazioniAl:=aImTotDetrazioniAl + aImDetrazioniAl;
             aImTotDetrazioniFiS:=aImTotDetrazioniFiS + aImDetrazioniFiS;
             numero_mesi_totali := 12;
         Else
             FOR i IN tabella_date_fam_ok_unite.FIRST .. tabella_date_fam_ok_unite.LAST
             Loop
--pipe.send_message('1tabella_date_fam_ok_unite(i).tDataDa = '||tabella_date_fam_ok_unite(i).tDataDa);
--pipe.send_message('1tabella_date_fam_ok_unite(i).tDataA = '||tabella_date_fam_ok_unite(i).tDataA);
          --devo escludere gli intervalli dell'anno precedente
                If To_Char(tabella_date_fam_ok_unite(i).tDataDa,'yyyy') = inEsercizioConguaglio Then
                   aImDetrazioniCo:=0;
                   aImDetrazioniFi:=0;
                   aImDetrazioniAl:=0;
                   aImDetrazioniFiS:=0;
                   --devo fare in modo che in totale non applico le detrazioni per più di 12 mesi
                   aDifferenzaMesi:=Ceil(MONTHS_BETWEEN(tabella_date_fam_ok_unite(i).tDataA, tabella_date_fam_ok_unite(i).tDataDa));
                   numero_mesi_totali := numero_mesi_totali + aDifferenzaMesi;
                   CNRCTB550.calcolaDetrazioniFam(aTotRedditoComplessivo,   --aImportoRifDetrazioniFam,
                                               anag,
                                               tabella_date_fam_ok_unite(i).tDataDa,
                                               tabella_date_fam_ok_unite(i).tDataA,
                                               aRecConguaglio.dt_registrazione,
                                               aImDetrazioniCo,
                                               aImDetrazioniFi,
                                               aImDetrazioniAl,
                                               aImDetrazioniFiS,
                                               esisteFiSenzaConiuge);

                  aImTotDetrazioniCo:=aImTotDetrazioniCo + aImDetrazioniCo;
                  aImTotDetrazioniFi:=aImTotDetrazioniFi + aImDetrazioniFi;
                  aImTotDetrazioniAl:=aImTotDetrazioniAl + aImDetrazioniAl;
                  aImTotDetrazioniFiS:=aImTotDetrazioniFiS + aImDetrazioniFiS;
                End If;
             END LOOP;

         End If;

         --per tutti gli intervalli dell'anno precedente, calcolo le detrazioni familiari alla situazione
         --di gennaio dell'anno corrente
         If numero_mesi_totali < 12 And numero_mesi_anno_precedente > 0 Then
             aImDetrazioniCo:=0;
             aImDetrazioniFi:=0;
             aImDetrazioniAl:=0;
             aImDetrazioniFiS:=0;
             CNRCTB550.calcolaDetrazioniFam(aTotRedditoComplessivo,   --aImportoRifDetrazioniFam,
                                           anag,
                                           TO_DATE('0101' || inEsercizioConguaglio,'DDMMYYYY'),
                                           TO_DATE('3101' || inEsercizioConguaglio,'DDMMYYYY'),
                                           aRecConguaglio.dt_registrazione,
                                           aImDetrazioniCo,
                                           aImDetrazioniFi,
                                           aImDetrazioniAl,
                                           aImDetrazioniFiS,
                                           esisteFiSenzaConiuge);
             If (numero_mesi_totali + numero_mesi_anno_precedente) <= 12 Then
                numero_mesi_anno_prec_validi:=numero_mesi_anno_precedente;
             Else
                numero_mesi_anno_prec_validi:=12-numero_mesi_totali;
             End If;
             --l'importo di maggiorazione del coniuge, che non deve essere rapportato al periodo,
             --se è stato già aggiunto prima, ora devo toglierlo
             If numero_mesi_totali > 0 --vuol dire che ho già aggiunto la maggiorazione e quindi ora la tolgo
                And Nvl(aImDetrazioniCo,0) > 0 Then
                Declare
                  maggCo   NUMBER;
                Begin
                  Select Nvl(im_maggiorazione,0)
                  Into maggCo
                  From DETRAZIONI_FAMILIARI
                  WHERE  ti_persona = 'C' AND
                         numero = 1 AND
                         dt_inizio_validita <= aRecConguaglio.dt_registrazione AND
                         dt_fine_validita >= aRecConguaglio.dt_registrazione AND
                         im_inferiore <= aTotRedditoComplessivo AND
                         im_superiore >= aTotRedditoComplessivo;
      If  aImDetrazioniCo - maggCo > 0 Then
                     aImDetrazioniCo:= aImDetrazioniCo - maggCo;
                  Else
                     aImDetrazioniCo := 0;
                  End If;
                Exception
                   When no_data_found THEN
                        IBMERR001.RAISE_ERR_GENERICO
                        ('Record scaglione per detrazioni familiari del coniuge non definito');
                   When Too_Many_Rows Then
                        IBMERR001.RAISE_ERR_GENERICO
                        ('Esistono Record scaglione per detrazioni familiari del coniuge doppi');
                End;
             End If;
             aImTotDetrazioniCo:=aImTotDetrazioniCo + aImDetrazioniCo*numero_mesi_anno_prec_validi;
             aImTotDetrazioniFi:=aImTotDetrazioniFi + aImDetrazioniFi*numero_mesi_anno_prec_validi;
             aImTotDetrazioniAl:=aImTotDetrazioniAl + aImDetrazioniAl*numero_mesi_anno_prec_validi;
             aImTotDetrazioniFiS:=aImTotDetrazioniFiS + aImDetrazioniFiS*numero_mesi_anno_prec_validi;
         End If;
         --per tutti gli intervalli dell'anno successivo, calcolo le detrazioni familiari alla situazione
         --di dicembre dell'anno corrente
         If (numero_mesi_totali + numero_mesi_anno_precedente) < 12 And numero_mesi_anno_successivo > 0 Then
             aImDetrazioniCo:=0;
             aImDetrazioniFi:=0;
             aImDetrazioniAl:=0;
             aImDetrazioniFiS:=0;
             CNRCTB550.calcolaDetrazioniFam(aTotRedditoComplessivo,   --aImportoRifDetrazioniFam,
                                           anag,
                                           TO_DATE('0112' || inEsercizioConguaglio,'DDMMYYYY'),
                                           TO_DATE('3112' || inEsercizioConguaglio,'DDMMYYYY'),
                                           aRecConguaglio.dt_registrazione,
                                           aImDetrazioniCo,
                                           aImDetrazioniFi,
                                           aImDetrazioniAl,
                                           aImDetrazioniFiS,
                                           esisteFiSenzaConiuge);

             If (numero_mesi_totali + numero_mesi_anno_precedente + numero_mesi_anno_successivo) <= 12 Then
                numero_mesi_anno_succ_validi:=numero_mesi_anno_successivo;
             Else
                numero_mesi_anno_succ_validi:=12-(numero_mesi_totali + numero_mesi_anno_precedente);
             End If;
             --l'importo di maggiorazione del coniuge, che non deve essere rapportato al periodo,
             --se è stato già aggiunto prima, ora devo toglierlo
             If (numero_mesi_totali > 0 Or numero_mesi_anno_precedente > 0) --vuol dire che ho già aggiunto la maggiorazione e quindi ora la tolgo
                And Nvl(aImDetrazioniCo,0) > 0 Then
                Declare
                  maggCo   NUMBER;
                Begin
                  Select Nvl(im_maggiorazione,0)
                  Into maggCo
                  From DETRAZIONI_FAMILIARI
                  WHERE  ti_persona = 'C' AND
                         numero = 1 AND
                         dt_inizio_validita <= aRecConguaglio.dt_registrazione AND
                         dt_fine_validita >= aRecConguaglio.dt_registrazione AND
                         im_inferiore <= aTotRedditoComplessivo AND
                         im_superiore >= aTotRedditoComplessivo;

                  If  aImDetrazioniCo - maggCo > 0 Then
                     aImDetrazioniCo:= aImDetrazioniCo - maggCo;
                  Else
                     aImDetrazioniCo := 0;
                  End If;
                Exception
                   When no_data_found THEN
                        IBMERR001.RAISE_ERR_GENERICO
                        ('Record scaglione per detrazioni familiari del coniuge non definito');
                   When Too_Many_Rows Then
                        IBMERR001.RAISE_ERR_GENERICO
                        ('Esistono Record scaglione per detrazioni familiari del coniuge doppi');
                End;
             End If;

             aImTotDetrazioniCo:=aImTotDetrazioniCo + aImDetrazioniCo*numero_mesi_anno_succ_validi;
             aImTotDetrazioniFi:=aImTotDetrazioniFi + aImDetrazioniFi*numero_mesi_anno_succ_validi;
             aImTotDetrazioniAl:=aImTotDetrazioniAl + aImDetrazioniAl*numero_mesi_anno_succ_validi;
             aImTotDetrazioniFiS:=aImTotDetrazioniFiS + aImDetrazioniFiS*numero_mesi_anno_succ_validi;
         End If;
End calcolaDetFam;

END;
