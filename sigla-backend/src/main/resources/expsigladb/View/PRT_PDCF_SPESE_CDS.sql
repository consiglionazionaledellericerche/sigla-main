--------------------------------------------------------
--  DDL for View PRT_PDCF_SPESE_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_PDCF_SPESE_CDS" ("ESERCIZIO", "VOCE", "DESCRIZIONE_VOCE", "CATEGORIA", "TIPO_CDS", "FUNZIONE", "DESCRIZIONE_FUNZIONE", "DESCRIZIONE_CATEGORIA") AS 
  SELECT
--
-- Date: 03/12/2003
-- Version: 1.1
--
-- Vista di stampa Piano dei conti finanziario (spese)
--
-- History:
--
-- Date: 19/02/2003
-- Version: 1.0
-- Creazione: questi commenti non erano stati forniti da CNR
--
-- Date: 03/12/2003
-- Version: 1.1
-- Modifica: eliminati gli outerjoin per ottimizzare la stampa (Cineca)
--
-- Body
--
   DISTINCT        a.esercizio, a.cd_elemento_voce, a.ds_elemento_voce,
                   a.cd_capoconto_fin, b.cd_tipo_unita, b.cd_funzione,
                   c.ds_funzione, d.ds_capoconto_fin
              FROM elemento_voce a,
                   ass_ev_funz_tipocds b,
                   funzione c,
                   capoconto_fin d
             WHERE a.ti_appartenenza = 'D'
               AND a.ti_gestione = 'S'
               AND b.cd_conto = a.cd_elemento_voce
               AND c.cd_funzione = b.cd_funzione
               And c.fl_utilizzabile = 'Y'
               AND d.cd_capoconto_fin = a.cd_capoconto_fin
          ORDER BY b.cd_tipo_unita,
                   b.cd_funzione,
                   a.cd_capoconto_fin,
                   a.cd_elemento_voce;
