--------------------------------------------------------
--  DDL for Package Body CNRCTB048
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB048" is

Function getcdsribaltato (aEs NUMBER, aCDS VARCHAR2) Return VARCHAR2 Is
rib_sn CHAR(1);
Begin
 Select Nvl(fl_ribaltato, 'N')
 Into RIB_SN
 From parametri_cds
 Where esercizio = aEs And
       cd_cds = aCDS;
 Return RIB_SN;
Exception
 When No_Data_Found Then
   Return 'N';
End;

function isDocModificato(aObb obbligazione%rowtype,aObbNew obbligazione%rowtype) return char is
aNum number;
aScad obbligazione_scadenzario%rowtype;
begin
  -- confronto campi significativi testata
If (aObbNew.fl_pgiro = 'N') Or
   (aObbNew.fl_pgiro = 'Y' and CNRCTB035.ISAPREPGIRO(aObbNew) And aObbNew.cd_tipo_documento_cont <> CNRCTB018.TI_DOC_IMP_RES) Or
   (aObbNew.fl_pgiro = 'Y' and aObbNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES) Then

     If Not (aObbNew.im_obbligazione = CNRCTB048.GETIMNONRISCONTRATO(aObb.cd_cds,aObb.esercizio,aObb.esercizio_originale,aObb.pg_obbligazione,aObb.ti_gestione) And
        aObbNew.stato_obbligazione = aObb.stato_obbligazione And -- con l'ipotesi che pluriennali ribaltate restino provvisorie
	aObbNew.dt_cancellazione is null) then
	      return 'Y';
     End If;

End If;

  if aObbNew.cd_terzo = aObb.cd_terzo Then
     --Verifico se sono state aggiunte modifiche ai residui (record in OBBLIGAZIONE_MODIFICA)
	  Declare
	    contaRec NUMBER := 0;
	  Begin
	    Select Count(0) Into contaRec
	    From obbligazione_modifica
	    Where cd_cds = aObbNew.Cd_cds
	    And   esercizio = aObbNew.esercizio
	    And   esercizio_originale = aObbNew.esercizio_originale
	    And   pg_obbligazione = aObbNew.pg_obbligazione;

            If contaRec > 0 Then
               return 'Y';
            End If;
	  End;
	  -- rp 29/01/2010 ignoro lo stato del mandato in quanto non deve essere cmq 'deriportabile'
         Declare
	    contaRec NUMBER := 0;
	  Begin
	    Select Count(0) Into contaRec
	    From mandato_riga
	    Where cd_cds = aObbNew.Cd_cds
	    And   esercizio_obbligazione = aObbNew.esercizio
	    And   esercizio_ori_obbligazione = aObbNew.esercizio_originale
	    And   pg_obbligazione = aObbNew.pg_obbligazione;

            If contaRec > 0 Then
               return 'Y';
            End If;
	  End;
      -- confronto le scadenze
	  for aScadNew in (select * from obbligazione_scadenzario
	  	  		   	   where cd_cds  		 = aObbNew.cd_cds
					     and esercizio 		 = aObbNew.esercizio
					     and esercizio_originale 	 = aObbNew.esercizio_originale
						 and pg_obbligazione = aObbNew.pg_obbligazione) loop

		if aScadNew.pg_obbl_scad_ori_riporto is null then
		   return 'Y';  -- se una scadenza non ha ori riporto, è stata aggiunta
		end if;

		aScad:=CNRCTB048.getOldScad(aObbNew,aScadNew);
		if (aObbNew.fl_pgiro = 'N')
		   or
		   (aObbNew.fl_pgiro = 'Y' and CNRCTB035.ISAPREPGIRO(aObbNew) and aObbNew.cd_tipo_documento_cont <> CNRCTB018.TI_DOC_IMP_RES)
		   or
		   (aObbNew.fl_pgiro = 'Y' and aObbNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES)
		then
			  if not (aScadNew.im_scadenza = aScad.im_scadenza - aScad.im_associato_doc_contabile
			  	 	 and
					 aScadNew.im_associato_doc_amm = aScad.im_associato_doc_amm - aScad.im_associato_doc_contabile
					 and
					 aScadNew.im_associato_doc_contabile = 0) then
				return 'Y';
			  end if;
		end if;

                -- S.F. 25/02/2009 Eliminato il controllo perchè la data di scadenza non è una informazione sostanziale per
                --                 impedire il riporta indietro
/*
		if aScadNew.dt_scadenza = CNRCTB048.getDtScadenza(aScad.esercizio + 1,aScad.DT_SCADENZA) then
			-- confronto dettagli scadenze:1 no
			-- potrebbe essere cambiata LdA per mappatura
			-- e l'imputazione finanziaria con riporto evoluto
			return 'N';
		else  -- una scadenza è cambiata
			return 'Y';
		end if;
*/

	  end loop; -- scadenze

	  -- S.F. 25/02/2009 In assenza di uno qualsiasi dei casi in cui il documento è cambiato ritorna "N"
          return 'N';

  else  -- è cambiata la testata
  	  return 'Y';
  end if;
end;

function isDocModificato(aAcc accertamento%rowtype,aAccNew accertamento%rowtype) return char is
aScad accertamento_scadenzario%rowtype;
esisteAltraScadRiportata number;
Begin
  -- confronto campi significativi testata
  If (aAccNew.fl_pgiro = 'N') Or
     (aAccNew.fl_pgiro = 'Y' and CNRCTB035.ISAPREPGIRO(aAccNew) and aAccNew.cd_tipo_documento_cont <> CNRCTB018.TI_DOC_ACC_RES) Or
     (aAccNew.fl_pgiro = 'Y' and aAccNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES) Then

      If Not (aAccNew.im_accertamento =
                    CNRCTB048.GETIMNONRISCONTRATO(aAcc.cd_cds,aAcc.esercizio,aAcc.esercizio_originale,aAcc.pg_accertamento,aAcc.ti_gestione) And
              aAccNew.dt_cancellazione Is null) Then
		Return 'Y';
      End If;
  End If;

  If aAccNew.cd_terzo = aAcc.cd_terzo Then

     --Verifico se sono state aggiunte modifiche ai residui (record in OBBLIGAZIONE_MODIFICA)
	  Declare
	    contaRec NUMBER := 0;
	  Begin
	    Select Count(0) Into contaRec
	    From accertamento_modifica
	    Where cd_cds = aAccNew.Cd_cds
	    And   esercizio = aAccNew.esercizio
	    And   esercizio_originale = aAccNew.esercizio_originale
	    And   pg_accertamento = aAccNew.pg_accertamento;

            If contaRec > 0 Then
               return 'Y';
            End If;
	  End;
         -- rp 29/01/2010 ignoro lo stato del reversale in quanto non deve essere cmq 'deriportabile'
         Declare
	    contaRec NUMBER := 0;
	  Begin
	    Select Count(0) Into contaRec
	    From reversale_riga
	    Where cd_cds = aAccNew.Cd_cds
	    And   esercizio_accertamento = aAccNew.esercizio
	    And   esercizio_ori_accertamento = aAccNew.esercizio_originale
	    And   pg_accertamento = aAccNew.pg_accertamento;

            If contaRec > 0 Then
               return 'Y';
            End If;
	  End;
      -- confronto le scadenze
	  For aScadNew in (select *
	                   from  accertamento_scadenzario
	  	           where cd_cds = aAccNew.cd_cds And
	  	                 esercizio = aAccNew.esercizio And
	  	                 esercizio_originale = aAccNew.esercizio_originale And
	  	                 pg_accertamento = aAccNew.pg_accertamento
                           order by pg_accertamento_scadenzario) Loop

		if aScadNew.pg_acc_scad_ori_riporto is null then
		   return 'Y';  -- se una scadenza non ha ori riporto, è stata aggiunta
		end if;

		-- escludo da ulteriori controlli scadenze tali che:
		-- 		   - il documento è ACR_RES
		-- 		   - l'importo è nullo
		--		   - esiste una scadenza successiva con pari scad_ori_riporto
		-- per gestione del riporto in avanti di acr/acr_res dopo deriporto senza
		-- cancellazione fisica

		Begin
			select 1 into esisteAltraScadRiportata
			from dual
			where exists (select 1
				      from accertamento_scadenzario
				      where cd_cds 		= aScadNew.cd_cds And
				            esercizio 	        = aScadNew.esercizio And
				            esercizio_originale = aScadNew.esercizio_originale And
				            pg_accertamento 	= aScadNew.pg_accertamento And
				            pg_accertamento_scadenzario > aScadNew.pg_accertamento_scadenzario And
				            pg_acc_scad_ori_riporto 	= aScadNew.pg_acc_scad_ori_riporto);
		Exception when NO_DATA_FOUND then
			esisteAltraScadRiportata := 0;
		End;

		if not (aAccNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES And aScadNew.im_scadenza = 0 And esisteAltraScadRiportata = 1) Then
			aScad:=CNRCTB048.getOldScad(aAccNew,aScadNew);

			if (aAccNew.fl_pgiro = 'N') Or
			   (aAccNew.fl_pgiro = 'Y' and CNRCTB035.ISAPREPGIRO(aAccNew) and aAccNew.cd_tipo_documento_cont <> CNRCTB018.TI_DOC_ACC_RES) or
			   (aAccNew.fl_pgiro = 'Y' and aAccNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES) then

				  If Not (aScadNew.im_scadenza = aScad.im_scadenza - aScad.im_associato_doc_contabile and
				          aScadNew.im_associato_doc_amm = aScad.im_associato_doc_amm - aScad.im_associato_doc_contabile and
				          aScadNew.im_associato_doc_contabile = 0) Then
					return 'Y';
				  End If;
			end if;

                -- S.F. 25/02/2009 Eliminato il controllo perchè la data di scadenza non è una informazione sostanziale per
                --                 impedire il riporta indietro
/*
			if aScadNew.dt_scadenza_incasso = CNRCTB048.getDtScadenza(aScad.esercizio + 1, aScad.DT_SCADENZA_INCASSO) Then
				-- confronto dettagli scadenze:2 no
				-- potrebbe essere cambiata LdA per mappatura
				-- e l'imputazione finanziaria per riporto evoluto
				return 'N';
			else  -- una scadenza è cambiata
				return 'Y';

			end if;
*/

		end if;

	  End Loop; -- scadenze

          -- S.F. 25/02/2009 In assenza di uno qualsiasi dei casi in cui il documento è cambiato ritorna "N"
          return 'N';

  Else  -- è cambiata la testata (IL TERZO DELLA TESTATA)
  	  Return 'Y';
  End If;
End;

 procedure checkEsercizio(aEs number, aCdCds varchar2) is
 aNum number;
 begin
 -- condizione sull'esercizio per poter ribaltare doc cont:
 -- l'esercizio corrente è aperto, esiste l'esercizio successivo
  if not CNRCTB008.ISESERCIZIOAPERTO(aEs,aCdCds) then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||(aEs)||' non è aperto per il cds:'||aCdCds);
  end if;
