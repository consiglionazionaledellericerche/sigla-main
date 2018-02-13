--------------------------------------------------------
--  DDL for View V_DT_MANREV_COMP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DT_MANREV_COMP" ("CD_CDS_COMPENSO", "CD_UO_COMPENSO", "ESERCIZIO_COMPENSO", "PG_COMPENSO", "ESERCIZIO_DOC_CONT", "DT_TRASMISSIONE", "DT_PAGAMENTO") AS 
  select
--
-- Date: 23/03/2004
-- Version: 2.1
--
-- View di estrazione delle informazioni delle date significative di mandati e reversali collegati a compenso
--
-- History:
--
-- Date: 15/10/2002
-- Version: 1.0
-- Creazione vista
--
-- Date: 07/05/2003
-- Version: 2.0
-- Estratto anche l'esercizio del mandato principale o esercizio di quelli secondari
--
-- Date: 23/03/2004
-- Version: 2.1
-- Fix errore CINECA, non erano filtrati mandati e reversali annullati
--
-- Date: 19/12/2005
-- Version: 2.2
-- Corretta anomalia: aggiunta la condizione stato!= 'A' in alcune select
--
-- Body:
--
 CD_CDS,
 CD_UNITA_ORGANIZZATIVA,
 ESERCIZIO,
 PG_COMPENSO,
 ESERCIZIO_DOC_CONT,
 max(DT_TRASMISSIONE),
 max(DT_PAGAMENTO) from (
select
 a.cd_cds,
 a.cd_unita_organizzativa,
 a.esercizio,
 a.pg_compenso,
 m.esercizio esercizio_doc_cont,
 m.dt_trasmissione,
 m.dt_pagamento
from compenso a, mandato_riga mr, mandato m where
      mr.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO'
  and mr.CD_UO_DOC_AMM = a.CD_UNITA_ORGANIZZATIVA
  and mr.CD_CDS_DOC_AMM = a.CD_CDS
  and mr.ESERCIZIO_DOC_AMM = a.ESERCIZIO
  and mr.PG_DOC_AMM = a.PG_COMPENSO
  and m.cd_cds = mr.cd_cds
  and m.esercizio = mr.esercizio
  and m.pg_mandato = mr.pg_mandato
  and m.stato != 'A'
union all
select
 a.cd_cds,
 a.cd_unita_organizzativa,
 a.esercizio,
 a.pg_compenso,
 r.esercizio esercizio_doc_cont,
 r.dt_trasmissione,
 r.dt_incasso
from compenso a, ass_comp_doc_cont_nmp anmp, reversale r where
 not exists (select 1 from mandato_riga mr where
      mr.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO'
  and mr.CD_UO_DOC_AMM = a.CD_UNITA_ORGANIZZATIVA
  and mr.CD_CDS_DOC_AMM = a.CD_CDS
  and mr.ESERCIZIO_DOC_AMM = a.ESERCIZIO
  and mr.PG_DOC_AMM = a.PG_COMPENSO
  and mr.stato != 'A'
 )
 and anmp.CD_UO_COMPENSO = a.CD_UNITA_ORGANIZZATIVA
 and anmp.CD_CDS_COMPENSO = a.CD_CDS
 and anmp.ESERCIZIO_COMPENSO = a.ESERCIZIO
 and anmp.PG_COMPENSO = a.PG_COMPENSO
 and anmp.cd_tipo_doc = 'R'
 and r.cd_cds = anmp.cd_cds_doc
 and r.esercizio = anmp.esercizio_doc
 and r.pg_reversale = anmp.pg_doc
 and r.stato != 'A'
union all
select
 a.cd_cds,
 a.cd_unita_organizzativa,
 a.esercizio,
 a.pg_compenso,
 m.esercizio esercizio_doc_cont,
 m.dt_trasmissione,
 m.dt_pagamento
from compenso a, ass_comp_doc_cont_nmp anmp, mandato m where
 not exists (select 1 from mandato_riga mr where
      mr.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO'
  and mr.CD_UO_DOC_AMM = a.CD_UNITA_ORGANIZZATIVA
  and mr.CD_CDS_DOC_AMM = a.CD_CDS
  and mr.ESERCIZIO_DOC_AMM = a.ESERCIZIO
  and mr.PG_DOC_AMM = a.PG_COMPENSO
  and mr.stato != 'A'
 )
 and not exists (select 1 from ass_comp_doc_cont_nmp atnmp where
      atnmp.CD_UO_COMPENSO = a.CD_UNITA_ORGANIZZATIVA
  and atnmp.CD_CDS_COMPENSO = a.CD_CDS
  and atnmp.ESERCIZIO_COMPENSO = a.ESERCIZIO
  and atnmp.PG_COMPENSO = a.PG_COMPENSO
  and atnmp.cd_tipo_doc = 'R'
 )
 and anmp.CD_UO_COMPENSO = a.CD_UNITA_ORGANIZZATIVA
 and anmp.CD_CDS_COMPENSO = a.CD_CDS
 and anmp.ESERCIZIO_COMPENSO = a.ESERCIZIO
 and anmp.PG_COMPENSO = a.PG_COMPENSO
 and anmp.cd_tipo_doc = 'M'
 and m.cd_cds = anmp.cd_cds_doc
 and m.esercizio = anmp.esercizio_doc
 and m.pg_mandato = anmp.pg_doc
 and m.stato != 'A'
)
group by
 CD_CDS,
 CD_UNITA_ORGANIZZATIVA,
 ESERCIZIO,
 PG_COMPENSO,
 ESERCIZIO_DOC_CONT
;

   COMMENT ON TABLE "V_DT_MANREV_COMP"  IS 'View di estrazione delle informazioni delle date significative di mandati e reversali collegati a compenso';
