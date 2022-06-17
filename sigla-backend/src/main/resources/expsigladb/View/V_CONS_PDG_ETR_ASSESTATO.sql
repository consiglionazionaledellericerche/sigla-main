--------------------------------------------------------
--  DDL for View V_CONS_PDG_ETR_ASSESTATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_ETR_ASSESTATO" ("ESERCIZIO", "PESO_DIP", "DIP", "DS_DIPARTIMENTO", "CDS", "DES_CDS", "UO", "DES_UO", "CDR", "DS_CDR", "CD_LINEA_ATTIVITA", "CD_ELEMENTO_VOCE", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "ESERCIZIO_PDG_VARIAZIONE", "PG_VARIAZIONE_PDG", "INI", "VAR_PIU", "VAR_MENO") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Piano di Gestione Entrate Assestato
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
Pdg_preventivo_etr_det.esercizio, -- DIPARTIMENTI INIZIALE
        nvl(p.peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,null) DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
        Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG              ,
        Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0)) INI,
        0 VAR_PIU,
        0 VAR_MENO
From    Pdg_preventivo_etr_det,
				elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
				v_linea_attivita_valida,
				UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
     		dipartimento_peso p
Where
  v_linea_attivita_valida.esercizio=  pdg_preventivo_etr_det.ESERCIZIO AND
  v_linea_attivita_valida.esercizio= p.esercizio(+) AND
  v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+) and
  v_linea_attivita_valida.pg_progetto                      Is Not Null and
  V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= pdg_preventivo_etr_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= pdg_preventivo_etr_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= pdg_preventivo_etr_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= pdg_preventivo_etr_det.CD_ELEMENTO_VOCE
and	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	pdg_preventivo_etr_det.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
And pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And PDG_PREVENTIVO_ETR_DET.STATO                    = 'Y'
And PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE Is NULL
And PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG        Is Null
Group BY pdg_preventivo_etr_det.esercizio,
        nvl(p.peso,1000) ,
        Nvl(v_linea_attivita_valida.cd_programma, Null) ,
        nvl(p.DS_DIPARTIMENTO,null) ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
        Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG
Having Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0))!=0
union -- DIPARTIMENTI VARIAZIONI PIU
Select  Pdg_preventivo_etr_det.esercizio,
        nvl(p.peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,null) DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
            Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG              ,
    0 INI,
        Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0)) VAR_PIU,
        0 VAR_MENO
From    Pdg_preventivo_etr_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
				v_linea_attivita_valida,
				UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
				PDG_VARIAZIONE,
     		dipartimento_peso p
Where
 v_linea_attivita_valida.ESERCIZIO                                  = pdg_preventivo_etr_det.ESERCIZIO  and
 v_linea_attivita_valida.pg_progetto                      Is Not Null and
 v_linea_attivita_valida.esercizio= p.esercizio(+) AND
 v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+) and
   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= pdg_preventivo_etr_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= pdg_preventivo_etr_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= pdg_preventivo_etr_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= pdg_preventivo_etr_det.CD_ELEMENTO_VOCE
and	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	pdg_preventivo_etr_det.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
And     PDG_PREVENTIVO_ETR_DET.STATO                    = 'Y'
And 	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And     PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO = 'APP'
And Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0) > 0
Group BY pdg_preventivo_etr_det.esercizio,
        nvl(p.peso,1000),
        Nvl(v_linea_attivita_valida.cd_programma, Null),
        nvl(p.DS_DIPARTIMENTO,null),
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA ,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
        Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG
Having Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0))!=0
union  -- DIPARTIMENTI VARIAZIONI MENO
Select  Pdg_preventivo_etr_det.esercizio,
        nvl(p.peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,null) DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
            Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG              ,
    0 INI,
        0 VAR_PIU,
        Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0)) VAR_MENO
From    Pdg_preventivo_etr_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
	v_linea_attivita_valida,
	PDG_VARIAZIONE,
        UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
     dipartimento_peso p
Where
   v_linea_attivita_valida.ESERCIZIO     = pdg_preventivo_etr_det.ESERCIZIO  and
   v_linea_attivita_valida.esercizio= p.esercizio(+) AND
   v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+) and
   v_linea_attivita_valida.pg_progetto                      Is Not Null and
  V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= pdg_preventivo_etr_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= pdg_preventivo_etr_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= pdg_preventivo_etr_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= pdg_preventivo_etr_det.CD_ELEMENTO_VOCE
