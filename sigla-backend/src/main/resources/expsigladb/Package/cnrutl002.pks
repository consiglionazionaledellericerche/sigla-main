CREATE OR REPLACE PACKAGE CNRUTL002 IS
--
-- CNRUTL002 - Package di utilita', sia per il martello 2006 che per la view v_sit_gae_residui_spesa delle stampe
--             ATTENZIONE !!!!! ESTRAE IL VALORE TRANNE CHE PER 999.000.000 (non ? facilmente recuperabile) !!!!
-- Date: 10/04/2006
-- Version: 1.0
--
-- Dependency:
--
-- History:
-- Date: 10/04/2006
-- Version: 1.0
-- Creazione
--
-- Date: 14/07/2006
-- Version: 2.13
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
Function IM_STANZ_INIZIALE_A1 (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function IM_STANZ_INIZIALE_CASSA (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                  INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                                  INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                                  INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                                  INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                                  INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                  INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                                  INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function RF_PREV_DEF_CASSA       (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                  INDIP           DIPARTIMENTO.CD_DIPARTIMENTO%Type,
                                  INCDS           UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA%Type,
                                  INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                  INLIV1          CLASSIFICAZIONE_VOCI.CD_LIVELLO1%Type,
                                  INLIV2          CLASSIFICAZIONE_VOCI.CD_LIVELLO2%Type,
                                  INLIV3          CLASSIFICAZIONE_VOCI.CD_LIVELLO3%Type,
                                  INLIV4          CLASSIFICAZIONE_VOCI.CD_LIVELLO4%Type,
                                  INLIV5          CLASSIFICAZIONE_VOCI.CD_LIVELLO5%Type,
                                  INLIV6          CLASSIFICAZIONE_VOCI.CD_LIVELLO6%Type,
                                  INLIV7          CLASSIFICAZIONE_VOCI.CD_LIVELLO7%Type) Return NUMBER;

Function RF_IM_STANZ_INIZIALE_A1 (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                  INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                                  INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                                  INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                                  INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                                  INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                  INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                                  INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function VARIAZIONI_PIU       (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function RF_VARIAZIONI_PIU    (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;


Function VARIAZIONI_PIU       (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type,
                               DA_DATA         DATE,
                               A_DATA          DATE) Return NUMBER;

Function VARIAZIONI_MENO      (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function RF_VARIAZIONI_MENO   (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function VARIAZIONI_MENO      (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type,
                               DA_DATA         DATE,
                               A_DATA          DATE) Return NUMBER;

Function VARIAZIONI_CON_SEGNO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type,
                               SEGNO           CHAR,
                               DA_DATA         DATE,
                               A_DATA          DATE) Return NUMBER;

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

Function IM_OBBL_ACC_COMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                           INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                           INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                           INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                           INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                           INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                           INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                           INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type,
                           DA_DATA         DATE,
                           A_DATA          DATE) Return NUMBER;

Function IM_OBBL_RES_IMP      (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function IM_OBBL_RES_PRO      (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function VAR_PIU_OBBL_RES_PRO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function VAR_MENO_OBBL_RES_PRO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
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

Function VAR_STANZ_RES_MENO (P_ESE       IN NUMBER,
                             P_ESE_RES   IN NUMBER,
                             P_CDR       IN VARCHAR2,
                             P_GAE       IN VARCHAR2,
                             P_APP       IN CHAR,
                             P_GEST      IN CHAR,
                             P_VOCE      IN VARCHAR2,
                             P_TIPO      IN VARCHAR2) Return NUMBER;

Function LIQUIDATO_PRO (P_ESE       IN NUMBER,
                        P_ESE_RES   IN NUMBER,
                        P_CDR       IN VARCHAR2,
                        P_GAE       IN VARCHAR2,
                        P_APP       IN CHAR,
                        P_GEST      IN CHAR,
                        P_VOCE      IN VARCHAR2) RETURN NUMBER;

Function RESIDUI_IMPROPRI (P_ESE       IN NUMBER,
                           P_ESE_RES   IN NUMBER,
                           P_CDR       IN VARCHAR2,
                           P_GAE       IN VARCHAR2,
                           P_APP       IN CHAR,
                           P_GEST      IN CHAR,
                           P_VOCE      IN VARCHAR2,
                           P_RIB       IN CHAR) RETURN NUMBER;

Function RESIDUI_IMPROPRI_LIQ (P_ESE       IN NUMBER,
                               P_ESE_RES   IN NUMBER,
                               P_CDR       IN VARCHAR2,
                               P_GAE       IN VARCHAR2,
                               P_APP       IN CHAR,
                               P_GEST      IN CHAR,
                               P_VOCE      IN VARCHAR2,
                               P_RIB       In CHAR) RETURN NUMBER;

Function RESIDUI_IMPROPRI_PAG (P_ESE       IN NUMBER,
                               P_ESE_RES   IN NUMBER,
                               P_CDR       IN VARCHAR2,
                               P_GAE       IN VARCHAR2,
                               P_APP       IN CHAR,
                               P_GEST      IN CHAR,
                               P_VOCE      IN VARCHAR2,
                               P_RIB       In CHAR) RETURN NUMBER;

Function RF_IM_OBBL_ACC_COMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                              INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                              INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                              INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                              INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                              INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                              INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                              INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function RF_IM_OBBL_RES_PRO  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                              INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                              INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                              INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                              INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                              INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                              INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                              INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function RF_IM_MANDATI_REVERSALI_PRO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                      INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                                      INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                                      INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                                      INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                                      INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                      INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                                      INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

Function RF_FULL_COST_AC (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                          INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                          INDIP           DIPARTIMENTO.CD_DIPARTIMENTO%Type,
                          INLIV1          CLASSIFICAZIONE_VOCI.CD_LIVELLO1%Type,
                          INLIV2          CLASSIFICAZIONE_VOCI.CD_LIVELLO1%Type) Return NUMBER;

Function IM_MANDATI_PER_IMPEGNO (INCDS           UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA%Type,
                                 INES            OBBLIGAZIONE.ESERCIZIO%Type,
                                 INESRES         OBBLIGAZIONE.ESERCIZIO_ORIGINALE%Type,
                                 INPGOBB         OBBLIGAZIONE.PG_OBBLIGAZIONE%Type,
                                 INPGOBBSCAD     OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO%Type) Return NUMBER;


Function IM_VINCOLI(INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                    INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                    INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                    INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                    INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                    INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                    INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type) Return NUMBER;

End CNRUTL002;


CREATE OR REPLACE PACKAGE BODY CNRUTL002 IS

Function IM_STANZ_RES_IMPROPRIO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                 INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                                 INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                                 INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                                 INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                                 INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                 INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                                 INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is
  VALORE NUMBER:=0;
   VALORE_singolo NUMBER:=0;
   recElementoVoceOld elemento_voce%rowtype;
   recElementoVoceNew elemento_voce%rowtype;
   nuovo_pdg parametri_cnr.fl_nuovo_pdg%type;
   old_nuovo_pdg parametri_cnr.fl_nuovo_pdg%type;
   INVOCE_agg VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type;
   INGEST_agg VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type;
   INAPP_agg VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type;
BEGIN

If INGEST = 'S' And INCDR != cnrctb020.getCdCdrEnte And INES > Nvl(INESRES, ines-1) And INES >= 2006 Then

  select fl_nuovo_pdg into nuovo_pdg from parametri_cnr
  where
  esercizio = INES;
  select fl_nuovo_pdg into old_nuovo_pdg from parametri_cnr
  where
  esercizio = INES-1;
  if(nuovo_pdg='Y') then
    for recElementoVoceOld in(
        Select elemento_voce.*
        From   ass_evold_evnew, elemento_voce
        Where  ass_evold_evnew.esercizio_old(+) = ELEMENTO_VOCE.ESERCIZIO
        And    ass_evold_evnew.ti_appartenenza_old(+) = ELEMENTO_VOCE.TI_APPARTENENZA
        And    ass_evold_evnew.ti_gestione_old (+)= ELEMENTO_VOCE.TI_GESTIONE
        And    ass_evold_evnew.cd_elemento_voce_old(+) = ELEMENTO_VOCE.CD_ELEMENTO_VOCE
        aND    ((ass_evold_evnew.esercizio_new = INES
        And    ass_evold_evnew.ti_appartenenza_new = INAPP
        And    ass_evold_evnew.ti_gestione_new = INGEST
        And    ass_evold_evnew.cd_elemento_voce_new = INVOCE)
        or (ELEMENTO_VOCE.ESERCIZIO         = INES
        and  ELEMENTO_VOCE.TI_APPARTENENZA  = INAPP
        and  ELEMENTO_VOCE.TI_GESTIONE      = INGEST
        and ELEMENTO_VOCE.CD_ELEMENTO_VOCE = INVOCE  and not exists
          (select 1 from ass_evold_evnew where
                 ass_evold_evnew.esercizio_new = ELEMENTO_VOCE.ESERCIZIO
          And    ass_evold_evnew.ti_appartenenza_new = ELEMENTO_VOCE.TI_APPARTENENZA
          And    ass_evold_evnew.ti_gestione_new = ELEMENTO_VOCE.TI_GESTIONE
          And    ass_evold_evnew.cd_elemento_voce_new = ELEMENTO_VOCE.CD_ELEMENTO_VOCE)))) loop

        If recElementoVoceOld.TI_APPARTENENZA is not null Then
           INAPP_agg := recElementoVoceOld.TI_APPARTENENZA;
           INGEST_agg := recElementoVoceOld.TI_GESTIONE;
           INVOCE_agg:= recElementoVoceOld.CD_ELEMENTO_VOCE;
        End If;
        if(old_nuovo_pdg='N') then
          Select Sum(TOT_IM_RESIDUI_RICOSTRUITI) +
                Sum(tot_IM_STANZ_INIZIALE_A1) + Sum(tot_VARIAZIONI_PIU) - Sum(tot_VARIAZIONI_MENO) - Sum(tot_IM_OBBL_ACC_COMP) +
                Sum(TOT_IM_STANZ_RES_IMPROPRIO) + Sum(TOT_VAR_PIU_STANZ_RES_IMP) - Sum(TOT_VAR_MENO_STANZ_RES_IMP) - Sum(TOT_IM_OBBL_RES_IMP) +
                Sum(TOT_VAR_MENO_OBBL_RES_PRO) - Sum(TOT_VAR_PIU_OBBL_RES_PRO)
          Into   VALORE_SINGOLO
          From   v_disp_res_improprie
          Where  ESERCIZIO                = ines-1 And
                 ESERCIZIO_RES            = Nvl(inesres, ESERCIZIO_RES)  And
                 CD_CENTRO_RESPONSABILITA = INCDR     And
                 CD_LINEA_ATTIVITA        = Nvl(INLA, CD_LINEA_ATTIVITA) And
                 TI_APPARTENENZA          = INAPP_agg      And
                 TI_GESTIONE              = INGEST_agg     And
                 CD_VOCE IN( Select CD_VOCE
                                 From   VOCE_F
                                 Where  ESERCIZIO        = INES-1 And
                                        TI_APPARTENENZA  = INAPP_agg And
                                        TI_GESTIONE      = INGEST_agg And
                                        CD_ELEMENTO_VOCE = INVOCE_agg);
      else
          Select Sum(TOT_IM_RESIDUI_RICOSTRUITI) +
              Sum(tot_IM_STANZ_INIZIALE_A1) + Sum(tot_VARIAZIONI_PIU) - Sum(tot_VARIAZIONI_MENO) - Sum(tot_IM_OBBL_ACC_COMP) +
              Sum(TOT_IM_STANZ_RES_IMPROPRIO) + Sum(TOT_VAR_PIU_STANZ_RES_IMP) - Sum(TOT_VAR_MENO_STANZ_RES_IMP) - Sum(TOT_IM_OBBL_RES_IMP) +
              Sum(TOT_VAR_MENO_OBBL_RES_PRO) - Sum(TOT_VAR_PIU_OBBL_RES_PRO)
        Into   VALORE
        From   v_disp_res_improprie
        Where  ESERCIZIO                = ines-1 And
               ESERCIZIO_RES            = Nvl(inesres, ESERCIZIO_RES)  And
               CD_CENTRO_RESPONSABILITA = INCDR     And
               CD_LINEA_ATTIVITA        = Nvl(INLA, CD_LINEA_ATTIVITA) And
               TI_APPARTENENZA          = INAPP      And
               TI_GESTIONE              = INGEST   And
               CD_VOCE IN( INELVOCE);
      end if;
      VALORE:=VALORE+nvl(VALORE_SINGOLO,0);
    end loop;
  else
      Select Sum(TOT_IM_RESIDUI_RICOSTRUITI) +
              Sum(tot_IM_STANZ_INIZIALE_A1) + Sum(tot_VARIAZIONI_PIU) - Sum(tot_VARIAZIONI_MENO) - Sum(tot_IM_OBBL_ACC_COMP) +
              Sum(TOT_IM_STANZ_RES_IMPROPRIO) + Sum(TOT_VAR_PIU_STANZ_RES_IMP) - Sum(TOT_VAR_MENO_STANZ_RES_IMP) - Sum(TOT_IM_OBBL_RES_IMP) +
              Sum(TOT_VAR_MENO_OBBL_RES_PRO) - Sum(TOT_VAR_PIU_OBBL_RES_PRO)
        Into   VALORE
        From   v_disp_res_improprie
        Where  ESERCIZIO                = ines-1 And
               ESERCIZIO_RES            = Nvl(inesres, ESERCIZIO_RES)  And
               CD_CENTRO_RESPONSABILITA = INCDR     And
               CD_LINEA_ATTIVITA        = Nvl(INLA, CD_LINEA_ATTIVITA) And
               TI_APPARTENENZA          = INAPP      And
               TI_GESTIONE              = INGEST     And
               CD_VOCE In (Select CD_VOCE
                           From   VOCE_F
                           Where  ESERCIZIO        = INES And
                                  TI_APPARTENENZA  = INAPP And
                                  TI_GESTIONE      = INGEST And
                                  CD_ELEMENTO_VOCE = INELVOCE);
  End If;
End If;
Return  Nvl(VALORE, 0);

END;

Function VAR_PIU_STANZ_RES_IMP  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is
VALORE NUMBER;

BEGIN

  Select NVL(SUM(Abs(IM_VARIAZIONE)), 0)
  Into   VALORE
  From   VAR_STANZ_RES_RIGA VR, VAR_STANZ_RES VT
  Where  VR.ESERCIZIO = INES And
         VR.ESERCIZIO_RES = Nvl(INESRES, VR.ESERCIZIO_RES) And
         VR.ESERCIZIO_VOCE = INES And
         VR.CD_CDR = INCDR AND
         VR.CD_LINEA_ATTIVITA = Nvl(INLA, VR.CD_LINEA_ATTIVITA)  AND
         VR.TI_APPARTENENZA = INAPP AND
         VR.TI_GESTIONE = INGEST AND
         VR.CD_VOCE = Nvl(INVOCE, VR.CD_VOCE) And
         VR.CD_ELEMENTO_VOCE = Nvl(INELVOCE, VR.CD_ELEMENTO_VOCE) And
         VR.IM_VARIAZIONE > 0 And
         VT.ESERCIZIO     = VR.ESERCIZIO And
         VT.PG_VARIAZIONE = VR.PG_VARIAZIONE And
         VT.STATO = 'APP';

Return  Nvl(VALORE, 0);

END;

Function VAR_MENO_STANZ_RES_IMP  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is
VALORE NUMBER;

BEGIN

  Select NVL(SUM(Abs(IM_VARIAZIONE)), 0)
  Into   VALORE
  From   VAR_STANZ_RES_RIGA VR, VAR_STANZ_RES VT
  Where  VR.ESERCIZIO = INES And
         VR.ESERCIZIO_RES = Nvl(INESRES, VR.ESERCIZIO_RES) And
         VR.ESERCIZIO_VOCE = INES And
         VR.CD_CDR = INCDR AND
         VR.CD_LINEA_ATTIVITA = Nvl(INLA, VR.CD_LINEA_ATTIVITA)  AND
         VR.TI_APPARTENENZA = INAPP AND
         VR.TI_GESTIONE = INGEST AND
         VR.CD_VOCE = Nvl(INVOCE, VR.CD_VOCE) And
         VR.CD_ELEMENTO_VOCE = Nvl(INELVOCE, VR.CD_ELEMENTO_VOCE) And
         VR.IM_VARIAZIONE < 0 And
         VT.ESERCIZIO     = VR.ESERCIZIO And
         VT.PG_VARIAZIONE = VR.PG_VARIAZIONE And
         VT.STATO = 'APP';

Return  Nvl(VALORE, 0);

END;


Function IM_STANZ_INIZIALE_A1 (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is
VALORE NUMBER;
TIPO_UO VARCHAR2(10);

Begin

If INES = INESRES Then -- COMPETENZA

Select CD_TIPO_UNITA
Into   TIPO_UO
From   UNITA_ORGANIZZATIVA
Where  CD_UNITA_ORGANIZZATIVA = Substr(INCDR, 1, 7);

If INGEST = 'E' Then

  If TIPO_UO != 'AREA' Then

   Select Sum(IM_ENTRATA)
   Into   VALORE
   From   PDG_MODULO_ENTRATE_GEST PDG, UNITA_ORGANIZZATIVA AREA
   Where  PDG.ESERCIZIO           = INES   AND
          PDG.CD_CDR_ASSEGNATARIO = INCDR  AND
          PDG.CD_LINEA_ATTIVITA   = INLA   AND
          PDG.TI_APPARTENENZA     = INAPP  AND
          PDG.TI_GESTIONE         = INGEST AND
          PDG.CD_ELEMENTO_VOCE    = INELVOCE AND
          PDG.CD_CDS_AREA = AREA.CD_UNITA_ORGANIZZATIVA AND
          AREA.CD_TIPO_UNITA != 'AREA' AND
          PDG.FL_SOLA_LETTURA = 'N' And
          (PDG.ESERCIZIO, PDG.CD_CENTRO_RESPONSABILITA) IN
    (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

  Elsif TIPO_UO = 'AREA' Then

   Select Sum(IM_ENTRATA)
   Into   VALORE
   From   PDG_MODULO_ENTRATE_GEST PDG
   Where  PDG.ESERCIZIO           = INES   AND
          PDG.CD_CDR_ASSEGNATARIO = INCDR  AND
          PDG.CD_LINEA_ATTIVITA   = INLA   AND
          PDG.TI_APPARTENENZA     = INAPP  AND
          PDG.TI_GESTIONE         = INGEST AND
          PDG.CD_ELEMENTO_VOCE    = INELVOCE AND
          (PDG.ESERCIZIO, PDG.CD_CENTRO_RESPONSABILITA) IN
    (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

  End If;

Elsif INGEST = 'S' Then

  If TIPO_UO != 'AREA' Then

-- SPESE DECENTRATE

         Select Nvl(Sum(PDG_SPE.IM_SPESE_GEST_DECENTRATA_INT) + Sum(PDG_SPE.IM_SPESE_GEST_DECENTRATA_EST), 0)
         Into   VALORE
         From   PDG_MODULO_SPESE_GEST PDG_SPE,
                UNITA_ORGANIZZATIVA AREA
         Where  PDG_SPE.ESERCIZIO           = INES    AND
                PDG_SPE.CD_CDR_ASSEGNATARIO = INCDR   AND
                PDG_SPE.CD_LINEA_ATTIVITA   = INLA    AND
                PDG_SPE.TI_APPARTENENZA     = INAPP   AND
                PDG_SPE.TI_GESTIONE         = INGEST  AND
                PDG_SPE.CD_ELEMENTO_VOCE    = INELVOCE  AND
                PDG_SPE.CD_CDS_AREA         = AREA.CD_UNITA_ORGANIZZATIVA AND
                AREA.CD_TIPO_UNITA         != 'AREA' AND
                (PDG_SPE.ESERCIZIO, PDG_SPE.CD_CENTRO_RESPONSABILITA) IN
          (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

         Select Nvl(VALORE, 0) + Nvl(Sum(PDG_SPE.IM_SPESE_GEST_ACCENTRATA_INT) + Sum(PDG_SPE.IM_SPESE_GEST_ACCENTRATA_EST), 0)
         Into   VALORE
         From   PDG_MODULO_SPESE_GEST PDG_SPE, V_CLASSIFICAZIONE_VOCI CLA,
                UNITA_ORGANIZZATIVA AREA
         Where  PDG_SPE.ESERCIZIO           = INES    AND
                PDG_SPE.CD_CDR_ASSEGNATARIO = INCDR   AND
                PDG_SPE.CD_LINEA_ATTIVITA   = INLA    AND
                PDG_SPE.TI_APPARTENENZA     = INAPP   AND
                PDG_SPE.TI_GESTIONE         = INGEST  AND
                PDG_SPE.CD_ELEMENTO_VOCE    = INELVOCE  AND
                PDG_SPE.ID_CLASSIFICAZIONE  = CLA.ID_CLASSIFICAZIONE And
                NVL(CLA.CDR_ACCENTRATORE, 'xxx') = PDG_SPE.CD_CDR_ASSEGNATARIO And
                PDG_SPE.CD_CDS_AREA         = AREA.CD_UNITA_ORGANIZZATIVA AND
                AREA.CD_TIPO_UNITA         != 'AREA' AND
                (PDG_SPE.ESERCIZIO, PDG_SPE.CD_CENTRO_RESPONSABILITA) IN
          (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

  Elsif TIPO_UO = 'AREA' Then

   Select Nvl(Sum(PDG_SPE.IM_SPESE_GEST_DECENTRATA_INT) + Sum(PDG_SPE.IM_SPESE_GEST_DECENTRATA_EST) +
              Sum(PDG_SPE.IM_SPESE_GEST_ACCENTRATA_EST) + Sum(PDG_SPE.IM_SPESE_GEST_ACCENTRATA_EST), 0)
   Into   VALORE
   From   PDG_MODULO_SPESE_GEST PDG_SPE
   Where  PDG_SPE.ESERCIZIO           = INES    AND
          PDG_SPE.CD_CDR_ASSEGNATARIO = INCDR   AND
          PDG_SPE.CD_LINEA_ATTIVITA   = INLA    AND
          PDG_SPE.TI_APPARTENENZA     = INAPP   AND
          PDG_SPE.TI_GESTIONE         = INGEST  AND
          PDG_SPE.CD_ELEMENTO_VOCE    = INELVOCE  AND
          (PDG_SPE.ESERCIZIO, PDG_SPE.CD_CENTRO_RESPONSABILITA) IN
    (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

   End If; -- TIPO UO

End If;

End If;

Return  Nvl(VALORE, 0);

End;

Function IM_STANZ_INIZIALE_CASSA (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                  INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                                  INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                                  INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                                  INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                                  INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                  INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                                  INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
Return NUMBER Is

VALORE NUMBER;
TIPO_UO VARCHAR2(10);

BEGIN

Select CD_TIPO_UNITA
Into   TIPO_UO
From   UNITA_ORGANIZZATIVA
Where  CD_UNITA_ORGANIZZATIVA = Substr(INCDR, 1, 7);

If INGEST = 'E' Then

  If TIPO_UO != 'AREA' Then

   Select Sum(IM_INCASSI)
   Into   VALORE
   From   PDG_MODULO_ENTRATE_GEST PDG, UNITA_ORGANIZZATIVA AREA
   Where  PDG.ESERCIZIO           = INES   AND
          PDG.CD_CDR_ASSEGNATARIO = INCDR  AND
          PDG.CD_LINEA_ATTIVITA   = INLA   AND
          PDG.TI_APPARTENENZA     = INAPP  AND
          PDG.TI_GESTIONE         = INGEST AND
          PDG.CD_ELEMENTO_VOCE    = INELVOCE AND
          PDG.CD_CDS_AREA = AREA.CD_UNITA_ORGANIZZATIVA AND
          AREA.CD_TIPO_UNITA != 'AREA' AND
          PDG.FL_SOLA_LETTURA = 'N' And
          (PDG.ESERCIZIO, PDG.CD_CENTRO_RESPONSABILITA) IN
    (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

  Elsif TIPO_UO = 'AREA' Then

   Select Sum(IM_INCASSI)
   Into   VALORE
   From   PDG_MODULO_ENTRATE_GEST PDG
   Where  PDG.ESERCIZIO           = INES   AND
          PDG.CD_CDR_ASSEGNATARIO = INCDR  AND
          PDG.CD_LINEA_ATTIVITA   = INLA   AND
          PDG.TI_APPARTENENZA     = INAPP  AND
          PDG.TI_GESTIONE         = INGEST AND
          PDG.CD_ELEMENTO_VOCE    = INELVOCE AND
          (PDG.ESERCIZIO, PDG.CD_CENTRO_RESPONSABILITA) IN
    (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

  End If;

Elsif INGEST = 'S' Then

  If TIPO_UO != 'AREA' Then

-- SPESE DECENTRATE

         Select Sum(IM_PAGAMENTI)
         Into   VALORE
         From   PDG_MODULO_SPESE_GEST PDG_SPE,
                UNITA_ORGANIZZATIVA AREA
         Where  PDG_SPE.ESERCIZIO           = INES    AND
                PDG_SPE.CD_CDR_ASSEGNATARIO = INCDR   AND
                PDG_SPE.CD_LINEA_ATTIVITA   = INLA    AND
                PDG_SPE.TI_APPARTENENZA     = INAPP   AND
                PDG_SPE.TI_GESTIONE         = INGEST  AND
                PDG_SPE.CD_ELEMENTO_VOCE    = INELVOCE  AND
                PDG_SPE.CD_CDS_AREA         = AREA.CD_UNITA_ORGANIZZATIVA AND
                AREA.CD_TIPO_UNITA         != 'AREA' AND
                (PDG_SPE.ESERCIZIO, PDG_SPE.CD_CENTRO_RESPONSABILITA) IN
                (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

         Select Nvl(VALORE, 0) + Nvl(Sum(IM_PAGAMENTI), 0)
         Into   VALORE
         From   PDG_MODULO_SPESE_GEST PDG_SPE, V_CLASSIFICAZIONE_VOCI CLA,
                UNITA_ORGANIZZATIVA AREA
         Where  PDG_SPE.ESERCIZIO           = INES    AND
                PDG_SPE.CD_CDR_ASSEGNATARIO = INCDR   AND
                PDG_SPE.CD_LINEA_ATTIVITA   = INLA    AND
                PDG_SPE.TI_APPARTENENZA     = INAPP   AND
                PDG_SPE.TI_GESTIONE         = INGEST  AND
                PDG_SPE.CD_ELEMENTO_VOCE    = INELVOCE  AND
                PDG_SPE.ID_CLASSIFICAZIONE  = CLA.ID_CLASSIFICAZIONE And
                NVL(CLA.CDR_ACCENTRATORE, 'xxx') = PDG_SPE.CD_CDR_ASSEGNATARIO And
                PDG_SPE.CD_CDS_AREA         = AREA.CD_UNITA_ORGANIZZATIVA AND
                AREA.CD_TIPO_UNITA         != 'AREA' AND
                (PDG_SPE.ESERCIZIO, PDG_SPE.CD_CENTRO_RESPONSABILITA) IN
          (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

  Elsif TIPO_UO = 'AREA' Then

   Select Sum(IM_PAGAMENTI)
   Into   VALORE
   From   PDG_MODULO_SPESE_GEST PDG_SPE
   Where  PDG_SPE.ESERCIZIO           = INES    AND
          PDG_SPE.CD_CDR_ASSEGNATARIO = INCDR   AND
          PDG_SPE.CD_LINEA_ATTIVITA   = INLA    AND
          PDG_SPE.TI_APPARTENENZA     = INAPP   AND
          PDG_SPE.TI_GESTIONE         = INGEST  AND
          PDG_SPE.CD_ELEMENTO_VOCE    = INELVOCE  AND
          (PDG_SPE.ESERCIZIO, PDG_SPE.CD_CENTRO_RESPONSABILITA) IN
    (SELECT ESERCIZIO, CD_CENTRO_RESPONSABILITA From PDG_ESERCIZIO Where STATO = 'CG');

   End If; -- TIPO UO

End If;

Return  Nvl(VALORE, 0);

End;


FUNCTION VARIAZIONI_PIU   (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                           INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                           INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                           INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                           INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                           INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                           INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                           INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is

Begin
Return  VARIAZIONI_PIU (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE, Null, Null);
End;

Function VARIAZIONI_PIU   (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                           INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                           INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                           INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                           INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                           INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                           INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                           INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type,
                           DA_DATA         DATE,
                           A_DATA          DATE)
RETURN NUMBER Is

Begin
Return  Nvl(VARIAZIONI_CON_SEGNO(INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE, '+', DA_DATA, A_DATA), 0);
End;


FUNCTION VARIAZIONI_MENO  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                           INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                           INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                           INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                           INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                           INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                           INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                           INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is

Begin
  Return  VARIAZIONI_MENO (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE, Null, Null);
End;


FUNCTION VARIAZIONI_MENO  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                           INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                           INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                           INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                           INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                           INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                           INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                           INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type,
                           DA_DATA         DATE,
                           A_DATA          DATE)
RETURN NUMBER Is

Begin
Return  Nvl(VARIAZIONI_CON_SEGNO(INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE, '-', DA_DATA, A_DATA), 0);
End;

Function VARIAZIONI_CON_SEGNO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type,
                               SEGNO           CHAR,
                               DA_DATA         DATE,
                               A_DATA          DATE)
RETURN NUMBER Is

VALORE NUMBER;
TIPO_UO VARCHAR2(10);
recParametriCNR parametri_cnr%rowtype;
Begin

recParametriCNR := CNRUTL001.getRecParametriCnr(INES);

Select CD_TIPO_UNITA
Into   TIPO_UO
From   UNITA_ORGANIZZATIVA
Where  CD_UNITA_ORGANIZZATIVA = Substr(INCDR, 1, 7);


-- PER GLI ISTITUTI SI CALCOLA SOLO PER LA COMPETENZA, PER L'ENTE ANCHE PER I RESIDUI

If INCDR != cnrctb020.getCdCdrEnte Then

  If INES = Nvl(INESRES, INES-1) Then -- SOLO PER LA COMPETENZA

    If INGEST = 'S' Then

      If SEGNO = '+' Then

      Select Sum(Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), 0) +
                 Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), 0) +
                 Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), 0) +
                 Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), 0))
      Into   VALORE
      From   PDG_VARIAZIONE T, PDG_VARIAZIONE_RIGA_GEST D
      Where  D.ESERCIZIO = INES And
             D.CD_CDR_ASSEGNATARIO = INCDR And
             D.CD_LINEA_ATTIVITA = INLA And
             D.TI_APPARTENENZA = INAPP And
             D.TI_GESTIONE = INGEST And
             D.CD_ELEMENTO_VOCE = INELVOCE And
             T.ESERCIZIO         = D.ESERCIZIO        And
             T.PG_VARIAZIONE_PDG = D.PG_VARIAZIONE_PDG And
             T.STATO In ('APP', 'APF') And
             CATEGORIA_DETTAGLIO != 'SCR' And
             T.DT_APPROVAZIONE Between Nvl(DA_DATA, T.DT_APPROVAZIONE) And Nvl(A_DATA, T.DT_APPROVAZIONE);

      Elsif SEGNO = '-' Then

      Select Sum(Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), 0, Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)) +
                 Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), 0, Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)) +
                 Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), 0, Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)) +
                 Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), 0, Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)))
      Into   VALORE
      From   PDG_VARIAZIONE T, PDG_VARIAZIONE_RIGA_GEST D
      Where  D.ESERCIZIO = INES And
             D.CD_CDR_ASSEGNATARIO = INCDR And
             D.CD_LINEA_ATTIVITA = INLA And
             D.TI_APPARTENENZA = INAPP And
             D.TI_GESTIONE = INGEST And
             D.CD_ELEMENTO_VOCE = INELVOCE And
             T.ESERCIZIO         = D.ESERCIZIO        And
             T.PG_VARIAZIONE_PDG = D.PG_VARIAZIONE_PDG And
             T.STATO In ('APP', 'APF') And
             CATEGORIA_DETTAGLIO != 'SCR' And
             T.DT_APPROVAZIONE Between Nvl(DA_DATA, T.DT_APPROVAZIONE) And Nvl(A_DATA, T.DT_APPROVAZIONE);
      End If;

  Elsif INGEST = 'E' Then

      If SEGNO = '+' Then

         Select Sum(Nvl(IM_ENTRATA, 0))
         Into   VALORE
         From   PDG_VARIAZIONE T, PDG_VARIAZIONE_RIGA_GEST D
         Where  D.ESERCIZIO = INES And
                D.CD_CDR_ASSEGNATARIO = INCDR And
                D.CD_LINEA_ATTIVITA = INLA And
                D.TI_APPARTENENZA = INAPP And
                D.TI_GESTIONE = INGEST And
                D.CD_ELEMENTO_VOCE = INELVOCE And
                T.ESERCIZIO         = D.ESERCIZIO        And
                T.PG_VARIAZIONE_PDG = D.PG_VARIAZIONE_PDG And
                T.STATO In ('APP', 'APF') And
                CATEGORIA_DETTAGLIO != 'SCR' And
                T.DT_APPROVAZIONE Between Nvl(DA_DATA, T.DT_APPROVAZIONE) And Nvl(A_DATA, T.DT_APPROVAZIONE) And
                Nvl(IM_ENTRATA, 0) > 0;

       Elsif SEGNO = '-' Then

         Select Sum(Nvl(IM_ENTRATA, 0))
         Into   VALORE
         From   PDG_VARIAZIONE T, PDG_VARIAZIONE_RIGA_GEST D
         Where  D.ESERCIZIO = INES And
                D.CD_CDR_ASSEGNATARIO = INCDR And
                D.CD_LINEA_ATTIVITA = INLA And
                D.TI_APPARTENENZA = INAPP And
                D.TI_GESTIONE = INGEST And
                D.CD_ELEMENTO_VOCE = INELVOCE And
                T.ESERCIZIO         = D.ESERCIZIO        And
                T.PG_VARIAZIONE_PDG = D.PG_VARIAZIONE_PDG And
                T.STATO In ('APP', 'APF') And
                CATEGORIA_DETTAGLIO != 'SCR' And
                T.DT_APPROVAZIONE Between Nvl(DA_DATA, T.DT_APPROVAZIONE) And Nvl(A_DATA, T.DT_APPROVAZIONE) And
                Nvl(IM_ENTRATA, 0) < 0;

      End If;  -- DEL SEGNO

    End If; -- TIPO GESTIONE E/S

  End If; -- PER LA COMPETENZA

