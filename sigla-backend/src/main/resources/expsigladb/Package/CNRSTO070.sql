--------------------------------------------------------
--  DDL for Package CNRSTO070
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRSTO070" as
--
-- CNRSTO070 - Package per la gestione dello storico degli INCARICHI
-- Date: 28/10/2006
-- Version: 1.0
--
--
-- Dependency:
--
-- History:
-- Date: 28/10/2006
-- Version: 1.0
-- Creazione
--
-- Constants:


-- Functions Procedures:
 -- Procedure di inserimento nelle tabella di storico dell'obbligazione
 -- Viene chiamata da trigger BU_OBBLIGAZIONE

 procedure sto_INCARICHI_RICHIESTA (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_RICHIESTA%rowtype);

 procedure sto_INCARICHI_PROCEDURA (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_PROCEDURA%rowtype);
 procedure sto_INCARICHI_PROCEDURA_ANNO (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_PROCEDURA_ANNO%rowtype);
 procedure sto_INCARICHI_PROCEDURA_ARCH (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_PROCEDURA_ARCHIVIO%rowtype);

 procedure sto_INCARICHI_REPERTORIO (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_REPERTORIO%rowtype);
 procedure sto_INCARICHI_REPERTORIO_ANNO (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_REPERTORIO_ANNO%rowtype);
 procedure sto_INCARICHI_REPERTORIO_ARCH (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_REPERTORIO_ARCHIVIO%rowtype);

end;
