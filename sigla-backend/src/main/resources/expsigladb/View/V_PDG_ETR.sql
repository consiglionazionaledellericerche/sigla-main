--------------------------------------------------------
--  DDL for View V_PDG_ETR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_ETR" ("ESERCIZIO", "PESO_DIPARTIMENTO", "CD_DIPARTIMENTO", "DS_DIPARTIMENTO", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "TIPO_PROGETTO", "DS_TIPO_PROGETTO", "CDS", "DS_CDS", "UO", "TIPO_UO", "DS_UO", "CDR", "CD_LINEA_ATTIVITA", "TITOLO", "DS_TITOLO", "CODICE_CLAS_ENTRATA", "DS_CLASSIFICAZIONE_ENTRATA", "ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "ORIGINE", "CATEGORIA_DETTAGLIO", "NATURA", "DS_NATURA", "PG_ENTRATA", "FL_RIBALTATO", "IM_ENTRATE_RICAVI", "IM_ENTRATE_SENZA_RICAVI") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista per la consultazione del PDG per commessa parte entrata
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
pdg_preventivo_etr_det.esercizio,
nvl(p.peso,1000),
Nvl(progetto.cd_dipartimento, Null),
p.DS_DIPARTIMENTO,
Nvl(progetto.cd_progetto,Null),
Nvl(progetto.ds_progetto,Null),
Nvl(com.cd_progetto,Null),
Nvl(com.ds_progetto,Null),
Nvl(modu.cd_progetto,Null),
Nvl(modu.ds_progetto,Null),
Nvl(modu.cd_tipo_progetto,Null),
TIPO_PROGETTO.DS_TIPO_PROGETTO,
cds.CD_UNITA_ORGANIZZATIVA,
cds.DS_UNITA_ORGANIZZATIVA ,
unita_organizzativa.cd_unita_organizzativa,
UNITA_ORGANIZZATIVA.CD_TIPO_UNITA,
UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA ,
PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA,
PDG_PREVENTIVO_ETR_DET.CD_LINEA_ATTIVITA,
TITOLO.CD_ELEMENTO_VOCE,
TITOLO.DS_ELEMENTO_VOCE,
classificazione_entrate.codice_cla_e,
classificazione_entrate.descrizione,
elemento_voce.cd_elemento_voce,
ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
PDG_PREVENTIVO_ETR_DET.ORIGINE,
PDG_PREVENTIVO_ETR_DET.CATEGORIA_DETTAGLIO,
PDG_PREVENTIVO_ETR_DET.CD_NATURA,
NATURA.DS_NATURA,
PDG_PREVENTIVO_ETR_DET.PG_ENTRATA,
PDG_PREVENTIVO.FL_RIBALTATO_SU_AREA,
pdg_preventivo_etr_det.IM_RA_RCE,
pdg_preventivo_etr_det.IM_RC_ESR
From    classificazione_entrate,
	elemento_voce,
	ELEMENTO_VOCE CATEGORIA,
	ELEMENTO_VOCE TITOLO,
	PDG_PREVENTIVO,
	pdg_preventivo_etr_det,
	linea_attivita,
	NATURA,
	cdr,
	unita_organizzativa,
	unita_organizzativa cds,
	progetto_prev progetto,
	progetto_prev com,
	progetto_prev modu,
	TIPO_PROGETTO,
	DIPARTIMENTO,
	dipartimento_peso p
Where
 progetto.esercizio= p.esercizio(+) AND
 progetto.cd_dipartimento= p.cd_dipartimento (+) and
 PDG_PREVENTIVO_ETR_DET.ESERCIZIO                = PDG_PREVENTIVO.ESERCIZIO
