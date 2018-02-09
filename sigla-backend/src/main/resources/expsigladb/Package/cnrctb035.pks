CREATE OR REPLACE package CNRCTB035 as
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
-- Aggiunto nella procedura GETPGIROCDSINV il parametro "aAccConScad" che per default ? 'N' e solo se chiamato
-- dalla liquidazione CORI ? 'Y' per indicare che occorre gestite le scadenze per le pgiro di entrata per consentire
-- la chiusura di una Reversale Provvisoria legata a gruppi CORI accentrati ancora aperti.
-- In questo caso, cio? se il gruppo ? aperto ma la Rev. Provv. ? chiusa, viene creata una nuova scadenza
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

-- Codici linee di attivit? speciali in CONFIGURAZIONE_CNR

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
-- pre: L'accertamento ? su partite di giro e l'obbligazione di contropartita ? gi? collegata a documenti amministrativi
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Annullamento dell'obbligazione
-- pre: Nessun'altra precondizione verificata
-- post:
--  Se l'accertamento ? su partita di giro
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
-- pre: L'obbligazione ? su partite di giro e l'accertamento di contropartita ? gi? collegata a documenti amministrativi
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Annullamento dell'obbligazione
-- pre: Nessun'altra precondizione verificata
-- post:
--  Se l'obbligazione ? su partita di giro
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
end;
/


