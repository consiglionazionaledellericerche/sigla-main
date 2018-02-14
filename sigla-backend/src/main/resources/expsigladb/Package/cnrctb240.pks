CREATE OR REPLACE package CNRCTB240 as
--==================================================================================================
--
-- CNRCTB240 - Package per la gestione delle liquidazione da Interfaccia
--
-- Date: 15/09/2003
-- Version: 1.8
--
-- Dependency: CNRCTB  015/250/255/260/265/270 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 03/03/2003
-- Version: 1.0
-- Creazione
--
-- Date: 11/03/2003
-- Version: 1.1
-- Modificata selezione delle uo di appartenenza
--
-- Date: 12/03/2003
-- Version: 1.2
-- Modificato il msg di errore per la mancanza di liquidazione uo da interfaccia
--
-- Date: 13/03/2003
-- Version: 1.3
-- Inserito calcolo della liquidazione provvisoria.
--
-- Date: 14/03/2003
-- Version: 1.4
-- Inserito controllo bloccante sul calcolo della liquidazione def/prov
-- nel caso in cui risulti gi? effettualta la liquidazione definitiva
-- non ? possibile calcolare la liquidazione DEF/PROV ma solo la RISTAMPA.
--
-- Date: 18/03/2003
-- Version: 1.5
-- Inserito chiusura cursore nelle exception
--
-- Date: 20/03/2003
-- Version: 1.6
--
-- Revisione controlli in lancio liquidazione iva da interfaccia e modifica per cursore esplicito
--
-- Date: 21/03/2003
-- Version: 1.7
--
-- Inserito controllo se non vi ? nulla da elaborare
--
-- Date: 15/09/2003
-- Version: 1.8
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata ? fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
--==================================================================================================
--
--
-- Constants:

   -- Tipi liquidazione IVA

   -- Commerciale
   TI_LIQ_IVA_COMMERC CONSTANT CHAR(1) :='C';
   -- Istituzionale intraue
   TI_LIQ_IVA_ISTINTR CONSTANT CHAR(1) :='I';
   -- San Marino senz'iva
   TI_LIQ_IVA_ISTSMSI CONSTANT CHAR(1) :='S';

--    K_PRIMA_GSTIVA CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'GESTIONE_IVA';
--    K_SECONDA_PRORATA_GSTIVA CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'ATTIVA_PRORATA';

--   TI_LIQUIDAZIONE CONSTANT VARCHAR2(50) :='LIQUIDAZIONE';

-- Variabili globali

   TYPE GenericCurTyp IS REF CURSOR;
--
-- Functions e Procedures:
--

-- Calcola la liquidazione IVA

   PROCEDURE elaboraLiquidInterf
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoStampa VARCHAR2,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aRistampa VARCHAR2,
       aRepID INTEGER,
       msg_out IN OUT VARCHAR2,
       aUtente VARCHAR2,
       aGruppoReport VARCHAR2,
       aEsercizioReale NUMBER,
       aTipoReportStato VARCHAR2
      );

   PROCEDURE preparaLiquidInterf
      (
       aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE,
       aLiquidazioneInterf LIQUID_IVA_INTERF%ROWTYPE,
       aTipoReport VARCHAR2,
       aTipoRegistro CHAR,
       aEsercizioReale NUMBER
      );

   PROCEDURE calcolaLiquidInterf
      (
       aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE,
       aLiquidazioneInterf LIQUID_IVA_INTERF%ROWTYPE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aGruppoStm CHAR,
       aGestioneProrata CHAR,
       aEsercizioReale NUMBER
      );

   PROCEDURE segnaLiquidazione
      (
       aLiquidazioneInterf LIQUID_IVA_INTERF%ROWTYPE,
       aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE,
       aEsercizioReale NUMBER
      );

END;


CREATE OR REPLACE package body CNRCTB240 is

