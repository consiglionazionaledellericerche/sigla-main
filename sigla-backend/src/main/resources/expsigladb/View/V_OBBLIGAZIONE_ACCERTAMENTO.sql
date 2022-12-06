--------------------------------------------------------
--  DDL for View V_OBBLIGAZIONE_ACCERTAMENTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_OBBLIGAZIONE_ACCERTAMENTO" ("CD_CDS", "ESERCIZIO", "ESERCIZIO_ORI_ACC_OBB", "PG_ACC_OBB", "TI_GESTIONE", "CD_TIPO_DOCUMENTO_CONT", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_APPARTENENZA", "CD_ELEMENTO_VOCE", "CD_TERZO", "IM_ACC_OBB", "FL_PGIRO", "TI_ORIGINE", "ESERCIZIO_COMPETENZA", "IM_ASSOCIATO_DOC_AMM", "IM_ASSOCIATO_REV_MAN", "IM_RISCONTRATO", "CD_CDS_ORI_RIPORTO", "ESERCIZIO_ORI_RIPORTO", "ESERCIZIO_ORI_ORI_RIPORTO", "PG_ACC_OBB_ORI_RIPORTO", "RIPORTATO", "ESERCIZIO_CONTRATTO", "STATO_CONTRATTO", "PG_CONTRATTO", "PG_VER_REC") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.5
--
-- Vista per l'estrazione di documenti contabili
-- al fine di annullamento massivo, riporto, riporto indietro
-- nel contesto della chiusura contabile
--
-- History:
--
-- Date: 21/05/2003
-- Version: 1.0
-- Creazione
--
-- Date: 06/06/2003
-- Version: 1.1
-- Eliminato flag riscontrato, inserito calcolo dell'importo riscotrato
--
-- Date: 13/06/2003
-- Version: 1.2
-- Aggiunto campo TI_ORIGINE per determinare origine di apertura di partite di giro
--
-- Date: 18/06/2003
-- Version: 1.3
-- Estrazioni obbligazioni pluriennali
--
-- Date: 14/04/2005
-- Version: 1.4
-- Aggiunti i campi ESERCIZIO_CONTRATTO, STATO_CONTRATTO, PG_CONTRATTO
--
-- Date: 18/07/2006
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
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
acc.CD_TERZO,
acc.IM_ACCERTAMENTO,
acc.FL_PGIRO,
ass.TI_ORIGINE,
acc.ESERCIZIO_COMPETENZA,
sum(accscad.IM_ASSOCIATO_DOC_AMM),
sum(accscad.IM_ASSOCIATO_DOC_CONTABILE),
getIm_riscontrato(acc.ESERCIZIO, acc.CD_CDS, acc.ESERCIZIO_ORIGINALE, acc.PG_ACCERTAMENTO, acc.TI_GESTIONE),
acc.CD_CDS_ORI_RIPORTO,
acc.ESERCIZIO_ORI_RIPORTO,
acc.ESERCIZIO_ORI_ORI_RIPORTO,
acc.PG_ACCERTAMENTO_ORI_RIPORTO,
acc.RIPORTATO,
acc.ESERCIZIO_CONTRATTO,
acc.STATO_CONTRATTO,
acc.PG_CONTRATTO,
acc.PG_VER_REC
from accertamento acc
	,accertamento_scadenzario accscad
	,ass_obb_acr_pgiro ass
where acc.DT_CANCELLAZIONE 	  is null
  and accscad.CD_CDS		  = acc.CD_CDS
  and accscad.ESERCIZIO		  = acc.ESERCIZIO
  and accscad.ESERCIZIO_ORIGINALE = acc.ESERCIZIO_ORIGINALE
  and accscad.PG_ACCERTAMENTO = acc.PG_ACCERTAMENTO
  and ass.CD_CDS		  (+) = acc.CD_CDS
  and ass.ESERCIZIO		  (+) = acc.ESERCIZIO
  and ass.ESERCIZIO_ORI_ACCERTAMENTO (+) = acc.ESERCIZIO_ORIGINALE
  and ass.PG_ACCERTAMENTO (+) = acc.PG_ACCERTAMENTO
group by
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
acc.CD_TERZO,
acc.IM_ACCERTAMENTO,
acc.FL_PGIRO,
ass.TI_ORIGINE,
acc.ESERCIZIO_COMPETENZA,
acc.CD_CDS_ORI_RIPORTO,
acc.ESERCIZIO_ORI_RIPORTO,
acc.ESERCIZIO_ORI_ORI_RIPORTO,
acc.PG_ACCERTAMENTO_ORI_RIPORTO,
acc.RIPORTATO,
acc.ESERCIZIO_CONTRATTO,
acc.STATO_CONTRATTO,
acc.PG_CONTRATTO,
acc.PG_VER_REC
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
obb.CD_TERZO,
obb.IM_OBBLIGAZIONE,
obb.FL_PGIRO,
ass.TI_ORIGINE,
obb.ESERCIZIO_COMPETENZA,
sum(obbscad.IM_ASSOCIATO_DOC_AMM),
sum(obbscad.IM_ASSOCIATO_DOC_CONTABILE),
getIm_riscontrato(obb.ESERCIZIO, obb.CD_CDS, obb.ESERCIZIO_ORIGINALE, obb.PG_OBBLIGAZIONE, obb.TI_GESTIONE),
obb.CD_CDS_ORI_RIPORTO,
obb.ESERCIZIO_ORI_RIPORTO,
obb.ESERCIZIO_ORI_ORI_RIPORTO,
obb.PG_OBBLIGAZIONE_ORI_RIPORTO,
obb.RIPORTATO,
obb.ESERCIZIO_CONTRATTO,
obb.STATO_CONTRATTO,
obb.PG_CONTRATTO,
obb.PG_VER_REC
from obbligazione obb
	,obbligazione_scadenzario obbscad
	,ass_obb_acr_pgiro ass
where obb.DT_CANCELLAZIONE    is null
  and obbscad.CD_CDS 		  = obb.CD_CDS
  and obbscad.ESERCIZIO 	  = obb.ESERCIZIO
  and obbscad.ESERCIZIO_ORIGINALE = obb.ESERCIZIO_ORIGINALE
  and obbscad.PG_OBBLIGAZIONE = obb.PG_OBBLIGAZIONE
  and ass.CD_CDS		  (+) = obb.CD_CDS
  and ass.ESERCIZIO		  (+) = obb.ESERCIZIO
  and ass.ESERCIZIO_ORI_OBBLIGAZIONE  (+) = obb.ESERCIZIO_ORIGINALE
  and ass.PG_OBBLIGAZIONE (+) = obb.PG_OBBLIGAZIONE
group by
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
obb.CD_TERZO,
obb.IM_OBBLIGAZIONE,
obb.FL_PGIRO,
ass.TI_ORIGINE,
obb.ESERCIZIO_COMPETENZA,
obb.CD_CDS_ORI_RIPORTO,
obb.ESERCIZIO_ORI_RIPORTO,
obb.ESERCIZIO_ORI_ORI_RIPORTO,
obb.PG_OBBLIGAZIONE_ORI_RIPORTO,
obb.RIPORTATO,
obb.ESERCIZIO_CONTRATTO,
obb.STATO_CONTRATTO,
obb.PG_CONTRATTO,
obb.PG_VER_REC;

   COMMENT ON TABLE "V_OBBLIGAZIONE_ACCERTAMENTO"  IS 'Vista per l''estrazione di documenti contabili
al fine di annullamento massivo, riporto, riporto indietro
nel contesto della chiusura contabile';