CREATE OR REPLACE package body CNRCTB035 is

 function isAprePgiro(aObb obbligazione%rowtype) return boolean is
  aNum number;
 begin
  select 1 into aNum from dual where exists (select 1 from ass_obb_acr_pgiro
                                             Where cd_cds = aObb.cd_cds
                                               And esercizio = aObb.esercizio
                                               And esercizio_ori_obbligazione = aObb.esercizio_originale
					       And pg_obbligazione = aObb.pg_obbligazione
					       And ti_origine = CNRCTB001.GESTIONE_SPESE
								  );
  return true;
 exception when NO_DATA_FOUND then
  return false;
 end;

 function isAprePgiro(aAcc accertamento%rowtype) return boolean is
  aNum number;
 begin
  select 1 into aNum from dual where exists (select 1 from ass_obb_acr_pgiro where
                                                 cd_cds = aAcc.cd_cds
					     And esercizio = aAcc.esercizio
					     And esercizio_ori_accertamento = aAcc.esercizio_originale
					     And pg_accertamento = aAcc.pg_accertamento
					     And ti_origine = CNRCTB001.GESTIONE_ENTRATE);
  return true;
 exception when NO_DATA_FOUND then
  return false;
 end;

 procedure lockDoc(aObb IN OUT obbligazione%rowtype) is
 begin
  lockDocCheck(aObb,null);
 end;

 procedure lockDoc(aAcc IN OUT accertamento%rowtype) is
 begin
  lockDocCheck(aAcc,null);
 end;

 procedure lockDocCheck(aObb IN OUT obbligazione%rowtype, aPgVerRec number) is
 begin
  begin
   select * into aObb from obbligazione Where
          cd_cds=  aObb.cd_cds
		  and esercizio = aObb.esercizio
                  And esercizio_originale = aObb.esercizio_originale
		  and pg_obbligazione = aObb.pg_obbligazione
		  for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Documento non trovato:'||getDesc(aObb));
  end;
  if aPgVerRec is not null then
   if aObb.pg_ver_rec != aPgVerRec then
    IBMERR001.RAISE_ERR_GENERICO('Risorsa non pi? valida:'||getDesc(aObb));
   end if;
  end if;
 end;

 procedure lockDocCheck(aAcc IN OUT accertamento%rowtype, aPgVerRec number) is
 begin
  begin
   select * into aAcc from accertamento where
          cd_cds=  aAcc.cd_cds
		  and esercizio = aAcc.esercizio
		  and esercizio_originale = aAcc.esercizio_originale
		  and pg_accertamento = aAcc.pg_accertamento
		  for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Documento non trovato:'||getDesc(aAcc));
  end;
  if aPgVerRec is not null then
   if aAcc.pg_ver_rec != aPgVerRec then
    IBMERR001.RAISE_ERR_GENERICO('Risorsa non pi? valida:'||getDesc(aAcc));
   end if;
  end if;
 end;

 procedure lockDocFull(aObb IN OUT obbligazione%rowtype) is
 begin
  lockDocFullCheck(aObb,null);
 end;

 procedure lockDocFullCheck(aObb IN OUT obbligazione%rowtype, aPgVerRec number) is
 begin
  lockDocCheck(aObb,aPgVerRec);
  for aObbScad in (select * from obbligazione_scadenzario where
          cd_cds=  aObb.cd_cds
		  and esercizio = aObb.esercizio
                  and esercizio_originale = aObb.esercizio_originale
		  and pg_obbligazione = aObb.pg_obbligazione
	     for update nowait) loop
	null;
  end loop;
  for aObbScadVoce in (select * from obbligazione_scad_voce where
          cd_cds=  aObb.cd_cds
		  and esercizio = aObb.esercizio
		  and esercizio_originale = aObb.esercizio_originale
		  and pg_obbligazione = aObb.pg_obbligazione
	     for update nowait) loop
	null;
  end loop;
 end;

 procedure lockDocFull(aAcc IN OUT accertamento%rowtype) is
 begin
  lockDocFullCheck(aAcc,null);
 end;

 procedure lockDocFullCheck(aAcc IN OUT accertamento%rowtype,aPgVerRec number) is
 begin
  lockDocCheck(aAcc,aPgVerRec);
  for aAccScad in (select * from accertamento_scadenzario where
          cd_cds=  aAcc.cd_cds
		  and esercizio = aAcc.esercizio
		  and esercizio_originale = aAcc.esercizio_originale
		  and pg_accertamento = aAcc.pg_accertamento
	     for update nowait) loop
	null;
  end loop;
  for aAccScadVoce in (select * from accertamento_scad_voce where
          cd_cds=  aAcc.cd_cds
		  and esercizio = aAcc.esercizio
		  and esercizio_originale = aAcc.esercizio_originale
		  and pg_accertamento = aAcc.pg_accertamento
	     for update nowait) loop
	null;
  end loop;
 end;

 function getDesc(aAcc accertamento%rowtype) return varchar2 is
 begin
  return 'n.:'||aAcc.pg_accertamento||' cds:'||aAcc.cd_cds||' es.:'||aAcc.esercizio||' esOri.:'||aAcc.esercizio_originale||' Uo Ori: '||aAcc.cd_uo_origine;
 End;

 function getDesc(aAcc accertamento%rowtype, aAccScad accertamento_scadenzario%rowtype) return varchar2 is
 begin
  return getDesc(aAcc)||' scad:'||aAccScad.pg_accertamento_scadenzario;
 end;

 function getDesc(aObb obbligazione%rowtype) return varchar2 is
 begin
  return 'n.:'||Nvl(aObb.pg_obbligazione, 0)||' cds:'||Nvl(aObb.cd_cds, 0)||' es.:'||Nvl(aObb.esercizio, 0)||' esOri.:'||Nvl(aObb.esercizio_originale, 0)||' Uo Ori: '||aObb.cd_uo_origine;
 End;

 function getDesc(aObb obbligazione%rowtype, aObbScad obbligazione_scadenzario%rowtype) return varchar2 is
 begin
  return getDesc(aObb)||' scad:'||aObbScad.pg_obbligazione_scadenzario;
 end;

 procedure aggiornaSaldoDocammObb(aCdCds varchar2, aEs number, aEsOri number, aPg number, aPgScad number, aDelta number, aUser varchar2) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
 begin
  if aDelta is null then
   IBMERR001.RAISE_ERR_GENERICO('Importo di variazione non valorizzato per '||cnrutil.getLabelObbligazioneMin()||' n.'||aPg||' cds:'||aCdCds||' esercizio:'||aEs);
  end if;
  begin
   select * into aObb from obbligazione where
        cd_cds = aCdCds
    and esercizio = aEs
    and esercizio_originale = aEsOri
    and pg_obbligazione = aPg
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' non trovata n.'||aPg||' cds:'||aCdCds||' esercizio:'||aEs);
  end;
  begin
   select * into aObbScad from obbligazione_scadenzario where
         cd_cds = aCdCds
     and esercizio = aEs
     and esercizio_originale = aEsOri
     and pg_obbligazione = aPg
     and pg_obbligazione_scadenzario = aPgScad
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Sadenza '||cnrutil.getLabelObbligazioneMin()||' non trovata n.'||aPg||' n. scad.'||aPgScad||' cds:'||aCdCds||' esercizio:'||aEs);
  end;
  if aObbScad.im_associato_doc_amm + aDelta < 0 then
   IBMERR001.RAISE_ERR_GENERICO('La variazione di '||aDelta||' rende negativo il saldo del collegamento a doc amministrativi per obb. n.'||aPg||' n. scad.'||aPgScad||' cds:'||aCdCds||' esercizio:'||aEs);
  end if;
  if aObbScad.im_scadenza - (aObbScad.im_associato_doc_amm + aDelta) < 0 then
   IBMERR001.RAISE_ERR_GENERICO('La scadenza risulta completamente collegata a documenti amministrativi per obb. n.'||aPg||' n. scad.'||aPgScad||' cds:'||aCdCds||' esercizio:'||aEs);
  end if;
  update obbligazione_scadenzario
     set
	   im_associato_doc_amm = im_associato_doc_amm + aDelta
	  ,pg_ver_rec=pg_ver_rec+1
	  ,duva=sysdate
	  ,utuv=aUser
  where
       cd_cds = aCdCds
   and esercizio = aEs
   and esercizio_originale = aEsOri
   and pg_obbligazione = aPg
   and pg_obbligazione_scadenzario = aPgScad;
 end;

 procedure aggiornaSaldoDocammAcc(aCdCds varchar2, aEs number, aEsOri number, aPg number, aPgScad number, aDelta number, aUser varchar2) is
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
 begin
  if aDelta is null then
   IBMERR001.RAISE_ERR_GENERICO('Importo di variazione non valorizzato per accertamento n.'||aPg||' cds:'||aCdCds||' esercizio:'||aEs);
  end if;
  begin
   select * into aAcc from accertamento where
        cd_cds = aCdCds
    and esercizio = aEs
    and esercizio_originale = aEsOri
    and pg_accertamento = aPg
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Accertamento non trovato n.'||aPg||' cds:'||aCdCds||' esercizio:'||aEs||' esOri.:'||aEsOri);
  end;
  begin
   select * into aAccScad from accertamento_scadenzario where
         cd_cds = aCdCds
     and esercizio = aEs
     and esercizio_originale = aEsOri
 	 and pg_accertamento = aPg
     and pg_accertamento_scadenzario = aPgScad
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scadenza accertamento non trovata n.'||aPg||' n. scad.'||aPgScad||' cds:'||aCdCds||' esercizio:'||aEs||' esOri.:'||aEsOri);
  end;
  if aAccScad.im_associato_doc_amm + aDelta < 0 then
   IBMERR001.RAISE_ERR_GENERICO('La variazione di '||aDelta||' rende negativo il saldo del collegamento a doc amministrativi per acc. n.'||aPg||' n. scad.'||aPgScad||' cds:'||aCdCds||' esercizio:'||aEs);
  end if;
  if aAccScad.im_scadenza - (aAccScad.im_associato_doc_amm + aDelta) < 0 then
   IBMERR001.RAISE_ERR_GENERICO('La scadenza risulta completamente collegata a documenti amministrativi per acc. n.'||aPg||' n. scad.'||aPgScad||' cds:'||aCdCds||' esercizio:'||aEs);
  end if;
  update accertamento_scadenzario
     set
	   im_associato_doc_amm = im_associato_doc_amm + aDelta
	  ,pg_ver_rec=pg_ver_rec+1
	  ,duva=sysdate
	  ,utuv=aUser
  where
       cd_cds = aCdCds
   and esercizio = aEs
   and esercizio_originale = aEsOri
   and pg_accertamento = aPg
   and pg_accertamento_scadenzario = aPgScad;
 end;

 procedure annullaAccertamento(
  aCdCds varchar2,
  aEs number,
  aEsOri number,
  aPgAccertamento number,
  aUser varchar2
 ) is
  aAcc accertamento%rowtype;
  aTSNow date;
  aObb obbligazione%rowtype;
  aAss ass_obb_acr_pgiro%rowtype;
  isRiportato char(1);
  aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;
 begin
  aTSNow:=sysdate;
  begin
   select * into aAcc from accertamento where
             cd_cds = aCdCds
		 and esercizio = aEs
		 and esercizio_originale = aEsOri
		 and pg_accertamento = aPgAccertamento
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Annotazione di entrata non trovata n.'||aPgAccertamento||' cds:'||aCdCds||' es.'||aEs);
  end;
  -- Verifico che l'accertamento non si aassociato a doc amm
  for aAccScad in (select * from accertamento_scadenzario
  	  		   	   where cd_cds = aAcc.cd_cds
				     and esercizio = aAcc.esercizio
                      		     and esercizio_originale = aAcc.esercizio_originale
					 and pg_accertamento = aAcc.pg_accertamento
					 and im_associato_doc_amm <> 0
					 for update nowait) loop
	 ibmerr001.RAISE_ERR_GENERICO('Esistono scadenze collegate a documenti amministrativi sull''accertamento n. '||aAcc.pg_accertamento||' cds: '||aAcc.cd_cds||' es: '||aAcc.esercizio||' esOri.:'||aAcc.esercizio_originale);
  end loop;

  -- Se obbligazione su partita di giro devo annullare anche la contropartita
  -- Questo solo se sulla contropartita non ci sono documenti
  -- e se la contropartita non ? gi? riportata
  if aAcc.fl_pgiro='Y' then
   begin
    select * into aAss from ass_obb_acr_pgiro where
                cd_cds = aAcc.cd_cds
	   	    and esercizio = aAcc.esercizio
		    and esercizio_ori_accertamento = aAcc.esercizio_originale
		    and pg_accertamento = aAcc.pg_accertamento
			and ti_origine = CNRCTB001.GESTIONE_ENTRATE;
    for aObbScad in (select * from obbligazione_scadenzario where
                          cd_cds = aAss.cd_cds
 					 and esercizio = aAss.esercizio
 					 and esercizio_originale = aAss.esercizio_ori_obbligazione
 					 and pg_obbligazione = aAss.pg_obbligazione
 					 and im_associato_doc_amm != 0
 					for update nowait) loop
     IBMERR001.RAISE_ERR_GENERICO('Esistono scadenze collegate a documenti amministrativi sull'''||cnrutil.getLabelObbligazioneMin()||' di partita di giro n.'||aAss.pg_obbligazione||' cds:'||aCdCds||' es.:'||aEs||' esOri.:'||aAss.esercizio_ori_obbligazione);
    end loop;
	-- verifico se la contropartita ? riportata
	select riportato into isRiportato
	from obbligazione
	where cd_cds = aAss.cd_cds
	  and esercizio = aAss.esercizio
	  and esercizio_originale = aAss.esercizio_ori_obbligazione
	  and pg_obbligazione = aAss.pg_obbligazione;
	if isRiportato = 'Y' then
		IBMERR001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioneMin()||' di partita di giro n.'||aAss.pg_obbligazione||' cds:'||aCdCds||' es.:'||aEs||' esOri.:'||aAss.esercizio_ori_obbligazione||' ? gi? riportata');
	end if;
    annullaObbligazione(
     aAss.cd_cds,
     aAss.esercizio,
     aAss.esercizio_ori_obbligazione,
     aAss.pg_obbligazione,
     aUser
    );
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;
  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  For aAccScad_voce in (select * from accertamento_scad_voce
                        Where cd_cds = aAcc.cd_cds
                          And esercizio = aAcc.esercizio
 			  And esercizio_originale = aAcc.esercizio_originale
 			  And pg_accertamento = aAcc.pg_accertamento) Loop
    aSaldoCdrLineaAcc.ESERCIZIO := aAccScad_voce.ESERCIZIO;
    aSaldoCdrLineaAcc.ESERCIZIO_RES := aAcc.ESERCIZIO_ORIGINALE;
    aSaldoCdrLineaAcc.CD_CENTRO_RESPONSABILITA := aAccScad_voce.CD_CENTRO_RESPONSABILITA;
    aSaldoCdrLineaAcc.CD_LINEA_ATTIVITA := aAccScad_voce.CD_LINEA_ATTIVITA;
    aSaldoCdrLineaAcc.TI_APPARTENENZA := aAcc.TI_APPARTENENZA;
    aSaldoCdrLineaAcc.TI_GESTIONE := aAcc.TI_GESTIONE;
    aSaldoCdrLineaAcc.CD_VOCE := aAcc.CD_VOCE;

    CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLineaAcc);

         If aAcc.esercizio = aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_ACC_COMP := 0 - aAccScad_voce.IM_VOCE;
         Elsif aAcc.esercizio > aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_RES_PRO := 0 - aAccScad_voce.IM_VOCE;
         End If;

    aSaldoCdrLineaAcc.UTUV := aAcc.utcr;

    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLineaAcc, '035.annullaAccertamento', 'N');
  End Loop;

  aggiornaSaldoDettScad(aAcc,0-aAcc.im_accertamento,false,aUser,aTSNow);

  if aAcc.CD_TIPO_DOCUMENTO_CONT <> 'ACR_RES' then
	  update accertamento set
		   dt_cancellazione = TRUNC(CNRCTB008.GETTIMESTAMPCONTABILE(aEs,aTSNow)),
	       im_accertamento = 0,
		   utuv=aUser,
		   duva=aTSNow,
		   pg_ver_rec = pg_ver_rec+1
	  where
	       cd_cds = aAcc.cd_cds
	   and esercizio = aAcc.esercizio
	   and esercizio_originale = aAcc.esercizio_originale
	   and pg_accertamento = aAcc.pg_accertamento;
  else
	  update accertamento set
	       im_accertamento = 0, -- non viene impostata la data di cancellazione
		   utuv=aUser,
		   duva=aTSNow,
		   pg_ver_rec = pg_ver_rec+1
	  where
	       cd_cds = aAcc.cd_cds
	   and esercizio = aAcc.esercizio
	   and esercizio_originale = aAcc.esercizio_originale
	   and pg_accertamento = aAcc.pg_accertamento;
  end if;

  for aAccScad in (
   select * from accertamento_scadenzario where
        cd_cds = aAcc.cd_cds
    and esercizio = aAcc.esercizio
    and esercizio_originale = aAcc.esercizio_originale
    and pg_accertamento = aAcc.pg_accertamento
   for update nowait
  ) loop
   update accertamento_scadenzario set
       im_scadenza=0,
       im_associato_doc_amm=0,
       im_associato_doc_contabile=0,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec = pg_ver_rec+1
   where
        cd_cds = aAccScad.cd_cds
    and esercizio = aAccScad.esercizio
    and esercizio_originale = aAccScad.esercizio_originale
    and pg_accertamento = aAccScad.pg_accertamento
	and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;
   for aAccScadVoce in (
   select * from accertamento_scad_voce where
         cd_cds = aAccScad.cd_cds
     and esercizio = aAcc.esercizio
     and esercizio_originale = aAccScad.esercizio_originale
     and pg_accertamento = aAccScad.pg_accertamento
     and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario
    for update nowait
   ) loop
    update accertamento_scad_voce set
       im_voce=0,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec = pg_ver_rec+1
    where
         cd_cds = aAccScad.cd_cds
     and esercizio = aAcc.esercizio
     and esercizio_originale = aAccScad.esercizio_originale
     and pg_accertamento = aAccScad.pg_accertamento
     and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario
	 and pg_accertamento_scadenzario = aAccScadVoce.pg_accertamento_scadenzario
	 and cd_centro_responsabilita = aAccScadVoce.cd_centro_responsabilita
	 and cd_linea_attivita = aAccScadVoce.cd_linea_attivita;
   end loop;
  end loop;
 end;

 procedure annullaScadAccertamento(
  aCdCds varchar2,
  aEs number,
  aEsOri number,
  aPgAccertamento number,
  aPgAccScad number,
  aUser varchar2
 ) is
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aTSNow date;
  aObb obbligazione%rowtype;
  aAss ass_obb_acr_pgiro%rowtype;
  isRiportato char(1);
  isCancellato char(1);
  aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;
 Begin
  aTSNow:=sysdate;
  begin
   select * into aAcc from accertamento where
             cd_cds = aCdCds
		 and esercizio = aEs
		 and esercizio_originale = aEsOri
		 and pg_accertamento = aPgAccertamento
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Documento di entrata non trovato n.'||aPgAccertamento||' cds:'||aCdCds||' es.'||aEs||' esOri.:'||aEsOri);
  end;
  begin
   select * into aAccScad from accertamento_scadenzario where
             cd_cds = aCdCds
		 and esercizio = aEs
		 and esercizio_originale = aEsOri
		 and pg_accertamento = aPgAccertamento
		 and pg_accertamento_scadenzario = aPgAccScad
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scadenza n.'||aPgAccScad||' di documento di entrata n.'||aPgAccertamento||' cds:'||aCdCds||' es.'||aEs||' esOri.:'||aEsOri||' non trovata');
  end;
  -- Verifico che l'accertamento non si associato a doc amm
  if aAccScad.im_associato_doc_amm <> 0 then
   ibmerr001.RAISE_ERR_GENERICO('Scadenza n.'||aPgAccScad||' collegate a documenti amministrativi sul documento di entrata n. '||aAcc.pg_accertamento||' cds: '||aAcc.cd_cds||' es: '||aAcc.esercizio||' esOri.:'||aAcc.esercizio_originale);
  end if;

  -- Se obbligazione su partita di giro devo annullare anche la contropartita
  -- Questo solo se sulla contropartita non ci sono documenti
  -- e se la contropartita non ? gi? riportata
  if aAcc.fl_pgiro='Y' then
   begin
    select * into aAss from ass_obb_acr_pgiro where
                cd_cds = aAcc.cd_cds
	   	    and esercizio = aAcc.esercizio
		    and esercizio_ori_accertamento = aAcc.esercizio_originale
		    and pg_accertamento = aAcc.pg_accertamento
			and ti_origine = CNRCTB001.GESTIONE_ENTRATE;
    for aObbScad in (select * from obbligazione_scadenzario where
                          cd_cds = aAss.cd_cds
 					 and esercizio = aAss.esercizio
 					 and esercizio_originale = aAss.esercizio_ori_obbligazione
 					 and pg_obbligazione = aAss.pg_obbligazione
 					 and im_associato_doc_amm != 0
 					for update nowait) loop
     IBMERR001.RAISE_ERR_GENERICO('Esistono scadenze collegate a documenti amministrativi sull'''||cnrutil.getLabelObbligazioneMin()||' di partita di giro n.'||aAss.pg_obbligazione||' cds:'||aCdCds||' es.'||aEs||' esOri.:'||aAss.esercizio_ori_obbligazione);
    end loop;
	-- verifico se la contropartita ? riportata
	select riportato into isRiportato
	from obbligazione
	where cd_cds = aAss.cd_cds
	  and esercizio = aAss.esercizio
	  and esercizio_originale = aAss.esercizio_ori_obbligazione
	  and pg_obbligazione = aAss.pg_obbligazione;
	if isRiportato = 'Y' then
		IBMERR001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioneMin()||' di partita di giro n.'||aAss.pg_obbligazione||' cds:'||aCdCds||' es.:'||aEs||' esOri.:'||aAss.esercizio_ori_obbligazione||' ? gi? riportata');
	end if;
    annullaObbligazione(
     aAss.cd_cds,
     aAss.esercizio,
     aAss.esercizio_ori_obbligazione,
     aAss.pg_obbligazione,
     aUser
    );
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;
  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  For aAccScad_voce in (select * from accertamento_scad_voce
                        Where cd_cds = aAcc.cd_cds
 			  and esercizio = aAcc.esercizio
 			  And esercizio_originale = aAcc.esercizio_originale
	                  and pg_accertamento = aAcc.pg_accertamento) Loop
    aSaldoCdrLineaAcc.ESERCIZIO := aAccScad_voce.ESERCIZIO;
    aSaldoCdrLineaAcc.ESERCIZIO_RES := aAcc.ESERCIZIO_ORIGINALE;
    aSaldoCdrLineaAcc.CD_CENTRO_RESPONSABILITA := aAccScad_voce.CD_CENTRO_RESPONSABILITA;
    aSaldoCdrLineaAcc.CD_LINEA_ATTIVITA := aAccScad_voce.CD_LINEA_ATTIVITA;
    aSaldoCdrLineaAcc.TI_APPARTENENZA := aAcc.TI_APPARTENENZA;
    aSaldoCdrLineaAcc.TI_GESTIONE := aAcc.TI_GESTIONE;
    aSaldoCdrLineaAcc.CD_VOCE := aAcc.CD_VOCE;

    CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLineaAcc);

         If aAcc.esercizio = aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_ACC_COMP := 0 - aAccScad_voce.IM_VOCE;
         Elsif aAcc.esercizio > aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_RES_PRO := 0 - aAccScad_voce.IM_VOCE;
         End If;

    aSaldoCdrLineaAcc.UTUV := aAcc.utcr;

    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLineaAcc, '035.annullaScadAccertamento', 'N');
  End Loop;
  aggiornaSaldoDettScad(aAcc,0-aAcc.im_accertamento,false,aUser,aTSNow);

  isCancellato:='N';
  if aAcc.im_accertamento - aAccScad.im_scadenza = 0 then
   isCancellato:='Y';
  end if;

  if aAcc.CD_TIPO_DOCUMENTO_CONT <> 'ACR_RES' then
	  update accertamento set
		   dt_cancellazione = decode(isCancellato,'Y',TRUNC(CNRCTB008.GETTIMESTAMPCONTABILE(aEs,aTSNow)),dt_cancellazione),
	       im_accertamento = im_accertamento - aAccScad.im_scadenza,
		   utuv=aUser,
		   duva=aTSNow,
		   pg_ver_rec = pg_ver_rec+1
	  where
	       cd_cds = aAcc.cd_cds
	   and esercizio = aAcc.esercizio
	   and esercizio_originale = aAcc.esercizio_originale
	   and pg_accertamento = aAcc.pg_accertamento;
  else
	  update accertamento set
	       im_accertamento =im_accertamento - aAccScad.im_scadenza, -- non viene impostata la data di cancellazione
		   utuv=aUser,
		   duva=aTSNow,
		   pg_ver_rec = pg_ver_rec+1
	  where
	       cd_cds = aAcc.cd_cds
	   and esercizio = aAcc.esercizio
	   and esercizio_originale = aAcc.esercizio_originale
	   and pg_accertamento = aAcc.pg_accertamento;
  end if;

  update accertamento_scadenzario set
       im_scadenza=0,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec = pg_ver_rec+1
  where
        cd_cds = aAccScad.cd_cds
    and esercizio = aAccScad.esercizio
    and esercizio_originale = aAccScad.esercizio_originale
    and pg_accertamento = aAccScad.pg_accertamento
	and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;
  for aAccScadVoce in (
   select * from accertamento_scad_voce where
         cd_cds = aAccScad.cd_cds
     and esercizio = aAcc.esercizio
     and esercizio_originale = aAccScad.esercizio_originale
     and pg_accertamento = aAccScad.pg_accertamento
     and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario
    for update nowait
  ) loop
   update accertamento_scad_voce set
       im_voce=0,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec = pg_ver_rec+1
   where
         cd_cds = aAccScad.cd_cds
     and esercizio = aAcc.esercizio
     and esercizio_originale = aAccScad.esercizio_originale
     and pg_accertamento = aAccScad.pg_accertamento
     and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario
	 and pg_accertamento_scadenzario = aAccScadVoce.pg_accertamento_scadenzario
	 and cd_centro_responsabilita = aAccScadVoce.cd_centro_responsabilita
	 and cd_linea_attivita = aAccScadVoce.cd_linea_attivita;
  end loop;
 end;

 procedure annullaObbligazione(
  aCdCds varchar2,
  aEs number,
  aEsOri number,
  aPgObbligazione number,
  aUser varchar2
 ) is
  aObb obbligazione%rowtype;
  aTSNow date;
  aAcc accertamento%rowtype;
  aAss ass_obb_acr_pgiro%rowtype;
  isRiportato char(1);
  aSaldoCdrLinea voce_f_saldi_cdr_linea%Rowtype;
  aDeltaSaldo voce_f_saldi_cdr_linea%Rowtype;

 begin
  aTSNow:=sysdate;
  begin
   select * into aObb from obbligazione where
             cd_cds = aCdCds
		 and esercizio = aEs
		 and esercizio_originale = aEsOri
		 and pg_obbligazione = aPgObbligazione
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Annotazione di spesa non trovata n.'||aPgObbligazione||' cds:'||aCdCds||' es.:'||aEs||' esOri.:'||aEsOri);
  end;

