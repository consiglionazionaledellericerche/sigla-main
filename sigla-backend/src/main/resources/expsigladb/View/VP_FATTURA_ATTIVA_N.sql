--------------------------------------------------------
--  DDL for View VP_FATTURA_ATTIVA_N
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_FATTURA_ATTIVA_N" ("ID_REPORT", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_FATTURA_ATTIVA", "TI_RECORD", "TI_STAMPA", "PROGRESSIVO_RIGA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "DS_UO_ORIGINE", "TI_FATTURA", "DT_REGISTRAZIONE", "DT_EMISSIONE", "PROTOCOLLO_IVA", "DS_FATTURA_ATTIVA", "DENOMINAZIONE_SEDE", "VIA_SEDE", "NUMERO_CIVICO_SEDE", "CAP_COMUNE_SEDE", "DS_COMUNE_SEDE", "FRAZIONE_SEDE", "CD_PROVINCIA_SEDE", "CODICE_FISCALE", "PARTITA_IVA", "IM_TOTALE_FATTURA", "IM_TOTALE_IMPONIBILE", "IM_TOTALE_IVA", "RIFERIMENTO_ORDINE", "CODICE_FISCALE_ENTE", "PARTITA_IVA_ENTE", "VIA_ENTE", "NUM_CIVICO_ENTE", "CAP_COMUNE_ENTE", "DS_COMUNE_ENTE", "CD_PROVINCIA_ENTE", "VIA_UO", "NUM_CIVICO_UO", "CAP_COMUNE_UO", "DS_COMUNE_UO", "CD_PROVINCIA_UO", "IBAN", "NUMERO_CONTO", "CIN", "INTESTAZIONE", "ABI", "CAB", "DS_ABICAB", "DS_COMUNE_BANCA", "CD_PROVINCIA_BANCA", "DS_RIGA_FATTURA", "PREZZO_UNITARIO", "QUANTITA", "IM_TOTALE_DIVISA", "CD_VOCE_IVA", "IM_IVA", "DS_VOCE_IVA", "PERCENTUALE", "FL_NON_IMPONIBILE", "FL_ESENTE", "FL_NON_SOGGETTO", "FL_ESCLUSO", "IM_IMPONIBILE_V_IVA", "IM_TOTALE_V_IVA", "TI_ITALIANO_ESTERO", "TI_BOLLO") AS 
  (SELECT
--
-- Date: 06/03/2008
-- Version: 1.10
--
-- Vista per la stampa della fattura attiva, nota di
-- credito e di debito su fattura attiva
--
-- History:
--
-- Date: 09/10/02
-- Version: 1.0
-- Creazione
--
-- Date: 28/11/02
-- Version: 1.1
-- Estratto TI_FATTURA per tutti i tipi di record
-- Aggiunto TI_STAMPA per identificare stampe/ristampe
-- Duplicazione dei dati per gestione numero copie in stampa
-- Costruzione della vista su V_STM_PARAMIN_FT_ATTIVA
--
-- Date: 04/12/02
-- Version: 1.2
-- Estrazione indirizzo del terzo
-- Estrazione intestazione su tutti i record per layout
--
-- Date: 17/12/02
-- Version: 1.3
-- Sistemazione vista per performance
--
-- Date: 04/02/2003
-- Version: 1.4
-- Estrazione cd_uo_origine a tutti i livelli di record
--
-- Date: 05/02/2003
-- Version: 1.5
-- Estrazione protocollo_iva, dt_emissione fattura attiva
--
-- Date: 25/02/2003
-- Version: 1.6
-- Richiesta 498, parte a): indirizzo uo emittente nel terzo
--
-- Date: 03/10/2003
-- Version: 1.7
-- Estrazione denominazione del terzo e non dell'anagrafica
-- Richiesta n. 637
--
-- Date: 21/01/2004
-- Version: 1.8
-- Estrazione CIN dalla BANCA (richiesta n. 697)
--
-- Date: 21/01/2004
-- Version: 1.9
-- Estrazione frazione_sede (richiesta n. 735)
--
-- Date: 06/03/2008
-- Version: 1.10
-- Modifica: aggiunto il campo TI_ITALIANO_ESTERO utile alla visualizzazione del codice IT
-- sulle fatture per committenti o cessionari esteri.
--
-- Body:
--
           vpar.id_report, vpar.cd_cds, vpar.cd_uo, vpar.esercizio,
           vpar.pg_fattura_attiva, 'A'                              -- testata
                                      ,
           'S'                                                       -- stampa
              ,
           0                                                --PROGRESSIVO_RIGA
            ,
           fa.cd_cds_origine, fa.cd_uo_origine                 -- uo emittente
                                              ,
           uo.ds_unita_organizzativa                        -- ds uo emittente
                                    ,
           fa.ti_fattura, fa.dt_registrazione, fa.dt_emissione,
           fa.protocollo_iva, fa.ds_fattura_attiva
