--------------------------------------------------------
--  DDL for Package CLBANCA
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CLBANCA" AS
  --
  -- CLBANCA- Package di gestione inserimenti nella tabella di Log :L_BANCA
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
  -- Pre-post-name: Inserimento di una riga nella tabella di LOG : 'L_BANCA'
  --
  -- pre: esistenza della tabella di LOG : 'L_BANCA'
  -- post: Viene inserita una riga nella tabella di LOG,
  --
  -- parametri:
  --   aL_BANCA-> Riga da inserire
  procedure INSERTRIGA(aTbName varchar2, aL_BANCA L_BANCA%rowtype);
  --
  -- Pre-post-name: aggiornamento della tabella di registro dei LOG : LOG_REGISTRY
  --
  -- pre: esistenza della tabella di LOG_REGISTRY
  -- post: aggiornamento della tabella di LOG_REGISTRY per lo stato della
  --       tabella L_BANCA
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
  -- Pre-post-name: attivazione del trigger sulla tabella :BANCA
  --
  -- pre: Esistenza del trigger in esame, sulla tabella BANCA, e stato del trigger in esame = Disable
  -- post: attivazione del trigger in esame sulla tabella BANCA
  --
  -- parametri:
  --   aNomeTrg -> Nome del trigger che vogliamo attivare
  procedure attivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 ) ;
  --
  -- Pre-post-name: disattivazione del trigger sulla tabella :BANCA
  --
  -- pre: Esistenza del trigger in esame, sulla tabella BANCA, e stato del trigger in esame = Enable
  -- post: disattivazione del trigger in esame sulla tabella BANCA
  --
  -- parametri:
  --   aNomeTrg -> Nome del trigger che vogliamo disattivare
  procedure disattivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 );
end;
