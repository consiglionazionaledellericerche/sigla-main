CREATE OR REPLACE package CNRCTB220 as
--
-- CNRCTB220 - Package controllo batch COEP
-- Date: 17/11/2004
-- Version: 3.18
--
-- Package per il controllo dei batch la gestione delle scritture COEP
--
-- Dependency: IBMUTL 200/210 CNRCTB 018/100/204/205/210
--
-- History:
--
-- Date: 08/05/2002
-- Version: 1.8
-- Creazione
--
-- Date: 16/05/2002
-- Version: 1.9
-- Riorganizzazione
--
-- Date: 29/05/2002
-- Version: 2.0
-- Aggiunta la contabilizzazione ultimi documenti
--
-- CNRCTB220 - Package controllo batch COEP
-- Date: 30/05/2002
-- Fix su recupero tipo di documento
--
-- Date: 31/05/2002
-- Version: 2.2
-- Portato lock e update doc amm e cont coge in package 205
--
-- Date: 06/07/2002
-- Version: 2.3
-- Aggiunta la parte relativa alla dismissione del bene durevole
--
-- Date: 18/07/2002
-- Version: 2.4
-- Aggiornamento documentazione
--
-- Date: 10/10/2002
-- Version: 2.5
-- Eliminato il commit per persistenza del log perch? implicito in log package con pragma autonomous transaction
--
-- Date: 18/10/2002
-- Version: 2.6
-- Sistemazione invocazioni e dump log
--
-- Date: 18/11/2002
-- Version: 2.7
-- Riorganizzazione package CNRCTB205
--
-- Date: 23/12/2002
-- Version: 2.8
-- Sistemata contabilizzazione ultimi documenti (solo per documenti esitati!)
--
-- Date: 28/01/2003
-- Version: 2.9
-- Introduzione del tipo di LOG in logStartExecutionUpdate
--
-- Date: 23/05/2003
-- Version: 3.0
-- Spaccato il loop per esercizio in loop per UO per doc amm e loop per cds per doc cont
--
-- Date: 11/06/2003
-- Version: 3.1
-- Aggiunto metodo che processa COEP per CDS (* = tutti)
--
-- Date: 18/06/2003
-- Version: 3.2
-- Fix logging
--
-- Date: 19/06/2003
-- Version: 3.3
-- Nuova versione dell'estrazione per CDS di origine del documento
-- Nuova gestione della dismissione del bene durevole
--
-- Date: 09/07/2003
-- Version: 3.4
-- Modificata interfaccia dei metodi di processo dei documenti per aggiunta timestamp operazione
--
-- Date: 05/08/2003
-- Version: 3.5
-- Aggiunto indicatore di terminazione del batch
--
-- Date: 05/08/2003
-- Version: 3.6
-- Loop UO spaccato per CDS
-- Introduzione RBSBIG
-- Sistemazione errore di locking errato su dismissione beni durevoli
--
-- Date: 06/08/2003
-- Version: 3.7
-- introduzione metodi commitRbsBig/rollbackRbsBig
--
-- Date: 11/11/2003
-- Version: 3.8
-- Controllo su chiusura definitiva esercizio economico precedente a quello in processo
--
-- Date: 05/03/2004
-- Version: 3.9
-- Sistemazione dell'interfaccia di chiamata della contabilit? coan
--
-- Date: 20/05/2004
-- Version: 3.10
-- Aggiunto caricamento dei beni durevoli (richiesta 693)
--
-- Date: 08/06/2004
-- Version: 3.11
-- Test richiesta 693
--
-- Date: 24/09/2004
-- Version: 3.12
-- Richiets 843: I controlli sull'esercizio (finanziario ed economico) del documento in processo sono cambiati
-- La parte di controlli riguardante l'esercizio del documento (che ? quello di registrazione economica) sono
-- stati spostati nel metodo: CNRCTB204.checkChiusuraEsercizio
--
-- Date: 25/09/2004
-- Version: 3.13
-- Sistemato problema di cursore causato da checkChiusuraEsercizio
--
-- Date: 05/11/2004
-- Version: 3.14
-- Aggiunto job annullamento documenti provenienti da esercizio precedenti chiusi economicamente
--
-- Date: 10/11/2004
-- Version: 3.15
-- Fix a job_registraAnnCogeEsChiuso per aggiunta rollback per non invalidare cursore
--
-- Date: 10/11/2004
-- Version: 3.16
-- Sistemato il logging del job di annullamento doc esercizio chiuso
--
-- Date: 12/11/2004
-- Version: 3.17
-- Inserimento, nel ciclo generali del job di registrazione COGE, dell'economica da migrazione inventario
--
-- Date: 17/11/2004
-- Version: 3.18
-- Workaround per fix errore di cursore non valido (fetch out of sequence di Oracle)
--
-- Constants:

 TIPO_LOG_JOB_COGE CONSTANT VARCHAR2(20) := 'REG_COGE00';

-- Functions e Procedures

