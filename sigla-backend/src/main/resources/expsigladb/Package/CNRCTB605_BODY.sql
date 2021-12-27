--------------------------------------------------------
--  DDL for Package Body CNRCTB605
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB605" AS

--==================================================================================================
-- Lettura di una missione esistente, verifica ammissibilità delle spese registrate e calcolo delle diarie
--==================================================================================================
PROCEDURE creaRateMinicarriera
   (
    inCdsMCarriera MINICARRIERA.cd_cds%TYPE,
    inUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEseMCarriera MINICARRIERA.esercizio%TYPE,
    inPgMCarriera MINICARRIERA.pg_minicarriera%TYPE,
    inCopiaCdsMCarriera MINICARRIERA.cd_cds%TYPE,
    inCopiaUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inCopiaEseMCarriera MINICARRIERA.esercizio%TYPE,
    inCopiaPgMCarriera MINICARRIERA.pg_minicarriera%TYPE
   ) IS
   eseguiLock CHAR(1);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   dataOdierna:=sysdate;
   dataOdiernaTronca:=TRUNC(sysdate);
   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura della minicarriera (testata della minicarriera in elaborazione)

   leggiDatiBaseMinicarriera(inCdsMCarriera,
                             inUoMCarriera,
                             inEseMCarriera,
                             inPgMCarriera,
                             eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Calcolo  e scrittura delle rate

   calcolaRate;
   scriviRate;

END creaRateMinicarriera;

--==================================================================================================
-- Lettura di una minicaroera
--==================================================================================================
PROCEDURE leggiDatiBaseMinicarriera
   (
    aCdCds MINICARRIERA.cd_cds%TYPE,
    aCdUo MINICARRIERA.cd_unita_organizzativa%TYPE,
    aEsercizio MINICARRIERA.esercizio%TYPE,
    aPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE,
    eseguiLock CHAR
   ) IS

BEGIN

   aRecMinicarriera:=CNRCTB600.getMinicarriera(aCdCds,
                                               aCdUo,
                                               aEsercizio,
                                               aPgMinicarriera,
                                               eseguiLock);

END leggiDatiBaseMinicarriera;


-- ==================================================================================================
-- Routine principale di calcolo delle rate
-- ==================================================================================================
PROCEDURE calcolaRate
   IS
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   -- Azzeramento della matrice rate

   matriceRate_tab.DELETE;

   --------------------------------------------------------------------------------------------------
   -- Distinguo se devo gestire o meno la tassazione separata. Se attivo la tassazione separata il
   -- trattamento selezionato deve essere per tassazione separata

   IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN
      aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento (aRecMinicarriera.cd_trattamento,
                                                         aRecMinicarriera.dt_registrazione);
      IF aRecTipoTrattamento.fl_tassazione_separata = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('La minicarriera in calcolo prevede la gestione a tassazione separata che non è compatibile ' ||
             'con quanto indicato nel trattamento selezionato');
      END IF;
      calcolaRateTassazioneSep;
   ELSE
      calcolaRateNormale;
   END IF;

END calcolaRate;

-- ==================================================================================================
-- Calcolo delle rate di una minicarriera con data inizio nell'esercizio di creazione o nel futuro
-- ==================================================================================================
PROCEDURE calcolaRateNormale
   IS
   isRataMensile CHAR(1);
   isRataMese CHAR(1);
   aDataMinScadenza DATE;
   aImportoRata NUMBER(15,2);
   aImportoResto NUMBER(15,2);
   --aNumeroGG NUMBER;

   aNumeroGGMiniComm  NUMBER;
   aImportoGGMiniComm NUMBER;
   aNumeroGGPrimaRata  NUMBER;
   aNumeroGGUltimaRata  NUMBER;
BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri
   /*viene passato dalla mappa
   numero_rate := ROUND(IBMUTL001.getMonthsBetween(IBMUTL001.getFirstDayOfMonth(aRecMinicarriera.dt_inizio_minicarriera),
                               IBMUTL001.getLastDayOfMonth(aRecMinicarriera.dt_fine_minicarriera)));
   */

   -- Controllo che il numero delle rate trovi capienza nel numero di giorni della minicariera
   IF IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                               aRecMinicarriera.dt_fine_minicarriera) < aRecMinicarriera.numero_rate THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Il numero di rate indicate nella minicarriera ' || aRecMinicarriera.numero_rate ||
          ' non trova capienza nei giorni di durata della stessa ' || IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                                                                                               aRecMinicarriera.dt_fine_minicarriera));
   END IF;

   -- Determino la tipologia delle rate che andranno a generarsi. Rate mensili o meno

   isRataMensile:=CNRCTB600.chkRateMensiliNormale(aRecMinicarriera);

   -- Se la rata è mensile e le date sono di inizio e fine mese allora metto isRataMese = 'Y'. Per ogni
   -- rata nel primo caso la data di fine è il giorno precedente della data inizio (al limite il fine mese),
   -- nel secondo caso la data di fine è sempre un fine mese
   -- Determino la data minima della scadenza riferita alla data di creazione (data di sistema).
   -- Rate mensili
   --    Si normalizza al 20 del mese di creazione o di quello successivo se il giorno è superiore a 20
   --    Si mantiene la data di sistema
   IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
      IF IBMUTL001.isDifferenzaMese(aRecMinicarriera.dt_inizio_minicarriera,
                                    aRecMinicarriera.dt_fine_minicarriera) = IBMUTL001.isIntervalloDateMese THEN
         isRataMese:=IBMUTL001.isIntervalloDateMese;
      ELSE
         isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      END IF;
      IF IBMUTL001.getDayOfDate(dataOdiernaTronca) > 20 THEN
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(IBMUTL001.getAddMonth(IBMUTL001.getFirstDayOfMonth(dataOdiernaTronca),1),'MMYYYY'),'DDMMYYYY');
      ELSE
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(dataOdiernaTronca,'MMYYYY'),'DDMMYYYY');
      END IF;
   ELSE
      isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      aDataMinScadenza:=dataOdiernaTronca;
   END IF;
   --------------------------------------------------------------------------------------------------
   -- Costruzione della matrice rate

   BEGIN

      FOR i IN 1 .. aRecMinicarriera.numero_rate

      LOOP

         -- Data inizio rata. Data inizio minicarriera o data fine rata precedente + 1

         IF i = 1 THEN
            matriceRate_tab(i).tDtInizio:=aRecMinicarriera.dt_inizio_minicarriera;
         ELSE
            matriceRate_tab(i).tDtInizio:=(matriceRate_tab(i-1).tDtFine + 1);
         END IF;

         -- Data fine rata. Data fine minicarriera o data fine mese

         IF i = aRecMinicarriera.numero_rate THEN
              matriceRate_tab(i).tDtFine:=aRecMinicarriera.dt_fine_minicarriera;
         ELSE
              matriceRate_tab(i).tDtFine:=IBMUTL001.getLastDayOfMonth(matriceRate_tab(i).tDtInizio);
         END IF;

         -- Determino la data di scadenza temporaneo portata al 20 della data fine rata solo se isRataMensile = Y

         IF isRataMensile = 'Y' THEN
            matriceRate_tab(i).tDtScadenza:=TO_DATE('20' || TO_CHAR(matriceRate_tab(i).tDtFine,'MMYYYY'),'DDMMYYYY');
         ELSE
            matriceRate_tab(i).tDtScadenza:=matriceRate_tab(i).tDtFine;
         END IF;

         -- Determino la data di scadenza in base ai mesi di anticipo e posticipo.
         -- Se posticipo data di fine rata + mesi posticipo -1.
         -- Se anticipo data di fine rata - mesi anticipo

         IF    aRecMinicarriera.ti_anticipo_posticipo = 'A' THEN
               matriceRate_tab(i).tDtScadenza:=IBMUTL001.getAddMonth(matriceRate_tab(i).tDtScadenza,
                                                                     (aRecMinicarriera.mesi_anticipo_posticipo * -1));
         ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'P' THEN
               matriceRate_tab(i).tDtScadenza:=IBMUTL001.getAddMonth(matriceRate_tab(i).tDtScadenza,
                                                                     (aRecMinicarriera.mesi_anticipo_posticipo - 1));
         END IF;

         -- Normalizzo sempre la data di scadenza calcolata alla data minima di scadenza se quest'ultima è maggiore
         IF matriceRate_tab(i).tDtScadenza < aDataMinScadenza THEN
            matriceRate_tab(i).tDtScadenza:=aDataMinScadenza;
         END IF;
      END LOOP;

      aNumeroGGMiniComm := 0;
      aNumeroGGPrimaRata:=0;
      aNumeroGGUltimaRata:=0;

      --Calcolo il numero di gg della minicarriera (rapportati a 30)
      For i IN 1 .. aRecMinicarriera.numero_rate  Loop
           If (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio) = IBMUTL001.getDayFirstDayOfMonth(matriceRate_tab(i).tDtInizio))
              And
              (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine)) Then

	         aNumeroGGMiniComm := aNumeroGGMiniComm + 30;
	         --potrebbe essere anche la prima e/o l'ultima rata
	         If i = 1 Then
	             aNumeroGGPrimaRata := 30;
	         Elsif i = aRecMinicarriera.numero_rate Then
	             aNumeroGGUltimaRata := 30;
	         End If;
	   Elsif (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio) = IBMUTL001.getDayFirstDayOfMonth(matriceRate_tab(i).tDtInizio)) Then
	         -- ultimo mese
	         aNumeroGGUltimaRata := IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine);
	         If aNumeroGGUltimaRata = 31 Then
	             aNumeroGGUltimaRata := 30;
	         Elsif IBMUTL001.getMonthOfDate(matriceRate_tab(i).tDtFine) = 2 And
	               aNumeroGGUltimaRata = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine) Then
	             aNumeroGGUltimaRata := 30;
	         End If;
	         aNumeroGGMiniComm := aNumeroGGMiniComm + aNumeroGGUltimaRata;
	   Elsif (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine)) Then
	         --primo mese
	         aNumeroGGPrimaRata:=(30 - (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio)-IBMUTL001.getDayFirstDayOfMonth(matriceRate_tab(i).tDtInizio)));
	         If aNumeroGGPrimaRata = 0 Then
	             aNumeroGGPrimaRata := 1;
	         End If;
	         aNumeroGGMiniComm := aNumeroGGMiniComm + aNumeroGGPrimaRata;
	   Else
	         --ho una minicarriera che dura meno di un mese
	         aNumeroGGMiniComm :=  IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) - IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio) + 1;
	         If aNumeroGGMiniComm = 0 Then
	            aNumeroGGMiniComm := 1;
	         End If;
	         If aNumeroGGMiniComm = 31 Then
	            aNumeroGGMiniComm := 30;
	         End If;
	         aNumeroGGPrimaRata:=aNumeroGGMiniComm;
	         aNumeroGGUltimaRata:=aNumeroGGMiniComm;
	   End If;
      END LOOP;

      aImportoGGMiniComm:=(aRecMinicarriera.im_totale_minicarriera / aNumeroGGMiniComm);
      aImportoResto:=aRecMinicarriera.im_totale_minicarriera;

      FOR i IN 1 .. aRecMinicarriera.numero_rate
      LOOP
	   If i = aRecMinicarriera.numero_rate Then
	       	         matriceRate_tab(i).tImRata:= aImportoResto;
	   Elsif ((IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio) = IBMUTL001.getDayFirstDayOfMonth(matriceRate_tab(i).tDtInizio))
                  And
                  (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine))) Then

	           aImportoRata := Round(aImportoGGMiniComm * 30,2);
	           matriceRate_tab(i).tImRata:= aImportoRata;
	           aImportoResto := aImportoResto - aImportoRata;
	   Elsif (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine)) Then
	         --primo mese
	         aImportoRata := Round(aImportoGGMiniComm * aNumeroGGPrimaRata,2);
	         matriceRate_tab(i).tImRata:= aImportoRata;
	         aImportoResto := aImportoResto - aImportoRata;
	   End If;
      END LOOP;

   END;

