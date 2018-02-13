--------------------------------------------------------
--  DDL for View V_STIPENDI_COFI_OBB_IMP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STIPENDI_COFI_OBB_IMP" ("ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "DS_OBBLIGAZIONE", "IM_TOTALE", "IM_DISPONIBILE", "ULTIMO_MESE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE") AS 
  select
--
-- Date: 19/07/2006
-- Version: 1.2
--
-- Per le obbligazioni generate annualmente per la gestione stipendiale, mette a disposizione
-- le informazioni relative all'importo complessivo, alla disponibilità residua ed alla voce del piano imputata
--
-- History:
--
-- Date: 03/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 10/01/2003
-- Version: 1.1
-- Aggiunta la descrizione dell'obbligazione in interfaccia
--
-- Date: 19/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 ESERCIZIO_OBBLIGAZIONE,
 ESERCIZIO_ORI_OBBLIGAZIONE,
 PG_OBBLIGAZIONE,
 DS_OBBLIGAZIONE,
 sum(IM_TOTALE),
 sum(IM_DISPONIBILE),
 sum(ULTIMO_MESE),
 CD_ELEMENTO_VOCE,
 DS_ELEMENTO_VOCE
from (
select
 a.esercizio_obbligazione,
 a.esercizio_ori_obbligazione,
 a.pg_obbligazione,
 b.ds_obbligazione,
 sum(c.im_scadenza) IM_TOTALE,
 sum(c.im_scadenza) - sum(c.im_associato_doc_contabile) IM_DISPONIBILE,
 0 ULTIMO_MESE,
 b.cd_elemento_voce,
 v.ds_elemento_voce
from
 stipendi_cofi_obb a,
 obbligazione b,
 elemento_voce v,
 obbligazione_scadenzario c
where
     b.cd_cds = a.cd_cds_obbligazione
 and b.esercizio = a.esercizio_obbligazione
 and b.esercizio_originale = a.esercizio_ori_obbligazione
 and b.pg_obbligazione = a.pg_obbligazione
 and c.cd_cds = a.cd_cds_obbligazione
 and c.esercizio = a.esercizio_obbligazione
 and c.esercizio_originale = a.esercizio_ori_obbligazione
 and c.pg_obbligazione = a.pg_obbligazione
 and v.esercizio = b.esercizio
 and v.ti_appartenenza = b.ti_appartenenza
 and v.ti_gestione = b.ti_gestione
 and v.cd_elemento_voce = b.cd_elemento_voce
 and a.esercizio=a.esercizio_obbligazione
group by
 a.esercizio_obbligazione,
 a.esercizio_ori_obbligazione,
 a.pg_obbligazione,
 b.ds_obbligazione,
 b.cd_elemento_voce,
 v.ds_elemento_voce
union all
 select
 a.esercizio_obbligazione,
 a.esercizio_ori_obbligazione,
 a.pg_obbligazione,
 b.ds_obbligazione,
 0 IM_TOTALE,
 0 IM_TOTALE,
 max(s.mese),
 b.cd_elemento_voce,
 v.ds_elemento_voce
from
 stipendi_cofi_obb a,
 obbligazione b,
 elemento_voce v,
 stipendi_cofi s
where
     b.cd_cds = a.cd_cds_obbligazione
 and b.esercizio = a.esercizio_obbligazione
 and b.esercizio_originale = a.esercizio_ori_obbligazione
 and b.pg_obbligazione = a.pg_obbligazione
 and v.esercizio = b.esercizio
 and v.ti_appartenenza = b.ti_appartenenza
 and v.ti_gestione = b.ti_gestione
 and v.cd_elemento_voce = b.cd_elemento_voce
 and s.stato = 'P'
 and s.esercizio = a.esercizio
 and a.esercizio=a.esercizio_obbligazione
group by
 a.esercizio_obbligazione,
 a.esercizio_ori_obbligazione,
 a.pg_obbligazione,
 b.ds_obbligazione,
 b.cd_elemento_voce,
 v.ds_elemento_voce
)
group by
 ESERCIZIO_OBBLIGAZIONE,
 ESERCIZIO_ORI_OBBLIGAZIONE,
 PG_OBBLIGAZIONE,
 DS_OBBLIGAZIONE,
 CD_ELEMENTO_VOCE,
 DS_ELEMENTO_VOCE;

   COMMENT ON TABLE "V_STIPENDI_COFI_OBB_IMP"  IS 'Per le obbligazioni generate annualmente per la gestione stipendiale, mette a disposizione
le informazioni relative all''importo complessivo, alla disponibilità residua ed alla voce del piano imputata';
