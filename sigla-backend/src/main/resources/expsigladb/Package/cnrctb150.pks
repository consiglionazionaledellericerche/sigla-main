CREATE OR REPLACE PACKAGE CNRCTB150 AS
-- =================================================================================================
--
-- CNRCTB150 - Package di gestione protocollazione - stampa delle fatture
--
-- Date: 15/09/2003
-- Version: 1.17
--
-- Dependency: CNRCTB 100/120/255 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 14/01/2002
-- Version: 1.0
-- Creazione
--
-- Date: 15/01/2002
-- Version: 1.1
-- Modifica procedura per la gestione della tabella RIF_PROTOCOLLO_IVA
--
-- Date: 17/01/2002
-- Version: 1.2
-- Modifica struttura del pachetto
--
-- Date: 17/01/2002
-- Version: 1.3
-- Modifica della gestione degli errori
--
-- Date: 17/01/2002
-- Version: 1.4
-- Introduzione della gestione dei Sezionali
--
-- Date: 17/01/2002
-- Version: 1.5
-- Modifica della modalita per il trattamento degli errori
--
-- Date: 18/01/2002
-- Version: 1.6
-- Inversione del controlli : Prioma chkparametri poi chkrif_protocollo_iva
--
-- Date: 11/02/2002
-- Version: 1.7
-- Modifica dei campi per il join tra la tabella FATTURA_XXX (PASSIVA, ATTIVA) e SEZIONALE
--
-- Date: 25/02/2002
-- Version: 1.8
-- Eliminazione gestione protocollazione per fatture passive.
--
-- Date: 25/02/2002
-- Version: 1.9
-- Correzione JOIN sulla tipologia fattura della tabella sezionale.
--
-- Date: 05/03/2002
-- Version: 1.10
-- Correzione UPDATE della fattura per la gestione del PG_VER_REC e della DUVA
--
-- Date: 24/05/2002
-- Version: 1.11
-- AGGIORNAMENTO DOCUMENTI CONTABILI TRAMITE IL PACKAGE CNRCTB100
--
-- Date: 24/05/2002
-- Version: 1.12
-- Nuova gestione delle tabelle di rifeimento rif_protocollo_iva, tramite la vista
-- vsx_rif_protocollo_iva.
--
-- Date: 15/07/2002
-- Version: 1.13
-- Fix per l'estrazione corretta delle fatture da protocollare, aggiunta (+)
-- Modificata gestione dell'errore per la mancanza del sezionala relativo al protocollo iva generale
--
-- Date: 16/07/2002
-- Version: 1.14
-- Fix per l'estrazione corretta delle fatture da protocollare, eliminata (+)
--
-- Date: 26/08/2002
-- Version: 1.15
-- Inserimento controllo sulla data di stampa associata alle fatture,
-- per ogni fattura da protocollare, viene calcolato il mese relativo alla data di stampa
-- e viene ricercato un corrispondente registro in stato definitivo.
--
-- Date: 06/03/2003
-- Version: 1.16
-- Fix errore CINECA 521. Rifacimento della procedura di protocollazione. Errato calcolo nella
-- sequenza dei protocolli a rottura di tipo_fattura e sezionale
--
-- Date: 15/09/2003
-- Version: 1.17
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata ? fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
-- =================================================================================================
--
-- Constants:
--
   errMsg VARCHAR2(200);

-- Variabili Globali Cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures:

----------------------------------------------------------------------------------------------------
-- Main PROCEDURE
----------------------------------------------------------------------------------------------------

-- Procedura che si occupa di valorizzare il protocollo IVA delle fatture Attive in stampa

   PROCEDURE vsx_protocollazione_doc
      (
       aPgCall VSX_RIF_PROTOCOLLO_IVA.PG_CALL%TYPE
      );

----------------------------------------------------------------------------------------------------
-- FUNZIONI e PROCEDURE di servizio
----------------------------------------------------------------------------------------------------
-- Funzione che restituisce
--    1 se il tipo di documento e valido
--    0 se il tipo di documento non e valido

   FUNCTION chkTipoDocumento
      (
       aPgCall vsx_rif_protocollo_iva.PG_CALL%TYPE
      ) RETURN VARCHAR2;

