--------------------------------------------------------
--  DDL for View V_AMMORTAMENTO_BENI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_AMMORTAMENTO_BENI" ("PG_INVENTARIO", "NR_INVENTARIO", "PROGRESSIVO", "CD_CATEGORIA_GRUPPO", "TI_AMMORTAMENTO_BENE", "FL_AMMORTAMENTO", "VALORE_INIZIALE", "VALORE_AMMORTIZZATO", "VARIAZIONE_PIU", "VARIAZIONE_MENO", "IMPONIBILE_AMMORTAMENTO", "FL_TOTALMENTE_SCARICATO", "ESERCIZIO_CARICO_BENE", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO_COMPETENZA", "CD_TIPO_AMMORTAMENTO", "TI_AMMORTAMENTO", "DT_CANCELLAZIONE", "PERC_PRIMO_ANNO", "PERC_SUCCESSIVI", "NUMERO_ANNI") AS 
  SELECT
-- =================================================================================================
--
-- Date: 29/09/2004
-- Version: 1.5
--
-- Vista estrazione delle informazioni relative al tipo ammortamento per i beni in inventario
--
-- History:
--
-- Date: 28/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 08/08/2003
-- Version: 1.1
-- Completamento vista con dati ubicazione (cds/uo) del bene + numero anni tipo ammortamento
--
-- Date: 02/12/2003
-- Version: 1.2
-- Inserimento del campo ESERCIZIO_CARICO_BENE della tabella inventario_beni
-- questo campo indica l'esercizio nel quale deve essere effettuato l'ammortamento del bene
--
-- Date: 03/12/2003
-- Version: 1.3
-- Inserimento del campo TI_AMMORTAMENTO_BENE della tabella inventario_beni
--
-- Date: 06/09/2004
-- Version: 1.4
--
-- Fix errore CINECA n. 835. Il sistema ammortizza erronemaente un bene anche se completamente scaricato
--
-- Date: 29/09/2004
-- Version: 1.5
--
-- Rilascio richiesta CINECA n. 841. Revisione per nuova gestione ammortamento beni che tenga conto
-- di quanto movimentato nell'esercizio successivo
--
-- Body:
--
-- =================================================================================================
       inv.PG_INVENTARIO,
       inv.NR_INVENTARIO,
       inv.PROGRESSIVO,
       inv.CD_CATEGORIA_GRUPPO,
       inv.TI_AMMORTAMENTO,
       inv.FL_AMMORTAMENTO,
       inv.VALORE_INIZIALE,
       NVL(inv.VALORE_AMMORTIZZATO,0),
       NVL(inv.VARIAZIONE_PIU,0),
       NVL(inv.VARIAZIONE_MENO,0),
       NVL(inv.IMPONIBILE_AMMORTAMENTO,0),
       inv.FL_TOTALMENTE_SCARICATO,
       inv.ESERCIZIO_CARICO_BENE,
       inv.cd_cds,
       inv.cd_unita_organizzativa,
       ass.ESERCIZIO_COMPETENZA,
       ass.CD_TIPO_AMMORTAMENTO,
       ass.TI_AMMORTAMENTO,
       ass.DT_CANCELLAZIONE,
       tamm.PERC_PRIMO_ANNO,
       tamm.PERC_SUCCESSIVI,
       tamm.NUMERO_ANNI
FROM   INVENTARIO_BENI inv,
       (SELECT *
        FROM   ASS_TIPO_AMM_CAT_GRUP_INV
        WHERE dt_cancellazione is null) ass,
       TIPO_AMMORTAMENTO tamm
WHERE  inv.FL_AMMORTAMENTO = 'Y' AND
       ass.CD_CATEGORIA_GRUPPO (+) = INV.CD_CATEGORIA_GRUPPO AND
       ass.TI_AMMORTAMENTO (+) = inv.TI_AMMORTAMENTO AND
       tamm.CD_TIPO_AMMORTAMENTO (+) = ass.CD_TIPO_AMMORTAMENTO AND
       tamm.TI_AMMORTAMENTO (+) = ass.TI_AMMORTAMENTO
;

   COMMENT ON TABLE "V_AMMORTAMENTO_BENI"  IS 'Vista per l''estrazione delle informazioni relative al tipo ammortamento, per i beni in inventario';
