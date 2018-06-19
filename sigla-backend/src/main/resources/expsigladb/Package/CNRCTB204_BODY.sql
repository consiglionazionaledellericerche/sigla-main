--------------------------------------------------------
--  DDL for Package Body CNRCTB204
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB204" is

 function pk2String(aLiqIva liquidazione_iva%rowtype) return varchar2 is
 begin
  return aLiqIva.tipo_liquidazione||'-'||aLiqIva.cd_cds||'-'||aLiqIva.esercizio||'-'||aLiqIva.cd_unita_organizzativa||'-'||to_char(aLiqIva.dt_inizio,'YYYYMMDD')||'-'||to_char(aLiqIva.dt_fine,'YYYYMMDD');
 end;

 function isEsercizioPartenza(aEs number) return boolean is
 begin
  if aEs = CNRCTB008.ESERCIZIO_PARTENZA then
   return true;
  else
   return false;
  end if;
 end;

 function getEsercizio(aTS date) return number is
 begin
  return to_number(to_char(aTS,'YYYY'));
 end;

 function checkIsCogeDifferita(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return boolean is
 begin
  return false;
 end;

 function getDescDocumento(aBS buono_carico_scarico_dett%rowtype) return varchar2 is
  aStringa varchar2(1000);
 begin
  aStringa:='';
  aStringa:=aStringa||' Pg Invent:'||aBS.pg_inventario;
  aStringa:=aStringa||' Tipo:'||aBS.ti_documento;
  aStringa:=aStringa||' Es:'||aBS.ESERCIZIO;
  aStringa:=aStringa||' Pg Buono C/S:'||aBS.pg_buono_c_s;
  aStringa:=aStringa||' Nr. invent.:'||aBS.nr_inventario;
  aStringa:=aStringa||' Progr.:'||aBS.progressivo;
  return aStringa;
 end;

 function getDescDocumento(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return varchar2 is
  aStringa varchar2(1000);
 begin
--  aStringa:='P/U: P';
  aStringa:='';
  aStringa:=aStringa||' Cds:'||aDocTst.CD_CDS;
  aStringa:=aStringa||' Uo:'||aDocTst.CD_UNITA_ORGANIZZATIVA;
  aStringa:=aStringa||' Es:'||aDocTst.ESERCIZIO;
  aStringa:=aStringa||' Tipo:'||aDocTst.CD_TIPO_DOCUMENTO;
  aStringa:=aStringa||' Dt_reg:'||aDocTst.DT_REGISTRAZIONE;
  aStringa:=aStringa||' Pg_doc:'||aDocTst.PG_NUMERO_DOCUMENTO;
  aStringa:=aStringa||' Stipo:'||aDocTst.TI_FATTURA;
  return aStringa;
 end;

 function getDescDocumento(aDocTst V_DOC_ULT_COGE_TSTA%rowtype) return varchar2 IS
  aStringa varchar2(1000);
 begin
--  aStringa:='P/U: U';
  aStringa:='';
  aStringa:=aStringa||' Cds:'||aDocTst.CD_CDS;
  aStringa:=aStringa||' Uo:'||aDocTst.CD_UNITA_ORGANIZZATIVA;
  aStringa:=aStringa||' Es:'||aDocTst.ESERCIZIO;
  aStringa:=aStringa||' Man/Rev.:'||aDocTst.CD_TIPO_DOCUMENTO_CONT;
  aStringa:=aStringa||' Tipo:'||aDocTst.TI_MAN_REV;
  aStringa:=aStringa||' Dt_reg:'||aDocTst.DT_EMISSIONE_DOCUMENTO_CONT;
  aStringa:=aStringa||' Dt_esi:'||aDocTst.DT_ESITO_DOCUMENTO_CONT;
  aStringa:=aStringa||' Pg_doc:'||aDocTst.PG_DOCUMENTO_CONT;
  return aStringa;
 end;

 function getCompetenzaFuoriEsercizio(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return boolean is
  isCompetenzaFuoriEsercizio boolean;
  aEsercizioComp number(4);
 begin
  isCompetenzaFuoriEsercizio:=false;
  aEsercizioComp:=getEsercizio(aDocTst.dt_a_competenza_coge);
  if aEsercizioComp = aDocTst.esercizio - 1 then
   isCompetenzaFuoriEsercizio:=true;
  end if;
  if aEsercizioComp < aDocTst.esercizio - 1 then
   IBMERR001.RAISE_ERR_GENERICO('La competenza economica (data fine periodo) del documento è errata:'||aDocTst.dt_a_competenza_coge||' per esercizio:'||aDocTst.esercizio);
  end if;
  return isCompetenzaFuoriEsercizio;
 end;

Function getCompetenzaacavalloconEsPrec(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return Boolean Is
  isCompetenzaacavallo boolean;
  Esercizio_daComp number(4);
  Esercizio_aComp  number(4);
 begin
  isCompetenzaacavallo := false;
  Esercizio_daComp := getEsercizio(aDocTst.dt_da_competenza_coge);
  Esercizio_aComp  := getEsercizio(aDocTst.dt_a_competenza_coge);

  if aDocTst.esercizio = Esercizio_aComp And
        Esercizio_daComp = Esercizio_aComp - 1 Then
   isCompetenzaacavallo := true;
  end if;

  if aDocTst.esercizio = Esercizio_aComp And
        Esercizio_daComp < Esercizio_aComp - 1 Then
   IBMERR001.RAISE_ERR_GENERICO('La competenza economica (data fine periodo) del documento è errata:'||aDocTst.dt_a_competenza_coge||' per esercizio:'||aDocTst.esercizio);
  end if;

  return isCompetenzaacavallo;
End;

 function getAnticipo(
  aCdTipoDoc varchar2,
  aEsDoc number,
  aCdCds varchar2,
  aCdUo varchar2,
  aPgDoc number
 ) return anticipo%rowtype is
  aMissione missione%rowtype;
  aCompenso compenso%rowtype;
  aAnticipo anticipo%rowtype;
 Begin
 Dbms_Output.put_line ('inizio ricerca anticipo da '||aCdTipoDoc);
  -- Solo MISSIONI e COMPENSI hanno anticipo
  if aCdTipoDoc not in (CNRCTB100.TI_COMPENSO, CNRCTB100.TI_MISSIONE) Then

   return aAnticipo;
  end if;
  if aCdTipoDoc = CNRCTB100.TI_COMPENSO then
   begin
    select * into aCompenso from compenso where
         esercizio = aEsDoc
	 and cd_cds = aCdCds
	 and cd_unita_organizzativa = aCdUo
     and pg_compenso = aPgDoc
    for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento amministrativo di tipo compenso non trovato');
   end;
   -- Se il compenso non è associato a missione
   if aCompenso.pg_missione is null Then

    return aAnticipo;
   end if;
   begin
    select * into aMissione from missione where
         cd_cds = aCompenso.cd_cds_missione
 	 and cd_unita_organizzativa = aCompenso.cd_uo_missione
	 and esercizio = aCompenso.esercizio_missione
	 and pg_missione = aCompenso.pg_missione
	 for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Missione associata a compenso non trovata');
   end;
  else
   -- Si tratta di missione
   begin
    select * into aMissione from missione where
         cd_cds = aCdCds
	 and cd_unita_organizzativa = aCdUo
	 and esercizio = aEsDoc
	 and pg_missione = aPgDoc
	for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento amministrativo di tipo missione non trovato');
   end;
  end if;
  begin
   -- Se la missione non è associata ad anticipo
   if aMissione.pg_anticipo is null Then

    return aAnticipo;
   end if;
   select * into aAnticipo from anticipo where
          cd_cds = aMissione.cd_cds_anticipo
	  and cd_unita_organizzativa = aMissione.cd_uo_anticipo
  	  and esercizio = aMissione.esercizio_anticipo
	  and pg_anticipo = aMissione.pg_anticipo
	  for update nowait;

   return aAnticipo;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Documento amministrativo di tipo anticipo collegato a missione non trovato');
  end;
  return aAnticipo;
 end;

 function getAnticipo(aDocTst V_DOC_ULT_COGE_RIGA%rowtype) return anticipo%rowtype is
 begin
  return getAnticipo(
    aDocTst.cd_tipo_doc,
    aDocTst.esercizio_doc,
    aDocTst.cd_cds_doc,
    aDocTst.cd_uo_doc,
    aDocTst.pg_numero_doc);
 end;

 function getAnticipo(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return anticipo%rowtype is
 begin
  return getAnticipo(
    aDocTst.cd_tipo_documento,
    aDocTst.esercizio,
    aDocTst.cd_cds,
    aDocTst.cd_unita_organizzativa,
    aDocTst.pg_numero_documento);
 end;

 -- Ritorna 'Y' se il compenso deriva da missione con anticipo
 -- e l'anticipo è maggiore del netto percipiente

 function isCompConAntMaggNetto(aEs number,aCdCds varchar2, aCdUo varchar2, aPgComp number)  return char is
  aCompenso compenso%rowtype;
  aAnticipo anticipo%rowtype;
 begin
  begin
   select * into aCompenso from compenso where
         esercizio = aEs
	 and cd_cds = aCdCds
	 and cd_unita_organizzativa = aCdUo
     and pg_compenso = aPgComp
    for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Documento amministrativo di tipo compenso non trovato');
  end;
  aAnticipo:=getAnticipo(CNRCTB100.TI_COMPENSO,aEs,aCdCds,aCdUo,aPgComp);
  if
 	              aAnticipo.pg_anticipo is not null
	          and aAnticipo.im_anticipo > aCompenso.im_netto_percipiente
  then
   return 'Y';
  else
   return 'N';
  end if;
 end;

 function isCompConAntMaggNetto(aDocTst v_doc_amm_coge_tsta%rowtype) return char is
 begin
  if aDocTst.cd_tipo_documento <> CNRCTB100.TI_COMPENSO then
   IBMERR001.RAISE_ERR_GENERICO('Tipo di documento non compatibile: il documento deve essere un compenso');
  end if;
  return isCompConAntMaggNetto(aDocTst.esercizio,aDocTst.cd_cds, aDocTst.cd_unita_organizzativa, aDocTst.pg_numero_documento);
 end;

 function isCompConAntMaggNetto(aDocTst v_doc_ult_coge_riga%rowtype) return char is
 begin
  if aDocTst.cd_tipo_doc <> CNRCTB100.TI_COMPENSO then
   IBMERR001.RAISE_ERR_GENERICO('Tipo di documento non compatibile: il documento deve essere un compenso');
  end if;
  return isCompConAntMaggNetto(aDocTst.esercizio_doc,aDocTst.cd_cds_doc, aDocTst.cd_uo_doc, aDocTst.pg_numero_doc);
 end;

 procedure getScritturePEPLock(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aListaScritture OUT CNRCTB200.scrittureList) is
  i NUMBER;
 begin
  i:=1;
  for aScr in (
   select * from scrittura_partita_doppia where
	    cd_cds_documento = aDocTst.cd_cds
    and esercizio_documento_amm = aDocTst.esercizio
	and cd_uo_documento = aDocTst.cd_unita_organizzativa
	and pg_numero_documento = aDocTst.pg_numero_documento
	and cd_tipo_documento = aDocTst.cd_tipo_documento
	and origine_scrittura = CNRCTB200.ORIGINE_DOCUMENTO_AMM
	and ti_scrittura = CNRCTB200.TI_SCRITTURA_PRIMA
	and cd_causale_coge is null
	and attiva = 'Y'
   for update nowait
  ) loop
   aListaScritture(i):=aScr;
   i:=i+1;
  end loop;
 end;

 procedure getScrittureAmmortamentoLock(aEs number, aCdCds varchar2, aListaScritture OUT CNRCTB200.scrittureList) is
  i NUMBER;
 begin
  i:=1;
  for aScr in (
   select * from scrittura_partita_doppia where
	    cd_cds = aCdCds
    and esercizio = aEs
	and origine_scrittura = CNRCTB200.ORIGINE_CHIUSURA
	and cd_causale_coge = CNRCTB200.CAU_AMMORTAMENTO
	and ti_scrittura = CNRCTB200.TI_SCRITTURA_SINGOLA
	and attiva = 'Y'
   for update nowait
  ) loop
   aListaScritture(i):=aScr;
   i:=i+1;
  end loop;
 end;

 procedure getScrittureLock(aEs number, aCdCds varchar2, aCdCausaleCoge varchar2, aOrigine varchar2, aListaScritture OUT CNRCTB200.scrittureList) is
  i NUMBER;
 begin
  i:=1;
  for aScr in (
   select * from scrittura_partita_doppia where
	    cd_cds = nvl(aCdCds,cd_cds)
    and esercizio = nvl(aEs,esercizio)
	and cd_causale_coge = aCdCausaleCoge
	and origine_scrittura = aOrigine
	and attiva = 'Y'
   for update nowait
  ) loop
   aListaScritture(i):=aScr;
   i:=i+1;
  end loop;
 end;

Function ISREVERSALESUPGIRO (aDocRiga In V_DOC_ULT_COGE_RIGA%Rowtype) Return Boolean Is
pgiro_sn CHAR(1);
Begin
Select Distinct(fl_pgiro)
Into   pgiro_sn
From   reversale_riga
Where  CD_CDS                      = aDocRiga.CD_CDS And
       ESERCIZIO                   = aDocRiga.ESERCIZIO And
       PG_REVERSALE                = aDocRiga.PG_DOCUMENTO_CONT And
       CD_CDS_DOC_AMM              = aDocRiga.CD_CDS_DOC And
       CD_UO_DOC_AMM               = aDocRiga.CD_UO_DOC And
       ESERCIZIO_DOC_AMM           = aDocRiga.ESERCIZIO_DOC And
       CD_TIPO_DOCUMENTO_AMM       = aDocRiga.CD_TIPO_DOC And
       PG_DOC_AMM                  = aDocRiga.PG_NUMERO_DOC;

If pgiro_sn = 'Y' Then
  Return True;
Elsif pgiro_sn = 'N' Then
  Return False;
End If;
Exception
When Too_Many_Rows Then
  Return False;
End;

Function ISMANDATOCOLLSUPGIRO (aDocRiga In V_DOC_ULT_COGE_RIGA%Rowtype) Return Boolean Is
pgiro_sn        CHAR(1);
MANDCOLL        MANDATO%Rowtype;
Begin

If aDocRiga.CD_TIPO_DOCUMENTO_CONT = 'REV' Then

Select  M.*
Into    MANDCOLL
From    MANDATO M, ASS_MANDATO_REVERSALE A
Where   A.CD_CDS_REVERSALE       = aDocRiga.CD_CDS AND
        A.ESERCIZIO_REVERSALE    = aDocRiga.ESERCIZIO AND
        A.PG_REVERSALE           = aDocRiga.PG_DOCUMENTO_CONT And
        A.CD_CDS_MANDATO         = M.CD_CDS And
        A.ESERCIZIO_MANDATO      = M.ESERCIZIO And
        A.PG_MANDATO             = M.PG_MANDATO;

Select Distinct(fl_pgiro)
Into   pgiro_sn
From   MANDATO_RIGA
Where  CD_CDS                      = MANDCOLL.CD_CDS And
       ESERCIZIO                   = MANDCOLL.ESERCIZIO And
       PG_MANDATO                  = MANDCOLL.PG_MANDATO;

If pgiro_sn = 'Y' Then
  Return True;
Elsif pgiro_sn = 'N' Then
  Return False;
End If;

Else
   IBMERR001.RAISE_ERR_GENERICO('Si sta cercando di determinare il Mandato collegato ad una Reversale ma il documento contabile non è una Reversale.');
End If;

Exception
When Too_Many_Rows Then
  Return False;
End;

 Function ISVOCECOLLCATEGORIA (INES NUMBER, INGEST CHAR, INAPP CHAR, INELVOCE VARCHAR2) Return Boolean Is
 VOCE_ASS_CAT NUMBER;
 Begin
--Dbms_Output.PUT_LINE ('ENTRO FUNZIONE '||INES||' '||INGEST||' '||INAPP||' '||INELVOCE);
   If INES Is Null Or INGEST Is Null Or INAPP Is Null Or INELVOCE  Is Null Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile determinare se l''elemento voce è associato a categoria, i dati forniti sono incompleti.');
   End If;

   Select  Count(*)
   Into    VOCE_ASS_CAT
   From    CATEGORIA_GRUPPO_VOCE
   Where   ESERCIZIO        = INES And
           TI_APPARTENENZA  = INAPP And
           TI_GESTIONE      = INGEST And
           CD_ELEMENTO_VOCE = INELVOCE;

   --Dbms_Output.PUT_LINE ('VOCE_ASS_CAT '||VOCE_ASS_CAT);

   If VOCE_ASS_CAT > 0 Then
--Dbms_Output.PUT_LINE ('RITORNO TRUE');
      Return True;
   Else
--Dbms_Output.PUT_LINE ('RITORNO FALSE');
      Return False;
   End If;
 End;


 Procedure getScritturePEPLock(aEs number, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdCausaleCoge varchar2, aListaScritture OUT CNRCTB200.scrittureList) is
  i NUMBER;
 begin
  i:=1;
  for aScr in (
   select * from scrittura_partita_doppia where
	    cd_cds_documento = aDocTst.cd_cds
    and esercizio = nvl(aEs,esercizio)
    and esercizio_documento_amm = aDocTst.esercizio
	and cd_uo_documento = aDocTst.cd_unita_organizzativa
	and pg_numero_documento = aDocTst.pg_numero_documento
	and cd_tipo_documento = aDocTst.cd_tipo_documento
	and cd_causale_coge = aCdCausaleCoge
	and origine_scrittura = CNRCTB200.ORIGINE_DOCUMENTO_AMM
	and attiva = 'Y'
   for update nowait
  ) loop
   aListaScritture(i):=aScr;
   i:=i+1;
  end loop;
 end;

 procedure getScritturePEPLock(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdCausaleCoge varchar2, aListaScritture OUT CNRCTB200.scrittureList) is
 begin
  getScritturePEPLock(aDocTst.esercizio, aDocTst, aCdCausaleCoge,aListaScritture);
 end;

 procedure getScrittureUEPLock(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aListaScritture OUT CNRCTB200.scrittureList) is
 begin
  aListaScritture.delete;
  for aScr in (
   select * from scrittura_partita_doppia where
	    cd_cds_documento = aDocTst.cd_cds
    and esercizio = aDocTst.esercizio
	and cd_uo_documento = aDocTst.cd_unita_organizzativa
	and pg_numero_documento = aDocTst.pg_documento_cont
	and cd_tipo_documento = aDocTst.cd_tipo_documento_cont
	and origine_scrittura = CNRCTB200.ORIGINE_DOCUMENTO_CONT
	and ti_scrittura in (CNRCTB200.TI_SCRITTURA_ULTIMA,CNRCTB200.TI_SCRITTURA_SINGOLA)
	and attiva = 'Y'
   for update nowait
  ) loop
   aListaScritture(aListaScritture.count+1):=aScr;
  end loop;
 end;

 function getSezione(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype) return char is
  aSezione char(1);
 begin
  aSezione:=CNRCTB100.getSezioneEconomica(aDocTst.cd_tipo_documento);
  if
         aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA
	 and aDocTst.ti_fattura = CNRCTB100.TI_FATT_NOTA_C
	 or
         aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA
	 and aDocTst.ti_fattura = CNRCTB100.TI_FATT_NOTA_C
  then
   aSezione:=CNRCTB200.getSezioneOpposta(aSezione);
  end if;
  return aSezione;
 end;

 function getSezione(aDocTst V_DOC_ULT_COGE_TSTA%rowtype) return char is
  aSezione char(1);
 begin
	  if    aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_MAN then
	   aSezione:=CNRCTB200.IS_DARE;
	  elsif aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_REV then
	   aSezione:=CNRCTB200.IS_AVERE;
      end if;
  return aSezione;
 end;

 function getContoEpPgiro(aDocTst v_doc_amm_coge_tsta%rowtype,aDoc v_doc_amm_coge_riga%rowtype) return voce_ep%rowtype is
  aContoEPPGiro voce_ep%rowtype;
  aAccBase accertamento%rowtype;
  aObbBase obbligazione%rowtype;
  aAssPgiro ass_obb_acr_pgiro%rowtype;
 begin

  -- Modifica del 25/08/2003 -> se la competenza è in esercizio precedente e l'esercizio attuale è il primo dell'applicazione
  -- Il conto di costo/ricavo è SP Iniziale o Fatture da emettere
  if CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) and aDocTst.esercizio = CNRCTB008.ESERCIZIO_PARTENZA then
   -- Solo per la fattura attiva si usa il conto speciale delle fatture da emettere
   -- Modifica del 19/02/2004 -> allargata alle fatture passive e notedi credito/debito la gestione fatta per le fatture attive
   if aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA then -- and aDocTst.ti_fattura = CNRCTB100.TI_FATT_FATTURA then
    return CNRCTB002.GETVOCEEPFATTUREDAEMETTERE(aDocTst.esercizio);
   elsif aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA then -- and aDocTst.ti_fattura = CNRCTB100.TI_FATT_FATTURA then
    return CNRCTB002.GETVOCEEPFATTUREDARICEVERE(aDocTst.esercizio);
   else -- Per gli altri documenti ritorna il conto di Stato Patrimoniale Iniziale
    return CNRCTB002.GETVOCEEPSTATOPATRINIZIALE(aDocTst.esercizio);
   end if;
  end if;
  if CNRCTB204.getSezione(aDocTst,aDoc) =CNRCTB200.IS_DARE then
  		return CNRCTB002.getVoceEpDebDiversiPgiro(aDocTst.esercizio);
  else
  		return CNRCTB002.getVoceEpCredDiversiPgiro(aDocTst.esercizio);
  end if;

 end;

 -- =======================================
 --
 -- CONTABILIZZAZIONE CORI
 --
 -- =======================================

 function getAssCoriEp(
  aEs number,
  aTipoCori varchar2,
  aTipoEntePercipiente char,
  aSezione char
 ) return ass_tipo_cori_voce_ep%rowtype is
  aEffCori ass_tipo_cori_voce_ep%rowtype;
 begin
  begin
	  select * into aEffCori from ass_tipo_cori_voce_ep where
           esercizio = aEs
	   and cd_contributo_ritenuta = aTipoCori
       and ti_ente_percepiente = aTipoEntePercipiente
	   and sezione = aSezione;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Associazione cori/coep non trovata per CORI:'||aTipoCori||' ente/perc:'||aTipoEntePercipiente||' sezione:'||aSezione);
  end;
  return aEffCori;
 end;

 function getAssCoriEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype) return ass_tipo_cori_voce_ep%rowtype is
  aEffCori ass_tipo_cori_voce_ep%rowtype;
  aSez char(1);
 begin

-- RECUPERA LA SEZIONE STANDARD PER IL DOCUMENTO

  aSez := getSezione(aDocTst, aCori);

-- E POI LA INVERTE NEL CASO DI NOTE DI CREDITO, MA SOLO PER IL CORRETTO RECUPERO DEL CONTO (PRIMA PER LE NOTE ATTIVE
-- USAVA IVA A CREDITO ANZICHE' LA RIDUZIONE DELL'IVA A DEBITO, E VICEVERSA)

-- 06.12.2006 LA GESTIONE DELLE NOTE DI CREDITO (RIDUZIONE DEL VALORE INIZIALE) IL CONTO IVA DA USARE E' LO STESSO DELLE FATTURE:
-- SE LA FATTURA ATTIVA MOVIMENTA IVA A DEBITO LA NOTA DI CREDITO DEVE MOVIMENTARE LA RIDUZIONE DELL'IVA A DEBITO
-- SE LA FATTURA PASSIVA MOVIMENTA IVA A CREDITO LA NOTA DI CREDITO DEVE MOVIMENTARE LA RIDUZIONE DELL'IVA A CREDITO

  If aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA And aDocTst.ti_fattura = CNRCTB100.TI_FATT_NOTA_C or
     aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA And aDocTst.ti_fattura = CNRCTB100.TI_FATT_NOTA_C
  then
   aSez := CNRCTB200.getSezioneOpposta(aSez);
  end if;

  return getAssCoriEp(aCori.esercizio, aCori.cd_contributo_ritenuta,aCori.ti_ente_percepiente, aSez);
 end;

 function trovaContoEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype) return voce_ep%rowtype is
  aEffCori ass_tipo_cori_voce_ep%rowtype;
  aContoEp voce_ep%rowtype;
  aSez char(1);
 Begin
Dbms_Output.PUT_LINE ('ENTRA IN trovaContoEp CORI');
    -- IBMUTL010.log(GETDESCDOCUMENTO(aDocTst),'TEST');

 -- Modifica del 25/08/2003 -> se la competenza è in esercizio precedente e l'esercizio attuale è il primo dell'applicazione
 -- Il conto di costo/ricavo è SP Iniziale
    if CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) and aDocTst.esercizio = CNRCTB008.ESERCIZIO_PARTENZA then
     -- Se non si  tratta di fattura attiva si usa il conto speciale stato patrimoniale iniziale
     -- Modifica del 19/02/2004 -> allargata a fatture passive e note di credito/debito la gestione fatta per le fatture attive
     if not (
	         aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA -- and aDocTst.ti_fattura = CNRCTB100.TI_FATT_FATTURA
    	  or aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA -- and aDocTst.ti_fattura = CNRCTB100.TI_FATT_FATTURA
		) then
      return CNRCTB002.GETVOCEEPSTATOPATRINIZIALE(aDocTst.esercizio);
     end if;
    end if;


	--  30/10/2002 Se il documento è un compenso o fattura con IVA su partita di giro il conto da utilizzare è quello del terzo diversi contropatita (debito)
    for aDoc in (select * from v_doc_amm_coge_riga where
          cd_tipo_documento = aDocTst.cd_tipo_documento
      and cd_cds = aDocTst.cd_cds
      and cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
      and esercizio = aDocTst.esercizio
      and pg_numero_documento = aDocTst.pg_numero_documento
	  and fl_pgiro = 'Y') loop
         return getContoEpPgiro(aDocTst,aDoc);
    end loop;

	if aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO then
	 declare
      aCoriLoc contributo_ritenuta%rowtype;
	  aCompenso compenso%rowtype;
	  aCdClassificazioneCori char(2);
     begin
	  begin
	   select * into aCompenso from compenso where
	       cd_cds = aCori.cd_cds
	   and esercizio = aCori.esercizio
	   and cd_unita_organizzativa = aCori.cd_unita_organizzativa
	   and pg_compenso = aCori.pg_numero_documento;
	  exception when NO_DATA_FOUND then
	   IBMERR001.RAISE_ERR_GENERICO('Compenso non trovato:'||aCori.pg_numero_documento||' uo:'||aCori.cd_unita_organizzativa||' es:'||aCori.esercizio);
	  end;
	  begin
	   select * into aCoriLoc from contributo_ritenuta where
	       cd_cds = aCori.cd_cds
	   and esercizio = aCori.esercizio
	   and cd_unita_organizzativa = aCori.cd_unita_organizzativa
	   and pg_compenso = aCori.pg_numero_documento
	   and cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
	   and ti_ente_percipiente = aCori.ti_ente_percepiente;
      exception when NO_DATA_FOUND then
	   IBMERR001.RAISE_ERR_GENERICO('Contributo ritenuta non trovato:'||aCori.cd_contributo_ritenuta);
      end;
	  -- Se la tipologia di contributo ritenuta è IVA o RIVALSA il conto ritornato è quello di costo principale del compenso
      aCdClassificazioneCori:=CNRCTB545.getTipoCoriDaRigaCompenso(aCoriLoc);
      IF (   aCdClassificazioneCori = CNRCTB545.isCoriIva AND aCompenso.TI_ISTITUZ_COMMERC = CNRCTB100.TI_ISTITUZIONALE
	      OR aCdClassificazioneCori = CNRCTB545.isCoriRivalsa
		 ) THEN
       for aDoc in (select * from v_doc_amm_coge_riga where
            cd_tipo_documento = aDocTst.cd_tipo_documento
        and cd_cds = aDocTst.cd_cds
        and cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
        and esercizio = aDocTst.esercizio
        and pg_numero_documento = aDocTst.pg_numero_documento) loop
         return trovaContoEp(aDocTst,aDoc);
       end loop;
	  END IF;
	 end;
	end if;

Dbms_Output.PUT_LINE ('PASSA aEffCori:=getAssCoriEp(aDocTst,aCori)');
    aEffCori:=getAssCoriEp(aDocTst,aCori);
    begin
	 aContoEp:=CNRCTB002.getVoceEp(aEffCori.esercizio, aEffCori.cd_voce_ep);
Dbms_Output.PUT_LINE ('E RECUPERA '||aContoEp.CD_VOCE_EP);
	exception when OTHERS then
     IBMERR001.RAISE_ERR_GENERICO('Conto economico: '||aEffCori.cd_voce_ep||' associato a CORI:'||aCori.cd_contributo_ritenuta||'non trovato');
	end;
  return aContoEp;
 end;
 function trovaContoContrEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype) return voce_ep%rowtype is
  aEffCori ass_tipo_cori_voce_ep%rowtype;
  aContoEp voce_ep%rowtype;
  aSez char(1);
 begin
    aEffCori:=getAssCoriEp(aDocTst,aCori);
    begin
	 aContoEp:=CNRCTB002.getVoceEp(aEffCori.esercizio, aEffCori.cd_voce_ep_contr);
	exception when OTHERS then
     IBMERR001.RAISE_ERR_GENERICO('Conto economico di contr.: '||aEffCori.cd_voce_ep_contr||' associato a CORI:'||aCori.cd_contributo_ritenuta||'non trovato');
	end;
  return aContoEp;
 end;
 function getSezioneCoriComp(aCori contributo_ritenuta%rowtype) return char is
  aSezione char(1);
 begin
  aSezione:=CNRCTB100.getSezioneEconomicaCori(CNRCTB100.TI_COMPENSO);
  if aCori.ammontare < 0 then
   aSezione:=CNRCTB200.getSezioneOpposta(aSezione);
  end if;
  return aSezione;
 end;

 function getSezione(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype) return char is
  aSezione char(1);
 begin
  aSezione:=CNRCTB100.getSezioneEconomicaCori(aDocTst.cd_tipo_documento);

  -- gestione particolare per nota di creadito

  If aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA And aDocTst.ti_fattura = CNRCTB100.TI_FATT_NOTA_C or
     aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA And aDocTst.ti_fattura = CNRCTB100.TI_FATT_NOTA_C
  then
   aSezione:=CNRCTB200.getSezioneOpposta(aSezione);
  end if;

  if aCori.ammontare < 0 then
   aSezione:=CNRCTB200.getSezioneOpposta(aSezione);
  end if;

  return aSezione;
 end;

 -- =======================================
 --
 -- CONTABILIZZAZIONE PARTE PRINCIPALE
 --
 -- =======================================

 procedure buildMovPrinc(
  aCdCds varchar2,
  aEs number,
  aCdUo varchar2,
  aContoEp voce_ep%rowtype,
  aImporto number,
  aSezione char,
  aDaCompCoge date,
  aACompCoge date,
  aCdTerzo number,
  aListaMovimenti IN OUT CNRCTB200.movimentiList,
  aUser varchar2,
  aTSnow date
 ) is
 begin
  buildMovPrinc(
   aCdCds,
   aEs,
   aCdUo,
   aContoEp,
   aImporto,
   aSezione,
   aDaCompCoge,
   aACompCoge,
   aCdTerzo,
   CNRCTB100.TI_ISTITUZIONALE,
   aListaMovimenti,
   aUser,
   aTSnow);
 end;

 function getSezioneChiusuraScr(aListaMovimenti IN CNRCTB200.movimentiList) return char is
  aTotDare number(15,2):=0;
  aTotAvere number(15,2):=0;
 begin
      for i in 1 .. aListaMovimenti.count loop
       if aListaMovimenti(i).sezione = CNRCTB200.IS_DARE then
        aTotDare:=aTotDare+aListaMovimenti(i).im_movimento;
	   else
        aTotAvere:=aTotAvere+aListaMovimenti(i).im_movimento;
	   end if;
	  end loop;
	  if aTotDare-aTotAvere > 0 then
       return CNRCTB200.IS_AVERE;
      elsif aTotDare-aTotAvere < 0 then
       return CNRCTB200.IS_DARE;
	  else
       return null;
	  end if;
 end;

 procedure buildChiusuraScrittura(
  aCdCds varchar2,
  aEs number,
  aCdUo varchar2,
  aContoEp voce_ep%rowtype,
  aDaCompCoge date,
  aACompCoge date,
  aCdTerzo number,
  aTiIstituzCommerc char,
  aListaMovimenti IN OUT CNRCTB200.movimentiList,
  aUser varchar2,
  aTSnow date
 ) is
  aTotDare number(15,2):=0;
  aTotAvere number(15,2):=0;

  aTotDare_I number(15,2):=0;
  aTotAvere_I number(15,2):=0;

  aTotDare_C number(15,2):=0;
  aTotAvere_C number(15,2):=0;

  aMovimento movimento_coge%rowtype;

