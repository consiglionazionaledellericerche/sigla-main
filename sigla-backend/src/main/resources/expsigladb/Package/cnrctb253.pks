CREATE OR REPLACE PACKAGE CNRCTB253 AS
--
--
-- CNRCTB253: Package per la gestione delle estrazioni IVA per la liquidazione annuale
--
-- Date: 17/02/2004
-- Version: 1.5
--
-- Dependency:
--
-- History:
--
-- Date: 27/11/2003
-- Version: 1.0
-- Creazione
--
-- Date: 09/12/2003
-- Version: 1.1
-- Correzione condizione bene su dettaglio per tabella riepilogativa quadri iva acquisti
--
-- Date: 10/12/2003
-- Version: 1.2
-- Corretta descrizione DS_A_9
--
-- Date: 03/02/2004
-- Version: 1.3
-- Modifica estrazione tabCodIvaVendite - richiesta n. 758
--
-- Date: 04/02/2004
-- Version: 1.4
-- Corretta modifica precedente: condizione or per selezione cd_voce_iva
--
-- Date: 17/02/2004
-- Version: 1.5
-- Fix estrazione iva debito/credito esercizio precedente (errore n. 768)
--
-- Constants:
--
-- Descrizioni per il pannello costante
-- Parte acquisti
DS_A_1 CONSTANT VARCHAR2(200)  := 'Importazioni';
DS_A_2 CONSTANT VARCHAR2(200)  := 'Autofatture registro v/caut';
DS_A_3 CONSTANT VARCHAR2(200)  := 'Acquisti IntraUE reg. v/cue';
DS_A_4 CONSTANT VARCHAR2(200)  := 'Acquisti IntraUE reg. v/cue con sola condizione "beni"';
DS_A_5 CONSTANT VARCHAR2(200)  := 'Acquisti RSM senza pag.to IVA reg. v/rsms';
DS_A_6 CONSTANT VARCHAR2(200)  := 'Acquisti RSM con add.to iva';
DS_A_7 CONSTANT VARCHAR2(200)  := 'Acquisto beni ammort.li di cui al totale';
DS_A_8 CONSTANT VARCHAR2(200)  := 'Altri acquisti e importazioni di cui al totale';
DS_A_9 CONSTANT VARCHAR2(200)  := 'Acquisto beni non ammort.li';
-- Parte vendite
DS_V_1 CONSTANT VARCHAR2(200)  := 'Operazioni effettuate nell''anno,ma con imposta esigibile in anni successivi';
DS_V_2 CONSTANT VARCHAR2(200)  := 'Ammontare delle cessioni di beni ad operatori RSM';
DS_V_3 CONSTANT VARCHAR2(200)  := 'Operazioni effettuate nell''anno, soggette a split payment';
-- Functions and Procedures:
--
--
-- estrazione tab1_LIQUIDAZIONIIVA_cineca.xls"
procedure riepilogoLiquidazioneIva(aId number,aEs number);

-- estrazioni tab2_IVA-ACQUISTI_cineca.xls, tab3_IVA-VA-VF_cineca_verificata.xls
-- aFlEsclusione = 'N' >>> tutti i codici iva (tab2_IVA-ACQUISTI_cineca.xls)
-- aFlEsclusione = 'Y' >>> solo codici iva con fl_escluso = 'N' e fl_non_soggetto = 'N' (tab3_IVA-VA-VF_cineca_verificata.xls)
procedure tabCodIvaAcquisti(aId number,aEs number,aFlEsclusione char);

-- estrazioni tab4_IVA_-VA-VE_cineca.xls,tab5_ICA_-VENDITE_cineca.xls
-- aFlEsclusione = 'N' >>> tutti i codici iva (tab5_ICA_-VENDITE_cineca.xls)
-- aFlEsclusione = 'Y' >>> solo codici iva con fl_escluso = 'N' e fl_non_soggetto = 'N' (tab4_IVA_-VA-VE_cineca.xls)
procedure tabCodIvaVendite(aId number, aEs number,aFlEsclusione char);

END CNRCTB253;


CREATE OR REPLACE PACKAGE BODY CNRCTB253 AS

