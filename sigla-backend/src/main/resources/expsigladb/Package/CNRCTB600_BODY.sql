--------------------------------------------------------
--  DDL for Package Body CNRCTB600
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB600" AS

-- ==================================================================================================
-- Ritorna un record della tabella MINICARRIERA
-- ==================================================================================================
FUNCTION getMinicarriera
   (
    aCdCds MINICARRIERA.cd_cds%TYPE,
    aCdUo MINICARRIERA.cd_unita_organizzativa%TYPE,
    aEsercizio MINICARRIERA.esercizio%TYPE,
    aPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE,
    eseguiLock CHAR
   ) RETURN MINICARRIERA%ROWTYPE IS
   aRecMinicarriera MINICARRIERA%ROWTYPE;

BEGIN

    IF eseguiLock = 'Y' THEN

       SELECT * INTO aRecMinicarriera
       FROM   MINICARRIERA
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUo AND
              esercizio = aEsercizio AND
              pg_minicarriera = aPgMinicarriera
       FOR UPDATE NOWAIT;

   ELSE

       SELECT * INTO aRecMinicarriera
       FROM   MINICARRIERA
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUo AND
              esercizio = aEsercizio AND
              pg_minicarriera = aPgMinicarriera;

   END IF;

   RETURN aRecMinicarriera;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Minicarriera U.O. ' || aCdUo ||
                   ' numero ' || aEsercizio || '/' || aPgMinicarriera ||
                   ' non trovata');

END getMinicarriera;


-- ==================================================================================================
-- Ritorna un record della tabella COMPENSO
-- ==================================================================================================
FUNCTION chkRateMensili
   (
    aDataInizio DATE,
    aDataFine DATE
   ) RETURN NUMBER IS

BEGIN

   -- Per essere mensile le due date devono essere di inizio e fine mese o
   -- avere lo stesso giorno di riferimento

   RETURN 0;

END chkRateMensili;

-- ==================================================================================================
-- Inserimento in MINICARRIERA
-- ==================================================================================================
PROCEDURE insMinicarriera
   (
    aRecMinicariera MINICARRIERA%ROWTYPE
   )IS

BEGIN

   INSERT INTO MINICARRIERA
          (cd_cds,
           esercizio,
           cd_unita_organizzativa,
           pg_minicarriera,
           dt_registrazione,
           ds_minicarriera,
           ti_anagrafico,
           cd_terzo,
           ragione_sociale,
           nome,
           cognome,
           codice_fiscale,
           partita_iva,
           cd_termini_pag,
           cd_modalita_pag,
           pg_banca,
           cd_tipo_rapporto,
           cd_trattamento,
           im_totale_minicarriera,
           numero_rate,
           ti_anticipo_posticipo,
           mesi_anticipo_posticipo,
           dt_inizio_minicarriera,
           dt_fine_minicarriera,
           stato_ass_compenso,
           stato,
           dt_sospensione,
           dt_ripristino,
           dt_rinnovo,
           dt_cessazione,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec,
           cd_cds_minicarriera_ori,
           esercizio_minicarriera_ori,
           cd_uo_minicarriera_ori,
           pg_minicarriera_ori,
           ti_istituz_commerc,
           fl_tassazione_separata,
           imponibile_irpef_eseprec2,
           imponibile_irpef_eseprec1,
           aliquota_irpef_media,
           fl_escludi_qvaria_deduzione,
           ti_prestazione)
   VALUES (aRecMinicariera.cd_cds,
           aRecMinicariera.esercizio,
           aRecMinicariera.cd_unita_organizzativa,
           aRecMinicariera.pg_minicarriera,
           aRecMinicariera.dt_registrazione,
           aRecMinicariera.ds_minicarriera,
           aRecMinicariera.ti_anagrafico,
           aRecMinicariera.cd_terzo,
           aRecMinicariera.ragione_sociale,
           aRecMinicariera.nome,
           aRecMinicariera.cognome,
           aRecMinicariera.codice_fiscale,
           aRecMinicariera.partita_iva,
           aRecMinicariera.cd_termini_pag,
           aRecMinicariera.cd_modalita_pag,
           aRecMinicariera.pg_banca,
           aRecMinicariera.cd_tipo_rapporto,
           aRecMinicariera.cd_trattamento,
           aRecMinicariera.im_totale_minicarriera,
           aRecMinicariera.numero_rate,
           aRecMinicariera.ti_anticipo_posticipo,
           aRecMinicariera.mesi_anticipo_posticipo,
           aRecMinicariera.dt_inizio_minicarriera,
           aRecMinicariera.dt_fine_minicarriera,
           aRecMinicariera.stato_ass_compenso,
           aRecMinicariera.stato,
           aRecMinicariera.dt_sospensione,
           aRecMinicariera.dt_ripristino,
           aRecMinicariera.dt_rinnovo,
           aRecMinicariera.dt_cessazione,
           aRecMinicariera.dacr,
           aRecMinicariera.utcr,
           aRecMinicariera.duva,
           aRecMinicariera.utuv,
           aRecMinicariera.pg_ver_rec,
           aRecMinicariera.cd_cds_minicarriera_ori,
           aRecMinicariera.esercizio_minicarriera_ori,
           aRecMinicariera.cd_uo_minicarriera_ori,
           aRecMinicariera.pg_minicarriera_ori,
           aRecMinicariera.ti_istituz_commerc,
           aRecMinicariera.fl_tassazione_separata,
           aRecMinicariera.imponibile_irpef_eseprec2,
           aRecMinicariera.imponibile_irpef_eseprec1,
           aRecMinicariera.aliquota_irpef_media,
           aRecMinicariera.fl_escludi_qvaria_deduzione,
           aRecMinicariera.ti_prestazione);

END insMinicarriera;

-- ==================================================================================================
-- Inserimento righe in MINICARRIERA_RATA
-- ==================================================================================================
PROCEDURE insMinicarrieraRate
   (
    aRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE
   )IS

BEGIN

   INSERT INTO MINICARRIERA_RATA
          (cd_cds,
           esercizio,
           cd_unita_organizzativa,
           pg_minicarriera,
           pg_rata,
           dt_inizio_rata,
           dt_fine_rata,
           dt_scadenza,
           im_rata,
           stato_ass_compenso,
           cd_cds_compenso,
           esercizio_compenso,
           cd_uo_compenso,
           pg_compenso,
           utcr,
           dacr,
           utuv,
           duva,
           pg_ver_rec)
   VALUES (aRecMCarrieraRata.cd_cds,
           aRecMCarrieraRata.esercizio,
           aRecMCarrieraRata.cd_unita_organizzativa,
           aRecMCarrieraRata.pg_minicarriera,
           aRecMCarrieraRata.pg_rata,
           aRecMCarrieraRata.dt_inizio_rata,
           aRecMCarrieraRata.dt_fine_rata,
           aRecMCarrieraRata.dt_scadenza,
           aRecMCarrieraRata.im_rata,
           aRecMCarrieraRata.stato_ass_compenso,
           aRecMCarrieraRata.cd_cds_compenso,
           aRecMCarrieraRata.esercizio_compenso,
           aRecMCarrieraRata.cd_uo_compenso,
           aRecMCarrieraRata.pg_compenso,
           aRecMCarrieraRata.utcr,
           aRecMCarrieraRata.dacr,
           aRecMCarrieraRata.utuv,
           aRecMCarrieraRata.duva,
           aRecMCarrieraRata.pg_ver_rec);

END insMinicarrieraRate;

-- ==================================================================================================
-- Cancellazione della testata di una minicarriera
-- ==================================================================================================
PROCEDURE delMinicarriera
   (
    aCdCds MINICARRIERA.cd_cds%TYPE,
    aCdUnitaOrganizzativa MINICARRIERA.cd_unita_organizzativa%TYPE,
    aEsercizio MINICARRIERA.esercizio%TYPE,
    aPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE
   )IS

BEGIN

   DELETE FROM MINICARRIERA
   WHERE  cd_cds = aCdCds AND
          cd_unita_organizzativa = aCdUnitaOrganizzativa AND
          esercizio = aEsercizio AND
          pg_minicarriera = aPgMinicarriera;

END delMinicarriera;

-- ==================================================================================================
-- Cancellazione di tutte le rate di una minicarriera
-- ==================================================================================================
PROCEDURE delMinicarrieraRate
   (
    aCdCds MINICARRIERA_RATA.cd_cds%TYPE,
    aCdUnitaOrganizzativa MINICARRIERA_RATA.cd_unita_organizzativa%TYPE,
    aEsercizio MINICARRIERA_RATA.esercizio%TYPE,
    aPgMinicarriera MINICARRIERA_RATA.pg_minicarriera%TYPE
   )IS

BEGIN

   DELETE FROM MINICARRIERA_RATA
   WHERE  cd_cds = aCdCds AND
          cd_unita_organizzativa = aCdUnitaOrganizzativa AND
          esercizio = aEsercizio AND
          pg_minicarriera = aPgMinicarriera;

END delMinicarrieraRate;

-- ==================================================================================================
-- Verifica se le rate sono mensili per minicarriere non gestite a tassazione separata
-- ==================================================================================================
FUNCTION chkRateMensiliNormale
   (
    aRecMinicarriera MINICARRIERA%ROWTYPE
   ) RETURN CHAR IS
   aDataRifDa DATE;
   aDataRifA DATE;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   aDataRifDa:=aRecMinicarriera.dt_inizio_minicarriera;
   aDataRifA:=aRecMinicarriera.dt_fine_minicarriera;

   --------------------------------------------------------------------------------------------------
   -- Determino la tipologia della rata
   -- Si considerano mensili isRataMensile:='Y' quando:
   -- 1) Le date di inizio e fine di una minicarriera coincidono con un inizio e fine mese
   -- 2) Le rate di inizio e fine di una minicarriera hanno, indipendentemente da mese e anno, la
   --    distanza di un giorno
   -- 3) In ogni caso il numero delle rate indicate coincide con quelle calcolate

   BEGIN

      -- Le date rappresentano un intervallo mensile

      IF IBMUTL001.isDifferenzaMensile(aDataRifDa, aDataRifA) = IBMUTL001.isIntervalloDateMensile THEN
         IF IBMUTL001.getMonthsBetween(aDataRifDa, aDataRifA) = aRecMinicarriera.numero_rate THEN
            RETURN IBMUTL001.isIntervalloDateMensile;
         END IF;
      END IF;

   END;

   IF IBMUTL001.getDaysBetween((aDataRifDa + 1), aDataRifDa) > aRecMinicarriera.numero_rate THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Il numero delle rate indicate per la minicarriera non trova capienza nei giorni residui della stessa');
   END IF;

   RETURN IBMUTL001.isNotIntervalloDateMensile;

