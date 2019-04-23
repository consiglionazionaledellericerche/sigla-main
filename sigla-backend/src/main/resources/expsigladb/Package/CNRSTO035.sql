--------------------------------------------------------
--  DDL for Package CNRSTO035
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRSTO035" as
--
-- CNRSTO050 - Package per la gestione dello storico OBBLIGAZIONE
-- Date: 14/07/2006
-- Version: 1.5
--
--
-- Dependency:
--
-- History:
-- Date: 02/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 23/04/2002
-- Version: 1.1
-- L'obbligazione scarica su storico la versione precedente alla modifica
--
-- Date: 24/04/2002
-- Version: 1.2
-- L'obbligazione scarica su storico la versione precedente alla modifica
-- Fix errore di table mutating anche in Before Update trigger
--
-- Date: 10/06/2003
-- Version: 1.3
-- Modificato metodo di insert su obbligazione_scadenzario_s
--
-- Date: 12/01/2006
-- Version: 1.4
-- Gestione Residui - Modificato metodo di insert su obbligazione per l'aggiunta del campo
-- ESERCIZIO_ORIGINALE
--
-- Date: 14/07/2006
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 21/04/2008
-- Version: 1.4
-- Aggiunto il campo FL_NETTO_SOSPESO nella procedura sto_OBBLIGAZIONE
--
-- Constants:


-- Functions Procedures:

 -- Procedura di scarico dell'obbligazione sullo storico
 -- La procedura non effettuan controlli
 --
 -- aObb deve contenere un rowtype completo di obbligazione (= record :new)
 -- aOldObb deve contenere un rowtype completo di obbligazione (= record :old)

 procedure scaricaSuStorico(aDesc varchar2, aOldObb obbligazione%rowtype, aObb obbligazione%rowtype);

 -- Procedure di inserimento nelle tabella di storico dell'obbligazione
 -- Viene chiamata da trigger BU_OBBLIGAZIONE

 procedure sto_OBBLIGAZIONE (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE%rowtype);
 procedure sto_OBBLIGAZIONE_SCADENZARIO (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE_SCADENZARIO%rowtype);
 procedure sto_OBBLIGAZIONE_SCAD_VOCE (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE_SCAD_VOCE%rowtype);
 procedure sto_OBBLIGAZIONE_PGIRO_MODIF (aPgStorico number, imp_iniziale NUMBER, imp_variazione NUMBER,aDest OBBLIGAZIONE%rowtype, newObb OBBLIGAZIONE%rowtype);

end;
