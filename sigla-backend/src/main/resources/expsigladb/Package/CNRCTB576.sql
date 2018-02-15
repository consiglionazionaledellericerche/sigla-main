--------------------------------------------------------
--  DDL for Package CNRCTB576
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB576" As
--==============================================================================
--
-- CNRCTB576 - Ribaltamento righe aperte di LIQUID_GRUPPO_CENTRO
--
-- Date: 14/07/2006
-- Version: 1.1
--
-- History:
--
-- Date: 23/12/2005
-- Version: 1.0
-- Creazione
--
-- Date: 14/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Creazione Package.
--
--==============================================================================
--
-- Vengono ribaltate solo le righe con STATO = 'I', DA_ESERCIZIO_PRECEDENTE = 'N'
-- Viene creata nel nuovo anno una PGIRO di spesa tronca in parte entrata ribaltamento di quella esistente
-- Viene creata una nuova riga in LIQUID_GRUPPO_CENTRO con ESERCIZIO = 'nuovo es.', con STATO = 'I', DA_ESERCIZIO_PRECEDENTE = 'Y'
--   e con la nuova PGIRO (Se una tale riga già esiste viene bloccata l'operazione)
-- Sulla vecchia LIQUID_GRUPPO_CENTRO cambia lo STATO e diventa 'R'= Ribaltata

 Procedure ribalta(aEs liquid_gruppo_centro.esercizio%Type,
 		   aStato liquid_gruppo_centro.stato%Type,
 		   aGruppo liquid_gruppo_centro.cd_gruppo_cr%Type,
 		   aRegione liquid_gruppo_centro.cd_regione%Type,
 		   aComune liquid_gruppo_centro.pg_comune%Type,
 		   aDaEsPrec liquid_gruppo_centro.da_esercizio_precedente%Type,
 		   aUser liquid_gruppo_centro.utcr%Type);

 Function creaGruppoCentro(New_aEs liquid_gruppo_centro.esercizio%Type,
   			     gruppo liquid_gruppo_centro.cd_gruppo_cr%Type,
   			     regione liquid_gruppo_centro.cd_regione%Type,
   			     comune liquid_gruppo_centro.pg_comune%Type,
   			     aTSNow date,
   			     aUser varchar2) return liquid_gruppo_centro%rowtype;
End;
