--------------------------------------------------------
--  DDL for View V_STAMPA_BILANCIO_RENDIC
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STAMPA_BILANCIO_RENDIC" ("FONTE", "ESERCIZIO", "TIPO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "CD_LIVELLO8", "CD_LIVELLO9", "DS_LIVELLO1", "DS_LIVELLO2", "DS_LIVELLO3", "DS_LIVELLO4", "DS_LIVELLO5", "DS_LIVELLO6", "DS_LIVELLO7", "DS_LIVELLO8", "DS_LIVELLO9", "PREVISIONE_INI", "SALDO_VARIAZIONI", "TOT_IMPACC_COMP", "TOT_IMPACC_RES", "TOT_MANREV_COMP", "TOT_MANREV_RES", "TOT_MOD_IMPACC_RES", "ASSESTATO_CASSA", "PREVISIONE_INI_ES_PREC", "SALDO_VARIAZIONI_ES_PREC", "TOT_IMPACC_COMP_ES_PREC", "TOT_IMPACC_RES_ES_PREC", "TOT_MANREV_COMP_ES_PREC", "TOT_MANREV_RES_ES_PREC", "TOT_MOD_IMPACC_RES_ES_PREC", "ASSESTATO_CASSA_ES_PREC") AS 
  (SELECT   a.fonte, a.esercizio, a.tipo, 
             a.cd_livello1, a.cd_livello2, a.cd_livello3, a.cd_livello4,
             a.cd_livello5, a.cd_livello6, a.cd_livello7, a.cd_livello8, a.cd_livello9,
             a.ds_livello1, a.ds_livello2, a.ds_livello3, a.ds_livello4,
             a.ds_livello5, a.ds_livello6, a.ds_livello7, a.ds_livello8, a.ds_livello9, 
             SUM (a.previsione_ini),
             SUM (a.saldo_variazioni), SUM (a.tot_impacc_comp),
             SUM (a.tot_impacc_res), SUM (a.tot_manrev_comp),
             SUM (a.tot_manrev_res), SUM (a.tot_mod_impacc_res),
             SUM (a.assestato_cassa), SUM (a.previsione_ini_es_prec),
             SUM (a.saldo_variazioni_es_prec),
             SUM (a.tot_impacc_comp_es_prec), SUM (a.tot_impacc_res_es_prec),
             SUM (a.tot_manrev_comp_es_prec), SUM (a.tot_manrev_res_es_prec),
             SUM (a.tot_mod_impacc_res_es_prec),
             SUM (a.assestato_cassa_es_prec)
    FROM v_stampa_bilancio_rendic_x_cdr a
    GROUP BY a.fonte,
             a.esercizio,
             a.tipo,
             a.cd_livello1,
             a.cd_livello2,
             a.cd_livello3,
             a.cd_livello4,
             a.cd_livello5,
             a.cd_livello6,
             a.cd_livello7,
             a.cd_livello8,
             a.cd_livello9,
             a.ds_livello1,
             a.ds_livello2,
             a.ds_livello3,
             a.ds_livello4,
             a.ds_livello5,
             a.ds_livello6,
             a.ds_livello7,
             a.ds_livello8,
             a.ds_livello9) ;
