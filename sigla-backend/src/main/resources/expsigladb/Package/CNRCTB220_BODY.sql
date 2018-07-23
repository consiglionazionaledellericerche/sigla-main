--------------------------------------------------------
--  DDL for Package Body CNRCTB220
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB220" is

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




 -- Solleva eccezione se la chiusura economica è in corso
 procedure checkChiusuraEconomicaInCorso(aEs number, aCdCds varchar2) is
 begin
	if(CNRCTB800.isSemStaticoCdsBloccato(aEs, aCdCds, CNRCTB200.SEMAFORO_CHIUSURA)) then
        IBMERR001.RAISE_ERR_GENERICO('Operazioni di chiusura economica in corso su cds:'||aCdCds||' es.:'||aEs);
	end if;
 end;

 procedure job_registraCogeCoan(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2, aData VARCHAR2) is
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

-- 06/06/2005 eliminato per consentire i RATEI (tra l'altro già previsti)

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
           and dt_registrazione <= nvl(to_date(aData,'dd/mm/yyyy'),dt_registrazione) 
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
                     FROM   INVENTARIO_BENI inv,buono_carico_Scarico_Dett dett,buono_carico_scarico
                     WHERE  cd_cds 		  = aCDS.cd_unita_organizzativa AND
                            esercizio_carico_bene = aEs And
                            fl_migrato 		  = 'Y'  and
                            inv.pg_inventario 	  = dett.pg_inventario and
                            inv.nr_inventario 	  = dett.nr_inventario and
                            inv.progressivo   	  = dett.progressivo   and
                            inv.dacr             like dett.dacr        and
                            buono_carico_scarico.pg_inventario = dett.pg_inventario  and
        										buono_carico_scarico.ti_documento = dett.ti_documento and
        										buono_carico_scarico.esercizio = dett.esercizio and
        										buono_carico_scarico.pg_buono_c_s = dett.pg_buono_c_s  and
                            buono_carico_scarico.data_registrazione <= nvl(to_date(aData,'dd/mm/yyyy'),buono_carico_scarico.data_registrazione)  and
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
				 and ti_documento = 'S' and
				 ( bcs.pg_inventario,bcs.ti_documento,bcs.esercizio,bcs.pg_buono_c_s) in
				 (select buono_carico_scarico.pg_inventario ,buono_carico_scarico.ti_documento ,buono_carico_scarico.esercizio,buono_carico_scarico.pg_buono_c_s 
				 from buono_carico_scarico
          where 
             buono_carico_scarico.data_registrazione <= nvl(to_date(aData,'dd/mm/yyyy'),buono_carico_scarico.data_registrazione))
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
				 and ti_documento = 'C' and
				  ( bcs.pg_inventario,bcs.ti_documento,bcs.esercizio,bcs.pg_buono_c_s) in
				 (select buono_carico_scarico.pg_inventario ,buono_carico_scarico.ti_documento ,buono_carico_scarico.esercizio,buono_carico_scarico.pg_buono_c_s 
				 from buono_carico_scarico
          where 
             buono_carico_scarico.data_registrazione <= nvl(to_date(aData,'dd/mm/yyyy'),buono_carico_scarico.data_registrazione))
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
     and ((stato in (CNRCTB038.STATO_AUT_ANN) and DT_EMISSIONE_DOCUMENTO_CONT <= nvl(to_date(aData,'dd/mm/yyyy'),DT_EMISSIONE_DOCUMENTO_CONT))     
     or   (stato in (CNRCTB038.STATO_AUT_ESI) and DT_ESITO_DOCUMENTO_CONT <= nvl(to_date(aData,'dd/mm/yyyy'),DT_ESITO_DOCUMENTO_CONT))) 
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
     IBMERR001.RAISE_ERR_GENERICO('L''esercizio ('||aEsAnn||') per il cds '||aCDS.cd_unita_organizzativa||' in cui è stato anullato il documento non è ancora aperto o chiuso');
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
     Null; -- scarto i cds non più validi e vado avanti.
--     IBMERR001.RAISE_ERR_GENERICO('(2) Il cds '||aCDS.cd_unita_organizzativa||' non è più valido nell''esercizio ('||aEsAnn||') in cui è stato richiesto di anullare il documento');
   End;

   END LOOP; -- Fine loop su CDS

  END LOOP; -- Fine loop su esercizio

  IBMUTL200.logInf(pg_exec,'FINE','Termine esecuzione','Termine esecuzione');
 END;


end;
