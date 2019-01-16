--------------------------------------------------------
--  DDL for View V_MANDATO_REVERSALE_VER_CORI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_REVERSALE_VER_CORI" (
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
    "TI_CC_BI",
    "CD_TERZO",
    "CD_TIPO_DOCUMENTO_CONT_PADRE",
    "PG_DOCUMENTO_CONT_PADRE",
    "TI_DOCUMENTO_CONT_PADRE",
    "PG_VER_REC",
    "VERSAMENTO_CORI",
    "DT_FIRMA",
    "TIPO_DEBITO_SIOPE",
    "ESITO_OPERAZIONE") AS
  SELECT
A.cd_tipo_documento_cont,
A.cd_cds,
A.esercizio,
A.pg_documento_cont,
A.cd_unita_organizzativa,
A.cd_cds_origine,
A.cd_uo_origine,
A.ti_documento_cont,
A.ds_documento_cont,
A.stato,
A.stato_trasmissione,
A.dt_emissione,
A.dt_trasmissione,
A.dt_ritrasmissione,
A.dt_pagamento_incasso,
A.dt_annullamento,
A.im_documento_cont,
A.im_ritenute,
A.im_pagato_incassato,
'C',
A.cd_terzo,
A.cd_tipo_documento_cont_padre,
A.pg_documento_cont_padre,
A.ti_documento_cont_padre,
A.pg_ver_rec,
'S',
A.DT_FIRMA,
a.tipo_debito_siope,
a.esito_operazione
FROM  V_MANDATO_REVERSALE A
Where Exists
(Select 1
From mandato_riga b
Where b.cd_tipo_documento_amm In ('GEN_CORV_S', 'TRASF_S')
And a.cd_cds=b.cd_cds
and a.esercizio=b.esercizio
And a.pg_documento_cont=b.pg_mandato)
And a.cd_tipo_documento_cont = 'MAN'
And a.cd_terzo = CNRUTIL.getTerzoVersCori(a.esercizio)
;
