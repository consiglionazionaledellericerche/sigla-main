CREATE OR REPLACE package CNRCTB037 as
--
-- CNRCTB037 - Package mandato/reversale
--
-- Date: 13/07/2006
-- Version: 3.7
--
-- Package per la gestione DB dell'obbligazione
--
-- Dependency: CNRCTB 001/018/038/100 IBMERR 001
--
-- History:
--
-- Date: 07/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 07/04/2002
-- Version: 1.1
-- Inserimento dell'associazione tra mandato e reversale
--
-- Date: 09/05/2002
-- Version: 1.2
-- Nuova gestione dell'errore e Aggiornamento tabelle MANDATO, REVERSALE per
-- attivazione dello stato sulla trasmissione cassiere;
-- aggiunto attributo stato_trasmissione scorporato da attributo stato
-- in quanto il processo non ? sempre lineare;
-- un mandato/reversale pu? risultare pagato/incassato ma non trasmesso.
--
-- Date: 09/05/2002
-- Version: 1.3
-- Scissione del package due, Un motore principale il cnrctb037 e un package di
-- funzioni generali che gestiscono l'inerimento nel MANDATO e REVERSALE cnrctb038
--
-- Date: 10/05/2002
-- Version: 1.4
-- Gestione dei parametri di input della procedura GENERADOCUMENTI in IN OUT
--
-- Date: 12/05/2002
-- Version: 1.5
-- Spostamento di costanti GP in CNRCTB038
--
-- Date: 14/05/2002
-- Version: 1.6
-- Aggiunta generazione di un solo mandato con n righe
--
-- Date: 19/05/2002
-- Version: 1.7
-- Pagamento/Incasso Mandati e Reversali
--
-- Date: 20/05/2002
-- Version: 1.8
-- Sistemazione meccanismi di aggiornamento saldi a pag/inc man. e rev.
--
-- Date: 20/05/2002
-- Version: 1.9
-- Gestione dell'aggiornamento dello stato dei doc amm tramite il package cnrctb300
-- con la procedura leggiMandatoReversale ().
--
-- Date: 30/05/2002
-- Version: 1.10
-- Impostazione dello stato_coge ='N' per la testata del mandato principale
-- Impostazione dello stato_coge ='X' per tutti i mandati e le reversali collegate al mandato principale
-- Impostazione del DUVA = DACR, UTUV = UTCR e PG_VER_REC =1  per il mandato principale e le sue righe
-- Impostazione del DUVA = DACR, UTUV = UTCR e PG_VER_REC =1  per i mandati collegati e le loro righe
-- Impostazione del DUVA = DACR, UTUV = UTCR e PG_VER_REC =1  per le reversali collegate e le loro righe
--
-- Date: 04/06/2002
-- Version: 1.11
-- Rimozione dei cursori locali, per la gestione delle fetch sul mandato riga e reversale riga
--
-- Date: 10/06/2002
-- Version: 1.12
-- Fix per la gestione dello STATO_COGE del mandato principale, che deve essere posto ad 'N''
--
-- Date: 10/06/2002
-- Version: 1.13
-- Fix errori generazione reversale, aggiornata documentazione dependency
--
-- Date: 18/06/2002
-- Version: 1.14
-- Fix - testate e righe ritornate dopo il salvataggio
-- Fix - sistemata la numerazione dei mandati - NON SI PUO' sommare una costante al numero di mandato corrente sballerebbe la numerazione
-- Fix - segnalazione di errore nel caso il mandato principale sia specificato senza righe
--
-- Date: 21/06/2002
-- Version: 1.15
-- Fix ulteriore su numerazione mandati
--
-- Date: 24/06/2002
-- Version: 1.16
-- Introdotta associazione mandato-mandato
--
-- Date: 27/06/2002
-- Version: 1.17
-- Gestione corretta del bollo
--
-- Date: 27/06/2002
-- Version: 1.17
-- Fix errore recupero bollo
--
-- Date: 02/07/2002
-- Version: 1.19
-- Gestione di registrazione n-reversali/m-mandati senza mandato principale
--
-- Date: 17/07/2002
-- Version: 1.20
-- Troncamento della data di registrazione
--
-- Date: 18/07/2002
-- Version: 1.21
-- Aggiornamento documentazione
--
-- Date: 24/07/2002
-- Version: 1.22
-- Aggiunta metodo di creazione mandato e una reversale collegata
--
-- Date: 30/07/2002
-- Version: 1.23
-- Lo stato COEP dei mandati e reversali collegati viene messo a X
-- solo se non valorizzato nel rowtype passato al metodo di generazione dei documenti
--
-- Date: 11/09/2002
-- Version: 1.24
-- Fix aggiornamento saldi e riscontro mandati/reversali
--
-- Date: 12/09/2002
-- Version: 1.25
-- Fix su aggiornamento saldi per annullamento
--
-- Date: 18/09/2002
-- Version: 1.26
-- Fix determinazione numerazione in creazione mandati secondari
--
-- Date: 25/09/2002
-- Version: 1.27
-- Fix agg. saldo doc autorizzatori
--
-- Date: 27/09/2002
-- Version: 1.28
-- Controllo di disponibilit? di cassa non effettuato su righe di mandati di tipo partita di giro
--
-- Date: 01/10/2002
-- Version: 1.29
-- Aggiornamento del flag partita di giro su riga del documento autorizzatorio sulla base del doc primo collegato
--
-- Date: 14/10/2002
-- Version: 1.30
-- Generazione e collegamento a mandato principale di documenti in numero arbitrario
--
-- Date: 17/10/2002
-- Version: 1.31
-- Fix su recupero tipo bollo per gestione reversale collegata a mandato principale
--
-- Date: 25/11/2002
-- Version: 1.32
-- Controlli di associabilit? mandato reversale per on-line
--
-- Date: 27/11/2002
-- Version: 1.33
-- Fix errore su controlli associabilit? mandato reversale
--
-- Date: 17/12/2002
-- Version: 1.34
-- Aggiunta procedure GENERAREVERSALE
--
-- Date: 19/12/2002
-- Version: 1.35
-- Aggiunta procedure GENERADETT_ETR_SOSPESO
--
-- Date: 22/01/2003
-- Version: 1.36
-- Aggiornamento con segno corretto dell'importo ritenute del mandato a cui collego una reversale CORI
--
-- Date: 04/02/2003
-- Version: 1.37
-- Controllo disassociabilit? mandato/reversale (bloccante se reversale collegata a compenso di cui il mandato e mandato principale)
-- Fix su calcolo della disponibilit? di cassa globale del cds (allineamento con on-line)
--
-- Date: 04/02/2003
-- Version: 1.38
-- Nel controllo di cassa CDS il mandato e calcolato al netto delle sue ritenute
--
-- Date: 20/02/2003
-- Version: 2.0
-- Generazione e modifica della reversale provvisoria per versamenti CORI/IVA
--
-- Date: 21/02/2003
-- Version: 2.1
-- Fix su rev provv
--
-- Date: 20/05/2003
-- Version: 2.2
-- La reversale di liquidazione CORI locale collegata al mandato di liquidazione non pu? essere scollegata dallo stesso
--
-- Date: 22/05/2003
-- Version: 3.0
-- Aggiunto metodo di generazione ecollegamento di mandato secondario a mandato principale
--
-- Date: 01/07/2003
-- Version: 3.1
-- Aggiunto metodo vsx per il cambiamento degli impegni su mandato di accreditamento
--
-- Date: 16/07/2003
-- Version: 3.2
-- Fix pg_ver_rec da vsx sostituito con pg_ver_rec_obb_scad
-- Impostazione ti_competenza_residui in testata del mandato sulla base della sez dei conti mandato
--
-- Date: 08/09/2003
-- Version: 3.3
--
-- Eliminazione della routine di aggiornamento della data di esigibilit? IVA su fatture attive in
-- riscontro delle reversali
--
-- Date: 11/09/2003
-- Version: 3.4
-- Fix su metodo check reversali: l'importo ritenute del mandato non deve tenere conto di eventuali mandati
-- secondari collegati al mandato principale.
-- Aggiornamento documentazione
--
-- Date: 27/10/2003
-- Version: 3.5
-- Fix su generazione di dettaglio di sospeso: deve aggiornare il pg_ver_rec e system info
-- del sospeso padre
--
-- Date: 09/12/2003
-- Version: 3.6
-- Corretto messaggio all'utente nella Procedura GENERADETT_ETR_SOSPESO().
--
-- Date: 13/07/2006
-- Version: 3.7
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 25/05/2014
-- Version: 3.8
-- Adeguamenti relativi al Bonus DL 66/2014
-- corretta anomalia per gestire ASS_MANDATO_MANDATO anche con un solo mandato da associare
--
-- Constants:
--
cPKCONFIG CONSTANT VARCHAR2(50):='LIMITE_UTILIZZO_CONTANTI';
cSKCONFIG CONSTANT VARCHAR2(100):='LIMITE1';
-- Date: 22/04/2008
-- Version: 3.8
-- Gestione Sospensione netto nei compensi
-- Aggiunta alla procedura GENERADOCUMENTI la possibilit? di creare l'undicesima reversale
--
--
--type righeObblScadList is table of obbligazione_scadenzario%rowtype index by binary_integer;
 type righeCapitoliList is table of obbligazione_scad_voce%rowtype index by binary_integer;

-- Functions e Procedures:

 -- Funzioni di Controllo

 -- Controllo di disponibilit? cassa del CDS

 function CheckCassaCds(testa_mandato mandato%rowtype) return boolean;

 -- Controllo il saldo dei capitoli collegati a scadenze di obbligazioni collegate alle righe specificate di mandato

 function CheckSaldoCapitoli (lRigheMandato CNRCTB038.righeMandatoList) return boolean;

 -- restituisce se la linea ? sfondabile a competenza
 Function islineasfondabile (ALINEA linea_attivita%Rowtype) return boolean;

 -- restituisce se la natura ? sfondabile a competenza
 Function iselementovocesfondabile (AELVOCE elemento_voce%Rowtype) return boolean;

 -- Estrae una scadenza di obbligazione collegata a riga di mandato

 function getScadenzaObbligazione(sRigaMandato mandato_riga%rowtype) return obbligazione_scadenzario%rowtype;

 -- Estrae i capitoli di una data scadenza di obbligazione

 function getCapitoliDiScadObblig(sRigaScadObblig obbligazione_scadenzario%rowtype) return righeCapitoliList;

 -- Controllo che il saldo sul capitolo sia sufficiente per emettere il mandato

 --function SaldoCapitoloSufficente(sRigaCapitolo obbligazione_scad_voce%rowtype,im_distribuito number) return boolean;

 -- Aggiorna il saldo documenti autorizzatori di spesa sul capitolo

 procedure updSaldoCapitoliM (aMan mandato%rowtype, aAzione char, aTiImporto char);

 -- Aggiorna il saldo documenti autorizzatori di entrata sul capitolo

 procedure updSaldoCapitoliR (aRev reversale%rowtype, aAzione char, aTiImporto char);

 -- Aggiorna le obbligazioni collegato a Mandato generata

 procedure updObbligazioni(Mandato_Testata mandato%rowtype);

 -- Aggiorna l'accertamento collegato a Reversale generata

 procedure updAccertamento(Reversale_Testata reversale%rowtype);

 -- Aggiorna la scadenza di obbligazione collegato a Mandato generato

 procedure updScadObbligazione(MandatoTestata mandato%rowtype ) ;

 -- Aggiorna la scadenza di accertamento collegato a Reversale generata

 procedure updScadAccertamento(ReversaleTestata reversale%rowtype) ;

 -- Generazione di mandato semplice senza reversali

 procedure generaDocumento(
   aManPrncT in out mandato%rowtype,
   aManPrncR in out CNRCTB038.righeMandatoList
 );

 -- Genera e collega documento autorizzatorio a mandato aManP

 procedure GENERAECOLLEGADOC(
   aManP IN mandato%rowtype,
   aRev IN OUT reversale%rowtype,
   aListaRev IN OUT CNRCTB038.righeReversaleList
  );

 procedure GENERAECOLLEGADOC(
   aManP in mandato%rowtype,
   aMan in out mandato%rowtype,
   aListaMan in out CNRCTB038.righeMandatoList
 );

 -- Generazione di mandato con singola reversale collegata

 procedure generaDocumento(
   aManPrncT in out mandato%rowtype,
   aManPrncR in out CNRCTB038.righeMandatoList,
   aRevT in out reversale%rowtype,
   aRevR in out CNRCTB038.righeReversaleList
 );

 -- Generazione di mandato e reversali+mandati collegati (fino a 10)

 procedure generaDocumenti(
   aManPrncT in out mandato%rowtype,
   aManPrncR in out CNRCTB038.righeMandatoList,
   aRevColl01T in out reversale%rowtype,
   aRevColl01R in out CNRCTB038.righeReversaleList ,
   aRevColl02T in out reversale%rowtype ,
   aRevColl02R in out CNRCTB038.righeReversaleList ,
   aRevColl03T in out reversale%rowtype ,
   aRevColl03R in out CNRCTB038.righeReversaleList ,
   aRevColl04T in out reversale%rowtype ,
   aRevColl04R in out CNRCTB038.righeReversaleList ,
   aRevColl05T in out reversale%rowtype ,
   aRevColl05R in out CNRCTB038.righeReversaleList ,
   aRevColl06T in out reversale%rowtype ,
   aRevColl06R in out CNRCTB038.righeReversaleList ,
   aRevColl07T in out reversale%rowtype ,
   aRevColl07R in out CNRCTB038.righeReversaleList ,
   aRevColl08T in out reversale%rowtype ,
   aRevColl08R in out CNRCTB038.righeReversaleList ,
   aRevColl09T in out reversale%rowtype ,
   aRevColl09R in out CNRCTB038.righeReversaleList ,
   aRevColl10T in out reversale%rowtype ,
   aRevColl10R in out CNRCTB038.righeReversaleList ,
   aRevColl11T in out reversale%rowtype ,
   aRevColl11R in out CNRCTB038.righeReversaleList ,
   aManColl01T in out mandato%rowtype ,
   aManColl01R in out CNRCTB038.righeMandatoList ,
   aManColl02T in out mandato%rowtype ,
   aManColl02R in out CNRCTB038.righeMandatoList ,
   aManColl03T in out mandato%rowtype ,
   aManColl03R in out CNRCTB038.righeMandatoList ,
   aManColl04T in out mandato%rowtype ,
   aManColl04R in out CNRCTB038.righeMandatoList ,
   aManColl05T in out mandato%rowtype ,
   aManColl05R in out CNRCTB038.righeMandatoList ,
   aManColl06T in out mandato%rowtype ,
   aManColl06R in out CNRCTB038.righeMandatoList ,
   aManColl07T in out mandato%rowtype ,
   aManColl07R in out CNRCTB038.righeMandatoList ,
   aManColl08T in out mandato%rowtype ,
   aManColl08R in out CNRCTB038.righeMandatoList ,
   aManColl09T in out mandato%rowtype ,
   aManColl09R in out CNRCTB038.righeMandatoList ,
   aManColl10T in out mandato%rowtype ,
   aManColl10R in out CNRCTB038.righeMandatoList
 );

 -- Tale metodo deve essere chiamato all'interno di un metodo che aggiorni utuv,duva e pg_ver rec
 --
 -- Pre-post name: Azioni di riscontro di mandato di pagamento
 -- Pre condizione: Il mandato ? stato oggetto di pagamento che ha aggiornato l'importo pagato.
 -- L'importo attuale pagato corrisponde all'importo del mandato
 -- Post condizione:
 --  lo stato del mandato viene portato a P
 --  vengono aggiornati i saldi documenti autorizzatori
 --  viene inserita la data di pagamento in tabella saldi COFI
 --
 -- Pre-post name: Azioni di riscontro parziale di mandato di pagamento
 -- Pre condizione: Il mandato ? stato oggetto di pagamento che ha aggiornato l'importo pagato.
 -- L'importo attuale pagato non corrisponde all'importo del mandato
 -- Post condizione: Nessuna azione viene esguita
 --
 -- N.B. Tale procedura deve essere chiamato all'interno di un metodo che aggiorni utuv,duva e pg_ver rec
 --      La procedura non aggiorna tali parametri sulla tabella MANDATO
 --
 procedure riscontroMandato
   (
    aEs NUMBER,
    aCdCds VARCHAR2,
    aPgMan NUMBER,
    aAzione VARCHAR2,
    aUtente VARCHAR2
  );

 --
 -- Pre-post name: Azioni di riscontro reversaldi in incasso
 -- Pre condizione: La reversale ? stata oggetto di incasso che ha aggiornato l'importo incassato.
 -- Post condizione:
 --  lo stato della reversale viene portato a P
 --  vengono aggiornati i saldi di pagamento in tabella saldi COFI
 --  viene inserita la data di incasso del mandato
 --
 -- Pre-post name: Azioni di riscontro parziale della reversale di regolarizzazione
 -- Pre condizione: La reversale ? stata oggetto di incasso che ha aggiornato l'importo incassato.
 -- L'importo attuale pagato non corrisponde all'importo del mandato
 -- Post condizione
 -- Post condizione: Nessuna azione viene esguita
 --
 -- N.B. Tale procedura deve essere chiamato all'interno di un metodo che aggiorni utuv,duva e pg_ver rec
 --      La procedura non aggiorna tali parametri sulla tabella REVERSALE
 --
 procedure riscontroReversale
   (
    aEs NUMBER,
    aCdCds VARCHAR2,
    aPgRev NUMBER,
    aAzione VARCHAR2,
    aUtente VARCHAR2
   );

 --
 -- Verifica di scollegabilit? della reversale dal mandato a cui ? collegata
 --
 -- Pre-post name: La reversale specificata ? di liquidazione del compenso liquidato con il mandato specificato
 -- Pre condizione: La reversale ? collegata a CORI del compenso liquidato con il mandato specificato
 -- Post condizione: Viene sollevata un'eccezione
 --
 -- Pre-post name: La reversale specificata ? collegata a documento generico di versamento di entrata (GEN_VER_E)
 -- Pre condizione: La reversale ? collegata ? collegata a documento generico di versamento di entrata (GEN_VER_E)
 -- Post condizione: Viene sollevata un'eccezione
 --
 -- Parametri:
 --
 --  aCdCdsMan -> Codice cds mandato
 --  aEsMan -> Esercizio mandato
 --  aPgMan -> Progressivo mandato
 --  aCdCdsRev -> Codice cds reversale collegata
 --  aEsRev number -> Esercizio reversale
 --  aPgRev number -> Progressivo reversale
 --

 PROCEDURE checkRevDisassocMan
 (
  aCdCdsMan varchar2,
  aEsMan number,
  aPgMan number,
  aCdCdsRev varchar2,
  aEsRev number,
  aPgRev number
 );

 --
 -- Verifica e ricalcola l'importo ritenute del Mandato
 --
 -- Pre-post name: Mandato non trovato
 -- Pre condizione: Il mandato corrispondente ai parametri specificati non viene trovato
 -- Post condizione: Viene sollevata un'eccezione
 --
 -- Pre-post name: Controllo importo ritenute minore del totale mandato
 -- Pre condizione: Per il mandato specificato il totale ritenute (somma importi reversali collegate) ? maggiore
 --                 dell'importo del mandato
 -- Post condizione: Viene sollevata un'eccezione
 --
 -- Pre-post name: Calcolo importo ritenute mandato
 -- Pre condizione: Nessuna precondizione verificata
 -- Post condizione: La procedura ritorna il totale delle ritenute collegate al mandato specificato (solo reversali collegate)
 --
 -- Parametri:
 --
 -- inCdsMandato -> Cds del mandato
 -- inEsercizioMandato -> Esercizio del mandato
 -- inPgMandato -> Progressivo mandato
 -- inImRitenute -> Importo ritenute del mandato calcolato dalla procedura
 --
 PROCEDURE checkReversaliAssociate
   (
    inCdsMandato MANDATO.cd_cds%TYPE,
    inEsercizioMandato MANDATO.esercizio%TYPE,
    inPgMandato MANDATO.pg_mandato%TYPE,
    inImRitenute IN OUT NUMBER
 );

 -- Generazione di una reversale
 procedure GENERAREVERSALE(
   aRev IN OUT reversale%rowtype,
   aListaRev IN OUT CNRCTB038.righeReversaleList
  );

