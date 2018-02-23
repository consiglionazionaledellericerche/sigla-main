--------------------------------------------------------
--  DDL for Package CNRSTO090
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRSTO090" as
--
-- CNRSTO090 - Package per la gestione dello storico dei dati della minicarriera
-- Version: 1.0
--
-- Procedure di inserimento nelle tabelle di storico
-- Viene chiamata da trigger BU_MINICARRIERA
-- Viene chiamata da trigger BU_MINICARRIERA_RATA
-- Viene chiamata da trigger BD_MINICARRIERA
-- Viene chiamata da trigger BD_MINICARRIERA_RATA

 procedure sto_minicarriera (aPgStorico number, aDest MINICARRIERA%rowtype);
 procedure sto_minicarriera_rata (aPgStorico number, aDest MINICARRIERA_RATA%rowtype);
end;