-- Verifico che l'obbligazione non sia associata a documenti amministrativi
For aObbScad in (select * from obbligazione_scadenzario
  	  		   	   where cd_cds = aObb.cd_cds
				     and esercizio = aObb.esercizio
				     and esercizio_originale = aObb.esercizio_originale
					 and pg_obbligazione = aObb.pg_obbligazione
					 and im_associato_doc_amm <> 0
					 for update nowait) loop
  ibmerr001.RAISE_ERR_GENERICO('Esistono scadenze associate a documenti amministrativi sull'''||cnrutil.getLabelObbligazioneMin()||'  n.'||aObb.pg_obbligazione||' cds:'||aObb.cd_cds||' es.'||aObb.esercizio||' esOri.:'||aObb.esercizio_originale);
End loop;

  -- Se obbligazione su partita di giro devo annullare anche la contropartita
  -- Questo solo se sulla contropartita non ci sono documenti
  -- e se non ? gi? riportata
  if aObb.fl_pgiro='Y' then

   begin
    select * into aAss from ass_obb_acr_pgiro where
                cd_cds = aObb.cd_cds
	   	    and esercizio = aObb.esercizio
		    and esercizio_ori_obbligazione = aObb.esercizio_originale
		    and pg_obbligazione = aObb.pg_obbligazione
			and ti_origine = CNRCTB001.GESTIONE_SPESE;
    For aAccScad in (select * from accertamento_scadenzario where
                          cd_cds = aAss.cd_cds
 					 and esercizio = aAss.esercizio
 					 and esercizio_originale = aAss.esercizio_ori_accertamento
 					 and pg_accertamento = aAss.pg_accertamento
 					 and im_associato_doc_amm != 0
 					for update nowait) loop
     IBMERR001.RAISE_ERR_GENERICO('Esistono scadenze collegate a documenti amministrativi sull''accertamento di partita di giro n.'||aAss.pg_accertamento||' cds:'||aCdCds||' es.'||aEs||' esOri.:'||aAss.esercizio_ori_accertamento);
    end loop;
	-- verifico se la controparte ? riportata
	select riportato into isRiportato
	from accertamento
	where cd_cds = aAss.cd_cds
	  and esercizio = aAss.esercizio
	  and esercizio_originale = aAss.esercizio_ori_accertamento
	  and pg_accertamento = aAss.pg_accertamento;
	if isRiportato = 'Y' then
	   ibmerr001.RAISE_ERR_GENERICO('L''accertamento su partita di giro n.'||aAss.pg_accertamento||' cds:'||aCdCds||' es.'||aEs||' esOri.:'||aAss.esercizio_ori_accertamento||' ? gi? riportato');
	end if;
    annullaAccertamento(
     aAss.cd_cds,
     aAss.esercizio,
     aAss.esercizio_ori_accertamento,
     aAss.pg_accertamento,
     aUser
    );
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;

  if aObb.CD_TIPO_DOCUMENTO_CONT <> 'IMP_RES' then
	  update obbligazione set
	       stato_obbligazione=STATO_STORNATO,
		   dt_cancellazione = TRUNC(CNRCTB008.GETTIMESTAMPCONTABILE(aEs,aTSNow)),
	       im_obbligazione = 0,
		   utuv=aUser,
		   duva=aTSNow,
		   pg_ver_rec = pg_ver_rec+1
	  where
	       cd_cds = aObb.cd_cds
	   and esercizio = aObb.esercizio
	   and esercizio_originale = aObb.esercizio_originale
	   and pg_obbligazione = aObb.pg_obbligazione;
  else
	  update obbligazione set
	       im_obbligazione = 0,
		   utuv=aUser,
		   duva=aTSNow,
		   pg_ver_rec = pg_ver_rec+1
	  where
	       cd_cds = aObb.cd_cds
	   and esercizio = aObb.esercizio
	   and esercizio_originale = aObb.esercizio_originale
	   and pg_obbligazione = aObb.pg_obbligazione;
  end if;

  For aObbScad in (
   select * from obbligazione_scadenzario where
        cd_cds = aObb.cd_cds
    and esercizio = aObb.esercizio
    and esercizio_originale = aObb.esercizio_originale
    and pg_obbligazione = aObb.pg_obbligazione
   for update nowait) loop

   update obbligazione_scadenzario set
       im_scadenza=0,
       im_associato_doc_amm=0,
       im_associato_doc_contabile=0,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec = pg_ver_rec+1
   where
        cd_cds = aObbScad.cd_cds
    and esercizio = aObbScad.esercizio
    and esercizio_originale = aObbScad.esercizio_originale
    and pg_obbligazione = aObbScad.pg_obbligazione
	and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;

   For aObbScadVoce in (
   select * from obbligazione_scad_voce where
         cd_cds = aObbScad.cd_cds
     and esercizio = aObb.esercizio
     and esercizio_originale = aObbScad.esercizio_originale
     and pg_obbligazione = aObbScad.pg_obbligazione
     and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario
    for update nowait
   ) Loop

    -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
    aggiornaSaldoDettScad(aObb,aObbScadVoce,0-aObbScadVoce.im_voce, false,aUser, aTSNow);

/* inizio 11.01.2006 stani */
If CNRCTB048.getcdsribaltato (aObb.ESERCIZIO, aObb.cd_cds) = 'Y' Then

  -- AGGIORNO SOLO L'ANNO DOPO

  aDeltaSaldo.ESERCIZIO := aObb.ESERCIZIO+1;
  aDeltaSaldo.ESERCIZIO_RES := aObb.ESERCIZIO_ORIGINALE;
  aDeltaSaldo.CD_CENTRO_RESPONSABILITA := aObbScadVoce.CD_CENTRO_RESPONSABILITA;
  aDeltaSaldo.CD_LINEA_ATTIVITA := aObbScadVoce.CD_LINEA_ATTIVITA;
  aDeltaSaldo.TI_APPARTENENZA := aObbScadVoce.TI_APPARTENENZA;
  aDeltaSaldo.TI_GESTIONE := aObbScadVoce.TI_GESTIONE;
  aDeltaSaldo.CD_VOCE := aObbScadVoce.CD_VOCE;

  CNRCTB054.RESET_IMPORTI_SALDI (aDeltaSaldo);

-- indipendentemente dal tipo documento dell'anno prima dall'anno dopo
-- devo aumentare sempre lo stanziamento residuo
  aDeltaSaldo.IM_STANZ_RES_IMPROPRIO := aObbScadVoce.IM_VOCE;
  aDeltaSaldo.UTUV := aObbScadVoce.utcr;

  CNRCTB054.crea_aggiorna_saldi(aDeltaSaldo, '035.annullaobbligazione', 'N');

End If;

/* fine 11.01.2006 stani */


    update obbligazione_scad_voce set
       im_voce=0,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec = pg_ver_rec+1
    where
         cd_cds = aObbScadVoce.cd_cds
     and esercizio = aObbScadVoce.esercizio
     and esercizio_originale = aObbScadVoce.esercizio_originale
     and pg_obbligazione = aObbScadVoce.pg_obbligazione
	 and pg_obbligazione_scadenzario = aObbScadVoce.pg_obbligazione_scadenzario
	 and ti_appartenenza = aObbScadVoce.ti_appartenenza
	 and ti_gestione = aObbScadVoce.ti_gestione
	 and cd_voce = aObbScadVoce.cd_voce
	 and cd_centro_responsabilita = aObbScadVoce.cd_centro_responsabilita
	 and cd_linea_attivita = aObbScadVoce.cd_linea_attivita;
   end loop;
  end loop;
 end;

 procedure annullaScadObbligazione(
  aCdCds varchar2,
  aEs number,
  aEsOri number,
  aPgObbligazione number,
  aPgObbScad number,
  aUser varchar2
 ) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aTSNow date;
  aAcc accertamento%rowtype;
  aAss ass_obb_acr_pgiro%rowtype;
  isRiportato char(1);
  isStornata char(1);
  aSaldoCdrLinea voce_f_saldi_cdr_linea%Rowtype;
 begin
  aTSNow:=sysdate;
  begin
   select * into aObb from obbligazione where
             cd_cds = aCdCds
		 and esercizio = aEs
		 and esercizio_originale = aEsOri
		 and pg_obbligazione = aPgObbligazione
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Documento di spesa non trovato n.'||aPgObbligazione||' cds:'||aCdCds||' es.:'||aEs||' esOri.:'||aEsOri);
  end;
  begin
   select * into aObbScad from obbligazione_scadenzario where
             cd_cds = aCdCds
		 and esercizio = aEs
		 and esercizio_originale = aEsOri
		 and pg_obbligazione = aPgObbligazione
		 and pg_obbligazione_scadenzario = aPgObbScad
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scadenza n.'||aPgObbScad||' di documento di spesa n.'||aPgObbligazione||' cds:'||aCdCds||' es.'||aEs||' non trovata');
  end;
  -- Verifico che l'obbligazione non sia associata a documenti amministrativi
  if aObbScad.im_associato_doc_amm <> 0 then
   ibmerr001.RAISE_ERR_GENERICO('Esistono scadenze associate a documenti amministrativi sul documento di spesa  n.'||aObb.pg_obbligazione||' cds:'||aObb.cd_cds||' es.:'||aObb.esercizio||' esOri.:'||aObb.esercizio_originale);
  end if;

  -- Se obbligazione su partita di giro devo annullare anche la contropartita
  -- Questo solo se sulla contropartita non ci sono documenti
  -- e se non ? gi? riportata
  if aObb.fl_pgiro='Y' then
   begin
    select * into aAss from ass_obb_acr_pgiro where
                cd_cds = aObb.cd_cds
		    and esercizio = aObb.esercizio
	   	    and esercizio_ori_obbligazione = aObb.esercizio_originale
		    and pg_obbligazione = aObb.pg_obbligazione
			and ti_origine = CNRCTB001.GESTIONE_SPESE;
    for aAccScad in (select * from accertamento_scadenzario where
                          cd_cds = aAss.cd_cds
 					 and esercizio = aAss.esercizio
 					 and esercizio_originale = aAss.esercizio_ori_accertamento
 					 and pg_accertamento = aAss.pg_accertamento
 					 and im_associato_doc_amm != 0
 					for update nowait) loop
     IBMERR001.RAISE_ERR_GENERICO('Esistono scadenze collegate a documenti amministrativi sull''accertamento di partita di giro n.'||aAss.pg_accertamento||' cds:'||aCdCds||' es.'||aEs||' esOri.:'||aAss.esercizio_ori_accertamento);
    end loop;
	-- verifico se la controparte ? riportata
	select riportato into isRiportato
	from accertamento
	where cd_cds = aAss.cd_cds
	  and esercizio = aAss.esercizio
	  and esercizio_originale = aAss.esercizio_ori_accertamento
	  and pg_accertamento = aAss.pg_accertamento;
	if isRiportato = 'Y' then
	   ibmerr001.RAISE_ERR_GENERICO('L''accertamento su partita di giro n.'||aAss.pg_accertamento||' cds:'||aCdCds||' es.'||aEs||' esOri.:'||aAss.esercizio_ori_accertamento||' ? gi? riportato');
	end if;
    annullaAccertamento(
     aAss.cd_cds,
     aAss.esercizio,
     aAss.esercizio_ori_accertamento,
     aAss.pg_accertamento,
     aUser
    );
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;
  isStornata:='N';
  if aObb.im_obbligazione - aObbScad.im_scadenza = 0 then
   isStornata:='Y';
  end if;

  if aObb.CD_TIPO_DOCUMENTO_CONT <> 'IMP_RES' then
	  update obbligazione set
	       stato_obbligazione=decode(isStornata,'Y',STATO_STORNATO,stato_obbligazione),
		   dt_cancellazione = decode(isStornata,'Y',TRUNC(CNRCTB008.GETTIMESTAMPCONTABILE(aEs,aTSNow)),dt_cancellazione),
	       im_obbligazione = im_obbligazione-aObbScad.im_scadenza,
		   utuv=aUser,
		   duva=aTSNow,
		   pg_ver_rec = pg_ver_rec+1
	  where
	       cd_cds = aObb.cd_cds
	   and esercizio = aObb.esercizio
	   and esercizio_originale = aObb.esercizio_originale
	   and pg_obbligazione = aObb.pg_obbligazione;
  else
	  update obbligazione set
	       im_obbligazione = im_obbligazione - aObbScad.im_scadenza,
		   utuv=aUser,
		   duva=aTSNow,
		   pg_ver_rec = pg_ver_rec+1
	  where
	       cd_cds = aObb.cd_cds
	   and esercizio = aObb.esercizio
	   and esercizio_originale = aObb.esercizio_originale
	   and pg_obbligazione = aObb.pg_obbligazione;
  end if;

  update obbligazione_scadenzario set
       im_scadenza=0,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec = pg_ver_rec+1
  where
        cd_cds = aObbScad.cd_cds
    and esercizio = aObbScad.esercizio
    and esercizio_originale = aObbScad.esercizio_originale
    and pg_obbligazione = aObbScad.pg_obbligazione
	and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;
  for aObbScadVoce in (
   select * from obbligazione_scad_voce where
         cd_cds = aObbScad.cd_cds
     and esercizio = aObb.esercizio
     and esercizio_originale = aObbScad.esercizio_originale
     and pg_obbligazione = aObbScad.pg_obbligazione
     and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario
    for update nowait
  ) Loop
   -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
   aggiornaSaldoDettScad(aObb,aObbScadVoce,0-aObbScadVoce.im_voce, false,aUser, aTSNow);
   update obbligazione_scad_voce set
       im_voce=0,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec = pg_ver_rec+1
   where
         cd_cds = aObbScadVoce.cd_cds
     and esercizio = aObbScadVoce.esercizio
     and esercizio_originale = aObbScadVoce.esercizio_originale
     and pg_obbligazione = aObbScadVoce.pg_obbligazione
	 and pg_obbligazione_scadenzario = aObbScadVoce.pg_obbligazione_scadenzario
	 and ti_appartenenza = aObbScadVoce.ti_appartenenza
	 and ti_gestione = aObbScadVoce.ti_gestione
	 and cd_voce = aObbScadVoce.cd_voce
	 and cd_centro_responsabilita = aObbScadVoce.cd_centro_responsabilita
	 and cd_linea_attivita = aObbScadVoce.cd_linea_attivita;
  end loop;
 end;



