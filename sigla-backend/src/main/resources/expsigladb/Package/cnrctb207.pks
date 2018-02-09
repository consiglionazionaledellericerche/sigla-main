CREATE OR REPLACE package CNRCTB207 as
--
-- CNRCTB207 - Package COGE-COAN STIPENDI
-- Date: 14/07/2006
-- Version: 2.13
--
-- Package per la gestione delle scritture COGE/COAN Stipendi
--
-- Dependency: CNRCTB 200/204 IBMERR 001
--
-- History:
--
-- Date: 22/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 23/09/2002
-- Version: 1.1
-- Gestione del mese
--
-- Date: 23/09/2002
-- Version: 1.2
-- Gestione aggiornamento tabella stipendi_coan
--
-- Date: 30/09/2002
-- Version: 1.3
-- Modificato interfaccia per passare direttamente il codice dell'UO
-- al posto del codice del CDR
--
-- Date: 16/10/2002
-- Version: 1.4
-- Rafforzati controlli su carico/scarico analitico
--
-- Date: 16/10/2002
-- Version: 1.5
-- Aggiunta contabilizzazione economica stipendi (prima scrittura)
--
-- Date: 18/10/2002
-- Version: 1.6
-- Fix numerazione scrittura COGE
--
-- Date: 22/10/2002
-- Version: 1.7
-- Fix su campi scrittura economica stipendi
--
-- Date: 23/10/2002
-- Version: 1.8
-- Fix date competenza coge movimenti principali scrittura economica
--
-- Date: 25/10/2002
-- Version: 1.9
-- Documentazione
--
-- Date: 27/10/2002
-- Version: 2.0
-- Recupero del corretto conto di contropartita cliente/fornitore
--
-- Date: 04/11/2002
-- Version: 2.1
-- Fix recupero UO in annulamento ribaltamento PDG in gestione
-- Controllo configurazione completa CDP del mese per UO in processo
--
-- Date: 11/11/2002
-- Version: 2.2
-- Lock su Costo del dipendente
--
-- Date: 13/11/2002
-- Version: 2.3
-- Fix su stipendi in analiticica
--
-- Date: 18/11/2002
-- Version: 2.4
-- Spostamento costanti in CNRCTB200 da CNRCTB205 e riorganizzazione CNRCTB205
--
-- Date: 11/12/2002
-- Version: 2.5
-- Fix messaggio errato
--
-- Date: 11/12/2002
-- Version: 2.6
-- Fix estrazione cdr RUO
--
-- Date: 16/07/2003
-- Version: 2.7
-- Modifica interfaccia trova conto anag
--
-- Date: 18/07/2003
-- Version: 2.8
-- Revisione per gestione dei cori negativi: richiamati metodi standard del package 204
-- Aggiunto controllo apertura esercizio+revisione pre-post
--
-- Date: 05/08/2003
-- Version: 2.9
-- Fix errori minori per test: aggiunti controlli di non ripetizione
-- Il terzo dei movimenti della scrittura economica ? stato unifornmato a quello delle obbligazioni
--
-- Date: 13/11/2003
-- Version: 2.10
-- Aggiunti i controlli su esercizio corrente e precedente per l'effettuazione delle operazioni
--
-- Date: 23/01/2004
-- Version: 2.11
-- Aggiunta gestione dei rotti in scarico dei costi del personale in gestione sull'analitica
--
-- Date: 01/03/2004
-- Version: 2.12
-- Evidenziati nella scrittura economica degli stipendi i soli carichi ENTE
--
-- Date: 14/07/2006
-- Version: 2.13
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

-- Functions e Procedures

-- Generazione delle scritture analitiche per scarico dei costi del personale mensili in gestione
--
-- Mensilmente dopo aver configurato come per la fase prvisionale la ripartizione dei costi del personale in gestione
-- viene effettuato lo scarico in analitica (Piano di Gestione in Gestione)
-- Tale scarico viene fatto per Unit? Organizzativa (una scrittura analitica per UO)
--
-- pre-post-name: Esercizio non aperto
-- pre: L'esercizio contabile in processo non risulta aperto
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello in processo non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Scarico su piano di gestione in gestione gi? effettuato
-- pre: Scarico del mese in processo su piano di gestione in gestione gi? effettuato
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Scarico mese precedente non ancora effetutato
-- pre: Il mese in processo > 1 e il mese precedente non ? ancora stato scaricato
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Configurazione di scarico non completa
-- pre: La configurazione di scarico per il mese in processo NON ? completa
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Creazione della scrittura analitica
-- pre: Nessun'altra precondizione verificata
-- post: Viene creata una scrittura analitica con la seguente regola:
--       1. Origine scrittura = 'STIPENDI'
--       2. N. movimenti corrispondenti alla spaccatura analitica relativa ai costi stipendiali per il mese in processo
--          all'interno dell'UO: il capitolo economico viene recuperato dall'associazione tra conto finanziario ed economico
--
-- Parametri:
-- aEs -> esercizio contabile
-- aMese -> numero del mese di riferimento
-- aCdUO -> codice UO che scarica il PDG in GESTIONE
-- aUser -> utente che effettua la modifica

 procedure scaricaCDPSuPdgGestione(aEsercizio number, aMese number, aCdUO varchar2, aUser varchar2);

