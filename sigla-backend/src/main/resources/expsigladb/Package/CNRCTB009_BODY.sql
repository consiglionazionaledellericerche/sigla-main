--------------------------------------------------------
--  DDL for Package Body CNRCTB009
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB009" as

function checkTuttiEserciziChiusi(lCds unita_organizzativa%rowtype, aEsercizio number) return boolean is
lNumEsercizioNonChiusi number;
begin
	 select count(*)
	 into lNumEsercizioNonChiusi
	 from esercizio
	 where cd_cds <> lCds.cd_unita_organizzativa
	 and   esercizio = aEsercizio
	 and   st_apertura_chiusura <> 'C';

	 if lNumEsercizioNonChiusi > 0 then
	 	return false;
	 else
	 	return true;
	 end if;
end;

function esisteManNonRiscontrati(aCds unita_organizzativa%rowtype, aEsercizio number) return boolean is
lNumMandatiNonPagati number;
lNumMandatiNonPagatiCnr number;
begin
	 -- un mandato risulta essere in stato riscontrato se risulta Incassata = P
	 -- questa query conta i mandati propri del cds in esame
	 -- e i mandati che il cds in esame ha fatto verso il cnr
	 select sum(decode(cd_cds_origine,CD_CDS,1,0)) verso_cds, sum(decode(cd_cds_origine,CD_CDS,0,1)) verso_cnr
	 into lNumMandatiNonPagati, lNumMandatiNonPagatiCnr
	 from mandato
	 where  CD_CDS_origine = aCds.cd_unita_organizzativa
	 and    ESERCIZIO = aEsercizio
	 and    STATO <> 'P'
	 and    STATO <> 'A';

	 if lNumMandatiNonPagati > 0 or lNumMandatiNonPagatiCnr > 0  then
	 	return true;
	 else
	 	return false;
	 end if;
end;

function esisteRevNonRiscontrate(aCds unita_organizzativa%rowtype, aEsercizio number ) return boolean is
lNumReversaliNonPagate number;
lNumReversaliNonPagateCnr number;
begin
	 -- una reversale risulta essere in stato riscontrato se risulta Incassata = P
	 -- questa query conta le reversali proprie del cds in esame
	 -- e le reversali che il cds in esame ha fatto verso il cnr
	 select sum(nvl(decode(cd_cds_origine,CD_CDS,1,0),0)) verso_cds, sum(nvl(decode(cd_cds_origine,CD_CDS,0,1),0)) verso_cnr
	 into lNumReversaliNonPagate, lNumReversaliNonPagateCnr
	 from reversale
	 where  CD_CDS_origine = aCds.cd_unita_organizzativa
	 and	ESERCIZIO = aEsercizio
	 and    STATO <> 'P'
	 and    STATO <> 'A';

	 if lNumReversaliNonPagate > 0 or lNumReversaliNonPagateCnr>0 then
	 	return true;
	 else
	 	return false;
	 end if;
end;

function esisteObbNonRiscontrate(aCds unita_organizzativa%rowtype, aEsercizio number) return number is
lNumObbNonRiscontrate number;
begin
	 -- in questo controllo dobbiamo ricoradre che esiste la pre condizione
	 -- che tutti i mandati legati al cds sono in SICURAMENTE o in stato pagato
	 -- o in stato annullato (function esisteMandatiNonRiscontrati), quindi
	 -- per sollevare un eccezione basta che esista un scadenza per cui
	 -- risulta IM_ASSOCIATO_DOC_CONTABILE <> IM_SCADENZA,
	 -- perchè per tutte le scadenze pe cui IM_ASSOCIATO_DOC_CONTABILE = IM_SCADENZA
	 -- implica che esiste un mandato a compertura totale e siccome vale la pre condizione
	 -- sui mandati, questo mandato risulta pagato.
	 -- recuperiamo tutte le obbligazioni che non sono riportate e che
	 -- non risultano STORNATE che non hanno importo associato a doc contabile
	 -- diverso da importo scadenza se esiste almeno una di queste
	 -- si solleva un eccezione

	 select count(*)
	 into lNumObbNonRiscontrate
	 from obbligazione obb, obbligazione_scadenzario obbs
	 where obb.cd_cds = obbs.cd_cds
	 and   obb.esercizio = obbs.esercizio
	 and   obb.esercizio_originale = obbs.esercizio_originale
	 and   obb.pg_obbligazione = obbs.pg_obbligazione
	 and   obb.cd_cds_origine = aCds.cd_unita_organizzativa
	 and   obb.esercizio = aEsercizio
	 and   obb.stato_obbligazione = 'P'
	 and   obb.riportato = 'N'
	 and   obb.esercizio = obb.Esercizio_Competenza
	 And   OBB.PG_OBBLIGAZIONE > 0;

	 if lNumObbNonRiscontrate > 0 then
	 	return 1;
		-- Esistono Obbligazioni Provvisorie
	 end if;

	 select count(*)
	 into lNumObbNonRiscontrate
	 from obbligazione obb, obbligazione_scadenzario obbs
	 where obb.cd_cds = obbs.cd_cds
	 and   obb.esercizio = obbs.esercizio
	 and   obb.esercizio_originale = obbs.esercizio_originale
	 and   obb.pg_obbligazione = obbs.pg_obbligazione
	 and   obb.cd_cds_origine = aCds.cd_unita_organizzativa
	 and   obb.esercizio = aEsercizio
	 and   obb.stato_obbligazione <> 'S'
	 and   obb.riportato = 'N'
	 and   obbs.IM_SCADENZA <> obbs.IM_ASSOCIATO_DOC_CONTABILE
	 And   OBB.PG_OBBLIGAZIONE > 0;

	 if lNumObbNonRiscontrate > 0 then
	 	return 2;
	 else
	 	return 0;
	 end if;
