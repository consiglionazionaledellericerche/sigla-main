CREATE OR REPLACE package IBMCLS001 as
--
-- IBMCLS001 - Package di utilit? gestione cluster BFRAME
-- Date: 07/09/2002
-- Version: 1.0
--
-- Dependency:
--
-- History:
--
-- Date: 07/09/2002
-- Version: 1.0
-- Creazione
--
-- Constants:
--
-- Functions e Procedures:
 function getBestUrl(aClusterUrl varchar2) return varchar2;
end;
/


CREATE OR REPLACE package body IBMCLS001 is
 function getBestUrl(aClusterUrl varchar2) return varchar2 is
  aBestServerUrl varchar2(500);
  alfa number(15,2);
  beta number(15,2);
  maxDelta number(15,2);
 begin
  aBestServerUrl:='';
  maxDelta:=-1000000;
  for aCT in (select * from v_bframe_cluster where
          cluster_url = aClusterUrl) loop
   if aCT.tot_weight = 0 then
    return '';
   end if;
   alfa:=aCT.weight/aCT.tot_weight;
   if aCT.tot_load = 0 then
    beta:=0;
   else
    beta:=aCT.load/aCT.tot_load;
   end if;
   if alfa-beta >= maxDelta then
    maxDelta:=alfa-beta;
	aBestServerUrl:=aCT.appserver_url;
   end if;
  end loop;
  return aBestServerUrl;
 end;
end;
/


