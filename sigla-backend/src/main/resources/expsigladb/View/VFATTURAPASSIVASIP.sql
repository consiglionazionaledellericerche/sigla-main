--------------------------------------------------------
--  DDL for View VFATTURAPASSIVASIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VFATTURAPASSIVASIP" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_TERZO", "DENOMINAZIONE", "INDIRIZZO", "PARTITA_IVA", "CODICE_FISCALE", "NR_FATTURA_FORNITORE", "DT_FATTURA_FORNITORE", "DS_FATTURA_PASSIVA", "IM_TOTALE_FATTURA", "PG_FATTURA", "CD_ELEMENTO_VOCE", "CD_CENTRO_RESPONSABILITA", "GAE", "DT_PAGAMENTO") AS 
  SELECT V.ESERCIZIO,V.CD_CDS,V.CD_UNITA_ORGANIZZATIVA,V.CD_TERZO,NVL(V.RAGIONE_SOCIALE,V.COGNOME||' '||V.NOME) DENOMINAZIONE,
A.VIA_FISCALE||DECODE(NVL(A.NUM_CIVICO_FISCALE,' '),' ',' ',','||A.NUM_CIVICO_FISCALE)||DECODE(NVL(A.FRAZIONE_FISCALE,' '),' ',' ',' '||A.FRAZIONE_FISCALE)||NVL(A.CAP_COMUNE_FISCALE,' ')||' '||C.DS_COMUNE INDIRIZZO,
V.PARTITA_IVA,V.CODICE_FISCALE,V.NR_FATTURA_FORNITORE,V.DT_FATTURA_FORNITORE,
V.DS_FATTURA_PASSIVA,Decode(v.ti_fattura,'C',-V.IM_TOTALE_FATTURA,V.IM_TOTALE_FATTURA),V.PG_FATTURA_PASSIVA,
obb.cd_elemento_voce  cd_elemento_voce,
obb_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
obb_scad_voce.cd_linea_attivita gae,m.dt_pagamento
 FROM FATTURA_PASSIVA V,ANAGRAFICO A,COMUNE C,TERZO T,FATTURA_PASSIVA_riga riga,
 obbligazione obb,obbligazione_scadenzario obb_scad,obbligazione_scad_voce obb_scad_voce,mandato m ,mandato_riga m_riga
Where
m_riga.cd_cds                           = m.cd_cds                                      And
m_riga.esercizio                        = m.esercizio                                   And
m_riga.pg_mandato                       = m.pg_mandato                                  And
m_riga.cd_cds                           = riga.cd_cds_obbligazione                      And
m_riga.ESERCIZIO_OBBLIGAZIONE           = riga.esercizio_obbligazione                   And
m_riga.ESERCIZIO_ORI_OBBLIGAZIONE       = riga.esercizio_ori_obbligazione               And
m_riga.PG_OBBLIGAZIONE                  = riga.pg_obbligazione                          And
m_riga.PG_OBBLIGAZIONE_SCADENZARIO      = riga.PG_OBBLIGAZIONE_SCADENZARIO              And
m_riga.esercizio_DOC_AMM                = riga.esercizio                                And
m_riga.CD_CDS_DOC_AMM                   = riga.cd_cds                                   And
m_riga.CD_UO_DOC_AMM                    = riga.cd_unita_organizzativa                   And
m_riga.pg_doc_amm                       = riga.pg_fattura_passiva                       And
riga.esercizio 				= v.esercizio 				        and
riga.cd_cds    				= v.cd_cds	 				and
riga.cd_unita_organizzativa 		= v.cd_unita_organizzativa 	                And
riga.pg_fattura_passiva 		= v.pg_fattura_passiva 		                and
riga.esercizio_obbligazione 		= obb_scad.esercizio 			        and
riga.cd_cds_obbligazione		= obb_scad.cd_cds 				and
riga.pg_obbligazione 			= obb_scad.pg_obbligazione 		        And
riga.pg_obbligazione_scadenzario	= obb_scad.pg_obbligazione_scadenzario	        and
riga.esercizio_ori_obbligazione 	= obb_scad.esercizio_originale	                and
obb_scad.esercizio			= obb.esercizio 				And
obb_scad.cd_cds 			= obb.cd_cds 				        And
obb_scad.pg_obbligazione 		= obb.pg_obbligazione 		                and
obb_scad.esercizio_originale		= obb.esercizio_originale	                and
obb_scad.esercizio			= obb_scad_voce.esercizio			and
obb_scad.cd_cds 			= obb_scad_voce.cd_cds 				and
obb_scad.pg_obbligazione 		= obb_scad_voce.pg_obbligazione 		and
obb_scad.pg_obbligazione_scadenzario	= obb_scad_voce.pg_obbligazione_scadenzario 	and
obb_scad.esercizio_originale		= obb_scad_voce.esercizio_originale	        and
V.CD_TERZO	  			= T.CD_TERZO	   		                And
T.CD_ANAG				= A.CD_ANAG				        AND
A.PG_COMUNE_FISCALE			= C.PG_COMUNE	                                AND
V.STATO_COFI                            = 'P'                                           And
M.DT_PAGAMENTO IS NOT Null
union
SELECT V.ESERCIZIO,V.CD_CDS,V.CD_UNITA_ORGANIZZATIVA,V.CD_TERZO,NVL(V.RAGIONE_SOCIALE,V.COGNOME||' '||V.NOME) DENOMINAZIONE,
A.VIA_FISCALE||DECODE(NVL(A.NUM_CIVICO_FISCALE,' '),' ',' ',','||A.NUM_CIVICO_FISCALE)||DECODE(NVL(A.FRAZIONE_FISCALE,' '),' ',' ',' '||A.FRAZIONE_FISCALE)||NVL(A.CAP_COMUNE_FISCALE,' ')||' '||C.DS_COMUNE INDIRIZZO,
V.PARTITA_IVA,V.CODICE_FISCALE,V.NR_FATTURA_FORNITORE,V.DT_FATTURA_FORNITORE,
V.DS_FATTURA_PASSIVA,Decode(v.ti_fattura,'C',-V.IM_TOTALE_FATTURA,V.IM_TOTALE_FATTURA) ,V.PG_FATTURA_PASSIVA,
acc.cd_elemento_voce  cd_elemento_voce,
acc_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
acc_scad_voce.cd_linea_attivita gae,r.dt_incasso
 FROM FATTURA_PASSIVA V,ANAGRAFICO A,COMUNE C,TERZO T,FATTURA_PASSIVA_riga riga,
  accertamento acc,accertamento_scadenzario acc_scad,accertamento_scad_voce acc_scad_voce,reversale r ,reversale_riga r_riga
