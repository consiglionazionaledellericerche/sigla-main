--------------------------------------------------------
--  DDL for View V_SINTESI_LDA_COMRES
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SINTESI_LDA_COMRES" ("ESERCIZIO", "CDR", "CDS", "UO", "COMMESSA", "MODULO", "LDA", "SPESE_A1_INI", "SPESE_A1_VAR", "SPESE_A1", "RESIDUO_A1", "OBBL_A1", "DOC_OBBL_A1", "MND_OBBL_A1", "FUNZ", "NAT", "DS_CDR", "DS_COMMESSA", "DS_MODULO", "DS_LDA", "CDR_AFF", "UO_AFF", "DIP_PRG") AS 
  Select ESERCIZIO,
       CDR, CDS, UO, COMMESSA, MODULO, LDA,
       Sum(SPESE_A1_INI), Sum(SPESE_A1_VAR), Sum(SPESE_A1_INI + SPESE_A1_VAR), Sum(RESIDUO_A1),
       Sum(OBBL_A1), Sum(DOC_OBBL_A1), Sum(MND_OBBL_A1),
       FUNZ, NAT,
       DS_CDR, DS_COMMESSA, DS_MODULO, DS_LDA,
       CDR_AFF, UO_AFF, DIP_PRG
From V_ANALITICA_LDA_COMRES
Group By ESERCIZIO,
         CDR,
         CDS,
         UO,
         COMMESSA,
         MODULO,
         LDA,
         FUNZ,
         NAT,
         DS_CDR,
         DS_COMMESSA,
         DS_MODULO,
         DS_LDA,
         CDR_AFF,
         UO_AFF,
         DIP_PRG;
