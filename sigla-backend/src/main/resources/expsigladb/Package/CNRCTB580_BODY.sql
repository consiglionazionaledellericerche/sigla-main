--------------------------------------------------------
--  DDL for Package Body CNRCTB580
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB580" AS

--==================================================================================================
-- Lettura di un record anticipo
--==================================================================================================
FUNCTION getAnticipo
   (
    aCdCds varchar2,
    aCdUo varchar2,
    aEs number,
    aPgAnticipo number,
    eseguiLock char
   ) RETURN ANTICIPO%ROWTYPE IS
   aRecAnticipo ANTICIPO%ROWTYPE;

BEGIN

   BEGIN
      IF eseguiLock = 'Y' THEN
         SELECT * INTO aRecAnticipo
         FROM   ANTICIPO
         WHERE  cd_cds = aCdCds AND
		cd_unita_organizzativa = aCdUo AND
                esercizio = aEs AND
                pg_anticipo = aPgAnticipo
         FOR UPDATE NOWAIT;
      ELSE
         SELECT * INTO aRecAnticipo
         FROM   ANTICIPO
         WHERE  cd_cds = aCdCds AND
		cd_unita_organizzativa = aCdUo AND
                esercizio = aEs AND
                pg_anticipo = aPgAnticipo;
      END IF;

      RETURN aRecAnticipo;

   EXCEPTION
      WHEN NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO
              ('Anticipo non trovato, cds:'||aCdCds||' es:'||aEs||' uo:'||aCdUo||' pg:'||aPgAnticipo);
   END;

END getAnticipo;

--==================================================================================================
-- Sgangio di un anticipo da una missione cancellata logicamente o fisicamente
--==================================================================================================
PROCEDURE sganciaAnticipoDaMissione
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEs NUMBER,
    aPgAnticipo NUMBER,
    aUser VARCHAR2,
    aTSNow DATE
   ) IS
   eseguiLock CHAR(1);
   flEsisteRimborso CHAR(1);
   aRecAnticipo ANTICIPO%ROWTYPE;


