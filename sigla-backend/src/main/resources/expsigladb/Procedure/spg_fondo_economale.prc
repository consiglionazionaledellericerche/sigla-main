CREATE OR REPLACE PROCEDURE SPG_FONDO_ECONOMALE
--
--
-- Date 22/03/2011
-- Version: 1.12
--
--
-- History:
--
-- Date: 26/09/2005
-- Version: 1.8
-- Creazione
--
-- Date: 11/03/2003
-- Version: 1.1
-- Corretta la visualizzazione della Descrizione dell'ultima riga: eliminato il carattere dell' Euro.
--
-- Date: 13/03/2003
-- Version: 1.2
-- Corretto filtro su dettagli di spesa: vengono considerati solo quelli
-- con dt_spesa compresa nel periodo di riferimento
--
-- Date: 08/04/2003
-- Version: 1.3
-- Corretta estrazione dettagli di spese non documentate (null i campi di obbligazione)
--
-- Date: 09/04/2003
-- Version: 1.4
-- Gestione mandati di incremento ammontare iniziale del fondo
-- Corretto inserimento mandati di reitegro (lo stesso mandato pu? reitegrare pi? spese)
--
-- Date: 06/05/2003
-- Version: 1.5
-- Corretto ordinamento dettagli mandati di reintegro (segnalazione n.587)
--
-- Date: 20/05/2003
-- Version: 1.6
-- Corretta gestione mandati di reintegro (segnalazione n. 601)
-- (Record duplicato)
--
-- Date: 25/08/2003
-- Version: 1.7
-- Aggiunta la stampa della reversale di regolarizzazione
-- per modifiche alla gestione del fondo per chiusura contabile e per segnalazione n. 622)
--
-- Date: 18/07/2006
-- Version: 1.8
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 15/01/2007
-- Version: 1.9
-- Modificata procedura per conversione stampa da .rpt a .jasper
--
--
-- Date: 30/01/2007
-- Version: 1.9
-- Corretta la visualizzazione degli importi spese.
-- L'importo veniva preso da FONDO_SPESA e ora da V_DOC_AMM_OBBLIG (dettaglio)
--
--
-- Date 16/07/2007
-- Version: 1.10
-- Modificato l'importo della versione 1.9 se la spesa non ? documentata viene preso l'importo da FONDO_SPESA
-- altrimenti da V_DOC_AMM_OBBLIG. decode(aFsp.FL_DOCUMENTATA,'N',aFsp.IM_AMMONTARE_SPESA,aCd_Voce.IM_VOCE)
--
--
-- Date 22/01/2008
-- Version: 1.11
-- Modificato l'inserimento di 'D:ultima riga' - 'V'. Creato un loop.
--
--
-- Date 22/03/2011
-- Version: 1.12
-- Modificato, nel "ciclo sulle righe del FONDO_ECONOMALE per i REINTEGRI - loop 4", l'ordinamento
-- order by	fsp_R.PG_MANDATO,fsp_R.PG_FONDO_SPESA prima era solo per pg_fondo_spesa.
-- Body:
--
(
 aCd_cds in varchar2,
 aCd_unita_organizzativa in varchar2,
 aEs  number,
 aCd_codice_fondo in varchar2,
 aDt_da in varchar2,
 aDt_a in varchar2,
 aUtcr in varchar2
) is
 aId number;
 i number;
 aVar1 varchar2(300) := null;
 aVar2 varchar2(300) := null;
 aDt_emissione date;
 aIm0 number(15,2) := 0;
 aIm1 number(15,2) := 0;
 tmp number := 0;
 aDtNext date;
 aMand mandato%rowtype;
 v_im_residuo number(15,2);
 v_im_spese number(15,2);
 v_im_entrate number(15,2);
 v_im_residuo_new number(15,2);
 v_sequenza number:=1;