-- =================================================================================================
-- Calcolo della Liquidazione IVA
--- ================================================================================================
PROCEDURE ElaboraLiquidInterf
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoStampa VARCHAR2,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    aRistampa VARCHAR2,
    aRepID INTEGER,
    msg_out IN OUT VARCHAR2,
    aUtente VARCHAR2,
    aGruppoReport VARCHAR2,
    aEsercizioReale NUMBER,
    aTipoReportStato VARCHAR2
   ) IS
   lRecUnitaOrganizzativa UNITA_ORGANIZZATIVA%ROWTYPE;
   lRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE;
   lRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE;
   lLiquidInterf LIQUID_IVA_INTERF%ROWTYPE;
   lCdCdsInterf liquid_iva_interf_cds%ROWTYPE;

   isErrore INTEGER;
   aContaLiquidazioni INTEGER;
   lStatement VARCHAR2(2000);
   flFattaLiquidazione CHAR(1);

   aCdCdsAltro VARCHAR2(30);
   aCdUoAltro VARCHAR2(30);
   lEseguiLiquidazione boolean;
   lDataOdierna date;
   lPgCaricamento number;
   lNumTotUO number;
   lNumUOPresenti number;
   flEsistePeriodo INTEGER;
   flEsistePeriodoPrecedente INTEGER;
   flEsistePeriodoPrima INTEGER;
   flEsistePeriodoDopo INTEGER;

   lLiquidIvaDefElaborata number;
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(aEsercizio);
   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   lDataOdierna := sysdate;

   -- L'attributo LIQUID_IVA_INTERF.pg_caricamento ? posto uguale a 0 (ZERO)

   lPgCaricamento:=0;
   flFattaLiquidazione:='N';

   -------------------------------------------------------------------------------------------------
   -- Controlli esecuzione

   -- Si verifica che in interfaccia siano presenti tutte le UO di un dato CDS e viceversa. Il controllo
   -- ? eseguito solo in caso di liquidazione definitiva in quanto quella provvisoria ? riferita ad una
   -- sola riga di interfaccia.

   BEGIN

      isErrore:=0;

      IF aTipoReport = 'D' THEN

         SELECT COUNT(*) INTO isErrore
         FROM   DUAL
         WHERE  EXISTS
                (
                    SELECT 1
                    FROM   LIQUID_IVA_INTERF A
                    WHERE  A.pg_caricamento = lPgCaricamento AND
                           A.cd_cds = aCdCdsOrigine AND
                           A.esercizio = aEsercizioReale AND
                           A.dt_inizio = aDataInizio AND
                           A.dt_fine = aDataFine AND
                           NOT EXISTS
                               (SELECT 1
                                FROM   V_UNITA_ORGANIZZATIVA_VALIDA B
                                WHERE  B.esercizio = aEsercizio AND
                                       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                                       B.fl_cds = 'N')
                    UNION ALL
                    SELECT 1
                    FROM   V_UNITA_ORGANIZZATIVA_VALIDA B
                    WHERE  B.esercizio = aEsercizio AND
                           B.cd_unita_padre = aCdCdsOrigine AND
                           B.fl_cds = 'N' AND
                           NOT EXISTS
                               (SELECT 1
                                FROM   LIQUID_IVA_INTERF A
                                WHERE  A.pg_caricamento = lPgCaricamento AND
                                       A.cd_cds = B.cd_unita_padre AND
                                       A.esercizio = aEsercizioReale AND
                                       A.cd_unita_organizzativa = B.cd_unita_organizzativa AND
                                       A.dt_inizio = aDataInizio AND
                                       A.dt_fine = aDataFine)
                );

         IF isErrore = 1 THEN
   	     IBMERR001.RAISE_ERR_GENERICO
                ('Non tutte le UO del cds ' || aCdCdsOrigine || ' sono presenti in interfaccia o viceversa');
         END IF;

         -- Controllo che in interfaccia ci siano, per ogni UO, le tre tipologie di liquidazione previste

         BEGIN

            SELECT DISTINCT COUNT(*) INTO aContaLiquidazioni
            FROM   LIQUID_IVA_INTERF
            WHERE  pg_caricamento = lPgCaricamento AND
                   cd_cds = aCdCdsOrigine AND
                   esercizio = aEsercizioReale AND
                   dt_inizio = aDataInizio AND
                   dt_fine = aDataFine
            GROUP BY cd_unita_organizzativa;

            IF aContaLiquidazioni != 3 THEN
   	       IBMERR001.RAISE_ERR_GENERICO
                  ('Nella tabella di interfaccia non sono presenti tutte e tre le tipologie di liquidazione ' ||
                   'per il cds ' || aCdCdsOrigine);
            END IF;

         EXCEPTION

            WHEN others THEN
   	         IBMERR001.RAISE_ERR_GENERICO
                    ('Nella tabella di interfaccia non sono presenti, per tutte le UO, le tre tipologie di ' ||
                     'liquidazione per il cds ' || aCdCdsOrigine);

         END;

      END IF;

      -- Controllo che non sia stata fatta la liquidazione del centro

      aCdCdsAltro:=UOENTE.cd_unita_padre;
      aCdUoAltro:=UOENTE.cd_unita_organizzativa;

      CNRCTB255.getStatoReportStatoNoGruppo(aCdCdsAltro,
                                            aCdUoAltro,
                                            aCdTipoSezionale,
                                            aDataInizio,
                                            aDataFine,
                                            aTipoReportStato,
                                            flEsistePeriodo,
                                            flEsistePeriodoPrecedente,
                                            flEsistePeriodoPrima,
                                            flEsistePeriodoDopo);

      IF flEsistePeriodo = 1 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('La liquidazione non ? ammessa in quanto esiste gi? la liquidazione del centro in stato ' ||
             'definitivo per il periodo richiesto');
      END IF;

      -- Non esiste una entrata per il periodo precedente. Se vi sono comunque delle entrate sulla tabella
      -- REPORT_STATO si solleva errore.
      -- In questo caso, allo stato attuale, non si distingue se le entrate trovate sono future.

      IF (flEsistePeriodoPrecedente = 0 AND
          (flEsistePeriodoPrima = 1 OR
           flEsistePeriodoDopo = 1)) THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('La liquidazione non ? ammessa in quanto la liquidazione centro, pur non risultando ' ||
             'presente in stato definitivo per il periodo precedente, ? definita  in altri periodi contabili');
      END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Cancellazione della riga di liquidazione inserita dalla procedura

   DELETE liquidazione_iva
   WHERE  CD_CDS = aCdCdsOrigine
	  and    ESERCIZIO = aEsercizio
	  and    CD_UNITA_ORGANIZZATIVA = aCdUoOrigine
	  and    TIPO_LIQUIDAZIONE = aGruppoReport
	  and    DT_INIZIO =  aDataInizio
	  and    DT_FINE   = aDataFine
	  and    REPORT_ID = aRepId;

   -------------------------------------------------------------------------------------------------
   -- Ciclo di processo delle liquidazioni in interfaccia

   IF aTipoReport = 'D' THEN

      lStatement := 'select * ' ||
                    'from   liquid_iva_interf ' ||
                    'where  pg_caricamento = ' || lPgCaricamento || ' AND ' ||
                           'cd_cds = ' || '''' || aCdCdsOrigine || '''' || ' AND ' ||
                           'esercizio = ' || aEsercizioReale || ' AND ' ||
                           'dt_inizio = ' || IBMUTL001.ASDYNDATE(aDataInizio) || ' AND ' ||
                           'dt_fine = ' || IBMUTL001.ASDYNDATE(aDataFine) || ' AND ' ||
                           'fl_gia_eleborata = ''N'' ';

   ELSE

      lStatement := 'select * ' ||
                    'from   liquid_iva_interf ' ||
                    'where  pg_caricamento = ' || lPgCaricamento || ' AND ' ||
                           'cd_cds = ' || '''' || aCdCdsOrigine || '''' || ' AND ' ||
                           'esercizio = ' || aEsercizioReale || ' AND ' ||
                           'cd_unita_organizzativa = ' || '''' || aCdUoOrigine || '''' || ' AND ' ||
                           'ti_liquidazione = ' || '''' || aGruppoReport || '''' || ' AND ' ||
                           'dt_inizio = ' || IBMUTL001.ASDYNDATE(aDataInizio) || ' AND ' ||
                           'dt_fine = ' || IBMUTL001.ASDYNDATE(aDataFine) || ' AND ' ||
                           'fl_gia_eleborata = ''N'' ';

   END IF;

   BEGIN

      OPEN gen_cur FOR
           lStatement;

      LOOP

         FETCH gen_cur INTO
               lLiquidInterf;

         EXIT WHEN gen_cur%NOTFOUND;

         flFattaLiquidazione:='Y';

         -- Azzera il record della liquidazione di default e valorizza gli elementi recuperati da lLiquidInterf

         lRecLiquidazioneIvaBase:=NULL;

         lRecLiquidazioneIvaBase.cd_cds:= lLiquidInterf.cd_cds;
         lRecLiquidazioneIvaBase.cd_unita_organizzativa:=lLiquidInterf.cd_unita_organizzativa;
         lRecLiquidazioneIvaBase.esercizio:=aEsercizio;
         lRecLiquidazioneIvaBase.dt_inizio:=aDataInizio;
         lRecLiquidazioneIvaBase.dt_fine:=aDataFine;
         lRecLiquidazioneIvaBase.report_id:=aRepID;
         lRecLiquidazioneIvaBase.tipo_liquidazione := lLiquidInterf.ti_liquidazione;
         lRecLiquidazioneIvaBase.stato:= aTipoReport;
         lRecLiquidazioneIvaBase.iva_vendite:=0;
         lRecLiquidazioneIvaBase.iva_vendite_diff:=0;
         lRecLiquidazioneIvaBase.iva_vend_diff_esig:=0;
         lRecLiquidazioneIvaBase.iva_autofatt:=0;
         lRecLiquidazioneIvaBase.iva_intraue:=0;
         lRecLiquidazioneIvaBase.iva_debito:=0;
         lRecLiquidazioneIvaBase.iva_acquisti:=0;
         lRecLiquidazioneIvaBase.iva_acq_non_detr:=0;
         lRecLiquidazioneIvaBase.iva_acquisti_diff:=0;
         lRecLiquidazioneIvaBase.iva_acq_diff_esig:=0;
         lRecLiquidazioneIvaBase.iva_credito:=0;
         lRecLiquidazioneIvaBase.var_imp_per_prec:=0;
         lRecLiquidazioneIvaBase.iva_non_vers_per_prec:=0;
         lRecLiquidazioneIvaBase.iva_deb_cred_per_prec:=0;
         lRecLiquidazioneIvaBase.cred_iva_comp_detr:=0;
         lRecLiquidazioneIvaBase.iva_deb_cred:=0;
         lRecLiquidazioneIvaBase.int_deb_liq_trim:=0;
         lRecLiquidazioneIvaBase.cred_iva_spec_detr:=0;
         lRecLiquidazioneIvaBase.acconto_iva_vers:=0;
         lRecLiquidazioneIvaBase.iva_da_versare:=0;
         lRecLiquidazioneIvaBase.iva_versata:=0;
         lRecLiquidazioneIvaBase.cred_iva_infrann_rimb:=0;
         lRecLiquidazioneIvaBase.cred_iva_infrann_comp:=0;
         lRecLiquidazioneIvaBase.iva_credito_no_prorata:=0;
         lRecLiquidazioneIvaBase.perc_prorata_detraibile:=0;
         lRecLiquidazioneIvaBase.dacr:=lDataOdierna;
         lRecLiquidazioneIvaBase.utcr:=aUtente;
         lRecLiquidazioneIvaBase.duva:=lDataOdierna;
         lRecLiquidazioneIvaBase.utuv:=aUtente;
         lRecLiquidazioneIvaBase.pg_ver_rec:=1;
         lRecLiquidazioneIvaBase.iva_liq_esterna:=0;

         -- La liquidazione pu? essere eseguita se :
         -- 1) non esistono liquidazioni precedenti e successive
         -- 2) esiste la liquidazione del periodo precedente ma non del periodo successivo

         CNRCTB255.getStatoReportStatoSiCdSez(lRecLiquidazioneIvaBase.cd_cds,
                                              lRecLiquidazioneIvaBase.cd_unita_organizzativa,
                                              aCdTipoSezionale,
                                              lRecLiquidazioneIvaBase.dt_inizio,
                                              lRecLiquidazioneIvaBase.dt_fine,
                                              lRecLiquidazioneIvaBase.tipo_liquidazione,
                                              aTipoReportStato,
                                              flEsistePeriodo,
                                              flEsistePeriodoPrecedente,
                                              flEsistePeriodoPrima,
                                              flEsistePeriodoDopo);

         IF aTipoReport = 'P' then

            IF flEsistePeriodo = 1 THEN
               IBMERR001.RAISE_ERR_GENERICO
	          ('La liquidazione per la UO '|| lLiquidInterf.cd_unita_organizzativa ||' risulta essere in stato definitivo per il periodo richiesto. ');
            END IF;

         ELSE

            IF flEsistePeriodo = 1 THEN
               IBMERR001.RAISE_ERR_GENERICO
                  ('La liquidazione risulta essere in stato definitivo per il periodo richiesto');
            END IF;

            -- Non esiste una entrata per il periodo precedente. Se vi sono comunque delle entrate sulla tabella
            -- REPORT_STATO si solleva errore.
            -- In questo caso, allo stato attuale, non si distingue se le entrate trovate sono future.

            IF (flEsistePeriodoPrecedente = 0 AND
                (flEsistePeriodoPrima = 1 OR
                 flEsistePeriodoDopo = 1)) THEN
               IBMERR001.RAISE_ERR_GENERICO
                  ('La liquidazione non risulta presente in stato definitivo per il periodo precedente ' ||
                   'mentre esistono altri periodi valorizzati');
            END IF;

         END IF;

         -- Creo lo liquidazione vuota, viene inserita una riga in LIQUIDAZIONE IVA

         CNRCTB265.insLiquidazioneIva(lRecLiquidazioneIvaBase);

         -- Elabora il calcolo della liquidazione

         PreparaLiquidInterf(lRecLiquidazioneIvaBase,
                             lLiquidInterf,
                             aTipoReport,
                             aTipoRegistro,
                             aEsercizioReale);

         -- Scrittura record di liquidazione definitivo

         IF aTipoReport = 'D' then

            cnrctb250.liquidazioneDefinitiva(lRecLiquidazioneIvaBase.cd_cds,
                                             lRecLiquidazioneIvaBase.cd_unita_organizzativa,
                                             lRecLiquidazioneIvaBase.Esercizio,
                                             aCdTipoSezionale,
                                             lRecLiquidazioneIvaBase.dt_inizio,
                                             lRecLiquidazioneIvaBase.dt_fine,
                                             aTipoStampa,
                                             aTipoRegistro,
                                             aTipoReport,
                                             aRistampa,
                                             lRecLiquidazioneIvaBase.report_id,
                                             lRecLiquidazioneIvaBase.utuv,
                                             lRecLiquidazioneIvaBase.tipo_liquidazione,
                                             aTipoReportStato);

            BEGIN

               SELECT * INTO   lRecLiquidazioneIva
               FROM   LIQUIDAZIONE_IVA A
               WHERE  A.cd_cds = lRecLiquidazioneIvaBase.cd_cds AND
                      A.cd_unita_organizzativa = lRecLiquidazioneIvaBase.cd_unita_organizzativa AND
                      A.esercizio = lRecLiquidazioneIvaBase.Esercizio AND
                      A.dt_inizio = lRecLiquidazioneIvaBase.Dt_Inizio AND
                      A.dt_fine = lRecLiquidazioneIvaBase.Dt_Fine AND
                      A.tipo_liquidazione = lRecLiquidazioneIvaBase.Tipo_Liquidazione AND
                      A.report_id = 0;

            EXCEPTION

               WHEN NO_DATA_FOUND THEN
                    IBMERR001.RAISE_ERR_GENERICO
                        ('Liquidazione iva del mese di ' || TO_CHAR(aDataInizio,'mm') || ' non presente');

            END;

            -- Contabilizzazione della liquidazione

            CNRCTB270.contabilLiquidIva(lRecLiquidazioneIva.cd_cds,
                                        lRecLiquidazioneIva.Esercizio,
                                        lRecLiquidazioneIva.cd_unita_organizzativa,
                                        lRecLiquidazioneIva.Dt_Inizio,
                                        lRecLiquidazioneIva.Dt_Fine,
                                        lRecLiquidazioneIva.utuv,
                                        lRecLiquidazioneIva.Tipo_Liquidazione);

            -- Elimino quanto registrato sulla tabella di appoggio nel calcolo della liquidazione

            DELETE FROM REPORT_GENERICO
            WHERE  ID = aRepId;

         END IF;

      END LOOP;

      CLOSE gen_cur;

      IF flFattaLiquidazione = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO
           ('Non ? presente alcun record di liquidazione iva interfaccia in stato da elaborare');
      END IF;

   END;


