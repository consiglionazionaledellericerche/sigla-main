--------------------------------------------------------
--  DDL for View V_INCARICHI_ELENCO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INCARICHI_ELENCO" ("ESERCIZIO", "PG_REPERTORIO", "CD_TIPO_ATTIVITA", "CD_CDS", "DS_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "BENEF_CODICE_FISCALE", "BENEF_PARTITA_IVA", "BENEF_DENOMINAZIONE_SEDE", "RESP_DENOMINAZIONE_SEDE", "FIRM_DENOMINAZIONE_SEDE", "NOMINATIVO", "OGGETTO", "IMPORTO_LORDO", "IMPORTO_VARIAZIONE", "DT_INIZIO_VALIDITA", "DT_FINE_VALIDITA", "DT_PROROGA", "DT_FINE_VALIDITA_VARIAZIONE", "DT_STIPULA", "DS_PROVVEDIMENTO", "ESERCIZIO_PROCEDURA", "PG_PROCEDURA", "DS_TIPO_NORMA", "DS_PROC_AMM", "DT_DICHIARAZIONE", "IDPERLA", "IDPERLANEW", "CODICEAOOIPA") AS
  (SELECT incarichi_repertorio.esercizio, incarichi_repertorio.pg_repertorio,
           incarichi_procedura.cd_tipo_attivita, incarichi_repertorio.cd_cds,
           cds.ds_unita_organizzativa, uo.cd_unita_organizzativa,
           uo.ds_unita_organizzativa, benef.codice_fiscale, benef.partita_iva,
           benef.denominazione_sede, resp.denominazione_sede,
           firm.denominazione_sede, resp.denominazione_sede,
           incarichi_procedura.oggetto, incarichi_repertorio.importo_lordo,
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
           incarichi_repertorio.dt_proroga,
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
           incarichi_repertorio.esercizio_procedura,
           incarichi_repertorio.pg_procedura, perla.ds_tipo_norma,
           procamm.ds_proc_amm,
           (select max(incarichi_repertorio_rapp.dt_dichiarazione) from incarichi_repertorio_rapp 
           where 
            incarichi_repertorio.esercizio =  incarichi_repertorio_rapp.esercizio(+) and
       			incarichi_repertorio.pg_repertorio =  incarichi_repertorio_rapp.pg_repertorio(+)) dt_dichiarazione,
       	   incarichi_repertorio.idperla,
       	   incarichi_repertorio.idperlanew,
       	   cds.codiceaooipa
--
-- Date: 05/03/2010
-- Version: 1.5
--
-- Vista elenco degli incarichi di collaborazione utilizzata per ricerca delle competenze
-- necessarie all''interno del CNR, utile alla estrazione in file XML
--
-- History:
--
-- Date: 5/11/2007
-- Version: 1.0
-- Creazione
--
-- Date: 5/2/2008
-- Version: 1.1
-- Aggiunta campo DT_STIPULA
--
-- Date: 15/09/2008
-- Version: 1.2
-- Aggiornata tenedo conto della nuova struttura degli incarichi che prevedono la nuova tabella
-- INCARICHI_PROCEDURA
--
-- Date: 15/04/2009
-- Version: 1.3
-- Aggiunti i campi IMPORTO_VARIAZIONE e DT_FINE_VALIDITA_VARIAZIONE
--
-- Date: 15/02/2010
-- Version: 1.4
-- Aggiunto il campo DS_PROVVEDIMENTO
--
-- Date: 05/03/2010
-- Version: 1.5
-- Aggiunti i campi ESERCIZIO_PROCEDURA, PG_PROCEDURA
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
           tipo_norma_perla perla,
           procedure_amministrative procamm
     WHERE incarichi_repertorio.stato IN ('PD', 'CC')
       AND incarichi_repertorio.fl_pubblica_contratto = 'Y'
       and incarichi_repertorio.dt_inizio_validita is not null
       AND incarichi_repertorio.esercizio_procedura =
                                                 incarichi_procedura.esercizio
       AND incarichi_repertorio.pg_procedura =
                                              incarichi_procedura.pg_procedura
       AND incarichi_repertorio.cd_cds = cds.cd_unita_organizzativa
       AND incarichi_repertorio.cd_unita_organizzativa =
                                                     uo.cd_unita_organizzativa
       AND incarichi_repertorio.cd_terzo = benef.cd_terzo
       AND incarichi_procedura.cd_terzo_resp = resp.cd_terzo(+)
       AND incarichi_procedura.cd_firmatario = firm.cd_terzo
       AND incarichi_procedura.cd_proc_amm_benef = procamm.cd_proc_amm(+)
       AND incarichi_procedura.cd_tipo_norma_perla = perla.cd_tipo_norma(+))
   ORDER BY 1, 2 ;
