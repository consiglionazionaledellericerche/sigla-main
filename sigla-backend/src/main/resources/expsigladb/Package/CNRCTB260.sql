--------------------------------------------------------
--  DDL for Package CNRCTB260
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB260" AS
--==================================================================================================
--
-- Cnrctb260 - Package di gestione delle procedure finalizzate al calcolo e alla stampa della liquidazione IVA,
-- usate nel package CNRCTB250
--
-- Date: 29/01/2004
-- Version: 4.14
--
-- Dependency: CNRCTB 100/120/255/250 IBMERR 001
--
-- History:
--
-- Date: 07/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 10/06/2002
-- Version: 1.1
-- Modifica per introduzione controlli sullo stato dei registri precedenti.
--
-- Date: 24/06/2002
-- Version: 1.2
-- Eliminazione del parametro aTiFattura
--
-- Date: 26/06/2002
-- Version: 1.3
-- Gestione formattata dei messaggi di errore :
-- CODICE ERRORE - DESCRIZIONE ERRORE.
-- Il codice errore e seguito da spazio trattino spazio, poi dalla descrizione,
-- che a sua volta � seguita dal punto.
--
-- Date: 03/07/2002
-- Version: 1.4
-- Introduzione del campo CD_UNITA_ORGANIZZATIVA in LIQUIDAZIONE_IVA
--
-- Date: 04/07/2002
-- Version: 1.5
-- Modifica gestione liquidazione IVA
--
-- Date: 08/07/2002
-- Version: 1.6
-- Correzione della condiz<ione di where sulla tabella liquidazione iva
-- quando si effettuava la scrittura iva (ora si va anche per uo)
--
-- Date: 11/07/2002
-- Version: 1.9
-- introdotta gestione separata per il calcolo della liquidazione del CNR e dei suio CDS
--
-- Date: 11/07/2002
-- Version: 1.10
-- Gestione errore tramite IBMERR001
--
-- Date: 17/07/2002
-- Version: 1.12
-- Aggiunta valorizzazione di utcr per report_dettaglio.
--
-- Date: 18/07/2002
-- Version: 1.13
-- Gestione del sezionale non trovato in fase di protocollazione delle fatture passive
--
-- Date: 19/07/2002
-- Version: 1.14
-- Calcolo campo iva_deb_cred della tabella liquidazione_iva
-- campo positivo => iva credito il cnr deve avere
-- campo negativo => iva debito  il cnr deve dare
--
-- Date: 23/07/2002
-- Version: 1.15
-- Modificato calcolo estrazione importo iva debito credito del periodo precedente
--
-- Date: 25/07/2002
-- Version: 1.16
-- Modifica all'inserimento in report dettaglio, per le fatture attive viene ora inseritio il
-- cd_cds origine e cd_uo origine invece che il cd_cds e cd_uo
--
-- Date: 02/09/2002
-- Version: 1.17
-- Modificata insert nella REPORT_STATO.
--
-- Date: 19/09/2002
-- Version: 2.0
-- Ristrutturazione del package
--
-- Date: 19/09/2002
-- Version: 2.1
-- Modifica where nella protocollazione delle fatture passive.
--
-- Date: 30/09/2002
-- Version: 2.2
-- Modifica la procedura di protocollazione Fatt Pass e Auto.
--
-- Date: 11/11/2002
-- Version: 2.3
-- Inserita protocollazione delle Autofatture nella stampa dei registri iva vendite.
--
-- Date: 14/11/2002
-- Version: 2.4
--
-- Fix errore in protocollazione fatture per cui sono incluse anche quelle annullate
-- Revisione completa della procedura di protocollazione documenti in quanto non teneva conto che
-- l'assegnazione dei protocolli deve operare su documenti in ordine di data (registrazione) e
-- progressivo identificativo.
-- Modifica viste per stampa registri
-- Introdotta la gestione di stampa anche dei sezionali di tipo istituzionale
-- Modifica della sintassi usata e reintrodotto il recupero della descrizione del sezionale e del
-- suo riferimento istituzionale e commerciale in sede di valorizzaParametri. Corretto l'assenza del
-- filtro per valorizzare sulle matrici acquisti e vendite i soli sezionali di acquisto o vendita.
--
-- Date: 09/12/2002
-- Version: 2.5
--
-- Revisione completa per centralizzare i controlli di ammissibilit� delle stampe e correzione degli
-- stessi.
--
-- Date: 10/12/2002
-- Version: 2.6
--
-- Revisione per modifica alla tabella REPORT_DETTAGLIO
--
-- Date: 11/12/2002
-- Version: 2.7
--
-- Esposto TOTALE GRUPPO invece di solo TOTALE nei riepilogativi dei registri e nei riepilogativi
-- Attivata la stampa riepilogativi
--
-- Date: 16/12/2002
-- Version: 3.0
--
-- Revisone complessiva della procedura
-- In tutte le stampe esposto, in report generico, come ultimi 3 campi le seguenti informazioni:
-- - tipoReport     -> P = Provvisorio D = Definitivo
-- - tipoRegistro     -> A = Acquisti V = Vendite
-- - descrizione gruppo di stampa
-- Inserita l'assegnazione dei protocolli sono per stampe definitive
--
-- Date: 17/12/2002
-- Version: 3.1
--
-- Allineamento vista alla nuova struttura della tabella REPORT_GENERICO
-- Fix errore di esposizione del TOTALE DI GRUPPO in riepilogativo per aliquota in stampa registri la
-- cui label veniva attaccata al codice gruppo.
--
-- Date: 18/12/2002
-- Version: 3.2
--
-- Spostata gestione dell'inserimento in tabella REPORT_STATO nel package CNRCTB255.
--
-- Date: 17/01/2003
-- Version: 3.3
--
-- Inserita la gestione differenziata della LIQUIDAZIONE_IVA per attivit� commerciali ed istituzionali
-- (San Marino senza IVA e Intra_ue) anche in sede di inserimento ed aggiornamento della tabella
-- LIQUIDAZIONE_IVA
--
-- Date: 24/01/2003
-- Version: 3.4
--
-- Modificata la semantica del campo corrente in sezionali: 0 rappresenta il corrente nel caso il sezionale
-- non sia stato ancora utilizzato: viene ora utilizzato corrente=-1 in variabili funzione getProtCorrenteSezionale
-- per indicare che il sezionale non � presente o non compatibile
--
-- Date: 31/01/2003
-- Version: 3.5
--
-- Fix errore per eliminare il doppio record per liquidazione (uno provvisorio e l'altro definitivo)
--
-- Date: 03/02/2003
-- Version: 3.6
--
-- Controllo definitivo gestione corretta dell'id_report per liquidazione definitiva (id = 0)
--
-- Date: 03/02/2003
-- Version: 4.0
--
-- Attivazione della liquidazione iva per istituzionali (San Marino senza IVA e Intra_ue)
-- Attivazione della gestione del prorata (CONFIGURAZIONE_CNR)
-- Rivista liquidazione ente errori nel recupero delle informazioni
--
-- Date: 07/02/2003
-- Version: 4.1
--
-- Revisione procedura per la predisposizione alle modifiche alla gestione della liquidazione IVA
-- Inserimento delle funzioni per stampa riepilogativo del centro
-- Correzione funzione di inserimento nella tabella REPORT_DETTAGLIO delle fatture ad esigibilit�
-- differita divenute esigibili in una liquidazione definitiva
--
-- Date: 07/02/2003
-- Version: 4.2
--
-- Fix errori in calcolo liquidazione. Spostamento della logica client sul server
--
-- Date: 10/02/2003
-- Version: 4.3
--
-- Completato inserimento gestione del riepilogativo per sezionale del centro spostato su package
-- CNRCTB265
-- Fix ultime anomalie in Liquidazione IVA, corretta scrittura in report dettaglio delle fatture
-- attive ad esigibilit� differita incluse in liquidazione
--
-- Date: 24/02/2003
-- Version: 4.4
--
-- Richiesta CINECA n. 486. Modifica layout della stampa del riepilogativo CENTRO. Inserito il recupero
-- dell'informazione della ragione sociale e partita IVA dell'ente da esporre in intestazione per tutti
-- i report IVA
--
-- Date: 28/02/2003
-- Version: 4.5
--
-- Inserito metodo di ritorno all'esistenza di una liquidazione definitiva
--
-- Date: 04/03/2003
-- Version: 4.6
--
-- FIX errore CINECA n. 516. Attivazione del calcolo del prorata anche nelle liquidazioni UO (SOLO COMMERCIALI)
--
-- Date: 05/03/2003
-- Version: 4.7
--
-- FIX errore CINECA n. 522. Non includeva nel calcolo dell'iva da versare l'eventuale credito o debito
-- del periodo precedente.
--
-- Date: 05/03/2003
-- Version: 4.8
--
-- FIX errore CINECA n. 523. Non era azzerata la variabile da esporre sulla colonna intraue
-- in stampa registri vendita
--
-- Date: 06/03/2003
-- Version: 4.9
--
-- Richieste CINECA
-- Le gestioni intra_ue e san marino senza IVA (istituzionali) a rilevanza IVA sono solo quelle dove
-- il tipo sezionale presenta l'attributo ti_bene_servizio = 'B'
--
-- Date: 18/03/2003
-- Version: 4.10
--
-- Fix errore CINECA n. 578
-- Non deve essere riporato il debito precedente quando questo � inferiore a 26 EURO nella liquidazione
-- delle singole uo.
--
-- Date: 15/09/2003
-- Version: 4.11
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata � fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
-- Date: 06/11/2003
-- Version: 4.12
--
-- Ottimizzazione prestazioni nella ristampa dei registri IVA. Segnalazione del CINECA sulla lentezza
-- dell'operazione
--
-- Date: 10/12/2003
-- Version: 4.13
--
-- Richiesta CINECA n. 628. Revisione della memorizzazione delle autofatture nella liquidazione IVA.
-- Non deve essere pi� fatta la distinzione tra autofatture INTRA ed altre, tutto deve essere memorizzato
-- nel campo iva dovuta sulle autofatture.
--
-- Date: 29/01/2004
-- Version: 4.14
--
-- Richiesta CINECA n. 739. Revisione recupero record relativi ad iva ad esigibilit� differita, si
-- vogliono includere non solo i record con data esigibilit� compresa nell'intervallo di selezione
-- ma anche quelli retrodatati e mai processati da liquidazione.
-- Interventi su:
-- 1) Calcolo della liquidazione. Routine calcolaLiquidazione
-- 2) Scrittura record processati dalla liquidazione in REPORT_DETTAGLIO. Routine insDettaglioPerLiquidazione.
-- 3) Sezione II della stampa esigibilita differita. Routine insFattureIvaDifferita
--
-- Date: 24/08/2005
-- Version: 4.15
--
-- Corretta anomalia: veniva sommata nello stesso campo l'IVA sulle fatture dell'esercizio in corso divenute
-- esigibili nel periodo con quelle di esercizi precedenti divenute esigibili nel periodo
--
--==================================================================================================
--
--
-- Constants:

   -- Variabili globali

   num_elemento_attuale NUMBER:=0;
   errMsg VARCHAR2(200);
   i BINARY_INTEGER;

   -- variabili recupero fetch e definizione cursore variabile

   cv_cd_cds VARCHAR2(30);
   cv_cd_uo  VARCHAR2(30);
   cv_esercizio NUMBER(4);
   cv_cd_cds_origine VARCHAR2(30);
   cv_cd_uo_origine VARCHAR2(30);
   cv_cd_tipo_sezionale VARCHAR2(10);
   cv_ti_fattura CHAR(1);
   cv_data_registrazione DATE;
   cv_numero_progressivo VARCHAR2(10);
   cv_riga_documento VARCHAR2(10);
   cv_data_emissione DATE;
   cv_numero_fattura VARCHAR2(20);
   cv_protocollo_iva NUMBER;
   cv_protocollo_iva_gen NUMBER;
   cv_comm_ist_testata VARCHAR2(10);
   cv_codice_anagrafico VARCHAR2(10);
   cv_ragione_sociale VARCHAR2(200);
   cv_imponibile_dettaglio NUMBER(15,2);
   cv_iva_dettaglio NUMBER(15,2);
   cv_iva_indetraibile_dettaglio NUMBER(15,2);
   cv_totale_dettaglio NUMBER(15,2);
   cv_comm_ist_dettaglio VARCHAR2(10);
   cv_codice_iva VARCHAR2(10);
   cv_percentuale_iva NUMBER(15,2);
   cv_descrizione_iva VARCHAR2(200);
   cv_fl_iva_detraibile CHAR(1);
   cv_percentuale_iva_detraibile NUMBER(15,2);
   cv_gruppo_iva VARCHAR2(50);
   cv_descrizione_gruppo_iva VARCHAR2(200);
   cv_intra_ue CHAR(1);
   cv_extra_ue CHAR(1);
   cv_bolla_doganale CHAR(1);
   cv_spedizioniere CHAR(1);
   cv_codice_valuta VARCHAR2(10);
   cv_importo_valuta NUMBER(15,2);
   cv_esigibilita_diff CHAR(1);
   cv_data_esigibilita_diff DATE;
   cv_iva_esigibile NUMBER(15,2);
   cv_imponibile_split_payment NUMBER(15,2);
   cv_iva_split_payment NUMBER(15,2);

   cv_tipo_documento VARCHAR2(100);

   cv_codice_iva_a VARCHAR2(10);
   cv_descrizione_iva_a VARCHAR2(200);
   cv_gruppo_iva_a VARCHAR2(50);
   cv_descrizione_gruppo_iva_a VARCHAR2(200);
   cv_imponibile_a NUMBER(15,2);
   cv_iva_a NUMBER(15,2);
   cv_totale_a NUMBER(15,2);
   cv_iva_indetraibile_a NUMBER(15,2);
   cv_imponibile_split_payment_a NUMBER(15,2);
   cv_iva_split_payment_a NUMBER(15,2);

   cv_codice_iva_b VARCHAR2(10);
   cv_descrizione_iva_b VARCHAR2(200);
   cv_gruppo_iva_b VARCHAR2(50);
   cv_imponibile_b NUMBER(15,2);
   cv_iva_b NUMBER(15,2);
   cv_totale_b NUMBER(15,2);
   cv_iva_indetraibile_b NUMBER(15,2);
   cv_imponibile_split_payment_b NUMBER(15,2);
   cv_iva_split_payment_b NUMBER(15,2);

   cv_iva_deb NUMBER(15,2);
   cv_iva_cre NUMBER(15,2);
   cv_iva_deb_cred NUMBER(15,2);
   cv_iva_deb_cred_per_prec NUMBER(15,2);
   cv_cred_iva_comp_detr NUMBER(15,2);
   cv_int_deb_liq_trim NUMBER(15,2);
   cv_cred_iva_spec_detr NUMBER(15,2);
   cv_iva_da_versare NUMBER(15,2);
   cv_iva_versata NUMBER(15,2);
   cv_cred_iva_infrann_rimb NUMBER(15,2);
   cv_cred_iva_infrann_comp NUMBER(15,2);
   cv_iva_credito_no_prorata NUMBER(15,2);

   cv_acconto_iva_versato NUMBER(15,2);
   cv_sezione_liquidazione VARCHAR2(10);
   cv_tipo_autofattura VARCHAR2(10);
   cv_tipo_documento_ft_pas VARCHAR2(10);
   cv_perc_prorata_detraibile NUMBER(5,2);
   cv_gestione_prorata VARCHAR2(1);
   cv_esercizio_euro VARCHAR2(1);
   cv_fl_split_payment CHAR(1);

   cv_numero_dichiarazione VARCHAR2(20);
   cv_data_inizio_validita DATE;
   cv_data_comunicazione_dic DATE;
   cv_data_comunicazione_rev DATE;
   cv_note VARCHAR2(200);

   cv_acquisto_vendita CHAR(1);
   cv_acquisto_vendita_a CHAR(1);
   cv_acquisto_vendita_b CHAR(1);
   cv_provvisorio_definitivo CHAR(1);
   cv_provvisorio_definitivo_a CHAR(1);
   cv_provvisorio_definitivo_b CHAR(1);
   cv_sequenza NUMBER;
   cv_split_payment CHAR(1);

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures:

