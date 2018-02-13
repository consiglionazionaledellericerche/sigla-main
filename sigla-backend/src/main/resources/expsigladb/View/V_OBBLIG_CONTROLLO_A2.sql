--------------------------------------------------------
--  DDL for View V_OBBLIG_CONTROLLO_A2
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_OBBLIG_CONTROLLO_A2" ("ESERCIZIO", "CD_CDS", "IM_STANZIAMENTO_A2", "IM_SALDO_OBBLIG_A2") AS 
  select
--
-- Date: 26/09/2002
-- Version: 1.2
--
-- Vista di estrazione dei parametri di controllo assunzione obbligazione
-- I parametri sono:
-- 1. Stanziamento presente su articoli ENTE relativi al CDS cd_cds per il secondo anno
--    Tali articoli spaccano il capitolo per natura ed appartengono a sezioni (funzioni) diverse
--    E' quindi necessario effettuare una aggregazione a livello del capitolo (CDS) considerando solo la parte propria (non AREA)
-- 2. Saldo obbligazioni dell'anno 2
--
-- History:
--
-- Date: 24/01/2002
-- Version: 1.0
-- Creazione
--
-- Date: 04/01/2002
-- Version: 1.1
-- Gestione AREE
--
-- Date: 26/09/2002
-- Version: 1.2
-- Fix estrazione obbligazioni
--
-- Body:
--
  esercizio,
  cd_cds,
  sum(im_stanziamento_a2) im_stanziamento_a2,
  sum(im_saldo_obblig_a2) im_saldo_obblig_a2
 from (
  select -- Stanziamento competenza anno 2
   a.esercizio esercizio,
   b.cd_cds cd_cds,
   sum(a.im_stanz_iniziale_a2) im_stanziamento_a2,
   0 im_saldo_obblig_a2
  from voce_f_saldi_cmp a, voce_f b where
       a.ti_competenza_residuo = 'C' -- CNRCTB055.TI_COMPETENZE
   and a.ti_appartenenza = 'C' -- CNRCTB001.APPARTENENZA_CNR
   and a.ti_gestione = 'S' -- CNRCTB001.GESTIONE_SPESE
   and b.esercizio = a.esercizio
   and b.ti_appartenenza = a.ti_appartenenza
   and b.ti_gestione = a.ti_gestione
   and b.cd_voce = a.cd_voce
   and b.cd_proprio_voce = b.cd_cds
   and b.ti_voce = 'E' --CNRCTB001.SOTTOARTICOLO
  group by
   a.esercizio,
   b.cd_cds
 union
  select -- Saldo obbligazioni pluriennali
   esercizio,
   cd_cds,
   0 im_stanziamento_a2,
   sum(im_obbligazione) im_saldo_obblig_a2
  from obbligazione where
       esercizio_competenza = esercizio + 1
  group by
   esercizio,
   cd_cds
 union
  select -- Stanziamento competenza anno 2
   a.esercizio esercizio,
   b.cd_proprio_voce cd_cds,
   sum(a.im_stanz_iniziale_a2) im_stanziamento_a2,
   0 im_saldo_obblig_a2
  from voce_f_saldi_cmp a, voce_f b where
       a.ti_competenza_residuo = 'C' -- CNRCTB055.TI_COMPETENZE
   and a.ti_appartenenza = 'C' -- CNRCTB001.APPARTENENZA_CNR
   and a.ti_gestione = 'S' -- CNRCTB001.GESTIONE_SPESE
   and b.esercizio = a.esercizio
   and b.ti_appartenenza = a.ti_appartenenza
   and b.ti_gestione = a.ti_gestione
   and b.cd_voce = a.cd_voce
   and b.cd_proprio_voce != b.cd_cds
   and b.ti_voce = 'E' --CNRCTB001.SOTTOARTICOLO
  group by
   a.esercizio,
   b.cd_proprio_voce
) group by
  esercizio,
  cd_cds
;

   COMMENT ON TABLE "V_OBBLIG_CONTROLLO_A2"  IS 'Vista di estrazione dei parametri di controllo assunzione obbligazione
I parametri sono:
 1. Stanziamento presente su articoli ENTE relativi al CDS cd_cds per il secondo anno
    Tali articoli spaccano il capitolo per natura ed appartengono a sezioni (funzioni) diverse
    E'' quindi necessario effettuare una aggregazione a livello del capitolo (CDS) considerando solo la parte propria (non AREA)
 2. Saldo obbligazioni dell''anno 2';
