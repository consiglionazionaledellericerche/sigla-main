--------------------------------------------------------
--  DDL for View VP_MISSIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_MISSIONE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_MISSIONE", "TI_RECORD_L1", "TI_RECORD_L2", "FL_RIMBORSO", "TI_PROVVISORIO_DEFINITIVO", "ES_FINANZIARIO", "DS_UNITA_ORGANIZZATIVA", "FL_ASSOCIATO_COMPENSO", "NOME", "COGNOME", "CD_TERZO", "VIA_SEDE", "NUMERO_CIVICO_SEDE", "CAP_COMUNE_SEDE", "DS_COMUNE", "CD_PROVINCIA", "TI_ANAGRAFICO", "MATRICOLA", "QUALIFICA", "TI_COMPETENZA_RESIDUO", "ESERCIZIO_ORI_OBBL_ACC", "PG_OBBL_ACC", "PG_OBBL_ACC_SCADENZARIO", "DT_SCADENZA", "PG_COMPENSO", "PG_MAN_REV", "IM_TOTALE_MISSIONE", "IM_DIARIA_LORDA", "IM_QUOTA_ESENTE", "IM_DIARIA_NETTO", "IM_SPESE", "IM_ANTICIPO", "IM_RIMBORSO", "IM_CR_ENTE", "IM_LORDO_PERCEPIENTE", "IM_NETTO_PECEPIENTE", "DS_MISSIONE", "DT_INIZIO_MISSIONE", "DT_FINE_MISSIONE", "DS_MODALITA_PAG", "INTESTAZIONE", "NUMERO_CONTO", "ABI", "CAB", "DS_ABICAB", "VIA_BANCA", "CAP_BANCA", "DS_COMUNE_BANCA", "CD_PROVINCIA_BANCA", "CD_VOCE", "CD_CONTRIBUTO_RITENUTA", "TI_ENTE_PERCIPIENTE", "AMMONTARE", "ALIQUOTA", "IMPONIBILE", "DS_CONTRIBUTO_RITENUTA", "PG_RIGA", "DT_INIZIO_TAPPA", "DS_SPESA", "FL_SPESA_ANTICIPATA", "CD_DIVISA_SPESA", "IM_SPESA_DIVISA", "CAMBIO_SPESA", "IM_BASE_MAGGIORAZIONE", "PERCENTUALE_MAGGIORAZIONE", "IM_MAGGIORAZIONE", "IM_SPESA_EURO", "IM_TOTALE_SPESA", "TI_AUTO", "CHILOMETRI", "INDENNITA_CHILOMETRICA", "IM_SPESE_ANTICIPATE", "DT_FINE_TAPPA", "CD_DIVISA_TAPPA", "CAMBIO_TAPPA", "IM_DIARIA", "IM_DIARIA_LORDA_DET", "IM_QUOTA_ESENTE_DET", "IM_DIARIA_NETTO_DET", "FL_DIARIA_MANUALE", "DETRAZIONE_ALTRI_NETTO", "DETRAZIONE_CONIUGE_NETTO", "DETRAZIONE_FIGLI_NETTO", "DETRAZIONI_LA_NETTO", "DETRAZIONI_PERSONALI_NETTO") AS 
  (select
--
-- Date: 18/07/2006
-- Version: 1.6
--
-- Vista per la stampa della nota liquidazione di una missione
--
--
-- History:
--
-- Date: 17/10/02
-- Version: 1.0
-- Creazione
--
-- Date: 13/12/02
-- Version: 1.1
-- Estrazione IM_TOTALE_MISSIONE
-- Corretta outer join
--
-- Date: 18/12/02
-- Version: 1.2
-- Estrazione informazioni relative all'anticipo
-- Aggiunto fl_rimborso per gestione missioni con importi minori dell'anticipo
--
-- Date: 10/01/03
-- Version: 1.3
-- Estrazione della qualifica del beneficiario. Estrazione ti_provvisorio_definitivo.
-- Estrazione importi di spesa in divisa.
-- Estrazione dettagli di diaria, dei cori e detrazioni per prospetto di liquidazione.
--
-- Date: 14/01/03
-- Version: 1.4
-- Estrazione pg_reversale, e importo rimborso
--
-- Date: 26/02/03
-- Version: 1.5
-- Estrazione MISSIONE_DIARIA.IM_DIARIA
--
-- Date: 18/07/2006
-- Version: 1.6
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 m.CD_CDS				  -- testata missioni non associate a compenso
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'A' 		  			   -- ti_record_l1
,'A'		  			   -- ti_record_l2
,null					   -- fl_rimborso
,m.TI_PROVVISORIO_DEFINITIVO
,mriga.ESERCIZIO     	   -- es_finanziario
,uo.DS_UNITA_ORGANIZZATIVA -- ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,m.NOME
,m.COGNOME
,m.CD_TERZO
,ter.VIA_SEDE
,ter.NUMERO_CIVICO_SEDE
,ter.CAP_COMUNE_SEDE
,com.DS_COMUNE
,com.CD_PROVINCIA
,m.TI_ANAGRAFICO
,decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO)  -- matricola
,rif.DS_INQUADRAMENTO											  -- qualifica
,null 															  -- ti_competenza_residuo
,mr.ESERCIZIO_ORI_OBBLIGAZIONE
,mr.PG_OBBLIGAZIONE
,mr.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null) 				  -- obbs.DT_SCADENZA
,0							  -- comp.PG_COMPENSO
,mriga.PG_MANDATO
,0				 -- m.IM_TOTALE_MISSIONE
,0				 -- m.IM_DIARIA_LORDA
,0				 -- m.IM_QUOTA_ESENTE
,0				 -- m.IM_DIARIA_NETTO
,0				 -- m.IM_SPESE
,0				 -- ant.IM_ANTICIPO
,0				 -- rim.IM_RIMBORSO
,0				 -- comp.IM_CR_ENTE
,0				 -- m.IM_LORDO_PERCEPIENTE
,0				 -- m.IM_NETTO_PECEPIENTE
,null			 -- m.DS_MISSIONE
,to_date(null)	 -- m.DT_INIZIO_MISSIONE
,to_date(null)	 -- m.DT_FINE_MISSIONE
,null			 -- rif.DS_MODALITA_PAG
,null			 -- ban.INTESTAZIONE
,null			 -- ban.NUMERO_CONTO
,null			 -- ban.ABI
,null			 -- ban.CAB
,null			 -- abi.DS_ABICAB
,null			 -- abi.VIA	via_banca
,null			 -- abi.CAP	cap_banca
,null			 -- com1.DS_COMUNE ds_comune_banca
,null			 -- com1.CD_PROVINCIA cd_prov_banca
,null				-- obbv.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
	,missione_riga mr
  ,mandato_riga mriga
	,unita_organizzativa uo
	,terzo ter
	,comune com
	,rif_inquadramento rif
	,rapporto rap
	,anticipo ant
