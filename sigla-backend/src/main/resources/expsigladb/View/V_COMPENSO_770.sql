--------------------------------------------------------
--  DDL for View V_COMPENSO_770
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_COMPENSO_770" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_COMPENSO", "ESERCIZIO_MANDATO", "CD_ANAG", "CD_TRATTAMENTO", "CD_QUADRO", "DS_QUADRO", "TI_MODELLO", "TI_RITENUTA", "CD_TI_COMPENSO", "DS_TI_COMPENSO", "IM_LORDO_PERCIPIENTE", "IM_NO_FISCALE", "QUOTA_ESENTE", "IM_NETTO_PERCIPIENTE", "QUOTA_ESENTE_INPS", "CD_ANAG_PIGNORATO", "IDENTIFICATIVO_SDI") AS
  SELECT
--
-- Date: 11/07/2011
-- Version: 1.0
--
-- Vista di estrazione compensi con trattamenti soggetti al 770
--
-- History:
-- Date: 11/07/2011
-- Version: 1.0
-- Creazione
--
-- Gestione del Modello Semplificato/Ordinario e dei relativi Quadri
--
-- Date: 18/07/2012
-- Version: 1.1
-- Modifia
--
-- Gestione del nuovo quadro SY con l'indicazione del terzo pignorato
-- Body:
--
distinct
 comp.cd_cds
,comp.cd_unita_organizzativa
,comp.esercizio
,comp.pg_compenso
,to_number(to_char(comp.dt_emissione_mandato,'YYYY'))
,ter.cd_anag
,comp.cd_trattamento
,conf.cd_quadro
,null ds_quadro
,qua.ti_modello
,conf.ti_ritenuta
,tc.cd_ti_compenso
,tc.ds_ti_compenso
,comp.im_lordo_percipiente
,comp.im_no_fiscale
,comp.quota_esente
,comp.im_netto_percipiente
,comp.quota_esente_inps
,pignorato.cd_anag
,fp.identificativo_sdi
from terzo ter,
     configurazione_770 conf,
     quadri_770 qua,
	   compenso comp,
	   tipo_compenso tc,
	   terzo pignorato,
	   fattura_passiva fp
where comp.stato_cofi     = 'P' -- solo compensi pagati
  and comp.cd_terzo  	  = ter.cd_terzo
  and comp.cd_trattamento = conf.cd_trattamento
  and comp.esercizio 	  = conf.esercizio
  and conf.esercizio    = qua.esercizio
  and ((qua.cd_quadro ='SCSY' and
    	conf.cd_quadro in('SC','SY')) or
  		conf.cd_quadro    like  qua.cd_quadro)
  and tc.cd_trattamento = comp.cd_trattamento
  and comp.cd_terzo_pignorato = pignorato.cd_terzo (+)
  and comp.cd_cds = fp.cds_compenso(+)
  and comp.esercizio = fp.esercizio_compenso(+)
  and comp.cd_unita_organizzativa = fp.uo_compenso(+)
  and comp.pg_compenso = fp.pg_compenso(+);

   COMMENT ON TABLE "V_COMPENSO_770"  IS 'Vista di estrazione compensi con trattamenti soggetti al 770';
