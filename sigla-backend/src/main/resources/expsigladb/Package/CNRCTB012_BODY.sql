--------------------------------------------------------
--  DDL for Package Body CNRCTB012
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB012" is
 procedure checkCessazioneLa(aEsCessazione number,aCdCdr varchar2,aCdLa varchar2) is
  aNum number;
 begin
  begin
   select distinct 1 into aNum from ASS_CDP_LA where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "ASS_CDP_LA" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from ASS_CDP_PDG where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "ASS_CDP_PDG" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from MAPPATURA_LA where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "MAPPATURA_LA" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from PDG_PREVENTIVO_ETR_DET where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "PDG_PREVENTIVO_ETR_DET" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from PDG_PREVENTIVO_SPE_DET where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "PDG_PREVENTIVO_SPE_DET" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from PDG_PREVENTIVO_ETR_VAR where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "PDG_PREVENTIVO_ETR_VAR" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from PDG_PREVENTIVO_SPE_VAR where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "PDG_PREVENTIVO_SPE_VAR" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from ANTICIPO where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "ANTICIPO" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from COMPENSO where
            cd_cdr_genrc = aCdCdr
	    and cd_linea_attivita_genrc  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "COMPENSO" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from ACCERTAMENTO_SCAD_VOCE where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "ACCERTAMENTO_SCAD_VOCE" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  begin
   select distinct 1 into aNum from OBBLIGAZIONE_SCAD_VOCE where
            cd_centro_responsabilita = aCdCdr
	    and cd_linea_attivita  =aCdLa
		and esercizio > aEsCessazione;
   IBMERR001.RAISE_ERR_GENERICO('La linea di attività è utilizzata in tabella "OBBLIGAZIONE_SCAD_VOCE" in esercizio successivo a quello di cessazione specificato');
  exception when NO_DATA_FOUND then
   null;
  end;
  for aObb in (select * from obbligazione o where
                    esercizio = aEsCessazione
				and riportato = 'N'
			    and exists (select 1 from obbligazione_scad_voce where
				                       cd_cds=o.cd_cds
								   and esercizio=o.esercizio
								   and esercizio_originale=o.esercizio_originale
								   and pg_obbligazione=o.pg_obbligazione
								   and cd_centro_responsabilita=aCdCdr
								   and cd_linea_attivita=aCdLa
				)
  ) loop
   if isEligibileRibalt('S',aObb.cd_cds,aObb.esercizio,aObb.esercizio_originale,aObb.pg_obbligazione)='Y' then
    IBMERR001.RAISE_ERR_GENERICO('Esistono '||cnrutil.getLabelObbligazioniMin()||' eligibili di ribaltamento in esercizio di cessazione della linea di attività che utilizzano');
   end if;
  end loop;
  for aAcc in (select * from accertamento a where
                    esercizio = aEsCessazione
				and riportato = 'N'
			    and exists (select 1 from accertamento_scad_voce where
				                       cd_cds=a.cd_cds
								   and esercizio=a.esercizio
								   and esercizio_originale=a.esercizio_originale
								   and pg_accertamento=a.pg_accertamento
								   and cd_centro_responsabilita=aCdCdr
								   and cd_linea_attivita=aCdLa
				)
  ) loop
   if isEligibileRibalt('E',aAcc.cd_cds,aAcc.esercizio,aAcc.esercizio_originale,aAcc.pg_accertamento)='Y' then
    IBMERR001.RAISE_ERR_GENERICO('Esistono '||cnrutil.getLabelObbligazioniMin()||' eligibili di ribaltamento in esercizio di cessazione della linea di attività che utilizzano');
   end if;
  end loop;
 end;

 procedure estrazLdAcessate(aEs number,aUser varchar2) is
 aPgEsec number;
 begin
 	  -- inizializzazione LOG
 	  aPgEsec := IBMUTL200.LOGSTART('ESTR_LA_CESSATE00','Estrazione linee attività cessate impropriamente',null,aUser,null,null);
	  ibmutl200.LOGINF(aPgEsec,'Estrazione linee di attività cessate impropriamente all''esercizio '||aEs||' - START '||to_char(sysdate,'YYYY/MM/DD HH:MI:SS'),'','');

	  lock table linea_attivita in exclusive mode nowait;
	  -- loop su LdA cessate nell'esercizio in input
	  for aLdA in (select * from linea_attivita
	  	  	   	   where esercizio_fine = aEs) loop
		  begin
		  	   checkCessazioneLa(aEs,aLdA.cd_centro_responsabilita,aLdA.cd_linea_attivita);
		  exception when OTHERS then
		       ibmutl200.LOGERR(aPgEsec,'Linea di attività ('||aLdA.cd_centro_responsabilita||', '||aLdA.cd_linea_attivita||'): '||DBMS_UTILITY.FORMAT_ERROR_STACK,'','');
		  end;
	  end loop; -- fine loop LdA
	  ibmutl200.LOGINF(aPgEsec,'Estrazione linee di attività cessate impropriamente all''esercizio '||aEs||' - STOP '||to_char(sysdate,'YYYY/MM/DD HH:MI:SS'),'','');
 end;

end;
