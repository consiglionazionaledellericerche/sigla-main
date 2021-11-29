CREATE OR REPLACE PROCEDURE SPG_CERTIFICAZIONE_770
--
-- Date: 13/07/2004
-- Version: 1.0
--
-- Creazione file per 770 Lavoro Autonomo
--
--
-- History:
--
-- Date: 13/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 20/09/2006
-- Version: 1.1
-- Adeguamento per il 770/2006 - Aggiunta la quota esente INPS
--
-- Date: 11/07/2011
-- Version: 1.2
-- Gestiti i due modelli, semplificato e ordinario, ed i quadri relativi
--
--
-- Body:
--
(tc in out IBMPRT000.t_cursore,
 aEs number,
 aTiModello varchar2,
 aQuadro  varchar2, -- valorizzato a '%' per stampa di tutti i quadri del Modello (per il momento non gestito)
 aCdAnag varchar2, -- valorizzato a '%' per stampa di tutte le anagrafiche
 aNota varchar2) is
aId number;
i number := 0;
aTmpCdAnag number;
aTmpCdAnagPignorato number;
aTmpQuadro varchar2(4) := null;
aTmpDsQuadro varchar2(300);
aTmpTiCompenso varchar2(5) := null;
aTmpDsTiCompenso varchar2(300);
aTmpTiRitenuta varchar2(1) := null;
aTmpPkCompenso varchar2(100);
aTmpAliquota number(10,6);
aImponibileFi number(15,2) :=0 ;
aImponibilePr number(15,2) :=0 ;
aImLordo number(15,2) :=0 ;
aImNonSoggRit number(15,2) :=0 ;
aImNonSoggCori number(15,2) := 0;
aImNonSoggInps number(15,2) := 0;
aImRit number(15,2) :=0 ;
aImCori number(15,2) :=0 ;
aImCoriEnte number(15,2) :=0 ;
aImNetto number(15,2) := 0;
aTmpCdTrattamento varchar2(10) := null;
begin
	select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;

	for aDett in (select * from
			 (select  v.cd_anag
 		 		 ,v.cd_cds
 				 ,v.cd_unita_organizzativa
 				 ,v.esercizio
 				 ,v.pg_compenso
				 ,v.CD_QUADRO
				 ,v.DS_QUADRO
				 ,v.TI_MODELLO
				 ,v.ti_ritenuta
				 ,v.cd_ti_compenso
				 ,v.ds_ti_compenso
				 ,v.im_lordo_percipiente
				 ,v.im_no_fiscale
				 ,v.quota_esente
				 ,v.im_netto_percipiente
				 ,v.quota_esente_inps
				 ,tcr.cd_classificazione_cori
				 ,cr.ti_ente_percipiente
				 ,cr.imponibile
				 ,cr.aliquota
				 ,cr.ammontare
				 ,v.cd_trattamento
				 ,v.cd_anag_pignorato
			  from   v_compenso_770 v
				,contributo_ritenuta cr
				,tipo_contributo_ritenuta tcr
			  where   to_char(v.cd_anag) Like aCdAnag
				  and v.TI_MODELLO = aTiModello
				  and ((aQuadro ='SCSY'
  		    and v.CD_QUADRO  in('SC','SY'))
  		     or v.CD_QUADRO     like  aQuadro)
				  and v.esercizio_mandato = aEs
				  and cr.cd_cds 	  = v.cd_cds
				  and cr.cd_unita_organizzativa   = v.cd_unita_organizzativa
				  and cr.esercizio 	  = v.esercizio
				  and cr.pg_compenso 	  = v.pg_compenso
				  --and cr.ti_ente_percipiente = 'P'
				  and tcr.cd_contributo_ritenuta  = cr.cd_contributo_ritenuta
				  and tcr.dt_ini_validita = cr.dt_ini_validita
				  and tcr.cd_classificazione_cori in ('FI','PR')
			  union all
			  select   v.cd_anag
 		 		 ,v.cd_cds cd_cds
 				 ,v.cd_unita_organizzativa cd_unita_organizzativa
 				 ,v.esercizio esercizio
 				 ,v.pg_compenso pg_compenso
				 ,v.CD_QUADRO
				 ,v.DS_QUADRO
				 ,v.TI_MODELLO
				 ,v.ti_ritenuta
				 ,v.cd_ti_compenso
				 ,v.ds_ti_compenso
				 ,v.im_lordo_percipiente
				 ,v.im_no_fiscale
				 ,v.quota_esente
				 ,v.im_netto_percipiente
				 ,v.quota_esente_inps
				 ,null cd_classificazione_cori
				 ,Null ti_ente_percipiente
				 ,0 imponibile
				 ,0 aliquota
				 ,0 ammontare
				 ,v.cd_trattamento
				 ,v.cd_anag_pignorato
			  from   v_compenso_770 v
			  where   to_char(v.cd_anag)  Like aCdAnag
			  and v.TI_MODELLO = aTiModello
			  and ((aQuadro ='SCSY'
  		  and v.CD_QUADRO  in('SC','SY'))
  		  or v.CD_QUADRO     like  aQuadro)
  		  and v.esercizio_mandato     = aEs
			  and not exists (select 1
			  		  from contributo_ritenuta cr
			   	  	      ,tipo_contributo_ritenuta tcr
					  where cr.cd_cds = v.cd_cds
					  And cr.cd_unita_organizzativa = v.cd_unita_organizzativa
					  And cr.esercizio	= v.esercizio
					  And cr.pg_compenso 	= v.pg_compenso
					  --And cr.ti_ente_percipiente 	  = 'P'
					  And tcr.cd_contributo_ritenuta= cr.cd_contributo_ritenuta
					  And tcr.dt_ini_validita       = cr.dt_ini_validita
					  And tcr.cd_classificazione_cori in ('FI','PR'))
			  )
			  order by cd_anag
			    ,CD_QUADRO
				  ,cd_ti_compenso
				  ,cd_anag_pignorato
			  	  ,cd_cds
				  ,cd_unita_organizzativa
				  ,pg_compenso
				  ,cd_classificazione_cori
				  --,aliquota
				  ,imponibile desc
				  )
	loop

		if aTmpCdAnag is null then -- primo anagrafico in processo
		   aTmpCdTrattamento  := aDett.cd_trattamento;
		   aTmpCdAnag 	 	    := aDett.cd_anag;
		   aTmpCdAnagPignorato:= aDett.cd_anag_pignorato;
		   aTmpQuadro 	      := aDett.cd_quadro;
		   aTmpDsQuadro       := aDett.ds_quadro;
		   aTmpTiCompenso 	  := aDett.cd_ti_compenso;
		   aTmpDsTiCompenso   := aDett.ds_ti_compenso;
		   aTmpTiRitenuta     := aDett.ti_ritenuta;

		   aTmpPkCompenso	    := aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;

		   aImLordo 		    := aDett.im_lordo_percipiente;
		   aImNonSoggRit	  := aDett.im_no_fiscale + aDett.quota_esente;
		   aImNonSoggCori	  := aDett.quota_esente;
		   aImNonSoggInps   := aDett.quota_esente_inps;
		   aImNetto 		    := aDett.im_netto_percipiente;

		   if aDett.cd_classificazione_cori = 'FI' then
		   	  aImponibileFi	  := aDett.imponibile;
			    aImponibilePr	  := 0;
			    aTmpAliquota 	  := aDett.aliquota;  -- valorizzo l'ultima aliquota
		   elsif aDett.cd_classificazione_cori = 'PR' then
		   	  aImponibilePr   := aDett.imponibile;
			    aImponibileFi	  := 0;
		   else
		   	  aImponibilePr   := 0;
			    aImponibileFi	  := 0;
		   end if;

		   i := i + 1;
		           insert into VPG_CERTIFICAZIONE_770  (ID,
							                              CHIAVE,
							                              TIPO,
							                              SEQUENZA,
							                              ESERCIZIO,
							                              TI_MODELLO,
							                              CD_ANAG,
							                              NOTA,
							                              CD_QUADRO,
							                              DS_QUADRO,
							                              TI_RITENUTA,
							                              NOME,
							                              COGNOME,
							                              RAGIONE_SOCIALE,
							                              VIA_NUM_FISCALE,
							                              CAP_COMUNE_FISCALE,
							                              FRAZIONE_FISCALE,
							                              DS_COMUNE_FISCALE,
							                              DS_PROVINCIA_FISCALE,
							                              CD_PROVINCIA_FISCALE,
							                              DS_NAZIONE_FISCALE,
							                              DT_NASCITA,
							                              DS_COMUNE_NASCITA,
							                              DS_PROVINCIA_NASCITA,
							                              CD_PROVINCIA_NASCITA,
							                              DS_NAZIONE_NASCITA,
							                              CODICE_FISCALE,
							                              PARTITA_IVA,
							                              TI_ENTITA,
                                            TI_SESSO,
                                            ID_FISCALE_ESTERO,
                                            CD_NAZIONE_770,
                                            CF_PI_PIGNORATO)
		   		             (Select distinct
		   		               aId
		   		              ,to_char(aDett.cd_anag)
				               	,'A'
				               	,i
				               	,aEs
				               	,aTiModello
				               	,aDett.cd_anag
				               	,aNota
				               	,aDett.cd_quadro
				               	,aDett.ds_quadro
				               	,aDett.ti_ritenuta
				               	,vat.nome
				               	,vat.cognome
				               	,vat.ragione_sociale
				               	,vat.via_fiscale||' '||vat.num_civico_fiscale
				               	,vat.cap_comune_fiscale
				               	,vat.frazione_fiscale
				               	,vat.ds_comune_fiscale
				               	,vat.ds_provincia_fiscale
				               	,vat.cd_provincia_fiscale
				               	,naz.ds_nazione
				               	,vat.dt_nascita
				               	,vat.ds_comune_nascita
				               	,vat.ds_provincia_nascita
				               	,vat.cd_provincia_nascita
				               	,naz2.ds_nazione
				               	,vat.codice_fiscale
				               	,vat.partita_iva
				               	,vat.ti_entita
				               	,vat.ti_sesso
				               	,vat.id_fiscale_estero
                        ,naz.cd_nazione_770
                        ,NULL
		   		             from v_anagrafico_terzo vat
		   	                	 ,nazione naz
			   	                 ,comune com
			                 	   ,nazione naz2
		   		             where vat.cd_anag	= aDett.cd_anag
		     	             	and naz.pg_nazione	= vat.pg_nazione_fiscale
			 	                and com.pg_comune (+) 	= vat.pg_comune_nascita
			 	                and naz2.pg_nazione (+) = com.pg_nazione);
		end if; -- fine inserimento primo anagrafico

		if aDett.cd_anag <> aTmpCdAnag OR
		    (aTmpTiCompenso != null and aDett.cd_ti_compenso != null and  aTmpTiCompenso != aDett.cd_ti_compenso) then -- il primo anag non passa di qui
	   	   i := i + 1;
		     -- scarico i dettagli dell'anagrafico precedente
	   	   insert into VPG_CERTIFICAZIONE_770  (ID,
							                                CHIAVE,
							                                TIPO,
							                                SEQUENZA,
							                                ESERCIZIO,
							                                TI_MODELLO,
							                                CD_ANAG,
							                                NOTA,
							                                CD_QUADRO,
							                                DS_QUADRO,
							                                TI_RITENUTA,
							                                CD_TI_COMPENSO,
							                                DS_TI_COMPENSO,
							                                IM_LORDO,
							                                IM_NON_SOGG_RIT,
							                                IMPONIBILE_FI,
							                                IMPONIBILE_PR,
							                                ALIQUOTA,
							                                IM_RITENUTE,
							                                IM_NON_SOGG_CORI,
							                                IM_CONTRIBUTI,
							                                IM_CONTRIBUTI_ENTE,
							                                IM_NETTO,
							                                IM_NON_SOGG_INPS,
							                                CD_TRATTAMENTO,
							                                CF_PI_PIGNORATO)
		   	                  values (aId
		   	                         ,to_char(aTmpCdAnag)
				                         ,'B'
				                         ,i
				                         ,aEs
				                         ,aTiModello
				                         ,aTmpCdAnag
				                         ,aNota
				                         ,aTmpQuadro
				                         ,aTmpDsQuadro
				                         ,aTmpTiRitenuta
				                         ,aTmpTiCompenso
				                         ,aTmpDsTiCompenso
				                         ,aImLordo
				                         ,aImNonSoggRit
				                         ,aImponibileFi
				                         ,aImponibilePr
				                         ,aTmpAliquota
				                         ,aImRit
				                         ,aImNonSoggCori
				                         ,aImCori
				                         ,aImCoriEnte
				                         ,aImNetto
				                         ,aImNonSoggInps
				                         ,aTmpCdTrattamento
				                         ,(select nvl(vatp.codice_fiscale,vatp.partita_iva)
		   	                   From v_anagrafico_terzo vatp
		   	                   Where 
		                       	  vatp.cd_anag (+)	=aTmpCdAnagPignorato));

		    -- reset delle variabili
        aTmpCdTrattamento :=aDett.cd_trattamento;
		    aTmpCdAnag        := aDett.cd_anag;
		    aTmpCdAnagPignorato:= aDett.cd_anag_pignorato;
		    aTmpQuadro 	      := aDett.cd_quadro;
		    aTmpDsQuadro      := aDett.ds_quadro;
		    aTmpTiCompenso 	  := aDett.cd_ti_compenso;
		    aTmpDsTiCompenso := aDett.ds_ti_compenso;
		    aTmpTiRitenuta    := aDett.ti_ritenuta;
		    --aTmpAliquota 	:= aDett.aliquota;

		    aTmpPKCompenso	:= aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
		    aImLordo 		:= aDett.im_lordo_percipiente; --:= 0;
		    aImNonSoggRit	:= aDett.im_no_fiscale + aDett.quota_esente; --:= 0;
		    aImNonSoggCori	:= aDett.quota_esente; --:= 0;
		    aImNonSoggInps       := aDett.quota_esente_inps;
		    aImRit		:= 0;
		    aImCori		:= 0;
		    aImCoriEnte		:= 0;
		    aImNetto 		:= aDett.im_netto_percipiente; --:= 0;
		    if aDett.cd_classificazione_cori = 'FI' then
		    	  aImponibileFi	  := aDett.imponibile;
			      aImponibilePr	  := 0;
			      aTmpAliquota 	  := aDett.aliquota;  -- valorizzo l'ultima aliquota
		    elsif aDett.cd_classificazione_cori = 'PR' then
		    	  aImponibilePr   := aDett.imponibile;
			      aImponibileFi	  := 0;
		    else
		    	  aImponibilePr   := 0;
			      aImponibileFi	  := 0;
		    end if;

		    -- inserimento nuova anagrafica
		    i := i + 1;
		        insert into VPG_CERTIFICAZIONE_770  (ID,
							                               CHIAVE,
							                               TIPO,
							                               SEQUENZA,
							                               ESERCIZIO,
							                               TI_MODELLO,
							                               CD_ANAG,
							                               NOTA,
							                               CD_QUADRO,
							                               DS_QUADRO,
							                               TI_RITENUTA,
							                               NOME,
							                               COGNOME,
							                               RAGIONE_SOCIALE,
							                               VIA_NUM_FISCALE,
							                               CAP_COMUNE_FISCALE,
							                               FRAZIONE_FISCALE,
							                               DS_COMUNE_FISCALE,
							                               DS_PROVINCIA_FISCALE,
							                               CD_PROVINCIA_FISCALE,
							                               DS_NAZIONE_FISCALE,
							                               DT_NASCITA,
							                               DS_COMUNE_NASCITA,
							                               DS_PROVINCIA_NASCITA,
							                               CD_PROVINCIA_NASCITA,
							                               DS_NAZIONE_NASCITA,
							                               CODICE_FISCALE,
							                               PARTITA_IVA,
							                               TI_ENTITA,
                                             TI_SESSO,
                                             ID_FISCALE_ESTERO,
                                             CD_NAZIONE_770,
                                             CF_PI_PIGNORATO)
		   	                  (Select distinct
		   	                  	 aId
		   	                  	,to_char(aDett.cd_anag)
				                    ,'A'
				                    ,i
				                    ,aEs
				                    ,aTiModello
				                    ,aDett.cd_anag
				                    ,aNota
				                    ,aDett.cd_quadro
				               	    ,aDett.ds_quadro
				               	    ,aDett.ti_ritenuta
				                    ,vat.nome
				                    ,vat.cognome
				                    ,vat.ragione_sociale
				                    ,vat.via_fiscale||' '||vat.num_civico_fiscale
				                    ,vat.cap_comune_fiscale
				                    ,vat.frazione_fiscale
				                    ,vat.ds_comune_fiscale
				                    ,vat.ds_provincia_fiscale
				                    ,vat.cd_provincia_fiscale
				                    ,naz.ds_nazione
				                    ,vat.dt_nascita
				                    ,vat.ds_comune_nascita
				                    ,vat.ds_provincia_nascita
				                    ,vat.cd_provincia_nascita
				                    ,naz2.ds_nazione
				                    ,vat.codice_fiscale
				                    ,vat.partita_iva
				                    ,vat.ti_entita
				                    ,vat.ti_sesso
				                    ,vat.id_fiscale_estero
                            ,naz.cd_nazione_770
                            ,NULL
		   	                   From v_anagrafico_terzo vat
		   	                       ,nazione naz
			                         ,comune com
			                         ,nazione naz2
		   	                   Where vat.cd_anag 	= aDett.cd_anag
		                       	 And naz.pg_nazione 	= vat.pg_nazione_fiscale
			                       And com.pg_comune (+) 	= vat.pg_comune_nascita
			                       And naz2.pg_nazione (+)= com.pg_nazione);
		end if; -- fine inserimento testata nuova anagrafica

    if aDett.cd_quadro = aTmpQuadro and aDett.cd_ti_compenso = aTmpTiCompenso  -- lo stesso quadro e lo stesso tipo compenso (mod.pag)  >>> aggiorno
       and (aDett.cd_anag_pignorato is null or (aDett.cd_anag_pignorato is not null and aDett.cd_anag_pignorato = aTmpCdAnagPignorato)) then -- anche se cambia il terzo pignorato devo inserire una nuova riga
		      If aTmpPkCompenso <> aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso Then
		      	    aImLordo 	    := aImLordo + aDett.im_lordo_percipiente;
		      	    aImNonSoggRit := aImNonSoggRit + aDett.im_no_fiscale + aDett.quota_esente;
		      	    aImNonSoggCori:= aImNonSoggCori + aDett.quota_esente;
		      	    aImNonSoggInps:= aImNonSoggInps + aDett.quota_esente_inps;
		      	    aImNetto	    := aImNetto + aDett.im_netto_percipiente;
		      End If;
		      if aDett.cd_classificazione_cori = 'FI' then
		      	    aTmpAliquota 	  := aDett.aliquota;  -- valorizzo l'ultima aliquota
		      	    aImRit := aImRit + aDett.ammontare;
		          if aTmpPkCompenso <> aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso then
		      	      aImponibileFi := aImponibileFi + aDett.imponibile;
		             aTmpPkCompenso := aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
		          end if;
		      elsif aDett.cd_classificazione_cori = 'PR' Then
		           If aDett.ti_ente_percipiente = 'P' Then
		      	        aImCori  := aImCori + aDett.ammontare;
		      	    Elsif aDett.ti_ente_percipiente = 'E' Then
		      	        aImCoriEnte  := aImCoriEnte + aDett.ammontare;
		      	    End If;
		          --aTmpAliquota := aDett.aliquota;
		          if aTmpPkCompenso <> aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso then
		      	      aImponibilePr := aImponibilePr + aDett.imponibile;
		             aTmpPkCompenso := aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
		          end if;
		      else -- no PR, no FI
		         	 --aTmpAliquota := aDett.aliquota;
		           aImRit := 0;
		           aImCori := 0;
		           aImCoriEnte := 0;
		           aImponibileFi := 0;
		           aImponibilePr := 0;
		           aTmpPkCompenso := aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
		      end if;
		else -- cambiato il quadro o il tipo compenso o il terzo pigorato (solo quadro SY)>>> inserisco
	        i := i + 1;
	        insert into VPG_CERTIFICAZIONE_770  (ID,
		      	                                CHIAVE,
		      	                                TIPO,
		      	                                SEQUENZA,
		      	                                ESERCIZIO,
		      	                                TI_MODELLO,
		      	                                CD_ANAG,
		      	                                NOTA,
		      	                                CD_QUADRO,
					                                  DS_QUADRO,
					                                  TI_RITENUTA,
		      	                                CD_TI_COMPENSO,
		      	                                DS_TI_COMPENSO,
		      	                                IM_LORDO,
		      	                                IM_NON_SOGG_RIT,
		      	                                IMPONIBILE_FI,
		      	                                IMPONIBILE_PR,
		      	                                ALIQUOTA,
		      	                                IM_RITENUTE,
		      	                                IM_NON_SOGG_CORI,
		      	                                IM_CONTRIBUTI,
		      	                                IM_CONTRIBUTI_ENTE,
		      	                                IM_NETTO,
		      	                                IM_NON_SOGG_INPS,
		      	                                CD_TRATTAMENTO,
																						CF_PI_PIGNORATO)
		                    values ( aId
		                         ,to_char(aDett.cd_anag)
		                         ,'B'
		                         ,i
		                         ,aEs
		                         ,aTiModello
		                         ,aDett.cd_anag
		                         ,aNota
		                         ,aTmpQuadro
				                     ,aTmpDsQuadro
				                     ,aTmpTiRitenuta
		                         ,aTmpTiCompenso
		                         ,aTmpDsTiCompenso
		                         ,aImLordo
		                         ,aImNonSoggRit
		                         ,aImponibileFi
		                         ,aImponibilePr
		                         ,aTmpAliquota
		                         ,aImRit
		                         ,aImNonSoggCori
		                         ,aImCori
		                         ,aImCoriEnte
		                         ,aImNetto
		                         ,aImNonSoggInps
		                         ,aTmpCdTrattamento
		                         ,(select nvl(vatp.codice_fiscale,vatp.partita_iva)
		   	                   From v_anagrafico_terzo vatp
		   	                   Where 
		                       	  vatp.cd_anag (+)	=aTmpCdAnagPignorato));
		      -- reset variabili
		      aTmpCdTrattamento := aDett.cd_trattamento;
		      aTmpCdAnag 		   := aDett.cd_anag;
		      aTmpCdAnagPignorato:= aDett.cd_anag_pignorato;
		      aTmpQuadro 	     := aDett.cd_quadro;
		      aTmpDsQuadro      := aDett.ds_quadro;
		      aTmpTiCompenso 	 := aDett.cd_ti_compenso;
		      aTmpDsTiCompenso  := aDett.ds_ti_compenso;
		      aTmpTiRitenuta    := aDett.ti_ritenuta;
		      aTmpPkCompenso	:= aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
		      aImLordo 		:= aDett.im_lordo_percipiente;
		      aImNonSoggRit	:= aDett.im_no_fiscale + aDett.quota_esente;
		      aImNonSoggCori	:= aDett.quota_esente;
		      aImNonSoggInps	:= aDett.quota_esente_inps;
		      aImNetto 		:= aDett.im_netto_percipiente;
		      if aDett.cd_classificazione_cori = 'FI' then
		      	  aImponibileFi	 := aDett.imponibile;
		          aImRit	       := aDett.ammontare;
		          aTmpAliquota 	  := aDett.aliquota;   -- valorizzo l'ultima aliquota
		          aImCori	       := 0;
		          aImCoriEnte	   := 0;
		          aImponibilePr	 := 0;
		      elsif aDett.cd_classificazione_cori = 'PR' then
		      	  aImponibileFi	  := 0;
		          aImRit	  := 0;
		          --aTmpAliquota 	  := aDett.aliquota;
		          If aDett.ti_ente_percipiente = 'P' Then
		              aImCori	  := aDett.ammontare;
		          Elsif aDett.ti_ente_percipiente = 'E' Then
		              aImCoriEnte  := aDett.ammontare;
		          End If;
		          aImponibilePr	  := aDett.imponibile;
		      else
		      	  aImponibileFi	:= 0;
		          aImRit	      := 0;
		          --aTmpAliquota 	  := 0;
		          aImCori	      := 0;
		          aImCoriEnte	  := 0;
		          aImponibilePr	:= 0;
		      end if;
     end if;     -- FINE DI if aDett.cd_quadro = aTmpQuadro and aDett.cd_ti_compenso = aTmpTiCompenso
	end loop;

	if i <> 0 then -- ? stato inserita la testata dell'anagrafica
	 	i := i + 1;
	    -- scarico gli ultimi dettagli dell'ultimo anagrafico
	 	insert into VPG_CERTIFICAZIONE_770  (ID,
						     CHIAVE,
						     TIPO,
						     SEQUENZA,
						     ESERCIZIO,
						     TI_MODELLO,
						     CD_ANAG,
						     NOTA,
						     CD_QUADRO,
					       DS_QUADRO,
					       TI_RITENUTA,
						     CD_TI_COMPENSO,
						     DS_TI_COMPENSO,
						     IM_LORDO,
						     IM_NON_SOGG_RIT,
						     IMPONIBILE_FI,
						     IMPONIBILE_PR,
						     ALIQUOTA,
						     IM_RITENUTE,
						     IM_NON_SOGG_CORI,
						     IM_CONTRIBUTI,
						     IM_CONTRIBUTI_ENTE,
						     IM_NETTO,
						     IM_NON_SOGG_INPS,
						     CD_TRATTAMENTO,
						     CF_PI_PIGNORATO)
	    		values   (aId
		  		 ,to_char(aTmpCdAnag)
				   ,'B'
				   ,i
				   ,aEs
				   ,aTiModello
				   ,aTmpCdAnag
				   ,aNota
				   ,aTmpQuadro
				   ,aTmpDsQuadro
				   ,aTmpTiRitenuta
				   ,aTmpTiCompenso
				   ,aTmpDsTiCompenso
				   ,aImLordo
				   ,aImNonSoggRit
				   ,aImponibileFi
				   ,aImponibilePr
				   ,aTmpAliquota
				   ,aImRit
				   ,aImNonSoggCori
				   ,aImCori
				   ,aImCoriEnte
				   ,aImNetto
				   ,aImNonSoggInps
				   ,aTmpCdTrattamento
				   ,(select nvl(vatp.codice_fiscale,vatp.partita_iva)
		   	                   From v_anagrafico_terzo vatp
		   	                   Where 
		                       	  vatp.cd_anag (+)	= aTmpCdAnagPignorato));
	end if;

	open tc for
    select * from VPG_CERTIFICAZIONE_770 where id = aId;
    Close tc; -- Remmarlo se lanciato da Crystal Report

end;
/