where m.FL_ASSOCIATO_COMPENSO 	      = 'N'
  and m.cd_cds = mr.cd_cds (+)
  and m.cd_unita_organizzativa = mr.cd_unita_organizzativa (+)
  and m.esercizio = mr.esercizio (+)
  and m.pg_missione = mr.pg_missione (+)
  and mriga.CD_CDS_DOC_AMM	  	  (+) = m.CD_CDS
  and mriga.CD_UO_DOC_AMM	  	  (+) = m.CD_UNITA_ORGANIZZATIVA
  and mriga.ESERCIZIO_DOC_AMM 	  (+) = m.ESERCIZIO
  and mriga.PG_DOC_AMM		  	  (+) = m.PG_MISSIONE
  and mriga.CD_TIPO_DOCUMENTO_AMM (+) = 'MISSIONE'
  and mriga.STATO				  (+) <> 'A'
  and uo.CD_UNITA_ORGANIZZATIVA	  	  = m.CD_UNITA_ORGANIZZATIVA
  and ter.CD_TERZO				  	  = m.CD_TERZO
  and com.PG_COMUNE				 	  = ter.PG_COMUNE_SEDE
  and rif.PG_RIF_INQUADRAMENTO		  = m.PG_RIF_INQUADRAMENTO
  and rap.CD_TIPO_RAPPORTO		  	  = m.CD_TIPO_RAPPORTO
  and rap.CD_ANAG				  	  = ter.CD_ANAG
  and ant.CD_CDS				   (+) = m.CD_CDS_ANTICIPO
  and ant.CD_UNITA_ORGANIZZATIVA   (+) = m.CD_UO_ANTICIPO
  and ant.ESERCIZIO				   (+) = m.ESERCIZIO_ANTICIPO
  and ant.PG_ANTICIPO			   (+) = m.PG_ANTICIPO
union all
select   				  -- testata missioni associate a compenso
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'A' 		  			   -- ti_record_l1
,'A'		  			   -- ti_record_l2
,null					   -- fl_rimborso
,m.TI_PROVVISORIO_DEFINITIVO
,mriga.ESERCIZIO     	   -- es_finanziario
,uo.DS_UNITA_ORGANIZZATIVA -- ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,m.NOME
,m.COGNOME
,m.CD_TERZO
,ter.VIA_SEDE
,ter.NUMERO_CIVICO_SEDE
,ter.CAP_COMUNE_SEDE
,com.DS_COMUNE
,com.CD_PROVINCIA
,m.TI_ANAGRAFICO
,decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO)  -- matricola
,rif.DS_INQUADRAMENTO											  -- qualifica
,null 															  -- ti_competenza_residuo
,criga.ESERCIZIO_ORI_OBBLIGAZIONE
,criga.PG_OBBLIGAZIONE
,criga.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null)					 -- obbs.DT_SCADENZA
,comp.PG_COMPENSO
,mriga.PG_MANDATO
,0				 -- m.IM_TOTALE_MISSIONE
,0				 -- m.IM_DIARIA_LORDA
,0				 -- m.IM_QUOTA_ESENTE
,0				 -- m.IM_DIARIA_NETTO
,0				 -- m.IM_SPESE
,0				 -- ant.IM_ANTICIPO
,0				 -- rim.IM_RIMBORSO
,0				 -- comp.IM_CR_ENTE
,0				 -- m.IM_LORDO_PERCEPIENTE
,0				 -- m.IM_NETTO_PECEPIENTE
,null			 -- m.DS_MISSIONE
,to_date(null) 	 -- m.DT_INIZIO_MISSIONE
,to_date(null) 	 -- m.DT_FINE_MISSIONE
,null			 -- rif.DS_MODALITA_PAG
,null			 -- ban.INTESTAZIONE
,null			 -- ban.NUMERO_CONTO
,null			 -- ban.ABI
,null			 -- ban.CAB
,null			 -- abi.DS_ABICAB
,null			 -- abi.VIA	via_banca
,null			 -- abi.CAP	cap_banca
,null			 -- com1.DS_COMUNE ds_comune_banca
,null			 -- com1.CD_PROVINCIA cd_prov_banca
,null			 -- obbv.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
 	,compenso comp
  ,compenso_riga criga
	,mandato_riga mriga
	,unita_organizzativa uo
	,terzo ter
	,comune com
	,rif_inquadramento rif
	,rapporto rap
	,anticipo ant
where m.FL_ASSOCIATO_COMPENSO 	  = 'Y'
  and comp.CD_CDS_MISSIONE		  = m.CD_CDS
  and comp.ESERCIZIO_MISSIONE	  = m.ESERCIZIO
  and comp.PG_MISSIONE			  = m.PG_MISSIONE
  and comp.CD_UO_MISSIONE		  = m.CD_UNITA_ORGANIZZATIVA
  and comp.STATO_COFI			  <> 'A'
  and comp.cd_cds = criga.cd_cds (+)
  and comp.cd_unita_organizzativa = criga.cd_unita_organizzativa (+)
  and comp.esercizio = criga.esercizio (+)
  and comp.pg_compenso = criga.pg_compenso (+)
  and mriga.CD_CDS_DOC_AMM	  	  (+) = comp.CD_CDS
  and mriga.CD_UO_DOC_AMM	  	  (+) = comp.CD_UNITA_ORGANIZZATIVA
  and mriga.ESERCIZIO_DOC_AMM 	  (+) = comp.ESERCIZIO
  and mriga.PG_DOC_AMM		  	  (+) = comp.PG_COMPENSO
  and mriga.CD_TIPO_DOCUMENTO_AMM (+) = 'COMPENSO'
  and mriga.STATO				  (+) <> 'A'
  and uo.CD_UNITA_ORGANIZZATIVA	  = m.CD_UNITA_ORGANIZZATIVA
  and ter.CD_TERZO				  = m.CD_TERZO
  and com.PG_COMUNE				  = ter.PG_COMUNE_SEDE
  and rif.PG_RIF_INQUADRAMENTO	  = m.PG_RIF_INQUADRAMENTO
  and rap.CD_TIPO_RAPPORTO		  = m.CD_TIPO_RAPPORTO
  and rap.CD_ANAG				  = ter.CD_ANAG
  and ant.CD_CDS				   (+) = m.CD_CDS_ANTICIPO
  and ant.CD_UNITA_ORGANIZZATIVA   (+) = m.CD_UO_ANTICIPO
  and ant.ESERCIZIO				   (+) = m.ESERCIZIO_ANTICIPO
  and ant.PG_ANTICIPO			   (+) = m.PG_ANTICIPO
union all
select distinct 		   -- capitoli missioni non associate a compenso, con anticipo minore della missione
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'A' 		  			   -- ti_record_l1
,'B2'		  			   -- ti_record_l2
,'N'					   -- anticipo < missione (no rimborso)
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0					   	   -- decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO) matricola
,null					   -- rif.DS_INQUADRAMENTO
,decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R') -- ti_competenza_residuo
,mr.ESERCIZIO_ORI_OBBLIGAZIONE
,mr.PG_OBBLIGAZIONE
,mr.PG_OBBLIGAZIONE_SCADENZARIO
,obbs.DT_SCADENZA
,0						   -- comp.PG_COMPENSO
,mriga.PG_MANDATO
,mr.IM_TOTALE_RIGA_MISSIONE
,round(m.IM_DIARIA_LORDA*mr.IM_TOTALE_RIGA_MISSIONE/m.IM_TOTALE_MISSIONE,2) IM_DIARIA_LORDA
,round(m.IM_QUOTA_ESENTE*mr.IM_TOTALE_RIGA_MISSIONE/m.IM_TOTALE_MISSIONE,2) IM_QUOTA_ESENTE
,round(m.IM_DIARIA_NETTO*mr.IM_TOTALE_RIGA_MISSIONE/m.IM_TOTALE_MISSIONE,2) IM_DIARIA_NETTO
,round(m.IM_SPESE*mr.IM_TOTALE_RIGA_MISSIONE/m.IM_TOTALE_MISSIONE,2) IM_SPESE
,ant.IM_ANTICIPO
,0				   	  	   -- rim.IM_RIMBORSO
,0				  		   -- comp.IM_CR_ENTE
,round(m.IM_LORDO_PERCEPIENTE*mr.IM_TOTALE_RIGA_MISSIONE/m.IM_TOTALE_MISSIONE,2) IM_LORDO_PERCEPIENTE
,round(m.IM_NETTO_PECEPIENTE*mr.IM_TOTALE_RIGA_MISSIONE/m.IM_TOTALE_MISSIONE,2) IM_NETTO_PERCEPIENTE
,m.DS_MISSIONE
,m.DT_INIZIO_MISSIONE
,m.DT_FINE_MISSIONE
,rif.DS_MODALITA_PAG
,ban.INTESTAZIONE
,ban.NUMERO_CONTO
,ban.ABI
,ban.CAB
,abi.DS_ABICAB
,abi.VIA via_banca
,abi.CAP cap_banca
,com.DS_COMUNE ds_comune_banca
,com.CD_PROVINCIA cd_prov_banca
,obbv.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
  ,missione_riga mr
	,obbligazione obb
	,obbligazione_scadenzario obbs
	,mandato_riga mriga
	,anticipo ant
	,rif_modalita_pagamento rif
	,banca ban
	,abicab abi
	,comune com
	,obbligazione_scad_voce obbv
