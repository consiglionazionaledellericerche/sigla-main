CREATE OR REPLACE PROCEDURE SPG_PARTITARIO_COMPENSI
--
-- Date: 04/12/2006
-- Version: 1.4
--
-- Protocollo VPG per stampa del partitario compensi
--
--
-- History:
--
-- Date: 06/11/2003
-- Version: 1.0
-- Creazione
--
-- Date: 14/11/2003
-- Version: 1.1
-- Aggiunta totalizzazioni importi dei CORI per UO e complessivo per ENTE
--
-- Date: 02/03/2004
-- Version: 1.2
-- Fix query su v_anagrafico_terzo
-- Fix estrazione dati del trattamento
--
-- Date: 18/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 04/12/2006
-- Version: 1.4
-- Modifica per la stampa Riepilogo Compensi
-- aggiunti i campi TOT_DEDUZIONE_IRPEF,TOT_DEDUZIONE_FAMILY
--
-- Body:
--
(
 inCdCds in varchar2,
 inCdUo in varchar2,
 inEs in number,
 inCdTerzo in number
) is
 aId number;
 i number;
 aNum1 number;
 aAnagTerzo v_anagrafico_terzo%rowtype;
 aCdAnag number := 0;
 aImDetPers number := 0;
 aFlNoTaxArea char(2);
 aFlNoFamilyArea char(2);
 isConguagliato char(2);
 aDtTrasmissione date;
 aPgMandato number := 0;
 aTotNetto number(15,2) := 0;
 aTotLordo number(15,2) := 0;
 aTotImponibile number(15,2) := 0;
 aTotDetrPers number(15,2) := 0;
 aTotDetrCon number(15,2) := 0;
 aTotDetrFi number(15,2) := 0;
 aTotDetrAl number(15,2) := 0;
 aTotDetrRidCuneo number(15,2) := 0;
 aTotIrpef number(15,2) := 0;
 aTotFamily number(15,2) := 0;
  uoEnte unita_organizzativa%rowtype;