--   if CNRCTB008.ISESERCIZIOCHIUSO(aEs,aCdCds) then
--    IBMERR001.RAISE_ERR_GENERICO('L''esercizio corrente '||aEs||' è chiuso per il cds:'||aCdCds);
--   end if;
  begin
  	   select 1 into aNum
	   from dual
	   where exists (select 1 from esercizio
	                 where cd_cds = aCdCds
					   and esercizio = aEs + 1);
  exception when no_data_found then
       ibmerr001.RAISE_ERR_GENERICO('L''esecizio '||(aEs+1)||'non è definito per il cds '||aCdCds);
  end;
 end;

 procedure checkDeRiportaScadEsNext(aObb obbligazione%rowtype, aObbScad obbligazione_scadenzario%rowtype, aObbNext obbligazione%rowtype, aObbScadNext obbligazione_scadenzario%rowtype) is
 aEsLP number;
 begin
  -- Controlla che i doc amministrativi collegati non siano stati pagati nel nuovo anno
  for aDocAmm in (select distinct
                    cd_tipo_documento_amm,
					cd_cds,
					esercizio,
					cd_unita_organizzativa,pg_documento_amm,
					ti_associato_manrev,
					is_sola_testata
				  from V_DOC_AMM_OBB
				    where
					      cd_cds_obbligazione         = aObbScadNext.cd_cds
					  and esercizio_obbligazione	  = aObbScadNext.esercizio
					  and esercizio_ori_obbligazione  = aObbScadNext.esercizio_originale
					  and pg_obbligazione		  = aObbScadNext.pg_obbligazione
					  and pg_obbligazione_scadenzario = aObbScadNext.pg_obbligazione_scadenzario
				  ) loop
	CNRCTB100.lockdocamm(aDocAmm.cd_tipo_documento_amm,aDocAmm.cd_cds,aDocAmm.esercizio,aDocAmm.cd_unita_organizzativa,aDocAmm.pg_documento_amm);
	-- se fattura passiva, verifico non sia stata associata una lettera di pagamento estero
	if aDocAmm.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_PASSIVA then
	   select esercizio_lettera into aEsLP
	   from fattura_passiva
	   where cd_cds = aDocAmm.cd_cds
	     and cd_unita_organizzativa = aDocAmm.cd_unita_organizzativa
		 and esercizio 				= aDocAmm.esercizio
		 and pg_fattura_passiva		= aDocAmm.pg_documento_amm;
	   if aEsLP > aDocAmm.esercizio then
          If cnrutil.isLabelObbligazione() Then
	   	    ibmerr001.RAISE_ERR_GENERICO('L''obbligazione '||CNRCTB035.getDesc(aObbNext)||' risulta associata a lettera di pagamento estero emessa nel nuovo esercizio');
       	  Else
	   	    ibmerr001.RAISE_ERR_GENERICO('L''impegno '||CNRCTB035.getDesc(aObbNext)||' risulta associato a lettera di pagamento estero emessa nel nuovo esercizio');
      	  End If;
	   end if;
	end if;
    -- Ciclo sulle righe (eventualemten fittizie ...)
    for aDocAmmRiga in (select * from V_DOC_AMM_OBB
				    where
					      cd_tipo_documento_amm       = aDocAmm.cd_tipo_documento_amm
					  and esercizio                   = aDocAmm.esercizio
					  and pg_documento_amm		  = aDocAmm.pg_documento_amm
					  and cd_cds			  = aDocAmm.cd_cds
					  and cd_unita_organizzativa      = aDocAmm.cd_unita_organizzativa
					  and cd_cds_obbligazione         = aObbScadNext.cd_cds
					  and esercizio_obbligazione	  = aObbScadNext.esercizio
					  and esercizio_ori_obbligazione  = aObbScadNext.esercizio_originale
					  and pg_obbligazione		  = aObbScadNext.pg_obbligazione
					  and pg_obbligazione_scadenzario = aObbScadNext.pg_obbligazione_scadenzario
					) loop
	      CNRCTB100.lockdocammRiga(aDocAmmRiga.cd_tipo_documento_amm,aDocAmmRiga.cd_cds,aDocAmmRiga.esercizio,aDocAmmRiga.cd_unita_organizzativa,aDocAmmRiga.pg_documento_amm,aDocAmmRiga.pg_riga);
          if aDocAmmRiga.esercizio = aObbNext.esercizio then
              If cnrutil.isLabelObbligazione() Then
                ibmerr001.RAISE_ERR_GENERICO('L''obbligazione '||CNRCTB035.getDesc(aObbNext)||' risulta associata a documento amministrativi emessi nel nuovo esercizio');
           	  Else
                ibmerr001.RAISE_ERR_GENERICO('L''impegno '||CNRCTB035.getDesc(aObbNext)||' risulta associato a documento amministrativi emessi nel nuovo esercizio');
          	  End If;
          end if;
          if aDocAmmRiga.stato_cofi = CNRCTB100.STATO_GEN_COFI_TOT_MR then
              If cnrutil.isLabelObbligazione() Then
                ibmerr001.RAISE_ERR_GENERICO('L''obbligazione '||CNRCTB035.getDesc(aObbNext)||' risulta associata a documento amministrativo (in parte) pagato nel nuovo esercizio');
           	  Else
                ibmerr001.RAISE_ERR_GENERICO('L''impegno '||CNRCTB035.getDesc(aObbNext)||' risulta associato a documento amministrativo (in parte) pagato nel nuovo esercizio');
          	  End If;
          end if;
          if not (aDocAmmRiga.ti_associato_manrev = CNRCTB100.TI_NON_ASSOC_MAN_REV) then
		   declare
		    lNum number;
		   begin
		   	select 1 into lNum
			from dual
			where exists (select 1 from mandato_riga
				  		  where cd_cds 				= aObbScadNext.cd_cds
						    and esercizio_obbligazione 		= aObbScadNext.esercizio
						    and esercizio_ori_obbligazione      = aObbScadNext.esercizio_originale
						    and pg_obbligazione 		= aObbScadNext.pg_obbligazione
						    and pg_obbligazione_scadenzario     = aObbScadNext.pg_obbligazione_scadenzario
						    and cd_cds_doc_amm 			= aDocAmmRiga.cd_cds
						    and cd_uo_doc_amm 			= aDocAmmRiga.cd_unita_organizzativa
						    and esercizio_doc_amm 		= aDocAmmRiga.esercizio
						    and cd_tipo_documento_amm 		= aDocAmmRiga.cd_tipo_documento_amm
						    and pg_doc_amm 			= aDocAmmRiga.pg_documento_amm);
			-- se esiste un mandato (anche annullato) legato alla scadenza nel nuovo esercizio
			-- allora l'obbligazione non può essere riportata indietro
            If cnrutil.isLabelObbligazione() Then
    			ibmerr001.RAISE_ERR_GENERICO('L''obbligazione '||CNRCTB035.getDesc(aObbNext)||' risulta essere o essere stata (parzialmente) pagata nel nuovo esercizio');
            Else
    			ibmerr001.RAISE_ERR_GENERICO('L''impegno '||CNRCTB035.getDesc(aObbNext)||' risulta essere o essere stata (parzialmente) pagato nel nuovo esercizio');
            End If;
		   exception when no_data_found then
		   	null; -- se il mandato (annullato) è sulla vecchia scadenza allora si può tornare indietro
		   end;
          end if;
	end loop;
  end loop;
 end;

 procedure checkDeRiportaEsNext(aObb in out obbligazione%rowtype, aObbNext obbligazione%rowtype) is
  aNum number;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  fl_par       parametri_cds.FL_RIPORTA_INDIETRO%Type;
 begin

  Begin
   Select Nvl(FL_RIPORTA_INDIETRO, 'N')
   Into   FL_PAR
   From   PARAMETRI_CDS PAR
   Where  PAR.CD_CDS = aObbNext.CD_CDS And
          PAR.ESERCIZIO = aObbNext.ESERCIZIO;

   If FL_PAR = 'N' Then
     ibmerr001.RAISE_ERR_GENERICO('Il CDS '||aObbNext.CD_CDS||' non è abilitato a riportare indietro documenti dall''esercizio '||aObbNext.ESERCIZIO||'.');
   End If;

  Exception
   When No_Data_Found Then
        ibmerr001.RAISE_ERR_GENERICO('Il CDS '||aObbNext.CD_CDS||' non possiede parametri per l''esercizio '||aObbNext.ESERCIZIO);
  End;

  -- Check documento riportato
  -- se pgiro cds, almeno una dele due parti deve essere riportata
  If aObb.fl_pgiro = 'Y' and not (aObb.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_IMP_RES, CNRCTB018.TI_DOC_IMP)) Then
  	  if CNRCTB035.isAprePgiro(aObb) then
	  	 CNRCTB035.GETPGIROCDS(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
	  else
	  	 CNRCTB035.GETPGIROCDSINV(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
	  end if;
	  if not (aObb.riportato = 'Y' or aAcc.riportato = 'Y') then
	  	 ibmerr001.RAISE_ERR_GENERICO('L''annotazione su partita di giro non è riportata '||CNRCTB035.getDesc(aObb));
	  end if;
  Else
	  if not (aObb.riportato = 'Y') then
          If cnrutil.isLabelObbligazione() Then
            ibmerr001.RAISE_ERR_GENERICO('L''obbligazione non è riportata '||CNRCTB035.getDesc(aObb));
       	  Else
            ibmerr001.RAISE_ERR_GENERICO('L''impegno non è riportato '||CNRCTB035.getDesc(aObb));
      	  End If;
	  end if;
  End If;

  -- Verifica che non esistano variazioni formali
  begin
   select 1 into aNum from dual where exists (
    select 1 from
	VARIAZIONE_FORMALE_IMP where
     	      cd_cds = aObbNext.cd_cds
		  and esercizio = aObbNext.esercizio
		  and esercizio_originale = aObbNext.esercizio_originale
		  and pg_obbligazione = aObbNext.pg_obbligazione
   );
   ibmerr001.RAISE_ERR_GENERICO('Esistono variazioni formali per l'''||cnrutil.getLabelObbligazioneMin()||' nel nuovo esercizio '||CNRCTB035.getDesc(aObbNext));
  exception when NO_DATA_FOUND then
   null;
  end;

  -- verifico se il documento è stato modificato nel nuovo esercizio rispetto a quanto riportato

  if CNRCTB048.isDocModificato(aObb,aObbNext) = 'Y' then
    If cnrutil.isLabelObbligazione() Then
      ibmerr001.RAISE_ERR_GENERICO('L''obbligazione è stata modificata nel nuovo esercizio, non può essere riportata indietro '||CNRCTB035.GETDESC(aObbNext));
    Else
      ibmerr001.RAISE_ERR_GENERICO('L''impegno è stata modificato nel nuovo esercizio, non può essere riportato indietro '||CNRCTB035.GETDESC(aObbNext));
    End If;
  end if;

  -- verifica se esiste un ordine sull'obbligazione emesso nel nuovo esercizio
  begin
  	   select 1 into aNum from dual
	   where exists (select 1 from ordine
	   		 		 where cd_cds = aObbNext.cd_cds
					   and esercizio = aObbNext.esercizio
					   and esercizio_ori_obbligazione = aObbNext.esercizio_originale
					   and pg_obbligazione = aObbNext.pg_obbligazione);
	   ibmerr001.RAISE_ERR_GENERICO('Esiste un ordine emesso sull'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.getDesc(aObbNext));
  exception when NO_DATA_FOUND then
    null;
  end;
 end;

 procedure checkDeRiportaScadEsNext(aAcc accertamento%rowtype, aAccScad accertamento_scadenzario%rowtype, aAccNext accertamento%rowtype, aAccScadNext accertamento_scadenzario%rowtype) is
 begin
  -- Controlla che i doc amministrativi collegati non siano stati pagati nel nuovo anno
  for aDocAmm in (select distinct
                    cd_tipo_documento_amm,
					cd_cds,
					esercizio,
					cd_unita_organizzativa,pg_documento_amm,
					ti_associato_manrev,
					is_sola_testata
                  from V_DOC_AMM_ACC
				    where cd_cds_accertamento         = aAccScadNext.cd_cds
					  and esercizio_accertamento	  = aAccScadNext.esercizio
					  and esercizio_ori_accertamento  = aAccScadNext.esercizio_originale
					  and pg_accertamento			  = aAccScadNext.pg_accertamento
					  and pg_accertamento_scadenzario = aAccScadNext.pg_accertamento_scadenzario
				) loop
	CNRCTB100.lockdocamm(aDocAmm.cd_tipo_documento_amm,aDocAmm.cd_cds,aDocAmm.esercizio,aDocAmm.cd_unita_organizzativa,aDocAmm.pg_documento_amm);
    -- Ciclo sulle righe (eventualemten fittizie ...)
    for aDocAmmRiga in (select * from V_DOC_AMM_ACC
				    where
					      cd_tipo_documento_amm       = aDocAmm.cd_tipo_documento_amm
					  and esercizio                   = aDocAmm.esercizio
					  and pg_documento_amm			  = aDocAmm.pg_documento_amm
					  and cd_cds					  = aDocAmm.cd_cds
					  and cd_unita_organizzativa      = aDocAmm.cd_unita_organizzativa
					  and cd_cds_accertamento         = aAccScadNext.cd_cds
					  and esercizio_accertamento	  = aAccScadNext.esercizio
					  and esercizio_ori_accertamento  = aAccScadNext.esercizio_originale
					  and pg_accertamento			  = aAccScadNext.pg_accertamento
					  and pg_accertamento_scadenzario = aAccScadNext.pg_accertamento_scadenzario
	) loop
	     CNRCTB100.lockdocammRiga(aDocAmmRiga.cd_tipo_documento_amm,aDocAmmRiga.cd_cds,aDocAmmRiga.esercizio,aDocAmmRiga.cd_unita_organizzativa,aDocAmmRiga.pg_documento_amm,aDocAmmRiga.pg_riga);
        if aDocAmmRiga.esercizio = aAccNext.esercizio then
         ibmerr001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.getDesc(aAccNext)||' risulta associato a documento amministrativi emessi nel nuovo esercizio');
		end if;
        if aDocAmmRiga.stato_cofi = CNRCTB100.STATO_GEN_COFI_TOT_MR then
         ibmerr001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.getDesc(aAccNext)||' risulta associato a documento amministrativo (in parte) pagato nel nuovo esercizio');
        end if;
        if not (aDocAmmRiga.ti_associato_manrev = CNRCTB100.TI_NON_ASSOC_MAN_REV) then
		 declare
		  lNum number;
		 begin
		  select 1 into lNum
		  from dual
		  where exists (select 1 from reversale_riga
		  			    where cd_cds = aAccScadNext.cd_cds
						  and esercizio_accertamento 	  = aAccScadNext.esercizio
						  and esercizio_ori_accertamento  = aAccScadNext.esercizio_originale
						  and pg_accertamento 		 	  = aAccScadNext.pg_accertamento
						  and pg_accertamento_scadenzario = aAccScadNext.pg_accertamento_scadenzario
						  and cd_cds_doc_amm 			  = aDocAmmRiga.cd_cds
						  and cd_uo_doc_amm				  = aDocAmmRiga.cd_unita_organizzativa
						  and esercizio_doc_amm			  = aDocAmmRiga.esercizio
						  and cd_tipo_documento_amm		  = aDocAmmRiga.cd_tipo_documento_amm
						  and pg_doc_amm				  = aDocAmmRiga.pg_documento_amm);
		  -- se esiste una reversale (anche annullata) collegata all'accertamento
		  -- nel nuovo esercizio, non è possibile tornare indietro

		  ibmerr001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.getDesc(aAccNext)||' risulta essere o essere stato (parzialmente) pagato nel nuovo esercizio');
		 exception when NO_DATA_FOUND then
		  null;  -- se la reversale (annullata) è sulla vecchia scadenza allora posso tornare indietro
		 end;
        end if;
	end loop;
  end loop;
 end;

 procedure checkDeRiportaEsNext(aAcc in out accertamento%rowtype, aAccNext accertamento%rowtype) is
  aNum number;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  fl_par       parametri_cds.FL_RIPORTA_INDIETRO%Type;

 begin

  Begin
   Select Nvl(FL_RIPORTA_INDIETRO, 'N')
   Into   FL_PAR
   From   PARAMETRI_CDS PAR
   Where  PAR.CD_CDS = aAccNext.CD_CDS And
          PAR.ESERCIZIO = aAccNext.ESERCIZIO;

   If FL_PAR = 'N' Then
     ibmerr001.RAISE_ERR_GENERICO('Il CDS '||aAccNext.CD_CDS||' non è abilitato a riportare indietro documenti dall''esercizio '||aAccNext.ESERCIZIO||'.');
   End If;

  Exception
   When No_Data_Found Then
        ibmerr001.RAISE_ERR_GENERICO('Il CDS '||aAccNext.CD_CDS||' non possiede parametri per l''esercizio '||aAccNext.ESERCIZIO);
  End;

  -- Check documento riportato
  -- se pgiro cds, almeno una dele due parti deve essere riportata
  if aAcc.fl_pgiro = 'Y' and not (aAcc.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_ACC_RES, CNRCTB018.TI_DOC_ACC))
  then
  	  if CNRCTB035.isAprePgiro(aAcc) then
	  	 CNRCTB035.GETPGIROCDS(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
	  else
	  	 CNRCTB035.GETPGIROCDSINV(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
	  end if;
	  if not (aObb.riportato = 'Y' or aAcc.riportato = 'Y') then
	  	 ibmerr001.RAISE_ERR_GENERICO('L''annotazione su partita di giro non è riportata '||CNRCTB035.getDesc(aObb));
	  end if;
  else
	  if not (aAcc.riportato = 'Y') then
	   ibmerr001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioneMin()||' non è riportata '||CNRCTB035.getDesc(aObb));
	  end if;
  end if;

  -- Verifica che non esistano variazioni formali
  begin
   select 1 into aNum from dual where exists (
    select 1 from VARIAZIONE_FORMALE_ACC where
     	      cd_cds = aAccNext.cd_cds
		  and esercizio = aAccNext.esercizio
		  and esercizio_originale  = aAccNext.esercizio_originale
		  and pg_accertamento = aAccNext.pg_accertamento
   );
   ibmerr001.RAISE_ERR_GENERICO('Esistono variazioni formali per l''accertamento nel nuovo esercizio'||CNRCTB035.getDesc(aAccNext));
  exception when NO_DATA_FOUND then
   null;
  end;
  -- verifico se il documento è stato modificato nel nuovo esercizio
  -- rispetto a quanto riportato
  if CNRCTB048.isDocModificato(aAcc,aAccNext) = 'Y' then
    ibmerr001.RAISE_ERR_GENERICO('L''accertamento è stato modificato nel nuovo esercizio, non può essere riportato indietro '||CNRCTB035.GETDESC(aAccNext));
  end if;
 end;

 procedure aggiornaDocAmm(aScad         obbligazione_scadenzario%rowtype,
		  	  aObbScadNext  obbligazione_scadenzario%rowtype,
			  aUser         varchar2,
			  aTSNow        date) is
 begin
	for aDocAmm in (select distinct cd_tipo_documento_amm,cd_cds,esercizio,cd_unita_organizzativa,pg_documento_amm, is_sola_testata from V_DOC_AMM_OBB d
				    where cd_cds_obbligazione         = aScad.cd_cds
					  and esercizio_obbligazione	  = aScad.esercizio
					  and esercizio_ori_obbligazione  = aScad.esercizio_originale
					  and pg_obbligazione		  = aScad.pg_obbligazione
					  and pg_obbligazione_scadenzario = aScad.pg_obbligazione_scadenzario
					  and not exists (select 1
		   	   	   		   	  from mandato_riga mriga,
						   	       mandato man
 	   	   	   		   		  where mriga.CD_CDS = aScad.cd_cds
						           and mriga.ESERCIZIO_OBBLIGAZIONE = aScad.esercizio
        		     				   and mriga.ESERCIZIO_ORI_OBBLIGAZIONE  = aScad.esercizio_originale
							   and mriga.PG_OBBLIGAZIONE	  = aScad.pg_obbligazione
							   and mriga.PG_OBBLIGAZIONE_SCADENZARIO = aScad.pg_obbligazione_scadenzario
							   and mriga.CD_CDS_DOC_AMM	 = d.cd_cds
							   and mriga.CD_UO_DOC_AMM	 = d.cd_unita_organizzativa
							   and mriga.ESERCIZIO_DOC_AMM	 = d.esercizio
							   and mriga.CD_TIPO_DOCUMENTO_AMM = d.cd_tipo_documento_amm
							   and mriga.PG_DOC_AMM	= d.pg_documento_amm
							   and man.cd_cds 	= mriga.cd_cds
							   and man.esercizio  	= mriga.esercizio
							   and man.pg_mandato 	= mriga.pg_mandato
							   and man.stato 	= 'P'
					  )
				   ) loop
        if aDocAmm.is_sola_testata = 'Y' then
         CNRCTB100.updatedocamm(aDocAmm.cd_tipo_documento_amm,aDocAmm.cd_cds,aDocAmm.esercizio,aDocAmm.cd_unita_organizzativa,aDocAmm.pg_documento_amm,
		  '
		    cd_cds_obbligazione = '''||aObbScadNext.cd_cds||''',
		    esercizio_obbligazione = '||aObbScadNext.esercizio||',
		    esercizio_ori_obbligazione = '||aObbScadNext.esercizio_originale||',
		    pg_obbligazione = '||aObbScadNext.pg_obbligazione||',
		    pg_obbligazione_scadenzario = '||aObbScadNext.pg_obbligazione_scadenzario||'
		  ',
		  null,
		  aUser,
		  aTsNow
		 );
		else
		 -- Aggiorna il pg_ver_rec della testata
         CNRCTB100.updatedocamm(aDocAmm.cd_tipo_documento_amm,aDocAmm.cd_cds,aDocAmm.esercizio,aDocAmm.cd_unita_organizzativa,aDocAmm.pg_documento_amm,
          '',
		  null,
		  aUser,
		  aTsNow
		 );
		 -- Cicla sulle righe del documento e le aggiorna
	     for aDocAmmRiga in (select * from V_DOC_AMM_OBB
				    where
					      cd_tipo_documento_amm       = aDocAmm.cd_tipo_documento_amm
					  and esercizio                   = aDocAmm.esercizio
					  and pg_documento_amm		  = aDocAmm.pg_documento_amm
					  and cd_cds			  = aDocAmm.cd_cds
					  and cd_unita_organizzativa      = aDocAmm.cd_unita_organizzativa
					  and cd_cds_obbligazione         = aScad.cd_cds
					  and esercizio_obbligazione	  = aScad.esercizio
					  and esercizio_ori_obbligazione  = aScad.esercizio_originale
					  and pg_obbligazione		  = aScad.pg_obbligazione
					  and pg_obbligazione_scadenzario = aScad.pg_obbligazione_scadenzario
					) loop
          CNRCTB100.updatedocammriga(aDocAmmRiga.cd_tipo_documento_amm,aDocAmmRiga.cd_cds,aDocAmmRiga.esercizio,aDocAmmRiga.cd_unita_organizzativa,aDocAmmRiga.pg_documento_amm,aDocAmmRiga.pg_riga,
		   '
		    cd_cds_obbligazione = '''||aObbScadNext.cd_cds||''',
		    esercizio_obbligazione = '||aObbScadNext.esercizio||',
		    esercizio_ori_obbligazione = '||aObbScadNext.esercizio_originale||',
		    pg_obbligazione = '||aObbScadNext.pg_obbligazione||',
		    pg_obbligazione_scadenzario = '||aObbScadNext.pg_obbligazione_scadenzario||'
		   ',
		   null,
		   aUser,
		   aTsNow
          );
		 end loop;
        end if;
	end loop;
 end;

 procedure aggiornaDocAmm(aScad accertamento_scadenzario%rowtype,
		  				 aAccScadNext accertamento_scadenzario%rowtype,
						 aUser varchar2,
						 aTSNow date) is
 begin
	for aDocAmm in (select distinct cd_tipo_documento_amm,cd_cds,esercizio,cd_unita_organizzativa,pg_documento_amm, is_sola_testata from V_DOC_AMM_ACC
				    where cd_cds_accertamento         = aScad.cd_cds
					  and esercizio_accertamento	  = aScad.esercizio
					  and esercizio_ori_accertamento  = aScad.esercizio_originale
					  and pg_accertamento			  = aScad.pg_accertamento
					  and pg_accertamento_scadenzario = aScad.pg_accertamento_scadenzario
					) loop
        if aDocAmm.is_sola_testata = 'Y' then
         CNRCTB100.updatedocamm(aDocAmm.cd_tipo_documento_amm,aDocAmm.cd_cds,aDocAmm.esercizio,aDocAmm.cd_unita_organizzativa,aDocAmm.pg_documento_amm,
		  '
		    cd_cds_accertamento = '''||aAccScadNext.cd_cds||''',
		    esercizio_accertamento = '||aAccScadNext.esercizio||',
		    esercizio_ori_accertamento = '||aAccScadNext.esercizio_originale||',
		    pg_accertamento = '||aAccScadNext.pg_accertamento||',
		    pg_accertamento_scadenzario = '||aAccScadNext.pg_accertamento_scadenzario||'
		  ',
		  null,
		  aUser,
		  aTsNow
		 );
		else
          CNRCTB100.updatedocamm(aDocAmm.cd_tipo_documento_amm,aDocAmm.cd_cds,aDocAmm.esercizio,aDocAmm.cd_unita_organizzativa,aDocAmm.pg_documento_amm,
          '',
    	  null,
    	  aUser,
    	  aTsNow
    	 );
     	 for aDocAmmRiga in (select * from V_DOC_AMM_ACC
				    where
					      cd_tipo_documento_amm       = aDocAmm.cd_tipo_documento_amm
					  and esercizio                   = aDocAmm.esercizio
					  and pg_documento_amm			  = aDocAmm.pg_documento_amm
					  and cd_cds					  = aDocAmm.cd_cds
					  and cd_unita_organizzativa     = aDocAmm.cd_unita_organizzativa
                      and cd_cds_accertamento         = aScad.cd_cds
					  and esercizio_accertamento	  = aScad.esercizio
					  and esercizio_ori_accertamento  = aScad.esercizio_originale
					  and pg_accertamento			  = aScad.pg_accertamento
					  and pg_accertamento_scadenzario = aScad.pg_accertamento_scadenzario
          ) loop
           CNRCTB100.updatedocammriga(aDocAmmRiga.cd_tipo_documento_amm,aDocAmmRiga.cd_cds,aDocAmmRiga.esercizio,aDocAmmRiga.cd_unita_organizzativa,aDocAmmRiga.pg_documento_amm,aDocAmmRiga.pg_riga,
   		   '
		     cd_cds_accertamento = '''||aAccScadNext.cd_cds||''',
		     esercizio_accertamento = '||aAccScadNext.esercizio||',
		     esercizio_ori_accertamento = '||aAccScadNext.esercizio_originale||',
		     pg_accertamento = '||aAccScadNext.pg_accertamento||',
		     pg_accertamento_scadenzario = '||aAccScadNext.pg_accertamento_scadenzario||'
		   ',
		   null,
		   aUser,
		   aTsNow
          );
         end loop;
		end if;
	end loop;
 end;

Procedure checkNoRiporta(aObb obbligazione%rowtype) Is
  aNum number;

Begin

For aDocAmm In (Select Distinct cd_tipo_documento_amm, cd_cds, esercizio, cd_unita_organizzativa, pg_documento_amm,
 				stato_pagamento_fondo_eco, stato_coge, stato_coan
		from   V_DOC_AMM_OBB
        	Where  cd_cds_obbligazione    = aObb.cd_cds And
        	       esercizio_obbligazione	= aObb.esercizio And
        	       esercizio_ori_obbligazione  = aObb.esercizio_originale And
        	       pg_obbligazione  = aObb.pg_obbligazione) Loop

    CNRCTB100.lockdocamm(aDocAmm.cd_tipo_documento_amm,aDocAmm.cd_cds,aDocAmm.esercizio,aDocAmm.cd_unita_organizzativa,aDocAmm.pg_documento_amm);

    If aDocAmm.stato_pagamento_fondo_eco <> CNRCTB100.STATO_NO_PFONDOECO Then
	 -- se il doc amm è assegnato a fondo ma non ancora associato deve essere
	 -- possibile riportare il documento contabile
	 -- se il doc amm è già legato a spesa del fondo, non si può riportare
	 Begin
	 	 select 1 into aNum
		 from dual
		 where exists (select 1
			       from  fondo_spesa
			       where cd_tipo_documento_amm = aDocAmm.cd_tipo_documento_amm And
			             cd_cds_doc_amm		   = aDocAmm.cd_cds And
			             cd_uo_doc_amm		   = aDocAmm.cd_unita_organizzativa And
			             esercizio_doc_amm	   = aDocAmm.esercizio And
			             pg_documento_amm	   = aDocAmm.pg_documento_amm);
		 IBMERR001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioniMin()||' '||CNRCTB035.GETDESC(aObb)||' risulta associata a fondo economale');
	 Exception when no_data_found then
	 	 null;
	 End;
    End If;

    If aDocAmm.cd_tipo_documento_amm in (CNRCTB100.TI_GEN_CORI_VER_SPESA) then
           IBMERR001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioniMin()||' '||CNRCTB035.GETDESC(aObb)||' è gestito in automatico alla liquidazione CORI o è di liquidazione IVA');
    End If;
  -- Rospuc 07/12/2016 Omessa contabilizzazione
	-- controlli commentati per test
	-- ATT!! TOGLIERE COMMENTI!!!
    If aDocAmm.stato_coge not in (CNRCTB100.STATO_COEP_CON,CNRCTB100.STATO_COEP_EXC) then
	   ibmerr001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioniMin()||' '||CNRCTB035.GETDESC(aObb)||' risulta associato a documento amministrativo da contabilizzare ('||
                   aDocAmm.cd_tipo_documento_amm||'/'||aDocAmm.cd_cds||'/'||aDocAmm.esercizio||'/'||aDocAmm.cd_unita_organizzativa||'/'||
                   aDocAmm.pg_documento_amm||')');
    End if;

    If aDocAmm.stato_coan not in (CNRCTB100.STATO_COEP_CON,CNRCTB100.STATO_COEP_EXC) then
	   ibmerr001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' risulta associato a documento amministrativo da contabilizzare ('||
               aDocAmm.cd_tipo_documento_amm||'/'||aDocAmm.cd_cds||'/'||aDocAmm.esercizio||'/'||aDocAmm.cd_unita_organizzativa||'/'||
               aDocAmm.pg_documento_amm||')');
    End If;
   -- fine  Rospuc 07/12/2016 Omessa contabilizzazione
