--------------------------------------------------------
--  DDL for Package CNRCTB253
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB253" AS
--
--
-- CNRCTB253: Package per la gestione delle estrazioni IVA per la liquidazione annuale
--
-- Date: 17/02/2004
-- Version: 1.5
--
-- Dependency:
--
-- History:
--
-- Date: 27/11/2003
-- Version: 1.0
-- Creazione
--
-- Date: 09/12/2003
-- Version: 1.1
-- Correzione condizione bene su dettaglio per tabella riepilogativa quadri iva acquisti
--
-- Date: 10/12/2003
-- Version: 1.2
-- Corretta descrizione DS_A_9
--
-- Date: 03/02/2004
-- Version: 1.3
-- Modifica estrazione tabCodIvaVendite - richiesta n. 758
--
-- Date: 04/02/2004
-- Version: 1.4
-- Corretta modifica precedente: condizione or per selezione cd_voce_iva
--
-- Date: 17/02/2004
-- Version: 1.5
-- Fix estrazione iva debito/credito esercizio precedente (errore n. 768)
--
-- Constants:
--
-- Descrizioni per il pannello costante
-- Parte acquisti
DS_A_1 CONSTANT VARCHAR2(200)  := 'Importazioni';
DS_A_2 CONSTANT VARCHAR2(200)  := 'Autofatture registro v/caut';
DS_A_3 CONSTANT VARCHAR2(200)  := 'Acquisti IntraUE reg. v/cue';
DS_A_4 CONSTANT VARCHAR2(200)  := 'Acquisti IntraUE reg. v/cue con sola condizione "beni"';
DS_A_5 CONSTANT VARCHAR2(200)  := 'Acquisti RSM senza pag.to IVA reg. v/rsms';
DS_A_6 CONSTANT VARCHAR2(200)  := 'Acquisti RSM con add.to iva';
DS_A_7 CONSTANT VARCHAR2(200)  := 'Acquisto beni ammort.li di cui al totale';
DS_A_8 CONSTANT VARCHAR2(200)  := 'Altri acquisti e importazioni di cui al totale';
DS_A_9 CONSTANT VARCHAR2(200)  := 'Acquisto beni non ammort.li';
-- Parte vendite
DS_V_1 CONSTANT VARCHAR2(200)  := 'Operazioni effettuate nell''anno,ma con imposta esigibile in anni successivi';
DS_V_2 CONSTANT VARCHAR2(200)  := 'Ammontare delle cessioni di beni ad operatori RSM';
DS_V_3 CONSTANT VARCHAR2(200)  := 'Operazioni effettuate nell''anno, soggette a split payment';
-- Functions and Procedures:
--
--
-- estrazione tab1_LIQUIDAZIONIIVA_cineca.xls"
procedure riepilogoLiquidazioneIva(aId number,aEs number);

-- estrazioni tab2_IVA-ACQUISTI_cineca.xls, tab3_IVA-VA-VF_cineca_verificata.xls
-- aFlEsclusione = 'N' >>> tutti i codici iva (tab2_IVA-ACQUISTI_cineca.xls)
-- aFlEsclusione = 'Y' >>> solo codici iva con fl_escluso = 'N' e fl_non_soggetto = 'N' (tab3_IVA-VA-VF_cineca_verificata.xls)
procedure tabCodIvaAcquisti(aId number,aEs number,aFlEsclusione char);

-- estrazioni tab4_IVA_-VA-VE_cineca.xls,tab5_ICA_-VENDITE_cineca.xls
-- aFlEsclusione = 'N' >>> tutti i codici iva (tab5_ICA_-VENDITE_cineca.xls)
-- aFlEsclusione = 'Y' >>> solo codici iva con fl_escluso = 'N' e fl_non_soggetto = 'N' (tab4_IVA_-VA-VE_cineca.xls)
procedure tabCodIvaVendite(aId number, aEs number,aFlEsclusione char);

END CNRCTB253;
