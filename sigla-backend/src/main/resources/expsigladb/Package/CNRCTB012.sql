--------------------------------------------------------
--  DDL for Package CNRCTB012
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB012" as
--
-- CNRCTB012 - Package di gestione delle linee di attivita (comuni e non) in chiusura esercizio
-- Date: 12/07/2006
-- Version: 1.3
--
-- Dependency: IBMERR001 IBMUTL200
--
-- History:
--
-- Date: 21/07/2003
-- Version: 1.0
-- Creazione
--
-- Date: 22/07/2003
-- Version: 1.1
-- Completamento controlli
--
-- Date: 02/10/2003
-- Version: 1.2
-- LF - Aggiunto metodo estrazLdAcessate per script di estrazione linee di attività
-- cessate impropriamente
--
-- Date: 12/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

-- Tipologie di tipi di linee di attivita
--

-- Functions e Procedures:

-- Verifica se è possibile impostare l'esercizio di cessazione specificato sulla linea di attività specificata

-- pre-post-name: La linea di attività è utilizzata in esercizio successivo a quello di cessazione
-- pre: Esiste almeno un dettaglio nelle seguenti tabelle:
--                         1. ASS_CDP_LA
--                         2. ASS_CDP_PDG
--                         3. MAPPATURA_LA
--                         4. PDG_PREVENTIVO_ETR_DET
--                         5. PDG_PREVENTIVO_SPE_DET
--                         6. PDG_PREVENTIVO_ETR_VAR
--                         7. PDG_PREVENTIVO_SPE_VAR
--                         8. ANTICIPO
--                         9. COMPENSO
--                        10. ACCERTAMENTO_SCAD_VOCE
--                        11. OBBLIGAZIONE_SCAD_VOCE
--             che referenzia la linea di attività in esercizio successivo a quello di desiderata terminazione
-- post: Viene sollevata un'eccezione per la prima occorrenza trovata nell'ordine dato delle tabelle
--
-- pre-post-name: Linea attività utilizzata in obbligazione/accertamento dell'esercizio aEsCessazione eligibile di ribaltamento
-- pre: L'obbligazione o accertamento che specifica la linea di attività nel dettaglio, in aEsCessazione è eligibile di ribaltamento su nuovo esercizio
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Nessuna altra pre condizione verificata
-- pre: Nessun'altra precondizione è verificata
-- post: Il programma termina senza sollevare eccezioni
--
-- Parametri:
-- aEsCessazione -> esercizio cessazione della linea di attività
-- aCdCdr -> Centro di responsabilità linea di attività su cui effettuare il controllo
-- aCdLa -> Codice linea di attività su cui effettuare il controllo

 procedure checkCessazioneLa(aEsCessazione number,aCdCdr varchar2,aCdLa varchar2);

 procedure estrazLdAcessate(aEs number,aUser varchar2);

end;