and	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	pdg_preventivo_etr_det.CD_LINEA_ATTIVITA				= v_linea_attivita_valida.CD_LINEA_ATTIVITA
And     PDG_PREVENTIVO_ETR_DET.STATO                    = 'Y'
And 	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And     PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO = 'APP'
And Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0) < 0
Group BY pdg_preventivo_etr_det.esercizio,
        nvl(p.peso,1000),
        Nvl(v_linea_attivita_valida.cd_programma, Null) ,
        nvl(p.DS_DIPARTIMENTO,null) ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA ,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
        Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG
Having Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0))!=0
union -- SAC INIZIALE
Select  Pdg_preventivo_etr_det.esercizio,
         nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
            Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG              ,
    Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0)) INI,
        0 VAR_PIU,
        0 VAR_MENO
From    Pdg_preventivo_etr_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	unita_organizzativa,
        UNITA_ORGANIZZATIVA CDS,
dipartimento_peso p
Where
Pdg_preventivo_etr_det.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= pdg_preventivo_etr_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= pdg_preventivo_etr_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= pdg_preventivo_etr_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= pdg_preventivo_etr_det.CD_ELEMENTO_VOCE
And     PDG_PREVENTIVO_ETR_DET.STATO                    = 'Y'
And     PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE Is NULL
And     PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG        Is Null
And     PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And 	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
GROUP BY Pdg_preventivo_etr_det.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA ,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
        Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG
Having Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0))!=0
union -- SAC VARIAZIONI PIU'
Select  Pdg_preventivo_etr_det.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
            Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG              ,
    0 INI,
        Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0)) VAR_PIU,
        0 VAR_MENO
From    Pdg_preventivo_etr_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	unita_organizzativa,
        PDG_VARIAZIONE,
        UNITA_ORGANIZZATIVA CDS,
dipartimento_peso p
Where
pdg_preventivo_etr_det.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= pdg_preventivo_etr_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= pdg_preventivo_etr_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= pdg_preventivo_etr_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= pdg_preventivo_etr_det.CD_ELEMENTO_VOCE
And     PDG_PREVENTIVO_ETR_DET.STATO                    = 'Y'
And     PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And     PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And     PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO = 'APP'
And 	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0) > 0
Group BY Pdg_preventivo_etr_det.esercizio,
         nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
        Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG
Having Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0))!=0
union -- SAC VARIAZIONI MENO
Select  Pdg_preventivo_etr_det.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA CDR,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
            Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG              ,
    0 INI,
        0 VAR_PIU,
        Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0)) VAR_MENO
From    Pdg_preventivo_etr_det,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	unita_organizzativa,
        PDG_VARIAZIONE,
        UNITA_ORGANIZZATIVA CDS,
dipartimento_peso p
Where
pdg_preventivo_etr_det.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
 V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE		= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= pdg_preventivo_etr_det.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= pdg_preventivo_etr_det.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= pdg_preventivo_etr_det.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= pdg_preventivo_etr_det.CD_ELEMENTO_VOCE
And     PDG_PREVENTIVO_ETR_DET.STATO                    = 'Y'
And     PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And     PDG_PREVENTIVO_ETR_DET.ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
And     PDG_PREVENTIVO_ETR_DET.PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO = 'APP'
And 	pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0) < 0
Group BY Pdg_preventivo_etr_det.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_preventivo_etr_det.CD_CENTRO_RESPONSABILITA,
        CDR.DS_CDR,
        pdg_preventivo_ETR_det.CD_LINEA_ATTIVITA,
        Pdg_preventivo_etr_det.cd_elemento_voce,
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
        Pdg_preventivo_ETR_det.ESERCIZIO_PDG_VARIAZIONE,
        Pdg_preventivo_ETR_det.PG_VARIAZIONE_PDG
Having Sum(Nvl(Pdg_preventivo_etr_det.IM_RA_RCE, 0)+Nvl(Pdg_preventivo_etr_det.IM_RC_ESR, 0))!=0
--Nuova Gestione
union
Select  Pdg_modulo_entrate_gest.esercizio, -- DIPARTIMENTI INIZIALE
        nvl(p.peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,null) DS_DIPARTIMENTO,
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO CDR,
        CDR.DS_CDR,
        Pdg_modulo_entrate_gest.CD_LINEA_ATTIVITA,
        Pdg_modulo_entrate_gest.cd_elemento_voce,
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
        Nvl(Sum(Pdg_modulo_entrate_gest.IM_ENTRATA), 0) INI,
        0 VAR_PIU,
        0 VAR_MENO
From    Pdg_modulo_entrate_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
	v_linea_attivita_valida,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
     dipartimento_peso p
Where
	v_linea_attivita_valida.pg_progetto                      Is Not Null   and
  v_linea_attivita_valida.ESERCIZIO  = Pdg_modulo_entrate_gest.ESERCIZIO   and
 	v_linea_attivita_valida.esercizio= p.esercizio(+) AND
  v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+) and
  V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_modulo_entrate_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_modulo_entrate_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_modulo_entrate_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_modulo_entrate_gest.CD_ELEMENTO_VOCE
