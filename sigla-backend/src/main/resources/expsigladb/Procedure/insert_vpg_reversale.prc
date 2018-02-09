CREATE OR REPLACE PROCEDURE INSERT_VPG_REVERSALE
--
-- Date: 17/05/2007
-- Version: 1.7
--
-- Stored procedure per inserimento record in VPG_REVERSALE
--
--
-- History:
--
-- Date: 26/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 07/03/2003
-- Version: 1.1
-- Corretto inserimento record di sospesi
--
-- Date: 20/03/2003
-- Version: 1.2
-- Filtro sui sospesi annullati
--
-- Date: 25/03/2003
-- Version: 1.3
-- Filtro sui riscontri
--
-- Date: 28/05/2003
-- Version: 1.4
-- Inserimento comune DS_COMUNE_SEDE terzo per record B
--
-- Date: 20/01/2004
-- Version: 1.5
-- Estrazione CIN dalla banca (richiesta n. 697)
--
-- Date: 19/07/2006
-- Version: 1.6
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 17/05/2007
-- Version: 1.7
-- Gestione Codici Siope:
-- aggiunti i campi CD_SIOPE,DS_SIOPE,IM_SIOPE
--
-- Date: 27/10/2010
-- Version: 1.8
-- Gestione CUP:
-- aggiornata la funzione per inserire il riepilogo CUP
--

-- Body:
--
(
 aCd_cds in varchar2,
 aEs in number,
 aPg in number,
 i in out number,
 aId in out number
) is
 aRev reversale%rowtype;
 aNum1 number := 0;
 aNum2 number := 0;
 aNum3 number := 0;
 aVar1 varchar2(300) := null;
 aVar2 varchar2(300) := null;
 cd_modPag rif_modalita_pagamento.CD_MODALITA_PAG%type:=null;

