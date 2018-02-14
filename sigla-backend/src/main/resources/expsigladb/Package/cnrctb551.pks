CREATE OR REPLACE PACKAGE CNRCTB551 AS
--==================================================================================================
--
-- CNRCTB551 - Calcolo e scrittura COMPENSI (Elaborazione imponibile fiscale per compensi da minicariera
--                                           con trattamento annualizzato)
--
-- Date: 02/11/2004
-- Version: 1.2
--
-- Dependency: CNRCTB 080/545/600 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 24/03/2003
-- Version: 1.0
--
-- Richiesta CINECA n.541. Revisione del calcolo dell'aliquota fiscale media in pagamento compensi da
-- minicarriera. Modifica metodo getImRateDiTerzoInEsercizio, non si esporta pi? semplicemente il valore
-- totale delle rate di minicarriera con scadenza nell'esercizio per un dato soggetto anagrafico ma
-- a rottura di ogni trattamento, si procede al calcolo del vero imponibile fiscale.
--
-- Date: 28/03/2003
-- Version: 1.1
--
-- Fix errore interno in calcolo compenso, l'imponibile era preso dal compenso in calcolo e non dal
-- valore esterno.
--
-- Date: 02/11/2004
-- Version: 1.2
--
-- Rilascio errore CINECA n. 850. errore in esegui calcolo di compenso da minicarriera di un terzo
-- avente anche una minicarriera con trattamento cessato.
--
-- Date: 01/08/2006
-- Version: 2.8
--
-- Aggiunta la nuova "Gestione dei Cervelli"
--==================================================================================================
--
-- Constants
--

--
-- Variabili globali
--

   dataOdierna DATE;

   -- Memorizza la testata del compenso in elaborazione

   aRecCompenso COMPENSO%ROWTYPE;

   -- Memorizza l'anagrafico di riferimento per il terzo beneficiario del compenso

   aRecAnagrafico ANAGRAFICO%ROWTYPE;

   -- Memorizza il tipo trattamento per il compenso in elaborazione

   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

   -- Definizione tabella PL/SQL di appoggio per calcolato contributi e ritenute

   tabella_cori CNRCTB545.insiemeCoriTab;
   tabella_cori_det CNRCTB545.insiemeCoriTab;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

-- Determinazione dell'imponibile fiscale al netto delle eventuali previdenziali

   PROCEDURE calcolaImponibileIrpef
      (
       inRecAnagrafico ANAGRAFICO%ROWTYPE,
       inRecCompenso COMPENSO%ROWTYPE,
       inCdTrattamento VARCHAR2,
       inImportoLordoIrpef NUMBER,
       aImponibileFiscale IN OUT NUMBER
      );

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------

-- Valorizzazione delle matrici per il calcolo dei dettagli di un compenso; CONTRIBUTO_RITENUTA e
-- CONTRIBUTO_RITENUTA_DET

   PROCEDURE costruisciTabellaCori;

   PROCEDURE costruisciTabellaCoriDet
      (
       indice BINARY_INTEGER,
       aImponibile CONTRIBUTO_RITENUTA_DET.imponibile%TYPE,
       aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE
      );

-- Verifica consistenza dei dati della matrice per il calcolo dei dettagli di un compenso

   PROCEDURE verificaTabellaCori;

-- Calcola CORI per trattamenti altro

   PROCEDURE calcolaCoriAltro
      (
       inImportoLordoIrpef NUMBER,
       aImponibileFiscale IN OUT NUMBER
      );

-- Valorizza i dati per contributi ritenuta definiti per territorio

   PROCEDURE getDatiTerritorio
      (
       aCdRegione IN OUT COMPENSO.cd_regione_add%TYPE,
       aCdProvincia IN OUT COMPENSO.cd_provincia_add%TYPE,
       aPgComune IN OUT COMPENSO.pg_comune_add%TYPE,
       indice BINARY_INTEGER
      );

-- Calcolo imponibile per ogni singolo cori del trattamento

   FUNCTION getImponibileCori
      (
       indice BINARY_INTEGER,
       aImponibile NUMBER
      )RETURN NUMBER;

END CNRCTB551;


CREATE OR REPLACE PACKAGE BODY CNRCTB551 AS

-- =================================================================================================
-- Determinazione dell'imponibile fiscale al netto delle eventuali previdenziali
-- =================================================================================================
PROCEDURE calcolaImponibileIrpef
   (
    inRecAnagrafico ANAGRAFICO%ROWTYPE,
    inRecCompenso COMPENSO%ROWTYPE,
    inCdTrattamento VARCHAR2,
    inImportoLordoIrpef NUMBER,
    aImponibileFiscale IN OUT NUMBER
   ) IS
   imCORIPercipiente NUMBER(15,2);
   imCORIEnte NUMBER(15,2);
   eseguiLock CHAR(1);
   aInserimentoModifica CHAR(1);
   aGestioneChiuso CHAR(1);

