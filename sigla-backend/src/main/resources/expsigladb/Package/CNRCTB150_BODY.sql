--------------------------------------------------------
--  DDL for Package Body CNRCTB150
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB150" IS
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
   aEsercizioFattura NUMBER(4);
   aPgFattura NUMBER;
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
                         aEsercizioFattura,
                         aPgFattura,
                         aDataStampa,
                         aUtente);

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   eseguiLock:='Y';
   data_operazione:=sysdate;
   aEsercizio:=TO_NUMBER(TO_CHAR(aDataStampa,'YYYY'));
   if aEsercizio != aEsercizioFattura THEN
            IBMERR001.RAISE_ERR_GENERICO
      ('L''anno di protocollazione ' || aEsercizio || ' risulta diverso dall''anno della fattura ' ||aEsercizioFattura||' per il progressivo '||aPgFattura);
   end if;
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
         -- al mese di un sezionale già stampato in modo definitivo

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
               ('Il sezionale ' || rigaSez.cd_tipo_sezionale || ' risulta già stampato in modo definitivo nel ' ||
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
         -- Nessun protocollo può valere -1

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
    aEsercizio IN OUT NUMBER,
    aPgFattura IN OUT NUMBER,
    aDataStampa IN OUT DATE,
    aUtente IN OUT VARCHAR2
   ) IS

BEGIN

   SELECT DISTINCT B.cd_cds_origine, B.cd_uo_origine, B.ESERCIZIO, B.pg_fattura_attiva, TRUNC(A.dt_stampa), A.utcr
          INTO aCdCdsOrigine, aCdUoOrigine, aEsercizio, aPgFattura, aDataStampa, aUtente
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
