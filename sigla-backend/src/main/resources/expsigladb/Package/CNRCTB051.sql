--------------------------------------------------------
--  DDL for Package CNRCTB051
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB051" as
--
-- CNRCTB051 - Package per la gestione del PIANO DI GESTIONE PRELIMINARE CDR
--
-- Date: 19/12/2005
-- Version: 1.2
--
-- Dependency: CNRCTB051 IBMERR 001
--
-- History:
--
-- Date: 25/10/2005
-- Version: 1.0
-- Creazione
--
-- Date: 02/12/2005
-- Version: 1.1
-- Gestito il tempo determinato
--
-- Date: 19/12/2005
-- Version: 1.1
-- Aggiunte le procedure INS_PDG_MODULO_SPESE_GEST e INS_PDG_MODULO_ENTRATE_GEST per
-- gestire il rilascio del PDGP sul PDG
--
-- Constants:
--
-- Stati del piano di gestione preliminare
--
STATO_PDGP_APERTURA CONSTANT VARCHAR2(5):='AC';
STATO_PDGP_CHIUSURA CONSTANT VARCHAR2(5):='CC';
STATO_PDGP_ADEGUAMENTO Constant VARCHAR2(5):='AD';
STATO_PDGP_APPROVATO CONSTANT VARCHAR2(5):='AP';

-- Functions e Procedures:

 -- Inserisce una riga nella tabella PDG_ESERCIZIO

 procedure ins_PDG_ESERCIZIO (aDest PDG_ESERCIZIO%rowtype);

 -- Inserisce una riga nella tabella PDG_MODULO

 procedure ins_PDG_MODULO (aDest PDG_MODULO%rowtype);

 -- Inserisce una riga nella tabella PDG_MODULO_SPESE

 procedure ins_PDG_MODULO_SPESE (aDest PDG_MODULO_SPESE%rowtype);

 -- Inserisce una riga nella tabella PDG_MODULO_ENTRATE

 procedure ins_PDG_MODULO_ENTRATE (aDest PDG_MODULO_ENTRATE%rowtype);

 -- Inserisce una riga nella tabella PDG_MODULO_COSTI

 procedure ins_PDG_MODULO_COSTI (aDest PDG_MODULO_COSTI%rowtype);

 -- Inserisce una riga nella tabella PDG_MODULO_SPESE_GEST

 procedure ins_PDG_MODULO_SPESE_GEST (aDest PDG_MODULO_SPESE_GEST%rowtype);

 -- Inserisce una riga nella tabella PDG_MODULO_ENTRATE_GEST

 Procedure ins_PDG_MODULO_ENTRATE_GEST (aDest PDG_MODULO_ENTRATE_GEST%rowtype);

 -- Inserisce una riga nella tabella PDG_MODULO_ENTRATE_GEST

 Procedure ins_PDG_VARIAZIONE (aDest PDG_VARIAZIONE%rowtype);

 Procedure ins_PDG_VARIAZIONE_RIGA_GEST (aDest PDG_VARIAZIONE_RIGA_GEST%rowtype);

 Procedure ins_ASS_VAR_STANZ_RES_CDR (aDest ASS_VAR_STANZ_RES_CDR%rowtype);

 Procedure ins_VAR_STANZ_RES_RIGA (aDest VAR_STANZ_RES_RIGA%rowtype);

 Procedure ins_VAR_STANZ_RES (aDest VAR_STANZ_RES%rowtype);

 -- Legge lockandole le righe di pdgp del cdr aCDR: se non trova le rige, solleva un'eccezione
 --
 -- aEs -> esercizio
 -- aCdCdr -> codice del centro di responsabilità

 procedure lockPdgP(aEs number, aCdCdr varchar2);

 -- Ritorna lo stato del piano del CDR aCDR
 --
 -- aEs -> esercizio
 -- aCdCdr -> codice del centro di responsabilità

 function getStato(aEs number, aCdCdr varchar2) return varchar2;

 -- Reset dei campi importo in rowtype passato

 procedure resetCampiImporto(aDett in out pdg_modulo_spese%rowtype);
 procedure resetCampiImporto(aDett in out pdg_modulo_costi%rowtype);
 procedure resetCampiImporto(aDett in out pdg_modulo_entrate%rowtype);
 procedure resetCampiImporto(aDett in out pdg_modulo_spese_gest%rowtype);

End;
