--------------------------------------------------------
--  DDL for View V_INCARICHI_ELENCO_FP_2
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INCARICHI_ELENCO_FP_2" ("ESERCIZIO", "PG_REPERTORIO", "CD_CDS", "DS_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "STATO", "CD_TERZO", "NOMINATIVO", "OGGETTO", "CD_PROC_AMM", "CD_TIPO_INCARICO", "DS_TIPO_INCARICO", "CD_TIPO_ATTIVITA", "DS_TIPO_ATTIVITA", "TIPO_NATURA", "FL_MERAMENTE_OCCASIONALE", "FL_ART51", "FL_INVIATO_CORTE_CONTI", "IMPORTO_LORDO", "IMPORTO_VARIAZIONE", "DT_INIZIO_VALIDITA", "DT_FINE_VALIDITA", "DT_FINE_VALIDITA_VARIAZIONE", "DT_STIPULA", "DS_PROVVEDIMENTO", "FL_PUBBLICA_CONTRATTO", "ESERCIZIO_PROCEDURA", "PG_PROCEDURA") AS 
  (SELECT incarichi_repertorio.esercizio, incarichi_repertorio.pg_repertorio,
           incarichi_repertorio.cd_cds, cds.ds_unita_organizzativa,
           uo.cd_unita_organizzativa, uo.ds_unita_organizzativa,
           incarichi_repertorio.stato, incarichi_repertorio.cd_terzo,
           DECODE (v_anagrafico_terzo.ti_entita,
                   'F', NVL (   v_anagrafico_terzo.cognome
                             || ' '
                             || v_anagrafico_terzo.nome,
                             v_anagrafico_terzo.ragione_sociale
                            ),
                   NVL (v_anagrafico_terzo.ragione_sociale,
                           v_anagrafico_terzo.cognome
                        || ' '
                        || v_anagrafico_terzo.nome
                       )
                  ),
           incarichi_procedura.oggetto, incarichi_procedura.cd_proc_amm,
           incarichi_procedura.cd_tipo_incarico,
           tipo_incarico.ds_tipo_incarico,
           incarichi_procedura.cd_tipo_attivita,
           tipo_attivita.ds_tipo_attivita, incarichi_procedura.tipo_natura,
           incarichi_procedura.fl_meramente_occasionale,
           incarichi_procedura.fl_art51,
           incarichi_repertorio.fl_inviato_corte_conti,
           incarichi_repertorio.importo_lordo,
           (SELECT NVL (SUM (importo_lordo), 0)
              FROM incarichi_repertorio_var
             WHERE incarichi_repertorio_var.esercizio =
                            incarichi_repertorio.esercizio
               AND incarichi_repertorio_var.pg_repertorio =
                                            incarichi_repertorio.pg_repertorio
               AND incarichi_repertorio_var.tipo_variazione IN ('I', 'T')
               AND incarichi_repertorio_var.stato = 'D') importo_variazione,
           incarichi_repertorio.dt_inizio_validita,
           incarichi_repertorio.dt_fine_validita,
           (SELECT MAX (dt_fine_validita)
              FROM incarichi_repertorio_var
             WHERE incarichi_repertorio_var.esercizio =
                      incarichi_repertorio.esercizio
               AND incarichi_repertorio_var.pg_repertorio =
                                            incarichi_repertorio.pg_repertorio
               AND incarichi_repertorio_var.tipo_variazione IN ('I', 'T')
               AND incarichi_repertorio_var.stato = 'D')
                                                  dt_fine_validita_variazione,
           incarichi_repertorio.dt_stipula,
           DECODE
              (incarichi_repertorio.nr_provv,
               NULL, NULL,
               DECODE (incarichi_repertorio.cd_provv,
                       NULL, 'nr. '
                        || incarichi_repertorio.nr_provv
                        || ' del '
                        || TO_CHAR (incarichi_repertorio.dt_provv,
                                    'dd/mm/yyyy'
                                   ),
                          incarichi_repertorio.cd_provv
                       || ' - nr. '
                       || incarichi_repertorio.nr_provv
                       || ' del '
                       || TO_CHAR (incarichi_repertorio.dt_provv,
                                   'dd/mm/yyyy')
                      )
              ) ds_provvedimento,
           incarichi_repertorio.fl_pubblica_contratto,
           incarichi_repertorio.esercizio_procedura,
           incarichi_repertorio.pg_procedura
--
-- Date: 05/03/2010
-- Version: 1.0
--
-- Vista elenco degli incarichi di collaborazione utilizzata per ricerca delle competenze
-- necessarie all''interno del CNR, utile alla estrazione in file XML per la Funzione Pubblica
--
-- History:
--
-- Date: 05/03/2010
-- Version: 1.0
-- Creazione
--
-- Body:
--
    FROM   incarichi_repertorio,
           incarichi_procedura,
           unita_organizzativa cds,
           unita_organizzativa uo,
           v_anagrafico_terzo,
           tipo_incarico,
           tipo_attivita
     WHERE incarichi_repertorio.stato IN ('PD', 'CC')
       AND incarichi_repertorio.cd_cds = cds.cd_unita_organizzativa
       AND incarichi_repertorio.cd_unita_organizzativa = uo.cd_unita_organizzativa
       AND incarichi_repertorio.cd_terzo = v_anagrafico_terzo.cd_terzo
       AND incarichi_repertorio.esercizio_procedura = incarichi_procedura.esercizio
       AND incarichi_repertorio.pg_procedura = incarichi_procedura.pg_procedura
       AND tipo_attivita.cd_tipo_attivita = incarichi_procedura.cd_tipo_attivita
       AND tipo_incarico.cd_tipo_incarico = incarichi_procedura.cd_tipo_incarico)
   ORDER BY 1, 2 ;
