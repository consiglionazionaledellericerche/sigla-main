--------------------------------------------------------
--  DDL for Package Body CNRCTB110
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB110" IS
 procedure creaGenerico(aGen in out documento_generico%rowtype, aListRighe in out CNRCTB100.docGenRigaList) is
  aNextNum number;
 begin
  aNextNum:=CNRCTB100.getNextNum(aGen.cd_cds, aGen.esercizio, aGen.cd_unita_organizzativa, aGen.cd_tipo_documento_amm, aGen.utcr, aGen.dacr);
  aGen.pg_documento_generico:=aNextNum;
  aGen.duva:=aGen.dacr;
  aGen.utuv:=aGen.utcr;
  aGen.pg_ver_rec:=1;
  aGen.im_totale:=0;
  for i in 1 .. aListRighe.count loop
   aGen.im_totale:=aGen.im_totale+aListRighe(i).im_riga;
  end loop;
  if aGen.dt_da_competenza_coge is null and aGen.dt_a_competenza_coge is null then
   aGen.dt_da_competenza_coge:=trunc(sysdate);
   aGen.dt_a_competenza_coge:=trunc(sysdate);
  end if;
  CNRCTB100.INS_DOCUMENTO_GENERICO(aGen);
  for i in 1 .. aListRighe.count loop
   aListRighe(i).pg_documento_generico:=aGen.pg_documento_generico;
   aListRighe(i).progressivo_riga:=i;
   aListRighe(i).duva:=aGen.dacr;
   aListRighe(i).utuv:=aGen.utcr;
   aListRighe(i).pg_ver_rec:=1;
   CNRCTB100.INS_DOCUMENTO_GENERICO_RIGA(aListRighe(i));
  end loop;
 end;

 procedure creaGenericoAggObbAcc(aGen in out documento_generico%rowtype, aListRighe in out CNRCTB100.docGenRigaList) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
 begin

  for i in 1..aListRighe.count loop
    verificaTerzo(aListRighe(i));
  end loop;
  creaGenerico(aGen, aListRighe);
  for i in 1..aListRighe.count loop
   if aListRighe(i).pg_obbligazione is not null then
    CNRCTB035.aggiornaSaldoDocammObb(aListRighe(i).cd_cds_obbligazione, aListRighe(i).esercizio_obbligazione, aListRighe(i).esercizio_ori_obbligazione, aListRighe(i).pg_obbligazione, aListRighe(i).pg_obbligazione_scadenzario, aListRighe(i).im_riga, aGen.utcr);
   elsif aListRighe(i).pg_accertamento is not null then
    CNRCTB035.aggiornaSaldoDocammAcc(aListRighe(i).cd_cds_accertamento, aListRighe(i).esercizio_accertamento, aListRighe(i).esercizio_ori_accertamento, aListRighe(i).pg_accertamento, aListRighe(i).pg_accertamento_scadenzario, aListRighe(i).im_riga, aGen.utcr);
   else
    IBMERR001.RAISE_ERR_GENERICO('Nessun riferimento ad accertamento o '||cnrutil.getLabelObbligazioneMin()||' per il documento generico');
   end if;
  end loop;
 end;

 procedure verificaTerzo( aRiga documento_generico_riga%rowtype) is
  aObbligazione obbligazione%rowtype;
  aAccertamento accertamento%rowtype;
  aAnag anagrafico%rowtype;
 begin
   if aRiga.pg_obbligazione is not null then
      begin
        select * into aObbligazione from obbligazione a where
	   a.cd_cds = aRiga.cd_cds_obbligazione
           and a.esercizio = aRiga.esercizio_obbligazione
           and a.esercizio_originale = aRiga.esercizio_ori_obbligazione
 	   and a.pg_obbligazione = aRiga.pg_obbligazione;
      exception when NO_DATA_FOUND then
          If cnrutil.isLabelObbligazione() Then
            IBMERR001.RAISE_ERR_GENERICO('Obbligazione non trovata');
       	  Else
            IBMERR001.RAISE_ERR_GENERICO('Impegno non trovato');
      	  End If;
      end;
      if aObbligazione.cd_terzo = aRiga.cd_terzo then
         return;
	  end if;
      begin
        select b.* into aAnag from terzo a, anagrafico b where
           a.cd_terzo = aObbligazione.cd_terzo
           and a.cd_anag = b.cd_anag;
      exception when NO_DATA_FOUND then
         IBMERR001.RAISE_ERR_GENERICO('Anagrafico non trovato');
      end;
      if aAnag.ti_entita = cnrctb080.TI_ENTITA_DIVERSI then
         return;
      else
         IBMERR001.RAISE_ERR_GENERICO('Attenzione la riga "' || aRiga.ds_riga ||'" ha un terzo incompatibile con il documento contabile associato');
	  end if;
   elsif aRiga.pg_accertamento is not null then
      begin
        select * into aAccertamento from accertamento a where
	   a.cd_cds = aRiga.cd_cds_accertamento
           and a.esercizio = aRiga.esercizio_accertamento
           and a.esercizio_originale = aRiga.esercizio_ori_accertamento
 	   and a.pg_accertamento = aRiga.pg_accertamento;
      exception when NO_DATA_FOUND then
         IBMERR001.RAISE_ERR_GENERICO('Accertamento non trovato');
      end;
      if aAccertamento.cd_terzo = aRiga.cd_terzo then
         return;
	  end if;
      begin
        select b.* into aAnag from terzo a, anagrafico b where
           a.cd_terzo = aAccertamento.cd_terzo
           and a.cd_anag = b.cd_anag;
      exception when NO_DATA_FOUND then
         IBMERR001.RAISE_ERR_GENERICO('Anagrafico non trovato');
      end;
      if aAnag.ti_entita = cnrctb080.TI_ENTITA_DIVERSI then
         return;
      else
         IBMERR001.RAISE_ERR_GENERICO('Attenzione la riga "'|| aRiga.ds_riga  ||'" ha un terzo '||aRiga.cd_terzo||' incompatibile con il terzo del documento contabile associato '||aAccertamento.cd_terzo);
	  end if;
   else
    IBMERR001.RAISE_ERR_GENERICO('Nessun riferimento ad accertamento o '||cnrutil.getLabelObbligazioneMin()||' per il documento generico');
   end if;
 end;


END;