end;

function esisteAccNonRiscontrate(aCds unita_organizzativa%rowtype, aEsercizio number) return boolean is
lNumAccNonRiscontrate number;
begin
	 -- in questo controllo dobbiamo ricoradre che esiste la pre condizione
	 -- che tutte le reversali legate al cds sono in SICURAMENTE o in stato pagato
	 -- o in stato annullato (function esisteRevNonRiscontrati), quindi
	 -- per sollevare un eccezione basta che esista un scadenza per cui
	 -- risulta IM_ASSOCIATO_DOC_CONTABILE <> IM_SCADENZA,
	 -- perchè per tutte le scadenze pe cui IM_ASSOCIATO_DOC_CONTABILE = IM_SCADENZA
	 -- implica che esiste una reversale a compertura totale e siccome vale la pre condizione
	 -- sulle reversali, questa reversale risulta pagata.
	 -- recuperiamo tutte gli accertamenti che non sono riportati e che
	 -- non risultano STORNATI che non hanno importo associato a doc contabile
	 -- diverso da importo scadenza se esiste almeno uno di questi
	 -- si solleva un eccezione

	 select count(*)
	 into lNumAccNonRiscontrate
	 from accertamento acc, accertamento_scadenzario accs
	 where acc.cd_cds = accs.cd_cds
	 and   acc.esercizio = accs.esercizio
	 and   acc.esercizio_originale = accs.esercizio_originale
	 and   acc.pg_accertamento = accs.pg_accertamento
	 and   acc.cd_cds_origine = aCds.cd_unita_organizzativa
	 and   acc.esercizio = aEsercizio
	 and   acc.DT_CANCELLAZIONE is null
	 and   acc.riportato = 'N'
	 and   accs.IM_SCADENZA <> accs.IM_ASSOCIATO_DOC_CONTABILE;

	 if lNumAccNonRiscontrate >0 then
	 	return true;
	 else
	 	return false;
	 end if;
end;

function esisteSospesoNonAssociato(aCds unita_organizzativa%rowtype, aEsercizio number) return boolean is
lNumSospesiNonAssociati number;
begin
	 select count(*)
	 into lNumSospesiNonAssociati
	 from sospeso
	 where cd_cds = aCds.cd_unita_organizzativa
	 and   esercizio = aEsercizio
	 and   cd_sospeso_padre is not null
	 and   fl_stornato ='N'
	 and   im_associato <> im_sospeso;

	 if lNumSospesiNonAssociati > 0 then
	 	return true;
	 else
	 	return false;
	 end if;
end;

-- =================================================================================================
-- Controllo esistenza di lettere di pagamento estero non associate a sospeso
-- =================================================================================================
FUNCTION esisteLetteraNonAssociata
   (aCds unita_organizzativa%rowtype,
    aEsercizio number
   ) return boolean is

lNumSospesiNonAssociati number;

BEGIN

   SELECT count(*) into lNumSospesiNonAssociati
   FROM   lettera_pagam_estero a
   WHERE  a.cd_cds = aCds.cd_unita_organizzativa and
          a.esercizio = aEsercizio and
          a.cd_sospeso is null and
          (
             (EXISTS
                (SELECT 1
                 FROM   fattura_passiva b
                 WHERE  B.cd_cds = A.cd_cds AND
                        B.esercizio_lettera = A.esercizio AND
                        B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                        B.pg_lettera  = A.pg_lettera AND
                        B.stato_cofi != 'A')
             )
           OR
             (EXISTS
                (SELECT 1
                 FROM   documento_generico c
                 WHERE  C.cd_cds = A.cd_cds AND
                        C.esercizio_lettera = A.esercizio AND
                        C.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                        C.pg_lettera  = A.pg_lettera AND
                        C.stato_cofi != 'A')
             )
          );

   if lNumSospesiNonAssociati > 0 then
      return true;
   else
      return false;
   end if;
end esisteLetteraNonAssociata;

