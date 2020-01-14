--------------------------------------------------------
--  DDL for Package CNRCTB035
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB035" as
--
-- CNRCTB035 - Package obbligazione/accertamento
-- Date: 12/07/2006
-- Version: 3.10
--
-- Package per la gestione DB dell'obbligazione/accertamento
--
-- Dependency: CNRCTB 001/008/054/018 IBMERR 001
--
-- History:
--
-- Date: 22/01/2002
-- Version: 1.0
-- Creazione
--
-- Date: 02/02/2002
-- Version: 1.1
-- Aggiunte le costanti di stato
--
-- Date: 20/02/2002
-- Version: 1.2
-- Modifica tabelle OBBLIGAZIONE
--
-- Date: 09/05/2002
-- Version: 1.3
-- Aggiunta la parte di gestione dell'accertamento
--
-- Date: 09/05/2002
-- Version: 1.4
-- Metodi di gestione scadenze acc/obb
--
-- Date: 10/05/2002
-- Version: 1.5
-- Modifiche per gestione partite di giro
--
-- Date: 11/05/2002
-- Version: 1.6
-- Test motore
--
-- Date: 25/06/2002
-- Version: 1.7
-- Fix aggiornamento saldi per generazione accertamento
--
-- Date: 08/07/2002
-- Version: 1.8
-- Aggiunti metodi di eliminazione logica di obbligazione e accertamento
--
-- Date: 08/07/2002
-- Version: 1.9
-- Fixx errore annulamento obb/acr
--
-- Date: 17/07/2002
-- Version: 2.0
-- Trunc dt_Registrazione e dt_scadenza in obbligazione/accertamento e derivati
--
-- Date: 18/07/2002
-- Version: 2.1
-- Aggiornata documentazione
--
-- Date: 21/07/2002
-- Version: 2.2
-- Aggiornamento dei saldi di sola competenza in creazione scadenza obbligazione
--
-- Date: 25/07/2002
-- Version: 2.3
-- Aggiunto metodo di aggiornamento saldo collegamento con doc amministrativi
--
-- Date: 25/07/2002
-- Version: 2.4
-- Aggiornamento su duva e pg_Ver_Rec
--
-- Date: 02/08/2002
-- Version: 2.5
-- Check di campienza su obb e acc in collegamento a doc amm
--
-- Date: 19/09/2002
-- Version: 2.6
-- Modificata struttura di aggiornamento dei saldi su obb e acc aggiungendo una chiamata di modifica dei saldi per delta
--
-- Date: 26/09/2002
-- Version: 2.7
-- Fix su gestione aggiornamento saldi su creazione/annullamento obb/acr
--
-- Date: 26/11/2002
-- Version: 2.8
-- Modificato INS_ACCERTAMENTO aggiunto il campo pg_accertamento_origine
--
-- Date: 17/12/2002
-- Version: 2.9
-- Aggiunto il type scadAcrList
--
-- Date: 20/02/2003
-- Version: 2.10
-- Metodi di descrizione accertamento/obbligazione
--
-- Date: 15/05/2003
-- Version: 3.0
-- Aggiunte funzioni di lettura pratiche su partita di giro
--
-- Date: 10/06/2003
-- Version: 3.1
-- Modificati annullaAccertamento(), annullaObbligazione() per gestione residui:
-- non viene impostata la data di cancellazione
-- Introduzione delle variazioni formali
-- Modifica dei metodi di insert su obbligazione/accertamento scadenzario
--
-- Date: 12/06/2003
-- Version: 3.2
-- Modifica ai metodi di aggiornamento dei saldi per gestione residui
--
-- Date: 16/06/2003
-- Version: 3.3
-- Modificata impostazione data cancellazione all'annullamento di accertamenti
-- e obbligazioni
-- Controllo delle stato riportato della contropartita pgiro per annullamento
--
-- Date: 19/06/2003
-- Version: 3.4
-- Tolto aggiornamento dei saldi per obbligazioni pluriennali
-- Controllo associazione a doc amm all'annullamento di accertamenti e obbligazioni
-- Nuovi metodi per lock di accertamento e obbligazione
-- Nuovi metodi per estrazione descrizione scadenza di obb/acc
--
-- Date: 01/07/2003
-- Version: 3.5
-- Modifica dei metodi di creazione variazioni formali su impegni
--
-- Date: 04/07/2003
-- Version: 3.6
-- Variazioni formali create solo ad esercizio precedente chiuso
--
-- Date: 04/07/2003
-- Version: 3.7
-- Fix metodi variazioni formali
--
-- Date: 06/08/2003
-- Version: 3.8
-- Introdotti metodi di annullamento della scadenz adi obbligazione e accertamento
--
-- Date: 19/04/2006
-- Version: 3.9
-- Aggiunto nella procedura GETPGIROCDSINV il parametro "aAccConScad" che per default è 'N' e solo se chiamato
-- dalla liquidazione CORI è 'Y' per indicare che occorre gestite le scadenze per le pgiro di entrata per consentire
-- la chiusura di una Reversale Provvisoria legata a gruppi CORI accentrati ancora aperti.
-- In questo caso, cioè se il gruppo è aperto ma la Rev. Provv. è chiusa, viene creata una nuova scadenza
-- per la pgiro di entrata ed una nuova rev. provv. a cui agganciarla
--
-- Date: 12/07/2006
-- Version: 3.10
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 07/11/2011
-- Version: 3.10
-- Gestito nella procedura GETPGIROCDSINV per il parametro "aAccConScad" anche il valore 'X'; in questo caso
-- viene baipassato il controllo secondo cui non sono gestite le scadenze per le pgiro di entrata
--
-- Constants:

STATO_DEFINITIVO CONSTANT CHAR(1):='D';
STATO_PROVVISORIO CONSTANT CHAR(1):='P';
STATO_STORNATO CONSTANT CHAR(1):='S';

-- Codici linee di attività speciali in CONFIGURAZIONE_CNR

LA_SPECIALE CONSTANT VARCHAR2(50) := 'LINEA_ATTIVITA_SPECIALE';
LA_SPESA_ENTE CONSTANT VARCHAR2(100) := 'LINEA_ATTIVITA_SPESA_ENTE';
LA_ENTRATA_ENTE CONSTANT VARCHAR2(100) := 'LINEA_ATTIVITA_ENTRATA_ENTE';

-- Codici terzi speciali in CONFIGURAZIONE_CNR

TERZO_SPECIALE CONSTANT VARCHAR2(100) := 'TERZO_SPECIALE';
CODICE_DIVERSI_PGIRO CONSTANT VARCHAR2(100) := 'CODICE_DIVERSI_PGIRO';

-- Dati per motore generazione obbligazioni automatico

type scadVoceListS is table of obbligazione_scad_voce%rowtype index by binary_integer;
LISTA_SCAD_VOCE_VUOTA_S scadVoceListS;
SCADENZA_VUOTA_S obbligazione_scadenzario%rowtype;

type scadVoceListE is table of accertamento_scad_voce%rowtype index by binary_integer;
LISTA_SCAD_VOCE_VUOTA_E scadVoceListE;
SCADENZA_VUOTA_E accertamento_scadenzario%rowtype;

type scadAcrList is table of accertamento_scadenzario%rowtype index by binary_integer;

-- Functions e Procedures:

procedure lockDoc(aObb IN OUT obbligazione%rowtype);
procedure lockDoc(aAcc IN OUT accertamento%rowtype);
procedure lockDocCheck(aObb IN OUT obbligazione%rowtype, aPgVerRec number);
procedure lockDocCheck(aAcc IN OUT accertamento%rowtype, aPgVerRec number);
procedure lockDocFull(aObb IN OUT obbligazione%rowtype);
procedure lockDocFull(aAcc IN OUT accertamento%rowtype);
procedure lockDocFullCheck(aObb IN OUT obbligazione%rowtype, aPgVerRec number);
procedure lockDocFullCheck(aAcc IN OUT accertamento%rowtype, aPgVerRec number);

function isAprePgiro(aObb obbligazione%rowtype) return boolean;
function isAprePgiro(aAcc accertamento%rowtype) return boolean;

