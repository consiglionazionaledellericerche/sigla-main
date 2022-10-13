CREATE OR REPLACE PROCEDURE         SPG_MANDATO
--
-- Date: 17/01/2010
-- Version: 1.18
--
-- Protocollo VPG per stampa massiva di mandati
--
--
-- History:
--
-- Date: 18/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 21/02/2003
-- Version: 1.1
-- Aggiunti parametri di ingresso
--
-- Date: 26/02/2003
-- Version: 1.2
-- Corretta select iniziale
--
-- Date: 07/03/2003
-- Version: 1.3
-- Inserita ds_mandato come causale
--
-- Date: 20/03/2003
-- Version: 1.4
-- Filtro sui sospesi annullati
--
-- Date: 24/03/2003
-- Version: 1.5
-- Estrazione del terzo cedente per modalit? di pagamento di tipo cessonario
--
-- Date: 24/03/2003
-- Version: 1.6
-- Corretta outer join cd_terzo_cedente
--
-- Date: 25/03/2003
-- Version: 1.7
-- Filtro sui riscontri
--
-- Date: 14/05/2003
-- Version: 1.8
-- Gestione modalit? di pagamento diverse note di credito-fatture
-- (Segnalazione n. 597)
--
-- Date: 19/05/2003
-- Version: 1.9
-- Corretto filtro per modalit? di pagamento diverse (segnalazione n. 597)
--
-- Date: 21/05/2003
-- Version: 1.10
-- Corretto filtro
--
-- Date: 26/05/2003
-- Version: 1.11
-- Corretta select modalit? di pagamento (segnalazione n. 605)
--
-- Date: 20/01/2004
-- Version: 1.12
-- Estrazione CIN dalla banca (richiesta n. 697)
--
-- Date: 21/01/2004
-- Version: 1.13
-- Estrazione dt_nascita del terzo (richiesta n. 699)
--
-- Date: 30/01/2006
-- Version: 1.14
-- Aggiunto campo CD_TIPO_DOCUMENTO_CONT per la stampa dei mandati vpg_man_rev_ass.rpt
--
-- Date: 18/07/2006
-- Version: 1.15
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 09/05/2007
-- Version: 1.16
-- Gestione Siope:
-- aggiornata la funzione per inserire il riepilogo Codici Siope
--
--
-- Date: 09/09/2010
-- Version: 1.17
-- Gestione CUP:
-- aggiornata la funzione per inserire il riepilogo CUP
--
--
-- Date: 17/01/2010
-- Version: 1.18
-- Gestione Conto Speciale Ente Banca d'Italia:
-- inserita la modifica il 5/1 ed ? stata modificata la gestione utilizzando le funzioni del package CNRCTB015
--
-- Body:
--
(
 aCd_cds in varchar2,
 aEs in number,
 aPg_da in number,
 aPg_a in number,
 aDt_da in varchar2,
 aDt_a in varchar2,
 aCd_terzo in varchar2
) is
 aId number;
 i number;
 aNum1 number := 0;
 aNum2 number := 0;
 aNum3 number := 0;
 aNum4 number := 0;
 aVar1 varchar2(300) := null;
 aVar2 varchar2(300) := null;
 aVar3 varchar2(10) := null;
 uo varchar2(30);
 FL_CONTO_BI char(1):=null;
 aAbiCCEnteBI varchar2(50);
 aCabCCEnteBI varchar2(50);
 aNCCEnteBI varchar2(50);
 tesoreria_unica  char(1):=null;
 flNuovoPdg parametri_cnr.fl_nuovo_pdg%type;
begin
 select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;
 i:=0;

 for aMan in (select * from mandato man
 	 	  	  where man.CD_CDS     = aCd_cds
			    and man.ESERCIZIO  = aEs
				and man.PG_MANDATO >= aPg_da
				and man.PG_MANDATO <= aPg_a
			    and man.DT_EMISSIONE >= to_date(aDt_da,'YYYY/MM/DD')
				and man.DT_EMISSIONE <= to_date(aDt_a,'YYYY/MM/DD')
			    and exists (select 1 from mandato_riga mriga
			  			    where mriga.CD_CDS     = aCd_cds
							  and mriga.ESERCIZIO  = aEs
							  and mriga.PG_MANDATO = man.PG_MANDATO
							  and to_char(mriga.CD_TERZO) like aCd_terzo)) loop

   i := i+1;

-- inizio inserimento record (A,A): testata e informazioni del beneficiario