Begin

Dbms_Output.PUT_LINE ('entro con '||aTiIstituzCommerc||' count '||aListaMovimenti.count||' voce '||acontoEp.cd_voce_ep);

If aTiIstituzCommerc In ('C', 'I') Then

   -- Generazione movimento di default
   aMovimento := null;
Dbms_Output.PUT_LINE ('inizio loop ');
   For i in 1 .. aListaMovimenti.count loop

Dbms_Output.PUT_LINE ('riga loop '||i);

--Dbms_Output.PUT_LINE ('sez '||aListaMovimenti(i).sezione);

       if aListaMovimenti(i).sezione = CNRCTB200.IS_DARE Then
Dbms_Output.PUT_LINE ('somma dare');
        aTotDare:=aTotDare+aListaMovimenti(i).im_movimento;
      else
        aTotAvere:=aTotAvere+aListaMovimenti(i).im_movimento;
      end if;
   End loop;

   if aTotDare-aTotAvere > 0 then
Dbms_Output.PUT_LINE ('B1 '||aContoEp.CD_VOCE_EP);
       buildMovPrinc(aCdCds,aEs,aCdUo,aContoEp,aTotDare-aTotAvere,CNRCTB200.IS_AVERE,aDaCompCoge,aACompCoge,aCdTerzo,aTiIstituzCommerc,aListaMovimenti,aUser,aTSnow);
   elsif aTotdare-aTotAvere < 0 then
