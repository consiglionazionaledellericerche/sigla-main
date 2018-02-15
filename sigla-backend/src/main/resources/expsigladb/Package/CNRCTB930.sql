--------------------------------------------------------
--  DDL for Package CNRCTB930
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB930" Is
-- =================================================================================================
-- Package per la gestione del 770
-- Versione 1.0 by Q.DIEGO
-- (testato 8 NOV 2004 by A.DAMICO)
--
-- Date: 22/09/2005
-- Version: 1.1
-- Adeguamento per il 770/2004
--
-- Date: 20/09/2006
-- Version: 1.2
-- Adeguamento per il 770/2006 - Aggiunta la quota esente INPS
--
-- Date: 11/07/2013
-- Version: 1.3
-- Adeguamento 770/2013 - Redditi 2012 (gestito il nuovo quadro)
--
-- Date: 22/07/2014
-- Version: 1.4
-- Adeguamento 770/2014 - Redditi 2013 (adeguato tracciato per il quadro SC)
-- =================================================================================================
   -- Costante tipo log per batch

   IDTIPOLOG CONSTANT VARCHAR2(20) := 'ESTRAZIONE_770';
   IDTIPOBLOB CONSTANT VARCHAR2(10) := 'ESTRAI_770';

-- Variabili globali

   dataOdierna DATE;
   mioCLOB CLOB;
   aRecBframeBlob BFRAME_BLOB%ROWTYPE;

  TYPE dsErroriRec IS RECORD
       (
        tStringaKey VARCHAR2(2000),
        tStringaErr VARCHAR2(2000)
       );

   TYPE dsErroriTab IS TABLE OF dsErroriRec
        INDEX BY BINARY_INTEGER;
   errori_tab dsErroriTab;

TYPE GenericCurType IS REF CURSOR;

PROCEDURE estrazione770
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inMsgError IN OUT VARCHAR2,
    inUtente VARCHAR2
   );
PROCEDURE estrazione770Interna
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2,
    pg_exec NUMBER
   );
PROCEDURE job_estrazione770
   (
    job NUMBER,
    pg_exec NUMBER,
    next_date DATE,
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inMsgError VARCHAR2,
    inUtente VARCHAR2
   );
PROCEDURE scriviFileQuadroSC
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2--,
    --aRecBframeBlob  BFRAME_BLOB%ROWTYPE,
    --inCLOB CLOB
   );

PROCEDURE scriviFileQuadroSF
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   );
PROCEDURE scriviFileQuadroSH
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   );
PROCEDURE scriviFileQuadroSY
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   );
   PROCEDURE scriviFileQuadroSCSY
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   );
End;