--    select distinct cd_terzo, cd_modalita_pag, pg_banca,  nvl(cd_terzo_cedente,0)
--    into aNum1, aVar1, aNum2, aNum3
--    from mandato_riga mriga
--    where mriga.CD_CDS     	 	  	 = aMan.CD_CDS
--      and mriga.ESERCIZIO  			 = aMan.ESERCIZIO
-- 	 and mriga.PG_MANDATO 			 = aMan.PG_MANDATO
-- 	 and mriga.CD_TIPO_DOCUMENTO_AMM <> 'FATTURA_P'
-- 	 and mriga.IM_MANDATO_RIGA		 <> 0; -- escludo note di credito

	select distinct * into aNum1, aVar1, aNum2, aNum3
	from (select distinct cd_terzo, cd_modalita_pag, pg_banca,  nvl(cd_terzo_cedente,0)
	   	  from mandato_riga mriga
	      where mriga.CD_CDS     	 	  	 = aMan.CD_CDS
	        and mriga.ESERCIZIO  			 = aMan.ESERCIZIO
		    	and mriga.PG_MANDATO 			 = aMan.PG_MANDATO
		 			and mriga.CD_TIPO_DOCUMENTO_AMM = 'FATTURA_P'
		 			and mriga.IM_MANDATO_RIGA <> 0  -- escludo note di credito
		  union all
	   	  select distinct cd_terzo, cd_modalita_pag, pg_banca,  nvl(cd_terzo_cedente,0)
	   	  from mandato_riga mriga
	   	  where mriga.CD_CDS     	 	  	 = aMan.CD_CDS
	        and mriga.ESERCIZIO  			 = aMan.ESERCIZIO
		 			and mriga.PG_MANDATO 			 = aMan.PG_MANDATO
		 			and mriga.CD_TIPO_DOCUMENTO_AMM <> 'FATTURA_P');

	select parametri_cnr.fl_tesoreria_unica, parametri_cnr.fl_nuovo_pdg into tesoreria_unica, flNuovoPdg
 from parametri_cnr
 where
 esercizio =aMan.esercizio;

   begin
   		select rmp.DS_MODALITA_PAG,FL_CONTO_BI into aVar2,FL_CONTO_BI
			from rif_modalita_pagamento rmp
			where rmp.CD_MODALITA_PAG = aVar1;
   exception when NO_DATA_FOUND then
   			 aVar2 := null;
   end;


   insert into VPG_MANDATO (ID,
							CHIAVE,
							DESCRIZIONE,
							SEQUENZA,
							CD_CDS,
							ESERCIZIO,
							PG_MANDATO,
							TI_RECORD_L1,
							TI_RECORD_L2,
							DT_EMISSIONE,
							TI_MANDATO,
							TI_COMPETENZA_RESIDUO,
							DS_CDS,
							CD_UO_ORIGINE,
							DS_UO_CDS,
							CAB,
							ABI,
							NUMERO_CONTO,
							CIN,
							INTESTAZIONE,
							IBAN,
							SWIFT,
							IM_NETTO,
							IM_NETTO_LETTERE,
							DS_MANDATO,
							CD_TERZO,
							CD_UO_TERZO,
							DENOMINAZIONE_SEDE,
							VIA_SEDE,
							CAP_COMUNE_SEDE,
							DS_COMUNE_SEDE,
							CD_PROVINCIA_BENEFICIARIO,
							CODICE_FISCALE,
							PARTITA_IVA,
							DT_NASCITA,
							CD_MODALITA_PAG,
							DS_MODALITA_PAG,
							IM_MANDATO,
							IM_RITENUTE,
							UTCR,
							DACR,
							STATO)
   select distinct aId,
   		  'Testata,beneficiario',
		  'Stampa RPT',
		  i,
		  aMan.CD_CDS,
		  aMan.ESERCIZIO,
		  aMan.PG_MANDATO,
		  'A',
		  'A',
		  aMan.DT_EMISSIONE,
		  aMan.TI_MANDATO,
		  aMan.TI_COMPETENZA_RESIDUO,
		  uo1.DS_UNITA_ORGANIZZATIVA,
		  aMan.CD_UO_ORIGINE,
		  uo2.DS_UNITA_ORGANIZZATIVA,
		  ban1.CAB,
		  ban1.ABI,
		  ban1.NUMERO_CONTO,
		  nvl(ban1.CIN,' '),
		  ban1.INTESTAZIONE,
		  ban1.codice_iban,
		  ban1.codice_swift,
		  aMan.IM_MANDATO - aMan.IM_RITENUTE,
		  IBMUTL001.INLETTERE(aMan.IM_MANDATO - aMan.IM_RITENUTE),
		  aMan.DS_MANDATO,
		  aNum1,
		  vat.CD_UNITA_ORGANIZZATIVA,
		  vat.DENOMINAZIONE_SEDE,
		  vat.VIA_SEDE || ' ' || vat.NUMERO_CIVICO_SEDE,
		  vat.CAP_COMUNE_SEDE,
		  vat.DS_COMUNE_SEDE,
		  vat.CD_PROVINCIA_SEDE,
		  vat.CODICE_FISCALE,
		  vat.PARTITA_IVA,
		  vat.DT_NASCITA,
		  aVar1,
		  aVar2,
		  aMan.IM_MANDATO,
		  aMan.IM_RITENUTE,
		  aMan.UTCR,
		  aMan.DACR,
		  aMan.stato
   from unita_organizzativa uo1
   	   ,unita_organizzativa uo2
	   ,terzo ter
	   ,banca ban1
	   ,v_anagrafico_terzo vat
   where uo1.CD_UNITA_ORGANIZZATIVA	 = aMan.CD_CDS
     and uo2.CD_UNITA_ORGANIZZATIVA  = aMan.CD_UO_ORIGINE
	 and ter.cd_unita_organizzativa  = aMan.CD_UNITA_ORGANIZZATIVA
	 and ban1.CD_TERZO				 = ter.CD_TERZO  AND
	       (( ban1.FL_CC_CDS       = 'Y' and
         tesoreria_unica ='N' ) or
         (tesoreria_unica ='Y' and
         exists(select 1 from configurazione_cnr where
         (configurazione_cnr.esercizio =0 or
         configurazione_cnr.esercizio =aMan.esercizio) and
         cd_chiave_primaria ='CONTO_CORRENTE_SPECIALE' and
         cd_chiave_secondaria='ENTE' and
         ban1.abi = val01 and
         ban1.cab = val02 and
         ban1.numero_conto like '%'||val03 )))
	 and ban1.FL_CANCELLATO          = 'N'
	 and vat.CD_TERZO				 = aNum1 ;

