CREATE OR REPLACE package CNRCTB060 as
--
-- CNRCTB060 - Package di gestione dello scarico dei costi del personale su PDG
--
-- Date: 06/02/2004
-- Version: 4.10
--
-- Dependency: CNRCTB 000/020/010/050 IBMERR 001
--
-- History:
--
-- Date: 08/10/2001
-- Version: 1.0
-- Creazione
-- Date: 08/10/2001
-- Version: 1.1
-- Aggiunto metodo di inserimento Costi Del Dipendente
--
-- Date: 18/10/2001
-- Version: 1.2
-- Completamento gestione UO Personale
--
-- Date: 19/10/2001
-- Version: 1.3
-- Aggiunto metodo di controllo scarico completo di dipendenti CDR I livello
-- o CDR di II livello AREA
--
-- Date: 22/10/2001
-- Version: 1.4
-- Fix errori di annulamento costi del personale
-- Scrittura CDP su PDG per RUO e non piu per CDR
--
-- Date: 25/10/2001
-- Version: 1.5
-- Aggiunta commenti
--
-- Date: 26/10/2001
-- Version: 1.6
-- Modifica check scarico dipendenti
--
-- Date: 27/10/2001
-- Version: 1.7
-- Modifica check scarico dipendenti e condizioni di scarico
--
-- Date: 31/10/2001
-- Version: 1.8
-- fix errori
--
-- Date: 08/11/2001
-- Version: 1.9
-- Eliminazione esercizio da STO
--
-- Date: 16/11/2001
-- Version: 2.0
-- Fix errori
--
-- Date: 22/11/2001
-- Version: 2.1
-- Fix errori
--
-- Date: 03/12/2001
-- Version: 2.2
-- Corretto il meccanismo di estrazione del CDR del personale
--
-- Date: 06/12/2001
-- Version: 2.3
-- Fix quadratura costi + uso colonne OGC (obbligazioni in essere) invece di ODC (Obbligazioni da contrarre)
--
-- Date: 29/12/2001
-- Version: 2.4
-- Completamento specifiche di scarico in termini di colonne movimentate + gestione oneri e tfr
--
-- Date: 15/01/2002
-- Version: 2.5
-- Reset rowtype per riutilizzo
--
-- Date: 16/01/2002
-- Version: 2.6
-- Fix errori scarico su pdg
--
-- Date: 17/01/2002
-- Version: 2.7
-- Arricchita descrizione dettagli
--
-- Date: 30/01/2002
-- Version: 2.8
-- Fix scarico oneri cnr
--
-- Date: 13/02/2002
-- Version: 2.9
-- Fix errore CNR n. 62
--
-- Date: 27/03/2002
-- Version: 3.0
-- Richiesta 56R - Aggregazione per voce-cdr-la dello scarico stipendiale
--
-- Date: 28/03/2002
-- Version: 3.1
-- Fix richiesta 56R - Spaccati i dettagli per dip. a tempo determinato ed indeterminato
--
-- Date: 12/04/2002
-- Version: 3.2
-- Fix richiesta 137R - Dettagli di carico nel servente non piu READ ONLY
-- Fix richiesta 145E - Possibilita di effettuare carico/scarico CDP anche in stato D/E
--
-- Date: 15/04/2002
-- Version: 3.3
-- Fix richiesta 76R - Il controllo di scarico completo va applicato a tutti i CDR quando il CDR e quello del personale
--
-- Date: 18/04/2002
-- Version: 3.4
-- Introduzione natura e funzione su dettaglio PDG
--
-- Date: 30/04/2002
-- Version: 3.5
-- Verifica che i PDG con configurati costi del personale siano in stato di chiusura (C o F) per chiudere il PDG del personale
--
-- Date: 02/05/2002
-- Version: 3.6
-- Richiesta 71R -> controllo di scarico completo costi del personale: non esistono scarichi non confermati sull'UO del CDR
-- di primo livello o responsabile di area che chiude
--
-- Date: 17/05/2002
-- Version: 3.7
-- Fix mancata aggregazione scarichi TFR
--
-- Date: 20/05/2002
-- Version: 3.8
-- Controllo in fase di chiusura CDR personale che non esistano matricole non ancora configurate
--
-- Date: 19/06/2002
-- Version: 3.9
-- Controllo in fase di chiusura CDR RUO che non esistano matricole non ancora configurate nell'UO di cui il CDR e responsabile
--
-- Date: 18/07/2002
-- Version: 4.0
-- Aggiornamento della documentazione
--
-- Date: 02/08/2002
-- Version: 4.1
-- Controllo che non ci siano conf. di scarico con natura 5 e UO di afferenza del CDR non collegata ad AREA
--
-- Date: 07/08/2002
-- Version: 4.2
-- Fix su eliminazione dei dettagli stipendiali dell'UO del personale
--
-- Date: 21/09/2002
-- Version: 4.3
-- REVISIONE COMPLETA DELLO SCARICO PER AGGREGAZIONE VIA VISTE
--
-- Date: 22/09/2002
-- Version: 4.4
-- Fix per test su revisione
--
-- Date: 23/09/2002
-- Version: 4.5
-- Gestione del mese=0 per CDP previsionali
--
-- Date: 07/11/2002
-- Version: 4.6
-- Fix mese=0 in query su tabelle in cui tale campo viene introdotto
--
-- Date: 11/12/2002
-- Version: 4.7
-- Fix scarico ripetuto su CDR personale nel caso esistano scarichi di altri cdr gi? effettuati
--
-- Date: 08/01/2004
-- Version: 4.8
-- Gestione dei rotti in scarico costi del dipendente
--
-- Date: 05/02/2004
-- Version: 4.9
-- Rimosso controllo in checkScaricoCdpCompleto sul fatto che lo stato di tutti i CDR con ass_cdp_la siano CHIUSI (C o F)
--
-- Date: 06/02/2004
-- Version: 4.10
-- Aggiunti controlli su annullamento e scarico costi del personale sullo stato <> B dell'aggregato del CDR del personale
--
-- Constants:
--
-- Stato di scarico del dettaglio del personale su piano di gestione

STATO_CDP_NON_SCARICATO CONSTANT VARCHAR2(5) := 'I';
STATO_CDP_SCARICATO CONSTANT VARCHAR2(5) := 'S';

STATO_ALTRAUO_ACCETTATO CONSTANT VARCHAR2(5) := 'Y';
STATO_ALTRAUO_NONDEFINITO CONSTANT VARCHAR2(5) := 'X';
STATO_ALTRAUO_RIFIUTATO CONSTANT VARCHAR2(5) := 'N';

-- Denominazione dettaglio di spesa per scarico dati stipendiali su PDG
DESC_DETT CONSTANT VARCHAR2(50) := 'Dettaglio di spesa per stipendi';
DESC_DETT_TFR CONSTANT VARCHAR2(50) := 'Dettaglio di spesa tfr';
DESC_DETT_ONERI_CNR CONSTANT VARCHAR2(50) := 'Dettaglio di spesa oneri Cnr';

CDP_TI_RAPP_DETERMINATO CONSTANT VARCHAR2(10) :='DET';
CDP_TI_RAPP_INDETERMINATO CONSTANT VARCHAR2(10) :='IND';

TIPO_PREVENTIVO CONSTANT CHAR(1) :='P';
TIPO_CONSUNTIVO CONSTANT CHAR(1) :='C';


-- Legge tutti i PDG RUO/NRUO del CDR RUO aCDRRUO NO LOCK POSSIBILE
--
-- aEs -> Esercizio contabile
-- aCdCDRRUO -> Codice del centro di responsabilita RUO

 cursor PDG_CON_CONFIG_SCR(aEs number, aCdCDRRUO varchar2) RETURN PDG_PREVENTIVO%ROWTYPE is (
   select a.* from PDG_PREVENTIVO a, V_PDG_CDR_RUO_NRUO b where
        b.esercizio = aEs
    and b.cd_cdr_root = aCdCDRRUO
    and a.esercizio = aEs
    and a.cd_centro_responsabilita = b.cd_centro_responsabilita
    and exists (select 1 from ASS_CDP_LA where
         esercizio = aEs
     and mese=0
     and cd_centro_responsabilita = b.cd_centro_responsabilita)
 );

 -- Functions e Procedures:

