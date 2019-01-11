--------------------------------------------------------
--  DDL for View V_ASS_MANDATO_FONDO_ECO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ASS_MANDATO_FONDO_ECO" (
    "CD_CDS",
    "ESERCIZIO",
    "PG_MANDATO",
    "CD_UNITA_ORGANIZZATIVA",
    "CD_CDS_ORIGINE",
    "CD_UO_ORIGINE",
    "CD_TIPO_DOCUMENTO_CONT",
    "TI_MANDATO",
    "TI_COMPETENZA_RESIDUO",
    "DS_MANDATO",
    "STATO",
    "DT_EMISSIONE",
    "DT_TRASMISSIONE",
    "DT_PAGAMENTO",
    "DT_ANNULLAMENTO",
    "IM_MANDATO",
    "IM_PAGATO",
    "UTCR",
    "DACR",
    "UTUV",
    "DUVA",
    "PG_VER_REC",
    "STATO_TRASMISSIONE",
    "DT_RITRASMISSIONE",
    "STATO_COGE",
    "IM_RITENUTE",
    "CD_TERZO",
    "CD_CODICE_FONDO",
    "TI_APERTURA_INCREMENTO",
    "DT_FIRMA",
    "PG_MANDATO_RIEMISSIONE",
    "STATO_TRASMISSIONE_ANNULLO",
    "DT_FIRMA_ANNULLO",
    "FL_RIEMISSIONE",
    "DT_PAGAMENTO_RICHIESTA",
    "ESITO_OPERAZIONE",
    "DT_ORA_ESITO_OPERAZIONE",
    "ERRORE_SIOPE_PLUS") AS
  SELECT
--==============================================================================
--
-- Date: 30/05/2002
-- Version: 1.0
--
-- Vista di estrazione dei mandati associati o associabili al fondo economale
-- sia come apertura dello stesso che come successivo aumento della dotazione.
-- Se il codice fondo è valorizzato allora il mandato è già stato associato al
-- fondo economale; ti_apertura_incremento allora vale A se presente sulla
-- testata del fondo economale (Apertura) I se presente in ASS_FONDO_ECO_MANDATO
-- (Incremento)
--
-- History:
--
-- Date: 30/05/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Body:
--
--==============================================================================
       A.cd_cds,
       A.esercizio,
       A.pg_mandato,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.cd_tipo_documento_cont,
       A.ti_mandato,
       A.ti_competenza_residuo,
       A.ds_mandato,
       A.stato,
       A.dt_emissione,
       A.dt_trasmissione,
       A.dt_pagamento,
       A.dt_annullamento,
       A.im_mandato,
       A.im_pagato,
       A.utcr,
       A.dacr,
       A.utuv,
       A.duva,
       A.pg_ver_rec,
       A.stato_trasmissione,
       A.dt_ritrasmissione,
       A.stato_coge,
       A.im_ritenute,
       T.cd_terzo,
       F.cd_codice_fondo,
       DECODE(F.cd_codice_fondo, NULL, NULL, 'A'),a.dt_firma, a.pg_mandato_riemissione,a.stato_trasmissione_annullo,a.dt_firma_annullo,a.fl_riemissione,
       a.DT_PAGAMENTO_RICHIESTA,
       a.esito_operazione,
       a.dt_ora_esito_operazione,
       a.errore_siope_plus
FROM   MANDATO A,
       MANDATO_TERZO T,
       FONDO_ECONOMALE F
WHERE  T.cd_cds = A.cd_cds AND
       T.esercizio = A.esercizio AND
       T.pg_mandato = A.pg_mandato AND
       F.cd_cds (+) = A.cd_cds AND
       F.esercizio (+) = A.esercizio AND
       F.pg_mandato (+) = A.pg_mandato AND
       EXISTS
           (SELECT 1
            FROM   MANDATO_RIGA B
            WHERE  B.cd_cds = A.cd_cds AND
                   B.esercizio = A.esercizio AND
                   B.pg_mandato = A.pg_mandato AND
                   B.cd_tipo_documento_amm = 'GEN_AP_FON') AND
       NOT EXISTS
           (SELECT 1
            FROM   ASS_FONDO_ECO_MANDATO M
            WHERE  M.cd_cds_mandato = A.cd_cds AND
                   M.esercizio_mandato = A.esercizio AND
                   M.pg_mandato = A.pg_mandato)
UNION ALL
SELECT A.cd_cds,
       A.esercizio,
       A.pg_mandato,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.cd_tipo_documento_cont,
       A.ti_mandato,
       A.ti_competenza_residuo,
       A.ds_mandato,
       A.stato,
       A.dt_emissione,
       A.dt_trasmissione,
       A.dt_pagamento,
       A.dt_annullamento,
       A.im_mandato,
       A.im_pagato,
       A.utcr,
       A.dacr,
       A.utuv,
       A.duva,
       A.pg_ver_rec,
       A.stato_trasmissione,
       A.dt_ritrasmissione,
       A.stato_coge,
       A.im_ritenute,
       T.cd_terzo,
       M.cd_codice_fondo,
       'I',a.dt_firma,a.pg_mandato_riemissione,a.stato_trasmissione_annullo,a.dt_firma_annullo,a.fl_riemissione,
       a.DT_PAGAMENTO_RICHIESTA,
       a.esito_operazione,
       a.dt_ora_esito_operazione,
       a.errore_siope_plus
FROM   MANDATO A,
       MANDATO_TERZO T,
       ASS_FONDO_ECO_MANDATO M
WHERE  T.cd_cds = A.cd_cds AND
       T.esercizio = A.esercizio AND
       T.pg_mandato = A.pg_mandato AND
       M.cd_cds_mandato = A.cd_cds AND
       M.esercizio_mandato = A.esercizio AND
       M.pg_mandato = A.pg_mandato AND
       EXISTS
           (SELECT 1
            FROM   MANDATO_RIGA B
            WHERE  B.cd_cds = A.cd_cds AND
                   B.esercizio = A.esercizio AND
                   B.pg_mandato = A.pg_mandato AND
                   B.cd_tipo_documento_amm = 'GEN_AP_FON');

   COMMENT ON TABLE "V_ASS_MANDATO_FONDO_ECO"  IS 'Vista di estrazione dei mandati associati o associabili al fondo economale
sia come apertura dello stesso che come successivo aumento della dotazione.
Se il codice fondo è valorizzato allora il mandato è già stato associato al
fondo economale; ti_apertura_incremento allora vale A se presente sulla
testata del fondo economale (Apertura) I se presente in ASS_FONDO_ECO_MANDATO
(Incremento)';
