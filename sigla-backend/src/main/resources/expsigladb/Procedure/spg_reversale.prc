CREATE OR REPLACE PROCEDURE         SPG_REVERSALE
--
-- Date: 03/03/2003
-- Version: 1.4
--
-- Protocollo VPG per stampa massiva di reversali
--
--
-- History:
--
-- Date: 11/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 25/02/2003
-- Version: 1.1
-- Aggiunti parametri di ingresso
--
-- Date: 26/02/2003
-- Version: 1.2
-- Corretta select iniziale
--
-- Date: 26/02/2003
-- Version: 1.3
-- Corretta gestione reversali di regolarizzazione:
-- pg_banca potrebbe essere null
--
-- Date: 03/03/2003
-- Version: 1.4
-- Uso di INSERT_VPG_REVERSALE
--
-- Body:
--
(
 aCd_cds in varchar2,
 aEs in number,
 aPg_da in number,
 aPg_a in number,
 aDt_da in varchar2,
 aDt_a in varchar2,
 aCd_terzo in varchar2
) is
 aId number;
 i number;
 aNum1 number := 0;
 aNum2 number := 0;
 aNum3 number := 0;
 aVar1 varchar2(300) := null;
 aVar2 varchar2(300) := null;
begin
 select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;
 i:=0;

 for aRev in (select * from reversale rev
 	 	   	   where ((aCd_cds =CNRCTB020.GETCDCDSENTE (rev.esercizio) and rev.CD_CDS = aCd_cds)  or
 	 	   	   rev.CD_CDS_origine         = aCd_cds )
			     and rev.ESERCIZIO		= aEs
				 and rev.PG_REVERSALE	>= aPg_da
				 and rev.PG_REVERSALE	<= aPg_a
				 and rev.DT_EMISSIONE   >= to_date(aDt_da,'YYYY/MM/DD')
				 and rev.DT_EMISSIONE	<= to_date(aDt_a,'YYYY/MM/DD')
				 and exists (select 1 from reversale_riga rriga
			  			     where rriga.CD_CDS       = rev.cd_cds
							   and rriga.ESERCIZIO    = aEs
							   and rriga.PG_REVERSALE = rev.PG_REVERSALE
							   and to_char(rriga.CD_TERZO) like aCd_terzo)) loop
	-- inizio loop 1

	i := i+1;

	insert_vpg_reversale (aRev.CD_CDS
						 ,aRev.ESERCIZIO
						 ,aRev.PG_REVERSALE
						 ,i
						 ,aId);

 end loop;  -- fine loop 1

end;
/