And     PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA = PDG_PREVENTIVO.CD_CENTRO_RESPONSABILITA
And     classificazione_entrate.ESERCIZIO		= elemento_voce.ESERCIZIO_CLA_E
and	classificazione_entrate.CODICE_CLA_E		= elemento_voce.COD_CLA_E
AND	ELEMENTO_VOCE.ESERCIZIO				= CATEGORIA.ESERCIZIO
AND	ELEMENTO_VOCE.TI_APPARTENENZA			= CATEGORIA.TI_APPARTENENZA
AND	ELEMENTO_VOCE.TI_GESTIONE			= CATEGORIA.TI_GESTIONE
AND	ELEMENTO_VOCE.CD_ELEMENTO_PADRE			= CATEGORIA.CD_ELEMENTO_VOCE
AND	CATEGORIA.ESERCIZIO				= TITOLO.ESERCIZIO
AND	CATEGORIA.TI_APPARTENENZA			= TITOLO.TI_APPARTENENZA
AND	CATEGORIA.TI_GESTIONE				= TITOLO.TI_GESTIONE
AND	CATEGORIA.CD_ELEMENTO_PADRE			= TITOLO.CD_ELEMENTO_VOCE
and	elemento_voce.ESERCIZIO 			= pdg_preventivo_etr_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= pdg_preventivo_etr_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= pdg_preventivo_etr_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= pdg_preventivo_etr_det.CD_ELEMENTO_VOCE
And     PDG_PREVENTIVO_ETR_DET.CD_NATURA                = NATURA.CD_NATURA
and	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= linea_attivita.CD_CENTRO_RESPONSABILITA
and	pdg_preventivo_etr_det.CD_LINEA_ATTIVITA	= linea_attivita.CD_LINEA_ATTIVITA
and	linea_attivita.CD_CENTRO_RESPONSABILITA		= cdr.CD_CENTRO_RESPONSABILITA
and	cdr.CD_UNITA_ORGANIZZATIVA			= unita_organizzativa.CD_UNITA_ORGANIZZATIVA
and	unita_organizzativa.CD_UNITA_PADRE		= cds.CD_UNITA_ORGANIZZATIVA
And     linea_attivita.pg_progetto                      Is Not Null
and	linea_attivita.PG_PROGETTO			= modu.PG_PROGETTO
And     MODU.ESERCIZIO                                  = PDG_PREVENTIVO_ETR_DET.ESERCIZIO
And 	MODU.ESERCIZIO_PROGETTO_PADRE			= COM.ESERCIZIO
And 	MODU.PG_PROGETTO_PADRE				= COM.PG_PROGETTO
And 	COM.ESERCIZIO_PROGETTO_PADRE			= PROGETTO.ESERCIZIO
And	COM.PG_PROGETTO_PADRE                           = PROGETTO.PG_PROGETTO
And     MODU.CD_TIPO_PROGETTO                           = TIPO_PROGETTO.CD_TIPO_PROGETTO
And     PROGETTO.CD_DIPARTIMENTO                        = DIPARTIMENTO.CD_DIPARTIMENTO
And     PDG_PREVENTIVO_ETR_DET.STATO                    = 'Y'
and	modu.LIVELLO					= 3
And     PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE Is NULL
And     PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG        Is NULL
Union All
Select
pdg_preventivo_etr_det.esercizio,
DECODE(UNITA_ORGANIZZATIVA.CD_TIPO_UNITA,'SAC',13,1000),
DECODE(UNITA_ORGANIZZATIVA.CD_TIPO_UNITA,'SAC','SAC',UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA),
DECODE(UNITA_ORGANIZZATIVA.CD_TIPO_UNITA,'SAC',CDS.DS_UNITA_ORGANIZZATIVA,NULL),
Null,
Null,
Null,
Null,
Null,
Null,
Null,
Null,
cds.CD_UNITA_ORGANIZZATIVA,
cds.DS_UNITA_ORGANIZZATIVA ,
unita_organizzativa.cd_unita_organizzativa,
UNITA_ORGANIZZATIVA.CD_TIPO_UNITA,
UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA ,
PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA,
PDG_PREVENTIVO_ETR_DET.CD_LINEA_ATTIVITA,
TITOLO.CD_ELEMENTO_VOCE,
TITOLO.DS_ELEMENTO_VOCE,
classificazione_entrate.codice_cla_e,
classificazione_entrate.descrizione,
elemento_voce.cd_elemento_voce,
ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
PDG_PREVENTIVO_ETR_DET.ORIGINE,
PDG_PREVENTIVO_ETR_DET.CATEGORIA_DETTAGLIO,
PDG_PREVENTIVO_ETR_DET.CD_NATURA,
NATURA.DS_NATURA,
PDG_PREVENTIVO_ETR_DET.PG_ENTRATA,
PDG_PREVENTIVO.FL_RIBALTATO_SU_AREA,
pdg_preventivo_etr_det.IM_RA_RCE,
pdg_preventivo_etr_det.IM_RC_ESR
From    classificazione_entrate,
	elemento_voce,
	ELEMENTO_VOCE CATEGORIA,
	ELEMENTO_VOCE TITOLO,
	PDG_PREVENTIVO,
	pdg_preventivo_etr_det,
	linea_attivita,
	NATURA,
	cdr,
	unita_organizzativa,
	unita_organizzativa cds
