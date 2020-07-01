--------------------------------------------------------
--  DDL for View VP_MINICARRIERA_BDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_MINICARRIERA_BDS" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_MINICARRIERA", "TI_RECORD_L1", "TI_RECORD_L2", "TI_RECORD_L3", "ESERCIZIO_BASE", "CODICE_FISCALE", "COGNOME", "NOME", "STATO", "DS_MINICARRIERA", "VIA_SEDE", "NUMERO_CIVICO_SEDE", "CAP_COMUNE_SEDE", "DS_COMUNE", "CD_PROVINCIA", "DT_INIZIO_MINICARRIERA", "DT_FINE_MINICARRIERA", "IM_TOTALE_MINICARRIERA", "FL_RINNOVO", "TI_ANTICIPO_POSTICIPO", "FL_TASSAZIONE_SEPARATA", "ALTRE_BORSE", "STATO_ASS_COMPENSO", "PG_COMPENSO", "ESERCIZIO_COMPENSO", "IM_NETTO_PERCIPIENTE", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DT_SCADENZA", "TI_COMPETENZA_RESIDUO", "CD_VOCE", "PG_RATA", "IM_RATA", "DETRAZIONE_ALTRI_NETTO", "DETRAZIONE_CONIUGE_NETTO", "DETRAZIONE_FIGLI_NETTO", "DETRAZIONI_LA_NETTO", "DETRAZIONI_PERSONALI_NETTO", "DETRAZIONE_RID_CUNEO_NETTO", "CD_CONTRIBUTO_RITENUTA", "AMMONTARE", "MAX_PG_RATA") AS
  (SELECT
--
-- Date: 18/07/2006
-- Version: 1.3
--
-- Vista per la stampa riepilogativa della borsa di studio
-- (minicarriera per borsa di studio)
--
-- History:
--
-- Date: 05/12/02
-- Version: 1.0
-- Creazione

-- Date: 11/12/02
-- Version: 1.1
-- Estrazione detrazioni in record distinti
-- Aggiunto campo max_pg_rata relativo allo stesso compenso
--
-- Date: 08/04/03
-- Version: 1.2
-- Corretta select relativa ai capitoli di un compenso (aggiunto distinct)
--
-- Date: 18/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 min.CD_CDS  -- testata della minicarriera
,min.CD_UNITA_ORGANIZZATIVA
,min.ESERCIZIO
,min.PG_MINICARRIERA
,'A' 				-- ti_record_l1
,'A'				-- ti_record_l2
,'A'				-- ti_record_l3
,esb.ESERCIZIO esercizio_base
,min.CODICE_FISCALE
,min.COGNOME
,min.NOME
,min.STATO
,min.DS_MINICARRIERA
,ter.VIA_SEDE
,ter.NUMERO_CIVICO_SEDE
,ter.CAP_COMUNE_SEDE
,com.DS_COMUNE
,com.CD_PROVINCIA
,min.DT_INIZIO_MINICARRIERA
,min.DT_FINE_MINICARRIERA
,min.IM_TOTALE_MINICARRIERA
,decode(min.DT_RINNOVO,null,'N','Y')  -- rinnovo
,min.TI_ANTICIPO_POSTICIPO
,min.FL_TASSAZIONE_SEPARATA
,calcolaAltreBorse(esb.ESERCIZIO,min.CD_TERZO,min.CD_CDS,min.CD_UNITA_ORGANIZZATIVA,min.ESERCIZIO,min.PG_MINICARRIERA)
,min.STATO_ASS_COMPENSO
,0			 	 -- cmin.PG_COMPENSO
,0			 	 -- cmin.ESERCIZIO_COMPENSO
,0				 -- com.IM_NETTO_PERCIPIENTE
,null			 -- com.CD_CDS_OBBLIGAZIONE
,0			 	 -- com.ESERCIZIO_OBBLIGAZIONE
,0			 	 -- com.ESERCIZIO_ORI_OBBLIGAZIONE
,0			 	 -- com.PG_OBBLIGAZIONE
,0			 	 -- com.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null)	 -- obbs.DT_SCADENZA
,null			 -- decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R')
,null			 -- obbv.CD_VOCE
,0			 		-- mrata.PG_RATA
,0			 		-- mrata.IM_RATA
,0			 		-- com.DETRAZIONE_ALTRI_NETTO
,0			 		-- com.DETRAZIONE_CONIUGE_NETTO
,0			 		-- com.DETRAZIONE_FIGLI_NETTO
,0			 		-- com.DETRAZIONI_LA_NETTO
,0			 		-- com.DETRAZIONI_PERSONALI_NETTO
,0
,null				-- crit.CD_CONTRIBUTO_RITENUTA
,0					-- crit.AMMONTARE
,0					-- max.MAX_PG_RATA
from minicarriera min
    ,esercizio_base esb
	,terzo ter
	,comune com
