--------------------------------------------------------
--  DDL for View V_PREVENTIVO_PAREGGIO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PREVENTIVO_PAREGGIO" ("CD_CDS", "ESERCIZIO", "TI_APPARTENENZA", "IM_ENTRATE", "IM_SPESE") AS 
  (
select
--
-- Date: 07/08/2002
-- Version: 1.0
--
-- Vista di estrazione del saldo assestato entrate/spese per CDS
--
-- History:
--
-- Date: 07/08/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
 cd_cds,
 esercizio,
 ti_appartenenza,
 sum(im_entrate),
 sum(im_spese)
from (
  select
   cd_cds,
   esercizio,
   ti_appartenenza,
   sum(im_stanz_iniziale_a1 + variazioni_piu - variazioni_meno) im_entrate,
   0 im_spese
  from voce_f_saldi_cmp where
       ti_gestione = 'E'
   and ti_competenza_residuo = 'C'
  group by
   cd_cds,
   esercizio,
   ti_appartenenza
 union all
  select
   cd_cds,
   esercizio,
   ti_appartenenza,
   0 im_entrate,
   sum(im_stanz_iniziale_a1 + variazioni_piu - variazioni_meno) im_spese
  from voce_f_saldi_cmp where
       ti_gestione = 'S'
   and ti_competenza_residuo = 'C'
  group by
   cd_cds,
   esercizio,
   ti_appartenenza
 ) group by
 cd_cds,
 esercizio,
 ti_appartenenza
)
;

   COMMENT ON TABLE "V_PREVENTIVO_PAREGGIO"  IS 'Vista di estrazione del saldo assestato entrate/spese per CDS';