-- Job di registrazione COGE/COAN
--
-- Controllo dei batch per la registrazione della contabilit? economica ed analitica.
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente a quello in processo non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello in processo non ? chiuso definitivamente per il CDS in processo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Documento amministrativo non contabilizzato in COGE oppure documento da contabilizzare nuovamente in COGE (storno e nuova contabilizzazione) (contabilit? economica)
-- pre: Documento amministrativo non contabilizzato in COGE oppure documento da contabilizzare nuovamente in COGE (storno e nuova contabilizzazione) (dal punto di vista della contabilit? economica)
-- post: Scrittura in partita doppia in contabilit? economica del documento amministrativo
--
-- pre-post-name: Dismissione di un bene durevole senza riferimenti esterni
-- pre: esiste almeno un inventario relativo all'esercizio
-- post: registra in COEP la dismissione di un bene durevole
--
-- pre-post-name: Caricamento di un bene durevole senza riferimenti esterni
-- pre: esiste almeno un inventario relativo all'esercizio
-- post: registra in COEP il caricamento di un bene durevole
--
-- pre-post-name: Documento amministrativo non contabilizzato in COGE oppure documento da contabilizzare nuovamente in COGE (storno e nuova contabilizzazione) (contabilit? analitica)
-- pre: Documento amministrativo non contabilizzato in COGE oppure documento da contabilizzare nuovamente in COGE (storno e nuova contabilizzazione) (dal punto di vista della contabilit? analitica)
-- post: aggiornamento dello stato del documento in contabilit? analitica
--

-- Parametri:
-- job -> Numero identificativo del job
-- pg_exec -> Progressivo di esecuzione
-- next_date -> Periodo di esecuzione
-- aEs -> Esercizio
-- aCdCds -> Cds da processare * = tutti cds

 procedure job_registraCogeCoan(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2);

-- Job di registrazione COGE annullamento documenti di esercizi precedenti chiusi economiicamente
-- definitivamente
--
-- Controllo dei batch per la registrazione della contabilit? economica ed analitica.
--
-- pre-post-name: Cds origine del documento non pi? valido nell'esercizio di registrazione economica dell'annullamento
-- pre: Il cds origine del documento non ? pi? valido nell'esercizio di registrazione economica dell'annullamento
-- post: Viene sollevata un'eccezione
-- pre-post-name: Esercizio in cui si annulla il documento non ancora aperto o chiuso finanziariamente
-- pre: L'esercizio in cui si annulla il documento non ancora aperto per il cds specificato
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente a quello in processo non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello in processo non ? chiuso definitivamente per il CDS in processo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Documento amministrativo o bene migrato non contabilizzato in COGE oppure
--                documento da contabilizzare nuovamente in COGE (storno e nuova contabilizzazione) (contabilit? economica)
-- pre: Documento amministrativo o bene migrato non contabilizzato in COGE oppure documento o bene da contabilizzare nuovamente in COGE (storno e nuova contabilizzazione) (dal punto di vista della contabilit? economica)
-- post: Scrittura in partita doppia in contabilit? economica del documento amministrativo o bene migrato
--
-- Parametri:
-- job -> Numero identificativo del job
-- pg_exec -> Progressivo di esecuzione
-- next_date -> Periodo di esecuzione
-- aEsAnn -> Esercizio di annullamento
-- aCdCds -> Cds da processare * = tutti cds

 procedure job_registraAnnCogeEsChiuso(job number, pg_exec number, next_date date, aEsAnn number, aCdCds varchar2);

end;
/