-- ,fa.RAGIONE_SOCIALE
-- ,fa.NOME
-- ,fa.COGNOME
           , ter1.denominazione_sede, ter1.via_sede, ter1.numero_civico_sede,
           ter1.cap_comune_sede, com1.ds_comune, decode ( com1.ti_italiano_estero,'I',ter1.frazione_sede, ter1.frazione_sede||' '||n.ds_nazione) frazione_sede,
           com1.cd_provincia, fa.codice_fiscale, decode(fa.partita_iva,null,null,decode(fa.fl_intra_ue,'Y',n.cd_iso||' '||fa.partita_iva,fa.partita_iva)),
           fa.im_totale_fattura, fa.im_totale_imponibile, fa.im_totale_iva,
           fa.riferimento_ordine, ana.codice_fiscale                   -- ente
                                                    ,
           ana.partita_iva                                      -- ente
                          ,
           ana.via_fiscale                                             -- ente
                          ,
           ana.num_civico_fiscale                                      -- ente
                                 ,
           ana.cap_comune_fiscale                                      -- ente
                                 ,
           com.ds_comune                             -- DS_COMUNE_FISCALE ente
                        ,
           com.cd_provincia                       -- CD_PROVINCIA_FISCALE ente
                           ,
           ter2.via_sede                                       -- uo emittente
                        ,
           ter2.numero_civico_sede                             -- uo emittente
                                  ,
           ter2.cap_comune_sede                                -- uo emittente
                               ,
           com2.ds_comune                                      -- uo emittente
                         ,
           com2.cd_provincia                                   -- uo emittente
                            ,
           NULL                                        --,ban.codice_iban ente
               ,
           NULL                                            --NUMERO_CONTO ente
               ,
           NULL                                                    -- CIN ente
               ,
           NULL                                            --INTESTAZIONE ente
               ,
           NULL                                                     --ABI ente
               ,
           NULL                                                     --CAB ente
               ,
           NULL                                               --DS_ABICAB ente
               ,
           NULL                                         --DS_COMUNE_BANCA ente
               ,
           NULL                                      --CD_PROVINCIA_BANCA ente
               ,
           NULL                                              --DS_RIGA_FATTURA
               ,
           0                                                 --PREZZO_UNITARIO
            ,
           0                                                        --QUANTITA
            ,
           0                                                --IM_TOTALE_DIVISA
            ,
           NULL                                                  --CD_VOCE_IVA
               ,
           0                                                          --IM_IVA
            ,
           NULL                                                  --DS_VOCE_IVA
               ,
           0                                                     --PERCENTUALE
            ,
           NULL                                            --FL_NON_IMPONIBILE
               ,
           NULL                                                    --FL_ESENTE
               ,
           NULL                                              --FL_NON_SOGGETTO
               ,
           NULL                                                   --FL_ESCLUSO
               ,
           0                               --totale imponibile per cd_voce_iva
            ,
           0                                      --totale iva per cd_voce_iva
            ,
           com1.ti_italiano_estero                         -- comune del terzo
           ,
           null                           -- ti_bollo
      FROM v_stm_paramin_ft_attiva vpar,
           fattura_attiva fa,
           unita_organizzativa uo,
           terzo ter1,
           comune com1,
           terzo ter,
           anagrafico ana,
           comune com,
           terzo ter2,
           comune com2,
           nazione n
     WHERE fa.cd_cds = vpar.cd_cds
       AND fa.cd_unita_organizzativa = vpar.cd_uo
       AND fa.esercizio = vpar.esercizio
       AND fa.pg_fattura_attiva = vpar.pg_fattura_attiva
       AND uo.cd_unita_organizzativa = fa.cd_uo_origine
       AND ter1.cd_terzo = fa.cd_terzo
       AND com1.pg_comune = ter1.pg_comune_sede
       AND ter.cd_unita_organizzativa = fa.cd_unita_organizzativa -- '999.000'
       AND ana.cd_anag = ter.cd_anag
       AND com.pg_comune = ana.pg_comune_fiscale
       AND ter2.cd_terzo = fa.cd_terzo_uo_cds
       AND com2.pg_comune = ter2.pg_comune_sede
       and n.pg_nazione = com1.pg_nazione
    UNION ALL
    SELECT vpar.id_report, vpar.cd_cds, vpar.cd_uo, vpar.esercizio,
           vpar.pg_fattura_attiva, 'B' ti_record                      -- righe
                                                ,
           'S'                                                       -- stampa
              ,
           frg.progressivo_riga                             --PROGRESSIVO_RIGA
                               ,
           NULL                                               --CD_CDS_ORIGINE
               ,
           fa.cd_uo_origine                                    -- uo emittente
                           ,
           uo.ds_unita_organizzativa, fa.ti_fattura, fa.dt_registrazione,
           fa.dt_emissione, fa.protocollo_iva, NULL        --DS_FATTURA_ATTIVA
