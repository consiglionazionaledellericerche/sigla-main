--------------------------------------------------------
--  DDL for View VP_DPDG_ETR_ENTE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DPDG_ETR_ENTE" ("ESERCIZIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_TITOLO", "DS_TITOLO", "CD_CAPOCONTO", "DS_CAPOCONTO", "CD_FUNZIONE", "DS_FUNZIONE", "CD_NATURA", "DS_NATURA", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE") AS 
  (
select
--
-- Date: 07/09/2002
-- Version: 1.0
--
-- Vista di stampa dei dettagli di entrata per ENTE
-- La vista effettua l'elisione di tutti i ricavi derivanti da altri CDR
-- La vista non effettua controlli su STO
--
-- History:
--
-- Date: 07/09/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
 d.ESERCIZIO
,e.TI_APPARTENENZA
,e.TI_GESTIONE
,e.CD_ELEMENTO_VOCE
,f.DS_ELEMENTO_VOCE
,ft.CD_ELEMENTO_VOCE
,ft.DS_ELEMENTO_VOCE
,cfin.CD_CAPOCONTO_FIN
,cfin.DS_CAPOCONTO_FIN
,e.cd_funzione
,i.ds_funzione
,e.cd_natura
,j.ds_natura
,sum(e.IM_RA_RCE)
,sum(e.IM_RB_RSE)
,sum(e.IM_RC_ESR)
,sum(e.IM_RD_A2_RICAVI)
,sum(e.IM_RE_A2_ENTRATE)
,sum(e.IM_RF_A3_RICAVI)
,sum(e.IM_RG_A3_ENTRATE)
from
PDG_PREVENTIVO d, --Testata pdg
PDG_PREVENTIVO_ETR_DET e, -- Dettaglio
ELEMENTO_VOCE f, -- Elemento voce del dettaglio
ELEMENTO_VOCE fc, -- Categoria
CAPOCONTO_FIN cfin, -- Capoconto finanziario
ELEMENTO_VOCE ft, -- Titolo
FUNZIONE i, -- Funzione
NATURA j -- Natura
where
    d.CD_CENTRO_RESPONSABILITA=e.CD_CENTRO_RESPONSABILITA
and e.ESERCIZIO=d.ESERCIZIO
and e.STATO = 'Y'
and e.categoria_dettaglio = 'SIN'
and f.ESERCIZIO=d.ESERCIZIO
and f.TI_APPARTENENZA=e.TI_APPARTENENZA
and f.TI_GESTIONE=e.TI_GESTIONE
and f.CD_ELEMENTO_VOCE=e.CD_ELEMENTO_VOCE
and fc.ESERCIZIO=f.ESERCIZIO
and fc.TI_APPARTENENZA=f.TI_APPARTENENZA
and fc.TI_GESTIONE=f.TI_GESTIONE
and fc.CD_ELEMENTO_VOCE=f.CD_ELEMENTO_PADRE
and ft.ESERCIZIO=fc.ESERCIZIO
and ft.TI_APPARTENENZA=fc.TI_APPARTENENZA
and ft.TI_GESTIONE=fc.TI_GESTIONE
and ft.CD_ELEMENTO_VOCE=fc.CD_ELEMENTO_PADRE
and cfin.CD_CAPOCONTO_FIN(+)=f.CD_CAPOCONTO_FIN
and i.cd_funzione (+)= e.cd_funzione
and j.cd_natura = e.cd_natura
group by
 d.ESERCIZIO
,e.TI_APPARTENENZA
,e.TI_GESTIONE
,e.CD_ELEMENTO_VOCE
,f.DS_ELEMENTO_VOCE
,ft.CD_ELEMENTO_VOCE
,ft.DS_ELEMENTO_VOCE
,cfin.CD_CAPOCONTO_FIN
,cfin.DS_CAPOCONTO_FIN
,e.cd_funzione
,i.ds_funzione
,e.cd_natura
,j.ds_natura
)
;

   COMMENT ON TABLE "VP_DPDG_ETR_ENTE"  IS 'Vista di stampa dei dettagli di entrata per ENTE
La vista effettua l''elisione di tutti i ricavi derivanti da altri CDR
La vista non effettua controlli su STO';