Where
r_riga.cd_cds                           = r.cd_cds                                      And
r_riga.esercizio                        = r.esercizio                                   And
r_riga.pg_reversale                     = r.pg_reversale                                And
r_riga.cd_cds                           = riga.cd_cds_accertamento                      And
r_riga.esercizio_accertamento           = riga.esercizio_accertamento                   And
r_riga.PG_accertamento                  = riga.pg_accertamento                          And
r_riga.PG_accertamento_SCADENZARIO      = riga.PG_accertamento_SCADENZARIO              And
r_riga.ESERCIZIO_ORI_accertamento       = riga.esercizio_ori_accertamento               And
r_riga.esercizio_DOC_AMM                = riga.esercizio                                And
r_riga.CD_CDS_DOC_AMM                   = riga.cd_cds                                   And
r_riga.CD_UO_DOC_AMM                    = riga.cd_unita_organizzativa                   And
r_riga.pg_doc_amm                       = riga.pg_fattura_passiva                       And
riga.esercizio 				= v.esercizio 				        And
riga.cd_cds    				= v.cd_cds	 			        And
riga.cd_unita_organizzativa 		= v.cd_unita_organizzativa 	                and
riga.pg_fattura_passiva 		= v.pg_fattura_passiva 		                and
riga.esercizio_accertamento 		= acc_scad.esercizio 			        and
riga.cd_cds_accertamento		= acc_scad.cd_cds 			        and
riga.pg_accertamento	        	= acc_scad.pg_accertamento 		        And
riga.pg_accertamento_scadenzario	= acc_scad.pg_accertamento_scadenzario 	        and
riga.esercizio_ori_accertamento 	= acc_scad.esercizio_originale	                and
acc_scad.esercizio			= acc.esercizio 			        and
acc_scad.cd_cds 			= acc.cd_cds 				        and
acc_scad.pg_accertamento 		= acc.pg_accertamento 		                and
acc_scad.esercizio_originale 	  	= acc.esercizio_originale	                and
acc_scad.esercizio		   	= acc_scad_voce.esercizio		        and
acc_scad.cd_cds 			= acc_scad_voce.cd_cds 			        and
acc_scad.pg_accertamento 	        = acc_scad_voce.pg_accertamento 		and
acc_scad.pg_accertamento_scadenzario	= acc_scad_voce.pg_accertamento_scadenzario 	and
acc_scad.esercizio_originale 	  	= acc_scad_voce.esercizio_originale	        and
V.CD_TERZO	  			= T.CD_TERZO	   		                AND
T.CD_ANAG				= A.CD_ANAG				        AND
A.PG_COMUNE_FISCALE			= C.PG_COMUNE				        AND
V.STATO_COFI                            = 'P'                                           And
r.DT_INCASSO IS NOT Null;
