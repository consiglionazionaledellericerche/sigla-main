--------------------------------------------------------
--  DDL for Package Body CNRMAR030
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRMAR030" is
 function fnum(anum number) return varchar2 is
 begin
  return to_char(anum,'9999999999999D99');
 end;

 function descAcc(aAcc accertamento%rowtype) return varchar2 is
 begin
  return 'ACC es:'||aAcc.esercizio||' cds:'||aAcc.cd_cds||' uo:'||aAcc.cd_unita_organizzativa||' esOri:'||aAcc.esercizio_originale||' pg:'||aAcc.pg_accertamento;
 end;

 function descAccScad(aAccScad accertamento_scadenzario%rowtype) return varchar2 is
 begin
  return 'pg_scad:'||aAccScad.pg_accertamento_scadenzario||' date:'||aAccScad.dt_scadenza_incasso;
 end;

 function descAccScadVoce(aAccScadVoce accertamento_scad_voce%rowtype) return varchar2 is
 begin
  return 'cd_cdr:'||aAccScadVoce.cd_centro_responsabilita||' la:'||aAccScadVoce.cd_linea_attivita;
 end;

 function descObb(aObb obbligazione%rowtype) return varchar2 is
 begin
  return 'OBB es:'||aObb.esercizio||' cds:'||aObb.cd_cds||' uo:'||aObb.cd_unita_organizzativa||' esOri:'||aObb.esercizio_originale||' pg:'||aObb.pg_obbligazione;
 end;

 function descObbScad(aObbScad obbligazione_scadenzario%rowtype) return varchar2 is
 begin
  return 'pg_scad:'||aObbScad.pg_obbligazione_scadenzario||' date:'||aObbScad.dt_scadenza;
 end;

 function descObbScadVoce(aObbScadVoce obbligazione_scad_voce%rowtype) return varchar2 is
 begin
  return 'cd_cdr:'||aObbScadVoce.cd_centro_responsabilita||' la:'||aObbScadVoce.cd_linea_attivita||' voce:'||aObbScadVoce.cd_voce;
 end;


 function MSG_DIS_PRIMI(aTipo varchar2,aAcc accertamento%rowtype,aNota varchar2)
 return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aTipo||' '||descAcc(aAcc)||' '||aNota;
  return aOut;
 end;

 function MSG_DIS_PRIMI(aTipo varchar2,aObb obbligazione%rowtype,aNota varchar2)
 return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aTipo||' '||descObb(aObb)||' '||aNota;
  return aOut;
 end;
 -- ******************************************************************
 -- ************************** ACCERTAMENTO **************************
 -- ******************************************************************
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a tutti i DOC GEN associati ad una scadenza accert.
 -- -------------------------------------------------------------------------------
 function getSaldoGenerico(aAccScad accertamento_scadenzario%rowtype) return number is
 lSaldoE number(15,2) ;
 lSaldoS number(15,2) ;
 begin
 	  lSaldoE := 0;
	  lSaldoS := 0;
 	  for lDocGenericoRiga in (select doc.*, tipo.ti_entrata_spesa from documento_generico_riga doc, tipo_documento_amm tipo
	  	  			   	   	  where doc.STATO_COFI <> STATO_ANNULLATO
							  and   doc.CD_TIPO_DOCUMENTO_AMM = tipo.CD_TIPO_DOCUMENTO_AMM
							  and   doc.CD_CDS_ACCERTAMENTO         = aAccScad.cd_cds
						   	  and	doc.ESERCIZIO_ACCERTAMENTO 	= aAccScad.esercizio
						   	  and	doc.ESERCIZIO_ORI_ACCERTAMENTO	= aAccScad.esercizio_originale
						   	  and   doc.PG_ACCERTAMENTO 		= aAccScad.pg_accertamento
						   	  and   doc.PG_ACCERTAMENTO_SCADENZARIO = aAccScad.pg_accertamento_scadenzario
							  for update nowait)
	  loop
	  	  if lDocGenericoRiga.TI_ENTRATA_SPESA = 'E' then
		  	 lSaldoE := lSaldoE + lDocGenericoRiga.im_riga;
		  end if;
	  	  if lDocGenericoRiga.TI_ENTRATA_SPESA = 'S' then
		  	 lSaldoS := lSaldoS + lDocGenericoRiga.im_riga;
		  end if;
	  end loop;
	  return abs(lSaldoE - lSaldoS);
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a tutte le FATTURE ATTIVE associate ad una scadenza accert.
 -- -------------------------------------------------------------------------------
 function getSaldoFatturaA(aAccScad accertamento_scadenzario%rowtype) return number is
 lSaldo number(15,2) ;
 lFatturaAttTestata fattura_attiva%rowtype;
 begin
	  lSaldo := 0;
 	  for lFatturaAttRiga in (select * from fattura_attiva_riga
	  	  			   	   	  where CD_CDS_ACCERTAMENTO         = aAccScad.cd_cds
						   	  and	ESERCIZIO_ACCERTAMENTO      = aAccScad.esercizio
						   	  and	ESERCIZIO_ORI_ACCERTAMENTO  = aAccScad.esercizio_originale
						   	  and   PG_ACCERTAMENTO 	    = aAccScad.pg_accertamento
						   	  and   PG_ACCERTAMENTO_SCADENZARIO = aAccScad.pg_accertamento_scadenzario
							  for update nowait)
	  loop
		  begin
		  	  select *
			  into lFatturaAttTestata
			  from fattura_attiva
			  where  CD_CDS = lFatturaAttRiga.cd_cds
			  and	 CD_UNITA_ORGANIZZATIVA = lFatturaAttRiga.CD_UNITA_ORGANIZZATIVA
			  and  	 ESERCIZIO				= lFatturaAttRiga.ESERCIZIO
			  and  	 PG_FATTURA_ATTIVA		= lFatturaAttRiga.PG_FATTURA_ATTIVA
			  for update nowait;
			  if lFatturaAttTestata.stato_cofi <> STATO_ANNULLATO then
				  -- se fatt = nota credito
				  if lFatturaAttTestata.ti_fattura = NOTA_CREDITO then
				 	lSaldo := lSaldo -(lFatturaAttRiga.im_imponibile + lFatturaAttRiga.im_iva);
				  -- altrimenti
				  else
				 	lSaldo := lSaldo +(lFatturaAttRiga.im_imponibile + lFatturaAttRiga.im_iva);
				  end if;
			  end if;
		  exception
		  when no_data_found then
		  	   raise;
		  end;
	  end loop;
	  return lSaldo;
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a tutte le FATTURE PASSIVE associate ad una scadenza accert.
 -- -------------------------------------------------------------------------------
 function getSaldoFatturaP(aAccScad accertamento_scadenzario%rowtype) return number is
 lSaldo number(15,2) ;
 begin
	  lSaldo := 0;
	  for lFatturaPasRiga in (select fpr.* from fattura_passiva_riga fpr, fattura_passiva fp
	   	  			   	   	  where fp.CD_CDS = fpr.cd_cds
							  and   fp.CD_UNITA_ORGANIZZATIVA = fpr.cd_unita_organizzativa
							  and   fp.ESERCIZIO = fpr.esercizio
							  and   fp.PG_FATTURA_PASSIVA = fpr.pg_fattura_passiva
							  and   fp.STATO_COFI <> STATO_ANNULLATO
							  and   fpr.CD_CDS_ACCERTAMENTO = aAccScad.cd_cds
	 					   	  and	fpr.ESERCIZIO_ACCERTAMENTO = aAccScad.esercizio
						   	  and	fpr.ESERCIZIO_ORI_ACCERTAMENTO	= aAccScad.esercizio_originale
	 					   	  and   fpr.PG_ACCERTAMENTO = aAccScad.pg_accertamento
	 					   	  and   fpr.PG_ACCERTAMENTO_SCADENZARIO = aAccScad.pg_accertamento_scadenzario
							  and   fp.fl_congelata = 'N'
							  for update nowait)
	  loop
		   lSaldo := lSaldo + lFatturaPasRiga.im_imponibile + lFatturaPasRiga.im_iva;
	  end loop;
	  return lSaldo;
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a i COMPENSI associati ad una scadenza accert.
 -- -------------------------------------------------------------------------------
 function getSaldoCompenso(aAccScad accertamento_scadenzario%rowtype) return number is
 lSaldo number (15,2);
 lUO unita_organizzativa%rowtype;
 begin
   lSaldo := 0;
   lUO := cnrctb020.GETUOENTE(aAccScad.ESERCIZIO);
   if aAccScad.cd_cds <> lUO.cd_unita_padre then
     for lCompenso in (select * from compenso
                    where CD_CDS_ACCERTAMENTO = aAccScad.cd_cds
             and ESERCIZIO_ACCERTAMENTO = aAccScad.esercizio
             and ESERCIZIO_ORI_ACCERTAMENTO = aAccScad.esercizio_originale
             and PG_ACCERTAMENTO = aAccScad.pg_accertamento
             and PG_ACCERTAMENTO_SCADENZARIO = aAccScad.pg_accertamento_scadenzario
          and   STATO_COFI <> STATO_ANNULLATO
          for update nowait)
    loop
       lSaldo := lSaldo + abs(lCompenso.im_totale_compenso) ;
    end loop;
   end if;
   return lSaldo;
 end;

 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a RIMBORSI associate ad una scadenza accert.
 -- -------------------------------------------------------------------------------
 function getSaldoRimborso(aAccScad accertamento_scadenzario%rowtype) return number is
 lSaldo number(15,2);
 begin
	  lSaldo := 0;
 	  for lRimborso in (select * from rimborso
	  	  			   	   	  where CD_CDS_ACCERTAMENTO = aAccScad.cd_cds
						   	  and	ESERCIZIO_ACCERTAMENTO = aAccScad.esercizio
                                                          and   ESERCIZIO_ORI_ACCERTAMENTO = aAccScad.esercizio_originale
						   	  and   PG_ACCERTAMENTO = aAccScad.pg_accertamento
						   	  and   PG_ACCERTAMENTO_SCADENZARIO = aAccScad.pg_accertamento_scadenzario
							  and   STATO_COFI <> STATO_ANNULLATO
							  for update nowait)
	  loop
		  	 lSaldo := lSaldo + lRimborso.im_rimborso;
	  end loop;
	  return lSaldo;
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a CONTRIBUTO_RITENUTE associate ad una scadenza accert.
 -- -------------------------------------------------------------------------------
