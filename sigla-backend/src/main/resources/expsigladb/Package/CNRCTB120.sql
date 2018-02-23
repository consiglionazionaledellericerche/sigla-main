--------------------------------------------------------
--  DDL for Package CNRCTB120
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB120" AS
--==================================================================================================
--
-- CNRCTB120 - Package gestione applicativa FATTURE
--
-- Date: 15/09/2003
-- Version: 4.3
--
-- Dependency: CNRCTB 100/255
--
-- History:
--
-- Date: 25/09/2002
-- Version: 1.0
--
-- Creazione
--
-- Date: 30/09/2002
-- Version: 1.1
--
-- Aggiunte le funzioni di lettura testata fattura, lettura sezionale e cancellazione fattura
--
-- Date: 14/11/2002
-- Version: 1.2
--
-- Aggiunta la funzione di lettura dei protocolli correnti di un dato sezionale per esercizio e uo
-- Aggiunta la funzione di aggiornamento del numero corrente sul sezionale
--
-- Date: 24/01/2003
-- Version: 1.3
--
-- Modificata la semantica del campo corrente in sezionali: 0 rappresenta il corrente nel caso il sezionale
-- non sia stato ancora utilizzato: viene ora utilizzato corrente=-1 in variabili funzione getProtCorrenteSezionale
-- per indicare che il sezionale non è presente o non compatibile
--
-- Date: 03/02/2003
-- Version: 4.0
--
-- Inserita routine per recupero dell'informazione di U.O. priva di alcun riferimento ai sezionali
--
-- Date: 06/02/2003
-- Version: 4.1
--
-- Inserita routine di recupero dell'informazione che una fattura attiva è gestita ad esigibilità
-- differita con i dati relativi alla sua inclusione in una stampa definitiva di registri IVA o
-- liquidazione
--
-- Date: 10/02/2003
-- Version: 4.2
--
-- Inserita routine di recupero dei dati dalla tabella TIPO_SEZIONALE dato un sezionale
--
-- Date: 15/09/2003
-- Version: 4.3
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata è fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
--==================================================================================================

--
-- Constants:
--

   -- Variabili globali

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures:

-- Lettura di una testata di fattura passiva dai riferimento documento del terzo

   FUNCTION getTstFatturaDaRifTerzo
      (
       aCdTerzoFat VARCHAR2,
       aEsercizioFat NUMBER,
       aTiFat VARCHAR2,
       aNumeroFat VARCHAR2,
       eseguiLock CHAR
      ) RETURN FATTURA_PASSIVA%ROWTYPE;

-- Ritorna un codice di sezionale acquisti ordinario

   FUNCTION getSezionaleOrdinarioAcq
      (
       aCdCds VARCHAR2,
       aUo VARCHAR2,
       aTipoIstitComm VARCHAR2,
       aEsercizio NUMBER
      ) RETURN SEZIONALE.cd_tipo_sezionale%TYPE;


-- Ritorna un record di TIPO_SEZIONALE dato un sezionale

   FUNCTION getTipoSezionale
      (
       aCdSezionale VARCHAR2
      ) RETURN TIPO_SEZIONALE%ROWTYPE;

-- Eliminazione di una fattura

   PROCEDURE eliminaFatturaPassiva
      (
       aCdCds FATTURA_PASSIVA.cd_cds%TYPE,
       aCdUo FATTURA_PASSIVA.cd_unita_organizzativa%TYPE,
       aEsercizio FATTURA_PASSIVA.esercizio%TYPE,
       aPgFattura FATTURA_PASSIVA.pg_fattura_passiva%TYPE
      );

-- Crea una fattura passiva partendo dalla testata de dalla collezione delle righe
-- Il metodo si limita ad effettuare la numerazione del documento
-- Il metodo inserisce testata e righe del documento

   PROCEDURE creaFattura(aFatt in out fattura_passiva%rowtype, aListRighe in out CNRCTB100.fattPassRigaList);

-- Lettura del protocollo iva corrente dai diversi sezionali dati gli identificativi della UO, l'esercizio
-- e un tipo sezionale.

   PROCEDURE getProtCorrenteSezionale
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       eseguiLock CHAR,
       aProtCorrenteFT IN OUT NUMBER,
       aProtCorrenteNC IN OUT NUMBER,
       aProtCorrenteND IN OUT NUMBER,
       aProtCorrentePG IN OUT NUMBER
      );

-- Aggiornamento del valore corrente sul sezionale

   PROCEDURE upgSezionalePgCorrente
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aProtocolloFT NUMBER,
       aProtocolloNC NUMBER,
       aProtocolloND NUMBER,
       aProtocolloPG NUMBER
      );

-- Ritorna Y o N a seconda che la U.O. abbia o meno definito i sezionali

   FUNCTION getUoHaSezionali
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aEsercizio NUMBER
      ) RETURN VARCHAR2;

-- Recupero dell'informazione che una fattura attiva è gestita ad esigibilità differita con i dati
-- relativi alla sua inclusione in una stampa definitiva di registri IVA o liquidazione

   FUNCTION getFtAttivaDiffConIva
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aEsercizio NUMBER,
       aPgFattura NUMBER,
       aPgRiga NUMBER,
       eseguiLock VARCHAR2
      ) RETURN VARCHAR2;

FUNCTION getFtDiffConIvaPassiva
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aPgFattura NUMBER,
    aPgRiga NUMBER,
    eseguiLock VARCHAR2
   ) RETURN VARCHAR2;

   -- Lettura di una testata di fattura passiva dai riferimento documento del terzo
   FUNCTION getFatturaRiferimento
      (
       aEsercizio NUMBER,
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aCompenso NUMBER,
       eseguiLock CHAR
      ) RETURN FATTURA_PASSIVA%ROWTYPE;


END;