Procedure aggiornaSaldoDettScad(aAcc accertamento%rowtype,
                                aDelta number,
                                isControlloBloccante boolean,
                                aUser varchar2,
                                aTSNow date) is
   aSaldo          voce_f_saldi_cmp%rowtype;

  recParametriCNR PARAMETRI_CNR%Rowtype;
Begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aAcc.esercizio);

  if recParametriCNR.fl_nuovo_pdg is not null and recParametriCNR.fl_nuovo_pdg='Y' Then
    return;
  End If;

  if aAcc.cd_tipo_documento_cont <> CNRCTB018.TI_DOC_ACC_RES then
	  select * into aSaldo
	  from voce_f_saldi_cmp
	  Where  esercizio = aAcc.esercizio
	   and cd_cds = aAcc.cd_cds
	   and ti_appartenenza = aAcc.ti_appartenenza
	   and ti_gestione = aAcc.ti_gestione
	   and cd_voce = aAcc.cd_voce
	   and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA
	  for update nowait;
  else
	  select * into aSaldo
	  from voce_f_saldi_cmp
	  Where esercizio = aAcc.esercizio
	   and cd_cds = aAcc.cd_cds
	   and ti_appartenenza = aAcc.ti_appartenenza
	   and ti_gestione = aAcc.ti_gestione
	   and cd_voce = aAcc.cd_voce
	   and ti_competenza_residuo = CNRCTB054.TI_RESIDUI
	  for update nowait;
  end if;
  CNRCTB054.aggiornaSaldi(aSaldo,'P', aDelta, aUser, aTSNow);
End;

Procedure creaScadAccertamento(
  aAcc IN OUT accertamento%rowtype,
  aScadenza1 in out accertamento_scadenzario%rowtype,
  posizione number,
  aDettScadenza1 in out scadVoceListE) Is
 aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;

 Begin
    If aDettScadenza1.count > 0 then

       aScadenza1.esercizio_originale           := aAcc.esercizio_originale;
       aScadenza1.pg_accertamento               := aAcc.pg_accertamento;
       aScadenza1.pg_accertamento_scadenzario   := posizione;
       aScadenza1.dt_scadenza_incasso           := trunc(aScadenza1.dt_scadenza_incasso);
       aScadenza1.dt_scadenza_emissione_fattura := trunc(aScadenza1.dt_scadenza_emissione_fattura);

       CNRCTB035.INS_accertamento_SCADENZARIO(aScadenza1);

       for i in 1 .. aDettScadenza1.count loop
         aDettScadenza1(i).esercizio_originale         := aAcc.esercizio_originale;
         aDettScadenza1(i).pg_accertamento             := aAcc.pg_accertamento;
         aDettScadenza1(i).pg_accertamento_scadenzario := aScadenza1.pg_accertamento_scadenzario;

Dbms_Output.put_line ('INS_accertamento_SCAD_VOCE '||aDettScadenza1(i).esercizio||' '||
aDettScadenza1(i).esercizio_originale||' '||aDettScadenza1(i).cd_cds||' '||aDettScadenza1(i).pg_accertamento);

	 CNRCTB035.INS_accertamento_SCAD_VOCE(aDettScadenza1(i));

         -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
         aSaldoCdrLineaAcc.ESERCIZIO := aDettScadenza1(i).ESERCIZIO;
         aSaldoCdrLineaAcc.ESERCIZIO_RES :=aAcc.ESERCIZIO_ORIGINALE;
         aSaldoCdrLineaAcc.CD_CENTRO_RESPONSABILITA := aDettScadenza1(i).CD_CENTRO_RESPONSABILITA;
         aSaldoCdrLineaAcc.CD_LINEA_ATTIVITA := aDettScadenza1(i).CD_LINEA_ATTIVITA;
         aSaldoCdrLineaAcc.TI_APPARTENENZA := aAcc.TI_APPARTENENZA;
         aSaldoCdrLineaAcc.TI_GESTIONE := aAcc.TI_GESTIONE;
         aSaldoCdrLineaAcc.CD_VOCE := aAcc.CD_VOCE;

         CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLineaAcc);

         If aAcc.esercizio = aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_ACC_COMP := aDettScadenza1(i).im_voce;
         Elsif aAcc.esercizio > aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_RES_PRO := aDettScadenza1(i).im_voce;
         End If;
         aSaldoCdrLineaAcc.UTUV := aAcc.utcr;

         CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLineaAcc, '035.creaScadAccertamento', 'N');
       end loop;
    End if;
End;

 procedure creaScadObbligazione(
  aObb           IN OUT obbligazione%rowtype,
  aScadenza1     in out obbligazione_scadenzario%rowtype,
  posizione      number,
  aDettScadenza1 in out scadVoceListS,
  isControlloBloccante boolean
 ) Is
 aSaldoCdrLinea voce_f_saldi_cdr_linea%Rowtype;

Begin
Dbms_Output.PUT_LINE ('creaScadObbligazione');

     If aDettScadenza1.count > 0 Then

      aScadenza1.esercizio_originale         := aObb.esercizio_originale;
      aScadenza1.pg_obbligazione             := aObb.pg_obbligazione;
      aScadenza1.pg_obbligazione_scadenzario := posizione;
      aScadenza1.dt_scadenza                 := trunc(aScadenza1.dt_scadenza);

      CNRCTB035.INS_OBBLIGAZIONE_SCADENZARIO(aScadenza1);

	 For i in 1 .. aDettScadenza1.count loop
             aDettScadenza1(i).esercizio_originale         := aObb.esercizio_originale;
             aDettScadenza1(i).pg_obbligazione             := aObb.pg_obbligazione;
             aDettScadenza1(i).pg_obbligazione_scadenzario := aScadenza1.pg_obbligazione_scadenzario;

 	     CNRCTB035.INS_OBBLIGAZIONE_SCAD_VOCE(aDettScadenza1(i));

	     if aObb.esercizio = aObb.esercizio_competenza then
	     -- obbligazione non pluriennale => aggiorno i saldi
       	       aggiornaSaldoDettScad(aObb,aDettScadenza1(i),aDettScadenza1(i).im_voce, isControlloBloccante,aDettScadenza1(i).utcr, aDettScadenza1(i).dacr);
       	     -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
	     end if;
	 End Loop;
     End If;
End;


