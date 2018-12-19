--------------------------------------------------------
--  DDL for View V_MANDATO_TERZO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_TERZO" (
    "CD_CDS",
    "ESERCIZIO",
    "PG_MANDATO",
    "CD_UNITA_ORGANIZZATIVA",
    "CD_CDS_ORIGINE",
    "CD_UO_ORIGINE",
    "CD_TIPO_DOCUMENTO_CONT",
    "TI_MANDATO",
    "TI_COMPETENZA_RESIDUO",
    "DS_MANDATO",
    "STATO",
    "DT_EMISSIONE",
    "DT_TRASMISSIONE",
    "DT_PAGAMENTO",
    "DT_ANNULLAMENTO",
    "IM_MANDATO",
    "IM_PAGATO",
    "UTCR",
    "DACR",
    "UTUV",
    "DUVA",
    "PG_VER_REC",
    "STATO_TRASMISSIONE",
    "STATO_COGE",
    "IM_RITENUTE",
    "DT_RITRASMISSIONE",
    "CD_TERZO",
    "CD_ANAG",
    "DENOMINAZIONE_SEDE",
    "DT_FIRMA",
    "STATO_TRASMISSIONE_ANNULLO",
    "FL_RIEMISSIONE",
    "DT_FIRMA_ANNULLO",
    "PG_MANDATO_RIEMISSIONE",
    "DT_PAGAMENTO_RICHIESTA",
    "ESITO_OPERAZIONE",
    "DT_ORA_ESITO_OPERAZIONE",
    "ERRORE_SIOPE_PLUS"
    ) AS
  SELECT
--
--
-- Date: 10/12/2002
-- Version: 1.0
--
-- View di estrazione delle testate di mandati e dei dati relativi al terzo beneficiario
--
-- History:
--
-- Date: 10/12/2002
-- Version: 1.0
-- Creazione vista
--
-- Date: 10/12/2015
-- Version: 1.1
-- Aggiunta la data di firma
--
-- Body:
--
A.CD_CDS,
A.ESERCIZIO,
A.PG_MANDATO ,
A.CD_UNITA_ORGANIZZATIVA,
A.CD_CDS_ORIGINE,
A.CD_UO_ORIGINE,
A.CD_TIPO_DOCUMENTO_CONT,
A.TI_MANDATO,
A.TI_COMPETENZA_RESIDUO,
A.DS_MANDATO,
A.STATO,
A.DT_EMISSIONE,
A.DT_TRASMISSIONE,
A.DT_PAGAMENTO,
A.DT_ANNULLAMENTO,
A.IM_MANDATO,
A.IM_PAGATO,
A.UTCR,
A.DACR,
A.UTUV,
A.DUVA,
A.PG_VER_REC,
A.STATO_TRASMISSIONE,
A.STATO_COGE,
A.IM_RITENUTE,
A.DT_RITRASMISSIONE,
B.CD_TERZO,
C.CD_ANAG,
C.DENOMINAZIONE_SEDE,
A.DT_FIRMA,
A.STATO_TRASMISSIONE_ANNULLO,
A.FL_RIEMISSIONE,
a.dt_firma_annullo,
a.pg_mandato_riemissione,
a.dt_pagamento_richiesta,
a.esito_operazione,
a.dt_ora_esito_operazione,
a.errore_siope_plus
FROM   MANDATO A,
	   MANDATO_TERZO B,
	   TERZO C
WHERE
A.ESERCIZIO = B.ESERCIZIO
AND
A.CD_CDS = B.CD_CDS
AND
A.PG_MANDATO = B.PG_MANDATO
AND
B.CD_TERZO = C.CD_TERZO;

   COMMENT ON TABLE "V_MANDATO_TERZO"  IS 'View di estrazione delle testate di mandati e dei dati relativi al terzo beneficiario';
