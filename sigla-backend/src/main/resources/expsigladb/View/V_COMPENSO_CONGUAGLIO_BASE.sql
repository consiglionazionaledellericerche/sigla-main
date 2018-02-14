--------------------------------------------------------
--  DDL for View V_COMPENSO_CONGUAGLIO_BASE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_COMPENSO_CONGUAGLIO_BASE" ("CD_ANAG", "CD_TERZO", "CD_CDS_COMPENSO", "CD_UO_COMPENSO", "ESERCIZIO_COMPENSO", "PG_COMPENSO", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "STATO_COFI", "DT_EMISSIONE_MANDATO", "FL_RECUPERO_RATE", "IS_COMPENSO_CONGUAGLIO", "IS_ASSOCIATO_CONGUAGLIO", "CD_TRATTAMENTO", "CD_TIPO_RAPPORTO", "FL_DETRAZIONI_FAMILIARI", "FL_DETRAZIONI_LAVORO", "FL_DETRAZIONI_ALTRE", "CD_CDS_CONGUAGLIO", "CD_UO_CONGUAGLIO", "ESERCIZIO_CONGUAGLIO", "PG_CONGUAGLIO", "DACR_CONGUAGLIO") AS 
  SELECT
--==================================================================================================
--
-- Date: 09/07/2003
-- Version: 1.6
--
-- Vista di estrazione dei dati di base dei compensi collegabili ad un conguaglio. Sono evidenziati
-- anche quelli già inclusi in precededenti conguagli e quelli che sono stati originati dal
-- conguaglio stesso
--
-- History:
--
-- Date: 16/07/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 23/08/2002
-- Version: 1.1
--
-- Recuperate anche le date da / a di competenza economica del compenso
--
-- Date: 28/08/2002
-- Version: 1.2
--
-- Inserito il filtro per non includere i compensi annullati
--
-- Date: 09/09/2002
-- Version: 1.3
--
-- Inserita estrazione dello stato COFI per controllo di inclusione nel conguaglio dei soli
-- compensi associati ad un mandato
--
-- Date: 10/01/2003
-- Version: 1.4
--
-- Inserito filtro per non estrarre i compensi da minicarriera soggetti a tassazione separata
--
-- Date: 16/04/2003
-- Version: 1.5
--
-- Inserito filtro per non estrarre i conguagli annullati
--
-- Date: 09/07/2003
-- Version: 1.6
--
-- Inserita estrazione della data di emissione mandato per filtro conguaglio sui compensi che
-- hanno data emissione mandato nell'esercizio di calcolo.
-- Inserita estrazione del flag fl_recupero_rate per gestire la sottrazione dei giorni di competenza
-- nel calcolo del conguaglio
--
-- Date: 24/01/2006
-- Version: 1.7
--
-- Aggiunto anche il Tipo Rapporto per poterlo poi usare nelle view V_TERZI_DA_CONGUAGLIARE_DET e
-- V_TERZI_DA_CONGUAGLIARE e quindi aggiungerlo nella Consultazione "Terzi da Conguagliare"
--
-- Date: 15/05/2007
-- Versione: 1.8
--
-- Aggiunto FL_DETRAZIONI_ALTRE per la gestione degli altri tipi di detrazioni (assegni alimentari)
--
-- Body:
--
--==================================================================================================
       A.cd_anag,
       B.cd_terzo,
       B.cd_cds,
       B.cd_unita_organizzativa,
       B.esercizio,
       B.pg_compenso,
       B.dt_da_competenza_coge,
       B.dt_a_competenza_coge,
       B.stato_cofi,
       B.dt_emissione_mandato,
       B.fl_recupero_rate,
       (SELECT DECODE(COUNT(*),0,'N','Y')
        FROM DUAL
        WHERE EXISTS
              (SELECT 1
               FROM   CONGUAGLIO G
               WHERE  G.cd_cds_compenso = B.cd_cds AND
                      G.cd_uo_compenso = B.cd_unita_organizzativa AND
                      G.esercizio = B.esercizio AND
                      G.pg_compenso = B.pg_compenso)),
       'Y',
       C.cd_trattamento,
       B.cd_tipo_rapporto,
       C.fl_detrazioni_familiari,
       C.fl_detrazioni_dipendente,
       C.fl_detrazioni_altre,
       D.cd_cds_conguaglio,
       D.cd_uo_conguaglio,
       D.esercizio_conguaglio,
       D.pg_conguaglio,
       E.dacr
FROM   TERZO A,
       COMPENSO B,
       TIPO_TRATTAMENTO C,
       ASS_COMPENSO_CONGUAGLIO D,
       CONGUAGLIO E
WHERE  B.cd_terzo = A.cd_terzo AND
       B.fl_compenso_mcarriera_tassep = 'N' AND
       C.cd_trattamento = B.cd_trattamento AND
       C.fl_soggetto_conguaglio = 'Y' AND
       C.dt_ini_validita <= B.dt_registrazione AND
       C.dt_fin_validita >= B.dt_registrazione AND
       D.cd_cds_compenso = B.cd_cds AND
       D.cd_uo_compenso = B.cd_unita_organizzativa AND
       D.esercizio_compenso = B.esercizio AND
       D.pg_compenso = B.pg_compenso AND
       E.cd_cds = D.cd_cds_conguaglio AND
       E.cd_unita_organizzativa = D.cd_uo_conguaglio AND
       E.esercizio = D.esercizio_conguaglio AND
       E.pg_conguaglio = D.pg_conguaglio AND
       E.dt_cancellazione IS NULL
UNION ALL
SELECT A.cd_anag,
       B.cd_terzo,
       B.cd_cds,
       B.cd_unita_organizzativa,
       B.esercizio,
       B.pg_compenso,
       B.dt_da_competenza_coge,
       B.dt_a_competenza_coge,
       B.stato_cofi,
       B.dt_emissione_mandato,
       B.fl_recupero_rate,
       'N',
       'N',
       C.cd_trattamento,
       B.cd_tipo_rapporto,
       C.fl_detrazioni_familiari,
       C.fl_detrazioni_dipendente,
       C.fl_detrazioni_altre,
       NULL,
       NULL,
       TO_NUMBER(NULL),
       TO_NUMBER(NULL),
       TO_DATE(NULL)
FROM   TERZO A,
       COMPENSO B,
       TIPO_TRATTAMENTO C
WHERE  B.cd_terzo = A.cd_terzo AND
       B.fl_compenso_mcarriera_tassep = 'N' AND
       C.cd_trattamento = B.cd_trattamento AND
       C.fl_soggetto_conguaglio = 'Y' AND
       C.dt_ini_validita <= B.dt_registrazione AND
       C.dt_fin_validita >= B.dt_registrazione AND
       NOT EXISTS
          (SELECT 1
           FROM   ASS_COMPENSO_CONGUAGLIO D
           WHERE  D.cd_cds_compenso = B.cd_cds AND
                  D.cd_uo_compenso = B.cd_unita_organizzativa AND
                  D.esercizio_compenso = B.esercizio AND
                  D.pg_compenso = B.pg_compenso);
