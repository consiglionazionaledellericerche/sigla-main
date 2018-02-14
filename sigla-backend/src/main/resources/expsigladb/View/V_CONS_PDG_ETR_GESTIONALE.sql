--------------------------------------------------------
--  DDL for View V_CONS_PDG_ETR_GESTIONALE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_ETR_GESTIONALE" ("ESERCIZIO", "PESO_DIP", "CD_DIPARTIMENTO", "CD_CENTRO_SPESA", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "CD_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "CD_ELEMENTO_VOCE", "CD_LINEA_ATTIVITA", "PG_MODULO", "CD_MODULO", "CD_TIPO_MODULO", "PG_COMMESSA", "CD_COMMESSA", "PG_PROGETTO", "CD_PROGETTO", "TOT_ENT_IST_A1", "VARIAZIONI_POSITIVE_DEC_INT", "VARIAZIONI_POSITIVE_DEC_EST", "VARIAZIONI_POSITIVE_ACC_INT", "VARIAZIONI_POSITIVE_ACC_EST", "VARIAZIONI_NEGATIVE_DEC_INT", "VARIAZIONI_NEGATIVE_DEC_EST", "VARIAZIONI_NEGATIVE_ACC_INT", "VARIAZIONI_NEGATIVE_ACC_EST") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Piano di Gestione Gestionale Entrate
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
ESERCIZIO, PESO_DIP, CD_DIPARTIMENTO, CD_CENTRO_SPESA, CD_UNITA_ORGANIZZATIVA,
       CD_CENTRO_RESPONSABILITA, CD_CLASSIFICAZIONE, NR_LIVELLO,
       CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
       CD_ELEMENTO_VOCE, CD_LINEA_ATTIVITA,
       PG_MODULO, CD_MODULO, CD_TIPO_MODULO, PG_COMMESSA, CD_COMMESSA, PG_PROGETTO, CD_PROGETTO,
       Nvl(Sum(TOT_ENT_IST_A1), 0) TOT_ENT_IST_A1,
       Nvl(Sum(VARIAZIONI_POSITIVE_DEC_INT), 0) VARIAZIONI_POSITIVE_DEC_INT,
       Nvl(Sum(VARIAZIONI_POSITIVE_DEC_EST), 0) VARIAZIONI_POSITIVE_DEC_EST,
       Nvl(Sum(VARIAZIONI_POSITIVE_ACC_INT), 0) VARIAZIONI_POSITIVE_ACC_INT,
       Nvl(Sum(VARIAZIONI_POSITIVE_ACC_EST), 0) VARIAZIONI_POSITIVE_ACC_EST,
       Nvl(Sum(VARIAZIONI_NEGATIVE_DEC_INT), 0) VARIAZIONI_NEGATIVE_DEC_INT,
       Nvl(Sum(VARIAZIONI_NEGATIVE_DEC_EST), 0) VARIAZIONI_NEGATIVE_DEC_EST,
       Nvl(Sum(VARIAZIONI_NEGATIVE_ACC_INT), 0) VARIAZIONI_NEGATIVE_ACC_INT,
       Nvl(Sum(VARIAZIONI_NEGATIVE_ACC_EST), 0) VARIAZIONI_NEGATIVE_ACC_EST