begin

	select * into aRev
	from reversale rev
	where rev.CD_CDS       = aCd_cds
	  and rev.ESERCIZIO    = aEs
	  and rev.PG_REVERSALE = aPg;

	-- inizio inserimento record A: testata

	select min(cd_terzo_uo),
		   min(PG_BANCA),
		   min(cd_terzo),
		   min(cd_modalita_pag)
	into aNum1,
		 aNum2,
		 aNum3,
		 cd_modPag
	from reversale_riga rriga
	where rriga.CD_CDS	     = aRev.CD_CDS
	  and rriga.ESERCIZIO    = aRev.ESERCIZIO
	  and rriga.PG_REVERSALE = aRev.PG_REVERSALE
	group by cd_cds, esercizio, pg_reversale;

	insert into VPG_REVERSALE  (ID,
								CHIAVE,
								SEQUENZA,
								DESCRIZIONE,
								CD_CDS,
								ESERCIZIO,
								PG_REVERSALE,
								TI_RECORD,
								DT_EMISSIONE,
								TI_REVERSALE,
								TI_COMPETENZA_RESIDUO,
								DS_CDS,
								CD_UO_ORIGINE,
								DS_UO_CDS,
								IM_REVERSALE,
								IM_REVERSALE_LETTERE,
								CD_TERZO,
								DENOMINAZIONE_SEDE,
								VIA_SEDE,
								CAP_COMUNE_SEDE,
								DS_COMUNE_SEDE,
								CD_PV_TERZO,
								CODICE_FISCALE,
								UTCR,
								DACR,
								STATO)
	select  aId,
		    'A:testata',
		    i,
			'Stampa RPT',
			aRev.CD_CDS,
			aRev.ESERCIZIO,
			aRev.PG_REVERSALE,
			'A',
			aRev.DT_EMISSIONE,
			aRev.TI_REVERSALE,
			aRev.TI_COMPETENZA_RESIDUO,
			uo1.DS_UNITA_ORGANIZZATIVA,
			aRev.CD_UO_ORIGINE,
			uo2.DS_UNITA_ORGANIZZATIVA,
			aRev.IM_REVERSALE,
			IBMUTL001.INLETTERE(aRev.IM_REVERSALE),
			aNum3, -- cd_terzo
			vat.DENOMINAZIONE_SEDE,
			vat.VIA_SEDE || ' ' || vat.NUMERO_CIVICO_SEDE,
			vat.CAP_COMUNE_SEDE,
			vat.DS_COMUNE_SEDE,
			vat.CD_PROVINCIA_SEDE,
			vat.CODICE_FISCALE,
			aRev.UTCR,
			aRev.DACR,
			aRev.STATO
	from unita_organizzativa uo1
		,unita_organizzativa uo2
		,v_anagrafico_terzo vat
	where uo1.CD_UNITA_ORGANIZZATIVA = aRev.CD_CDS
	  and uo2.CD_UNITA_ORGANIZZATIVA = aRev.CD_UO_ORIGINE
	  and vat.CD_TERZO				 = aNum3;

 begin
  if (cd_modPag is not null and cd_modPag='BI') then
  	 update VPG_REVERSALE vpg
		 set (CAB,
			  ABI,
			  NUMERO_CONTO,
			  CIN,
			  INTESTAZIONE,
			  IBAN)
		 = (select ban.CAB,
		   		   ban.ABI,
				   ban.NUMERO_CONTO,
				   nvl(ban.CIN,' '),
				   ban.INTESTAZIONE,
				   ban.codice_iban
		    from banca ban
			where ban.CD_TERZO = aNum1
	  		  and ban.PG_BANCA = aNum2)
		 where vpg.CD_CDS 		= aRev.CD_CDS
		   and vpg.ESERCIZIO 	= aRev.ESERCIZIO
		   and vpg.PG_REVERSALE = aRev.PG_REVERSALE
		   and vpg.TI_RECORD	= 'A'
		   and vpg.SEQUENZA		= i;
  else

		 update VPG_REVERSALE vpg
		 set (CAB,
			  ABI,
			  NUMERO_CONTO,
			  CIN,
			  INTESTAZIONE,
			  IBAN)
		 = (select ban.CAB,
		   		   ban.ABI,
				   ban.NUMERO_CONTO,
				   nvl(ban.CIN,' '),
				   ban.INTESTAZIONE,
				   ban.codice_iban
		    from banca ban,parametri_cnr
			where
					parametri_cnr.esercizio =aRev.esercizio and
					(( ban.PG_BANCA = aNum2 and
	  		  parametri_cnr.fl_tesoreria_unica ='N' ) or
         (parametri_cnr.fl_tesoreria_unica ='Y' and
         exists(select 1 from configurazione_cnr where
         (configurazione_cnr.esercizio =0 or
         configurazione_cnr.esercizio =aRev.esercizio) and
         cd_chiave_primaria ='CONTO_CORRENTE_SPECIALE' and
         cd_chiave_secondaria='ENTE' and
         ban.abi =val01 and
         ban.cab =val02 and
         ban.numero_conto like '%'||val03 )))
	 and ban.FL_CANCELLATO          = 'N'
	 and ban.CD_TERZO				 = aNum1 )
		 where vpg.CD_CDS 		= aRev.CD_CDS
		   and vpg.ESERCIZIO 	= aRev.ESERCIZIO
		   and vpg.PG_REVERSALE = aRev.PG_REVERSALE
		   and vpg.TI_RECORD	= 'A'
		   and vpg.SEQUENZA		= i;
		end if;
		 update VPG_REVERSALE vpg
		 set (DS_ABICAB,
			  VIA,
			  CAP,
			  DS_COMUNE,
			  CD_PROVINCIA)
		 = (select abi.DS_ABICAB,
				  abi.VIA,
				  abi.CAP,
				  com.DS_COMUNE,
				  com.CD_PROVINCIA
			from abicab abi
				,comune com
			where abi.ABI       = vpg.ABI
			  and abi.CAB	    = vpg.CAB
			  and com.PG_COMUNE = abi.PG_COMUNE)
		 where vpg.CD_CDS 		= aRev.CD_CDS
		   and vpg.ESERCIZIO 	= aRev.ESERCIZIO
		   and vpg.PG_REVERSALE = aRev.PG_REVERSALE
		   and vpg.TI_RECORD	= 'A'
		   and vpg.SEQUENZA		= i;
	exception when NO_DATA_FOUND then
			  null;
	end;

	-- fine inserimento record A: testata

	-- inizio inserimento record B: righe

	-- ciclo sulle righe dellal reversale
	for aRriga in (select * from reversale_riga rriga
	   	   		   where rriga.CD_CDS	  	= aRev.CD_CDS
	 				 and rriga.ESERCIZIO	= aRev.ESERCIZIO
	 				 and rriga.PG_REVERSALE = aRev.PG_REVERSALE) loop
	   -- inizio loop 2

	   	  i := i+1;

	   	  insert into VPG_REVERSALE (ID,
									CHIAVE,
									SEQUENZA,
									DESCRIZIONE,
									CD_CDS,
									ESERCIZIO,
									PG_REVERSALE,
									TI_RECORD,
									DT_EMISSIONE,
									TI_REVERSALE,
									TI_COMPETENZA_RESIDUO,
									CD_TERZO,
									DENOMINAZIONE_SEDE,
									VIA_SEDE,
									CAP_COMUNE_SEDE,
									DS_COMUNE_SEDE,
									CD_PV_TERZO,
									CODICE_FISCALE,
									CD_TIPO_DOCUMENTO_AMM,
									PG_DOC_AMM,
									IM_REVERSALE_RIGA,
									IM_REVERSALE_RIGA_LETTERE,
									DS_REVERSALE_RIGA,
									ESERCIZIO_ORI_ACCERTAMENTO,
									PG_ACCERTAMENTO,
									DS_ACCERTAMENTO,
									CD_VOCE,
									DS_VOCE,
									UTCR,
									DACR)
		  select aId,
		    	 'B:dettagli',
		    	 i,
				 'Stampa RPT',
				 aRev.CD_CDS,
				 aRev.ESERCIZIO,
				 aRev.PG_REVERSALE,
				 'B',
				 aRev.DT_EMISSIONE,
				 aRev.TI_REVERSALE,
				 aRev.TI_COMPETENZA_RESIDUO,
				 aRriga.CD_TERZO,
				 vat.DENOMINAZIONE_SEDE,
				 vat.VIA_SEDE || ' ' || vat.NUMERO_CIVICO_SEDE,
				 vat.CAP_COMUNE_SEDE,
				 vat.DS_COMUNE_SEDE,
				 vat.CD_PROVINCIA_SEDE,
				 vat.CODICE_FISCALE,
				 aRriga.CD_TIPO_DOCUMENTO_AMM,
				 aRriga.PG_DOC_AMM,
				 aRriga.IM_REVERSALE_RIGA,
				 IBMUTL001.INLETTERE(aRriga.IM_REVERSALE_RIGA),
				 aRriga.DS_REVERSALE_RIGA,
				 aRriga.ESERCIZIO_ORI_ACCERTAMENTO,
				 aRriga.PG_ACCERTAMENTO,
				 acc.DS_ACCERTAMENTO,
				 acc.cd_voce,
				 voce.DS_VOCE,
				 aRev.UTCR,
				 aRev.DACR
		  from v_anagrafico_terzo vat
		  	  ,accertamento acc
			  ,voce_f voce
		  where vat.CD_TERZO         = aRriga.CD_TERZO
		    and acc.CD_CDS 		     = aRriga.CD_CDS
	 			and acc.ESERCIZIO 	     = aRriga.ESERCIZIO
	 			and acc.ESERCIZIO_ORIGINALE  = aRriga.ESERCIZIO_ORI_ACCERTAMENTO
	 			and acc.PG_ACCERTAMENTO  = aRriga.PG_ACCERTAMENTO
			and voce.ESERCIZIO 	  	 = acc.ESERCIZIO
	 			and voce.TI_APPARTENENZA in ('C','D')
	 			and voce.TI_GESTIONE 	 = 'E'
	 			and voce.CD_VOCE 		 = acc.CD_VOCE;

	end loop; -- fine loop 2

	-- fine inserimento record B: righe

	-- inizio inserimento record C: sospesi

	for aSosp in (select * from sospeso_det_etr sosp
		 	  	  where sosp.CD_CDS_reversale       = aRev.cd_cds
	 		   	    and sosp.ESERCIZIO    = aRev.esercizio
	 		   		and sosp.PG_REVERSALE = aRev.pg_reversale
					and sosp.STATO		  <> 'A'
					and sosp.TI_SOSPESO_RISCONTRO = 'S') loop
	-- inizio loop 3

	   i := i+1;

	   insert into VPG_REVERSALE (ID,
								CHIAVE,
								SEQUENZA,
								DESCRIZIONE,
								CD_CDS,
								ESERCIZIO,
								PG_REVERSALE,
								TI_RECORD,
								DT_EMISSIONE,
								TI_REVERSALE,
								TI_COMPETENZA_RESIDUO,
								CD_SOSPESO,
								IM_ASSOCIATO,
								DT_REGISTRAZ_SOSPESO,
								UTCR,
								DACR)
	   select aId,
		    'C:sospesi',
		    i,
			'Stampa RPT',
			aRev.CD_CDS,
			aRev.ESERCIZIO,
			aRev.PG_REVERSALE,
			'C',
			aRev.DT_EMISSIONE,
			aRev.TI_REVERSALE,
			aRev.TI_COMPETENZA_RESIDUO,
			aSosp.CD_SOSPESO,
			aSosp.IM_ASSOCIATO,
			s.DT_REGISTRAZIONE,
			aRev.UTCR,
			aRev.DACR
	   from sospeso s
	   where s.CD_CDS 			    = aSosp.CD_CDS
 		 and s.ESERCIZIO 		    = aSosp.ESERCIZIO
 		 and s.TI_ENTRATA_SPESA     = aSosp.TI_ENTRATA_SPESA
 		 and s.TI_SOSPESO_RISCONTRO = aSosp.TI_SOSPESO_RISCONTRO
 		 and s.CD_SOSPESO 		    = aSosp.CD_SOSPESO;

	end loop;  -- fine loop 3

	-- fine inserimento record C: sospesi

	   -- inizio inserimento record D: codici siope

   for aRsiope in (select distinct rsiope.cd_siope CD_SIOPE, descrizione DS_SIOPE, sum(importo) IM_SIOPE, rsiope.pg_reversale
   			from reversale_siope rsiope, codici_siope siope
				where rsiope.CD_CDS = aRev.CD_CDS
  				and rsiope.ESERCIZIO  = aRev.ESERCIZIO
  				and rsiope.PG_REVERSALE = aRev.PG_REVERSALE
				and rsiope.esercizio_siope = siope.esercizio
				and rsiope.ti_gestione = siope.ti_gestione
				and rsiope.cd_siope = siope.cd_siope group by rsiope.cd_siope, descrizione, rsiope.pg_reversale) loop
   -- inizio loop 4

   	  i:= i+1;

	  insert into VPG_REVERSALE (ID,
								CHIAVE,
								SEQUENZA,
								DESCRIZIONE,
								CD_CDS,
								ESERCIZIO,
								PG_REVERSALE,
								TI_REVERSALE,
								TI_RECORD,
								CD_SIOPE,
								DS_SIOPE,
								IM_SIOPE,
								UTCR,
								DACR)
	   values( aId,
		    'D:siope',
		    i,
			'Stampa RPT',
			aRev.CD_CDS,
			aRev.ESERCIZIO,
			aRev.PG_REVERSALE,
			aRev.TI_REVERSALE,
			'D',
			aRsiope.CD_SIOPE,
			aRsiope.DS_SIOPE,
			aRsiope.IM_SIOPE,
			aRev.UTCR,
			aRev.DACR);

   end loop; -- fine loop 4

