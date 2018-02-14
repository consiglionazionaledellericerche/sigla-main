--------------------------------------------------------
--  DDL for View PRT_STAMPA_PERSONALE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_PERSONALE" ("TIPO", "ESERCIZIO", "MESE", "UO", "CDR", "ALTRA_UO", "CD_PROGETTO", "DS_PROGETTO", "CD_DIPARTIMENTO", "DS_DIPARTIMENTO", "CD_COMMESSA", "DS_COMMESSA", "PG_PROGETTO", "CD_MODULO", "DS_MODULO", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "ID_MATRICOLA", "NOMINATIVO", "DS_PROFILO", "PERCENT_A1", "PERCENT_A2", "PERCENT_A3", "STIPENDIO_A1", "STIPENDIO_A2", "STIPENDIO_A3", "STIPENDIO", "CDS", "TI_PREV_CONS", "COSTO_MENSILE") AS 
  SELECT
--
-- Date: 28/10/2010
-- Version: 1.5
--
-- Ripartizione del personale su linee/macrolinee
--
--
-- History:
--
-- Date: 28/05/2004
-- Version: 1.0
-- Creazione
--
-- Date: 14/06/2004
-- Version: 1.1
-- Aggiunto parametro cds
--
-- Date: 01/04/2005
-- Version: 1.2
-- Aggiunti codice e descizione Commessa, Modulo di attività
--
-- Date: 09/11/2006
-- Version: 1.3
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Date: 01/07/2009
-- Version: 1.4
-- Aggiunta le colonne STIPENDIO_A2,STIPENDIO_A3,TI_PREV_CONS,COSTO_MENSILE
--
-- Date: 28/10/2010
-- Version: 1.5
-- Modificata la colonna stipendio -> stipendio_a1 e aggiunta la colonna stipendio
-- come stipendio totale di stipendio_a1
-- Body:
--
         'Somme non ripartite' tipo, costo_del_dipendente.esercizio,
            costo_del_dipendente.mese,
            costo_del_dipendente.cd_unita_organizzativa, NULL, NULL, NULL,
            NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
            costo_del_dipendente.id_matricola,
            costo_del_dipendente.nominativo, costo_del_dipendente.ds_profilo,
            0, 0, 0, 0, 0, 0, 0,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons, 0
       FROM costo_del_dipendente, unita_organizzativa u
      WHERE NOT EXISTS (
               SELECT 1
                 FROM ass_cdp_la
                WHERE ass_cdp_la.mese = costo_del_dipendente.mese
                  AND ass_cdp_la.esercizio = costo_del_dipendente.esercizio
                  AND ass_cdp_la.id_matricola =
                         costo_del_dipendente.id_matricola                                                                  /*
                                                           And COSTO_DEL_DIPENDENTE.CD_UNITA_ORGANIZZATIVA =
                                                                                   (Select CD_UNITA_ORGANIZZATIVA From  CDR
                                                                                         Where CD_CENTRO_RESPONSABILITA = ASS_CDP_LA.CD_CENTRO_RESPONSABILITA)*/)
        AND NOT EXISTS (
               SELECT 1
                 FROM ass_cdp_uo
                WHERE ass_cdp_uo.mese = costo_del_dipendente.mese
                  AND ass_cdp_uo.esercizio = costo_del_dipendente.esercizio
                  AND ass_cdp_uo.id_matricola =
                                             costo_del_dipendente.id_matricola
                  AND ass_cdp_uo.cd_unita_organizzativa =
                                   costo_del_dipendente.cd_unita_organizzativa)
        AND u.cd_unita_organizzativa =
                                   costo_del_dipendente.cd_unita_organizzativa
   GROUP BY 'Somme non ripartite',
            costo_del_dipendente.esercizio,
            costo_del_dipendente.mese,
            costo_del_dipendente.cd_unita_organizzativa,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            costo_del_dipendente.id_matricola,
            costo_del_dipendente.nominativo,
            costo_del_dipendente.ds_profilo,
            0,
            0,
            0,
            0,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons
   UNION ALL
   SELECT   'Somme non ripartite', costo_del_dipendente.esercizio,
            costo_del_dipendente.mese, ass_cdp_uo.cd_unita_organizzativa,
            NULL, costo_del_dipendente.cd_unita_organizzativa altra_uo, NULL,
            NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
            ass_cdp_uo.id_matricola, costo_del_dipendente.nominativo,
            costo_del_dipendente.ds_profilo, 0, 0, 0, 0, 0, 0, 0,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons, 0
       FROM ass_cdp_uo, costo_del_dipendente, unita_organizzativa u
      WHERE ass_cdp_uo.mese = costo_del_dipendente.mese
        AND ass_cdp_uo.esercizio = costo_del_dipendente.esercizio
        AND ass_cdp_uo.id_matricola = costo_del_dipendente.id_matricola
        AND NOT EXISTS (
               SELECT 1
                 FROM ass_cdp_la
                WHERE ass_cdp_la.mese = costo_del_dipendente.mese
                  AND ass_cdp_la.esercizio = costo_del_dipendente.esercizio
                  AND ass_cdp_la.id_matricola =
                                             costo_del_dipendente.id_matricola
                                                                              /*And COSTO_DEL_DIPENDENTE.CD_UNITA_ORGANIZZATIVA =
                                                                                 (Select CD_UNITA_ORGANIZZATIVA From  CDR
                                                                                   Where CD_CENTRO_RESPONSABILITA = ASS_CDP_LA.CD_CENTRO_RESPONSABILITA)*/
            )
        AND u.cd_unita_organizzativa =
                                   costo_del_dipendente.cd_unita_organizzativa
   GROUP BY 'Somme non ripartite',
            costo_del_dipendente.esercizio,
            costo_del_dipendente.mese,
            ass_cdp_uo.cd_unita_organizzativa,
            NULL,
            costo_del_dipendente.cd_unita_organizzativa,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            ass_cdp_uo.id_matricola,
            costo_del_dipendente.nominativo,
            costo_del_dipendente.ds_profilo,
            0,
            0,
            0,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons
   UNION ALL
  SELECT   'Somme ripartite', costo_del_dipendente.esercizio,
            costo_del_dipendente.mese,
            costo_del_dipendente.cd_unita_organizzativa,
            v_linea_attivita_valida.cd_centro_responsabilita, NULL altra_uo,
            progetto.cd_progetto, progetto.ds_progetto,
            nvl(progetto.cd_dipartimento,commessa.cd_dipartimento),
            (SELECT ds_dipartimento
               FROM dipartimento
              WHERE cd_dipartimento =
                                     nvl(progetto.cd_dipartimento,commessa.cd_dipartimento))
                                                              ds_dipartimento,
            commessa.cd_progetto, commessa.ds_progetto, modulo.pg_progetto,
            modulo.cd_progetto, modulo.ds_progetto,
            v_linea_attivita_valida.cd_linea_attivita,
            v_linea_attivita_valida.ds_linea_attivita,
            costo_del_dipendente.id_matricola,
            costo_del_dipendente.nominativo, costo_del_dipendente.ds_profilo,
            ass_cdp_la.prc_la_a1 percent_a1, ass_cdp_la.prc_la_a2 percent_a2,
            ass_cdp_la.prc_la_a3 percent_a3,
            DECODE ((  SUM (  costo_del_dipendente.im_a1
                            + costo_del_dipendente.im_oneri_cnr_a1
                            + costo_del_dipendente.im_tfr_a1
                           )
                     * ass_cdp_la.prc_la_a1
                    ),
                    0, 0,
                    (  (  SUM (  costo_del_dipendente.im_a1
                               + costo_del_dipendente.im_oneri_cnr_a1
                               + costo_del_dipendente.im_tfr_a1
                              )
                        * ass_cdp_la.prc_la_a1
                       )
                     / 100
                    )
                   ) stipendio_a1,
            DECODE
                ((  SUM (  costo_del_dipendente.im_a2
                         + costo_del_dipendente.im_oneri_cnr_a2
                         + costo_del_dipendente.im_tfr_a2
                        )
                  * ass_cdp_la.prc_la_a2
                 ),
                 0, 0,
                 (  (  SUM (  costo_del_dipendente.im_a2
                            + costo_del_dipendente.im_oneri_cnr_a2
                            + costo_del_dipendente.im_tfr_a2
                           )
                     * ass_cdp_la.prc_la_a2
                    )
                  / 100
                 )
                ) stipendio_a2,
            DECODE
                ((  SUM (  costo_del_dipendente.im_a3
                         + costo_del_dipendente.im_oneri_cnr_a3
                         + costo_del_dipendente.im_tfr_a3
                        )
                  * ass_cdp_la.prc_la_a3
                 ),
                 0, 0,
                 (  (  SUM (  costo_del_dipendente.im_a3
                            + costo_del_dipendente.im_oneri_cnr_a3
                            + costo_del_dipendente.im_tfr_a3
                           )
                     * ass_cdp_la.prc_la_a3
                    )
                  / 100
                 )
                ) stipendio_a3,
            DECODE ((  SUM (  costo_del_dipendente.im_a1
                            + costo_del_dipendente.im_oneri_cnr_a1
                            + costo_del_dipendente.im_tfr_a1
                           )
                                    ),
                    0, 0,
                    (  (  SUM (  costo_del_dipendente.im_a1
                               + costo_del_dipendente.im_oneri_cnr_a1
                               + costo_del_dipendente.im_tfr_a1
                              )
                                              )
                                        )
                   ) stipendio,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons,
            DECODE
               (ti_prev_cons,
                'P', 0,
                DECODE ((  SUM (  costo_del_dipendente.im_a1
                                + costo_del_dipendente.im_oneri_cnr_a1
                                + costo_del_dipendente.im_tfr_a1
                               )
                         * ass_cdp_la.prc_la_a1
                        ),
                        0, 0,
                        (  (  SUM (  costo_del_dipendente.im_a1
                                   + costo_del_dipendente.im_oneri_cnr_a1
                                   + costo_del_dipendente.im_tfr_a1
                                  )
                            * ass_cdp_la.prc_la_a1
                           )
                         / 100
                        )
                       )
               ) costo_mensile
       FROM v_linea_attivita_valida,
            ass_cdp_la,
            costo_del_dipendente,
            unita_organizzativa u,
            progetto_prev commessa,
            progetto_prev modulo,
            progetto_prev progetto
      WHERE ass_cdp_la.fl_dip_altra_uo = 'N'
        AND ass_cdp_la.cd_linea_attivita = v_linea_attivita_valida.cd_linea_attivita
        AND v_linea_attivita_valida.cd_centro_responsabilita =
                                           ass_cdp_la.cd_centro_responsabilita
        and v_linea_attivita_valida.esercizio = costo_del_dipendente.esercizio
        AND ass_cdp_la.mese = costo_del_dipendente.mese
        AND ass_cdp_la.esercizio = costo_del_dipendente.esercizio
        AND ass_cdp_la.id_matricola = costo_del_dipendente.id_matricola
        AND u.cd_unita_organizzativa =
                                   costo_del_dipendente.cd_unita_organizzativa
        AND v_linea_attivita_valida.pg_progetto = modulo.pg_progetto(+)
        AND (   modulo.esercizio IS NULL
             OR modulo.esercizio = costo_del_dipendente.esercizio
            )
        AND modulo.esercizio_progetto_padre = commessa.esercizio(+)
        AND modulo.pg_progetto_padre = commessa.pg_progetto(+)
        AND commessa.esercizio_progetto_padre = progetto.esercizio(+)
        AND commessa.pg_progetto_padre = progetto.pg_progetto(+)
   GROUP BY 'Somme ripartite',
            costo_del_dipendente.esercizio,
            costo_del_dipendente.mese,
            costo_del_dipendente.cd_unita_organizzativa,
            v_linea_attivita_valida.cd_centro_responsabilita,
            NULL,
            progetto.cd_progetto,
            progetto.ds_progetto,
            nvl(progetto.cd_dipartimento,commessa.cd_dipartimento),
            commessa.cd_progetto,
            commessa.ds_progetto,
            modulo.pg_progetto,
            modulo.cd_progetto,
            modulo.ds_progetto,
            v_linea_attivita_valida.cd_linea_attivita,
            v_linea_attivita_valida.ds_linea_attivita,
            costo_del_dipendente.id_matricola,
            costo_del_dipendente.nominativo,
            costo_del_dipendente.ds_profilo,
            ass_cdp_la.prc_la_a1,
            ass_cdp_la.prc_la_a2,
            ass_cdp_la.prc_la_a3,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons
   UNION ALL
   SELECT   'Somme assegnate ad altra UO', costo_del_dipendente.esercizio,
            costo_del_dipendente.mese,
            costo_del_dipendente.cd_unita_organizzativa,
            v_linea_attivita_valida.cd_centro_responsabilita,
            ass_cdp_uo.cd_unita_organizzativa altra_uo, progetto.cd_progetto,
            progetto.ds_progetto,  nvl(progetto.cd_dipartimento,commessa.cd_dipartimento),
            (SELECT ds_dipartimento
               FROM dipartimento
              WHERE cd_dipartimento =
                                      nvl(progetto.cd_dipartimento,commessa.cd_dipartimento))
                                                              ds_dipartimento,
            commessa.cd_progetto, commessa.ds_progetto, modulo.pg_progetto,
            modulo.cd_progetto, modulo.ds_progetto,
            v_linea_attivita_valida.cd_linea_attivita,
            v_linea_attivita_valida.ds_linea_attivita,
            costo_del_dipendente.id_matricola,
            costo_del_dipendente.nominativo, costo_del_dipendente.ds_profilo,
              NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
            * "ASS_CDP_LA"."PRC_LA_A1"
            / 100 percent_a1,
              NVL ("ASS_CDP_UO"."PRC_UO_A2", 100)
            * "ASS_CDP_LA"."PRC_LA_A2"
            / 100 percent_a2,
              NVL ("ASS_CDP_UO"."PRC_UO_A3", 100)
            * "ASS_CDP_LA"."PRC_LA_A3"
            / 100 percent_a3,
            DECODE ((  SUM (  costo_del_dipendente.im_a1
                            + costo_del_dipendente.im_oneri_cnr_a1
                            + costo_del_dipendente.im_tfr_a1
                           )
                     * NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
                     * "ASS_CDP_LA"."PRC_LA_A1"
                     / 100
                    ),
                    0, 0,
                    (  (  SUM (  costo_del_dipendente.im_a1
                               + costo_del_dipendente.im_oneri_cnr_a1
                               + costo_del_dipendente.im_tfr_a1
                              )
                        * NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
                        * "ASS_CDP_LA"."PRC_LA_A1"
                        / 100
                       )
                     / 100
                    )
                   ) stipendio_a1,
            DECODE
                ((  SUM (  costo_del_dipendente.im_a2
                         + costo_del_dipendente.im_oneri_cnr_a2
                         + costo_del_dipendente.im_tfr_a2
                        )
                  * NVL ("ASS_CDP_UO"."PRC_UO_A2", 100)
                  * "ASS_CDP_LA"."PRC_LA_A2"
                  / 100
                 ),
                 0, 0,
                 (  (  SUM (  costo_del_dipendente.im_a2
                            + costo_del_dipendente.im_oneri_cnr_a2
                            + costo_del_dipendente.im_tfr_a2
                           )
                     * NVL ("ASS_CDP_UO"."PRC_UO_A2", 100)
                     * "ASS_CDP_LA"."PRC_LA_A2"
                     / 100
                    )
                  / 100
                 )
                ) stipendio_a2,
            DECODE
                ((  SUM (  costo_del_dipendente.im_a3
                         + costo_del_dipendente.im_oneri_cnr_a3
                         + costo_del_dipendente.im_tfr_a3
                        )
                  * NVL ("ASS_CDP_UO"."PRC_UO_A3", 100)
                  * "ASS_CDP_LA"."PRC_LA_A3"
                  / 100
                 ),
                 0, 0,
                 (  (  SUM (  costo_del_dipendente.im_a3
                            + costo_del_dipendente.im_oneri_cnr_a3
                            + costo_del_dipendente.im_tfr_a3
                           )
                     * NVL ("ASS_CDP_UO"."PRC_UO_A3", 100)
                     * "ASS_CDP_LA"."PRC_LA_A3"
                     / 100
                    )
                  / 100
                 )
                ) stipendio_a3,
            DECODE ((  SUM (  costo_del_dipendente.im_a1
                            + costo_del_dipendente.im_oneri_cnr_a1
                            + costo_del_dipendente.im_tfr_a1
                           )
                                    ),
                    0, 0,
                    (  (  SUM (  costo_del_dipendente.im_a1
                               + costo_del_dipendente.im_oneri_cnr_a1
                               + costo_del_dipendente.im_tfr_a1
                              )
                                              )
                                        )
                   ) stipendio,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons,
            DECODE
               (ti_prev_cons,
                'P', 0,
                DECODE ((  SUM (  costo_del_dipendente.im_a1
                                + costo_del_dipendente.im_oneri_cnr_a1
                                + costo_del_dipendente.im_tfr_a1
                               )
                         * NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
                         * "ASS_CDP_LA"."PRC_LA_A1"
                         / 100
                        ),
                        0, 0,
                        (  (  SUM (  costo_del_dipendente.im_a1
                                   + costo_del_dipendente.im_oneri_cnr_a1
                                   + costo_del_dipendente.im_tfr_a1
                                  )
                            * NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
                            * "ASS_CDP_LA"."PRC_LA_A1"
                            / 100
                           )
                         / 100
                        )
                       )
               ) costo_mensile
       FROM v_linea_attivita_valida,
            ass_cdp_la,
            ass_cdp_uo,
            costo_del_dipendente,
            unita_organizzativa u,
            progetto_prev commessa,
            progetto_prev modulo,
            progetto_prev progetto
      WHERE ass_cdp_la.fl_dip_altra_uo = 'Y'
        AND ass_cdp_la.cd_linea_attivita = v_linea_attivita_valida.cd_linea_attivita
        AND ass_cdp_uo.id_matricola = ass_cdp_la.id_matricola
        AND v_linea_attivita_valida.cd_centro_responsabilita =
                                           ass_cdp_la.cd_centro_responsabilita
        AND ass_cdp_uo.mese = ass_cdp_la.mese
        AND ass_cdp_uo.esercizio = ass_cdp_la.esercizio
        AND ass_cdp_uo.cd_unita_organizzativa =
               (SELECT cd_unita_organizzativa
                  FROM cdr
                 WHERE cd_centro_responsabilita =
                                           ass_cdp_la.cd_centro_responsabilita)
        AND ass_cdp_la.mese = costo_del_dipendente.mese
        AND ass_cdp_la.esercizio = costo_del_dipendente.esercizio
        AND ass_cdp_la.id_matricola = costo_del_dipendente.id_matricola
        AND u.cd_unita_organizzativa =
                                   costo_del_dipendente.cd_unita_organizzativa
        and v_linea_attivita_valida.esercizio = costo_del_dipendente.esercizio
        AND v_linea_attivita_valida.pg_progetto = modulo.pg_progetto(+)
        AND (   modulo.esercizio IS NULL
             OR modulo.esercizio = costo_del_dipendente.esercizio
            )
        AND modulo.esercizio_progetto_padre = commessa.esercizio(+)
        AND modulo.pg_progetto_padre = commessa.pg_progetto(+)
        AND commessa.esercizio_progetto_padre = progetto.esercizio(+)
        AND commessa.pg_progetto_padre = progetto.pg_progetto(+)
   GROUP BY 'Somme assegnate ad altra UO',
            costo_del_dipendente.esercizio,
            costo_del_dipendente.mese,
            costo_del_dipendente.cd_unita_organizzativa,
            v_linea_attivita_valida.cd_centro_responsabilita,
            ass_cdp_uo.cd_unita_organizzativa,
            progetto.cd_progetto,
            progetto.ds_progetto,
             nvl(progetto.cd_dipartimento,commessa.cd_dipartimento),
            commessa.cd_progetto,
            commessa.ds_progetto,
            modulo.pg_progetto,
            modulo.cd_progetto,
            modulo.ds_progetto,
            v_linea_attivita_valida.cd_linea_attivita,
            v_linea_attivita_valida.ds_linea_attivita,
            costo_del_dipendente.id_matricola,
            costo_del_dipendente.nominativo,
            costo_del_dipendente.ds_profilo,
            NVL (ass_cdp_uo.prc_uo_a1, 100) * ass_cdp_la.prc_la_a1 / 100,
            NVL (ass_cdp_uo.prc_uo_a2, 100) * ass_cdp_la.prc_la_a2 / 100,
            NVL (ass_cdp_uo.prc_uo_a3, 100) * ass_cdp_la.prc_la_a3 / 100,
            NVL (ass_cdp_uo.prc_uo_a1, 100),
            ass_cdp_la.prc_la_a1,
            NVL (ass_cdp_uo.prc_uo_a2, 100),
            ass_cdp_la.prc_la_a2,
            NVL (ass_cdp_uo.prc_uo_a3, 100),
            ass_cdp_la.prc_la_a3,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons
   UNION ALL
   SELECT   'Somme ricevute da altra UO', costo_del_dipendente.esercizio,
            costo_del_dipendente.mese, ass_cdp_uo.cd_unita_organizzativa,
            v_linea_attivita_valida.cd_centro_responsabilita,
            costo_del_dipendente.cd_unita_organizzativa altra_uo,
            progetto.cd_progetto, progetto.ds_progetto,
             nvl(progetto.cd_dipartimento,commessa.cd_dipartimento),
            (SELECT ds_dipartimento
               FROM dipartimento
              WHERE cd_dipartimento =
                                      nvl(progetto.cd_dipartimento,commessa.cd_dipartimento))
                                                              ds_dipartimento,
            commessa.cd_progetto, commessa.ds_progetto, modulo.pg_progetto,
            modulo.cd_progetto, modulo.ds_progetto,
            v_linea_attivita_valida.cd_linea_attivita,
            v_linea_attivita_valida.ds_linea_attivita, ass_cdp_uo.id_matricola,
            costo_del_dipendente.nominativo, costo_del_dipendente.ds_profilo,
              NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
            * "ASS_CDP_LA"."PRC_LA_A1"
            / 100 percent_a1,
              NVL ("ASS_CDP_UO"."PRC_UO_A2", 100)
            * "ASS_CDP_LA"."PRC_LA_A2"
            / 100 percent_a2,
              NVL ("ASS_CDP_UO"."PRC_UO_A3", 100)
            * "ASS_CDP_LA"."PRC_LA_A3"
            / 100 percent_a3,
            DECODE ((  SUM (  costo_del_dipendente.im_a1
                            + costo_del_dipendente.im_oneri_cnr_a1
                            + costo_del_dipendente.im_tfr_a1
                           )
                     * NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
                     * "ASS_CDP_LA"."PRC_LA_A1"
                     / 100
                    ),
                    0, 0,
                    (  (  SUM (  costo_del_dipendente.im_a1
                               + costo_del_dipendente.im_oneri_cnr_a1
                               + costo_del_dipendente.im_tfr_a1
                              )
                        * NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
                        * "ASS_CDP_LA"."PRC_LA_A1"
                        / 100
                       )
                     / 100
                    )
                   ) stipendio_a1,
            DECODE
                ((  SUM (  costo_del_dipendente.im_a2
                         + costo_del_dipendente.im_oneri_cnr_a2
                         + costo_del_dipendente.im_tfr_a2
                        )
                  * NVL ("ASS_CDP_UO"."PRC_UO_A2", 100)
                  * "ASS_CDP_LA"."PRC_LA_A2"
                  / 100
                 ),
                 0, 0,
                 (  (  SUM (  costo_del_dipendente.im_a2
                            + costo_del_dipendente.im_oneri_cnr_a2
                            + costo_del_dipendente.im_tfr_a2
                           )
                     * NVL ("ASS_CDP_UO"."PRC_UO_A2", 100)
                     * "ASS_CDP_LA"."PRC_LA_A2"
                     / 100
                    )
                  / 100
                 )
                ) stipendio_a2,
            DECODE
                ((  SUM (  costo_del_dipendente.im_a3
                         + costo_del_dipendente.im_oneri_cnr_a3
                         + costo_del_dipendente.im_tfr_a3
                        )
                  * NVL ("ASS_CDP_UO"."PRC_UO_A3", 100)
                  * "ASS_CDP_LA"."PRC_LA_A3"
                  / 100
                 ),
                 0, 0,
                 (  (  SUM (  costo_del_dipendente.im_a3
                            + costo_del_dipendente.im_oneri_cnr_a3
                            + costo_del_dipendente.im_tfr_a3
                           )
                     * NVL ("ASS_CDP_UO"."PRC_UO_A3", 100)
                     * "ASS_CDP_LA"."PRC_LA_A3"
                     / 100
                    )
                  / 100
                 )
                ) stipendio_a3,
            DECODE ((  SUM (  costo_del_dipendente.im_a1
                            + costo_del_dipendente.im_oneri_cnr_a1
                            + costo_del_dipendente.im_tfr_a1
                           )
                                    ),
                    0, 0,
                    (  (  SUM (  costo_del_dipendente.im_a1
                               + costo_del_dipendente.im_oneri_cnr_a1
                               + costo_del_dipendente.im_tfr_a1
                              )
                                              )
                                        )
                   ) stipendio,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons,
            DECODE
               (ti_prev_cons,
                'P', 0,
                DECODE ((  SUM (  costo_del_dipendente.im_a1
                                + costo_del_dipendente.im_oneri_cnr_a1
                                + costo_del_dipendente.im_tfr_a1
                               )
                         * NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
                         * "ASS_CDP_LA"."PRC_LA_A1"
                         / 100
                        ),
                        0, 0,
                        (  (  SUM (  costo_del_dipendente.im_a1
                                   + costo_del_dipendente.im_oneri_cnr_a1
                                   + costo_del_dipendente.im_tfr_a1
                                  )
                            * NVL ("ASS_CDP_UO"."PRC_UO_A1", 100)
                            * "ASS_CDP_LA"."PRC_LA_A1"
                            / 100
                           )
                         / 100
                        )
                       )
               ) costo_mensile
       FROM v_linea_attivita_valida,
            ass_cdp_la,
            ass_cdp_uo,
            costo_del_dipendente,
            unita_organizzativa u,
            progetto_prev commessa,
            progetto_prev modulo,
            progetto_prev progetto
      WHERE ass_cdp_la.fl_dip_altra_uo = 'Y'
        AND ass_cdp_la.cd_linea_attivita = v_linea_attivita_valida.cd_linea_attivita
        AND v_linea_attivita_valida.cd_centro_responsabilita =
                                           ass_cdp_la.cd_centro_responsabilita
        AND ass_cdp_uo.mese = ass_cdp_la.mese
        AND ass_cdp_uo.esercizio = ass_cdp_la.esercizio
        AND ass_cdp_uo.id_matricola = ass_cdp_la.id_matricola
        AND ass_cdp_uo.cd_unita_organizzativa =
               (SELECT cd_unita_organizzativa
                  FROM cdr
                 WHERE cd_centro_responsabilita =
                                           ass_cdp_la.cd_centro_responsabilita)
        AND ass_cdp_la.mese = costo_del_dipendente.mese
        AND ass_cdp_la.esercizio = costo_del_dipendente.esercizio
        AND costo_del_dipendente.cd_unita_organizzativa !=
               (SELECT cd_unita_organizzativa
                  FROM cdr
                 WHERE cd_centro_responsabilita =
                                       v_linea_attivita_valida.cd_centro_responsabilita)
        AND ass_cdp_la.id_matricola = costo_del_dipendente.id_matricola
        AND u.cd_unita_organizzativa =
                                   ass_cdp_uo.cd_unita_organizzativa
        and v_linea_attivita_valida.esercizio = costo_del_dipendente.esercizio
        AND v_linea_attivita_valida.pg_progetto = modulo.pg_progetto(+)
        AND (   modulo.esercizio IS NULL
             OR modulo.esercizio = costo_del_dipendente.esercizio
            )
        AND modulo.esercizio_progetto_padre = commessa.esercizio(+)
        AND modulo.pg_progetto_padre = commessa.pg_progetto(+)
        AND commessa.esercizio_progetto_padre = progetto.esercizio(+)
        AND commessa.pg_progetto_padre = progetto.pg_progetto(+)
   GROUP BY 'Somme ricevute da altra UO',
            costo_del_dipendente.esercizio,
            costo_del_dipendente.mese,
            ass_cdp_uo.cd_unita_organizzativa,
            v_linea_attivita_valida.cd_centro_responsabilita,
            costo_del_dipendente.cd_unita_organizzativa,
            progetto.cd_progetto,
            progetto.ds_progetto,
            nvl(progetto.cd_dipartimento,commessa.cd_dipartimento),
            commessa.cd_progetto,
            commessa.ds_progetto,
            modulo.pg_progetto,
            modulo.cd_progetto,
            modulo.ds_progetto,
            v_linea_attivita_valida.cd_linea_attivita,
            v_linea_attivita_valida.ds_linea_attivita,
            ass_cdp_uo.id_matricola,
            costo_del_dipendente.nominativo,
            costo_del_dipendente.ds_profilo,
            NVL ("ASS_CDP_UO"."PRC_UO_A1", 100) * "ASS_CDP_LA"."PRC_LA_A1"
            / 100,
            NVL ("ASS_CDP_UO"."PRC_UO_A2", 100) * "ASS_CDP_LA"."PRC_LA_A2"
            / 100,
            NVL ("ASS_CDP_UO"."PRC_UO_A3", 100) * "ASS_CDP_LA"."PRC_LA_A3"
            / 100,
            NVL (ass_cdp_uo.prc_uo_a1, 100),
            ass_cdp_la.prc_la_a1,
            NVL (ass_cdp_uo.prc_uo_a2, 100),
            ass_cdp_la.prc_la_a2,
            NVL (ass_cdp_uo.prc_uo_a3, 100),
            ass_cdp_la.prc_la_a3,
            DECODE (u.fl_cds,
                    'N', u.cd_unita_padre,
                    u.cd_unita_organizzativa
                   ),
            ti_prev_cons;