procedure GENERADETT_ETR_SOSPESO(
   aRev IN  reversale%rowtype,
   aSospeso IN OUT sospeso%rowtype,
   aUser varchar2
 );


 -- Crea un reversale provvisoria nel CDS aggiornando tutti i collegamenti con doc amministrativi e contabili
 procedure generaRevProvvPgiro(aRev IN OUT reversale%rowtype, aRevRiga IN OUT reversale_riga%rowtype, aTSNow date, aUser varchar2);

 -- Modifica di importo di reversale provvisoria (nuovo importo letto da letto da aRev.im_reversale)
 -- Tale metodo NON aggiorna sull'obbligazione la quota collegata a doc contabili
 -- Il metodo aggiorna oi saldi dei documenti autorizzatori
 procedure modificaRevProvvPgiro(aRev IN OUT reversale%rowtype, aTSNow date, aUser varchar2);


 -- La procedura si occupa, a partire della riga di mandato, di vedere se per essa ? disponibile un solo codice SIOPE,
 -- nel qual caso lo inserisce con l'importo totale della riga
 Procedure Inserisci_SIOPE_automatico (aNewManRiga In mandato_riga%Rowtype);

 -- La procedura si occupa, a partire della riga di reversale, di vedere se per essa ? disponibile un solo codice SIOPE,
 -- nel qual caso lo inserisce con l'importo totale della riga
 Procedure Inserisci_SIOPE_automatico (aNewRevRiga In reversale_riga%Rowtype);


 -- Modifica degli impegni di mandato di accreditamento
 procedure vsx_man_acc(
       aPgCall NUMBER
 );

 -- Verifica della tracciabilit? dei pagamenti: per pagamenti superiori ad un certo valore sono ammissibili solo
 -- alcune modalit? di pagamento (a seconda del documento amministrativo utilizzato)
 PROCEDURE verificaTracciabilitaPag (aEs In NUMBER, aDataEmis In DATE, aCdModPag In VARCHAR2, aCdTipoDocAmm In VARCHAR2, aImMandatoNetto In NUMBER);

end;
/


CREATE OR REPLACE package body CNRCTB037 is

-- =================================================
-- VSX di modifica impegni mandato di accreditamento
-- =================================================

 procedure vsx_man_acc(
       aPgCall NUMBER
 ) is
   aUser varchar2(20);
   aTSNow date;
   aCdCds varchar2(30);
   aEs number(4);
   aPgMan number(10);
   aObb obbligazione%rowtype;
   aObbN obbligazione%rowtype;
   aObbO obbligazione%rowtype;
   aObbScadN obbligazione_scadenzario%rowtype;
   aMan mandato%rowtype;
   aGenRiga documento_generico_riga%rowtype;
   aNewGenRiga documento_generico_riga%rowtype;
   aNumRigaGen number(10);
   aManRiga mandato_riga%rowtype;
   aNewManRiga mandato_riga%rowtype;
 begin
  for aVSX in (select distinct cd_cds,esercizio,pg_mandato,utuv,duva from vsx_man_acc where
                pg_call = aPgCall
        ) loop
   aCdCds:=aVSX.cd_cds;
   aEs:=aVSX.esercizio;
   aPgMan:=aVSX.pg_mandato;
   aTSNow:=aVSX.duva;
   aUser:=aVSX.utuv;
   exit;
  end loop;
  if
      aCdCds is null
   or aEs is null
   or aPgMan is null
   or aTSNow is null
   or aUser is null
  then
   IBMERR001.RAISE_ERR_GENERICO('Parametro VSX non definito');
  end if;
  begin
   select * into aMan from mandato where
          esercizio = aEs
      and cd_cds = aCdCds
      and pg_mandato = aPgMan
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Mandato non trovato');
  end;

  for aVSX in (select * from vsx_man_acc where
                pg_call = aPgCall
        ) loop
   aObbN:=null;
   aObbN.cd_cds:=aVSX.cd_cds;
   aObbN.esercizio:=aVSX.esercizio;
   aObbN.esercizio_originale:=aVSX.esercizio_ori_obbligazione;
   aObbN.pg_obbligazione:=aVSX.pg_obbligazione;
   CNRCTB035.LOCKDOCFULL(aObbN);

      -- Verifico che l'obbligazione non sia stata riportata nell'anno successivo
   If aObbN.riportato = 'Y' Then
    IBMERR001.RAISE_ERR_GENERICO('Modifica non possibile! L''impegno '||CNRCTB035.getDesc(aObbN)||' collegato al mandato risulta riportato ad anno successivo.');
   End If;

   -- Verifico che il pg_ver_rec delle scadenze non sia cambiato
   begin
    select * into aObbScadN from obbligazione_scadenzario where
       cd_cds = aObbN.cd_cds
   and esercizio = aObbN.esercizio
   and esercizio_originale = aObbN.esercizio_originale
   and pg_obbligazione = aObbN.pg_obbligazione
   and pg_obbligazione_scadenzario = aVSX.pg_obbligazione_scadenzario
     and pg_ver_rec = aVSX.pg_ver_rec_obb_scad;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Risorsa non pi? valida x');
   end;
   if aObbScadN.im_scadenza < aObbScadN.im_associato_doc_amm + aVSX.im_riga then
    IBMERR001.RAISE_ERR_GENERICO('Disponibilit? esaurita su impegno:'||CNRCTB035.getDesc(aObbN));
   end if;
  end loop;

  -- Aggiorno i saldi emesso in delta neg. per i vecchi cap.
--DBMS_Output.PUT_LINE ('M A');

  updSaldoCapitoliM(aMan, 'A', 'A');

  -- Se man pagato aggiorno i saldi pagato in delta neg. per i vecchi cap.
  if aMan.STATO = CNRCTB038.STATO_AUT_ESI then
--DBMS_Output.PUT_LINE ('M B');
   updSaldoCapitoliM(aMan, 'A', 'U');
  end if;

  -- Aggiorno nell'obbligazione il saldo autorizzatori
  for aTempManRiga in (select * from mandato_riga where
          esercizio = aMan.esercizio
      and cd_cds = aMan.cd_cds
      and pg_mandato = aMan.pg_mandato for update nowait) loop
   aObbO.cd_cds:=aTempManRiga.cd_cds;
   aObbO.esercizio:=aTempManRiga.esercizio_obbligazione;
   aObbO.esercizio_originale:=aTempManRiga.esercizio_ori_obbligazione;
   aObbO.pg_obbligazione:=aTempManRiga.pg_obbligazione;
   CNRCTB035.LOCKDOCFULL(aObbO);
      -- Verifico che le obbligazioni legate al mandato non siano stat riportata nell'anno successivo
   If aObbO.riportato = 'Y' Then
    IBMERR001.RAISE_ERR_GENERICO('Modifica non possibile! L''impegno '||CNRCTB035.getDesc(aObbO)||' collegato al mandato risulta riportato ad anno successivo.');
   End If;

   update obbligazione_scadenzario set
         im_associato_doc_contabile = im_associato_doc_contabile - aTempManRiga.im_mandato_riga,
         im_associato_doc_amm = im_associato_doc_amm - aTempManRiga.im_mandato_riga,
     utuv=aUser,
     duva=aTSNow,
         pg_ver_rec=pg_ver_rec+1
   where
       cd_cds = aObbO.cd_cds
   and esercizio = aObbO.esercizio
   and esercizio_originale = aObbO.esercizio_originale
   and pg_obbligazione = aObbO.pg_obbligazione
   and pg_obbligazione_scadenzario = aTempManRiga.pg_obbligazione_scadenzario;
  end loop;

  -- Salvo la prima riga del mandato da usare come template
  for aTempManRiga in (select * from mandato_riga where
          esercizio = aMan.esercizio
      and cd_cds = aMan.cd_cds
      and pg_mandato = aMan.pg_mandato) loop
   aManRiga:=aTempManRiga;
   exit;
  end loop;

  delete from mandato_siope where
          esercizio = aMan.esercizio
      and cd_cds = aMan.cd_cds
      and pg_mandato = aMan.pg_mandato;

  delete from mandato_riga where
          esercizio = aMan.esercizio
      and cd_cds = aMan.cd_cds
      and pg_mandato = aMan.pg_mandato;

  CNRCTB100.LOCKDOCAMM(aManRiga.cd_tipo_documento_amm,aManRiga.cd_cds_doc_amm,aManRiga.esercizio,aManRiga.cd_uo_doc_amm,aManRiga.pg_doc_amm);
  -- Salvo la prima riga del mandato da usare come template
  for aTempGenRiga in (select * from documento_generico_riga where
              cd_tipo_documento_amm = aManRiga.cd_tipo_documento_amm
          and esercizio = aManRiga.esercizio
      and cd_cds = aManRiga.cd_cds_doc_amm
      and esercizio = aManRiga.esercizio_doc_amm
          and cd_unita_organizzativa = aManRiga.cd_uo_doc_amm
      and pg_documento_generico = aManRiga.pg_doc_amm
  for update nowait
  ) loop
   aGenRiga:=aTempGenRiga;
   exit;
  end loop;

  delete from documento_generico_riga where
              cd_tipo_documento_amm = aManRiga.cd_tipo_documento_amm
          and esercizio = aManRiga.esercizio
      and cd_cds = aManRiga.cd_cds_doc_amm
      and esercizio = aManRiga.esercizio_doc_amm
          and cd_unita_organizzativa = aManRiga.cd_uo_doc_amm
      and pg_documento_generico = aManRiga.pg_doc_amm;
  aNumRigaGen:=0;
  for aVSX in (select * from vsx_man_acc where
                pg_call = aPgCall
        ) loop
   aObbN:=null;
   aObbN.cd_cds:=aVSX.cd_cds;
   aObbN.esercizio:=aVSX.esercizio;
   aObbN.esercizio_originale:=aVSX.esercizio_ori_obbligazione;
   aObbN.pg_obbligazione:=aVSX.pg_obbligazione;
   CNRCTB035.LOCKDOCFULL(aObbN);
      -- Verifico che le obbligazioni legate al mandato non siano stat riportata nell'anno successivo
   If aObbN.riportato = 'Y' Then
    IBMERR001.RAISE_ERR_GENERICO('Modifica non possibile! L''impegno '||CNRCTB035.getDesc(aObbN)||' collegato al mandato risulta riportato ad anno successivo.');
   End If;
   -- Verifico che il pg_ver_rec delle scadenze non sia cambiato
   begin
    select * into aObbScadN from obbligazione_scadenzario where
       cd_cds = aObbN.cd_cds
   and esercizio = aObbN.esercizio
   and esercizio_originale = aObbN.esercizio_originale
   and pg_obbligazione = aObbN.pg_obbligazione
   and pg_obbligazione_scadenzario = aVSX.pg_obbligazione_scadenzario;
--   and pg_ver_rec = aVSX.pg_ver_rec_obb_scad;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Risorsa non pi? valida x');
   end;
   -- Ricreo le righe del generico
   aNewGenRiga:=aGenRiga;
   aNumRigaGen:=aNumRigaGen+1;
   aNewGenRiga.progressivo_riga := aNumRigaGen;
   aNewGenRiga.cd_cds_obbligazione := aObbScadN.cd_cds;
   aNewGenRiga.esercizio_obbligazione := aObbScadN.esercizio;
   aNewGenRiga.esercizio_ori_obbligazione := aObbScadN.esercizio_originale;
   aNewGenRiga.pg_obbligazione := aObbScadN.pg_obbligazione;
   aNewGenRiga.pg_obbligazione_scadenzario := aObbScadN.pg_obbligazione_scadenzario;
   aNewGenRiga.im_riga := aVSX.im_riga;
   aNewGenRiga.duva := aVSX.duva;
   aNewGenRiga.utuv := aVSX.utuv;
   aNewGenRiga.pg_ver_rec := 1;
   CNRCTB100.ins_documento_generico_riga(aNewGenRiga);
   -- Ricreo le righe del mandato
   aNewManRiga:=aManRiga;
   aNewManRiga.cd_cds := aObbScadN.cd_cds;
   aNewManRiga.esercizio_obbligazione := aObbScadN.esercizio;
   aNewManRiga.esercizio_ori_obbligazione := aObbScadN.esercizio_originale;
   aNewManRiga.pg_obbligazione := aObbScadN.pg_obbligazione;
   aNewManRiga.pg_obbligazione_scadenzario := aObbScadN.pg_obbligazione_scadenzario;
   aNewManRiga.im_mandato_riga := aVSX.im_riga;
   aNewManRiga.duva := aVSX.duva;
   aNewManRiga.utuv := aVSX.utuv;
   aNewManRiga.pg_ver_rec := 1;
   CNRCTB038.ins_mandato_riga(aNewManRiga);

   -- 23.04.2007 Stanislao Fusco
   -- Inserita procedura che pu? inserire la riga automatica SIOPE
   Inserisci_SIOPE_automatico (aNewManRiga);

   update obbligazione_scadenzario set
         im_associato_doc_contabile = im_associato_doc_contabile + aVSX.im_riga,
         im_associato_doc_amm = im_associato_doc_amm + aVSX.im_riga,
     utuv=aUser,
     duva=aTSNow,
         pg_ver_rec=pg_ver_rec+1
   where
       cd_cds = aObbN.cd_cds
   and esercizio = aObbN.esercizio
   and esercizio_originale = aObbN.esercizio_originale
   and pg_obbligazione = aObbN.pg_obbligazione
   and pg_obbligazione_scadenzario = aVSX.pg_obbligazione_scadenzario;
  end loop;
  if aObbN.pg_obbligazione is null Or aObbN.esercizio_originale Is Null Then
   IBMERR001.RAISE_ERR_GENERICO('Errore interno');
  end if;
  -- Aggiorno il tipo competenza/residui del mandato
  if aMan.ti_competenza_residuo = CNRCTB038.TI_MAN_COMP and aObbN.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES then
   update mandato
   set
    ti_competenza_residuo=CNRCTB038.TI_MAN_RES,
    utuv=aUser,
  duva=aTSNow,
  pg_ver_rec=pg_ver_rec+1
   where
              esercizio = aMan.esercizio
          and cd_cds = aMan.cd_cds
      and pg_mandato = aMan.pg_mandato;
  elsif aMan.ti_competenza_residuo = CNRCTB038.TI_MAN_RES and aObbN.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP then
   update mandato
   set
    ti_competenza_residuo=CNRCTB038.TI_MAN_COMP,
    utuv=aUser,
  duva=aTSNow,
  pg_ver_rec=pg_ver_rec+1
   where
              esercizio = aMan.esercizio
          and cd_cds = aMan.cd_cds
      and pg_mandato = aMan.pg_mandato;
  else
   update mandato
   set
    utuv=aUser,
  duva=aTSNow,
  pg_ver_rec=pg_ver_rec+1
   where
              esercizio = aMan.esercizio
          and cd_cds = aMan.cd_cds
      and pg_mandato = aMan.pg_mandato;
  end if;
  -- Aggiorno i saldi emesso in delta neg. per i vecchi cap.
--DBMS_Output.PUT_LINE ('M C');
  updSaldoCapitoliM(aMan, 'I', 'A');
  -- Se man pagato aggiorno i saldi pagato in delta neg. per i vecchi cap.
  if aMan.STATO = CNRCTB038.STATO_AUT_ESI then
--DBMS_Output.PUT_LINE ('M D');
   updSaldoCapitoliM(aMan, 'I', 'U');
  end if;

 end;

-- =================================================================================================
-- Aggiornamento dello stato pagato su mandati
-- =================================================================================================
PROCEDURE riscontroMandato
   (aEs NUMBER,
    aCdCds VARCHAR2,
    aPgMan NUMBER,
    aAzione VARCHAR2,
    aUtente VARCHAR2) IS
   aMan mandato%rowtype;

Begin

  select * into aMan
  from   mandato
  where  esercizio = aEs And
         cd_cds = aCdCds And
         pg_mandato = aPgMan
  for update nowait;

  if aAzione = 'I' and aMan.im_mandato <> aMan.im_pagato then
   return;
  end if;

  If aAzione <> 'A' Then

   If aAzione = 'M' And aMan.im_mandato <> aMan.im_pagato Then

     Update mandato
     Set    stato = CNRCTB038.STATO_MAN_EME,
            dt_pagamento = null,
            duva = aMan.duva,
            utuv = aMan.utuv,
      pg_ver_rec = pg_ver_rec+1
     Where  esercizio = aEs And
            cd_cds = aCdCds And
            pg_mandato = aPgMan;

   Else

    Update Mandato
    Set    stato = CNRCTB038.STATO_MAN_PAG,
           dt_pagamento = Nvl((Select Max(Trunc(SOSPESO.DT_REGISTRAZIONE))
                               From   SOSPESO, SOSPESO_DET_USC
                               Where  SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO = 'R' And
                                      SOSPESO_DET_USC.STATO        != 'A' And
                                      SOSPESO_DET_USC.CD_CDS       = aCdCds And
                                      SOSPESO_DET_USC.ESERCIZIO    = aEs And
                                      SOSPESO_DET_USC.PG_MANDATO   = aPgMan And
                                      SOSPESO.CD_CDS               = SOSPESO_DET_USC.CD_CDS And
                                      SOSPESO.ESERCIZIO            = SOSPESO_DET_USC.ESERCIZIO And
                                      SOSPESO.TI_ENTRATA_SPESA     = SOSPESO_DET_USC.TI_ENTRATA_SPESA And
                                      SOSPESO.TI_SOSPESO_RISCONTRO = SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO And
                                      SOSPESO.CD_SOSPESO           = SOSPESO_DET_USC.CD_SOSPESO), Trunc(aMan.duva)),
           duva = aMan.duva,
           utuv = aMan.utuv,
           pg_ver_rec = pg_ver_rec + 1
    Where esercizio = aEs And
          cd_cds = aCdCds And
          pg_mandato = aPgMan;

-- SF 13.09.2007 INSERITO PER GESTIRE LO STATO DELLA RIGA, COMPLETAMENTE TRASCURATO
    Update Mandato_RIGA
    Set    stato = CNRCTB038.STATO_MAN_PAG,
           duva = aMan.duva,
           utuv = aMan.utuv,
           pg_ver_rec = pg_ver_rec + 1
    Where esercizio = aEs And
          cd_cds = aCdCds And
          pg_mandato = aPgMan;

   End If;

  End If; -- AZIONE DIVERSA DA "A"

  If aAzione = 'A' Or (aAzione = 'M' And aMan.im_mandato <> aMan.im_pagato) Then
   updSaldoCapitoliM (aMan,'A','U');
  Else
   updSaldoCapitoliM (aMan,'I','U');
  End if;

End;

