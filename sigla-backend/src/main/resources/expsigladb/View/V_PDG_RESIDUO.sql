--------------------------------------------------------
--  DDL for View V_PDG_RESIDUO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_RESIDUO" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "STATO_RESIDUO", "IM_MASSA_SPENDIBILE", "PG_DETTAGLIO", "CD_CDR_LINEA", "CD_LINEA_ATTIVITA", "LA_DENOMINAZIONE", "CD_FUNZIONE", "CD_NATURA", "CD_PROGETTO", "CD_DIPARTIMENTO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CODICE_CLA_S", "CLA_S_DESCRIZIONE", "RES_DESCRIZIONE", "IM_RESIDUO", "STATO_DETTAGLIO") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista di estrazione dei dati della ricostruzione dei residui per l'anno 2005
-- utili alla relativa mappa di consultazione
--
-- History:
--
-- Date: 28/06/2005
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
pdg_residuo.esercizio, pdg_residuo.cd_centro_responsabilita, cdr.ds_cdr, pdg_residuo.stato stato_residuo, im_massa_spendibile,
pg_dettaglio, cd_cdr_linea, pdg_residuo_det.cd_linea_attivita, linea_attivita.denominazione, pdg_residuo_det.cd_funzione,
pdg_residuo_det.cd_natura, progetto.cd_progetto, progetto.cd_dipartimento, pdg_residuo_det.ti_appartenenza,
pdg_residuo_det.ti_gestione, pdg_residuo_det.cd_elemento_voce, elemento_voce.ds_elemento_voce,
elemento_voce.cod_cla_s, classificazione_spese.descrizione, pdg_residuo_det.descrizione, im_residuo, pdg_residuo_det.stato stato_dettaglio
from pdg_residuo, pdg_residuo_det, cdr, linea_attivita, elemento_voce, progetto_gest progetto, classificazione_spese
where pdg_residuo.esercizio=pdg_residuo_det.esercizio
  and pdg_residuo.cd_centro_responsabilita=pdg_residuo_det.cd_centro_responsabilita
  and pdg_residuo.cd_centro_responsabilita=cdr.cd_centro_responsabilita
  and pdg_residuo_det.cd_cdr_linea=linea_attivita.cd_centro_responsabilita
  and pdg_residuo_det.cd_linea_attivita=linea_attivita.cd_linea_attivita
  and pdg_residuo_det.esercizio=elemento_voce.esercizio
  and pdg_residuo_det.ti_appartenenza=elemento_voce.ti_appartenenza
  and pdg_residuo_det.ti_gestione=elemento_voce.ti_gestione
  and pdg_residuo_det.cd_elemento_voce=elemento_voce.cd_elemento_voce
  And linea_attivita.pg_progetto=progetto.pg_progetto (+)
  And (progetto.esercizio Is Null Or progetto.esercizio = pdg_residuo.esercizio)
  And elemento_voce.esercizio_cla_s=classificazione_spese.esercizio (+)
  And elemento_voce.cod_cla_s=classificazione_spese.codice_cla_s (+)
Order By pdg_residuo.esercizio, pdg_residuo.cd_centro_responsabilita, pg_dettaglio
;

   COMMENT ON TABLE "V_PDG_RESIDUO"  IS 'Vista di estrazione dei dati della ricostruzione dei residui per l''anno 2005';