BEGIN
   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   dataOdierna:=sysdate;
   eseguiLock:='Y';
   aInserimentoModifica:='I';
   aGestioneChiuso:='Y';

   -- Valorizzazione delle variabili globali relative al record di anagrafico e del compenso in calcolo

   aRecAnagrafico:=inRecAnagrafico;
   aRecCompenso:=inRecCompenso;

   -------------------------------------------------------------------------------------------------
   -- Recupero dati del tipo trattamento indicato nel compenso

   aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento(inCdTrattamento,
                                                     aRecCompenso.dt_registrazione,
                                                     aGestioneChiuso);


   -------------------------------------------------------------------------------------------------
   -- Costruzione e verifica della matrice per il calcolo compensi
   CostruisciTabellaCori;
   VerificaTabellaCori;

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione calcolo cori nella matrice di calcolo compensi (solo soggetti altri)

   aImponibileFiscale:=0;
   calcolaCoriAltro(inImportoLordoIrpef,
                    aImponibileFiscale);

END calcolaImponibileIrpef;

-- =================================================================================================
-- Valorizzazione della matrice per il calcolo dei dettagli CORI di un compenso
-- =================================================================================================
PROCEDURE costruisciTabellaCori
   IS
   i BINARY_INTEGER;
   aRecVTrattamento V_TIPO_TRATTAMENTO_TIPO_CORI%ROWTYPE;
   gen_cur GenericCurTyp;

BEGIN

   -- Valorizzazione variabili

   tabella_cori.DELETE;
   tabella_cori_det.DELETE;
   i:=0;

   BEGIN

      -- Estrazione dei contributi ritenuta associati al tipo trattamento letto dal compenso
      -- Lettura storica dalla vista in base alla data di registrazione del compenso

      OPEN gen_cur FOR

           SELECT *
           FROM   V_TIPO_TRATTAMENTO_TIPO_CORI
           WHERE  cd_trattamento = aRecTipoTrattamento.cd_trattamento AND
                  dt_ini_val_trattamento = aRecTipoTrattamento.dt_ini_validita AND
                  dt_fin_val_trattamento = aRecTipoTrattamento.dt_fin_validita AND
                  dt_ini_val_tratt_cori <= aRecCompenso.dt_registrazione AND
                  dt_fin_val_tratt_cori >= aRecCompenso.dt_registrazione AND
                  dt_ini_val_tipo_cori <= aRecCompenso.dt_registrazione AND
                  dt_fin_val_tipo_cori >= aRecCompenso.dt_registrazione
           ORDER BY cd_trattamento,
                    id_riga;

      LOOP

         FETCH gen_cur INTO aRecVTrattamento;

         EXIT WHEN gen_cur%NOTFOUND;

         i:=i + 1;
         tabella_cori(i).tCdCori:=aRecVTrattamento.cd_cori;
         tabella_cori(i).tDtIniValCori:=aRecVTrattamento.dt_ini_val_tipo_cori;
         tabella_cori(i).tTiCassaCompetenza:=aRecVTrattamento.ti_cassa_competenza;
         tabella_cori(i).tPrecisione:=aRecVTrattamento.precisione;
         tabella_cori(i).tPgClassificazioneMontanti:=aRecVTrattamento.pg_classificazione_montanti;
         tabella_cori(i).tCdClassificazioneCori:=aRecVTrattamento.cd_classificazione_cori;
         tabella_cori(i).tFlScriviMontanti:=aRecVTrattamento.fl_scrivi_montanti;
         tabella_cori(i).tIdRiga:=aRecVTrattamento.id_riga;
         tabella_cori(i).tSegno:=aRecVTrattamento.segno;
         tabella_cori(i).tCalcoloImponibile:=aRecVTrattamento.calcolo_imponibile;
         tabella_cori(i).tFlSospensioneIrpef:=aRecVTrattamento.fl_sospensione_irpef;
         tabella_cori(i).tMontante:=NULL;
         tabella_cori(i).tImponibileLordo:=NULL;
         tabella_cori(i).tImDeduzioneIrpef:=NULL;
         tabella_cori(i).tImponibileNetto:=NULL;
         tabella_cori(i).tAliquotaEnte:=NULL;
         tabella_cori(i).tBaseCalcoloEnte:=NULL;
         tabella_cori(i).tAmmontareEnteLordo:=NULL;
         tabella_cori(i).tAmmontareEnte:=NULL;
         tabella_cori(i).tAliquotaPercip:=NULL;
         tabella_cori(i).tBaseCalcoloPercip:=NULL;
         tabella_cori(i).tAmmontarePercipLordo:=NULL;
         tabella_cori(i).tAmmontarePercip:=NULL;

      END LOOP;

      CLOSE gen_cur;

   END;

