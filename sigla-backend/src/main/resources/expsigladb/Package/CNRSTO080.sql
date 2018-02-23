--------------------------------------------------------
--  DDL for Package CNRSTO080
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRSTO080" as
--
-- CNRSTO080 - Package per la gestione dello storico dei dati Intrastat dopo l'invio
-- Date: 28/05/2010
-- Version: 1.0
--
-- Procedure di inserimento nelle tabelle di storico
-- Viene chiamata da trigger BU_FATTURA_ATTIVA_INTRA
-- Viene chiamata da trigger BU_FATTURA_PASSIVA_INTRA
-- Viene chiamata da trigger BD_FATTURA_ATTIVA_INTRA
-- Viene chiamata da trigger BD_FATTURA_PASSIVA_INTRA

 procedure sto_FATTURA_PASSIVA_INTRA (aPgStorico number, aDest FATTURA_PASSIVA_INTRA%rowtype);
 procedure sto_FATTURA_ATTIVA_INTRA (aPgStorico number, aDest FATTURA_ATTIVA_INTRA%rowtype);

end;