Elsif INCDR = cnrctb020.getCdCdrEnte Then
Dbms_Output.PUT_LINE ('5');
    if (recParametriCNR.fl_nuovo_pdg = 'N' ) then
              If SEGNO = '+' Then
        Dbms_Output.PUT_LINE ('10');
               Select Sum(Nvl(IM_VARIAZIONE, 0))
               Into   VALORE
               From   VAR_BILANCIO T, VAR_BILANCIO_DET D
               Where  D.CD_CDS          = CNRCTB020.getCDCDSENTE (INES) And
                      D.ESERCIZIO       = INES And
                      D.TI_APPARTENENZA = INAPP And
                      D.TI_GESTIONE     = INGEST And
                    ((D.CD_VOCE = INVOCE And INVOCE Is Not Null) Or
                     (INVOCE Is Null And
                      D.CD_VOCE In (Select CD_VOCE
                                    From   VOCE_F
                                    Where  ESERCIZIO        = INES And
                                           TI_APPARTENENZA  = INAPP And
                                           TI_GESTIONE      = INGEST And
                                           CD_ELEMENTO_VOCE = INELVOCE))) And
                      T.CD_CDS          = D.CD_CDS And
                      T.ESERCIZIO       = D.ESERCIZIO And
                      T.TI_APPARTENENZA = D.TI_APPARTENENZA And
                      T.PG_VARIAZIONE   = D.PG_VARIAZIONE And
                      T.ESERCIZIO_IMPORTI = INESRES And
                      T.STATO           = 'D' And
                      Nvl(IM_VARIAZIONE, 0) > 0;

             Elsif SEGNO = '-' Then

               Select Sum(Nvl(IM_VARIAZIONE, 0))
               Into   VALORE
               From   VAR_BILANCIO T, VAR_BILANCIO_DET D
               Where  D.CD_CDS          = CNRCTB020.getCDCDSENTE (INES) And
                      D.ESERCIZIO       = INES And
                      D.TI_APPARTENENZA = INAPP And
                      D.TI_GESTIONE     = INGEST And
                    ((D.CD_VOCE = INVOCE And INVOCE Is Not Null) Or
                     (INVOCE Is Null And
                      D.CD_VOCE In (Select CD_VOCE
                                    From   VOCE_F
                                    Where  ESERCIZIO        = INES And
                                           TI_APPARTENENZA  = INAPP And
                                           TI_GESTIONE      = INGEST And
                                           CD_ELEMENTO_VOCE = INELVOCE))) And
                      T.CD_CDS          = D.CD_CDS And
                      T.ESERCIZIO       = D.ESERCIZIO And
                      T.TI_APPARTENENZA = D.TI_APPARTENENZA And
                      T.PG_VARIAZIONE   = D.PG_VARIAZIONE And
                      T.ESERCIZIO_IMPORTI = INESRES And
                      T.STATO           = 'D' And
                      Nvl(IM_VARIAZIONE, 0) < 0;

             End If;
     else
        If INES = Nvl(INESRES, INES-1) Then -- SOLO COMPETENZA

          If INGEST = 'S' Then

                  If SEGNO = '+' Then

                  Select Sum(Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), 0) +
                             Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), 0) +
                             Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), 0) +
                             Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), 0))
                  Into   VALORE
                  From   PDG_VARIAZIONE T, PDG_VARIAZIONE_RIGA_GEST D
                  Where  D.ESERCIZIO = INES And
                         D.CD_CDR_ASSEGNATARIO = INCDR And
                         D.CD_LINEA_ATTIVITA = INLA And
                         D.TI_APPARTENENZA = INAPP And
                         D.TI_GESTIONE = INGEST And
                         D.CD_ELEMENTO_VOCE = INELVOCE And
                         T.ESERCIZIO         = D.ESERCIZIO        And
                         T.PG_VARIAZIONE_PDG = D.PG_VARIAZIONE_PDG And
                         T.STATO In ('APP', 'APF') And
                         CATEGORIA_DETTAGLIO != 'SCR' And
                         T.DT_APPROVAZIONE Between Nvl(DA_DATA, T.DT_APPROVAZIONE) And Nvl(A_DATA, T.DT_APPROVAZIONE);

                  Elsif SEGNO = '-' Then

                  Select Sum(Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), 0, Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)) +
                             Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), 0, Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)) +
                             Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), 0, Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)) +
                             Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), 0, Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)))
                  Into   VALORE
                  From   PDG_VARIAZIONE T, PDG_VARIAZIONE_RIGA_GEST D
                  Where  D.ESERCIZIO = INES And
                         D.CD_CDR_ASSEGNATARIO = INCDR And
                         D.CD_LINEA_ATTIVITA = INLA And
                         D.TI_APPARTENENZA = INAPP And
                         D.TI_GESTIONE = INGEST And
                         D.CD_ELEMENTO_VOCE = INELVOCE And
                         T.ESERCIZIO         = D.ESERCIZIO        And
                         T.PG_VARIAZIONE_PDG = D.PG_VARIAZIONE_PDG And
                         T.STATO In ('APP', 'APF') And
                         CATEGORIA_DETTAGLIO != 'SCR' And
                         T.DT_APPROVAZIONE Between Nvl(DA_DATA, T.DT_APPROVAZIONE) And Nvl(A_DATA, T.DT_APPROVAZIONE);
                  End If;
          end if;  -- INGEST = 'S'
        end if; -- solo competenza
     end if;  -- nuovo_pdg

  End If; -- DEL CDR != '999.000.000'