-- =================================================================================================
-- Aggiornamento dello stato pagato su reversali
-- =================================================================================================
 procedure riscontroReversale
   (
    aEs NUMBER,
    aCdCds VARCHAR2,
    aPgRev NUMBER,
    aAzione VARCHAR2,
    aUtente VARCHAR2
   ) IS

 aRev reversale%rowtype;
 aFatturaAttiva fattura_attiva%rowtype;
 begin
  select * into aRev from reversale where
       esercizio = aEs
   and cd_cds = aCdCds
   and pg_reversale = aPgRev
  for update nowait;
  if aAzione = 'I' and aRev.im_reversale <> aRev.im_incassato then
   return;
  end if;
  if aAzione <> 'A' then -- Attualmente l'annullamento gestisce il mandato dall'on-line
   if aAzione = 'M' and aRev.im_reversale <> aRev.im_incassato then
    update reversale set
     stato = CNRCTB038.STATO_REV_EME,
     dt_incasso = null,
     duva = aRev.duva,
     utuv = aRev.utuv,
  pg_ver_rec=pg_ver_rec+1
    where
         esercizio = aEs
     and cd_cds = aCdCds
     and pg_reversale = aPgRev;
   else
     Update reversale
     Set    stato = CNRCTB038.STATO_REV_PAG,
            dt_incasso = Nvl((Select Max(Trunc(SOSPESO.DT_REGISTRAZIONE))
                              From   SOSPESO, SOSPESO_DET_ETR
                              Where  SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO = 'R' And
                                     SOSPESO_DET_ETR.STATO        != 'A' And
                                     SOSPESO_DET_ETR.CD_CDS       = aCdCds And
                                     SOSPESO_DET_ETR.ESERCIZIO    = aEs And
                                     SOSPESO_DET_ETR.PG_REVERSALE = aPgRev And
                                     SOSPESO.CD_CDS               = SOSPESO_DET_ETR.CD_CDS And
                                     SOSPESO.ESERCIZIO            = SOSPESO_DET_ETR.ESERCIZIO And
                                     SOSPESO.TI_ENTRATA_SPESA     = SOSPESO_DET_ETR.TI_ENTRATA_SPESA And
                                     SOSPESO.TI_SOSPESO_RISCONTRO = SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO And
                                     SOSPESO.CD_SOSPESO           = SOSPESO_DET_ETR.CD_SOSPESO), Trunc(aRev.duva)),
            duva = aRev.duva,
            utuv = aRev.utuv,
      pg_ver_rec = pg_ver_rec+1
     Where esercizio = aEs And
           cd_cds = aCdCds And
           pg_reversale = aPgRev;

-- SF 13.09.2007 INSERITO PER GESTIRE LO STATO DELLA RIGA, COMPLETAMENTE TRASCURATO
    Update REVERSALE_RIGA
    Set    stato = CNRCTB038.STATO_REV_PAG,
           duva = aRev.duva,
           utuv = aRev.utuv,
           pg_ver_rec = pg_ver_rec + 1
    Where esercizio = aEs And
           cd_cds = aCdCds And
           pg_reversale = aPgRev;

   end if;
  end if;
  if aAzione = 'A'  or (aAzione = 'M' and aRev.im_reversale <> aRev.im_incassato) then
--DBMS_Output.PUT_LINE ('R A');
   updSaldoCapitoliR (aRev,'A','U');
  else
--DBMS_Output.PUT_LINE ('R B');
   updSaldoCapitoliR (aRev,'I','U');
  end if;

  -- Aggiornamento della data_esigibilit?_differita su fattura attiva
  --
  -- funzione eliminata
  --
  -- if aAzione <> 'A' then
  -- for aRiga in (select * from reversale_riga where
  --      esercizio = aEs
  --  and cd_cds = aCdCds
  --  and pg_reversale = aPgRev
  -- ) loop
  --  if aRiga.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_ATTIVA then
  --     select * into aFatturaAttiva from fattura_attiva where
  --          cd_cds = aRiga.cd_cds_doc_amm
  --      and esercizio = aRiga.esercizio_doc_amm
  --    and cd_unita_organizzativa = aRiga.cd_uo_doc_amm
  --    and pg_fattura_attiva = aRiga.pg_doc_amm
  --      for update nowait;
  --      CNRCTB100.UPDATEDOCAMM(
  --        aRiga.cd_tipo_documento_amm,
  --        aFatturaAttiva.cd_cds,
  --        aFatturaAttiva.esercizio,
  --        aFatturaAttiva.cd_unita_organizzativa,
  --        aFatturaAttiva.pg_fattura_attiva,
  --        'data_esigibilita_iva = '||IBMUTL001.ASDYNDATE(aRev.duva),
  --      null,
  --      aRev.utuv,
  --      aRev.duva
  --     );
  --  end if;
  -- end loop;
  -- end if;
 end;

 procedure generaDocumento(
   aManPrncT in out mandato%rowtype,
   aManPrncR in out CNRCTB038.righeMandatoList,
   aRevT in out reversale%rowtype,
   aRevR in out CNRCTB038.righeReversaleList
 ) is begin
  generaDocumenti(
   aManPrncT,
   aManPrncR,
   aRevT,
   aRevR,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA
  );
 end;


 procedure generaDocumento(
   aManPrncT in out mandato%rowtype,
   aManPrncR in out CNRCTB038.righeMandatoList
 ) is begin
  generaDocumenti(
   aManPrncT,
   aManPrncR,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.REV_VUOTA,
   CNRCTB038.REV_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA,
   CNRCTB038.MAN_VUOTO,
   CNRCTB038.MAN_RIGHE_VUOTA
  );
 end;

 function CheckCassaCds(testa_mandato mandato%rowtype) return boolean is
 gen_riga v_disp_cassa_cds%rowtype;
 scarto number;
 begin
    select *
    into gen_riga
    from v_disp_cassa_cds
    where esercizio = testa_mandato.esercizio
    and   cd_cds    = testa_mandato.cd_cds;
 -- trattamento errore
    scarto :=  nvl(gen_riga.im_DIsponibilita_cassa,0)
   --17/03/2006 Raffaele Pagano: richiesta di eliminare l'aggiornamento della disponibilit? di cassa
   --all'atto dell'invio dei mandati di trasferimento
--              +nvl(gen_riga.im_accreditamenti_ic,0)
        -- Computa nel calcolo il mandato in processo al netto delle sue ritenute
        -nvl(testa_mandato.im_mandato,0)
        +nvl(testa_mandato.im_ritenute,0);
    if scarto <0 then
       return False;
    else
       return True;
    end if;
 exception when no_data_found then
    return False;
 end;

 function CheckSaldoCapitoli (lRigheMandato CNRCTB038.righeMandatoList) return boolean is
 i number;
 j number;
 scarto number(15,2);
 im_distribuito number(15,2);
 denominatore number(15,2);
 im_residuo number(15,2);
 peso number;

 controllo_logico boolean;
 num_righe number;
 desc_errore varchar2(2000);

 numeroCapitoli number;
 sRigaScadObblig obbligazione_scadenzario%rowtype;
 lRigheCapitoli righeCapitoliList;
 begin
   /*i:=1;
   lRigheCapitoli.DELETE;

   num_righe :=lRigheMandato.count;

   for i in 1..num_righe loop
    -- controllo effettuato solamente su righe di partita di giro
    if lRigheMandato(i).fl_pgiro = 'N' then
     -- per ogni riga del mandato si recupera la scadenza dell'obligazione associata
     sRigaScadObblig := getScadenzaObbligazione(lRigheMandato(i));
       if sRigaScadObblig.cd_cds is null then
       desc_errore := 'associazione con scadenza '||cnrutil.getLabelObbligazioneMin()||' non trovata';
       IBMERR001.RAISE_ERR_GENERICO(desc_errore);
     end if;

     -- per la scadenza di cui al punto precedente, si recupera il capitolo associato
     lRigheCapitoli := getCapitoliDiScadObblig(sRigaScadObblig);
       if lRigheCapitoli.Count = 0 then
       desc_errore := 'associazione con Capitolo non trovata';
       IBMERR001.RAISE_ERR_GENERICO(desc_errore);
     end if;
     -- Calcolare come l'importo della riga del mandato viene distribuita su i vari capitoli
     scarto := 0;
     numeroCapitoli := lRigheCapitoli.Count;

     if numeroCapitoli >1 then
       for j in 1..numeroCapitoli loop
           if j<numeroCapitoli then
             if sRigaScadObblig.IM_SCADENZA = 0 then
               peso := 0;
           else
               peso :=  lRigheCapitoli(j).IM_VOCE/sRigaScadObblig.IM_SCADENZA;
           end if;
           im_distribuito := peso * lRigheMandato(i).IM_MANDATO_RIGA;
           controllo_logico := not SaldoCapitoloSufficente(lRigheCapitoli(j),im_distribuito);
           scarto := scarto + im_distribuito;
           else
             im_residuo := lRigheMandato(i).IM_MANDATO_RIGA - scarto;
             controllo_logico := not SaldoCapitoloSufficente(lRigheCapitoli(numeroCapitoli),im_residuo);
         end if;
           if controllo_logico then
              -- il saldo del capitolo risulta minore alla porzione di importo
            return False;
         end if;
       end loop;
     else
        -- numeroCapitoli =1
        im_distribuito := sRigaScadObblig.IM_SCADENZA;
      if (not SaldoCapitoloSufficente(lRigheCapitoli(1),im_distribuito)) then
        -- il saldo del capitolo risulta minore alla porzione di importo
      return FALSE;
      end if;
     end if;
     end if;
   end loop;*/

   return True;

 end;

 Function islineasfondabile (ALINEA linea_attivita%Rowtype) return Boolean Is
 conta_sfondabile NUMBER;
 Begin
   Select Count(0)
   Into   conta_sfondabile
   From   linea_attivita
   Where  cd_centro_responsabilita = alinea.cd_centro_responsabilita And
          cd_linea_attivita= alinea.cd_natura And
          FL_LIMITE_ASS_OBBLIG = 'N';  -- N = NO CONTROLLO DISPONIBILITA, QUINDI SFONDABILE
   If conta_sfondabile = 0 Then
     Return False;
   Else
     Return True;
   End If;
 End;

 Function iselementovocesfondabile (AELVOCE elemento_voce%Rowtype) return Boolean Is
 conta_sfondabile NUMBER;
 Begin
   Select Count(0)
   Into   conta_sfondabile
   From   elemento_voce
   Where  ESERCIZIO         = AELVOCE.ESERCIZIO         AND
          TI_APPARTENENZA   = AELVOCE.TI_APPARTENENZA   AND
          TI_GESTIONE       = AELVOCE.TI_GESTIONE       AND
          CD_ELEMENTO_VOCE  = AELVOCE.CD_ELEMENTO_VOCE  AND
          FL_LIMITE_ASS_OBBLIG = 'N';  -- N = NO CONTROLLO DISPONIBILITA, QUINDI SFONDABILE
   If conta_sfondabile = 0 Then
     Return False;
   Else
     Return True;
   End If;
 End;

 Function getScadenzaObbligazione(sRigaMandato mandato_riga%rowtype) return obbligazione_scadenzario%rowtype is
 sObbligazione obbligazione%rowtype;
 sScadObblig obbligazione_scadenzario%rowtype;
 begin
    select *
    into sObbligazione
    from obbligazione
    where cd_cds = sRigaMandato.CD_CDS
    and   esercizio = sRigaMandato.ESERCIZIO_OBBLIGAZIONE
    and   esercizio_originale = sRigaMandato.ESERCIZIO_ORI_OBBLIGAZIONE
    and   pg_obbligazione = sRigaMandato.PG_OBBLIGAZIONE for update nowait;

    select *
    into sScadObblig
    from obbligazione_scadenzario
    where cd_cds = sRigaMandato.CD_CDS
    and   esercizio = sRigaMandato.ESERCIZIO_OBBLIGAZIONE
    and   esercizio_originale = sRigaMandato.ESERCIZIO_ORI_OBBLIGAZIONE
    and   pg_obbligazione = sRigaMandato.PG_OBBLIGAZIONE
    and   pg_obbligazione_scadenzario = sRigaMandato.PG_OBBLIGAZIONE_SCADENZARIO for update nowait;
    return sScadObblig;
 exception when no_data_found then
       return sScadObblig;
 end;

 function getCapitoliDiScadObblig(sRigaScadObblig obbligazione_scadenzario%rowtype) return righeCapitoliList is
 lRigheCapitolo righeCapitoliList;
 rigaCapitolo obbligazione_scad_voce%rowtype;
 cur_gen CNRCTB038.GenericCurTyp;
 num_riga number;
 begin
    num_riga :=0;
    open cur_gen for
      select *
      from obbligazione_scad_voce
      where cd_cds = sRigaScadObblig.CD_CDS
      and   esercizio = sRigaScadObblig.ESERCIZIO
      and   esercizio_originale = sRigaScadObblig.ESERCIZIO_ORIGINALE
      and   pg_obbligazione = sRigaScadObblig.PG_OBBLIGAZIONE
      and   pg_obbligazione_scadenzario = sRigaScadObblig.PG_OBBLIGAZIONE_SCADENZARIO for update nowait;
    loop
      FETCH cur_gen INTO rigaCapitolo;
          num_riga := num_riga +1;
        lRigheCapitolo(num_riga) :=rigaCapitolo;
      EXIT WHEN cur_gen%NOTFOUND;
    end loop;

    return lRigheCapitolo;
 exception when no_data_found then
       return lRigheCapitolo;
 end;

/*
 function SaldoCapitoloSufficente(sRigaCapitolo obbligazione_scad_voce%rowtype,im_distribuito number) return boolean is
 saldoCapitolo number(15,2);

 begin
    select c.IM_STANZ_INIZIALE_A1 + c.VARIAZIONI_PIU - c.VARIAZIONI_MENO - c.IM_MANDATI_REVERSALI
    into saldoCapitolo
    from voce_f_saldi_cmp c
    where c.cd_cds = sRigaCapitolo.CD_CDS
    and   c.esercizio = sRigaCapitolo.ESERCIZIO
    and   c.ti_appartenenza = sRigaCapitolo.TI_APPARTENENZA
    and   c.ti_gestione = sRigaCapitolo.TI_GESTIONE
    and   c.cd_voce = sRigaCapitolo.CD_VOCE for update nowait;

    if (saldoCapitolo - im_distribuito)>=0 then
     return TRUE;
    else
       return FALSE;
    end if;
 exception when no_data_found then
       return false;
 end;
*/

 procedure updSaldoCapitoli(aEs number, aCdCds varchar2, aPgDoc number, aTiDoc char, aAzione char, aTiImporto char, aUser varchar2, aTSNow date) is
  aTotDistr number(15,2);
  aSaldo voce_f_saldi_cmp%rowtype;
  aLastSaldo voce_f_saldi_cmp%rowtype;
  aLastCap v_mandato_reversale_voce%rowtype;
  aDelta number(15,2);
  aImManAlgebrico number(15,2);
  aSaldoCdrLinea voce_f_saldi_cdr_linea%Rowtype;
  parametriCnr parametri_cnr%Rowtype;
 begin
  aTotDistr:=0;
  aLastCap:=null;
  aLastSaldo:=null;
  aImManAlgebrico:=0;
  if(parametriCnr.fl_nuovo_pdg='N') then
    for aListaCap in (select * from v_mandato_reversale_voce where
                            esercizio = aEs
              and cd_cds = aCdCds
              and pg_documento = aPgDoc
              and ti_documento = aTiDoc
             ) loop
     begin
      select * into aSaldo from voce_f_saldi_cmp where
           CD_CDS=aListaCap.cd_cds
       and ESERCIZIO=aListaCap.esercizio
       and TI_APPARTENENZA=aListaCap.ti_appartenenza
       and TI_GESTIONE=aListaCap.ti_gestione
       and CD_VOCE=aListaCap.cd_voce
       and TI_COMPETENZA_RESIDUO=Decode(aListaCap.ti_appartenenza, CNRCTB001.APPARTENENZA_CDS, CNRCTB054.TI_COMPETENZA, aListaCap.ti_competenza_residuo)
      for update nowait;
     exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('Capitolo finanziario non trovato:'||aListaCap.cd_voce);
     end;

     if aAzione = 'A' then
      aDelta:=0-aListaCap.im_capitolo_pesato;
      aImManAlgebrico:=0-aListaCap.im_documento;
     else
      aDelta:=aListaCap.im_capitolo_pesato;
      aImManAlgebrico:=aListaCap.im_documento;
     end if;

     aTotDistr:=aTotDistr+aDelta;
     CNRCTB054.aggiornaSaldi(aSaldo,aTiImporto,aDelta,aUser,aTSNow);
     aLastSaldo:=aSaldo;
     aLastCap:=aListaCap;
    end loop;
    if aLastCap.cd_voce is not null and aImManAlgebrico-aTotDistr <> 0 Then

     -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
     CNRCTB054.aggiornaSaldi(aSaldo,aTiImporto,aImManAlgebrico-aTotDistr,aUser,aTSNow);
    end if;
  end if;
  for aListaScad in (select * from v_mandato_reversale_scad_voce
                     Where esercizio = aEs
           And cd_cds = aCdCds
           And pg_documento = aPgDoc
           And ti_documento = aTiDoc) loop
   Begin
    -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
    aSaldoCdrLinea.ESERCIZIO := aListaScad.ESERCIZIO;
    aSaldoCdrLinea.ESERCIZIO_RES := aListaScad.ESERCIZIO_ORIGINALE;
    aSaldoCdrLinea.CD_CENTRO_RESPONSABILITA := aListaScad.CD_CENTRO_RESPONSABILITA;
    aSaldoCdrLinea.CD_LINEA_ATTIVITA := aListaScad.CD_LINEA_ATTIVITA;
    aSaldoCdrLinea.TI_APPARTENENZA := aListaScad.TI_APPARTENENZA;
    aSaldoCdrLinea.TI_GESTIONE := aListaScad.TI_GESTIONE;
    aSaldoCdrLinea.CD_VOCE := aListaScad.CD_VOCE;

    CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLinea);

If aTiImporto = 'A' Then
    If aListaScad.CD_TIPO_DOCUMENTO_CONT In (CNRCTB018.TI_DOC_OBB, CNRCTB018.TI_DOC_OBB_RES_PRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES,
                                             CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_IMP, CNRCTB018.TI_DOC_IMP_RES,
                                             CNRCTB018.TI_DOC_ACC_RES, CNRCTB018.TI_DOC_ACC_PGIRO, CNRCTB018.TI_DOC_ACC_PGIRO_RES,
                                             CNRCTB018.TI_DOC_ACC_SIST, CNRCTB018.TI_DOC_ACC) Then
        If aAzione = 'A' Then
          aSaldocdrlinea.IM_MANDATI_REVERSALI_PRO := -aListaScad.IM_VOCE;
        Else
          aSaldocdrlinea.IM_MANDATI_REVERSALI_PRO := aListaScad.IM_VOCE;
        End If;
    Elsif aListaScad.CD_TIPO_DOCUMENTO_CONT = CNRCTB018.TI_DOC_OBB_RES_IMPRO Then

        If aAzione = 'A' Then
          aSaldocdrlinea.IM_MANDATI_REVERSALI_IMP := -aListaScad.IM_VOCE;
        Else
          aSaldocdrlinea.IM_MANDATI_REVERSALI_IMP := aListaScad.IM_VOCE;
        End If;


    End If;

Elsif aTiImporto = 'U' Then

        If aAzione = 'A' Then
          aSaldocdrlinea.IM_PAGAMENTI_INCASSI := -aListaScad.IM_VOCE;
        Else
          aSaldocdrlinea.IM_PAGAMENTI_INCASSI := aListaScad.IM_VOCE;
        End If;