procedure ins_VP_LIQUID_IVA_ANNUALE(aDest vp_liquid_iva_annuale%rowtype) is
begin
	insert into vp_liquid_iva_annuale  (ID,
										CHIAVE,
										TIPO,
										SEQUENZA,
										ESERCIZIO,
										DS_MESE,
										CD_VOCE_IVA,
										DS_VOCE_IVA,
										IMPONIBILE,
										IM_IVA,
										IM_TOTALE)
	values (aDest.ID,
			aDest.CHIAVE,
			aDest.TIPO,
			aDest.SEQUENZA,
			aDest.ESERCIZIO,
			aDest.DS_MESE,
			aDest.CD_VOCE_IVA,
			aDest.DS_VOCE_IVA,
			aDest.IMPONIBILE,
			aDest.IM_IVA,
			aDest.IM_TOTALE);
end;

procedure inizializzaPannelloAcquisti(aId number,aEs number, aSeq in out number) is
aDettaglio vp_liquid_iva_annuale%rowtype;
begin
	 aDettaglio.id := aId;
	 aDettaglio.chiave := to_char(aId);
	 aDettaglio.tipo := 'C';
	 aDettaglio.esercizio := aEs;
	 aDettaglio.imponibile := 0;
	 aDettaglio.im_iva := 0;
	 aDettaglio.im_totale := 0;

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_1;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_2;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_3;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_4;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_5;
	 aDettaglio.im_iva := null;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_6;
	 aDettaglio.im_iva := null;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_7;
	 aDettaglio.im_iva := null;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_8;
	 aDettaglio.im_iva := null;
	 aDettaglio.imponibile := null;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.ds_voce_iva := DS_A_9;
	 aDettaglio.im_iva := null;
	 aDettaglio.imponibile := 0;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

end;

procedure inizializzaPannelloVendite(aId number,aEs number,aSeq in out number) is
aDettaglio vp_liquid_iva_annuale%rowtype;
begin
	aDettaglio.id := aId;
	aDettaglio.chiave := to_char(aId);
	aDettaglio.tipo := 'C';
	aDettaglio.esercizio := aEs;
	aDettaglio.imponibile := 0;
	aDettaglio.im_totale := 0;
	aDettaglio.im_iva := 0;

	aSeq := aSeq + 1;
	aDettaglio.sequenza := aSeq;
	aDettaglio.ds_voce_iva := DS_V_1;
	ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	aSeq := aSeq + 1;
	aDettaglio.sequenza := aSeq;
	aDettaglio.ds_voce_iva := DS_V_2;
	ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	aSeq := aSeq + 1;
	aDettaglio.sequenza := aSeq;
	aDettaglio.ds_voce_iva := DS_V_3;
	ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);
end;

procedure riepilogoLiquidazioneIva(aId number,aEs number) is
aDettaglio vp_liquid_iva_annuale%rowtype;
aUoEnte unita_organizzativa%rowtype;
aImCredDebAnnoPrec number;
aTot number := 0;
aSeq number:= 0;
begin
	 select * into aUoEnte
	 from unita_organizzativa
	 where cd_tipo_unita = 'ENTE'
	   and fl_cds = 'N';

	 begin
	 	  select IVA_DEB_CRED_PER_PREC
		  into aImCredDebAnnoPrec
		  from liquidazione_iva
		  where cd_cds 		   	 	   = aUoEnte.cd_unita_padre
		    and esercizio 			   = aEs
			and cd_unita_organizzativa = aUoEnte.cd_unita_organizzativa
			and tipo_liquidazione 	   = 'C'
			and dt_inizio 			   = to_date(aEs||'0101','YYYYMMDD')
			and dt_fine 			   = to_date(aEs||'0131','YYYYMMDD')
			and stato 				   = 'D'; --  solo liquidazioni definitive
	 exception when no_data_found then
	 	  aImCredDebAnnoPrec := 0;
	 end;

	 aDettaglio.id := aId;
	 aDettaglio.chiave := to_char(aId);
	 aDettaglio.esercizio := aEs;

	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.tipo := 'A';
	 aDettaglio.ds_mese := 'Debito/Credito da esercizio precedente';
	 aDettaglio.im_iva := aImCredDebAnnoPrec;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	 -- valorizzazione dei mesi
	 for aLiqIva in (select *
	 	 		 	 from liquidazione_iva
					 where cd_cds = aUoEnte.cd_unita_padre
					   and cd_unita_organizzativa = aUoEnte.cd_unita_organizzativa
					   and tipo_liquidazione = 'C'
					   and to_number(to_char(dt_inizio,'YYYY')) = aEs
					   and to_number(to_char(dt_fine,'YYYY')) = aEs
					   and stato = 'D'
					 order by dt_inizio) loop
	 -- inizio loop 1 (liquidazioni)
	 	aSeq := aSeq + 1;
		aDettaglio.sequenza := aSeq;
		aDettaglio.tipo := 'B';
		aDettaglio.ds_mese := to_char(aLiqIva.dt_inizio,'MONTH');
		aDettaglio.im_iva := aLiqIva.iva_da_versare;
		ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

		aTot := aTot + aLiqIva.iva_da_versare;
	 end loop; -- fine loop 1 (liquidazioni)

	 -- inserimento del totale
	 aSeq := aSeq + 1;
	 aDettaglio.sequenza := aSeq;
	 aDettaglio.tipo := 'C';
	 aDettaglio.ds_mese := 'TOTALE';
	 aDettaglio.im_iva := aTot;
	 ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