where esb.ESERCIZIO >= to_number(to_char(min.DT_INIZIO_MINICARRIERA,'YYYY'),'9999')
  and ter.CD_TERZO 	 = min.CD_TERZO
  and com.PG_COMUNE	 = ter.PG_COMUNE_SEDE
union all
select   -- dettagli (della testata) dei compensi associati alla minicarriera
 min.CD_CDS
,min.CD_UNITA_ORGANIZZATIVA
,min.ESERCIZIO
,min.PG_MINICARRIERA
,'A' 				-- ti_record_l1
,'B'				-- ti_record_l2
,'A'				-- ti_record_l3
,0					-- esb.ESERCIZIO esercizio_base
,null				-- min.CODICE_FISCALE
,null				-- min.COGNOME
,null				-- min.NOME
,null				-- min.STATO
,null				-- min.DS_MINICARRIERA
,null				-- ter.VIA_SEDE
,null				-- ter.NUMERO_CIVICO_SEDE
,null				-- ter.CAP_COMUNE_SEDE
,null				-- com.DS_COMUNE
,null				-- com.CD_PROVINCIA
,to_date(null)		-- min.DT_INIZIO_MINICARRIERA
,to_date(null)		-- min.DT_FINE_MINICARRIERA
,0					-- min.IM_TOTALE_MINICARRIERA
,null				-- decode(min.DT_RINNOVO,null,'N','Y')  -- rinnovo
,null				-- min.TI_ANTICIPO_POSTICIPO
,null				-- min.FL_TASSAZIONE_SEPARATA
,0					-- calcolaAltreBorse(esb.ESERCIZIO,min.CD_TERZO)
,null 				-- mrata.STATO_ASS_COMPENSO
,cmin.PG_COMPENSO
,cmin.ESERCIZIO_COMPENSO
,0				 -- com.IM_NETTO_PERCIPIENTE
,com.CD_CDS_OBBLIGAZIONE
,com.ESERCIZIO_OBBLIGAZIONE
,com.ESERCIZIO_ORI_OBBLIGAZIONE
,com.PG_OBBLIGAZIONE
,com.PG_OBBLIGAZIONE_SCADENZARIO
,obbs.DT_SCADENZA
,decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R')
,null			    -- obbv.CD_VOCE
,cmin.MAX_PG_RATA	-- mrata.PG_RATA
,0			 		-- mrata.IM_RATA
,0			 		-- com.DETRAZIONE_ALTRI_NETTO
,0			 		-- com.DETRAZIONE_CONIUGE_NETTO
,0			 		-- com.DETRAZIONE_FIGLI_NETTO
,0			 		-- com.DETRAZIONI_LA_NETTO
,0			 		-- com.DETRAZIONI_PERSONALI_NETTO
,0
,null				-- crit.CD_CONTRIBUTO_RITENUTA
,0					-- crit.AMMONTARE
,0					-- max.MAX_PG_RATA
from minicarriera min
	,(select distinct
 			 mrata.CD_CDS
			,mrata.CD_UNITA_ORGANIZZATIVA
			,mrata.ESERCIZIO
			,mrata.PG_MINICARRIERA
			,mrata.CD_CDS_COMPENSO
			,mrata.CD_UO_COMPENSO
			,mrata.ESERCIZIO_COMPENSO
			,mrata.PG_COMPENSO
			,max(mrata.PG_RATA) max_pg_rata
	 from minicarriera_rata mrata
	 group by mrata.CD_CDS
			,mrata.CD_UNITA_ORGANIZZATIVA
			,mrata.ESERCIZIO
			,mrata.PG_MINICARRIERA
			,mrata.CD_CDS_COMPENSO
			,mrata.CD_UO_COMPENSO
			,mrata.ESERCIZIO_COMPENSO
			,mrata.PG_COMPENSO) cmin
	,compenso com
	,obbligazione_scadenzario obbs
	,obbligazione obb
