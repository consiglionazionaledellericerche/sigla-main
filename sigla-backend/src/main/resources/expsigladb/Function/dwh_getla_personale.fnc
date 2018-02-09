CREATE OR REPLACE function DWH_getLA_Personale(in_CdCdr in varchar2)
 return varchar2 is
  aLA   linea_attivita.cd_linea_attivita%type;
 begin
  select cd_linea_Attivita
  into   aLA
  from   linea_attivita
  where  cd_centro_responsabilita = in_CdCdr and
         cd_tipo_linea_attivita = 'SAUOP';
  return aLA;
 end;
/


