--------------------------------------------------------
--  DDL for View PRT_ELENCO_CONTRATTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_ELENCO_CONTRATTI" ("CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "STATO", "PG_CONTRATTO", "ESERCIZIO_PADRE", "STATO_PADRE", "PG_CONTRATTO_PADRE", "NATURA_CONTABILE", "CD_PROTOCOLLO_GENERALE", "CD_TERZO_FIRMATARIO", "CD_TERZO_RESP", "FIG_GIUR_EST", "OGGETTO", "CD_TIPO_CONTRATTO", "DT_STIPULA", "DT_INIZIO_VALIDITA", "DT_FINE_VALIDITA", "DT_PROROGA", "IM_CONTRATTO_ATTIVO", "IM_CONTRATTO_PASSIVO", "IM_SPESA_ASS", "IM_ENTRATA_ASS", "DS_UNITA_ORGANIZZATIVA", "DS_TERZO_FIRMATARIO", "DS_TERZO_RESP", "DS_FIG_GIUR_EST", "DS_TIPO_CONTRATTO") AS 
  select /*+ RULE */
--
-- Date: 06/10/2005
-- Version: 1.0
--
-- View sottostante l'Elenco dei contratti
--
-- History:
--
-- Date: 06/10/2005
-- Version: 1.0
-- Author: Aurelio D'Amico
--
-- Creazione
--
-- Body
--
ct.CD_UNITA_ORGANIZZATIVA,
ct.ESERCIZIO,
ct.STATO,
ct.PG_CONTRATTO,
ct.ESERCIZIO_PADRE,
ct.STATO_PADRE,
ct.PG_CONTRATTO_PADRE,
ct.NATURA_CONTABILE,
ct.CD_PROTOCOLLO_GENERALE,
ct.CD_TERZO_FIRMATARIO,
ct.CD_TERZO_RESP,
ct.FIG_GIUR_EST,
ct.OGGETTO,
ct.CD_TIPO_CONTRATTO,
ct.DT_STIPULA,
ct.DT_INIZIO_VALIDITA,
ct.DT_FINE_VALIDITA,
ct.DT_PROROGA,
ct.IM_CONTRATTO_ATTIVO,
ct.IM_CONTRATTO_PASSIVO,
(select sum(im_obbligazione) from obbligazione
	where esercizio_contratto = ct.esercizio
	and stato_contratto = ct.stato
	and pg_contratto = ct.pg_contratto
	and (esercizio = esercizio_originale or obbligazione.cd_tipo_documento_cont ='OBB_RESIM')
	group by esercizio_contratto, stato_contratto, pg_contratto)as IM_SPESA_ASS,
(select sum(im_accertamento) from accertamento
	where esercizio_contratto = ct.esercizio
	and stato_contratto = ct.stato
	and pg_contratto = ct.pg_contratto
	and esercizio = esercizio_originale
	group by esercizio_contratto, stato_contratto, pg_contratto) as IM_ENTRATA_ASS,
uo.DS_UNITA_ORGANIZZATIVA,
t1.DENOMINAZIONE_SEDE as DS_TERZO_FIRMATARIO,
t2.DENOMINAZIONE_SEDE as DS_TERZO_RESP,
t3.DENOMINAZIONE_SEDE as DS_FIG_GIUR_EST,
tc.DS_TIPO_CONTRATTO
from
CONTRATTO ct,
TIPO_CONTRATTO tc,
UNITA_ORGANIZZATIVA uo,
TERZO t1,
TERZO t2,
TERZO t3
where ct.CD_TIPO_CONTRATTO = tc.CD_TIPO_CONTRATTO(+)
  and ct.CD_UNITA_ORGANIZZATIVA = uo.CD_UNITA_ORGANIZZATIVA(+)
  and ct.CD_TERZO_RESP = t2.CD_TERZO(+)
  and ct.FIG_GIUR_EST = t3.CD_TERZO(+)
  and ct.CD_TERZO_FIRMATARIO = t1.CD_TERZO(+);
