--------------------------------------------------------
--  DDL for View V_MANDATO_OBBLSCAD
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_OBBLSCAD" ("CD_CDS", "ESERCIZIO", "PG_MANDATO", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "TOT_RIGA_SU_SCAD") AS 
  select
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Vista di aggregazione delle righe dei mandati rispetto alle scadenze di obbligazione
--
-- History:
--
-- Date: 19/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
mr.CD_CDS, mr.ESERCIZIO, mr.PG_MANDATO, mr.ESERCIZIO_OBBLIGAZIONE, mr.ESERCIZIO_ORI_OBBLIGAZIONE, mr.PG_OBBLIGAZIONE, mr.PG_OBBLIGAZIONE_SCADENZARIO,SUM(mr.IM_MANDATO_RIGA) TOT_RIGA_SU_SCAD
from mandato_riga mr
group by mr.CD_CDS,
	  mr.ESERCIZIO,
	  mr.PG_MANDATO,
	  mr.ESERCIZIO_OBBLIGAZIONE,
	  mr.ESERCIZIO_ORI_OBBLIGAZIONE,
	  mr.PG_OBBLIGAZIONE,
	  mr.PG_OBBLIGAZIONE_SCADENZARIO;
