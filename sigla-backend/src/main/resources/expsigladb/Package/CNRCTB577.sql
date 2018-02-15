--------------------------------------------------------
--  DDL for Package CNRCTB577
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB577" AS
--==============================================================================
--
-- CNRCTB577 - Caricamento estrazione dettaglio cori per matricola
--
-- Date: 26/08/2004
-- Version: 1.8
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 30/07/2003
-- Version: 1.0
-- Creazione
--
-- Date: 31/07/2003
-- Version: 1.1
-- Test estrazione CORI
--
-- Date: 01/08/2003
-- Version: 1.2
-- Fix su gestione tassazione separata
--
-- Date: 06/08/2003
-- Version: 1.3
-- Gestione corretta cd_imponibile A1/I1
-- Gestione corretta estrazione aggregato
--
-- Date: 10/12/2003
-- Version: 1.4
-- Modifica logica di estrazione CNR_ESTRAZIONE_CORI_AGG
--
-- Date: 12/12/2003
-- Version: 1.5
-- Completamento richiesta 712
-- Fix errore su estrazione CORI da liquid_gruppo_cori_dett -> non usava l'esercizio_contributo_ritenuta
--
-- Date: 12/12/2003
-- Version: 1.6
-- Completamento richiesta 712: Reset pg_riga anche su cambiamento esercizio_compenso
--
-- Date: 19/12/2003
-- Version: 1.7
-- Fix errore 715
--
-- Date: 26/08/2004
-- Version: 1.8
-- Fix errore estrazione CORI da liquid_dett quando l'esercizio del cori differisce da quello della liquidazione
--
-- Date: 17/11/2005
-- Versione: 1.9
-- Aggiunta la procedura "scaricaDettNonLiquid" che scarica nella tabella CNR_ESTRAZIONE_CORI anche tutto quello
-- che non e' stato ancora versato (questo perche' ad esempio le previdenziali vanno versate a gennaio dell'anno
-- successivo e l'estrazione per i conguagli deve essere fatta prima)
--
-- Date: 28/12/2006
-- Versione: 1.10
-- Gestite le date di competenza dei compensi
-- Gestite le varie tipologie di pagamento:
-- "1" - Tassazione anno corrente
-- "5" - Tassazione anni precedenti
-- "4" - Tassazione separata (per la tassazione separata non distinguiamo anno corrente e anno precedente)
--
-- Date: 17/03/2009
-- Versione: 1.12
-- Gestito lo Stralcio mensile dei dati dei dipendenti alla procedura NSIP
--
-- Date: 19/05/2009
-- Versione: 1.11
-- Gestite nello Stralcio mensile la ritenuta sospesa e le ritenute a zero ma con imponibile valorizzato
--
-- Date: 15/04/2011
-- Versione: 1.11
-- Gestito il fatto che la liquidazione può essere effettuata anche dalla 999.000
--==============================================================================
--
-- Constants
--
-- Functions e Procedures
--
 procedure ins_CNR_ESTRAZIONE_CORI (aDest CNR_ESTRAZIONE_CORI%rowtype);
 procedure ins_CNR_ESTRAZIONE_CORI_AGG (aDest CNR_ESTRAZIONE_CORI_AGG%rowtype);

 procedure scaricaDettLiquid(aLiquid liquid_cori%rowtype,aTSNow date,aUser varchar2);
 Procedure scaricaDettNonLiquid(aLiquidFitt liquid_cori%rowtype,aTSNow date,aUser varchar2);
 procedure aggregaDettLiquid(aEs number, aTSNow date,aUser varchar2);

 procedure scaricaDettLiquidMensile(aLiquid liquid_cori%rowtype, aMese NUMBER, aTSNow date,aUser varchar2);
 procedure scaricaDettNoImpostaMensile(aEs NUMBER, aMese NUMBER, aTSNow date, aUser varchar2);
 procedure aggregaDettLiquidMensile(aEs number, aMese NUMBER, aTSNow date,aUser varchar2);

 Procedure stralcioMensile(aEs number, aMese NUMBER, aUser varchar2);
END;
