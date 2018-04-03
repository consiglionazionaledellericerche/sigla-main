CREATE OR REPLACE PROCEDURE         SPG_MISSIONE
--
-- Date: 18/07/2006
-- Version: 1.13
--
-- Protocollo VPG per stampa massiva di note e prospetti di liquidazione missione
--
--
-- History:
--
-- Date: 03/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 26/02/2003
-- Version: 1.1
-- Corretta selezione missioni
--
-- Date: 03/03/2003
-- Version: 1.2
-- Estrazione MISSIONE_DIARIA.IM_DIARIA (segnalazione 503)
--
-- Date: 06/03/2003
-- Version: 1.3
-- Gestione eccezione per chiave duplicata in dettagli di diaria
--
-- Date: 13/03/2003
-- Version: 1.4
-- Corretta gestione testata per missioni provvisorie
--
-- Date: 08/04/2003
-- Version: 1.5
-- Corretta gestione rapporti anagrafico (segnalazione n.567)
--
-- Date: 15/04/2003
-- Version: 1.6
-- Completata correzione gestione rapporto
--
-- Date: 06/05/2003
-- Version: 1.7
-- Corretta tipizzazione record indennit? chilometrica (segnalazione n. 589)
--
-- Date: 08/05/2003
-- Version: 1.8
-- Inserito campo aliquota dei cori (segnalazione n. 593)
--
-- Date: 27/05/2003
-- Version: 1.9
-- Valorizzato ds_comune e cd_provincia per missioni associate a compenso
--
-- Date: 04/08/2003
-- Version: 1.10
-- Corretta valorizzazione pg_reversale del rimborso
--
-- Date: 29/09/2003
-- Version: 1.11
-- Inserito im_anticipo per im_anticipo = im_missione (no rimborso)
-- (segnalazione n.648)
-- Stampa riquadro anticipo anche nel caso questo transiti per il fondo
-- economale (segnalazione n. 649)
--
-- Date: 20/01/2004
-- Version: 1.12
-- Estrazione CIN dalla BANCA (richiesta n. 697)
--
-- Date: 18/07/2006
-- Version: 1.13
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
(
 aCd_cds in varchar2,
 aCd_uo in varchar2,
 aEs in number,
 aPg_da in number,
 aPg_a in number,
 aCd_terzo in varchar2
) is
 aId number;
 i number;
 aNum1 number := 0;
 aNum2 number := 0;
 aNum3 number := 0;
 aVar1 varchar2(300) := null;
 aVar2 varchar2(300) := null;
 aComp compenso%rowtype;
 aAnt anticipo%rowtype;
 aRim rimborso%rowtype;
