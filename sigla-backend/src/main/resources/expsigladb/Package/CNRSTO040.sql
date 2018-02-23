--------------------------------------------------------
--  DDL for Package CNRSTO040
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRSTO040" as
--
-- CNRSTO040 - Package per la gestione dello storico ACCERTAMENTO
-- Date: 19/07/2006
-- Version: 1.3
--
--
-- Dependency:
--
-- History:
-- Date: 06/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 08/05/2002
-- Version: 1.1
-- Lo storico non viene annulato ad ogni scarico
--
-- Date: 10/06/2003
-- Version: 1.2
-- Modificato metodo di insert su accertamento_scadenzario_s
--
-- Date: 19/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 21/04/2008
-- Version: 1.4
-- Aggiunto il campo FL_NETTO_SOSPESO
--
-- Constants:

-- Functions and Procedures:

-- Procedura di scarico dell'accertamento sullo storico
--
-- aAcc deve contenere un rowtype completo dell'accertamento (= record :new)
-- aOldAcc deve contenere un rowtype completo di accertamento (= record :old)

 procedure scaricaSuStorico(aDesc varchar2, aOldAcc accertamento%rowtype, aAcc accertamento%rowtype);

 -- Procedure di inserimento nelle tabella di storico dell'accertamento
 -- Viene chiamata da trigger BU_ACCERTAMENTO

 procedure sto_ACCERTAMENTO (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO%rowtype);
 procedure sto_ACCERTAMENTO_SCADENZARIO (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO_SCADENZARIO%rowtype);
 procedure sto_ACCERTAMENTO_SCAD_VOCE (aPgStorico number, aDsStorico varchar2, aDest ACCERTAMENTO_SCAD_VOCE%rowtype);
 procedure sto_ACCERTAMENTO_PGIRO_MODIF (aPgStorico number, imp_iniziale NUMBER, imp_variazione NUMBER,aDest ACCERTAMENTO%rowtype);

end;