where cmin.CD_CDS 				  = min.CD_CDS
  and cmin.CD_UNITA_ORGANIZZATIVA = min.CD_UNITA_ORGANIZZATIVA
  and cmin.ESERCIZIO			  = min.ESERCIZIO
  and cmin.PG_MINICARRIERA		  = min.PG_MINICARRIERA
  and com.CD_CDS				  = cmin.CD_CDS_COMPENSO
  and com.CD_UNITA_ORGANIZZATIVA  = cmin.CD_UO_COMPENSO
  and com.ESERCIZIO				  = cmin.ESERCIZIO_COMPENSO
  and com.PG_COMPENSO			  = cmin.PG_COMPENSO
  and obbs.CD_CDS				  = com.CD_CDS_OBBLIGAZIONE
  and obbs.ESERCIZIO			  = com.ESERCIZIO_OBBLIGAZIONE
  and obbs.ESERCIZIO_ORIGINALE		  = com.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbs.PG_OBBLIGAZIONE		  = com.PG_OBBLIGAZIONE
  and obbs.PG_OBBLIGAZIONE_SCADENZARIO = com.PG_OBBLIGAZIONE_SCADENZARIO
  and obb.CD_CDS			  = com.CD_CDS
  and obb.ESERCIZIO			  = com.ESERCIZIO
  and obb.ESERCIZIO_ORIGINALE		  = com.ESERCIZIO_ORI_OBBLIGAZIONE
  and obb.PG_OBBLIGAZIONE		  = com.PG_OBBLIGAZIONE
union all
select distinct -- dettagli (dei compensi) dei capitoli legati alle obbligazioni
 min.CD_CDS
,min.CD_UNITA_ORGANIZZATIVA
,min.ESERCIZIO
,min.PG_MINICARRIERA
,'A' 				-- ti_record_l1
,'B'				-- ti_record_l2
,'B'				-- ti_record_l3
,0					-- esb.ESERCIZIO esercizio_base
,null				-- min.CODICE_FISCALE
,null				-- min.COGNOME
,null				-- min.NOME
,null				-- min.STATO
,null				-- min.DS_MINICARRIERA
,null				-- ter.VIA_SEDE
,null				-- ter.NUMERO_CIVICO_SEDE
,null				-- ter.CAP_COMUNE_SEDE
,null				-- com.DS_COMUNE
,null				-- com.CD_PROVINCIA
,to_date(null)		-- min.DT_INIZIO_MINICARRIERA
,to_date(null)		-- min.DT_FINE_MINICARRIERA
,0					-- min.IM_TOTALE_MINICARRIERA
,null				-- decode(min.DT_RINNOVO,null,'N','Y')  -- rinnovo
,null				-- min.TI_ANTICIPO_POSTICIPO
,null				-- min.FL_TASSAZIONE_SEPARATA
,0					-- calcolaAltreBorse(esb.ESERCIZIO,min.CD_TERZO)
,null 				-- mrata.STATO_ASS_COMPENSO
,cmin.PG_COMPENSO
,cmin.ESERCIZIO_COMPENSO
,0				 -- com.IM_NETTO_PERCIPIENTE
,null				-- com.CD_CDS_OBBLIGAZIONE
,0					-- com.ESERCIZIO_OBBLIGAZIONE
,0					-- com.ESERCIZIO_ORI_OBBLIGAZIONE
,0					-- com.PG_OBBLIGAZIONE
,0					-- com.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null)		-- obbs.DT_SCADENZA
,null			 	-- decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R')
,obbv.CD_VOCE
,cmin.MAX_PG_RATA	-- mrata.PG_RATA
,0			 		-- mrata.IM_RATA
,0			 		-- com.DETRAZIONE_ALTRI_NETTO
,0			 		-- com.DETRAZIONE_CONIUGE_NETTO
,0			 		-- com.DETRAZIONE_FIGLI_NETTO
,0			 		-- com.DETRAZIONI_LA_NETTO
,0			 		-- com.DETRAZIONI_PERSONALI_NETTO
,0
,null				-- crit.CD_CONTRIBUTO_RITENUTA
,0					-- crit.AMMONTARE
,0					-- max.MAX_PG_RATA
from minicarriera min
	,(select distinct
 			 mrata.CD_CDS
			,mrata.CD_UNITA_ORGANIZZATIVA
			,mrata.ESERCIZIO
			,mrata.PG_MINICARRIERA
			,mrata.CD_CDS_COMPENSO
			,mrata.CD_UO_COMPENSO
			,mrata.ESERCIZIO_COMPENSO
			,mrata.PG_COMPENSO
			,max(mrata.PG_RATA) max_pg_rata
	 from minicarriera_rata mrata
	 group by mrata.CD_CDS
			,mrata.CD_UNITA_ORGANIZZATIVA
			,mrata.ESERCIZIO
			,mrata.PG_MINICARRIERA
			,mrata.CD_CDS_COMPENSO
			,mrata.CD_UO_COMPENSO
			,mrata.ESERCIZIO_COMPENSO
			,mrata.PG_COMPENSO) cmin
	,compenso com
	,obbligazione_scad_voce obbv
