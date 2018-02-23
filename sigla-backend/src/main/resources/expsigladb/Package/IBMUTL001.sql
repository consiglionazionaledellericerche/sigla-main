--------------------------------------------------------
--  DDL for Package IBMUTL001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL001" as
--==================================================================================================
--
-- IBMUTL001 - Package di utilita generiche
--
-- Date: 17/03/2004
-- Version: 2.11
--
-- Procedure e Funzioni di servizio generale
--
-- History:
--
-- Date: 20/09/2001
-- Version: 1.1
-- Aggiunta la funzione di conteggio delle occorrenze di una stringa in un'altra
--
-- Date: 16/03/2002
-- Version: 1.2
-- Allineamento con correttiva/evolutiva 1.1.0
-- Funzione di check stringa numerica
--
-- Date: 21/03/2002
-- Version: 1.3
-- Inserimento funzione getLocalTransactionID
-- Funzione che restituisce l'identificativo della Transazione Oracle in uso
--
-- Date: 25/03/2002
-- Version: 1.4
-- Eliminazione output da conta occorrenze
--
-- Date: 26/03/2002
-- Version: 1.5
-- Aggiunti metodi manipolazione stringhe (tokenizzazione, stripping, frazionamento)
--
-- Date: 26/03/2002
-- Version: 1.6
-- Aggiunto metodo di replace token
--
-- Date: 17/05/2002
-- Version: 1.7
-- Aggiunti metodi per dynamic sql
--
-- Date: 26/06/2002
-- Version: 1.8
--
-- Aggiunti metodi gestione date
--
-- Date: 28/06/2002
-- Version: 1.9
--
-- Fix errore in calcolo del numero dei mesi che intercorrono tra due date
--
-- Date: 10/07/2002
-- Version: 2.0
--
-- Fix errore di conversione della data
--
-- Date: 26/07/2002
-- Version: 2.1
--
-- Aggiunte routine per calcolo differenza giorni e rettifica calcolo matematico delle date
--
-- Date: 01/08/2002
-- Version: 2.2
--
-- Fix errori in routine date
--
-- Date: 05/08/2002
-- Version: 2.3
--
-- Inserita routine per calcolo giorni tra due date in anno commerciale
--
-- Date: 17/09/2002
-- Version: 2.4
--
-- Inserita function inlettere() per la conversione di un numero in lettere
--
-- Date: 15/10/2002
-- Version: 2.5
--
-- Fix in calcolo numero giorni tra due date in anno commerciale se entrambe le date rappresentano
-- il giorno 15 di un mese
--
-- Date: 18/03/2003
-- Version: 2.6
--
-- Aggiunta procedura di deferred commit
--
-- Date: 02/04/2003
-- Version: 2.7
--
-- Aggiunto callback di recupero connessione
-- Aggiunto metodo lock della transazione per deferred commit
--
-- Date: 03/04/2003
-- Version: 2.8
-- Fix su unlock transation per on-line
--
-- Date: 20/05/2003
-- Version: 2.9
-- Corretta funzione conversione in lettere (lunghezza stringa)
--
-- Date: 17/02/2004
-- Version: 2.10
-- Corretta funzione isNumeric: errore 765
--
-- Date: 17/03/2004
-- Version: 2.11
--
-- Aggiunta routine per tornare il mese data una data
--
-- Date: 27/02/2005
-- Version: 2.12
--
-- Aggiunta funzione per tornare la data di refresh del DB
--
--==================================================================================================
--
-- Constants:
--

   isIntervalloDateMensile CONSTANT CHAR(1):='Y';
   isNotIntervalloDateMensile CONSTANT CHAR(1):='N';
   isIntervalloDateMese CONSTANT CHAR(1):='Y';
   isNotIntervalloDateMese CONSTANT CHAR(1):='N';

type stringList is table of varchar2(1000) index by binary_integer;

-- per le look-up-table e per la collezione di token di 3 elementi
TYPE V_ARR IS VARRAY(20) OF VARCHAR2(20);