-- Inserimento record per titolo report (comune a tutte le stampe IVA)

   PROCEDURE insTitColReportGenerico
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       aTitoloReport1 VARCHAR2,
       aTitoloReport2 VARCHAR2,
       aTitoloReport3 VARCHAR2,
       aDenominazioneEnte VARCHAR2,
       aPIvaEnte VARCHAR2,
       aEsercizioRif INTEGER,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2,
       aTipoReport VARCHAR2,
       aTipoRegistro VARCHAR2
      );

-- Inserimento del frontespizio per il riepilogo sezionali in caso di stampa multipla degli stessi

   PROCEDURE insProspettiPerRegistri
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       aPasso INTEGER,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCodiceSezionale VARCHAR2,
       aDescrizioneSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoReport VARCHAR2,
       aTipoRegistro VARCHAR2,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2
      );

-- Stampa corpo del registro IVA sia acquisti che vendite

   PROCEDURE insFatturePerRegistri
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       aPasso INTEGER,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCodiceSezionale VARCHAR2,
       aDescrizioneSezionale VARCHAR2,
       aTipoIstituzCommerc CHAR,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aRistampa VARCHAR2,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2,
       aMonetaItalia VARCHAR2,
       aTipoReportStato VARCHAR2
      );

-- Assegnazione del numero di protocollo IVA per fatture passive e autofatture in sede di stampa
-- registri IVA

   PROCEDURE insProtFatturePassAuto
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aTiIstituzCommerc CHAR,
       aTipoReport VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTiDocumento VARCHAR2
      );

