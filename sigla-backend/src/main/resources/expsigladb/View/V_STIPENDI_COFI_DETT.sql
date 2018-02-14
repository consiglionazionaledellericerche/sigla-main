--------------------------------------------------------
--  DDL for View V_STIPENDI_COFI_DETT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STIPENDI_COFI_DETT" ("ESERCIZIO", "MESE", "TIPO_FLUSSO", "ENTRATA_SPESA") AS 
  SELECT DISTINCT
--
-- Date: 29/09/2006
-- Version: 1.0
--
-- Per la gestione dei Flussi Stipendiali
--
-- History:
--
-- Date: 29/09/2006
-- Version: 1.0
-- Creazione
--
-- Body:
--
 ESERCIZIO,
 MESE,
 TIPO_FLUSSO,
 'S'
FROM STIPENDI_COFI_OBB_SCAD_DETT
UNION ALL
 SELECT DISTINCT
 ESERCIZIO,
 MESE,
 TIPO_FLUSSO,
 'E'
FROM STIPENDI_COFI_CORI_DETT
;