Return  Nvl(VALORE, 0);
End;

FUNCTION IM_OBBL_ACC_COMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                           INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                           INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                           INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                           INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                           INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                           INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                           INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is

Begin
  Return IM_OBBL_ACC_COMP (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE, Null, Null);
End;

Function IM_OBBL_ACC_COMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                           INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                           INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                           INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                           INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                           INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                           INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                           INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type,
                           DA_DATA         DATE,
                           A_DATA          DATE)
Return NUMBER Is
VALORE NUMBER;
PGIRO           ELEMENTO_VOCE.FL_PARTITA_GIRO%Type;
TIPOLOGIA1       VARCHAR2(200);
TIPOLOGIA2       VARCHAR2(200);
TIPOLOGIA3       VARCHAR2(200);

Begin

Begin

Select FL_PARTITA_GIRO
Into   PGIRO
From   ELEMENTO_VOCE
Where  ESERCIZIO        = INES And
       TI_APPARTENENZA  = INAPP And
       TI_GESTIONE      = INGEST And
       CD_ELEMENTO_VOCE = INELVOCE;

If INGEST = 'S' And INES = Nvl(INESRES, INES-1) Then -- OBBLIGAZIONI COMPETENZA

   If PGIRO = 'Y' Then
      TIPOLOGIA1 := 'OBB';
      TIPOLOGIA2 := 'OBB_PGIRO';
      TIPOLOGIA3 := 'IMP';
   Else
      If INCDR = cnrctb020.getCdCdrEnte Then
          TIPOLOGIA1 := 'IMP';
      Else
          TIPOLOGIA1 := 'OBB';
      End If;
   End If;

   Select Sum(IM_VOCE)
   Into   VALORE
   From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS
   Where  OSV.ESERCIZIO = INES And
          OSV.TI_APPARTENENZA = INAPP And
          OSV.TI_GESTIONE = INGEST And
          OSV.CD_VOCE = Nvl(INVOCE, OSV.CD_VOCE) And
          OSV.CD_CENTRO_RESPONSABILITA = INCDR And
          OSV.CD_LINEA_ATTIVITA = INLA And
          OSV.CD_CDS = OS.CD_CDS And
          OSV.ESERCIZIO = OS.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
          OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE And
          OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
          OSV.CD_CDS = O.CD_CDS And
          OSV.ESERCIZIO = O.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
          OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
         ((O.ESERCIZIO_ORIGINALE = INES And 'Y' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES)) Or
          (O.ESERCIZIO_ORI_RIPORTO Is Null And 'N' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES))) And
          O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
          O.PG_OBBLIGAZIONE > 0 And
          O.DT_REGISTRAZIONE Between Nvl(DA_DATA, O.DT_REGISTRAZIONE) And Nvl(A_DATA, O.DT_REGISTRAZIONE) And
          (O.CD_TIPO_DOCUMENTO_CONT = TIPOLOGIA1 Or
           O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA2, TIPOLOGIA1) Or
           O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA3, TIPOLOGIA1));