if(tesoreria_unica='N') then
-- se la UO ? ENTE e CD_TIPO_DOCUMENTO_AMM = GEN_CORV_S la modalit? di pagamento deve essere BI

select count(*) into aNum4
from
(
 select CD_TIPO_DOCUMENTO_AMM
             from mandato_riga mriga
          	 where mriga.CD_CDS     	 	  	 = aMan.CD_CDS
	        	and mriga.ESERCIZIO  			 = aMan.ESERCIZIO
		    		and mriga.PG_MANDATO 			 = aMan.PG_MANDATO
		 				and mriga.IM_MANDATO_RIGA <> 0  -- escludo note di credito
            and CD_TIPO_DOCUMENTO_AMM =  CNRCTB100.TI_GEN_CORI_VER_SPESA);

     aAbiCCEnteBI:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE_BI,CNRCTB080.ENTE);
     aCabCCEnteBI:=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE_BI,CNRCTB080.ENTE);
     aNCCEnteBI:=CNRCTB015.GETVAL03PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE_BI,CNRCTB080.ENTE);

 --if aNum4 > 0 and CNRCTB020.getdesUO(aMan.CD_CDS) = CNRCTB020.TIPO_ENTE then
if aNum4 > 0 and CNRCTB020.getCDCDSENTE(aMan.esercizio) = aMan.CD_CDS then

        update VPG_MANDATO vpg
        set (CAB,
						 ABI,
						 NUMERO_CONTO,
						 CIN,
						 INTESTAZIONE,
						 IBAN,
						 SWIFT) =
             ( select ban1.CAB,
		              ban1.ABI,
		              ban1.NUMERO_CONTO,
		              nvl(ban1.CIN,' '),
		              ban1.INTESTAZIONE,
		              ban1.codice_iban,
		              ban1.codice_swift from banca ban1,terzo ter
                where ter.cd_unita_organizzativa  = aMan.CD_UNITA_ORGANIZZATIVA
	            and ban1.CD_TERZO				 = ter.CD_TERZO
                and ban1.abi = aAbiCCEnteBI
                and ban1.cab = aCabCCEnteBI
                and ban1.numero_conto = aNCCEnteBI)
               -- and ban1.ti_pagamento = 'I')
        where vpg.CD_CDS 		 = aMan.CD_CDS
	    and vpg.ESERCIZIO 	 = aMan.ESERCIZIO
	    and vpg.PG_MANDATO   = aMan.PG_MANDATO
	    and vpg.TI_RECORD_L1 = 'A'
	    and vpg.TI_RECORD_L2 = 'A'
	    and vpg.SEQUENZA	 = i;
 end if;
else
-- tesoreria unica
-- se la UO ? 000.407 e CD_TIPO_DOCUMENTO_AMM = GEN_CORV_S la modalit? di pagamento deve essere BI
    uo := CNRCTB020.getCdUOVersIVA(aEs);