-- Funzione che restituisce
--    1 se la tabella VSX_RIF_PROTOCOLLO_IVA contiene almeno una riga per la sessione utente in esame
--    0 se la tabella VSX_RIF_PROTOCOLLO_IVA contiene 0 righe per la sessione utente in esame

   FUNCTION chkRif_Protocollo_Iva
      (
       aPgCall VSX_RIF_PROTOCOLLO_IVA.PG_CALL%TYPE,
       p_tipo_documento_amm VSX_RIF_PROTOCOLLO_IVA.tipo_documento_amm%TYPE
      ) RETURN NUMBER;

-- Lock dei documenti amministrativi oggetto di stampa

   PROCEDURE LOCK_TABELLE
      (
       aPgCall VSX_RIF_PROTOCOLLO_IVA.PG_CALL%TYPE,
       p_tipo_documento_amm VSX_RIF_PROTOCOLLO_IVA.tipo_documento_amm%TYPE
      );

-- Ritorna il cds e la uo di orgine delle fatture in stampa e la data di stampa e l'utente definiti in
-- VSX_RIF_PROTOCOLLO_IVA

   PROCEDURE getDatiBaseProtocolla
      (
       aPgCall VSX_RIF_PROTOCOLLO_IVA.PG_CALL%TYPE,
       aCdCdsOrigine IN OUT VARCHAR2,
       aCdUoOrigine IN OUT VARCHAR2,
       aDataStampa IN OUT DATE,
       aUtente IN OUT VARCHAR2
      );

END CNRCTB150;


