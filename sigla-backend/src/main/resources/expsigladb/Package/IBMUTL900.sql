--------------------------------------------------------
--  DDL for Package IBMUTL900
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL900" is
--
-- IBMUTL900 - Package di utilita generiche di sviluppo PL/SQL
-- >>>>> Date: 17/10/2001
-- >>>>> Version: 1.1
--
-- Procedure e Funzioni di servizio generale per lo sviluppo PL/SQL
--
-- History:
--
-- >>>>> Date: 17/10/2001
-- >>>>> Version: 1.1
-- fix errori
--

-- Constants:

-- Functions & Procedures:

-- Crea lo scheletro fittizio di una vista ottenuta mettendo in join tutte le tabelle elencate in
-- tabelle: la vista avra nome aName

 procedure view_builder(aName varchar2, tabelle IBMCST001.vcarray);

end;
