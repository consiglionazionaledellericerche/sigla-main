--------------------------------------------------------
--  DDL for Package Body CNRCTB047
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB047" is

procedure delete_vsx(aPgCall number) is
begin
	 delete from vsx_chiusura
	 where pg_call = aPgCall;
end;

procedure inizializzaRiassunto is
begin
	-- inizializzo le variabili globali per il log riassuntivo
	totImp := 0;
	totImpRes := 0;
	totObb := 0;
	totObbResPro := 0;
	totObbPlur := 0;
	totAnnPGiro := 0;
	totAcc := 0;
	totAccPlur := 0;
	totAccRes := 0;
	totImpRip := 0;
	totImpResRip := 0;
	totObbRip := 0;
	totObbResPRoRip := 0;
	totObbPlurRip := 0;
	totAnnPGiroRip := 0;
	totAccRip := 0;
	totAccPlurRip := 0;
	totAccResRip := 0;
	imRiportatoSpesa := 0;
	imNonRiportatoSpesa := 0;
	imRiportatoEntrata := 0;
	imNonRiportatoEntrata := 0;
end;

procedure aggiornaRiassunto(aObb obbligazione%rowtype, docRiportato boolean) is
begin
	-- totale documenti processati
	if aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP then
		totImp := totImp + 1;
	elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES then
		totImpRes := totImpRes + 1;
	elsif (aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB and aObb.esercizio = aObb.esercizio_competenza) then
		totObb := totObb + 1;
	elsif (aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB and aObb.esercizio <> aObb.esercizio_competenza) then
		totObbPlur := totObbPlur + 1;
	elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_RES_PRO Then
	        totObbResPro := totObbResPro + 1;
	elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_PGIRO then
		totAnnPGiro := totAnnPGiro + 1;
	end if;

	-- totale documenti riportati e im documenti riportati/non riportati
	if docRiportato then
		if aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP then
			totImpRip := totImpRip + 1;
		elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES then
			totImpResRip := totImpResRip + 1;
		elsif (aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB and aObb.esercizio = aObb.esercizio_competenza) then
			totObbRip := totObbRip + 1;
		elsif (aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB and aObb.esercizio <> aObb.esercizio_competenza) then
			totObbPlurRip := totObbPlurRip + 1;
        	elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_RES_PRO Then
                        totObbResPRoRip := totObbResPRoRip + 1;
		elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_PGIRO then
			totAnnPGiroRip := totAnnPGiroRip + 1;
		end if;
		imRiportatoSpesa := imRiportatoSpesa + aObb.im_obbligazione;
	else -- doc non riportato
		imNonRiportatoSpesa := imNonriportatoSpesa + aObb.im_obbligazione;
	end if;
end;

procedure aggiornaRiassunto(aAcc accertamento%rowtype, docRiportato boolean) is
begin
	-- totale documenti processati
	if aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC then
	   totAcc := totAcc + 1;
	elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES then
	   totAccRes := totAccRes + 1;
	elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PGIRO then
	   totAnnPGiro := totAnnPGiro + 1;
	elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PLUR then
	   totAccPlur := totAccPlur + 1;
	end if;

	-- totale documenti riportati e im documenti riportati/non riportati
	if docRiportato then
		if aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC then
		   totAccRip := totAccRip + 1;
		elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES then
		   totAccResRip := totAccRip + 1;
		elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PGIRO then
		   totAnnPGiroRip := totAnnPGiroRip + 1;
		elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PLUR then
		   totAccPlurRip := totAccPlurRip + 1;
		end if;
		imRiportatoEntrata := imRiportatoEntrata + aAcc.im_accertamento;
	else -- doc non riportato
		imNonRiportatoEntrata := imNonriportatoEntrata + aAcc.im_accertamento;
	end if;
end;

function riassunto return varchar2 is
Str varchar2(4000);
begin
	Str :=      'Impegni residui processati: '||totImpRes||', ';
	Str := Str||'Impegni residui riportati: '||totImpResRip||', ';
	Str := Str||'Impegni processati: '||totImp||', ';
	Str := Str||'Impegni riportati: '||totImpRip||', ';
	Str := Str||cnrutil.getLabelObbligazioni()||' residue processate: '||totObbResPro||', ';
	Str := Str||cnrutil.getLabelObbligazioni()||' residue riportate: '||totObbResProRip||', ';
	Str := Str||cnrutil.getLabelObbligazioni()||' pluriennali processate: '||totObbPlur||', ';
	Str := Str||cnrutil.getLabelObbligazioni()||' pluriennali riportate: '||totObbPlurRip||', ';
	Str := Str||'Accertamenti processati: '||totAcc||', ';
	Str := Str||'Accertamenti riportati: '||totAccRip||', ';
	Str := Str||'Accertamenti pluriennali processati: '||totAccPlur||', ';
	Str := Str||'Accertamenti pluriennali riportati: '||totAccPlurRip||',';
	Str := Str||'Annotazioni su partita di giro processate: '||totAnnPGiro||', ';
	Str := Str||'Annotazioni su partita di giro riportate: '||totAnnPGiroRip||'. ';
	Str := Str||'Importo di spesa riportato: '||imRiportatoSpesa||', ';
	Str := Str||'Importo di spesa non riportato: '||imNonRiportatoSpesa||', ';
	Str := Str||'Importo di entrata riportato: '||imRiportatoEntrata||', ';
	Str := Str||'Importo di entrata non riportato: '||imNonRiportatoEntrata;
	return Str;
end;

