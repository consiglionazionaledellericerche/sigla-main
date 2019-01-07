--------------------------------------------------------
--  DDL for View V_MANDATO_REVERSALE_PRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_REVERSALE_PRE" (
    "CD_TIPO_DOCUMENTO_CONT",
    "CD_CDS",
    "ESERCIZIO",
    "PG_DOCUMENTO_CONT",
    "CD_UNITA_ORGANIZZATIVA",
    "CD_CDS_ORIGINE",
    "CD_UO_ORIGINE",
    "TI_DOCUMENTO_CONT",
    "DS_DOCUMENTO_CONT",
    "STATO",
    "STATO_TRASMISSIONE",
    "DT_EMISSIONE",
    "DT_TRASMISSIONE",
    "DT_RITRASMISSIONE",
    "DT_PAGAMENTO_INCASSO",
    "DT_ANNULLAMENTO",
    "IM_DOCUMENTO_CONT",
    "IM_RITENUTE",
    "IM_PAGATO_INCASSATO",
    "CD_TERZO",
    "CD_TIPO_DOCUMENTO_CONT_PADRE",
    "PG_DOCUMENTO_CONT_PADRE",
    "TI_DOCUMENTO_CONT_PADRE",
    "PG_VER_REC",
    "DT_FIRMA",
    "TIPO_DEBITO_SIOPE",
    "ESITO_OPERAZIONE"
    ) AS
  SELECT
--
--
-- Date: 07/02/2003
-- Version: 1.5
--
-- Pre-view di estrazione delle testate di mandati (non di regolarizzazione) e delle testate di reversali definitive
-- (non di regolarizzazione), relativi terzi e degli eventuali doc.contabili in associazione.
--
-- History:
--
-- Date: 24/06/2002
-- Version: 1.0
-- Creazione vista
--
-- Date: 10/07/2002
-- Version: 1.1
-- Aggiunta gestione relazioni fra mandati e mandati (ASS_MANDATO_MANDATO)
--
-- Date: 18/09/2002
-- Version: 1.2
-- Filtraggio fl_con_man_prc=Y su V_ASS_DOC_CONTABILI
--
-- Date: 10/12/2002
-- Version: 1.4
-- Ottimizzazione
--
-- Date: 07/02/2003
-- Version: 1.5
-- Fix su errore estrazione associazioni di accreditamento
--
-- Date: 10/12/2015
-- Version: 1.6
-- Aggiunta la data di firma
--
-- Body:
--
          a.cd_tipo_documento_cont,
