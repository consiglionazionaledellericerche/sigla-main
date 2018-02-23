--------------------------------------------------------
--  DDL for Package CNRSTO010
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRSTO010" as
--
-- CNRSTO010 - Package per la gestione dello storico contratto
-- Date: 28/07/2017
-- Version: 1.0
--
 procedure scaricaSuStorico( aPgStorico number, aDsStorico varchar2, aOld contratto%rowtype);
End;