END costruisciTabellaCori;

-- =================================================================================================
-- Verifica consistenza dei dati della matrice per il calcolo dei dettagli CORI di un compenso
-- =================================================================================================
PROCEDURE verificaTabellaCori
   IS
   i BINARY_INTEGER;
   conta NUMBER;
   aNumeroBase NUMBER(15,2);
   aNumeroIntero NUMBER(15,2);
   aBloccoCalcolo VARCHAR2(5);

BEGIN

   conta:=tabella_cori.COUNT;

   -- Errore in estrazione specifiche dei cori definiti per il trattamento in esame

   IF conta = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO
                   ('Nessun record valido di specifica contributi e ritenute per il trattamento ' ||
                    aRecTipoTrattamento.cd_trattamento);
   END IF;

   -- Errore nella definizione delle regole di calcolo

   FOR i IN tabella_cori.FIRST .. tabella_cori.LAST

   LOOP

      -- Errata sequenza tra elementi in matrice e id riga del calcolo

      IF LPAD(i,3,0) != tabella_cori(i).tIdRiga THEN
         IBMERR001.RAISE_ERR_GENERICO
                      ('Errore in sequenza dell''algoritmo di calcolo definito in TRATTAMENTO_CORI ' ||
                       CHR(10) || 'Sequenza ' || LPAD(i,3,0) || ' Id riga ' || tabella_cori(i).tIdRiga);
      END IF;

      -- Errata valorizzazione delle formule di calcolo

      IF tabella_cori(i).tCalcoloImponibile != '000' THEN

         -- Errata dimensione della formula di calcolo

         aNumeroBase:=(LENGTH(tabella_cori(i).tCalcoloImponibile) / 5);
         aNumeroIntero:=TRUNC(aNumeroBase);
         IF aNumeroBase != aNumeroIntero THEN
            IBMERR001.RAISE_ERR_GENERICO
                         ('Errore di dimensione (non divisibile per 5) della formula di calcolo in TRATTAMENTO_CORI ' ||
                          CHR(10) || 'id riga ' || tabella_cori(i).tIdRiga);
         END IF;

         FOR j IN 1 .. (LENGTH(tabella_cori(i).tCalcoloImponibile) / 5)

         LOOP

            aBloccoCalcolo:=SUBSTR(tabella_cori(i).tCalcoloImponibile, ((j * 5) - 5 + 1), 5);

            -- Segno non correttamente definito

            IF  (SUBSTR(aBloccoCalcolo,1,1) != '+' AND
                 SUBSTR(aBloccoCalcolo,1,1) != '-') THEN
                IBMERR001.RAISE_ERR_GENERICO
                             ('Errore in valorizzazione del segno nella formula di calcolo in TRATTAMENTO_CORI ' ||
                              CHR(10) || 'id riga ' || tabella_cori(i).tIdRiga);
            END IF;

            -- Riferimento al valore ente, percepiente o entrambi non correttamente definito

            IF  (SUBSTR(aBloccoCalcolo,5,1) != 'E' AND
                 SUBSTR(aBloccoCalcolo,5,1) != 'P' AND
                 SUBSTR(aBloccoCalcolo,5,1) != '*'And
                 SUBSTR(aBloccoCalcolo,5,1) != '%') THEN
                IBMERR001.RAISE_ERR_GENERICO
                             ('Errore in valorizzazione del riferimento a ente, percepiente o entrambi nella ' ||
                              'formula di calcolo in TRATTAMENTO_CORI ' ||
                              CHR(10) || 'id riga ' || tabella_cori(i).tIdRiga);
            END IF;

            -- Errato puntatore id riga della formula
	    If SUBSTR(aBloccoCalcolo,5,1) != '%' Then
              IF (LPAD(conta,3,0) < SUBSTR(aBloccoCalcolo,2,3) OR
                  tabella_cori(i).tIdRiga <= SUBSTR(aBloccoCalcolo,2,3)) THEN
                  IBMERR001.RAISE_ERR_GENERICO
                               ('Errore in valorizzazione del riferimento id riga nella ' ||
                                'formula di calcolo in TRATTAMENTO_CORI ' ||
                                CHR(10) || 'id riga ' || tabella_cori(i).tIdRiga ||
                                ' formula ' || SUBSTR(aBloccoCalcolo,2,3));
              END IF;
            End If;

         END LOOP;

      END IF;

   END LOOP;

