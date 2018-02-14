CREATE OR REPLACE package CNRCTB107 as
--
-- CNRCTB107 - Congelamento fatture esercizi precedenti chiusi
-- Date: 13/07/2006
-- Version: 1.10
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 26/07/2003
-- Version: 1.0
-- Creazione
--
-- Date: 28/07/2003
-- Version: 1.1
-- Test
--
-- Date: 04/08/2003
-- Version: 1.2
-- Fix evita il reproc. delle congelate
-- Aggiunti commit
--
-- Date: 05/08/2003
-- Version: 1.3
-- Fix per gestione note di credito (fasate finanziariamente con la sezione del documento) e debito eventualmente collegate
--
-- Date: 06/08/2003
-- Version: 1.4
-- Controllo che non esista mandato emesso su fattura da congelare
-- Controllo che la fattura non transiti da fondo economale
-- Controllo che la fattura non sia associata a moduli 1210 associati a sospeso
--
-- Date: 06/08/2003
-- Version: 1.5
-- Annullamento scadenza di obbligazione e accertamento
--
-- Date: 26/08/2003
-- Version: 1.6
-- Filtrate le fatture non ancora protocollate sui registri IVA
--
-- Date: 28/08/2003
-- Version: 1.7
-- Segnalato il fatto che nessun documento ? stato processato in log
--
-- Date: 28/08/2003
-- Version: 1.8
-- Modifiche per approfondimento analisi:
--  il congelamento ? effettuato solo in esercizio > esercizio origine del documento
--  l'esercizio precedente a quello di congelamento ? chiuso
--  quello di congelamento aperto
--  vengono sganciate e "annullate" scadenze di obb/acc dell'esercizio di congelamento
--  su tali scadenze NON devono insistere NOTE di credito debito create nell'anno di congelamento
--  le scadenze da sganciare e "annullare" non devono essere collegate a mandati/reversali emessi/riscontrati
--
-- Date: 11/09/2003
-- Version: 1.9
-- Documentazione di dettaglio
--
-- Date: 13/07/2006
-- Version: 1.10
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

TIPO_LOG_JOB_CONG_FAT_ES_CH CONSTANT VARCHAR2(20):='CONG_FAT_ES_CH';

-- Functions e Procedures:

-- Congelamento massivo di fatture (attive/passive)

-- Note:

-- 1.	La funzione di congelamento massivo di fatture ? irreversibile, cio? non ? possibile ripristinare le fatture che sono state messe in stato congelato. Sar? cura dell'utente verificare l'idoneit? di tale operazione prima di invocarla.
-- 2.	Il sistema consente di congelare anche le fatture parzialmente pagate/incassate cio? le fatture con alcune righe gi? associate a mandati/reversali. In tal caso lo scollegamento delle obbligazioni/accertamenti riguarda solo la parte non pagata/incassata e analogamente il valore della scrittura coep riguarda solo questa parte. Inoltre Il sistema deve impedire l'annullamento di un mandato/reversale che paga/incassa righe di fatture congelate.


