--------------------------------------------------------
--  DDL for View V_ESTRAI_DATI_INPS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ESTRAI_DATI_INPS" ("CD_CDS_COMPENSO", "CD_UO_COMPENSO", "ESERCIZIO_COMPENSO", "PG_COMPENSO", "CHIAVE_COMPENSO", "FL_SENZA_CALCOLI", "STATO_COFI", "DT_REGISTRAZIONE_COMPENSO", "DT_CMP_DA_COMPENSO", "DT_CMP_A_COMPENSO", "DT_EMISSIONE_MANDATO", "DT_TRASMISSIONE_MANDATO", "DT_PAGAMENTO_MANDATO", "ESERCIZIO_PAGAMENTO", "TI_ANAGRAFICO", "CD_TERZO", "CD_ANAG", "CD_TRATTAMENTO", "CD_TIPO_RAPPORTO", "CD_CONTRIBUTO_RITENUTA", "DS_CONTRIBUTO_RITENUTA", "TI_ENTE_PERCIPIENTE", "IMPONIBILE", "ALIQUOTA", "BASE_CALCOLO", "AMMONTARE", "PG_RIGA", "IMPONIBILE_DET", "ALIQUOTA_DET", "BASE_CALCOLO_DET", "AMMONTARE_DET", "CD_RAPPORTO_INPS", "CD_ATTIVITA_INPS", "CD_ALTRA_ASS_INPS", "PG_COMUNE_INPS") AS 
  SELECT  /*+ optimizer_features_enable('10.1.0') */
--==================================================================================================
--
-- Date: 12/03/2004
-- Version: 1.0
--
-- Estrazione singoli compensi.
--
-- History:
--
-- Date: 02/02/2004
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 18/02/2005
-- Version: 1.1
--
-- Aggiunte anche le prestazioni occasionali
--
-- Date: 03/05/2005
-- Version: 1.2
--
-- Aggiunto il tipo rapporto
--
-- Date: 19/05/2005
-- Version: 1.3
--
-- Aggiunti Codice rapporto INPS, Codice attivita' INPS, Codice altra assicurazione INPS
-- Sostituita la condizione "B.cd_contributo_ritenuta IN ('INPSA','INPSB','INPSC','INPS10D','INPS15E','INPS17F')"
-- con "F.FL_GLA = 'Y'".
--
-- Date: 22/05/2007
-- Version: 1.4
--
-- Aggiunto il Comune presso il quale il lavoratore ha svolto la propria attività
--
-- Body:
--
--==================================================================================================
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       A.pg_compenso,
       A.cd_cds || A.cd_unita_organizzativa || A.esercizio || A.pg_compenso,
       A.fl_senza_calcoli,
       A.stato_cofi,
       A.dt_registrazione,
       A.dt_da_competenza_coge,
       A.dt_a_competenza_coge,
       A.dt_emissione_mandato,
       E.dt_trasmissione,
       E.dt_pagamento,
       DECODE(A.dt_emissione_mandato, NULL, 0, TO_NUMBER(TO_CHAR(A.dt_emissione_mandato,'YYYY'))),
       A.ti_anagrafico,
       A.cd_terzo,
       D.cd_anag,
       A.cd_trattamento,
       A.cd_tipo_rapporto,
       B.cd_contributo_ritenuta,
       F.ds_contributo_ritenuta,
       B.ti_ente_percipiente,
       B.imponibile,
       B.aliquota,
       B.base_calcolo,
       B.ammontare,
       NVL(C.pg_riga,0),
       NVL(C.imponibile,0),
       NVL(C.aliquota,0),
       NVL(C.base_calcolo,0),
       NVL(C.ammontare,0),
       A.cd_rapporto_inps,
       A.cd_attivita_inps,
       A.cd_altra_ass_inps,
       A.pg_comune_inps
FROM   COMPENSO A,
       CONTRIBUTO_RITENUTA B,
       CONTRIBUTO_RITENUTA_DET C,
       TERZO D,
       V_DT_MANREV_COMP E,
       TIPO_CONTRIBUTO_RITENUTA F
WHERE  A.ti_anagrafico = 'A' AND
       A.stato_cofi = 'P' AND
       B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_compenso = A.pg_compenso AND
       C.cd_cds (+) = B.cd_cds AND
       C.cd_unita_organizzativa (+) = B.cd_unita_organizzativa AND
       C.esercizio (+) = B.esercizio AND
       C.pg_compenso (+) = B.pg_compenso AND
       C.cd_contributo_ritenuta (+) = B.cd_contributo_ritenuta AND
       C.ti_ente_percipiente (+) = B.ti_ente_percipiente AND
       D.cd_terzo = A.cd_terzo AND
       E.cd_cds_compenso (+) = A.cd_cds AND
       E.cd_uo_compenso (+) = A.cd_unita_organizzativa AND
       E.esercizio_compenso (+) = A.esercizio AND
       E.pg_compenso (+) = A.pg_compenso AND
       F.cd_contributo_ritenuta = B.cd_contributo_ritenuta AND
       F.dt_ini_validita = B.dt_ini_validita And
       F.FL_GLA = 'Y';
