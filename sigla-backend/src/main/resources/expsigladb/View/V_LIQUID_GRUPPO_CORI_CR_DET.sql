--------------------------------------------------------
--  DDL for View V_LIQUID_GRUPPO_CORI_CR_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_GRUPPO_CORI_CR_DET" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "PG_LIQUIDAZIONE_ORIGINE", "CD_GRUPPO_CR", "IM_LIQUIDATO", "PG_LIQUIDAZIONE", "FL_ACCENTRATO", "CD_REGIONE", "PG_COMUNE", "CD_TERZO_VERSAMENTO", "CD_MODALITA_PAGAMENTO", "PG_BANCA") AS 
  select
--
-- Date: 25/02/2003
-- Version: 1.3
--
-- View di join tra liquid_gruppo_cori e gruppo_cr_det
--
-- History:
--
-- Date: 23/06/2003
-- Version: 1.0
-- Creazione vista
--
-- Body:
--
 a.esercizio, a.cd_cds, a.cd_unita_organizzativa,
 a.cd_cds_origine, a.cd_uo_origine, a.pg_liquidazione_origine,
 a.cd_gruppo_cr, a.im_liquidato, a.pg_liquidazione,
 a.fl_accentrato, a.cd_regione, a.pg_comune,
 c.cd_terzo_versamento, c.cd_modalita_pagamento, c.pg_banca
from liquid_gruppo_cori a, gruppo_cr_det c
where
    c.esercizio = a.esercizio
and c.cd_gruppo_cr = a.cd_gruppo_cr
and c.cd_regione = a.cd_regione
and c.pg_comune = a.pg_comune
;

   COMMENT ON TABLE "V_LIQUID_GRUPPO_CORI_CR_DET"  IS 'View di join tra liquid_gruppo_cori e gruppo_cr_det';
