--------------------------------------------------------
--  DDL for View V_STIPENDI_MONTANTI_PRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STIPENDI_MONTANTI_PRE" ("ESERCIZIO", "CD_ANAG", "MATRICOLA", "PG_CLASSIFICAZIONE_MONTANTI", "IMPONIBILE_LORDO", "IMPONIBILE_NETTO") AS 
  SELECT
--
-- Date: 03/12/2002
-- Version: 1.0
--
-- Per tutte le matricole, mette a disposizione le informazioni relative ai montanti previdenziali e
-- fiscali derivanti dai compensi liquidati nel corso dei vari esercizi
--
-- History:
--
-- Date: 03/12/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
       DISTINCT C.esercizio,
       T.cd_anag,
       R.matricola_dipendente,
       TCR.pg_classificazione_montanti,
       CR.imponibile_lordo,
       CR.imponibile
FROM   COMPENSO C,
       CONTRIBUTO_RITENUTA CR,
       TIPO_CONTRIBUTO_RITENUTA TCR,
       TERZO T,
       RAPPORTO R
WHERE  C.ti_anagrafico = 'D' AND
       CR.cd_cds = C.cd_cds AND
       CR.cd_unita_organizzativa = C.cd_unita_organizzativa AND
       CR.esercizio = C.esercizio AND
       CR.pg_compenso = C.pg_compenso AND
       TCR.cd_contributo_ritenuta = CR.cd_contributo_ritenuta AND
       TCR.dt_ini_validita = CR.dt_ini_validita AND
       TCR.fl_scrivi_montanti = 'Y' AND
       T.cd_terzo = C.cd_terzo AND
       R.cd_tipo_rapporto = C.cd_tipo_rapporto AND
       R.cd_anag = T.cd_anag AND
       R.dt_ini_validita =
          (
           SELECT MAX(R1.dt_ini_validita)
           FROM   RAPPORTO R1
           WHERE  R1.cd_tipo_rapporto = R.cd_tipo_rapporto AND
                  R1.cd_anag = R.cd_anag
           )
;
