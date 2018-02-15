--------------------------------------------------------
--  DDL for Package Body CNRCTB505
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB505" AS

--==================================================================================================
-- Lettura di una missione esistente, verifica ammissibilità delle spese registrate e calcolo delle diarie
--==================================================================================================
PROCEDURE elaboraMissioneDiaria
   (
    inCdsMissione MISSIONE.cd_cds%TYPE,
    inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
    inEsercizioMissione MISSIONE.esercizio%TYPE,
    inPgMissione MISSIONE.pg_missione%TYPE
   ) IS
   eseguiLock CHAR(1);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   dataOdierna:=sysdate;
   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura dati di base legati alla missione in calcolo; testata missione, anagrafico, base
   -- inquadramento e quote esenti

   getDatiBaseMissione(inCdsMissione,
                       inUoMissione,
                       inEsercizioMissione,
                       inPgMissione,
                       eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Costruzione matrice diarie per tappa e verifica della consistenza delle spese. Queste devono
   -- trovare una entrata nella tabella MISSIONE_ABBATTIMENTI di configurazione all'ammissibilità
   -- delle stesse

   creaMatriceTappe;
   verificaConfigSpeseTappe;

   -------------------------------------------------------------------------------------------------
   -- Calcolo della diaria. Se la tappa è in comune altro non è possibile che la quota esente sia
   -- inferiore all'importo della diaria

   calcolaDiaria;

   -------------------------------------------------------------------------------------------------
   -- Cancellazione delle righe di diaria preesistenti nella missione ed inserimento di quelle calcolate

   inserisciDiarie;

END elaboraMissioneDiaria;
--==================================================================================================
-- Lettura di una missione esistente, verifica ammissibilità delle spese registrate e calcolo dei rimborsi
--==================================================================================================
PROCEDURE elaboraMissioneRimborso
   (
    inCdsMissione MISSIONE.cd_cds%TYPE,
    inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
    inEsercizioMissione MISSIONE.esercizio%TYPE,
    inPgMissione MISSIONE.pg_missione%TYPE,
    inCdTrattamento MISSIONE.cd_trattamento%TYPE default null
   ) IS
   eseguiLock CHAR(1);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   dataOdierna:=sysdate;
   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura dati di base legati alla missione in calcolo; testata missione, anagrafico, base
   -- inquadramento e quote esenti

   getDatiBaseMissione(inCdsMissione,
                       inUoMissione,
                       inEsercizioMissione,
                       inPgMissione,
                       eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Costruzione matrice diarie per tappa e verifica della consistenza delle spese. Queste devono
   -- trovare una entrata nella tabella MISSIONE_ABBATTIMENTI di configurazione all'ammissibilità
   -- delle stesse

   creaMatriceTappe;
   -- ???? da gestire     -- verificaConfigSpeseTappe;

   -------------------------------------------------------------------------------------------------
   -- Calcolo della diaria. Se la tappa è in comune altro non è possibile che la quota esente sia
   -- inferiore all'importo della diaria

   calcolaRimborso(inCdTrattamento);

   -------------------------------------------------------------------------------------------------
   -- Cancellazione delle righe di diaria preesistenti nella missione ed inserimento di quelle calcolate

   inserisciRimborsi;

END elaboraMissioneRimborso;

--==================================================================================================
-- Lettura dati di base legati alla missione in calcolo; testata missione, anagrafico, base
-- inquadramento e quote esenti
--==================================================================================================
PROCEDURE getDatiBaseMissione
   (
    inCdsMissione MISSIONE.cd_cds%TYPE,
    inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
    inEsercizioMissione MISSIONE.esercizio%TYPE,
    inPgMissione MISSIONE.pg_missione%TYPE,
    eseguiLock CHAR
   ) IS

BEGIN

   -- Lettura della testata della missione

   aRecMissione:=CNRCTB500.getMissione(inCdsMissione,
                                       inUoMissione,
                                       inEsercizioMissione,
                                       inPgMissione,
                                       eseguiLock);

   -- Recupero dei dati dell'anagrafico associato al terzo della missione

   aRecAnagrafico:=CNRCTB080.getAnag (aRecMissione.cd_terzo);

   -- Lettura dati base dell'inquadramento

   aRecRifInquadramento:=CNRCTB080.getRifInquadramento(aRecMissione.cd_tipo_rapporto,
                                                       aRecAnagrafico.cd_anag,
                                                       trunc(aRecMissione.dt_inizio_missione));

   -- Lettura della quota esente italia e estero

   IF (aRecMissione.fl_comune_proprio = 'Y' OR
       aRecMissione.fl_comune_altro = 'Y') THEN
      aRecQuotaEsenteItalia:=CNRCTB500.getMissioneQuotaEsente ('I',
                                                               trunc(aRecMissione.dt_inizio_missione));
   END IF;

   IF aRecMissione.fl_comune_estero = 'Y' THEN
      aRecQuotaEsenteEstero:=CNRCTB500.getMissioneQuotaEsente ('E',
                                                               trunc(aRecMissione.dt_inizio_missione));
   END IF;

END getDatiBaseMissione;


--==================================================================================================
-- Costruzione matrice diaria per tappa. Si eseguono i seguenti passi:
-- 1) Si estraggono i dati relativi alle tappe.
-- 2) Per ogni tappa si determina la tipologie delle spese registrate.
--
--==================================================================================================
PROCEDURE creaMatriceTappe
   IS
   i BINARY_INTEGER;

   aRecMissioneTappa MISSIONE_TAPPA%ROWTYPE;
   aRecMissioneDiaria MISSIONE_DIARIA%ROWTYPE;
   aRecMissioneQuotaRimborso MISSIONE_QUOTA_RIMBORSO%ROWTYPE;
   aCdAreaEstera  RIF_AREE_PAESI_ESTERI.CD_AREA_ESTERA%TYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Azzeramento variabili

   matriceTappe_tab.DELETE;
   i:=0;

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione della matrice tappe; stessa cardinalità di MISSIONE_TAPPA per una data missione

   BEGIN

      OPEN gen_cur FOR
           SELECT *
           FROM   MISSIONE_TAPPA
           WHERE  cd_cds = aRecMissione.cd_cds AND
                  esercizio = aRecMissione.esercizio AND
                  cd_unita_organizzativa = aRecMissione.cd_unita_organizzativa AND
                  pg_missione = aRecMissione.pg_missione
           ORDER BY dt_inizio_tappa;

      LOOP

         FETCH gen_cur INTO aRecMissioneTappa;

         EXIT WHEN gen_cur%NOTFOUND;

         i:=i + 1;

         matriceTappe_tab(i).tDtInizio:=aRecMissioneTappa.dt_inizio_tappa;
         matriceTappe_tab(i).tDtFine:=aRecMissioneTappa.dt_fine_tappa;

         -- Determina il numero di ore della tappa

         matriceTappe_tab(i).tNumeroOre:=CNRCTB500.calcolaOreTappa(aRecMissioneTappa.dt_inizio_tappa,
                                                                   aRecMissioneTappa.dt_fine_tappa);

         -- Determina il numero di minuti della tappa

         matriceTappe_tab(i).tNumeroMinuti:=CNRCTB500.calcolaMinutiTappa(aRecMissioneTappa.dt_inizio_tappa,
                                                                         aRecMissioneTappa.dt_fine_tappa);

         -- Determina area geografica e la conseguente quota esente lorda

         IF aRecMissioneTappa.fl_comune_estero ='Y' THEN
            matriceTappe_tab(i).tAreaGeografica:='E';
            matriceTappe_tab(i).tQuotaEsente:=aRecQuotaEsenteEstero.quota_esente;
         ELSE
            matriceTappe_tab(i).tAreaGeografica:='I';
            matriceTappe_tab(i).tQuotaEsente:=aRecQuotaEsenteItalia.quota_esente;
         END IF;
         matriceTappe_tab(i).tPgNazione:=aRecMissioneTappa.pg_nazione;

         Begin
            select cd_area_estera
            into aCdAreaEstera
            from NAZIONE
            where pg_nazione = aRecMissioneTappa.pg_nazione;

            If aCdAreaEstera is not null then
                matriceTappe_tab(i).tCdAreaEstera := aCdAreaEstera;
            Elsif matriceTappe_tab(i).tAreaGeografica != 'I' then
                IBMERR001.RAISE_ERR_GENERICO('Area Estera non definita per la nazione: ' || aRecMissioneTappa.pg_nazione);
            End if;
				 End;
         matriceTappe_tab(i).tIsComuneProprio:=aRecMissioneTappa.fl_comune_proprio;

         -- Inquadramento

         matriceTappe_tab(i).tPgRifInquadramento:=aRecMissione.pg_rif_inquadramento;

         -- Switch ammissibilità spese

         matriceTappe_tab(i).tFlPasto:='N';
         matriceTappe_tab(i).tFlAlloggio:='N';
         matriceTappe_tab(i).tFlTrasporto:='N';
         matriceTappe_tab(i).tFlNavigazione:=aRecMissioneTappa.fl_navigazione;
         matriceTappe_tab(i).tFlVittoGratuito:=aRecMissioneTappa.fl_vitto_gratuito;
         matriceTappe_tab(i).tFlAlloggioGratuito:=aRecMissioneTappa.fl_alloggio_gratuito;
         matriceTappe_tab(i).tFlVittoAlloggioGratuito:=aRecMissioneTappa.fl_vitto_alloggio_gratuito;
         matriceTappe_tab(i).tFlNoDiaria:=aRecMissioneTappa.fl_no_diaria;
				 matriceTappe_tab(i).tFlRimborso:=aRecMissioneTappa.fl_rimborso;

         -- Valori di default

         matriceTappe_tab(i).tOkAbbattimento:='N';
         matriceTappe_tab(i).tPercentualeAbbattimento:=0;
         matriceTappe_tab(i).tDiariaNetto:=0;
         matriceTappe_tab(i).tRimborso:= 0;

         -- Lettura delle spese registrate a fronte di ogni tappa. La funzione aggiorna gli elementi
         -- della matrice passati in input in base alle risultanze ricavate dalle spese associate ad
         -- ogni tappa.

         leggiSpeseTappa(aRecMissioneTappa,
                         matriceTappe_tab(i).tFlPasto,
                         matriceTappe_tab(i).tFlAlloggio,
                         matriceTappe_tab(i).tFlTrasporto);

         -- Lettura della diaria massima da applicare alla tappa. Si tratta della sola diaria lorda senza
         -- alcuna normalizzazione per le ore di riferimento o in base alle percentuali di abbattimento.

         IF aRecMissioneTappa.fl_no_diaria = 'Y' THEN
            matriceTappe_tab(i).tDiariaLorda:=0;
         ELSE
            aRecMissioneDiaria:=CNRCTB500.getMissioneDiaria (aRecMissioneTappa,
                                                             aRecRifInquadramento.cd_gruppo_inquadramento,
                                                             aRecMissioneTappa.dt_inizio_tappa);
                                                             --aRecMissione.dt_inizio_missione);
            matriceTappe_tab(i).tDiariaLorda:=aRecMissioneDiaria.im_diaria;
         END IF;

				 IF aRecMissioneTappa.fl_rimborso = 'Y' THEN
				      aRecMissioneQuotaRimborso := CNRCTB500.getMissioneQuotaRimborso (aRecMissioneTappa,
                                                                               aRecRifInquadramento.cd_gruppo_inquadramento,
                                                                               matriceTappe_tab(i).tCdAreaEstera,
                                                                               aRecMissioneTappa.dt_inizio_tappa);
				      matriceTappe_tab(i).tRimborso :=aRecMissioneQuotaRimborso.im_rimborso;
				 END IF;

      END LOOP;

      CLOSE gen_cur;

   END;

   RETURN;