Dbms_Output.PUT_LINE ('B2 '||aContoEp.CD_VOCE_EP);
       buildMovPrinc(aCdCds,aEs,aCdUo,aContoEp,aTotAvere-aTotDare,CNRCTB200.IS_DARE,aDaCompCoge,aACompCoge,aCdTerzo,aTiIstituzCommerc,aListaMovimenti,aUser,aTSnow);
   else
       return;
   end if;

 Elsif aTiIstituzCommerc = 'P' Then   -- PROMISCUO

   -- Generazione movimento di default
   aMovimento := Null;
   for i in 1 .. aListaMovimenti.count loop

     If aListaMovimenti(i).sezione = CNRCTB200.IS_DARE Then
       If aListaMovimenti(i).TI_ISTITUZ_COMMERC = 'I' Then
         aTotDare_I := Nvl(aTotDare_I, 0) + aListaMovimenti(i).im_movimento;
       Elsif aListaMovimenti(i).TI_ISTITUZ_COMMERC = 'C' Then
         aTotDare_C := Nvl(aTotDare_C, 0) + aListaMovimenti(i).im_movimento;
       End If;
     else
       If aListaMovimenti(i).TI_ISTITUZ_COMMERC = 'I' Then
        aTotAvere_I := Nvl(aTotAvere_I, 0) + aListaMovimenti(i).im_movimento;
       Elsif aListaMovimenti(i).TI_ISTITUZ_COMMERC = 'C' Then
        aTotAvere_C := Nvl(aTotAvere_C, 0) + aListaMovimenti(i).im_movimento;
       End If;
     End if;

   end loop;

   if aTotDare_I - aTotAvere_I > 0 then
      buildMovPrinc(aCdCds,aEs,aCdUo,aContoEp,aTotDare_I-aTotAvere_I,CNRCTB200.IS_AVERE,aDaCompCoge,aACompCoge,aCdTerzo,CNRCTB100.TI_ISTITUZIONALE,aListaMovimenti,aUser,aTSnow);
   elsif aTotdare_I - aTotAvere_I < 0 then
      buildMovPrinc(aCdCds,aEs,aCdUo,aContoEp,aTotAvere_I-aTotDare_I,CNRCTB200.IS_DARE,aDaCompCoge,aACompCoge,aCdTerzo,CNRCTB100.TI_ISTITUZIONALE,aListaMovimenti,aUser,aTSnow);
   end if;

   if aTotDare_C - aTotAvere_C > 0 then
      buildMovPrinc(aCdCds,aEs,aCdUo,aContoEp,aTotDare_C-aTotAvere_C,CNRCTB200.IS_AVERE,aDaCompCoge,aACompCoge,aCdTerzo,CNRCTB100.TI_COMMERCIALE,aListaMovimenti,aUser,aTSnow);
   elsif aTotdare_C - aTotAvere_C < 0 then
      buildMovPrinc(aCdCds,aEs,aCdUo,aContoEp,aTotAvere_C-aTotDare_C,CNRCTB200.IS_DARE,aDaCompCoge,aACompCoge,aCdTerzo,CNRCTB100.TI_COMMERCIALE,aListaMovimenti,aUser,aTSnow);
   end if;

 End If;  -- DEL CASO PROMISCUO

 end;

 procedure buildMovPrinc(
  aCdCds varchar2,
  aEs number,
  aCdUo varchar2,
  aContoEp voce_ep%rowtype,
  aImporto number,
  aSezione char,
  aDaCompCoge date,
  aACompCoge date,
  aCdTerzo number,
  aTiIstituzCommerc char,
  aListaMovimenti IN OUT CNRCTB200.movimentiList,
  aUser varchar2,
  aTSnow date
 ) is
  aMovimento movimento_coge%rowtype;
 begin
      -- Generazione movimento di default
	  -- Condizione di comressione dei movimenti: il tipo istituz/commerciale non
	  -- deve essere diverso
      aMovimento:=null;
      for i in 1 .. aListaMovimenti.count loop
       if (
	            aListaMovimenti(i).cd_voce_ep = aContoEp.cd_voce_ep
	        and aListaMovimenti(i).sezione = aSezione
	        and aListaMovimenti(i).dt_da_competenza_coge = aDaCompCoge
	        and aListaMovimenti(i).dt_a_competenza_coge = aACompCoge
	        and aListaMovimenti(i).ti_istituz_commerc = aTiIstituzCommerc
		  ) then
		aListaMovimenti(i).im_movimento:=aListaMovimenti(i).im_movimento + aImporto;
		return;
	   end if;
	  end loop;
      aMovimento.CD_CDS:=aCdCds;
      aMovimento.ESERCIZIO:=aEs;
      aMovimento.CD_UNITA_ORGANIZZATIVA:=aCdUo;
      aMovimento.PG_SCRITTURA:=null;
      aMovimento.PG_MOVIMENTO:=null;

      aMovimento.CD_VOCE_EP:=aContoEp.cd_voce_ep;
      aMovimento.SEZIONE:=aSezione;
      aMovimento.IM_MOVIMENTO:=aImporto;

      aMovimento.TI_ISTITUZ_COMMERC:=aTiIstituzCommerc;

      aMovimento.CD_TERZO:=aCdTerzo;
      aMovimento.DT_DA_COMPETENZA_COGE:=aDaCompCoge;
      aMovimento.DT_A_COMPETENZA_COGE:=aACompCoge;
      aMovimento.STATO:=CNRCTB200.STATO_DEFINITIVO;
      dbms_output.put_line('mov '||aContoEp.cd_voce_ep);
      aListaMovimenti(aListaMovimenti.COUNT+1):=aMovimento;
 end;