END chkRateMensiliNormale;


-- ==================================================================================================
-- Verifica se le rate sono mensili per minicarriere gestite a tassazione separata
-- ==================================================================================================
FUNCTION chkRateMensiliTassazSep
   (
    aRecMinicarriera MINICARRIERA%ROWTYPE
   ) RETURN CHAR IS
   i BINARY_INTEGER;
   isMese CHAR(1);
   isMensile CHAR(1);
   aDataRif DATE;
   aDataScadPrimaRata DATE;
   aDataScadSecondaRata DATE;
   aNumeroRata INTEGER;

BEGIN

   isMese:=NULL;
   isMensile:=NULL;
   aDataRif:=NULL;
   aDataScadPrimaRata:=NULL;
   aDataScadSecondaRata:=NULL;
   aNumeroRata:=0;

   --------------------------------------------------------------------------------------------------
   -- Determino la tipologia della rata
   -- Si considerano mensili isRataMensile:='Y' quando:
   -- 1) Le date di inizio e fine di una minicarriera coincidono con un inizio e fine mese
   -- 2) Le rate di inizio e fine di una minicarriera hanno, indipendentemente da mese e anno, la
   --    distanza di un giorno
   -- 3) In ogni caso il numero delle rate indicate coincide con quelle calcolate
   -- Per la tassazione separata il numero delle rate è calcolato in base alle seguenti regole:
   -- Intervallo date mese mensile
   -- 1) NO anticipo posticipo:
   --       La prima rata va dalla data di inizio della minicarriera al 31/12/XXXX. Se la data di fine della
   --       minicarriera è < della data di inizio esercizio di registrazione esiste solo la prima rata. In
   --       questo caso la data di fine della prima rata è pari alla data di fine della minicarriera stessa.
   --       La seconda rata va da 01/01/XXXX a 31/01/XXXX se l'intervallo rappresenta mesi interi
   --       altrimenti         da 01/01/XXXX a giorno precedente inizio minicarriera del mese di febbraio
   --       Le altre rate, se presenti, sono calcolate in modo ordinario.
   -- 2) SI anticipo:
   --       La prima rata va dalla data di inizio della minicarriera al 31/12/XXXX. Se la data di fine della
   --       minicarriera è < della data di inizio esercizio di registrazione esiste solo la prima rata. In
   --       questo caso la data di fine della prima rata è pari alla data di fine della minicarriera stessa.
   --       La seconda rata va da 01/01/XXXX a 31/01/XXXX + mesi anticipo se l'intervallo rappresenta mesi interi
   --       altrimenti         da 01/01/XXXX a giorno precedente inizio minicarriera del mese di febbraio +
   --                          mesi anticipo
   --       Le altre rate sono calcolate in modo ordinario.

   BEGIN

      -----------------------------------------------------------------------------------------------
      -- Controllo date

      -- Classificazione della tipologia di intervallo date della minicarriera

      IF    IBMUTL001.isDifferenzaMese(aRecMinicarriera.dt_inizio_minicarriera,
                                       aRecMinicarriera.dt_fine_minicarriera) = IBMUTL001.isIntervalloDateMese THEN
            isMese:='Y';
            isMensile:='N';
      ELSIF IBMUTL001.isDifferenzaMensile(aRecMinicarriera.dt_inizio_minicarriera,
                                          aRecMinicarriera.dt_fine_minicarriera) = IBMUTL001.isIntervalloDateMensile THEN
            isMese:='N';
            isMensile:='Y';
      ELSE
            isMese:='N';
            isMensile:='N';
      END IF;

      -- Loop controllo date

      FOR i IN 1 .. 2

      LOOP

         aDataRif:=getDataScadenzaRata(aRecMinicarriera,
                                       isMese,
                                       isMensile,
                                       i,
                                       aDataScadPrimaRata);


         IF i = 1 THEN

            aDataScadPrimaRata:=aDataRif;

            -- Se la data di fine della prima rata corrisponde a quella di fine della minicarriere è prevista
            -- una sola rata

            IF aDataScadPrimaRata = aRecMinicarriera.dt_fine_minicarriera THEN
               IF aRecMinicarriera.numero_rate = 1 THEN
                  RETURN IBMUTL001.isIntervalloDateMensile;
               ELSE
                  IBMERR001.RAISE_ERR_GENERICO
                     ('La minicarriera in calcolo prevede solo 1 rata ma ne è stato indicato un numero diverso ' ||
                      aRecMinicarriera.numero_rate);
               END IF;
            END IF;

            -- Se la data di fine della prima rata è < della data di inizio della minicarriera si solleva errore

            IF aDataScadPrimaRata < aRecMinicarriera.dt_inizio_minicarriera THEN
               IBMERR001.RAISE_ERR_GENERICO
                  ('La data di inizio della minicarriera risulta maggiore della data di fine ' ||
                   'calcolata della prima rata nell''esercizio passato');
            END IF;

            -- Se la data di fine della prima rata è < della data di fine della minicarriera è stato
            -- indicato dall'utente una sola rata si solleva un errore

            IF (aDataScadPrimaRata < aRecMinicarriera.dt_fine_minicarriera AND
                aRecMinicarriera.numero_rate = 1) THEN
               IBMERR001.RAISE_ERR_GENERICO
                  ('E'' stato indicato un numero di rate pari ad 1 per una minicarriera che deve generare più rate');
            END IF;

         ELSE

            aDataScadSecondaRata:=aDataRif;

            -- Se la data di fine della prima rata corrisponde a quella di fine della minicarriere è prevista
            -- una sola rata

            IF aDataScadSecondaRata = aRecMinicarriera.dt_fine_minicarriera THEN
                IF aRecMinicarriera.numero_rate = 2 THEN
                   RETURN IBMUTL001.isIntervalloDateMensile;
                ELSE
                   IBMERR001.RAISE_ERR_GENERICO
                      ('La minicarriera in calcolo prevede solo 2 rate ma ne è stato indicato un numero diverso ' ||
                       aRecMinicarriera.numero_rate);
                END IF;
            END IF;

         END IF;

      END LOOP;

      -- Controllo uguaglianza con il numero delle rate

      IF (isMese = 'Y' OR
          isMensile = 'Y') THEN
         IF aRecMinicarriera.numero_rate = (IBMUTL001.getMonthsBetween
                                               (
                                                  (aDataScadSecondaRata + 1),
                                                   aRecMinicarriera.dt_fine_minicarriera
                                               ) + 2
                                           ) THEN
            RETURN IBMUTL001.isIntervalloDateMensile;
         END IF;
      END IF;

      -- Controllo che il numero di giorni trovi capienza nelle rate

      IF aRecMinicarriera.numero_rate > IBMUTL001.getDaysBetween((aDataScadSecondaRata + 1),
                                                                 aRecMinicarriera.dt_fine_minicarriera) THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Il numero delle rate indicate per la minicarriera non trova capienza nei giorni residui della stessa');
      END IF;

   END;

   RETURN IBMUTL001.isNotIntervalloDateMensile;

END chkRateMensiliTassazSep;

