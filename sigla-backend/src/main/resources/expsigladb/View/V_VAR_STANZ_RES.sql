--------------------------------------------------------
--  DDL for View V_VAR_STANZ_RES
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_VAR_STANZ_RES" ("ESERCIZIO", "ESERCIZIO_RES", "PG_VARIAZIONE", "DS_VARIAZIONE", "CD_CDR_PROPONENTE", "DS_CDR_PROPONENTE", "CD_CDR_ASSEGNATARIO", "DS_CDR_ASSEGNATARIO", "IM_VARIAZIONE", "IM_SPESA", "STATO") AS 
  Select
--
--
-- Date: 16/05/2008
-- Version: 1.0
--
-- Vista per la stampa delle Variazioni allo Stanziamento Residuo
--
-- History:
--
-- Date: 16/05/2008
-- Version: 1.0
-- Creazione
--
-- Body:
--
--
t.ESERCIZIO,
t.ESERCIZIO_RES,
t.PG_VARIAZIONE,
t.DS_VARIAZIONE,
t.CD_CENTRO_RESPONSABILITA,
ct.DS_CDR,
r.CD_CDR,
cr.DS_CDR,
Sum(r.IM_VARIAZIONE),
a.IM_SPESA,
t.STATO
From VAR_STANZ_RES t, VAR_STANZ_RES_RIGA r, cdr ct, cdr cr,
ASS_VAR_STANZ_RES_CDR a
Where t.esercizio = r.esercizio
And t.pg_variazione = r.pg_variazione
And t.cd_centro_responsabilita = ct.cd_centro_responsabilita
And t.esercizio = a.esercizio
And t.pg_variazione = a.pg_variazione
And r.cd_cdr  = a.cd_centro_responsabilita
And r.cd_cdr = cr.cd_centro_responsabilita
group by t.ESERCIZIO,t.ESERCIZIO_RES,t.PG_VARIAZIONE,t.DS_VARIAZIONE,t.CD_CENTRO_RESPONSABILITA,ct.DS_CDR,r.CD_CDR,cr.DS_CDR,a.IM_SPESA,t.STATO;
