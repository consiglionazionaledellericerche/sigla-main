--------------------------------------------------------
--  DDL for Package CNRCTB040
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB040" as
--
-- CNRCTB040 - Package di gestione applicativa accertamenti
-- Date: 13/07/2006
-- Version: 2.4
--
-- Package per la gestione applicativa dell'accertamento
--
-- Dependency: CNRCTB 001/010/015/018/020/035 IBMERR 001
--
-- History:
--
-- Date: 09/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 10/05/2002
-- Version: 1.1
-- Gestione partita di giro
--
-- Date: 11/05/2002
-- Version: 1.2
-- Test motore
--
-- Date: 13/05/2002
-- Version: 1.3
-- Modificata interfaccia pgiro
--
-- Date: 10/06/2002
-- Version: 1.4
-- Carezione partita di giro a partire dalla spesa
--
-- Date: 21/06/2002
-- Version: 1.5
-- Fix su date
--
-- Date: 24/06/2002
-- Version: 1.6
-- Aggiunto metodo per genrazione di accertamento con una scadenza e dettagli scadenza
--
-- Date: 26/06/2002
-- Version: 1.7
-- Sistemazione motore di creazione accertamenti CNR per CDS
--
-- Date: 17/07/2002
-- Version: 1.8
-- Troncamento dt_registrazione e date scadenza ACCERTAMENTO e derivati
--
-- Date: 17/07/2002
-- Version: 1.9
-- Eliminazione del metodo di creaziione impegno su partita di giro
--
-- Date: 18/07/2002
-- Version: 2.0
-- Aggiornamento documentazione
--
-- Date: 26/09/2002
-- Version: 2.1
-- Fix su aggiornamento dei saldi accertamento a livello di testata e non di dettaglio minimo
--
-- Date: 14/10/2002
-- Version: 2.2
-- Creazione partita di giro tronca in parte spese aperta da entrata
--
-- Date: 17/10/2002
-- Version: 2.3
-- Impostata la data di cancellazione su obbligazione di contropartita creata da accertamento pgiro tronco
--
-- Date: 13/07/2006
-- Version: 2.4
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

-- Functions e Procedures:

-- Crea un accertamento con una sola scadenza su bilancio CNR per CDS

 procedure creaAccertamento(
  isControlloBloccante boolean,
  aAcc IN OUT accertamento%rowtype,
  aScadenza1 in out accertamento_scadenzario%rowtype,
  aDettScadenza1 in out CNRCTB035.scadVoceListE
 );

-- Crea un accertamento su bilancio CNR per CDS

 procedure creaAccertamento(
  isControlloBloccante boolean,
  aAcc IN OUT accertamento%rowtype,
  aScadenza1 in out accertamento_scadenzario%rowtype,
  aDettScadenza1 in out CNRCTB035.scadVoceListE,
  aScadenza2 in out accertamento_scadenzario%rowtype,
  aDettScadenza2 in out CNRCTB035.scadVoceListE,
  aScadenza3 in out accertamento_scadenzario%rowtype,
  aDettScadenza3 in out CNRCTB035.scadVoceListE,
  aScadenza4 in out accertamento_scadenzario%rowtype,
  aDettScadenza4 in out CNRCTB035.scadVoceListE,
  aScadenza5 in out accertamento_scadenzario%rowtype,
  aDettScadenza5 in out CNRCTB035.scadVoceListE,
  aScadenza6 in out accertamento_scadenzario%rowtype,
  aDettScadenza6 in out CNRCTB035.scadVoceListE,
  aScadenza7 in out accertamento_scadenzario%rowtype,
  aDettScadenza7 in out CNRCTB035.scadVoceListE,
  aScadenza8 in out accertamento_scadenzario%rowtype,
  aDettScadenza8 in out CNRCTB035.scadVoceListE,
  aScadenza9 in out accertamento_scadenzario%rowtype,
  aDettScadenza9 in out CNRCTB035.scadVoceListE,
  aScadenza10 in out accertamento_scadenzario%rowtype,
  aDettScadenza10 in out CNRCTB035.scadVoceListE
 );

-- Creazione accertamenti su partita di giro

 procedure creaAccertamentoPgiro(
  isControlloBloccante boolean,
  aAcc IN OUT accertamento%rowtype,
  aAccScad IN OUT accertamento_scadenzario%rowtype,
  aObb OUT obbligazione%rowtype,
  aObbScad OUT obbligazione_scadenzario%rowtype,
  aDtScadenza date
 );

-- Creazione accertamenti su partita di giro con controparte obbligazione annullata

 procedure creaAccertamentoPgiroTronc(
  isControlloBloccante boolean,
  aAcc IN OUT accertamento%rowtype,
  aAccScad IN OUT accertamento_scadenzario%rowtype,
  aObb OUT obbligazione%rowtype,
  aObbScad OUT obbligazione_scadenzario%rowtype,
  aDtScadenza date
 );

end;
