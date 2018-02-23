--------------------------------------------------------
--  DDL for Package IBMCST001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMCST001" as
--
-- IBMCST001 - Package di definizione costanti generali
-- >>>>> Date: 30/01/2002
-- >>>>> Version: 1.2
--
-- History:
-- Date: 08/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 25/01/2002
-- Version: 1.1
-- Introduzione costante utente di migrazione
--
-- Date: 30/01/2002
-- Version: 1.2
-- Rimosso l'underscore nei TAGS dei dati di migrazione e di test
--
-- Constants:

-- Type ARRAY da usare come parametro di appoggio

TYPE vcarray IS VARRAY(100) OF varchar2(300);

-- Markers usati per identificare i record di test

TESTRECORD_IDENTIFIER CONSTANT varchar2(20) := '$$$$$$TEST$REC$$$$$$';
TESTRECORD_PG_VER_REC constant number := 99999999;

MIGRECORD_IDENTIFIER constant varchar2(20):='$$$$$MIGRAZIONE$$$$$';

end;
