--------------------------------------------------------
--  DDL for View V_REVERSALE_TERZO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_REVERSALE_TERZO" (
  "CD_CDS",
  "ESERCIZIO",
  "PG_REVERSALE",
  "CD_UNITA_ORGANIZZATIVA",
  "CD_CDS_ORIGINE",
  "CD_UO_ORIGINE",
  "CD_TIPO_DOCUMENTO_CONT",
  "TI_REVERSALE",
  "TI_COMPETENZA_RESIDUO",
  "DS_REVERSALE",
  "STATO",
  "DT_EMISSIONE",
  "DT_TRASMISSIONE",
  "DT_INCASSO",
  "DT_ANNULLAMENTO",
  "IM_REVERSALE",
  "IM_INCASSATO",
  "UTCR",
  "DACR",
  "UTUV",
  "DUVA",
  "PG_VER_REC",
  "STATO_TRASMISSIONE",
  "STATO_COGE",
  "DT_RITRASMISSIONE",
  "CD_TERZO",
  "CD_ANAG",
  "DENOMINAZIONE_SEDE",
  "FL_ASSOCIAZIONE_SIOPE_COMPLETA",
  "DT_FIRMA",
  "STATO_TRASMISSIONE_ANNULLO",
  "FL_RIEMISSIONE",
  "DT_FIRMA_ANNULLO",
  "PG_REVERSALE_RIEMISSIONE",
  "ESITO_OPERAZIONE",
  "DT_ORA_ESITO_OPERAZIONE",
  "ERRORE_SIOPE_PLUS"
  ) AS
  SELECT
--
--
-- Date: 21/06/2007
-- Version: 1.1
--
-- View di estrazione delle testate di reversali e dei dati relativi al terzo beneficiario
--
-- History:
--
-- Date: 10/12/2002
-- Version: 1.0
-- Creazione vista
--
-- Date: 21/06/2007
-- Version: 1.1
-- Aggiunto campo FL_ASSOCIAZIONE_SIOPE_COMPLETA utile per sapere se i dettagli della reversale sono stati
-- o  meno completamente associati ai codici SIOPE
--
-- Body:
--
A.CD_CDS,
A.ESERCIZIO,
A.PG_REVERSALE ,
A.CD_UNITA_ORGANIZZATIVA,
A.CD_CDS_ORIGINE,
A.CD_UO_ORIGINE,
A.CD_TIPO_DOCUMENTO_CONT,
A.TI_REVERSALE,
A.TI_COMPETENZA_RESIDUO,
A.DS_REVERSALE,
A.STATO,
A.DT_EMISSIONE,
A.DT_TRASMISSIONE,
A.DT_INCASSO,
A.DT_ANNULLAMENTO,
A.IM_REVERSALE,
A.IM_INCASSATO,
A.UTCR,
A.DACR,
A.UTUV,
A.DUVA,
A.PG_VER_REC,
A.STATO_TRASMISSIONE,
A.STATO_COGE,
A.DT_RITRASMISSIONE,
B.CD_TERZO,
C.CD_ANAG,
C.DENOMINAZIONE_SEDE,
Decode
(
   (SELECT NVL(SUM(D.IMPORTO), 0) FROM REVERSALE_SIOPE D
        WHERE D.CD_CDS = A.CD_CDS
        And   D.ESERCIZIO = A.ESERCIZIO
        And   D.PG_REVERSALE = A.PG_REVERSALE),
   A.IM_REVERSALE,
   'Y',
   'N'
) FL_ASSOCIAZIONE_SIOPE_COMPLETA,
A.DT_FIRMA,
A.STATO_TRASMISSIONE_ANNULLO,
A.FL_RIEMISSIONE,
a.dt_firma_annullo,
a.pg_reversale_riemissione,
a.esito_operazione,
a.dt_ora_esito_operazione,
a.errore_siope_plus
FROM   REVERSALE A,
REVERSALE_TERZO B,
TERZO C
WHERE A.ESERCIZIO = B.ESERCIZIO
AND A.CD_CDS = B.CD_CDS
AND A.PG_REVERSALE = B.PG_REVERSALE
AND B.CD_TERZO = C.CD_TERZO;

   COMMENT ON TABLE "V_REVERSALE_TERZO"  IS 'View di estrazione delle testate di reversali e dei dati relativi al terzo beneficiario';
