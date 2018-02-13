--------------------------------------------------------
--  DDL for View V_CDP_TOT_PRC_SCR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_TOT_PRC_SCR" ("ESERCIZIO", "MESE", "CD_UNITA_ORGANIZZATIVA", "ID_MATRICOLA", "PRC_A1", "PRC_A2", "PRC_A3") AS 
  ( SELECT
--
-- Date: 05/02/2004
-- Version: 1.3
--
-- Totalizza le percentuali dei costi del dipendente
-- Somma i costi scaricati su PDG (propri) +
--         costi verso altra uo confermati
--
-- History:
--
-- Date: 01/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 20/10/2001
-- Version: 1.1
-- Aggiunta UO di appartenenza del dipendente in interfaccia
--
-- Date: 19/09/2002
-- Version: 1.2
-- Aggiunta colonna MESE
--
-- Date: 05/02/2004
-- Version: 1.3
-- Matricole con SOLO configurazioni di scarico verso altra UO non confermate o rifiutate non venivano estratte
--
-- Body:
--
 ESERCIZIO,
 MESE,
 CD_UNITA_ORGANIZZATIVA,
 ID_MATRICOLA,
 SUM(PRC_A1),
 SUM(PRC_A2),
 SUM(PRC_A3)
FROM (
 SELECT
  a.ESERCIZIO ESERCIZIO,
  a.MESE MESE,
  b.CD_UNITA_ORGANIZZATIVA CD_UNITA_ORGANIZZATIVA,
  a.ID_MATRICOLA ID_MATRICOLA,
  a.PRC_LA_A1 PRC_A1,
  a.PRC_LA_A2 PRC_A2,
  a.PRC_LA_A3 PRC_A3
 FROM
  ASS_CDP_LA a,
  V_CDP_MATRICOLA_UO b
 WHERE
      a.FL_DIP_ALTRA_UO = 'N' -- Ripartizione costi propri
  AND a.STATO = 'S'           -- Scaricato su PDG
  AND b.ESERCIZIO = a.ESERCIZIO
  AND b.MESE = a.MESE
  AND b.ID_MATRICOLA = a.ID_MATRICOLA
 UNION ALL
  SELECT
   a.ESERCIZIO ESERCIZIO,
   a.MESE MESE,
   b.CD_UNITA_ORGANIZZATIVA CD_UNITA_ORGANIZZATIVA,
   a.ID_MATRICOLA ID_MATRICOLA,
   a.PRC_UO_A1 PRC_A1,
   a.PRC_UO_A2 PRC_A2,
   a.PRC_UO_A3 PRC_A3
  FROM
   ASS_CDP_UO a,
   V_CDP_MATRICOLA_UO b
  WHERE
       a.STATO = 'Y'
   AND b.ESERCIZIO = a.ESERCIZIO
   AND b.MESE = a.MESE
   AND b.ID_MATRICOLA = a.ID_MATRICOLA
  UNION ALL
   SELECT
    b.ESERCIZIO ESERCIZIO,
	b.MESE MESE,
    b.CD_UNITA_ORGANIZZATIVA CD_UNITA_ORGANIZZATIVA,
    b.ID_MATRICOLA ID_MATRICOLA,
    0,
    0,
    0
   FROM
    V_CDP_MATRICOLA_UO b
   WHERE
    not exists (select 1 from ASS_CDP_UO a where
         b.ESERCIZIO = a.ESERCIZIO
     AND b.MESE = a.MESE
     AND b.ID_MATRICOLA = a.ID_MATRICOLA
     AND a.STATO = 'Y'
    )
    and not exists (select 1 from ASS_CDP_LA a where
         b.ESERCIZIO = a.ESERCIZIO
     AND b.MESE = a.MESE
     AND b.ID_MATRICOLA = a.ID_MATRICOLA
    )
 )
 GROUP BY
 ESERCIZIO,
 MESE,
 ID_MATRICOLA,
 CD_UNITA_ORGANIZZATIVA
)
;

   COMMENT ON TABLE "V_CDP_TOT_PRC_SCR"  IS 'Totalizza le percentuali dei costi del dipendente
Somma i costi scaricati su PDG (propri) +
        costi verso altra uo confermati';
