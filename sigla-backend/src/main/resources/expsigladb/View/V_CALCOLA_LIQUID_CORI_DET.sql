--------------------------------------------------------
--  DDL for View V_CALCOLA_LIQUID_CORI_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CALCOLA_LIQUID_CORI_DET" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "ESERCIZIO_COMPENSO", "ESERCIZIO_DOC_CONT", "PG_COMPENSO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "DT_REGISTRAZIONE", "DS_COMPENSO", "TI_ANAGRAFICO", "CD_TERZO", "DS_TERZO", "DT_CANCELLAZIONE", "DT_EMISSIONE_MANDATO", "DT_TRASMISSIONE_MANDATO", "DT_PAGAMENTO_MANDATO", "CD_CONTRIBUTO_RITENUTA", "CD_CLASSIFICAZIONE_CORI", "CD_REGIONE_CORI", "PG_COMUNE_CORI", "TI_ENTE_PERCIPIENTE", "IM_CORI", "CD_GRUPPO_CR", "CD_REGIONE", "PG_COMUNE") AS 
  select
--
-- Date: 06/05/2003
-- Version: 3.0
--
-- View di estrazione dei dettagli CORI da liquidare
--
-- History:
--
-- Date: 08/06/2002
-- Version: 1.0
-- Creazione vista
--
-- Date: 13/06/2002
-- Version: 1.1
-- Gestione regione * e comune 0
--
-- Date: 17/06/2002
-- Version: 1.2
-- Estrae il CORI da liquidare solo se ha riferimenti ad accertamento o obbligazione
-- Aggiunge la descrizione del terzo (letta da anagrafica)
--
-- Date: 18/06/2002
-- Version: 1.3
-- Cambiato nome vista
--
-- Date: 23/07/2002
-- Version: 1.4
-- Estrazione del corretto indice di regione/comune con cui entrare in GRUPPO_CR_DET sull base della classificazione del CORI
--
-- Date: 02/09/2002
-- Version: 1.5
-- Estrazione dei soli CORI di compensi in stato cofi P
--
-- Date: 15/10/2002
-- Version: 1.6
-- Estrazione delle informazioni relative a data di trasmissione invio del mandato/reversale collegata direttamente dai documenti collegati al compenso
--
-- Date: 30/01/2003
-- Version: 2.0
-- Utilizzo della function getPgComuneLiquidCori per estrazione del pg comune versamento con logiche
-- di estrazione allargate -> vedi spec. function
--
-- Date: 06/05/2003
-- Version: 3.0
-- Gestione calcolo liquidazione CORI in chiusura: aggiunto esercizio_compenso in interfaccia vista
-- aggiunto anche esercizio doc contabile collegato a compenso
--
-- Body:
--
  a.CD_CDS,
  a.CD_UNITA_ORGANIZZATIVA,
  c.ESERCIZIO,
  a.ESERCIZIO,
  dtm.ESERCIZIO_DOC_CONT,
  a.PG_COMPENSO,
  a.CD_CDS_ORIGINE,
  a.CD_UO_ORIGINE,
  a.DT_REGISTRAZIONE,
  a.DS_COMPENSO,
  a.TI_ANAGRAFICO,
  a.CD_TERZO,
  f.CD_ANAG || ' - ' || decode(nvl(f.RAGIONE_SOCIALE,'-1'),'-1',f.COGNOME || ' ' || f.NOME,f.RAGIONE_SOCIALE),
  a.DT_CANCELLAZIONE,
  a.DT_EMISSIONE_MANDATO,
  dtm.DT_TRASMISSIONE,
  dtm.DT_PAGAMENTO,
  b.CD_CONTRIBUTO_RITENUTA,
  cr.cd_classificazione_cori,
  e.cd_regione,
  e.pg_comune,
  b.TI_ENTE_PERCIPIENTE,
  b.AMMONTARE,
  e.CD_GRUPPO_CR,
  e.CD_REGIONE,
  e.PG_COMUNE
from compenso a, contributo_ritenuta b, tipo_contributo_ritenuta cr, tipo_cr_base c, gruppo_cr_det e, anagrafico f , terzo g,
     v_dt_manrev_comp dtm
where
     g.cd_terzo = a.cd_terzo
 and a.stato_cofi = 'P'
 and f.cd_anag = g.cd_anag
 and b.cd_cds = a.cd_cds
 and b.esercizio = a.esercizio
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.pg_compenso =a.pg_compenso
 and cr.cd_contributo_ritenuta = b.cd_contributo_ritenuta
 and cr.dt_ini_validita = b.dt_ini_validita
 and c.cd_contributo_ritenuta = b.cd_contributo_ritenuta
 and e.esercizio = c.esercizio
 and e.cd_gruppo_cr =c.cd_gruppo_cr
 AND (CR.cd_classificazione_cori != 'IV' ) --IVA
 -- Estrazione del corretto indice di regione/comune con cui entrare in GRUPPO_CR_DET sull base della classificazione del CORI
 and e.cd_regione = getCdRegioneLiquidCori(a.fl_compenso_stipendi,b.cd_contributo_ritenuta,b.ti_ente_percipiente,cr.cd_classificazione_cori, c.esercizio, c.cd_gruppo_cr, a.cd_regione_add,a.cd_regione_irap)
 and e.pg_comune = getPgComuneLiquidCori(cr.cd_classificazione_cori, c.esercizio, c.cd_gruppo_cr, a.pg_comune_add)
 and (
     b.esercizio_accertamento is not null
  or b.esercizio_obbligazione is not null
 )
 and dtm.CD_UO_COMPENSO = a.CD_UNITA_ORGANIZZATIVA
 and dtm.CD_CDS_COMPENSO = a.CD_CDS
 and dtm.ESERCIZIO_COMPENSO = a.ESERCIZIO
 and dtm.PG_COMPENSO = a.PG_COMPENSO;

   COMMENT ON TABLE "V_CALCOLA_LIQUID_CORI_DET"  IS 'View di estrazione dei dettagli CORI da liquidare';