function esisteInvetarioAperto(aCds unita_organizzativa%rowtype, aEsercizio number) return boolean is
lNumInvetariAperti number;
begin
	 select count(*)
	 into lNumInvetariAperti
	 from ass_inventario_uo ass, inventario_ap_ch inv
	 where ass.pg_inventario = inv.pg_inventario
	 and   ass.cd_cds = aCds.cd_unita_organizzativa
	 and   inv.esercizio = aEsercizio
	 and   inv.stato ='A';

	 if lNumInvetariAperti > 0 then
	 	return true;
	 else
	 	return false;
	 end if;
end;

function esisteFondoAperto(aCds unita_organizzativa%rowtype,aEsercizio number) return boolean is
lNumFondiNonChiusi number;
begin
	 select count(*)
	 into lNumFondiNonChiusi
	 from fondo_economale
	 where cd_cds = aCds.cd_unita_organizzativa
	 and   esercizio = aEsercizio
	 and   (fl_rev_da_emettere = 'Y' or fl_aperto='Y');

	 if lNumFondiNonChiusi > 0 then
	 	return true;
	 else
	    return false;
	 end if;
end;

-- =================================================================================================
-- Conta le unità organizzative per un dato CdS valide in un esercizio con o senza riferimento al
-- livello
-- =================================================================================================
FUNCTION contaUO
   (aCds unita_organizzativa%ROWTYPE,
    aLivello CHAR,
    aEsercizio NUMBER
   ) RETURN number is

lNumUo number:=0;
lLivello number;

BEGIN

   IF aLivello = '*' THEN

      -- Conta tutte le uo di un CdS indipendentemente dal livello

      SELECT COUNT(*) INTO lNumUo
      FROM   unita_organizzativa
      where  cd_unita_organizzativa like aCds.cd_unita_organizzativa || '.%' AND
             esercizio_inizio <= aEsercizio AND
             esercizio_fine >= aEsercizio;

   ELSE

      -- Conta tutte le uo di un CdS dato un livello

      BEGIN

         lLivello := to_number(aLivello);

         SELECT COUNT(*) INTO lNumUo
         FROM   unita_organizzativa
         WHERE  cd_unita_organizzativa like aCds.cd_unita_organizzativa || '.%' AND
                livello = lLivello AND
                esercizio_inizio <= aEsercizio AND
                esercizio_fine >= aEsercizio;

      EXCEPTION

         WHEN others THEN
              RETURN 0;

      END;

   END IF;

   RETURN lNumUo;

END contaUO;

-- =================================================================================================
-- Controllo che per ogni UO valida nell'esercizio siano state fatte le liquidazioni definitive del
-- mese di dicembre
-- =================================================================================================
FUNCTION tutteLiquidIvaDefinitive
   (aCds unita_organizzativa%rowtype,
    aEsercizio number
   ) RETURN boolean is

lLiquidazioniDefinitiva number;
TipiLiquidazioniDefinitiva number;
lNumUo number;

BEGIN

   lNumUo := contaUo(aCds, '*', aEsercizio);

   SELECT count(*) into lLiquidazioniDefinitiva
   from liquidazione_iva
   where cd_cds = aCds.cd_unita_organizzativa and
         stato = 'D' and
         to_char(dt_inizio,'ddmmyyyy') = '0112'||to_char(aEsercizio) and
         to_char(dt_fine,'ddmmyyyy') ='3112'||to_char(aEsercizio);

 	 SELECT count(distinct tipo_liquidazione) into TipiLiquidazioniDefinitiva
   from liquidazione_iva
   where
   			 cd_cds = aCds.cd_unita_organizzativa and
         stato = 'D' and
         esercizio = aEsercizio;

   -- Siccome per ogni UO devono esistere 3 tipi di liquidazione IVA in stato definitivo
   --(Iva Commerciale, Istituzionale e San Marino) per sapere se tutte le liquidazioni iva del cds sono chiuse,
   -- R.P. 13/10/2011 in seguito alla creazione di nuovi tipi di liquidazione modificato il controllo
   -- devono esistere NumUo * TipiLiquidazioniDefinitiva liquidazioni definitive

   if lLiquidazioniDefinitiva = lNumUo * TipiLiquidazioniDefinitiva then
      return true;
   else
      return false;
   end if;

end tutteLiquidIvaDefinitive;

function tuttiPDGChiusi(aCds unita_organizzativa%rowtype, aEsercizio number) return boolean is
lEsistePdgNonDefinitivo boolean;
lNumPdgNonDefinitivi number;
begin
	 lEsistePdgNonDefinitivo := false;
	 -- Cicliamo su tutti i CDR
	 for lCdr in (select *
			 	  from CDR
				  where cd_unita_organizzativa like aCds.cd_unita_organizzativa || '.%')
	 loop
	 	 lNumPdgNonDefinitivi := 0;
	 	 -- Per ogniCDR verifichiamo il PDG
		 select count(*)
		 into lNumPdgNonDefinitivi
		 from pdg_preventivo
		 where cd_centro_responsabilita = lCdr.cd_centro_responsabilita
		 and   esercizio = aEsercizio
		 and   stato <> 'F';
		 if lNumPdgNonDefinitivi > 0 then
		 	lEsistePdgNonDefinitivo := true;
		 end if;
	 end loop;

	 if lEsistePdgNonDefinitivo then
	 	return false;
	 else
	 	return true;
	 end if;
