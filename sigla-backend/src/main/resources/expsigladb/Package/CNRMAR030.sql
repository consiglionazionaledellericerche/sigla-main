--------------------------------------------------------
--  DDL for Package CNRMAR030
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMAR030" as
--
-- CNRMAR030 - Package controllo/martello obbligazioni/accertamenti (batch)
-- Date: 14/07/2006
-- Version: 1.33
--
-- Package per il martellamento/verifica disallineamento saldi
--
-- Dependency: IBMUTL 200/210
--
-- History:
--
-- Date: 11/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 29/01/2003
-- Version: 1.1
-- Estensioni
--
-- Date: 24/04/2003
-- Version: 1.2
-- Parte 1, verifica importo ass doc amm delle scad accertamenti con
-- 		  	i il saldo di tutti i doc amm associati alla scadenza acc.
--
-- Date: 29/04/2003
-- Version: 1.3
-- Parte 2, verifica importo ass doc amm delle scad accertamenti o scad obblig.
-- 		  	con il saldo di tutti i doc amm associati alla scadenza in esame
--
-- Date: 05/05/2003
-- Version: 1.4
-- Parte 3, verifica importo ass doc amm delle scad accertamenti o scad obblig.
-- 		  	con il saldo di tutti i doc amm associati alla scadenza in esame
--
-- Date: 07/05/2003
-- Version: 1.5
-- Parte 4, Introduzione fondo economale
--
-- Date: 08/05/2003
-- Version: 1.6
-- Parte 5  revisione log errore - introduzione errore 025 - 525
--
-- Date: 08/05/2003
-- Version: 1.7
-- Parte 6  introduzione logica compenso
--
-- Date: 08/05/2003
-- Version: 1.8
-- Parte 7  verifica stato annulato dei documenti amministrativi associati
--
-- Date: 09/05/2003
-- Version: 1.9
-- Parte 8  Fix query recupero saldo missione
--
-- Date: 09/05/2003
-- Version: 1.10
-- Parte 9  Fix, aggiunto abs quando calcolo il saldo dei doc generici
--
-- Date: 21/05/2003
-- Version: 1.11
-- Parte 10 Modificata struttura log in output
--
-- Date: 30/05/2003
-- Version: 1.12
-- Regole supp iva fatture passive
--
-- Date: 30/05/2003
-- Version: 1.13
-- Fix Regole supp iva fatture passive mancava leettura testata fattura in caso
-- di legame con sospeso
--
-- Date: 10/06/2003
-- Version: 1.14
-- Fix a query "Ciclo su tutte le Obbligazione relative al cds e esercizio in input."
--
-- Date: 30/06/2003
-- Version: 1.15
-- Esclusione dei documenti generici di reintegro del fondo, quando si calcola il totale
-- di tutti i doc gen associati ad una obbligazione.
--
-- Date: 03/07/2003
-- Version: 1.16
-- Modificata funzione EsisteSpesaFondo, invece di estrarre le righe del fondo
-- legate ad una obbligazione scadenza, ora vengono contate.
--
-- Date: 03/07/2003
-- Version: 1.17
-- Commit inserito per rilasciare tutte le risorse occupate
--
-- Date: 08/07/2003
-- Version: 1.18
-- Modificato il calcolo del saldo fatture Passive per obbligazioni
-- inserita la regola che se una fattura passiva risulta ti_bene_servizio = 'B'
-- e Istituzionale e (intraUE o San Marino) allora im_obbligazione_scadenza = im_sospeso + im_iva
--
-- Date: 08/07/2003
-- Version: 1.19
-- Eliminato Commit
--
-- Date: 15/07/2003
-- Version: 1.20
-- Inserita gestione particolare per il controllo im_associato_doc_amm relativo
-- alle scadenze di obbligazione che soddisfano le seguenti condizioni
-- la scadenza è legata ad una fattura (riga di fattura ) a sua volta associata
-- con un sospeso. Inoltre la fattura in esame risulta avere più righe associate
-- a diverse obbligazioni. In questo caso deve succedere sempre che
-- l'importo di pagamento della lettera deve coincidere con la somma dei campi
-- im_associato_doc_amm delle varie scadenze. Vale sempre la regola introdotta
-- nella versione 1.18
--
-- Date: 18/07/2003
-- Version: 1.21
-- Inserita ulteriori regole per il trattamento del saldo derivante da fattura passiva
--
-- Date: 18/07/2003
-- Version: 1.22
-- Inserito ciclo sulla testata della fattura passiva
--
-- Date: 18/07/2003
-- Version: 1.23
-- Fix sulla scansine delle righe di fattura passiva
--
-- Date: 18/07/2003
-- Version: 1.24
-- Fix sulla valorizzazione di Fatture Bene o Servizio
-- la fattura passiva viene considerata di tipo bene anche
-- quando il campo ti_bene_servizio = '*'
--
-- Date: 21/07/2003
-- Version: 1.25
-- Fix sul caso in cui una fattura passiva risulta legata a più obbligazioni
-- o a più scadenze di obbligazione
--
-- Date: 21/07/2003
-- Version: 1.26
-- Fix 2 sul caso in cui una fattura passiva risulta legata a più obbligazioni
-- o a più scadenze di obbligazione
--
-- Date: 12/09/2003
-- Version: 1.27
-- Fix inserito controllo di esclusione delle scadenze di obbligazione e accertamento
-- che risultano riportate, queste non devono essere considerate nella quadratura
-- relativa al totale dei doc amministrativi
--
-- Date: 17/09/2003
-- Version: 1.28
-- inserito controllo dei saldi associati a documenti amministrativi, per le scadenze Obb/Acc
-- che risultano ribaltate ad esercizio successivo e con im_Associato_doc_amm = 0
-- Per queste scadenze infatti la procedura di ribaltamento non provvede a cambiare
-- i riferimenti nei doc amm associati, per cui se non venissoro controllate, potrebbe
-- non essere rilevato un errore indotto nell'esercizio corrente.
--
-- Date: 27/11/2003
-- Version: 1.29
-- Inserita where condition fl_congelata ='N' per scartare le fatture passive congelate
-- quando si totalizzano i doc amm associati ad una scadenza (obb,acc)
-- Rimossa totalizzazione delle contributo ritenute nel calcolo del saldo doc amm
-- sulle scadenze obbligazione
-- Rimosso saldo del compenso nella totalizzazione del saldo doc amm sulle scadenze
-- accertamento quando queste sono del cds ENTE = 999
--
-- Date: 01/12/2003
-- Version: 1.30
-- 1) Rimossa totalizzazione delle contributo ritenute nel calcolo del saldo doc amm sulle scadenze obbligazione
-- 2) Rimosso saldo del compenso nella totalizzazione del saldo doc amm sulle scadenze  accertamento quando queste sono del cds ENTE = 999
--
-- Date: 12/01/2004
-- Version: 1.31
-- inserito filtro sullo stato dell'obbligazione e sulla data di cancellazione dell'accertamento
-- nelle funzioni EsisteScadRiportata.
--
-- Date: 20/01/2004
-- Version: 1.32
-- inserito filtro sul controllo di im_associato_doc_amm (obbligazione/accertamento)
-- il filtro riguarda il caso in cui esiste una scadenza riportata ma solo in stato annulato
-- in questo caso il controllo sulla totalizzazione dei doc amm viene fatto solo se il campo
-- riportato <> 'Y'
--
-- Date: 14/07/2006
-- Version: 1.33
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants
--
TIPO_LOG_MAR_OBBACC CONSTANT VARCHAR2(20):='MAR_OBB_ACC00';
NOTA_CREDITO CONSTANT CHAR(1):='C';
STATO_ANNULLATO CONSTANT CHAR(1):='A';
STATO_DEFINITIVO CONSTANT CHAR(1):='D';
-- Tipi di disallineamenti supportati

