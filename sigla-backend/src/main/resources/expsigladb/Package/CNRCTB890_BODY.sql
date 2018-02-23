--------------------------------------------------------
--  DDL for Package Body CNRCTB890
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB890" AS

-- =================================================================================================
-- Ritorna un record della tabella PRINT_PRIORITY
-- =================================================================================================
FUNCTION getPrintPriority
   (
    aReportName VARCHAR2
   ) RETURN PRINT_PRIORITY%ROWTYPE IS
   aRecPrintPriority PRINT_PRIORITY%ROWTYPE;

BEGIN

   SELECT * INTO aRecPrintPriority
   FROM   PRINT_PRIORITY
   WHERE  report_name =  aReportName;

   RETURN aRecPrintPriority;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Non trovata alcuna stampa con nome ' || aReportName);

END getPrintPriority;

-- =================================================================================================
-- Inserimento in PRINT_SPOOLER
-- =================================================================================================
PROCEDURE insPrintSpooler
   (
    aRecPrintSpooler PRINT_SPOOLER%ROWTYPE
   ) IS

BEGIN

   INSERT INTO PRINT_SPOOLER
          (PG_STAMPA,
           DT_SCADENZA,
           PRIORITA,
           DS_STAMPA,
           DS_UTENTE,
           REPORT,
           INTERVALLO_INIZIO,
           INTERVALLO_FINE,
           STATO,
           SERVER,
           PRIORITA_SERVER,
           NOME_FILE,
           TI_VISIBILITA,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           ID_REPORT_GENERICO,
           VISIBILITA,
           FL_EMAIL)
   VALUES (aRecPrintSpooler.pg_stampa,
           aRecPrintSpooler.dt_scadenza,
           aRecPrintSpooler.priorita,
           aRecPrintSpooler.ds_stampa,
           aRecPrintSpooler.ds_utente,
           aRecPrintSpooler.report,
           aRecPrintSpooler.intervallo_inizio,
           aRecPrintSpooler.intervallo_fine,
           aRecPrintSpooler.stato,
           aRecPrintSpooler.server,
           aRecPrintSpooler.priorita_server,
           aRecPrintSpooler.nome_file,
           aRecPrintSpooler.ti_visibilita,
           aRecPrintSpooler.dacr,
           aRecPrintSpooler.utcr,
           aRecPrintSpooler.duva,
           aRecPrintSpooler.utuv,
           aRecPrintSpooler.pg_ver_rec,
           aRecPrintSpooler.id_report_generico,
           aRecPrintSpooler.visibilita,
           aRecPrintSpooler.fl_email);

END insPrintSpooler;

-- =================================================================================================
-- Inserimento in PRINT_SPOOLER
-- =================================================================================================
PROCEDURE insPrintSpooler
   (
    aPgStampa NUMBER,
    aPriorita NUMBER,
    aDsStampa VARCHAR2,
    aReport VARCHAR2,
    aPrioritaServer NUMBER,
    aTiVisibilita VARCHAR2,
    aVisibilita VARCHAR2,
    aIdReportGenerico NUMBER,
    aUser VARCHAR2,
    aTSNow DATE
   ) IS

   aRecPrintSpooler PRINT_SPOOLER%ROWTYPE;

BEGIN

   aRecPrintSpooler:=NULL;
   aRecPrintSpooler.pg_stampa:=aPgStampa;
   aRecPrintSpooler.priorita:=aPriorita;
   aRecPrintSpooler.ds_stampa:=aDsStampa;
   aRecPrintSpooler.report:=aReport;
   aRecPrintSpooler.stato:=STATO_SPOOL_INCODA;
   aRecPrintSpooler.priorita_server:=aPrioritaServer;
   aRecPrintSpooler.ti_visibilita:=aTiVisibilita;
   aRecPrintSpooler.dacr:=aTSNow;
   aRecPrintSpooler.utcr:=aUser;
   aRecPrintSpooler.duva:=aTSNow;
   aRecPrintSpooler.utuv:=aUser;
   aRecPrintSpooler.pg_ver_rec:=1;
   aRecPrintSpooler.id_report_generico:=aIdReportGenerico;
   aRecPrintSpooler.visibilita:=aVisibilita;
   aRecPrintSpooler.fl_email :='N';
   insPrintSpooler(aRecPrintSpooler);