-- Scarica sul PDG di aCdCdr i costi leggendo i dati da COSTO_DEL_DIPENDENTE
-- e utilizzando la configurazione impostata in ASS_CDP_LA per la ripartizione dei costi sulle linee di attivita
-- Il cdr di codice aCdCdr e un RUO
--
-- pre-post-name: Dipendente non scaricato completamente
-- pre: il totale di scarico configurato di qualche dipendendente dell'UO e inferiore a 100%
-- post: Genera un eccezione col messaggio "La configurazione di scarico dei dipendenti
--    dell'UO xxx.yy non e completa!
-- pre-post-name: Scarico verso altra UO non accettato ne confermato
-- pre: qualche scarico verso altra UO per qualche matricola dell'UO e in stato 'I' (iniziale)
-- post: Genera un eccezione col messaggio "La configurazione di scarico dei dipendenti
--    dell'UO xxx.yy non e completa!
-- pre-post-name: PDG gia chiuso
-- pre: Lo stato del PDG del CDR specificato e diverso da A o B, D o E
-- post: Genera eccezione
-- pre-post-name: Aggregato del CDR del personale chiuso
-- pre: Lo stato dell'aggregato del CDR del personale ? CHIUSO (stato 'B')
-- post: Genera eccezione
-- pre-post-name: PDG non ruo
-- pre: Il cdr specificato non e responsabile di unita organizzativa
-- post: Genera eccezione
-- pre-post-name: pdg del cdr personale gia chiuso
-- pre: Il pdg del cdr personale (codice = CONFIGURAZIONE_CNR,'CDR_SPECIALE','CDR_PERSONALE')
-- --    non e in stato A o B, D o E
-- post: Genera un eccezione col messaggio "Il piano di gestione del CDR del personale
--    xxx.yy.zz non e in stato iniziale o di pre-chiusura!"
-- pre-post-name: pdg cdr caricati gia chiusi
-- pre: Qualcuno dei cdr che compaiono in ASS_CDP_LA per le matricole appartenenti alla
--   uo del cdr specificato ha il pdg in stato diverso da A o B, D o E
-- post: Genera un'eccezione col messaggio "Il piano di gestione del CDR NRUO xxx.yy.zz
--     non e in stato iniziale o di pre-chiusura!"
-- pre-post-name: scarico gia effettuato
-- pre: Lo scarico dei costi e gia stato effettuato sul cdr specificato
-- post: Genera una eccezione con messaggio "Operazione gia effettuata su cdr"
-- pre-post-name: l'UO di afferenza del CDR RUO non e collegata ad AREA ed esiste conf di scarico con LA di natura 5
-- pre: Esiste una configurazione di scarico per CDR afferente ad UO non collegata ad Area di Ricerca e LA con natura = 5
-- post: Viene sollevata un'eccezione
-- pre-post-name: UO Personale
-- pre: il cdr specficato e il cdr del personale (codice = CONFIGURAZIONE_CNR,'CDR_SPECIALE','CDR_PERSONALE') oppure
--      la matricola e di dipendente a tempo determinato
-- post: Per ogni cdr/voce/linea di attivita in ASS_CDP_LA e
--    crea un dettaglio SPE di spesa (PDG_PREVENTIVO_SPE_DET) sul pdg del cdr specificato
--    con i seguenti valori:
--     SPE.ESERCIZIO:=CDR.esercizio;
--     SPE.CD_CENTRO_RESPONSABILITA:=CDR.cd_centro_responsabilita;
--     SPE.CD_LINEA_ATTIVITA:=ASS_CDP_LA.cd_linea_attivita;
--     SPE.TI_APPARTENENZA:=CDP.ti_appartenenza;
--     SPE.TI_GESTIONE:=CDP.ti_gestione;
--     SPE.CD_ELEMENTO_VOCE:=CDP.cd_elemento_voce;
--     SPE.PG_SPESA:=(Nuovo pg_spesa)
--     SPE.DT_REGISTRAZIONE:=(Data odierna)
--     SPE.DESCRIZIONE:="Dettaglio di spesa per stipendi"
--     SPE.STATO:='Y';
--     SPE.ORIGINE:='STI'
--     SPE.CATEGORIA_DETTAGLIO:='SIN'
--     SPE.FL_SOLA_LETTURA:='Y'
--     SPE.IM_*:=0 (tutti gli importi) tranne ...
--   Nel caso di dipendenti a tempo indeterminato:
--         SPE.IM_RH_CCS_COSTI=SPE.IM_RK_CCS_SPESE_OGC=aImportoA1
--         SPE.IM_RAA_A2_COSTI_FINALI=SPE.IM_RAE_A2_SPESE_OGC=aImportoA2
--         SPE.IM_RAH_A3_COSTI_FINALI=SPE.IM_RAN_A3_SPESE_OGC=aImportoA3
--   Nel caso di dipendenti a tempo determinato:
--         SPE.IM_RO_CSS_ALTRI_COSTI
--
--   L'importo e calcolato con la seguente regola:
--
--   aImportX=(((COSTO_DEL_DIPENDENTE.im_aX/100) * ASSCDPLA.prc_la_aX)/100) * PRCAUO_AX con X=1,2,3
--
--   aggregando gli importi per cdr voce linea di attivita (spaccati per TIPO DI RAPPORTO DET/IND).
--
--   Dove PRC_UO_Ax e ottenuto dalla riga ASS_CDP_UO tale che:
--   ASS_CDP_UO.ESERCIZIO = CDR.ESERCIZIO AND
--   ASS_CDP_UO.CD_UNITA_ORGANIZZATIVA = CDR.CD_UNITA_ORGANIZZATIVA AND
--   ASS_CDP_ID_MATRICOLA = CDP.ID_MATRICOLA
--
--   Con le stesse regole utilizzate per la voce esplicita di costo_del_dipendente viene creato un
--   dettaglio sulla voce ONERI_CNR
--
--   Oltre a tale dettaglio viene creato 1 dettaglio singolo TFR
--   leggendo le quote corrispondenti in COSTO_DEL_DIPENDENTE e frazionandole con le stesse regole utilizzate
--   per le quote di spesa esplicita sui tre anni. In questo caso le colonne movimentate sono:
--   IM_RO_CSS_ALTRI_COSTI per il primo anno
--   IM_RAA_A2_COSTI_FINALI per il secondo anno
--   IM_RAH_A3_COSTI_FINALI per il terzo anno
--
--  Inoltre per ogni dettaglio di spesa SPE creato, crea una riga in ASS_CDP_PDG
--      ASS_CDP_PDG.ESERCIZIO:=SPE.esercizio;
--      ASS_CDP_PDG.TI_PREV_CONS:=SPE.ti_prev_cons;
--     ASS_CDP_PDG.ID_MATRICOLA:=SPE.ti_rapporto;
--      ASS_CDP_PDG.CD_CENTRO_RESPONSABILITA:=SPE.cd_centro_responsabilita;
--      ASS_CDP_PDG.CD_LINEA_ATTIVITA:=SPE.cd_linea_attivita;
--   ASS_CDP_PDG.TI_APPARTENENZA:=SPE.ti_appartenenza;
--      ASS_CDP_PDG.TI_GESTIONE:=SPE.ti_gestione;
--      ASS_CDP_PDG.CD_ELEMENTO_VOCE:=SPE.cd_elemento_voce;
--   ASS_CDP_PDG.PG_SPESA:=SPE.pg_spesa;
--
--  Infine mette a 'S' lo stato di tutti gli ASS_CDP_LA per i cdr dell'uo del
--  cdr specficato e imposta la data di scarico alla data odierna
-- pre-post-name: Tutti i controlli superati
-- pre: Nessun'altra precondizione e verificata
-- post: Per ogni cdr/voce/linea di attivita crea due dettagli SPE1 e SPE2 di spesa (PDG_PREVENTIVO_SPE_DET)
--    con i seguenti valori:
--   SPE1.ESERCIZIO:=CDR.esercizio;
--   SPE1.CD_CENTRO_RESPONSABILITA:=CDR.cd_centro_responsabilita;
--     SPE1.CD_LINEA_ATTIVITA:=ASS_CDP_LA.cd_linea_attivita;
--   SPE1.TI_APPARTENENZA:=CDP.ti_appartenenza;
--   SPE1.TI_GESTIONE:=CDP.ti_gestione;
--   SPE1.CD_ELEMENTO_VOCE:=CDP.cd_elemento_voce;
--     SPE1.PG_SPESA:=(Nuovo pg_spesa)
--   SPE1.DT_REGISTRAZIONE:=(Data odierna)
--   SPE1.DESCRIZIONE:="Dettaglio di spesa per stipendi"
--   SPE1.STATO:='Y';
--     SPE1.ORIGINE:='STI'
--   SPE1.CATEGORIA_DETTAGLIO:='SCR'
--   SPE1.FL_SOLA_LETTURA:='Y'
--     SPE1.PG_SPESA_CLGS:=SPE2.PG_SPESA;
--   SPE1.CD_CENTRO_RESPONSABILITA_CLGS:=SPE2.CD_CENTRO_RESPONSABILITA;
--   SPE1.CD_LINEA_ATTIVITA_CLGS:=SPE2.CD_LINEA_ATTIVITA;
--     SPE1.TI_APPARTENENZA_CLGS:=SPE2.TI_APPARTENENZA;
--   SPE1.TI_GESTIONE_CLGS:=SPE2.TI_GESTIONE;
--   SPE1.CD_ELEMENTO_VOCE_CLGS:=SPE2.CD_ELEMENTO_VOCE;
--     SPE1.IM_*:=0 (tutti gli altri importi) tranne ...
--   Solo per dipendenti a tempo indeterminato:
--         SPE.IM_RH_CCS_COSTI=SPE.IM_RL_CCS_SPESE_OGC_ALTRA_UO
--         SPE.IM_RAA_A2_COSTI_FINALI=SPE.IM_RAF_A2_SPESE_OGC_ALTRA_UO
--         SPE.IM_RAH_A3_COSTI_FINALI=SPE.IM_RAO_A3_SPESE_OGC_ALTRA_UO
--
--   SPE2.ESERCIZIO:=CDR.esercizio
--   SPE2.CD_CENTRO_RESPONSABILITA:=(CONFIGURAZIONE_CNR,'CDR_SPECIALE','CDR_PERSONALE')
--     SPE2.CD_LINEA_ATTIVITA:=( cd_linea_attivita tale che
--            esercizio = aCDR.esercizio
--             and cd_centro_responsabilita = (CONFIGURAZIONE_CNR,'CDR_SPECIALE','CDR_PERSONALE')
--             and cd_tipo_linea_attivita = 'SAUOP';
--          Se non c'e va creata )
--   SPE2.TI_APPARTENENZA:=CDP.ti_appartenenza;
--   SPE2.TI_GESTIONE:=CDP.ti_gestione;
--     SPE2.CD_ELEMENTO_VOCE:=CDP.cd_elemento_voce;
--   SPE2.PG_SPESA:=(Nuovo pg_spesa);
--   SPE2.DT_REGISTRAZIONE:=(Data odierna)
--     SPE2.DESCRIZIONE:="Dettaglio di spesa per stipendi"
--   SPE2.STATO:='Y';
--   SPE2.ORIGINE:='STI'
--     SPE2.CATEGORIA_DETTAGLIO:='CAR'
--   SPE2.FL_SOLA_LETTURA:='N'; -- Richiesta 137R
--   SPE2.PG_SPESA_CLGS:=SPE1.PG_SPESA;
--   SPE2.CD_CENTRO_RESPONSABILITA_CLGS:=SPE1.CD_CENTRO_RESPONSABILITA;
--   SPE2.CD_LINEA_ATTIVITA_CLGS:=SPE1.CD_LINEA_ATTIVITA;
--   SPE2.TI_APPARTENENZA_CLGS:=SPE1.TI_APPARTENENZA;
--     SPE2.TI_GESTIONE_CLGS:=SPE1.TI_GESTIONE;
--   SPE2.CD_ELEMENTO_VOCE_CLGS:=SPE1.CD_ELEMENTO_VOCE;
--   SPE2.IM_RU_SPESE_COSTI_ALTRUI:=SPE1.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
--     SPE2.IM_RAG_A2_SPESE_COSTI_ALTRUI:=SPE1.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
--   SPE2.IM_RAP_A3_SPESE_COSTI_ALTRUI:=SPE1.IM_RAM_A3_SPESE_ODC_ALTRA_UO
--   SPE2.IM_*:=0 (tutti gli altri importi)
--         SPE2.IM_RU_SPESE_COSTI_ALTRUI
--         SPE2.IM_RAG_A2_SPESE_COSTI_ALTRUI
--         SPE2.IM_RAP_A3_SPESE_COSTI_ALTRUI
--
--   L'importo e calcolato con la seguente regola:
--
--   aImportX=(((COSTO_DEL_DIPENDENTE.im_aX/100) * ASSCDPLA.prc_la_aX)/100) * PRCAUO_AX con X=1,2,3
--
--   aggregando gli importi per cdr voce linea di attivita (spaccati per TIPO DI RAPPORTO DET/IND).
--
--  Dove PRC_UO_Ax e ottenuto dalla riga ASS_CDP_UO tale che:
--   ASS_CDP_UO.ESERCIZIO = CDR.ESERCIZIO AND
--   ASS_CDP_UO.CD_UNITA_ORGANIZZATIVA = CDR.CD_UNITA_ORGANIZZATIVA AND
--   ASS_CDP_ID_MATRICOLA = CDP.ID_MATRICOLA
--
--   Con le stesse regole utilizzate per la voce esplicita di costo_del_dipendente viene creato un
--   dettaglio sulla voce ONERI_CNR
--
--   Oltre a tale dettaglio viene creato 1 dettaglio singolo per TFR
--   leggendo le quote corrispondenti in COSTO_DEL_DIPENDENTE e frazionandole con le stesse regole utilizzate
--   per le quote di spesa esplicita sui tre anni.
--
--  Inoltre per ogni dettaglio di spesa SPE creato, crea una riga in ASS_CDP_PDG
--      ASS_CDP_PDG.ESERCIZIO:=SPE.esercizio;
--      ASS_CDP_PDG.TI_PREV_CONS:=SPE.ti_prev_cons;
--       ASS_CDP_PDG.ID_MATRICOLA:=SPE.ti_rapporto;
--      ASS_CDP_PDG.CD_CENTRO_RESPONSABILITA:=SPE.cd_centro_responsabilita;
--      ASS_CDP_PDG.CD_LINEA_ATTIVITA:=SPE.cd_linea_attivita;
--       ASS_CDP_PDG.TI_APPARTENENZA:=SPE.ti_appartenenza;
--      ASS_CDP_PDG.TI_GESTIONE:=SPE.ti_gestione;
--      ASS_CDP_PDG.CD_ELEMENTO_VOCE:=SPE.cd_elemento_voce;
--       ASS_CDP_PDG.PG_SPESA:=SPE.pg_spesa;
--
--  Infine mette a 'S' lo stato di tutti gli ASS_CDP_LA per i cdr dell'uo del
--  cdr specficato e imposta la data di scarico alla data odierna

-- Parametri:
-- aEsercizio -> anno di esercizio
-- aCdCdr -> Codice del centro di responsabilita che scarica CDP
-- aUser -> Utente che effettua l'operazione

 procedure scaricaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2);


