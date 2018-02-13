--------------------------------------------------------
--  DDL for View PRT_CUP_MANDATO_GAE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_CUP_MANDATO_GAE" ("CD_CDS", "UO", "ESERCIZIO", "PG_MANDATO", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DS_OBBLIGAZIONE", "CD_TERZO", "DENOMINAZIONE", "CD_CUP", "DS_CUP", "IMPORTO", "DT_EMISSIONE", "DT_PAGAMENTO", "DT_TRASMISSIONE") AS 
  SELECT   m.cd_cds_origine, m.cd_uo_origine, mc.esercizio, mc.pg_mandato,
            mc.esercizio_obbligazione, mc.esercizio_ori_obbligazione,
            mc.pg_obbligazione, mc.pg_obbligazione_scadenzario,obbligazione.ds_obbligazione,terzo.cd_terzo,terzo.denominazione_sede,
            mc.cd_cup, c.descrizione, SUM (MC.IMPORTO),
            TRUNC (m.dt_emissione), TRUNC (m.dt_pagamento),
            TRUNC (m.dt_trasmissione)
       FROM mandato m, mandato_cup mc, cup c,obbligazione,obbligazione_scadenzario,terzo,mandato_riga
      WHERE m.cd_cds = mc.cd_cds
        AND m.esercizio = mc.esercizio
        AND m.pg_mandato = mc.pg_mandato
        AND m.stato != 'A'
        AND mc.cd_cup = c.cd_cup
        and obbligazione.cd_cds                 = obbligazione_scadenzario.cd_cds
        AND obbligazione.esercizio              = obbligazione_scadenzario.esercizio
        AND obbligazione.esercizio_originale    = obbligazione_scadenzario.esercizio_originale
        AND obbligazione.pg_obbligazione        = obbligazione_scadenzario.pg_obbligazione
        AND obbligazione_scadenzario.cd_cds 		= mc.cd_cds
        AND obbligazione_scadenzario.esercizio = mc.esercizio_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione 				=  mc.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =  mc.pg_obbligazione_scadenzario
        AND obbligazione_scadenzario.ESERCIZIO_ORIGINALE  =   mc.esercizio_ori_obbligazione
        and m.cd_cds = mandato_riga.cd_cds
        AND m.esercizio = mandato_riga.esercizio
        AND m.pg_mandato = mandato_riga.pg_mandato
        AND obbligazione_scadenzario.cd_cds 		= mandato_riga.cd_cds
        AND obbligazione_scadenzario.esercizio = mandato_riga.esercizio_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione 				=  mandato_riga.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =  mandato_riga.pg_obbligazione_scadenzario
        AND obbligazione_scadenzario.ESERCIZIO_ORIGINALE  =   mandato_riga.esercizio_ori_obbligazione
        and terzo.cd_terzo = mandato_riga.cd_terzo
   GROUP BY  m.cd_cds_origine, m.cd_uo_origine, mc.esercizio, mc.pg_mandato,
            mc.esercizio_obbligazione, mc.esercizio_ori_obbligazione,
            mc.pg_obbligazione, mc.pg_obbligazione_scadenzario,obbligazione.ds_obbligazione,terzo.cd_terzo,terzo.denominazione_sede,
            mc.cd_cup, c.descrizione,
            TRUNC (m.dt_emissione), TRUNC (m.dt_pagamento),
            TRUNC (m.dt_trasmissione)
   UNION
   -- Nuova tabella associazione Cup
   SELECT   m.cd_cds_origine, m.cd_uo_origine, mc.esercizio, mc.pg_mandato,
            mc.esercizio_obbligazione, mc.esercizio_ori_obbligazione,
            mc.pg_obbligazione, mc.pg_obbligazione_scadenzario,obbligazione.ds_obbligazione,terzo.cd_terzo,terzo.denominazione_sede,
            mc.cd_cup, c.descrizione, SUM (MC.IMPORTO),
            TRUNC (m.dt_emissione), TRUNC (m.dt_pagamento),
            TRUNC (m.dt_trasmissione)
       FROM mandato m, mandato_siope_cup mc, cup c,obbligazione,obbligazione_scadenzario,terzo,mandato_riga
      WHERE m.cd_cds = mc.cd_cds
        AND m.esercizio = mc.esercizio
        AND m.pg_mandato = mc.pg_mandato
        AND m.stato != 'A'
        AND mc.cd_cup = c.cd_cup
        and obbligazione.cd_cds                 = obbligazione_scadenzario.cd_cds
        AND obbligazione.esercizio              = obbligazione_scadenzario.esercizio
        AND obbligazione.esercizio_originale    = obbligazione_scadenzario.esercizio_originale
        AND obbligazione.pg_obbligazione        = obbligazione_scadenzario.pg_obbligazione
        AND obbligazione_scadenzario.cd_cds 		= mc.cd_cds
        AND obbligazione_scadenzario.esercizio = mc.esercizio_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione 				=  mc.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =  mc.pg_obbligazione_scadenzario
        AND obbligazione_scadenzario.ESERCIZIO_ORIGINALE  =   mc.esercizio_ori_obbligazione
        and m.cd_cds = mandato_riga.cd_cds
        AND m.esercizio = mandato_riga.esercizio
        AND m.pg_mandato = mandato_riga.pg_mandato
        AND obbligazione_scadenzario.cd_cds 		= mandato_riga.cd_cds
        AND obbligazione_scadenzario.esercizio = mandato_riga.esercizio_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione 				=  mandato_riga.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =  mandato_riga.pg_obbligazione_scadenzario
        AND obbligazione_scadenzario.ESERCIZIO_ORIGINALE  =   mandato_riga.esercizio_ori_obbligazione
        and terzo.cd_terzo = mandato_riga.cd_terzo
   GROUP BY  m.cd_cds_origine, m.cd_uo_origine, mc.esercizio, mc.pg_mandato,
            mc.esercizio_obbligazione, mc.esercizio_ori_obbligazione,
            mc.pg_obbligazione, mc.pg_obbligazione_scadenzario,obbligazione.ds_obbligazione,terzo.cd_terzo,terzo.denominazione_sede,
            mc.cd_cup, c.descrizione,
            TRUNC (m.dt_emissione), TRUNC (m.dt_pagamento),
            TRUNC (m.dt_trasmissione);
