--------------------------------------------------------
--  DDL for View PRT_STAMPA_SOSP_DA_ASS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_SOSP_DA_ASS" ("ESERCIZIO", "TI_ENTRATA_SPESA", "CD_SOSPESO", "CD_CDS_ORIGINE", "IM_SOSPESO", "DISPONIBILE", "DT_REGISTRAZIONE", "TI_CC_BI", "CAUSALE", "DS_ANAGRAFICO") AS 
  SELECT
  -- Date: 27/10/2003
  -- Version: 1.0
  --
  -- Stampa sospesi da assegnare
  --
  -- History:
  --
  -- Date: 27/10/2003
  -- Creazione
  --
  -- Body
  --
  sospeso.ESERCIZIO,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.CD_SOSPESO,
  sospeso.CD_CDS_ORIGINE,
  sospeso.IM_SOSPESO,
  sum(sospeso.IM_SOSPESO) - sum(sospeso.IM_ASSOCIATO) AS DISPONIBILE,
  sospeso.DT_REGISTRAZIONE,
  sospeso.TI_CC_BI,
  sospeso.CAUSALE,
  sospeso.DS_ANAGRAFICO
  FROM
  sospeso
  WHERE
  (sospeso.TI_ENTRATA_SPESA = 'E' OR sospeso.TI_ENTRATA_SPESA = 'S') AND
  sospeso.FL_STORNATO = 'N' AND
  sospeso.TI_SOSPESO_RISCONTRO = 'S' AND
  sospeso.CD_SOSPESO_PADRE IS NOT NULL AND
  sospeso.IM_SOSPESO > sospeso.IM_ASSOCIATO AND
  sospeso.STATO_SOSPESO = 'S'
  GROUP BY
  sospeso.ESERCIZIO,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.CD_SOSPESO,
  sospeso.CD_CDS_ORIGINE,
  sospeso.IM_SOSPESO,
  sospeso.DT_REGISTRAZIONE,
  sospeso.TI_CC_BI,
  sospeso.CAUSALE,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.DS_ANAGRAFICO
  ORDER BY
  sospeso.DT_REGISTRAZIONE;

   COMMENT ON TABLE "PRT_STAMPA_SOSP_DA_ASS"  IS 'Stampa sospesi da assegnare';
