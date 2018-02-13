--------------------------------------------------------
--  DDL for View VP_REVERSALE_ASS_MANDATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_REVERSALE_ASS_MANDATO" ("CD_CDS", "ESERCIZIO", "PG_REVERSALE", "TI_RECORD", "ID_RIGA", "DT_EMISSIONE", "TI_REVERSALE", "TI_COMPETENZA_RESIDUO", "DS_CDS", "CD_UO_ORIGINE", "DS_UO_CDS", "CAB", "ABI", "NUMERO_CONTO", "INTESTAZIONE", "DS_ABICAB", "VIA", "CAP", "CD_PROVINCIA", "IM_REVERSALE", "IM_REVERSALE_LETTERE", "CD_TERZO", "DENOMINAZIONE_SEDE", "VIA_SEDE", "CAP_COMUNE_SEDE", "CD_PV_TERZO", "CODICE_FISCALE", "CD_TIPO_DOCUMENTO_AMM", "PG_DOC_AMM", "IM_REVERSALE_RIGA", "IM_REVERSALE_RIGA_LETTERE", "DS_REVERSALE_RIGA", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "DS_ACCERTAMENTO", "CD_VOCE", "DS_VOCE", "CD_SOSPESO", "IM_ASSOCIATO", "DT_REGISTRAZ_SOSPESO", "UTCR", "DACR", "CIN", "DS_COMUNE", "DS_COMUNE_SEDE", "ESERCIZIO_MANDATO", "CD_CDS_MANDATO", "PG_MANDATO", "ESERCIZIO_SIOPE", "TI_GESTIONE", "CD_SIOPE", "DS_SIOPE", "IM_SIOPE", "CODICE_IBAN") AS 
  (SELECT
--
-- Date: 22/05/2007
-- Version: 1.2
--
-- Vista per la stampa delle reversali associate ai mandati
--
--
-- History:
--
-- Date: 30/11/05
-- Version: 1.0
-- Creazione
--
-- Date: 19/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 22/05/2007
-- Version: 1.2
-- Gestione Istat-Siope:
-- aggiunti i campi ESERCIZIO_SIOPE,TI_GESTIONE ,CD_SIOPE ,ds_siope,im_siope utili alla stampa
--
-- Body:
--
 rev.cd_cds
,rev.esercizio
,rev.pg_reversale
,'A'
,null
,rev.DT_EMISSIONE
,rev.ti_reversale
,rev.ti_competenza_residuo
,uo1.DS_UNITA_ORGANIZZATIVA ds_cds
,rev.CD_UO_ORIGINE
,uo2.DS_UNITA_ORGANIZZATIVA ds_uo_cds
,ban.CAB
,ban.ABI
,ban.NUMERO_CONTO
,ban.INTESTAZIONE
,abi.DS_ABICAB
,abi.VIA
,abi.CAP
,com.CD_PROVINCIA
,rev.IM_REVERSALE
,IBMUTL001.INLETTERE(rev.IM_REVERSALE)
,rg.CD_TERZO
,l.DENOMINAZIONE_SEDE
,l.VIA_SEDE || ' ' || l.NUMERO_CIVICO_SEDE
,l.CAP_COMUNE_SEDE
,l.CD_PROVINCIA_SEDE CD_PV_TERZO
,l.CODICE_FISCALE
,null   --CD_TIPO_DOCUMENTO_AMM
,0      --PG_DOC_AMM
,0		--IM_REVERSALE_RIGA
,null   --INLETTERE(rriga.IM_REVERSALE_RIGA)
,null   --DS_REVERSALE_RIGA
,null   --ESERCIZIO_ORI_ACCERTAMENTO
,0		--PG_ACCERTAMENTO
,null   --DS_ACCERTAMENTO
,null   --cd_voce
,null   --ds_voce
,null
,0
,to_date(null)
,rev.UTCR
,rev.DACR
,ban.CIN
,com.DS_COMUNE
,l.DS_COMUNE_SEDE
,ass.ESERCIZIO_MANDATO
,ass.CD_CDS_MANDATO
,ass.PG_MANDATO
,null
,null
,null
,null
,Null
,ban.codice_iban
from reversale rev
 	,unita_organizzativa uo1
 	,unita_organizzativa uo2
	,(select cd_cds, esercizio, PG_REVERSALE, min(cd_terzo_uo) cd_terzo_uo, min(PG_BANCA) pg_banca, min(cd_terzo) cd_terzo
	  from reversale_riga
	  group by cd_cds, esercizio, PG_REVERSALE) rg
 	,banca ban
 	,abicab abi
 	,comune com
	,v_anagrafico_terzo l
	,ass_mandato_reversale ass
where uo1.CD_UNITA_ORGANIZZATIVA = rev.CD_CDS
  and uo2.CD_UNITA_ORGANIZZATIVA = rev.CD_UO_ORIGINE
  and rg.cd_cds 				 = rev.cd_cds
  and rg.esercizio 				 = rev.esercizio
  and rg.pg_reversale 			 = rev.pg_reversale
  and ban.cd_terzo 			 (+) = rg.cd_terzo_uo
  and ban.PG_BANCA 			 (+) = rg.pg_banca
  and abi.ABI 				 (+) = ban.ABI
  and abi.CAB 				 (+) = ban.CAB
  and com.PG_COMUNE 		 (+) = abi.PG_COMUNE
  and l.cd_terzo  	  	  	 	 = rg.cd_terzo
  And rev.cd_cds                     = ass.cd_cds_reversale
  And rev.esercizio                  = ass.esercizio_reversale
  And rev.pg_reversale               = ass.pg_reversale
)
union all
(select
 rev.cd_cds
,rev.esercizio
,rev.pg_reversale
,'B'
,rowidtochar(rriga.rowid)
,rev.DT_EMISSIONE
,rev.ti_reversale
,rev.ti_competenza_residuo
,null   --uo1.DS_UNITA_ORGANIZZATIVA ds_cds
,null   --rev.CD_UO_ORIGINE
,null   --uo2.DS_UNITA_ORGANIZZATIVA ds_uo_cds
,null   --ban1.CAB
,null   --ban1.ABI
,null   --ban1.NUMERO_CONTO
,null   --ban1.INTESTAZIONE
,Null   --abi1.DS_ABICAB
,null   --abi1.VIA
,null   --abi1.CAP
,null   --com1.CD_PROVINCIA
,0   	--rev.IM_REVERSALE
,null   --IBMUTL001.INLETTERE(rev.IM_REVERSALE)
,rriga.CD_TERZO
,l.DENOMINAZIONE_SEDE
,l.VIA_SEDE || ' ' || l.NUMERO_CIVICO_SEDE
,l.CAP_COMUNE_SEDE
,l.CD_PROVINCIA_SEDE CD_PV_TERZO
,l.CODICE_FISCALE
,rriga.CD_TIPO_DOCUMENTO_AMM
,rriga.PG_DOC_AMM
,rriga.IM_REVERSALE_RIGA
,IBMUTL001.INLETTERE(rriga.IM_REVERSALE_RIGA)
,rriga.DS_REVERSALE_RIGA
,rriga.ESERCIZIO_ORI_ACCERTAMENTO
,rriga.PG_ACCERTAMENTO
,acc.DS_ACCERTAMENTO
,acc.cd_voce
,voce.ds_voce
,null
,0
,to_date(null)
,rev.UTCR
,rev.DACR
,Null
,Null
,Null
,ass.ESERCIZIO_MANDATO
,ass.CD_CDS_MANDATO
,ass.PG_MANDATO
,null
,null
,null
,null
,Null
,Null   --ban1.codice_iban
from reversale rev
	,reversale_riga rriga
	,v_anagrafico_terzo l
	,accertamento acc
	,voce_f voce
	,ass_mandato_reversale ass
where rriga.CD_CDS	  	  = rev.CD_CDS
  and rriga.ESERCIZIO	  = rev.ESERCIZIO
  and rriga.PG_REVERSALE  = rev.PG_REVERSALE
  and l.cd_terzo  	  	  = rriga.cd_terzo
  and acc.CD_CDS 		  = rriga.CD_CDS
  and acc.ESERCIZIO 	  = rriga.ESERCIZIO
  and acc.ESERCIZIO_ORIGINALE 	  = rriga.ESERCIZIO_ORI_ACCERTAMENTO
  and acc.PG_ACCERTAMENTO = rriga.PG_ACCERTAMENTO
  and voce.ESERCIZIO 	  = acc.ESERCIZIO
  and voce.TI_APPARTENENZA in ('C','D')
  and voce.TI_GESTIONE 	  = 'E'
  and voce.CD_VOCE 		  = acc.CD_VOCE
  And rev.cd_cds                     = ass.cd_cds_reversale
  And rev.esercizio                  = ass.esercizio_reversale
  And rev.pg_reversale               = ass.pg_reversale
)
union all
(select
 rev.cd_cds
,rev.esercizio
,rev.pg_reversale
,'C'
,null
,rev.DT_EMISSIONE
,rev.TI_REVERSALE
,rev.TI_COMPETENZA_RESIDUO
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
,0
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
,0
,null
,null
,null
,sosp.CD_SOSPESO
,sosp.IM_ASSOCIATO
,s.DT_REGISTRAZIONE
,rev.UTCR
,rev.DACR
,Null
,Null
,Null
,ass.ESERCIZIO_MANDATO
,ass.CD_CDS_MANDATO
,ass.PG_MANDATO
,null
,null
,null
,null
,Null
,Null   --ban1.codice_iban
From reversale rev
	,sospeso_det_etr sosp
	,sospeso s
	,ass_mandato_reversale ass
where sosp.CD_CDS_reversale        (+) = rev.cd_cds
  and sosp.ESERCIZIO     (+) = rev.esercizio
  and sosp.PG_REVERSALE  (+) = rev.pg_reversale
  and s.CD_CDS 			 (+) = sosp.CD_CDS
  and s.ESERCIZIO 		 (+) = sosp.ESERCIZIO
  and s.TI_ENTRATA_SPESA (+) = sosp.TI_ENTRATA_SPESA
  and s.TI_SOSPESO_RISCONTRO (+) = sosp.TI_SOSPESO_RISCONTRO
  and s.CD_SOSPESO 		 (+) = sosp.CD_SOSPESO
  AND sosp.STATO		  <> 'A'
  And sosp.TI_SOSPESO_RISCONTRO = 'S'
  And rev.cd_cds                     = ass.cd_cds_reversale
  And rev.esercizio                  = ass.esercizio_reversale
  And rev.pg_reversale               = ass.pg_reversale
)
union all
(select Distinct
 rev.cd_cds
,rev.esercizio
,rev.pg_reversale
,'D'
,null
,rev.DT_EMISSIONE
,rev.TI_REVERSALE
,rev.TI_COMPETENZA_RESIDUO
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
,0
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
,0
,null
,null
,null
,null--sosp.CD_SOSPESO
,null--sosp.IM_ASSOCIATO
,null--s.DT_REGISTRAZIONE
,null--rev.UTCR
,null--rev.DACR
,Null
,Null
,Null
,ass.ESERCIZIO_MANDATO
,ass.CD_CDS_MANDATO
,ass.PG_MANDATO
,rsiope.ESERCIZIO_SIOPE
,rsiope.TI_GESTIONE
,a.CD_SIOPE
,a.ds_siope
,a.im_siope
,Null   --ban1.codice_iban
From reversale rev
	,reversale_siope rsiope
	,ass_mandato_reversale ass,
	 (select distinct rsiope.esercizio,rsiope.cd_cds,rsiope.cd_siope CD_SIOPE, descrizione DS_SIOPE, sum(importo) IM_SIOPE, ass.pg_mandato, ass.pg_reversale
   	  from reversale_siope rsiope, codici_siope siope, ass_mandato_reversale ass,reversale rev
		where rsiope.CD_CDS = rev.CD_CDS
  		and rsiope.ESERCIZIO  = rev.ESERCIZIO
  		and rsiope.PG_REVERSALE = rev.PG_REVERSALE
		and rsiope.esercizio_siope = siope.esercizio
		and rsiope.ti_gestione = siope.ti_gestione
		And rsiope.cd_cds         = ass.cd_cds_reversale
  		And rsiope.esercizio      = ass.esercizio_reversale
  		And rsiope.pg_reversale   = ass.pg_reversale
		and rsiope.cd_siope = siope.cd_siope
		group by rsiope.esercizio,rsiope.cd_cds,rsiope.cd_siope, descrizione, ass.pg_mandato, ass.pg_reversale) a
where rsiope.CD_CDS	  	  = rev.CD_CDS
  and rsiope.ESERCIZIO	  = rev.ESERCIZIO
  and rsiope.PG_REVERSALE  = rev.PG_REVERSALE
  And rev.cd_cds                     = ass.cd_cds_reversale
  And rev.esercizio                  = ass.esercizio_reversale
  And rev.pg_reversale               = ass.pg_reversale
  and a.CD_siope = rsiope.CD_siope
  and a.pg_mandato = ass.pg_mandato
  and a.pg_reversale = ass.pg_reversale
  And a.pg_reversale = rsiope.pg_reversale
  and a.esercizio = rsiope.esercizio
  and a.cd_cds = rsiope.cd_cds);
