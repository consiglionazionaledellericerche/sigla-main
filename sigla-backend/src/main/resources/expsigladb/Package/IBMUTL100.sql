--------------------------------------------------------
--  DDL for Package IBMUTL100
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL100" is
--
-- IBMUTL100 - Package di servizio per attivita DDL
-- >>>>> Date: 27/01/2002
-- >>>>> Version: 1.5
--
-- Procedure e Funzioni di servizio di ausilio per operazioni di tipo DDL
--
-- History:
-- >>>>> Date: 15/09/2001
-- >>>>> Version: 1.1
-- aggiunte funzioni di servizio per la generazione di ddl
--
-- >>>>> Date: 05/10/2001
-- >>>>> Version: 1.2
-- aggiunto metodo di creazione procedura di copia dati interesercizio
--
-- >>>>> Date: 09/10/2001
-- >>>>> Version: 1.3
-- aggiunto metodo per generazione ddl storico
-- eliminato l'owner come parametro da tutte le procedure, perche viene utilizzato
-- lo user corrente
--
-- >>>>> Date: 27/11/2001
-- >>>>> Version: 1.4
-- Aggiunti metodi di costruzione alias colonne e sum colonne
--
-- >>>>> Date: 27/01/2002
-- >>>>> Version: 1.5
-- Aggiunto metodo di creazione insert-select statement
--
-- Constants:

-- Functions :

-- Ritorna una array di nomi di colonne della primary key della tabella aTabName sotto lo schema aOwner

 function getPkCols(aTableName varchar2) return IBMCST001.vcarray;

-- Copia i dati dalla tabella origine alla destinazione sotto lo schema aOwner.
-- Attenzione solo le colonne in comune tra le due tabelle vengono portate da una
-- tabella all'altra

 procedure copiaDatiEter(destinazione varchar2, origine varchar2);

-- stampa su dbms_output l'insert procedure per la tabella

 procedure creaInsertProcStatement(destinazione varchar2);

 -- Metodo di creazione procedura di copia dati interesercizio

 procedure creaCpIntEserProcStatement(destinazione varchar2);

-- Estrae i nomi delle colonne della tabella destinazione nella seguente forma: <colname> <aPrefix><colname>,
-- per gestire l'alias di colonne nell viste

 procedure creaAliasColonne(destinazione varchar2, aPrefix varchar2);

-- replaceOriginWith != null viene sostituito a nome colonna
-- aColPrefix prefisso dato al nome di colonna
 procedure creaAliasColonne(destinazione varchar2, aColPrefix varchar2, replaceOriginWith varchar2, aAliasPrefix varchar2);

-- Estrae i nomi delle colonne della tabella destinazione nella seguente forma: sum(<colname>),
-- per gestire le somme nelle viste

 procedure creaSumColonne(destinazione varchar2);


-- stampa su dbms_output gli statements di assegnamento (aRT nome del rowtype)

 procedure creaAssignmentStatement(destinazione varchar2, aRT varchar2);

 procedure creaAssignmentStatement(destinazione varchar2, aRT varchar2, aRO varchar2);

-- Estrae le DDL di creazione delle FK di cui tabName e parent nello schema aOwner

 procedure extractFKStatements(tabName varchar2);

 -- Estrae le DDL di creazione della tabella di storico

 procedure generaStoricoPer(destinazione varchar2);

 -- Genera l'insert procedure nella tabella storico a partire
 -- dal rowtype della tabella di partenza e dato il pg e desc di storico

 procedure creaInsertStoricoProcStatement(destinazione varchar2);

 -- Crea insert - select statment

 procedure creaInsertSelectStatement(destinazione varchar2, origine varchar2);

end;