select count(*) into aNum4
from
(
 select CD_TIPO_DOCUMENTO_AMM
             from mandato_riga mriga
          	 where mriga.CD_CDS     	 	  	 = aMan.CD_CDS
	        	and mriga.ESERCIZIO  			 = aMan.ESERCIZIO
		    		and mriga.PG_MANDATO 			 = aMan.PG_MANDATO
		 				and mriga.IM_MANDATO_RIGA <> 0  -- escludo note di credito
            and CD_TIPO_DOCUMENTO_AMM =  CNRCTB100.TI_GEN_CORI_VER_SPESA);

     aAbiCCEnteBI:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE_BI,CNRCTB080.ENTE);
     aCabCCEnteBI:=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE_BI,CNRCTB080.ENTE);
     aNCCEnteBI:=CNRCTB015.GETVAL03PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE_BI,CNRCTB080.ENTE);
	if (FL_CONTO_BI ='Y') then
		if aNum4 > 0 and aMan.CD_unita_organizzativa = uo then

	        update VPG_MANDATO vpg
	        set (CAB,
							 ABI,
							 NUMERO_CONTO,
							 CIN,
							 INTESTAZIONE,
							 IBAN,
							 SWIFT) =
	             ( select ban1.CAB,
			              ban1.ABI,
			              ban1.NUMERO_CONTO,
			              nvl(ban1.CIN,' '),
			              ban1.INTESTAZIONE,
			              ban1.codice_iban,
			              ban1.codice_swift from banca ban1,terzo ter
	                where ter.cd_unita_organizzativa  = aMan.CD_UNITA_ORGANIZZATIVA
		            and ban1.CD_TERZO				 = ter.CD_TERZO
	                and ban1.abi = aAbiCCEnteBI
	                and ban1.cab = aCabCCEnteBI
	                and ban1.numero_conto like '%' ||aNCCEnteBI)
	               -- and ban1.ti_pagamento = 'I')
	        where vpg.CD_CDS 		 = aMan.CD_CDS
		    and vpg.ESERCIZIO 	 = aMan.ESERCIZIO
		    and vpg.PG_MANDATO   = aMan.PG_MANDATO
		    and vpg.TI_RECORD_L1 = 'A'
		    and vpg.TI_RECORD_L2 = 'A'
		    and vpg.SEQUENZA	 = i;
	end if;
	end if;
end if;
--
-- informazioni terzo cedente

   if aNum3 <> 0 then
   	  update VPG_MANDATO vpg
	  set (CD_TERZO_CEDENTE,
	  	   DENOM_SEDE_CEDENTE)
	  = (select aNum3, ter.DENOMINAZIONE_SEDE
	  	 from terzo ter
		 where ter.CD_TERZO = aNum3)
	  where vpg.CD_CDS 		 = aMan.CD_CDS
	    and vpg.ESERCIZIO 	 = aMan.ESERCIZIO
	    and vpg.PG_MANDATO   = aMan.PG_MANDATO
	    and vpg.TI_RECORD_L1 = 'A'
	    and vpg.TI_RECORD_L2 = 'A'
	    and vpg.SEQUENZA	 = i;
   end if;

-- coordinate bancarie istituto cassiere
   begin
   		update VPG_MANDATO vpg
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
			  and abi.PG_COMUNE = com.PG_COMUNE(+))
		 where vpg.CD_CDS 		= aMan.CD_CDS
		   and vpg.ESERCIZIO 	= aMan.ESERCIZIO
		   and vpg.PG_MANDATO   = aMan.PG_MANDATO
		   and vpg.TI_RECORD_L1	= 'A'
		   and vpg.TI_RECORD_L2	= 'A'
		   and vpg.SEQUENZA		= i;
   exception when NO_DATA_FOUND then
   			 null;
   end;

-- coordinate bancarie beneficiario

