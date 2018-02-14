--------------------------------------------------------
--  DDL for View VP_MANDATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_MANDATO" ("CD_CDS", "ESERCIZIO", "PG_MANDATO", "TI_RECORD_L1", "TI_RECORD_L2", "ID_RIGA", "DT_EMISSIONE", "TI_MANDATO", "TI_COMPETENZA_RESIDUO", "DS_CDS", "CD_UO_ORIGINE", "DS_UO_CDS", "CAB", "ABI", "NUMERO_CONTO", "INTESTAZIONE", "DS_ABICAB", "VIA", "CAP", "CD_PROVINCIA", "IM_NETTO", "IM_NETTO_LETTERE", "DS_MANDATO", "CD_TERZO", "CD_UO_TERZO", "DENOMINAZIONE_SEDE", "VIA_SEDE", "CAP_COMUNE_SEDE", "CD_PROVINCIA_BENEFICIARIO", "CODICE_FISCALE", "PARTITA_IVA", "CD_TIPO_DOCUMENTO_AMM", "PG_DOC_AMM", "IM_NETTO_RIGA", "IM_NETTO_RIGA_LETTERE", "CD_MODALITA_PAG", "DS_MODALITA_PAG", "TI_PAGAMENTO", "NUMERO_CONTO_TERZO", "INTESTAZIONE_TERZO", "CAB_TERZO", "ABI_TERZO", "DS_ABICAB_TERZO", "VIA_BANCA_TERZO", "CAP_BANCA_TERZO", "CD_PV_BANCA_TERZO", "DS_MANDATO_RIGA", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "DS_OBBLIGAZIONE", "CD_VOCE", "DS_VOCE", "PG_REVERSALE", "DS_REVERSALE_RIGA", "IM_REVERSALE_RIGA", "IM_MANDATO", "IM_RITENUTE", "CD_TIPO_BOLLO", "DS_TIPO_BOLLO", "CD_SOSPESO", "IM_ASSOCIATO", "DT_REGISTRAZ_SOSPESO", "UTCR", "DACR") AS 
  (SELECT
--
-- Date: 18/07/2006
-- Version: 2.6
--
-- Vista per la stampa dei mandati
--
--
-- History:
--
-- Date: 13/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/09/2002
-- Version: 1.1
-- Riorganizzazione della vista per la gestione di reversali, capitoli e sospesi
-- multipli sullo stesso mandato. Introduzione di TI_RECORD per identificare
-- i record della vista
-- TI_RECORD = 'C' -> Record relativi alle righe del mandato con i capitoli
-- TI_RECORD = 'R' -> Record relativi alle reversali associate al mandato
-- TI_RECORD = 'S' -> Record relativi ai sospesi associati al mandato
--
-- Date: 19/09/2002
-- Version: 1.2
-- Inserito numero riga di mandato_riga
-- Aggiunto ulteriore gruppo:
-- TI_RECORD = 'O' -> Record relativi ai capitoli delle obbligazioni
--
-- Date: 20/09/2002
-- Version: 1.3
-- Sistemazione delle join
--
-- Date: 25/09/2002
-- Version: 1.4
-- Modifiche tipologie dei record e dell'identificativo della riga di mandato
--
-- Date: 27/09/2002
-- Version: 1.5
-- Correzione c/c dell'Istituto Cassiere
--
-- Date: 02/10/2002
-- Version: 1.6
-- Inserito importo associato a sospeso. Inserito livello dei bolli
--
-- Date: 16/10/2002
-- Version: 1.7
-- Modifica del conto corrente dell'Istituto Cassiere (c/c uo emittente)
--
-- Date: 21/10/2002
-- Version: 1.8
-- Aggiunta estrazione sospeso.dt_registrazione, data e utenza
-- creazione mandato.
--
-- Date: 30/10/2002
-- Version: 1.9
-- Estrazione ti_competenza_residuo in tutti i tipi di record
-- (per layout del report)
--
-- Date: 21/11/2002
-- Version: 2.0
-- Concatenato numero civico all'indirizzo del terzo
-- Aggiunte informazioni di testata ai record di riga per layout di stampa
-- Aggiunte informazioni sul terzo in testata per layout di stampa
--
-- Date: 26/11/2002
-- Version: 2.1
-- Eliminate informazioni di testata dai record delle righe
--
-- Date: 23/01/2003
-- Version: 2.2
-- 1.Modifica del conto corrente dell'Istituto Cassiere, uso del flag fl_cc_cds.
-- 2.Estrazione ti_pagamento, ds_modalita_pagamento.
-- 3.Eliminazione dell'estrazione delle informazioni bancarie del terzo per i record
--   di dettaglio delle righe.
-- 4.Estrazione partita iva del beneficiario
-- 5.Estrazione cd_uo corrispondente al beneficiario (per mandati di accreditamento cds -> cnr)
-- 6.Estrazione intestazione bancaria del terzo
--
-- Date: 10/02/2003
-- Version: 2.3
-- Modifica del c/c dell'Istituto Cassiere: cd_unita_organizzativa e NON cd_uo_origine
--
-- Date: 07/03/2003
-- Version: 2.4
-- Estrazione DS_MANDATO come causale di pagamento
--
-- Date: 25/03/2003
-- Version: 2.5
-- Filtro sui sospesi
--
-- Date: 18/07/2006
-- Version: 2.6
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 man.cd_cds 	-- testata del mandato e informazioni del terzo (beneficiario)
,man.esercizio
,man.pg_mandato
,'A'
,'A'
,null
,man.DT_EMISSIONE
,man.TI_MANDATO
,man.TI_COMPETENZA_RESIDUO
,uo1.DS_UNITA_ORGANIZZATIVA DS_CDS
,man.CD_UO_ORIGINE
,uo2.DS_UNITA_ORGANIZZATIVA DS_UO_CDS
,ban.CAB
,ban.ABI
,ban.NUMERO_CONTO
,ban.INTESTAZIONE
,abi.DS_ABICAB
,abi.VIA
,abi.CAP
,com.CD_PROVINCIA
,man.IM_MANDATO - man.IM_RITENUTE
,IBMUTL001.INLETTERE(man.IM_MANDATO - man.IM_RITENUTE)
,man.DS_MANDATO
,vmriga.CD_TERZO
,l.CD_UNITA_ORGANIZZATIVA
,l.DENOMINAZIONE_SEDE
,l.VIA_SEDE || ' ' || l.NUMERO_CIVICO_SEDE
,l.CAP_COMUNE_SEDE
,l.CD_PROVINCIA_SEDE
,l.CODICE_FISCALE
,l.PARTITA_IVA
,null -- CD_TIPO_DOCUMENTO_AMM
,0 	  -- PG_DOC_AMM
,0 	  -- IM_MANDATO_RIGA - IM_RITENUTE_RIGA
,null -- INLETTERE(IM_MANDATO_RIGA - IM_RITENUTE_RIGA)
,vmriga.CD_MODALITA_PAG
,rifmp.DS_MODALITA_PAG
,ban2.TI_PAGAMENTO
,ban2.NUMERO_CONTO
,ban2.INTESTAZIONE
,ban2.CAB
,ban2.ABI
,abi2.DS_ABICAB
,abi2.VIA
,abi2.CAP
,com2.CD_PROVINCIA
,null
,null
,0
,null
,null
,null
,0
,null
,0
,man.IM_MANDATO
,man.IM_RITENUTE
,null
,null
,null
,0
,to_date(null)
,man.UTCR
,man.DACR
from
  	 mandato man
 	,unita_organizzativa uo1
	,unita_organizzativa uo2
 	,terzo ter
 	,banca ban
 	,abicab abi
 	,comune com
	,(select distinct mriga.CD_CDS
			 		 ,mriga.ESERCIZIO
					 ,mriga.PG_MANDATO
					 ,mriga.CD_TERZO
					 ,mriga.PG_BANCA
					 ,mriga.CD_MODALITA_PAG
	  from mandato_riga mriga)  vmriga
	,rif_modalita_pagamento rifmp
	,v_anagrafico_terzo l
	,banca ban2
	,abicab abi2
	,comune com2
where
      uo1.CD_UNITA_ORGANIZZATIVA  = man.CD_CDS
  and uo2.CD_UNITA_ORGANIZZATIVA  = man.cd_uo_origine
  and ter.cd_unita_organizzativa  = man.CD_UNITA_ORGANIZZATIVA
  and ban.CD_TERZO 		          = ter.cd_terzo
  and ban.FL_CC_CDS		  		  = 'Y'
  and abi.ABI (+) 		  		  = ban.ABI
  and abi.CAB (+) 		  		  = ban.CAB
  and com.PG_COMUNE (+)   		  = abi.PG_COMUNE
  and vmriga.CD_CDS	       		  = man.CD_CDS
  and vmriga.ESERCIZIO      	  = man.ESERCIZIO
  and vmriga.PG_MANDATO     	  = man.PG_MANDATO
  and rifmp.CD_MODALITA_PAG		  = vmriga.CD_MODALITA_PAG
  and l.cd_terzo 	       = vmriga.cd_terzo
  and ban2.cd_terzo    (+) = vmriga.cd_terzo
  and ban2.pg_banca    (+) = vmriga.pg_banca
  and abi2.ABI 		   (+) = ban2.ABI
  and abi2.CAB 		   (+) = ban2.CAB
  and com2.PG_COMUNE   (+) = abi2.PG_COMUNE
)
union all
select 	  			   -- dettagli delle righe di mandato
 man.cd_cds
