CREATE OR REPLACE PROCEDURE SPG_MAN_REV_ASS
--
-- Date: 26/02/2003
-- Version: 1.0
--
-- Per stampa mandato e reversali associate
--
--
-- History:
--
-- Date: 26/02/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
(
 tc2 in out IBMPRT000.t_cursore,
 aCd_cds_man in varchar2,
 aEs_man in number,
 aPg_man in number
) is
 aId number;
 i number := 0;
begin
select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;

 for aAssmr in (select * from ass_mandato_reversale assmr
 	 		    where assmr.CD_CDS_MANDATO    = aCd_cds_man
				  and assmr.ESERCIZIO_MANDATO = aEs_man
				  and assmr.PG_MANDATO		  = aPg_man) loop

	 i := i+1;

	 insert_vpg_reversale(aAssmr.CD_CDS_REVERSALE,
	 					  aAssmr.ESERCIZIO_REVERSALE,
						  aAssmr.PG_REVERSALE,
						  i,
						  aId);

 end loop;

 open tc2 for
  select * from VPG_REVERSALE where id = aId;

-- close tc2; --- ELIMINARE PER RICHIAMARE DA CR !!!

end;
/