begin
 select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;
 i:=0;

 -- Cerca il FONDO_ECONOMALE rispondente alla chiave data
 for aFec in (select * from FONDO_ECONOMALE fec
 	 	   	   where fec.CD_CDS         = aCd_cds
			     and fec.ESERCIZIO		= aEs
			     and fec.CD_UNITA_ORGANIZZATIVA = aCd_unita_organizzativa
			     and fec.CD_CODICE_FONDO	= aCd_codice_fondo) loop
	-- inizio loop 1

	i := i+1;

	-- inizio inserimento record P: testata e prima riga

	-- Cerca il mandato di apertura relativo al Fondo Economale trovato
	select mand.DT_EMISSIONE, mand.IM_MANDATO
	into aDt_emissione, aIm0
	from MANDATO mand
	where mand.CD_CDS     = aFec.CD_CDS
	and   mand.ESERCIZIO  = aFec.ESERCIZIO
	and   mand.PG_MANDATO = aFec.PG_MANDATO;

	if to_date(aDt_da,'yyyy/mm/dd') < trunc(aDt_emissione) then
	-- La stampa ? stata richiesta da una data ANTECEDENTE alla data di emissione del Mand. di Pag.
	-- di apertura del FE: i dati relativi alla prima riga saranno estrapolati dal mandato legato al Fondo Econoamale
		insert into VPG_FONDO_ECONOMALE (ID,
										CHIAVE,
										SEQUENZA,
										DESCRIZIONE,
										CD_CDS,
										ESERCIZIO,
										CD_UNITA_ORGANIZZATIVA,
										CD_CODICE_FONDO,
										TI_RECORD_L1,
										DS_FONDO,
										DS_UNITA_ORGANIZZATIVA,
										CD_TERZO,
										DENOMINAZIONE_SEDE,
										DT_SPESA,
										DS_SPESA,
										PG_MANDATO_FEC,
										IM_ENTRATE,
										IM_RESIDUO,
										UTCR,
										DACR)
		select  aId,
			    'A:testata',
			    i,
				'Stampa RPT',
				aFec.CD_CDS,
				aFec.ESERCIZIO,
				aFec.CD_UNITA_ORGANIZZATIVA,
				aFec.CD_CODICE_FONDO,
				'P',
				aFec.DS_FONDO,
				uo.DS_UNITA_ORGANIZZATIVA,
				aFec.CD_TERZO,
				t.DENOMINAZIONE_SEDE,
				mand.DT_EMISSIONE,
				mand.DS_MANDATO || ' - Mandato n. ' || aFec.PG_MANDATO,
				aFec.PG_MANDATO,
				mand.IM_MANDATO,
				mand.IM_MANDATO,
				aUtcr,
				sysdate
		from MANDATO mand,
			 UNITA_ORGANIZZATIVA uo,
	 	 	 TERZO t
		 where mand.CD_CDS    = aFec.CD_CDS
		 and   mand.ESERCIZIO = aFec.ESERCIZIO
		 and   mand.PG_MANDATO= aFec.PG_MANDATO
	  	 and   t.CD_TERZO 	  = aFec.CD_TERZO
		 and   uo.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA ;

		-- mandati di incremento ammontare iniziale del fondo
		for aMandato in (select man.* from ass_fondo_eco_mandato afem, mandato man
					   	   where afem.CD_CDS				 = aFec.CD_CDS
						     and afem.ESERCIZIO				 = aFec.ESERCIZIO
							 and afem.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
							 and afem.CD_CODICE_FONDO		 = aFec.CD_CODICE_FONDO
							 and man.CD_CDS					 = afem.CD_CDS_MANDATO
							 and man.ESERCIZIO				 = afem.ESERCIZIO_MANDATO
							 and man.PG_MANDATO				 = afem.PG_MANDATO
						    order by man.PG_MANDATO) loop

			i := i+1;


			aIm0 := aIm0 + aMandato.IM_MANDATO;

			insert into VPG_FONDO_ECONOMALE  (ID,
											CHIAVE,
											SEQUENZA,
											DESCRIZIONE,
											CD_CDS,
											ESERCIZIO,
											CD_UNITA_ORGANIZZATIVA,
											CD_CODICE_FONDO,
											TI_RECORD_L1,
											DS_SPESA,
											PG_MANDATO_FSP,
											IM_ENTRATE,
											UTCR,
											DACR)
			values (aId,
				    'A:testata',
				    i,
					'Stampa RPT',
					aFec.CD_CDS,
					aFec.ESERCIZIO,
					aFec.CD_UNITA_ORGANIZZATIVA,
					aFec.CD_CODICE_FONDO,
					'P',
					'Mandato n. ' || aMandato.PG_MANDATO || ' per incremento ammontare iniziale fondo economale',
					aMandato.PG_MANDATO,
					aMandato.IM_MANDATO,
					aUtcr,
					sysdate);

		end loop; -- fine loop sui mandati di incremento
	else
	-- La stampa ? stata richiesta da una data SUCCESSIVA alla data di emissione del Mand. di Pag.
	-- 	  di apertura del FE

		-- determino l'importo di incremento sul fondo tramite mandati con dt emissione
		-- precedente alla data di riferimento della stampa

		select sum(man.IM_MANDATO) into aIm0
		from ass_fondo_eco_mandato afem
			,mandato man
		where afem.CD_CDS				  = aFec.CD_CDS
		  and afem.ESERCIZIO			  = aFec.ESERCIZIO
		  and afem.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
		  and afem.CD_CODICE_FONDO		  = aFec.CD_CODICE_FONDO
		  and man.CD_CDS				  = afem.CD_CDS_MANDATO
		  and man.ESERCIZIO				  = afem.ESERCIZIO_MANDATO
		  and man.PG_MANDATO			  = afem.PG_MANDATO
		  and man.DT_EMISSIONE			  <= to_date(aDt_da,'yyyy/mm/dd');

		select (aFec.IM_AMMONTARE_INIZIALE - nvl(a.amm_sp,0) + nvl(b.im_mand,0) + aIm0)
		into aIm1
		from (select sum(nvl(fsp.IM_AMMONTARE_SPESA,0)) amm_sp
			 from FONDO_SPESA fsp
			 where fsp.CD_CDS 	 			  = aFec.CD_CDS
			 and   fsp.ESERCIZIO 			  = aFec.ESERCIZIO
			 and   fsp.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
			 and   fsp.CD_CODICE_FONDO 		  = aFec.CD_CODICE_FONDO
			 and   fsp.DT_SPESA	  < to_date(aDt_a,'YYYY/MM/DD')
			 ) a,
			 (select sum(nvl(mand.IM_MANDATO,0)) im_mand
					 from FONDO_SPESA fsp, MANDATO mand
					 where fsp.CD_CDS 	   		      = aFec.CD_CDS
					 and   fsp.ESERCIZIO 	 		  = aFec.ESERCIZIO
					 and   fsp.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
					 and   fsp.CD_CODICE_FONDO 		  = aFec.CD_CODICE_FONDO
					 and   fsp.DT_SPESA  < to_date(aDt_a,'YYYY/MM/DD')
					 and   fsp.CD_CDS_MANDATO 		  = mand.CD_CDS
					 and   fsp.ESERCIZIO_MANDATO 	  = mand.ESERCIZIO
					 and   fsp.PG_MANDATO 			  = mand.PG_MANDATO
					 and   mand.DT_EMISSIONE  <to_date(aDt_a,'YYYY/MM/DD')
			  ) b;

		insert into VPG_FONDO_ECONOMALE  (ID,
									CHIAVE,
									SEQUENZA,
									DESCRIZIONE,
									CD_CDS,
									ESERCIZIO,
									CD_UNITA_ORGANIZZATIVA,
									CD_CODICE_FONDO,
									TI_RECORD_L1,
									DS_FONDO,
									DS_UNITA_ORGANIZZATIVA,
									CD_TERZO,
									DENOMINAZIONE_SEDE,
									DT_SPESA,
									DS_SPESA,
									IM_ENTRATE,
									IM_RESIDUO,
									UTCR,
									DACR)

		select aId,
			    'A:testata',
			    i,
				'Stampa RPT',
				aFec.CD_CDS,
				aFec.ESERCIZIO,
				aFec.CD_UNITA_ORGANIZZATIVA,
				aFec.CD_CODICE_FONDO,
				'P',
				aFec.DS_FONDO,
				uo.DS_UNITA_ORGANIZZATIVA,
				aFec.CD_TERZO,
				t.DENOMINAZIONE_SEDE,
			   	to_date(aDt_da,'YYYY/MM/DD'),
				'Valore Iniziale Fondo',
				aIm1,
				aIm1,
				aUtcr,
				sysdate
		from UNITA_ORGANIZZATIVA uo,
	 	 	 TERZO t
		where t.CD_TERZO = aFec.CD_TERZO
		and   uo.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA;

		-- ciclo sui mandati di incremento con dt emissione successiva alla
		-- data di riferimento della stampa

		for aMandato in (select man.* from ass_fondo_eco_mandato afem, mandato man
				 	     where afem.CD_CDS				   = aFec.CD_CDS
					       and afem.ESERCIZIO			   = aFec.ESERCIZIO
					   	   and afem.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
					   	   and afem.CD_CODICE_FONDO		   = aFec.CD_CODICE_FONDO
					   	   and man.CD_CDS				   = afem.CD_CDS_MANDATO
					   	   and man.ESERCIZIO			   = afem.ESERCIZIO_MANDATO
					   	   and man.PG_MANDATO			   = afem.PG_MANDATO
					   	   and man.DT_EMISSIONE	   > to_date(aDt_da,'yyyy/mm/dd')) loop

			i:= i+1;

			aIm1 := aIm1 + aMandato.IM_MANDATO;

			insert into VPG_FONDO_ECONOMALE (ID,
											CHIAVE,
											SEQUENZA,
											DESCRIZIONE,
											CD_CDS,
											ESERCIZIO,
											CD_UNITA_ORGANIZZATIVA,
											CD_CODICE_FONDO,
											TI_RECORD_L1,
											DS_SPESA,
											PG_MANDATO_FSP,
											IM_ENTRATE,
											UTCR,
											DACR)
			values (aId,
				    'A:testata',
				    i,
					'Stampa RPT',
					aFec.CD_CDS,
					aFec.ESERCIZIO,
					aFec.CD_UNITA_ORGANIZZATIVA,
					aFec.CD_CODICE_FONDO,
					'P',
					'Mandato n. ' || aMandato.PG_MANDATO || ' per incremento ammontare iniziale fondo economale',
					aMandato.PG_MANDATO,
					aMandato.IM_MANDATO,
					aUtcr,
					sysdate);

		end loop; -- fine ciclo sui mandati di incremento dell'importo

	end if;
	-- fine inserimento record P: testata e prima riga

	-- estraggo le dt_spesa delle righe del FONDO_ECONOMALE - loop 2
	for aDt in (select distinct(dt_fsp.DT_SPESA) DT_SPESA FROM FONDO_SPESA dt_fsp
			   	where dt_fsp.CD_CDS 		 = aFec.CD_CDS
				and   dt_fsp.ESERCIZIO 	     = aFec.ESERCIZIO
				and   dt_fsp.CD_CODICE_FONDO = aFec.CD_CODICE_FONDO
				and   dt_fsp.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
				order by dt_fsp.DT_SPESA) loop

		-- ciclo sulle righe del FONDO_ECONOMALE per le SPESE - loop 3
		for aFsp in (select * from FONDO_SPESA fsp
	 	 	   	   where fsp.CD_CDS         = aFec.CD_CDS
				     and fsp.ESERCIZIO		= aFec.ESERCIZIO
				     and fsp.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
				     and fsp.CD_CODICE_FONDO= aFec.CD_CODICE_FONDO
					 and fsp.DT_SPESA 		= aDt.DT_SPESA
					 and fsp.DT_SPESA>= to_date(aDt_da,'YYYY/MM/DD')
					 and fsp.DT_SPESA	<= to_date(aDt_a,'YYYY/MM/DD')
					 order by fsp.PG_FONDO_SPESA) loop
		-- inizio loop 3

			-- inizio inserimento record S: spese
			if aFsp.FL_DOCUMENTATA = 'Y' then
			-- La riga di FONDO_SPESA ? associata a documenti amministrativi
			-- recupero le OBBLIGAZIONI relative alla riga di FONDO_SPESA.
			-- I dati relativi alle Obbligazioni legate ai Documenti Amministrativi sono recuperati
			-- 	 dalla vista V_DOC_AMM_OBBLIG
			   for aObb in (select distinct CD_CDS_OBBLIG, ESERCIZIO_OBBLIG, ESERCIZIO_ORI_OBBLIG, PG_OBBLIG, PG_OBBLIGAZIONE_SCADENZARIO
							from V_DOC_AMM_OBBLIG v_obblig
							where v_obblig.CD_CDS_DOC_AMM    = aFsp.CD_CDS_DOC_AMM
							and   v_obblig.CD_UO_DOC_AMM  	 = aFsp.CD_UO_DOC_AMM
							and   v_obblig.ESERCIZIO_DOC_AMM = aFsp.ESERCIZIO_DOC_AMM
							and   v_obblig.PG_DOC_AMM 		 = aFsp.PG_DOCUMENTO_AMM
							and   v_obblig.CD_TIPO_DOCUMENTO_AMM = aFsp.CD_TIPO_DOCUMENTO_AMM) loop

				 -- Ciclo sulle Obbligazioni trovate e recupero i dati relativi ai Capitoli per ogni singola
				 --	   Obbligazione.
			      for aCd_Voce in (select distinct CD_VOCE, sum(im_voce) im_voce --distinct(cd_voce)
				  	  		    from V_DOC_AMM_OBBLIG v_cap
				   	   			where v_cap.CD_CDS_DOC_AMM   = aFsp.CD_CDS_DOC_AMM
								and   v_cap.CD_UO_DOC_AMM  	 = aFsp.CD_UO_DOC_AMM
								and   v_cap.ESERCIZIO_DOC_AMM= aFsp.ESERCIZIO_DOC_AMM
								and   v_cap.PG_DOC_AMM 		 = aFsp.PG_DOCUMENTO_AMM
								and   v_cap.CD_TIPO_DOCUMENTO_AMM = aFsp.CD_TIPO_DOCUMENTO_AMM
								and   v_cap.CD_CDS_OBBLIG    = aObb.CD_CDS_OBBLIG
								and   v_cap.ESERCIZIO_OBBLIG = aObb.ESERCIZIO_OBBLIG
								and   v_cap.ESERCIZIO_ORI_OBBLIG = aObb.ESERCIZIO_ORI_OBBLIG
								and   v_cap.PG_OBBLIG		 = aObb.PG_OBBLIG
								and   v_cap.PG_OBBLIGAZIONE_SCADENZARIO = aObb.PG_OBBLIGAZIONE_SCADENZARIO
								and   v_cap.IM_VOCE>0
								group by v_cap.ESERCIZIO_OBBLIG,v_cap.ESERCIZIO_ORI_OBBLIG,
									  	 v_cap.PG_OBBLIG,v_cap.PG_OBBLIGAZIONE_SCADENZARIO,v_cap.CD_VOCE,v_cap.progressivo_riga
										 --aggiunta la group by e sum(im_voce)
								) loop


				     i := i+1;

					 insert into VPG_FONDO_ECONOMALE CD_TIPO_DOC_AMM (ID,
												CHIAVE,
												SEQUENZA,
												DESCRIZIONE,
												CD_CDS,
												ESERCIZIO,
												CD_UNITA_ORGANIZZATIVA,
												CD_CODICE_FONDO,
												TI_RECORD_L1,
												PG_FONDO_SPESA,
												DT_SPESA,
												FL_FORNITORE_SALTUARIO,
												CD_TERZO,
												DENOMINAZIONE_FORNITORE,
												FL_DOCUMENTATA,
												CD_TIPO_DOC_AMM,
												DS_SPESA,
												IM_NETTO_SPESA,
												ESERCIZIO_ORI_OBBLIGAZIONE,
												PG_OBBLIGAZIONE,
												ESERCIZIO_OBBLIGAZIONE,
												PG_OBBLIGAZIONE_SCAD,
												CD_VOCE,
												PG_DOC_AMM,
												ESERCIZIO_DOC_AMM,
												FL_REINTEGRATA,
												IM_SPESE,
												UTCR,
												DACR)
					VALUES (aId,
						    'B:spese',
						    i,
							'Stampa RPT',
							aFec.CD_CDS,
							aFec.ESERCIZIO,
							aFec.CD_UNITA_ORGANIZZATIVA,
							aFec.CD_CODICE_FONDO,
							'S',
							aFsp.PG_FONDO_SPESA,
							aFsp.DT_SPESA,
							aFsp.FL_FORNITORE_SALTUARIO,
							aFsp.CD_TERZO,
							aFsp.DENOMINAZIONE_FORNITORE,
							aFsp.FL_DOCUMENTATA,
							aFsp.CD_TIPO_DOCUMENTO_AMM,
							decode (aFsp.CD_TIPO_DOCUMENTO_AMM,'MISSIONE',aFsp.DS_SPESA||' - Importo netto: ¬ '||aFsp.IM_NETTO_SPESA,
								   							   'COMPENSO',aFsp.DS_SPESA||' - Importo netto: '||aFsp.IM_NETTO_SPESA,
															   aFsp.DS_SPESA),
							aFsp.IM_NETTO_SPESA,
							aObb.ESERCIZIO_ORI_OBBLIG,
							aObb.PG_OBBLIG,
							aObb.ESERCIZIO_OBBLIG,
							aObb.PG_OBBLIGAZIONE_SCADENZARIO,
							aCd_Voce.CD_VOCE,
							aFsp.PG_DOCUMENTO_AMM,
							aFsp.ESERCIZIO_DOC_AMM,
							aFsp.FL_REINTEGRATA,
							decode(aFsp.FL_DOCUMENTATA,'N',aFsp.IM_AMMONTARE_SPESA,aCd_Voce.IM_VOCE),
							--aCd_Voce.IM_VOCE,
							--aFsp.IM_AMMONTARE_SPESA,
							aUtcr,
							sysdate);
				 end loop; -- fine ciclo sui CAPITOLI
			   end loop; -- fine ciclo sulle OBBLIGAZIONI
			else
			-- La riga di FONDO_SPESA NON ? associata a documenti amministrativi: le informazioni relative
			-- all'Obbligazione sono presenti sulla riga del Fondo Spesa.
			-- FALSO! potrebbe non esserci ancora l'associazione!

			   if aFsp.CD_CDS_OBBLIGAZIONE is not null then
			   -- associazione gi? effettuata
				   for aCd_Voce in(select  CD_VOCE, sum(im_voce) im_voce --distinct(cd_voce)
				   	   			from OBBLIGAZIONE_SCAD_VOCE obb
				   	   			where obb.CD_CDS          = aFsp.CD_CDS_OBBLIGAZIONE
								and  obb.ESERCIZIO 	  = aFsp.ESERCIZIO_OBBLIGAZIONE
								and  obb.ESERCIZIO_ORIGINALE = aFsp.ESERCIZIO_ORI_OBBLIGAZIONE
								and  obb.PG_OBBLIGAZIONE = aFsp.PG_OBBLIGAZIONE
								and  obb.PG_OBBLIGAZIONE_SCADENZARIO = aFsp.PG_OBBLIGAZIONE_SCADENZARIO
								and  obb.IM_VOCE>0
								group by obb.ESERCIZIO, obb.ESERCIZIO_ORIGINALE,
								obb.PG_OBBLIGAZIONE,obb.PG_OBBLIGAZIONE_SCADENZARIO,obb.CD_VOCE
								--aggiunta la group by e sum(im_voce)
								) loop

						 i := i+1;

						 insert into VPG_FONDO_ECONOMALE  (ID,
													CHIAVE,
													SEQUENZA,
													DESCRIZIONE,
													CD_CDS,
													ESERCIZIO,
													CD_UNITA_ORGANIZZATIVA,
													CD_CODICE_FONDO,
													TI_RECORD_L1,
													PG_FONDO_SPESA,
													DT_SPESA,
													FL_FORNITORE_SALTUARIO,
													CD_TERZO,
													DENOMINAZIONE_FORNITORE,
													FL_DOCUMENTATA,
													DS_SPESA,
													ESERCIZIO_ORI_OBBLIGAZIONE,
													PG_OBBLIGAZIONE,
													ESERCIZIO_OBBLIGAZIONE,
													PG_OBBLIGAZIONE_SCAD,
													CD_VOCE,
													FL_REINTEGRATA,
													IM_SPESE,
													UTCR,
													DACR)
						VALUES (aId,
							    'B:spese',
							    i,
								'Stampa RPT',
								aFec.CD_CDS,
								aFec.ESERCIZIO,
								aFec.CD_UNITA_ORGANIZZATIVA,
								aFec.CD_CODICE_FONDO,
								'S',
								aFsp.PG_FONDO_SPESA,
								aFsp.DT_SPESA,
								aFsp.FL_FORNITORE_SALTUARIO,
								aFsp.CD_TERZO,
								aFsp.DENOMINAZIONE_FORNITORE,
								aFsp.FL_DOCUMENTATA,
								aFsp.DS_SPESA,
								aFsp.ESERCIZIO_ORI_OBBLIGAZIONE,
								aFsp.PG_OBBLIGAZIONE,
								aFsp.ESERCIZIO_OBBLIGAZIONE,
								aFsp.PG_OBBLIGAZIONE_SCADENZARIO,
								aCd_Voce.CD_VOCE,
								aFsp.FL_REINTEGRATA,
								decode(aFsp.FL_DOCUMENTATA,'N',aFsp.IM_AMMONTARE_SPESA,aCd_Voce.IM_VOCE),
								--aCd_Voce.IM_VOCE,
								--aFsp.IM_AMMONTARE_SPESA,
								aUtcr,
								sysdate);

					 end loop; -- fine loop sui capitoli per FL_DOCUMENTATA = 'N'
			   else -- associazione non ancora effettuata
					 i := i+1;

					 insert into VPG_FONDO_ECONOMALE  (ID,
												CHIAVE,
												SEQUENZA,
												DESCRIZIONE,
												CD_CDS,
												ESERCIZIO,
												CD_UNITA_ORGANIZZATIVA,
												CD_CODICE_FONDO,
												TI_RECORD_L1,
												PG_FONDO_SPESA,
												DT_SPESA,
												FL_FORNITORE_SALTUARIO,
												CD_TERZO,
												DENOMINAZIONE_FORNITORE,
												FL_DOCUMENTATA,
												DS_SPESA,
        	        	        						        ESERCIZIO_ORI_OBBLIGAZIONE,
												PG_OBBLIGAZIONE,
												ESERCIZIO_OBBLIGAZIONE,
												PG_OBBLIGAZIONE_SCAD,
												FL_REINTEGRATA,
												IM_SPESE,
												UTCR,
												DACR)
					VALUES (aId,
						    'B:spese',
						    i,
							'Stampa RPT',
							aFec.CD_CDS,
							aFec.ESERCIZIO,
							aFec.CD_UNITA_ORGANIZZATIVA,
							aFec.CD_CODICE_FONDO,
							'S',
							aFsp.PG_FONDO_SPESA,
							aFsp.DT_SPESA,
							aFsp.FL_FORNITORE_SALTUARIO,
							aFsp.CD_TERZO,
							aFsp.DENOMINAZIONE_FORNITORE,
							aFsp.FL_DOCUMENTATA,
							aFsp.DS_SPESA,
							aFsp.ESERCIZIO_ORI_OBBLIGAZIONE,
							aFsp.PG_OBBLIGAZIONE,
							aFsp.ESERCIZIO_OBBLIGAZIONE,
							aFsp.PG_OBBLIGAZIONE_SCADENZARIO,
							aFsp.FL_REINTEGRATA,
							aFsp.IM_AMMONTARE_SPESA,
							aUtcr,
							sysdate);
			   end if;

			end if;

		end loop; -- fine loop (3) per le spese

		-- determino la data di spesa successiva per corretto ordinamento dei
		-- mandati di reintegro

		select min(fsp.DT_SPESA) into aDtNext
		from fondo_spesa fsp
		where fsp.CD_CDS         	  	 = aFec.CD_CDS
		  and fsp.ESERCIZIO				 = aFec.ESERCIZIO
		  and fsp.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
		  and fsp.CD_CODICE_FONDO		 = aFec.CD_CODICE_FONDO
		  and fsp.DT_SPESA 				 > aDt.DT_SPESA;

		-- se ? l'ultima data, assegno al next l'upper bound del range definito
		-- dall'utente
		if aDtNext is null then aDtNext := to_date(aDt_a,'YYYY/MM/DD')+1; end if;

		-- ciclo sulle righe del FONDO_ECONOMALE per i REINTEGRI - loop 4
		for aFsp_R in (select *
					   from FONDO_SPESA fsp_R
	 	 	   	   	   where fsp_R.CD_CDS         = aFec.CD_CDS
				         and fsp_R.ESERCIZIO	  = aFec.ESERCIZIO
				     	 and fsp_R.CD_CODICE_FONDO= aFec.CD_CODICE_FONDO
				     	 and fsp_R.CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
					 	 and fsp_R.FL_REINTEGRATA 		  = 'Y'
						 and exists
						 	 (select 1
	 					 	  from mandato man
	 						  where man.cd_cds     = fsp_R.CD_CDS_MANDATO
	   						    and man.esercizio  = fsp_R.ESERCIZIO_MANDATO
	   							and man.PG_MANDATO = fsp_R.PG_MANDATO
								and man.DT_EMISSIONE>=to_date(aDt_da,'YYYY/MM/DD') -- DT_EMISSIONE compresa nelle date di
				  				and man.DT_EMISSIONE<=to_date(aDt_a ,'YYYY/MM/DD') --	 riferimento della stampa
				  				and man.DT_EMISSIONE >= aDt.DT_SPESA
				  				and man.DT_EMISSIONE < aDtNext
				  				and man.STATO		 <> 'A')
                  order by	fsp_R.PG_MANDATO,fsp_R.PG_FONDO_SPESA) loop
					 	 --order by fsp_R.PG_FONDO_SPESA) loop


		   -- lo stesso mandato pu? reintegrare pi? spese ---> non si deve duplicare l'informazione

   		   select max(sequenza) into tmp
		   from VPG_FONDO_ECONOMALE
		   where ID				        = aId
		     and CHIAVE 				= 'C:reintegri'
		  	 and SEQUENZA				<= i
		  	 and CD_CDS			     	= aFec.CD_CDS
		  	 and ESERCIZIO			    = aFec.ESERCIZIO
		  	 and CD_UNITA_ORGANIZZATIVA = aFec.CD_UNITA_ORGANIZZATIVA
		  	 and CD_CODICE_FONDO		= aFec.CD_CODICE_FONDO
		  	 and TI_RECORD_L1			= 'R'
		  	 and PG_MANDATO_FSP is not null
		  	 and PG_MANDATO_FSP		   	= aFsp_R.PG_MANDATO;

		   if tmp is null then
		   -- il record non ? stato inserito in riferimento ad altri dettagli di spesa

		   	    i := i+1;

				select * into aMand
				from mandato
				where CD_CDS       = aFsp_R.CD_CDS_MANDATO
				  and ESERCIZIO    = aFsp_R.ESERCIZIO_MANDATO
				  and PG_MANDATO   = aFsp_R.PG_MANDATO
				  and DT_EMISSIONE>=to_date(aDt_da,'YYYY/MM/DD') -- DT_EMISSIONE compresa nelle date di
				  and DT_EMISSIONE<=to_date(aDt_a ,'YYYY/MM/DD') --	 riferimento della stampa
				  and DT_EMISSIONE >= aDt.DT_SPESA
				  and DT_EMISSIONE < aDtNext
				  and STATO		   <> 'A'; -- esiste per condizione sul loop

			   -- inizio inserimento record R: reintegri
				insert into VPG_FONDO_ECONOMALE  (ID,
											CHIAVE,
											SEQUENZA,
											DESCRIZIONE,
											CD_CDS,
											ESERCIZIO,
											CD_UNITA_ORGANIZZATIVA,
											CD_CODICE_FONDO,
											TI_RECORD_L1,
											DT_SPESA,
											DS_SPESA,
											PG_MANDATO_FSP,
											IM_ENTRATE,
											UTCR,
											DACR)
				values (aId,
					    'C:reintegri',
					    i,
						'Stampa RPT',
						aFec.CD_CDS,
						aFec.ESERCIZIO,
						aFec.CD_UNITA_ORGANIZZATIVA,
						aFec.CD_CODICE_FONDO,
						'R',
						aMand.DT_EMISSIONE,
						aMand.DS_MANDATO || ' - Mandato n. ' || aFsp_R.PG_MANDATO,
						aFsp_R.PG_MANDATO,
						aMand.IM_MANDATO,
						aUtcr,
						sysdate);
				-- fine   inserimento record R: reintegri

		   end if; -- fine blocco if

		end loop; -- fine loop (4) per i REINTEGRI


	end loop; -- fine loop (2) sulle date

	-- inizio inserimento record U: ultima riga
	-- Controllo se il Fondo Economale ? in stato "Chiuso", (FL_APERTO = 'N'): in tal caso procedo.

	if aFec.FL_APERTO = 'N' then

		-- aMand ? il mandato di regolarizzazione di reintegro del fondo
		-- valorizzato nel loop precedente dei reintegri

		-- inserimento penultima riga 'V' della reversale di regolarizzazione


		for ult_riga in (select rev.DT_EMISSIONE,
								rev.DS_REVERSALE,
								rev.PG_REVERSALE,
								rev.IM_REVERSALE
						from ass_mandato_reversale amr,
							 REVERSALE rev
						where amr.CD_CDS_MANDATO    = aMand.cd_cds
						  and amr.ESERCIZIO_MANDATO = aMand.esercizio
						  and amr.PG_MANDATO		= aMand.pg_mandato
						  and rev.CD_CDS       		= amr.CD_CDS_reversale
						  and rev.ESERCIZIO    		= amr.ESERCIZIO_REVERSALE
						  and rev.PG_REVERSALE 		= amr.PG_REVERSALE
						  and rev.DT_EMISSIONE<= to_date(aDt_a,'YYYY/MM/DD') )loop
				i := i+1;

					insert into VPG_FONDO_ECONOMALE  (ID,
												CHIAVE,
												SEQUENZA,
												DESCRIZIONE,
												CD_CDS,
												ESERCIZIO,
												CD_UNITA_ORGANIZZATIVA,
												CD_CODICE_FONDO,
												TI_RECORD_L1,
												DT_SPESA,
												DS_SPESA,
												PG_REVERSALE,
												IM_SPESE,
												IM_RESIDUO,
												UTCR,
												DACR)
					values ( aId,
						    'D:ultima riga',
						    i,
							'Stampa RPT',
							aFec.CD_CDS,
							aFec.ESERCIZIO,
							aFec.CD_UNITA_ORGANIZZATIVA,
							aFec.CD_CODICE_FONDO,
							'V',
							ult_riga.DT_EMISSIONE,
							ult_riga.DS_REVERSALE || ' - Reversale n. ' || ult_riga.PG_REVERSALE,
							ult_riga.PG_REVERSALE,
							ult_riga.IM_REVERSALE,
							0,
							aUtcr,
							sysdate);

		  end loop;

		i := i+1;


		if aFec.FL_REV_DA_EMETTERE = 'N' and aFec.pg_reversale is not null then

		 -- ? stata emessa la reversale di chiusura
			insert into VPG_FONDO_ECONOMALE  (ID,
										CHIAVE,
										SEQUENZA,
										DESCRIZIONE,
										CD_CDS,
										ESERCIZIO,
										CD_UNITA_ORGANIZZATIVA,
										CD_CODICE_FONDO,
										TI_RECORD_L1,
										DT_SPESA,
										DS_SPESA,
										PG_REVERSALE,
										IM_SPESE,
										IM_RESIDUO,
										UTCR,
										DACR)
			select  aId,
				    'D:ultima riga',
				    i,
					'Stampa RPT',
					aFec.CD_CDS,
					aFec.ESERCIZIO,
					aFec.CD_UNITA_ORGANIZZATIVA,
					aFec.CD_CODICE_FONDO,
					'U',
					rev.DT_EMISSIONE,
					rev.DS_REVERSALE || ' - Reversale n. ' || rev.PG_REVERSALE,
					rev.PG_REVERSALE,
					rev.IM_REVERSALE,
					0,
					aUtcr,
					sysdate
			from REVERSALE rev
			where rev.CD_CDS       = aFec.CD_CDS
			and   rev.ESERCIZIO    = aFec.ESERCIZIO_REVERSALE
			and   rev.PG_REVERSALE = aFec.PG_REVERSALE
			and   rev.DT_EMISSIONE<= to_date(aDt_a,'YYYY/MM/DD');
		end if;


	end if;


	-- fine   inserimento record U: ultima riga

 end loop;  -- fine loop 1

 for rec_im_res in ( 	select *
 	 		from vpg_fondo_economale
 			where id = aId
 			order by sequenza
			) loop
				begin
				--dbms_output.put_line('v_sequenza :'||v_sequenza);
				--v_im_residuo := nvl(rec_im_res.IM_RESIDUO,0);

				select nvl(im_residuo,0) into v_im_residuo
				from vpg_fondo_economale
				where id = rec_im_res.id
				and sequenza = rec_im_res.sequenza;

				select nvl(im_spese,0), nvl(im_entrate,0) into v_im_spese,v_im_entrate
				from vpg_fondo_economale
				where id = aId
				and sequenza = v_sequenza+1;

				--dbms_output.put_line('v_sequenza :'||v_sequenza);
				--dbms_output.put_line('v_im_residuo :'||v_im_residuo||' - v_im_spese: '||v_im_spese||' v_im_entrate: '||v_im_entrate);

				v_im_residuo_new := nvl(v_im_residuo,0) - nvl(v_im_spese,0) + nvl(v_im_entrate,0);
				--dbms_output.put_line('v_sequenza :'||v_sequenza);
				--dbms_output.put_line('v_im_residuo - v_im_spese + v_im_entrate = v_im_residuo_new : '||v_im_residuo||' - '||v_im_spese||' + '||v_im_entrate||' = '||v_im_residuo_new);

					 update vpg_fondo_economale
 					 set im_residuo = v_im_residuo_new
 					 where sequenza = v_sequenza+1
					 and id = aId;

					 v_sequenza := v_sequenza + 1;

				exception
					when no_data_found then
						exit;
				end;
 end loop;

 -- Apro il cursore sulla vista VPG_FONDO_ECONOMALE
/* open tc for
  select * from VPG_FONDO_ECONOMALE where id = aId;*/

-- close tc; --- ELIMINARE PER RICHIAMARE DA CR !!!
end;
/