Function trovaContoEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype) return voce_ep%rowtype is
  aAss ass_ev_voceep%rowtype;
  aVoceEp voce_ep%rowtype;
  aVoceSA varchar2(45);
Begin

-- Modifica del 25/08/2003 -> se la competenza è in esercizio precedente e l'esercizio attuale è il primo dell'applicazione
-- Il conto di costo/ricavo è SP Iniziale o Fatture da emettere
If CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) And aDocTst.esercizio = CNRCTB008.ESERCIZIO_PARTENZA Then
   -- Aggiunta gestione per fatture passive il 19/02/2004
   -- Solo per la fattura attiva (e passiva) si usa il conto speciale delle fatture da emettere (ricevere)
     If aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA then -- and aDocTst.ti_fattura = CNRCTB100.TI_FATT_FATTURA then
      Return CNRCTB002.GETVOCEEPFATTUREDAEMETTERE(aDocTst.esercizio);
     Elsif aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA then -- and aDocTst.ti_fattura = CNRCTB100.TI_FATT_FATTURA then
      Return CNRCTB002.GETVOCEEPFATTUREDARICEVERE(aDocTst.esercizio);
     Else -- Per gli altri documenti ritorna il conto di Stato Patrimoniale Iniziale
      Return CNRCTB002.GETVOCEEPSTATOPATRINIZIALE(aDocTst.esercizio);
     End if;