where m.FL_ASSOCIATO_COMPENSO 	  	   = 'N'
  and m.cd_cds = mr.cd_cds
  and m.cd_unita_organizzativa = mr.cd_unita_organizzativa
  and m.esercizio = mr.esercizio
  and m.pg_missione = mr.pg_missione
  and obb.CD_CDS					   = mr.CD_CDS_OBBLIGAZIONE
  and obb.ESERCIZIO					   = mr.ESERCIZIO_OBBLIGAZIONE
  and obb.ESERCIZIO_ORIGINALE			   = mr.ESERCIZIO_ORI_OBBLIGAZIONE
  and obb.PG_OBBLIGAZIONE			   = mr.PG_OBBLIGAZIONE
  and obbs.CD_CDS				  	   = mr.CD_CDS_OBBLIGAZIONE
  and obbs.ESERCIZIO			  	   = mr.ESERCIZIO_OBBLIGAZIONE
  and obbs.ESERCIZIO_ORIGINALE			   = mr.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbs.PG_OBBLIGAZIONE		  	   = mr.PG_OBBLIGAZIONE
  and obbs.PG_OBBLIGAZIONE_SCADENZARIO = mr.PG_OBBLIGAZIONE_SCADENZARIO
  and mriga.CD_CDS_DOC_AMM	  	  	   (+) = m.CD_CDS
  and mriga.CD_UO_DOC_AMM	  	  	   (+) = m.CD_UNITA_ORGANIZZATIVA
  and mriga.ESERCIZIO_DOC_AMM 	  	   (+) = m.ESERCIZIO
  and mriga.PG_DOC_AMM		  	  	   (+) = m.PG_MISSIONE
  and mriga.CD_TIPO_DOCUMENTO_AMM 	   (+) = 'MISSIONE'
  and mriga.STATO				  	   (+) <> 'A'
  and ant.CD_CDS				   (+) = m.CD_CDS_ANTICIPO
  and ant.CD_UNITA_ORGANIZZATIVA   (+) = m.CD_UO_ANTICIPO
  and ant.ESERCIZIO				   (+) = m.ESERCIZIO_ANTICIPO
  and ant.PG_ANTICIPO			   (+) = m.PG_ANTICIPO
  and rif.CD_MODALITA_PAG		   	   = m.CD_MODALITA_PAG
  and ban.CD_TERZO				   	   = m.CD_TERZO
  and ban.PG_BANCA				   	   = m.PG_BANCA
  and abi.ABI					   (+) = ban.ABI
  and abi.CAB					   (+) = ban.CAB
  and com.PG_COMUNE			   	   (+) = abi.PG_COMUNE
  and obbv.CD_CDS				  	   = mr.CD_CDS_OBBLIGAZIONE
  and obbv.ESERCIZIO				   = mr.ESERCIZIO_OBBLIGAZIONE
  and obbv.ESERCIZIO_ORIGINALE			   = mr.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbv.PG_OBBLIGAZIONE		  	   = mr.PG_OBBLIGAZIONE
  and obbv.PG_OBBLIGAZIONE_SCADENZARIO = mr.PG_OBBLIGAZIONE_SCADENZARIO
union all
select distinct 		   -- capitoli missioni non associate a compenso, con anticipo maggiore della missione
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'A' 		  			   -- ti_record_l1
,'B2'		  			   -- ti_record_l2
,'Y'					   -- anticipo > missione (rimborso)
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0					   	   -- decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO) matricola
,null					   -- rif.DS_INQUADRAMENTO
,decode(acc.ESERCIZIO_ORI_RIPORTO,null,'C','R') -- ti_competenza_residuo
,rim.ESERCIZIO_ORI_ACCERTAMENTO
,rim.PG_ACCERTAMENTO
,rim.PG_ACCERTAMENTO_SCADENZARIO
,accs.DT_SCADENZA_INCASSO
,0						   -- comp.PG_COMPENSO
,rriga.PG_REVERSALE
,m.IM_TOTALE_MISSIONE
,m.IM_DIARIA_LORDA
,m.IM_QUOTA_ESENTE
,m.IM_DIARIA_NETTO
,m.IM_SPESE
,ant.IM_ANTICIPO
,rim.IM_RIMBORSO
,0				  		   -- comp.IM_CR_ENTE
,m.IM_LORDO_PERCEPIENTE
,m.IM_NETTO_PECEPIENTE
,m.DS_MISSIONE
,m.DT_INIZIO_MISSIONE
,m.DT_FINE_MISSIONE
,rif.DS_MODALITA_PAG
,ban.INTESTAZIONE
,ban.NUMERO_CONTO
,ban.ABI
,ban.CAB
,abi.DS_ABICAB
,abi.VIA via_banca
,abi.CAP cap_banca
,com.DS_COMUNE ds_comune_banca
,com.CD_PROVINCIA cd_prov_banca
,acc.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
	,anticipo ant
	,rimborso rim
	,accertamento acc
	,accertamento_scadenzario accs
	,reversale_riga rriga
	,rif_modalita_pagamento rif
	,banca ban
	,abicab abi
	,comune com