CREATE OR REPLACE package body CNRCTB220 is

 function buildCallString(aBS buono_carico_scarico_dett%rowtype, aUser varchar2) return varchar2 is
  aStatement varchar2(2000);
 begin
  if aBS.ti_documento = 'S' then
   aStatement:='-- DISMISSIONE BENE DUREVOLE';
  else
   aStatement:='-- CARICO BENE DUREVOLE';
  end if;
  aStatement:=   aStatement||'
  declare
     aBS buono_carico_scarico_dett%rowtype;
  begin ';
     aStatement:=aStatement||'
     select * into aBS from buono_carico_scarico_dett where
	        esercizio = '||aBS.esercizio||'
		and pg_inventario = '||aBS.pg_inventario||'
		and ti_documento = '''||aBS.ti_documento||'''
		and pg_buono_c_s = '||aBS.pg_buono_c_s||'
		and nr_inventario = '||aBS.nr_inventario||'
		and progressivo = '||aBS.progressivo||'
		for update nowait;';

  if aBS.ti_documento = 'S' then
   aStatement:=aStatement||'      CNRCTB205.regDismBeneDurevoleCOGE(aBS, '''||aUser||''',sysdate);';
  else
   aStatement:=aStatement||'      CNRCTB205.regCaricBeneDurevoleCOGE(aBS, '''||aUser||''',sysdate);';
  end if;
  aStatement:=   aStatement||'
  end;';
  return aStatement;
 end;

 function buildCallString
    (aCdCds VARCHAR2,
     aCdUo VARCHAR2,
     aEsercizio NUMBER,
     aCdCatGrp VARCHAR2,
     aUser VARCHAR2
    ) RETURN VARCHAR2 IS
  aStatement varchar2(2000);
  aCnrInventBeniMig CNR_INVENTARIO_BENI_MIG%ROWTYPE;
 begin
  aStatement:='-- MIGRAZIONE BENE DUREVOLE';
  aStatement:=aStatement ||
     'declare
       aCnrInventBeniMig CNR_INVENTARIO_BENI_MIG%ROWTYPE;
      begin ';
  aStatement:=aStatement ||
              'SELECT cd_unita_organizzativa, ' ||
                     'cd_categoria_gruppo, ' ||
	             'SUM(valore_iniziale) valore_iniziale, ' ||
	             'SUM(valore_ammortizzato) valore_ammortizzato, ' ||
                     'stato_coge ' ||
              'INTO   aCnrInventBeniMig.cir_cd_uo, ' ||
                     'aCnrInventBeniMig.cir_cd_categoria_gruppo, ' ||
                     'aCnrInventBeniMig.cir_valore_iniziale, ' ||
                     'aCnrInventBeniMig.cir_valore_ammortizzato, ' ||
                     'aCnrInventBeniMig.stato ' ||
              'FROM   INVENTARIO_BENI INV,BUONO_CARICO_SCARICO_DETT DETT ' ||
              'WHERE  cd_cds = ' || '''' || aCdCds || '''' || ' AND ' ||
               	     'inv.pg_inventario = dett.pg_inventario and '||
                     'inv.nr_inventario = dett.nr_inventario and '||
                     'inv.progressivo   = dett.progressivo   and '||
                     'inv.fl_migrato = '||''''||'Y'||''''||' and '||
                     'dett.ti_documento = '||''''||'C'||''''||' and '||
                     'inv.dacr       like dett.dacr          and '||
                     'cd_unita_organizzativa = ' || '''' || aCdUo || '''' || ' AND ' ||
                     'esercizio_carico_bene = ' || aEsercizio || ' AND ' ||
                     'cd_categoria_gruppo = ' || '''' || aCdCatGrp || '''' || ' AND ' ||
                     'stato_coge IN (' || '''' || CNRCTB100.STATO_COEP_INI || '''' || ',' ||
                                             '''' || CNRCTB100.STATO_COEP_DA_RIP || '''' || ') ' ||
              'GROUP BY cd_unita_organizzativa, cd_categoria_gruppo, stato_coge ; ';

  aStatement:=aStatement ||
              '  CNRCTB205.regMigrazioneBeniCoge(' || aEsercizio || ',' ||
                                                 '''' || aCdCds || '''' || ',' ||
                                                 '''' || aCdUo || '''' || ',' ||
                                                 '''' || aCnrInventBeniMig.cir_cd_categoria_gruppo || '''' || ',' ||
                                                 aCnrInventBeniMig.cir_valore_iniziale || ',' ||
                                                 aCnrInventBeniMig.cir_valore_ammortizzato || ',' ||
                                                 '''' || aUser || '''' || ',' || sysdate ||','|| '''' || aCnrInventBeniMig.stato || '''' ||  '); ';
  aStatement:=aStatement || 'end;';

  return aStatement;
 end;


 function buildCallString(aTsta V_DOC_ULT_COGE_TSTA%rowtype, aUser varchar2) return varchar2 is
  aStatement varchar2(2000);
 begin
  aStatement:='-- COEP ULTIMI TEMPLATE
    declare
     aTsta V_DOC_ULT_COGE_TSTA%rowtype;
	begin ';
  aStatement:=aStatement||'
     select * into aTsta from V_DOC_ULT_COGE_TSTA where
	        esercizio = '||aTsta.esercizio||'
		and cd_tipo_documento_cont = '''||aTsta.cd_tipo_documento_cont||'''
		and cd_cds = '''||aTsta.cd_cds||'''
		and pg_documento_cont = '||aTsta.pg_documento_cont||';
     CNRCTB205.regDocPagCoge(aTsta, '''||aUser||''',sysdate);
    end;';
  return aStatement;
 end;

 function buildCallStringCoan(aTsta V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2) return varchar2 is
  aStatement varchar2(2000);
 begin
  aStatement:='-- COAN TEMPLATE
	begin ';
     aStatement:=aStatement||'
     CNRCTB210.regDocAmmCoan('||aTsta.ESERCIZIO||',
  	  							   '''||aTsta.CD_CDS||''',
  								   '''||aTsta.CD_UNITA_ORGANIZZATIVA||''',
  								   '''||aTsta.CD_TIPO_DOCUMENTO||''',
  								   '||aTsta.PG_NUMERO_DOCUMENTO||',
								   '''||aUser||''',sysdate);
   end;';
  return aStatement;
 end;

 function buildCallStringCoep(aTsta V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2) return varchar2 is
  aStatement varchar2(2000);
 begin
  aStatement:='-- COEP PRIMI TEMPLATE
    declare
     aTsta V_DOC_AMM_COGE_TSTA%rowtype;
	begin ';
  aStatement:=aStatement||'
     select * into aTsta from V_DOC_AMM_COGE_TSTA where
	        esercizio = '||aTsta.esercizio||'
		and cd_tipo_documento = '''||aTsta.cd_tipo_documento||'''
		and cd_cds = '''||aTsta.cd_cds||'''
		and cd_unita_organizzativa = '''||aTsta.cd_unita_organizzativa||'''
		and pg_numero_documento = '||aTsta.pg_numero_documento||';
     CNRCTB205.regDocAmmCoge(aTsta, '''||aUser||''',sysdate);
    end;';
  return aStatement;
 end;

 function buildCallStringAnnEsChiuso(aEsAnn number, aTsta V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2) return varchar2 is
  aStatement varchar2(2000);
 begin
  aStatement:='-- COEP PRIMI TEMPLATE
    declare
     aTsta V_DOC_AMM_COGE_TSTA%rowtype;
	 aEsAnn number(4):='||aEsAnn||';
	begin ';
  aStatement:=aStatement||'
     select * into aTsta from V_DOC_AMM_COGE_TSTA where
	        esercizio = '||aTsta.esercizio||'
		and cd_tipo_documento = '''||aTsta.cd_tipo_documento||'''
		and cd_cds = '''||aTsta.cd_cds||'''
		and cd_unita_organizzativa = '''||aTsta.cd_unita_organizzativa||'''
		and pg_numero_documento = '||aTsta.pg_numero_documento||';
     CNRCTB205.REGANNULLADOCESCHIUSOCOGE(aEsAnn,aTsta, '''||aUser||''', sysdate);
    end;';
  return aStatement;
 end;

 function buildCallStringRigheAnnEsChiu(aEsAnn number, aRigheAnn V_DOC_AMM_COGE_RIGHE_ANNULLATE%rowtype, aUser varchar2) return varchar2 is
  aStatement varchar2(2000);
 begin
  aStatement:='-- COEP PRIMI TEMPLATE
    declare
       aRigheAnn V_DOC_AMM_COGE_RIGHE_ANNULLATE%rowtype;
       aEsAnn number(4):='||aEsAnn||';
    begin ';
     aStatement:=aStatement||'
     select * into aRigheAnn from V_DOC_AMM_COGE_RIGHE_ANNULLATE where
	        esercizio = '||aRigheAnn.esercizio||'
		and cd_tipo_documento = '''||aRigheAnn.cd_tipo_documento||'''
		and cd_cds = '''||aRigheAnn.cd_cds||'''
		and cd_unita_organizzativa = '''||aRigheAnn.cd_unita_organizzativa||'''
		and pg_numero_documento = '||aRigheAnn.pg_numero_documento||';
     CNRCTB205.REGANNULLARIGHEDOCESCHIUSOCOGE(aEsAnn,aRigheAnn, '''||aUser||''', sysdate);
    end;';
  return aStatement;
 end;




 -- Solleva eccezione se la chiusura economica ? in corso
 procedure checkChiusuraEconomicaInCorso(aEs number, aCdCds varchar2) is
 begin
	if(CNRCTB800.isSemStaticoCdsBloccato(aEs, aCdCds, CNRCTB200.SEMAFORO_CHIUSURA)) then
        IBMERR001.RAISE_ERR_GENERICO('Operazioni di chiusura economica in corso su cds:'||aCdCds||' es.:'||aEs);
	end if;
 end;

 procedure job_registraCogeCoan(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2) is
  aStringa varchar2(4000);
  aTSNow date;
  aUser varchar2(20);
  aChekLock CHAR(1);
  aChekUpdate CHAR(1);
  aMan mandato%rowtype;
  aRev reversale%rowtype;
  aDocCoan V_COAN_DOCUMENTI%rowtype;
 begin
  IBMUTL015.setRbsBig;
  aTSNow:=sysdate;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);

  -- Aggiorna le info di testata del log
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_JOB_COGE, job, 'Batch di registrazione economica. CDS: '||nvl(aCdCds,'UNDEF')||' Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));

  if aCdCds is null then
   IBMERR001.RAISE_ERR_GENERICO('CDS non specificato');
  end if;

  for aCDS in (select cd_unita_organizzativa from v_unita_organizzativa_valida where
       esercizio = aEs
   and cd_unita_organizzativa = decode(aCdCds,'*',cd_unita_organizzativa,aCdCds)
   and fl_cds = 'Y'
   order by cd_unita_organizzativa
  ) loop
   -- Fix del 20040924 Richiesta 843
   CNRCTB204.checkChiusuraEsercizio(aEs, aCDS.cd_unita_organizzativa);
   IBMUTL015.rollbackRbsBig;

-- 06/06/2005 eliminato per consentire i RATEI (tra l'altro gi? previsti)

--   if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aCDS.cd_unita_organizzativa)='Y') then
--    IBMUTL015.rollbackRbsBig;
--    IBMUTL200.logErr(pg_exec, null,'Esercizio economico precedente non chiuso definitivamente','XX-'||aCDS.cd_unita_organizzativa);
--   else

    IBMUTL015.rollbackRbsBig;
    for aUO in (select cd_unita_organizzativa from v_unita_organizzativa_valida where
         esercizio = aEs
     and cd_unita_padre = aCDS.cd_unita_organizzativa
     and fl_cds = 'N'
     order by cd_unita_organizzativa
    ) loop
     for aTsta in (select * from V_DOC_AMM_COGE_TSTA where
           esercizio = aEs
   	   and cd_cds_origine = aCDS.cd_unita_organizzativa
	   and cd_uo_origine = aUO.cd_unita_organizzativa
       and
	  (
         stato_coge in (CNRCTB100.STATO_COEP_INI, CNRCTB100.STATO_COEP_DA_RIP )
       or stato_coan in (CNRCTB100.STATO_COEP_INI, CNRCTB100.STATO_COEP_DA_RIP )
	  )
     ) loop
      BEGIN
       -- Scrittura dei movimenti COGE
       if aTsta.stato_coge in (CNRCTB100.STATO_COEP_INI, CNRCTB100.STATO_COEP_DA_RIP ) then
   	    CNRCTB205.regDocAmmCoge(aTsta, aUser, sysdate);
        checkChiusuraEconomicaInCorso(aTsta.esercizio, aTsta.cd_cds_origine);
	    IBMUTL015.commitRbsBig;
        IBMUTL200.logInf(pg_exec, 'COPEPOK',buildCallStringCoep(aTsta,aUser),'EP-'||aUO.cd_unita_organizzativa||CNRCTB204.getDescDocumento(aTsta));
  	   end if;
      EXCEPTION WHEN OTHERS THEN
       IBMUTL015.rollbackRbsBig;
  	   IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,buildCallStringCoep(aTsta,aUser),'EP-'||aUO.cd_unita_organizzativa||CNRCTB204.getDescDocumento(aTsta));
      END;

      BEGIN
       if aTsta.stato_coan in (CNRCTB100.STATO_COEP_INI, CNRCTB100.STATO_COEP_DA_RIP ) then
  	    CNRCTB210.regDocAmmCoan(aTsta.ESERCIZIO, aTsta.CD_CDS, aTsta.CD_UNITA_ORGANIZZATIVA, aTsta.CD_TIPO_DOCUMENTO, aTsta.PG_NUMERO_DOCUMENTO, aUser, sysdate);
        checkChiusuraEconomicaInCorso(aTsta.esercizio, aTsta.cd_cds_origine);
        IBMUTL015.commitRbsBig;
        IBMUTL200.logInf(pg_exec, 'COANOK',buildCallStringCoan(aTsta,aUser),'AN-'||aUO.cd_unita_organizzativa||CNRCTB204.getDescDocumento(aTsta));
	   end if;
	  EXCEPTION WHEN OTHERS THEN
       IBMUTL015.rollbackRbsBig;
  	   IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,buildCallStringCoan(aTsta,aUser),'AN-'||aUO.cd_unita_organizzativa||CNRCTB204.getDescDocumento(aTsta));
      END;
     END LOOP; -- Fine loop su docs UO
    END LOOP; -- Fine loop su UO
--   end if;
  END LOOP; -- Fine loop su CDS

  -- Migrazione inventario beni

  for aCDS in (select * from v_unita_organizzativa_valida where
       esercizio = aEs
   and cd_unita_organizzativa = decode(aCdCds,'*',cd_unita_organizzativa,aCdCds)
   and fl_cds = 'Y'
   order by cd_unita_organizzativa
  ) loop
   -- Fix del 20040924 Richiesta 843
   CNRCTB204.checkChiusuraEsercizio(aEs, aCDS.cd_unita_organizzativa);
   IBMUTL015.rollbackRbsBig;
   if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aCDS.cd_unita_organizzativa)='Y') then
    IBMUTL015.rollbackRbsBig;
    IBMUTL200.logErr(pg_exec, null,'Esercizio economico precedente non chiuso definitivamente','XX-'||aCDS.cd_unita_organizzativa);
   else
    IBMUTL015.rollbackRbsBig;


     for aBeneMig in (SELECT cd_unita_organizzativa,
                            cd_categoria_gruppo,
                            sum(valore_iniziale) valore_iniziale,
                            sum(valore_ammortizzato) valore_ammortizzato,
                            stato_coge
                     FROM   INVENTARIO_BENI inv,buono_carico_Scarico_Dett dett
                     WHERE  cd_cds 		  = aCDS.cd_unita_organizzativa AND
                            esercizio_carico_bene = aEs And
                            fl_migrato 		  = 'Y'  and
                            inv.pg_inventario 	  = dett.pg_inventario and
                            inv.nr_inventario 	  = dett.nr_inventario and
                            inv.progressivo   	  = dett.progressivo   and
                            inv.dacr             like dett.dacr        and
                            Stato_coge in(CNRCTB100.STATO_COEP_DA_RIP,CNRCTB100.STATO_COEP_INI)
                     GROUP BY cd_unita_organizzativa, cd_categoria_gruppo,stato_coge
                     ORDER BY cd_unita_organizzativa, cd_categoria_gruppo,stato_coge
    ) Loop
     BEGIN
      CNRCTB205.regMigrazioneBeniCoge(aEs,
                                      aCDS.cd_unita_organizzativa,
                                      aBeneMig.cd_unita_organizzativa,
                                      aBeneMig.cd_categoria_gruppo,
                                      aBeneMig.valore_iniziale,
                                      aBeneMig.valore_ammortizzato,
                                      aUser,
                                      Sysdate,
                                      aBeneMig.stato_COGE);
      checkChiusuraEconomicaInCorso(aEs, aCDS.cd_unita_organizzativa);
      IBMUTL015.commitRbsBig;
      IBMUTL200.logInf(pg_exec,
                       'MIGBDOK',
                       buildCallString(aCDS.cd_unita_organizzativa, aBeneMig.cd_unita_organizzativa, aEs,
                                       aBeneMig.cd_categoria_gruppo, aUser),
                       'DS-' || aCDS.cd_unita_organizzativa || 'MB-Migrazione Beni:' || aEs || '-' || aBeneMig.cd_unita_organizzativa || '-' ||aBeneMig.cd_categoria_gruppo);
     EXCEPTION WHEN OTHERS THEN
      IBMUTL015.rollbackRbsBig;
 	  IBMUTL200.logErr(pg_exec,
                           DBMS_UTILITY.FORMAT_ERROR_STACK,
                           buildCallString(aCDS.cd_unita_organizzativa, aBeneMig.cd_unita_organizzativa, aEs,
                                           aBeneMig.cd_categoria_gruppo, aUser),
                           'MB-Migrazione Beni:' || aEs || '-' || aBeneMig.cd_unita_organizzativa || '-' ||aBeneMig.cd_categoria_gruppo);
     END;
    end loop;
   end if;
  end loop; -- End loop CDS

  -- Dismissioni di beni durevoli
  for aCDS in (select * from v_unita_organizzativa_valida where
       esercizio = aEs
   and cd_unita_organizzativa = decode(aCdCds,'*',cd_unita_organizzativa,aCdCds)
   and fl_cds = 'Y'
   order by cd_unita_organizzativa
  ) loop
   -- Fix del 20040924 Richiesta 843
   CNRCTB204.checkChiusuraEsercizio(aEs, aCDS.cd_unita_organizzativa);
   IBMUTL015.rollbackRbsBig;
   if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aCDS.cd_unita_organizzativa)='Y') then
    IBMUTL015.rollbackRbsBig;
    IBMUTL200.logErr(pg_exec, null,'Esercizio economico precedente non chiuso definitivamente','XX-'||aCDS.cd_unita_organizzativa);
   else
    IBMUTL015.rollbackRbsBig;
    for aInv in (select distinct pg_inventario from inventario_beni where cd_cds = aCDS.cd_unita_organizzativa) loop
     for aBS in (select * from buono_carico_scarico_dett bcs where
                     esercizio = aEs
				 and pg_inventario = aInv.pg_inventario
				 and ti_documento = 'S'
				 and (stato_coge in (CNRCTB100.STATO_COEP_INI,CNRCTB100.STATO_COEP_DA_RIP) Or
				      stato_coge_quote in (CNRCTB100.STATO_COEP_INI,CNRCTB100.STATO_COEP_DA_RIP))
     ) loop
      BEGIN
       CNRCTB205.regDismBeneDurevoleCOGE(aBS, aUser, sysdate);
       checkChiusuraEconomicaInCorso(aBS.esercizio, aCDS.cd_unita_organizzativa);
       IBMUTL015.commitRbsBig;
       IBMUTL200.logInf(pg_exec,'DISBDOK',buildCallString(aBS,aUser),'DS-'||aCDS.cd_unita_organizzativa||'DB-Dismissioni per inventario:'||CNRCTB204.getDescDocumento(aBS));
      EXCEPTION WHEN OTHERS THEN
       IBMUTL015.rollbackRbsBig;
  	   IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,buildCallString(aBS,aUser),'DB-Dismissioni per inventario:'||CNRCTB204.getDescDocumento(aBS));
      END;
     end loop; -- End loop bs_scarico_dett
    end loop; -- End loop inventario
   end if;
  end loop; -- End loop CDS

  -- Carico di beni durevoli
  for aCDS in (select * from v_unita_organizzativa_valida where
       esercizio = aEs
   and cd_unita_organizzativa = decode(aCdCds,'*',cd_unita_organizzativa,aCdCds)
   and fl_cds = 'Y'
   order by cd_unita_organizzativa
  ) loop
   -- Fix del 20040924 Richiesta 843
   CNRCTB204.checkChiusuraEsercizio(aEs, aCDS.cd_unita_organizzativa);
   IBMUTL015.rollbackRbsBig;
   if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aCDS.cd_unita_organizzativa)='Y') then
    IBMUTL015.rollbackRbsBig;
    IBMUTL200.logErr(pg_exec, null,'Esercizio economico precedente non chiuso definitivamente','XX-'||aCDS.cd_unita_organizzativa);
   else
    IBMUTL015.rollbackRbsBig;
    for aInv in (select distinct pg_inventario from inventario_beni where cd_cds = aCDS.cd_unita_organizzativa) loop
     for aBS in (select * from buono_carico_scarico_dett bcs where
                     esercizio = aEs
				 and pg_inventario = aInv.pg_inventario
				 and ti_documento = 'C'
				 and stato_coge in (CNRCTB100.STATO_COEP_INI,CNRCTB100.STATO_COEP_DA_RIP)
     ) loop
      BEGIN
       CNRCTB205.REGCARICBENEDUREVOLECOGE(aBS, aUser, sysdate);
       checkChiusuraEconomicaInCorso(aBS.esercizio, aCDS.cd_unita_organizzativa);
       IBMUTL015.commitRbsBig;
       IBMUTL200.logInf(pg_exec,'CARBDOK',buildCallString(aBS,aUser),'DS-'||aCDS.cd_unita_organizzativa||'CB-Carico in inventario:'||CNRCTB204.getDescDocumento(aBS));
      EXCEPTION WHEN OTHERS THEN
       IBMUTL015.rollbackRbsBig;
  	   IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,buildCallString(aBS,aUser),'CB-Carico in inventario:'||CNRCTB204.getDescDocumento(aBS));
      END;
     end loop; -- End loop bs_carico_dett
    end loop; -- End loop inventario
   end if;
  end loop; -- End loop CDS


  -- Scritture ULTIME e SINGOLE (eseguite solo su documenti esitati o annullati)
  --  IBMUTL210.logStartExecutionUpd(pg_exec, job, 'Batch di registrazione economica (ultimi documenti). Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));
  for aCDS in (select cd_unita_organizzativa from v_unita_organizzativa_valida where
       esercizio = aEs
   and cd_unita_organizzativa = decode(aCdCds,'*',cd_unita_organizzativa,aCdCds)
   and fl_cds = 'Y'
   order by cd_unita_organizzativa
  ) loop
   -- Fix del 20040924 Richiesta 843
   CNRCTB204.checkChiusuraEsercizio(aEs, aCDS.cd_unita_organizzativa);
   IBMUTL015.rollbackRbsBig;
   if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aCDS.cd_unita_organizzativa)='Y') then
    IBMUTL015.rollbackRbsBig;
    IBMUTL200.logErr(pg_exec, null,'Esercizio economico precedente non chiuso definitivamente','XX-'||aCDS.cd_unita_organizzativa);
   else
    IBMUTL015.rollbackRbsBig;
    for aTstaUlt in (select * from V_DOC_ULT_COGE_TSTA where
         esercizio = aEs
     and cd_cds_origine = aCDS.cd_unita_organizzativa
     and stato in (CNRCTB038.STATO_AUT_ESI,CNRCTB038.STATO_AUT_ANN)
     and stato_coge in (CNRCTB100.STATO_COEP_INI, CNRCTB100.STATO_COEP_DA_RIP)
	 and cd_tipo_documento_cont in (CNRCTB018.TI_DOC_MAN,CNRCTB018.TI_DOC_REV)
    ) loop
     BEGIN
   	  CNRCTB205.regDocPagCoge(aTstaUlt, aUser, sysdate);
      checkChiusuraEconomicaInCorso(aTstaUlt.esercizio, aTstaUlt.cd_cds_origine);
      IBMUTL015.commitRbsBig;
      IBMUTL200.logInf(pg_exec, 'COEPUOK',buildCallString(aTstaUlt,aUser),'EU-'||aCDS.cd_unita_organizzativa||'.000'||CNRCTB204.getDescDocumento(aTstaUlt));
     EXCEPTION WHEN OTHERS THEN
      IBMUTL015.rollbackRbsBig;
  	  IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,buildCallString(aTstaUlt,aUser),'EU-'||aCDS.cd_unita_organizzativa||'.000'||CNRCTB204.getDescDocumento(aTstaUlt));
     END;
    END LOOP; -- fine loop su documenti
   end if;
  END LOOP; -- fine loop su CDS
  IBMUTL200.logInf(pg_exec,'FINE','Termine esecuzione','Termine esecuzione');
 END;

Procedure job_registraAnnCogeEsChiuso(job number, pg_exec number, next_date date, aEsAnn number, aCdCds varchar2) is
  aStringa      VARCHAR2(4000);
  aTSNow        DATE;
  aUser         VARCHAR2(20);
  aChekLock     CHAR(1);
  aChekUpdate   CHAR(1);
  aEs           NUMBER(4);
  aNum          NUMBER;
  aTestata      v_doc_amm_coge_tsta%Rowtype;

Begin
  IBMUTL015.setRbsBig;
  aTSNow:=sysdate;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);

  -- Aggiorna le info di testata del log
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_JOB_COGE, job, 'Batch di registrazione economica annullamento doc esercizio chiuso. Es:'||aEsAnn||' CDS: '||nvl(aCdCds,'UNDEF')||' Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));

  if aEsAnn is null then
   IBMERR001.RAISE_ERR_GENERICO('Esercizio di annullamento non specificato');
  end if;

  if aCdCds is null then
   IBMERR001.RAISE_ERR_GENERICO('CDS non specificato');
  end if;

-- loop sugli esercizi

For aEsRT in (select esercizio from esercizio_base where
         esercizio < aEsAnn
	 and esercizio >= CNRCTB008.ESERCIZIO_PARTENZA
     order by esercizio desc) loop -- loop sugli esercizi (comincia dall'esercizio precedente)

   aEs := aEsRT.esercizio;


-- per ogni esercizio loop sui cds

   For aCDS in (Select  cd_unita_organizzativa
                From    v_unita_organizzativa_valida
                Where   esercizio = aEs And
                        cd_unita_organizzativa = decode(aCdCds,'*',cd_unita_organizzativa,aCdCds) And
                        fl_cds = 'Y'
                Order by cd_unita_organizzativa) Loop -- loop sui CDS

   Begin
    Select 1 into aNum
    From   v_unita_organizzativa_valida
    Where  cd_unita_organizzativa = aCDS.cd_unita_organizzativa And
           esercizio = aEsAnn And
           fl_cds='Y';

   if not CNRCTB008.ISESERCIZIOAPERTOOCHIUSO(aEsAnn, aCDS.cd_unita_organizzativa) then
     IBMERR001.RAISE_ERR_GENERICO('L''esercizio ('||aEsAnn||') per il cds '||aCDS.cd_unita_organizzativa||' in cui ? stato anullato il documento non ? ancora aperto o chiuso');
   end if;

   IBMUTL015.rollbackRbsBig;
   CNRCTB204.checkChiusuraEsercizio(aEsAnn, aCDS.cd_unita_organizzativa);

   if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs, aCDS.cd_unita_organizzativa)='Y') then
     IBMUTL015.rollbackRbsBig;
     IBMUTL200.logErr(pg_exec, null,'Esercizio economico '||aEs||' non chiuso definitivamente per il cds:'||aCDS.cd_unita_organizzativa,'XX-'||aCDS.cd_unita_organizzativa);
   else
     IBMUTL015.rollbackRbsBig;

-- per ogni esercizio e cds loop sulle UO

     For aUO in (Select cd_unita_organizzativa
                 From   v_unita_organizzativa_valida
                 Where  esercizio = aEs And
                        cd_unita_padre = aCDS.cd_unita_organizzativa And
                        fl_cds = 'N'
                 Order by cd_unita_organizzativa) Loop -- loop sulle UO

      IBMUTL015.rollbackRbsBig;

-- per ogni esercizio, cds e uo loop sui documenti con SELEZIONE SUI DOCUMENTI

-- ANNULLAMENTO TOTALE, QUELLO VECCHIO

      For aTsta In (Select *
                    From   V_DOC_AMM_COGE_TSTA
                    Where  esercizio = aEs And
                           cd_cds_origine = aCDS.cd_unita_organizzativa And
                           cd_uo_origine = aUO.cd_unita_organizzativa And
                           stato_coge in (CNRCTB100.STATO_COEP_INI, CNRCTB100.STATO_COEP_DA_RIP ) And
                           (stato_cofi = CNRCTB100.STATO_GEN_COFI_ANN or fl_congelata = 'Y')) Loop -- loop sui documenti

-- chiamata vera e propria di annullamento

       Begin
        IBMUTL015.rollbackRbsBig;
	CNRCTB205.REGANNULLADOCESCHIUSOCOGE(aEsAnn,aTsta, aUser, sysdate);
	IBMUTL015.commitRbsBig;
        IBMUTL200.logInf(pg_exec, 'ANNDOCESCH',buildCallStringAnnEsChiuso(aEsAnn,aTsta,aUser),'EP-'||aUO.cd_unita_organizzativa||CNRCTB204.getDescDocumento(aTsta));
       Exception WHEN OTHERS THEN
        IBMUTL015.rollbackRbsBig;
  	IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,buildCallStringAnnEsChiuso(aEsAnn,aTsta,aUser),'EP-'||aUO.cd_unita_organizzativa||CNRCTB204.getDescDocumento(aTsta));
       End;

      End Loop; -- Fine loop su docs UO

-- FINE ANNULLAMENTO TOTALE, QUELLO VECCHIO

-- STANI 03.05.2007 AGGIUNTA CONTABILIZZAZIONE DELL'ANNULLAMENTO DELLE RIGHE (loop direttamente sulle righe)

      For aRigheAnn In
                   (Select R.*
                    From   V_DOC_AMM_COGE_TSTA T, V_DOC_AMM_COGE_RIGHE_ANNULLATE R
                    Where  R.esercizio              = aEs                          And
                           R.cd_cds_origine         = aCDS.cd_unita_organizzativa  And
                           R.cd_uo_origine          = aUO.cd_unita_organizzativa   And
                           T.CD_CDS                 = R.CD_CDS                     And
                           T.CD_UNITA_ORGANIZZATIVA = R.CD_UNITA_ORGANIZZATIVA     And
                           T.ESERCIZIO              = R.ESERCIZIO                  And
                           T.CD_TIPO_DOCUMENTO      = R.CD_TIPO_DOCUMENTO          And
                           T.PG_NUMERO_DOCUMENTO    = R.PG_NUMERO_DOCUMENTO        And
                           R.stato_coge in (CNRCTB100.STATO_COEP_INI, CNRCTB100.STATO_COEP_DA_RIP)) Loop -- loop sui documenti

        Select *
        Into   aTestata
        From   v_doc_amm_coge_tsta
        Where  CD_TIPO_DOCUMENTO      = aRigheAnn.CD_TIPO_DOCUMENTO And
               CD_CDS                 = aRigheAnn.CD_CDS And
               CD_UNITA_ORGANIZZATIVA = aRigheAnn.CD_UNITA_ORGANIZZATIVA And
               ESERCIZIO              = aRigheAnn.ESERCIZIO And
               PG_NUMERO_DOCUMENTO    = aRigheAnn.PG_NUMERO_DOCUMENTO;

-- chiamata vera e propria di annullamento delle righe

       Begin
        IBMUTL015.rollbackRbsBig;
	CNRCTB205.regAnnullaRigheDocEsChiusoCOGE(aEsAnn, aRigheAnn, aUser, sysdate);
	IBMUTL015.commitRbsBig;
        IBMUTL200.logInf(pg_exec, 'ANNDOCRIGHEESCH', buildCallStringRigheAnnEsChiu(aEsAnn,aRigheAnn,aUser),'EP-'||aUO.cd_unita_organizzativa||CNRCTB204.getDescDocumento(aTestata));
       Exception WHEN OTHERS THEN
        IBMUTL015.rollbackRbsBig;
  	IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,buildCallStringRigheAnnEsChiu(aEsAnn,aRigheAnn,aUser),'EP-'||aUO.cd_unita_organizzativa||CNRCTB204.getDescDocumento(aTestata));
       End;

      End Loop; -- Fine loop su docs UO

-- FINE ANNULLAMENTO RIGHE, QUELLO NUOVO


     END LOOP; -- Fine loop su UO

    END IF;

   Exception when NO_DATA_FOUND Then
     Null; -- scarto i cds non pi? validi e vado avanti.
--     IBMERR001.RAISE_ERR_GENERICO('(2) Il cds '||aCDS.cd_unita_organizzativa||' non ? pi? valido nell''esercizio ('||aEsAnn||') in cui ? stato richiesto di anullare il documento');
   End;

   END LOOP; -- Fine loop su CDS

  END LOOP; -- Fine loop su esercizio

  IBMUTL200.logInf(pg_exec,'FINE','Termine esecuzione','Termine esecuzione');
 END;


end;
/


