--------------------------------------------------------
--  DDL for View V_COGE_SCR_MOV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_COGE_SCR_MOV" ("DT_CONTABILIZZAZIONE", "CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "PG_SCRITTURA", "CD_TERZO", "ORIGINE_SCRITTURA", "CD_CAUSALE_COGE", "TI_SCRITTURA", "CD_TIPO_DOCUMENTO", "DS_TIPO_DOCUMENTO", "CD_CDS_DOCUMENTO", "CD_UO_DOCUMENTO", "PG_NUMERO_DOCUMENTO", "ESERCIZIO_DOCUMENTO_AMM", "CD_COMP_DOCUMENTO", "IM_SCRITTURA", "STATO", "ATTIVA", "CD_VOCE_EP", "DS_VOCE_EP", "SEZIONE", "IM_MOVIMENTO", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "TI_ISTITUZ_COMMERC", "FL_MOV_TERZO", "DS_SCRITTURA") AS 
  SELECT
--
-- Date: 14/04/2004
-- Version: 1.0
--
-- Vista di estrazione join scritture movimenti conti
--
-- History:
-- Date: 14/04/2004
-- Version: 1.0
-- Creazione
--
-- Date: 26/07/2005
-- Version:
-- Modifica: aggiunti 2 campi DT_CONTABILIZZAZIONE e DS_TIPO_DOCUMENTO per la  "Stampa Elenco Movimenti"
--
-- Body:
--
-- Date: 27/01/2006
-- Version:
-- Modifica: rielaborata la select perchè troppo lenta la consultazione Scheda Analitica Conto
--
-- Body:
--
          t.dt_contabilizzazione, t.cd_cds, t.esercizio,
          t.cd_unita_organizzativa, t.pg_scrittura, t.cd_terzo,
          t.origine_scrittura, t.cd_causale_coge, t.ti_scrittura,
          t.cd_tipo_documento,
          DECODE
             (t.origine_scrittura,
              'DOCAMM', decode(nvl(t.pg_numero_documento,0),0,null, cnrctb002.getdestipodocamm (t.cd_tipo_documento)),
              'DOCCONT', decode(nvl(t.pg_numero_documento,0),0,null, cnrctb002.getdestipodoccont (t.cd_tipo_documento)),
               NULL
             ) ds_tipo_documento,
          t.cd_cds_documento, t.cd_uo_documento, t.pg_numero_documento,
          t.esercizio_documento_amm, t.cd_comp_documento, t.im_scrittura,
          t.stato, t.attiva, d.cd_voce_ep,
          cnrctb002.getdesvoceep (d.esercizio, d.cd_voce_ep) ds_voce_ep,
          d.sezione, d.im_movimento, d.dt_da_competenza_coge,
          d.dt_a_competenza_coge, d.ti_istituz_commerc, d.fl_mov_terzo,
          t.ds_scrittura
     FROM scrittura_partita_doppia t, movimento_coge d
    WHERE t.cd_cds = d.cd_cds
      AND t.esercizio = d.esercizio
      AND t.cd_unita_organizzativa = d.cd_unita_organizzativa
      AND t.pg_scrittura = d.pg_scrittura;