-- Composizione di statement sql di tipo dinamico
-- aTiStatementSql = '01' --> SELECT per aggiornamento protocolli sui documenti

   FUNCTION componiStatementSql
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aTiIstituzCommerc CHAR,
       aTipoReport VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTiDocumento VARCHAR2,
       eseguiLock CHAR,
       aTiStatementSql VARCHAR2
      ) RETURN VARCHAR2;

-- Stampa del riepilogo per aliquota in stampa registri iva

   PROCEDURE insRiepilogoIvaPerRegistri
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       aPasso INTEGER,
       aEsercizio NUMBER,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aCdTipoSezionale VARCHAR2
      );

-- Stampa RIEPILOGATIVO per UO con dettaglio per sezionale

   PROCEDURE insFatturePerRiepilogativi
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       aPasso INTEGER,
       descrizioneSezionale VARCHAR2,
       aTiIstituzCommerc CHAR,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2
      );

-- Stampa del registro riepilogativo senza dettaglio per sezionale. Si esegue solo se risultano
-- elaborati pi� di un sezionale

   PROCEDURE insTotaliPerRiepilogativi
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       aPasso INTEGER,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2
      );

   PROCEDURE insFattureIvaDifferita
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       descrizioneSezionale VARCHAR2,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aRistampa VARCHAR2,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2
      );

   PROCEDURE insTotaliSezionaleIvaDifferita
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2
      );

   PROCEDURE insTotaliIvaDifferita
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2
      );

   PROCEDURE insTotaliIvaDiffPerCodIVA
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2
      );

   PROCEDURE insRiepilogoIvaDifferita
      (
       repID INTEGER,
       aSequenza IN OUT INTEGER,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       totaleImponibile NUMBER,
       totaleIva NUMBER,
       totaleIvaIndetraibile NUMBER,
       totaleFattura NUMBER,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2
      );