Elsif INGEST = 'E' And INES = Nvl(INESRES, INES-1) Then -- ACCERTAMENTI COMPETENZA

   Select Sum(IM_VOCE)
   Into   VALORE
   From   ACCERTAMENTO_SCAD_VOCE OSV, ACCERTAMENTO O, ACCERTAMENTO_SCADENZARIO OS
   Where  O.ESERCIZIO = INES And
          O.TI_APPARTENENZA = INAPP And
          O.TI_GESTIONE = INGEST And
          O.CD_VOCE = Nvl(INVOCE, O.CD_VOCE) And
          OSV.CD_CENTRO_RESPONSABILITA = INCDR And
          OSV.CD_LINEA_ATTIVITA = INLA And
          OSV.CD_CDS = OS.CD_CDS And
          OSV.ESERCIZIO = OS.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
          OSV.PG_ACCERTAMENTO = OS.PG_ACCERTAMENTO And
          OSV.PG_ACCERTAMENTO_SCADENZARIO = OS.PG_ACCERTAMENTO_SCADENZARIO And
          OSV.CD_CDS = O.CD_CDS And
          OSV.ESERCIZIO = O.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
          OSV.PG_ACCERTAMENTO = O.PG_ACCERTAMENTO And
         ((O.ESERCIZIO_ORIGINALE = INES And 'Y' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES)) Or
          (O.ESERCIZIO_ORI_RIPORTO Is Null And 'N' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES))) And
          O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
          O.PG_ACCERTAMENTO > 0 And
          O.DT_REGISTRAZIONE Between Nvl(DA_DATA, O.DT_REGISTRAZIONE) And Nvl(A_DATA, O.DT_REGISTRAZIONE) And
          O.CD_TIPO_DOCUMENTO_CONT In ('ACR', 'ACR_SIST', 'ACR_PGIRO');
End If;

Exception
 When No_Data_Found Then Null;
End;

Return  Nvl(VALORE, 0);

END;


FUNCTION IM_OBBL_RES_IMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is
VALORE NUMBER;
BEGIN

Select Sum(IM_VOCE)
Into   VALORE
From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS
Where  OSV.ESERCIZIO = INES And
       OSV.TI_APPARTENENZA = INAPP And
       OSV.TI_GESTIONE = INGEST And
       O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
       OSV.CD_VOCE = Nvl(INVOCE, OSV.CD_VOCE) And
       OSV.CD_CENTRO_RESPONSABILITA = INCDR And
       OSV.CD_LINEA_ATTIVITA = INLA And
       OSV.CD_CDS = OS.CD_CDS And
       OSV.ESERCIZIO = OS.ESERCIZIO And
       OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
       OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE And
       OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
       OSV.CD_CDS = O.CD_CDS And
       OSV.ESERCIZIO = O.ESERCIZIO And
       OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
       OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
       O.ESERCIZIO_ORIGINALE = Nvl(INESRES, O.ESERCIZIO_ORIGINALE) And
       O.PG_OBBLIGAZIONE > 0 And
       O.CD_TIPO_DOCUMENTO_CONT = 'OBB_RESIM';

Return  Nvl(VALORE, 0);
End;

FUNCTION IM_OBBL_RES_PRO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                          INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                          INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                          INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                          INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                          INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                          INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                          INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is

VALORE NUMBER;
PGIRO           ELEMENTO_VOCE.FL_PARTITA_GIRO%Type;
TIPOLOGIA1       VARCHAR2(200);
TIPOLOGIA2       VARCHAR2(200);
TIPOLOGIA3       VARCHAR2(200);
REG_2006         CHAR(1);

Begin

Begin

Select FL_PARTITA_GIRO
Into   PGIRO
From   ELEMENTO_VOCE
Where  ESERCIZIO        = INES And
       TI_APPARTENENZA  = INAPP And
       TI_GESTIONE      = INGEST And
       CD_ELEMENTO_VOCE = INELVOCE;

If INGEST = 'S' And INES > Nvl(INESRES, INES-1) Then -- OBBLIGAZIONI/IMPEGNI RESIDUI PROPRI

   If PGIRO = 'Y' Then
      TIPOLOGIA1 := 'OBB_PGIR_R';
      TIPOLOGIA2 := 'IMP_RES';
   Else
      If INCDR = cnrctb020.getCdCdrEnte Then
          TIPOLOGIA1 := 'IMP_RES';
      Else
          TIPOLOGIA1 := 'OBB_RES';
      End If;
   End If;

   Select Sum(IM_VOCE)
   Into   VALORE
   From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS
   Where  OSV.ESERCIZIO = INES And
          OSV.TI_APPARTENENZA = INAPP And
          OSV.TI_GESTIONE = INGEST And
          OSV.CD_VOCE = Nvl(INVOCE, OSV.CD_VOCE) And
          O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
          OSV.CD_CENTRO_RESPONSABILITA = INCDR And
          OSV.CD_LINEA_ATTIVITA = INLA And
          OSV.CD_CDS = OS.CD_CDS And
          OSV.ESERCIZIO = OS.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
          OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE And
          OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
          OSV.CD_CDS = O.CD_CDS And
          OSV.ESERCIZIO = O.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
          O.PG_OBBLIGAZIONE > 0 And
          OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
         ((((O.ESERCIZIO_ORIGINALE = INESRES And INESRES Is Not Null) Or (O.ESERCIZIO_ORIGINALE < INES And INESRES Is Null))
           And 'Y' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES)) Or
         (((O.ESERCIZIO_ORI_RIPORTO = INESRES And INESRES Is Not Null) Or (O.ESERCIZIO_ORI_RIPORTO < INES And INESRES Is Null))
           And 'N' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES))) And
         (O.CD_TIPO_DOCUMENTO_CONT = TIPOLOGIA1 Or
          O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA2, TIPOLOGIA1) Or
          O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA3, TIPOLOGIA1));

