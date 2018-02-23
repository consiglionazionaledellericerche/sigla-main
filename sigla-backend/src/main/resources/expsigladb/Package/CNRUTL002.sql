--------------------------------------------------------
--  DDL for Package CNRUTL002
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRUTL002" IS
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
