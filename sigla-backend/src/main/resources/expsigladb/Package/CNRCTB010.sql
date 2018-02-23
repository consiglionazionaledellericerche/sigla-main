--------------------------------------------------------
--  DDL for Package CNRCTB010
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB010" as
--
-- CNRCTB010 - Package di gestione delle linee di attivita (comuni e non) e insieme di linee di attivita
-- Date: 17/04/2002
-- Version: 2.2
--
-- L'aggiunzione un CDR
-- produce l'inserimento delle associazioni con tipi di linee di attivita proprie e di sistema.
--
-- Dependency: CNRCTB008 CNRCTB020 IBMUTL001 IBMERR001
--
-- History:
--
-- Date: 29/08/2001
-- Version: 1.0
-- Creazione
-- Date: 29/08/2001
-- Version: 1.1
-- Aggiunto aggiornamento indotto da inserimento(eliminazione di cdr
--
-- Date: 16/09/2001
-- Version: 1.2
-- Aggiunte tipo di linee di attivita speciali (sistema)/procedure di inserimento della linea di attivita
--
-- Date: 02/10/2001
-- Version: 1.3
-- Aggiunte metodi di creazione di dati di test in automatico
--
-- Date: 16/10/2001
-- Version: 1.4
-- Spostati in CNRTST010 i metodi di generazione dei dati di test
-- Gestione del tipo unita ENTE
--
-- Date: 18/10/2001
-- Version: 1.5
-- Aggiunti metodi per l'estrazione e creazione delle linee di attivita SAUOP
--
-- Date: 08/11/2001
-- Version: 1.6
-- Gestione della Linea di attivita
--
-- Date: 15/11/2001
-- Version: 1.7
-- Gestione dell'inserimento delle associazioni in automatico per tipi di la comuni e di sistema
-- Rimozione delle procedure di inseirmnto ed eliminazione delle linee di attivita comuni

-- Date: 16/11/2001
-- Version: 1.8
-- Fix errori per migrazione eliminazione esercizio da STO

-- Date: 20/02/2002
-- Version: 1.9
-- Gestione INSIEME e SEZIONE E/S in Linea di attivita e Tipo linea attivita

-- Date: 18/03/2002
-- Version: 2.0
-- Allineamento con versione 1.9.0 correttiva/evolutiva
-- Aggiunta gestione dell'insieme automatico in scarico verso area

-- Date: 21/03/2002
-- Version: 2.1
-- Tolto esercizio_inizio da risultato

-- Date: 17/04/2002
-- Version: 2.2
-- Richiesta CNR 129E - Numerazione insieme per CDR

-- Constants:

-- Tipologie di tipi di linee di attivita
--
-- Tipo LA comune
TI_TIPO_LA_COMUNE CONSTANT varchar2(1) := 'C';
-- Tipo LA di sistema
TI_TIPO_LA_SISTEMA CONSTANT varchar2(1) := 'S';
-- Tipo LA propria
TI_TIPO_LA_PROPRIA CONSTANT varchar2(1) := 'P';

-- Tipi di linee di attivita di SISTEMA
--
-- Tipo LA Spesa verso UO Personale
TI_LA_SAUOP CONSTANT varchar2(10):='SAUOP';
-- Tipo LA Spesa verso altra UO
TI_LA_SAUO CONSTANT varchar2(10):='SAUO';
-- Tipo LA Costo verso altro CDR
TI_LA_CSSAC CONSTANT varchar2(10):='CSSAC';
-- Tipo LA Propria
TI_LA_PROP CONSTANT varchar2(10):='PROP';

-- Sezione linee di attivita
TI_GESTIONE_SPESE CONSTANT CHAR(1) := 'S';
TI_GESTIONE_ENTRATE CONSTANT CHAR(1) := 'E';

-- Denominazione linea di attivita SPESE PER COSTI ALTRUI
SPESE_PER_COSTI_ALTRUI CONSTANT varchar2(300) := 'Spese per costi altrui';

LUNGHEZZA_COD_INSIEME_LA CONSTANT NUMBER := 8;

-- Descrizione insieme collegamento entrate CSSAC/spese automatico in AREA
DESC_INS_SCR_N5_AREA CONSTANT varchar2(200) := 'Insieme di collegamento entrata CSSAC natura 5 con spese dettagli scaricati su AREA';

-- Functions  Procedures:

-- Azioni effettuate alla creazione di un cdr
-- Vengono Aggiunte le associazioni di quel cdr con tutte le linee di attivita proprie e di sistema
-- aCdr -> row type cdr proveniente da trigger di inserimento del CDR

 procedure aggiornaAssTipoLaCdrOnCreaCdr(aCdr cdr%rowtype);

-- Inserisce la linea di attivita dato il rowtype

 procedure ins_LINEA_ATTIVITA (aDest LINEA_ATTIVITA%rowtype);

-- Inserisce l'associazione della linea di attivita con l'esercizio

 procedure ins_ASS_LINEA_ATTIVITA_ESER (aDest ASS_LINEA_ATTIVITA_ESERCIZIO%rowtype);

-- inserimento nella associazione per accentatore

 procedure ins_ASS_LA_CLASS_VOCI (aDest ASS_LA_CLASS_VOCI%rowtype);

-- Inserisce il tipo di linea di attivita dato il rowtype

 procedure ins_TIPO_LINEA_ATTIVITA (aDest TIPO_LINEA_ATTIVITA%rowtype);

-- Inserisce associazione tra tipo di linea di attivita e Cdr dato il rowtype

 procedure ins_ASS_TIPO_LA_CDR (aDest ASS_TIPO_LA_CDR%rowtype);

-- Inserisce un insieme di linee di attivita dato il rowtype

 procedure ins_INSIEME_LA (aDest INSIEME_LA%rowtype);

-- Copia i risultati della linea origine in quella destinazione
-- Si assume che la linea destinazione non abbia risultati

 procedure copiaRisultati(aLADestinazione linea_attivita%rowtype, aLAOrigine linea_attivita%rowtype);

-- Calcola la prossima chiave di una linea di attivita in un dato CDR
-- Da utilizzare solo per linee di attivita Proprie o di Sistema (tipo = 'P', 'S')

 function getNextCodice(tipologia char,aLA linea_attivita%rowtype) return varchar2;

-- Ritorna la linea di attivita SAUOP per il CDR aCDR
-- aEs -> esercizio contabile
-- aCdCdr -> codice del centro di responsabilita
--

 function getLASAUOP(aEs number, aCdCdr varchar2) return linea_attivita%rowtype;

-- Crea la linea di attivita SAUOP per il CDR aCDR
-- aEs -> esercizio contabile
-- aCdCdr -> codice del centro di responsabilita
-- aUser -> utenza di creazione del record
--

 function creaLASAUOP(aEs number, aCdCdr varchar2, aUser varchar2) return linea_attivita%rowtype;

-- Ritorna l'insieme automatico per collegamento entrata CSSAC in AREA per natura 5 (Scarico verso AREA)
-- aEs -> esercizio contabile
-- aCdCdr -> codice del centro di responsabilita
--

 function getInsiemeScrArea(aEs number, aCdCdrArea varchar2) return insieme_la%rowtype;

-- Crea l'insieme automatico per collegamento entrata CSSAC in AREA per natura 5 (Scarico verso AREA)
-- aEs -> esercizio contabile
-- aCdCdr -> codice del centro di responsabilita
-- aUser -> utenza di creazione del record
--

 function creaInsiemeScrArea(aEs number, aCdCdrArea varchar2, aUser varchar2) return insieme_la%rowtype;

end;
