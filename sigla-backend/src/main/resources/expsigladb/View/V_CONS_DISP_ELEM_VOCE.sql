--------------------------------------------------------
--  DDL for View V_CONS_DISP_ELEM_VOCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_DISP_ELEM_VOCE" ("ESERCIZIO", "ESERCIZIO_RES", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "CD_MODULO", "CD_ELEMENTO_VOCE", "CD_VOCE", "IM_STANZ_INIZIALE_A1", "VARIAZIONI_PIU", "VARIAZIONI_MENO", "ASSESTATO", "IM_OBBL_ACC_COMP", "DISPONIBILITA") AS 
  select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Consultazione disponibilità Elemento Voce
--
--
-- History:
--
-- Date: 03/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
 a.esercizio,
 a.esercizio_res,
 b.cd_unita_organizzativa,
 a.cd_centro_responsabilita,
 a.cd_linea_attivita,
 decode(modu.cd_progetto,null,'Modulo non definito',modu.cd_progetto),
 a.cd_elemento_voce,
 a.cd_voce,
 a.im_stanz_iniziale_a1,
 a.variazioni_piu,
 a.variazioni_meno,
 (a.im_stanz_iniziale_a1+a.variazioni_piu-a.variazioni_meno) assestato,
 a.im_obbl_acc_comp,
 (a.im_stanz_iniziale_a1+a.variazioni_piu-a.variazioni_meno-a.im_obbl_acc_comp) disponibilita
from voce_f_saldi_cdr_linea a, cdr b, linea_attivita la, progetto_gest modu
where a.cd_centro_responsabilita = b.cd_centro_responsabilita
and la.cd_linea_attivita = a.cd_linea_attivita
and la.cd_centro_responsabilita = a.cd_centro_responsabilita
and la.pg_progetto = modu.pg_progetto(+)
And (modu.esercizio Is Null Or modu.esercizio = a.esercizio)
and a.esercizio = a.esercizio_res
union
select
 a.esercizio,
 a.esercizio_res,
 b.cd_unita_organizzativa,
 a.cd_centro_responsabilita,
 a.cd_linea_attivita,
 decode(modu.cd_progetto,null,'Modulo non definito',modu.cd_progetto),
 a.cd_elemento_voce,
 a.cd_voce,
 a.im_stanz_res_improprio-a.var_piu_obbl_res_pro+a.var_meno_obbl_res_pro im_stanz_res_improprio,
 a.var_piu_stanz_res_imp,
 a.var_meno_stanz_res_imp,
 (a.im_stanz_res_improprio+a.var_piu_stanz_res_imp-a.var_meno_stanz_res_imp-
  a.var_piu_obbl_res_pro+a.var_meno_obbl_res_pro) assestato,
 (a.im_obbl_res_imp+a.var_piu_obbl_res_imp-a.var_meno_obbl_res_imp),
 (a.im_stanz_res_improprio+a.var_piu_stanz_res_imp-a.var_meno_stanz_res_imp-(a.im_obbl_res_imp+a.var_piu_obbl_res_imp-a.var_meno_obbl_res_imp+a.var_piu_obbl_res_pro-a.var_meno_obbl_res_pro)) disponibilita
from voce_f_saldi_cdr_linea a, cdr b, linea_attivita la, progetto_gest modu
where a.cd_centro_responsabilita = b.cd_centro_responsabilita
and la.cd_linea_attivita = a.cd_linea_attivita
and la.cd_centro_responsabilita = a.cd_centro_responsabilita
and la.pg_progetto = modu.pg_progetto(+)
And (modu.esercizio Is Null Or modu.esercizio = a.esercizio)
and a.esercizio > a.esercizio_res;
