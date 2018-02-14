--------------------------------------------------------
--  DDL for View V_CONS_ASSESTATO_COMP_PER_DATA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_ASSESTATO_COMP_PER_DATA" ("ESERCIZIO", "CD_DIPARTIMENTO", "PG_PROGETTO", "CD_PROGETTO", "DS_PROGETTO", "PG_COMMESSA", "CD_COMMESSA", "DS_COMMESSA", "PG_MODULO", "CD_MODULO", "DS_MODULO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_LIVELLO1", "DS_LIVELLO1", "CD_LIVELLO2", "DS_LIVELLO2", "CD_LIVELLO3", "DS_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "PG_VARIAZIONE_PDG", "DATA_APPROVAZIONE_VAR", "IM_STANZ_INIZIALE_A1", "VARIAZIONI_PIU", "VARIAZIONI_MENO") AS 
  Select
--
-- Date: 13/11/2006
-- Version: 1.1
--
-- Estrae le informazioni dell'assestato di competenza per data
--
-- History:
--
-- Date: 10/11/2006
-- Version: 1.0
-- Creazione
--
-- Date: 13/11/2006
-- Version: 1.11
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
SALDI.ESERCIZIO,
       PROGETTO.CD_DIPARTIMENTO,
       PROGETTO.PG_PROGETTO,
       PROGETTO.CD_PROGETTO,
       PROGETTO.DS_PROGETTO,
       COM.PG_PROGETTO,
       COM.CD_PROGETTO,
       COM.DS_PROGETTO,
       MODU.PG_PROGETTO,
       MODU.CD_PROGETTO,
       MODU.DS_PROGETTO,
       SALDI.CD_CENTRO_RESPONSABILITA,
       SALDI.CD_LINEA_ATTIVITA,
       SALDI.TI_APPARTENENZA,
       SALDI.TI_GESTIONE,
       SALDI.CD_ELEMENTO_VOCE,
       ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
       CLASS.CD_LIVELLO1,
       LIV_1.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO2,
       LIV_2.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO3,
       LIV_3.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO4,
       CLASS.CD_LIVELLO5,
       CLASS.CD_LIVELLO6,
       CLASS.CD_LIVELLO7,
       Null,
       TRUNC(TO_DATE('01/01/'||SALDI.ESERCIZIO, 'DD/MM/YYYY'), 'YEAR'),
       SALDI.IM_STANZ_INIZIALE_A1,
       0,
       0
From   VOCE_F_SALDI_CDR_LINEA SALDI,
       ELEMENTO_VOCE,
       V_CLASSIFICAZIONE_VOCI CLASS,
       LINEA_ATTIVITA,
       PROGETTO_GEST PROGETTO,
       PROGETTO_GEST COM,
       PROGETTO_GEST MODU,
       V_CLASSIFICAZIONE_VOCI LIV_1,
       V_CLASSIFICAZIONE_VOCI LIV_2,
       V_CLASSIFICAZIONE_VOCI LIV_3
