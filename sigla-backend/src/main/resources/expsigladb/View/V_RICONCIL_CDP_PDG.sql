--------------------------------------------------------
--  DDL for View V_RICONCIL_CDP_PDG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_RICONCIL_CDP_PDG" ("ESERCIZIO", "MESE", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "IM_DEL_A1", "IM_DEL_A2", "IM_DEL_A3", "IM_PDG_A1", "IM_PDG_A2", "IM_PDG_A3", "IM_CDP_A1", "IM_CDP_A2", "IM_CDP_A3") AS 
  select
--
-- Date: 23/09/2002
-- Version: 1.0
--
-- Vista di riconciliazione dllo spaccato mensile (pdg se mese = 0) costi dipendente tra CDP e PDG
--
-- History:
-- Date: 23/09/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
 esercizio,
 mese,
 ti_appartenenza,
 ti_gestione,
 cd_elemento_voce,
 sum(im_pdg_a1-im_cdp_a1),
 sum(im_pdg_a2-im_cdp_a2),
 sum(im_pdg_a3-im_cdp_a3),
 sum(im_pdg_a1),
 sum(im_pdg_a2),
 sum(im_pdg_a3),
 sum(im_cdp_a1),
 sum(im_cdp_a2),
 sum(im_cdp_a3)
from
(select
  esercizio,
  0 mese,
  ti_appartenenza,
  ti_gestione,
  cd_elemento_voce,
  IM_RH_CCS_COSTI  + IM_RO_CSS_ALTRI_COSTI  im_pdg_a1,
  IM_RAA_A2_COSTI_FINALI im_pdg_a2,
  IM_RAH_A3_COSTI_FINALI im_pdg_a3,
  0 im_cdp_a1,
  0 im_cdp_a2,
  0 im_cdp_a3
 from pdg_preventivo_spe_det where
       origine = 'STI'
   and categoria_dettaglio in ('SIN','SCR')
 union all
 select
  a.esercizio,
  a.mese,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_elemento_voce,
  0 im_pdg_a1,
  0 im_pdg_a2,
  0 im_pdg_a3,
  im_a1*b.prc_a1 im_cdp_a1,
  decode(ti_rapporto,'IND',im_a2*b.prc_a2,0) im_cdp_a2,
  decode(ti_rapporto,'IND',im_a3*b.prc_a3,0) im_cdp_a3
 from costo_del_dipendente a, V_CDP_SPACCATO_CDR_LA_MATR b where
      a.id_matricola = b.id_matricola
  and a.mese=b.mese
  and a.esercizio = b.esercizio
  and b.stato = 'S'
 union all
 select
  a.esercizio,
  a.mese,
  a.ti_appartenenza,
  a.ti_gestione,
  c.val01,
  0 im_pdg_a1,
  0 im_pdg_a2,
  0 im_pdg_a3,
  im_oneri_cnr_a1*b.prc_a1 im_cdp_a1,
  decode(ti_rapporto,'IND',im_oneri_cnr_a2*b.prc_a2,0) im_cdp_a2,
  decode(ti_rapporto,'IND',im_oneri_cnr_a3*b.prc_a3,0) im_cdp_a3
 from costo_del_dipendente a, V_CDP_SPACCATO_CDR_LA_MATR b, configurazione_cnr c where
      a.id_matricola = b.id_matricola
  and a.esercizio = b.esercizio
  and a.mese=b.mese
  and b.stato = 'S'
  and c.esercizio = b.esercizio
  and c.cd_chiave_primaria = 'ELEMENTO_VOCE_SPECIALE'
  and c.cd_chiave_secondaria = 'ONERI_CNR'
 union all
 select
  a.esercizio,
  a.mese,
  a.ti_appartenenza,
  a.ti_gestione,
  c.val01,
  0 im_pdg_a1,
  0 im_pdg_a2,
  0 im_pdg_a3,
  im_tfr_a1*b.prc_a1 im_cdp_a1,
  im_tfr_a2*b.prc_a2 im_cdp_a2,
  im_tfr_a3*b.prc_a3 im_cdp_a3
 from costo_del_dipendente a, V_CDP_SPACCATO_CDR_LA_MATR b, configurazione_cnr c where
      a.id_matricola = b.id_matricola
  and a.esercizio = b.esercizio
  and a.mese=b.mese
  and b.stato = 'S'
  and c.esercizio = b.esercizio
  and c.cd_chiave_primaria = 'ELEMENTO_VOCE_SPECIALE'
  and c.cd_chiave_secondaria = 'TFR'
) group by
  esercizio,
  mese,
  ti_appartenenza,
  ti_gestione,
  cd_elemento_voce
;

   COMMENT ON TABLE "V_RICONCIL_CDP_PDG"  IS 'Vista di riconciliazione dllo spaccato mensile (pdg se mese = 0) costi dipendente tra CDP e PDG';
