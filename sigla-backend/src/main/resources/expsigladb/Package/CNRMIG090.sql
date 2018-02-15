--------------------------------------------------------
--  DDL for Package CNRMIG090
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMIG090" as
--
-- CNRMIG090 - Package di migrazione delle anagrafiche
--
-- Date: 03/02/2002
-- Version: 1.11
--
-- Dependency:
--
-- History:
--
-- Date: 22/11/2002
-- Version: 1.0
-- Creazione
--
-- Date: 26/11/2002
-- Version: 1.1
-- Corretto inserimento del terzo (lTerzo.CAP_COMUNE_SEDE := lComuneSede.cd_cap)
--
-- Date: 02/12/2002
-- Version: 1.2
-- Corretto ricerca comune di default
--
-- Date: 03/12/2002
-- Version: 1.3
-- Corretta il mwetodo di ricera per anagrafico
--
-- Date: 04/12/2002
-- Version: 1.4
-- Corretta il metodo di ricera per anagrafico parte 2
--
-- Date: 06/12/2002
-- Version: 1.5
-- Corretta il metodo di ricera per anagrafico parte 3
--
-- Date: 09/12/2002
-- Version: 1.6
-- Inserita valorizzazione default per intestazione banca
--
-- Date: 16/12/2002
-- Version: 1.7
-- Modificata la ricerca del comune di nascita,
-- inserendo un ulteriore ricerca per codice catastale estratto dal Codice fiscale
--
-- Date: 16/12/2002
-- Version: 1.8
-- Corretta la ricerca del comune di nascita per codice catastale
--
-- Date: 17/12/2002
-- Version: 1.9
-- Inserito exception no_data_found in ricerca del comune di nascita per codice catastale
--
-- Date: 18/12/2002
-- Version: 1.10
-- Inserita ORDER BY nella query di lettura sulla tabella CNR_ANAGRAFICO
-- in modo da rispettare l'ordine di inserimento definito dal timestamp
-- della tabella.
--
-- Date: 03/02/2003
-- Version: 1.11
-- Gestione della eccezione su i dati di pagamento e bancari non trovati
--
-- Constants:
--
cgUtente constant varchar2(20) :='$$$$$MIGRAZIONE$$$$$';
cgData constant date :=sysdate;
gPgLog number;
-- Functions e Procedures:
--
 procedure caricaAnagrafica;

 procedure creaAnagrafico(aAnag cnr_anagrafico%rowtype);

 procedure creaTerzi(aAnagSci cnr_anagrafico%rowtype,aAnagrafico anagrafico%rowtype, aPgLog number default 0);

 procedure creaRecapiti(aTerzo terzo%rowtype, aTerzoSci cnr_terzo%rowtype);

 procedure creaPagamento(aTerzoCir terzo%rowtype, aTerzoSci cnr_terzo%rowtype);

 procedure creaBanca(aTerzo terzo%rowtype, aTerzoSCI cnr_terzo%rowtype);

 function chkPresenzaAnag(aAnag cnr_anagrafico%rowtype, aAnagrafico in out anagrafico%rowtype) return boolean;

 procedure getComuneNascita(aAnag cnr_anagrafico%rowtype,  aComuneNascita IN OUT comune%rowtype);

 procedure getComuneNascitaPerCod (aAnag cnr_anagrafico%rowtype,  aComuneNascitaPerCod IN OUT comune%rowtype);

 procedure getComuneFiscale(aAnag cnr_anagrafico%rowtype,  aComuneFiscale IN OUT comune%rowtype);

 procedure getComuneSede(aTerzoSCI cnr_terzo%rowtype,  aComuneSede IN OUT comune%rowtype);

 procedure getNazionalita(aAnag cnr_anagrafico%rowtype,  aNazionalita IN OUT nazione%rowtype);

 procedure INS_ANAGRAFICO(aAnagrafico anagrafico%rowtype);

 procedure INS_TERZO(aTerzo Terzo%rowtype);

 procedure INS_BANCA (aBanca banca%rowtype);

 procedure INS_PAGAMENTO (aPagamento modalita_pagamento%rowtype);

 procedure INS_RECAPITO (aRecapito telefono%rowtype);
end;