Where  SALDI.ESERCIZIO                = SALDI.ESERCIZIO_RES                      And
       CLASS.ID_CLASSIFICAZIONE       = ELEMENTO_VOCE.ID_CLASSIFICAZIONE         And
       ELEMENTO_VOCE.ESERCIZIO        = SALDI.ESERCIZIO                          And
       ELEMENTO_VOCE.TI_APPARTENENZA  = SALDI.TI_APPARTENENZA                    And
       ELEMENTO_VOCE.TI_GESTIONE      = SALDI.TI_GESTIONE                        And
       ELEMENTO_VOCE.CD_ELEMENTO_VOCE = SALDI.CD_ELEMENTO_VOCE                   And
       SALDI.CD_CENTRO_RESPONSABILITA = LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA  And
       SALDI.CD_LINEA_ATTIVITA        = LINEA_ATTIVITA.CD_LINEA_ATTIVITA         And
       LINEA_ATTIVITA.PG_PROGETTO     IS NOT NULL                                And
       LINEA_ATTIVITA.PG_PROGETTO     = MODU.PG_PROGETTO                         And
       MODU.ESERCIZIO                 = SALDI.ESERCIZIO                          And
       MODU.ESERCIZIO_PROGETTO_PADRE  = COM.ESERCIZIO                            And
       MODU.PG_PROGETTO_PADRE         = COM.PG_PROGETTO                          And
       COM.ESERCIZIO_PROGETTO_PADRE   = PROGETTO.ESERCIZIO                       And
       COM.PG_PROGETTO_PADRE          = PROGETTO.PG_PROGETTO                     And
       CLASS.ESERCIZIO                = LIV_1.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_1.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_1.CD_LIVELLO1                        And
       LIV_1.CD_LIVELLO2              IS NULL                                    And
       CLASS.ESERCIZIO                = LIV_2.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_2.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_2.CD_LIVELLO1                        And
       CLASS.CD_LIVELLO2              = LIV_2.CD_LIVELLO2                        And
       LIV_2.CD_LIVELLO3              IS NULL                                    And
       CLASS.ESERCIZIO                = LIV_3.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_3.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_3.CD_LIVELLO1                        And
       CLASS.CD_LIVELLO2              = LIV_3.CD_LIVELLO2                        And
       CLASS.CD_LIVELLO3              = LIV_3.CD_LIVELLO3                        And
       LIV_3.CD_LIVELLO4              IS Null                                    And
       SALDI.IM_STANZ_INIZIALE_A1     != 0
Union All
Select T.ESERCIZIO,
       PROGETTO.CD_DIPARTIMENTO,
       PROGETTO.PG_PROGETTO,
       PROGETTO.CD_PROGETTO,
       PROGETTO.DS_PROGETTO,
       COM.PG_PROGETTO,
       COM.CD_PROGETTO,
       COM.DS_PROGETTO,
       MODU.PG_PROGETTO,
       MODU.CD_PROGETTO,
       MODU.DS_PROGETTO,
       D.CD_CDR_ASSEGNATARIO,
       D.CD_LINEA_ATTIVITA,
       D.TI_APPARTENENZA,
       D.TI_GESTIONE,
       D.CD_ELEMENTO_VOCE,
       ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
       CLASS.CD_LIVELLO1,
       LIV_1.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO2,
       LIV_2.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO3,
       LIV_3.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO4,
       CLASS.CD_LIVELLO5,
       CLASS.CD_LIVELLO6,
       CLASS.CD_LIVELLO7,
       T.PG_VARIAZIONE_PDG,
       T.DT_APPROVAZIONE,
       0, -- INI
       Decode(D.TI_GESTIONE,
         'S',
         Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), 0) +
         Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), 0) +
         Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), 0) +
         Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), 0),
         'E',
         Decode(Abs(Nvl(IM_ENTRATA, 0)), Nvl(IM_ENTRATA, 0), Nvl(IM_ENTRATA, 0), 0)), -- VAR PIU
       0  -- VAR MENO
From   PDG_VARIAZIONE T,
       PDG_VARIAZIONE_RIGA_GEST D,
       ELEMENTO_VOCE,
       V_CLASSIFICAZIONE_VOCI CLASS,
       LINEA_ATTIVITA,
       PROGETTO_GEST PROGETTO,
       PROGETTO_GEST COM,
       PROGETTO_GEST MODU,
       V_CLASSIFICAZIONE_VOCI LIV_1,
       V_CLASSIFICAZIONE_VOCI LIV_2,
       V_CLASSIFICAZIONE_VOCI LIV_3