-- Annulla uno scarico su PDG effettuato precedentemente per il CDR RUO di codice aCdCdr
--
-- Il cdr di codice cd_cdr e un RUO
--
-- pre-post-name: PDG gia chiuso
-- pre: Lo stato del PDG del CDR specificato e diverso da A o B, D o E
-- post: Genera eccezione
-- pre-post-name: PDG non ruo
-- pre: Il cdr specificato non e responsabile di unita organizzativa
-- post: Genera eccezione
-- pre-post-name: pdg del cdr personale gia chiuso
-- pre: Il pdg del cdr personale (codice = CONFIGURAZIONE_CNR,'CDR_SPECIALE','CDR_PERSONALE')
--    non e in stato A o B, D o E
-- post: Genera un eccezione col messaggio "Il piano di gestione del CDR del personale
--    xxx.yy.zz non e in stato iniziale o di pre-chiusura!"
-- pre-post-name: Aggregato del CDR del personale chiuso
-- pre: Lo stato dell'aggregato del CDR del personale ? CHIUSO (stato 'B')
-- post: Genera eccezione
-- pre-post-name: pdg cdr caricati gia chiusi
-- pre: Qualcuno dei cdr che compaiono in ASS_CDP_LA per le matricole appartenenti alla
--   uo del cdr specificato ha il pdg in stato diverso da A o B, D o E
-- post: Genera un'eccezione col messaggio "Il piano di gestione del CDR NRUO xxx.yy.zz
--     non e in stato iniziale o di pre-chiusura!"
-- pre-post-name: scarico non ancora effettuato
-- pre: Lo scarico dei costi non e ancora stato effettuato sul cdr specificato
-- post: Genera una eccezione con messaggio "Operazione non ancora effettuata su cdr"
-- pre-post-name: Tutti i controlli superati
-- pre: Nessun'altra precondizione verificata
-- post: Elimina tutti i dettagli di spesa PDG_PREVENTIVO_SPE_DET dei CDR che stanno nell'UO del cdr
--   specificato tali che:
--    PDG_PREVENTIVO_SPE_DET.esercizio = esercizio and
--    PDG_PREVENTIVO_SPE_DET.origine = 'STI'
--    Elimina tutti i dettagli di spesa PDG_PREVENTIVO_SPE_DET collegati a qualche CDR che sta
--    nell'UO del cdr specificato tali che:
--    PDG_PREVENTIVO_SPE_DET.esercizio = esercizio and
--    PDG_PREVENTIVO_SPE_DET.origine = 'STI'
--    Riporta a 'I' lo stato degli ASS_CDP_LA per i CDR che stanno nell'UO del cdr specificato
--
-- Parametri:
-- aEsercizio -> anno di esercizio
-- aCdCdr -> Codice del centro di responsabilita che ha scaricato CDP
-- aUser -> Utente che effettua l'operazione

 procedure annullaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2);

-- Check scarico completo dei costi del dipendente
--
-- Verifica di scarico completo dei dipendenti su di un certo CDR RUO (Responsabile di UO).
-- Verifica dello scrico di tutti i dipendenti (con controllo che non ne esistano di non configurati nel caso del CDR RUO del Personale)
--
--
-- pre-post-name: Controllo che non esistano matricole appartenenti ad una UO non configurate nel caso il CDR sia RUO di tale UO.
-- pre:  Il CDR RUO e di una data Unita Organizzativa UO. Esiste almeno una matricola appartenente ad UO che non e presente ne in configurazione cdr-linee di attivita ne in configurazione di scarico matricole su altra unita organizzativa.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Controllo che non esistano matricole in tutto il CNR non configurate nel caso il CDR RUO sia del Personale.
-- pre:  Il CDR RUO e quello del Personale.
-- Esiste almeno una matricola che non e presente ne in configurazione cdr-linee di attivita ne in configurazione di scarico matricole su altra unita organizzativa.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Controllo di scarico completo di tutti gli altri CDR RUO nel caso in cui il CDR da controllare sia quello del Personale
-- pre: Il CDR RUO e quello del personale
--            Esiste un CDR RUO diverso da quello del personale per cui non e completo lo scarico dei costi del personale
-- post: Ritorna 'N'
--
-- pre-post-name: Esistono scarichi da altra UO non confermati
-- pre: Esiste uno scarico sul CDR RUO in processo da altra UO di dipendente non confermato dal CDR RUO in processo
-- post: Ritorna 'N'
--
-- pre-post-name: Esistono scarichi da altra UO confermati e non ripartiti al 100% sotto il CDR RUO in processo
-- pre: Esiste uno scarico di dipendente da altra UO confermato nel CDR RUO in processo e non ivi ripartito al 100%
-- post: Ritorna 'N'
--
-- pre-post-name: Esistono CDR della linea del CDR RUO che non hanno effettuato lo scarico dei costi del personale su PDG
-- pre: Esiste uno scarico di dipendente da altra UO confermato nell'UO in processo e non ivi ripartito al 100%
-- post: Ritorna 'N'
--
-- pre-post-name: Controllo superato
-- pre: Nessun'altra precondizione verificata
-- post: Ritorna 'Y'
--
-- Parametri:
-- aEsercizio -> esercizio contabile
-- aCdCdr -> codice del CDR RUO su cui effetuare il controllo

 function checkScaricoCDPCompleto(aEsercizio number, aCdCdr varchar2) return char;

 -- Inserisce una riga nella tabella di associazione tra CDP e PDG

 procedure ins_ASS_CDP_PDG (aDest ASS_CDP_PDG%rowtype);

 -- Inserisce una riga nella tabella costo del dipendente

 procedure ins_COSTO_DEL_DIPENDENTE (aDest COSTO_DEL_DIPENDENTE%rowtype);

 -- Inserisce una riga nella tabella degli arrotondamenti del costo del dipendente

 procedure ins_ASS_CDP_ROUND (aDest ASS_CDP_ROUND%rowtype);

end;


CREATE OR REPLACE package body CNRCTB060 is

 -- Imposta gli importi sul dettaglio di PDG da creare

 procedure setImporto(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype,
                      aDestS in out pdg_preventivo_spe_det%rowtype) is
--  isTempoDeterminato boolean;
 begin

