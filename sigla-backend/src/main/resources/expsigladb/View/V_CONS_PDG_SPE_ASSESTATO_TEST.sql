--------------------------------------------------------
--  DDL for View V_CONS_PDG_SPE_ASSESTATO_TEST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_SPE_ASSESTATO_TEST" ("ESERCIZIO", "PESO_DIP", "DIP", "DS_DIPARTIMENTO", "CDS", "DES_CDS", "UO", "DES_UO", "CDR", "DS_CDR", "CD_LINEA_ATTIVITA", "CD_ELEMENTO_VOCE", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "ESERCIZIO_PDG_VARIAZIONE", "PG_VARIAZIONE_PDG", "INI", "VAR_PIU", "VAR_MENO") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Piano di Gestione Spese Assestato
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
Pdg_preventivo_spe_det.esercizio, -- DIPARTIMENTI INIZIALE
        DECODE(PROGETTO.CD_DIPARTIMENTO,'TA',1,'ET',2,'AG',3,'ME',4,'SV',5,'PM',6,'MD',7,'SP',8,'ICT',9,'IC',10,'PC',11,'DG',12,'SAC',13,1000) PESO_DIP,
        Nvl(progetto.cd_dipartimento, Null) DIP,
        DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG              ,
        Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
        Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI) ini,
        0 VAR_PIU,
        0 VAR_MENO
From    Pdg_preventivo_spe_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI ,
	linea_attivita,
	progetto_prev progetto,
	progetto_prev com,
	progetto_prev modu,
	DIPARTIMENTO,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR
Where   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
and	Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= linea_attivita.CD_CENTRO_RESPONSABILITA
and	Pdg_preventivo_spe_det.CD_LINEA_ATTIVITA	= linea_attivita.CD_LINEA_ATTIVITA
And     linea_attivita.pg_progetto                      Is Not Null
and	linea_attivita.PG_PROGETTO			= modu.PG_PROGETTO
And     modu.ESERCIZIO                                  = Pdg_preventivo_spe_det.ESERCIZIO
And     modu.ESERCIZIO_PROGETTO_PADRE                   = com.ESERCIZIO
And	modu.PG_PROGETTO_PADRE                          = com.PG_PROGETTO
And     com.ESERCIZIO_PROGETTO_PADRE                    = progetto.ESERCIZIO
And	com.PG_PROGETTO_PADRE                           = progetto.PG_PROGETTO
And     PROGETTO.CD_DIPARTIMENTO                        = DIPARTIMENTO.CD_DIPARTIMENTO
And     Pdg_preventivo_spe_det.STATO                    = 'Y'
And 	pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE Is NULL
And     Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG        Is NULL
GROUP BY Pdg_preventivo_spe_det.esercizio,
        DECODE(PROGETTO.CD_DIPARTIMENTO,'TA',1,'ET',2,'AG',3,'ME',4,'SV',5,'PM',6,'MD',7,'SP',8,'ICT',9,'IC',10,'PC',11,'DG',12,'SAC',13,1000),
        Nvl(progetto.cd_dipartimento, Null),
        DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG
Having Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
       Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI)!=0
Union  -- DIPARTIMENTI VARIAZIONI PIU  --12/01/2006 eliminata la union all
        Select  Pdg_preventivo_spe_det.esercizio,
        DECODE(PROGETTO.CD_DIPARTIMENTO,'TA',1,'ET',2,'AG',3,'ME',4,'SV',5,'PM',6,'MD',7,'SP',8,'ICT',9,'IC',10,'PC',11,'DG',12,'SAC',13,1000) PESO_DIP,
        Nvl(progetto.cd_dipartimento, Null) DIP,
        DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG              ,
        0 ini,
        Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
            Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI) VAR_PIU,
        0 VAR_MENO
From    Pdg_preventivo_spe_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
	linea_attivita,
	progetto_prev progetto,
	progetto_prev com,
	progetto_prev modu,
	DIPARTIMENTO,
	PDG_VARIAZIONE,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR
Where   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
and	Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= linea_attivita.CD_CENTRO_RESPONSABILITA
and	Pdg_preventivo_spe_det.CD_LINEA_ATTIVITA	= linea_attivita.CD_LINEA_ATTIVITA
And     linea_attivita.pg_progetto                      Is Not Null
and	linea_attivita.PG_PROGETTO			= modu.PG_PROGETTO
And     modu.ESERCIZIO                                  = Pdg_preventivo_spe_det.ESERCIZIO
And     modu.ESERCIZIO_PROGETTO_PADRE                   = com.ESERCIZIO
And	modu.PG_PROGETTO_PADRE                          = com.PG_PROGETTO
And     com.ESERCIZIO_PROGETTO_PADRE                    = progetto.ESERCIZIO
And	com.PG_PROGETTO_PADRE                           = progetto.PG_PROGETTO
And     PROGETTO.CD_DIPARTIMENTO                        = DIPARTIMENTO.CD_DIPARTIMENTO
And     Pdg_preventivo_spe_det.STATO                    = 'Y'
And     Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO = 'APP'
And 	pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
        Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
        nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
        nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0) +
        Nvl(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI, 0) > 0
