--------------------------------------------------------
--  DDL for View V_MANDATO_UPG_STATO_DOCAMM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_UPG_STATO_DOCAMM" ("CD_CDS_MANDATO", "ESERCIZIO_MANDATO", "PG_MANDATO", "CD_UO_MANDATO", "CD_CDS_ORI_MANDATO", "CD_UO_ORI_MANDATO", "CD_TIPO_MANDATO", "TI_MANDATO", "STATO_MANDATO", "DT_EMISSIONE_MANDATO", "CD_TERZO_MANDATO", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "CD_CDS_DOCAMM", "CD_UO_DOCAMM", "ESERCIZIO_DOCAMM", "CD_TIPO_DOCAMM", "PG_DOCAMM") AS 
  SELECT
-- =================================================================================================
--
-- Date: 18/07/2006
-- Version: 1.3
--
-- Vista di estrazione di MANDATO join MANDATO_RIGA per aggiornamento dello stato_cofi e cancellazione
-- logica sui documenti amministrativi associati allo stesso
--
-- History:
--
-- Date: 18/03/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 05/02/2003
-- Version: 1.1
--
-- Estrazione della data di emissione del mandato per poter esporre la data di esigibilità iva
--
-- Date: 19/02/2003
-- Version: 1.2
--
-- Estrazione del codice terzo della riga mandato per gestire l'aggiornamento dello stato sui
-- documenti amministrativi. Se più righe di generico insistono sulla stessa scadenza di obbligazione
-- (partite di giro a consumo su CNR) solo quelle che hanno terzo uguale a quello del mandato devono
-- essere aggiornate in modo concorde con inserimento o annullamento di mandati e reversali.
--
-- Date: 18/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
-- =================================================================================================
       A.cd_cds,
       A.esercizio,
       A.pg_mandato,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.cd_tipo_documento_cont,
       A.ti_mandato,
       A.stato,
       A.dt_emissione,
       B.cd_terzo,
       B.esercizio_obbligazione,
       B.esercizio_ori_obbligazione,
       B.pg_obbligazione,
       B.pg_obbligazione_scadenzario,
       B.cd_cds_doc_amm,
       B.cd_uo_doc_amm,
       B.esercizio_doc_amm,
       B.cd_tipo_documento_amm,
       B.pg_doc_amm
FROM   MANDATO A,
       MANDATO_RIGA B
WHERE  B.cd_cds = A.cd_cds AND
       B.esercizio = A.esercizio AND
       B.pg_mandato = A.pg_mandato;

   COMMENT ON TABLE "V_MANDATO_UPG_STATO_DOCAMM"  IS 'Vista di estrazione di MANDATO join MANDATO_RIGA per aggiornamento dello
stato_cofi e cancellazione logica sui documenti amministrativi associati
allo stesso';
