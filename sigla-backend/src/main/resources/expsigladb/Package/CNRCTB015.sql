--------------------------------------------------------
--  DDL for Package CNRCTB015
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB015" as
--
-- CNRCTB015 - Package di gestione tabelle di CONFIGURAZIONE GENERALI: CONFIGURAZIONE_CNR LUNGHEZZA_CHIAVI
-- Date: 10/07/2003
-- Version: 1.10
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 05/10/2001
-- Version: 1.1
-- Creazione
--
-- Date: 05/10/2001
-- Version: 1.1
-- Agiunta gestione copia dati interesercizio per tabella LUNGHEZZA_CHIAVI
--
-- Date: 18/10/2001
-- Version: 1.2
-- Aggiunto il metodo per il controllo di esistenza dell'esercizio indiretto in tabella esercizio
-- Nuovi metodi per lettura da tabella CONFIGURAZIONE_CNR
--
-- Date: 08/11/2001
-- Version: 1.3
-- Eliminazione esercizio da STO
--
-- Date: 16/11/2001
-- Version: 1.4
-- Enforcing error handling
--
-- Date: 21/01/2002
-- Version: 1.5
-- Lettura di val02 da CONFIGURAZIONE_CNR
--
-- Date: 25/03/2002
-- Version: 1.6
-- Funzioni di lettura dei dati numerici di CONFIGURAZIONE_CNR
--
-- Date: 18/07/2002
-- Version: 1.7
-- Aggiornamento documentazione
--
-- Date: 01/12/2002
-- Version: 1.8
-- Aggiunto metodo per estrazione val03 da configurazione CNR
--
-- Date: 24/01/2003
-- Version: 1.9
-- Aggiunto il metodo di estrazione im02 da configurazione CNR
--
-- Date: 10/07/2003
-- Version: 1.10
-- Aggiunto il metodo di estrazione dt01 da configurazione CNR
--
-- Constants:
--
-- Functions e Procedures:

-- Estrae il valore val01 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il primo valore di tipo stringa del record trovato (val01)

 Function getVal01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;

-- Si assume che eserecizio sia significativo
 function getVal01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;

-- Estrae il valore im01 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il primo valore di tipo stringa del record trovato (im01)

 function getIm01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number;

-- Si assume che eserecizio sia significativo
 function getIm01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number;

-- Estrae il valore im02 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il primo valore di tipo stringa del record trovato (im02)

 function getIm02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number;

-- Si assume che eserecizio sia significativo
 function getIm02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number;


-- Ritorna il secondo valore di tipo stringa del record trovato (val02) per esercizio

 function getVal02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;
-- Si assume che eserecizio sia significativo
 function getVal02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;

-- Ritorna il terzo valore di tipo stringa del record trovato (val03) per esercizio

 function getVal03PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;
-- Si assume che eserecizio sia significativo
 function getVal03PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;

-- Estrae il valore dt01 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il primo valore di tipo data del record trovato (dt01)

 function getDt01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date;

-- Si assume che eserecizio sia significativo
 function getDt01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date;

-- Estrae il valore dt02 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il secondo valore di tipo data del record trovato (dt02)

 function getDt02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date;

-- Si assume che eserecizio sia significativo
 function getDt02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date;

-- Copia dati interesercizio per tabella LUNGHEZZA_CHIAVI
-- aEsDest -> row type esercizio di destinazione
--            dal rowtype vengono letti anche utuvu/utcr/duva/dacr

 procedure coie_LUNGHEZZA_CHIAVI (aEsDest esercizio%rowtype);

--
-- Funzione che verifica l'esistenza di un esercizio in tabella esercizio indirettamente, controllando se
-- esistono in lunghezza_chiavi records per quell'esercizio
-- Tale metodo indiretto viene chiamato quando non sia possibile rileggere nel contesto di chiamata al metodo
-- la tabella esercizio (ad esempio nell'ambito di chiamata via trigger)
--

 function testEsercizioGiaPresente(aEsDest esercizio%rowtype) return boolean;

-- Verifica se per il CDS occorre utilizzare la linea dedicata per Partita di Giro dai Parametri CDS
-- (Entrata / Spesa a seconda del tipo gestione)
 Function UtilizzaGAEdedicataPgiroCDS (aEsercizio NUMBER, aCDS VARCHAR2, ati_gestione CHAR) return Boolean;

-- Estrae la linea dedicata per Partita di Giro dai Parametri CDS (Entrata / Spesa a seconda del tipo gestione)
 Function get_LINEA_PGIRO_cds (aEsercizio NUMBER, aCDS VARCHAR2, ati_gestione CHAR) return Linea_attivita%Rowtype;


end;
