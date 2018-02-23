--------------------------------------------------------
--  DDL for Package CNRCTB100
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB100" AS
--==================================================================================================
--
-- CNRCTB100 - Package di utilità DOCUMENTI AMMINISTRATIVI
--
-- Date: 13/07/2006
-- Version: 4.4
--
-- Dependency: CNRCTB 001/015/250 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 07/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 08/05/2002
-- Version: 1.1
-- Aggiunti metodi e costanti documenti
--
-- Date: 12/05/2002
-- Version: 1.2
-- Aggiunte costanti
--
-- Date: 13/05/2002
-- Version: 1.3
-- Aggiunte costanti per compensi
--
-- Date: 15/05/2002
-- Version: 1.4
-- Aggiunte costanti per CORI
--
-- Date: 16/05/2002
-- Version: 1.5
-- Aggiunto il recupero della sezione economica per CORI
--
-- Date: 16/05/2002
-- Version: 1.6
-- Aggiunta la gestione del COMPENSO
--
-- Date: 17/05/2002
-- Version: 1.7
-- Fix errori
--
-- Date: 19/05/2002
-- Version: 1.8
-- Aggiunti metodi per il recupero del tipo di bene/servizio
--
-- Date: 20/05/2002
-- Version: 1.9
-- Sistemata la gestione del tipo doc amm in isInTabellaGenerico
--
-- Date: 21/05/2002
-- Version: 2.0
-- Aggiunte nuove costanti per fatt. attive
--
-- Date: 22/05/2002
-- Version: 2.1
-- Enforce null parameter check
--
-- Date: 29/05/2002
-- Version: 2.2
-- Aggiunto nuovo tipo di doc generico (apertura fondo economale)
--
-- Date: 30/05/2002
-- Version: 2.3
-- Aggiunta nuovi tipi di documenti amministrativi (MISSIONE e ANTICIPO)
--
-- Date: 31/05/2002
-- Version: 2.4
-- Aggiunto stato coep esclusione
--
-- Date: 07/06/2002
-- Version: 2.5
-- Aggiunto il tipo documento rimborso
--
-- Date: 07/06/2002
-- Version: 2.6
-- Sistemato il metodo di recupero della sezione economica principale
--
-- Date: 10/06/2002
-- Version: 2.7
-- Sistemato il metodo di recupero della sezione cori
--
-- Date: 21/06/2002
-- Version: 2.8
-- Aggiunto metodo di recupero del codice di bene servizio di tipo sconto abbuono
-- Aggiunto metodo di check che il bene servizio sia di tipo sconto abbuono
--
-- Date: 01/07/2002
-- Version: 2.9
-- Aggiunto il nuovo tipo di doc generico per recupero creaditi da terzi
--
-- Date: 18/07/2002
-- Version: 3.0
--
-- Introdotta routine per controllo data di registrazione in base ai registri iva stampati
-- solo fatture passive
--
-- Date: 18/07/2002
-- Version: 3.1
-- Aggiunta AUTOFATTURA (solo testata) al protocollo
--
-- Date: 18/07/2002
-- Version: 3.2
-- Aggiornamento documentazione
--
-- Date: 19/07/2002
-- Version: 3.3
-- Recuperato l'identifivativo del nome tipo registro da costante definita su package CNRCTB250
--
-- Date: 12/09/2002
-- Version: 3.4
-- Rimozione della costante TI_REGISTRO definita nel package CNRCTB250,
-- al suo posto viene ora valorizzata un variabile locale aTipoReport
--
-- Date: 25/09/2002
-- Version: 3.5
-- Introdotti metodi per il salvataggio di FATTURE PASSIVE
--
-- Date: 17/10/2002
-- Version: 3.6
-- Aggiunti nuovi doc generici
--
-- Date: 17/10/2002
-- Version: 3.7
-- Aggiunte nuove costanti per indicare la tipologia di acquisto (beni, servizi o indifferente di
-- una fattura acquisti.
-- Aggiornamento inserimento standard di una fattura passiva
--
-- Date: 12/11/2002
-- Version: 3.8
-- Periodo di competenza in testata doc generico
--
-- Date: 17/12/2002
-- Version: 3.9
-- Aggiunta la costante GEN_CHIUSURA_FONDO
--
-- Date: 19/06/2003
-- Version: 4.0
-- Aggiunto nuovo metodo di aggiornamento dati sistemistici del doc amm SOLO per la parte verrec
--
-- Date: 30/06/2003
-- Version: 4.1
-- Fix updateDocAmm, updateDocAmmriga per setClause null
--
-- Date: 18/07/2003
-- Version: 4.2
--
-- Date: 01/03/2004
-- Version: 4.3
-- Aggiunto il tipo PROMISCUO
--
-- Date: 13/07/2006
-- Version: 4.4
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Adeguamento package per nuovi attributi su FATTURA_PASSIVA (fl_congelata).
--
--==================================================================================================
--
-- Constants:
--

BENE_SERVIZIO_SPECIALE CONSTANT VARCHAR2(50) := 'BENE_SERVIZIO_SPECIALE';
BENE_SERVIZIO_SA CONSTANT VARCHAR2(100) := 'SCONTO_ABBUONO';

-- Carico ente percepiente in effetto COEP/COFI dei CORI

TI_CARICO_ENTE CONSTANT CHAR(1) := 'E';
TI_CARICO_PERCEPIENTE CONSTANT CHAR(1) := 'P';


-- Sezione economica
IS_DARE CONSTANT CHAR(1) := 'D';
IS_AVERE CONSTANT CHAR(1) := 'A';

-- Costanti tipo fattura (per fatture attive e passive)
-- Tipo fattura
TI_FATT_FATTURA CONSTANT CHAR(1) := 'F';
-- Tipo nota di debito
TI_FATT_NOTA_D CONSTANT CHAR(1) := 'D';
-- Tipo nota di credito
TI_FATT_NOTA_C CONSTANT CHAR(1) := 'C';

-- Causali di emissione di fatture attive
TI_FA_CAUSALE_BENE_DUREVOLE CONSTANT CHAR(1) := 'B';
TI_FA_CAUSALE_CONTRATTO CONSTANT CHAR(1) := 'C';
TI_FA_CAUSALE_LIBERA CONSTANT CHAR(1) := 'L';
TI_FA_CAUSALE_TARIFFARIO CONSTANT CHAR(1) := 'T';

-- Tipo documenti amministratvi
TI_FATTURA_PASSIVA CONSTANT VARCHAR2(10) := 'FATTURA_P';
TI_FATTURA_ATTIVA CONSTANT VARCHAR2(10) := 'FATTURA_A';
TI_GENERICO_ENTRATA CONSTANT VARCHAR2(10) := 'GENERICO_E';
TI_GENERICO_SPESA CONSTANT VARCHAR2(10) := 'GENERICO_S';
TI_GENERICO_TRASF_E CONSTANT VARCHAR2(10) := 'TRASF_E';
TI_GENERICO_TRASF_S CONSTANT VARCHAR2(10) := 'TRASF_S';
TI_COMPENSO CONSTANT VARCHAR2(10) := 'COMPENSO';

TI_MISSIONE CONSTANT VARCHAR2(10) := 'MISSIONE';
TI_ANTICIPO CONSTANT VARCHAR2(10) := 'ANTICIPO';
TI_RIMBORSO CONSTANT VARCHAR2(10) := 'RIMBORSO';

TI_AUTOFATTURA CONSTANT VARCHAR2(10) := 'AUTOFATT';

-- Generico per regolamento di sospeso
TI_GENERICO_REGOLA_E CONSTANT VARCHAR2(10) := 'REGOLA_E';

-- Generici per contributi ritenute
TI_GEN_CORI_ACC_ENTRATA CONSTANT VARCHAR2(10) := 'GEN_CORA_E';
TI_GEN_CORI_ACC_SPESA CONSTANT VARCHAR2(10) := 'GEN_CORA_S';
TI_GEN_CORI_VER_ENTRATA CONSTANT VARCHAR2(10) := 'GEN_CORV_E';
TI_GEN_CORI_VER_SPESA CONSTANT VARCHAR2(10) := 'GEN_CORV_S';

-- Generico per generazione compenso negativo
TI_GEN_REC_CRED_DA_TERZI CONSTANT VARCHAR2(10) := 'GEN_RC_DAT';

-- Generico di reintegro fondo economale
TI_GEN_REINTEGRO_FONDO CONSTANT VARCHAR2(10) := 'GEN_RE_FON';

-- Generico di apertura fondo economale
TI_GEN_APERTURA_FONDO CONSTANT VARCHAR2(10) := 'GEN_AP_FON';

-- Generico di chiusura fondo economale
TI_GEN_CHIUSURA_FONDO CONSTANT VARCHAR2(10) := 'GEN_CH_FON';

-- Generico versamento stipendi
TI_GEN_VER_STIPENDI CONSTANT VARCHAR2(10) := 'GEN_STIP_S';

-- Generico per incasso IVA da fatture estere
TI_GEN_IVA_ENTRATA CONSTANT VARCHAR2(10) := 'GEN_IVA_E';

-- Tipo istituzionale commerciale (es. generico)
TI_ISTITUZIONALE CONSTANT CHAR(1) := 'I';
TI_COMMERCIALE CONSTANT CHAR(1) := 'C';
TI_PROMISCUO CONSTANT CHAR(1) := 'P';

TI_NON_ASSOC_MAN_REV CONSTANT CHAR(1) := 'N';
TI_ASSOC_PAR_MAN_REV CONSTANT CHAR(1) := 'P';
TI_ASSOC_TOT_MAN_REV CONSTANT CHAR(1) := 'T';

-- Stati documento per contabilità economico/patrimoniale (per tutti i documenti).

STATO_COEP_INI CONSTANT VARCHAR2(1) := 'N'; -- Documento non contabilizzato in COGE; valore di default.
STATO_COEP_CON CONSTANT VARCHAR2(1) := 'C'; -- Documento contabilizzato in COGE.
STATO_COEP_DA_RIP CONSTANT VARCHAR2(1) := 'R';-- Documento da contabilizzare nuovamente in COGE (storno e nuova contabilizzazione).
STATO_COEP_EXC CONSTANT VARCHAR2(1) := 'X';-- Documento da non contabilizzare in COGE.

-- Stato documento in relazione al fondo economale

STATO_NO_PFONDOECO CONSTANT VARCHAR2(10) := 'N';
STATO_ASS_PFONDOECO CONSTANT VARCHAR2(10) := 'A';
STATO_REG_PFONDOECO CONSTANT VARCHAR2(10) := 'R';

-- Tipologia di acquisto beni, servizi o indifferente per fatture passive

TI_FT_ACQ_BENI CONSTANT CHAR(1) := 'B';
TI_FT_ACQ_SERVIZI CONSTANT CHAR(1) := 'S';
TI_FT_ACQ_INDIFFERENTE CONSTANT CHAR(1) := '*';

-- Costanti fattura
-- ***************************
TYPE fattPassRigaList IS TABLE OF FATTURA_PASSIVA_RIGA%ROWTYPE INDEX BY BINARY_INTEGER;

-- Costanti documento generico
-- ***************************
TYPE docGenRigaList IS TABLE OF DOCUMENTO_GENERICO_RIGA%ROWTYPE INDEX BY BINARY_INTEGER;

-- Stati documento per contabilità finanziaria.

STATO_GEN_COFI_INI CONSTANT CHAR(1) := 'I'; -- Iniziale. Allo stato attuale non usato in quanto è obbligatoria l'associazione con obbligazione o accertamento.
STATO_GEN_COFI_CONT CONSTANT CHAR(1) := 'C'; -- Contabilizzato. Documento associato ad obbligazione; l''intero documento deve sempre essere totalmente associato ad obbligazione o accertamento.
STATO_GEN_COFI_PAR_MR CONSTANT CHAR(1) := 'Q'; -- Parzialmente associata a mandato/reversale; alcune righe del generico sono in stato P
STATO_GEN_COFI_TOT_MR CONSTANT CHAR(1) := 'P'; -- Pagata; tutte le righe del generico sono associate a mandati/reversali
STATO_GEN_COFI_ANN CONSTANT CHAR(1) := 'A'; --  Tutte le righe del generico risultano annullate logicamente

-- Costanti compenso
-- *****************
-- Stati documento per contabilità finanziaria.

STATO_COM_COFI_INI CONSTANT CHAR(1) := 'I'; -- Iniziale.
STATO_COM_COFI_CONT CONSTANT CHAR(1) := 'C'; -- Contabilizzato. Documento associato ad obbligazione; l''intero documento deve sempre essere totalmente associato ad obbligazione o accertamento.
STATO_COM_COFI_TOT_MR CONSTANT CHAR(1) := 'P'; -- Pagato; compenso liquidato

-- Costanti in configurazione CNR
CCNR_DIVISA CONSTANT VARCHAR2(30):='CD_DIVISA';
CCNR_EURO CONSTANT VARCHAR2(30):='EURO';

-- Functions e Procedures:

-- Estrazione del bene/servizio di tipo sconto/abbuono
FUNCTION getBeneServScontoAbbuono RETURN VARCHAR2;
-- Check che il bene servizio sia di tipo sconto/abbuono
FUNCTION isBeneServScontoAbbuono(aCdBeneServ VARCHAR2) RETURN BOOLEAN;

-- Aggiorna per chiave primaria la testata del documento

 PROCEDURE updateDocAmm(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2,
  aUser VARCHAR2,
  aTSNow DATE
 );

-- Modifica solo il verrec ma non duva e utuv

 PROCEDURE updateDocAmm_noDuvaUtuv(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2
 );

-- Aggiorna per chiave primaria la riga del documento

 PROCEDURE updateDocAmmRiga(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aPgRiga NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2,
  aUser VARCHAR2,
  aTSNow DATE
 );

 PROCEDURE updateDocAmmRiga_noDuvaUtuv(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aPgRiga NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2);


-- Mette un lock esclusivo sulla riga di documento amministrativo identificato dai parametri specificati
 PROCEDURE lockDocAmm(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER
 );

 PROCEDURE lockDocAmmRiga(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aPgRiga NUMBER
 );

-- Ritorna la where condition per DML su doc amm per chiave

 FUNCTION getTstaWhereCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2;

-- Ritorna la where condition per DML su doc amm per chiave
-- Se aPgDocDett is null -> ritorna la where per estrazione di TUTTE le righe

 FUNCTION getRigaWhereCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER,
  aPgDocDett NUMBER
 ) RETURN VARCHAR2;

 FUNCTION getTstaRigaCondForJoin (
  aCdTipoDoc VARCHAR2
 ) RETURN VARCHAR2;

 FUNCTION getTstaRigaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2;

 FUNCTION getTstaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2;

 FUNCTION getRigaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER,
  aPgDocDett NUMBER
 ) RETURN VARCHAR2;

