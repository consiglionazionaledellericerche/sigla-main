--------------------------------------------------------
--  DDL for Package CONTROLLO_IMPORTI
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CONTROLLO_IMPORTI" As

/*
--IM_STANZ_INIZIALE_A1
VARIAZIONI_PIU                  -- NON CI SONO ANCORA (variazioni al gestionale)
VARIAZIONI_MENO                 -- NON CI SONO ANCORA (variazioni al gestionale)
IM_STANZ_INIZIALE_CASSA         -- INUTILE
VARIAZIONI_PIU_CASSA            -- INUTILE
VARIAZIONI_MENO_CASSA           -- INUTILE
--IM_OBBL_ACC_COMP
--IM_STANZ_RES_IMPROPRIO
--VAR_PIU_STANZ_RES_IMP
--VAR_MENO_STANZ_RES_IMP
--IM_OBBL_RES_IMP
--IM_OBBL_RES_PRO
VAR_PIU_OBBL_RES_PRO            -- NON CI SONO ANCORA (variazioni ai residui propri)
VAR_MENO_OBBL_RES_PRO           -- NON CI SONO ANCORA (variazioni ai residui propri)
--IM_MANDATI_REVERSALI_PRO
--IM_MANDATI_REVERSALI_IMP
IM_PAGAMENTI_INCASSI            -- DA FARE
*/

-- ATTENZIONE !!!!! ESTRAE IL VALORE TRANNE CHE PER 999.000.000 (non è facilmente recuperabile) !!!!
Function IM_STANZ_INIZIALE_A1 (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function IM_STANZ_RES_IMPROPRIO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function VAR_PIU_STANZ_RES_IMP  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function VAR_MENO_STANZ_RES_IMP  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function IM_OBBL_ACC_COMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function IM_OBBL_RES_IMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function IM_OBBL_RES_PRO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function IM_MANDATI_REVERSALI_PRO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function IM_MANDATI_REVERSALI_IMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

End;
