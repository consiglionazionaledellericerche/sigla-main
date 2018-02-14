--------------------------------------------------------
--  DDL for View V_CDP_MATRICOLA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_MATRICOLA" ("ESERCIZIO", "MESE", "ID_MATRICOLA", "TI_PREV_CONS", "CD_UO_CARICO", "CD_UNITA_ORGANIZZATIVA", "TI_APPARTENENZA", "TI_GESTIONE", "TI_RAPPORTO", "IM_A1", "IM_A2", "IM_A3", "TI_PROVENIENZA", "STATO_CARICO", "NOMINATIVO", "DT_SCAD_CONTRATTO", "CD_LIVELLO_1", "CD_LIVELLO_2", "CD_LIVELLO_3", "CD_PROFILO", "DS_PROFILO", "ORIGINE_FONTI", "FL_RAPPORTO13") AS 
  (SELECT
--
-- Date: 28/09/2011
-- Version: 1.8
--
-- Aggrega da tabella COSTO_DEL_DIPENDENTE per matricola indipendentemente dalla voce
-- Vengono riportati anche i costi del dipendente scaricati su altra UO attraverso ASS_CDP_UO
--
-- History:
-- Date: 02/10/2001
-- Version: 1.1
-- Creazione
--
-- Date: 29/12/2001
-- Version: 1.2
-- Completamento gestione scarico costi dipendenti su PDG
--
-- Date: 27/02/2002
-- Version: 1.3
-- Modificato semantica di CD_UNITA_ORGANIZZATIVA: ora e l'uo a cui appartiene il dipendente,
-- prima era l'uo su cui caricare i costi. Aggiunto il campo CD_UO_CARICO che ha la semantica
-- del vecchio CD_UNITA_ORGANIZZATIVA.
--
-- Date: 27/03/2002
-- Version: 1.4
-- Aggiunti i campi NOMINATIVO, DT_SCAD_CONTRATTO, CD_LIVELLO_x, CD_PROFILO, DS_PROFILO
--
-- Date: 16/04/2002
-- Version: 1.5
-- I campi IM_An sono ora la somma dei campi
--     COSTO_DEL_DIPENDENTE.IM_An +
--  COSTO_DEL_DIPENDENTE.IM_ONERI_CNR_An +
--  COSTO_DEL_DIPENDENTE.IM_TFR_An
--
-- Date: 19/09/2001
-- Version: 1.6
-- Aggiunta colonna MESE
--
-- Date: 26/09/2007
-- Version: 1.7
-- Aggiunta colonna ORIGINE_FONTI
--
-- Date: 28/09/2011
-- Version: 1.8
-- Aggiunta colonna FL_RAPPORTO13
--
-- Body:
--
-- Table: COSTO_DEL_DIPENDENTE
             costo_del_dipendente.esercizio, costo_del_dipendente.mese,
             costo_del_dipendente.id_matricola,
             costo_del_dipendente.ti_prev_cons,
             costo_del_dipendente.cd_unita_organizzativa,
             costo_del_dipendente.cd_unita_organizzativa,
             costo_del_dipendente.ti_appartenenza,
             costo_del_dipendente.ti_gestione,
             costo_del_dipendente.ti_rapporto,
             SUM (  costo_del_dipendente.im_a1
                  + costo_del_dipendente.im_oneri_cnr_a1
                  + costo_del_dipendente.im_tfr_a1
                 ),
             SUM (  costo_del_dipendente.im_a2
                  + costo_del_dipendente.im_oneri_cnr_a2
                  + costo_del_dipendente.im_tfr_a2
                 ),
             SUM (  costo_del_dipendente.im_a3
                  + costo_del_dipendente.im_oneri_cnr_a3
                  + costo_del_dipendente.im_tfr_a3
                 ),
             'I' /* provenienza interna */, NULL,
             costo_del_dipendente.nominativo,
             costo_del_dipendente.dt_scad_contratto,
             costo_del_dipendente.cd_livello_1,
             costo_del_dipendente.cd_livello_2,
             costo_del_dipendente.cd_livello_3,
             costo_del_dipendente.cd_profilo, costo_del_dipendente.ds_profilo,
             costo_del_dipendente.origine_fonti,
             costo_del_dipendente.fl_rapporto13             
        FROM costo_del_dipendente
    GROUP BY costo_del_dipendente.esercizio,
             costo_del_dipendente.mese,
             costo_del_dipendente.id_matricola,
             costo_del_dipendente.ti_prev_cons,
             costo_del_dipendente.cd_unita_organizzativa,
             costo_del_dipendente.cd_unita_organizzativa,
             costo_del_dipendente.ti_appartenenza,
             costo_del_dipendente.ti_gestione,
             costo_del_dipendente.ti_rapporto,
             costo_del_dipendente.dt_scarico,
             costo_del_dipendente.nominativo,
             costo_del_dipendente.dt_scad_contratto,
             costo_del_dipendente.cd_livello_1,
             costo_del_dipendente.cd_livello_2,
             costo_del_dipendente.cd_livello_3,
             costo_del_dipendente.cd_profilo,
             costo_del_dipendente.ds_profilo,
             costo_del_dipendente.origine_fonti,
             costo_del_dipendente.fl_rapporto13
    UNION ALL
    SELECT   costo_del_dipendente.esercizio, costo_del_dipendente.mese,
             costo_del_dipendente.id_matricola,
             costo_del_dipendente.ti_prev_cons,
             ass_cdp_uo.cd_unita_organizzativa,
             costo_del_dipendente.cd_unita_organizzativa,
             costo_del_dipendente.ti_appartenenza,
             costo_del_dipendente.ti_gestione,
             costo_del_dipendente.ti_rapporto,
               SUM (  costo_del_dipendente.im_a1
                    + costo_del_dipendente.im_oneri_cnr_a1
                    + costo_del_dipendente.im_tfr_a1
                   )
             * ass_cdp_uo.prc_uo_a1
             / 100,
               SUM (  costo_del_dipendente.im_a2
                    + costo_del_dipendente.im_oneri_cnr_a2
                    + costo_del_dipendente.im_tfr_a2
                   )
             * ass_cdp_uo.prc_uo_a2
             / 100,
               SUM (  costo_del_dipendente.im_a3
                    + costo_del_dipendente.im_oneri_cnr_a3
                    + costo_del_dipendente.im_tfr_a3
                   )
             * ass_cdp_uo.prc_uo_a3
             / 100,
             'C'                                    /* Caricato da altra UO */
                ,
             ass_cdp_uo.stato, costo_del_dipendente.nominativo,
             costo_del_dipendente.dt_scad_contratto,
             costo_del_dipendente.cd_livello_1,
             costo_del_dipendente.cd_livello_2,
             costo_del_dipendente.cd_livello_3,
             costo_del_dipendente.cd_profilo, costo_del_dipendente.ds_profilo,
             costo_del_dipendente.origine_fonti,
             costo_del_dipendente.fl_rapporto13
        FROM ass_cdp_uo, costo_del_dipendente
       WHERE ass_cdp_uo.id_matricola = costo_del_dipendente.id_matricola
         AND ass_cdp_uo.esercizio = costo_del_dipendente.esercizio
         AND ass_cdp_uo.mese = costo_del_dipendente.mese
    GROUP BY costo_del_dipendente.esercizio,
             costo_del_dipendente.mese,
             costo_del_dipendente.id_matricola,
             costo_del_dipendente.ti_prev_cons,
             ass_cdp_uo.cd_unita_organizzativa,
             costo_del_dipendente.cd_unita_organizzativa,
             costo_del_dipendente.ti_appartenenza,
             costo_del_dipendente.ti_gestione,
             costo_del_dipendente.ti_rapporto,
             costo_del_dipendente.dt_scarico,
             ass_cdp_uo.stato,
             ass_cdp_uo.prc_uo_a1,
             ass_cdp_uo.prc_uo_a2,
             ass_cdp_uo.prc_uo_a3,
             costo_del_dipendente.nominativo,
             costo_del_dipendente.dt_scad_contratto,
             costo_del_dipendente.cd_livello_1,
             costo_del_dipendente.cd_livello_2,
             costo_del_dipendente.cd_livello_3,
             costo_del_dipendente.cd_profilo,
             costo_del_dipendente.ds_profilo,
             costo_del_dipendente.origine_fonti,
             costo_del_dipendente.fl_rapporto13) ;

   COMMENT ON TABLE "V_CDP_MATRICOLA"  IS 'Aggrega da tabella COSTO_DEL_DIPENDENTE per matricola indipendentemente dalla voce
Vengono riportati anche i costi del dipendente scaricati su altra UO attraverso ASS_CDP_UO';