-- (NB: per mandati di regolarizzazione, cd_modalita_pag, pg_banca potrebbero essere null)
   begin
   		update VPG_MANDATO vpg
		set    (TI_PAGAMENTO,
				NUMERO_CONTO_TERZO,
				CIN_TERZO,
				INTESTAZIONE_TERZO,
				CAB_TERZO,
				ABI_TERZO,
				IBAN_TERZO,
				SWIFT_TERZO)
		 = (select ban2.TI_PAGAMENTO,
				   ban2.NUMERO_CONTO,
				   nvl(ban2.CIN,' '),
				   ban2.INTESTAZIONE,
				   ban2.CAB,
				   ban2.ABI,
				   ban2.codice_iban,
				   ban2.codice_swift
			from banca ban2
			where ban2.CD_TERZO = aNum1
			  and ban2.PG_BANCA = aNum2)
		 where vpg.CD_CDS 		= aMan.CD_CDS
		   and vpg.ESERCIZIO 	= aMan.ESERCIZIO
		   and vpg.PG_MANDATO   = aMan.PG_MANDATO
		   and vpg.TI_RECORD_L1	= 'A'
		   and vpg.TI_RECORD_L2	= 'A'
		   and vpg.SEQUENZA		= i;
   exception when NO_DATA_FOUND then
   			 null;
   end;

   begin
   		update VPG_MANDATO vpg
		set (DS_ABICAB_TERZO,
			 VIA_BANCA_TERZO,
			 CAP_BANCA_TERZO,
			 DS_COMUNE_BANCA_TERZO,
			 CD_PV_BANCA_TERZO)
		 = (select abi.DS_ABICAB,
				   abi.VIA,
				   abi.CAP,
				   com.DS_COMUNE,
				   com.CD_PROVINCIA
			from abicab abi
				,comune com
			where abi.ABI       = vpg.ABI_TERZO
			  and abi.CAB	    = vpg.CAB_TERZO
			  and abi.PG_COMUNE = com.PG_COMUNE(+))
		 where vpg.CD_CDS 		= aMan.CD_CDS
		   and vpg.ESERCIZIO 	= aMan.ESERCIZIO
		   and vpg.PG_MANDATO   = aMan.PG_MANDATO
		   and vpg.TI_RECORD_L1	= 'A'
		   and vpg.TI_RECORD_L2	= 'A'
		   and vpg.SEQUENZA		= i;
   exception when NO_DATA_FOUND then
   			 null;
   end;

-- fine inserimento record (A,A): testata e informazioni del beneficiario

   -- ciclo sulle righe di mandato
   for aMriga in (select mriga.*, mriga.rowid from mandato_riga mriga
   	   		  	  where mriga.CD_CDS	     = aMan.CD_CDS
				    and mriga.ESERCIZIO      = aMan.ESERCIZIO
  					and mriga.PG_MANDATO     = aMan.PG_MANDATO) loop
   -- inizio loop 1

   	  i := i+1;
-- inizio inserimento record (B,A): dettagli delle righe di mandato
	  insert into VPG_MANDATO  (ID,
								CHIAVE,
								DESCRIZIONE,
								SEQUENZA,
								CD_CDS,
								ESERCIZIO,
								PG_MANDATO,
								TI_RECORD_L1,
								TI_RECORD_L2,
								ID_RIGA,
								DT_EMISSIONE,
								TI_MANDATO,
								TI_COMPETENZA_RESIDUO,
								CD_TERZO,
								CD_TIPO_DOCUMENTO_AMM,
								PG_DOC_AMM,
								IM_NETTO_RIGA,
								IM_NETTO_RIGA_LETTERE,
								DS_MANDATO_RIGA,
								ESERCIZIO_ORI_OBBLIGAZIONE,
								PG_OBBLIGAZIONE,
								DS_OBBLIGAZIONE,
								IM_MANDATO,
								IM_RITENUTE,
								UTCR,
								DACR,
								cd_tipo_documento_cont)
	  select  aId,
	   		  'Dettagli',
			  'Stampa RPT',
			  i,
			  aMan.CD_CDS,
			  aMan.ESERCIZIO,
			  aMan.PG_MANDATO,
			  'B',
			  'A',
			  rowidtochar(aMriga.rowid),
			  aMan.DT_EMISSIONE,
			  aMan.TI_MANDATO,
			  aMan.TI_COMPETENZA_RESIDUO,
			  aMriga.CD_TERZO,
			  aMriga.CD_TIPO_DOCUMENTO_AMM,
			  aMriga.PG_DOC_AMM,
			  aMriga.IM_MANDATO_RIGA - aMriga.IM_RITENUTE_RIGA,
			  IBMUTL001.INLETTERE(aMriga.IM_MANDATO_RIGA - aMriga.IM_RITENUTE_RIGA),
			  aMriga.DS_MANDATO_RIGA,
			  aMriga.ESERCIZIO_ORI_OBBLIGAZIONE,
			  aMriga.PG_OBBLIGAZIONE,
			  obb.DS_OBBLIGAZIONE,
			  aMan.IM_MANDATO,
			  aMan.IM_RITENUTE,
			  aMan.UTCR,
			  aMan.DACR,
			  obb.cd_tipo_documento_cont
	  from obbligazione obb
	  where obb.CD_CDS 	   	     = aMriga.CD_CDS
  	    and obb.ESERCIZIO        = aMriga.ESERCIZIO
  	    and obb.ESERCIZIO_ORIGINALE  = aMriga.ESERCIZIO_ORI_OBBLIGAZIONE
  		and obb.PG_OBBLIGAZIONE  = aMriga.PG_OBBLIGAZIONE;