,man.esercizio
,man.pg_mandato
,'B'
,'A'
,rowidtochar(mriga.rowid)
,man.DT_EMISSIONE
,man.TI_MANDATO
,man.TI_COMPETENZA_RESIDUO
,null  -- uo1.DS_UNITA_ORGANIZZATIVA DS_CDS
,null  -- man.CD_UO_ORIGINE
,null  -- uo2.DS_UNITA_ORGANIZZATIVA DS_UO_CDS
,null  -- ban.CAB
,null  -- ban.ABI
,null  -- ban.NUMERO_CONTO
,null  -- ban.INTESTAZIONE
,null  -- abi.DS_ABICAB
,null  -- abi.VIA
,null  -- abi.CAP
,null  -- com.CD_PROVINCIA
,0	   -- man.IM_MANDATO - man.IM_RITENUTE
,null  -- IBMUTL001.INLETTERE(man.IM_MANDATO - man.IM_RITENUTE)
,man.DS_MANDATO
,mriga.CD_TERZO
,null  -- l.CD_UNITA_ORGANIZZATIVA
,null  -- l.DENOMINAZIONE_SEDE
,null  -- l.VIA_SEDE || ' ' || l.NUMERO_CIVICO_SEDE
,null  -- l.CAP_COMUNE_SEDE
,null  -- l.CD_PROVINCIA_SEDE
,null  -- l.CODICE_FISCALE
,null  -- l.PARTITA_IVA
,mriga.CD_TIPO_DOCUMENTO_AMM
,mriga.PG_DOC_AMM
,mriga.IM_MANDATO_RIGA - mriga.IM_RITENUTE_RIGA
,IBMUTL001.INLETTERE(mriga.IM_MANDATO_RIGA - mriga.IM_RITENUTE_RIGA)
,null			   -- mriga.CD_MODALITA_PAG
,null			   -- rifmp.DS_MODALITA_PAG
,null			   -- ban2.TI_PAGAMENTO
,null			   -- ban2.NUMERO_CONTO
,null			   -- ban2.INTESTAZIONE
,null			   -- ban2.CAB
,null			   -- ban2.ABI
,null			   -- abi2.DS_ABICAB
,null			   -- abi2.VIA
,null			   -- abi2.CAP
,null			   -- com2.CD_PROVINCIA
,mriga.DS_MANDATO_RIGA
,mriga.ESERCIZIO_ORI_OBBLIGAZIONE
,mriga.PG_OBBLIGAZIONE
,obb.DS_OBBLIGAZIONE
,null
,null
,0
,null
,0
,man.IM_MANDATO
,man.IM_RITENUTE
,null
,null
,null
,0
,to_date(null)
,mriga.UTCR
,mriga.DACR
from mandato man
	,mandato_riga mriga
	,obbligazione obb