--==================================================================================================
-- Ritorna la data di scadenza di una rata (prima o seconda) per i controlli sulle minicarriere a
-- tassazione separata
--==================================================================================================
FUNCTION getDataScadenzaRata
   (
    aRecMinicarriera MINICARRIERA%ROWTYPE,
    isRataMeseIntero CHAR,
    isRataMensile CHAR,
    aRataCalcolo INTEGER,
    aDataScadPrimaRata DATE
   ) RETURN DATE IS
   dataOdierna DATE;
   aDataInizioAnno DATE;
   aGGDataRifDa NUMBER;
   aGGRif NUMBER;
   aDataControllo DATE;
   aNumeroGG NUMBER;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   dataOdierna:=TRUNC(sysdate);
   aDataInizioAnno:=TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');
   aGGDataRifDa:=IBMUTL001.getDayOfDate(aRecMinicarriera.dt_inizio_minicarriera);

   BEGIN

      -----------------------------------------------------------------------------------------------
      -- Determinazione della data di scadenza della rata

      -----------------------------------------------------------------------------------------------
      -- Prima rata in scadenza

      IF aRataCalcolo = 1 THEN

         -- Se no anticipo/positicipo o solo anticipo la data di fine della prima rata è sempre pari al
         -- 31/12 o alla data di scadenza della minicarriera se questa è tutta di competenza di esercizi passati

         IF    (aRecMinicarriera.ti_anticipo_posticipo = 'N' OR
                aRecMinicarriera.ti_anticipo_posticipo = 'A') THEN
               IF aRecMinicarriera.dt_fine_minicarriera < (aDataInizioAnno - 1) THEN
                  aDataControllo:=aRecMinicarriera.dt_fine_minicarriera;
               ELSE
                  aDataControllo:=(aDataInizioAnno - 1);
               END IF;

         -- Se posticipo la data di fine della prima rata è calcolata per differenza tra la data di sistema
         -- della registrazione della minicarriera e i mesi di posticipo. Se il periodo di competenza della
         -- minicarriera indica un intervallo di mesi interi ritorna un fine mese altrimenti è pari al GG -1
         -- della data inizio. In ogni caso se questa è > del 31/12 si torna 31/12.
         -- Se la data di fine così calcolata è maggiore della data di fine della minicarriera si torna la
         -- data di fine della stessa

         ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'P' THEN
               IF isRataMeseIntero = 'Y' THEN
                  aDataControllo:=IBMUTL001.getLastDayOfMonth
                                     (
                                        IBMUTL001.getAddMonth(dataOdierna, (aRecMinicarriera.mesi_anticipo_posticipo * -1))
                                     );
               ELSE
                  aDataControllo:=IBMUTL001.getFirstDayOfMonth
                                     (
                                        IBMUTL001.getAddMonth(dataOdierna, (aRecMinicarriera.mesi_anticipo_posticipo * -1))
                                     );
                  aGGRif:=IBMUTL001.getDayLastDayOfMonth(aDataControllo);
                  IF aGGDataRifDa > aGGRif THEN
                     aDataControllo:=(aDataControllo + (aGGRif - 1));
                  ELSE
                     aDataControllo:=(aDataControllo + (aGGDataRifDa - 2));
                  END IF;
               END IF;

               -- Normalizzazione alla fine esercizio

               IF aDataControllo > (aDataInizioAnno - 1) THEN
                  aDataControllo:=(aDataInizioAnno - 1);
               END IF;

               -- Normalizzazione alla data di fine della minicarriera

               IF aRecMinicarriera.dt_fine_minicarriera < aDataControllo THEN
                  aDataControllo:=aRecMinicarriera.dt_fine_minicarriera;
               END IF;

         END IF;

      -----------------------------------------------------------------------------------------------
      -- Seconda rata in scadenza

      ELSE

         -- L'intervallo di competenza della minicarriera rappresenta un mese intero

         IF    isRataMeseIntero = 'Y' THEN

               -- No anticipo posticipo. La data di fine è la fine mese del mese successivo la prima rata

               IF    aRecMinicarriera.ti_anticipo_posticipo = 'N' THEN
                     aDataControllo:=IBMUTL001.getAddMonth(aDataScadPrimaRata,1);

               -- Anticipo. La data di fine è la data fine della prima rata + mesi anticipo

               ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'A' THEN
                     aDataControllo:=IBMUTL001.getLastDayOfMonth
                                        (
                                            IBMUTL001.getAddMonth(aDataScadPrimaRata, aRecMinicarriera.mesi_anticipo_posticipo)
                                        );

               -- Posticipo. La data di fine è la fine mese del mese successivo la prima rata

               ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'P' THEN
                     aDataControllo:=IBMUTL001.getAddMonth(aDataScadPrimaRata,1);
               END IF;

         -- L'intervallo di competenza della minicarriera rappresenta un intervallo mensile

         ELSIF isRataMensile = 'Y' THEN

               -- No anticipo posticipo. La data di fine è il GG -1 della data di inizio della minicarriera nel
               -- mese + 2 della data di prima scadenza

               IF    aRecMinicarriera.ti_anticipo_posticipo = 'N' THEN
                     aDataControllo:=IBMUTL001.getAddMonth((aDataScadPrimaRata + 1),1);

               -- Anticipo. La data di fine è il GG -1 della data di inizio della minicarriera nel
               -- mese + 2 della data di prima scadenza + mesi anticipo

               ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'A' THEN
                     aDataControllo:=IBMUTL001.getAddMonth
                                        (
                                           (aDataScadPrimaRata + 1),(aRecMinicarriera.mesi_anticipo_posticipo + 1)
                                        );

               -- Posticipo. La data di fine è il GG -1 della data di inizio della minicarriera nel
               -- mese + 1 della data di prima scadenza

               ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'P' THEN
                     aDataControllo:=IBMUTL001.getAddMonth((aDataScadPrimaRata + 1),1);
               END IF;

               -- Normalizzazione giorno di fine della seconda rata

               aGGRif:=IBMUTL001.getDayLastDayOfMonth(aDataControllo);
               IF aGGDataRifDa > aGGRif THEN
                  aDataControllo:=(aDataControllo + (aGGRif - 1));
               ELSE
                  aDataControllo:=(aDataControllo + (aGGDataRifDa - 2));
               END IF;

         -- L'intervallo di competenza della minicarriera non rappresenta un intervallo mensile

         ELSE

            -- Calcolo del numero di giorni complessivo della minicarriera a partire dalla fine della prima rata

            aNumeroGG:=ROUND(
                              (
                                (
                                   IBMUTL001.getDaysBetween((aDataScadPrimaRata + 1), aRecMinicarriera.dt_fine_minicarriera)
                                ) / (aRecMinicarriera.numero_rate - 1)
                              )
                            );

            -- No anticipo posticipo. Sommo i giorni alla data di scadenza della prima rata

            IF    aRecMinicarriera.ti_anticipo_posticipo = 'N' THEN
                  aDataControllo:=(aDataScadPrimaRata + aNumeroGG);

            -- Anticipo. La data di fine è il GG -1 della data di inizio della minicarriera nel
            -- mese + 2 della data di prima scadenza + mesi anticipo

            ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'A' THEN
                  aDataControllo:=IBMUTL001.getAddMonth
                                        (
                                           (aDataScadPrimaRata + 1),(aRecMinicarriera.mesi_anticipo_posticipo + 1)
                                        );
                  aGGRif:=IBMUTL001.getDayLastDayOfMonth(aDataControllo);
                  IF aGGDataRifDa > aGGRif THEN
                     aDataControllo:=(aDataControllo + (aGGRif - 1));
                  ELSE
                     aDataControllo:=(aDataControllo + (aGGDataRifDa - 2));
                  END IF;

            -- Posticipo. Sommo i giorni alla data di scadenza della prima rata

            ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'P' THEN
                  aDataControllo:=(aDataScadPrimaRata + aNumeroGG);
            END IF;

         END IF;

         -- Normalizzazione alla data di fine della minicarriera

         IF aRecMinicarriera.dt_fine_minicarriera < aDataControllo THEN
            aDataControllo:=aRecMinicarriera.dt_fine_minicarriera;
         END IF;

      END IF;

   END;

   RETURN aDataControllo;

END getDataScadenzaRata;

--==================================================================================================
-- Ritorna il record di MINICARRIERA_RATA se un compenso risulta associato alla stessa
--==================================================================================================
PROCEDURE sganciaAssCompensoMCarriera
   (
    aCdsCompenso COMPENSO.cd_cds%TYPE,
    aUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aPgCompenso COMPENSO.pg_compenso%TYPE
   ) IS
   aContaAssociato INTEGER;
   aContaNotAssociato INTEGER;
   aStatoAssociato MINICARRIERA.stato_ass_compenso%TYPE;

   aRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE;
   aRecMinicarriera MINICARRIERA%ROWTYPE;
   gen_cur GenericCurTyp;

BEGIN

   aRecMCarrieraRata:=NULL;
   aRecMinicarriera:=NULL;
   aContaAssociato:=0;
   aContaNotAssociato:=0;
   aStatoAssociato:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Lettura della testata della minicarriera associata al compenso in cancellazione

   BEGIN

      SELECT DISTINCT B.* INTO aRecMinicarriera
      FROM   MINICARRIERA_RATA A,
             MINICARRIERA B
      WHERE  A.cd_cds_compenso = aCdsCompenso AND
             A.cd_uo_compenso = aUoCompenso AND
             A.esercizio_compenso = aEsercizio AND
             A.pg_compenso = aPgCompenso AND
             B.cd_cds = A.cd_cds AND
             B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
             B.esercizio = A.esercizio AND
             B.pg_minicarriera = A.pg_minicarriera;

      -- Impedisco la cancellazione se lo stato della minicarriera è sospesa o cessata

      IF (aRecMinicarriera.stato = 'S' OR
          aRecMinicarriera.stato = 'C') THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Impossibile sganciare un compenso da una minicarriera in stato S (sospeso) o C (cessato)');
      END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Eliminazione del riferimento al compenso sulla rata della minicarriera

   BEGIN

      -- Sgancio del compenso dalle rate di una minicarriera

      OPEN gen_cur FOR

           SELECT *
           FROM   MINICARRIERA_RATA
           WHERE  cd_cds_compenso = aCdsCompenso AND
                  cd_uo_compenso = aUoCompenso AND
                  esercizio_compenso = aEsercizio AND
                  pg_compenso = aPgCompenso;

      LOOP

         FETCH gen_cur INTO
               aRecMCarrieraRata;

         EXIT WHEN gen_cur%NOTFOUND;

         UPDATE MINICARRIERA_RATA
         SET    cd_cds_compenso = NULL,
                cd_uo_compenso = NULL,
                esercizio_compenso = NULL,
                pg_compenso = NULL,
                stato_ass_compenso = 'N'
         WHERE  cd_cds = aRecMCarrieraRata.cd_cds AND
                cd_unita_organizzativa = aRecMCarrieraRata.cd_unita_organizzativa AND
                esercizio = aRecMCarrieraRata.esercizio AND
                pg_minicarriera = aRecMCarrieraRata.pg_minicarriera AND
                pg_rata = aRecMCarrieraRata.pg_rata;

      END LOOP;

      CLOSE gen_cur;

   END;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dello stato sulla testata della minicarriera

   BEGIN

      OPEN gen_cur FOR

           SELECT *
           FROM   MINICARRIERA_RATA
           WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                  cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                  esercizio = aRecMinicarriera.esercizio AND
                  pg_minicarriera = aRecMinicarriera.pg_minicarriera;
      LOOP

         FETCH gen_cur INTO
               aRecMCarrieraRata;

         EXIT WHEN gen_cur%NOTFOUND;

         IF aRecMCarrieraRata.stato_ass_compenso = 'N' THEN
            aContaNotAssociato:=aContaNotAssociato + 1;
         ELSE
            aContaAssociato:=aContaAssociato + 1;
         END IF;

      END LOOP;

      CLOSE gen_cur;

      -- Aggiornamento della testata della MINICARRIERA

      IF aContaAssociato = 0 THEN
         aStatoAssociato:='N';
      ELSE
         aStatoAssociato:='P';
      END IF;

      UPDATE MINICARRIERA
      SET    stato_ass_compenso = aStatoAssociato
      WHERE  cd_cds = aRecMinicarriera.cd_cds AND
             cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
             esercizio = aRecMinicarriera.esercizio AND
             pg_minicarriera = aRecMinicarriera.pg_minicarriera;

   END;

   RETURN;

END sganciaAssCompensoMCarriera;

