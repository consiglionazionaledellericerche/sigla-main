--------------------------------------------------------
--  DDL for View PRT_STAMPA_SOSP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_SOSP" ("CDS", "ESERCIZIO", "TIPOSOSP", "TIPOES", "CODSOSP", "IMPORTO", "DATA", "TIPOBANCA", "CAUS", "DESCR") AS 
  SELECT
  -- Date: 27/10/2003
  -- Version: 1.3
  --
  -- Stampa Sospesi
  --
  -- History:
  --
  -- Date: 10/06/2003
  -- Version: 1.0
-- Creazione
  --
  -- Date: 27/10/2003
  -- Version: 1.3
-- eliminato filtro stato sospeso
  --
  -- Body
  --
  sospeso.CD_CDS,
  sospeso.esercizio,
  sospeso.TI_SOSPESO_RISCONTRO,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.CD_SOSPESO,
  sospeso.IM_SOSPESO,
  sospeso.DT_REGISTRAZIONE,
  sospeso.TI_CC_BI,
  sospeso.CAUSALE,
  sospeso.DS_ANAGRAFICO
FROM
  sospeso
WHERE   sospeso.CD_SOSPESO_PADRE is not null
AND SOSPESO.TI_SOSPESO_RISCONTRO = 'S'
AND SOSPESO.FL_STORNATO ='N'
   order by sospeso.DT_REGISTRAZIONE;

   COMMENT ON TABLE "PRT_STAMPA_SOSP"  IS 'Stampa SOSPESI';