where mriga.CD_CDS	       = man.CD_CDS
  and mriga.ESERCIZIO      = man.ESERCIZIO
  and mriga.PG_MANDATO     = man.PG_MANDATO
  and obb.CD_CDS 	   	   = mriga.CD_CDS
  and obb.ESERCIZIO        = mriga.ESERCIZIO
  and obb.ESERCIZIO_ORIGINALE  = mriga.ESERCIZIO_ORI_OBBLIGAZIONE
  and obb.PG_OBBLIGAZIONE  = mriga.PG_OBBLIGAZIONE
union all
(select   		  -- reversali associate al mandato
 man.cd_cds
,man.esercizio
,man.pg_mandato
,'C'
,'A'
,null
,man.DT_EMISSIONE
,man.TI_MANDATO
,man.TI_COMPETENZA_RESIDUO
,null   -- uo1.DS_UNITA_ORGANIZZATIVA DS_CDS
,null 	-- man.CD_UO_ORIGINE
,null 	-- uo2.DS_UNITA_ORGANIZZATIVA DS_UO_CDS
,null 	-- ban.CAB
,null 	-- ban.ABI
,null 	-- ban.NUMERO_CONTO
,null 	-- ban.INTESTAZIONE
,null  	-- abi.DS_ABICAB
,null  	-- abi.VIA
,null  	-- abi.CAP
,null  	-- com.CD_PROVINCIA
,0 		-- man.IM_MANDATO - man.IM_RITENUTE
,null 	-- IBMUTL001.INLETTERE(man.IM_MANDATO - man.IM_RITENUTE)
,null	-- man.DS_MANDATO
,0 		-- mriga.CD_TERZO
,null	-- l.CD_UNITA_ORGANIZZATIVA
,null  	-- l.DENOMINAZIONE_SEDE
,null  	-- l.VIA_SEDE || ' ' || l.NUMERO_CIVICO_SEDE
,null  	-- l.CAP_COMUNE_SEDE
,null  	-- l.CD_PROVINCIA_SEDE
,null  	-- l.CODICE_FISCALE
,null  	-- l.PARTITA_IVA
,null 	-- mriga.CD_TIPO_DOCUMENTO_AMM
,0 		-- mriga.PG_DOC_AMM
,0 		-- mriga.IM_MANDATO_RIGA - mriga.IM_RITENUTE_RIGA
,null 	-- IBMUTL001.INLETTERE(mriga.IM_MANDATO_RIGA - mriga.IM_RITENUTE_RIGA)
,null			   -- mriga.CD_MODALITA_PAG
,null			   -- rifmp.DS_MODALITA_PAG
,null			   -- ban2.TI_PAGAMENTO
,null			   -- ban2.NUMERO_CONTO
,null			   -- ban2.INTESTAZIONE
,null			   -- ban2.CAB
,null			   -- ban2.ABI
,null			   -- abi2.DS_ABICAB
,null			   -- abi2.VIA
,null			   -- abi2.CAP
,null			   -- com2.CD_PROVINCIA
,null 			   -- mriga.DS_MANDATO_RIGA
,null 			   -- mriga.ESERCIZIO_ORI_OBBLIGAZIONE
,0 				   -- mriga.PG_OBBLIGAZIONE
,null 			   -- obb.DS_OBBLIGAZIONE
,null
,null
,assmr.PG_REVERSALE
,r.DS_REVERSALE
,r.IM_REVERSALE
,0
,0
,null
,null
,null
,0
,to_date(null)
,man.UTCR
,man.DACR
from mandato man
	,ass_mandato_reversale assmr
	,reversale r
where assmr.CD_CDS_MANDATO  = man.cd_cds
  and assmr.ESERCIZIO_MANDATO  = man.esercizio
  and assmr.PG_MANDATO  = man.pg_mandato
  and r.CD_CDS  = assmr.CD_CDS_REVERSALE
  and r.ESERCIZIO  = assmr.ESERCIZIO_REVERSALE
  and r.PG_REVERSALE  = assmr.PG_REVERSALE
)
union all
(select   			  -- sospesi associati al mandato
 man.cd_cds
,man.esercizio
,man.pg_mandato
,'D'
,'A'
,null
,man.DT_EMISSIONE
,man.TI_MANDATO
,man.TI_COMPETENZA_RESIDUO
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,0
,null
,null
,0
,null
,null
,null
,null
,null
,null
,null
,null
,0
,0
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,0
,null
,null
,null
,0
,null
,0
,0
,0
,null
,null
,sosp.CD_SOSPESO
,sosp.IM_ASSOCIATO
,s.DT_REGISTRAZIONE
,man.UTCR
,man.DACR
from mandato man
	,sospeso_det_usc sosp
	,sospeso s
where sosp.CD_CDS 	   	   		 = man.cd_cds
  and sosp.ESERCIZIO  			 = man.esercizio
  and sosp.PG_MANDATO  			 = man.pg_mandato
  and sosp.TI_SOSPESO_RISCONTRO  = 'S'
  and sosp.STATO 				 <> 'A'
  and s.CD_CDS 					 = sosp.CD_CDS
  and s.ESERCIZIO 				 = sosp.ESERCIZIO
  and s.TI_ENTRATA_SPESA 		 = sosp.TI_ENTRATA_SPESA
  and s.TI_SOSPESO_RISCONTRO 	 = sosp.TI_SOSPESO_RISCONTRO
  and s.CD_SOSPESO 				 = sosp.CD_SOSPESO
)
union all
(select distinct       -- capitoli associati alle obbligazioni delle righe di mandato
 mriga.cd_cds
,mriga.esercizio
,mriga.pg_mandato
,'B'
,'B'
,rowidtochar(mriga.rowid)
,to_date(null)
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,0
,null
,null
,0
,null
,null
,null
,null
,null
,null
,null
,null
,0
,0
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,mriga.ESERCIZIO_ORI_OBBLIGAZIONE
,mriga.PG_OBBLIGAZIONE
,null
,obbv.CD_VOCE
,voce.DS_VOCE
,0
,null
,0
,0
,0
,null
,null
,null
,0
,to_date(null)
,mriga.UTCR
,mriga.DACR
from
	 mandato_riga mriga
	,obbligazione_scad_voce obbv
	,voce_f voce
where
      obbv.cd_cds = mriga.CD_CDS
  and obbv.esercizio = mriga.ESERCIZIO
  and obbv.esercizio_originale = mriga.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbv.pg_obbligazione = mriga.PG_OBBLIGAZIONE
  and obbv.pg_obbligazione_scadenzario = mriga.PG_OBBLIGAZIONE_SCADENZARIO
  and voce.ESERCIZIO = obbv.ESERCIZIO
  and voce.TI_GESTIONE = obbv.ti_gestione
  and voce.TI_APPARTENENZA = obbv.ti_appartenenza
  and voce.CD_VOCE = obbv.CD_VOCE
)
union all
(select   		   -- bolli associati al mandato
 man.cd_cds
,man.esercizio
,man.pg_mandato
,'E'
,'A'
,null
,man.DT_EMISSIONE
,man.TI_MANDATO
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,0
,null
,null
,0
,null
,null
,null
,null
,null
,null
,null
,null
,0
,0
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,null
,0
,null
,null
,null
,0
,null
,0
,0
,0
,mterzo.CD_TIPO_BOLLO
,tbollo.DS_TIPO_BOLLO
,null
,0
,to_date(null)
,man.UTCR
,man.DACR
from
	 mandato man
	,mandato_terzo mterzo
	,tipo_bollo tbollo
where mterzo.CD_CDS     = man.CD_CDS
  and mterzo.ESERCIZIO  = man.ESERCIZIO
  and mterzo.PG_MANDATO = man.PG_MANDATO
  and tbollo.CD_TIPO_BOLLO 	= mterzo.CD_TIPO_BOLLO
);

   COMMENT ON TABLE "VP_MANDATO"  IS 'Vista per la stampa dei mandati';