end;

function tuttiPDGAggChiusi(aCds unita_organizzativa%rowtype, aEsercizio number) return boolean is
lEsistePdgAggNonDefinitivo boolean;
lNumPdgAggNonDefinitivi number;
begin
	 lEsistePdgAggNonDefinitivo := false;
	 -- Cicliamo su tutti i CDR
	 for lCdr in (select *
			 	  from CDR
				  where cd_unita_organizzativa like aCds.cd_unita_organizzativa || '.%'
				  and   livello = 1)
	 loop
	 	 lNumPdgAggNonDefinitivi := 0;
	 	 -- Per ogniCDR verifichiamo il PDG
		 select count(*)
		 into lNumPdgAggNonDefinitivi
		 from pdg_aggregato
		 where cd_centro_responsabilita = lCdr.cd_centro_responsabilita
		 and   esercizio = aEsercizio
		 and   stato <> 'B';
		 if lNumPdgAggNonDefinitivi > 0 then
		 	 lEsistePdgAggNonDefinitivo := true;
		 end if;
	 end loop;

	 if lEsistePdgAggNonDefinitivo then
	 	return false;
	 else
	 	return true;
	 end if;
end;

/*
function getAndLogAvanzoCnr (aEs number, lUser varchar2) return number as
 lAvanzoMemorizzatoE number(15,2):=0;
 lAvanzoMemorizzatoS number(15,2):=0;
 lPgExec number;
 lStartT date;
 lEndT date;
 lStart varchar2(80);
 lEnd varchar2(80);
 lDelta varchar2(80);
 aCds varchar2(30);
 aCdVoceAvE varchar2(50);
 aCdVoceAvS varchar2(50);
 aUOENTE unita_organizzativa%rowtype;
 lAvanzoAmmCnrCalcolato number;
 lAvanzoCassaCnrCalcolato number;
 aREs esercizio%rowtype;
begin

   aUOENTE:=CNRCTB020.GETUOENTE(aEs);
   aCds:=aUOENTE.cd_unita_padre;

   -- estraiamo avanzo amministrazione CNR dalla vista v_avanzo_amm_cnr per il 2003
   begin
    select nvl(im_avanzo,0)
     into lAvanzoAmmCnrCalcolato
     from v_avanzo_amm_cnr
	   where esercizio = aEs;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Impossibile determinare l''avanzo di amministrazione Cnr per esercizio:'||aEs);
   end;
   -- estraiamo avanzo di cassa CNR dalla vista v_disp_cassa_cnr per il 2003
   begin
    select nvl(IM_DISPONIBILTA_CASSA,0)
     into lAvanzoCassaCnrCalcolato
    from v_disp_cassa_cnr
	   where
	            esercizio = aEs
			and cd_cds=aCds;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Impossibile determinare l''avanzo di cassa Cnr per esercizio:'||aEs);
   end;

   -- Leggo in aREs l'esercizio successivo al corrente per verificare se esiste
   begin
    select * into aREs from esercizio where
	  	  cd_Cds = aCds
	and   esercizio = aEs+1
	  for update nowait;
   exception when NO_DATA_FOUND then
	IBMERR001.RAISE_ERR_GENERICO('Esercizio ('||(aEs+1)||') non trovato per l''ENTE');
   end;

   -- Leggo il record dell'esercizio in chiusura
   begin
    select * into aREs from esercizio where
	  	  cd_Cds = aCds
	and   esercizio = aEs
	  for update nowait;
   exception when NO_DATA_FOUND then
	IBMERR001.RAISE_ERR_GENERICO('Esercizio ('||(aEs)||') non trovato per l''ENTE');
   end;

   aCdVoceAvE:=cnrctb015.getVal01PerChiave(aEs+1, gPKCONF, gSKCONFCNR_ENTRATA);
   aCdVoceAvS:=cnrctb015.getVal01PerChiave(aEs+1, gPKCONF, gSKCONFCNR_SPESA);

   if aCdVoceAvS is null or aCdVoceAvE is null then
    IBMERR001.RAISE_ERR_GENERICO('Capitoli avanzo/disavanzo non definiti in CONFIGURAZIONE_CNR per esercizio:'||(aEs+1));
   end if;

   -- estraiamo avanzo cassa Entrata da voce_f_saldi_cmp
   begin
    select (IM_STANZ_INIZIALE_A1 + VARIAZIONI_PIU - VARIAZIONI_MENO)
     into lAvanzoMemorizzatoE
    from voce_f_saldi_cmp
    where
	       cd_cds = aCds
     and   esercizio = aEs+1
     and   ti_appartenenza = gENTE
     and   ti_gestione = gENTRATA
     and   cd_voce = aCdVoceAvE
     and   ti_competenza_residuo = gCOMPETENZA
	for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Previsione capitolo avanzo non trovata per esercizio:'||(aEs+1));
   end;

   begin
    -- estraiamo avanzo cassa Spesa da voce_f_saldi_cmp
    select (IM_STANZ_INIZIALE_A1 + VARIAZIONI_PIU - VARIAZIONI_MENO) * (-1)
     into lAvanzoMemorizzatoS
    from voce_f_saldi_cmp
    where
	       cd_cds = aCds
     and   esercizio = aEs+1
     and   ti_appartenenza = gENTE
     and   ti_gestione = gSPESA
     and   cd_voce = aCdVoceAvS
     and   ti_competenza_residuo = gCOMPETENZA
	for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Previsione disavanzo non trovata per esercizio:'||(aEs+1));
   end;

   -- Inseriamo Testata Log
   lPgExec := ibmutl200.LOGSTART('Procedura per il controllo avanzo del cds ' ||aCds , lUser, null, null);

   -- Inseriamo intestazione dettaglio Log
   lStartT := sysdate;
   lStart:=to_char(lStartT,'YYYYMMDD HH:MI:SS');
   IBMUTL200.logInf(lPgExec,'Controllo Avanzo START  at: '||lStart||' es.'||aEs||' cds.'||aCds,aEs||aCds,'SOC');

   if lAvanzoMemorizzatoE <> 0 then
   	  if lAvanzoMemorizzatoE <> lAvanzoAmmCnrCalcolato then
     	 IBMUTL200.logInf(lPgExec,'Avanzo di amministrazione calcolato =' || to_char(lAvanzoAmmCnrCalcolato)||'  - Avanzo di amministrazione presunto = ' || to_char(lAvanzoMemorizzatoE) ,'Delta = '||to_char(lAvanzoAmmCnrCalcolato - lAvanzoMemorizzatoE) ,'SOCD');
   	  end if;
   else
   	  if lAvanzoMemorizzatoS <> lAvanzoAmmCnrCalcolato then
     	 IBMUTL200.logInf(lPgExec,'Avanzo di amministrazione calcolato =' || to_char(lAvanzoAmmCnrCalcolato)||'  - Avanzo di amministrazione presunto = ' || to_char(lAvanzoMemorizzatoS) ,'Delta = '||to_char(lAvanzoAmmCnrCalcolato - lAvanzoMemorizzatoS) ,'SOCD');
   	  end if;
   end if;
   lEndT:=sysdate;
   lEnd:=to_char(lEndT,'YYYYMMDD HH:MI:SS');
   lDelta:=to_char((lEndT-lStartT)*24*3600,'999999');
   IBMUTL200.logInf(lPgExec,'Controllo Quadratura END at: '||lEnd||' tot exec time(s):'||lDelta||' es.'||aEs||' cds.'||aCds,aEs||aCds,'SOC');

   -- Ritorna l'avanzo di cassa CNR calcolato
   return lAvanzoCassaCnrCalcolato;
end;
*/