END verificaTabellaCori;

-- =================================================================================================
-- Calcolo del singolo contributo/ritenuta
-- =================================================================================================
PROCEDURE calcolaCoriAltro
   (
    inImportoLordoIrpef NUMBER,
    aImponibileFiscale IN OUT NUMBER
   ) IS
   i BINARY_INTEGER;

   aCdRegione COMPENSO.cd_regione_add%TYPE;
   aCdProvincia COMPENSO.cd_provincia_add%TYPE;
   aPgComune COMPENSO.pg_comune_add%TYPE;

   aImponibileLordoCori NUMBER(15,2);
   aImponibileNettoCori NUMBER(15,2);

   isRilevaAnnualizzato CHAR(1);

   aImportoAccessoScaglione NUMBER(15,2);
   aImportoMaxRifScaglione NUMBER(15,2);
   aImportoAccessoDetrazioni NUMBER(15,2);
   aAliquotaIrpefAnag SCAGLIONE.aliquota%TYPE;
   aBloccoCalcolo VARCHAR2(5);
   aImponibileBlocco NUMBER(15,2);
   resto NUMBER(15,2);
   aTiEntePercip SCAGLIONE.ti_ente_percipiente%TYPE;

   aRecScaglioneMontante V_PRE_SCAGLIONE%ROWTYPE;
   aRecTmpScaglioneImponibile V_SCAGLIONE%ROWTYPE;
   aRecScaglioneImponibile V_PRE_SCAGLIONE%ROWTYPE;
   aRecVoceIva VOCE_IVA%ROWTYPE;
   aRecTipologiaRischio TIPOLOGIA_RISCHIO%ROWTYPE;

   gen_cur_a GenericCurTyp;

