--------------------------------------------------------
--  DDL for View V_CDP_TOT_PRC_SCR_ALUO_LINEA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_TOT_PRC_SCR_ALUO_LINEA" ("ESERCIZIO", "MESE", "ID_MATRICOLA", "CD_UO_MATRICOLA", "CD_UNITA_ORGANIZZATIVA", "PRC_A1", "PRC_A2", "PRC_A3") AS 
  (select
--
-- Date: 19/09/2002
-- Version: 1.2
--
-- Vista di estrazione totale percentuali scaricate su UO della stessa linea dell'UO di
-- appartenenza della matricola
-- La vista non vrifica la validita dell'STO
--
-- History:
--
-- Date: 19/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2001
-- Version: 1.1
-- Eliminazione esercizio da STO
--
-- Date: 19/09/2002
-- Version: 1.2
-- Aggiunta colonna MESE
--
-- Body:
--
a.esercizio,
a.MESE,
a.id_matricola,
b.cd_unita_organizzativa,
c.cd_unita_organizzativa,
sum(e.prc_la_a1),
sum(e.prc_la_a2),
sum(e.prc_la_a3)
from
v_cdp_matricola_uo a, -- Tabella costo del dipendente
unita_organizzativa b, -- Uo del dipendente
unita_organizzativa c, -- Uo della linea del CDR I livello
cdr d, -- CDR afferente all'UO
ass_cdp_la e,
ass_cdp_uo f
where
b.cd_unita_organizzativa = a.cd_unita_organizzativa
and (
(
b.cd_tipo_unita in ('SAC','AREA')
and c.cd_unita_organizzativa = b.cd_unita_organizzativa
) or (
b.cd_tipo_unita in ('IST','PNIR')
and c.cd_unita_padre = b.cd_unita_padre
)
)
and f.esercizio = a.esercizio
and f.mese = a.mese
and f.id_matricola = a.id_matricola
and b.cd_unita_organizzativa != c.cd_unita_organizzativa -- UO diversa da quella di appartenenza della matricola
and c.cd_unita_organizzativa = f.cd_unita_organizzativa
and f.stato = 'Y'
and d.cd_unita_organizzativa = c.cd_unita_organizzativa
and e.esercizio = a.esercizio
and e.mese = a.mese
and e.id_matricola = a.id_matricola
and e.cd_centro_responsabilita = d.cd_centro_responsabilita
and e.stato = 'S' -- Scaricato su PDG
group by
a.esercizio,
a.mese,
a.id_matricola,
b.cd_unita_organizzativa,
c.cd_unita_organizzativa
)
;

   COMMENT ON TABLE "V_CDP_TOT_PRC_SCR_ALUO_LINEA"  IS 'Vista di estrazione totale percentuali scaricate su UO della stessa linea dell''UO di
appartenenza della matricola
La vista non vrifica la validità dell''STO';
