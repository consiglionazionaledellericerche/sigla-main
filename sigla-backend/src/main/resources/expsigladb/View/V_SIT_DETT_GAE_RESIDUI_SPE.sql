--------------------------------------------------------
--  DDL for View V_SIT_DETT_GAE_RESIDUI_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIT_DETT_GAE_RESIDUI_SPE" ("ESERCIZIO", "CDS", "UO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "CD_NATURA", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_VOCE", "DS_ELEMENTO_VOCE", "ESERCIZIO_RES", "INIZIALI_RIB", "VAR_RES_PRO_PIU", "VAR_RES_PRO_MENO", "TOTALE", "LIQUIDATI_RIB", "PAGATI_RIB", "DA_PAGARE_RIB", "ASSUNTI_ES_IN_CORSO", "LIQUIDATI_ES_IN_CORSO", "PAGATI_ES_IN_CORSO", "DA_PAGARE_ES_IN_CORSO") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista SITUAZIONE Dettagliata GAE sui residui di spesa
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
ESERCIZIO, substr(CDS,1,50), substr(UO,1,50), CD_CENTRO_RESPONSABILITA, DS_CDR, CD_LINEA_ATTIVITA, DS_LINEA_ATTIVITA,
       CD_NATURA, CD_PROGETTO, DS_PROGETTO, CD_COMMESSA, DS_COMMESSA, CD_MODULO, DS_MODULO, TI_APPARTENENZA, TI_GESTIONE,
       CD_ELEMENTO_VOCE, CD_VOCE, DS_ELEMENTO_VOCE, ESERCIZIO_RES,
-- INIZIALI_RIB
       Nvl(RES_PRO_INI, 0) + Nvl(RES_IMP_RIBALTATI, 0) INIZIALI_RIB,
-- VAR_RES_PRO_PIU
       Nvl(VAR_RES_PRO_PIU, 0) VAR_RES_PRO_PIU,
-- VAR_RES_PRO_MENO
       Nvl(VAR_RES_PRO_MENO, 0) VAR_RES_PRO_MENO,
-- TOTALE
      (Nvl(RES_PRO_INI, 0) + Nvl(RES_IMP_RIBALTATI, 0)) + Nvl(VAR_RES_PRO_PIU, 0) - Nvl(VAR_RES_PRO_MENO, 0) TOTALE,
-- LIQUIDATI_RIB
       Nvl(LIQUIDATO_PRO, 0) + Nvl(RES_IMP_RIB_LIQ, 0) LIQUIDATI_RIB,
-- PAGATI_RIB
       Nvl(PAGATO_PRO, 0) + Nvl(RES_IMP_RIB_PAG, 0) PAGATI_RIB,
-- DA_PAGARE_RIB
      ((Nvl(RES_PRO_INI, 0) + Nvl(RES_IMP_RIBALTATI, 0)) + Nvl(VAR_RES_PRO_PIU, 0) - Nvl(VAR_RES_PRO_MENO, 0)) -
       (Nvl(PAGATO_PRO, 0) + Nvl(RES_IMP_RIB_PAG, 0)) DA_PAGARE_RIB,
-- ASSUNTI_ES_IN_CORSO
       Nvl(RES_IMP_EM_ESE, 0) ASSUNTI_ES_IN_CORSO,
-- LIQUIDATI_ES_IN_CORSO
       Nvl(RES_IMP_EM_ESE_LIQ, 0) LIQUIDATI_ES_IN_CORSO,
-- PAGATI_ES_IN_CORSO
       Nvl(RES_IMP_EM_ESE_PAG, 0) PAGATI_ES_IN_CORSO,
-- DA_PAGARE_ES_IN_CORSO
       Nvl(RES_IMP_EM_ESE, 0) - Nvl(RES_IMP_EM_ESE_PAG, 0) DA_PAGARE_ES_IN_CORSO
From  V_SIT_GAE_RESIDUI_SPESA;