END creaMatriceTappe;

--==================================================================================================
-- Lettura della tipologie di spese registrate per ogni tappa. Si aggiornano i riferimenti
-- per la ricerca di una entrata in MISSIONE_ABBATTIMENTI
--==================================================================================================
PROCEDURE leggiSpeseTappa
   (
    aRecMissioneTappa MISSIONE_TAPPA%ROWTYPE,
    flPasto IN OUT CHAR,
    flAlloggio IN OUT CHAR,
    flTrasporto IN OUT CHAR
   ) IS
   aAreaGeografica CHAR(1);
   gen_cur_d GenericCurTyp;
   aRecMissioneDettaglio MISSIONE_DETTAGLIO%ROWTYPE;

BEGIN

   -- Ciclo di lettura dei dettagli di spesa della missione

   BEGIN

      OPEN gen_cur_d FOR

           SELECT *
           FROM   MISSIONE_DETTAGLIO
           WHERE  cd_cds = aRecMissioneTappa.cd_cds AND
                  cd_unita_organizzativa = aRecMissioneTappa.cd_unita_organizzativa AND
                  esercizio = aRecMissioneTappa.esercizio AND
                  pg_missione = aRecMissioneTappa.pg_missione AND
                  dt_inizio_tappa = aRecMissioneTappa.dt_inizio_tappa AND
                  ti_spesa_diaria = 'S';

      LOOP

         FETCH gen_cur_d INTO aRecMissioneDettaglio;

         EXIT WHEN gen_cur_d%NOTFOUND;

         -- Determino le tipologie di spesa presenti nella tappa ed aggiorno gli indicatori
         -- di tipologia spesa nella matrice delle tappe.

         IF    aRecMissioneDettaglio.ti_cd_ti_spesa = 'A' THEN
               flAlloggio:='Y';
         ELSIF aRecMissioneDettaglio.ti_cd_ti_spesa = 'P' THEN
               flPasto:='Y';
         ELSIF (aRecMissioneDettaglio.ti_cd_ti_spesa = 'R' OR
                aRecMissioneDettaglio.ti_cd_ti_spesa = 'T') THEN
               flTrasporto:='Y';
         END IF;

      END LOOP;

      CLOSE gen_cur_d;

   END;

   RETURN;

END leggiSpeseTappa;