End Loop;

  -- Verifica che l'obbligazione non sia collegata a fondo economale

For aSpesa in (Select *
               From  fondo_spesa
               Where cd_cds_obbligazione = aObb.cd_cds And
                     esercizio_obbligazione=aObb.esercizio And
                     esercizio_ori_obbligazione=aObb.esercizio_originale And
                     pg_obbligazione=aObb.pg_obbligazione) Loop
    IBMERR001.RAISE_ERR_GENERICO('L''obbligazione '||CNRCTB035.GETDESC(aObb)||' risulta associato a spese non documentate del fondo economale');
End Loop;


If aObb.fl_pgiro = 'Y' Then

-- Verifica che l'obbligazione non sia collegata a CORI di compenso ...
-- 25.10.2007 S.F. ... ma sono se ha importi da ribaltare (A CAUSA DEL NUOVO RIBALTAMENTO DELLE PARTITE DI GIRO,
-- DAVA ERRORE SE L'ACCERTAMENTO SI TRASCINAVA QUESTO NEL NUOVO ANNO ANCHE SE DEVE ESSERE CREATO A ZERO)

   Begin
--    If cnrctb048.getImNonPagatoRiscosso(aObb.Cd_Cds, aObb.Esercizio, aObb.esercizio_originale, aObb.pg_obbligazione, 'S') > 0 Then
       Select distinct 1
       Into   aNum
       From   contributo_ritenuta
       Where  cd_cds_obbligazione = aObb.cd_cds And
              esercizio_obbligazione = aObb.esercizio And
              esercizio_ori_obbligazione = aObb.esercizio_originale And
              pg_obbligazione = aObb.pg_obbligazione;
       IBMERR001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' risulta associato a CORI di compensi');
