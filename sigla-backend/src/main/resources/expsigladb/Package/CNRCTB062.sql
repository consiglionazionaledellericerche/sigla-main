--------------------------------------------------------
--  DDL for Package CNRCTB062
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB062" as
--
-- CNRCTB062 - Package per il recupero delle informazioni delle view per i servizi REST dei Brevetti
-- Date: 08/09/2016
-- Version: 1.0
--
-- History:
--
-- Date: 08/09/2016
-- Version: 1.0
-- Creazione
--
 function getEsercizioReversale(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return number;
 function getPgReversale(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return number;
 function getDataReversale(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return date;
 function getEsercizioMandato(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return number;
 function getPgMandato(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return number;
 function getDataMandato(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return date;

end;