-- =================================================================================================
-- Verifica esistenza di tappe le cui spese non hanno trovato una entrata nella tabella
-- MISSIONE_ABBATTIMENTI di configurazione all'ammissibilità delle stesse
-- =================================================================================================
PROCEDURE verificaConfigSpeseTappe
   IS
   i BINARY_INTEGER;
   isDurataMaggioreOttoOre CHAR(1);
   aNumeroOre NUMBER;
   aStringa VARCHAR2(1000);
   aStringa2 VARCHAR2(1000);

   aRecMissioneAbbattimenti MISSIONE_ABBATTIMENTI%ROWTYPE;

   gen_cur_a GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Calcolo la durata complessiva della missione. Se questa è maggiore di 8 ore allora deve sempre
   -- essere usato l'accesso maggiore di 8 ore nel recupero degli abbattimenti

   isDurataMaggioreOttoOre:='N';
   IF TRUNC((aRecMissione.dt_fine_missione - aRecMissione.dt_inizio_missione),5) > isOttoOre THEN
      isDurataMaggioreOttoOre:='Y';
   END IF;

   -- Ciclo di lettura della matrice tappe della missione per aggiornamento degli indicatori
   -- di ritrovamento di un riferimento in MISSIONE_ABBATTIMENTI dati i dettagli di spesa
   -- associati ad una tappa

   BEGIN

      FOR i IN matriceTappe_tab.FIRST .. matriceTappe_tab.LAST

      LOOP

         -- Normalizzazione del numero di ore portato dalla tappa rispetto alla durata della stessa
         -- missione. Se loa missione dura più di otto ore allora si considera che la stessa tappa è
         -- maggiore di otto ore. Si prevede tuttavia il caso in cui la durata della tappa sia maggiore
         -- di quella della missione (stato estero sempre in 24 ore)

         IF isDurataMaggioreOttoOre = 'Y' THEN
            IF matriceTappe_tab(i).tNumeroOre > 8 THEN
               aNumeroOre:=matriceTappe_tab(i).tNumeroOre;
            ELSE
               aNumeroOre:=9;
            END IF;
         ELSE
            aNumeroOre:=matriceTappe_tab(i).tNumeroOre;
         END IF;

         -- Lettura abbattimenti

         OPEN gen_cur_a FOR

              SELECT *
              FROM   MISSIONE_ABBATTIMENTI
              WHERE  dt_inizio_validita <= matriceTappe_tab(i).tDtInizio AND
                     dt_fine_validita >= matriceTappe_tab(i).tDtFine AND
                     fl_pasto = matriceTappe_tab(i).tFlPasto AND
                     fl_alloggio = matriceTappe_tab(i).tFlAlloggio AND
                     fl_trasporto = matriceTappe_tab(i).tFlTrasporto AND
                     fl_navigazione = matriceTappe_tab(i).tFlNavigazione AND
                     fl_vitto_gratuito = matriceTappe_tab(i).tFlVittoGratuito AND
                     fl_alloggio_gratuito = matriceTappe_tab(i).tFlAlloggioGratuito AND
                     fl_vitto_alloggio_gratuito = matriceTappe_tab(i).tFlVittoAlloggioGratuito AND
                     (
                      ti_area_geografica = matriceTappe_tab(i).tAreaGeografica OR
                      ti_area_geografica = '*'
                     ) AND
                     (
                      pg_nazione = matriceTappe_tab(i).tPgNazione OR
                      pg_nazione = 0
                     ) AND
                     (
                      pg_rif_inquadramento  = matriceTappe_tab(i).tPgRifInquadramento OR
                      pg_rif_inquadramento = 0
                     )
              ORDER BY pg_rif_inquadramento desc,
                       pg_nazione desc,
                       ti_area_geografica desc;

         LOOP

            FETCH gen_cur_a INTO aRecMissioneAbbattimenti;

            EXIT WHEN gen_cur_a%NOTFOUND;

            -- Valorizzazione dell'indicatore nella matrice tappe di recupero della percentuale di
            -- abbattimento della diaria

            IF    aNumeroOre <= 4 THEN
                  IF aRecMissioneAbbattimenti.durata_ore = '< 4' THEN
                     matriceTappe_tab(i).tOkAbbattimento:='Y';
                     matriceTappe_tab(i).tPercentualeAbbattimento:=aRecMissioneAbbattimenti.percentuale_abbattimento;
                     EXIT;
                  END IF;
            ELSIF (aNumeroOre > 4 AND
                   aNumeroOre <= 8) THEN
                  IF aRecMissioneAbbattimenti.durata_ore = '> 4' THEN
                     matriceTappe_tab(i).tOkAbbattimento:='Y';
                     matriceTappe_tab(i).tPercentualeAbbattimento:=aRecMissioneAbbattimenti.percentuale_abbattimento;
                     EXIT;
                 END IF;
            ELSIF aNumeroOre > 8 THEN
                  IF aRecMissioneAbbattimenti.durata_ore = '> 8' THEN
                     matriceTappe_tab(i).tOkAbbattimento:='Y';
                     matriceTappe_tab(i).tPercentualeAbbattimento:=aRecMissioneAbbattimenti.percentuale_abbattimento;
                     EXIT;
                  END IF;
            END IF;

         END LOOP;

         CLOSE gen_cur_a;

      END LOOP;

   END;

   -- Ciclo di lettura della matrice tappe della missione per verifica esistenza di configurazioni di spesa
   -- registrate e non previste dal regolamento

   BEGIN

      aStringa:=NULL;

      FOR i IN matriceTappe_tab.FIRST .. matriceTappe_tab.LAST


      LOOP

         IF matriceTappe_tab(i).tOkAbbattimento='N' THEN
            IF NVL( LENGTH(aStringa), 0) < 900 THEN
               aStringa:= aStringa || 'TAPPA data inizio ' ||
                          TO_CHAR(matriceTappe_tab(i).tDtInizio,'DD/MM/YYYY') || CHR(10);
            END IF;
         END IF;

      END LOOP;

      IF aStringa IS NOT NULL THEN
         aStringa:='Non trovata entrata in MISSIONE_ABBATTIMENTI per le spese definite in: ' || CHR(10) ||
                   aStringa;
         IBMERR001.RAISE_ERR_GENERICO (aStringa);
      END IF;

   END;

   RETURN;

END verificaConfigSpeseTappe;

--==================================================================================================
-- Esecuzione del calcolo della diaria. E' segnalato errore se si tratta di missione italia in
-- comune altro con quota esente inderiore all'importo della diaria
--==================================================================================================
PROCEDURE calcolaDiaria
   IS
   i BINARY_INTEGER;

BEGIN

   BEGIN

      FOR i IN matriceTappe_tab.FIRST .. matriceTappe_tab.LAST

      LOOP

         -- Lettura della diaria massima da applicare alla tappa. Se, per la tappa, è vero il
         -- flag di no diaria questa è posta = 0.
         -- Negli altri casi si operano i seguenti calcoli:
         -- 1) Si determina il valore della diaria lorda per le ore effettive di durata della missione in
         --    data tappa.
         -- 2) Si determina l'importo dela diaria netta considerando la percentuale di abbattimento
         --    recuperata per ogni singola tappa.
         -- 3) Si abbatte allo stesso modo della diaria la quota esente.
         -- 4) Se la tappa della missione è in Italia e comune proprio viene azzerata la quota esente.
         -- 4) Se la quota esente è maggiore della diaria netta si rende uguale a quest'ultima. Si solleva
         --    errore se la quota esente è minore della diaria netta in caso di missione in italia con
         --    tappa in altro comune.

         IF matriceTappe_tab(i).tFlNoDiaria = 'Y' THEN
            matriceTappe_tab(i).tDiariaLorda:=0;
            matriceTappe_tab(i).tDiariaNetto:=0;
            matriceTappe_tab(i).tQuotaEsente:=0;
         ELSE
            matriceTappe_tab(i).tDiariaLorda:=ROUND((matriceTappe_tab(i).tDiariaLorda / 24 *
                                                     matriceTappe_tab(i).tNumeroOre),2);
            matriceTappe_tab(i).tDiariaNetto:=ROUND((matriceTappe_tab(i).tDiariaLorda -
                                                       (matriceTappe_tab(i).tDiariaLorda *
                                                        matriceTappe_tab(i).tPercentualeAbbattimento / 100)),2);

            IF (matriceTappe_tab(i).tAreaGeografica = 'I' AND
                matriceTappe_tab(i).tIsComuneProprio = 'Y') THEN
               matriceTappe_tab(i).tQuotaEsente:=0;
            ELSE
               matriceTappe_tab(i).tQuotaEsente:=
                                   ROUND((matriceTappe_tab(i).tQuotaEsente -
                                            (matriceTappe_tab(i).tQuotaEsente *
                                             matriceTappe_tab(i).tPercentualeAbbattimento / 100)),2);
            END IF;

            IF (matriceTappe_tab(i).tAreaGeografica = 'I' AND
                matriceTappe_tab(i).tIsComuneProprio = 'N' AND
                matriceTappe_tab(i).tQuotaEsente < matriceTappe_tab(i).tDiariaNetto) THEN
               IBMERR001.RAISE_ERR_GENERICO
                   ('LA TAPPA in Italia comune altro - data inizio ' ||
                    TO_CHAR(matriceTappe_tab(i).tDtInizio,'DD/MM/YYYY') ||
                    ' presenta una quota esente inferiore all''importo della diaria');
            END IF;

            IF (matriceTappe_tab(i).tQuotaEsente > matriceTappe_tab(i).tDiariaNetto) THEN
               matriceTappe_tab(i).tQuotaEsente:=matriceTappe_tab(i).tDiariaNetto;
            END IF;


         END IF;

      END LOOP;

   END;

   RETURN;

