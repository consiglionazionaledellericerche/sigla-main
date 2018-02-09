CREATE OR REPLACE FUNCTION getIm_riscontrato
--==================================================================================================
--
-- Date: 12/07/2006
-- Version: 1.2
--
-- Ritorna l'importo riscontrato per accertamenti e obbligazioni
-- al fine del ribaltamento anno su anno nell'ambito della chiusura
-- contabile
--
--
-- History:
--
-- Date: 06/06/2003
-- Version: 1.0
-- Creazione funzione
--
-- Date: 06/06/2003
-- Version: 1.1
-- Non ritorna valori NULL
--
-- Date: 12/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- aggiunto il parametro di ingresso aEsOri: Esercizio Originale Impegno/Accertamento
--
--
-- Body:
--
--==================================================================================================
   (aEs    NUMBER
   ,aCdCds VARCHAR2
   ,aEsOri NUMBER
   ,aPg	   NUMBER
   ,aTiGestione VARCHAR2) RETURN NUMBER IS

   im_riscontrato number := 0;

BEGIN

	if aTiGestione = 'E' then
	   select sum(rriga.im_reversale_riga) into im_riscontrato
	   from reversale_riga rriga
	   where rriga.cd_cds  				  = aCdCds
	     and rriga.esercizio_accertamento = aEs
	     and rriga.esercizio_ori_accertamento = aEsOri
		 and rriga.PG_ACCERTAMENTO 		  = aPg
		 and exists (select 1
				 	 from reversale rev
					 where rev.CD_CDS 		 = rriga.CD_CDS
					   and rev.ESERCIZIO 	 = rriga.ESERCIZIO
					   and rev.PG_REVERSALE = rriga.PG_REVERSALE
					   and rev.STATO = 'P');
	else
		select sum(mriga.IM_MANDATO_RIGA) into im_riscontrato
		from mandato_riga mriga
		where mriga.CD_CDS	   			   = aCdCds
		  and mriga.ESERCIZIO_OBBLIGAZIONE = aEs
		  and mriga.ESERCIZIO_ORI_OBBLIGAZIONE = aEsOri
		  and mriga.PG_OBBLIGAZIONE		   = aPg
		  and exists (select 1
		  	  		  from mandato man
					  where man.CD_CDS	   = mriga.CD_CDS
					    and man.ESERCIZIO  = mriga.ESERCIZIO
						and man.PG_MANDATO = mriga.PG_MANDATO
						and man.STATO	   = 'P');
	end if;

	if im_riscontrato is null then
	   im_riscontrato := 0;
	end if;

	RETURN im_riscontrato;

END getIm_riscontrato;
/


