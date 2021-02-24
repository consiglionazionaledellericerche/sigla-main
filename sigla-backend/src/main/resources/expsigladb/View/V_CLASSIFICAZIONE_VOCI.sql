--------------------------------------------------------
--  DDL for View V_CLASSIFICAZIONE_VOCI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CLASSIFICAZIONE_VOCI" ("ID_CLASSIFICAZIONE", "ESERCIZIO", "TI_GESTIONE", "DS_CLASSIFICAZIONE", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "ID_CLASS_PADRE", "NR_LIVELLO", "FL_MASTRINO", "FL_CLASS_SAC", "FL_SOLO_GESTIONE", "FL_PIANO_RIPARTO", "FL_ACCENTRATO", "FL_DECENTRATO", "FL_ESTERNA_DA_QUADRARE_SAC", "CDR_ACCENTRATORE", "TI_CLASSIFICAZIONE", "FL_PREV_OBB_ANNO_SUC", "IM_LIMITE_ASSESTATO", "DUVA", "UTUV", "DACR", "UTCR", "PG_VER_REC", "CD_LIVELLO_LAST", "CD_CLASSIFICAZIONE") AS
  SELECT
--
-- Date: 08/09/2005
-- Version: 1.0
--
-- Estrae le Classificazioni Ufficiali associabili al piano dei conti finanziario
--
-- History:
-- Date: 08/09/2005
-- Version: 1.0
-- Creazione
ID_CLASSIFICAZIONE,
ESERCIZIO,
TI_GESTIONE,
DS_CLASSIFICAZIONE,
CD_LIVELLO1,
CD_LIVELLO2,
CD_LIVELLO3,
CD_LIVELLO4,
CD_LIVELLO5,
CD_LIVELLO6,
CD_LIVELLO7,
ID_CLASS_PADRE,
decode(length(CD_LIVELLO1), Null, 0,
decode(length(CD_LIVELLO2), Null, 1,
decode(length(CD_LIVELLO3), Null, 2,
decode(length(CD_LIVELLO4), Null, 3,
decode(length(CD_LIVELLO5), Null, 4,
decode(length(CD_LIVELLO6), Null, 5,
decode(length(CD_LIVELLO7), Null, 6, 7))))))) NR_LIVELLO,
FL_MASTRINO,
FL_CLASS_SAC,
FL_SOLO_GESTIONE,
FL_PIANO_RIPARTO,
FL_ACCENTRATO,
FL_DECENTRATO,
FL_ESTERNA_DA_QUADRARE_SAC,
CDR_ACCENTRATORE,
TI_CLASSIFICAZIONE,
FL_PREV_OBB_ANNO_SUC,
IM_LIMITE_ASSESTATO,
DUVA,
UTUV,
DACR,
UTCR,
PG_VER_REC,
decode(length(CD_LIVELLO1), Null, Null,
decode(length(CD_LIVELLO2), Null, CD_LIVELLO1,
decode(length(CD_LIVELLO3), Null, CD_LIVELLO2,
decode(length(CD_LIVELLO4), Null, CD_LIVELLO3,
decode(length(CD_LIVELLO5), Null, CD_LIVELLO4,
decode(length(CD_LIVELLO6), Null, CD_LIVELLO5,
decode(length(CD_LIVELLO7), Null, CD_LIVELLO6, CD_LIVELLO7))))))) CD_LIVELLO_LAST,
decode(length(CD_LIVELLO1), Null, Null, CD_LIVELLO1)||
decode(length(CD_LIVELLO2), Null, Null, '.'||CD_LIVELLO2)||
decode(length(CD_LIVELLO3), Null, Null, '.'||CD_LIVELLO3)||
decode(length(CD_LIVELLO4), Null, Null, '.'||CD_LIVELLO4)||
decode(length(CD_LIVELLO5), Null, Null, '.'||CD_LIVELLO5)||
decode(length(CD_LIVELLO6), Null, Null, '.'||CD_LIVELLO6)||
decode(length(CD_LIVELLO7), Null, Null, '.'||CD_LIVELLO7) CD_CLASSIFICAZIONE
from Classificazione_voci;