--==================================================================================================
-- Torna il valore complessivo delle rate di minicarriere attive definite per un dato terzo e con
-- scadenza in un dato esercizio indipendentemente dal fatto che queste risultano liquidate
--==================================================================================================
FUNCTION getImRateDiTerzoInEsercizio
   (
    aRecAnagrafico ANAGRAFICO%ROWTYPE,
    aRecCompenso COMPENSO%ROWTYPE,
    aCdCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    aCdUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    aEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    aPgMcarriera MINICARRIERA.pg_minicarriera%TYPE
   ) RETURN NUMBER IS
   aDataInizio DATE;
   aDataFine DATE;
   aCdTerzo TERZO.cd_terzo%TYPE;
   aImporto NUMBER(15,2);
   aImponibileFiscale NUMBER(15,2);
   eseguiLock CHAR(1);
   flTrovato CHAR(1);

   matriceImTrattamento_tab imTrattamentoTab;
   i BINARY_INTEGER;

   aRecMinicarriera MINICARRIERA%ROWTYPE;

   gen_cur_a GenericCurTyp;
   gen_cur GenericCurTyp;

BEGIN

   aDataInizio:=TO_DATE('0101' || aRecCompenso.esercizio,'DDMMYYYY');
   aDataFine:=TO_DATE('3112' || aRecCompenso.esercizio,'DDMMYYYY');
   aRecMinicarriera:=NULL;
   eseguiLock:='Y';

   matriceImTrattamento_tab.DELETE;

   -------------------------------------------------------------------------------------------------
   -- Ciclo di recupero delle rate di minicarriera scadenti in un esercizio e riferite ad un dato
   -- soggetto anagrafico

   -- Ciclo primario di recupero dei codici terzo associati ad una dato anagrafico

   BEGIN

      OPEN gen_cur_a FOR

           SELECT cd_terzo
           FROM   TERZO
           WHERE  cd_anag = aRecAnagrafico.cd_anag;

      LOOP

         FETCH gen_cur_a INTO
               aCdTerzo;

         EXIT WHEN gen_cur_a%NOTFOUND;

         -- Ciclo secondario di recupero delle minicarriere riferite ad un dato terzo

         BEGIN

            OPEN gen_cur FOR

                 SELECT cd_cds,
                        cd_unita_organizzativa,
                        esercizio,
                        pg_minicarriera,
                        cd_trattamento,
                        fl_tassazione_separata,
                        stato
                 FROM   MINICARRIERA
                 WHERE  cd_terzo = aCdTerzo AND
                        pg_minicarriera > 0;

            LOOP

               FETCH gen_cur INTO
                     aRecMinicarriera.cd_cds,
                     aRecMinicarriera.cd_unita_organizzativa,
                     aRecMinicarriera.esercizio,
                     aRecMinicarriera.pg_minicarriera,
                     aRecMinicarriera.cd_trattamento,
                     aRecMinicarriera.fl_tassazione_separata,
                     aRecMinicarriera.stato;

               EXIT WHEN gen_cur%NOTFOUND;

               -- Lettura delle rate di minicarriera. In caso di rate a tassazione separata escludo
               -- quelle che, pur scadenti nel periodo in esame, hanno data fine anteriore all'inizio
               -- dell'esercizio della minicarriera

               BEGIN

                  IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN

                     IF aRecMinicarriera.stato = 'A' THEN

                        SELECT NVL(SUM(im_rata),0) INTO aImporto
                        FROM   MINICARRIERA_RATA
                        WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                               cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                               esercizio = aRecMinicarriera.esercizio AND
                               pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                               dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                               dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');

                     ELSE

                        SELECT NVL(SUM(im_rata),0) INTO aImporto
                        FROM   MINICARRIERA_RATA
                        WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                               cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                               esercizio = aRecMinicarriera.esercizio AND
                               pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                               dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                               dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY') AND
                               cd_cds_compenso IS NOT NULL;

                     END IF;

                  ELSE

                     IF aRecMinicarriera.stato = 'A' THEN

                        SELECT NVL(SUM(im_rata),0) INTO aImporto
                        FROM   MINICARRIERA_RATA
                        WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                               cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                               esercizio = aRecMinicarriera.esercizio AND
                               pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                               dt_scadenza BETWEEN aDataInizio AND aDataFine;

                     ELSE

                        SELECT NVL(SUM(im_rata),0) INTO aImporto
                        FROM   MINICARRIERA_RATA
                        WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                               cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                               esercizio = aRecMinicarriera.esercizio AND
                               pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                               dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                               cd_cds_compenso IS NOT NULL;

                     END IF;

                  END IF;

               EXCEPTION

                  WHEN no_data_found THEN
                       aImporto:=0;

               END;

               -- Sommo le rate.
               -- Se la matrice trattamento importo è vuota valorizzo il primo elemento
               -- Se la matrice trattamento importo è piena allora se il codice trattamento è già
               -- stato processato aggiorno l'importo altrimenti creo una nuova entrata

               IF matriceImTrattamento_tab.COUNT = 0 THEN

                  matriceImTrattamento_tab(1).tCdTrattamento:=aRecMinicarriera.cd_trattamento;
                  matriceImTrattamento_tab(1).tImportoLordoIrpef:=aImporto;
                  matriceImTrattamento_tab(1).tImponibileIrpef:=0;

               ELSE

                  flTrovato:='N';

                  FOR i IN matriceImTrattamento_tab.FIRST .. matriceImTrattamento_tab.LAST

                  LOOP

                     IF matriceImTrattamento_tab(i).tCdTrattamento = aRecMinicarriera.cd_trattamento THEN
                        flTrovato:='Y';
                        matriceImTrattamento_tab(i).tImportoLordoIrpef:=matriceImTrattamento_tab(i).tImportoLordoIrpef + aImporto;
                        EXIT;
                     END IF;

                  END LOOP;

                  IF flTrovato = 'N' THEN
                     i:=matriceImTrattamento_tab.COUNT + 1;
                     matriceImTrattamento_tab(i).tCdTrattamento:=aRecMinicarriera.cd_trattamento;
                     matriceImTrattamento_tab(i).tImportoLordoIrpef:=aImporto;
                     matriceImTrattamento_tab(i).tImponibileIrpef:=0;
                  END IF;

               END IF;

            END LOOP;

            CLOSE gen_cur;

         END;

      END LOOP;

      CLOSE gen_cur_a;

   END;

   -------------------------------------------------------------------------------------------------
   -- Controllo eventuale recupero degli importi delle rate di una minicarriera non ancora salvata

   IF (aPgMcarriera IS NOT NULL AND
       aPgMcarriera < 0) THEN

      BEGIN

         aRecMinicarriera:=getMinicarriera(aCdCdsMcarriera,
                                           aCdUoMcarriera,
                                           aEsercizioMcarriera,
                                           aPgMcarriera,
                                           eseguiLock);

         -- Lettura delle rate di minicarriera. In caso di rate a tassazione separata escludo
         -- quelle che, pur scadenti nel periodo in esame, hanno data fine anteriore all'inizio
         -- dell'esercizio della minicarriera

         BEGIN

            IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN

               SELECT NVL(SUM(im_rata),0) INTO aImporto
               FROM   MINICARRIERA_RATA
               WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                      cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                      esercizio = aRecMinicarriera.esercizio AND
                      pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                      dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                      dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');

            ELSE

               SELECT NVL(SUM(im_rata),0) INTO aImporto
               FROM   MINICARRIERA_RATA
               WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                      cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                      esercizio = aRecMinicarriera.esercizio AND
                      pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                      dt_scadenza BETWEEN aDataInizio AND aDataFine;

            END IF;

         EXCEPTION

            WHEN no_data_found THEN
                 aImporto:=0;

         END;

         -- Sommo le rate
         -- Se la matrice trattamento importo è vuota valorizzo il primo elemento
         -- Se la matrice trattamento importo è piena allora se il codice trattamento è già
         -- stato processato aggiorno l'importo altrimenti creo una nuova entrata

         IF matriceImTrattamento_tab.COUNT = 0 THEN

            matriceImTrattamento_tab(1).tCdTrattamento:=aRecMinicarriera.cd_trattamento;
            matriceImTrattamento_tab(1).tImportoLordoIrpef:=aImporto;
            matriceImTrattamento_tab(1).tImponibileIrpef:=0;

         ELSE

            flTrovato:='N';

            FOR i IN matriceImTrattamento_tab.FIRST .. matriceImTrattamento_tab.LAST

            LOOP

               IF matriceImTrattamento_tab(i).tCdTrattamento = aRecMinicarriera.cd_trattamento THEN
                  flTrovato:='Y';
                  matriceImTrattamento_tab(i).tImportoLordoIrpef:=matriceImTrattamento_tab(i).tImportoLordoIrpef + aImporto;
                  EXIT;
               END IF;

            END LOOP;

            IF flTrovato = 'N' THEN
               i:=matriceImTrattamento_tab.COUNT + 1;
               matriceImTrattamento_tab(i).tCdTrattamento:=aRecMinicarriera.cd_trattamento;
               matriceImTrattamento_tab(i).tImportoLordoIrpef:=aImporto;
               matriceImTrattamento_tab(i).tImponibileIrpef:=0;
            END IF;

         END IF;

      END;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Controllo che la matrice trattamento importo non sia vuota

   IF matriceImTrattamento_tab.COUNT = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('E'' stato recuperato un valore delle rate pari a 0. ' ||
          'Probabilmente sono state pagata anticipatamente o in ritardo una o più rate con scadenza ' ||
          'rispettivamente in esercizi successivi o precedenti');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Eseguo il calcolo delle previdenziali per ogni elemento della matrice trattamento importo

   BEGIN

      aImporto:=0;
      aImponibileFiscale:=0;

      FOR i IN matriceImTrattamento_tab.FIRST .. matriceImTrattamento_tab.LAST

      LOOP

         aImporto:=aImporto + matriceImTrattamento_tab(i).tImportoLordoIrpef;

         IF matriceImTrattamento_tab(i).tImportoLordoIrpef > 0 THEN
            CNRCTB551.calcolaImponibileIrpef(aRecAnagrafico,
                                             aRecCompenso,
                                             matriceImTrattamento_tab(i).tCdTrattamento,
                                             matriceImTrattamento_tab(i).tImportoLordoIrpef,
                                             matriceImTrattamento_tab(i).tImponibileIrpef);
         END IF;

         aImponibileFiscale:=aImponibileFiscale + matriceImTrattamento_tab(i).tImponibileIrpef;

      END LOOP;

   END;

   RETURN aImponibileFiscale;