and	Pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_modulo_entrate_gest.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
And 	Pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     pdg_modulo_entrate_gest.CATEGORIA_DETTAGLIO     = 'DIR'
Group BY Pdg_modulo_entrate_gest.esercizio,
        nvl(p.peso,1000) ,
        Nvl( v_linea_attivita_valida.cd_programma, Null) ,
        nvl(p.DS_DIPARTIMENTO,null) ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA ,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO,
        CDR.DS_CDR,
        Pdg_modulo_entrate_gest.CD_LINEA_ATTIVITA,
        Pdg_modulo_entrate_gest.cd_elemento_voce,
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
Having Nvl(Sum(Pdg_modulo_entrate_gest.IM_ENTRATA), 0)!=0
union -- DIPARTIMENTI VARIAZIONI PIU
Select  Pdg_variazione_riga_gest.esercizio,
        nvl(p.peso,1000) PESO_DIP,
        Nvl( v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,null) DS_DIPARTIMENTO,
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
        0 INI,
        Nvl(Sum(Pdg_variazione_riga_gest.IM_ENTRATA), 0) VAR_PIU,
        0 VAR_MENO
From    Pdg_variazione_riga_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
		v_linea_attivita_valida,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
	PDG_VARIAZIONE,
     dipartimento_peso p
Where
	v_linea_attivita_valida.pg_progetto                      Is Not Null   and
  v_linea_attivita_valida.ESERCIZIO  = Pdg_variazione_riga_gest.ESERCIZIO   and
 	v_linea_attivita_valida.esercizio= p.esercizio(+) AND
  v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+) and
   V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_variazione_riga_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_variazione_riga_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_variazione_riga_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_variazione_riga_gest.CD_ELEMENTO_VOCE
and	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
And 	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Pdg_variazione_riga_gest.ESERCIZIO              = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG      = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO In ('APP', 'APF')
And     Nvl(Pdg_variazione_riga_gest.IM_ENTRATA, 0) > 0
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
Group BY Pdg_variazione_riga_gest.esercizio,
        nvl(p.peso,1000) ,
        Nvl(v_linea_attivita_valida.cd_programma, Null) ,
        nvl(p.DS_DIPARTIMENTO,null) ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA ,
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
Having Nvl(Sum(Pdg_variazione_riga_gest.IM_ENTRATA), 0)!=0
union  -- DIPARTIMENTI VARIAZIONI MENO
Select  Pdg_variazione_riga_gest.esercizio,
        nvl(p.peso,1000) PESO_DIP,
        Nvl(v_linea_attivita_valida.cd_programma, Null) DIP,
        nvl(p.DS_DIPARTIMENTO,null) DS_DIPARTIMENTO,
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
        0 INI,
        0 VAR_PIU,
        Nvl(Sum(Pdg_variazione_riga_gest.IM_ENTRATA), 0) VAR_MENO
From    Pdg_variazione_riga_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
	v_linea_attivita_valida,
	UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA CDS,
        CDR,
	PDG_VARIAZIONE,
     dipartimento_peso p
Where
	v_linea_attivita_valida.pg_progetto                      Is Not Null   and
  v_linea_attivita_valida.ESERCIZIO  = Pdg_variazione_riga_gest.ESERCIZIO   and
 	v_linea_attivita_valida.esercizio= p.esercizio(+) AND
  v_linea_attivita_valida.cd_programma= p.cd_dipartimento (+) and
  V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_variazione_riga_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_variazione_riga_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_variazione_riga_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_variazione_riga_gest.CD_ELEMENTO_VOCE