Where  CLASS.ID_CLASSIFICAZIONE       = ELEMENTO_VOCE.ID_CLASSIFICAZIONE         And
       ELEMENTO_VOCE.ESERCIZIO        = D.ESERCIZIO                              And
       ELEMENTO_VOCE.TI_APPARTENENZA  = D.TI_APPARTENENZA                        And
       ELEMENTO_VOCE.TI_GESTIONE      = D.TI_GESTIONE                            And
       ELEMENTO_VOCE.CD_ELEMENTO_VOCE = D.CD_ELEMENTO_VOCE                       And
       D.CD_CDR_ASSEGNATARIO          = LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA  And
       D.CD_LINEA_ATTIVITA            = LINEA_ATTIVITA.CD_LINEA_ATTIVITA         And
       LINEA_ATTIVITA.PG_PROGETTO     IS NOT NULL                                And
       LINEA_ATTIVITA.PG_PROGETTO     = MODU.PG_PROGETTO                         And
       MODU.ESERCIZIO                 = D.ESERCIZIO                              And
       MODU.ESERCIZIO_PROGETTO_PADRE  = COM.ESERCIZIO                            And
       MODU.PG_PROGETTO_PADRE         = COM.PG_PROGETTO                          And
       COM.ESERCIZIO_PROGETTO_PADRE   = PROGETTO.ESERCIZIO                       And
       COM.PG_PROGETTO_PADRE          = PROGETTO.PG_PROGETTO                     And
       T.ESERCIZIO                    = D.ESERCIZIO                              And
       T.PG_VARIAZIONE_PDG            = D.PG_VARIAZIONE_PDG                      And
       T.STATO                        In ('APP', 'APF')                          And
       CATEGORIA_DETTAGLIO            != 'SCR'                                   And
       CLASS.ESERCIZIO                = LIV_1.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_1.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_1.CD_LIVELLO1                        And
       LIV_1.CD_LIVELLO2              IS NULL                                    And
       CLASS.ESERCIZIO                = LIV_2.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_2.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_2.CD_LIVELLO1                        And
       CLASS.CD_LIVELLO2              = LIV_2.CD_LIVELLO2                        And
       LIV_2.CD_LIVELLO3              IS NULL                                    And
       CLASS.ESERCIZIO                = LIV_3.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_3.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_3.CD_LIVELLO1                        And
       CLASS.CD_LIVELLO2              = LIV_3.CD_LIVELLO2                        And
       CLASS.CD_LIVELLO3              = LIV_3.CD_LIVELLO3                        And
       LIV_3.CD_LIVELLO4              IS Null And
       Decode(D.TI_GESTIONE, 'S',
         Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), 0) +
         Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), 0) +
         Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), 0) +
         Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), 0),
         'E', Decode(Abs(Nvl(IM_ENTRATA, 0)), Nvl(IM_ENTRATA, 0), Nvl(IM_ENTRATA, 0), 0)) != 0
Union All
Select T.ESERCIZIO,
       PROGETTO.CD_DIPARTIMENTO,
       PROGETTO.PG_PROGETTO,
       PROGETTO.CD_PROGETTO,
       PROGETTO.DS_PROGETTO,
       COM.PG_PROGETTO,
       COM.CD_PROGETTO,
       COM.DS_PROGETTO,
       MODU.PG_PROGETTO,
       MODU.CD_PROGETTO,
       MODU.DS_PROGETTO,
       D.CD_CDR_ASSEGNATARIO,
       D.CD_LINEA_ATTIVITA,
       D.TI_APPARTENENZA,
       D.TI_GESTIONE,
       D.CD_ELEMENTO_VOCE,
       ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
       CLASS.CD_LIVELLO1,
       LIV_1.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO2,
       LIV_2.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO3,
       LIV_3.DS_CLASSIFICAZIONE,
       CLASS.CD_LIVELLO4,
       CLASS.CD_LIVELLO5,
       CLASS.CD_LIVELLO6,
       CLASS.CD_LIVELLO7,
       T.PG_VARIAZIONE_PDG,
       T.DT_APPROVAZIONE,
       0, -- INI
       0, -- VAR PIU
       Decode (D.TI_GESTIONE,
         'S',
         Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), 0, Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0))) +
         Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), 0, Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0))) +
         Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), 0, Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0))) +
         Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), 0, Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0))),
         'E',
         Decode(Abs(Nvl(IM_ENTRATA, 0)), Nvl(IM_ENTRATA, 0), 0, Abs(Nvl(IM_ENTRATA, 0)))) -- VAR MENO