--    End If;
   Exception
     When NO_DATA_FOUND Then Null;
   End;


   -- Verifica che l'obbligazione non sia di liquidazione_cori
   For aGruppoCentro in (Select *
                         From liquid_gruppo_centro
                         Where cd_cds_obb_accentr = aObb.cd_cds And
                               esercizio_obb_accentr=aObb.esercizio And
                               esercizio_ori_obb_accentr=aObb.esercizio_originale And
                               pg_obb_accentr=aObb.pg_obbligazione) Loop
     IBMERR001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' risulta parte di liquidazione CORI accentrata (l''obbligazione è presente in LIQUID_GRUPPO_CENTRO)');
   End Loop;

   -- Verifica che l'obbligazione non sia di liquidazione_iva_accentrata
   For aGruppoCentro in (Select *
                         From  liquidazione_iva_centro
                         Where cd_cds_obb_accentr = aObb.cd_cds And
                               esercizio_obb_accentr=aObb.esercizio And
                               esercizio_ori_obb_accentr=aObb.esercizio_originale And
                               pg_obb_accentr=aObb.pg_obbligazione) Loop
    IBMERR001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' risulta parte di liquidazione IVA al centro (l''obbligazione è presente in LIQUIDAZIONE_IVA_CENTRO)');
   End Loop;
End If; -- l'if partita di giro

End;

 procedure checkNoRiporta(aAcc accertamento%rowtype) is
  aNum number;
 begin
  for aDocAmm in (select distinct
                    cd_tipo_documento_amm,
					cd_cds,
					esercizio,
					cd_unita_organizzativa,
					pg_documento_amm,
					stato_cofi,
					stato_coge,
					stato_coan
				  from V_DOC_AMM_ACC
				    where
					      cd_cds_accertamento         = aAcc.cd_cds
					  and esercizio_accertamento	  = aAcc.esercizio
					  and esercizio_ori_accertamento  = aAcc.esercizio_originale
					  and pg_accertamento			  = aAcc.pg_accertamento
				  ) loop
	CNRCTB100.lockdocamm(aDocAmm.cd_tipo_documento_amm,aDocAmm.cd_cds,aDocAmm.esercizio,aDocAmm.cd_unita_organizzativa,aDocAmm.pg_documento_amm);
    if aDocAmm.cd_tipo_documento_amm in (CNRCTB100.TI_GEN_CORI_VER_ENTRATA) then
     IBMERR001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.GETDESC(aAcc)||' è gestito in automatico alla liquidazione CORI');
	end if;
	/*RP 07/12/2016 Omessa contabilizzazione
	-- controlli commentati per test
	-- ATT!! TOGLIERE COMMENTI!!!
	if aDocAmm.stato_coge not in (CNRCTB100.STATO_COEP_CON,CNRCTB100.STATO_COEP_EXC) Then
	   If aDocAmm.stato_cofi != CNRCTB100.STATO_GEN_COFI_ANN Then
	     ibmerr001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.GETDESC(aAcc)||' risulta associato a documento amministrativo da contabilizzare in economica ('||
               aDocAmm.cd_tipo_documento_amm||'/'||aDocAmm.cd_cds||'/'||aDocAmm.esercizio||'/'||aDocAmm.cd_unita_organizzativa||'/'||
               aDocAmm.pg_documento_amm||')');
           Else
	     ibmerr001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.GETDESC(aAcc)||' risulta associato a un dettaglio annullato di documento amministrativo da contabilizzare in economica ('||
               aDocAmm.cd_tipo_documento_amm||'/'||aDocAmm.cd_cds||'/'||aDocAmm.esercizio||'/'||aDocAmm.cd_unita_organizzativa||'/'||
               aDocAmm.pg_documento_amm||')');
           End If;
	end if;

	if aDocAmm.stato_coan not in (CNRCTB100.STATO_COEP_CON,CNRCTB100.STATO_COEP_EXC) then
	   ibmerr001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.GETDESC(aAcc)||' risulta associato a documento amministrativo da contabilizzare in analitica ('||
               aDocAmm.cd_tipo_documento_amm||'/'||aDocAmm.cd_cds||'/'||aDocAmm.esercizio||'/'||aDocAmm.cd_unita_organizzativa||'/'||
               aDocAmm.pg_documento_amm||')');
	end if;
	*/
  end loop;
  -- Verifica che l'accertamento non sia collegata a CORI di compenso
  if aAcc.fl_pgiro = 'Y' then
   begin
    select distinct 1 into aNum from contributo_ritenuta where
	        cd_cds_accertamento = aAcc.cd_cds
        and esercizio_accertamento = aAcc.esercizio
        and esercizio_ori_accertamento  = aAcc.esercizio_originale
	    and pg_accertamento = aAcc.pg_accertamento;
    IBMERR001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.GETDESC(aAcc)||' risulta associato a CORI di compensi');
   exception when NO_DATA_FOUND then
    null;
   end;
   -- Verifica che l'obbligazione non sia di liquidazione_cori
   for aGruppoCentro in (select * from liquid_gruppo_centro_comp where
                               cd_cds_acc_accentr = aAcc.cd_cds
							   and esercizio_acc_accentr=aAcc.esercizio
							   and esercizio_ori_acc_accentr=aAcc.esercizio_originale
							   and pg_acc_accentr=aAcc.pg_accertamento)
   loop
    IBMERR001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.GETDESC(aAcc)||' risulta parte di liquidazione CORI accentrata');
   end loop;
  end if;
 end;

Procedure checkRiportaEsNext(aAcc accertamento%rowtype, controlloRibalt boolean, controlloVoce boolean) Is
aErrMsn varchar2(4000);
aDt     date;
aUo     unita_organizzativa%rowtype;
aEs     number;
aNum    number;
fl_par  parametri_cds.FL_RIPORTA_INDIETRO%Type;
fl_ria  parametri_cds.FL_RIACCERTAMENTO%Type;
begin
  Begin
   Select Nvl(FL_RIPORTA_AVANTI, 'N'), Nvl(FL_RIACCERTAMENTO, 'Y')
   Into   FL_PAR, FL_RIA
   From   PARAMETRI_CDS PAR
   Where  PAR.CD_CDS = aAcc.CD_CDS_origine And
          PAR.ESERCIZIO = aAcc.ESERCIZIO;

   If FL_PAR = 'N' Then
     ibmerr001.RAISE_ERR_GENERICO('Il CDS '||aAcc.CD_CDS||' non è abilitato a riportare avanti documenti dall''esercizio '||aAcc.ESERCIZIO||'.');
   End If;

   If aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES And FL_RIA = 'Y' And aAcc.STATO_RESIDUO is null and aAcc.FL_PGIRO='N' Then
     ibmerr001.RAISE_ERR_GENERICO('L''accertamento '||CNRCTB035.GETDESC(aAcc)||' è un documento privo dello stato. Ribaltamento non possibile!');
   End If;

  Exception
   When No_Data_Found Then
        ibmerr001.RAISE_ERR_GENERICO('Il CDS '||aAcc.CD_CDS||' non possiede parametri per l''esercizio '||aAcc.ESERCIZIO);
  End;

	aEs := aAcc.esercizio + 1;

	-- documento già riportato
	if aAcc.riportato = 'Y' then
	   aErrMsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' è un documento già riportato nel nuovo esercizio';
	   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	end if;

	-- verifico se il documento ha importo nullo
	-- se non è pgiro, o se origine di pgiro
	if (aAcc.fl_pgiro = 'Y' and CNRCTB035.ISAPREPGIRO(aAcc))
	   or
	   (aAcc.fl_pgiro = 'N')
	then
		if aAcc.im_accertamento = 0 then
		   aErrMsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' non viene riportato in quanto ha importo nullo';
		   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
		end if;
	end if;

	-- verifico se il documento è ribaltabile
	if controlloRibalt then
		if isEligibileRibalt(CNRCTB001.GESTIONE_ENTRATE,
		   					 aAcc.cd_cds,
							 aAcc.esercizio,
							 aAcc.esercizio_originale,
							 aAcc.pg_accertamento) = 'N' Then
                  If isAccEsaurito (aAcc) Then
 		     aErrMsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' non è un documento ribaltabile. '||
' Esso risulta completamente riscosso.';
		     ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
                  Elsif  getImPagRiscNonRiscontrato(aAcc.cd_cds, aAcc.esercizio, aAcc.esercizio_originale, aAcc.pg_accertamento, 'E') > 0 Then
 		     aErrMsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' non è un documento ribaltabile. '||
' Verificare che le reversali associate alle scadenze riscosse dell''Accertamento siano totalmente riscontrate. La quota non riscontrata risulta di '||Ltrim(Rtrim(To_Char(getImPagRiscNonRiscontrato(aAcc.cd_cds, aAcc.esercizio, aAcc.esercizio_originale, aAcc.pg_accertamento, 'E'), '999g999g999g999g999g990d00')))||'.';
		     ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
                  Else
		   aErrMsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' non è un documento ribaltabile per cause generiche.';
		   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
                  End If;
		end if;
	end if;


	-- verifico se il documento è associato a pratiche CORI, IVA, FONDO ECONOMALE
	checkNoRiporta(aAcc);

	-- verifico la validità del terzo nell'anno su cui si ribalta
	select dt_fine_rapporto into aDt
	from terzo
	where cd_terzo = aAcc.cd_terzo;
	if to_number(to_char(aDt,'YYYY')) < aEs then
	   aErrMsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' non può essere riportato: terzo '||aAcc.cd_terzo||' non valido nell''esercizio '||aEs;
	   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	end if;

	-- verifico la validità della voce_f sul nuovo esercizio
	-- se non ribalto con cambio imputazione finanziaria
	if controlloVoce then
	    declare
		  recParametriCNR PARAMETRI_CNR%Rowtype;
        begin
          recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
		  if recParametriCNR.fl_nuovo_pdg is null or recParametriCNR.fl_nuovo_pdg='N' Then
  			select dacr into aDt
			from voce_f
			where esercizio       = aEs
			  and ti_appartenenza = aAcc.ti_appartenenza
			  and ti_gestione	  = aAcc.ti_gestione
			  and cd_voce		  = aAcc.cd_voce;
		  End If;
		exception when NO_DATA_FOUND then
			aErrMsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' non può essere riportato: capitolo '||aAcc.cd_voce||' non esistente sull''esercizio '||aEs;
			ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
		end;
	end if;

	-- verifico la validità dell'unità organizzativa sul nuovo esercizio
	aUo := CNRCTB020.GETUOVALIDA(aEs, aAcc.cd_unita_organizzativa);