where cmin.CD_CDS 				  = min.CD_CDS
  and cmin.CD_UNITA_ORGANIZZATIVA = min.CD_UNITA_ORGANIZZATIVA
  and cmin.ESERCIZIO			  = min.ESERCIZIO
  and cmin.PG_MINICARRIERA		  = min.PG_MINICARRIERA
  and com.CD_CDS				  = cmin.CD_CDS_COMPENSO
  and com.CD_UNITA_ORGANIZZATIVA  = cmin.CD_UO_COMPENSO
  and com.ESERCIZIO				  = cmin.ESERCIZIO_COMPENSO
  and com.PG_COMPENSO			  = cmin.PG_COMPENSO
  and obbv.CD_CDS				  = com.CD_CDS_OBBLIGAZIONE
  and obbv.ESERCIZIO			  = com.ESERCIZIO_OBBLIGAZIONE
  and obbv.ESERCIZIO_ORIGINALE		  = com.ESERCIZIO_ORI_OBBLIGAZIONE
  and obbv.PG_OBBLIGAZIONE		  = com.PG_OBBLIGAZIONE
  and obbv.PG_OBBLIGAZIONE_SCADENZARIO = com.PG_OBBLIGAZIONE_SCADENZARIO
union all
select   -- rate della minicarriera, raggruppate per compenso
 mrata.CD_CDS
,mrata.CD_UNITA_ORGANIZZATIVA
,mrata.ESERCIZIO
,mrata.PG_MINICARRIERA
,'B' 				-- ti_record_l1
,'A'				-- ti_record_l2
,'A'				-- ti_record_l3
,0					-- esb.ESERCIZIO esercizio_base
,null				-- min.CODICE_FISCALE
,null				-- min.COGNOME
,null				-- min.NOME
,null				-- min.STATO
,null				-- min.DS_MINICARRIERA
,null				-- ter.VIA_SEDE
,null				-- ter.NUMERO_CIVICO_SEDE
,null				-- ter.CAP_COMUNE_SEDE
,null				-- com.DS_COMUNE
,null				-- com.CD_PROVINCIA
,to_date(null)		-- min.DT_INIZIO_MINICARRIERA
,to_date(null)		-- min.DT_FINE_MINICARRIERA
,0					-- min.IM_TOTALE_MINICARRIERA
,null				-- decode(min.DT_RINNOVO,null,'N','Y')  -- rinnovo
,null				-- min.TI_ANTICIPO_POSTICIPO
,null				-- min.FL_TASSAZIONE_SEPARATA
,0					-- calcolaAltreBorse(esb.ESERCIZIO,min.CD_TERZO)
,mrata.STATO_ASS_COMPENSO
,decode(mrata.PG_COMPENSO,null,9999999999,mrata.PG_COMPENSO)
,decode(mrata.ESERCIZIO_COMPENSO,null,9999,mrata.ESERCIZIO_COMPENSO)
,com.IM_NETTO_PERCIPIENTE
,null				-- com.CD_CDS_OBBLIGAZIONE
,0					-- com.ESERCIZIO_OBBLIGAZIONE
,0			 	 -- com.ESERCIZIO_ORI_OBBLIGAZIONE
,0					-- com.PG_OBBLIGAZIONE
,0					-- com.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null)		-- obbs.DT_SCADENZA
,null			 	-- decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R')
,null 				-- obbv.CD_VOCE
,mrata.PG_RATA
,mrata.IM_RATA
,0			   		-- com.DETRAZIONE_ALTRI_NETTO
,0			   		-- com.DETRAZIONE_CONIUGE_NETTO
,0			   		-- com.DETRAZIONE_FIGLI_NETTO
,0			   		-- com.DETRAZIONI_LA_NETTO
,0			   		-- com.DETRAZIONI_PERSONALI_NETTO
,0
,null				-- crit.CD_CONTRIBUTO_RITENUTA
,0					-- crit.AMMONTARE
,0					-- max.MAX_PG_RATA
from minicarriera_rata mrata
	,compenso com
