--------------------------------------------------------
--  DDL for View V_CONS_PDGG_ETR_BIL_IST_DIP_FO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDGG_ETR_BIL_IST_DIP_FO" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "PG_MODULO", "CD_MODULO", "DS_MODULO", "CD_TIPO_MODULO", "DS_TIPO_MODULO", "PG_COMMESSA", "CD_COMMESSA", "DS_COMMESSA", "PG_PROGETTO", "CD_PROGETTO", "DS_PROGETTO", "CD_DIPARTIMENTO", "DS_DIPARTIMENTO", "DS_DETTAGLIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_LINEA_ATTIVITA", "DS_LINEA", "TOT_ENT_IST_A1", "TOT_INC_IST_A1", "TOT_ENT_AREE_A1", "TOT_INC_AREE_A1") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Piano di Gestione Entrate per Istituto/Dipartimento/FO
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
PDG.ESERCIZIO, PDG.CD_CDR_ASSEGNATARIO, CDR.DS_CDR,
       CD_CLASSIFICAZIONE, DS_CLASSIFICAZIONE,
       NR_LIVELLO, CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
       MODU.PG_PROGETTO PG_MODULO, MODU.CD_PROGETTO CD_MODULO, MODU.DS_PROGETTO DS_MODULO,
       MODU.CD_TIPO_PROGETTO CD_TIPO_MODULO, TIPO_PROGETTO.DS_TIPO_PROGETTO DS_TIPO_MODULO,
       COMM.PG_PROGETTO PG_COMMESSA, COMM.CD_PROGETTO CD_COMMESSA, COMM.DS_PROGETTO DS_COMMESSA,
       PROG.PG_PROGETTO, PROG.CD_PROGETTO, PROG.DS_PROGETTO,
       PROG.CD_DIPARTIMENTO, DIP.DS_DIPARTIMENTO,
       DESCRIZIONE,
       PDG.TI_APPARTENENZA, PDG.TI_GESTIONE, PDG.CD_ELEMENTO_VOCE, EV.DS_ELEMENTO_VOCE,
       PDG.CD_LINEA_ATTIVITA, LA.DENOMINAZIONE,
       SUM(IM_ENTRATA) TOT_ENT_IST_A1,
       Sum(IM_INCASSI) TOT_INC_IST_A1,
       0 TOT_ENT_AREE_A1,
       0 TOT_INC_AREE_A1
FROM   PARAMETRI_CNR PAR,
       PDG_MODULO_ENTRATE_GEST PDG, V_CLASSIFICAZIONE_VOCI CLA, UNITA_ORGANIZZATIVA AREA,
       CDR, PROGETTO_GEST MODU, PROGETTO_GEST COMM, PROGETTO_GEST PROG, TIPO_PROGETTO, DIPARTIMENTO DIP,
       ELEMENTO_VOCE EV, LINEA_ATTIVITA LA
WHERE  PAR.ESERCIZIO = PDG.ESERCIZIO AND
       PAR.FL_NUOVO_PDG = 'N' AND
       PDG.ID_CLASSIFICAZIONE = CLA.ID_CLASSIFICAZIONE AND
       PDG.CD_CDS_AREA = AREA.CD_UNITA_ORGANIZZATIVA AND
       PDG.CD_CDR_ASSEGNATARIO = CDR.CD_CENTRO_RESPONSABILITA AND
       EV.ESERCIZIO         = PDG.ESERCIZIO        AND
       EV.TI_APPARTENENZA   = PDG.TI_APPARTENENZA  AND
       EV.TI_GESTIONE       = PDG.TI_GESTIONE      AND
       EV.CD_ELEMENTO_VOCE  = PDG.CD_ELEMENTO_VOCE And
       PDG.CD_CDR_ASSEGNATARIO = LA.CD_CENTRO_RESPONSABILITA And
       PDG.CD_LINEA_ATTIVITA = LA.CD_LINEA_ATTIVITA And
       PDG.PG_PROGETTO = MODU.PG_PROGETTO AND
       MODU.ESERCIZIO = PDG.ESERCIZIO And
       MODU.ESERCIZIO_PROGETTO_PADRE = COMM.ESERCIZIO AND
       MODU.PG_PROGETTO_PADRE = COMM.PG_PROGETTO AND
       COMM.ESERCIZIO_PROGETTO_PADRE = PROG.ESERCIZIO AND
       COMM.PG_PROGETTO_PADRE = PROG.PG_PROGETTO AND
       MODU.CD_TIPO_PROGETTO = TIPO_PROGETTO.CD_TIPO_PROGETTO AND
       DIP.CD_DIPARTIMENTO = PROG.CD_DIPARTIMENTO AND
       AREA.CD_TIPO_UNITA != 'AREA' AND
       PDG.FL_SOLA_LETTURA = 'N'