BEGIN

   BEGIN

      ----------------------------------------------------------------------------------------------
      -- Loop sulla matrice dei codici contributo/ritenuta da elaborare per il trattamento in gestione

      FOR i IN tabella_cori.FIRST .. tabella_cori.LAST

      LOOP

         ----------------------------------------------------------------------------------------------
         -- Azzeramento variabili per il calcolo

         aImponibileLordoCori:=0;
         aTiEntePercip:=NULL;
         isRilevaAnnualizzato:='N';

         -------------------------------------------------------------------------------------------
         -- Valorizzazione dei parametri di regione, provincia e comune

         getDatiTerritorio(aCdRegione,
                           aCdProvincia,
                           aPgComune,
                           i);

         -------------------------------------------------------------------------------------------
         -- Determino il valore dell'imponibile reale per ogni singolo cori in trattamento.

         aImponibileLordoCori:=getImponibileCori(i,
                                                 inImportoLordoIrpef);

         -------------------------------------------------------------------------------------------
         -- Recupero dello scaglione di riferimento al calcolo.
         -- Se opero su un cori IRPEF esco dalla procedura

         IF tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriFiscale THEN
            aImponibileFiscale:=aImponibileLordoCori;
            EXIT;
         END IF;

         -- Determinazione del valore massimo complessivo di accesso allo scaglione (valore lordo)

         aImportoMaxRifScaglione:=aImponibileLordoCori;

         -------------------------------------------------------------------------------------------
         -- Determino il valore di ingresso per la ricerca dello scaglione.

         aImportoAccessoScaglione:=0;
         aAliquotaIrpefAnag:=0;

         -- i cori INAIL e IVA non sono gestiti sulla tabella scaglioni ma i valori estratti sono
         -- portati sulla struttura dello stesso per congruenza della procedura

         IF    tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriIva THEN
               IF aRecCompenso.cd_voce_iva IS NOT NULL THEN
                  aRecVoceIva:=CNRCTB545.getVoceIva(aRecCompenso.cd_voce_iva);
               ELSE
                  aRecVoceIva:=NULL;
                  aRecVoceIva.percentuale:=0;
               END IF;
               aRecScaglioneMontante:=NULL;
               aRecScaglioneMontante.cd_contributo_ritenuta:=aRecVoceIva.cd_voce_iva;
               aRecScaglioneMontante.ti_ente_percipiente:='E';
               aRecScaglioneMontante.aliquota_ente:=aRecVoceIva.percentuale;
               aRecScaglioneMontante.base_calcolo_ente:=100;
               aRecScaglioneMontante.im_inferiore:=0;
               aRecScaglioneMontante.im_superiore:=9999999999999;
         ELSIF tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriInail THEN
               IF aRecCompenso.cd_tipologia_rischio IS NOT NULL THEN
                  aRecTipologiaRischio:=CNRCTB545.getTipologiaRischio(aRecCompenso.cd_tipologia_rischio,
                                                                      aRecCompenso.dt_registrazione);
               ELSE
                  aRecTipologiaRischio:=NULL;
                  aRecTipologiaRischio.aliquota_ente:=0;
                  aRecTipologiaRischio.aliquota_percipiente:=0;
               END IF;
               aRecScaglioneMontante:=NULL;
               aRecScaglioneMontante.cd_contributo_ritenuta:=aRecTipologiaRischio.cd_tipologia_rischio;
               aRecScaglioneMontante.ti_ente_percipiente:='*';
               aRecScaglioneMontante.aliquota_ente:=aRecTipologiaRischio.aliquota_ente;
               aRecScaglioneMontante.aliquota_percip:=aRecTipologiaRischio.aliquota_percipiente;
               aRecScaglioneMontante.base_calcolo_ente:=100;
               aRecScaglioneMontante.base_calcolo_percip:=100;
               aRecScaglioneMontante.im_inferiore:=0;
               aRecScaglioneMontante.im_superiore:=9999999999999;
         ELSE

            -- Lettura degli scaglioni (uno solo, se si opera su pi? scaglioni ? il primo)
            aRecScaglioneMontante:=CNRCTB545.getScaglione(tabella_cori(i).tCdCori,
                                                          aRecTipoTrattamento.ti_anagrafico,
                                                          aRecCompenso.dt_registrazione,
                                                          aImportoAccessoScaglione,
                                                          aAliquotaIrpefAnag,
                                                          aCdRegione,
                                                          aCdProvincia,
                                                          aPgComune);
         END IF;

         -------------------------------------------------------------------------------------------
         -- Lettura degli scaglioni, verifico che il montante arricchito dell'imponibile non determini
         -- un nuovo scaglione

         -- L'imponibile o il trattamento non prevedono l'operativit? su pi? scaglioni

         IF aRecScaglioneMontante.im_superiore >= aImportoMaxRifScaglione THEN

            -- Valorizzazione di imponibile e montante. Imponibile ? il vero valore dell'imponibile mentre
            -- montante ? quello utilizzato per il recupero dell'aliquota dagli scaglioni

            tabella_cori(i).tImponibileLordo:=aImponibileLordoCori;
            tabella_cori(i).tImponibileNetto:=aImponibileLordoCori;
            tabella_cori(i).tImDeduzioneIrpef:=0;
            tabella_cori(i).tMontante:=aImportoAccessoScaglione;

            IF (aRecScaglioneMontante.ti_ente_percipiente = 'P' OR
                aRecScaglioneMontante.ti_ente_percipiente = '*') THEN
               tabella_cori(i).tAliquotaPercip:=aRecScaglioneMontante.aliquota_percip;
               tabella_cori(i).tBaseCalcoloPercip:=aRecScaglioneMontante.base_calcolo_percip;
               tabella_cori(i).tAmmontarePercipLordo:=ROUND(aImponibileLordoCori *
                                                            (aRecScaglioneMontante.base_calcolo_percip / 100) *
                                                            (tabella_cori(i).tAliquotaPercip / 100),2);
               tabella_cori(i).tAmmontarePercip:=tabella_cori(i).tAmmontarePercipLordo;
            END IF;

            IF (aRecScaglioneMontante.ti_ente_percipiente = 'E' OR
                aRecScaglioneMontante.ti_ente_percipiente = '*') THEN
               tabella_cori(i).tAliquotaEnte:=aRecScaglioneMontante.aliquota_ente;
               tabella_cori(i).tBaseCalcoloEnte:=aRecScaglioneMontante.base_calcolo_ente;
               tabella_cori(i).tAmmontareEnteLordo:=ROUND(aImponibileLordoCori *
                                                          (aRecScaglioneMontante.base_calcolo_ente / 100) *
                                                          (aRecScaglioneMontante.aliquota_ente / 100),2);
               tabella_cori(i).tAmmontareEnte:=tabella_cori(i).tAmmontareEnteLordo;
            END IF;

         -- L'imponibile o il trattamento prevedono l'operativit? su pi? scaglioni

         ELSE

            -- scrivo l'importo del primo scaglione. Si distinguono gli importi di avvio in base al fatto
            -- che il cori in elaborazione ? annualizzato o meno

            aImponibileBlocco:=aRecScaglioneMontante.im_superiore;
            resto:=aImponibileLordoCori - aImponibileBlocco;

            costruisciTabellaCoriDet(i,
                                     aImponibileBlocco,
                                     aRecScaglioneMontante);

            BEGIN

               OPEN gen_cur_a FOR

                    SELECT *
                    FROM   V_SCAGLIONE
                    WHERE  cd_contributo_ritenuta = tabella_cori(i).tCdCori AND
                           (ti_anagrafico = aRecCompenso.ti_anagrafico OR
                            ti_anagrafico = '*' ) AND
                           dt_inizio_validita <=aRecCompenso.dt_registrazione AND
                           dt_fine_validita >= aRecCompenso.dt_registrazione AND
                           cd_regione = aCdRegione AND
                           cd_provincia = aCdProvincia AND
                           pg_comune = aPgComune AND
                           im_inferiore >= aRecScaglioneMontante.im_superiore AND
                           im_inferiore <= aImportoMaxRifScaglione
                    ORDER BY 4;

               LOOP

                  FETCH gen_cur_a INTO aRecTmpScaglioneImponibile;

                  EXIT WHEN gen_cur_a%NOTFOUND;

                  IF    (aRecTmpScaglioneImponibile.aliquota_ente > 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip > 0) THEN
                        aTiEntePercip:='*';
                  ELSIF (aRecTmpScaglioneImponibile.aliquota_ente > 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip = 0) THEN
                        aTiEntePercip:='E';
                  ELSIF (aRecTmpScaglioneImponibile.aliquota_ente = 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip > 0) THEN
                        aTiEntePercip:='P';
                  END IF;

                  aRecScaglioneImponibile.cd_contributo_ritenuta:=aRecTmpScaglioneImponibile.cd_contributo_ritenuta;
                  aRecScaglioneImponibile.ti_anagrafico:=aRecTmpScaglioneImponibile.ti_anagrafico;
                  aRecScaglioneImponibile.dt_inizio_validita:=aRecTmpScaglioneImponibile.dt_inizio_validita;
                  aRecScaglioneImponibile.im_inferiore:=aRecTmpScaglioneImponibile.im_inferiore;
                  aRecScaglioneImponibile.cd_regione:=aRecTmpScaglioneImponibile.cd_regione;
                  aRecScaglioneImponibile.cd_provincia:=aRecTmpScaglioneImponibile.cd_provincia;
                  aRecScaglioneImponibile.pg_comune:=aRecTmpScaglioneImponibile.pg_comune;
                  aRecScaglioneImponibile.ti_ente_percipiente:=aTiEntePercip;
                  aRecScaglioneImponibile.im_superiore:=aRecTmpScaglioneImponibile.im_superiore;
                  aRecScaglioneImponibile.aliquota_ente:=aRecTmpScaglioneImponibile.aliquota_ente;
                  aRecScaglioneImponibile.aliquota_percip:=aRecTmpScaglioneImponibile.aliquota_percip;
                  aRecScaglioneImponibile.base_calcolo_ente:=aRecTmpScaglioneImponibile.base_calcolo_ente;
                  aRecScaglioneImponibile.base_calcolo_percip:=aRecTmpScaglioneImponibile.base_calcolo_percip;
                  aRecScaglioneImponibile.dt_fine_validita:=aRecTmpScaglioneImponibile.dt_fine_validita;

                  IF aImportoMaxRifScaglione < aRecScaglioneImponibile.im_superiore THEN
                     aImponibileBlocco:=resto;
                  ELSE
                     aImponibileBlocco:=aRecScaglioneImponibile.im_superiore -
                                        aRecScaglioneImponibile.im_inferiore +
                                        0.01;
                     resto:=resto - aImponibileBlocco;
                  END IF;

                  costruisciTabellaCoriDet(i,
                                           aImponibileBlocco,
                                           aRecScaglioneImponibile);

               END LOOP;

               -- close cursore

               CLOSE gen_cur_a;

            END;

            -- Memorizzo imponibile reale e montante di riferimento

            tabella_cori(i).tImponibileLordo:=aImponibileLordoCori;
            tabella_cori(i).tImponibileNetto:=aImponibileLordoCori;
            tabella_cori(i).tImDeduzioneIrpef:=0;
            tabella_cori(i).tMontante:=aImportoAccessoScaglione;

         END IF;

      END LOOP;

   END;

   RETURN;