End if;

If aDoc.cd_tipo_documento in (CNRCTB100.TI_ANTICIPO, CNRCTB100.TI_RIMBORSO) then
   aVoceEp:= CNRCTB002.GETVOCEEPANTICIPOMISSIONE(aDocTst.esercizio);

   if aVoceEp.cd_voce_ep is  null then
    -- Recupero conto associato di default
			Begin
			   select * into aAss from ass_ev_voceep where
			        esercizio = aDoc.esercizio
			    and ti_appartenenza = aDoc.ti_appartenenza_ev
			    and ti_gestione = aDoc.ti_gestione_ev
			    and cd_elemento_voce = aDoc.cd_elemento_voce_ev;
			Exception when NO_DATA_FOUND then
			   IBMERR001.RAISE_ERR_GENERICO('Associazione tra voce del piano finanziaria (gest.'||aDoc.ti_gestione_ev||' app.'||aDoc.ti_appartenenza_ev||' n.:'||aDoc.cd_elemento_voce_ev||') ed economica non trovata');
			end;
  		return CNRCTB002.GETVOCEEP(aDoc.esercizio,aAss.cd_voce_ep);
	else
   return aVoceEp;
	End if;
end if;
-- se contabilizzo una nota di credito allora:
--  1. se la voce finanziaria è patrimoniale movimento sempre il conto di attivo ad essa legato
--  2. altrimenti, se la competenza economica del costo/ricavo della fattura a cui è legata
--     cade in un esercizio precedente chiuso allora anzichè movimentare il costo/ricavo in rettifica deve movimentare
--     le insussistenze passive/attive

If aDocTst.TI_FATTURA = 'C' Then -- se è una nota

----------------------- VERIFICO SE LA VOCE E' PATRIMONIALE (associata a categoria) --------------------------

   Declare
      ese       elemento_voce.esercizio%Type;
      gest      elemento_voce.ti_gestione%Type;
      app       elemento_voce.ti_appartenenza%Type;
      elvoce    elemento_voce.cd_elemento_voce%Type;

   Begin
     Select Distinct ESERCIZIO_EV, TI_APPARTENENZA_EV, TI_GESTIONE_EV, CD_ELEMENTO_VOCE_EV
     Into   ese, app, gest, elvoce
     From   V_DOC_AMM_COGE_RIGA
     Where  CD_TIPO_DOCUMENTO      = aDocTst.CD_TIPO_DOCUMENTO       And
            CD_CDS                 = aDocTst.CD_CDS                  And
            CD_UNITA_ORGANIZZATIVA = aDocTst.CD_UNITA_ORGANIZZATIVA  And
            ESERCIZIO              = aDocTst.ESERCIZIO               And
            PG_NUMERO_DOCUMENTO    = aDocTst.PG_NUMERO_DOCUMENTO;

     If ISVOCECOLLCATEGORIA (ese, gest, app, elvoce) Then
