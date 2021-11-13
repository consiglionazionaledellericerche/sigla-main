--------------------------------------------------------
--  DDL for Package Body CNRMIG100
--------------------------------------------------------

CREATE OR REPLACE PACKAGE BODY "CNRMIG100" is

lPgExec number;

function isRibaltamentoPDGPEffettuato (aEs number, aPgEsec number) return boolean is
--aPgEsecPrec integer;
conta_es      number;
conta_voce    number;
begin
	begin
	  select count(1)
	  into conta_es
	  from esercizio_base
		where esercizio = aEs;
  exception when NO_DATA_FOUND then
			 conta_es := 0;
	end;

	begin
  	select count(1)
	  into conta_voce
	  from elemento_voce
	  where esercizio = aEs;
  exception when NO_DATA_FOUND then
			 conta_voce := 0;
	end;

	if conta_es <> 0 or conta_voce <> 0 then
	   return true;
	else
		return false;
	end if;

  /* ribaltamento_log viene cancellata quindi non è attendibile fare questo controllo
	begin
		select pg_esecuzione into aPgEsecPrec
		from ribaltamento_log
		where esercizio   = aEs
		  and pg_esecuzione < aPgEsec
		  and stato 	  = 'P';
	exception when NO_DATA_FOUND then
		aPgEsecPrec := 0;
	end;

	if aPgEsecPrec <> 0 then
	   return true;
	else
		return false;
	end if;
  */
end;
----------------------------------------------------------------------------
function isRibaltamentoAltroEffettuato (aEs number, aPgEsec number) return boolean is
--aPgEsecPrec integer;
conta_gruppi   number;
begin
	begin
	  select count(1)
	  into conta_gruppi
	  from GRUPPO_CR
		where esercizio = aEs;
	exception when NO_DATA_FOUND then
			 conta_gruppi := 0;
	end;

  if conta_gruppi <> 0 then
	   return true;
	else
		return false;
	end if;

	/*
	begin
		select pg_esecuzione into aPgEsecPrec
		from ribaltamento_log
		where esercizio   = aEs
		  and pg_esecuzione < aPgEsec
		  and stato 	  = 'S';
	exception when NO_DATA_FOUND then
		aPgEsecPrec := 0;
	end;

	if aPgEsecPrec <> 0 then
	   return true;
	else
		return false;
	end if;
  */
end;
----------------------------------------------------------------------------
procedure startLogRibaltamento(aEs number, aPgEsec number, aDsProcesso varchar2, aUtcr varchar2) is
aRibLogDest ribaltamento_log%rowtype;
begin
	aRibLogDest.ESERCIZIO	  := aEs;
	aRibLogDest.PG_ESECUZIONE := aPgEsec;
	aRibLogDest.DS_PROCESSO   := aDsProcesso;
	aRibLogDest.START_EXEC	  := sysdate;
	aRibLogDest.STATO		  := 'I';
	aRibLogDest.UTCR		  := aUtcr;
	aRibLogDest.DACR		  := sysdate;
	aRibLogDest.UTUV		  := aUtcr;
	aRibLogDest.DUVA		  := sysdate;
	aRibLogDest.PG_VER_REC	  := 1;
	ins_ribaltamento_log(aRibLogDest);