-- Lettura della liquidazione

   PROCEDURE letturaLiquidazione
      (
       repID INTEGER,
       aCdCds VARCHAR2,
       aEsercizio NUMBER,
       aCdUo VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2,
       inGruppoReport VARCHAR2
      );

-- Calcolo della liquidazione UO

   PROCEDURE calcolaLiquidazione
      (
       repID INTEGER,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       acquistiVendite INTEGER,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2,
       aEsercizioEuro VARCHAR2,
       aGestioneProrata VARCHAR2,
       aEsercizioReale NUMBER,
       inGruppoReport VARCHAR2
      );

-- Calcolo della liquidazione ENTE

   PROCEDURE calcolaLiquidazioneEnte
      (
       repID INTEGER,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2,
       aEsercizioEuro VARCHAR2,
       aGestioneProrata VARCHAR2,
       aEsercizioReale NUMBER,
       inGruppoReport VARCHAR2
      );

-- Scrittura della liquidazione

   PROCEDURE scritturaLiquidazione
      (
       repID INTEGER,
       aCdCds VARCHAR2,
       aEsercizio NUMBER,
       aCdUo VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       gruppoStm CHAR,
       sottoGruppoStm CHAR,
       descrizioneGruppoStm VARCHAR2,
       inGruppoReport VARCHAR2
      );