CREATE OR REPLACE PACKAGE BODY CNRCTB150 IS
-- =================================================================================================
-- Procedura che si occupa di valorizzare il protocollo IVA delle fatture Attive.
-- =================================================================================================
PROCEDURE vsx_protocollazione_doc
   (
    aPgCall VSX_RIF_PROTOCOLLO_IVA.pg_call%TYPE
   ) IS
   p_tipo_documento_amm VSX_RIF_PROTOCOLLO_IVA.tipo_documento_amm%TYPE;
   aCdCdsOrigine VARCHAR2(30);
   aCdUoOrigine VARCHAR2(30);
   aDataStampa DATE;
   aEsercizio NUMBER(4);
   aDataInizio DATE;
   aDataFine DATE;
   aUtente VARCHAR2(20);
   eseguiLock CHAR(1);
   data_operazione DATE;

   aProtocolloFT NUMBER(10);
   aProtocolloNC NUMBER(10);
   aProtocolloND NUMBER(10);
   aProtocolloPG NUMBER(10);
   aProtocolloTmp NUMBER(10);
   aProtocolloGenTmp NUMBER(10);
   flEsistePeriodo INTEGER;
   flEsistePeriodoPrecedente INTEGER;
   flEsistePeriodoPrima INTEGER;
   flEsistePeriodoDopo INTEGER;
   aTipoReportStato VARCHAR2(50);
   aGruppoReport CHAR(1);
   cl_set varchar2(2000);

   cvTipoSezionale VARCHAR2(10);
   aRecFatturaAttiva FATTURA_ATTIVA%ROWTYPE;

   curs_doc GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Controllo congruenza parametri in input

   p_tipo_documento_amm := chkTipoDocumento(aPgCall);

   IF chkRif_Protocollo_Iva(aPgCall,p_tipo_documento_amm) = 0 THEN
      errMsg:='Fattura non trovata';
      RAISE no_data_found;
   END IF;

   -- Recupero delle informazioni di cds e UO origine e della data di stampa dei documenti in elaborazione
   -- (devono essere unici)

   getDatiBaseProtocolla(aPgCall,
                         aCdCdsOrigine,
                         aCdUoOrigine,
                         aDataStampa,
                         aUtente);

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   eseguiLock:='Y';
   data_operazione:=sysdate;
   aEsercizio:=TO_NUMBER(TO_CHAR(aDataStampa,'YYYY'));
   aDataInizio:=IBMUTL001.getFirstDayOfMonth(aDataStampa);
   aDataFine:=IBMUTL001.getLastDayOfMonth(aDataStampa);
   aTipoReportStato:=CNRCTB255.TI_REGISTRO_ACQUISTI;

   -------------------------------------------------------------------------------------------------
   -- Lock delle fatture attive oggetto di stampa

   LOCK_TABELLE (aPgCall,p_tipo_documento_amm);

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura sezionali coinvolti

   BEGIN

      FOR rigaSez IN

          (SELECT DISTINCT B.cd_tipo_sezionale
           FROM   VSX_RIF_PROTOCOLLO_IVA A,
                  FATTURA_ATTIVA B
           WHERE  A.pg_call = aPgCall AND
                  B.cd_cds = A.cd_cds AND
                  B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                  B.esercizio = A.esercizio AND
                  B.pg_fattura_attiva = A.pg_fattura AND
                  B.fl_stampa = 'N')

      LOOP

         -- Controllo che il mese relativo alla data di stampa della fattura attiva non corrisponda,
         -- al mese di un sezionale gi? stampato in modo definitivo

         flEsistePeriodo:=0;
         flEsistePeriodoPrecedente:=0;
         flEsistePeriodoPrima:=0;
         flEsistePeriodoDopo:=0;
         aGruppoReport:='*';

         CNRCTB255.getStatoReportStatoSiCdSez(aCdCdsOrigine,
                                              aCdUoOrigine,
                                              rigaSez.cd_tipo_sezionale,
                                              aDataInizio,
                                              aDataFine,
                                              aGruppoReport,
                                              aTipoReportStato,
                                              flEsistePeriodo,
                                              flEsistePeriodoPrecedente,
                                              flEsistePeriodoPrima,
                                              flEsistePeriodoDopo);

         IF (flEsistePeriodo = 1 OR
             flEsistePeriodoDopo = 1) THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Il sezionale ' || rigaSez.cd_tipo_sezionale || ' risulta gi? stampato in modo definitivo nel ' ||
                'periodo della stampa o in uno successivo');
         END IF;

         -- Recupero e lock del valore corrente dei protocolli di numerazione per il sezionale
         -- in elaborazione.

         aProtocolloFT:=-1;
         aProtocolloNC:=-1;
         aProtocolloND:=-1;
         aProtocolloPG:=-1;

         CNRCTB120.getProtCorrenteSezionale(aCdCdsOrigine,
                                            aCdUoOrigine,
                                            aEsercizio,
                                            rigaSez.cd_tipo_sezionale,
                                            eseguiLock,
                                            aProtocolloFT,
                                            aProtocolloNC,
                                            aProtocolloND,
                                            aProtocolloPG);

         -- Verifico l'esistenza di tutti i tipi di sezionali relativi al codice sezionale in esame.
         -- Nessun protocollo pu? valere -1

         IF (aProtocolloFT = -1 OR
             aProtocolloNC = -1 OR
             aProtocolloND = -1 OR
             aProtocolloPG = -1) THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Per il sezionale ' || rigaSez.cd_tipo_sezionale || ' manca la definizione di qualche tipologia ' ||
                'in numerazione');
         END IF;

         -- Ciclo di lettura delle singole fatture in stampa

         BEGIN

            OPEN curs_doc FOR

                 SELECT B.*
                 FROM   VSX_RIF_PROTOCOLLO_IVA A,
                        FATTURA_ATTIVA B
                 WHERE  A.pg_call = aPgCall AND
                        B.cd_cds = A.cd_cds AND
                        B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                        B.esercizio = A.esercizio AND
                        B.pg_fattura_attiva = A.pg_fattura AND
                        B.cd_tipo_sezionale = rigaSez.cd_tipo_sezionale AND
                        B.fl_stampa = 'N'
                 ORDER BY B.pg_fattura_attiva;

            LOOP

               FETCH curs_doc INTO
                     aRecFatturaAttiva;

               EXIT WHEN curs_doc%NOTFOUND;

               -- Incremento il protocollo iva dipendente dal tipo documento

               IF    aRecFatturaAttiva.ti_fattura = CNRCTB100.TI_FATT_FATTURA THEN
                     aProtocolloFT:=aProtocolloFT + 1;
                     aProtocolloTmp:=aProtocolloFT;
               ELSIF aRecFatturaAttiva.ti_fattura = CNRCTB100.TI_FATT_NOTA_C THEN
                     aProtocolloNC:=aProtocolloNC + 1;
                     aProtocolloTmp:=aProtocolloNC;
               ELSIF aRecFatturaAttiva.ti_fattura = CNRCTB100.TI_FATT_NOTA_D THEN
                     aProtocolloND:=aProtocolloND + 1;
                     aProtocolloTmp:=aProtocolloND;
               END IF;

               -- Incremento il protocollo iva generale

               aProtocolloPG:=aProtocolloPG + 1;
               aProtocolloGenTmp:=aProtocolloPG;

               -- Aggiornamento del protocollo iva sul documento

               cl_set := ' protocollo_iva = ' || aProtocolloTmp  || ', ' ||
                         ' protocollo_iva_generale = ' || aProtocolloGenTmp ||', ' ||
                         ' fl_stampa = ''Y''' ||', ' ||
                         ' dt_emissione = '||IBMUTL001.ASDYNTIMESTAMP(aDataStampa);

               CNRCTB100.UPDATEDOCAMM(CNRCTB100.TI_FATTURA_ATTIVA,
                                      aRecFatturaAttiva.cd_cds,
                                      aRecFatturaAttiva.esercizio,
                                      aRecFatturaAttiva.cd_unita_organizzativa,
                                      aRecFatturaAttiva.pg_fattura_attiva,
                                      cl_set,
                                      NULL,
                                      aUtente,
                                      data_operazione);

            END LOOP;

            CLOSE curs_doc;

         END;

         -- Aggiornamento progressivo corrente per il sezionale in elaborazione

         CNRCTB120.upgSezionalePgCorrente(aCdCdsOrigine,
                                          aCdUoOrigine,
                                          aEsercizio,
                                          rigaSez.cd_tipo_sezionale,
                                          aProtocolloFT,
                                          aProtocolloNC,
                                          aProtocolloND,
                                          aProtocolloPG);

      END LOOP;

   END;

