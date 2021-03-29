--------------------------------------------------------
--  DDL for View V_INCARICHI_ELENCO_FP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INCARICHI_ELENCO_FP" ("ESERCIZIO", "PG_REPERTORIO", "CD_CDS", "DS_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "BENEF_CODICE_FISCALE", "BENEF_PARTITA_IVA", "BENEF_DENOMINAZIONE_SEDE", "RESP_DENOMINAZIONE_SEDE", "FIRM_DENOMINAZIONE_SEDE", "STATO", "CD_TERZO", "NOMINATIVO", "OGGETTO", "CD_PROC_AMM", "CD_TIPO_INCARICO", "DS_TIPO_INCARICO", "CD_TIPO_ATTIVITA", "DS_TIPO_ATTIVITA", "TIPO_NATURA", "FL_MERAMENTE_OCCASIONALE", "FL_ART51", "FL_INVIATO_CORTE_CONTI", "IMPORTO_LORDO", "IMPORTO_VARIAZIONE", "DT_INIZIO_VALIDITA", "DT_FINE_VALIDITA", "DT_FINE_VALIDITA_VARIAZIONE", "DT_STIPULA", "DS_PROVVEDIMENTO", "FL_PUBBLICA_CONTRATTO", "ESERCIZIO_PROCEDURA", "PG_PROCEDURA", "DS_TIPO_NORMA", "DS_PROC_AMM", "DT_DICHIARAZIONE", "IDPERLA", "CODICEAOOIPA") AS
  (SELECT incarichi_repertorio.esercizio, incarichi_repertorio.pg_repertorio,
           incarichi_repertorio.cd_cds, cds.ds_unita_organizzativa,
           uo.cd_unita_organizzativa, uo.ds_unita_organizzativa,
           benef.codice_fiscale, benef.partita_iva,
           benef.denominazione_sede, resp.denominazione_sede,
           firm.denominazione_sede,
           incarichi_repertorio.stato, incarichi_repertorio.cd_terzo,
           DECODE (benef.ti_entita,
                   'F', NVL (   benef.cognome
                             || ' '
                             || benef.nome,
                             benef.ragione_sociale
                            ),
                   NVL (benef.ragione_sociale,
                           benef.cognome
                        || ' '
                        || benef.nome
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
           incarichi_repertorio.pg_procedura, perla.ds_tipo_norma,
           procamm.ds_proc_amm,
           (select max(incarichi_repertorio_rapp.dt_dichiarazione) from incarichi_repertorio_rapp
           where 
            incarichi_repertorio.esercizio =  incarichi_repertorio_rapp.esercizio(+) and
            incarichi_repertorio.pg_repertorio =  incarichi_repertorio_rapp.pg_repertorio(+)) dt_dichiarazione,
           incarichi_repertorio.idperla,
       	   cds.codiceAooIpa
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
           v_anagrafico_terzo benef,
           terzo resp,
           terzo firm,
           tipo_incarico,
           tipo_attivita,
           tipo_norma_perla perla,
           procedure_amministrative procamm
     WHERE incarichi_repertorio.stato IN ('PD', 'CC')
       AND incarichi_repertorio.cd_cds = cds.cd_unita_organizzativa
       AND incarichi_repertorio.cd_unita_organizzativa = uo.cd_unita_organizzativa
       AND incarichi_repertorio.cd_terzo = benef.cd_terzo
       AND incarichi_repertorio.esercizio_procedura = incarichi_procedura.esercizio
       AND incarichi_repertorio.pg_procedura = incarichi_procedura.pg_procedura
       AND incarichi_procedura.cd_terzo_resp = resp.cd_terzo(+)
       AND incarichi_procedura.cd_firmatario = firm.cd_terzo
       AND incarichi_procedura.fl_art51 = 'N'
       AND tipo_attivita.cd_tipo_attivita = incarichi_procedura.cd_tipo_attivita
       AND tipo_incarico.cd_tipo_incarico = incarichi_procedura.cd_tipo_incarico
       AND tipo_attivita.tipo_associazione = 'INC'
       AND tipo_incarico.tipo_associazione = 'INC'
       AND incarichi_procedura.cd_proc_amm_benef = procamm.cd_proc_amm(+)
       AND incarichi_procedura.cd_tipo_norma_perla = perla.cd_tipo_norma(+))
   ORDER BY 1, 2 ;
