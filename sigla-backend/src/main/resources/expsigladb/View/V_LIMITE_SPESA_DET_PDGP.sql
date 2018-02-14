--------------------------------------------------------
--  DDL for View V_LIMITE_SPESA_DET_PDGP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIMITE_SPESA_DET_PDGP" ("ESERCIZIO", "CD_CDS", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "FONTE", "IMPORTO_LIMITE", "IMPORTO_PREVISTO", "CD_CLASSIFICAZIONE", "ID_CLASSIFICAZIONE", "CD_PROGETTO", "DS_PROGETTO", "CD_AREA") AS 
  select d.esercizio, CNRUTL001.getCdsFromCdr(s.cd_centro_responsabilita), d.ti_appartenenza, d.ti_gestione,
            d.cd_elemento_voce, e.ds_elemento_voce, d.fonte, d.importo_limite,
            decode(d.fonte,'FIN',nvl(IM_SPESE_GEST_DECENTRATA_INT,0),nvl(IM_SPESE_GEST_DECENTRATA_EST,0)) ,
            v.cd_classificazione, v.id_classificazione,p.cd_progetto,p.ds_progetto,s.cd_cds_area
            FROM limite_spesa_det d, elemento_voce e,v_classificazione_voci v,pdg_modulo_spese s ,progetto_prev p
            where         e.esercizio = d.esercizio_voce and
                          e.ti_appartenenza = d.ti_appartenenza                 and
                          e.ti_gestione = d.ti_gestione                                 and
                          e.cd_elemento_voce = d.cd_elemento_voce      and
                          s.id_classificazione = e.id_classificazione and
                          v.id_classificazione = s.id_classificazione   and
                          d.cd_cds    = s.cd_cds_area   and
                          d.esercizio = s.esercizio     and
                          p.pg_progetto = s.pg_progetto and
                          p.esercizio = s.esercizio and
                          p.tipo_fase = 'P'
   ORDER BY d.esercizio, d.cd_cds, d.cd_elemento_voce, d.fonte;