END calcolaDiaria;

--==================================================================================================
-- Esecuzione del calcolo della diaria. E' segnalato errore se si tratta di missione italia in
-- comune altro con quota esente inderiore all'importo della diaria
--==================================================================================================
PROCEDURE calcolaRimborso (inCdTrattamento IN MISSIONE.cd_trattamento%TYPE)
   IS
   i BINARY_INTEGER;
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;
   contaRitFiscale NUMBER:=0;
BEGIN

   BEGIN

      -- Recupero dati del tipo trattamento indicato nella missione
      if inCdTrattamento is not null then
          aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento(inCdTrattamento,
                                                            aRecMissione.dt_registrazione);

         SELECT count(1)
         into contaRitFiscale
         FROM V_TIPO_TRATTAMENTO_TIPO_CORI
         WHERE cd_trattamento = aRecTipoTrattamento.cd_trattamento
           AND dt_ini_val_trattamento = aRecTipoTrattamento.dt_ini_validita
           AND dt_fin_val_trattamento = aRecTipoTrattamento.dt_fin_validita
           AND dt_ini_val_tratt_cori <= aRecMissione.dt_registrazione
           AND dt_fin_val_tratt_cori >= aRecMissione.dt_registrazione
           AND dt_ini_val_tipo_cori <= aRecMissione.dt_registrazione
           AND dt_fin_val_tipo_cori >= aRecMissione.dt_registrazione
           AND cd_classificazione_cori = CNRCTB545.isCoriFiscale;
      else
          contaRitFiscale :=99999;
      end if;

--pipe.send_message('contaRitFiscale = '||contaRitFiscale);


      FOR i IN matriceTappe_tab.FIRST .. matriceTappe_tab.LAST

      LOOP

         IF matriceTappe_tab(i).tFlRimborso = 'Y' THEN
            If matriceTappe_tab(i).tNumeroMinuti >= 1440 then
                 -- il rimborso è già quello inserito
                 -- verifico solo che la quota esente non sia maggiore del rimborso
                 If contaRitFiscale > 0 then
                    If matriceTappe_tab(i).tQuotaEsente > matriceTappe_tab(i).tRimborso then
                         matriceTappe_tab(i).tQuotaEsente := matriceTappe_tab(i).tRimborso;
                    End If;
                 Else
                    matriceTappe_tab(i).tQuotaEsente := 0;
                 End if;
            Elsif matriceTappe_tab(i).tNumeroMinuti < 1440 and matriceTappe_tab(i).tNumeroMinuti >= 720  then
                 matriceTappe_tab(i).tRimborso := matriceTappe_tab(i).tRimborso/2;
                 If contaRitFiscale > 0 then
                    If matriceTappe_tab(i).tQuotaEsente > matriceTappe_tab(i).tRimborso then
                         matriceTappe_tab(i).tQuotaEsente := matriceTappe_tab(i).tRimborso;
                    End If;
                 Else
                    matriceTappe_tab(i).tQuotaEsente := 0;
                 End if;
            Else   -- matriceTappe_tab(i).tNumeroMinuti < 720
                 matriceTappe_tab(i).tRimborso := 0;
                 matriceTappe_tab(i).tQuotaEsente := 0;
            End if;
         END IF;
      END LOOP;

   END;

   RETURN;

END calcolaRimborso;
--==================================================================================================
-- Inserimento delle righe di diaria in MISSIONE_DETTAGLIO
--==================================================================================================
PROCEDURE inserisciDiarie
   IS
   i BINARY_INTEGER;

   aRecMissioneDettaglio MISSIONE_DETTAGLIO%ROWTYPE;
   aBaseIncrementoRiga MISSIONE_DETTAGLIO.pg_riga%TYPE;

BEGIN

   --  Inserimento delle righe di diaria calcolate. E' inserita una riga diaria per  ogni tappa definita

   aRecMissioneDettaglio:=NULL;

   SELECT NVL(MAX(pg_riga), 0) INTO aBaseIncrementoRiga
   FROM   MISSIONE_DETTAGLIO
   WHERE  cd_cds = aRecMissione.cd_cds AND
          cd_unita_organizzativa = aRecMissione.cd_unita_organizzativa AND
          esercizio = aRecMissione.esercizio AND
          pg_missione = aRecMissione.pg_missione;

   BEGIN

      FOR i IN 1 .. matriceTappe_tab.COUNT

      LOOP

         aRecMissioneDettaglio.cd_cds:=aRecMissione.cd_cds;
         aRecMissioneDettaglio.esercizio:=aRecMissione.esercizio;
         aRecMissioneDettaglio.cd_unita_organizzativa:=aRecMissione.cd_unita_organizzativa;
         aRecMissioneDettaglio.pg_missione:=aRecMissione.pg_missione;
         aRecMissioneDettaglio.dt_inizio_tappa:=matriceTappe_tab(i).tDtInizio;
         aRecMissioneDettaglio.pg_riga:=aBaseIncrementoRiga + i;
         aRecMissioneDettaglio.ti_spesa_diaria:='D';
         aRecMissioneDettaglio.im_spesa_divisa:=0;
         aRecMissioneDettaglio.cambio_spesa:=0;
         aRecMissioneDettaglio.im_spesa_euro:=0;
         aRecMissioneDettaglio.im_spesa_max_divisa:=0;
         aRecMissioneDettaglio.im_spesa_max:=0;
         aRecMissioneDettaglio.im_base_maggiorazione:=0;
         aRecMissioneDettaglio.percentuale_maggiorazione:=0;
         aRecMissioneDettaglio.im_maggiorazione:=0;
         aRecMissioneDettaglio.indennita_chilometrica:=0;
         aRecMissioneDettaglio.im_totale_spesa:=0;
         aRecMissioneDettaglio.fl_diaria_manuale:='N';
         aRecMissioneDettaglio.im_diaria_lorda:=matriceTappe_tab(i).tDiariaLorda;
         aRecMissioneDettaglio.im_diaria_netto:=matriceTappe_tab(i).tDiariaNetto;
         aRecMissioneDettaglio.im_quota_esente:=matriceTappe_tab(i).tQuotaEsente;
         aRecMissioneDettaglio.utcr:=aRecMissione.utcr;
         aRecMissioneDettaglio.dacr:=aRecMissione.dacr;
         aRecMissioneDettaglio.utuv:=aRecMissione.utuv;
         aRecMissioneDettaglio.duva:=aRecMissione.duva;
         aRecMissioneDettaglio.pg_ver_rec:=aRecMissione.pg_ver_rec;
         aRecMissioneDettaglio.im_maggiorazione_euro:=0;
				 aRecMissioneDettaglio.im_rimborso:=0;
         CNRCTB500.insMissioneDettaglio (aRecMissioneDettaglio);

      END LOOP;

   END;

   RETURN;