end;

procedure checkRiportaEsNext(aObb obbligazione%rowtype, controlloRibalt boolean) is
--aObbV v_obbligazione_accertamento%rowtype;
aDt date;
aUO unita_organizzativa%rowtype;
aErrMsn varchar2(4000);
aEs number;
aNum number;

fl_par  parametri_cds.FL_RIPORTA_INDIETRO%Type;

Begin

  Begin
   Select Nvl(FL_RIPORTA_AVANTI, 'N')
   Into   FL_PAR
   From   PARAMETRI_CDS PAR
   Where  PAR.CD_CDS = aObb.CD_CDS And
          PAR.ESERCIZIO = aObb.ESERCIZIO;

   If FL_PAR = 'N' Then
     ibmerr001.RAISE_ERR_GENERICO('Il CDS '||aObb.CD_CDS||' non è abilitato a riportare avanti documenti dall''esercizio '||aObb.ESERCIZIO||'.');
   End If;

  Exception
   When No_Data_Found Then
        ibmerr001.RAISE_ERR_GENERICO('Il CDS '||aObb.CD_CDS||' non possiede parametri per l''esercizio '||aObb.ESERCIZIO);
  End;

	aEs := aObb.esercizio + 1;

	-- documento già riportato
	if aObb.riportato = 'Y' then
	   aErrMsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' è un documento già riportato nel nuovo esercizio';
	   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	end if;


	-- verifico se il documento ha importo nullo
	-- solo se no pgiro, o se origine di pgiro
	if (aObb.fl_pgiro = 'N')
	   or
	   (aObb.fl_pgiro = 'Y' and CNRCTB035.ISAPREPGIRO(aObb))
	then
		if aObb.im_obbligazione = 0 then
		   aErrMsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' non viene riportato in quanto ha importo nullo';
		   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
		end if;
	end if;

	-- verifico se il documento è ribaltabile
	if controlloRibalt then
		if isEligibileRibalt(CNRCTB001.GESTIONE_SPESE,
		   					 aObb.cd_cds,
							 aObb.esercizio,
							 aObb.esercizio_originale,
							 aObb.pg_obbligazione) = 'N' then
                  If isObbEsaurita (aObb) Then
		   aErrMsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' non è un documento ribaltabile. '||
' Risulta completamente pagato.';
		   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
		     ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
                  Elsif  getImPagRiscNonRiscontrato(aObb.cd_cds, aObb.esercizio, aObb.esercizio_originale, aObb.pg_obbligazione, 'S') > 0 Then
		   aErrMsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' non è un documento ribaltabile. '||
' Verificare che i mandati associati alle scadenze pagate dell''Impegno siano totalmente riscontrati. La quota non riscontrata risulta di '||Ltrim(Rtrim(To_Char(getImPagRiscNonRiscontrato(aObb.cd_cds, aObb.esercizio, aObb.esercizio_originale, aObb.pg_obbligazione, 'S'), '999g999g999g999g999g990d00')))||'.';
		   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
                  Else
		   aErrMsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' non è un documento ribaltabile per cause generiche.';
		   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
                  End If;
		end if;
	end if;

	-- verifico se il documento è associato a pratiche CORI, IVA, FONDO ECONOMALE
	checkNoRiporta(aObb);

	-- verifico la validità del terzo sull'esercizio su cui si ribalta
	select dt_fine_rapporto into aDt
	from terzo
	where cd_terzo = aObb.cd_terzo;

	if to_number(to_char(aDt,'YYYY')) < aEs then
	   aErrMsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' non può essere riportata: terzo '||aObb.cd_terzo||' non valido nell''esercizio '||aEs;
	   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	end if;

	-- verifico la validità dell'unità organizzativa sul nuovo esercizio
	aUo := CNRCTB020.GETUOVALIDA(aEs, aObb.cd_unita_organizzativa);

end;

Function getImNonRiscontrato(aCdCds varchar2, aEs number, aEsOri number, aPg number, aTiGestione varchar2) return number is
  aIm number;
 begin
	select im_acc_obb - im_riscontrato into aIm
	from v_obbligazione_accertamento
	where cd_cds      = aCdCds
	  and esercizio   = aEs
	  and esercizio_ori_acc_obb = aEsOri
	  and pg_acc_obb  = aPg
	  and ti_gestione = aTiGestione;

	return aIm;
end;

function getImPagRiscNonRiscontrato(aCdCds varchar2, aEs number, aEsOri number, aPg number, aTiGestione varchar2) return number Is
  aIm number;
 begin
	select IM_ASSOCIATO_REV_MAN - im_riscontrato into aIm
	from v_obbligazione_accertamento
	where cd_cds      = aCdCds
	  and esercizio   = aEs
	  and esercizio_ori_acc_obb = aEsOri
	  and pg_acc_obb  = aPg
	  and ti_gestione = aTiGestione;

	return aIm;
end;



Function getImNonPagatoRiscosso(aCdCds varchar2, aEs number, aEsOri number, aPg number, aTiGestione varchar2) return number is
  aIm number;
 begin
	select im_acc_obb - IM_ASSOCIATO_REV_MAN
	into aIm
	from v_obbligazione_accertamento
	where cd_cds      = aCdCds
	  and esercizio   = aEs
	  and esercizio_ori_acc_obb = aEsOri
	  and pg_acc_obb  = aPg
	  and ti_gestione = aTiGestione;

	return aIm;
end;

function getImRiscontratoManRev(aCdCds varchar2, aEs number, aPg number, aTipo varchar2) return number Is
  aIm number;
Begin
 If aTipo = 'R' Then
   Select nvl(sum(SOSPESO_DET_ETR.IM_ASSOCIATO), 0)
   Into   aIm
   From   SOSPESO_DET_ETR
   Where  SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO = 'R' AND
          SOSPESO_DET_ETR.STATO        = 'N' AND
          SOSPESO_DET_ETR.ti_entrata_spesa = 'E' And
          SOSPESO_DET_ETR.CD_CDS       = aCdCds AND
          SOSPESO_DET_ETR.ESERCIZIO    = aEs AND
          SOSPESO_DET_ETR.PG_REVERSALE = aPg;
 Elsif aTipo = 'M' Then
   Select nvl(sum(SOSPESO_DET_USC.IM_ASSOCIATO), 0)
   Into   aIm
   From   SOSPESO_DET_USC
   Where  SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO = 'R' AND
          SOSPESO_DET_USC.STATO        = 'N' AND
          SOSPESO_DET_usc.ti_entrata_spesa = 'S' And
          SOSPESO_DET_USC.CD_CDS       = aCdCds AND
          SOSPESO_DET_USC.ESERCIZIO    = aEs AND
          SOSPESO_DET_USC.PG_MANDATO   = aPg;
 End If;

 return aIm;
End;


function getStatoRibaltabileScad(aEs number,aScad obbligazione_scadenzario%rowtype) return varchar2 is
isRibaltabile char(1);
aStato char(1);
begin
	if (aScad.im_scadenza > aScad.im_associato_doc_contabile) then
	   isRibaltabile := 'Y';
	else
	   isRibaltabile := 'N';
	end if;
	return isRibaltabile;
end;

function getStatoRibaltabileScad(aEs number,aScad accertamento_scadenzario%rowtype) return varchar2 is
isRibaltabile char(1);
aStato char(1);
begin
	if (aScad.im_scadenza > aScad.im_associato_doc_contabile) then
	   isRibaltabile := 'Y';
	else
	   isRibaltabile := 'N';
	end if;
	return isRibaltabile;
end;

function getPg(aObb obbligazione%rowtype, aEs number, aUser varchar2) return number is
aPgNext number;
begin
-- SF 21.11.2006 con la nuova gestione dell'esercizio originale il numero del documento non cambia più.

aPgNext := aObb.pg_obbligazione;

/*
 If aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES then
	aPgNext := aObb.pg_obbligazione;
 Elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP then
-- 	aPgNext := aObb.esercizio*1000000+aObb.pg_obbligazione;
	aPgNext := aObb.pg_obbligazione;
 Elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB then
 	--aPgNext := CNRCTB018.getNextNumDocCont(aObb.cd_tipo_documento_cont, aEs, aObb.cd_cds, aUser);
        -- 03.01.2006 nuova gestione stani
--        aPgNext := aObb.esercizio * 1000000 + aObb.pg_obbligazione;
	aPgNext := aObb.pg_obbligazione;
 End if;
*/
 Return aPgNext;
End;

function getPg(aAcc accertamento%rowtype, aEs number, aUser varchar2) return number is
aPgNext number;
begin
 If aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES then
		aPgNext := aAcc.pg_accertamento;
 Elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC then
--		aPgNext := aAcc.esercizio*1000000+aAcc.pg_accertamento;
		aPgNext := aAcc.pg_accertamento;
 Elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PGIRO_RES then
		aPgNext := aAcc.pg_accertamento;
 Elsif (aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PLUR And aAcc.esercizio_competenza = aAcc.esercizio) then
		aPgNext := CNRCTB018.GETNEXTNUMDOCCONT(CNRCTB018.TI_DOC_ACC, aEs, aAcc.cd_cds, aUser);
 Elsif (aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PLUR And aAcc.esercizio_competenza > aAcc.esercizio) then
		aPgNext := CNRCTB018.GETNEXTNUMDOCCONT(aAcc.cd_tipo_documento_cont,aEs,aAcc.cd_cds,aUser);
 End If;
 Return aPgNext;
End;

function getDtScadenza(aEs number,aDt_scadenza date) return date is
aDt date;
begin
	 if to_number(to_char(aDt_scadenza,'YYYY')) >= aEs then
	 	aDt := aDt_scadenza;
	 else
	 	aDt := to_date('3112'||aEs,'DDMMYYYY');
	 end if;
	 return aDt;
end;

function getLdA(aDettScad obbligazione_scad_voce%rowtype, aObb obbligazione%rowtype, aEs obbligazione.esercizio%type) return v_linea_attivita_valida%rowtype is
  aLda v_linea_attivita_valida%rowtype;
begin
  if (aObb.cd_tipo_documento_cont In (CNRCTB018.TI_DOC_OBB, CNRCTB018.TI_DOC_OBB_RES_PRO) and aObb.fl_pgiro = 'N') then
    begin
      select * into aLdA
	  from v_linea_attivita_valida la
	  where la.ESERCIZIO = aEs
	  And  (la.CD_CENTRO_RESPONSABILITA, la.CD_LINEA_ATTIVITA) in
	  		(select mla.cd_centro_responsabilita, mla.cd_linea_attivita
			 from  mappatura_la mla
	  		 where esercizio_ori = aDettScad.esercizio
	  		 And   cd_centro_responsabilita_ori = aDettScad.cd_centro_responsabilita
	  		 And   cd_linea_attivita_ori = aDettScad.cd_linea_attivita);
   	  return aLdA;
    exception
      when NO_DATA_FOUND then
  	    null;
    end;
  end if;

  begin
	select * into aLdA
  	from v_linea_attivita_valida
  	where esercizio = aEs
  	and   cd_centro_responsabilita = aDettScad.cd_centro_responsabilita
  	and   cd_linea_attivita		 = aDettScad.cd_linea_attivita;
  exception
    when NO_DATA_FOUND then
      null;
  end;

  return aLda;
end;

function getLdA(aDettScad accertamento_scad_voce%rowtype, aAcc accertamento%rowtype, aEs accertamento.esercizio%type) return v_linea_attivita_valida%rowtype is
  aLda v_linea_attivita_valida%rowtype;
begin
  if aAcc.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_ACC_RES, CNRCTB018.TI_DOC_ACC) and aAcc.fl_pgiro = 'N' then
    begin
	  select * into aLdA
	  from v_linea_attivita_valida la
	  where la.ESERCIZIO = aEs
      And (la.CD_CENTRO_RESPONSABILITA, la.CD_LINEA_ATTIVITA) in
		  (select mla.cd_centro_responsabilita, mla.cd_linea_attivita
		   from mappatura_la mla
		   where esercizio_ori 			 	= aDettScad.esercizio
		   and cd_centro_responsabilita_ori = aDettScad.cd_centro_responsabilita
		   and cd_linea_attivita_ori		= aDettScad.cd_linea_attivita);
   	  return aLdA;
    exception
      when NO_DATA_FOUND then
  	    null;
    end;
  end if;

  begin
	select * into aLdA
	from v_linea_attivita_valida
	where esercizio = aEs
	and   cd_centro_responsabilita = aDettScad.cd_centro_responsabilita
	and   cd_linea_attivita		 = aDettScad.cd_linea_attivita;
  exception
    when NO_DATA_FOUND then
     null;
  end;

  return aLda;
end;

function getVoceF(aObb obbligazione%rowtype,
		 		  aLdA v_linea_attivita_valida%rowtype,
				  aDettScad obbligazione_scad_voce%rowtype,
				  aEs number) return voce_f%rowtype is
