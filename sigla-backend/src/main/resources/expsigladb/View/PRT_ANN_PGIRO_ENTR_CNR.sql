--------------------------------------------------------
--  DDL for View PRT_ANN_PGIRO_ENTR_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_ANN_PGIRO_ENTR_CNR" ("ESERCIZIO", "CDS", "CDS_OR", "UO_OR", "ESERCIZIO_ORIGINALE", "PROGRESSIVO", "DATA", "DESCRIZIONE", "IMPORTO", "VOCE_BILANCIO", "DEBITORE", "DS_CDS", "DS_UO", "DATA_CANC") AS 
  select
--
-- Date: 19/07/2006
-- Version: 1.1
--
-- Vista di stampa Registro Annotazioni di Entrata su Partita di Giro - CNR
--
-- History:
--
-- Date :13/01/2003
-- Version: 1.0
-- Creazione
--
-- Date: 19/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body
--
accertamento.ESERCIZIO,
accertamento.CD_CDS,
accertamento.CD_CDS_ORIGINE,
accertamento.CD_UO_ORIGINE,
accertamento.ESERCIZIO_ORIGINALE,
accertamento.PG_ACCERTAMENTO,
accertamento.DT_REGISTRAZIONE,
accertamento.DS_ACCERTAMENTO,
accertamento.IM_ACCERTAMENTO,
accertamento.CD_ELEMENTO_VOCE,
terzo.DENOMINAZIONE_SEDE,
b.DS_UNITA_ORGANIZZATIVA as ds_cds,
a.DS_UNITA_ORGANIZZATIVA as ds_uo,
ACCERTAMENTO.DT_CANCELLAZIONE
from accertamento, terzo, unita_organizzativa a, unita_organizzativa b
where accertamento.CD_TERZO=terzo.CD_TERZO and
	  (accertamento.CD_TIPO_DOCUMENTO_CONT='ACR' OR
	  accertamento.CD_TIPO_DOCUMENTO_CONT='ACR_RES') and
	  accertamento.FL_PGIRO='Y' and
	  a.CD_UNITA_ORGANIZZATIVA(+)=accertamento.CD_UO_ORIGINE and
	  b.CD_UNITA_ORGANIZZATIVA(+)=accertamento.CD_CDS_ORIGINE
order by accertamento.ESERCIZIO, accertamento.ESERCIZIO_ORIGINALE, accertamento.PG_ACCERTAMENTO;

   COMMENT ON TABLE "PRT_ANN_PGIRO_ENTR_CNR"  IS 'Vista di stampa Registro Annotazioni di Entrata su Partita di Giro - CNR';