Procedure aggiornaSaldoDettScad(aObb obbligazione%rowtype, aObbScadVoce obbligazione_scad_voce%rowtype,aDelta number, isControlloBloccante boolean,aUser varchar2, aTSNow date) is
  aSaldo voce_f_saldi_cmp%rowtype;
  aSaldoCdrLinea voce_f_saldi_cdr_linea%Rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  begin
    if aObb.cd_tipo_documento_cont <> CNRCTB018.TI_DOC_IMP_RES then
  	  select * into aSaldo from voce_f_saldi_cmp where
  	       esercizio = aObbScadVoce.esercizio
  	   and cd_cds = aObbScadVoce.cd_cds
  	   and ti_appartenenza = aObbScadVoce.ti_appartenenza
  	   and ti_gestione = aObbScadVoce.ti_gestione
  	   and cd_voce = aObbScadVoce.cd_voce
  	   and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA
  	  for update nowait;
    else
  	  select * into aSaldo from voce_f_saldi_cmp where
  	       esercizio = aObbScadVoce.esercizio
  	   and cd_cds = aObbScadVoce.cd_cds
  	   and ti_appartenenza = aObbScadVoce.ti_appartenenza
  	   and ti_gestione = aObbScadVoce.ti_gestione
  	   and cd_voce = aObbScadVoce.cd_voce
  	   and ti_competenza_residuo = CNRCTB054.TI_RESIDUI
  	  for update nowait;
    end if;

    if isControlloBloccante then
     if aSaldo.im_stanz_iniziale_a1 + aSaldo.variazioni_piu - aSaldo.variazioni_meno - aSaldo.im_obblig_imp_acr - aDelta < 0 then
      IBMERR001.RAISE_ERR_GENERICO('Disponibilit? per emissione '||cnrutil.getLabelObbligazioniMin()||' esaurita');
     end if;
    end if;
  Exception
    When no_data_found Then
      recParametriCNR := CNRUTL001.getRecParametriCnr(aObbScadVoce.esercizio);
      if recParametriCNR.fl_nuovo_pdg='N' Then
        IBMERR001.RAISE_ERR_GENERICO('Tabella Saldi (voce_f_saldi_cmp) vuota. Aggiornamento non possibile.');
      End If;
  End;

  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  aSaldoCdrLinea.ESERCIZIO := aObbScadVoce.ESERCIZIO;
  aSaldoCdrLinea.ESERCIZIO_RES := aObb.ESERCIZIO_ORIGINALE;
  aSaldoCdrLinea.CD_CENTRO_RESPONSABILITA := aObbScadVoce.CD_CENTRO_RESPONSABILITA;
  aSaldoCdrLinea.CD_LINEA_ATTIVITA := aObbScadVoce.CD_LINEA_ATTIVITA;
  aSaldoCdrLinea.TI_APPARTENENZA := aObbScadVoce.TI_APPARTENENZA;
  aSaldoCdrLinea.TI_GESTIONE := aObbScadVoce.TI_GESTIONE;
  aSaldoCdrLinea.CD_VOCE := aObbScadVoce.CD_VOCE;

  CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLinea);

  If aObb.esercizio = aObb.esercizio_originale Then
      aSaldocdrlinea.IM_OBBL_ACC_COMP := aDelta;
  Elsif aObb.esercizio > aObb.esercizio_originale Then
     If aObb.CD_TIPO_DOCUMENTO_CONT In (cnrctb018.TI_DOC_IMP_RES, cnrctb018.TI_DOC_OBB_RES_PRO, cnrctb018.TI_DOC_OBB_PGIRO_RES) Then
        aSaldocdrlinea.IM_OBBL_RES_PRO := aDelta;
     Elsif aObb.CD_TIPO_DOCUMENTO_CONT = cnrctb018.TI_DOC_OBB_RES_IMPRO Then
        aSaldocdrlinea.IM_OBBL_RES_IMP := aDelta;
     End If;
  End If;

  aSaldoCdrLinea.UTUV := aObbScadVoce.utcr;

  CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLinea, '035.aggiornaSaldoDettScad', 'N');

  CNRCTB054.aggiornaSaldi(aSaldo, 'P', aDelta, aUser, aTSNow);
 end;

 procedure ins_OBBLIGAZIONE (aDest OBBLIGAZIONE%rowtype) is
  begin
   insert into OBBLIGAZIONE (
     ESERCIZIO
    ,CD_CDS
    ,ESERCIZIO_ORIGINALE
    ,PG_OBBLIGAZIONE
    ,CD_TIPO_DOCUMENTO_CONT
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,CD_TIPO_OBBLIGAZIONE
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,DT_REGISTRAZIONE
    ,DS_OBBLIGAZIONE
    ,NOTE_OBBLIGAZIONE
    ,CD_TERZO
    ,IM_OBBLIGAZIONE
    ,IM_COSTI_ANTICIPATI
    ,ESERCIZIO_COMPETENZA
    ,STATO_OBBLIGAZIONE
    ,DT_CANCELLAZIONE
    ,CD_RIFERIMENTO_CONTRATTO
    ,DT_SCADENZA_CONTRATTO
    ,FL_CALCOLO_AUTOMATICO
    ,CD_FONDO_RICERCA
    ,FL_SPESE_COSTI_ALTRUI
    ,FL_PGIRO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,RIPORTATO
    ,CD_CDS_ORI_RIPORTO
    ,ESERCIZIO_ORI_RIPORTO
    ,ESERCIZIO_ORI_ORI_RIPORTO
    ,PG_OBBLIGAZIONE_ORI_RIPORTO
    ,ESERCIZIO_CONTRATTO
    ,STATO_CONTRATTO
    ,PG_CONTRATTO
    ,ESERCIZIO_REP
    ,PG_REPERTORIO
    ,MOTIVAZIONE
    ,FL_NETTO_SOSPESO
    ,FL_GARA_IN_CORSO
    ,DS_GARA_IN_CORSO
    ,STATO_COGE_DOCAMM
    ,STATO_COGE_DOCCONT
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.CD_TIPO_DOCUMENTO_CONT
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.CD_TIPO_OBBLIGAZIONE
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DS_OBBLIGAZIONE
    ,aDest.NOTE_OBBLIGAZIONE
    ,aDest.CD_TERZO
    ,aDest.IM_OBBLIGAZIONE
    ,aDest.IM_COSTI_ANTICIPATI
    ,aDest.ESERCIZIO_COMPETENZA
    ,aDest.STATO_OBBLIGAZIONE
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_RIFERIMENTO_CONTRATTO
    ,aDest.DT_SCADENZA_CONTRATTO
    ,aDest.FL_CALCOLO_AUTOMATICO
    ,aDest.CD_FONDO_RICERCA
    ,aDest.FL_SPESE_COSTI_ALTRUI
    ,aDest.FL_PGIRO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.RIPORTATO
    ,aDest.CD_CDS_ORI_RIPORTO
    ,aDest.ESERCIZIO_ORI_RIPORTO
    ,aDest.ESERCIZIO_ORI_ORI_RIPORTO
    ,aDest.PG_OBBLIGAZIONE_ORI_RIPORTO
    ,aDest.ESERCIZIO_CONTRATTO
    ,aDest.STATO_CONTRATTO
    ,aDest.PG_CONTRATTO
    ,aDest.ESERCIZIO_REP
    ,aDest.PG_REPERTORIO
    ,aDest.MOTIVAZIONE
    ,nvl(aDest.FL_NETTO_SOSPESO,'N')
    ,nvl(aDest.FL_GARA_IN_CORSO,'N')
    ,aDest.DS_GARA_IN_CORSO
    ,aDest.stato_coge_docamm
    ,aDest.stato_coge_doccont
    );
 end;
 procedure ins_OBBLIGAZIONE_SCADENZARIO (aDest OBBLIGAZIONE_SCADENZARIO%rowtype) is
  begin
   insert into OBBLIGAZIONE_SCADENZARIO (
     PG_OBBL_SCAD_ORI_RIPORTO
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE_SCADENZARIO
    ,DT_SCADENZA
    ,DS_SCADENZA
    ,IM_SCADENZA
    ,IM_ASSOCIATO_DOC_AMM
    ,IM_ASSOCIATO_DOC_CONTABILE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.PG_OBBL_SCAD_ORI_RIPORTO
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.DT_SCADENZA
    ,aDest.DS_SCADENZA
    ,aDest.IM_SCADENZA
    ,aDest.IM_ASSOCIATO_DOC_AMM
    ,aDest.IM_ASSOCIATO_DOC_CONTABILE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_OBBLIGAZIONE_SCAD_VOCE (aDest OBBLIGAZIONE_SCAD_VOCE%rowtype) is
  begin
   insert into OBBLIGAZIONE_SCAD_VOCE (
     ESERCIZIO
    ,CD_CDS
    ,ESERCIZIO_ORIGINALE
    ,PG_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE_SCADENZARIO
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_VOCE
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,IM_VOCE
    ,CD_FONDO_RICERCA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_VOCE
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.IM_VOCE
    ,aDest.CD_FONDO_RICERCA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_ACCERTAMENTO (aDest ACCERTAMENTO%rowtype) is
  begin
   insert into ACCERTAMENTO (
     ESERCIZIO_COMPETENZA
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_ACCERTAMENTO
    ,CD_TIPO_DOCUMENTO_CONT
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_VOCE
    ,DT_REGISTRAZIONE
    ,DS_ACCERTAMENTO
    ,NOTE_ACCERTAMENTO
    ,CD_TERZO
    ,IM_ACCERTAMENTO
    ,DT_CANCELLAZIONE
    ,CD_RIFERIMENTO_CONTRATTO
    ,DT_SCADENZA_CONTRATTO
    ,CD_FONDO_RICERCA
    ,FL_PGIRO
    ,RIPORTATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS_ORI_RIPORTO
    ,ESERCIZIO_ORI_RIPORTO
    ,ESERCIZIO_ORI_ORI_RIPORTO
    ,PG_ACCERTAMENTO_ORI_RIPORTO
    ,PG_ACCERTAMENTO_ORIGINE
    ,ESERCIZIO_CONTRATTO
    ,STATO_CONTRATTO
    ,PG_CONTRATTO
    ,FL_CALCOLO_AUTOMATICO
    ,FL_NETTO_SOSPESO
    ,STATO_COGE_DOCAMM
    ,STATO_COGE_DOCCONT
   ) values (
     aDest.ESERCIZIO_COMPETENZA
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_ACCERTAMENTO
    ,aDest.CD_TIPO_DOCUMENTO_CONT
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_VOCE
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DS_ACCERTAMENTO
    ,aDest.NOTE_ACCERTAMENTO
    ,aDest.CD_TERZO
    ,aDest.IM_ACCERTAMENTO
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_RIFERIMENTO_CONTRATTO
    ,aDest.DT_SCADENZA_CONTRATTO
    ,aDest.CD_FONDO_RICERCA
    ,aDest.FL_PGIRO
    ,aDest.RIPORTATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS_ORI_RIPORTO
    ,aDest.ESERCIZIO_ORI_RIPORTO
    ,aDest.ESERCIZIO_ORI_ORI_RIPORTO
    ,aDest.PG_ACCERTAMENTO_ORI_RIPORTO
    ,aDest.PG_ACCERTAMENTO_ORIGINE
    ,aDest.ESERCIZIO_CONTRATTO
    ,aDest.STATO_CONTRATTO
    ,aDest.PG_CONTRATTO
    ,Nvl(aDest.FL_CALCOLO_AUTOMATICO,'N')
    ,nvl(aDest.FL_NETTO_SOSPESO,'N')
    ,aDest.stato_coge_docamm
    ,aDest.stato_coge_doccont
    );
 end;
 procedure ins_ACCERTAMENTO_SCADENZARIO (aDest ACCERTAMENTO_SCADENZARIO%rowtype) is
  begin
   insert into ACCERTAMENTO_SCADENZARIO (
     PG_ACC_SCAD_ORI_RIPORTO
    ,IM_SCADENZA
    ,IM_ASSOCIATO_DOC_AMM
    ,IM_ASSOCIATO_DOC_CONTABILE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,DT_SCADENZA_EMISSIONE_FATTURA
    ,DT_SCADENZA_INCASSO
    ,DS_SCADENZA
     ) values (
     aDest.PG_ACC_SCAD_ORI_RIPORTO
    ,aDest.IM_SCADENZA
    ,aDest.IM_ASSOCIATO_DOC_AMM
    ,aDest.IM_ASSOCIATO_DOC_CONTABILE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.DT_SCADENZA_EMISSIONE_FATTURA
    ,aDest.DT_SCADENZA_INCASSO
    ,aDest.DS_SCADENZA
    );
 end;
 procedure ins_ACCERTAMENTO_SCAD_VOCE (aDest ACCERTAMENTO_SCAD_VOCE%rowtype) is
  begin
   insert into ACCERTAMENTO_SCAD_VOCE (
     CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,IM_VOCE
    ,CD_FONDO_RICERCA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.IM_VOCE
    ,aDest.CD_FONDO_RICERCA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_ASS_OBB_ACR_PGIRO (aDest ASS_OBB_ACR_PGIRO%rowtype) is
 begin
   insert into ASS_OBB_ACR_PGIRO (
     CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORI_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE
    ,ESERCIZIO_ORI_ACCERTAMENTO
    ,PG_ACCERTAMENTO
    ,TI_ORIGINE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORI_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.ESERCIZIO_ORI_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO
    ,aDest.TI_ORIGINE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure getPgiroCdsBase(
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%rowtype,
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype,
    fromEntrataSpesa char
 ) is

aAssPGiro ass_obb_acr_pgiro%rowtype;

Begin
--pipe.send_message('b1');
  Begin

   Select * into aAcc
   From     accertamento
   Where    cd_cds = aAcc.cd_cds And
            esercizio = aAcc.esercizio And
            esercizio_originale = aAcc.esercizio_originale And
            pg_accertamento = aAcc.pg_accertamento And
            fl_pgiro = 'Y'
   For update nowait;

   Select * into aAccScad
   From     accertamento_scadenzario
   Where    cd_cds = aAcc.cd_cds And
            esercizio = aAcc.esercizio And
            esercizio_originale = aAcc.esercizio_originale And
            pg_accertamento = aAcc.pg_accertamento
   For update nowait;

   Select * into aAccScadVoce
   From     accertamento_scad_voce
   Where    cd_cds = aAcc.cd_cds And
            esercizio = aAcc.esercizio And
            esercizio_originale = aAcc.esercizio_originale And
            pg_accertamento = aAcc.pg_accertamento
   For Update nowait;

  Exception
   when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non trovato '||CNRCTB035.getDesc(aAcc));
   when TOO_MANY_ROWS Then
--pipe.send_message('b2');
      -- se sto effettuando il ribaltamento di liquid_gruppo_centro, potrebbero esserci pi? scadenze,
      -- quindi devo superare il controllo anche perch? viene generata una pgiro tronca in parte entrata
      -- altrimenti sollevo l'errore
      -- anche se l'accertamento ? legato a rev provv devo superare il controllo perch? potrebbero esserci + scadenze
      Declare
         conta NUMBER:=0;
         conta_rev_provv NUMBER:=0;
         conta_gruppo NUMBER:=0;
      Begin
         Select Count(1)
         Into conta
         From liquid_gruppo_centro
         Where cd_cds_obb_accentr = aObb.cd_cds
           And esercizio_obb_accentr = aObb.esercizio
           And pg_obb_accentr = aObb.pg_obbligazione
           And esercizio_ori_obb_accentr = aObb.esercizio_originale;

         -- Nel caso in cui Obb non ? valorizzato lo valorizzo prima di fare i controlli
         Begin
              Select * into aAssPGiro
              From   ASS_OBB_ACR_PGIRO
              Where  cd_cds = aAcc.cd_cds And
                     esercizio = aAcc.esercizio And
                     esercizio_ori_accertamento = aAcc.esercizio_originale And
                     pg_accertamento = aAcc.pg_accertamento And
                     ti_origine = fromEntrataSpesa
              For Update nowait;
         Exception
           When No_Data_Found Then
             IBMERR001.RAISE_ERR_GENERICO('Associazione Accertamento '||CNRCTB035.getDesc(aAcc)||' su partita di giro con origine '||fromEntrataSpesa||' non trovata.');
         End;

         Select Count(1)
         Into conta_gruppo
         From liquid_gruppo_centro
         Where cd_cds_obb_accentr = aAssPGiro.cd_cds
           And esercizio_obb_accentr = aAssPGiro.esercizio
           And pg_obb_accentr = aAssPGiro.pg_obbligazione
           And esercizio_ori_obb_accentr = aAssPGiro.esercizio_ori_obbligazione;

         Select Count(1)
         Into conta_rev_provv
         From accertamento_scadenzario a
         Where a.cd_cds = aAssPGiro.cd_cds
	         And a.esercizio = aAssPGiro.esercizio
           And a.esercizio_originale = aAssPGiro.esercizio_ori_accertamento
	         And a.pg_accertamento = aAssPGiro.pg_accertamento
	         And (a.cd_cds, a.esercizio, a.esercizio_originale, a.pg_accertamento, a.pg_accertamento_scadenzario) In
	             (Select rr.cd_cds, rr.esercizio, rr.esercizio_ori_accertamento, rr.pg_accertamento, rr.pg_accertamento_scadenzario
	              From reversale_riga rr, reversale r
	              Where r.esercizio = rr.esercizio
	                And r.cd_cds = rr.cd_cds
	                And r.pg_reversale = rr.pg_reversale
	                And rr.cd_cds = a.cd_cds
	                And rr.esercizio = a.esercizio
	                And rr.esercizio_ori_accertamento = a.esercizio_originale
	                And rr.pg_accertamento = a.pg_accertamento
	                And rr.pg_accertamento_scadenzario = a.pg_accertamento_scadenzario
	                And r.cd_tipo_documento_cont=CNRCTB018.TI_DOC_REV_PROVV);

      	 If conta > 0 or conta_rev_provv > 0 or conta_gruppo > 0 Then
      	      select * into aAccScad from accertamento_scadenzario
      	      Where cd_cds = aAcc.cd_cds
	              and esercizio = aAcc.esercizio
      	        And esercizio_originale = aAcc.esercizio_originale
	              And pg_accertamento = aAcc.pg_accertamento
	              And Rownum < 2
		          for update nowait;
   	          select * into aAccScadVoce from accertamento_scad_voce
   	          Where cd_cds = aAcc.cd_cds
	              And esercizio = aAcc.esercizio
      	        and esercizio_originale = aAcc.esercizio_originale
	              And pg_accertamento = aAcc.pg_accertamento
	              And Rownum < 2
   		        for update nowait;
      	 Else
      	    IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non pu? avere pi? di una scadenza e di un dettaglio '||CNRCTB035.getDesc(aAcc));
	       End If;
      Exception
         when NO_DATA_FOUND then
             IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non trovato '||CNRCTB035.getDesc(aAcc));
      End;
  End;
--pipe.send_message('b3');
--  if aAcc.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
--   IBMERR001.RAISE_ERR_GENERICO('Funzione non supportata per accertamenti su partita di giro sull''Ente');
--  end if;

  Begin

   Begin
        Select * into aAssPGiro
        From   ASS_OBB_ACR_PGIRO
        Where  cd_cds = aAccScadVoce.cd_cds And
               esercizio = aAccScadVoce.esercizio And
               esercizio_ori_accertamento = aAccScadVoce.esercizio_originale And
               pg_accertamento = aAccScadVoce.pg_accertamento And
               ti_origine = fromEntrataSpesa
        For Update nowait;
   Exception
     When No_Data_Found Then
       IBMERR001.RAISE_ERR_GENERICO('Associazione Accertamento '||CNRCTB035.getDesc(aAcc)||' su partita di giro con origine '||fromEntrataSpesa||' non trovata.');
   End;

   Select *
   Into   aObb
   From   obbligazione
   Where  cd_cds = aAssPGiro.cd_cds And
          esercizio = aAssPGiro.esercizio And
          esercizio_originale = aAssPGiro.esercizio_ori_obbligazione And
          pg_obbligazione = aAssPGiro.pg_obbligazione And
          fl_pgiro = 'Y'
   For Update nowait;

   Select *
   Into   aObbScad
   From   obbligazione_scadenzario
   Where  cd_cds = aAssPGiro.cd_cds And
          esercizio = aAssPGiro.esercizio And
          esercizio_originale = aAssPGiro.esercizio_ori_obbligazione And
          pg_obbligazione = aAssPGiro.pg_obbligazione
   For Update nowait;

   Select *
   Into   aObbScadVoce
   From   obbligazione_scad_voce
   Where  cd_cds = aAssPGiro.cd_cds And
          esercizio = aAssPGiro.esercizio And
          esercizio_originale = aAssPGiro.esercizio_ori_obbligazione And
          pg_obbligazione = aAssPGiro.pg_obbligazione
   For Update nowait;

  Exception
   When NO_DATA_FOUND Then
      IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' su partita di giro non trovata (1) '||CNRCTB035.getDesc(aObb));
   When TOO_MANY_ROWS Then
      IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' su partita di giro non pu? avere pi? di una scadenza e di un dettaglio '||CNRCTB035.getDesc(aObb));
  End;

 End;



 procedure getPgiroCdsBase(
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype,
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%rowtype,
    fromEntrataSpesa CHAR,
    accConScad CHAR Default 'N',    --? 'Y' quando viene chiamata dalla Liquidazione CORI per le liquidazioni accentrate, 'X' dalla liquidazione della '999.000'
    aTSNow DATE Default Null,	    --? valorizzato quando viene chiamata dalla Liquidazione CORI
    aUser VARCHAR2 Default Null     --? valorizzato quando viene chiamata dalla Liquidazione CORI
 ) is
  aAssPGiro ass_obb_acr_pgiro%rowtype;
 begin
  begin
   select * into aObb from obbligazione where
             cd_cds = aObb.cd_cds
	 And esercizio = aObb.esercizio
      	 And esercizio_originale = aObb.esercizio_originale
	 And pg_obbligazione = aObb.pg_obbligazione
		 and fl_pgiro = 'Y'
		 for update nowait;
   select * into aObbScad from obbligazione_scadenzario where
             cd_cds = aObb.cd_cds
	 And esercizio = aObb.esercizio
      	 And esercizio_originale = aObb.esercizio_originale
	 And pg_obbligazione = aObb.pg_obbligazione
		 for update nowait;
   select * into aObbScadVoce from obbligazione_scad_voce where
             cd_cds = aObb.cd_cds
	 And esercizio = aObb.esercizio
      	 And esercizio_originale = aObb.esercizio_originale
	 And pg_obbligazione = aObb.pg_obbligazione
   for update nowait;
  exception
   when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' su partita di giro non trovata (2) '||CNRCTB035.getDesc(aObb));
   when TOO_MANY_ROWS then
    IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' su partita di giro non pu? avere pi? di una scadenza e di un dettaglio '||CNRCTB035.getDesc(aObb));
  end;

