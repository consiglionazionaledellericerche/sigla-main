--------------------------------------------------------
--  DDL for Package IBMUTL050
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL050" as
--
-- CNRCTB000 - Package contenente funzioni di utilita generale
-- Date: 10/09/2003
-- Version: 1.3
--
-- Dependency:
--
-- History:
--
-- Date: 05/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 12/09/2002
-- Version: 1.1
-- Compilazione triggers e tutto, aggiunti metodi per compilazione dello schema dell'utente che si è collegato (tranne sys e system)
-- Eliminato lo user come parametro per limitazioni di invocazione in procedure su all_objects
--
-- Date: 13/09/2002
-- Version: 1.2
-- FIX SU COMPILAZIONE PACKAGES
--
-- Date: 10/09/2003
-- Version: 1.3
-- FIX compilazione view trappata eccezione di compilazione non anadat a buon fine
--
-- Constants:

-- Functions e Procedures:

PROCEDURE schedula_compilazione_pkg;

PROCEDURE schedula_compilazione_view;

PROCEDURE schedula_compilazione_triggers;

PROCEDURE compila_tutto;

end;