-- fine inserimento record (D): siope
-- inizio inserimento record (E): cup

   -- ciclo sui cup
   for aRcup in (select distinct rcup.cd_cup CD_cup, descrizione DS_CUP, sum(importo) IM_CUP, rcup.pg_reversale
   			from reversale_cup rcup, cup
				where rcup.CD_CDS = aRev.CD_CDS
  				and rcup.ESERCIZIO  = aRev.ESERCIZIO
  				and rcup.pg_reversale = aRev.pg_reversale
				  and rcup.cd_cup = cup.cd_cup group by rcup.cd_cup, descrizione, rcup.pg_reversale
				  union
				  (select distinct rcup.cd_cup CD_cup, descrizione DS_CUP, sum(importo) IM_CUP, rcup.pg_reversale
   			from reversale_siope_cup rcup, cup
				where rcup.CD_CDS = aRev.CD_CDS
  				and rcup.ESERCIZIO  = aRev.ESERCIZIO
  				and rcup.pg_reversale = aRev.pg_reversale
				  and rcup.cd_cup = cup.cd_cup group by rcup.cd_cup, descrizione, rcup.pg_reversale)) loop
   -- inizio loop 5

   	  i:= i+1;
insert into VPG_REVERSALE (ID,
								CHIAVE,
								SEQUENZA,
								DESCRIZIONE,
								CD_CDS,
								ESERCIZIO,
								PG_REVERSALE,
								TI_REVERSALE,
								TI_RECORD,
								CD_CUP,
								DS_CUP,
								IM_CUP,
								UTCR,
								DACR)
	   values( aId,
		    'E:cup',
  		  i,
  			'Stampa RPT',
  			aRev.CD_CDS,
  			aRev.ESERCIZIO,
  			aRev.PG_REVERSALE,
  			aRev.TI_REVERSALE,
  			'E',
  			aRcup.CD_CUP,
  			aRcup.DS_CUP,
  			aRcup.IM_CUP,
  			aRev.UTCR,
  			aRev.DACR);
   end loop; -- fine loop 5

-- fine inserimento record (E): CUP

end;
/