-- vsx_annullamentoDocCont
-- -----------------------
-- pre-post name: annullamento massivo di documenti contabili - risorsa non piש valida
-- pre: il documento contabile in processo ט stato nel frattempo modificato
-- post: viene creata una riga di log di errore. La procedura procede al successivo,
--       se esiste, documento da processare
--
-- pre-post name: annullamento massivo di documenti contabili - esercizio non aperto
-- pre: l'esercizio del documento non ט aperto per il cds
-- post: viene creata una riga di log di errore. La procedura procede al successivo,
--       se esiste, documento da processare
--
--
-- pre-post name: annullamento massivo di documenti contabili - documento riportato
--                all'esercizio successivo, ma non riportabile indietro
-- pre: il documento contabile in processo ט ancora valido, risulta riportato all'esercizio
--      successivo, ma non soddisfa le condizione per essere riportato all'esercizio origine
-- post: viene creata una riga di log dell'errore sollevato. La procedura procede
--       al successivo, se esiste, documento da processare.
--
-- pre-post name: annullamento massivo di documenti contabili - errore nell'annullamento
-- pre: il documento contabile in processo ט ancora valido, non puע essere annullato
-- post: il documento viene riportato all'esercizio di partenza se risulta giא riportato, viene
--       generato un errore nella fase di annullamento, viene creata una riga di log
--       dell'errore sollevato. La procedura procede al successivo, se esiste, documento
--       da processare.
--
-- pre-post name: annullamento massivo di documenti contabili - operazione eseguita
-- pre: nessuna delle precedenti precondizioni ט soddisfatta
-- post: il documento viene riportato all'esercizio di partenza se risulta giא riportato,
--       viene annullato logicamente, viene creata una riga di log di avvenuto annullamento.
--       La procedura procede al successivo, se esiste, documento da processare.
--
-- Parametri:
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aPg_exec -> numero progressivo del batch
procedure vsx_annullamentoDocCont(aPgCall number, aPg_exec number) is
aObb obbligazione%rowtype;
aAcc accertamento%rowtype;
aDescDocCont varchar2(500);
aUser varchar2(20);
aEs number;
aDeltaSaldo voce_f_saldi_cdr_linea%Rowtype;
begin
	Select Distinct utcr, esercizio into aUser, aEs
	from vsx_chiusura
	where pg_call = aPgCall;

	for aCds in (select * from v_unita_organizzativa_valida
			 	 where esercizio = aEs
				   and fl_cds = 'Y'
				 order by cd_unita_organizzativa) loop
		for aDocCont in (select * from vsx_chiusura
					 	 where pg_call = aPgCall
						   and cd_cds_origine  = aCds.cd_unita_organizzativa) loop
			savepoint CNRCTB047_SP_002;
			begin
				if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then
				    aAcc.cd_cds := aDocCont.cd_cds;
					aAcc.esercizio := aDocCont.esercizio;
					aAcc.esercizio_originale := aDocCont.esercizio_ori_acc_obb;
					aAcc.pg_accertamento := aDocCont.pg_acc_obb;

					-- verifica della validitא della risorsa
					-- lock del documento
					CNRCTB035.LOCKDOCFULLCHECK(aAcc,aDocCont.pg_ver_rec_doc);

					aDescDocCont := 'Accertamento '||CNRCTB035.GETDESC(aAcc);

					-- verifico lo stato dell'esercizio
					if not cnrctb008.ISESERCIZIOAPERTO(aAcc.esercizio,aAcc.cd_cds) then
					   ibmerr001.RAISE_ERR_GENERICO('Esercizio '||aAcc.esercizio||' non aperto per il cds'||aAcc.cd_cds);
					end if;

					-- se il documento ט riportato, viene riportato indietro
					-- e quindi annullato
					if aAcc.riportato = 'Y' then
						CNRCTB046.DERIPORTOESNEXTACC(aAcc,aUser,sysdate);
					end if;

					CNRCTB035.ANNULLAACCERTAMENTO(aAcc.CD_CDS,
					                              aAcc.ESERCIZIO,
					                              aAcc.ESERCIZIO_ORIGINALE,
								      aAcc.PG_ACCERTAMENTO,
								      aUser);
				else
					aObb.cd_cds := aDocCont.cd_cds;
					aObb.esercizio := aDocCont.esercizio;
					aObb.esercizio_originale := aDocCont.esercizio_ori_acc_obb;
					aObb.pg_obbligazione := aDocCont.pg_acc_obb;

					-- verifica della validitא della risorsa
					-- lock del documento
					CNRCTB035.LOCKDOCFULLCHECK(aObb,aDocCont.pg_ver_rec_doc);

					aDescDocCont := cnrutil.getLabelObbligazione()||' '||CNRCTB035.GETDESC(aObb);

					-- verifico lo stato dell'esercizio
					if not cnrctb008.ISESERCIZIOAPERTO(aObb.esercizio,aObb.cd_cds) then
					   ibmerr001.RAISE_ERR_GENERICO('Esercizio '||aObb.esercizio||' non aperto per il cds'||aObb.cd_cds);
					end if;

					-- se il documento ט riportato, viene riportato indietro
					-- e quindi annullato
					if aObb.riportato = 'Y' then
						CNRCTB046.DERIPORTOESNEXTOBB(aObb,aUser,sysdate);
					end if;

					CNRCTB035.ANNULLAOBBLIGAZIONE(aObb.CD_CDS,
					                              aObb.ESERCIZIO,
					                              aObb.ESERCIZIO_ORIGINALE,
								      aObb.PG_OBBLIGAZIONE,
								      aUser);
				end if;
				commit;
				ibmutl200.loginf(aPg_exec,DS_ANNULLAMENTO||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',aDescDocCont||': ט stato annullato','');
			exception when OTHERS then
				rollback to savepoint CNRCTB047_SP_002;
				IBMUTL200.LOGERR(aPg_exec,DS_ANNULLAMENTO||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',aDescDocCont||': '||DBMS_UTILITY.FORMAT_ERROR_STACK,'');
			end;

		end loop; -- ciclo sui documenti
	end loop; -- ciclo sui cds validi
end;


procedure job_annullamento_doc_cont(job number, pg_exec number, next_date date, aPgCall number, aUser varchar2) is
begin
-- updata batch_log_tsta e committa
	IBMUTL210.logStartExecutionUpd (pg_exec,
									LOG_TIPO_ANN_DOCCONT,
									job,
									'Richista utente: '||aUser,
									DS_ANNULLAMENTO||' Start:'||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'));
	begin
		vsx_annullamentoDocCont(aPgCall, pg_exec);
		delete_vsx(aPgCall);
		commit;
		-- Messaggio all'utente di operazione completata
		IBMUTL205.LOGINF(DS_ANNULLAMENTO, DS_ANNULLAMENTO||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'),'Operazione completata (pg_exec='||pg_exec||')',aUser);
	exception when others then
		rollback;
		delete_vsx(aPgCall);
		commit;
		-- Messaggio di attenzione all'utente
		IBMUTL205.LOGWAR(DS_ANNULLAMENTO, DS_ANNULLAMENTO||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_exec||')',DBMS_UTILITY.FORMAT_ERROR_STACK,aUser);
	end;
end;

-- vsx_riportoNextDocCont
-- ----------------------
-- pre-post name: riporto all'esercizio successivo - risorsa non piש valida
-- pre: il documento contabile in processo ט stato nel frattempo modificato
-- post: viene aggiornato il riassunto per il documento non riportato, e creata una
--       riga di log di errore. La procedura procede al successivo, se esiste,
--       documento da processare
--
-- pre-post name: riporto all'esercizio successivo - errore nel riportare il documento
-- pre: il documento contabile in processo ט ancora valido, viene sollevato un errore
--      nella procedura di riporto all'esercizio successivo
-- post: viene aggiornato il riassunto per il documento non riportato, e creata una
--       riga di log dell'errore sollevato. La procedura procede al successivo, se esiste,
--       documento da processare
--
-- pre-post name: riporto all'esercizio successivo - operazione eseguita
-- pre: nessuna delle pre condizioni precedenti
-- post: viene aggiornato il riassunto per il documento riportato, e crea una riga
--       di log di informazione all'utente di avvenuto ribaltamento. La procedura procede
--       al successivo, se esiste, documento da processare
--
-- Parametri:
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aPg_exec -> numero progressivo del batch

/* 02.01.2006 inizio procedura modificata */

procedure vsx_riportoNextDocCont(aPgCall number, aPg_exec NUMBER, inCDS VARCHAR2) is
aObb obbligazione%rowtype;
aAcc accertamento%rowtype;
aDescDocCont varchar2(500);
isRibaltabile char(1);
aUser varchar2(20);
aTSNow date;
aEs number;
aAccNew accertamento%rowtype;
aObbNew obbligazione%rowtype;
ERRORE CHAR(1) := 'N';
aCd_elemento_voce VARCHAR2(50);

Begin

select distinct esercizio_ribaltamento, utcr
into aEs, aUser
from vsx_chiusura
where pg_call = aPgCall;

aTSNow := sysdate;

-- 03.01.2006 STANI
IBMUTL001.LOCK_TRANSACTION;

Savepoint CNRCTB047_SP_001;

-- ciclo per tutti i CDS (per ribaltamento cds x cds, quindi nel nostro caso, inutile)

For aCds in (select * from v_unita_organizzativa_valida
	     where esercizio = aEs - 1 And
	           fl_cds = 'Y' And
	           cd_unita_organizzativa = inCDS
	     order by cd_unita_organizzativa) loop

-- prendo tutti i documenti presenti nel cursore da ribaltare per il CDS ciclato

For aDocCont in (select * from vsx_chiusura
	 	 where pg_call = aPgCall And
	 	       cd_cds_origine  = aCds.cd_unita_organizzativa) loop

--Savepoint CNRCTB047_SP_001;

Begin

  	if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then  -- accertamenti
 		aAcc.cd_cds := aDocCont.cd_cds;
  		aAcc.esercizio := aDocCont.esercizio;
  		aAcc.esercizio_originale := aDocCont.esercizio_ori_acc_obb;
		aAcc.pg_accertamento := aDocCont.pg_acc_obb;
  		-- verifica della validitא del documento e lock della risorsa
  		CNRCTB035.LOCKDOCFULLCHECK(aAcc,aDocCont.pg_ver_rec_doc);
  		aDescDocCont := 'Accertamento '||CNRCTB035.GETDESC(aAcc);
  		cnrctb046.riportoEsNextAcc(aAcc, null,aUser, aTSNow);
  	else  -- obbligazioni
  		aObb.cd_cds := aDocCont.cd_cds;
  		aObb.esercizio := aDocCont.esercizio;
  		aObb.esercizio_originale := aDocCont.esercizio_ori_acc_obb;
		aObb.pg_obbligazione := aDocCont.pg_acc_obb;
  		-- verifica della validitא del documento e lock della risorsa
  		CNRCTB035.LOCKDOCFULLCHECK(aObb,aDocCont.pg_ver_rec_doc);
  		aDescDocCont := cnrutil.getLabelObbligazione()||' '||CNRCTB035.GETDESC(aObb);
  		cnrctb046.riportoEsNextObb(aObb, null, null, aUser, aTSNow);
  	end if;

        -- logging
	if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then
	  aAccNew := cnrctb048.GETDOCRIPORTATO(aAcc);
	     if aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES then
	 	declare
		  lNum number;
		begin
		  select 1 into lNum
		  from dual
		  where exists (select 1 from accertamento_scad_voce
			        where cd_cds  = aAcc.cd_cds And
                                      esercizio = aAcc.esercizio And
                                      esercizio_originale = aAcc.esercizio_originale And
                                      pg_accertamento = aAcc.pg_accertamento And
                                      cd_centro_responsabilita = CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE) And
                                      cd_linea_attivita = CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE));

  		  ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio con linea di attivitא di sistema. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'', 'N');
		exception when NO_DATA_FOUND then
		  ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'', 'N');
		end;
	     Elsif aAcc.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_ACC, CNRCTB018.TI_DOC_ACC_PLUR) then
		   ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'', 'N');
	     Else  -- ACR_PGIRO
	           ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Annotazione su partita di giro '||aDescDocCont||': ט stato riportata al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'', 'N');
	     End if;

  	else -- spese
		aObbNew := cnrctb048.GETDOCRIPORTATO(aObb);
		if aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_PGIRO then
		   ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Annotazione su partita di giro '||aDescDocCont||': ט stato riportata al nuovo esercizio con numero '||aObbNew.pg_obbligazione||'. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'', 'N');
		elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB then
		   ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',cnrutil.getLabelObbligazione()||' '||aDescDocCont||': ט stato riportata al nuovo esercizio con numero '||aObbNew.pg_obbligazione||'. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'', 'N');
		elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_RES_PRO then
		   ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',cnrutil.getLabelObbligazione()||' di tipo "Residuo proprio" '||aDescDocCont||': ט stato riportata al nuovo esercizio con numero '||aObbNew.pg_obbligazione||'. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'', 'N');
		elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_RES_IMPRO then
		   ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',cnrutil.getLabelObbligazione()||' di tipo "Residuo improprio" '||aDescDocCont||': ט stato riportata al nuovo esercizio con numero '||aObbNew.pg_obbligazione||'. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'', 'N');
		else -- impegni
		   ibmutl200.LOGINF_TEMP(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Impegno '||aDescDocCont||': ט stato riportata al nuovo esercizio con numero '||aObbNew.pg_obbligazione||'. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'', 'N');
		end if;
	end if;