BEGIN

   -------------------------------------------------------------------------------------------------
   -- Lettura del record anticipo

   eseguiLock:='Y';
   aRecAnticipo:=getAnticipo(aCdCds,
                             aCdUo,
                             aEs,
                             aPgAnticipo,
                             eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Verifico che non vi sia rimborso

   SELECT DECODE(COUNT(*),0,'N','Y') INTO flEsisteRimborso
   FROM   DUAL
   WHERE  EXISTS
          (SELECT 1
           FROM   RIMBORSO
           WHERE  cd_cds_anticipo = aRecAnticipo.cd_cds AND
                  cd_uo_anticipo = aRecAnticipo.cd_unita_organizzativa  AND
                  esercizio_anticipo = aRecAnticipo.esercizio  AND
                  pg_anticipo = aRecAnticipo.pg_anticipo);

   IF flEsisteRimborso = 'Y' THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('L''anticipo ' || aEs || '/' || aPgAnticipo || ' risulta associato ad un rimborso.' ||
          'Impossibile eliminare la missione');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Sgancio dell'anticipo

   UPDATE ANTICIPO
   SET    fl_associato_missione = 'N',
          utuv = aUser,
          duva = aTSNow,
          pg_ver_rec = pg_ver_rec + 1
  WHERE   cd_cds = aCdCds AND
          cd_unita_organizzativa = aCdUo AND
          esercizio = aEs AND
          pg_anticipo = aPgAnticipo;


END sganciaAnticipoDaMissione;




 Procedure rimborsoAnticipo(aAnticipo anticipo%rowtype, aUser varchar2, aTSNow date, aEsercizioScrivania number) is
  aEvEntrata elemento_voce%rowtype;
  aRimb rimborso%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aDettScadList CNRCTB035.scadVoceListE;
  aAccScadVoce accertamento_Scad_voce%rowtype;
  aVoceF voce_f%rowtype;
  aUOENTE unita_organizzativa%rowtype;
  aCdTerzoUo number(8);
  aCdModPagUo varchar2(10);
  aPgBancaUo number(10);
  aMissione missione%rowtype;
  aCompenso compenso%rowtype;
  aImporto number(15,2);

  annoCompetenzaA number;
  esercizioPrecedente number;
  dataCreazioneMax DATE;
	recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  -- Se l'anticipo non è pagato -> esce
  if aAnticipo.stato_cofi != CNRCTB100.STATO_GEN_COFI_TOT_MR and
     aAnticipo.stato_pagamento_fondo_eco != CNRCTB100.STATO_REG_PFONDOECO then
   IBMERR001.RAISE_ERR_GENERICO('L''anticipo non risulta essere stato pagato !');
  end if;

  if aAnticipo.CD_LINEA_ATTIVITA is null or aAnticipo.CD_CENTRO_RESPONSABILITA is null then
   IBMERR001.RAISE_ERR_GENERICO('Linea di attività da utilizzare per rimborso anticipo non specificata');
  end if;

  -- verifica che l'anticipo non soia collegato a missione
  if aAnticipo.fl_associato_missione = 'Y' then
   begin
    select * into aMissione from missione where
		 cd_cds_anticipo = aAnticipo.cd_cds
	 and esercizio_anticipo = aAnticipo.esercizio
	 and cd_uo_anticipo = aAnticipo.cd_unita_organizzativa
	 and pg_anticipo = aAnticipo.pg_anticipo
	 and stato_cofi <> CNRCTB100.STATO_GEN_COFI_ANN -- 09.06.2004 - BORRIELLO - Fix Err. CNR 828
	for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Missione collegata ad anticipo non trovata');
   end;
   aImporto:=aAnticipo.im_anticipo-aMissione.im_totale_missione;
   if aMissione.fl_associato_compenso = 'Y' then
    begin
     select * into aCompenso from compenso where
	 	  cd_cds_missione = aMissione.cd_cds
	  and esercizio_missione = aMissione.esercizio
	  and cd_uo_missione = aMissione.cd_unita_organizzativa
	  and pg_missione = aMissione.pg_missione
	 for update nowait;
	exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Compenso collegato a missione con anticipo non trovato');
	end;
    aImporto:=aAnticipo.im_anticipo-aCompenso.im_netto_percipiente;
   end if;
   -- Se l'importo della missione è maggiore rispetto all'anticipo ritorno
   if aImporto <= 0 then
       IBMERR001.RAISE_ERR_GENERICO('Importo anticipo minore o uguale all''importo della missione');
   end if;
  else
   aImporto:=aAnticipo.im_anticipo;
  end if;


  recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizioScrivania);
  If nvl(recParametriCnr.fl_nuovo_pdg,'N')='N' Then
		  begin
		   select * into aEvEntrata from elemento_voce where
		       esercizio = aEsercizioScrivania
		   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
		   and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
		   and cd_elemento_voce = CNRCTB015.GETVAL01PERCHIAVE(aEsercizioScrivania,CNRCTB585.ELEMENTO_VOCE_SPECIALE,CNRCTB585.RIMBORSO_ANTICIPI);
		  exception when NO_DATA_FOUND then
		   IBMERR001.RAISE_ERR_GENERICO('Voce speciale di rimborso anticipi non trovata in configurazione cnr');
		  end;
		  begin
		   select * into aVoceF from voce_f where
		       esercizio = aEsercizioScrivania
		   and ti_appartenenza = aEvEntrata.ti_appartenenza
		   and ti_gestione = aEvEntrata.ti_gestione
		   and cd_titolo_capitolo = aEvEntrata.cd_elemento_voce
		   and( cd_unita_organizzativa = aAnticipo.cd_unita_organizzativa or
		   cd_unita_organizzativa is null)
		   and fl_mastrino = 'Y';
		  exception when NO_DATA_FOUND then
		   IBMERR001.RAISE_ERR_GENERICO('Articolo di entrata CNR non trovato per voce del piano: '||aEvEntrata.cd_elemento_voce||' su uo: '||aAnticipo.cd_unita_organizzativa);
		  end;
  else
	  begin
	   select * into aEvEntrata from elemento_voce where
	       esercizio = aEsercizioScrivania
	   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	   and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
	   and cd_elemento_voce = CNRCTB015.GETVAL01PERCHIAVE(aEsercizioScrivania,CNRCTB585.ELEMENTO_VOCE_SPECIALE,CNRCTB585.RIMBORSO_ANTICIPI);
	  exception when NO_DATA_FOUND then
	   IBMERR001.RAISE_ERR_GENERICO('Voce speciale di rimborso anticipi non trovata in configurazione cnr');
	  end;
  end if;
  aUOENTE:=CNRCTB020.getUOENTE(aEsercizioScrivania);

  If CNRCTB008.ISESERCIZIOCHIUSO(aEsercizioScrivania, aAnticipo.cd_cds)  then
   IBMERR001.RAISE_ERR_GENERICO('Impossibile emettere l''accertamento nell''esercizio '||aEsercizioScrivania||', è chiuso per il cds: '||aAnticipo.cd_cds);
  End If;

  aAcc     := null;
  aAccScad := null;

  aAcc.CD_CDS:=aUOEnte.cd_unita_padre;
  aAcc.ESERCIZIO:=aEsercizioScrivania;
  aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC;
  aAcc.CD_UNITA_ORGANIZZATIVA:=aUOEnte.cd_unita_organizzativa;
  aAcc.CD_CDS_ORIGINE:=aAnticipo.cd_cds;
  aAcc.CD_UO_ORIGINE:=aAnticipo.cd_unita_organizzativa;
  aAcc.CD_ELEMENTO_VOCE:=aEvEntrata.cd_elemento_voce;
	If nvl(recParametriCnr.fl_nuovo_pdg,'N')='N' Then
		aAcc.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
  	aAcc.TI_GESTIONE:=aVoceF.ti_gestione;
  	aAcc.CD_VOCE:=aVoceF.cd_voce;
  else
		aAcc.TI_APPARTENENZA:=aEvEntrata.ti_appartenenza;
  	aAcc.TI_GESTIONE:=aEvEntrata.ti_gestione;
  	aAcc.CD_VOCE:=aEvEntrata.cd_elemento_voce;
  end if;
  aAcc.DT_REGISTRAZIONE:=trunc(CNRCTB008.getTimestampContabile(aEsercizioScrivania, aTSNow));
  aAcc.DS_ACCERTAMENTO:='Rimborso anticipo n.'||aAnticipo.pg_anticipo||' cds:'||aAnticipo.cd_cds||' uo:'||aAnticipo.cd_unita_organizzativa;
  aAcc.NOTE_ACCERTAMENTO:='';
  aAcc.CD_TERZO:=aAnticipo.cd_terzo;
  aAcc.IM_ACCERTAMENTO:=aImporto;
  aAcc.FL_PGIRO:='N';
  aAcc.RIPORTATO:='N';
  aAcc.DACR:=aTSNow;
  aAcc.UTCR:=aUser;
  aAcc.DUVA:=aTSNow;
  aAcc.UTUV:=aUser;
  aAcc.PG_VER_REC:=1;
  aAcc.ESERCIZIO_COMPETENZA:=aEsercizioScrivania;

  aAccScad.CD_CDS:=aAcc.CD_CDS;
  aAccScad.ESERCIZIO:=aAcc.ESERCIZIO;
  aAccScad.ESERCIZIO_ORIGINALE:=aAcc.ESERCIZIO_ORIGINALE;
  aAccScad.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
  aAccScad.PG_ACCERTAMENTO_SCADENZARIO:=1;
  aAccScad.DT_SCADENZA_EMISSIONE_FATTURA:=trunc(aTSNow);
  aAccScad.DT_SCADENZA_INCASSO:=trunc(aTSNow);
  aAccScad.DS_SCADENZA:=aAcc.DS_ACCERTAMENTO;
  aAccScad.IM_SCADENZA:=aAcc.IM_ACCERTAMENTO;
  aAccScad.IM_ASSOCIATO_DOC_AMM:=aAcc.IM_ACCERTAMENTO;
  aAccScad.IM_ASSOCIATO_DOC_CONTABILE:=0;
  aAccScad.DACR:=aAcc.DACR;
  aAccScad.UTCR:=aAcc.UTCR;
  aAccScad.DUVA:=aAcc.DUVA;
  aAccScad.UTUV:=aAcc.UTUV;
  aAccScad.PG_VER_REC:=aAcc.PG_VER_REC;

  aAccScadVoce.CD_CDS:=aAccScad.CD_CDS;
  aAccScadVoce.ESERCIZIO:=aAccScad.ESERCIZIO;
  aAccScadVoce.ESERCIZIO_ORIGINALE:=aAccScad.ESERCIZIO_ORIGINALE;
  aAccScadVoce.PG_ACCERTAMENTO:=aAccScad.PG_ACCERTAMENTO;
  aAccScadVoce.PG_ACCERTAMENTO_SCADENZARIO:=aAccScad.PG_ACCERTAMENTO_SCADENZARIO;
  aAccScadVoce.CD_CENTRO_RESPONSABILITA:=aAnticipo.cd_centro_responsabilita;
  aAccScadVoce.CD_LINEA_ATTIVITA:=aAnticipo.CD_LINEA_ATTIVITA;
  aAccScadVoce.IM_VOCE:=aAccScad.IM_SCADENZA;
  aAccScadVoce.CD_FONDO_RICERCA:=null;
  aAccScadVoce.DACR:=aAccScad.DACR;
  aAccScadVoce.UTCR:=aAccScad.UTCR;
  aAccScadVoce.DUVA:=aAccScad.DUVA;
  aAccScadVoce.UTUV:=aAccScad.UTUV;
  aAccScadVoce.PG_VER_REC:=aAccScad.PG_VER_REC;
  aDettScadList(1):=aAccScadVoce;

  CNRCTB040.CREAACCERTAMENTO(false,aAcc,aAccScad,aDettScadList);

  CNRCTB080.GETTERZOPERUO(aAnticipo.cd_unita_organizzativa, aCdTerzoUo, aCdModPagUo, aPgBancaUo ,aAnticipo.esercizio);

  aRimb.CD_CDS:=aAcc.cd_cds;
  aRimb.ESERCIZIO:=aAcc.esercizio;
  aRimb.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
  aRimb.PG_RIMBORSO:=CNRCTB100.GETNEXTNUM(aUOEnte.cd_unita_padre,aRimb.esercizio,aUOEnte.cd_unita_organizzativa,CNRCTB100.TI_RIMBORSO,aUser,aTSNow);
  aRimb.ESERCIZIO_ANTICIPO:=aAnticipo.esercizio;
  aRimb.CD_CDS_ANTICIPO:=aAnticipo.cd_cds;
  aRimb.CD_UO_ANTICIPO:=aAnticipo.cd_unita_organizzativa;
  aRimb.PG_ANTICIPO:=aAnticipo.pg_anticipo;
  aRimb.CD_CDS_ORIGINE:=aAcc.cd_cds_origine;
  aRimb.CD_UO_ORIGINE:=aAcc.cd_uo_origine;
  aRimb.DT_REGISTRAZIONE:=trunc(CNRCTB008.getTimestampContabile(aEsercizioScrivania, aTSNow));
  aRimb.DT_DA_COMPETENZA_COGE:=aRimb.dt_registrazione;
  aRimb.DT_A_COMPETENZA_COGE:=aRimb.dt_registrazione;

  annoCompetenzaA := TO_CHAR(aRimb.dt_a_competenza_coge, 'YYYY');
  esercizioPrecedente := aEsercizioScrivania-1;
  if(annoCompetenzaA=esercizioPrecedente) then
    dataCreazioneMax := CNRCTB015.getDt01PerChiave(aRimb.esercizio, 'CHIUSURA_COSTANTI', 'TERMINE_CREAZIONE_DOCAMM_ES_PREC');
	if(aRimb.DT_REGISTRAZIONE > dataCreazioneMax) then
	   IBMERR001.RAISE_ERR_GENERICO('Non è possibile inserire documenti con competenza nell''anno precedente con data di registrazione successiva al ' || TO_CHAR(dataCreazioneMax, 'DD-MM-YYYY') ||' !');
	 end if;
  end if;

