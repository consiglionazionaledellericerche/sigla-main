--------------------------------------------------------
--  DDL for View V_CDP_SPACC_CDR_LA_VOCE_LELLO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_SPACC_CDR_LA_VOCE_LELLO" ("ESERCIZIO", "MESE", "CD_CDR_ROOT", "CD_CDR", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_LINEA_ATTIVITA", "TI_RAPPORTO", "TI_PREV_CONS", "IM_A1", "IM_A2", "IM_A3", "IM_RND_A1", "IM_RND_A2", "IM_RND_A3") AS 
  select
--
-- Date: 08/01/2004
-- Version: 1.1
--
-- Vista di estrazione dello spaccato mensile (pdg se mese = 0) costi dipendente per cdr/voce/la
--
-- History:
-- Date: 23/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 08/01/2004
-- Version: 1.1
-- Estrazione delgli arrotondamenti causati dalla precisione (15,2)
--
-- Date: 06/10/2005
-- Version: 1.2
-- Separati gli Oneri CNR ed il TFR del personale a tempo indeterminato da quello a tempo determinato
--
-- Body:
--
 esercizio,
 mese,
 cd_cdr_root,
 cd_cdr,
 ti_appartenenza,
 ti_gestione,
 cd_elemento_voce,
 cd_linea_attivita,
 ti_rapporto,
 ti_prev_cons,
 sum(im_a1),
 sum(im_a2),
 sum(im_a3),
 sum(im_a1)-round(sum(im_a1),2),
 sum(im_a2)-round(sum(im_a2),2),
 sum(im_a3)-round(sum(im_a3),2)
from (
 select
  a.esercizio,
  a.mese,
  a.cd_cdr_root,
  a.cd_cdr,
  p.ti_appartenenza,
  p.ti_gestione,
  p.cd_elemento_voce,
  a.cd_linea_attivita,
  p.ti_rapporto,
  p.ti_prev_cons,
  p.im_a1*a.prc_a1 im_a1,
  decode(p.ti_rapporto,'IND',p.im_a2*a.prc_a2,0) im_a2,
  decode(p.ti_rapporto,'IND',p.im_a3*a.prc_a3,0) im_a3
 from costo_del_dipendente p, V_CDP_SPACCATO_CDR_LA_MATR a where
          p.esercizio = a.esercizio
	  and p.ti_prev_cons = 'P'
	  and p.id_matricola = a.id_matricola
	  and p.mese = a.mese
union all
 select
  a.esercizio,
  a.mese,
  a.cd_cdr_root,
  a.cd_cdr,
  p.ti_appartenenza,
  p.ti_gestione,
  c.val01,
  a.cd_linea_attivita,
  p.ti_rapporto,
  p.ti_prev_cons,
  p.im_oneri_cnr_a1*a.prc_a1 im_a1,
  decode(p.ti_rapporto,'IND',p.im_oneri_cnr_a2*a.prc_a2,0) im_a2,
  decode(p.ti_rapporto,'IND',p.im_oneri_cnr_a3*a.prc_a3,0) im_a3
 from costo_del_dipendente p, V_CDP_SPACCATO_CDR_LA_MATR a, configurazione_cnr c
 Where p.esercizio = a.esercizio
   And p.ti_prev_cons = 'P'
   And p.id_matricola = a.id_matricola
   And p.mese = a.mese
   And c.esercizio = a.esercizio
   And c.cd_chiave_primaria = 'ELEMENTO_VOCE_SPECIALE'
   And ((c.cd_chiave_secondaria = 'ONERI_CNR' And p.ti_rapporto = 'IND')
         Or
        (c.cd_chiave_secondaria = 'ONERI_CNR_TEMPO_DET' And p.ti_rapporto = 'DET'))
union all
 select
  a.esercizio,
  a.mese,
  a.cd_cdr_root,
  a.cd_cdr,
  p.ti_appartenenza,
  p.ti_gestione,
  c.val01,
  a.cd_linea_attivita,
  p.ti_rapporto,
  p.ti_prev_cons,
  p.im_tfr_a1*a.prc_a1 im_a1,
  p.im_tfr_a2*a.prc_a2 im_a2,
  p.im_tfr_a3*a.prc_a3 im_a3
 from costo_del_dipendente p, V_CDP_SPACCATO_CDR_LA_MATR a, configurazione_cnr c
 Where p.esercizio = a.esercizio
   And a.mese = p.mese
   And p.ti_prev_cons = 'P'
   And p.id_matricola = a.id_matricola
   And c.esercizio = a.esercizio
   And c.cd_chiave_primaria = 'ELEMENTO_VOCE_SPECIALE'
   And ((c.cd_chiave_secondaria = 'TFR' And p.ti_rapporto = 'IND')
         Or
        (c.cd_chiave_secondaria = 'TFR_TEMPO_DET' And p.ti_rapporto = 'DET'))
) group by
   esercizio,
   mese,
   cd_cdr_root,
   cd_cdr,
   ti_appartenenza,
   ti_gestione,
   cd_elemento_voce,
   cd_linea_attivita,
   ti_rapporto,
   ti_prev_cons;