EXCEPTION

   WHEN NO_DATA_FOUND THEN
        IBMERR001.RAISE_ERR_GENERICO(errMsg);

END vsx_protocollazione_doc;


-- =================================================================================================
-- Lock dei documenti amministrativi oggetto di stampa
-- =================================================================================================
PROCEDURE LOCK_TABELLE
   (
    aPgCall VSX_RIF_PROTOCOLLO_IVA.pg_call%TYPE,
    p_tipo_documento_amm VSX_RIF_PROTOCOLLO_IVA.tipo_documento_amm%TYPE
   ) IS
   riga VSX_RIF_PROTOCOLLO_IVA%rowtype;

BEGIN

   FOR riga in

       (SELECT * FROM VSX_RIF_PROTOCOLLO_IVA RIF
        WHERE  RIF.PG_CALL = aPgCall AND
               RIF.TIPO_DOCUMENTO_AMM =CNRCTB100.TI_FATTURA_ATTIVA)

   LOOP

      CNRCTB100.LOCKDOCAMM(CNRCTB100.TI_FATTURA_ATTIVA,
                           riga.cd_Cds,
                           riga.esercizio,
                           riga.cd_unita_organizzativa,
                           riga.pg_fattura);


   END LOOP;

END LOCK_TABELLE;


   --=======================================================================================
   --============================ chkRif_Protocollo_Iva ====================================
   --=======================================================================================
   -- Funzione che restituisce 1 se la tabella VSX_RIF_PROTOCOLLO_IVA contiene almeno una riga per la sessione utente in esame
   --                          0 se la tabella VSX_RIF_PROTOCOLLO_IVA contiene 0 righe per la sessione utente in esame
   FUNCTION chkRif_Protocollo_Iva(aPgCall VSX_RIF_PROTOCOLLO_IVA.pg_call%TYPE,
                                  p_tipo_documento_amm VSX_RIF_PROTOCOLLO_IVA.tipo_documento_amm%TYPE) RETURN NUMBER IS
   controllo number;
   BEGIN
       controllo := 0;
       IF p_tipo_documento_amm='FATTURA_A' THEN
          SELECT count(*)
	        INTO controllo
            FROM VSX_RIF_PROTOCOLLO_IVA IVA,
                 FATTURA_ATTIVA     FAT
    	   WHERE IVA.PG_CALL     	   		=  aPgCall
	       AND   IVA.CD_CDS                 =  FAT.CD_CDS
		   AND   IVA.CD_UNITA_ORGANIZZATIVA =  FAT.CD_Unita_organizzativa
           AND   IVA.ESERCIZIO              =  FAT.ESERCIZIO
		   AND   IVA.PG_FATTURA             =  FAT.PG_FATTURA_ATTIVA
		   AND   UPPER(FAT.FL_STAMPA)       != 'Y';
	   END IF;
       IF controllo>0 THEN
          RETURN 1;
       ELSE
          RETURN 0;
       END IF;
   END;
   --====================================  Fine  ============================================

   --=======================================================================================
   --============================    chkParametriInput  ====================================
   --=======================================================================================
   -- Funzione che restituisce 1 il sezionale e presente
   --                          0 il sezionale non e presente
   FUNCTION chkTipoDocumento (aPgCall vsx_rif_protocollo_iva.pg_call%TYPE ) RETURN varchar2 IS
   pp_max_tipo_doc vsx_rif_protocollo_iva.TIPO_DOCUMENTO_AMM%TYPE;
   pp_min_tipo_doc vsx_rif_protocollo_iva.TIPO_DOCUMENTO_AMM%TYPE;
   BEGIN
   	  select min(TIPO_DOCUMENTO_AMM), max(TIPO_DOCUMENTO_AMM)
	  into pp_max_tipo_doc, pp_min_tipo_doc
	  from vsx_rif_protocollo_iva
	  where pg_call = aPgCall;

      IF (pp_max_tipo_doc = cnrctb100.ti_fattura_attiva) AND
	     (pp_min_tipo_doc = cnrctb100.ti_fattura_attiva) THEN
         RETURN pp_max_tipo_doc;
      ELSE
         RETURN '0';
      END IF;
   END;
   --====================================  Fine  ============================================

-- =================================================================================================
-- Ritorna il cds e la uo di orgine delle fatture in stampa e la stessa data di stampa
-- =================================================================================================
PROCEDURE getDatiBaseProtocolla
   (
    aPgCall VSX_RIF_PROTOCOLLO_IVA.pg_call%TYPE,
    aCdCdsOrigine IN OUT VARCHAR2,
    aCdUoOrigine IN OUT VARCHAR2,
    aDataStampa IN OUT DATE,
    aUtente IN OUT VARCHAR2
   ) IS

BEGIN

   SELECT DISTINCT B.cd_cds_origine, B.cd_uo_origine, TRUNC(A.dt_stampa), A.utcr
          INTO aCdCdsOrigine, aCdUoOrigine, aDataStampa, aUtente
   FROM   VSX_RIF_PROTOCOLLO_IVA A,
          FATTURA_ATTIVA B
   WHERE  A.pg_call = aPgCall AND
          B.cd_cds = A.CD_CDS AND
          B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
          B.esercizio = A.esercizio AND
          B.pg_fattura_attiva = A.pg_fattura AND
          UPPER(B.FL_STAMPA) = 'N';

END getDatiBaseProtocolla;


END CNRCTB150; -- PACKAGE END;


