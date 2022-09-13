--------------------------------------------------------
--  DDL for View V_DOC_PASSIVO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_PASSIVO" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "CD_NUMERATORE", "PG_VER_REC", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_FATTURA", "STATO_COFI", "STATO_PAGAMENTO_FONDO_ECO", "DT_PAGAMENTO_FONDO_ECO", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DT_FATTURA_FORNITORE", "NR_FATTURA_FORNITORE", "CD_TERZO", "CD_TERZO_CESSIONARIO", "COGNOME", "NOME", "RAGIONE_SOCIALE", "PG_BANCA", "CD_MODALITA_PAG", "IM_IMPONIBILE_DOC_AMM", "IM_IVA_DOC_AMM", "IM_TOTALE_DOC_AMM", "PG_LETTERA", "TI_ENTRATA_SPESA", "TI_SOSPESO_RISCONTRO", "CD_SOSPESO", "FL_DA_ORDINI", "FL_SELEZIONE", "FL_FAI_REVERSALE") AS 
  SELECT /*+ optimizer_features_enable('10.1.0') */
--==================================================================================================
--
-- Date: 18/07/2006
-- Version: 1.15
--
-- Pre view di estrazione delle righe di fatture passive, note di credito attive
-- su obbligazioni e documenti generici passivi utilizzabili nella costruzione di
-- una mandato.
-- I record con FL_SELEZIONE = 'Y', se non pagati, sono quelli che possono essere
-- oggetto di associazione ad un nuovo mandato o reversale.
--
-- Dependency: FUNCTION getFlSelezione/getFlFaiReversale/getModPagCessionario
--
-- History:
--
-- Date: 18/02/2002
-- Version: 1.0
--
-- Creazione
--
-- Date: 25/03/2002
-- Version: 1.1
--
-- Corretta estrazione; nella selezione dei documenti generici non era presente
-- nelle condizioni di where l'attibuto CD_TIPO_DOCUMENTO_AMM ed il filto sui
-- record di dettaglio che puntano ad obbligazioni.
-- Inserito il recupero del codice terzo cessionario nella gestione della
-- cessione di credito
-- Inserito filtro per eliminare le righe documento cancellate
--
-- Date: 11/06/2002
-- Version: 1.2
--
-- Introdotti nell'interfaccia della vista due nuovi attributi:
-- 1) stato_pagamento_fondo_eco.
-- 2) dt_pagamento_fondo_eco.
-- Eliminato il filtro sui documenti associati a fondo economale; questi sono
-- estratti e presentati con fl_selezione = 'N'.
-- Introdotta la gestione anche dei documenti amministrativi ANTICIPO e COMPENSO
--
-- Date: 28/06/2002
-- Version: 1.3
--
-- Introdotta la gestione del documento amministrativo MISSIONE
--
-- Date: 08/07/2002
-- Version: 1.4
-- Il compenso non è mai generato da on-line mandato
-- L'anticipo non deve avere a null l'obbligazione collegata
-- L'importo della missione deve essere la differenza tra il totale missione e l'importo dell'anticipo
-- La missione non deve essere collegata a compenso
--
-- Date: 09/07/2002
-- Version: 1.5
-- Se l'importo della missione è inferire all'anticipo -> la missione non può essere direttamente contabilizzata in mandati
--
-- Date: 19/07/2002
-- Version: 1.6
-- Fix errore in selezione MISSIONI per la generazione mandati se queste non sono associate ad anticipi
--
-- Date: 03/09/2002
--
-- Version: 1.7
-- Inserito controllo di non estrarre compensi negativi
--
-- Date: 10/09/2002
-- Version: 1.8
--
-- Controllo modello 1210 ed estrazione dei relativi campi per la gestione del controllo sospeso su
-- mandati
--
-- Date: 15/10/2002
-- Version: 1.9
--
-- Inserita l'estrazione del campo FL_FAI_REVERSALE per gestire i casi in cui a fronte dell'emissione
-- di un mandato deve essere generata in automatico e dall'on-line una reversale. Solo fatture
--
-- Date: 08/11/2002
-- Version: 1.10
--
-- Fix errore interno 2783 estesa gestione alla funzione getFlSelezione anche alle missoni
-- Implementata la gestione della cessione di credito
--
-- Date: 10/12/2002
-- Version: 1.11
--
-- Fix errore interno 2988 estesa gestione alla funzione getFlSelezione anche ad anticipi. Non
-- era controllato il fatto che un anticipo fosse rimborsato.
-- Era errato il ritorno della funzione in caso di documento associato a fondo economale
--
-- Date: 19/02/2003
-- Version: 1.12
--
-- Estrazione stato cofi dalle righe
--
-- Date: 03/06/2003
-- Version: 1.13
-- Aggiunto il pg_ver_rec della testata del doc amministrativo
--
-- Date: 06/08/2003
-- Version: 1.14
-- L'estrazione del fl_selezione, è stata modificata per gestire anche il fl_congelata relativo alle Fatture Passive e alle
-- Fatture Attive di tipo Nota di Credito (ti_fattura = 'C').
--
-- Date: 18/07/2006
-- Version: 1.15
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 21/04/2008
-- Version: 1.16
-- Gestione Terzo/Modalità di pagamento sul dettaglio della Fattura Passiva
--
-- Body:
--
--==================================================================================================
          a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'FATTURA_P',
          a.pg_fattura_passiva, 'GEN' cd_numeratore, a.pg_ver_rec, a.cd_cds_origine,
          a.cd_uo_origine, a.ti_fattura, b.stato_cofi,
          a.stato_pagamento_fondo_eco, a.dt_pagamento_fondo_eco,
          b.cd_cds_obbligazione, b.esercizio_obbligazione,
          b.esercizio_ori_obbligazione, b.pg_obbligazione,
          b.pg_obbligazione_scadenzario, a.dt_fattura_fornitore,
          a.nr_fattura_fornitore, a.cd_terzo, b.cd_terzo_cessionario,
          a.cognome, a.nome, a.ragione_sociale,
          DECODE (b.cd_terzo_cessionario,
                  NULL, b.pg_banca,
                  d.pg_banca_delegato
                 ),
          DECODE (b.cd_terzo_cessionario,
                  NULL, b.cd_modalita_pag,
                  SUBSTR (getmodpagcessionario (b.cd_terzo_cessionario,
                                                d.ti_pagamento
                                               ),
                          1,
                          10
                         )
                 ),
          DECODE (a.ti_fattura, 'C', (b.im_imponibile * -1), b.im_imponibile),
          DECODE (a.ti_fattura, 'C', (b.im_iva * -1), b.im_iva),
          DECODE (a.ti_fattura,
                  'C', ((b.im_imponibile + b.im_iva) * -1),
                  (b.im_imponibile + b.im_iva
                  )
                 ),
          a.pg_lettera, c.ti_entrata_spesa, c.ti_sospeso_riscontro,
          c.cd_sospeso, nvl(a.fl_da_ordini,'N'),
          SUBSTR (getflselezione ('FATTURA_P',
                                  a.stato_pagamento_fondo_eco,
                                  a.ti_fattura,
                                  a.pg_lettera,
                                  c.cd_sospeso,
                                  NULL,
                                  0,
                                  0,
                                  0,
                                  a.fl_congelata,
                                  a.stato_liquidazione
                                 ),
                  1,
                  1
                 ),
          SUBSTR (getflfaireversale (a.ti_fattura,
                                     a.ti_istituz_commerc,
                                     a.ti_bene_servizio,
                                     a.fl_san_marino_senza_iva,
                                     DECODE (a.fl_merce_intra_ue,
                                             'Y', 'Y',
                                             a.fl_intra_ue
                                            ),
                                     a.fl_split_payment,
                                     DECODE (a.ti_bene_servizio,
                                             'B', t.ti_bene_servizio,
                                             t.fl_servizi_non_residenti
                                            )
                                    ),
                  1,
                  1
                 )
     FROM fattura_passiva a,
          fattura_passiva_riga b,
          lettera_pagam_estero c,
          banca d,
          tipo_sezionale t
    WHERE a.cd_tipo_sezionale = t.cd_tipo_sezionale
      AND b.cd_cds = a.cd_cds
      AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
      AND b.esercizio = a.esercizio
      AND b.pg_fattura_passiva = a.pg_fattura_passiva
      AND b.dt_cancellazione IS NULL
      AND nvl(a.fl_da_ordini,'N')= 'N'
      AND c.cd_cds(+) = a.cd_cds
      AND c.cd_unita_organizzativa(+) = a.cd_unita_organizzativa
      AND c.esercizio(+) = a.esercizio_lettera
      AND c.pg_lettera(+) = a.pg_lettera
      AND d.cd_terzo = b.cd_terzo
      AND d.pg_banca = b.pg_banca
   UNION ALL
   SELECT a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'FATTURA_A',
          a.pg_fattura_attiva, 'GEN' cd_numeratore, a.pg_ver_rec, a.cd_cds_origine,
          a.cd_uo_origine, a.ti_fattura, b.stato_cofi, 'N', TO_DATE (NULL),
          b.cd_cds_obbligazione, b.esercizio_obbligazione,
          b.esercizio_ori_obbligazione, b.pg_obbligazione,
          b.pg_obbligazione_scadenzario, TO_DATE (NULL), NULL, a.cd_terzo,
          TO_NUMBER (NULL), a.cognome, a.nome, a.ragione_sociale, a.pg_banca,
          a.cd_modalita_pag, b.im_imponibile, b.im_iva,
          (b.im_imponibile + b.im_iva), TO_NUMBER (NULL), NULL, NULL, NULL, 'N',
          DECODE (a.fl_congelata, 'Y', 'N', 'Y'), 'N'
     FROM fattura_attiva a, fattura_attiva_riga b
    WHERE a.ti_fattura = 'C'
      AND b.cd_cds = a.cd_cds
      AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
      AND b.esercizio = a.esercizio
      AND b.pg_fattura_attiva = a.pg_fattura_attiva
      AND b.cd_cds_obbligazione IS NOT NULL
      AND b.esercizio_obbligazione IS NOT NULL
      AND b.esercizio_ori_obbligazione IS NOT NULL
      AND b.pg_obbligazione IS NOT NULL
      AND b.pg_obbligazione_scadenzario IS NOT NULL
      AND b.dt_cancellazione IS NULL
   UNION ALL
   SELECT a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
          a.cd_tipo_documento_amm, a.pg_documento_generico, 'GEN' cd_numeratore,
          b.pg_ver_rec,
          b.cd_cds_origine, b.cd_uo_origine, NULL, a.stato_cofi,
          b.stato_pagamento_fondo_eco, b.dt_pagamento_fondo_eco,
          a.cd_cds_obbligazione, a.esercizio_obbligazione,
          a.esercizio_ori_obbligazione, a.pg_obbligazione,
          a.pg_obbligazione_scadenzario, TO_DATE (NULL), NULL, a.cd_terzo,
          a.cd_terzo_cessionario, a.cognome, a.nome, a.ragione_sociale,
          DECODE (a.cd_terzo_cessionario,
                  NULL, a.pg_banca,
                  d.pg_banca_delegato
                 ),
          DECODE (a.cd_terzo_cessionario,
                  NULL, a.cd_modalita_pag,
                  SUBSTR (getmodpagcessionario (a.cd_terzo_cessionario,
                                                d.ti_pagamento
                                               ),
                          1,
                          10
                         )
                 ),
          0, 0, a.im_riga, b.pg_lettera, c.ti_entrata_spesa,
          c.ti_sospeso_riscontro, c.cd_sospeso, 'N',
          SUBSTR (getflselezione ('DOC_GENERICO',
                                  b.stato_pagamento_fondo_eco,
                                  NULL,
                                  b.pg_lettera,
                                  c.cd_sospeso,
                                  NULL,
                                  0,
                                  0,
                                  0,
                                  NULL,
                                  b.stato_liquidazione
                                 ),
                  1,
                  1
                 ),
          'N'
     FROM documento_generico_riga a,
          documento_generico b,
          lettera_pagam_estero c,
          banca d
    WHERE a.cd_cds_obbligazione IS NOT NULL
      AND a.esercizio_obbligazione IS NOT NULL
      AND a.esercizio_ori_obbligazione IS NOT NULL
      AND a.pg_obbligazione IS NOT NULL
      AND a.pg_obbligazione_scadenzario IS NOT NULL
      AND a.dt_cancellazione IS NULL
      AND b.cd_cds = a.cd_cds
      AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
      AND b.esercizio = a.esercizio
      AND b.cd_tipo_documento_amm = a.cd_tipo_documento_amm
      AND b.pg_documento_generico = a.pg_documento_generico
      AND c.cd_cds(+) = b.cd_cds
      AND c.cd_unita_organizzativa(+) = b.cd_unita_organizzativa
      AND c.esercizio(+) = b.esercizio_lettera
      AND c.pg_lettera(+) = b.pg_lettera
      AND d.cd_terzo = a.cd_terzo
      AND d.pg_banca = a.pg_banca
   UNION ALL
   SELECT a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'ANTICIPO',
          a.pg_anticipo, 'GEN' cd_numeratore, a.pg_ver_rec,
          a.cd_cds_origine, a.cd_uo_origine,
          NULL, a.stato_cofi, a.stato_pagamento_fondo_eco,
          a.dt_pagamento_fondo_eco, a.cd_cds_obbligazione,
          a.esercizio_obbligazione, a.esercizio_ori_obbligazione,
          a.pg_obbligazione, a.pg_obbligazione_scadenzario, TO_DATE (NULL),
          NULL, a.cd_terzo, TO_NUMBER (NULL), a.cognome, a.nome,
          a.ragione_sociale, a.pg_banca, a.cd_modalita_pag, 0, 0,
          a.im_anticipo, TO_NUMBER (NULL), NULL, NULL, NULL, 'N',
          SUBSTR (getflselezione ('ANTICIPO',
                                  a.stato_pagamento_fondo_eco,
                                  NULL,
                                  0,
                                  NULL,
                                  NULL,
                                  0,
                                  0,
                                  NVL (b.im_rimborso, 0),
                                  NULL
                                 ),
                  1,
                  1
                 ),
          'N'
     FROM anticipo a, rimborso b
    WHERE a.cd_cds_obbligazione IS NOT NULL
      AND a.esercizio_obbligazione IS NOT NULL
      AND a.esercizio_ori_obbligazione IS NOT NULL
      AND a.pg_obbligazione IS NOT NULL
      AND a.pg_obbligazione_scadenzario IS NOT NULL
      AND a.dt_cancellazione IS NULL
      AND b.cd_cds_anticipo(+) = a.cd_cds
      AND b.cd_uo_anticipo(+) = a.cd_unita_organizzativa
      AND b.esercizio_anticipo(+) = a.esercizio
      AND b.pg_anticipo(+) = a.pg_anticipo
   UNION ALL
   SELECT a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'COMPENSO',
          a.pg_compenso, 'GEN' cd_numeratore, a.pg_ver_rec,
          a.cd_cds_origine, a.cd_uo_origine,
          NULL, a.stato_cofi, a.stato_pagamento_fondo_eco,
          a.dt_pagamento_fondo_eco, a.cd_cds_obbligazione,
          a.esercizio_obbligazione, a.esercizio_ori_obbligazione,
          a.pg_obbligazione, a.pg_obbligazione_scadenzario, TO_DATE (NULL),
          NULL, a.cd_terzo, TO_NUMBER (NULL), a.cognome, a.nome,
          a.ragione_sociale, a.pg_banca, a.cd_modalita_pag, 0, 0,
          a.im_totale_compenso, TO_NUMBER (NULL), NULL, NULL, NULL,
          'N', 'N', 'N'
     FROM compenso a
    WHERE a.cd_cds_obbligazione IS NOT NULL
      AND a.esercizio_obbligazione IS NOT NULL
      AND a.esercizio_ori_obbligazione IS NOT NULL
      AND a.pg_obbligazione IS NOT NULL
      AND a.pg_obbligazione_scadenzario IS NOT NULL
      AND a.dt_cancellazione IS NULL
      AND a.pg_compenso > 0
   UNION ALL
   SELECT a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'MISSIONE',
          a.pg_missione, 'GEN' cd_numeratore, a.pg_ver_rec,
          a.cd_cds, a.cd_unita_organizzativa,
          NULL, a.stato_cofi, a.stato_pagamento_fondo_eco,
          a.dt_pagamento_fondo_eco, a.cd_cds_obbligazione,
          a.esercizio_obbligazione, a.esercizio_ori_obbligazione,
          a.pg_obbligazione, a.pg_obbligazione_scadenzario, TO_DATE (NULL),
          NULL, a.cd_terzo, TO_NUMBER (NULL), a.cognome, a.nome,
          a.ragione_sociale, a.pg_banca, a.cd_modalita_pag, 0, 0,
          a.im_totale_missione - NVL (b.im_anticipo, 0), TO_NUMBER (NULL),
          NULL, NULL, NULL, 'N',
          SUBSTR (getflselezione ('MISSIONE',
                                  a.stato_pagamento_fondo_eco,
                                  NULL,
                                  0,
                                  NULL,
                                  a.fl_associato_compenso,
                                  a.im_totale_missione,
                                  NVL (b.im_anticipo, 0),
                                  0,
                                  NULL,
                                  a.stato_liquidazione
                                 ),
                  1,
                  1
                 ),
          'N'
     FROM missione a, anticipo b
    WHERE a.cd_cds_obbligazione IS NOT NULL
      AND a.esercizio_obbligazione IS NOT NULL
      AND a.esercizio_ori_obbligazione IS NOT NULL
      AND a.pg_obbligazione IS NOT NULL
      AND a.pg_obbligazione_scadenzario IS NOT NULL
      AND a.dt_cancellazione IS NULL
      AND b.esercizio(+) = a.esercizio_anticipo
      AND b.cd_cds(+) = a.cd_cds_anticipo
      AND b.cd_unita_organizzativa(+) = a.cd_uo_anticipo
      AND b.pg_anticipo(+) = a.pg_anticipo
   UNION ALL
   SELECT a.cd_cds, c.cd_unita_organizzativa, a.esercizio, 'ORDINE',
          a.numero, a.cd_numeratore, a.pg_ver_rec,
          a.cd_cds, c.cd_unita_organizzativa,
          NULL, 'C' stato_cofi, 'N' stato_pagamento_fondo_eco,
          NULL dt_pagamento_fondo_eco, a.cd_cds_obbl,
          a.esercizio_obbl, a.esercizio_orig_obbl,
          a.pg_obbligazione, a.pg_obbligazione_scad, TO_DATE (NULL),
          NULL, b.cd_terzo, TO_NUMBER (NULL), b.cognome, b.nome,
          b.ragione_sociale, b.pg_banca, b.cd_modalita_pag, a.im_imponibile, a.im_iva,
          a.im_totale_consegna, TO_NUMBER (NULL),
          NULL, NULL, NULL,
          'N', 'N', 'N'
     FROM ordine_acq_consegna a, ordine_acq b, unita_operativa_ord c
    WHERE a.cd_cds_obbl IS NOT NULL
      AND a.esercizio_obbl IS NOT NULL
      AND a.esercizio_orig_obbl IS NOT NULL
      AND a.pg_obbligazione IS NOT NULL
      AND a.pg_obbligazione_scad IS NOT NULL
      AND a.cd_cds = b.cd_cds
      AND a.cd_unita_operativa = b.cd_unita_operativa
      and a.esercizio = b.esercizio
      and a.cd_numeratore = b.cd_numeratore
      and a.numero = b.numero
      and a.cd_unita_operativa = c.cd_unita_operativa;

   COMMENT ON TABLE "V_DOC_PASSIVO"  IS 'Pre view di estrazione delle righe di fatture passive, attive e documenti passivi necessari
alla selezione nella costruzione di un mandato. La vista è stata scorporata da
V_DOC_PASSIVO_OBBLIGAZIONE.
I record con FL_SELEZIONE = ''Y'' sono quelli, se non pagati, che possono essere oggetto di
associazione ad un nuovo mandato';



