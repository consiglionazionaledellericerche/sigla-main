--------------------------------------------------------
--  DDL for View PRT_VPG_PREV_FIN_ENT_SPE_DEC_D
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_VPG_PREV_FIN_ENT_SPE_DEC_D" ("ID", "CHIAVE", "TIPO", "SEQUENZA", "ESERCIZIO", "CDR", "LINEA", "PG_MODULO", "DIP", "CD_LIVELLO1", "CD_LIVELLO2", "RES_PRES_AC", "PREV_COMPETENZA_AC", "PREV_FULL_AC", "PREV_CASSA_AC", "RES_INIZ_REALI_AP", "PREV_ASS_COMP_AP", "PREV_ASS_FULL_AP", "PREV_ASS_CASSA_AP") AS 
  SELECT ID, CHIAVE, TIPO, SEQUENZA,
IMPORTO_1,   -- ESERCIZIO
ATTRIBUTO_1,    -- CDR
ATTRIBUTO_2,    -- LINEA
IMPORTO_2,    -- PG_MODULO
ATTRIBUTO_3,    -- DIP
ATTRIBUTO_4,    -- CD_LIVELLO1
ATTRIBUTO_5,    -- CD_LIVELLO2,
IMPORTO_3,    -- RES_PRES_AC
IMPORTO_4,    -- PREV_COMPETENZA_AC
IMPORTO_5,    -- PREV_FULL_AC
IMPORTO_6,    -- PREV_CASSA_AC,
IMPORTO_7,    -- RES_INIZ_REALI_AP
IMPORTO_8,    -- PREV_ASS_COMP_AP
IMPORTO_9,    -- PREV_ASS_FULL_AP
IMPORTO_10    -- PREV_ASS_CASSA_AP
From TMP_REPORT_GENERICO
;