-- Pre-post name: L'esercizio specificato non deve essere quello di partenza
-- Pre: L'esercizio aEs ? quello di partenza
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: L'esercizio specificato deve essere aperto
-- Pre: L'esercizio aEs non ? aperto
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: L'esercizio precedente a quello specificato non ? chiuso
-- Pre: L'esercizio aEs-1 non ? chiuso
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: L'esercizio precedente a quello specificato non ? chiuso
-- Pre: L'esercizio aEs-1 non ? chiuso
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che non ci siano mandati emessi/esitati nell'anno di congelamento sulla fattura
-- Pre: Esiste almeno un mandato emesso o esitato nell'esercizio di congelamento per la fattura in processo
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che la fattura passiva non debba transiatare o sia transitata da fondo economale
-- Pre: La fattura in processo ? marcata come associabile o associata a fondo economale (ST_PAGAMENTO_FONDO_ECO)
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che NON ci siano lettere di pagamento con sospeso associate alla fattura passiva nell'anno di congelamento
-- Pre: Esiste almeno una lettera di pagamento estero (mod. 1210) associata alla fattura passiva nell'anno di congelamento
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che la fattura non sia collegata finanziariamente a note di credito/debito emesse nell'esercizio di congelamento
-- Pre: Esiste almeno una riga della fattura che insiste su scadenza di accert./obbligaz. su cui insiste una nota di credito o debito emessa
--      nell'esercizio di congelamento
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che la fattura non sia collegata finanziariamente a note di credito/debito collegate anche ad altre fatture
-- Pre: Esiste almeno una riga della fattura che insiste su scadenza di accert./obbligaz. su cui insiste una nota di credito o debito emessa
--      che insiste finanziariamente anche su righe di altre fatture
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Congelamento di fatture attive/passive
-- Pre: Nessuna delle precondizioni ? verificata
-- Post:
--  Esecuzione del congelamento
--	 Per ogni fattura congelabile il sistema:
--    a. imposta un flag di 'congelato' sul documento in modo da impedire all'utente di effettuare modifiche
--       al documento stesso tramite l'applicazione on-line: il 'congelamento' della fattura viene fatto a livello
--       di testata della fattura cio? tutte le righe della fattura risultano congelate
--	  b. scollega tutte le scadenze delle obbligazioni/accertamenti su cui erano state contabilizzate le righe delle
--       fatture. Imposta al valore 0 l'importo di tutte le scadenze delle obbligazioni/accertamenti (e dei relativi
--       dettagli di scadenza) su cui erano state contabilizzate le righe delle fatture e aggiorna l'importo di testata
--       delle obbligazioni/accertamenti se anche l'importo di testata dell'obbligazione/accertamento risulta essere 0
--       il sistema procede all'annullamento dell'intera obbligazione. Quindi decrementa i saldi dei capitoli finanziari
--    c. se la fattura ? contabilizzata su scadenze di obbligazioni sulle quali sono state anche contabilizzate note di credito
--       il sistema procede al congelamento anche di queste ultime

-- Parametri
--   job,pg_exec,next_date -> parametri impliciti per gestione via batch
--   aTiDoc -> Tipo di documento amministrativo (FATTURA_A o FATTURA_P)
--   aCdCds -> Centro di spesa (null -> tutti i cds)
--   aEs -> Esercizio in cui avviene il congelamento della fattura
--   aCdUo -> Unit? organizzativa documento amministrativo (null -> tutte le UO)
--   aPgDoc -> Progressivo documento amministrativo (null -> tutti i documenti)

 procedure job_congelaFatturaEsChiuso(job number, pg_exec number, next_date date, aTiDoc varchar2, aCdCds varchar2, aEs number, aCdUo varchar2, aPgDoc number);

end;


CREATE OR REPLACE package body CNRCTB107 is

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
   IBMERR001.RAISE_ERR_GENERICO('Non ? possibile effetuare il congelamento di fatture nell''esercizio di partenza dell''applicazione');
  end if;

  if not CNRCTB008.ISESERCIZIOAPERTO(aEs,aCdCds) then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non ? aperto per il cds:'||aCdCds);
  end if;

  if not CNRCTB008.ISESERCIZIOCHIUSO(aEs-1,aCdCds)  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||(aEs-1)||' non ? chiuso per il cds:'||aCdCds);
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
     IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non ? chiuso per il cds:'||aCdCds);
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
        IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a mandato emesso o esitato nell''anno di congelamento ('||aEs||')');
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
         IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a modulo 1210 associato a sospeso nell''anno di congelamento ('||aEs||')');
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
            IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a nota di credito generata nell''esercizio di congelamento ('||aEs||')');
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
                  IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a nota di credito collegata anche ad altra fattura');
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
            IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a nota di debito generata nell''esercizio di congelamento ('||aEs||')');
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
                  IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a nota di debito collegata anche ad altra fattura');
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
        IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a reversale emessa o riscontrata in esercizio di congelamento ('||aEs||')');
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
               IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a nota di credito generata nell''esercizio di congelamento ('||aEs||')');
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
               IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a nota di credito collegata anche ad altra fattura');
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
               IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a nota di debito generata nell''esercizio di congelamento ('||aEs||')');
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
               IBMERR001.RAISE_ERR_GENERICO('La fattura '||getDesc(aFattura.cd_tipo_documento_amm,aFattura.cd_cds,aFattura.esercizio,aFattura.cd_unita_organizzativa,aFattura.pg_documento_amm)||' ? collegata a nota di debito collegata anche ad altra fattura');
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
   IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK, 'Nessun documento ? stato processato','Nessun documento ? stato processato');
  end if;
 end;
end;