GROUP BY Pdg_preventivo_spe_det.esercizio,
        DECODE(PROGETTO.CD_DIPARTIMENTO,'TA',1,'ET',2,'AG',3,'ME',4,'SV',5,'PM',6,'MD',7,'SP',8,'ICT',9,'IC',10,'PC',11,'DG',12,'SAC',13,1000),
        Nvl(progetto.cd_dipartimento, Null),
        DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG
Having Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
       Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI)!=0
Union   -- DIPARTIMENTI VARIAZIONI MENO --12/01/2006 eliminata la union all
Select  Pdg_preventivo_spe_det.esercizio,
        DECODE(PROGETTO.CD_DIPARTIMENTO,'TA',1,'ET',2,'AG',3,'ME',4,'SV',5,'PM',6,'MD',7,'SP',8,'ICT',9,'IC',10,'PC',11,'DG',12,'SAC',13,1000) PESO_DIP,
        Nvl(progetto.cd_dipartimento, Null) DIP,
        DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG              ,
        0 ini,
        0 VAR_PIU,
        Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
            Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI) VAR_MENO
From    Pdg_preventivo_spe_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
	linea_attivita,
	progetto_prev progetto,
	progetto_prev com,
	progetto_prev modu,
	DIPARTIMENTO,
	PDG_VARIAZIONE,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR
Where   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
and	Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= linea_attivita.CD_CENTRO_RESPONSABILITA
and	Pdg_preventivo_spe_det.CD_LINEA_ATTIVITA	= linea_attivita.CD_LINEA_ATTIVITA
And     linea_attivita.pg_progetto                      Is Not Null
and	linea_attivita.PG_PROGETTO			= modu.PG_PROGETTO
And     modu.ESERCIZIO                                  = Pdg_preventivo_spe_det.ESERCIZIO
And     modu.ESERCIZIO_PROGETTO_PADRE                   = com.ESERCIZIO
And	modu.PG_PROGETTO_PADRE                          = com.PG_PROGETTO
And     com.ESERCIZIO_PROGETTO_PADRE                    = progetto.ESERCIZIO
And	com.PG_PROGETTO_PADRE                           = progetto.PG_PROGETTO
And     PROGETTO.CD_DIPARTIMENTO                        = DIPARTIMENTO.CD_DIPARTIMENTO
And     Pdg_preventivo_spe_det.STATO                    = 'Y'
And     Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO = 'APP'
And 	pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And         Nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI, 0) < 0
GROUP BY Pdg_preventivo_spe_det.esercizio,
        DECODE(PROGETTO.CD_DIPARTIMENTO,'TA',1,'ET',2,'AG',3,'ME',4,'SV',5,'PM',6,'MD',7,'SP',8,'ICT',9,'IC',10,'PC',11,'DG',12,'SAC',13,1000),
        Nvl(progetto.cd_dipartimento, Null),
        DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG
Having Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
       Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI)!=0
Union  -- SAC INIZIALE --12/01/2006 eliminata la union all
Select  Pdg_preventivo_spe_det.esercizio,
        13,
        'SAC',
        'dipartimento SAC',
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG              ,
        Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
            Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI) ini,
        0 VAR_PIU,
        0 VAR_MENO
From    Pdg_preventivo_spe_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS
Where   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
And     Pdg_preventivo_spe_det.STATO                    = 'Y'
And     Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE Is NULL
And     Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG        Is Null
And     Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And 	pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
Group BY Pdg_preventivo_spe_det.esercizio,
        13,
        'SAC',
        'dipartimento SAC',
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG
Having Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
       Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI)!=0
Union  -- SAC VARIAZIONI PIU' --12/01/2006 eliminata la union all
Select  Pdg_preventivo_spe_det.esercizio,
        13,
        'SAC',
        'dipartimento SAC',
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG              ,
        0 ini,
        Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
            Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI) VAR_PIU,
        0 VAR_MENO
From    Pdg_preventivo_spe_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
        UNITA_ORGANIZZATIVA CDS,
	unita_organizzativa,
        PDG_VARIAZIONE