END calcolaRateNormale;

/* Vecchia procedura - prima della Finanziaria 2007
PROCEDURE calcolaRateNormale
   IS
   isRataMensile CHAR(1);
   isRataMese CHAR(1);
   aDataMinScadenza DATE;
   aImportoRata NUMBER(15,2);
   aImportoResto NUMBER(15,2);
   aNumeroGG NUMBER;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   -- Controllo che il numero delle rate trovi capienza nel numero di giorni della minicariera

   IF IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                               aRecMinicarriera.dt_fine_minicarriera) < aRecMinicarriera.numero_rate THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Il numero di rate indicate nella minicarriera ' || aRecMinicarriera.numero_rate ||
          ' non trova capienza nei giorni di durata della stessa ' || IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                                                                                               aRecMinicarriera.dt_fine_minicarriera));
   END IF;

   -- Determino la tipologia delle rate che andranno a generarsi. Rate mensili o meno

   isRataMensile:=CNRCTB600.chkRateMensiliNormale(aRecMinicarriera);

   -- Se la rata è mensile e le date sono di inizio e fine mese allora metto isRataMese = 'Y'. Per ogni
   -- rata nel primo caso la data di fine è il giorno precedente della data inizio (al limite il fine mese),
   -- nel secondo caso la data di fine è sempre un fine mese
   -- Determino la data minima della scadenza riferita alla data di creazione (data di sistema).
   -- Rate mensili
   --    Si normalizza al 20 del mese di creazione o di quello successivo se il giorno è superiore a 20
   --    Si mantiene la data di sistema

   IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
      IF IBMUTL001.isDifferenzaMese(aRecMinicarriera.dt_inizio_minicarriera,
                                    aRecMinicarriera.dt_fine_minicarriera) = IBMUTL001.isIntervalloDateMese THEN
         isRataMese:=IBMUTL001.isIntervalloDateMese;
      ELSE
         isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      END IF;
      IF IBMUTL001.getDayOfDate(dataOdiernaTronca) > 20 THEN
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(IBMUTL001.getAddMonth(IBMUTL001.getFirstDayOfMonth(dataOdiernaTronca),1),'MMYYYY'),'DDMMYYYY');
      ELSE
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(dataOdiernaTronca,'MMYYYY'),'DDMMYYYY');
      END IF;
   ELSE
      isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      aDataMinScadenza:=dataOdiernaTronca;
   END IF;

   --------------------------------------------------------------------------------------------------
   -- Calcolo dell'importo della rata in modo proporzionale al numero di rate

   aImportoRata:=ROUND((aRecMinicarriera.im_totale_minicarriera / aRecMinicarriera.numero_rate),2);
   aImportoResto:=aRecMinicarriera.im_totale_minicarriera;

   --------------------------------------------------------------------------------------------------
   -- Se le rate non sono mensili allora calcolo i giorni di ogni rata in modo proporzionale al numero
   -- delle stesse

   IF isRataMensile = IBMUTL001.isNotIntervalloDateMensile THEN
      aNumeroGG:=ROUND(((IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                                                  aRecMinicarriera.dt_fine_minicarriera)) /
                       aRecMinicarriera.numero_rate));
   ELSE
      aNumeroGG:=0;
   END IF;

   --------------------------------------------------------------------------------------------------
   -- Costruzione della matrice rate

   BEGIN

      FOR i IN 1 .. aRecMinicarriera.numero_rate

      LOOP

         -- Data inizio rata. Data inizio minicarriera o data fine rata precedente + 1

         IF i = 1 THEN
            matriceRate_tab(i).tDtInizio:=aRecMinicarriera.dt_inizio_minicarriera;
         ELSE
            IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
               matriceRate_tab(i).tDtInizio:=IBMUTL001.getAddMonth(aRecMinicarriera.dt_inizio_minicarriera,(i-1));
            ELSE
               matriceRate_tab(i).tDtInizio:=(matriceRate_tab(i-1).tDtFine + 1);
            END IF;
         END IF;

         -- Data fine rata. Data fine minicarriera o data fine mese o calcolata in base ai giorni

         IF i = aRecMinicarriera.numero_rate THEN
            matriceRate_tab(i).tDtFine:=aRecMinicarriera.dt_fine_minicarriera;
         ELSE
            IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
               IF isRataMese = IBMUTL001.isIntervalloDateMese THEN
                  matriceRate_tab(i).tDtFine:=IBMUTL001.getLastDayOfMonth(matriceRate_tab(i).tDtInizio);
               ELSE
                  matriceRate_tab(i).tDtFine:=(IBMUTL001.getAddMonth(aRecMinicarriera.dt_inizio_minicarriera,(i)) - 1);
               END IF;
            ELSE
               matriceRate_tab(i).tDtFine:=matriceRate_tab(i).tDtInizio + aNumeroGG;
            END IF;
         END IF;

         -- Determino la data di scadenza temporaneo portata al 20 della data fine rata solo se isRataMensile = Y

         IF isRataMensile = 'Y' THEN
            matriceRate_tab(i).tDtScadenza:=TO_DATE('20' || TO_CHAR(matriceRate_tab(i).tDtFine,'MMYYYY'),'DDMMYYYY');
         ELSE
            matriceRate_tab(i).tDtScadenza:=matriceRate_tab(i).tDtFine;
         END IF;

         -- Determino la data di scadenza in base ai mesi di anticipo e posticipo.
         -- Se posticipo data di fine rata + mesi posticipo -1.
         -- Se anticipo data di fine rata - mesi anticipo

         IF    aRecMinicarriera.ti_anticipo_posticipo = 'A' THEN
               matriceRate_tab(i).tDtScadenza:=IBMUTL001.getAddMonth(matriceRate_tab(i).tDtScadenza,
                                                                     (aRecMinicarriera.mesi_anticipo_posticipo * -1));
         ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'P' THEN
               matriceRate_tab(i).tDtScadenza:=IBMUTL001.getAddMonth(matriceRate_tab(i).tDtScadenza,
                                                                     (aRecMinicarriera.mesi_anticipo_posticipo - 1));
         END IF;

         -- Normalizzo sempre la data di scadenza calcolata alla data minima di scadenza se quest'ultima è maggiore

         IF matriceRate_tab(i).tDtScadenza < aDataMinScadenza THEN
            matriceRate_tab(i).tDtScadenza:=aDataMinScadenza;
         END IF;

         -- Importo della rata. L'ultima rata accoglie il resto della divisione

         IF i = aRecMinicarriera.numero_rate THEN
            matriceRate_tab(i).tImRata:=aImportoResto;
         ELSE
            matriceRate_tab(i).tImRata:=aImportoRata;
            aImportoResto:=aImportoResto - aImportoRata;
         END IF;

      END LOOP;

   END;

END calcolaRateNormale;
*/
-- ==================================================================================================
-- Calcolo delle rate di una minicarriera con data inizio nell'esercizio di creazione o nel futuro
-- ==================================================================================================
PROCEDURE calcolaRateTassazioneSep
   IS
   isRataMensile CHAR(1);
   isRataMese CHAR(1);
   isMensile CHAR(1);
   isMese CHAR(1);
   aDataMinScadenza DATE;
   aImportoRata NUMBER(15,2);
   aImportoPrimaRata NUMBER(15,2);
   aImportoSecondaRata NUMBER(15,2);
   aImportoResto NUMBER(15,2);
   aNumeroGG NUMBER;
   ggTotaliComm INTEGER;
   ggPrimaRataComm INTEGER;
   dtScadPrimaRataComm DATE;
   ggSecondaRataComm INTEGER;
   dtScadSecondaRataComm DATE;
   ggRestoRataComm INTEGER;
   aDataInizioAnno DATE;
   aDataRif DATE;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   aDataInizioAnno:=TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');
   isMensile:=NULL;
   isMese:=NULL;
   dtScadPrimaRataComm:=NULL;
   dtScadSecondaRataComm:=NULL;
   aImportoPrimaRata:=0;
   aImportoSecondaRata:=0;

   -- Determino la tipologia delle rate che andranno a generarsi. Rate mensili o meno

   isRataMensile:=CNRCTB600.chkRateMensiliTassazSep(aRecMinicarriera);

   -- Se la rata è mensile e le date sono di inizio e fine mese allora metto isRataMese = 'Y'. Nella
   -- generazione delle rate la data di fine rata è sempre un fine mese.
   -- Determino la data minima della scadenza. Se le rate sono mensili allora si normalizza al 20
   -- del mese di creazione o di quello successivo se la data di sistema è superiore al giorno 20
   -- Valorizzo isMese e isMensile per l'accesso alla routine di determinazione della rata di prima scadenza

   IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
      IF IBMUTL001.isDifferenzaMese(aRecMinicarriera.dt_inizio_minicarriera, aRecMinicarriera.dt_fine_minicarriera) = IBMUTL001.isIntervalloDateMese THEN
         isRataMese:=IBMUTL001.isIntervalloDateMese;
         isMensile:='N';
         isMese:='Y';
      ELSE
         isRataMese:=IBMUTL001.isNotIntervalloDateMese;
         isMensile:='Y';
         isMese:='N';
      END IF;
      IF TO_CHAR(dataOdiernaTronca,'DD') > 20 THEN
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(IBMUTL001.getAddMonth(IBMUTL001.getFirstDayOfMonth(dataOdiernaTronca),1),'MMYYYY'),'DDMMYYYY');
      ELSE
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(dataOdiernaTronca,'MMYYYY'),'DDMMYYYY');
      END IF;
   ELSE
      isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      aDataMinScadenza:=dataOdiernaTronca;
      isMensile:='N';
      isMese:='N';
   END IF;

   --------------------------------------------------------------------------------------------------
   -- Determinazione della data di scadenza della prima e seconda rata


   FOR i IN 1 .. 2

   LOOP

      aDataRif:=CNRCTB600.getDataScadenzaRata(aRecMinicarriera,
                                              isMese,
                                              isMensile,
                                              i,
                                              dtScadPrimaRataComm);

      IF i = 1 THEN
         dtScadPrimaRataComm:=aDataRif;
      ELSE
         dtScadSecondaRataComm:=aDataRif;
      END IF;

   END LOOP;


   --------------------------------------------------------------------------------------------------
   -- Calcolo i giorni totali sull'anno commerciale della minicarriera.
   -- Calcolo i giorni normali della minicarriera secondo l'anno solare a partire della seconda rata.
   -- Tale calcolo opera solo per le rate successive alla seconda.

   ggTotaliComm:=IBMUTL001.getDaysCommBetween(aRecMinicarriera.dt_inizio_minicarriera, aRecMinicarriera.dt_fine_minicarriera);
   IF aRecMinicarriera.numero_rate > 2 THEN
      aNumeroGG:=ROUND(
                          (
                              IBMUTL001.getDaysBetween((dtScadSecondaRataComm + 1), aRecMinicarriera.dt_fine_minicarriera) /
                              (aRecMinicarriera.numero_rate - 2)
                          )
                      );

   END IF;


   --------------------------------------------------------------------------------------------------
   -- Calcolo dell'importo delle rate

   aImportoResto:=aRecMinicarriera.im_totale_minicarriera;

   aImportoPrimaRata:=ROUND((aRecMinicarriera.im_totale_minicarriera  / ggTotaliComm) *
                            (IBMUTL001.getDaysCommBetween(aRecMinicarriera.dt_inizio_minicarriera,
                                                          dtScadPrimaRataComm)
                            ),2);

   IF aRecMinicarriera.numero_rate > 1 THEN
      aImportoSecondaRata:=ROUND((aRecMinicarriera.im_totale_minicarriera  / ggTotaliComm) *
                                 (IBMUTL001.getDaysCommBetween((dtScadPrimaRataComm + 1),
                                                               dtScadSecondaRataComm)
                                 ),2);
   END IF;

   IF aRecMinicarriera.numero_rate > 2 THEN
      aImportoRata:=ROUND(
                            (
                               (aRecMinicarriera.im_totale_minicarriera - aImportoPrimaRata - aImportoSecondaRata) /
                               (aRecMinicarriera.numero_rate - 2)
                            ),2
                         );
   END IF;

   --------------------------------------------------------------------------------------------------
   -- Costruzione della matrice rate

   BEGIN

      FOR i IN 1 .. aRecMinicarriera.numero_rate

      LOOP

         -- Data inizio rata.

         IF i = 1 THEN
            matriceRate_tab(i).tDtInizio:=aRecMinicarriera.dt_inizio_minicarriera;
         ELSE
            matriceRate_tab(i).tDtInizio:=(matriceRate_tab(i-1).tDtFine + 1);
         END IF;

         -- Data fine rata. Data fine minicarriera o data fine mese o calcolata in base ai giorni

         IF i = aRecMinicarriera.numero_rate THEN
            matriceRate_tab(i).tDtFine:=aRecMinicarriera.dt_fine_minicarriera;
         ELSE
            IF    i = 1 THEN
                  matriceRate_tab(i).tDtFine:=dtScadPrimaRataComm;
            ELSIF i = 2 THEN
                  matriceRate_tab(i).tDtFine:=dtScadSecondaRataComm;
            ELSE
               IF isRataMensile = 'Y' THEN
                  IF isRataMese = 'Y' THEN
                     matriceRate_tab(i).tDtFine:=IBMUTL001.getLastDayOfMonth(matriceRate_tab(i).tDtInizio);
                  ELSE
                     matriceRate_tab(i).tDtFine:=(IBMUTL001.getAddMonth(matriceRate_tab(i).tDtInizio,1) - 1);
                  END IF;
               ELSE
                  matriceRate_tab(i).tDtFine:=matriceRate_tab(i).tDtInizio + aNumeroGG - 1;
               END IF;
            END IF;
         END IF;

         -- Determino la data di scadenza temporaneo portata al 20 della data fine rata solo se isRataMensile = Y

         IF isRataMensile = 'Y' THEN
            matriceRate_tab(i).tDtScadenza:=TO_DATE('20' || TO_CHAR(matriceRate_tab(i).tDtFine,'MMYYYY'),'DDMMYYYY');
         ELSE
            matriceRate_tab(i).tDtScadenza:=matriceRate_tab(i).tDtFine;
         END IF;

         -- Normalizzo sempre la data di scadenza calcolata alla data minima di scadenza se quest'ultima è maggiore

         IF matriceRate_tab(i).tDtScadenza < aDataMinScadenza THEN
            matriceRate_tab(i).tDtScadenza:=aDataMinScadenza;
         END IF;

         -- Importo della rata. L'ultima rata accoglie il resto della divisione

         IF i = aRecMinicarriera.numero_rate THEN
            matriceRate_tab(i).tImRata:=aImportoResto;
         ELSE
            IF    i = 1 THEN
                  matriceRate_tab(i).tImRata:=aImportoPrimaRata;
                  aImportoResto:=aImportoResto - aImportoPrimaRata;
            ELSIF i = 2 THEN
                  matriceRate_tab(i).tImRata:=aImportoSecondaRata;
                  aImportoResto:=aImportoResto - aImportoSecondaRata;
            ELSE
                  matriceRate_tab(i).tImRata:=aImportoRata;
                  aImportoResto:=aImportoResto - aImportoRata;
            END IF;
         END IF;

      END LOOP;

   END;