------ nel caso CERCO IL CONTO ASSOCIATO ALLA VOCE (E LO PASSO ALLA SCRITTURA AL POSTO DI UTILE O PERDITA SU CAMBIO)
            Select VOCE_EP.*
            Into   aVoceEp
            From   ass_ev_voceep, VOCE_EP
            Where  ass_ev_voceep.esercizio = ese And
                   ass_ev_voceep.ti_appartenenza = app And
                   ass_ev_voceep.ti_gestione = gest  And
                   ass_ev_voceep.cd_elemento_voce = elvoce And
                   ass_ev_voceep.ESERCIZIO  = VOCE_EP.ESERCIZIO And
                   ass_ev_voceep.CD_VOCE_EP = VOCE_EP.CD_VOCE_EP;
            Return aVoceEp;
     End If;
   Exception
     When Others Then Null;
   End;


   If isCompFattEsPreChiusoADTREG (aDocTst, aDoc) Then  -- e la fattura a cui è legata ha competenza in un esercizio chiuso
      If aDoc.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA Then
        Return CNRCTB002.getVoceEpInsussPassive(aDocTst.esercizio);
      Elsif aDoc.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA Then
        Return CNRCTB002.getVoceEpInsussAttive(aDocTst.esercizio);
      End If;
   End If;
End If;  -- altrimenti si comporta come prima

-- Gestione sconto/abbuono su fattura passiva
-- se il documento reca l'indicazione di codice bene servizio
If aDoc.cd_bene_servizio is not null then

   -- sconto / abbuono ricavo
   If CNRCTB100.isBeneServScontoAbbuono(aDoc.cd_bene_servizio) and (aDoc.im_imponibile < 0) then
    aVoceEp:= CNRCTB002.getVoceEpScontoAbbRicavo(aDocTst.esercizio);
   End if;

   -- sconto / abbuono costo
   If CNRCTB100.isBeneServScontoAbbuono(aDoc.cd_bene_servizio) and (aDoc.im_imponibile > 0) then
    aVoceEp:= CNRCTB002.getVoceEpScontoAbbCosto(aDocTst.esercizio);
   End if;

End if;
 IF AVOCEEP.CD_VOCE_EP IS NULL THEN

-- Recupero conto associato di default
Begin
   select * into aAss from ass_ev_voceep where
        esercizio = aDoc.esercizio
    and ti_appartenenza = aDoc.ti_appartenenza_ev
    and ti_gestione = aDoc.ti_gestione_ev
    and cd_elemento_voce = aDoc.cd_elemento_voce_ev;
Exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Associazione tra voce del piano finanziaria (gest.'||aDoc.ti_gestione_ev||' app.'||aDoc.ti_appartenenza_ev||' n.:'||aDoc.cd_elemento_voce_ev||') ed economica non trovata');
  end;
  return CNRCTB002.GETVOCEEP(aDoc.esercizio,aAss.cd_voce_ep);
  END IF;
End;

 function trovaContoContrEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number,ACdElementoVoce varchar2) return voce_ep%rowtype is
  aAnag v_anagrafico_terzo%rowtype;
  aAssDaUsare ass_anag_voce_ep%rowtype;
  aVoceEp voce_ep%rowtype;
  aDescTerzo varchar2(1000);
 begin
  return trovaContoAnag(aDocTst.esercizio, aCdTerzo, aDocTst.cd_tipo_documento, aDocTst.ti_fattura,ACdElementoVoce );
 end;

 function trovaContoAnag(aEs number, aCdTerzo number, aTipoDoc varchar2, aTiFattura char,ACdElementoVoce varchar2) return voce_ep%rowtype is
  aAnag v_anagrafico_terzo%rowtype;
  aAssDaUsare ass_anag_voce_ep%rowtype;
  aVoceEp voce_ep%rowtype;
  aDescTerzo varchar2(1000);
  aTipoTerzo char(1);
 	recParametriCNR PARAMETRI_CNR%Rowtype;
  ass ass_ev_voceep%rowtype;
  aContoEp voce_ep%rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  -- Trovo l'entita anagrafica completa
  begin
   select * into aAnag from v_anagrafico_terzo where
    cd_terzo = aCdTerzo;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Terzo non trovato: '||aCdTerzo);
  end;
  -- Determino il tipo terzo da utilizzare in tabella di associazione anagrafica-conto
  if aTipoDoc is not null then
   -- La sezione economica
   if CNRCTB100.GETSEZIONEECONOMICA(aTipoDoc) = CNRCTB200.IS_AVERE then
		 aTipoTerzo:='D'; -- Documenti attivi il terzo è un debitore
	 else
     aTipoTerzo:='C'; -- Documenti passivi il terzo è un creditore
   end if;
  else
   IBMERR001.RAISE_ERR_GENERICO('Errore di recupero conto numerario debito/credito: sezione principale del documento amministrativo non specificata');
  end if;

if(recParametriCNR.fl_nuovo_pdg ='Y' and ACdElementoVoce is not null) then
ass:=null;
dbms_output.put_line ('Voce in :'||aEs||'/'||ACdElementoVoce||' '||aTipoTerzo||' '||aTipoDoc);
   begin
    select * into ass
    from ass_ev_voceep
    where
    	  esercizio = aEs
       	and ((ti_gestione = 'E' and (aTipoTerzo='D' or (aTiFattura is not null and aTiFattura='C' ))) or
       		   (ti_gestione = 'S' and (aTipoTerzo='C' or (aTiFattura is not null and aTiFattura='C' ))))
       	and CD_ELEMENTO_VOCE = ACdElementoVoce;

  exception when NO_DATA_FOUND then
   	IBMERR001.RAISE_ERR_GENERICO('Associazione ev/coep non trovata per voce:'||aEs||'/'||ACdElementoVoce);
   					when too_many_rows then
   	IBMERR001.RAISE_ERR_GENERICO('Errore di recupero associazione multipla ev/coep  per :'||aEs||'/'||ACdElementoVoce||' '||aTipoTerzo||' '||aTipoDoc||' '||ass.CD_VOCE_EP_CONTR);
  end;
  if (ass.CD_VOCE_EP_CONTR is null) then
 			IBMERR001.RAISE_ERR_GENERICO('Contropartita mancante in Associazione ev/coep:'||aEs||'/'||ACdElementoVoce);
 	else
  		aContoEp:=CNRCTB002.getVoceEp(ass.esercizio, ass.CD_VOCE_EP_CONTR);
  end if;
  return aContoEp;
  elsif (recParametriCNR.fl_nuovo_pdg ='Y' and ACdElementoVoce is null) then
  	dbms_output.put_line ('Attenzione voce null - nuova gestione');
else
  -- Con l'entita anagrafica cerco in ass_anag_voce_ep
  for aAssAnagVoce in (select * from ass_anag_voce_ep where
        esercizio = aEs
    and (ti_terzo = aTipoTerzo)
    and (italiano_estero = aAnag.ti_italiano_estero or italiano_estero = '*')
    and (ti_entita = aAnag.ti_entita or ti_entita = '*')
    and (
	        aAnag.ti_entita != TI_ANAG_ENT_GIU
	     or aAnag.ti_entita = TI_ANAG_ENT_GIU and ente_altro = aAnag.ti_entita_giuridica
	     or ente_altro = '*'
		)
    and (cd_classific_anag = aAnag.cd_classific_anag or cd_classific_anag = '*')
	order by
	  ti_terzo desc,
	  italiano_estero desc,
	  ti_entita desc,
	  ente_altro desc,
	  cd_classific_anag desc
   ) loop
    -- Piglio la prima ricorrenza valida ed esco dal loop
    aAssDaUsare:=aAssAnagVoce;
	goto nextStep;
  end loop;
  <<nextStep>>
  if aAssDaUsare.cd_voce_ep is null then
   aDescTerzo:=
        ' Tipo terzo: '||nvl(aTipoTerzo,'*')
	 || ' Italiano estero: '||nvl(aAnag.ti_italiano_estero,'*')
	 || ' Tipo entita: '||nvl(aAnag.ti_entita,'*')
	 || ' Ente/Altro: '||nvl(aAnag.ti_entita_giuridica,'*')
	 || ' Cass. anagrafica: '||nvl(aAnag.cd_classific_anag,'*');
   IBMERR001.RAISE_ERR_GENERICO('Conto di contropartita non trovato per terzo: '||aDescTerzo);
  end if;
  return CNRCTB002.getVoceEp(aEs, aAssDaUsare.cd_voce_ep);
  end if;
 end;

 function trovaContoAnag(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number,ACdElementoVoce varchar2) return voce_ep%rowtype is
 begin
  return trovaContoAnag(aDocTst.esercizio, aCdTerzo, aDocTst.cd_tipo_documento, aDocTst.ti_fattura,ACdElementoVoce);
 end;

 function trovaContoAnag(aDocUltRiga V_DOC_ULT_COGE_RIGA%rowtype, aCdTerzo number,ACdElementoVoce varchar2) return voce_ep%rowtype is
  aDocAmm V_DOC_AMM_COGE_TSTA%rowtype;
 begin
  if aDocUltRiga.cd_tipo_doc in (CNRCTB100.TI_FATTURA_ATTIVA,CNRCTB100.TI_FATTURA_PASSIVA) then
   begin
    select * into aDocAmm from V_DOC_AMM_COGE_TSTA where
                     cd_cds = aDocUltRiga.cd_cds_doc
  			    and esercizio = aDocUltRiga.esercizio_doc
  				and cd_tipo_documento = aDocUltRiga.cd_tipo_doc
 				and cd_unita_organizzativa = aDocUltRiga.cd_uo_doc
 				and pg_numero_documento = aDocUltRiga.pg_numero_doc;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento amministrativo non trovato');
   end;
   return trovaContoAnag(aDocUltRiga.esercizio, aCdTerzo, aDocUltRiga.cd_tipo_doc, aDocAmm.ti_fattura,ACdElementoVoce);
  else
   return trovaContoAnag(aDocUltRiga.esercizio, aCdTerzo, aDocUltRiga.cd_tipo_doc, null,ACdElementoVoce);
  end if;
 end;

 -- =======================================
 --
 -- CONTABILIZZAZIONE ULTIMA
 --
 -- =======================================