-- ,null          --RAGIONE_SOCIALE
-- ,null          --NOME
-- ,null          --COGNOME
           ,
           NULL                                          -- DENOMINAZIONE_SEDE
               ,
           NULL                                                     --VIA_SEDE
               ,
           NULL                                           --NUMERO_CIVICO_SEDE
               ,
           NULL                                              --CAP_COMUNE_SEDE
               ,
           NULL                                                    --DS_COMUNE
               ,
           NULL                                                --frazione_sede
               ,
           NULL                                                 --CD_PROVINCIA
               ,
           NULL                                               --CODICE_FISCALE
               ,
           NULL                                                  --PARTITA_IVA
               ,
           0                                               --IM_TOTALE_FATTURA
            ,
           0                                            --IM_TOTALE_IMPONIBILE
            ,
           0                                                   --IM_TOTALE_IVA
            ,
           fa.riferimento_ordine, NULL    -- ana.CODICE_FISCALE        -- ente
                                      ,
           NULL                             -- ana.PARTITA_IVA         -- ente
               ,
           NULL                             -- ana.VIA_FISCALE         -- ente
               ,
           NULL                           -- ana.NUM_CIVICO_FISCALE    -- ente
               ,
           NULL                           -- ana.CAP_COMUNE_FISCALE    -- ente
               ,
           NULL                             -- com.DS_COMUNE           -- ente
               ,
           NULL                             -- com.CD_PROVINCIA        -- ente
               ,
           NULL                        -- ter2.VIA_SEDE        -- uo emittente
               ,
           NULL                    -- ter2.NUMERO_CIVICO_SEDE  -- uo emittente
               ,
           NULL                       -- ter2.CAP_COMUNE_SEDE  -- uo emittente
               ,
           NULL                     -- com1.DS_COMUNE          -- uo emittente
               ,
           NULL                  -- com1.CD_PROVINCIA          -- uo emittente
               ,
           NULL                                       -- ,ban.codice_iban ente
               ,
           NULL                                            --NUMERO_CONTO ente
               ,
           NULL                                                    -- CIN ente
               ,
           NULL                                            --INTESTAZIONE ente
               ,
           NULL                                                     --ABI ente
               ,
           NULL                                                     --CAB ente
               ,
           NULL                                               --DS_ABICAB ente
               ,
           NULL                                         --DS_COMUNE_BANCA ente
               ,
           NULL                                      --CD_PROVINCIA_BANCA ente
               ,
           frg.ds_riga_fattura, frg.prezzo_unitario, frg.quantita,
           frg.im_totale_divisa, frg.cd_voce_iva, frg.im_iva, vi.ds_voce_iva,
           vi.percentuale, vi.fl_non_imponibile, vi.fl_esente,
           vi.fl_non_soggetto, vi.fl_escluso,
           0                               --totale imponibile per cd_voce_iva
            ,
           0                                      --totale iva per cd_voce_iva
            ,
           NULL                                            -- comune del terzo
           ,
           null											-- tipo bollo voce iva
      FROM v_stm_paramin_ft_attiva vpar,
           fattura_attiva fa,
           fattura_attiva_riga frg,
           unita_organizzativa uo,
           voce_iva vi
     WHERE fa.cd_cds = vpar.cd_cds
       AND fa.cd_unita_organizzativa = vpar.cd_uo
       AND fa.esercizio = vpar.esercizio
       AND fa.pg_fattura_attiva = vpar.pg_fattura_attiva
       AND frg.cd_cds = fa.cd_cds
       AND frg.cd_unita_organizzativa = fa.cd_unita_organizzativa
       AND frg.esercizio = fa.esercizio
       AND frg.pg_fattura_attiva = fa.pg_fattura_attiva
       AND uo.cd_unita_organizzativa = fa.cd_uo_origine
       AND vi.cd_voce_iva = frg.cd_voce_iva
    UNION ALL
    SELECT   vpar.id_report, vpar.cd_cds, vpar.cd_uo, vpar.esercizio,
             vpar.pg_fattura_attiva,
             'C' ti_record
                    -- totali imponibile/non imponibile/esente/fuori campo iva
                          ,
             'S'                                                     -- stampa
                ,
             0                                              --PROGRESSIVO_RIGA
              ,
             NULL                                             --CD_CDS_ORIGINE
                 ,
             fa.cd_uo_origine                                  -- uo emittente
                             ,
             uo.ds_unita_organizzativa, fa.ti_fattura, fa.dt_registrazione,
             fa.dt_emissione, fa.protocollo_iva, NULL      --DS_FATTURA_ATTIVA
-- ,null          --RAGIONE_SOCIALE
-- ,null          --NOME
-- ,null          --COGNOME
             ,
             NULL                                        -- DENOMINAZIONE_SEDE
                 ,
             NULL                                                   --VIA_SEDE
                 ,
             NULL                                         --NUMERO_CIVICO_SEDE
                 ,
             NULL                                            --CAP_COMUNE_SEDE
                 ,
             NULL                                                  --DS_COMUNE
                 ,
             NULL                                              --frazione_sede
                 ,
             NULL                                               --CD_PROVINCIA
                 ,
             NULL                                             --CODICE_FISCALE
                 ,
             NULL                                                --PARTITA_IVA
                 ,
             0                                             --IM_TOTALE_FATTURA
              ,
             0                                          --IM_TOTALE_IMPONIBILE
              ,
             0                                                 --IM_TOTALE_IVA
              ,
             fa.riferimento_ordine, NULL    -- ana.CODICE_FISCALE      -- ente
                                        ,
             NULL                             -- ana.PARTITA_IVA       -- ente
                 ,
             NULL                             -- ana.VIA_FISCALE       -- ente
                 ,
             NULL                           -- ana.NUM_CIVICO_FISCALE  -- ente
                 ,
             NULL                           -- ana.CAP_COMUNE_FISCALE  -- ente
                 ,
             NULL                             -- com.DS_COMUNE         -- ente
                 ,
             NULL                             -- com.CD_PROVINCIA      -- ente
                 ,
             NULL                        -- ter2.VIA_SEDE      -- uo emittente
                 ,
             NULL                   -- ter2.NUMERO_CIVICO_SEDE -- uo emittente
                 ,
             NULL                      -- ter2.CAP_COMUNE_SEDE -- uo emittente
                 ,
             NULL                  -- com1.DS_COMUNE           -- uo emittente
                 ,
             NULL                  -- com1.CD_PROVINCIA        -- uo emittente
                 ,
             NULL                                      -- ban.codice_iban ente
                 ,
             NULL                                          --NUMERO_CONTO ente
                 ,
             NULL                                                  -- CIN ente
                 ,
             NULL                                          --INTESTAZIONE ente
                 ,
             NULL                                                   --ABI ente
                 ,
             NULL                                                   --CAB ente
                 ,
             NULL                                             --DS_ABICAB ente
                 ,
             NULL                                       --DS_COMUNE_BANCA ente
                 ,
             NULL                                    --CD_PROVINCIA_BANCA ente
                 ,
             NULL                                            --DS_RIGA_FATTURA
                 ,
             0                                               --PREZZO_UNITARIO
              ,
             0                                                      --QUANTITA
              ,
             0                                              --IM_TOTALE_DIVISA
              ,
             vi.cd_voce_iva, 0                                        --IM_IVA
                              ,
             vi.ds_voce_iva, vi.percentuale, vi.fl_non_imponibile,
             vi.fl_esente, vi.fl_non_soggetto, vi.fl_escluso,
             SUM (frg.im_imponibile)                      -- totale imponibile
                                    ,
             SUM (frg.im_iva)                                    -- totale iva
                             ,
             NULL                                          -- comune del terzo
           ,
           vi.ti_bollo											-- tipo bollo voce iva
        FROM v_stm_paramin_ft_attiva vpar,
             fattura_attiva fa,
             fattura_attiva_riga frg,
             unita_organizzativa uo,
             voce_iva vi
       WHERE fa.cd_cds = vpar.cd_cds
         AND fa.cd_unita_organizzativa = vpar.cd_uo
         AND fa.esercizio = vpar.esercizio
         AND fa.pg_fattura_attiva = vpar.pg_fattura_attiva
         AND frg.cd_cds = fa.cd_cds
         AND frg.cd_unita_organizzativa = fa.cd_unita_organizzativa
         AND frg.esercizio = fa.esercizio
         AND frg.pg_fattura_attiva = fa.pg_fattura_attiva
         AND uo.cd_unita_organizzativa = fa.cd_uo_origine
         AND vi.cd_voce_iva = frg.cd_voce_iva
    GROUP BY vpar.id_report,
             vpar.cd_cds,
             vpar.cd_uo,
             vpar.esercizio,
             vpar.pg_fattura_attiva,
             fa.cd_uo_origine,
             fa.ti_fattura,
             fa.dt_registrazione,
             fa.dt_emissione,
             fa.protocollo_iva,
             fa.riferimento_ordine,
             uo.ds_unita_organizzativa,
             vi.percentuale,
             vi.fl_non_imponibile,
             vi.fl_esente,
             vi.fl_non_soggetto,
             vi.fl_escluso,
             vi.ds_voce_iva,
             vi.cd_voce_iva,
             vi.ti_bollo
    UNION ALL
