CREATE OR REPLACE Procedure regLiqIvaMensileCOGE_STANI(aEs number, aEs_scrittura NUMBER, aCds varchar2, aUO varchar2, aTipo char, aDtDa date, aDtA date, aUser varchar2) is
  aLiqIva liquidazione_iva%rowtype;
  aVoceDebVersoErario voce_ep%rowtype;
  aVoceEarioCIVA voce_ep%rowtype;
  aVoceIvaDebito voce_ep%rowtype;
  aVoceIvaCredito voce_ep%rowtype;
  aVoceBancaCds voce_ep%rowtype;
  aVoceCostoIvaNonDetraibile voce_ep%rowtype;
  aVoceRicavoIvaNonDetraibile voce_ep%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aGen documento_generico%rowtype;
  aTSNow date;
  aListaMovimenti CNRCTB200.movimentiList;
  aScrittura scrittura_partita_doppia%rowtype;
  aDtContabil date;
  aUOVERSIVA unita_organizzativa%rowtype;
  aUOVERSCENTRO unita_organizzativa%rowtype;
  aUOENTE unita_organizzativa%rowtype;
  aPK2String varchar2(50);
  aCdTerzo number(8);
  isVersamentoCentro boolean;
  IST_COMM_LIQ  MOVIMENTO_COGE.TI_ISTITUZ_COMMERC%Type;

Begin

Dbms_Output.PUT_LINE ('ENTRO IN regLiqIvaMensileCOGE');

  If aEs is Null Or aCds is Null Or aUO is Null Or aTipo is Null Or aDtDa is Null Or aDtA is Null Or
        aUser is Null Then
   IBMERR001.RAISE_ERR_GENERICO('Parametri per registrazione economica liquidazione mensile IVA ente non specificati');
  End if;

  -- Fix del 20040924 Richiesta 843
--  CNRCTB204.checkChiusuraEsercizio(aEs, aCds);

Dbms_Output.PUT_LINE ('DOPO checkChiusuraEsercizio');

  -- L'esercizio economico precedente deve essere chiuso definitivamente
  if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aCds)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente '||To_Char(aEs-1)||' non ? chiuso definitivamente per il cds: '||aCds);
  end if;

  aTSNow:=sysdate;
  -- Estrazione dell'UO di versamento IVA
  aUOVERSCENTRO:=CNRCTB020.getUOVersIVA(aEs);
  aUOENTE:=CNRCTB020.getUOENTE(aEs);

  Begin

   Select *
   Into   aLiqIva
   From   liquidazione_iva
   Where  esercizio = aEs And
          cd_cds = aCds And
          cd_unita_organizzativa = aUO And
          tipo_liquidazione = aTipo And
          dt_inizio = aDtDa And
          dt_fine = aDtA And
          report_id = 0
   For Update nowait;

-- DERIVO IL TIPO ISTITUZIONALE/COMMERCIALE DELLA SCRITTURA CHE VERRA' A PARTIRE DAL TIPO_LIQUIDAZIONE:
--  SE "C = Sezionali commerciali" ALLORA E' COMMERCIALE
--  SE "I = Sezionali istituzionali intraue" OPPURE "S = Sezionali istituzionali San Marino senza IVA"
--         ALLORA E' ISTITUZIONALE

Dbms_Output.PUT_LINE ('TROVATA LIQUIDAZIONE_IVA');

   If aLiqIva.TIPO_LIQUIDAZIONE = 'C' Then
        IST_COMM_LIQ := 'C';
   Elsif aLiqIva.TIPO_LIQUIDAZIONE In ('I', 'S') Then
        IST_COMM_LIQ := 'I';
   End If;

  Exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO(CNRCTB205.getDesc(aLiqIva)||' non trovata.');
  End;

  if aLiqIva.cd_unita_organizzativa=aUOENTE.cd_unita_organizzativa Then
   aUOVERSIVA:=aUOVERSCENTRO;
   isVersamentoCentro:=true;
   -- Modifica del 22/04/2004
   -- la scrittura che gira il conto erario IVA sulla banca viene fatta sul mandato di versamento
   -- del centro e quindi la gestione non ? pi? fatta a livello di questo metodo
   Dbms_Output.PUT_LINE ('ESCE 1');
   return;
  else
   aUOVERSIVA:=CNRCTB020.getUOValida(aLiqIva.esercizio,aLiqIva.cd_unita_organizzativa);
   isVersamentoCentro:=false;
  end if;