end;
----------------------------------------------------------------------------
procedure endLogRibaltamentoPerPDGP(aEs number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
begin

	if aStato = 'I' then
	   aStato := 'P';
	end if;

    update RIBALTAMENTO_LOG
	set END_EXEC   = sysdate,
		STATO 	   = aStato,
		UTUV	   = cgUtente,
		DUVA	   = sysdate,
		PG_VER_REC = pg_ver_rec + 1
	where esercizio     = aEs
	  and pg_esecuzione = aPgEsec;

	if aStato = 'P' then
		aMessage := 'Ribaltamento per PDGP completato con successo, verificare e eseguire commit';
		ibmutl200.loginf(aPgEsec,aMessage,'','');
	elsif aStato = 'W' then
		rollback;
		aMessage := 'Ribaltamento per PDGP completato, ma con problemi - vedi BATCH_LOG_RIGA con pg_esecuzione '||aPgEsec||'. Eseguito rollback.';
		ibmutl200.loginf(aPgEsec,aMessage,'','');
	else -- aStato = 'E'
		ibmutl200.LOGERR(aPgEsec,aMessage,'','');
		ibmerr001.RAISE_ERR_GENERICO(aMessage);
	end if;

end;
----------------------------------------------------------------------------
procedure endLogRibaltamentoAltro(aEs number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
begin

	if aStato = 'I' then
	   aStato := 'S';
	end if;

    update RIBALTAMENTO_LOG
	set END_EXEC   = sysdate,
		STATO 	   = aStato,
		UTUV	   = cgUtente,
		DUVA	   = sysdate,
		PG_VER_REC = pg_ver_rec + 1
	where esercizio     = aEs
	  and pg_esecuzione = aPgEsec;

	if aStato = 'S' then
		aMessage := 'Ribaltamento completato con successo, verificare e eseguire commit';
		ibmutl200.loginf(aPgEsec,aMessage,'','');
	elsif aStato = 'W' then
		rollback;
		aMessage := 'Ribaltamento completato, ma con problemi - vedi BATCH_LOG_RIGA con pg_esecuzione '||aPgEsec||'. Eseguito rollback.';
		ibmutl200.loginf(aPgEsec,aMessage,'','');
	else -- aStato = 'E'
		ibmutl200.LOGERR(aPgEsec,aMessage,'','');
		ibmerr001.RAISE_ERR_GENERICO(aMessage);
	end if;

end;
----------------------------------------------------------------------------
procedure ins_RIBALTAMENTO_LOG (aDest RIBALTAMENTO_LOG%rowtype) is
  begin
   insert into RIBALTAMENTO_LOG (
     ESERCIZIO
    ,PG_ESECUZIONE
	,DS_PROCESSO
    ,START_EXEC
    ,END_EXEC
    ,STATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.PG_ESECUZIONE
	,aDest.DS_PROCESSO
    ,aDest.START_EXEC
    ,aDest.END_EXEC
    ,aDest.STATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
end;
----------------------------------------------------------------------------
procedure init_ribaltamento_altro(aEs number, aMessage in out varchar2) is
aEsPrec number;
aPgEsec number;
stato_fine char(1) := 'I';
aNum number;
begin

	begin
		aPgEsec := IBMUTL200.LOGSTART(TI_LOG_RIBALTAMENTO_ALTRO,dsProcesso_altro,null,cgUtente,null,null);

		startLogRibaltamento(aEs, aPgEsec, dsProcesso_altro , cgUtente);

		if isRibaltamentoAltroEffettuato(aEs, aPgEsec) then
		   stato_fine := 'E';
		   aMessage := 'Lo script di ribaltamento è già stato eseguito con successo per l''esercizio '|| aEs ||': non è possibile eseguirlo nuovamente.';
		   endLogRibaltamentoAltro(aEs, aPgEsec, stato_fine, aMessage);
		end if;
		if Not isRibaltamentoPDGPEffettuato(aEs, aPgEsec) then
		   stato_fine := 'E';
		   aMessage := 'Non è stato ancora eseguito il ribaltamento per il PDGP per l''esercizio '|| aEs ||': non è possibile proseguire.';
		   endLogRibaltamentoAltro(aEs, aPgEsec, stato_fine, aMessage);
		end if;

		aEsPrec := aEs - 1;

  -- inizio spostamento in ribaltamento_altro rospuc
	-- Ribaltamento elemento voce
	aMessage := 'Ribaltamento dell''anagrafica dei capitoli sull''esercizio '||aEs||'. Lock tabelle ELEMENTO_VOCE, VOCE_F, VOCE_F_SALDI_CMP';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aElVoce in (select * from ELEMENTO_VOCE
				    where esercizio = aEsPrec
				    and not exists(select 1 from elemento_voce ev
				    where ev.esercizio				= aEs and
				    			ev.ti_gestione 			= elemento_voce.ti_gestione   and
				    			ev.ti_appartenenza  = elemento_voce.ti_appartenenza and
				    			ev.cd_elemento_voce = elemento_voce.cd_elemento_voce)
					order by ti_appartenenza, ti_gestione, cd_elemento_voce) loop -- ordine per RX_ELEMENTO_VOCE00
		begin

			insert into ELEMENTO_VOCE  (ESERCIZIO,
						    TI_APPARTENENZA,
						    TI_GESTIONE,
						    CD_ELEMENTO_VOCE,
						    CD_PROPRIO_ELEMENTO,
						    TI_ELEMENTO_VOCE,
						    FL_LIMITE_ASS_OBBLIG,
						    FL_VOCE_PERSONALE,
						    CD_PARTE,
						    DS_ELEMENTO_VOCE,
						    DACR,
						    UTUV,
						    UTCR,
						    DUVA,
						    PG_VER_REC,
						    CD_ELEMENTO_PADRE,
						    FL_PARTITA_GIRO,
						    CD_CAPOCONTO_FIN,
						    FL_VOCE_SAC,
						    FL_VOCE_NON_SOGG_IMP_AUT,
						    ESERCIZIO_CLA_E,
  						    COD_CLA_E,
  						    ESERCIZIO_CLA_S,
  						    COD_CLA_S,
  						    FL_RECON,
  						    FL_INV_BENI_PATR,
  						    ID_CLASSIFICAZIONE,
  						    CD_TIPO_SPESA_SAC,
  						    CD_TIPO_SPESA_IST,
  						    FL_VOCE_FONDO,
  						    FL_CHECK_TERZO_SIOPE,
  						    FL_INV_BENI_COMP,
  						    FL_LIMITE_SPESA,
  						    FL_PRELIEVO,
  						    FL_SOGGETTO_PRELIEVO,
  						    PERC_PRELIEVO_PDGP_ENTRATE,
  						    FL_SOLO_RESIDUO,
  						    FL_SOLO_COMPETENZA,
  						    FL_TROVATO,
  						    FL_AZZERA_RESIDUI,
  						    ESERCIZIO_ELEMENTO_PADRE,
  						    TI_APPARTENENZA_ELEMENTO_PADRE,
  						    TI_GESTIONE_ELEMENTO_PADRE,
  						    FL_MISSIONI,
  						    CD_UNITA_PIANO,
  						    CD_VOCE_PIANO,
  						    GG_DEROGA_OBBL_COMP_PRG_SCAD,
  						    GG_DEROGA_OBBL_RES_PRG_SCAD,
  						    FL_COMUNICA_PAGAMENTI,
  						    FL_LIMITE_COMPETENZA,
  						    BLOCCO_IMPEGNI_NATFIN)
			values (aEs,
				aElVoce.TI_APPARTENENZA,
				aElVoce.TI_GESTIONE,
				aElVoce.CD_ELEMENTO_VOCE,
				aElVoce.CD_PROPRIO_ELEMENTO,
				aElVoce.TI_ELEMENTO_VOCE,
				aElVoce.FL_LIMITE_ASS_OBBLIG,
				aElVoce.FL_VOCE_PERSONALE,
				aElVoce.CD_PARTE,
				aElVoce.DS_ELEMENTO_VOCE,
				sysdate,
				cgUtente,
				cgUtente,
				sysdate,
				1,
				aElVoce.CD_ELEMENTO_PADRE,
				aElVoce.FL_PARTITA_GIRO,
				aElVoce.CD_CAPOCONTO_FIN,
				aElVoce.FL_VOCE_SAC,
				aElVoce.FL_VOCE_NON_SOGG_IMP_AUT,
				Decode(aElVoce.COD_CLA_E,Null,Null,aEs),
  				aElVoce.COD_CLA_E,
  				Decode(aElVoce.COD_CLA_S,Null,Null,aEs),
  				aElVoce.COD_CLA_S,
  				aElVoce.FL_RECON,
  				aElVoce.FL_INV_BENI_PATR,
  				null,
  				aElVoce.CD_TIPO_SPESA_SAC,
  				aElVoce.CD_TIPO_SPESA_IST,
  				aElVoce.FL_VOCE_FONDO,
  				aElVoce.FL_CHECK_TERZO_SIOPE,
  				aElVoce.FL_INV_BENI_COMP,
  				aElVoce.FL_LIMITE_SPESA,
  				aElVoce.FL_PRELIEVO,
  				aElVoce.FL_SOGGETTO_PRELIEVO,
  				aElVoce.PERC_PRELIEVO_PDGP_ENTRATE,
  				aElVoce.FL_SOLO_RESIDUO,
  				aElVoce.FL_SOLO_COMPETENZA,
  				aElVoce.FL_TROVATO,
  				aElVoce.FL_AZZERA_RESIDUI,
  				aElVoce.ESERCIZIO_ELEMENTO_PADRE,
  				aElVoce.TI_APPARTENENZA_ELEMENTO_PADRE,
  				aElVoce.TI_GESTIONE_ELEMENTO_PADRE,
  				aElVoce.FL_MISSIONI,
  				aElVoce.CD_UNITA_PIANO,
  				aElVoce.CD_VOCE_PIANO,
  				aElVoce.GG_DEROGA_OBBL_COMP_PRG_SCAD,
  				aElVoce.GG_DEROGA_OBBL_RES_PRG_SCAD,
  				aElVoce.FL_COMUNICA_PAGAMENTI,
  				aElVoce.FL_LIMITE_COMPETENZA,
  				aElVoce.BLOCCO_IMPEGNI_NATFIN);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ELEMENTO_VOCE con PK('||aEs||', '
					||aElVoce.TI_APPARTENENZA||', '
					||aElVoce.TI_GESTIONE||', '
					||aElVoce.CD_ELEMENTO_VOCE||') già esistente','','');
			stato_fine := 'W';
		end;
	end loop;
	-- all'inserimento di record in ELEMENTO_VOCE viene attivato un trigger
	-- che esplode l'anagrafica dei capitoli sul piano dei conti finanziario
	-- in VOCE_F

	-- Ribaltamento delle associazioni fra elementi voce
	aMessage := 'Ribaltamento delle associazioni fra elementi voce sull''esercizio '||aEs||'. Lock tabella ASS_EV_EV';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aAssEVEV in (select * from ASS_EV_EV
			 	 where esercizio = aEsPrec
			 	 and
			 	 not exists(select 1 from ass_ev_ev assNew where
					assNew.ESERCIZIO=aEs				and
					assNew.TI_APPARTENENZA=ASS_EV_EV.TI_APPARTENENZA							and
					assNew.TI_GESTIONE	=ASS_EV_EV.TI_GESTIONE										and
					assNew.CD_ELEMENTO_VOCE	=ASS_EV_EV.CD_ELEMENTO_VOCE						and
					assNew.CD_ELEMENTO_VOCE_COLL=ASS_EV_EV.CD_ELEMENTO_VOCE_COLL	and
					assNew.TI_APPARTENENZA_COLL =ASS_EV_EV.TI_APPARTENENZA_COLL		and
					assNew.TI_GESTIONE_COLL	=ASS_EV_EV.TI_GESTIONE_COLL		and
					assNew.CD_CDS	=ASS_EV_EV.CD_CDS							and
					assNew.CD_NATURA	= ASS_EV_EV.CD_NATURA			)) loop
		begin
			insert into ASS_EV_EV  (ESERCIZIO,
						TI_APPARTENENZA,
						TI_GESTIONE,
						CD_ELEMENTO_VOCE,
						CD_ELEMENTO_VOCE_COLL,
						TI_APPARTENENZA_COLL,
						TI_GESTIONE_COLL,
						CD_CDS,
						CD_NATURA,
						TI_ELEMENTO_VOCE,
						TI_ELEMENTO_VOCE_COLL,
						UTUV,
						DACR,
						UTCR,
						DUVA,
						PG_VER_REC)
			values (aEs,
				aAssEVEV.TI_APPARTENENZA,
				aAssEVEV.TI_GESTIONE,
				aAssEVEV.CD_ELEMENTO_VOCE,
				aAssEVEV.CD_ELEMENTO_VOCE_COLL,
				aAssEVEV.TI_APPARTENENZA_COLL,
				aAssEVEV.TI_GESTIONE_COLL,
				aAssEVEV.CD_CDS,
				aAssEVEV.CD_NATURA,
				aAssEVEV.TI_ELEMENTO_VOCE,
				aAssEVEV.TI_ELEMENTO_VOCE_COLL,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_EV_EV con PK('||aEs||', '
					||aAssEVEV.TI_APPARTENENZA||', '
					||aAssEVEV.TI_GESTIONE||', '
					||aAssEVEV.CD_ELEMENTO_VOCE||', '
					||aAssEVEV.CD_ELEMENTO_VOCE_COLL||', '
					||aAssEVEV.TI_APPARTENENZA_COLL||', '
					||aAssEVEV.TI_GESTIONE_COLL||', '
					||aAssEVEV.CD_CDS||', '
					||aAssEVEV.CD_NATURA||') già esistente','','');
			stato_fine := 'W';
		end;
	end loop;

	-- Ribaltamento associazioni tra categorie e voci del piano dei conti
	aMessage := 'Ribaltamento associazioni tra categorie e voci del piano dei conti sull''esercizio '||aEs||'. Lock tabella CATEGORIA_GRUPPO_VOCE';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aCatGV in (select * from CATEGORIA_GRUPPO_VOCE
			   	   where esercizio = aEsPrec) loop
		begin
			insert into CATEGORIA_GRUPPO_VOCE  (CD_CATEGORIA_GRUPPO,
							    ESERCIZIO,
							    TI_APPARTENENZA,
							    TI_GESTIONE,
							    DACR,
							    UTCR,
							    DUVA,
							    UTUV,
							    PG_VER_REC,
							    CD_ELEMENTO_VOCE)
			values (aCatGV.CD_CATEGORIA_GRUPPO,
				aEs,
				aCatGV.TI_APPARTENENZA,
				aCatGV.TI_GESTIONE,
				sysdate,
				cgUtente,
				sysdate,
				cgutente,
				1,
				aCatGV.CD_ELEMENTO_VOCE);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CATEGORIA_GRUPPO_VOCE con PK ('||aCatGV.CD_CATEGORIA_GRUPPO||', '
					 ||aEs||', '
					 ||aCatGV.TI_APPARTENENZA||', '
					 ||aCatGV.TI_GESTIONE||', '
					 ||aCatGV.CD_ELEMENTO_VOCE||') già esistente','','');
			stato_fine := 'W';
		end;
	end loop;
-- Ribaltamento classificazione voci
	declare
	   new_id NUMBER;
	   new_id_padre NUMBER:=Null;
	begin
	lock table classificazione_voci in exclusive mode nowait;
	aMessage := 'Ribaltamento Classificazione Voci in aggiunta sull''esercizio '||aEs||'. Lock tabella CLASSIFICAZIONE_VOCI';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');

	Select Max(id_classificazione)
	Into new_id
	From CLASSIFICAZIONE_VOCI;

	  for aClassV in (select * from CLASSIFICAZIONE_VOCI
			where esercizio = aEsPrec and not exists(select 1 from classificazione_voci class_new where
			class_new.esercizio=aEs and
			class_new.ti_gestione = classificazione_voci.ti_gestione  and
			class_new.cd_livello1 = classificazione_voci.cd_livello1  and
			nvl(class_new.cd_livello2,' ') = nvl(classificazione_voci.cd_livello2,' ')  and
      nvl(class_new.cd_livello3,' ') = nvl(classificazione_voci.cd_livello3,' ')  and
      nvl(class_new.cd_livello4,' ') = nvl(classificazione_voci.cd_livello4,' ')  and
      nvl(class_new.cd_livello5,' ') = nvl(classificazione_voci.cd_livello5,' ')  and
      nvl(class_new.cd_livello6,' ') = nvl(classificazione_voci.cd_livello6,' ')  and
      nvl(class_new.cd_livello7,' ') = nvl(classificazione_voci.cd_livello7,' ') )
			order by esercizio, ti_gestione, cd_livello1 Desc, cd_livello2 Desc ,cd_livello3 Desc ,
			cd_livello4 Desc, cd_livello5 Desc, cd_livello6 Desc, cd_livello7 Desc)
	  loop
		begin
			new_id := new_id + 1;
			insert into CLASSIFICAZIONE_VOCI (ID_CLASSIFICAZIONE,
							  ESERCIZIO,
							  TI_GESTIONE,
							  DS_CLASSIFICAZIONE,
							  CD_LIVELLO1,
							  CD_LIVELLO2,
							  CD_LIVELLO3,
							  CD_LIVELLO4,
							  CD_LIVELLO5,
							  CD_LIVELLO6,
							  CD_LIVELLO7,
							  ID_CLASS_PADRE,
							  FL_MASTRINO,
							  DUVA,
							  UTUV,
							  DACR,
							  UTCR,
							  PG_VER_REC,
							  FL_CLASS_SAC,
							  FL_SOLO_GESTIONE,
							  FL_ACCENTRATO,
							  FL_DECENTRATO,
							  CDR_ACCENTRATORE,
							  FL_PIANO_RIPARTO,
						  	  FL_ESTERNA_DA_QUADRARE_SAC,
						  	  FL_VISTO_DIP_VARIAZIONI,
						  	  TI_CLASSIFICAZIONE,
						  	  FL_PREV_OBB_ANNO_SUC,
						  	  IM_LIMITE_ASSESTATO)
			values (new_id,
				aEs,
				aClassV.TI_GESTIONE,
				aClassV.DS_CLASSIFICAZIONE,
				aClassV.CD_LIVELLO1,
				aClassV.CD_LIVELLO2,
				aClassV.CD_LIVELLO3,
				aClassV.CD_LIVELLO4,
				aClassV.CD_LIVELLO5,
				aClassV.CD_LIVELLO6,
				aClassV.CD_LIVELLO7,
				aClassV.ID_CLASS_PADRE,
				aClassV.FL_MASTRINO,
				sysdate,
				cgUtente,
				sysdate,
				cgutente,
				1,
				aClassV.FL_CLASS_SAC,
				aClassV.FL_SOLO_GESTIONE,
				aClassV.FL_ACCENTRATO,
				aClassV.FL_DECENTRATO,
				aClassV.CDR_ACCENTRATORE,
				aClassV.FL_PIANO_RIPARTO,
				aClassV.FL_ESTERNA_DA_QUADRARE_SAC,
				aClassV.FL_VISTO_DIP_VARIAZIONI,
				aclassV.TI_CLASSIFICAZIONE,
				aclassV.FL_PREV_OBB_ANNO_SUC,
				aclassV.IM_LIMITE_ASSESTATO);

			--Aggiorno l'elemento voce con la nuova classificazione creata
			update elemento_voce v
			set v.id_classificazione =  new_id
			where v.esercizio = aEs
			and (v.ti_appartenenza, v.ti_gestione, v.cd_elemento_voce) In
			     (Select a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce
			     From elemento_voce a
			     Where a.esercizio = aEsPrec
			       And a.id_classificazione = aClassV.id_classificazione);
		exception
		   when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CLASSIFICAZIONE_VOCI con PK ('||new_id||') già esistente','','');
			stato_fine := 'W';
		   when OTHERS then
			ibmutl200.LOGWAR(aPgEsec,'SK_CLASSIFICAZIONE_VOCI violata per la classificazione ('||new_id||')','','');
			stato_fine := 'W';
		end;
	  end loop;
	end;

	-- Aggiornamento id_class_padre nel nuovo anno
	Declare
    	    rec_class 	CLASSIFICAZIONE_VOCI%Rowtype;
    	    new_padre   NUMBER;
	Begin
	   for aClassPadre in (select * from CLASSIFICAZIONE_VOCI
		    where esercizio = aEsPrec
		    And id_class_padre Is Not Null and exists(select 1 from classificazione_voci new_class
		    where
		    new_class.esercizio = aEs and
		    CLASSIFICAZIONE_VOCI.id_class_padre=new_class.id_class_padre)) loop
  		select *
		into rec_class
		From CLASSIFICAZIONE_VOCI
		where esercizio = aEsPrec
		and ti_gestione = aClassPadre.ti_gestione
		and id_classificazione = aClassPadre.id_class_padre;

		select id_classificazione
		into new_padre
		from CLASSIFICAZIONE_VOCI
		where esercizio = aEs
		and ti_gestione = rec_class.ti_gestione
		and cd_livello1 = rec_class.cd_livello1
		and ((rec_class.cd_livello2 Is Not Null And cd_livello2 = rec_class.cd_livello2)
	      	      Or
	      	     (rec_class.cd_livello2 Is Null And cd_livello2 Is Null))
		and ((rec_class.cd_livello3 Is Not Null And cd_livello3 = rec_class.cd_livello3)
	              Or
	             (rec_class.cd_livello3 Is Null And cd_livello3 Is Null))
		and ((rec_class.cd_livello4 Is Not Null And cd_livello4 = rec_class.cd_livello4)
	              Or
	             (rec_class.cd_livello4 Is Null And cd_livello4 Is Null))
		and ((rec_class.cd_livello5 Is Not Null And cd_livello5 = rec_class.cd_livello5)
	              Or
	             (rec_class.cd_livello5 Is Null And cd_livello5 Is Null))
		and ((rec_class.cd_livello6 Is Not Null And cd_livello6 = rec_class.cd_livello6)
	              Or
	             (rec_class.cd_livello6 Is Null And cd_livello6 Is Null))
		and ((rec_class.cd_livello7 Is Not Null And cd_livello7 = rec_class.cd_livello7)
	              Or
	             (rec_class.cd_livello7 Is Null And cd_livello7 Is Null));

		update CLASSIFICAZIONE_VOCI
		set id_class_padre = new_padre
		where esercizio = aEs
		and ti_gestione = aClassPadre.ti_gestione
		and cd_livello1 = aClassPadre.cd_livello1
		and ((aClassPadre.cd_livello2 Is Not Null And cd_livello2 = aClassPadre.cd_livello2)
	             Or
	             (aClassPadre.cd_livello2 Is Null And cd_livello2 Is Null))
		and ((aClassPadre.cd_livello3 Is Not Null And cd_livello3 = aClassPadre.cd_livello3)
	             Or
	             (aClassPadre.cd_livello3 Is Null And cd_livello3 Is Null))
		and ((aClassPadre.cd_livello4 Is Not Null And cd_livello4 = aClassPadre.cd_livello4)
	             Or
	             (aClassPadre.cd_livello4 Is Null And cd_livello4 Is Null))
		and ((aClassPadre.cd_livello5 Is Not Null And cd_livello5 = aClassPadre.cd_livello5)
	             Or
	             (aClassPadre.cd_livello5 Is Null And cd_livello5 Is Null))
		and ((aClassPadre.cd_livello6 Is Not Null And cd_livello6 = aClassPadre.cd_livello6)
	             Or
	             (aClassPadre.cd_livello6 Is Null And cd_livello6 Is Null))
		and ((aClassPadre.cd_livello7 Is Not Null And cd_livello7 = aClassPadre.cd_livello7)
	             Or
	             (aClassPadre.cd_livello7 Is Null And cd_livello7 Is Null));
   	End Loop;
	End;

	-- Ribaltamento per la Gestione SIOPE

	-- Ribaltamento dei codici siope
	aMessage := 'Ribaltamento Codici SIOPE sull''esercizio '||aEs||'. Lock tabella CODICI_SIOPE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	For aCodSiope in (select * from CODICI_SIOPE
			  where esercizio = aEsPrec)
	Loop
		Begin
			insert into CODICI_SIOPE  (ESERCIZIO,
  						   TI_GESTIONE,
  						   CD_SIOPE,
  						   DESCRIZIONE,
  						   UTCR,
  						   DACR,
  						   UTUV,
  						   DUVA,
  						   PG_VER_REC)
			values (aEs,
  				aCodSiope.TI_GESTIONE,
  				aCodSiope.CD_SIOPE,
  				aCodSiope.DESCRIZIONE,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		Exception when dup_val_on_index then
			ibmutl200.logwar(aPgEsec,'Codice SIOPE con PK('||aEs||', '
							     	       ||aCodSiope.TI_GESTIONE||', '
								       ||aCodSiope.CD_SIOPE||') già inserito','','');
			stato_fine := 'W';
		End;
	End Loop;

	-- Ribaltamento delle associazioni fra tipologie istat e codici siope
	aMessage := 'Ribaltamento Associazione Tipologia ISTAT - Codici SIOPE sull''esercizio '||aEs||'. Lock tabella ASS_TIPOLOGIA_ISTAT_SIOPE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	For aAssIstatSiope in (select * from ASS_TIPOLOGIA_ISTAT_SIOPE
			       where esercizio_siope = aEsPrec)
	Loop
		Begin
			insert into ASS_TIPOLOGIA_ISTAT_SIOPE  (PG_TIPOLOGIA,
								ESERCIZIO_SIOPE,
  						   		TI_GESTIONE_SIOPE,
  						   		CD_SIOPE,
  						   		UTCR,
  						   		DACR,
  						   		UTUV,
  						   		DUVA,
  						   		PG_VER_REC)
			values (aAssIstatSiope.PG_TIPOLOGIA,
				aEs,
  				aAssIstatSiope.TI_GESTIONE_SIOPE,
  				aAssIstatSiope.CD_SIOPE,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		Exception when dup_val_on_index then
			ibmutl200.logwar(aPgEsec,'Associazione Tipologia ISTAT - Codici SIOPE con PK('||aAssIstatSiope.PG_TIPOLOGIA||', '
							     	       				      ||aEs||', '
							     	                                      ||aAssIstatSiope.TI_GESTIONE_SIOPE||', '
								                                      ||aAssIstatSiope.CD_SIOPE||') già inserita','','');
			stato_fine := 'W';
		End;
	End Loop;

	-- Ribaltamento delle associazioni fra elementi voce e codici siope
	aMessage := 'Ribaltamento delle associazioni fra elementi voce e codici siope sull''esercizio '||aEs||'. Lock tabella ASS_EV_SIOPE';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	For aAssEVSiope in (select * from ASS_EV_SIOPE
			    where esercizio = aEsPrec) loop
		Begin
			insert into ASS_EV_SIOPE  (ESERCIZIO,
						   TI_APPARTENENZA,
						   TI_GESTIONE,
						   CD_ELEMENTO_VOCE,
						   ESERCIZIO_SIOPE,
						   TI_GESTIONE_SIOPE,
						   CD_SIOPE,
						   UTCR,
						   DACR,
						   UTUV,
						   DUVA,
						   PG_VER_REC)
			values (aEs,
				aAssEVSiope.TI_APPARTENENZA,
				aAssEVSiope.TI_GESTIONE,
				aAssEVSiope.CD_ELEMENTO_VOCE,
				aEs,
				aAssEVSiope.TI_GESTIONE_SIOPE,
				aAssEVSiope.CD_SIOPE,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_EV_SIOPE con PK('||aEs||', '
					||aAssEVSiope.TI_APPARTENENZA||', '
					||aAssEVSiope.TI_GESTIONE||', '
					||aAssEVSiope.CD_ELEMENTO_VOCE||', '
					||aEs||', '
					||aAssEVSiope.TI_GESTIONE_SIOPE||', '
					||aAssEVSiope.CD_SIOPE||') già esistente','','');
			stato_fine := 'W';
		end;
	end loop;

	-- Ribaltamento delle associazioni fra contributi/ritenute e codici siope
	aMessage := 'Ribaltamento Associazione Contributi/ritenute - Codici SIOPE sull''esercizio '||aEs||'. Lock tabella ASS_TIPO_CONTR_RITENUTA_SIOPE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	For aAssContrRitSiope in (Select * From ASS_TIPO_CONTR_RITENUTA_SIOPE
			          Where esercizio = aEsPrec)
	Loop
		Begin
			insert into ASS_TIPO_CONTR_RITENUTA_SIOPE  (ESERCIZIO,
								    CD_CONTRIBUTO_RITENUTA,
								    DT_INI_VALIDITA,
								    ESERCIZIO_SIOPE_S,
  						   		    TI_GESTIONE_SIOPE_S,
  						   		    CD_SIOPE_S,
  						   		    ESERCIZIO_SIOPE_E,
  						   		    TI_GESTIONE_SIOPE_E,
  						   		    CD_SIOPE_E,
  						   		    DACR,
  						   		    UTCR,
  						   		    DUVA,
  						   		    UTUV,
  						   		    PG_VER_REC)
			values (aEs,
				aAssContrRitSiope.CD_CONTRIBUTO_RITENUTA,
				aAssContrRitSiope.DT_INI_VALIDITA,
				aEs,
  				aAssContrRitSiope.TI_GESTIONE_SIOPE_S,
  				aAssContrRitSiope.CD_SIOPE_S,
  				aEs,
  				aAssContrRitSiope.TI_GESTIONE_SIOPE_E,
  				aAssContrRitSiope.CD_SIOPE_E,
				sysdate,
				cgUtente,
				sysdate,
				cgUtente,
				1);
		Exception when dup_val_on_index then
			ibmutl200.logwar(aPgEsec,'Associazione Contributi/ritenute - Codici SIOPE con PK('||aEs||', '
							     	       				      ||aAssContrRitSiope.CD_CONTRIBUTO_RITENUTA||', '
							     	                                      ||aAssContrRitSiope.DT_INI_VALIDITA||', '
							     	                                      ||aAssContrRitSiope.TI_GESTIONE_SIOPE_S||', '
							     	                                      ||aAssContrRitSiope.CD_SIOPE_S||', '
							     	                                      ||aAssContrRitSiope.TI_GESTIONE_SIOPE_E||', '
								                                      ||aAssContrRitSiope.CD_SIOPE_E||') già inserita','','');
			stato_fine := 'W';
		End;
	End Loop;

		-- Ribaltamento del piano dei conti E/P
		ribaltaEP(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);

		-- Ribaltamento configurazione CORI per PDGP
		ribaltaCORI_pdgp(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);
-- fine spostamento in ribaltamento  altro rospuc

		-- Ribaltamento FIRME sull'esercizio contabile destinazione
		-- a partire dall'esercizio precedente
		begin
			aMessage := 'Inserimento delle FIRME per l''esercizio base '||aEs||'. Lock tabella FIRME';
			ibmutl200.LOGINF(aPgEsec,aMessage,'','');
			insert into FIRME (ESERCIZIO,TIPO, FIRMA1, FIRMA2, FIRMA3, FIRMA4,
			                   DACR, UTCR, DUVA, UTUV, PG_VER_REC, DT_STAMPA)
			select aEs, TIPO, FIRMA1, FIRMA2, FIRMA3, FIRMA4,
			       sysdate,cgUtente,sysdate,cgUtente,1,NULL
			from FIRME
			where ESERCIZIO = aEsPrec;
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'FIRME per l''esercizio base '||aEs||' già esistenti','','');
			stato_fine := 'W';
		end;

		-- Ribaltamento ASS_INCARICO_ATTIVITA sull'esercizio contabile destinazione
		-- a partire dall'esercizio precedente
		-- archivio già caricato anche negli anni successivi
		begin
			aMessage := 'Inserimento di ASS_INCARICO_ATTIVITA per l''esercizio base '||aEs||'. Lock tabella ASS_INCARICO_ATTIVITA';
			ibmutl200.LOGINF(aPgEsec,aMessage,'','');
			insert into ASS_INCARICO_ATTIVITA (ESERCIZIO,CD_TIPO_INCARICO,CD_TIPO_ATTIVITA,TIPO_NATURA,CD_TIPO_LIMITE,
							   STATO,UTCR,DACR,UTUV,DUVA,PG_VER_REC)
			select  aEs,CD_TIPO_INCARICO,CD_TIPO_ATTIVITA,TIPO_NATURA,CD_TIPO_LIMITE,
			 	STATO,cgUtente,Sysdate,cgUtente,Sysdate,1
			from ASS_INCARICO_ATTIVITA
			where ESERCIZIO = aEsPrec;
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_INCARICO_ATTIVITA per l''esercizio base '||aEs||' già esistente','','');
			stato_fine := 'W';
		end;

		-- Ribaltamento configurazione CORI
		ribaltaCORI_altro(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);

		-- Ribaltamento associazione tra UO e tipologie sezionali
		aMessage := 'Ribaltamento associazione tra UO e tipologie sezionali sull''esercizio '||aEs||'. Lock tabella SEZIONALE';
		ibmutl200.loginf(aPgEsec,aMessage,'','');
		for	aSez in (select * from SEZIONALE
				 	 where esercizio = aEsPrec
					   and cd_cds in (select cd_unita_organizzativa
					   	   		  	  from v_unita_organizzativa_valida
			 	 					  where fl_cds    = 'Y'
				   					    and esercizio = aEs)) loop
			begin
				 insert into SEZIONALE (CD_CDS,
							CD_UNITA_ORGANIZZATIVA,
							ESERCIZIO,
							CD_TIPO_SEZIONALE,
							TI_FATTURA,
							PRIMO,
							CORRENTE,
							ULTIMO,
							DACR,
							UTCR,
							DUVA,
							UTUV,
							PG_VER_REC)
				 		values (aSez.CD_CDS,
							aSez.CD_UNITA_ORGANIZZATIVA,
							aEs,
							aSez.CD_TIPO_SEZIONALE,
							aSez.TI_FATTURA,
							aSez.PRIMO,
							0,
							aSez.ULTIMO,
							sysdate,
							cgUtente,
							sysdate,
							cgUtente,
							1);
			exception when DUP_VAL_ON_INDEX then
				ibmutl200.LOGWAR(aPgEsec,'SEZIONALE con PK ('||aSez.CD_CDS||', '
						 ||aSez.CD_UNITA_ORGANIZZATIVA||', '
						 ||aEs||', '
						 ||aSez.CD_TIPO_SEZIONALE||', '
						 ||aSez.TI_FATTURA||') già inserito','','');
				stato_fine := 'W';
			end;
		end loop;

		-- Ribaltamento tabella causali di registrazione COGE
		aMessage := 'Ribaltamento tabella causali di registrazione COGE sull''esercizio '||aEs||'. Lock tabella CAUSALE_COGE';
		ibmutl200.loginf(aPgEsec,aMessage,'','');
		for aCC in (select * from causale_coge
				    where esercizio = aEsPrec) loop
			begin
				 insert into CAUSALE_COGE (ESERCIZIO,
							   CD_CAUSALE_COGE,
							   DS_CAUSALE,
							   TI_CAUSALE_COGE,
							   CENTRO_COSTO,
							   DT_CANCELLAZIONE,
							   DACR,
							   DUVA,
							   UTUV,
							   UTCR,
							   PG_VER_REC)
				 values (aEs,
					 aCC.CD_CAUSALE_COGE,
					 aCC.DS_CAUSALE,
					 aCC.TI_CAUSALE_COGE,
					 aCC.CENTRO_COSTO,
					 aCC.DT_CANCELLAZIONE,
					 sysdate,
					 sysdate,
					 cgUtente,
					 cgUtente,
					 1);
			exception when DUP_VAL_ON_INDEX then
				ibmutl200.logwar(aPgEsec,'Causale coge con PK('||aEs||', '||aCC.cd_causale_coge||') già inserita','','');
				stato_fine := 'W';
			end;
		end loop;

		-- ribaltamento tabella associativa tra la categoria di un bene e il tipo ammortamento
		aMessage := 'Ribaltamento associazione tra la categoria di un bene e il tipo ammortamento sull''esercizio '||aEs||'. Lock tabella ASS_TIPO_AMM_CAT_GRUP_INV';
		ibmutl200.loginf(aPgEsec,aMessage,'','');
		for aAssCatAmm in (select * from ASS_TIPO_AMM_CAT_GRUP_INV
				 	  	   where esercizio_competenza = aEsPrec) loop
			begin
				insert into ASS_TIPO_AMM_CAT_GRUP_INV (CD_TIPO_AMMORTAMENTO,
									TI_AMMORTAMENTO,
									CD_CATEGORIA_GRUPPO,
									ESERCIZIO_COMPETENZA,
									DT_CANCELLAZIONE,
									UTCR,
									DACR,
									UTUV,
									DUVA,
									PG_VER_REC)
				values (aAssCatAmm.CD_TIPO_AMMORTAMENTO,
					aAssCatAmm.TI_AMMORTAMENTO,
					aAssCatAmm.CD_CATEGORIA_GRUPPO,
					aEs,
					aAssCatAmm.DT_CANCELLAZIONE,
					cgUtente,
					sysdate,
					cgUtente,
					sysdate,
					1);
			exception when dup_val_on_index then
				ibmutl200.logwar(aPgEsec,'Associazione ammortamento-categoria bene con PK('||aAssCatAmm.cd_tipo_ammortamento||', '
													   ||aAssCatAmm.ti_ammortamento||', '
													   ||aAssCatAmm.cd_categoria_gruppo||', '
													   ||aEs||') già inserita','','');
				stato_fine := 'W';
			end;
		end loop;

		-- ribaltamento associazioni tra CODICE SIA in interfaccia di ritorno cassiere e codice CDS CIR
 		aMessage := 'Ribaltamento associazioni tra CODICE SIA in interfaccia di ritorno cassiere e codice CDS CIR sull''esercizio '||aEs||'. Lock tabella EXT_CASSIERE_CDS';
 		ibmutl200.loginf(aPgEsec,aMessage,'','');
 		for aExt in (select * from EXT_CASSIERE_CDS
 				 	 where esercizio = aEsPrec
 				 	 and cd_cds in (select cd_unita_organizzativa
							from v_unita_organizzativa_valida
							where fl_cds    = 'Y'
							and esercizio = aEs)) loop
 			begin
				insert into EXT_CASSIERE_CDS (ESERCIZIO,
							      CODICE_PROTO,
							      CD_CDS,
							      DACR,
							      UTCR,
							      DUVA,
							      UTUV,
							      PG_VER_REC,
							      codice_sia,
							      codice_cuc)
				values (aEs,
					aExt.CODICE_PROTO,
					aExt.CD_CDS,
					sysdate,
					cgUtente,
					sysdate,
					cgUtente,
					1,
				  aExt.CODICE_SIA,
          aExt.CODICE_CUC);
			exception when dup_val_on_index then
				ibmutl200.logwar(aPgEsec,'Associazione tra CODICE SIA in interfaccia di ritorno cassiere e codice CDS CIR con PK('||aEs||', '
													   ||aExt.codice_proto||') già inserita','','');
				stato_fine := 'W';
			end;
 		end loop;

    -- Ribaltamento anagrafiche per INTRASTAT
    ribaltaINTRASTAT(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);

		-- ribaltamento Nazioni inserite nella BLACKLIST
 		aMessage := 'Ribaltamento Nazioni inserite nella BLACKLIST sull''esercizio '||aEs||'. Lock tabella NAZIONE_BLACKLIST';
 		ibmutl200.loginf(aPgEsec,aMessage,'','');
 		for aNaz in (select * from NAZIONE_BLACKLIST
 				 	 where esercizio = aEsPrec) loop
 			begin
				insert into NAZIONE_BLACKLIST (ESERCIZIO,
							      									 CD_CATASTALE,
																		   DS_NAZIONE,
																		   DACR,
																		   UTCR,
																		   DUVA,
																		   UTUV,
																		   PG_VER_REC,
																			 CD_NAZIONE)
				       values (aEs,
					             aNaz.CD_CATASTALE,
					             aNaz.DS_NAZIONE,
											 sysdate,
					             cgUtente,
					             sysdate,
					             cgUtente,
					             1,
					             aNaz.CD_NAZIONE);
			exception when dup_val_on_index then
				ibmutl200.logwar(aPgEsec,'Nazione inserita nella BLACKLIST con PK('||aEs||', '
													   ||aNaz.CD_CATASTALE||','||aNaz.CD_NAZIONE||') già inserita','','');
				stato_fine := 'W';
			end;
 		end loop;

		-- Update del log sul processo

		endLogRibaltamentoAltro(aEs, aPgEsec, stato_fine, aMessage );

	exception when OTHERS then
	    rollback;
		aMessage := 'Errore non gestito: '||DBMS_UTILITY.FORMAT_ERROR_STACK;
		ibmutl200.LOGERR(aPgEsec,aMessage,'','');
	end;

end;
----------------------------------------------------------------------------
procedure init_ribaltamento_pdgp(aEs number, aPgEsec number, aMessage in out varchar2) is
aEsPrec         number;
stato_fine      char(1) := 'I';
aNum            number;
new_val01       VARCHAR2(100);
begin
	begin
        dbms_output.put_line('0001');
		startLogRibaltamento(aEs, aPgEsec, dsProcesso_pdgp , cgUtente);

		if isRibaltamentoPDGPEffettuato(aEs, aPgEsec) then
		   stato_fine := 'E';
		   aMessage := 'Lo script di ribaltamento per il PDGP è già stato eseguito con successo per l''esercizio '|| aEs ||': non è possibile eseguirlo nuovamente.';
		   endLogRibaltamentoPerPDGP(aEs, aPgEsec, stato_fine, aMessage);
		end if;
		if isRibaltamentoAltroEffettuato(aEs, aPgEsec) then
		   stato_fine := 'E';
		   aMessage := 'Lo script di ribaltamento è già stato eseguito con successo per l''esercizio '|| aEs ||': non è possibile eseguire quello per il PDGP.';
		   endLogRibaltamentoPerPDGP(aEs, aPgEsec, stato_fine, aMessage);
		end if;

		aEsPrec := aEs - 1;

		begin
			 select 1 into aNum
			 from dual
			 where exists (select 1 from esercizio_base
			 	   		   where esercizio = aEs);
			 stato_fine := 'E';
			 aMessage := 'Esercizio base '||aEs||' già esistente: impossibile ribaltare';
			 endLogRibaltamentoPerPDGP(aEs, aPgEsec, stato_fine, aMessage);
		exception when NO_DATA_FOUND then
			 null;
		end;

		begin
			 select 1 into aNum
			 from dual
			 where exists (select 1 from elemento_voce
			 	   		   where esercizio = aEs);
			 stato_fine := 'E';
			 aMessage := 'Esistono elementi voce definiti sull''esercizio '||aEs||': impossibile ribaltare';
			 endLogRibaltamentoPerPDGP(aEs, aPgEsec, stato_fine, aMessage);
		exception when NO_DATA_FOUND then
			 null;
		end;

		-- Creazione esercizio base
		begin
			aMessage := 'Inserimento dell''esercizio base '||aEs||'. Lock tabella ESERCIZIO_BASE';
			ibmutl200.LOGINF(aPgEsec,aMessage,'','');

			insert into ESERCIZIO_BASE (ESERCIZIO, DACR, DUVA, UTUV, UTCR, PG_VER_REC)
			values (aEs,sysdate,sysdate,cgUtente,cgUtente,1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Esercizio base '||aEs||' già esistente','','');
			stato_fine := 'W';
		end;

		-- Creazione Parametri CNR
		begin
			aMessage := 'Inserimento dei Parametri CNR per l''esercizio base '||aEs||'. Lock tabella PARAMETRI_CNR';
			ibmutl200.LOGINF(aPgEsec,aMessage,'','');
			insert into PARAMETRI_CNR (ESERCIZIO,CD_TIPO_RAPPORTO,IMPORTO_FRANCHIGIA_OCCA,
					      	   DACR,UTCR,DUVA,UTUV,PG_VER_REC,FL_VERSAMENTI_CORI,VERSAMENTI_CORI_GIORNO,CD_TIPO_RAPPORTO_PROF,
					      	   LIVELLO_PDG_DECIS_SPE,LIVELLO_PDG_DECIS_ETR,LIVELLO_CONTRATT_PDG_SPE,FL_REGOLAMENTO_2006,FL_DIARIA_MISS_ITALIA,
					      	   FL_VISTO_DIP_VARIAZIONI,IMPORTO_MIN_VISTO_DIP,FL_MOTIVAZIONE_SU_IMP,IMPORTO_MAX_IMP,
					      	   FL_DEDUZIONE_IRPEF,FL_DEDUZIONE_FAMILY,FL_DETRAZIONI_ALTRE,FL_DETRAZIONI_FAMILY,
						         FL_APPROVATO_DEFINITIVO,FL_SIOPE,RICERCA_PROF_INT_GIORNI_PUBBL,RICERCA_PROF_INT_GIORNI_SCAD,FL_INCARICO,
						         OGGETTO_EMAIL_TERZI_CONGUA,CORPO_EMAIL_TERZI_CONGUA,CORPO_EMAIL,OGGETTO_EMAIL,FL_OBB_INTRASTAT,FL_CUP, CLAUSOLA_ORDINE,DATA_STIPULA_CONTRATTI,LIVELLO_PDG_COFOG,
						         FL_SIOPE_CUP,FL_CREDITO_IRPEF,FL_NUOVO_PDG,DATA_ATTIVAZIONE_NEW_VOCE,FL_TESORERIA_UNICA,FL_PUBBLICA_CONTRATTO,LIVELLO_PAT,LIVELLO_ECO,
						         FL_PDG_CODLAST,FL_PDG_CONTRATTAZIONE,FL_PDG_QUADRA_FONTI_ESTERNE,FL_NUOVA_GESTIONE_PG)
			select aEs, CD_TIPO_RAPPORTO,IMPORTO_FRANCHIGIA_OCCA,
			       sysdate,cgUtente,sysdate,cgUtente,1,FL_VERSAMENTI_CORI,VERSAMENTI_CORI_GIORNO,CD_TIPO_RAPPORTO_PROF,
			       LIVELLO_PDG_DECIS_SPE,LIVELLO_PDG_DECIS_ETR,LIVELLO_CONTRATT_PDG_SPE,FL_REGOLAMENTO_2006,FL_DIARIA_MISS_ITALIA,
			       FL_VISTO_DIP_VARIAZIONI,IMPORTO_MIN_VISTO_DIP,FL_MOTIVAZIONE_SU_IMP,IMPORTO_MAX_IMP,
			       FL_DEDUZIONE_IRPEF,FL_DEDUZIONE_FAMILY,FL_DETRAZIONI_ALTRE,FL_DETRAZIONI_FAMILY,
			       'N',FL_SIOPE,RICERCA_PROF_INT_GIORNI_PUBBL,RICERCA_PROF_INT_GIORNI_SCAD,FL_INCARICO,
		         NULL,NULL,NULL,NULL,FL_OBB_INTRASTAT,FL_CUP,CLAUSOLA_ORDINE,DATA_STIPULA_CONTRATTI,LIVELLO_PDG_COFOG,FL_SIOPE_CUP,'N'--FL_CREDITO_IRPEF
		         ,FL_NUOVO_PDG,null,FL_TESORERIA_UNICA,FL_PUBBLICA_CONTRATTO,LIVELLO_PAT,LIVELLO_ECO,
		         FL_PDG_CODLAST,FL_PDG_CONTRATTAZIONE,FL_PDG_QUADRA_FONTI_ESTERNE,FL_NUOVA_GESTIONE_PG
			from PARAMETRI_CNR
			where ESERCIZIO = aEsPrec;
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Parametri CNR per l''esercizio base '||aEs||' già esistenti','','');
			stato_fine := 'W';
		end;
dbms_output.put_line('0002');

		-- Definizione esercizio contabile dell'ente
		begin
			 aMessage := 'Inserimento dell''esercizio contabile '||aEs||' per l''ente. Lock tabelle ESERCIZIO, NUMERAZIONE_DOC_CONT';
			ibmutl200.LOGINF(aPgEsec,aMessage,'','');
			insert into ESERCIZIO (CD_CDS
				   				  ,ESERCIZIO
								  ,DS_ESERCIZIO
								  ,ST_APERTURA_CHIUSURA
								  ,DACR
								  ,UTCR
								  ,DUVA
								  ,UTUV
								  ,PG_VER_REC
								  ,IM_CASSA_INIZIALE)
			values (cnrctb020.getCDCDSENTE(aEs)
				   ,aEs
				   ,'Esercizio contabile '|| aEs
				   ,'I'
				   ,sysdate
				   ,cgUtente
				   ,sysdate
				   ,cgUtente
				   ,1
				   ,0);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Esercizio contabile '||aEs||' per l''ente già esistente','','');
			stato_fine := 'W';
		end;

		-- Aggiornamento numeratori doc cont
		CNRCTB018.AGGIORNANUMERATORI(aEs,cnrctb020.getCDCDSENTE(aEs), cgUtente);


		-- Ribaltamento LUNGHEZZA_CHIAVI
		/*lock table lunghezza_chiavi in exclusive mode nowait;
		aMessage := 'Ribaltamento lunghezze chiavi. Lock tabella LUNGHEZZA_CHIAVI';
		ibmutl200.LOGINF(aPgEsec,aMessage,'','');
		declare
			 lEs esercizio%rowtype;
		begin
			 select * into lEs
			 from esercizio
			 where cd_cds = cnrctb020.getCDCDSENTE(aEs)
			   and esercizio = aEs;
			 CNRCTB015.coie_LUNGHEZZA_CHIAVI (lEs);
		exception when dup_val_on_index then
				  null;
		end;*/

-- NOTA BENE: STANI
-- prima del 14 ottobre 2009 era qui il ribaltamento della CONFIGURAZIONE_CNR - SPOSTATA PIU' IN BASSO DOPO LA CLASSIFICAZIONE_VOCI

    -- Ribaltamento TIPO_VARIAZIONE sull'esercizio contabile destinazione
		-- a partire dall'esercizio precedente
		begin
			aMessage := 'Inserimento di TIPO_VARIAZIONE per l''esercizio base '||aEs||'. Lock tabella TIPO_VARIAZIONE';
			ibmutl200.LOGINF(aPgEsec,aMessage,'','');
			insert into TIPO_VARIAZIONE (ESERCIZIO,CD_TIPO_VARIAZIONE,DS_TIPO_VARIAZIONE,TI_TIPO_VARIAZIONE,
						     FL_UTILIZZABILE_ENTE,FL_UTILIZZABILE_AREA,FL_UTILIZZABILE_CDS,
  						     TI_APPROVAZIONE,DACR,UTCR,DUVA,UTUV,PG_VER_REC,
  						     FL_VISTO_DIP_SALDO_MODULO,FL_VISTO_DIP_CLASSIFICAZIONE,FL_VARIAZIONE_TRASFERIMENTO)
			select aEs,CD_TIPO_VARIAZIONE,DS_TIPO_VARIAZIONE,TI_TIPO_VARIAZIONE,
			       FL_UTILIZZABILE_ENTE,FL_UTILIZZABILE_AREA,FL_UTILIZZABILE_CDS,
  			       TI_APPROVAZIONE,sysdate,cgUtente,sysdate,cgUtente,1,
  			       FL_VISTO_DIP_SALDO_MODULO,FL_VISTO_DIP_CLASSIFICAZIONE,FL_VARIAZIONE_TRASFERIMENTO
			from TIPO_VARIAZIONE
			where ESERCIZIO = aEsPrec;
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'TIPO_VARIAZIONE per l''esercizio base '||aEs||' già esistente','','');
			stato_fine := 'W';
		end;
dbms_output.put_line('0003');

		-- Ribaltamento dell'anagrafica dei capitoli
		ribaltaEV(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);
dbms_output.put_line('0004');

		-- Ribaltamento CONFIGURAZIONE_CNR sull'esercizio contabile destinazione
		-- a partire dall'esercizio precedente
		aMessage := 'Ribaltamento configurazione CNR sull''esercizio '||aEs||'. Lock tabella CONFIGURAZIONE_CNR';
		ibmutl200.LOGINF(aPgEsec,aMessage,'','');
		for aConfCNR in (select * from CONFIGURAZIONE_CNR
					 	 where esercizio = aEsPrec) Loop

                        If aConfCNR.cd_chiave_primaria = 'PIANO_RIPARTO' Then

                           Begin
                             Select new_cla.id_classificazione
                             Into   new_val01
                             From   classificazione_voci old_cla, classificazione_voci New_cla
                             Where  old_cla.id_classificazione = aConfCNR.val01 And
                                    old_cla.esercizio = aEsPrec And
                                    new_cla.esercizio = aEs And
                                    old_cla.TI_GESTIONE           =  new_cla.TI_GESTIONE And
                                    old_cla.CD_LIVELLO1           =  new_cla.CD_LIVELLO1 And
                                    Nvl(old_cla.CD_LIVELLO2, 'A') =  Nvl(new_cla.CD_LIVELLO2, 'A') And
                                    Nvl(old_cla.CD_LIVELLO3, 'A') =  Nvl(new_cla.CD_LIVELLO3, 'A') And
                                    Nvl(old_cla.CD_LIVELLO4, 'A') =  Nvl(new_cla.CD_LIVELLO4, 'A') And
                                    Nvl(old_cla.CD_LIVELLO5, 'A') =  Nvl(new_cla.CD_LIVELLO5, 'A') And
                                    Nvl(old_cla.CD_LIVELLO6, 'A') =  Nvl(new_cla.CD_LIVELLO6, 'A') And
                                    Nvl(old_cla.CD_LIVELLO7, 'A') =  Nvl(new_cla.CD_LIVELLO7, 'A');
                           Exception
                             When Others Then
                                   new_val01 := Null;
                           End;

                        Else
                           new_val01 := aConfCNR.VAL01;
                        End If;

			begin
				insert into CONFIGURAZIONE_CNR (ESERCIZIO,
								CD_UNITA_FUNZIONALE,
								CD_CHIAVE_PRIMARIA,
								CD_CHIAVE_SECONDARIA,
								VAL01,
								VAL02,
								VAL03,
								VAL04,
								IM01,
								IM02,
								DT01,
								DT02,
								DACR,
								UTCR,
								DUVA,
								UTUV,
								PG_VER_REC)
				values (aEs,
					aConfCNR.CD_UNITA_FUNZIONALE,
					aConfCNR.CD_CHIAVE_PRIMARIA,
					aConfCNR.CD_CHIAVE_SECONDARIA,
					new_val01,
					aConfCNR.VAL02,
					aConfCNR.VAL03,
					aConfCNR.VAL04,
					aConfCNR.IM01,
					aConfCNR.IM02,
					aConfCNR.DT01,
					aConfCNR.DT02,
					sysdate,
					cgUtente,
					sysdate,
					cgUtente,
					1);
			exception when DUP_VAL_ON_INDEX then
				ibmutl200.LOGWAR(aPgEsec,'CONFIGURAZIONE_CNR con PK('||aEs||', '
					         ||aConfCNR.CD_UNITA_FUNZIONALE||', '
						 ||aConfCNR.CD_CHIAVE_PRIMARIA||', '
						 ||aConfCNR.CD_CHIAVE_SECONDARIA||') già esistente','','');
				stato_fine := 'W';
			end;
		end loop;
dbms_output.put_line('0005');


		-- Ribaltamento della struttura organizzativa
		ribaltaStruttOrg(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);
dbms_output.put_line('0006');

    -- Ribaltamento limiti di spesa
    ribaltaLimitiSpesa(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);
/*  spostato in ribaltamento_altro  rospuc
		-- Ribaltamento del piano dei conti E/P
		ribaltaEP(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);

		-- Ribaltamento configurazione CORI per PDGP
		ribaltaCORI_pdgp(aEs, aEsPrec, aPgEsec, stato_fine, aMessage);
*/
		-- Update del log sul processo

		endLogRibaltamentoPerPDGP(aEs, aPgEsec, stato_fine, aMessage );
dbms_output.put_line('0007');

	exception when OTHERS then
	    rollback;
		aMessage := 'Errore non gestito: '||DBMS_UTILITY.FORMAT_ERROR_STACK;
		ibmutl200.LOGERR(aPgEsec,aMessage,'','');
	end;

end;

procedure init_ribaltamento_pdgp(aEs number, aMessage in out varchar2) is
    aPgEsec  number;
begin
    aPgEsec := IBMUTL200.LOGSTART(TI_LOG_RIBALTAMENTO_PDGP,dsProcesso_pdgp,null,cgUtente,null,null);
    init_ribaltamento_pdgp(aEs, aPgEsec, aMessage);
end;
----------------------------------------------------------------------------
Procedure ribaltaEV(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
begin
	-- Ribaltamento Classificazioni Entrate
	aMessage := 'Ribaltamento Classificazioni Entrate sull''esercizio '||aEsDest||'. Lock tabella CLASSIFICAZIONE_ENTRATE';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aClassE in (select * from CLASSIFICAZIONE_ENTRATE
			where esercizio = aEsOrig
			order by esercizio, codice_cla_e) loop
		begin
			insert into CLASSIFICAZIONE_ENTRATE (ESERCIZIO,
							     CODICE_CLA_E,
							     DESCRIZIONE,
							     FL_MASTRINO,
							     ESERCIZIO_PADRE,
							     CODICE_CLA_E_PADRE,
							     DACR,
							     UTCR,
							     DUVA,
							     UTUV,
							     PG_VER_REC)
			values (aEsDest,
				aClassE.CODICE_CLA_E,
				aClassE.DESCRIZIONE,
				aClassE.FL_MASTRINO,
				DECODE(aClassE.CODICE_CLA_E_PADRE, NULL, NULL,aEsDest),
				aClassE.CODICE_CLA_E_PADRE,
				sysdate,
				cgUtente,
				sysdate,
				cgutente,
				1);
		exception
		   when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CLASSIFICAZIONE_ENTRATE con PK ('||aEsDest||', '||aClassE.CODICE_CLA_E||') già esistente','','');
			aStato := 'W';
		   when OTHERS then
			ibmutl200.LOGWAR(aPgEsec,'SK_CLASSIFICAZIONE_ENTRATE violata per la classificazione ('||aEsDest||', '||aClassE.CODICE_CLA_E||')','','');
			aStato := 'W';
		end;
	end loop;
	-- Ribaltamento Classificazioni Spese
	aMessage := 'Ribaltamento Classificazioni Spese sull''esercizio '||aEsDest||'. Lock tabella CLASSIFICAZIONE_SPESE';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aClassS in (select * from CLASSIFICAZIONE_SPESE
			where esercizio = aEsOrig
			order by esercizio, codice_cla_s) loop
		begin
			insert into CLASSIFICAZIONE_SPESE (ESERCIZIO,
							     CODICE_CLA_S,
							     DESCRIZIONE,
							     FL_MASTRINO,
							     ESERCIZIO_PADRE,
							     CODICE_CLA_S_PADRE,
							     DACR,
							     UTCR,
							     DUVA,
							     UTUV,
							     PG_VER_REC)
			values (aEsDest,
				aClassS.CODICE_CLA_S,
				aClassS.DESCRIZIONE,
				aClassS.FL_MASTRINO,
				DECODE(aClassS.CODICE_CLA_S_PADRE, NULL, NULL,aEsDest),
				aClassS.CODICE_CLA_S_PADRE,
				sysdate,
				cgUtente,
				sysdate,
				cgutente,
				1);
		exception
		   when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CLASSIFICAZIONE_SPESE con PK ('||aEsDest||', '||aClassS.CODICE_CLA_S||') già esistente','','');
			aStato := 'W';
		   when OTHERS then
			ibmutl200.LOGWAR(aPgEsec,'SK_CLASSIFICAZIONE_SPESE violata per la classificazione ('||aEsDest||', '||aClassS.CODICE_CLA_S||')','','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento Parametri Livelli
        Begin
	   aMessage := 'Ribaltamento Parametri Livelli sull''esercizio '||aEsDest||'. Lock tabella PARAMETRI_LIVELLI';
	   ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	   insert into PARAMETRI_LIVELLI (ESERCIZIO,LIVELLI_ENTRATA,
	   				  LUNG_LIVELLO1E,DS_LIVELLO1E,
	   				  LUNG_LIVELLO2E,DS_LIVELLO2E,
	   				  LUNG_LIVELLO3E,DS_LIVELLO3E,
	   				  LUNG_LIVELLO4E,DS_LIVELLO4E,
	   				  LUNG_LIVELLO5E,DS_LIVELLO5E,
	   				  LUNG_LIVELLO6E,DS_LIVELLO6E,
	   				  LUNG_LIVELLO7E,DS_LIVELLO7E,
	   				  LIVELLI_SPESA,
	   				  LUNG_LIVELLO1S,DS_LIVELLO1S,
	   				  LUNG_LIVELLO2S,DS_LIVELLO2S,
	   				  LUNG_LIVELLO3S,DS_LIVELLO3S,
	   				  LUNG_LIVELLO4S,DS_LIVELLO4S,
	   				  LUNG_LIVELLO5S,DS_LIVELLO5S,
	   				  LUNG_LIVELLO6S,DS_LIVELLO6S,
	   				  LUNG_LIVELLO7S,DS_LIVELLO7S,
				      	  DACR,UTCR,DUVA,UTUV,PG_VER_REC)
		select aEsDest, LIVELLI_ENTRATA,
	   	  LUNG_LIVELLO1E,DS_LIVELLO1E,
	   	  LUNG_LIVELLO2E,DS_LIVELLO2E,
	   	  LUNG_LIVELLO3E,DS_LIVELLO3E,
	   	  LUNG_LIVELLO4E,DS_LIVELLO4E,
	   	  LUNG_LIVELLO5E,DS_LIVELLO5E,
	   	  LUNG_LIVELLO6E,DS_LIVELLO6E,
	   	  LUNG_LIVELLO7E,DS_LIVELLO7E,
	   	  LIVELLI_SPESA,
	   	  LUNG_LIVELLO1S,DS_LIVELLO1S,
	   	  LUNG_LIVELLO2S,DS_LIVELLO2S,
	   	  LUNG_LIVELLO3S,DS_LIVELLO3S,
	   	  LUNG_LIVELLO4S,DS_LIVELLO4S,
	   	  LUNG_LIVELLO5S,DS_LIVELLO5S,
	   	  LUNG_LIVELLO6S,DS_LIVELLO6S,
	   	  LUNG_LIVELLO7S,DS_LIVELLO7S,
		  Sysdate,cgUtente,sysdate,cgUtente,1
		from PARAMETRI_LIVELLI
		where ESERCIZIO = aEsOrig;
	Exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Parametri Livelli per l''esercizio base '||aEsDest||' già esistenti','','');
			aStato := 'W';
	End;

	-- Ribaltamento elemento voce
	aMessage := 'Ribaltamento dell''anagrafica dei capitoli sull''esercizio '||aEsDest||'. Lock tabelle ELEMENTO_VOCE, VOCE_F, VOCE_F_SALDI_CMP';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aElVoce in (select * from ELEMENTO_VOCE
				    where esercizio = aEsOrig
					order by ti_appartenenza, ti_gestione, cd_elemento_voce) loop -- ordine per RX_ELEMENTO_VOCE00
		begin
			insert into ELEMENTO_VOCE  (ESERCIZIO,
						    TI_APPARTENENZA,
						    TI_GESTIONE,
						    CD_ELEMENTO_VOCE,
						    CD_PROPRIO_ELEMENTO,
						    TI_ELEMENTO_VOCE,
						    FL_LIMITE_ASS_OBBLIG,
						    FL_VOCE_PERSONALE,
						    CD_PARTE,
						    DS_ELEMENTO_VOCE,
						    DACR,
						    UTUV,
						    UTCR,
						    DUVA,
						    PG_VER_REC,
						    CD_ELEMENTO_PADRE,
						    FL_PARTITA_GIRO,
						    CD_CAPOCONTO_FIN,
						    FL_VOCE_SAC,
						    FL_VOCE_NON_SOGG_IMP_AUT,
						    ESERCIZIO_CLA_E,
  						    COD_CLA_E,
  						    ESERCIZIO_CLA_S,
  						    COD_CLA_S,
  						    FL_RECON,
  						    FL_INV_BENI_PATR,
  						    ID_CLASSIFICAZIONE,
  						    CD_TIPO_SPESA_SAC,
  						    CD_TIPO_SPESA_IST,
  						    FL_VOCE_FONDO,
  						    FL_CHECK_TERZO_SIOPE,
  						    FL_INV_BENI_COMP,
  						    FL_LIMITE_SPESA,
  						    FL_PRELIEVO,
  						    FL_SOGGETTO_PRELIEVO,
  						    PERC_PRELIEVO_PDGP_ENTRATE,
  						    FL_SOLO_RESIDUO,
  						    FL_SOLO_COMPETENZA,
  						    FL_TROVATO,
  						    FL_AZZERA_RESIDUI,
  						    ESERCIZIO_ELEMENTO_PADRE,
  						    TI_APPARTENENZA_ELEMENTO_PADRE,
  						    TI_GESTIONE_ELEMENTO_PADRE,
  						    FL_MISSIONI,
  						    CD_UNITA_PIANO,
  						    CD_VOCE_PIANO,
  						    GG_DEROGA_OBBL_COMP_PRG_SCAD,
  						    GG_DEROGA_OBBL_RES_PRG_SCAD,
                            FL_COMUNICA_PAGAMENTI,
                            FL_LIMITE_COMPETENZA,
                            BLOCCO_IMPEGNI_NATFIN)
			values (aEsDest,
				aElVoce.TI_APPARTENENZA,
				aElVoce.TI_GESTIONE,
				aElVoce.CD_ELEMENTO_VOCE,
				aElVoce.CD_PROPRIO_ELEMENTO,
				aElVoce.TI_ELEMENTO_VOCE,
				aElVoce.FL_LIMITE_ASS_OBBLIG,
				aElVoce.FL_VOCE_PERSONALE,
				aElVoce.CD_PARTE,
				aElVoce.DS_ELEMENTO_VOCE,
				sysdate,
				cgUtente,
				cgUtente,
				sysdate,
				1,
				aElVoce.CD_ELEMENTO_PADRE,
				aElVoce.FL_PARTITA_GIRO,
				aElVoce.CD_CAPOCONTO_FIN,
				aElVoce.FL_VOCE_SAC,
				aElVoce.FL_VOCE_NON_SOGG_IMP_AUT,
				Decode(aElVoce.COD_CLA_E,Null,Null,aEsDest),
  				aElVoce.COD_CLA_E,
  				Decode(aElVoce.COD_CLA_S,Null,Null,aEsDest),
  				aElVoce.COD_CLA_S,
  				aElVoce.FL_RECON,
  				aElVoce.FL_INV_BENI_PATR,
  				null,
  				aElVoce.CD_TIPO_SPESA_SAC,
  				aElVoce.CD_TIPO_SPESA_IST,
  				aElVoce.FL_VOCE_FONDO,
  				aElVoce.FL_CHECK_TERZO_SIOPE,
  				aElVoce.FL_INV_BENI_COMP,
  				aElVoce.FL_LIMITE_SPESA,
  				aElVoce.FL_PRELIEVO,
  				aElVoce.FL_SOGGETTO_PRELIEVO,
  				aElVoce.PERC_PRELIEVO_PDGP_ENTRATE,
  				aElVoce.FL_SOLO_RESIDUO,
  				aElVoce.FL_SOLO_COMPETENZA,
  				aElVoce.FL_TROVATO,
  				aElVoce.FL_AZZERA_RESIDUI,
  				aElVoce.ESERCIZIO_ELEMENTO_PADRE,
  				aElVoce.TI_APPARTENENZA_ELEMENTO_PADRE,
  				aElVoce.TI_GESTIONE_ELEMENTO_PADRE,
  				aElVoce.FL_MISSIONI,
  				aElVoce.CD_UNITA_PIANO,
  				aElVoce.CD_VOCE_PIANO,
  				aElVoce.GG_DEROGA_OBBL_COMP_PRG_SCAD,
  				aElVoce.GG_DEROGA_OBBL_RES_PRG_SCAD,
                aElVoce.FL_COMUNICA_PAGAMENTI,
                aElVoce.FL_LIMITE_COMPETENZA,
                aElVoce.BLOCCO_IMPEGNI_NATFIN
  				);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ELEMENTO_VOCE con PK('||aEsDest||', '
					||aElVoce.TI_APPARTENENZA||', '
					||aElVoce.TI_GESTIONE||', '
					||aElVoce.CD_ELEMENTO_VOCE||') già esistente','','');
			aStato := 'W';
		end;
	end loop;
	-- all'inserimento di record in ELEMENTO_VOCE viene attivato un trigger
	-- che esplode l'anagrafica dei capitoli sul piano dei conti finanziario
	-- in VOCE_F

	-- Ribaltamento delle associazioni elementi voce, funzioni e tipologia di cds
	aMessage := 'Ribaltamento delle associazioni elementi voce, funzioni e tipologia di cds sull''esercizio '||aEsDest||'. Lock tabella ASS_EV_FUNZ_TIPOCDS';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aAssEVF in (select * from ASS_EV_FUNZ_TIPOCDS
			 	 where esercizio = aEsOrig) loop
		begin
			insert into ASS_EV_FUNZ_TIPOCDS (ESERCIZIO,
							 CD_FUNZIONE,
							 CD_TIPO_UNITA,
							 CD_CONTO,
							 UTUV,
							 DACR,
							 UTCR,
							 DUVA,
							 PG_VER_REC)
			values (aEsDest,
				aAssEVF.CD_FUNZIONE,
				aAssEVF.CD_TIPO_UNITA,
				aAssEVF.CD_CONTO,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_EV_FUNZ_TIPOCDS con PK('||aEsDest||', '
		   			  ||aAssEVF.CD_FUNZIONE||', '
					  ||aAssEVF.CD_TIPO_UNITA||', '
					  ||aAssEVF.CD_CONTO||') già esistente','','');
			aStato := 'W';
		end;
	end loop;
	-- all'inserimento di record in ASS_EV_FUNZ_TIPOCDS viene attivato un trigger
	-- che esplode l'anagrafica dei capitoli sul piano dei conti finanziario
	-- in VOCE_F

	-- Ribaltamento delle associazioni fra elementi voce
	aMessage := 'Ribaltamento delle associazioni fra elementi voce sull''esercizio '||aEsDest||'. Lock tabella ASS_EV_EV';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aAssEVEV in (select * from ASS_EV_EV
			 	 where esercizio = aEsOrig) loop
		begin
			insert into ASS_EV_EV  (ESERCIZIO,
						TI_APPARTENENZA,
						TI_GESTIONE,
						CD_ELEMENTO_VOCE,
						CD_ELEMENTO_VOCE_COLL,
						TI_APPARTENENZA_COLL,
						TI_GESTIONE_COLL,
						CD_CDS,
						CD_NATURA,
						TI_ELEMENTO_VOCE,
						TI_ELEMENTO_VOCE_COLL,
						UTUV,
						DACR,
						UTCR,
						DUVA,
						PG_VER_REC)
			values (aEsDest,
				aAssEVEV.TI_APPARTENENZA,
				aAssEVEV.TI_GESTIONE,
				aAssEVEV.CD_ELEMENTO_VOCE,
				aAssEVEV.CD_ELEMENTO_VOCE_COLL,
				aAssEVEV.TI_APPARTENENZA_COLL,
				aAssEVEV.TI_GESTIONE_COLL,
				aAssEVEV.CD_CDS,
				aAssEVEV.CD_NATURA,
				aAssEVEV.TI_ELEMENTO_VOCE,
				aAssEVEV.TI_ELEMENTO_VOCE_COLL,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_EV_EV con PK('||aEsDest||', '
					||aAssEVEV.TI_APPARTENENZA||', '
					||aAssEVEV.TI_GESTIONE||', '
					||aAssEVEV.CD_ELEMENTO_VOCE||', '
					||aAssEVEV.CD_ELEMENTO_VOCE_COLL||', '
					||aAssEVEV.TI_APPARTENENZA_COLL||', '
					||aAssEVEV.TI_GESTIONE_COLL||', '
					||aAssEVEV.CD_CDS||', '
					||aAssEVEV.CD_NATURA||') già esistente','','');
			aStato := 'W';
		end;
	end loop;
/* spostato in ribaltamento_altro rospuc
	-- Ribaltamento associazioni tra categorie e voci del piano dei conti
	lock table categoria_gruppo_voce in exclusive mode nowait;
	aMessage := 'Ribaltamento associazioni tra categorie e voci del piano dei conti sull''esercizio '||aEsDest||'. Lock tabella CATEGORIA_GRUPPO_VOCE';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aCatGV in (select * from CATEGORIA_GRUPPO_VOCE
			   	   where esercizio = aEsOrig) loop
		begin
			insert into CATEGORIA_GRUPPO_VOCE  (CD_CATEGORIA_GRUPPO,
							    ESERCIZIO,
							    TI_APPARTENENZA,
							    TI_GESTIONE,
							    DACR,
							    UTCR,
							    DUVA,
							    UTUV,
							    PG_VER_REC,
							    CD_ELEMENTO_VOCE)
			values (aCatGV.CD_CATEGORIA_GRUPPO,
				aEsDest,
				aCatGV.TI_APPARTENENZA,
				aCatGV.TI_GESTIONE,
				sysdate,
				cgUtente,
				sysdate,
				cgutente,
				1,
				aCatGV.CD_ELEMENTO_VOCE);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CATEGORIA_GRUPPO_VOCE con PK ('||aCatGV.CD_CATEGORIA_GRUPPO||', '
					 ||aEsDest||', '
					 ||aCatGV.TI_APPARTENENZA||', '
					 ||aCatGV.TI_GESTIONE||', '
					 ||aCatGV.CD_ELEMENTO_VOCE||') già esistente','','');
			aStato := 'W';
		end;
	end loop;
*/ -- fine spostamento in ribaltamento_altro rospuc
	-- Ribaltamento classificazione voci
	declare
	   new_id NUMBER;
	   new_id_padre NUMBER:=Null;
	begin
	lock table classificazione_voci in exclusive mode nowait;
	aMessage := 'Ribaltamento Classificazione Voci sull''esercizio '||aEsDest||'. Lock tabella CLASSIFICAZIONE_VOCI';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');

	Select Max(id_classificazione)
	Into new_id
	From CLASSIFICAZIONE_VOCI;

	  for aClassV in (select * from CLASSIFICAZIONE_VOCI
			where esercizio = aEsOrig
			order by esercizio, ti_gestione, cd_livello1 Desc, cd_livello2 Desc ,cd_livello3 Desc ,
			cd_livello4 Desc, cd_livello5 Desc, cd_livello6 Desc, cd_livello7 Desc)
	  loop
		begin
			new_id := new_id + 1;
			insert into CLASSIFICAZIONE_VOCI (ID_CLASSIFICAZIONE,
							  ESERCIZIO,
							  TI_GESTIONE,
							  DS_CLASSIFICAZIONE,
							  CD_LIVELLO1,
							  CD_LIVELLO2,
							  CD_LIVELLO3,
							  CD_LIVELLO4,
							  CD_LIVELLO5,
							  CD_LIVELLO6,
							  CD_LIVELLO7,
							  ID_CLASS_PADRE,
							  FL_MASTRINO,
							  DUVA,
							  UTUV,
							  DACR,
							  UTCR,
							  PG_VER_REC,
							  FL_CLASS_SAC,
							  FL_SOLO_GESTIONE,
							  FL_ACCENTRATO,
							  FL_DECENTRATO,
							  CDR_ACCENTRATORE,
							  FL_PIANO_RIPARTO,
						  	  FL_ESTERNA_DA_QUADRARE_SAC,
						  	  FL_VISTO_DIP_VARIAZIONI,
						  	  TI_CLASSIFICAZIONE,
						  	  FL_PREV_OBB_ANNO_SUC,
						  	  IM_LIMITE_ASSESTATO)
			values (new_id,
				aEsDest,
				aClassV.TI_GESTIONE,
				aClassV.DS_CLASSIFICAZIONE,
				aClassV.CD_LIVELLO1,
				aClassV.CD_LIVELLO2,
				aClassV.CD_LIVELLO3,
				aClassV.CD_LIVELLO4,
				aClassV.CD_LIVELLO5,
				aClassV.CD_LIVELLO6,
				aClassV.CD_LIVELLO7,
				null,
				aClassV.FL_MASTRINO,
				sysdate,
				cgUtente,
				sysdate,
				cgutente,
				1,
				aClassV.FL_CLASS_SAC,
				aClassV.FL_SOLO_GESTIONE,
				aClassV.FL_ACCENTRATO,
				aClassV.FL_DECENTRATO,
				aClassV.CDR_ACCENTRATORE,
				aClassV.FL_PIANO_RIPARTO,
				aClassV.FL_ESTERNA_DA_QUADRARE_SAC,
				aClassV.FL_VISTO_DIP_VARIAZIONI,
				aclassV.TI_CLASSIFICAZIONE,
				aclassV.FL_PREV_OBB_ANNO_SUC,
				aclassV.IM_LIMITE_ASSESTATO);

			--Aggiorno l'elemento voce con la nuova classificazione creata
			update elemento_voce v
			set v.id_classificazione =  new_id
			where v.esercizio = aEsDest
			and (v.ti_appartenenza, v.ti_gestione, v.cd_elemento_voce) In
			     (Select a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce
			     From elemento_voce a
			     Where a.esercizio = aEsOrig
			       And a.id_classificazione = aClassV.id_classificazione);
		exception
		   when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CLASSIFICAZIONE_VOCI con PK ('||new_id||') già esistente','','');
			aStato := 'W';
		   when OTHERS then
			ibmutl200.LOGWAR(aPgEsec,'SK_CLASSIFICAZIONE_VOCI violata per la classificazione ('||new_id||')','','');
			aStato := 'W';
		end;
	  end loop;
	end;

	-- Aggiornamento id_class_padre nel nuovo anno
	Declare
    	    rec_class 	CLASSIFICAZIONE_VOCI%Rowtype;
    	    new_padre   NUMBER;
	Begin
	   for aClassPadre in (select * from CLASSIFICAZIONE_VOCI
		    where esercizio = aEsOrig
		    And id_class_padre Is Not Null)
  	   loop
  		select *
		into rec_class
		From CLASSIFICAZIONE_VOCI
		where esercizio = aEsOrig
		and ti_gestione = aClassPadre.ti_gestione
		and id_classificazione = aClassPadre.id_class_padre;

		select id_classificazione
		into new_padre
		from CLASSIFICAZIONE_VOCI
		where esercizio = aEsDest
		and ti_gestione = rec_class.ti_gestione
		and cd_livello1 = rec_class.cd_livello1
		and ((rec_class.cd_livello2 Is Not Null And cd_livello2 = rec_class.cd_livello2)
	      	      Or
	      	     (rec_class.cd_livello2 Is Null And cd_livello2 Is Null))
		and ((rec_class.cd_livello3 Is Not Null And cd_livello3 = rec_class.cd_livello3)
	              Or
	             (rec_class.cd_livello3 Is Null And cd_livello3 Is Null))
		and ((rec_class.cd_livello4 Is Not Null And cd_livello4 = rec_class.cd_livello4)
	              Or
	             (rec_class.cd_livello4 Is Null And cd_livello4 Is Null))
		and ((rec_class.cd_livello5 Is Not Null And cd_livello5 = rec_class.cd_livello5)
	              Or
	             (rec_class.cd_livello5 Is Null And cd_livello5 Is Null))
		and ((rec_class.cd_livello6 Is Not Null And cd_livello6 = rec_class.cd_livello6)
	              Or
	             (rec_class.cd_livello6 Is Null And cd_livello6 Is Null))
		and ((rec_class.cd_livello7 Is Not Null And cd_livello7 = rec_class.cd_livello7)
	              Or
	             (rec_class.cd_livello7 Is Null And cd_livello7 Is Null));

		update CLASSIFICAZIONE_VOCI
		set id_class_padre = new_padre
		where esercizio = aEsDest
		and ti_gestione = aClassPadre.ti_gestione
		and cd_livello1 = aClassPadre.cd_livello1
		and ((aClassPadre.cd_livello2 Is Not Null And cd_livello2 = aClassPadre.cd_livello2)
	             Or
	             (aClassPadre.cd_livello2 Is Null And cd_livello2 Is Null))
		and ((aClassPadre.cd_livello3 Is Not Null And cd_livello3 = aClassPadre.cd_livello3)
	             Or
	             (aClassPadre.cd_livello3 Is Null And cd_livello3 Is Null))
		and ((aClassPadre.cd_livello4 Is Not Null And cd_livello4 = aClassPadre.cd_livello4)
	             Or
	             (aClassPadre.cd_livello4 Is Null And cd_livello4 Is Null))
		and ((aClassPadre.cd_livello5 Is Not Null And cd_livello5 = aClassPadre.cd_livello5)
	             Or
	             (aClassPadre.cd_livello5 Is Null And cd_livello5 Is Null))
		and ((aClassPadre.cd_livello6 Is Not Null And cd_livello6 = aClassPadre.cd_livello6)
	             Or
	             (aClassPadre.cd_livello6 Is Null And cd_livello6 Is Null))
		and ((aClassPadre.cd_livello7 Is Not Null And cd_livello7 = aClassPadre.cd_livello7)
	             Or
	             (aClassPadre.cd_livello7 Is Null And cd_livello7 Is Null));
   	End Loop;
	End;

/* spostato in ribaltamento_altro rospuc
	-- Ribaltamento per la Gestione SIOPE
	-- Ribaltamento dei codici siope
	lock table CODICI_SIOPE in exclusive mode nowait;
	aMessage := 'Ribaltamento Codici SIOPE sull''esercizio '||aEsDest||'. Lock tabella CODICI_SIOPE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	For aCodSiope in (select * from CODICI_SIOPE
			  where esercizio = aEsOrig)
	Loop
		Begin
			insert into CODICI_SIOPE  (ESERCIZIO,
  						   TI_GESTIONE,
  						   CD_SIOPE,
  						   DESCRIZIONE,
  						   UTCR,
  						   DACR,
  						   UTUV,
  						   DUVA,
  						   PG_VER_REC)
			values (aEsDest,
  				aCodSiope.TI_GESTIONE,
  				aCodSiope.CD_SIOPE,
  				aCodSiope.DESCRIZIONE,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		Exception when dup_val_on_index then
			ibmutl200.logwar(aPgEsec,'Codice SIOPE con PK('||aEsDest||', '
							     	       ||aCodSiope.TI_GESTIONE||', '
								       ||aCodSiope.CD_SIOPE||') già inserito','','');
			aStato := 'W';
		End;
	End Loop;

	-- Ribaltamento delle associazioni fra tipologie istat e codici siope
	lock table ASS_TIPOLOGIA_ISTAT_SIOPE in exclusive mode nowait;
	aMessage := 'Ribaltamento Associazione Tipologia ISTAT - Codici SIOPE sull''esercizio '||aEsDest||'. Lock tabella ASS_TIPOLOGIA_ISTAT_SIOPE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	For aAssIstatSiope in (select * from ASS_TIPOLOGIA_ISTAT_SIOPE
			       where esercizio_siope = aEsOrig)
	Loop
		Begin
			insert into ASS_TIPOLOGIA_ISTAT_SIOPE  (PG_TIPOLOGIA,
								ESERCIZIO_SIOPE,
  						   		TI_GESTIONE_SIOPE,
  						   		CD_SIOPE,
  						   		UTCR,
  						   		DACR,
  						   		UTUV,
  						   		DUVA,
  						   		PG_VER_REC)
			values (aAssIstatSiope.PG_TIPOLOGIA,
				aEsDest,
  				aAssIstatSiope.TI_GESTIONE_SIOPE,
  				aAssIstatSiope.CD_SIOPE,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		Exception when dup_val_on_index then
			ibmutl200.logwar(aPgEsec,'Associazione Tipologia ISTAT - Codici SIOPE con PK('||aAssIstatSiope.PG_TIPOLOGIA||', '
							     	       				      ||aEsDest||', '
							     	                                      ||aAssIstatSiope.TI_GESTIONE_SIOPE||', '
								                                      ||aAssIstatSiope.CD_SIOPE||') già inserita','','');
			aStato := 'W';
		End;
	End Loop;

	-- Ribaltamento delle associazioni fra elementi voce e codici siope
	lock table ass_ev_siope in exclusive mode nowait;
	aMessage := 'Ribaltamento delle associazioni fra elementi voce e codici siope sull''esercizio '||aEsDest||'. Lock tabella ASS_EV_SIOPE';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	For aAssEVSiope in (select * from ASS_EV_SIOPE
			    where esercizio = aEsOrig) loop
		Begin
			insert into ASS_EV_SIOPE  (ESERCIZIO,
						   TI_APPARTENENZA,
						   TI_GESTIONE,
						   CD_ELEMENTO_VOCE,
						   ESERCIZIO_SIOPE,
						   TI_GESTIONE_SIOPE,
						   CD_SIOPE,
						   UTCR,
						   DACR,
						   UTUV,
						   DUVA,
						   PG_VER_REC)
			values (aEsDest,
				aAssEVSiope.TI_APPARTENENZA,
				aAssEVSiope.TI_GESTIONE,
				aAssEVSiope.CD_ELEMENTO_VOCE,
				aEsDest,
				aAssEVSiope.TI_GESTIONE_SIOPE,
				aAssEVSiope.CD_SIOPE,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_EV_SIOPE con PK('||aEsDest||', '
					||aAssEVSiope.TI_APPARTENENZA||', '
					||aAssEVSiope.TI_GESTIONE||', '
					||aAssEVSiope.CD_ELEMENTO_VOCE||', '
					||aEsDest||', '
					||aAssEVSiope.TI_GESTIONE_SIOPE||', '
					||aAssEVSiope.CD_SIOPE||') già esistente','','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento delle associazioni fra contributi/ritenute e codici siope
	lock table ASS_TIPO_CONTR_RITENUTA_SIOPE in exclusive mode nowait;
	aMessage := 'Ribaltamento Associazione Contributi/ritenute - Codici SIOPE sull''esercizio '||aEsDest||'. Lock tabella ASS_TIPO_CONTR_RITENUTA_SIOPE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	For aAssContrRitSiope in (Select * From ASS_TIPO_CONTR_RITENUTA_SIOPE
			          Where esercizio = aEsOrig)
	Loop
		Begin
			insert into ASS_TIPO_CONTR_RITENUTA_SIOPE  (ESERCIZIO,
								    CD_CONTRIBUTO_RITENUTA,
								    DT_INI_VALIDITA,
								    ESERCIZIO_SIOPE_S,
  						   		    TI_GESTIONE_SIOPE_S,
  						   		    CD_SIOPE_S,
  						   		    ESERCIZIO_SIOPE_E,
  						   		    TI_GESTIONE_SIOPE_E,
  						   		    CD_SIOPE_E,
  						   		    DACR,
  						   		    UTCR,
  						   		    DUVA,
  						   		    UTUV,
  						   		    PG_VER_REC)
			values (aEsDest,
				aAssContrRitSiope.CD_CONTRIBUTO_RITENUTA,
				aAssContrRitSiope.DT_INI_VALIDITA,
				aEsDest,
  				aAssContrRitSiope.TI_GESTIONE_SIOPE_S,
  				aAssContrRitSiope.CD_SIOPE_S,
  				aEsDest,
  				aAssContrRitSiope.TI_GESTIONE_SIOPE_E,
  				aAssContrRitSiope.CD_SIOPE_E,
				sysdate,
				cgUtente,
				sysdate,
				cgUtente,
				1);
		Exception when dup_val_on_index then
			ibmutl200.logwar(aPgEsec,'Associazione Contributi/ritenute - Codici SIOPE con PK('||aEsDest||', '
							     	       				      ||aAssContrRitSiope.CD_CONTRIBUTO_RITENUTA||', '
							     	                                      ||aAssContrRitSiope.DT_INI_VALIDITA||', '
							     	                                      ||aAssContrRitSiope.TI_GESTIONE_SIOPE_S||', '
							     	                                      ||aAssContrRitSiope.CD_SIOPE_S||', '
							     	                                      ||aAssContrRitSiope.TI_GESTIONE_SIOPE_E||', '
								                                      ||aAssContrRitSiope.CD_SIOPE_E||') già inserita','','');
			aStato := 'W';
		End;
	End Loop;
	*/ -- fine spostamento ribaltamento_altro rospuc
End;
----------------------------------------------------------------------------
procedure ribaltaStruttOrg(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
parent_key_not_found exception;
pragma exception_init(parent_key_not_found,-2291);
begin

	aMessage := 'Creazione dell''esercizio contabile '||aEsDest||' per i CDS e impostazione delle percentuali di copertura '||cnrutil.getLabelObbligazioniMin()||'. Lock tabelle ESERCIZIO, PRC_COPERTURA_OBBLIG, NUMERAZIONE_DOC_CONT.';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	-- ciclo sui cds validi nel corso dell'esercizio di destinazione
	for aCds in (select * from v_unita_organizzativa_valida
			 	 where fl_cds    = 'Y'
				   and esercizio = aEsDest
				   and cd_unita_organizzativa <> cnrctb020.getCDCDSENTE(aEsDest)) loop
		-- creazione dell'esercizio contabile per il cds
		begin
			 aMessage := 'Inserimento esercizio contabile per CdS: '||aCds.cd_unita_organizzativa;
			 ibmutl200.LOGINF(aPgEsec,aMessage,'','');
			 insert into esercizio (CD_CDS,
									ESERCIZIO,
									DS_ESERCIZIO,
									ST_APERTURA_CHIUSURA,
									DACR,
									UTCR,
									DUVA,
									UTUV,
									PG_VER_REC,
									IM_CASSA_INIZIALE)
			 values (aCds.cd_unita_organizzativa
			 	   	,aEsDest
				   	,'Esercizio contabile '|| aEsDest
				   	,'I'
				   	,sysdate
				   	,cgUtente
				   	,sysdate
				   	,cgUtente
				   	,1
				   	,0); --- Viene settata alla chiusura dell'esercizio
dbms_output.put_line('0010 + '||aCds.cd_unita_organizzativa);

			 -- esplosione del piano dei conti in funzione dell'esercizio contabile appena creato
--			 ibmutl200.LOGINF(aPgEsec,'cnrctb001.creaEsplVociEsercizio('||aEsDest||','||aCds.cd_unita_organizzativa||','||cgUtente||')','','');
			 cnrctb001.creaEsplVociEsercizio(aEsDest, aCds.cd_unita_organizzativa, cgUtente);

		exception
		when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Esercizio contabile '||aEsDest||' per il CDS '||aCds.cd_unita_organizzativa||' già definito','','');
			aStato := 'W';
		end;
		-- impostazione delle percentuali di copertura obbligazioni
		begin
--			 ibmutl200.LOGINF(aPgEsec,'Impostazione percentuali copertura per cds:'||aCds.cd_unita_organizzativa,'','');
			 insert into prc_copertura_obblig  (ESERCIZIO,
												CD_UNITA_ORGANIZZATIVA,
												PRC_COPERTURA_OBBLIG_2,
												PRC_COPERTURA_OBBLIG_3,
												DACR,
												UTCR,
												DUVA,
												UTUV,
												PG_VER_REC)
			 select  aEsDest
			 		,prc.CD_UNITA_ORGANIZZATIVA
					,prc.PRC_COPERTURA_OBBLIG_2
					,prc.PRC_COPERTURA_OBBLIG_3
					,sysdate
					,cgUtente
					,sysdate
					,cgUtente
					,1
			 from prc_copertura_obblig prc
			 where prc.esercizio 	   	  	  = aEsOrig
			   and prc.CD_UNITA_ORGANIZZATIVA = aCds.cd_unita_organizzativa;

			  dbms_output.put_line('0011');
exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Percentuali di copertura '||cnrutil.getLabelObbl()||' per il CDS '||aCds.cd_unita_organizzativa|| ' per l''esercizio '|| aEsDest||' già impostate','','');
			aStato := 'W';
		end;

		-- aggiornamento dei numeratori dei documenti contabili in NUMERAZIONE_DOC_CONT
--		ibmutl200.LOGINF(aPgEsec,'CNRCTB018.AGGIORNANUMERATORI('||aEsDest||','||aCds.cd_unita_organizzativa||','|| cgUtente||')','','');
		CNRCTB018.AGGIORNANUMERATORI(aEsDest, aCds.cd_unita_organizzativa, cgUtente);
			  dbms_output.put_line('0012');

	end loop; -- fine ciclo sui cds
dbms_output.put_line('0013');

	aMessage := 'Creazione dei parametri CDS per l''esercizio contabile '||aEsDest||' per i CDS validi. Lock tabella PARAMETRI_CDS.';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	-- ciclo sui cds validi nel corso dell'esercizio di destinazione
	for aCds in (select * from v_unita_organizzativa_valida
			 	 where fl_cds    = 'Y'
				   and esercizio = aEsDest) loop
	   -- creazione dei parametri per il cds
	   Declare
		FL_LINEA_E	varchar2(1);
  		CD_CDR_LINEA_E	varchar2(30);
  		CD_LINEA_E	varchar2(10);
  		FL_LINEA_S	varchar2(1);
  		CD_CDR_LINEA_S	varchar2(30);
  		CD_LINEA_S	varchar2(10);
  	   Begin
  	   	--se la linea non è valida, ripulisco i campi
  	   	select FL_LINEA_PGIRO_E_CDS,CD_CDR_LINEA_PGIRO_E,CD_LINEA_PGIRO_E,
  		       FL_LINEA_PGIRO_S_CDS,CD_CDR_LINEA_PGIRO_S,CD_LINEA_PGIRO_S
  		into FL_LINEA_E,CD_CDR_LINEA_E,CD_LINEA_E,FL_LINEA_S,CD_CDR_LINEA_S,CD_LINEA_S
  		from parametri_cds
  		where CD_CDS = aCds.cd_unita_organizzativa
	          and ESERCIZIO = aEsOrig;
dbms_output.put_line('0014');

		 If CD_CDR_LINEA_E Is Not Null Or CD_LINEA_E Is Not Null Then
			 Declare
			   e   VARCHAR2(1);
			 Begin
			   Select '1'
			   Into e
			   From linea_attivita
			   Where cd_centro_responsabilita = CD_CDR_LINEA_E
			     And cd_linea_attivita = CD_LINEA_E
			     And esercizio_fine >= aEsDest;
			 Exception
			    When No_Data_Found Then
			       FL_LINEA_E := 'N';
			       CD_CDR_LINEA_E := Null;
			       CD_LINEA_E := Null;
			 End;
		 End If;
		 If CD_CDR_LINEA_S Is Not Null Or CD_LINEA_S Is Not Null Then
			 Declare
			   s   VARCHAR2(1);
			 Begin
			   Select '1'
			   Into s
			   From linea_attivita
			   Where cd_centro_responsabilita = CD_CDR_LINEA_S
			     And cd_linea_attivita = CD_LINEA_S
			     And esercizio_fine >= aEsDest;
			 Exception
			    When No_Data_Found Then
			       FL_LINEA_S := 'N';
			       CD_CDR_LINEA_S := Null;
			       CD_LINEA_S := Null;
			 End;
		 End If;
dbms_output.put_line('0014');

		begin
			insert into parametri_cds (CD_CDS,
						   ESERCIZIO,
 						   FL_COMMESSA_OBBLIGATORIA,
						   DACR,
						   UTCR,
					 	   DUVA,
						   UTUV,
						   PG_VER_REC,
						   FL_PROGETTO_NUMERATORE,
						   PROGETTO_NUMERATORE_CIFRE,
						   IM_SOGLIA_CONTRATTO_S,
						   IM_SOGLIA_CONTRATTO_E,
						   FL_OBBLIGO_PROTOCOLLO_INF,
						   FL_CONTRATTO_CESSATO,
						   BLOCCO_IMPEGNI_CDR_GAE,
  						   FL_APPROVA_VAR_PDG,
  						   FL_RIBALTATO,
  						   IM_SOGLIA_RIBALTAMENTO_RES_IMP,
  						   FL_APPROVA_VAR_STANZ_RES,
  						   IM_SOGLIA_CONSUMO_RESIDUO,
  						   FL_BLOCCO_ASS_IMP_IVA,
  						   FL_RIPORTA_AVANTI,
						   FL_RIPORTA_INDIETRO,
  						   FL_LINEA_PGIRO_E_CDS,
  						   CD_CDR_LINEA_PGIRO_E,
  						   CD_LINEA_PGIRO_E,
  						   FL_LINEA_PGIRO_S_CDS,
  						   CD_CDR_LINEA_PGIRO_S,
  						   CD_LINEA_PGIRO_S,
  						   FL_MOD_OBBL_RES,
  						   FL_BLOCCO_IBAN,
  						   FL_KIT_FIRMA_DIGITALE,
  						   CD_DIPARTIMENTO,
  						   FL_RIACCERTAMENTO,
  						   FL_RIOBBLIGAZIONE,
  						   FL_BLOCCO_IMPEGNI_NATFIN,
                           ABIL_PROGETTO_STRORG)
			 select aCds.cd_unita_organizzativa,
			 	aEsDest,
			 	FL_COMMESSA_OBBLIGATORIA,
			 	sysdate,
				cgUtente,
				sysdate,
				cgUtente,
				1,
				FL_PROGETTO_NUMERATORE,
				0,
				IM_SOGLIA_CONTRATTO_S,
				IM_SOGLIA_CONTRATTO_E,
				FL_OBBLIGO_PROTOCOLLO_INF,
				FL_CONTRATTO_CESSATO,
				BLOCCO_IMPEGNI_CDR_GAE,
  				FL_APPROVA_VAR_PDG,
  				FL_RIBALTATO,
  				IM_SOGLIA_RIBALTAMENTO_RES_IMP,
  				FL_APPROVA_VAR_STANZ_RES,
  				IM_SOGLIA_CONSUMO_RESIDUO,
  				FL_BLOCCO_ASS_IMP_IVA,
  				FL_RIPORTA_AVANTI,
				FL_RIPORTA_INDIETRO,
  				FL_LINEA_E,
  				CD_CDR_LINEA_E,
  				CD_LINEA_E,
  				FL_LINEA_S,
  				CD_CDR_LINEA_S,
  				CD_LINEA_S,
  				FL_MOD_OBBL_RES,
  				FL_BLOCCO_IBAN,
  				FL_KIT_FIRMA_DIGITALE,
  				cd_dipartimento,
  				FL_RIACCERTAMENTO,
  				FL_RIOBBLIGAZIONE,
  				FL_BLOCCO_IMPEGNI_NATFIN,
                ABIL_PROGETTO_STRORG
			 from parametri_cds
			 where CD_CDS = aCds.cd_unita_organizzativa
			   and ESERCIZIO = aEsOrig;
			  dbms_output.put_line('0015');

		exception
		when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Parametri CDS per l''esercizio contabile '||aEsDest||' e per il CDS '||aCds.cd_unita_organizzativa||' già definiti','','');
			aStato := 'W';
		end;
	   Exception
	      When No_Data_Found Then
			ibmutl200.LOGWAR(aPgEsec,'Parametri CDS per l''esercizio contabile '||aEsDest||' e per il CDS '||aCds.cd_unita_organizzativa||' non presenti nell''Esercizio di origine','','');
			aStato := 'W';
	   End;
	end loop;

	aMessage := 'Creazione della tabella CHIUSURA_COEP per l''esercizio contabile '||aEsDest||' per i CDS validi. Lock tabella CHIUSURA_COEP.';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	-- ciclo sui cds validi nel corso dell'esercizio di destinazione
	for aCds in (select * from v_unita_organizzativa_valida
			 	 where fl_cds    = 'Y'
				   and esercizio = aEsDest) loop
	   -- creazione dei parametri di chiusura coep per il cds
	   Begin
			insert into chiusura_coep (CD_CDS,
  						   ESERCIZIO,
						   STATO,
						   DACR,
						   UTCR,
						   DUVA,
						   UTUV,
						   PG_VER_REC)
			 select aCds.cd_unita_organizzativa,
			 	aEsDest,
			 	'A',
			 	sysdate,
				cgUtente,
				sysdate,
				cgUtente,
				1
			 from chiusura_coep
			 where CD_CDS = aCds.cd_unita_organizzativa
			   and ESERCIZIO = aEsOrig;
		exception
		when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Parametri di Chiusura coep per l''esercizio contabile '||aEsDest||' e per il CDS '||aCds.cd_unita_organizzativa||' già definiti','','');
			aStato := 'W';
		end;
	end loop;

	aMessage := 'Ribaltamento tabella di identificazione dei collegamenti tra conti di partita di giro su '||aEsDest||'. Lock tabella ASS_PARTITA_GIRO.';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aAssPgiro in (select * from ASS_PARTITA_GIRO
				  	  where esercizio = aEsOrig) loop
		begin
			insert into ASS_PARTITA_GIRO (ESERCIZIO,
										  TI_APPARTENENZA,
										  TI_GESTIONE,
										  CD_VOCE,
										  TI_APPARTENENZA_CLG,
										  TI_GESTIONE_CLG,
										  CD_VOCE_CLG,
										  DACR,
										  UTCR,
										  DUVA,
										  UTUV,
										  PG_VER_REC)
			values (aEsDest,
				    aAssPgiro.TI_APPARTENENZA,
				    aAssPgiro.TI_GESTIONE,
				    aAssPgiro.CD_VOCE,
				    aAssPgiro.TI_APPARTENENZA_CLG,
				    aAssPgiro.TI_GESTIONE_CLG,
				    aAssPgiro.CD_VOCE_CLG,
					sysdate,
					cgUtente,
					sysdate,
					cgUtente,
					1);
		exception
		when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_PARTITA_GIRO con PK( '||aEsDest||', '
									||aAssPgiro.TI_APPARTENENZA||', '
									||aAssPgiro.TI_GESTIONE||', '
									||aAssPgiro.CD_VOCE||') già esistente','','');
			aStato := 'W';
		when parent_key_not_found then
			ibmutl200.LOGWAR(aPgEsec,'Chiave padre non trovata per ASS_PARTITA_GIRO con PK('||aEsDest||', '
									||aAssPgiro.TI_APPARTENENZA||', '
									||aAssPgiro.TI_GESTIONE||', '
									||aAssPgiro.CD_VOCE||'). '||SQLERRM,'','');
			aStato := 'W';
		end;
	end loop; -- fine ciclo sulle associazioni

	-- Ribaltamento elenco dei CDS che sono gestiti in versamento via interfaccia
	aMessage := 'Ribaltamento elenco dei CDS che sono gestiti in versamento via interfaccia sull''esercizio '||aEsDest||'. Lock tabella LIQUID_CORI_INTERF_CDS';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aInterf in (select * from LIQUID_CORI_INTERF_CDS
				    where esercizio = aEsOrig
				      and cd_cds in (select cd_unita_organizzativa
						     from v_unita_organizzativa_valida
						     where fl_cds    = 'Y'
						      and esercizio = aEsDest)) loop
		begin
			insert into LIQUID_CORI_INTERF_CDS (CD_CDS,
												ESERCIZIO,
												DACR,
												UTCR,
												DUVA,
												UTUV,
												PG_VER_REC,
												TIPO)
			values (aInterf.cd_cds,
				    aEsDest,
					sysdate,
					cgUtente,
					sysdate,
				  	cgUtente,
					1,
					aInterf.tipo);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Cds '||aInterf.cd_cds||' già esistente per l''esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento elenco dei CDS che in un determinato esercizio utilizzano l'interfaccia di generazione della liquidazione IVA
	aMessage := 'Ribaltamento elenco dei CDS che sono gestiti in versamento via interfaccia sull''esercizio '||aEsDest||'. Lock tabella LIQUID_IVA_INTERF_CDS';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aInterf in (select * from LIQUID_IVA_INTERF_CDS
				    where esercizio = aEsOrig
				      and cd_cds in (select cd_unita_organizzativa
						     from v_unita_organizzativa_valida
						     where fl_cds    = 'Y'
						      and esercizio = aEsDest)) loop
		begin
			insert into LIQUID_IVA_INTERF_CDS  (CD_CDS,
												ESERCIZIO,
												DACR,
												UTCR,
												DUVA,
												UTUV,
												PG_VER_REC)
			values (aInterf.cd_cds,
				    aEsDest,
					sysdate,
					cgUtente,
					sysdate,
				  	cgUtente,
					1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Cds '||aInterf.cd_cds||' già esistente per l''esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	end loop;
	-- Ribaltamento UO che hanno una gestione dell'IVA esterna
	aMessage := 'Ribaltamento UO che hanno una gestione dell''IVA esterna sull''esercizio '||aEsDest||'. Lock tabella LIQUID_IVA_ESTERNA';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aEsterna in (select * from LIQUID_IVA_ESTERNA
				           where esercizio = aEsOrig
				             and cd_unita_organizzativa in (select cd_unita_organizzativa
						                                        from v_unita_organizzativa_valida
						                                        where esercizio = aEsDest)) loop
		begin
			insert into LIQUID_IVA_ESTERNA  (ESERCIZIO,
			                                 CD_UNITA_ORGANIZZATIVA,
			                                 TIPO,
												               DACR,
												               UTCR,
												               DUVA,
												               UTUV,
												               PG_VER_REC)
			values (aEsDest,
			        aEsterna.cd_unita_organizzativa,
				      aEsterna.tipo,
					    sysdate,
					    cgUtente,
					    sysdate,
				  	  cgUtente,
					    1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'UO '||aEsterna.cd_unita_organizzativa||' già esistente per l''esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento Dipartimento_peso
	aMessage := 'Ribaltamento Dipartimento peso sull''esercizio '||aEsDest||'. Lock tabella Dipartimento_peso';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aDati in (select * from Dipartimento_peso
				           where esercizio = aEsOrig) loop
		begin
			insert into Dipartimento_peso   (ESERCIZIO,
			                                 CD_DIPARTIMENTO,
			                                 DS_DIPARTIMENTO,
			                                 PESO,
												               DACR,
												               UTCR,
												               DUVA,
												               UTUV,
												               PG_VER_REC)
			values (aEsDest,
			        aDati.CD_DIPARTIMENTO,
				      aDati.Ds_DIPARTIMENTO,
				      aDati.peso,
					    sysdate,
					    cgUtente,
					    sysdate,
				  	  cgUtente,
					    1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Dipartimento peso già esistente per l''esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	end loop;

	--Ribaltamento ASS_UO_AREA
	aMessage := 'Creazione di ASS_UO_AREA per l''esercizio contabile '||aEsDest||' per le UO valide. Lock tabella ASS_UO_AREA.';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');

	for aDati In(Select * From  ASS_UO_AREA
		     Where esercizio = aEsOrig
		     And cd_unita_organizzativa In (select cd_unita_organizzativa
		      				    from v_unita_organizzativa_valida
			 	 		    where fl_cds    = 'N'
				   		    and esercizio = aEsDest)) Loop
		-- creazione dell'associazione
		begin
			insert into ASS_UO_AREA (ESERCIZIO,
  						 CD_UNITA_ORGANIZZATIVA,
  						 CD_AREA_RICERCA,
  						 FL_PRESIDENTE_AREA,
  					         UTCR,
  						 DACR,
  						 UTUV,
  						 DUVA,
  						 PG_VER_REC)
			values (aEsDest,
				aDati.CD_UNITA_ORGANIZZATIVA,
  				aDati.CD_AREA_RICERCA,
  				aDati.FL_PRESIDENTE_AREA,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		exception
		when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Associazione UO-AREA per l''esercizio contabile '||aEsDest||' e per la UO '||aDati.cd_unita_organizzativa||' già definiti','','');
			aStato := 'W';
		end;
	end loop;
	--Ribaltamento ASS_DIPARTIMENTO_AREA
	aMessage := 'Creazione di ASS_DIPARTIMENTO_AREA per l''esercizio contabile '||aEsDest||' per le Aree valide. Lock tabella ASS_DIPARTIMENTO_AREA.';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');

	for aDati In(Select * From  ASS_DIPARTIMENTO_AREA
		     Where esercizio = aEsOrig
		     And cd_cds_area In (select cd_unita_organizzativa
		      				    from v_unita_organizzativa_valida
			 	 		    where esercizio = aEsDest)) Loop
		-- creazione dell'associazione
		begin
			insert into ASS_DIPARTIMENTO_AREA (ESERCIZIO,
  						 	   CD_DIPARTIMENTO,
  						 	   CD_CDS_AREA,
  					         	   UTCR,
  						 	   DACR,
  						 	   UTUV,
  						 	   DUVA,
  						 	   PG_VER_REC)
			values (aEsDest,
				aDati.CD_DIPARTIMENTO,
  				aDati.CD_CDS_AREA,
				cgUtente,
				sysdate,
				cgUtente,
				sysdate,
				1);
		exception
		when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Associazione DIPARTIMENTO_AREA per l''esercizio contabile '||aEsDest||' e per il dipartimento '||aDati.CD_DIPARTIMENTO||' già definita','','');
			aStato := 'W';
		end;
	end loop;
end;
----------------------------------------------------------------------------
procedure ribaltaEP(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
begin

	-- Ribaltamento conti E/P
	aMessage := 'Ribaltamento del piano dei conti E/P sull''esercizio '||aEsDest||'. Lock tabella VOCE_EP';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aVoceEP in (select * from VOCE_EP
				    where esercizio = aEsOrig
					order by cd_voce_ep) loop  -- order per RK_VOCE_EP00
		begin
			insert into VOCE_EP (ESERCIZIO,
								 CD_VOCE_EP,
								 NATURA_VOCE,
								 TI_VOCE_EP,
								 DS_VOCE_EP,
								 LIVELLO,
								 CD_PROPRIO_VOCE_EP,
								 FL_MASTRINO,
								 TI_SEZIONE,
								 RIEPILOGA_A,
								 RIAPRE_A_CONTO_ECONOMICO,
								 FL_A_PAREGGIO,
								 CONTO_SPECIALE,
								 DUVA,
								 UTUV,
								 DACR,
								 UTCR,
								 PG_VER_REC,
								 CD_VOCE_EP_PADRE,
								 id_classificazione)
			values (aEsDest,
					aVoceEP.CD_VOCE_EP,
					aVoceEP.NATURA_VOCE,
					aVoceEP.TI_VOCE_EP,
					aVoceEP.DS_VOCE_EP,
					aVoceEP.LIVELLO,
					aVoceEP.CD_PROPRIO_VOCE_EP,
					aVoceEP.FL_MASTRINO,
					aVoceEP.TI_SEZIONE,
					aVoceEP.RIEPILOGA_A,
					aVoceEP.RIAPRE_A_CONTO_ECONOMICO,
					aVoceEP.FL_A_PAREGGIO,
					aVoceEP.CONTO_SPECIALE,
					sysdate,
					cgUtente,
					sysdate,
					cgUtente,
					1,
					aVoceEP.CD_VOCE_EP_PADRE,
					NULL);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Voce E/P '||aVoceEP.CD_VOCE_EP||' già esistente per l''esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento  delle associazioni entrate-ricavi spese-costi
	aMessage := 'Ribaltamento  delle associazioni entrate-ricavi spese-costi sull''esercizio '||aEsDest||'. Lock tabella ASS_EV_VOCEEP';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aAssEVEP in (select * from ASS_EV_VOCEEP
			 	 where esercizio = aEsOrig) loop
		begin
			 insert into ASS_EV_VOCEEP (ESERCIZIO,
										TI_APPARTENENZA,
										TI_GESTIONE,
										CD_ELEMENTO_VOCE,
										CD_VOCE_EP,
										UTUV,
										DACR,
										UTCR,
										PG_VER_REC,
										DUVA,
										CD_VOCE_EP_CONTR)
			 values (aEsDest,
			 		 aAssEVEP.TI_APPARTENENZA,
					 aAssEVEP.TI_GESTIONE,
					 aAssEVEP.CD_ELEMENTO_VOCE,
					 aAssEVEP.CD_VOCE_EP,
					 cgUtente,
					 sysdate,
					 cgUtente,
					 1,
					 sysdate,
					 aAssEVEP.CD_VOCE_EP_CONTR);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_EV_VOCEEP con PK ('||aEsDest||', '
															 ||aAssEVEP.TI_APPARTENENZA||', '
															 ||aAssEVEP.TI_GESTIONE||', '
															 ||aAssEVEP.CD_ELEMENTO_VOCE||', '
															 ||aAssEVEP.CD_VOCE_EP||') già inserita','','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento Parametri Livelli
        Begin
	   aMessage := 'Ribaltamento Parametri Livelli EP sull''esercizio '||aEsDest||'. Lock tabella PARAMETRI_LIVELLI_EP';
	   ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	   insert into PARAMETRI_LIVELLI_EP (ESERCIZIO,LIVELLI_ECO,
	   				  LUNG_LIVELLO1E,DS_LIVELLO1E,
	   				  LUNG_LIVELLO2E,DS_LIVELLO2E,
	   				  LUNG_LIVELLO3E,DS_LIVELLO3E,
	   				  LUNG_LIVELLO4E,DS_LIVELLO4E,
	   				  LUNG_LIVELLO5E,DS_LIVELLO5E,
	   				  LUNG_LIVELLO6E,DS_LIVELLO6E,
	   				  LUNG_LIVELLO7E,DS_LIVELLO7E,
	   				  LUNG_LIVELLO8E,DS_LIVELLO8E,
	   				  LIVELLI_PAT,
	   				  LUNG_LIVELLO1P,DS_LIVELLO1P,
	   				  LUNG_LIVELLO2P,DS_LIVELLO2P,
	   				  LUNG_LIVELLO3P,DS_LIVELLO3P,
	   				  LUNG_LIVELLO4P,DS_LIVELLO4P,
	   				  LUNG_LIVELLO5P,DS_LIVELLO5P,
	   				  LUNG_LIVELLO6P,DS_LIVELLO6P,
	   				  LUNG_LIVELLO7P,DS_LIVELLO7P,
	   				  LUNG_LIVELLO8P,DS_LIVELLO8P,
				      DACR,UTCR,DUVA,UTUV,PG_VER_REC)
		select aEsDest, LIVELLI_ECO,
	   	  LUNG_LIVELLO1E,DS_LIVELLO1E,
	   	  LUNG_LIVELLO2E,DS_LIVELLO2E,
	   	  LUNG_LIVELLO3E,DS_LIVELLO3E,
	   	  LUNG_LIVELLO4E,DS_LIVELLO4E,
	   	  LUNG_LIVELLO5E,DS_LIVELLO5E,
	   	  LUNG_LIVELLO6E,DS_LIVELLO6E,
	   	  LUNG_LIVELLO7E,DS_LIVELLO7E,
	   	  LUNG_LIVELLO8E,DS_LIVELLO8E,
	   	  LIVELLI_PAT,
	   	  LUNG_LIVELLO1P,DS_LIVELLO1P,
	   	  LUNG_LIVELLO2P,DS_LIVELLO2P,
	   	  LUNG_LIVELLO3P,DS_LIVELLO3P,
	   	  LUNG_LIVELLO4P,DS_LIVELLO4P,
	   	  LUNG_LIVELLO5P,DS_LIVELLO5P,
	   	  LUNG_LIVELLO6P,DS_LIVELLO6P,
	   	  LUNG_LIVELLO7P,DS_LIVELLO7P,
	   	  LUNG_LIVELLO8P,DS_LIVELLO8P,
	   	  Sysdate,cgUtente,sysdate,cgUtente,1
		from PARAMETRI_LIVELLI_EP
		where ESERCIZIO = aEsOrig;
	Exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Parametri Livelli EP per l''esercizio base '||aEsDest||' già esistenti','','');
			aStato := 'W';
	End;

	-- Ribaltamento classificazione voci ep
	declare
	   new_id NUMBER;
	   new_id_padre NUMBER:=Null;
	begin
	lock table classificazione_voci_ep in exclusive mode nowait;
	aMessage := 'Ribaltamento Classificazione Voci ep sull''esercizio '||aEsDest||'. Lock tabella CLASSIFICAZIONE_VOCI_EP';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');

	Select Max(id_classificazione)
	Into new_id
	From CLASSIFICAZIONE_VOCI_EP;

	  for aClassV in (select * from CLASSIFICAZIONE_VOCI_EP
			where esercizio = aEsOrig
			order by esercizio, tipo, cd_livello1 Desc, cd_livello2 Desc ,cd_livello3 Desc ,
			cd_livello4 Desc, cd_livello5 Desc, cd_livello6 Desc, cd_livello7 Desc, cd_livello8 Desc)
	  loop
		begin
			new_id := new_id + 1;
			insert into CLASSIFICAZIONE_VOCI_EP (ID_CLASSIFICAZIONE,
							  ESERCIZIO,
							  TIPO,
							  DS_CLASSIFICAZIONE,
							  CD_LIVELLO1,
							  CD_LIVELLO2,
							  CD_LIVELLO3,
							  CD_LIVELLO4,
							  CD_LIVELLO5,
							  CD_LIVELLO6,
							  CD_LIVELLO7,
							  CD_LIVELLO8,
							  ID_CLASS_PADRE,
							  FL_MASTRINO,
							  DUVA,
							  UTUV,
							  DACR,
							  UTCR,
							  PG_VER_REC)
			values (new_id,
				aEsDest,
				aClassV.TIPO,
				aClassV.DS_CLASSIFICAZIONE,
				aClassV.CD_LIVELLO1,
				aClassV.CD_LIVELLO2,
				aClassV.CD_LIVELLO3,
				aClassV.CD_LIVELLO4,
				aClassV.CD_LIVELLO5,
				aClassV.CD_LIVELLO6,
				aClassV.CD_LIVELLO7,
				aClassV.CD_LIVELLO8,
				null,
				aClassV.FL_MASTRINO,
				sysdate,
				cgUtente,
				sysdate,
				cgutente,
				1);

			--Aggiorno l'elemento voce con la nuova classificazione creata
			update VOCE_EP v
			set v.id_classificazione =  new_id
			where v.esercizio = aEsDest
			and (v.cd_voce_ep) In
			     (Select a.cd_voce_ep
			     From voce_ep a
			     Where a.esercizio = aEsOrig
			       And a.id_classificazione = aClassV.id_classificazione);
		exception
		   when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CLASSIFICAZIONE_VOCI_EP con PK ('||new_id||') già esistente','','');
			aStato := 'W';
		   when OTHERS then
			ibmutl200.LOGWAR(aPgEsec,'SK_CLASSIFICAZIONE_VOCI_EP violata per la classificazione ('||new_id||')','','');
			aStato := 'W';
		end;
	  end loop;
	end;

	-- Aggiornamento id_class_padre nel nuovo anno
	Declare
    	    rec_class 	CLASSIFICAZIONE_VOCI_EP%Rowtype;
    	    new_padre   NUMBER;
	Begin
	   for aClassPadre in (select * from CLASSIFICAZIONE_VOCI_EP
		    where esercizio = aEsOrig
		    And id_class_padre Is Not Null)
  	   loop
  		select *
		into rec_class
		From CLASSIFICAZIONE_VOCI_EP
		where esercizio = aEsOrig
		and id_classificazione = aClassPadre.id_class_padre;

		select id_classificazione
		into new_padre
		from CLASSIFICAZIONE_VOCI_EP
		where esercizio = aEsDest
		and tipo = rec_class.tipo
		and cd_livello1 = rec_class.cd_livello1
		and ((rec_class.cd_livello2 Is Not Null And cd_livello2 = rec_class.cd_livello2)
	      	      Or
	      	     (rec_class.cd_livello2 Is Null And cd_livello2 Is Null))
		and ((rec_class.cd_livello3 Is Not Null And cd_livello3 = rec_class.cd_livello3)
	              Or
	             (rec_class.cd_livello3 Is Null And cd_livello3 Is Null))
		and ((rec_class.cd_livello4 Is Not Null And cd_livello4 = rec_class.cd_livello4)
	              Or
	             (rec_class.cd_livello4 Is Null And cd_livello4 Is Null))
		and ((rec_class.cd_livello5 Is Not Null And cd_livello5 = rec_class.cd_livello5)
	              Or
	             (rec_class.cd_livello5 Is Null And cd_livello5 Is Null))
		and ((rec_class.cd_livello6 Is Not Null And cd_livello6 = rec_class.cd_livello6)
	              Or
	             (rec_class.cd_livello6 Is Null And cd_livello6 Is Null))
		and ((rec_class.cd_livello7 Is Not Null And cd_livello7 = rec_class.cd_livello7)
	              Or
	             (rec_class.cd_livello7 Is Null And cd_livello7 Is Null))
	   and ((rec_class.cd_livello8 Is Not Null And cd_livello8 = rec_class.cd_livello8)
	              Or
	             (rec_class.cd_livello8 Is Null And cd_livello8 Is Null));

		update CLASSIFICAZIONE_VOCI_EP
		set id_class_padre = new_padre
		where esercizio = aEsDest
		and tipo = aClassPadre.tipo
		and cd_livello1 = aClassPadre.cd_livello1
		and ((aClassPadre.cd_livello2 Is Not Null And cd_livello2 = aClassPadre.cd_livello2)
	             Or
	             (aClassPadre.cd_livello2 Is Null And cd_livello2 Is Null))
		and ((aClassPadre.cd_livello3 Is Not Null And cd_livello3 = aClassPadre.cd_livello3)
	             Or
	             (aClassPadre.cd_livello3 Is Null And cd_livello3 Is Null))
		and ((aClassPadre.cd_livello4 Is Not Null And cd_livello4 = aClassPadre.cd_livello4)
	             Or
	             (aClassPadre.cd_livello4 Is Null And cd_livello4 Is Null))
		and ((aClassPadre.cd_livello5 Is Not Null And cd_livello5 = aClassPadre.cd_livello5)
	             Or
	             (aClassPadre.cd_livello5 Is Null And cd_livello5 Is Null))
		and ((aClassPadre.cd_livello6 Is Not Null And cd_livello6 = aClassPadre.cd_livello6)
	             Or
	             (aClassPadre.cd_livello6 Is Null And cd_livello6 Is Null))
		and ((aClassPadre.cd_livello7 Is Not Null And cd_livello7 = aClassPadre.cd_livello7)
	             Or
	             (aClassPadre.cd_livello7 Is Null And cd_livello7 Is Null))
	  and ((aClassPadre.cd_livello8 Is Not Null And cd_livello8 = aClassPadre.cd_livello8)
	             Or
	             (aClassPadre.cd_livello8 Is Null And cd_livello8 Is Null));
   	End Loop;
	End;

/*
	-- Ribaltamento associazione tra tipo di soggetto anagrafico e voce economica
	lock table ass_anag_voce_ep in exclusive mode nowait;
	aMessage := 'Ribaltamento  delle associazione tra tipo di soggetto anagrafico e voce economica sull''esercizio '||aEsDest||'. Lock tabella ASS_ANAG_VOCE_EP';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aAssAnagEP in (select * from ASS_ANAG_VOCE_EP
			 	   	   where esercizio = aEsOrig) loop
		begin
			 insert into ASS_ANAG_VOCE_EP  (ESERCIZIO,
											TI_TERZO,
											ITALIANO_ESTERO,
											TI_ENTITA,
											ENTE_ALTRO,
											CD_CLASSIFIC_ANAG,
											CD_VOCE_EP,
											UTCR,
											DACR,
											UTUV,
											DUVA,
											PG_VER_REC)
			 values (aEsDest,
			 		 aAssAnagEP.TI_TERZO,
					 aAssAnagEP.ITALIANO_ESTERO,
					 aAssAnagEP.TI_ENTITA,
					 aAssAnagEP.ENTE_ALTRO,
					 aAssAnagEP.CD_CLASSIFIC_ANAG,
					 aAssAnagEP.CD_VOCE_EP,
					 cgUtente,
					 sysdate,
					 cgUtente,
					 sysdate,
					 1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_ANAG_VOCE_EP con PK (' ||aEsDest||', '
																 ||aAssAnagEP.TI_TERZO||', '
																 ||aAssAnagEP.ITALIANO_ESTERO||', '
																 ||aAssAnagEP.TI_ENTITA||', '
																 ||aAssAnagEP.ENTE_ALTRO||','
																 ||aAssAnagEP.CD_CLASSIFIC_ANAG||') già inserita','','');
			aStato := 'W';
		end;
	end loop;
*/
	-- Ribaltamento associazione fra categoria inventariale e voce del piano economico
	aMessage := 'Ribaltamento associazione fra categoria inventariale e voce del piano economico';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aAssCatVoceEp in (select * from categoria_gruppo_voce_ep
					  	  where esercizio = aEsOrig) loop
		begin
			 insert into CATEGORIA_GRUPPO_VOCE_EP (CD_CATEGORIA_GRUPPO,
													ESERCIZIO,
													SEZIONE,
													CD_VOCE_EP,
													CD_VOCE_EP_CONTR,
													DACR,
													UTCR,
													DUVA,
													UTUV,
													PG_VER_REC,
													TI_APPARTENENZA,
    											TI_GESTIONE,
    											CD_ELEMENTO_VOCE,
    											CD_VOCE_EP_PLUS,
    											CD_VOCE_EP_MINUS,
    											FL_DEFAULT)
			 values (aAssCatVoceEp.CD_CATEGORIA_GRUPPO,
			 		 aEsDest,
					 aAssCatVoceEp.SEZIONE,
					 aAssCatVoceEp.CD_VOCE_EP,
					 aAssCatVoceEp.CD_VOCE_EP_CONTR,
					 sysdate,
					 cgUtente,
					 sysdate,
					 cgUtente,
					 1,
					 aAssCatVoceEp.TI_APPARTENENZA,
					 aAssCatVoceEp.TI_GESTIONE,
					 aAssCatVoceEp.CD_ELEMENTO_VOCE,
					 aAssCatVoceEp.CD_VOCE_EP_PLUS,
					 aAssCatVoceEp.CD_VOCE_EP_MINUS,
					 aAssCatVoceEp.FL_DEFAULT);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CATEGORIA_GRUPPO_VOCE_EP con PK (' ||aAssCatVoceEp.CD_CATEGORIA_GRUPPO||', '
																 ||aEsDest||', '
																 ||aAssCatVoceEp.SEZIONE||') già inserita','','');
			aStato := 'W';
		end;
	end loop;

		-- Ribaltamento associazione CNR_ASS_CONTO_GRUPPO_EP
	aMessage := 'Ribaltamento associazione CNR_ASS_CONTO_GRUPPO_EP';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aAssContoGruppoEp in (select * from CNR_ASS_CONTO_GRUPPO_EP
					  	  where esercizio = aEsOrig) loop
		begin
			 insert into CNR_ASS_CONTO_GRUPPO_EP (ESERCIZIO,
                            CD_PIANO_GRUPPI,
                            CD_GRUPPO_EP,
                            CD_VOCE_EP,
                            SEZIONE,
                            DS_ASSOCIAZIONE,
                            SEGNO,
                            DUVA,
                            UTUV,
                            DACR,
                            UTCR,
                            PG_VER_REC)
			 values (aEsDest,
					     aAssContoGruppoEp.CD_PIANO_GRUPPI,
               aAssContoGruppoEp.CD_GRUPPO_EP,
               aAssContoGruppoEp.CD_VOCE_EP,
               aAssContoGruppoEp.SEZIONE,
               aAssContoGruppoEp.DS_ASSOCIAZIONE,
               aAssContoGruppoEp.SEGNO,
               sysdate,
					     cgUtente,
					     sysdate,
					     cgUtente,
					     1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CNR_ASS_CONTO_GRUPPO_EP con PK (' ||aEsDest||', '
																 ||aAssContoGruppoEp.CD_PIANO_GRUPPI||', '
																 ||aAssContoGruppoEp.CD_GRUPPO_EP||', '
																 ||aAssContoGruppoEp.CD_VOCE_EP||') già inserita','','');
			aStato := 'W';
		end;
	end loop;

end;
----------------------------------------------------------------------------
procedure ribaltaCORI_altro(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
uoEnte unita_organizzativa%rowtype;
begin
	-- Ribaltamento record con Pk ('999',aEsOrig,'999.0000',0) in LIQUID_CORI
	uoEnte:=CNRCTB020.GETUOENTE(aEsDest);
	aMessage := 'Ribaltamento record con Pk ('||cnrctb020.getCDCDSENTE(aEsDest)||','||aEsOrig||','||uoEnte.cd_unita_organizzativa||',0) in LIQUID_CORI sull''esercizio '||aEsDest||'. Lock tabella LIQUID_CORI';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	begin
		 insert into LIQUID_CORI (CD_CDS,
					ESERCIZIO,
					CD_UNITA_ORGANIZZATIVA,
					PG_LIQUIDAZIONE,
					DT_DA,
					DT_A,
					STATO,
					DACR,
					UTCR,
					DUVA,
					UTUV,
					PG_VER_REC,
					DA_ESERCIZIO_PRECEDENTE)
		 select lc.CD_CDS,
		 	aEsDest,
			lc.CD_UNITA_ORGANIZZATIVA,
			lc.PG_LIQUIDAZIONE,
			IBMUTL001.getAddMonth(dt_da,12),
			IBMUTL001.getAddMonth(dt_a,12),
			lc.STATO,
			sysdate,
			cgUtente,
			sysdate,
			cgUtente,
			1,
			lc.DA_ESERCIZIO_PRECEDENTE
		 from liquid_cori lc
		 where lc.CD_CDS  		 = cnrctb020.getCDCDSENTE(aEsDest)
		   and lc.ESERCIZIO 		 = aEsOrig
		   and lc.CD_UNITA_ORGANIZZATIVA = uoEnte.cd_unita_organizzativa
		   and lc.PG_LIQUIDAZIONE 	 = 0;
	exception when DUP_VAL_ON_INDEX then
		ibmutl200.LOGWAR(aPgEsec,'LIQUID_CORI con PK('||cnrctb020.getCDCDSENTE(aEsDest)||','||aEsOrig||','||uoEnte.cd_unita_organizzativa||',0) già inserita','','');
		aStato := 'W';
	end;


	-- Ribaltamento definizioni gruppi CORI
	aMessage := 'Ribaltamento definizioni gruppi CORI sull''esercizio '||aEsDest||'. Lock tabella GRUPPO_CR';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aGrCR in (select * from GRUPPO_CR
			       where esercizio = aEsOrig) loop
		begin
			 insert into GRUPPO_CR (ESERCIZIO,
						CD_GRUPPO_CR,
						DS_GRUPPO_CR,
						FL_ACCENTRATO,
						DACR,
						UTCR,
						DUVA,
						UTUV,
						PG_VER_REC,
						FL_ANNO_PREC,
						FL_F24ONLINE,
						FL_F24ONLINE_PREVID,
						CD_TRIBUTO_ERARIO,
						CD_TIPO_RIGA_F24,
						CD_MATRICOLA_INPS)
			 values (aEsDest,
			 	aGrCR.CD_GRUPPO_CR,
			 	aGrCR.DS_GRUPPO_CR,
			 	aGrCR.FL_ACCENTRATO,
				sysdate,
				cgUtente,
				sysdate,
				cgUtente,
				1,
				aGrCR.FL_ANNO_PREC,
				aGrCR.FL_F24ONLINE,
				aGrCR.FL_F24ONLINE_PREVID,
				aGrCR.CD_TRIBUTO_ERARIO,
				aGrCR.CD_TIPO_RIGA_F24,
				aGrCR.CD_MATRICOLA_INPS);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Gruppo CORI '||aGrCR.CD_GRUPPO_CR||' già inserito per l''esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento dettagli gruppo CORI
	aMessage := 'Ribaltamento dettagli gruppo CORI sull''esercizio '||aEsDest||'. Lock tabella GRUPPO_CR_DET';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for	aGrCRDet in (select * from GRUPPO_CR_DET
				 	 where esercizio = aEsOrig) loop
		begin
			 insert into GRUPPO_CR_DET (ESERCIZIO,
						    CD_GRUPPO_CR,
						    CD_REGIONE,
						    PG_COMUNE,
						    CD_TERZO_VERSAMENTO,
						    CD_MODALITA_PAGAMENTO,
						    PG_BANCA,
						    DACR,
						    UTCR,
						    DUVA,
						    UTUV,
						    PG_VER_REC)
			 values (aEsDest,
			 	 aGrCRDet.CD_GRUPPO_CR,
			 	 aGrCRDet.CD_REGIONE,
			 	 aGrCRDet.PG_COMUNE,
			 	 aGrCRDet.CD_TERZO_VERSAMENTO,
			 	 aGrCRDet.CD_MODALITA_PAGAMENTO,
			 	 aGrCRDet.PG_BANCA,
				 sysdate,
				 cgUtente,
				 sysdate,
				 cgUtente,
				 1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'GRUPPO_CR_DET con PK ('||aEsDest||', '
					||aGrCRDet.CD_GRUPPO_CR||', '
					||aGrCRDet.CD_REGIONE||', '
					||aGrCRDet.PG_COMUNE||') già inserito','','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento regole in deroga per UO
	aMessage := 'Ribaltamento regole in deroga per UO sull''esercizio '||aEsDest||'. Lock tabella GRUPPO_CR_UO';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for	aGrCRUO in (select * from GRUPPO_CR_UO
				    where esercizio = aEsOrig) loop
		begin
			 insert into GRUPPO_CR_UO (ESERCIZIO,
						   CD_GRUPPO_CR,
						   CD_UNITA_ORGANIZZATIVA,
						   FL_ACCENTRATO,
						   DACR,
						   UTCR,
						   DUVA,
						   UTUV,
						   PG_VER_REC)
			 values (aEsDest,
			 	aGrCRUO.CD_GRUPPO_CR,
			 	aGrCRUO.CD_UNITA_ORGANIZZATIVA,
			 	aGrCRUO.FL_ACCENTRATO,
				sysdate,
				cgUtente,
				sysdate,
				cgUtente,
				1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'GRUPPO_CR_UO con PK ('||aEsDest||', '
					||aGrCRUO.CD_GRUPPO_CR||', '
					||aGrCRUO.CD_UNITA_ORGANIZZATIVA||') già inserito','','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento associazione CORI e relativo gruppo
	aMessage := 'Ribaltamento associazione CORI e relativo gruppo sull''esercizio '||aEsDest||'. Lock tabella TIPO_CR_BASE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for	aTiCR in (select * from TIPO_CR_BASE
			  	  where esercizio = aEsOrig) loop
		begin
			 insert into TIPO_CR_BASE (ESERCIZIO,
						   CD_CONTRIBUTO_RITENUTA,
						   CD_GRUPPO_CR,
						   DACR,
						   UTCR,
						   DUVA,
						   UTUV,
						   PG_VER_REC)
			 values (aEsDest,
			 	aTiCR.CD_CONTRIBUTO_RITENUTA,
			 	aTiCR.CD_GRUPPO_CR,
				sysdate,
				cgUtente,
				sysdate,
				cgUtente,
				1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Contributo ritenuta '||aTiCR.CD_CONTRIBUTO_RITENUTA||' già definito per esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento archivio per liquidazione stipendi
	aMessage := 'Ribaltamento archivio per liquidazione stipendi sull''esercizio '||aEsDest||'. Lock tabella STIPENDI_COFI';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	For	i In 1..15 Loop
		begin
			 insert into STIPENDI_COFI  (ESERCIZIO,
						     MESE,
						     STATO,
						     DACR,
						     UTCR,
						     DUVA,
						     UTUV,
						     PG_VER_REC)
			 values (aEsDest,
			 	 i,
				 'I',
				 sysdate,
				 cgUtente,
				 sysdate,
				 cgUtente,
				 1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Mese '||i||' già definito per esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	End Loop;

	-- Ribaltamento informazioni relative alla regione per liquidazione CORI stipendiali
	aMessage := 'Ribaltamento informazioni relative alla regione per liquidazione CORI stipendiali sull''esercizio '||aEsDest||'. Lock tabella STIPENDI_COFI_CORI_REG';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for	aStip in (select * from STIPENDI_COFI_CORI_REG
			  	  where esercizio = aEsOrig) loop
		begin
			 insert into STIPENDI_COFI_CORI_REG (ESERCIZIO,
							     CD_CONTRIBUTO_RITENUTA,
							     TI_ENTE_PERCIPIENTE,
							     CD_REGIONE,
							     DACR,
							     UTCR,
							     DUVA,
							     UTUV,
							     PG_VER_REC)
			 values (aEsDest,
			 	aStip.CD_CONTRIBUTO_RITENUTA,
				aStip.TI_ENTE_PERCIPIENTE,
				aStip.CD_REGIONE,
				sysdate,
				cgUtente,
				sysdate,
				cgUtente,
				1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'Contributo ritenuta '||aStip.CD_CONTRIBUTO_RITENUTA||' di tipo '||aStip.TI_ENTE_PERCIPIENTE||' già definito per esercizio '||aEsDest,'','');
			aStato := 'W';
		end;
	end loop;
End;
----------------------------------------------------------------------------
procedure ribaltaCORI_pdgp(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
begin

	-- Ribaltamento associazioni tipo cori ed EV
	aMessage := 'Ribaltamento associazioni tipo cori ed EV sull''esercizio '||aEsDest||'. Lock tabella ASS_TIPO_CORI_EV';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aAssCoriEV in (select * from ASS_TIPO_CORI_EV
			 	 where esercizio = aEsOrig) loop
		begin
			 insert into ASS_TIPO_CORI_EV(
					ESERCIZIO,
					TI_APPARTENENZA,
					TI_GESTIONE,
					TI_ENTE_PERCEPIENTE,
					CD_CONTRIBUTO_RITENUTA,
					CD_ELEMENTO_VOCE,
					UTCR,
					DACR,
					UTUV,
					DUVA,
					PG_VER_REC)
			 values (aEsDest,
			 		 aAssCoriEV.TI_APPARTENENZA,
					 aAssCoriEV.TI_GESTIONE,
					 aAssCoriEV.TI_ENTE_PERCEPIENTE,
					 aAssCoriEV.CD_CONTRIBUTO_RITENUTA,
					 aAssCoriEV.CD_ELEMENTO_VOCE,
					 cgUtente,
					 sysdate,
					 cgUtente,
					 sysdate,
					 1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_TIPO_CORI_EV con PK ('||aEsDest||', '
					||aAssCoriEV.TI_APPARTENENZA||', '
					||aAssCoriEV.TI_GESTIONE||', '
					||aAssCoriEV.TI_ENTE_PERCEPIENTE||', '
					||aAssCoriEV.CD_CONTRIBUTO_RITENUTA||') già inserita','','');
			aStato := 'W';
		end;
	end loop;

	-- Ribaltamento associazione tipo cori e conto EP
	aMessage := 'Ribaltamento associazione tipo cori e conto EP sull''esercizio '||aEsDest||'. Lock tabella ASS_TIPO_CORI_VOCE_EP';
	ibmutl200.loginf(aPgEsec,aMessage,'','');
	for aAssCoriEP in (select * from ASS_TIPO_CORI_VOCE_EP
			 	 where esercizio = aEsOrig) loop
		begin
			 insert into ASS_TIPO_CORI_VOCE_EP (ESERCIZIO,
							    CD_CONTRIBUTO_RITENUTA,
								TI_ENTE_PERCEPIENTE,
								SEZIONE,
								CD_VOCE_EP,
								CD_VOCE_EP_CONTR,
								DACR,
								UTCR,
								DUVA,
								UTUV,
								PG_VER_REC)
			 values (aEsDest,
			 		 aAssCoriEP.CD_CONTRIBUTO_RITENUTA,
			 		 aAssCoriEP.TI_ENTE_PERCEPIENTE,
			 		 aAssCoriEP.SEZIONE,
			 		 aAssCoriEP.CD_VOCE_EP,
			 		 aAssCoriEP.CD_VOCE_EP_CONTR,
					 sysdate,
					 cgUtente,
					 sysdate,
					 cgUtente,
					 1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'ASS_TIPO_CORI_VOCE_EP con PK ('||aEsDest||','
					||aAssCoriEP.CD_CONTRIBUTO_RITENUTA||','
					||aAssCoriEP.TI_ENTE_PERCEPIENTE||','
					||aAssCoriEP.SEZIONE||' già inserita','','');
			aStato := 'W';
		end;
	end loop;

end;
----------------------------------------------------------------------------
procedure ribaltaINTRASTAT(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
   id   number:=0;
begin
	-- Ribaltamento NATURA_TRANSAZIONE
	lock table NATURA_TRANSAZIONE in exclusive mode nowait;
	aMessage := 'Ribaltamento NATURA_TRANSAZIONE sull''esercizio '||aEsDest||'. Lock tabella NATURA_TRANSAZIONE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');

	select max(id_natura_transazione)
	into id
	from NATURA_TRANSAZIONE;

	for aNaturaTransazione in (select * from NATURA_TRANSAZIONE
			 	 where esercizio = aEsOrig
			 	 order by id_natura_transazione) loop
		begin
			 id:=id+1;
			 insert into NATURA_TRANSAZIONE(
					ID_NATURA_TRANSAZIONE,
          ESERCIZIO,
          CD_NATURA_TRANSAZIONE,
          CD_OPER_TRIANGOLARE,
          DS_NATURA_TRANSAZIONE,
          DACR,
          UTCR,
          DUVA,
          UTUV,
          PG_VER_REC)
			 values (id,
			         aEsDest,
               aNaturaTransazione.CD_NATURA_TRANSAZIONE,
               aNaturaTransazione.CD_OPER_TRIANGOLARE,
               aNaturaTransazione.DS_NATURA_TRANSAZIONE,
					     sysdate,
					     cgUtente,
					     sysdate,
					     cgUtente,
					     1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'NATURA_TRANSAZIONE con PK ('||id||') già inserita','','');
			aStato := 'W';
		end;
	end loop;
	-- Ribaltamento MODALITA_TRASPORTO
	aMessage := 'Ribaltamento MODALITA_TRASPORTO sull''esercizio '||aEsDest||'. Lock tabella MODALITA_TRASPORTO';
	ibmutl200.loginf(aPgEsec,aMessage,'','');

	for aModalitaTrasporto in (select * from MODALITA_TRASPORTO
			 	 where esercizio = aEsOrig) loop
		begin
			 insert into MODALITA_TRASPORTO(
					ESERCIZIO,
					CD_MODALITA_TRASPORTO,
					DS_MODALITA_TRASPORTO,
          DACR,
          UTCR,
          DUVA,
          UTUV,
          PG_VER_REC)
			 values (aEsDest,
			 		     aModalitaTrasporto.CD_MODALITA_TRASPORTO,
					     aModalitaTrasporto.DS_MODALITA_TRASPORTO,
					     sysdate,
					     cgUtente,
					     sysdate,
					     cgUtente,
					     1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'MODALITA_TRASPORTO con PK ('||aEsDest||', '
					||aModalitaTrasporto.CD_MODALITA_TRASPORTO||') già inserita','','');
			aStato := 'W';
		end;
	end loop;
	-- Ribaltamento CONDIZIONE_CONSEGNA
	aMessage := 'Ribaltamento CONDIZIONE_CONSEGNA sull''esercizio '||aEsDest||'. Lock tabella CONDIZIONE_CONSEGNA';
	ibmutl200.loginf(aPgEsec,aMessage,'','');

	for aCondizioneConsegna in (select * from CONDIZIONE_CONSEGNA
			 	 where esercizio = aEsOrig) loop
		begin
			 insert into CONDIZIONE_CONSEGNA(
					ESERCIZIO,
					CD_INCOTERM,
					CD_GRUPPO,
					DS_CONDIZIONE_CONSEGNA,
          DACR,
          UTCR,
          DUVA,
          UTUV,
          PG_VER_REC)
			 values (aEsDest,
			 		     aCondizioneConsegna.CD_INCOTERM,
					     aCondizioneConsegna.CD_GRUPPO,
					     aCondizioneConsegna.DS_CONDIZIONE_CONSEGNA,
					     sysdate,
					     cgUtente,
					     sysdate,
					     cgUtente,
					     1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CONDIZIONE_CONSEGNA con PK ('||aEsDest||', '
					||aCondizioneConsegna.CD_INCOTERM||') già inserita','','');
			aStato := 'W';
		end;
	end loop;
	-- Ribaltamento NOMENCLATURA_COMBINATA
	lock table NOMENCLATURA_COMBINATA in exclusive mode nowait;
	aMessage := 'Ribaltamento NOMENCLATURA_COMBINATA sull''esercizio '||aEsDest||'. Lock tabella NOMENCLATURA_COMBINATA';
	ibmutl200.loginf(aPgEsec,aMessage,'','');

	select max(id_nomenclatura_combinata)
	into id
	from NOMENCLATURA_COMBINATA;

	for aNomenclatura in (select * from NOMENCLATURA_COMBINATA
			 	 where esercizio = aEsOrig
			 	   and esercizio_fine >= aEsDest
			 	 order by id_nomenclatura_combinata) loop
		begin
			 id:=id+1;
			 insert into NOMENCLATURA_COMBINATA(
					ID_NOMENCLATURA_COMBINATA,
					ESERCIZIO,
					CD_NOMENCLATURA_COMBINATA,
					DS_NOMENCLATURA_COMBINATA,
					LIVELLO,
					DACR,
					UTCR,
					DUVA,
					UTUV,
					PG_VER_REC,
					ESERCIZIO_INIZIO,
					ESERCIZIO_FINE,
          UNITA_SUPPLEMENTARI)
			 values (id,
					     aEsDest,
					     aNomenclatura.CD_NOMENCLATURA_COMBINATA,
					     aNomenclatura.DS_NOMENCLATURA_COMBINATA,
					     aNomenclatura.LIVELLO,
					     sysdate,
					     cgUtente,
					     sysdate,
					     cgUtente,
					     1,
					     aNomenclatura.ESERCIZIO_INIZIO,
					     aNomenclatura.ESERCIZIO_FINE,
               aNomenclatura.UNITA_SUPPLEMENTARI);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'NOMENCLATURA_COMBINATA con PK ('||id||') già inserita','','');
			aStato := 'W';
		end;
	end loop;
	-- Ribaltamento MODALITA_EROGAZIONE
	aMessage := 'Ribaltamento MODALITA_EROGAZIONE sull''esercizio '||aEsDest||'. Lock tabella MODALITA_EROGAZIONE';
	ibmutl200.loginf(aPgEsec,aMessage,'','');

	for aModalitaErogazione in (select * from MODALITA_EROGAZIONE
			 	 where esercizio = aEsOrig) loop
		begin
			 insert into MODALITA_EROGAZIONE(
					ESERCIZIO,
					CD_MODALITA_EROGAZIONE,
					DS_MODALITA_EROGAZIONE,
          DACR,
          UTCR,
          DUVA,
          UTUV,
          PG_VER_REC)
			 values (aEsDest,
			 		     aModalitaErogazione.CD_MODALITA_EROGAZIONE,
					     aModalitaErogazione.DS_MODALITA_EROGAZIONE,
					     sysdate,
					     cgUtente,
					     sysdate,
					     cgUtente,
					     1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'MODALITA_EROGAZIONE con PK ('||aEsDest||', '
					||aModalitaErogazione.CD_MODALITA_EROGAZIONE||') già inserita','','');
			aStato := 'W';
		end;
	end loop;
	-- Ribaltamento MODALITA_INCASSO
	aMessage := 'Ribaltamento MODALITA_INCASSO sull''esercizio '||aEsDest||'. Lock tabella MODALITA_INCASSO';
	ibmutl200.loginf(aPgEsec,aMessage,'','');

	for aModalitaIncasso in (select * from MODALITA_INCASSO
			 	 where esercizio = aEsOrig) loop
		begin
			 insert into MODALITA_INCASSO(
					ESERCIZIO,
					CD_MODALITA_INCASSO,
					DS_MODALITA_INCASSO,
          DACR,
          UTCR,
          DUVA,
          UTUV,
          PG_VER_REC)
			 values (aEsDest,
			 		     aModalitaIncasso.CD_MODALITA_INCASSO,
					     aModalitaIncasso.DS_MODALITA_INCASSO,
					     sysdate,
					     cgUtente,
					     sysdate,
					     cgUtente,
					     1);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'MODALITA_INCASSO con PK ('||aEsDest||', '
					||aModalitaIncasso.CD_MODALITA_INCASSO||') già inserita','','');
			aStato := 'W';
		end;
	end loop;
	-- Ribaltamento CODICI_CPA
	lock table CODICI_CPA in exclusive mode nowait;
	aMessage := 'Ribaltamento CODICI_CPA sull''esercizio '||aEsDest||'. Lock tabella CODICI_CPA';
	ibmutl200.loginf(aPgEsec,aMessage,'','');

	select max(id_cpa)
	into id
	from CODICI_CPA;

	for aCodiciCPA in (select * from CODICI_CPA
			 	 where esercizio = aEsOrig
			 	 order by id_cpa) loop
		begin
			 id:=id+1;
			 insert into CODICI_CPA(
					ID_CPA,
					ESERCIZIO,
					CD_CPA,
					DS_CPA,
					LIVELLO,
					DACR,
					UTCR,
					DUVA,
					UTUV,
					PG_VER_REC,
					TI_BENE_SERVIZIO,
					FL_UTILIZZABILE)
			 values (id,
					     aEsDest,
					     aCodiciCPA.CD_CPA,
					     aCodiciCPA.DS_CPA,
					     aCodiciCPA.LIVELLO,
					     sysdate,
					     cgUtente,
					     sysdate,
					     cgUtente,
					     1,
					     aCodiciCPA.TI_BENE_SERVIZIO,
					     aCodiciCPA.FL_UTILIZZABILE);
		exception when DUP_VAL_ON_INDEX then
			ibmutl200.LOGWAR(aPgEsec,'CODICI_CPA con PK ('||id||') già inserito','','');
			aStato := 'W';
		end;
	end loop;
end;
----------------------------------------------------------------------------
procedure ribaltaLimitiSpesa(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2) is
parent_key_not_found exception;
pragma exception_init(parent_key_not_found,-2291);
begin
  -- Ribaltamento tabella LIMITE_SPESA
	aMessage := 'Ribaltamento dei limiti di spesa sull''esercizio '||aEsDest||'. Lock tabella LIMITE_SPESA';
	ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	for aLimitiSpesa in (select * from LIMITE_SPESA
			 	               where esercizio = aEsOrig) loop
			--inserimento testata
		  begin
			  insert into LIMITE_SPESA  (ESERCIZIO,
                                   TI_APPARTENENZA,
                                   TI_GESTIONE,
                                   CD_ELEMENTO_VOCE,
                                   FONTE,
                                   IMPORTO_LIMITE,
                                   IMPORTO_ASSEGNATO,
                                   UTCR,
                                   DACR,
                                   UTUV,
                                   DUVA,
                                   PG_VER_REC,
                                   ESERCIZIO_VOCE)
			  values (aEsDest,
				        aLimitiSpesa.TI_APPARTENENZA,
                aLimitiSpesa.TI_GESTIONE,
                aLimitiSpesa.CD_ELEMENTO_VOCE,
                aLimitiSpesa.FONTE,
                0,
                0,
				        cgUtente,
				        sysdate,
				        cgUtente,
				        sysdate,
				        1,
				        aEsDest);
		  exception when DUP_VAL_ON_INDEX then
			  ibmutl200.LOGWAR(aPgEsec,'LIMITE_SPESA con PK('||aEsDest||', '
					||aLimitiSpesa.TI_APPARTENENZA||', '
					||aLimitiSpesa.TI_GESTIONE||', '
					||aLimitiSpesa.CD_ELEMENTO_VOCE||', '
					||aLimitiSpesa.FONTE||') già esistente','','');
			  aStato := 'W';
		  end;
		  --inserimento dettagli
      aMessage := 'Creazione dei limiti di spesa nell''esercizio contabile '||aEsDest||' per i CDS. Lock tabelle LIMITE_SPESA_DET.';
	    ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	    -- ciclo sui cds validi nel corso dell'esercizio di destinazione
	    for aCds in (select * from v_unita_organizzativa_valida
	          		 	 where fl_cds    = 'Y'
	         			     and esercizio = aEsDest
	       		   	     and cd_unita_organizzativa <> '999') loop
	       	begin
	       		 aMessage := 'Inserimento limiti di spesa per CdS: '||aCds.cd_unita_organizzativa;
	       		 ibmutl200.LOGINF(aPgEsec,aMessage,'','');
	       		 insert into LIMITE_SPESA_DET (ESERCIZIO,
                                            CD_CDS,
                                            TI_APPARTENENZA,
                                            TI_GESTIONE,
                                            CD_ELEMENTO_VOCE,
                                            FONTE,
                                            IMPORTO_LIMITE,
                                            IMPEGNI_ASSUNTI,
                                            UTCR,
                                            DACR,
                                            UTUV,
                                            DUVA,
                                            PG_VER_REC,
                                            IMPORTO_INIZIALE,
                                            ESERCIZIO_VOCE)
              values (aEsDest,
                      aCds.cd_unita_organizzativa,
				              aLimitiSpesa.TI_APPARTENENZA,
                      aLimitiSpesa.TI_GESTIONE,
                      aLimitiSpesa.CD_ELEMENTO_VOCE,
                      aLimitiSpesa.FONTE,
                      0,
                      0,
				              cgUtente,
				              sysdate,
				              cgUtente,
				              sysdate,
				              1,
				              0,
				              aEsDest);
		      exception when DUP_VAL_ON_INDEX then
			         ibmutl200.LOGWAR(aPgEsec,'LIMITI_SPESA con PK('||aEsDest||', '
				       	||aCds.cd_unita_organizzativa||', '
				       	||aLimitiSpesa.TI_APPARTENENZA||', '
				       	||aLimitiSpesa.TI_GESTIONE||', '
				       	||aLimitiSpesa.CD_ELEMENTO_VOCE||', '
				       	||aLimitiSpesa.FONTE||') già esistente','','');
			         aStato := 'W';
	       	end;
	    end loop; -- fine ciclo sui cds
	end loop;  -- fine ciclo aLimitiSpesa

end;

procedure AGGIORMENTO_PROGETTI(aEs number, pg_exec number) as
   aTSNow date;
   aUser varchar2(20);
   aMessage varchar2(500);
   P_INDEX NUMBER := 0;
   CONTA_INS NUMBER := 0;
begin
   aTSNow:=sysdate;
   aUser:=IBMUTL200.getUserFromLog(pg_exec);

   --Inserisco sui progetti le voci di bilancio nuove create associate ai piani economici e che prevedono l'inserimento automatico e non manuale
   INSERT INTO ASS_PROGETTO_PIAECO_VOCE
      (PG_PROGETTO, CD_UNITA_ORGANIZZATIVA, CD_VOCE_PIANO, ESERCIZIO_PIANO, ESERCIZIO_VOCE, TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, DACR, UTCR, DUVA, UTUV, PG_VER_REC)
   SELECT A.PG_PROGETTO, B.CD_UNITA_PIANO, B.CD_VOCE_PIANO, A.ESERCIZIO_PIANO,
          B.ESERCIZIO, B.TI_APPARTENENZA, B.TI_GESTIONE, B.CD_ELEMENTO_VOCE, aTSNow, aUser, aTSNow, aUser, 0
   FROM PROGETTO_PIANO_ECONOMICO A, ELEMENTO_VOCE B, VOCE_PIANO_ECONOMICO_PRG C
   WHERE A.CD_UNITA_ORGANIZZATIVA = C.CD_UNITA_ORGANIZZATIVA
   AND   A.CD_VOCE_PIANO = C.CD_VOCE_PIANO
   AND   B.CD_UNITA_PIANO = C.CD_UNITA_ORGANIZZATIVA
   AND   B.CD_VOCE_PIANO = C.CD_VOCE_PIANO
   AND   A.ESERCIZIO_PIANO = B.ESERCIZIO
   AND   C.FL_ADD_VOCIBIL = 'N'
   AND   A.ESERCIZIO_PIANO = aEs
   AND   NOT EXISTS(SELECT 1 FROM ASS_PROGETTO_PIAECO_VOCE D
                    WHERE D.PG_PROGETTO = A.PG_PROGETTO
                    AND   D.CD_UNITA_ORGANIZZATIVA = A.CD_UNITA_ORGANIZZATIVA
                    AND   D.CD_VOCE_PIANO = A.CD_VOCE_PIANO
                    AND   D.ESERCIZIO_PIANO = A.ESERCIZIO_PIANO);

   aMessage := 'Aggiornamento voci su piano economico progetti. Inseriti '||sql%rowcount||' record.';
   ibmutl200.LOGINF(pg_exec,aMessage,'','');

   --Inserisco sulle rimodulazioni dei progetti le voci di bilancio nuove create associate ai piani economici e che prevedono l'inserimento automatico e non manuale
   FOR REC IN (SELECT DISTINCT A.PG_PROGETTO, A.PG_RIMODULAZIONE, B.CD_UNITA_PIANO, B.CD_VOCE_PIANO, A.ESERCIZIO_PIANO
               FROM PROGETTO_RIMODULAZIONE_PPE A, PROGETTO_RIMODULAZIONE PR, ELEMENTO_VOCE B, VOCE_PIANO_ECONOMICO_PRG C
               WHERE A.CD_UNITA_ORGANIZZATIVA = C.CD_UNITA_ORGANIZZATIVA
               AND   A.CD_VOCE_PIANO = C.CD_VOCE_PIANO
               AND   B.CD_UNITA_PIANO = C.CD_UNITA_ORGANIZZATIVA
               AND   B.CD_VOCE_PIANO = C.CD_VOCE_PIANO
               AND   A.ESERCIZIO_PIANO = B.ESERCIZIO
               AND   C.FL_ADD_VOCIBIL = 'N'
               AND   A.PG_PROGETTO = PR.PG_PROGETTO
               AND   A.PG_RIMODULAZIONE = PR.PG_RIMODULAZIONE
               AND   PR.STATO NOT IN ('A', 'R')
               AND   A.ESERCIZIO_PIANO = aEs
               AND   (A.IM_VAR_SPESA_FINANZIATO > 0 OR A.IM_VAR_SPESA_COFINANZIATO > 0 OR A.IM_STOASS_SPESA_FINANZIATO > 0 OR A.IM_STOASS_SPESA_COFINANZIATO > 0)
               AND   NOT EXISTS(SELECT 1 FROM PROGETTO_RIMODULAZIONE_VOCE D
                                WHERE D.PG_PROGETTO = A.PG_PROGETTO
                                AND   D.PG_RIMODULAZIONE = A.PG_RIMODULAZIONE
                                AND   D.CD_UNITA_ORGANIZZATIVA = A.CD_UNITA_ORGANIZZATIVA
                                AND   D.CD_VOCE_PIANO = A.CD_VOCE_PIANO
                                AND   D.ESERCIZIO_PIANO = A.ESERCIZIO_PIANO)
               AND   NOT EXISTS(SELECT 1 FROM ASS_PROGETTO_PIAECO_VOCE E
                                WHERE E.PG_PROGETTO = A.PG_PROGETTO
                                AND   E.CD_UNITA_ORGANIZZATIVA = A.CD_UNITA_ORGANIZZATIVA
                                AND   E.CD_VOCE_PIANO = A.CD_VOCE_PIANO
                                AND   E.ESERCIZIO_PIANO = A.ESERCIZIO_PIANO)) LOOP

       P_INDEX := 0;

       FOR DET IN (SELECT A.PG_PROGETTO, A.PG_RIMODULAZIONE, B.CD_UNITA_PIANO, B.CD_VOCE_PIANO, A.ESERCIZIO_PIANO,
       	               B.ESERCIZIO, B.TI_APPARTENENZA, B.TI_GESTIONE, B.CD_ELEMENTO_VOCE
                   FROM PROGETTO_RIMODULAZIONE_PPE A, PROGETTO_RIMODULAZIONE PR, ELEMENTO_VOCE B, VOCE_PIANO_ECONOMICO_PRG C
                   WHERE A.CD_UNITA_ORGANIZZATIVA = C.CD_UNITA_ORGANIZZATIVA
                   AND   A.CD_VOCE_PIANO = C.CD_VOCE_PIANO
                   AND   B.CD_UNITA_PIANO = C.CD_UNITA_ORGANIZZATIVA
                   AND   B.CD_VOCE_PIANO = C.CD_VOCE_PIANO
                   AND   A.ESERCIZIO_PIANO = B.ESERCIZIO
                   AND   C.FL_ADD_VOCIBIL = 'N'
                   AND   A.PG_PROGETTO = PR.PG_PROGETTO
                   AND   A.PG_RIMODULAZIONE = PR.PG_RIMODULAZIONE
                   AND   PR.STATO NOT IN ('A', 'R')
                   AND   A.ESERCIZIO_PIANO = aEs
                   AND   (A.IM_VAR_SPESA_FINANZIATO > 0 OR A.IM_VAR_SPESA_COFINANZIATO > 0 OR A.IM_STOASS_SPESA_FINANZIATO > 0 OR A.IM_STOASS_SPESA_COFINANZIATO > 0)
                   AND   NOT EXISTS(SELECT 1 FROM PROGETTO_RIMODULAZIONE_VOCE D
                                    WHERE D.PG_PROGETTO = A.PG_PROGETTO
                    				AND   D.PG_RIMODULAZIONE = A.PG_RIMODULAZIONE
                                    AND   D.CD_UNITA_ORGANIZZATIVA = A.CD_UNITA_ORGANIZZATIVA
                                    AND   D.CD_VOCE_PIANO = A.CD_VOCE_PIANO
                                    AND   D.ESERCIZIO_PIANO = A.ESERCIZIO_PIANO)
                   AND   NOT EXISTS(SELECT 1 FROM ASS_PROGETTO_PIAECO_VOCE E
                                    WHERE E.PG_PROGETTO = A.PG_PROGETTO
                                    AND   E.CD_UNITA_ORGANIZZATIVA = A.CD_UNITA_ORGANIZZATIVA
                                    AND   E.CD_VOCE_PIANO = A.CD_VOCE_PIANO
                                    AND   E.ESERCIZIO_PIANO = A.ESERCIZIO_PIANO)
                   AND   A.PG_PROGETTO = REC.PG_PROGETTO
                   AND   A.PG_RIMODULAZIONE = REC.PG_RIMODULAZIONE
                   AND   B.CD_UNITA_PIANO = REC.CD_UNITA_PIANO
                   AND   B.CD_VOCE_PIANO = REC.CD_VOCE_PIANO
                   AND   A.ESERCIZIO_PIANO = REC.ESERCIZIO_PIANO) LOOP

           P_INDEX := P_INDEX + 1;
           INSERT INTO PROGETTO_RIMODULAZIONE_VOCE
               (PG_PROGETTO, PG_RIMODULAZIONE, CD_UNITA_ORGANIZZATIVA, CD_VOCE_PIANO, ESERCIZIO_PIANO,
                PG_VARIAZIONE, ESERCIZIO_VOCE, TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, TI_OPERAZIONE, IM_VAR_SPESA_FINANZIATO, IM_VAR_SPESA_COFINANZIATO, DACR, UTCR, DUVA, UTUV, PG_VER_REC)
           VALUES(DET.PG_PROGETTO, DET.PG_RIMODULAZIONE, DET.CD_UNITA_PIANO, DET.CD_VOCE_PIANO, DET.ESERCIZIO_PIANO,
                P_INDEX, DET.ESERCIZIO, DET.TI_APPARTENENZA, DET.TI_GESTIONE, DET.CD_ELEMENTO_VOCE, 'A', 0, 0,
                aTSNow, aUser, aTSNow, aUser, 0);

           CONTA_INS := CONTA_INS + SQL%ROWCOUNT;
       END LOOP;
   END LOOP;
   aMessage := 'Aggiornamento voci su rimodulazione piano economico progetti. Inseriti '||sql%rowcount||' record.';
   ibmutl200.LOGINF(pg_exec,aMessage,'','');
end;

procedure JOB_RIBALTAMENTO_PDGP(job number, pg_exec number, next_date date, aEs number) as
    aTSNow date;
    aUser varchar2(20);
    aMessage varchar2(500);
begin
    aTSNow:=sysdate;
    aUser:=IBMUTL200.getUserFromLog(pg_exec);
    lPgExec := pg_exec;

    -- Aggiorna le info di testata del log
    IBMUTL210.logStartExecutionUpd(lPgExec, TI_LOG_RIBALTAMENTO_PDGP, job, 'Batch di ribaltamento configurazione, str.organizzativa, anagrafica capitoli e piano dei conti. Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));

	if aEs = 0 then
	   ibmutl200.logErr(lPgExec,'Esercizio zero non gestito', '', '');
	else
	   insert into numerazione_base (ESERCIZIO, COLONNA, TABELLA, CD_CORRENTE, CD_MASSIMO, DUVA, UTUV, DACR, UTCR, PG_VER_REC, CD_INIZIALE )
	   (select aEs, COLONNA, TABELLA, 0, CD_MASSIMO, aTSNow, aUser, aTSNow, aUser, 1, CD_INIZIALE
	    FROM numerazione_base
	    WHERE Esercizio = aEs - 1
	    AND TABELLA IN ('VAR_STANZ_RES' , 'VAR_STANZ_RES$'));

	   INIT_RIBALTAMENTO_pdgp(aEs,pg_exec,aMessage);
	   AGGIORMENTO_PROGETTI(aEs,pg_exec);

       ibmutl200.logInf(pg_exec,aMessage, '', '');
       ibmutl200.logInf(pg_exec,'Batch di ribaltamento configurazione, str.organizzativa, anagrafica capitoli e piano dei conti.', 'End:'||to_char(sysdate,'YYYY/MM/DD HH-MI-SS'), '');
    end if;
 end;
----------------------------------------------------------------------------
end;
