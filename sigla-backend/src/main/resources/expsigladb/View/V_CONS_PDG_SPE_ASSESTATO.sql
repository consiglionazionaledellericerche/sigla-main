--------------------------------------------------------
--  DDL for View V_CONS_PDG_SPE_ASSESTATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_SPE_ASSESTATO" ("ESERCIZIO", "PESO_DIP", "DIP", "DS_DIPARTIMENTO", "CDS", "DES_CDS", "UO", "DES_UO", "CDR", "DS_CDR", "CD_LINEA_ATTIVITA", "CD_ELEMENTO_VOCE", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "ESERCIZIO_PDG_VARIAZIONE", "PG_VARIAZIONE_PDG", "INI", "VAR_PIU", "VAR_MENO") AS 
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
        nvl(p.peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,dipartimento.ds_dipartimento) DS_DIPARTIMENTO,
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
	v_linea_attivita_valida,
	DIPARTIMENTO,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
        dipartimento_peso p
Where
   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
and	Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_preventivo_spe_det.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
and v_linea_attivita_valida.ESERCIZIO= Pdg_preventivo_spe_det.ESERCIZIO
And v_linea_attivita_valida.pg_progetto                      Is Not Null
And v_linea_attivita_valida.cd_programma                        = DIPARTIMENTO.CD_DIPARTIMENTO
and v_linea_attivita_valida.ESERCIZIO= p.esercizio(+)
AND v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+)
And  Pdg_preventivo_spe_det.STATO                    = 'Y'
And 	pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE Is NULL
And     Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG        Is NULL
GROUP BY Pdg_preventivo_spe_det.esercizio,
        nvl(p.peso,1000),
        Nvl(v_linea_attivita_valida.cd_programma, Null),
        nvl(p.DS_DIPARTIMENTO,dipartimento.ds_dipartimento) ,
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
        nvl(p.peso,1000),
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,dipartimento.ds_dipartimento) DS_DIPARTIMENTO,
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
	v_linea_attivita_valida,
	DIPARTIMENTO,
	PDG_VARIAZIONE,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
        dipartimento_peso p
Where
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
and	Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_preventivo_spe_det.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
and v_linea_attivita_valida.ESERCIZIO= Pdg_preventivo_spe_det.ESERCIZIO
And v_linea_attivita_valida.pg_progetto                      Is Not Null
And v_linea_attivita_valida.cd_programma                        = DIPARTIMENTO.CD_DIPARTIMENTO
and v_linea_attivita_valida.ESERCIZIO= p.esercizio(+)
AND v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+)
And Pdg_preventivo_spe_det.STATO                    = 'Y'
And Pdg_preventivo_spe_det.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And Pdg_preventivo_spe_det.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And PDG_VARIAZIONE.STATO = 'APP'
And 	pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     nvl(PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0) +
        Nvl(PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0) +
        nvl(PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0) +
        nvl(PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0) +
        Nvl(PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI, 0) > 0
GROUP BY Pdg_preventivo_spe_det.esercizio,
        nvl(p.peso,1000),
        Nvl(v_linea_attivita_valida.cd_programma, Null),
        nvl(p.DS_DIPARTIMENTO,dipartimento.ds_dipartimento),
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
        nvl(p.peso,1000),
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,dipartimento.ds_dipartimento) DS_DIPARTIMENTO,
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
	v_linea_attivita_valida,
	DIPARTIMENTO,
	PDG_VARIAZIONE,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
        dipartimento_peso p
Where
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_preventivo_spe_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_preventivo_spe_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_preventivo_spe_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_preventivo_spe_det.CD_ELEMENTO_VOCE
and	Pdg_preventivo_spe_det.CD_CENTRO_RESPONSABILITA	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_preventivo_spe_det.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
and v_linea_attivita_valida.ESERCIZIO= Pdg_preventivo_spe_det.ESERCIZIO
And     v_linea_attivita_valida.pg_progetto                      Is Not Null
And     v_linea_attivita_valida.cd_programma                        = DIPARTIMENTO.CD_DIPARTIMENTO
and v_linea_attivita_valida.ESERCIZIO= p.esercizio(+)
AND v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+)
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
        nvl(p.peso,1000),
        Nvl(v_linea_attivita_valida.cd_programma, Null),
        nvl(p.DS_DIPARTIMENTO,dipartimento.ds_dipartimento),
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
        nvl( peso,1000),
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
        UNITA_ORGANIZZATIVA CDS,
dipartimento_peso p
Where
Pdg_preventivo_spe_det.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
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
         nvl( peso,1000),
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
        nvl( peso,1000),
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
        PDG_VARIAZIONE,
dipartimento_peso p
Where
Pdg_preventivo_spe_det.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
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
        nvl( peso,1000),
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
         nvl( peso,1000),
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
        PDG_VARIAZIONE,