--  function getSaldoRitenute(aAccScad accertamento_scadenzario%rowtype) return number is
--  lSaldo number(15,2);
--  begin
-- 	  lSaldo := 0;
--  	  for lRitenute in (select * from CONTRIBUTO_RITENUTA
-- 	  	  			   	   	  where CD_CDS_ACCERTAMENTO = aAccScad.cd_cds
-- 						   	  and	ESERCIZIO_ACCERTAMENTO = aAccScad.esercizio
--                                                        and   ESERCIZIO_ORI_ACCERTAMENTO = aAccScad.esercizio_originale
-- 						   	  and   PG_ACCERTAMENTO = aAccScad.pg_accertamento
-- 						   	  and   PG_ACCERTAMENTO_SCADENZARIO = aAccScad.pg_accertamento_scadenzario)
-- 	  loop
-- 		  	 lSaldo := lSaldo + abs(lRitenute.imponibile);
-- 	  end loop;
-- 	  return lSaldo;
--  end;

 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo totale dei documenti amm associati ad una scadenza asccertamento
 -- -------------------------------------------------------------------------------
 function GETSALDODOCAMM(aAccScad accertamento_scadenzario%rowtype ) return number is
 lSaldoTotale number(15,2);
 lSaldoGenerico number;
 begin
 	  lSaldoTotale := 0;
 	  lSaldoTotale := getSaldoGenerico (aAccScad) + getSaldoFatturaA(aAccScad) + getSaldoFatturaP(aAccScad) + getSaldoCompenso(aAccScad) + getSaldoRimborso(aAccScad)  ; -- + getSaldoRitenute(aAccScad)
	  return lSaldoTotale;
 end;

 -- ******************************************************************
 -- ************************** OBBLIGAZIONE **************************
 -- ******************************************************************
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo al FONDO ECONONALE associate ad una scadenza obbligazione.
 -- -------------------------------------------------------------------------------
 function getSaldoFondo(aObbScad obbligazione_scadenzario%rowtype, aEsisteSpesa in out boolean, aReintegrata in out boolean) return number is
 lSaldo number(15,2);
 lFondo FONDO_ECONOMALE%rowtype;
 begin
	  lSaldo := 0;
 	  aEsisteSpesa := false;
	  aReintegrata := false;
 	  for lFondoSpesa in (select * from FONDO_SPESA
	  	  			   	  where CD_CDS_OBBLIGAZIONE         = aObbScad.cd_cds
						  and	ESERCIZIO_OBBLIGAZIONE 		= aObbScad.esercizio
						  and	ESERCIZIO_ORI_OBBLIGAZIONE 	= aObbScad.esercizio_originale
						  and   PG_OBBLIGAZIONE 	    	= aObbScad.pg_obbligazione
						  and   PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario
						  for update nowait)
	  loop
	  	  begin
		  	  -- se le spese non sono reintegrate  allora puo succedere che importo scadenza <> importo associato_doc_amm
			  lSaldo := lSaldo + lFondoSpesa.im_ammontare_spesa;
			  if lFondoSpesa.FL_REINTEGRATA ='Y' then
			  	 aEsisteSpesa := true;
			  	 aReintegrata := true;
			  else
			  	 aEsisteSpesa := true;
			  	 aReintegrata := false;
			  end if;
	  	  end;
	  end loop;
	  return lSaldo;
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo alle MISSIONI associate ad una scadenza obbligazione.
 -- -------------------------------------------------------------------------------
 function getSaldoMissione(aObbScad obbligazione_scadenzario%rowtype) return number is
 lSaldo number(15,2);
 lAnticipo anticipo%rowtype;
 begin
	  lSaldo := 0;
 	  for lMissione in (select missione.* from MISSIONE, MISSIONE_RIGA
	  	  			   	   	  where MISSIONE_RIGA.CD_CDS_OBBLIGAZIONE        = aObbScad.cd_cds
						   	  and	MISSIONE_RIGA.ESERCIZIO_OBBLIGAZIONE 	 = aObbScad.esercizio
						   	  and	MISSIONE_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE = aObbScad.esercizio_originale
						   	  and   MISSIONE_RIGA.PG_OBBLIGAZIONE 	    	 = aObbScad.pg_obbligazione
						   	  and   MISSIONE_RIGA.PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario
						   	  and   MISSIONE.CD_CDS = MISSIONE_RIGA.CD_CDS
						   	  and   MISSIONE.CD_UNITA_ORGANIZZATIVA = MISSIONE_RIGA.CD_UNITA_ORGANIZZATIVA
						   	  and   MISSIONE.ESERCIZIO = MISSIONE_RIGA.ESERCIZIO
						   	  and   MISSIONE.PG_MISSIONE = MISSIONE_RIGA.PG_MISSIONE
							  and   MISSIONE.STATO_COFI <> STATO_ANNULLATO
							  for update nowait)
	  loop
	  	  begin
		  	  -- se miss ass anticipo allora
			  if lMissione.pg_anticipo is not null then
				  select ant.*
				  into lAnticipo
				  from anticipo ant
				  where ant.cd_cds = lMissione.cd_cds_anticipo
				  and   ant.cd_unita_organizzativa = lMissione.cd_uo_anticipo
				  and   ant.esercizio = lMissione.esercizio_anticipo
				  and   ant.pg_anticipo = lMissione.pg_anticipo
				  for update nowait;

			  	  lSaldo := lSaldo + lMissione.im_totale_missione - lAnticipo.im_anticipo;
			  else
			  	  lSaldo := lSaldo + lMissione.im_totale_missione ;
			  end if;
	  	  exception when no_data_found then
		  			lSaldo := lSaldo + lMissione.im_totale_missione ;
	  	  end;
	  end loop;
	  return lSaldo;
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo agli ANTICIPI associati ad una scadenza obbligazione.
 -- -------------------------------------------------------------------------------
 function getSaldoAnticipo(aObbScad obbligazione_scadenzario%rowtype) return number is
 lSaldo number(15,2);
 begin
	  lSaldo := 0;
 	  for lAnticipo in (select * from ANTICIPO
	  	  			   	   	  where CD_CDS_OBBLIGAZIONE         = aObbScad.cd_cds
						   	  and	ESERCIZIO_OBBLIGAZIONE 		= aObbScad.esercizio
						   	  and	ESERCIZIO_ORI_OBBLIGAZIONE 	= aObbScad.esercizio_originale
						   	  and   PG_OBBLIGAZIONE 	    	= aObbScad.pg_obbligazione
						   	  and   PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario
							  and   STATO_COFI <> STATO_ANNULLATO
							  for update nowait)
	  loop
		  	 lSaldo := lSaldo + lAnticipo.im_anticipo;
	  end loop;
	  return lSaldo;
 end;
 function esisteSpesaFondo(aObbScad obbligazione_scadenzario%rowtype) return boolean is
 lNumSpeseFondo number;
 begin
 	  select count(*)
	  into lNumSpeseFondo
	  from fondo_spesa
	  where CD_CDS_OBBLIGAZIONE = aObbScad.cd_cds
	  and ESERCIZIO_OBBLIGAZIONE = aObbScad.esercizio
	  and ESERCIZIO_ORI_OBBLIGAZIONE = aObbScad.esercizio_originale
	  and PG_OBBLIGAZIONE = aObbScad.pg_obbligazione
	  and PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario;
	  if lNumSpeseFondo > 0 then
	  	 return TRUE;
	  else
	  	  return FALSE;
	  end if;
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a tutti i DOC GEN associati ad una scadenza obbli.
 -- esclusi quelli che sono trattati anche come spese fondo (documenti di reintegro).
 -- -------------------------------------------------------------------------------
 function getSaldoGenerico(aObbScad obbligazione_scadenzario%rowtype) return number is
 lSaldoE number(15,2) ;
 lSaldoS number(15,2) ;
 begin
	  lSaldoE := 0;
	  lSaldoS := 0;
 	  for lDocGenericoRiga in (select doc.*, tipo.ti_entrata_spesa from documento_generico_riga doc, tipo_documento_amm tipo
	  	  			   	   	  where doc.CD_TIPO_DOCUMENTO_AMM       = tipo.CD_TIPO_DOCUMENTO_AMM
							  and   doc.CD_CDS_OBBLIGAZIONE         = aObbScad.cd_cds
						   	  and	doc.ESERCIZIO_OBBLIGAZIONE      = aObbScad.esercizio
        	                                          and   doc.ESERCIZIO_ORI_OBBLIGAZIONE  = aObbScad.esercizio_originale
						   	  and   doc.PG_OBBLIGAZIONE 	        = aObbScad.pg_obbligazione
						   	  and   doc.PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario
							  and   STATO_COFI <> STATO_ANNULLATO
							  for update nowait)
	  loop
	  	  if not esisteSpesaFondo(aObbScad) then
		  	  if lDocGenericoRiga.TI_ENTRATA_SPESA = 'E' then
			  	 lSaldoE := lSaldoE + lDocGenericoRiga.im_riga;
			  end if;
		  	  if lDocGenericoRiga.TI_ENTRATA_SPESA = 'S' then
			  	 lSaldoS := lSaldoS + lDocGenericoRiga.im_riga;
			  end if;
	  	  else
		  	  lSaldoE := 0;
			  lSaldoS := 0;
		  end if;

	  end loop;
	  return abs(lSaldoE - lSaldoS);
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a tutte le FATTURE ATTIVE associate ad una scadenza obbligazione.
 -- -------------------------------------------------------------------------------
 function getSaldoFatturaA(aObbScad obbligazione_scadenzario%rowtype) return number is
 lSaldo number(15,2);
 begin
	  lSaldo := 0;
 	  for lFatturaAttRiga in (select * from fattura_attiva_riga
	  	  			   	   	  where CD_CDS_OBBLIGAZIONE         = aObbScad.cd_cds
						   	  and	ESERCIZIO_OBBLIGAZIONE 	    = aObbScad.esercizio
                                                	  and   ESERCIZIO_ORI_OBBLIGAZIONE  = aObbScad.esercizio_originale
						   	  and   PG_OBBLIGAZIONE 	    = aObbScad.pg_obbligazione
						   	  and   PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario
							  and   STATO_COFI <> STATO_ANNULLATO
							  for update nowait)
	  loop
		  	 lSaldo := lSaldo + lFatturaAttRiga.im_imponibile + lFatturaAttRiga.im_iva;
	  end loop;
	  return lSaldo;
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a tutte le FATTURE PASSIVE associate ad una scadenza obbligazione.
 -- -------------------------------------------------------------------------------
 function getSaldoFatturaP(aObbScad obbligazione_scadenzario%rowtype, pg_exec number) return number is
 lSaldo number(15,2);
 lLettera lettera_pagam_estero%rowtype;
 lFatturaSospeso boolean;
 lFatturaLettera boolean;
 lFatturaBene boolean;
 lFatturaIntraUe boolean;
 lFatturaExtraUe boolean;
 lFatturaCommerciale boolean;
 lFatturaSanMarinoSenzaIva boolean;
 lNumScadObb number;
 lSommaScadenze boolean;
 lTotScadenze number(15,2);
 lIva number(15,2);
 lObb obbligazione%rowtype;
 begin
 	  -- Selezioniamo L'obbligazione relativa alla scadenza
  	  select *
	  into lObb
	  from obbligazione
	  where cd_cds = aObbScad.cd_cds
	  and   esercizio = aObbScad.esercizio
  	  and   esercizio_originale = aObbScad.esercizio_originale
	  and   pg_obbligazione = aObbScad.pg_obbligazione
	  for update nowait;

	  -- Azzariamo le variabili
	  lSaldo := 0;
	  lIva := 0;
	  -- Vediamo se la testata di fattura risulta essere associata ad un sospeso
   	   -- Vediamo se la fattura risulta associata a compenso
 	   -- Passo 1) Selezioniamo la testata di fattura

	   for lFatturaPasTestata in (select *
						  		   from fattura_passiva
						  		   where (cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva)
						  		   		 in (select cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva
						  				 	 from fattura_passiva_riga
						  					 where CD_CDS_OBBLIGAZIONE         =  aObbScad.cd_cds
						  					 and   ESERCIZIO_OBBLIGAZIONE 	   =  aObbScad.esercizio
                                                                                       	 and   ESERCIZIO_ORI_OBBLIGAZIONE  =  aObbScad.esercizio_originale
						  					 and   PG_OBBLIGAZIONE 		   =  aObbScad.pg_obbligazione
						  					 and   PG_OBBLIGAZIONE_SCADENZARIO =  aObbScad.pg_obbligazione_scadenzario
						  					 and   STATO_COFI 				   <> STATO_ANNULLATO)
						  		   and fl_congelata = 'N'
						  		   for update nowait)

 		   loop


  		   -- Vediamo se la fattura e di tipo bene o servizio
  		   if lFatturaPasTestata.ti_bene_servizio = 'B' or
		   	  lFatturaPasTestata.ti_bene_servizio = '*' then
  		   	  lFatturaBene := true;
  		   else
  		   	  lFatturaBene := false;
  		   end if;

  		   -- Vediamo se la fattura e di Istituzionale o Commerciale
  		   if lFatturaPasTestata.ti_istituz_commerc='C' then
  		   	  lFatturaCommerciale := true ;
  		   else
  		   	  lFatturaCommerciale := False ;
  		   end if;

  		   -- Vediamo se la fattura e intra_ue
  		   if lFatturaPasTestata.fl_intra_ue= 'Y' then
  		   	  lFatturaIntraUe := true;
  		   else
  		   	  lFatturaIntraUe := false;
  		   end if;

  		   -- Vediamo se la fattura e intra_ue
  		   if lFatturaPasTestata.fl_extra_ue= 'Y' then
  		   	  lFatturaExtraUe := true;
  		   else
  		   	  lFatturaExtraUe := false;
  		   end if;

  		   -- Vediamo se la fattura e intra_ue
  		   if lFatturaPasTestata.fl_san_marino_senza_iva ='Y' then
  		   	  lFatturaSanMarinoSenzaIva := true;
  		   else
  		   	  lFatturaSanMarinoSenzaIva := false;
  		   end if;

 		   -- Vediamo se risulta associato ad un 1210
 		   begin
 			   select let.*
 			   into lLettera
 			   from lettera_pagam_estero let
 			   where let.cd_cds 	            = lFatturaPasTestata.cd_cds
 			   and	 let.cd_unita_organizzativa = lFatturaPasTestata.cd_unita_organizzativa
 			   and	 let.esercizio 				= lFatturaPasTestata.esercizio_lettera
 			   and   let.pg_lettera				= lFatturaPasTestata.pg_lettera
 			   for update nowait;

 		   	   lFatturaLettera := true;

 		   	   -- Vediamo se risulta associato ad un sospeso
 		   	   if lLettera.cd_sospeso is not null then
 		   	   	  lFatturaSospeso := true;
 		   	   else
 		   	   	  lFatturaSospeso := false;
 		       end if;

 		   	   -- Vediamo se sulla fattura insisto più scadenze di obbligazioni
 		   	   select count(distinct pg_obbligazione_scadenzario)
 		   	   into lNumScadObb
 		   	   from fattura_passiva_riga
 		   	   where cd_cds                 = lFatturaPasTestata.cd_cds
 		   	   and   cd_unita_organizzativa = lFatturaPasTestata.cd_unita_organizzativa
 		   	   and   esercizio 				= lFatturaPasTestata.esercizio
 		   	   and   pg_fattura_passiva 	= lFatturaPasTestata.pg_fattura_passiva;

			   if lNumScadObb = 1 or lNumScadObb = 0 then
	 		   	   -- Vediamo se sulla fattura insisto più obbligazioni
	 		   	   select count(distinct pg_obbligazione)
	 		   	   into lNumScadObb
	 		   	   from fattura_passiva_riga
	 		   	   where cd_cds                 = lFatturaPasTestata.cd_cds
	 		   	   and   cd_unita_organizzativa = lFatturaPasTestata.cd_unita_organizzativa
	 		   	   and   esercizio 		= lFatturaPasTestata.esercizio
	 		   	   and   pg_fattura_passiva 	= lFatturaPasTestata.pg_fattura_passiva;
			   end if;
 		   	   if lNumScadObb > 1 then
 		   	   	  lSommaScadenze := true;
 		   	   else
 		   	   	  lSommaScadenze := false;
 		   	   end if;
 		   exception
 		   when no_data_found then
 	   	   		lFatturaLettera := false;
 		   end;
 		   -- Gestione casistica
   	 	   if lFatturaLettera then
 		  	  if lFatturaSospeso then
 			  	 if lSommaScadenze then
 				  	-- In questo caso la scadenza risulta:
 				  	-- 1) Legata ad una fattura_riga
 				  	-- 2) La fattura_riga ha una lettera
 				  	-- 3) La fattura in esame risulta avere più righe
 				  	--    a cui sono legate scadenze di obbligazioni DIVERSE.

 				  	-- lSaldo viene impostato a aObbScad.im_associato_doc_amm;
 				  	-- in modo da far quadrare i controlli fatti in unscita dalla GETSALDODOCAMM
 				  	lSaldo := aObbScad.im_associato_doc_amm;

 				  	-- Vengono però eseguiti i seguenti controlli
 				  	-- in questo caso deve infatti risultare che la somma(im_associato_doc_amm)
 				  	-- di tutte le scadenze associate alla fattura in esame deve coincidere
 				  	-- con lLettera.im_pagamento + eventuale (lIva)
 				  	select nvl(sum(nvl(im_associato_doc_amm,0)),0)
 				  	into lTotScadenze
 				  	from obbligazione_scadenzario obbs, fattura_passiva_riga fatr
 				  	where obbs.cd_cds                      = fatr.cd_cds_obbligazione
 				  	and   obbs.esercizio                   = fatr.esercizio_obbligazione
 				  	and   obbs.esercizio_originale         = fatr.esercizio_ori_obbligazione
 				  	and   obbs.pg_obbligazione             = fatr.pg_obbligazione
 				  	and   obbs.pg_obbligazione_scadenzario = fatr.pg_obbligazione_scadenzario
 				  	and   fatr.cd_cds                      = lFatturaPasTestata.cd_cds
 				  	and   fatr.esercizio                   = lFatturaPasTestata.esercizio
 				  	and   fatr.cd_unita_organizzativa      = lFatturaPasTestata.cd_unita_organizzativa
 				  	and   fatr.pg_fattura_passiva          = lFatturaPasTestata.pg_fattura_passiva;

 			  	  	-- se intraue istit sanmarino istit
 			  	  	if not lFatturaCommerciale and lFatturaBene and (not lFatturaExtraUe) and
 					   (lFatturaIntraUe  or lFatturaSanMarinoSenzaIva) then
 			  	  	   lIva := lFatturaPasTestata.im_totale_iva;
 			  	  	end if;

 				  	if lTotScadenze <> lLettera.im_pagamento + lIva then
 					   IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI520,lObb,' scad: '||aObbScad.pg_obbligazione_scadenzario),'delta:'||fnum(lTotScadenze - lLettera.im_pagamento + lIva),'SOCD');
 				    end if;
 			  	 else -- La fattura risulta legata ad una scadenza di obbligazione

 			  	  	-- se intraue istit sanmarino istit
 			  	  	if not lFatturaCommerciale and lFatturaBene and (not lFatturaExtraUe) and
 					   (lFatturaIntraUe  or lFatturaSanMarinoSenzaIva) then
 			  	  	   lIva := lFatturaPasTestata.im_totale_iva;
 			  	  	end if;
 			 	 	lSaldo := lSaldo + lLettera.im_pagamento + lIva;
 			  	 end if;

 			  else -- Lettera ma Nessun sospeso
 			 	 -- non bisogna controllare niente
 				 lSaldo := aObbScad.im_associato_doc_amm;
 			  end if; -- Lettera ma Nessun sospeso
   	 	   else -- Senza Lettera e senza Sospeso
 		   		for lFatturaPasRiga in (select * from fattura_passiva_riga
 				  	  			   	   	where CD_CDS_OBBLIGAZIONE         = aObbScad.cd_cds
 									   	and   ESERCIZIO_OBBLIGAZIONE 	  = aObbScad.esercizio
 									   	and   ESERCIZIO_ORI_OBBLIGAZIONE  = aObbScad.esercizio_originale
 									   	and   PG_OBBLIGAZIONE 		  = aObbScad.pg_obbligazione
 									   	and   PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario
										and   cd_cds 			  = lFatturaPasTestata.cd_cds
										and   cd_unita_organizzativa 	  = lFatturaPasTestata.cd_unita_organizzativa
										and   esercizio 		  = lFatturaPasTestata.esercizio
										and   pg_fattura_passiva	  = lFatturaPasTestata.pg_fattura_passiva
 										and   STATO_COFI <> STATO_ANNULLATO
 										for update nowait)
 				loop
 					begin
 				  		 -- se fatt = nota credito
 				  		 if lFatturaPasTestata.ti_fattura = NOTA_CREDITO then
 			  	      	 	-- se intraue comm sanmarino comm
 							if lFatturaCommerciale and (not lFatturaExtraUe) and
 							   (lFatturaIntraUe or lFatturaSanMarinoSenzaIva) then
 							 	-- Calcoliamo il saldo
								lSaldo := lSaldo - lFatturaPasRiga.im_imponibile ;
 							else
 							 	if lFatturaCommerciale and (lFatturaExtraUe) and
 						 	       not (lFatturaIntraUe or lFatturaSanMarinoSenzaIva)
							       and not lFatturaBene then
								   -- Calcoliamo il saldo
							   	   lSaldo := lSaldo - lFatturaPasRiga.im_imponibile ;
							   else
							   	   -- Calcoliamo il saldo
							   	   lSaldo := lSaldo - (lFatturaPasRiga.im_imponibile + lFatturaPasRiga.im_iva);
							   end if;
 							end if;
 				  		 else
 							if lFatturaCommerciale and (not lFatturaExtraUe) and
 						 	   (lFatturaIntraUe or lFatturaSanMarinoSenzaIva) then
 						       -- Calcoliamo il saldo
							   lSaldo := lSaldo + lFatturaPasRiga.im_imponibile ;
 							else
 					 	 	   if lFatturaCommerciale and (lFatturaExtraUe) and
 						 	      not (lFatturaIntraUe or lFatturaSanMarinoSenzaIva)
							      and not lFatturaBene then
							   	  -- Calcoliamo il saldo
								  lSaldo := lSaldo + lFatturaPasRiga.im_imponibile ;
							   else
							   	   -- Calcoliamo il saldo
							   	   lSaldo := lSaldo +(lFatturaPasRiga.im_imponibile + lFatturaPasRiga.im_iva);
							   end if;
 							end if;
 				  		 end if;

 					end;
 				end loop; -- fine ciclo sulle righe di fattura
   	 	   end if; -- Senza Lettera e senza Sospeso
	   end loop;
	  return lSaldo;
 end;
 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a i COMPENSI associati ad una scadenza obbligazione.
 -- -------------------------------------------------------------------------------
 function getSaldoCompenso(aObbScad obbligazione_scadenzario%rowtype) return number is
 lSaldo number (15,2);
 lAnticipo Anticipo%rowtype;
 begin
	  lSaldo := 0;
 	  for lCompenso in (select compenso.cd_cds_missione, compenso.cd_uo_missione, 
 	  	                       compenso.esercizio, compenso.pg_missione,
 	  	                       compenso.im_netto_percipiente,
 	  	                       compenso.im_totale_compenso,
                               compenso_riga.im_totale_riga_compenso
 	                    from compenso_riga, compenso
	  	  			    where compenso_riga.CD_CDS_OBBLIGAZIONE = aObbScad.cd_cds
						and   compenso_riga.ESERCIZIO_OBBLIGAZIONE = aObbScad.esercizio
					   	and   compenso_riga.ESERCIZIO_ORI_OBBLIGAZIONE = aObbScad.esercizio_originale
						and   compenso_riga.PG_OBBLIGAZIONE = aObbScad.pg_obbligazione
						and   compenso_riga.PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario
						and   compenso_riga.cd_cds = compenso.cd_cds
						and   compenso_riga.cd_unita_organizzativa = compenso.cd_unita_organizzativa
						and   compenso_riga.esercizio = compenso.esercizio
						and   compenso_riga.pg_compenso = compenso.pg_compenso
						and   compenso.STATO_COFI <> STATO_ANNULLATO
						for update nowait)
	  loop
	  	  begin
		  	   select ant.*
			   into lAnticipo
			   from missione mis, anticipo ant
			   where mis.cd_cds                 = lCompenso.cd_cds_missione
			   and   mis.cd_unita_organizzativa = lCompenso.cd_uo_missione
			   and   mis.esercizio 				= lCompenso.esercizio
			   and   mis.pg_missione 			= lCompenso.pg_missione
			   and   mis.cd_cds_anticipo 		= ant.cd_cds
			   and   mis.cd_uo_anticipo 		= ant.cd_unita_organizzativa
			   and   mis.esercizio_anticipo 	= ant.esercizio
			   and   mis.pg_anticipo 			= ant.pg_anticipo
			   and   ant.cd_cds_obbligazione    = aObbScad.cd_cds
			   and   ant.esercizio_obbligazione	= aObbScad.esercizio
			   and   ant.esercizio_ori_obbligazione = aObbScad.esercizio_originale
			   and   ant.pg_obbligazione 		= aObbScad.pg_obbligazione
			   and   ant.pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario
			   for update nowait;

			   if lAnticipo.im_anticipo <= lCompenso.IM_NETTO_PERCIPIENTE then
			   	  lSaldo := lSaldo + lCompenso.im_totale_riga_compenso - lAnticipo.im_anticipo;
			   else
			   	  lSaldo := lSaldo + lCompenso.im_totale_riga_compenso - lCompenso.im_netto_percipiente;
			   end if;

		  exception when no_data_found then
		  			lSaldo := lSaldo + lCompenso.im_totale_riga_compenso;
		  end;
	  end loop;
	  return lSaldo;
 end;

 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo relativo a CONTRIBUTO_RITENUTE associate ad una scadenza obbligazione.
 -- -------------------------------------------------------------------------------
