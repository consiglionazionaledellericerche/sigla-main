--------------------------------------------------------
--  DDL for View ELEMENTO_VOCE_CLASS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "ELEMENTO_VOCE_CLASS" ("ESERCIZIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "ID_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "ID_CLASS_PADRE", "FL_ACCENTRATO", "FL_DECENTRATO", "CDR_ACCENTRATORE", "FL_LIMITE_ASS_OBBLIG", "FL_VOCE_PERSONALE", "CD_PARTE", "FL_VOCE_SAC", "CD_TIPO_SPESA_SAC", "CD_TIPO_SPESA_IST") AS 
  select ev.ESERCIZIO               ,
TI_APPARTENENZA                ,
ev.TI_GESTIONE                    ,
CD_ELEMENTO_VOCE               ,
DS_ELEMENTO_VOCE               ,
ev.ID_CLASSIFICAZIONE             ,
DS_CLASSIFICAZIONE             ,
CD_LIVELLO1                    ,
CD_LIVELLO2                    ,
CD_LIVELLO3                    ,
CD_LIVELLO4                    ,
CD_LIVELLO5                    ,
CD_LIVELLO6                    ,
CD_LIVELLO7                    ,
ID_CLASS_PADRE                 ,
FL_ACCENTRATO                  ,
FL_DECENTRATO                  ,
CDR_ACCENTRATORE               ,
FL_LIMITE_ASS_OBBLIG           ,
FL_VOCE_PERSONALE              ,
CD_PARTE                       ,
FL_VOCE_SAC                    ,
CD_TIPO_SPESA_SAC              ,
CD_TIPO_SPESA_IST
from elemento_voce ev, classificazione_voci p
where ev.ID_CLASSIFICAZIONE = p.ID_CLASSIFICAZIONE;
