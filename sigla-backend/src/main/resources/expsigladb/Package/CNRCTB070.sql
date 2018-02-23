--------------------------------------------------------
--  DDL for Package CNRCTB070
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB070" as
--
-- CNRCTB070 - Package di mappatura tabelle per la gestione delle VARIAZIONE AL PIANO DI GESTIONE
-- Date: 28/01/2003
-- Version: 1.3
--
-- Dependency:
--
-- History:
--
-- Date: 17/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/01/2002
-- Version: 1.1
-- Correzione: la costante ORIGINE_VARIAZIONE vale 'PDV', non 'VAR'
--
-- Date: 28/01/2003
-- Version: 1.2
-- Aggiunto lo stato sulla testata della variazione
--
-- Date: 28/01/2003
-- Version: 1.3
-- Fix hist.
--
-- Constants:
--
-- Origine dei dettagli PDG
--
-- Origine diretta imputazione utente
ORIGINE_VARIAZIONE CONSTANT VARCHAR2(3):='PDV';
--
-- Stati del piano di gestione per la fase di variazioni
--
STATO_PDG_MODIFICATO CONSTANT VARCHAR2(5):='M';
STATO_PDG_APERTURA_PER_VAR CONSTANT VARCHAR2(5):='G';
STATO_PDG_PRECHIUSURA_PER_VAR CONSTANT VARCHAR2(5):='H';
--
-- Stati del pdg_aggregato per la fase di variazioni
--
STATO_AGGREGATO_MODIFICATO CONSTANT VARCHAR2(5):='M';
STATO_AGGREGATO_ESAMINATO CONSTANT VARCHAR2(5):='E';

--
-- Stati variazione pdg
--
VARIAZIONE_APERTA CONSTANT CHAR(1):='A';
VARIAZIONE_CHIUSA CONSTANT CHAR(1):='C';
--
-- Functions e Procedures:
--
-- Controlla che il cdr specificato sia valido nell'esercizio specificato
-- ed titolare di un pdg aggregato (cdr di 1^ livello (non ente) o responsabile di
-- area
 function isCdrValidoPerAggregato(aEs number,aCdCdr varchar2) return boolean;

-- Wrappers generati automaticamente:
 procedure ins_PDG_PREVENTIVO_VAR (aDest PDG_PREVENTIVO_VAR%rowtype);
 procedure ins_PDG_PREVENTIVO_ETR_VAR (aDest PDG_PREVENTIVO_ETR_VAR%rowtype);
 procedure ins_PDG_PREVENTIVO_SPE_VAR (aDest PDG_PREVENTIVO_SPE_VAR%rowtype);
 procedure ins_PDG_AGGREGATO_ETR_VAR (aDest PDG_AGGREGATO_ETR_VAR%rowtype);
 procedure ins_PDG_AGGREGATO_SPE_VAR (aDest PDG_AGGREGATO_SPE_VAR%rowtype);
end;
