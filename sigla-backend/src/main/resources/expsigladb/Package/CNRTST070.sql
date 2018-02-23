--------------------------------------------------------
--  DDL for Package CNRTST070
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRTST070" AS
--
-- CNRTST070 - Package per la generazione di utenze fittizie per gestione TESTS
--
-- Date: 25/03/2002
-- Version: 1.0
--
--
-- Dependency: CNRCTB 001/060 IBMCST001 IBMUTL001
--
-- History:
-- Date: 25/03/2002
-- Version: 1.0
-- Creazione
--
-- Constants:

-- Functions & Procedures:
--
-- Creazione di utenti secondo la seguente specifica:
--
-- Per ogni CDS viene creato un gestore
-- Sotto ogni gestore viene creato un utente per ogni CDR con nome = al CDR e utente template con tutti gli accessi sull'UO di competenza
--

 procedure creaUtentiTest;

end;
