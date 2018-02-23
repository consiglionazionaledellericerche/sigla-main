--------------------------------------------------------
--  DDL for Package Body CNRCTB002
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB002" is
 procedure getVociEpPgiro(aEs number, aTiAppartenenza char, aTiGestione char, aCdEv varchar2, aCosto out voce_ep%rowtype, aRicavo out voce_ep%rowtype) is
  aAssPgiro ass_partita_giro%rowtype;
  aAssVoceEp ass_ev_voceep%rowtype;
 begin
  begin
   select * into aAssVoceEp from ass_ev_voceep where
        esercizio = aEs
    and ti_appartenenza = aTiAppartenenza
    and ti_gestione = aTiGestione
    and cd_elemento_voce = aCdEv;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto economico associato a conto:'||aCdEv||' appartenenza:'||aTiAppartenenza||' gestione:'||aTiGestione||' non trovato');
  end;
  if aTiGestione = CNRCTB001.GESTIONE_ENTRATE then
         aRicavo:=getVoceEp(aEs, aAssVoceEp.cd_voce_ep);
         begin
		  select * into aAssPgiro from ass_partita_giro where
		        esercizio = aEs
		    and ti_appartenenza = aTiAppartenenza
		    and ti_gestione = aTiGestione
            and cd_voce = aCdEv;
		 exception when NO_DATA_FOUND then
          IBMERR001.RAISE_ERR_GENERICO('Conto su partita associato a conto:'||aCdEv||' non trovato');
		 end;
		 begin
		  select * into aAssVoceEp from ass_ev_voceep where
		       esercizio = aAssPgiro.esercizio
		   and ti_appartenenza = aAssPgiro.ti_appartenenza_clg
		   and ti_gestione = aAssPgiro.ti_gestione_clg
		   and cd_elemento_voce = aAssPgiro.cd_voce_clg;
         exception when NO_DATA_FOUND then
          IBMERR001.RAISE_ERR_GENERICO('Conto economico associato a conto:'||aAssPgiro.cd_voce_clg||' appartenenza:'||aAssPgiro.ti_appartenenza_clg||' gestione:'||aAssPgiro.ti_gestione_clg||' non trovato');
		 end;
         aCosto:=getVoceEp(aEs, aAssVoceEp.cd_voce_ep);
  else
         aCosto:=getVoceEp(aEs, aAssVoceEp.cd_voce_ep);
         begin
		  select * into aAssPgiro from ass_partita_giro where
		        esercizio = aEs
		    and ti_appartenenza_clg = aTiAppartenenza
		    and ti_gestione_clg = aTiGestione
            and cd_voce_clg = aCdEv;
		 exception when NO_DATA_FOUND then
          IBMERR001.RAISE_ERR_GENERICO('Conto su partita associato a conto:'||aCdEv||' non trovato');
		 end;
		 begin
        	 select * into aAssVoceEp from ass_ev_voceep where
        	      esercizio = aAssPgiro.esercizio
        	  and ti_appartenenza = aAssPgiro.ti_appartenenza
        	  and ti_gestione = aAssPgiro.ti_gestione
        	  and cd_elemento_voce = aAssPgiro.cd_voce;
         exception when NO_DATA_FOUND then
          IBMERR001.RAISE_ERR_GENERICO('Conto economico associato a conto:'||aAssPgiro.cd_voce||' appartenenza:'||aAssPgiro.ti_appartenenza||' gestione:'||aAssPgiro.ti_gestione||' non trovato');
		 end;
         aRicavo:=getVoceEp(aEs, aAssVoceEp.cd_voce_ep);
  end if;
 end;


 function getVoceEp(aEs number, aCdVoce varchar2) return voce_ep%rowtype is
  aVoceEp voce_ep%rowtype;
 begin
  begin
   select * into aVoceEp from voce_ep where
          esercizio = aEs
      and cd_voce_ep = aCdVoce;
	 return aVoceEp;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto ep non trovato:'||aEs||'-'||aCdVoce);
  end;
 end;

 -- Estrae la descrizionr di un conto per chiave
 function getDesVoceEp(aEs number, aCdVoce varchar2) return VARCHAR Is
 des_conto voce_ep.ds_voce_ep%Type;
 begin
  begin
   select ds_voce_ep
   into des_conto
   from voce_ep where
          esercizio = aEs
      and cd_voce_ep = aCdVoce;
  return des_conto;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto ep non trovato:'||aEs||'-'||aCdVoce);
  end;
 end;

 -- Estrae la descrizion del tipo_doc_amm
 function getDesTipoDocAmm(aCd_tipo_documento_amm VARCHAR2) return VARCHAR Is
 des_tipo tipo_documento_amm.ds_tipo_documento_amm%Type;
 begin
  begin
   select ds_tipo_documento_amm
   into des_tipo
   from tipo_documento_amm
   Where cd_tipo_documento_amm = aCd_tipo_documento_amm;
  return des_tipo;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Tipo Documento Amministrativo non trovato:'||aCd_tipo_documento_amm);
  end;
 end;

 -- Estrae la descrizion del tipo_doc_cont
 function getDesTipoDocCont(aCd_tipo_documento_cont VARCHAR2) return VARCHAR Is
 des_tipo tipo_documento_cont.ds_tipo_documento_cont%Type;
 begin
  begin
   select ds_tipo_documento_cont
   into des_tipo
   from tipo_documento_cont
   Where cd_tipo_documento_cont = aCd_tipo_documento_cont;
  return des_tipo;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Tipo Documento Contabile non trovato:'||aCd_tipo_documento_cont);
  end;
 end;

 function getVoceEpMinusval(aEs number, cat varchar2, voce varchar2) return voce_ep%rowtype is
  aVoceSA varchar2(45);
  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
  	aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','MINUSVALENZA');
  else
    begin
      if cat is null then
        IBMERR001.RAISE_ERR_GENERICO('Parametro necessario per la configurazione della minusvalenza mancante!');
     	end if;
	   	select CD_VOCE_EP_MINUS into aVoceSA from categoria_gruppo_voce_ep
	    where
	    esercizio = aEs and
	    CD_VOCE_EP_MINUS is not null and
	    cd_categoria_gruppo = cat and
	    ((voce is not null and
	     cd_elemento_voce = voce) or
	    (voce is null and fl_default = 'Y' )) and
	    sezione='A';
	  exception when no_data_found then
	      IBMERR001.RAISE_ERR_GENERICO('Parametro necessario per la configurazione della minusvalenza mancante, categoria :'||cat);
	  end;
  end if;
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpPlusval(aEs number, cat varchar2, voce varchar2) return voce_ep%rowtype is
  aVoceSA varchar2(45);
  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
  	aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','PLUSVALENZA');
  else
    begin
      if cat is null then
        IBMERR001.RAISE_ERR_GENERICO('Parametro necessario per la configurazione della plusvalenza mancante!');
     	end if;
	   	select CD_VOCE_EP_PLUS into aVoceSA from categoria_gruppo_voce_ep
	    where
	    esercizio = aEs and
	    CD_VOCE_EP_PLUS is not null and
	    cd_categoria_gruppo = cat and
	    ((voce is not null and
	     cd_elemento_voce = voce) or
	    (voce is null and fl_default = 'Y' )) and
	    sezione='A';
	  exception when no_data_found then
	      IBMERR001.RAISE_ERR_GENERICO('Parametro necessario per la configurazione della plusvalenza mancante, categoria :'||cat);
	  end;
  end if;
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpScontoAbbCosto(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
	  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','SCONTO_ABBUONO_COSTO');
    return getVoceEp(aEs, aVoceSA);
  else
    return null;
  end if;
 end;

 function getVoceEpScontoAbbRicavo(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
	  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','SCONTO_ABBUONO_RICAVO');
    return getVoceEp(aEs, aVoceSA);
 else
    return null;
  end if;
 end;

 function getVoceEpBanca(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','BANCA');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpBancaCds(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_tesoreria_unica='N' then
	 aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','BANCA_CDS');
   return getVoceEp(aEs, aVoceSA);
  else
     return getVoceEpBanca(aEs);
  end if;
 end;

 function getVoceEpCassa(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','CASSA');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpAnticipoMissione(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
	   aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','ANTICIPO_MISSIONE');
     return getVoceEp(aEs, aVoceSA);
  else
    return  null;
  end if;
 end;

 function getVoceEpInsussistenzeCredito(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','INSUSSISTENZE_CREDITO');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpPerditaSuCambio(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
		aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','PERDITA_SU_CAMBIO');
  	return getVoceEp(aEs, aVoceSA);
  else
    return null;
  end if;
 end;

 function getVoceEpUtileSuCambio(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
	 	aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','UTILE_SU_CAMBIO');
  	return getVoceEp(aEs, aVoceSA);
  else
  	return null;
  end if;
 end;

 function getVoceEpErarioCIva(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','ERARIO_C_IVA');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpIvaDebito(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','IVA_DEBITO');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpIvaCredito(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','IVA_CREDITO');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpCreditiIniziali(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
		aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','CREDITI_INIZIALI');
  	return getVoceEp(aEs, aVoceSA);
  else
   	return null;
  end if;
 end;

 function getVoceEpDebitiIniziali(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
 	 	aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','DEBITI_INIZIALI');
  	return getVoceEp(aEs, aVoceSA);
  else
  	return null;
  end if;
 end;

 function getVoceEpStatoPatrIniziale(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  if recParametriCNR.fl_nuovo_pdg='N' then
  	aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','STATO_PATRIMONIALE_INIZIALE');
  	return getVoceEp(aEs, aVoceSA);
  else
  	return null;
  end if;
 end;

 function getVoceEpFattureDaEmettere(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','FATTURE_DA_EMETTERE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpFattureDaRicevere(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','FATTURE_DA_RICEVERE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpCommissioniBanca(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','COMMISSIONI_BANCARIE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpCostoIvaNonDetraibile(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','COSTO_IVA_NON_DETRAIBILE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpRicIvaNonDetraibile(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','RICAVO_IVA_NON_DETRAIBILE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpRiscontiAttivi(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','RISCONTI_ATTIVI');
  return getVoceEp(aEs, aVoceSA);
 end;
 function getVoceEpRiscontiPassivi(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','RISCONTI_PASSIVI');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpRateiAttivi(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','RATEI_ATTIVI');
  return getVoceEp(aEs, aVoceSA);
 end;
 function getVoceEpRateiPassivi(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 Begin
  	aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','RATEI_PASSIVI');
  	return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpSopravvAttive(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','SOPRAVVENIENZE_ATTIVE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpSopravvPassive(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','SOPRAVVENIENZE_PASSIVE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpInsussAttive(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','INSUSSISTENZE_ATTIVE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpInsussPassive(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','INSUSSISTENZE_PASSIVE');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpCredDiversiPgiro(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  --  CONTO P71011  DA MATRICE MEF INCOERENTE VOLUTAMENTE
  aVoceSA:=CNRCTB015.getVal02PerChiave(aEs, 'VOCEEP_SPECIALE','DEBITI_CREDITI_DIVERSI_PGIRO');
  return getVoceEp(aEs, aVoceSA);
 end;

 function getVoceEpDebDiversiPgiro(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
 	--  CONTO 91011  DA MATRICE MEF INCOERENTE VOLUTAMENTE
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','DEBITI_CREDITI_DIVERSI_PGIRO');
  return getVoceEp(aEs, aVoceSA);
 end;

 -- Estrae il conto speciale chiusura conto economico
 function getVoceEpContoEconomico(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
begin
	  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','CONTO_ECONOMICO');
  	return getVoceEp(aEs, aVoceSA);
 end;

 -- Estrae il conto speciale chiusura stato patrimoniale
 function getVoceEpStatoPatrimoniale(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
begin
		aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','STATO_PATRIMONIALE');
  	return getVoceEp(aEs, aVoceSA);
 end;

 -- Estrae il conto speciale utile perdita d'esercizio
 function getVoceEpUtilePerditaEsercizio(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','UTILE_PERDITA_ESERCIZIO');
  return getVoceEp(aEs, aVoceSA);
 end;
 function getVoceEpRimbMutuoEsercizio(aEs number) return voce_ep%rowtype is
 aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','RIMBORSO_MUTUO');
  return getVoceEp(aEs, aVoceSA);
 end;

 -- Estrae il conto speciale di contropartita per coge migrazione beni
 function getVoceEpMigraBeni(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','CONTROPARTITA_MIGRAZIONE_BENI');
  return getVoceEp(aEs, aVoceSA);
 end;

 -- Estrae il conto speciale di patrimonio netto
 function getVoceEpPatrimNetto(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','PATRIMONIO_NETTO');
  return getVoceEp(aEs, aVoceSA);
 end;

 -- Estrae il conto speciale di CONTROPARTITA REVERSALI DI REGOLARIZZAZIONE SU PGIRO
 function getVoceCONTR_REV_REG_PGIRO(aEs number) return voce_ep%Rowtype Is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal01PerChiave(aEs, 'VOCEEP_SPECIALE','CONTR_REV_REGOLARIZZ_PGIRO');
  return getVoceEp(aEs, aVoceSA);
 end;
 function getVoceEpErarioCIvaGirocontoUO(aEs number) return voce_ep%rowtype is
  aVoceSA varchar2(45);
 begin
  aVoceSA:=CNRCTB015.getVal02PerChiave(aEs, 'VOCEEP_SPECIALE','ERARIO_C_IVA');
  dbms_output.put_line(aVoceSA);
  return getVoceEp(aEs, aVoceSA);
 end;

 procedure coie_VOCE_EP (aEsDest esercizio%rowtype) is
  begin
   insert into VOCE_EP (
     ESERCIZIO
    ,CD_VOCE_EP
    ,NATURA_VOCE
    ,TI_VOCE_EP
    ,DS_VOCE_EP
    ,LIVELLO
    ,CD_PROPRIO_VOCE_EP
    ,FL_MASTRINO
    ,TI_SEZIONE
    ,RIEPILOGA_A
    ,RIAPRE_A_CONTO_ECONOMICO
    ,FL_A_PAREGGIO
    ,CONTO_SPECIALE
    ,DUVA
    ,UTUV
    ,DACR
    ,UTCR
    ,PG_VER_REC
    ,CD_VOCE_EP_PADRE
   )
   select * from (
    select
     aEsDest.esercizio ESERCIZIO
    ,CD_VOCE_EP
    ,NATURA_VOCE
    ,TI_VOCE_EP
    ,DS_VOCE_EP
    ,LIVELLO
    ,CD_PROPRIO_VOCE_EP
    ,FL_MASTRINO
    ,TI_SEZIONE
    ,RIEPILOGA_A
    ,RIAPRE_A_CONTO_ECONOMICO
    ,FL_A_PAREGGIO
    ,CONTO_SPECIALE
    ,aEsDest.duva
    ,aEsDest.utuv
    ,aEsDest.dacr
    ,aEsDest.utcr
    ,PG_VER_REC
    ,CD_VOCE_EP_PADRE
   from VOCE_EP
    where esercizio = 0
   order by esercizio,cd_voce_ep
  );
 end;

end;
