--------------------------------------------------------
--  DDL for View V_COMPENSO_CERTIFICAZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_COMPENSO_CERTIFICAZIONE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_COMPENSO", "ESERCIZIO_MANDATO", "CD_ANAG", "CD_TRATTAMENTO", "TI_CERTIFICAZIONE", "DS_CERTIFICAZIONE", "CD_TI_COMPENSO", "DS_TI_COMPENSO", "IM_LORDO_PERCIPIENTE", "IM_NO_FISCALE", "QUOTA_ESENTE", "IM_NETTO_PERCIPIENTE", "QUOTA_ESENTE_INPS") AS 
  SELECT
--
-- Date: 13/02/2004
-- Version: 1.1
--
-- Vista di estrazione compensi con trattamenti soggetti a certificazione
--
-- History:
-- Date: 29/01/2004
-- Version: 1.0
-- Creazione
--
-- Date: 13/02/2004
-- Version: 1.1
-- Inclusi soli compensi pagati, estrazione esercizio della data di emissione
-- del mandato di pagamento del compenso
--
-- Date: 20/09/2006
-- Version: 1.2
-- Aggiunta la colonna QUOTA_ESENTE_INPS per adeguamento 770/2006
--
-- Body:
--
 comp.cd_cds
,comp.cd_unita_organizzativa
,comp.esercizio
,comp.pg_compenso
,to_number(to_char(comp.dt_emissione_mandato,'YYYY'))
,ter.cd_anag
,comp.cd_trattamento
,csc.ti_certificazione
,csc.ds_certificazione
,tc.cd_ti_compenso
,tc.ds_ti_compenso
,comp.im_lordo_percipiente
,comp.im_no_fiscale
,comp.quota_esente
,comp.im_netto_percipiente
,comp.quota_esente_inps
from terzo ter
	,config_stm_certificazione csc
	,compenso comp
	,tipo_compenso tc
where comp.stato_cofi     = 'P' -- solo compensi pagati
  and comp.cd_terzo  	  = ter.cd_terzo
  and comp.cd_trattamento = csc.cd_trattamento
  and comp.esercizio 	  = csc.esercizio
  and tc.cd_trattamento	  = comp.cd_trattamento;

   COMMENT ON TABLE "V_COMPENSO_CERTIFICAZIONE"  IS 'Vista di estrazione compensi con trattamenti soggetti a certificazione';