END ElaboraLiquidInterf;

-- =================================================================================================
-- Questa procedura si occupa di valorizzare opportunamente le tabelle report_generico e liquidazione_iva
-- =================================================================================================
PROCEDURE PreparaLiquidInterf
   (
    aRecLiquidazioneIvaBase liquidazione_iva%rowtype,
    aLiquidazioneInterf liquid_iva_interf%rowtype,
    aTipoReport VARCHAR2,
    aTipoRegistro CHAR,
    aEsercizioReale NUMBER
   ) IS
   lPasso NUMBER;
   lGruppoStm CHAR(1);
   lSottoGruppoStm CHAR(1);
   lDescrizioneGruppoStm CHAR(100);
   lGestioneProrata char(1);

BEGIN

   -------------------------------------------------------------------------------------------------
   --  Ciclo per l'esecuzione delle query di calcolo della Liquidazione IVA

   FOR lPasso IN 1 .. 3

   LOOP

      IF    lPasso = 1 THEN
            lGruppoStm:='A';
            lSottoGruppoStm:='A';
            lDescrizioneGruppoStm:='CORPO GRUPPO A';
      ELSIF lPasso = 2 THEN
            lGruppoStm:='A';
            lSottoGruppoStm:='A';
            lDescrizioneGruppoStm:='CORPO GRUPPO A';
      ELSIF lPasso = 3 THEN
            lGruppoStm:='A';
            lSottoGruppoStm:='A';
            lDescrizioneGruppoStm:='CORPO GRUPPO A';
      END IF;

      ----------------------------------------------------------------------------------------------
      -- Lettura dati da liquidazione e creazione record di appoggio su REPORT_GENERICO

      IF    lPasso = 1 THEN
            CNRCTB260.letturaLiquidazione(aRecLiquidazioneIvaBase.report_id,
                                          aRecLiquidazioneIvaBase.cd_cds,
                                          aRecLiquidazioneIvaBase.esercizio,
                                          aRecLiquidazioneIvaBase.cd_unita_organizzativa,
                                          aRecLiquidazioneIvaBase.dt_inizio,
                                          aRecLiquidazioneIvaBase.dt_fine,
                                          aTipoRegistro,
                                          aTipoReport,
                                          lGruppoStm,
                                          lSottoGruppoStm,
                                          lDescrizioneGruppoStm,
                                          aRecLiquidazioneIvaBase.Tipo_Liquidazione);

      ----------------------------------------------------------------------------------------------
      -- Calcolo importi per la liquidazione distinguo tra liquidazione dell'ente o altro

      ELSIF lPasso = 2 THEN

            -- Calcola liquidazione ente -----------------------------------------------------------

            IF aRecLiquidazioneIvaBase.cd_cds = cnrctb020.getCDCDSENTE(aEsercizioReale) THEN
               IBMERR001.RAISE_ERR_GENERICO ('Il CDS ' || aRecLiquidazioneIvaBase.cd_cds ||
                                             ' non valido, non esiste nella tabella di interfaccia');

            ELSE

               -- Calcola liquidazione UO -------------------------------------------------------------

               BEGIN

                  lGestioneProrata := CNRCTB015.getVal01PerChiave(aEsercizioReale,
                                                                  CNRCTB250.K_PRIMA_GSTIVA,
                                                                  CNRCTB250.K_SECONDA_PRORATA_GSTIVA);
                  calcolaLiquidInterf(aRecLiquidazioneIvaBase,
                                      aLiquidazioneInterf,
                                      aTipoRegistro,
                                      aTipoReport,
                                      lGruppoStm,
                                      lGestioneProrata,
                                      aEsercizioReale);

               END;

            END IF;

      -------------------------------------------------------------------------------------------
      -- Scrittura su LIQUIDAZIONE_IVA dei dati di liquidazione IVA calcolati

      ELSIF lPasso = 3 THEN

            CNRCTB260.scritturaLiquidazione(aRecLiquidazioneIvaBase.report_id,
                                            aRecLiquidazioneIvaBase.cd_cds,
                                            aRecLiquidazioneIvaBase.esercizio,
                                            aRecLiquidazioneIvaBase.cd_unita_organizzativa,
                                            aRecLiquidazioneIvaBase.dt_inizio,
                                            aRecLiquidazioneIvaBase.dt_fine,
                                            aTipoRegistro,
                                            aTipoReport,
                                            lGruppoStm,
                                            lSottoGruppoStm,
                                            lDescrizioneGruppoStm,
                                            aRecLiquidazioneIvaBase.tipo_Liquidazione);

            IF aTipoReport = 'D' THEN
               segnaLiquidazione(aLiquidazioneInterf,
                                 aRecLiquidazioneIvaBase,
                                 aEsercizioReale);
            END IF;

      END IF;

   END LOOP;

