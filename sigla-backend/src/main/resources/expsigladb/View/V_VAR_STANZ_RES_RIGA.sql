--------------------------------------------------------
--  DDL for View V_VAR_STANZ_RES_RIGA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_VAR_STANZ_RES_RIGA" ("ESERCIZIO", "ESERCIZIO_RES", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DENOMINAZIONE", "DS_LINEA_ATTIVITA", "PG_PROGETTO", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_NATURA", "DS_NATURA", "CD_MODULO", "DS_MODULO", "PG_VARIAZIONE", "STATO", "STANZ_ATTUALE", "IM_VARIAZIONE", "DS_CLASSIFICAZIONE", "TITOLO", "CATEGORIA") AS 
  SELECT DISTINCT
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
vr.ESERCIZIO,
vr.esercizio_res,
vr.cd_cdr,
c.ds_cdr,
vr.cd_linea_attivita,
v.denominazione_la,
v.ds_linea_attivita,
v.pg_progetto,
vr.cd_elemento_voce,
e.ds_elemento_voce,
v.cd_natura,
v.ds_natura,
v.cd_modulo,
v.ds_modulo,
vr.pg_variazione,
t.stato,
Cal_Assestato.ASSESTATO_SPESA_RES(vr.ESERCIZIO, vr.esercizio_res, NULL, NULL, vr.cd_cdr, vr.cd_elemento_voce, vr.cd_linea_attivita, NULL),
vr.im_variazione,
CV.ds_classificazione,
CV.cd_livello1 titolo,
CV.cd_livello2 categoria
FROM
VAR_STANZ_RES_RIGA vr,  v_prog_com_mod v, CDR c, ELEMENTO_VOCE e, CLASSIFICAZIONE_VOCI CV, VAR_STANZ_RES t
WHERE vr.ESERCIZIO = v.ESERCIZIO
AND vr.cd_cdr = v.CDR
AND vr.cd_linea_attivita = v.cd_linea_attivita
AND vr.cd_cdr = c.cd_centro_responsabilita
AND vr.cd_elemento_voce = e.cd_elemento_voce
AND vr.ESERCIZIO = e.ESERCIZIO
AND e.id_classificazione = CV.id_classificazione
And vr.esercizio = t.esercizio
And vr.pg_variazione = t.pg_variazione;
