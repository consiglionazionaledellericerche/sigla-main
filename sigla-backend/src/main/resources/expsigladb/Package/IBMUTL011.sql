--------------------------------------------------------
--  DDL for Package IBMUTL011
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL011" as
--
-- IBMUTL011 - Package per il controllo del logging via IBMLOG a livello di istanza
-- Date: 18/05/2001
-- Version: 1.0
--
-- History:
--
-- Date: 18/05/2001
-- Version: 1.0
-- Creazione
--
-- Constants:

-- Functions & Procedures:

 function isLogEnabled return boolean;
end;
