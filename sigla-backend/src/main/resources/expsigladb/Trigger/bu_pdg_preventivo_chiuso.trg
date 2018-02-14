CREATE OR REPLACE TRIGGER BU_PDG_PREVENTIVO_CHIUSO
  BEFORE UPDATE of stato
  on pdg_preventivo
  for each row
DISABLE
begin
--
-- Trigger di attivato sulla modifica dello stato dei piani di gestione
--
-- Date: 31/03/2005
-- Version: 1.0
--
-- Dependency: JAVA
--
-- History:
--
-- Date: 31/03/2005
-- Version: 1.0
-- Creazione
--
-- Body:
--
    if :old.stato = 'C' then
        declare
            wRec NUMBER;
        begin
            select 1 into wRec
            from   utente
            where  cd_utente = :New.utuv
              and cd_gestore = 'CNRTUTTO';
        exception
            when no_data_found Then
                declare
                    wCdr varchar2(1000);
                begin
                    select CD_CENTRO_RESPONSABILITA into wCdr
                    from   cdr
                    where  cdr.CD_CENTRO_RESPONSABILITA = :old.CD_CENTRO_RESPONSABILITA
                      and (cdr.LIVELLO = '1' or cdr.cd_unita_organizzativa in
                           (select CD_UNITA_ORGANIZZATIVA
                            from  unita_organizzativa
                            where unita_organizzativa.CD_TIPO_UNITA = 'AREA'));
                    IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione risulta chiuso. Impossibile la modifica.');
                exception
                    when no_data_found then null;
                end;
            end;
    end if;
end;
/


