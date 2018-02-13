--------------------------------------------------------
--  DDL for View DIP_DA_CONG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "DIP_DA_CONG" ("TERZO", "MATRICOLA", "TI_ENTE_PERCIPIENTE", "CD_CONTRIBUTO_RITENUTA", "IMPONIBILE", "AMMONTARE") AS 
  select
  a.CD_TERZO,
  r.matricola_dipendente,
  b.TI_ENTE_PERCIPIENTE,
  b.cd_contributo_ritenuta,
  --e.CD_GRUPPO_CR,
  --e.CD_REGIONE,
  --e.PG_COMUNE,
  sum(b.imponibile),
  sum(b.AMMONTARE)
from compenso a, contributo_ritenuta b, tipo_contributo_ritenuta cr,/* tipo_cr_base c, gruppo_cr_det e,*/
     v_dt_manrev_comp dtm,terzo t, rapporto r
where
  a.stato_cofi = 'P'
  and b.cd_cds = a.cd_cds
 and b.esercizio = a.esercizio
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.pg_compenso =a.pg_compenso
 and cr.cd_contributo_ritenuta = b.cd_contributo_ritenuta
 and cr.dt_ini_validita = b.dt_ini_validita
 --and c.cd_contributo_ritenuta = b.cd_contributo_ritenuta
 --and e.esercizio = c.esercizio
 --and e.cd_gruppo_cr =c.cd_gruppo_cr
 -- Estrazione del corretto indice di regione/comune con cui entrare in GRUPPO_CR_DET sull base della classificazione del CORI
 --and e.cd_regione = getCdRegioneLiquidCori(a.fl_compenso_stipendi,b.cd_contributo_ritenuta,b.ti_ente_percipiente,cr.cd_classificazione_cori, c.esercizio, c.cd_gruppo_cr, a.cd_regione_add,a.cd_regione_irap)
 --and e.pg_comune = getPgComuneLiquidCori(cr.cd_classificazione_cori, c.esercizio, c.cd_gruppo_cr, a.pg_comune_add)
 and (
     b.esercizio_accertamento is not null
  or b.esercizio_obbligazione is not null
 )
 and dtm.CD_UO_COMPENSO = a.CD_UNITA_ORGANIZZATIVA
 and dtm.CD_CDS_COMPENSO = a.CD_CDS
 and dtm.ESERCIZIO_COMPENSO = a.ESERCIZIO
 and dtm.PG_COMPENSO = a.PG_COMPENSO
 and exists (select 1 from tipo_rapporto where
         cd_tipo_rapporto = a.cd_tipo_rapporto
  and ti_dipendente_altro = 'D')
 and dtm.DT_TRASMISSIONE between to_date('01/01/2007','dd/mm/yyyy') and to_date('31/12/2007','dd/mm/yyyy')
 And a.cd_terzo = t.cd_terzo
 And t.cd_anag = r.cd_anag
 --And (R.CAUSALE_FINE_RAPPORTO Is Null Or (A.DT_DA_COMPETENZA_COGE >= R.DT_INI_VALIDITA
   --                                        And A.DT_A_COMPETENZA_COGE <= R.DT_FIN_VALIDITA))
 And A.DT_DA_COMPETENZA_COGE >= R.DT_INI_VALIDITA
 And A.DT_A_COMPETENZA_COGE <= R.DT_FIN_VALIDITA
 And R.CD_TIPO_RAPPORTO = 'DIP'
Group by   a.CD_TERZO,
  r.matricola_dipendente,
  b.TI_ENTE_PERCIPIENTE,
  b.cd_contributo_ritenuta;
