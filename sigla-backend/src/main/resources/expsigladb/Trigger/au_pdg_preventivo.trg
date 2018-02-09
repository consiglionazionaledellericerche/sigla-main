CREATE OR REPLACE TRIGGER AU_PDG_PREVENTIVO
AFTER UPDATE
on PDG_PREVENTIVO
for each row
declare
 aCDR cdr%rowtype;
 aUO unita_organizzativa%rowtype;
 aPdg pdg_preventivo%rowtype;
begin
--
-- Trigger attivato su aggiornamento della tabella PDG_PREVENTIVO
--
-- Date: 17/12/2002
-- Version: 1.4
--
-- Dependency: CNRSTO050
--
-- History:
--
-- Date: 01/09/2001
-- Version: 1.0
-- Creazione
-- Date: 28/09/2001
-- Version: 1.1
-- Creazione dell'agregato iniziale per CDR II livello responsabile di area
--
-- Date: 09/10/2001
-- Version: 1.2
-- Gestione dello storico
--
-- Date: 09/11/2001
-- Version: 1.3
-- ELiminazione esercizio da STO
--
-- Date: 17/12/2002
-- Version: 1.4
-- Aggiunta gestione storico variazioni al pdg
--
-- Creazione dell'aggregato di PDG iniziale per PDG di CDR di primo livello
-- Body:
--
-- Copio :new in aPdg
     aPdg.ESERCIZIO:=:new.ESERCIZIO;
     aPdg.CD_CENTRO_RESPONSABILITA:=:new.CD_CENTRO_RESPONSABILITA;
     aPdg.STATO:=:new.STATO;
     aPdg.ANNOTAZIONI:=:new.ANNOTAZIONI;
     aPdg.FL_RIBALTATO_SU_AREA:=:new.FL_RIBALTATO_SU_AREA;
     aPdg.DACR:=:new.DACR;
     aPdg.UTCR:=:new.UTCR;
     aPdg.DUVA:=:new.DUVA;
     aPdg.UTUV:=:new.UTUV;
     aPdg.PG_VER_REC:=:new.PG_VER_REC;
-- Scarico dello storico
 if :new.stato != :old.stato then
  CNRSTO050.scaricaSuStorico(CNRSTO050.descPassaggioStato(:old.stato,:new.stato),aPdg);
		 -- Se passo dallo stato M allo stato F e il cdr ? di 1^ livello o area
		 if (:old.stato = CNRCTB070.STATO_PDG_MODIFICATO and
		     :new.stato = CNRCTB050.STATO_PDG_FINALE and
				 CNRCTB070.ISCDRVALIDOPERAGGREGATO(:new.esercizio,:new.cd_centro_responsabilita)) then
				 -- Creo un nuovo frame di variazione e trasformo i dettagli di variazione in definitivi
				 CNRCTB075.GENERAFRAMEVARPDG(aPdg);
		 end if;
 end if;
end;
/


