--------------------------------------------------------
--  DDL for View V_DOC_AMM_RIGA_KEY
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_RIGA_KEY" ("CD_TIPO_DOCUMENTO_AMM", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_DOCUMENTO", "PROGRESSIVO_RIGA") AS 
  (
select
--
-- Date: 15/05/2002
-- Version: 1.0
--
-- Vista di estrazione della chiave dei dettagli di doc amministrativi
--
-- History:
--
-- Date: 15/05/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
     'FATTURA_P'
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,PG_FATTURA_PASSIVA
    ,PROGRESSIVO_RIGA
from FATTURA_PASSIVA_RIGA
union all
select
     'FATTURA_A'
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,PG_FATTURA_ATTIVA
    ,PROGRESSIVO_RIGA
from FATTURA_ATTIVA_RIGA
union all
select -- Estrazione documento generico
     cd_tipo_documento_amm
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,PG_DOCUMENTO_GENERICO
    ,PROGRESSIVO_RIGA
from DOCUMENTO_GENERICO_RIGA
)
;

   COMMENT ON TABLE "V_DOC_AMM_RIGA_KEY"  IS 'Vista di estrazione della chiave dei dettagli di doc amministrativi';
