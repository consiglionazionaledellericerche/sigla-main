--------------------------------------------------------
--  DDL for Package Body CNRCTB200
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB200" is
 -- Modifica del 13/11/2003: Per esercizi precedenti a quello di partenza, la chiusura COEP è sempre definitiva
 function getChiusuraCoep(aEs number,aCdCds varchar2) return CHIUSURA_COEP%rowtype is
  aCC CHIUSURA_COEP%rowtype;
  aNumEs number(4);
 begin
  -- Se aEs è minore dell'esercizio inizio dell'applicazione è considerato chiuso definitivamente per il CDS aCdCds

  if aEs < CNRCTB008.ESERCIZIO_PARTENZA then
   aCC.cd_cds:=aCdCds;
   aCC.esercizio:=aEs;
   aCC.stato:=STATO_CHIUSURA_DEF;
   aCC.duva:=sysdate;
   aCC.dacr:=sysdate;
   aCC.utuv:='$$$$$MIGRAZIONE$$$$$';
   aCC.utcr:='$$$$$MIGRAZIONE$$$$$';
   aCC.pg_ver_rec:=1;
   return aCC;
  end if;

  -- Se l'esercizio inizio del CDS è maggiore di aEs, aEs è considerato chiuso definitivamente per il CDS aCdCds
  select esercizio_inizio into aNumEs from unita_organizzativa where cd_unita_organizzativa = aCdCds;
  if aNumEs > aEs then
   aCC.cd_cds:=aCdCds;
   aCC.esercizio:=aEs;
   aCC.stato:=STATO_CHIUSURA_DEF;
   aCC.duva:=sysdate;
   aCC.dacr:=sysdate;
   aCC.utuv:='$$$$$MIGRAZIONE$$$$$';
   aCC.utcr:='$$$$$MIGRAZIONE$$$$$';
   aCC.pg_ver_rec:=1;
   return aCC;
  end if;

  Begin
    select * into aCC from chiusura_coep where esercizio  = aEs and cd_cds = aCdCds;
  Exception
    When No_Data_Found Then
        aCC.cd_cds    := aCdCds;
        aCC.esercizio := aEs;
        aCC.stato     := STATO_PROVA_ANNULLATA; -- SE NON TROVA LA RIGA E' APERTO
        aCC.duva := sysdate;
        aCC.dacr := sysdate;
        aCC.utuv := 'NO_RIGA';
        aCC.utcr := 'NO_RIGA';
        aCC.pg_ver_rec := 1;
  End;
  return aCC;
 end;

 function isChiusuraCoepDef(aEs number,aCdCds varchar2) return char is
  aCC CHIUSURA_COEP%rowtype;
 begin
  if CNRCTB800.isSemStaticoCdsBloccato(aEs, aCdCds, SEMAFORO_CHIUSURA) then
   return 'Y';
  end if;
  aCC:=getChiusuraCoep(aEs, aCdCds);
  if aCC.stato = STATO_CHIUSURA_DEF then
   return 'Y';
  else
   return 'N';
  end if;
 exception when NO_DATA_FOUND then
  return 'N';
 end;

 function isChiusuraCoepProva(aEs number,aCdCds varchar2) return char is
  aCC CHIUSURA_COEP%rowtype;
 begin
  aCC:=getChiusuraCoep(aEs, aCdCds);
  if aCC.stato = STATO_PROVA_CHIUSURA then
   return 'Y';
  else
   return 'N';
  end if;
 exception when NO_DATA_FOUND then
  return 'N';
 end;

 function checkMovimentiCambiati(
  aListaMovOld movimentiList,
  aInListaMovNew movimentiList
 ) return boolean is
  isMovCambiato boolean;
  aTmpList movimentiList;
  aListaMovNew movimentiList;
 begin
  isMovCambiato:=true;

  if aListaMovOld.count != aInListaMovNew.count then
   return true;
  end if;

    -- copio in aListaMovNew il contenuto di aInListaMovNew
  aListaMovNew.delete;
  for k in 1..aInListaMovNew.count loop
   aListaMovNew(k):=aInListaMovNew(k);
  end loop;
  -- TEST DI UGUAGLIANZA DI MOVIMENTI
  isMovCambiato:=false;
  for k in 1..aListaMovOld.count loop
		isMovCambiato:=true; -- Serve per controllare che esista il nuovo movimento in corrispondenza del vecchio
		for u in 1..aListaMovNew.count loop
			if
                  aListaMovOld(k).CD_CDS=aListaMovNew(u).CD_CDS
              AND aListaMovOld(k).ESERCIZIO=aListaMovNew(u).ESERCIZIO
              AND aListaMovOld(k).CD_UNITA_ORGANIZZATIVA=aListaMovOld(u).CD_UNITA_ORGANIZZATIVA
              AND aListaMovOld(k).SEZIONE=aListaMovNew(u).SEZIONE
              AND aListaMovOld(k).TI_ISTITUZ_COMMERC=aListaMovNew(u).TI_ISTITUZ_COMMERC
              AND aListaMovOld(k).IM_MOVIMENTO=aListaMovNew(u).IM_MOVIMENTO
              AND nvl(aListaMovOld(k).FL_MOV_TERZO,'$')=nvl(aListaMovNew(u).FL_MOV_TERZO,'$')
              AND nvl(aListaMovOld(k).STATO,'$')=nvl(aListaMovNew(u).STATO,'$')
              AND nvl(aListaMovOld(k).CD_TERZO,-99999999)=nvl(aListaMovNew(u).CD_TERZO,-99999999)
              AND nvl(aListaMovOld(k).CD_VOCE_EP,'$_$_$_$_$_')=nvl(aListaMovNew(u).CD_VOCE_EP,'$_$_$_$_$_')
              AND nvl(aListaMovOld(k).DT_DA_COMPETENZA_COGE,to_date('01011960','DDMMYYYY'))=nvl(aListaMovNew(u).DT_DA_COMPETENZA_COGE,to_date('01011960','DDMMYYYY'))
              AND nvl(aListaMovOld(k).DT_A_COMPETENZA_COGE,to_date('01011960','DDMMYYYY'))=nvl(aListaMovNew(u).DT_A_COMPETENZA_COGE,to_date('01011960','DDMMYYYY'))
            then -- Trovato il movimento corrispondente
             aTmpList.delete;
             -- Ricopio in aTmpList aListaMovNew eccetto il movimento trovato uguale a corrispondente in vecchia
             for h in 1..aListaMovNew.count loop
              if h!=u then
               aTmpList(aTmpList.count+1):=aListaMovNew(h);
              end if;
             end loop;
             isMovCambiato:=false;
		     -- Resetto aListaMovNew con aTmpList
             aListaMovNew:=aTmpList;
             exit;
            end if;
        end loop; -- Fine loop sulla lista riaggiornata dei movimenti nuovi
        -- Se non viene trovato nessun movimento corrispondente a quello vecchio in processo devo uscire dal loop dei movimenti
        if isMovCambiato=true then
         exit;
        end if;
  end loop; -- Fine loop sui movimenti vecchi
  return isMovCambiato;
 end;

 function isModificata(aListaScrittureOld scrittureList, aInListaScrittureNew scrittureList) return boolean is
  aTmpList scrittureList;
  aListaMovOld movimentiList;
  aListaMovNew movimentiList;
  aScritturaNew scrittura_partita_doppia%rowtype;
  aScritturaOld scrittura_partita_doppia%rowtype;
  aListaScrittureNew scrittureList;
  isCambiata boolean;
 begin
  isCambiata:=false;
  -- Se il numero di scritture è diverso: sicuramente devo riemettere
  if aListaScrittureOld.count != aInListaScrittureNew.count then
   return true;
  end if;

  -- copio in aListaScrittureNew il contenuto di aInListaScrittureNew
  aListaScrittureNew.delete;
  for i in 1..aInListaScrittureNew.count loop
   aListaScrittureNew(i):=aInListaScrittureNew(i);
  end loop;

  for i in 1..aListaScrittureOld.count loop
   isCambiata:=true; -- Serve per controllare che la scrittura nuova in corrispondenza del terzo della vecchia sia trovata
   for j in 1..aListaScrittureNew.count loop
    -- Se la testata della scrittura è uguale devo verificare l'uguaglianza dei movimenti
    if(
           aListaScrittureOld(i).CD_CDS=aListaScrittureNew(j).CD_CDS
       AND aListaScrittureOld(i).ESERCIZIO=aListaScrittureNew(j).ESERCIZIO
       AND aListaScrittureOld(i).CD_UNITA_ORGANIZZATIVA=aListaScrittureNew(j).CD_UNITA_ORGANIZZATIVA
       AND aListaScrittureOld(i).IM_SCRITTURA=aListaScrittureNew(j).IM_SCRITTURA
       AND aListaScrittureOld(i).TI_SCRITTURA=aListaScrittureNew(j).TI_SCRITTURA
       AND aListaScrittureOld(i).STATO=aListaScrittureNew(j).STATO
       AND nvl(aListaScrittureOld(i).ORIGINE_SCRITTURA,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).ORIGINE_SCRITTURA,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_CAUSALE_COGE,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_CAUSALE_COGE,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_TIPO_DOCUMENTO,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_TIPO_DOCUMENTO,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_COMP_DOCUMENTO,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_COMP_DOCUMENTO,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_DIVISA,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_DIVISA,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).COSTO_PLURIENNALE,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).COSTO_PLURIENNALE,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).DS_SCRITTURA,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).DS_SCRITTURA,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_CDS_DOCUMENTO,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_CDS_DOCUMENTO,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_UO_DOCUMENTO,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_UO_DOCUMENTO,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).ESERCIZIO_DOCUMENTO_AMM,-9999)=nvl(aListaScrittureNew(j).ESERCIZIO_DOCUMENTO_AMM,-9999)
       AND nvl(aListaScrittureOld(i).CD_TERZO,-99999999)=nvl(aListaScrittureNew(j).CD_TERZO,-99999999)
       AND nvl(aListaScrittureOld(i).PG_NUMERO_DOCUMENTO,-9999999999)=nvl(aListaScrittureNew(j).PG_NUMERO_DOCUMENTO,-9999999999)
       AND nvl(aListaScrittureOld(i).PG_ENTE,-9999999999)=nvl(aListaScrittureNew(j).PG_ENTE,-9999999999)
       AND nvl(aListaScrittureOld(i).DT_PAGAMENTO,to_date('01011960','DDMMYYYY'))=nvl(aListaScrittureNew(j).DT_PAGAMENTO,to_date('01011960','DDMMYYYY'))
    ) then
	 aListaMovOld.delete;
	 aListaMovNew.delete;
	 aScritturaNew:=aListaScrittureNew(j);
	 aScritturaOld:=aListaScrittureOld(i);
	 getScritturaEPLock(aScritturaOld,aListaMovOld);
	 getScritturaEPLock(aScritturaNew,aListaMovNew);
     -- Se trovo corrispondenza in tutti i movimenti
	 if not checkMovimentiCambiati(aListaMovOld,aListaMovNew) then
             aTmpList.delete;
             -- Ricopio in aTmpList aListaScrittureNew eccetto la scrittura trovata uguale a corrispondente in vecchia lista
             for h in 1..aListaScrittureNew.count loop
              if h!=j then
               aTmpList(aTmpList.count+1):=aListaScrittureNew(h);
              end if;
             end loop;
             aListaScrittureNew:=aTmpList;
             isCambiata:=false; -- Serve per controllare che la scrittura nuova in corrispondenza del terzo della vecchia sia trovata
             exit;
	 else
	         null;
     end if;
	end if;
   end loop;
   if isCambiata then
    return true;
   end if;
  end loop;
  if aListaScrittureNew.count=0 then
   return false;
  else
   return true;
  end if;
 end;

 function isModificata(aScritturaOld IN OUT scrittura_analitica%rowtype, aScritturaNew IN OUT scrittura_analitica%rowtype) return boolean is
  isCambiata boolean;
  isMovCambiato boolean;
  aListaMovOld movAnalitList;
  aListaMovNew movAnalitList;
  aTmpList movAnalitList;
 begin
  isCambiata:=false;
  isMovCambiato:=false;
  aListaMovOld.delete;
  aListaMovNew.delete;
  getScritturaANLock(aScritturaOld,aListaMovOld);
  getScritturaANLock(aScritturaNew,aListaMovNew);
  if aListaMovOld.count != aListaMovNew.count then
   return true;
  end if;
  isMovCambiato:=false;
  if(
          aScritturaOld.CD_CDS<>aScritturaNew.CD_CDS
       OR aScritturaOld.ESERCIZIO<>aScritturaNew.ESERCIZIO
       OR aScritturaOld.CD_UNITA_ORGANIZZATIVA<>aScritturaNew.CD_UNITA_ORGANIZZATIVA
       OR aScritturaOld.IM_SCRITTURA<>aScritturaNew.IM_SCRITTURA
       OR aScritturaOld.TI_SCRITTURA<>aScritturaNew.TI_SCRITTURA
       OR aScritturaOld.STATO<>aScritturaNew.STATO
       OR nvl(aScritturaOld.ORIGINE_SCRITTURA,'$_$_$_$_$_')<>nvl(aScritturaNew.ORIGINE_SCRITTURA,'$_$_$_$_$_')
       OR nvl(aScritturaOld.CD_TIPO_DOCUMENTO,'$_$_$_$_$_')<>nvl(aScritturaNew.CD_TIPO_DOCUMENTO,'$_$_$_$_$_')
       OR nvl(aScritturaOld.CD_CDS_DOCUMENTO,'$_$_$_$_$_')<>nvl(aScritturaNew.CD_CDS_DOCUMENTO,'$_$_$_$_$_')
       OR nvl(aScritturaOld.CD_UO_DOCUMENTO,'$_$_$_$_$_')<>nvl(aScritturaNew.CD_UO_DOCUMENTO,'$_$_$_$_$_')
       OR nvl(aScritturaOld.PG_NUMERO_DOCUMENTO,-9999999999)<>nvl(aScritturaNew.PG_NUMERO_DOCUMENTO,-9999999999)
       OR nvl(aScritturaOld.CD_TERZO,-99999999)<>nvl(aScritturaNew.CD_TERZO,-99999999)
       OR nvl(aScritturaOld.CD_COMP_DOCUMENTO,'$_$_$_$_$_')<>nvl(aScritturaNew.CD_COMP_DOCUMENTO,'$_$_$_$_$_')
       OR nvl(aScritturaOld.DS_SCRITTURA,'$_$_$_$_$_')<>nvl(aScritturaNew.DS_SCRITTURA,'$_$_$_$_$_')
--       OR nvl(aScritturaOld.DT_CONTABILIZZAZIONE,to_date('01011960','DDMMYYYY'))<>nvl(aScritturaNew.DT_CONTABILIZZAZIONE,to_date('01011960','DDMMYYYY'))
       OR nvl(aScritturaOld.DT_CANCELLAZIONE,to_date('01011960','DDMMYYYY'))<>nvl(aScritturaNew.DT_CANCELLAZIONE,to_date('01011960','DDMMYYYY'))
       OR nvl(aScritturaOld.ESERCIZIO_DOCUMENTO_AMM,-9999)<>nvl(aScritturaNew.ESERCIZIO_DOCUMENTO_AMM,-9999)
  ) then
   return true;
  end if;
  for k in 1..aListaMovOld.count loop
   isMovCambiato:=true; -- Serve per controllare che esista il nuovo movimento in corrispondenza del vecchio
   for u in 1..aListaMovNew.count loop
    if
	        aListaMovOld(k).CD_CDS=aListaMovNew(u).CD_CDS
        AND aListaMovOld(k).ESERCIZIO=aListaMovNew(u).ESERCIZIO
        AND aListaMovOld(k).CD_UNITA_ORGANIZZATIVA=aListaMovOld(u).CD_UNITA_ORGANIZZATIVA
        AND aListaMovOld(k).CD_VOCE_EP=aListaMovNew(u).CD_VOCE_EP
        AND aListaMovOld(k).IM_MOVIMENTO=aListaMovNew(u).IM_MOVIMENTO
        AND aListaMovOld(k).SEZIONE=aListaMovNew(u).SEZIONE
        AND aListaMovOld(k).STATO=aListaMovNew(u).STATO
        AND aListaMovOld(k).CD_CENTRO_RESPONSABILITA=aListaMovNew(u).CD_CENTRO_RESPONSABILITA
        AND aListaMovOld(k).CD_NATURA=aListaMovNew(u).CD_NATURA
        AND aListaMovOld(k).DS_MOVIMENTO=aListaMovNew(u).DS_MOVIMENTO
        AND nvl(aListaMovOld(k).CD_TERZO,-99999999)=nvl(aListaMovNew(u).CD_TERZO,-99999999)
        AND nvl(aListaMovOld(k).CD_FUNZIONE,'$_')=nvl(aListaMovNew(u).CD_FUNZIONE,'$_')
        AND nvl(aListaMovOld(k).CD_LINEA_ATTIVITA,'$_$_$_$_$_')=nvl(aListaMovNew(u).CD_LINEA_ATTIVITA,'$_$_$_$_$_')
        AND nvl(aListaMovOld(k).PG_NUMERO_DOCUMENTO,-9999999999)=nvl(aListaMovNew(u).PG_NUMERO_DOCUMENTO,-9999999999)
	then -- Trovato il movimento corrispondente
        aTmpList.delete;
		-- Rimuovo dalla lista nuova il movimento trovato uguale a corrispondente in vecchia
		for h in 1..aListaMovNew.count loop
		 if h!=u then
		  aTmpList(aTmpList.count+1):=aListaMovNew(h);
		 end if;
		end loop;
		isMovCambiato:=false;
		exit;
	 end if;
    end loop;
	if isMovCambiato then
     return true;
	end if;
    aListaMovNew.delete;
    aListaMovNew:=aTmpList;
   end loop;
  return isMovCambiato;
 end;

 function getSezioneOpposta(aMovimento movimento_coge%rowtype) return char is
 begin
  if aMovimento.sezione = CNRCTB200.IS_DARE then
   return CNRCTB200.IS_AVERE;
  else
   return CNRCTB200.IS_DARE;
  end if;
 end;

 function getSezioneOpposta(aSezione char) return char is
 begin
  if aSezione = CNRCTB200.IS_DARE then
   return CNRCTB200.IS_AVERE;
  else
   return CNRCTB200.IS_DARE;
  end if;
 end;

 function getDescScrittura(aScrittura scrittura_partita_doppia%rowtype) return varchar2 is
 begin
  return 'n.'||aScrittura.pg_scrittura||' del Cds '||aScrittura.cd_cds||' UO '||aScrittura.cd_unita_organizzativa||' in esercizio '||aScrittura.esercizio;
 end;

 function getDescScrittura(aScrittura scrittura_analitica%rowtype) return varchar2 is
 begin
  return 'n.'||aScrittura.pg_scrittura||' del Cds '||aScrittura.cd_cds||' UO '||aScrittura.cd_unita_organizzativa||' in esercizio '||aScrittura.esercizio;
 end;

 function getNextProgressivo(aEsercizio number,aCdCds varchar2, aCdUnitaOrganizzativa varchar2, aTipoScrittura varchar2, aUser varchar2, aTSNow date default sysdate) return number is
  aNumRec numerazione_COGE_coan%rowtype;
 begin
  begin
   select * into aNumRec from numerazione_coge_coan where
        cd_cds = aCdCds
    and esercizio = aEsercizio
    and cd_unita_organizzativa = aCdUnitaOrganizzativa
    and ti_documento = aTipoScrittura for update nowait;
  exception when NO_DATA_FOUND then
   aNumRec.esercizio:=aEsercizio;
   aNumRec.cd_cds:=aCdCds;
   aNumRec.cd_unita_organizzativa:=aCdUnitaOrganizzativa;
   aNumRec.ti_documento:=aTipoScrittura;
   aNumRec.corrente:=0;
   aNumRec.primo:=0;
   aNumRec.ultimo:=MAX_NUMERATORE;
   aNumRec.utcr:=aUser;
   aNumRec.utuv:=aUser;
   aNumRec.dacr:=aTSNow;
   aNumRec.duva:=aTSNow;
   aNumRec.pg_ver_rec:=1;
   begin
    ins_NUMERAZIONE_COGE_COAN(aNumRec);
   exception when DUP_VAL_ON_INDEX then
    IBMERR001.RAISE_ERR_GENERICO('Numerazione scritture COGE temporaneamente non disponibile');
   end;
  end;
  update numerazione_coge_coan
   set
    corrente=corrente+1,
	utuv=aUser,
 	duva=aTSNow,
	pg_ver_rec=pg_ver_rec+1
  where
        cd_cds = aCdCds
    and esercizio = aEsercizio
    and cd_unita_organizzativa = aCdUnitaOrganizzativa
    and ti_documento = aTipoScrittura;
  return aNumRec.corrente + 1;
 end;

 procedure disattivaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_partita_doppia
    set
	 attiva='N',
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='N';
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure annullaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_partita_doppia
    set
	 attiva='N',
	 dt_cancellazione=trunc(aTSNow),
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='N';
   aScrittura.dt_cancellazione:=trunc(aTSNow);
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure eliminaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
  for aScr in (select * from scrittura_partita_doppia where
            cd_cds=aScrittura.cd_cds
			and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
			and esercizio=aScrittura.esercizio
			and pg_scrittura=aScrittura.pg_scrittura
		    for update nowait)
  loop
   delete from movimento_coge where
                cd_cds=aScr.cd_cds
			and cd_unita_organizzativa = aScr.cd_unita_organizzativa
			and esercizio=aScr.esercizio
			and pg_scrittura=aScr.pg_scrittura;
   delete from scrittura_partita_doppia where
                cd_cds=aScr.cd_cds
			and cd_unita_organizzativa = aScr.cd_unita_organizzativa
			and esercizio=aScr.esercizio
			and pg_scrittura=aScr.pg_scrittura;
  end loop;
 end;

 procedure annullaScrittura(aScrittura IN OUT scrittura_analitica%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_analitica
    set
	 attiva='N',
	 dt_cancellazione=trunc(aTSNow),
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='N';
   aScrittura.dt_cancellazione:=trunc(aTSNow);
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure eliminaScrittura(aScrittura IN OUT scrittura_analitica%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
  for aScr in (select * from scrittura_analitica where
            cd_cds=aScrittura.cd_cds
			and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
			and esercizio=aScrittura.esercizio
			and pg_scrittura=aScrittura.pg_scrittura
		    for update nowait)
  loop
   delete from movimento_coan where
                cd_cds=aScr.cd_cds
			and cd_unita_organizzativa = aScr.cd_unita_organizzativa
			and esercizio=aScr.esercizio
			and pg_scrittura=aScr.pg_scrittura;
   delete from scrittura_analitica where
                cd_cds=aScr.cd_cds
			and cd_unita_organizzativa = aScr.cd_unita_organizzativa
			and esercizio=aScr.esercizio
			and pg_scrittura=aScr.pg_scrittura;
  end loop;
 end;

 procedure disattivaScrittura(aScrittura IN OUT scrittura_analitica%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_analitica
    set
	 attiva='N',
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='N';
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure attivaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_partita_doppia
    set attiva='Y',
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='Y';
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure attivaScrittura(aScrittura IN OUT scrittura_analitica%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_analitica
    set attiva='Y',
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='Y';
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure getScritturaEPLock(aScrittura IN OUT scrittura_partita_doppia%rowtype, aListaMovimenti OUT movimentiList) is
  aIndex number;
 begin
  begin
   select * into aScrittura from scrittura_partita_doppia where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura EP '||getDescScrittura(aScrittura));
  end;
  aListaMovimenti.delete;
  aIndex:=1;
  for aMovimentoEp in (select * from movimento_coge where
                           cd_cds = aScrittura.cd_cds
                       and esercizio = aScrittura.esercizio
                       and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
					   and pg_scrittura = aScrittura.pg_scrittura
					   for update nowait
					  ) loop
   aListaMovimenti(aIndex):=aMovimentoEp;
   aIndex:=aIndex+1;
  end loop;
 end;

Procedure getScritturaANLock(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti OUT movAnalitList) is
  aIndex number;
 begin
  begin
   select * into aScrittura from scrittura_analitica where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura AN '||getDescScrittura(aScrittura));
  end;
  aListaMovimenti.delete;
  aIndex:=1;
  for aMovimentoAn in (select * from movimento_coan where
                           cd_cds = aScrittura.cd_cds
                       and esercizio = aScrittura.esercizio
                       and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
					   and pg_scrittura = aScrittura.pg_scrittura
					   for update nowait
					  ) loop
   aListaMovimenti(aIndex):=aMovimentoAn;
   aIndex:=aIndex+1;
  end loop;
End;

 procedure creaScrittStornoCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_partita_doppia%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
Dbms_Output.PUT_LINE ('creaScrittStornoCoge 1');
  aScritturaOriginale:=aScrittura;
  disattivaScrittura(aScrittura,aUser,aTSNow);
  aScrittura.pg_scrittura:=null;
  for i in 1 .. aListaMovimenti.count loop
Dbms_Output.PUT_LINE ('creaScrittStornoCoge NEL LOOP');
   aListaMovimenti(i).pg_scrittura:=null;
   aListaMovimenti(i).pg_movimento:=null;
   aListaMovimenti(i).sezione:=getSezioneOpposta(aListaMovimenti(i));
  end loop;
  aScrittura.pg_scrittura_annullata:=aScritturaOriginale.pg_scrittura;
Dbms_Output.PUT_LINE ('creaScrittStornoCoge 2');
  CREASCRITTCOGE(aScrittura,aListaMovimenti);
 end;

  procedure annullaScrittCoan (
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_analitica%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  annullaScrittura(aScrittura,aUser,aTSNow);
  -- Aggiorna i saldi in negativo
  annullaSaldoCoan(
     aScrittura,
     aListaMovimenti);
 end;

  procedure eliminaScrittCoan (
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_analitica%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  -- Aggiorna i saldi in negativo
  annullaSaldoCoan(
     aScrittura,
     aListaMovimenti);
  eliminaScrittura(aScrittura,aUser,aTSNow);
 end;

 procedure annullaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default Sysdate) is
  aScritturaOriginale scrittura_partita_doppia%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  annullaScrittura(aScrittura,aUser,aTSNow);
  -- Aggiorna i saldi in negativo
  annullaSaldoCoge(aScrittura, aListaMovimenti);
 end;

 procedure eliminaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_partita_doppia%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  -- Aggiorna i saldi in negativo
  annullaSaldoCoge(
     aScrittura,
     aListaMovimenti);
  eliminaScrittura(aScrittura,aUser,aTSNow);
 end;

 procedure creaScrittStornoCoan(
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_analitica%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScritturaOriginale:=aScrittura;
  disattivaScrittura(aScrittura,aUser,aTSNow);
  aScrittura.pg_scrittura:=null;
  for i in 1 .. aListaMovimenti.count loop
   aListaMovimenti(i).pg_scrittura:=null;
   aListaMovimenti(i).pg_movimento:=null;
   aListaMovimenti(i).im_movimento:=-aListaMovimenti(i).im_movimento;
  end loop;
  aScrittura.pg_scrittura_annullata:=aScritturaOriginale.pg_scrittura;
--Dbms_Output.put_line ('chiamata a CREASCRITTCOAN 1');
  CREASCRITTCOAN(aScrittura,aListaMovimenti);
 end;

 procedure creaScrittStornoCoge(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti movimentiList;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScrittura.esercizio := aEs;
  aScrittura.cd_cds := aCdCds;
  aScrittura.cd_unita_organizzativa := aCdUnitaOrganizzativa;
  aScrittura.pg_scrittura := aPgScrittura;
  getScritturaEPLock(aScrittura, aListaMovimenti);
  creaScrittStornoCoge(
   aScrittura,
   aListaMovimenti,
   aUser,
   aTSNow);
 end;

 procedure creaScrittStornoCoan(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScrittura scrittura_analitica%rowtype;
  aListaMovimenti movAnalitList;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScrittura.cd_cds := aCdCds;
  aScrittura.esercizio := aEs;
  aScrittura.cd_unita_organizzativa := aCdUnitaOrganizzativa;
  aScrittura.pg_scrittura := aPgScrittura;
  getScritturaANLock(aScrittura, aListaMovimenti);
--Dbms_Output.put_line ('a');
  creaScrittStornoCoan(
   aScrittura,
   aListaMovimenti,
   aUser,
   aTSNow);
 end;

 procedure creaScrittCoge(
   aEs number,
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList
 ) is
   aSaldoCoge saldo_coge%rowtype;
   aTotDare number(15,2);
   aTotAvere number(15,2);
 Begin

  if aListaMovimenti.count=0 then
Dbms_Output.PUT_LINE ('NIENTE MOVIMENTI, ESCE');
   return;
  end if;

Dbms_Output.PUT_LINE ('ARRIVA UTENTE '||aScrittura.UTCR);

  -- Imposta l'esercizio di destinazione
  aScrittura.esercizio:=aEs;
  -- Determina il numero della scrittura
  aScrittura.pg_scrittura:=getNextProgressivo(aScrittura.esercizio,aScrittura.cd_cds, aScrittura.cd_unita_organizzativa, TIPO_COGE,aScrittura.utcr, aScrittura.dacr);

  aScrittura.im_scrittura:=0;
  aTotDare:=0;
  aTotAvere:=0;
  for i in 1 .. aListaMovimenti.count loop
   -- accumula gli importi dei movimenti della sezione dare per avere l'importo complessivo
   -- della scrittura

   if aListaMovimenti(i).SEZIONE = IS_DARE then
    aScrittura.im_scrittura := aScrittura.im_scrittura + aListaMovimenti(i).im_movimento;

    aTotDare := aTotDare + aListaMovimenti(i).im_movimento;
   else
    aTotAvere := aTotAvere + aListaMovimenti(i).im_movimento;
   end if;
  end loop;
  if (aTotAvere <> aTotDare) then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura non quadrata in Dare ('||aTotDare||')/Avere ('||aTotAvere||')');
  end if;

  aScrittura.duva:=aScrittura.dacr;
  aScrittura.utuv:=aScrittura.utcr;
  aScrittura.pg_ver_rec:=1;
  -- Inserisce la testata della scrittura
Dbms_Output.PUT_LINE ('INS '||aScrittura.ESERCIZIO||' '||aScrittura.CD_CDS||' '||aScrittura.CD_UNITA_ORGANIZZATIVA||' '||aScrittura.PG_SCRITTURA);
  ins_SCRITTURA_PARTITA_DOPPIA(aScrittura);
  -- Inserisce i movimenti

  for i in 1 .. aListaMovimenti.count loop
   aListaMovimenti(i).cd_cds:=aScrittura.cd_cds;
   aListaMovimenti(i).esercizio:=aScrittura.esercizio;
   aListaMovimenti(i).cd_unita_organizzativa:=aScrittura.cd_unita_organizzativa;
   aListaMovimenti(i).pg_scrittura:=aScrittura.pg_scrittura;
   aListaMovimenti(i).pg_movimento:=i;
   aListaMovimenti(i).dacr:=aScrittura.dacr;
   aListaMovimenti(i).duva:=aScrittura.duva;
   aListaMovimenti(i).utuv:=aScrittura.utuv;
   aListaMovimenti(i).utcr:=aScrittura.utcr;
   aListaMovimenti(i).pg_ver_rec:=aScrittura.pg_ver_rec;
   if aListaMovimenti(i).TI_ISTITUZ_COMMERC is null then
    aListaMovimenti(i).TI_ISTITUZ_COMMERC := TI_ISTITUZIONALE;
   end if;
   ins_MOVIMENTO_COGE(aListaMovimenti(i));
  end loop;

  -- Aggiorna i saldi

  aggiornaSaldoCoge(
     aScrittura,
     aListaMovimenti);
 end;

 procedure creaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList
 ) is
 begin
  creaScrittCoge(aScrittura.esercizio,aScrittura,aListaMovimenti);
 end;

 procedure creaScrittCoan(
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList
 ) is
   aSaldoCoan saldo_coan%rowtype;
 Begin
  -- Determina il numero della scrittura

  aScrittura.pg_scrittura:=getNextProgressivo(aScrittura.esercizio,aScrittura.cd_cds, aScrittura.cd_unita_organizzativa, TIPO_COAN,aScrittura.utcr, aScrittura.dacr);

  aScrittura.im_scrittura:=0;
  for i in 1 .. aListaMovimenti.count loop

   -- accumula gli importi dei movimenti per avere l'importo complessivo della scrittura
   aScrittura.im_scrittura:=aScrittura.im_scrittura+aListaMovimenti(i).im_movimento;
  end loop;

  aScrittura.duva:=aScrittura.dacr;
  aScrittura.utuv:=aScrittura.utcr;
  aScrittura.pg_ver_rec:=1;
  -- Inserisce la testata della scrittura
  ins_SCRITTURA_ANALITICA(aScrittura);
  -- Inserisce i movimenti

  for i in 1 .. aListaMovimenti.count loop
   aListaMovimenti(i).cd_cds:=aScrittura.cd_cds;
   aListaMovimenti(i).esercizio:=aScrittura.esercizio;
   aListaMovimenti(i).cd_unita_organizzativa:=aScrittura.cd_unita_organizzativa;
   aListaMovimenti(i).pg_scrittura:=aScrittura.pg_scrittura;
   aListaMovimenti(i).pg_movimento:=i;
   aListaMovimenti(i).dacr:=aScrittura.dacr;
   aListaMovimenti(i).duva:=aScrittura.duva;
   aListaMovimenti(i).utuv:=aScrittura.utuv;
   aListaMovimenti(i).utcr:=aScrittura.utcr;
   aListaMovimenti(i).pg_ver_rec:=aScrittura.pg_ver_rec;
--Dbms_Output.put_line ('ins finale');
   ins_MOVIMENTO_COAN(aListaMovimenti(i));

  end loop;

  -- Aggiorna i saldi

  aggiornaSaldoCoan(
     aScrittura,
     aListaMovimenti);
 end;

 procedure aggiornaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype,
                             aListaMovimenti IN OUT movimentiList,
                             aUser varchar2,
                             aTSNow date default sysdate) is
  aSaldo saldo_coge%rowtype;
  aMovimento movimento_coge%rowtype;
  aTotDare number(15,2);
  aTotAvere number(15,2);
 begin
  for i in 1 .. aListaMovimenti.count loop
   begin
    aMovimento:=aListaMovimenti(i);
    select * into aSaldo
    from saldo_coge
    Where cd_cds = aMovimento.cd_cds
      and esercizio = aMovimento.esercizio
      and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
      and cd_voce_ep = aMovimento.cd_voce_ep
      and cd_terzo = aMovimento.cd_terzo
      And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC
    for update nowait;
   exception when NO_DATA_FOUND then
    aSaldo.CD_CDS:=aMovimento.cd_cds;
    aSaldo.ESERCIZIO:=aMovimento.esercizio;
    aSaldo.CD_UNITA_ORGANIZZATIVA:=aMovimento.cd_unita_organizzativa;
    aSaldo.CD_VOCE_EP:=aMovimento.cd_voce_ep;
    aSaldo.CD_TERZO:=aMovimento.cd_terzo;
    aSaldo.TI_ISTITUZ_COMMERC:=aMovimento.TI_ISTITUZ_COMMERC;
    aSaldo.TOT_DARE:=0;
    aSaldo.TOT_AVERE:=0;
    aSaldo.DACR:=aTSNow;
    aSaldo.UTCR:=aUser;
    aSaldo.DUVA:=aTSNow;
    aSaldo.UTUV:=aUser;
    aSaldo.PG_VER_REC:=1;
 	begin
	 ins_SALDO_COGE(aSaldo);
    exception when DUP_VAL_ON_INDEX then
     IBMERR001.RAISE_ERR_GENERICO('Saldo scritture COGE temporaneamente non disponibile');
    end;
   end;
   -- aggiorna il saldo
   aTotAvere:=0;
   aTotDare:=0;

-- Vista la nuova gestione sulle modifiche, i saldi degli storni vanno in sezioni opposta ai documenti
--
--   if aScrittura.pg_scrittura_annullata is not null then -- Sugli storni aggiorna in negativo sulla sezione opposta a quella del movimento
--    if aMovimento.sezione = IS_DARE then
--     aTotAvere:=0-aMovimento.im_movimento;
--   else
-- 	 aTotDare:=0-aMovimento.im_movimento;
--    end if;
--   else

    if aMovimento.sezione = IS_DARE then
         aTotDare:=aMovimento.im_movimento;
    else
 	 aTotAvere:=aMovimento.im_movimento;
    end if;

--   end if;

   update saldo_coge
    Set tot_dare  = tot_dare + aTotDare,
        tot_avere = tot_avere + aTotAvere,
 	 utuv=aUser,
 	 duva=aTSNow,
 	 pg_ver_rec=pg_ver_rec+1
   Where cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_voce_ep = aMovimento.cd_voce_ep
     and cd_terzo = aMovimento.cd_terzo
     And TI_ISTITUZ_COMMERC = aMovimento.TI_ISTITUZ_COMMERC;
  end loop;
 end;

 procedure aggiornaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype, aListaMovimenti IN OUT movimentiList) is
 begin
  aggiornaSaldoCoge(aScrittura, aListaMovimenti, aScrittura.utuv, aScrittura.duva);
 end;

 procedure annullaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype,
                            aListaMovimenti IN OUT movimentiList,
                            aUser varchar2,
                            aTSNow date default sysdate) is
  aSaldo saldo_coge%rowtype;
  aMovimento movimento_coge%rowtype;
  aTotDare number(15,2);
  aTotAvere number(15,2);
 begin
  for i in 1 .. aListaMovimenti.count loop
   begin
    aMovimento:=aListaMovimenti(i);
    select * into aSaldo
    from saldo_coge
    Where cd_cds = aMovimento.cd_cds
      and esercizio = aMovimento.esercizio
      and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
      and cd_voce_ep = aMovimento.cd_voce_ep
      and cd_terzo = aMovimento.cd_terzo
      And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC
      for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Saldo scritture COGE da annullare non disponibile');
   end;
   -- aggiorna il saldo
   aTotAvere:=0;
   aTotDare:=0;
   if aMovimento.sezione = IS_DARE then
    aTotDare:=aMovimento.im_movimento;
   else
 	aTotAvere:=aMovimento.im_movimento;
   end if;
   update saldo_coge
    set
     tot_dare=tot_dare-aTotDare,
     tot_avere=tot_avere-aTotAvere,
 	 utuv=aUser,
 	 duva=aTSNow,
 	 pg_ver_rec=pg_ver_rec+1
   where
         cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_voce_ep = aMovimento.cd_voce_ep
     and cd_terzo = aMovimento.cd_terzo
     And TI_ISTITUZ_COMMERC = aMovimento.TI_ISTITUZ_COMMERC;
  end loop;
 end;

 procedure annullaSaldoCoan(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti IN OUT movAnalitList, aUser varchar2, aTSNow date default sysdate) is
  aSaldo saldo_coan%rowtype;
  aMovimento movimento_coan%rowtype;
  aTotDare number(15,2);
  aTotAvere number(15,2);
 begin
  for i in 1 .. aListaMovimenti.count loop
   begin
    aMovimento:=aListaMovimenti(i);
    select * into aSaldo
    from saldo_coan
    Where cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
     and cd_linea_attivita = aMovimento.cd_linea_attivita
     and cd_voce_ep = aMovimento.cd_voce_ep
     And TI_ISTITUZ_COMMERC = aMovimento.TI_ISTITUZ_COMMERC
    For update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Saldo scritture COGE da annullare non disponibile');
   end;
   -- aggiorna il saldo
   aTotAvere:=0;
   aTotDare:=0;
   if aMovimento.sezione = IS_DARE then
    aTotDare:=aMovimento.im_movimento;
   else
 	aTotAvere:=aMovimento.im_movimento;
   end if;
   update saldo_coan
    Set tot_dare=tot_dare-aTotDare,
        tot_avere=tot_avere-aTotAvere,
 	 utuv=aUser,
 	 duva=aTSNow,
 	 pg_ver_rec=pg_ver_rec+1
   Where cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
     and cd_linea_attivita = aMovimento.cd_linea_attivita
     and cd_voce_ep = aMovimento.cd_voce_ep
     And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC;
  end loop;
 end;

 procedure annullaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype, aListaMovimenti IN OUT movimentiList) is
 begin
  annullaSaldoCoge(aScrittura, aListaMovimenti, aScrittura.utuv, aScrittura.duva);
 end;

 procedure annullaSaldoCoan(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti IN OUT movAnalitList) is
 begin
  annullaSaldoCoan(aScrittura, aListaMovimenti, aScrittura.utuv, aScrittura.duva);
 end;

 procedure aggiornaSaldoCoan(aScrittura IN OUT scrittura_analitica%Rowtype,
                             aListaMovimenti IN OUT movAnalitList,
                             aUser varchar2,
                             aTSNow date default sysdate) is
  aSaldo saldo_coan%rowtype;
  aMovimento movimento_coan%rowtype;
 Begin
  for i in 1 .. aListaMovimenti.count loop
   begin
    aMovimento:=aListaMovimenti(i);

    select * into aSaldo from saldo_coan where
         cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_voce_ep = aMovimento.cd_voce_ep
     and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
     and cd_linea_attivita = aMovimento.cd_linea_attivita
     And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC
     for update nowait;

   exception when NO_DATA_FOUND then

    aSaldo.CD_CDS:=aMovimento.cd_cds;
    aSaldo.ESERCIZIO:=aMovimento.esercizio;
    aSaldo.CD_UNITA_ORGANIZZATIVA:=aMovimento.cd_unita_organizzativa;
    aSaldo.CD_VOCE_EP:=aMovimento.cd_voce_ep;
    aSaldo.cd_centro_responsabilita:=aMovimento.cd_centro_responsabilita;
    aSaldo.cd_linea_attivita:=aMovimento.cd_linea_attivita;
    aSaldo.TI_ISTITUZ_COMMERC := aMovimento.TI_ISTITUZ_COMMERC;
    aSaldo.tot_dare:=0;
    aSaldo.tot_avere:=0;
    aSaldo.DACR:=aTSNow;
    aSaldo.UTCR:=aUser;
    aSaldo.DUVA:=aTSNow;
    aSaldo.UTUV:=aUser;
    aSaldo.PG_VER_REC:=1;

    Begin
      Ins_SALDO_COAN(aSaldo);
    Exception when DUP_VAL_ON_INDEX then
      IBMERR001.RAISE_ERR_GENERICO('Saldo scritture COAN temporaneamente non disponibile');
    End;

   end;
   -- aggiorna il saldo coan
	 if aMovimento.sezione = IS_DARE then
           update saldo_coan
            set
             tot_dare=tot_dare+aMovimento.im_movimento,
        	 utuv=aUser,
         	 duva=aTSNow,
         	 pg_ver_rec=pg_ver_rec+1
           where
                 cd_cds = aMovimento.cd_cds
             and esercizio = aMovimento.esercizio
             and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
             and cd_voce_ep = aMovimento.cd_voce_ep
             and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
             and cd_linea_attivita = aMovimento.cd_linea_attivita
             And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC;
 	 else
           update saldo_coan
            set
             tot_avere=tot_avere+aMovimento.im_movimento,
        	 utuv=aUser,
         	 duva=aTSNow,
         	 pg_ver_rec=pg_ver_rec+1
           where
                 cd_cds = aMovimento.cd_cds
             and esercizio = aMovimento.esercizio
             and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
             and cd_voce_ep = aMovimento.cd_voce_ep
             and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
             and cd_linea_attivita = aMovimento.cd_linea_attivita
             And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC;
     end if;
  end loop;
 end;

 procedure aggiornaSaldoCoan(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti IN OUT movAnalitList) is
 begin
  aggiornaSaldoCoan(aScrittura, aListaMovimenti, aScrittura.utuv, aScrittura.duva);
 end;

 procedure aggiornaSaldoCoge(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti movimentiList;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScrittura.cd_cds := aCdCds;
  aScrittura.esercizio := aEs;
  aScrittura.cd_unita_organizzativa := aCdUnitaOrganizzativa;
  aScrittura.pg_scrittura := aPgScrittura;
  getScritturaEPLock(aScrittura, aListaMovimenti);
  aggiornaSaldoCoge(
   aScrittura,
   aListaMovimenti,
   aUser,
   aTSNow
  );
 end;

 procedure aggiornaSaldoCoan(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScrittura scrittura_analitica%rowtype;
  aListaMovimenti movAnalitList;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScrittura.cd_cds := aCdCds;
  aScrittura.esercizio := aEs;
  aScrittura.cd_unita_organizzativa := aCdUnitaOrganizzativa;
  aScrittura.pg_scrittura := aPgScrittura;
  getScritturaANLock(aScrittura, aListaMovimenti);
  aggiornaSaldoCoan(
   aScrittura,
   aListaMovimenti,
   aUser,
   aTSNow
  );
 end;

 procedure ins_SCRITTURA_PARTITA_DOPPIA (aDest SCRITTURA_PARTITA_DOPPIA%rowtype) is
  begin
   insert into SCRITTURA_PARTITA_DOPPIA (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,ORIGINE_SCRITTURA
    ,CD_TERZO
    ,CD_CAUSALE_COGE
    ,CD_TIPO_DOCUMENTO
	,CD_CDS_DOCUMENTO
	,CD_UO_DOCUMENTO
    ,PG_NUMERO_DOCUMENTO
    ,CD_COMP_DOCUMENTO
    ,IM_SCRITTURA
    ,TI_SCRITTURA
    ,DT_CONTABILIZZAZIONE
    ,DT_PAGAMENTO
    ,DT_CANCELLAZIONE
    ,STATO
    ,CD_DIVISA
    ,COSTO_PLURIENNALE
    ,PG_ENTE
    ,DS_SCRITTURA
    ,PG_SCRITTURA_ANNULLATA
    ,ATTIVA
    ,ESERCIZIO_DOCUMENTO_AMM
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.ORIGINE_SCRITTURA
    ,aDest.CD_TERZO
    ,aDest.CD_CAUSALE_COGE
    ,aDest.CD_TIPO_DOCUMENTO
	,aDest.CD_CDS_DOCUMENTO
	,aDest.CD_UO_DOCUMENTO
    ,aDest.PG_NUMERO_DOCUMENTO
    ,aDest.CD_COMP_DOCUMENTO
    ,aDest.IM_SCRITTURA
    ,aDest.TI_SCRITTURA
    ,aDest.DT_CONTABILIZZAZIONE
    ,aDest.DT_PAGAMENTO
    ,aDest.DT_CANCELLAZIONE
    ,aDest.STATO
    ,aDest.CD_DIVISA
    ,aDest.COSTO_PLURIENNALE
    ,aDest.PG_ENTE
    ,aDest.DS_SCRITTURA
    ,aDest.PG_SCRITTURA_ANNULLATA
    ,aDest.ATTIVA
    ,aDest.ESERCIZIO_DOCUMENTO_AMM
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_SCRITTURA_ANALITICA (aDest SCRITTURA_ANALITICA%rowtype) is
  begin
   insert into SCRITTURA_ANALITICA (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,ORIGINE_SCRITTURA
	,CD_TERZO
    ,CD_TIPO_DOCUMENTO
    ,CD_CDS_DOCUMENTO
    ,CD_UO_DOCUMENTO
    ,PG_NUMERO_DOCUMENTO
    ,CD_COMP_DOCUMENTO
    ,IM_SCRITTURA
    ,TI_SCRITTURA
    ,DT_CONTABILIZZAZIONE
    ,DT_CANCELLAZIONE
    ,STATO
    ,DS_SCRITTURA
    ,PG_SCRITTURA_ANNULLATA
    ,ATTIVA
    ,ESERCIZIO_DOCUMENTO_AMM
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.ORIGINE_SCRITTURA
	,aDest.CD_TERZO
    ,aDest.CD_TIPO_DOCUMENTO
    ,aDest.CD_CDS_DOCUMENTO
    ,aDest.CD_UO_DOCUMENTO
    ,aDest.PG_NUMERO_DOCUMENTO
    ,aDest.CD_COMP_DOCUMENTO
    ,aDest.IM_SCRITTURA
    ,aDest.TI_SCRITTURA
    ,aDest.DT_CONTABILIZZAZIONE
    ,aDest.DT_CANCELLAZIONE
    ,aDest.STATO
    ,aDest.DS_SCRITTURA
    ,aDest.PG_SCRITTURA_ANNULLATA
    ,aDest.ATTIVA
    ,aDest.ESERCIZIO_DOCUMENTO_AMM
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_MOVIMENTO_COGE (aDest MOVIMENTO_COGE%rowtype) is
  begin
   insert into MOVIMENTO_COGE (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,PG_MOVIMENTO
    ,CD_TERZO
    ,CD_VOCE_EP
    ,IM_MOVIMENTO
    ,SEZIONE
    ,DT_DA_COMPETENZA_COGE
    ,DT_A_COMPETENZA_COGE
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
	,TI_ISTITUZ_COMMERC
	,fl_mov_terzo
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.PG_MOVIMENTO
    ,aDest.CD_TERZO
    ,aDest.CD_VOCE_EP
    ,aDest.IM_MOVIMENTO
    ,aDest.SEZIONE
    ,aDest.DT_DA_COMPETENZA_COGE
    ,aDest.DT_A_COMPETENZA_COGE
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
	,aDest.TI_ISTITUZ_COMMERC
	,aDest.fl_mov_terzo
    );
 end;
 procedure ins_MOVIMENTO_COAN (aDest MOVIMENTO_COAN%rowtype) is
  begin
   insert into MOVIMENTO_COAN (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,CD_VOCE_EP
    ,PG_MOVIMENTO
    ,SEZIONE
    ,TI_ISTITUZ_COMMERC
    ,CD_CENTRO_RESPONSABILITA
    ,CD_TERZO
    ,IM_MOVIMENTO
    ,CD_FUNZIONE
    ,CD_NATURA
    ,STATO
    ,DS_MOVIMENTO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_LINEA_ATTIVITA
    ,PG_NUMERO_DOCUMENTO
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.CD_VOCE_EP
    ,aDest.PG_MOVIMENTO
    ,aDest.SEZIONE
    ,ADest.TI_ISTITUZ_COMMERC
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_TERZO
    ,aDest.IM_MOVIMENTO
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.STATO
    ,aDest.DS_MOVIMENTO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.PG_NUMERO_DOCUMENTO
    );
 end;
 procedure ins_SALDO_COGE (aDest SALDO_COGE%rowtype) is
  begin
   insert into SALDO_COGE (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_VOCE_EP
    ,CD_TERZO
    ,TI_ISTITUZ_COMMERC
    ,TOT_DARE
    ,TOT_AVERE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_VOCE_EP
    ,aDest.CD_TERZO
    ,adest.TI_ISTITUZ_COMMERC
    ,aDest.TOT_DARE
    ,aDest.TOT_AVERE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_SALDO_COAN (aDest SALDO_COAN%rowtype) is
  begin
   insert into SALDO_COAN (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,CD_VOCE_EP
    ,TI_ISTITUZ_COMMERC
    ,TOT_DARE
    ,TOT_AVERE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.CD_VOCE_EP
    ,adest.TI_ISTITUZ_COMMERC
    ,aDest.TOT_DARE
    ,aDest.TOT_AVERE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 End;

 procedure ins_NUMERAZIONE_COGE_COAN (aDest NUMERAZIONE_COGE_COAN%rowtype) is
  begin
   insert into NUMERAZIONE_COGE_COAN (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,TI_DOCUMENTO
    ,PRIMO
    ,CORRENTE
    ,ULTIMO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.TI_DOCUMENTO
    ,aDest.PRIMO
    ,aDest.CORRENTE
    ,aDest.ULTIMO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_CHIUSURA_COEP (aDest CHIUSURA_COEP%rowtype) is
  begin
   insert into CHIUSURA_COEP (
     CD_CDS
    ,ESERCIZIO
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
end;