Elsif INGEST = 'E' Then

   TIPOLOGIA1 := 'ACR_PGIR_R';
   TIPOLOGIA2 := 'ACR_RES';

   Select Nvl(FL_REGOLAMENTO_2006, 'N')
   Into   REG_2006
   From   parametri_cnr
   Where  esercizio = INES;

   If REG_2006 = 'N' Then
      TIPOLOGIA3 := 'ACR_PGIRO';
   End If;

   Select Sum(IM_VOCE)
   Into   VALORE
   From   ACCERTAMENTO_SCAD_VOCE OSV, ACCERTAMENTO O, ACCERTAMENTO_SCADENZARIO OS
   Where  O.ESERCIZIO = INES And
          O.TI_APPARTENENZA = INAPP And
          O.TI_GESTIONE = INGEST And
          O.CD_VOCE = Nvl(INVOCE, O.CD_VOCE) And
          O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
          OSV.CD_CENTRO_RESPONSABILITA = INCDR And
          OSV.CD_LINEA_ATTIVITA = INLA And
          OSV.CD_CDS = OS.CD_CDS And
          OSV.ESERCIZIO = OS.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
          OSV.PG_ACCERTAMENTO = OS.PG_ACCERTAMENTO And
          OSV.PG_ACCERTAMENTO_SCADENZARIO = OS.PG_ACCERTAMENTO_SCADENZARIO And
          OSV.CD_CDS = O.CD_CDS And
          OSV.ESERCIZIO = O.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
          OSV.PG_ACCERTAMENTO = O.PG_ACCERTAMENTO And
         ((((O.ESERCIZIO_ORIGINALE = INESRES And INESRES Is Not Null) Or (O.ESERCIZIO_ORIGINALE < INES And INESRES Is Null))
           And 'Y' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES)) Or
         (((O.ESERCIZIO_ORI_RIPORTO = INESRES And INESRES Is Not Null) Or (O.ESERCIZIO_ORI_RIPORTO < INES And INESRES Is Null))
           And 'N' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES))) And
         (O.CD_TIPO_DOCUMENTO_CONT = TIPOLOGIA1 Or
          O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA2, TIPOLOGIA1) Or
          O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA3, TIPOLOGIA1));

End If;

If INCDR != cnrctb020.getCdCdrEnte Then
  Return  Nvl(VALORE, 0) -
          VAR_PIU_OBBL_RES_PRO (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE) +
          Abs(VAR_MENO_OBBL_RES_PRO (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE));

Elsif INCDR = cnrctb020.getCdCdrEnte Then
  Return  Nvl(VALORE, 0) -
          VARIAZIONI_PIU (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE) +
          Abs(VARIAZIONI_MENO (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE));
End If;

Exception
 When No_Data_Found Then Return 0;
End;

End;


Function VAR_PIU_OBBL_RES_PRO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
Return NUMBER Is
 VALORE NUMBER;
Begin

If INGEST = 'S' Then

   Select Sum(IM_MODIFICA)
   Into   VALORE
   From   OBBLIGAZIONE_MOD_VOCE OMV, OBBLIGAZIONE_MODIFICA OM, PARAMETRI_CNR PAR
   Where  OMV.ESERCIZIO = INES And
          OMV.TI_APPARTENENZA = INAPP And
          OMV.TI_GESTIONE = INGEST And
          OMV.CD_CDS = OM.CD_CDS And
          OMV.ESERCIZIO = OM.ESERCIZIO And
          OMV.PG_MODIFICA = OM.PG_MODIFICA And
          PAR.ESERCIZIO = OM.ESERCIZIO And
          OM.ESERCIZIO_ORIGINALE = Nvl(INESRES, ESERCIZIO_ORIGINALE) And
          ((PAR.FL_NUOVO_PDG='Y' AND OMV.CD_VOCE = INELVOCE) OR
           (PAR.FL_NUOVO_PDG='N' AND
           OMV.CD_VOCE In (Select CD_VOCE
                          From   VOCE_F
                          Where  ESERCIZIO        = INES And
                                 TI_APPARTENENZA  = INAPP And
                                 TI_GESTIONE      = INGEST And
                                 CD_ELEMENTO_VOCE = INELVOCE))) And
          OMV.CD_CENTRO_RESPONSABILITA = INCDR And
          OMV.CD_LINEA_ATTIVITA = Nvl(INLA, CD_LINEA_ATTIVITA) And
          OMV.IM_MODIFICA > 0 And
          OM.PG_MODIFICA > 0;

Elsif INGEST = 'E' Then

   Select Sum(IM_MODIFICA)
   Into   VALORE
   From   ACCERTAMENTO_MOD_VOCE OMV, ACCERTAMENTO_MODIFICA OM, PARAMETRI_CNR PAR
   Where  OMV.ESERCIZIO = INES And
          OMV.TI_APPARTENENZA = INAPP And
          OMV.TI_GESTIONE = INGEST And
          OMV.CD_CDS = OM.CD_CDS And
          OMV.ESERCIZIO = OM.ESERCIZIO And
          OMV.PG_MODIFICA = OM.PG_MODIFICA And
          PAR.ESERCIZIO = OM.ESERCIZIO And
          OM.ESERCIZIO_ORIGINALE = Nvl(INESRES, ESERCIZIO_ORIGINALE) And
          ((PAR.FL_NUOVO_PDG='Y' AND OMV.CD_VOCE = INELVOCE) OR
           (PAR.FL_NUOVO_PDG='N' AND
           OMV.CD_VOCE In (Select CD_VOCE
                      From   VOCE_F
                      Where  ESERCIZIO        = INES And
                             TI_APPARTENENZA  = INAPP And
                             TI_GESTIONE      = INGEST And
                             CD_ELEMENTO_VOCE = INELVOCE))) And
          OMV.CD_CENTRO_RESPONSABILITA = INCDR And
          OMV.CD_LINEA_ATTIVITA = INLA And
          OMV.IM_MODIFICA > 0 And
          OM.PG_MODIFICA > 0;

End If;

Return  Nvl(VALORE, 0);

End;

Function VAR_MENO_OBBL_RES_PRO (INES           VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
Return NUMBER Is
 VALORE NUMBER;
Begin

If INGEST = 'S' Then

   Select Nvl(Sum(Abs(IM_MODIFICA)), 0)
   Into   VALORE
   From   OBBLIGAZIONE_MOD_VOCE OMV, OBBLIGAZIONE_MODIFICA OM, PARAMETRI_CNR PAR
   Where  OMV.ESERCIZIO = INES And
          OMV.TI_APPARTENENZA = INAPP And
          OMV.TI_GESTIONE = INGEST And
          OMV.CD_CDS = OM.CD_CDS And
          OMV.ESERCIZIO = OM.ESERCIZIO And
          OMV.PG_MODIFICA = OM.PG_MODIFICA And
          PAR.ESERCIZIO = OM.ESERCIZIO And
          OM.ESERCIZIO_ORIGINALE = Nvl(INESRES, ESERCIZIO_ORIGINALE) And
          ((PAR.FL_NUOVO_PDG='Y' AND OMV.CD_VOCE = INELVOCE) OR
           (PAR.FL_NUOVO_PDG='N' AND
           OMV.CD_VOCE In (Select CD_VOCE
                          From   VOCE_F
                          Where  ESERCIZIO        = INES And
                                 TI_APPARTENENZA  = INAPP And
                                 TI_GESTIONE      = INGEST And
                                 CD_ELEMENTO_VOCE = INELVOCE))) And
          OMV.CD_CENTRO_RESPONSABILITA = INCDR And
          OMV.CD_LINEA_ATTIVITA = Nvl(INLA, CD_LINEA_ATTIVITA) And
          OMV.IM_MODIFICA < 0 And
          OM.PG_MODIFICA > 0;

Elsif INGEST = 'E' Then

   Select Nvl(Sum(Abs(IM_MODIFICA)), 0)
   Into   VALORE
   From   ACCERTAMENTO_MOD_VOCE OMV, ACCERTAMENTO_MODIFICA OM, PARAMETRI_CNR PAR
   Where  OMV.ESERCIZIO = INES And
          OMV.TI_APPARTENENZA = INAPP And
          OMV.TI_GESTIONE = INGEST And
          OMV.CD_CDS = OM.CD_CDS And
          OMV.ESERCIZIO = OM.ESERCIZIO And
          OMV.PG_MODIFICA = OM.PG_MODIFICA And
          PAR.ESERCIZIO = OM.ESERCIZIO And
          OM.ESERCIZIO_ORIGINALE = Nvl(INESRES, ESERCIZIO_ORIGINALE) And
          ((PAR.FL_NUOVO_PDG='Y' AND OMV.CD_VOCE = INELVOCE) OR
           (PAR.FL_NUOVO_PDG='N' AND
           OMV.CD_VOCE In (Select CD_VOCE
                      From   VOCE_F
                      Where  ESERCIZIO        = INES And
                             TI_APPARTENENZA  = INAPP And
                             TI_GESTIONE      = INGEST And
                             CD_ELEMENTO_VOCE = INELVOCE))) And
          OMV.CD_CENTRO_RESPONSABILITA = INCDR And
          OMV.CD_LINEA_ATTIVITA = INLA And
          OMV.IM_MODIFICA < 0 And
          OM.PG_MODIFICA > 0;

End If;

Return  Nvl(VALORE, 0);

End;


Function IM_MANDATI_REVERSALI_PRO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                   INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                                   INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                                   INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                                   INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                                   INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                   INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                                   INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is
VALORE NUMBER;
PGIRO           ELEMENTO_VOCE.FL_PARTITA_GIRO%Type;
TIPOLOGIA1       VARCHAR2(200);
TIPOLOGIA2       VARCHAR2(200);
TIPOLOGIA3       VARCHAR2(200);
REG_2006         CHAR(1);

Begin

Begin

Select FL_PARTITA_GIRO
Into   PGIRO
From   ELEMENTO_VOCE
Where  ESERCIZIO        = INES And
       TI_APPARTENENZA  = INAPP And
       TI_GESTIONE      = INGEST And
       CD_ELEMENTO_VOCE = INELVOCE;

If INGEST = 'S' Then

  If INES = Nvl(INESRES, INES-1) Then -- MANDATI SU IMPEGNI A COMPETENZA

     If PGIRO = 'Y' Then
        TIPOLOGIA1 := 'OBB';
        TIPOLOGIA2 := 'OBB_PGIRO';
        TIPOLOGIA3 := 'IMP';
     Else
        If INCDR = cnrctb020.getCdCdrEnte Then
            TIPOLOGIA1 := 'IMP';
        Else
            TIPOLOGIA1 := 'OBB';
        End If;
     End If;

     Select to_number(to_char(SUM(DECODE(NVL(OS.IM_SCADENZA,0),0,0,(OSV.IM_VOCE/OS.IM_SCADENZA )*MR.IM_MANDATO_RIGA)),'9999999999999D99'))
     Into   VALORE
     From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS, MANDATO_RIGA MR, MANDATO M
     Where  OSV.ESERCIZIO = INES And
            OSV.TI_APPARTENENZA = INAPP And
            OSV.TI_GESTIONE = INGEST And
            OSV.CD_VOCE = Nvl(INVOCE, OSV.CD_VOCE) And
            OSV.CD_CENTRO_RESPONSABILITA = INCDR And
            OSV.CD_LINEA_ATTIVITA = INLA And
            OSV.CD_CDS = OS.CD_CDS And
            OSV.ESERCIZIO = OS.ESERCIZIO And
            OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
            OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE And
            OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
            OSV.CD_CDS = O.CD_CDS And
            OSV.ESERCIZIO = O.ESERCIZIO And
            OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
            OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
            O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
          ((O.ESERCIZIO_ORIGINALE = INES And 'Y' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES)) Or
           (O.ESERCIZIO_ORI_RIPORTO Is Null And 'N' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES))) And
           (O.CD_TIPO_DOCUMENTO_CONT = TIPOLOGIA1 Or
            O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA2, TIPOLOGIA1) Or
            O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA3, TIPOLOGIA1)) And
            O.PG_OBBLIGAZIONE > 0 And
            OS.CD_CDS = MR.CD_CDS AND
            OS.ESERCIZIO = MR.ESERCIZIO_OBBLIGAZIONE AND
            OS.ESERCIZIO_ORIGINALE = MR.ESERCIZIO_ORI_OBBLIGAZIONE And
            OS.PG_OBBLIGAZIONE = MR.PG_OBBLIGAZIONE And
            OS.PG_OBBLIGAZIONE_SCADENZARIO = MR.PG_OBBLIGAZIONE_SCADENZARIO And
            MR.CD_CDS     = M.CD_CDS     AND
            MR.ESERCIZIO  = M.ESERCIZIO  AND
            MR.PG_MANDATO = M.PG_MANDATO And
            M.STATO != 'A';

  Elsif INES > Nvl(INESRES, INES-1) Then -- RESIDUI PROPRI

     If PGIRO = 'Y' Then
         TIPOLOGIA1 := 'OBB_PGIR_R';
         TIPOLOGIA2 := 'IMP_RES';
     Else
         If INCDR = cnrctb020.getCdCdrEnte Then
             TIPOLOGIA1 := 'IMP_RES';
         Else
             TIPOLOGIA1 := 'OBB_RES';
         End If;
     End If;

     Select to_number(to_char(SUM(DECODE(NVL(OS.IM_SCADENZA,0),0,0,(OSV.IM_VOCE/OS.IM_SCADENZA )*MR.IM_MANDATO_RIGA)),'9999999999999D99'))
     Into   VALORE
     From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS, MANDATO_RIGA MR, MANDATO M
     Where  OSV.ESERCIZIO = INES And
            OSV.TI_APPARTENENZA = INAPP And
            OSV.TI_GESTIONE = INGEST And
            OSV.CD_VOCE = Nvl(INVOCE, OSV.CD_VOCE) And
            O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
            OSV.CD_CENTRO_RESPONSABILITA = INCDR And
            OSV.CD_LINEA_ATTIVITA = INLA And
            OSV.CD_CDS = OS.CD_CDS And
            OSV.ESERCIZIO = OS.ESERCIZIO And
            OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
            OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE And
            OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
            OSV.CD_CDS = O.CD_CDS And
            OSV.ESERCIZIO = O.ESERCIZIO And
            OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
            OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
         ((O.ESERCIZIO_ORIGINALE = Nvl(INESRES, O.ESERCIZIO_ORIGINALE) And 'Y' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES)) Or
          (O.ESERCIZIO_ORI_RIPORTO = Nvl(INESRES, O.ESERCIZIO_ORI_RIPORTO) And 'N' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES))) And
           (O.CD_TIPO_DOCUMENTO_CONT = TIPOLOGIA1 Or
            O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA2, TIPOLOGIA1) Or
            O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA3, TIPOLOGIA1)) And
            OS.CD_CDS = MR.CD_CDS AND
            OS.ESERCIZIO = MR.ESERCIZIO_OBBLIGAZIONE AND
            OS.ESERCIZIO_ORIGINALE = MR.ESERCIZIO_ORI_OBBLIGAZIONE And
            OS.PG_OBBLIGAZIONE = MR.PG_OBBLIGAZIONE And
            OS.PG_OBBLIGAZIONE_SCADENZARIO = MR.PG_OBBLIGAZIONE_SCADENZARIO And
            MR.CD_CDS     = M.CD_CDS     AND
            MR.ESERCIZIO  = M.ESERCIZIO  AND
            MR.PG_MANDATO = M.PG_MANDATO And
            O.PG_OBBLIGAZIONE > 0 And
            M.STATO != 'A';

  End If;