where com.CD_CDS				 (+) = mrata.CD_CDS_COMPENSO
  and com.CD_UNITA_ORGANIZZATIVA (+) = mrata.CD_UO_COMPENSO
  and com.ESERCIZIO				 (+) = mrata.ESERCIZIO_COMPENSO
  and com.PG_COMPENSO			 (+) = mrata.PG_COMPENSO
union all
select   -- contributi ritenuta associati al compenso
 mrata.CD_CDS
,mrata.CD_UNITA_ORGANIZZATIVA
,mrata.ESERCIZIO
,mrata.PG_MINICARRIERA
,'B' 				-- ti_record_l1
,'A'				-- ti_record_l2
,'B'				-- ti_record_l3
,0					-- esb.ESERCIZIO esercizio_base
,null				-- min.CODICE_FISCALE
,null				-- min.COGNOME
,null				-- min.NOME
,null				-- min.STATO
,null				-- min.DS_MINICARRIERA
,null				-- ter.VIA_SEDE
,null				-- ter.NUMERO_CIVICO_SEDE
,null				-- ter.CAP_COMUNE_SEDE
,null				-- com.DS_COMUNE
,null				-- com.CD_PROVINCIA
,to_date(null)		-- min.DT_INIZIO_MINICARRIERA
,to_date(null)		-- min.DT_FINE_MINICARRIERA
,0					-- min.IM_TOTALE_MINICARRIERA
,null				-- decode(min.DT_RINNOVO,null,'N','Y')  -- rinnovo
,null				-- min.TI_ANTICIPO_POSTICIPO
,null				-- min.FL_TASSAZIONE_SEPARATA
,0					-- calcolaAltreBorse(esb.ESERCIZIO,min.CD_TERZO)
,mrata.STATO_ASS_COMPENSO
,mrata.PG_COMPENSO
,mrata.ESERCIZIO_COMPENSO
,com.IM_NETTO_PERCIPIENTE
,null				-- com.CD_CDS_OBBLIGAZIONE
,0					-- com.ESERCIZIO_OBBLIGAZIONE
,0			 	 -- com.ESERCIZIO_ORI_OBBLIGAZIONE
,0					-- com.PG_OBBLIGAZIONE
,0					-- com.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null)		-- obbs.DT_SCADENZA
,null			 	-- decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R')
,null 				-- obbv.CD_VOCE
,mrata.PG_RATA
,0					-- mrata.IM_RATA
,0			   		-- com.DETRAZIONE_ALTRI_NETTO
,0			   		-- com.DETRAZIONE_CONIUGE_NETTO
,0			   		-- com.DETRAZIONE_FIGLI_NETTO
,0			   		-- com.DETRAZIONI_LA_NETTO
,0			   		-- com.DETRAZIONI_PERSONALI_NETTO
,0
,crit.CD_CONTRIBUTO_RITENUTA
,crit.AMMONTARE
,decode(mrata.STATO_ASS_COMPENSO,'T',max.MAX_PG_RATA,0)
from minicarriera_rata mrata
	,compenso com
	,contributo_ritenuta crit
	,(select com.cd_cds, com.cd_unita_organizzativa, com.esercizio, com.pg_compenso, max(mrata.pg_rata) max_pg_rata
	  from minicarriera_rata mrata
	  	  ,compenso com
	  where com.CD_CDS				  = mrata.CD_CDS_COMPENSO
  	    and com.CD_UNITA_ORGANIZZATIVA  = mrata.CD_UO_COMPENSO
  		and com.ESERCIZIO				  = mrata.ESERCIZIO_COMPENSO
  		and com.PG_COMPENSO			  = mrata.PG_COMPENSO
	  group by com.cd_cds, com.cd_unita_organizzativa, com.esercizio, com.pg_compenso) max
where com.CD_CDS				  (+) = mrata.CD_CDS_COMPENSO
  and com.CD_UNITA_ORGANIZZATIVA  (+) = mrata.CD_UO_COMPENSO
  and com.ESERCIZIO				  (+) = mrata.ESERCIZIO_COMPENSO
  and com.PG_COMPENSO			  (+) = mrata.PG_COMPENSO
  and crit.CD_CDS				  (+) = com.CD_CDS
  and crit.CD_UNITA_ORGANIZZATIVA (+) = com.CD_UNITA_ORGANIZZATIVA
  and crit.ESERCIZIO			  (+) = com.ESERCIZIO
  and crit.PG_COMPENSO			  (+) = com.PG_COMPENSO
  and crit.TI_ENTE_PERCIPIENTE	  (+) = 'P'
  and max.cd_cds 				 (+) = com.CD_CDS
  and max.cd_unita_organizzativa (+) = com.CD_UNITA_ORGANIZZATIVA
  and max.esercizio				 (+) = com.ESERCIZIO
  and max.pg_compenso			 (+) = com.PG_COMPENSO
