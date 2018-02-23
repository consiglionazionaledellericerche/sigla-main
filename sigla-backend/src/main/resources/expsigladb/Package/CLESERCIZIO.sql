--------------------------------------------------------
--  DDL for Package CLESERCIZIO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CLESERCIZIO" AS
  --
  -- CLESERCIZIO- Package di gestione inserimenti nella tabella di Log :L_ESERCIZIO
  -- Date: 19/03/2004
  -- Version: 1.0
  --
  -- Dependency:
  --
  -- History:
  --
  -- Date: 19/03/2004
  -- Version: 1.0
  -- Creazione
  --
  -- Constants:
  --
  -- Functions e Procedures:
  --
  -- Pre-post-name: Inserimento di una riga nella tabella di LOG : 'L_ESERCIZIO'
  --
  -- pre: esistenza della tabella di LOG : 'L_ESERCIZIO'
  -- post: Viene inserita una riga nella tabella di LOG,
  --
  -- parametri:
  --   aL_ESERCIZIO-> Riga da inserire
  procedure INSERTRIGA(aTbName varchar2, aL_ESERCIZIO L_ESERCIZIO%rowtype);
  --
  -- Pre-post-name: aggiornamento della tabella di registro dei LOG : LOG_REGISTRY
  --
  -- pre: esistenza della tabella di LOG_REGISTRY
  -- post: aggiornamento della tabella di LOG_REGISTRY per lo stato della
  --       tabella L_ESERCIZIO
  --
  -- parametri:
  --   aTbName -> Nome della tabella sorgente
  --   aTipoTrg -> Tipologia del trigger che ha scatenato la modifica
  --            'I' -> Trigger di Inserimento
  --            'D' -> Trigger di Cancellazione
  --            'U' -> Trigger di Aggiornamento
  --   aUser -> Utente che ha scatenato la modifica
  procedure updateRegistry(aTbName varchar2, aTipoTrg char, aUser varchar2 );
  --
  -- Pre-post-name: attivazione del trigger sulla tabella :ESERCIZIO
  --
  -- pre: Esistenza del trigger in esame, sulla tabella ESERCIZIO, e stato del trigger in esame = Disable
  -- post: attivazione del trigger in esame sulla tabella ESERCIZIO
  --
  -- parametri:
  --   aNomeTrg -> Nome del trigger che vogliamo attivare
  procedure attivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 ) ;
  --
  -- Pre-post-name: disattivazione del trigger sulla tabella :ESERCIZIO
  --
  -- pre: Esistenza del trigger in esame, sulla tabella ESERCIZIO, e stato del trigger in esame = Enable
  -- post: disattivazione del trigger in esame sulla tabella ESERCIZIO
  --
  -- parametri:
  --   aNomeTrg -> Nome del trigger che vogliamo disattivare
  procedure disattivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 );
end;