-- Verifica che le scritture non siano gi? state fatte
aPK2String:=CNRCTB204.pk2String(aLiqIva);

For aTSCR in (Select *
              From   scrittura_partita_doppia
              Where  esercizio = aEs And
                     cd_cds= aUOVERSIVA.cd_unita_padre And
                     cd_unita_organizzativa = aUOVERSIVA.cd_unita_organizzativa And
                     origine_scrittura = CNRCTB200.ORIGINE_LIQUID_IVA And
                     cd_comp_documento = aPK2String
	      For Update nowait) Loop
   IBMERR001.RAISE_ERR_GENERICO(CNRCTB205.getDesc(aLiqIva)||': Scrittura gi? eseguita.');
End Loop;

  aDtContabil := To_Date('01/01/2008', 'dd/mm/yyyy');

  aCdTerzo := 0;

  -- Estrazione dei dati di versamento (doc generico) se esistono
  Begin

Dbms_Output.PUT_LINE ('CERCA DOC GEN');

   Select *
   Into   aGen
   From   documento_generico
   Where  cd_tipo_documento_amm = aLiqIva.cd_tipo_documento And
          esercizio = aLiqIva.esercizio_doc_amm And
          cd_cds= aLiqIva.cd_cds_doc_amm And
          cd_unita_organizzativa = aLiqIva.cd_uo_doc_amm And
          pg_documento_generico = aLiqIva.pg_doc_amm;

   For aTGenRiga in (Select *
                     From   documento_generico_riga
                     Where  cd_tipo_documento_amm = aLiqIva.cd_tipo_documento And
                            esercizio = aLiqIva.esercizio_doc_amm And
                            cd_cds= aLiqIva.cd_cds_doc_amm And
                            cd_unita_organizzativa = aLiqIva.cd_uo_doc_amm And
                            pg_documento_generico = aLiqIva.pg_doc_amm) Loop

    aGenRiga:=aTGenRiga;
    Exit;
   End Loop;

   if not isVersamentoCentro then
    aVoceDebVersoErario:=CNRCTB204.trovaContoAnag(aLiqIva.esercizio, aGenRiga.cd_terzo, aLiqIva.cd_tipo_documento, null);
   end if;

   aDtContabil := To_Date('01/01/2008', 'dd/mm/yyyy');

   aCdTerzo := aGenRiga.cd_terzo;

  Exception when NO_DATA_FOUND then
     Null; -- IBMERR001.RAISE_ERR_GENERICO('Documento generico di versamento IVA non trovato');
  End;

  -- Estrazione dei conti
  aVoceEarioCIVA:=CNRCTB002.GETVOCEEPERARIOCIVA(aLiqIva.esercizio);

  if not isVersamentoCentro then
   aVoceIvaDebito:=CNRCTB002.GETVOCEEPIVADEBITO(aLiqIva.esercizio);
   aVoceIvaCredito:=CNRCTB002.GETVOCEEPIVACREDITO(aLiqIva.esercizio);
  end if;

-- prima REGISTRAZIONE ===> IVA VENDITE