-- 	if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then
-- 	   aggiornaRiassunto(aAcc, DOC_RIPORTATO);
-- 	else
-- 	   aggiornaRiassunto(aObb, DOC_RIPORTATO);
-- 	end if;

--        commit;

Exception when OTHERS then
--    rollback to savepoint CNRCTB047_SP_001;
    ERRORE := 'Y';
    if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then
	  aggiornaRiassunto(aAcc, DOC_NON_RIPORTATO);
    else
	  aggiornaRiassunto(aObb, DOC_NON_RIPORTATO);
    end if;
    IBMUTL200.LOGERR_TEMP(aPg_exec,
                     DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',
                     'Riporto documento '||aDescDocCont||' fallito: '||DBMS_UTILITY.FORMAT_ERROR_STACK,
                     '',
                     'N');

End;

End loop; -- ciclo sui documenti

Begin
If CNRCTB048.getcdsribaltato (aEs-1, inCDS) = 'N' Then
  ribalta_disp_improprie (aEs-1, inCDS, aUser,aPg_exec);
End If;

Update  parametri_cds
Set     fl_ribaltato = 'Y'
Where   ESERCIZIO = aEs-1 And
        CD_CDS = INcds;
Exception

When Others Then
    ERRORE := 'Y';
    IBMUTL200.LOGERR_TEMP(aPg_exec,
                     DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',
                     'Ribaltamento disponibilitא improprie fallito: '||DBMS_UTILITY.FORMAT_ERROR_STACK,
                     '',
                     'N');
End;

End loop; -- ciclo sui cds

 If Nvl(ERRORE, 'N') = 'Y' Then
    rollback to savepoint CNRCTB047_SP_001;
 Else
    IBMUTL001.DEFERRED_COMMIT;
 End If;
End;

/* 02.01.2006 fine procedura modificata */

procedure job_riporto_next_doc_cont(job number, pg_exec number, next_date date, aPgCall number, aUser VARCHAR2, aCDS VARCHAR2) is
Str varchar2(4000);
begin
-- inserito il 09.01.2005
IBMUTL001.LOCK_TRANSACTION;