aVoce voce_f%rowtype;
aCdTipoUnita varchar2(20);
begin
	select cd_tipo_unita into aCdTipoUnita
	from unita_organizzativa
	where cd_unita_organizzativa = aObb.cd_cds
	  and fl_cds = 'Y';

	if aObb.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_IMP, CNRCTB018.TI_DOC_IMP_RES, CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES) then
	    -- non dipende dalla linea di attività, che è fittizia
	    select * into aVoce
		from voce_f
		where esercizio    	  = aEs
		  and ti_appartenenza = aDettScad.ti_appartenenza
		  and ti_gestione	  = aDettScad.ti_gestione
		  and cd_voce		  = aDettScad.cd_voce;
	else
		if aCdTipoUnita = CNRCTB020.TIPO_SAC then
		   if aObb.fl_spese_costi_altrui = 'Y' then
		   -- indipendentemente da LdA e CdR, il capitolo resta quello originale
			    select * into aVoce
				from voce_f
				where esercizio    	  = aEs
				  and ti_appartenenza = aDettScad.ti_appartenenza
				  and ti_gestione	  = aDettScad.ti_gestione
				  and cd_voce		  = aDettScad.cd_voce;
		   else
				select * into aVoce
				from voce_f
				where esercizio    		       = aEs
				  and ti_appartenenza 	 	   = aObb.ti_appartenenza
				  and ti_gestione 		 	   = aObb.ti_gestione
				  and cd_titolo_capitolo 	   = aObb.cd_elemento_voce
				  and cd_funzione		 	   = aLda.cd_funzione
				  and cd_centro_responsabilita = aLda.cd_centro_responsabilita;
		   end if;
		else
		   if aObb.fl_spese_costi_altrui = 'Y' then
		   -- indipendentemente da LdA e CdR, il capitolo resta quello originale
			    select * into aVoce
				from voce_f
				where esercizio    	  = aEs
				  and ti_appartenenza = aDettScad.ti_appartenenza
				  and ti_gestione	  = aDettScad.ti_gestione
				  and cd_voce		  = aDettScad.cd_voce;
		   else
				select * into aVoce
				from voce_f
				where esercizio    		       = aEs
				  and ti_appartenenza 	 	   = aObb.ti_appartenenza
				  and ti_gestione 		 	   = aObb.ti_gestione
				  and cd_unita_organizzativa   = aObb.cd_unita_organizzativa
				  and cd_titolo_capitolo 	   = aObb.cd_elemento_voce
				  and cd_funzione		 	   = aLda.cd_funzione;
		   end if;
		end if;
	end if;

	return aVoce;
end;

function getVoceF(aObb obbligazione%rowtype,
		 		  aLdA v_linea_attivita_valida%rowtype,
				  aEV elemento_voce%rowtype,
				  aDettScad obbligazione_scad_voce%rowtype,
				  aEs number) return voce_f%rowtype is
aVoce voce_f%rowtype;
aCdTipoUnita varchar2(20);
oldLda linea_attivita%rowtype;
begin
	select cd_tipo_unita into aCdTipoUnita
	from unita_organizzativa
	where cd_unita_organizzativa = aObb.cd_cds
	  and fl_cds = 'Y';

	if aObb.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_IMP, CNRCTB018.TI_DOC_IMP_RES, CNRCTB018.TI_DOC_OBB_PGIRO) then
		-- non dipende dalla linea di attività, che è fittizia
	    select * into aVoce
		from voce_f
		where esercizio    	  = aEs
		  and ti_appartenenza = aEV.ti_appartenenza
		  and ti_gestione	  = aEV.ti_gestione
		  and cd_titolo_capitolo = aEV.cd_elemento_voce;
	else
		if aCdTipoUnita = CNRCTB020.TIPO_SAC then
		   if aObb.fl_spese_costi_altrui = 'Y' then
		   -- ricostruito in funzione del nuovo elemento voce, e della vecchia LdA
		   	    select * into oldLda
				from linea_attivita
				where cd_centro_responsabilita = aDettScad.cd_centro_responsabilita
				  and cd_linea_attivita = aDettScad.cd_linea_attivita;

				select * into aVoce
				from voce_f
				where esercizio    		       = aEs
				  and ti_appartenenza 	 	   = aEV.ti_appartenenza
				  and ti_gestione 		 	   = aEV.ti_gestione
				  and cd_titolo_capitolo 	   = aEV.cd_elemento_voce
				  and cd_funzione		 	   = oldLda.cd_funzione
				  and cd_centro_responsabilita = oldLda.cd_centro_responsabilita;
--
-- 			    select * into aVoce
-- 				from voce_f
-- 				where esercizio    	  = aEs
-- 				  and ti_appartenenza = aDettScad.ti_appartenenza
-- 				  and ti_gestione	  = aDettScad.ti_gestione
-- 				  and cd_voce		  = aDettScad.cd_voce;
		   else
				select * into aVoce
				from voce_f
				where esercizio    		       = aEs
				  and ti_appartenenza 	 	   = aEV.ti_appartenenza
				  and ti_gestione 		 	   = aEV.ti_gestione
				  and cd_titolo_capitolo 	   = aEV.cd_elemento_voce
				  and cd_funzione		 	   = aLda.cd_funzione
				  and cd_centro_responsabilita = aLda.cd_centro_responsabilita;
		   end if;
		else
		   if aObb.fl_spese_costi_altrui = 'Y' then
		   -- ricostruito in funzione del nuovo elemento voce, e della vecchia LdA
		   	    select * into oldLda
				from linea_attivita
				where cd_centro_responsabilita = aDettScad.cd_centro_responsabilita
				  and cd_linea_attivita = aDettScad.cd_linea_attivita;

				select * into aVoce
				from voce_f
				where esercizio    		       = aEs
				  and ti_appartenenza 	 	   = aEV.ti_appartenenza
				  and ti_gestione 		 	   = aEV.ti_gestione
				  and cd_titolo_capitolo 	   = aEV.cd_elemento_voce
				  and cd_funzione		 	   = oldLda.cd_funzione;

		   else
				select * into aVoce
				from voce_f
				where esercizio    		       = aEs
				  and ti_appartenenza 	 	   = aEV.ti_appartenenza
				  and ti_gestione 		 	   = aEV.ti_gestione
				  and cd_unita_organizzativa   = aObb.cd_unita_organizzativa
				  and cd_titolo_capitolo 	   = aEV.cd_elemento_voce
				  and cd_funzione		 	   = aLda.cd_funzione;
		   end if;
		end if;
	end if;

	return aVoce;
end;

function getPgVerRec(aObbNext obbligazione%rowtype) return number is
aNum number;
begin
 if aObbNext.cd_tipo_documento_cont In (CNRCTB018.TI_DOC_IMP_RES, CNRCTB018.TI_DOC_OBB_RES_PRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES) then
   Select nvl(max(pg_storico_),1)
   Into aNum
   from obbligazione_s
   where cd_cds    = aObbNext.cd_cds
     and esercizio = aObbNext.esercizio
     and esercizio_originale = aObbNext.esercizio_originale
     and pg_obbligazione = aObbNext.pg_obbligazione;
 else
     aNum := 1; -- per competenza la numerazione è sempre rigenerata
                -- non capiterà più ! 04.01.2006 stani
 end if;
 return aNum;
end;

function getPgVerRec(aAccNext accertamento%rowtype) return number is
aNum number;
begin
	if aAccNext.cd_tipo_documento_cont In (CNRCTB018.TI_DOC_ACC_RES, CNRCTB018.TI_DOC_ACC_PGIRO_RES) then
		select nvl(max(pg_storico_),1) into aNum
		from accertamento_s
		where cd_cds 	      = aAccNext.cd_cds
		  and esercizio	   	  = aAccNext.esercizio
		  and esercizio_originale = aAccNext.esercizio_originale
		  and pg_accertamento = aAccNext.pg_accertamento;
	else
		aNum := 1; -- per competenza la numerazione è sempre rigenerata
	end if;
	return aNum;
end;


procedure creaTestataObb(aObb obbligazione%rowtype, aObbNext in out obbligazione%rowtype, aEs number, aUser varchar2, aTSNow date) is
aIm number;
aPg number;
begin
    aIm := getImNonRiscontrato(aObb.cd_cds,aObb.esercizio,aObb.esercizio_originale,aObb.pg_obbligazione,aObb.ti_gestione);
-- dal 2006 restituisce sempre lo stesso pg_documento dell'esercizio precedente (praticamente è inutile la chiamata)
    aPg := getPg(aObb,aEs,aUser);

    aObbNext.CD_CDS				  :=aObb.CD_CDS;
    aObbNext.ESERCIZIO				  :=aEs;
    aObbNext.ESERCIZIO_ORIGINALE	 	  :=aObb.ESERCIZIO_ORIGINALE;
    aObbNext.PG_OBBLIGAZIONE		 	  :=aPg;
    aObbNext.CD_UNITA_ORGANIZZATIVA	 	  :=aObb.CD_UNITA_ORGANIZZATIVA;
    aObbNext.CD_CDS_ORIGINE			  :=aObb.CD_CDS_ORIGINE;
    aObbNext.CD_UO_ORIGINE			  :=aObb.CD_UO_ORIGINE;
    aObbNext.DT_REGISTRAZIONE		 	  :=to_date('0101'||aEs,'DDMMYYYY');
    aObbNext.DS_OBBLIGAZIONE		 	  :=aObb.DS_OBBLIGAZIONE;
    aObbNext.NOTE_OBBLIGAZIONE		 	  :=aObb.NOTE_OBBLIGAZIONE;
    aObbNext.CD_TERZO				  :=aObb.CD_TERZO;
    aObbNext.IM_OBBLIGAZIONE		 	  :=aIm;
    aObbNext.IM_COSTI_ANTICIPATI	 	  :=aObb.IM_COSTI_ANTICIPATI;

    aObbNext.ESERCIZIO_CONTRATTO  := aObb.ESERCIZIO_CONTRATTO;
    aObbNext.STATO_CONTRATTO      := aObb.STATO_CONTRATTO    ;
    aObbNext.PG_CONTRATTO         := aObb.PG_CONTRATTO       ;

    aObbNext.ESERCIZIO_REP        := aObb.ESERCIZIO_REP ;
    aObbNext.PG_REPERTORIO        := aObb.PG_REPERTORIO ;

	if aObb.esercizio_competenza <= aEs then
	   -- non viene generata una nuova obbligazione pluriennale
	   aObbNext.ESERCIZIO_COMPETENZA   	  :=aEs;
	else
	   -- è un'obbligazione pluriennale
	   aObbNext.ESERCIZIO_COMPETENZA	  := aObb.ESERCIZIO_COMPETENZA;
	end if;
    aObbNext.STATO_OBBLIGAZIONE		 	  :=aObb.STATO_OBBLIGAZIONE;
    aObbNext.CD_RIFERIMENTO_CONTRATTO	  :=aObb.CD_RIFERIMENTO_CONTRATTO;
    aObbNext.DT_SCADENZA_CONTRATTO	 	  :=aObb.DT_SCADENZA_CONTRATTO;
    aObbNext.FL_CALCOLO_AUTOMATICO	 	  := 'N'; -- aObb.FL_CALCOLO_AUTOMATICO;
    aObbNext.CD_FONDO_RICERCA		 	  :=aObb.CD_FONDO_RICERCA;
    aObbNext.FL_SPESE_COSTI_ALTRUI	 	  :=aObb.FL_SPESE_COSTI_ALTRUI;
    aObbNext.FL_PGIRO				  :=aObb.FL_PGIRO;
    aObbNext.RIPORTATO				  :='N';
    aObbNext.DACR				  :=aTSNow;
    aObbNext.UTCR				  :=aUser;
    aObbNext.DUVA				  :=aTSNow;
    aObbNext.UTUV				  :=aUser;
    aObbNext.PG_VER_REC				  :=getPgVerRec(aObbNext);-- per gestione storico
    aObbNext.CD_CDS_ORI_RIPORTO		 	  :=aObb.CD_CDS;
    aObbNext.ESERCIZIO_ORI_RIPORTO		  :=aObb.ESERCIZIO;
    aObbNext.ESERCIZIO_ORI_ORI_RIPORTO		  :=aObb.ESERCIZIO_ORIGINALE;
    aObbNext.PG_OBBLIGAZIONE_ORI_RIPORTO          :=aObb.PG_OBBLIGAZIONE;
    aObbNext.MOTIVAZIONE                          :=aObb.MOTIVAZIONE;

    aObbNext.FL_NETTO_SOSPESO   := aObb.FL_NETTO_SOSPESO ;
    aObbNext.FL_GARA_IN_CORSO   := aObb.FL_GARA_IN_CORSO ;
    aObbNext.DS_GARA_IN_CORSO   := aObb.DS_GARA_IN_CORSO ;
    aObbNext.stato_coge_docamm := aObb.stato_coge_docamm;
    aObbNext.stato_coge_doccont := aObb.stato_coge_doccont;
    aObbNext.FL_DETERMINA_ALLEGATA   := 'N' ;
End;

procedure creaScadObb(aObbNext obbligazione%rowtype,
		  			  aScad obbligazione_scadenzario%rowtype,
					  aObbScadNext in out obbligazione_scadenzario%rowtype,
					  aEs number,
					  aUser varchar2,
					  aTSNow date) is