From   PDG_VARIAZIONE T,
       PDG_VARIAZIONE_RIGA_GEST D,
       ELEMENTO_VOCE,
       V_CLASSIFICAZIONE_VOCI CLASS,
       LINEA_ATTIVITA,
       PROGETTO_GEST PROGETTO,
       PROGETTO_GEST COM,
       PROGETTO_GEST MODU,
       V_CLASSIFICAZIONE_VOCI LIV_1,
       V_CLASSIFICAZIONE_VOCI LIV_2,
       V_CLASSIFICAZIONE_VOCI LIV_3
Where  CLASS.ID_CLASSIFICAZIONE       = ELEMENTO_VOCE.ID_CLASSIFICAZIONE         And
       ELEMENTO_VOCE.ESERCIZIO        = D.ESERCIZIO                              And
       ELEMENTO_VOCE.TI_APPARTENENZA  = D.TI_APPARTENENZA                        And
       ELEMENTO_VOCE.TI_GESTIONE      = D.TI_GESTIONE                            And
       ELEMENTO_VOCE.CD_ELEMENTO_VOCE = D.CD_ELEMENTO_VOCE                       And
       D.CD_CDR_ASSEGNATARIO          = LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA  And
       D.CD_LINEA_ATTIVITA            = LINEA_ATTIVITA.CD_LINEA_ATTIVITA         And
       LINEA_ATTIVITA.PG_PROGETTO     IS NOT NULL                                And
       LINEA_ATTIVITA.PG_PROGETTO     = MODU.PG_PROGETTO                         And
       MODU.ESERCIZIO                 = D.ESERCIZIO                              And
       MODU.ESERCIZIO_PROGETTO_PADRE  = COM.ESERCIZIO                            And
       MODU.PG_PROGETTO_PADRE         = COM.PG_PROGETTO                          And
       COM.ESERCIZIO_PROGETTO_PADRE   = PROGETTO.ESERCIZIO                       And
       COM.PG_PROGETTO_PADRE          = PROGETTO.PG_PROGETTO                     And
       T.ESERCIZIO                    = D.ESERCIZIO                              And
       T.PG_VARIAZIONE_PDG            = D.PG_VARIAZIONE_PDG                      And
       T.STATO                        In ('APP', 'APF')                          And
       CATEGORIA_DETTAGLIO            != 'SCR'                                   And
       CLASS.ESERCIZIO                = LIV_1.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_1.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_1.CD_LIVELLO1                        And
       LIV_1.CD_LIVELLO2              IS NULL                                    And
       CLASS.ESERCIZIO                = LIV_2.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_2.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_2.CD_LIVELLO1                        And
       CLASS.CD_LIVELLO2              = LIV_2.CD_LIVELLO2                        And
       LIV_2.CD_LIVELLO3              IS NULL                                    And
       CLASS.ESERCIZIO                = LIV_3.ESERCIZIO                          And
       CLASS.TI_GESTIONE              = LIV_3.TI_GESTIONE                        And
       CLASS.CD_LIVELLO1              = LIV_3.CD_LIVELLO1                        And
       CLASS.CD_LIVELLO2              = LIV_3.CD_LIVELLO2                        And
       CLASS.CD_LIVELLO3              = LIV_3.CD_LIVELLO3                        And
       LIV_3.CD_LIVELLO4              IS Null And
       Decode (D.TI_GESTIONE, 'S',
         Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0), 0, Abs(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0))) +
         Decode(Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0), 0, Abs(Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0))) +
         Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0), 0, Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0))) +
         Decode(Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)), Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0), 0, Abs(Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0))),
         'E', Decode(Abs(Nvl(IM_ENTRATA, 0)), Nvl(IM_ENTRATA, 0), 0, Abs(Nvl(IM_ENTRATA, 0)))) != 0;