-- fine inserimento record (B,A): dettagli delle righe di mandato

-- inizio inserimento record (B,B): capitoli associati alle obbligazioni delle righe di mandato
	  -- ciclo sui capitoli
	  for aObbv in (select * from obbligazione_scad_voce obbv
	  	  		    where obbv.cd_cds                      = aMriga.CD_CDS
					  and obbv.esercizio                   = aMriga.ESERCIZIO
					  and obbv.esercizio_originale         = aMriga.ESERCIZIO_ORI_OBBLIGAZIONE
  					  and obbv.pg_obbligazione             = aMriga.PG_OBBLIGAZIONE
  					  and obbv.pg_obbligazione_scadenzario = aMriga.PG_OBBLIGAZIONE_SCADENZARIO) loop
	  -- inizio loop 5

	  	 i:= i+1;

	     if flNuovoPdg = 'Y' Then
		  	 insert into VPG_MANDATO   (ID,
										CHIAVE,
										DESCRIZIONE,
										SEQUENZA,
										CD_CDS,
										ESERCIZIO,
										PG_MANDATO,
										TI_RECORD_L1,
										TI_RECORD_L2,
										ID_RIGA,
										ESERCIZIO_ORI_OBBLIGAZIONE,
										PG_OBBLIGAZIONE,
										CD_VOCE,
										DS_VOCE,
										UTCR,
										DACR)
		     select  aId,
		   		  'Capitoli',
				  'Stampa RPT',
				  i,
				  aMan.CD_CDS,
				  aMan.ESERCIZIO,
				  aMan.PG_MANDATO,
				  'B',
				  'B',
				  rowidtochar(aMriga.rowid),
				  aMriga.ESERCIZIO_ORI_OBBLIGAZIONE,
				  aMriga.PG_OBBLIGAZIONE,
				  aObbv.CD_VOCE,
				  voce.DS_ELEMENTO_VOCE,
				  aMan.UTCR,
				  aMan.DACR
		     from elemento_voce voce
			 where voce.ESERCIZIO       = aObbv.ESERCIZIO
			   and voce.TI_GESTIONE 	= aObbv.ti_gestione
	  		   and voce.TI_APPARTENENZA = aObbv.ti_appartenenza
	  		   and voce.CD_ELEMENTO_VOCE = aObbv.CD_VOCE;
	     else
		  	 insert into VPG_MANDATO   (ID,
											CHIAVE,
											DESCRIZIONE,
											SEQUENZA,
											CD_CDS,
											ESERCIZIO,
											PG_MANDATO,
											TI_RECORD_L1,
											TI_RECORD_L2,
											ID_RIGA,
											ESERCIZIO_ORI_OBBLIGAZIONE,
											PG_OBBLIGAZIONE,
											CD_VOCE,
											DS_VOCE,
											UTCR,
											DACR)
		     select  aId,
		   		  'Capitoli',
				  'Stampa RPT',
				  i,
				  aMan.CD_CDS,
				  aMan.ESERCIZIO,
				  aMan.PG_MANDATO,
				  'B',
				  'B',
				  rowidtochar(aMriga.rowid),
				  aMriga.ESERCIZIO_ORI_OBBLIGAZIONE,
				  aMriga.PG_OBBLIGAZIONE,
				  aObbv.CD_VOCE,
				  voce.DS_VOCE,
				  aMan.UTCR,
				  aMan.DACR
		     from voce_f voce
			 where voce.ESERCIZIO       = aObbv.ESERCIZIO
			   and voce.TI_GESTIONE 	= aObbv.ti_gestione
	  		   and voce.TI_APPARTENENZA = aObbv.ti_appartenenza
	  		   and voce.CD_VOCE 		= aObbv.CD_VOCE;
	  	 end if;

	  end loop; -- fine loop 5

-- fine inserimento record (B,B): capitoli associati alle obbligazioni delle righe di mandato

   end loop; -- fine loop 1