END inserisciDiarie;
--==================================================================================================
-- Inserimento delle righe di rimborso in MISSIONE_DETTAGLIO
--==================================================================================================
PROCEDURE inserisciRimborsi
   IS
   i BINARY_INTEGER;

   aRecMissioneDettaglio MISSIONE_DETTAGLIO%ROWTYPE;
   aBaseIncrementoRiga MISSIONE_DETTAGLIO.pg_riga%TYPE;

BEGIN

   --  Inserimento delle righe di rimborso calcolate.
   -- ?????? E' inserita una riga diaria per  ogni tappa definita

   aRecMissioneDettaglio:=NULL;

   SELECT NVL(MAX(pg_riga), 0) INTO aBaseIncrementoRiga
   FROM   MISSIONE_DETTAGLIO
   WHERE  cd_cds = aRecMissione.cd_cds AND
          cd_unita_organizzativa = aRecMissione.cd_unita_organizzativa AND
          esercizio = aRecMissione.esercizio AND
          pg_missione = aRecMissione.pg_missione;

   BEGIN

      FOR i IN 1 .. matriceTappe_tab.COUNT
      LOOP
--pipe.send_message('matriceTappe_tab(i).tAreaGeografica = '||matriceTappe_tab(i).tAreaGeografica);
         if matriceTappe_tab(i).tAreaGeografica = 'E' and matriceTappe_tab(i).tFlRimborso = 'Y' then
              aRecMissioneDettaglio.cd_cds:=aRecMissione.cd_cds;
              aRecMissioneDettaglio.esercizio:=aRecMissione.esercizio;
              aRecMissioneDettaglio.cd_unita_organizzativa:=aRecMissione.cd_unita_organizzativa;
              aRecMissioneDettaglio.pg_missione:=aRecMissione.pg_missione;
              aRecMissioneDettaglio.dt_inizio_tappa:=matriceTappe_tab(i).tDtInizio;
              aRecMissioneDettaglio.pg_riga:=aBaseIncrementoRiga + i;
              aRecMissioneDettaglio.ti_spesa_diaria:='R';
              aRecMissioneDettaglio.im_spesa_divisa:=0;
              aRecMissioneDettaglio.cambio_spesa:=0;
              aRecMissioneDettaglio.im_spesa_euro:=0;
              aRecMissioneDettaglio.im_spesa_max_divisa:=0;
              aRecMissioneDettaglio.im_spesa_max:=0;
              aRecMissioneDettaglio.im_base_maggiorazione:=0;
              aRecMissioneDettaglio.percentuale_maggiorazione:=0;
              aRecMissioneDettaglio.im_maggiorazione:=0;
              aRecMissioneDettaglio.indennita_chilometrica:=0;
              aRecMissioneDettaglio.im_totale_spesa:=0;
              aRecMissioneDettaglio.fl_diaria_manuale:='N';
              aRecMissioneDettaglio.im_diaria_lorda:=0;
              aRecMissioneDettaglio.im_diaria_netto:=0;
              aRecMissioneDettaglio.im_quota_esente:=matriceTappe_tab(i).tQuotaEsente;
              aRecMissioneDettaglio.im_rimborso:=matriceTappe_tab(i).tRimborso;
              aRecMissioneDettaglio.utcr:=aRecMissione.utcr;
              aRecMissioneDettaglio.dacr:=aRecMissione.dacr;
              aRecMissioneDettaglio.utuv:=aRecMissione.utuv;
              aRecMissioneDettaglio.duva:=aRecMissione.duva;
              aRecMissioneDettaglio.pg_ver_rec:=aRecMissione.pg_ver_rec;
              aRecMissioneDettaglio.im_maggiorazione_euro:=0;

              CNRCTB500.insMissioneDettaglio (aRecMissioneDettaglio);
         end if;
      END LOOP;

   END;

   RETURN;

END inserisciRimborsi;

--==================================================================================================
-- Valorizzazione di importo lordo e quota esente per generazione compenso
--==================================================================================================
PROCEDURE calcolaImportiCompenso
   (
    inCdsMissione MISSIONE.cd_cds%TYPE,
    inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
    inEsercizioMissione MISSIONE.esercizio%TYPE,
    inPgMissione MISSIONE.pg_missione%TYPE,
    inImportoLordo IN OUT NUMBER,
    inQuotaEsente IN OUT NUMBER,
    inAliquotaIrpef IN OUT NUMBER
   ) IS
   eseguiLock CHAR(1);
   aImportoSpesaMaggiorazione NUMBER(15,2);
   aImportoLordoGenerico NUMBER(15,2);
   aQuotaEsenteGenerico NUMBER(15,2);
   aImportoLordoComuneP NUMBER(15,2);
   aQuotaEsenteComuneP NUMBER(15,2);
   aImportoLordoComuneA NUMBER(15,2);
   aQuotaEsenteComuneA NUMBER(15,2);
   aImportoLordoComuneE NUMBER(15,2);
   aQuotaEsenteComuneE NUMBER(15,2);
   aImportoRimborso NUMBER(15,2);
   aQuotaEsenteRimborso NUMBER(15,2);

   aRecMissioneTappa MISSIONE_TAPPA%ROWTYPE;
   aRecMissioneDettaglio MISSIONE_DETTAGLIO%ROWTYPE;

   gen_cur_t GenericCurTyp;
   gen_cur_d GenericCurTyp;

BEGIN