END calcolaRateTassazioneSep;

-- ==================================================================================================
-- Scrittura delle rate di una minicarriera
-- ==================================================================================================
PROCEDURE scriviRate
   IS

BEGIN

   -- Cancellazione delle rate

   CNRCTB600.delMinicarrieraRate(aRecMinicarriera.cd_cds,
                                 aRecMinicarriera.cd_unita_organizzativa,
                                 aRecMinicarriera.esercizio,
                                 aRecMinicarriera.pg_minicarriera);

   -- Ciclo di inserimento delle rate

   BEGIN

      aRecMCarrieraRata:=NULL;
      aRecMCarrieraRata.cd_cds:=aRecMinicarriera.cd_cds;
      aRecMCarrieraRata.cd_unita_organizzativa:=aRecMinicarriera.cd_unita_organizzativa;
      aRecMCarrieraRata.esercizio:=aRecMinicarriera.esercizio;
      aRecMCarrieraRata.pg_minicarriera:=aRecMinicarriera.pg_minicarriera;
      aRecMCarrieraRata.stato_ass_compenso:='N';
      aRecMCarrieraRata.cd_cds_compenso:=NULL;
      aRecMCarrieraRata.esercizio_compenso:=NULL;
      aRecMCarrieraRata.cd_uo_compenso:=NULL;
      aRecMCarrieraRata.pg_compenso:=NULL;
      aRecMCarrieraRata.dacr:=dataOdierna;
      aRecMCarrieraRata.utcr:=aRecMinicarriera.utcr;
      aRecMCarrieraRata.duva:=dataOdierna;
      aRecMCarrieraRata.utuv:=aRecMinicarriera.utuv;
      aRecMCarrieraRata.pg_ver_rec:=aRecMinicarriera.pg_ver_rec;

      FOR i IN matriceRate_tab.FIRST .. matriceRate_tab.LAST

      LOOP

         aRecMCarrieraRata.pg_rata:=i;
         aRecMCarrieraRata.dt_inizio_rata:=matriceRate_tab(i).tDtInizio;
         aRecMCarrieraRata.dt_fine_rata:=matriceRate_tab(i).tDtFine;
         aRecMCarrieraRata.dt_scadenza:=matriceRate_tab(i).tDtScadenza;
         aRecMCarrieraRata.im_rata:=matriceRate_tab(i).tImRata;
         CNRCTB600.insMinicarrieraRate(aRecMCarrieraRata);

      END LOOP;

   END;

