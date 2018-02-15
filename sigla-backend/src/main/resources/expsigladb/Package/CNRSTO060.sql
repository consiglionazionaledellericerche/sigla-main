--------------------------------------------------------
--  DDL for Package CNRSTO060
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRSTO060" as
--
-- CNRSTO060 - Package per la gestione dello storico PDG_MODULO, PDG_MODULO_COSTI, PDG_MODULO_SPESE, PDG_MODULO_ENTRATE
-- Date: 29/12/2005
-- Version: 1.0
--
--
-- Dependency:
--
-- History:
-- Date: 29/12/2005
-- Version: 1.0
-- Creazione
--
-- Constants:

-- Functions and Procedures:

-- Procedura di scarico dell'accertamento sullo storico
--
-- aOldEs deve contenere un rowtype completo di pdg_esercizio (= record :new)
-- aEs deve contenere un rowtype completo di pdg_esercizio (= record :old)

 procedure scaricaSuStorico(aOldEs pdg_esercizio%rowtype, aEs pdg_esercizio%rowtype);

End;
