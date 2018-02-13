--------------------------------------------------------
--  DDL for View V_CONS_VAR_BILANCIO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_VAR_BILANCIO" ("CD_CDS", "ESERCIZIO", "TI_APPARTENENZA", "PG_VARIAZIONE", "DS_VARIAZIONE", "DS_DELIBERA", "TI_VARIAZIONE", "CD_CAUSALE_VAR_BILANCIO", "DS_CAUSALE_VAR_BILANCIO", "STATO", "ESERCIZIO_IMPORTI", "ESERCIZIO_PDG_VARIAZIONE", "PG_VARIAZIONE_PDG", "ESERCIZIO_VAR_STANZ_RES", "PG_VAR_STANZ_RES", "TI_GESTIONE", "CD_VOCE", "IM_VARIAZIONE") AS 
  Select var_bilancio.CD_CDS, var_bilancio.ESERCIZIO, var_bilancio.TI_APPARTENENZA, var_bilancio.PG_VARIAZIONE,
DS_VARIAZIONE, DS_DELIBERA, TI_VARIAZIONE, var_bilancio.CD_CAUSALE_VAR_BILANCIO, DS_CAUSALE_VAR_BILANCIO, STATO, ESERCIZIO_IMPORTI, ESERCIZIO_PDG_VARIAZIONE,
PG_VARIAZIONE_PDG, ESERCIZIO_VAR_STANZ_RES, PG_VAR_STANZ_RES, TI_GESTIONE, CD_VOCE, IM_VARIAZIONE
--
-- Date: 23/05/2006
-- Version: 1.0
--
-- Consultazione Variazioni al Bilancio
--
--
-- History:
--
-- Date: 23/05/2006
-- Version: 1.0
-- Creazione:
From var_bilancio, var_bilancio_det, causale_var_bilancio
Where var_bilancio.cd_cds = var_bilancio_det.cd_cds
And var_bilancio.esercizio = var_bilancio_det.esercizio
And var_bilancio.ti_appartenenza = var_bilancio_det.ti_appartenenza
And var_bilancio.pg_variazione = var_bilancio_det.pg_variazione
And var_bilancio.cd_causale_var_bilancio = causale_var_bilancio.cd_causale_var_bilancio
Order By var_bilancio.cd_cds, var_bilancio.esercizio, var_bilancio.ti_appartenenza,
var_bilancio.pg_variazione, var_bilancio_det.ti_gestione, var_bilancio_det.cd_voce

;