-- Annulamento delle scritture analitiche per scarico dei costi del personale mensili in gestione
--
-- pre-post-name: Esercizio non aperto
-- pre: L'esercizio contabile in processo non risulta aperto
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello in processo non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name:  Check che il mese corrente sia stato scaricato
-- pre: Il mese in processo non ? ancora stato scaricato sul piano di gestione in gestione per l'UO specificata
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Scarico mese successivo a quello in processo gi? effettuato per l'UO specificata
-- pre: Il mese in processo < 12 e il mese successivo ? gi? stato scaricato
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Annullamneto scarico piano di gestione in gestione
-- pre: Nessun'altra precondizione verificata
-- post: Viene creata una scrittura analitica di storno della scrittura precedentementemente effetuata.
--
-- Parametri:
-- aEs -> esercizio contabile
-- aMese -> numero del mese di riferimento
-- aCdUO -> codice UO che scarica il PDG in GESTIONE
-- aUser -> utente che effettua la modifica

 procedure annullaScaricaCDPSuPdgGestione(aEsercizio number, aMese number, aCdUO varchar2, aUser varchar2);

-- Generazione della prima scrittura economica per liquidazione mensile degli stipendi
--
-- pre-post-name:  Check liquidazione mensile stipendi gi? effettuata per il mese in processo
-- pre: La liquidazione mensileIl mese in processo non ? ancora stato effettuata
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio finanziario non aperto per il CDS stipendi
-- pre: L'esercizio non ? aperto per il CDS stipendi
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello in processo non ? chiuso definitivamente per il CDS stipendi
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Creazione della prima scrittura economica
-- pre: Nesun'altra precondizione verificata
-- post: Viene creata una scrittura economica con le seguenti caratteristiche:
--   1. nx movimenti di costo corrispondenti alle n scadenze di obbligazione processate in finanziaria per il mese aggregate
--      per conto economico, sezione e data inizio e fine competenza (sempre = primo e ultimo giorno del mese in processo).
--      l'importo di ogni movimento ? calcolato distribuendo l'importo del mandato stipendi meno i cori ente (sommati algebricamente) con
--      le percentuali rispetto al totale del mandato di ogni singola scadenza finanziaria.
--      Gli eventuali rotti vengono aggiunti all'ultimo movimento
--   2. mx movimenti relativi agli m cori ente (come nella contabilizzazione economica del compenso)  aggregati
--      per conto economico, sezione e data inizio e fine competenza ereditati dal compenso stipendi
--	 3. 1 movimento di contropartita sul terzo utilizzato nel mandato principale stipendi che chiude la scrittura
--
-- Parametri:
-- aEs -> esercizio contabile
-- aMese -> numero del mese di riferimento
-- aUser -> utente che effettua la modifica

 procedure regStipendiCOGE(aEs number, aMese number, aUser varchar2);

 procedure regStipendiCOGE(aEs number, aUser varchar2);

 procedure ins_ASS_CDP_MESE_ROUND (aDest ASS_CDP_MESE_ROUND%rowtype);

end;
/