begin
 select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;
 i:=0;
 uoEnte:=cnrctb020.getUoEnte(inEs);
 -- inserimento dati anagrafici del terzo (indipendentemente dall'esistenza dei compensi
 select cd_anag into aCdAnag
 from terzo
 where cd_terzo = inCdTerzo; -- deve esistere, validazione dall'online

 select * into aAnagTerzo
 from v_anagrafico_terzo
 where cd_anag = aCdAnag
   and cd_terzo = inCdTerzo;

 begin
 	  select decode(fl_notaxarea,'Y','SI','NO'),decode(fl_nofamilyarea,'Y','SI','NO'),im_detrazione_personale_anag
	  into aFlNoTaxArea,aFlNoFamilyArea,aImDetPers
	  from anagrafico_esercizio
	  where cd_anag   = aAnagTerzo.cd_anag
	    and esercizio = inEs;
 exception when no_data_found then
      aFlNoTaxArea := 'NO';
      aFlNoFamilyArea := 'NO';
	  aImDetPers   := 0;
 end;

 insert into vpg_partitario_compensi  (ID,
					CHIAVE,
					TIPO,
					SEQUENZA,
					TI_RECORD_L1,
					TI_RECORD_L2,
					CD_TERZO,
					DENOMINAZIONE_SEDE,
					VIA_RESIDENZA,
					COMUNE_RESIDENZA,
					CD_PROVINCIA_RESIDENZA,
					DS_PROVINCIA_RESIDENZA,
					FRAZIONE_RESIDENZA,
					VIA_FISCALE,
					COMUNE_FISCALE,
					CD_PROVINCIA_FISCALE,
					DS_PROVINCIA_FISCALE,
					FRAZIONE_FISCALE,
					CODICE_FISCALE,
					PARTITA_IVA,
					DATA_NASCITA,
					COMUNE_NASCITA,
					CD_PROVINCIA_NASCITA,
					DS_PROVINCIA_NASCITA,
					ALIQUOTA_FISCALE,
					CD_ATTIVITA_INPS,
					ALTRA_ASS_PREVID_INPS,
					SEDE_INAIL,
					MATRICOLA_INAIL,
					CD_ANAG_CORR,
					FL_NOTAXAREA,
					FL_NOFAMILYAREA,
					IM_DETRAZIONE_PERSONALE_ANAG,
					DT_FINE_RAPPORTO,
					CAUSALE_FINE_RAPPORTO)
 select aId,
 		'chiave',
		't',
		i,
		'A',
		'A',
		aAnagTerzo.cd_terzo,
		aAnagTerzo.denominazione_sede,
		aAnagTerzo.via_sede||decode(aAnagTerzo.numero_civico_sede,null,null,' n.'||aAnagTerzo.numero_civico_sede),
		aAnagTerzo.DS_COMUNE_SEDE,
		aAnagTerzo.CD_PROVINCIA_SEDE,
		aAnagTerzo.DS_PROVINCIA_SEDE,
		aAnagTerzo.FRAZIONE_SEDE,
		aAnagTerzo.VIA_FISCALE||decode(aAnagTerzo.NUM_CIVICO_FISCALE,null,null,' n.'||aAnagTerzo.NUM_CIVICO_FISCALE),
		aAnagTerzo.DS_COMUNE_FISCALE,
		aAnagTerzo.CD_PROVINCIA_FISCALE,
		aAnagTerzo.DS_PROVINCIA_FISCALE,
		aAnagTerzo.FRAZIONE_FISCALE,
		aAnagTerzo.CODICE_FISCALE,
		aAnagTerzo.PARTITA_IVA,
		aAnagTerzo.DT_NASCITA,
		aAnagTerzo.DS_COMUNE_NASCITA,
		aAnagTerzo.CD_PROVINCIA_NASCITA,
		aAnagTerzo.DS_PROVINCIA_NASCITA,
		an.ALIQUOTA_FISCALE,
		aAnagTerzo.CD_ATTIVITA_INPS,
		aAnagTerzo.ALTRA_ASS_PREVID_INPS,
		aAnagTerzo.SEDE_INAIL,
		aAnagTerzo.MATRICOLA_INAIL,
		aAnagTerzo.CD_ENTE_APPARTENENZA,
		aFlNoTaxArea,
		aFlNoFamilyArea,
		aImDetPers,
		aAnagTerzo.DT_FINE_RAPPORTO,
		aAnagTerzo.CAUSALE_FINE_RAPPORTO
 from anagrafico an
 where an.CD_ANAG = aAnagTerzo.cd_anag;
 -- fine inserimento dati anagrafici del percipiente

 -- inserimento dati dei compensi
 for aCds in (select * from unita_organizzativa
 	 	  	  where cd_unita_organizzativa like inCdCds
			    and fl_cds 				   = 'Y'
			  order by cd_unita_organizzativa asc) loop

    -- inizio loop 1 (cds)
 	for aUo in (select * from unita_organizzativa
			    where cd_unita_organizzativa like inCdUo
				  and fl_cds 				 = 'N'
				  and cd_unita_padre 		 = aCds.cd_unita_organizzativa
				order by cd_unita_organizzativa asc) loop
				-- l'ente deve essere l'ultimo elaborato per poter totalizzare

	   -- inizio loop 2 (uo)
	   for aComp in (select * from compenso
	   	   		 	 where cd_cds  		   	      = aCds.cd_unita_organizzativa
					   and cd_unita_organizzativa = aUo.cd_unita_organizzativa
					   --and esercizio 			  = inEs
					   and To_Char(dt_emissione_mandato,'YYYY')  = inEs
					   and cd_terzo 			  = inCdTerzo
					   and dt_cancellazione is null -- compensi non annullati
					 order by pg_compenso) loop

	      -- inizio loop 3 (compensi)
	   	  i := i + 1;

		  -- cerco il mandato principale, a meno che il compenso trasiti per il fondo economale
		  if aComp.stato_pagamento_fondo_eco = 'N' then
			  if aComp.pg_obbligazione is not null then
				  begin  -- determinazione del mandato principale
				  	   select pg_mandato, dt_trasmissione
					   into aPgMandato, aDtTrasmissione
					   from mandato man
					   where exists (select 1 from mandato_riga mriga
					   		 		 where mriga.cd_cds				   = aComp.cd_cds_obbligazione
									   and mriga.ESERCIZIO_OBBLIGAZIONE= aComp.esercizio_obbligazione
									   and mriga.ESERCIZIO_ORI_OBBLIGAZIONE= aComp.esercizio_ori_obbligazione
									   and mriga.PG_OBBLIGAZIONE	   = aComp.pg_obbligazione
									   and mriga.PG_OBBLIGAZIONE_SCADENZARIO = aComp.pg_obbligazione_scadenzario
									   and mriga.cd_cds_doc_amm        = aComp.cd_cds
					     			   	   and mriga.cd_uo_doc_amm 		   = aComp.cd_unita_organizzativa
						 			   and mriga.esercizio_doc_amm 	   = aComp.esercizio
						 			   and mriga.cd_tipo_documento_amm = 'COMPENSO'
						 			   and mriga.pg_doc_amm			   = aComp.pg_compenso
									   and mriga.cd_cds				   = man.cd_cds
									   and mriga.esercizio			   = man.esercizio
									   and mriga.pg_mandato 		   = man.pg_mandato)
						 and dt_annullamento is null;
				  exception when NO_DATA_FOUND then
				       aPgMandato := 0;
					   aDtTrasmissione := null;
				  end;
			  else
			  	  aPgMandato := 0;
				  aDtTrasmissione := null;
			  end if;
		  else -- transita per il fondo
		  	  aDtTrasmissione := aComp.dt_pagamento_fondo_eco;
			  aPgMandato	  := 0;
		  end if;

		  -- verifico se il compenso ? gi? stato conguagliato
		  begin
		  	   select 1 into aNum1
			   from ass_compenso_conguaglio
			   where cd_cds_compenso    = aComp.cd_cds
			     and cd_uo_compenso		= aComp.cd_unita_organizzativa
				 and esercizio_compenso = aComp.esercizio
				 and pg_compenso		= aComp.pg_compenso;
			   isConguagliato := 'SI';
		  exception when NO_DATA_FOUND then
		       isConguagliato := 'NO';
		  end;

		  -- record principale del compenso
		  insert into vpg_partitario_compensi  (ID,
							CHIAVE,
							TIPO,
							SEQUENZA,
							TI_RECORD_L1,
							TI_RECORD_L2,
							CD_CDS,
							CD_UNITA_ORGANIZZATIVA,
							ESERCIZIO,
							DS_CDS,
							DS_UNITA_ORGANIZZATIVA,
							PG_COMPENSO,
							DS_COMPENSO,
							FL_SENZA_CALCOLI,
							ESERCIZIO_ORI_OBBLIGAZIONE,
							PG_OBBLIGAZIONE,
							PG_OBBLIGAZIONE_SCADENZARIO,
							STATO_PAGAMENTO_FONDO_ECO,
							PG_MANDATO,
							DT_TRASMISSIONE,
							STATO_COFI,
							FL_CONGUAGLIATO,
							CD_TI_RAPPORTO,
							DS_TIPO_RAPPORTO,
							CD_TRATTAMENTO,
							DS_TRATTAMENTO,
							DT_DA_COMPETENZA_COGE,
							DT_A_COMPETENZA_COGE,
							IM_LORDO_PERCIPIENTE,
							IM_NETTO_PERCIPIENTE,
							IM_CR_ENTE,
							IM_CR_PERCIPIENTE,
							IM_DEDUZIONE_IRPEF,
							DETRAZIONI_PERSONALI,
							DETRAZIONI_CONIUGE,
							DETRAZIONI_FIGLI,
							DETRAZIONI_ALTRI,
                            DETRAZIONI_RID_CUNEO)

		  select aId,
		 		'chiave',
				't',
				i,
				'B',
				'A',
				aComp.cd_cds,
				aComp.cd_unita_organizzativa,
				aComp.esercizio,
				uo1.DS_UNITA_ORGANIZZATIVA,
				uo2.DS_UNITA_ORGANIZZATIVA,
				aComp.pg_compenso,
				aComp.ds_compenso,
				decode(aComp.fl_senza_calcoli,'Y','SI','NO'),
				aComp.esercizio_ori_obbligazione,
				aComp.pg_obbligazione,
				aComp.pg_obbligazione_scadenzario,
				decode(aComp.stato_pagamento_fondo_eco,'N','NO','SI'),
				aPgMandato,
				aDtTrasmissione,
				aComp.stato_cofi,
				isConguagliato,
				aComp.CD_TIPO_RAPPORTO,
				rapp.DS_TIPO_RAPPORTO,
				aComp.cd_trattamento,
				tratt.DS_TI_TRATTAMENTO,
				aComp.dt_da_competenza_coge,
				aComp.dt_a_competenza_coge,
				aComp.im_lordo_percipiente,
				aComp.im_netto_percipiente,
				aComp.im_cr_ente,
				aComp.im_cr_percipiente,
				aComp.im_deduzione_irpef,
				aComp.detrazioni_personali,
				aComp.detrazione_coniuge,
				aComp.detrazione_figli,
				aComp.detrazione_altri,
                aComp.detrazione_riduzione_cuneo
		  from unita_organizzativa uo1
		  	  ,unita_organizzativa uo2
			  ,tipo_rapporto rapp
			  ,tipo_trattamento tratt
		  where uo1.CD_UNITA_ORGANIZZATIVA = aComp.cd_cds
		    and uo2.CD_UNITA_ORGANIZZATIVA = aComp.cd_unita_organizzativa
			and rapp.CD_TIPO_RAPPORTO	   = aComp.cd_tipo_rapporto
			and tratt.CD_TRATTAMENTO	   = aComp.cd_trattamento
			and tratt.DT_INI_VALIDITA	   <= aComp.dt_registrazione
			and tratt.DT_FIN_VALIDITA	   >= aComp.dt_registrazione;

		  -- contributi ritenuta
		  for aCori in (select * from contributo_ritenuta
		  	  		    where cd_cds  				 = aComp.cd_cds
						  and cd_unita_organizzativa = aComp.cd_unita_organizzativa
						  and esercizio 			 = aComp.esercizio
						  and pg_compenso 			 = aComp.pg_compenso
						  order by cd_contributo_ritenuta) loop
		     -- inizio loop 4 (cori)
			 i := i + 1;
			 insert into vpg_partitario_compensi   (ID,
								CHIAVE,
								TIPO,
								SEQUENZA,
								TI_RECORD_L1,
								TI_RECORD_L2,
								CD_CDS,
								CD_UNITA_ORGANIZZATIVA,
								ESERCIZIO,
								PG_COMPENSO,
								CD_CONTRIBUTO_RITENUTA,
								DS_CONTRIBUTO_RITENUTA,
								TI_ENTE_PERCIPIENTE,
								IMPONIBILE_LORDO,
								IM_DEDUZIONE_IRPEF,
								ALIQUOTA,
								AMMONTARE_LORDO,
								AMMONTARE,
								IM_DEDUZIONE_FAMILY,
								DETRAZIONI_PERSONALI,
								DETRAZIONI_CONIUGE,
								DETRAZIONI_FIGLI,
								DETRAZIONI_ALTRI,
                                DETRAZIONI_RID_CUNEO)

			 select aId,
		 			'chiave',
					't',
					i,
					'B',
					'B',
					aComp.cd_cds,
					aComp.cd_unita_organizzativa,
					aComp.esercizio,
					aComp.pg_compenso,
					aCori.cd_contributo_ritenuta,
					tcr.DS_CONTRIBUTO_RITENUTA,
					aCori.ti_ente_percipiente,
					aCori.imponibile_lordo,
					aCori.im_deduzione_irpef,
					aCori.aliquota,
					aCori.ammontare_lordo,
					aCori.ammontare,
					aCori.im_deduzione_family,
					aComp.detrazioni_personali,
					aComp.detrazione_coniuge,
					aComp.detrazione_figli,
					aComp.detrazione_altri,
                    aComp.detrazione_riduzione_cuneo
			 from tipo_contributo_ritenuta tcr
			 where tcr.cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
			   and tcr.dt_ini_validita		  = aCori.dt_ini_validita;

		  end loop; -- fine loop 4 (cori)
	   end loop; -- fine loop 3 (compensi)

	   -- determino i totali delle detrazioni per uo
	   select sum(v.DETRAZIONI_PERSONALI),
	   		  sum(v.DETRAZIONI_CONIUGE),
			  sum(v.DETRAZIONI_FIGLI),
			  sum(v.DETRAZIONI_ALTRI),
			  sum(v.IM_DEDUZIONE_IRPEF),
			  sum(v.IM_DEDUZIONE_FAMILY),
              sum(v.DETRAZIONI_RID_CUNEO)
	   into aTotDetrPers, aTotDetrCon, aTotDetrFi, aTotDetrAl, aTotIrpef, aTotFamily, aTotDetrRidCuneo
	   from vpg_partitario_compensi v
	   where id = aId
	     and chiave = 'chiave'
		 and tipo = 't'
		 and ti_record_l1 = 'B'
		 and ti_record_l2 = 'A'
		 and cd_cds = aCds.cd_unita_organizzativa
		 and cd_unita_organizzativa = aUo.cd_unita_organizzativa;



	   -- determino i totali per uo
	   for aCoriIns in (select distinct cd_contributo_ritenuta, ti_ente_percipiente, ds_contributo_ritenuta
	   	   			    from vpg_partitario_compensi
						where ID 					 = aId
						  and chiave 				 = 'chiave'
						  and tipo 					 = 't'
						  and ti_record_l1 			 = 'B'
						  and ti_record_l2 			 = 'B'
						  and cd_cds 				 = aCds.cd_unita_organizzativa
						  and cd_unita_organizzativa = aUo.cd_unita_organizzativa) loop
		   -- inizio loop 5 (cori inseriti)
		   i := i + 1;

		   select sum(ammontare), sum(ammontare_lordo), sum(imponibile_lordo), sum(im_deduzione_irpef), sum(im_deduzione_family)
		   into aTotNetto, aTotLordo, aTotImponibile, aTotIrpef, aTotFamily
		   from  vpg_partitario_compensi
		   where ID 					 = aId
			 and chiave 				 = 'chiave'
			 and tipo 					 = 't'
			 and ti_record_l1 			 = 'B'
			 and ti_record_l2 			 = 'B'
			 and cd_cds 				 = aCds.cd_unita_organizzativa
			 and cd_unita_organizzativa  = aUo.cd_unita_organizzativa
			 and cd_contributo_ritenuta  = aCoriIns.cd_contributo_ritenuta
			 and ti_ente_percipiente	 = aCoriIns.ti_ente_percipiente;


		   insert into vpg_partitario_compensi (ID,
							CHIAVE,
							TIPO,
							SEQUENZA,
							TI_RECORD_L1,
							TI_RECORD_L2,
							CD_CDS,
							CD_UNITA_ORGANIZZATIVA,
							ESERCIZIO,
							CD_CONTRIBUTO_RITENUTA,
							TI_ENTE_PERCIPIENTE,
							DS_CONTRIBUTO_RITENUTA,
							TOT_AMMONTARE_LORDO,
							TOT_AMMONTARE,
							TOT_IMPONIBILE_LORDO,
							TOT_DETRAZIONI_PERSONALI,
							TOT_DETRAZIONI_CONIUGE,
							TOT_DETRAZIONI_FIGLI,
							TOT_DETRAZIONI_ALTRI,
							TOT_DEDUZIONE_IRPEF,
							TOT_DEDUZIONE_FAMILY,
							TOT_DETRAZIONI_RID_CUNEO
							)
		   values (aId,
		   		  'chiave',
				  't',
				  i,
				  'C',
				  'A',
				  aCds.cd_unita_organizzativa,
				  aUo.cd_unita_organizzativa,
				  inEs,
				  aCoriIns.cd_contributo_ritenuta,
				  aCoriIns.ti_ente_percipiente,
				  aCoriIns.ds_contributo_ritenuta,
				  aTotLordo,
				  aTotNetto,
				  aTotImponibile,
				  aTotDetrPers,
				  aTotDetrCon,
				  aTotDetrFi,
				  aTotDetrAl,
				  aTotIrpef,
				  aTotFamily,
				  aTotDetrRidCuneo);

	   end loop; -- fine loop 5 (cori inseriti)

	end loop; -- fine loop 2 (uo)
 end loop; -- fine loop 1 (cds)
 -- fine inserimento dati dei compensi

 -- record di totalizzazione dei cori per l'ente
 if inCdCds = '%' then -- ? l'ente

	-- determino i totali delle detrazioni per uo
	select sum(v.DETRAZIONI_PERSONALI),
		   sum(v.DETRAZIONI_CONIUGE),
		   sum(v.DETRAZIONI_FIGLI),
		   sum(v.DETRAZIONI_ALTRI),
		   sum(v.IM_DEDUZIONE_IRPEF),
		   sum(v.IM_DEDUZIONE_FAMILY),
           sum(v.DETRAZIONI_RID_CUNEO)
	into aTotDetrPers, aTotDetrCon, aTotDetrFi, aTotDetrAl,aTotIrpef,aTotFamily, aTotDetrRidCuneo
	from vpg_partitario_compensi v
	where id = aId
	  and chiave = 'chiave'
	  and tipo = 't'
	  and ti_record_l1 = 'B'
	  and ti_record_l2 = 'A';

 	for aCoriInsEnte in (select distinct cd_contributo_ritenuta, ti_ente_percipiente, ds_contributo_ritenuta
					 	 from vpg_partitario_compensi
						 where ID 			= aId
						   and chiave 		= 'chiave'
						   and tipo 		= 't'
						   and ti_record_l1 = 'C'
						   and ti_record_l2 = 'A') loop
	 	-- inizio loop 6 (cori per ente)
		i := i + 1;

		select sum(tot_ammontare), sum(tot_ammontare_lordo), sum(tot_imponibile_lordo), sum(tot_deduzione_irpef), sum(tot_deduzione_family)
		into aTotNetto, aTotLordo, aTotImponibile, aTotIrpef, aTotFamily
		from vpg_partitario_compensi
		where id = aId
		  and chiave = 'chiave'
		  and tipo	 = 't'
		  and ti_record_l1 = 'C'
		  and ti_record_l2 = 'A'
		  and cd_contributo_ritenuta = aCoriInsEnte.cd_contributo_ritenuta
		  and ti_ente_percipiente	 = aCoriInsEnte.ti_ente_percipiente;

		  dbms_output.put_line('Deduzione Irpef: '||aTotIrpef||'- Deduzione Family: '||aTotIrpef);

		insert into vpg_partitario_compensi (ID,
						     CHIAVE,
						     TIPO,
						     SEQUENZA,
						     TI_RECORD_L1,
						     TI_RECORD_L2,
						     CD_TERZO,
						     CD_CDS,
						     CD_UNITA_ORGANIZZATIVA,
						     CD_CONTRIBUTO_RITENUTA,
						     TI_ENTE_PERCIPIENTE,
						     DS_CONTRIBUTO_RITENUTA,
						     TOT_AMMONTARE_LORDO,
						     TOT_AMMONTARE,
						     TOT_IMPONIBILE_LORDO,
						     TOT_DETRAZIONI_PERSONALI,
						     TOT_DETRAZIONI_CONIUGE,
						     TOT_DETRAZIONI_FIGLI,
						     TOT_DETRAZIONI_ALTRI,
						     TOT_DEDUZIONE_IRPEF,
					             TOT_DEDUZIONE_FAMILY,
                             TOT_DETRAZIONI_RID_CUNEO
						     )
		values (aId,
			    'chiave',
				't',
				i,
				'D',
				'A',
				inCdTerzo,
				cnrctb020.getCDCDSENTE(inEs),
				uoEnte.cd_unita_organizzativa,
				aCoriInsEnte.cd_contributo_ritenuta,
				aCoriInsEnte.ti_ente_percipiente,
				aCoriInsEnte.ds_contributo_ritenuta,
				aTotLordo,
				aTotNetto,
				aTotImponibile,
				aTotDetrPers,
				aTotDetrCon,
				aTotDetrFi,
				aTotDetrAl,
				aTotIrpef,
				aTotFamily,
                aTotDetrRidCuneo
				);

	 end loop; -- fine loop 6 (cori per ente)
 end if;

/*  open tc for
   select * from vpg_partitario_compensi where id = aId;*/

--  close tc; --- ELIMINARE PER RICHIAMARE DA CR !!!

end;
/
