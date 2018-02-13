--------------------------------------------------------
--  DDL for View V_OBBLIG_CONTROLLO_A1
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_OBBLIG_CONTROLLO_A1" ("ESERCIZIO", "CD_CDS", "IM_STANZIAMENTO_A1", "IM_VAR_PIU_A1", "IM_VAR_MENO_A1", "IM_RESIDUO", "IM_RES_VAR_PIU", "IM_RES_VAR_MENO", "IM_CASSA_INIZIALE", "IM_SALDO_OBBLIG_A1") AS 
  select 
-- 
-- Date: 19/02/2002 
-- Version: 1.2 
-- 
-- Vista di estrazione dei parametri di controllo assunzione obbligazione 
-- I parametri sono: 
-- 1. Stanziamento presente su articoli ENTE relativi al CDS cd_cds 
--    Tali articoli spaccano il capitolo per natura e appartengono a sezioni (funzioni) diverse 
--    E' quindi necessario effettuare una aggregazione a livello del capitolo (CDS) considerando solo la parte propria 
--    Se il cds e area estraggo i sottoarticoli corrispondenti in presidente dell'Area 
-- 2. Residui come sopra 
-- 3. Fondo di cassa iniziale 
-- 4. Saldo obbligazioni dell'anno del CDS 
-- 
-- History: 
-- 
-- Date: 24/01/2002 
-- Version: 1.0 
-- Creazione 
-- 
-- Date: 04/02/2002 
-- Version: 1.1 
-- Introdotta estrazione parte AREE + Variazioni + - 
-- 
-- Date: 19/02/2002 
-- Version: 1.2 
-- Gestione importo cassa iniziale letto da esercizio
--
-- Body: 
-- 
  esercizio, 
  cd_cds, 
  sum(im_stanziamento_a1) im_stanziamento_a1, 
  sum(im_var_piu_a1) im_var_piu_a1, 
  sum(im_var_meno_a1) im_var_meno_a1, 
  sum(im_residuo) im_residuo, 
  sum(im_res_var_piu) im_res_var_piu, 
  sum(im_res_var_meno) im_res_var_meno, 
  sum(im_cassa_iniziale) im_cassa_iniziale, 
  sum(im_saldo_obblig_a1) im_saldo_obblig_a1 
 from ( 
  select -- Stanziamento competenza CDS 
   a.esercizio esercizio, 
   b.cd_cds cd_cds, 
   sum(a.im_stanz_iniziale_a1) im_stanziamento_a1, 
   sum(a.variazioni_piu) im_var_piu_a1, 
   sum(a.variazioni_meno) im_var_meno_a1, 
   0 im_residuo, 
   0 im_res_var_piu, 
   0 im_res_var_meno, 
   0 im_cassa_iniziale, 
   0 im_saldo_obblig_a1 
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
  select -- Stanziamento residuo 
   a.esercizio esercizio, 
   b.cd_cds cd_cds, 
   0 im_stanziamento_a1, 
   0 im_var_piu_a1, 
   0 im_var_meno_a1, 
   sum(a.im_stanz_iniziale_a1) im_residuo, 
   sum(a.variazioni_piu) im_res_var_piu, 
   sum(a.variazioni_meno) im_res_var_meno, 
   0 im_cassa_iniziale, 
   0 im_saldo_obblig_a1 
  from voce_f_saldi_cmp a, voce_f b where 
       a.ti_competenza_residuo = 'R' -- CNRCTB055.TI_RESIDUI 
   and a.ti_appartenenza = 'C' -- CNRCTB001.APPARTENENZA_CNR 
   and a.ti_gestione = 'S' -- CNRCTB001.GESTIONE_SPESE 
   and b.esercizio  = a.esercizio 
   and b.ti_appartenenza = a.ti_appartenenza 
   and b.ti_gestione = a.ti_gestione 
   and b.cd_voce = a.cd_voce 
   and b.cd_proprio_voce = b.cd_cds 
   and b.ti_voce = 'E' --CNRCTB001.SOTTOARTICOLO 
  group by 
   a.esercizio, 
   b.cd_cds 
 union 
  select -- Saldo obbligazioni di competenza 
   a.esercizio esercizio, 
   a.cd_cds cd_cds, 
   0 im_stanziamento_a1, 
   0 im_var_piu_a1, 
   0 im_var_meno_a1, 
   0 im_residuo, 
   0 im_res_var_piu, 
   0 im_res_var_meno, 
   0 im_cassa_inizialel, 
   sum(im_obblig_imp_acr) im_saldo_obblig_a1 
  from voce_f_saldi_cmp a, voce_f b where 
       a.ti_competenza_residuo = 'C' -- CNRCTB055.TI_COMPETENZE 
   and a.ti_appartenenza = 'D' -- CNRCTB001.APPARTENENZA_CDS 
   and a.ti_gestione = 'S' -- CNRCTB001.GESTIONE_SPESE 
   and b.esercizio  = a.esercizio 
   and b.ti_appartenenza = a.ti_appartenenza 
   and b.ti_gestione = a.ti_gestione 
   and b.cd_voce = a.cd_voce 
   and b.cd_cds = a.cd_cds 
   and b.ti_voce in ('C','A') --CNRCTB001.CAPITOLO o CNRCTB001.ARTICOLO 
  group by 
   a.esercizio, 
   a.cd_cds 
 union 
  select -- Stanziamento competenza CDS per AREA 
   a.esercizio esercizio, 
   b.cd_proprio_voce cd_cds, 
   sum(a.im_stanz_iniziale_a1) im_stanziamento_a1, 
   sum(a.variazioni_piu) im_var_piu_a1, 
   sum(a.variazioni_meno) im_var_meno_a1, 
   0 im_residuo, 
   0 im_res_var_piu, 
   0 im_res_var_meno, 
   0 im_cassa_iniziale, 
   0 im_saldo_obblig_a1 
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
 union 
  select -- Stanziamento residuo 
   a.esercizio esercizio, 
   b.cd_proprio_voce cd_cds, 
   0 im_stanziamento_a1, 
   0 im_var_piu_a1, 
   0 im_var_meno_a1, 
   sum(a.im_stanz_iniziale_a1) im_residuo, 
   sum(a.variazioni_piu) im_res_var_piu, 
   sum(a.variazioni_meno) im_res_var_meno, 
   0 im_cassa_iniziale, 
   0 im_saldo_obblig_a1 
  from voce_f_saldi_cmp a, voce_f b where 
       a.ti_competenza_residuo = 'R' -- CNRCTB055.TI_RESIDUI 
   and a.ti_appartenenza = 'C' -- CNRCTB001.APPARTENENZA_CNR 
   and a.ti_gestione = 'S' -- CNRCTB001.GESTIONE_SPESE 
   and b.esercizio  = a.esercizio 
   and b.ti_appartenenza = a.ti_appartenenza 
   and b.ti_gestione = a.ti_gestione 
   and b.cd_voce = a.cd_voce 
   and b.cd_proprio_voce != b.cd_cds 
   and b.ti_voce = 'E' --CNRCTB001.SOTTOARTICOLO 
  group by 
   a.esercizio, 
   b.cd_proprio_voce 
 union 
  select -- Cassa iniziale 
   esercizio esercizio,
   cd_cds cd_cds, 
   0 im_stanziamento_a1, 
   0 im_var_piu_a1, 
   0 im_var_meno_a1, 
   0 im_residuo, 
   0 im_res_var_piu, 
   0 im_res_var_meno, 
   im_cassa_iniziale im_cassa_iniziale, 
   0 im_saldo_obblig_a1 
  from esercizio 
) group by 
  esercizio, 
  cd_cds;

   COMMENT ON TABLE "V_OBBLIG_CONTROLLO_A1"  IS 'Vista di estrazione dei parametri di controllo assunzione obbligazione 
I parametri sono: 
 1. Stanziamento presente su articoli ENTE relativi al CDS cd_cds 
    Tali articoli spaccano il capitolo per natura e appartengono a sezioni (funzioni) diverse 
    E'' quindi necessario effettuare una aggregazione a livello del capitolo (CDS) considerando solo la parte propria 
    Se il cds e area estraggo i sottoarticoli corrispondenti in presidente dell''Area 
 2. Residui come sopra 
 3. Fondo di cassa iniziale 
 4. Saldo obbligazioni dell''anno del CDS';