-- Operazione di annullamento di accertamento
--
-- pre-post-name: Accertamento non trovato
-- pre: Viene cercata la testata dell'accertamento specificato per l'annullamento e non viene trovato
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Acertamento su partite di giro e contropartita collegata a doc amministrativi
-- pre: L'accertamento è su partite di giro e l'obbligazione di contropartita è già collegata a documenti amministrativi
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Annullamento dell'obbligazione
-- pre: Nessun'altra precondizione verificata
-- post:
--  Se l'accertamento è su partita di giro
--       Viene annullata l'obbligazione di contropartita invocando CNRCTB035.annullaObbligazione
--  Viene aggiornato il saldo del capitolo di bilancio
--  Viene aggiornata la testata dell'accertamento come segue:
--          dt_cancellazione = data variazione,
--          im_accertamento= 0
-- Per ogni scadenza dell'accertamento, viene aggiornato
--        im_scadenza=0
--        im_associato_doc_amm=0
--        im_associato_doc_contabile=0
--     Per ogni dettaglio di scadenza di accertamento viene aggiornato
--                  im_voce=0
--
-- Parametri:
--    aCdCds -> Codice cds accertamento
--    aEs -> Esercizio accertamento
--    aPgObbligazione -> Progressivo dell'accartamento
--    aUser -> utente che effettua l'accertamento

 procedure annullaAccertamento(
  aCdCds varchar2,
  aEs number,
  aEsOri number,
  aPgAccertamento number,
  aUser varchar2
 );

 procedure annullaScadAccertamento(
  aCdCds varchar2,
  aEs number,
  aEsOri number,
  aPgAccertamento number,
  aPgAccScad number,
  aUser varchar2
 );

-- Operazione di annullamento di obbligazione
--
-- pre-post-name: Obbligazione non trovata
-- pre: Viene cercata la testata dell'obbligazione specificata per l'annullamento e non viene trovata
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Obbligazione su partite di giro e contropartita collegata a doc amministrativi
-- pre: L'obbligazione è su partite di giro e l'accertamento di contropartita è già collegata a documenti amministrativi
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Annullamento dell'obbligazione
-- pre: Nessun'altra precondizione verificata
-- post:
--  Se l'obbligazione è su partita di giro
--       Viene annullato l'accertamento di contropartita invocando CNRCTB035.annullaAccertamento
--  Viene aggiornata la testata dell'obbligazione come segue:
--          stato_obbligazione=STATO_STORNATO,
--          dt_cancellazione = data variazione,
--          im_obbligazione = 0
-- Per ogni scadenza dell'obbligazione, viene aggiornato
--          im_scadenza=0
--          im_associato_doc_amm=0
--          im_associato_doc_contabile=0
--     Per ogni dettaglio di scadenza di obbligazione viene aggiornato
--                  im_voce=0
--                  Viene aggiornato il saldo del capitolo di bilancio
--
-- Parametri:
--    aCdCds -> Codice cds obbligazione
--    aEs -> Esercizio obbligazione
--    aPgObbligazione -> Progressivo dell'obbligazione
--    aUser -> utente che effettua l'operazione

 procedure annullaObbligazione(
  aCdCds varchar2,
  aEs number,
  aEsOri number,
  aPgObbligazione number,
  aUser varchar2
 );

 procedure annullaScadObbligazione(
  aCdCds varchar2,
  aEs number,
  aEsOri number,
  aPgObbligazione number,
  aPgObbScad number,
  aUser varchar2
 );

 -- Crea una scadenza di obbligazione
 procedure creaScadObbligazione(
  aObb IN OUT obbligazione%rowtype,
  aScadenza1 in out obbligazione_scadenzario%rowtype,
  posizione number,
  aDettScadenza1 in out scadVoceListS,
  isControlloBloccante boolean
 );

 -- Aggiorna il saldo capitolo per obbligazione
 procedure aggiornaSaldoDettScad(aObb obbligazione%rowtype, aObbScadVoce obbligazione_scad_voce%rowtype,aDelta number, isControlloBloccante boolean,aUser varchar2, aTSNow date);

 -- Crea una scadenza di accertamento
 procedure creaScadAccertamento(
  aAcc IN OUT accertamento%rowtype,
  aScadenza1 in out accertamento_scadenzario%rowtype,
  posizione number,
  aDettScadenza1 in out scadVoceListE
 );

 -- Aggiorna il saldo di collegamento con documenti amministrativi della scadenza di obbligazione
 procedure aggiornaSaldoDocammObb(aCdCds varchar2, aEs number, aEsOri number, aPg number, aPgScad number, aDelta number, aUser varchar2);
 -- Aggiorna il saldo di collegamento con documenti amministrativi della scadenza di accertamento
 procedure aggiornaSaldoDocammAcc(aCdCds varchar2, aEs number, aEsOri number, aPg number, aPgScad number, aDelta number, aUser varchar2);

 -- Aggiorna il saldo capitolo per accertamento per delta
 procedure aggiornaSaldoDettScad(aAcc accertamento%rowtype,aDelta number, isControlloBloccante boolean,aUser varchar2, aTSNow date);

-- Funzioni di inserimento linea obbligazione/impegno
 procedure ins_OBBLIGAZIONE (aDest OBBLIGAZIONE%rowtype);
 procedure ins_OBBLIGAZIONE_SCADENZARIO (aDest OBBLIGAZIONE_SCADENZARIO%rowtype);
 procedure ins_OBBLIGAZIONE_SCAD_VOCE (aDest OBBLIGAZIONE_SCAD_VOCE%rowtype);

