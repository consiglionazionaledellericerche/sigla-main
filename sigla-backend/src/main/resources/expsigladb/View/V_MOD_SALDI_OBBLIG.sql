--------------------------------------------------------
--  DDL for View V_MOD_SALDI_OBBLIG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MOD_SALDI_OBBLIG" ("PG_OLD", "CD_CDS", "ESERCIZIO", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "IM_DELTA_VOCE", "IM_DELTA_MAN_VOCE", "IM_DELTA_PAG_VOCE") AS 
  select
--
-- Date: 18/07/2006
-- Version: 1.3
--
-- Vista di estrazione delle differenze di saldi obbligazione rispetto alla versione di storico con pg_storico pg_old
--
-- History:
--
-- Date: 07/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 16/09/2002
-- Version: 1.1
-- Aggiunti i delta saldi autorizz./ultimi per cambio conto in obbligazione
--
-- Date: 17/09/2002
-- Version: 1.2
-- Fix decode
--
-- Date: 18/07/2006
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
 pg_obbligazione,
 ti_appartenenza,
 ti_gestione,
 cd_voce,
 sum(im_delta_voce),
 sum(im_delta_man_voce),
 sum(im_delta_pag_voce)
from (
 select
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  sum(a.im_voce) im_delta_voce,
  0 im_delta_man_voce,
  0 im_delta_pag_voce
 from
  obbligazione_s s, obbligazione_scad_voce a
 where
      s.cd_cds = a.cd_cds
  and s.esercizio = a.esercizio
  and s.esercizio_originale = a.esercizio_originale
  and s.pg_obbligazione = a.pg_obbligazione
 group by
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce
union all
 select
  a.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  0 -sum(a.im_voce) im_delta_voce,
  0 im_delta_man_voce,
  0 im_delta_pag_voce
 from
  obbligazione_s s, obbligazione_scad_voce_s a
 where
      s.cd_cds = a.cd_cds
  and s.esercizio = a.esercizio
  and s.esercizio_originale = a.esercizio_originale
  and s.pg_obbligazione = a.pg_obbligazione
  and s.pg_storico_ = a.pg_storico_
 group by
  a.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce
union all
 select
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  0 im_delta_voce,
  decode(u.cd_tipo_unita,'ENTE',b.im_mandato_riga,decode(s.fl_pgiro,'Y',b.im_mandato_riga,a.im_voce)) im_delta_man_voce,
  decode(c.stato,'P',decode(u.cd_tipo_unita,'ENTE',b.im_mandato_riga,decode(s.fl_pgiro,'Y',b.im_mandato_riga,a.im_voce)),0) im_delta_pag_voce
 from
  unita_organizzativa u,
  obbligazione_s s,
  obbligazione_scadenzario os,
  obbligazione_scad_voce a,
  mandato_riga b, mandato c
 where
      s.cd_cds = a.cd_cds
  and s.esercizio = a.esercizio
  and s.pg_obbligazione = a.pg_obbligazione
  and b.cd_cds = a.cd_cds
  and b.esercizio = a.esercizio
  and b.esercizio_ori_obbligazione = a.esercizio_originale
  and b.pg_obbligazione = a.pg_obbligazione
  and b.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
  and os.cd_cds = a.cd_cds
  and os.esercizio = a.esercizio
  and os.esercizio_originale = a.esercizio_originale
  and os.pg_obbligazione = a.pg_obbligazione
  and os.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
  and c.cd_cds = b.cd_cds
  and c.esercizio = b.esercizio
  and c.pg_mandato = b.pg_mandato
  and u.cd_unita_organizzativa = a.cd_cds
  and u.fl_cds = 'Y'
  and c.stato <> 'A'
union all
 select
  a.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  0 im_delta_voce,
  0-decode(u.cd_tipo_unita,'ENTE',b.im_mandato_riga,decode(s.fl_pgiro,'Y',b.im_mandato_riga,a.im_voce)) im_delta_man_voce,
  0-decode(c.stato,'P',decode(u.cd_tipo_unita,'ENTE',b.im_mandato_riga,decode(s.fl_pgiro,'Y',b.im_mandato_riga,a.im_voce)),0) im_delta_pag_voce
 from
  obbligazione_scad_voce_s a,
  unita_organizzativa u,
  obbligazione_s s,
  obbligazione_scadenzario_s os,
  mandato_riga b, mandato c
 where
      s.cd_cds = a.cd_cds
  and s.esercizio = a.esercizio
  and s.esercizio_originale = a.esercizio_originale
  and s.pg_obbligazione = a.pg_obbligazione
  and s.pg_storico_ = a.pg_storico_
  and b.cd_cds = a.cd_cds
  and b.esercizio = a.esercizio
  and b.esercizio_ori_obbligazione = a.esercizio_originale
  and b.pg_obbligazione = a.pg_obbligazione
  and b.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
  and os.cd_cds = a.cd_cds
  and os.esercizio = a.esercizio
  and os.esercizio_originale = a.esercizio_originale
  and os.pg_obbligazione = a.pg_obbligazione
  and os.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
  and os.pg_storico_ = a.pg_storico_
  and c.cd_cds = b.cd_cds
  and c.esercizio = b.esercizio
  and c.pg_mandato = b.pg_mandato
  and u.cd_unita_organizzativa = a.cd_cds
  and u.fl_cds = 'Y'
  and c.stato <> 'A'
)
group by
 pg_storico_,
 cd_cds,
 esercizio,
 esercizio_originale,
 pg_obbligazione,
 ti_appartenenza,
 ti_gestione,
 cd_voce;
