--------------------------------------------------------
--  DDL for Package IBMUTL205
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL205" is
--
-- IBMUTL205 - Package di servizio per gestione dei messaggi agli utenti attraverso TOOL bframe message tool
-- Date: 28/01/2003
-- Version: 1.1
--
-- Gestione tabelle di log dei jobs applicativi
--
-- History:
-- Date: 02/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 28/01/2003
-- Version: 1.1
-- Fix minore
--
-- Constants:

-- Tipi di messaggi
TI_ERRORE CONSTANT VARCHAR2(5) := 'E';
TI_WARNING CONSTANT VARCHAR2(5) := 'W';
TI_INFO CONSTANT VARCHAR2(5) := 'I';

-- Utenza messaggi interna
MESSAGE_USER CONSTANT VARCHAR2(20) := '$$$$$MESSG_USER$$$$$';

-- Functions e Procedures:

-- Crea un'entry di log errore

 procedure logErr(
	 aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
	,aUser varchar2 default null
 );

-- Crea un'entry di log warning
 procedure logWar(
     aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
    ,aUser varchar2 default null
 );

-- Crea un'entry di log info
 procedure logInf(
     aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
    ,aUser varchar2 default null
 );


-- Procedure di inserimento MESSAGGIO
 procedure ins_MESSAGGIO (aDest MESSAGGIO%rowtype);

end;
