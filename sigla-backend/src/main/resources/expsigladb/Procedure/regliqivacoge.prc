CREATE OR REPLACE procedure         RegLiqIvaCOGE(aEsercizio number, aUser varchar2) is
begin
 for aLiqIva in (select distinct cd_cds,cd_unita_organizzativa,tipo_liquidazione,dt_inizio,dt_fine from liquidazione_iva
              where
              esercizio = aEsercizio and
                       report_id = 0
                          ) loop
      cnrctb205.regLiqIvaMensileCOGE(aEsercizio,aLiqIva.cd_cds,aLiqIva.cd_unita_organizzativa,aLiqIva.tipo_liquidazione,aLiqIva.dt_inizio,aLiqIva.dt_fine,aUser);
  end loop;
end;
/