END getImRateDiTerzoInEsercizio;

--==================================================================================================
-- Inserita routine di calcolo del numero complessivo di giorni per le rate di minicarriera per revisione
-- calcolo della deduzione IRPEF
--==================================================================================================
PROCEDURE getNumeroGGRateMcarriera
   (
    aRecAnagrafico ANAGRAFICO%ROWTYPE,
    aRecCompenso COMPENSO%ROWTYPE,
    aCdCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    aCdUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    aEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    aPgMcarriera MINICARRIERA.pg_minicarriera%TYPE,
    aNumeroGGTutti IN OUT INTEGER,
    aNumeroGGProprio IN OUT INTEGER
   ) IS

   aDataInizio DATE;
   aDataFine DATE;
begin   
   aDataInizio:=TO_DATE('0101' || aRecCompenso.esercizio,'DDMMYYYY');
   aDataFine:=TO_DATE('3112' || aRecCompenso.esercizio,'DDMMYYYY');
   getNumeroGGRateMcarriera(aRecAnagrafico,
      aRecCompenso,
      aCdCdsMcarriera,
      aCdUoMcarriera,
      aEsercizioMcarriera,
      aPgMcarriera,
      aDataInizio,
      aDataFine,
      aNumeroGGTutti,
      aNumeroGGProprio);
end;

PROCEDURE getNumeroGGRateMcarriera
   (
    aRecAnagrafico ANAGRAFICO%ROWTYPE,
    aRecCompenso COMPENSO%ROWTYPE,
    aCdCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    aCdUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    aEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    aPgMcarriera MINICARRIERA.pg_minicarriera%TYPE,
    aDataInizio DATE,
    aDataFine DATE,
    aNumeroGGTutti IN OUT INTEGER,
    aNumeroGGProprio IN OUT INTEGER
   ) IS

   aCdTerzo TERZO.cd_terzo%TYPE;
   eseguiLock CHAR(1);
   flTrovato CHAR(1);
   i BINARY_INTEGER;

   tabella_date CNRCTB545.intervalloDateTab;
   tabella_date_ok CNRCTB545.intervalloDateTab;
   tabella_date_proprio CNRCTB545.intervalloDateTab;
   tabella_date_proprio_ok CNRCTB545.intervalloDateTab;

   aRecMinicarriera MINICARRIERA%ROWTYPE;
   aRecMcarrieraRata MINICARRIERA_RATA%ROWTYPE;
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

   gen_cur_t GenericCurTyp;
   gen_cur_m GenericCurTyp;
   gen_cur_r GenericCurTyp;

BEGIN

   aNumeroGGTutti:=0;
   aNumeroGGProprio:=0;
   aRecMinicarriera:=NULL;
   aRecMcarrieraRata:=NULL;
   eseguiLock:='Y';

   tabella_date.DELETE;
   tabella_date_ok.DELETE;
   tabella_date_proprio.DELETE;
   tabella_date_proprio_ok.DELETE;


   -------------------------------------------------------------------------------------------------
   -- Ciclo di recupero delle rate di minicarriera scadenti in un esercizio e riferite ad un dato
   -- soggetto anagrafico

   -- Ciclo primario di recupero dei codici terzo associati ad una dato anagrafico

   BEGIN

      OPEN gen_cur_t FOR

           SELECT cd_terzo
           FROM   TERZO
           WHERE  cd_anag = aRecAnagrafico.cd_anag;

      LOOP

         FETCH gen_cur_t INTO
               aCdTerzo;

         EXIT WHEN gen_cur_t%NOTFOUND;

         -- Ciclo secondario di recupero delle minicarriere riferite ad un dato terzo --------------

         BEGIN

            OPEN gen_cur_m FOR

                 SELECT A.cd_cds,
                        A.cd_unita_organizzativa,
                        A.esercizio,
                        A.pg_minicarriera,
                        A.cd_trattamento,
                        A.fl_tassazione_separata,
                        A.stato
                 FROM   MINICARRIERA A
                 WHERE  A.cd_terzo = aCdTerzo AND
                        A.pg_minicarriera > 0 AND
                        EXISTS
                           (SELECT 1
                            FROM   TIPO_TRATTAMENTO B
                            WHERE  B.cd_trattamento = A.cd_trattamento AND
                                   B.dt_ini_validita <= A.dt_registrazione AND
                                   B.dt_fin_validita >= A.dt_registrazione AND
                                   B.fl_soggetto_conguaglio = 'Y');

            LOOP

               FETCH gen_cur_m INTO
                     aRecMinicarriera.cd_cds,
                     aRecMinicarriera.cd_unita_organizzativa,
                     aRecMinicarriera.esercizio,
                     aRecMinicarriera.pg_minicarriera,
                     aRecMinicarriera.cd_trattamento,
                     aRecMinicarriera.fl_tassazione_separata,
                     aRecMinicarriera.stato;

               EXIT WHEN gen_cur_m%NOTFOUND;

               -- Lettura delle rate di minicarriera. In caso di rate a tassazione separata escludo
               -- quelle che, pur scadenti nel periodo in esame, hanno data fine anteriore all'inizio
               -- dell'esercizio della minicarriera

               BEGIN

                  IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN

                     IF aRecMinicarriera.stato = 'A' THEN

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');

                     ELSE

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY') AND
                                    cd_cds_compenso IS NOT NULL;

                     END IF;

                  ELSE

                     IF aRecMinicarriera.stato = 'A' THEN

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine;

                     ELSE

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    cd_cds_compenso IS NOT NULL;

                     END IF;

                  END IF;

                  LOOP

                     FETCH gen_cur_r INTO
                           aRecMcarrieraRata.dt_inizio_rata,
                           aRecMcarrieraRata.dt_fine_rata;

                     EXIT WHEN gen_cur_r%NOTFOUND;

                     -- Composizione della matrice delle date per il calcolo dei giorni reali di competenza
                     -- delle rate di minicarriera

                     CNRCTB545.componiMatriceDate(tabella_date,
                                                  aRecMcarrieraRata.dt_inizio_rata,
                                                  aRecMcarrieraRata.dt_fine_rata,
                                                  'N',
                                                  'N');

                     IF (aRecMinicarriera.cd_cds = aCdCdsMcarriera AND
                         aRecMinicarriera.cd_unita_organizzativa = aCdUoMcarriera AND
                         aRecMinicarriera.esercizio = aEsercizioMcarriera AND
                         aRecMinicarriera.pg_minicarriera = aPgMcarriera) THEN
                        CNRCTB545.componiMatriceDate(tabella_date_proprio,
                                                     aRecMcarrieraRata.dt_inizio_rata,
                                                     aRecMcarrieraRata.dt_fine_rata,
                                                     'N',
                                                     'N');
                     END IF;

                  END LOOP;

                  CLOSE gen_cur_r;

               END;

            END LOOP;

            CLOSE gen_cur_m;

         END;

      END LOOP;

      CLOSE gen_cur_t;

   END;

   -------------------------------------------------------------------------------------------------
   -- Controllo eventuale recupero degli importi delle rate di una minicarriera non ancora salvata

   IF (aPgMcarriera IS NOT NULL AND
       aPgMcarriera < 0) THEN

      BEGIN

         -- Recupero della minicarriera

         aRecMinicarriera:=getMinicarriera(aCdCdsMcarriera,
                                           aCdUoMcarriera,
                                           aEsercizioMcarriera,
                                           aPgMcarriera,
                                           eseguiLock);

         -- Recupero dati del tipo trattamento indicato nel compenso

         aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento(aRecMinicarriera.cd_trattamento,
                                                           aRecMinicarriera.dt_registrazione);

         -- Lettura delle rate di minicarriera. In caso di rate a tassazione separata escludo
         -- quelle che, pur scadenti nel periodo in esame, hanno data fine anteriore all'inizio
         -- dell'esercizio della minicarriera.
         -- La lettura è eseguita solo se aRecTipoTrattamento.fl_soggetto_conguaglio = 'Y'

         BEGIN

            IF aRecTipoTrattamento.fl_soggetto_conguaglio = 'Y' THEN

               IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN

                  OPEN gen_cur_r FOR

                       SELECT dt_inizio_rata,
                              dt_fine_rata
                       FROM   MINICARRIERA_RATA
                       WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                              cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                              esercizio = aRecMinicarriera.esercizio AND
                              pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                              dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                              dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');
               ELSE

                  OPEN gen_cur_r FOR

                       SELECT dt_inizio_rata,
                              dt_fine_rata
                       FROM   MINICARRIERA_RATA
                       WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                              cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                              esercizio = aRecMinicarriera.esercizio AND
                              pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                              dt_scadenza BETWEEN aDataInizio AND aDataFine;
               END IF;

               LOOP

                  FETCH gen_cur_r INTO
                        aRecMcarrieraRata.dt_inizio_rata,
                        aRecMcarrieraRata.dt_fine_rata;

                  EXIT WHEN gen_cur_r%NOTFOUND;

                  -- Composizione della matrice delle date per il calcolo dei giorni reali di competenza
                  -- delle rate di minicarriera

                  CNRCTB545.componiMatriceDate(tabella_date,
                                               aRecMcarrieraRata.dt_inizio_rata,
                                               aRecMcarrieraRata.dt_fine_rata,
                                               'N',
                                               'N');

                  IF (aRecMinicarriera.cd_cds = aCdCdsMcarriera AND
                      aRecMinicarriera.cd_unita_organizzativa = aCdUoMcarriera AND
                      aRecMinicarriera.esercizio = aEsercizioMcarriera AND
                      aRecMinicarriera.pg_minicarriera = aPgMcarriera) THEN
                     CNRCTB545.componiMatriceDate(tabella_date_proprio,
                                                  aRecMcarrieraRata.dt_inizio_rata,
                                                  aRecMcarrieraRata.dt_fine_rata,
                                                  'N',
                                                  'N');
                  END IF;

               END LOOP;

               CLOSE gen_cur_r;

            END IF;

         END;

      END;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Normalizzazione delle date per eliminare periodi di eventuale sovrapposizione e calcolo dei
   -- giorni

   IF tabella_date.COUNT > 0 THEN
      normalizzaMatriceDate(tabella_date,
                            tabella_date_ok);

      aNumeroGGTutti:=CNRCTB545.getGiorniMatriceDate(tabella_date_ok);
   END IF;

   IF tabella_date_proprio.COUNT > 0 THEN
      normalizzaMatriceDate(tabella_date_proprio,
                            tabella_date_proprio_ok);

      aNumeroGGProprio:=CNRCTB545.getGiorniMatriceDate(tabella_date_proprio_ok);
   END IF;

   -- Eliminato il test in quanto è possibile avere valore 0 nel numero giorni