-- Componenti scrittura ultima o singola

 function trovaContoContrEp(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aDoc V_DOC_ULT_COGE_RIGA%rowtype) return voce_ep%rowtype is
 begin
  if aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_REV and aDocTst.ti_man_rev = CNRCTB038.TI_REV_REG Then
-- 25.05.2006 FRANCA CAMPANALE (MODIFICA AD UNA PRECEDENTE RICHIESTA): PER CONTABILIZZARE UNA REVERSALE DI REGOLARIZZAZIORE
-- DEVO ANDARE A VEDERE IL MANDATO COLLEGATO: SE E' SU PDIGIRO DEVO USARE I DEBITI, SE E' SU VOCI ORDINARIE
-- DEVO USARE LE INSUSSISTENZE

/*
    If ISMANDATOCOLLSUPGIRO (aDoc) Then
        return CNRCTB002.getVoceCONTR_REV_REG_PGIRO(aDocTst.esercizio);
    Else
        return CNRCTB002.getVoceEpInsussistenzeCredito(aDocTst.esercizio);
    End If;
*/

-- 29.05.2006 (FRANCA E PIERO) NON E' PIU' COSI'. LA CONTABILIZZAZIONE DELLE REVERSALI DI REGOLARIZZAZIONE DEVE MOVIMENTARE SEMPRE
--            I DEBITI VERSO FORNITORI.

--    If ISMANDATOCOLLSUPGIRO (aDoc) Then

			if (aDoc.esercizio_doc < aDoc.esercizio) then
        	return CNRCTB002.getVoceCONTR_REV_REG_PGIRO(aDocTst.esercizio);
       else
       declare
 					ass ass_ev_voceep%rowtype;
         begin
          select * into ass
    			from ass_ev_voceep
    			where
    	  	esercizio = aDocTst.esercizio
	   			and TI_APPARTENENZA = nvl(null,TI_APPARTENENZA)
       		and ti_gestione = 'E'
       		and CD_ELEMENTO_VOCE = aDoc.CD_ELEMENTO_VOCE;
       			return CNRCTB002.getVoceEp(ass.esercizio, ass.CD_VOCE_EP);
  			exception when NO_DATA_FOUND then
   				IBMERR001.RAISE_ERR_GENERICO('Associazione ev/coep non trovata per voce:'||aDocTst.esercizio||'/'||' E '||'/'|| aDoc.CD_ELEMENTO_VOCE);
  			end;
    end if;
