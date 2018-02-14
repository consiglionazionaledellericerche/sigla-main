--------------------------------------------------------
--  DDL for View V_CONS_MANDATI_TERZO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_MANDATI_TERZO" ("ESERCIZIO", "CDS", "CDS_ORIGINE", "DS_CDS_ORIGINE", "UO", "PG_MANDATO", "DS_MANDATO", "NATURA_MANDATO", "TI_MANDATO", "STATO_MANDATO", "PG_DISTINTA", "UO_DISTINTA", "DT_EMISSIONE", "DT_TRASMISSIONE", "DT_PAGAMENTO", "DT_ANNULLAMENTO", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "DS_OBBLIGAZIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_TERZO", "DENOMINAZIONE_SEDE", "PG_BANCA", "ABI", "CAB", "NUMERO_CONTO", "INTESTAZIONE", "TI_PAGAMENTO", "DS_TI_PAGAMENTO", "IM_MANDATO_RIGA", "IM_RITENUTE_RIGA", "NETTO_RIGA", "STATO_COGE") AS 
  select
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Consultazione Ricerca Mandati per Terzo
--
--
-- History:
--
-- Date: 20/04/2006
-- Version: 1.0
-- Creazione:
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
	m.esercizio,m.cd_cds,
	m.cd_cds_origine,
	cds.ds_unita_organizzativa,m.cd_uo_origine,mr.pg_mandato, Nvl(mr.ds_mandato_riga, M.DS_MANDATO),
        CNRCTB000.tipo_mandato (mr.cD_CDS, mr.ESERCIZIO, mr.PG_MANDATO, mr.ESERCIZIO_OBBLIGAZIONE,
                                mr.PG_OBBLIGAZIONE, mr.PG_OBBLIGAZIONE_SCADENZARIO, mr.CD_CDS_DOC_AMM,
                                mr.CD_UO_DOC_AMM, mr.ESERCIZIO_DOC_AMM, mr.CD_TIPO_DOCUMENTO_AMM,
                                mr.PG_DOC_AMM, mr.ESERCIZIO_ORI_OBBLIGAZIONE),
	m.ti_mandato,mr.stato,
	d.pg_distinta,
	d.cd_unita_organizzativa,
	m.dt_emissione,m.dt_trasmissione,m.dt_pagamento,m.dt_annullamento,
	mr.esercizio_obbligazione,o.esercizio_originale,o.pg_obbligazione,o.ds_obbligazione,o.cd_elemento_voce,
	ev.ds_elemento_voce,mr.cd_terzo,t.denominazione_sede,b.pg_banca,b.abi,b.cab,b.numero_conto,b.intestazione,
	b.ti_pagamento,decode(b.ti_pagamento,'A','Altro','B','Bancario','P','Postale','Q','Quietanza',null) ds_ti_pagamento,
	mr.im_mandato_riga,mr.im_ritenute_riga,(mr.im_mandato_riga-mr.im_ritenute_riga) netto_riga, m.stato_coge
from mandato m, mandato_riga mr, obbligazione o, banca b, elemento_voce ev, terzo t, unita_organizzativa cds, distinta_cassiere_det d
where m.esercizio=mr.esercizio
and m.cd_cds=mr.cd_cds
and m.pg_mandato=mr.pg_mandato
and mr.cd_cds=o.cd_cds
and mr.esercizio_obbligazione=o.esercizio
and mr.esercizio_ori_obbligazione=o.esercizio_originale
and mr.pg_obbligazione=o.pg_obbligazione
and mr.cd_terzo=b.cd_terzo(+)
and mr.pg_banca=b.pg_banca(+)
and ev.cd_elemento_voce=o.cd_elemento_voce
and ev.esercizio=o.esercizio
and ev.ti_gestione=o.ti_gestione
and ev.ti_appartenenza=o.ti_appartenenza
and mr.cd_terzo = t.cd_terzo
and cds.cd_unita_organizzativa = m.cd_cds_origine
and d.cd_cds(+)=m.cd_cds
and d.esercizio(+)=m.esercizio
and d.cd_unita_organizzativa(+)=m.cd_unita_organizzativa
and d.pg_mandato(+)=m.pg_mandato
;