--  if aObb.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
--   IBMERR001.RAISE_ERR_GENERICO('Funzione non supportata per '||cnrutil.getLabelObbligazioniMin()||' su partita di giro sull''Ente');
--  end if;

  Begin
   select * into aAssPGiro from ASS_OBB_ACR_PGIRO where
        cd_cds = aObbScadVoce.cd_cds
	and esercizio = aObbScadVoce.esercizio
	and esercizio_ori_obbligazione = aObbScadVoce.esercizio_originale
	and pg_obbligazione = aObbScadVoce.pg_obbligazione
	and ti_origine = fromEntrataSpesa
   for update nowait;
  Exception
    when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('Obbligazione su partita di giro '||CNRCTB035.getDesc(aObb)||' non associato al relativo accertamento');
  End;
  If accConScad = 'N' Then
    Begin
      select * into aAcc from accertamento where
             cd_cds = aAssPGiro.cd_cds
	 And esercizio = aAssPGiro.esercizio
      	 And esercizio_originale = aAssPGiro.esercizio_ori_accertamento
	 And pg_accertamento = aAssPGiro.pg_accertamento
		 and fl_pgiro = 'Y'
		 for update nowait;
      select * into aAccScad from accertamento_scadenzario where
             cd_cds = aAssPGiro.cd_cds
	 And esercizio = aAssPGiro.esercizio
      	 And esercizio_originale = aAssPGiro.esercizio_ori_accertamento
	 And pg_accertamento = aAssPGiro.pg_accertamento
		 for update nowait;
      select * into aAccScadVoce from accertamento_scad_voce where
             cd_cds = aAssPGiro.cd_cds
	 And esercizio = aAssPGiro.esercizio
      	 And esercizio_originale = aAssPGiro.esercizio_ori_accertamento
	 And pg_accertamento = aAssPGiro.pg_accertamento
            for update nowait;
    Exception
       when NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non trovato '||CNRCTB035.getDesc(aAcc));
       when TOO_MANY_ROWS then
           IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non pu? avere pi? di una scadenza e di un dettaglio '||CNRCTB035.getDesc(aAcc));
    End;
  elsif accConScad = 'X' Then
    Begin
      select * into aAcc
      from accertamento
      where cd_cds = aAssPGiro.cd_cds
	      And esercizio = aAssPGiro.esercizio
      	And esercizio_originale = aAssPGiro.esercizio_ori_accertamento
	      And pg_accertamento = aAssPGiro.pg_accertamento
		    And fl_pgiro = 'Y'
		  for update nowait;

      select * into aAccScad
      from accertamento_scadenzario
      where cd_cds = aAssPGiro.cd_cds
	      And esercizio = aAssPGiro.esercizio
      	And esercizio_originale = aAssPGiro.esercizio_ori_accertamento
	      And pg_accertamento = aAssPGiro.pg_accertamento
	      And Rownum < 2
	    for update nowait;

      select * into aAccScadVoce
      from accertamento_scad_voce
      where cd_cds = aAssPGiro.cd_cds
	      And esercizio = aAssPGiro.esercizio
      	And esercizio_originale = aAssPGiro.esercizio_ori_accertamento
	      And pg_accertamento = aAssPGiro.pg_accertamento
	      And Rownum < 2
      for update nowait;
    Exception
       when NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non trovato '||CNRCTB035.getDesc(aAcc));
       when TOO_MANY_ROWS then
           IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non pu? avere pi? di una scadenza e di un dettaglio '||CNRCTB035.getDesc(aAcc));
    End;
  Else   --accConScad = 'Y'
    Begin
      Select * Into aAcc
      From accertamento
      Where cd_cds = aAssPGiro.cd_cds
	      And esercizio = aAssPGiro.esercizio
	      And esercizio_originale = aAssPGiro.esercizio_ori_accertamento
      	And pg_accertamento = aAssPGiro.pg_accertamento
	      And fl_pgiro = 'Y'
      For update nowait;
    Exception
      When NO_DATA_FOUND Then
          IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non trovato '||CNRCTB035.getDesc(aAcc));
    End;
    Begin
      --Prendo la scadenza legata all'unica Reversale Provvisoria che dovrebbe esserci
      Select * Into aAccScad
      From accertamento_scadenzario a
      Where a.cd_cds = aAssPGiro.cd_cds
      	And a.esercizio = aAssPGiro.esercizio
        And a.esercizio_originale = aAssPGiro.esercizio_ori_accertamento
	      And a.pg_accertamento = aAssPGiro.pg_accertamento
	      And (a.cd_cds, a.esercizio, a.esercizio_originale, a.pg_accertamento, a.pg_accertamento_scadenzario) In
	    (Select rr.cd_cds, rr.esercizio, rr.esercizio_ori_accertamento, rr.pg_accertamento, rr.pg_accertamento_scadenzario
	     From reversale_riga rr, reversale r
	     Where r.esercizio = rr.esercizio
	       And r.cd_cds = rr.cd_cds
	       And r.pg_reversale = rr.pg_reversale
	       And rr.cd_cds = a.cd_cds
	       And rr.esercizio = a.esercizio
	       And rr.esercizio_ori_accertamento = a.esercizio_originale
	       And rr.pg_accertamento = a.pg_accertamento
	       And rr.pg_accertamento_scadenzario = a.pg_accertamento_scadenzario
	       And r.cd_tipo_documento_cont=CNRCTB018.TI_DOC_REV_PROVV)
       For update nowait;

       Select * Into aAccScadVoce
       From accertamento_scad_voce
       Where cd_cds = aAccScad.cd_cds
         And esercizio = aAccScad.esercizio
         And esercizio_originale = aAccScad.esercizio_originale
         And pg_accertamento = aAccScad.pg_accertamento
         And pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario
        For update nowait;

    Exception
          When NO_DATA_FOUND Then
             --LIQUID_GRUPPO_CENTRO ? in stato 'I' ma la Reversale provvisoria ? chiusa
             --Inserimento di una nuova scadenza per la pgiro di entrata
             Declare
	               aScadenzaOld accertamento_scadenzario%Rowtype;
                 aScad_voceOld accertamento_scad_voce%Rowtype;
                 aDocGenRigaOld documento_generico_riga%Rowtype;
                 aDocGenRiga documento_generico_riga%Rowtype;
                 aRev reversale%Rowtype;
  		           aRevRiga reversale_riga%Rowtype;
	           Begin
	              Begin
		                Select *
		                Into aScadenzaOld
		                From accertamento_scadenzario a
		                Where a.cd_cds = aAcc.cd_cds
		                  And a.esercizio = aAcc.esercizio
        	            And a.esercizio_originale = aAcc.esercizio_originale
                      And a.pg_accertamento = aAcc.pg_accertamento
		                  And a.pg_accertamento_scadenzario = (Select Max(pg_accertamento_scadenzario)
		  		  			                                         From accertamento_scadenzario b
		  					                                           Where b.cd_cds = aAcc.cd_cds
		    					                                           And b.esercizio = aAcc.esercizio
		    					                                           And b.esercizio_originale = aAcc.esercizio_originale
        	    					                                     And b.pg_accertamento = aAcc.pg_accertamento);
		            Exception
		               When NO_DATA_FOUND Then
          		       IBMERR001.RAISE_ERR_GENERICO('Non trovato lo scadenzario per l''Accertamento su partita di giro '||CNRCTB035.getDesc(aAcc));
		            End;
	     	        aAccScad.cd_cds := aScadenzaOld.cd_cds;
	     	        aAccScad.esercizio := aScadenzaOld.esercizio;
	     	        aAccScad.esercizio_originale := aScadenzaOld.esercizio_originale;
	     	        aAccScad.pg_accertamento := aScadenzaOld.pg_accertamento;
		            aAccScad.pg_accertamento_scadenzario := aScadenzaOld.pg_accertamento_scadenzario + 1;
       		      aAccScad.dt_scadenza_emissione_fattura:=aTSNow;
       		      aAccScad.dt_scadenza_incasso:=aTSNow;
       		      aAccScad.ds_scadenza := aScadenzaOld.ds_scadenza;
		            aAccScad.im_scadenza := 0;
		            aAccScad.im_associato_doc_amm := 0;
		            aAccScad.im_associato_doc_contabile := 0;
		            aAccScad.dacr := aTSNow;
		            aAccScad.utcr := aUser;
                aAccScad.duva := aTSNow;
		            aAccScad.utuv := aUser;
		            aAccScad.pg_ver_rec := 1;
		            aAccScad.pg_acc_scad_ori_riporto := Null;
                CNRCTB035.INS_accertamento_SCADENZARIO(aAccScad);

		            For aScad_voceOld In (Select * From accertamento_scad_voce
		            		       Where cd_cds = aScadenzaOld.cd_cds
		               			 And esercizio = aScadenzaOld.esercizio
		               			 And esercizio_originale = aScadenzaOld.esercizio_originale
                   	    			 And pg_accertamento = aScadenzaOld.pg_accertamento
		               			 And pg_accertamento_scadenzario = aScadenzaOld.pg_accertamento_scadenzario) Loop
		                         aAccScadVoce.cd_cds := aScad_voceOld.cd_cds;
	                	          aAccScadVoce.esercizio := aScad_voceOld.esercizio;
	                	          aAccScadVoce.esercizio_originale := aScad_voceOld.esercizio_originale;
	                	          aAccScadVoce.pg_accertamento := aScad_voceOld.pg_accertamento;
		                         aAccScadVoce.pg_accertamento_scadenzario := aAccScad.pg_accertamento_scadenzario;
		                         aAccScadVoce.cd_centro_responsabilita := aScad_voceOld.cd_centro_responsabilita;
		                         aAccScadVoce.cd_linea_attivita := aScad_voceOld.cd_linea_attivita;
		                         aAccScadVoce.im_voce := 0;
		                         aAccScadVoce.cd_fondo_ricerca := aScad_voceOld.cd_fondo_ricerca;
		                         aAccScadVoce.dacr := aTSNow;
		                         aAccScadVoce.utcr := aUser;
                  		        aAccScadVoce.duva := aTSNow;
		                         aAccScadVoce.utuv := aUser;
		                         aAccScadVoce.pg_ver_rec := 1;
		                         CNRCTB035.ins_ACCERTAMENTO_SCAD_VOCE(aAccScadVoce);
		            End Loop;
                 --Inserimento di una nuova riga del documento generico legato alla pgiro
                 Select * Into aDocGenRigaOld
                 From documento_generico_riga
                 Where cd_cds = aAcc.cd_cds
		               And esercizio = aAcc.esercizio
		               And cd_unita_organizzativa = aAcc.cd_unita_organizzativa
		               And cd_cds_accertamento = aAcc.cd_cds
		               And esercizio_accertamento = aAcc.esercizio
		               And esercizio_ori_accertamento = aAcc.esercizio_originale
		               And pg_accertamento = aAcc.pg_accertamento
		               And pg_accertamento_scadenzario = aScadenzaOld.pg_accertamento_scadenzario;

		             aDocGenRiga.cd_cds := aDocGenRigaOld.cd_cds;
  		           aDocGenRiga.cd_unita_organizzativa := aDocGenRigaOld.cd_unita_organizzativa;
  		           aDocGenRiga.esercizio := aDocGenRigaOld.esercizio;
  		           aDocGenRiga.cd_tipo_documento_amm := aDocGenRigaOld.cd_tipo_documento_amm;
  		           aDocGenRiga.pg_documento_generico := aDocGenRigaOld.pg_documento_generico;
  		           aDocGenRiga.progressivo_riga := aDocGenRigaOld.progressivo_riga + 1;
  		           aDocGenRiga.ds_riga := aDocGenRigaOld.ds_riga;
  		           aDocGenRiga.im_riga_divisa := 0;
  		           aDocGenRiga.im_riga := 0;
  		           aDocGenRiga.cd_terzo := aDocGenRigaOld.cd_terzo;
  		           aDocGenRiga.cd_terzo_cessionario := aDocGenRigaOld.cd_terzo_cessionario;
  		           aDocGenRiga.cd_terzo_uo_cds := aDocGenRigaOld.cd_terzo_uo_cds;
  		           aDocGenRiga.ragione_sociale := aDocGenRigaOld.ragione_sociale;
  		           aDocGenRiga.nome := aDocGenRigaOld.nome;
  		           aDocGenRiga.cognome := aDocGenRigaOld.cognome;
  		           aDocGenRiga.codice_fiscale := aDocGenRigaOld.codice_fiscale;
  		           aDocGenRiga.partita_iva := aDocGenRigaOld.partita_iva;
  		           aDocGenRiga.cd_modalita_pag := aDocGenRigaOld.cd_modalita_pag;
  		           aDocGenRiga.pg_banca := aDocGenRigaOld.pg_banca;
  		           aDocGenRiga.cd_termini_pag := aDocGenRigaOld.cd_termini_pag;
  		           aDocGenRiga.cd_termini_pag_uo_cds := aDocGenRigaOld.cd_termini_pag_uo_cds;
  		           aDocGenRiga.pg_banca_uo_cds := aDocGenRigaOld.pg_banca_uo_cds;
  		           aDocGenRiga.cd_modalita_pag_uo_cds := aDocGenRigaOld.cd_modalita_pag_uo_cds;
  		           aDocGenRiga.note := aDocGenRigaOld.note;
  		           aDocGenRiga.dt_da_competenza_coge := aDocGenRigaOld.dt_da_competenza_coge;
  		           aDocGenRiga.dt_a_competenza_coge := aDocGenRigaOld.dt_a_competenza_coge;
  		           aDocGenRiga.stato_cofi := aDocGenRigaOld.stato_cofi;
  		           aDocGenRiga.dt_cancellazione := Null;
  		           aDocGenRiga.cd_cds_obbligazione := Null;
  		           aDocGenRiga.esercizio_obbligazione := Null;
  		           aDocGenRiga.esercizio_ori_obbligazione := Null;
  		           aDocGenRiga.pg_obbligazione  := Null;
  		           aDocGenRiga.pg_obbligazione_scadenzario := Null;
  		           aDocGenRiga.cd_cds_accertamento := aAcc.cd_cds;
  		           aDocGenRiga.esercizio_accertamento := aAcc.esercizio;
  		           aDocGenRiga.esercizio_ori_accertamento := aAcc.esercizio_originale;
  		           aDocGenRiga.pg_accertamento := aAcc.pg_accertamento;
  		           aDocGenRiga.pg_accertamento_scadenzario  := aAccScadVoce.pg_accertamento_scadenzario;
  		           aDocGenRiga.dacr := aTSNow;
  		           aDocGenRiga.utcr := aUser;
  		           aDocGenRiga.utuv := aUser;
  		           aDocGenRiga.duva := aTSNow;
  		           aDocGenRiga.pg_ver_rec := 1;
  		           aDocGenRiga.ti_associato_manrev := CNRCTB100.TI_ASSOC_TOT_MAN_REV;
		             CNRCTB100.ins_DOCUMENTO_GENERICO_RIGA(aDocGenRiga);

		             --Inserimento di una nuova Reversale Provvisoria a cui associare le righe create
                 aRev:=null;
    		         aRevRiga:=null;

    		         aRev.CD_CDS:=aAcc.cd_cds;
    	           aRev.ESERCIZIO:=aAcc.esercizio;
    	           aRev.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
    	           aRev.CD_CDS_ORIGINE:=aAcc.cd_cds;
    	           aRev.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
    	           aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV_PROVV;
    	           aRev.TI_REVERSALE:=CNRCTB038.TI_REV_SOS;
    	           aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
    	           aRev.DS_REVERSALE:=aDocGenRiga.ds_riga;
    	           aRev.STATO:=CNRCTB038.STATO_REV_EME;
    	           aRev.DT_EMISSIONE:=TRUNC(aTSNow);
    	           aRev.IM_REVERSALE:=0;
    	           aRev.IM_INCASSATO:=0;
    	           aRev.DACR:=aTSNow;
    	           aRev.UTCR:=aUser;
    	           aRev.DUVA:=aTSNow;
    	           aRev.UTUV:=aUser;
    	           aRev.PG_VER_REC:=1;
    	           aRev.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;
    	           aRevRiga.CD_CDS:=aRev.cd_cds;
    	           aRevRiga.ESERCIZIO:=aRev.esercizio;
    	           aRevRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.esercizio;
    	           aRevRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.esercizio_originale;
    	           aRevRiga.PG_ACCERTAMENTO:=aAcc.pg_accertamento;
    	           aRevRiga.PG_ACCERTAMENTO_SCADENZARIO:=aAccScad.pg_accertamento_scadenzario;
    	           aRevRiga.CD_CDS_DOC_AMM:=aDocGenRiga.cd_cds;
    	           aRevRiga.CD_UO_DOC_AMM:=aDocGenRiga.cd_unita_organizzativa;
    	           aRevRiga.ESERCIZIO_DOC_AMM:=aDocGenRiga.esercizio;
    	           aRevRiga.CD_TIPO_DOCUMENTO_AMM:= aDocGenRiga.cd_tipo_documento_amm;
    	           aRevRiga.PG_DOC_AMM:=aDocGenRiga.pg_documento_generico;
    	           aRevRiga.DS_REVERSALE_RIGA:=aRev.ds_reversale;
    	           aRevRiga.STATO:=aRev.STATO;
    	           aRevRiga.CD_TERZO:=aDocGenRiga.cd_terzo;
    	           aRevRiga.CD_TERZO_UO:=aDocGenRiga.cd_terzo_uo_cds;
    	           aRevRiga.PG_BANCA:=aDocGenRiga.pg_banca_uo_cds;
    	           aRevRiga.CD_MODALITA_PAG:=aDocGenRiga.cd_modalita_pag_uo_cds;
    	           aRevRiga.IM_REVERSALE_RIGA:=0;
    	           aRevRiga.FL_PGIRO:='Y';
    	           aRevRiga.UTCR:=aUser;
    	           aRevRiga.DACR:=aTSNow;
    	           aRevRiga.UTUV:=aUser;
    	           aRevRiga.DUVA:=aTSNow;
    	           aRevRiga.PG_VER_REC:=1;
    	           CNRCTB037.generaRevProvvPgiro(aRev, aRevRiga, aTSNow, aUser);
    	     End;
   	  When TOO_MANY_ROWS then
    	    IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non pu? essere legato a pi? Reversali Provvisorie '||CNRCTB035.getDesc(aAcc));
    End;
  End If;
 End;

 procedure getPgiroCds(
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%rowtype,
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype
 ) is
 Begin
  getPgiroCdsBase(
    aAcc,
    aAccScad,
    aAccScadVoce,
    aObb,
    aObbScad,
    aObbScadVoce,
    CNRCTB001.GESTIONE_ENTRATE);
 end;

 procedure getPgiroCdsInv(
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%rowtype,
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype
 ) is
 begin
  getPgiroCdsBase(
    aAcc,
    aAccScad,
    aAccScadVoce,
    aObb,
    aObbScad,
    aObbScadVoce,
	CNRCTB001.GESTIONE_SPESE
  );
 end;

 procedure getPgiroCds(
    aObb IN OUT obbligazione%rowtype,
    aObbScad IN OUT obbligazione_scadenzario%rowtype,
    aObbScadVoce IN OUT obbligazione_scad_voce%rowtype,
    aAcc IN OUT accertamento%rowtype,
    aAccScad IN OUT accertamento_scadenzario%rowtype,
    aAccScadVoce IN OUT accertamento_scad_voce%rowtype
 ) is
 begin
  getPgiroCdsBase(
    aObb,
    aObbScad,
    aObbScadVoce,
    aAcc,
    aAccScad,
    aAccScadVoce,
    CNRCTB001.GESTIONE_SPESE
  );
 end;

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
 ) is
 begin
  getPgiroCdsBase(
    aObb,
    aObbScad,
    aObbScadVoce,
    aAcc,
    aAccScad,
    aAccScadVoce,
    CNRCTB001.GESTIONE_ENTRATE,
    aAccConScad,
    aTSNow,
    aUser
  );
 end;