a.cd_cds,
a.esercizio,
a.pg_mandato,
a.cd_unita_organizzativa,
a.cd_cds_origine,
a.cd_uo_origine,
a.ti_mandato,
a.ds_mandato,
a.stato,
nvl(a.stato_trasmissione_annullo,a.stato_trasmissione),
a.dt_emissione,
a.dt_trasmissione,
a.dt_ritrasmissione,
a.dt_pagamento,
a.dt_annullamento,
a.im_mandato,
a.im_ritenute,
a.im_pagato,
b.cd_terzo,
a.cd_tipo_documento_cont,
a.pg_mandato,
a.ti_mandato,
a.pg_ver_rec,
nvl(a.dt_firma_annullo,a.dt_firma),
a.tipo_debito_siope,
a.esito_operazione
FROM mandato a, mandato_terzo b
WHERE a.ti_mandato <> 'R'
AND b.cd_cds = a.cd_cds
AND b.esercizio = a.esercizio
AND b.pg_mandato = a.pg_mandato
AND NOT EXISTS
(
   SELECT
   1
   FROM ass_mandato_mandato e
   WHERE
                      a.cd_cds = e.cd_cds_coll
   AND a.esercizio = e.esercizio_coll
   AND e.CD_CDS = e.CD_CDS_COLL
   AND a.pg_mandato = e.pg_mandato_coll
)
UNION
ALL
SELECT
a.cd_tipo_documento_cont,
a.cd_cds,
a.esercizio,
a.pg_reversale,
a.cd_unita_organizzativa,
a.cd_cds_origine,
a.cd_uo_origine,
a.ti_reversale,
a.ds_reversale,
a.stato,
nvl(a.stato_trasmissione_annullo,a.stato_trasmissione),
a.dt_emissione,
a.dt_trasmissione,
a.dt_ritrasmissione,
a.dt_incasso,
a.dt_annullamento,
a.im_reversale,
0,
a.im_incassato,
b.cd_terzo,
a.cd_tipo_documento_cont,
a.pg_reversale,
a.ti_reversale,
a.pg_ver_rec,
nvl(a.dt_firma_annullo,a.dt_firma),
a.tipo_debito_siope,
a.esito_operazione
FROM reversale a, reversale_terzo b
WHERE a.ti_reversale <> 'R'
AND a.cd_tipo_documento_cont <> 'REV_PROVV'
AND a.cd_cds = b.cd_cds
AND a.esercizio = b.esercizio
AND a.pg_reversale = b.pg_reversale
AND NOT EXISTS
(
   SELECT
   1
   FROM ass_mandato_reversale e
   WHERE e.cd_cds_reversale = a.cd_cds
   AND e.esercizio_reversale = a.esercizio
   AND e.pg_reversale = a.pg_reversale
   AND e.ti_origine = 'S'
   AND e.cd_cds_mandato = e.cd_cds_reversale
)
UNION
ALL
SELECT
a.cd_tipo_documento_cont,
a.cd_cds,
a.esercizio,
a.pg_reversale,
a.cd_unita_organizzativa,
a.cd_cds_origine,
a.cd_uo_origine,
a.ti_reversale,
a.ds_reversale,
a.stato,
nvl(a.stato_trasmissione_annullo,a.stato_trasmissione),
a.dt_emissione,
a.dt_trasmissione,
a.dt_ritrasmissione,
a.dt_incasso,
a.dt_annullamento,
a.im_reversale,
0,
a.im_incassato,
b.cd_terzo,
'MAN',
f.pg_mandato,
f.ti_mandato,
a.pg_ver_rec,
nvl(a.dt_firma_annullo,a.dt_firma),
a.tipo_debito_siope,
a.esito_operazione
FROM reversale a, reversale_terzo b, ass_mandato_reversale e, mandato f
WHERE a.ti_reversale <> 'R'
AND a.cd_tipo_documento_cont <> 'REV_PROVV'
AND b.cd_cds = a.cd_cds
AND b.esercizio = a.esercizio
AND b.pg_reversale = a.pg_reversale
AND e.cd_cds_reversale = a.cd_cds
AND e.esercizio_reversale = a.esercizio
AND e.pg_reversale = a.pg_reversale
AND e.ti_origine = 'S'
AND f.cd_cds = e.cd_cds_mandato
AND f.esercizio = e.esercizio_mandato
AND e.cd_cds_mandato = e.cd_cds_reversale
AND f.pg_mandato = e.pg_mandato
UNION
ALL
SELECT
a.cd_tipo_documento_cont,
a.cd_cds,
a.esercizio,
a.pg_mandato,
a.cd_unita_organizzativa,
a.cd_cds_origine,
a.cd_uo_origine,
a.ti_mandato,
a.ds_mandato,
a.stato,
nvl(a.stato_trasmissione_annullo,a.stato_trasmissione),
a.dt_emissione,
a.dt_trasmissione,
a.dt_ritrasmissione,
a.DT_PAGAMENTO,
a.dt_annullamento,
a.im_mandato,
0,
a.IM_PAGATO,
b.cd_terzo,
'MAN',
f.pg_mandato,
f.ti_mandato,
a.pg_ver_rec,
nvl(a.dt_firma_annullo,a.dt_firma),
a.tipo_debito_siope,
a.esito_operazione
FROM mandato a, mandato_terzo b, ass_mandato_mandato e, mandato f
WHERE a.ti_mandato = 'P'
AND b.cd_cds = a.cd_cds
AND b.esercizio = a.esercizio
AND b.pg_mandato = a.pg_mandato
AND e.cd_cds_coll = a.cd_cds
AND e.esercizio_coll = a.esercizio
AND e.pg_mandato_coll = a.pg_mandato
AND f.cd_cds = e.cd_cds
AND f.esercizio = e.esercizio
AND e.CD_CDS = e.CD_CDS_COLL
AND f.pg_mandato = e.pg_mandato;
