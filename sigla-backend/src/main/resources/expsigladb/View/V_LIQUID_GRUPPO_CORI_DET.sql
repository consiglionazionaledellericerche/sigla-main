--------------------------------------------------------
--  DDL for View V_LIQUID_GRUPPO_CORI_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_GRUPPO_CORI_DET" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_LIQUIDAZIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "PG_LIQUIDAZIONE_ORIGINE", "ESERCIZIO_CONTRIBUTO_RITENUTA", "PG_COMPENSO", "DS_TERZO", "DT_EMISSIONE_MANDATO", "CD_CONTRIBUTO_RITENUTA", "TI_ENTE_PERCIPIENTE", "IM_CORI", "CD_GRUPPO_CR", "CD_REGIONE", "PG_COMUNE") AS 
  select
--
-- Date: 13/06/2003
-- Version: 2.2
--
-- View di estrazione dei dettagli CORI
--
-- History:
--
-- Date: 18/06/2002
-- Version: 1.0
-- Creazione vista
--
-- Date: 25/06/2002
-- Version: 1.1
-- Modifica tabelle base
--
-- Date: 06/05/2003
-- Version: 2.0
-- Modifiche per gestione liquidazione CORI in CHIUSURA esercizio
--
-- Date: 21/05/2003
-- Version: 2.1
-- Entro sui CORI det per esercizio_contributo_ritenuta
--
-- Date: 13/06/2003
-- Version: 2.2
--
-- Date: 15/04/2005
-- Version: 2.3
-- Modifica fatta per la gestione dei versamenti unificati per le UO della SAC
--
-- Body:
--
  det.CD_CDS,
  det.CD_UNITA_ORGANIZZATIVA,
  det.ESERCIZIO,
  det.PG_LIQUIDAZIONE,
  det.CD_CDS_ORIGINE,
  det.CD_UO_ORIGINE,
  det.PG_LIQUIDAZIONE_ORIGINE,
  det.ESERCIZIO_CONTRIBUTO_RITENUTA,
  det.PG_COMPENSO,
  v_det.DS_TERZO,
  v_det.DT_EMISSIONE_MANDATO,
  det.CD_CONTRIBUTO_RITENUTA,
  det.TI_ENTE_PERCIPIENTE,
  v_det.IM_CORI,
  det.CD_GRUPPO_CR,
  det.CD_REGIONE,
  det.PG_COMUNE
from liquid_gruppo_cori_det det, v_calcola_liquid_cori_det v_det
where
      det.CD_CDS_ORIGINE = v_det.CD_CDS                --det.CD_CDS = v_det.CD_CDS
  and det.CD_UO_ORIGINE = v_det.CD_UNITA_ORGANIZZATIVA --and det.CD_UNITA_ORGANIZZATIVA = v_det.CD_UNITA_ORGANIZZATIVA
  and det.ESERCIZIO_CONTRIBUTO_RITENUTA = v_det.ESERCIZIO_COMPENSO
  and det.ESERCIZIO = v_det.ESERCIZIO
  and det.PG_COMPENSO = v_det.PG_COMPENSO
  and det.CD_CONTRIBUTO_RITENUTA = v_det.CD_CONTRIBUTO_RITENUTA
  and det.TI_ENTE_PERCIPIENTE = v_det.TI_ENTE_PERCIPIENTE
  and det.CD_GRUPPO_CR = v_det.CD_GRUPPO_CR
  and det.CD_REGIONE = v_det.CD_REGIONE
  and det.PG_COMUNE = v_det.PG_COMUNE
;

   COMMENT ON TABLE "V_LIQUID_GRUPPO_CORI_DET"  IS 'View di estrazione dei dettagli CORI da liquidare';