GROUP BY PDG.ESERCIZIO, PDG.CD_CDR_ASSEGNATARIO, CDR.DS_CDR,
       CD_CLASSIFICAZIONE, DS_CLASSIFICAZIONE, NR_LIVELLO,
       CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
       MODU.PG_PROGETTO, MODU.CD_PROGETTO, MODU.DS_PROGETTO,
       MODU.CD_TIPO_PROGETTO, TIPO_PROGETTO.DS_TIPO_PROGETTO,
       COMM.PG_PROGETTO, COMM.CD_PROGETTO, COMM.DS_PROGETTO,
       PROG.PG_PROGETTO, PROG.CD_PROGETTO, PROG.DS_PROGETTO,
       PROG.CD_DIPARTIMENTO, DIP.DS_DIPARTIMENTO,
       DESCRIZIONE, PDG.TI_APPARTENENZA, PDG.TI_GESTIONE, PDG.CD_ELEMENTO_VOCE, EV.DS_ELEMENTO_VOCE,
       PDG.CD_LINEA_ATTIVITA, LA.DENOMINAZIONE
Union ALL
SELECT PDG.ESERCIZIO,
       PDG.CD_CDR_ASSEGNATARIO, CDR.DS_CDR,
       CD_CLASSIFICAZIONE, DS_CLASSIFICAZIONE, NR_LIVELLO,
       CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5,
       CD_LIVELLO6, CD_LIVELLO7,
       MODU.PG_PROGETTO PG_MODULO, MODU.CD_PROGETTO CD_MODULO, MODU.DS_PROGETTO DS_MODULO,
       MODU.CD_TIPO_PROGETTO CD_TIPO_MODULO, TIPO_PROGETTO.DS_TIPO_PROGETTO DS_TIPO_MODULO,
       COMM.PG_PROGETTO PG_COMMESSA, COMM.CD_PROGETTO CD_COMMESSA, COMM.DS_PROGETTO DS_COMMESSA,
       PROG.PG_PROGETTO, PROG.CD_PROGETTO, PROG.DS_PROGETTO,
       PROG.CD_DIPARTIMENTO, DIP.DS_DIPARTIMENTO,
       DESCRIZIONE,
       PDG.TI_APPARTENENZA, PDG.TI_GESTIONE, PDG.CD_ELEMENTO_VOCE, EV.DS_ELEMENTO_VOCE,
       PDG.CD_LINEA_ATTIVITA, LA.DENOMINAZIONE,
       0 TOT_ENT_IST_A1,
       0 TOT_INC_IST_A1,
       SUM(IM_ENTRATA) TOT_ENT_AREE_A1,
       Sum(IM_INCASSI) TOT_INC_AREE_A1
From   PARAMETRI_CNR PAR,
       PDG_MODULO_ENTRATE_GEST PDG, V_CLASSIFICAZIONE_VOCI CLA, UNITA_ORGANIZZATIVA AREA,
       CDR, PROGETTO_GEST MODU, PROGETTO_GEST COMM, PROGETTO_GEST PROG, TIPO_PROGETTO, DIPARTIMENTO DIP,
       ELEMENTO_VOCE EV, LINEA_ATTIVITA LA
