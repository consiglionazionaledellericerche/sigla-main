--------------------------------------------------------
--  DDL for View V_CDP_TOT_PRC_LELLO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_TOT_PRC_LELLO" ("ESERCIZIO", "MESE", "CD_UNITA_ORGANIZZATIVA", "ID_MATRICOLA", "PRC_A1", "PRC_A2", "PRC_A3") AS 
  ( SELECT
--
-- Date: 09/03/2006
-- Version: 1.5
--
-- Totalizza le percentuali dei costi del dipendente configurate
-- Somma i costi configurati per scarico su PDG (propri) +
--         costi configurati verso altra uo confermati
--
-- History:
--
-- Date: 27/10/2001
-- Version: 1.2
-- Creazione
--
-- Date: 19/09/2002
-- Version: 1.3
-- Aggiunta colonna MESE
--
-- Date: 05/02/2004
-- Version: 1.4
-- Matricole con SOLO configurazioni di scarico verso altra UO non confermate o rifiutate non venivano estratte
--
-- Date: 09/03/2006
-- Version: 1.5
-- Matricole con SOLO configurazioni di scarico verso altra UO non confermate o rifiutate non venivano estratte
-- per la UO su cui erano scaricate
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
  AND b.ESERCIZIO = a.ESERCIZIO
  AND b.MESE = a.MESE
  AND b.ID_MATRICOLA = a.ID_MATRICOLA
/* UNION ALL
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
*/
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
  --COSTI DIPENDENTI ATTRIBUITI AD ALTRE UO
  UNION ALL
   SELECT
    a.ESERCIZIO ESERCIZIO,
    a.MESE MESE,
    c.CD_UNITA_ORGANIZZATIVA CD_UNITA_ORGANIZZATIVA,
    a.ID_MATRICOLA ID_MATRICOLA,
    a.PRC_LA_A1*(b.prc_uo_a1/100) PRC_A1,
    a.PRC_LA_A2*(b.prc_uo_a2/100) PRC_A2,
    a.PRC_LA_A3*(b.prc_uo_a3/100) PRC_A3
   FROM
    ASS_CDP_LA a,
    ASS_CDP_UO b,
    V_STRUTTURA_ORGANIZZATIVA c
   WHERE
        a.FL_DIP_ALTRA_UO = 'Y' -- Ripartizione costi altrui
    AND b.ESERCIZIO = a.ESERCIZIO
    AND b.MESE = a.MESE
    AND b.ID_MATRICOLA = a.ID_MATRICOLA
    AND a.ESERCIZIO = c.ESERCIZIO
    AND a.CD_CENTRO_RESPONSABILITA = c.CD_ROOT
    And b.CD_UNITA_ORGANIZZATIVA = c.CD_UNITA_ORGANIZZATIVA
    And (prc_uo_a1 != 0 Or
         prc_uo_a2 != 0 Or
         prc_uo_a3 != 0)
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
    ASS_CDP_UO b
   Where
        (prc_uo_a1 != 0 Or
         prc_uo_a2 != 0 Or
         prc_uo_a3 != 0) And
        (b.STATO = 'X' Or
         (b.STATO = 'Y' And
          not exists (select 1 from ASS_CDP_LA a, V_STRUTTURA_ORGANIZZATIVA c
                      where a.FL_DIP_ALTRA_UO = 'Y' -- Ripartizione costi altrui
                       AND b.ESERCIZIO = a.ESERCIZIO
                       AND b.MESE = a.MESE
                       AND b.ID_MATRICOLA = a.ID_MATRICOLA
                       AND a.ESERCIZIO = c.ESERCIZIO
                       AND a.CD_CENTRO_RESPONSABILITA = c.CD_ROOT
                       AND c.CD_UNITA_ORGANIZZATIVA = b.CD_UNITA_ORGANIZZATIVA)))
 )
 GROUP BY
 ESERCIZIO,
 MESE,
 ID_MATRICOLA,
 CD_UNITA_ORGANIZZATIVA
);