-- Ritorna l'esistenza o meno di una liquidazione definitiva

   FUNCTION chkEsisteLiquidazioneDef
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aEsercizio NUMBER,
       aDataInizio DATE,
       aDataFine DATE,
       aGruppoReport VARCHAR2
      ) RETURN VARCHAR2;


--
--   PROCEDURE insRiportoDatiIntento
--      (
--       repID INTEGER,
--       aSequenza IN OUT INTEGER,
--       aPasso INTEGER,
--       aCdCdsOrigine VARCHAR2,
--       aCdUoOrigine VARCHAR2,
--       aEsercizio NUMBER,
--       aCdTipoSezionale VARCHAR2,
--       aDataInizio DATE,
--       aDataFine DATE,
--       aTipoRegistro VARCHAR2,
--       aRistampa VARCHAR2,
--       gruppoStm CHAR,
--       sottoGruppoStm CHAR,
--       descrizioneGruppoStm VARCHAR2
--      );
--
--   PROCEDURE insRevocaDatiIntento
--      (
--       repID INTEGER,
--       aSequenza IN OUT INTEGER,
--       aPasso INTEGER,
--       aCdCdsOrigine VARCHAR2,
--       aCdUoOrigine VARCHAR2,
--       aEsercizio NUMBER,
--       aCdTipoSezionale VARCHAR2,
--       aDataInizio DATE,
--       aDataFine DATE,
--       aTipoRegistro VARCHAR2,
--       aRistampa VARCHAR2,
--       gruppoStm CHAR,
--       sottoGruppoStm CHAR,
--       descrizioneGruppoStm VARCHAR2
--      );
--
--FUNCTION insAnnotazioniLibere
--    (
--     repID INTEGER,
--     aSequenza IN OUT INTEGER,
--     aPasso INTEGER,
--     codiceSezionale VARCHAR2,
--     prefissoSezionale VARCHAR2,
--     descrizioneSezionale VARCHAR2,
--     aCodiceEsercizio VARCHAR2,
--     aDataInizio DATE,
--     aDataFine DATE,
--     aTipoRegistro VARCHAR2,
--     gruppoStm CHAR,
--     sottoGruppoStm CHAR,
--     descrizioneGruppoStm VARCHAR2
--    ) RETURN NUMBER;
--
procedure insDettaglioPerRegistri
   (
    repID INTEGER,
    aPasso INTEGER,
    descrizioneSezionale VARCHAR2,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCodiceSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aUtente VARCHAR2,
    aTipoReportStato VARCHAR2
  ) ;

   PROCEDURE insRecLiquidazioneDef
      (
       repID INTEGER,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aDataInizio DATE,
       aDataFine DATE,
       utente VARCHAR2,
       inGruppoReport VARCHAR2
      );

   PROCEDURE insDettaglioPerLiquidazione
      (
       repID INTEGER,
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       utente VARCHAR2,
       inGruppoReport VARCHAR2
      );

PROCEDURE InsDataDiffFatturePassAuto
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE );
END CNRCTB260;
