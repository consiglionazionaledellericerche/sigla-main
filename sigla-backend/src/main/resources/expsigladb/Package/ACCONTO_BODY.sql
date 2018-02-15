--------------------------------------------------------
--  DDL for Package Body ACCONTO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "ACCONTO" As

-- ==================================================================================================
-- Inserimento di un record in ACCONTO_CLASSIFIC_CORI_ALTRI
-- ==================================================================================================
PROCEDURE insAccontoClassificCori
   (
    aRecAccontoClassificCori ACCONTO_CLASSIFIC_CORI_ALTRI%Rowtype
   ) IS

BEGIN

      INSERT INTO ACCONTO_CLASSIFIC_CORI_ALTRI
             (ESERCIZIO,
              CD_ANAG,
              CD_CLASSIFICAZIONE_CORI,
              IMPONIBILE,
  	      ALIQUOTA,
  	      IM_ACCONTO_CALCOLATO,
              IM_ACCONTO_TRATTENUTO,
              --IM_ACCONTO_CONGUAGLIATO,
              PG_COMUNE,
              DACR,
              UTCR,
              DUVA,
              UTUV,
              PG_VER_REC)
      VALUES (aRecAccontoClassificCori.esercizio,
              aRecAccontoClassificCori.cd_anag,
              aRecAccontoClassificCori.cd_classificazione_cori,
              aRecAccontoClassificCori.imponibile,
              aRecAccontoClassificCori.aliquota,
              aRecAccontoClassificCori.im_acconto_calcolato,
              aRecAccontoClassificCori.im_acconto_trattenuto,
              --aRecAccontoClassificCori.im_acconto_conguagliato,
              aRecAccontoClassificCori.pg_comune,
              aRecAccontoClassificCori.dacr,
              aRecAccontoClassificCori.utcr,
              aRecAccontoClassificCori.duva,
              aRecAccontoClassificCori.utuv,
              aRecAccontoClassificCori.pg_ver_rec);

END insAccontoClassificCori;

/* Per il calcolo dell'imponibile su cui calcolare l'acconto (imponibile lordo - family)
   Procedura che richiama le stese procedure del CUD e conserva i dati in ESTRAZIONE_CUD */
Procedure calcolaImponibileAnnoPrec
   (
    inEsercizio NUMBER,
    inRepID     INTEGER,
    inUtente    VARCHAR2
   ) Is
   gen_cur_anag GenericCurTyp;
   aCd_anag	 anagrafico.cd_anag%Type;
Begin
      cnrctb900.valorizzaParametri(inEsercizio-1);
      Open gen_cur_anag For

	   Select Distinct t.cd_anag
	   From TERZO T
	   Where T.CD_TERZO In('74008','77544','76212','65957','76211','76210','77771','87631');
      Loop
         Fetch gen_cur_anag Into aCd_anag;

         Exit When gen_cur_anag %NOTFOUND;
            -------------------------------------------------------------------------------------------------
   	    -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
   	    -- dei dati relativi alla testata del compenso
	    cnrctb900.inserisciDatiCUDDett(inEsercizio-1,
                        aCd_anag,
                        inRepID,
                        inUtente);


      End Loop;
      Close gen_cur_anag;
      -------------------------------------------------------------------------------------------------
      -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
      -- dei dati relativi alla testata del compenso

      cnrctb900.upgDatiCUDDettCori(inEsercizio-1,
                                   inRepID);

      --COMMIT;
      -------------------------------------------------------------------------------------------------
      -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
      -- dei dati relativi alla testata del compenso

      cnrctb900.upgDatiCUDDettCong(inEsercizio-1,
                                   inRepID);
      -------------------------------------------------------------------------------------------------
      -- Aggregazione dati di ESTRAZIONE_CUD_DETT in ESTRAZIONE_CUD.

      cnrctb900.aggregaDatiCud(inEsercizio-1,
                               inRepID,
                               inUtente);

End calcolaImponibileAnnoPrec;

Procedure calcolaAddTerritorioAcconto
    (
       inEsercizio  ACCONTO_CLASSIFIC_CORI_ALTRI.esercizio%Type,
       inPerc	    NUMBER,
       inUtente     ACCONTO_CLASSIFIC_CORI_ALTRI.utuv%Type
    ) Is

    gen_cur_anag GenericCurTyp;
    aCd_anag	 anagrafico.cd_anag%Type;
    aRecAccontoClassificCori ACCONTO_CLASSIFIC_CORI_ALTRI%ROWTYPE;
    aCdRegione  REGIONE.cd_regione%TYPE;
    aCdProvincia PROVINCIA.cd_provincia%TYPE;
    aPgComune	 ACCONTO_CLASSIFIC_CORI_ALTRI.pg_comune%Type;
    aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE;
    aTiAnag	varchar2(1);
    aAliq  ACCONTO_CLASSIFIC_CORI_ALTRI.aliquota%Type;
    aCdCori	V_PRE_SCAGLIONE.cd_contributo_ritenuta%Type:='ADDCOM';
    inRepID  	INTEGER;
    imponibileLordoAnnoPrec	number(15,2);
    im_deduzione_family		number(15,2);
    imponibileNettoAnnoPrec	number(15,2);
