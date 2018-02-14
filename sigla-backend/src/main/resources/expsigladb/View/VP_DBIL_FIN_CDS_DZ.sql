--------------------------------------------------------
--  DDL for View VP_DBIL_FIN_CDS_DZ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DBIL_FIN_CDS_DZ" ("ESERCIZIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_PARTE", "CDC_TITOLO", "CDC_SEZIONE", "CDC_RUBRICA", "CDC_CAPITOLO", "CDC_ARTICOLO", "CD_CDS", "DS_UNITA_ORGANIZZATIVA", "CD_TIPO_UNITA", "DS_ELEMENTO_VOCE", "DS_TITOLO", "IM_STANZ_INIZIALE_AP", "IM_STANZ_INIZIALE", "VARIAZIONI_PIU", "VARIAZIONI_MENO") AS 
  (select
--
-- Date: 18/12/2002
-- Version: 1.0
--
-- Vista di stampa del bilancio finanziario dei CdS per importi significativi per la stampa diversi da 0
--
-- History:
--
-- Date: 18/12/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
 ESERCIZIO,
TI_APPARTENENZA, TI_GESTIONE, CD_PARTE, CDC_TITOLO,
CDC_SEZIONE, CDC_RUBRICA, CDC_CAPITOLO, CDC_ARTICOLO,
CD_CDS, DS_UNITA_ORGANIZZATIVA, CD_TIPO_UNITA, DS_ELEMENTO_VOCE,
DS_TITOLO, IM_STANZ_INIZIALE_AP, IM_STANZ_INIZIALE, VARIAZIONI_PIU,
VARIAZIONI_MENO from VP_DBIL_FIN_CDS
where
     IM_STANZ_INIZIALE_AP <> 0
  or IM_STANZ_INIZIALE <> 0
)
;

   COMMENT ON TABLE "VP_DBIL_FIN_CDS_DZ"  IS 'Vista di stampa del bilancio finanziario dei CdS per importi significativi per la stampa diversi da 0';
