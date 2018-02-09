CREATE OR REPLACE PROCEDURE SPG_CERTIFICAZIONE
--
-- Date: 02/04/2004
-- Version: 1.4
--
-- Per stampa certificazioni dei compensi
--
--
-- History:
--
-- Date: 26/01/2004
-- Version: 1.0
-- Creazione
--
-- Date: 29/01/2004
-- Version: 1.1
-- Modifiche per gestione separata cori PR ed FI
--
-- Date: 03/02/2004
-- Version: 1.2
-- Aggiunta colonna della quota esente del compenso
--
-- Date: 12/02/2004
-- Version: 1.3
-- Corretto reset variabili per raggruppamento
--
-- Date: 02/04/2004
-- Version: 1.4
-- Corretta join per dati di nascita (errore n. 809)
--
-- Date: 01/02/2005
-- Version: 1.5
-- Modifiche per l'aggiunta degli OCCA
-- Modificato il criterio di rottura dei dati (prima era per trattamento e aliquota, ora solo per trattamento)
--
-- Body:
--
(
 aEs number,
 aTiCertif varchar2,
 aCdAnag varchar2, -- valorizzato a '%' per stampa di tutte le anagrafiche
 aNota varchar2) is
aId number;
i number := 0;
aTmpCdAnag number;
aTmpTiCompenso varchar2(5) := null;
aTmpDsTiCompenso varchar2(300);
aTmpPkCompenso varchar2(100);
aTmpAliquota number(10,6);
aImponibileFi number(15,2) :=0 ;
aImponibilePr number(15,2) :=0 ;
aImLordo number(15,2) :=0 ;
aImNonSoggRit number(15,2) :=0 ;
aImNonSoggCori number(15,2) := 0;
aImRit number(15,2) :=0 ;
aImCori number(15,2) :=0 ;
aImNetto number(15,2) := 0;
begin
	select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;

	for aDett in (select * from
				 (select   v.cd_anag
 		 				  ,v.cd_cds
 						  ,v.cd_unita_organizzativa
 						  ,v.esercizio
 						  ,v.pg_compenso
						  ,v.ds_certificazione
						  ,v.cd_ti_compenso
						  ,v.ds_ti_compenso
						  ,v.im_lordo_percipiente
						  ,v.im_no_fiscale
						  ,v.quota_esente
						  ,v.im_netto_percipiente
						  ,tcr.cd_classificazione_cori
						  ,cr.imponibile
						  ,cr.aliquota
						  ,cr.ammontare
				  from   v_compenso_certificazione v
						,contributo_ritenuta cr
						,tipo_contributo_ritenuta tcr
				  where   to_char(v.cd_anag) 	  	  like aCdAnag
					  and v.TI_CERTIFICAZIONE 	  	  = aTiCertif
					  and v.esercizio_mandato 		  = aEs
					  and cr.cd_cds 				  = v.cd_cds
					  and cr.cd_unita_organizzativa   = v.cd_unita_organizzativa
					  and cr.esercizio 				  = v.esercizio
					  and cr.pg_compenso 			  = v.pg_compenso
					  and cr.ti_ente_percipiente 	  = 'P'
					  and tcr.cd_contributo_ritenuta  = cr.cd_contributo_ritenuta
					  and tcr.dt_ini_validita 		  = cr.dt_ini_validita
					  and tcr.cd_classificazione_cori in ('FI','PR')
				  union all
				  select   v.cd_anag
 		 				  ,v.cd_cds cd_cds
 						  ,v.cd_unita_organizzativa cd_unita_organizzativa
 						  ,v.esercizio esercizio
 						  ,v.pg_compenso pg_compenso
						  ,v.ds_certificazione
						  ,v.cd_ti_compenso
						  ,v.ds_ti_compenso
						  ,v.im_lordo_percipiente
						  ,v.im_no_fiscale
						  ,v.quota_esente
						  ,v.im_netto_percipiente
						  ,null cd_classificazione_cori
						  ,0 imponibile
						  ,0 aliquota
						  ,0 ammontare
				  from   v_compenso_certificazione v
				  where   to_char(v.cd_anag)  	  	  like aCdAnag
					  and v.TI_CERTIFICAZIONE 	  	  = aTiCertif
					  and v.esercizio_mandato	  	  = aEs
					  and not exists (select 1
					  	  	  		  from contributo_ritenuta cr
									  	  ,tipo_contributo_ritenuta tcr
									  where cr.cd_cds 				  = v.cd_cds
					  				    and cr.cd_unita_organizzativa = v.cd_unita_organizzativa
					  					and cr.esercizio			  = v.esercizio
					  					and cr.pg_compenso 			  = v.pg_compenso
					  					and cr.ti_ente_percipiente 	  = 'P'
					  					and tcr.cd_contributo_ritenuta= cr.cd_contributo_ritenuta
					  					and tcr.dt_ini_validita       = cr.dt_ini_validita
					  					and tcr.cd_classificazione_cori in ('FI','PR'))
				  )
				  order by cd_anag
					  ,cd_ti_compenso
					  ,cd_cds
					  ,cd_unita_organizzativa
					  ,pg_compenso
					  ,cd_classificazione_cori
					  ,aliquota
					  ,imponibile Desc)
	loop
		if aTmpCdAnag is null then -- primo anagrafico in processo
		   --l'aliquota e gli imponibili vengono calcolati dopo sui singoli dettagli
		   --l'aliquota viene presa solo quella FI
		   aTmpCdAnag 	 	:= aDett.cd_anag;
		   aTmpTiCompenso 	:= aDett.cd_ti_compenso;
		   aTmpDsTiCompenso   	:= aDett.ds_ti_compenso;
		   aTmpAliquota 	:= 0;
		   aTmpPkCompenso	:= aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
		   aImponibilePr   	:= 0;
		   aImponibileFi   	:= 0;
		   aImLordo 	 	:= aDett.im_lordo_percipiente;
		   aImNetto	 	:= aDett.im_netto_percipiente;

		   i := i + 1;
		   insert into VPG_CERTIFICAZIONE  (ID,
						CHIAVE,
						TIPO,
						SEQUENZA,
						ESERCIZIO,
						TI_CERTIFICAZIONE,
						CD_ANAG,
						NOTA,
						DS_CERTIFICAZIONE,
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
						PARTITA_IVA)
		   	(select distinct
		   		   aId
		   		  ,to_char(aDett.cd_anag)
				  ,'A'
				  ,i
				  ,aEs
				  ,aTiCertif
				  ,aDett.cd_anag
				  ,aNota
				  ,aDett.ds_certificazione
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
		   	from v_anagrafico_terzo vat
		   		,nazione naz
			   	,comune com
			   	,nazione naz2
		   	where vat.cd_anag	     = aDett.cd_anag
		     	  and naz.pg_nazione		 = vat.pg_nazione_fiscale
			   and com.pg_comune 	 (+) = vat.pg_comune_nascita
			   and naz2.pg_nazione (+) = com.pg_nazione);
		end if; -- fine inserimento primo anagrafico

		if aDett.cd_anag <> aTmpCdAnag then -- il primo anag non passa di qui
	   	   i := i + 1;
		   -- scarico i dettagli dell'anagrafico precedente
	   	   insert into VPG_CERTIFICAZIONE  (ID,
						CHIAVE,
						TIPO,
						SEQUENZA,
						ESERCIZIO,
						TI_CERTIFICAZIONE,
						CD_ANAG,
						NOTA,
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
						IM_NETTO)
		   	values (aId
		   		 ,to_char(aTmpCdAnag)
				 ,'B'
				 ,i
				 ,aEs
				 ,aTiCertif
				 ,aTmpCdAnag
				 ,aNota
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
				 ,aImNetto);

		   -- reset delle variabili
		   aTmpCdAnag       	:= aDett.cd_anag;
		   aTmpTiCompenso 	:= aDett.cd_ti_compenso;
		   aTmpDsTiCompenso 	:= aDett.ds_ti_compenso;
		   aTmpPKCompenso	:= aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
		   aImNonSoggRit	:= 0;
		   aImNonSoggCori	:= 0;
		   aImRit		:= 0;
		   aImCori		:= 0;
		   aTmpAliquota 	:= 0;
		   --gli imponibili vengono inizializzati dopo
		   aImponibilePr   	:= 0;
		   aImponibileFi   	:= 0;
		   aImLordo 	 	:= aDett.im_lordo_percipiente;
		   aImNetto	 	:= aDett.im_netto_percipiente;

		   -- inserimento nuova anagrafica
		   i := i + 1;
		   insert into VPG_CERTIFICAZIONE  (ID,
						CHIAVE,
						TIPO,
						SEQUENZA,
						ESERCIZIO,
						TI_CERTIFICAZIONE,
						CD_ANAG,
						NOTA,
						DS_CERTIFICAZIONE,
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
						PARTITA_IVA)
		   	(select distinct
		   		   aId
		   		  ,to_char(aDett.cd_anag)
				  ,'A'
				  ,i
				  ,aEs
				  ,aTiCertif
				  ,aDett.cd_anag
				  ,aNota
				  ,aDett.ds_certificazione
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
		   	from v_anagrafico_terzo vat
		   	   ,nazione naz
			   ,comune com
			   ,nazione naz2
		   	where vat.cd_anag 	     = aDett.cd_anag
		          and naz.pg_nazione 	 = vat.pg_nazione_fiscale
			  and com.pg_comune 	 (+) = vat.pg_comune_nascita
			  and naz2.pg_nazione (+) = com.pg_nazione);
		end if; -- fine inserimento testata nuova anagrafica

		if aDett.cd_ti_compenso = aTmpTiCompenso then
		   If aTmpPkCompenso <> aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso then
			   aImLordo 	 := aImLordo + aDett.im_lordo_percipiente;
			   --aImNonSoggRit := aImNonSoggRit + aDett.im_no_fiscale + aDett.quota_esente;
			   --aImNonSoggCori:= aImNonSoggCori + aDett.quota_esente;
			   aImNetto	 := aImNetto + aDett.im_netto_percipiente;
			   if aDett.cd_classificazione_cori = 'FI' Then
			       If Nvl(aDett.aliquota,0) != 0  Then
		                  aTmpAliquota 	  := aDett.aliquota;
		         End If;
		         aImNonSoggRit := aImNonSoggRit + aDett.im_no_fiscale + aDett.quota_esente;
			   	   aImRit  	:= aImRit + aDett.ammontare;
				     aImponibileFi := aImponibileFi + aDett.imponibile;
			   elsif aDett.cd_classificazione_cori = 'PR' then
			       aImNonSoggCori:= aImNonSoggCori + aDett.quota_esente;
			   	   aImCori  := aImCori + aDett.ammontare;
				     aImponibilePr := aImponibilePr + aDett.imponibile;
			   end if;
			   aTmpPkCompenso := aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
		   Else  --il compenso e' sempre lo stesso
			   --aImNonSoggRit := aImNonSoggRit + aDett.im_no_fiscale + aDett.quota_esente;
			   --aImNonSoggCori:= aImNonSoggCori + aDett.quota_esente;
			   if aDett.cd_classificazione_cori = 'FI' Then
			       If Nvl(aDett.aliquota,0) != 0 Then
		                 aTmpAliquota 	  := aDett.aliquota;
		         End If;
		         aImNonSoggRit := aImNonSoggRit + aDett.im_no_fiscale + aDett.quota_esente;
			   	   aImRit  	:= aImRit + aDett.ammontare;
			   	   aImponibileFi := aImponibileFi + aDett.imponibile;
			   elsif aDett.cd_classificazione_cori = 'PR' then
			       aImNonSoggCori:= aImNonSoggCori + aDett.quota_esente;
			   	   aImCori  := aImCori + aDett.ammontare;
				     aImponibilePr := aImponibilePr + aDett.imponibile;
			   end if;
		   End If;
		else -- cambiato tipo compenso >>> inserisco
	   	   i := i + 1;
	   	   insert into VPG_CERTIFICAZIONE  (ID,
						CHIAVE,
						TIPO,
						SEQUENZA,
						ESERCIZIO,
						TI_CERTIFICAZIONE,
						CD_ANAG,
						NOTA,
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
						IM_NETTO)
		   	values (aId
		   		 ,to_char(aDett.cd_anag)
				 ,'B'
				 ,i
				 ,aEs
				 ,aTiCertif
				 ,aDett.cd_anag
				 ,aNota
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
				 ,aImNetto);
			   -- reset variabili con i valori del nuovo compenso
			   aTmpCdAnag 		:= aDett.cd_anag;
			   aTmpTiCompenso 	:= aDett.cd_ti_compenso;
			   aTmpDsTiCompenso := aDett.ds_ti_compenso;
			   aTmpPkCompenso	:= aDett.cd_cds||aDett.cd_unita_organizzativa||aDett.esercizio||aDett.pg_compenso;
			   aImLordo 		:= aDett.im_lordo_percipiente;
			   aImNonSoggRit	:= aDett.im_no_fiscale + aDett.quota_esente;
			   aImNonSoggCori	:= aDett.quota_esente;
			   aImNetto 		:= aDett.im_netto_percipiente;
			   aTmpAliquota         := 0;
			   if aDett.cd_classificazione_cori = 'FI' then
			   	  aImponibileFi	  	:= aDett.imponibile;
				  aImRit		:= aDett.ammontare;
				  If Nvl(aDett.aliquota,0) != 0 Then
		                         aTmpAliquota 	  := aDett.aliquota;
		                  End If;
				  aImCori		:= 0;
				  aImponibilePr	  	:= 0;
			   elsif aDett.cd_classificazione_cori = 'PR' then
			   	  aImponibileFi	  	:= 0;
				  aImRit		:= 0;
				  aImCori		:= aDett.ammontare;
				  aImponibilePr	  	:= aDett.imponibile;
			   else
			   	  aImponibileFi	  	:= 0;
				  aImRit		:= 0;
				  aImCori		:= 0;
				  aImponibilePr	  	:= 0;
			   end if;
		end if;
	end loop;

	if i <> 0 then -- ? stato inserita la testata dell'anagrafica
	 	i := i + 1;
	    -- scarico gli ultimi dettagli dell'ultimo anagrafico
	 	insert into VPG_CERTIFICAZIONE  (ID,
						CHIAVE,
						TIPO,
						SEQUENZA,
						ESERCIZIO,
						TI_CERTIFICAZIONE,
						CD_ANAG,
						NOTA,
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
						IM_NETTO)
	    		values   (aId
		  		 ,to_char(aTmpCdAnag)
				 ,'B'
				 ,i
				 ,aEs
				 ,aTiCertif
				 ,aTmpCdAnag
				 ,aNota
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
				 ,aImNetto);
	end if;
End;
/