-- Funzioni di inserimento linea accertamento
 procedure ins_ACCERTAMENTO (aDest ACCERTAMENTO%rowtype);
 procedure ins_ACCERTAMENTO_SCADENZARIO (aDest ACCERTAMENTO_SCADENZARIO%rowtype);
 procedure ins_ACCERTAMENTO_SCAD_VOCE (aDest ACCERTAMENTO_SCAD_VOCE%rowtype);

-- Funzioni di inserimento associazione tra documenti su partita di giro
 procedure ins_ASS_OBB_ACR_PGIRO (aDest ASS_OBB_ACR_PGIRO%rowtype);

-- Ritorna una stringa di descrizione dell'obbligazione
 function getDesc(aObb obbligazione%rowtype) return varchar2;

-- Ritorna una stringa di descrizione dell'accertamento
 function getDesc(aAcc accertamento%rowtype) return varchar2;

-- Ritorna una stringa di descrizione dell'obbligazione scadenza
 function getDesc(aObb obbligazione%rowtype, aObbScad obbligazione_scadenzario%rowtype) return varchar2;

-- Ritorna una stringa di descrizione dell'accertamento
 function getDesc(aAcc accertamento%rowtype, aAccScad accertamento_scadenzario%rowtype) return varchar2;

-- Legge la partita di giro CDS aperta dall'accertamento
-- Parametri:
--    aAcc -> accertamento (valorizzare cd_cds,esercizio,esercizio_originale,pg_accertamento)
--    aAccScad -> Scadenza
--    aAccScadVoce -> Scad voce
--    aObb -> obbligazione
--    aObbScad -> Scadenza
--    aObbScadVoce -> Scad voce
 procedure getPgiroCds(
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%rowtype,
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype
 );

-- Legge la partita di giro CDS aperta dall'obbligazione entrando con l'accertamento
-- Parametri:
--    aAcc -> accertamento (valorizzare cd_cds,esercizio,esercizio_originale,pg_accertamento)
--    aAccScad -> Scadenza
--    aAccScadVoce -> Scad voce
--    aObb -> obbligazione
--    aObbScad -> Scadenza
--    aObbScadVoce -> Scad voce
 procedure getPgiroCdsInv(
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%rowtype,
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype
 );

-- Legge la partita di giro CDS aperta dall'obbligazione
-- Parametri:
--    aObb -> obbligazione (valorizzare cd_cds,esercizio,esercizio_originale,pg_obbligazione)
--    aObbScad -> Scadenza
--    aObbScadVoce -> Scad voce
--    aAcc -> accertamento
--    aAccScad -> Scadenza
--    aAccScadVoce -> Scad voce
 procedure getPgiroCds(
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype,
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%rowtype
 );

-- Legge la partita di giro CDS aperta dall'accertamento entrando con l'obbligazione
-- Parametri:
--    aObb -> obbligazione (valorizzare cd_cds,esercizio,esercizio_originale,pg_obbligazione)
--    aObbScad -> Scadenza
--    aObbScadVoce -> Scad voce
--    aAcc -> accertamento
--    aAccScad -> Scadenza
--    aAccScadVoce -> Scad voce

 procedure getPgiroCdsInv(
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype,
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%Rowtype,
    aAccConScad  CHAR Default 'N',
    aTSNow DATE Default Null,
    aUser VARCHAR2 Default Null
 );

procedure creaVariazioneFormaleAcc(oldAcc accertamento%rowtype, newAcc accertamento%rowtype);
procedure ins_VARIAZIONE_FORMALE_ACC (aDest VARIAZIONE_FORMALE_ACC%rowtype);
procedure creaVariazioneFormaleImpAU(oldObbScadVoce obbligazione_scad_voce%rowtype,newObbScadVoce obbligazione_scad_voce%rowtype);
procedure ins_VARIAZIONE_FORMALE_IMP (aDest VARIAZIONE_FORMALE_IMP%rowtype);
procedure creaVariazioneFormaleImpAD(oldObbScadVoce obbligazione_scad_voce%rowtype);
procedure creaVariazioneFormaleImpAI(newObbScadVoce obbligazione_scad_voce%rowtype);
procedure sdoppiaScadenzaAccertamento(aAccScad accertamento_scadenzario%rowtype,aNewImp number,aUser varchar2);
end;