Dbms_Output.PUT_LINE ('PREPARA SCRITTURA');

  aScrittura:=null;
  aScrittura.CD_CDS:=aUOVERSIVA.cd_unita_padre;
  aScrittura.ESERCIZIO:=aEs_scrittura;
  aScrittura.CD_UNITA_ORGANIZZATIVA:=aUOVERSIVA.cd_unita_organizzativa;
  aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_LIQUID_IVA;
  aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_LIQIVAVENDITE;
  aScrittura.CD_COMP_DOCUMENTO:=aPK2String;
  aScrittura.CD_TIPO_DOCUMENTO:=aGenRiga.cd_tipo_documento_amm;
  aScrittura.CD_CDS_DOCUMENTO:=aGenRiga.cd_cds;
  aScrittura.CD_UO_DOCUMENTO:=aGenRiga.cd_unita_organizzativa;
  aScrittura.PG_NUMERO_DOCUMENTO:=aGenRiga.pg_documento_generico;
  aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura

  aScrittura.DT_CONTABILIZZAZIONE := aDtContabil;

  aScrittura.DT_PAGAMENTO:=null;
  aScrittura.DT_CANCELLAZIONE:=null;
  aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_PRIMA; -- Si tratta del tipo di scrittura prima o ultima
  aScrittura.CD_TERZO:=aCdTerzo;
  aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
  aScrittura.CD_DIVISA:=aGen.cd_divisa;
  aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non ? chiaro
  aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

  aScrittura.DS_SCRITTURA := 'Liquidazione IVA mensile CDS '||aUOVERSIVA.cd_unita_padre||', UO '||aUOVERSIVA.cd_unita_organizzativa;

  aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
  aScrittura.ATTIVA:='Y';
  aScrittura.ESERCIZIO_DOCUMENTO_AMM:=aGenRiga.esercizio;
  aScrittura.DACR:=aTSNow;
  aScrittura.UTCR:=aUser;

  -- Scritture SOLO per le UO decentrate
  if not isVersamentoCentro then
   -- Generazione della scrittura per le vendite
   if aLiqIva.iva_Debito > 0 then
    aListaMovimenti.delete;

Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 1');

    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                            aVoceIvaDebito,aLiqIva.iva_Debito,CNRCTB200.IS_DARE,To_Date('01/01/2008', 'dd/mm/yyyy')  , To_Date('01/01/2008', 'dd/mm/yyyy')  ,aCdTerzo,
                            IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceEarioCIVA,
                        aLiqIva.iva_Debito,CNRCTB200.IS_AVERE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),
                        aCdTerzo,
                            IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
    aScrittura.ds_scrittura:='IVA VENDITE';

Dbms_Output.PUT_LINE ('INS SCRITTURA 1');

CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
   end if;


-- seconda REGISTRAZIONE ===> IVA ACQUISTI

   -- Errore 830 - al posto di iva_credito va utilizzata l'iva a credito senza
   -- prorata (campo iva_credito_no_prorata della liquidazione)
   -- Generazione della scrittura per gli acquisti
   if aLiqIva.iva_credito_no_prorata > 0 then
    aListaMovimenti.delete;
    aScrittura.pg_scrittura:=null;
    aScrittura.im_Scrittura:=null;
    aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_LIQIVAACQUISTI;
    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceEarioCIVA,
                                aLiqIva.iva_credito_no_prorata,CNRCTB200.IS_DARE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),
                                aCdTerzo,
                            IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceIvaCredito,
                aLiqIva.iva_credito_no_prorata,CNRCTB200.IS_AVERE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),aCdTerzo,
                            IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
    aScrittura.ds_scrittura:='IVA ACQUISTI';

Dbms_Output.PUT_LINE ('INS SCRITTURA 2');

    CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
   end if;
  end if;

-- terza REGISTRAZIONE ===> IVA SALDO

  -- Generazione della scrittura per il saldo se necessaria per aprire il debito verso
  -- l'erario chiuso dal pagamento del mandato

  aListaMovimenti.delete;
  aScrittura.pg_scrittura:=null;
  aScrittura.im_Scrittura:=null;
  aScrittura.ds_scrittura:='IVA SALDO';
  aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_LIQIVASALDO;

  -- Movimenti per l'ENTE
  if isVersamentoCentro Then -- se la UO ? la UO ENTE

   aVoceBancaCds:=CNRCTB002.GETVOCEEPBANCACDS(aLiqIva.esercizio);
Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 2');
   CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceEarioCIVA,
                aGen.im_totale,CNRCTB200.IS_DARE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),aCdTerzo,
                           IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                           aListaMovimenti,aUser,aTSnow);
   CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceBancaCds,aGen.im_totale,
   CNRCTB200.IS_AVERE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),aCdTerzo,
                           IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                           aListaMovimenti,aUser,aTSnow);
  Else  -- altrimenti (per tutte le altre UO normali)

  -- Movimenti per le UO decentrate

   aVoceCostoIvaNonDetraibile:=CNRCTB002.getVoceEpCostoIvaNonDetraibile(aLiqIva.esercizio);
   aVoceRicavoIvaNonDetraibile:=CNRCTB002.getVoceEpRicIvaNonDetraibile(aLiqIva.esercizio);

   if aGen.pg_documento_generico is not null then

