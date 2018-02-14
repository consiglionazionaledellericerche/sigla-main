--------------------------------------------------------
--  DDL for View VM_COSTI_DIP_GEST_PRE_VIEW
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VM_COSTI_DIP_GEST_PRE_VIEW" ("ESERCIZIO", "TI_PREV_CONS", "MESE", "MATRICOLA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_UNITA_ORGANIZZATIVA", "IMPORTO", "IMPORTO_ONERI_CNR", "IMPORTO_TFR") AS 
  SELECT
-- =================================================================================================
-- Date: 13/10/2005
-- Version 1.0
--
-- Creazione vista
--
-- =================================================================================================
        A.anno,
        'C',
        A.mese,
        A.matricola,
        'D',
        'S',
        A.voce_cia,
        DECODE(A.tit, NULL, NULL, SUBSTR(A.tit,1,3) || '.' || SUBSTR(A.tit,4,3)),
        A.importo,
        A.oneri_cnr,
        A.tfr
FROM    CNR_COSTI_GEST A
;
