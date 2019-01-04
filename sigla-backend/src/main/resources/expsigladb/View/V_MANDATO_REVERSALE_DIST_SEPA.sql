--------------------------------------------------------
--  DDL for View V_MANDATO_REVERSALE_DIST_SEPA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_REVERSALE_DIST_SEPA" ("CD_TIPO_DOCUMENTO_CONT", "CD_CDS", "ESERCIZIO", "PG_DOCUMENTO_CONT", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_DOCUMENTO_CONT", "DS_DOCUMENTO_CONT", "STATO", "STATO_TRASMISSIONE", "DT_EMISSIONE", "DT_TRASMISSIONE", "DT_RITRASMISSIONE", "DT_PAGAMENTO_INCASSO", "DT_ANNULLAMENTO", "IM_DOCUMENTO_CONT", "IM_RITENUTE", "IM_PAGATO_INCASSATO", "TI_CC_BI", "CD_TERZO", "CD_TIPO_DOCUMENTO_CONT_PADRE", "PG_DOCUMENTO_CONT_PADRE", "TI_DOCUMENTO_CONT_PADRE", "PG_VER_REC", "VERSAMENTO_CORI", "DT_FIRMA", "TIPO_DEBITO_SIOPE") AS
  Select "CD_TIPO_DOCUMENTO_CONT","CD_CDS","ESERCIZIO","PG_DOCUMENTO_CONT","CD_UNITA_ORGANIZZATIVA","CD_CDS_ORIGINE","CD_UO_ORIGINE","TI_DOCUMENTO_CONT","DS_DOCUMENTO_CONT","STATO","STATO_TRASMISSIONE","DT_EMISSIONE","DT_TRASMISSIONE","DT_RITRASMISSIONE","DT_PAGAMENTO_INCASSO","DT_ANNULLAMENTO","IM_DOCUMENTO_CONT","IM_RITENUTE","IM_PAGATO_INCASSATO","TI_CC_BI","CD_TERZO","CD_TIPO_DOCUMENTO_CONT_PADRE","PG_DOCUMENTO_CONT_PADRE","TI_DOCUMENTO_CONT_PADRE","PG_VER_REC","VERSAMENTO_CORI","DT_FIRMA", "TIPO_DEBITO_SIOPE"
From v_mandato_reversale
where
cd_tipo_documento_cont='MAN' and
stato!='A' and
ti_documento_cont='P' and
(cd_cds, esercizio,PG_DOCUMENTO_CONT) in
(select m.cd_cds, m.esercizio, m.pg_mandato
from mandato m,
mandato_riga mr,banca b,rif_modalita_pagamento r
where
m.cd_cds     = mr.cd_cds and
m.esercizio = mr.esercizio and
m.pg_mandato =mr.pg_mandato and
mr.CD_MODALITA_PAG = r.cd_modalita_pag and
r.FL_CANCELLATO='N' and
r.fl_flusso ='Y' and
mr.cd_terzo = b.cd_terzo and
mr.pg_banca = b.pg_banca and
b.ti_pagamento in ('B','N') and
(b.codice_iban is  null or
(b.codice_iban is not null and
not exists(select 1 from nazione n where substr(b.codice_iban,1,2)= n.cd_iso and nvl(n.fl_sepa,'N')='Y'))))
-- condizione modificata per prendere tutti i BO e BOEST no sepa
and not exists(select 1 from mandato where
 mandato.cd_cds_origine= v_mandato_reversale.cd_cds_origine and
 mandato.esercizio=v_mandato_reversale.esercizio and
 mandato.pg_mandato_riemissione =  v_mandato_reversale.PG_DOCUMENTO_CONT)
union all
Select "CD_TIPO_DOCUMENTO_CONT","CD_CDS","ESERCIZIO","PG_DOCUMENTO_CONT","CD_UNITA_ORGANIZZATIVA","CD_CDS_ORIGINE","CD_UO_ORIGINE","TI_DOCUMENTO_CONT","DS_DOCUMENTO_CONT","STATO","STATO_TRASMISSIONE","DT_EMISSIONE","DT_TRASMISSIONE","DT_RITRASMISSIONE","DT_PAGAMENTO_INCASSO","DT_ANNULLAMENTO","IM_DOCUMENTO_CONT","IM_RITENUTE","IM_PAGATO_INCASSATO","TI_CC_BI","CD_TERZO","CD_TIPO_DOCUMENTO_CONT_PADRE","PG_DOCUMENTO_CONT_PADRE","TI_DOCUMENTO_CONT_PADRE","PG_VER_REC","VERSAMENTO_CORI","DT_FIRMA", "TIPO_DEBITO_SIOPE"
From v_mandato_reversale
where
cd_tipo_documento_cont='REV' and
stato!='A' and
ti_documento_cont='I' and
(cd_cds, esercizio,PG_DOCUMENTO_CONT) in
(select rev.cd_cds, rev.esercizio, rev.pg_reversale
from reversale rev,
reversale_riga rr,banca b,rif_modalita_pagamento r
where
rev.cd_cds     = rr.cd_cds and
rev.esercizio  = rr.esercizio and
rev.pg_reversale =rr.pg_reversale and
rr.CD_MODALITA_PAG = r.cd_modalita_pag and
r.FL_CANCELLATO='N' and
r.fl_flusso ='Y' and
rr.cd_terzo = b.cd_terzo and
rr.pg_banca = b.pg_banca and
b.ti_pagamento in ('B','N') and
(b.codice_iban is  null or
(b.codice_iban is not null and
not exists(select 1 from nazione n where substr(b.codice_iban,1,2)= n.cd_iso and nvl(n.fl_sepa,'N')='Y'))))
-- condizione modificata per prendere tutti i BO e BOEST no sepa
and not exists(select 1 from reversale where
  reversale.cd_cds_origine= v_mandato_reversale.cd_cds_origine and
 reversale.esercizio=v_mandato_reversale.esercizio and
 reversale.pg_reversale_riemissione =  v_mandato_reversale.PG_DOCUMENTO_CONT);