/*
function getAndLogAvanzoCds (aCds varchar2, aEs number, lUser varchar2) return number as
 lAvanzoMemorizzatoE number(15,2):=0;
 lAvanzoMemorizzatoS number(15,2):=0;
 lAvanzoCalcolato number(15,2):=0;
 lPgExec number;
 lStartT date;
 lEndT date;
 lStart varchar2(80);
 lEnd varchar2(80);
 lDelta varchar2(80);
 aCdVoceAvE varchar2(50);
 aCdVoceAvS varchar2(50);
 aUOVALIDA unita_organizzativa%rowtype;
 aVUOVALIDA v_unita_organizzativa_valida%rowtype;
 aREs esercizio%rowtype;
begin

   aUOVALIDA:=CNRCTB020.GETUOVALIDA(aEs,aCds);

   if not (aUOVALIDA.fl_cds='Y') then
    IBMERR001.RAISE_ERR_GENERICO('L''uo specificata non è un CDS '||aCds);
   end if;

   -- estraiamo avanzo cassa dalla v_avanzo_cassa_cds per il 2003
   begin
	   select nvl(im_avanzo,0)
	   into lAvanzoCalcolato
	   from v_avanzo_cassa_cds
	   where
	          esercizio = aEs
		  and cd_cds=aCds;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Impossibile determinare l''avanzo di cassa Cds '||aCds||' per esercizio:'||aEs);
   end;

   -- Se il cds non è più valido nell'esercizio successivo esce senza eseguire ulteriori operazioni

   begin
	  select * into aVUOVALIDA from v_unita_organizzativa_valida where
	             esercizio = aEs+1
			 and cd_unita_organizzativa=aCds;
   exception when NO_DATA_FOUND then
	return lAvanzoCalcolato;
   end;

   -- Leggo il record dell'esercizio successivo a quello in chiusura per il cds
   begin
    select * into aREs from esercizio where
	  	  cd_Cds = aCds
	and   esercizio = aEs+1
	  for update nowait;
   exception when NO_DATA_FOUND then
	IBMERR001.RAISE_ERR_GENERICO('Esercizio ('||(aEs+1)||') non trovato per cds:'||aCds);
   end;

   -- Leggo il record dell'esercizio in chiusura
   begin
    select * into aREs from esercizio where
	  	  cd_Cds = aCds
	and   esercizio = aEs
	  for update nowait;
   exception when NO_DATA_FOUND then
	IBMERR001.RAISE_ERR_GENERICO('Esercizio ('||(aEs)||') non trovato per cds:'||aCds);
   end;

   aCdVoceAvE:=cnrctb015.getVal01PerChiave(aEs+1, gPKCONF, gSKCONFCDS_ENTRATA);
   aCdVoceAvS:=cnrctb015.getVal01PerChiave(aEs+1, gPKCONF, gSKCONFCDS_SPESA);

   if aCdVoceAvS is null or aCdVoceAvE is null then
    IBMERR001.RAISE_ERR_GENERICO('Capitoli avanzo/disavanzo non definiti in CONFIGURAZIONE_CNR per esercizio:'||(aEs+1));
   end if;

   -- estraiamo avanzo cassa Entrata da voce_f_saldi_cmp
   begin
    select (IM_STANZ_INIZIALE_A1 + VARIAZIONI_PIU - VARIAZIONI_MENO)
     into lAvanzoMemorizzatoE
    from voce_f_saldi_cmp
    where
	       cd_cds = aCds
     and   esercizio = aEs+1
     and   ti_appartenenza = gNONENTE
     and   ti_gestione = gENTRATA
     and   cd_voce = aCdVoceAvE
     and   ti_competenza_residuo = gCOMPETENZA
	for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Previsione avanzo non trovata per esercizio:'||(aEs+1));
   end;

   begin
    -- estraiamo avanzo cassa Spesa da voce_f_saldi_cmp
    select (IM_STANZ_INIZIALE_A1 + VARIAZIONI_PIU - VARIAZIONI_MENO) * (-1)
     into lAvanzoMemorizzatoS
    from voce_f_saldi_cmp
    where
	       cd_cds = aCds
     and   esercizio = aEs+1
     and   ti_appartenenza = gNONENTE
     and   ti_gestione = gSPESA
     and   cd_voce = aCdVoceAvS
     and   ti_competenza_residuo = gCOMPETENZA
	for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Previsione disavanzo non trovata per esercizio:'||(aEs+1));
   end;

   -- Inseriamo Testata Log
   lPgExec := ibmutl200.LOGSTART('Procedura per il controllo avanzo del cds ' ||aCds , lUser, null, null);

   -- Inseriamo intestazione dettaglio Log
   lStartT := sysdate;
   lStart:=to_char(lStartT,'YYYYMMDD HH:MI:SS');
   IBMUTL200.logInf(lPgExec,'Controllo Avanzo START  at: '||lStart||' es.'||aEs||' cds.'||aCds,aEs||aCds,'SOC');

   if lAvanzoMemorizzatoE <> 0 then
   	  if lAvanzoMemorizzatoE <> lAvanzoCalcolato then
     	 IBMUTL200.logInf(lPgExec,'Avanzo di cassa calcolato =' || to_char(lAvanzoCalcolato)||'  - Avanzo di cassa presunto = ' || to_char(lAvanzoMemorizzatoE) ,'Delta = '||to_char(lAvanzoCalcolato - lAvanzoMemorizzatoE) ,'SOCD');
   	  end if;
   else
   	  if lAvanzoMemorizzatoS <> lAvanzoCalcolato then
     	 IBMUTL200.logInf(lPgExec,'Avanzo di cassa calcolato =' || to_char(lAvanzoCalcolato)||'  - Avanzo di cassa presunto = ' || to_char(lAvanzoMemorizzatoS) ,'Delta = '||to_char(lAvanzoCalcolato - lAvanzoMemorizzatoS) ,'SOCD');
   	  end if;
   end if;
   lEndT:=sysdate;
   lEnd:=to_char(lEndT,'YYYYMMDD HH:MI:SS');
   lDelta:=to_char((lEndT-lStartT)*24*3600,'999999');
   IBMUTL200.logInf(lPgExec,'Controllo Quadratura END at: '||lEnd||' tot exec time(s):'||lDelta||' es.'||aEs||' cds.'||aCds,aEs||aCds,'SOC');

   -- Ritorna l'avanzo di cassa CDS calcolato
   return lAvanzoCalcolato;
end;
*/