End If;

    aSaldoCdrLinea.UTUV := aUser;

    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLinea, '037.updSaldoCapitoli', 'N');
   End;
   End Loop;
 end;

 procedure updSaldoCapitoliM(aMan mandato%rowtype, aAzione char, aTiImporto char) is
 begin
  updSaldoCapitoli(aMan.esercizio, aMan.cd_cds, aMan.pg_mandato, 'M', aAzione, aTiImporto, aMan.utuv, aMan.duva);
 end;

 procedure updSaldoCapitoliR (aRev reversale%rowtype, aAzione char, aTiImporto char) is
 begin
  updSaldoCapitoli(aRev.esercizio, aRev.cd_cds, aRev.pg_reversale, 'R', aAzione, aTiImporto, aRev.utuv, aRev.duva);
 end;

 procedure updObbligazioni(Mandato_Testata mandato%rowtype)  is

 begin
    update obbligazione
    set duva = Mandato_Testata.duva,
        utuv = Mandato_Testata.utuv,
      pg_ver_rec = pg_ver_rec + 1
    where (cd_cds, esercizio, esercizio_originale, pg_obbligazione ) in
        (select cd_cds, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione
       from mandato_riga
       where cd_cds    =  Mandato_Testata.CD_CDS
       and   esercizio = Mandato_Testata.ESERCIZIO
       and pg_mandato  =  Mandato_Testata.PG_MANDATO
       group by cd_cds, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione );
 end;

 procedure updAccertamento(Reversale_Testata reversale%rowtype)  is

 begin
    update accertamento
    set duva = Reversale_Testata.duva,
        utuv = Reversale_Testata.utuv,
      pg_ver_rec = pg_ver_rec + 1
    where (cd_cds, esercizio, esercizio_originale, pg_accertamento ) in
        (select cd_cds, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento
       from reversale_riga
       where cd_cds    =  Reversale_Testata.CD_CDS
       and   esercizio = Reversale_Testata.ESERCIZIO
       and pg_reversale  =  Reversale_Testata.PG_REVERSALE
       group by cd_cds, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento );
 end;

 procedure updScadObbligazione(MandatoTestata mandato%rowtype) is
 begin
    updObbligazioni(MandatoTestata);


    for sRigaVMandatoObblScad in (select * from
       v_mandato_obblScad
      where cd_cds =  MandatoTestata.CD_CDS
      and   esercizio = MandatoTestata.ESERCIZIO
      and   pg_mandato = MandatoTestata.PG_MANDATO
      ) loop
          update obbligazione_scadenzario
          set  IM_ASSOCIATO_DOC_CONTABILE = IM_ASSOCIATO_DOC_CONTABILE + sRigaVMandatoObblScad.TOT_RIGA_SU_SCAD,
           DUVA = MandatoTestata.DUVA,
           UTUV = MandatoTestata.UTUV,
           PG_VER_REC = PG_VER_REC +1
        where cd_cds =  sRigaVMandatoObblScad.CD_CDS
        and   esercizio = sRigaVMandatoObblScad.ESERCIZIO_OBBLIGAZIONE
        and   esercizio_originale = sRigaVMandatoObblScad.ESERCIZIO_ORI_OBBLIGAZIONE
        and   pg_obbligazione = sRigaVMandatoObblScad.PG_OBBLIGAZIONE
        and   pg_obbligazione_scadenzario = sRigaVMandatoObblScad.PG_OBBLIGAZIONE_SCADENZARIO;
    end loop;
 end;

 procedure updScadAccertamento(ReversaleTestata reversale%rowtype) is
 begin
    updAccertamento(ReversaleTestata);

      for sRigaVReversaleAccerScad in (
      select *
      from v_reversale_AccerScad
      where cd_cds =  ReversaleTestata.CD_CDS
      and   esercizio = ReversaleTestata.ESERCIZIO
      and   pg_reversale = ReversaleTestata.PG_REVERSALE)
      loop
        update accertamento_scadenzario
        set  IM_ASSOCIATO_DOC_CONTABILE = IM_ASSOCIATO_DOC_CONTABILE + sRigaVReversaleAccerScad.TOT_REVERSALE_RIGHE,
         DUVA = ReversaleTestata.DUVA,
         UTUV = ReversaleTestata.UTUV,
         PG_VER_REC = PG_VER_REC +1
      where cd_cds =  sRigaVReversaleAccerScad.CD_CDS
      and   esercizio = sRigaVReversaleAccerScad.ESERCIZIO
      and   esercizio_originale = sRigaVReversaleAccerScad.ESERCIZIO_ORI_ACCERTAMENTO
      and   pg_accertamento = sRigaVReversaleAccerScad.PG_ACCERTAMENTO
      and   pg_accertamento_scadenzario = sRigaVReversaleAccerScad.PG_ACCERTAMENTO_SCADENZARIO;
    end loop;
end;


 procedure generaDocumenti(
   aManPrncT in out  mandato%rowtype,
   aManPrncR in out  CNRCTB038.righeMandatoList,
   aRevColl01T in out reversale%rowtype ,
   aRevColl01R in out CNRCTB038.righeReversaleList ,
   aRevColl02T in out reversale%rowtype ,
   aRevColl02R in out CNRCTB038.righeReversaleList ,
   aRevColl03T in out reversale%rowtype ,
   aRevColl03R in out CNRCTB038.righeReversaleList ,
   aRevColl04T in out reversale%rowtype ,
   aRevColl04R in out CNRCTB038.righeReversaleList ,
   aRevColl05T in out reversale%rowtype ,
   aRevColl05R in out CNRCTB038.righeReversaleList ,
   aRevColl06T in out reversale%rowtype ,
   aRevColl06R in out CNRCTB038.righeReversaleList ,
   aRevColl07T in out reversale%rowtype ,
   aRevColl07R in out CNRCTB038.righeReversaleList ,
   aRevColl08T in out reversale%rowtype ,
   aRevColl08R in out CNRCTB038.righeReversaleList ,
   aRevColl09T in out reversale%rowtype ,
   aRevColl09R in out CNRCTB038.righeReversaleList ,
   aRevColl10T in out reversale%rowtype ,
   aRevColl10R in out CNRCTB038.righeReversaleList ,
   aRevColl11T in out reversale%rowtype ,
   aRevColl11R in out CNRCTB038.righeReversaleList ,
   aManColl01T in out mandato%rowtype ,
   aManColl01R in out CNRCTB038.righeMandatoList ,
   aManColl02T in out mandato%rowtype ,
   aManColl02R in out CNRCTB038.righeMandatoList ,
   aManColl03T in out mandato%rowtype ,
   aManColl03R in out CNRCTB038.righeMandatoList ,
   aManColl04T in out mandato%rowtype ,
   aManColl04R in out CNRCTB038.righeMandatoList ,
   aManColl05T in out mandato%rowtype ,
   aManColl05R in out CNRCTB038.righeMandatoList ,
   aManColl06T in out mandato%rowtype ,
   aManColl06R in out CNRCTB038.righeMandatoList ,
   aManColl07T in out mandato%rowtype ,
   aManColl07R in out CNRCTB038.righeMandatoList ,
   aManColl08T in out mandato%rowtype ,
   aManColl08R in out CNRCTB038.righeMandatoList ,
   aManColl09T in out mandato%rowtype ,
   aManColl09R in out CNRCTB038.righeMandatoList ,
   aManColl10T in out mandato%rowtype ,
   aManColl10R in out CNRCTB038.righeMandatoList
 ) is
 controlloCassaCds boolean;
 controlloSaldoCapitoli boolean;

 sRigaScadVoce obbligazione_scad_voce%rowtype;
 sTestaMandato_gen mandato%rowtype;
 lRigheMandato_gen CNRCTB038.righeMandatoList;

 sTestaReversale_gen reversale%rowtype;
 lRigheReversale_gen CNRCTB038.righeReversaleList;

 sMandatoTerzo mandato_terzo%rowtype;
 sReversaleTerzo reversale_terzo%rowtype;
 sCodiceTerzo mandato_riga.CD_TERZO%type;
 contatore number;
 i number;
 aAssManMan ass_mandato_mandato%rowtype;

 desc_errore varchar2(2000);
 aTipoBolloS tipo_bollo%rowtype;
 aTipoBolloE tipo_bollo%rowtype;
 isConMandatoPrincipale boolean;
 aObb obbligazione%rowtype;
 aAcc accertamento%rowtype;
 tesoreria_unica char(1):='N';
 begin
      isConMandatoPrincipale:=true;

      begin
       select * into aTipoBolloS from tipo_bollo where
            ti_entrata_spesa in ('*',CNRCTB001.GESTIONE_SPESE)
      and fl_cancellato='N'
      and fl_default='Y';
    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Tipo bollo di default non trovato');
    end;


      begin
       select * into aTipoBolloE from tipo_bollo where
            ti_entrata_spesa in ('*',CNRCTB001.GESTIONE_ENTRATE)
      and fl_cancellato='N'
      and fl_default='Y';
    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Tipo bollo di default non trovato');
    end;

    for contatore in 0..10 loop
        if contatore = 0 then
         sTestaMandato_gen := aManPrncT;
         lRigheMandato_gen := aManPrncR;
      end if;
        if contatore = 1 then
         sTestaMandato_gen := aManColl01T;
         lRigheMandato_gen := aManColl01R;
      end if;
        if contatore = 2 then
         sTestaMandato_gen := aManColl02T;
         lRigheMandato_gen := aManColl02R;
      end if;
        if contatore = 3 then
         sTestaMandato_gen := aManColl03T;
         lRigheMandato_gen := aManColl03R;
      end if;
        if contatore = 4 then
         sTestaMandato_gen := aManColl04T;
         lRigheMandato_gen := aManColl04R;
      end if;
        if contatore = 5 then
         sTestaMandato_gen := aManColl05T;
         lRigheMandato_gen := aManColl05R;
      end if;
        if contatore = 6 then
         sTestaMandato_gen := aManColl06T;
         lRigheMandato_gen := aManColl06R;
      end if;
        if contatore = 7 then
         sTestaMandato_gen := aManColl07T;
         lRigheMandato_gen := aManColl07R;
      end if;
        if contatore = 8 then
         sTestaMandato_gen := aManColl08T;
         lRigheMandato_gen := aManColl08R;
      end if;
        if contatore = 9 then
         sTestaMandato_gen := aManColl09T;
         lRigheMandato_gen := aManColl09R;
      end if;
        if contatore = 10 then
         sTestaMandato_gen := aManColl10T;
         lRigheMandato_gen := aManColl10R;
      end if;
      if contatore = 0 and lRigheMandato_gen.count = 0 then
           isConMandatoPrincipale:=false;
      end if;
      if (lRigheMandato_gen.count > 0) then
        begin
         select fl_tesoreria_unica into tesoreria_unica from parametri_cnr
         where
              esercizio =sTestaMandato_gen.esercizio;

        exception when NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO('Configurazione gestione tesoreria mancante');
        end;
          -- Impostazione del progressivo del mandato
          if(tesoreria_unica='N') then
            sTestaMandato_gen.pg_mandato:= CNRCTB018.getNextNumDocCont(sTestaMandato_gen.cd_tipo_documento_cont, sTestaMandato_gen.esercizio, sTestaMandato_gen.cd_cds, sTestaMandato_gen.utcr);
          else
            sTestaMandato_gen.pg_mandato:= CNRCTB018.getNextNumDocCont(sTestaMandato_gen.cd_tipo_documento_cont, sTestaMandato_gen.esercizio, cnrctb020.getCDCDSENTE(sTestaMandato_gen.esercizio), sTestaMandato_gen.utcr);
          end if;
          -- Impostazione dello STATO_COGE
          if contatore =0 then
             -- Mandato principale
             sTestaMandato_gen.STATO_COGE := CNRCTB100.STATO_COEP_INI;
          else
             -- Mandati collegati - se non impostato viene messo stato coge X
             if sTestaMandato_gen.STATO_COGE is null then
                sTestaMandato_gen.STATO_COGE := CNRCTB100.STATO_COEP_EXC;
             end if;
          end if;
          -- Troncamento della data di registrazione
          sTestaMandato_gen.dt_emissione:=trunc(sTestaMandato_gen.dt_emissione);
          -- Impostazione di UTUV DUVA e PG_VER_REC per la testata del mandato
          sTestaMandato_gen.UTUV := sTestaMandato_gen.UTCR;
          sTestaMandato_gen.DUVA := sTestaMandato_gen.DUVA;
          sTestaMandato_gen.PG_VER_REC := 1;
          -- Controlli
          -- Tracciabilit? pagamenti: Controllo ammissibilit? della modalit? di pagamento per la tipologia del documento generico
          -- Solo per mandati di pagamento
          if sTestaMandato_gen.ti_mandato = CNRCTB038.TI_MAN_PAG then
             for i in 1..lRigheMandato_gen.count loop
                 verificaTracciabilitaPag(sTestaMandato_gen.esercizio,
                                          sTestaMandato_gen.dt_emissione,
                                          lRigheMandato_gen(i).cd_modalita_pag,
                                          lRigheMandato_gen(i).cd_tipo_documento_amm,
                                          sTestaMandato_gen.im_mandato - sTestaMandato_gen.im_ritenute);
             end loop;
          end if;

          if (tesoreria_unica='N') then
              controlloCassaCds      := CheckCassaCds(sTestaMandato_gen);
          else
              controlloCassaCds:= True;
          end if;
          /*
          --forzo il controllo di cassa a true per i mandati di versamento cori ed iva
          --(da sistemare:deve valere solo per i versamenti accentrati!!!!!.....)
          if not controlloCassaCds then
             -- controllo se ? un mandato di versamento cori, cio? se tutte le righe
             -- hanno tipo documento = GEN_CORV_S, oppure di versamento iva commerciale
             -- cio? tipo_documento = TRASF_S
             for aInd in 1..lRigheMandato_gen.count loop
                if lRigheMandato_gen(aInd).cd_tipo_documento_amm=cnrctb100.TI_GEN_CORI_VER_SPESA then
                --if lRigheMandato_gen(aInd).cd_tipo_documento_amm=cnrctb100.TI_GENERICO_TRASF_S then
                      controlloCassaCds := True;
                end if;
             end loop;
          end if;
          */
          for aInd in 1..lRigheMandato_gen.count loop
                -- controllo se il l'obbligazione specificata sulle righe ? su partita di giro ed eventualemente aggiorno la riga del mandato di conseguenza
                begin
                 select * into aObb from obbligazione where
                      cd_cds = lRigheMandato_gen(aInd).cd_cds
                  and esercizio = lRigheMandato_gen(aInd).esercizio_obbligazione
                  and esercizio_originale = lRigheMandato_gen(aInd).esercizio_ori_obbligazione
                  and pg_obbligazione = lRigheMandato_gen(aInd).pg_obbligazione
                 for update nowait;
                exception when NO_DATA_FOUND then
                 IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' a cui collegare la riga di mandato non trovata');
                end;
                lRigheMandato_gen(aInd).fl_pgiro:=aObb.fl_pgiro;
          end loop;
          controlloSaldoCapitoli := CheckSaldoCapitoli(lRigheMandato_gen);
          if controlloCassaCds and controlloSaldoCapitoli then
             cnrctb038.ins_MANDATO(sTestaMandato_gen);
           for i in 1..lRigheMandato_gen.count loop
             lRigheMandato_gen(i).pg_mandato:=sTestaMandato_gen.pg_mandato;
                 -- Impostazione di UTUV DUVA e PG_VER_REC
             lRigheMandato_gen(i).UTUV:= lRigheMandato_gen(i).UTCR;
             lRigheMandato_gen(i).DUVA:= lRigheMandato_gen(i).DACR;
             lRigheMandato_gen(i).PG_VER_REC:= 1;
           -- Inserimento riga del mandato
             cnrctb038.ins_MANDATO_RIGA(lRigheMandato_gen(i));

                                         -- 23.04.2007 Stanislao Fusco
                                         -- Inserita procedura che pu? inserire la riga automatica SIOPE
                                         Inserisci_SIOPE_automatico (lRigheMandato_gen(i));

           sCodiceTerzo := lRigheMandato_gen(i).CD_TERZO;
         end loop;
--DBMS_Output.PUT_LINE ('generaDocumenti');
         updSaldoCapitoliM(sTestaMandato_gen,'I','A');

         updScadObbligazione(sTestaMandato_gen);
         sMandatoTerzo.CD_CDS := sTestaMandato_gen.CD_CDS;
         sMandatoTerzo.ESERCIZIO := sTestaMandato_gen.ESERCIZIO;
         sMandatoTerzo.PG_MANDATO := sTestaMandato_gen.PG_MANDATO;
         sMandatoTerzo.CD_TERZO   := sCodiceTerzo;
         sMandatoTerzo.CD_TIPO_BOLLO := aTipoBolloS.cd_tipo_bollo;
         sMandatoTerzo.DACR := sTestaMandato_gen.DACR;
         sMandatoTerzo.UTCR := sTestaMandato_gen.UTCR;
         sMandatoTerzo.DUVA := sTestaMandato_gen.DUVA;
         sMandatoTerzo.UTUV := sTestaMandato_gen.UTUV;
         sMandatoTerzo.PG_VER_REC := 1;
         cnrctb038.ins_MANDATO_TERZO(sMandatoTerzo);
         -- Aggiorna il collegamento mandato-mandato se c? il mandato principale
