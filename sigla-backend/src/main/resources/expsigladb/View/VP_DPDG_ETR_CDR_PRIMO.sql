--------------------------------------------------------
--  DDL for View VP_DPDG_ETR_CDR_PRIMO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DPDG_ETR_CDR_PRIMO" ("ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "CD_CDS", "DS_CDS", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "STATO", "ANNOTAZIONI", "FL_RIBALTATO_SU_AREA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_TITOLO", "DS_TITOLO", "CD_CAPOCONTO", "DS_CAPOCONTO", "CD_FUNZIONE", "DS_FUNZIONE", "CD_NATURA", "DS_NATURA", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE") AS 
  (
select
--
-- Date: 07/09/2002
-- Version: 1.0
--
-- Vista di stampa dei dettagli di entrata CDR di PRIMO LIVELLO
-- La vista effettua l'elisione dei ricavi figurativi, cioè di tutti quei ricavi provenienti da CDR sotto lo stesso CDR di primo livello
-- o dal CDR di primo livello stesso
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
,c_primo.CD_UNITA_ORGANIZZATIVA
,b.DS_UNITA_ORGANIZZATIVA
,a.CD_UNITA_ORGANIZZATIVA
,a.DS_UNITA_ORGANIZZATIVA
,c_primo.CD_CENTRO_RESPONSABILITA
,c_primo.DS_CDR
,d.STATO
,d.ANNOTAZIONI
,d.FL_RIBALTATO_SU_AREA
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
UNITA_ORGANIZZATIVA a, -- CDS di afferenza del CDR di primo livello
UNITA_ORGANIZZATIVA b, -- UO di afferenza del CDR di primo livello
CDR c_primo, -- Cdr di primo livello
CDR c, -- Cdr pdg
PDG_PREVENTIVO d, --Testata pdg
PDG_PREVENTIVO_ETR_DET e, -- Dettaglio
CDR c_cdr_coll,
ELEMENTO_VOCE f, -- Elemento voce del dettaglio
ELEMENTO_VOCE fc, -- Categoria
CAPOCONTO_FIN cfin, -- Capoconto finanziario
ELEMENTO_VOCE ft, -- Titolo
FUNZIONE i, -- Funzione
NATURA j -- Natura
where
    a.fl_cds = 'Y'
and b.CD_UNITA_PADRE=a.CD_UNITA_ORGANIZZATIVA
and b.fl_cds = 'N'
and b.CD_UNITA_ORGANIZZATIVA=c_primo.CD_UNITA_ORGANIZZATIVA
and d.CD_CENTRO_RESPONSABILITA=c.CD_CENTRO_RESPONSABILITA
and (
    c.cd_centro_responsabilita =  c_primo.cd_centro_responsabilita
 or c.cd_cdr_afferenza =  c_primo.cd_centro_responsabilita
)
and e.ESERCIZIO=d.ESERCIZIO
and e.CD_CENTRO_RESPONSABILITA=c.CD_CENTRO_RESPONSABILITA
and e.STATO = 'Y'
and (
      e.categoria_dettaglio = 'SIN' and c_cdr_coll.cd_centro_responsabilita = e.cd_centro_responsabilita
   or (
         e.categoria_dettaglio = 'CAR'
    and  c_cdr_coll.cd_centro_responsabilita =e.cd_centro_responsabilita_clgs
    and (
          c_cdr_coll.cd_cdr_afferenza is null and c_cdr_coll.cd_centro_responsabilita <> c_primo.cd_centro_responsabilita
       or c_cdr_coll.cd_cdr_afferenza is not null and c_cdr_coll.cd_cdr_afferenza <> c_primo.cd_centro_responsabilita
    )
   )
)
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
,c_primo.CD_UNITA_ORGANIZZATIVA
,b.DS_UNITA_ORGANIZZATIVA
,a.CD_UNITA_ORGANIZZATIVA
,a.DS_UNITA_ORGANIZZATIVA
,c_primo.CD_CENTRO_RESPONSABILITA
,c_primo.DS_CDR
,d.STATO
,d.ANNOTAZIONI
,d.FL_RIBALTATO_SU_AREA
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

   COMMENT ON TABLE "VP_DPDG_ETR_CDR_PRIMO"  IS 'Vista di stampa dei dettagli di entrata CDR di PRIMO LIVELLO
La vista effettua l''elisione dei ricavi figurativi, cioè di tutti quei ricavi provenienti da CDR sotto lo stesso CDR di primo livello
o dal CDR di primo livello stesso.
La vista non effettua controlli su STO';