where m.FL_ASSOCIATO_COMPENSO 	  	   = 'N'
  and not exists(select '1' from missione_riga mr
                 where m.cd_cds = mr.cd_cds
                 and   m.cd_unita_organizzativa = mr.cd_unita_organizzativa
                 and   m.esercizio = mr.esercizio
                 and   m.pg_missione = mr.pg_missione)
  and ant.CD_CDS				   (+) = m.CD_CDS_ANTICIPO
  and ant.CD_UNITA_ORGANIZZATIVA   (+) = m.CD_UO_ANTICIPO
  and ant.ESERCIZIO				   (+) = m.ESERCIZIO_ANTICIPO
  and ant.PG_ANTICIPO			   (+) = m.PG_ANTICIPO
  and rim.CD_CDS_ANTICIPO		   (+) = ant.CD_CDS
  and rim.CD_UO_ANTICIPO		   (+) = ant.CD_UNITA_ORGANIZZATIVA
  and rim.ESERCIZIO_ANTICIPO	   (+) = ant.ESERCIZIO
  and rim.PG_ANTICIPO			   (+) = ant.PG_ANTICIPO
  and acc.CD_CDS				   (+) = rim.CD_CDS_ACCERTAMENTO
  and acc.ESERCIZIO				   (+) = rim.ESERCIZIO
  and acc.ESERCIZIO_ORIGINALE			   (+) = rim.ESERCIZIO_ORI_ACCERTAMENTO
  and acc.PG_ACCERTAMENTO		   (+) = rim.PG_ACCERTAMENTO
  and accs.CD_CDS 				   (+) = rim.CD_CDS_ACCERTAMENTO
  and accs.ESERCIZIO			   (+) = rim.ESERCIZIO
  and accs.ESERCIZIO_ORIGINALE			   (+) = rim.ESERCIZIO_ORI_ACCERTAMENTO
  and accs.PG_ACCERTAMENTO		   (+) = rim.PG_ACCERTAMENTO
  and accs.PG_ACCERTAMENTO_SCADENZARIO (+) = rim.PG_ACCERTAMENTO_SCADENZARIO
  and rriga.CD_CDS_DOC_AMM	  	  	   (+) = rim.CD_CDS
  and rriga.CD_UO_DOC_AMM	  	  	   (+) = rim.CD_UNITA_ORGANIZZATIVA
  and rriga.ESERCIZIO_DOC_AMM 	  	   (+) = rim.ESERCIZIO
  and rriga.PG_DOC_AMM		  	  	   (+) = rim.PG_RIMBORSO
  and rriga.CD_TIPO_DOCUMENTO_AMM 	   (+) = 'RIMBORSO'
  and rriga.STATO				  	   (+) <> 'A'
  and rif.CD_MODALITA_PAG		   	   = m.CD_MODALITA_PAG
  and ban.CD_TERZO				   	   = m.CD_TERZO
  and ban.PG_BANCA				   	   = m.PG_BANCA
  and abi.ABI					   (+) = ban.ABI
  and abi.CAB					   (+) = ban.CAB
  and com.PG_COMUNE			   	   (+) = abi.PG_COMUNE
union all
select distinct 		   -- capitoli missioni associate a compenso, con anticipo minore della missione
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'A' 		  			   -- ti_record_l1
,'B2'		  			   -- ti_record_l2
,'N'					   -- anticipo < missione (no rimborso)
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0					   	   -- decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO) matricola
,null					   -- rif.DS_INQUADRAMENTO
,decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R') -- ti_competenza_residuo
,criga.ESERCIZIO_ORI_OBBLIGAZIONE
,criga.PG_OBBLIGAZIONE
,criga.PG_OBBLIGAZIONE_SCADENZARIO
,obbs.DT_SCADENZA
,comp.PG_COMPENSO
,mriga.PG_MANDATO
,ROUND(m.IM_TOTALE_MISSIONE*criga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2) IM_TOTALE_MISSIONE
,ROUND(m.IM_DIARIA_LORDA*criga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2) IM_DIARIA_LORDA
,ROUND(m.IM_QUOTA_ESENTE*criga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2) IM_QUOTA_ESENTE
,ROUND(m.IM_DIARIA_NETTO*criga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2) IM_DIARIA_NETTO
,ROUND(m.IM_SPESE*criga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2) IM_SPESE
,ant.IM_ANTICIPO
,0				   	  	   -- rim.IM_RIMBORSO
,ROUND(comp.IM_CR_ENTE*criga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2) IM_CR_ENTE
,ROUND(m.IM_LORDO_PERCEPIENTE*criga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2) IM_LORDO_PERCEPIENTE
,ROUND(m.IM_NETTO_PECEPIENTE*criga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2) IM_NETTO_PECEPIENTE
,m.DS_MISSIONE
,m.DT_INIZIO_MISSIONE
,m.DT_FINE_MISSIONE
,rif.DS_MODALITA_PAG
,ban.INTESTAZIONE
,ban.NUMERO_CONTO
,ban.ABI
,ban.CAB
,abi.DS_ABICAB
,abi.VIA via_banca
,abi.CAP cap_banca
,com.DS_COMUNE ds_comune_banca
,com.CD_PROVINCIA cd_prov_banca
,obbv.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
 	,compenso comp
  ,compenso_riga criga
	,obbligazione obb
	,obbligazione_scadenzario obbs
	,mandato_riga mriga
	,anticipo ant
	,rif_modalita_pagamento rif
	,banca ban
	,abicab abi
	,comune com
	,obbligazione_scad_voce obbv
where m.FL_ASSOCIATO_COMPENSO 	  	   = 'Y'
  and comp.CD_CDS_MISSIONE		  	   = m.CD_CDS
  and comp.ESERCIZIO_MISSIONE	  	   = m.ESERCIZIO
  and comp.PG_MISSIONE			  	   = m.PG_MISSIONE
  and comp.CD_UO_MISSIONE		  	   = m.CD_UNITA_ORGANIZZATIVA
  and comp.STATO_COFI			  	   <> 'A'
  and comp.CD_CDS = criga.CD_CDS
  and comp.CD_UNITA_ORGANIZZATIVA = criga.CD_UNITA_ORGANIZZATIVA
  and comp.ESERCIZIO = criga.ESERCIZIO
  and comp.PG_COMPENSO = criga.PG_COMPENSO
  and obb.CD_CDS					   = criga.CD_CDS_OBBLIGAZIONE
  and obb.ESERCIZIO					   = criga.ESERCIZIO_OBBLIGAZIONE
  and obb.ESERCIZIO_ORIGINALE			= criga.ESERCIZIO_ORI_OBBLIGAZIONE
  and obb.PG_OBBLIGAZIONE			   = criga.PG_OBBLIGAZIONE
  and obbs.CD_CDS				  	   = criga.CD_CDS_OBBLIGAZIONE
  and obbs.ESERCIZIO			  	   = criga.ESERCIZIO_OBBLIGAZIONE
  and obbs.ESERCIZIO_ORIGINALE			= criga.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbs.PG_OBBLIGAZIONE		  	   = criga.PG_OBBLIGAZIONE
  and obbs.PG_OBBLIGAZIONE_SCADENZARIO = criga.PG_OBBLIGAZIONE_SCADENZARIO
  and mriga.CD_CDS_DOC_AMM	  	  	   (+) = comp.CD_CDS
  and mriga.CD_UO_DOC_AMM	  	  	   (+) = comp.CD_UNITA_ORGANIZZATIVA
  and mriga.ESERCIZIO_DOC_AMM 	  	   (+) = comp.ESERCIZIO
  and mriga.PG_DOC_AMM		  	  	   (+) = comp.PG_COMPENSO
  and mriga.CD_TIPO_DOCUMENTO_AMM 	   (+) = 'COMPENSO'
  and mriga.STATO				  	   (+) <> 'A'
  and ant.CD_CDS				   (+) = m.CD_CDS_ANTICIPO
  and ant.CD_UNITA_ORGANIZZATIVA   (+) = m.CD_UO_ANTICIPO
  and ant.ESERCIZIO				   (+) = m.ESERCIZIO_ANTICIPO
  and ant.PG_ANTICIPO			   (+) = m.PG_ANTICIPO
  and rif.CD_MODALITA_PAG		   	   = m.CD_MODALITA_PAG
  and ban.CD_TERZO				   	   = m.CD_TERZO
  and ban.PG_BANCA				   	   = m.PG_BANCA
  and abi.ABI					   (+) = ban.ABI
  and abi.CAB					   (+) = ban.CAB
  and com.PG_COMUNE			   	   (+) = abi.PG_COMUNE
  and obbv.CD_CDS				  	   = criga.CD_CDS_OBBLIGAZIONE
  and obbv.ESERCIZIO				   = criga.ESERCIZIO_OBBLIGAZIONE
  and obbv.ESERCIZIO_ORIGINALE			= criga.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbv.PG_OBBLIGAZIONE		  	   = criga.PG_OBBLIGAZIONE
  and obbv.PG_OBBLIGAZIONE_SCADENZARIO = criga.PG_OBBLIGAZIONE_SCADENZARIO
