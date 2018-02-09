CREATE OR REPLACE FUNCTION calcolaAltreBorse
--==================================================================================================
--
-- Date: 23/01/2003
-- Version: 1.2
--
-- Ritorna l'importo complessivo lordo percipiente effettivamente pagato
-- nel corso dell'anno di stampa del prospetto per rate di minicarriera di
-- tipo borsa di studio per lo stesso terzo
--
--
-- History:
--
-- Date: 06/12/2002
-- Version: 1.0
-- Creazione funzione
--
-- Date: 11/12/2002
-- Version: 1.1
-- Esclusione mandati annullati
--
-- Date: 23/01/2003
-- Version: 1.2
-- Gestione eccezione per mandato non trovato
--
-- Body:
--
--==================================================================================================
   (aEs1 NUMBER
   ,aCdTerzo NUMBER
   ,aCdCds VARCHAR2
   ,aCdUo VARCHAR2
   ,aEs2 NUMBER
   ,aPgMini NUMBER) RETURN NUMBER IS;

   imAltreBorse NUMBER(15,2);
   aImMandato NUMBER(15,2);

BEGIN
	 imAltreBorse:=0;

	 for aComp in (select * from compenso com
	 	 	   	  		  where com.CD_TERZO  		   		 = aCdTerzo
						    and com.FL_COMPENSO_MINICARRIERA = 'Y'
						    and com.TI_ASSOCIATO_MANREV 	 = 'T'
							and com.CD_TIPO_RAPPORTO in (select cd_tipo_rapporto from tipo_rapporto
													 			where ti_dipendente_altro = 'A'
																  and ti_rapporto_altro = 'B')
							and not exists (select 1 from minicarriera_rata mrata
										   		   where mrata.CD_CDS 			 	  = aCdCds
												     and mrata.CD_UNITA_ORGANIZZATIVA = aCdUo
													 and mrata.ESERCIZIO			  = aEs2
													 and mrata.PG_MINICARRIERA		  = aPgMini
													 and mrata.CD_CDS_COMPENSO		  = com.CD_CDS
													 and mrata.CD_UO_COMPENSO		  = com.CD_UNITA_ORGANIZZATIVA
													 and mrata.ESERCIZIO_COMPENSO	  = com.ESERCIZIO
													 and mrata.PG_COMPENSO			  = com.PG_COMPENSO)
				  ) loop

		begin
			select mriga.IM_MANDATO_RIGA into aImMandato
			from mandato_riga mriga
			where mriga.ESERCIZIO  		 	  = aEs1
			  and mriga.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO'
			  and mriga.CD_CDS_DOC_AMM 		  = aComp.CD_CDS
			  and mriga.CD_UO_DOC_AMM  		  = aComp.CD_UNITA_ORGANIZZATIVA
			  and mriga.ESERCIZIO	   		  = aComp.ESERCIZIO
			  and mriga.PG_DOC_AMM	   		  = aComp.PG_COMPENSO
			  and mriga.STATO				  <> 'A';
		exception when no_data_found then
			aImMandato:= 0;
		end;

		imAltreBorse := imAltreBorse + aImMandato;

	 end loop;

	 RETURN imAltreBorse;

END calcolaAltreBorse;
/


