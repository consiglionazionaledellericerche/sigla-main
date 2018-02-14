--------------------------------------------------------
--  DDL for View V_PAGATO_PER_TIPO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PAGATO_PER_TIPO" ("ESERCIZIO", "VOCE", "DESCRIZIONE", "TIPO", "TI_MANDATO", "DS_TIPO", "TOT_PAGATO") AS 
  Select
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Vista di estrazione del Pagato per TIPO_SPESA
--
-- History:
--
-- Date: 08/11/2004
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
ESERCIZIO, VOCE, DESCRIZIONE, TIPO, TI_MANDATO, ds_tipo, Nvl(Sum(PAGATO),0) TOT_PAGATO
From
(SELECT  R.ESERCIZIO, O.CD_ELEMENTO_VOCE VOCE, E.DS_ELEMENTO_VOCE DESCRIZIONE,
         T.DS_TIPO_SPESA TIPO,
        (Select ti_mandato FROM MANDATO WHERE cd_cds = r.cd_cds AND ESERCIZIO = r.ESERCIZIO AND pg_mandato = r.pg_mandato) ti_mandato,
        Decode ((Select ti_mandato FROM MANDATO WHERE cd_cds = r.cd_cds AND ESERCIZIO = r.ESERCIZIO AND pg_mandato = r.pg_mandato), 'P', 'Pagamento', 'A', 'Accreditamento', 'R', 'Regolarizzazione', 'S', 'Sospeso') ds_tipo,
         --Nvl(Sum(R.IM_MANDATO_RIGA),0) PAGATO
         R.IM_MANDATO_RIGA PAGATO
FROM    OBBLIGAZIONE O, ELEMENTO_VOCE E, MANDATO_RIGA R, TIPO_SPESA T, UNITA_ORGANIZZATIVA U
WHERE   O.CD_CDS                = R.CD_CDS                      AND
        O.ESERCIZIO             = R.ESERCIZIO_OBBLIGAZIONE      AND
        O.ESERCIZIO_ORIGINALE   = R.ESERCIZIO_ORI_OBBLIGAZIONE  And
        O.PG_OBBLIGAZIONE       = R.PG_OBBLIGAZIONE             And
	O.CD_UNITA_ORGANIZZATIVA = U.CD_UNITA_ORGANIZZATIVA	AND
        U.CD_TIPO_UNITA		= 'SAC'				AND
        R.STATO 		!= 'A'				AND
        E.CD_TIPO_SPESA_SAC	= T.CD_TIPO_SPESA (+)		AND
        O.ESERCIZIO		= E.ESERCIZIO			AND
        O.TI_APPARTENENZA	= E.TI_APPARTENENZA		AND
        O.TI_GESTIONE		= E.TI_GESTIONE			AND
        O.CD_ELEMENTO_VOCE 	= E.CD_ELEMENTO_VOCE		AND
        E.TI_GESTIONE 		= 'S'				AND
        E.TI_APPARTENENZA	= 'D'				AND
        E.TI_ELEMENTO_VOCE	= 'C'				AND
        E.CD_PARTE		= '1'
--Group By R.ESERCIZIO, O.CD_ELEMENTO_VOCE, E.DS_ELEMENTO_VOCE, T.DS_TIPO_SPESA
Union All
SELECT  R.ESERCIZIO, O.CD_ELEMENTO_VOCE VOCE, E.DS_ELEMENTO_VOCE DESCRIZIONE,
        T.DS_TIPO_SPESA TIPO,
       (Select ti_mandato FROM MANDATO WHERE cd_cds = r.cd_cds AND ESERCIZIO = r.ESERCIZIO AND pg_mandato = r.pg_mandato) ti_mandato,
       Decode ((Select ti_mandato FROM MANDATO WHERE cd_cds = r.cd_cds AND ESERCIZIO = r.ESERCIZIO AND pg_mandato = r.pg_mandato), 'P', 'Pagamento', 'A', 'Accreditamento', 'R', 'Regolarizzazione', 'S', 'Sospeso'),
        --Nvl(Sum(R.IM_MANDATO_RIGA),0) PAGATO
        R.IM_MANDATO_RIGA
FROM    OBBLIGAZIONE O, ELEMENTO_VOCE E, MANDATO_RIGA R, TIPO_SPESA T, UNITA_ORGANIZZATIVA U
WHERE   O.CD_CDS                = R.CD_CDS                      AND
        O.ESERCIZIO             = R.ESERCIZIO_OBBLIGAZIONE      AND
        O.ESERCIZIO_ORIGINALE   = R.ESERCIZIO_ORI_OBBLIGAZIONE  And
        O.PG_OBBLIGAZIONE       = R.PG_OBBLIGAZIONE             And
	O.CD_UNITA_ORGANIZZATIVA = U.CD_UNITA_ORGANIZZATIVA	AND
        U.CD_TIPO_UNITA		!= 'SAC'			AND
        R.STATO 		!= 'A'				AND
        E.CD_TIPO_SPESA_IST	= T.CD_TIPO_SPESA (+)		AND
        O.ESERCIZIO		= E.ESERCIZIO			AND
        O.TI_APPARTENENZA	= E.TI_APPARTENENZA		AND
        O.TI_GESTIONE		= E.TI_GESTIONE			AND
        O.CD_ELEMENTO_VOCE 	= E.CD_ELEMENTO_VOCE		AND
        E.TI_GESTIONE 		= 'S'				AND
        E.TI_APPARTENENZA	= 'D'				AND
        E.TI_ELEMENTO_VOCE	= 'C'				AND
        E.CD_PARTE		= '1')
--Group By R.ESERCIZIO, O.CD_ELEMENTO_VOCE, E.DS_ELEMENTO_VOCE, T.DS_TIPO_SPESA)
Group By ESERCIZIO, VOCE, DESCRIZIONE, TIPO, TI_MANDATO, ds_tipo;