union all
select distinct 		   -- capitoli missioni associate a compenso, con anticipo maggiore della missione
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'A' 		  			   -- ti_record_l1
,'B2'		  			   -- ti_record_l2
,'Y'					   -- anticipo < missione (no rimborso)
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0					   	   -- decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO) matricola
,null					   -- rif.DS_INQUADRAMENTO
,decode(acc.ESERCIZIO_ORI_RIPORTO,null,'C','R') -- ti_competenza_residuo
,rim.ESERCIZIO_ORI_ACCERTAMENTO
,rim.PG_ACCERTAMENTO
,rim.PG_ACCERTAMENTO_SCADENZARIO
,accs.DT_SCADENZA_INCASSO
,comp.PG_COMPENSO
,rriga.PG_REVERSALE
,m.IM_TOTALE_MISSIONE
,m.IM_DIARIA_LORDA
,m.IM_QUOTA_ESENTE
,m.IM_DIARIA_NETTO
,m.IM_SPESE
,ant.IM_ANTICIPO
,rim.IM_RIMBORSO
,comp.IM_CR_ENTE
,m.IM_LORDO_PERCEPIENTE
,m.IM_NETTO_PECEPIENTE
,m.DS_MISSIONE
,m.DT_INIZIO_MISSIONE
,m.DT_FINE_MISSIONE
,rif.DS_MODALITA_PAG
,ban.INTESTAZIONE
,ban.NUMERO_CONTO
,ban.ABI
,ban.CAB
,abi.DS_ABICAB
,abi.VIA via_banca
,abi.CAP cap_banca
,com.DS_COMUNE ds_comune_banca
,com.CD_PROVINCIA cd_prov_banca
,acc.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
 	,compenso comp
	,anticipo ant
	,rimborso rim
	,accertamento acc
	,accertamento_scadenzario accs
	,reversale_riga rriga
	,rif_modalita_pagamento rif
	,banca ban
	,abicab abi
	,comune com
where m.FL_ASSOCIATO_COMPENSO 	  	   = 'Y'
  and comp.CD_CDS_MISSIONE		  	   = m.CD_CDS
  and comp.ESERCIZIO_MISSIONE	  	   = m.ESERCIZIO
  and comp.PG_MISSIONE			  	   = m.PG_MISSIONE
  and comp.CD_UO_MISSIONE		  	   = m.CD_UNITA_ORGANIZZATIVA
  and comp.STATO_COFI			  	   <> 'A'
  and not exists(select '1' from compenso_riga cr
                 where comp.cd_cds = cr.cd_cds
                 and   comp.cd_unita_organizzativa = cr.cd_unita_organizzativa
                 and   comp.esercizio = cr.esercizio
                 and   comp.pg_compenso = cr.pg_compenso)
  and ant.CD_CDS				   (+) = m.CD_CDS_ANTICIPO
  and ant.CD_UNITA_ORGANIZZATIVA   (+) = m.CD_UO_ANTICIPO
  and ant.ESERCIZIO				   (+) = m.ESERCIZIO_ANTICIPO
  and ant.PG_ANTICIPO			   (+) = m.PG_ANTICIPO
  and rim.CD_CDS_ANTICIPO		   (+) = ant.CD_CDS
  and rim.CD_UO_ANTICIPO		   (+) = ant.CD_UNITA_ORGANIZZATIVA
  and rim.ESERCIZIO_ANTICIPO	   (+) = ant.ESERCIZIO
  and rim.PG_ANTICIPO			   (+) = ant.PG_ANTICIPO
  and acc.CD_CDS				   (+) = rim.CD_CDS_ACCERTAMENTO
  and acc.ESERCIZIO				   (+) = rim.ESERCIZIO
  and acc.ESERCIZIO_ORIGINALE			   (+) = rim.ESERCIZIO_ORI_ACCERTAMENTO
  and acc.PG_ACCERTAMENTO		   (+) = rim.PG_ACCERTAMENTO
  and accs.CD_CDS 				   (+) = rim.CD_CDS_ACCERTAMENTO
  and accs.ESERCIZIO			   (+) = rim.ESERCIZIO
  and accs.ESERCIZIO_ORIGINALE			   (+) = rim.ESERCIZIO_ORI_ACCERTAMENTO
  and accs.PG_ACCERTAMENTO		   (+) = rim.PG_ACCERTAMENTO
  and accs.PG_ACCERTAMENTO_SCADENZARIO (+) = rim.PG_ACCERTAMENTO_SCADENZARIO
  and rriga.CD_CDS_DOC_AMM	  	  	   (+) = rim.CD_CDS
  and rriga.CD_UO_DOC_AMM	  	  	   (+) = rim.CD_UNITA_ORGANIZZATIVA
  and rriga.ESERCIZIO_DOC_AMM 	  	   (+) = rim.ESERCIZIO
  and rriga.PG_DOC_AMM		  	  	   (+) = rim.PG_RIMBORSO
  and rriga.CD_TIPO_DOCUMENTO_AMM 	   (+) = 'RIMBORSO'
  and rriga.STATO				  	   (+) <> 'A'
  and rif.CD_MODALITA_PAG		   	   = m.CD_MODALITA_PAG
  and ban.CD_TERZO				   	   = m.CD_TERZO
  and ban.PG_BANCA				   	   = m.PG_BANCA
  and abi.ABI					   (+) = ban.ABI
  and abi.CAB					   (+) = ban.CAB
  and com.PG_COMUNE			   	   (+) = abi.PG_COMUNE
union all
select distinct 		   -- CORI del compenso per missioni associate a compenso
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'A' 		  			   -- ti_record_l1
,'C'		  			   -- ti_record_l2
,null					   -- fl_rimborso
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0					   	   -- decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO) matricola
,null					   -- decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R') ti_competenza_residuo
,null					   -- rif.DS_INQUADRAMENTO
,criga.ESERCIZIO_ORI_OBBLIGAZIONE
,criga.PG_OBBLIGAZIONE
,criga.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null)			   -- obbs.DT_SCADENZA
,comp.PG_COMPENSO
,0				 		   -- mriga.PG_MANDATO
,0				 		   -- m.IM_TOTALE_MISSIONE
,0				 		   -- m.IM_DIARIA_LORDA
,0				 		   -- m.IM_QUOTA_ESENTE
,0				 		   -- m.IM_DIARIA_NETTO
,0				 		   -- m.IM_SPESE
,0				 		   -- ant.IM_ANTICIPO
,0				 		   -- rim.IM_RIMBORSO
,0				 		   -- comp.IM_CR_ENTE
,0				 		   -- m.IM_LORDO_PERCEPIENTE
,0				 		   -- m.IM_NETTO_PECEPIENTE
,m.DS_MISSIONE
,m.DT_INIZIO_MISSIONE
,m.DT_FINE_MISSIONE
,rif.DS_MODALITA_PAG
,ban.INTESTAZIONE
,ban.NUMERO_CONTO
,ban.ABI
,ban.CAB
,abi.DS_ABICAB
,abi.VIA via_banca
,abi.CAP cap_banca
,com.DS_COMUNE ds_comune_banca
,com.CD_PROVINCIA cd_prov_banca
,null					   -- obbv.CD_VOCE
,cori.CD_CONTRIBUTO_RITENUTA
,cori.TI_ENTE_PERCIPIENTE
,cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
 	,compenso comp
  ,compenso_riga criga
	,rif_modalita_pagamento rif
	,banca ban
	,abicab abi
	,comune com
	,contributo_ritenuta cori