end;

procedure tabCodIvaAcquisti(aId number,aEs number,aFlEsclusione char) is
aDettaglio vp_liquid_iva_annuale%rowtype;
aSeq number := 0;
aTotImponibile number := 0;
aTotIva number := 0;
aTot number := 0;
aImpTipoA number := 0;
aTotTipoA number := 0;
aImpTipoB number := 0;
aTotTipoB number := 0;
begin
	aDettaglio.id := aId;
	aDettaglio.chiave := to_char(aId);
	aDettaglio.esercizio := aEs;

	if aFlEsclusione = 'N' then
	-- tabella 2
	   for aCodIva in (select esercizio,cd_voce_iva,ds_voce_iva,sum(imponibile_dettaglio) totImpDett
	   	   		   	   from v_liquid_iva_ann_acquisti
					   where esercizio = aEs
					     and protocollo_iva is not null
						 and ti_istituz_commerc_riga = 'C'
					   group by esercizio,cd_voce_iva,ds_voce_iva) loop
	   -- inizio loop 1 (codici iva)
	   	  aSeq := aSeq + 1;
		  aDettaglio.tipo := 'A';
		  aDettaglio.sequenza := aSeq;
		  aDettaglio.cd_voce_iva := aCodIva.cd_voce_iva;
		  aDettaglio.ds_voce_iva := aCodIva.ds_voce_iva;
		  aDettaglio.imponibile := aCodIva.totImpDett;
		  ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

		  aTotImponibile := aTotImponibile + aCodIva.totImpDett;

	   end loop; -- fine loop 1 (codici iva)
	   -- inserimento del totale
	   aSeq := aSeq + 1;
	   aDettaglio.tipo := 'B';
	   aDettaglio.sequenza := aSeq;
	   aDettaglio.cd_voce_iva := null;
	   aDettaglio.ds_voce_iva := 'TOTALE';
	   aDettaglio.imponibile := aTotImponibile;
	   ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	else
	-- tabella 3
	   for aCodIva in (select esercizio,cd_voce_iva,ds_voce_iva,
	   	   		   	  		  sum(imponibile_dettaglio) totImpDett,
							  sum(iva_dettaglio) totIvaDett,
							  sum(totale_dettaglio) totDett
	   	   		   	   from v_liquid_iva_ann_acquisti
					   where esercizio = aEs
					     and protocollo_iva is not null
						 and ti_istituz_commerc_riga = 'C'
						 and fl_escluso = 'N'
						 and fl_non_soggetto = 'N'
					   group by esercizio,cd_voce_iva,ds_voce_iva) loop
	   -- inizio loop 1 (codici iva)
	   	  aSeq := aSeq + 1;
		  aDettaglio.tipo := 'A';
		  aDettaglio.sequenza := aSeq;
		  aDettaglio.cd_voce_iva := aCodIva.cd_voce_iva;
		  aDettaglio.ds_voce_iva := aCodIva.ds_voce_iva;
		  aDettaglio.imponibile := aCodIva.totImpDett;
		  aDettaglio.im_iva := aCodIva.totIvaDett;
		  aDettaglio.im_totale := aCodIva.totDett;
		  ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

		  aTotImponibile := aTotImponibile + aCodIva.totImpDett;
		  aTotIva := aTotIva + aCodIva.totIvaDett;
		  aTot := aTot + aCodIva.totDett;

	   end loop; -- fine loop 1 (codici iva)

	   -- inserimento dei totali
	   aSeq := aSeq + 1;
	   aDettaglio.tipo := 'B';
	   aDettaglio.sequenza := aSeq;
	   aDettaglio.cd_voce_iva := null;
	   aDettaglio.ds_voce_iva := 'TOTALI';
	   aDettaglio.imponibile := aTotImponibile;
	   aDettaglio.im_iva := aTotIva;
	   aDettaglio.im_totale := aTot;
	   ins_VP_LIQUID_IVA_ANNUALE(aDettaglio);

	   inizializzaPannelloAcquisti (aId,aEs,aSeq);

	   for aCodIvaDett in (select esercizio, cd_tipo_sez_autofatt,
	   	   			   	  		  fl_extra_ue, fl_san_marino_senza_iva, fl_san_marino_con_iva,
								  cd_bene_servizio, fl_gestione_inventario, ti_bene_servizio,
		   	   		   	  		  sum(imponibile_dettaglio) totImpDett,
								  sum(iva_dettaglio) totIvaDett,
								  sum(totale_dettaglio) totDett,
								  sum(imponibile_dett_coll) totImpDettColl,
								  sum(iva_dett_coll) totIvaDettColl,
								  sum(tot_dett_coll) totDettColl,
								  FL_CODIVA_AUTOFATTURA
	   	   		   	   from v_liquid_iva_ann_acquisti
					   where esercizio = aEs
					     and protocollo_iva is not null
						 and fl_escluso = 'N'
						 and fl_non_soggetto = 'N'
						 and ti_istituz_commerc_riga = 'C'
					   group by esercizio, cd_tipo_sez_autofatt,
	   	   			   	  		fl_extra_ue, fl_san_marino_senza_iva, fl_san_marino_con_iva,
								cd_bene_servizio, fl_gestione_inventario, ti_bene_servizio,FL_CODIVA_AUTOFATTURA) loop
	   -- inizio loop 2 (codici Iva dettagliati)
		  -- aggiornamento dettagli
		  if aCodIvaDett.fl_extra_ue = 'Y' then
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.totImpDett + nvl(aCodIvaDett.totImpDettColl,0),
			 	 im_iva = im_iva + aCodIvaDett.totIvaDett + nvl(aCodIvaDett.totIvaDettColl,0),
				 im_totale = im_totale + aCodIvaDett.totDett + nvl(aCodIvaDett.totDettColl,0)
			 where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_A_1;
		  end if;

		  if aCodIvaDett.cd_tipo_sez_autofatt = 'v/caut' and nvl(aCodIvaDett.fl_codiva_autofattura,'Y') ='Y' then
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.totImpDett,
			 	 im_iva = im_iva + aCodIvaDett.totIvaDett,
				 im_totale = im_totale + aCodIvaDett.totDett
			 where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_A_2;
		  end if;

		  if aCodIvaDett.cd_tipo_sez_autofatt = 'v/cue' then
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.totImpDett,
			 	 im_iva = im_iva + aCodIvaDett.totIvaDett,
				 im_totale = im_totale + aCodIvaDett.totDett
			 where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_A_3;
		  end if;

		  if aCodIvaDett.cd_tipo_sez_autofatt = 'v/cue'
		  	 and aCodIvaDett.ti_bene_servizio = 'B' then
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.totImpDett,
			 	 im_iva = im_iva + aCodIvaDett.totIvaDett,
				 im_totale = im_totale + aCodIvaDett.totDett
			 where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_A_4;
		  end if;

		  if aCodIvaDett.fl_san_marino_senza_iva = 'Y'
		  	 and aCodIvaDett.cd_tipo_sez_autofatt = 'v/rsms' then
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.totImpDett,
				 im_totale = im_totale + aCodIvaDett.totDett
			 where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_A_5;
		  end if;

		  if aCodIvaDett.fl_san_marino_con_iva = 'Y' then
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.totImpDett,
				 im_totale = im_totale + aCodIvaDett.totDett
			 where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_A_6;
		  end if;

		  if aCodIvaDett.fl_gestione_inventario = 'Y' then
		  	 aImpTipoA := aImpTipoA + aCodIvaDett.totImpDett;
			 aTotTipoA := aTotTipoA + aCodIvaDett.totDett;
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.totImpDett,
				 im_totale = im_totale + aCodIvaDett.totDett
			 where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_A_7;
		  else
		  	 aImpTipoB := aImpTipoB + aCodIvaDett.totImpDett;
			 aTotTipoB := aTotTipoB + aCodIvaDett.totDett;
			 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.totImpDett,
				 im_totale = im_totale + aCodIvaDett.totDett
			 where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_A_9;
		  end if;

		  update vp_liquid_iva_annuale
	      set /*imponibile = aTotImponibile -(aImpTipoA + aImpTipoB),*/
		  	  im_totale = aTot - (aTotTipoA + aTotTipoB)
		  where id = aId
		    and chiave = to_char(aId)
			and tipo = 'C'
			and ds_voce_iva = DS_A_8;

	   end loop; -- fine loop 2 (codici Iva dettagliati)
	end if;