END calcolaCoriAltro;

-- =================================================================================================
-- Valorizza i dati per contributi ritenuta definiti per territorio. Funzione comune a tutti i calcoli
-- di contributo_ritenuta
-- =================================================================================================
PROCEDURE getDatiTerritorio
   (
    aCdRegione IN OUT COMPENSO.cd_regione_add%TYPE,
    aCdProvincia IN OUT COMPENSO.cd_provincia_add%TYPE,
    aPgComune IN OUT COMPENSO.pg_comune_add%TYPE,
    indice BINARY_INTEGER
   ) IS

BEGIN

   -- Default variabili

   aCdRegione:='*';
   aCdProvincia:='*';
   aPgComune:=0;

   BEGIN

      -- Valorizzazione dei dati di territorio per i contributi ritenuta per i quali questa
      -- gestione ? rilevante
      IF tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriIrap Then
	 -- per gestire il caso in cui il terzo ha compensi con IRAP gi? pagati ed il nuovo
	 -- compenso non ha l'IRAP (vedi cervelli)
	 If aRecCompenso.cd_regione_irap Is Not Null Then
            aCdRegione:=aRecCompenso.cd_regione_irap;
         Else
            -- prendo la regione IRAP dalla UO
            aCdRegione:=cnrctb610.getCdRegioneIrap(aRecCompenso.cd_unita_organizzativa);
         End If;
      END IF;

      IF tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriAddReg THEN
         aCdRegione:=aRecCompenso.cd_regione_add;
      END IF;

      IF tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriAddPro THEN
         aCdProvincia:=aRecCompenso.cd_provincia_add;
      END IF;

      IF tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriAddCom THEN
         aPgComune:=aRecCompenso.pg_comune_add;
      END IF;

   END;