/*
procedure scriviImCassaInizialeCnr (aEs number, aAvanzoIniziale number, aUser varchar2) as
 lCassaIniziale number (15,2) := 0;
 aCds varchar2(30);
 aUOENTE unita_organizzativa%rowtype;
 aREs esercizio%rowtype;
 aTSNow date;
begin
     aTSNow:=sysdate;
     aUOENTE:=CNRCTB020.GETUOENTE(aEs);
     aUOENTE:=CNRCTB020.GETUOENTE(aEs+1);
	 aCds:=aUOENTE.cd_unita_padre;
	 begin
      select * into aREs from esercizio where
	 	 cd_Cds = aCds
	  and   esercizio = aEs + 1
	  for update nowait;
	 exception when NO_DATA_FOUND then
	  IBMERR001.RAISE_ERR_GENERICO('Esercizio ('||(aEs+1)||') successivo a quello in chiusura non trovato per cds:'||aCds);
	 end;

	 -- scrivi
	 --update esercizio
	 --set
	  --IM_CASSA_INIZIALE = aAvanzoIniziale,
	  --utuv=aUser,
	  --duva=aTSNow,
	  --pg_ver_rec=pg_ver_rec+1
	 --where
	        --cd_Cds = aCds
	  --and   esercizio = aEs + 1;
end;
*/