where m.FL_ASSOCIATO_COMPENSO 	  	   = 'Y'
  and comp.CD_CDS_MISSIONE		  	   = m.CD_CDS
  and comp.ESERCIZIO_MISSIONE	  	   = m.ESERCIZIO
  and comp.PG_MISSIONE			  	   = m.PG_MISSIONE
  and comp.CD_UO_MISSIONE		  	   = m.CD_UNITA_ORGANIZZATIVA
  and comp.STATO_COFI			  	   <> 'A'
  and comp.cd_cds = criga.cd_cds (+)
  and comp.cd_unita_organizzativa = criga.cd_unita_organizzativa (+)
  and comp.esercizio = criga.esercizio (+)
  and comp.pg_compenso = criga.pg_compenso (+)
  and rif.CD_MODALITA_PAG		   	   = m.CD_MODALITA_PAG
  and ban.CD_TERZO				   	   = m.CD_TERZO
  and ban.PG_BANCA				   	   = m.PG_BANCA
  and abi.ABI					   (+) = ban.ABI
  and abi.CAB					   (+) = ban.CAB
  and com.PG_COMUNE			   	   (+) = abi.PG_COMUNE
  and cori.CD_CDS					   = comp.CD_CDS
  and cori.CD_UNITA_ORGANIZZATIVA	   = comp.CD_UNITA_ORGANIZZATIVA
  and cori.ESERCIZIO				   = comp.ESERCIZIO
  and cori.PG_COMPENSO				   = comp.PG_COMPENSO
union all
select	          -- estrazione dettagli di spesa della missione (non rimborso KM)
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'B' 		  			   -- ti_record_l1
,'A'		  			   -- ti_record_l2
,null					   -- fl_rimborso
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,null					   -- m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0						   -- matricola
,null					   -- rif.DS_INQUADRAMENTO
,null 					   -- ti_competenza_residuo
,0                                                 -- m.ESERCIZIO_ORI_OBBLIGAZIONE
,0						   -- m.PG_OBBLIGAZIONE
,0						   -- m.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null) 			   -- obbs.DT_SCADENZA
,0						   -- comp.PG_COMPENSO
,0						   --mriga.PG_MANDATO
,0				 -- m.IM_TOTALE_MISSIONE
,0				 -- m.IM_DIARIA_LORDA
,0				 -- m.IM_QUOTA_ESENTE
,0				 -- m.IM_DIARIA_NETTO
,m.IM_SPESE
,0				 -- ant.IM_ANTICIPO
,0				 -- rim.IM_RIMBORSO
,0				 -- comp.IM_CR_ENTE
,0				 -- m.IM_LORDO_PERCEPIENTE
,0				 -- m.IM_NETTO_PECEPIENTE
,null			 -- m.DS_MISSIONE
,to_date(null)	 -- m.DT_INIZIO_MISSIONE
,to_date(null)	 -- m.DT_FINE_MISSIONE
,null			 -- rif.DS_MODALITA_PAG
,null			 -- ban.INTESTAZIONE
,null			 -- ban.NUMERO_CONTO
,null			 -- ban.ABI
,null			 -- ban.CAB
,null			 -- abi.DS_ABICAB
,null			 -- abi.VIA	via_banca
,null			 -- abi.CAP	cap_banca
,null			 -- com1.DS_COMUNE ds_comune_banca
,null			 -- com1.CD_PROVINCIA cd_prov_banca
,null				-- obbv.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,mdet.PG_RIGA
,mdet.DT_INIZIO_TAPPA
,mdet.DS_SPESA
,mdet.FL_SPESA_ANTICIPATA
,mdet.CD_DIVISA_SPESA
,mdet.IM_SPESA_DIVISA
,mdet.CAMBIO_SPESA
,mdet.IM_BASE_MAGGIORAZIONE
,mdet.PERCENTUALE_MAGGIORAZIONE
,mdet.IM_MAGGIORAZIONE
,mdet.IM_SPESA_EURO
,mdet.IM_TOTALE_SPESA
,mdet.TI_AUTO
,mdet.CHILOMETRI
,mdet.INDENNITA_CHILOMETRICA
,m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
	,missione_dettaglio mdet
where mdet.CD_CDS			  	  = m.CD_CDS
  and mdet.CD_UNITA_ORGANIZZATIVA = m.CD_UNITA_ORGANIZZATIVA
  and mdet.ESERCIZIO			  = m.ESERCIZIO
  and mdet.PG_MISSIONE			  = m.PG_MISSIONE
  and mdet.TI_SPESA_DIARIA		  = 'S'
  and mdet.TI_CD_TI_SPESA		  in ('A','N','P','T')
union all
select	          -- estrazione dettagli di spesa della missione (rimborso KM)
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'B' 		  			   -- ti_record_l1
,'B'		  			   -- ti_record_l2
,null					   -- fl_rimborso
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,null					   -- m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0						   -- matricola
,null					   -- rif.DS_INQUADRAMENTO
,null 					   -- ti_competenza_residuo
,0                                                 -- m.ESERCIZIO_ORI_OBBLIGAZIONE
,0						   -- m.PG_OBBLIGAZIONE
,0						   -- m.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null) 			   -- obbs.DT_SCADENZA
,0						   -- comp.PG_COMPENSO
,0						   --mriga.PG_MANDATO
,0				 -- m.IM_TOTALE_MISSIONE
,0				 -- m.IM_DIARIA_LORDA
,0				 -- m.IM_QUOTA_ESENTE
,0				 -- m.IM_DIARIA_NETTO
,m.IM_SPESE
,0				 -- ant.IM_ANTICIPO
,0				 -- rim.IM_RIMBORSO
,0				 -- comp.IM_CR_ENTE
,0				 -- m.IM_LORDO_PERCEPIENTE
,0				 -- m.IM_NETTO_PECEPIENTE
,null			 -- m.DS_MISSIONE
,to_date(null)	 -- m.DT_INIZIO_MISSIONE
,to_date(null)	 -- m.DT_FINE_MISSIONE
,null			 -- rif.DS_MODALITA_PAG
,null			 -- ban.INTESTAZIONE
,null			 -- ban.NUMERO_CONTO
,null			 -- ban.ABI
,null			 -- ban.CAB
,null			 -- abi.DS_ABICAB
,null			 -- abi.VIA	via_banca
,null			 -- abi.CAP	cap_banca
,null			 -- com1.DS_COMUNE ds_comune_banca
,null			 -- com1.CD_PROVINCIA cd_prov_banca
,null				-- obbv.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,mdet.PG_RIGA
,mdet.DT_INIZIO_TAPPA
,mdet.DS_SPESA
,mdet.FL_SPESA_ANTICIPATA
,mdet.CD_DIVISA_SPESA
,mdet.IM_SPESA_DIVISA
,mdet.CAMBIO_SPESA
,mdet.IM_BASE_MAGGIORAZIONE
,mdet.PERCENTUALE_MAGGIORAZIONE
,mdet.IM_MAGGIORAZIONE
,mdet.IM_SPESA_EURO
,mdet.IM_TOTALE_SPESA
,mdet.TI_AUTO
,mdet.CHILOMETRI
,mdet.INDENNITA_CHILOMETRICA
,m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
	,missione_dettaglio mdet
where mdet.CD_CDS			  	  = m.CD_CDS
  and mdet.CD_UNITA_ORGANIZZATIVA = m.CD_UNITA_ORGANIZZATIVA
  and mdet.ESERCIZIO			  = m.ESERCIZIO
  and mdet.PG_MISSIONE			  = m.PG_MISSIONE
  and mdet.TI_SPESA_DIARIA		  = 'S'
  and mdet.TI_CD_TI_SPESA		  = 'R'