END getDatiTerritorio;

-- =================================================================================================
-- Calcolo imponibile per ogni singolo cori del trattamento
-- =================================================================================================
FUNCTION getImponibileCori
   (
    indice BINARY_INTEGER,
    aImponibile NUMBER
   ) RETURN NUMBER IS
   j BINARY_INTEGER;
   aImponibileBaseCori NUMBER(15,2);
   aImponibileRealeCori NUMBER(15,2);
   operatore NUMBER(15,2);
   aBloccoCalcolo VARCHAR2(5);

BEGIN

   aImponibileBaseCori:=0;
   aImponibileRealeCori:=0;

   BEGIN

      ----------------------------------------------------------------------------------------------
      -- Determino l'imponibile di base

      aImponibileBaseCori:=aImponibile;

      -- Se la regola di calcolo indica un algoritmo eseguo lo stesso

      IF tabella_cori(indice).tCalcoloImponibile = '000' THEN

         aImponibileRealeCori:=aImponibileBaseCori;

      ELSE

         aImponibileRealeCori:=aImponibileBaseCori;

         FOR j IN 1 .. (LENGTH(tabella_cori(indice).tCalcoloImponibile) / 5)

         LOOP

            aBloccoCalcolo:=SUBSTR(tabella_cori(indice).tCalcoloImponibile, ((j * 5) - 5 + 1), 5);
            IF SUBSTR(aBloccoCalcolo,1,1) = '+' THEN
               operatore:=1;
            ELSE
               operatore:=-1;
            END IF;

            IF    SUBSTR(aBloccoCalcolo,5,1) = 'P' THEN
                  aImponibileRealeCori:=
                      (
                         aImponibileRealeCori +
                         (NVL(tabella_cori(TO_NUMBER(SUBSTR(aBloccoCalcolo,2,3))).tAmmontarePercip,0) * operatore)
                      );
            ELSIF SUBSTR(aBloccoCalcolo,5,1) = 'E' THEN
                  aImponibileRealeCori:=
                     (
                         aImponibileRealeCori +
                         (NVL(tabella_cori(TO_NUMBER(SUBSTR(aBloccoCalcolo,2,3))).tAmmontareEnte,0) * operatore)
                     );
            ELSIF SUBSTR(aBloccoCalcolo,5,1) = '%' Then
                  aImponibileRealeCori:=
                     (
                         aImponibileRealeCori +
                         (aImponibileRealeCori * (NVL(To_Number(SUBSTR(aBloccoCalcolo,2,3)),0)) / 100 * operatore)
                     );
            ELSE  aImponibileRealeCori:=
                     (
                         aImponibileRealeCori +
                         (NVL(tabella_cori(TO_NUMBER(SUBSTR(aBloccoCalcolo,2,3))).tAmmontarePercip,0) * operatore) +
                         (NVL(tabella_cori(TO_NUMBER(SUBSTR(aBloccoCalcolo,2,3))).tAmmontareEnte,0) * operatore)
                     );

            END IF;

         END LOOP;

      END IF;

   END;

   -- Eseguo la normalizzazone dell'imponibile in base alla precisione indicata nel trattamento.
   -- Prendo in considerazione solo la precisione pari a 1.

   IF (tabella_cori(indice).tPrecisione IS NOT NULL AND
       tabella_cori(indice).tPrecisione = 1) THEN
      aImponibileRealeCori:=ROUND(aImponibileRealeCori);
   END IF;

   RETURN aImponibileRealeCori;

