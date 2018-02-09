CREATE OR REPLACE TRIGGER AU_UNITA_ORGANIZZATIVA
  AFTER UPDATE
  on UNITA_ORGANIZZATIVA
  for each row
declare
aUO unita_organizzativa%rowtype;
begin
--
-- Trigger attivato su aggiornamento di UNITA_ORGANIZZATIVA
--
-- Date: 10/04/2003
-- Version: 1.2
--
-- Dependency: IBMERR001
--
-- History:
--
-- Date: 01/06/2001
-- Version: 1.0
-- Creazione
-- Date: 08/11/2001
-- Version: 1.1
-- Eliminazione esercizio da STO
-- Date: 10/04/2003
-- Version: 1.2
-- Blocco di modifica dell'area di ricerca di afferenza
--
-- Body:
--

-- Check che non venga cambiata l'AREA di ricerca impostata

 if
      :old.cd_area_ricerca is not null
  and :new.cd_area_ricerca is null
 then
  IBMERR001.RAISE_ERR_GENERICO('Non ? possibile rimuovere il collegamento con area di ricerca');
 end if;

 if
      :old.cd_area_ricerca is not null
  and :new.cd_area_ricerca is not null
  and :new.cd_area_ricerca <> :old.cd_area_ricerca
 then
  IBMERR001.RAISE_ERR_GENERICO('Non ? possibile cambiare il riferimento ad area di ricerca');
 end if;

-- Check che non cambi la PK
 if
  :old.cd_unita_organizzativa!=:new.cd_unita_organizzativa
 then
  IBMERR001.raise_no_change_pk('Unita Organizzativa');
 end if;
-- Check che non cambi il CDS di appartenenza + flag che caratterizzano l'UO + dati base
 if
     :old.cd_unita_padre!=:new.cd_unita_padre
  or :old.fl_uo_cds!=:new.fl_uo_cds
  or :old.fl_rubrica!=:new.fl_rubrica
  or :old.cd_proprio_unita!=:new.cd_proprio_unita
  or :old.cd_tipo_unita!=:new.cd_tipo_unita
 then
  IBMERR001.raise_no_change('Alcune caratteristiche dell''Unita organizzativa non sono modificabili');
 end if;
end;
/


