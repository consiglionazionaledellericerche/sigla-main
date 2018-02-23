--------------------------------------------------------
--  DDL for Package CNRSTO050
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRSTO050" as
--
-- CNRSTO050 - Package per la gestione dello storico PIANO DI GESTIONE CDR
-- Date: 09/10/2001
-- Version: 1.0
--
--
-- Dependency: CNRCTB050
--
-- History:
-- Date: 09/10/2001
-- Version: 1.0
-- Creazione

-- Constants:


-- Functions & Procedures:

 -- Procedura di scarico del PDG sullo storico
 -- La procedura non effettuan controlli
 --
 -- aPdg deve contenere un rowtype completo di pdg preventivo

 procedure scaricaSuStorico(aDesc varchar2, aPdg pdg_preventivo%rowtype);

 function descPassaggioStato(vecchioStato varchar2, nuovoStato varchar2) return varchar2;

 -- Procedure di inserimento nelle tabella di storico del piano di gestione

 procedure sto_PDG_PREVENTIVO (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO%rowtype);
 procedure sto_PDG_PREVENTIVO_ETR_DET (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO_ETR_DET%rowtype);
 procedure sto_PDG_PREVENTIVO_SPE_DET (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO_SPE_DET%rowtype);

end;
