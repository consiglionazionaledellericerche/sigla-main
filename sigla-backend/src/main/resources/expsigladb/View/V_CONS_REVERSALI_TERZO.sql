--------------------------------------------------------
--  DDL for View V_CONS_REVERSALI_TERZO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_REVERSALI_TERZO" ("ESERCIZIO", "CDS", "CDS_ORIGINE", "DS_CDS_ORIGINE", "UO_ORIGINE", "PG_REVERSALE", "DS_REVERSALE", "TI_REVERSALE", "STATO_REVERSALE", "PG_DISTINTA", "UO_DISTINTA", "DT_EMISSIONE", "DT_TRASMISSIONE", "DT_INCASSO", "DT_ANNULLAMENTO", "ESERCIZIO_ACCERTAMENTO", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "DS_ACCERTAMENTO", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_TERZO", "DENOMINAZIONE_SEDE", "PG_BANCA", "ABI", "CAB", "NUMERO_CONTO", "INTESTAZIONE", "TI_PAGAMENTO", "DS_TI_PAGAMENTO", "IM_REVERSALE_RIGA", "STATO_COGE") AS 
  select
--
-- Date: 19/07/2006
-- Version: 1.1
--
-- Consultazione Ricerca Reversali per Terzo
--
--
-- History:
--
-- Date: 21/04/2006
-- Version: 1.0
-- Creazione
--
-- Date: 19/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
	r.esercizio,r.cd_cds,r.cd_cds_origine,cds.ds_unita_organizzativa,r.cd_uo_origine,rr.pg_reversale,rr.ds_reversale_riga,r.ti_reversale,rr.stato,
	d.pg_distinta,d.cd_unita_organizzativa,r.dt_emissione,r.dt_trasmissione,r.dt_incasso,r.dt_annullamento,
	rr.esercizio_accertamento,a.esercizio_originale,a.pg_accertamento,a.ds_accertamento,
	a.cd_elemento_voce,ev.ds_elemento_voce,rr.cd_terzo,t.denominazione_sede,b.pg_banca,b.abi,b.cab,b.numero_conto,b.intestazione,
	b.ti_pagamento,decode(b.ti_pagamento,'A','Altro','B','Bancario','P','Postale','Q','Quietanza',null) ds_ti_pagamento,
	rr.im_reversale_riga, r.stato_coge
from reversale r, reversale_riga rr, accertamento a, banca b, elemento_voce ev, terzo t, unita_organizzativa cds, distinta_cassiere_det d
where r.esercizio=rr.esercizio
and r.cd_cds=rr.cd_cds
and r.pg_reversale=rr.pg_reversale
and rr.cd_cds=a.cd_cds
and rr.esercizio_accertamento=a.esercizio
and rr.esercizio_ori_accertamento=a.esercizio_originale
and rr.pg_accertamento=a.pg_accertamento
and rr.cd_terzo_uo=b.cd_terzo(+)
and rr.pg_banca=b.pg_banca(+)
and ev.cd_elemento_voce=a.cd_elemento_voce
and ev.esercizio=a.esercizio
and ev.ti_gestione=a.ti_gestione
and ev.ti_appartenenza=a.ti_appartenenza
and rr.cd_terzo = t.cd_terzo
and cds.cd_unita_organizzativa = r.cd_cds_origine
and d.cd_cds(+)=r.cd_cds
and d.esercizio(+)=r.esercizio
and d.cd_unita_organizzativa(+)=r.cd_uo_origine
and d.pg_reversale(+)=r.pg_reversale;