Where   PDG_PREVENTIVO_ETR_DET.ESERCIZIO                = PDG_PREVENTIVO.ESERCIZIO
And     PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA = PDG_PREVENTIVO.CD_CENTRO_RESPONSABILITA
And     classificazione_entrate.ESERCIZIO		= elemento_voce.ESERCIZIO_CLA_E
and	classificazione_entrate.CODICE_CLA_E		= elemento_voce.COD_CLA_E
AND	ELEMENTO_VOCE.ESERCIZIO				= CATEGORIA.ESERCIZIO
AND	ELEMENTO_VOCE.TI_APPARTENENZA			= CATEGORIA.TI_APPARTENENZA
AND	ELEMENTO_VOCE.TI_GESTIONE			= CATEGORIA.TI_GESTIONE
AND	ELEMENTO_VOCE.CD_ELEMENTO_PADRE			= CATEGORIA.CD_ELEMENTO_VOCE
AND	CATEGORIA.ESERCIZIO				= TITOLO.ESERCIZIO
AND	CATEGORIA.TI_APPARTENENZA			= TITOLO.TI_APPARTENENZA
AND	CATEGORIA.TI_GESTIONE				= TITOLO.TI_GESTIONE
AND	CATEGORIA.CD_ELEMENTO_PADRE			= TITOLO.CD_ELEMENTO_VOCE
and	elemento_voce.ESERCIZIO 			= pdg_preventivo_etr_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= pdg_preventivo_etr_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= pdg_preventivo_etr_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= pdg_preventivo_etr_det.CD_ELEMENTO_VOCE
And     PDG_PREVENTIVO_ETR_DET.CD_NATURA                = NATURA.CD_NATURA
and	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= linea_attivita.CD_CENTRO_RESPONSABILITA
and	pdg_preventivo_etr_det.CD_LINEA_ATTIVITA	= linea_attivita.CD_LINEA_ATTIVITA
and	linea_attivita.CD_CENTRO_RESPONSABILITA		= cdr.CD_CENTRO_RESPONSABILITA
and	cdr.CD_UNITA_ORGANIZZATIVA			= unita_organizzativa.CD_UNITA_ORGANIZZATIVA
and	unita_organizzativa.CD_UNITA_PADRE		= cds.CD_UNITA_ORGANIZZATIVA
And     linea_attivita.pg_progetto                      Is Null
And     pdg_preventivo_etr_det.stato                    = 'Y'
And     PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE Is NULL
And     PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG        Is Null
;

   COMMENT ON TABLE "V_PDG_ETR"  IS 'Vista per la consultazione del PDG per commessa parte entrate';