CREATE OR REPLACE package body CNRCTB207 is
 procedure annullaScaricaCDPSuPdgGestione(aEsercizio number, aMese number, aCdUO varchar2, aUser varchar2) is
  aTSNow date;
  aScrittura scrittura_analitica%rowtype;
  aListaMovimenti CNRCTB200.movAnalitList;
  aUO unita_organizzativa%rowtype;
  aStipCoan stipendi_coan%rowtype;
  aOldScrittura scrittura_analitica%rowtype;
 begin
  aTSNow:=sysdate;

  aUO:=CNRCTB020.GETUOVALIDA(aEsercizio,aCdUO);

  -- L'esercizio contabile deve essere aperto per il cds origine del documento
  if not CNRCTB008.ISESERCIZIOAPERTO(aEsercizio, aUO.cd_unita_padre)
  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio contabile ('||aEsercizio||') non ? aperto per il cds:'||aUO.cd_unita_padre);
  end if;

  -- L'esercizio economico precedente deve essere chiuso definitivamente
  if not (CNRCTB200.ISCHIUSURACOEPDEF(aEsercizio-1, aUO.cd_unita_padre)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente non ? chiuso definitivamente per il cds: '||aUO.cd_unita_padre);
  end if;

  -- Check che il mese corrente sia stato scaricato
  begin
   select * into aStipCoan from stipendi_coan where
        esercizio = aEsercizio
    and mese= aMese
    and cd_cds = aUO.cd_unita_padre
    and cd_uo = aUO.cd_unita_organizzativa for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scarico dei costi stipendiali su piano di gestione in gestione non ancora effettuato.');
  end;

  -- Check che il mese successivo non sia gi? stato scaricato
  if aMese < 13 then
   begin
    select * into aStipCoan from stipendi_coan where
         esercizio = aEsercizio
     and mese= aMese + 1
     and cd_cds = aUO.cd_unita_padre
     and cd_uo = aUO.cd_unita_organizzativa;
    IBMERR001.RAISE_ERR_GENERICO('Scarico dei costi stipendiali su piano di gestione in gestione gi? effettuato per mese successivo al corrente.');
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;

  aScrittura.esercizio := aEsercizio;
  aScrittura.cd_cds := aUO.cd_unita_padre;
  aScrittura.cd_unita_organizzativa := aUO.cd_unita_organizzativa;
  aScrittura.pg_scrittura := aStipCoan.pg_scrittura_an;
  CNRCTB200.getScritturaANLock(aScrittura, aListaMovimenti);
  aOldScrittura:=aScrittura;
  CNRCTB200.CREASCRITTSTORNOCOAN(aScrittura,aListaMovimenti,aUser,aTSNow);

  -- Cleanup degli arrotondamenti
  for aAssCdpMeseRnd in (select * from ass_cdp_mese_round where
       esercizio = aOldScrittura.esercizio
   and cd_cds = aOldScrittura.cd_cds
   and cd_unita_organizzativa = aOldScrittura.cd_unita_organizzativa
   and pg_scrittura = aOldScrittura.pg_scrittura
   and mese = aMese
   for update nowait) loop
    null;
  end loop;
  delete from ass_cdp_mese_round where
       esercizio = aOldScrittura.esercizio
   and cd_cds = aOldScrittura.cd_cds
   and cd_unita_organizzativa = aOldScrittura.cd_unita_organizzativa
   and pg_scrittura = aOldScrittura.pg_scrittura
   and mese = aMese;

  -- Leggo il CDR

  -- Lettura lockante e aggiornamento dello stato di scarico in ASS_CDP_LA
  for aAssCdpLa in (select * from ass_cdp_la where
       esercizio = aEsercizio
   and mese = aMese
   and cd_centro_responsabilita in (
    select cd_centro_responsabilita from cdr where
	   cd_unita_organizzativa = aUO.cd_unita_organizzativa
   )
   for update nowait
  ) loop
   null;
  end loop;

  update ass_cdp_la set
      stato = CNRCTB060.STATO_CDP_NON_SCARICATO
	 ,utuv=aUser
	 ,duva=aTSNow
	 ,pg_ver_rec=pg_ver_rec+1
  where
       esercizio = aEsercizio
   and cd_centro_responsabilita in (
    select cd_centro_responsabilita from cdr where
	   cd_unita_organizzativa = aUO.cd_unita_organizzativa
  );

  delete from stipendi_coan where
        esercizio = aEsercizio
    and mese= aMese
    and cd_cds = aUO.cd_unita_padre
    and cd_uo = aUO.cd_unita_organizzativa;

 end;

-- Funzione di accantonamento dei rotti di scarico

 procedure fill_round_table(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype, aVoceEp VOCE_EP%rowtype, aTSNow date, aUser varchar2) is
  aAssCdpMeseRnd ASS_CDP_MESE_ROUND%rowtype;
 begin
    if
	     aASSCDPLA.IM_RND_A1 = 0
	then
     return;
	end if;
	aAssCdpMeseRnd:=null;
    aAssCdpMeseRnd.ESERCIZIO:=aASSCDPLA.ESERCIZIO;
    aAssCdpMeseRnd.CD_CDR_ROOT:=aASSCDPLA.CD_CDR_ROOT;
    aAssCdpMeseRnd.CD_VOCE_EP:=aVoceEp.CD_VOCE_EP;
    aAssCdpMeseRnd.MESE:=aASSCDPLA.MESE;
    begin
	 select * into aAssCdpMeseRnd from ass_cdp_mese_round where
	      ESERCIZIO=aAssCdpMeseRnd.esercizio
      and CD_CDR_ROOT=aAssCdpMeseRnd.cd_cdr_root
      and CD_VOCE_EP=aAssCdpMeseRnd.cd_voce_ep
      and MESE=aAssCdpMeseRnd.MESE
	  for update nowait;
     update ass_cdp_mese_round set
      IM_ARR_A1=IM_ARR_A1+aASSCDPLA.im_rnd_a1
	 where
	      ESERCIZIO=aAssCdpMeseRnd.esercizio
      and CD_CDR_ROOT=aAssCdpMeseRnd.cd_cdr_root
      and CD_VOCE_EP=aAssCdpMeseRnd.cd_voce_ep;
	exception when NO_DATA_FOUND then
     aAssCdpMeseRnd.IM_ARR_A1:=aASSCDPLA.im_rnd_a1;
     aAssCdpMeseRnd.IM_ARR_A1_NON_DISTR:=0;
     aAssCdpMeseRnd.DACR:=aTSNow;
     aAssCdpMeseRnd.UTUV:=aUser;
     aAssCdpMeseRnd.UTCR:=aUser;
     aAssCdpMeseRnd.DUVA:=aTSNow;
     aAssCdpMeseRnd.PG_VER_REC:=1;
     ins_ASS_CDP_MESE_ROUND(aAssCdpMeseRnd);
	end;
 end;

 procedure scaricaCDPSuPdgGestione(aEsercizio number, aMese number, aCdUO varchar2, aUser varchar2) is
  aCdr cdr%rowtype;
  aTSNow date;
  aScrittura scrittura_analitica%rowtype;
  aCDRRUO cdr%rowtype;
  aListaMovimenti CNRCTB200.movAnalitList;
  aMov movimento_coan%rowtype;
  aLA linea_attivita%rowtype;
  aLAROUND linea_attivita%rowtype;
  aUO unita_organizzativa%rowtype;
  aAss ass_ev_voceep%rowtype;
  aVoceEp voce_ep%rowtype;
  aStipCoan stipendi_coan%rowtype;
  aNum number;
  movPosizione number;
  vMese VARCHAR2(2);
 Begin
  If (aMese In(1,2,3,4,5,6,7,8,9,10,11,12)) Then
    If (Length(To_Char(aMese)) = 1) Then
      vMese := '0'||aMese;
    Else
      vMese := aMese;
    End If;
  End If;
  aTSNow:=sysdate;

  -- Leggo l'UO

  aUO:=CNRCTB020.GETUOVALIDA(aEsercizio,aCdUO);

  -- Controllo esercizio

  -- L'esercizio contabile deve essere aperto per il cds origine del documento
  if not CNRCTB008.ISESERCIZIOAPERTO(aEsercizio, aUO.cd_unita_padre)
  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio contabile ('||aEsercizio||') non ? aperto per il cds:'||aUO.cd_unita_padre);
  end if;

  -- L'esercizio economico precedente deve essere chiuso definitivamente
  if not (CNRCTB200.ISCHIUSURACOEPDEF(aEsercizio-1, aUO.cd_unita_padre)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente non ? chiuso definitivamente per il cds: '||aUO.cd_unita_padre);
  end if;

  -- Check di scarico gi? effettuato

  begin
   select * into aStipCoan from stipendi_coan where
        esercizio = aEsercizio
    and mese= aMese
    and cd_cds = aUO.cd_unita_padre
    and cd_uo = aUO.cd_unita_organizzativa
	for update nowait;
    if aStipCoan.pg_scrittura_an is not null then
     IBMERR001.RAISE_ERR_GENERICO('Scarico dei costi stipendiali su piano di gestione in gestione gi? effettuato.');
    end if;
  exception when NO_DATA_FOUND then
   null;
  end;

  -- Check che lo scarico per il mese precedente sia gi? stato effettuato (mese corrente > 1)

  if aMese > 1 then
   begin
    select * into aStipCoan from stipendi_coan where
         esercizio = aEsercizio
     and mese=aMese-1
     and cd_cds = aUO.cd_unita_padre
     and cd_uo = aUO.cd_unita_organizzativa;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Scarico dei costi stipendiali su piano di gestione in gestione non ancora effettuato per il mese precedente');
   end;
  end if;

  -- Lettura lockante e aggiornamento dello stato di scarico in ASS_CDP_LA

  for aAssCdpLaTemp in (select * from ass_cdp_la where
       esercizio = aEsercizio
   and mese = aMese
   and cd_centro_responsabilita in (
    select cd_centro_responsabilita from cdr where
	   cd_unita_organizzativa = aUO.cd_unita_organizzativa
   )
   for update nowait
  ) loop
   null;
  end loop;

  -- Lock di CDP

  for aCDPMese in (
   select a.id_matricola from costo_del_dipendente a where exists (select 1 from ass_cdp_la b where
        b.esercizio = a.esercizio
    and b.mese = a.mese
    and b.id_matricola = a.id_matricola
    and b.cd_centro_responsabilita in (
     select cd_centro_responsabilita from cdr where
	    cd_unita_organizzativa = aUO.cd_unita_organizzativa
    )
   )
   and a.esercizio = aEsercizio
   and a.mese = aMese
   for update of a.id_matricola
  )loop
   null;
  end loop;

  -- Il totale di scarico configurato dei dipendenti dell'UO e 100%  (comprendento le quote verso altre UO accettate)

  select count(*) into aNum from V_CDP_TOT_PRC where
         esercizio = aEsercizio
     and mese = aMese
	 and cd_unita_organizzativa = aUO.cd_unita_organizzativa
	 and (
	         prc_a1 < 100
		  or prc_a2 < 100
		  or prc_a3 < 100
		 );
  if aNum > 0 then
   IBMERR001.RAISE_ERR_GENERICO('La configurazione di scarico dei dipendenti dell''UO '||aUO.cd_unita_organizzativa||' non e'' completa!');
  end if;

  begin
   select * into aCDRRUO from cdr where
         cd_unita_organizzativa = aUO.cd_unita_organizzativa
	 and to_number(cd_proprio_cdr) = 0;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Cdr di primo livello non trovato per UO:'||aUO.cd_unita_organizzativa);
  end;

  for aASSCDPLAB in (
      select * from V_CDP_SPACCATO_CDR_LA_VOCE where
	          esercizio = aEsercizio
		  and mese = aMese
		  and cd_cdr_root = aCDRRUO.cd_centro_responsabilita order by cd_cdr
  ) loop -- Cicla sulle configurazioni di scarico
   select * into aLA from linea_attivita where
           cd_centro_responsabilita = aASSCDPLAB.cd_cdr
	   and cd_linea_attivita = aASSCDPLAB.cd_linea_attivita;
   begin
    select * into aAss from ass_ev_voceep where
         esercizio = aASSCDPLAB.esercizio
     and ti_appartenenza = aASSCDPLAB.ti_appartenenza
     and ti_gestione = aASSCDPLAB.ti_gestione
     and cd_elemento_voce = aASSCDPLAB.cd_elemento_voce;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Associazione tra voce del piano finanziaria ed economica non trovata per voce di spesa cds:'||aASSCDPLAB.cd_elemento_voce);
   end;
   aVoceEp:=CNRCTB002.GETVOCEEP(aAss.esercizio, aAss.cd_voce_ep);

   fill_round_table(aASSCDPLAB, aVoceEp, aTSNow, aUser);

   aMov:=null;
   aMov.CD_CDS:=aUO.cd_unita_padre;
   aMov.ESERCIZIO:=aASSCDPLAB.esercizio;
   aMov.CD_UNITA_ORGANIZZATIVA:=aUO.cd_unita_organizzativa;
   aMov.CD_VOCE_EP:=aVoceEp.cd_voce_ep;
   aMov.SEZIONE:=CNRCTB100.IS_DARE;
   aMov.CD_CENTRO_RESPONSABILITA:=aASSCDPLAB.cd_cdr;
   aMov.IM_MOVIMENTO:=aASSCDPLAB.im_a1;
   aMov.CD_TERZO:=null;
   aMov.CD_FUNZIONE:=aLA.cd_funzione;
   aMov.CD_NATURA:=aLA.cd_natura;
   aMov.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aMov.DS_MOVIMENTO:='Stipendi mese n.'||aMese;
   aMov.DACR:=aTSNow;
   aMov.UTCR:=aUser;
   aMov.DUVA:=aTSNow;
   aMov.UTUV:=aUser;
   aMov.PG_VER_REC:=1;
   aMov.CD_LINEA_ATTIVITA:=aLA.cd_linea_attivita;
   aMov.PG_NUMERO_DOCUMENTO:=null;
   aListaMovimenti(aListaMovimenti.count+1):=aMov;
  end loop;

  -- Distribuzione dei rotti su dettagli creati

  -- Imposto la linea di attivit? da usare per i movimenti dei rotti
  aLAROUND:=aLA;

  for aAssCdpMeseRnd in (select * from ass_cdp_mese_round where
	          esercizio = aEsercizio
		  and cd_cdr_root = aCDRRUO.cd_centro_responsabilita
		  and mese = aMese
		  for update nowait) loop  -- Cicla sulle configurazioni dei ROTTI
   aMov:=null;
   aMov.CD_CDS:=aUO.cd_unita_padre;
   aMov.ESERCIZIO:=aEsercizio;
   aMov.CD_UNITA_ORGANIZZATIVA:=aUO.cd_unita_organizzativa;
   aMov.CD_VOCE_EP:=aAssCdpMeseRnd.cd_voce_ep;
   aMov.SEZIONE:=CNRCTB100.IS_DARE;
   aMov.CD_CENTRO_RESPONSABILITA:=aLAROUND.cd_centro_responsabilita;
   aMov.IM_MOVIMENTO:=round(aAssCdpMeseRnd.im_arr_a1,2);
   aMov.CD_TERZO:=null;
   aMov.CD_FUNZIONE:=aLAROUND.cd_funzione;
   aMov.CD_NATURA:=aLAROUND.cd_natura;
   aMov.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aMov.DS_MOVIMENTO:='Stipendi mese n.'||aMese||' gestione rotti';
   aMov.DACR:=aTSNow;
   aMov.UTCR:=aUser;
   aMov.DUVA:=aTSNow;
   aMov.UTUV:=aUser;
   aMov.PG_VER_REC:=1;
   aMov.CD_LINEA_ATTIVITA:=aLAROUND.cd_linea_attivita;
   aMov.PG_NUMERO_DOCUMENTO:=null;
   aListaMovimenti(aListaMovimenti.count+1):=aMov;
   movPosizione:=aListaMovimenti.count;
   update ass_cdp_mese_round set
	     pg_movimento = movPosizione
	    ,cd_cds = aUO.cd_unita_padre
	    ,cd_unita_organizzativa = aUO.cd_unita_organizzativa
		,im_arr_a1_non_distr = round(aAssCdpMeseRnd.im_arr_a1 - round(aAssCdpMeseRnd.im_arr_a1,2),30)
   where
            ESERCIZIO=aAssCdpMeseRnd.esercizio
        and CD_CDR_ROOT=aAssCdpMeseRnd.CD_CDR_ROOT
        and CD_VOCE_EP=aAssCdpMeseRnd.cd_voce_ep
 	    and MESE=aAssCdpMeseRnd.mese;
  end loop;

  aScrittura.CD_CDS:=aUO.cd_unita_padre;
  aScrittura.ESERCIZIO:=aEsercizio;
  aScrittura.CD_UNITA_ORGANIZZATIVA:=aUO.cd_unita_organizzativa;
  aScrittura.ORIGINE_SCRITTURA:='STIPENDI';
  aScrittura.CD_TERZO:=null;
  aScrittura.CD_TIPO_DOCUMENTO:=null;
  aScrittura.PG_NUMERO_DOCUMENTO:=null;
  aScrittura.CD_COMP_DOCUMENTO:=null;
  aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
  If (vMese Is Not Null) Then
    aScrittura.DT_CONTABILIZZAZIONE := last_day(to_date('01'||vMese||aesercizio, 'ddmmyyyy')); --Trunc(aTSNow); -- stani
  Else
    aScrittura.DT_CONTABILIZZAZIONE := Trunc(aTSNow); -- stani
  End If;
  aScrittura.DT_CANCELLAZIONE:=null;
  aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
  aScrittura.DS_SCRITTURA:='Contabilizzazione mensile stipendi. Mese n.'||aMese;
  aScrittura.PG_SCRITTURA_ANNULLATA:=null;
  aScrittura.ATTIVA:='Y';
  aScrittura.ESERCIZIO_DOCUMENTO_AMM:=null;
  aScrittura.DACR:=aTSNow;
  aScrittura.UTCR:=aUser;
  aScrittura.DUVA:=aTSNow;
  aScrittura.UTUV:=aUser;
  aScrittura.PG_VER_REC:=1;
  aScrittura.CD_CDS_DOCUMENTO:=null;
  aScrittura.CD_UO_DOCUMENTO:=null;

  CNRCTB200.creaScrittCoan(
   aScrittura,
   aListaMovimenti
  );

  update ass_cdp_mese_round set
	    pg_scrittura = aScrittura.pg_scrittura
  where
            ESERCIZIO=aEsercizio
        and CD_CDR_ROOT=aCDRRUO.cd_centro_responsabilita
 	    and MESE=aMese;

  update ass_cdp_la set
      stato = CNRCTB060.STATO_CDP_SCARICATO
	 ,utuv=aUser
	 ,duva=aTSNow
	 ,pg_ver_rec=pg_ver_rec+1
  where
       esercizio = aEsercizio
   and mese= aMese
   and cd_centro_responsabilita in (
    select cd_centro_responsabilita from cdr where
	   cd_unita_organizzativa = aUO.cd_unita_organizzativa
  );

  -- Aggionramento della testata STIPENDI COEP
  begin
   select * into aStipCoan from stipendi_coan where
        esercizio = aEsercizio
    and mese= aMese
    and cd_cds = aUO.cd_unita_padre
    and cd_uo = aUO.cd_unita_organizzativa;
   update stipendi_coan set
      pg_scrittura_an = aScrittura.pg_scrittura,
	  utuv=aUser,
	  duva=aTSNow,
	  pg_ver_rec=pg_ver_rec+1
   where
        esercizio = aStipCoan.esercizio
	and mese = aStipCoan.mese
	and cd_cds = aStipCoan.cd_cds
	and cd_uo = aStipCoan.cd_uo;
  exception when NO_DATA_FOUND then
   insert into STIPENDI_COAN (
     ESERCIZIO
    ,MESE
    ,CD_CDS
    ,CD_UO
    ,PG_SCRITTURA_AN
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aEsercizio
    ,aMese
    ,aUO.cd_unita_padre
    ,aUO.cd_unita_organizzativa
    ,aScrittura.pg_scrittura
    ,aTSNow
    ,aUser
    ,aTSNow
    ,aUser
    ,1
    );
  end;
 end;

-- ==============================================
-- ==============================================
-- ==============================================
-- ==============================================
-- ==============================================
--
-- SCRITTURA ECONOMICA COMPENSI (PRIMA SCRITTURA)
--
-- ==============================================
-- ==============================================
-- ==============================================
-- ==============================================
-- ==============================================

 function trovaContoEp(aObb obbligazione%rowtype) return voce_ep%rowtype is
  aAss ass_ev_voceep%rowtype;
  aVoceEp voce_ep%rowtype;
  aVoceSA varchar2(45);
 begin
 -- Recupero conto associato di default
  begin
   select * into aAss from ass_ev_voceep where
        esercizio = aObb.esercizio
    and ti_appartenenza = aObb.ti_appartenenza
    and ti_gestione = aObb.ti_gestione
    and cd_elemento_voce = aObb.cd_elemento_voce;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Associazione tra voce del piano finanziaria ed economica non trovata');
  end;
  return CNRCTB002.GETVOCEEP(aObb.esercizio,aAss.cd_voce_ep);
 end;

 function buildScrPEP(aEs number,aMese number,aComp compenso%rowtype, aUOP unita_organizzativa%rowtype, aCdTerzo number, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype is
  aScrittura scrittura_partita_doppia%rowtype;
 begin
   aScrittura:=null;
   aScrittura.CD_CDS:=aUOP.cd_unita_padre;
   aScrittura.ESERCIZIO:=aEs;
   aScrittura.CD_UNITA_ORGANIZZATIVA:=aUOP.cd_unita_organizzativa;
   aScrittura.PG_SCRITTURA:=null;
   aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_STIPENDI;
   aScrittura.CD_TIPO_DOCUMENTO:=CNRCTB100.TI_COMPENSO;
   aScrittura.CD_CDS_DOCUMENTO:=aComp.cd_cds;
   aScrittura.CD_UO_DOCUMENTO:=aComp.cd_unita_organizzativa;
   aScrittura.PG_NUMERO_DOCUMENTO:=aComp.pg_compenso;
   aScrittura.DT_CONTABILIZZAZIONE := last_day(to_date('01'||TO_CHAR(LPAD(Replace(Replace(amese, 13, 12), 15, 12), 2, '0'))||To_Char(aes), 'ddmmyyyy')); --Trunc(aTSNow); -- stani
   aScrittura.DT_PAGAMENTO:=null;
   aScrittura.DT_CANCELLAZIONE:=null;
   aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_PRIMA; -- Si tratta del tipo di scrittura prima o ultima
   aScrittura.CD_TERZO:=aCdTerzo;
   aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aScrittura.CD_DIVISA:='EURO';
   aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non e chiaro
   aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE
   aScrittura.DS_SCRITTURA:='Prima scrittura contabilizzazione economica stipendi mese:'||aMese; -- chi lo valorizza e come non e chiaro
   aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
   aScrittura.ATTIVA:='Y';
   aScrittura.ESERCIZIO_DOCUMENTO_AMM:=aComp.esercizio;
   aScrittura.DACR:=aTSNow;
   aScrittura.UTCR:=aUser;
   return aScrittura;
 end;
procedure regStipendiCOGE(aEs number, aUser varchar2) is
aStip STIPENDI_COFI%rowtype;
begin
 for aStip in (select * from STIPENDI_COFI where
       esercizio = aEs  and
       stato=CNRCTB100.STATO_COM_COFI_TOT_MR
  ) loop
  	regStipendiCOGE(aEs,aStip.mese,aUser);
  end loop;
end;

procedure regStipendiCOGE(aEs number, aMese number, aUser varchar2) is
  aScrittura scrittura_partita_doppia%rowtype;
  aMovimento movimento_coge%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aTSNow date;
  aContoEp voce_ep%rowtype;
  aListaScritture CNRCTB200.scrittureList;
  aListaNuoveScritture CNRCTB200.scrittureList;
  aListaVecchieScritture CNRCTB200.scrittureList;
  aListaNuoviMovimenti CNRCTB200.movimentiList;
  aListaM CNRCTB200.movimentiList;
  aAssPgiro ass_obb_acr_pgiro%rowtype;
  isProcessaCOEP boolean;
  aUO unita_organizzativa%rowtype;
  aObb obbligazione%rowtype;
  aStip stipendi_cofi%rowtype;
  aTotLordoScadenze number(15,2);
  aTotCaricoEnte number(15,2);
  aTotDistribuito number(15,2);
  i number;
  aNumObb number;
  aComp compenso%rowtype;
  aGen documento_generico%rowtype;
  aImporto number(15,2);
  aDocTst v_doc_amm_coge_tsta%rowtype;
  aContoEpCori voce_ep%rowtype;
  aSezCori char(1);
  aContoEpAnag voce_ep%rowtype;
  aEffCori ass_tipo_cori_voce_ep%rowtype;
  aScr scrittura_partita_doppia%rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  aTSNow:=sysdate;
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  -- Carica dalla tabella stipendi_cofi e verifica che la liquidazione mensile sia stata effettuata

  begin
   select * into aStip from stipendi_cofi where
        esercizio=aEs
	and mese=aMese
	and stato=CNRCTB100.STATO_COM_COFI_TOT_MR
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Liquidazione stipendi mese:'||aMese||'/'||aEs||' non ancora effettuata');
  end;

  -- L'esercizio contabile deve essere aperto per il cds di liquidazione stipendi
  if not CNRCTB008.ISESERCIZIOAPERTO(aEs, aStip.cd_cds_comp)
  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio contabile '||aEs||' non ? aperto per il cds:'||aStip.cd_cds_comp);
  end if;

  -- L'esercizio economico precedente deve essere chiuso definitivamente
  if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aStip.cd_cds_comp)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente non ? chiuso definitivamente per il cds: '||aStip.cd_cds_comp);
  end if;

  -- Carica il compenso parte cori stipendi

  begin
   select * into aComp from compenso where
        pg_compenso=aStip.pg_comp
    and cd_cds=aStip.cd_cds_comp
    and cd_unita_organizzativa=aStip.cd_uo_comp
    and esercizio=aStip.esercizio_comp
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Compenso fittizio per liquidazione stipendi mese:'||aMese||'/'||aEs||' non trovato');
  end;

  -- Verifica che non sia gi? stata effettuata questa scrittura

  begin
   select * into aScr from scrittura_partita_doppia where
                origine_scrittura=CNRCTB200.ORIGINE_STIPENDI
			and esercizio = aStip.esercizio
			and CD_CDS = aStip.cd_cds_comp
            and CD_TIPO_DOCUMENTO = CNRCTB100.TI_COMPENSO
            and PG_NUMERO_DOCUMENTO=aStip.pg_comp
            and ESERCIZIO_DOCUMENTO_AMM=aStip.esercizio_comp
            and CD_CDS_DOCUMENTO=aStip.cd_cds_comp
            and CD_UO_DOCUMENTO=aStip.cd_uo_comp;
   IBMERR001.RAISE_ERR_GENERICO('Registrazione economica stipendi mese:'||aMese||'/'||aEs||' gi? effettuata');
  exception when NO_DATA_FOUND then
   null;
  end;

  -- Carica il doc generico collegato al mandato principale stipendi

  begin
   select * into aGen from documento_generico where
	    cd_cds=aStip.cd_cds_doc_gen
	and cd_unita_organizzativa=aStip.cd_uo_doc_gen
	and esercizio=aStip.esercizio_doc_gen
	and cd_tipo_documento_amm=aStip.cd_tipo_doc_gen
	and pg_documento_generico=aStip.pg_doc_gen
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Documento generico per liquidazione stipendi mese:'||aMese||'/'||aEs||' non trovato');
  end;

  -- Carico il totale carico ente dei CORI
/* stani per Franca 04.11.2004
  select nvl(sum(ammontare),0) into aTotCaricoEnte from CONTRIBUTO_RITENUTA where
                 pg_compenso=aComp.pg_compenso
             and cd_cds=aComp.cd_cds
             and cd_unita_organizzativa=aComp.cd_unita_organizzativa
             and esercizio=aComp.esercizio
			 and ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE;

  -- Carico il totale

  select nvl(sum(im_totale),0) into aTotLordoScadenze from STIPENDI_COFI_OBB_SCAD where
       esercizio = aEs
   and mese = aMese;

  select count(*) into aNumObb from  STIPENDI_COFI_OBB_SCAD where
       esercizio = aEs
   and mese = aMese;
*/

  i:=0;
/* stani 04.11  aTotDistribuito:=0;*/
  for aStipObb in (select * from STIPENDI_COFI_OBB_SCAD where
       esercizio = aEs
   and mese = aMese
   for update nowait
  ) loop
   i:=i+1;
   select * into aObb from obbligazione where
        cd_cds = aStipObb.cd_cds_obbligazione
    and esercizio = aStipObb.esercizio
    and esercizio_originale = aStipObb.esercizio_ori_obbligazione
    and pg_obbligazione = aStipObb.pg_obbligazione
   for update nowait;
   -- Generazione movimento di default
   aContoEp:=trovaContoEp(aObb);
/*   aImporto:=aStipObb.im_totale * (aTotLordoScadenze-aTotCaricoEnte)/aTotLordoScadenze; stani per Franca 04.11.2004 sostituito aimporto*/
   aImporto:=aStipObb.im_totale;
/*   aTotDistribuito:=aTotDistribuito+aImporto; stani per Franca 04.11*/
   -- Distribuzione dei rotti su ultimo movimento
/* stani per Franca 04.11
    if i=aNumObb then
    aImporto:=aImporto+(aTotLordoScadenze-aTotCaricoEnte)-aTotDistribuito;
   end if;
*/
   CNRCTB204.buildMovPrinc(aObb.cd_cds,aObb.esercizio,aObb.cd_unita_organizzativa,aContoEp,aImporto,CNRCTB100.IS_DARE,aComp.dt_da_competenza_coge,aComp.dt_a_competenza_coge,aObb.cd_terzo,aListaMovimenti,aUser,aTSnow);
   if (recParametriCNR.fl_nuovo_pdg ='Y') then
    	aContoEpAnag:=CNRCTB204.trovaContoContrEp(aObb.esercizio,null,null,null,aContoEp.cd_voce_ep);
   		CNRCTB204.buildMovPrinc(aObb.cd_cds,aObb.esercizio,aObb.cd_unita_organizzativa,aContoEpAnag,aImporto,CNRCTB100.IS_AVERE,aComp.dt_da_competenza_coge,aComp.dt_a_competenza_coge,aObb.cd_terzo,aListaMovimenti,aUser,aTSnow);
   end if;
  end loop;
/* stani per Franca 04.11.2004
  select * into aDocTst from v_doc_amm_coge_tsta where
                 pg_numero_documento=aComp.pg_compenso
             and cd_tipo_documento = CNRCTB100.TI_COMPENSO
             and cd_cds=aComp.cd_cds
             and cd_unita_organizzativa=aComp.cd_unita_organizzativa
             and esercizio=aComp.esercizio;

  -- Evidenzia nella scrittura I SOLI CARICHI ENTE
  for aCori in (select * from v_doc_amm_coge_cori where
                 pg_numero_documento=aComp.pg_compenso
             and cd_tipo_documento = CNRCTB100.TI_COMPENSO
			 and ti_ente_percepiente = CNRCTB100.TI_CARICO_ENTE
             and cd_cds=aComp.cd_cds
             and cd_unita_organizzativa=aComp.cd_unita_organizzativa
             and esercizio=aComp.esercizio
 		   ) loop
   aEffCori:=CNRCTB204.getAssCoriEp(aDocTst,aCori);
   begin
		aContoEpCori:=CNRCTB002.getVoceEp(aEffCori.esercizio, aEffCori.cd_voce_ep);
   exception when OTHERS then
    	IBMERR001.RAISE_ERR_GENERICO('Conto economico: '||aEffCori.cd_voce_ep||' associato a CORI:'||aCori.cd_contributo_ritenuta||'non trovato');
   end;

   aSezCori:=CNRCTB204.getSezione(aDocTst,aCori);
   CNRCTB204.buildMovPrinc(aComp.cd_cds,aComp.esercizio,aComp.cd_unita_organizzativa,aContoEpCori,abs(aCori.ammontare),aSezCori,aComp.dt_da_competenza_coge,aComp.dt_a_competenza_coge,aObb.cd_terzo,aListaMovimenti,aUser,aTSnow);
   end loop;
fine stani per Franca per stipendi 04.11.2004*/

  aUO:=CNRCTB020.GETUOVALIDA(aObb.esercizio,aObb.cd_unita_organizzativa);

  if (recParametriCNR.fl_nuovo_pdg ='N') then
  	aContoEpAnag:=CNRCTB204.trovaContoAnag(aObb.esercizio,aObb.cd_terzo,aStip.cd_tipo_doc_gen,null,null);
  	CNRCTB204.buildChiusuraScrittura(aComp.cd_cds,aComp.esercizio,aComp.cd_unita_organizzativa,aContoEpAnag,aComp.dt_da_competenza_coge,aComp.dt_a_competenza_coge,aObb.cd_terzo,CNRCTB100.TI_ISTITUZIONALE,aListaMovimenti,aUser,aTSnow);
  end if;
  aScrittura:=buildScrPEP(aEs,aMese,aComp,aUO,aObb.cd_terzo,aUser,aTSNow);
  CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
 end;

 procedure ins_ASS_CDP_MESE_ROUND (aDest ASS_CDP_MESE_ROUND%rowtype) is
  begin
   insert into ASS_CDP_MESE_ROUND (
     ESERCIZIO
    ,CD_CDR_ROOT
    ,CD_VOCE_EP
    ,MESE
    ,IM_ARR_A1
    ,IM_ARR_A1_NON_DISTR
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,PG_MOVIMENTO
    ,DACR
    ,UTUV
    ,UTCR
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDR_ROOT
    ,aDest.CD_VOCE_EP
    ,aDest.MESE
    ,aDest.IM_ARR_A1
    ,aDest.IM_ARR_A1_NON_DISTR
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.PG_MOVIMENTO
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

end;
/