END scriviRate;

--==================================================================================================
-- Esecuzione della copia di una minicarriera esistente
--==================================================================================================
PROCEDURE copiaMinicarriera
   (
    inCdsMCarriera MINICARRIERA.cd_cds%TYPE,
    inUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEseMCarriera MINICARRIERA.esercizio%TYPE,
    inPgMCarriera MINICARRIERA.pg_minicarriera%TYPE,
    inCopiaCdsMCarriera MINICARRIERA.cd_cds%TYPE,
    inCopiaUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inCopiaEseMCarriera MINICARRIERA.esercizio%TYPE,
    inCopiaPgMCarriera MINICARRIERA.pg_minicarriera%TYPE
   ) IS
   eseguiLock CHAR(1);
   aRecMinicarrieraClone MINICARRIERA%ROWTYPE;
   aRecMCarrieraRateClone MINICARRIERA_RATA%ROWTYPE;
   aImporto NUMBER(15,2);
   aDataMin DATE;
   aDataMax DATE;
   aNumeroRate NUMBER(3);
   dataOdierna DATE;
   dataMinMaxDefault DATE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   eseguiLock:='Y';
   dataOdierna:=SYSDATE;

   -- Settaggio della data di riferimento (registrazione o rate) di default

   dataMinMaxDefault:=TRUNC(dataOdierna);
   IF dataMinMaxDefault > TO_DATE('3112' || inCopiaEseMCarriera, 'DDMMYYYY') THEN
      dataMinMaxDefault:= TO_DATE('3112' || inCopiaEseMCarriera, 'DDMMYYYY');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione della minicarriera in calcolo

   aRecMinicarriera:=CNRCTB600.getMinicarriera(inCdsMCarriera,
                                               inUoMCarriera,
                                               inEseMCarriera,
                                               inPgMCarriera,
                                               eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Copia MINICARRIERA

   -- Copia come memorizzazione del record in elaborazione in caso di inserimento o modifica
   -- In caso di ripristino o rinnovo la nuova minicarriera ha data registrazione pari alla data
   -- odierna eventualmente limitata al 31/12 dell'esercizio di riferimento.

   aRecMinicarrieraClone:=aRecMinicarriera;
   aRecMinicarrieraClone.cd_cds:=inCopiaCdsMCarriera;
   aRecMinicarrieraClone.cd_unita_organizzativa:=inCopiaUoMCarriera;
   aRecMinicarrieraClone.esercizio:=inCopiaEseMCarriera;
   aRecMinicarrieraClone.pg_minicarriera:=inCopiaPgMCarriera;
   IF (aRecMinicarriera.stato ='R' OR
       aRecMinicarriera.stato ='P') THEN
      aRecMinicarrieraClone.cd_cds_minicarriera_ori:=aRecMinicarriera.cd_cds;
      aRecMinicarrieraClone.cd_uo_minicarriera_ori:=aRecMinicarriera.cd_unita_organizzativa;
      aRecMinicarrieraClone.esercizio_minicarriera_ori:=aRecMinicarriera.esercizio;
      aRecMinicarrieraClone.pg_minicarriera_ori:=aRecMinicarriera.pg_minicarriera;
      aRecMinicarrieraClone.dt_registrazione:=dataMinMaxDefault;
      aRecMinicarrieraClone.stato:='A';
      aRecMinicarrieraClone.dt_sospensione:=NULL;
      aRecMinicarrieraClone.dt_ripristino:=NULL;
      aRecMinicarrieraClone.dt_rinnovo:=NULL;
      aRecMinicarrieraClone.dt_cessazione:=NULL;
      aRecMinicarrieraClone.dacr:=dataOdierna;
      aRecMinicarrieraClone.duva:=dataOdierna;
      aRecMinicarrieraClone.utcr:=aRecMinicarrieraClone.utuv;
      aRecMinicarrieraClone.pg_ver_rec:=1;

      aImporto:=0;
      aDataMin:=NULL;
      aDataMax:=NULL;
      aNumeroRate:=0;

      -- Valorizzazioni campi per minicarriera ripristinata

      IF aRecMinicarriera.stato ='R' THEN
         CNRCTB600.getNonLiquidatoRate(aRecMinicarriera.cd_cds,
                                       aRecMinicarriera.cd_unita_organizzativa,
                                       aRecMinicarriera.esercizio,
                                       aRecMinicarriera.pg_minicarriera,
                                       aImporto,
                                       aDataMin,
                                       aDataMax,
                                       aNumeroRate);
         aRecMinicarrieraClone.im_totale_minicarriera:=aImporto;
         aRecMinicarrieraClone.numero_rate:=aNumeroRate;
         aRecMinicarrieraClone.dt_inizio_minicarriera:=aDataMin;
         aRecMinicarrieraClone.dt_fine_minicarriera:=aDataMax;
      END IF;

      -- Se aDataMin o aDataMax sono NULL allora si valorizzano con la data odierna

      IF (aDataMin IS NULL OR
          aDataMax IS NULL) THEN
         aRecMinicarrieraClone.dt_inizio_minicarriera:=dataMinMaxDefault;
         aRecMinicarrieraClone.dt_fine_minicarriera:=dataMinMaxDefault;
      END IF;

   END IF;

   -- Validazione del terzo sulla minicarriera in clonazione

   IF CNRCTB080.chkEsisteTerzoPerCompenso(aRecMinicarrieraClone.cd_terzo,
                                          aRecMinicarrieraClone.cd_tipo_rapporto,
                                          aRecMinicarrieraClone.ti_anagrafico,
                                          aRecMinicarrieraClone.dt_registrazione,
                                          aRecMinicarrieraClone.dt_inizio_minicarriera,
                                          aRecMinicarrieraClone.dt_fine_minicarriera) = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO('Terzo ' || aRecMinicarrieraClone.cd_terzo || ' inesistente. ' ||
                                   'Il terzo e/o il rapporto ' || aRecMinicarrieraClone.cd_tipo_rapporto ||
                                   'risultano cessati o non contigui alle date di riferimento della minicarriera');

   END IF;

   CNRCTB600.insMinicarriera(aRecMinicarrieraClone);

   -- Copia delle rate della minicarriera

   IF aRecMinicarriera.stato='A' THEN

      BEGIN

         OPEN gen_cur FOR

              SELECT *
              FROM   MINICARRIERA_RATA
              WHERE  cd_cds = inCdsMCarriera AND
                     cd_unita_organizzativa = inUoMCarriera AND
                     esercizio = inEseMCarriera AND
                     pg_minicarriera = inPgMCarriera
              ORDER BY 1,2,3,4,5;

         LOOP

            FETCH gen_cur INTO
                  aRecMCarrieraRateClone;

            EXIT WHEN gen_cur%NOTFOUND;

            aRecMCarrieraRateClone.cd_cds:=inCopiaCdsMCarriera;
            aRecMCarrieraRateClone.cd_unita_organizzativa:=inCopiaUoMCarriera;
            aRecMCarrieraRateClone.esercizio:=inCopiaEseMCarriera;
            aRecMCarrieraRateClone.pg_minicarriera:=inCopiaPgMCarriera;

            CNRCTB600.insMinicarrieraRate(aRecMCarrieraRateClone);

        END LOOP;

        CLOSE gen_cur;

      END;

   END IF;

END copiaMinicarriera;

-- ==================================================================================================
-- Inserita routine di controllo della consistenza delle rate di una minicarriera
-- ==================================================================================================
PROCEDURE chkScadRateMinicarriera
   (
    inCdsMCarriera MINICARRIERA.cd_cds%TYPE,
    inUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEseMCarriera MINICARRIERA.esercizio%TYPE,
    inPgMCarriera MINICARRIERA.pg_minicarriera%TYPE
   ) IS
   eseguiLock CHAR(1);
   aRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE;
   aMemRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE;
   aNumeroRate INTEGER;
   aTotImportoRate MINICARRIERA_RATA.im_rata%TYPE;
   aDtInizioPrimaRata DATE;
   aDtFineUltimaRata DATE;
   aStringa VARCHAR2(2000);

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   eseguiLock:='Y';
   aMemRecMCarrieraRata:=NULL;
   aNumeroRate:=0;
   aTotImportoRate:=0;
   aStringa:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione della minicarriera in calcolo

   aRecMinicarriera:=CNRCTB600.getMinicarriera(inCdsMCarriera,
                                               inUoMCarriera,
                                               inEseMCarriera,
                                               inPgMCarriera,
                                               eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Ciclo di lettura delle rate di una minicarriera

   BEGIN

      OPEN gen_cur FOR

           SELECT *
           FROM   MINICARRIERA_RATA
           WHERE  cd_cds = inCdsMCarriera AND
                  cd_unita_organizzativa = inUoMCarriera AND
                  esercizio = inEseMCarriera AND
                  pg_minicarriera = inPgMCarriera
           ORDER BY 1,2,3,4,5;

      LOOP

         FETCH gen_cur INTO
               aRecMCarrieraRata;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Memorizzo il record precedente

         IF aMemRecMCarrieraRata.cd_cds IS NULL THEN
            aDtInizioPrimaRata:=aRecMCarrieraRata.dt_inizio_rata;
            aMemRecMCarrieraRata:=aRecMCarrieraRata;
         END IF;

         aDtFineUltimaRata:=aRecMCarrieraRata.dt_fine_rata;
         aNumeroRate:=aNumeroRate + 1;
         aTotImportoRate:=aTotImportoRate + aRecMCarrieraRata.im_rata;

         -- Controllo congruenza delle date sulle singole rate

         IF aNumeroRate > 1 THEN

            LOOP

               IF aRecMCarrieraRata.dt_inizio_rata > aRecMCarrieraRata.dt_fine_rata + 1 THEN
                  aStringa:=aStringa || 'Errore in sequenza delle date su rata ' ||
                            LPAD(aRecMCarrieraRata.pg_rata,3,0) || CHR(10);
                  EXIT;
               END IF;

               IF aRecMCarrieraRata.dt_inizio_rata != aMemRecMCarrieraRata.dt_fine_rata + 1 THEN
                  aStringa:=aStringa || 'Errore in sequenza delle date su rata ' ||
                            LPAD(aRecMCarrieraRata.pg_rata,3,0) || CHR(10);
                  EXIT;
               END IF;

               IF aRecMCarrieraRata.dt_scadenza < aMemRecMCarrieraRata.dt_scadenza THEN
                  aStringa:=aStringa || 'Errore in sequenza delle date su rata ' ||
                            LPAD(aRecMCarrieraRata.pg_rata,3,0) || CHR(10);
                  EXIT;
               END IF;

               EXIT;

            END LOOP;

            aMemRecMCarrieraRata:=aRecMCarrieraRata;

         END IF;

      END LOOP;

        CLOSE gen_cur;

      -- Controllo

      IF aRecMinicarriera.numero_rate != aNumeroRate THEN
         aStringa:=aStringa ||
                    'Numero rate in testata minicarriera ' || aRecMinicarriera.numero_rate  ||
                    ' diverso dal numero rate presente in dettaglio ' || aNumeroRate  || CHR(10);
      END IF;

      IF aRecMinicarriera.im_totale_minicarriera != aTotImportoRate THEN
         aStringa:=aStringa ||
                   'Importo totale della minicarriera ' || aRecMinicarriera.im_totale_minicarriera ||
                   ' diverso dalla sommatora degli importi delle rate ' || aTotImportoRate || CHR(10);
      END IF;

      IF aRecMinicarriera.dt_inizio_minicarriera != aDtInizioPrimaRata THEN
         aStringa:=aStringa ||
                   'Data di inizio della minicarriera ' || TO_CHAR(aRecMinicarriera.dt_inizio_minicarriera,'DD/MM/YYYY') ||
                   ' diverso dalla data inizio della prima rata ' || TO_CHAR(aDtInizioPrimaRata,'DD/MM/YYYY') || CHR(10);
      END IF;

      IF aRecMinicarriera.dt_fine_minicarriera != aDtFineUltimaRata THEN
         aStringa:=aStringa ||
                   'Data di fine della minicarriera ' || TO_CHAR(aRecMinicarriera.dt_fine_minicarriera,'DD/MM/YYYY') ||
                   ' diversa dalla data fine dell''ultima rata ' || TO_CHAR(aDtFineUltimaRata,'DD/MM/YYYY') || CHR(10);
      END IF;

   END;

   IF aStringa IS NOT NULL THEN
      IBMERR001.RAISE_ERR_GENERICO (aStringa);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Validazione del terzo sulla minicarriera
   DECLARE
     dtFine date;
   BEGIN
       IF aRecMinicarriera.stato='C' and aRecMinicarriera.dt_cessazione IS NOT NULL THEN
          dtFine := aRecMinicarriera.dt_cessazione;
       ELSE
          dtFine := aRecMinicarriera.dt_fine_minicarriera;
       END IF;

       IF CNRCTB080.chkEsisteTerzoPerCompenso(aRecMinicarriera.cd_terzo,
                                              aRecMinicarriera.cd_tipo_rapporto,
                                              aRecMinicarriera.ti_anagrafico,
                                              aRecMinicarriera.dt_registrazione,
                                              aRecMinicarriera.dt_inizio_minicarriera,
                                              dtFine) = 0 THEN
          IBMERR001.RAISE_ERR_GENERICO('Terzo ' || aRecMinicarriera.cd_terzo || ' inesistente. ' ||
                                       'Il terzo e/o il rapporto ' || aRecMinicarriera.cd_tipo_rapporto ||
                                       ' risultano cessati o non contigui alle date di riferimento della minicarriera ('||
                                       to_char(aRecMinicarriera.dt_inizio_minicarriera,'dd/mm/yyyy')||' - '||
                                       to_char(dtFine,'dd/mm/yyyy')||')');
       END IF;
  END;
END chkScadRateMinicarriera;

END;
