--------------------------------------------------------
--  DDL for Package CNRCTB110
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB110" as
--
-- CNRCTB110 - Package gestione applicativa DOCUMENTO GENERICO
--
-- Date: 13/07/2006
-- Version: 1.7
--
-- Dependency: CNRCTB 100
--
-- History:
--
-- Date: 12/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 26/05/2002
-- Version: 1.1
-- Calcolo automatico importo di testata
--
-- Date: 18/07/2002
-- Version: 1.2
-- Aggiornamento documentazione
--
-- Date: 25/07/2002
-- Version: 1.3
-- Creato metodo per aggiornamento scadenza obbligazione/accertamento per creazione generico
--
-- Date: 10/11/2002
-- Version: 1.4
-- Inserimento del periodo di competenza se non specificato (data inizio fine = data odierna)
--
-- Date: 07/01/2003
-- Version: 1.5
-- Aggiunto il controllo di compatibilità fra il terzo del documento amm e e il terzo del doc contabile
--
-- Date: 13/06/2003
-- Version: 1.6
-- Utilizzo corretto delle coordinate di obbligazioni e accertamenti in riga doc. generico
-- Fix descrizione errore terzo incompatibile
--
-- Date: 13/07/2006
-- Version: 1.7
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:
--

-- Functions e Procedures:

--
-- Crea un documento generico partendo dalla testata de dalla collezione delle righe
-- Il metodo si limita ad effettuare la numerazione del documento
-- Il metodo inserisce testata e righe del documento
--
--
 procedure creaGenerico(aGen in out documento_generico%rowtype, aListRighe in out CNRCTB100.docGenRigaList);

--
-- Crea un documento generico partendo dalla testata de dalla collezione delle righe
-- Il metodo non si limita ad effettuare la numerazione del documento ma anche ad aggiornare il saldo collegato a documenti
-- amministrativi dell'obbligazione o accertamento collegati.
-- Il metodo inserisce testata e righe del documento
--
--
 procedure creaGenericoAggObbAcc(aGen in out documento_generico%rowtype, aListRighe in out CNRCTB100.docGenRigaList);

 procedure verificaTerzo( aRiga documento_generico_riga%rowtype);


END;
