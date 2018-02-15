--------------------------------------------------------
--  DDL for Package CNRCTB585
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB585" AS
--==============================================================================
--
-- CNRCTB585 - Gestione tabelle anticipo/rimborso
--
-- Date: 19/07/2006
-- Version: 1.3
--
-- Dependency:
--
-- History:
--
-- Date: 24/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 25/06/2002
-- Version: 1.1
-- Fix errore
--
-- Date: 30/07/2002
-- Version: 1.2
-- Aggiunti dati terzo
--
-- Date: 19/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Creazione Package.
--
--==============================================================================
--
-- Constants
--

ELEMENTO_VOCE_SPECIALE CONSTANT VARCHAR2(50):='ELEMENTO_VOCE_SPECIALE';
RIMBORSO_ANTICIPI CONSTANT VARCHAR2(100):='RIMBORSO_ANTICIPO';

--
-- Functions e Procedures
--
 procedure ins_RIMBORSO (aDest RIMBORSO%rowtype);

END;
