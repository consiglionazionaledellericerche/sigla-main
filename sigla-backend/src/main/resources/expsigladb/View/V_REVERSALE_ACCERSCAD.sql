--------------------------------------------------------
--  DDL for View V_REVERSALE_ACCERSCAD
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_REVERSALE_ACCERSCAD" ("CD_CDS", "ESERCIZIO", "PG_REVERSALE", "ESERCIZIO_ACCERTAMENTO", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "TOT_REVERSALE_RIGHE") AS 
  SELECT
--
-- Date: 19/07/2006
-- Version: 1.1
--
-- Vista di aggregazione delle righe delle reversali rispetto alle scadenze degli accertamenti
--
-- History:
--
-- Date: 19/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 19/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
RR.CD_CDS
	   ,RR.ESERCIZIO
	   ,RR.PG_REVERSALE
	   ,RR.ESERCIZIO_ACCERTAMENTO
	   ,RR.ESERCIZIO_ORI_ACCERTAMENTO
	   ,RR.PG_ACCERTAMENTO
	   ,RR.PG_ACCERTAMENTO_SCADENZARIO
	   ,SUM(RR.IM_REVERSALE_RIGA)
FROM REVERSALE_RIGA RR
GROUP BY RR.CD_CDS
	   ,RR.ESERCIZIO
	   ,RR.PG_REVERSALE
	   ,RR.ESERCIZIO_ACCERTAMENTO
	   ,RR.ESERCIZIO_ORI_ACCERTAMENTO
	   ,RR.PG_ACCERTAMENTO
	   ,RR.PG_ACCERTAMENTO_SCADENZARIO;