/*
if isConMandatoPrincipale = false then
PIPE.SEND_MESSAGE('false ');
else
PIPE.SEND_MESSAGE('true ');
end if;
PIPE.SEND_MESSAGE('contatore = '||contatore);

PIPE.SEND_MESSAGE('aManPrncT.cd_cds = '||aManPrncT.cd_cds);
PIPE.SEND_MESSAGE('aManPrncT.pg_mandato = '||aManPrncT.pg_mandato);
PIPE.SEND_MESSAGE('sTestaMandato_gen.CD_CDS = '||sTestaMandato_gen.CD_CDS);
PIPE.SEND_MESSAGE('sTestaMandato_gen.pg_mandato = '||sTestaMandato_gen.pg_mandato);
*/
         if contatore > 0 and isConMandatoPrincipale then

                  aAssManMan.CD_CDS:=aManPrncT.cd_cds;
                  aAssManMan.ESERCIZIO:=aManPrncT.esercizio;
                  aAssManMan.PG_MANDATO:=aManPrncT.pg_mandato;
                  aAssManMan.CD_CDS_COLL:=sTestaMandato_gen.CD_CDS;
                  aAssManMan.ESERCIZIO_COLL:=sTestaMandato_gen.esercizio;
                  aAssManMan.PG_MANDATO_COLL:=sTestaMandato_gen.pg_mandato;
                  aAssManMan.DACR:=sTestaMandato_gen.DACR;
                  aAssManMan.UTCR:=sTestaMandato_gen.UTCR;
                  aAssManMan.DUVA:=sTestaMandato_gen.DACR;
                  aAssManMan.UTUV:=sTestaMandato_gen.UTCR;
                  aAssManMan.PG_VER_REC:=1;
          CNRCTB038.INS_ASS_MANDATO_MANDATO(aAssManMan);
         end if;
               cnrctb300.leggiMandatoReversale(sTestaMandato_gen.CD_CDS,
                                         sTestaMandato_gen.ESERCIZIO,
                             sTestaMandato_gen.PG_MANDATO,
                             'MAN',
                             'I',
                             sTestaMandato_gen.utuv);
        else
           desc_errore := 'Saldo CC dei Capitoli oppure Cassa Cds, non sufficiente';
           IBMERR001.RAISE_ERR_GENERICO(desc_errore);
          end if;
      end if;
        if contatore = 0 then
         aManPrncT := sTestaMandato_gen;
         aManPrncR := lRigheMandato_gen;
      end if;
        if contatore = 1 then
         aManColl01T := sTestaMandato_gen;
         aManColl01R := lRigheMandato_gen;
      end if;
        if contatore = 2 then
         aManColl02T := sTestaMandato_gen;
         aManColl02R := lRigheMandato_gen;
      end if;
        if contatore = 3 then
         aManColl03T := sTestaMandato_gen;
         aManColl03R := lRigheMandato_gen;
      end if;
        if contatore = 4 then
         aManColl04T := sTestaMandato_gen;
         aManColl04R := lRigheMandato_gen;
      end if;
        if contatore = 5 then
         aManColl05T := sTestaMandato_gen;
         aManColl05R := lRigheMandato_gen;
      end if;
        if contatore = 6 then
         aManColl06T := sTestaMandato_gen;
         aManColl06R := lRigheMandato_gen;
      end if;
        if contatore = 7 then
         aManColl07T := sTestaMandato_gen;
         aManColl07R := lRigheMandato_gen;
      end if;
        if contatore = 8 then
         aManColl08T := sTestaMandato_gen;
         aManColl08R := lRigheMandato_gen;
      end if;
        if contatore = 9 then
         aManColl09T := sTestaMandato_gen;
         aManColl09R := lRigheMandato_gen;
      end if;
        if contatore = 10 then
         aManColl10T := sTestaMandato_gen;
         aManColl10R := lRigheMandato_gen;
      end if;
    end loop;

    contatore:=1;

      for contatore in 1..11 loop
        if contatore = 1 then
         sTestaReversale_gen := aRevColl01T;
         lRigheReversale_gen := aRevColl01R;
      end if;
        if contatore = 2 then
         sTestaReversale_gen := aRevColl02T;
         lRigheReversale_gen := aRevColl02R;
      end if;
        if contatore = 3 then
         sTestaReversale_gen := aRevColl03T;
         lRigheReversale_gen := aRevColl03R;
      end if;
        if contatore = 4 then
         sTestaReversale_gen := aRevColl04T;
         lRigheReversale_gen := aRevColl04R;
      end if;
        if contatore = 5 then
         sTestaReversale_gen := aRevColl05T;
         lRigheReversale_gen := aRevColl05R;
      end if;
        if contatore = 6 then
         sTestaReversale_gen := aRevColl06T;
         lRigheReversale_gen := aRevColl06R;
      end if;
        if contatore = 7 then
         sTestaReversale_gen := aRevColl07T;
         lRigheReversale_gen := aRevColl07R;
      end if;
        if contatore = 8 then
         sTestaReversale_gen := aRevColl08T;
         lRigheReversale_gen := aRevColl08R;
      end if;
        if contatore = 9 then
         sTestaReversale_gen := aRevColl09T;
         lRigheReversale_gen := aRevColl09R;
      end if;
        if contatore = 10 then
         sTestaReversale_gen := aRevColl10T;
         lRigheReversale_gen := aRevColl10R;
      end if;
      if contatore = 11 then
         sTestaReversale_gen := aRevColl11T;
         lRigheReversale_gen := aRevColl11R;
      end if;
      if (lRigheReversale_gen.count > 0) then
        select fl_tesoreria_unica into tesoreria_unica from parametri_cnr
         where
              esercizio =sTestaReversale_gen.esercizio;
       if(tesoreria_unica='N') then
            sTestaReversale_gen.pg_reversale   := CNRCTB018.getNextNumDocCont(sTestaReversale_gen.cd_tipo_documento_cont, sTestaReversale_gen.esercizio, sTestaReversale_gen.cd_cds, sTestaReversale_gen.utcr);
       else
            sTestaReversale_gen.pg_reversale   := CNRCTB018.getNextNumDocCont(sTestaReversale_gen.cd_tipo_documento_cont, sTestaReversale_gen.esercizio, cnrctb020.getCDCDSENTE(sTestaReversale_gen.esercizio), sTestaReversale_gen.utcr);
       end if;
        for aInd in 1..lRigheReversale_gen.count loop
                -- controllo se il l'accertamento specificata sulle righe ? su partita di giro ed eventualemente aggiorno la riga della reversale di conseguenza
              begin
               select * into aAcc from accertamento where
                    cd_cds = lRigheReversale_gen(aInd).cd_cds
                and esercizio = lRigheReversale_gen(aInd).esercizio_accertamento
                and esercizio_originale = lRigheReversale_gen(aInd).esercizio_ori_accertamento
                and pg_accertamento = lRigheReversale_gen(aInd).pg_accertamento
               for update nowait;
              exception when NO_DATA_FOUND then
               IBMERR001.RAISE_ERR_GENERICO('Accertamento a cui collegare la riga di reversale non trovato');
              end;
                lRigheReversale_gen(aInd).fl_pgiro:=aAcc.fl_pgiro;
              end loop;
        -- Impostazione dello STATO_COGE - se non impostato viene messo a X
        if sTestaReversale_gen.STATO_COGE is null then
         sTestaReversale_gen.STATO_COGE := CNRCTB100.STATO_COEP_EXC;
        end if;
        -- Troncamento della data di registrazione
        sTestaReversale_gen.dt_emissione:=trunc(sTestaReversale_gen.dt_emissione);
        -- Impostazione di UTUV DUVA e PG_VER_REC per la testata della reversale
            sTestaReversale_gen.UTUV := sTestaReversale_gen.UTCR;
            sTestaReversale_gen.DUVA := sTestaReversale_gen.DUVA;
            sTestaReversale_gen.PG_VER_REC := 1;
        -- Inserimento della testata della reversale
              cnrctb038.ins_REVERSALE(sTestaReversale_gen);
          for i in 1..lRigheReversale_gen.count loop
            lRigheReversale_gen(i).pg_reversale:= sTestaReversale_gen.pg_reversale;
                -- Impostazione di UTUV DUVA e PG_VER_REC
            lRigheReversale_gen(i).UTUV:= lRigheReversale_gen(i).UTCR;
            lRigheReversale_gen(i).DUVA:= lRigheReversale_gen(i).DACR;
            lRigheReversale_gen(i).PG_VER_REC:= 1;
          -- Inserimento riga
            cnrctb038.ins_REVERSALE_RIGA(lRigheReversale_gen(i));

                                  -- 23.05.2007 Stanislao Fusco
                                  -- Inserita procedura che pu? inserire la riga automatica SIOPE
                                  Inserisci_SIOPE_automatico (lRigheReversale_gen(i));

          sCodiceTerzo := lRigheReversale_gen(i).CD_TERZO;
        end loop;
          updScadAccertamento(sTestaReversale_gen);
--DBMS_Output.PUT_LINE ('R 1');
          updSaldoCapitoliR(sTestaReversale_gen,'I','A');
          sReversaleTerzo.CD_CDS := sTestaReversale_gen.CD_CDS;
          sReversaleTerzo.CD_TERZO := sCodiceTerzo;
          sReversaleTerzo.CD_TIPO_BOLLO := aTipoBolloE.cd_tipo_bollo;
          sReversaleTerzo.DACR := sTestaReversale_gen.DACR;
          sReversaleTerzo.DUVA := sTestaReversale_gen.DUVA;
          sReversaleTerzo.ESERCIZIO := sTestaReversale_gen.ESERCIZIO;
          sReversaleTerzo.PG_REVERSALE := sTestaReversale_gen.PG_REVERSALE;
          sReversaleTerzo.PG_VER_REC := 1;
          sReversaleTerzo.UTCR := sTestaReversale_gen.UTCR;
          sReversaleTerzo.UTUV := sTestaReversale_gen.UTUV;
          cnrctb038.ins_REVERSALE_TERZO(sReversaleTerzo);
        if isConMandatoPrincipale then -- Se c'? mandato principale lo collego alla reversale
         cnrctb038.ins_ASS_MANDATO_REVERSALE (aManPrncT, aManPrncT.pg_mandato, sTestaReversale_gen);
            end if;
        cnrctb300.leggiMandatoReversale(sTestaReversale_gen.CD_CDS,
                                      sTestaReversale_gen.ESERCIZIO,
                          sTestaReversale_gen.PG_REVERSALE,
                          'REV',
                          'I',
                          sTestaReversale_gen.utuv);
      end if;
        if contatore = 1 then
         aRevColl01T := sTestaReversale_gen;
         aRevColl01R := lRigheReversale_gen;
      end if;
        if contatore = 2 then
         aRevColl02T := sTestaReversale_gen;
         aRevColl02R := lRigheReversale_gen;
      end if;
        if contatore = 3 then
         aRevColl03T := sTestaReversale_gen;
         aRevColl03R := lRigheReversale_gen;
      end if;
        if contatore = 4 then
         aRevColl04T := sTestaReversale_gen;
         aRevColl04R := lRigheReversale_gen;
      end if;
        if contatore = 5 then
         aRevColl05T := sTestaReversale_gen;
         aRevColl05R := lRigheReversale_gen;
      end if;
        if contatore = 6 then
         aRevColl06T := sTestaReversale_gen;
         aRevColl06R := lRigheReversale_gen;
      end if;
        if contatore = 7 then
         aRevColl07T := sTestaReversale_gen;
         aRevColl07R := lRigheReversale_gen;
      end if;
        if contatore = 8 then
         aRevColl08T := sTestaReversale_gen;
         aRevColl08R := lRigheReversale_gen;
      end if;
        if contatore = 9 then
         aRevColl09T := sTestaReversale_gen;
         aRevColl09R := lRigheReversale_gen;
      end if;
        if contatore = 10 then
         aRevColl10T := sTestaReversale_gen;
         aRevColl10R := lRigheReversale_gen;
      end if;
      if contatore = 11 then
         aRevColl11T := sTestaReversale_gen;
         aRevColl11R := lRigheReversale_gen;
      end if;
    end loop;
 exception when dup_val_on_index then
     desc_errore := 'Chiave duplicata';
     IBMERR001.RAISE_ERR_GENERICO(desc_errore);
 end;

 procedure GENERAECOLLEGADOC(
   aManP in mandato%rowtype,
   aRev in out reversale%rowtype,
   aListaRev in out CNRCTB038.righeReversaleList
 ) is
  aTempManP mandato%rowtype;
  sReversaleTerzo reversale_terzo%rowtype;
  sCodiceTerzo mandato_riga.CD_TERZO%type;
  desc_errore varchar2(2000);
  aTipoBolloE tipo_bollo%rowtype;
  aAcc accertamento%rowtype;
  tesoreria_unica char(1):='N';
 begin
       begin
       select * into aTipoBolloE from tipo_bollo where
            ti_entrata_spesa in ('*',CNRCTB001.GESTIONE_ENTRATE)
      and fl_cancellato='N'
      and fl_default='Y';
    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Tipo bollo di default non trovato');
    end;

          begin
            select * into aTempManP from mandato
      Where esercizio = aManP.esercizio And
            cd_cds = aManP.cd_cds And
            pg_mandato = aManp.pg_mandato
            for update nowait;
          exception when NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO('Mandato non trovato: '||aManP.pg_mandato||' cds:'||aManP.cd_cds||' es.:'||aManP.esercizio);
    end;

    begin
       select fl_tesoreria_unica into tesoreria_unica from parametri_cnr
       where
            esercizio =aManP.esercizio;

    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Configurazione gestione tesoreria mancante');
    end;
    If (aListaRev.count > 0) then
       if(tesoreria_unica='N') then
           aRev.pg_reversale   := CNRCTB018.getNextNumDocCont(aRev.cd_tipo_documento_cont, aRev.esercizio, aRev.cd_cds, aRev.utcr);
       else
           aRev.pg_reversale   := CNRCTB018.getNextNumDocCont(aRev.cd_tipo_documento_cont, aRev.esercizio, cnrctb020.getCDCDSENTE(aRev.esercizio), aRev.utcr);
       end if;
        for aInd in 1..aListaRev.count loop
                -- controllo se il l'accertamento specificata sulle righe ? su partita di giro
                -- ed eventualemente aggiorno la riga della reversale di conseguenza
              Begin
               select *
               into aAcc
               from  accertamento
               Where cd_cds = aListaRev(aInd).cd_cds
                and  esercizio = aListaRev(aInd).esercizio_accertamento
                and  esercizio_originale = aListaRev(aInd).esercizio_ori_accertamento
                and  pg_accertamento = aListaRev(aInd).pg_accertamento
                 for update nowait;
              Exception when NO_DATA_FOUND then
               IBMERR001.RAISE_ERR_GENERICO('Accertamento a cui collegare la riga di reversale non trovato');
              End;
              aListaRev(aInd).fl_pgiro := aAcc.fl_pgiro;

            End loop;
        -- Impostazione dello STATO_COGE - se non impostato viene messo a X
      if aRev.STATO_COGE is null then
       aRev.STATO_COGE := CNRCTB100.STATO_COEP_EXC;
      end if;

      -- Troncamento della data di registrazione
      aRev.dt_emissione:=trunc(aRev.dt_emissione);
      -- Impostazione di UTUV DUVA e PG_VER_REC per la testata della reversale
            aRev.UTUV := aRev.UTCR;
            aRev.DUVA := aRev.DUVA;
            aRev.PG_VER_REC := 1;

      -- Inserimento della testata della reversale
          cnrctb038.ins_REVERSALE(aRev);

        for i in 1..aListaRev.count loop
            aListaRev(i).pg_reversale:= aRev.pg_reversale;
                -- Impostazione di UTUV DUVA e PG_VER_REC
            aListaRev(i).UTUV:= aListaRev(i).UTCR;
            aListaRev(i).DUVA:= aListaRev(i).DACR;
            aListaRev(i).PG_VER_REC:= 1;
          -- Inserimento riga
            cnrctb038.ins_REVERSALE_RIGA(aListaRev(i));

            -- 23.05.2007 Stanislao Fusco
                      -- Inserita procedura che pu? inserire la riga automatica SIOPE

                      Inserisci_SIOPE_automatico (aListaRev(i));

          sCodiceTerzo := aListaRev(i).CD_TERZO;
            end loop;

          updScadAccertamento(aRev);

          updSaldoCapitoliR(aRev,'I','A');

          sReversaleTerzo.CD_CDS := aRev.CD_CDS;
          sReversaleTerzo.CD_TERZO := sCodiceTerzo;
          sReversaleTerzo.CD_TIPO_BOLLO := aTipoBolloE.cd_tipo_bollo;
          sReversaleTerzo.DACR := aRev.DACR;
          sReversaleTerzo.DUVA := aRev.DUVA;
          sReversaleTerzo.ESERCIZIO := aRev.ESERCIZIO;
          sReversaleTerzo.PG_REVERSALE := aRev.PG_REVERSALE;
          sReversaleTerzo.PG_VER_REC := 1;
          sReversaleTerzo.UTCR := aRev.UTCR;
          sReversaleTerzo.UTUV := aRev.UTUV;

          cnrctb038.ins_REVERSALE_TERZO(sReversaleTerzo);

      cnrctb038.ins_ASS_MANDATO_REVERSALE (aManP, aManP.pg_mandato, aRev);

      cnrctb300.leggiMandatoReversale(aRev.CD_CDS, aRev.ESERCIZIO, aRev.PG_REVERSALE, 'REV', 'I', aRev.utuv);

                  -- Aggiorno l'importo ritenute del mandato principale
      update mandato
           -- Devo sommare le ritenute !!!! 22/01/2003
              Set im_ritenute = im_ritenute + aRev.im_reversale
      Where esercizio = aManP.esercizio And
            cd_cds = aManP.cd_cds And
            pg_mandato = aManp.pg_mandato;
      end if;
 end;

 procedure GENERAECOLLEGADOC(
   aManP in mandato%rowtype,
   aMan in out mandato%rowtype,
   aListaMan in out CNRCTB038.righeMandatoList
 ) is
  aTempManP mandato%rowtype;
  sMandatoTerzo mandato_terzo%rowtype;
  sCodiceTerzo mandato_riga.CD_TERZO%type;
  aAssManMan ass_mandato_mandato%rowtype;
  desc_errore varchar2(2000);
  aTipoBolloE tipo_bollo%rowtype;
  aObb obbligazione%rowtype;
  tesoreria_unica char(1):='N';
 begin
       begin
       select * into aTipoBolloE from tipo_bollo where
            ti_entrata_spesa in ('*',CNRCTB001.GESTIONE_SPESE)
      and fl_cancellato='N'
      and fl_default='Y';
    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Tipo bollo di default non trovato');
    end;

          begin
          select * into aTempManP from mandato
        where
             esercizio = aManP.esercizio
         and cd_cds = aManP.cd_cds
         and pg_mandato = aManp.pg_mandato
        for update nowait;
      exception when NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO('Mandato non trovato: '||aManP.pg_mandato||' cds:'||aManP.cd_cds||' es.:'||aManP.esercizio);
      end;
      begin
       select fl_tesoreria_unica into tesoreria_unica from parametri_cnr
       where
            esercizio =aMan.esercizio;

    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Configurazione gestione tesoreria mancante');
    end;
      if (aListaMan.count > 0) then
         if(tesoreria_unica='N') then
            aMan.pg_mandato   := CNRCTB018.getNextNumDocCont(aMan.cd_tipo_documento_cont, aMan.esercizio, aMan.cd_cds, aMan.utcr);
        else
            aMan.pg_mandato   := CNRCTB018.getNextNumDocCont(aMan.cd_tipo_documento_cont, aMan.esercizio,cnrctb020.getCDCDSENTE(aMan.esercizio), aMan.utcr);
       end if;
        for aInd in 1..aListaMan.count loop
                -- controllo se il mandato specificato sulle righe ? su partita di giro ed eventualemente aggiorno la riga del mandato di conseguenza
              begin
               select * into aObb from obbligazione where
                    cd_cds = aListaMan(aInd).cd_cds
                and esercizio = aListaMan(aInd).esercizio_obbligazione
                and esercizio_originale = aListaMan(aInd).esercizio_ori_obbligazione
          and pg_obbligazione = aListaMan(aInd).pg_obbligazione
               for update nowait;
              exception when NO_DATA_FOUND then
               IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' a cui collegare la riga di mandato non trovata');
              end;
                aListaMan(aInd).fl_pgiro:=aObb.fl_pgiro;
              end loop;
        -- Impostazione dello STATO_COGE - se non impostato viene messo a X
        if aMan.STATO_COGE is null then
         aMan.STATO_COGE := CNRCTB100.STATO_COEP_EXC;
        end if;
        -- Troncamento della data di registrazione
        aMan.dt_emissione:=trunc(aMan.dt_emissione);
        -- Impostazione di UTUV DUVA e PG_VER_REC per la testata del mandato
            aMan.UTUV := aMan.UTCR;
            aMan.DUVA := aMan.DUVA;
            aMan.PG_VER_REC := 1;
        -- Inserimento della testata della reversale
              cnrctb038.ins_MANDATO(aMan);
          for i in 1..aListaMan.count loop
            aListaMan(i).pg_mandato:= aMan.pg_mandato;
                -- Impostazione di UTUV DUVA e PG_VER_REC
            aListaMan(i).UTUV:= aListaMan(i).UTCR;
            aListaMan(i).DUVA:= aListaMan(i).DACR;
            aListaMan(i).PG_VER_REC:= 1;
          -- Inserimento riga
            cnrctb038.ins_MANDATO_RIGA(aListaMan(i));

                                  -- 23.04.2007 Stanislao Fusco
                                  -- Inserita procedura che pu? inserire la riga automatica SIOPE
                                  Inserisci_SIOPE_automatico(aListaMan(i));

          sCodiceTerzo := aListaMan(i).CD_TERZO;
        end loop;
          updScadObbligazione(aMan);
