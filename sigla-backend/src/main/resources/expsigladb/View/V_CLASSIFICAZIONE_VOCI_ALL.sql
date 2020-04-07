--------------------------------------------------------
--  DDL for View V_CLASSIFICAZIONE_VOCI_ALL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CLASSIFICAZIONE_VOCI_ALL" ("ID_CLASSIFICAZIONE", "ESERCIZIO", "TI_GESTIONE", "DS_CLASSIFICAZIONE", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "ID_CLASS_PADRE", "NR_LIVELLO", "FL_MASTRINO", "FL_CLASS_SAC", "FL_SOLO_GESTIONE", "FL_PIANO_RIPARTO", "FL_ACCENTRATO", "FL_DECENTRATO", "FL_ESTERNA_DA_QUADRARE_SAC", "CDR_ACCENTRATORE", "TI_CLASSIFICAZIONE", "FL_PREV_OBB_ANNO_SUC", "IM_LIMITE_ASSESTATO", "DUVA", "UTUV", "DACR", "UTCR", "PG_VER_REC", "CD_CLASSIFICAZIONE", "CD_LIV1", "CD_LIV2", "CD_LIV3", "CD_LIV4", "CD_LIV5", "CD_LIV6", "CD_LIV7", "ID_LIV1", "ID_LIV2", "ID_LIV3", "ID_LIV4", "ID_LIV5", "ID_LIV6", "ID_LIV7", "DS_LIV1", "DS_LIV2", "DS_LIV3", "DS_LIV4", "DS_LIV5", "DS_LIV6", "DS_LIV7") AS
  SELECT
