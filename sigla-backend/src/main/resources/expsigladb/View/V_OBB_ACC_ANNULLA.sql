--------------------------------------------------------
--  DDL for View V_OBB_ACC_ANNULLA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_OBB_ACC_ANNULLA" ("CD_CDS", "ESERCIZIO", "ESERCIZIO_ORI_ACC_OBB", "PG_ACC_OBB", "TI_GESTIONE", "CD_TIPO_DOCUMENTO_CONT", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_APPARTENENZA", "CD_ELEMENTO_VOCE", "CD_VOCE", "CD_TERZO", "IM_ACC_OBB", "FL_PGIRO", "ESERCIZIO_COMPETENZA", "CD_CDS_ORI_RIPORTO", "ESERCIZIO_ORI_RIPORTO", "ESERCIZIO_ORI_ORI_RIPORTO", "PG_ACC_OBB_ORI_RIPORTO", "RIPORTATO", "PG_VER_REC") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.2
--
-- Vista per l'estrazione dei documenti annullabili
--
-- History:
--
-- Date: 26/06/2003
-- Version: 1.0
-- Creazione
--
-- Date: 26/04/2004
-- Version: 1.1
-- Estrazione cd_voce a null per allineamento interfaccia con v_obb_acc_riporta/deriporta
-- Errore n. 816
--
-- Date: 18/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 acc.CD_CDS,
 acc.ESERCIZIO,
 acc.ESERCIZIO_ORIGINALE,
 acc.PG_ACCERTAMENTO,
 acc.TI_GESTIONE,
 acc.CD_TIPO_DOCUMENTO_CONT,
 acc.CD_CDS_ORIGINE,
 acc.CD_UO_ORIGINE,
 acc.TI_APPARTENENZA,
 acc.CD_ELEMENTO_VOCE,
 null,
 acc.CD_TERZO,
 acc.IM_ACCERTAMENTO,
 acc.FL_PGIRO,
 acc.ESERCIZIO_COMPETENZA,
 acc.CD_CDS_ORI_RIPORTO,
 acc.ESERCIZIO_ORI_RIPORTO,
 acc.ESERCIZIO_ORI_ORI_RIPORTO,
 acc.PG_ACCERTAMENTO_ORI_RIPORTO,
 acc.RIPORTATO,
 acc.PG_VER_REC
from accertamento acc
where
 isEligibileAnnull('E',acc.cd_cds,acc.esercizio,acc.esercizio_originale,acc.pg_accertamento) = 'Y'
union all
select
obb.CD_CDS,
obb.ESERCIZIO,
obb.ESERCIZIO_ORIGINALE,
obb.PG_OBBLIGAZIONE,
obb.TI_GESTIONE,
obb.CD_TIPO_DOCUMENTO_CONT,
obb.CD_CDS_ORIGINE,
obb.CD_UO_ORIGINE,
obb.TI_APPARTENENZA,
obb.CD_ELEMENTO_VOCE,
null,
obb.CD_TERZO,
obb.IM_OBBLIGAZIONE,
obb.FL_PGIRO,
obb.ESERCIZIO_COMPETENZA,
obb.CD_CDS_ORI_RIPORTO,
obb.ESERCIZIO_ORI_RIPORTO,
obb.ESERCIZIO_ORI_ORI_RIPORTO,
obb.PG_OBBLIGAZIONE_ORI_RIPORTO,
obb.RIPORTATO,
obb.PG_VER_REC
from obbligazione obb
where
 isEligibileAnnull('S',obb.cd_cds,obb.esercizio,obb.esercizio_originale,obb.pg_obbligazione) = 'Y';

   COMMENT ON TABLE "V_OBB_ACC_ANNULLA"  IS 'Vista per l''estrazione dei documenti annullabili';