--  function getSaldoRitenute(aObbScad obbligazione_scadenzario%rowtype) return number is
--  lSaldo number(15,2) ;
--  begin
-- 	  lSaldo := 0;
--  	  for lRitenute in (select cr.* from CONTRIBUTO_RITENUTA cr, compenso co
-- 	  	  			   	   	  where co.CD_CDS = cr.cd_cds
-- 							  and   co.CD_UNITA_ORGANIZZATIVA = cr.CD_UNITA_ORGANIZZATIVA
-- 							  and   co.ESERCIZIO = cr.ESERCIZIO
-- 							  and   co.PG_COMPENSO = cr.PG_COMPENSO
-- 							  and   cr.CD_CDS_OBBLIGAZIONE         = aObbScad.cd_cds
-- 						   	  and	cr.ESERCIZIO_OBBLIGAZIONE      = aObbScad.esercizio
--						     	  and   cr.ESERCIZIO_ORI_OBBLIGAZIONE  = aObbScad.esercizio_originale
-- 						   	  and   cr.PG_OBBLIGAZIONE 	       = aObbScad.pg_obbligazione
-- 						   	  and   cr.PG_OBBLIGAZIONE_SCADENZARIO = aObbScad.pg_obbligazione_scadenzario
-- 							  and   co.STATO_COFI <> STATO_ANNULLATO
-- 							  for update nowait)
-- 	  loop
-- 		  	 lSaldo := lSaldo + abs(lRitenute.imponibile);
-- 	  end loop;
-- 	  return lSaldo;
--  end;

 -- -------------------------------------------------------------------------------
 -- Restituisce il saldo totale dei documenti amm associati ad una scadenza obbligazione
 -- -------------------------------------------------------------------------------
 function GETSALDODOCAMM(aObbScad obbligazione_scadenzario%rowtype, aEsisteSpesa in out boolean, aReintegrata in out boolean,pg_exec number) return number is
 lSaldoTotale number(15,2);
 lSaldoGenerico number;
 begin
 	  lSaldoTotale := 0;
	  aEsisteSpesa := false;
	  aReintegrata := false;
 	  lSaldoTotale := getSaldoGenerico (aObbScad) + getSaldoFatturaA(aObbScad) + getSaldoFatturaP(aObbScad,pg_exec) + getSaldoCompenso(aObbScad)  + getSaldoAnticipo(aObbScad) + getSaldoMissione(aObbScad) + getSaldoFondo(aObbScad,aEsisteSpesa,aReintegrata); --+ getSaldoRitenute(aObbScad)
	  return lSaldoTotale;
 end;

 function EsisteScadRiportata(aObbScad obbligazione_scadenzario%rowtype) return boolean as
 lObbRip obbligazione%rowtype;
 lEsisteScadRip number;
 lControllo boolean := false;
 begin
 	  begin
	 	  select *
		  into lObbRip
		  from obbligazione
		  where cd_cds_ori_riporto = aObbScad.cd_cds
		  and   esercizio_ori_riporto = aObbScad.esercizio
		  and   esercizio_ori_ori_riporto = aObbScad.esercizio_originale
		  and   pg_obbligazione_ori_riporto = aObbScad.pg_obbligazione
		  and   stato_obbligazione <> 'S';

		  lcontrollo  := true;

 	  exception
	  when no_data_found then
	  	   return false;
	  end;

	  if lControllo then
	 	  select count(*)
		  into lEsisteScadRip
		  from obbligazione_scadenzario
		  where cd_cds = lObbRip.cd_cds
		  and   esercizio = lObbRip.esercizio
		  and   esercizio_originale = lObbRip.esercizio_originale
		  and   pg_obbligazione = lObbRip.pg_obbligazione
		  and   pg_obbl_scad_ori_riporto = aObbScad.pg_obbligazione_scadenzario;

		  if lEsisteScadRip > 0 then
		  	 return true;
		  else
		  	 return false;
		  end if;
	  end if;

	  return false;
 end;

 function EsisteScadRiportata(aAccScad accertamento_scadenzario%rowtype) return boolean as
 lAccRip accertamento%rowtype;
 lEsisteScadRip number;
 lControllo boolean := false;
 begin
 	  begin
	 	  select *
		  into lAccRip
		  from accertamento
		  where cd_cds_ori_riporto = aAccScad.cd_cds
		  and   esercizio_ori_riporto = aAccScad.esercizio
                  and   esercizio_ori_ori_riporto = aAccScad.esercizio_originale
		  and   pg_accertamento_ori_riporto = aAccScad.pg_accertamento
		  and   dt_cancellazione is null;

		  lcontrollo  := true;

 	  exception
	  when no_data_found then
	  	   return false;
	  end;

	  if lControllo then
	 	  select count(*)
		  into lEsisteScadRip
		  from accertamento_scadenzario
		  where cd_cds = lAccRip.cd_cds
		  and   esercizio = lAccRip.esercizio
                  and   esercizio_originale = lAccRip.esercizio_originale
		  and   pg_accertamento = lAccRip.pg_accertamento
		  and   pg_acc_scad_ori_riporto = aAccScad.pg_accertamento_scadenzario;

		  if lEsisteScadRip > 0 then
		  	 return true;
		  else
		  	 return false;
		  end if;
	  end if;

	  return false;
 end;

 procedure job_mar_primi00(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2, isModifica char) is
  aTSNow date;
  aUser varchar2(20);
  aEndT date;
  aStartT date;
  aEnd varchar2(80);
  aStart varchar2(80);
  aDelta varchar2(80);
  aMsgTipoMar varchar2(30);
  aNumAcc number:=0;
  aNumObb number:=0;
  lCds unita_organizzativa%rowtype;
 begin -- MAIN
  	  aTSNow:=sysdate;
  	  aUser:=IBMUTL200.getUserFromLog(pg_exec);
	  if isModifica = 'Y' then
	   	 aMsgTipoMar:='MARTELLO';
	  else
	   	  aMsgTipoMar:='VERIFICA';
	  end if;
  	  aStartT:=sysdate;
  	  aStart:=to_char(sysdate,'YYYYMMDD HH:MI:SS');
  	  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_MAR_OBBACC, job, D_PRIMI||'-'||aMsgTipoMar||' (disallinamenti primi documenti). Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'),D_PRIMI);
	  begin -- livello 1
	   	   IBMUTL200.logInf(pg_exec, D_PRIMI||'-START  at: '||aStart||' es.'||aEs||' cds.'||aCdCds,aEs||aCdCds,'SOC');
		   -- --------------------------------------
		   -- Inizio procedura di controllo/martello
		   -- --------------------------------------
		   begin
		   		select *
				into lCds
				from unita_organizzativa
				where cd_unita_organizzativa = aCdCds
				and fl_cds = 'Y'
				for update nowait;
		   exception
		   when NO_DATA_FOUND then
		   		IBMERR001.RAISE_ERR_GENERICO('Cds non trovato:'||aCdCds);
		   end ;

		   declare
		    aTotScad number(15,2);
		    aTotScadVoce number(15,2);
		   begin -- livello 2
			    -- --------------------------------------------------------------
			    -- Ciclo su tutti gli accertamenti del cds in esame per esercizio
				-- --------------------------------------------------------------
			    for aAcc in (select * from accertamento
						 	 where esercizio = aEs
							 and cd_cds = aCdCds
							 for update nowait)
			    loop  -- accertamento
			  		aNumAcc:=aNumAcc+1;
			    	-- -------------------------------------------------------------------------
			    	-- Ciclo su tutte le scadenze di accertamento del cds in esame per esercizio
					-- -------------------------------------------------------------------------
					for aAccS in (select * from accertamento_scadenzario
					 	 	   	   where cd_cds = aAcc.cd_cds
                                                                   and esercizio = aAcc.esercizio
								   and esercizio_originale = aAcc.esercizio_originale
								   and pg_accertamento = aAcc.pg_accertamento
								  for update nowait)
					loop -- accertamento_scadenzario
				         select nvl(sum(im_voce),0) into aTotScadVoce
						 from accertamento_scad_voce
						 where cd_cds = aAccS.cd_cds
                                                 and esercizio = aAccS.esercizio
						 and esercizio_originale = aAccS.esercizio_originale
						 and pg_accertamento = aAccS.pg_accertamento
						 and pg_accertamento_scadenzario = aAccS.pg_accertamento_scadenzario;

				    	 -- ----------------------------------------------------------------
						 -- Check D_PRIMI005 ACC
						 -- --------------------------------------------------------------------------------
						 -- Controllo se l'importo della scadenza coincide con la somma delle voci associate
						 -- --------------------------------------------------------------------------------
						 -- MAX -- verificare se in caso che il cds sia ENTE il controllo è valido
				         if aTotScadVoce <> aAccS.im_scadenza then
				          IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI005,aAcc,' scad: '||aAccS.pg_accertamento_scadenzario),'delta:'||fnum(aAccS.im_scadenza-aTotScadVoce),'SOCD');
				     	 end if;
						 -- -------------------------------------------------------------------
				    	 -- Check D_PRIMI010 ACC
						 -- Importo ass. doc. cont. > importo associato doc. amm
						 -- -------------------------------------------------------------------
				         if aAccS.im_associato_doc_amm < aAccS.im_associato_doc_contabile then
				          IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI010,aAcc,' scad: '||aAccS.pg_accertamento_scadenzario),'delta:'||fnum(aAccS.im_associato_doc_amm - aAccS.im_associato_doc_contabile),'SOCD');
				     	 end if;
				    	 -- -------------------------------------------------------------------
						 -- Check D_PRIMI015 ACC
						 -- Saldo reversali collegate disallineato con tot documenti collegati:
						 -- ritorna im_associato_doc_contabile - aSaldoAutorizz. collegati
						 -- -------------------------------------------------------------------
				         declare
				          		aSaldoAutorizzRev number(15,2);
						 begin
						  	  select nvl(sum(im_reversale_riga),0)
							  into aSaldoAutorizzRev
							  from reversale r, reversale_riga rr
						  	  where rr.cd_cds = aAccS.cd_cds
                                                          and rr.esercizio_accertamento = aAccS.esercizio
							  and rr.esercizio_ori_accertamento = aAccS.esercizio_originale
							  and rr.pg_accertamento = aAccS.pg_accertamento
							  and rr.pg_accertamento_scadenzario = aAccS.pg_accertamento_scadenzario
				              and r.esercizio  =rr.esercizio
							  and r.cd_cds = rr.cd_cds
							  and r.pg_reversale = rr.pg_reversale
							  and r.stato <> STATO_ANNULLATO;

						  	  -- MAX -- verificare se in caso che il cds sia ENTE il controllo è valido
				          	  if aAccS.im_associato_doc_contabile <> aSaldoAutorizzRev then
				           	  	 IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI015,aAcc,' scad: '||aAccS.pg_accertamento_scadenzario),'delta:'||fnum(aAccS.im_associato_doc_contabile - aSaldoAutorizzRev),'SOCD');
				      	  	  end if;
						 end;
				    	 -- --------------------------------------------------------------------
						 -- Check D_PRIMI020 ACC
						 -- Controllo che il saldo di tutti i documenti amm associati alla scad
						 -- sia identica al valore del campo im_associato_doc_amm
						 -- --------------------------------------------------------------------
				         declare
				          		aSaldoAssDocAmmAcc number(15,2);
						  		-- cnrctb020.TIPO_ENTE ='ENTE';
						 begin
						 	  if not EsisteScadRiportata (aAccS) then
							  	  if aAcc.riportato = 'N' then
								 	  aSaldoAssDocAmmAcc := GETSALDODOCAMM(aAccS);
									  -- Quadriamo il saldo associato con doc amm
						          	  if aAccS.im_associato_doc_amm <> aSaldoAssDocAmmAcc then
						           	  	 IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI020,aAcc,' scad: '||aAccS.pg_ACCERTAMENTO_scadenzario),'delta:'||fnum(aAccS.im_associato_doc_amm - aSaldoAssDocAmmAcc),'SOCD');
						      	  	  end if;
							    	  -- --------------------------------------------------------------------
									  -- Check D_PRIMI025 ACC
									  -- controllo importo scadenza con imp ass doc amm
							    	  -- --------------------------------------------------------------------
									  if lCds.cd_tipo_unita <> cnrctb020.TIPO_ENTE then
									     if aAccS.im_associato_doc_amm <> aAccS.im_scadenza and aSaldoAssDocAmmAcc > 0 then
									   	    IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI025,aAcc,' scad: '||aAccS.pg_accertamento_scadenzario),'delta:'||fnum(aAccS.im_associato_doc_amm - aAccS.im_scadenza),'SOCD');
									     end if;
									  end if;
							  	  end if;
							  else -- esiste scadenza riportata
							  	   -- in questo caso controlliamo la scadenza solo se risulta avere im_associato_doc_amm = 0
								   -- in quanto per questa tipologia di scadenze la procedura di ribaltamento non cambia i riferimenti dei
								   -- documenti amministrativi associati,viceversa per le altre scadenze
								   -- ribaltate, anche i riferimenti nei doc amm associati risultano puntare alla scadenza ribaltata
								   -- potrebbe quindi succedere per le scadenze con im_associato a doc_amm = 0 che risultano
								   -- ribaltate (portate ad esercizio successivo), venga indotto un errore nell'esercizio attuale
								   -- che viene in questo modo rilevato
								   if aAccS.im_associato_doc_amm = 0 then
								 	  aSaldoAssDocAmmAcc := GETSALDODOCAMM(aAccS);
									  -- Quadriamo il saldo associato con doc amm
						          	  if aAccS.im_associato_doc_amm <> aSaldoAssDocAmmAcc then
						           	  	 IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI020,aAcc,' scad: '||aAccS.pg_ACCERTAMENTO_scadenzario),'delta:'||fnum(aAccS.im_associato_doc_amm - aSaldoAssDocAmmAcc),'SOCD');
						      	  	  end if;
							    	  -- --------------------------------------------------------------------
									  -- Check D_PRIMI025 ACC
									  -- controllo importo scadenza con imp ass doc amm
							    	  -- --------------------------------------------------------------------
									  if lCds.cd_tipo_unita <> cnrctb020.TIPO_ENTE then
									     if aAccS.im_associato_doc_amm <> aAccS.im_scadenza and aSaldoAssDocAmmAcc > 0 then
									   	    IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI025,aAcc,' scad: '||aAccS.pg_accertamento_scadenzario),'delta:'||fnum(aAccS.im_associato_doc_amm - aAccS.im_scadenza),'SOCD');
									     end if;
									  end if;
								   end if;
						 	  end if;
						 end;
					end loop; -- accertamento_scadenzario
					-- ----------------------------------------------
					-- Check D_PRIMI000 ACC
					-- Controllo cha la somma delle scadenze di accertamento sia pari all'importo
					-- dell'accertamento
					-- ----------------------------------------------
			    	select nvl(sum(im_scadenza),0) into aTotScad
					from accertamento_scadenzario
					where cd_cds = aAcc.cd_cds
                                        and esercizio = aAcc.esercizio
					and esercizio_originale = aAcc.esercizio_originale
					and pg_accertamento = aAcc.pg_accertamento;

				    if aTotScad <> aAcc.im_accertamento then
				       IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI000,aAcc,''),'delta:'||fnum(aAcc.im_accertamento-aTotScad),'SOCD');
				 	end if;
			    end loop; -- accertamento
				-- ***********************************************************
				-- ********************** OBBLIGAZIONE ***********************
				-- ***********************************************************
		 		-- --------------------------------------------------------------------
				-- Ciclo su tutte le Obbligazione relative al cds e esercizio in input.
		 		-- --------------------------------------------------------------------
			    for aObb in (select * from obbligazione
						 	 where esercizio = aEs
							 and cd_cds = aCdCds
							 and stato_obbligazione = STATO_DEFINITIVO
							 for update nowait)
			    loop -- obbligazioni
					aNumObb:=aNumObb+1;
					for aObbS in (select * from obbligazione_scadenzario
					 	 	   	  where cd_cds = aObb.cd_cds
								  and esercizio = aObb.esercizio
								  and esercizio_originale = aObb.esercizio_originale
								  and pg_obbligazione = aObb.pg_obbligazione
								  for update nowait)
					loop -- scadenze obbligazione
				         select nvl(sum(im_voce),0) into aTotScadVoce
						 from obbligazione_scad_voce
						 where cd_cds = aObbS.cd_cds
					         and esercizio = aObbS.esercizio
						 and esercizio_originale = aObbS.esercizio_originale
						 and pg_obbligazione = aObbS.pg_obbligazione
						 and pg_obbligazione_scadenzario = aObbS.pg_obbligazione_scadenzario;

				    	 -- ----------------------------------------------------------------
						 -- Check D_PRIMI505 OBB
						 -- Controllo se la somma delle voci coincide con l'importo della scadenza
				 		 -- --------------------------------------------------------------------
				         if aTotScadVoce <> aObbS.im_scadenza then
				          IBMUTL200.logInf(pg_exec,MSG_DIS_PRIMI(D_PRIMI505,aObb,'scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_scadenza-aTotScadVoce),'SOCD');
				     	 end if;
				    	 -- ----------------------------------------------------------------
				    	 -- Check D_PRIMI510 OBB
						 -- Controllo che l'importo ass a doc amm deve essere > dellimporto ass doc cont
				 		 -- --------------------------------------------------------------------
				         if aObbS.im_associato_doc_amm < aObbS.im_associato_doc_contabile then
				          IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI510,aObb,' scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_associato_doc_amm - aObbS.im_associato_doc_contabile),'SOCD');
				     	 end if;
				    	 -- ----------------------------------------------------------------
				    	 -- Check D_PRIMI515 OBB
				    	 -- Controllo che la somma dei mandati deve essere uguale im_associato_doc_contabile
				    	 -- ----------------------------------------------------------------
				         declare
				          aSaldoAutorizz number(15,2);
						 begin
							  select nvl(sum(im_mandato_riga),0) into aSaldoAutorizz
							  from mandato m, mandato_riga mr
						  	  where mr.cd_cds = aObbS.cd_cds
						          and mr.esercizio_obbligazione = aObbS.esercizio
							  and mr.esercizio_ori_obbligazione = aObbS.esercizio_originale
							  and mr.pg_obbligazione = aObbS.pg_obbligazione
							  and mr.pg_obbligazione_scadenzario = aObbS.pg_obbligazione_scadenzario
				              and m.esercizio  =mr.esercizio
							  and m.cd_cds = mr.cd_cds
							  and m.pg_mandato = mr.pg_mandato
							  and m.stato <> STATO_ANNULLATO;

					          if aObbS.im_associato_doc_contabile <> aSaldoAutorizz then
					           	 IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI515,aObb,' scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_associato_doc_contabile - aSaldoAutorizz),'SOCD');
					      	  end if;
						 end;
				    	 -- --------------------------------------------------------------------
						 -- Check D_PRIMI520 OBB
						 -- Controllo che la somma di tutti i documenti amm associati alla scad
						 -- deve essere uguale al campo im_associato_doc_amm
						 -- --------------------------------------------------------------------
				         declare
					          	aSaldoAssDocAmm number(15,2);
					  		  	lReintegrata boolean;
							  	lEsisteSpesa boolean;
						 begin
						 	  if not EsisteScadRiportata(aObbS) then
							  	  if aObb.riportato = 'N' then
									  aSaldoAssDocAmm := GETSALDODOCAMM(aObbS, lEsisteSpesa, lReintegrata, pg_exec);

								 	  if aObbS.im_associato_doc_amm <> aSaldoAssDocAmm then
					           	  	  	 IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI520,aObb,' scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_associato_doc_amm - aSaldoAssDocAmm),'SOCD');
								 	  end if;

									  if lCds.cd_tipo_unita <> cnrctb020.TIPO_ENTE then
									  	 if (lEsisteSpesa and lReintegrata) or (not lEsisteSpesa) then
							          	    if aSaldoAssDocAmm > 0 and aObbS.im_associato_doc_amm <> aObbS.im_scadenza then
							           	  	   IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI525,aObb,' scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_associato_doc_amm - aObbS.im_scadenza),'SOCD');
							      	  	    end if;
										 end if;
									  else
							          	  if aObbS.im_associato_doc_amm > aObbS.im_scadenza then
							           	  	 IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI525,aObb,' scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_associato_doc_amm - aObbS.im_scadenza),'SOCD');
							      	  	  end if;
									  end if;
							  	  end if;
						 	  else -- esiste scadenza riportata
							  	   -- in questo caso controlliamo la scadenza solo se risulta avere im_associato_doc_amm = 0
								   -- in quanto per questa tipologia di scadenze la procedura di ribaltamento non cambia i riferimenti dei
								   -- documenti amministrativi associati,viceversa per le altre scadenze
								   -- ribaltate, anche i riferimenti nei doc amm associati risultano puntare alla scadenza ribaltata
								   -- potrebbe quindi succedere per le scadenze con im_associato a doc_amm = 0 che risultano
								   -- ribaltate (portate ad esercizio successivo), venga indotto un errore nell'esercizio attuale
								   -- che viene in questo modo rilevato
								   if aObbS.im_associato_doc_amm = 0 then
									  aSaldoAssDocAmm := GETSALDODOCAMM(aObbS, lEsisteSpesa, lReintegrata, pg_exec);
								 	  if aObbS.im_associato_doc_amm <> aSaldoAssDocAmm then
					           	  	  	 IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI520,aObb,' scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_associato_doc_amm - aSaldoAssDocAmm),'SOCD');
								 	  end if;
									  if lCds.cd_tipo_unita <> cnrctb020.TIPO_ENTE then
									  	 if (lEsisteSpesa and lReintegrata) or (not lEsisteSpesa) then
							          	    if aSaldoAssDocAmm > 0 and aObbS.im_associato_doc_amm <> aObbS.im_scadenza then
							           	  	   IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI525,aObb,' scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_associato_doc_amm - aObbS.im_scadenza),'SOCD');
							      	  	    end if;
										 end if;
									  else
							          	  if aObbS.im_associato_doc_amm > aObbS.im_scadenza then
							           	  	 IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI525,aObb,' scad: '||aObbS.pg_obbligazione_scadenzario),'delta:'||fnum(aObbS.im_associato_doc_amm - aObbS.im_scadenza),'SOCD');
							      	  	  end if;
									  end if;
								   end if;
							  end if;
						 end;
					end loop; -- scadenze obbligazione
					-- ------------------------------------------------------------------
					-- Check D_PRIMI500 OBB
					-- Controllo che la somma delle scadenze coincida con l'obbligazione
					-- ------------------------------------------------------------------
				    select nvl(sum(im_scadenza),0) into aTotScad
					from obbligazione_scadenzario
					where cd_cds = aObb.cd_cds
				        and esercizio = aObb.esercizio
					and esercizio_originale = aObb.esercizio_originale
					and pg_obbligazione = aObb.pg_obbligazione;

					if aTotScad <> aObb.im_obbligazione then
				       IBMUTL200.logInf(pg_exec, MSG_DIS_PRIMI(D_PRIMI500,aObb,''),'delta:'||fnum(aObb.im_obbligazione-aTotScad),'SOCD');
				 	end if;
			    end loop; -- obbligazione
		   end; -- livello 2
	   	   -- Fine procedura di controllo/martello
	   	   aEndT:=sysdate;
	   	   aEnd:=to_char(aEndT,'YYYYMMDD HH:MI:SS');
	   	   aDelta:=to_char((aEndT-aStartT)*24*3600,'999999');
	   	   IBMUTL200.logInf(pg_exec,D_PRIMI||'-PROCESSATE '||aNumObb||' OBB + '||aNumAcc||' ACC',aEs||aCdCds,'SOC');
	   	   IBMUTL200.logInf(pg_exec,D_PRIMI||'-END at: '||aEnd||' tot exec time(s):'||aDelta||' es.'||aEs||' cds.'||aCdCds,aEs||aCdCds,'SOC');
	  EXCEPTION
	  WHEN OTHERS THEN
	   	   ROLLBACK;
	   	   IBMUTL200.logErr(pg_exec, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,'SOC');
	  END; -- livello 1
 end; -- MAIN
end;