/*
  isTempoDeterminato:=false;

  if aASSCDPLA.ti_rapporto = CDP_TI_RAPP_DETERMINATO then
   isTempoDeterminato:=true;
  end if;

  -- Se si tratta di tempo determinato, gli importi vanno su una sola colonna nel primo anno
  if isTempoDeterminato then
   aDestS.IM_RO_CSS_ALTRI_COSTI:=aASSCDPLA.im_a1;
   return;
  end if;
*/

  if aDestS.categoria_dettaglio=CNRCTB050.DETTAGLIO_SCARICO then
   aDestS.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=aASSCDPLA.im_a1;
  else
   aDestS.IM_RK_CCS_SPESE_OGC:=aASSCDPLA.im_a1;
  end if;
  aDestS.IM_RH_CCS_COSTI:=aASSCDPLA.im_a1;

  if aDestS.categoria_dettaglio=CNRCTB050.DETTAGLIO_SCARICO then
   aDestS.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=aASSCDPLA.im_a2;
  else
   aDestS.IM_RAE_A2_SPESE_OGC:=aASSCDPLA.im_a2;
  end if;
  aDestS.IM_RAA_A2_COSTI_FINALI:=aASSCDPLA.im_a2;

  if aDestS.categoria_dettaglio=CNRCTB050.DETTAGLIO_SCARICO then
   aDestS.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=aASSCDPLA.im_a3;
  else
   aDestS.IM_RAN_A3_SPESE_OGC:=aASSCDPLA.im_a3;
  end if;
  aDestS.IM_RAH_A3_COSTI_FINALI:=aASSCDPLA.im_a3;
 end;

 procedure setImportoDettaglioCollegato(aDestSColl in out pdg_preventivo_spe_det%rowtype, aDestS pdg_preventivo_spe_det%rowtype) is
 begin
   aDestSColl.IM_RU_SPESE_COSTI_ALTRUI:=aDestS.IM_RL_CCS_SPESE_OGC_ALTRA_UO;
   aDestSColl.IM_RAG_A2_SPESE_COSTI_ALTRUI:=aDestS.IM_RAF_A2_SPESE_OGC_ALTRA_UO;
   aDestSColl.IM_RAP_A3_SPESE_COSTI_ALTRUI:=aDestS.IM_RAO_A3_SPESE_OGC_ALTRA_UO;
 end;

 procedure setImportoTFR(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype, aDestS in out pdg_preventivo_spe_det%rowtype) is
 begin
  aDestS.IM_RO_CSS_ALTRI_COSTI:=aASSCDPLA.im_a1;
  aDestS.IM_RAA_A2_COSTI_FINALI:=aASSCDPLA.im_a2;
  aDestS.IM_RAH_A3_COSTI_FINALI:=aASSCDPLA.im_a3;
 end;

-- Funzione di accantonamento dei rotti di scarico

 procedure fill_round_table(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype, aTSNow date, aUser varchar2) is
  aAssCdpRnd ASS_CDP_ROUND%rowtype;
 begin
    if
	     aASSCDPLA.IM_RND_A1 = 0
	 and aASSCDPLA.IM_RND_A2 = 0
	 and aASSCDPLA.IM_RND_A3 = 0
	then
     return;
	end if;
	aAssCdpRnd:=null;
    aAssCdpRnd.ESERCIZIO:=aASSCDPLA.ESERCIZIO;
    aAssCdpRnd.CD_CDR_ROOT:=aASSCDPLA.CD_CDR_ROOT;
    aAssCdpRnd.TI_APPARTENENZA:=aASSCDPLA.TI_APPARTENENZA;
    aAssCdpRnd.TI_GESTIONE:=aASSCDPLA.TI_GESTIONE;
    aAssCdpRnd.CD_ELEMENTO_VOCE:=aASSCDPLA.CD_ELEMENTO_VOCE;
    aAssCdpRnd.TI_RAPPORTO:=aASSCDPLA.TI_RAPPORTO;
    begin
	 select * into aAssCdpRnd from ass_cdp_round where
	      ESERCIZIO=aAssCdpRnd.esercizio
      and CD_CDR_ROOT=aAssCdpRnd.cd_cdr_root
      and TI_APPARTENENZA=aAssCdpRnd.ti_appartenenza
      and TI_GESTIONE=aAssCdpRnd.ti_gestione
      and CD_ELEMENTO_VOCE=aAssCdpRnd.cd_elemento_voce
      and TI_RAPPORTO=aAssCdpRnd.ti_rapporto
	  for update nowait;
     update ass_cdp_round set
      IM_ARR_A1=IM_ARR_A1+aASSCDPLA.im_rnd_a1,
      IM_ARR_A2=IM_ARR_A2+aASSCDPLA.im_rnd_a2,
      IM_ARR_A3=IM_ARR_A3+aASSCDPLA.im_rnd_a3
	 where
	      ESERCIZIO=aAssCdpRnd.esercizio
      and CD_CDR_ROOT=aAssCdpRnd.cd_cdr_root
      and TI_APPARTENENZA=aAssCdpRnd.ti_appartenenza
      and TI_GESTIONE=aAssCdpRnd.ti_gestione
      and CD_ELEMENTO_VOCE=aAssCdpRnd.cd_elemento_voce
      and TI_RAPPORTO=aAssCdpRnd.ti_rapporto;
	exception when NO_DATA_FOUND then
     aAssCdpRnd.IM_ARR_A1:=aASSCDPLA.im_rnd_a1;
     aAssCdpRnd.IM_ARR_A2:=aASSCDPLA.im_rnd_a2;
     aAssCdpRnd.IM_ARR_A3:=aASSCDPLA.im_rnd_a3;
     aAssCdpRnd.IM_ARR_A1_NON_DISTR:=0;
     aAssCdpRnd.IM_ARR_A2_NON_DISTR:=0;
     aAssCdpRnd.IM_ARR_A3_NON_DISTR:=0;
     aAssCdpRnd.DACR:=aTSNow;
     aAssCdpRnd.UTUV:=aUser;
     aAssCdpRnd.UTCR:=aUser;
     aAssCdpRnd.DUVA:=aTSNow;
     aAssCdpRnd.PG_VER_REC:=1;
     ins_ASS_CDP_ROUND(aAssCdpRnd);
	end;
 end;

 procedure scaricaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
  aCDRRUO cdr%rowtype;
  aUOAfferenza unita_organizzativa%rowtype;
  aPgDettaglio number(10);
  aPgDettaglioColl number(10);
  aDestS pdg_preventivo_spe_det%rowtype;
  aDestSColl pdg_preventivo_spe_det%rowtype;
  aTSNow date;
  aAss ass_cdp_pdg%rowtype;
  aCDRPersonale cdr%rowtype;
  isCDRInUOPersonale boolean;
  aLASAUOP linea_attivita%rowtype;
  aNum number(8);
  aVoceTFR elemento_voce%rowtype;
  aVoceONERICNR elemento_voce%rowtype;
--  isTempoDeterminato boolean;
  aLATmp linea_attivita%rowtype;
  isDettRedistrRottiFound boolean;
  isProcessato boolean;
  matricola_err         VARCHAR2(3000);
 begin
  aTSNow:=sysdate;

  -- Leggo il CDR

  aCDRRUO:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

  if to_number(aCDRRUO.cd_proprio_cdr) != 0 then
   IBMERR001.RAISE_ERR_GENERICO('Operazione permessa solo su cdr di tipo RUO!');
  end if;

  -- Lock del PDG del CDR in processo

  CNRCTB050.LOCKPDG(aEsercizio, aCdCdr);

  -- Leggo il CDR del personale e lock del PDG

  aCDRPersonale:=CNRCTB020.GETCDRPERSONALE;
  CNRCTB050.LOCKPDG(aEsercizio, aCDRPersonale.cd_centro_responsabilita);

  -- Verifico che l'aggregato del CDR del personale NON sia chiuso in stato B

  if CNRCTB050.checkStatoAggregato(aEsercizio,
                                   aCDRPersonale.cd_centro_responsabilita,
								   CNRCTB050.STATO_AGGREGATO_FINALE) = 'Y'
  then
   IBMERR001.RAISE_ERR_GENERICO('L''agregato del CDR del personale risulta chiuso. Non ? possibile effettuare lo scarico dei costi del personale');
  end if;

  if aCdCdr = aCDRPersonale.cd_centro_responsabilita then
   select count(*) into aNum from ass_cdp_pdg a, pdg_preventivo_spe_det b where
         a.esercizio = aEsercizio
	and  b.esercizio = a.esercizio
	and  b.cd_centro_responsabilita = a.cd_centro_responsabilita
	and  b.cd_linea_attivita = a.cd_linea_attivita
	and  b.ti_appartenenza = a.ti_appartenenza
	and  b.ti_gestione = a.ti_gestione
	and  b.cd_elemento_voce = a.cd_elemento_voce
	and  b.pg_spesa = a.pg_spesa
    and  b.origine=CNRCTB050.ORIGINE_STIPENDI
    and  b.categoria_dettaglio in (CNRCTB050.DETTAGLIO_SINGOLO,CNRCTB050.DETTAGLIO_SCARICO)
    and  a.cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
		   );
  else
   select count(*) into aNum from ass_cdp_pdg where
        esercizio = aEsercizio
    and  cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
		   );
  end if;

  if aNum > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Operazione gia effettuata su cdr '||aCdCdr);
  end if;

  -- Stabilisco se il CDR ricevente e dell'UO Personale o no
  isCDRInUOPersonale:=false;
  if aCDRPersonale.cd_unita_organizzativa = aCDRRUO.cd_unita_organizzativa then
   isCDRInUOPersonale:=true;
  end if;

  aUOAfferenza:=CNRCTB020.getUOAfferenza(aCDRRUO);

  if not (CNRCTB050.GETSTATO(aEsercizio,aCDRPersonale.cd_centro_responsabilita) in (
	  CNRCTB050.STATO_PDG_INIZIALE,
	  CNRCTB050.STATO_PDG_PRE_CHIUSURA,
	  CNRCTB050.STATO_PDG_RC,
	  CNRCTB050.STATO_PDG_RC_PRE_CHIUSURA
	 )) then
   IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR del personale '||aCDRPersonale.cd_centro_responsabilita||' non e in stato iniziale o di pre-chiusura!');
  end if;

  for aPDG in PDG_CON_CONFIG_SCR(aEsercizio, aCDRRUO.cd_centro_responsabilita) loop
   CNRCTB050.LOCKPDG(aEsercizio, aCDRRUO.cd_centro_responsabilita);
   if not (aPDG.stato in (
	   CNRCTB050.STATO_PDG_INIZIALE,
	   CNRCTB050.STATO_PDG_PRE_CHIUSURA,
	   CNRCTB050.STATO_PDG_RC,
	   CNRCTB050.STATO_PDG_RC_PRE_CHIUSURA
	  )) then
    IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR NRUO '||aPDG.cd_centro_responsabilita||' non e in stato iniziale o di pre-chiusura!');
   end if;
  end loop;

  -- Il totale di scarico configurato dei dipendenti dell'UO e 100%  (comprendento le quote verso altre UO accettate)

  select count(*) into aNum from V_CDP_TOT_PRC where
         esercizio = aEsercizio
     and mese = 0
	 and cd_unita_organizzativa = aCDRRUO.cd_unita_organizzativa
	 and (
	         prc_a1 < 100
		  or prc_a2 < 100
		  or prc_a3 < 100
		 );

  if aNum > 0 Then

    For rec_err In     (Select Distinct id_matricola
                       From V_CDP_TOT_PRC where
                          esercizio = aEsercizio And
                          mese = 0 And
                          cd_unita_organizzativa = aCDRRUO.cd_unita_organizzativa
	                  and (prc_a1 < 100 Or
	                       prc_a2 < 100 Or
	                       prc_a3 < 100)) Loop

        matricola_err := matricola_err||' - '||rec_err.id_matricola;

    End Loop;

   IBMERR001.RAISE_ERR_GENERICO('La configurazione di scarico dei dipendenti dell''UO '||aCDRRUO.cd_unita_organizzativa||' non e'' completa per n. '||anum||' matricola/e ('||matricola_err||')');
  end if;

  -- Carico le voci del piano TFR e ONERICNR

  aVoceONERICNR:=CNRCTB000.GETVOCEONERICNR(aEsercizio);
  aVoceTFR:=CNRCTB000.GETVOCETFR(aEsercizio);

  -- Start dello scarico su PDG dei costi del personale

  for aASSCDPLA in (
      select * from V_CDP_SPACCATO_CDR_LA_VOCE where
	          esercizio = aEsercizio
		  and mese = 0
		  and cd_cdr_root = aCDRRUO.cd_centro_responsabilita order by cd_cdr) loop  -- Cicla sulle configurazioni di scarico

	-- Accantonamento degli arrotondamenti dovuti allo scarico corrente (per elemento voce e tipo rapporto)
    fill_round_table(aASSCDPLA, aTSNow, aUser);

    -- Determina il tipo di rapporto dalla tabella COSTO_DEL_DIPENDENTE