--   IF (aNumeroGGTutti = 0 OR aNumeroGGProprio = 0) THEN
--      IBMERR001.RAISE_ERR_GENERICO
--         ('E'' stato recuperato un valore dei giorni delle rate (totale o proprio) pari a 0. ' ||
--          'Probabilmente sono state pagata anticipatamente o in ritardo una o più rate con scadenza ' ||
--          'rispettivamente in esercizi successivi o precedenti');
--   END IF;

END getNumeroGGRateMcarriera;
--==================================================================================================
-- Inserita routine di calcolo del numero complessivo di mesi per le rate di minicarriera per revisione
-- calcolo della deduzione FAMILY
--==================================================================================================
PROCEDURE getNumeroMMRateMcarriera
   (
    aRecAnagrafico ANAGRAFICO%ROWTYPE,
    aRecCompenso COMPENSO%ROWTYPE,
    aCdCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    aCdUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    aEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    aPgMcarriera MINICARRIERA.pg_minicarriera%TYPE,
    aNumeroMMTutti IN OUT INTEGER,
    aNumeroMMProprio IN OUT INTEGER,
    aNumeroMMEsercizio IN OUT INTEGER,
    aOrigineCompenso INTEGER
   ) IS

   aDataInizio DATE;
   aDataFine DATE;
   aCdTerzo TERZO.cd_terzo%TYPE;
   eseguiLock CHAR(1);
   flTrovato CHAR(1);
   i BINARY_INTEGER;

   tabella_date CNRCTB545.intervalloDateTab;
   tabella_date_ok CNRCTB545.intervalloDateTab;
   tabella_date_proprio CNRCTB545.intervalloDateTab;
   tabella_date_proprio_ok CNRCTB545.intervalloDateTab;

   aRecMinicarriera MINICARRIERA%ROWTYPE;
   aRecMcarrieraRata MINICARRIERA_RATA%ROWTYPE;
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

   gen_cur_t GenericCurTyp;
   gen_cur_m GenericCurTyp;
   gen_cur_r GenericCurTyp;
   gen_cur_con GenericCurTyp;

   aRecCompensoConguaglioBase V_COMPENSO_CONGUAGLIO_BASE%ROWTYPE;
   aRecCompensoC COMPENSO%ROWTYPE;
BEGIN

   aDataInizio:=TO_DATE('0101' || aRecCompenso.esercizio,'DDMMYYYY');
   aDataFine:=TO_DATE('3112' || aRecCompenso.esercizio,'DDMMYYYY');
   aNumeroMMTutti:=0;
   aNumeroMMProprio:=0;
   aNumeroMMEsercizio:=0;
   aRecMinicarriera:=NULL;
   aRecMcarrieraRata:=NULL;
   eseguiLock:='Y';

   tabella_date.DELETE;
   tabella_date_ok.DELETE;
   tabella_date_proprio.DELETE;
   tabella_date_proprio_ok.DELETE;


   -------------------------------------------------------------------------------------------------
   -- Ciclo di recupero delle rate di minicarriera scadenti in un esercizio e riferite ad un dato
   -- soggetto anagrafico

   -- Ciclo primario di recupero dei codici terzo associati ad una dato anagrafico

   BEGIN

      OPEN gen_cur_t FOR

           SELECT cd_terzo
           FROM   TERZO
           WHERE  cd_anag = aRecAnagrafico.cd_anag;

      LOOP

         FETCH gen_cur_t INTO
               aCdTerzo;

         EXIT WHEN gen_cur_t%NOTFOUND;

         -- Ciclo secondario di recupero delle minicarriere riferite ad un dato terzo --------------

         BEGIN

            OPEN gen_cur_m FOR

                 SELECT A.cd_cds,
                        A.cd_unita_organizzativa,
                        A.esercizio,
                        A.pg_minicarriera,
                        A.cd_trattamento,
                        A.fl_tassazione_separata,
                        A.stato
                 FROM   MINICARRIERA A
                 WHERE  A.cd_terzo = aCdTerzo AND
                        A.pg_minicarriera > 0 AND
                        EXISTS
                           (SELECT 1
                            FROM   TIPO_TRATTAMENTO B
                            WHERE  B.cd_trattamento = A.cd_trattamento AND
                                   B.dt_ini_validita <= A.dt_registrazione AND
                                   B.dt_fin_validita >= A.dt_registrazione AND
                                   B.fl_soggetto_conguaglio = 'Y');

            LOOP

               FETCH gen_cur_m INTO
                     aRecMinicarriera.cd_cds,
                     aRecMinicarriera.cd_unita_organizzativa,
                     aRecMinicarriera.esercizio,
                     aRecMinicarriera.pg_minicarriera,
                     aRecMinicarriera.cd_trattamento,
                     aRecMinicarriera.fl_tassazione_separata,
                     aRecMinicarriera.stato;

               EXIT WHEN gen_cur_m%NOTFOUND;

               -- Lettura delle rate di minicarriera. In caso di rate a tassazione separata escludo
               -- quelle che, pur scadenti nel periodo in esame, hanno data fine anteriore all'inizio
               -- dell'esercizio della minicarriera

               BEGIN

                  IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN

                     IF aRecMinicarriera.stato = 'A' THEN

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');

                     ELSE

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY') AND
                                    cd_cds_compenso IS NOT NULL;

                     END IF;

                  ELSE

                     IF aRecMinicarriera.stato = 'A' THEN

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine;

                     ELSE

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    cd_cds_compenso IS NOT NULL;

                     END IF;

                  END IF;

                  LOOP

                     FETCH gen_cur_r INTO
                           aRecMcarrieraRata.dt_inizio_rata,
                           aRecMcarrieraRata.dt_fine_rata;

                     EXIT WHEN gen_cur_r%NOTFOUND;

                     -- Composizione della matrice delle date per il calcolo dei giorni reali di competenza
                     -- delle rate di minicarriera

                     CNRCTB545.componiMatriceDate(tabella_date,
                                                  aRecMcarrieraRata.dt_inizio_rata,
                                                  aRecMcarrieraRata.dt_fine_rata,
                                                  'N',
                                                  'N');

                     IF (aRecMinicarriera.cd_cds = aCdCdsMcarriera AND
                         aRecMinicarriera.cd_unita_organizzativa = aCdUoMcarriera AND
                         aRecMinicarriera.esercizio = aEsercizioMcarriera AND
                         aRecMinicarriera.pg_minicarriera = aPgMcarriera) THEN
                        CNRCTB545.componiMatriceDate(tabella_date_proprio,
                                                     aRecMcarrieraRata.dt_inizio_rata,
                                                     aRecMcarrieraRata.dt_fine_rata,
                                                     'N',
                                                     'N');
                     END IF;

                  END LOOP;

                  CLOSE gen_cur_r;

               END;

            END LOOP;

            CLOSE gen_cur_m;

         END;

      END LOOP;

      CLOSE gen_cur_t;

   END;

   -------------------------------------------------------------------------------------------------
   -- Controllo eventuale recupero degli importi delle rate di una minicarriera non ancora salvata

   IF (aPgMcarriera IS NOT NULL AND
       aPgMcarriera < 0) THEN

      BEGIN

         -- Recupero della minicarriera

         aRecMinicarriera:=getMinicarriera(aCdCdsMcarriera,
                                           aCdUoMcarriera,
                                           aEsercizioMcarriera,
                                           aPgMcarriera,
                                           eseguiLock);

         -- Recupero dati del tipo trattamento indicato nel compenso

         aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento(aRecMinicarriera.cd_trattamento,
                                                           aRecMinicarriera.dt_registrazione);

         -- Lettura delle rate di minicarriera. In caso di rate a tassazione separata escludo
         -- quelle che, pur scadenti nel periodo in esame, hanno data fine anteriore all'inizio
         -- dell'esercizio della minicarriera.
         -- La lettura è eseguita solo se aRecTipoTrattamento.fl_soggetto_conguaglio = 'Y'

         BEGIN

            IF aRecTipoTrattamento.fl_soggetto_conguaglio = 'Y' THEN

               IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN

                  OPEN gen_cur_r FOR

                       SELECT dt_inizio_rata,
                              dt_fine_rata
                       FROM   MINICARRIERA_RATA
                       WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                              cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                              esercizio = aRecMinicarriera.esercizio AND
                              pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                              dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                              dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');
               ELSE

                  OPEN gen_cur_r FOR

                       SELECT dt_inizio_rata,
                              dt_fine_rata
                       FROM   MINICARRIERA_RATA
                       WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                              cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                              esercizio = aRecMinicarriera.esercizio AND
                              pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                              dt_scadenza BETWEEN aDataInizio AND aDataFine;
               END IF;

               LOOP

                  FETCH gen_cur_r INTO
                        aRecMcarrieraRata.dt_inizio_rata,
                        aRecMcarrieraRata.dt_fine_rata;

                  EXIT WHEN gen_cur_r%NOTFOUND;

                  -- Composizione della matrice delle date per il calcolo dei giorni reali di competenza
                  -- delle rate di minicarriera

                  CNRCTB545.componiMatriceDate(tabella_date,
                                               aRecMcarrieraRata.dt_inizio_rata,
                                               aRecMcarrieraRata.dt_fine_rata,
                                               'N',
                                               'N');

                  IF (aRecMinicarriera.cd_cds = aCdCdsMcarriera AND
                      aRecMinicarriera.cd_unita_organizzativa = aCdUoMcarriera AND
                      aRecMinicarriera.esercizio = aEsercizioMcarriera AND
                      aRecMinicarriera.pg_minicarriera = aPgMcarriera) THEN
                     CNRCTB545.componiMatriceDate(tabella_date_proprio,
                                                  aRecMcarrieraRata.dt_inizio_rata,
                                                  aRecMcarrieraRata.dt_fine_rata,
                                                  'N',
                                                  'N');
                  END IF;

               END LOOP;

               CLOSE gen_cur_r;

            END IF;

         END;

      END;

   END IF;
   -- Se il Compenso è un compenso da Conguaglio vanno presi in considerazione
   -- i compensi non da minicarriera
   If aOrigineCompenso=CNRCTB545.isCompensoConguaglio Then
     Begin
      Open gen_cur_con For
        Select *
        From V_COMPENSO_CONGUAGLIO_BASE A
        Where A.cd_anag = aRecAnagrafico.cd_anag
          And A.esercizio_compenso <= aRecCompenso.esercizio
          And A.dt_emissione_mandato >= aDataInizio
          And A.dt_emissione_mandato <= aDataFine
          And A.is_compenso_conguaglio = 'N'
          And A.stato_cofi = 'P';
      Loop
        Fetch gen_cur_con Into aRecCompensoConguaglioBase;
        Exit When gen_cur_con%Notfound;
          aRecCompensoC:=CNRCTB545.getCompenso(aRecCompensoConguaglioBase.cd_cds_compenso,
                                               aRecCompensoConguaglioBase.cd_uo_compenso,
                                               aRecCompensoConguaglioBase.esercizio_compenso,
                                               aRecCompensoConguaglioBase.pg_compenso,
                                               eseguiLock);
          --pipe.send_message('aRecCompensoC.dt_da_competenza_coge '||aRecCompensoC.dt_da_competenza_coge);
          --pipe.send_message('aRecCompensoC.dt_a_competenza_coge '||aRecCompensoC.dt_a_competenza_coge);
          CNRCTB545.componiMatriceDate(tabella_date,
                                       aRecCompensoC.dt_da_competenza_coge,
                                       aRecCompensoC.dt_a_competenza_coge,
                                       'N',
                                       'Y');
      End Loop;
     End;
   End If;
   -------------------------------------------------------------------------------------------------
   -- Normalizzazione delle date per eliminare periodi di eventuale sovrapposizione e calcolo dei
   -- giorni

   IF tabella_date.COUNT > 0 THEN
      normalizzaMatriceDate(tabella_date,
                            tabella_date_ok);
      aNumeroMMTutti:=CNRCTB545.getMesiMatriceDate(tabella_date_ok);
      If aOrigineCompenso=CNRCTB545.isCompensoConguaglio Then
        aNumeroMMEsercizio:=CNRCTB545.getMesiMatriceDateEsForDays(tabella_date_ok,aRecCompenso.esercizio);
      Else
        aNumeroMMEsercizio:=CNRCTB545.getMesiMatriceDateEsercizio(tabella_date_ok,aRecCompenso.esercizio);
      End If;
   END IF;

   IF tabella_date_proprio.COUNT > 0 Then
      normalizzaMatriceDate(tabella_date_proprio,
                            tabella_date_proprio_ok);

      aNumeroMMProprio:=CNRCTB545.getMesiMatriceDate(tabella_date_proprio_ok);
   END IF;

   -- Eliminato il test in quanto è possibile avere valore 0 nel numero giorni

