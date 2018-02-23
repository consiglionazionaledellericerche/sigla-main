--------------------------------------------------------
--  DDL for Package CNRCTB008
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB008" as
--
-- CNRCTB008 - Package di gestione dell'esercizio
-- Date: 24/09/2004
-- Version: 1.10
--
-- Gestisce operationi e costanti legate alla tabella ESERCIZIO
--
-- Dependency:
--
-- History:
--
-- Date: 16/11/2001
-- Version: 1.0
-- Creazione
-- Date: 09/02/2002
-- Version: 1.1
-- Introdotte le costanti degli stati dell'esercizio
-- Date: 27/01/2002
-- Version: 1.2
-- Esercizio infinito impostato a 2100
-- Date: 27/10/2002
-- Version: 1.3
-- Esercizio di partenza in produzione dell'applicazione
-- Date: 12/12/2002
-- Version: 1.4
-- Introduzione funzione estrazione esercizio di partenza da configurazione CNR
--
-- Date: 03/06/2003
-- Version: 1.5
-- Introdotte funzioni getStatoEsercizio, isEsercizioAperto, isEsercizioChiuso
--
-- Date: 13/06/2003
-- Version: 1.6
-- Aggiunto metodo per la restituzione della data contabile in relazione all'esercizio
--
-- Date: 16/07/2003
-- Version: 1.7
-- Lo stato di esercizi precedenti a quello di partenza è sempre CHIUSO
--
-- Date: 03/09/2003
-- Version: 1.8
-- Aggiunto metodo isEsercizioChiuso/Aperto/YesNo per gestione applicativa
--
-- Date: 04/09/2003
-- Version: 1.9
-- Aggiunto metodo isEsChiusoPerAlmenoUnCdsYesNo per gestione applicativa
--
-- Date: 24/09/2004
-- Version: 1.10
-- Aggiunto metodo isApertoOChiuso
--
-- Constants:

-- Esercizio infinito
 ESERCIZIO_INFINITO CONSTANT NUMBER(4) := 2100;

 CONST_ESERCIZIO_SPECIALE CONSTANT VARCHAR2(50) := 'ESERCIZIO_SPECIALE';
 CONST_ESERCIZIO_PARTENZA CONSTANT VARCHAR2(50) := 'ESERCIZIO_PARTENZA';

-- Esercizio partenza
 function ESERCIZIO_PARTENZA return number;

-- Stati dell'esercizio
 STATO_INIZIALE CONSTANT VARCHAR2(1) := 'I';
 STATO_APERTURA_PDG CONSTANT VARCHAR2(1) := 'G';
 STATO_APERTURA CONSTANT VARCHAR2(1) := 'A';
 STATO_CHIUSURA_PROVVISORIA CONSTANT VARCHAR2(1) := 'P';
 STATO_CHIUSURA_DEFINITIVA CONSTANT VARCHAR2(1) := 'C';

-- Ritorna lo stato dell'esercizio per il Cds
function getStatoEsercizio (aEs number, aCdCds varchar2) return varchar2;

-- Verifica se lo stato APERTO dell'esercizio per il Cds
function isEsercizioAperto (aEs number, aCdCds varchar2) return boolean;

-- Ritorna true se lo stato dell'esercizio è APERTO o CHIUSO per il Cds
function isEsercizioApertoOChiuso (aEs number, aCdCds varchar2) return boolean;

-- Verifica se lo stato CHIUSO dell'esercizio per il Cds
function isEsercizioChiuso (aEs number, aCdCds varchar2) return boolean;

-- Verifica se l'esercizio è aperto ('Y') o meno ('N')
function isEsercizioApertoYesNo (aEs number, aCdCds varchar2) return varchar2;
-- Verifica se l'esercizio è chiuso ('Y') o meno ('N')
function isEsercizioChiusoYesNo (aEs number, aCdCds varchar2) return varchar2;

-- Verifica se l'esercizio è chiuso ('Y') per almeno un cds o meno ('N')
function isEsChiusoPerAlmenoUnCdsYesNo (aEs number) return varchar2;

-- Ritorna un timestamp costruito con le seguenti regole:
--
-- L'esercizio specificato = a quello di aTSNow
--    ritorna aTSNow
-- L'esercizio specificato + 1 = a qhello di aTSNow
--    ritorna un timestamp 31/12/<esercizio specificato> HHMISS (attuali)
-- Altrimenti solleva eccezione

function getTimestampContabile(aEs number, aTSNow date) return date;
-- Aggiunta funzione senza blocco se esercizio/cds non esistente - richiamata nella verifica delle contabilizzazioni con competenza in anni precedenti
-- alla creazione del cds
function isEsercizioApertoSenzaBlocco(aEs number, aCdCds varchar2) return boolean;
end;