/* stani
    isTempoDeterminato:=false;
    if aASSCDPLA.ti_rapporto = CDP_TI_RAPP_DETERMINATO then
     isTempoDeterminato:=true;
    end if;
*/

    aDestS:=null;
	 -- CREO IL DETTAGLIO LA PRIMA VOLTA
    aDestS:=null;
    aPgDettaglio:=0;
	select NVL(max(pg_spesa),0) into aPgDettaglio from PDG_PREVENTIVO_SPE_DET where
            ESERCIZIO=aEsercizio
        and CD_CENTRO_RESPONSABILITA=aASSCDPLA.cd_cdr
        and CD_LINEA_ATTIVITA=aASSCDPLA.cd_linea_attivita
        and TI_APPARTENENZA=aASSCDPLA.ti_appartenenza
        and TI_GESTIONE=aASSCDPLA.ti_gestione
        and CD_ELEMENTO_VOCE=aASSCDPLA.cd_elemento_voce;

 	aPgDettaglio:=aPgDettaglio + 1;

 	 -- Leggo la linea di attivita per aggiornare il dettaglio del PDG con funzione e natura
 	select * into aLATmp from linea_attivita where
 	       cd_linea_attivita = aASSCDPLA.cd_linea_attivita
 	   and cd_centro_responsabilita = aASSCDPLA.cd_cdr;

    if
	       aUOAfferenza.cd_area_ricerca is null
       and aLaTmp.cd_natura = '5'
	then
	  IBMERR001.RAISE_ERR_GENERICO('Esiste una configurazione di scarico con LA di natura 5 su CDR collegato ad UO non collegata ad Area di Ricerca');
	end if;

    aDestS.ESERCIZIO:=aEsercizio;
    aDestS.CD_CENTRO_RESPONSABILITA:=aASSCDPLA.cd_cdr;
    aDestS.CD_LINEA_ATTIVITA:=aASSCDPLA.cd_linea_attivita;
    aDestS.CD_FUNZIONE:=aLATmp.cd_funzione;
    aDestS.CD_NATURA:=aLATmp.cd_natura;
    aDestS.TI_APPARTENENZA:=aASSCDPLA.ti_appartenenza;
    aDestS.TI_GESTIONE:=aASSCDPLA.ti_gestione;
    aDestS.CD_ELEMENTO_VOCE:=aASSCDPLA.cd_elemento_voce;
    aDestS.PG_SPESA:=aPgDettaglio;
    aDestS.DT_REGISTRAZIONE:=aTSNow;

    if aASSCDPLA.cd_elemento_voce = aVoceONERICNR.cd_elemento_voce then
       aDestS.DESCRIZIONE:=DESC_DETT_ONERI_CNR||' '||aASSCDPLA.ti_rapporto;
 	elsif aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce then
       aDestS.DESCRIZIONE:=DESC_DETT_TFR||' '||aASSCDPLA.ti_rapporto;
    else
       aDestS.DESCRIZIONE:=DESC_DETT||' '||aASSCDPLA.ti_rapporto;
 	end if;

    aDestS.STATO:='Y';
    aDestS.ORIGINE:=CNRCTB050.ORIGINE_STIPENDI;
-- stani
    if isCDRInUOPersonale /*or isTempoDeterminato*/ Or
        (aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce) then
        aDestS.CATEGORIA_DETTAGLIO:=CNRCTB050.DETTAGLIO_SINGOLO;
    else
        aDestS.CATEGORIA_DETTAGLIO:=CNRCTB050.DETTAGLIO_SCARICO;
    end if;

 	aDestS.FL_SOLA_LETTURA:='Y'; -- I DETTAGLI STIPENDIALI NON SONO TOCCABILI!!!

  	CNRCTB050.resetCampiImporto(aDestS);

 	if aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce then
     setImportoTFR(aASSCDPLA, aDestS);
    else
     setImporto(aASSCDPLA, aDestS);
    end if;

    aDestS.DACR:=aTSNow;
    aDestS.UTCR:=aUser;
    aDestS.DUVA:=aTSNow;
    aDestS.UTUV:=aUser;
    aDestS.PG_VER_REC:=1;
    CNRCTB050.ins_PDG_PREVENTIVO_SPE_DET (aDestS);

    aAss.ESERCIZIO:=aDestS.esercizio;
    aAss.TI_PREV_CONS:=aASSCDPLA.ti_prev_cons;
    aAss.ID_MATRICOLA:=aASSCDPLA.ti_rapporto;
    aAss.CD_CENTRO_RESPONSABILITA:=aDestS.cd_centro_responsabilita;
    aAss.CD_LINEA_ATTIVITA:=aDestS.cd_linea_attivita;
    aAss.TI_APPARTENENZA:=aDestS.ti_appartenenza;
    aAss.TI_GESTIONE:=aDestS.ti_gestione;
    aAss.CD_ELEMENTO_VOCE:=aDestS.cd_elemento_voce;
    aAss.PG_SPESA:=aDestS.pg_spesa;
    aAss.DACR:=aTSNow;
    aAss.UTCR:=aUser;
    aAss.DUVA:=aTSNow;
    aAss.UTUV:=aUser;
    aAss.PG_VER_REC:=1;
    INS_ASS_CDP_PDG(aAss);
      -- Se il CDR d'origine non appartiene all'UO Personale e la matricola non e a tempo determinato,
      -- creo il dettaglio collegato nel CDR responsabile dell'UO del Personale