-- inizio inserimento record (C,A): reversali associate al mandato
   -- ciclo sulle reversali associate
   for aAssmr in (select * from ass_mandato_reversale assmr
   	   		  	  where assmr.CD_CDS_MANDATO     = aMan.cd_cds
				    and assmr.ESERCIZIO_MANDATO  = aMan.esercizio
  					and assmr.PG_MANDATO         = aMan.pg_mandato
  					order by assmr.pg_reversale) loop
   -- inizio loop 2

   	  i:= i+1;

	  insert into VPG_MANDATO  (ID,
								CHIAVE,
								DESCRIZIONE,
								SEQUENZA,
								CD_CDS,
								ESERCIZIO,
								PG_MANDATO,
								TI_RECORD_L1,
								TI_RECORD_L2,
								DT_EMISSIONE,
								TI_MANDATO,
								TI_COMPETENZA_RESIDUO,
								CD_CDS_REV,
								ESERCIZIO_REV,
								PG_REVERSALE,
								DS_REVERSALE_RIGA,
								IM_REVERSALE_RIGA,
								UTCR,
								DACR)
	  select  aId,
	   		  'Reversali associate',
			  'Stampa RPT',
			  i,
			  aMan.CD_CDS,
			  aMan.ESERCIZIO,
			  aMan.PG_MANDATO,
			  'C',
			  'A',
			  aMan.DT_EMISSIONE,
			  aMan.TI_MANDATO,
			  aMan.TI_COMPETENZA_RESIDUO,
			  aAssmr.CD_CDS_REVERSALE,
			  aAssmr.ESERCIZIO_REVERSALE,
			  aAssmr.PG_REVERSALE,
			  r.DS_REVERSALE,
			  r.IM_REVERSALE,
			  aMan.UTCR,
			  aMan.DACR
	  from reversale r
	  where r.CD_CDS        = aAssmr.CD_CDS_REVERSALE
	    and r.ESERCIZIO     = aAssmr.ESERCIZIO_REVERSALE
  		and r.PG_REVERSALE  = aAssmr.PG_REVERSALE;

   end loop; -- fine loop 2

-- fine inserimento record (C,A): reversali associate al mandato

-- inizio inserimento record (D,A): sospesi
   -- ciclo sui sospesi
   for aSosp in (select * from sospeso_det_usc sosp
   	   		 	 where sosp.CD_CDS_mandato      = aMan.cd_cds
  				   and sosp.ESERCIZIO   = aMan.esercizio
  				   and sosp.PG_MANDATO  = aMan.pg_mandato
				   and sosp.STATO		<> 'A'
				   and sosp.TI_SOSPESO_RISCONTRO = 'S') loop
   -- inizio loop 3

   	  i:= i+1;

	  insert into VPG_MANDATO  (ID,
								CHIAVE,
								DESCRIZIONE,
								SEQUENZA,
								CD_CDS,
								ESERCIZIO,
								PG_MANDATO,
								TI_RECORD_L1,
								TI_RECORD_L2,
								DT_EMISSIONE,
								TI_MANDATO,
								TI_COMPETENZA_RESIDUO,
								CD_SOSPESO,
								IM_ASSOCIATO,
								DT_REGISTRAZ_SOSPESO,
								UTCR,
								DACR)
	  select  aId,
	   		  'Sospesi',
			  'Stampa RPT',
			  i,
			  aMan.CD_CDS,
			  aMan.ESERCIZIO,
			  aMan.PG_MANDATO,
			  'D',
			  'A',
			  aMan.DT_EMISSIONE,
			  aMan.TI_MANDATO,
			  aMan.TI_COMPETENZA_RESIDUO,
			  aSosp.CD_SOSPESO,
			  aSosp.IM_ASSOCIATO,
			  s.DT_REGISTRAZIONE,
			  aMan.UTCR,
			  aMan.DACR
	  from sospeso s
	  where s.CD_CDS 			   = aSosp.CD_CDS
  	    and s.ESERCIZIO 		   = aSosp.ESERCIZIO
  		and s.TI_ENTRATA_SPESA 	   = aSosp.TI_ENTRATA_SPESA
  		and s.TI_SOSPESO_RISCONTRO = aSosp.TI_SOSPESO_RISCONTRO
  		and s.CD_SOSPESO 		   = aSosp.CD_SOSPESO;

   end loop; -- fine loop 3

-- fine inserimento record (D,A): sospesi

-- inizio inserimento record (E,A): bolli

   -- ciclo sui bolli
   for aMterzo in (select * from mandato_terzo mterzo
   	   		   	   where mterzo.CD_CDS     = aMan.CD_CDS
  				     and mterzo.ESERCIZIO  = aMan.ESERCIZIO
  					 and mterzo.PG_MANDATO = aMan.PG_MANDATO) loop
   -- inizio loop 4

   	  i:= i+1;

	  insert into VPG_MANDATO  (ID,
								CHIAVE,
								DESCRIZIONE,
								SEQUENZA,
								CD_CDS,
								ESERCIZIO,
								PG_MANDATO,
								TI_RECORD_L1,
								TI_RECORD_L2,
								DT_EMISSIONE,
								TI_MANDATO,
								TI_COMPETENZA_RESIDUO,
								CD_TIPO_BOLLO,
								DS_TIPO_BOLLO,
								UTCR,
								DACR)
	  select  aId,
	   		  'Bolli',
			  'Stampa RPT',
			  i,
			  aMan.CD_CDS,
			  aMan.ESERCIZIO,
			  aMan.PG_MANDATO,
			  'E',
			  'A',
			  aMan.DT_EMISSIONE,
			  aMan.TI_MANDATO,
			  aMan.TI_COMPETENZA_RESIDUO,
			  aMterzo.CD_TIPO_BOLLO,
			  tbollo.DS_TIPO_BOLLO,
			  aMan.UTCR,
			  aMan.DACR
	  from tipo_bollo tbollo
	  where tbollo.CD_TIPO_BOLLO = aMterzo.CD_TIPO_BOLLO;

   end loop; -- fine loop 4

