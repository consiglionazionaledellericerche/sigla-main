--------------------------------------------------------
--  DDL for View V_INVENT_BENI_APG_TRASF
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INVENT_BENI_APG_TRASF" ("PG_INVENTARIO", "NR_INVENTARIO", "PROGRESSIVO", "DS_BENE", "CD_CATEGORIA_GRUPPO", "TI_AMMORTAMENTO", "FL_AMMORTAMENTO", "CD_CONDIZIONE_BENE", "TI_COMMERCIALE_ISTITUZIONALE", "VALORE_INIZIALE", "VALORE_AMMORTIZZATO", "VARIAZIONE_PIU", "VARIAZIONE_MENO", "IMPONIBILE_AMMORTAMENTO", "VALORE_ALIENAZIONE", "FL_TOTALMENTE_SCARICATO", "COLLOCAZIONE", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_UBICAZIONE", "CD_ASSEGNATARIO", "DT_VALIDITA_VARIAZIONE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "ETICHETTA", "ESERCIZIO_CARICO_BENE", "ID_BENE_ORIGINE", "FL_MIGRATO", "LOCAL_TRANSACTION_ID", "FL_TRASF_COME_PRINCIPALE", "PG_INVENTARIO_PRINCIPALE", "NR_INVENTARIO_PRINCIPALE", "PROGRESSIVO_PRINCIPALE") AS 
  SELECT
-- =================================================================================================
--
-- Date: 20/09/2004
-- Version: 1.0
--
-- Vista per gestione trasferimento beni
--
-- History:
--
-- Date: 20/04/2004
-- Version: 1.0
--
-- Creazione vista
--
-- Body:
--
-- =================================================================================================
       A.pg_inventario,
       A.nr_inventario,
       A.progressivo,
       B.ds_bene,
       B.cd_categoria_gruppo,
       B.ti_ammortamento,
       B.fl_ammortamento,
       B.cd_condizione_bene,
       B.ti_commerciale_istituzionale,
       B.valore_iniziale,
       B.valore_ammortizzato,
       B.variazione_piu,
       B.variazione_meno,
       B.imponibile_ammortamento,
       B.valore_alienazione,
       B.fl_totalmente_scaricato,
       B.collocazione,
       B.cd_cds,
       B.cd_unita_organizzativa,
       B.cd_ubicazione,
       B.cd_assegnatario,
       B.dt_validita_variazione,
       B.dacr,
       B.utcr,
       B.duva,
       B.utuv,
       B.pg_ver_rec,
       B.etichetta,
       B.esercizio_carico_bene,
       B.id_bene_origine,
       B.fl_migrato,
       A.local_transaction_id,
       A.fl_trasf_come_principale,
       A.pg_inventario_principale,
       A.nr_inventario_principale,
       A.progressivo_principale
FROM   inventario_beni_apg A,
       inventario_beni B
WHERE  B.pg_inventario = A.pg_inventario AND
       B.nr_inventario = A.nr_inventario AND
       B.progressivo = A.progressivo
;

   COMMENT ON TABLE "V_INVENT_BENI_APG_TRASF"  IS 'Vista per la gestione on line del trasferimento beni';
