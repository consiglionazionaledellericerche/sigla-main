CREATE OR REPLACE FUNCTION GETPGDISTINTAPREC
--==================================================================================================
--
-- Date: 16/04/2003
-- Version: 1.0
--
-- Ritorna il numero progressivo di una eventuale precedente distinta cui
-- il documento contabile era stato inviato (torna 0 se non esiste)
--
-- History:
--
-- Date: 16/04/2003
-- Version: 1.0
-- Creazione funzione
--
-- Body:
--
--==================================================================================================
   (aCd_cds varchar2
   ,aEs number
   ,aCd_uo varchar2
   ,aPg_distinta number
   ,aTipo_doc varchar2
   ,aCds_origine varchar2
   ,aPg_doc number) RETURN number IS

   aPg number(10);

BEGIN
	 Declare
	    tipo	varchar2(20);
	    tipo_sac    varchar2(20);
	 Begin
	    --SE IL CDS E' DI TIPO SAC, LA UO PUO' ESSERE QUALSIASI
	    Select CD_TIPO_UNITA
	    Into tipo
	    From unita_organizzativa
	    Where cd_unita_organizzativa = aCd_cds;

	    tipo_sac := CNRCTB020.TIPO_SAC;

	 	  if aTipo_doc = 'M' then
		 	  select max(dc.PG_DISTINTA) into aPg
			  from distinta_cassiere dc
			  where dc.CD_CDS 		          = aCd_cds
			    and dc.ESERCIZIO 	          = aEs
			        And dc.CD_UNITA_ORGANIZZATIVA = Decode(tipo,tipo_sac,dc.CD_UNITA_ORGANIZZATIVA, aCd_uo)
				--and dc.CD_UNITA_ORGANIZZATIVA = aCd_uo
				and dc.PG_DISTINTA 			  < aPg_distinta
				and exists   (select 1
							  from distinta_cassiere_det dcd
							  where dcd.CD_CDS 			 	   = dc.CD_CDS
							    and dcd.ESERCIZIO  			   = dc.ESERCIZIO
								and dcd.CD_UNITA_ORGANIZZATIVA = dc.CD_UNITA_ORGANIZZATIVA
								and dcd.PG_DISTINTA			   = dc.PG_DISTINTA
								and dcd.cd_cds_origine      = aCds_origine
								and dcd.PG_MANDATO 		   	   = aPg_doc);
		  else
		 	  select max(dc.PG_DISTINTA) into aPg
			  from distinta_cassiere dc
			  where dc.CD_CDS 		          = aCd_cds
			    and dc.ESERCIZIO 	          = aEs
			        And dc.CD_UNITA_ORGANIZZATIVA = Decode(tipo,tipo_sac,dc.CD_UNITA_ORGANIZZATIVA, aCd_uo)
				--and dc.CD_UNITA_ORGANIZZATIVA = aCd_uo
				and dc.PG_DISTINTA 			  < aPg_distinta
				and exists   (select 1
							  from distinta_cassiere_det dcd
							  where dcd.CD_CDS 			 	   = dc.CD_CDS
							    and dcd.ESERCIZIO  			   = dc.ESERCIZIO
								and dcd.CD_UNITA_ORGANIZZATIVA = dc.CD_UNITA_ORGANIZZATIVA
								and dcd.PG_DISTINTA			   = dc.PG_DISTINTA
							  and dcd.cd_cds_origine      = aCds_origine
								and dcd.PG_REVERSALE 		   = aPg_doc);
		  end if;

	 exception when NO_DATA_FOUND then
			  aPg := 0;
	 end;

	 return aPg;

END GETPGDISTINTAPREC;
/