--DBMS_Output.PUT_LINE ('M 1');
          updSaldoCapitoliM(aMan,'I','A');
          sMandatoTerzo.CD_CDS := aMan.CD_CDS;
          sMandatoTerzo.CD_TERZO := sCodiceTerzo;
          sMandatoTerzo.CD_TIPO_BOLLO := aTipoBolloE.cd_tipo_bollo;
          sMandatoTerzo.DACR := aMan.DACR;
          sMandatoTerzo.DUVA := aMan.DUVA;
          sMandatoTerzo.ESERCIZIO := aMan.ESERCIZIO;
          sMandatoTerzo.PG_MANDATO := aMan.PG_MANDATO;
          sMandatoTerzo.PG_VER_REC := 1;
          sMandatoTerzo.UTCR := aMan.UTCR;
          sMandatoTerzo.UTUV := aMan.UTUV;
          cnrctb038.ins_MANDATO_TERZO(sMandatoTerzo);
              aAssManMan.CD_CDS:=aManP.cd_cds;
              aAssManMan.ESERCIZIO:=aManP.esercizio;
              aAssManMan.PG_MANDATO:=aManP.pg_mandato;
              aAssManMan.CD_CDS_COLL:=aMan.CD_CDS;
              aAssManMan.ESERCIZIO_COLL:=aMan.esercizio;
              aAssManMan.PG_MANDATO_COLL:=aMan.pg_mandato;
              aAssManMan.DACR:=aMan.DACR;
              aAssManMan.UTCR:=aMan.UTCR;
              aAssManMan.DUVA:=aMan.DACR;
              aAssManMan.UTUV:=aMan.UTCR;
              aAssManMan.PG_VER_REC:=1;
          cnrctb038.ins_ASS_MANDATO_MANDATO (aAssManMan);
        cnrctb300.leggiMandatoReversale(aMan.CD_CDS,
                                      aMan.ESERCIZIO,
                          aMan.PG_MANDATO,
                          'MAN',
                          'I',
                          aMan.utuv);
      end if;
 end;

 PROCEDURE checkRevDisassocMan
 (
  aCdCdsMan varchar2,
  aEsMan number,
  aPgMan number,
  aCdCdsRev varchar2,
  aEsRev number,
  aPgRev number
 ) IS
 BEGIN
  for aComp in (select a.* from compenso a, mandato_riga b where
           b.cd_cds_doc_amm = a.cd_cds
     and b.cd_uo_doc_amm = a.cd_unita_organizzativa
     and b.cd_tipo_documento_amm = CNRCTB100.TI_COMPENSO
     and b.pg_doc_amm = a.pg_compenso
     and b.esercizio_doc_amm = a.esercizio
     and b.cd_cds = aCdCdsMan
     and b.esercizio = aEsMan
     and b.pg_mandato = aPgMan) loop
   for aCori in (select a.* from contributo_ritenuta a, reversale_riga b
    where
         a.cd_cds = aComp.cd_cds
     and a.esercizio = aComp.esercizio
     and a.cd_unita_organizzativa = aComp.cd_unita_organizzativa
     and a.pg_compenso = aComp.pg_compenso
     and b.cd_cds = a.cd_cds_accertamento
     and b.esercizio_accertamento = a.esercizio_accertamento
     and b.esercizio_ori_accertamento = a.esercizio_ori_accertamento
     and b.pg_accertamento = a.pg_accertamento
     and b.pg_accertamento_scadenzario = a.pg_accertamento_scadenzario
     and b.esercizio_doc_amm = a.esercizio
     and b.cd_cds = aCdCdsRev
     and b.esercizio = aEsRev
     and b.pg_reversale = aPgRev) loop
    -- Verifica che la reversale non sia relativa al compenso di cui aPgMan ? il mandato principale
    IBMERR001.RAISE_ERR_GENERICO('La reversale n. '||aPgRev||' non ? scollegabile dal mandato n.'||aPgMan||' cds:'||aCdCdsMan||' es.:'||aEsMan);
   end loop;
  end loop;
  -- Se la reversale ? collegata a generico di versamento ENTRATA non pu? essere scollegata dal mandato
  declare
   aRRL reversale_riga%rowtype;
  begin
    select * into aRRL from reversale_riga where
          esercizio = aEsRev
    and cd_cds = aCdCdsRev
    and pg_reversale = aPgRev
     for update nowait;
    if aRRL.cd_tipo_documento_amm = CNRCTB100.TI_GEN_CORI_VER_ENTRATA then
       IBMERR001.RAISE_ERR_GENERICO('La reversale n. '||aPgRev||' non ? scollegabile dal mandato n.'||aPgMan||' cds:'||aCdCdsMan||' es.:'||aEsMan);
    end if;
  end;
 END;


 PROCEDURE checkReversaliAssociate
   (
    inCdsMandato MANDATO.cd_cds%TYPE,
    inEsercizioMandato MANDATO.esercizio%TYPE,
    inPgMandato MANDATO.pg_mandato%TYPE,
    inImRitenute IN OUT NUMBER
   ) IS
     aImRitenute NUMBER(15,2);
     aImTmp NUMBER(15,2);
     aMan mandato%rowtype;
   BEGIN
    begin
   select * into aMan from mandato where
        cd_cds = inCdsMandato
    and esercizio = inEsercizioMandato
    and pg_mandato = inPgMandato
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Mandato non trovato n.'||inPgMandato||' cds:'||inCdsMandato||' es.:'||inEsercizioMandato);
  end;
    select nvl(sum(b.im_reversale),0) into aImTmp from ass_mandato_reversale a, reversale b where
        a.cd_cds_mandato = inCdsMandato
    and a.esercizio_mandato = inEsercizioMandato
    and a.pg_mandato = inPgMandato
    and a.cd_cds_reversale = inCdsMandato
    and a.esercizio_reversale = inEsercizioMandato
    and b.cd_cds = a.cd_cds_reversale
    and b.esercizio = a.esercizio_reversale
    and b.pg_reversale = a.pg_reversale
    and b.stato = CNRCTB038.STATO_AUT_EME;
    aImRitenute:=aImTmp;
  if aImRitenute > aMan.im_mandato then
   IBMERR001.RAISE_ERR_GENERICO('L''importo delle ritenute supera l''importo del mandato n.'||inPgMandato||' cds:'||inCdsMandato||' es.:'||inEsercizioMandato);
  end if;
    inImRitenute := aImRitenute;
 END checkReversaliAssociate;

 --
 -- Pre-post name: Generazione reversale
 -- Pre condizione: Sono stati definiti i dati di testata e delle righe della reversale
 -- Post condizione:
 --  La reversale e le sue righe vengono inserita; per ogni riga viene calcolato il fl_pgiro;
 --  viene creata la reversale_terzo selezioando il tipo bollo do default; vengono aggiornati gli
 --  importi associati a doc.contabili delle scadenze degli accertamenti delle righe delle reversali;
 --  vengono aggiornati i saldi delle voci.
 --

procedure GENERAREVERSALE(
   aRev IN OUT reversale%rowtype,
   aListaRev IN OUT CNRCTB038.righeReversaleList
 ) is
  sReversaleTerzo reversale_terzo%rowtype;
  sCodiceTerzo mandato_riga.CD_TERZO%type;
  desc_errore varchar2(2000);
  aTipoBolloE tipo_bollo%rowtype;
  aAcc accertamento%rowtype;
  tesoreria_unica char(1):='N';
begin
   begin
       select * into aTipoBolloE from tipo_bollo where
            ti_entrata_spesa in ('*',CNRCTB001.GESTIONE_ENTRATE)
      and fl_cancellato='N'
      and fl_default='Y';
    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Tipo bollo di default non trovato');
    end;
    begin
       select fl_tesoreria_unica into tesoreria_unica from parametri_cnr
       where
            esercizio =aRev.esercizio;

    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Configurazione gestione tesoreria mancante');
    end;
   if (aListaRev.count > 0) then
      if(tesoreria_unica='N') then
           aRev.pg_reversale   := CNRCTB018.getNextNumDocCont(aRev.cd_tipo_documento_cont, aRev.esercizio, aRev.cd_cds, aRev.utcr);
       else
           aRev.pg_reversale   := CNRCTB018.getNextNumDocCont(aRev.cd_tipo_documento_cont, aRev.esercizio, cnrctb020.getCDCDSENTE(aRev.esercizio), aRev.utcr);
       end if;
    for aInd in 1..aListaRev.count loop
        -- controllo se il l'accertamento specificata sulle righe ? su partita di giro ed eventualemente aggiorno la riga della reversale di conseguenza
        begin
           select * into aAcc from accertamento where
                cd_cds = aListaRev(aInd).cd_cds
                  and esercizio = aListaRev(aInd).esercizio_accertamento
                  and esercizio_originale = aListaRev(aInd).esercizio_ori_accertamento
                  and pg_accertamento = aListaRev(aInd).pg_accertamento
             for update nowait;
        exception when NO_DATA_FOUND then
          IBMERR001.RAISE_ERR_GENERICO('Accertamento a cui collegare la riga di reversale non trovato');
        end;
        aListaRev(aInd).fl_pgiro:=aAcc.fl_pgiro;
        end loop;
  -- Impostazione dello STATO_COGE - se non impostato viene messo a X
      if aRev.STATO_COGE is null then
       aRev.STATO_COGE := CNRCTB100.STATO_COEP_EXC;
    end if;
 -- Troncamento della data di registrazione
    aRev.dt_emissione:=trunc(aRev.dt_emissione);
 -- Impostazione di UTUV DUVA e PG_VER_REC per la testata della reversale
      aRev.UTUV := aRev.UTCR;
      aRev.DUVA := aRev.DUVA;
      aRev.PG_VER_REC := 1;
  -- Inserimento della testata della reversale
        cnrctb038.ins_REVERSALE(aRev);
      for i in 1..aListaRev.count loop
         aListaRev(i).pg_reversale:= aRev.pg_reversale;
           -- Impostazione di UTUV DUVA e PG_VER_REC
         aListaRev(i).UTUV:= aListaRev(i).UTCR;
         aListaRev(i).DUVA:= aListaRev(i).DACR;
         aListaRev(i).PG_VER_REC:= 1;
       -- Inserimento riga
        cnrctb038.ins_REVERSALE_RIGA(aListaRev(i));

              -- 23.05.2007 Stanislao Fusco
                        -- Inserita procedura che pu? inserire la riga automatica SIOPE
                        Inserisci_SIOPE_automatico (aListaRev(i));

      sCodiceTerzo := aListaRev(i).CD_TERZO;
    end loop;
      updScadAccertamento(aRev);
--DBMS_Output.PUT_LINE ('R 3');
      updSaldoCapitoliR(aRev,'I','A');
      sReversaleTerzo.CD_CDS := aRev.CD_CDS;
      sReversaleTerzo.CD_TERZO := sCodiceTerzo;
      sReversaleTerzo.CD_TIPO_BOLLO := aTipoBolloE.cd_tipo_bollo;
      sReversaleTerzo.DACR := aRev.DACR;
      sReversaleTerzo.DUVA := aRev.DUVA;
      sReversaleTerzo.ESERCIZIO := aRev.ESERCIZIO;
      sReversaleTerzo.PG_REVERSALE := aRev.PG_REVERSALE;
      sReversaleTerzo.PG_VER_REC := 1;
      sReversaleTerzo.UTCR := aRev.UTCR;
      sReversaleTerzo.UTUV := aRev.UTUV;
      cnrctb038.ins_REVERSALE_TERZO(sReversaleTerzo);
    cnrctb300.leggiMandatoReversale(aRev.CD_CDS,
                                      aRev.ESERCIZIO,
                          aRev.PG_REVERSALE,
                          'REV',
                          'I',
                          aRev.utuv);
  end if;
 end;


 -- crea il dettaglio di un sospeso di entrata associato alla reversale
 -- e aggiorna l'importo associato della testata del sospeso

procedure GENERADETT_ETR_SOSPESO(
   aRev IN  reversale%rowtype,
   aSospeso IN OUT sospeso%rowtype,
   aUser varchar2
 ) is
   aSospesoDet sospeso_det_etr%rowtype;
   aTSNow date;
 begin

   aTSNow:=sysdate;

   begin
   select * into aSospeso from sospeso a
   where
        cd_cds = aSospeso.cd_cds
        and esercizio = aSospeso.esercizio
      and ti_entrata_spesa = aSospeso.ti_entrata_spesa
    and ti_sospeso_riscontro = aSospeso.ti_sospeso_riscontro
    and cd_sospeso = aSospeso.cd_sospeso
     for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Il sospeso non ? stato trovato');
  end;

  if (aSospeso.im_sospeso - aSospeso.im_associato) < aRev.im_reversale then
     IBMERR001.RAISE_ERR_GENERICO('La disponibilit? del sospeso ? inferiore all''importo della reversale');
  end if;

   aSospesoDet.CD_CDS:=aSospeso.cd_cds;
   aSospesoDet.ESERCIZIO:=aRev.esercizio;
   aSospesoDet.PG_REVERSALE:=aRev.pg_reversale;
   aSospesoDet.TI_ENTRATA_SPESA:=aSospeso.ti_entrata_spesa;
   aSospesoDet.TI_SOSPESO_RISCONTRO:=aSospeso.ti_sospeso_riscontro;
   aSospesoDet.CD_SOSPESO:=aSospeso.cd_sospeso;
   aSospesoDet.IM_ASSOCIATO:=aRev.im_reversale;
   aSospesoDet.STATO:=cnrctb038.STATO_SOSPESO_DET_DEFAULT;
   aSospesoDet.DACR:=aTSNow;
   aSospesoDet.UTCR:=aUser;
   aSospesoDet.UTUV:=aUser;
   aSospesoDet.DUVA:=aTSNow;
   aSospesoDet.PG_VER_REC:=1;
   aSospesoDet.CD_CDS_REVERSALE:=aRev.cd_cds;
   cnrctb038.INS_SOSPESO_DET_ETR( aSospesoDet );

   update sospeso
      set
     im_associato = im_associato+ aRev.im_reversale,cd_cds_origine =aRev.cd_cds,
     utuv=aUser,
     duva=aTSNow,
     pg_ver_rec=pg_ver_rec+1
   where
      cd_cds = aSospeso.cd_cds
      and esercizio = aSospeso.esercizio
    and ti_entrata_spesa = aSospeso.ti_entrata_spesa
    and ti_sospeso_riscontro = aSospeso.ti_sospeso_riscontro
    and cd_sospeso = aSospeso.cd_sospeso;
   end;

   -- Non aggiorna l'importo associato a DOC amm su accertamento
  procedure modificaRevProvvPgiro(aRev IN OUT reversale%rowtype, aTSNow date, aUser varchar2) is
   aLocRev reversale%rowtype;
   aLocRevRiga reversale_riga%rowtype;
   aAcc accertamento%rowtype;
   aSaldo voce_f_saldi_cmp%rowtype;
   aSaldoCdrLinea voce_f_saldi_cdr_linea%Rowtype;
   parametriCnr parametri_cnr%rowtype;
  begin
    begin
   select * into aLocRev from reversale where
        cd_cds = aRev.cd_cds
      and esercizio = aRev.esercizio
      and pg_reversale = aRev.pg_reversale
   for update nowait;
   select * into aLocRevRiga from reversale_riga where
        cd_cds = aRev.cd_cds
      and esercizio = aRev.esercizio
      and pg_reversale = aRev.pg_reversale
   for update nowait;
    exception
   when NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Reversale non trovata');
   when TOO_MANY_ROWS then
        IBMERR001.RAISE_ERR_GENERICO('La reversale provvisoria con pi? righe non ? modificabile');
  end;

    begin
     select * into aAcc from accertamento where
           cd_cds = aLocRevRiga.cd_cds
       and esercizio = aLocRevRiga.esercizio_accertamento
       and esercizio_originale = aLocRevRiga.esercizio_ori_accertamento
       and pg_accertamento = aLocRevRiga.pg_accertamento
       and fl_pgiro = 'Y'
   for update nowait;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro a cui collegare la riga di reversale non trovato');
    end;

  update reversale set
   im_reversale = aRev.im_reversale,
   duva=aTSNow,
   utuv=aUser,
   pg_ver_rec=pg_ver_rec+1
  where cd_cds = aRev.cd_cds
    and esercizio = aRev.esercizio
    and pg_reversale = aRev.pg_reversale;

  update reversale_riga set
   im_reversale_riga = aRev.im_reversale,
   duva=aTSNow,
   utuv=aUser,
   pg_ver_rec=pg_ver_rec+1
  where cd_cds = aRev.cd_cds
    and esercizio = aRev.esercizio
    and pg_reversale = aRev.pg_reversale;

  update reversale_siope set
   importo = aRev.im_reversale,
   duva=aTSNow,
   utuv=aUser,
   pg_ver_rec=pg_ver_rec+1
  where cd_cds = aRev.cd_cds
    and esercizio = aRev.esercizio
    and pg_reversale = aRev.pg_reversale;
    if(parametriCnr.fl_nuovo_pdg='N') then
        begin
         select * into aSaldo from voce_f_saldi_cmp where
             CD_CDS=aAcc.cd_cds
         and ESERCIZIO=aAcc.esercizio
         and TI_APPARTENENZA=aAcc.ti_appartenenza
         and TI_GESTIONE=aAcc.ti_gestione
         and CD_VOCE=aAcc.cd_voce
         and TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
        for update nowait;
       exception when NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Capitolo finanziario non trovato:'||aAcc.cd_voce);
       end;

   -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
   CNRCTB054.aggiornaSaldi(aSaldo,'A',aRev.im_reversale-aLocRev.im_reversale,aUser,aTSNow);
   end if;
   for aListaScad in (select * from v_mandato_reversale_scad_voce
                     Where esercizio = aRev.esercizio
           And cd_cds = aRev.cd_cds
           And pg_documento = aRev.pg_reversale
           And ti_documento = 'R') loop
   Begin
    -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
    aSaldoCdrLinea.ESERCIZIO := aListaScad.ESERCIZIO;
    aSaldoCdrLinea.ESERCIZIO_RES := aListaScad.ESERCIZIO_ORIGINALE;
    aSaldoCdrLinea.CD_CENTRO_RESPONSABILITA := aListaScad.CD_CENTRO_RESPONSABILITA;
    aSaldoCdrLinea.CD_LINEA_ATTIVITA := aListaScad.CD_LINEA_ATTIVITA;
    aSaldoCdrLinea.TI_APPARTENENZA := aListaScad.TI_APPARTENENZA;
    aSaldoCdrLinea.TI_GESTIONE := aListaScad.TI_GESTIONE;
    aSaldoCdrLinea.CD_VOCE := aListaScad.CD_VOCE;

    CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLinea);

    If aListaScad.CD_TIPO_DOCUMENTO_CONT In (CNRCTB018.TI_DOC_OBB, CNRCTB018.TI_DOC_OBB_RES_PRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES,
                                             CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_IMP, CNRCTB018.TI_DOC_IMP_RES,
                                             CNRCTB018.TI_DOC_ACC_RES, CNRCTB018.TI_DOC_ACC_PGIRO, CNRCTB018.TI_DOC_ACC_PGIRO_RES,
                                             CNRCTB018.TI_DOC_ACC_SIST, CNRCTB018.TI_DOC_ACC) Then
         aSaldocdrlinea.IM_MANDATI_REVERSALI_PRO := aListaScad.IM_VOCE;
    Elsif aListaScad.CD_TIPO_DOCUMENTO_CONT = CNRCTB018.TI_DOC_OBB_RES_IMPRO Then
         aSaldocdrlinea.IM_MANDATI_REVERSALI_IMP := aListaScad.IM_VOCE;
    End If;

    aSaldoCdrLinea.UTUV := aUser;

    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLinea, '037.modificaRevProvvPgiro', 'N');
   End;
   End Loop;
  end;

 Procedure generaRevProvvPgiro(aRev IN OUT reversale%rowtype, aRevRiga IN OUT reversale_riga%rowtype, aTSNow date, aUser varchar2) is
   aReversaleTerzo reversale_terzo%rowtype;
   sCodiceTerzo number(8);
   aTipoBolloE tipo_bollo%rowtype;
   aAcc accertamento%rowtype;
   tesoreria_unica char(1):='N';
  begin
    begin
        select * into aTipoBolloE from tipo_bollo where
            ti_entrata_spesa in ('*',CNRCTB001.GESTIONE_ENTRATE)
      and fl_cancellato='N'
      and fl_default='Y';
    exception when NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Tipo bollo di default non trovato');
    end;

    begin
       select fl_tesoreria_unica into tesoreria_unica from parametri_cnr
       where
            esercizio =aRev.esercizio;

    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Configurazione gestione tesoreria mancante');
    end;
    if aRev.cd_tipo_documento_cont is null or aRev.cd_tipo_documento_cont <> CNRCTB018.TI_DOC_REV_PROVV then
     IBMERR001.RAISE_ERR_GENERICO('Il tipo di reversale non ? specificato o non ? il tipo provvisorio');
    end if;
    if(tesoreria_unica='N') then
           aRev.pg_reversale   := CNRCTB018.getNextNumDocCont(aRev.cd_tipo_documento_cont, aRev.esercizio, aRev.cd_cds, aRev.utcr);
       else
           aRev.pg_reversale   := CNRCTB018.getNextNumDocCont(aRev.cd_tipo_documento_cont, aRev.esercizio, cnrctb020.getCDCDSENTE(aRev.esercizio), aRev.utcr);
       end if;

    -- controllo se il l'accertamento specificata sulle righe ? su partita di giro ed eventualemente aggiorno la riga della reversale di conseguenza
    begin
     select * into aAcc from accertamento where
           cd_cds = aRevRiga.cd_cds
       and esercizio = aRevRiga.esercizio_accertamento
       and esercizio_originale = aRevRiga.esercizio_ori_accertamento
       and pg_accertamento = aRevRiga.pg_accertamento
       and fl_pgiro = 'Y'
   for update nowait;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro a cui collegare la riga di reversale non trovato');
    end;
  -- Impostazione dello STATO_COGE - se non impostato viene messo a X
  if aRev.STATO_COGE is null then
   aRev.STATO_COGE := CNRCTB100.STATO_COEP_EXC;
  end if;
  -- Troncamento della data di registrazione
  aRev.dt_emissione:=trunc(aRev.dt_emissione);
  -- Impostazione di UTUV DUVA e PG_VER_REC per la testata della reversale
    aRev.UTUV := aRev.UTCR;
    aRev.DUVA := aRev.DUVA;
    aRev.PG_VER_REC := 1;
  -- Inserimento della testata della reversale
    cnrctb038.ins_REVERSALE(aRev);
    aRevRiga.pg_reversale:= aRev.pg_reversale;
  -- Impostazione di UTUV DUVA e PG_VER_REC
  aRevRiga.UTUV:= aRevRiga.UTCR;
  aRevRiga.DUVA:= aRevRiga.DACR;
  aRevRiga.PG_VER_REC:= 1;
    -- Inserimento riga
  cnrctb038.ins_REVERSALE_RIGA(aRevRiga);

        -- 23.05.2007 Stanislao Fusco
        -- Inserita procedura che pu? inserire la riga automatica SIOPE
        Inserisci_SIOPE_automatico (aRevRiga);

  sCodiceTerzo := aRevRiga.CD_TERZO;
    updScadAccertamento(aRev);