D_PRIMI CONSTANT VARCHAR2(10):='D_PRIMI'; --  Tipo disallineamento


D_PRIMI000 CONSTANT VARCHAR2(60):='ACC-D_PRIMI000-IM_ACC<>SUM(SCAD)'; -- Somma importi scadenze diverso da importo obb: ritorna imp test - somma imp scad
D_PRIMI005 CONSTANT VARCHAR2(60):='ACC-D_PRIMI005-IM_SCAD<>SUM(SCADV)'; -- Somma dettagli di scadenza diverso da imp. scadenza obb: ritorna imp scade - somma imp scad voce
D_PRIMI010 CONSTANT VARCHAR2(60):='ACC-D_PRIMI010-SCAD.IM_ASS_DOC_AMM<SCAD.IM_ASS_DOC_CONT'; -- Importo ass. doc. cont. > importo associato doc. amm
D_PRIMI015 CONSTANT VARCHAR2(60):='ACC-D_PRIMI015-SCAD.IM_ASS_DOC_CONT<>SUM(IM_REV_RIGA)'; -- Saldo reversali collegate disallineato con tot documenti collegati: ritorna im_associato_doc_contabile - aSaldoAutorizz. collegati
D_PRIMI020 CONSTANT VARCHAR2(60):='ACC-D_PRIMI020-SCAD.IM_ASS_DOC_AMM<>SUM(DOC-AMM)'; -- Importo associato doc. amm <> Somma importi dei doc amm associati ad accertamenti ritorna : scdenza im_associato_doc_amm - aSaldoAssDocAmm colleagti
D_PRIMI025 CONSTANT VARCHAR2(60):='ACC-D_PRIMI025-SCAD.IM_ASS_DOC_AMM<>IM_SCAD'; -- Importo associato doc. amm <> imp scadenza ritorna : scdenza im_associato_doc_amm - imp scadenza
--
D_PRIMI500 CONSTANT VARCHAR2(60):='OBB-D_PRIMI500-IM_OBB<>SUM(SCAD)'; -- Somma importi scadenze diverso da importo obb: ritorna imp test - somma imp scad
D_PRIMI505 CONSTANT VARCHAR2(60):='OBB-D_PRIMI505-IM_SCAD<>SUM(SCADV)'; -- Somma dettagli di scadenza diverso da imp. scadenza obb: ritorna imp scade - somma imp scad voce
D_PRIMI510 CONSTANT VARCHAR2(60):='OBB-D_PRIMI510-SCAD.IM_ASS_DOC_AMM<SCAD.IM_ASS_DOC_CONT'; -- Importo ass. doc. cont. > importo associato doc. amm
D_PRIMI515 CONSTANT VARCHAR2(60):='OBB-D_PRIMI515-SCAD.IM_ASS_DOC_CONT<> SUM(IM_MAN_RIGA)'; -- Saldo mandati collegati disallineato con tot documenti collegati: ritorna im_associato_doc_contabile - aSaldoAutorizz. collegati
D_PRIMI520 CONSTANT VARCHAR2(60):='OBB-D_PRIMI520-SCAD.IM_ASS_DOC_AMM<>SUM(DOC-AMM)'; -- Importo associato doc. amm <> Somma importi dei doc amm associati ad obbligazioni ritorna : scadenza im_associato_doc_amm - aSaldoAssDocAmm colleagti
D_PRIMI525 CONSTANT VARCHAR2(60):='OBB-D_PRIMI525-SCAD.IM_ASS_DOC_AMM<>IM_SCAD'; -- Importo associato doc. amm <> imp scadenza ritorna : scdenza im_associato_doc_amm - imp scadenza
--

-- Functions e Procedures

-- Parametri:

-- aEs -> Esercizio
-- aCDS -> Esercizio
-- isModifica -> Y = update N preview

 procedure job_mar_primi00(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2, isModifica char);

 function MSG_DIS_PRIMI(aTipo varchar2, aAcc accertamento%rowtype, aNota varchar2) return varchar2;
 function MSG_DIS_PRIMI(aTipo varchar2, aObb obbligazione%rowtype, aNota varchar2) return varchar2;


end;
