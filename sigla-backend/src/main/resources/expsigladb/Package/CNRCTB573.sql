--------------------------------------------------------
--  DDL for Package CNRCTB573
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB573" AS
--==============================================================================
--
-- CNRCTB573 - Liquidazione massa contributi/ritenute
--
-- Date: 05/12/2003
-- Version: 1.3
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 22/05/2003
-- Version: 1.0
-- Creazione
--
-- Date: 23/05/2003
-- Version: 1.1
-- Gestione batch
--
-- Date: 03/12/2003
-- Version: 1.2
-- Documentazione
--
-- Date: 05/12/2003
-- Version: 1.3
-- Permette anche la liquidazione di gruppi cori nulli accentrati
--
-- Date: 20/12/2005
-- Version: 1.4
-- Creazione nuove procedure "job_liquid_cori_massa" e "job_liquid_cori_massa_istituti" 
-- per la gestione della Liquidazione CORI massiva. 
-- Tale Liquidazione viene fatta solo per gli Istituti (CDS non SAC) e solo per i gruppi accentrati.
-- Solo se è "Da Esercizio Precedente" vengono presi anche i gruppi negativi.
--==============================================================================
--
-- Constants
--

LOG_TIPO_LIQCORIMAS CONSTANT VARCHAR2(20):='LIQUID_CORI_MASS00';

--
-- Functions e Procedures
--

-- Calcolo liquidazione CORI di massa da esercizio precedente
--
-- pre-post-name: Data da, Data a, esercizio o utente non specificati per la liquidazione
-- pre: alcuni parametri di imput necessari non sono specificati
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Calcolo della liquidazione massiva da esercizio precedente
-- pre: L'utente chiede che venga effettuata la liquidazione massiva da esercizio precedente
-- post:
--    Per ogni UO diversa dall'UO di versamento accentrato ed esistente nel nuovo esercizio (aEs specificato)
--      Viene calcolato il pg di liquidazione
--      Viene richiamato i calcolo della liqiuidazione CORI da esercizio precedente
--      I gruppi di versamento accentrato calcolati con la liquidazione, vengono inseriti in vista VSX per il
--      processo di liquidazione vera e propria
--      Viene effettuata la liquidazione, svuotata la vista VSX
--      Se viene sollevato errore questo viene loggato con log err sui log
--      Se non viene sollevato errore questo viene loggato con log inf sui log
--    Al termine dell'operazione arriva un messaggio all'utente che ha lanciato l'operazione
--

 procedure job_liquidazione_cori_massa
   (
    job NUMBER, pg_exec NUMBER, next_date DATE,
    aEs number,
	aDtDa date,
	aDtA date,
	aUser varchar2
   );
 Procedure job_liquid_cori_massa
   (
    aEs NUMBER, es_prec VARCHAR2, aDtDa DATE, aDtA DATE, aUser VARCHAR2
   );
   
 Procedure job_liquid_cori_massa_istituti
   (
    job NUMBER,  pg_ex NUMBER, next_date DATE,
    aEs NUMBER, es_prec VARCHAR2, aDtDa DATE, aDtA DATE,  aUser VARCHAR2
   ); 
End;