-- Ritorna 'Y' se il tipo di documento amministrativo specificato si trova nella tabella specificata nel nome

 FUNCTION isInTabellaFatturaPassiva(aTipoDocAmm VARCHAR2) RETURN CHAR;
 FUNCTION isInTabellaFatturaAttiva(aTipoDocAmm VARCHAR2) RETURN CHAR;
 FUNCTION isInTabellaGenerico(aTipoDocAmm VARCHAR2) RETURN CHAR;
 FUNCTION isInTabellaCompenso(aTipoDocAmm VARCHAR2) RETURN CHAR;
 FUNCTION isInTabellaAnticipo(aTipoDocAmm VARCHAR2) RETURN CHAR;
 FUNCTION isInTabellaRimborso(aTipoDocAmm VARCHAR2) RETURN CHAR;
 FUNCTION isInTabellaMissione(aTipoDocAmm VARCHAR2) RETURN CHAR;
 FUNCTION isInTabellaAutofattura(aTipoDocAmm VARCHAR2) RETURN CHAR;

-- Ritorna il nome della tabella per la gestione del tipo di documento specificato

 FUNCTION getTabella(aTipoDocAmm VARCHAR2) RETURN VARCHAR2;
 FUNCTION getTabellaDett(aTipoDocAmm VARCHAR2) RETURN VARCHAR2;