dipartimento_peso p
Where
Pdg_preventivo_spe_det.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
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
        nvl( peso,1000),
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
--Nuova Gestione
Union
Select  Pdg_modulo_spese_gest.esercizio, -- DIPARTIMENTI INIZIALE
        nvl(peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,DIPARTIMENTO.DS_DIPARTIMENTO) DS_DIPARTIMENTO,
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
	v_linea_attivita_valida,
	DIPARTIMENTO,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
        dipartimento_peso p
Where
  V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_modulo_spese_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_modulo_spese_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_modulo_spese_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_modulo_spese_gest.CD_ELEMENTO_VOCE
and	Pdg_modulo_spese_gest.CD_CDR_ASSEGNATARIO	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_modulo_spese_gest.CD_LINEA_ATTIVITA 	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
and v_linea_attivita_valida.ESERCIZIO= Pdg_modulo_spese_gest.ESERCIZIO
And v_linea_attivita_valida.pg_progetto                      Is Not Null
And v_linea_attivita_valida.cd_programma                        = DIPARTIMENTO.CD_DIPARTIMENTO
and v_linea_attivita_valida.ESERCIZIO= p.esercizio(+)
AND v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+)
And 	Pdg_modulo_spese_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     pdg_modulo_spese_gest.CATEGORIA_DETTAGLIO       In ('DIR', 'STI')
GROUP BY Pdg_modulo_spese_gest.esercizio,
        nvl(peso,1000) ,
        Nvl(v_linea_attivita_valida.cd_programma, Null) ,
        nvl(p.DS_DIPARTIMENTO,DIPARTIMENTO.DS_DIPARTIMENTO) ,
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
Union  -- DIPARTIMENTI VARIAZIONI PIU  --12/01/2006 eliminata la union all
Select  Pdg_variazione_riga_gest.esercizio,
        nvl(peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,DIPARTIMENTO.DS_DIPARTIMENTO) DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO CDR,
        CDR.DS_CDR,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        Pdg_variazione_riga_gest.cd_elemento_voce,
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
        Pdg_variazione_riga_gest.ESERCIZIO,
        Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG              ,
        0 ini,
        Nvl(Sum(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0) VAR_PIU,
        0 VAR_MENO
From    Pdg_variazione_riga_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
	v_linea_attivita_valida,
	DIPARTIMENTO,
	PDG_VARIAZIONE,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
        dipartimento_peso p
Where
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_variazione_riga_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_variazione_riga_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_variazione_riga_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_variazione_riga_gest.CD_ELEMENTO_VOCE
and	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
and v_linea_attivita_valida.ESERCIZIO= Pdg_variazione_riga_gest.ESERCIZIO
And v_linea_attivita_valida.pg_progetto                      Is Not Null
And v_linea_attivita_valida.cd_programma                        = DIPARTIMENTO.CD_DIPARTIMENTO
and v_linea_attivita_valida.ESERCIZIO= p.esercizio(+)
AND v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+)
And     Pdg_variazione_riga_gest.ESERCIZIO              = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG      = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO In ('APP', 'APF')
And 	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
And     Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0) > 0
GROUP BY Pdg_variazione_riga_gest.esercizio,
        nvl(peso,1000) ,
        Nvl(v_linea_attivita_valida.cd_programma, Null) ,
        nvl(p.DS_DIPARTIMENTO,DIPARTIMENTO.DS_DIPARTIMENTO) ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO,
        CDR.DS_CDR,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        Pdg_variazione_riga_gest.cd_elemento_voce,
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
        Pdg_variazione_riga_gest.ESERCIZIO,
        Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG
Having Nvl(Sum(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0)!=0
Union   -- DIPARTIMENTI VARIAZIONI MENO --12/01/2006 eliminata la union all
Select  Pdg_variazione_riga_gest.esercizio,
        nvl(peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,DIPARTIMENTO.DS_DIPARTIMENTO) DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO CDR,
        CDR.DS_CDR,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        Pdg_variazione_riga_gest.cd_elemento_voce,
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
        Pdg_variazione_riga_gest.ESERCIZIO,
        Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG              ,
        0 ini,
        0 VAR_PIU,
        Nvl(Sum(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0) VAR_MENO
From    Pdg_variazione_riga_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
	v_linea_attivita_valida,
	DIPARTIMENTO,
	PDG_VARIAZIONE,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
        dipartimento_peso p
Where
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_variazione_riga_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_variazione_riga_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_variazione_riga_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_variazione_riga_gest.CD_ELEMENTO_VOCE
and	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
and v_linea_attivita_valida.ESERCIZIO= Pdg_variazione_riga_gest.ESERCIZIO
And v_linea_attivita_valida.pg_progetto                      Is Not Null
And v_linea_attivita_valida.cd_programma                        = DIPARTIMENTO.CD_DIPARTIMENTO
and v_linea_attivita_valida.ESERCIZIO= p.esercizio(+)
AND v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+)
And     Pdg_variazione_riga_gest.ESERCIZIO              = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG      = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO In ('APP', 'APF')
And 	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
And     Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0) < 0
GROUP BY Pdg_variazione_riga_gest.esercizio,
        nvl(peso,1000) ,
        Nvl(v_linea_attivita_valida.cd_programma, Null) ,
        nvl(p.DS_DIPARTIMENTO,DIPARTIMENTO.DS_DIPARTIMENTO) ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO,
        CDR.DS_CDR,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        Pdg_variazione_riga_gest.cd_elemento_voce,
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
        Pdg_variazione_riga_gest.ESERCIZIO,
        Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG
Having Nvl(Sum(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0)!=0
Union  -- SAC INIZIALE --12/01/2006 eliminata la union all
Select  Pdg_modulo_spese_gest.esercizio,
        nvl( peso,1000),
        'SAC',
         nvl(p.DS_DIPARTIMENTO,'Amministrazione dell''Ente') ds_dipartimento,
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
        null,
        null,
        Nvl(Sum(Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0) ini,
        0 VAR_PIU,
        0 VAR_MENO
From    Pdg_modulo_spese_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
dipartimento_peso p
Where
Pdg_modulo_spese_gest.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
  V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_modulo_spese_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_modulo_spese_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_modulo_spese_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_modulo_spese_gest.CD_ELEMENTO_VOCE
And     Pdg_modulo_spese_gest.CD_CDR_ASSEGNATARIO       = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And 	Pdg_modulo_spese_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     pdg_modulo_spese_gest.CATEGORIA_DETTAGLIO       In ('DIR', 'STI')
Group BY Pdg_modulo_spese_gest.esercizio,
        nvl( peso,1000),
        'SAC',
         nvl(p.DS_DIPARTIMENTO,'Amministrazione dell''Ente') ,
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
               Nvl(Pdg_modulo_spese_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0)!=0
Union  -- SAC VARIAZIONI PIU' --12/01/2006 eliminata la union all
Select  Pdg_variazione_riga_gest.esercizio,
        nvl( peso,1000),
        'SAC',
         nvl(p.DS_DIPARTIMENTO,'Amministrazione dell''Ente') ds_dipartimento,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO CDR,
        CDR.DS_CDR,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        Pdg_variazione_riga_gest.cd_elemento_voce,
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
        Pdg_variazione_riga_gest.ESERCIZIO,
        Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG,
        0 ini,
        Nvl(Sum(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0) VAR_PIU,
        0 VAR_MENO
From    Pdg_variazione_riga_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
        UNITA_ORGANIZZATIVA CDS,
	unita_organizzativa,
        PDG_VARIAZIONE,
dipartimento_peso p
Where
Pdg_variazione_riga_gest.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
  V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_variazione_riga_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_variazione_riga_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_variazione_riga_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_variazione_riga_gest.CD_ELEMENTO_VOCE
And     Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO    = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA                      = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And     Pdg_variazione_riga_gest.ESERCIZIO              = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG      = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO In ('APP', 'APF')
And 	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
And     Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0) > 0
Group BY Pdg_variazione_riga_gest.esercizio,
        nvl( peso,1000),
        'SAC',
         nvl(p.DS_DIPARTIMENTO,'Amministrazione dell''Ente') ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO,
        CDR.DS_CDR,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        Pdg_variazione_riga_gest.cd_elemento_voce,
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
        Pdg_variazione_riga_gest.ESERCIZIO,
        Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG
Having Nvl(Sum(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0)!=0
Union  -- SAC VARIAZIONI MENO  --12/01/2006 eliminata la union all
Select  Pdg_variazione_riga_gest.esercizio,
       nvl( peso,1000),
        'SAC',
         nvl(p.DS_DIPARTIMENTO,'Amministrazione dell''Ente') ds_dipartimento,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO CDR,
        CDR.DS_CDR,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        Pdg_variazione_riga_gest.cd_elemento_voce,
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
        Pdg_variazione_riga_gest.ESERCIZIO,
        Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG,
        0 ini,
        0 VAR_PIU,
        Nvl(Sum(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0) VAR_MENO
From    Pdg_variazione_riga_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
        UNITA_ORGANIZZATIVA CDS,
	unita_organizzativa,
        PDG_VARIAZIONE,
dipartimento_peso p
Where
Pdg_variazione_riga_gest.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_variazione_riga_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_variazione_riga_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_variazione_riga_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_variazione_riga_gest.CD_ELEMENTO_VOCE
And     Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO    = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA                      = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And     Pdg_variazione_riga_gest.ESERCIZIO              = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG      = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO In ('APP', 'APF')
And 	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
And     Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
        Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0) < 0
Group BY Pdg_variazione_riga_gest.esercizio,
       nvl( peso,1000),
        'SAC',
         nvl(p.DS_DIPARTIMENTO,'Amministrazione dell''Ente') ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO,
        CDR.DS_CDR,
        Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA,
        Pdg_variazione_riga_gest.cd_elemento_voce,
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
        Pdg_variazione_riga_gest.ESERCIZIO,
        Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG
Having Nvl(Sum(Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_INT, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_DECENTRATA_EST, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
               Nvl(Pdg_variazione_riga_gest.IM_SPESE_GEST_ACCENTRATA_EST, 0)), 0)!=0;
