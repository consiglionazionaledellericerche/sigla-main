--------------------------------------------------------
--  DDL for View VCOMPENSOSIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VCOMPENSOSIP" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_TERZO", "DENOMINAZIONE", "PARTITA_IVA", "CODICE_FISCALE", "TIPO", "MATRICOLA", "DS_COMPENSO", "IM_TOTALE_COMPENSO", "PG_COMPENSO", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "CD_ELEMENTO_VOCE", "CD_CENTRO_RESPONSABILITA", "GAE", "DT_PAGAMENTO") AS 
  SELECT V.ESERCIZIO,V.CD_CDS,V.CD_UNITA_ORGANIZZATIVA,V.CD_TERZO,NVL(V.RAGIONE_SOCIALE,V.COGNOME||' '||V.NOME) DENOMINAZIONE,
V.PARTITA_IVA,V.CODICE_FISCALE,TI_ANAGRAFICO,
 (Select Distinct MATRICOLA_DIPENDENTE From RAPPORTO,terzo t Where CD_TIPO_RAPPORTO ='DIP' And RAPPORTO.CD_ANAG =T.CD_ANAG And
 v.cd_terzo = t.cd_terzo) MATRICOLA_DIPENDENTE,
V.DS_compenso,V.IM_TOTALE_compenso,V.PG_compenso,v.DT_da_competenza_coge,v.DT_a_competenza_coge,
obb.cd_elemento_voce  cd_elemento_voce,
obb_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
obb_scad_voce.cd_linea_attivita gae,m.dt_pagamento
 FROM compenso V, compenso_riga cr,
 obbligazione obb,obbligazione_scadenzario obb_scad,obbligazione_scad_voce obb_scad_voce,mandato m ,mandato_riga m_riga
Where
m_riga.esercizio                        = m.esercizio                                   And
m_riga.cd_cds                           = m.cd_cds                                      And
m_riga.pg_mandato                       = m.pg_mandato                                  And
m_riga.ESERCIZIO_OBBLIGAZIONE           = cr.esercizio_obbligazione                      And
m_riga.ESERCIZIO_ORI_OBBLIGAZIONE       = cr.esercizio_ori_obbligazione                  And
m_riga.cd_cds                           = cr.cd_cds_obbligazione                         And
m_riga.PG_OBBLIGAZIONE                  = cr.pg_obbligazione                             And
m_riga.PG_OBBLIGAZIONE_SCADENZARIO      = cr.PG_OBBLIGAZIONE_SCADENZARIO                 And
v.cd_cds 								= cr.cd_cds                                      And
v.cd_unita_organizzativa 				= cr.cd_unita_organizzativa                      And
v.esercizio 							= cr.esercizio                                   And
v.pg_compenso 							= cr.pg_compenso                                And
m_riga.esercizio_DOC_AMM                = V.esercizio                                   And
m_riga.CD_CDS_DOC_AMM                   = V.cd_cds                                      And
m_riga.CD_UO_DOC_AMM                    = V.cd_unita_organizzativa                      And
m_riga.pg_doc_amm                       = V.pg_compenso                                 And
cr.esercizio_obbligazione                = obb_scad.esercizio 		                and
cr.cd_cds_obbligazione	                = obb_scad.cd_cds    		                and
cr.pg_obbligazione 			= obb_scad.pg_obbligazione 		        And
cr.pg_obbligazione_scadenzario		= obb_scad.pg_obbligazione_scadenzario          and
cr.esercizio_ori_obbligazione 		= obb_scad.esercizio_originale	                And
obb_scad.esercizio        	        = obb.esercizio			                and
obb_scad.cd_cds 			= obb.cd_cds 				        And
obb_scad.pg_obbligazione 		= obb.pg_obbligazione 		                and
obb_scad.esercizio_originale		= obb.esercizio_originale	                and
obb_scad.esercizio			= obb_scad_voce.esercizio			and
obb_scad.cd_cds 			= obb_scad_voce.cd_cds 				and
obb_scad.pg_obbligazione 		= obb_scad_voce.pg_obbligazione 		and
obb_scad.pg_obbligazione_scadenzario	= obb_scad_voce.pg_obbligazione_scadenzario 	AND
obb_scad.esercizio_originale		= obb_scad_voce.esercizio_originale	        and
V.STATO_COFI='P'  And
v.PG_MISSIONE Is Null And
FL_GENERATA_FATTURA     ='N' And
FL_COMPENSO_STIPENDI    ='N' And
FL_COMPENSO_CONGUAGLIO  ='N' And
--FL_SENZA_CALCOLI  ='N' And
im_totale_compenso  > 0
And M.DT_PAGAMENTO IS NOT NULL;
