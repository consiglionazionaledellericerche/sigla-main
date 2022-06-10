CREATE OR REPLACE FORCE VIEW V_PARTITE_GIRO
(TIPO_CDS,ESERCIZIO,ESERCIZIO_RES,VOCE_OBB,IMPEGNATO,LIQUIDATO_IMP,PAGATO,VOCE_ACC,ACCERTATO,LIQUIDATO_ACC,INCASSATO,DIFF_IMPEGNATO_ACCERTATO,DIFF_IMPEGNATO_PAGATO,DIFF_ACCERTATO_INCASSATO)
AS
(SELECT   tipo_cds, esercizio, esercizio_res, voce_obb,
             SUM (impegnato) impegnato, SUM (liquidato_imp) liquidato_imp,
             SUM (pagato) pagato, voce_acc, SUM (accertato) accertato,
             SUM (liquidato_acc) liquidato_acc, SUM (incassato) incassato,
             SUM (impegnato) - SUM (accertato) diff_impegnato_accertato,
             SUM (impegnato) - SUM (pagato) diff_impegnato_pagato,
             SUM (accertato) - SUM (incassato) diff_accertato_incassato
        FROM (
--impegnato SAC--
              SELECT   'SAC' tipo_cds, o.esercizio esercizio,
                       o.esercizio_originale esercizio_res,
                       --Substr(O.CD_TIPO_DOCUMENTO_CONT, 5,10) Tipo_doc,
                       o.cd_elemento_voce voce_obb,
                       SUM (os.im_scadenza) impegnato,
                       SUM (os.im_associato_doc_amm) liquidato_imp,
                       SUM (os.im_associato_doc_contabile) pagato,
                       ass.cd_voce voce_acc, 0 accertato, 0 liquidato_acc,
                       0 incassato
                  FROM obbligazione o,
                       obbligazione_scadenzario os,
                       ass_partita_giro ass
                 WHERE o.cd_cds = os.cd_cds
                   AND o.esercizio = os.esercizio
                   AND o.esercizio_originale = os.esercizio_originale
                   AND o.pg_obbligazione = os.pg_obbligazione
                   AND o.esercizio = ass.esercizio
                   AND o.ti_gestione = ass.ti_gestione_clg
                   AND o.ti_appartenenza = ass.ti_appartenenza_clg
                   AND o.cd_elemento_voce = ass.cd_voce_clg
                   AND o.cd_cds = CNRCTB020.getcdCDSSACValido(O.ESERCIZIO)
                   AND o.fl_pgiro = 'Y'
              GROUP BY 'SAC',
                       o.esercizio,
                       o.esercizio_originale,
                       --    Substr(O.CD_TIPO_DOCUMENTO_CONT, 5,10),
                       o.cd_elemento_voce,
                       ass.cd_voce
              UNION
--impegnato Istituti--
              SELECT   'Istituti' tipo_cds, o.esercizio esercizio,
                       o.esercizio_originale esercizio_res,
                       --Substr(O.CD_TIPO_DOCUMENTO_CONT, 5,10) Tipo_doc,
                       o.cd_elemento_voce voce_obb,
                       SUM (os.im_scadenza) impegnato,
                       SUM (os.im_associato_doc_amm) liquidato_imp,
                       SUM (os.im_associato_doc_contabile) pagato,
                       ass.cd_voce voce_acc, 0 accertato, 0 liquidato_acc,
                       0 incassato
                  FROM obbligazione o,
                       obbligazione_scadenzario os,
                       ass_partita_giro ass
                 WHERE o.cd_cds = os.cd_cds
                   AND o.esercizio = os.esercizio
                   AND o.esercizio_originale = os.esercizio_originale
                   AND o.pg_obbligazione = os.pg_obbligazione
                   AND o.esercizio = ass.esercizio
                   AND o.ti_gestione = ass.ti_gestione_clg
                   AND o.ti_appartenenza = ass.ti_appartenenza_clg
                   AND o.cd_elemento_voce = ass.cd_voce_clg
                   AND o.cd_cds != CNRCTB020.getcdCDSSACValido(O.ESERCIZIO)
                   AND o.cd_cds != '999'
                   AND o.fl_pgiro = 'Y'
              GROUP BY 'Istituti',
                       o.esercizio,
                       o.esercizio_originale,
                       --  Substr(O.CD_TIPO_DOCUMENTO_CONT, 5,10),
                       o.cd_elemento_voce,
                       ass.cd_voce
              UNION
--impegnato 999--
              SELECT   'Ente' tipo_cds, o.esercizio esercizio,
                       o.esercizio_originale esercizio_res,
                       --Substr(O.CD_TIPO_DOCUMENTO_CONT, 5,10) Tipo_doc,
                       o.cd_elemento_voce voce_obb,
                       SUM (os.im_scadenza) impegnato,
                       SUM (os.im_associato_doc_amm) liquidato_imp,
                       SUM (os.im_associato_doc_contabile) pagato,
                       ass.cd_voce voce_acc, 0 accertato, 0 liquidato_acc,
                       0 incassato
                  FROM obbligazione o,
                       obbligazione_scadenzario os,
                       ass_partita_giro ass
                 WHERE o.cd_cds = os.cd_cds
                   AND o.esercizio = os.esercizio
                   AND o.esercizio_originale = os.esercizio_originale
                   AND o.pg_obbligazione = os.pg_obbligazione
                   AND o.esercizio = ass.esercizio
                   AND o.ti_gestione = ass.ti_gestione_clg
                   AND o.ti_appartenenza = ass.ti_appartenenza_clg
                   AND o.cd_elemento_voce = ass.cd_voce_clg
                   AND o.cd_cds = CNRCTB020.getcdCDSSACValido(O.ESERCIZIO)
                   AND o.fl_pgiro = 'Y'
                   AND ass.ti_appartenenza = 'C'
              GROUP BY 'Ente',
                       o.esercizio,
                       o.esercizio_originale,
                       --    Substr(O.CD_TIPO_DOCUMENTO_CONT, 5,10),
                       o.cd_elemento_voce,
                       ass.cd_voce
              UNION
--Accertato SAC--
              SELECT   'SAC' tipo_cds, a.esercizio esercizio,
                       a.esercizio_originale esercizio_res,
                       --Substr(A.CD_TIPO_DOCUMENTO_CONT, 5,10) Tipo_doc,
                       ass.cd_voce_clg voce_obb, 0 impegnato, 0 liquidato_imp,
                       0 pagato, a.cd_elemento_voce voce_acc,
                       SUM (ac.im_scadenza) accertato,
                       SUM (ac.im_associato_doc_amm) liquidato_acc,
                       SUM (ac.im_associato_doc_contabile) incassato
                  FROM accertamento a,
                       accertamento_scadenzario ac,
                       ass_partita_giro ass
                 WHERE a.cd_cds = ac.cd_cds
                   AND a.esercizio = ac.esercizio
                   AND a.esercizio_originale = ac.esercizio_originale
                   AND a.pg_accertamento = ac.pg_accertamento
                   AND a.esercizio = ass.esercizio
                   AND a.ti_gestione = ass.ti_gestione
                   AND a.ti_appartenenza = ass.ti_appartenenza
                   AND a.cd_elemento_voce = ass.cd_voce
                   AND A.cd_cds = CNRCTB020.getcdCDSSACValido(A.ESERCIZIO)
                   AND a.fl_pgiro = 'Y'
              GROUP BY 'SAC',
                       a.esercizio,
                       a.esercizio_originale,
                       --Substr(A.CD_TIPO_DOCUMENTO_CONT, 5,10),
                       ass.cd_voce_clg,
                       a.cd_elemento_voce
              UNION
--Accertato Istituti--
              SELECT   'Istituti' tipo_cds, a.esercizio esercizio,
                       a.esercizio_originale esercizio_res,
                       --Substr(A.CD_TIPO_DOCUMENTO_CONT, 5,10) Tipo_doc,
                       ass.cd_voce_clg voce_obb, 0 impegnato, 0 liquidato_imp,
                       0 pagato, a.cd_elemento_voce voce_acc,
                       SUM (ac.im_scadenza) accertato,
                       SUM (ac.im_associato_doc_amm) liquidato_acc,
                       SUM (ac.im_associato_doc_contabile) incassato
                  FROM accertamento a,
                       accertamento_scadenzario ac,
                       ass_partita_giro ass
                 WHERE a.cd_cds = ac.cd_cds
                   AND a.esercizio = ac.esercizio
                   AND a.esercizio_originale = ac.esercizio_originale
                   AND a.pg_accertamento = ac.pg_accertamento
                   AND a.esercizio = ass.esercizio
                   AND a.ti_gestione = ass.ti_gestione
                   AND a.ti_appartenenza = ass.ti_appartenenza
                   AND a.cd_elemento_voce = ass.cd_voce
                   AND A.cd_cds != CNRCTB020.getcdCDSSACValido(A.ESERCIZIO)
                   AND a.cd_cds != '999'
                   AND a.fl_pgiro = 'Y'
              GROUP BY 'Istituti',
                       a.esercizio,
                       a.esercizio_originale,
                       --    Substr(A.CD_TIPO_DOCUMENTO_CONT, 5,10),
                       ass.cd_voce_clg,
                       a.cd_elemento_voce
              UNION
--Accertato 999--
              SELECT   'Ente' tipo_cds, a.esercizio esercizio,
                       a.esercizio_originale esercizio_res,
                       --Substr(A.CD_TIPO_DOCUMENTO_CONT, 5,10) Tipo_doc,
                       ass.cd_voce_clg voce_obb, 0 impegnato, 0 liquidato_imp,
                       0 pagato, a.cd_elemento_voce voce_acc,
                       SUM (ac.im_scadenza) accertato,
                       SUM (ac.im_associato_doc_amm) liquidato_acc,
                       SUM (ac.im_associato_doc_contabile) incassato
                  FROM accertamento a,
                       accertamento_scadenzario ac,
                       ass_partita_giro ass
                 WHERE a.cd_cds = ac.cd_cds
                   AND a.esercizio = ac.esercizio
                   AND a.esercizio_originale = ac.esercizio_originale
                   AND a.pg_accertamento = ac.pg_accertamento
                   AND a.esercizio = ass.esercizio
                   AND a.ti_gestione = ass.ti_gestione
                   AND a.ti_appartenenza = ass.ti_appartenenza
                   AND a.cd_elemento_voce = ass.cd_voce
                   AND a.cd_cds = '999'
                   AND a.fl_pgiro = 'Y'
                   AND ass.ti_appartenenza = 'C'
              GROUP BY 'Ente',
                       a.esercizio,
                       a.esercizio_originale,
                       --    Substr(A.CD_TIPO_DOCUMENTO_CONT, 5,10),
                       ass.cd_voce_clg,
                       a.cd_elemento_voce)
    GROUP BY tipo_cds, esercizio, esercizio_res,
                                                --Tipo_doc,
                                                voce_obb, voce_acc) 