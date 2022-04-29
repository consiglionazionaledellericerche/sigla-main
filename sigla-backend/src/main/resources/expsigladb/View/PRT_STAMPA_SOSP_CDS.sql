--------------------------------------------------------
--  DDL for View PRT_STAMPA_SOSP_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_SOSP_CDS" ("ESERCIZIO", "TI_ENTRATA_SPESA", "CD_SOSPESO", "CD_CDS_ORIGINE", "DISPONIBILE", "IM_SOSPESO", "DT_REGISTRAZIONE", "TI_CC_BI", "CAUSALE", "DS_ANAGRAFICO", "CD_AVVISO_PAGOPA") AS
  SELECT
  -- Date: 21/10/2003
  -- Version: 1.2
  --
  -- Stampa sospesi assegnati a cds
  --
  -- History:
  --
  -- Date: 24/06/2003
  -- Version: 1.0
  -- Creazione
  --
  -- Date: 15/10/2003
  -- Version: 1.1
  -- aggiunto parametro ti_ent_sp
  --
  -- Date: 21/10/2003
  -- Version: 1.2
  -- modificato commento query in minuscolo
  --
  -- Body
  --
  sospeso.esercizio,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.CD_SOSPESO,
  sospeso.CD_CDS_ORIGINE,
  SUM(sospeso.IM_SOSPESO) - SUM(sospeso.IM_ASSOCIATO) AS DISPONIBILE,
  sospeso.IM_SOSPESO,
  sospeso.DT_REGISTRAZIONE,
  sospeso.TI_CC_BI,
  sospeso.CAUSALE,
  sospeso.DS_ANAGRAFICO,
  sospeso.CD_AVVISO_PAGOPA
  FROM
  sospeso
  WHERE
  (sospeso.TI_ENTRATA_SPESA = 'E' OR sospeso.TI_ENTRATA_SPESA = 'S') AND
  sospeso.FL_STORNATO = 'N' AND
  sospeso.STATO_SOSPESO = 'A' AND
  sospeso.CD_CDS = cnrctb020.getCdCdsEnte(SOSPESO.ESERCIZIO)  AND
  sospeso.TI_SOSPESO_RISCONTRO = 'S' AND
  sospeso.CD_SOSPESO_PADRE IS NOT NULL AND
  sospeso.IM_SOSPESO > sospeso.IM_ASSOCIATO
  GROUP BY
  sospeso.esercizio,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.CD_SOSPESO,
  sospeso.CD_CDS_ORIGINE,
  sospeso.IM_SOSPESO,
  sospeso.DT_REGISTRAZIONE,
  sospeso.TI_CC_BI,
  sospeso.CAUSALE,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.DS_ANAGRAFICO,
  sospeso.CD_AVVISO_PAGOPA
  ORDER BY
  sospeso.DT_REGISTRAZIONE;

   COMMENT ON TABLE "PRT_STAMPA_SOSP_CDS"  IS 'STAMPA SOSPESI  ASSOCIATI A CDS';