END PreparaLiquidInterf;

-- =================================================================================================
-- Calcolo della liquidazione IVA da interfaccia
-- =================================================================================================
PROCEDURE calcolaLiquidInterf
   (
    aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE,
    aLiquidazioneInterf LIQUID_IVA_INTERF%ROWTYPE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    aGruppoStm CHAR,
    aGestioneProrata CHAR,
    aEsercizioReale NUMBER
   ) IS

   aDataInizioLiqPrec DATE;
   aDataFineLiqPrec DATE;
   aEsercizioFineLiqPrec NUMBER(4);
   aCreDebPrecedente NUMBER(15,2);
   aIvaCreditoPeriodoPrec NUMBER(15,2);
   aPercentualeProrata NUMBER(15,2);
   aEsercizioPerProrata NUMBER(4);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Lettura dell'eventuale credito/debito dalla precedente liquidazione.
   -- Si legge il campo LIQUIDAZIONE_IVA.iva_da_versare della liquidazione definitiva precedente al
   -- periodo in gestione. Si mantiene il campo solo se positivo (credito) o negativo <= 26 euro
   -- (debito non versato)

   aDataFineLiqPrec:= aRecLiquidazioneIvaBase.dt_inizio - 1;
   aDataInizioLiqPrec:=IBMUTL001.getFirstDayOfMonth(aDataFineLiqPrec);
   aEsercizioFineLiqPrec:=TO_NUMBER(TO_CHAR(aDataFineLiqPrec,'YYYY'));

   -- L'esercizio ? corretto per gestire il fatto che il mese di dicembre ? sempre liquidato
   -- nell'esercizio successivo

   IF TO_CHAR(aDataFineLiqPrec,'MM') = '12' THEN
      aEsercizioFineLiqPrec:=aEsercizioFineLiqPrec + 1;
   END IF;

   BEGIN

      SELECT NVL(iva_da_versare,0) INTO aCreDebPrecedente
      FROM   LIQUIDAZIONE_IVA
      WHERE  cd_cds = aRecLiquidazioneIvaBase.cd_cds AND
             esercizio = aEsercizioFineLiqPrec AND
             cd_unita_organizzativa = aRecLiquidazioneIvaBase.cd_unita_organizzativa AND
             tipo_liquidazione = aRecLiquidazioneIvaBase.tipo_liquidazione AND
             dt_inizio = aDataInizioLiqPrec AND
             dt_fine = aDataFineLiqPrec AND
             stato = 'D';

   EXCEPTION

      WHEN NO_DATA_FOUND THEN
           aCreDebPrecedente:=0;

   END;

   aIvaCreditoPeriodoPrec:=0;

   -- Controllo del valore del credito del periodo precedente. L'eventuale debito inferiore a 26 euro
   -- non ? memorizzato in quanto comunque versato dalle singole Uo.

   IF    aCreDebPrecedente > 0 THEN
         aIvaCreditoPeriodoPrec:=aCreDebPrecedente;
   END IF;

   -- modifica la riga inserita da leggi Liquidazione

   UPDATE VP_LIQUIDAZIONE_IVA
   SET    rp_iva_deb_cred_per_prec = aIvaCreditoPeriodoPrec,
          rp_iva_ven = aLiquidazioneInterf.iva_debito,
          rp_iva_acq = aLiquidazioneInterf.iva_credito
   WHERE  id_report = aRecLiquidazioneIvaBase.report_id AND
          chiave = aGruppoStm AND
          tipo = 'D' AND
          sequenza = 2;

   -------------------------------------------------------------------------------------------------
   -- Calcolo del prorata. Normalizzazione dell'esercizio di riferimento

   BEGIN

      aPercentualeProrata:=NULL;

      IF (aGestioneProrata = 'Y' AND
          aLiquidazioneInterf.ti_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC) THEN

         aEsercizioPerProrata:=(aEsercizioReale - 1);

         BEGIN

            SELECT percentuale INTO aPercentualeProrata
            FROM   PRO_RATA
            WHERE  esercizio = aEsercizioPerProrata AND
                   cd_attivita_commerciale = '*';

         EXCEPTION

            WHEN no_data_found THEN
                 aPercentualeProrata:=NULL;

         END;

      END IF;

      UPDATE VP_LIQUIDAZIONE_IVA
      SET    rp_gestione_prorata = aGestioneProrata,
             rp_esercizio_euro = 'Y',
             rp_perc_prorata_detraibile = aPercentualeProrata
      WHERE  id_report = aRecLiquidazioneIvaBase.report_id AND
             chiave = aGruppoStm AND
             tipo = 'D' AND
             sequenza = 2;

   END;