Begin
      dataOdierna := Sysdate;
      aCdRegione:='*';
      aCdProvincia:='*';
      aTiAnag:='*';
      aAliq:=0;

      inRepID:=999999999;

      --Procedura che conserva in ESTRAZIONE_CUD gli imponibili di tutti i terzi interessati
      calcolaImponibileAnnoPrec(inEsercizio,inRepID,inUtente);

      Open gen_cur_anag For

	   Select Distinct t.cd_anag
	   From TERZO T
	   Where T.CD_TERZO In('74008','77544','76212','65957','76211','76210','77771','87631');

      Loop

         Fetch gen_cur_anag Into aCd_anag;

         Exit When gen_cur_anag %NOTFOUND;

	 --prendo il comune del domicilio fiscale
	 aPgComune := cnrctb080.getAnagDaCdAnag(aCd_anag).pg_comune_fiscale;
	 If aPgComune Is Not Null Then

	     aRecAccontoClassificCori.esercizio := inEsercizio;
	     aRecAccontoClassificCori.cd_anag := aCd_anag;
	     aRecAccontoClassificCori.cd_classificazione_cori := cnrctb545.isCoriAddCom;

	     Select Nvl(Sum(imponibile_si_detr),0), Nvl(Sum(im_deduzione_family),0), Nvl(Sum(imponibile_irpef),0)
   	     Into imponibileLordoAnnoPrec, im_deduzione_family, imponibileNettoAnnoPrec
   	     From   ESTRAZIONE_CUD
   	     Where  id_estrazione = inRepID
     	       And  esercizio = inEsercizio-1
     	       And  dip_cd_anag = aCd_anag
     	       And  rigo_cococo = 1;

	     --solo se l'imponibile netto è >0 calcolo l'acconto
	     If imponibileNettoAnnoPrec > 0 Then

  	           aRecAccontoClassificCori.imponibile := imponibileLordoAnnoPrec - im_deduzione_family;

	           --prendo l'aliquota associata
	           aRecVPreScaglione:=CNRCTB545.getScaglione(aCdCori,
                                             	 aTiAnag,
                                             	 dataOdierna,
                                             	 aRecAccontoClassificCori.imponibile,
                                             	 aAliq,
                                             	 aCdRegione,
                                             	 aCdProvincia,
                                               aPgComune);
                   /*
                   Exception
                       When Others Then
                          pipe.send_message('aCdCori = '||aCdCori);
                          pipe.send_message('aTiAnag = '||aTiAnag);
                          pipe.send_message('dataOdierna = '||dataOdierna);
                          pipe.send_message('aRecAccontoClassificCori.imponibile = '||aRecAccontoClassificCori.imponibile);
                          pipe.send_message('aAliq = '||aAliq);
                          pipe.send_message('aCdRegione = '||aCdRegione);
                          pipe.send_message('aCdProvincia = '||aCdProvincia);
                          pipe.send_message('aPgComune = '||aPgComune);
                   End;*/
                   --inserisco se le l'aliquota è > 0
                   If aRecVPreScaglione.aliquota_percip > 0 Then
	                aRecAccontoClassificCori.aliquota := aRecVPreScaglione.aliquota_percip;
                        aRecAccontoClassificCori.im_acconto_calcolato := ((aRecAccontoClassificCori.imponibile * aRecAccontoClassificCori.aliquota / 100) * inPerc)/100;
                        aRecAccontoClassificCori.im_acconto_trattenuto := 0;
                        --aRecAccontoClassificCori.im_acconto_conguagliato := 0;
                        aRecAccontoClassificCori.pg_comune := aPgComune;
                        aRecAccontoClassificCori.dacr := dataOdierna;
                        aRecAccontoClassificCori.utcr := inUtente;
                        aRecAccontoClassificCori.duva := dataOdierna;
                        aRecAccontoClassificCori.utuv := inUtente;
                        aRecAccontoClassificCori.pg_ver_rec := 1;

	                insAccontoClassificCori(aRecAccontoClassificCori);
	           End If;
	     End If;
	End If;
      End Loop;

      Close gen_cur_anag;

End calcolaAddTerritorioAcconto;


END;