--   IF (aNumeroMMTutti = 0 OR aNumeroMMProprio = 0) THEN
--      IBMERR001.RAISE_ERR_GENERICO
--         ('E'' stato recuperato un valore dei giorni delle rate (totale o proprio) pari a 0. ' ||
--          'Probabilmente sono state pagata anticipatamente o in ritardo una o più rate con scadenza ' ||
--          'rispettivamente in esercizi successivi o precedenti');
--   END IF;

END getNumeroMMRateMcarriera;

--==================================================================================================
-- Inserita routine di calcolo del numero complessivo di giorni per le rate di minicarriera
-- con scadenza nell'anno e competenza in un dato intervallo
--==================================================================================================
PROCEDURE getNumeroGGRateInPeriodo
   (
    aCdTerzo COMPENSO.CD_TERZO%TYPE,
    aEsercizioCompenso  COMPENSO.ESERCIZIO%TYPE,
    aDtDaCompetenza  COMPENSO.DT_DA_COMPETENZA_COGE%TYPE,
    aDtACompetenza COMPENSO.DT_A_COMPETENZA_COGE%TYPE,
    aNumeroGGTutti IN OUT INTEGER
   ) IS

   aDataInizio DATE;
   aDataFine DATE;
   --aCdTerzo TERZO.cd_terzo%TYPE;
   eseguiLock CHAR(1);
   flTrovato CHAR(1);
   i BINARY_INTEGER;

   tabella_date CNRCTB545.intervalloDateTab;
   tabella_date_ok CNRCTB545.intervalloDateTab;

   aRecMinicarriera MINICARRIERA%ROWTYPE;
   aRecMcarrieraRata MINICARRIERA_RATA%ROWTYPE;
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

   gen_cur_m GenericCurTyp;
   gen_cur_r GenericCurTyp;

BEGIN

   aDataInizio:=TO_DATE('0101' || aEsercizioCompenso,'DDMMYYYY');
   aDataFine:=TO_DATE('3112' || aEsercizioCompenso,'DDMMYYYY');
   aNumeroGGTutti:=0;
   aRecMinicarriera:=NULL;
   aRecMcarrieraRata:=NULL;
   eseguiLock:='Y';

   tabella_date.DELETE;
   tabella_date_ok.DELETE;

   -------------------------------------------------------------------------------------------------
   -- Ciclo di recupero delle rate di minicarriera scadenti in un esercizio e riferite ad un dato
   -- soggetto anagrafico

   -- Ciclo secondario di recupero delle minicarriere riferite ad un dato terzo --------------
   BEGIN

            OPEN gen_cur_m FOR

                 SELECT A.cd_cds,
                        A.cd_unita_organizzativa,
                        A.esercizio,
                        A.pg_minicarriera,
                        A.cd_trattamento,
                        A.fl_tassazione_separata,
                        A.stato
                 FROM   MINICARRIERA A
                 WHERE  A.cd_terzo = aCdTerzo AND
                        A.pg_minicarriera > 0 AND
                        EXISTS
                           (SELECT 1
                            FROM   TIPO_TRATTAMENTO B
                            WHERE  B.cd_trattamento = A.cd_trattamento AND
                                   B.dt_ini_validita <= A.dt_registrazione AND
                                   B.dt_fin_validita >= A.dt_registrazione AND
                                   B.fl_soggetto_conguaglio = 'Y');

            LOOP

               FETCH gen_cur_m INTO
                     aRecMinicarriera.cd_cds,
                     aRecMinicarriera.cd_unita_organizzativa,
                     aRecMinicarriera.esercizio,
                     aRecMinicarriera.pg_minicarriera,
                     aRecMinicarriera.cd_trattamento,
                     aRecMinicarriera.fl_tassazione_separata,
                     aRecMinicarriera.stato;

               EXIT WHEN gen_cur_m%NOTFOUND;

               -- Lettura delle rate di minicarriera. In caso di rate a tassazione separata escludo
               -- quelle che, pur scadenti nel periodo in esame, hanno data fine anteriore all'inizio
               -- dell'esercizio della minicarriera

               BEGIN

                  IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN

                     IF aRecMinicarriera.stato = 'A' THEN

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');

                     ELSE

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    dt_fine_rata >= TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY') AND
                                    cd_cds_compenso IS NOT NULL;

                     END IF;

                  ELSE

                     IF aRecMinicarriera.stato = 'A' THEN

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine;

                     ELSE

                        OPEN gen_cur_r FOR

                             SELECT dt_inizio_rata,
                                    dt_fine_rata
                             FROM   MINICARRIERA_RATA
                             WHERE  cd_cds = aRecMinicarriera.cd_cds AND
                                    cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
                                    esercizio = aRecMinicarriera.esercizio AND
                                    pg_minicarriera = aRecMinicarriera.pg_minicarriera AND
                                    dt_scadenza BETWEEN aDataInizio AND aDataFine AND
                                    cd_cds_compenso IS NOT NULL;

                     END IF;

                  END IF;

                  LOOP

                     FETCH gen_cur_r INTO
                           aRecMcarrieraRata.dt_inizio_rata,
                           aRecMcarrieraRata.dt_fine_rata;

                     EXIT WHEN gen_cur_r%NOTFOUND;

                     -- sel la rata è esterna al periodo di competenza in input la riconduco ad esso
                     if aRecMcarrieraRata.dt_inizio_rata < aDtDaCompetenza and
                        aRecMcarrieraRata.dt_fine_rata > aDtDaCompetenza then
                           aRecMcarrieraRata.dt_inizio_rata := aDtDaCompetenza;
                     end if;
                     if aRecMcarrieraRata.dt_fine_rata > aDtACompetenza and
                        aRecMcarrieraRata.dt_inizio_rata < aDtACompetenza then
                           aRecMcarrieraRata.dt_fine_rata := aDtACompetenza;
                     end if;


                     -- Composizione della matrice delle date per il calcolo dei giorni reali di competenza
                     -- delle rate di minicarriera
                     if aRecMcarrieraRata.dt_inizio_rata >= aDtDaCompetenza and
                        aRecMcarrieraRata.dt_fine_rata <= aDtACompetenza then
                           CNRCTB545.componiMatriceDate(tabella_date,
                                                  aRecMcarrieraRata.dt_inizio_rata,
                                                  aRecMcarrieraRata.dt_fine_rata,
                                                  'N',
                                                  'N');
                     end if;
                  END LOOP;

                  CLOSE gen_cur_r;

               END;

            END LOOP;

            CLOSE gen_cur_m;

   END;

   -------------------------------------------------------------------------------------------------
   -- Normalizzazione delle date per eliminare periodi di eventuale sovrapposizione e calcolo dei
   -- giorni

   IF tabella_date.COUNT > 0 THEN
      normalizzaMatriceDate(tabella_date,
                            tabella_date_ok);

      aNumeroGGTutti:=CNRCTB545.getGiorniMatriceDate(tabella_date_ok);
   END IF;

   -- Eliminato il test in quanto è possibile avere valore 0 nel numero giorni