aDt date;
begin
	aDt := getDtScadenza(aEs,aScad.DT_SCADENZA);

	aObbScadNext.CD_CDS	   	   			    :=aObbNext.CD_CDS;
	aObbScadNext.ESERCIZIO				   	:=aObbNext.ESERCIZIO;
	aObbScadNext.DT_SCADENZA				:=aDt;
	aObbScadNext.DS_SCADENZA				:=aScad.DS_SCADENZA;
	aObbScadNext.IM_SCADENZA				:=aScad.IM_SCADENZA - aScad.IM_ASSOCIATO_DOC_CONTABILE ;
	aObbScadNext.IM_ASSOCIATO_DOC_AMM	   	:=aScad.IM_ASSOCIATO_DOC_AMM - aScad.IM_ASSOCIATO_DOC_CONTABILE ;
	aObbScadNext.IM_ASSOCIATO_DOC_CONTABILE :=0;
	aObbScadNext.DACR					   	:=aTSNow;
	aObbScadNext.UTCR					   	:=aUser;
	aObbScadNext.DUVA					   	:=aTSNow;
	aObbScadNext.UTUV					   	:=aUser;
	aObbScadNext.PG_VER_REC				   	:=1;
	aObbScadNext.PG_OBBL_SCAD_ORI_RIPORTO   :=aScad.PG_OBBLIGAZIONE_SCADENZARIO;

end;

procedure creaScadDettObb(aObbScadNext obbligazione_scadenzario%rowtype,
		  			   	  aDettScad obbligazione_scad_voce%rowtype,
					   	  aObbScadDettNext in out obbligazione_scad_voce%rowtype,
					   	  aUser varchar2,
					   	  aTSNow date) is
begin
	aObbScadDettNext.CD_CDS					  :=aObbScadNext.CD_CDS;
	aObbScadDettNext.ESERCIZIO				  :=aObbScadNext.ESERCIZIO;
	aObbScadDettNext.TI_APPARTENENZA		  :=aDettScad.TI_APPARTENENZA;
	aObbScadDettNext.TI_GESTIONE			  :=aDettScad.TI_GESTIONE;
-- 	aObbScadDettNext.CD_VOCE				  :=aDettScad.CD_VOCE;
-- 	aObbScadDettNext.CD_CENTRO_RESPONSABILITA :=aDettScad.CD_CENTRO_RESPONSABILITA;
-- 	aObbScadDettNext.CD_LINEA_ATTIVITA		  :=aDettScad.CD_LINEA_ATTIVITA;
	aObbScadDettNext.IM_VOCE				  :=aDettScad.IM_VOCE;
	aObbScadDettNext.CD_FONDO_RICERCA		  :=aDettScad.CD_FONDO_RICERCA;
	aObbScadDettNext.DACR					  :=aTSNow;
	aObbScadDettNext.UTCR					  :=aUser;
	aObbScadDettNext.DUVA					  :=aTSNow;
	aObbScadDettNext.UTUV					  :=aUser;
	aObbScadDettNext.PG_VER_REC				  :=1;
end;

procedure creaTestataAcc(aAcc accertamento%rowtype, aAccNext in out accertamento%rowtype, aEs number, aUser varchar2, aTSNow date) is
aIm number;
aPg number;
begin
	aIm := getImNonRiscontrato(aAcc.cd_cds,aAcc.esercizio,aAcc.esercizio_originale,aAcc.pg_accertamento,aAcc.ti_gestione);
	aPg := getPg(aAcc, aEs, aUser);

	aAccNext.CD_CDS	   			          :=aAcc.CD_CDS;
	aAccNext.ESERCIZIO			      	  :=aEs;
	aAccNext.ESERCIZIO_ORIGINALE		      	  :=aAcc.ESERCIZIO_ORIGINALE;
	aAccNext.PG_ACCERTAMENTO	      	  :=aPg;
	aAccNext.CD_UNITA_ORGANIZZATIVA   	  :=aAcc.CD_UNITA_ORGANIZZATIVA;
	aAccNext.CD_CDS_ORIGINE		      	  :=aAcc.CD_CDS_ORIGINE;
	aAccNext.CD_UO_ORIGINE			  	  :=aAcc.CD_UO_ORIGINE;
-- 	aAccNext.TI_APPARTENENZA		  	  :=aAcc.TI_APPARTENENZA;
-- 	aAccNext.TI_GESTIONE			  	  :=aAcc.TI_GESTIONE;
-- 	aAccNext.CD_ELEMENTO_VOCE		  	  :=aAcc.CD_ELEMENTO_VOCE;
-- 	aAccNext.CD_VOCE				  	  :=aAcc.CD_VOCE;
	aAccNext.DT_REGISTRAZIONE		  	  :=to_date('0101'||aEs,'DDMMYYYY');
	aAccNext.DS_ACCERTAMENTO		  	  :=aAcc.DS_ACCERTAMENTO;
	aAccNext.NOTE_ACCERTAMENTO		  	  :=aAcc.NOTE_ACCERTAMENTO;
	aAccNext.CD_TERZO				  	  :=aAcc.CD_TERZO;
	aAccNext.IM_ACCERTAMENTO		  	  :=aIm;
	aAccNext.DT_CANCELLAZIONE		  	  :=aAcc.DT_CANCELLAZIONE;
	aAccNext.CD_RIFERIMENTO_CONTRATTO 	  :=aAcc.CD_RIFERIMENTO_CONTRATTO;
	aAccNext.DT_SCADENZA_CONTRATTO	  	  :=aAcc.DT_SCADENZA_CONTRATTO;
	aAccNext.CD_FONDO_RICERCA		  	  :=aAcc.CD_FONDO_RICERCA;
	aAccNext.FL_PGIRO				  	  :=aAcc.FL_PGIRO;
	aAccNext.RIPORTATO				  	  :='N';
	aAccNext.DACR					  	  :=aTSNow;
	aAccNext.UTCR					  	  :=aUser;
	aAccNext.DUVA					  	  :=aTSNow;
	aAccNext.UTUV					  	  :=aUser;
	aAccNext.PG_VER_REC				  	  :=getPgVerRec(aAccNext); -- per gestione storico
	aAccNext.CD_CDS_ORI_RIPORTO		  	  :=aAcc.CD_CDS;
	aAccNext.ESERCIZIO_ORI_RIPORTO	  	  :=aAcc.ESERCIZIO;
	aAccNext.ESERCIZIO_ORI_ORI_RIPORTO  	  :=aAcc.ESERCIZIO_ORIGINALE;
	aAccNext.PG_ACCERTAMENTO_ORI_RIPORTO  :=aAcc.PG_ACCERTAMENTO;

  aAccNext.ESERCIZIO_CONTRATTO := aAcc.ESERCIZIO_CONTRATTO;
  aAccNext.STATO_CONTRATTO     := aAcc.STATO_CONTRATTO    ;
  aAccNext.PG_CONTRATTO        := aAcc.PG_CONTRATTO       ;

  aAccNext.FL_NETTO_SOSPESO   := aAcc.FL_NETTO_SOSPESO ;
  aAccNext.stato_coge_docamm := aAcc.stato_coge_docamm;
  aAccNext.stato_coge_doccont := aAcc.stato_coge_doccont;

	if aAcc.esercizio_competenza <= aEs then
	    aAccNext.ESERCIZIO_COMPETENZA	  := aEs;
	else
		aAccNext.ESERCIZIO_COMPETENZA	  :=aAcc.ESERCIZIO_COMPETENZA;
	end if;
--	aAccNext.PG_ACCERTAMENTO_ORIGINE	 :=aAcc.PG_ACCERTAMENTO_ORIGINE;

end;

procedure creaScadAcc(aAccNext accertamento%rowtype,
		  			  aScad accertamento_scadenzario%rowtype,
					  aAccScadNext in out accertamento_scadenzario%rowtype,
					  aEs number,
					  aUser varchar2,
					  aTSNow date) is
aDt date;
begin
	aDt := getDtScadenza(aEs,aScad.DT_SCADENZA_INCASSO);

	aAccScadNext.CD_CDS						   :=aAccNext.CD_CDS;
	aAccScadNext.ESERCIZIO					   :=aAccNext.ESERCIZIO;
-- 	aAccScadNext.PG_ACCERTAMENTO			   :=aAccNext.PG_ACCERTAMENTO;
-- 	aAccScadNext.PG_ACCERTAMENTO_SCADENZARIO   :=aScad.PG_ACCERTAMENTO_SCADENZARIO;
	aAccScadNext.DT_SCADENZA_EMISSIONE_FATTURA :=aDt;
	aAccScadNext.DT_SCADENZA_INCASSO		   :=aDt;
	aAccScadNext.DS_SCADENZA				   :=aScad.DS_SCADENZA;
	aAccScadNext.IM_SCADENZA				   :=aScad.IM_SCADENZA - aScad.IM_ASSOCIATO_DOC_CONTABILE;
	aAccScadNext.IM_ASSOCIATO_DOC_AMM		   :=aScad.IM_ASSOCIATO_DOC_AMM - aScad.IM_ASSOCIATO_DOC_CONTABILE;
	aAccScadNext.IM_ASSOCIATO_DOC_CONTABILE	   :=0;
	aAccScadNext.DACR						   :=aTSNow;
	aAccScadNext.UTCR						   :=aUser;
	aAccScadNext.DUVA						   :=aTSNow;
	aAccScadNext.UTUV						   :=aUser;
	aAccScadNext.PG_VER_REC					   :=1;
	aAccScadNext.PG_ACC_SCAD_ORI_RIPORTO	   :=aScad.PG_ACCERTAMENTO_SCADENZARIO;
end;

procedure creaScadDettAcc(aAccScadNext accertamento_scadenzario%rowtype,
		  aDettScad accertamento_scad_voce%rowtype,
		  aAccDettScadNext in out accertamento_scad_voce%rowtype,
		  aUser varchar2,
		  aTSNow date) is
begin
	aAccDettScadNext.CD_CDS	  	  			  	 :=aAccScadNext.CD_CDS;
	aAccDettScadNext.ESERCIZIO					 :=aAccScadNext.ESERCIZIO;
-- 	aAccDettScadNext.CD_CENTRO_RESPONSABILITA 	 :=aDettScad.CD_CENTRO_RESPONSABILITA;
-- 	aAccDettScadNext.CD_LINEA_ATTIVITA		  	 :=aDettScad.CD_LINEA_ATTIVITA;
	aAccDettScadNext.IM_VOCE				  	 :=aDettScad.IM_VOCE;
	aAccDettScadNext.CD_FONDO_RICERCA		  	 :=aDettScad.CD_FONDO_RICERCA;
	aAccDettScadNext.DACR					  	 :=aTSNow;
	aAccDettScadNext.UTCR					  	 :=aUser;
	aAccDettScadNext.DUVA					  	 :=aTSNow;
	aAccDettScadNext.UTUV					  	 :=aUser;
	aAccDettScadNext.PG_VER_REC				  	 :=1;

end;

 function getDocRiportato(aObb obbligazione%rowtype) return obbligazione%rowtype is
  aObbNew obbligazione%rowtype;
 begin
  begin
   select * into aObbNew
   from obbligazione
   Where cd_cds_ori_riporto  =aObb.cd_cds And
         esercizio_ori_riporto = aObb.esercizio And
         esercizio_ori_ori_riporto  =aObb.esercizio_originale And
         pg_obbligazione_ori_riporto=aObb.pg_obbligazione
   for update nowait;
  exception
   when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento riportato in nuovo esercizio non trovato collegato a:'||CNRCTB035.getDesc(aObb));
   when TOO_MANY_ROWS then
    IBMERR001.RAISE_ERR_GENERICO('Esiste più di un documento nel nuovo esercizio corrispondente al documento:'||CNRCTB035.getDesc(aObb));
  end;
  return aObbNew;
 end;

 function getOldScad(aObbNew obbligazione%rowtype, aObbScadNew obbligazione_scadenzario%rowtype) return obbligazione_scadenzario%rowtype is
  aObbScad obbligazione_scadenzario%rowtype;
 begin
  begin
   select * into aObbScad from obbligazione_scadenzario where
             cd_cds  =aObbNew.cd_cds_ori_riporto
		 and esercizio = aObbNew.esercizio_ori_riporto
		 and esercizio_originale=aObbNew.esercizio_ori_ori_riporto
		 and pg_obbligazione=aObbNew.pg_obbligazione_ori_riporto
		 and pg_obbligazione_scadenzario=aObbScadNew.pg_obbl_scad_ori_riporto
   for update nowait;
  exception
   when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Scadenza di documento corrispondente a scadenza riportata:'||CNRCTB035.getDesc(aObbNew, aObbScadNew)||' non trovata');
  end;
  return aObbScad;
 end;

 function getOldScad(aAccNew accertamento%rowtype, aAccScadNew accertamento_scadenzario%rowtype) return accertamento_scadenzario%rowtype is
  aAccScad accertamento_scadenzario%rowtype;
 begin
  begin
   select * into aAccScad from accertamento_scadenzario where
             cd_cds  =aAccNew.cd_cds_ori_riporto
		 and esercizio = aAccNew.esercizio_ori_riporto
		 and esercizio_originale=aAccNew.esercizio_ori_ori_riporto
		 and pg_accertamento=aAccNew.pg_accertamento_ori_riporto
		 and pg_accertamento_scadenzario=aAccScadNew.pg_acc_scad_ori_riporto
   for update nowait;
  exception
   when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Scadenza di documento corrispondente a scadenza riportata:'||CNRCTB035.getDesc(aAccNew, aAccScadNew)||' non trovata');
  end;
  return aAccScad;
 end;

 function getDocRiportato(aAcc accertamento%rowtype) return accertamento%rowtype is
  aAccNew accertamento%rowtype;
 begin
  begin
   select * into aAccNew from accertamento where
             cd_cds_ori_riporto  =aAcc.cd_cds
		 and esercizio_ori_riporto = aAcc.esercizio
		 and esercizio_ori_ori_riporto=aAcc.esercizio_originale
		 and pg_accertamento_ori_riporto=aAcc.pg_accertamento
   for update nowait;
  exception
   when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento riportato in nuovo esercizio non trovato colleato a:'||CNRCTB035.getDesc(aAcc));
   when TOO_MANY_ROWS then
    IBMERR001.RAISE_ERR_GENERICO('Esiste più di un documento nel nuovo esercizio corrispondente al documento:'||CNRCTB035.getDesc(aAcc));
  end;
  return aAccNew;
 end;

 function getDocOrigine(aObbNew obbligazione%rowtype) return obbligazione%rowtype is
  aObb obbligazione%rowtype;
 begin
  begin
   select * into aObb from obbligazione where
             cd_cds  =aObbNew.cd_cds_ori_riporto
		 and esercizio = aObbNew.esercizio_ori_riporto
		 and esercizio_originale=aObbNew.esercizio_ori_ori_riporto
		 and pg_obbligazione=aObbNew.pg_obbligazione_ori_riporto
   for update nowait;
  exception
   when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento di origine non trovato collegato a:'||CNRCTB035.getDesc(aObbNew));
   when TOO_MANY_ROWS then
    IBMERR001.RAISE_ERR_GENERICO('Esiste più di un documento di origine corrispondente al documento:'||CNRCTB035.getDesc(aObbNew));
  end;
  return aObb;
 end;

 function getDocOrigine(aAccNew accertamento%rowtype) return accertamento%rowtype is
  aAcc accertamento%rowtype;
 begin
  begin
   select * into aAcc from accertamento where
             cd_cds  =aAccNew.cd_cds_ori_riporto
		 and esercizio = aAccNew.esercizio_ori_riporto
		 and esercizio_originale=aAccNew.esercizio_ori_ori_riporto
		 and pg_accertamento=aAccNew.pg_accertamento_ori_riporto
   for update nowait;
  exception
   when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento di origine non trovato collegato a:'||CNRCTB035.getDesc(aAccNew));
   when TOO_MANY_ROWS then
    IBMERR001.RAISE_ERR_GENERICO('Esiste più di un documento di origine corrispondente al documento:'||CNRCTB035.getDesc(aAccNew));
  end;
  return aAcc;
 end;