--
-- Date: 08/09/2005
-- Version: 1.0
--
-- Estrae il codice di tutte le Classificazioni Ufficiali
-- Simile a V_CLASSIFICAZIONE_VOCI con l'aggiunta che per ogni classificazione viene riproposto anche il codice
-- dei livelli precedenti
--
-- History:
-- Date: 08/09/2005
-- Version: 1.0
-- Creazione
          a.id_classificazione, a.esercizio, a.ti_gestione,
          a.ds_classificazione, a.cd_livello1, a.cd_livello2, a.cd_livello3,
          a.cd_livello4, a.cd_livello5, a.cd_livello6, a.cd_livello7,
          a.id_class_padre, a.nr_livello, a.fl_mastrino, a.fl_class_sac,
          a.fl_solo_gestione, a.fl_piano_riparto, a.fl_accentrato,
          a.fl_decentrato, a.fl_esterna_da_quadrare_sac, a.cdr_accentratore,
          a.ti_classificazione, a.fl_prev_obb_anno_suc, a.im_limite_assestato,
          a.duva, a.utuv, a.dacr, a.utcr, a.pg_ver_rec, a.cd_classificazione,
          DECODE (a.nr_livello,
                  1, a.cd_classificazione,
                  2, b.cd_classificazione,
                  3, c.cd_classificazione,
                  4, d.cd_classificazione,
                  5, e.cd_classificazione,
                  6, f.cd_classificazione,
                  7, g.cd_classificazione,
                  NULL
                 ) cd_liv1,
          DECODE (a.nr_livello,
                  2, a.cd_classificazione,
                  3, b.cd_classificazione,
                  4, c.cd_classificazione,
                  5, d.cd_classificazione,
                  6, e.cd_classificazione,
                  7, f.cd_classificazione,
                  NULL
                 ) cd_liv2,
          DECODE (a.nr_livello,
                  3, a.cd_classificazione,
                  4, b.cd_classificazione,
                  5, c.cd_classificazione,
                  6, d.cd_classificazione,
                  7, e.cd_classificazione,
                  NULL
                 ) cd_liv3,
          DECODE (a.nr_livello,
                  4, a.cd_classificazione,
                  5, b.cd_classificazione,
                  6, c.cd_classificazione,
                  7, d.cd_classificazione,
                  NULL
                 ) cd_liv4,
          DECODE (a.nr_livello,
                  5, a.cd_classificazione,
                  6, b.cd_classificazione,
                  7, c.cd_classificazione,
                  NULL
                 ) cd_liv5,
          DECODE (a.nr_livello,
                  6, a.cd_classificazione,
                  7, b.cd_classificazione,
                  NULL
                 ) cd_liv6,
          DECODE (a.nr_livello, 7, a.cd_classificazione, NULL) cd_liv7,
          DECODE (a.nr_livello,
                  1, a.id_classificazione,
                  2, b.id_classificazione,
                  3, c.id_classificazione,
                  4, d.id_classificazione,
                  5, e.id_classificazione,
                  6, f.id_classificazione,
                  7, g.id_classificazione,
                  NULL
                 ) id_liv1,
          DECODE (a.nr_livello,
                  2, a.id_classificazione,
                  3, b.id_classificazione,
                  4, c.id_classificazione,
                  5, d.id_classificazione,
                  6, e.id_classificazione,
                  7, f.id_classificazione,
                  NULL
                 ) id_liv2,
          DECODE (a.nr_livello,
                  3, a.id_classificazione,
                  4, b.id_classificazione,
                  5, c.id_classificazione,
                  6, d.id_classificazione,
                  7, e.id_classificazione,
                  NULL
                 ) id_liv3,
          DECODE (a.nr_livello,
                  4, a.id_classificazione,
                  5, b.id_classificazione,
                  6, c.id_classificazione,
                  7, d.id_classificazione,
                  NULL
                 ) id_liv4,
          DECODE (a.nr_livello,
                  5, a.id_classificazione,
                  6, b.id_classificazione,
                  7, c.id_classificazione,
                  NULL
                 ) id_liv5,
          DECODE (a.nr_livello,
                  6, a.id_classificazione,
                  7, b.id_classificazione,
                  NULL
                 ) id_liv6,
          DECODE (a.nr_livello, 7, a.id_classificazione, NULL) id_liv7,
          DECODE (a.nr_livello,
                  1, a.ds_classificazione,
                  2, b.ds_classificazione,
                  3, c.ds_classificazione,
                  4, d.ds_classificazione,
                  5, e.ds_classificazione,
                  6, f.ds_classificazione,
                  7, g.ds_classificazione,
                  NULL
                 ) ds_liv1,
          DECODE (a.nr_livello,
                  2, a.ds_classificazione,
                  3, b.ds_classificazione,
                  4, c.ds_classificazione,
                  5, d.ds_classificazione,
                  6, e.ds_classificazione,
                  7, f.ds_classificazione,
                  NULL
                 ) ds_liv2,
          DECODE (a.nr_livello,
                  3, a.ds_classificazione,
                  4, b.ds_classificazione,
                  5, c.ds_classificazione,
                  6, d.ds_classificazione,
                  7, e.ds_classificazione,
                  NULL
                 ) ds_liv3,
          DECODE (a.nr_livello,
                  4, a.ds_classificazione,
                  5, b.ds_classificazione,
                  6, c.ds_classificazione,
                  7, d.ds_classificazione,
                  NULL
                 ) ds_liv4,
          DECODE (a.nr_livello,
                  5, a.ds_classificazione,
                  6, b.ds_classificazione,
                  7, c.ds_classificazione,
                  NULL
                 ) ds_liv5,
          DECODE (a.nr_livello,
                  6, a.ds_classificazione,
                  7, b.ds_classificazione,
                  NULL
                 ) ds_liv6,
          DECODE (a.nr_livello, 7, a.ds_classificazione, NULL) ds_liv7
     FROM v_classificazione_voci a,
          v_classificazione_voci b,
          v_classificazione_voci c,
          v_classificazione_voci d,
          v_classificazione_voci e,
          v_classificazione_voci f,
          v_classificazione_voci g
    WHERE a.id_class_padre = b.id_classificazione(+)
      AND b.id_class_padre = c.id_classificazione(+)
      AND c.id_class_padre = d.id_classificazione(+)
      AND d.id_class_padre = e.id_classificazione(+)
      AND e.id_class_padre = f.id_classificazione(+)
      AND f.id_class_padre = g.id_classificazione(+) ;