--   IF (aNumeroGGTutti = 0 OR aNumeroGGProprio = 0) THEN
--      IBMERR001.RAISE_ERR_GENERICO
--         ('E'' stato recuperato un valore dei giorni delle rate (totale o proprio) pari a 0. ' ||
--          'Probabilmente sono state pagata anticipatamente o in ritardo una o più rate con scadenza ' ||
--          'rispettivamente in esercizi successivi o precedenti');
--   END IF;

END getNumeroGGRateInPeriodo;


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

--==================================================================================================
-- Torna l'ammontare complessivo, la minima data inizio, la massima data fine ed il numero delle rate
-- non ancora liquidate data una minicarriera
--==================================================================================================
PROCEDURE getNonLiquidatoRate
   (
    aCdCds MINICARRIERA_RATA.cd_cds%TYPE,
    aCdUnitaOrganizzativa MINICARRIERA_RATA.cd_unita_organizzativa%TYPE,
    aEsercizio MINICARRIERA_RATA.esercizio%TYPE,
    aPgMinicarriera MINICARRIERA_RATA.pg_minicarriera%TYPE,
    aImporto IN OUT NUMBER,
    aDataMin IN OUT DATE,
    aDataMax IN OUT DATE,
    aNumeroRate IN OUT NUMBER
   ) IS
   aRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE;
   gen_cur GenericCurTyp;

BEGIN

   aNumeroRate:=0;
   aImporto:=0;

   BEGIN

      OPEN gen_cur FOR

           SELECT *
           FROM   MINICARRIERA_RATA
           WHERE  cd_cds = aCdCds AND
                  cd_unita_organizzativa = aCdUnitaOrganizzativa AND
                  esercizio = aEsercizio AND
                  pg_minicarriera = aPgMinicarriera AND
                  stato_ass_compenso = 'N'
           ORDER BY dt_inizio_rata;
      LOOP

         FETCH gen_cur INTO
               aRecMCarrieraRata;

         EXIT WHEN gen_cur%NOTFOUND;

         aNumeroRate:=aNumeroRate + 1;

         IF aNumeroRate = 1 THEN
            aDataMin:=aRecMCarrieraRata.dt_inizio_rata;
         END IF;

         aDataMax:=aRecMCarrieraRata.dt_fine_rata;

         aImporto:=aImporto +  aRecMCarrieraRata.im_rata;

      END LOOP;

      CLOSE gen_cur;

   END;

END getNonLiquidatoRate;

-- =================================================================================================
-- Torna il valore dell'aliquota media irpef per calcolo minicarriera a tassazione separata
-- =================================================================================================
FUNCTION getAliquotaMediaIrpef
   (
    aImponibile_eseprec2 NUMBER,
    aImponibile_eseprec1 NUMBER,
    aEsercizio NUMBER,
    aEsercizioMCarriera NUMBER,
    aDtRegMCarriera DATE,
    aTiAnagrafico CHAR,
    aCdTerzo NUMBER,
    aCdTipoRapporto VARCHAR2,
    aCdTipoTrattamento VARCHAR2
   ) RETURN NUMBER IS
   dataOdierna DATE;
   aDataFineAnno DATE;
   aDataAccessoScaglione DATE;
   aImponibileBase NUMBER(15,2);
   aAliquotaMedia NUMBER(10,6);
   aImportoAccesso NUMBER(15,2);
   aImportoMaxRif number(15,2);
   aImponibileBlocco NUMBER(15,2);
   resto NUMBER(15,2);
   aImportoIrpef NUMBER(15,2);
   esisteIrpef CHAR(1);
   aCdRegione COMPENSO.cd_regione_add%TYPE;
   aCdProvincia COMPENSO.cd_provincia_add%TYPE;
   aPgComune COMPENSO.pg_comune_add%TYPE;
   aAliquotaAnag SCAGLIONE.aliquota%TYPE;

   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;
   aRecVTrattamento V_TIPO_TRATTAMENTO_TIPO_CORI%ROWTYPE;
   aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE;
   aRecTmpScaglioneImponibile V_SCAGLIONE%ROWTYPE;


   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Estrazione dei dati di base al calcolo dell'aliquota media

   -- Valorizzazione variabili

   esisteIrpef:='N';
   dataOdierna:=TRUNC(sysdate);
   aDataFineAnno:=TO_DATE('3112' || aEsercizio,'DDMMYYYY');
   IF dataOdierna > aDataFineAnno THEN
      dataOdierna:=aDataFineAnno;
   END IF;
   aDataAccessoScaglione:=TO_DATE('3112' || (aEsercizioMCarriera),'DDMMYYYY');

   -- Calcolo dell'imponibile base come somma degli impnoibili dei due esercizi precedenti diviso 2

   aImponibileBase:=ROUND(((aImponibile_eseprec2 + aImponibile_eseprec1) / 2),2);

   -- Recupero dati del tipo trattamento indicato nella minicarriera

   aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento(aCdTipoTrattamento,
                                                     aDtRegMCarriera);

   -- Recupero dei dati relativi al trattamento selezionato per la minicarriera. Si estrae il solo CORI
   -- di tipo IRPEF

   BEGIN

      OPEN gen_cur FOR

           SELECT *
           FROM   V_TIPO_TRATTAMENTO_TIPO_CORI
           WHERE  cd_trattamento = aRecTipoTrattamento.cd_trattamento AND
                  dt_ini_val_trattamento = aRecTipoTrattamento.dt_ini_validita AND
                  dt_fin_val_trattamento = aRecTipoTrattamento.dt_fin_validita AND
                  dt_ini_val_tratt_cori <= dataOdierna AND
                  dt_fin_val_tratt_cori >= dataOdierna AND
                  dt_ini_val_tipo_cori <= dataOdierna AND
                  dt_fin_val_tipo_cori >= dataOdierna
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
               esisteIrpef:='Y';
               EXIT;
            END IF;
         END IF;

      END LOOP;

      CLOSE gen_cur;

      IF esisteIrpef = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Il trattamento selezionato non comprende un cori di tipo IRPEF a scaglioni, impossibile procedere ' ||
             'con il calcolo della aliquota media per tassazione separata');
      END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Calcolo del valore IRPEF

   -- Valorizzazione variabili

   aCdRegione:='*';
   aCdProvincia:='*';
   aPgComune:=0;
   aImportoIrpef:=0;
   aImportoAccesso:=0;
   aImportoMaxRif:=aImponibileBase;
   aAliquotaAnag:=0;

   -- Recupero del primo scaglione

   aRecVPreScaglione:=CNRCTB545.getScaglione(aRecVTrattamento.cd_cori,
                                             aTiAnagrafico,
                                             aDataAccessoScaglione,
                                             aImportoAccesso,
                                             aAliquotaAnag,
                                             aCdRegione,
                                             aCdProvincia,
                                             aPgComune);

   -- L'accesso agli scaglioni successivi è svolto solo se l'importo massimo di riferimento è maggiore del
   -- valore dell'importo superiore altrimenti si determina il valore dell'irpef sul primo scaglione

   IF aRecVPreScaglione.im_superiore >= aImportoMaxRif THEN
      aImportoIrpef:=ROUND((aImponibileBase * (aRecVPreScaglione.aliquota_percip / 100)),2);
   ELSE

      -- Valorizzazione importo IRPEF per il primo scaglione

      aImponibileBlocco:=aRecVPreScaglione.im_superiore;
      resto:=aImportoMaxRif - aImponibileBlocco;
      aImportoIrpef:=ROUND((aImponibileBlocco * (aRecVPreScaglione.aliquota_percip / 100)),2);

      -- Valorizzazione importo IRPEF per i restanti scaglioni

      BEGIN

         OPEN gen_cur FOR

              SELECT *
              FROM   V_SCAGLIONE
              WHERE  cd_contributo_ritenuta = aRecVTrattamento.cd_cori AND
                     (ti_anagrafico = aTiAnagrafico OR
                      ti_anagrafico = '*' ) AND
                     dt_inizio_validita <= aDataAccessoScaglione AND
                     dt_fine_validita >= aDataAccessoScaglione AND
                     cd_regione = aCdRegione AND
                     cd_provincia = aCdProvincia AND
                     pg_comune = aPgComune AND
                     im_inferiore >= aRecVPreScaglione.im_superiore AND
                     im_inferiore <= aImportoMaxRif
              ORDER BY 4;

         LOOP

            FETCH gen_cur INTO aRecTmpScaglioneImponibile;

            EXIT WHEN gen_cur%NOTFOUND;

            IF aImportoMaxRif < aRecTmpScaglioneImponibile.im_superiore THEN
               aImponibileBlocco:=resto;
            ELSE
               aImponibileBlocco:=aRecTmpScaglioneImponibile.im_superiore -
                                  aRecTmpScaglioneImponibile.im_inferiore +
                                  0.01;
               resto:=resto - aImponibileBlocco;
            END IF;

            aImportoIrpef:=aImportoIrpef + ROUND(aImponibileBlocco * (aRecTmpScaglioneImponibile.aliquota_percip / 100),2);

         END LOOP;

         CLOSE gen_cur;

      END;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Calcolo dell'aliquota media IRPEF

   -- Se l'importo irpef è uguale a zero si torna, come aliquota media, quella del primo scaglione
   -- altrimenti è calcolata come valore irpef moltiplicato 100 diviso per l'imponibile base

   IF aImportoIrpef = 0 THEN
      aAliquotaMedia:=ROUND((aRecVPreScaglione.aliquota_percip),2);
   ELSE
      aAliquotaMedia:=ROUND(((aImportoIrpef * 100) / aImponibileBase),2);
   END IF;

   RETURN aAliquotaMedia;

END getAliquotaMediaIrpef;

END;
