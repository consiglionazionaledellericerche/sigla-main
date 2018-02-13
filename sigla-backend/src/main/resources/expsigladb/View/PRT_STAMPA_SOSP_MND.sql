--------------------------------------------------------
--  DDL for View PRT_STAMPA_SOSP_MND
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_SOSP_MND" ("ESERCIZIO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_SOSPESO", "IM_SOSPESO", "IM_ASSOCIATO", "DT_REGISTRAZIONE", "TI_CC_BI", "CAUSALE", "DS_ANAGRAFICO", "PG_MANDATO", "DISPONIBILE") AS 
  select
--
-- Date: 26/11/2003
-- Version: 1.0
--
-- Stampa sospesi di spesa associati a mandati
--
--
-- History:
--
-- Date: 26/11/2003
-- Version: 1.0
-- Creazione
--
-- Body
--
sospeso.ESERCIZIO,
mandato.CD_CDS_ORIGINE,
sospeso.CD_UO_ORIGINE,
sospeso.CD_SOSPESO,
sospeso.IM_SOSPESO,
sospeso.IM_ASSOCIATO,
sospeso.DT_REGISTRAZIONE,
sospeso.TI_CC_BI,
sospeso.CAUSALE,
sospeso.DS_ANAGRAFICO,
mandato.PG_MANDATO,
(sum(sospeso.IM_SOSPESO) - sum(sospeso.IM_ASSOCIATO)) as disponibile
from
sospeso, sospeso_det_usc, mandato
where
sospeso.CD_SOSPESO = sospeso_det_usc.CD_SOSPESO and
sospeso.TI_ENTRATA_SPESA = 'S' and
sospeso.STATO_SOSPESO = 'A' and
sospeso_det_usc.STATO <> 'A' and
sospeso.TI_SOSPESO_RISCONTRO = 'S' and
sospeso.CD_SOSPESO_PADRE is not null and
sospeso.CD_CDS = cnrctb020.getCdCdsEnte(SOSPESO.ESERCIZIO) and
mandato.TI_MANDATO = 'S' and
sospeso.ESERCIZIO = sospeso_det_usc.ESERCIZIO and
sospeso.TI_ENTRATA_SPESA = sospeso_det_usc.TI_ENTRATA_SPESA and
sospeso.TI_SOSPESO_RISCONTRO = sospeso_det_usc.TI_SOSPESO_RISCONTRO and
sospeso.CD_SOSPESO = sospeso_det_usc.CD_SOSPESO and
sospeso_det_usc.CD_CDS_mandato = mandato.CD_CDS and
sospeso_det_usc.ESERCIZIO = mandato.ESERCIZIO and
mandato.PG_MANDATO = sospeso_det_usc.PG_MANDATO
group by
mandato.CD_CDS_ORIGINE,
sospeso.CD_CDS,
sospeso.ESERCIZIO,
sospeso.CD_UO_ORIGINE,
sospeso.CD_SOSPESO,
sospeso.IM_SOSPESO,
sospeso.IM_ASSOCIATO,
sospeso.DT_REGISTRAZIONE,
sospeso.TI_CC_BI,
sospeso.CAUSALE,
sospeso.DS_ANAGRAFICO,
mandato.PG_MANDATO
order by
sospeso.DT_REGISTRAZIONE;

   COMMENT ON TABLE "PRT_STAMPA_SOSP_MND"  IS 'Stampa sospesi di spesa associati a mandati';