-- stani
    if not (isCDRInUoPersonale /*or isTempoDeterminato*/ Or
            (aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce)) then

       -- Estraggo la linea di attivita di tipo SAUOP per il CDR in questione
       begin
 	    aLASAUOP:=CNRCTB010.getLASAUOP(aEsercizio, aCDRPersonale.cd_centro_responsabilita);
 	   exception when NO_DATA_FOUND then
 	    aLASAUOP:=CNRCTB010.creaLASAUOP(aEsercizio,aCDRPersonale.cd_centro_responsabilita,aUser);
 	   end;

       aPgDettaglioColl:=0;
 	   select NVL(max(pg_spesa),0) into aPgDettaglioColl from PDG_PREVENTIVO_SPE_DET where
            ESERCIZIO=aEsercizio
        and CD_CENTRO_RESPONSABILITA=aCDRPersonale.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aLASAUOP.cd_linea_attivita
        and TI_APPARTENENZA=aASSCDPLA.ti_appartenenza
        and TI_GESTIONE=aASSCDPLA.ti_gestione
        and CD_ELEMENTO_VOCE=aASSCDPLA.cd_elemento_voce;

 	   aPgDettaglioColl:=aPgDettaglioColl + 1;

 	   aDestSColl:=null;
       aDestSColl.ESERCIZIO:=aEsercizio;
       aDestSColl.CD_CENTRO_RESPONSABILITA:=aCDRPersonale.cd_centro_responsabilita;
       aDestSColl.CD_LINEA_ATTIVITA:=aLASAUOP.cd_linea_attivita;
       aDestSColl.CD_FUNZIONE:=aLASAUOP.cd_funzione;
       aDestSColl.CD_NATURA:=aLASAUOP.cd_natura;
       aDestSColl.TI_APPARTENENZA:=aASSCDPLA.ti_appartenenza;
       aDestSColl.TI_GESTIONE:=aASSCDPLA.ti_gestione;
       aDestSColl.CD_ELEMENTO_VOCE:=aASSCDPLA.cd_elemento_voce;
       aDestSColl.PG_SPESA:=aPgDettaglioColl;
       aDestSColl.DT_REGISTRAZIONE:=aTSNow;

 	   if aASSCDPLA.cd_elemento_voce = aVoceONERICNR.cd_elemento_voce then
        aDestSColl.DESCRIZIONE:=DESC_DETT_ONERI_CNR||' '||aASSCDPLA.ti_rapporto;
       else
        aDestSColl.DESCRIZIONE:=DESC_DETT||' '||aASSCDPLA.ti_rapporto;
       end if;

       aDestSColl.STATO:='Y';
       aDestSColl.ORIGINE:=CNRCTB050.ORIGINE_STIPENDI;
       aDestSColl.CATEGORIA_DETTAGLIO:=CNRCTB050.DETTAGLIO_CARICO;

       -- Richiesta 137R

       aDestSColl.FL_SOLA_LETTURA:='N';

 	   aDestSColl.PG_SPESA_CLGS:=aDestS.PG_SPESA;
       aDestSColl.CD_CENTRO_RESPONSABILITA_CLGS:=aDestS.CD_CENTRO_RESPONSABILITA;
       aDestSColl.CD_LINEA_ATTIVITA_CLGS:=aDestS.CD_LINEA_ATTIVITA;
       aDestSColl.TI_APPARTENENZA_CLGS:=aDestS.TI_APPARTENENZA;
       aDestSColl.TI_GESTIONE_CLGS:=aDestS.TI_GESTIONE;
       aDestSColl.CD_ELEMENTO_VOCE_CLGS:=aDestS.CD_ELEMENTO_VOCE;

 	   CNRCTB050.resetCampiImporto(aDestSColl);
 	   setImportoDettaglioCollegato(aDestSColl, aDestS);

       aDestSColl.DACR:=aTSNow;
       aDestSColl.UTCR:=aUser;
       aDestSColl.DUVA:=aTSNow;
       aDestSColl.UTUV:=aUser;
       aDestSColl.PG_VER_REC:=1;
       CNRCTB050.ins_PDG_PREVENTIVO_SPE_DET (aDestSColl);

 	  -- Aggiorno il dettaglio originale con i dati del dettaglio collegato

 	   update PDG_PREVENTIVO_SPE_DET set
 	   PG_SPESA_CLGS = aPgDettaglioColl,
        CD_CENTRO_RESPONSABILITA_CLGS=aDestSColl.cd_centro_responsabilita,
        CD_LINEA_ATTIVITA_CLGS=aDestSColl.cd_linea_attivita,
        TI_APPARTENENZA_CLGS=aDestSColl.ti_appartenenza,
        TI_GESTIONE_CLGS=aDestSColl.ti_gestione,
        CD_ELEMENTO_VOCE_CLGS=aDestSColl.cd_elemento_voce
 	   where
            ESERCIZIO=aDestS.esercizio
        and CD_CENTRO_RESPONSABILITA=aDestS.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aDestS.cd_linea_attivita
        and TI_APPARTENENZA=aDestS.ti_appartenenza
        and TI_GESTIONE=aDestS.ti_gestione
        and CD_ELEMENTO_VOCE=aDestS.cd_elemento_voce
 	    and PG_SPESA=aDestS.pg_spesa;

 	  -- Inserisce il collegamento tra CDP e dettaglio di CARICO su UO Personale
       aAss.ESERCIZIO:=aDestSColl.esercizio;
       aAss.TI_PREV_CONS:=aASSCDPLA.ti_prev_cons;
       aAss.ID_MATRICOLA:=aASSCDPLA.ti_rapporto;
	   aAss.CD_CENTRO_RESPONSABILITA:=aDestSColl.cd_centro_responsabilita;
       aAss.CD_LINEA_ATTIVITA:=aDestSColl.cd_linea_attivita;
       aAss.TI_APPARTENENZA:=aDestSColl.ti_appartenenza;
       aAss.TI_GESTIONE:=aDestSColl.ti_gestione;
       aAss.CD_ELEMENTO_VOCE:=aDestSColl.cd_elemento_voce;
       aAss.PG_SPESA:=aDestSColl.pg_spesa;
       aAss.DACR:=aTSNow;
       aAss.UTCR:=aUser;
       aAss.DUVA:=aTSNow;
       aAss.UTUV:=aUser;
       aAss.PG_VER_REC:=1;
       INS_ASS_CDP_PDG(aAss);
 	end if;
    update ass_cdp_la set
	     stato = STATO_CDP_SCARICATO
	    ,dt_scarico = aTSNow
		,utuv=aUser
		,duva=aTSNow
		,pg_ver_rec = pg_ver_rec + 1
    where
	       esercizio = aASSCDPLA.esercizio
	   and mese = 0
	   and cd_centro_responsabilita = aASSCDPLA.cd_cdr
	   and cd_linea_attivita = aASSCDPLA.cd_linea_attivita
	   and stato = STATO_CDP_NON_SCARICATO;
  end loop;

  -- Distribuzione dei rotti su dettagli creati

  for aAssCdpRnd in (select * from ass_cdp_round where
	          esercizio = aEsercizio
		  and cd_cdr_root = aCDRRUO.cd_centro_responsabilita
		  for update nowait) loop  -- Cicla sulle configurazioni dei ROTTI
   isDettRedistrRottiFound:=false;
   for aModSpeDet in (select p.* from pdg_preventivo_spe_det p, V_PDG_CDR_RUO_NRUO c where
                p.esercizio=aAssCdpRnd.esercizio
			and c.cd_cdr_root = aAssCdpRnd.cd_cdr_root
			and c.esercizio=p.esercizio
            and p.cd_centro_responsabilita=c.cd_centro_responsabilita
			and p.origine=CNRCTB050.ORIGINE_STIPENDI
			and p.ti_appartenenza = aAssCdpRnd.ti_appartenenza
			and p.ti_gestione = aAssCdpRnd.ti_gestione
			and p.cd_elemento_voce = aAssCdpRnd.cd_elemento_voce
			and p.categoria_dettaglio in (CNRCTB050.DETTAGLIO_SINGOLO,CNRCTB050.DETTAGLIO_SCARICO)
			and exists (select 1 from ass_cdp_pdg where
			           ESERCIZIO=p.esercizio
                   and CD_CENTRO_RESPONSABILITA=p.cd_centro_responsabilita
                   and CD_LINEA_ATTIVITA=p.cd_linea_attivita
                   and TI_APPARTENENZA=p.ti_appartenenza
                   and TI_GESTIONE=p.ti_gestione
                   and CD_ELEMENTO_VOCE=p.cd_elemento_voce
                   and PG_SPESA=p.pg_spesa
				   and ti_prev_cons=TIPO_PREVENTIVO
				   and id_matricola=aAssCdpRnd.ti_rapporto
			)
   ) loop
    isProcessato:=false;* stani     if
	      not isProcessato
	  and     aAssCdpRnd.ti_rapporto=CDP_TI_RAPP_DETERMINATO
	  and not aAssCdpRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce
	  and aModSpeDet.CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SINGOLO
	then
     if
                 (aModSpeDet.IM_RO_CSS_ALTRI_COSTI = 0 and aAssCdpRnd.im_arr_a1 <> 0)
			  or aModSpeDet.IM_RO_CSS_ALTRI_COSTI+aAssCdpRnd.im_arr_a1 < 0
     then
      null;
     else
      update pdg_preventivo_spe_det set
       IM_RO_CSS_ALTRI_COSTI=IM_RO_CSS_ALTRI_COSTI+aAssCdpRnd.im_arr_a1
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza
        and TI_GESTIONE=aModSpeDet.ti_gestione
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce
 	    and PG_SPESA=aModSpeDet.pg_spesa;
      isProcessato:=true;
     end if;
	end if;
