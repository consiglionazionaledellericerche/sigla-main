--------------------------------------------------------
--  DDL for View V_LIQUID_IVA_ANN_ACQUISTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_IVA_ANN_ACQUISTI" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_FATTURA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "CD_TIPO_SEZ_AUTOFATT", "TI_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "DT_REGISTRAZIONE", "DT_EMISSIONE", "TI_ISTITUZ_COMMERC_TSTA", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "TOTALE_DETTAGLIO", "FL_INTRA_UE", "FL_EXTRA_UE", "FL_BOLLA_DOGANALE", "FL_SPEDIZIONIERE", "FL_SAN_MARINO_SENZA_IVA", "FL_SAN_MARINO_CON_IVA", "CD_VOCE_IVA", "TI_ISTITUZ_COMMERC_RIGA", "CD_BENE_SERVIZIO", "DS_VOCE_IVA", "FL_ESCLUSO", "FL_NON_SOGGETTO", "FL_GESTIONE_INVENTARIO", "TI_BENE_SERVIZIO", "ESIGIBILITA_DIFFERITA", "DT_ESIGIBILITA_DIFFERITA", "IMPONIBILE_DETT_COLL", "IVA_DETT_COLL", "IVA_INDET_DETT_COLL", "TOT_DETT_COLL", "FL_CODIVA_AUTOFATTURA") AS 
  select
--
-- Date: 01/12/2003
-- Version: 1.0
--
-- Vista per le estrazione delle liquidazioni IVA annuali, parte acquisti
--
-- History:
--
-- Date: 27/11/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
fp.CD_CDS,
fp.CD_UNITA_ORGANIZZATIVA,
fp.ESERCIZIO,
fp.PG_FATTURA_PASSIVA,
fp.CD_CDS_ORIGINE,
fp.CD_UO_ORIGINE,
fp.CD_TIPO_SEZIONALE,
auto.CD_TIPO_SEZIONALE,
fp.TI_FATTURA,
fp.PROTOCOLLO_IVA,
fp.PROTOCOLLO_IVA_GENERALE,
fp.DT_REGISTRAZIONE,
fp.DT_FATTURA_FORNITORE,
fp.TI_ISTITUZ_COMMERC,
DECODE(fp.ti_fattura, 'C', (fpr.im_imponibile * -1),fpr.im_imponibile),
DECODE(fp.ti_fattura, 'C', (fpr.im_iva * -1), fpr.im_iva),
0,
DECODE(fp.ti_fattura, 'C', ((fpr.im_imponibile + fpr.im_iva) * -1), fpr.im_imponibile + fpr.im_iva),
fp.FL_INTRA_UE,
fp.FL_EXTRA_UE,
fp.FL_BOLLA_DOGANALE,
fp.FL_SPEDIZIONIERE,
fp.FL_SAN_MARINO_SENZA_IVA,
fp.FL_SAN_MARINO_CON_IVA,
fpr.CD_VOCE_IVA,
fpr.TI_ISTITUZ_COMMERC,
fpr.CD_BENE_SERVIZIO,
vi.DS_VOCE_IVA,
vi.FL_ESCLUSO,
vi.FL_NON_SOGGETTO,
bs.FL_GESTIONE_INVENTARIO,
bs.TI_BENE_SERVIZIO,
'N',
TRUNC(fp.DT_REGISTRAZIONE),
DECODE(fpc.ti_fattura, 'C', (fprc.im_imponibile * -1),fprc.im_imponibile),
DECODE(fpc.ti_fattura, 'C', (fprc.im_iva * -1), fprc.im_iva),
0,
DECODE(fpc.ti_fattura, 'C', ((fprc.im_imponibile + fprc.im_iva) * -1), fprc.im_imponibile + fprc.im_iva),
(select decode(count(0),0,null,vi.fl_autofattura) from fattura_passiva_riga riga,voce_iva
        where
        riga.CD_CDS                         = fp.CD_CDS                  	 	and
       	riga.CD_UNITA_ORGANIZZATIVA     		= fp.CD_UNITA_ORGANIZZATIVA    	and
        riga.ESERCIZIO                      = fp.ESERCIZIO                	and
       	riga.PG_FATTURA_PASSIVA             = fp.PG_FATTURA_PASSIVA 				and
       	riga.CD_VOCE_IVA                    = VOCE_IVA.CD_VOCE_IVA  			 and
       	VOCE_IVA.fl_autofattura ='Y') flag
from fattura_passiva fp
	,fattura_passiva_riga fpr
	,bene_servizio bs
	,voce_iva vi
	,fattura_passiva fpc
	,fattura_passiva_riga fprc
	,autofattura auto
where fp.DT_CANCELLAZIONE is null
  and fpr.CD_CDS 		  	 	  		  = fp.CD_CDS
  and fpr.CD_UNITA_ORGANIZZATIVA 		  = fp.CD_UNITA_ORGANIZZATIVA
  and fpr.ESERCIZIO				 		  = fp.ESERCIZIO
  and fpr.PG_FATTURA_PASSIVA	 		  = fp.PG_FATTURA_PASSIVA
  and bs.CD_BENE_SERVIZIO		 		  = fpr.CD_BENE_SERVIZIO
  and vi.CD_VOCE_IVA			 		  = fpr.CD_VOCE_IVA
  and fpc.CD_CDS_FAT_CLGS			  (+) = fp.CD_CDS
  and fpc.CD_UO_FAT_CLGS			  (+) = fp.cd_unita_organizzativa
  and fpc.ESERCIZIO_FAT_CLGS 		  (+) = fp.ESERCIZIO
  and fpc.PG_FATTURA_PASSIVA_FAT_CLGS (+) = fp.PG_FATTURA_PASSIVA
  and fprc.CD_CDS 		  	 	 	  (+) = fpc.CD_CDS
  and fprc.CD_UNITA_ORGANIZZATIVA 	  (+) = fpc.CD_UNITA_ORGANIZZATIVA
  and fprc.ESERCIZIO				  (+) = fpc.ESERCIZIO
  and fprc.PG_FATTURA_PASSIVA		  (+) = fpc.PG_FATTURA_PASSIVA
  and fprc.CD_BENE_SERVIZIO 		  (+) = '19'
  and auto.CD_CDS_FT_PASSIVA 		  (+) = fp.CD_CDS
  and auto.CD_UO_FT_PASSIVA 		  (+) = fp.CD_UNITA_ORGANIZZATIVA
  and auto.ESERCIZIO 				  (+) = fp.esercizio
  and auto.PG_FATTURA_PASSIVA 		  (+) = fp.pg_fattura_passiva;

   COMMENT ON TABLE "V_LIQUID_IVA_ANN_ACQUISTI"  IS 'Vista per le estrazione delle liquidazioni IVA annuali, parte acquisti';
