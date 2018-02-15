--------------------------------------------------------
--  DDL for Package CNRCTB890
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB890" AS
--==================================================================================================
--
-- CNRCTB890 - package di utilità per gestione infrastruttura stampa
--
-- Date: 17/03/2004
-- Version: 1.0
--
-- Dependency: CNRCTB 015/100 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 17/03/2004
-- Version: 1.0
--
-- Creazione package
--
--==================================================================================================
--
-- Constants
--

   -- Stato PRINT_SPOOLER

   STATO_SPOOL_INCODA CONSTANT CHAR(1):='C';          -- in coda
   STATO_SPOOL_INESECUZIONE CONSTANT CHAR(1):='X';    -- in esecuzione
   STATO_SPOOL_ERRORE CONSTANT CHAR(1):='E';          -- in errore
   STATO_SPOOL_ESEGUITO CONSTANT CHAR(1):='S';        -- eseguita

   -- Tipologia visibilità PRINT_SPOOLER

   STM_VISIBILE_UTENTE CONSTANT CHAR(1):='U';          -- visibilità utente
   STM_VISIBILE_CDR CONSTANT CHAR(1):='C';             -- visibilità CdR
   STM_VISIBILE_UO CONSTANT CHAR(1):='O';              -- visibilità unità organizzativa
   STM_VISIBILE_CDS CONSTANT CHAR(1):='S';             -- visibilità Cds
   STM_VISIBILE_TUTTI CONSTANT CHAR(1):='P';           -- visibilità pubblica
   STM_VISIBILE_CNR CONSTANT CHAR(1):='N';             -- visibilità CNR

   -- Parte fissa del nome parametro in PRINT_SPOOLER_PARAM

   NOME_PARAMETRO_BASE CONSTANT VARCHAR2(6):='prompt';

   -- Variabili globali

--
-- Functions e Procedures
--

-- Ritorna un record della tabella PRINT_PRIORITY

   FUNCTION getPrintPriority
      (
       aReportName VARCHAR2
      ) RETURN PRINT_PRIORITY%ROWTYPE;

-- Inserimento in PRINT_SPOOLER

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
      );

-- Inserimento in PRINT_SPOOLER_PARAM

   PROCEDURE insPrintSpoolerParam
      (
       aPgStampa NUMBER,
       aNumParam NUMBER,
       aValoreParam VARCHAR2,
       aUser VARCHAR2,
       aTSNow DATE
      );
PROCEDURE insPrintSpoolerParam
   (
    aPgStampa NUMBER,
    aNomeParam  VARCHAR2,
    aTipoParam VARCHAR2,
    aValoreParam VARCHAR2,
    aUser VARCHAR2,
    aTSNow DATE
   );
END CNRCTB890;
