--------------------------------------------------------
--  DDL for View V_LIQUID_IVA_ANN_VENDITE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_IVA_ANN_VENDITE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_FATTURA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "DT_REGISTRAZIONE", "DT_EMISSIONE", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "TOTALE_DETTAGLIO", "FL_SAN_MARINO", "CD_VOCE_IVA", "DS_VOCE_IVA", "FL_ESCLUSO", "FL_NON_SOGGETTO", "ESIGIBILITA_DIFFERITA", "DT_ESIGIBILITA_DIFFERITA") AS 
  select
--
-- Date: 27/11/2003
-- Version: 1.0
--
-- Vista per le estrazione delle liquidazioni IVA annuali, parte vendite
--
-- History:
--
-- Date: 27/11/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
fa.CD_CDS,
fa.CD_UNITA_ORGANIZZATIVA,
fa.ESERCIZIO,
fa.PG_FATTURA_ATTIVA,
fa.CD_CDS_ORIGINE,
fa.CD_UO_ORIGINE,
fa.CD_TIPO_SEZIONALE,
fa.TI_FATTURA,
fa.PROTOCOLLO_IVA,
fa.PROTOCOLLO_IVA_GENERALE,
fa.DT_REGISTRAZIONE,
fa.DT_EMISSIONE,
DECODE(fa.ti_fattura, 'C', (SUM(far.im_imponibile) * -1), SUM(far.im_imponibile)),
DECODE(fa.ti_fattura, 'C', (SUM(far.im_iva) * -1), SUM(far.im_iva)),
DECODE(fa.ti_fattura, 'C', (SUM(far.im_imponibile + far.im_iva) * -1), SUM(far.im_imponibile + far.im_iva)),
fa.FL_SAN_MARINO,
far.CD_VOCE_IVA,
vi.DS_VOCE_IVA,
vi.FL_ESCLUSO,
vi.FL_NON_SOGGETTO,
fa.FL_LIQUIDAZIONE_DIFFERITA,
trunc(faR.DATA_ESIGIBILITA_IVA)
from fattura_attiva fa
	,fattura_attiva_riga far
	,voce_iva vi
where fa.DT_CANCELLAZIONE is null
  and far.CD_CDS		  	 	 = fa.CD_CDS
  and far.CD_UNITA_ORGANIZZATIVA = fa.CD_UNITA_ORGANIZZATIVA
  and far.ESERCIZIO				 = fa.ESERCIZIO
  and far.PG_FATTURA_ATTIVA		 = fa.PG_FATTURA_ATTIVA
  and vi.CD_VOCE_IVA			 = far.CD_VOCE_IVA
group by
fa.CD_CDS,
fa.CD_UNITA_ORGANIZZATIVA,
fa.ESERCIZIO,
fa.PG_FATTURA_ATTIVA,
fa.CD_CDS_ORIGINE,
fa.CD_UO_ORIGINE,
fa.CD_TIPO_SEZIONALE,
fa.TI_FATTURA,
fa.PROTOCOLLO_IVA,
fa.PROTOCOLLO_IVA_GENERALE,
fa.DT_REGISTRAZIONE,
fa.DT_EMISSIONE,
fa.FL_SAN_MARINO,
far.CD_VOCE_IVA,
vi.DS_VOCE_IVA,
vi.FL_ESCLUSO,
vi.FL_NON_SOGGETTO,
fa.FL_LIQUIDAZIONE_DIFFERITA,
trunc(faR.DATA_ESIGIBILITA_IVA);

   COMMENT ON TABLE "V_LIQUID_IVA_ANN_VENDITE"  IS 'Vista per le estrazione delle liquidazioni IVA annuali, parte vendite';