procedure aggiornaAccNext(aAccNext accertamento%rowtype) is
aLocAcc accertamento%rowtype;
begin
	aLocAcc := aAccNext;
    -- lock del documento esistente
    cnrctb035.LOCKDOCFULL(aLocAcc);

	update accertamento
	set CD_TIPO_DOCUMENTO_CONT	 = aAccNext.cd_tipo_documento_cont,
		CD_UNITA_ORGANIZZATIVA	 = aAccNext.cd_unita_organizzativa,
		CD_CDS_ORIGINE			 = aAccNext.cd_cds_origine,
		CD_UO_ORIGINE			 = aAccNext.cd_uo_origine,
		TI_APPARTENENZA			 = aAccNext.ti_appartenenza,
		TI_GESTIONE				 = aAccNext.ti_gestione,
		CD_ELEMENTO_VOCE		 = aAccNext.cd_elemento_voce,
		CD_VOCE					 = aAccNext.cd_voce,
		DT_REGISTRAZIONE		 = aAccNext.dt_registrazione,
		DS_ACCERTAMENTO			 = aAccNext.ds_accertamento,
		NOTE_ACCERTAMENTO		 = aAccNext.note_accertamento,
		CD_TERZO				 = aAccNext.cd_terzo,
		IM_ACCERTAMENTO			 = aAccNext.im_accertamento,
		DT_CANCELLAZIONE		 = aAccNext.dt_cancellazione,
		CD_RIFERIMENTO_CONTRATTO = aAccNext.cd_riferimento_contratto,
		DT_SCADENZA_CONTRATTO	 = aAccNext.dt_scadenza_contratto,
		CD_FONDO_RICERCA		 = aAccNext.cd_fondo_ricerca,
		FL_PGIRO				 = aAccNext.fl_pgiro,
		RIPORTATO				 = aAccNext.riportato,
		DUVA					 = aAccNext.duva,
		UTUV					 = aAccNext.utuv,
		PG_VER_REC				 = pg_ver_rec + 1,
		CD_CDS_ORI_RIPORTO		 = aAccNext.cd_cds_ori_riporto,
		ESERCIZIO_ORI_RIPORTO	 = aAccNext.esercizio_ori_riporto,
		ESERCIZIO_ORI_ORI_RIPORTO	 = aAccNext.esercizio_ori_ori_riporto,
		PG_ACCERTAMENTO_ORI_RIPORTO = aAccNext.pg_accertamento_ori_riporto,
		ESERCIZIO_COMPETENZA		= aAccNext.esercizio_competenza,
		PG_ACCERTAMENTO_ORIGINE		= aAccNext.pg_accertamento_origine
	where cd_cds          = aAccNext.cd_cds
	  and esercizio 	  = aAccNext.esercizio
	  and esercizio_originale = aAccNext.esercizio_originale
	  and pg_accertamento = aAccNext.pg_accertamento;

end;

procedure aggiornaImpNext(aObbNext obbligazione%rowtype) is
aLocObb obbligazione%rowtype;
begin
	aLocObb := aObbNext;
    -- lock del documento esistente
    cnrctb035.LOCKDOCFULL(aLocObb);

	update obbligazione
	set CD_TIPO_DOCUMENTO_CONT       = aObbNext.cd_tipo_documento_cont,
		CD_UNITA_ORGANIZZATIVA		 = aObbNext.cd_unita_organizzativa,
		CD_CDS_ORIGINE				 = aObbNext.cd_cds_origine,
		CD_UO_ORIGINE				 = aObbNext.cd_uo_origine,
		CD_TIPO_OBBLIGAZIONE		 = aObbNext.cd_tipo_obbligazione,
		TI_APPARTENENZA				 = aObbNext.ti_appartenenza,
		TI_GESTIONE					 = aObbNext.ti_gestione,
		CD_ELEMENTO_VOCE			 = aObbNext.cd_elemento_voce,
		DT_REGISTRAZIONE			 = aObbNext.dt_registrazione,
		DS_OBBLIGAZIONE				 = aObbNext.ds_obbligazione,
		NOTE_OBBLIGAZIONE			 = aObbNext.note_obbligazione,
		CD_TERZO					 = aObbNext.cd_terzo,
		IM_OBBLIGAZIONE				 = aObbNext.im_obbligazione,
		IM_COSTI_ANTICIPATI			 = aObbNext.im_costi_anticipati,
		ESERCIZIO_COMPETENZA		 = aObbNext.esercizio_competenza,
		STATO_OBBLIGAZIONE			 = aObbNext.stato_obbligazione,
		DT_CANCELLAZIONE			 = aObbNext.dt_cancellazione,
		CD_RIFERIMENTO_CONTRATTO	 = aObbNext.cd_riferimento_contratto,
		DT_SCADENZA_CONTRATTO		 = aObbNext.dt_scadenza_contratto,
		FL_CALCOLO_AUTOMATICO		 = aObbNext.fl_calcolo_automatico,
		CD_FONDO_RICERCA			 = aObbNext.cd_fondo_ricerca,
		FL_SPESE_COSTI_ALTRUI		 = aObbNext.fl_spese_costi_altrui,
		FL_PGIRO					 = aObbNext.fl_pgiro,
		RIPORTATO					 = aObbNext.riportato,
		DUVA						 = aObbNext.duva,
		UTUV						 = aObbNext.utuv,
		PG_VER_REC					 = pg_ver_rec + 1,
		CD_CDS_ORI_RIPORTO			 = aObbNext.cd_cds_ori_riporto,
		ESERCIZIO_ORI_RIPORTO		 = aObbNext.esercizio_ori_riporto,
		ESERCIZIO_ORI_ORI_RIPORTO	 = aObbNext.esercizio_ori_ori_riporto,
		PG_OBBLIGAZIONE_ORI_RIPORTO	 = aObbNext.pg_obbligazione_ori_riporto
	where cd_cds = aObbNext.cd_cds
	  and esercizio = aObbNext.esercizio
	  and esercizio_originale = aObbNext.esercizio_originale
	  and pg_obbligazione = aObbNext.pg_obbligazione;

end;


procedure aggiornaScadImpNext(
		  aObbNext obbligazione%rowtype,
		  aObbScadNext obbligazione_scadenzario%rowtype,
		  posizione number,
		  ListaObbScadVoceNext CNRCTB035.scadVoceListS,
		  isControlloBloccante boolean) is
begin
-- solo per impegni!!!
-- => un solo livello di scadenza e scad voce
	 if ListaObbScadVoceNext.count > 0 then -- dovrebbe essere count = 1
	 	update obbligazione_scadenzario
		set DT_SCADENZA                 = aObbScadNext.dt_scadenza,
			DS_SCADENZA					= aObbScadNext.ds_scadenza,
			IM_SCADENZA					= aObbScadNext.im_scadenza,
			IM_ASSOCIATO_DOC_AMM		= aObbScadNext.im_associato_doc_amm,
			IM_ASSOCIATO_DOC_CONTABILE	= aObbScadNext.im_associato_doc_contabile,
			DUVA						= aObbScadNext.duva,
			UTUV						= aObbScadNext.utuv,
			PG_VER_REC					= pg_ver_rec + 1,
			PG_OBBL_SCAD_ORI_RIPORTO	= aObbScadNext.pg_obbl_scad_ori_riporto
		where cd_cds 					  = aObbNext.cd_cds
		  and esercizio 				  = aObbNext.esercizio
		  and esercizio_originale = aObbNext.esercizio_originale
		  and pg_obbligazione 			  = aObbNext.pg_obbligazione
		  and pg_obbligazione_scadenzario = posizione;

		for i in 1..ListaObbScadVoceNext.count loop
			update obbligazione_scad_voce
			set TI_APPARTENENZA			 = ListaObbScadVoceNext(i).ti_appartenenza,
				TI_GESTIONE				 = ListaObbScadVoceNext(i).ti_gestione,
				CD_VOCE					 = ListaObbScadVoceNext(i).cd_voce,
				CD_CENTRO_RESPONSABILITA = ListaObbScadVoceNext(i).cd_centro_responsabilita,
				CD_LINEA_ATTIVITA		 = ListaObbScadVoceNext(i).cd_linea_attivita,
				IM_VOCE					 = ListaObbScadVoceNext(i).im_voce,
				CD_FONDO_RICERCA		 = ListaObbScadVoceNext(i).cd_fondo_ricerca,
				DUVA					 = ListaObbScadVoceNext(i).duva,
				UTUV					 = ListaObbScadVoceNext(i).utuv,
				PG_VER_REC				 = pg_ver_rec + 1
			where cd_cds 				      = aObbNext.cd_cds
			  and esercizio 				  = aObbNext.esercizio
			  and esercizio_originale = aObbNext.esercizio_originale
			  and pg_obbligazione 			  = aObbNext.pg_obbligazione
			  and pg_obbligazione_scadenzario = posizione;

			-- aggiorno i saldi
			if aObbNext.esercizio = aObbNext.esercizio_competenza then
			-- obbligazione non pluriennale => aggiorno i saldi
			   CNRCTB035.aggiornaSaldoDettScad(aObbNext,ListaObbScadVoceNext(i),ListaObbScadVoceNext(i).im_voce, isControlloBloccante,ListaObbScadVoceNext(i).utuv, ListaObbScadVoceNext(i).duva);
			end if;

		end loop;
	 end if;
end;

-- restituisce true se l'accertamento è stato completamente incassato
Function isAccEsaurito (aAcc accertamento%Rowtype) Return Boolean Is
 tot_associato_rev  NUMBER;

Begin
 Select Sum(b.im_associato_doc_contabile)
 Into   tot_associato_rev
 From   accertamento a, accertamento_scadenzario b
 Where  a.cd_cds = aAcc.cd_cds And
        a.esercizio = aAcc.esercizio And
        a.esercizio_originale = aAcc.esercizio_originale And
	a.pg_accertamento = aAcc.pg_accertamento And
	a.cd_cds = b.cd_cds And
	a.esercizio = b.esercizio And
	a.esercizio_originale = b.esercizio_originale And
	a.pg_accertamento = b.pg_accertamento And
	a.DT_CANCELLAZIONE is Null And
	a.riportato = 'N';

 If  tot_associato_rev = aAcc.im_accertamento Then
    Return True;
 Else
    Return False;
 End If;
End;

-- restituisce true se l'obbligazione è stata completamente incassata
Function isObbEsaurita (aObb obbligazione%Rowtype) Return Boolean Is
 tot_associato_man  NUMBER;
Begin
 Select Sum(b.im_associato_doc_contabile)
 Into   tot_associato_man
 From   obbligazione a, obbligazione_scadenzario b
 Where  a.cd_cds = aObb.cd_cds And
        a.esercizio = aObb.esercizio And
        a.esercizio_originale = aObb.esercizio_originale And
	a.pg_obbligazione = aObb.pg_obbligazione And
	a.cd_cds = b.cd_cds And
	a.esercizio = b.esercizio And
	a.esercizio_originale = b.esercizio_originale And
	a.pg_obbligazione = b.pg_obbligazione And
	a.DT_CANCELLAZIONE is Null And
	a.riportato = 'N';

 If  tot_associato_man = aObb.im_obbligazione Then
    Return True;
 Else
    Return False;
 End If;
End;

end;
