--------------------------------------------------------
--  DDL for View V_CONS_STATO_INVIO_REVERSALI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_STATO_INVIO_REVERSALI" (
    "CD_CDS",
    "ESERCIZIO",
    "PG_REVERSALE",
    "CD_UNITA_ORGANIZZATIVA",
    "TI_REVERSALE",
    "DS_REVERSALE",
    "STATO",
    "IM_REVERSALE",
    "IM_INCASSATO",
    "DT_EMIS_REV",
    "DT_ANNULLAMENTO",
    "DT_INCASSO",
    "PG_DISTINTA",
    "PG_DISTINTA_DEF",
    "DT_EMIS_DIS",
    "DT_INVIO_DIS",
    "ESITO_OPERAZIONE",
    "DT_ORA_ESITO_OPERAZIONE",
    "ERRORE_SIOPE_PLUS"
    ) AS
  Select reversale.cd_cds, reversale.esercizio, reversale.pg_reversale,
          reversale.cd_unita_organizzativa, reversale.ti_reversale,
          reversale.ds_reversale, reversale.stato, reversale.im_reversale,
          reversale.im_incassato, reversale.dt_emissione,
          reversale.dt_annullamento, reversale.dt_incasso,NULL pg_distinta, NULL pg_distinta_def, NULL dt_emis_dis,
          NULL dt_invio_dis,esito_operazione,dt_ora_esito_operazione,errore_siope_plus
    From  reversale
    Where Not Exists (Select 1
                      From   distinta_cassiere_det
                      Where  cd_cds_origine       = reversale.cd_cds And
                             esercizio    = reversale.esercizio And
                             pg_reversale = reversale.pg_reversale)
   Union
-- MANDATI IN DISTINTA
   Select reversale.cd_cds, reversale.esercizio, reversale.pg_reversale,
          reversale.cd_unita_organizzativa, reversale.ti_reversale,
          reversale.ds_reversale, reversale.stato, reversale.im_reversale,
          reversale.im_incassato, reversale.dt_emissione,
          reversale.dt_annullamento, reversale.dt_incasso,distinta_cassiere.pg_distinta, distinta_cassiere.pg_distinta_def,
          distinta_cassiere.dt_emissione dt_emis_dis,
          distinta_cassiere.dt_invio dt_invio_dis,esito_operazione,dt_ora_esito_operazione,errore_siope_plus
   From   reversale, distinta_cassiere, distinta_cassiere_det
   Where  reversale.cd_cds = distinta_cassiere_det.cd_cds_origine And
          reversale.esercizio = distinta_cassiere_det.esercizio And
          reversale.pg_reversale = distinta_cassiere_det.pg_reversale And
          distinta_cassiere.cd_cds = distinta_cassiere_det.cd_cds And
          distinta_cassiere.esercizio = distinta_cassiere_det.esercizio And
          distinta_cassiere.cd_unita_organizzativa = distinta_cassiere_det.cd_unita_organizzativa And
          distinta_cassiere.pg_distinta = distinta_cassiere_det.pg_distinta;
