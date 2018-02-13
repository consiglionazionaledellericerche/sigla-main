--------------------------------------------------------
--  DDL for View V_MANDATO_ACCREDITAMENTO_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_ACCREDITAMENTO_CDS" ("ESERCIZIO", "CD_CDS", "PG_MANDATO", "IM_MANDATO", "CD_CDS_BENEFICIARIO") AS 
  SELECT
--
-- Date: 06/09/2002
-- Version: 1.0
--
-- Estrae i mandati di accreditamento del CNR trasmessi al cassiere e il codice del CDS beneficiario del mandato
--
-- History:
--
-- Date: 06/09/2002
-- Version: 1.0
-- Creazione
--
--
-- Body:
--
a.esercizio,
a.cd_cds,
a.pg_mandato,
a.im_mandato,
b.cd_cds_reversale
from
mandato a,
ass_mandato_reversale b
where
a.ti_mandato = 'A' and
a.STATO_TRASMISSIONE = 'T' and
a.stato <> 'A' and
a.esercizio = b.esercizio_mandato and
a.cd_cds = b.cd_cds_mandato and
a.pg_mandato = b.pg_mandato
;

   COMMENT ON TABLE "V_MANDATO_ACCREDITAMENTO_CDS"  IS 'Estrae i mandati di accreditamento del CNR trasmessi al cassiere e il codice del CDS beneficiario del mandato';
