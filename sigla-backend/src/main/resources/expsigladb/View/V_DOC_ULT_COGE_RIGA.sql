--------------------------------------------------------
--  DDL for View V_DOC_ULT_COGE_RIGA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_ULT_COGE_RIGA" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_TIPO_DOCUMENTO_CONT", "PG_DOCUMENTO_CONT", "DT_DOCUMENTO_CONT", "CD_CDS_DOC", "ESERCIZIO_DOC", "CD_TIPO_DOC", "CD_UO_DOC", "PG_NUMERO_DOC", "CD_TERZO", "STATO_PAGAMENTO_FONDO_ECO", "CD_ELEMENTO_VOCE", "STATO_COGE_DOCCONT", "IM_DOC", "IM_LORDO_DOC") AS 
  (
select
--
-- Date: 18/07/2006
-- Version: 1.10
--
-- Vista di estrazione delle righe dei documenti di pagamento/incasso a fini COGE
--
-- History:
--
-- Date: 05/03/2002
-- Version: 1.0
-- Creazione
--
-- Date: 22/05/2002
-- Version: 1.1
-- Estrazione cd_cds_doc_amm
--
-- Date: 04/07/2002
-- Version: 1.2
-- Fix errore reversali
--
-- Date: 17/10/2002
-- Version: 1.3
-- Il mandato degli stipendi ritorna il compenso CORI come documento collegato e non il generico di versamento stipendi effettivamente collegato
--
-- Date: 03/11/2002
-- Version: 1.4
-- Estrazione del terzo corretto dal documento amministrativo per chiusura della partita fornitore/cliente
-- Il teroz estratto dalla vista NON è quello del documento autorizzatorio, ma quel del doc amministrativo che quel doc aut.
-- paga o incassa. Nel caso speciale del documento generico, possono esserci più terzi sulle righe.
--
-- Date: 12/11/2002
-- Version: 1.5
-- Estrazione del netto da mandato riga (im_mandato_riga - im_ritenute_riga)
--
-- Date: 13/12/2002
-- Version: 1.6
-- Estrazione del lordo mandato riga
--
-- Date: 07/01/2003
-- Version: 1.7
-- Corretta gestione del tipo STATO_PAGAMENTO_FONDO_ECO: messo a R per doc generici di apertura reintegro e chiusura del fondo
-- Con tale gestione viene correttamente effettuata la contabilizzazione economica cassa -> banca anche per i mandati associati a tali documenti
--
-- Date: 27/05/2003
-- Version: 1.8
-- Ottimizzata l'estrazione delle info di dettaglio di documenti diversi dal doc generico
-- tramite la vista V_DOC_AMM_COGE_NOGEN_TER_FON
--
-- Date: 16/06/2003
-- Version: 1.9
-- Ottimizzazione generale con introduzione di funzioni
--
-- Date: 18/07/2006
-- Version: 1.10
-- Gestione Impegni/Accertamenti Residui:
-- aggiornati i parametri di get_coge_ult_terzo modificata per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
-- Sezione relativa ai documenti generici
 a.esercizio,
 a.cd_cds,
 b.cd_unita_organizzativa,
 b.cd_tipo_documento_cont,
 b.pg_mandato,
 b.dt_pagamento,
 a.cd_cds_doc_amm,
 a.esercizio_doc_amm,
 a.cd_tipo_documento_amm,
 a.cd_uo_doc_amm,
 a.pg_doc_amm,
 get_coge_ult_terzo(b.cd_tipo_documento_cont,a.cd_tipo_documento_amm, a.cd_cds_doc_amm,a.esercizio_doc_amm,a.cd_uo_doc_amm,a.pg_doc_amm,a.esercizio_obbligazione,a.esercizio_ori_obbligazione,a.pg_obbligazione,a.pg_obbligazione_scadenzario),
 substr(get_coge_ult_fondo(a.cd_tipo_documento_amm, a.cd_cds_doc_amm,a.esercizio_doc_amm,a.cd_uo_doc_amm,a.pg_doc_amm),1,1),
 get_coge_ult_voce(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_obbligazione,a.esercizio_ori_obbligazione,a.pg_obbligazione),
 get_coge_ult_stato_doccont(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_obbligazione,a.esercizio_ori_obbligazione,a.pg_obbligazione),
 sum(a.im_mandato_riga-a.im_ritenute_riga), -- Uso il netto presente nel mandato: attualmente diverso da im_mandato_Riga solo per compensi
 sum(a.im_mandato_riga) -- lordo
from mandato_riga a, mandato b where
     a.cd_cds = b.cd_cds
 and a.esercizio = b.esercizio
 and a.pg_mandato = b.pg_mandato
 and a.cd_tipo_documento_amm <> 'GEN_STIP_S'  -- Per il mandato degli stipendi viene estratto il compenso dei CORI
-- and b.stato = 'P'
group by
 a.esercizio,
 a.cd_cds,
 b.cd_unita_organizzativa,
 b.cd_tipo_documento_cont,
 b.pg_mandato,
 b.dt_pagamento,
 a.cd_cds_doc_amm,
 a.esercizio_doc_amm,
 a.cd_tipo_documento_amm,
 a.cd_uo_doc_amm,
 a.pg_doc_amm,
 get_coge_ult_terzo(b.cd_tipo_documento_cont,a.cd_tipo_documento_amm, a.cd_cds_doc_amm,a.esercizio_doc_amm,a.cd_uo_doc_amm,a.pg_doc_amm,a.esercizio_obbligazione,a.esercizio_ori_obbligazione,a.pg_obbligazione,a.pg_obbligazione_scadenzario),
 substr(get_coge_ult_fondo(a.cd_tipo_documento_amm, a.cd_cds_doc_amm,a.esercizio_doc_amm,a.cd_uo_doc_amm,a.pg_doc_amm),1,1),
 get_coge_ult_voce(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_obbligazione,a.esercizio_ori_obbligazione,a.pg_obbligazione),
 get_coge_ult_stato_doccont(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_obbligazione,a.esercizio_ori_obbligazione,a.pg_obbligazione)
-- decode(c.cd_tipo_documento_amm,'GEN_AP_FON','R','GEN_RE_FON','R',c.stato_pagamento_fondo_eco)
union all
select
 b.esercizio,
 b.cd_cds,
 b.cd_unita_organizzativa,
 b.cd_tipo_documento_cont,
 b.pg_mandato,
 b.dt_pagamento,
 d.cd_cds_comp,
 d.esercizio_comp,
 'COMPENSO',
 d.cd_uo_comp,
 d.pg_comp,
 e.cd_terzo,
 'N',
 get_coge_ult_voce(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_obbligazione,a.esercizio_ori_obbligazione,a.pg_obbligazione),
 'N',
 a.im_mandato_riga,
 a.im_mandato_riga
from mandato b, stipendi_cofi d, compenso e ,mandato_riga a where
     d.cd_cds_mandato = b.cd_cds
 and d.esercizio_mandato = b.esercizio
 and d.pg_mandato = b.pg_mandato
 and e.cd_cds = d.cd_cds_comp
 and e.cd_unita_organizzativa = d.cd_uo_comp
 and e.esercizio = d.esercizio_comp
 and e.pg_compenso = d.pg_comp
 and a.cd_cds = b.cd_cds
 and a.esercizio = b.esercizio
 and a.pg_mandato = b.pg_mandato
union all
-- documenti generici di entrata
select
 a.esercizio,
 a.cd_cds,
 b.cd_unita_organizzativa,
 b.cd_tipo_documento_cont,
 b.pg_reversale,
 b.dt_incasso,
 a.cd_cds_doc_amm,
 a.esercizio_doc_amm,
 a.cd_tipo_documento_amm,
 a.cd_uo_doc_amm,
 a.pg_doc_amm,
 get_coge_ult_terzo(b.cd_tipo_documento_cont,a.cd_tipo_documento_amm, a.cd_cds_doc_amm,a.esercizio_doc_amm,a.cd_uo_doc_amm,a.pg_doc_amm,a.esercizio_accertamento,a.esercizio_ori_accertamento,a.pg_accertamento,a.pg_accertamento_scadenzario),
 substr(get_coge_ult_fondo(a.cd_tipo_documento_amm, a.cd_cds_doc_amm,a.esercizio_doc_amm,a.cd_uo_doc_amm,a.pg_doc_amm),1,1),
 get_coge_ult_voce(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_accertamento,a.esercizio_ori_accertamento,a.pg_accertamento),
 get_coge_ult_stato_doccont(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_accertamento,a.esercizio_ori_accertamento,a.pg_accertamento),
 sum(a.im_reversale_riga),
 sum(a.im_reversale_riga) -- lordo
from reversale_riga a, reversale b where
     a.cd_cds = b.cd_cds
 and a.esercizio = b.esercizio
 and a.pg_reversale = b.pg_reversale
-- and b.stato = 'P'
group by
 a.esercizio,
 a.cd_cds,
 b.cd_unita_organizzativa,
 b.cd_tipo_documento_cont,
 b.pg_reversale,
 b.dt_incasso,
 a.cd_cds_doc_amm,
 a.esercizio_doc_amm,
 a.cd_tipo_documento_amm,
 a.cd_uo_doc_amm,
 a.pg_doc_amm,
 get_coge_ult_terzo(b.cd_tipo_documento_cont,a.cd_tipo_documento_amm, a.cd_cds_doc_amm,a.esercizio_doc_amm,a.cd_uo_doc_amm,a.pg_doc_amm,a.esercizio_accertamento,a.esercizio_ori_accertamento,a.pg_accertamento,a.pg_accertamento_scadenzario),
 substr(get_coge_ult_fondo(a.cd_tipo_documento_amm, a.cd_cds_doc_amm,a.esercizio_doc_amm,a.cd_uo_doc_amm,a.pg_doc_amm),1,1),
 get_coge_ult_voce(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_accertamento,a.esercizio_ori_accertamento,a.pg_accertamento),
 get_coge_ult_stato_doccont(b.cd_tipo_documento_cont, a.cd_cds_doc_amm,a.esercizio_accertamento,a.esercizio_ori_accertamento,a.pg_accertamento)
);

   COMMENT ON TABLE "V_DOC_ULT_COGE_RIGA"  IS 'Vista di estrazione della testata dei documenti di pagamento/incasso a fini COGE';
