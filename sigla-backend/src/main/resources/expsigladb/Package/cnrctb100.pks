CREATE OR REPLACE PACKAGE CNRCTB100 AS
--==================================================================================================
--
-- CNRCTB100 - Package di utilit? DOCUMENTI AMMINISTRATIVI
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

-- Stati documento per contabilit? economico/patrimoniale (per tutti i documenti).

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

-- Stati documento per contabilit? finanziaria.

STATO_GEN_COFI_INI CONSTANT CHAR(1) := 'I'; -- Iniziale. Allo stato attuale non usato in quanto ? obbligatoria l'associazione con obbligazione o accertamento.
STATO_GEN_COFI_CONT CONSTANT CHAR(1) := 'C'; -- Contabilizzato. Documento associato ad obbligazione; l''intero documento deve sempre essere totalmente associato ad obbligazione o accertamento.
STATO_GEN_COFI_PAR_MR CONSTANT CHAR(1) := 'Q'; -- Parzialmente associata a mandato/reversale; alcune righe del generico sono in stato P
STATO_GEN_COFI_TOT_MR CONSTANT CHAR(1) := 'P'; -- Pagata; tutte le righe del generico sono associate a mandati/reversali
STATO_GEN_COFI_ANN CONSTANT CHAR(1) := 'A'; --  Tutte le righe del generico risultano annullate logicamente

-- Costanti compenso
-- *****************
-- Stati documento per contabilit? finanziaria.

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

-- Funzione di controllo ammissibilit? della data di registrazione di fatture in base ai periodi iva stampati

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


