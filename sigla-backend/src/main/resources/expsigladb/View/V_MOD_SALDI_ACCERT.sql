--------------------------------------------------------
--  DDL for View V_MOD_SALDI_ACCERT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MOD_SALDI_ACCERT" ("PG_OLD", "CD_CDS", "ESERCIZIO", "ESERCIZIO_ORIGINALE", "PG_ACCERTAMENTO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "IM_DELTA_VOCE", "IM_DELTA_REV_VOCE", "IM_DELTA_INC_VOCE") AS 
  (select
--
-- Date: 19/07/2006
-- Version: 1.3
--
-- Vista di estrazione delle differenze di saldi accertamento rispetto alla versione di storico con pg_storico pg_old
--
-- History:
--
-- Date: 07/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 16/09/2002
-- Version: 1.1
-- Gestione saldi autorizzatori e ultimi
--
-- Date: 17/09/2002
-- Version: 1.2
-- Fix decode
--
-- Date: 19/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 pg_storico_,
 cd_cds,
 esercizio,
 esercizio_originale,
 pg_accertamento,
 ti_appartenenza,
 ti_gestione,
 cd_voce,
 sum(im_delta_voce),
 sum(im_delta_rev_voce),
 sum(im_delta_pag_voce)
from (
 select
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  sum(a.im_accertamento) im_delta_voce,
  0 im_delta_rev_voce,
  0 im_delta_pag_voce
 from
  accertamento_s s,
  accertamento a
 where
      s.cd_cds = a.cd_cds
  and s.esercizio = a.esercizio
  and s.esercizio_originale = a.esercizio_originale
  and s.pg_accertamento = a.pg_accertamento
 group by
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce
union all
 select
  a.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  0 -sum(a.im_accertamento) im_delta_voce,
  0 im_delta_rev_voce,
  0 im_delta_pag_voce
 from
  accertamento_s a
 group by
  a.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce
union all
 select
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  0 im_delta_voce,
  b.im_reversale_riga im_delta_rev_voce,
  decode(c.stato,'P',b.im_reversale_riga,0) im_delta_inc_voce
 from
  accertamento_s s,
  accertamento a,
  accertamento_scadenzario sc,
  reversale_riga b, reversale c
 where
      sc.cd_cds = a.cd_cds
  and sc.esercizio = a.esercizio
  and sc.esercizio_originale = a.esercizio_originale
  and sc.pg_accertamento = a.pg_accertamento
  and b.cd_cds = a.cd_cds
  and b.esercizio = a.esercizio
  and b.esercizio_ori_accertamento = a.esercizio_originale
  and b.pg_accertamento = a.pg_accertamento
  and b.pg_accertamento_scadenzario = sc.pg_accertamento_scadenzario
  and s.cd_cds = a.cd_cds
  and s.esercizio = a.esercizio
  and s.esercizio_originale = a.esercizio_originale
  and s.pg_accertamento = a.pg_accertamento
  and c.cd_cds = b.cd_cds
  and c.esercizio = b.esercizio
  and c.pg_reversale = b.pg_reversale
  and c.stato <> 'A'
union all
 select
  a.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  0 im_delta_voce,
  0 - b.im_reversale_riga im_delta_rev_voce,
  0 - decode(c.stato,'P',b.im_reversale_riga,0) im_delta_inc_voce
 from
  accertamento_s a,
  accertamento_scadenzario_s sc,
  reversale_riga b, reversale c
 where
      b.cd_cds = a.cd_cds
  and b.esercizio = a.esercizio
  and b.esercizio_ori_accertamento = a.esercizio_originale
  and b.pg_accertamento = a.pg_accertamento
  and b.pg_accertamento_scadenzario = sc.pg_accertamento_scadenzario
  and sc.cd_cds = a.cd_cds
  and sc.esercizio = a.esercizio
  and sc.esercizio_originale = a.esercizio_originale
  and sc.pg_accertamento = a.pg_accertamento
  and sc.pg_storico_ = a.pg_storico_
  and c.cd_cds = b.cd_cds
  and c.esercizio = b.esercizio
  and c.pg_reversale = b.pg_reversale
  and c.stato <> 'A'
)
group by
 pg_storico_,
 cd_cds,
 esercizio,
 esercizio_originale,
 pg_accertamento,
 ti_appartenenza,
 ti_gestione,
 cd_voce
);
