--------------------------------------------------------
--  DDL for Package IBMCLS001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMCLS001" as
--
-- IBMCLS001 - Package di utilità gestione cluster BFRAME
-- Date: 07/09/2002
-- Version: 1.0
--
-- Dependency:
--
-- History:
--
-- Date: 07/09/2002
-- Version: 1.0
-- Creazione
--
-- Constants:
--
-- Functions e Procedures:
 function getBestUrl(aClusterUrl varchar2) return varchar2;
end;
