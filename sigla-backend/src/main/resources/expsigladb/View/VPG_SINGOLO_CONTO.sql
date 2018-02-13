--------------------------------------------------------
--  DDL for View VPG_SINGOLO_CONTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VPG_SINGOLO_CONTO" ("ID", "CHIAVE", "TIPO", "SEQUENZA", "ESERCIZIO", "CD_CDS", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_VOCE", "TI_COMPETENZA_RESIDUO", "DS_CDS", "DS_VOCE", "IM_STANZ_INIZIALE_A1", "VARIAZIONI_PIU", "VARIAZIONI_MENO", "IM_STANZ_ATTUALE", "IM_DISP_CASSA", "IM_OBBLIG_IMP_ACR", "IM_OBBL_RES_IMP", "VAR_PIU_OBBL_RES_PRO", "VAR_MENO_OBBL_RES_PRO", "IM_MANDATI_REVERSALI", "IM_MANDATI_REVERSALI_IMP", "IM_PAGAMENTI_INCASSI", "IM_1210_NO_SOSPESI", "PG_VARIAZIONE", "DT_DELIBERA", "DS_VARIAZIONE", "IM_VARIAZIONE", "DT_REGISTRAZIONE", "ESERCIZIO_ORI_OBB_ACR", "PG_OBB_ACR", "FL_ANNOTAZIONE", "CD_TERZO", "DENOMINAZIONE_SEDE", "DS_OBB_ACR", "IM_VOCE", "IM_VOCE_NO_DOCCONT", "DT_EMISSIONE", "PG_MAN_REV", "FL_PAGATO", "DS_MAN_REV", "DS_MAN_REV_RIGA", "PG_OBB_ACR_SCADENZARIO") AS 
  SELECT
--
-- Date: 19/07/2006
-- Version: 1.4
--
-- Vista per la stampa della situazione del singolo conto
-- Protocollo VPG
--
-- History:
--
-- Date: 26/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 21/01/2004
-- Version: 1.1
-- Estrazione del cd_terzo
--
-- Date: 28/01/2004
-- Version: 1.2
-- Fix estrazione attributi long
--
-- Date: 02/03/2004
-- Version: 1.3
-- Aggiunta colonna di importo voce non associato a doc cont
-- (richiesta n. 778)
--
-- Date: 19/07/2006
-- Version: 1.4
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
          ID, chiave, tipo, sequenza, importo_1,                 -- esercizio
          attributo_4,        -- cd_cds
          attributo_1,        -- ti_appartenenza
          attributo_2,        -- ti_gestione
          attributo_5,        -- cd_elemento_voce
          attributo_6,        -- ds_elemento_voce
          attributo_3,        -- cd_voce
          attributo_8,        -- ti_competenza_residuo
-- TIPO A (info generali sul capitolo)
          attributo_long_1,     -- ds_cds,
          attributo_long_2,     -- ds_voce,
          importo_2,  -- im_stanz_iniziale_a1,
          importo_3,  -- variazioni_piu,
          importo_4,  -- variazioni_meno,
          importo_5,  -- im_stanz_attuale,
          importo_6,  -- im_disp_cassa,
          importo_7,  -- im_obblig_imp_acr,
          importo_14, -- IM_OBBL_RES_IMP
          importo_11, -- VAR_PIU_OBBL_RES_PRO
          importo_12, -- VAR_MENO_OBBL_RES_PRO
          importo_8,  -- im_mandati_reversali,
          importo_13, -- IM_MANDATI_REVERSALI_IMP
          importo_9,  -- im_pagamenti_incassi,
          importo_10, -- im_1210_no_sospesi,
-- TIPO B (variazioni di bilancio)
          importo_15, -- pg_variazione,
          data_1,
                                                               -- dt_delibera,
          attributo_long_10,                                  -- ds_variazione,
                           importo_16,                       -- im_variazione,
-- TIPO C (obbligazioni/accertamenti)
                                      data_2,             -- dt_registrazione,
                                             importo_23,
                                                     -- esercizio_ori_obb_acr,
                                                        importo_17,
                                                                -- pg_obb_acr,
          attributo_9,                                      -- fl_annotazione,
                      importo_21,                                  -- cd_terzo
                                 attributo_10,          -- denominazione_sede,
                                              attributo_long_4, -- ds_obb_acr,
                                                               importo_18,
                                                                   -- im_voce,
          importo_22,                                    -- im_voce_no_doccont
-- TIPO D (mandati/reversali - includendo alcuni campi del tipo c)
                     data_2,                                  -- dt_emissione,
                            importo_19,                         -- pg_man_rev,
                                       attributo_12,             -- fl_pagato,
                                                    attributo_long_5,
                                                                -- ds_man_rev,
          attributo_long_6,                                -- DS_MAN_REV_RIGA,
                           importo_20                -- PG_OBB_ACR_SCADENZARIO
     FROM tmp_report_generico;

   COMMENT ON TABLE "VPG_SINGOLO_CONTO"  IS 'Vista per la stampa della situazione del singolo conto
Protocollo VPG';