-- stani: questo committa all'interno
-- fa un update di BATCH_LOG_TSTA e poi committa

	IBMUTL210.logStartExecutionUpd_TEMP (pg_exec, LOG_TIPO_RIP_NEXT, job,
          'Richiesta utente: '||aUser,DS_RIPORTO_NEXT||' Start:'||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'));

-- mette una serie di variabili a 0
	inizializzaRiassunto;

	begin
		vsx_riportoNextDocCont(aPgCall, pg_exec, aCDS); -- ribaltamento vero e proprio
		delete_vsx(aPgCall);
-- stani: perchט non committa dentro
                IBMUTL001.DEFERRED_COMMIT;
		Str := riassunto;
		-- Messaggio all'utente di operazione completata

-- stani: anche questo committa dentro

  If IBMUTL200.get_err_batch_log_riga_temp Then
		IBMUTL205.LOGINF(DS_RIPORTO_NEXT,
		    DS_RIPORTO_NEXT||' '||
		    to_char(sysdate,'DD/MM/YYYY HH:MI:SS'),'Operazione completata SENZA il ribaltamento dei documenti (verificare i documenti in errore nella funzione: '||
' "Funzionalitא di servizio/Logs Applicativi/Visualizzazione" per il Progressivo Esecuzione '||
		    pg_exec||')',
		    aUser);
  Else
		IBMUTL205.LOGINF(DS_RIPORTO_NEXT,
		    DS_RIPORTO_NEXT||' '||
		    to_char(sysdate,'DD/MM/YYYY HH:MI:SS'),'Operazione completata CON il ribaltamento dei documenti (i documenti ribaltati sono consultabili nella funzione: '||
' "Funzionalitא di servizio/Logs Applicativi/Visualizzazione" per il Progressivo Esecuzione '||
		    pg_exec||')',
		    aUser);
  End If;

	exception when others then
		rollback;
		delete_vsx(aPgCall);
-- stani: perchט non committa dentro
                Commit;
		-- Messaggio di attenzione all'utente
		IBMUTL205.LOGWAR(DS_RIPORTO_NEXT,
		       DS_RIPORTO_NEXT||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_exec||')',
		       DBMS_UTILITY.FORMAT_ERROR_STACK,
		       aUser);
	end;

--IBMUTL200.scarica_temp_in_BATCH_LOG_TSTA;
IBMUTL200.scarica_temp_in_BATCH_LOG_RIGA;
Commit;

end;

-- vsx_riportoPrevDocCont
-- ----------------------
-- pre-post name: riporto all'esercizio origine - risorsa non piש valida
-- pre: il documento contabile in processo ט stato nel frattempo modificato
-- post: viene creata una riga di log di errore. La procedura procede al successivo,
--       se esiste, documento da processare
--
-- pre-post name: riporto all'esercizio origine - errore nel riportare il documento
-- pre: il documento contabile in processo ט ancora valido, viene sollevato un errore
--      nella procedura di riporto all'esercizio origine
-- post: viene creata una riga di log dell'errore sollevato. La procedura procede
--       al successivo, se esiste, documento da processare.
--
-- pre-post name: riporto all'esercizio origine - operazione eseguita
-- pre: nessuna delle pre condizioni precedenti creata una riga di log di
--      informazione all'utente di avvenuto ribaltamento. La procedura procede al successivo,
--      se esiste, documento da processare
--
-- Parametri:
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aPg_exec -> numero progressivo del batch
procedure vsx_riportoPrevDocCont(aPgCall number, aPg_exec number) is
aObb obbligazione%rowtype;
aAcc accertamento%rowtype;
aDescDocCont varchar2(500);
aUser varchar2(20);
aTSNow date;
aEs number;
begin
	select distinct utcr, esercizio into aUser, aEs
	from vsx_chiusura
	where pg_call = aPgCall;

	aTSNow := sysdate;

	for aCds in (select * from v_unita_organizzativa_valida
			 	 where esercizio = aEs
				   and fl_cds = 'Y'
				 order by cd_unita_organizzativa) loop
		for aDocCont in (select * from vsx_chiusura
					 	 where pg_call = aPgCall
						   and cd_cds_origine  = aCds.cd_unita_organizzativa) loop
			savepoint CNRCTB047_SP_003;
			begin
				if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then  -- accertamenti
				    aAcc.cd_cds := aDocCont.cd_cds;
					aAcc.esercizio := aDocCont.esercizio;
					aAcc.esercizio_originale := aDocCont.esercizio_ori_acc_obb;
					aAcc.pg_accertamento := aDocCont.pg_acc_obb;

					-- verifica della validitא della risorsa
					-- lock del documento
					CNRCTB035.LOCKDOCFULLCHECK(aAcc,aDocCont.pg_ver_rec_doc);

					aDescDocCont := 'Accertamento '||CNRCTB035.GETDESC(aAcc);

					cnrctb046.DERIPORTOESNEXTACC(aAcc,aUser,aTSNow);

				else  -- obbligazioni
					aObb.cd_cds := aDocCont.cd_cds;
					aObb.esercizio := aDocCont.esercizio;
					aObb.esercizio_originale := aDocCont.esercizio_ori_acc_obb;
					aObb.pg_obbligazione := aDocCont.pg_acc_obb;

					-- verifica della validitא del documento
					-- e lock della risorsa
					CNRCTB035.LOCKDOCFULLCHECK(aObb,aDocCont.pg_ver_rec_doc);

					aDescDocCont := cnrutil.getLabelObbligazione()||' '||CNRCTB035.GETDESC(aObb);

					cnrctb046.DERIPORTOESNEXTOBB(aObb, aUser, aTSNow);

				end if;
				commit;
				ibmutl200.LOGINF(aPg_exec,DS_RIPORTO_PREV||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',aDescDocCont||': ט stato riportato all''esercizio precedente','');
			exception when OTHERS then
				rollback to savepoint CNRCTB047_SP_003;
				IBMUTL200.LOGERR(aPg_exec,DS_RIPORTO_PREV||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',aDescDocCont||': '||DBMS_UTILITY.FORMAT_ERROR_STACK,'');
			end;
		end loop;  -- ciclo sui documenti
	end loop; -- ciclo sui cds

end;

procedure job_riporto_prev_doc_cont(job number, pg_exec number, next_date date, aPgCall number, aUser varchar2) is
begin
-- updata batch_log_tsta e committa
	IBMUTL210.logStartExecutionUpd (pg_exec,
									LOG_TIPO_RIP_PREV,
									job,
									'Richista utente: '||aUser,
									DS_RIPORTO_PREV||' Start:'||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'));
	begin
		vsx_riportoPrevDocCont(aPgCall, pg_exec);
		delete_vsx(aPgCall);
		commit;
		-- Messaggio all'utente di operazione completata
		IBMUTL205.LOGINF(DS_RIPORTO_PREV, DS_RIPORTO_PREV||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'),'Operazione completata (pg_exec='||pg_exec||')',aUser);
	exception when others then
		rollback;
		delete_vsx(aPgCall);
		commit;
		-- Messaggio di attenzione all'utente
		IBMUTL205.LOGWAR(DS_RIPORTO_PREV, DS_RIPORTO_PREV||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_exec||')',DBMS_UTILITY.FORMAT_ERROR_STACK,aUser);
	end;
end;

-- vsx_riportoNextDocContVoce
-- --------------------------
-- pre-post name: riporto all'esercizio successivo con cambio imputazione finanziaria -
--                elemento voce non valido
-- pre: l'elemento voce specificato nella chiamata alla procedura non esiste
-- post: viene creata un messaggio di errore all'utente, e vengono eliminati i record
--       dalla vista vsx_chiusura
--
-- pre-post name: riporto all'esercizio successivo con cambio imputazione finanziaria -
--                voce_f non valido
-- pre: la voce_f specificata nella chiamata alla procedura non esiste
-- post: viene creata un messaggio di errore all'utente, e vengono eliminati i record
--       dalla vista vsx_chiusura
--
-- pre-post name: riporto all'esercizio successivo con cambio imputazione finanziaria -
--                risorsa non piש valida
-- pre: il documento contabile in processo ט stato nel frattempo modificato
-- post: viene creata una riga di log di errore. La procedura procede al successivo,
--       se esiste, documento da processare
--
-- pre-post name: riporto all'esercizio successivo con cambio imputazione finanziaria -
--                errore nel riportare il documento
-- pre: il documento contabile in processo ט ancora valido, viene sollevato un errore
--      nella procedura di riporto all'esercizio origine
-- post: viene creata una riga di log dell'errore sollevato. La procedura procede
--       al successivo, se esiste, documento da processare.
--
-- pre-post name: riporto all'esercizio successivo con cambio imputazione finanziaria -
--                operazione eseguita
-- pre: nessuna delle pre condizioni precedenti creata una riga di log di
--      informazione all'utente di avvenuto ribaltamento. La procedura procede al successivo,
--      se esiste, documento da processare
--
-- Parametri:
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aPg_exec -> numero progressivo del batch
procedure vsx_riportoNextDocContVoce(aPgCall number, aPg_exec number) is
aObb obbligazione%rowtype;
aAcc accertamento%rowtype;
aDescDocCont varchar2(500);
isRibaltabile char(1);
aUser varchar2(20);
aTSNow date;
aEs number;
aEsDoc number;
aTiGestione char(1);
aTiAppartenenza char(1);
aCdElementoVoce varchar2(20);
aCdVoce varchar2(50);
aElementoVoce elemento_voce%rowtype;
aVoceF voce_f%rowtype;
aObbNew obbligazione%rowtype;
aAccNew accertamento%rowtype;
begin
	select distinct esercizio,
		   			ti_gestione,
					ti_appartenenza,
					cd_elemento_voce,
					cd_voce,
					esercizio_ribaltamento,
					utcr
	into aEsDoc,
		 aTiGestione,
		 aTiAppartenenza,
		 aCdElementoVoce,
		 aCdVoce,
		 aEs,
		 aUser
	from vsx_chiusura
	where pg_call = aPgCall;

	aTSNow := sysdate;

	if aCdVoce is null then
	-- ט un elemento_voce
	   begin
		   select * into aElementoVoce
		   from elemento_voce
		   where esercizio        = aEsDoc + 1
		     and ti_appartenenza  = aTiAppartenenza
			 and ti_gestione	  = aTiGestione
			 and cd_elemento_voce = aCdElementoVoce;
	   exception when NO_DATA_FOUND then
	   	   ibmerr001.RAISE_ERR_GENERICO('Elemento voce non valido');
	   end;
	else -- ט una voce_f, solo per impegni non pgiro
	   begin
	   	   select * into aVoceF
		   from voce_f
		   where esercizio        = aEsDoc + 1
		     and ti_appartenenza  = aTiAppartenenza
			 and ti_gestione		  = aTiGestione
			 and cd_voce 		  = aCdVoce;
	   exception when NO_DATA_FOUND then
	   	   ibmerr001.RAISE_ERR_GENERICO('Voce finanaziaria non valida');
	   end;
	end if;

	for aCds in (select * from v_unita_organizzativa_valida
			 	 where esercizio = aEsDoc
				   and fl_cds = 'Y'
				 order by cd_unita_organizzativa) loop
		for aDocCont in (select * from vsx_chiusura
					 	 where pg_call = aPgCall
						   and cd_cds_origine  = aCds.cd_unita_organizzativa) loop
			savepoint CNRCTB047_SP_004;
			begin
				if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then  -- accertamenti

					aAcc.cd_cds := aDocCont.cd_cds;
					aAcc.esercizio := aDocCont.esercizio;
					aAcc.esercizio_originale := aDocCont.esercizio_ori_acc_obb;
					aAcc.pg_accertamento := aDocCont.pg_acc_obb;

					-- verifica della validitא del documento
					-- e lock della risorsa
					CNRCTB035.LOCKDOCFULLCHECK(aAcc,aDocCont.pg_ver_rec_doc);

					aDescDocCont := 'Accertamento '||CNRCTB035.GETDESC(aAcc);

					CNRCTB046.riportoEsNextAcc(aAcc,aElementoVoce,aUser,aTSNow);

				else  -- obbligazioni
					aObb.cd_cds := aDocCont.cd_cds;
					aObb.esercizio := aDocCont.esercizio;
					aObb.esercizio_originale := aDocCont.esercizio_ori_acc_obb;
					aObb.pg_obbligazione := aDocCont.pg_acc_obb;

					-- verifica della validitא del documento
					-- e lock della risorsa
					CNRCTB035.LOCKDOCFULLCHECK(aObb,aDocCont.pg_ver_rec_doc);

					aDescDocCont := cnrutil.getLabelObbligazione()||' '||CNRCTB035.GETDESC(aObb);

					CNRCTB046.riportoEsNextObb(aObb,aElementoVoce,aVoceF,aUser,aTSNow);

				end if;

				-- logging
				if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then
				    aAccNew := cnrctb048.GETDOCRIPORTATO(aAcc);
					if aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES then
						declare
						  lNum number;
						begin
							select 1 into lNum
							from dual
						    where exists (select 1 from accertamento_scad_voce
						   		   Where cd_cds = aAcc.cd_cds
								     	 And esercizio 		  = aAcc.esercizio
									 And esercizio_originale  = aAcc.esercizio_originale
									 And pg_accertamento 	  = aAcc.pg_accertamento
									 And cd_centro_responsabilita  = CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE)
									 And cd_linea_attivita	  = CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE));
							ibmutl200.LOGINF(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio con linea di attivitא di sistema. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'');
						exception when NO_DATA_FOUND then
							ibmutl200.LOGINF(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'');
						end;
					elsif aAcc.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_ACC, CNRCTB018.TI_DOC_ACC_PLUR) then
						ibmutl200.LOGINF(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'');
					else  -- ACR_PGIRO
						ibmutl200.LOGINF(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Annotazione su partita di giro '||aDescDocCont||': ט stato riportata al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'');
					end if;
				else -- spese
					aObbNew := cnrctb048.GETDOCRIPORTATO(aObb);
					if aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_PGIRO then
					   ibmutl200.LOGINF(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Annotazione su partita di giro '||aDescDocCont||': ט stata riportata al nuovo esercizio. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'');
					elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_RES_PRO then
					   ibmutl200.LOGINF(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',cnrutil.getLabelObbligazione()||' '||aDescDocCont||': ט stata riportata al nuovo esercizio. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'');
					else -- impegni
					   ibmutl200.LOGINF(aPg_exec,DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Impegno '||aDescDocCont||': ט stato riportato al nuovo esercizio. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'');
					end if;
				end if;

-- 				if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then
-- 				   aggiornaRiassunto(aAcc, DOC_RIPORTATO);
-- 				else
-- 				   aggiornaRiassunto(aObb, DOC_RIPORTATO);
-- 				end if;
				commit;
			exception when OTHERS then
			    rollback to savepoint CNRCTB047_SP_004;
				if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then
				   aggiornaRiassunto(aAcc, DOC_NON_RIPORTATO);
				else
				   aggiornaRiassunto(aObb, DOC_NON_RIPORTATO);
				end if;
				IBMUTL200.LOGERR(aPg_exec,DS_RIPORTO_NEXT_VOCE||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Riporto documento '||aDescDocCont||': '||DBMS_UTILITY.FORMAT_ERROR_STACK,'');
			end;

		end loop; -- ciclo sui documenti
	end loop; -- ciclo sui cds
end;

procedure job_riporto_next_doc_cont_voce(job number, pg_exec number, next_date date, aPgCall number, aUser varchar2) is
Str varchar2(4000);
begin
-- updata batch_log_tsta e committa
	IBMUTL210.logStartExecutionUpd (pg_exec,
									LOG_TIPO_RIP_NEXT,
									job,
									'Richista utente: '||aUser,
									DS_RIPORTO_NEXT_VOCE||' Start:'||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'));
	inizializzaRiassunto;
	begin
		vsx_riportoNextDocContVoce(aPgCall, pg_exec);
		delete_vsx(aPgCall);
		commit;
		Str := riassunto;
		-- log riassuntivo
--		ibmutl200.LOGINF(pg_exec,DS_RIPORTO_NEXT_VOCE||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_exec||') ',Str,'');
		-- Messaggio all'utente di operazione completata
		IBMUTL205.LOGINF(DS_RIPORTO_NEXT_VOCE, DS_RIPORTO_NEXT_VOCE||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'),'Operazione completata (pg_exec='||pg_exec||')',aUser);
	exception when others then
		rollback;
		delete_vsx(aPgCall);
		commit;
		-- Messaggio di attenzione all'utente
		IBMUTL205.LOGWAR(DS_RIPORTO_NEXT_VOCE, DS_RIPORTO_NEXT_VOCE||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_exec||')',DBMS_UTILITY.FORMAT_ERROR_STACK,aUser);
	end;
end;

procedure annullamentoDocCont(aPgCall number, aUser varchar2) is
aProcedure varchar2(4000);
begin
	aProcedure := 'CNRCTB047.job_annullamento_doc_cont(job, pg_exec, next_date, '||aPgCall||', '''||aUser||''');';
	IBMUTL210.CREABATCHDINAMICO(
		DS_ANNULLAMENTO,
		aProcedure,
		aUser);
	IBMUTL001.DEFERRED_COMMIT;
	IBMERR001.RAISE_ERR_GENERICO('Operazione sottomessa per esecuzione. Al completamento l''utente riceverא un messaggio di notifica dello stato dell''operazione');
end;


-- chiamata da java
procedure riportoNextEsDocCont(aPgCall number, aUser VARCHAR2, aCDS VARCHAR2) is
aProcedure varchar2(4000);
num_job   NUMBER;
pg_exec   NUMBER;

begin
	aProcedure := 'CNRCTB047.job_riporto_next_doc_cont(job, pg_exec, next_date, '||aPgCall||', '''||aUser||''', '''||aCDS||''');';

-- PER POTER DARE
Select ibmseq00_dyna_job.nextval into num_Job from dual;
select IBMSEQ00_BATCH_LOG.nextval into pg_Exec from dual;

	IBMUTL210.CREABATCHDINAMICO(
		DS_RIPORTO_NEXT,
		aProcedure,
		aUser);
	IBMUTL001.DEFERRED_COMMIT;
	IBMERR001.RAISE_ERR_GENERICO('Operazione sottomessa per esecuzione. Al completamento l''utente riceverא un messaggio di notifica con l''esito dell''operazione, CDS '||aCDS||'. '||
' L''operazione di ribaltamento andrא a buon fine solo se tutti i documenti saranno ribaltati. '||
' In caso contrario verificare le cause del mancato ribaltamento nella funzione: '||
' "Funzionalitא di servizio/Logs Applicativi/Gestione" per il Progressivo Esecuzione '||To_Char(pg_exec+1)||
' e Progressivo Job '||To_Char(num_job+1));
end;

procedure riportoPrevEsDocCont(aPgCall number, aUser varchar2) is
aProcedure varchar2(4000);
begin
	aProcedure := 'CNRCTB047.job_riporto_prev_doc_cont(job, pg_exec, next_date, '||aPgCall||', '''||aUser||''');';
	IBMUTL210.CREABATCHDINAMICO(
		DS_RIPORTO_PREV,
		aProcedure,
		aUser);
	IBMUTL001.DEFERRED_COMMIT;
	IBMERR001.RAISE_ERR_GENERICO('Operazione sottomessa per esecuzione. Al completamento l''utente riceverא un messaggio di notifica dello stato dell''operazione');
end;

procedure riportoNextEsDocContVoce(aPgCall number, aUser varchar2) is
aProcedure varchar2(4000);
begin
	aProcedure := 'CNRCTB047.job_riporto_next_doc_cont_voce(job, pg_exec, next_date, '||aPgCall||', '''||aUser||''');';
	IBMUTL210.CREABATCHDINAMICO(
		DS_RIPORTO_NEXT_VOCE,
		aProcedure,
		aUser);
	IBMUTL001.DEFERRED_COMMIT;
	IBMERR001.RAISE_ERR_GENERICO('Operazione sottomessa per esecuzione. Al completamento l''utente riceverא un messaggio di notifica dello stato dell''operazione');
end;

procedure ribaltamentoMassivo(aEs number) is
aUser varchar2(20) := '$$$$RIBALTAMENTO$$$$';
aTSNow date := sysdate;
aPg_exec number;
aObb obbligazione%rowtype;
aObbNew obbligazione%rowtype;
aAcc accertamento%rowtype;
aAccNew accertamento%rowtype;
esisteEsChiuso boolean;
aDescDocCont varchar2(500);
begin
	-- inizializza il log
-- inserisce in BATCH_LOG_TSTA e committa
	aPg_exec := ibmutl200.LOGSTART_temp(
					CNRCTB047.LOG_TIPO_RIP_NEXT,
					CNRCTB047.DS_RIPORTO_NEXT||' Start:'||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'),
					'Richista utente: '||aUser,
					aUser,
					null,
					null);
	-- verifico che l'esercizio contabile per tutti i cds sia aperto
	esisteEsChiuso := false;
	for aCds in (select * from v_unita_organizzativa_valida
			 	 where esercizio = aEs
				   and fl_cds = 'Y') loop
		if cnrctb008.ISESERCIZIOCHIUSO(aEs,aCds.cd_unita_organizzativa) then
		   esisteEsChiuso := true;
		   ibmutl200.LOGWAR_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Esercizio '||aEs||' per cds '||aCds.cd_unita_organizzativa||' chiuso.','');
		end if;
	end loop; -- cds
	if esisteEsChiuso then
	   ibmerr001.RAISE_ERR_GENERICO('Esiste cds con esercizio chiuso');
	end if;

	for aCds in (select /*+ORDERED*/ * from v_unita_organizzativa_valida
			 	 where esercizio = aEs
				   and fl_cds 	 = 'Y'
				   and cnrctb008.GETSTATOESERCIZIO(aEs,cd_unita_organizzativa) = cnrctb008.STATO_APERTURA
				 order by cd_unita_organizzativa) loop
		for aDocCont in (select * from v_obb_acc_riporta
					 	 where cd_cds    = aCds.cd_unita_organizzativa
						   and esercizio = aEs) loop
		  begin
			savepoint SCRIPT_SP_001;
			if aDocCont.ti_gestione = CNRCTB001.GESTIONE_ENTRATE then
			   aAcc.cd_cds 			:= aDocCont.cd_cds;
			   aAcc.esercizio 		:= aDocCont.esercizio;
			   aAcc.esercizio_originale     := aDocCont.esercizio_ori_acc_obb;
			   aAcc.pg_accertamento := aDocCont.pg_acc_obb;
			   cnrctb035.LOCKDOCFULL(aAcc);
			   aDescDocCont := 'Accertamento '||CNRCTB035.GETDESC(aAcc);
			   cnrctb046.riportoEsNextAcc(aAcc, null,aUser, aTSNow);
			else -- spese
			   aObb.cd_cds 			:= aDocCont.cd_cds;
			   aObb.esercizio 		:= aDocCont.esercizio;
			   aObb.esercizio_originale     := aDocCont.esercizio_ori_acc_obb;
			   aObb.pg_obbligazione := aDocCont.pg_acc_obb;
			   cnrctb035.LOCKDOCFULL(aObb);
			   aDescDocCont := cnrutil.getLabelObbligazione()||' '||CNRCTB035.GETDESC(aObb);
			   cnrctb046.riportoEsNextObb(aObb, null, null, aUser, aTSNow);
			end if;

			-- logging
			if aDocCont.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE then
			    aAccNew := cnrctb048.GETDOCRIPORTATO(aAcc);
				if aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES then
					declare
					  lNum number;
					begin
						select 1 into lNum
						from dual
					    where exists (select 1 from accertamento_scad_voce
					   		  where cd_cds 		= aAcc.cd_cds
							    And esercizio 	= aAcc.esercizio
							    And esercizio_originale = aAcc.esercizio_originale
						            And pg_accertamento = aAcc.pg_accertamento
							    And cd_centro_responsabilita  = CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE)
						            And cd_linea_attivita = CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE));
						ibmutl200.LOGINF_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio con linea di attivitא di sistema. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'', 'N');
					exception when NO_DATA_FOUND then
						ibmutl200.LOGINF_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'', 'N');
					end;
				elsif aAcc.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_ACC, CNRCTB018.TI_DOC_ACC_PLUR) then
					ibmutl200.LOGINF_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Accertamento '||aDescDocCont||': ט stato riportato al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'', 'N');
				else  -- ACR_PGIRO
					ibmutl200.LOGINF_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Annotazione su partita di giro '||aDescDocCont||': ט stato riportata al nuovo esercizio. Importo accertamento origine '||aAcc.im_accertamento||' importo riportato '||aAccNew.im_accertamento,'', 'N');
				end if;
			else -- spese
				aObbNew := cnrctb048.GETDOCRIPORTATO(aObb);
				if aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_PGIRO then
				   ibmutl200.LOGINF_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Annotazione su partita di giro '||aDescDocCont||': ט stato riportata al nuovo esercizio. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'', 'N');
				elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_RES_PRO then
				   ibmutl200.LOGINF_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',cnrutil.getLabelObbligazione()||' '||aDescDocCont||': ט stata riportata al nuovo esercizio. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'', 'N');
				else -- impegni
				   ibmutl200.LOGINF_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Impegno '||aDescDocCont||': ט stato riportato al nuovo esercizio. Importo '||cnrutil.getLabelObbligazioneMin()||' origine '||aObb.im_obbligazione||' importo riportato '||aObbNew.im_obbligazione,'', 'N');
				end if;
			end if;
			commit;
		  exception when OTHERS then
		  	rollback to savepoint SCRIPT_SP_001;
			IBMUTL200.LOGERR_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Riporto documento '||aDescDocCont||' fallito: '||DBMS_UTILITY.FORMAT_ERROR_STACK,'', 'N');
		  end;

		end loop; -- ciclo sui doc cont
	end loop; -- ciclo sui cds

	ibmutl200.LOGINF_TEMP(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ','Ribaltamento massivo documenti contabili completato','', 'N');

exception when OTHERS then
	ibmutl200.LOGERR(aPg_exec,cnrctb047.DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||aPg_exec||') ',DBMS_UTILITY.FORMAT_ERROR_STACK,'');
end;

procedure job_ribalta_massivo_doc_cont(job number, pg_exec number, next_date date, aPgCall number, aUser varchar2, aEs number) Is
Str varchar2(4000);
Begin
-- updata batch_log_tsta e committa
	IBMUTL210.logStartExecutionUpd_TEMP (pg_exec,
					LOG_TIPO_RIP_NEXT,
					job,
					'Richiesta utente: '||aUser,
					DS_RIPORTO_NEXT||' Start:'||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'));

	inizializzaRiassunto;
	begin
	   	cnrctb047.RIBALTAMENTOMASSIVO(aEs);
		--delete_vsx(aPgCall);
	    	commit;
	   	Str := riassunto;
		-- log riassuntivo
--		ibmutl200.LOGINF(pg_exec,DS_RIPORTO_NEXT||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_exec||') ',Str,'');
		-- Messaggio all'utente di operazione completata
		IBMUTL205.LOGINF(DS_RIPORTO_NEXT, DS_RIPORTO_NEXT||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'),'Operazione completata (pg_exec='||pg_exec||')',aUser);
	exception when others then
		rollback;
		delete_vsx(aPgCall);
		commit;
		-- Messaggio di attenzione all'utente
		IBMUTL205.LOGWAR(DS_RIPORTO_NEXT, DS_RIPORTO_NEXT||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_exec||')',DBMS_UTILITY.FORMAT_ERROR_STACK,aUser);
	end;
End;

procedure ribalta_disp_improprie (aEs NUMBER, aCDS VARCHAR2, aUser VARCHAR2,pg_esec number default null) Is
ADELTASALDO VOCE_F_SALDI_CDR_LINEA%Rowtype;
aSogliaPar      NUMBER;
livCofog        number:=0;
esisteCofog number:=0;
recParametriCNR      PARAMETRI_CNR%Rowtype;
Begin
recParametriCNR := CNRUTL001.getRecParametriCnr(aEs+1);

if recParametriCNR.esercizio is null Then
  ibmerr001.RAISE_ERR_GENERICO('Ribaltamento disponibilitא improprie fallito, manca la configurazione per esercizio '||to_char(aEs+1));
End If;

livCofog := recParametriCNR.LIVELLO_PDG_COFOG;

Select Nvl(IM_SOGLIA_RIBALTAMENTO_RES_IMP, 0)
Into   aSogliaPar
From   parametri_cds
Where  esercizio = aEs And
       cd_cds = aCDS;
For disp_imp_da_ribaltare In (Select ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA,
                                     CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE,
                                     Sum(TOT_IM_RESIDUI_RICOSTRUITI) TOT_IM_RESIDUI_RICOSTRUITI,
                                     Sum(TOT_IM_STANZ_INIZIALE_A1)   TOT_IM_STANZ_INIZIALE_A1,
                                     Sum(TOT_VARIAZIONI_PIU)         TOT_VARIAZIONI_PIU,
                                     Sum(TOT_VARIAZIONI_MENO)        TOT_VARIAZIONI_MENO,
                                     Sum(TOT_IM_OBBL_ACC_COMP)       TOT_IM_OBBL_ACC_COMP,
                                     Sum(TOT_IM_STANZ_RES_IMPROPRIO) TOT_IM_STANZ_RES_IMPROPRIO,
                                     Sum(TOT_VAR_PIU_STANZ_RES_IMP)  TOT_VAR_PIU_STANZ_RES_IMP,
                                     Sum(TOT_VAR_MENO_STANZ_RES_IMP) TOT_VAR_MENO_STANZ_RES_IMP,
                                     Sum(TOT_IM_OBBL_RES_IMP)        TOT_IM_OBBL_RES_IMP,
                                     Sum(TOT_VAR_PIU_OBBL_RES_IMP)   TOT_VAR_PIU_OBBL_RES_IMP,
                                     Sum(TOT_VAR_MENO_OBBL_RES_IMP)  TOT_VAR_MENO_OBBL_RES_IMP,
                                     Sum(TOT_VAR_PIU_OBBL_RES_PRO)   TOT_VAR_PIU_OBBL_RES_PRO,
                                     Sum(TOT_VAR_MENO_OBBL_RES_PRO)  TOT_VAR_MENO_OBBL_RES_PRO
                               From  v_disp_res_improprie
                               Where esercizio = aEs And
                                     TI_GESTIONE = CNRCTB001.GESTIONE_SPESE And
                                     CNRUTL001.GETCDSFROMCDR(CD_CENTRO_RESPONSABILITA) = aCDS
                               Group By ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA,
                                      CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE) Loop

--If disp_imp_da_ribaltare.ESERCIZIO = disp_imp_da_ribaltare.ESERCIZIO_RES Then
 -- COMPETENZA

  If  disp_imp_da_ribaltare.TOT_IM_RESIDUI_RICOSTRUITI +
      -- RESIDUO DELLA COMPETENZA: ASSESTATO - IMPEGNATO A COMPETENZA
      disp_imp_da_ribaltare.tot_IM_STANZ_INIZIALE_A1 + disp_imp_da_ribaltare.tot_VARIAZIONI_PIU - disp_imp_da_ribaltare.tot_VARIAZIONI_MENO -
      disp_imp_da_ribaltare.tot_IM_OBBL_ACC_COMP +
      -- RESIDUO DEI RESIDUI: STANZIAMENTO RESIDUO ASSESTATO +/- VARIAZIONI AI RESIDUI PROPRI INVERTITE
      disp_imp_da_ribaltare.TOT_IM_STANZ_RES_IMPROPRIO + disp_imp_da_ribaltare.TOT_VAR_PIU_STANZ_RES_IMP - disp_imp_da_ribaltare.TOT_VAR_MENO_STANZ_RES_IMP -
      disp_imp_da_ribaltare.TOT_IM_OBBL_RES_IMP + disp_imp_da_ribaltare.TOT_VAR_MENO_OBBL_RES_PRO - disp_imp_da_ribaltare.TOT_VAR_PIU_OBBL_RES_PRO != 0 And
      disp_imp_da_ribaltare.TOT_IM_RESIDUI_RICOSTRUITI +
      -- RESIDUO DELLA COMPETENZA: ASSESTATO - IMPEGNATO A COMPETENZA
      disp_imp_da_ribaltare.tot_IM_STANZ_INIZIALE_A1 + disp_imp_da_ribaltare.tot_VARIAZIONI_PIU - disp_imp_da_ribaltare.tot_VARIAZIONI_MENO -
      disp_imp_da_ribaltare.tot_IM_OBBL_ACC_COMP +
      -- RESIDUO DEI RESIDUI: STANZIAMENTO RESIDUO ASSESTATO +/- VARIAZIONI AI RESIDUI PROPRI INVERTITE
      disp_imp_da_ribaltare.TOT_IM_STANZ_RES_IMPROPRIO + disp_imp_da_ribaltare.TOT_VAR_PIU_STANZ_RES_IMP - disp_imp_da_ribaltare.TOT_VAR_MENO_STANZ_RES_IMP -
      disp_imp_da_ribaltare.TOT_IM_OBBL_RES_IMP + disp_imp_da_ribaltare.TOT_VAR_MENO_OBBL_RES_PRO - disp_imp_da_ribaltare.TOT_VAR_PIU_OBBL_RES_PRO > aSogliaPar Then

	if(disp_imp_da_ribaltare.cd_centro_responsabilita!= cnrctb020.getCdCdrEnte ) then
		 -- 06/11/2013 Rospuc controllo indicazione Cofog prima di procedere al ribaltamento
		if(livCofog!=0 ) then
		  	If (pg_esec is not null) then
				Begin
				  select count(0) into esisteCofog
				  from linea_attivita
          		  where cd_cofog is not null
          		  and 	cd_centro_responsabilita = disp_imp_da_ribaltare.cd_centro_responsabilita
          		  and  	cd_linea_attivita  = disp_imp_da_ribaltare.cd_linea_attivita;

			      if (esisteCofog=0 ) then
			      	ibmerr001.RAISE_ERR_GENERICO('Ribaltamento disponibilità improprie fallito manca il cofog sulla gae '||disp_imp_da_ribaltare.cd_centro_responsabilita||'/'||disp_imp_da_ribaltare.cd_linea_attivita);
			      end if;
			   	Exception
				  When Others Then
				    IBMUTL200.LOGERR_TEMP(pg_esec,
					      DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_esec||') ',
				          'Ribaltamento disponibilità improprie fallito: '||DBMS_UTILITY.FORMAT_ERROR_STACK,
					      '',
					      'N');
				End;
			Else
				-- andando in no_data_found eccezione gestita nella chiamata
				Begin
				  select 1 into livCofog from linea_attivita
		          where cd_cofog is not null
		          and   cd_centro_responsabilita = disp_imp_da_ribaltare.cd_centro_responsabilita
		          and   cd_linea_attivita  = disp_imp_da_ribaltare.cd_linea_attivita;
	        	Exception
		          when no_data_found then
		            IBMERR001.RAISE_ERR_GENERICO('Ribaltamento disponibilità improprie fallito: manca il cofog sulla gae '||disp_imp_da_ribaltare.cd_centro_responsabilita||'/'||disp_imp_da_ribaltare.cd_linea_attivita);
		      	End;
		    End if;
		End if;

		-- 27/11/2015 RafPag controllo presenza progetto
   	    Declare
		  esisteProgetto number:=0;
  	    Begin
		  select count(0) into esisteProgetto
	      from v_linea_attivita_valida
          where esercizio = aEs + 1
          and   cd_centro_responsabilita = disp_imp_da_ribaltare.cd_centro_responsabilita
          and   cd_linea_attivita  = disp_imp_da_ribaltare.cd_linea_attivita
          and   pg_progetto is not null;

 	      If (esisteProgetto=0 ) then
		  	If (pg_esec is not null) then
              Begin
  	        	ibmerr001.RAISE_ERR_GENERICO('Ribaltamento disponibilità improprie fallito manca il progetto sulla gae '||disp_imp_da_ribaltare.cd_centro_responsabilita||'/'||disp_imp_da_ribaltare.cd_linea_attivita||' nell''anno '||to_char(aEs + 1));
			  Exception
			    When Others Then
				  IBMUTL200.LOGERR_TEMP(pg_esec,
					                    DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_esec||') ',
					                    'Ribaltamento disponibilità improprie fallito: '||DBMS_UTILITY.FORMAT_ERROR_STACK,
					                    '',
					                    'N');
			  End;
		    Else
              IBMERR001.RAISE_ERR_GENERICO('Ribaltamento disponibilità improprie fallito: manca il progetto sulla gae '||disp_imp_da_ribaltare.cd_centro_responsabilita||'/'||disp_imp_da_ribaltare.cd_linea_attivita);
 	        End if;
		  End if;
		End;

		-- 04/01/2019 RafPag controllo progetto approvato
   	    Declare
		  esisteProgetto number:=0;
  	    Begin
		  select count(0) into esisteProgetto
	      from v_linea_attivita_valida, progetto_other_field
          where esercizio = aEs + 1
          and   v_linea_attivita_valida.cd_centro_responsabilita = disp_imp_da_ribaltare.cd_centro_responsabilita
          and   v_linea_attivita_valida.cd_linea_attivita  = disp_imp_da_ribaltare.cd_linea_attivita
          and   v_linea_attivita_valida.pg_progetto is not null
          and   v_linea_attivita_valida.pg_progetto = progetto_other_field.pg_progetto
          and   progetto_other_field.stato = 'APP';

 	      If (esisteProgetto=0 ) then
		  	If (pg_esec is not null) then
              Begin
  	        	ibmerr001.RAISE_ERR_GENERICO('Ribaltamento disponibilità improprie fallito: non approvato il progetto sulla gae '||disp_imp_da_ribaltare.cd_centro_responsabilita||'/'||disp_imp_da_ribaltare.cd_linea_attivita||' nell''anno '||to_char(aEs + 1));
			  Exception
			    When Others Then
				  IBMUTL200.LOGERR_TEMP(pg_esec,
					                    DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_esec||') ',
					                    'Ribaltamento disponibilità improprie fallito: '||DBMS_UTILITY.FORMAT_ERROR_STACK,
					                    '',
					                    'N');
			  End;
		    Else
              IBMERR001.RAISE_ERR_GENERICO('Ribaltamento disponibilità improprie fallito: non approvato il progetto sulla gae '||disp_imp_da_ribaltare.cd_centro_responsabilita||'/'||disp_imp_da_ribaltare.cd_linea_attivita);
 	        End if;
		  End if;
		End;

      aDeltaSaldo.ESERCIZIO := aEs + 1;
      aDeltaSaldo.ESERCIZIO_RES := disp_imp_da_ribaltare.ESERCIZIO_RES;
      aDeltaSaldo.CD_CENTRO_RESPONSABILITA := disp_imp_da_ribaltare.CD_CENTRO_RESPONSABILITA;
      aDeltaSaldo.CD_LINEA_ATTIVITA := disp_imp_da_ribaltare.CD_LINEA_ATTIVITA;

      aDeltaSaldo.TI_APPARTENENZA := disp_imp_da_ribaltare.TI_APPARTENENZA;
      aDeltaSaldo.TI_GESTIONE := disp_imp_da_ribaltare.TI_GESTIONE;
      aDeltaSaldo.CD_VOCE := disp_imp_da_ribaltare.CD_VOCE;

	  if nvl(recParametriCNR.fl_nuovo_pdg, 'N') = 'Y' Then
        Declare
           recElementoVoceOld elemento_voce%rowtype;
           recElementoVoceNew elemento_voce%rowtype;
        Begin
          recElementoVoceOld.esercizio := aEs;
          recElementoVoceOld.ti_appartenenza := disp_imp_da_ribaltare.TI_APPARTENENZA;
          recElementoVoceOld.ti_gestione := disp_imp_da_ribaltare.TI_GESTIONE;

          Begin
            Select cd_elemento_voce
            into recElementoVoceOld.cd_elemento_voce
		    from voce_f
		    Where esercizio = aEs
		    And   ti_appartenenza = disp_imp_da_ribaltare.TI_APPARTENENZA
		    And   ti_gestione = disp_imp_da_ribaltare.TI_GESTIONE
		    And   cd_voce = disp_imp_da_ribaltare.CD_VOCE;
		  Exception
		    When no_data_found Then
              recElementoVoceOld.cd_elemento_voce := disp_imp_da_ribaltare.CD_VOCE;
          End;

          If pg_esec is not null then
            Begin
              recElementoVoceNew := CNRCTB046.getElementoVoceNew(recElementoVoceOld);
            Exception
              When Others Then
    			  IBMUTL200.LOGERR_TEMP(pg_esec,
  				                    DS_RIPORTO_NEXT||aEs||' '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_esec||') ',
				                    'Ribaltamento disponibilità improprie fallito: '||DBMS_UTILITY.FORMAT_ERROR_STACK,
				                    '',
				                    'N');
		    End;
		  Else
            recElementoVoceNew := CNRCTB046.getElementoVoceNew(recElementoVoceOld);
          End If;
          If recElementoVoceNew.TI_APPARTENENZA is not null Then
             aDeltaSaldo.TI_APPARTENENZA := recElementoVoceNew.TI_APPARTENENZA;
             aDeltaSaldo.TI_GESTIONE := recElementoVoceNew.TI_GESTIONE;
             aDeltaSaldo.CD_VOCE := recElementoVoceNew.CD_ELEMENTO_VOCE;
          End If;
        End;
      End If;
      aDeltaSaldo.UTUV := aUser;

      CNRCTB054.RESET_IMPORTI_SALDI (aDELTASALDO);

      aDeltaSaldo.IM_STANZ_RES_IMPROPRIO :=

        disp_imp_da_ribaltare.TOT_IM_RESIDUI_RICOSTRUITI +
        -- RESIDUO DELLA COMPETENZA: ASSESTATO - IMPEGNATO A COMPETENZA
        disp_imp_da_ribaltare.tot_IM_STANZ_INIZIALE_A1 + disp_imp_da_ribaltare.tot_VARIAZIONI_PIU - disp_imp_da_ribaltare.tot_VARIAZIONI_MENO -
        disp_imp_da_ribaltare.tot_IM_OBBL_ACC_COMP +
        -- RESIDUO DEI RESIDUI: STANZIAMENTO RESIDUO ASSESTATO +/- VARIAZIONI AI RESIDUI PROPRI INVERTITE
        disp_imp_da_ribaltare.TOT_IM_STANZ_RES_IMPROPRIO + disp_imp_da_ribaltare.TOT_VAR_PIU_STANZ_RES_IMP - disp_imp_da_ribaltare.TOT_VAR_MENO_STANZ_RES_IMP -
        disp_imp_da_ribaltare.TOT_IM_OBBL_RES_IMP + disp_imp_da_ribaltare.TOT_VAR_MENO_OBBL_RES_PRO - disp_imp_da_ribaltare.TOT_VAR_PIU_OBBL_RES_PRO;

      CNRCTB054.CREA_AGGIORNA_SALDI(aDELTASALDO, '047.ribalta_disp_improprie 1', 'N');
		end if;
  End If;

/*
Elsif disp_imp_da_ribaltare.ESERCIZIO > disp_imp_da_ribaltare.ESERCIZIO_RES Then
 -- RESIDUI

  If ((disp_imp_da_ribaltare.tot_IM_STANZ_RES_IMPROPRIO +
       disp_imp_da_ribaltare.tot_VAR_PIU_STANZ_RES_IMP -
       disp_imp_da_ribaltare.tot_VAR_MENO_STANZ_RES_IMP) -
      (disp_imp_da_ribaltare.tot_IM_OBBL_RES_IMP +
       disp_imp_da_ribaltare.tot_VAR_PIU_OBBL_RES_IMP -
       disp_imp_da_ribaltare.tot_VAR_MENO_OBBL_RES_IMP)) != 0 And
     ((disp_imp_da_ribaltare.tot_IM_STANZ_RES_IMPROPRIO +
       disp_imp_da_ribaltare.tot_VAR_PIU_STANZ_RES_IMP -
       disp_imp_da_ribaltare.tot_VAR_MENO_STANZ_RES_IMP) -
      (disp_imp_da_ribaltare.tot_IM_OBBL_RES_IMP +
       disp_imp_da_ribaltare.tot_VAR_PIU_OBBL_RES_IMP -
       disp_imp_da_ribaltare.tot_VAR_MENO_OBBL_RES_IMP)) > aSogliaPar Then

       aDeltaSaldo.ESERCIZIO := aEs + 1;
       aDeltaSaldo.ESERCIZIO_RES := disp_imp_da_ribaltare.ESERCIZIO_RES;
       aDeltaSaldo.CD_CENTRO_RESPONSABILITA := disp_imp_da_ribaltare.CD_CENTRO_RESPONSABILITA;
       aDeltaSaldo.CD_LINEA_ATTIVITA := disp_imp_da_ribaltare.CD_LINEA_ATTIVITA;
       aDeltaSaldo.TI_APPARTENENZA := disp_imp_da_ribaltare.TI_APPARTENENZA;
       aDeltaSaldo.TI_GESTIONE := disp_imp_da_ribaltare.TI_GESTIONE;
       aDeltaSaldo.CD_VOCE := disp_imp_da_ribaltare.CD_VOCE;
       aDeltaSaldo.UTUV := aUser;

      CNRCTB054.RESET_IMPORTI_SALDI (aDELTASALDO);

       aDeltaSaldo.IM_STANZ_RES_IMPROPRIO :=
       ((disp_imp_da_ribaltare.tot_IM_STANZ_RES_IMPROPRIO + disp_imp_da_ribaltare.tot_VAR_PIU_STANZ_RES_IMP -
         disp_imp_da_ribaltare.tot_VAR_MENO_STANZ_RES_IMP) -
        (disp_imp_da_ribaltare.tot_IM_OBBL_RES_IMP + disp_imp_da_ribaltare.tot_VAR_PIU_OBBL_RES_IMP -
         disp_imp_da_ribaltare.tot_VAR_MENO_OBBL_RES_IMP));

       CNRCTB054.CREA_AGGIORNA_SALDI(aDELTASALDO, '047.ribalta_disp_improprie 2', 'N');

  End If;

End If;
*/
End Loop;

End;
End;
