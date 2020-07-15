--------------------------------------------------------
--  DDL for View V_PDG_SPE_BILANCIO_PREV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_SPE_BILANCIO_PREV" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "DS_CLASSIFICAZIONE_1", "CD_LIVELLO2", "DS_CLASSIFICAZIONE_2", "CD_LIVELLO3", "CD_LIVELLO4", "DS_CLASSIFICAZIONE_4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "PG_MODULO", "CD_MODULO", "DS_MODULO", "CD_TIPO_MODULO", "DS_TIPO_MODULO", "PG_COMMESSA", "CD_COMMESSA", "DS_COMMESSA", "PG_PROGETTO", "CD_PROGETTO", "DS_PROGETTO", "CD_DIPARTIMENTO", "DS_DIPARTIMENTO", "IM_DEC_IST_INT", "IM_DEC_IST_EST", "IM_DEC_AREA_INT", "IM_DEC_AREA_EST", "TRATT_ECON_INT", "TRATT_ECON_EST", "IM_ACC_ALTRE_SP_INT", "IM_PREV_A2", "IM_PREV_A3") AS 
  (select 
v.ESERCIZIO, 
v.CD_CENTRO_RESPONSABILITA, 
v.DS_CDR, 
v.CD_CLASSIFICAZIONE, 
v.DS_CLASSIFICAZIONE, 
v.NR_LIVELLO, 
v.CD_LIVELLO1, 
liv_1.DS_CLASSIFICAZIONE,
v.CD_LIVELLO2, 
liv_2.DS_CLASSIFICAZIONE,
v.CD_LIVELLO3, 
v.CD_LIVELLO4, 
liv_4.DS_CLASSIFICAZIONE,
v.CD_LIVELLO5, 
v.CD_LIVELLO6, 
v.CD_LIVELLO7, 
v.PG_MODULO, 
v.CD_MODULO,
v.DS_MODULO, 
v.CD_TIPO_MODULO, 
v.DS_TIPO_MODULO, 
v.PG_COMMESSA, 
v.CD_COMMESSA, 
v.DS_COMMESSA, 
v.PG_PROGETTO, 
v.CD_PROGETTO, 
v.DS_PROGETTO, 
v.CD_DIPARTIMENTO, 
UPPER(v.DS_DIPARTIMENTO), 
v.IM_DEC_IST_INT, 
v.IM_DEC_IST_EST, 
v.IM_DEC_AREA_INT, 
v.IM_DEC_AREA_EST, 
v.TRATT_ECON_INT, 
v.TRATT_ECON_EST, 
v.IM_ACC_ALTRE_SP_INT, 
v.IM_PREV_A2, 
v.IM_PREV_A3
from V_CONS_PDG_SPE_BIL_IST_DIP_FO v , 
        v_classificazione_voci class, 
        V_CLASSIFICAZIONE_VOCI LIV_1,
        V_CLASSIFICAZIONE_VOCI LIV_2,
        V_CLASSIFICAZIONE_VOCI LIV_4
WHERE   v.esercizio = class.esercizio
and   v.cd_classificazione = class.cd_classificazione
AND     CLASS.ESERCIZIO                = LIV_1.ESERCIZIO
AND     CLASS.TI_GESTIONE              = LIV_1.TI_GESTIONE
AND     CLASS.CD_LIVELLO1              = LIV_1.CD_LIVELLO1
AND     LIV_1.CD_LIVELLO2              IS NULL
AND     CLASS.ESERCIZIO                = LIV_2.ESERCIZIO
AND     CLASS.TI_GESTIONE              = LIV_2.TI_GESTIONE
AND     CLASS.CD_LIVELLO1              = LIV_2.CD_LIVELLO1
AND     CLASS.CD_LIVELLO2              = LIV_2.CD_LIVELLO2
AND     LIV_2.CD_LIVELLO3              IS NULL
AND     CLASS.ESERCIZIO                = LIV_4.ESERCIZIO
AND     CLASS.TI_GESTIONE              = LIV_4.TI_GESTIONE
AND     CLASS.CD_LIVELLO1              = LIV_4.CD_LIVELLO1
AND     CLASS.CD_LIVELLO2              = LIV_4.CD_LIVELLO2
AND     CLASS.CD_LIVELLO3              = LIV_4.CD_LIVELLO3
AND     CLASS.CD_LIVELLO4              = LIV_4.CD_LIVELLO4
AND     LIV_4.CD_LIVELLO5              IS Null);