--------------------------------------------------------
--  DDL for View V_LIQUID_CORI_MANCANTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_CORI_MANCANTI" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO_COMPENSO", "PG_COMPENSO", "CD_CONTRIBUTO_RITENUTA", "CD_GRUPPO_CR", "CD_REGIONE", "PG_COMUNE", "DT_MANDATO") AS 
  Select
--
-- View di estrazione U.O. non SAC che non hanno effettuoto la liquidazione CORI
--
-- History:
--
-- Date: 26/10/2005
-- Version: 1.0
-- Creazione vista
--
-- Body:
--
  a.CD_CDS,
  a.CD_UNITA_ORGANIZZATIVA,
  a.ESERCIZIO,
  a.PG_COMPENSO,
  b.CD_CONTRIBUTO_RITENUTA,
  e.CD_GRUPPO_CR,
  e.CD_REGIONE,
  e.PG_COMUNE,
  Decode(CNRCTB575.ISLIQUIDACORIINVIATO(a.Esercizio),'Y', dtm.DT_TRASMISSIONE, a.DT_EMISSIONE_MANDATO)
From compenso a, contributo_ritenuta b, tipo_contributo_ritenuta cr,
     tipo_cr_base c, gruppo_cr_det e, v_dt_manrev_comp dtm
where a.stato_cofi = 'P'
 And b.cd_cds = a.cd_cds
 and b.esercizio = a.esercizio
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.pg_compenso =a.pg_compenso
 and cr.cd_contributo_ritenuta = b.cd_contributo_ritenuta
 and cr.dt_ini_validita = b.dt_ini_validita
 and c.cd_contributo_ritenuta = b.cd_contributo_ritenuta
 And c.esercizio = b.esercizio
 and e.esercizio = c.esercizio
 and e.cd_gruppo_cr =c.cd_gruppo_cr
 and ((a.fl_split_payment ='Y' and cr.cd_classificazione_cori!='IV') or nvl(a.fl_split_payment,'N')='N')
 -- Estrazione del corretto indice di regione/comune con cui entrare in GRUPPO_CR_DET sull base della classificazione del CORI
 and e.cd_regione = getCdRegioneLiquidCori(a.fl_compenso_stipendi,b.cd_contributo_ritenuta,b.ti_ente_percipiente,cr.cd_classificazione_cori, c.esercizio, c.cd_gruppo_cr, a.cd_regione_add,a.cd_regione_irap)
 and e.pg_comune = getPgComuneLiquidCori(cr.cd_classificazione_cori, c.esercizio, c.cd_gruppo_cr, a.pg_comune_add)
 and (b.esercizio_accertamento is not null
      Or b.esercizio_obbligazione is not Null)
 and dtm.CD_UO_COMPENSO = a.CD_UNITA_ORGANIZZATIVA
 and dtm.CD_CDS_COMPENSO = a.CD_CDS
 and dtm.ESERCIZIO_COMPENSO = a.ESERCIZIO
 and dtm.PG_COMPENSO = a.PG_COMPENSO
 And a.CD_CDS In (Select cd_unita_organizzativa
                  From unita_organizzativa
                  Where fl_cds = 'Y'
                    And cd_tipo_unita != 'SAC')
  And Not Exists (Select ESERCIZIO_CONTRIBUTO_RITENUTA, PG_COMPENSO, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_CONTRIBUTO_RITENUTA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE
                 From liquid_gruppo_cori_det G
                 Where G.esercizio_contributo_ritenuta = a.esercizio
                   And G.cd_cds_origine = a.cd_cds
                   And G.cd_uo_origine = a.cd_unita_organizzativa
                And G.cd_contributo_ritenuta = b.cd_contributo_ritenuta
     And G.pg_compenso = a.pg_compenso
     And G.ti_ente_percipiente = b.ti_ente_percipiente
     And G.cd_gruppo_cr = c.cd_gruppo_cr
     And G.cd_regione = e.cd_regione
     And G.pg_comune = e.pg_comune);
