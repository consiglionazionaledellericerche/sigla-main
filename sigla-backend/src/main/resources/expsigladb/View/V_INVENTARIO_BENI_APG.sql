--------------------------------------------------------
--  DDL for View V_INVENTARIO_BENI_APG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INVENTARIO_BENI_APG" ("PG_INVENTARIO", "NR_INVENTARIO", "PROGRESSIVO", "DS_BENE", "CD_CATEGORIA_GRUPPO", "TI_AMMORTAMENTO", "FL_AMMORTAMENTO", "CD_CONDIZIONE_BENE", "TI_COMMERCIALE_ISTITUZIONALE", "VALORE_INIZIALE", "VALORE_AMMORTIZZATO", "VARIAZIONE_PIU", "VARIAZIONE_PIU_INIZIALE", "VARIAZIONE_MENO", "VARIAZIONE_MENO_INIZIALE", "IMPONIBILE_AMMORTAMENTO", "VALORE_ALIENAZIONE", "FL_TOTALMENTE_SCARICATO", "COLLOCAZIONE", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_UBICAZIONE", "CD_ASSEGNATARIO", "DT_VALIDITA_VARIAZIONE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "LOCAL_TRANSACTION_ID", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "PG_FATTURA", "PROGRESSIVO_RIGA", "FL_VISIBILE", "VALORE_ALIENAZIONE_APG", "ETICHETTA", "ESERCIZIO_CARICO_BENE", "ID_BENE_ORIGINE", "FL_MIGRATO", "FL_TRASF_COME_PRINCIPALE", "PG_INVENTARIO_PRINCIPALE", "NR_INVENTARIO_PRINCIPALE", "PROGRESSIVO_PRINCIPALE", "PG_RIGA", "DT_ACQUISIZIONE", "CD_BARRE", "CD_TIPO_DOCUMENTO_AMM", "TARGA", "SERIALE", "CD_CATEGORIA_GRUPPO_NEW", "ID_TRANSITO_BENI_ORDINI") AS
  SELECT
--
-- Date: 02/08/2004
-- Version: 1.6
--
-- Vista per la gestione della paginazione dei beni nell'ambito
-- dello scarico dall'inventario
--
-- History:
--
--
-- Date: 19/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 02/05/2002
-- Version: 1.1
-- Inserimento del campo DT_VALIDITA_VARIAZIONE
--
-- Date: 23/05/2002
-- Version: 1.2
-- Modifiche tabelle
--
-- Date: 21/10/2002
-- Version: 1.3
-- Aggiunta campo etichetta
--
-- Date: 04/12/2003
-- Version: 1.4
-- Aggiunta campo ESERCIZIO_CARICO_BENE
--
-- Date: 24/06/2004
-- Version: 1.5
-- Aggiunta campo VARIAZIONE_PIU per gestione aumento di valore da fattura
--
-- Date: 02/08/2004
-- Version: 1.6
-- Aggiunta campi da ID_BENE_ORIGINE a PROGRESSIVO_PRINCIPALE per gestione migrazione e trasferimento beni
--
-- Body:
--
       inv.PG_INVENTARIO
       ,inv.NR_INVENTARIO
       ,inv.PROGRESSIVO
       ,inv.DS_BENE
       ,inv.CD_CATEGORIA_GRUPPO
       ,inv.TI_AMMORTAMENTO
       ,inv.FL_AMMORTAMENTO
       ,inv.CD_CONDIZIONE_BENE
       ,inv.TI_COMMERCIALE_ISTITUZIONALE
       ,inv.VALORE_INIZIALE
       ,inv.VALORE_AMMORTIZZATO
       ,apg.VARIAZIONE_PIU
       ,inv.VARIAZIONE_PIU VARIAZIONE_PIU_INIZIALE
       ,apg.VARIAZIONE_MENO
       ,inv.VARIAZIONE_MENO VARIAZIONE_MENO_INIZIALE
       ,inv.IMPONIBILE_AMMORTAMENTO
       ,inv.VALORE_ALIENAZIONE
       ,apg.FL_TOTALMENTE_SCARICATO
       ,inv.COLLOCAZIONE
       ,inv.CD_CDS
       ,inv.CD_UNITA_ORGANIZZATIVA
       ,inv.CD_UBICAZIONE
       ,inv.CD_ASSEGNATARIO
       ,inv.DT_VALIDITA_VARIAZIONE
       ,inv.DACR
       ,inv.UTCR
       ,inv.DUVA
       ,inv.UTUV
       ,inv.PG_VER_REC
       ,apg.LOCAL_TRANSACTION_ID
       ,apg.CD_CDS CD_CDS_DOC_AMM
       ,apg.CD_UNITA_ORGANIZZATIVA CD_UO_DOC_AMM
       ,apg.ESERCIZIO ESERCIZIO_DOC_AMM
       ,apg.PG_FATTURA
       ,apg.PROGRESSIVO_RIGA
       ,apg.FL_VISIBILE
       ,apg.VALORE_ALIENAZIONE VALORE_ALIENAZIONE_APG
       ,inv.ETICHETTA
       ,inv.ESERCIZIO_CARICO_BENE
       ,inv.ID_BENE_ORIGINE
       ,inv.FL_MIGRATO
       ,apg.FL_TRASF_COME_PRINCIPALE
       ,apg.PG_INVENTARIO_PRINCIPALE
       ,apg.NR_INVENTARIO_PRINCIPALE
       ,apg.PROGRESSIVO_PRINCIPALE
       ,apg.PG_RIGA
       ,inv.dt_acquisizione
       ,INV.CD_BARRE
       ,apg.CD_TIPO_DOCUMENTO_AMM
       ,inv.targa
       ,inv.seriale
       ,APG.CD_CATEGORIA_GRUPPO_NEW
       ,inv.ID_TRANSITO_BENI_ORDINI
From   inventario_beni inv,
       inventario_beni_apg apg
where inv.PG_INVENTARIO = apg.PG_INVENTARIO
and   inv.NR_INVENTARIO = apg.NR_INVENTARIO
and   inv.PROGRESSIVO   = apg.PROGRESSIVO;
