CREATE OR REPLACE FUNCTION GETSTATODOCCONT
--==================================================================================================
--
-- Date: 17/04/2003
-- Version: 1.3
--
-- Ritorna lo stato di un documento contabile (mandato o reversale) inserito in distinta
-- al fine della stampa della distinta cassiere
--
-- History:
--
-- Date: 31/03/2003
-- Version: 1.0
-- Creazione funzione
--
-- Date: 31/03/2003
-- Version: 1.1
-- Corretta select iniziale
-- Corretta determinazione stato
--
-- Date: 16/04/2003
-- Version: 1.2
-- Uso della function GETPGDISTINTAPREC
--
-- Date: 17/04/2003
-- Version: 1.3
-- Ritorna lo stato A annullato o non annullato NA di un documento contabile
-- (1. anche i documenti annullati e mai trasmessi in precedenza possono
--     essere inseriti in distinta
--  2. non ? possibile distiguere fra emesso o pagato in quanto la dt_pagamento/incasso
--     non ? un timestamp)
--
-- Body:
--
--==================================================================================================
   (aCd_cds varchar2
   ,aEs number
   ,aCd_uo varchar2
   ,aPg_distinta number
   ,aTipo_doc varchar2
    ,aCd_cds_origine varchar2
   ,aPg_doc number
   ,aDt_ann date
   ,aDt_trasm date) RETURN varchar2 IS

   aPg number(10);
   stato char(2);

BEGIN

	 aPg := getpgdistintaprec(aCd_cds,aEs,aCd_uo,aPg_distinta,aTipo_doc,aCd_cds_origine,aPg_doc);

	 if aPg <> 0 then
		stato := 'A';
	 else
	 	 if aDt_ann is null then
		 	stato := 'NA';
		 else
		 	 if aDt_ann < aDt_trasm then
			 	stato := 'A';
			 else
			 	stato := 'NA';
			 end if;
		 end if;
	 end if;

	 RETURN stato;

END GETSTATODOCCONT;
/


