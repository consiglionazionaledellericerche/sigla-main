--------------------------------------------------------
--  DDL for Package CNRCTB850
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB850" as
--
-- CNRCTB850 - Package di gestione del controllo di logging applicativo
-- Date: 13/01/2004
-- Version: 1.5
--
-- Dependency:
--
-- History:
--
-- Date: 08/09/2003
-- Version: 1.0
-- Creazione
--
-- Date: 22/09/2003
-- Version: 1.1
-- Aggiunto gestione id_clone e nuova procedura per eliminare tutti i record di CONTROLLO_ACCESSO_UTENTE
-- relativi ad un clone
--
-- Date: 22/09/2003
-- Version: 1.2
-- Ora getAndLock solleva una eccezione applicativa se il record in CONTROLLO_UTENTE è lockato
--
-- Date: 13/10/2003
-- Version: 1.3
-- register effettua un test su cd_cds: se nullo tenta di caricare il cd_cds_configuratore dell'utente
-- se l'utente ha '*' come cd_cds_configuratore usa il cds dell'ente
--
-- Date: 13/10/2003
-- Version: 1.4
-- Gestiti 3 tentativi di accesso al semaforo (CONTROLLO_ACCESSO)
--
-- Date: 13/01/2004
-- Version: 1.5
-- Corretto errore in unregisterAll (select into restituiva più di una riga)
--
-- Constants:
--
RESOURCE_BUSY EXCEPTION;

PRAGMA EXCEPTION_INIT(RESOURCE_BUSY,-54);

STATO_ACCESSIBILE CONSTANT CHAR(1) := 'A';
STATO_NON_ACCESSIBILE CONSTANT CHAR(1) := 'N';

 procedure register(aCdCds varchar2,aEs number,aCdUtente varchar2,aIdSessione varchar2,aIdClone varchar2);
 procedure unregister(aIdSessione varchar2);
 procedure unregisterAll(aIdClone varchar2);


-- Functions e Procedures:
 procedure ins_CONTROLLO_ACCESSO (aDest CONTROLLO_ACCESSO%rowtype);
 procedure ins_CONTROLLO_ACCESSO_UTENTE (aDest CONTROLLO_ACCESSO_UTENTE%rowtype);
end;