union all
select   -- detrazioni associate al compenso
 mrata.CD_CDS
,mrata.CD_UNITA_ORGANIZZATIVA
,mrata.ESERCIZIO
,mrata.PG_MINICARRIERA
,'B' 				-- ti_record_l1
,'A'				-- ti_record_l2
,'C'				-- ti_record_l3
,0					-- esb.ESERCIZIO esercizio_base
,null				-- min.CODICE_FISCALE
,null				-- min.COGNOME
,null				-- min.NOME
,null				-- min.STATO
,null				-- min.DS_MINICARRIERA
,null				-- ter.VIA_SEDE
,null				-- ter.NUMERO_CIVICO_SEDE
,null				-- ter.CAP_COMUNE_SEDE
,null				-- com.DS_COMUNE
,null				-- com.CD_PROVINCIA
,to_date(null)		-- min.DT_INIZIO_MINICARRIERA
,to_date(null)		-- min.DT_FINE_MINICARRIERA
,0					-- min.IM_TOTALE_MINICARRIERA
,null				-- decode(min.DT_RINNOVO,null,'N','Y')  -- rinnovo
,null				-- min.TI_ANTICIPO_POSTICIPO
,null				-- min.FL_TASSAZIONE_SEPARATA
,0					-- calcolaAltreBorse(esb.ESERCIZIO,min.CD_TERZO)
,mrata.STATO_ASS_COMPENSO
,mrata.PG_COMPENSO
,mrata.ESERCIZIO_COMPENSO
,com.IM_NETTO_PERCIPIENTE
,null				-- com.CD_CDS_OBBLIGAZIONE
,0					-- com.ESERCIZIO_OBBLIGAZIONE
,0			 	 -- com.ESERCIZIO_ORI_OBBLIGAZIONE
,0					-- com.PG_OBBLIGAZIONE
,0					-- com.PG_OBBLIGAZIONE_SCADENZARIO
,to_date(null)		-- obbs.DT_SCADENZA
,null			 	-- decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R')
,null 				-- obbv.CD_VOCE
,mrata.PG_RATA
,0					-- mrata.IM_RATA
,com.DETRAZIONE_ALTRI_NETTO
,com.DETRAZIONE_CONIUGE_NETTO
,com.DETRAZIONE_FIGLI_NETTO
,com.DETRAZIONI_LA_NETTO
,com.DETRAZIONI_PERSONALI_NETTO
,COM.DETRAZIONE_RID_CUNEO_NETTO
,null				-- crit.CD_CONTRIBUTO_RITENUTA
,0					-- crit.AMMONTARE
,decode(mrata.STATO_ASS_COMPENSO,'T',max.MAX_PG_RATA,0)
from minicarriera_rata mrata
	,compenso com
	,(select com.cd_cds, com.cd_unita_organizzativa, com.esercizio, com.pg_compenso, max(mrata.pg_rata) max_pg_rata
	  from minicarriera_rata mrata
	  	  ,compenso com
	  where com.CD_CDS				  = mrata.CD_CDS_COMPENSO
  	    and com.CD_UNITA_ORGANIZZATIVA  = mrata.CD_UO_COMPENSO
  		and com.ESERCIZIO				  = mrata.ESERCIZIO_COMPENSO
  		and com.PG_COMPENSO			  = mrata.PG_COMPENSO
	  group by com.cd_cds, com.cd_unita_organizzativa, com.esercizio, com.pg_compenso) max
where com.CD_CDS				 (+) = mrata.CD_CDS_COMPENSO
  and com.CD_UNITA_ORGANIZZATIVA (+) = mrata.CD_UO_COMPENSO
  and com.ESERCIZIO				 (+) = mrata.ESERCIZIO_COMPENSO
  and com.PG_COMPENSO			 (+) = mrata.PG_COMPENSO
  and max.cd_cds 				 (+) = com.CD_CDS
  and max.cd_unita_organizzativa (+) = com.CD_UNITA_ORGANIZZATIVA
  and max.esercizio				 (+) = com.ESERCIZIO
  and max.pg_compenso			 (+) = com.PG_COMPENSO
);

   COMMENT ON TABLE "VP_MINICARRIERA_BDS"  IS 'Vista per la stampa riepilogativa della borsa di studio (minicarriera per borsa di studio)';