and	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= v_linea_attivita_valida.CD_CENTRO_RESPONSABILITA
and	Pdg_variazione_riga_gest.CD_LINEA_ATTIVITA	= v_linea_attivita_valida.CD_LINEA_ATTIVITA
And 	Pdg_variazione_riga_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     Pdg_variazione_riga_gest.ESERCIZIO              = PDG_VARIAZIONE.ESERCIZIO
And     Pdg_variazione_riga_gest.PG_VARIAZIONE_PDG      = PDG_VARIAZIONE.PG_VARIAZIONE_PDG
And     PDG_VARIAZIONE.STATO In ('APP', 'APF')
And     Nvl(Pdg_variazione_riga_gest.IM_ENTRATA, 0) < 0
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
Group BY Pdg_variazione_riga_gest.esercizio,
        nvl(p.peso,1000) ,
        Nvl(v_linea_attivita_valida.cd_programma, Null) ,
        nvl(p.DS_DIPARTIMENTO,null) ,
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA ,
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
Having Nvl(Sum(Pdg_variazione_riga_gest.IM_ENTRATA), 0)!=0
union -- SAC INIZIALE
Select  Pdg_modulo_entrate_gest.esercizio,
         nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
        CDS.CD_UNITA_ORGANIZZATIVA CDS,
        CDS.DS_UNITA_ORGANIZZATIVA DES_CDS,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA UO,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA DES_UO,
        Pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO CDR,
        CDR.DS_CDR,
        Pdg_modulo_entrate_gest.CD_LINEA_ATTIVITA,
        Pdg_modulo_entrate_gest.cd_elemento_voce,
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
        Nvl(Sum(Pdg_modulo_entrate_gest.IM_ENTRATA), 0) INI,
        0 VAR_PIU,
        0 VAR_MENO
From    Pdg_modulo_entrate_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	unita_organizzativa,
        UNITA_ORGANIZZATIVA CDS,
dipartimento_peso p
Where
Pdg_modulo_entrate_gest.esercizio= p.esercizio(+) AND
'SAC'= p.cd_dipartimento (+) and
  V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE	= elemento_voce.ID_CLASSIFICAZIONE
and	elemento_voce.ESERCIZIO 			= Pdg_modulo_entrate_gest.ESERCIZIO
and	elemento_voce.TI_APPARTENENZA 		  	= Pdg_modulo_entrate_gest.TI_APPARTENENZA
AND	elemento_voce.TI_GESTIONE			= Pdg_modulo_entrate_gest.TI_GESTIONE
and	elemento_voce.CD_ELEMENTO_VOCE		  	= Pdg_modulo_entrate_gest.CD_ELEMENTO_VOCE
And     Pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO = CDR.CD_CENTRO_RESPONSABILITA
And     CDR.CD_UNITA_ORGANIZZATIVA = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
And 	Pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO	= CDR.CD_CENTRO_RESPONSABILITA
And 	CDR.CD_UNITA_ORGANIZZATIVA			= UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
And 	UNITA_ORGANIZZATIVA.CD_UNITA_PADRE		= CDS.CD_UNITA_ORGANIZZATIVA
And     unita_organizzativa.CD_TIPO_UNITA = 'SAC'
And     pdg_modulo_entrate_gest.CATEGORIA_DETTAGLIO     = 'DIR'
GROUP BY Pdg_modulo_entrate_gest.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
        CDS.CD_UNITA_ORGANIZZATIVA,
        CDS.DS_UNITA_ORGANIZZATIVA ,
        UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
        UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA,
        Pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO,
        CDR.DS_CDR,
        Pdg_modulo_entrate_gest.CD_LINEA_ATTIVITA,
        Pdg_modulo_entrate_gest.cd_elemento_voce,
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
Having Nvl(Sum(Pdg_modulo_entrate_gest.IM_ENTRATA), 0)!=0
union -- SAC VARIAZIONI PIU'
Select  Pdg_variazione_riga_gest.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
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
        0 INI,
        Nvl(Sum(Pdg_variazione_riga_gest.IM_ENTRATA), 0) VAR_PIU,
        0 VAR_MENO
From    Pdg_variazione_riga_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	unita_organizzativa,
        PDG_VARIAZIONE,
        UNITA_ORGANIZZATIVA CDS,
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
And     Nvl(Pdg_variazione_riga_gest.IM_ENTRATA, 0) > 0
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
Group BY Pdg_variazione_riga_gest.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
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
Having Nvl(Sum(Pdg_variazione_riga_gest.IM_ENTRATA), 0)!=0
union -- SAC VARIAZIONI MENO
Select  Pdg_variazione_riga_gest.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
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
        0 INI,
        0 VAR_PIU,
        Nvl(Sum(Pdg_variazione_riga_gest.IM_ENTRATA), 0) VAR_MENO
From    Pdg_variazione_riga_gest,
	elemento_voce,
        V_CLASSIFICAZIONE_VOCI,
        cdr,
	unita_organizzativa,
        PDG_VARIAZIONE,
        UNITA_ORGANIZZATIVA CDS,
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
And     Nvl(Pdg_variazione_riga_gest.IM_ENTRATA, 0) < 0
And     Pdg_variazione_riga_gest.CATEGORIA_DETTAGLIO    = 'DIR'
Group BY Pdg_variazione_riga_gest.esercizio,
        nvl( peso,1000),
        'SAC',
        'Amministrazione dell''Ente',
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
Having Nvl(Sum(Pdg_variazione_riga_gest.IM_ENTRATA), 0)!=0;