function getPgUltimaVar(aAcc accertamento%rowtype, tipo varchar2) return number is
aPg number;
begin
	 if tipo = 'D' then  -- variazione per il documento
		 select max(pg_variazione) into aPg
		 from variazione_formale_acc
		 where cd_cds			= aAcc.cd_cds
		   and esercizio		= aAcc.esercizio
		   and esercizio_originale = aAcc.esercizio_originale
		   and pg_accertamento	= aAcc.pg_accertamento;

		 if aPg is null then aPg := 0; end if;
	 else -- tipo = 'V' variazione per il documento/voce
	 	 select max(pg_variazione) into aPg
		 from variazione_formale_acc
		 where cd_cds			= aAcc.cd_cds
		   and esercizio		= aAcc.esercizio
		   and esercizio_originale = aAcc.esercizio_originale
		   and pg_accertamento	= aAcc.pg_accertamento
		   and cd_voce			= aAcc.cd_voce;

		 if aPg is null then aPg := 0; end if;
	  end if;
	  return aPg;
end;

procedure calcolaPg_ImIniz(aAcc accertamento%rowtype, aPg in out number, aIm in out number) is
pg_ultima_var_voce number := 0;
pg_ultima_var_doc number := 0;
begin
	pg_ultima_var_doc := getPgUltimaVar(aAcc,'D');
	pg_ultima_var_voce := getPgUltimaVar(aAcc,'V');

	if pg_ultima_var_doc = 0 then
	   -- non esistono variazioni precedenti per il documento
	   aPg := 0;
	   aIm := aAcc.im_accertamento;
	else
	   aPg := pg_ultima_var_doc;
	   if pg_ultima_var_voce = 0 then
	   	  -- non esistono variazioni precedenti per il documento/voce
		  aIm := 0;
	   else
		  select im_iniziale into aIm
		  from variazione_formale_acc
		  where cd_cds 			  = aAcc.cd_cds
		    and esercizio		  = aAcc.esercizio
		        and esercizio_originale = aAcc.esercizio_originale
			and pg_accertamento	  = aAcc.pg_accertamento
			and cd_voce			  = aAcc.cd_voce
			and pg_variazione	  = pg_ultima_var_voce;
	   end if;
	end if;