END insPrintSpooler;

-- =================================================================================================
-- Inserimento in PRINT_SPOOLER_PARAM
-- =================================================================================================
PROCEDURE insPrintSpoolerParam
   (
    aRecPrintSpoolerParam PRINT_SPOOLER_PARAM%ROWTYPE
   ) IS

BEGIN

   INSERT INTO PRINT_SPOOLER_PARAM
          (PG_STAMPA,
           NOME_PARAM,
           VALORE_PARAM,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           PARAM_TYPE)
   VALUES (aRecPrintSpoolerParam.pg_stampa,
           aRecPrintSpoolerParam.nome_param,
           aRecPrintSpoolerParam.valore_param,
           aRecPrintSpoolerParam.dacr,
           aRecPrintSpoolerParam.utcr,
           aRecPrintSpoolerParam.duva,
           aRecPrintSpoolerParam.utuv,
           aRecPrintSpoolerParam.pg_ver_rec,
           aRecPrintSpoolerParam.param_type);

END insPrintSpoolerParam;

-- =================================================================================================
-- Inserimento in PRINT_SPOOLER_PARAM
-- =================================================================================================
PROCEDURE insPrintSpoolerParam
   (
    aPgStampa NUMBER,
    aNumParam NUMBER,
    aValoreParam VARCHAR2,
    aUser VARCHAR2,
    aTSNow DATE
   ) IS

   aRecPrintSpoolerParam PRINT_SPOOLER_PARAM%ROWTYPE;

BEGIN

   aRecPrintSpoolerParam:=NULL;
   aRecPrintSpoolerParam.pg_stampa:=aPgStampa;
   aRecPrintSpoolerParam.nome_param:=NOME_PARAMETRO_BASE || aNumParam;
   aRecPrintSpoolerParam.valore_param:=aValoreParam;
   aRecPrintSpoolerParam.dacr:=aTSNow;
   aRecPrintSpoolerParam.utcr:=aUser;
   aRecPrintSpoolerParam.duva:=aTSNow;
   aRecPrintSpoolerParam.utuv:=aUser;
   aRecPrintSpoolerParam.pg_ver_rec:=1;

   insPrintSpoolerParam(aRecPrintSpoolerParam);

END insPrintSpoolerParam;

-- =================================================================================================
-- Inserimento in PRINT_SPOOLER_PARAM
-- =================================================================================================
PROCEDURE insPrintSpoolerParam
   (
    aPgStampa NUMBER,
    aNomeParam  VARCHAR2,
    aTipoParam VARCHAR2,
    aValoreParam VARCHAR2,
    aUser VARCHAR2,
    aTSNow DATE
   ) IS

   aRecPrintSpoolerParam PRINT_SPOOLER_PARAM%ROWTYPE;

BEGIN

   aRecPrintSpoolerParam:=NULL;
   aRecPrintSpoolerParam.pg_stampa:=aPgStampa;
   aRecPrintSpoolerParam.nome_param:=aNomeParam;
   aRecPrintSpoolerParam.valore_param:=aValoreParam;
   aRecPrintSpoolerParam.dacr:=aTSNow;
   aRecPrintSpoolerParam.utcr:=aUser;
   aRecPrintSpoolerParam.duva:=aTSNow;
   aRecPrintSpoolerParam.utuv:=aUser;
   aRecPrintSpoolerParam.pg_ver_rec:=1;
   aRecPrintSpoolerParam.param_type := aTipoParam;

   insPrintSpoolerParam(aRecPrintSpoolerParam);

END insPrintSpoolerParam;

END;
