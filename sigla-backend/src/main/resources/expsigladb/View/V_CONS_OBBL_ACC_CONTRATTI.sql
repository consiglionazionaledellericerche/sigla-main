--------------------------------------------------------
--  DDL for View V_CONS_OBBL_ACC_CONTRATTI
--------------------------------------------------------

CREATE OR REPLACE FORCE VIEW v_cons_obbl_acc_contratti (tipo,
                                                        esercizio_competenza,
                                                        cd_unita_organizzativa,
                                                        esercizio_originale,
                                                        pg_doc,
                                                        dt_registrazione,
                                                        ds_doc,
                                                        cd_terzo,
                                                        cd_elemento_voce,
                                                        cd_commessa,
                                                        ds_commessa,
                                                        esercizio_contratto_padre,
                                                        stato_contratto_padre,
                                                        pg_contratto_padre,
                                                        esercizio_contratto,
                                                        stato_contratto,
                                                        pg_contratto,
                                                        oggetto_contratto,
                                                        im_accertamento,
                                                        im_obbligazione
                                                       )
AS
   SELECT
--
-- Date: 09/11/2006
-- Version: 1.2
--
-- Vista per la consultazione dei Documenti Contabili associati a un contratto
--
-- History:
--
-- Date: 19/04/2005
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 09/11/2006
-- Version: 1.2
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
            'ETR' tipo, accertamento.esercizio_competenza,
            accertamento.cd_unita_organizzativa,
            accertamento.esercizio_originale, accertamento.pg_accertamento,
            accertamento.dt_registrazione, accertamento.ds_accertamento,
            accertamento.cd_terzo, accertamento.cd_elemento_voce,
            commessa.cd_progetto, commessa.ds_progetto,
            contratto_padre.esercizio, contratto_padre.stato,
            contratto_padre.pg_contratto, contratto.esercizio,
            contratto.stato, contratto.pg_contratto, contratto.oggetto,
            SUM (accertamento_scad_voce.im_voce) totale_entrate,
            0 totale_spese
       FROM progetto_gest commessa,
            progetto_gest modulo,
            contratto,
            contratto contratto_padre,
            accertamento,
            accertamento_scadenzario,
            accertamento_scad_voce,
            v_linea_attivita_valida
      WHERE contratto.esercizio = accertamento.esercizio_contratto
        AND contratto.stato = accertamento.stato_contratto
        AND contratto.pg_contratto = accertamento.pg_contratto
        AND accertamento.cd_cds = accertamento_scadenzario.cd_cds
        AND accertamento.esercizio = accertamento_scadenzario.esercizio
        AND accertamento.esercizio_originale =
                                  accertamento_scadenzario.esercizio_originale
        AND accertamento.pg_accertamento =
                                      accertamento_scadenzario.pg_accertamento
        AND (   accertamento.esercizio_originale = accertamento.esercizio
             OR (    accertamento.cd_tipo_documento_cont = 'ACR_RES'
                 AND NOT EXISTS (
                        SELECT 1
                          FROM accertamento acc
                         WHERE accertamento.cd_cds = acc.cd_cds
                           AND accertamento.esercizio > acc.esercizio
                           AND accertamento.esercizio_originale =
                                                       acc.esercizio_originale
                           AND accertamento.pg_accertamento =
                                                           acc.pg_accertamento
                           AND accertamento.esercizio_contratto =
                                                       acc.esercizio_contratto
                           AND accertamento.stato_contratto =
                                                           acc.stato_contratto
                           AND accertamento.pg_contratto = acc.pg_contratto)
                )
            )
        AND accertamento_scadenzario.cd_cds = accertamento_scad_voce.cd_cds
        AND accertamento_scadenzario.esercizio =
                                              accertamento_scad_voce.esercizio
        AND accertamento_scadenzario.esercizio_originale =
                                    accertamento_scad_voce.esercizio_originale
        AND accertamento_scadenzario.pg_accertamento =
                                        accertamento_scad_voce.pg_accertamento
        AND accertamento_scadenzario.pg_accertamento_scadenzario =
                            accertamento_scad_voce.pg_accertamento_scadenzario
        AND accertamento_scad_voce.cd_centro_responsabilita =
                              v_linea_attivita_valida.cd_centro_responsabilita
        AND accertamento_scad_voce.cd_linea_attivita =
                                     v_linea_attivita_valida.cd_linea_attivita
        AND v_linea_attivita_valida.esercizio = accertamento.esercizio
        AND v_linea_attivita_valida.pg_progetto = modulo.pg_progetto(+)
        AND (   modulo.esercizio IS NULL
             OR modulo.esercizio = accertamento.esercizio
            )
        AND modulo.esercizio_progetto_padre = commessa.esercizio(+)
        AND modulo.pg_progetto_padre = commessa.pg_progetto(+)
        AND contratto.esercizio_padre = contratto_padre.esercizio(+)
        AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
   GROUP BY 'ETR',
            accertamento.esercizio_competenza,
            accertamento.cd_unita_organizzativa,
            accertamento.esercizio_originale,
            accertamento.pg_accertamento,
            accertamento.dt_registrazione,
            accertamento.ds_accertamento,
            accertamento.cd_terzo,
            accertamento.cd_elemento_voce,
            commessa.cd_progetto,
            commessa.ds_progetto,
            contratto_padre.esercizio,
            contratto_padre.stato,
            contratto_padre.pg_contratto,
            contratto.esercizio,
            contratto.stato,
            contratto.pg_contratto,
            contratto.oggetto
   UNION ALL
   SELECT   'ETR' tipo, accertamento.esercizio_competenza,
            accertamento.cd_unita_organizzativa,
            accertamento.esercizio_originale, accertamento.pg_accertamento,
            accertamento.dt_registrazione, accertamento.ds_accertamento,
            accertamento.cd_terzo, accertamento.cd_elemento_voce,
            commessa.cd_progetto, commessa.ds_progetto,
            contratto_padre.esercizio, contratto_padre.stato,
            contratto_padre.pg_contratto, contratto.esercizio,
            contratto.stato, contratto.pg_contratto, contratto.oggetto,
            SUM (accertamento_mod_voce.im_modifica) totale_entrate,
            0 totale_spese
       FROM progetto_gest commessa,
            progetto_gest modulo,
            contratto,
            contratto contratto_padre,
            accertamento,
            accertamento_modifica,
            accertamento_mod_voce,
            v_linea_attivita_valida
      WHERE contratto.esercizio = accertamento.esercizio_contratto
        AND contratto.stato = accertamento.stato_contratto
        AND contratto.pg_contratto = accertamento.pg_contratto
        AND accertamento.cd_cds = accertamento_modifica.cd_cds
        AND accertamento.esercizio = accertamento_modifica.esercizio
        AND accertamento.esercizio_originale =
                                     accertamento_modifica.esercizio_originale
        AND accertamento.pg_accertamento =
                                         accertamento_modifica.pg_accertamento
        AND accertamento_modifica.cd_cds = accertamento_mod_voce.cd_cds
        AND accertamento_modifica.esercizio = accertamento_mod_voce.esercizio
        AND accertamento_modifica.pg_modifica =
                                             accertamento_mod_voce.pg_modifica
        AND accertamento_mod_voce.cd_centro_responsabilita =
                              v_linea_attivita_valida.cd_centro_responsabilita
        AND accertamento_mod_voce.cd_linea_attivita =
                                     v_linea_attivita_valida.cd_linea_attivita
        AND EXISTS (
               SELECT 1
                 FROM accertamento acc
                WHERE accertamento.cd_cds = acc.cd_cds
                  --And ACCERTAMENTO.ESERCIZIO = acc.ESERCIZIO
                  AND accertamento.esercizio_originale =
                                                       acc.esercizio_originale
                  AND accertamento.pg_accertamento = acc.pg_accertamento
                  AND accertamento.esercizio_contratto =
                                                       acc.esercizio_contratto
                  AND accertamento.stato_contratto = acc.stato_contratto
                  AND accertamento.pg_contratto = acc.pg_contratto
                  AND acc.esercizio = acc.esercizio_originale)
        AND v_linea_attivita_valida.pg_progetto = modulo.pg_progetto(+)
        AND v_linea_attivita_valida.esercizio = accertamento.esercizio
        AND (   modulo.esercizio IS NULL
             OR modulo.esercizio = accertamento.esercizio
            )
        AND modulo.esercizio_progetto_padre = commessa.esercizio(+)
        AND modulo.pg_progetto_padre = commessa.pg_progetto(+)
        AND contratto.esercizio_padre = contratto_padre.esercizio(+)
        AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
   GROUP BY 'ETR',
            accertamento.esercizio_competenza,
            accertamento.cd_unita_organizzativa,
            accertamento.esercizio_originale,
            accertamento.pg_accertamento,
            accertamento.dt_registrazione,
            accertamento.ds_accertamento,
            accertamento.cd_terzo,
            accertamento.cd_elemento_voce,
            commessa.cd_progetto,
            commessa.ds_progetto,
            contratto_padre.esercizio,
            contratto_padre.stato,
            contratto_padre.pg_contratto,
            contratto.esercizio,
            contratto.stato,
            contratto.pg_contratto,
            contratto.oggetto
   UNION ALL
   SELECT   'SPE' tipo, obbligazione.esercizio_competenza,
            obbligazione.cd_unita_organizzativa,
            obbligazione.esercizio_originale, obbligazione.pg_obbligazione,
            obbligazione.dt_registrazione, obbligazione.ds_obbligazione,
            obbligazione.cd_terzo, obbligazione.cd_elemento_voce,
            commessa.cd_progetto, commessa.ds_progetto,
            contratto_padre.esercizio, contratto_padre.stato,
            contratto_padre.pg_contratto, contratto.esercizio,
            contratto.stato, contratto.pg_contratto, contratto.oggetto,
            0 totale_entrate,
            SUM (obbligazione_scad_voce.im_voce) totale_spese
       FROM progetto_gest commessa,
            progetto_gest modulo,
            contratto,
            contratto contratto_padre,
            obbligazione,
            obbligazione_scadenzario,
            obbligazione_scad_voce,
            v_linea_attivita_valida
      WHERE contratto.esercizio = obbligazione.esercizio_contratto
        AND contratto.stato = obbligazione.stato_contratto
        AND contratto.pg_contratto = obbligazione.pg_contratto
        AND obbligazione.cd_cds = obbligazione_scadenzario.cd_cds
        AND obbligazione.esercizio = obbligazione_scadenzario.esercizio
        AND obbligazione.esercizio_originale =
                                  obbligazione_scadenzario.esercizio_originale
        AND obbligazione.pg_obbligazione =
                                      obbligazione_scadenzario.pg_obbligazione
        AND (   obbligazione.esercizio_originale = obbligazione.esercizio
             OR obbligazione.cd_tipo_documento_cont = 'OBB_RESIM'
             OR (    obbligazione.cd_tipo_documento_cont = 'OBB_RES'
                 AND NOT EXISTS (
                        SELECT 1
                          FROM obbligazione obb
                         WHERE obbligazione.cd_cds = obb.cd_cds
                           AND obbligazione.esercizio > obb.esercizio
                           AND obbligazione.esercizio_originale =
                                                       obb.esercizio_originale
                           AND obbligazione.pg_obbligazione =
                                                           obb.pg_obbligazione
                           AND obbligazione.esercizio_contratto =
                                                       obb.esercizio_contratto
                           AND obbligazione.stato_contratto =
                                                           obb.stato_contratto
                           AND obbligazione.pg_contratto = obb.pg_contratto)
                )
            )
        AND obbligazione_scadenzario.cd_cds = obbligazione_scad_voce.cd_cds
        AND obbligazione_scadenzario.esercizio =
                                              obbligazione_scad_voce.esercizio
        AND obbligazione_scadenzario.esercizio_originale =
                                    obbligazione_scad_voce.esercizio_originale
        AND obbligazione_scadenzario.pg_obbligazione =
                                        obbligazione_scad_voce.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                            obbligazione_scad_voce.pg_obbligazione_scadenzario
        AND obbligazione_scad_voce.cd_centro_responsabilita =
                              v_linea_attivita_valida.cd_centro_responsabilita
        AND obbligazione_scad_voce.cd_linea_attivita =
                                     v_linea_attivita_valida.cd_linea_attivita
        AND v_linea_attivita_valida.pg_progetto = modulo.pg_progetto(+)
        AND v_linea_attivita_valida.esercizio = obbligazione.esercizio
        AND (   modulo.esercizio IS NULL
             OR modulo.esercizio = obbligazione.esercizio
            )
        AND modulo.esercizio_progetto_padre = commessa.esercizio(+)
        AND modulo.pg_progetto_padre = commessa.pg_progetto(+)
        AND contratto.esercizio_padre = contratto_padre.esercizio(+)
        AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
   GROUP BY 'SPE',
            obbligazione.esercizio_competenza,
            obbligazione.cd_unita_organizzativa,
            obbligazione.esercizio_originale,
            obbligazione.pg_obbligazione,
            obbligazione.dt_registrazione,
            obbligazione.ds_obbligazione,
            obbligazione.cd_terzo,
            obbligazione.cd_elemento_voce,
            commessa.cd_progetto,
            commessa.ds_progetto,
            contratto_padre.esercizio,
            contratto_padre.stato,
            contratto_padre.pg_contratto,
            contratto.esercizio,
            contratto.stato,
            contratto.pg_contratto,
            contratto.oggetto
   UNION ALL
   SELECT   'SPE' tipo, obbligazione.esercizio_competenza,
            obbligazione.cd_unita_organizzativa,
            obbligazione.esercizio_originale, obbligazione.pg_obbligazione,
            obbligazione.dt_registrazione, obbligazione.ds_obbligazione,
            obbligazione.cd_terzo, obbligazione.cd_elemento_voce,
            commessa.cd_progetto, commessa.ds_progetto,
            contratto_padre.esercizio, contratto_padre.stato,
            contratto_padre.pg_contratto, contratto.esercizio,
            contratto.stato, contratto.pg_contratto, contratto.oggetto,
            0 totale_entrate,
            SUM (obbligazione_mod_voce.im_modifica) totale_spese
       FROM progetto_gest commessa,
            progetto_gest modulo,
            contratto,
            contratto contratto_padre,
            obbligazione,
            obbligazione_modifica,
            obbligazione_mod_voce,
            v_linea_attivita_valida
      WHERE contratto.esercizio = obbligazione.esercizio_contratto
        AND contratto.stato = obbligazione.stato_contratto
        AND contratto.pg_contratto = obbligazione.pg_contratto
        AND obbligazione.cd_cds = obbligazione_modifica.cd_cds
        AND obbligazione.esercizio = obbligazione_modifica.esercizio
        AND obbligazione.esercizio_originale =
                                     obbligazione_modifica.esercizio_originale
        AND obbligazione.pg_obbligazione =
                                         obbligazione_modifica.pg_obbligazione
        AND obbligazione_modifica.cd_cds = obbligazione_mod_voce.cd_cds
        AND obbligazione_modifica.esercizio = obbligazione_mod_voce.esercizio
        AND obbligazione_modifica.pg_modifica =
                                             obbligazione_mod_voce.pg_modifica
        -- per evitare che prende le variazioni su impegni già diminuiti nell'importo sull'obbligazione collegato al contratto
        AND EXISTS (
               SELECT 1
                 FROM obbligazione obb
                WHERE obbligazione.cd_cds = obb.cd_cds
                  --And OBBLIGAZIONE.ESERCIZIO = obb.ESERCIZIO
                  AND obbligazione.esercizio_originale =
                                                       obb.esercizio_originale
                  AND obbligazione.pg_obbligazione = obb.pg_obbligazione
                  AND obbligazione.esercizio_contratto =
                                                       obb.esercizio_contratto
                  AND obbligazione.stato_contratto = obb.stato_contratto
                  AND obbligazione.pg_contratto = obb.pg_contratto
                  AND (   obb.esercizio = obb.esercizio_originale
                       OR obb.cd_tipo_documento_cont = 'OBB_RESIM'
                      ))
        AND obbligazione_mod_voce.cd_centro_responsabilita =
                              v_linea_attivita_valida.cd_centro_responsabilita
        AND obbligazione_mod_voce.cd_linea_attivita =
                                     v_linea_attivita_valida.cd_linea_attivita
        AND v_linea_attivita_valida.pg_progetto = modulo.pg_progetto(+)
        AND v_linea_attivita_valida.esercizio = obbligazione.esercizio
        AND (   modulo.esercizio IS NULL
             OR modulo.esercizio = obbligazione.esercizio
            )
        AND modulo.esercizio_progetto_padre = commessa.esercizio(+)
        AND modulo.pg_progetto_padre = commessa.pg_progetto(+)
        AND contratto.esercizio_padre = contratto_padre.esercizio(+)
        AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
   GROUP BY 'SPE',
            obbligazione.esercizio_competenza,
            obbligazione.cd_unita_organizzativa,
            obbligazione.esercizio_originale,
            obbligazione.pg_obbligazione,
            obbligazione.dt_registrazione,
            obbligazione.ds_obbligazione,
            obbligazione.cd_terzo,
            obbligazione.cd_elemento_voce,
            commessa.cd_progetto,
            commessa.ds_progetto,
            contratto_padre.esercizio,
            contratto_padre.stato,
            contratto_padre.pg_contratto,
            contratto.esercizio,
            contratto.stato,
            contratto.pg_contratto,
            contratto.oggetto;