--commit;
--IBMERR001.RAISE_ERR_GENERICO('PROVA FIX ERRORE');

   inImportoLordo:=0;
   inQuotaEsente:=0;
   inAliquotaIrpef:=0;
   aImportoSpesaMaggiorazione:=0;
   aImportoLordoGenerico:=0;
   aQuotaEsenteGenerico:=0;
   aImportoLordoComuneP:=0;
   aQuotaEsenteComuneP:=0;
   aImportoLordoComuneA:=0;
   aQuotaEsenteComuneA:=0;
   aImportoLordoComuneE:=0;
   aQuotaEsenteComuneE:=0;
   aImportoRimborso:=0;
   aQuotaEsenteRimborso:=0;

   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura della testata della missione

   BEGIN

      -- Lettura dati di base legati alla missione in calcolo; testata missione, anagrafico, base
      -- inquadramento e quote esenti

      getDatiBaseMissione(inCdsMissione,
                          inUoMissione,
                          inEsercizioMissione,
                          inPgMissione,
                          eseguiLock);

   END;

   -------------------------------------------------------------------------------------------------
   -- Ciclo di lettura delle tappe e quindi dei dettagli della missione

   BEGIN

      OPEN gen_cur_t FOR

           SELECT *
           FROM   MISSIONE_TAPPA
           WHERE  cd_cds = inCdsMissione AND
                  cd_unita_organizzativa = inUoMissione AND
                  esercizio = inEsercizioMissione AND
                  pg_missione = inPgMissione
           ORDER BY dt_inizio_tappa;

      LOOP

         FETCH gen_cur_t INTO aRecMissioneTappa;

         EXIT WHEN gen_cur_t%NOTFOUND;

         -- Lettura del dettaglio di spese e diaria della missione

         BEGIN

            aImportoSpesaMaggiorazione:=0;
            aImportoLordoGenerico:=0;
            aQuotaEsenteGenerico:=0;

            OPEN gen_cur_d FOR

                 SELECT *
                 FROM   MISSIONE_DETTAGLIO
                 WHERE  cd_cds = aRecMissioneTappa.cd_cds AND
                        cd_unita_organizzativa = aRecMissioneTappa.cd_unita_organizzativa AND
                        esercizio = aRecMissioneTappa.esercizio AND
                        pg_missione = aRecMissioneTappa.pg_missione AND
                        dt_inizio_tappa = aRecMissioneTappa.dt_inizio_tappa;

            LOOP

               FETCH gen_cur_d INTO aRecMissioneDettaglio;

               EXIT WHEN gen_cur_d%NOTFOUND;

               -- Lettura delle spese. In caso di spese anticipate recupero l'eventuale importo della
               -- maggiorazione negli altri casi opero con l'importo totale della spesa

               IF aRecMissioneDettaglio.ti_spesa_diaria = 'S' THEN

                  IF aRecMissioneDettaglio.fl_spesa_anticipata = 'Y' THEN
                     aImportoSpesaMaggiorazione:=aRecMissioneDettaglio.im_maggiorazione_euro;
                  ELSE
                     aImportoSpesaMaggiorazione:=aRecMissioneDettaglio.im_totale_spesa;
                  END IF;

                  IF aRecMissioneTappa.fl_comune_proprio = 'Y' THEN
                     IF aRecMissioneDettaglio.ti_cd_ti_spesa = 'T' THEN
                        aImportoLordoGenerico:=aImportoLordoGenerico + aImportoSpesaMaggiorazione;
                        aQuotaEsenteGenerico:=aQuotaEsenteGenerico + aImportoSpesaMaggiorazione;
                     ELSE
                        aImportoLordoGenerico:=aImportoLordoGenerico + aImportoSpesaMaggiorazione;
                     END IF;
                  ELSE
                     aImportoLordoGenerico:=aImportoLordoGenerico + aImportoSpesaMaggiorazione;
                     aQuotaEsenteGenerico:=aQuotaEsenteGenerico + aImportoSpesaMaggiorazione;
                  END IF;

               ELSIF aRecMissioneDettaglio.ti_spesa_diaria = 'D' THEN

                  aImportoLordoGenerico:=aImportoLordoGenerico + aRecMissioneDettaglio.im_diaria_netto;
                  aQuotaEsenteGenerico:=aQuotaEsenteGenerico + aRecMissioneDettaglio.im_quota_esente;

               ELSE    --aRecMissioneDettaglio.ti_spesa_diaria = 'R' (quota di rimborso)

                  aImportoRimborso:=aImportoRimborso + aRecMissioneDettaglio.im_rimborso;
                  aQuotaEsenteRimborso:=aQuotaEsenteRimborso + aRecMissioneDettaglio.im_quota_esente;

               END IF;
--pipe.send_message('aImportoLordoGenerico = '||aImportoLordoGenerico);
--pipe.send_message('aQuotaEsenteGenerico = '||aQuotaEsenteGenerico);
            END LOOP;

            CLOSE gen_cur_d;

            IF    aRecMissioneTappa.fl_comune_proprio = 'Y' THEN
                  aImportoLordoComuneP:=aImportoLordoComuneP + aImportoLordoGenerico;
                  aQuotaEsenteComuneP:=aQuotaEsenteComuneP + aQuotaEsenteGenerico;
            ELSIF aRecMissioneTappa.fl_comune_altro = 'Y' THEN
                  aImportoLordoComuneA:=aImportoLordoComuneA + aImportoLordoGenerico;
                  aQuotaEsenteComuneA:=aQuotaEsenteComuneA + aQuotaEsenteGenerico;
            ELSIF aRecMissioneTappa.fl_comune_estero = 'Y' THEN
                  aImportoLordoComuneE:=aImportoLordoComuneE + aImportoLordoGenerico;
                  aQuotaEsenteComuneE:=aQuotaEsenteComuneE + aQuotaEsenteGenerico;
            END IF;

         END;

      END LOOP;

      CLOSE gen_cur_t;

      IF aRecMissione.fl_comune_estero = 'N' THEN
         inImportoLordo:=aImportoLordoComuneP + aImportoLordoComuneA;
         inQuotaEsente:=aQuotaEsenteComuneP + aQuotaEsenteComuneA;
      ELSE
--pipe.send_message('inAliquotaIrpef = '||inAliquotaIrpef);

         lordizzaImporto(aImportoLordoComuneE,
                         aQuotaEsenteComuneE,
                         inAliquotaIrpef,
                         aRecAnagrafico.aliquota_fiscale);

         inImportoLordo:=aImportoLordoComuneP + aImportoLordoComuneA + aImportoLordoComuneE + aImportoRimborso;
         inQuotaEsente:=aQuotaEsenteComuneP + aQuotaEsenteComuneA + aQuotaEsenteComuneE + aQuotaEsenteRimborso;
--pipe.send_message('inImportoLordo = '||inImportoLordo);
--pipe.send_message('inQuotaEsente = '||inQuotaEsente);

      END IF;

   END;

   RETURN;

END calcolaImportiCompenso;