declare
   aAbiCCEnte varchar2(50);
   aCabCCEnte varchar2(50);
   aNCCEnte varchar2(50);
   aPgBanca number(10);
  begin
     aAbiCCEnte:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE,CNRCTB080.ENTE);
     aCabCCEnte:=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE,CNRCTB080.ENTE);
     aNCCEnte:=CNRCTB015.GETVAL03PERCHIAVE(CNRCTB080.CONTO_CORRENTE_SPECIALE,CNRCTB080.ENTE);
     select a.pg_banca into aPgBanca from banca a
     where
	            a.ti_pagamento = CNRCTB080.TI_PAGAMENTO_BANCARIO
            and a.cd_terzo = aCdTerzoUo
            and a.fl_cancellato = 'N'
			and a.abi = aAbiCCEnte
			and a.cab = aCabCCEnte
			and a.numero_conto like '%'||aNCCEnte;
			aPgBancaUo:=aPgBanca;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Informazioni di tipo BANCA (Ente) non trovate per terzo UO ENTE:'||aCdTerzoUo);
  end;
  aRimb.DS_RIMBORSO:='Rimborso anticipo n.'||aAnticipo.pg_anticipo;
  aRimb.TI_ANAGRAFICO:=aAnticipo.ti_anagrafico;
  aRimb.CD_TERZO:=aAnticipo.cd_terzo;
  aRimb.CD_TERZO_UO_CDS:= aCdTerzoUo;
  aRimb.CD_MODALITA_PAG_UO_CDS:= aCdModPagUo;
  aRimb.PG_BANCA_UO_CDS:= aPgBancaUo;
  aRimb.IM_RIMBORSO:=aImporto;
  aRimb.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_CONT;

	if aAnticipo.fl_associato_missione = 'Y' then
    --aRimb.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
    aRimb.STATO_COGE:=CNRCTB100.STATO_COEP_INI;
    aRimb.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
  else
    aRimb.STATO_COGE:=CNRCTB100.STATO_COEP_INI;
    --aRimb.STATO_COAN:=CNRCTB100.STATO_COEP_INI; stani 28/04/2005
    -- Se il rimborso non è associato a missione eredita il flag dall'anticipo (che, inspiegabilmente
    -- nasce sempre da NON CONTABILIZZARE in COAN.
    aRimb.STATO_COAN := aAnticipo.STATO_COAN;
  end if;

  aRimb.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_NON_ASSOC_MAN_REV;
  aRimb.DT_CANCELLAZIONE:=null;
  aRimb.CD_CDS_ACCERTAMENTO:=aAcc.cd_cds;
  aRimb.ESERCIZIO_ACCERTAMENTO:=aAcc.esercizio;
  aRimb.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.esercizio_originale;
  aRimb.PG_ACCERTAMENTO:=aAcc.pg_accertamento;
  aRimb.PG_ACCERTAMENTO_SCADENZARIO:=aAccScad.pg_accertamento_scadenzario;
  aRimb.RAGIONE_SOCIALE:=aAnticipo.ragione_sociale;
  aRimb.NOME:=aAnticipo.nome;
  aRimb.COGNOME:=aAnticipo.cognome;
  aRimb.CODICE_FISCALE:=aAnticipo.codice_fiscale;
  aRimb.PARTITA_IVA:=aAnticipo.partita_iva;
  aRimb.CD_TERMINI_PAG:=aAnticipo.cd_termini_pag;
  aRimb.CD_MODALITA_PAG:=aAnticipo.cd_modalita_pag;
  aRimb.PG_BANCA:=aAnticipo.pg_banca;
  aRimb.DACR:=aTSNow;
  aRimb.UTCR:=aUser;
  aRimb.DUVA:=aTSNow;
  aRimb.UTUV:=aUser;
  aRimb.PG_VER_REC:=1;
  CNRCTB585.INS_RIMBORSO(aRimb);
 end;

 procedure rimborsoCompletoAnticipo( aCdCds varchar2,
 		   							 aEs number,
									 aCdUo varchar2,
									 aPgAnticipo number,
									 aEsercizioScrivania number,
									 aUser varchar2) is
  aAnticipo anticipo%rowtype;
  aTSNow date;
 begin
  aTSNow:=sysdate;
  begin
   select * into aAnticipo from anticipo where
        esercizio = aEs
    and cd_cds = aCdCds
	and cd_unita_organizzativa = aCdUo
	and pg_anticipo = aPgAnticipo
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Anticipo non trovato: '||aPgAnticipo);
  end;
-- Verifica che l'anticipo non sia collegato a missione
--  if aAnticipo.fl_associato_missione = 'Y' then
--   IBMERR001.RAISE_ERR_GENERICO('Anticipo '||aPgAnticipo||' già collegato a missione. Impossibile rimborsarlo completamente');
--  end if;
  rimborsoAnticipo(aAnticipo, aUser, aTSNow, aEsercizioScrivania);
 end;
END;