END getImponibileCori;


-- =================================================================================================
-- Valorizzazione della matrice per il calcolo dei dettagli CORI di un compenso (dettaglio)
-- =================================================================================================
PROCEDURE costruisciTabellaCoriDet
   (
    indice BINARY_INTEGER,
    aImponibile CONTRIBUTO_RITENUTA_DET.imponibile%TYPE,
    aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE
   ) IS
   k BINARY_INTEGER;

BEGIN

   k:=tabella_cori_det.COUNT + 1;
   tabella_cori_det(k).tCdCori:=tabella_cori(indice).tCdCori;
   tabella_cori_det(k).tTiCassaCompetenza:=tabella_cori(indice).tTiCassaCompetenza;
   tabella_cori_det(k).tPrecisione:=tabella_cori(indice).tPrecisione;
   tabella_cori_det(k).tPgClassificazioneMontanti:=tabella_cori(indice).tPgClassificazioneMontanti;
   tabella_cori_det(k).tCdClassificazioneCori:=tabella_cori(indice).tCdClassificazioneCori;
   tabella_cori_det(k).tFlScriviMontanti:=tabella_cori(indice).tFlScriviMontanti;
   tabella_cori_det(k).tIdRiga:=tabella_cori(indice).tIdRiga;
   tabella_cori_det(k).tSegno:=tabella_cori(indice).tSegno;
   tabella_cori_det(k).tCalcoloImponibile:=tabella_cori(indice).tCalcoloImponibile;
   tabella_cori_det(k).tImponibileNetto:=NULL;
   tabella_cori_det(k).tAliquotaEnte:=NULL;
   tabella_cori_det(k).tBaseCalcoloEnte:=NULL;
   tabella_cori_det(k).tAmmontareEnte:=NULL;
   tabella_cori_det(k).tAliquotaPercip:=NULL;
   tabella_cori_det(k).tBaseCalcoloPercip:=NULL;
   tabella_cori_det(k).tAmmontarePercip:=NULL;

   tabella_cori_det(k).tImponibileNetto:=aImponibile;

   IF (aRecVPreScaglione.ti_ente_percipiente = 'P' OR
       aRecVPreScaglione.ti_ente_percipiente = '*') THEN
      tabella_cori_det(k).tAliquotaPercip:=aRecVPreScaglione.aliquota_percip;
      tabella_cori_det(k).tBaseCalcoloPercip:=aRecVPreScaglione.base_calcolo_percip;
      tabella_cori_det(k).tAmmontarePercip:=ROUND(aImponibile * (aRecVPreScaglione.base_calcolo_percip / 100) *
                                                  (aRecVPreScaglione.aliquota_percip / 100),2);
      tabella_cori(indice).tAliquotaPercip:=0;
      tabella_cori(indice).tBaseCalcoloPercip:=0;
      tabella_cori(indice).tAmmontarePercipLordo:=NVL(tabella_cori(indice).tAmmontarePercipLordo,0) +
                                                  tabella_cori_det(k).tAmmontarePercip;
      tabella_cori(indice).tAmmontarePercip:=tabella_cori(indice).tAmmontarePercipLordo;
   END IF;

   IF (aRecVPreScaglione.ti_ente_percipiente = 'E' OR
       aRecVPreScaglione.ti_ente_percipiente = '*') THEN
      tabella_cori_det(k).tAliquotaEnte:=aRecVPreScaglione.aliquota_ente;
      tabella_cori_det(k).tBaseCalcoloEnte:=aRecVPreScaglione.base_calcolo_ente;
      tabella_cori_det(k).tAmmontareEnte:=ROUND(aImponibile * (aRecVPreScaglione.base_calcolo_ente / 100) *
                                                (aRecVPreScaglione.aliquota_ente / 100),2);
      tabella_cori(indice).tAliquotaEnte:=0;
      tabella_cori(indice).tBaseCalcoloEnte:=0;
      tabella_cori(indice).tAmmontareEnteLordo:=NVL(tabella_cori(indice).tAmmontareEnteLordo,0) +
                                                tabella_cori_det(k).tAmmontareEnte;
      tabella_cori(indice).tAmmontareEnte:=tabella_cori(indice).tAmmontareEnteLordo;
   END IF;

END costruisciTabellaCoriDet;

END;


