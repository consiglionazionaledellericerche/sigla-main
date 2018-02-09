CREATE OR REPLACE TRIGGER AU_PDG_AGGREGATO
AFTER UPDATE
on PDG_AGGREGATO
for each row
declare
 aCDR cdr%rowtype;
 aUO unita_organizzativa%rowtype;
 aPdgAggregato pdg_aggregato%rowtype;
begin
--
-- Trigger attivato su aggiornamento della tabella PDG_AGGREGATO
--
-- Date: 29/01/2003
-- Version: 1.6
--
-- Dependency: CNRCTB070, CNRCTB075
--
-- History:
--
-- Date: 17/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 30/12/2002
-- Version: 1.1
-- Introduzione metodo speciale per gestione ribaltamenti su area per variazioni a piano di gestione
-- Tale metodo viene invocato solo per chiusure in B da stati superiori a B del pdg aggregato
--
-- Date: 17/01/2003
-- Version: 1.2
-- Correzione per errore: la creazione dello storico dell'aggregato va fatta solo se
-- si passa in stato B.
--
-- Date: 24/01/2003
-- Version: 1.3
-- Correzione: non copiavo STATO in aPdgAggregato; inoltre effettuavo un test sullo stato
-- inutile (gi? fatto dall'if pi? esterno)
--
-- Date: 27/01/2003
-- Version: 1.4
-- Controllo di non reiterazione della chiusura principale A->B
--
-- Date: 28/01/2003
-- Version: 1.5
-- Modifica per creare testata di pdg_preventivo_var su passaggio di aggregato da B a M
-- invece che su passaggio di preventivo da M a F.
--
-- Date: 29/01/2003
-- Version: 1.6
-- Aggiunta chiusura del frame di variazioni
--
-- Gestione dello storico delle variazioni al pdg aggregato
-- Body:
--
-- Copio :new in aPdgAggregato
     aPdgAggregato.ESERCIZIO:=:new.ESERCIZIO;
     aPdgAggregato.CD_CENTRO_RESPONSABILITA:=:new.CD_CENTRO_RESPONSABILITA;
     aPdgAggregato.STATO:=:new.STATO;
     aPdgAggregato.DACR:=:new.DACR;
     aPdgAggregato.UTCR:=:new.UTCR;
     aPdgAggregato.DUVA:=:new.DUVA;
     aPdgAggregato.UTUV:=:new.UTUV;
     aPdgAggregato.PG_VER_REC:=:new.PG_VER_REC;
 if (:old.stato != :new.stato and :new.stato = CNRCTB050.STATO_AGGREGATO_FINALE) then
  if (CNRCTB070.ISCDRVALIDOPERAGGREGATO(aPdgAggregato.esercizio,aPdgAggregato.cd_centro_responsabilita)) then
		  -- Ribaltamento su aree SOLO se il nuovo stato aggregato ? diverso da 'A' (iniziale)
      if :old.stato != CNRCTB050.STATO_AGGREGATO_INIZIALE then
				CNRCTB053.RIBALTASUAREAPDGVAR(aPdgAggregato.esercizio,aPdgAggregato.cd_centro_responsabilita,aPdgAggregato.utuv);
      else -- Se il vecchio stato era quello iniziale
       declare
	    aPdgVar pdg_preventivo_var%rowtype;
	   begin
	    select * into aPdgVar from pdg_preventivo_var where
		        esercizio = aPdgAggregato.ESERCIZIO
			and cd_centro_responsabilita = aPdgAggregato.cd_centro_responsabilita
			and pg_variazione_pdg = 0;
		IBMERR001.RAISE_ERR_GENERICO('E'' gi? stata effettuata la prima chiusura dell''aggregato dallo stato iniziale');
       exception when NO_DATA_FOUND then
        null;
	   end;
	  end if;
	  -- Creazione storico dettagli anche ad ogni passaggio in B (Targato pg_variazione=0)
	  CNRCTB075.GENERAFRAMEVARPDGAGGREGATO(aPdgAggregato);

	  -- Chiude il frame di variazioni se il vecchio stato non ? quello iniziale
      if :old.stato != CNRCTB050.STATO_AGGREGATO_INIZIALE then
	   CNRCTB075.CHIUDEFRAMEVARPDG(aPdgAggregato);
      end if;
  end if;
 elsif (:old.stato = CNRCTB050.STATO_AGGREGATO_FINALE and :new.stato = CNRCTB070.STATO_AGGREGATO_MODIFICATO) then
 		CNRCTB075.APREFRAMEVARPDG(aPdgAggregato);
 end if;
end;
/


