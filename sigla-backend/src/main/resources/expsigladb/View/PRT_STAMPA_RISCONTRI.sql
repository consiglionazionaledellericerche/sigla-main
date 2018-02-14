--------------------------------------------------------
--  DDL for View PRT_STAMPA_RISCONTRI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_RISCONTRI" ("CDS", "ESERCIZIO", "TIPOSOSP", "TIPOES", "CODSOSP", "IMPORTO", "DATA", "TIPOBANCA", "CAUS", "DESCR", "UO", "PG_MANREV", "DESC_TERZO", "UO_ORIGINE") AS 
  SELECT
-- Date: 18/05/2004
-- Version: 1.3
--
-- Stampa RISCONTRI
--
-- History:
--
-- Date: 10/06/2003
-- Version: 1.0
-- Creazione
--
-- Date:21/10/2003
-- Version: 1.2
-- aggiunto filtro riscontri stornati
--
-- Date:18/05/2004
-- Version: 1.3
-- aggiunto parametro esercizio
--
-- Body
--
  sospeso.CD_CDS,
  sospeso.ESERCIZIO,
  sospeso.TI_SOSPESO_RISCONTRO,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.CD_SOSPESO,
  sospeso.IM_SOSPESO,
  sospeso.DT_REGISTRAZIONE,
  sospeso.TI_CC_BI,
  sospeso.CAUSALE,
  sospeso.DS_ANAGRAFICO,
  v_cons_reversali_riscontrate.cd_unita_organizzativa,
  v_cons_reversali_riscontrate.PG_REVERSALE,
  TERZO.DENOMINAZIONE_SEDE,
  reversale.cd_uo_origine
FROM
  sospeso,v_cons_reversali_riscontrate,REVERSALE_TERZO,TERZO,reversale
WHERE   sospeso.CD_SOSPESO_PADRE is null
AND sospeso.TI_SOSPESO_RISCONTRO = 'R'
AND sospeso.FL_STORNATO = 'N'
and sospeso.TI_ENTRATA_SPESA = 'E' AND
v_cons_reversali_riscontrate.ESERCIZIO= sospeso.esercizio and
v_cons_reversali_riscontrate.CD_CDS= sospeso.CD_CDS and
v_cons_reversali_riscontrate.CD_SOSPESO= sospeso.CD_SOSPESO and
v_cons_reversali_riscontrate.ESERCIZIO= REVERSALE_TERZO.esercizio and
v_cons_reversali_riscontrate.CD_CDS= REVERSALE_TERZO.CD_CDS and
v_cons_reversali_riscontrate.PG_REVERSALE= REVERSALE_TERZO.PG_REVERSALE and
TERZO.CD_TERZO = REVERSALE_TERZO.CD_TERZO  and
v_cons_reversali_riscontrate.ESERCIZIO= REVERSALE.esercizio and
v_cons_reversali_riscontrate.CD_CDS= REVERSALE.CD_CDS and
v_cons_reversali_riscontrate.PG_REVERSALE= REVERSALE.PG_REVERSALE
UNION
SELECT
sospeso.CD_CDS,
  sospeso.ESERCIZIO,
  sospeso.TI_SOSPESO_RISCONTRO,
  sospeso.TI_ENTRATA_SPESA,
  sospeso.CD_SOSPESO,
  sospeso.IM_SOSPESO,
  sospeso.DT_REGISTRAZIONE,
  sospeso.TI_CC_BI,
  sospeso.CAUSALE,
  sospeso.DS_ANAGRAFICO,
  V_CONS_MANDATI_RISCONTRATI.cd_unita_organizzativa,
  V_CONS_MANDATI_RISCONTRATI.PG_MANDATO,
  TERZO.DENOMINAZIONE_SEDE,
  mandato.cd_uo_origine
FROM
  sospeso,V_CONS_MANDATI_RISCONTRATI,MANDATO_TERZO,TERZO,mandato
WHERE   sospeso.CD_SOSPESO_PADRE is null
AND sospeso.TI_SOSPESO_RISCONTRO = 'R'
AND sospeso.FL_STORNATO = 'N'
and sospeso.TI_ENTRATA_SPESA = 'S' AND
V_CONS_MANDATI_RISCONTRATI.ESERCIZIO= sospeso.esercizio and
V_CONS_MANDATI_RISCONTRATI.CD_CDS= sospeso.CD_CDS and
V_CONS_MANDATI_RISCONTRATI.CD_SOSPESO= sospeso.CD_SOSPESO and
V_CONS_MANDATI_RISCONTRATI.ESERCIZIO= MANDATO_TERZO.esercizio and
V_CONS_MANDATI_RISCONTRATI.CD_CDS= MANDATO_TERZO.CD_CDS and
V_CONS_MANDATI_RISCONTRATI.PG_MANDATO= MANDATO_TERZO.PG_MANDATO and
TERZO.CD_TERZO = MANDATO_TERZO.CD_TERZO  and
V_CONS_MANDATI_RISCONTRATI.ESERCIZIO= MANDATO.esercizio and
V_CONS_MANDATI_RISCONTRATI.CD_CDS= MANDATO.CD_CDS and
V_CONS_MANDATI_RISCONTRATI.PG_MANDATO= MANDATO.PG_MANDATO;
