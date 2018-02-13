--------------------------------------------------------
--  DDL for View V_CDP_SPACCATO_CDR_LA_MATR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_SPACCATO_CDR_LA_MATR" ("ESERCIZIO", "MESE", "CD_CDR_ROOT", "CD_CDR", "CD_LINEA_ATTIVITA", "ID_MATRICOLA", "STATO", "PRC_A1", "PRC_A2", "PRC_A3") AS 
  select
--
-- Date: 23/09/2002
-- Version: 1.0
--
-- Vista di estrazione dello spaccato mensile (pdg se mese = 0) per matricola/la/cdr
-- La vista ritorna percentuali riferite all'unità scalate nel caso la ripartizione derivi da scarico
-- di costi dipendente da altra UO
--
-- History:
-- Date: 23/09/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
  a.esercizio,
  a.mese,
  c.cd_cdr_root,
  c.cd_centro_responsabilita,
  a.cd_linea_attivita,
  a.id_matricola,
  a.stato,
  (b.prc_uo_a1/100)*(a.prc_la_a1/100) im_a1,
  (b.prc_uo_a2/100)*(a.prc_la_a2/100) im_a2,
  (b.prc_uo_a3/100)*(a.prc_la_a3/100) im_a3
 from ass_cdp_la a, ass_cdp_uo b, V_PDG_CDR_RUO_NRUO c where
	      b.id_matricola = a.id_matricola
      and b.esercizio =a.esercizio
	  and a.mese = b.mese
	  and b.cd_unita_organizzativa = c.cd_unita_organizzativa
	  and a.esercizio = c.esercizio
	  and a.cd_centro_responsabilita = c.cd_centro_responsabilita
	  and a.fl_dip_altra_uo = 'Y'
union all
 select
  a.esercizio,
  a.mese,
  c.cd_cdr_root,
  c.cd_centro_responsabilita,
  a.cd_linea_attivita,
  a.id_matricola,
  a.stato,
  a.prc_la_a1/100 im_a1,
  a.prc_la_a2/100 im_a2,
  a.prc_la_a3/100 im_a3
 from ass_cdp_la a, V_PDG_CDR_RUO_NRUO c where
	      c.esercizio = a.esercizio
	  and c.cd_centro_responsabilita = a.cd_centro_responsabilita
	  and a.fl_dip_altra_uo = 'N'
;

   COMMENT ON TABLE "V_CDP_SPACCATO_CDR_LA_MATR"  IS 'Vista di estrazione dello spaccato mensile (pdg se mese = 0) per matricola/la/cdr
La vista ritorna percentuali riferite all''unità scalate nel caso la ripartizione derivi da scarico
di costi dipendente da altra UO';