--==================================================================================================
-- Lordizzazione importo per missione estera
--==================================================================================================
PROCEDURE lordizzaImporto
   (
    aInOutImportoLordo IN OUT NUMBER,
    aInOutQuotaEsente IN OUT NUMBER,
    inAliquotaIrpef IN OUT NUMBER,
    aAliquotaFiscaleAnag ANAGRAFICO.aliquota_fiscale%TYPE
   ) IS
   i BINARY_INTEGER;
   eseguiLock CHAR(1);
   aImportoNetto NUMBER(15,2);
   aMontanteIRPEF NUMBER(15,2);
   aMontanteINPS NUMBER(15,2);
   aAliquotaIrpef NUMBER(10,6);
   aAliquotaInps NUMBER(10,6);
   aAliquotaAnag NUMBER(10,6);
   isAnnualizzato CHAR(1);
   aImportoAccessoScaglione NUMBER(15,2);
   aImportoMaxRifScaglione NUMBER(15,2);
   aRecMontanti MONTANTI%ROWTYPE;
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;
   aRecVTrattamento V_TIPO_TRATTAMENTO_TIPO_CORI%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   aMontanteIRPEF:=0;
   aMontanteINPS:=0;
   aAliquotaIrpef:=0;
   aAliquotaInps:=0;
   aAliquotaAnag:=0;
   aImportoNetto:=aInOutImportoLordo - aInOutQuotaEsente;
   coriLordizza_tab.DELETE;
   i:=0;

   -------------------------------------------------------------------------------------------------
   -- Lettura dei montanti

   eseguiLock:='Y';
   aRecMontanti:=CNRCTB080.getMontanti(aRecMissione.esercizio,
                                       aRecAnagrafico.cd_anag,
                                       eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Recupero dati del tipo trattamento indicato nel compenso

   aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento(aRecMissione.cd_trattamento,
                                                     aRecMissione.dt_registrazione);

   -------------------------------------------------------------------------------------------------
   -- Recupero dei dati relativi al trattamento selezionato per la missione

   BEGIN

      OPEN gen_cur FOR

           SELECT *
           FROM   V_TIPO_TRATTAMENTO_TIPO_CORI
           WHERE  cd_trattamento = aRecTipoTrattamento.cd_trattamento AND
                  dt_ini_val_trattamento = aRecTipoTrattamento.dt_ini_validita AND
                  dt_fin_val_trattamento = aRecTipoTrattamento.dt_fin_validita AND
                  dt_ini_val_tratt_cori <= aRecMissione.dt_registrazione AND
                  dt_fin_val_tratt_cori >= aRecMissione.dt_registrazione AND
                  dt_ini_val_tipo_cori <= aRecMissione.dt_registrazione AND
                  dt_fin_val_tipo_cori >= aRecMissione.dt_registrazione AND
                  fl_uso_in_lordizza = 'Y'
           ORDER BY cd_trattamento,
                    id_riga;

      LOOP

         FETCH gen_cur INTO aRecVTrattamento;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Controllo se si tratta di contributo ritenuta che identifica irpef a scaglioni

         IF aRecVTrattamento.pg_classificazione_montanti IS NOT NULL THEN
            IF CNRCTB545.getIsIrpefScaglioni(aRecVTrattamento.cd_classificazione_cori,
                                             aRecVTrattamento.pg_classificazione_montanti,
                                             aRecVTrattamento.fl_scrivi_montanti) = 'Y' THEN
               i:=i + 1;
               coriLordizza_tab(i).tCdCori:=aRecVTrattamento.cd_cori;
               coriLordizza_tab(i).tIsIrpef:='Y';
               IF aRecMissione.ti_anagrafico = 'D' THEN
                  aMontanteIRPEF:=aMontanteIRPEF + aRecMontanti.irpef_dipendenti;
               ELSE
                  aMontanteIRPEF:=aMontanteIRPEF + aRecMontanti.irpef_altri;
               END IF;
            ELSE
               i:=i + 1;
               coriLordizza_tab(i).tCdCori:=aRecVTrattamento.cd_cori;
               coriLordizza_tab(i).tIsIrpef:='N';
               IF aRecVTrattamento.fl_scrivi_montanti = 'Y' THEN
                  IF aRecVTrattamento.pg_classificazione_montanti = 2 THEN
                     IF aRecMissione.ti_anagrafico = 'D' THEN
                        aMontanteINPS:=aMontanteINPS + aRecMontanti.inps_dipendenti;
                     ELSE
                        aMontanteINPS:=aMontanteINPS + aRecMontanti.inps_altri;
                     END IF;
                  ELSE
                     IF aRecVTrattamento.pg_classificazione_montanti = 3 THEN
                        IF aRecMissione.ti_anagrafico = 'D' THEN
                           aMontanteINPS:=aMontanteINPS + aRecMontanti.inps_tesoro_dipendenti;
                        END IF;
                     END IF;
                     IF aRecVTrattamento.pg_classificazione_montanti = 7 THEN
                         aMontanteINPS:=aMontanteINPS + aRecMontanti.inps_occasionali;
                     END IF;
                  END IF;
               END IF;
            END IF;
         END IF;

      END LOOP;

      CLOSE gen_cur;

   END;

   -------------------------------------------------------------------------------------------------
   -- Verifico se devo eseguire la lordizzazione

   IF coriLordizza_tab.COUNT > 0 THEN

      aImportoAccessoScaglione:=0;
      aImportoMaxRifScaglione:=0;

      BEGIN

         FOR i IN coriLordizza_tab.FIRST .. coriLordizza_tab.LAST

         LOOP

            isAnnualizzato:='N';

            IF coriLordizza_tab(i).tIsIrpef = 'Y' THEN

               aImportoAccessoScaglione:=aMontanteIrpef;
               aImportoMaxRifScaglione:=aMontanteIrpef;
               aAliquotaAnag:=0;

               -- Determino le eventuali eccezioni in calcolo irpef solo per il calcolo non dipendente

               IF aRecMissione.ti_anagrafico = 'A' THEN

                  IF aRecTipoTrattamento.fl_irpef_annualizzata = 'Y' THEN
                     isAnnualizzato:='2';
                     aImportoAccessoScaglione:=0;
                     aImportoMaxRifScaglione:=ROUND(
                                                      (aImportoNetto /
                                                       IBMUTL001.getDaysCommBetween
                                                            (TRUNC(aRecMissione.dt_inizio_missione),
                                                             TRUNC(aRecMissione.dt_fine_missione)
                                                            ) *
                                                       360
                                                      ),2
                                                   );
                     aAliquotaAnag:=0;
                  END IF;

                  IF (aAliquotaFiscaleAnag IS NOT NULL AND
                      aAliquotaFiscaleAnag > 0) THEN
                     isAnnualizzato:='1';
                     aImportoAccessoScaglione:=0;
                     aAliquotaAnag:=aAliquotaFiscaleAnag;
                  END IF;

               END IF;

               aAliquotaIrpef:=aAliquotaIrpef + getAliquotaLordizza(coriLordizza_tab(i).tIsIrpef,
                                                                    coriLordizza_tab(i).tCdCori,
                                                                    aRecTipoTrattamento.ti_anagrafico,
                                                                    aRecMissione.dt_registrazione,
                                                                    isAnnualizzato,
                                                                    aImportoAccessoScaglione,
                                                                    aImportoMaxRifScaglione,
                                                                    aAliquotaAnag);
            ELSE
               aImportoAccessoScaglione:=aMontanteInps;
               aImportoMaxRifScaglione:=aMontanteInps;
               aAliquotaAnag:=0;

               aAliquotaInps:=aAliquotaInps + getAliquotaLordizza(coriLordizza_tab(i).tIsIrpef,
                                                                    coriLordizza_tab(i).tCdCori,
                                                                    aRecTipoTrattamento.ti_anagrafico,
                                                                    aRecMissione.dt_registrazione,
                                                                    isAnnualizzato,
                                                                    aImportoAccessoScaglione,
                                                                    aImportoMaxRifScaglione,
                                                                    aAliquotaAnag);
            END IF;

         END LOOP;
--pipe.send_message('aAliquotaIrpef = '||aAliquotaIrpef);
--pipe.send_message('aAliquotaInps = '||aAliquotaInps);

         aImportoNetto:=aImportoNetto * (1 / (1 - aAliquotaIrpef * (1 - aAliquotaInps)));
         aInOutImportoLordo:=aInOutQuotaEsente + aImportoNetto;
--pipe.send_message('aImportoNetto = '||aImportoNetto);
--pipe.send_message('aInOutImportoLordo = '||aInOutImportoLordo);
         -- Ritorna l'aliquota irpef per lordizzazione in caso di trattamento annualizzato

         IF isAnnualizzato = '2' THEN
            inAliquotaIrpef:=(aAliquotaIrpef * 100);
         END IF;

      END;

   END IF;

   RETURN;

END lordizzaImporto;

--==================================================================================================
-- Ritorna l'aliquota cori per lordizzazione
--==================================================================================================
FUNCTION getAliquotaLordizza
   (
    isIrpef CHAR,
    aCdContributoRitenuta SCAGLIONE.cd_contributo_ritenuta%TYPE,
    aTiAnagrafico SCAGLIONE.ti_anagrafico%TYPE,
    aDataRif DATE,
    isAnnualizzato CHAR,
    aImportoAccesso IN OUT SCAGLIONE.im_inferiore%TYPE,
    aImportoMaxRif SCAGLIONE.im_inferiore%TYPE,
    aAliquotaAnag IN OUT SCAGLIONE.aliquota%TYPE
   ) RETURN SCAGLIONE.aliquota%TYPE IS
   aCdRegione COMPENSO.cd_regione_add%TYPE;
   aCdProvincia COMPENSO.cd_provincia_add%TYPE;
   aPgComune COMPENSO.pg_comune_add%TYPE;
   aAliquotaPerLordizza SCAGLIONE.aliquota%TYPE;
   aImponibileBlocco NUMBER(15,2);
   resto NUMBER(15,2);
   aImportoIrpef NUMBER(15,2);

   aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE;
   aRecTmpScaglioneImponibile V_SCAGLIONE%ROWTYPE;

   gen_cur_s GenericCurTyp;

BEGIN

   -- Lettura dello scaglione per il recupero dell'aliquota cori

   aCdRegione:='*';
   aCdProvincia:='*';
   aPgComune:=0;
   aImportoIrpef:=0;
   aAliquotaPerLordizza:=0;

   aRecVPreScaglione:=CNRCTB545.getScaglione(aCdContributoRitenuta,
                                             aTiAnagrafico,
                                             aDataRif,
                                             aImportoAccesso,
                                             aAliquotaAnag,
                                             aCdRegione,
                                             aCdProvincia,
                                             aPgComune);

   -- L'accesso agli scaglioni successivi è svolto solo in caso di annualizzazione altrimenti si torna
   -- l'aliquota dello scaglione

   IF isAnnualizzato = '2' THEN

      -- Se il valore annualizzato è contenuto nel primo scaglione ritorno l'aliquota determinata altrimenti
      -- determino l'importo IRPEF per tutti gli scaglioni

      IF aRecVPreScaglione.im_superiore >= aImportoMaxRif THEN
         aAliquotaPerLordizza:=aRecVPreScaglione.aliquota_percip / 100;

      ELSE

         -- Valorizzazione importo IRPEF per il primo scaglione

         aImponibileBlocco:=aRecVPreScaglione.im_superiore;
         resto:=aImportoMaxRif - aImponibileBlocco;
         aImportoIrpef:=aImportoIrpef + ROUND(aImponibileBlocco * (aRecVPreScaglione.aliquota_percip / 100),2);

         -- Valorizzazione importo IRPEF per i restanti scaglioni

         BEGIN

            OPEN gen_cur_s FOR

                 SELECT *
                 FROM   V_SCAGLIONE
                 WHERE  cd_contributo_ritenuta = aCdContributoRitenuta AND
                        (ti_anagrafico = aTiAnagrafico OR
                         ti_anagrafico = '*' ) AND
                        dt_inizio_validita <=aDataRif AND
                        dt_fine_validita >= aDataRif AND
                        cd_regione = aCdRegione AND
                        cd_provincia = aCdProvincia AND
                        pg_comune = aPgComune AND
                        im_inferiore >= aRecVPreScaglione.im_superiore AND
                        im_inferiore <= aImportoMaxRif
                 ORDER BY 4;

            LOOP

               FETCH gen_cur_s INTO aRecTmpScaglioneImponibile;

               EXIT WHEN gen_cur_s%NOTFOUND;

               IF aImportoMaxRif < aRecTmpScaglioneImponibile.im_superiore THEN
                  aImponibileBlocco:=resto;
               ELSE
                  aImponibileBlocco:=aRecTmpScaglioneImponibile.im_superiore -
                                     aRecTmpScaglioneImponibile.im_inferiore +
                                     1;
                  resto:=resto - aImponibileBlocco;
               END IF;

               aImportoIrpef:=aImportoIrpef + ROUND(aImponibileBlocco * (aRecTmpScaglioneImponibile.aliquota_percip / 100),2);

            END LOOP;

            -- close cursore

            CLOSE gen_cur_s;

         END;

            aAliquotaPerLordizza:=(aImportoIrpef / aImportoMaxRif);

      END IF;

   ELSE
      aAliquotaPerLordizza:=aRecVPreScaglione.aliquota_percip / 100;
   END IF;

   RETURN aAliquotaPerLordizza;

END getAliquotaLordizza;


--==================================================================================================
-- Controllo eliminazione della missione
--==================================================================================================
PROCEDURE eseguiDelMissione
   (
    inCdsMissione MISSIONE.cd_cds%TYPE,
    inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
    inEsercizioMissione MISSIONE.esercizio%TYPE,
    inPgMissione MISSIONE.pg_missione%TYPE,
    statoCancella IN OUT NUMBER
   ) IS
   eseguiLock CHAR(1);
   aTipoChiamata CHAR(1);
   aStatoCancellazione INTEGER;
   fl_chk_anticipo CHAR(1);
   aRecMissione MISSIONE%ROWTYPE;
   aRecCompenso COMPENSO%ROWTYPE;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione variabili

   eseguiLock:='Y';
   aTipoChiamata:='M';
   aStatoCancellazione:=0;
   IF statoCancella = 0 THEN
      fl_chk_anticipo:='N';
   ELSE
      fl_chk_anticipo:='Y';
   END IF;
   statoCancella:=0;

   -------------------------------------------------------------------------------------------------
   -- Recupero la missione

   aRecMissione:=CNRCTB500.getMissione (inCdsMissione,
                                        inUoMissione,
                                        inEsercizioMissione,
                                        inPgMissione,
                                        eseguiLock);

   -- La missione risulta essere già in stato annullato

   IF aRecMissione.stato_cofi = 'A' THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('La missone selezionata risulta essere già stata annullata in data ' ||
          TO_CHAR(aRecMissione.dt_cancellazione,'DD/MM/YYYY'));
   END IF;

   -- La missione risulta essere associato a mandati e/o reversali

   IF aRecMissione.stato_cofi = 'P' THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Impossibile eliminare una missione che risulta pagata. Occorre prima annullare ' ||
          'il mandato associato');
   END IF;

   -- La missione risulta essere associato ad una spesa del fondo economale

   IF aRecMissione.stato_pagamento_fondo_eco = 'R' THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Impossibile eliminare una missione che risulta associata ad una spesa su fondo economale.');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Sgangio anticipo da missione

   IF (fl_chk_anticipo = 'Y' AND
       aRecMissione.pg_anticipo IS NOT NULL) THEN
      CNRCTB580.sganciaAnticipoDaMissione(aRecMissione.cd_cds_anticipo,
                                          aRecMissione.cd_uo_anticipo,
                                          aRecMissione.esercizio_anticipo,
                                          aRecMissione.pg_anticipo,
                                          aRecMissione.utuv,
                                          sysdate);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Verifico se la missione è associata ad un compenso

   IF aRecMissione.fl_associato_compenso = 'Y' THEN

      aRecCompenso:=CNRCTB545.getCompensoDaMissione(aRecMissione.cd_cds,
                                                    aRecMissione.cd_unita_organizzativa,
                                                    aRecMissione.esercizio,
                                                    aRecMissione.pg_missione,
                                                    eseguiLock);

      -- Controllo la cancellazione del compenso

      aStatoCancellazione:=CNRCTB550.cancellaCompenso(aRecCompenso.cd_cds,
                                                      aRecCompenso.cd_unita_organizzativa,
                                                      aRecCompenso.esercizio,
                                                      aRecCompenso.pg_compenso,
                                                      aTipoChiamata);

      IF aStatoCancellazione = CNRCTB545.isCancellazioneFisica THEN
         CNRCTB545.eliminaFisicoCompenso(aRecCompenso.cd_cds,
                                         aRecCompenso.cd_unita_organizzativa,
                                         aRecCompenso.esercizio,
                                         aRecCompenso.pg_compenso);
      ELSE
         CNRCTB545.eliminaLogicoCompenso(aRecCompenso.cd_cds,
                                         aRecCompenso.cd_unita_organizzativa,
                                         aRecCompenso.esercizio,
                                         aRecCompenso.pg_compenso,
                                         aRecMissione.utuv,
                                         'MI');
      END IF;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Ritorno lo stato della cancellazione da farsi sulla missione

   IF aStatoCancellazione = 0 THEN
      IF aRecMissione.ti_associato_manrev = 'N'  AND
	  	 (aRecMissione.stato_coge = 'N' OR aRecMissione.stato_coge = 'X') AND
		 (aRecMissione.stato_coan = 'N' OR aRecMissione.stato_coan = 'X') THEN
         aStatoCancellazione:=CNRCTB545.isCancellazioneFisica;
      ELSE
         aStatoCancellazione:=CNRCTB545.isCancellazioneLogica;
      END IF;
   END IF;

   statoCancella:=aStatoCancellazione;

END eseguiDelMissione;

END;
