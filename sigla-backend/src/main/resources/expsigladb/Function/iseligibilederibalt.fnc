CREATE OR REPLACE FUNCTION isEligibileDeRibalt(aTiGestione char,aCdCdsDoc varchar2,aEsDoc number,aEsOriDoc number,aPgDoc number) RETURN CHAR IS
--==================================================================================================
--
-- Date: 12/07/2006
-- Version: 1.2
--
-- Estrazione documenti ribaltabili all esercizio precedente (obb/acc)
--
-- History:
--
-- Date: 23/07/2003
-- Version: 1.0
-- Creazione function
--
-- Date: 06/08/2003
-- Version: 1.1
-- Aggiunta documentazione
--
-- Date: 12/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- aggiunto il parametro di ingresso aEsOriDoc: Esercizio Originale Impegno/Accertamento
--
-- Body:
--
--==================================================================================================
aEsOriAcc number(4);
aEsOriObb number(4);
aPgAcc number;
aPgObb number;
isRiportato char(1);
aImRiscontrato number;
begin
-- I seguenti documenti:
-- - documenti contabili di appartenenza dell'ente
-- - documenti contabili di appartenenza del cds ma non su partita di giro
-- sono potenzialmente riportabili all'esercizio di origine se sono targati come riportati
-- Per annotazioni su partita di giro nel bilancio CdS:
-- - se il documento ? la controparte tronca allora non ? de-riportabile (lo ? l'origine
--   dell'annotazione)
-- - se il documento apre la partita di giro e l'importo riscontrato ? positivo, allora non pu?
--   essere deriportato
-- - se il documento apre la partita di giro e l'importo riscontrato ? nullo, allora ? pu?
--   essere deriportato se targato come riportato
-- - se non apre la partita di giro, e la controparte di apertura dell'annotazione ? riscontrata,
--   e il documento ? targato come riportato allora pu? riportato indietro
-- - se non apre la partita di giro, e la controparte di apertura dell'annotazione non ?
--   riscontrata, allora non pu? essere portata indietro (lo ? la controparte)
 if aTiGestione = 'S' then
 	for aObb in (select * from obbligazione
			 	 where cd_cds = aCdCdsDoc
				   and esercizio = aEsDoc
				   and esercizio_originale = aEsOriDoc
				   and pg_obbligazione = aPgDoc) loop
		if aObb.ti_appartenenza = 'C' then
		   return aObb.riportato;
		else --  aObb.ti_appartenenza = 'D'
		   if aObb.fl_pgiro = 'N' then
		   	  return aObb.riportato;
		   else -- pgiro
			   if aObb.dt_cancellazione is not null and aObb.im_obbligazione = 0 then
			   	  return 'N';
			   end if;

		       begin
			  	   select esercizio_ori_accertamento, pg_accertamento into aEsOriAcc, aPgAcc
				   from ass_obb_acr_pgiro
				   where cd_cds = aObb.cd_cds
				     and esercizio = aObb.esercizio
				     and esercizio_ori_obbligazione = aObb.esercizio_originale
					 and pg_obbligazione = aObb.pg_obbligazione
					 and ti_origine 	 = 'S';  -- apre pgiro
				   -- devo verificare come ? stata generata nel nuovo anno
			  	   aImRiscontrato := getIm_riscontrato(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,'S');
				   if aImRiscontrato > 0 then
				   	  return 'N';
				   else
					  return aObb.riportato;
				   end if;
			   exception when NO_DATA_FOUND then -- non apre pgiro
				   select esercizio_ori_accertamento, pg_accertamento into aEsOriAcc, aPgAcc
				   from ass_obb_acr_pgiro
				   where cd_cds = aObb.cd_cds
				     and esercizio = aObb.esercizio
				     and esercizio_ori_obbligazione = aObb.esercizio_originale
					 and pg_obbligazione = aObb.pg_obbligazione
					 and ti_origine = 'E';

				   -- se origine pgiro riscontrata, allora chiusura pgiro riportata
			  	   aImRiscontrato := getIm_riscontrato(aObb.esercizio,aObb.cd_cds,aEsOriAcc,aPgAcc,'E');
				   if aImRiscontrato > 0 and aObb.riportato = 'Y' then
				   	  return 'Y';
				   else
					  return 'N';
				   end if;
			   end;
		   end if;
		end if;
	end loop;
 else -- entrate
 	for aAcc in (select * from accertamento
			 	 where cd_cds = aCdCdsDoc
				   and esercizio = aEsDoc
				   and esercizio_originale = aEsOriDoc
				   and pg_accertamento = aPgDoc) loop
		if aAcc.ti_appartenenza = 'C' then
		   return aAcc.riportato;
		else --  aObb.ti_appartenenza = 'D'
		   if aAcc.fl_pgiro = 'N' then
		   	  return aAcc.riportato;
		   else -- pgiro
			   if aAcc.dt_cancellazione is not null and aAcc.im_accertamento = 0 then
			   	  return 'N';
			   end if;

		       begin
			  	   select esercizio_ori_obbligazione, pg_obbligazione into aEsOriObb, aPgObb
				   from ass_obb_acr_pgiro
				   where cd_cds = aAcc.cd_cds
				     and esercizio = aAcc.esercizio
				     and esercizio_ori_accertamento = aAcc.esercizio_originale
					 and pg_accertamento = aAcc.pg_accertamento
					 and ti_origine 	 = 'E';  -- apre pgiro
				   -- devo verificare come ? stata generata nel nuovo anno
			  	   aImRiscontrato := getIm_riscontrato(aAcc.esercizio,aAcc.cd_cds,aAcc.esercizio_originale,aAcc.pg_accertamento,'E');
				   if aImRiscontrato > 0 then
				   	  return 'N';
				   else
					  return aAcc.riportato;
				   end if;
			   exception when NO_DATA_FOUND then -- non apre pgiro
				   select esercizio_ori_obbligazione, pg_obbligazione into aEsOriObb, aPgObb
				   from ass_obb_acr_pgiro
				   where cd_cds = aAcc.cd_cds
				     and esercizio = aAcc.esercizio
				     and esercizio_ori_accertamento = aAcc.esercizio_originale
					 and pg_accertamento = aAcc.pg_accertamento
					 and ti_origine 	 = 'S';

				   -- se origine pgiro riscontrata, allora chiusura pgiro riportata
			  	   aImRiscontrato := getIm_riscontrato(aAcc.esercizio,aAcc.cd_cds,aEsOriObb,aPgObb,'S');
				   if aImRiscontrato > 0 and aAcc.riportato = 'Y' then
				   	  return 'Y';
				   else
					  return 'N';
				   end if;
			   end;
		   end if;
		end if;
	end loop;
 end if;
end;
/