-- fine inserimento record (E,A): bolli

-- inizio inserimento record (F,A): siope

   -- ciclo sui codici siope
   for aMsiope in (select distinct msiope.cd_siope CD_SIOPE, descrizione DS_SIOPE, sum(importo) IM_SIOPE, msiope.pg_mandato
   			from mandato_siope msiope, codici_siope siope
				where msiope.CD_CDS = aMan.CD_CDS
  				and msiope.ESERCIZIO  = aMan.ESERCIZIO
  				and msiope.PG_MANDATO = aMan.PG_MANDATO
				and msiope.esercizio_siope = siope.esercizio
				and msiope.ti_gestione = siope.ti_gestione
				and msiope.cd_siope = siope.cd_siope group by msiope.cd_siope, descrizione, msiope.pg_mandato) loop
   -- inizio loop 5

   	  i:= i+1;

	  insert into VPG_MANDATO  (ID,
								CHIAVE,
								DESCRIZIONE,
								SEQUENZA,
								CD_CDS,
								ESERCIZIO,
								PG_MANDATO,
								TI_RECORD_L1,
								TI_RECORD_L2,
								CD_SIOPE,
								DS_SIOPE,
								IM_SIOPE,
								UTCR,
								DACR)
	  values(  aId,
	   		  'Siope',
			  'Stampa RPT',
			  i,
			  aMan.CD_CDS,
			  aMan.ESERCIZIO,
			  aMan.PG_MANDATO,
			  'F',
			  'A',
			  aMsiope.CD_SIOPE,
			  aMsiope.DS_SIOPE,
			  aMsiope.IM_SIOPE,
			  aMan.UTCR,
			  aMan.DACR);

   end loop; -- fine loop 5

-- fine inserimento record (F,A): siope
-- inizio inserimento record (G,A): cup

   -- ciclo sui cup
   for aMcup in (select distinct mcup.cd_cup CD_cup, descrizione DS_CUP, sum(importo) IM_CUP, mcup.pg_mandato
   			from mandato_cup mcup, cup
				where mcup.CD_CDS = aMan.CD_CDS
  				and mcup.ESERCIZIO  = aMan.ESERCIZIO
  				and mcup.PG_MANDATO = aMan.PG_MANDATO
				  and mcup.cd_cup = cup.cd_cup group by mcup.cd_cup, descrizione, mcup.pg_mandato
				  union
				  (select distinct mcup.cd_cup CD_cup, descrizione DS_CUP, sum(importo) IM_CUP, mcup.pg_mandato
   			from mandato_siope_cup mcup, cup
				where mcup.CD_CDS = aMan.CD_CDS
  				and mcup.ESERCIZIO  = aMan.ESERCIZIO
  				and mcup.PG_MANDATO = aMan.PG_MANDATO
				  and mcup.cd_cup = cup.cd_cup group by mcup.cd_cup, descrizione, mcup.pg_mandato)) loop
   -- inizio loop 6

   	  i:= i+1;

	  insert into VPG_MANDATO  (ID,
								CHIAVE,
								DESCRIZIONE,
								SEQUENZA,
								CD_CDS,
								ESERCIZIO,
								PG_MANDATO,
								TI_RECORD_L1,
								TI_RECORD_L2,
								CD_CUP,
								DS_CUP,
								IM_CUP,
								UTCR,
								DACR)
	  values(  aId,
	   		  'CUP',
			  'Stampa RPT',
			  i,
			  aMan.CD_CDS,
			  aMan.ESERCIZIO,
			  aMan.PG_MANDATO,
			  'G',
			  'A',
			  aMcup.CD_CUP,
			  aMcup.DS_CUP,
			  aMcup.IM_CUP,
			  aMan.UTCR,
			  aMan.DACR);

   end loop; -- fine loop 6

-- fine inserimento record (G,A): CUP
 end loop;
End;
/