*/
	if
	        not isProcessato
	    and aAssCdpRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce
	    and aModSpeDet.CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SINGOLO
	then
     if
              (aModSpeDet.IM_RO_CSS_ALTRI_COSTI=0 and aAssCdpRnd.im_arr_a1 <> 0)
           or (aModSpeDet.IM_RAA_A2_COSTI_FINALI=0 and aAssCdpRnd.im_arr_a2 <> 0)
           or (aModSpeDet.IM_RAH_A3_COSTI_FINALI=0 and aAssCdpRnd.im_arr_a3 <> 0)
           or aModSpeDet.IM_RO_CSS_ALTRI_COSTI+aAssCdpRnd.im_arr_a1 < 0
           or aModSpeDet.IM_RAA_A2_COSTI_FINALI+aAssCdpRnd.im_arr_a2 < 0
           or aModSpeDet.IM_RAH_A3_COSTI_FINALI+aAssCdpRnd.im_arr_a3 < 0
     then
      null;
     else
      update pdg_preventivo_spe_det set
       IM_RO_CSS_ALTRI_COSTI=IM_RO_CSS_ALTRI_COSTI+aAssCdpRnd.im_arr_a1
      ,IM_RAA_A2_COSTI_FINALI=IM_RAA_A2_COSTI_FINALI+aAssCdpRnd.im_arr_a2
      ,IM_RAH_A3_COSTI_FINALI=IM_RAH_A3_COSTI_FINALI+aAssCdpRnd.im_arr_a3
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza
        and TI_GESTIONE=aModSpeDet.ti_gestione
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce
 	    and PG_SPESA=aModSpeDet.pg_spesa;
      isProcessato:=true;
     end if;
    end if;

	if
	      not isProcessato
	  and not (
	      aAssCdpRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce
	 -- stani  or aAssCdpRnd.ti_rapporto=CDP_TI_RAPP_DETERMINATO
	  ) and aModSpeDet.CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SINGOLO
	then
     if
              (aModSpeDet.IM_RK_CCS_SPESE_OGC = 0 and aAssCdpRnd.im_arr_a1 <> 0)
           or (aModSpeDet.IM_RAE_A2_SPESE_OGC = 0 and aAssCdpRnd.im_arr_a2 <> 0)
           or (aModSpeDet.IM_RAN_A3_SPESE_OGC = 0 and aAssCdpRnd.im_arr_a3 <> 0)
           or aModSpeDet.IM_RK_CCS_SPESE_OGC+aAssCdpRnd.im_arr_a1 < 0
           or aModSpeDet.IM_RAE_A2_SPESE_OGC+aAssCdpRnd.im_arr_a2 < 0
           or aModSpeDet.IM_RAN_A3_SPESE_OGC+aAssCdpRnd.im_arr_a3 < 0
     then
      null;
     else
      update pdg_preventivo_spe_det set
       IM_RK_CCS_SPESE_OGC=IM_RK_CCS_SPESE_OGC+aAssCdpRnd.im_arr_a1
      ,IM_RH_CCS_COSTI=IM_RH_CCS_COSTI+aAssCdpRnd.im_arr_a1
      ,IM_RAE_A2_SPESE_OGC=IM_RAE_A2_SPESE_OGC+aAssCdpRnd.im_arr_a2
      ,IM_RAA_A2_COSTI_FINALI=IM_RAA_A2_COSTI_FINALI+aAssCdpRnd.im_arr_a2
      ,IM_RAN_A3_SPESE_OGC=IM_RAN_A3_SPESE_OGC+aAssCdpRnd.im_arr_a3
      ,IM_RAH_A3_COSTI_FINALI=IM_RAH_A3_COSTI_FINALI+aAssCdpRnd.im_arr_a3
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza
        and TI_GESTIONE=aModSpeDet.ti_gestione
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce
 	    and PG_SPESA=aModSpeDet.pg_spesa;
      isProcessato:=true;
     end if;
    end if;
	if
          not isProcessato
	  and not (
	      aAssCdpRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce
	   -- stani or aAssCdpRnd.ti_rapporto=CDP_TI_RAPP_DETERMINATO
	  ) and
	  aModSpeDet.CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SCARICO
	then
     if
              (aModSpeDet.IM_RL_CCS_SPESE_OGC_ALTRA_UO=0 and aAssCdpRnd.im_arr_a1 <> 0)
           or (aModSpeDet.IM_RAF_A2_SPESE_OGC_ALTRA_UO=0 and aAssCdpRnd.im_arr_a2 <> 0)
           or (aModSpeDet.IM_RAO_A3_SPESE_OGC_ALTRA_UO=0 and aAssCdpRnd.im_arr_a3 <> 0)
           or aModSpeDet.IM_RL_CCS_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a1 < 0
           or aModSpeDet.IM_RAF_A2_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a2 < 0
           or aModSpeDet.IM_RAO_A3_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a3 < 0
     then
      null;
     else
      update pdg_preventivo_spe_det set
       IM_RL_CCS_SPESE_OGC_ALTRA_UO=IM_RL_CCS_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a1
      ,IM_RH_CCS_COSTI=IM_RH_CCS_COSTI+aAssCdpRnd.im_arr_a1
      ,IM_RAF_A2_SPESE_OGC_ALTRA_UO=IM_RAF_A2_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a2
      ,IM_RAA_A2_COSTI_FINALI=IM_RAA_A2_COSTI_FINALI+aAssCdpRnd.im_arr_a2
      ,IM_RAO_A3_SPESE_OGC_ALTRA_UO=IM_RAO_A3_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a3
      ,IM_RAH_A3_COSTI_FINALI=IM_RAH_A3_COSTI_FINALI+aAssCdpRnd.im_arr_a3
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza
        and TI_GESTIONE=aModSpeDet.ti_gestione
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce
 	    and PG_SPESA=aModSpeDet.pg_spesa;
      update pdg_preventivo_spe_det set
         IM_RU_SPESE_COSTI_ALTRUI=IM_RU_SPESE_COSTI_ALTRUI+aAssCdpRnd.im_arr_a1
        ,IM_RAG_A2_SPESE_COSTI_ALTRUI=IM_RAG_A2_SPESE_COSTI_ALTRUI+aAssCdpRnd.im_arr_a2
        ,IM_RAP_A3_SPESE_COSTI_ALTRUI=IM_RAP_A3_SPESE_COSTI_ALTRUI+aAssCdpRnd.im_arr_a3
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita_clgs
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita_clgs
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza_clgs
        and TI_GESTIONE=aModSpeDet.ti_gestione_clgs
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce_clgs
 	    and PG_SPESA=aModSpeDet.pg_spesa_clgs;
	   isProcessato:=true;
     end if;
    end if;
    if isProcessato then
	 update ass_cdp_round set
	     cd_centro_responsabilita = aModSpeDet.cd_centro_responsabilita
	    ,cd_linea_attivita = aModSpeDet.cd_linea_attivita
	    ,pg_spesa = aModSpeDet.pg_spesa
		,im_arr_a1_non_distr = round(aAssCdpRnd.im_arr_a1 - round(aAssCdpRnd.im_arr_a1,2),30)
		,im_arr_a2_non_distr = round(aAssCdpRnd.im_arr_a2 - round(aAssCdpRnd.im_arr_a2,2),30)
		,im_arr_a3_non_distr = round(aAssCdpRnd.im_arr_a3 - round(aAssCdpRnd.im_arr_a3,2),30)
	 where
            ESERCIZIO=aAssCdpRnd.esercizio
        and CD_CDR_ROOT=aAssCdpRnd.CD_CDR_ROOT
        and TI_APPARTENENZA=aAssCdpRnd.ti_appartenenza
        and TI_GESTIONE=aAssCdpRnd.ti_gestione
        and CD_ELEMENTO_VOCE=aAssCdpRnd.cd_elemento_voce
 	    and TI_RAPPORTO=aAssCdpRnd.ti_rapporto;
     isDettRedistrRottiFound:=true;
     exit;
    end if;
   end loop;
   if not isDettRedistrRottiFound then
    IBMERR001.RAISE_ERR_GENERICO('Impossibile redistribuire i rotti per cdr RUO:'||aAssCdpRnd.cd_cdr_root||' es:'||aAssCdpRnd.esercizio||'voce del piano:'||aAssCdpRnd.cd_elemento_voce||' tipo rapporto:'||aAssCdpRnd.ti_rapporto);
   end if;
  end loop;

 end;

 procedure annullaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
  aTSNow date;
  aCDRRUO cdr%rowtype;
  aCDRPersonale cdr%rowtype;
  aNum number(8);
 begin
  aTSNow:=sysdate;

  -- Leggo il CDR

  aCDRRUO:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

  if to_number(aCDRRUO.cd_proprio_cdr) != 0 then
   IBMERR001.RAISE_ERR_GENERICO('Operazione permessa solo su CDR di tipo RUO!');
  end if;

  select count(*) into aNum from ass_cdp_pdg where
   esercizio = aEsercizio
   and cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
		   );

  if aNum = 0 then
   IBMERR001.RAISE_ERR_GENERICO('Operazione di scarico non ancora effettuata per cdr '||aCdCdr);
  end if;

  -- Leggo il CDR del personale

  aCDRPersonale:=CNRCTB020.GETCDRPERSONALE;

  -- Lock PDG del CDR del personale

  CNRCTB050.LOCKPDG(aEsercizio, aCDRPersonale.cd_centro_responsabilita);

  -- Verifico che l'aggregato del CDR del personale NON sia chiuso in stato B

  if CNRCTB050.checkStatoAggregato(aEsercizio,
                                   aCDRPersonale.cd_centro_responsabilita,
								   CNRCTB050.STATO_AGGREGATO_FINALE) = 'Y'
  then
   IBMERR001.RAISE_ERR_GENERICO('L''agregato del CDR del personale risulta chiuso. Non ? possibile annullare lo scarico dei costi del personale');
  end if;

  -- Controllo modificabilita PDG CDR Personale

  if not (CNRCTB050.GETSTATO(aEsercizio,aCDRPersonale.cd_centro_responsabilita) in (
	                 CNRCTB050.STATO_PDG_INIZIALE,
	                 CNRCTB050.STATO_PDG_PRE_CHIUSURA,
	                 CNRCTB050.STATO_PDG_RC,
	                 CNRCTB050.STATO_PDG_RC_PRE_CHIUSURA
        )) then
   IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR del personale '||aCDRPersonale.cd_centro_responsabilita||' non e attualmente modificabile!');
  end if;

  -- Controllo modificabilita PDG CDR RUO/NRUO

  for aPDG in PDG_CON_CONFIG_SCR(aEsercizio, aCDRRUO.cd_centro_responsabilita) loop
   if not (aPDG.stato in (
	            CNRCTB050.STATO_PDG_INIZIALE,
	            CNRCTB050.STATO_PDG_PRE_CHIUSURA,
	            CNRCTB050.STATO_PDG_RC,
	            CNRCTB050.STATO_PDG_RC_PRE_CHIUSURA
          )) then
    IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR '||aPDG.cd_centro_responsabilita||' non e in stato iniziale o di pre-chiusura!');
   end if;
  end loop;

  -- Eliminazione dei dettagli collegati nel CDR del personale

  delete from pdg_preventivo_spe_det where
         esercizio = aEsercizio
	 and cd_centro_responsabilita = aCDRPersonale.cd_centro_responsabilita
	 and cd_centro_responsabilita_clgs in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		       esercizio = aEsercizio
		   and cd_cdr_root =  aCdCdr
		  )
     and categoria_dettaglio in (CNRCTB050.DETTAGLIO_CARICO)
     and origine = CNRCTB050.ORIGINE_STIPENDI;

  -- Eliminazione del dettaglio principale nel CDR di origine

  delete from pdg_preventivo_spe_det where
         esercizio = aEsercizio
	 and cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
		 )
     and categoria_dettaglio in (CNRCTB050.DETTAGLIO_SINGOLO, CNRCTB050.DETTAGLIO_SCARICO)
     and origine = CNRCTB050.ORIGINE_STIPENDI;

  update ass_cdp_la set
	     stato = STATO_CDP_NON_SCARICATO
	    ,dt_scarico = null
		,utuv = aUser
		,duva = aTSNow
		,pg_ver_rec = pg_ver_rec + 1
	where
	     esercizio = aEsercizio
     and mese = 0
	 and (
	          cd_centro_responsabilita = aCdCdr
		   or cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
           )
         );
 end;

 -- Verifica di scarico completo dei dipendenti su di un certo CDR
 --
 -- 1. Per ogni CDR responsabile di UO verifico che:
 --   1a. non esistono pending da altra UO
 --   1b. che tutte le  percentuali di matricole
 --       di altra UO accettate dall'UO in processo siano state configurate in ripartizione nell'UO al 100%
 --
 -- 2. Per ogni CDR verifico che:
 --     2a. tutti i costi configurati sul quel CDR siano stati scaricati su PDG

 function checkScaricoCDPCompleto(aEsercizio number, aCdCdr varchar2) return char is
  aNum NUMBER(8);
  aCdr cdr%rowtype;
  aCDRPersonale cdr%rowtype;
  aPDGConScarichi pdg_preventivo%rowtype;
  aTotNonConfig number;
  aNumConfNonCompl number;
 begin


  -- Leggo il CDR del personale e lock del PDG

  aCDRPersonale:=CNRCTB020.GETCDRPERSONALE;

  -- Se il cdr del personale e il CDR in processo devo applicare il controllo a tutti i CDR validi

  if aCDRPersonale.cd_centro_responsabilita = aCdCdr then
   aTotNonConfig:=0;
   -- Verifico che non esistano matricole non configurate in tutto il CNR
   select count(*) into aTotNonConfig from costo_del_dipendente a where
       a.esercizio=aEsercizio
   and a.mese = 0
   and not exists (
    select 1 from ass_cdp_la where
	     esercizio = a.esercizio
	 and id_matricola = a.id_matricola
     and mese = 0
   ) and not exists (
    select 1 from ass_cdp_uo where
     	 esercizio = a.esercizio
	 and id_matricola = a.id_matricola
     and mese = 0
	 and stato != STATO_ALTRAUO_RIFIUTATO
   );
   if aTotNonConfig > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Esistono matricole non configurate in termini di CDR/LA');
   end if;

   -- locko il cdr del personale
   CNRCTB050.LOCKPDG(aEsercizio, aCDRPersonale.cd_centro_responsabilita);
   for aCdrNonPersonale in (select * from v_cdr_valido where
                                  esercizio = aEsercizio
							  and cd_centro_responsabilita != aCDRPersonale.cd_centro_responsabilita) loop
    if checkScaricoCDPCompleto(aEsercizio, aCdrNonPersonale.cd_centro_responsabilita)='N' then
	 return 'N';
	end if;*
	begin
	 select * into aPDGConScarichi from pdg_preventivo a where
	      a.esercizio = aEsercizio
	  and a.cd_centro_responsabilita = aCdrNonPersonale.cd_centro_responsabilita
	  and exists (select 1 from ass_cdp_la where
	       esercizio = aEsercizio
	   and cd_centro_responsabilita = a.cd_centro_responsabilita
       and mese = 0
	  );
	  if not (aPDGConScarichi.stato = CNRCTB050.STATO_PDG_CHIUSURA or aPDGConScarichi.stato = CNRCTB050.STATO_PDG_FINALE) then
	   return 'N';
	  end if;
	exception when NO_DATA_FOUND then
	 null;
	end;