-- Ritorna la sezione Dare/Avere principale del documento

 FUNCTION getSezioneEconomica(aTipoDocAmm VARCHAR2) RETURN CHAR;

-- Ritorna la sezione Dare/Avere principale cori
 FUNCTION getSezioneEconomicaCori(aTipoDocAmm VARCHAR2) RETURN CHAR;

-- Ritorna il nome del campo di progressivo del documento del tipo specificato
 FUNCTION getNomePg(aTipoDocAmm VARCHAR2) RETURN VARCHAR2;

-- Ritorna il nome del campo di progressivo del dettaglio di documento del tipo specificato
 FUNCTION getNomePgDett(aTipoDocAmm VARCHAR2) RETURN VARCHAR2;

-- Funzioni di numerazione doc amm
-- Non gestisca le numerazioni provvisorie (tipo doc X$)

 FUNCTION getNextNum(aCdCds VARCHAR2, aEs NUMBER, aCdUo VARCHAR2, aTipoDocumento VARCHAR2, aUser VARCHAR2, aTSNow DATE DEFAULT SYSDATE) RETURN NUMBER;

-- Funzione di inserimento del record di numerazione deocumenti amministrativi
 PROCEDURE ins_NUMERAZIONE_DOC_AMM (aDest NUMERAZIONE_DOC_AMM%ROWTYPE);

