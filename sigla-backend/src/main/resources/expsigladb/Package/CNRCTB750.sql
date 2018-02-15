--------------------------------------------------------
--  DDL for Package CNRCTB750
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB750" AS
--==================================================================================================
--
-- CNRCTB750 - package di utilità per gestione cassiere
--
-- Date: 08/01/2004
-- Version: 1.16
--
-- Dependency:
--
-- History:
--
-- Date: 20/03/2003
-- Version: 1.0
-- Creazione package
--
-- Date: 15/04/2003
-- Version: 1.1
-- Modifica struttura EXT_CASSIERE00
--
-- Date: 16/04/2003
-- Version: 1.2
-- Introdotta gestione batch
--
-- Date: 17/04/2003
-- Version: 1.3
-- Completamento
--
-- Date: 22/04/2003
-- Version: 1.4
-- Corretta gestione sospesi da Banca d'Italia
--
-- Date: 26/04/2003
-- Version: 1.5
-- Gestione registrazione del log collegato al processo dell'interfaccia
--
-- Date: 26/05/2003
-- Version: 1.6
-- Gestione annullamento sospesi e caricamento riscontri
--
-- Date: 29/05/2003
-- Version: 1.7
-- Fix nuove funzionalita
--
-- Date: 05/06/2003
-- Version: 1.8
-- Gestione dell'update/annullamento di righe in BFRAME_BLOB
--
-- Date: 15/09/2003
-- Version: 1.9
-- Aggiornamento documentazione
--
-- Date: 32/10/2003
-- Version: 1.10
-- Introduzione della gestione di pagamento relativo alla Banca d'Italia
-- Introduzione del nuovo tracciato record dei Log
-- Azzeramento delle variabili aCds e aR01 all'interno del ciclo di calcolo
--
-- Date: 27/10/2003
-- Version: 1.11
-- Fix di non processo reversali e mandati annullati
-- Fix segnalazione errore nel caso la reversale ente non sia da Banca d'Italia mentre lo è il riscontro
-- della Banca.
-- Segnalazione di errore nel caso la reversale sia parte da Banca d'Italia e parte no.
--
-- Date: 27/10/2003
-- Version: 1.12
-- Fix minori: errore di log della reversale/mandato non trovato/annullato.
-- Fix segnalazione errore nel caso la reversale sia da Banca d'Italia mentre il non lo è il riscontro
-- della Banca.
--
-- Date: 20/11/2003
-- Version: 1.13
-- Fix distinta cassiere: errore nella selezione dei mandati/reversali da inserire in una distinta.
--  Se uno dei documenti inseriti è collegato ad un compenso al quale sono collegati Mand./Rev. CORI,
--	(tabella ASS_COMP_DOC_CONT_NMP), devono essere indicati anche quei Mand./Rev.
--
-- Date: 25/11/2003
-- Version: 1.14
-- Fix sul metodo checkDocContForDistCas: errore nella ricerca dei doc. cont. associati ad un eventuale
-- compenso.
--
-- Date: 27/11/2003
-- Version: 1.15
-- Fix sul metodo checkDocContForDistCas: Lock delle tabelle DISTINTA_CASSIERE e ASS_COMP_DOC_CONT_NMP.
--
-- Date: 08/01/2004
-- Version: 1.16
-- Fix mancato aggiornamento dello stato processato del record di storno sospeso
-- Aggiornamento di utuv,duva,pg_ver_rec aggiornamento dello stato processato del record di storno/inserimento sospeso
--
--==================================================================================================
--
-- Constants
--

CONTO_CORRENTE_SPECIALE CONSTANT VARCHAR2(50) := 'CONTO_CORRENTE_SPECIALE';
CONTO_CORRENTE_ENTE CONSTANT VARCHAR2(100) := 'ENTE';

-- ABIBNL determinato utilizzato CONFIGURAZIONE_CNR
ABIBNL number(5);