--DBMS_Output.PUT_LINE ('R 4');
    updSaldoCapitoliR(aRev,'I','A');
    aReversaleTerzo.CD_CDS := aRev.CD_CDS;
    aReversaleTerzo.CD_TERZO := sCodiceTerzo;
    aReversaleTerzo.CD_TIPO_BOLLO := aTipoBolloE.cd_tipo_bollo;
    aReversaleTerzo.DACR := aRev.DACR;
    aReversaleTerzo.DUVA := aRev.DUVA;
    aReversaleTerzo.ESERCIZIO := aRev.ESERCIZIO;
    aReversaleTerzo.PG_REVERSALE := aRev.PG_REVERSALE;
    aReversaleTerzo.PG_VER_REC := 1;
    aReversaleTerzo.UTCR := aRev.UTCR;
    aReversaleTerzo.UTUV := aRev.UTUV;
    cnrctb038.ins_REVERSALE_TERZO(aReversaleTerzo);
  end;


Procedure Inserisci_SIOPE_automatico (aNewManRiga In mandato_riga%Rowtype) Is
   aEV                  elemento_voce%Rowtype;
   aAnagrafico          anagrafico%Rowtype;
   aCD_SIOPE            codici_siope.cd_SIOPE%Type;
   aMandato_SIOPE       mandato_siope%rowtype;
   aRecLiqIVA           LIQUIDAZIONE_IVA%Rowtype;
   aRecManAccreditamento  MANDATO%Rowtype;
   MY_CHIAVE_SECONDARIA CONFIGURAZIONE_CNR.CD_CHIAVE_SECONDARIA%Type;
   aObb                 obbligazione%Rowtype;

Begin -- DELLA PROCEDURA

-- recupero tutta l'obbligazione della riga del mandato (SERVE DOPO IN DIVERSI PUNTI)

    Select * Into aObb
    From  obbligazione
    Where cd_cds              = aNewManRiga.cd_cds And
          esercizio           = aNewManRiga.ESERCIZIO_OBBLIGAZIONE And
          esercizio_originale = aNewManRiga.ESERCIZIO_ORI_OBBLIGAZIONE And
          pg_obbligazione     = aNewManRiga.PG_OBBLIGAZIONE;

-- ============================================================================================
--              SE SI TRATTA DI MANDATI DI ACCREDITAMENTO I CODICI SONO FISSI (CONF_CNR)
-- ============================================================================================

Begin
  Select *
  Into  aRecManAccreditamento
  From  Mandato
  Where CD_CDS = aNewManRiga.CD_CDS And
        ESERCIZIO = aNewManRiga.ESERCIZIO And
        PG_MANDATO = aNewManRiga.PG_MANDATO And
        TI_MANDATO = CNRCTB038.TI_MAN_ACCRED;

  MY_CHIAVE_SECONDARIA := 'MANDATO_ACCREDITAMENTO';
  aCD_SIOPE := CNRCTB015.getVal01PerChiave(aNewManRiga.esercizio, 'CODICE_SIOPE_DEFAULT', MY_CHIAVE_SECONDARIA);

If aCD_SIOPE Is Not Null Then
  Dbms_Output.PUT_LINE ('MANDATO '||aNewManRiga.CD_CDS||' '||aNewManRiga.ESERCIZIO||' '||aNewManRiga.PG_MANDATO||' '||
                      ' Riga '||aNewManRiga.DS_MANDATO_RIGA||' - ACCREDITAMENTO - CODICE SIOPE FISSO '||aCD_SIOPE);
End If;

Exception
  When No_Data_Found Then aCD_SIOPE := Null;
End;

-- ======================================================================================================
--  SE SI TRATTA DI MANDATI DI LIQUIDAZIONE IVA COMMERCIALE/ISTITUZIONALE I CODICI SONO FISSI (CONF_CNR)
-- ======================================================================================================

If aCD_SIOPE Is Null Then

 Begin
    -- Cerco il doc. amm. della riga di mandato su LIQUIDAZIONE_IVA

    Select  *
    Into    aRecLiqIVA
    From    LIQUIDAZIONE_IVA
    Where   CD_TIPO_DOCUMENTO = aNewManRiga.CD_TIPO_DOCUMENTO_AMM And
            CD_CDS_DOC_AMM    = aNewManRiga.CD_CDS_DOC_AMM        And
            CD_UO_DOC_AMM     = aNewManRiga.CD_UO_DOC_AMM         And
            ESERCIZIO_DOC_AMM = aNewManRiga.ESERCIZIO_DOC_AMM     And
            PG_DOC_AMM        = aNewManRiga.PG_DOC_AMM            And
            STATO             = 'D';

    If aRecLiqIVA.CD_CDS = CNRCTB020.GETCDCDSENTE (aNewManRiga.esercizio) Then
       If aRecLiqIVA.TIPO_LIQUIDAZIONE = 'C' Then
            MY_CHIAVE_SECONDARIA := 'MANDATI_SAC_IVA_COMMERCIALE';
       Elsif aRecLiqIVA.TIPO_LIQUIDAZIONE In ('I', 'S', 'X') Then
            MY_CHIAVE_SECONDARIA := 'MANDATI_SAC_IVA_ISTITUZIONALE';
       Elsif aRecLiqIVA.TIPO_LIQUIDAZIONE In ('P') Then
            MY_CHIAVE_SECONDARIA := 'MANDATI_SAC_IVA_SPLIT';
       End If;
    Else
       If aRecLiqIVA.TIPO_LIQUIDAZIONE = 'C' Then
            MY_CHIAVE_SECONDARIA := 'MANDATI_ISTITUTI_IVA_COMMERCIALE';
       Elsif aRecLiqIVA.TIPO_LIQUIDAZIONE In ('I', 'S', 'X', 'P') Then
            MY_CHIAVE_SECONDARIA := 'MANDATI_ISTITUTI_IVA_ISTITUZIONALE';
       End If;
    End If;

    -- se c'? recupero il Codice SIOPE da Configuazione in base al tipo di Liquidazione

    aCD_SIOPE := CNRCTB015.getVal01PerChiave(aNewManRiga.esercizio, 'CODICE_SIOPE_DEFAULT', MY_CHIAVE_SECONDARIA);

If aCD_SIOPE Is Not Null Then
  Dbms_Output.PUT_LINE ('MANDATO '||aNewManRiga.CD_CDS||' '||aNewManRiga.ESERCIZIO||' '||aNewManRiga.PG_MANDATO||' '||
                      ' Riga '||aNewManRiga.DS_MANDATO_RIGA||' - '||MY_CHIAVE_SECONDARIA||' - CODICE SIOPE FISSO '||aCD_SIOPE);
End If;

 Exception
   When No_Data_Found Then aCD_SIOPE := Null;
 End;

End If; -- SIOPE ANCORA NULL

-- ==========================================================================================================
--    SE SI TRATTA DI TRASFERIMENTI O VERSAMENTI DI RITENUTE RECUPERO IL CODICE SIOPE DAL CODICE TRIBUTO
-- ==========================================================================================================

If aCD_SIOPE Is Null Then

 Begin

