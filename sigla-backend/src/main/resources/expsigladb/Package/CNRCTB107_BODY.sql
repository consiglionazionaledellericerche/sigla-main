--------------------------------------------------------
--  DDL for Package Body CNRCTB107
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB107" is

 function getDesc(aTiDoc varchar,aCdCds varchar2,aEs number,aCdUo varchar2,aPgDoc number) return varchar2 is
  aStringa varchar2(1000);
 begin
  aStringa:='';
  aStringa:=aStringa||' Cds:'||aCdCds;
  aStringa:=aStringa||' Uo:'||aCdUo;
  aStringa:=aStringa||' Es:'||aEs;
  aStringa:=aStringa||' Tipo:'||aTiDoc;
  aStringa:=aStringa||' Pg_doc:'||aPgDoc;
  return aStringa;
 end;

 procedure job_congelaFatturaEsChiuso(job number, pg_exec number, next_date date, aTiDoc varchar2, aCdCds varchar2, aEs number, aCdUo varchar2, aPgDoc number) is
  aTSNow date;
  aUser varchar2(20);
  aNum number;
  aNumeroProcessati number;
 begin
  if CNRCTB008.ESERCIZIO_PARTENZA=aEs then
   IBMERR001.RAISE_ERR_GENERICO('Non è possibile effetuare il congelamento di fatture nell''esercizio di partenza dell''applicazione');
  end if;

  if not CNRCTB008.ISESERCIZIOAPERTO(aEs,aCdCds) then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non è aperto per il cds:'||aCdCds);
  end if;

  if not CNRCTB008.ISESERCIZIOCHIUSO(aEs-1,aCdCds)  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||(aEs-1)||' non è chiuso per il cds:'||aCdCds);
  end if;

  aNumeroProcessati:=0;
  aTSNow:=sysdate;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);
  -- Aggiorna le info di testata del log
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_JOB_CONG_FAT_ES_CH, job, 'Annullamento fatture di es. chiuso cds: '||nvl(aCdCds,'*')||' UO: '||nvl(aCdUo,'*')||' PG_DOC: '||nvl(to_char(aPgDoc),'*')||' Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));
  -- Ciclo su esercizi di provenienza dei documenti
  for aEsercizio in (select esercizio from esercizio_base where esercizio < aEs and esercizio >= CNRCTB008.ESERCIZIO_PARTENZA order by esercizio desc) loop
   -- Ciclo sui CDS validi in tali esercizi
   for aCds in (select * from v_unita_organizzativa_valida where
                       esercizio = aEsercizio.esercizio
 				  and cd_unita_organizzativa = nvl(aCdCds,cd_unita_organizzativa)
 				  and fl_cds = 'Y'
   ) loop
    if not CNRCTB008.ISESERCIZIOCHIUSO(aEsercizio.esercizio,aCdCds) then
     IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non è chiuso per il cds:'||aCdCds);
    end if;
    -- Ciclo sui UO validi di cds validi in tali esercizi
    for aUo in (select * from v_unita_organizzativa_valida where
                       esercizio = aEsercizio.esercizio
 				  and cd_unita_padre = aCds.cd_unita_organizzativa
 				  and cd_unita_organizzativa = nvl(aCdUo,cd_unita_organizzativa)
 				  and fl_cds = 'N'
    ) loop
     for aFattura in (select distinct cd_tipo_documento_amm, ti_fattura, cd_cds, esercizio, cd_unita_organizzativa, pg_documento_amm, stato_pagamento_fondo_eco from V_DOC_AMM_OBB where
                              cd_cds=aUo.cd_unita_padre
         				  and cd_unita_organizzativa=aUo.cd_unita_organizzativa
         				  and cd_tipo_documento_amm in (CNRCTB100.TI_FATTURA_PASSIVA,CNRCTB100.TI_FATTURA_ATTIVA)
 						  and cd_tipo_documento_amm = nvl(aTiDoc,cd_tipo_documento_amm)
         				  and esercizio=aEsercizio.esercizio
						  and fl_congelata = 'N'
                          -- SOLO FATTURE PROTOCOLLATE SONO CONGELABILI
						  and protocollo_iva is not null
         				  and pg_documento_amm=nvl(aPgDoc,pg_documento_amm)
         				  and stato_cofi not in ('P','A')
         				  and
         				      (
         					      ti_fattura = CNRCTB100.TI_FATT_FATTURA
         					   or cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_ATTIVA and ti_fattura = CNRCTB100.TI_FATT_NOTA_C
         					  )
         				  and esercizio_obbligazione != esercizio
         				  and esercizio_obbligazione is not null
         		  		  and esercizio_obbligazione = aEs
     ) loop
      begin
       CNRCTB100.LOCKDOCAMM(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm);
       CNRCTB100.LOCKDOCAMMRIGA(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm,null);
	   aNumeroProcessati:=aNumeroProcessati+1;

       -- Check che non ci siano mandati emessi/esitati nell'anno di congelamento sulla fattura

       for aTrash in (select 1 from mandato_riga mr where
              cd_cds_doc_amm=aFattura.cd_cds
          and cd_uo_doc_amm=aFattura.cd_unita_organizzativa
          and cd_tipo_documento_amm = aFattura.cd_tipo_documento_amm
          and esercizio_doc_amm=aFattura.esercizio
          and pg_doc_amm=aFattura.pg_documento_amm
		  -- Posso congelare solo se NON esistono mandati emessi o esitati nell'anno di congelamento
		  and esercizio = aEs
          and exists (select 1 from mandato where
		           cd_cds=mr.cd_cds
			   and esercizio=mr.esercizio
			   and pg_mandato=mr.pg_mandato
		       and stato in (CNRCTB038.STATO_AUT_EME, CNRCTB038.STATO_AUT_ESI)
		  )
	    for update nowait
	   ) loop
        IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a mandato emesso o esitato nell''anno di congelamento ('||aEs||')');
	   end loop;


       if aFattura.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_PASSIVA then

        -- Check che non ci siano fatture da transitare per fondo economale

        if aFattura.stato_pagamento_fondo_eco <> CNRCTB100.STATO_NO_PFONDOECO then
         IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' risulta associata o da associare a spesa del fondo economale');
	    end if;

	    -- Check che non ci siano lettere di pagamento con sospeso associato sulla fattura nell'anno di congelamento

        for aTrash1 in (select 1 from fattura_passiva fp where
              cd_cds=aFattura.cd_cds
          and cd_unita_organizzativa=aFattura.cd_unita_organizzativa
          and esercizio=aFattura.esercizio
          and pg_fattura_passiva=aFattura.pg_documento_amm
          and exists (select 1 from lettera_pagam_estero where
		           cd_cds=fp.cd_cds
			   and esercizio=fp.esercizio
			   and pg_lettera=fp.pg_lettera
               and esercizio=fp.esercizio_lettera
               and esercizio = aEs
			   and cd_sospeso is not null
		  )
	     for update nowait
	    ) loop
         IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a modulo 1210 associato a sospeso nell''anno di congelamento ('||aEs||')');
	    end loop;
       end if;

	   -- Start operazioni di modifica
       CNRCTB100.UPDATEDOCAMM(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm,
        ' fl_congelata = ''Y''',
        null,
        aUser,
        aTsNow
       );

       if aFattura.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_PASSIVA then
        -- Ciclo su tutte le righe della fattura con esercizio_obbligazione = all'esercizio di congelamento
        for aRiga in (select * from fattura_passiva_riga where
               cd_cds=aFattura.cd_cds
           and cd_unita_organizzativa=aFattura.cd_unita_organizzativa
           and esercizio=aFattura.esercizio
           and esercizio_obbligazione=aEs
           and pg_fattura_passiva=aFattura.pg_documento_amm)
        loop
         for aNotaC in (select * from fattura_passiva an where
                               ti_fattura = CNRCTB100.TI_FATT_NOTA_C
                           and exists (select 1 from fattura_passiva_riga where
                                               cd_cds_obbligazione = aRiga.cd_cds_obbligazione
         								   and esercizio_obbligazione=aRiga.esercizio_obbligazione
         								   and esercizio_ori_obbligazione = aRiga.esercizio_ori_obbligazione
         								   and pg_obbligazione = aRiga.pg_obbligazione
         								   and pg_obbligazione_scadenzario = aRiga.pg_obbligazione_scadenzario
         						           and cd_cds = an.cd_cds
										   and esercizio = an.esercizio
										   and cd_unita_organizzativa = an.cd_unita_organizzativa
										   and pg_fattura_passiva = an.pg_fattura_passiva)
								for update nowait)
         loop
		   if aNotaC.esercizio = aEs then
            IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a nota di credito generata nell''esercizio di congelamento ('||aEs||')');
		   end if;
           begin
            select distinct 1 into aNum from fattura_passiva_riga where
                    (
                         cd_cds_assncna_fin<>aFattura.cd_cds
                      or cd_uo_assncna_fin<>aFattura.cd_unita_organizzativa
                      or esercizio_assncna_fin<>aFattura.esercizio
                      or pg_fattura_assncna_fin<>aFattura.pg_documento_amm
            )
                     and cd_cds=aNotaC.cd_cds
                     and cd_unita_organizzativa=aNotaC.cd_unita_organizzativa
                     and esercizio=aNotaC.esercizio
                     and pg_fattura_passiva=aNotaC.pg_fattura_passiva;
                  IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a nota di credito collegata anche ad altra fattura');
           exception when NO_DATA_FOUND then
           null;
           end;
           CNRCTB100.UPDATEDOCAMM(aFattura.cd_tipo_documento_amm,aNotaC.cd_cds,aNotaC.esercizio,aNotaC.cd_unita_organizzativa,aNotaC.pg_fattura_passiva,
         	  ' fl_congelata = ''Y''',
         	  null,
         	  aUser,
          		  aTsNow
              );
         end loop;
         for aNotaD in (select * from fattura_passiva an where
                               ti_fattura = CNRCTB100.TI_FATT_NOTA_D
                           and exists (select 1 from fattura_passiva_riga where
                                               cd_cds_obbligazione = aRiga.cd_cds_obbligazione
         								   and esercizio_obbligazione=aRiga.esercizio_obbligazione
         								   and esercizio_ori_obbligazione = aRiga.esercizio_ori_obbligazione
         								   and pg_obbligazione = aRiga.pg_obbligazione
         								   and pg_obbligazione_scadenzario = aRiga.pg_obbligazione_scadenzario
         						           and cd_cds = an.cd_cds
										   and esercizio = an.esercizio
										   and cd_unita_organizzativa = an.cd_unita_organizzativa
										   and pg_fattura_passiva = an.pg_fattura_passiva)
								for update nowait)
         loop
           if aNotaD.esercizio = aEs then
            IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a nota di debito generata nell''esercizio di congelamento ('||aEs||')');
           end if;
           begin
            select distinct 1 into aNum from fattura_passiva_riga where
                     (
                         cd_cds_assncna_fin<>aFattura.cd_cds
                      or cd_uo_assncna_fin<>aFattura.cd_unita_organizzativa
                      or esercizio_assncna_fin<>aFattura.esercizio
                      or pg_fattura_assncna_fin<>aFattura.pg_documento_amm
            )
                     and cd_cds=aNotaD.cd_cds
                     and cd_unita_organizzativa=aNotaD.cd_unita_organizzativa
                     and esercizio=aNotaD.esercizio
                     and pg_fattura_passiva=aNotaD.pg_fattura_passiva;
                  IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a nota di debito collegata anche ad altra fattura');
           exception when NO_DATA_FOUND then
           null;
           end;
           CNRCTB100.UPDATEDOCAMM(aFattura.cd_tipo_documento_amm,aNotaD.cd_cds,aNotaD.esercizio,aNotaD.cd_unita_organizzativa,aNotaD.pg_fattura_passiva,
         	  ' fl_congelata = ''Y''',
         	  null,
         	  aUser,
          		  aTsNow
              );
         end loop;
        end loop;
       end if;

       for aObbS in (select * from obbligazione_scadenzario os where
         		      exists (select 1 from V_DOC_AMM_OBB where
                                       cd_cds=aFattura.cd_cds
                                   and esercizio = aFattura.esercizio
								   and cd_tipo_documento_amm = aFattura.cd_tipo_documento_amm
                                   and cd_unita_organizzativa = aFattura.cd_unita_organizzativa
                                   and pg_documento_amm = aFattura.pg_documento_amm
								   and cd_cds_obbligazione = os.cd_cds
								   and esercizio_obbligazione = os.esercizio
         						           and esercizio_ori_obbligazione = os.esercizio_originale
								   and pg_obbligazione = os.pg_obbligazione
								   and pg_obbligazione_scadenzario  =os.pg_obbligazione_scadenzario
                                   and esercizio_obbligazione > esercizio
                                   and esercizio_obbligazione is not null
                                   and esercizio_obbligazione = aEs
                                   and stato_cofi not in ('P','A')
                      )
         		  for update nowait
       ) loop
        CNRCTB035.AGGIORNASALDODOCAMMOBB(aObbS.cd_cds, aObbS.esercizio, aObbS.esercizio_originale, aObbS.pg_obbligazione, aObbS.pg_obbligazione_scadenzario, 0-aObbS.im_scadenza, aUser);
        CNRCTB035.ANNULLASCADOBBLIGAZIONE(aObbS.cd_cds, aObbS.esercizio, aObbS.esercizio_originale, aObbS.pg_obbligazione, aObbS.pg_obbligazione_scadenzario, aUser);
       end loop;
       IBMUTL200.logInf(pg_exec, 'ANNULLOK',null,'CF-'||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm));
       commit;
 	 exception when others then
       IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK, null,'CF-'||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm));
 	  rollback;
 	 end;
     end loop;
 	for aFattura in (select distinct cd_tipo_documento_amm, ti_fattura, cd_cds, esercizio, cd_unita_organizzativa, pg_documento_amm from V_DOC_AMM_ACC where
                                  cd_cds=aCdCds
         				  and cd_tipo_documento_amm in (CNRCTB100.TI_FATTURA_PASSIVA,CNRCTB100.TI_FATTURA_ATTIVA)
 						  and cd_tipo_documento_amm = nvl(aTiDoc,cd_tipo_documento_amm)
         				  and cd_unita_organizzativa=aCdUo
         				  and esercizio=aEsercizio.esercizio
         				  and pg_documento_amm=nvl(aPgDoc,pg_documento_amm)
						  and protocollo_iva is not null
  						  and fl_congelata = 'N'
         				  and stato_cofi not in ('P','A')
         				  and
         				      (
         					      ti_fattura = CNRCTB100.TI_FATT_FATTURA
         					   or cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_PASSIVA and ti_fattura = CNRCTB100.TI_FATT_NOTA_C
         					  )
         				  and esercizio_accertamento != esercizio
         				  and esercizio_accertamento is not null
         		  		  and esercizio_accertamento = aEs
     ) loop
      begin
       CNRCTB100.LOCKDOCAMM(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm);
       CNRCTB100.LOCKDOCAMMRIGA(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm,null);
  	   aNumeroProcessati:=aNumeroProcessati+1;
       CNRCTB100.UPDATEDOCAMM(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm,
           ' fl_congelata = ''Y''',
       	  null,
       	  aUser,
       	  aTsNow
       );

	   for aTrash in (select 1 from reversale_riga mr where
              cd_cds_doc_amm=aFattura.cd_cds
          and cd_uo_doc_amm=aFattura.cd_unita_organizzativa
          and cd_tipo_documento_amm = aFattura.cd_tipo_documento_amm
          and esercizio_doc_amm=aFattura.esercizio
          and pg_doc_amm=aFattura.pg_documento_amm
		  -- Reversale esitata o riscontrata in esercizio di congelamento
		  and esercizio = aEs
          and exists (select 1 from reversale where
		           cd_cds=mr.cd_cds
			   and esercizio=mr.esercizio
			   and pg_reversale=mr.pg_reversale
		       and stato in (CNRCTB038.STATO_AUT_EME, CNRCTB038.STATO_AUT_ESI)
		  )
	    for update nowait
	   ) loop
        IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a reversale emessa o riscontrata in esercizio di congelamento ('||aEs||')');
	   end loop;

	   if aFattura.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_ATTIVA then
        for aRiga in (select * from fattura_attiva_riga where
                 cd_cds=aFattura.cd_cds
             and cd_unita_organizzativa=aFattura.cd_unita_organizzativa
             and esercizio=aFattura.esercizio
             and pg_fattura_attiva=aFattura.pg_documento_amm
             -- Ciclo solo sulle righe con accertamento in esercizio di congelamento
             and esercizio_accertamento=aEs)
        loop
         for aNotaC in (select * from fattura_attiva an where
                               ti_fattura = CNRCTB100.TI_FATT_NOTA_C
                           and exists (select 1 from fattura_attiva_riga where
                                               cd_cds_accertamento = aRiga.cd_cds_accertamento
         								   and esercizio_accertamento=aRiga.esercizio_accertamento
         								   and esercizio_ori_accertamento=aRiga.esercizio_ori_accertamento
         								   and pg_accertamento = aRiga.pg_accertamento
         								   and pg_accertamento_scadenzario = aRiga.pg_accertamento_scadenzario
         						           and cd_cds = an.cd_cds
										   and esercizio = an.esercizio
										   and cd_unita_organizzativa = an.cd_unita_organizzativa
										   and pg_fattura_attiva = an.pg_fattura_attiva)
       						for update nowait)
         loop
              if aNotaC.esercizio = aEs then
               IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a nota di credito generata nell''esercizio di congelamento ('||aEs||')');
              end if;
		      begin
		       select distinct 1 into aNum from fattura_attiva_riga where
                  (
					   cd_cds_assncna_fin<>aFattura.cd_cds
                   or cd_uo_assncna_fin<>aFattura.cd_unita_organizzativa
                   or esercizio_assncna_fin<>aFattura.esercizio
                   or pg_fattura_assncna_fin<>aFattura.pg_documento_amm
			      )
                  and cd_cds=aNotaC.cd_cds
                  and cd_unita_organizzativa=aNotaC.cd_unita_organizzativa
                  and esercizio=aNotaC.esercizio
                  and pg_fattura_attiva=aNotaC.pg_fattura_attiva;
               IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a nota di credito collegata anche ad altra fattura');
			  exception when NO_DATA_FOUND then
			   null;
			  end;
               CNRCTB100.UPDATEDOCAMM(aFattura.cd_tipo_documento_amm,aNotaC.cd_cds,aNotaC.esercizio,aNotaC.cd_unita_organizzativa,aNotaC.pg_fattura_attiva,
         	  ' fl_congelata = ''Y''',
         	  null,
         	  aUser,
          		  aTsNow
               );
         end loop;
         for aNotaD in (select * from fattura_attiva an where
                               ti_fattura = CNRCTB100.TI_FATT_NOTA_D
                           and exists (select 1 from fattura_attiva_riga where
                                            cd_cds_accertamento = aRiga.cd_cds_accertamento
         								   and esercizio_accertamento=aRiga.esercizio_accertamento
         								   and esercizio_ori_accertamento=aRiga.esercizio_ori_accertamento
         								   and pg_accertamento = aRiga.pg_accertamento
         								   and pg_accertamento_scadenzario = aRiga.pg_accertamento_scadenzario
         						           and cd_cds = an.cd_cds
										   and esercizio = an.esercizio
										   and cd_unita_organizzativa = an.cd_unita_organizzativa
										   and pg_fattura_attiva = an.pg_fattura_attiva)
       						for update nowait)
         loop
              if aNotaD.esercizio = aEs then
               IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a nota di debito generata nell''esercizio di congelamento ('||aEs||')');
              end if;
		      begin
		       select distinct 1 into aNum from fattura_attiva_riga where
                  (
					   cd_cds_assncna_fin<>aFattura.cd_cds
                   or cd_uo_assncna_fin<>aFattura.cd_unita_organizzativa
                   or esercizio_assncna_fin<>aFattura.esercizio
                   or pg_fattura_assncna_fin<>aFattura.pg_documento_amm
			      )
                  and cd_cds=aNotaD.cd_cds
                  and cd_unita_organizzativa=aNotaD.cd_unita_organizzativa
                  and esercizio=aNotaD.esercizio
                  and pg_fattura_attiva=aNotaD.pg_fattura_attiva;
               IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' è collegata a nota di debito collegata anche ad altra fattura');
			  exception when NO_DATA_FOUND then
			   null;
			  end;
               CNRCTB100.UPDATEDOCAMM(aFattura.cd_tipo_documento_amm,aNotaD.cd_cds,aNotaD.esercizio,aNotaD.cd_unita_organizzativa,aNotaD.pg_fattura_attiva,
         	  ' fl_congelata = ''Y''',
         	  null,
         	  aUser,
          		  aTsNow
               );
         end loop;
        end loop;
       end if;

	   for aAccS in (select * from accertamento_scadenzario sa where
                               exists (select 1 from V_DOC_AMM_ACC where
                                       cd_cds=aFattura.cd_cds
                                   and esercizio = aFattura.esercizio
								   and cd_tipo_documento_amm = aFattura.cd_tipo_documento_amm
                                   and cd_unita_organizzativa = aFattura.cd_unita_organizzativa
                                   and pg_documento_amm = aFattura.pg_documento_amm
                                   and esercizio_accertamento > esercizio
								   and cd_cds_accertamento = sa.cd_cds
								   and esercizio_accertamento = sa.esercizio
      								   and esercizio_ori_accertamento=sa.esercizio_originale
								   and pg_accertamento = sa.pg_accertamento
								   and pg_accertamento_scadenzario = sa.pg_accertamento_scadenzario
                                   and esercizio_accertamento is not null
                                   and esercizio_accertamento = aEs
                                   and stato_cofi not in ('P','A')
                      )
         		  for update nowait
       ) loop
        CNRCTB035.AGGIORNASALDODOCAMMACC(aAccS.cd_cds, aAccS.esercizio, aAccS.esercizio_originale, aAccS.pg_accertamento, aAccS.pg_accertamento_scadenzario, 0-aAccS.im_scadenza, aUser);
        CNRCTB035.ANNULLASCADACCERTAMENTO(aAccS.cd_cds, aAccS.esercizio, aAccS.esercizio_originale, aAccS.pg_accertamento, aAccS.pg_accertamento_scadenzario, aUser);
       end loop;
       IBMUTL200.logInf(pg_exec, 'ANNULLOK',null,'CF-'||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm));
       commit;
      exception when others then
       IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK, null,'CF-'||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm));
       rollback;
      end;
 	end loop;
    end loop;
   end loop;
  end loop;
  if aNumeroProcessati=0 then
   IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK, 'Nessun documento è stato processato','Nessun documento è stato processato');
  end if;
 end;
end;