-- Movimenti nel caso ci sia iva da versare (si ? versata l'IVA)

-- NEI CASI IN CUI IL SALDO FINALE PREVEDE UN SALDO A DEBITO (QUINDI CON IVA DA VERSARE),
-- OLTRE ALLE SCRITTURE CHE TRAVASANO L'IVA A CREDITO E L'IVA A DEBITO NELL'ERARIO C/IVA
-- (GIA' FATTE PRIMA), SI DEVONO FARE ANCHE QUELLE CHE RILEVANO L'EVENTUALE COSTO PER IVA INDETRAIBILE E
-- E LA CHIUSURA DEL CONTO ERARIO SUL CONTO DEBITI VERSO ERARIO PER LA PARTE VERSATA

-- fa la registrazione con in AVERE l'Erario C/IVa per stornare la parte indetraibile (PRO RATA)

    If aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata < 0 then
Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 3');
     CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                             aVoceEarioCIVA,
                             Abs(aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata),
                             CNRCTB200.IS_AVERE, To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'), aCdTerzo,
                             IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);

     CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                             aVoceCostoIvaNonDetraibile,
                             Abs(aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata),
                             CNRCTB200.IS_DARE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),aCdTerzo,
                             IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);
    End If;

-- COMUNQUE REGISTRA LA CHIUSURA DEL CONTO ERARIO C/IVA CON IL DEBITO VS/ERARIO
Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 4');
    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                            aVoceEarioCIVA, aGen.im_totale,
                            CNRCTB200.IS_DARE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),aCdTerzo,
                            IST_COMM_LIQ, --CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);

    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                            aVoceDebVersoErario, aGen.im_totale,
                            CNRCTB200.IS_AVERE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),aCdTerzo,
                            IST_COMM_LIQ, --CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
   else

-- Movimenti nel caso NON ci sia iva da versare

-- NEI CASI IN CUI IL SALDO FINALE NON PREVEDE UN SALDO A DEBITO (QUINDI CON IVA NON DA VERSARE
-- MA MAGARI DA PORTARE A CREDITO NEL PERIODO SUCCESSIVO),  OLTRE ALLE SCRITTURE CHE TRAVASANO
-- L'IVA A CREDITO E L'IVA A DEBITO NELL'ERARIO C/IVA (GIA' FATTE PRIMA),
-- SI DEVONO FARE ANCHE QUELLE CHE RILEVANO L'EVENTUALE COSTO PER IVA INDETRAIBILE

-- NON SI FA LA CHIUSURA DEL CONTO ERARIO SUL CONTO DEBITI VERSO ERARIO PER LA PARTE VERSATA PERCHE'
-- NON C'E' VERSAMENTO !!!

-- fa la registrazione con in AVERE l'Erario C/IVA per stornare la parte indetraibile (PRO RATA)

    If aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata < 0 then
Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 5');
     CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                             aVoceEarioCIVA,
                             Abs(aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata),
                             CNRCTB200.IS_AVERE, To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'), aCdTerzo,
                             IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);

     CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                             aVoceCostoIvaNonDetraibile,
                             Abs(aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata),
                             CNRCTB200.IS_DARE,To_Date('01/01/2008', 'dd/mm/yyyy'),To_Date('01/01/2008', 'dd/mm/yyyy'),aCdTerzo,
                             IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);
    End If;

   end if;
  end if;
Dbms_Output.PUT_LINE ('INS SCRITTURA 3');

  CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
 end;
/