union all
select distinct 		   -- capitoli anticipo su missioni
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'A' 		  			   -- ti_record_l1
,'B1'		  			   -- ti_record_l2
,null					   -- fl_rimborso
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0					   	   -- decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO) matricola
,null					   -- rif.DS_INQUADRAMENTO
,decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R') -- ti_competenza_residuo
,ant.ESERCIZIO_ORI_OBBLIGAZIONE
,ant.PG_OBBLIGAZIONE
,ant.PG_OBBLIGAZIONE_SCADENZARIO
,obbs.DT_SCADENZA
,0						   -- comp.PG_COMPENSO
,mriga.PG_MANDATO
,0				  		   -- m.IM_TOTALE_MISSIONE
,0				  		   -- m.IM_DIARIA_LORDA
,0				 		   -- m.IM_QUOTA_ESENTE
,0				 		   -- m.IM_DIARIA_NETTO
,0				  		   -- m.IM_SPESE
,ant.IM_ANTICIPO
,0				 		   -- rim.IM_RIMBORSO
,0				  		   -- comp.IM_CR_ENTE
,0				  		   -- m.IM_LORDO_PERCEPIENTE
,0				  		   -- m.IM_NETTO_PECEPIENTE
,null			  		   -- m.DS_MISSIONE
,to_date(null)			   -- m.DT_INIZIO_MISSIONE
,to_date(null)			   -- m.DT_FINE_MISSIONE
,null					   -- rif.DS_MODALITA_PAG
,null					   -- ban.INTESTAZIONE
,null					   -- ban.NUMERO_CONTO
,null					   -- ban.ABI
,null					   -- ban.CAB
,null					   -- abi.DS_ABICAB
,null					   -- abi.VIA via_banca
,null					   -- abi.CAP cap_banca
,null					   -- com.DS_COMUNE ds_comune_banca
,null					   -- com.CD_PROVINCIA cd_prov_banca
,obbv.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
	,anticipo ant
	,obbligazione obb
	,obbligazione_scadenzario obbs
	,mandato_riga mriga
	,obbligazione_scad_voce obbv
where ant.CD_CDS				       = m.CD_CDS_ANTICIPO
  and ant.CD_UNITA_ORGANIZZATIVA       = m.CD_UO_ANTICIPO
  and ant.ESERCIZIO				       = m.ESERCIZIO_ANTICIPO
  and ant.PG_ANTICIPO			       = m.PG_ANTICIPO
  and obb.CD_CDS					   = ant.CD_CDS_OBBLIGAZIONE
  and obb.ESERCIZIO					   = ant.ESERCIZIO_OBBLIGAZIONE
  and obb.ESERCIZIO_ORIGINALE			= ant.ESERCIZIO_ORI_OBBLIGAZIONE
  and obb.PG_OBBLIGAZIONE			   = ant.PG_OBBLIGAZIONE
  and obbs.CD_CDS				  	   = ant.CD_CDS_OBBLIGAZIONE
  and obbs.ESERCIZIO			  	   = ant.ESERCIZIO_OBBLIGAZIONE
  and obbs.ESERCIZIO_ORIGINALE			= ant.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbs.PG_OBBLIGAZIONE		  	   = ant.PG_OBBLIGAZIONE
  and obbs.PG_OBBLIGAZIONE_SCADENZARIO = ant.PG_OBBLIGAZIONE_SCADENZARIO
  and mriga.CD_CDS_DOC_AMM	  	  	   (+) = m.CD_CDS
  and mriga.CD_UO_DOC_AMM	  	  	   (+) = m.CD_UNITA_ORGANIZZATIVA
  and mriga.ESERCIZIO_DOC_AMM 	  	   (+) = m.ESERCIZIO
  and mriga.PG_DOC_AMM		  	  	   (+) = m.PG_MISSIONE
  and mriga.CD_TIPO_DOCUMENTO_AMM 	   (+) = 'ANTICIPO'
  and mriga.STATO				  	   (+) <> 'A'
  and obbv.CD_CDS				  	   = ant.CD_CDS_OBBLIGAZIONE
  and obbv.ESERCIZIO				   = ant.ESERCIZIO_OBBLIGAZIONE
  and obbv.ESERCIZIO_ORIGINALE			= ant.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbv.PG_OBBLIGAZIONE		  	   = ant.PG_OBBLIGAZIONE
  and obbv.PG_OBBLIGAZIONE_SCADENZARIO = ant.PG_OBBLIGAZIONE_SCADENZARIO
union all
select	          -- estrazione dettagli di diaria
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'C' 		  			   -- ti_record_l1
,'A'		  			   -- ti_record_l2
,null					   -- fl_rimborso
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,null					   -- m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0						   -- matricola
,null					   -- rif.DS_INQUADRAMENTO
,null 					   -- ti_competenza_residuo
,0                                                 -- m.ESERCIZIO_ORI_OBBLIGAZIONE
,0						   -- m.PG_OBBLIGAZIONE
,0						   -- m.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null) 			   -- obbs.DT_SCADENZA
,0						   -- comp.PG_COMPENSO
,0						   --mriga.PG_MANDATO
,0				 -- m.IM_TOTALE_MISSIONE
,m.IM_DIARIA_LORDA
,m.IM_QUOTA_ESENTE
,m.IM_DIARIA_NETTO
,m.IM_SPESE
,0				 -- ant.IM_ANTICIPO
,0				 -- rim.IM_RIMBORSO
,0				 -- comp.IM_CR_ENTE
,0				 -- m.IM_LORDO_PERCEPIENTE
,0				 -- m.IM_NETTO_PECEPIENTE
,null			 -- m.DS_MISSIONE
,to_date(null)	 -- m.DT_INIZIO_MISSIONE
,to_date(null)	 -- m.DT_FINE_MISSIONE
,null			 -- rif.DS_MODALITA_PAG
,null			 -- ban.INTESTAZIONE
,null			 -- ban.NUMERO_CONTO
,null			 -- ban.ABI
,null			 -- ban.CAB
,null			 -- abi.DS_ABICAB
,null			 -- abi.VIA	via_banca
,null			 -- abi.CAP	cap_banca
,null			 -- com1.DS_COMUNE ds_comune_banca
,null			 -- com1.CD_PROVINCIA cd_prov_banca
,null				-- obbv.CD_VOCE
,null		 			   -- cori.CD_CONTRIBUTO_RITENUTA
,null					   -- cori.TI_ENTE_PERCIPIENTE
,0						   -- cori.AMMONTARE
,0						   -- cori.ALIQUOTA
,0						   -- cori.IMPONIBILE
,null					   -- tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,mtap.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,mtap.DT_FINE_TAPPA
,mtap.CD_DIVISA_TAPPA
,mtap.CAMBIO_TAPPA
,mdia.IM_DIARIA
,mdet.IM_DIARIA_LORDA
,mdet.IM_QUOTA_ESENTE
,mdet.IM_DIARIA_NETTO
,mdet.FL_DIARIA_MANUALE
,0						   -- comp.DETRAZIONE_ALTRI_NETTO
,0						   -- comp.DETRAZIONE_CONIUGE_NETTO
,0						   -- comp.DETRAZIONE_FIGLI_NETTO
,0						   -- comp.DETRAZIONI_LA_NETTO
,0						   -- comp.DETRAZIONI_PERSONALI_NETTO
from missione m
	,missione_tappa mtap
	,missione_dettaglio mdet
	,rif_inquadramento rif
	,missione_diaria mdia
