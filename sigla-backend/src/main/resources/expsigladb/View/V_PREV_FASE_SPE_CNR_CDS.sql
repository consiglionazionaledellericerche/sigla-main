--------------------------------------------------------
--  DDL for View V_PREV_FASE_SPE_CNR_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PREV_FASE_SPE_CNR_CDS" ("CD_CDS", "ESERCIZIO", "TI_APPARTENENZA", "IM_SPESE_CNR", "IM_SPESE_CDS", "IM_CASSA_INIZIALE") AS 
  (
select
--
-- Date: 28/08/2002
-- Version: 1.0
--
-- Vista di estrazione del saldo complessivo trasferimenti (comp/res), saldo compl. spese CDS + fondo iniziale di cassa CDS
-- per controllo di fase spese CNR/CDS
--
-- History:
--
-- Date: 28/08/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
 cd_cds,
 esercizio,
 'D',
 sum(im_spese_cnr),
 sum(im_spese_cds),
 sum(im_cassa_iniziale)
from (
  select
   b.cd_proprio_voce cd_cds,
   a.esercizio,
   im_stanz_iniziale_a1 + variazioni_piu - variazioni_meno im_spese_cnr,
   0 im_spese_cds,
   0 im_cassa_iniziale
  from voce_f_saldi_cmp a, voce_f b where
       a.ti_appartenenza = 'C'
   and a.ti_gestione = 'S'
   and b.esercizio = a.esercizio
   and b.ti_appartenenza = a.ti_appartenenza
   and b.ti_gestione = a.ti_gestione
   and b.cd_voce = a.cd_voce
   and b.fl_mastrino = 'Y'
   and b.cd_parte = 1
 union all
  select
   cd_cds,
   esercizio,
   0 im_spese_cnr,
   im_stanz_iniziale_a1 + variazioni_piu - variazioni_meno im_spese_cds,
   0 im_cassa_iniziale
  from voce_f_saldi_cmp where
       ti_appartenenza = 'D'
   and ti_gestione = 'S'
 union all
  SELECT
   CD_CDS CD_CDS,
   ESERCIZIO ESERCIZIO,
   0 IM_SPESE_CNR,
   0 IM_SPESE_CDS,
   IM_CASSA_INIZIALE
  FROM
   ESERCIZIO
 ) group by
 cd_cds,
 esercizio
)
;

   COMMENT ON TABLE "V_PREV_FASE_SPE_CNR_CDS"  IS 'Vista di estrazione del saldo complessivo trasferimenti (comp/res), saldo compl. spese CDS + fondo iniziale di cassa CDS
per controllo di fase spese CNR/CDS';
