--------------------------------------------------------
--  DDL for Package CLCONFIGURAZIONE_CNR
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CLCONFIGURAZIONE_CNR" AS
  --
  -- CLCONFIGURAZIONE_CNR- Package di gestione inserimenti nella tabella di Log :L_CONFIGURAZIONE_CNR
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
  -- Pre-post-name: Inserimento di una riga nella tabella di LOG : 'L_CONFIGURAZIONE_CNR'
  --
  -- pre: esistenza della tabella di LOG : 'L_CONFIGURAZIONE_CNR'
  -- post: Viene inserita una riga nella tabella di LOG,
  --
  -- parametri:
  --   aL_CONFIGURAZIONE_CNR-> Riga da inserire
  procedure INSERTRIGA(aTbName varchar2, aL_CONFIGURAZIONE_CNR L_CONFIGURAZIONE_CNR%rowtype);
  --
  -- Pre-post-name: aggiornamento della tabella di registro dei LOG : LOG_REGISTRY
  --
  -- pre: esistenza della tabella di LOG_REGISTRY
  -- post: aggiornamento della tabella di LOG_REGISTRY per lo stato della
  --       tabella L_CONFIGURAZIONE_CNR
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
  -- Pre-post-name: attivazione del trigger sulla tabella :CONFIGURAZIONE_CNR
  --
  -- pre: Esistenza del trigger in esame, sulla tabella CONFIGURAZIONE_CNR, e stato del trigger in esame = Disable
  -- post: attivazione del trigger in esame sulla tabella CONFIGURAZIONE_CNR
  --
  -- parametri:
  --   aNomeTrg -> Nome del trigger che vogliamo attivare
  procedure attivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 ) ;
  --
  -- Pre-post-name: disattivazione del trigger sulla tabella :CONFIGURAZIONE_CNR
  --
  -- pre: Esistenza del trigger in esame, sulla tabella CONFIGURAZIONE_CNR, e stato del trigger in esame = Enable
  -- post: disattivazione del trigger in esame sulla tabella CONFIGURAZIONE_CNR
  --
  -- parametri:
  --   aNomeTrg -> Nome del trigger che vogliamo disattivare
  procedure disattivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 );
end;
