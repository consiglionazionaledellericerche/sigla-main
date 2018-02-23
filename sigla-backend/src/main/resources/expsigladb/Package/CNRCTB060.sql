--------------------------------------------------------
--  DDL for Package CNRCTB060
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB060" as
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
-- Fix scarico ripetuto su CDR personale nel caso esistano scarichi di altri cdr già effettuati
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
-- pre: Lo stato dell'aggregato del CDR del personale è CHIUSO (stato 'B')
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
-- pre: Lo stato dell'aggregato del CDR del personale è CHIUSO (stato 'B')
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
