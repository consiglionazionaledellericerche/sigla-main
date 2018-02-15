--------------------------------------------------------
--  DDL for Package IBMUTL020
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL020" as
--
-- IBMUTL020 - Package di utilita per protocollo VSX
-- Date: 07/06/2002
-- Version: 1.0
--
-- History:
--
-- Date: 07/06/2002
-- Version: 1.0
-- Creazione
--
--
-- Functions & Procedures:

-- Ritorna il progressivo da utilizzare per la chiamata VSX

 function vsx_get_pg_call return number;
end;