CREATE OR REPLACE PACKAGE BODY CNRCTB100 IS
 FUNCTION getBeneServScontoAbbuono RETURN VARCHAR2 IS
 BEGIN
  RETURN Cnrctb015.GETVAL01PERCHIAVE(BENE_SERVIZIO_SPECIALE,BENE_SERVIZIO_SA);
 END;

 FUNCTION isBeneServScontoAbbuono(aCdBeneServ VARCHAR2) RETURN BOOLEAN IS
 BEGIN
  IF getBeneServScontoAbbuono = aCdBeneServ THEN
   RETURN TRUE;
  ELSE
   RETURN FALSE;
  END IF;
 END;

 PROCEDURE updateDocAmmInt(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2,
  isNotDuvaUtuv boolean,
  aUser VARCHAR2,
  aTSNow DATE
 ) IS
  aStr VARCHAR2(2000);
  aStatement varchar2(2000);
 BEGIN
  if isNotDuvaUtuv is null then
   IBMERR001.RAISE_ERR_GENERICO('Specificare il tipo di agg. dei parametri sistemistici del record');
  end if;
  if aSetClause is null then
  	 aStatement := '';
  else
  	 aStatement := aSetClause||', ';
  end if;
  aStr:='update '||Cnrctb100.GETTABELLA(aCdTipoDocumento);
  if isNotDuvaUtuv then
   aStr:=aStr||' set '||aStatement||' pg_ver_rec=pg_ver_rec+1 ';
  else
   aStr:=aStr||' set '||aStatement||' duva='||Ibmutl001.asDynTimestamp(aTSNow)||', utuv='''||aUser||''', pg_ver_rec=pg_ver_rec+1 ';
  end if;
  aStr:=aStr||Cnrctb100.GETTSTAWHERECONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento);
  IF aWhereClause IS NOT NULL THEN
   aStr:=aStr||' and ('||aWhereClause||')';
  END IF;
  EXECUTE IMMEDIATE aStr;
 END;

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
 ) IS
 begin
  updateDocAmmInt(
   aCdTipoDocumento,
   aCdCds,
   aEs,
   aCdUo,
   aPgDocumento,
   aSetClause,
   aWhereClause,
   false, -- update verrec duva utuv
   aUser,
   aTSNow);
 end;

 PROCEDURE updateDocAmm_noDuvaUtuv(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2
 ) IS
 begin
  updateDocAmmInt(
   aCdTipoDocumento,
   aCdCds,
   aEs,
   aCdUo,
   aPgDocumento,
   aSetClause,
   aWhereClause,
   true, -- update solo verrec
   null,
   null);
 end;

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
 ) IS
  aStr VARCHAR2(2000);
  aTab VARCHAR2(40);
  aStatement varchar2(2000);
 BEGIN
  aTab:=Cnrctb100.GETTABELLADETT(aCdTipoDocumento);
  IF aTab IS NULL THEN
   RETURN;
  END IF;

  if aSetClause is null then
  	 aStatement := '';
  else
  	 aStatement := aSetClause||', ';
  end if;
  aStr:='update '||aTab;
  aStr:=aStr||' set '||aStatement||' duva='||Ibmutl001.asDynTimestamp(aTSNow)||', utuv='''||aUser||''', pg_ver_rec=pg_ver_rec+1 ';
  aStr:=aStr||Cnrctb100.GETRIGAWHERECONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento,aPgRiga);
  IF aWhereClause IS NOT NULL THEN
   aStr:=aStr||' and ('||aWhereClause||')';
  END IF;
  EXECUTE IMMEDIATE aStr;
 END;

 Procedure updateDocAmmRiga_noDuvaUtuv(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aPgRiga NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2
 ) IS
  aStr VARCHAR2(2000);
  aTab VARCHAR2(40);
  aStatement varchar2(2000);

 Begin
  aTab:=Cnrctb100.GETTABELLADETT(aCdTipoDocumento);
  IF aTab IS NULL THEN
   RETURN;
  END IF;
  if aSetClause is null then
  	 aStatement := '';
  else
  	 aStatement := aSetClause||', ';
  end if;
  aStr:='update '||aTab;
  aStr:=aStr||' set '||aStatement||' pg_ver_rec=pg_ver_rec+1 ';
  aStr:=aStr||Cnrctb100.GETRIGAWHERECONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento,aPgRiga);
  IF aWhereClause IS NOT NULL THEN
   aStr:=aStr||' and ('||aWhereClause||')';
  END IF;
  EXECUTE IMMEDIATE aStr;
 END;

 PROCEDURE lockDocAmm(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER
 ) IS
 BEGIN
  EXECUTE IMMEDIATE 'select * '||GETTSTAFROMCONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento)||' for update nowait';
 END;

 PROCEDURE lockDocAmmRiga(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aPgRiga NUMBER
 ) IS
  aTab VARCHAR2(40);
 BEGIN
  aTab:=Cnrctb100.GETTABELLADETT(aCdTipoDocumento);
  IF aTab IS NULL THEN
   RETURN;
  END IF;
  EXECUTE IMMEDIATE 'select * from '||aTab||GETRIGAWHERECONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento,aPgRiga)||' for update nowait';
 END;

 FUNCTION getTstaWhereCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2 IS
  aStr VARCHAR2(2000);
  aTab VARCHAR2(40);
 BEGIN
  IF
       aCdTipoDoc IS NULL
    OR aCdCds IS NULL
    OR aEs IS NULL
    OR aCdUo IS NULL
    OR aPgDoc IS NULL
  THEN
   Ibmerr001.RAISE_ERR_GENERICO('Chiave documento non completamente valorizzata');
  END IF;
  aTab:=GETTABELLA(aCdTipoDoc);
  aStr:=' where '||aTab||'.cd_cds = '''||aCdCds||''' and '||aTab||'.esercizio = '||aEs||' and '||aTab||'.cd_unita_organizzativa = '''||aCdUo||''' ';
  IF isInTabellaGenerico(aCdTipoDoc) = 'Y' THEN
   aStr:=aStr||' and '||aTab||'.cd_tipo_documento_amm = '''||aCdTipoDoc||''' ';
  END IF;
  aStr:=aStr||' and '||aTab||'.'||getNomePg(aCdTipoDoc)||'='||aPgDoc;
  RETURN aStr;
 END;

 FUNCTION getTstaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2 IS
 BEGIN
  RETURN ' from '||GETTABELLA(aCdTipoDoc)||getTstaWhereCondForKey (
                                                 aCdTipoDoc,
                                                 aCdCds,
                                                 aEs,
                                                 aCdUo,
                                                 aPgDoc
                                            );
 END;

 FUNCTION getRigaWhereCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER,
  aPgDocDett NUMBER
 ) RETURN VARCHAR2 IS
  aStr VARCHAR2(2000);
  aTab VARCHAR2(40);
 BEGIN
  aTab:=GETTABELLADETT(aCdTipoDoc);

  IF
       aCdTipoDoc IS NULL
    OR aCdCds IS NULL
    OR aEs IS NULL
    OR aCdUo IS NULL
    OR aPgDoc IS NULL
  THEN
   Ibmerr001.RAISE_ERR_GENERICO('Chiave documento non completamente valorizzata');
  END IF;
  IF aTab IS NULL THEN
   RETURN NULL;
  END IF;
  aStr:=' where '||aTab||'.cd_cds = '''||aCdCds||''' and '||aTab||'.esercizio = '||aEs||' and '||aTab||'.cd_unita_organizzativa = '''||aCdUo||''' ';
  IF isInTabellaGenerico(aCdTipoDoc) = 'Y' THEN
   aStr:=aStr||' and '||aTab||'.cd_tipo_documento_amm = '''||aCdTipoDoc||''' ';
  END IF;
  aStr:=aStr||' and '||aTab||'.'||getNomePg(aCdTipoDoc)||'='||aPgDoc;
  IF aPgDocDett IS NOT NULL THEN
   aStr:=aStr||' and '||aTab||'.'||getNomePgDett(aCdTipoDoc)||'='||aPgDocDett;
  END IF;
  RETURN aStr;
 END;

 FUNCTION getRigaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER,
  aPgDocDett NUMBER
 ) RETURN VARCHAR2 IS
  aTab VARCHAR2(40);
 BEGIN
  aTab:=GETTABELLADETT(aCdTipoDoc);
  IF aTab IS NULL THEN
   RETURN NULL;
  END IF;
  RETURN ' from '||aTab||getRigaWhereCondForKey (
                                                 aCdTipoDoc,
                                                 aCdCds,
                                                 aEs,
                                                 aCdUo,
                                                 aPgDoc,
                                                 aPgDocDett);
 END;

 FUNCTION getTstaRigaCondForJoin (
  aCdTipoDoc VARCHAR2
 ) RETURN VARCHAR2 IS
  aStr VARCHAR2(2000);
  aTestata VARCHAR2(40);
  aRiga VARCHAR2(40);
 BEGIN
  IF
       aCdTipoDoc IS NULL
  THEN
   Ibmerr001.RAISE_ERR_GENERICO('Tipo documento non specificato');
  END IF;
   aTestata:=GETTABELLA(aCdTipoDoc);
   aRiga:=GETTABELLADETT(aCdTipoDoc);
   IF aRiga IS NULL THEN
    RETURN NULL;
   END IF;
   aStr:=
                    aRiga||'.cd_cds = '||aTestata||'.cd_cds'
         ||' and '||aRiga||'.esercizio = '||aTestata||'.esercizio'
         ||' and '||aRiga||'.cd_unita_organizzativa = '||aTestata||'.cd_unita_organizzativa'
         ||' and '||aRiga||'.'||getNomePg(aCdTipoDoc)||'='||aTestata||'.'||getNomePg(aCdTipoDoc);

  IF isInTabellaGenerico(aCdTipoDoc) = 'Y' THEN
   aStr:=aStr||' and '||aRiga||'.cd_tipo_documento_amm = '||aTestata||'.cd_tipo_documento_amm';
  END IF;
  RETURN aStr;
 END;

 FUNCTION getTstaRigaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2 IS
  aStr VARCHAR2(2000);
  aRiga VARCHAR2(40);
 BEGIN
  aRiga:=GETTABELLADETT(aCdTipoDoc);
  IF aRiga IS NULL THEN
   RETURN NULL;
  END IF;
  aStr:=' from '||GETTABELLA(aCdTipoDoc)||','||GETTABELLADETT(aCdTipoDoc);
  aStr:=aStr||getTstaWhereCondForKey (
               aCdTipoDoc,
               aCdCds,
               aEs,
               aCdUo,
               aPgDoc
              ) || ' and ' || getTstaRigaCondForJoin (aCdTipoDoc);
  RETURN aStr;
 END;

 FUNCTION getNextNum(aCdCds VARCHAR2, aEs NUMBER, aCdUo VARCHAR2, aTipoDocumento VARCHAR2, aUser VARCHAR2, aTSNow DATE DEFAULT SYSDATE) RETURN NUMBER IS
  aNum NUMERAZIONE_DOC_AMM%ROWTYPE;
  aTipoDoc TIPO_DOCUMENTO_AMM%ROWTYPE;
 BEGIN
  BEGIN
    IF aTipoDocumento LIKE '%$' THEN
     Ibmerr001.RAISE_ERR_GENERICO('Tipo di documento provvisorio non gestito da questo numeratore: '||aTipoDocumento);
	END IF;
    BEGIN
     SELECT * INTO aTipoDoc FROM TIPO_DOCUMENTO_AMM WHERE
      cd_tipo_documento_amm = aTipoDocumento;
    EXCEPTION WHEN NO_DATA_FOUND THEN
     Ibmerr001.RAISE_ERR_GENERICO('Tipo di documento non esistente: '||aTipoDocumento);
    END;
    SELECT * INTO aNum FROM NUMERAZIONE_DOC_AMM WHERE
         ESERCIZIO = aEs
     AND cd_cds = aCdCds
     AND cd_unita_organizzativa = aCdUo
     AND cd_tipo_documento_amm = aTipoDocumento FOR UPDATE NOWAIT;
  EXCEPTION WHEN NO_DATA_FOUND THEN
      aNum.CD_CDS:=aCdCds;
      aNum.CD_UNITA_ORGANIZZATIVA:=aCdUo;
      aNum.ESERCIZIO:=aEs;
      aNum.CD_TIPO_DOCUMENTO_AMM:=aTipoDocumento;
      aNum.CORRENTE:=0;
      aNum.UTCR:=aUser;
      aNum.DACR:=aTSNow;
      aNum.UTUV:=aUser;
      aNum.DUVA:=aTSNow;
      aNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_AMM(aNum);
  END;
  UPDATE NUMERAZIONE_DOC_AMM SET
       corrente=corrente+1,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec=pg_ver_rec+1
  WHERE
       ESERCIZIO = aEs
   AND cd_cds = aCdCds
   AND cd_unita_organizzativa = aCdUo
   AND cd_tipo_documento_amm = aTipoDocumento;
  RETURN aNum.corrente+1;
 END;


 FUNCTION getTabella(aTipoDocAmm VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  IF isInTabellaFatturaPassiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'FATTURA_PASSIVA';
  END IF;
  IF isInTabellaFatturaAttiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'FATTURA_ATTIVA';
  END IF;
  IF isInTabellaGenerico(aTipoDocAmm) = 'Y' THEN
   RETURN 'DOCUMENTO_GENERICO';
  END IF;
  IF isInTabellaCompenso(aTipoDocAmm) = 'Y' THEN
   RETURN 'COMPENSO';
  END IF;
  IF isInTabellaAnticipo(aTipoDocAmm) = 'Y' THEN
   RETURN 'ANTICIPO';
  END IF;
  IF isInTabellaRimborso(aTipoDocAmm) = 'Y' THEN
   RETURN 'RIMBORSO';
  END IF;
  IF isInTabellaMissione(aTipoDocAmm) = 'Y' THEN
   RETURN 'MISSIONE';
  END IF;
  IF isInTabellaAutofattura(aTipoDocAmm) = 'Y' THEN
   RETURN 'AUTOFATTURA';
  END IF;
  Ibmerr001.RAISE_ERR_GENERICO('Nessuna associazione a tabella per il tipo di documento specificato');
 END;

 FUNCTION getTabellaDett(aTipoDocAmm VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  IF isInTabellaFatturaPassiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'FATTURA_PASSIVA_RIGA';
  END IF;
  IF isInTabellaFatturaAttiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'FATTURA_ATTIVA_RIGA';
  END IF;
  IF isInTabellaGenerico(aTipoDocAmm) = 'Y' THEN
   RETURN 'DOCUMENTO_GENERICO_RIGA';
  END IF;
  IF
      isInTabellaCompenso(aTipoDocAmm) = 'Y'
   OR isInTabellaAnticipo(aTipoDocAmm) = 'Y'
   OR isInTabellaRimborso(aTipoDocAmm) = 'Y'
   OR isInTabellaMissione(aTipoDocAmm) = 'Y'
   OR isInTabellaAutofattura(aTipoDocAmm) = 'Y'
  THEN
   RETURN NULL;
  END IF;
  Ibmerr001.RAISE_ERR_GENERICO('Nessuna associazione a tabella dettagli per il tipo di documento specificato');
 END;

 FUNCTION getSezioneEconomica(aTipoDocAmm VARCHAR2) RETURN CHAR IS
  aSezione CHAR(1);
  aTipoEntrataSpesa CHAR(1);
 BEGIN
  BEGIN
   SELECT ti_entrata_spesa INTO aTipoEntrataSpesa FROM TIPO_DOCUMENTO_AMM WHERE
      cd_tipo_documento_amm = aTipoDocAmm;
  EXCEPTION WHEN NO_DATA_FOUND THEN
   Ibmerr001.RAISE_ERR_GENERICO('Tipo di documento non supportato');
  END;

  IF aTipoEntrataSpesa = Cnrctb001.GESTIONE_SPESE THEN
   aSezione:=IS_DARE;
  ELSE
   aSezione:=IS_AVERE;
  END IF;
  RETURN aSezione;
 END;

 FUNCTION getSezioneEconomicaCori(aTipoDocAmm VARCHAR2) RETURN CHAR IS
  aSezione CHAR(1);
 BEGIN
  RETURN getSezioneEconomica(aTipoDocAmm);
 END;

 FUNCTION getNomePg(aTipoDocAmm VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  IF isInTabellaFatturaPassiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_FATTURA_PASSIVA';
  END IF;
  IF isInTabellaFatturaAttiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_FATTURA_ATTIVA';
  END IF;
  IF isInTabellaGenerico(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_DOCUMENTO_GENERICO';
  END IF;
  IF isInTabellaCompenso(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_COMPENSO';
  END IF;
  IF isInTabellaAnticipo(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_ANTICIPO';
  END IF;
  IF isInTabellaRimborso(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_RIMBORSO';
  END IF;
  IF isInTabellaMissione(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_MISSIONE';
  END IF;
  IF isInTabellaAutofattura(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_AUTOFATTURA';
  END IF;
  Ibmerr001.RAISE_ERR_GENERICO('Nessuna associazione a tabella per il tipo di documento specificato');
 END;

 FUNCTION getNomePgDett(aTipoDocAmm VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  IF
        isInTabellaFatturaPassiva(aTipoDocAmm) = 'Y'
     OR isInTabellaFatturaAttiva(aTipoDocAmm) = 'Y'
	 OR isInTabellaGenerico(aTipoDocAmm) = 'Y'
  THEN
   RETURN 'PROGRESSIVO_RIGA';
  END IF;
  IF
       isInTabellaCompenso(aTipoDocAmm) = 'Y'
	OR isInTabellaAnticipo(aTipoDocAmm) = 'Y'
	OR isInTabellaRimborso(aTipoDocAmm) = 'Y'
	OR isInTabellaMissione(aTipoDocAmm) = 'Y'
	OR isInTabellaAutofattura(aTipoDocAmm) = 'Y'
  THEN
   RETURN NULL;
  END IF;
  Ibmerr001.RAISE_ERR_GENERICO('Nessuna associazione a tabella per il tipo di documento specificato');
 END;

 FUNCTION isInTabellaFatturaPassiva(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_FATTURA_PASSIVA) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;
 FUNCTION isInTabellaGenerico(aTipoDocAmm VARCHAR2) RETURN CHAR IS
  aTipo VARCHAR2(10);
 BEGIN
  BEGIN
   SELECT cd_tipo_documento_amm INTO aTipo FROM TIPO_DOCUMENTO_AMM WHERE
        CD_TIPO_DOCUMENTO_AMM = aTipoDocAmm
	AND FL_DOC_GENERICO = 'Y';
   RETURN 'Y';
  EXCEPTION WHEN NO_DATA_FOUND THEN
   RETURN 'N';
  END;
 END;
 FUNCTION isInTabellaFatturaAttiva(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_FATTURA_ATTIVA) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaCompenso(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_COMPENSO) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaAnticipo(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_ANTICIPO) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaRimborso(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_RIMBORSO) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaAutofattura(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_AUTOFATTURA) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaMissione(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_MISSIONE) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 PROCEDURE ins_DOCUMENTO_GENERICO (aDest DOCUMENTO_GENERICO%ROWTYPE) IS
  BEGIN
   INSERT INTO DOCUMENTO_GENERICO (
     CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,CD_TIPO_DOCUMENTO_AMM
    ,PG_DOCUMENTO_GENERICO
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,DATA_REGISTRAZIONE
    ,DS_DOCUMENTO_GENERICO
    ,TI_ISTITUZ_COMMERC
    ,IM_TOTALE
    ,DT_PAGAMENTO_FONDO_ECO
    ,STATO_COFI
    ,STATO_COGE
    ,CD_DIVISA
    ,CAMBIO
    ,DT_CANCELLAZIONE
    ,ESERCIZIO_LETTERA
    ,PG_LETTERA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,DT_SCADENZA
    ,STATO_COAN
    ,STATO_PAGAMENTO_FONDO_ECO
    ,TI_ASSOCIATO_MANREV
	,DT_DA_COMPETENZA_COGE
	,DT_A_COMPETENZA_COGE
   ) VALUES (
     aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.CD_TIPO_DOCUMENTO_AMM
    ,aDest.PG_DOCUMENTO_GENERICO
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.DATA_REGISTRAZIONE
    ,aDest.DS_DOCUMENTO_GENERICO
    ,aDest.TI_ISTITUZ_COMMERC
    ,aDest.IM_TOTALE
    ,aDest.DT_PAGAMENTO_FONDO_ECO
    ,aDest.STATO_COFI
    ,aDest.STATO_COGE
    ,aDest.CD_DIVISA
    ,aDest.CAMBIO
    ,aDest.DT_CANCELLAZIONE
    ,aDest.ESERCIZIO_LETTERA
    ,aDest.PG_LETTERA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.DT_SCADENZA
    ,aDest.STATO_COAN
    ,aDest.STATO_PAGAMENTO_FONDO_ECO
    ,aDest.TI_ASSOCIATO_MANREV
	,aDest.DT_DA_COMPETENZA_COGE
	,aDest.DT_A_COMPETENZA_COGE
    );
 END;
 PROCEDURE ins_DOCUMENTO_GENERICO_RIGA (aDest DOCUMENTO_GENERICO_RIGA%ROWTYPE) IS
  BEGIN
   INSERT INTO DOCUMENTO_GENERICO_RIGA (
     CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,CD_TIPO_DOCUMENTO_AMM
    ,PG_DOCUMENTO_GENERICO
    ,PROGRESSIVO_RIGA
    ,DS_RIGA
    ,IM_RIGA_DIVISA
    ,IM_RIGA
    ,CD_TERZO
    ,CD_TERZO_CESSIONARIO
    ,CD_TERZO_UO_CDS
    ,RAGIONE_SOCIALE
    ,NOME
    ,COGNOME
    ,CODICE_FISCALE
    ,PARTITA_IVA
    ,CD_MODALITA_PAG
    ,PG_BANCA
    ,CD_TERMINI_PAG
    ,CD_TERMINI_PAG_UO_CDS
    ,PG_BANCA_UO_CDS
    ,CD_MODALITA_PAG_UO_CDS
    ,NOTE
    ,DT_DA_COMPETENZA_COGE
    ,DT_A_COMPETENZA_COGE
    ,STATO_COFI
    ,DT_CANCELLAZIONE
    ,CD_CDS_OBBLIGAZIONE
    ,ESERCIZIO_OBBLIGAZIONE
    ,ESERCIZIO_ORI_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE_SCADENZARIO
    ,CD_CDS_ACCERTAMENTO
    ,ESERCIZIO_ACCERTAMENTO
    ,ESERCIZIO_ORI_ACCERTAMENTO
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,DACR
    ,UTCR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,TI_ASSOCIATO_MANREV
   ) VALUES (
     aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.CD_TIPO_DOCUMENTO_AMM
    ,aDest.PG_DOCUMENTO_GENERICO
    ,aDest.PROGRESSIVO_RIGA
    ,aDest.DS_RIGA
    ,aDest.IM_RIGA_DIVISA
    ,aDest.IM_RIGA
    ,aDest.CD_TERZO
    ,aDest.CD_TERZO_CESSIONARIO
    ,aDest.CD_TERZO_UO_CDS
    ,aDest.RAGIONE_SOCIALE
    ,aDest.NOME
    ,aDest.COGNOME
    ,aDest.CODICE_FISCALE
    ,aDest.PARTITA_IVA
    ,aDest.CD_MODALITA_PAG
    ,aDest.PG_BANCA
    ,aDest.CD_TERMINI_PAG
    ,aDest.CD_TERMINI_PAG_UO_CDS
    ,aDest.PG_BANCA_UO_CDS
    ,aDest.CD_MODALITA_PAG_UO_CDS
    ,aDest.NOTE
    ,aDest.DT_DA_COMPETENZA_COGE
    ,aDest.DT_A_COMPETENZA_COGE
    ,aDest.STATO_COFI
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_CDS_OBBLIGAZIONE
    ,aDest.ESERCIZIO_OBBLIGAZIONE
    ,aDest.ESERCIZIO_ORI_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.CD_CDS_ACCERTAMENTO
    ,aDest.ESERCIZIO_ACCERTAMENTO
    ,aDest.ESERCIZIO_ORI_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.TI_ASSOCIATO_MANREV
    );
 END;

 PROCEDURE ins_NUMERAZIONE_DOC_AMM (aDest NUMERAZIONE_DOC_AMM%ROWTYPE) IS
 BEGIN
  INSERT INTO NUMERAZIONE_DOC_AMM (
    CD_CDS
   ,CD_UNITA_ORGANIZZATIVA
   ,ESERCIZIO
   ,CD_TIPO_DOCUMENTO_AMM
   ,CORRENTE
   ,UTCR
   ,DACR
   ,UTUV
   ,DUVA
   ,PG_VER_REC
  ) VALUES (
    aDest.CD_CDS
   ,aDest.CD_UNITA_ORGANIZZATIVA
   ,aDest.ESERCIZIO
   ,aDest.CD_TIPO_DOCUMENTO_AMM
   ,aDest.CORRENTE
   ,aDest.UTCR
   ,aDest.DACR
   ,aDest.UTUV
   ,aDest.DUVA
   ,aDest.PG_VER_REC
   );
 END;

--==================================================================================================
-- Funzione di controllo ammissibilit? della data di registrazione di fatture in base ai periodi
-- iva stampati
--==================================================================================================
PROCEDURE chkDtRegistrazPerIva
   (inCdCds FATTURA_PASSIVA.cd_cds_origine%TYPE,
    inCdUo FATTURA_PASSIVA.cd_uo_origine%TYPE,
    inEsercizio FATTURA_PASSIVA.ESERCIZIO%TYPE,
    inCdTipoSezionale FATTURA_PASSIVA.cd_tipo_sezionale%TYPE,
    inDtRegistrazione FATTURA_PASSIVA.dt_registrazione%TYPE
   ) IS
   aRecReportStato REPORT_STATO%ROWTYPE;

   ti_sez CHAR(1); -- Il tipo sezionale pu? essere di V=Vendite, A= acquisti
   aTiRegistro VARCHAR2(100);
BEGIN

   BEGIN
	  SELECT TI_ACQUISTI_VENDITE
	  INTO ti_sez
	  FROM TIPO_SEZIONALE
	  WHERE cd_tipo_sezionale =inCdTipoSezionale;

--Codice eliminato in quanto non esiste piu' la distinzione tra Registro Acquisti e Vendite
--Esiste solo il tipo 'REGISTRO_IVA'
/*
	  IF ti_sez ='A' THEN -- FATTURE_PASSIVE
		aTiRegistro := 'REGISTRO_IVA_ACQUISTI';
	  ELSE
		aTiRegistro := 'REGISTRO_IVA_VENDITE';
	  END IF;
*/

      aTiRegistro := 'REGISTRO_IVA';

      SELECT * INTO aRecReportStato
      FROM   REPORT_STATO A
      WHERE  A.cd_cds = inCdCds AND
             A.cd_unita_organizzativa = inCdUo AND
             A.ESERCIZIO = inEsercizio AND
             A.cd_tipo_sezionale = inCdTipoSezionale AND
             A.ti_documento = '*' AND
             A.tipo_report = aTiRegistro AND
             (stato = 'B' OR
              stato = 'C') AND
             A.dt_inizio =
                (SELECT MAX(B.dt_inizio)
                 FROM   REPORT_STATO B
                 WHERE  B.cd_cds = A.cd_cds AND
                        B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                        B.ESERCIZIO = A.ESERCIZIO AND
                        B.cd_tipo_sezionale = A.cd_tipo_sezionale AND
                        B.ti_documento = A.ti_documento AND
                        B.tipo_report = A.tipo_report AND
                        (B.stato = 'B' Or
                         B.stato = 'C'));

      IF Trunc(inDtRegistrazione) <= Trunc(aRecReportStato.dt_fine) THEN
         Ibmerr001.RAISE_ERR_GENERICO('Data registrazione inferiore/uguale a ultimo periodo definitivo di stampa registri IVA ' ||
                                      TO_CHAR(aRecReportStato.dt_fine, 'DD/MM/YYYY'));
      END IF;


   EXCEPTION

      WHEN NO_DATA_FOUND THEN
           NULL;

   END;

   RETURN;

END chkDtRegistrazPerIva;

PROCEDURE ins_FATTURA_PASSIVA
   (aDest FATTURA_PASSIVA%rowtype
   ) is

BEGIN

   INSERT INTO FATTURA_PASSIVA
          (CD_CDS,
           CD_UNITA_ORGANIZZATIVA,
           ESERCIZIO,
           PG_FATTURA_PASSIVA,
           CD_CDS_ORIGINE,
           CD_UO_ORIGINE,
           CD_TIPO_SEZIONALE,
           TI_FATTURA,
           PROTOCOLLO_IVA,
           PROTOCOLLO_IVA_GENERALE,
           DT_REGISTRAZIONE,
           NR_FATTURA_FORNITORE,
           DT_FATTURA_FORNITORE,
           ESERCIZIO_FATTURA_FORNITORE,
           DT_SCADENZA,
           DT_CANCELLAZIONE,
           DS_FATTURA_PASSIVA,
           TI_ISTITUZ_COMMERC,
           FL_INTRA_UE,
           FL_EXTRA_UE,
           FL_SAN_MARINO_CON_IVA,
           FL_SAN_MARINO_SENZA_IVA,
           FL_AUTOFATTURA,
           FL_BOLLA_DOGANALE,
           FL_SPEDIZIONIERE,
           FL_FATTURA_COMPENSO,
           CD_TERZO_CESSIONARIO,
           CD_TERZO_UO_CDS,
           CD_TERZO,
           RAGIONE_SOCIALE,
           NOME,
           COGNOME,
           CODICE_FISCALE,
           PARTITA_IVA,
           CD_TERMINI_PAG,
           CD_TERMINI_PAG_UO_CDS,
           PG_BANCA,
           PG_BANCA_UO_CDS,
           CD_MODALITA_PAG,
           CD_MODALITA_PAG_UO_CDS,
           IM_TOTALE_IMPONIBILE_DIVISA,
           IM_TOTALE_IMPONIBILE,
           IM_TOTALE_IVA,
           IM_TOTALE_FATTURA,
           CD_DIVISA,
           CAMBIO,
           STATO_COFI,
           STATO_COGE,
           DT_PAGAMENTO_FONDO_ECO,
           NUMERO_PROTOCOLLO,
           NOTE,
           DT_DA_COMPETENZA_COGE,
           DT_A_COMPETENZA_COGE,
           ESERCIZIO_LETTERA,
           PG_LETTERA,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           TI_ASSOCIATO_MANREV,
           STATO_COAN,
           STATO_PAGAMENTO_FONDO_ECO,
           IM_TOTALE_QUADRATURA,
           TI_BENE_SERVIZIO,
           CD_CDS_FAT_CLGS,
           CD_UO_FAT_CLGS,
           ESERCIZIO_FAT_CLGS,
           PG_FATTURA_PASSIVA_FAT_CLGS,
           FL_CONGELATA,
           fl_liquidazione_differita,
           data_protocollo,
					 esercizio_compenso,
					 cds_compenso,
					 uo_compenso,
					 pg_compenso
           )
   VALUES (aDest.CD_CDS,
           aDest.CD_UNITA_ORGANIZZATIVA,
           aDest.ESERCIZIO,
           aDest.PG_FATTURA_PASSIVA,
           aDest.CD_CDS_ORIGINE,
           aDest.CD_UO_ORIGINE,
           aDest.CD_TIPO_SEZIONALE,
           aDest.TI_FATTURA,
           aDest.PROTOCOLLO_IVA,
           aDest.PROTOCOLLO_IVA_GENERALE,
           aDest.DT_REGISTRAZIONE,
           aDest.NR_FATTURA_FORNITORE,
           aDest.DT_FATTURA_FORNITORE,
           aDest.ESERCIZIO_FATTURA_FORNITORE,
           aDest.DT_SCADENZA,
           aDest.DT_CANCELLAZIONE,
           aDest.DS_FATTURA_PASSIVA,
           aDest.TI_ISTITUZ_COMMERC,
           aDest.FL_INTRA_UE,
           aDest.FL_EXTRA_UE,
           aDest.FL_SAN_MARINO_CON_IVA,
           aDest.FL_SAN_MARINO_SENZA_IVA,
           aDest.FL_AUTOFATTURA,
           aDest.FL_BOLLA_DOGANALE,
           aDest.FL_SPEDIZIONIERE,
           aDest.FL_FATTURA_COMPENSO,
           aDest.CD_TERZO_CESSIONARIO,
           aDest.CD_TERZO_UO_CDS,
           aDest.CD_TERZO,
           aDest.RAGIONE_SOCIALE,
           aDest.NOME,
           aDest.COGNOME,
           aDest.CODICE_FISCALE,
           aDest.PARTITA_IVA,
           aDest.CD_TERMINI_PAG,
           aDest.CD_TERMINI_PAG_UO_CDS,
           aDest.PG_BANCA,
           aDest.PG_BANCA_UO_CDS,
           aDest.CD_MODALITA_PAG,
           aDest.CD_MODALITA_PAG_UO_CDS,
           aDest.IM_TOTALE_IMPONIBILE_DIVISA,
           aDest.IM_TOTALE_IMPONIBILE,
           aDest.IM_TOTALE_IVA,
           aDest.IM_TOTALE_FATTURA,
           aDest.CD_DIVISA,
           aDest.CAMBIO,
           aDest.STATO_COFI,
           aDest.STATO_COGE,
           aDest.DT_PAGAMENTO_FONDO_ECO,
           aDest.NUMERO_PROTOCOLLO,
           aDest.NOTE,
           aDest.DT_DA_COMPETENZA_COGE,
           aDest.DT_A_COMPETENZA_COGE,
           aDest.ESERCIZIO_LETTERA,
           aDest.PG_LETTERA,
           aDest.DACR,
           aDest.UTCR,
           aDest.DUVA,
           aDest.UTUV,
           aDest.PG_VER_REC,
           aDest.TI_ASSOCIATO_MANREV,
           aDest.STATO_COAN,
           aDest.STATO_PAGAMENTO_FONDO_ECO,
           aDest.IM_TOTALE_QUADRATURA,
           aDest.TI_BENE_SERVIZIO,
           aDest.CD_CDS_FAT_CLGS,
           aDest.CD_UO_FAT_CLGS,
           aDest.ESERCIZIO_FAT_CLGS,
           aDest.PG_FATTURA_PASSIVA_FAT_CLGS,
           aDest.FL_CONGELATA,
           aDest.fl_liquidazione_differita,
           aDest.data_protocollo,
           aDest.esercizio_compenso,
           aDest.cds_compenso,
           aDest.uo_compenso,
           aDest.pg_compenso);

END;

 procedure ins_FATTURA_PASSIVA_RIGA (aDest FATTURA_PASSIVA_RIGA%rowtype) is
  begin
   insert into FATTURA_PASSIVA_RIGA (
     PG_OBBLIGAZIONE_SCADENZARIO
    ,CD_CDS_ACCERTAMENTO
    ,ESERCIZIO_ACCERTAMENTO
    ,ESERCIZIO_ORI_ACCERTAMENTO
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS_ASSNCNA_FIN
    ,CD_UO_ASSNCNA_FIN
    ,ESERCIZIO_ASSNCNA_FIN
    ,PG_FATTURA_ASSNCNA_FIN
    ,PG_RIGA_ASSNCNA_FIN
    ,CD_CDS_ASSNCNA_ECO
    ,CD_UO_ASSNCNA_ECO
    ,ESERCIZIO_ASSNCNA_ECO
    ,PG_FATTURA_ASSNCNA_ECO
    ,PG_RIGA_ASSNCNA_ECO
    ,TI_ASSOCIATO_MANREV
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,PG_FATTURA_PASSIVA
    ,PROGRESSIVO_RIGA
    ,TI_ISTITUZ_COMMERC
    ,CD_BENE_SERVIZIO
    ,DS_RIGA_FATTURA
    ,PREZZO_UNITARIO
    ,QUANTITA
    ,IM_TOTALE_DIVISA
    ,IM_IMPONIBILE
    ,CD_VOCE_IVA
    ,FL_IVA_FORZATA
    ,IM_IVA
    ,IM_DIPONIBILE_NC
    ,STATO_COFI
    ,DT_DA_COMPETENZA_COGE
    ,DT_A_COMPETENZA_COGE
    ,DT_CANCELLAZIONE
    ,CD_CDS_OBBLIGAZIONE
    ,ESERCIZIO_OBBLIGAZIONE
    ,ESERCIZIO_ORI_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE
    ,CD_TERZO_CESSIONARIO
    ,CD_TERZO
    ,CD_TERMINI_PAG
    ,PG_BANCA
    ,CD_MODALITA_PAG
    ,data_esigibilita_iva
   ) values (
     aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.CD_CDS_ACCERTAMENTO
    ,aDest.ESERCIZIO_ACCERTAMENTO
    ,aDest.ESERCIZIO_ORI_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS_ASSNCNA_FIN
    ,aDest.CD_UO_ASSNCNA_FIN
    ,aDest.ESERCIZIO_ASSNCNA_FIN
    ,aDest.PG_FATTURA_ASSNCNA_FIN
    ,aDest.PG_RIGA_ASSNCNA_FIN
    ,aDest.CD_CDS_ASSNCNA_ECO
    ,aDest.CD_UO_ASSNCNA_ECO
    ,aDest.ESERCIZIO_ASSNCNA_ECO
    ,aDest.PG_FATTURA_ASSNCNA_ECO
    ,aDest.PG_RIGA_ASSNCNA_ECO
    ,aDest.TI_ASSOCIATO_MANREV
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.PG_FATTURA_PASSIVA
    ,aDest.PROGRESSIVO_RIGA
    ,aDest.TI_ISTITUZ_COMMERC
    ,aDest.CD_BENE_SERVIZIO
    ,aDest.DS_RIGA_FATTURA
    ,aDest.PREZZO_UNITARIO
    ,aDest.QUANTITA
    ,aDest.IM_TOTALE_DIVISA
    ,aDest.IM_IMPONIBILE
    ,aDest.CD_VOCE_IVA
    ,aDest.FL_IVA_FORZATA
    ,aDest.IM_IVA
    ,aDest.IM_DIPONIBILE_NC
    ,aDest.STATO_COFI
    ,aDest.DT_DA_COMPETENZA_COGE
    ,aDest.DT_A_COMPETENZA_COGE
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_CDS_OBBLIGAZIONE
    ,aDest.ESERCIZIO_OBBLIGAZIONE
    ,aDest.ESERCIZIO_ORI_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.CD_TERZO_CESSIONARIO
    ,aDest.CD_TERZO
    ,aDest.CD_TERMINI_PAG
    ,aDest.PG_BANCA
    ,aDest.CD_MODALITA_PAG
    ,aDest.data_esigibilita_iva
    );
 end;
procedure aggiorna_data_differita(es NUMBER,cds VARCHAR2,uo VARCHAR2,pg_fattura NUMBER,riga NUMBER) Is
Begin
  Update fattura_passiva_riga Set data_esigibilita_iva=Trunc(Sysdate)
  Where
    esercizio = es And
    cd_cds   = cds And
    cd_unita_organizzativa = uo And
    pg_fattura_passiva = pg_fattura And
    progressivo_riga=riga;
End;
procedure aggiorna_data_differita_attive(es NUMBER,cds VARCHAR2,uo VARCHAR2,pg_fattura NUMBER,riga NUMBER) Is
Begin
  Update fattura_attiva_riga Set data_esigibilita_iva=Trunc(Sysdate)
  Where
    esercizio = es And
    cd_cds   = cds And
    cd_unita_organizzativa = uo And
    pg_fattura_attiva = pg_fattura And
    progressivo_riga=riga;
End;
procedure insProgrUnivocoFatturaPassiva(es NUMBER,data_a date)is
num  varchar2(10);
begin
for fatture in
 (select * from fattura_passiva
 where
   esercizio            = es and
   dt_registrazione    > (select dt01 from configurazione_cnr where
   cd_chiave_primaria   = 'REGISTRO_UNICO_FATPAS' and
   cd_chiave_secondaria = 'DATA_INIZIO') and
   dt_registrazione <= data_a  and
   PROGR_UNIVOCO is null
order by esercizio,dt_registrazione,cd_cds,cd_unita_organizzativa,pg_fattura_passiva
for update nowait) loop
  begin
  select cd_corrente into num from numerazione_base where
    esercizio = es and
    colonna='PG_REGISTRO_UNICO_FATPAS' and
    tabella ='FATTURA_PASSIVA';
 exception when no_data_found then
	Insert into NUMERAZIONE_BASE
   (ESERCIZIO, COLONNA, TABELLA, CD_CORRENTE, CD_MASSIMO, DUVA, UTUV, DACR, UTCR, PG_VER_REC, CD_INIZIALE)
 Values
   (es, 'PG_REGISTRO_UNICO_FATPAS', 'FATTURA_PASSIVA', '0', '99999999', sysdate, 'RIBALTAMENTO', sysdate, 'RIBALTAMENTO', 1, '1');
   num:='0';
end;
update fattura_passiva
 	set PROGR_UNIVOCO= to_number(num)+1
where
			esercizio = fatture.esercizio and
      cd_cds    = fatture.cd_cds  and
      cd_unita_organizzativa = fatture.cd_unita_organizzativa and
      pg_fattura_passiva = fatture.pg_fattura_passiva;
update NUMERAZIONE_BASE set cd_corrente =(num+1) where esercizio = es and colonna='PG_REGISTRO_UNICO_FATPAS' and tabella='FATTURA_PASSIVA';
end loop;
exception when CNRCTB850.RESOURCE_BUSY then
      IBMERR001.RAISE_ERR_GENERICO('Funzionalit? temporaneamente non accessibile per l''esercizio e la data selezionata.');
end;
END;