-- Functions e Procedures:
 -- Callbakc chiamato ad ogni richiesta connessione a DATASOURCE
 procedure trace_user_connection(aUser varchar2, aTSNow date, aHTTPSID varchar2);
 -- Funzione di lock applicativo della transazione per il deferred commit
 procedure LOCK_TRANSACTION;
 -- Funzione di commit deferred per la corretta chiusura di transazione applicative
 PROCEDURE DEFERRED_COMMIT;
 -- Funzione di unlock della transazione per invocazione client
 PROCEDURE UNLOCK_TRANSACTION;

 -- Dot Concat: concatena aS1 ed aS2 con un punto separatore
 function dotConcat(aS1 varchar2, aS2 varchar2) return varchar2;

 -- Conta il numero di occorrenze della stringa aPattern in aOrigine
 function contaOccorrenze(aOrigine varchar2, aPattern varchar2) return NUMBER;

 -- Ritorna 'Y' se la stringa aStr rappresenta un numero
 function isNumeric(aStr varchar2) return char;

  -- Restituisce il LOCAL_TRANSACTION_ID
 function getLocalTransactionID(createTransaction BOOLEAN) return VARCHAR2;

 -- Ritorna la stringa epurata delle occorrenze del pattern aPattern
 function strip(aStr varchar2, aPattern varchar2) return varchar2;

 -- Ritorna la stringa epurata delle occorrenze del pattern aPattern
 function replaceToken(aStr varchar2, aWhat varchar2, aWith varchar2) return varchar2;

 -- Funzioni di spezzamento
 function rightOfFirst(aStr varchar2, aPattern varchar2) return varchar2;
 function leftOfFirst(aStr varchar2, aPattern varchar2) return varchar2;
 function rightOfLast(aStr varchar2, aPattern varchar2) return varchar2;
 function leftOfLast(aStr varchar2, aPattern varchar2) return varchar2;

 function getErrorMessage Return varchar2;
 -- Function che ritorna una stringList di tokens estratti da aStr con spezzmento aPattern
 function tokenize(aStr varchar2, aPattern varchar2) return stringList;

 -- Ritorna la stringa da utilizzare in una query dinamica per la data come timestamp o date

 function asDynTimestamp(aTS date) return varchar2;
 function asDynDate(aTS date) return varchar2;

 -- Data una data ritorna 'Y' o 'N' se la data è un inizio

 FUNCTION isFirstDayOfMonth(aData DATE) RETURN CHAR;

 -- Data una data ritorna la corrispondente data di inizio mese

 FUNCTION getFirstDayOfMonth(aData DATE) RETURN DATE;

 -- Data una data ritorna 'Y' o 'N' se la data è un fine mese

 FUNCTION isLastDayOfMonth(aData DATE) RETURN CHAR;

 -- Data una data ritorna la corrispondente data di fine mese

 FUNCTION getLastDayOfMonth(aData DATE) RETURN DATE;

 -- Data una data ritorna l'intero rappresentante il primo giorno del mese

 FUNCTION getDayFirstDayOfMonth(aData DATE) RETURN NUMBER;

 -- Data una data ritorna l'intero rappresentante l'ultimo giorno del mese

 FUNCTION getDayLastDayOfMonth(aData DATE) RETURN NUMBER;

-- Data una data ritorna l'intero rappresentante il giorno del mese

 FUNCTION getDayOfDate(aData DATE) RETURN NUMBER;

-- Data una data ritorna l'intero rappresentante il giorno del mese

 FUNCTION getMonthOfDate(aData DATE) RETURN NUMBER;

-- Data una data ritorna l'intero rappresentante l'anno

 FUNCTION getYearOfDate(aData DATE) RETURN NUMBER;

 -- Ritorna 'Y' o 'N' se la distanza tra due date rappresenta un intervallo mensile.

 FUNCTION isDifferenzaMensile(aDataDa DATE, aDataA DATE) RETURN CHAR;

 -- Ritorna 'Y' o 'N' se la distanza tra due date rappresenta un intervallo mensile.

 FUNCTION isDifferenzaMese(aDataDa DATE, aDataA DATE) RETURN CHAR;

 -- Ritorna la data sommata o sottratta di un numero di mesi

 FUNCTION getAddMonth(aData DATE, aNumero INTEGER) RETURN DATE;

 -- Ritorna i mesi di differenza tra due date.

 FUNCTION getMonthsBetween (aDataDa DATE, aDataA DATE) RETURN NUMBER;

 -- Ritorna i giorni di differenza tra due date.

 FUNCTION getDaysBetween (aDataDa DATE, aDataA DATE) RETURN NUMBER;

 -- Ritorna i giorni di differenza tra due date (anno commerciale).

 FUNCTION getDaysCommBetween (aDataDa DATE, aDataA DATE) RETURN NUMBER;

-- per convertire un numero in lettere nella parte intera e
-- concatenare la parte decimale in cifre
FUNCTION inlettere(valore NUMBER) RETURN VARCHAR2;

-- per convertire un NUMBER in VARCHAR2 con il formato specificato
FUNCTION to_varchar(valore NUMBER) RETURN VARCHAR2;

-- per convertire un intero in lettere
FUNCTION converti(numero VARCHAR2) RETURN VARCHAR2;
--
-- per convertire in lettere i token di 3 elementi
FUNCTION parse3Digit(token VARCHAR2,posizione INT) RETURN VARCHAR2;
--
-- per suddividere l'intero in tokens di 3 elementi
FUNCTION spezzaGruppiDi3(numero VARCHAR2) RETURN V_ARR;
--
-- Ritorna la data di refresh del DB
Function getDBRefreshDate RETURN DATE;

end;