END calcolaLiquidInterf;

-- =================================================================================================
-- Valorizzazione ad elaborata dell'input dell'interfaccia
-- =================================================================================================
PROCEDURE segnaLiquidazione
   (
    aLiquidazioneInterf LIQUID_IVA_INTERF%ROWTYPE,
    aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE,
    aEsercizioReale NUMBER
   ) IS

BEGIN

    UPDATE LIQUID_IVA_INTERF
    SET    fl_gia_eleborata ='Y',
           utuv = aRecLiquidazioneIvaBase.utuv,
           duva = aRecLiquidazioneIvaBase.duva,
           pg_ver_rec = pg_ver_rec + 1
    WHERE  PG_CARICAMENTO = aLiquidazioneInterf.PG_CARICAMENTO AND
           CD_CDS = aLiquidazioneInterf.CD_CDS AND
           ESERCIZIO = aEsercizioReale AND
           CD_UNITA_ORGANIZZATIVA = aLiquidazioneInterf.CD_UNITA_ORGANIZZATIVA AND
           TI_LIQUIDAZIONE = aLiquidazioneInterf.TI_LIQUIDAZIONE AND
           DT_INIZIO = aLiquidazioneInterf.DT_INIZIO AND
           DT_FINE = aLiquidazioneInterf.DT_FINE;

END segnaLiquidazione;

END;


