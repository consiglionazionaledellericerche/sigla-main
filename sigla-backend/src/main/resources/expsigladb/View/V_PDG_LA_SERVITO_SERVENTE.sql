--------------------------------------------------------
--  DDL for View V_PDG_LA_SERVITO_SERVENTE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_LA_SERVITO_SERVENTE" ("CD_UO_SERVENTE", "CD_CDR_SERVENTE", "CD_LA_SERVENTE", "CD_CDR_SERVITO", "CD_LA_SERVITO", "PRC_A1", "PRC_A2", "PRC_A3") AS 
  select
--
-- Date: 24/09/2002
-- Version: 1.1
--
-- Vista che estrae le percentuali di scarico su base unitaria di quote previsionali di una la di servito su diversi cdr serventi
-- di una unica uo servente
-- La vista raggruppa logicamente i serventi per uo di appartenenza
--
-- History:
--
-- Date: 23/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/09/2002
-- Version: 1.1
-- Percentuali relativizzate ad uo servente
--
-- Body:
--
 a.cd_uo_servente,
 a.cd_cdr_servente,
 a.cd_la_servente,
 a.cd_cdr_servito,
 a.cd_la_servito,
 decode(tot_a1,0,0,a.im_a1/tot_a1),
 decode(tot_a2,0,0,a.im_a2/tot_a2),
 decode(tot_a3,0,0,a.im_a3/tot_a3)
from
V_PDG_LA_SERVITO_SERVENTE_PRE a,
(
 select
  cd_uo_servente,
  cd_cdr_servito,
  cd_la_servito,
  sum(im_a1) tot_a1,
  sum(im_a2) tot_a2,
  sum(im_a3) tot_a3
 from
  V_PDG_LA_SERVITO_SERVENTE_PRE
  group by
   cd_uo_servente,
   cd_cdr_servito,
   cd_la_servito
) b
 where
      b.cd_uo_servente = a.cd_uo_servente
  and b.cd_cdr_servito = a.cd_cdr_servito
  and b.cd_la_servito = a.cd_la_servito
;

   COMMENT ON TABLE "V_PDG_LA_SERVITO_SERVENTE"  IS 'Vista che estrae le percentuali di scarico su base unitaria di quote previsionali di una la di servito su diversi serventi
La vista raggruppa logicamente i serventi per uo di appartenenza';