WHERE  PAR.ESERCIZIO = PDG.ESERCIZIO AND
       PAR.FL_NUOVO_PDG = 'N' AND
       PDG.ID_CLASSIFICAZIONE = CLA.ID_CLASSIFICAZIONE AND
       PDG.CD_CDS_AREA = AREA.CD_UNITA_ORGANIZZATIVA AND
       PDG.CD_CDR_ASSEGNATARIO = CDR.CD_CENTRO_RESPONSABILITA AND
       EV.ESERCIZIO         = PDG.ESERCIZIO        AND
       EV.TI_APPARTENENZA   = PDG.TI_APPARTENENZA  AND
       EV.TI_GESTIONE       = PDG.TI_GESTIONE      AND
       EV.CD_ELEMENTO_VOCE  = PDG.CD_ELEMENTO_VOCE AND
       PDG.CD_CDR_ASSEGNATARIO = LA.CD_CENTRO_RESPONSABILITA And
       PDG.CD_LINEA_ATTIVITA = LA.CD_LINEA_ATTIVITA And
       PDG.PG_PROGETTO = MODU.PG_PROGETTO And
       MODU.ESERCIZIO = PDG.ESERCIZIO And
       MODU.ESERCIZIO_PROGETTO_PADRE = COMM.ESERCIZIO AND
       MODU.PG_PROGETTO_PADRE = COMM.PG_PROGETTO AND
       COMM.ESERCIZIO_PROGETTO_PADRE = PROG.ESERCIZIO AND
       COMM.PG_PROGETTO_PADRE = PROG.PG_PROGETTO AND
       MODU.CD_TIPO_PROGETTO = TIPO_PROGETTO.CD_TIPO_PROGETTO AND
       DIP.CD_DIPARTIMENTO = PROG.CD_DIPARTIMENTO AND
       AREA.CD_TIPO_UNITA = 'AREA' AND
       PDG.FL_SOLA_LETTURA = 'N'
GROUP BY PDG.ESERCIZIO, PDG.CD_CDR_ASSEGNATARIO, CDR.DS_CDR,
       CD_CLASSIFICAZIONE, DS_CLASSIFICAZIONE, NR_LIVELLO,
       CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
       MODU.PG_PROGETTO, MODU.CD_PROGETTO, MODU.DS_PROGETTO,
       MODU.CD_TIPO_PROGETTO, TIPO_PROGETTO.DS_TIPO_PROGETTO,
       COMM.PG_PROGETTO, COMM.CD_PROGETTO, COMM.DS_PROGETTO,
       PROG.PG_PROGETTO, PROG.CD_PROGETTO, PROG.DS_PROGETTO,
       PROG.CD_DIPARTIMENTO, DIP.DS_DIPARTIMENTO,
       DESCRIZIONE, PDG.TI_APPARTENENZA, PDG.TI_GESTIONE, PDG.CD_ELEMENTO_VOCE, EV.DS_ELEMENTO_VOCE,
       PDG.CD_LINEA_ATTIVITA, LA.DENOMINAZIONE
