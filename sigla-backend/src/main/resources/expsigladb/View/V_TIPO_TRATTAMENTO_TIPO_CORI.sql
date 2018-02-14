--------------------------------------------------------
--  DDL for View V_TIPO_TRATTAMENTO_TIPO_CORI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TIPO_TRATTAMENTO_TIPO_CORI" ("CD_TRATTAMENTO", "DT_INI_VAL_TRATTAMENTO", "DT_FIN_VAL_TRATTAMENTO", "TI_ANAGRAFICO", "FL_DETRAZIONI_FAMILIARI", "FL_DETRAZIONI_DIPENDENTE", "FL_TASSAZIONE_SEPARATA", "FL_IRPEF_ANNUALIZZATA", "CD_CORI", "DT_INI_VAL_TRATT_CORI", "DT_FIN_VAL_TRATT_CORI", "ID_RIGA", "SEGNO", "CALCOLO_IMPONIBILE", "DT_INI_VAL_TIPO_CORI", "DT_FIN_VAL_TIPO_CORI", "TI_CASSA_COMPETENZA", "PRECISIONE", "PG_CLASSIFICAZIONE_MONTANTI", "CD_CLASSIFICAZIONE_CORI", "FL_SCRIVI_MONTANTI", "FL_USO_IN_LORDIZZA", "FL_GLA", "FL_ALIQUOTA_RIDOTTA", "CD_ENTE_PREV_STI", "FL_SOSPENSIONE_IRPEF", "FL_CREDITO_IRPEF") AS 
  SELECT
--==============================================================================
--
-- Date: 17/06/2002
-- Version: 1.1
--
-- Vista di estrazione di TIPO_TRATTAMENTO join TRATTAMENTO_CORI join
-- TIPO_CONTRIBUTO_RITENUTA per calcolo dei compensi. Tutte le tabelle sono storiche
--
-- History:
--
-- Date: 16/04/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 17/06/2002
-- Version: 1.1
--
-- Aggiunta l'estrazione dei nuovi attributi definiti sulla TIPO_CONTRIBUTO_RITENUTA:
--     pg_classificazione_montanti
--     cd_classificazione_cori
--     fl_scrivi_montanti
--     fl_uso_in_lordizza
--
-- Date: 19/05/2005
-- Version: 1.2
--
-- Aggiunta l'estrazione dei nuovi attributi definiti sulla TIPO_CONTRIBUTO_RITENUTA:
--     fl_gla,
--     fl_aliquota_ridotta
--
-- Date: 16/01/2006
-- Version: 1.3
--
-- Aggiunto il nuovo attributo definiti sulla TIPO_CONTRIBUTO_RITENUTA:
--     cd_ente_prev_sti
--
--
-- Date: 25/05/2014
-- Version: 1.4
--
-- Adeguamenti relativi al Bonus DL 66/2014
-- Aggiunto il nuovo attributo definito sulla TIPO_CONTRIBUTO_RITENUTA:
--     fl_credito_irpef
--
--
-- Body:
--
--==============================================================================
       A.cd_trattamento,
       A.dt_ini_validita,
       A.dt_fin_validita,
       A.ti_anagrafico,
       A.fl_detrazioni_familiari,
       A.fl_detrazioni_dipendente,
       A.fl_tassazione_separata,
       A.fl_irpef_annualizzata,
       B.cd_contributo_ritenuta,
       B.dt_inizio_validita,
       B.dt_fine_validita,
       B.id_riga,
       B.segno,
       B.calcolo_imponibile,
       C.dt_ini_validita,
       C.dt_fin_validita,
       C.ti_cassa_competenza,
       C.precisione,
       C.pg_classificazione_montanti,
       C.cd_classificazione_cori,
       C.fl_scrivi_montanti,
       C.fl_uso_in_lordizza,
       C.fl_gla,
       C.fl_aliquota_ridotta,
       C.cd_ente_prev_sti,
       C.fl_sospensione_irpef,
       C.fl_credito_irpef
FROM   TIPO_TRATTAMENTO A,
       TRATTAMENTO_CORI B,
       TIPO_CONTRIBUTO_RITENUTA C
WHERE  B.cd_trattamento = A.cd_trattamento AND
       C.cd_contributo_ritenuta = B.cd_contributo_ritenuta;
