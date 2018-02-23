--------------------------------------------------------
--  DDL for Package IBMUTL200
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL200" is
--
-- IBMUTL200 - Package di servizio per gestione logs dei JOBS applicativi e tabella di controllo BATCH
-- Date: 28/01/2003
-- Version: 1.6
--
-- Gestione tabelle di log dei jobs applicativi
--
-- History:
-- Date: 07/03/2002
-- Version: 1.0
-- Creazione
--
-- Date: 09/03/2002
-- Version: 1.1
-- Registrazione del numero di batch in BATCH_CONTROL allo start del log
--
-- Date: 11/03/2002
-- Version: 1.2
-- Riorganizzazione
--
-- Date: 20/06/2002
-- Version: 1.3
-- Modificato intervallo in creaBatchDinamico(): ora deve essere
-- specificato in secondi.
--
-- Date: 30/09/2002
-- Version: 1.4
-- Aggiunte le pragma options per transazione autonoma del log
--
-- Date: 11/10/2002
-- Version: 1.5
-- Fix per errore di non commit/rollback su transazioni autonome
--
-- Date: 28/01/2003
-- Version: 1.6
-- Introduzione del tipo catalogato di log
--
-- Constants:

MSG_REMOVE_BATCH CONSTANT VARCHAR2(200):='$$$$$RIMOZIONE_BATCH$$$$$';
MSG_DEREG_BATCH CONSTANT VARCHAR2(200):='$$$$$DEREGISTRAZIONE_BATCH$$$$$';
MSG_REG_BATCH CONSTANT VARCHAR2(200):='$$$$$REGISTRAZIONE_BATCH$$$$$';
MSG_REG_BATCH_DINAMICO CONSTANT VARCHAR2(200):='$$$$$REGISTRAZIONE_BATCH_DINAMICO$$$$$';

MSG_DEFAULT_HANDLER_ERRORI CONSTANT VARCHAR2(200):='$$$$$DEFAULT_HANDLER_ERRORI$$$$$';

-- Tipi di messaggi
TI_ERRORE CONSTANT VARCHAR2(5) := 'E';
TI_WARNING CONSTANT VARCHAR2(5) := 'W';
TI_INFO CONSTANT VARCHAR2(5) := 'I';

-- Tipi di LOG

TIPO_LOG_INDEFINITO CONSTANT VARCHAR2(20) := 'UNDEF00';

-- Utenza batch di default
BATCH_USER CONSTANT VARCHAR2(20) := '$$$$$BATCH_USER$$$$$';

-- Functions e Procedures:

-- Legge dalla testata del log per l'esecuzione corrente lo user

 function getUserFromLog(aPgExec number) return varchar2;

-- Apre una sessione di log
-- Restituisce l'identificativo univoco dell'esecuzione

 function logStart(
     aDesc varchar2
    ,aNota varchar2
    ,aUtcr varchar2
    ,aPgJob NUMBER
    ,aPgBatch number
 ) return number;

 function logStart(
     aDesc varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number;

 function logStart(
     aTipo varchar2
    ,aDesc varchar2
    ,aNota varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number;

 function logStart(
     aTipo varchar2
    ,aDesc varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number;

-- Dichiaro le PL/SQL table

type BATCH_LOG_TSTA_TEMP is table of BATCH_LOG_TSTA%rowtype index by binary_integer;
aBATCH_LOG_TSTA_TEMP  BATCH_LOG_TSTA_TEMP;
type BATCH_LOG_RIGA_TEMP is table of BATCH_LOG_RIGA%rowtype index by binary_integer;
aBATCH_LOG_RIGA_TEMP  BATCH_LOG_RIGA_TEMP;
aBATCH_LOG_RIGA_TEMP_da_ins  BATCH_LOG_RIGA_TEMP;

-- Identiche alle precedenti, ma scrivono in PL/SQL Table

-- Apre una sessione di log
-- Restituisce l'identificativo univoco dell'esecuzione

 function logStart_temp(
     aDesc varchar2
    ,aNota varchar2
    ,aUtcr varchar2
    ,aPgJob NUMBER
    ,aPgBatch number
 ) return number;

 function logStart_temp(
     aDesc varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number;

 function logStart_temp(
     aTipo varchar2
    ,aDesc varchar2
    ,aNota varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number;

 function logStart_temp(
     aTipo varchar2
    ,aDesc varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number;


-- Crea un'entry di log errore
procedure logErr(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 );

-- DUPLICATA PER INSERIMENTO IN TABELLE TEMP

procedure logErr_TEMP(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 );


 procedure logErr(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 );

-- Crea un'entry di log warning
 procedure logWar(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 );

 procedure logWar_TEMP(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 );

-- Crea un'entry di log info
 procedure logInf(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 );

-- DUPLICATO PER TABELLE TEMP

 procedure logInf_TEMP(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 );


-- Crea un'entry di log info
 procedure logInf(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 );

-- Procedure di inserimento testata e riga del log
 procedure ins_BATCH_LOG_TSTA (aDest BATCH_LOG_TSTA%rowtype);
 procedure ins_BATCH_LOG_RIGA (aDest BATCH_LOG_RIGA%rowtype);
 Procedure ins_BATCH_CONTROL (aDest BATCH_CONTROL%rowtype);

-- Procedura di inserimento scarti file cassiere
 Procedure ins_EXT_CASSIERE00_SCARTI (aDest EXT_CASSIERE00_SCARTI%rowtype);

-- Procedure di scaricamento delle PL/SQL Table nella testata e riga del log
 Procedure scarica_temp_in_BATCH_LOG_TSTA;
 procedure scarica_temp_in_BATCH_LOG_RIGA;

 Function get_count_batch_log_riga_temp Return NUMBER;

 Function get_err_batch_log_riga_temp Return Boolean;

end;
