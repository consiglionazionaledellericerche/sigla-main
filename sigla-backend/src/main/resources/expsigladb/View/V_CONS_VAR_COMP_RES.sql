--------------------------------------------------------
--  DDL for View V_CONS_VAR_COMP_RES
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_VAR_COMP_RES" ("TIPO_VAR", "ESERCIZIO", "NUM_VAR", "RIFERIMENTI_DESC_VARIAZIONE", "DESC_VARIAZIONE", "CDR_PROPONENTE", "DESC_CDR_PROPONENTE", "ES_RESIDUO", "CDR_ASSEGN", "DESC_CDR_ASSEGN", "DS_TIPO_VARIAZIONE", "STATO", "FONTE", "IMPORTO", "IM_DEC_INT", "IM_DEC_EST", "IM_ACC_INT", "IM_ACC_EST", "IM_ENTRATA", "GAE", "VOCE_DEL_PIANO", "DT_APPROVAZIONE", "TI_MOTIVAZIONE_VARIAZIONE") AS
  (
select
'Competenza' tipo_var, a.esercizio, a.pg_variazione_pdg num_var, a.ds_variazione riferimenti_desc_variazione, a.riferimenti desc_variazione, a.cd_centro_responsabilita cdr_proponente,
(select c.ds_cdr from cdr c where a.cd_centro_responsabilita = c.cd_centro_responsabilita) desc_cdr_proponente,
0 es_residuo, b.cd_cdr_assegnatario cdr_assegn,
(select c.ds_cdr from cdr c where b.cd_cdr_assegnatario = c.cd_centro_responsabilita) desc_cdr_assegn,
d.ds_tipo_variazione ds_tipo_variazione,
a.stato, a.tipologia_fin fonte, 0 importo, b.im_spese_gest_decentrata_int im_dec_int, b.im_spese_gest_decentrata_est im_dec_est, b.im_spese_gest_accentrata_int im_acc_int, b.im_spese_gest_accentrata_est im_acc_est,
b.im_entrata,b.cd_linea_attivita gae, b.cd_elemento_voce voce_del_piano,a.dt_approvazione,a.ti_motivazione_variazione
from
pdg_variazione a, pdg_variazione_riga_gest b, cdr c, tipo_variazione d
where
a.pg_variazione_pdg = b.pg_variazione_pdg
and a.cd_centro_responsabilita = c.cd_centro_responsabilita
and a.esercizio = b.esercizio
and d.cd_tipo_variazione = a.tipologia
and d.esercizio = a.esercizio
union
select
'Residuo', a.esercizio, a.pg_variazione num_var, a.ds_variazione riferimenti_desc_variazione, a.ds_delibera desc_variazione, a.cd_centro_responsabilita cdr_proponente,
(select c.ds_cdr from cdr c where a.cd_centro_responsabilita = c.cd_centro_responsabilita) desc_cdr_proponente,
a.esercizio_res es_residuo, b.cd_cdr cdr_assegn,
(select c.ds_cdr from cdr c where b.cd_cdr = c.cd_centro_responsabilita) desc_cdr_assegn,
a.tipologia ds_tipo_variazione,
a.stato, a.tipologia_fin fonte, b.im_variazione importo, 0 im_dec_int, 0 im_dec_est, 0 im_acc_int, 0 im_acc_est,0 im_entrata, b.cd_linea_attivita gae,
b.cd_elemento_voce voce_del_piano,a.dt_approvazione,a.ti_motivazione_variazione
from
var_stanz_res a, var_stanz_res_riga b, cdr c
where
a.pg_variazione = b.pg_variazione
and a.cd_centro_responsabilita = c.cd_centro_responsabilita
and a.esercizio = b.esercizio
);
