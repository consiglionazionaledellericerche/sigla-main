--------------------------------------------------------
--  DDL for View V_CDP_TOTALI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_TOTALI" ("ESERCIZIO", "MESE", "CD_UNITA_ORGANIZZATIVA", "ID_MATRICOLA", "PRC_A1", "PRC_A2", "PRC_A3") AS 
  ( SELECT
--
-- Date: 19/09/2002
-- Version: 1.2
--
-- Totalizza le percentuali dei costi del dipendente per ogni uo
-- Somma i costi scaricati per linea di attivita e cdr appartenenti
-- alla uo specificata piu i costi scaricati su altra uo (solo se
-- la matricola appartiene alla UO specificata
-- La vista non esegue controlli di validita dell'STO
--
-- History:
--
-- Date: 12/07/2001
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2001
-- Version: 1.1
-- Eliminazione esercizio STO
--
-- Date: 19/09/2002
-- Version: 1.2
-- Aggiunta colonna MESE
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
ASS_CDP_LA.ESERCIZIO,
ASS_CDP_LA.MESE,
CDR.CD_UNITA_ORGANIZZATIVA,
ASS_CDP_LA.ID_MATRICOLA,
ASS_CDP_LA.PRC_LA_A1 PRC_A1,
ASS_CDP_LA.PRC_LA_A2 PRC_A2,
ASS_CDP_LA.PRC_LA_A3 PRC_A3
FROM
ASS_CDP_LA,
CDR
WHERE
CDR.CD_CENTRO_RESPONSABILITA = ASS_CDP_LA.CD_CENTRO_RESPONSABILITA
UNION ALL
SELECT
ASS_CDP_UO.ESERCIZIO,
ASS_CDP_UO.MESE,
V_CDP_MATRICOLA_UO.CD_UNITA_ORGANIZZATIVA,
ASS_CDP_UO.ID_MATRICOLA,
ASS_CDP_UO.PRC_UO_A1 PRC_A1,
ASS_CDP_UO.PRC_UO_A2 PRC_A2,
ASS_CDP_UO.PRC_UO_A3 PRC_A3
FROM
ASS_CDP_UO,
V_CDP_MATRICOLA_UO
WHERE
ASS_CDP_UO.ESERCIZIO = V_CDP_MATRICOLA_UO.ESERCIZIO AND
ASS_CDP_UO.MESE = V_CDP_MATRICOLA_UO.MESE AND
ASS_CDP_UO.ID_MATRICOLA = V_CDP_MATRICOLA_UO.ID_MATRICOLA AND
STATO <> 'N' )
GROUP BY
ESERCIZIO,
MESE,
ID_MATRICOLA,
CD_UNITA_ORGANIZZATIVA
)
;

   COMMENT ON TABLE "V_CDP_TOTALI"  IS 'Totalizza le percentuali dei costi del dipendente
Somma i costi scaricati per linea di attività e cdr (escludendo
i cdr non appartenenti al cds del dipendente) più i costi
scaricati su altra uo (escludendo quelli non accettati)
La vista non esegue controlli di validità dell''STO';
