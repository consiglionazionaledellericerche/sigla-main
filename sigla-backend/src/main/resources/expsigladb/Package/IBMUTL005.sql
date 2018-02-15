--------------------------------------------------------
--  DDL for Package IBMUTL005
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL005" as
--==================================================================================================
--
-- IBMUTL005 - Package di utilita per la gestione di C e BLOB in tabella BFRAME_BLOBS
--
-- Date: 05/02/2004
-- Version: 1.5
--
-- History:
--
-- Date: 01/06/2003
-- Version: 1.0
-- Creazione
--
-- Date: 04/06/2003
-- Version: 1.1
-- Aggiunto invoker callback di caricamento file
--
-- Date: 05/06/2003
-- Version: 1.2
-- Aggiunto callback su eliminazione file
--
-- Date: 08/09/2003
-- Version: 1.3
-- Fix errori in invocazione callback di caricamento bframe_blob
--
-- Date: 06/10/2003
-- Version: 1.4
-- Gestione dell'errore nel caso di file non compatibile.
--
-- Date: 05/02/2004
-- Version: 1.5
-- Inserita gestione Blob per il cud
--
-- Constants:
gStatoErr constant char(1) := 'E';
gStatoIni constant char(1) := 'X';
gStatoEse constant char(1) := 'S';
--
-- Functions e Procedures:
--
--
 function openClob(aTipo varchar2, aPath varchar2, aFileName varchar2) return clob;
 function openClobForWrite(aTipo varchar2, aPath varchar2, aFileName varchar2) return clob;
 function nextLine(aClob clob, aSPos IN OUT integer, aString OUT varchar2) return integer;
 procedure putLine(aCLob IN OUT clob, aString IN varchar2);
 procedure closeClob(aClob IN OUT clob);

 procedure updateCallback(aBframeBlob in out BFRAME_BLOB%rowtype);
 procedure deleteCallback(aBframeBlob in out BFRAME_BLOB%rowtype);
 -- -------------------------------------------------------------- --
 procedure ShIniCBlob(aTipo BFRAME_BLOB.CD_TIPO%type,
 		   		   aPath BFRAME_BLOB.PATH%type,
				   aFilename BFRAME_BLOB.FILENAME%type,
				   aTiVisibilita BFRAME_BLOB.TI_VISIBILITA%type,
				   aDsFile BFRAME_BLOB.DS_FILE%type,
				   aDsUtente BFRAME_BLOB.DS_UTENTE%type,
				   aUtente varchar2,
				   aCBlob in out clob);
 -- -------------------------------------------------------------- --
 procedure ShPutLine(aTipo BFRAME_BLOB.CD_TIPO%type,
 		   		     aPath BFRAME_BLOB.PATH%type,
				     aFilename BFRAME_BLOB.FILENAME%type,
				     aCLob IN OUT clob, aString IN varchar2);
 -- -------------------------------------------------------------- --
 procedure ShCloseClob(aTipo BFRAME_BLOB.CD_TIPO%type,
 		   		       aPath BFRAME_BLOB.PATH%type,
				       aFilename BFRAME_BLOB.FILENAME%type,
					   aClob IN OUT clob);
end;