From
(Select pdg_modulo_entrate_gest.ESERCIZIO, -- DIPARTIMENTI INIZIALE
         Decode(unita_organizzativa.CD_TIPO_UNITA, 'SAC', '13',to_char(nvl(p.peso,1000))) PESO_DIP,
         Decode(unita_organizzativa.CD_TIPO_UNITA, 'SAC', 'SAC',Nvl(prog.cd_dipartimento, Null)) CD_DIPARTIMENTO,
        unita_organizzativa.CD_UNITA_PADRE CD_CENTRO_SPESA,
        unita_organizzativa.CD_UNITA_ORGANIZZATIVA,
        pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO CD_CENTRO_RESPONSABILITA,
        v_classificazione_voci.CD_CLASSIFICAZIONE,
        v_classificazione_voci.NR_LIVELLO,
        v_classificazione_voci.CD_LIVELLO1,
        v_classificazione_voci.CD_LIVELLO2,
        v_classificazione_voci.CD_LIVELLO3,
        v_classificazione_voci.CD_LIVELLO4,
        v_classificazione_voci.CD_LIVELLO5,
        v_classificazione_voci.CD_LIVELLO6,
        v_classificazione_voci.CD_LIVELLO7,
        pdg_modulo_entrate_gest.CD_ELEMENTO_VOCE,
        pdg_modulo_entrate_gest.CD_LINEA_ATTIVITA,
        modu.PG_PROGETTO PG_MODULO, modu.CD_PROGETTO CD_MODULO, modu.CD_TIPO_PROGETTO CD_TIPO_MODULO,
        comm.PG_PROGETTO PG_COMMESSA, comm.CD_PROGETTO CD_COMMESSA,
        prog.PG_PROGETTO, prog.CD_PROGETTO,
        Nvl(pdg_modulo_entrate_gest.IM_ENTRATA, 0) TOT_ENT_IST_A1,
        0 VARIAZIONI_POSITIVE_DEC_INT,
        0 VARIAZIONI_POSITIVE_DEC_EST,
        0 VARIAZIONI_POSITIVE_ACC_INT,
        0 VARIAZIONI_POSITIVE_ACC_EST,
        0 VARIAZIONI_NEGATIVE_DEC_INT,
        0 VARIAZIONI_NEGATIVE_DEC_EST,
        0 VARIAZIONI_NEGATIVE_ACC_INT,
        0 VARIAZIONI_NEGATIVE_ACC_EST
From    pdg_modulo_entrate_gest,
        V_CLASSIFICAZIONE_VOCI ,
	UNITA_ORGANIZZATIVA,
        CDR,
	LINEA_ATTIVITA,
	ELEMENTO_VOCE,
	PROGETTO_GEST PROG,
	PROGETTO_GEST COMM,
	PROGETTO_GEST MODU,
	dipartimento_peso p
Where
 PROG.esercizio= p.esercizio(+) AND
 PROG.cd_dipartimento= p.cd_dipartimento (+) and
  -- join tra pdg_modulo_entrate_gest e ELEMENTO_VOCE
        pdg_modulo_entrate_gest.ESERCIZIO                 = elemento_voce.ESERCIZIO
And     pdg_modulo_entrate_gest.TI_APPARTENENZA           = elemento_voce.TI_APPARTENENZA
And     pdg_modulo_entrate_gest.TI_GESTIONE               = elemento_voce.TI_GESTIONE
And     pdg_modulo_entrate_gest.CD_ELEMENTO_VOCE          = elemento_voce.CD_ELEMENTO_VOCE
And     pdg_modulo_entrate_gest.CATEGORIA_DETTAGLIO       = 'DIR'
-- join tra ELEMENTO_VOCE e V_CLASSIFICAZIONE_VOCI
And     elemento_voce.ID_CLASSIFICAZIONE                = v_classificazione_voci.ID_CLASSIFICAZIONE
-- join tra pdg_modulo_entrate_gest e LINEA_ATTIVITA
And     pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO	= linea_attivita.CD_CENTRO_RESPONSABILITA
And	pdg_modulo_entrate_gest.CD_LINEA_ATTIVITA	        = linea_attivita.CD_LINEA_ATTIVITA
And     linea_attivita.pg_progetto                      Is Not Null
-- join tra LINEA_ATTIVITA e MODULO (PROGETTO)
And	linea_attivita.PG_PROGETTO			= modu.PG_PROGETTO
And     modu.ESERCIZIO                                  = pdg_modulo_entrate_gest.ESERCIZIO
-- join tra MODULO (PROGETTO) e COMMESSA (PROGETTO)
And	modu.ESERCIZIO_PROGETTO_PADRE			= comm.ESERCIZIO
And	modu.PG_PROGETTO_PADRE				= comm.PG_PROGETTO
-- join tra COMMESSA (PROGETTO) e PROGETTO (PROGETTO)
And	comm.ESERCIZIO_PROGETTO_PADRE                   = prog.ESERCIZIO
And	comm.PG_PROGETTO_PADRE                          = prog.PG_PROGETTO
-- join tra pdg_modulo_entrate_gest e CDR
And 	pdg_modulo_entrate_gest.CD_CDR_ASSEGNATARIO	= cdr.CD_CENTRO_RESPONSABILITA
-- join tra CDR e UNITA_ORGANIZZATIVA
And 	cdr.CD_UNITA_ORGANIZZATIVA			= unita_organizzativa.CD_UNITA_ORGANIZZATIVA
Union All-- VARIAZIONI PIU'
Select  v_cons_pdg_var_gestionale.ESERCIZIO,
        v_cons_pdg_var_gestionale.PESO_DIP,
        v_cons_pdg_var_gestionale.CD_DIPARTIMENTO,
        v_cons_pdg_var_gestionale.CD_CENTRO_SPESA,
        v_cons_pdg_var_gestionale.CD_UNITA_ORGANIZZATIVA,
        v_cons_pdg_var_gestionale.CD_CENTRO_RESPONSABILITA,
        v_cons_pdg_var_gestionale.CD_CLASSIFICAZIONE,
        v_cons_pdg_var_gestionale.NR_LIVELLO,
        v_cons_pdg_var_gestionale.CD_LIVELLO1,
        v_cons_pdg_var_gestionale.CD_LIVELLO2,
        v_cons_pdg_var_gestionale.CD_LIVELLO3,
        v_cons_pdg_var_gestionale.CD_LIVELLO4,
        v_cons_pdg_var_gestionale.CD_LIVELLO5,
        v_cons_pdg_var_gestionale.CD_LIVELLO6,
        v_cons_pdg_var_gestionale.CD_LIVELLO7,
        v_cons_pdg_var_gestionale.CD_ELEMENTO_VOCE,
        v_cons_pdg_var_gestionale.CD_LINEA_ATTIVITA,
        v_cons_pdg_var_gestionale.PG_MODULO,
        v_cons_pdg_var_gestionale.CD_MODULO,
        v_cons_pdg_var_gestionale.CD_TIPO_MODULO,
        v_cons_pdg_var_gestionale.PG_COMMESSA,
        v_cons_pdg_var_gestionale.CD_COMMESSA,
        v_cons_pdg_var_gestionale.PG_PROGETTO,
        v_cons_pdg_var_gestionale.CD_PROGETTO,
        0 TOT_ENT_IST_A1,
        v_cons_pdg_var_gestionale.VARIAZIONI_POSITIVE_DEC_INT,
        v_cons_pdg_var_gestionale.VARIAZIONI_POSITIVE_DEC_EST,
        v_cons_pdg_var_gestionale.VARIAZIONI_POSITIVE_ACC_INT,
        v_cons_pdg_var_gestionale.VARIAZIONI_POSITIVE_ACC_EST,
        v_cons_pdg_var_gestionale.VARIAZIONI_NEGATIVE_DEC_INT,
        v_cons_pdg_var_gestionale.VARIAZIONI_NEGATIVE_DEC_EST,
        v_cons_pdg_var_gestionale.VARIAZIONI_NEGATIVE_ACC_INT,
        v_cons_pdg_var_gestionale.VARIAZIONI_NEGATIVE_ACC_EST
From    V_CONS_PDG_VAR_GESTIONALE
Where   v_cons_pdg_var_gestionale.TI_GESTIONE = 'E'
And     v_cons_pdg_var_gestionale.STATO In ('APP', 'APF'))
Group By ESERCIZIO, PESO_DIP, CD_DIPARTIMENTO, CD_CENTRO_SPESA, CD_UNITA_ORGANIZZATIVA,
       CD_CENTRO_RESPONSABILITA, CD_CLASSIFICAZIONE, NR_LIVELLO,
       CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
       CD_ELEMENTO_VOCE, CD_LINEA_ATTIVITA,
       PG_MODULO, CD_MODULO, CD_TIPO_MODULO, PG_COMMESSA, CD_COMMESSA, PG_PROGETTO, CD_PROGETTO
Having Nvl(Sum(TOT_ENT_IST_A1), 0) != 0 Or
       Nvl(Sum(VARIAZIONI_POSITIVE_DEC_INT), 0) != 0 Or Nvl(Sum(VARIAZIONI_POSITIVE_DEC_EST), 0) != 0 Or
       Nvl(Sum(VARIAZIONI_POSITIVE_ACC_INT), 0) != 0 Or Nvl(Sum(VARIAZIONI_POSITIVE_ACC_EST), 0) != 0 Or
       Nvl(Sum(VARIAZIONI_NEGATIVE_DEC_INT), 0) != 0 Or Nvl(Sum(VARIAZIONI_NEGATIVE_DEC_EST), 0) != 0 Or
       Nvl(Sum(VARIAZIONI_NEGATIVE_ACC_INT), 0) != 0 Or Nvl(Sum(VARIAZIONI_NEGATIVE_ACC_EST), 0) != 0;
