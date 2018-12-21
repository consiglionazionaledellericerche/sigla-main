--------------------------------------------------------
--  DDL for Package CNRCTB037
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB037" as
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
-- Controllo di disponibilità di cassa non effettuato su righe di mandati di tipo partita di giro
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
-- Fix su calcolo della disponibilità di cassa globale del cds (allineamento con on-line)
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

 -- Controllo di disponibilità cassa del CDS

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
   aListaRev IN OUT CNRCTB038.righeReversaleList,
   isFromStipendi in boolean default false
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
