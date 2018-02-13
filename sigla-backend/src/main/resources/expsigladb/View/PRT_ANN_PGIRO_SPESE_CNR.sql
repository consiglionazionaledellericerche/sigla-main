--------------------------------------------------------
--  DDL for View PRT_ANN_PGIRO_SPESE_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_ANN_PGIRO_SPESE_CNR" ("ESERCIZIO", "CDS", "UO", "ESERCIZIO_ORIGINALE", "PROGRESSIVO", "DATA", "DESCRIZIONE", "IMPORTO", "VOCE_BILANCIO", "STATO_OBBL", "CREDITORE", "DS_CDS", "DS_UO", "CDS_OR", "UO_OR") AS 
  select
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Vista di stampa Registro Annotazioni Partita di Giro parte Spese CNR
--
-- History:
--
-- Date: 13/01/2003
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body
--
obbligazione.ESERCIZIO,
obbligazione.CD_CDS,
obbligazione.CD_UNITA_ORGANIZZATIVA,
obbligazione.ESERCIZIO_ORIGINALE,
obbligazione.PG_OBBLIGAZIONE,
obbligazione.DT_REGISTRAZIONE,
obbligazione.DS_OBBLIGAZIONE,
obbligazione.IM_OBBLIGAZIONE,
obbligazione.CD_ELEMENTO_VOCE,
obbligazione.STATO_OBBLIGAZIONE,
terzo.DENOMINAZIONE_SEDE,
b.DS_UNITA_ORGANIZZATIVA as ds_cds,
a.DS_UNITA_ORGANIZZATIVA as ds_uo,
obbligazione.CD_CDS_ORIGINE,
obbligazione.CD_UO_ORIGINE
from obbligazione, terzo, unita_organizzativa a, unita_organizzativa b
where obbligazione.CD_TERZO=terzo.CD_TERZO and
	  (obbligazione.CD_TIPO_DOCUMENTO_CONT='IMP' OR
	  obbligazione.CD_TIPO_DOCUMENTO_CONT='IMP_RES') and
	  obbligazione.FL_PGIRO='Y' and
	  a.CD_UNITA_ORGANIZZATIVA(+)=obbligazione.CD_UO_ORIGINE and
	  b.CD_UNITA_ORGANIZZATIVA(+)=obbligazione.CD_CDS_ORIGINE
order by obbligazione.ESERCIZIO, obbligazione.ESERCIZIO_ORIGINALE, obbligazione.PG_OBBLIGAZIONE;

   COMMENT ON TABLE "PRT_ANN_PGIRO_SPESE_CNR"  IS 'Vista di stampa Registro Annotazioni Partita di Giro parte Spese CNR';
