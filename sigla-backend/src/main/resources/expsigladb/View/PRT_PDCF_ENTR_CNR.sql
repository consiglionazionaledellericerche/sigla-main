--------------------------------------------------------
--  DDL for View PRT_PDCF_ENTR_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_PDCF_ENTR_CNR" ("ESERCIZIO", "VOCE", "DESCRIZIONE_VOCE", "NATURA", "RISERVATO_SAC") AS 
  select
--
-- Date: 19/02/2003
-- Version: 1.0
--
-- Vista di stampa Piano dei conti finanziario (entrate)
--
-- History:
--
-- Date: 19/02/2003
-- Version: 1.0
-- Creazione: questi commenti non erano stati forniti da CNR
--
-- Body
--
distinct a.esercizio, a.cd_elemento_voce, a.ds_elemento_voce, c.ds_natura, a.fl_voce_sac
from elemento_voce a, ass_ev_ev b, natura c
where a.ti_appartenenza='C' and
	  a.ti_gestione='E' and
	  b.cd_elemento_voce=a.cd_elemento_voce and
	  c.cd_natura=b.cd_natura
order by a.cd_elemento_voce
;