--    Else
--        return CNRCTB002.getVoceEpInsussistenzeCredito(aDocTst.esercizio);
--    End If;
    dbms_output.put_line('ci arrivo con '||aDoc.CD_ELEMENTO_VOCE);
    if (aDoc.CD_ELEMENTO_VOCE is not null) then
      RETURN trovaContoContrEp(aDocTst.esercizio,NULL,'E',aDoc.cd_elemento_voce,null);
    end if;

  end if;
  dbms_output.put_line('ci passo '||aDoc.CD_ELEMENTO_VOCE);

  if aDoc.stato_pagamento_fondo_eco in (CNRCTB100.STATO_ASS_PFONDOECO, CNRCTB100.STATO_REG_PFONDOECO) then
   return CNRCTB002.getVoceEpCassa(aDocTst.esercizio);
  end if;
   if aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_MAN and aDocTst.ti_man_rev = CNRCTB038.TI_MAN_REG Then
    if (aDoc.CD_ELEMENTO_VOCE is not null) then 
      RETURN trovaContoContrEp(aDocTst.esercizio,NULL,'S',aDoc.cd_elemento_voce,null);
    end if;
  end if;
  if aDocTst.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
   return CNRCTB002.getVoceEpBanca(aDocTst.esercizio);
  else
   return CNRCTB002.getVoceEpBancaCds(aDocTst.esercizio);
  end if;

 end;

 function getTipoScrittura(aDoc V_DOC_ULT_COGE_RIGA%rowtype) return char is
 begin
  if aDoc.cd_tipo_doc = CNRCTB100.TI_GEN_REINTEGRO_FONDO then
   return CNRCTB200.TI_SCRITTURA_SINGOLA;
  end if;
  return CNRCTB200.TI_SCRITTURA_ULTIMA;
 end;

 function buildScrPEP(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype is
  aScrittura scrittura_partita_doppia%rowtype;
 begin
   aScrittura:=null;
   aScrittura.CD_CDS:=aDocTst.cd_cds_origine;
   aScrittura.ESERCIZIO:=aDocTst.esercizio;
   aScrittura.CD_UNITA_ORGANIZZATIVA:=aDocTst.cd_uo_origine;
   aScrittura.PG_SCRITTURA:=null;
   aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_DOCUMENTO_AMM;
   aScrittura.CD_TIPO_DOCUMENTO:=aDocTst.cd_tipo_documento;
   aScrittura.CD_CDS_DOCUMENTO:=aDocTst.cd_cds;
   aScrittura.CD_UO_DOCUMENTO:=aDocTst.cd_unita_organizzativa;
   aScrittura.PG_NUMERO_DOCUMENTO:=aDocTst.pg_numero_documento;
   aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura

-- SF 26.09.2007
-- ANOMALIA: TALVOLTA LA DATA DELLA SCRITTURA IN PARTITA DOPPIA E' ESTERNA ALL'ESERCIZIO A CUI SI RIFERISCE
-- SOLUZIONE: ANCHE SE IL DOC. AMMINISTRATIVO SI RICONTABILIZZA A SEGUITO DI UNA MODIFICA INTERVENUTA SUCCESSIVAMENTE
--            ALLA REGISTRAZIONE DEL DOCUMENTO LA SCRITTURA PORTA SEMPRE LA DATA DI REGISTRAZIONE DEL DOCUMENTO,
--            CHE NON PUO' ESSERE MAI ESTERNA ALL'ESERCIZIO

--   If aDocTst.stato_coge = CNRCTB100.STATO_COEP_DA_RIP Then
--    aScrittura.DT_CONTABILIZZAZIONE := trunc(aDocTst.duva);
--   else
    aScrittura.DT_CONTABILIZZAZIONE := trunc(aDocTst.dt_registrazione);
--   end if;

   aScrittura.DT_PAGAMENTO:=null;
   aScrittura.DT_CANCELLAZIONE:=null;
   aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_PRIMA; -- Si tratta del tipo di scrittura prima o ultima
   aScrittura.CD_TERZO:=aCdTerzo;
   aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aScrittura.CD_DIVISA:=aDocTst.cd_divisa;
   aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non è chiaro
   aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE
   aScrittura.DS_SCRITTURA := 'Contabilizzazione '||aDocTst.CD_TIPO_DOCUMENTO||': '||aDocTst.CD_CDS||'/'||
                              aDocTst.CD_UNITA_ORGANIZZATIVA||'/'||aDocTst.ESERCIZIO||'/'||
                              aDocTst.PG_NUMERO_DOCUMENTO;    --null; -- chi lo valorizza e come non è chiaro
   aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
   aScrittura.ATTIVA:='Y';
   aScrittura.ESERCIZIO_DOCUMENTO_AMM:=aDocTst.esercizio;
   aScrittura.DACR:=aTSNow;
   aScrittura.UTCR:=aUser;
   return aScrittura;
 end;

 function buildScrPEPAnnull(aEs number, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype is
  aScrittura scrittura_partita_doppia%rowtype;
 begin
   aScrittura:=null;
   aScrittura.CD_CDS:=aDocTst.cd_cds_origine;
   aScrittura.ESERCIZIO:=aEs;
   aScrittura.cd_causale_coge:=CNRCTB200.CAU_ANNULL_ES_CHIUSO;
   aScrittura.CD_UNITA_ORGANIZZATIVA:=aDocTst.cd_uo_origine;
   aScrittura.PG_SCRITTURA:=null;
   aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_DOCUMENTO_AMM;
   aScrittura.CD_TIPO_DOCUMENTO:=aDocTst.cd_tipo_documento;
   aScrittura.CD_CDS_DOCUMENTO:=aDocTst.cd_cds;
   aScrittura.CD_UO_DOCUMENTO:=aDocTst.cd_unita_organizzativa;
   aScrittura.PG_NUMERO_DOCUMENTO:=aDocTst.pg_numero_documento;
   aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura
   aScrittura.DT_CONTABILIZZAZIONE:=to_date('0101'||(aEs),'DDMMYYYY');
   aScrittura.DT_PAGAMENTO:=null;
   aScrittura.DT_CANCELLAZIONE:=null;
   aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_PRIMA; -- Si tratta del tipo di scrittura prima o ultima
   aScrittura.CD_TERZO:=aCdTerzo;
   aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aScrittura.CD_DIVISA:=aDocTst.cd_divisa;
   aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non è chiaro
   aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE
   aScrittura.DS_SCRITTURA := 'Annullamento '||aDocTst.CD_TIPO_DOCUMENTO||': '||aDocTst.CD_CDS||'/'||
                              aDocTst.CD_UNITA_ORGANIZZATIVA||'/'||aDocTst.ESERCIZIO||'/'||
                              aDocTst.PG_NUMERO_DOCUMENTO;    --null; -- chi lo valorizza e come non è chiaro
   aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
   aScrittura.ATTIVA:='Y';
   aScrittura.ESERCIZIO_DOCUMENTO_AMM:=aDocTst.esercizio;
   aScrittura.DACR:=aTSNow;
   aScrittura.UTCR:=aUser;
   return aScrittura;
 end;

 function buildScrUEP(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aCdTerzo number, aTipoScrittura char, aCdCausale varchar2, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype is
  aScrittura scrittura_partita_doppia%rowtype;
 begin
   aScrittura:=null;
   aScrittura.CD_CDS:=aDocTst.cd_cds_origine;
   aScrittura.ESERCIZIO:=aDocTst.esercizio;
   aScrittura.CD_UNITA_ORGANIZZATIVA:=aDocTst.cd_uo_origine;
   aScrittura.PG_SCRITTURA:=null;
   aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_DOCUMENTO_CONT;
   aScrittura.CD_CAUSALE_COGE:=aCdCausale;
   aScrittura.CD_CDS_DOCUMENTO:=aDocTst.cd_cds;
   aScrittura.CD_UO_DOCUMENTO:=aDocTst.cd_unita_organizzativa;
   aScrittura.CD_TIPO_DOCUMENTO:=aDocTst.cd_tipo_documento_cont;
   aScrittura.PG_NUMERO_DOCUMENTO:=aDocTst.pg_documento_cont;
   aScrittura.IM_SCRITTURA:=null; -- Impostato a quadratura dei movimenti in realizzazione della scrittura
-- 09.08.2006 data della scrittura = data emissione mandato/reversale
   aScrittura.DT_CONTABILIZZAZIONE := Nvl(aDocTst.DT_EMISSIONE_DOCUMENTO_CONT, Trunc(aTSNow));
   aScrittura.DT_PAGAMENTO:=aDocTst.dt_esito_documento_cont;
   aScrittura.DT_CANCELLAZIONE:=null;
   aScrittura.TI_SCRITTURA:=aTipoScrittura;
   aScrittura.CD_TERZO:=aCdTerzo;
   aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aScrittura.CD_DIVISA:=null;
   aScrittura.COSTO_PLURIENNALE:=null;
   aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE
   aScrittura.DS_SCRITTURA := 'Contabilizzazione '||aDocTst.CD_TIPO_DOCUMENTO_CONT||': '||aDocTst.CD_CDS||'/'||
                              aDocTst.CD_UNITA_ORGANIZZATIVA||'/'||aDocTst.ESERCIZIO||'/'||
                              aDocTst.PG_DOCUMENTO_CONT;    --null; -- chi lo valorizza e come non è chiaro
   aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
   aScrittura.ATTIVA:='Y';
   aScrittura.ESERCIZIO_DOCUMENTO_AMM:=null;
   aScrittura.DACR:=aTSNow;
   aScrittura.UTCR:=aUser;
   return aScrittura;
 end;

 function buildScrUEP(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aCdTerzo number, aTipoScrittura char, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype is
 begin
  return buildScrUEP(aDocTst, aCdTerzo, aTipoScrittura, null, aUser, aTSnow);
 end;

-- RILEVA SE, PER UNA NOTA CREDITO, LA COMPETENZA ECONOMICA DELLA FATTURA E' IN ESERCIZIO (PRECEDENTE) CHIUSO
 Function isCompFattEsPreChiusoADTREG (aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype) return Boolean Is
 max_comp_coge_fatt  DATE;
 aChiusura           CHIUSURA_COEP%Rowtype;
 Begin
   If aDocTst.ti_fattura != 'C' Then
        IBMERR001.RAISE_ERR_GENERICO('Impossibile rilevare la competenza economica della Fattura collegata, il documento non '||
                'è una Nota di Credito.');
   End If;

   If aDocTst.CD_TIPO_DOCUMENTO = CNRCTB100.TI_FATTURA_PASSIVA Then
Dbms_Output.PUT_LINE ('FATT PASS '||aDoc.CD_CDS||' '||aDoc.CD_UNITA_ORGANIZZATIVA||' '||aDoc.ESERCIZIO||' '||aDoc.PG_NUMERO_DOCUMENTO||' '||aDoc.PG_RIGA);
     Select Max(dt_a_competenza_coge)
     Into   max_comp_coge_fatt
     From   fattura_passiva_riga
     Where  CD_CDS                  = aDoc.CD_CDS And
            CD_UNITA_ORGANIZZATIVA  = aDoc.CD_UNITA_ORGANIZZATIVA And
            ESERCIZIO               = aDoc.ESERCIZIO And
            PG_FATTURA_PASSIVA      = aDoc.PG_NUMERO_DOCUMENTO;
   Elsif aDocTst.CD_TIPO_DOCUMENTO = CNRCTB100.TI_FATTURA_ATTIVA Then
Dbms_Output.PUT_LINE ('FATT ATT '||aDoc.CD_CDS||' '||aDoc.CD_UNITA_ORGANIZZATIVA||' '||aDoc.ESERCIZIO||' '||aDoc.PG_NUMERO_DOCUMENTO||' '||aDoc.PG_RIGA);
     Select Max(dt_a_competenza_coge)
     Into   max_comp_coge_fatt
     From   fattura_attiva_riga
     Where  CD_CDS                  = aDoc.CD_CDS And
            CD_UNITA_ORGANIZZATIVA  = aDoc.CD_UNITA_ORGANIZZATIVA And
            ESERCIZIO               = aDoc.ESERCIZIO And
            PG_FATTURA_ATTIVA      = aDoc.PG_NUMERO_DOCUMENTO;
   End If;

   aChiusura := CNRCTB200.getChiusuraCoep(To_Char(max_comp_coge_fatt, 'yyyy'), aDocTst.cd_cds);

   If Nvl(aChiusura.stato, 'A') = 'C' And Trunc(aDocTst.DT_REGISTRAZIONE) >= Trunc(aChiusura.DUVA) Then
--          IBMERR001.RAISE_ERR_GENERICO('Messaggio di prova: Esercizio competenza fattura chiuso.');
          Return True;
   Else
          Return False;
   End If;
End;

 procedure checkChiusuraEsercizio(aEs number, aCdCds varchar2) is
 begin
  if not CNRCTB008.ISESERCIZIOAPERTOOCHIUSO(aEs, aCdCds) then
    IBMERR001.RAISE_ERR_GENERICO('L''esercizio ('||aEs||') non è ancora aperto per il cds :'||aCdCds);
  end if;
  if (CNRCTB200.ISCHIUSURACOEPDEF(aEs, aCdCds)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico è chiuso definitivamente o in fase di chiusura per il cds: '||aCdCds);
  end if;
 end;

 function trovaContoContrEp(aEs number,aTiAppartenza char,aTiGestione Char, aEV varchar2,aVocein varchar2) return voce_ep%rowtype is
  ass ass_ev_voceep%rowtype;
  aContoEp voce_ep%rowtype;
 begin
  begin
  if (aEV is not null) then
    select * into ass
    from ass_ev_voceep
    where
    	  esercizio = aEs
	   		and TI_APPARTENENZA = nvl(aTiAppartenza,TI_APPARTENENZA)
       	and ti_gestione = nvl(aTiGestione,ti_gestione)
       	and CD_ELEMENTO_VOCE = aEV;
  elsif (aVocein is not null) then
  	select * into ass
    from ass_ev_voceep
    where
    	  esercizio = aEs
	     	and CD_VOCe_EP = aVocein and
	     	rownum =1;
  end if;

  exception when NO_DATA_FOUND then
   	IBMERR001.RAISE_ERR_GENERICO('Associazione ev/coep non trovata per voce:'||aEs||'/'||aTiGestione||'/'||aEV||' o VoceIn /'||aVocein);
  end;
  if (ass.CD_VOCE_EP_CONTR is null) then
 			IBMERR001.RAISE_ERR_GENERICO('Contropartita mancante in Associazione ev/coep:'||aEs||'/'||aTiGestione||'/'||aEV||' o VoceIn /'||aVocein);
 	else
  		aContoEp:=CNRCTB002.getVoceEp(ass.esercizio, ass.CD_VOCE_EP_CONTR);
  end if;
  return aContoEp;
 end;
end;