where mtap.CD_CDS		   	      = m.CD_CDS
  and mtap.CD_UNITA_ORGANIZZATIVA = m.CD_UNITA_ORGANIZZATIVA
  and mtap.ESERCIZIO			  = m.ESERCIZIO
  and mtap.PG_MISSIONE			  = m.PG_MISSIONE
  and mdet.CD_CDS				  = mtap.CD_CDS
  and mdet.CD_UNITA_ORGANIZZATIVA = mtap.CD_UNITA_ORGANIZZATIVA
  and mdet.ESERCIZIO			  = mtap.ESERCIZIO
  and mdet.PG_MISSIONE			  = mtap.PG_MISSIONE
  and mdet.DT_INIZIO_TAPPA		  = mtap.DT_INIZIO_TAPPA
  and mdet.TI_SPESA_DIARIA		  = 'D'
  and rif.PG_RIF_INQUADRAMENTO	  = m.PG_RIF_INQUADRAMENTO
  and mdia.PG_NAZIONE			  = mtap.PG_NAZIONE
  and mdia.CD_GRUPPO_INQUADRAMENTO = rif.CD_GRUPPO_INQUADRAMENTO
  and mdia.DT_INIZIO_VALIDITA 	   <= m.DT_INIZIO_MISSIONE
  and mdia.DT_FINE_VALIDITA		   >= m.DT_INIZIO_MISSIONE
union all
select distinct 		   -- CORI del compenso per missioni associate a compenso (dettagli per prospetto)
 m.CD_CDS
,m.CD_UNITA_ORGANIZZATIVA
,m.ESERCIZIO
,m.PG_MISSIONE
,'D' 		  			   -- ti_record_l1
,'A'		  			   -- ti_record_l2
,null					   -- fl_rimborso
,m.TI_PROVVISORIO_DEFINITIVO
,0						   -- mriga.ESERCIZIO es_finanziario
,null					   -- uo.DS_UNITA_ORGANIZZATIVA ds_unita_organizzativa
,m.FL_ASSOCIATO_COMPENSO
,null					   -- m.NOME
,null					   -- m.COGNOME
,0					   	   -- m.CD_TERZO
,null					   -- ter.VIA_SEDE
,null					   -- ter.NUMERO_CIVICO_SEDE
,null					   -- ter.CAP_COMUNE_SEDE
,null					   -- com.DS_COMUNE
,null					   -- com.CD_PROVINCIA
,null					   -- m.TI_ANAGRAFICO
,0					   	   -- decode(m.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,m.CD_TERZO) matricola
,null					   -- decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R') ti_competenza_residuo
,null					   -- rif.DS_INQUADRAMENTO
,0                                                 -- comp.ESERCIZIO_ORI_OBBLIGAZIONE
,0						   -- comp.PG_OBBLIGAZIONE
,0						   -- comp.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null)			   -- obbs.DT_SCADENZA
,comp.PG_COMPENSO
,0				 		   -- mriga.PG_MANDATO
,0				 		   -- m.IM_TOTALE_MISSIONE
,0				 		   -- m.IM_DIARIA_LORDA
,0				 		   -- m.IM_QUOTA_ESENTE
,0				 		   -- m.IM_DIARIA_NETTO
,0				 		   -- m.IM_SPESE
,0				 		   -- ant.IM_ANTICIPO
,0				 		   -- rim.IM_RIMBORSO
,0				 		   -- comp.IM_CR_ENTE
,0				 		   -- m.IM_LORDO_PERCEPIENTE
,0				 		   -- m.IM_NETTO_PECEPIENTE
,null					   -- m.DS_MISSIONE
,to_date(null)			   -- m.DT_INIZIO_MISSIONE
,to_date(null)			   -- m.DT_FINE_MISSIONE
,null					   -- rif.DS_MODALITA_PAG
,null					   -- ban.INTESTAZIONE
,null					   -- ban.NUMERO_CONTO
,null					   -- ban.ABI
,null					   -- ban.CAB
,null					   -- abi.DS_ABICAB
,null					   -- abi.VIA via_banca
,null					   -- abi.CAP cap_banca
,null					   -- com.DS_COMUNE ds_comune_banca
,null					   -- com.CD_PROVINCIA cd_prov_banca
,null					   -- obbv.CD_VOCE
,cori.CD_CONTRIBUTO_RITENUTA
,cori.TI_ENTE_PERCIPIENTE
,cori.AMMONTARE
,cori.ALIQUOTA
,cori.IMPONIBILE
,tcr.DS_CONTRIBUTO_RITENUTA
,0						   -- mdet.PG_RIGA
,to_date(null) 			   -- mdet.DT_INIZIO_TAPPA
,null 					   -- mdet.DS_SPESA
,null 					   -- mdet.FL_SPESA_ANTICIPATA
,null					   -- mdet.CD_DIVISA_SPESA
,0						   -- mdet.IM_SPESA_DIVISA
,0						   -- mdet.CAMBIO_SPESA
,0						   -- mdet.IM_BASE_MAGGIORAZIONE
,0						   -- mdet.PERCENTUALE_MAGGIORAZIONE
,0						   -- mdet.IM_MAGGIORAZIONE
,0						   -- mdet.IM_SPESA_EURO
,0						   -- mdet.IM_TOTALE_SPESA
,null					   -- mdet.TI_AUTO
,0 						   -- mdet.CHILOMETRI
,0						   -- mdet.INDENNITA_CHILOMETRICA
,0						   -- m.IM_SPESE_ANTICIPATE
,to_date(null)			   -- mtap.DT_FINE_TAPPA
,null					   -- mtap.CD_DIVISA_TAPPA
,0						   -- mtap.CAMBIO_TAPPA
,0						   -- mdia.IM_DIARIA
,0						   -- mdet.IM_DIARIA_LORDA
,0						   -- mdet.IM_QUOTA_ESENTE
,0						   -- mdet.IM_DIARIA_NETTO
,null					   -- mdet.FL_DIARIA_MANUALE
,comp.DETRAZIONE_ALTRI_NETTO
,comp.DETRAZIONE_CONIUGE_NETTO
,comp.DETRAZIONE_FIGLI_NETTO
,comp.DETRAZIONI_LA_NETTO
,comp.DETRAZIONI_PERSONALI_NETTO
from missione m
 	,compenso comp
	,contributo_ritenuta cori
	,tipo_contributo_ritenuta tcr
where m.FL_ASSOCIATO_COMPENSO 	  	   = 'Y'
  and comp.CD_CDS_MISSIONE		  	   = m.CD_CDS
  and comp.ESERCIZIO_MISSIONE	  	   = m.ESERCIZIO
  and comp.PG_MISSIONE			  	   = m.PG_MISSIONE
  and comp.CD_UO_MISSIONE		  	   = m.CD_UNITA_ORGANIZZATIVA
  and comp.STATO_COFI			  	   <> 'A'
  and cori.CD_CDS					   = comp.CD_CDS
  and cori.CD_UNITA_ORGANIZZATIVA	   = comp.CD_UNITA_ORGANIZZATIVA
  and cori.ESERCIZIO				   = comp.ESERCIZIO
  and cori.PG_COMPENSO				   = comp.PG_COMPENSO
  and tcr.CD_CONTRIBUTO_RITENUTA	   = cori.CD_CONTRIBUTO_RITENUTA
  and tcr.DT_INI_VALIDITA			   = cori.DT_INI_VALIDITA
);

   COMMENT ON TABLE "VP_MISSIONE"  IS 'Vista per la stampa della nota liquidazione di una missione';