begin
 select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;
 i:=0;

 for aMiss in (select * from missione m
 	 	   	   where m.CD_CDS                 = aCd_cds
			     and m.CD_UNITA_ORGANIZZATIVA = aCd_uo
				 and m.ESERCIZIO			  = aEs
				 and m.PG_MISSIONE			  >= aPg_da
				 and m.PG_MISSIONE			  <= aPg_a
				 and m.STATO_COFI			  != 'A'
				 and to_char(m.CD_TERZO) like aCd_terzo) loop
	-- inizio loop 1

	i := i+1;

	if aMiss.FL_ASSOCIATO_COMPENSO = 'N' then
	--  missioni non associate a compenso

	--	inizio inserimento record (A,A) testata

		begin
		     --Il mandato su missione è unico anche in presenza di più impegni sulla missione
		     --per cui la select deve riportare un'unica riga
			 select distinct mriga.ESERCIZIO, mriga.PG_MANDATO into aNum1, aNum2
			 from mandato_riga mriga
			 where mriga.CD_CDS_DOC_AMM	  	   = aMiss.CD_CDS
	  		   and mriga.CD_UO_DOC_AMM	  	   = aMiss.CD_UNITA_ORGANIZZATIVA
	  		   and mriga.ESERCIZIO_DOC_AMM 	   = aMiss.ESERCIZIO
	  		   and mriga.PG_DOC_AMM		  	   = aMiss.PG_MISSIONE
	  		   and mriga.CD_TIPO_DOCUMENTO_AMM = 'MISSIONE'
	  		   and mriga.STATO				   <> 'A' ;
		exception when NO_DATA_FOUND then
			  aNum1 := 0;
			  aNum2 := 0;
		end;

		begin
			 select uo.DS_UNITA_ORGANIZZATIVA into aVar1
			 from unita_organizzativa uo
			 where uo.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA;
		exception when NO_DATA_FOUND then
			 aVar1 := null;
		end;

		select rif.DS_INQUADRAMENTO into aVar2
		from rif_inquadramento rif
		where rif.PG_RIF_INQUADRAMENTO = aMiss.PG_RIF_INQUADRAMENTO;

		select i+Count(1) Into i
		from terzo ter
			,rapporto rap
		where ter.CD_TERZO         = aMiss.CD_TERZO
		  and rap.CD_TIPO_RAPPORTO = aMiss.CD_TIPO_RAPPORTO
		  and rap.CD_ANAG		   = ter.CD_ANAG
		  and rap.DT_INI_VALIDITA <= Trunc(aMiss.DT_INIZIO_MISSIONE)
		  and rap.DT_FIN_VALIDITA >= Trunc(aMiss.DT_INIZIO_MISSIONE);

		insert into VPG_MISSIONE   (ID,
									CHIAVE,
									SEQUENZA,
									DESCRIZIONE,
									CD_CDS,
									CD_UNITA_ORGANIZZATIVA,
									ESERCIZIO,
									PG_MISSIONE,
									TI_RECORD_L1,
									TI_RECORD_L2,
									TI_PROVVISORIO_DEFINITIVO,
									ES_FINANZIARIO,
									DS_UNITA_ORGANIZZATIVA,
									FL_ASSOCIATO_COMPENSO,
									NOME,
									COGNOME,
									CD_TERZO,
									VIA_SEDE,
									NUMERO_CIVICO_SEDE,
									CAP_COMUNE_SEDE,
									TI_ANAGRAFICO,
									ESERCIZIO_ORI_OBBL_ACC,
									PG_OBBL_ACC,
									PG_OBBL_ACC_SCADENZARIO,
									PG_MAN_REV,
									MATRICOLA,
									QUALIFICA,
									DT_REGISTRAZIONE)
		select aId,
			   'AA:Testata',
			   i+Rownum,
			   'Stampa RPT',
			   aMiss.CD_CDS,
			   aMiss.CD_UNITA_ORGANIZZATIVA,
			   aMiss.ESERCIZIO,
			   aMiss.PG_MISSIONE,
			   'A',
			   'A',
			   aMiss.TI_PROVVISORIO_DEFINITIVO,
			   aNum1,
			   aVar1,
			   aMiss.FL_ASSOCIATO_COMPENSO,
			   aMiss.NOME,
			   aMiss.COGNOME,
			   aMiss.CD_TERZO,
			   ter.VIA_SEDE,
			   ter.NUMERO_CIVICO_SEDE,
			   ter.CAP_COMUNE_SEDE,
			   aMiss.TI_ANAGRAFICO,
			   aMiss.ESERCIZIO_ORI_OBBLIGAZIONE,
			   aMiss.PG_OBBLIGAZIONE,
			   aMiss.PG_OBBLIGAZIONE_SCADENZARIO,
			   aNum2,
			   decode(aMiss.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,aMiss.CD_TERZO),
			   aVar2,
			   aMiss.DT_REGISTRAZIONE
		from terzo ter
			,rapporto rap
		where ter.CD_TERZO         = aMiss.CD_TERZO
		  and rap.CD_TIPO_RAPPORTO = aMiss.CD_TIPO_RAPPORTO
		  and rap.CD_ANAG		   = ter.CD_ANAG
		  and rap.DT_INI_VALIDITA <= Trunc(aMiss.DT_INIZIO_MISSIONE)
		  and rap.DT_FIN_VALIDITA >= Trunc(aMiss.DT_INIZIO_MISSIONE);

		select ter.PG_COMUNE_SEDE into aNum1
		from terzo ter
		where ter.CD_TERZO = aMiss.CD_TERZO;

		update VPG_MISSIONE vpg
		set (DS_COMUNE,
			 CD_PROVINCIA)
		= (select com.DS_COMUNE
		  		 ,com.CD_PROVINCIA
		   from comune com
		   where com.PG_COMUNE = aNum1)
		where vpg.CD_CDS 	   	 	     = aMiss.CD_CDS
		  and vpg.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
		  and vpg.ESERCIZIO				 = aMiss.ESERCIZIO
		  and vpg.PG_MISSIONE			 = aMiss.PG_MISSIONE
		  and vpg.TI_RECORD_L1			 = 'A'
		  and vpg.TI_RECORD_L2			 = 'A'
		  and vpg.SEQUENZA				 = i+Rownum;

	-- fine inserimento record (A,A) testata

	-- inizio inserimento record (A,B2) capitoli della missione

    -- ciclo sulle obbligazioni associate alla missione
	for aObblig in (select * from missione_riga missriga
	                where missriga.cd_cds = aMiss.cd_cds
	                and   missriga.cd_unita_organizzativa = aMiss.cd_unita_organizzativa
	                and   missriga.esercizio = aMiss.esercizio
	                and   missriga.pg_missione = aMiss.pg_missione) Loop
	-- missioni con anticipo < missione => no rimborso
		begin
			 select distinct mriga.ESERCIZIO, mriga.PG_MANDATO into aNum1, aNum2
			 from mandato_riga mriga
			 where mriga.CD_CDS_DOC_AMM	  	   = aMiss.CD_CDS
	  		   and mriga.CD_UO_DOC_AMM	  	   = aMiss.CD_UNITA_ORGANIZZATIVA
	  		   and mriga.ESERCIZIO_DOC_AMM 	   = aMiss.ESERCIZIO
	  		   and mriga.PG_DOC_AMM		  	   = aMiss.PG_MISSIONE
	  		   and mriga.CD_TIPO_DOCUMENTO_AMM = 'MISSIONE'
	  		   and mriga.STATO				   <> 'A' ;
		exception when NO_DATA_FOUND then
			  aNum1 := 0;
			  aNum2 := 0;
		end;

		begin
			 select ant.IM_ANTICIPO into aNum3
			 from anticipo ant
			 where ant.CD_CDS				   = aMiss.CD_CDS_ANTICIPO
  			   and ant.CD_UNITA_ORGANIZZATIVA  = aMiss.CD_UO_ANTICIPO
  			   and ant.ESERCIZIO			   = aMiss.ESERCIZIO_ANTICIPO
  			   and ant.PG_ANTICIPO			   = aMiss.PG_ANTICIPO;
		exception when NO_DATA_FOUND then
			 aNum3 := 0;
		end;

	   -- ciclo sui capitoli associati alla missione
	    for aVoce in (select * from obbligazione_scad_voce obbv
	   	   		 	  where obbv.CD_CDS				  	     = aObblig.CD_CDS_OBBLIGAZIONE
  					    and obbv.ESERCIZIO				     = aObblig.ESERCIZIO_OBBLIGAZIONE
  					    and obbv.ESERCIZIO_ORIGINALE		 = aObblig.ESERCIZIO_ORI_OBBLIGAZIONE
  					    and obbv.PG_OBBLIGAZIONE		  	 = aObblig.PG_OBBLIGAZIONE
  					    and obbv.PG_OBBLIGAZIONE_SCADENZARIO = aObblig.PG_OBBLIGAZIONE_SCADENZARIO) loop
	    -- inizio loop 3

	   	  i := i+1;

		  insert into VPG_MISSIONE (ID,
									CHIAVE,
									SEQUENZA,
									DESCRIZIONE,
									CD_CDS,
									CD_UNITA_ORGANIZZATIVA,
									ESERCIZIO,
									PG_MISSIONE,
									TI_RECORD_L1,
									TI_RECORD_L2,
									FL_RIMBORSO,
									TI_PROVVISORIO_DEFINITIVO,
									FL_ASSOCIATO_COMPENSO,
									ESERCIZIO_ORI_OBBL_ACC,
									PG_OBBL_ACC,
									PG_OBBL_ACC_SCADENZARIO,
									TI_COMPETENZA_RESIDUO,
									DT_SCADENZA,
									IM_ANTICIPO,
									CD_VOCE,
									PG_MAN_REV,
									DS_MODALITA_PAG,
									INTESTAZIONE,
									NUMERO_CONTO,
									CIN,
									ABI,
									CAB,
									IM_TOTALE_MISSIONE,
									IM_DIARIA_LORDA,
									IM_QUOTA_ESENTE,
									IM_DIARIA_NETTO,
									IM_SPESE,
									IM_LORDO_PERCEPIENTE,
									IM_NETTO_PECEPIENTE,
									DS_MISSIONE,
									DT_INIZIO_MISSIONE,
									DT_FINE_MISSIONE,
									IBAN)
		  select   aId,
	 			   'AB2:missione,capitoli',
	 			   i,
	 			   'Stampa RPT',
	 			   aMiss.CD_CDS,
	 			   aMiss.CD_UNITA_ORGANIZZATIVA,
	 			   aMiss.ESERCIZIO,
	 			   aMiss.PG_MISSIONE,
	 			   'A',
	 			   'B2',
	 			   'N',
	 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
	 			   aMiss.FL_ASSOCIATO_COMPENSO,
	 			   aObblig.ESERCIZIO_ORI_OBBLIGAZIONE,
	 			   aObblig.PG_OBBLIGAZIONE,
	 			   aObblig.PG_OBBLIGAZIONE_SCADENZARIO,
	 			   decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R'),
	 			   obbs.DT_SCADENZA,
	 			   aNum3,
	 			   aVoce.CD_VOCE,
	 			   aNum2,
	 			   rif.DS_MODALITA_PAG,
	 			   ban.INTESTAZIONE,
	 			   ban.NUMERO_CONTO,
				   nvl(ban.CIN,' '),
	 			   ban.ABI,
	 			   ban.CAB,
	 			   aMiss.IM_TOTALE_MISSIONE,
	 			   aMiss.IM_DIARIA_LORDA,
	 			   aMiss.IM_QUOTA_ESENTE,
	 			   aMiss.IM_DIARIA_NETTO,
	 			   aMiss.IM_SPESE,
	 			   aMiss.IM_LORDO_PERCEPIENTE,
	 			   aMiss.IM_NETTO_PECEPIENTE,
	 			   aMiss.DS_MISSIONE,
	 			   aMiss.DT_INIZIO_MISSIONE,
	 			   aMiss.DT_FINE_MISSIONE,
	 			   ban.codice_iban
		  from obbligazione obb
			  ,obbligazione_scadenzario obbs
			  ,rif_modalita_pagamento rif
			  ,banca ban
		  where obb.CD_CDS			             = aObblig.CD_CDS_OBBLIGAZIONE
			and obb.ESERCIZIO		   	   	     = aObblig.ESERCIZIO_OBBLIGAZIONE
			and obb.ESERCIZIO_ORIGINALE	   	   	 = aObblig.ESERCIZIO_ORI_OBBLIGAZIONE
  			and obb.PG_OBBLIGAZIONE	   		   	 = aObblig.PG_OBBLIGAZIONE
			and obbs.CD_CDS				   	     = aObblig.CD_CDS_OBBLIGAZIONE
			and obbs.ESERCIZIO		 	   	     = aObblig.ESERCIZIO_OBBLIGAZIONE
			and obbs.ESERCIZIO_ORIGINALE	  	 = aObblig.ESERCIZIO_ORI_OBBLIGAZIONE
			and obbs.PG_OBBLIGAZIONE		  	 = aObblig.PG_OBBLIGAZIONE
			and obbs.PG_OBBLIGAZIONE_SCADENZARIO = aObblig.PG_OBBLIGAZIONE_SCADENZARIO
			and rif.CD_MODALITA_PAG			   	 = aMiss.CD_MODALITA_PAG
			and ban.CD_TERZO					 = aMiss.CD_TERZO
			and ban.PG_BANCA					 = aMiss.PG_BANCA;

		  begin
			  update VPG_MISSIONE vpg
			  set (DS_ABICAB,
				   VIA_BANCA,
				   CAP_BANCA,
				   DS_COMUNE_BANCA,
				   CD_PROVINCIA_BANCA)
			  = (select   abi.DS_ABICAB,
				  		  abi.VIA,
						  abi.CAP,
						  com.DS_COMUNE,
						  com.CD_PROVINCIA
				 from abicab abi
				   	 ,comune com
				 where abi.ABI       = vpg.ABI
				   and abi.CAB	     = vpg.CAB
				   and com.PG_COMUNE = abi.PG_COMUNE)
			  where vpg.CD_CDS 	   	 	       = aMiss.CD_CDS
				and vpg.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
				and vpg.ESERCIZIO			   = aMiss.ESERCIZIO
				and vpg.PG_MISSIONE			   = aMiss.PG_MISSIONE
				and vpg.TI_RECORD_L1		   = 'A'
				and vpg.TI_RECORD_L2		   = 'B2'
				and vpg.SEQUENZA			   = i;
		  exception when NO_DATA_FOUND then
		  			null;
		  end;

	    end loop; -- fine loop 3
	else
	-- missioni con anticipo >= missione
	   	 declare
		 	esisteAnticipo boolean;
		 begin
		 	 begin
				 select * into aAnt
				 from anticipo ant
				 where ant.CD_CDS	              = aMiss.CD_CDS_ANTICIPO
	  			   and ant.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UO_ANTICIPO
	  			   and ant.ESERCIZIO			  = aMiss.ESERCIZIO_ANTICIPO
	  			   and ant.PG_ANTICIPO			  = aMiss.PG_ANTICIPO;
				 esisteAnticipo := true;
			 exception when NO_DATA_FOUND then
			 	 esisteAnticipo := false;
			 end;

			 if esisteAnticipo then
				 begin
				 	  select * into aRim
					  from rimborso rim
					  where rim.CD_CDS_ANTICIPO	   = aAnt.CD_CDS
  				   		and rim.CD_UO_ANTICIPO	   = aAnt.CD_UNITA_ORGANIZZATIVA
  				   		and rim.ESERCIZIO_ANTICIPO = aAnt.ESERCIZIO
  				   		and rim.PG_ANTICIPO	       = aAnt.PG_ANTICIPO;
				  	  insert into VPG_MISSIONE vpg (ID,
													CHIAVE,
													SEQUENZA,
													DESCRIZIONE,
													CD_CDS,
													CD_UNITA_ORGANIZZATIVA,
													ESERCIZIO,
													PG_MISSIONE,
													TI_RECORD_L1,
													TI_RECORD_L2,
													FL_RIMBORSO,
													TI_PROVVISORIO_DEFINITIVO,
													FL_ASSOCIATO_COMPENSO,
													ESERCIZIO_ORI_OBBL_ACC,
													PG_OBBL_ACC,
													PG_OBBL_ACC_SCADENZARIO,
													TI_COMPETENZA_RESIDUO,
													DT_SCADENZA,
													IM_ANTICIPO,
													IM_RIMBORSO,
													CD_VOCE,
													DS_MODALITA_PAG,
													INTESTAZIONE,
													NUMERO_CONTO,
													CIN,
													ABI,
													CAB,
													IM_TOTALE_MISSIONE,
													IM_DIARIA_LORDA,
													IM_QUOTA_ESENTE,
													IM_DIARIA_NETTO,
													IM_SPESE,
													IM_LORDO_PERCEPIENTE,
													IM_NETTO_PECEPIENTE,
													DS_MISSIONE,
													DT_INIZIO_MISSIONE,
													DT_FINE_MISSIONE,
													IBAN)
			  	     select    aId,
				 			   'AB2:missione,capitoli',
				 			   i,
				 			   'Stampa RPT',
				 			   aMiss.CD_CDS,
				 			   aMiss.CD_UNITA_ORGANIZZATIVA,
				 			   aMiss.ESERCIZIO,
				 			   aMiss.PG_MISSIONE,
				 			   'A',
				 			   'B2',
				 			   'Y',
				 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
				 			   aMiss.FL_ASSOCIATO_COMPENSO,
				 			   aRim.ESERCIZIO_ORI_ACCERTAMENTO,
				 			   aRim.PG_ACCERTAMENTO,
				 			   aRim.PG_ACCERTAMENTO_SCADENZARIO,
				 			   decode(acc.ESERCIZIO_ORI_RIPORTO,null,'C','R'),
				 			   accs.DT_SCADENZA_INCASSO,
				 			   aAnt.IM_ANTICIPO,
							   aRim.IM_RIMBORSO,
				 			   acc.CD_VOCE,
				 			   rif.DS_MODALITA_PAG,
				 			   ban.INTESTAZIONE,
				 			   ban.NUMERO_CONTO,
							   nvl(ban.CIN,' '),
				 			   ban.ABI,
				 			   ban.CAB,
				 			   aMiss.IM_TOTALE_MISSIONE,
				 			   aMiss.IM_DIARIA_LORDA,
				 			   aMiss.IM_QUOTA_ESENTE,
				 			   aMiss.IM_DIARIA_NETTO,
				 			   aMiss.IM_SPESE,
				 			   aMiss.IM_LORDO_PERCEPIENTE,
				 			   aMiss.IM_NETTO_PECEPIENTE,
				 			   aMiss.DS_MISSIONE,
				 			   aMiss.DT_INIZIO_MISSIONE,
				 			   aMiss.DT_FINE_MISSIONE,
	 			                          ban.codice_iban
			         from accertamento acc
				 	 	 ,accertamento_scadenzario accs
					 	 ,rif_modalita_pagamento rif
					 	 ,banca ban
     		 		 where acc.CD_CDS	  	   = aRim.CD_CDS_ACCERTAMENTO
	  			       and acc.ESERCIZIO	   = aRim.ESERCIZIO_ACCERTAMENTO
	  			       and acc.ESERCIZIO_ORIGINALE = aRim.ESERCIZIO_ORI_ACCERTAMENTO
	  			   	   and acc.PG_ACCERTAMENTO = aRim.PG_ACCERTAMENTO
				   	   and accs.CD_CDS 				        = aRim.CD_CDS_ACCERTAMENTO
	  			   	   and accs.ESERCIZIO			        = aRim.ESERCIZIO_ACCERTAMENTO
	  			   	   and accs.ESERCIZIO_ORIGINALE = aRim.ESERCIZIO_ORI_ACCERTAMENTO
	  			   	   and accs.PG_ACCERTAMENTO		        = aRim.PG_ACCERTAMENTO
	  			   	   and accs.PG_ACCERTAMENTO_SCADENZARIO = aRim.PG_ACCERTAMENTO_SCADENZARIO
				   	   and rif.CD_MODALITA_PAG				= aMiss.CD_MODALITA_PAG
				   	   and ban.CD_TERZO						= aMiss.CD_TERZO
				   	   and ban.PG_BANCA						= aMiss.PG_BANCA;

					 begin
						  update VPG_MISSIONE vpg
						  set (PG_MAN_REV)
						  = (select   rriga.PG_REVERSALE
							 from reversale_riga rriga
							 where rriga.CD_CDS_DOC_AMM	  	  	    = aRim.CD_CDS
		  			   		   and rriga.CD_UO_DOC_AMM	  	  	    = aRim.CD_UNITA_ORGANIZZATIVA
		  			   		   and rriga.ESERCIZIO_DOC_AMM 	  	    = aRim.ESERCIZIO
		  			   		   and rriga.PG_DOC_AMM		  	  	    = aRim.PG_RIMBORSO
		  			   		   and rriga.CD_TIPO_DOCUMENTO_AMM 	    = 'RIMBORSO'
		  			   		   and rriga.STATO				  	    <> 'A')
						  where vpg.CD_CDS 	   	 	       = aMiss.CD_CDS
							and vpg.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
							and vpg.ESERCIZIO			   = aMiss.ESERCIZIO
							and vpg.PG_MISSIONE			   = aMiss.PG_MISSIONE
							and vpg.TI_RECORD_L1		   = 'A'
							and vpg.TI_RECORD_L2		   = 'B2'
							and vpg.SEQUENZA			   = i;
					  exception when NO_DATA_FOUND then
					  			null;
					  end;

				 exception when NO_DATA_FOUND then  -- non esiste rimborso
			    	 insert into VPG_MISSIONE vpg  (ID,
													CHIAVE,
													SEQUENZA,
													DESCRIZIONE,
													CD_CDS,
													CD_UNITA_ORGANIZZATIVA,
													ESERCIZIO,
													PG_MISSIONE,
													TI_RECORD_L1,
													TI_RECORD_L2,
													FL_RIMBORSO,
													TI_PROVVISORIO_DEFINITIVO,
													FL_ASSOCIATO_COMPENSO,
													IM_ANTICIPO,
													DS_MODALITA_PAG,
													INTESTAZIONE,
													NUMERO_CONTO,
													CIN,
													ABI,
													CAB,
													IM_TOTALE_MISSIONE,
													IM_DIARIA_LORDA,
													IM_QUOTA_ESENTE,
													IM_DIARIA_NETTO,
													IM_SPESE,
													IM_LORDO_PERCEPIENTE,
													IM_NETTO_PECEPIENTE,
													DS_MISSIONE,
													DT_INIZIO_MISSIONE,
													DT_FINE_MISSIONE,
													IBAN)
				  	 select    aId,
				 			   'AB2:missione,capitoli',
				 			   i,
				 			   'Stampa RPT',
				 			   aMiss.CD_CDS,
				 			   aMiss.CD_UNITA_ORGANIZZATIVA,
				 			   aMiss.ESERCIZIO,
				 			   aMiss.PG_MISSIONE,
				 			   'A',
				 			   'B2',
				 			   'Y',
				 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
				 			   aMiss.FL_ASSOCIATO_COMPENSO,
				 			   aAnt.IM_ANTICIPO,
				 			   rif.DS_MODALITA_PAG,
				 			   ban.INTESTAZIONE,
				 			   ban.NUMERO_CONTO,
							   nvl(ban.CIN,' '),
				 			   ban.ABI,
				 			   ban.CAB,
				 			   aMiss.IM_TOTALE_MISSIONE,
				 			   aMiss.IM_DIARIA_LORDA,
				 			   aMiss.IM_QUOTA_ESENTE,
				 			   aMiss.IM_DIARIA_NETTO,
				 			   aMiss.IM_SPESE,
				 			   aMiss.IM_LORDO_PERCEPIENTE,
				 			   aMiss.IM_NETTO_PECEPIENTE,
				 			   aMiss.DS_MISSIONE,
				 			   aMiss.DT_INIZIO_MISSIONE,
				 			   aMiss.DT_FINE_MISSIONE,
	 			                          ban.codice_iban
				     from rif_modalita_pagamento rif
						 ,banca ban
				     where rif.CD_MODALITA_PAG				= aMiss.CD_MODALITA_PAG
					   and ban.CD_TERZO						= aMiss.CD_TERZO
					   and ban.PG_BANCA						= aMiss.PG_BANCA;

				 end;
			 else -- non esiste anticipo
		    	 insert into VPG_MISSIONE vpg  (ID,
												CHIAVE,
												SEQUENZA,
												DESCRIZIONE,
												CD_CDS,
												CD_UNITA_ORGANIZZATIVA,
												ESERCIZIO,
												PG_MISSIONE,
												TI_RECORD_L1,
												TI_RECORD_L2,
												FL_RIMBORSO,
												TI_PROVVISORIO_DEFINITIVO,
												FL_ASSOCIATO_COMPENSO,
												DS_MODALITA_PAG,
												INTESTAZIONE,
												NUMERO_CONTO,
												CIN,
												ABI,
												CAB,
												IM_TOTALE_MISSIONE,
												IM_DIARIA_LORDA,
												IM_QUOTA_ESENTE,
												IM_DIARIA_NETTO,
												IM_SPESE,
												IM_LORDO_PERCEPIENTE,
												IM_NETTO_PECEPIENTE,
												DS_MISSIONE,
												DT_INIZIO_MISSIONE,
												DT_FINE_MISSIONE,
												IBAN)
			  	 select    aId,
			 			   'AB2:missione,capitoli',
			 			   i,
			 			   'Stampa RPT',
			 			   aMiss.CD_CDS,
			 			   aMiss.CD_UNITA_ORGANIZZATIVA,
			 			   aMiss.ESERCIZIO,
			 			   aMiss.PG_MISSIONE,
			 			   'A',
			 			   'B2',
			 			   'Y',
			 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
			 			   aMiss.FL_ASSOCIATO_COMPENSO,
			 			   rif.DS_MODALITA_PAG,
			 			   ban.INTESTAZIONE,
			 			   ban.NUMERO_CONTO,
						   nvl(ban.CIN,' '),
			 			   ban.ABI,
			 			   ban.CAB,
			 			   aMiss.IM_TOTALE_MISSIONE,
			 			   aMiss.IM_DIARIA_LORDA,
			 			   aMiss.IM_QUOTA_ESENTE,
			 			   aMiss.IM_DIARIA_NETTO,
			 			   aMiss.IM_SPESE,
			 			   aMiss.IM_LORDO_PERCEPIENTE,
			 			   aMiss.IM_NETTO_PECEPIENTE,
			 			   aMiss.DS_MISSIONE,
			 			   aMiss.DT_INIZIO_MISSIONE,
			 			   aMiss.DT_FINE_MISSIONE,
	 			                   ban.codice_iban
			     from rif_modalita_pagamento rif
					 ,banca ban
			     where rif.CD_MODALITA_PAG				= aMiss.CD_MODALITA_PAG
				   and ban.CD_TERZO						= aMiss.CD_TERZO
				   and ban.PG_BANCA						= aMiss.PG_BANCA;

			end if;

		    begin
			     update VPG_MISSIONE vpg
			     set (DS_ABICAB,
				      VIA_BANCA,
				      CAP_BANCA,
				      DS_COMUNE_BANCA,
				      CD_PROVINCIA_BANCA)
			     = (select   abi.DS_ABICAB,
				    	     abi.VIA,
						  	 abi.CAP,
						  	 com.DS_COMUNE,
						  	 com.CD_PROVINCIA
				    from abicab abi
				   	    ,comune com
				 	where abi.ABI       = vpg.ABI
				      and abi.CAB	     = vpg.CAB
				   	  and com.PG_COMUNE = abi.PG_COMUNE)
			     where vpg.CD_CDS 	   	 	       = aMiss.CD_CDS
				   and vpg.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
				   and vpg.ESERCIZIO			   = aMiss.ESERCIZIO
				   and vpg.PG_MISSIONE			   = aMiss.PG_MISSIONE
				   and vpg.TI_RECORD_L1		   = 'A'
				   and vpg.TI_RECORD_L2		   = 'B2'
				   and vpg.SEQUENZA			   = i;
		    exception when NO_DATA_FOUND then
		  			null;
		  	end;

		end; -- fine blocco locale


	end if; -- fine distinzione missioni con/senza rimborso

	-- fine inserimento record (A,B2) capitoli della missione

 	else
	-- missioni associate a compenso

		 select * into aComp
		 from compenso comp
		 where comp.CD_CDS_MISSIONE		  = aMiss.CD_CDS
 			   and comp.ESERCIZIO_MISSIONE	  = aMiss.ESERCIZIO
 			   and comp.PG_MISSIONE			  = aMiss.PG_MISSIONE
 			   and comp.CD_UO_MISSIONE		  = aMiss.CD_UNITA_ORGANIZZATIVA
 			   and comp.STATO_COFI			  <> 'A' ;

		 begin
		 	  select mriga.ESERCIZIO, mriga.PG_MANDATO into aNum1, aNum2
			  from mandato_riga mriga
			  where mriga.CD_CDS_DOC_AMM	  	= aComp.CD_CDS
  		   	    and mriga.CD_UO_DOC_AMM	  	    = aComp.CD_UNITA_ORGANIZZATIVA
  		   		and mriga.ESERCIZIO_DOC_AMM 	= aComp.ESERCIZIO
  		   		and mriga.PG_DOC_AMM		  	= aComp.PG_COMPENSO
  		   		and mriga.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO'
  		   		and mriga.STATO				   <> 'A' ;
		 exception when NO_DATA_FOUND then
		 		   aNum1 := 0;
				   aNum2 := 0;
		 end;

		 begin
			 select ant.IM_ANTICIPO into aNum3
			 from anticipo ant
			 where ant.CD_CDS				   = aMiss.CD_CDS_ANTICIPO
  			   and ant.CD_UNITA_ORGANIZZATIVA  = aMiss.CD_UO_ANTICIPO
  			   and ant.ESERCIZIO			   = aMiss.ESERCIZIO_ANTICIPO
  			   and ant.PG_ANTICIPO			   = aMiss.PG_ANTICIPO;
		 exception when NO_DATA_FOUND then
			 aNum3 := 0;
		 end;


	--	inizio inserimento record (A,A) testata
                 i := i+1;
		 insert into VPG_MISSIONE  (ID,
									CHIAVE,
									SEQUENZA,
									DESCRIZIONE,
									CD_CDS,
									CD_UNITA_ORGANIZZATIVA,
									ESERCIZIO,
									PG_MISSIONE,
									TI_RECORD_L1,
									TI_RECORD_L2,
									TI_PROVVISORIO_DEFINITIVO,
									ES_FINANZIARIO,
									DS_UNITA_ORGANIZZATIVA,
									FL_ASSOCIATO_COMPENSO,
									NOME,
									COGNOME,
									CD_TERZO,
									VIA_SEDE,
									NUMERO_CIVICO_SEDE,
									CAP_COMUNE_SEDE,
									TI_ANAGRAFICO,
									ESERCIZIO_ORI_OBBL_ACC,
									PG_OBBL_ACC,
									PG_OBBL_ACC_SCADENZARIO,
									PG_MAN_REV,
									MATRICOLA,
									QUALIFICA,
									DT_REGISTRAZIONE)
		 select aId,
			    'AA:Testata',
			    i,
			    'Stampa RPT',
			    aMiss.CD_CDS,
			    aMiss.CD_UNITA_ORGANIZZATIVA,
			    aMiss.ESERCIZIO,
			    aMiss.PG_MISSIONE,
			    'A',
			    'A',
			    aMiss.TI_PROVVISORIO_DEFINITIVO,
			    aNum1,
			    uo.DS_UNITA_ORGANIZZATIVA,
			    aMiss.FL_ASSOCIATO_COMPENSO,
			    aMiss.NOME,
			    aMiss.COGNOME,
			    aMiss.CD_TERZO,
			    ter.VIA_SEDE,
			    ter.NUMERO_CIVICO_SEDE,
			    ter.CAP_COMUNE_SEDE,
			    aMiss.TI_ANAGRAFICO,
			    aComp.ESERCIZIO_ORI_OBBLIGAZIONE,
			    aComp.PG_OBBLIGAZIONE,
			    aComp.PG_OBBLIGAZIONE_SCADENZARIO,
			    aNum2,
			    decode(aMiss.TI_ANAGRAFICO,'D',rap.MATRICOLA_DIPENDENTE,aMiss.CD_TERZO),
			    rif.DS_INQUADRAMENTO,
			    aMiss.DT_REGISTRAZIONE
		 from unita_organizzativa uo
		     ,terzo ter
			 ,rif_inquadramento rif
			 ,rapporto rap
		 where uo.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
		   and ter.CD_TERZO				 = aMiss.CD_TERZO
		   and rif.PG_RIF_INQUADRAMENTO  = aMiss.PG_RIF_INQUADRAMENTO
		   and rap.CD_TIPO_RAPPORTO 	 = aMiss.CD_TIPO_RAPPORTO
		   and rap.CD_ANAG		   		 = ter.CD_ANAG
		   and rap.DT_INI_VALIDITA 		 <= Trunc(aMiss.DT_INIZIO_MISSIONE)
		   and rap.DT_FIN_VALIDITA 		 >= Trunc(aMiss.DT_INIZIO_MISSIONE)
		   And Rownum <2;

		select ter.PG_COMUNE_SEDE into aNum1
		from terzo ter
		where ter.CD_TERZO = aMiss.CD_TERZO;

		update VPG_MISSIONE vpg
		set (DS_COMUNE,
			 CD_PROVINCIA)
		= (select com.DS_COMUNE
		  		 ,com.CD_PROVINCIA
		   from comune com
		   where com.PG_COMUNE = aNum1)
		where vpg.CD_CDS 	   	 	     = aMiss.CD_CDS
		  and vpg.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
		  and vpg.ESERCIZIO				 = aMiss.ESERCIZIO
		  and vpg.PG_MISSIONE			 = aMiss.PG_MISSIONE
		  and vpg.TI_RECORD_L1			 = 'A'
		  and vpg.TI_RECORD_L2			 = 'A'
		  and vpg.SEQUENZA				 = i;

	--	fine inserimento record (A,A) testata

	-- inizio inserimento record (A,B2) missione + capitoli

	   	 if aComp.PG_OBBLIGAZIONE is not null then
		 -- missioni con anticipo < missione => no rimborso

			-- ciclo sui capitoli
			for aVoce in (select * from obbligazione_scad_voce obbv
					  	  where obbv.CD_CDS		     = aComp.CD_CDS_OBBLIGAZIONE
						    and obbv.ESERCIZIO		     = aComp.ESERCIZIO_OBBLIGAZIONE
						    and obbv.ESERCIZIO_ORIGINALE     = aComp.ESERCIZIO_ORI_OBBLIGAZIONE
							and obbv.PG_OBBLIGAZIONE     = aComp.PG_OBBLIGAZIONE
  							and obbv.PG_OBBLIGAZIONE_SCADENZARIO = aComp.PG_OBBLIGAZIONE_SCADENZARIO) loop
			-- inizio loop 4

			   i := i+1;

		  	   insert into VPG_MISSIONE (ID,
										CHIAVE,
										SEQUENZA,
										DESCRIZIONE,
										CD_CDS,
										CD_UNITA_ORGANIZZATIVA,
										ESERCIZIO,
										PG_MISSIONE,
										TI_RECORD_L1,
										TI_RECORD_L2,
										FL_RIMBORSO,
										TI_PROVVISORIO_DEFINITIVO,
										FL_ASSOCIATO_COMPENSO,
										ESERCIZIO_ORI_OBBL_ACC,
										PG_OBBL_ACC,
										PG_OBBL_ACC_SCADENZARIO,
										TI_COMPETENZA_RESIDUO,
										DT_SCADENZA,
										PG_COMPENSO,
										IM_CR_ENTE,
										IM_ANTICIPO,
										CD_VOCE,
										PG_MAN_REV,
										DS_MODALITA_PAG,
										INTESTAZIONE,
										NUMERO_CONTO,
										CIN,
										ABI,
										CAB,
										IM_TOTALE_MISSIONE,
										IM_DIARIA_LORDA,
										IM_QUOTA_ESENTE,
										IM_DIARIA_NETTO,
										IM_SPESE,
										IM_LORDO_PERCEPIENTE,
										IM_NETTO_PECEPIENTE,
										DS_MISSIONE,
										DT_INIZIO_MISSIONE,
										DT_FINE_MISSIONE,
										IBAN)
			   select  aId,
		 			   'AB2:missione,capitoli',
		 			   i,
		 			   'Stampa RPT',
		 			   aMiss.CD_CDS,
		 			   aMiss.CD_UNITA_ORGANIZZATIVA,
		 			   aMiss.ESERCIZIO,
		 			   aMiss.PG_MISSIONE,
		 			   'A',
		 			   'B2',
		 			   'N',
		 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
		 			   aMiss.FL_ASSOCIATO_COMPENSO,
		 			   aComp.ESERCIZIO_ORI_OBBLIGAZIONE,
		 			   aComp.PG_OBBLIGAZIONE,
		 			   aComp.PG_OBBLIGAZIONE_SCADENZARIO,
		 			   decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R'),
		 			   obbs.DT_SCADENZA,
					   aComp.PG_COMPENSO,
					   aComp.IM_CR_ENTE,
		 			   aNum3,
		 			   aVoce.CD_VOCE,
		 			   aNum2,
		 			   rif.DS_MODALITA_PAG,
		 			   ban.INTESTAZIONE,
		 			   ban.NUMERO_CONTO,
					   nvl(ban.CIN,' '),
		 			   ban.ABI,
		 			   ban.CAB,
		 			   aMiss.IM_TOTALE_MISSIONE,
		 			   aMiss.IM_DIARIA_LORDA,
		 			   aMiss.IM_QUOTA_ESENTE,
		 			   aMiss.IM_DIARIA_NETTO,
		 			   aMiss.IM_SPESE,
		 			   aMiss.IM_LORDO_PERCEPIENTE,
		 			   aMiss.IM_NETTO_PECEPIENTE,
		 			   aMiss.DS_MISSIONE,
		 			   aMiss.DT_INIZIO_MISSIONE,
		 			   aMiss.DT_FINE_MISSIONE,
	 			           ban.codice_iban
			   from obbligazione obb
				   ,obbligazione_scadenzario obbs
				   ,rif_modalita_pagamento rif
				   ,banca ban
			   where obb.CD_CDS			          = aComp.CD_CDS_OBBLIGAZIONE
				 and obb.ESERCIZIO		   	  = aComp.ESERCIZIO_OBBLIGAZIONE
				 and obb.ESERCIZIO_ORIGINALE  	   	  = aComp.ESERCIZIO_ORI_OBBLIGAZIONE
	  			 and obb.PG_OBBLIGAZIONE		  = aComp.PG_OBBLIGAZIONE
				 and obbs.CD_CDS			  = aComp.CD_CDS_OBBLIGAZIONE
				 and obbs.ESERCIZIO		   	  = aComp.ESERCIZIO_OBBLIGAZIONE
				 and obbs.ESERCIZIO_ORIGINALE	   	  = aComp.ESERCIZIO_ORI_OBBLIGAZIONE
				 and obbs.PG_OBBLIGAZIONE         	  = aComp.PG_OBBLIGAZIONE
				 and obbs.PG_OBBLIGAZIONE_SCADENZARIO = aComp.PG_OBBLIGAZIONE_SCADENZARIO
				 and rif.CD_MODALITA_PAG			  = aMiss.CD_MODALITA_PAG
				 and ban.CD_TERZO					  = aMiss.CD_TERZO
				 and ban.PG_BANCA					  = aMiss.PG_BANCA;

			   begin
				  update VPG_MISSIONE vpg
				  set (DS_ABICAB,
					   VIA_BANCA,
					   CAP_BANCA,
					   DS_COMUNE_BANCA,
					   CD_PROVINCIA_BANCA)
				  = (select   abi.DS_ABICAB,
					  		  abi.VIA,
							  abi.CAP,
							  com.DS_COMUNE,
							  com.CD_PROVINCIA
					 from abicab abi
					   	 ,comune com
					 where abi.ABI       = vpg.ABI
					   and abi.CAB	     = vpg.CAB
					   and com.PG_COMUNE = abi.PG_COMUNE)
				  where vpg.CD_CDS 	   	 	       = aMiss.CD_CDS
					and vpg.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
					and vpg.ESERCIZIO			   = aMiss.ESERCIZIO
					and vpg.PG_MISSIONE			   = aMiss.PG_MISSIONE
					and vpg.TI_RECORD_L1		   = 'A'
					and vpg.TI_RECORD_L2		   = 'B2'
					and vpg.SEQUENZA			   = i;
			   exception when NO_DATA_FOUND then
			  			null;
			   end;

			end loop; -- fine loop 4

		 else
		 -- missioni con anticipo > missione => rimborso

	 		declare
	 				aAnt anticipo%rowtype;
	 				aRim rimborso%rowtype;
	 		begin

	 			 begin
					 select * into aAnt
	 				 from anticipo ant
	 				 where ant.CD_CDS	              = aMiss.CD_CDS_ANTICIPO
	 	  			   and ant.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UO_ANTICIPO
	 	  			   and ant.ESERCIZIO			  = aMiss.ESERCIZIO_ANTICIPO
	 	  			   and ant.PG_ANTICIPO			  = aMiss.PG_ANTICIPO;

	 				 select * into aRim
	 				 from rimborso rim
	 				 where rim.CD_CDS_ANTICIPO	  = aAnt.CD_CDS
	   				   and rim.CD_UO_ANTICIPO	  = aAnt.CD_UNITA_ORGANIZZATIVA
	   				   and rim.ESERCIZIO_ANTICIPO = aAnt.ESERCIZIO
	   				   and rim.PG_ANTICIPO	      = aAnt.PG_ANTICIPO;

		 	    	 insert into VPG_MISSIONE vpg  (ID,
		 											CHIAVE,
		 											SEQUENZA,
		 											DESCRIZIONE,
		 											CD_CDS,
		 											CD_UNITA_ORGANIZZATIVA,
		 											ESERCIZIO,
		 											PG_MISSIONE,
		 											TI_RECORD_L1,
		 											TI_RECORD_L2,
		 											FL_RIMBORSO,
		 											TI_PROVVISORIO_DEFINITIVO,
		 											FL_ASSOCIATO_COMPENSO,
		 											ESERCIZIO_ORI_OBBL_ACC,
		 											PG_OBBL_ACC,
		 											PG_OBBL_ACC_SCADENZARIO,
		 											TI_COMPETENZA_RESIDUO,
													PG_COMPENSO,
		 											DT_SCADENZA,
		 											IM_ANTICIPO,
													IM_RIMBORSO,
		 											CD_VOCE,
		 											DS_MODALITA_PAG,
		 											INTESTAZIONE,
		 											NUMERO_CONTO,
													CIN,
		 											ABI,
		 											CAB,
		 											IM_TOTALE_MISSIONE,
		 											IM_DIARIA_LORDA,
		 											IM_QUOTA_ESENTE,
		 											IM_DIARIA_NETTO,
		 											IM_SPESE,
		 											IM_LORDO_PERCEPIENTE,
		 											IM_NETTO_PECEPIENTE,
		 											DS_MISSIONE,
		 											DT_INIZIO_MISSIONE,
		 											DT_FINE_MISSIONE,
		 											IBAN)
		 		  	 select    aId,
		 		 			   'AB2:missione,capitoli',
		 		 			   i,
		 		 			   'Stampa RPT',
		 		 			   aMiss.CD_CDS,
		 		 			   aMiss.CD_UNITA_ORGANIZZATIVA,
		 		 			   aMiss.ESERCIZIO,
		 		 			   aMiss.PG_MISSIONE,
		 		 			   'A',
		 		 			   'B2',
		 		 			   'Y',
		 		 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
		 		 			   aMiss.FL_ASSOCIATO_COMPENSO,
		   	  			   	   aRim.ESERCIZIO_ORI_ACCERTAMENTO,
		 		 			   aRim.PG_ACCERTAMENTO,
		 		 			   aRim.PG_ACCERTAMENTO_SCADENZARIO,
		 		 			   decode(acc.ESERCIZIO_ORI_RIPORTO,null,'C','R'),
							   aComp.PG_COMPENSO,
		 		 			   accs.DT_SCADENZA_INCASSO,
		 		 			   aAnt.IM_ANTICIPO,
							   aRim.IM_RIMBORSO,
		 		 			   acc.CD_VOCE,
		 		 			   rif.DS_MODALITA_PAG,
		 		 			   ban.INTESTAZIONE,
		 		 			   ban.NUMERO_CONTO,
							   nvl(ban.CIN,' '),
		 		 			   ban.ABI,
		 		 			   ban.CAB,
		 		 			   aMiss.IM_TOTALE_MISSIONE,
		 		 			   aMiss.IM_DIARIA_LORDA,
		 		 			   aMiss.IM_QUOTA_ESENTE,
		 		 			   aMiss.IM_DIARIA_NETTO,
		 		 			   aMiss.IM_SPESE,
		 		 			   aMiss.IM_LORDO_PERCEPIENTE,
		 		 			   aMiss.IM_NETTO_PECEPIENTE,
		 		 			   aMiss.DS_MISSIONE,
		 		 			   aMiss.DT_INIZIO_MISSIONE,
		 		 			   aMiss.DT_FINE_MISSIONE,
	 			                           ban.codice_iban
		 		     from accertamento acc
		 			 	 ,accertamento_scadenzario accs
		 				 ,rif_modalita_pagamento rif
		 				 ,banca ban
		 		     where acc.CD_CDS	  	    = aRim.CD_CDS_ACCERTAMENTO
		   			   and acc.ESERCIZIO	    = aRim.ESERCIZIO
	  			   	   and acc.ESERCIZIO_ORIGINALE = aRim.ESERCIZIO_ORI_ACCERTAMENTO
		   			   and acc.PG_ACCERTAMENTO  = aRim.PG_ACCERTAMENTO
		 			   and accs.CD_CDS 				        = aRim.CD_CDS_ACCERTAMENTO
		   			   and accs.ESERCIZIO			        = aRim.ESERCIZIO
	  			   	   and accs.ESERCIZIO_ORIGINALE = aRim.ESERCIZIO_ORI_ACCERTAMENTO
		   			   and accs.PG_ACCERTAMENTO		        = aRim.PG_ACCERTAMENTO
		   			   and accs.PG_ACCERTAMENTO_SCADENZARIO = aRim.PG_ACCERTAMENTO_SCADENZARIO
		 			   and rif.CD_MODALITA_PAG				= aMiss.CD_MODALITA_PAG
		 			   and ban.CD_TERZO						= aMiss.CD_TERZO
		 			   and ban.PG_BANCA						= aMiss.PG_BANCA;

				 begin
					  update VPG_MISSIONE vpg
					  set (PG_MAN_REV)
					  = (select   rriga.PG_REVERSALE
						 from reversale_riga rriga
						 where rriga.CD_CDS_DOC_AMM	  	  	    = aRim.CD_CDS
	  			   		   and rriga.CD_UO_DOC_AMM	  	  	    = aRim.CD_UNITA_ORGANIZZATIVA
	  			   		   and rriga.ESERCIZIO_DOC_AMM 	  	    = aRim.ESERCIZIO
	  			   		   and rriga.PG_DOC_AMM		  	  	    = aRim.PG_RIMBORSO
	  			   		   and rriga.CD_TIPO_DOCUMENTO_AMM 	    = 'RIMBORSO'
	  			   		   and rriga.STATO				  	    <> 'A')
					  where vpg.CD_CDS 	   	 	       = aMiss.CD_CDS
						and vpg.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
						and vpg.ESERCIZIO			   = aMiss.ESERCIZIO
						and vpg.PG_MISSIONE			   = aMiss.PG_MISSIONE
						and vpg.TI_RECORD_L1		   = 'A'
						and vpg.TI_RECORD_L2		   = 'B2'
						and vpg.SEQUENZA			   = i;
				  exception when NO_DATA_FOUND then
				  			null;
				  end;



	 			 exception when NO_DATA_FOUND then

		 	    	 insert into VPG_MISSIONE vpg  (ID,
		 											CHIAVE,
		 											SEQUENZA,
		 											DESCRIZIONE,
		 											CD_CDS,
		 											CD_UNITA_ORGANIZZATIVA,
		 											ESERCIZIO,
		 											PG_MISSIONE,
		 											TI_RECORD_L1,
		 											TI_RECORD_L2,
		 											FL_RIMBORSO,
		 											TI_PROVVISORIO_DEFINITIVO,
		 											FL_ASSOCIATO_COMPENSO,
													PG_COMPENSO,
		 											DS_MODALITA_PAG,
		 											INTESTAZIONE,
		 											NUMERO_CONTO,
													CIN,
		 											ABI,
		 											CAB,
		 											IM_TOTALE_MISSIONE,
		 											IM_DIARIA_LORDA,
		 											IM_QUOTA_ESENTE,
		 											IM_DIARIA_NETTO,
		 											IM_SPESE,
		 											IM_LORDO_PERCEPIENTE,
		 											IM_NETTO_PECEPIENTE,
		 											DS_MISSIONE,
		 											DT_INIZIO_MISSIONE,
		 											DT_FINE_MISSIONE,
		 											IM_ANTICIPO,
		 											IBAN)
		 		  	 select    aId,
		 		 			   'AB2:missione,capitoli',
		 		 			   i,
		 		 			   'Stampa RPT',
		 		 			   aMiss.CD_CDS,
		 		 			   aMiss.CD_UNITA_ORGANIZZATIVA,
		 		 			   aMiss.ESERCIZIO,
		 		 			   aMiss.PG_MISSIONE,
		 		 			   'A',
		 		 			   'B2',
		 		 			   'Y',
		 		 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
		 		 			   aMiss.FL_ASSOCIATO_COMPENSO,
							   aComp.PG_COMPENSO,
		 		 			   rif.DS_MODALITA_PAG,
		 		 			   ban.INTESTAZIONE,
		 		 			   ban.NUMERO_CONTO,
							   nvl(ban.CIN,' '),
		 		 			   ban.ABI,
		 		 			   ban.CAB,
		 		 			   aMiss.IM_TOTALE_MISSIONE,
		 		 			   aMiss.IM_DIARIA_LORDA,
		 		 			   aMiss.IM_QUOTA_ESENTE,
		 		 			   aMiss.IM_DIARIA_NETTO,
		 		 			   aMiss.IM_SPESE,
		 		 			   aMiss.IM_LORDO_PERCEPIENTE,
		 		 			   aMiss.IM_NETTO_PECEPIENTE,
		 		 			   aMiss.DS_MISSIONE,
		 		 			   aMiss.DT_INIZIO_MISSIONE,
		 		 			   aMiss.DT_FINE_MISSIONE,
		 		 			   aAnt.IM_ANTICIPO,
	 			                          ban.codice_iban
		 		     from rif_modalita_pagamento rif
		 				 ,banca ban
		 		     where rif.CD_MODALITA_PAG				= aMiss.CD_MODALITA_PAG
		 			   and ban.CD_TERZO						= aMiss.CD_TERZO
		 			   and ban.PG_BANCA						= aMiss.PG_BANCA;

	 			 end;


			     begin
	 			     update VPG_MISSIONE vpg
	 			     set (DS_ABICAB,
	 				      VIA_BANCA,
	 				      CAP_BANCA,
	 				      DS_COMUNE_BANCA,
	 				      CD_PROVINCIA_BANCA)
	 			     = (select   abi.DS_ABICAB,
	 				    	     abi.VIA,
	 						  	 abi.CAP,
	 						  	 com.DS_COMUNE,
	 						  	 com.CD_PROVINCIA
	 				    from abicab abi
	 				   	    ,comune com
	 				 	where abi.ABI       = vpg.ABI
	 				      and abi.CAB	    = vpg.CAB
	 				   	  and com.PG_COMUNE = abi.PG_COMUNE)
	 			     where vpg.CD_CDS 	   	 	       = aMiss.CD_CDS
	 				   and vpg.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
	 				   and vpg.ESERCIZIO			   = aMiss.ESERCIZIO
	 				   and vpg.PG_MISSIONE			   = aMiss.PG_MISSIONE
	 				   and vpg.TI_RECORD_L1		   = 'A'
	 				   and vpg.TI_RECORD_L2		   = 'B2'
	 				   and vpg.SEQUENZA			   = i;
	 		     exception when NO_DATA_FOUND then
	 		  			null;
			  	 end;

	 		end; -- fine blocco locale

		 end if; -- fine distinzione con/senza rimborso

	-- fine inserimento record (A,B2) missione + capitoli

	     -- loop sui cori
		 for aCori in (select * from contributo_ritenuta cori
		 	 	   	   where cori.CD_CDS				   = aComp.CD_CDS
  					     and cori.CD_UNITA_ORGANIZZATIVA   = aComp.CD_UNITA_ORGANIZZATIVA
  						 and cori.ESERCIZIO				   = aComp.ESERCIZIO
  						 and cori.PG_COMPENSO			   = aComp.PG_COMPENSO) loop
		 -- inizio loop 5

		 	  i := i+1;

		 -- inizio inserimento record (A,C) cori del compenso associato alla missione
			  insert into VPG_MISSIONE (ID,
										CHIAVE,
										SEQUENZA,
										DESCRIZIONE,
										CD_CDS,
										CD_UNITA_ORGANIZZATIVA,
										ESERCIZIO,
										PG_MISSIONE,
										TI_RECORD_L1,
										TI_RECORD_L2,
										TI_PROVVISORIO_DEFINITIVO,
										FL_ASSOCIATO_COMPENSO,
										ESERCIZIO_ORI_OBBL_ACC,
		 								PG_OBBL_ACC,
										PG_OBBL_ACC_SCADENZARIO,
										PG_COMPENSO,
										DS_MODALITA_PAG,
										INTESTAZIONE,
										NUMERO_CONTO,
										CIN,
										ABI,
										CAB,
										IM_TOTALE_MISSIONE,
										IM_DIARIA_LORDA,
										IM_QUOTA_ESENTE,
										IM_DIARIA_NETTO,
										IM_SPESE,
										IM_LORDO_PERCEPIENTE,
										IM_NETTO_PECEPIENTE,
										DS_MISSIONE,
										DT_INIZIO_MISSIONE,
										DT_FINE_MISSIONE,
										CD_CONTRIBUTO_RITENUTA,
										TI_ENTE_PERCIPIENTE,
										AMMONTARE,
										IBAN)
			  select   aId,
		 			   'AC:CORI',
		 			   i,
		 			   'Stampa RPT',
		 			   aMiss.CD_CDS,
		 			   aMiss.CD_UNITA_ORGANIZZATIVA,
		 			   aMiss.ESERCIZIO,
		 			   aMiss.PG_MISSIONE,
		 			   'A',
		 			   'C',
		 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
		 			   aMiss.FL_ASSOCIATO_COMPENSO,
		 			   aComp.ESERCIZIO_ORI_OBBLIGAZIONE,
		 			   aComp.PG_OBBLIGAZIONE,
		 			   aComp.PG_OBBLIGAZIONE_SCADENZARIO,
					   aComp.PG_COMPENSO,
		 			   rif.DS_MODALITA_PAG,
		 			   ban.INTESTAZIONE,
		 			   ban.NUMERO_CONTO,
					   nvl(ban.CIN,' '),
		 			   ban.ABI,
		 			   ban.CAB,
		 			   aMiss.IM_TOTALE_MISSIONE,
		 			   aMiss.IM_DIARIA_LORDA,
		 			   aMiss.IM_QUOTA_ESENTE,
		 			   aMiss.IM_DIARIA_NETTO,
		 			   aMiss.IM_SPESE,
		 			   aMiss.IM_LORDO_PERCEPIENTE,
		 			   aMiss.IM_NETTO_PECEPIENTE,
		 			   aMiss.DS_MISSIONE,
		 			   aMiss.DT_INIZIO_MISSIONE,
		 			   aMiss.DT_FINE_MISSIONE,
					   aCori.CD_CONTRIBUTO_RITENUTA,
					   aCori.TI_ENTE_PERCIPIENTE,
					   aCori.AMMONTARE,
	 			           ban.codice_iban
		 	  from rif_modalita_pagamento rif
			  	  ,banca ban
			  where rif.CD_MODALITA_PAG	= aMiss.CD_MODALITA_PAG
			    and ban.CD_TERZO		= aMiss.CD_TERZO
  				and ban.PG_BANCA	    = aMiss.PG_BANCA;

			  begin
	 			     update VPG_MISSIONE vpg
	 			     set (DS_ABICAB,
	 				      VIA_BANCA,
	 				      CAP_BANCA,
	 				      DS_COMUNE_BANCA,
	 				      CD_PROVINCIA_BANCA)
	 			     = (select   abi.DS_ABICAB,
	 				    	     abi.VIA,
	 						  	 abi.CAP,
	 						  	 com.DS_COMUNE,
	 						  	 com.CD_PROVINCIA
	 				    from abicab abi
	 				   	    ,comune com
	 				 	where abi.ABI       = vpg.ABI
	 				      and abi.CAB	    = vpg.CAB
	 				   	  and com.PG_COMUNE = abi.PG_COMUNE)
	 			     where vpg.CD_CDS 	   	 	       = aMiss.CD_CDS
	 				   and vpg.CD_UNITA_ORGANIZZATIVA  = aMiss.CD_UNITA_ORGANIZZATIVA
	 				   and vpg.ESERCIZIO			   = aMiss.ESERCIZIO
	 				   and vpg.PG_MISSIONE			   = aMiss.PG_MISSIONE
	 				   and vpg.TI_RECORD_L1		   = 'A'
	 				   and vpg.TI_RECORD_L2		   = 'C'
	 				   and vpg.SEQUENZA			   = i;
	 		  exception when NO_DATA_FOUND then
	 		  			null;
			  end;

		 	-- fine inserimento record (A,C) cori del compenso associato alla missione

			-- inizio inserimento record (E,A) dettaglio cori per prospetto

			  insert into VPG_MISSIONE (ID,
										CHIAVE,
										SEQUENZA,
										DESCRIZIONE,
										CD_CDS,
										CD_UNITA_ORGANIZZATIVA,
										ESERCIZIO,
										PG_MISSIONE,
										TI_RECORD_L1,
										TI_RECORD_L2,
										TI_PROVVISORIO_DEFINITIVO,
										FL_ASSOCIATO_COMPENSO,
										PG_COMPENSO,
										CD_CONTRIBUTO_RITENUTA,
										TI_ENTE_PERCIPIENTE,
										AMMONTARE,
										ALIQUOTA,
										IMPONIBILE,
										DS_CONTRIBUTO_RITENUTA,
										DETRAZIONE_ALTRI_NETTO,
										DETRAZIONE_CONIUGE_NETTO,
										DETRAZIONE_FIGLI_NETTO,
										DETRAZIONI_LA_NETTO,
										DETRAZIONI_PERSONALI_NETTO)
			  select aId,
			  		  'EA:dettaglio CORI',
					  i,
					  'Stampa RPT',
		 			   aMiss.CD_CDS,
		 			   aMiss.CD_UNITA_ORGANIZZATIVA,
		 			   aMiss.ESERCIZIO,
		 			   aMiss.PG_MISSIONE,
		 			   'E',
		 			   'A',
		 			   aMiss.TI_PROVVISORIO_DEFINITIVO,
		 			   aMiss.FL_ASSOCIATO_COMPENSO,
					   aComp.PG_COMPENSO,
					   aCori.CD_CONTRIBUTO_RITENUTA,
					   aCori.TI_ENTE_PERCIPIENTE,
					   aCori.AMMONTARE,
					   aCori.ALIQUOTA,
					   aCori.IMPONIBILE,
					   tcr.DS_CONTRIBUTO_RITENUTA,
					   aComp.DETRAZIONE_ALTRI_NETTO,
					   aComp.DETRAZIONE_CONIUGE_NETTO,
					   aComp.DETRAZIONE_FIGLI_NETTO,
					   aComp.DETRAZIONI_LA_NETTO,
					   aComp.DETRAZIONI_PERSONALI_NETTO
			  from tipo_contributo_ritenuta tcr
			  where tcr.CD_CONTRIBUTO_RITENUTA	   = aCori.CD_CONTRIBUTO_RITENUTA
  			    and tcr.DT_INI_VALIDITA			   = aCori.DT_INI_VALIDITA;

			-- fine inserimento record (E,A) dettaglio cori per prospetto

		 end loop;  -- fine loop 5

	end if; -- fine distinzione per missioni associate a compenso

	--	inizio inserimento record (A,B1) anticipo + capitoli

	begin
		select *
		into aAnt
		from anticipo ant
		where ant.CD_CDS                 = aMiss.CD_CDS_ANTICIPO
		  and ant.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UO_ANTICIPO
		  and ant.ESERCIZIO				 = aMiss.ESERCIZIO_ANTICIPO
		  and ant.PG_ANTICIPO			 = aMiss.PG_ANTICIPO;

		-- ciclo sui capitoli associati all'anticipo
		for aVoce in (select * from obbligazione_scad_voce obbv
				  	  where obbv.CD_CDS			 		   	 = aAnt.CD_CDS_OBBLIGAZIONE
					    and obbv.ESERCIZIO		   		   	 = aAnt.ESERCIZIO_OBBLIGAZIONE
					    and obbv.ESERCIZIO_ORIGINALE   		   	 = aAnt.ESERCIZIO_ORI_OBBLIGAZIONE
						and obbv.PG_OBBLIGAZIONE   		   	 = aAnt.PG_OBBLIGAZIONE
						and obbv.PG_OBBLIGAZIONE_SCADENZARIO = aAnt.PG_OBBLIGAZIONE_SCADENZARIO) loop
		-- inizio loop 2

			i := i+1;

			insert into VPG_MISSIONE   (ID,
										CHIAVE,
										SEQUENZA,
										DESCRIZIONE,
										CD_CDS,
										CD_UNITA_ORGANIZZATIVA,
										ESERCIZIO,
										PG_MISSIONE,
										TI_RECORD_L1,
										TI_RECORD_L2,
										TI_PROVVISORIO_DEFINITIVO,
										FL_ASSOCIATO_COMPENSO,
										ESERCIZIO_ORI_OBBL_ACC,
										PG_OBBL_ACC,
										PG_OBBL_ACC_SCADENZARIO,
										TI_COMPETENZA_RESIDUO,
										DT_SCADENZA,
										IM_ANTICIPO,
										CD_VOCE)
			select aId,
				   'AB1:anticipo,capitoli',
				   i,
				   'Stampa RPT',
				   aMiss.CD_CDS,
				   aMiss.CD_UNITA_ORGANIZZATIVA,
				   aMiss.ESERCIZIO,
				   aMiss.PG_MISSIONE,
				   'A',
				   'B1',
				   aMiss.TI_PROVVISORIO_DEFINITIVO,
				   aMiss.FL_ASSOCIATO_COMPENSO,
				   aAnt.ESERCIZIO_ORI_OBBLIGAZIONE,
				   aAnt.PG_OBBLIGAZIONE,
				   aAnt.PG_OBBLIGAZIONE_SCADENZARIO,
				   decode(obb.ESERCIZIO_ORI_RIPORTO,null,'C','R'),
				   obbs.DT_SCADENZA,
				   aAnt.IM_ANTICIPO,
				   aVoce.CD_VOCE
			from obbligazione obb
				,obbligazione_scadenzario obbs
			where obb.CD_CDS			 	   = aAnt.CD_CDS_OBBLIGAZIONE
			  and obb.ESERCIZIO			 	   = aAnt.ESERCIZIO_OBBLIGAZIONE
			  and obb.ESERCIZIO_ORIGINALE		 	   = aAnt.ESERCIZIO_ORI_OBBLIGAZIONE
	  		  and obb.PG_OBBLIGAZIONE		 	   = aAnt.PG_OBBLIGAZIONE
	  		  and obbs.CD_CDS				   = aAnt.CD_CDS_OBBLIGAZIONE
	  		  and obbs.ESERCIZIO			  	   = aAnt.ESERCIZIO_OBBLIGAZIONE
			  and obbs.ESERCIZIO_ORIGINALE		 	   = aAnt.ESERCIZIO_ORI_OBBLIGAZIONE
	  		  and obbs.PG_OBBLIGAZIONE		  	   = aAnt.PG_OBBLIGAZIONE
	  		  and obbs.PG_OBBLIGAZIONE_SCADENZARIO = aAnt.PG_OBBLIGAZIONE_SCADENZARIO;

			if aAnt.STATO_PAGAMENTO_FONDO_ECO = 'N' then
			  begin
 			     update VPG_MISSIONE vpg
 			     set (PG_MAN_REV)
 			     = (select mriga.pg_mandato
 				    from mandato_riga mriga
 				 	where mriga.CD_CDS_DOC_AMM			   = aAnt.cd_cds
			  		  and mriga.CD_UO_DOC_AMM			   = aAnt.cd_unita_organizzativa
			  		  and mriga.ESERCIZIO_DOC_AMM		   = aAnt.esercizio
			  		  and mriga.PG_DOC_AMM				   = aAnt.pg_anticipo
			  		  and mriga.CD_TIPO_DOCUMENTO_AMM 	   = 'ANTICIPO'
	  		  		  and mriga.STATO				  	   <> 'A')
 			     where vpg.CD_CDS 	   	 	       = aMiss.CD_CDS
 				   and vpg.CD_UNITA_ORGANIZZATIVA  = aMiss.CD_UNITA_ORGANIZZATIVA
 				   and vpg.ESERCIZIO			   = aMiss.ESERCIZIO
 				   and vpg.PG_MISSIONE			   = aMiss.PG_MISSIONE
 				   and vpg.TI_RECORD_L1		   = 'A'
 				   and vpg.TI_RECORD_L2		   = 'B1'
 				   and vpg.SEQUENZA			   = i;
	 		  exception when NO_DATA_FOUND then
	 		  			null;
			  end;
			end if;

		end loop; -- fine loop 2

	exception when NO_DATA_FOUND then
			  aVar1 := null;
			  aNum1 := 0;
			  aNum2 := 0;
			  aNum3 := 0;
			  -- non esiste anticipo
	end;

	--	fine inserimento record (A,B1) anticipo + capitoli

	-- inizio inserimento record (B,A) dettagli di spesa

	-- ciclo sui dettagli di spesa
	for aMdet in (select * from missione_dettaglio mdet
			 	  where mdet.CD_CDS			  	    = aMiss.CD_CDS
  				    and mdet.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
  				    and mdet.ESERCIZIO			    = aMiss.ESERCIZIO
  				    and mdet.PG_MISSIONE			= aMiss.PG_MISSIONE
  				    and mdet.TI_SPESA_DIARIA		= 'S'
  				    and mdet.TI_CD_TI_SPESA		    in ('A','N','P','T')) loop
	-- inizio loop 6

	    i := i+1;

		insert into VPG_MISSIONE (ID,
			   					  CHIAVE,
								  SEQUENZA,
								  DESCRIZIONE,
								  CD_CDS,
								  CD_UNITA_ORGANIZZATIVA,
								  ESERCIZIO,
								  PG_MISSIONE,
								  TI_RECORD_L1,
								  TI_RECORD_L2,
								  TI_PROVVISORIO_DEFINITIVO,
								  IM_SPESE,
								  PG_RIGA,
								  DT_INIZIO_TAPPA,
								  DS_SPESA,
								  FL_SPESA_ANTICIPATA,
								  CD_DIVISA_SPESA,
								  IM_SPESA_DIVISA,
								  CAMBIO_SPESA,
								  IM_BASE_MAGGIORAZIONE,
								  PERCENTUALE_MAGGIORAZIONE,
								  IM_MAGGIORAZIONE,
								  IM_SPESA_EURO,
								  IM_TOTALE_SPESA,
								  TI_AUTO,
								  CHILOMETRI,
								  INDENNITA_CHILOMETRICA,
								  IM_SPESE_ANTICIPATE)
	 	values  (aId,
			     'BA:spese dettagli',
				 i,
				 'Stampa RPT',
				 aMiss.CD_CDS,
				 aMiss.CD_UNITA_ORGANIZZATIVA,
				 aMiss.ESERCIZIO,
				 aMiss.PG_MISSIONE,
				 'B',
				 'A',
				 aMiss.TI_PROVVISORIO_DEFINITIVO,
				 aMiss.IM_SPESE,
				 aMdet.PG_RIGA,
				 aMdet.DT_INIZIO_TAPPA,
				 aMdet.DS_SPESA,
				 aMdet.FL_SPESA_ANTICIPATA,
				 aMdet.CD_DIVISA_SPESA,
				 aMdet.IM_SPESA_DIVISA,
				 aMdet.CAMBIO_SPESA,
				 aMdet.IM_BASE_MAGGIORAZIONE,
				 aMdet.PERCENTUALE_MAGGIORAZIONE,
				 aMdet.IM_MAGGIORAZIONE,
				 aMdet.IM_SPESA_EURO,
				 aMdet.IM_TOTALE_SPESA,
				 aMdet.TI_AUTO,
				 aMdet.CHILOMETRI,
				 aMdet.INDENNITA_CHILOMETRICA,
				 aMiss.IM_SPESE_ANTICIPATE);

	end loop; -- fine loop 6

	-- fine inserimento record (B,A) dettagli di spesa

	-- inizio inserimento record (B,B) dettagli di rimborso KM

	-- ciclo sui dettagli di spesa
	for aMdet in (select * from missione_dettaglio mdet
			 	  where mdet.CD_CDS			  	    = aMiss.CD_CDS
  				    and mdet.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
  				    and mdet.ESERCIZIO			    = aMiss.ESERCIZIO
  				    and mdet.PG_MISSIONE			= aMiss.PG_MISSIONE
  				    and mdet.TI_SPESA_DIARIA		= 'S'
  				    and mdet.TI_CD_TI_SPESA		    = 'R') loop
	-- inizio loop 7

	    i := i+1;

		insert into VPG_MISSIONE (ID,
			   					  CHIAVE,
								  SEQUENZA,
								  DESCRIZIONE,
								  CD_CDS,
								  CD_UNITA_ORGANIZZATIVA,
								  ESERCIZIO,
								  PG_MISSIONE,
								  TI_RECORD_L1,
								  TI_RECORD_L2,
								  TI_PROVVISORIO_DEFINITIVO,
								  IM_SPESE,
								  PG_RIGA,
								  DT_INIZIO_TAPPA,
								  DS_SPESA,
								  FL_SPESA_ANTICIPATA,
								  CD_DIVISA_SPESA,
								  IM_SPESA_DIVISA,
								  CAMBIO_SPESA,
								  IM_BASE_MAGGIORAZIONE,
								  PERCENTUALE_MAGGIORAZIONE,
								  IM_MAGGIORAZIONE,
								  IM_SPESA_EURO,
								  IM_TOTALE_SPESA,
								  TI_AUTO,
								  CHILOMETRI,
								  INDENNITA_CHILOMETRICA,
								  IM_SPESE_ANTICIPATE)
	 	values  (aId,
			     'BB:rimborso km',
				 i,
				 'Stampa RPT',
				 aMiss.CD_CDS,
				 aMiss.CD_UNITA_ORGANIZZATIVA,
				 aMiss.ESERCIZIO,
				 aMiss.PG_MISSIONE,
				 'B',
				 'B',
				 aMiss.TI_PROVVISORIO_DEFINITIVO,
				 aMiss.IM_SPESE,
				 aMDet.PG_RIGA,
				 aMDet.DT_INIZIO_TAPPA,
				 aMDet.DS_SPESA,
				 aMDet.FL_SPESA_ANTICIPATA,
				 aMDet.CD_DIVISA_SPESA,
				 aMDet.IM_SPESA_DIVISA,
				 aMDet.CAMBIO_SPESA,
				 aMDet.IM_BASE_MAGGIORAZIONE,
				 aMDet.PERCENTUALE_MAGGIORAZIONE,
				 aMDet.IM_MAGGIORAZIONE,
				 aMDet.IM_SPESA_EURO,
				 aMDet.IM_TOTALE_SPESA,
				 aMDet.TI_AUTO,
				 aMDet.CHILOMETRI,
				 aMDet.INDENNITA_CHILOMETRICA,
				 aMiss.IM_SPESE_ANTICIPATE);

	end loop; -- fine loop 7

	-- fine inserimento record (B,B) dettagli di rimborso KM

	-- inizio inserimento record (C,A) dettagli di diaria

	-- ciclo sulle tappe
	for aMtap in (select * from missione_tappa mtap
			 	  where mtap.CD_CDS			  	    = aMiss.CD_CDS
  				    and mtap.CD_UNITA_ORGANIZZATIVA = aMiss.CD_UNITA_ORGANIZZATIVA
  				    and mtap.ESERCIZIO			    = aMiss.ESERCIZIO
  				    and mtap.PG_MISSIONE			= aMiss.PG_MISSIONE
                    order by pg_missione,mtap.dt_inizio_tappa) loop
	-- inizio loop 8

	    i := i+1;

		select mdia.IM_DIARIA into aNum1
		from rif_inquadramento rif
			,missione_diaria mdia
		where rif.PG_RIF_INQUADRAMENTO	   = aMiss.PG_RIF_INQUADRAMENTO
		  and mdia.PG_NAZIONE			   = aMtap.PG_NAZIONE
  		  and mdia.CD_GRUPPO_INQUADRAMENTO = rif.CD_GRUPPO_INQUADRAMENTO
  		  and mdia.DT_INIZIO_VALIDITA 	   <= Trunc(aMiss.DT_INIZIO_MISSIONE)
  		  and mdia.DT_FINE_VALIDITA		   >= Trunc(aMiss.DT_INIZIO_MISSIONE);

		begin
			insert into VPG_MISSIONE (ID,
				   					  CHIAVE,
									  SEQUENZA,
									  DESCRIZIONE,
									  CD_CDS,
									  CD_UNITA_ORGANIZZATIVA,
									  ESERCIZIO,
									  PG_MISSIONE,
									  TI_RECORD_L1,
									  TI_RECORD_L2,
									  TI_PROVVISORIO_DEFINITIVO,
									  IM_DIARIA_LORDA,
									  IM_QUOTA_ESENTE,
									  IM_DIARIA_NETTO,
									  IM_SPESE,
									  DT_INIZIO_TAPPA,
									  DT_FINE_TAPPA,
									  CD_DIVISA_TAPPA,
									  CAMBIO_TAPPA,
									  IM_DIARIA,
									  IM_DIARIA_LORDA_DET,
									  IM_QUOTA_ESENTE_DET,
									  IM_DIARIA_NETTO_DET,
									  FL_DIARIA_MANUALE)
		 	select   aId,
				     'CA:diaria dettagli',
					 i,
					 'Stampa RPT',
					 aMiss.CD_CDS,
					 aMiss.CD_UNITA_ORGANIZZATIVA,
					 aMiss.ESERCIZIO,
					 aMiss.PG_MISSIONE,
					 'C',
					 'A',
					 aMiss.TI_PROVVISORIO_DEFINITIVO,
					 aMiss.IM_DIARIA_LORDA,
					 aMiss.IM_QUOTA_ESENTE,
					 aMiss.IM_DIARIA_NETTO,
					 aMiss.IM_SPESE,
					 aMtap.DT_INIZIO_TAPPA,
					 aMtap.DT_FINE_TAPPA,
					 aMtap.CD_DIVISA_TAPPA,
					 aMtap.CAMBIO_TAPPA,
					 aNum1,
					 mdet.IM_DIARIA_LORDA,
					 mdet.IM_QUOTA_ESENTE,
					 mdet.IM_DIARIA_NETTO,
					 mdet.FL_DIARIA_MANUALE
			from missione_dettaglio mdet
			where mdet.CD_CDS			   	  = aMtap.CD_CDS
			  and mdet.CD_UNITA_ORGANIZZATIVA = aMtap.CD_UNITA_ORGANIZZATIVA
	  		  and mdet.ESERCIZIO			  = aMtap.ESERCIZIO
	  		  and mdet.PG_MISSIONE			  = aMtap.PG_MISSIONE
	  		  and mdet.DT_INIZIO_TAPPA		  = aMtap.DT_INIZIO_TAPPA
	  		  and mdet.TI_SPESA_DIARIA		  = 'D';
		exception when DUP_VAL_ON_INDEX then
				  null;
		end;
	begin
	select IM_rimborso into aNum1
		from rif_inquadramento rif ,MISSIONE_QUOTA_RIMBORSO qrim ,nazione
		where rif.PG_RIF_INQUADRAMENTO	   = aMiss.PG_RIF_INQUADRAMENTO
		  and NAZIONE.PG_NAZIONE			   = aMtap.PG_NAZIONE
		  and qrim.cd_area_estera			   = nazione.cd_area_estera
  		and qrim.CD_GRUPPO_INQUADRAMENTO = rif.CD_GRUPPO_INQUADRAMENTO
  		and qrim.DT_INIZIO_VALIDITA 	   <= Trunc(aMiss.DT_INIZIO_MISSIONE)
  		and qrim.DT_FINE_VALIDITA		   >= Trunc(aMiss.DT_INIZIO_MISSIONE)
  		AND NAZIONE.TI_NAZIONE!='I';
  exception when NO_DATA_FOUND then
			  aNum1 := 0;
	end;
	begin
			insert into VPG_MISSIONE (ID,
				   					  CHIAVE,
									  SEQUENZA,
									  DESCRIZIONE,
									  CD_CDS,
									  CD_UNITA_ORGANIZZATIVA,
									  ESERCIZIO,
									  PG_MISSIONE,
									  TI_RECORD_L1,
									  TI_RECORD_L2,
									  TI_PROVVISORIO_DEFINITIVO,
									  TOT_QUOTA_RIMBORSO,
									  IM_QUOTA_ESENTE,
									  DT_INIZIO_TAPPA,
									  DT_FINE_TAPPA,
									  IM_TABELL_RIMB,
									  QUOTA_RIMBORSO,
									  IM_QUOTA_ESENTE_DET
									  )
		 	select   aId,
				     'DA:rimborso dettagli',
					 i,
					 'Stampa RPT',
					 aMiss.CD_CDS,
					 aMiss.CD_UNITA_ORGANIZZATIVA,
					 aMiss.ESERCIZIO,
					 aMiss.PG_MISSIONE,
					 'D',
					 'A',
					 aMiss.TI_PROVVISORIO_DEFINITIVO,
					 aMiss.IM_RIMBORSO,
					 aMiss.IM_QUOTA_ESENTE,
					 aMtap.DT_INIZIO_TAPPA,
					 aMtap.DT_FINE_TAPPA,
					 aNum1,
					 mdet.IM_RIMBORSO,
					 mdet.IM_QUOTA_ESENTE
			from missione_dettaglio mdet
			where mdet.CD_CDS			   	  = aMtap.CD_CDS
			  and mdet.CD_UNITA_ORGANIZZATIVA = aMtap.CD_UNITA_ORGANIZZATIVA
	  		  and mdet.ESERCIZIO			  = aMtap.ESERCIZIO
	  		  and mdet.PG_MISSIONE			  = aMtap.PG_MISSIONE
	  		  and mdet.DT_INIZIO_TAPPA		  = aMtap.DT_INIZIO_TAPPA
	  		  and mdet.TI_SPESA_DIARIA		  = 'R';
		exception when DUP_VAL_ON_INDEX then
				  null;
		end;
		-- fine inserimento record (D,A) dettagli rimborso

	end loop; -- fine loop 8
	-- fine inserimento record (C,A) dettagli di diaria

 end loop;  -- fine loop 1
End;
/