-- r.p. 12/03/2013 in realtà la select sotto non serve in questo momento avendo indicato le modalita fisse :( sulla stampa
-- ma la stampa si aspetta ti_record 'D'
    SELECT vpar.id_report, vpar.cd_cds, vpar.cd_uo, vpar.esercizio,
           vpar.pg_fattura_attiva, 'D'                        -- estremi conto
                                      ,
           'S'                                                       -- stampa
              ,
           0                                                --PROGRESSIVO_RIGA
            ,
           NULL                                               --CD_CDS_ORIGINE
               ,
           fa.cd_uo_origine                                    -- uo emittente
                           ,
           uo.ds_unita_organizzativa, fa.ti_fattura, fa.dt_registrazione,
           fa.dt_emissione, fa.protocollo_iva, NULL        --DS_FATTURA_ATTIVA
-- ,null          --RAGIONE_SOCIALE
-- ,null          --NOME
-- ,null          --COGNOME
           ,
           NULL                                          -- DENOMINAZIONE_SEDE
               ,
           NULL                                                     --VIA_SEDE
               ,
           NULL                                           --NUMERO_CIVICO_SEDE
               ,
           NULL                                              --CAP_COMUNE_SEDE
               ,
           NULL                                                    --DS_COMUNE
               ,
           NULL                                                --frazione_sede
               ,
           NULL                                                 --CD_PROVINCIA
               ,
           NULL                                               --CODICE_FISCALE
               ,
           NULL                                                  --PARTITA_IVA
               ,
           0                                               --IM_TOTALE_FATTURA
            ,
           0                                            --IM_TOTALE_IMPONIBILE
            ,
           0                                                   --IM_TOTALE_IVA
            ,
           NULL                                           --RIFERIMENTO_ORDINE
               ,
           NULL                                          --CODICE_FISCALE ente
               ,
           NULL                                             --PARTITA_IVA ente
               ,
           NULL                                             --VIA_FISCALE ente
               ,
           NULL                                      --NUM_CIVICO_FISCALE ente
               ,
           NULL                                      --CAP_COMUNE_FISCALE ente
               ,
           NULL                                       --DS_COMUNE_FISCALE ente
               ,
           NULL                                    --CD_PROVINCIA_FISCALE ente
               ,
           NULL                                        --VIA_SEDE uo emittente
               ,
           NULL                                 --NUM_CIVICO_SEDE uo emittente
               ,
           NULL                                 --CAP_COMUNE_SEDE uo emittente
               ,
           NULL                                       --DS_COMUNE uo emittente
               ,
           NULL                                    --CD_PROVINCIA uo emittente
               ,
              ban.codice_iban
           || ' codice Swift '
           || NVL (ban.codice_swift, ' ')                   -- ban.codice_iban
                                         ,
           ban.numero_conto                                            -- ente
                           ,
           ban.cin                                                     -- ente
                  ,
           ban.intestazione                                            -- ente
                           ,
           ban.abi                                                     -- ente
                  ,
           ban.cab                                                     -- ente
                  ,
           abi.ds_abicab                                               -- ente
                        ,
           com.ds_comune                                --DS_COMUNE_BANCA ente
                        ,
           com.cd_provincia                          --CD_PROVINCIA_BANCA ente
                           ,
           NULL                                              --DS_RIGA_FATTURA
               ,
           0                                                 --PREZZO_UNITARIO
            ,
           0                                                        --QUANTITA
            ,
           0                                                --IM_TOTALE_DIVISA
            ,
           NULL                                                  --CD_VOCE_IVA
               ,
           0                                                          --IM_IVA
            ,
           NULL                                                  --DS_VOCE_IVA
               ,
           0                                                     --PERCENTUALE
            ,
           NULL                                            --FL_NON_IMPONIBILE
               ,
           NULL                                                    --FL_ESENTE
               ,
           NULL                                              --FL_NON_SOGGETTO
               ,
           NULL                                                   --FL_ESCLUSO
               ,
           0                               --totale imponibile per cd_voce_iva
            ,
           0                                      --totale iva per cd_voce_iva
            ,
           NULL                                            -- comune del terzo
           ,
           null											-- tipo bollo voce iva
      FROM v_stm_paramin_ft_attiva vpar,
           fattura_attiva fa,
           unita_organizzativa uo,
           banca ban,
           abicab abi,
           comune com
     WHERE fa.cd_cds = vpar.cd_cds
       AND fa.cd_unita_organizzativa = vpar.cd_uo
       AND fa.esercizio = vpar.esercizio
       AND fa.pg_fattura_attiva = vpar.pg_fattura_attiva
       AND uo.cd_unita_organizzativa = fa.cd_uo_origine
       AND ban.cd_terzo = fa.cd_terzo_uo_cds
       /* commentato ripristino vecchia versione - visto i continui cambiamenti lasciato commentato
         and ((fa.CD_MODALITA_PAG_UO_CDS!='BI' and ban.PG_BANCA = fa.PG_BANCA_UO_CDS) or
            ( fa.CD_MODALITA_PAG_UO_CDS ='BI' and
                  ban.codice_iban =CNRCTB015.GETVAL01PERCHIAVE('CONTO_CORRENTE_SPECIALE','INCASSO') )) */
       AND ban.pg_banca = fa.pg_banca_uo_cds
       AND abi.abi(+) = ban.abi
       AND abi.cab(+) = ban.cab
       AND com.pg_comune(+) = abi.pg_comune
    UNION ALL         -- seconda estrazione degli stessi dati, TI_STAMPA = 'R'
    SELECT vpar.id_report, vpar.cd_cds, vpar.cd_uo, vpar.esercizio,
           vpar.pg_fattura_attiva, 'A'                              -- testata
                                      ,
           'R'                                                     -- ristampa
              ,
           0                                                --PROGRESSIVO_RIGA
            ,
           fa.cd_cds_origine, fa.cd_uo_origine                 -- uo emittente
                                              ,
           uo.ds_unita_organizzativa                        -- ds uo emittente
                                    ,
           fa.ti_fattura, fa.dt_registrazione, fa.dt_emissione,
           fa.protocollo_iva, fa.ds_fattura_attiva
-- ,fa.RAGIONE_SOCIALE
-- ,fa.NOME
-- ,fa.COGNOME
           , ter1.denominazione_sede, ter1.via_sede, ter1.numero_civico_sede,
           ter1.cap_comune_sede, com1.ds_comune,  decode ( com1.ti_italiano_estero,'I',ter1.frazione_sede, ter1.frazione_sede||' '||n.ds_nazione) frazione_sede,
           com1.cd_provincia, fa.codice_fiscale, decode(fa.partita_iva,null,null,decode(fa.fl_intra_ue,'Y',n.cd_iso||' '||fa.partita_iva,fa.partita_iva)),
           fa.im_totale_fattura, fa.im_totale_imponibile, fa.im_totale_iva,
           fa.riferimento_ordine, ana.codice_fiscale                   -- ente
                                                    ,
           ana.partita_iva                                             -- ente
                          ,
           ana.via_fiscale                                             -- ente
                          ,
           ana.num_civico_fiscale                                      -- ente
                                 ,
           ana.cap_comune_fiscale                                      -- ente
                                 ,
           com.ds_comune                             -- DS_COMUNE_FISCALE ente
                        ,
           com.cd_provincia                       -- CD_PROVINCIA_FISCALE ente
                           ,
           ter2.via_sede                                       -- uo emittente
                        ,
           ter2.numero_civico_sede                             -- uo emittente
                                  ,
           ter2.cap_comune_sede                                -- uo emittente
                               ,
           com2.ds_comune                                      -- uo emittente
                         ,
           com2.cd_provincia                                   -- uo emittente
                            ,
           NULL                                             --,ban.codice_iban
               ,
           NULL                                            --NUMERO_CONTO ente
               ,
           NULL                                                    -- CIN ente
               ,
           NULL                                            --INTESTAZIONE ente
               ,
           NULL                                                     --ABI ente
               ,
           NULL                                                     --CAB ente
               ,
           NULL                                               --DS_ABICAB ente
               ,
           NULL                                         --DS_COMUNE_BANCA ente
               ,
           NULL                                      --CD_PROVINCIA_BANCA ente
               ,
           NULL                                              --DS_RIGA_FATTURA
               ,
           0                                                 --PREZZO_UNITARIO
            ,
           0                                                        --QUANTITA
            ,
           0                                                --IM_TOTALE_DIVISA
            ,
           NULL                                                  --CD_VOCE_IVA
               ,
           0                                                          --IM_IVA
            ,
           NULL                                                  --DS_VOCE_IVA
               ,
           0                                                     --PERCENTUALE
            ,
           NULL                                            --FL_NON_IMPONIBILE
               ,
           NULL                                                    --FL_ESENTE
               ,
           NULL                                              --FL_NON_SOGGETTO
               ,
           NULL                                                   --FL_ESCLUSO
               ,
           0                               --totale imponibile per cd_voce_iva
            ,
           0                                      --totale iva per cd_voce_iva
            ,
           com1.ti_italiano_estero                         -- comune del terzo
           ,
           null											-- tipo bollo voce iva
      FROM v_stm_paramin_ft_attiva vpar,
           fattura_attiva fa,
           unita_organizzativa uo,
           terzo ter1,
           comune com1,
           terzo ter,
           anagrafico ana,
           comune com,
           terzo ter2,
           comune com2,
           nazione n
     WHERE fa.cd_cds = vpar.cd_cds
       AND fa.cd_unita_organizzativa = vpar.cd_uo
       AND fa.esercizio = vpar.esercizio
       AND fa.pg_fattura_attiva = vpar.pg_fattura_attiva
       AND uo.cd_unita_organizzativa = fa.cd_uo_origine
       AND ter1.cd_terzo = fa.cd_terzo
       AND com1.pg_comune = ter1.pg_comune_sede
       AND ter.cd_unita_organizzativa = fa.cd_unita_organizzativa -- '999.000'
       AND ana.cd_anag = ter.cd_anag
       AND com.pg_comune = ana.pg_comune_fiscale
       AND ter2.cd_terzo = fa.cd_terzo_uo_cds
       AND com2.pg_comune = ter2.pg_comune_sede
       and n.pg_nazione = com1.pg_nazione
    UNION ALL
    SELECT vpar.id_report, vpar.cd_cds, vpar.cd_uo, vpar.esercizio,
           vpar.pg_fattura_attiva, 'B' ti_record                      -- righe
                                                ,
           'R'                                                     -- ristampa
              ,
           frg.progressivo_riga                             --PROGRESSIVO_RIGA
                               ,
           NULL                                               --CD_CDS_ORIGINE
               ,
           fa.cd_uo_origine                                    -- uo emittente
                           ,
           uo.ds_unita_organizzativa, fa.ti_fattura, fa.dt_registrazione,
           fa.dt_emissione, fa.protocollo_iva, NULL        --DS_FATTURA_ATTIVA
-- ,null          --RAGIONE_SOCIALE
-- ,null          --NOME
-- ,null          --COGNOME
           ,
           NULL                                          -- DENOMINAZIONE_SEDE
               ,
           NULL                                                     --VIA_SEDE
               ,
           NULL                                           --NUMERO_CIVICO_SEDE
               ,
           NULL                                              --CAP_COMUNE_SEDE
               ,
           NULL                                                    --DS_COMUNE
               ,
           NULL                                                --frazione_sede
               ,
           NULL                                                 --CD_PROVINCIA
               ,
           NULL                                               --CODICE_FISCALE
               ,
           NULL                                                  --PARTITA_IVA
               ,
           0                                               --IM_TOTALE_FATTURA
            ,
           0                                            --IM_TOTALE_IMPONIBILE
            ,
           0                                                   --IM_TOTALE_IVA
            ,
           fa.riferimento_ordine, NULL    -- ana.CODICE_FISCALE        -- ente
                                      ,
           NULL                             -- ana.PARTITA_IVA         -- ente
               ,
           NULL                             -- ana.VIA_FISCALE         -- ente
               ,
           NULL                           -- ana.NUM_CIVICO_FISCALE    -- ente
               ,
           NULL                           -- ana.CAP_COMUNE_FISCALE    -- ente
               ,
           NULL                             -- com.DS_COMUNE           -- ente
               ,
           NULL                             -- com.CD_PROVINCIA        -- ente
               ,
           NULL                        -- ter2.VIA_SEDE        -- uo emittente
               ,
           NULL                   -- ter2.NUMERO_CIVICO_SEDE   -- uo emittente
               ,
           NULL                      -- ter2.CAP_COMUNE_SEDE   -- uo emittente
               ,
           NULL                     -- com1.DS_COMUNE          -- uo emittente
               ,
           NULL                  -- com1.CD_PROVINCIA          -- uo emittente
               ,
           NULL                                            -- ,ban.codice_iban
               ,
           NULL                                            --NUMERO_CONTO ente
               ,
           NULL                                                    -- CIN ente
               ,
           NULL                                            --INTESTAZIONE ente
               ,
           NULL                                                     --ABI ente
               ,
           NULL                                                     --CAB ente
               ,
           NULL                                               --DS_ABICAB ente
               ,
           NULL                                         --DS_COMUNE_BANCA ente
               ,
           NULL                                      --CD_PROVINCIA_BANCA ente
               ,
           frg.ds_riga_fattura, frg.prezzo_unitario, frg.quantita,
           frg.im_totale_divisa, frg.cd_voce_iva, frg.im_iva, vi.ds_voce_iva,
           vi.percentuale, vi.fl_non_imponibile, vi.fl_esente,
           vi.fl_non_soggetto, vi.fl_escluso,
           0                               --totale imponibile per cd_voce_iva
            ,
           0                                      --totale iva per cd_voce_iva
            ,
           NULL                                            -- comune del terzo
           ,
           null											-- tipo bollo voce iva
      FROM v_stm_paramin_ft_attiva vpar,
           fattura_attiva fa,
           fattura_attiva_riga frg,
           unita_organizzativa uo,
           voce_iva vi
     WHERE fa.cd_cds = vpar.cd_cds
       AND fa.cd_unita_organizzativa = vpar.cd_uo
       AND fa.esercizio = vpar.esercizio
       AND fa.pg_fattura_attiva = vpar.pg_fattura_attiva
       AND frg.cd_cds = fa.cd_cds
       AND frg.cd_unita_organizzativa = fa.cd_unita_organizzativa
       AND frg.esercizio = fa.esercizio
       AND frg.pg_fattura_attiva = fa.pg_fattura_attiva
       AND uo.cd_unita_organizzativa = fa.cd_uo_origine
       AND vi.cd_voce_iva = frg.cd_voce_iva
    UNION ALL
    SELECT   vpar.id_report, vpar.cd_cds, vpar.cd_uo, vpar.esercizio,
             vpar.pg_fattura_attiva,
             'C' ti_record
                    -- totali imponibile/non imponibile/esente/fuori campo iva
                          ,
             'R'                                                   -- ristampa
                ,
             0                                              --PROGRESSIVO_RIGA
              ,
             NULL                                             --CD_CDS_ORIGINE
                 ,
             fa.cd_uo_origine                                  -- uo emittente
                             ,
             uo.ds_unita_organizzativa, fa.ti_fattura, fa.dt_registrazione,
             fa.dt_emissione, fa.protocollo_iva, NULL      --DS_FATTURA_ATTIVA
-- ,null          --RAGIONE_SOCIALE
-- ,null          --NOME
-- ,null          --COGNOME
             ,
             NULL                                        -- DENOMINAZIONE_SEDE
                 ,
             NULL                                                   --VIA_SEDE
                 ,
             NULL                                         --NUMERO_CIVICO_SEDE
                 ,
             NULL                                            --CAP_COMUNE_SEDE
                 ,
             NULL                                                  --DS_COMUNE
                 ,
             NULL                                              --frazione_sede
                 ,
             NULL                                               --CD_PROVINCIA
                 ,
             NULL                                             --CODICE_FISCALE
                 ,
             NULL                                                --PARTITA_IVA
                 ,
             0                                             --IM_TOTALE_FATTURA
              ,
             0                                          --IM_TOTALE_IMPONIBILE
              ,
             0                                                 --IM_TOTALE_IVA
              ,
             fa.riferimento_ordine, NULL    -- ana.CODICE_FISCALE      -- ente
                                        ,
             NULL                             -- ana.PARTITA_IVA       -- ente
                 ,
             NULL                             -- ana.VIA_FISCALE       -- ente
                 ,
             NULL                           -- ana.NUM_CIVICO_FISCALE  -- ente
                 ,
             NULL                           -- ana.CAP_COMUNE_FISCALE  -- ente
                 ,
             NULL                             -- com.DS_COMUNE         -- ente
                 ,
             NULL                             -- com.CD_PROVINCIA      -- ente
                 ,
             NULL                        -- ter2.VIA_SEDE      -- uo emittente
                 ,
             NULL                   -- ter2.NUMERO_CIVICO_SEDE -- uo emittente
                 ,
             NULL                   -- ter2.CAP_COMUNE_SEDE    -- uo emittente
                 ,
             NULL                  -- com1.DS_COMUNE           -- uo emittente
                 ,
             NULL                  -- com1.CD_PROVINCIA        -- uo emittente
                 ,
             NULL                                          -- ,ban.codice_iban
                 ,
             NULL                                          --NUMERO_CONTO ente
                 ,
             NULL                                                  -- CIN ente
                 ,
             NULL                                          --INTESTAZIONE ente
                 ,
             NULL                                                   --ABI ente
                 ,
             NULL                                                   --CAB ente
                 ,
             NULL                                             --DS_ABICAB ente
                 ,
             NULL                                       --DS_COMUNE_BANCA ente
                 ,
             NULL                                    --CD_PROVINCIA_BANCA ente
                 ,
             NULL                                            --DS_RIGA_FATTURA
                 ,
             0                                               --PREZZO_UNITARIO
              ,
             0                                                      --QUANTITA
              ,
             0                                              --IM_TOTALE_DIVISA
              ,
             vi.cd_voce_iva, 0                                        --IM_IVA
                              ,
             vi.ds_voce_iva, vi.percentuale, vi.fl_non_imponibile,
             vi.fl_esente, vi.fl_non_soggetto, vi.fl_escluso,
             SUM (frg.im_imponibile)                      -- totale imponibile
                                    ,
             SUM (frg.im_iva)                                    -- totale iva
                             ,
             NULL                                          -- comune del terzo
           ,
           vi.ti_bollo											-- tipo bollo voce iva
        FROM v_stm_paramin_ft_attiva vpar,
             fattura_attiva fa,
             fattura_attiva_riga frg,
             unita_organizzativa uo,
             voce_iva vi
       WHERE fa.cd_cds = vpar.cd_cds
         AND fa.cd_unita_organizzativa = vpar.cd_uo
         AND fa.esercizio = vpar.esercizio
         AND fa.pg_fattura_attiva = vpar.pg_fattura_attiva
         AND frg.cd_cds = fa.cd_cds
         AND frg.cd_unita_organizzativa = fa.cd_unita_organizzativa
         AND frg.esercizio = fa.esercizio
         AND frg.pg_fattura_attiva = fa.pg_fattura_attiva
         AND uo.cd_unita_organizzativa = fa.cd_uo_origine
         AND vi.cd_voce_iva = frg.cd_voce_iva
    GROUP BY vpar.id_report,
             vpar.cd_cds,
             vpar.cd_uo,
             vpar.esercizio,
             vpar.pg_fattura_attiva,
             fa.cd_uo_origine,
             fa.ti_fattura,
             fa.dt_registrazione,
             fa.dt_emissione,
             fa.protocollo_iva,
             fa.riferimento_ordine,
             uo.ds_unita_organizzativa,
             vi.percentuale,
             vi.fl_non_imponibile,
             vi.fl_esente,
             vi.fl_non_soggetto,
             vi.fl_escluso,
             vi.ds_voce_iva,
             vi.cd_voce_iva,
             vi.ti_bollo
    UNION ALL
-- r.p. 12/03/2013 in realtà la select sotto non serve in questo momento avendo indicato le modalita fisse :( sulla stampa
-- ma la stampa si aspetta ti_record 'D'
    SELECT vpar.id_report, vpar.cd_cds, vpar.cd_uo, vpar.esercizio,
           vpar.pg_fattura_attiva, 'D'                        -- estremi conto
                                      ,
           'R'                                                     -- ristampa
              ,
           0                                                --PROGRESSIVO_RIGA
            ,
           NULL                                               --CD_CDS_ORIGINE
               ,
           fa.cd_uo_origine                                    -- uo emittente
                           ,
           uo.ds_unita_organizzativa                           --DS_UO_ORIGINE
                                    ,
           fa.ti_fattura, fa.dt_registrazione, fa.dt_emissione,
           fa.protocollo_iva, NULL                         --DS_FATTURA_ATTIVA
-- ,null          --RAGIONE_SOCIALE
-- ,null          --NOME
-- ,null          --COGNOME
           ,
           NULL                                          -- DENOMINAZIONE_SEDE
               ,
           NULL                                                     --VIA_SEDE
               ,
           NULL                                           --NUMERO_CIVICO_SEDE
               ,
           NULL                                              --CAP_COMUNE_SEDE
               ,
           NULL                                                    --DS_COMUNE
               ,
           NULL                                                --frazione_sede
               ,
           NULL                                                 --CD_PROVINCIA
               ,
           NULL                                               --CODICE_FISCALE
               ,
           NULL                                                  --PARTITA_IVA
               ,
           0                                               --IM_TOTALE_FATTURA
            ,
           0                                            --IM_TOTALE_IMPONIBILE
            ,
           0                                                   --IM_TOTALE_IVA
            ,
           NULL                                           --RIFERIMENTO_ORDINE
               ,
           NULL                                          --CODICE_FISCALE ente
               ,
           NULL                                             --PARTITA_IVA ente
               ,
           NULL                                             --VIA_FISCALE ente
               ,
           NULL                                      --NUM_CIVICO_FISCALE ente
               ,
           NULL                                      --CAP_COMUNE_FISCALE ente
               ,
           NULL                                       --DS_COMUNE_FISCALE ente
               ,
           NULL                                    --CD_PROVINCIA_FISCALE ente
               ,
           NULL                                        --VIA_SEDE uo emittente
               ,
           NULL                                 --NUM_CIVICO_SEDE uo emittente
               ,
           NULL                                 --CAP_COMUNE_SEDE uo emittente
               ,
           NULL                                       --DS_COMUNE uo emittente
               ,
           NULL                                    --CD_PROVINCIA uo emittente
               ,
              ban.codice_iban
           || ' codice Swift '
           || NVL (ban.codice_swift, ' ')                   -- ban.codice_iban
                                         ,
           ban.numero_conto                                            -- ente
                           ,
           ban.cin                                                     -- ente
                  ,
           ban.intestazione                                            -- ente
                           ,
           ban.abi                                                     -- ente
                  ,
           ban.cab                                                     -- ente
                  ,
           abi.ds_abicab                                               -- ente
                        ,
           com.ds_comune                                --DS_COMUNE_BANCA ente
                        ,
           com.cd_provincia                          --CD_PROVINCIA_BANCA ente
                           ,
           NULL                                              --DS_RIGA_FATTURA
               ,
           0                                                 --PREZZO_UNITARIO
            ,
           0                                                        --QUANTITA
            ,
           0                                                --IM_TOTALE_DIVISA
            ,
           NULL                                                  --CD_VOCE_IVA
               ,
           0                                                          --IM_IVA
            ,
           NULL                                                  --DS_VOCE_IVA
               ,
           0                                                     --PERCENTUALE
            ,
           NULL                                            --FL_NON_IMPONIBILE
               ,
           NULL                                                    --FL_ESENTE
               ,
           NULL                                              --FL_NON_SOGGETTO
               ,
           NULL                                                   --FL_ESCLUSO
               ,
           0                               --totale imponibile per cd_voce_iva
            ,
           0                                      --totale iva per cd_voce_iva
            ,
           NULL                                            -- comune del terzo
           ,
           null											-- tipo bollo voce iva
      FROM v_stm_paramin_ft_attiva vpar,
           fattura_attiva fa,
           unita_organizzativa uo,
           banca ban,
           abicab abi,
           comune com
     WHERE fa.cd_cds = vpar.cd_cds
       AND fa.cd_unita_organizzativa = vpar.cd_uo
       AND fa.esercizio = vpar.esercizio
       AND fa.pg_fattura_attiva = vpar.pg_fattura_attiva
       AND uo.cd_unita_organizzativa = fa.cd_uo_origine
       AND ban.cd_terzo = fa.cd_terzo_uo_cds
       /* commentato ripristino vecchia versione - visto i continui cambiamenti lasciato commentato
          and ((fa.CD_MODALITA_PAG_UO_CDS!='BI' and ban.PG_BANCA = fa.PG_BANCA_UO_CDS) or
             ( fa.CD_MODALITA_PAG_UO_CDS ='BI' and
                   ban.codice_iban =CNRCTB015.GETVAL01PERCHIAVE('CONTO_CORRENTE_SPECIALE','INCASSO') )) */
       AND ban.pg_banca = fa.pg_banca_uo_cds
       AND abi.abi(+) = ban.abi
       AND abi.cab(+) = ban.cab
       AND com.pg_comune(+) = abi.pg_comune);