end;

 procedure ins_VARIAZIONE_FORMALE_ACC (aDest VARIAZIONE_FORMALE_ACC%rowtype) is
 begin
   insert into VARIAZIONE_FORMALE_ACC (
     CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_ACCERTAMENTO
    ,CD_VOCE
    ,PG_VARIAZIONE
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,IM_INIZIALE
    ,IM_VARIAZIONE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_ACCERTAMENTO
    ,aDest.CD_VOCE
    ,aDest.PG_VARIAZIONE
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.IM_INIZIALE
    ,aDest.IM_VARIAZIONE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

procedure creaVariazioneFormaleAcc(oldAcc accertamento%rowtype, newAcc accertamento%rowtype) is
aDest variazione_formale_acc%rowtype;
im_iniz number;
pg_ultima_var number := 0;
aStato char(1);
begin
	if oldAcc.CD_TIPO_DOCUMENTO_CONT = 'ACR_RES'  then

	   begin
	   	select st_apertura_chiusura into aStato
		from esercizio
		where cd_cds = oldAcc.cd_cds
		  and esercizio = oldAcc.esercizio - 1;
	   exception when no_data_found then
	   	 aStato := CNRCTB008.STATO_CHIUSURA_DEFINITIVA;
	   end;

	   if aStato = CNRCTB008.STATO_CHIUSURA_DEFINITIVA and -- l'esercizio precedente ? chiuso
		  (oldAcc.IM_ACCERTAMENTO <> newAcc.IM_ACCERTAMENTO
		   or oldAcc.CD_VOCE 		  <> newAcc.CD_VOCE) then -- ? cambiato l'importo o il capitolo per ACR_RES

			if (oldAcc.ESERCIZIO       = newAcc.ESERCIZIO and
			    oldAcc.TI_APPARTENENZA = newAcc.TI_APPARTENENZA and
			    oldAcc.TI_GESTIONE 	   = newAcc.TI_GESTIONE and
			    oldAcc.CD_VOCE 		   = newAcc.CD_VOCE) then  -- non ? cambiato il capitolo

			   calcolaPg_Iminiz(oldAcc,pg_ultima_var, im_iniz);

			   aDest.CD_CDS  		 := newAcc.CD_CDS;
			   aDest.ESERCIZIO		 := newAcc.ESERCIZIO;
			   aDest.ESERCIZIO_ORIGINALE := newAcc.ESERCIZIO_ORIGINALE;
			   aDest.PG_ACCERTAMENTO := newAcc.PG_ACCERTAMENTO;
			   aDest.CD_VOCE		 := newAcc.CD_VOCE;
			   aDest.PG_VARIAZIONE	 := pg_ultima_var + 1;
			   aDest.TI_APPARTENENZA := newAcc.TI_APPARTENENZA;
			   aDest.TI_GESTIONE	 := newAcc.TI_GESTIONE;
			   aDest.IM_INIZIALE	 := im_iniz;
			   aDest.IM_VARIAZIONE	 := newAcc.IM_ACCERTAMENTO - oldAcc.IM_ACCERTAMENTO;
			   aDest.DACR			 := sysdate;
			   aDest.UTCR			 := newAcc.UTUV;
			   aDest.DUVA			 := sysdate;
			   aDest.UTUV			 := newAcc.UTUV;
			   aDest.PG_VER_REC	  	 := 1;

			   ins_VARIAZIONE_FORMALE_ACC(aDest);

			else -- ? cambiato il capitolo
			-- variazione sul capitolo "vecchio"
			   calcolaPg_Iminiz(oldAcc,pg_ultima_var, im_iniz);

			   aDest.CD_CDS  		 := oldAcc.CD_CDS;
			   aDest.ESERCIZIO		 := oldAcc.ESERCIZIO;
			   aDest.ESERCIZIO_ORIGINALE := oldAcc.ESERCIZIO_ORIGINALE;
			   aDest.PG_ACCERTAMENTO := oldAcc.PG_ACCERTAMENTO;
			   aDest.CD_VOCE		 := oldAcc.CD_VOCE;
			   aDest.PG_VARIAZIONE	 := pg_ultima_var + 1;
			   aDest.TI_APPARTENENZA := oldAcc.TI_APPARTENENZA;
			   aDest.TI_GESTIONE	 := oldAcc.TI_GESTIONE;
			   aDest.IM_INIZIALE	 := im_iniz;
			   aDest.IM_VARIAZIONE	 := 0 - oldAcc.IM_ACCERTAMENTO;
			   aDest.DACR			 := sysdate;
			   aDest.UTCR			 := newAcc.UTUV;
			   aDest.DUVA			 := sysdate;
			   aDest.UTUV			 := newAcc.UTUV;
			   aDest.PG_VER_REC	  	 := 1;

			   ins_VARIAZIONE_FORMALE_ACC(aDest);

			   -- variazione sul capitolo "nuovo"
			   calcolaPg_Iminiz(newAcc,pg_ultima_var, im_iniz);

			   aDest.CD_CDS  		 := newAcc.CD_CDS;
			   aDest.ESERCIZIO		 := newAcc.ESERCIZIO;
			   aDest.ESERCIZIO_ORIGINALE := newAcc.ESERCIZIO_ORIGINALE;
			   aDest.PG_ACCERTAMENTO := newAcc.PG_ACCERTAMENTO;
			   aDest.CD_VOCE		 := newAcc.CD_VOCE;
			   aDest.PG_VARIAZIONE	 := pg_ultima_var + 1;
			   aDest.TI_APPARTENENZA := newAcc.TI_APPARTENENZA;
			   aDest.TI_GESTIONE	 := newAcc.TI_GESTIONE;
			   aDest.IM_INIZIALE	 := im_iniz;
			   aDest.IM_VARIAZIONE	 := newAcc.IM_ACCERTAMENTO;
			   aDest.DACR			 := sysdate;
			   aDest.UTCR			 := newAcc.UTUV;
			   aDest.DUVA			 := sysdate;
			   aDest.UTUV			 := newAcc.UTUV;
			   aDest.PG_VER_REC	  	 := 1;

			   ins_VARIAZIONE_FORMALE_ACC(aDest);

			end if;

	   end if;
	end if;
end;

 procedure ins_VARIAZIONE_FORMALE_IMP (aDest VARIAZIONE_FORMALE_IMP%rowtype) is
  begin
   insert into VARIAZIONE_FORMALE_IMP (
     CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_OBBLIGAZIONE
    ,CD_VOCE
    ,PG_VARIAZIONE
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,IM_INIZIALE
    ,IM_VARIAZIONE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.CD_VOCE
    ,aDest.PG_VARIAZIONE
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.IM_INIZIALE
    ,aDest.IM_VARIAZIONE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

function getPgUltimaVar(aObbScadVoce obbligazione_scad_voce%rowtype, tipo varchar2) return number is
aPg number;
begin
	 if tipo = 'D' then  -- variazione per il documento
		 select max(pg_variazione) into aPg
		 from variazione_formale_imp
		 where cd_cds			= aObbScadVoce.cd_cds
		   and esercizio		= aObbScadVoce.esercizio
		   and esercizio_originale = aObbScadVoce.esercizio_originale
		   and pg_obbligazione	= aObbScadVoce.pg_obbligazione;

		 if aPg is null then aPg := 0; end if;
	 else -- tipo = 'V' variazione per il documento/voce
	 	 select max(pg_variazione) into aPg
		 from variazione_formale_imp
		 where cd_cds			= aObbScadVoce.cd_cds
		   and esercizio		= aObbScadVoce.esercizio
		   and esercizio_originale = aObbScadVoce.esercizio_originale
		   and pg_obbligazione	= aObbScadVoce.pg_obbligazione
		   and cd_voce			= aObbScadVoce.cd_voce;

		 if aPg is null then aPg := 0; end if;
	  end if;
	  return aPg;
end;

procedure calcolaPg_ImIniz(aObbScadVoce obbligazione_scad_voce%rowtype, aPg in out number, aIm in out number) is
pg_ultima_var_voce number := 0;
pg_ultima_var_doc number := 0;
begin
	pg_ultima_var_doc := getPgUltimaVar(aObbScadVoce,'D');
	pg_ultima_var_voce := getPgUltimaVar(aObbScadVoce,'V');

	if pg_ultima_var_doc = 0 then
	   -- non esistono variazioni precedenti per il documento
	   aPg := 0;
	   aIm := aObbScadVoce.im_voce;
	else
	   aPg := pg_ultima_var_doc;
	   if pg_ultima_var_voce = 0 then
	   	  -- non esistono variazioni precedenti per il documento/voce
		  aIm := 0;
	   else
		  select im_iniziale into aIm
		  from variazione_formale_imp
		  where cd_cds 			  = aObbScadVoce.cd_cds
		    and esercizio		  = aObbScadVoce.esercizio
		        and esercizio_originale = aObbScadVoce.esercizio_originale
			and pg_obbligazione	  = aObbScadVoce.pg_obbligazione
			and cd_voce			  = aObbScadVoce.cd_voce
			and pg_variazione	  = pg_ultima_var_voce;
	   end if;
	end if;

end;

procedure creaVariazioneFormaleImpAU(oldObbScadVoce obbligazione_scad_voce%rowtype,newObbScadVoce obbligazione_scad_voce%rowtype) is
aDest variazione_formale_imp%rowtype;
im_iniz number;
pg_ultima_var number := 0;
tipo_doc_cont varchar2(10);
aStato char(1);
begin
	 select CD_TIPO_DOCUMENTO_CONT into tipo_doc_cont
	 from obbligazione
	 where cd_cds 	  	   = oldObbScadVoce.CD_CDS
	   and esercizio 	   = oldObbScadVoce.ESERCIZIO
           and esercizio_originale = oldObbScadVoce.ESERCIZIO_ORIGINALE
	   and pg_obbligazione = oldObbScadVoce.PG_OBBLIGAZIONE;

	 if tipo_doc_cont = 'IMP_RES' then

/* 06.03.2006 SF SVINCOLATA LA GENERAZIONE DELLE VARIAZIONI FORMALI PER VARIAZIONI AUTOMATICHE DA APPROVAZIONE
                 VARIAZIONI ALLO STANZIAMENTO RESIDUO */

/*
		begin
		 select st_apertura_chiusura into aStato
		 from esercizio
		 where cd_cds    = oldObbScadVoce.cd_cds
		   and esercizio = oldObbScadVoce.esercizio - 1;
		exception when no_data_found then
		   aStato := CNRCTB008.STATO_CHIUSURA_DEFINITIVA;
		end;

	    if aStato = CNRCTB008.STATO_CHIUSURA_DEFINITIVA and -- l'esercizio precedente ? chiuso
	 	   oldObbScadVoce.IM_VOCE <> newObbScadVoce.IM_VOCE then  -- ? stato cambiato l'importo
*/

	    if oldObbScadVoce.IM_VOCE <> newObbScadVoce.IM_VOCE then  -- ? stato cambiato l'importo

			calcolaPg_ImIniz(oldObbScadVoce,pg_ultima_var,im_iniz);

			aDest.CD_CDS		  := newObbScadVoce.CD_CDS;
			aDest.ESERCIZIO		  := newObbScadVoce.ESERCIZIO;
			aDest.ESERCIZIO_ORIGINALE := newObbScadVoce.ESERCIZIO_ORIGINALE;
			aDest.PG_OBBLIGAZIONE := newObbScadVoce.PG_OBBLIGAZIONE;
			aDest.CD_VOCE		  := newObbScadVoce.CD_VOCE;
			aDest.PG_VARIAZIONE	  := pg_ultima_var + 1;
			aDest.TI_APPARTENENZA := newObbScadVoce.TI_APPARTENENZA;
			aDest.TI_GESTIONE	  := newObbScadVoce.TI_GESTIONE;
			aDest.IM_INIZIALE	  := im_iniz;
			aDest.IM_VARIAZIONE	  := newObbScadVoce.IM_VOCE - oldObbScadVoce.IM_VOCE;
			aDest.DACR			  := sysdate;
			aDest.UTCR			  := newObbScadVoce.UTUV;
			aDest.DUVA			  := sysdate;
			aDest.UTUV			  := newObbScadVoce.UTUV;
			aDest.PG_VER_REC	  := 1;

			ins_VARIAZIONE_FORMALE_IMP(aDest);

	 	end if;
	end if;

end;

procedure creaVariazioneFormaleImpAD(oldObbScadVoce obbligazione_scad_voce%rowtype) is
aDest variazione_formale_imp%rowtype;
im_iniz number;
pg_ultima_var number := 0;
aTipoDocCont varchar2(10);
aUser varchar2(20);
aStato char(1);

Begin
Dbms_Output.PUT_LINE ('A1');
	 select distinct cd_tipo_documento_cont into aTipoDocCont
	 from obbligazione_s
	 where cd_cds 	  	   = oldObbScadVoce.CD_CDS
	   and esercizio 	   = oldObbScadVoce.ESERCIZIO
	   and esercizio_originale = oldObbScadVoce.ESERCIZIO_ORIGINALE
	   and pg_obbligazione = oldObbScadVoce.PG_OBBLIGAZIONE;

	 if aTipoDocCont = 'IMP_RES'  then

/* 06.03.2006 SF SVINCOLATA LA GENERAZIONE DELLE VARIAZIONI FORMALI PER VARIAZIONI AUTOMATICHE DA APPROVAZIONE
                 VARIAZIONI ALLO STANZIAMENTO RESIDUO */


	 	begin
		 select st_apertura_chiusura into aStato
		 from esercizio
		 where cd_cds    = oldObbScadVoce.cd_cds
		   and esercizio = oldObbScadVoce.esercizio - 1;
		exception when no_data_found then
				  aStato := CNRCTB008.STATO_CHIUSURA_DEFINITIVA;
		end;

		if aStato = CNRCTB008.STATO_CHIUSURA_DEFINITIVA then

Dbms_Output.PUT_LINE ('A');

			select utuv into aUser
			from obbligazione
			where cd_cds	 	  = oldObbScadVoce.CD_CDS
			  and esercizio		  = oldObbScadVoce.ESERCIZIO
	                  and esercizio_originale = oldObbScadVoce.ESERCIZIO_ORIGINALE
			  and pg_obbligazione = oldObbScadVoce.PG_OBBLIGAZIONE;
Dbms_Output.PUT_LINE ('B');
			calcolaPg_ImIniz(oldObbScadVoce,pg_ultima_var,im_iniz);
Dbms_Output.PUT_LINE ('C');
			aDest.CD_CDS		  := oldObbScadVoce.CD_CDS;
			aDest.ESERCIZIO		  := oldObbScadVoce.ESERCIZIO;
			aDest.ESERCIZIO_ORIGINALE := oldObbScadVoce.ESERCIZIO_ORIGINALE;
			aDest.PG_OBBLIGAZIONE := oldObbScadVoce.PG_OBBLIGAZIONE;
			aDest.CD_VOCE		  := oldObbScadVoce.CD_VOCE;
			aDest.PG_VARIAZIONE	  := pg_ultima_var + 1;
			aDest.TI_APPARTENENZA := oldObbScadVoce.TI_APPARTENENZA;
			aDest.TI_GESTIONE	  := oldObbScadVoce.TI_GESTIONE;
			aDest.IM_INIZIALE	  := im_iniz;
			aDest.IM_VARIAZIONE	  := 0 - oldObbScadVoce.IM_VOCE;
			aDest.DACR			  := sysdate;
			aDest.UTCR			  := aUser;
			aDest.DUVA			  := sysdate;
			aDest.UTUV			  := aUser;
			aDest.PG_VER_REC	  := 1;
Dbms_Output.PUT_LINE ('D');
			ins_VARIAZIONE_FORMALE_IMP(aDest);
	 	end if;
	end if;
end;

procedure creaVariazioneFormaleImpAI(newObbScadVoce obbligazione_scad_voce%rowtype) is
aDest variazione_formale_imp%rowtype;
im_iniz number;
pg_ultima_var number := 0;
tipo_doc_cont varchar2(10);
aStato char(1);
begin
	 select CD_TIPO_DOCUMENTO_CONT into tipo_doc_cont
	 from obbligazione
	 where cd_cds 	  	   = newObbScadVoce.CD_CDS
	   and esercizio 	   = newObbScadVoce.ESERCIZIO
	   and esercizio_originale = newObbScadVoce.ESERCIZIO_ORIGINALE
	   and pg_obbligazione = newObbScadVoce.PG_OBBLIGAZIONE;

	 if tipo_doc_cont = 'IMP_RES' then
/*
		 begin
			 select st_apertura_chiusura into aStato
			 from esercizio
			 where cd_cds = newObbScadVoce.cd_cds
			   and esercizio = newObbScadVoce.esercizio - 1;
		 exception when no_data_found then
		 	 aStato := CNRCTB008.STATO_CHIUSURA_DEFINITIVA;
		 end;

	 	if aStato = CNRCTB008.STATO_CHIUSURA_DEFINITIVA then
*/

			-- variazione formale sul "nuovo" capitolo
			calcolaPg_ImIniz(newObbScadVoce,pg_ultima_var,im_iniz);

			aDest.CD_CDS		  := newObbScadVoce.CD_CDS;
			aDest.ESERCIZIO		  := newObbScadVoce.ESERCIZIO;
			aDest.ESERCIZIO_ORIGINALE := newObbScadVoce.ESERCIZIO_ORIGINALE;
			aDest.PG_OBBLIGAZIONE := newObbScadVoce.PG_OBBLIGAZIONE;
			aDest.CD_VOCE		  := newObbScadVoce.CD_VOCE;
			aDest.PG_VARIAZIONE	  := pg_ultima_var + 1;
			aDest.TI_APPARTENENZA := newObbScadVoce.TI_APPARTENENZA;
			aDest.TI_GESTIONE	  := newObbScadVoce.TI_GESTIONE;
			aDest.IM_INIZIALE	  := im_iniz;
			aDest.IM_VARIAZIONE	  := newObbScadVoce.IM_VOCE;
			aDest.DACR			  := sysdate;
			aDest.UTCR			  := newObbScadVoce.UTUV;
			aDest.DUVA			  := sysdate;
			aDest.UTUV			  := newObbScadVoce.UTUV;
			aDest.PG_VER_REC	  := 1;

			ins_VARIAZIONE_FORMALE_IMP(aDest);

--	 	end if;
	end if;
end;

end;
/


