--------------------------------------------------------
--  DDL for View V_DOC_AMM_COGE_NOGEN_TER_FON
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_COGE_NOGEN_TER_FON" ("CD_TIPO_DOCUMENTO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_NUMERO_DOCUMENTO", "CD_TERZO", "STATO_PAGAMENTO_FONDO_ECO") AS 
  (
select /*+ index (a PX_FATTURA_PASSIVA) */
--
-- Date: 23/05/2003
-- Version: 1.0
--
-- Vista di estrazione del terzo e informazioni di legame con fondo economale
-- di documenti amministrativi escluso il documento generico
--
-- History:
--
-- Date: 23/05/2003
-- Version: 1.0
-- Creazione
--
--
-- Body:
--
     'FATTURA_P'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_FATTURA_PASSIVA
    ,a.CD_TERZO
	,a.STATO_PAGAMENTO_FONDO_ECO
from FATTURA_PASSIVA a
 where
   a.pg_fattura_passiva >= 0
union all
  select /*+ index (a PX_FATTURA_ATTIVA) */
     'FATTURA_A'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_FATTURA_ATTIVA
    ,a.CD_TERZO
	,'N' -- fattura attiva non gestita tramite fondo economale
from FATTURA_ATTIVA a
   where a.pg_fattura_attiva >= 0
union all
  select /*+ index (a PX_COMPENSO) */ -- Seleziona COMPENSO
     'COMPENSO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_COMPENSO
    ,cd_terzo
	,a.STATO_PAGAMENTO_FONDO_ECO
from COMPENSO a
   where a.pg_compenso >= 0
union all
  select /*+ index (a PX_ANTICIPO) */ -- Seleziona ANTICIPO
     'ANTICIPO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_ANTICIPO
    ,cd_terzo
	,a.STATO_PAGAMENTO_FONDO_ECO
from ANTICIPO a
   where a.pg_anticipo >= 0
union all
  select /*+ index (a PX_RIMBORSO) */ -- Seleziona RIMBORSO
     'RIMBORSO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_RIMBORSO
    ,cd_terzo
	,'N' -- rimborso non gestito tramite fondo economale
from RIMBORSO a
   where a.pg_rimborso >= 0
union all
  select /*+ index (a PX_MISSIONE) */ -- Seleziona MISSIONE
     'MISSIONE'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_MISSIONE
    ,cd_terzo
	,a.STATO_PAGAMENTO_FONDO_ECO
from MISSIONE a
   where a.pg_missione >= 0
)
;

   COMMENT ON TABLE "V_DOC_AMM_COGE_NOGEN_TER_FON"  IS 'Vista di estrazione del terzo e informazioni di legame con fondo economale
di documenti amministrativi escluso il documento generico';