UNION ALL
SELECT
       PDG.ESERCIZIO, PDG.CD_CDR_ASSEGNATARIO, CDR.DS_CDR,
       CD_CLASSIFICAZIONE, DS_CLASSIFICAZIONE,
       NR_LIVELLO, CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
       NULL PG_MODULO, NULL CD_MODULO, NULL DS_MODULO,
       COMM.CD_TIPO_PROGETTO CD_TIPO_MODULO, TIPO_PROGETTO.DS_TIPO_PROGETTO DS_TIPO_MODULO,
       COMM.PG_PROGETTO PG_COMMESSA, COMM.CD_PROGETTO CD_COMMESSA, COMM.DS_PROGETTO DS_COMMESSA,
       PROG.PG_PROGETTO, PROG.CD_PROGETTO, PROG.DS_PROGETTO,
       PROG.CD_DIPARTIMENTO, DIP.DS_DIPARTIMENTO,
       DESCRIZIONE,
       PDG.TI_APPARTENENZA, PDG.TI_GESTIONE, PDG.CD_ELEMENTO_VOCE, EV.DS_ELEMENTO_VOCE,
       PDG.CD_LINEA_ATTIVITA, LA.DENOMINAZIONE,
       SUM(IM_ENTRATA) TOT_ENT_IST_A1,
       Sum(IM_INCASSI) TOT_INC_IST_A1,
       0 TOT_ENT_AREE_A1,
       0 TOT_INC_AREE_A1
FROM   PARAMETRI_CNR PAR,
       PDG_MODULO_ENTRATE_GEST PDG, V_CLASSIFICAZIONE_VOCI CLA,
       CDR, PROGETTO_GEST COMM, PROGETTO_GEST PROG, TIPO_PROGETTO, DIPARTIMENTO DIP,
       ELEMENTO_VOCE EV, LINEA_ATTIVITA LA
WHERE  PAR.ESERCIZIO = PDG.ESERCIZIO AND
       PAR.FL_NUOVO_PDG = 'Y' AND
       PDG.ID_CLASSIFICAZIONE = CLA.ID_CLASSIFICAZIONE AND
       PDG.CD_CDR_ASSEGNATARIO = CDR.CD_CENTRO_RESPONSABILITA AND
       EV.ESERCIZIO         = PDG.ESERCIZIO        AND
       EV.TI_APPARTENENZA   = PDG.TI_APPARTENENZA  AND
       EV.TI_GESTIONE       = PDG.TI_GESTIONE      AND
       EV.CD_ELEMENTO_VOCE  = PDG.CD_ELEMENTO_VOCE And
       PDG.CD_CDR_ASSEGNATARIO = LA.CD_CENTRO_RESPONSABILITA And
       PDG.CD_LINEA_ATTIVITA = LA.CD_LINEA_ATTIVITA And
       COMM.PG_PROGETTO = PDG.PG_PROGETTO AND
       COMM.ESERCIZIO = PDG.ESERCIZIO And
       COMM.ESERCIZIO_PROGETTO_PADRE = PROG.ESERCIZIO AND
       COMM.PG_PROGETTO_PADRE = PROG.PG_PROGETTO AND
       COMM.CD_TIPO_PROGETTO = TIPO_PROGETTO.CD_TIPO_PROGETTO(+) AND
       DIP.CD_DIPARTIMENTO = PROG.CD_DIPARTIMENTO AND
       PDG.FL_SOLA_LETTURA = 'N'
GROUP BY PDG.ESERCIZIO, PDG.CD_CDR_ASSEGNATARIO, CDR.DS_CDR,
       CD_CLASSIFICAZIONE, DS_CLASSIFICAZIONE, NR_LIVELLO,
       CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7,
       NULL, NULL, NULL,
       COMM.CD_TIPO_PROGETTO, TIPO_PROGETTO.DS_TIPO_PROGETTO,
       COMM.PG_PROGETTO, COMM.CD_PROGETTO, COMM.DS_PROGETTO,
       PROG.PG_PROGETTO, PROG.CD_PROGETTO, PROG.DS_PROGETTO,
       PROG.CD_DIPARTIMENTO, DIP.DS_DIPARTIMENTO,
       DESCRIZIONE, PDG.TI_APPARTENENZA, PDG.TI_GESTIONE, PDG.CD_ELEMENTO_VOCE, EV.DS_ELEMENTO_VOCE,
       PDG.CD_LINEA_ATTIVITA, LA.DENOMINAZIONE;
