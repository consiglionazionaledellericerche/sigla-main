--------------------------------------------------------
--  DDL for View V_PDG_RESIDUO_COMPLETA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_RESIDUO_COMPLETA" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "STATO_RESIDUO", "IM_MASSA_SPENDIBILE", "PG_DETTAGLIO", "CD_CDR_LINEA", "CD_LINEA_ATTIVITA", "LA_DENOMINAZIONE", "CD_FUNZIONE", "CD_NATURA", "CD_PROGETTO", "CD_DIPARTIMENTO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CODICE_CLA_S", "CLA_S_DESCRIZIONE", "RES_DESCRIZIONE", "IM_RESIDUO", "STATO_DETTAGLIO") AS 
  Select
--
-- Date: 12/07/2005
-- Version: 1.0
--
-- Vista di estrazione dei dati della ricostruzione dei residui per l'anno 2005
-- utili alla stampa BO di controllo compilazione
--
-- History:
--
-- Body:
--
"ESERCIZIO","CD_CENTRO_RESPONSABILITA","DS_CDR","STATO_RESIDUO","IM_MASSA_SPENDIBILE","PG_DETTAGLIO","CD_CDR_LINEA","CD_LINEA_ATTIVITA","LA_DENOMINAZIONE","CD_FUNZIONE","CD_NATURA","CD_PROGETTO","CD_DIPARTIMENTO","TI_APPARTENENZA","TI_GESTIONE","CD_ELEMENTO_VOCE","DS_ELEMENTO_VOCE","CODICE_CLA_S","CLA_S_DESCRIZIONE","RES_DESCRIZIONE","IM_RESIDUO","STATO_DETTAGLIO"
From v_pdg_residuo
where stato_dettaglio != 'A'
Union
Select
pdg_residuo.esercizio, pdg_residuo.cd_centro_responsabilita, cdr.DS_CDR, pdg_residuo.stato stato_residuo, im_massa_spendibile,
Null, Null, Null, Null, Null, Null, Null, Null, Null,
Null, Null, Null, Null, Null, Null, Null, Null
from pdg_residuo,cdr
where pdg_residuo.cd_centro_responsabilita=cdr.cd_centro_responsabilita
and Not Exists
(Select 1 From pdg_residuo_det
 Where pdg_residuo.esercizio=pdg_residuo_det.esercizio
   and pdg_residuo.cd_centro_responsabilita=pdg_residuo_det.cd_centro_responsabilita)
   Order By 1, 2, 3;
