--------------------------------------------------------
--  DDL for View V_MANDATO_REVERSALE_DIST_ANN
--------------------------------------------------------
CREATE OR REPLACE FORCE VIEW "V_MANDATO_REVERSALE_DIST_ANN" (
    "CD_TIPO_DOCUMENTO_CONT",
    "CD_CDS", "ESERCIZIO",
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
    "ESITO_OPERAZIONE",
    "STATO_VAR_SOS"
    ) AS
  Select "CD_TIPO_DOCUMENTO_CONT",
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
            "ESITO_OPERAZIONE",
            "STATO_VAR_SOS"
    From v_mandato_reversale
    where cd_tipo_documento_cont='MAN'
    and (esito_operazione is null or esito_operazione != 'NON_ACQUISITO')
    and (((stato='A' or stato_var_sos = 'VARIAZIONE_DEFINITIVA') and dt_trasmissione is not null) or
     exists(select 1 from mandato where
     mandato.cd_cds_origine= v_mandato_reversale.cd_cds_origine and
     mandato.esercizio=v_mandato_reversale.esercizio and
     mandato.pg_mandato_riemissione =  v_mandato_reversale.PG_DOCUMENTO_CONT))
union all
  -- Aggiungo anche le reversali non acquisite che possono essere ritrasmesse non legate a mandati
  Select "CD_TIPO_DOCUMENTO_CONT",
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
            "ESITO_OPERAZIONE",
            "STATO_VAR_SOS"
    From v_mandato_reversale
    where cd_tipo_documento_cont = 'REV'
    and esito_operazione = 'NON_ACQUISITO'
    and not exists (
        select 1 from ASS_MANDATO_REVERSALE amr
        where amr.ESERCIZIO_REVERSALE = v_mandato_reversale.esercizio
          and amr.PG_REVERSALE = v_mandato_reversale.pg_documento_cont
    )
    and stato='E'
    and dt_trasmissione is not null
union all
  -- Aggiungo anche i mandati non acquisiti che possono essere ritrasmessi
  Select "CD_TIPO_DOCUMENTO_CONT",
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
            "ESITO_OPERAZIONE",
            "STATO_VAR_SOS"
    From v_mandato_reversale
    where cd_tipo_documento_cont = 'MAN'
    and esito_operazione = 'NON_ACQUISITO'
    and ((stato='E' and dt_trasmissione is not null) or
     exists(select 1 from mandato where
     mandato.cd_cds_origine= v_mandato_reversale.cd_cds_origine and
     mandato.esercizio=v_mandato_reversale.esercizio and
     mandato.pg_mandato_riemissione =  v_mandato_reversale.PG_DOCUMENTO_CONT))
union all
  Select "CD_TIPO_DOCUMENTO_CONT",
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
         "ESITO_OPERAZIONE",
         "STATO_VAR_SOS"
    From v_mandato_reversale
    where cd_tipo_documento_cont='REV'
    and (esito_operazione is null or esito_operazione != 'NON_ACQUISITO')
    and (((stato='A' or stato_var_sos = 'VARIAZIONE_DEFINITIVA') and dt_trasmissione is not null) or
     exists(select 1 from reversale where
     reversale.cd_cds_origine= v_mandato_reversale.cd_cds_origine and
     reversale.esercizio=v_mandato_reversale.esercizio and
     reversale.pg_reversale_riemissione =  v_mandato_reversale.PG_DOCUMENTO_CONT));