Elsif INGEST = 'E' Then

  If INES = Nvl(INESRES, INES-1) Then -- COMPETENZA

   TIPOLOGIA1 := 'ACR_PGIRO';
   TIPOLOGIA2 := 'ACR';
   TIPOLOGIA3 := 'ACR_SIST';

   Select to_number(to_char(SUM(DECODE(NVL(OS.IM_SCADENZA,0),0,0,(OSV.IM_VOCE/OS.IM_SCADENZA )*RR.IM_REVERSALE_RIGA)),'9999999999999D99'))
   Into   VALORE
   From   ACCERTAMENTO_SCAD_VOCE OSV, ACCERTAMENTO O, ACCERTAMENTO_SCADENZARIO OS, REVERSALE_RIGA RR, REVERSALE R
   Where  O.ESERCIZIO = INES And
          O.TI_APPARTENENZA = INAPP And
          O.TI_GESTIONE = INGEST And
          O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
          O.CD_VOCE = Nvl(INVOCE, O.CD_VOCE) And
          OSV.CD_CENTRO_RESPONSABILITA = INCDR And
          OSV.CD_LINEA_ATTIVITA = INLA And
          OSV.CD_CDS = OS.CD_CDS And
          OSV.ESERCIZIO = OS.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
          OSV.PG_ACCERTAMENTO = OS.PG_ACCERTAMENTO And
          OSV.PG_ACCERTAMENTO_SCADENZARIO = OS.PG_ACCERTAMENTO_SCADENZARIO And
          OSV.CD_CDS = O.CD_CDS And
          OSV.ESERCIZIO = O.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
          OSV.PG_ACCERTAMENTO = O.PG_ACCERTAMENTO And
         ((O.ESERCIZIO_ORIGINALE = INES And 'Y' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES)) Or
          (O.ESERCIZIO_ORI_RIPORTO Is Null And 'N' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES))) And
         (O.CD_TIPO_DOCUMENTO_CONT = TIPOLOGIA1 Or
          O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA2, TIPOLOGIA1) Or
          O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA3, TIPOLOGIA1)) And
          OS.CD_CDS = RR.CD_CDS And
          OS.ESERCIZIO = RR.ESERCIZIO_ACCERTAMENTO And
          OS.ESERCIZIO_ORIGINALE = RR.ESERCIZIO_ORI_ACCERTAMENTO And
          OS.PG_ACCERTAMENTO = RR.PG_ACCERTAMENTO And
          OS.PG_ACCERTAMENTO_SCADENZARIO = RR.PG_ACCERTAMENTO_SCADENZARIO And
          RR.CD_CDS     = R.CD_CDS     AND
          RR.ESERCIZIO  = R.ESERCIZIO  AND
          RR.PG_REVERSALE = R.PG_REVERSALE And
          R.STATO != 'A';

  Elsif INES > Nvl(INESRES, INES-1) Then -- RESIDUI

   TIPOLOGIA1 := 'ACR_PGIR_R';
   TIPOLOGIA2 := 'ACR_RES';

   Select Nvl(FL_REGOLAMENTO_2006, 'N')
   Into   REG_2006
   From   parametri_cnr
   Where esercizio = INES;

   If REG_2006 = 'N' Then
      TIPOLOGIA3 := 'ACR_PGIRO';
   End If;

   Select to_number(to_char(SUM(DECODE(NVL(OS.IM_SCADENZA,0),0,0,(OSV.IM_VOCE/OS.IM_SCADENZA )*RR.IM_REVERSALE_RIGA)),'9999999999999D99'))
   Into   VALORE
   From   ACCERTAMENTO_SCAD_VOCE OSV, ACCERTAMENTO O, ACCERTAMENTO_SCADENZARIO OS, REVERSALE_RIGA RR, REVERSALE R
   Where  O.ESERCIZIO = INES And
          O.TI_APPARTENENZA = INAPP And
          O.TI_GESTIONE = INGEST And
          O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
          O.CD_VOCE = Nvl(INVOCE, O.CD_VOCE) And
          OSV.CD_CENTRO_RESPONSABILITA = INCDR And
          OSV.CD_LINEA_ATTIVITA = INLA And
          OSV.CD_CDS = OS.CD_CDS And
          OSV.ESERCIZIO = OS.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
          OSV.PG_ACCERTAMENTO = OS.PG_ACCERTAMENTO And
          OSV.PG_ACCERTAMENTO_SCADENZARIO = OS.PG_ACCERTAMENTO_SCADENZARIO And
          OSV.CD_CDS = O.CD_CDS And
          OSV.ESERCIZIO = O.ESERCIZIO And
          OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
          OSV.PG_ACCERTAMENTO = O.PG_ACCERTAMENTO And
        ((((O.ESERCIZIO_ORIGINALE = INESRES And INESRES Is Not Null) Or (O.ESERCIZIO_ORIGINALE < INES And INESRES Is Null))
           And 'Y' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES)) Or
         (((O.ESERCIZIO_ORI_RIPORTO = INESRES And INESRES Is Not Null) Or (O.ESERCIZIO_ORI_RIPORTO < INES And INESRES Is Null))
           And 'N' = (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = INES))) And
         (O.CD_TIPO_DOCUMENTO_CONT = TIPOLOGIA1 Or
          O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA2, TIPOLOGIA1) Or
          O.CD_TIPO_DOCUMENTO_CONT = Nvl(TIPOLOGIA3, TIPOLOGIA1)) And
          OS.CD_CDS = RR.CD_CDS AND
          OS.ESERCIZIO = RR.ESERCIZIO_ACCERTAMENTO And
          OS.ESERCIZIO_ORIGINALE = RR.ESERCIZIO_ORI_ACCERTAMENTO And
          OS.PG_ACCERTAMENTO = RR.PG_ACCERTAMENTO AND
          OS.PG_ACCERTAMENTO_SCADENZARIO = RR.PG_ACCERTAMENTO_SCADENZARIO And
          RR.CD_CDS     = R.CD_CDS     AND
          RR.ESERCIZIO  = R.ESERCIZIO  AND
          RR.PG_REVERSALE = R.PG_REVERSALE And
          R.STATO != 'A';

  End If;

End If;

Exception
 When No_Data_Found Then Null;
End;

Return  Nvl(VALORE, 0);

END;