--
-- Functions e Procedures
--
 procedure checkRigaFile(aRiga ext_cassiere00%rowtype);

-- Pre-post-name: Invocazione del batch di processing dell'interfaccia di ritorno cassiere
-- pre: viene richiesta l'elaborazione dell'interfaccia di ritorno cassiere nell'esercizio spec. per il file specificato
--      dall'utente specificato
-- post: viene creato un job oracle (che invoca job_interfaccia_cassiere) sottomesso per l'esecuzione in background e
--       notificato all'utente il completamento dell'operazione di sottomissione.
--
-- Parametri:
--   aEs -> esercizio contabile
--   aNomeFile -> Nome del file dati
--   aUser -> utente che effettua l'operazione
 procedure processaInterfaccia(aEs number, aNomeFile varchar2,aUser varchar2);

-- Job di caricamento dell'interfaccia di ritorno cassiere per la gestione di:
--  1. Sospesi (Inserimento/Cancellazione)
--  2. Riscontri (Solo inserimento)
--
-- Pre-post-name: Record di testat T01 non trovato
-- pre: Nel loop delle righe del file cassiere viene trovato un dato di tipo TXX con XX <> 01 prima di trovare un record di tipo T01
-- post: Viene sollevata un'eccezione
--
-- Pre-post-name: Record di testat T01 con esercizio diverso dall'esercizio specificato
-- pre: L'esercizio aEs è diverso da quello del record di tipo T01 trovato
-- post: Viene sollevata un'eccezione
--
-- Pre-post-name: La reversale o il mandato risultano già parzialmente riscontrata manualmente
-- pre: Esistono riscontri manuali per il documento autorizzatorio
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: La reversale o il mandato risultano annullati
-- pre: La reversale ed il mandato specificato sono annullati
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: L'importo dei riscontri supera l'importo della reversale (mandato)
-- pre: il totale riscontrato risulta superiore all'importo del documento
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Reversale CNR non da Banca d'Italia mentre il riscontro della Banca è da Banca d'Italia
-- pre: La Reversale CNR non è da Banca d'Italia mentre il riscontro specificato si
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Reversale CNR da Banca d'Italia mentre il riscontro della Banca non è da Banca d'Italia
-- pre: La Reversale CNR è da Banca d'Italia mentre il riscontro specificato nell'interfaccia no
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Reversale in parte da Banca d'Italia ed in parte no
-- pre: La Reversale risulta per parte da Banca d'Italia e per parte no
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Documento da processare di CDS con specifica cc banca d'italia
-- pre: NUMERO_CONT_TESO del record T30 = '000000218154' e CDS letto da T01 diverso da Ente
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Tipo di oridinativo non compatibile (T30)
-- pre:  se il tipo di ordinativo (TIPO_ORDINATIVO) è diverso da 'R' o 'M' su record T30
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Stato ordinativo non compatibile per record T30
-- pre: Lo stato dell'ordinativo (STATO_ORDINATIVO) = '03' e il segno (SEGNO_IMPORTO_OPERAZIONE) = 1 (record T30)
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Sospeso già annullato
-- pre: Richiesto annullamento di sospeso (record T32 e STATO_SOSP del record = '05') e il sospeso è già annullato
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Sospeso già utilizzato
-- pre: Richiesto annullamento di sospeso (record T32 e STATO_SOSP del record = '05') e importo associato a doc.
--      autorizzatori o 1210 è diverso da 0
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Caricamento dell'interfaccia di ritorno cassiere
-- pre: viene richiesta l'elaborazione dell'interfaccia di ritorno cassiere nell'esercizio spec. per il file specificato
--      dall'utente specificato
-- post: Vengono eseguite le seguenti operiazioni:
--       lock della tabella EXT_CASSIERE00 che contiene il file con il tracciato della banca
--       Per ogni record in STATO iniziale presente in EXT_CASSIERE00 per l'esercizio e nome file specificati
--       ordinati per progressivo record:
--         1. Il record è di tipo T01 (record aR01)
--         2. Il record è di tipo T30 (Riscontri/Mandati record aR30)
--            	 Se lo stato dell'ordinativo (STATO_ORDINATIVO) = '03' e il segno (SEGNO_IMPORTO_OPERAZIONE) = 1
--                Determina il CDS dal record T01 corrispondente
--                Se il tipo di ordinativo (TIPO_ORDINATIVO) è 'R' (Reversale)
--                  > Crea il record di riscontro (aRisc) in tabella SOSPESO con le seguenti caratteristiche:
--                            ...
--							aRisc.DT_REGISTRAZIONE:=aR30.DATA_ESECUZIONE (T30)
--                            aRisc.DS_ANAGRAFICO:=aR30.DESCRIZIONE_BENEFICIARIODEBITO (T30)
--                            aRisc.CAUSALE:=aR30.CAUSALE;
-- 	                        Se aR01.NUMERO_CONT_TESO = '000000218154' allora
--                             Se il CDS è l'Ente
--                        	  aRisc.TI_CC_BI:='B'; (Tipo conto corrente da Banca d'Italia)
--                        	altrimenti
--                             aRisc.TI_CC_BI:='C'; (Conto corrente CDS)
--                            aRisc.IM_SOSPESO:=aR30.IMPORTO_OPERAZIONE;
--                            aRisc.IM_ASSOCIATO:=aR30.IMPORTO_OPERAZIONE;
--                            ...
--                  > Aggiorno il dettaglio det ETR
--                  > Aggiorna l'importo incassato della reversale
--                  > Se il totale riscontrato alla data = importo reversale
--                         Invoca CNRCTB037.riscontroReversale per le azioni di aggiornamento dei saldi Incassato
--                altrimenti (Mandato 'M')
--                  > Crea il record di riscontro (aRisc) in tabella SOSPESO con le seguenti caratteristiche:
--                            ...
--							aRisc.DT_REGISTRAZIONE:=aR30.DATA_ESECUZIONE (T30)
--                            aRisc.DS_ANAGRAFICO:=aR30.DESCRIZIONE_BENEFICIARIODEBITO (T30)
--                            aRisc.CAUSALE:=aR30.CAUSALE;
-- 	                        Se aR01.NUMERO_CONT_TESO = '000000218154' allora
--                             Se il CDS è l'Ente
--                        	  aRisc.TI_CC_BI:='B'; (Tipo conto corrente da Banca d'Italia)
--                        	altrimenti
--                             aRisc.TI_CC_BI:='C'; (Conto corrente CDS)
--                            aRisc.IM_SOSPESO:=aR30.IMPORTO_OPERAZIONE;
--                            aRisc.IM_ASSOCIATO:=aR30.IMPORTO_OPERAZIONE;
--                            ...
--                  > Aggiorno il dettaglio det USC
--                  > Aggiorna l'importo incassato del mandato
--                  > Se il totale riscontrato alla data = importo mandato
--                         Invoca CNRCTB037.riscontroMandato per le azioni di aggiornamento dei saldi Pagato
--         3. Il record è di tipo T32 (Sospesi, record aR32)
--             	 Se GESTIONE STORNO (aR32.STATO_SOSP = '05')
--                 Viene aggiornato lo stato a stornato (fl_stornato) del sopeso padre e dei suoi figli
--             	 Altrimenti se GESTIONE INSERIMENTO (aR32.STATO_SOSP = '02') e aR32.IMPORT = aR32.IMPORT_RESI_OPE
--               e aR32.SEGNO_IMPO = aR32.SEGNO_IMPO_RESI_OPER ed aR32.SEGNO_IMPO = 1
--                 Viene creato un record nella tabella SOPESO per il sospeso padre (aSospeso) con le sguenti caratteristiche
--				      ...
--                    aSospeso.CD_SOSPESO:=aR32.NUMERO_SOSP;
--                    aSospeso.DT_REGISTRAZIONE:=TO_DATE(aR32.DATA_OPER,'YYYYMMDD')
--                    aSospeso.DS_ANAGRAFICO:=aR32.DESCRI_BENE
--                    aSospeso.CAUSALE:=aR32.CAUSAL_1A_PART concatenata con aR32.CAUSAL_2A_PART
--                    -- identificazione banca d'Italia solo per sospesi ENTE
--                    Se il aR01.NUMERO_CONT_TESO = '000000218154'
--                     Se il cds è l'Ente
--                      aSospeso.TI_CC_BI:='B' (Banca d'Italia)
--                    altrimenti
--                     aSospeso.TI_CC_BI:='C'
--                    aSospeso.IM_SOSPESO:=aR32.IMPORT*aR32.SEGNO_IMPO
--                 Viene creato un record per il sospeso figlio (aSF) con la seguente caratteristica:
--                 Se aCds.cd_tipo_unita diversa da Ente
--                  aSF.CD_CDS_ORIGINE:=aSospeso.CD_CDS
--                  aSF.CD_UO_ORIGINE:=null
--                  aSF.STATO_SOSPESO:='A' --ASSEGNATO A CDS
--                 altrimenti
--                  aSF.CD_CDS_ORIGINE:=null
--                  aSF.CD_UO_ORIGINE:=null
--                  aSF.STATO_SOSPESO:='I' --INIZIALE
--       Al termine dell'operazione viene mandato attraverso il package 205 un messaggio applicativo all'utente
--       con informazioni riassuntive sull'operazione.
--       Gli errori vengono loggati con l'usuale meccanismo di log.
--       Se una riga di interfaccia viene processata con successo, viene effettuato anche un commit.
--       Altrimenti viene effetuato un rollback.
--
-- Parametri:
--   job,pg_exec,next_date -> parametri sistemistici dell'interfaccia di invocazione JOBs Oracle
--   aEs -> esercizio contabile
--   aNomeFile -> Nome del file dati
--   aUser -> utente che effettua l'operazione

 procedure job_interfaccia_cassiere (
  job NUMBER,
  pg_exec NUMBER,
  next_date DATE,
  aEs number,
  aNomeFile varchar2,
  aUser varchar2
 );

 procedure carica_ext_cassiere(aTipo varchar2, aPath varchar2, aFilename varchar2, aClob in out clob);
 procedure checkRemoveFile(aTipo varchar2, aPath varchar2, aFilename varchar2, aClob in out clob);

-- Pre-post-name: Controlla tutti i documenti inseriti in distinta.
-- pre: Cicla su tutti i documenti inseriti nella distinta.

-- post: Per ogni documento inserito in distinta, controlla nella tabella ASS_COMP_DOC_CONT_NMP
-- 		se è associato ad un compenso. In caso affermativo, controlla che tutti i Mand./Rev. collegati
--		al compenso, siano presenti nella distinta.
--		Se un documento è associato a Compenso il quale è associato a Mand./Rev. CORI non presenti nella distinta,
--		viene lanciata una eccezione indicando all'utente che il documento è associato a
-- 		doc. cont. legati ad un Compenso che ha dei Mand./Rev. che devono essere inclusi nella distinta.
--
-- Parametri:
--   aCdCds -> Cd_Cds della distinta
--   aEs 	-> esercizio della distinta
--   aCdUO 	-> Cd_uo della distinta
--   aPgDistinta -> progressivo della distinta

 procedure checkDocContForDistCas(
  aCdCds varchar2,
  aEs number,
  aCdUO varchar2,
  aPgDistinta number
 );
 procedure checkDocContForDistCasAnn(
   aCdCds varchar2,
   aEs number,
   aCdUO varchar2,
   aPgDistinta number
 );
END;
