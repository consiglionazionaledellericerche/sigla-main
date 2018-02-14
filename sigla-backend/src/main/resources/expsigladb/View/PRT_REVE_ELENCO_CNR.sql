--------------------------------------------------------
--  DDL for View PRT_REVE_ELENCO_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REVE_ELENCO_CNR" ("ESERCIZIO", "COD_CDS", "PROGRESSIVO", "DATA_EMISSIONE", "UO_EMITTENTE", "TIPOLOGIA", "STATO", "STATO_TRASMISSIONE", "COMPETENZA_RESIDUO", "ANNO_ACCERTAMENTO", "ANNO_ORI_ACCERTAMENTO", "NUM_ACCERTAMENTO", "CDS_ORI_ACCERTAMENTO", "UO_ORI_ACCERTAMENTO", "TERZO_COD", "LORDO", "COD_VOCE", "TERZO_DES", "RIGA_DES", "PG_SCAD_ACC") AS 
  select distinct
--
-- Date: 19/07/2006
-- Version: 1.2
--
-- Vista di stampa del Giornale delle Reversali
--
-- History:
--
-- Date: 27/11/2002
-- Version: 1.0
-- Creazione
--
-- date: 20/03/2003
-- Version: 1.1
-- inserito distinct nella select, aggiunto alla vista pg_scad_acc
--
-- Date: 19/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
reversale.ESERCIZIO,
reversale.CD_CDS,
reversale.PG_REVERSALE,
reversale.DT_EMISSIONE,
reversale.CD_UO_ORIGINE,
reversale.TI_REVERSALE,
reversale.STATO,
reversale.STATO_TRASMISSIONE,
reversale.TI_COMPETENZA_RESIDUO,
reversale_riga.ESERCIZIO_ACCERTAMENTO,
reversale_riga.ESERCIZIO_ORI_ACCERTAMENTO,
reversale_riga.PG_ACCERTAMENTO,
accertamento.cd_cds_origine,
accertamento.cd_uo_origine,
reversale_riga.CD_TERZO,
reversale_riga.IM_REVERSALE_RIGA,
accertamento.CD_VOCE,
terzo.DENOMINAZIONE_SEDE,
reversale_riga.DS_REVERSALE_RIGA,
reversale_riga.PG_ACCERTAMENTO_SCADENZARIO
from reversale, reversale_riga, accertamento, terzo
where reversale_riga.CD_CDS = reversale.CD_CDS
and   reversale_riga.ESERCIZIO = reversale.ESERCIZIO
and   reversale_riga.PG_REVERSALE = reversale.PG_REVERSALE
and   accertamento.CD_CDS = reversale_riga.CD_CDS
and   accertamento.ESERCIZIO = reversale_riga.ESERCIZIO
and   accertamento.ESERCIZIO_ORIGINALE = reversale_riga.ESERCIZIO_ORI_ACCERTAMENTO
and   accertamento.PG_ACCERTAMENTO = reversale_riga.PG_ACCERTAMENTO
and   terzo.CD_TERZO = reversale_riga.CD_TERZO
order by reversale.ESERCIZIO, reversale.CD_CDS, reversale.PG_REVERSALE;

   COMMENT ON TABLE "PRT_REVE_ELENCO_CNR"  IS 'Vista di stampa del Giornale delle Reversali';