end;

procedure tabCodIvaVendite(aId number, aEs number,aFlEsclusione char) is
aDettaglio vp_liquid_iva_annuale%rowtype;
aTotImponibile number := 0;
aTotIva number := 0;
aTot number := 0;
aSeq number := 0;
begin
	aDettaglio.id := aId;
	aDettaglio.chiave := to_char(aId);
	aDettaglio.esercizio := aEs;

	if aFlEsclusione = 'N' then
	   for aCodIva in (select esercizio, cd_voce_iva, ds_voce_iva,
	   	   		   	  		  sum(imponibile_dettaglio) impDett
	   	   		   	   from v_liquid_iva_ann_vendite
					   where to_number(to_char(dt_emissione,'YYYY')) = aEs
					     and protocollo_iva is not null
					   group by esercizio, cd_voce_iva, ds_voce_iva) loop
	   -- inizio loop 1 (codici iva)
	   	  aSeq := aSeq + 1;
		  aDettaglio.tipo := 'A';
		  aDettaglio.sequenza := aSeq;
		  aDettaglio.cd_voce_iva := aCodIva.cd_voce_iva;
		  aDettaglio.ds_voce_iva := aCodIva.ds_voce_iva;
		  aDettaglio.imponibile := aCodIva.impDett;
		  ins_vp_liquid_iva_annuale(aDettaglio);

		  aTotImponibile := aTotImponibile + aCodIva.impDett;

	   end loop; -- fine loop 1 (codici iva)
	   -- inserimento del totale
	   aSeq := aSeq + 1;
	   aDettaglio.tipo := 'B';
	   aDettaglio.sequenza := aSeq;
	   aDettaglio.cd_voce_iva := null;
	   aDettaglio.ds_voce_iva := 'TOTALE';
	   aDettaglio.imponibile := aTotImponibile;
	   ins_vp_liquid_iva_annuale(aDettaglio);

	else
	   for aCodIva in (select esercizio, cd_voce_iva, ds_voce_iva,
	   	   		   	  		  sum(imponibile_dettaglio) impDett,
							  sum(iva_dettaglio) ivaDett,
							  sum(totale_dettaglio)  totDett
					   from v_liquid_iva_ann_vendite
					   where to_number(to_char(dt_emissione,'YYYY')) = aEs
					     and protocollo_iva is not null
						 and ((fl_escluso = 'N' and fl_non_soggetto = 'N')
						 	  or
							  (cd_voce_iva in ('FC','F7'))
							 )
						 -- richiesta n. 758, non ? possibile aggiungere FC ed F7
						 -- senza cablare i codici, le altre condizioni includerebbero
						 -- altre voci iva non richieste
					   group by esercizio, cd_voce_iva, ds_voce_iva) loop
	   -- inizio loop 1 (codici iva)
	   	  aSeq := aSeq + 1;
		  aDettaglio.tipo := 'A';
		  aDettaglio.sequenza := aSeq;
		  aDettaglio.cd_voce_iva := aCodIva.cd_voce_iva;
		  aDettaglio.ds_voce_iva := aCodIva.ds_voce_iva;
		  aDettaglio.imponibile := aCodIva.impDett;
		  aDettaglio.im_iva := aCodIva.ivaDett;
		  aDettaglio.im_totale := aCodIva.totDett;
		  ins_vp_liquid_iva_annuale(aDettaglio);

		  aTotImponibile := aTotImponibile + aCodIva.impDett;
		  aTotIva := aTotIva + aCodIva.ivaDett;
		  aTot := aTot + aCodIva.totDett;

	   end loop; -- fine
	   -- fine loop 1 (codici iva)
	   -- inserimento dei totali
	   aSeq := aSeq + 1;
	   aDettaglio.tipo := 'B';
	   aDettaglio.sequenza := aSeq;
	   aDettaglio.cd_voce_iva := null;
	   aDettaglio.ds_voce_iva := 'TOTALI';
	   aDettaglio.imponibile := aTotImponibile;
	   aDettaglio.im_iva := aTotIva;
	   aDettaglio.im_totale := aTot;
	   ins_vp_liquid_iva_annuale(aDettaglio);

	   inizializzaPannelloVendite(aId,aEs,aSeq);

	   -- aggiornamento dettagli
	   for aCodIvaDett in (select v_liquid_iva_ann_vendite.esercizio, esigibilita_differita,
	   	   			   	  		  dt_esigibilita_differita, fl_san_marino,
								  sum(imponibile_dettaglio) impDett,
								  sum(iva_dettaglio) ivaDett,
								  sum(totale_dettaglio) totDett
						   from v_liquid_iva_ann_vendite, configurazione_cnr cnr
						   where to_number(to_char(dt_emissione,'YYYY')) = aEs
						     and protocollo_iva is not null
							 and ((fl_escluso = 'N' and fl_non_soggetto = 'N')
						 	      or
							      (cd_voce_iva in ('FC','F7'))
							 	 )
							 	  and Cnr.ESERCIZIO = 0
       					  and Cnr.cd_chiave_primaria = 'SPLIT_PAYMENT'
       						AND Cnr.cd_chiave_secondaria = 'ATTIVA'
       						AND Cnr.cd_unita_funzionale = '*'
       						AND dt_emissione <   NVL (Cnr.dt01, dt_emissione)
							 -- richiesta n. 758, non ? possibile aggiungere FC ed F7
							 -- senza cablare i codici, le altre condizioni includerebbero
							 -- altre voci iva non richieste
						   group by v_liquid_iva_ann_vendite.esercizio, esigibilita_differita,
	   	   			   	  		  dt_esigibilita_differita, fl_san_marino) loop
	   -- inizio loop 2 (codici iva dettagliati)
	   	  aSeq := aSeq + 1;

		  if aCodIvaDett.esigibilita_differita = 'Y'
		  	 and (to_number(to_char(aCodIvaDett.dt_esigibilita_differita,'YYYY')) > aEs
			      or
				  aCodIvaDett.dt_esigibilita_differita is null)
		  then
			 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.impDett,
			 	 im_totale = im_totale + aCodIvaDett.totDett
		     where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_V_1;
		  end if;

		  if aCodIvaDett.fl_san_marino = 'Y' then
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.impDett,
			 	 im_totale = im_totale + aCodIvaDett.totDett
		     where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_V_2;
		  end if;
	   end loop;  -- fine loop 2 (codici iva dettagliati)
  -- aggiornamento dettagli
	   for aCodIvaDett in (select v_liquid_iva_ann_vendite.esercizio, esigibilita_differita,
	   	   			   	  		   fl_san_marino,
								  sum(imponibile_dettaglio) impDett,
								  sum(iva_dettaglio) ivaDett,
								  sum(totale_dettaglio) totDett
						   from v_liquid_iva_ann_vendite, configurazione_cnr cnr
						   where to_number(to_char(dt_emissione,'YYYY')) = aEs
						     and protocollo_iva is not null
							 and ((fl_escluso = 'N' and fl_non_soggetto = 'N')
						 	      or
							      (cd_voce_iva in ('FC','F7'))
							 	 )
							 	  and Cnr.ESERCIZIO = 0
       					  and Cnr.cd_chiave_primaria = 'SPLIT_PAYMENT'
       						AND Cnr.cd_chiave_secondaria = 'ATTIVA'
       						AND Cnr.cd_unita_funzionale = '*'
       						AND dt_emissione >=   NVL (Cnr.dt01, dt_emissione)
							 -- richiesta n. 758, non ? possibile aggiungere FC ed F7
							 -- senza cablare i codici, le altre condizioni includerebbero
							 -- altre voci iva non richieste
						   group by v_liquid_iva_ann_vendite.esercizio, esigibilita_differita,
	   	   			   	  		   fl_san_marino) loop
	   -- inizio loop 2 (codici iva dettagliati)
	   	  aSeq := aSeq + 1;

		  if aCodIvaDett.esigibilita_differita = 'Y'
		  then
			 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.impDett,
			 	 im_iva= im_iva + aCodIvaDett.ivaDett,
			 	 im_totale = im_totale + aCodIvaDett.totDett
		     where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_V_3;
		  end if;

		  if aCodIvaDett.fl_san_marino = 'Y' then
		  	 update vp_liquid_iva_annuale
			 set imponibile = imponibile + aCodIvaDett.impDett,
			 	 im_totale = im_totale + aCodIvaDett.totDett
		     where id = aId
			   and chiave = to_char(aId)
			   and tipo = 'C'
			   and ds_voce_iva = DS_V_2;
		  end if;
	   end loop;  -- fine loop 2 (codici iva dettagliati)

	end if;
end;

END CNRCTB253;