-- Funzioni di servizio inserimento documento generico
 PROCEDURE ins_DOCUMENTO_GENERICO (aDest DOCUMENTO_GENERICO%ROWTYPE);
 PROCEDURE ins_DOCUMENTO_GENERICO_RIGA (aDest DOCUMENTO_GENERICO_RIGA%ROWTYPE);

-- Funzione di controllo ammissibilità della data di registrazione di fatture in base ai periodi iva stampati

   PROCEDURE chkDtRegistrazPerIva
      (inCdCds FATTURA_PASSIVA.cd_cds_origine%TYPE,
       inCdUo FATTURA_PASSIVA.cd_uo_origine%TYPE,
       inEsercizio FATTURA_PASSIVA.ESERCIZIO%TYPE,
       inCdTipoSezionale FATTURA_PASSIVA.cd_tipo_sezionale%TYPE,
       inDtRegistrazione FATTURA_PASSIVA.dt_registrazione%TYPE
      );

-- Funzioni di inserimento testata e righe di fattura passiva
 procedure ins_FATTURA_PASSIVA_RIGA (aDest FATTURA_PASSIVA_RIGA%rowtype);
 procedure ins_FATTURA_PASSIVA (aDest FATTURA_PASSIVA%rowtype);
 procedure aggiorna_data_differita(es NUMBER,cds VARCHAR2,uo VARCHAR2,pg_fattura NUMBER,riga NUMBER);
 procedure aggiorna_data_differita_attive(es NUMBER,cds VARCHAR2,uo VARCHAR2,pg_fattura NUMBER,riga NUMBER);
 procedure insProgrUnivocoFatturaPassiva(es NUMBER,data_a date);
END;
