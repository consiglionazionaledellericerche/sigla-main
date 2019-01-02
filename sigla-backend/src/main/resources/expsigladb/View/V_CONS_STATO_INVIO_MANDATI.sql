--------------------------------------------------------
--  DDL for View V_CONS_STATO_INVIO_MANDATI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_STATO_INVIO_MANDATI" (
    "CD_CDS",
    "ESERCIZIO",
    "PG_MANDATO",
    "CD_UNITA_ORGANIZZATIVA",
    "TI_MANDATO",
    "DS_MANDATO",
    "STATO",
    "IM_MANDATO",
    "IM_RITENUTE",
    "IM_NETTO",
    "IM_PAGATO",
    "DT_EMIS_MAN",
    "DT_ANNULLAMENTO",
    "DT_PAGAMENTO",
    "PG_DISTINTA",
    "PG_DISTINTA_DEF",
    "DT_EMIS_DIS",
    "DT_INVIO_DIS",
    "CD_MODALITA_PAG",
    "ESITO_OPERAZIONE",
    "DT_ORA_ESITO_OPERAZIONE",
    "ERRORE_SIOPE_PLUS"
  ) AS
  Select mandato.cd_cds, mandato.esercizio, mandato.pg_mandato,
          mandato.cd_unita_organizzativa, mandato.ti_mandato,
          mandato.ds_mandato, mandato.stato, mandato.im_mandato,
          mandato.im_ritenute,
          NVL (mandato.im_mandato, 0) - NVL (mandato.im_ritenute, 0) im_netto,
          mandato.im_pagato, mandato.dt_emissione, mandato.dt_annullamento,
          mandato.dt_pagamento, NULL pg_distinta, NULL pg_distinta_def, NULL dt_emis_dis,
          NULL dt_invio_dis,(select decode(max(cd_modalita_pag),min(cd_modalita_pag),max(cd_modalita_pag),null) from mandato_riga
          where   mandato_riga.cd_cds   = mandato.cd_cds And
                             mandato_riga.esercizio  = mandato.esercizio And
                             mandato_riga.pg_mandato = mandato.pg_mandato) cd_modalita_pag,esito_operazione,dt_ora_esito_operazione,errore_siope_plus
    From  mandato
    Where Not Exists (Select 1
                      From   distinta_cassiere_det
                      Where  cd_cds_origine     = mandato.cd_cds And
                             esercizio  = mandato.esercizio And
                             pg_mandato = mandato.pg_mandato)
   Union
-- MANDATI IN DISTINTA
   Select mandato.cd_cds, mandato.esercizio, mandato.pg_mandato,
          mandato.cd_unita_organizzativa, mandato.ti_mandato,
          mandato.ds_mandato, mandato.stato, mandato.im_mandato,
          mandato.im_ritenute,
          NVL (mandato.im_mandato, 0) - NVL (mandato.im_ritenute, 0) im_netto,
          mandato.im_pagato, mandato.dt_emissione, mandato.dt_annullamento,
          mandato.dt_pagamento, distinta_cassiere.pg_distinta, distinta_cassiere.pg_distinta_def,
          distinta_cassiere.dt_emissione dt_emis_dis,
          distinta_cassiere.dt_invio dt_invio_dis,(select decode(max(cd_modalita_pag),min(cd_modalita_pag),max(cd_modalita_pag),null) from mandato_riga
          where   mandato_riga.cd_cds   = mandato.cd_cds And
                             mandato_riga.esercizio  = mandato.esercizio And
                             mandato_riga.pg_mandato = mandato.pg_mandato) cd_modalita_pag,esito_operazione,dt_ora_esito_operazione,errore_siope_plus
   From   mandato, distinta_cassiere, distinta_cassiere_det
   Where  mandato.cd_cds = distinta_cassiere_det.cd_cds_origine And
          mandato.esercizio = distinta_cassiere_det.esercizio And
          mandato.pg_mandato = distinta_cassiere_det.pg_mandato And
          distinta_cassiere.cd_cds = distinta_cassiere_det.cd_cds And
          distinta_cassiere.esercizio = distinta_cassiere_det.esercizio And
          distinta_cassiere.cd_unita_organizzativa = distinta_cassiere_det.cd_unita_organizzativa And
          distinta_cassiere.pg_distinta = distinta_cassiere_det.pg_distinta;