/*
procedure scriviImCassaInizialeCds (aCds varchar2, aEs number, aAvanzoIniziale number, aUser varchar2) as
 lCassaIniziale number (15,2) := 0;
 aUOVALIDA unita_organizzativa%rowtype;
 aVUOVALIDA v_unita_organizzativa_valida%rowtype;
 aREs esercizio%rowtype;
 aTSNow date;
begin
	 aTSNow:=sysdate;
     aUOVALIDA:=CNRCTB020.GETUOVALIDA(aEs,aCds);

     if not (aUOVALIDA.fl_cds='Y') then
      IBMERR001.RAISE_ERR_GENERICO('L''uo specificata non è un CDS '||aCds);
     end if;

	 -- Se il cds non è più valido nell'esercizio successivo esce senza eseguire operazioni
	 begin
	  select * into aVUOVALIDA from v_unita_organizzativa_valida where
	             esercizio = aEs+1
			 and cd_unita_organizzativa=aCds;
     exception when NO_DATA_FOUND then
	  return;
	 end;

	 begin
      select * into aREs from esercizio where
	 	 cd_Cds = aCds
	  and   esercizio = aEs + 1
	  for update nowait;
	 exception when NO_DATA_FOUND then
	  IBMERR001.RAISE_ERR_GENERICO('Esercizio ('||(aEs+1)||') successivo a quello in chiusura non trovato per cds:'||aCds);
	 end;

	 -- scrivi
	 --update esercizio
	 --set
	  --IM_CASSA_INIZIALE = aAvanzoIniziale,
	  --utuv=aUser,
	  --duva=aTSNow,
	  --pg_ver_rec=pg_ver_rec+1
	 --where
	        --cd_Cds = aCds
	  --and   esercizio = aEs + 1;
end;
*/

-- Procedura  di gestione dell'avanzo di cassa e amministrazione per CNR e cassa per il CDS
/* 02.07.2009 STANI
procedure gestioneAvanzo(aCds varchar2, aEs number, aUser varchar2) is
	aAvanzoCassaCnr number;
	aAvanzoCassaCds number;
    aUO unita_organizzativa%rowtype;

begin

    aUO:=CNRCTB020.GETUOVALIDA(aEs,aCds);

    -- Gestione degli avanzi di cassa e amministrazione CNR e cassa CDS
    aAvanzoCassaCnr:=0;
    aAvanzoCassaCds:=0;

    if aUO.cd_tipo_unita = CNRCTB020.TIPO_ENTE then
	 aAvanzoCassaCnr := getAndLogAvanzoCnr (aEs, aUser);
         scriviImCassaInizialeCnr (aEs,aAvanzoCassaCnr, aUser);
    else
         aAvanzoCassaCds := getAndLogAvanzoCds (aCds, aEs, aUser);
         scriviImCassaInizialeCds (aCds, aEs, aAvanzoCassaCds, aUser);
    end if;
end;
*/

procedure checkEsercizioChiusura(aEs number, aCds varchar2, aUser varchar2) is
    lEnte boolean;
    lCds unita_organizzativa%rowtype;
	aAvanzoCassaCnr number;
	aAvanzoCassaCds number;
    aUO unita_organizzativa%rowtype;