*/
   end loop;
  end if;

  -- Leggo il CDR

  aCDR:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

  -- Se si tratta di CDR RUO (non del personale) devo verificare che NON esistano matricole NON configurate appartenenti all'UO

  if aCDR.cd_centro_responsabilita != aCDRPersonale.cd_centro_responsabilita and to_number(aCDR.cd_proprio_cdr) = 0 then -- Cdr responsabile di UO
      aTotNonConfig:=0;
	  -- Verifico che non esistano matricole non configurate e appartenenti all'UO di cui aCDR e responsabile
      select count(*) into aTotNonConfig from costo_del_dipendente a where
           a.esercizio=aEsercizio
	   and a.mese=0
       and a.cd_unita_organizzativa=aCDR.cd_unita_organizzativa
	   and not exists (
          select 1 from ass_cdp_la where
   	           esercizio = a.esercizio
   	       and id_matricola = a.id_matricola
           and mese = 0
       )
	   and not exists (
          select 1 from ass_cdp_uo where
        	   esercizio = a.esercizio
   	       and id_matricola = a.id_matricola
           and mese = 0
	       and stato != STATO_ALTRAUO_RIFIUTATO
       );
      if aTotNonConfig > 0 then
       IBMERR001.RAISE_ERR_GENERICO('Esistono matricole non configurate in termini di CDR/LA');
      end if;
  end if;

  -- 1. Per ogni CDR responsabile di UO verifico che:
  --   1a. non esistono pending da altra UO
  --   1b. che tutte le  percentuali di matricole
  --       di altra UO accettate dall'UO in processo siano state configurate in ripartizione nell'UO al 100%

  if to_number(aCDR.cd_proprio_cdr) = 0 then

   -- Il totale di scarico configurato dei dipendenti dell'UO e 100%  (comprendento le quote verso altre UO accettate)

   aNumConfNonCompl:=0;
   select count(*) into aNumConfNonCompl from V_CDP_TOT_PRC where
        esercizio = aEsercizio
    and mese = 0
    and cd_unita_organizzativa = aCDR.cd_unita_organizzativa
    and (
          prc_a1 < 100
       or prc_a2 < 100
       or prc_a3 < 100
    );
   if aNumConfNonCompl > 0 then
    IBMERR001.RAISE_ERR_GENERICO('La configurazione di scarico dei dipendenti dell''UO '||aCDR.cd_unita_organizzativa||' non e completa!');
   end if;

   -- Non esistono pending da altra UO

   for aAssCDPUO in (
    select * from ASS_CDP_UO
     where
 	      esercizio = aEsercizio
      and mese = 0
	  and cd_unita_organizzativa = aCDR.cd_unita_organizzativa
	  and stato = STATO_ALTRAUO_NONDEFINITO
   ) loop
    return 'N';
   end loop;


   -- Tutto cio che e stato accettato da altra UO risulta configurato per lo scrico all'interno dell'UO al 100%

   for aAssCDPUO in (
    select * from ASS_CDP_UO
     where
 	      esercizio = aEsercizio
      and mese =0
	  and cd_unita_organizzativa = aCDR.cd_unita_organizzativa
	  and stato = STATO_ALTRAUO_ACCETTATO
   ) loop
    aNum:=0;
    select count(*) into aNum from (select id_matricola from ASS_CDP_LA where
         esercizio = aEsercizio
     and mese=0
     and id_matricola = aAssCDPUO.id_matricola
	 and fl_dip_altra_uo = 'Y'
	 and (esercizio, cd_centro_responsabilita) in (
	  select esercizio, cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
     	   esercizio = aEsercizio
	   and cd_cdr_root = aCDR.cd_centro_responsabilita
	 )
     group by id_matricola
     having
	     sum(prc_la_a1) != 100
      or sum(prc_la_a2) != 100
	  or sum(prc_la_a3) != 100
    );
    if aNum > 0 then
     return 'N';
    end if;
   end loop;

  end if; -- Fine blocco applicato a CDR responsabile di UO

 -- 2. Per ogni CDR verifico che:
 --     2a. tutti i costi configurati sul quel CDR siano stati scaricati su PDG

  aNum:=0;
  select count(*) into aNum from ASS_CDP_LA where
        esercizio = aEsercizio
    and mese =0
    and cd_centro_responsabilita = aCdCdr
    and stato <> STATO_CDP_SCARICATO;
  if aNum > 0 then
   return 'N';
  end if;

  return 'Y';
 end;

 procedure ins_ASS_CDP_PDG (aDest ASS_CDP_PDG%rowtype) is
  begin
   insert into ASS_CDP_PDG (
     ESERCIZIO
    ,TI_PREV_CONS
    ,ID_MATRICOLA
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,PG_SPESA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.TI_PREV_CONS
    ,aDest.ID_MATRICOLA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.PG_SPESA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_COSTO_DEL_DIPENDENTE (aDest COSTO_DEL_DIPENDENTE%rowtype) is
  begin
   insert into COSTO_DEL_DIPENDENTE (
     ESERCIZIO
    ,TI_PREV_CONS
    ,ID_MATRICOLA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_UNITA_ORGANIZZATIVA
    ,TI_RAPPORTO
    ,IM_A1
    ,IM_A2
    ,IM_A3
    ,IM_ONERI_CNR_A1
    ,IM_ONERI_CNR_A2
    ,IM_ONERI_CNR_A3
    ,IM_TFR_A1
    ,IM_TFR_A2
    ,IM_TFR_A3
    ,DT_SCARICO
    ,DACR
    ,UTCR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.TI_PREV_CONS
    ,aDest.ID_MATRICOLA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.TI_RAPPORTO
    ,aDest.IM_A1
    ,aDest.IM_A2
    ,aDest.IM_A3
    ,aDest.IM_ONERI_CNR_A1
    ,aDest.IM_ONERI_CNR_A2
    ,aDest.IM_ONERI_CNR_A3
    ,aDest.IM_TFR_A1
    ,aDest.IM_TFR_A2
    ,aDest.IM_TFR_A3
    ,aDest.DT_SCARICO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_ASS_CDP_ROUND (aDest ASS_CDP_ROUND%rowtype) is
  begin
   insert into ASS_CDP_ROUND (
     ESERCIZIO
    ,CD_CDR_ROOT
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,TI_RAPPORTO
    ,IM_ARR_A1
    ,IM_ARR_A2
    ,IM_ARR_A3
    ,IM_ARR_A1_NON_DISTR
    ,IM_ARR_A2_NON_DISTR
    ,IM_ARR_A3_NON_DISTR
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,PG_SPESA
    ,DACR
    ,UTUV
    ,UTCR
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDR_ROOT
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.TI_RAPPORTO
    ,aDest.IM_ARR_A1
    ,aDest.IM_ARR_A2
    ,aDest.IM_ARR_A3
    ,aDest.IM_ARR_A1_NON_DISTR
    ,aDest.IM_ARR_A2_NON_DISTR
    ,aDest.IM_ARR_A3_NON_DISTR
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.PG_SPESA
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

end;


