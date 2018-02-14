--------------------------------------------------------
--  DDL for View VM_COSTI_DIP_GEST_VIEW
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VM_COSTI_DIP_GEST_VIEW" ("ESERCIZIO", "TI_PREV_CONS", "MESE", "MATRICOLA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_UNITA_ORGANIZZATIVA", "IMPORTO", "IMPORTO_ONERI_CNR", "IMPORTO_TFR") AS 
  SELECT
-- =================================================================================================
-- Date: 13/10/2005
-- Version 1.0
--
-- Creazione vista
--
-- =================================================================================================
        A.esercizio,
        A.ti_prev_cons,
        A.mese,
        A.matricola,
        A.ti_appartenenza,
        A.ti_gestione,
        A.cd_elemento_voce,
        A.cd_unita_organizzativa,
        SUM(A.importo),
        SUM(A.importo_oneri_cnr),
        SUM(A.importo_tfr)
FROM    VM_COSTI_DIP_GEST_PRE_VIEW A
GROUP BY A.esercizio,
         A.ti_prev_cons,
         A.mese,
         A.matricola,
         A.ti_appartenenza,
         A.ti_gestione,
         A.cd_elemento_voce,
         A.cd_unita_organizzativa
;