Function IM_MANDATI_REVERSALI_IMP (INES        VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                               INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                               INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                               INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                               INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                               INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                               INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                               INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
RETURN NUMBER Is
VALORE NUMBER;

BEGIN

If INGEST = 'S' Then

     Select to_number(to_char(SUM(DECODE(NVL(OS.IM_SCADENZA,0),0,0,(OSV.IM_VOCE/OS.IM_SCADENZA )*MR.IM_MANDATO_RIGA)),'9999999999999D99'))
     Into   VALORE
     From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS, MANDATO_RIGA MR, MANDATO M
     Where  OSV.ESERCIZIO = INES And
            OSV.TI_APPARTENENZA = INAPP And
            OSV.TI_GESTIONE = INGEST And
            OSV.CD_VOCE = Nvl(INVOCE, OSV.CD_VOCE) And
            OSV.CD_CENTRO_RESPONSABILITA = INCDR And
            OSV.CD_LINEA_ATTIVITA = INLA And
            OSV.CD_CDS = OS.CD_CDS And
            OSV.ESERCIZIO = OS.ESERCIZIO And
            OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
            OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE And
            OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
            OSV.CD_CDS = O.CD_CDS And
            OSV.ESERCIZIO = O.ESERCIZIO And
            OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
            OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
            O.CD_ELEMENTO_VOCE = Nvl(INELVOCE, O.CD_ELEMENTO_VOCE) And
            O.ESERCIZIO_ORIGINALE = Nvl(INESRES, O.ESERCIZIO_ORIGINALE)  And
            O.CD_TIPO_DOCUMENTO_CONT = 'OBB_RESIM' And
            OS.CD_CDS = MR.CD_CDS And
            OS.ESERCIZIO = MR.ESERCIZIO_OBBLIGAZIONE And
            OS.ESERCIZIO_ORIGINALE = MR.ESERCIZIO_ORI_OBBLIGAZIONE And
            OS.PG_OBBLIGAZIONE  = MR.PG_OBBLIGAZIONE And
            OS.PG_OBBLIGAZIONE_SCADENZARIO = MR.PG_OBBLIGAZIONE_SCADENZARIO And
            MR.CD_CDS     = M.CD_CDS     AND
            MR.ESERCIZIO  = M.ESERCIZIO  AND
            MR.PG_MANDATO = M.PG_MANDATO And
            O.PG_OBBLIGAZIONE > 0 And
            M.STATO != 'A';
End If;

Return  Nvl(VALORE, 0);

END;

FUNCTION VAR_STANZ_RES_MENO
(P_ESE       IN NUMBER,
 P_ESE_RES   IN NUMBER,
 P_CDR       IN VARCHAR2,
 P_GAE       IN VARCHAR2,
 P_APP       IN CHAR,
 P_GEST      IN CHAR,
 P_VOCE      IN VARCHAR2,
 P_TIPO      IN VARCHAR2)
RETURN NUMBER IS

VALORE NUMBER;

TIPOLOGIA1       VARCHAR2(200);
TIPOLOGIA2       VARCHAR2(200);

BEGIN

If P_TIPO = 'ECO' Then
        TIPOLOGIA1 := 'ECO';
Else
        TIPOLOGIA1 := 'STO';
        TIPOLOGIA2 := 'STO_INT';
End If;

  Select NVL(SUM(ABS(IM_VARIAZIONE)), 0)
  Into   VALORE
  From   VAR_STANZ_RES_RIGA VR, VAR_STANZ_RES VT
  Where  VR.ESERCIZIO = P_ESE AND
         VR.ESERCIZIO_RES = P_ESE_RES AND
         VR.ESERCIZIO_VOCE = P_ESE AND
         VR.CD_CDR = P_CDR  AND
         VR.CD_LINEA_ATTIVITA = P_GAE AND
         VR.TI_APPARTENENZA = P_APP AND
         VR.TI_GESTIONE = P_GEST AND
         VR.CD_VOCE = P_VOCE AND
         VR.IM_VARIAZIONE < 0 AND
         VT.ESERCIZIO     = VR.ESERCIZIO AND
         VT.PG_VARIAZIONE = VR.PG_VARIAZIONE AND
        (VT.TIPOLOGIA = TIPOLOGIA1 Or
         VT.TIPOLOGIA = Nvl(TIPOLOGIA2, TIPOLOGIA1)) And
         VT.STATO = 'APP';

RETURN VALORE;
END;

FUNCTION LIQUIDATO_PRO
(P_ESE       IN NUMBER,
 P_ESE_RES   IN NUMBER,
 P_CDR       IN VARCHAR2,
 P_GAE       IN VARCHAR2,
 P_APP       IN CHAR,
 P_GEST      IN CHAR,
 P_VOCE      IN VARCHAR2)
RETURN NUMBER IS

VALORE NUMBER;

Begin

If P_GEST = 'S' Then

  Select NVL(SUM(IM_VOCE), 0)
  Into   VALORE
  From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS
  Where  OSV.ESERCIZIO = P_ESE AND
         OSV.TI_APPARTENENZA = P_APP AND
         OSV.TI_GESTIONE = P_GEST AND
         OSV.CD_VOCE = P_VOCE AND
         OSV.CD_CENTRO_RESPONSABILITA = P_CDR AND
         OSV.CD_LINEA_ATTIVITA = P_GAE AND
         OSV.CD_CDS = OS.CD_CDS AND
         OSV.ESERCIZIO = OS.ESERCIZIO AND
         OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
         OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE AND
         OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO AND
         OSV.CD_CDS = O.CD_CDS AND
         OSV.ESERCIZIO = O.ESERCIZIO AND
         OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
         OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
         O.CD_TIPO_DOCUMENTO_CONT In ('OBB_RES', 'IMP_RES') And
         O.ESERCIZIO_ORIGINALE = P_ESE_RES And
         OS.IM_ASSOCIATO_DOC_AMM = OS.IM_SCADENZA And
         O.PG_OBBLIGAZIONE > 0;

Elsif P_GEST = 'E' Then

  Select NVL(SUM(IM_VOCE), 0)
  Into   VALORE
  From   ACCERTAMENTO_SCAD_VOCE OSV, ACCERTAMENTO O, ACCERTAMENTO_SCADENZARIO OS
  Where  O.ESERCIZIO = P_ESE AND
         O.TI_APPARTENENZA = P_APP AND
         O.TI_GESTIONE = P_GEST AND
         O.CD_VOCE = P_VOCE AND
         OSV.CD_CENTRO_RESPONSABILITA = P_CDR AND
         OSV.CD_LINEA_ATTIVITA = P_GAE AND
         OSV.CD_CDS = OS.CD_CDS AND
         OSV.ESERCIZIO = OS.ESERCIZIO AND
         OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
         OSV.PG_ACCERTAMENTO = OS.PG_ACCERTAMENTO AND
         OSV.PG_ACCERTAMENTO_SCADENZARIO = OS.PG_ACCERTAMENTO_SCADENZARIO AND
         OSV.CD_CDS = O.CD_CDS AND
         OSV.ESERCIZIO = O.ESERCIZIO AND
         OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
         OSV.PG_ACCERTAMENTO = O.PG_ACCERTAMENTO And
         O.CD_TIPO_DOCUMENTO_CONT In ('ACR_RES') And
         O.ESERCIZIO_ORIGINALE = P_ESE_RES And
         OS.IM_ASSOCIATO_DOC_AMM = OS.IM_SCADENZA;

End If;

Return Valore;
End;

Function RESIDUI_IMPROPRI
(P_ESE       IN NUMBER,
 P_ESE_RES   IN NUMBER,
 P_CDR       IN VARCHAR2,
 P_GAE       IN VARCHAR2,
 P_APP       IN CHAR,
 P_GEST      IN CHAR,
 P_VOCE      IN VARCHAR2,
 P_RIB       IN CHAR)
RETURN NUMBER Is

VALORE NUMBER;

Begin
  Select NVL(SUM(IM_VOCE), 0)
  Into   VALORE
  From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS
  Where  OSV.ESERCIZIO = P_ESE AND
         OSV.TI_APPARTENENZA = P_APP AND
         OSV.TI_GESTIONE = P_GEST AND
         OSV.CD_VOCE = P_VOCE AND
         OSV.CD_CENTRO_RESPONSABILITA = P_CDR AND
         OSV.CD_LINEA_ATTIVITA = P_GAE AND
         OSV.CD_CDS = OS.CD_CDS AND
         OSV.ESERCIZIO = OS.ESERCIZIO AND
         OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
         OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE AND
         OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO AND
         OSV.CD_CDS = O.CD_CDS AND
         OSV.ESERCIZIO = O.ESERCIZIO AND
         OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
         OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
         O.CD_TIPO_DOCUMENTO_CONT In ('OBB_RESIM') And
         O.PG_OBBLIGAZIONE > 0 And
         P_ESE_RES = O.ESERCIZIO_ORIGINALE AND
       ((P_RIB = 'Y' And O.PG_OBBLIGAZIONE_ORI_RIPORTO Is Not Null) Or
        (P_RIB = 'N' And O.PG_OBBLIGAZIONE_ORI_RIPORTO Is Null));

Return VALORE;
End;


Function RESIDUI_IMPROPRI_LIQ
(P_ESE       IN NUMBER,
 P_ESE_RES   IN NUMBER,
 P_CDR       IN VARCHAR2,
 P_GAE       IN VARCHAR2,
 P_APP       IN CHAR,
 P_GEST      IN CHAR,
 P_VOCE      IN VARCHAR2,
 P_RIB       IN CHAR)
RETURN NUMBER Is

VALORE NUMBER;

Begin
  Select NVL(SUM(IM_VOCE), 0)
  Into   VALORE
  From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS
  Where  OSV.ESERCIZIO = P_ESE AND
         OSV.TI_APPARTENENZA = P_APP AND
         OSV.TI_GESTIONE = P_GEST AND
         OSV.CD_VOCE = P_VOCE AND
         OSV.CD_CENTRO_RESPONSABILITA = P_CDR AND
         OSV.CD_LINEA_ATTIVITA = P_GAE AND
         OSV.CD_CDS = OS.CD_CDS AND
         OSV.ESERCIZIO = OS.ESERCIZIO AND
         OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
         OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE AND
         OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO AND
         OSV.CD_CDS = O.CD_CDS AND
         OSV.ESERCIZIO = O.ESERCIZIO AND
         OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
         OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
         O.CD_TIPO_DOCUMENTO_CONT = 'OBB_RESIM' And
         O.PG_OBBLIGAZIONE > 0 And
         P_ESE_RES = O.ESERCIZIO_ORIGINALE AND
       ((P_RIB = 'Y' And O.PG_OBBLIGAZIONE_ORI_RIPORTO Is Not Null) Or
        (P_RIB = 'N' And O.PG_OBBLIGAZIONE_ORI_RIPORTO Is Null)) And
         OS.IM_ASSOCIATO_DOC_AMM = OS.IM_SCADENZA;

Return VALORE;
End;

Function RESIDUI_IMPROPRI_PAG -- PRENDERE DA MARTELLO
(P_ESE       IN NUMBER,
 P_ESE_RES   IN NUMBER,
 P_CDR       IN VARCHAR2,
 P_GAE       IN VARCHAR2,
 P_APP       IN CHAR,
 P_GEST      IN CHAR,
 P_VOCE      IN VARCHAR2,
 P_RIB       IN CHAR)
RETURN NUMBER Is

VALORE NUMBER;

Begin
  Select to_number(to_char(Nvl(SUM(DECODE(NVL(OS.IM_SCADENZA,0),0,0,(OSV.IM_VOCE/OS.IM_SCADENZA )*MR.IM_MANDATO_RIGA)), 0),'9999999999999D99'))
  Into   VALORE
  From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS, MANDATO_RIGA MR, MANDATO M
  Where  OSV.ESERCIZIO = P_ESE And
         OSV.TI_APPARTENENZA = P_APP And
         OSV.TI_GESTIONE = P_GEST And
         OSV.CD_VOCE = P_VOCE And
         OSV.CD_CENTRO_RESPONSABILITA = P_CDR And
         OSV.CD_LINEA_ATTIVITA = P_GAE And
         OSV.CD_CDS = OS.CD_CDS And
         OSV.ESERCIZIO = OS.ESERCIZIO And
         OSV.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
         OSV.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE And
         OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
         OSV.CD_CDS = O.CD_CDS And
         OSV.ESERCIZIO = O.ESERCIZIO And
         OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE And
         OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE And
         O.ESERCIZIO_ORIGINALE = P_ESE_RES And
         O.CD_TIPO_DOCUMENTO_CONT = 'OBB_RESIM' And
         OS.CD_CDS = MR.CD_CDS And
         OS.ESERCIZIO = MR.ESERCIZIO_OBBLIGAZIONE And
         OS.ESERCIZIO_ORIGINALE = MR.ESERCIZIO_ORI_OBBLIGAZIONE And
         OS.PG_OBBLIGAZIONE  = MR.PG_OBBLIGAZIONE And
         OS.PG_OBBLIGAZIONE_SCADENZARIO = MR.PG_OBBLIGAZIONE_SCADENZARIO And
       ((P_RIB = 'Y' And O.PG_OBBLIGAZIONE_ORI_RIPORTO Is Not Null) Or
        (P_RIB = 'N' And O.PG_OBBLIGAZIONE_ORI_RIPORTO Is Null)) And
         MR.CD_CDS     = M.CD_CDS     AND
         MR.ESERCIZIO  = M.ESERCIZIO  AND
         MR.PG_MANDATO = M.PG_MANDATO And
         O.PG_OBBLIGAZIONE > 0 And
         M.STATO != 'A';
Return VALORE;

End;


Function RF_IM_OBBL_ACC_COMP (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                          INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                          INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                          INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                          INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                          INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                          INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                          INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
Return NUMBER Is

REG_2006  CHAR(1);
VALORE    NUMBER;

Begin

   Select Nvl(FL_REGOLAMENTO_2006, 'N')
   Into   REG_2006
   From parametri_cnr
   Where esercizio = INES;

   If REG_2006 = 'Y' Then

     Select Sum(IM_OBBL_ACC_COMP)
     Into   VALORE
     From   VOCE_F_SALDI_CDR_LINEA
     Where  ESERCIZIO                 =  INES    And
            ESERCIZIO_RES             =  INES    And
            CD_CENTRO_RESPONSABILITA  =  INCDR   And
            CD_LINEA_ATTIVITA         =  INLA    And
            TI_APPARTENENZA           =  INAPP   And
            TI_GESTIONE               =  INGEST  And
            CD_VOCE                   =  Nvl(INVOCE, CD_VOCE)  And
            CD_ELEMENTO_VOCE          =  INELVOCE;

   Elsif REG_2006 = 'N' Then

     VALORE := IM_OBBL_ACC_COMP (INES, INES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE);

   End If;

Return Nvl(VALORE, 0);

End;

Function RF_IM_OBBL_RES_PRO  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                              INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                              INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                              INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                              INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                              INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                              INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                              INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
Return NUMBER Is

REG_2006  CHAR(1);
VALORE    NUMBER;

Begin

   Select Nvl(FL_REGOLAMENTO_2006, 'N')
   Into   REG_2006
   From parametri_cnr
   Where esercizio = INES;

   If REG_2006 = 'Y' Then

     Select Sum(IM_OBBL_RES_PRO)
     Into   VALORE
     From   VOCE_F_SALDI_CDR_LINEA
     Where  ESERCIZIO                 = INES    And
            ESERCIZIO_RES             < INES    And --  tutti gli anni residui
            CD_CENTRO_RESPONSABILITA  = INCDR   And
            CD_LINEA_ATTIVITA         = Nvl(INLA, CD_LINEA_ATTIVITA) And
            TI_APPARTENENZA           = INAPP   And
            TI_GESTIONE               = INGEST  And
            CD_VOCE                   = Nvl(INVOCE, CD_VOCE)  And
            CD_ELEMENTO_VOCE          = INELVOCE;

   Elsif REG_2006 = 'N' Then

     VALORE := IM_OBBL_RES_PRO (INES, Null, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE);

   End If;

Return Nvl(VALORE, 0);

End;

Function RF_IM_MANDATI_REVERSALI_PRO  (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                       INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                                       INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                                       INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                                       INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                                       INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                       INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                                       INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
Return NUMBER Is

REG_2006  CHAR(1);
VALORE    NUMBER;

Begin

   Select Nvl(FL_REGOLAMENTO_2006, 'N')
   Into   REG_2006
   From parametri_cnr
   Where esercizio = INES;

   If REG_2006 = 'Y' Then

     Select Sum(IM_MANDATI_REVERSALI_PRO)-- + Sum(IM_MANDATI_REVERSALI_IMP)
     Into   VALORE
     From   VOCE_F_SALDI_CDR_LINEA
     Where  ESERCIZIO                 = INES    And
          ((ESERCIZIO_RES = INESRES And INESRES Is Not Null) Or (ESERCIZIO_RES < INES And INESRES Is Null)) And
            CD_CENTRO_RESPONSABILITA  = INCDR   And
            CD_LINEA_ATTIVITA         = INLA    And
            TI_APPARTENENZA           = INAPP   And
            TI_GESTIONE               = INGEST  And
            CD_VOCE                   = Nvl(INVOCE, CD_VOCE)  And
            CD_ELEMENTO_VOCE          = INELVOCE;

   Elsif REG_2006 = 'N' Then

     VALORE := IM_MANDATI_REVERSALI_PRO (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE);

   End If;

Return Nvl(VALORE, 0);

End;

Function RF_IM_STANZ_INIZIALE_A1 (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                  INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                                  INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                                  INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                                  INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                                  INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                  INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                                  INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)

Return NUMBER Is

REG_2006  CHAR(1);
VALORE    NUMBER;

Begin

   Select Nvl(FL_REGOLAMENTO_2006, 'N')
   Into   REG_2006
   From parametri_cnr
   Where esercizio = INES;

   If REG_2006 = 'Y' Then

     VALORE := IM_STANZ_INIZIALE_A1 (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE);

   Elsif REG_2006 = 'N' Then

     Select Sum(INI)
     Into   VALORE
     From   V_CONS_PDG_ETR_ASSESTATO
     Where  ESERCIZIO                 = INES    And
            CDR                       = INCDR   And
            CD_LINEA_ATTIVITA         = INLA    And
            CD_ELEMENTO_VOCE          = INELVOCE;

   End If;

Return Nvl(VALORE, 0);

End;

Function RF_VARIAZIONI_PIU (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                            INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                            INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                            INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                            INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                            INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                            INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                            INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)

Return NUMBER Is

REG_2006  CHAR(1);
VALORE    NUMBER;

Begin

   Select Nvl(FL_REGOLAMENTO_2006, 'N')
   Into   REG_2006
   From parametri_cnr
   Where esercizio = INES;

   If REG_2006 = 'Y' Then

     VALORE := VARIAZIONI_PIU (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE);

   Elsif REG_2006 = 'N' Then

     Select Sum(VAR_PIU)
     Into   VALORE
     From   V_CONS_PDG_ETR_ASSESTATO
     Where  ESERCIZIO                 = INES    And
            CDR                       = INCDR   And
            CD_LINEA_ATTIVITA         = INLA    And
            CD_ELEMENTO_VOCE          = INELVOCE;

   End If;

Return Nvl(VALORE, 0);

End;

Function RF_VARIAZIONI_MENO (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                             INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                             INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                             INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                             INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                             INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                             INVOCE          VOCE_F_SALDI_CDR_LINEA.CD_VOCE%Type,
                             INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)

Return NUMBER Is

REG_2006  CHAR(1);
VALORE    NUMBER;

Begin

   Select Nvl(FL_REGOLAMENTO_2006, 'N')
   Into   REG_2006
   From parametri_cnr
   Where esercizio = INES;

   If REG_2006 = 'Y' Then

     VALORE := VARIAZIONI_MENO (INES, INESRES, INCDR, INLA, INAPP, INGEST, INVOCE, INELVOCE);

   Elsif REG_2006 = 'N' Then

     Select Sum(VAR_MENO)
     Into   VALORE
     From   V_CONS_PDG_ETR_ASSESTATO
     Where  ESERCIZIO                 = INES    And
            CDR                       = INCDR   And
            CD_LINEA_ATTIVITA         = INLA    And
            CD_ELEMENTO_VOCE          = INELVOCE;

   End If;

Return Nvl(VALORE, 0);

End;

Function RF_PREV_DEF_CASSA       (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                                  INDIP           DIPARTIMENTO.CD_DIPARTIMENTO%Type,
                                  INCDS           UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA%Type,
                                  INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                                  INLIV1          CLASSIFICAZIONE_VOCI.CD_LIVELLO1%Type,
                                  INLIV2          CLASSIFICAZIONE_VOCI.CD_LIVELLO2%Type,
                                  INLIV3          CLASSIFICAZIONE_VOCI.CD_LIVELLO3%Type,
                                  INLIV4          CLASSIFICAZIONE_VOCI.CD_LIVELLO4%Type,
                                  INLIV5          CLASSIFICAZIONE_VOCI.CD_LIVELLO5%Type,
                                  INLIV6          CLASSIFICAZIONE_VOCI.CD_LIVELLO6%Type,
                                  INLIV7          CLASSIFICAZIONE_VOCI.CD_LIVELLO7%Type) Return NUMBER Is

VALORE    NUMBER;

Begin
 Null;
End;

Function RF_FULL_COST_AC (INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                          INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                          INDIP           DIPARTIMENTO.CD_DIPARTIMENTO%Type,
                          INLIV1          CLASSIFICAZIONE_VOCI.CD_LIVELLO1%Type,
                          INLIV2          CLASSIFICAZIONE_VOCI.CD_LIVELLO1%Type) Return NUMBER Is

VALORE    NUMBER;

Begin
 Begin

 Select Nvl(FULL_COSTING, 0)
 Into   VALORE
 From   PARAMETRI_REND_PREV_DEC
 Where  ESERCIZIO = INES And
        TI_GESTIONE = INGEST And
        CD_DIPARTIMENTO = INDIP And
        CD_LIVELLO1 = INLIV1 And
        CD_LIVELLO2 = INLIV2;

 Exception
  When No_Data_Found Then Null;
 End;

Return Nvl(VALORE, 0);

End;

Function IM_MANDATI_PER_IMPEGNO (INCDS           UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA%Type,
                                 INES            OBBLIGAZIONE.ESERCIZIO%Type,
                                 INESRES         OBBLIGAZIONE.ESERCIZIO_ORIGINALE%Type,
                                 INPGOBB         OBBLIGAZIONE.PG_OBBLIGAZIONE%Type,
                                 INPGOBBSCAD     OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO%Type) Return NUMBER Is

VALORE    NUMBER;

Begin

  Begin
     Select to_number(to_char(SUM(DECODE(NVL(OS.IM_SCADENZA,0),0,0,(OSV.IM_VOCE/OS.IM_SCADENZA )*MR.IM_MANDATO_RIGA)),'9999999999999D99'))
     Into   VALORE
     From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE_SCADENZARIO OS, MANDATO_RIGA MR, MANDATO M
     Where  OSV.CD_CDS                      = OS.CD_CDS And
            OSV.ESERCIZIO                   = OS.ESERCIZIO And
            OSV.ESERCIZIO_ORIGINALE         = OS.ESERCIZIO_ORIGINALE And
            OSV.PG_OBBLIGAZIONE             = OS.PG_OBBLIGAZIONE And
            OSV.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
            OSV.CD_CDS                      = INCDS And
            OSV.ESERCIZIO                   = INES And
            OSV.ESERCIZIO_ORIGINALE         = INESRES And
            OSV.PG_OBBLIGAZIONE             = INPGOBB And
            OSV.PG_OBBLIGAZIONE_SCADENZARIO = Nvl(INPGOBBSCAD, OSV.PG_OBBLIGAZIONE_SCADENZARIO) And
            OS.CD_CDS                       = MR.CD_CDS AND
            OS.ESERCIZIO                    = MR.ESERCIZIO_OBBLIGAZIONE AND
            OS.ESERCIZIO_ORIGINALE          = MR.ESERCIZIO_ORI_OBBLIGAZIONE And
            OS.PG_OBBLIGAZIONE              = MR.PG_OBBLIGAZIONE And
            OS.PG_OBBLIGAZIONE_SCADENZARIO  = MR.PG_OBBLIGAZIONE_SCADENZARIO And
            MR.CD_CDS                       = M.CD_CDS     AND
            MR.ESERCIZIO                    = M.ESERCIZIO  AND
            MR.PG_MANDATO                   = M.PG_MANDATO And
            M.STATO                        != 'A';
  Exception
    When No_Data_Found Then Null;
  End;

Return Nvl(VALORE, 0);

End;

Function IM_VINCOLI(INES            VOCE_F_SALDI_CDR_LINEA.ESERCIZIO%Type,
                    INESRES         VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES%Type,
                    INCDR           VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA%Type,
                    INLA            VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA%Type,
                    INAPP           VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA%Type,
                    INGEST          VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE%Type,
                    INELVOCE        VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE%Type)
Return NUMBER Is
  VALORE    NUMBER;
Begin
  SELECT NVL (SUM (x.im_vincolo), 0)
  Into   VALORE
  FROM pdg_vincolo x
  WHERE x.esercizio <= INES
  AND x.esercizio_res = INESRES
  AND x.cd_centro_responsabilita = INCDR
  AND x.cd_linea_attivita = INLA
  AND x.ti_appartenenza = INAPP
  AND x.ti_gestione = INGEST
  AND x.cd_elemento_voce = INELVOCE
  AND x.fl_attivo = 'Y';

  Return Nvl(VALORE, 0);
End;
End CNRUTL002;