--Dbms_Output.PUT_LINE ('TIPO OBB '||aObb.CD_TIPO_DOCUMENTO_CONT||' '||
                    --' OBB '||aObb.CD_CDS||' '||aObb.ESERCIZIO||' '||aObb.ESERCIZIO_ORIGINALE||' '||aObb.PG_OBBLIGAZIONE||
                    --' OBB ORIGINALE '||aObb.CD_CDS_ORI_RIPORTO||' '||aObb.ESERCIZIO_ORI_RIPORTO||' '||aObb.ESERCIZIO_ORI_ORI_RIPORTO||' '||aObb.PG_OBBLIGAZIONE_ORI_RIPORTO);

    If aObb.CD_TIPO_DOCUMENTO_CONT = cnrctb018.TI_DOC_OBB_PGIRO Then

    -- se ? una partita di giro di competenza cerco il codice siope associato al codice tributo
    -- che trovo su contributo ritenuta con la partita di giro di entrata collegata a quella di spesa
    -- del mandato di versamento

       Select ASS.CD_SIOPE_S
       Into   aCD_SIOPE
       From   ASS_TIPO_CONTR_RITENUTA_SIOPE ASS, CONTRIBUTO_RITENUTA CR, ASS_OBB_ACR_PGIRO ASSPGIRO
       Where  ASSPGIRO.CD_CDS                     = aObb.CD_CDS And
              ASSPGIRO.ESERCIZIO                  = aObb.ESERCIZIO And
              ASSPGIRO.ESERCIZIO_ORI_OBBLIGAZIONE = aObb.ESERCIZIO_ORIGINALE And
              ASSPGIRO.PG_OBBLIGAZIONE            = aObb.PG_OBBLIGAZIONE And
              CR.CD_CDS_ACCERTAMENTO              = ASSPGIRO.CD_CDS And
              CR.ESERCIZIO_ACCERTAMENTO           = ASSPGIRO.ESERCIZIO And
              CR.ESERCIZIO_ORI_ACCERTAMENTO       = ASSPGIRO.ESERCIZIO_ORI_ACCERTAMENTO And
              CR.PG_ACCERTAMENTO                  = ASSPGIRO.PG_ACCERTAMENTO And
              CR.CD_CONTRIBUTO_RITENUTA           = ASS.CD_CONTRIBUTO_RITENUTA And
              CR.DT_INI_VALIDITA                  = ASS.DT_INI_VALIDITA And
              ASS.ESERCIZIO                       = aNewManRiga.ESERCIZIO;

    Elsif aObb.CD_TIPO_DOCUMENTO_CONT = cnrctb018.TI_DOC_OBB_PGIRO_RES Then

    -- se ? una partita di giro residua cerco il codice siope associato al codice tributo
    -- che trovo su contributo ritenuta con la partita di giro di entrata collegata a quella di spesa
    -- (dell'anno precedente, cio? originale) del mandato di versamento

       Select ASS.CD_SIOPE_S
       Into   aCD_SIOPE
       From   ASS_TIPO_CONTR_RITENUTA_SIOPE ASS, CONTRIBUTO_RITENUTA CR, ASS_OBB_ACR_PGIRO ASSPGIRO
       Where  ASSPGIRO.CD_CDS                     = aObb.CD_CDS_ORI_RIPORTO And
              ASSPGIRO.ESERCIZIO                  = aObb.ESERCIZIO_ORI_RIPORTO And
              ASSPGIRO.ESERCIZIO_ORI_OBBLIGAZIONE = aObb.ESERCIZIO_ORI_ORI_RIPORTO And
              ASSPGIRO.PG_OBBLIGAZIONE            = aObb.PG_OBBLIGAZIONE_ORI_RIPORTO And
              CR.CD_CDS_ACCERTAMENTO              = ASSPGIRO.CD_CDS And
              CR.ESERCIZIO_ACCERTAMENTO           = ASSPGIRO.ESERCIZIO And
              CR.ESERCIZIO_ORI_ACCERTAMENTO       = ASSPGIRO.ESERCIZIO_ORI_ACCERTAMENTO And
              CR.PG_ACCERTAMENTO                  = ASSPGIRO.PG_ACCERTAMENTO And
              CR.CD_CONTRIBUTO_RITENUTA           = ASS.CD_CONTRIBUTO_RITENUTA And
              CR.DT_INI_VALIDITA                  = ASS.DT_INI_VALIDITA And
              ASS.ESERCIZIO                       = aNewManRiga.ESERCIZIO;

    End If;

If aCD_SIOPE Is Not Null Then
Dbms_Output.PUT_LINE ('MANDATO '||aNewManRiga.CD_CDS||' '||aNewManRiga.ESERCIZIO||' '||aNewManRiga.PG_MANDATO||' '||
                      ' Riga '||aNewManRiga.DS_MANDATO_RIGA||
--                    ' OBB '||aObb.CD_CDS||' '||aObb.ESERCIZIO||' '||aObb.ESERCIZIO_ORIGINALE||' '||aObb.PG_OBBLIGAZIONE||
--                    ' OBB ORIGINALE '||aObb.CD_CDS_ORI_RIPORTO||' '||aObb.ESERCIZIO_ORI_RIPORTO||' '||aObb.ESERCIZIO_ORI_ORI_RIPORTO||' '||aObb.PG_OBBLIGAZIONE_ORI_RIPORTO||
                      ' - VERSAMENTO RITENUTE - CODICE SIOPE FISSO '||Nvl(aCD_SIOPE, 'nullo'));
End If;

 Exception
    When No_Data_Found Then aCD_SIOPE := Null;
 End;

End If; -- SIOPE ANCORA NULL

-- ==========================================================================================================
--                              RECUPERO IL CODICE SIOPE DALL'ELEMENTO VOCE
-- ==========================================================================================================

If aCD_SIOPE Is Null Then

 Begin
       -- determino l'elemento voce dell'impegno

       Select EV.*
       Into   aEV
       From   elemento_voce EV
       Where  aObb.ESERCIZIO           = EV.ESERCIZIO       And
              aObb.TI_APPARTENENZA     = EV.TI_APPARTENENZA And
              aObb.TI_GESTIONE         = EV.TI_GESTIONE     And
              aObb.CD_ELEMENTO_VOCE    = EV.CD_ELEMENTO_VOCE;

       -- determino l'ANAGRAFICA del mandato per poter fare il controllo di congruit? della tipologia ISTAT

       Select anagrafico.*
       Into   aAnagrafico
       From   ANAGRAFICO, TERZO
       Where  aNewManRiga.cd_terzo = terzo.cd_terzo And
              terzo.cd_anag        = anagrafico.cd_anag;

       If aEv.FL_CHECK_TERZO_SIOPE = 'Y' Then

          Select CS.CD_SIOPE
          Into   aCD_SIOPE
          From   ASS_EV_SIOPE ASS, CODICI_SIOPE CS, ASS_TIPOLOGIA_ISTAT_SIOPE ASS_TIPO
          Where  ASS.ESERCIZIO               = aEV.ESERCIZIO        And
                 ASS.TI_APPARTENENZA         = aEV.TI_APPARTENENZA  And
                 ASS.TI_GESTIONE             = aEV.TI_GESTIONE      And
                 ASS.CD_ELEMENTO_VOCE        = aEV.CD_ELEMENTO_VOCE And
                 ASS.ESERCIZIO_SIOPE         = CS.ESERCIZIO         And
                 ASS.TI_GESTIONE_SIOPE       = CS.TI_GESTIONE       And
                 ASS.CD_SIOPE                = CS.CD_SIOPE          And
                 ASS_TIPO.ESERCIZIO_SIOPE    = CS.ESERCIZIO         And
                 ASS_TIPO.TI_GESTIONE_SIOPE  = CS.TI_GESTIONE       And
                 ASS_TIPO.CD_SIOPE           = CS.CD_SIOPE          And
                 ASS_TIPO.PG_TIPOLOGIA       = aAnagrafico.PG_TIPOLOGIA;

       Elsif aEv.FL_CHECK_TERZO_SIOPE = 'N' Then

          Select CD_SIOPE
          Into   aCD_SIOPE
          From   ASS_EV_SIOPE
          Where  ESERCIZIO        = aEV.ESERCIZIO And
                 TI_APPARTENENZA  = aEV.TI_APPARTENENZA And
                 TI_GESTIONE      = aEV.TI_GESTIONE And
                 CD_ELEMENTO_VOCE = aEV.CD_ELEMENTO_VOCE;
                  --And                  Rownum < 2; -- da togliere

       End If;

       Exception
         -- SE TROVO PIU' DI UN CODICE SIOPE DISPONIBILE (O NESSUNO) NON INSERISCO NULLA IN AUTOMATICO
         -- E SE LA PIANGE L'UTENTE

         When Too_Many_Rows Then aCD_SIOPE := Null;
         When No_Data_Found Then aCD_SIOPE := Null;
       End; -- END RICERCA SIOPE SU ELEMENTO_VOCE

If aCD_SIOPE Is Not Null Then
  Dbms_Output.PUT_LINE ('MANDATO '||aNewManRiga.CD_CDS||' '||aNewManRiga.ESERCIZIO||' '||aNewManRiga.PG_MANDATO||' '||
                      ' Riga '||aNewManRiga.DS_MANDATO_RIGA||
                      ' - RECUPERO TRADIZIONALE DA ELEMENTO VOCE - CODICE SIOPE '||aCD_SIOPE);
End If;

End If; -- SIOPE ANCORA VUOTO


-- ===================================================================================================
-- ALLA FINE, DOPO AVER DETERMINATO IL CODICE SIOPE DEL CASO (E SOLO SE E' IN GRADO DI DETERMINARLO),
-- EFFETTUA L'INSERIMENTO VERO E PROPRIO
-- ===================================================================================================

If aCD_SIOPE Is Not Null Then
   aMandato_SIOPE.CD_CDS                      := aNewManRiga.CD_CDS;
   aMandato_SIOPE.ESERCIZIO                   := aNewManRiga.ESERCIZIO;
   aMandato_SIOPE.PG_MANDATO                  := aNewManRiga.PG_MANDATO;
   aMandato_SIOPE.ESERCIZIO_OBBLIGAZIONE      := aNewManRiga.ESERCIZIO_OBBLIGAZIONE;
   aMandato_SIOPE.ESERCIZIO_ORI_OBBLIGAZIONE  := aNewManRiga.ESERCIZIO_ORI_OBBLIGAZIONE;
   aMandato_SIOPE.PG_OBBLIGAZIONE             := aNewManRiga.PG_OBBLIGAZIONE;
   aMandato_SIOPE.PG_OBBLIGAZIONE_SCADENZARIO := aNewManRiga.PG_OBBLIGAZIONE_SCADENZARIO;
   aMandato_SIOPE.CD_CDS_DOC_AMM              := aNewManRiga.CD_CDS_DOC_AMM;
   aMandato_SIOPE.CD_UO_DOC_AMM               := aNewManRiga.CD_UO_DOC_AMM;
   aMandato_SIOPE.ESERCIZIO_DOC_AMM           := aNewManRiga.ESERCIZIO_DOC_AMM;
   aMandato_SIOPE.CD_TIPO_DOCUMENTO_AMM       := aNewManRiga.CD_TIPO_DOCUMENTO_AMM;
   aMandato_SIOPE.PG_DOC_AMM                  := aNewManRiga.PG_DOC_AMM;
   aMandato_SIOPE.ESERCIZIO_SIOPE             := aNewManRiga.ESERCIZIO;
   aMandato_SIOPE.TI_GESTIONE                 := CNRCTB001.GESTIONE_SPESE;
   aMandato_SIOPE.CD_SIOPE                    := aCD_SIOPE;
   aMandato_SIOPE.IMPORTO                     := aNewManRiga.IM_MANDATO_RIGA;
   aMandato_SIOPE.UTCR                        := aNewManRiga.UTCR;
   aMandato_SIOPE.DACR                        := aNewManRiga.DACR;
   aMandato_SIOPE.UTUV                        := aNewManRiga.UTUV;
   aMandato_SIOPE.DUVA                        := aNewManRiga.DUVA;
   aMandato_SIOPE.PG_VER_REC                  := aNewManRiga.PG_VER_REC;

   cnrctb038.ins_MANDATO_SIOPE(aMandato_SIOPE);

Else
  Dbms_Output.PUT_LINE ('MANDATO '||aNewManRiga.CD_CDS||' '||aNewManRiga.ESERCIZIO||' '||aNewManRiga.PG_MANDATO||' '||
                      ' Riga '||aNewManRiga.DS_MANDATO_RIGA||' NESSUN CODICE SIOPE RECUPERATO');
End If;

End; -- DELLA PROCEDURA


Procedure Inserisci_SIOPE_automatico (aNewRevRiga In reversale_riga%Rowtype) Is
   aEV                  Elemento_voce%Rowtype;
   aAnagrafico          Anagrafico%Rowtype;
   aCD_SIOPE            Codici_siope.cd_SIOPE%Type;
   aReversale_SIOPE     Reversale_siope%rowtype;
   aRecLiqIvaCentro     LIQUIDAZIONE_IVA_CENTRO%Rowtype;
   aRecRevTrasferimento REVERSALE%Rowtype;
   MY_CHIAVE_SECONDARIA CONFIGURAZIONE_CNR.CD_CHIAVE_SECONDARIA%Type;
   aAcc                 accertamento%Rowtype;

Begin -- DELLA PROCEDURA

-- recupero tutto l'accertamento della riga della reversale (SERVE DOPO IN DIVERSI PUNTI)

    Select * Into aAcc
    From  accertamento
    Where cd_cds              = aNewRevRiga.CD_CDS And
          esercizio           = aNewRevRiga.ESERCIZIO_ACCERTAMENTO And
          esercizio_originale = aNewRevRiga.ESERCIZIO_ORI_ACCERTAMENTO And
          pg_accertamento     = aNewRevRiga.PG_ACCERTAMENTO;

-- ============================================================================================
--              SE SI TRATTA DI REVERSALE DI TRASFERIMENTO I CODICI SONO FISSI (CONF_CNR)
-- ============================================================================================

Begin
  Select *
  Into aRecRevTrasferimento
  From Reversale
  Where CD_CDS = aNewRevRiga.CD_CDS
  And   ESERCIZIO = aNewRevRiga.ESERCIZIO
  And   PG_REVERSALE = aNewRevRiga.PG_REVERSALE
  And   TI_REVERSALE = 'A';

  MY_CHIAVE_SECONDARIA := 'REVERSALE_TRASFERIMENTO';
  aCD_SIOPE := CNRCTB015.getVal01PerChiave(aNewRevRiga.esercizio, 'CODICE_SIOPE_DEFAULT', MY_CHIAVE_SECONDARIA);

If aCD_SIOPE Is Not Null Then
  Dbms_Output.PUT_LINE ('REVERSALE '||aNewRevRiga.CD_CDS||' '||aNewRevRiga.ESERCIZIO||' '||aNewRevRiga.PG_REVERSALE
                        ||' '||' DESCR '||aNewRevRiga.DS_REVERSALE_RIGA||
                        ' REVERSALE_TRASFERIMENTO - CODICE SIOPE '||aCD_SIOPE);
End If;


Exception
  When No_Data_Found Then aCD_SIOPE := Null;
End;

-- ======================================================================================================
--  SE SI TRATTA DI REVERSALI DI LIQUIDAZIONE IVA COMMERCIALE/ISTITUZIONALE I CODICI SONO FISSI (CONF_CNR)
-- ======================================================================================================

If aCD_SIOPE Is Null Then

  Begin -- DEL RECUPERO DEL CODICE SIOPE

  Select  *
  Into    aRecLiqIvaCentro
  From    LIQUIDAZIONE_IVA_CENTRO
  Where   CD_CDS_OBB_ACCENTR        = aNewRevRiga.CD_CDS And
          ESERCIZIO_OBB_ACCENTR     = aNewRevRiga.ESERCIZIO_ACCERTAMENTO And
          PG_OBB_ACCENTR            = aNewRevRiga.PG_ACCERTAMENTO And
          ESERCIZIO_ORI_OBB_ACCENTR = aNewRevRiga.ESERCIZIO_ORI_ACCERTAMENTO And
          STATO             = 'C';

  If aRecLiqIvaCentro.TIPO_LIQUIDAZIONE = 'C' Then
          MY_CHIAVE_SECONDARIA := 'REVERSALI_SAC_IVA_COMMERCIALE';
  Elsif aRecLiqIvaCentro.TIPO_LIQUIDAZIONE In ('I', 'S' ,'X', 'P') Then
          MY_CHIAVE_SECONDARIA := 'REVERSALI_SAC_IVA_ISTITUZIONALE';
  End If;

  aCD_SIOPE := CNRCTB015.getVal01PerChiave(aNewRevRiga.esercizio, 'CODICE_SIOPE_DEFAULT', MY_CHIAVE_SECONDARIA);

If aCD_SIOPE Is Not Null Then
  Dbms_Output.PUT_LINE ('REVERSALE '||aNewRevRiga.CD_CDS||' '||aNewRevRiga.ESERCIZIO||' '||aNewRevRiga.PG_REVERSALE
                        ||' '||' DESCR '||aNewRevRiga.DS_REVERSALE_RIGA||MY_CHIAVE_SECONDARIA||
                        ' CODICE SIOPE '||aCD_SIOPE);
End If;


  Exception
    When No_Data_Found Then aCD_SIOPE := Null;
  End;

End If; -- SIOPE ANCORA NULL

-- ==========================================================================================================
--    SE SI TRATTA DI REVERSALI PER L'INCASSO DI TRASFERIMENTI DI RITENUTE RECUPERO IL CODICE SIOPE
--    DAL CODICE TRIBUTO
-- ==========================================================================================================

If aCD_SIOPE Is Null Then

 Begin

--Dbms_Output.PUT_LINE ('TIPO OBB '||aObb.CD_TIPO_DOCUMENTO_CONT||' '||
                    --' OBB '||aObb.CD_CDS||' '||aObb.ESERCIZIO||' '||aObb.ESERCIZIO_ORIGINALE||' '||aObb.PG_OBBLIGAZIONE||
                    --' OBB ORIGINALE '||aObb.CD_CDS_ORI_RIPORTO||' '||aObb.ESERCIZIO_ORI_RIPORTO||' '||aObb.ESERCIZIO_ORI_ORI_RIPORTO||' '||aObb.PG_OBBLIGAZIONE_ORI_RIPORTO);

    If aAcc.CD_TIPO_DOCUMENTO_CONT = cnrctb018.TI_DOC_ACC_PGIRO Then

    -- se ? una partita di giro di entrata di competenza cerco il codice siope associato al codice tributo
    -- che trovo su contributo ritenuta con la partita di giro che sta sulla riga di reversale

       Select ASS.CD_SIOPE_E
       Into   aCD_SIOPE
       From   ASS_TIPO_CONTR_RITENUTA_SIOPE ASS, CONTRIBUTO_RITENUTA CR
       Where  CR.CD_CDS_ACCERTAMENTO              = aAcc.CD_CDS And
              CR.ESERCIZIO_ACCERTAMENTO           = aAcc.ESERCIZIO And
              CR.ESERCIZIO_ORI_ACCERTAMENTO       = aAcc.ESERCIZIO_ORIGINALE And
              CR.PG_ACCERTAMENTO                  = aAcc.PG_ACCERTAMENTO And
              CR.CD_CONTRIBUTO_RITENUTA           = ASS.CD_CONTRIBUTO_RITENUTA And
              CR.DT_INI_VALIDITA                  = ASS.DT_INI_VALIDITA And
              ASS.ESERCIZIO                       = aNewRevRiga.ESERCIZIO;

    Elsif aAcc.CD_TIPO_DOCUMENTO_CONT = cnrctb018.TI_DOC_ACC_PGIRO_RES Then

    -- se ? una partita di giro residua cerco il codice siope associato al codice tributo
    -- che trovo su contributo ritenuta con la partita di giro di entrata (dell'anno precedente,
    -- cio? originale) della reversale

       Select ASS.CD_SIOPE_E
       Into   aCD_SIOPE
       From   ASS_TIPO_CONTR_RITENUTA_SIOPE ASS, CONTRIBUTO_RITENUTA CR
       Where  CR.CD_CDS_ACCERTAMENTO              = aAcc.CD_CDS And
              CR.ESERCIZIO_ACCERTAMENTO           = aAcc.ESERCIZIO And
              CR.ESERCIZIO_ORI_ACCERTAMENTO       = aAcc.ESERCIZIO_ORIGINALE And
              CR.PG_ACCERTAMENTO                  = aAcc.PG_ACCERTAMENTO And
              CR.CD_CONTRIBUTO_RITENUTA           = ASS.CD_CONTRIBUTO_RITENUTA And
              CR.DT_INI_VALIDITA                  = ASS.DT_INI_VALIDITA And
              ASS.ESERCIZIO                       = aNewRevRiga.ESERCIZIO;

    End If;

If aCD_SIOPE Is Not Null Then
  Dbms_Output.PUT_LINE ('REVERSALE '||aNewRevRiga.CD_CDS||' '||aNewRevRiga.ESERCIZIO||' '||aNewRevRiga.PG_REVERSALE
                        ||' '||' DESCR '||aNewRevRiga.DS_REVERSALE_RIGA||
                        ' RITENUTE CODICE SIOPE '||aCD_SIOPE);
End If;

/*If aCD_SIOPE Is Not Null Then
Dbms_Output.PUT_LINE ('MANDATO '||aNewManRiga.CD_CDS||' '||aNewManRiga.ESERCIZIO||' '||aNewManRiga.PG_MANDATO||' '||
                      ' Riga '||aNewManRiga.DS_MANDATO_RIGA||
--                    ' OBB '||aObb.CD_CDS||' '||aObb.ESERCIZIO||' '||aObb.ESERCIZIO_ORIGINALE||' '||aObb.PG_OBBLIGAZIONE||
--                    ' OBB ORIGINALE '||aObb.CD_CDS_ORI_RIPORTO||' '||aObb.ESERCIZIO_ORI_RIPORTO||' '||aObb.ESERCIZIO_ORI_ORI_RIPORTO||' '||aObb.PG_OBBLIGAZIONE_ORI_RIPORTO||
                      ' - VERSAMENTO RITENUTE - CODICE SIOPE FISSO '||Nvl(aCD_SIOPE, 'nullo'));
End If;
*/
 Exception
    When No_Data_Found Then aCD_SIOPE := Null;
 End;

End If; -- SIOPE ANCORA NULL


-- ==========================================================================================================
--                              RECUPERO IL CODICE SIOPE DALL'ELEMENTO VOCE
-- ==========================================================================================================

If aCD_SIOPE Is Null Then

     Begin

     -- determino l'elemento voce dell'impegno

     Select EV.*
     Into   aEV
     From   elemento_voce EV, accertamento ACC
     Where  ACC.cd_cds              = aNewRevRiga.cd_cds And
            ACC.ESERCIZIO           = aNewRevRiga.ESERCIZIO_ACCERTAMENTO And
            ACC.ESERCIZIO_ORIGINALE = aNewRevRiga.ESERCIZIO_ORI_ACCERTAMENTO And
            ACC.PG_ACCERTAMENTO     = aNewRevRiga.PG_ACCERTAMENTO And
            ACC.ESERCIZIO           = EV.ESERCIZIO       And
            ACC.TI_APPARTENENZA     = EV.TI_APPARTENENZA And
            ACC.TI_GESTIONE         = EV.TI_GESTIONE     And
            ACC.CD_ELEMENTO_VOCE    = EV.CD_ELEMENTO_VOCE;

     -- determino l'ANAGRAFICA del mandato per poter fare il controllo di congruit? della tipologia ISTAT

     Select anagrafico.*
     Into   aAnagrafico
     From   ANAGRAFICO, TERZO
     Where  aNewRevRiga.cd_terzo = terzo.cd_terzo And
            terzo.cd_anag        = anagrafico.cd_anag;

     If aEv.FL_CHECK_TERZO_SIOPE = 'Y' Then

        Select CS.CD_SIOPE
        Into   aCD_SIOPE
        From   ASS_EV_SIOPE ASS, CODICI_SIOPE CS, ASS_TIPOLOGIA_ISTAT_SIOPE ASS_TIPO
        Where  ASS.ESERCIZIO               = aEV.ESERCIZIO        And
               ASS.TI_APPARTENENZA         = aEV.TI_APPARTENENZA  And
               ASS.TI_GESTIONE             = aEV.TI_GESTIONE      And
               ASS.CD_ELEMENTO_VOCE        = aEV.CD_ELEMENTO_VOCE And
               ASS.ESERCIZIO_SIOPE         = CS.ESERCIZIO         And
               ASS.TI_GESTIONE_SIOPE       = CS.TI_GESTIONE       And
               ASS.CD_SIOPE                = CS.CD_SIOPE          And
               ASS_TIPO.ESERCIZIO_SIOPE    = CS.ESERCIZIO         And
               ASS_TIPO.TI_GESTIONE_SIOPE  = CS.TI_GESTIONE       And
               ASS_TIPO.CD_SIOPE           = CS.CD_SIOPE          And
               ASS_TIPO.PG_TIPOLOGIA       = aAnagrafico.PG_TIPOLOGIA;

     Elsif aEv.FL_CHECK_TERZO_SIOPE = 'N' Then

        Select CD_SIOPE
        Into   aCD_SIOPE
        From   ASS_EV_SIOPE
        Where  ESERCIZIO        = aEV.ESERCIZIO And
               TI_APPARTENENZA  = aEV.TI_APPARTENENZA And
               TI_GESTIONE      = aEV.TI_GESTIONE And
               CD_ELEMENTO_VOCE = aEV.CD_ELEMENTO_VOCE;

     End If;

If aCD_SIOPE Is Not Null Then
  Dbms_Output.PUT_LINE ('REVERSALE '||aNewRevRiga.CD_CDS||' '||aNewRevRiga.ESERCIZIO||' '||aNewRevRiga.PG_REVERSALE
                        ||' '||' DESCR '||aNewRevRiga.DS_REVERSALE_RIGA||
                        ' RECUPERO TRADIZIONALE '||aCD_SIOPE);
End If;


     Exception
       -- SE TROVO PIU' DI UN CODICE SIOPE DISPONIBILE (O NESSUNO) NON INSERISCO NULLA IN AUTOMATICO
       -- E SE LA PIANGE L'UTENTE

       When Too_Many_Rows Then aCD_SIOPE := Null;
       When No_Data_Found Then aCD_SIOPE := Null;
     End; -- SIOPE DA ELEMENTO_VOCE

End If;   -- SIOPE ANCORA VUOTO

-- ===================================================================================================
-- ALLA FINE, DOPO AVER DETERMINATO IL CODICE SIOPE DEL CASO (E SOLO SE E' IN GRADO DI DETERMINARLO),
-- EFFETTUA L'INSERIMENTO VERO E PROPRIO
-- ===================================================================================================

If aCD_SIOPE Is Not Null Then

      aReversale_SIOPE.CD_CDS                      := aNewRevRiga.CD_CDS;
      aReversale_SIOPE.ESERCIZIO                   := aNewRevRiga.ESERCIZIO;
      aReversale_SIOPE.PG_REVERSALE                := aNewRevRiga.PG_REVERSALE;
      aReversale_SIOPE.ESERCIZIO_ACCERTAMENTO      := aNewRevRiga.ESERCIZIO_ACCERTAMENTO;
      aReversale_SIOPE.ESERCIZIO_ORI_ACCERTAMENTO  := aNewRevRiga.ESERCIZIO_ORI_ACCERTAMENTO;
      aReversale_SIOPE.PG_ACCERTAMENTO             := aNewRevRiga.PG_ACCERTAMENTO;
      aReversale_SIOPE.PG_ACCERTAMENTO_SCADENZARIO := aNewRevRiga.PG_ACCERTAMENTO_SCADENZARIO;
      aReversale_SIOPE.CD_CDS_DOC_AMM              := aNewRevRiga.CD_CDS_DOC_AMM;
      aReversale_SIOPE.CD_UO_DOC_AMM               := aNewRevRiga.CD_UO_DOC_AMM;
      aReversale_SIOPE.ESERCIZIO_DOC_AMM           := aNewRevRiga.ESERCIZIO_DOC_AMM;
      aReversale_SIOPE.CD_TIPO_DOCUMENTO_AMM       := aNewRevRiga.CD_TIPO_DOCUMENTO_AMM;
      aReversale_SIOPE.PG_DOC_AMM                  := aNewRevRiga.PG_DOC_AMM;
      aReversale_SIOPE.ESERCIZIO_SIOPE             := aNewRevRiga.ESERCIZIO;
      aReversale_SIOPE.TI_GESTIONE                 := CNRCTB001.GESTIONE_ENTRATE;
      aReversale_SIOPE.CD_SIOPE                    := aCD_SIOPE;
      aReversale_SIOPE.IMPORTO                     := aNewRevRiga.IM_REVERSALE_RIGA;
      aReversale_SIOPE.UTCR                        := aNewRevRiga.UTCR;
      aReversale_SIOPE.DACR                        := aNewRevRiga.DACR;
      aReversale_SIOPE.UTUV                        := aNewRevRiga.UTUV;
      aReversale_SIOPE.DUVA                        := aNewRevRiga.DUVA;
      aReversale_SIOPE.PG_VER_REC                  := aNewRevRiga.PG_VER_REC;

      cnrctb038.ins_REVERSALE_SIOPE(aReversale_SIOPE);

End If;

End; -- DELLA PROCEDURA

PROCEDURE verificaTracciabilitaPag
   (aEs    NUMBER,
    aDataEmis DATE,
    aCdModPag VARCHAR2,
    aCdTipoDocAmm VARCHAR2,
    aImMandatoNetto NUMBER) IS
   aIm01   number;
   aDt01   date;
   aDt02   date;
   var     varchar2(1);

Begin
   if aDataEmis is null or aCdModPag is null or aCdTipoDocAmm is null or aImMandatoNetto is null then
    IBMERR001.RAISE_ERR_GENERICO('Alcuni parametri per la verifica della tracciabilit? dei pagamenti non sono valorizzati');
   end if;

   aIm01:= cnrctb015.getIm01PerChiave(0, cPKCONFIG, cSKCONFIG);
   aDt01:= cnrctb015.getDt01PerChiave(0, cPKCONFIG, cSKCONFIG);
   aDt02:= cnrctb015.getDt02PerChiave(0, cPKCONFIG, cSKCONFIG);
   if aIm01 is null or aDt01 is null or aDt02 is null then
    IBMERR001.RAISE_ERR_GENERICO('Parametri per la tracciabilit? dei pagamenti non definiti in CONFIGURAZIONE_CNR per esercizio: '||(0)||', chiave primaria: '||cPKCONFIG||', chiave secondaria: '||cSKCONFIG);
   end if;
--PIPE.SEND_MESSAGE('aImMandatoNetto = '||aImMandatoNetto);
  if aDataEmis < aDt01 OR aDataEmis > aDt02 OR aImMandatoNetto <= aIm01 then
    return;
  end if;
--PIPE.SEND_MESSAGE('aCdTipoDocAmm = '||aCdTipoDocAmm);
--PIPE.SEND_MESSAGE('aCdModPag = '||aCdModPag);
     Begin
        select '1'
        into var
        from ass_doc_amm_rif_mod
        where cd_tipo_documento_amm = aCdTipoDocAmm
          and cd_modalita_pag = aCdModPag;
     Exception
        when no_data_found then
            IBMERR001.RAISE_ERR_GENERICO('Non ? possibile procedere. Nel rispetto della tracciabilit? dei pagamenti, per la tipologia di documento: '||aCdTipoDocAmm||', non ? ammissibile la modalit? di pagamento: '||aCdModPag);
     End;
End;

end;
/


