--------------------------------------------------------
--  DDL for View V_INCARICHI_ASS_RIC_BORSE_ST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INCARICHI_ASS_RIC_BORSE_ST" ("ESERCIZIO", "PG_REPERTORIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "STATO", "ESERCIZIO_PROCEDURA", "PG_PROCEDURA", "CD_TERZO", "COGNOME", "NOME", "CODICE_FISCALE", "DT_REGISTRAZIONE", "DT_CANCELLAZIONE", "DT_STIPULA", "DT_INIZIO_VALIDITA", "DT_FINE_VALIDITA", "DT_PROROGA", "DT_PROROGA_PAGAM", "TI_ISTITUZ_COMMERC", "CD_TIPO_RAPPORTO", "CD_TRATTAMENTO", "FL_PUBBLICA_CONTRATTO", "IMPORTO_LORDO", "IMPORTO_COMPLESSIVO", "UTCR", "DACR", "UTUV", "DUVA", "PG_VER_REC", "FL_INVIATO_CORTE_CONTI", "DT_INVIO_CORTE_CONTI", "ESITO_CORTE_CONTI", "CD_PROVV", "NR_PROVV", "DT_PROVV", "DT_EFFICACIA", "OGGETTO", "TIPO_NATURA", "TIPO_ASSOCIAZIONE") AS 
  SELECT inc.esercizio, inc.pg_repertorio, inc.cd_cds,
          inc.cd_unita_organizzativa, inc.stato,
          proc.esercizio esercizio_procedura, proc.pg_procedura, inc.cd_terzo,
          anag.cognome, anag.nome, anag.codice_fiscale, inc.dt_registrazione,
          inc.dt_cancellazione, inc.dt_stipula, inc.dt_inizio_validita,
          inc.dt_fine_validita, inc.dt_proroga, inc.dt_proroga_pagam,
          inc.ti_istituz_commerc, tipinc.cd_tipo_rapporto, inc.cd_trattamento,
          inc.fl_pubblica_contratto, inc.importo_lordo,
          inc.importo_complessivo, inc.utcr, inc.dacr, inc.utuv, inc.duva,
          inc.pg_ver_rec, inc.fl_inviato_corte_conti,
          inc.dt_invio_corte_conti, inc.esito_corte_conti, inc.cd_provv,
          inc.nr_provv, inc.dt_provv, inc.dt_efficacia, proc.oggetto,
          proc.tipo_natura, tipinc.tipo_associazione
     FROM incarichi_repertorio inc,
          incarichi_procedura proc,
          tipo_incarico tipinc,
          terzo,
          anagrafico anag
    WHERE inc.esercizio_procedura = proc.esercizio
      AND inc.pg_procedura = proc.pg_procedura
      AND tipinc.cd_tipo_incarico = proc.cd_tipo_incarico
      AND inc.cd_terzo = terzo.cd_terzo
      AND terzo.cd_anag = anag.cd_anag ;
