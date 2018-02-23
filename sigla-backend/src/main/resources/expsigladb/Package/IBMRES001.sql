--------------------------------------------------------
--  DDL for Package IBMRES001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMRES001" is
--
-- IBMRES001 - Package di servizio RISERVATO per la creazione di save set e il loro restore
-- Date: 25/03/2002
-- Version: 1.0
--
-- Procedure e Funzioni di servizio di ausilio per operazioni di tipo DDL
--
-- !!!!!!WARNING!!!!!!
--
-- E' NECESSARIO EFFETTUARE IL GRANT ESPLICITO SEGUENTE ALL'UTENTE CHE UTILIZZA TALE PACKAGE:
-- grant create table to <schema owner>;
--
-- Le procedure di questo package creano fotografie dello schema utente a livello di dati e permettono il restore di tali
-- fotografie.
--
-- !!!!!!****!!!!!!
--
-- Prima di utilizzare le procedure SI CONSIGLIA VIVAMENTE DI ESEGUIRE UN EXPORT DELLA BASE DATI!!!!
--
-- History:
-- Date: 25/03/2002
-- Version: 1.0
-- Creazione
--
-- Constants:

TEMP_PREFIX CONSTANT char(1) := 'Y';
BKUP_TEMP_PREFIX CONSTANT char(1) := 'W';
DOLLARO CONSTANT char(1) := '$';

-- Functions & Procedures:

-- Crea una fotografica della base dati dell'utente collegato targando ogni tabella temporanea con Y<aNumImmagine>$<nome tabella>
 procedure creaFoto(aNumImmagine number);
 procedure creaFotoBackup(aNumImmagine number);

-- Elimina una foto precedentemente effettuata
 procedure eliminaFoto(aNumImmagine number);
 procedure eliminaFotoBackup(aNumImmagine number);

-- Restore di una foto precedentemente effettuata
 procedure restoreFoto(aNumImmagine number);

end;
