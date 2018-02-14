--------------------------------------------------------
--  DDL for View V_BIL_PREV_SOTTOARTICOLI_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_BIL_PREV_SOTTOARTICOLI_CNR" ("ESERCIZIO", "TI_GESTIONE", "TI_APPARTENENZA", "CD_VOCE_PADRE", "CD_VOCE", "CD_PROPRIO_VOCE", "CD_CDS", "CD_NATURA", "IM_STANZ_INIZIALE_A1", "IM_STANZ_INIZIALE_A2", "IM_STANZ_INIZIALE_A3") AS 
  (select
--
-- Date: 09/09/2002
-- Version: 1.3
--
-- Vista di estrazione dei sottoarticoli del bilancio CNR parte spese eligibili per alimentare il bilancio
-- preventivo CDS parte entrata: la vista
-- Entro sui sotto articoli della parte spese del CNR e ci sono due casi
-- cd_cds NON AREA
--   In questo caso estraggo tutti i sottoarticoli del capitolo = a cd_cds con importi con codice proprio = a quello del
--   CDS in processo
-- La vista ritorna i totali assestati
--
-- cd_cds AREA
--   In questo caso estraggo tutti i sottoarticoli con importi definiti sotto tutti i capitoli-articoli che riportino
--   l'area come codice proprio del livello, indipendentemente dal CDS-CAPITOLO sotto cui si trovano
--
-- La vista aggrega i dati indipendentemente dalla funzione
-- La vista non verifica la validita dell'STO
--
-- History:
--
-- Date: 03/10/2001
-- Version: 1.0
-- Creazione
-- Date: 14/11/2001
-- Version: 1.1
-- Eliminazione esercizio da STO
-- Date: 26/02/2002
-- Version: 1.2
-- Fix derrore di estrazione degli stanziamenti fatti su area e CDS SAC
-- Estrae solo dati con almeno un importo diverso da 0
-- Date: 09/09/2002
-- Version: 1.3
-- Estrae l'assestato per il primo anno sommando iniziale e variazioni
--
-- Body:
--
 b.ESERCIZIO
,b.TI_GESTIONE
,b.TI_APPARTENENZA
,a.CD_VOCE_PADRE
,b.CD_VOCE
,a.CD_PROPRIO_VOCE
,c.CD_UNITA_ORGANIZZATIVA
,a.CD_NATURA
,sum(b.IM_STANZ_INIZIALE_A1 + b.variazioni_piu - b.variazioni_meno)
,sum(b.IM_STANZ_INIZIALE_A2)
,sum(b.IM_STANZ_INIZIALE_A3)
from
UNITA_ORGANIZZATIVA c,  -- CDS
VOCE_F a, -- Dove risiedono i sottoarticoli
VOCE_F_SALDI_CMP b -- Dove stanno le previsioni
where
    b.ti_appartenenza = 'C'
and b.ti_gestione = 'S'
and a.ti_voce = 'E'
--and a.cd_categoria = 1
and a.cd_parte = 1
and a.esercizio = b.esercizio
and a.ti_gestione = b.ti_gestione
and a.ti_appartenenza = b.ti_appartenenza
and a.cd_voce = b.cd_voce
and c.fl_cds = 'Y'
and (
 (
      c.cd_tipo_unita = 'AREA'
  and a.cd_proprio_voce = c.cd_unita_organizzativa
 ) or (
      c.cd_tipo_unita != 'AREA'
  and a.cd_cds = c.cd_unita_organizzativa
  and a.cd_proprio_voce = c.cd_unita_organizzativa
 )
)
 and (
     b.IM_STANZ_INIZIALE_A1 + b.variazioni_piu - b.variazioni_meno != 0
  or b.IM_STANZ_INIZIALE_A2 != 0
  or b.IM_STANZ_INIZIALE_A3 != 0
 )
group by
 b.ESERCIZIO
,b.TI_GESTIONE
,b.TI_APPARTENENZA
,a.CD_VOCE_PADRE
,b.CD_VOCE
,a.CD_PROPRIO_VOCE
,c.CD_UNITA_ORGANIZZATIVA
,a.CD_NATURA)
;

   COMMENT ON TABLE "V_BIL_PREV_SOTTOARTICOLI_CNR"  IS 'Vista di estrazione dei sottoarticoli del bilancio CNR parte spese eligibili per alimentare il bilancio
preventivo CDS parte entrata: la vista
Entro sui sotto articoli della parte spese del CNR e ci sono due casi
cd_cds NON AREA
  In questo caso estraggo tutti i sottoarticoli con importi con codice proprio = a quello del
  CDS in processo
cd_cds AREA
  In questo caso estraggo tutti i sottoarticoli con importi definiti in tutti che riportino
  l''area come codice proprio del livello, indipendentemente dal CDS-CAPITOLO sotto cui si trovano
La vista non verifica la validità dell''STO
La vista ritorna i totali assestati';