begin
	-- selezioniamo la UO dalla tabella relativa
	select *
	into lCds
	from unita_organizzativa
	where cd_unita_organizzativa = aCds
	and fl_cds = 'Y';

	-- controlliamo se si tratta dell'ente
	if lCds.cd_tipo_unita <> cnrctb020.TIPO_ENTE then
		lEnte := false;
	else
		lEnte := true;
	end if;

	-- Se il cds è ente e non risulta che gli esercizi di tutti i suoi cds siano chiusi
	-- allora viene sollevata un eccezione
	if lEnte and not checkTuttiEserciziChiusi(lCds,aEs) then
	   ibmerr001.RAISE_ERR_GENERICO('Non risultano chiusi definitivamente gli esercizi di tutti i CDS ');
	end if;

	-- Controlliamo se tutti i mandati del cds in esame risultano riscontrati
	-- un mandato risulta riscontrato se ad esso è legato un sospeso, ma ciò
	-- implica che lo stato del mandato sia P = Pagato
	-- Se risulta che un mandato prorio o verso il CNR non sia riscontrato
	-- allora si solleva un eccezione
	if esisteManNonRiscontrati(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Non tutti i mandati risultano riscontrati ');
	end if;

	-- Controlliamo se tutte le reversali del cds in esame risultano riscontrate
	-- una reversale risulta riscontrata se ad essa è legato un sospeso, ma ciò
	-- implica che lo stato della reversale sia I = Incassata
	-- Se risulta che una reversale proria o verso il CNR non sia riscontrata
	-- allora si solleva un eccezione
	if esisteRevNonRiscontrate(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Non tutte le reversali risultano riscontrate ');
	end if;

	-- Controlliamo che se i fondi economali sono aperti
	-- Se esistono solleviamo eccezione
	if esisteFondoAperto(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Esistono Fondi Economali non chiusi definitivamente');
	end if;

	-- Controlliamo che tutte le obbligazioni non ancora riportate siano legate
	-- a documenti amministrativi e che inoltre risultino riscontrate
	-- un obbligazione risulta riscontrata se associata ad un mandato in stato Pagato
	if esisteObbNonRiscontrate(lCds,aEs) = 2 then
      If cnrutil.isLabelObbligazione() Then
   	    ibmerr001.RAISE_ERR_GENERICO('Esistono obbligazioni non riscontrate nell''esercizio in esame e non riportate nell''esercizio successivo');
   	  Else
   	    ibmerr001.RAISE_ERR_GENERICO('Esistono impegni non riscontrati nell''esercizio in esame e non riportati nell''esercizio successivo');
      End If;
	end if;
	if esisteObbNonRiscontrate(lCds,aEs) = 1 then
      If cnrutil.isLabelObbligazione() Then
   	    ibmerr001.RAISE_ERR_GENERICO('Esistono obbligazioni provvisorie nell''esercizio in esame');
   	  Else
   	    ibmerr001.RAISE_ERR_GENERICO('Esistono impegni provvisori nell''esercizio in esame');
   	  End If;
	end if;

	-- Controlliamo che tutte gli accertamenti non ancora riportati siano legati
	-- a documenti amministrativi e che inoltre risultino riscontrati
	-- un accertamento risulta riscontrato se associato ad un mandato in stato Pagato
	if esisteAccNonRiscontrate(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Esistono accertamenti non riscontrati nell''esercizio in esame e non riportate nell''esercizio successivo');
	end if;

	-- Controlliamo che se esistono sospesi figli non stornati e non associati
	-- completamente a mandati/reversali
	-- Se esistono solleviamo eccezione
	if esisteSospesoNonAssociato(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Esistono Sospesi non associati completamente a mandati/reversali');
	end if;

	-- Controlliamo che se esistono lettere pagamento estero che non hanno
	-- sospeso associato
	-- Se esistono solleviamo eccezione
	if esisteLetteraNonAssociata(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Esistono Lettere Pagamento Estero non associati a sospesi');
	end if;

	-- Controlliamo che se gli invetari sono aperti
	-- Se esistono solleviamo eccezione
	if esisteInvetarioAperto(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Esistono Inventari aperti');
	end if;

	-- Controlliamo che se le liquidazioni iva sono state registrate
	-- Se non esistono registrate solleviamo eccezione
	if not tutteLiquidIvaDefinitive(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Esistono Liquidazioni IVA non definitive');
	end if;

	-- Controlliamo che tutti i piani di gestione del cds
	-- devono essere chiusi definitivamente stato = F (in questo caso
	-- dobbiamo considerare tutti i piani di gestione
	-- relativi ai tutti CDR legati al nostro cds )
	if not tuttiPDGChiusi(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Esistono Piani di gestione non chiusi definitivamente');
	end if;

	-- Controlliamo che tutti i piani di gestione aggregati del cds
	-- devono essere chiusi definitivamente stato = B (in questo caso
	-- dobbiamo considerare tutti i piani di gestione aggregati
	-- relativi ai soli CDR di primo livello legati al nostro cds)
	if not tuttiPDGAggChiusi(lCds,aEs) then
   	  ibmerr001.RAISE_ERR_GENERICO('Esistono Piani di gestione Aggregati non chiusi definitivamente');
	end if;

-- 02.07.2009 STANI	gestioneAvanzo(aCds, aEs, aUser);
end;

end;