Where   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
And     Pdg_preventivo_spe_det.STATO                    = 'Y'
And     Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And     Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO = 'APP'
And 	pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI, 0) > 0
Group BY Pdg_preventivo_spe_det.esercizio,
        13,
        'SAC',
        'dipartimento SAC',
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG
Having Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
       Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI)!=0
Union  -- SAC VARIAZIONI MENO  --12/01/2006 eliminata la union all
Select  Pdg_preventivo_spe_det.esercizio,
        13,
        'SAC',
        'dipartimento SAC',
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG              ,
        0 ini,
        0 VAR_PIU,
        Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
            Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI) VAR_MENO
From    Pdg_preventivo_spe_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	unita_organizzativa,
        UNITA_ORGANIZZATIVA CDS,
        PDG_VARIAZIONE
Where   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
And     Pdg_preventivo_spe_det.STATO                    = 'Y'
And     Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And     Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO = 'APP'
And 	pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And         Nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI, 0) < 0
GROUP BY Pdg_preventivo_spe_det.esercizio,
        13,
        'SAC',
        'dipartimento SAC',
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_spe_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_spe_det.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG
Having Sum(nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
            Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
            nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)) +
       Sum(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI)!=0
Union
Select  Pdg_modulo_spese_gest.esercizio, -- DIPARTIMENTI INIZIALE
        DECODE(PROGETTO.CD_DIPARTIMENTO,'TA',1,'ET',2,'AG',3,'ME',4,'SV',5,'PM',6,'MD',7,'SP',8,'ICT',9,'IC',10,'PC',11,'DG',12,'SAC',13,1000) PESO_DIP,
        Nvl(progetto.cd_dipartimento, Null) DIP,
        DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_modulo_spese_gest.CD_CDR_ASSEGNATARIO CDR,
        CDR.DS_CDR,
        Pdg_modulo_spese_gest.CD_LINEA_ATTIVITA,
        Pdg_modulo_spese_gest.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7,
        NULL,
        NULL,
        Nvl(Sum(nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0) ini,
        0 VAR_PIU,
        0 VAR_MENO
From    Pdg_modulo_spese_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI ,
	linea_attivita,
	progetto_gest progetto,
	progetto_gest com,
	progetto_gest modu,
	DIPARTIMENTO,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR
Where   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_modulo_spese_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_modulo_spese_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_modulo_spese_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_modulo_spese_gest.CD_ELEMENTO_VOCE
and	Pdg_modulo_spese_gest.CD_CDR_ASSEGNATARIO	= linea_attivita.CD_CENTRO_RESPONSABILITA
and	Pdg_modulo_spese_gest.CD_LINEA_ATTIVITA 	= linea_attivita.CD_LINEA_ATTIVITA
And     linea_attivita.pg_progetto                      Is Not Null
and	linea_attivita.PG_PROGETTO			= modu.PG_PROGETTO
And     modu.ESERCIZIO                                  = Pdg_modulo_spese_gest.ESERCIZIO
And     modu.ESERCIZIO_PROGETTO_PADRE                   = com.ESERCIZIO
And	modu.PG_PROGETTO_PADRE                          = com.PG_PROGETTO
And     com.ESERCIZIO_PROGETTO_PADRE                    = progetto.ESERCIZIO
And	com.PG_PROGETTO_PADRE                           = progetto.PG_PROGETTO
And     PROGETTO.CD_DIPARTIMENTO                        = DIPARTIMENTO.CD_DIPARTIMENTO
And 	Pdg_modulo_spese_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     pdg_modulo_spese_gest.CATEGORIA_DETTAGLIO       In ('DIR', 'STI')
GROUP BY Pdg_modulo_spese_gest.esercizio,
        DECODE(PROGETTO.CD_DIPARTIMENTO,'TA',1,'ET',2,'AG',3,'ME',4,'SV',5,'PM',6,'MD',7,'SP',8,'ICT',9,'IC',10,'PC',11,'DG',12,'SAC',13,1000),
        Nvl(progetto.cd_dipartimento, Null),
        DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_modulo_spese_gest.CD_CDR_ASSEGNATARIO,
        CDR.DS_CDR,
        Pdg_modulo_spese_gest.CD_LINEA_ATTIVITA,
        Pdg_modulo_spese_gest.cd_elemento_voce,
        V_CLASSIFICAZIONE_VOCI.CD_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE,
        V_CLASSIFICAZIONE_VOCI.NR_LIVELLO,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO2,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO3,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO4,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO5,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO6,
        V_CLASSIFICAZIONE_VOCI.CD_LIVELLO7
Having Nvl(Sum(Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
               Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
               Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
               Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0) !=0
;
