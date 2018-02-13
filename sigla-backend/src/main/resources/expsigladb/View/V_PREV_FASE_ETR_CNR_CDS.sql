--------------------------------------------------------
--  DDL for View V_PREV_FASE_ETR_CNR_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PREV_FASE_ETR_CNR_CDS" ("CD_CDS", "ESERCIZIO", "TI_APPARTENENZA", "CD_VOCE", "IM_ARTICOLO") AS 
  (
select
--
-- Date: 28/08/2002
-- Version: 1.0
--
-- Vista di estrazione del saldo complessivo articoli di spesa che alimentano l'entrata cds cd_voce
-- per controllo di fase entrate CNR/CDS
--
-- History:
--
-- Date: 28/08/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
 b.cd_cds,
 b.esercizio,
 'D',
 a.cd_elemento_voce_coll cd_voce,
 sum(im_stanz_iniziale_a1) im_articoli_cnr
from ass_ev_ev a, V_BIL_PREV_SOTTOARTICOLI_CNR b, unita_organizzativa c where
    	       a.ti_appartenenza_coll = 'D'
    	   and a.ti_gestione_coll = 'E'
		   and a.ti_elemento_voce_coll = 'C'
    	   and a.ti_appartenenza = 'C'
    	   and a.ti_gestione = 'S'
           and a.ti_elemento_voce = 'T'
    	   and a.cd_cds = c.cd_tipo_unita
		   and c.cd_unita_organizzativa = b.cd_cds
		   and c.fl_cds = 'Y'
		   and b.esercizio = a.esercizio
		   and b.cd_natura = a.cd_natura
		   and b.ti_appartenenza = a.ti_appartenenza
		   and b.ti_gestione = a.ti_gestione
		   and substr(b.cd_voce,1,4) = a.cd_elemento_voce -- Titolo CNR
group by
 b.cd_cds,
 b.esercizio,
 a.cd_elemento_voce_coll
)
;

   COMMENT ON TABLE "V_PREV_FASE_ETR_CNR_CDS"  IS 'Vista di estrazione del saldo complessivo articoli di spesa che alimentano l''entrata cds cd_voce
per controllo di fase entrate CNR/CDS';
