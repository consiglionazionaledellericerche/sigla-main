--------------------------------------------------------
--  DDL for Package Body CNRCTB030
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB030" is
 function getDescObbligazione(aObbScad obbligazione_scadenzario%rowtype) return varchar2 is
 begin
  return cnrutil.getLabelObbligazioneMin()||' n.'||aObbScad.pg_obbligazione||' cds:'||aObbSCad.cd_cds||' es:'||aObbScad.esercizio||' esOri:'||aObbScad.esercizio_originale;
 End;

 procedure adeguaObbSV(aObbScad obbligazione_scadenzario%rowtype,aNewImp number,aUser varchar2) is
  aTSNow date;
  aScadVoce obbligazione_scad_voce%rowtype;
  aObb obbligazione%rowtype;
 begin
  aTSNow:=sysdate;
  -- Verifico che la scadenza abbia SOLO un dettaglio scad voce
  begin
   select * into aScadVoce from obbligazione_scad_voce where
           cd_cds = aObbScad.cd_cds
       and esercizio = aObbScad.esercizio
       and esercizio_originale = aObbScad.esercizio_originale
       and pg_obbligazione = aObbScad.pg_obbligazione
       and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario
   for update nowait;
  exception
   when TOO_MANY_ROWS  then
    IBMERR001.RAISE_ERR_GENERICO('L'''||getDescObbligazione(aObbScad)||' ha scadenze con più di un dettaglio analitico. Funzione non supportata');
   when NO_DATA_FOUND  then
    IBMERR001.RAISE_ERR_GENERICO('Nessuna scadenza analitica trovata per '||getDescObbligazione(aObbScad));
  end;
  -- estraggo la testata dell'obbligazione per aggiornare saldi
  aObb.cd_cds := aObbScad.cd_cds;
  aObb.esercizio := aObbScad.esercizio;
  aObb.esercizio_originale := aObbScad.esercizio_originale;
  aObb.pg_obbligazione := aObbScad.pg_obbligazione;
  CNRCTB035.LOCKDOC(aObb);
  -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  CNRCTB035.aggiornaSaldoDettScad(aObb,aScadVoce, aNewImp-aObbScad.im_scadenza,false,aUser,aTSNow);

  update obbligazione set
   im_obbligazione = im_obbligazione - aObbScad.im_scadenza + aNewImp,
   utuv=aUser,
   duva=aTSNow,
   pg_ver_rec=pg_ver_rec+1
  Where cd_cds = aObbScad.cd_cds
    and esercizio = aObbScad.esercizio
    and esercizio_originale = aObbScad.esercizio_originale
    and pg_obbligazione = aObbScad.pg_obbligazione;

  update obbligazione_scadenzario set
   im_scadenza = aNewImp,
   utuv=aUser,
   duva=aTSNow,
   pg_ver_rec=pg_ver_rec+1
  Where cd_cds = aObbScad.cd_cds
    and esercizio = aObbScad.esercizio
    and esercizio_originale = aObbScad.esercizio_originale
    and pg_obbligazione = aObbScad.pg_obbligazione
    and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;

  update obbligazione_scad_voce set
   im_voce = aNewImp,
   utuv=aUser,
   duva=aTSNow,
   pg_ver_rec=pg_ver_rec+1
  Where cd_cds = aObbScad.cd_cds
    and esercizio = aObbScad.esercizio
    and esercizio_originale = aObbScad.esercizio_originale
    and pg_obbligazione = aObbScad.pg_obbligazione
    and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;
 End;

 procedure adeguaObbScadSuccSV(aObbScad obbligazione_scadenzario%rowtype,aNextPgObbScad number,aNewImp number,aUser varchar2) is
  aNextScad obbligazione_scadenzario%rowtype;
  aTSNow date;
  aTotScadVoce number;
  aLastPg number;
 begin

  If aNewImp = aObbScad.im_scadenza Then --Scadenza con importo già uguale a quello richiesto
    return;
  end if;

  aTSNow:=sysdate;
  aTotScadVoce:=0;

  -- Leggo la scadenza su cui spostare le somme e se non esiste la creo
  begin
    select * into aNextScad from obbligazione_scadenzario where
           cd_cds = aObbScad.cd_cds
       and esercizio = aObbScad.esercizio
       and esercizio_originale = aObbScad.esercizio_originale
       and pg_obbligazione = aObbScad.pg_obbligazione
       and pg_obbligazione_scadenzario = aNextPgObbScad;
  exception
    when NO_DATA_FOUND then
      aNextScad.CD_CDS := aObbScad.CD_CDS;
      aNextScad.ESERCIZIO := aObbScad.ESERCIZIO;
      aNextScad.ESERCIZIO_ORIGINALE := aObbScad.ESERCIZIO_ORIGINALE;
      aNextScad.PG_OBBLIGAZIONE := aObbScad.PG_OBBLIGAZIONE;
      aNextScad.PG_OBBLIGAZIONE_SCADENZARIO := aNextPgObbScad;

      if aObbScad.DS_SCADENZA='gennaio' and aNextPgObbScad=2 Then
        aNextScad.DT_SCADENZA := to_date('25/02/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'febbraio';
      elsif aObbScad.DS_SCADENZA='febbraio' and aNextPgObbScad=3 Then
        aNextScad.DT_SCADENZA := to_date('25/03/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'marzo';
      elsif aObbScad.DS_SCADENZA='marzo' and aNextPgObbScad=4 Then
        aNextScad.DT_SCADENZA := to_date('25/04/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'aprile';
      elsif aObbScad.DS_SCADENZA='aprile' and aNextPgObbScad=5 Then
        aNextScad.DT_SCADENZA := to_date('25/05/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'maggio';
      elsif aObbScad.DS_SCADENZA='maggio' and aNextPgObbScad=6 Then
        aNextScad.DT_SCADENZA := to_date('25/06/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'giugno';
      elsif aObbScad.DS_SCADENZA='giugno' and aNextPgObbScad=7 Then
        aNextScad.DT_SCADENZA := to_date('25/07/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'luglio';
      elsif aObbScad.DS_SCADENZA='luglio' and aNextPgObbScad=8 Then
        aNextScad.DT_SCADENZA := to_date('25/08/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'agosto';
      elsif aObbScad.DS_SCADENZA='agosto' and aNextPgObbScad=9 Then
        aNextScad.DT_SCADENZA := to_date('25/09/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'settembre';
      elsif aObbScad.DS_SCADENZA='settembre' and aNextPgObbScad=10 Then
        aNextScad.DT_SCADENZA := to_date('25/10/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'ottobre';
      elsif aObbScad.DS_SCADENZA='ottobre' and aNextPgObbScad=11 Then
        aNextScad.DT_SCADENZA := to_date('25/11/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'novembre';
      elsif aObbScad.DS_SCADENZA='novembre' and aNextPgObbScad=12 Then
        aNextScad.DT_SCADENZA := to_date('15/12/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'tredicesima';
      elsif aObbScad.DS_SCADENZA='tredicesima' and aNextPgObbScad=13 Then
        aNextScad.DT_SCADENZA := to_date('25/12/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'dicembre';
      elsif aObbScad.DS_SCADENZA='dicembre' and aNextPgObbScad=14 Then
        aNextScad.DT_SCADENZA := to_date('31/12/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'residuo';
      elsif aNextPgObbScad=15 Then
        aNextScad.DT_SCADENZA := to_date('30/11/'||aObbScad.esercizio, 'dd/mm/yyyy');
        aNextScad.DS_SCADENZA := 'recupero addizionali reg/com';
      else
        aNextScad.DT_SCADENZA := aObbScad.DT_SCADENZA;
        aNextScad.DS_SCADENZA := aObbScad.DS_SCADENZA;
      end if;

      aNextScad.IM_SCADENZA := 0;
      aNextScad.IM_ASSOCIATO_DOC_AMM := 0;
      aNextScad.IM_ASSOCIATO_DOC_CONTABILE := 0;
      aNextScad.DACR := aTSNow;
      aNextScad.UTCR := aUser;
      aNextScad.DUVA := aTSNow;
      aNextScad.UTUV := aUser;
      aNextScad.PG_VER_REC := 0;

      cnrctb035.ins_OBBLIGAZIONE_SCADENZARIO(aNextScad);
  end;

  if aNextScad.im_scadenza - (aNewImp - aObbScad.im_scadenza) < 0 then
   IBMERR001.RAISE_ERR_GENERICO('La modifica di importo sulla scadenza:'||aObbScad.pg_obbligazione_scadenzario||' rende negativa o nulla la scadenza successiva per l'''||getDescObbligazione(aObbScad));
  end if;

  -- Aggiorno l'utente ultimo aggiornamento sul record principale
  update obbligazione set
   utuv=aUser,
   duva=aTSNow,
   pg_ver_rec=pg_ver_rec+1
  Where cd_cds = aObbScad.cd_cds
    and esercizio = aObbScad.esercizio
    and esercizio_originale = aObbScad.esercizio_originale
    and pg_obbligazione = aObbScad.pg_obbligazione;

  -- Aggiorno l'importo sulla scadenza di pagamento
  update obbligazione_scadenzario set
   im_scadenza = aNewImp,
   utuv=aUser,
   duva=aTSNow,
   pg_ver_rec=pg_ver_rec+1
  Where cd_cds = aObbScad.cd_cds
    and esercizio = aObbScad.esercizio
    and esercizio_originale = aObbScad.esercizio_originale
    and pg_obbligazione = aObbScad.pg_obbligazione
    and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;

  -- Scarico la diifferenza sulla scadenza successiva
  update obbligazione_scadenzario set
   im_scadenza = im_scadenza - (aNewImp - aObbScad.im_scadenza),
   utuv=aUser,
   duva=aTSNow,
   pg_ver_rec=pg_ver_rec+1
  Where cd_cds = aNextScad.cd_cds
    and esercizio = aNextScad.esercizio
    and esercizio_originale = aNextScad.esercizio_originale
    and pg_obbligazione = aNextScad.pg_obbligazione
    and pg_obbligazione_scadenzario = aNextScad.pg_obbligazione_scadenzario;

  --aggiorno i dettagli voce della scadenza di pagamento e scarico la differenza su quella della scadenza
  --successiva creandoli se non esistono
  Declare
    totScadVoceOld number := 0;
    totImportoSpostato number := 0;
  Begin
    for aObbScadVoce in (select * from obbligazione_scad_voce where
                         cd_cds = aObbScad.cd_cds
                         and esercizio = aObbScad.esercizio
                         and esercizio_originale = aObbScad.esercizio_originale
                         and pg_obbligazione = aObbScad.pg_obbligazione
                         and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario) Loop
      Declare
        impDaSpostare number := 0;
      Begin
       if(aObbScad.im_scadenza!=0) then
        impDaSpostare := aObbScadVoce.IM_VOCE - Round(aObbScadVoce.IM_VOCE*aNewImp/aObbScad.im_scadenza,2);
       else
          IBMERR001.RAISE_ERR_GENERICO('Errore in fase di aggiornamento scadenza verificare obbligazione/scadenza: '||aObbScad.pg_obbligazione||'/'||aObbScad.pg_obbligazione_scadenzario||'.');
       end if;

        if impDaSpostare > 0 Then
          UPDATE obbligazione_scad_voce
          SET IM_VOCE = aObbScadVoce.IM_VOCE - impDaSpostare,
              DUVA = aTSNow,
              UTUV = aUser
          Where esercizio = aObbScad.esercizio
          and esercizio_originale = aObbScad.esercizio_originale
          and pg_obbligazione = aObbScad.pg_obbligazione
          and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario
          and TI_APPARTENENZA = aObbScadVoce.TI_APPARTENENZA
          and TI_GESTIONE = aObbScadVoce.TI_GESTIONE
          and CD_VOCE = aObbScadVoce.CD_VOCE
          and CD_CENTRO_RESPONSABILITA = aObbScadVoce.CD_CENTRO_RESPONSABILITA
          and CD_LINEA_ATTIVITA = aObbScadVoce.CD_LINEA_ATTIVITA;

          totScadVoceOld := totScadVoceOld + (aObbScadVoce.IM_VOCE - impDaSpostare);

          --Cerco la stessa combinazione sulla scadenza successiva per aggiornarla.
          --Se non la trovo la creo
          Declare
            aNextScadVoce obbligazione_scad_voce%rowtype;
          Begin
            Update obbligazione_scad_voce
            set IM_VOCE = IM_VOCE + impDaSpostare
            where cd_cds = aNextScad.cd_cds
            and esercizio = aNextScad.esercizio
            and esercizio_originale = aNextScad.esercizio_originale
            and pg_obbligazione = aNextScad.pg_obbligazione
            and pg_obbligazione_scadenzario = aNextScad.pg_obbligazione_scadenzario
            and TI_APPARTENENZA = aObbScadVoce.TI_APPARTENENZA
            and TI_GESTIONE = aObbScadVoce.TI_GESTIONE
            and CD_VOCE = aObbScadVoce.CD_VOCE
            and CD_CENTRO_RESPONSABILITA = aObbScadVoce.CD_CENTRO_RESPONSABILITA
            and CD_LINEA_ATTIVITA = aObbScadVoce.CD_LINEA_ATTIVITA;

            If sql%rowcount=0 Then
              aNextScadVoce := null;
              aNextScadVoce.CD_CDS := aNextScad.CD_CDS;
              aNextScadVoce.ESERCIZIO := aNextScad.ESERCIZIO;
              aNextScadVoce.ESERCIZIO_ORIGINALE := aNextScad.ESERCIZIO_ORIGINALE;
              aNextScadVoce.PG_OBBLIGAZIONE := aNextScad.PG_OBBLIGAZIONE;
              aNextScadVoce.PG_OBBLIGAZIONE_SCADENZARIO := aNextScad.PG_OBBLIGAZIONE_SCADENZARIO;
              aNextScadVoce.TI_APPARTENENZA := aObbScadVoce.TI_APPARTENENZA;
              aNextScadVoce.TI_GESTIONE := aObbScadVoce.TI_GESTIONE;
              aNextScadVoce.CD_VOCE := aObbScadVoce.CD_VOCE;
              aNextScadVoce.CD_CENTRO_RESPONSABILITA := aObbScadVoce.CD_CENTRO_RESPONSABILITA;
              aNextScadVoce.CD_LINEA_ATTIVITA := aObbScadVoce.CD_LINEA_ATTIVITA;
              aNextScadVoce.IM_VOCE := impDaSpostare;
              aNextScadVoce.DACR := aTSNow;
              aNextScadVoce.UTCR := aUser;
              aNextScadVoce.DUVA := aTSNow;
              aNextScadVoce.UTUV := aUser;
              aNextScadVoce.PG_VER_REC := 0;

              cnrctb035.ins_OBBLIGAZIONE_SCAD_VOCE(aNextScadVoce);
            End if;

            totImportoSpostato := totImportoSpostato + impDaSpostare;
          End;
        End If;
      End;
    End Loop;

    --Riapro i cursori per spalmare la differenza riprendendola dalla scadenza successiva
    If totScadVoceOld != aNewImp Then
      Declare
        diffDaSpalmare number := aNewImp - totScadVoceOld;
      Begin
        for aObbScadVoce in (select * from obbligazione_scad_voce
                             where cd_cds = aObbScad.cd_cds
                             and esercizio = aObbScad.esercizio
                             and esercizio_originale = aObbScad.esercizio_originale
                             and pg_obbligazione = aObbScad.pg_obbligazione
                             and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario) Loop
          Declare
            diffDett number := aObbScadVoce.im_voce + diffDaSpalmare;
          Begin
            If diffDett < 0 Then
              diffDett := -aObbScadVoce.im_voce;
            Else
              diffDett := diffDaSpalmare;
            End If;

            update obbligazione_scad_voce
            set im_voce = aObbScadVoce.im_voce + diffDett
            where cd_cds = aObbScadVoce.cd_cds
            and esercizio = aObbScadVoce.esercizio
            and esercizio_originale = aObbScadVoce.esercizio_originale
            and pg_obbligazione = aObbScadVoce.pg_obbligazione
            and pg_obbligazione_scadenzario = aObbScadVoce.pg_obbligazione_scadenzario
            and TI_APPARTENENZA = aObbScadVoce.TI_APPARTENENZA
            and TI_GESTIONE = aObbScadVoce.TI_GESTIONE
            and CD_VOCE = aObbScadVoce.CD_VOCE
            and CD_CENTRO_RESPONSABILITA = aObbScadVoce.CD_CENTRO_RESPONSABILITA
            and CD_LINEA_ATTIVITA = aObbScadVoce.CD_LINEA_ATTIVITA;

            update obbligazione_scad_voce
            set im_voce = im_voce - diffDett
            where cd_cds = aNextScad.cd_cds
            and esercizio = aNextScad.esercizio
            and esercizio_originale = aNextScad.esercizio_originale
            and pg_obbligazione = aNextScad.pg_obbligazione
            and pg_obbligazione_scadenzario = aNextScad.pg_obbligazione_scadenzario
            and TI_APPARTENENZA = aObbScadVoce.TI_APPARTENENZA
            and TI_GESTIONE = aObbScadVoce.TI_GESTIONE
            and CD_VOCE = aObbScadVoce.CD_VOCE
            and CD_CENTRO_RESPONSABILITA = aObbScadVoce.CD_CENTRO_RESPONSABILITA
            and CD_LINEA_ATTIVITA = aObbScadVoce.CD_LINEA_ATTIVITA;

            diffDaSpalmare := diffDaSpalmare - diffDett;
            If diffDaSpalmare = 0 Then
              exit;
            End If;
          End;
        End loop;

        If diffDaSpalmare!=0 Then
          IBMERR001.RAISE_ERR_GENERICO('Errore in fase di sdoppiamento scadenza '||getDescObbligazione(aObbScad)||'.');
        End If;
      End;
    End if;
  End;
 End;

 function getTotManRegolNoPgiro(aEsScrivania number,aCdCds varchar2) return number is
  tot_man_reg NUMBER:=0;
  uo_ente unita_organizzativa%rowtype;
  aCds unita_organizzativa%rowtype;
 begin
         tot_man_reg:=0;

		 -- Recupero la UO Ente
		 uo_ente := CNRCTB020.getuoente(aEsScrivania);

		 -- Recupera il rowtype di CDS
		 aCds := CNRCTB020.GETCDSVALIDO(aEsScrivania,aCdCds);

		 if aCdCds = uo_ente.CD_UNITA_PADRE then
		  return 0;
		 end if;

		 if aCds.cd_tipo_unita=CNRCTB020.TIPO_AREA then
		  -- Calcolo la somma dei mandati di regolarizzazione emessi per il Cds di scrivania di tipo area
		  -- quindi entrando sulla voce_f con cd_proprio_voce=cd_cds area
		  -- e cd_cds = a presidente dell'area
 		  select nvl(sum(im_mandato_riga),0) into tot_man_reg
		   from v_mandato_voce_f_cds
		   where
		         cd_cds_voce is not null
		   and 	 cd_proprio_voce = aCdCds
		   and	 esercizio	 = aEsScrivania
		   and 	 cd_cds		 = uo_ente.CD_UNITA_PADRE; -- Cds Ente
		 else
		  -- Calcolo la somma dei mandati di regolarizzazione emessi per il Cds di scrivania
		  -- non di tipo area: cd_cds della voce = cds di scrivania cd_cds della voce != cd_proprio della voce
		  select nvl(sum(im_mandato_riga),0) into tot_man_reg
		   from v_mandato_voce_f_cds
		   where cd_cds_voce = aCdCds
		   and   cd_proprio_voce = cd_cds_voce
		   and	 esercizio	 = aEsScrivania
		   and 	 cd_cds		 = uo_ente.CD_UNITA_PADRE; -- Cds Ente
		 end if;
		 return tot_man_reg;
 end;

/*
 function getSaldoManRevPgiroEsPrec(aEsScrivania number,aCdCds varchar2) return number is
  uo_ente unita_organizzativa%rowtype;
  aDeltaMRPgiroEsPrec NUMBER;
 begin
		 -- Recupero la UO Ente
		 uo_ente := CNRCTB020.getuoente(aEsScrivania);

		 if aCdCds = uo_ente.CD_UNITA_PADRE then
		  return 0;
		 end if;

         -- Estrazione mandati/reversali su partita di giro esercizio precedente (se esercizio corrente > esercizio partenza)
		 aDeltaMRPgiroEsPrec:=0;
         if aEsScrivania > CNRCTB008.ESERCIZIO_PARTENZA then
          select
           nvl(sum(decode(a.ti_gestione,CNRCTB001.GESTIONE_SPESE,1,-1)*a.IM_MANDATI_REVERSALI),0) into aDeltaMRPgiroEsPrec
          from voce_f_saldi_cmp a where
               a.esercizio<=aEsScrivania-1
           and a.cd_cds=aCdCds
           and a.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
           and a.ti_competenza_residuo = CNRCTB054.TI_COMPETENZA
           and exists (select 1 from elemento_voce where
                esercizio = a.esercizio
            and ti_appartenenza = a.ti_appartenenza
            and ti_gestione = a.ti_gestione
            and cd_elemento_voce = a.cd_voce
            and fl_partita_giro='Y'
           );
         end if;
		 return aDeltaMRPgiroEsPrec;
 end;
*/

/*
 function getMassaSpendibileResidui(
  aEs number,
  aEsScrivania number,
  aCdCds varchar2,
  aCdElementoVoce varchar2
 ) return number is
  aEV elemento_voce%rowtype;
  aRate number;
  aPRCCOP prc_copertura_obblig%rowtype;
  aControllo1  V_OBBLIG_CONTROLLO_RESIDUI%rowtype;
  aControllo2  V_OBBLIG_CONTROLLO_A2%rowtype;
  aControllo3  V_OBBLIG_CONTROLLO_A3%rowtype;
  aCDS unita_organizzativa%rowtype;
  aAppartenenza char(1);
 begin
  if aEs is null or aEsScrivania is null then
   IBMERR001.RAISE_ERR_GENERICO('Esercizio di competenza o di scrivania non specificato');
  end if;

  if aEs = aEsScrivania then
      declare
       tot_man_reg NUMBER:=0;
       aDeltaMRPgiroEsPrec NUMBER:=0;
	  begin
	     select * into aControllo1 from V_OBBLIG_CONTROLLO_RESIDUI where
	           esercizio = aEsScrivania
	       and cd_cds = aCdCds;
         tot_man_reg:=getTotManRegolNoPgiro(aEsScrivania,aCdCds);
         aDeltaMRPgiroEsPrec:=getSaldoManRevPgiroEsPrec(aEsScrivania,aCdCds);
	     return
		         aControllo1.IM_STANZIAMENTO_A1
			   + aControllo1.IM_VAR_PIU_A1
			   - aControllo1.IM_VAR_MENO_A1
			   + aControllo1.IM_RESIDUO
			   + aControllo1.IM_RES_VAR_PIU
			   - aControllo1.IM_RES_VAR_MENO
			   + aControllo1.IM_CASSA_INIZIALE
			   - tot_man_reg -- Detraggo l'importo relativo alla somma dei mandati di regolariz.
			   - aControllo1.IM_SALDO_MAN_REV
			   + aDeltaMRPgiroEsPrec;
	  end; -- Fine declare
  end if;

  if aEs = aEsScrivania + 1 then
   begin
     select * into aPRCCOP from prc_copertura_obblig where
	      esercizio = aEsScrivania
	  and cd_unita_organizzativa = aCdCds;
 	 aRate:=aPRCCOP.prc_copertura_obblig_2;
   exception when NO_DATA_FOUND then
	 aRate:=100;
   end;
   select * into aControllo2 from V_OBBLIG_CONTROLLO_A2 where
        esercizio = aEsScrivania
    and cd_cds = aCdCds;
    return (aControllo2.IM_STANZIAMENTO_A2 * aRate/100) - aControllo2.IM_SALDO_OBBLIG_A2;
  end if;

  if aEs = aEsScrivania + 2 then
   begin
     select * into aPRCCOP from prc_copertura_obblig where
	      esercizio = aEsScrivania
	  and cd_unita_organizzativa = aCdCds;
   	 aRate:=aPRCCOP.prc_copertura_obblig_3;
   exception when NO_DATA_FOUND then
	aRate:=100;
   end;
   select * into aControllo3 from V_OBBLIG_CONTROLLO_A3 where
        esercizio = aEsScrivania
    and cd_cds = aCdCds;
    return(aControllo3.IM_STANZIAMENTO_A3 * aRate/100) - aControllo3.IM_SALDO_OBBLIG_A3;
  end if;
  return 0;
 end;
*/

 function checkDisponibilitaCassa(
  esercizio number,
  cd_cds varchar2,
  esercizio_originale number,
  pg_obbligazione NUMBER
 ) return char is
 begin
  return 'Y';
 end;

 procedure creaImpegniCnr(aEs VARCHAR2, aCDR VARCHAR2, Utente VARCHAR2) is
  aUOENTE v_unita_organizzativa_valida%rowtype;
 begin

  select * into aUOENTE
  from v_unita_organizzativa_valida where
       esercizio=aEs
   and cd_tipo_unita = CNRCTB020.TIPO_ENTE
   and fl_uo_cds='Y';

  for aSaldo in (select a.*
                 from   VOCE_F_SALDI_CDR_LINEA A
                 Where  a.esercizio = aEs And
                        a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR And
                        a.esercizio = a.esercizio_res And
                                exists (select 1
                                        from    voce_f
                                        Where   esercizio = aEs  and
                                                ti_appartenenza = CNRCTB001.APPARTENENZA_CNR And
                                                ti_gestione = CNRCTB001.GESTIONE_SPESE  And
                                                cd_voce = a.cd_voce And
                                                cd_parte = CNRCTB001.PARTE1)) loop
   aggiornaImpegnoCapitolo(aSaldo, 0, Utente);
  end loop;
 end;

Procedure aggiornaImpegnoCapitolo(aSaldo voce_f_saldi_cdr_linea%rowtype, isPerVariazione Boolean, DELTA_VAR_RES NUMBER, utente VARCHAR2) is
  aObblig               obbligazione%rowtype;
  aObbligScad           obbligazione_scadenzario%rowtype;
  aObbligScadVoce       obbligazione_scad_voce%rowtype;
  aTSNow                date;
  aUOENTE               v_unita_organizzativa_valida%rowtype;
  aCDRENTE              v_cdr_valido%rowtype;
  aNumeratore           number;
  aNuovoImporto         number(15,2);
  aCodTerzoDivImpegni   number;
  aTerzoTemp            terzo%rowtype;
  aSaldoCdrLinea        voce_f_saldi_cdr_linea%Rowtype;
  TIPO_IMPEGNO          VARCHAR2(10);
  DES_TIPO_IMPEGNO      VARCHAR2(50);
  delta_saldo_cdr       NUMBER := 0;

  TIPO_IMPEGNO_RIB      VARCHAR2(10);
  aObbligRib            obbligazione%rowtype;
  aObbligScadRib        obbligazione_scadenzario%rowtype;
  aObbligScadVoceRib    obbligazione_scad_voce%rowtype;
  aNuovoImportoRib      number(15,2):=0;
  delta_saldo_cdrRib    NUMBER := 0;
  aSaldoCdrLineaRib     voce_f_saldi_cdr_linea%Rowtype;
Begin

aTSNow:=sysdate;

if isPerVariazione is null then
   IBMERR001.RAISE_ERR_GENERICO('Errore interno in aggiornamento impegno capitolo');
End if;

Begin

   /* 22.02.2006 stani:
   AGGIORNAMENTO DELL'IMPEGNO RESIDUO PER APPROVAZIONE VARIAZIONE AL BILANZIO DI SERVIZIO (RESIDUO)
   PER APPROVAZIONE DI VARIAZIONE ALLO STANZIAMENTO RESIDUO */

   -- Per identificare l'impegno del capitolo bisogna partire da scad voce che contiene il codice del capitolo
   -- in join con obbligazione che contiene il tipo di documento A SECONDA DELL'ANNO RESIDUO ('IMP')

If aSaldo.esercizio = aSaldo.esercizio_res Then
        TIPO_IMPEGNO := CNRCTB018.TI_DOC_IMP;
        DES_TIPO_IMPEGNO := 'Impegno';
Elsif aSaldo.esercizio > aSaldo.esercizio_res Then
        TIPO_IMPEGNO := CNRCTB018.TI_DOC_IMP_RES;
        DES_TIPO_IMPEGNO := 'Impegno Residuo';
End if;

-----------------------------------------------------------------------------------------------------------------------
------------------------------------ RECUPERO L'IMPEGNO DA DIMINUIRE/AUMENTARE... -------------------------------------
-----------------------------------------------------------------------------------------------------------------------

If Nvl(DELTA_VAR_RES, 0) >= 0 Then

-- SE DEVO AUMENTARE L'IMPEGNO PRENDO IL PIU' GRANDE DI QUELL'ESERCIZIO RESIDUO CON O SENZA DISPONIBILITA'

   select a.*
   into  aObbligScadVoce
   from  obbligazione_scad_voce a, obbligazione b
   Where a.CD_CDS    = CNRCTB020.GETCDCDSENTE (aSaldo.esercizio)
     And a.ESERCIZIO = aSaldo.esercizio
     and a.ESERCIZIO_ORIGINALE = aSaldo.esercizio_res
     and a.TI_APPARTENENZA = aSaldo.ti_appartenenza
     and a.TI_GESTIONE = aSaldo.ti_gestione
     and a.CD_VOCE     = aSaldo.cd_voce
     and b.CD_CDS      = a.cd_cds
     and b.ESERCIZIO   = a.esercizio
     and b.ESERCIZIO_ORIGINALE=a.esercizio_originale
     and b.PG_OBBLIGAZIONE = a.PG_OBBLIGAZIONE
     and b.CD_TIPO_DOCUMENTO_CONT = TIPO_IMPEGNO
     And b.stato_obbligazione != cnrctb035.stato_stornato
     And A.pg_obbligazione = (Select Max(a.pg_obbligazione)
                            from  obbligazione_scad_voce a, obbligazione b
                            Where a.CD_CDS    = CNRCTB020.GETCDCDSENTE (aSaldo.esercizio)
                              and a.ESERCIZIO = aSaldo.esercizio
                              and a.ESERCIZIO_ORIGINALE = aSaldo.esercizio_res
                              and a.TI_APPARTENENZA = aSaldo.ti_appartenenza
                              and a.TI_GESTIONE = aSaldo.ti_gestione
                              and a.CD_VOCE     = aSaldo.cd_voce
                              and b.CD_CDS      = a.cd_cds
                              and b.ESERCIZIO   = a.esercizio
                              and b.ESERCIZIO_ORIGINALE   = a.esercizio_originale
                              and b.PG_OBBLIGAZIONE = a.PG_OBBLIGAZIONE
                              and b.CD_TIPO_DOCUMENTO_CONT = TIPO_IMPEGNO)
   for update nowait;

Elsif Nvl(DELTA_VAR_RES, 0) < 0 Then

-- SE DEVO DIMINUIRE L'IMPEGNO PRENDO IL PIU' GRANDE DI QUELL'ESERCIZIO RESIDUO CHE ABBIA DISPONIBILITA'

   select a.*
   into  aObbligScadVoce
   from  obbligazione_scad_voce a, obbligazione b
   Where a.CD_CDS    = CNRCTB020.GETCDCDSENTE (aSaldo.esercizio)
     And a.ESERCIZIO = aSaldo.esercizio
     and a.ESERCIZIO_ORIGINALE = aSaldo.esercizio_res
     and a.TI_APPARTENENZA = aSaldo.ti_appartenenza
     and a.TI_GESTIONE = aSaldo.ti_gestione
     and a.CD_VOCE     = aSaldo.cd_voce
     and b.CD_CDS      = a.cd_cds
     and b.ESERCIZIO   = a.esercizio
     and b.ESERCIZIO_ORIGINALE=a.esercizio_originale
     and b.PG_OBBLIGAZIONE = a.PG_OBBLIGAZIONE
     and b.CD_TIPO_DOCUMENTO_CONT = TIPO_IMPEGNO
     And b.stato_obbligazione != cnrctb035.stato_stornato
     And A.pg_obbligazione = (Select Max(a.pg_obbligazione)
                            from  obbligazione_scad_voce a, obbligazione b, obbligazione_scadenzario OS
                            Where a.CD_CDS    = CNRCTB020.GETCDCDSENTE (aSaldo.esercizio)
                              and a.ESERCIZIO = aSaldo.esercizio
                              and a.ESERCIZIO_ORIGINALE = aSaldo.esercizio_res
                              and a.TI_APPARTENENZA = aSaldo.ti_appartenenza
                              and a.TI_GESTIONE = aSaldo.ti_gestione
                              and a.CD_VOCE     = aSaldo.cd_voce
                              and b.CD_CDS      = a.cd_cds
                              and b.ESERCIZIO   = a.esercizio
                              and b.ESERCIZIO_ORIGINALE   = a.esercizio_originale
                              and b.PG_OBBLIGAZIONE = a.PG_OBBLIGAZIONE
                              And A.CD_CDS                      = OS.cd_cds
                              And A.ESERCIZIO                   = OS.esercizio
                              And A.ESERCIZIO_ORIGINALE         = OS.esercizio_originale
                              And A.PG_OBBLIGAZIONE             = OS.pg_obbligazione
                              And A.PG_OBBLIGAZIONE_SCADENZARIO = OS.pg_obbligazione_scadenzario
                              And OS.IM_SCADENZA - OS.IM_ASSOCIATO_DOC_AMM >=  abs(Nvl(DELTA_VAR_RES,0))
                              and b.CD_TIPO_DOCUMENTO_CONT = TIPO_IMPEGNO)
   for update nowait;

End If;

Select * into aObbligScad
from   obbligazione_scadenzario
 Where CD_CDS                        = aObbligScadVoce.cd_cds
   And ESERCIZIO                     = aObbligScadVoce.esercizio
   And ESERCIZIO_ORIGINALE           = aObbligScadVoce.esercizio_originale
   And PG_OBBLIGAZIONE              = aObbligScadVoce.pg_obbligazione
   And PG_OBBLIGAZIONE_SCADENZARIO   = aObbligScadVoce.pg_obbligazione_scadenzario
 for update nowait;

Select * into aObblig
from obbligazione
 Where CD_CDS=aObbligScadVoce.cd_cds
   And ESERCIZIO=aObbligScadVoce.esercizio
   and ESERCIZIO_ORIGINALE = aObbligScadVoce.esercizio_originale
  and  PG_OBBLIGAZIONE=aObbligScadVoce.pg_obbligazione
 for update nowait;

-----------------------------------------------------------------------------------------------------------------------
----------------------------------------------- FINE RECUPERO IMPEGNO -------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------

-------------------------------------------------- lo trovo -----------------------------------------------------------

If isPerVariazione Then
    /* 11/02/2011 ROSPUC */
    -- Se riportato ma con disponibilita questa procedura viene richiamata per tutti gli esercizi

    if aObblig.riportato = 'Y'  then
        --- Inizio recupero impegno esercizio successivo
        -----------------------------------------------------------------------------------------------------------------------
        ------------------------------------ RECUPERO L'IMPEGNO DA DIMINUIRE/AUMENTARE... -------------------------------------
        -----------------------------------------------------------------------------------------------------------------------
             --DEVE ESSERE PER FORZA RESIDUO
             TIPO_IMPEGNO_RIB := CNRCTB018.TI_DOC_IMP_RES;
             Begin
             If Nvl(DELTA_VAR_RES, 0) >= 0 Then

          -- SE DEVO AUMENTARE L'IMPEGNO PRENDO IL PIU' GRANDE DI QUELL'ESERCIZIO RESIDUO CON O SENZA DISPONIBILITA'

                 select a.*
                 into  aObbligScadVoceRib
                 from  obbligazione_scad_voce a, obbligazione b
                 Where a.CD_CDS    = CNRCTB020.GETCDCDSENTE (aSaldo.esercizio)
                   And a.ESERCIZIO = aSaldo.esercizio+1
                   and a.ESERCIZIO_ORIGINALE = aSaldo.esercizio_res
                   and a.TI_APPARTENENZA = aSaldo.ti_appartenenza
                   and a.TI_GESTIONE = aSaldo.ti_gestione
                   and a.CD_VOCE     = aSaldo.cd_voce
                   and b.CD_CDS      = a.cd_cds
                   and b.ESERCIZIO   = a.esercizio
                   and b.ESERCIZIO_ORIGINALE=a.esercizio_originale
                   and b.PG_OBBLIGAZIONE = a.PG_OBBLIGAZIONE
                   and b.CD_TIPO_DOCUMENTO_CONT = TIPO_IMPEGNO_RIB
                   And b.stato_obbligazione != cnrctb035.stato_stornato
                   And A.pg_obbligazione = (Select Max(a.pg_obbligazione)
                                          from  obbligazione_scad_voce a, obbligazione b
                                          Where a.CD_CDS    = CNRCTB020.GETCDCDSENTE (aSaldo.esercizio)
                                            and a.ESERCIZIO = aSaldo.esercizio+1
                                            and a.ESERCIZIO_ORIGINALE = aSaldo.esercizio_res
                                            and a.TI_APPARTENENZA = aSaldo.ti_appartenenza
                                            and a.TI_GESTIONE = aSaldo.ti_gestione
                                            and a.CD_VOCE     = aSaldo.cd_voce
                                            and b.CD_CDS      = a.cd_cds
                                            and b.ESERCIZIO   = a.esercizio
                                            and b.ESERCIZIO_ORIGINALE   = a.esercizio_originale
                                            and b.PG_OBBLIGAZIONE = a.PG_OBBLIGAZIONE
                                            and b.CD_TIPO_DOCUMENTO_CONT = TIPO_IMPEGNO_RIB)
                 for update nowait;

           Elsif Nvl(DELTA_VAR_RES, 0) < 0 Then

          -- SE DEVO DIMINUIRE L'IMPEGNO PRENDO IL PIU' GRANDE DI QUELL'ESERCIZIO RESIDUO CHE ABBIA DISPONIBILITA'

             select a.*
             into  aObbligScadVoceRib
             from  obbligazione_scad_voce a, obbligazione b
             Where a.CD_CDS    = CNRCTB020.GETCDCDSENTE (aSaldo.esercizio)
               And a.ESERCIZIO = aSaldo.esercizio+1
               and a.ESERCIZIO_ORIGINALE = aSaldo.esercizio_res
               and a.TI_APPARTENENZA = aSaldo.ti_appartenenza
               and a.TI_GESTIONE = aSaldo.ti_gestione
               and a.CD_VOCE     = aSaldo.cd_voce
               and b.CD_CDS      = a.cd_cds
               and b.ESERCIZIO   = a.esercizio
               and b.ESERCIZIO_ORIGINALE=a.esercizio_originale
               and b.PG_OBBLIGAZIONE = a.PG_OBBLIGAZIONE
               and b.CD_TIPO_DOCUMENTO_CONT = TIPO_IMPEGNO_RIB
               And b.stato_obbligazione != cnrctb035.stato_stornato
               And A.pg_obbligazione = (Select Max(a.pg_obbligazione)
                                      from  obbligazione_scad_voce a, obbligazione b, obbligazione_scadenzario OS
                                      Where a.CD_CDS    = CNRCTB020.GETCDCDSENTE (aSaldo.esercizio)
                                        and a.ESERCIZIO = aSaldo.esercizio+1
                                        and a.ESERCIZIO_ORIGINALE = aSaldo.esercizio_res
                                        and a.TI_APPARTENENZA = aSaldo.ti_appartenenza
                                        and a.TI_GESTIONE = aSaldo.ti_gestione
                                        and a.CD_VOCE     = aSaldo.cd_voce
                                        and b.CD_CDS      = a.cd_cds
                                        and b.ESERCIZIO   = a.esercizio
                                        and b.ESERCIZIO_ORIGINALE   = a.esercizio_originale
                                        and b.PG_OBBLIGAZIONE = a.PG_OBBLIGAZIONE
                                        And A.CD_CDS                      = OS.cd_cds
                                        And A.ESERCIZIO                   = OS.esercizio
                                        And A.ESERCIZIO_ORIGINALE         = OS.esercizio_originale
                                        And A.PG_OBBLIGAZIONE             = OS.pg_obbligazione
                                        And A.PG_OBBLIGAZIONE_SCADENZARIO = OS.pg_obbligazione_scadenzario
                                        And OS.IM_SCADENZA - OS.IM_ASSOCIATO_DOC_AMM >= abs(Nvl(DELTA_VAR_RES,0))
                                        and b.CD_TIPO_DOCUMENTO_CONT = TIPO_IMPEGNO_RIB)
             for update nowait;

          End If;

          Select * into aObbligScadRib
          from   obbligazione_scadenzario
           Where CD_CDS                        = aObbligScadVoceRib.cd_cds
             And ESERCIZIO                     = aObbligScadVoceRib.esercizio
             And ESERCIZIO_ORIGINALE           = aObbligScadVoceRib.esercizio_originale
             And PG_OBBLIGAZIONE               = aObbligScadVoceRib.pg_obbligazione
             And PG_OBBLIGAZIONE_SCADENZARIO   = aObbligScadVoceRib.pg_obbligazione_scadenzario
           for update nowait;

          Select * into aObbligRib
          from obbligazione
           Where CD_CDS=aObbligScadVoceRib.cd_cds
             And ESERCIZIO=aObbligScadVoceRib.esercizio
             and ESERCIZIO_ORIGINALE = aObbligScadVoceRib.esercizio_originale
            and  PG_OBBLIGAZIONE=aObbligScadVoceRib.pg_obbligazione
           for update nowait;

          if aObbligScadRib.IM_SCADENZA - aObbligScadRib.IM_ASSOCIATO_DOC_AMM + DELTA_VAR_RES < 0 Then
            IBMERR001.RAISE_ERR_GENERICO('L''impegno n.'||aObbligRib.pg_obbligazione||' del '||aObbligRib.esercizio_originale||' relativo al capitolo '||aObbligScadVoceRib.cd_voce||' risulta riportato a nuovo esercizio e non è modificabile.');
          else
             aNuovoImportoRib := aObbligScadRib.IM_SCADENZA + DELTA_VAR_RES;
             delta_saldo_cdrRib := Nvl(DELTA_VAR_RES, 0);
          end if;
        exception when no_data_found then
          IBMERR001.RAISE_ERR_GENERICO('L''impegno relativo al capitolo '||aSaldo.cd_voce||' risulta riportato a nuovo esercizio e non è modificabile.');
        end;

    end if;

    -- Se la modifica è fatta per variazione, devo sanare necessariamente le differenze
    -- tra assestato e importo associato a doc. amm. dell'impegno

    If TIPO_IMPEGNO = CNRCTB018.TI_DOC_IMP Then  -- VARIAZIONE DI UN COMPETENZA
        If aObbligScad.IM_SCADENZA + DELTA_VAR_RES < aObbligScad.im_associato_doc_amm Then
         IBMERR001.RAISE_ERR_GENERICO('Il nuovo importo dell'''||DES_TIPO_IMPEGNO||' n. '||aObbligScad.pg_obbligazione||' del '||aObbligScad.esercizio_originale||
         ' sulla voce '||aSaldo.cd_voce||' risulterebbe inferiore a quanto già associato a documenti amministrativi (importo '||
         To_Char(aObbligScad.IM_SCADENZA)||', variazione '||DELTA_VAR_RES||', quota associata '||aObbligScad.im_associato_doc_amm||')');
        Else
         aNuovoImporto := aObbligScad.IM_SCADENZA + DELTA_VAR_RES;
         delta_saldo_cdr := Nvl(DELTA_VAR_RES, 0);
        End if;
    Elsif TIPO_IMPEGNO = CNRCTB018.TI_DOC_IMP_RES Then  -- VARIAZIONE DI UN RESIDUO

    -- VERIFICARE STANI DANIELA
        if aObbligScad.IM_SCADENZA - aObbligScad.IM_ASSOCIATO_DOC_AMM + DELTA_VAR_RES < 0 Then
         IBMERR001.RAISE_ERR_GENERICO('La scadenza dell''impegno residuo '||aObbligScad.pg_obbligazione||' del '||aObbligScad.esercizio_originale||
         ' sul capitolo '||aSaldo.cd_voce||' ('||To_Char(aObbligScad.IM_SCADENZA)||
         ') è già associata a documenti amministrativi per '||aObbligScad.im_associato_doc_amm||'. Non può essere inserita la variazione di '||DELTA_VAR_RES);
        else
         aNuovoImporto := aObbligScad.IM_SCADENZA + DELTA_VAR_RES;
         delta_saldo_cdr := Nvl(DELTA_VAR_RES, 0);
        end if;
    End If;

Else -- NON PER VARIAZIONE
     -- 24.01.2008 POTREBBE ANCHE ESSERE CHE LE CHIUSURE CONTINUE DEI PIANI DI GESTIONE INCREMENTANO LO STESSO IMPEGNO
     --            MA L'INCREMENTO NON E' CONSIDERATO PER VARIAZIONE

    -- L'importo dell'impegno non può essere inferiore al saldo dei documenti amministrativi

    if aSaldo.im_stanz_iniziale_a1 + aSaldo.VARIAZIONI_PIU - aSaldo.VARIAZIONI_MENO < aObbligScad.im_associato_doc_amm then
     aNuovoImporto := aObbligScad.im_associato_doc_amm;
/*
         IBMERR001.RAISE_ERR_GENERICO('Attenzione !! Lo stanziamento sulla voce '||aSaldo.cd_voce||' ('||
aSaldo.im_stanz_iniziale_a1||') risulterebbe inferiore a quanto già associato '||
'a documenti amministrativi ('||aObbligScad.im_associato_doc_amm||')');
*/
    else
     aNuovoImporto := aSaldo.im_stanz_iniziale_a1 + aSaldo.VARIAZIONI_PIU - aSaldo.VARIAZIONI_MENO;
     delta_saldo_cdr := Nvl(aSaldo.im_stanz_iniziale_a1, 0) + Nvl(aSaldo.VARIAZIONI_PIU, 0) - Nvl(aSaldo.VARIAZIONI_MENO, 0) - Nvl(aObbligScadVoce.im_voce, 0);
    end if;

End if;

---------------------------------- ASSEGNO IL NUOVO IMPORTO A TUTTE E TRE LE TABELLE ---------------------------------
------------------------------- A MENO CHE IL NUOVO VALORE NON SIA UGUALE AL VECCHIO ---------------------------------

   If aObblig.IM_OBBLIGAZIONE != aNuovoImporto And aNuovoImporto >= 0 Then
      Update obbligazione
      Set IM_OBBLIGAZIONE = aNuovoImporto,
   	  UTUV            = utente,
   	  DUVA            = Sysdate,
   	  PG_VER_REC      = PG_VER_REC+1
      Where CD_CDS          = aObbligScadVoce.cd_cds
       and  ESERCIZIO       = aObbligScadVoce.esercizio
       and  ESERCIZIO_ORIGINALE = aObbligScadVoce.esercizio_originale
       and  PG_OBBLIGAZIONE = aObbligScadVoce.pg_obbligazione;
   End If;

   If aObbligScad.IM_SCADENZA != aNuovoImporto And aNuovoImporto >= 0 Then
      Update obbligazione_scadenzario
      Set IM_SCADENZA     = aNuovoImporto,
    	  UTUV            = utente,
    	  DUVA            = Sysdate,
    	  PG_VER_REC      = PG_VER_REC+1
      Where CD_CDS                     = aObbligScadVoce.cd_cds
       and  ESERCIZIO                  = aObbligScadVoce.esercizio
       and  ESERCIZIO_ORIGINALE        = aObbligScadVoce.esercizio_originale
       and  PG_OBBLIGAZIONE            = aObbligScadVoce.pg_obbligazione
       And PG_OBBLIGAZIONE_SCADENZARIO = aObbligScadVoce.pg_obbligazione_scadenzario;
   End If;

   If aObbligScadVoce.IM_VOCE != aNuovoImporto And aNuovoImporto >= 0 Then
      Update obbligazione_scad_voce
      Set IM_VOCE    = aNuovoImporto,
    	  UTUV       = utente,
    	  DUVA       = Sysdate,
    	  PG_VER_REC = PG_VER_REC+1
       Where CD_CDS             = aObbligScadVoce.cd_cds
        and  ESERCIZIO          = aObbligScadVoce.esercizio
        and  ESERCIZIO_ORIGINALE = aObbligScadVoce.esercizio_originale
        and  PG_OBBLIGAZIONE    = aObbligScadVoce.pg_obbligazione
        and  PG_OBBLIGAZIONE_SCADENZARIO = aObbligScadVoce.pg_obbligazione_scadenzario
        and  TI_APPARTENENZA    = aObbligScadVoce.ti_appartenenza
        and  TI_GESTIONE        = aObbligScadVoce.ti_gestione;
   End If;

/*Rospuc */
if(aNuovoImportoRib !=0 ) then
 If aObbligRib.IM_OBBLIGAZIONE != aNuovoImportoRib And aNuovoImportoRib >= 0 Then
      Update obbligazione
      Set IM_OBBLIGAZIONE = aNuovoImportoRib,
   	  UTUV            = utente,
   	  DUVA            = Sysdate,
   	  PG_VER_REC      = PG_VER_REC+1
      Where CD_CDS          = aObbligScadVoceRib.cd_cds
       and  ESERCIZIO       = aObbligScadVoceRib.esercizio
       and  ESERCIZIO_ORIGINALE = aObbligScadVoceRib.esercizio_originale
       and  PG_OBBLIGAZIONE = aObbligScadVoceRib.pg_obbligazione;
   End If;

   If aObbligScadRib.IM_SCADENZA != aNuovoImportoRib And aNuovoImportoRib >= 0 Then
      Update obbligazione_scadenzario
      Set IM_SCADENZA     = aNuovoImportoRib,
    	  UTUV            = utente,
    	  DUVA            = Sysdate,
    	  PG_VER_REC      = PG_VER_REC+1
      Where CD_CDS                     = aObbligScadVoceRib.cd_cds
       and  ESERCIZIO                  = aObbligScadVoceRib.esercizio
       and  ESERCIZIO_ORIGINALE        = aObbligScadVoceRib.esercizio_originale
       and  PG_OBBLIGAZIONE            = aObbligScadVoceRib.pg_obbligazione
       And PG_OBBLIGAZIONE_SCADENZARIO = aObbligScadVoceRib.pg_obbligazione_scadenzario;
   End If;

   If aObbligScadVoceRib.IM_VOCE != aNuovoImportoRib And aNuovoImportoRib >= 0 Then
      Update obbligazione_scad_voce
      Set IM_VOCE    = aNuovoImportoRib,
    	  UTUV       = utente,
    	  DUVA       = Sysdate,
    	  PG_VER_REC = PG_VER_REC+1
       Where CD_CDS             = aObbligScadVoceRib.cd_cds
        and  ESERCIZIO          = aObbligScadVoceRib.esercizio
        and  ESERCIZIO_ORIGINALE = aObbligScadVoceRib.esercizio_originale
        and  PG_OBBLIGAZIONE    = aObbligScadVoceRib.pg_obbligazione
        and  PG_OBBLIGAZIONE_SCADENZARIO = aObbligScadVoceRib.pg_obbligazione_scadenzario
        and  TI_APPARTENENZA    = aObbligScadVoceRib.ti_appartenenza
        and  TI_GESTIONE        = aObbligScadVoceRib.ti_gestione;
   End If;
end if;

  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  aSaldoCdrLinea.ESERCIZIO                := aObbligScadVoce.ESERCIZIO;
  aSaldoCdrLinea.ESERCIZIO_RES            := Nvl(aObblig.ESERCIZIO_ORIGINALE, aObblig.ESERCIZIO);
  aSaldoCdrLinea.CD_CENTRO_RESPONSABILITA := aObbligScadVoce.CD_CENTRO_RESPONSABILITA;
  aSaldoCdrLinea.CD_LINEA_ATTIVITA        := aObbligScadVoce.CD_LINEA_ATTIVITA;
  aSaldoCdrLinea.TI_APPARTENENZA          := aObbligScadVoce.TI_APPARTENENZA;
  aSaldoCdrLinea.TI_GESTIONE              := aObbligScadVoce.TI_GESTIONE;
  aSaldoCdrLinea.CD_VOCE                  := aObbligScadVoce.CD_VOCE;

   CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLinea);
if (aNuovoImportoRib!= 0) then

  aSaldoCdrLineaRib.ESERCIZIO                := aObbligScadVoceRib.ESERCIZIO;
  aSaldoCdrLineaRib.ESERCIZIO_RES            := Nvl(aObbligRib.ESERCIZIO_ORIGINALE, aObbligRib.ESERCIZIO);
  aSaldoCdrLineaRib.CD_CENTRO_RESPONSABILITA := aObbligScadVoceRib.CD_CENTRO_RESPONSABILITA;
  aSaldoCdrLineaRib.CD_LINEA_ATTIVITA        := aObbligScadVoceRib.CD_LINEA_ATTIVITA;
  aSaldoCdrLineaRib.TI_APPARTENENZA          := aObbligScadVoceRib.TI_APPARTENENZA;
  aSaldoCdrLineaRib.TI_GESTIONE              := aObbligScadVoceRib.TI_GESTIONE;
  aSaldoCdrLineaRib.CD_VOCE                  := aObbligScadVoceRib.CD_VOCE;
  CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLineaRib);
end if;


--  aSaldoCdrLinea.IM_OBBL_ACC_COMP := aNuovoImporto - aObbligScadVoce.IM_VOCE;
-- CREAZIONE IMPEGNO COMPETENZA, INUTILE TUTTI GLI IF.......(QUELLI CHE C'ERANO PRIMA)
-- ERRORE DA CORREGGERE, SE LA COMPETENZA GIA' C'E' DEVE ESSERE RICOPERTA.

  If isPerVariazione Then  -- VARIAZIONE
     If TIPO_IMPEGNO = CNRCTB018.TI_DOC_IMP_RES Then  -- VARIAZIONE DI UN RESIDUO
         If delta_saldo_cdr >= 0 Then  -- POSITIVA
             aSaldocdrlinea.VAR_PIU_OBBL_RES_PRO := delta_saldo_cdr;
         Elsif delta_saldo_cdr <= 0 Then  -- VARIAZIONE DI UN RESIDUO
             aSaldocdrlinea.VAR_MENO_OBBL_RES_PRO := Abs(delta_saldo_cdr);
         End If;
     Else -- VARIAZIONE DI UN COMPETENZA
         aSaldocdrlinea.IM_OBBL_ACC_COMP := delta_saldo_cdr;
     End If;
     -- variazione impegno ribaltato sempre residuo
     if (aNuovoImportoRib!=0) then
         aSaldocdrlineaRib.IM_OBBL_RES_PRO := delta_saldo_cdrRib;
     end if;
  Else -- NON VARIAZIONE MA CREAZIONE ...
       -- 24.01.2008 ... O INCREMENTO DI IMPEGNO GIA' ESISTENTE PER CHIUSURA NUOVI PDG DA PARTE DI ALTRI CDR
     If TIPO_IMPEGNO = CNRCTB018.TI_DOC_IMP_RES Then  -- CREAZIONE DI UN RESIDUO
         aSaldocdrlinea.IM_OBBL_RES_PRO := delta_saldo_cdr;
     Else -- COMPETENZA
         aSaldocdrlinea.IM_OBBL_ACC_COMP := delta_saldo_cdr;
     End If;
  End If;

  aSaldoCdrLinea.UTUV := utente;

  CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLinea, '030.aggiornaImpegnoCapitolo', 'N');

  if (aNuovoImportoRib!=0) then
    aSaldoCdrLineaRib.UTUV := utente;
    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLineaRib, '030.aggiornaImpegnoCapitolo', 'N');
  end if;

  -- Se l'impegno non esiste e l'aggiornamento non è per variazione di bilancio, l'impegno viene creato
  -- Se l'aggiornamento è per variazione di bilancio l'impegno deve essere già presente (eventualmente con importo a 0) NON PIU' !!!!

  -- ATTENZIONE !!!! 16.05.2006
  -- DAL 2006 NON E' PIU' VERO CHE L'IMPEGNO CI DEVE ESSERE PER FORZA, ANCHE SE A ZERO.
  -- QUINDI, PER VARIAZIONE, SE NON C'E' OCCORRE CREARLO.

Exception
    When Too_Many_Rows Then
      IBMERR001.RAISE_ERR_GENERICO('Esiste più di un '||DES_TIPO_IMPEGNO||' del CDS '||CNRCTB020.GETCDCDSENTE(aSaldo.esercizio)||
' dell''esercizio '||aSaldo.esercizio||' (proveniente dal '||aSaldo.esercizio_res||') per la Voce '||aSaldo.cd_voce);
    When No_Data_Found then

 ---- SE NON TROVO L'IMPEGNO LO DEVO CREARE (A MENO CHE NON SIA PER VARIAZIONE NEL QUAL CASO DEVE ESISTERE PER FORZA)  ------
 ---- 16.05.2006 SE NON TROVO L'IMPEGNO LO DEVO CREARE ANCHE PER VARIAZIONE

    If isPerVariazione And Nvl(DELTA_VAR_RES, 0) < 0 Then
      If TIPO_IMPEGNO = CNRCTB018.TI_DOC_IMP THEN
       IBMERR001.RAISE_ERR_GENERICO(DES_TIPO_IMPEGNO||' del CDS '||CNRCTB020.GETCDCDSENTE(aSaldo.esercizio)||
', esercizio '||aSaldo.esercizio||' sulla Voce '||aSaldo.cd_voce||
' non trovato, con stato non valido oppure senza disponibilità sufficiente (importo variazione '||DELTA_VAR_RES||').');
      Elsif TIPO_IMPEGNO = CNRCTB018.TI_DOC_IMP_RES Then
       IBMERR001.RAISE_ERR_GENERICO(DES_TIPO_IMPEGNO||' del CDS '||CNRCTB020.GETCDCDSENTE(aSaldo.esercizio)||
', esercizio '||aSaldo.esercizio||' (proveniente dal '||aSaldo.esercizio_res||') sulla Voce '||aSaldo.cd_voce||
' non trovato, con stato non valido oppure senza disponibilità sufficiente (importo variazione '||DELTA_VAR_RES||').');
      End If;
    End if;

    select * into aUOENTE
    from v_unita_organizzativa_valida
    Where esercizio=aSaldo.esercizio
    and cd_unita_padre = CNRUTL001.GETCDSFROMCDR(aSaldo.cd_centro_responsabilita)
    and cd_tipo_unita = CNRCTB020.TIPO_ENTE
    and fl_uo_cds='Y';

    select *
    into aCDRENTE
    from v_cdr_valido
    Where esercizio=aSaldo.esercizio
      and cd_unita_organizzativa = aUOENTE.cd_unita_organizzativa;

   -- Estrae da voce_f la tipologia di intervento (per categoria 2) o la categoria (per categoria 1) sempre per parte 1
   -- NON PIU', CE L'HA SOPRA IL RECORD DEL SALDO

   aCodTerzoDivImpegni := CNRCTB015.GETIM01PERCHIAVE(CNRCTB016.TERZO_SPECIALE,CNRCTB016.CODICE_DIVERSI_IMPEGNI);

   if aCodTerzoDivImpegni is null then
    IBMERR001.RAISE_ERR_GENERICO('Codice terzo ''Diversi'' da utilizzare come entità anagrafica per l''impegno, non specificato in Configurazione CNR');
   end if;

   begin
    select * into aTerzoTemp from terzo
     where cd_terzo = aCodTerzoDivImpegni;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Codice terzo ''Diversi'' da utilizzare come entità anagrafica per l''impegno, non trovato nella tabella TERZO');
   end;

   aNumeratore:=CNRCTB018.getNextNumDocCont(TIPO_IMPEGNO, aSaldo.esercizio, CNRCTB020.GETCDCDSENTE (aSaldo.esercizio), aSaldo.utuv);
   aObblig.ESERCIZIO:=aSaldo.esercizio;
   aObblig.ESERCIZIO_ORIGINALE:=aSaldo.esercizio_res;
   aObblig.CD_CDS := CNRCTB020.GETCDCDSENTE (aSaldo.esercizio);
   aObblig.ESERCIZIO_ORIGINALE:=aSaldo.esercizio_res;
   aObblig.PG_OBBLIGAZIONE:=aNumeratore;
   aObblig.CD_TIPO_DOCUMENTO_CONT:=TIPO_IMPEGNO;
   aObblig.CD_UNITA_ORGANIZZATIVA:=aCDRENTE.cd_unita_organizzativa;
   aObblig.CD_CDS_ORIGINE := CNRCTB020.GETCDCDSENTE (aSaldo.esercizio);
   aObblig.CD_UO_ORIGINE:=aCDRENTE.cd_unita_organizzativa;
   aObblig.CD_TIPO_OBBLIGAZIONE:=NULL;
   aObblig.TI_APPARTENENZA:=aSaldo.ti_appartenenza;
   aObblig.TI_GESTIONE:=aSaldo.ti_gestione;
   aObblig.CD_ELEMENTO_VOCE:= aSaldo.CD_ELEMENTO_VOCE;
   aObblig.DT_REGISTRAZIONE := trunc(aSaldo.duva);

   If isPerVariazione Then
        aObblig.DS_OBBLIGAZIONE := 'Impegno relativo a capitolo '||aSaldo.cd_voce||' generato a seguito dell''approvazione di una variazione';
   Else
        aObblig.DS_OBBLIGAZIONE := 'Impegno relativo a capitolo '||aSaldo.cd_voce;
   End If;

   aObblig.NOTE_OBBLIGAZIONE:=NULL;
   aObblig.CD_TERZO:=aCodTerzoDivImpegni;

   If isPerVariazione Then
        aObblig.IM_OBBLIGAZIONE := Nvl(DELTA_VAR_RES, 0);
   Else
        aObblig.IM_OBBLIGAZIONE := aSaldo.im_stanz_iniziale_a1;
   End If;

   aObblig.IM_COSTI_ANTICIPATI:=0;
   aObblig.ESERCIZIO_COMPETENZA:=aSaldo.esercizio;
   aObblig.STATO_OBBLIGAZIONE:='D';
   aObblig.DT_CANCELLAZIONE:=NULL;
   aObblig.CD_RIFERIMENTO_CONTRATTO:=NULL;
   aObblig.DT_SCADENZA_CONTRATTO:=NULL;
   aObblig.FL_CALCOLO_AUTOMATICO:='Y';
   aObblig.CD_FONDO_RICERCA:=NULL;
   aObblig.FL_SPESE_COSTI_ALTRUI:='N';
   aObblig.FL_PGIRO:='N';
   aObblig.DACR:=aTSNow;
   aObblig.UTCR:=utente;
   aObblig.DUVA:=aTSNow;
   aObblig.UTUV:=utente;
   aObblig.RIPORTATO:='N';
   aObblig.PG_VER_REC:=1;

   aNuovoImporto := aSaldo.im_stanz_iniziale_a1;

   If aObblig.IM_OBBLIGAZIONE != 0 Then
      CNRCTB035.ins_OBBLIGAZIONE(aObblig);
   End If;

   aObbligScad.ESERCIZIO:=aSaldo.esercizio;
   aObbligScad.CD_CDS:= CNRCTB020.GETCDCDSENTE (aSaldo.esercizio);
   aObbligScad.ESERCIZIO_ORIGINALE:=aSaldo.esercizio_res;
   aObbligScad.PG_OBBLIGAZIONE:=aNumeratore;
   aObbligScad.PG_OBBLIGAZIONE_SCADENZARIO:=1;
   aObbligScad.DT_SCADENZA:=trunc(aSaldo.duva);

   If isPerVariazione Then
        aObbligScad.DS_SCADENZA := 'Impegno relativo a capitolo '||aSaldo.cd_voce||' generato a seguito dell''approvazione di una variazione.';
   Else
        aObbligScad.DS_SCADENZA := 'Impegno relativo a capitolo '||aSaldo.cd_voce;
   End If;

   aObbligScad.IM_SCADENZA:=aObblig.IM_OBBLIGAZIONE;
   aObbligScad.IM_ASSOCIATO_DOC_AMM:=0;
   aObbligScad.IM_ASSOCIATO_DOC_CONTABILE:=0;
   aObbligScad.DACR:=aTSNow;
   aObbligScad.UTCR:=utente;
   aObbligScad.DUVA:=aTSNow;
   aObbligScad.UTUV:=utente;
   aObbligScad.PG_VER_REC:=1;

   If aObbligScad.IM_SCADENZA != 0 Then
     CNRCTB035.ins_OBBLIGAZIONE_SCADENZARIO(aObbligScad);
   End If;

   aObbligScadVoce.ESERCIZIO:=aSaldo.esercizio;
   aObbligScadVoce.CD_CDS := CNRCTB020.GETCDCDSENTE (aSaldo.esercizio);
   aObbligScadVoce.ESERCIZIO_ORIGINALE:=aSaldo.esercizio_res;
   aObbligScadVoce.PG_OBBLIGAZIONE:=aNumeratore;
   aObbligScadVoce.PG_OBBLIGAZIONE_SCADENZARIO:=1;
   aObbligScadVoce.TI_APPARTENENZA:=aSaldo.ti_appartenenza;
   aObbligScadVoce.TI_GESTIONE:=aSaldo.ti_gestione;
   aObbligScadVoce.CD_VOCE:=aSaldo.cd_voce;
   aObbligScadVoce.CD_CENTRO_RESPONSABILITA:=aCDRENTE.cd_centro_responsabilita;
   aObbligScadVoce.CD_LINEA_ATTIVITA:=CNRCTB015.getVal02PerChiave(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_SPESA_ENTE);
   aObbligScadVoce.IM_VOCE:=aObbligScad.IM_SCADENZA;
   aObbligScadVoce.CD_FONDO_RICERCA:=NULL;
   aObbligScadVoce.DACR:=aTSNow;
   aObbligScadVoce.UTCR:=utente;
   aObbligScadVoce.DUVA:=aTSNow;
   aObbligScadVoce.UTUV:=utente;
   aObbligScadVoce.PG_VER_REC:=1;

   If aObbligScadVoce.IM_VOCE != 0 Then
     CNRCTB035.ins_OBBLIGAZIONE_SCAD_VOCE(aObbligScadVoce);
   End If;

  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  aSaldoCdrLinea.ESERCIZIO := aObbligScadVoce.ESERCIZIO;
  aSaldoCdrLinea.ESERCIZIO_RES := Nvl(aObblig.ESERCIZIO_ORIGINALE, aObblig.ESERCIZIO);
  aSaldoCdrLinea.CD_CENTRO_RESPONSABILITA := aObbligScadVoce.CD_CENTRO_RESPONSABILITA;
  aSaldoCdrLinea.CD_LINEA_ATTIVITA := aObbligScadVoce.CD_LINEA_ATTIVITA;
  aSaldoCdrLinea.TI_APPARTENENZA := aObbligScadVoce.TI_APPARTENENZA;
  aSaldoCdrLinea.TI_GESTIONE := aObbligScadVoce.TI_GESTIONE;
  aSaldoCdrLinea.CD_VOCE := aObbligScadVoce.CD_VOCE;

  CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLinea);

--  aSaldoCdrLinea.IM_OBBL_ACC_COMP := aNuovoImporto - aObbligScadVoce.IM_VOCE;
-- CREAZIONE IMPEGNO COMPETENZA, INUTILE TUTTI GLI IF.......(QUELLI CHE C'ERANO PRIMA)

  -- se e' competenza
  If aSaldoCdrLinea.ESERCIZIO = aSaldoCdrLinea.ESERCIZIO_RES Then
    -- o è variazione
    If isPerVariazione Then
       --aSaldocdrlinea.VARIAZIONI_PIU := aObbligScadVoce.IM_VOCE;
       aSaldocdrlinea.IM_OBBL_ACC_COMP := aObbligScadVoce.IM_VOCE;
    Else
    -- o impegno
       aSaldocdrlinea.IM_OBBL_ACC_COMP := aObbligScadVoce.IM_VOCE;
    End If;
  Elsif aSaldoCdrLinea.ESERCIZIO > aSaldoCdrLinea.ESERCIZIO_RES Then
  -- se e' residuo
    If isPerVariazione Then
    -- o è variazione
       aSaldocdrlinea.VAR_PIU_OBBL_RES_PRO := aObbligScadVoce.IM_VOCE;
    Else
    -- o è residuo
       aSaldocdrlinea.IM_OBBL_RES_PRO := aObbligScadVoce.IM_VOCE;
    End If;
  End If;


  aSaldoCdrLinea.UTUV := utente;

  CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLinea, '030.aggiornaImpegnoCapitolo', 'N');


End;
--Fine  exception when no_data_found
if(aNuovoImportoRib!=0) then
  update voce_f_saldi_cmp
    Set   im_obblig_imp_acr = aNuovoImportoRib,
          utuv=aSaldo.utuv,
          duva=aSaldo.duva,
          pg_ver_rec=pg_ver_rec+1
    Where esercizio = aObbligRib.esercizio
   and cd_cds = aObbligRib.cd_cds
   and ti_appartenenza = aObbligRib.ti_appartenenza
   and ti_gestione = aObbligRib.ti_gestione
   and cd_voce = aObbligScadVoceRib.cd_voce
   and ti_competenza_residuo = Decode (TIPO_IMPEGNO_RIB, CNRCTB018.TI_DOC_IMP, CNRCTB054.TI_COMPETENZA, CNRCTB054.TI_RESIDUI);
end if;
  update voce_f_saldi_cmp
  Set   im_obblig_imp_acr = aNuovoImporto,
        utuv=aSaldo.utuv,
        duva=aSaldo.duva,
        pg_ver_rec=pg_ver_rec+1
  Where esercizio = aObblig.esercizio
   and cd_cds = aObblig.cd_cds
   and ti_appartenenza = aObblig.ti_appartenenza
   and ti_gestione = aObblig.ti_gestione
   and cd_voce = aObbligScadVoce.cd_voce
   and ti_competenza_residuo = Decode (TIPO_IMPEGNO, CNRCTB018.TI_DOC_IMP, CNRCTB054.TI_COMPETENZA, CNRCTB054.TI_RESIDUI);

 end;


Procedure aggiornaImpegnoCapitolo(aEs varchar2, aEs_Residuo NUMBER, aVoceF varchar2, isPerVariazione Boolean, DELTA_VAR_RES NUMBER, utente VARCHAR2) is
  aUOENTE v_unita_organizzativa_valida%rowtype;
  aSaldo voce_f_saldi_cDR_LINEA%rowtype;
Begin

  if isPerVariazione is null then
   IBMERR001.RAISE_ERR_GENERICO('Errore interno in aggiornamento impegno capitolo.');
  end if;

Dbms_Output.PUT_LINE ('dati input '||aEs||' '||aEs_Residuo||' '||aVoceF||' '||DELTA_VAR_RES) ;
  begin

Dbms_Output.PUT_LINE ('a');

   select * into aUOENTE
   from  v_unita_organizzativa_valida
   Where esercizio=aEs
     and cd_tipo_unita = CNRCTB020.TIPO_ENTE
     and fl_uo_cds='Y';

  -- VISTO CHE E' STATA USATA IMPROPRIAMENTE SIA LA P1 CHE LA P2 CERCO SOLO IL SALDO CON LA LINEA BUONA

  Select * into aSaldo
   From  VOCE_F_SALDI_CDR_LINEA
   Where esercizio = aEs
    And  ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
    And  ESERCIZIO_RES = aEs_Residuo
    And  cd_Voce = aVoceF
    And CD_LINEA_ATTIVITA = CNRCTB015.getVal02PerChiave(0, 'LINEA_ATTIVITA_SPECIALE',  'LINEA_ATTIVITA_SPESA_ENTE')
    And  exists (select 1 from voce_f
                 Where  esercizio = aEs And
                        ti_appartenenza = CNRCTB001.APPARTENENZA_CNR And
                        ti_gestione = CNRCTB001.GESTIONE_SPESE  And
                        cd_voce = aVoceF And
                        cd_parte = CNRCTB001.PARTE1);

   aggiornaImpegnoCapitolo(aSaldo, isPerVariazione, DELTA_VAR_RES, utente);
Dbms_Output.PUT_LINE ('d');
  Exception
    When NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Non esiste alcun Impegno dell''Ente sull''esercizio '||aEs||', esercizio residuo '||aEs_Residuo||
', Voce '||aVoceF||' e GAE '||CNRCTB015.getVal02PerChiave(0, 'LINEA_ATTIVITA_SPECIALE',  'LINEA_ATTIVITA_SPESA_ENTE')||
' (non esistono i saldi per Voce/CdR/GAE)');
    When Too_Many_Rows then
        IBMERR001.RAISE_ERR_GENERICO('Esistono impegni dell''Ente su diversi GAE sull''esercizio '||aEs||', esercizio residuo '||aEs_Residuo||
', Voce '||aVoceF||'  e GAE '||CNRCTB015.getVal02PerChiave(0, 'LINEA_ATTIVITA_SPECIALE',  'LINEA_ATTIVITA_SPESA_ENTE')||
'(esistono più saldi per Voce/CdR/GAE)');
  End;
End;

 Procedure aggiornaImpegnoCapitolo(aSaldo voce_f_saldi_cdr_linea%Rowtype, DELTA_VAR_RES NUMBER, utente VARCHAR2) is
 begin
  aggiornaImpegnoCapitolo(aSaldo, False, DELTA_VAR_RES, utente);
 end;


 procedure aggiornaImpegnoCapitolo(aEs varchar2, aEs_residuo NUMBER, aVoceF VARCHAR2, DELTA_VAR_RES NUMBER, utente VARCHAR2) is
 begin
  aggiornaImpegnoCapitolo(aEs, aEs_residuo, aVoceF, False, DELTA_VAR_RES, utente);
 end;



 procedure aggiornaImpegnoCapitoloVar(aEs varchar2, aEs_Residuo NUMBER, aVoceF VARCHAR2, DELTA_VAR_RES NUMBER, utente VARCHAR2) is
 begin
  aggiornaImpegnoCapitolo(aEs, aEs_Residuo, aVoceF, True, DELTA_VAR_RES, utente);
 end;


-- Registrazione obbligazione su partita di giro

 procedure creaObbligazionePgiroInt(
  isControlloBloccante boolean,
  isTrunc boolean,
  aObb IN OUT obbligazione%rowtype,
  aObbScad IN OUT obbligazione_scadenzario%rowtype,
  aAcc OUT accertamento%rowtype,
  aAccScad OUT accertamento_scadenzario%rowtype,
  aDtScadenza date
 ) is
  aNumeratore number;
  aDettScadC obbligazione_scad_voce%rowtype;
  aDettScadCList CNRCTB035.scadVoceListS;
  aDettScadenza accertamento_scad_voce%rowtype;
  aDettScadenzaList CNRCTB035.scadVoceListE;
  aEV elemento_voce%rowtype;
  aEVContr elemento_voce%rowtype;
  aAssPGiro ass_partita_giro%rowtype;
  aCdCdr varchar2(30);
  aCdLa varchar2(10);
  aVoceF voce_f%rowtype;
  aVoceFContr voce_f%rowtype;
  aCdTerzoContropartita number(10);
  aCDS unita_organizzativa%rowtype;
  aCDSOrigine unita_organizzativa%rowtype;
  aAssDocPGiro ass_obb_acr_pgiro%rowtype;
  aTipoDocContr varchar2(10);
  aSaldoCdrLineaObb voce_f_saldi_cdr_linea%Rowtype;
  aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;
  aGAE_dedicata_CDS  linea_attivita%Rowtype;
  parametriCnr parametri_cnr%Rowtype;
 Begin

-- N.B. SE SI TRATTA DI RIBALTAMENTO GLI ARRIVA LA TIPOLOGIA DELLA PGIRO RESIDUA GIA' CAMBIATA
Dbms_Output.put_line ('dentro creaObbligazionePgiroInt');

  if aObb.fl_pgiro is null or aObb.fl_pgiro <> 'Y' then
   IBMERR001.RAISE_ERR_GENERICO('Documento non su partita di giro');
  end if;

  if aObb.cd_tipo_documento_cont is null Or
     aObb.cd_tipo_documento_cont not in (CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES,
                                         CNRCTB018.TI_DOC_IMP, CNRCTB018.TI_DOC_IMP_RES) then
   IBMERR001.RAISE_ERR_GENERICO('Documento non compatibile (1)');
  end if;

  begin
   select * into aEV from elemento_voce where
        esercizio = aObb.esercizio
	and ti_gestione = aObb.ti_gestione
	and ti_appartenenza = aObb.ti_appartenenza
    and cd_elemento_voce = aObb.cd_elemento_voce
    and fl_partita_giro = 'Y';
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Voce del piano partita di giro di spesa non trovata');
  end;
  aCDS := CNRCTB020.GETCDSVALIDO(aObb.esercizio, aObb.cd_cds);
  aCDSOrigine := CNRCTB020.GETCDSVALIDO(aObb.esercizio, aObb.cd_cds_origine);

  if (parametriCnr.fl_nuovo_pdg='N' ) then
	  begin
	   select * into aVoceF from voce_f where
	        esercizio = aEV.esercizio
		and ti_gestione = aEV.ti_gestione
		and ti_appartenenza = aEV.ti_appartenenza
		and cd_voce = aObb.cd_elemento_voce -- In spesa la partita di giro non è articolata
	    and fl_mastrino = 'Y';
	  exception when NO_DATA_FOUND then
	   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario partita di giro di spesa non trovato');
	  end;

	  if aCDS.cd_tipo_unita = CNRCTB020.TIPO_ENTE and aEV.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS then
	   IBMERR001.RAISE_ERR_GENERICO('La voce del piano specificata non è una voce del piano dell''ENTE');
	  end if;

	  if aCDS.cd_tipo_unita <> CNRCTB020.TIPO_ENTE and aEV.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
	   IBMERR001.RAISE_ERR_GENERICO('La voce del piano specificata non è una voce del piano di CDS');
	  end if;
  end if;
  if aObb.esercizio != aObb.esercizio_competenza then
   IBMERR001.RAISE_ERR_GENERICO('1. Generazione automatica di '||cnrutil.getLabelObbligazioneMin()||' in esercizi futuri non supportata!');
  end if;

-- 17.10.2007 SF: NUMERA LE PARTITE DI GIRO SOLO SE NON SONO RESIDUE !!!!
  If aObb.cd_tipo_documento_cont != CNRCTB018.TI_DOC_OBB_PGIRO_RES Then
    aNumeratore := CNRCTB018.getNextNumDocCont(aObb.cd_tipo_documento_cont, aObb.esercizio, aObb.cd_cds, aObb.utcr);
  Else
    aNumeratore := aObb.pg_obbligazione; -- rimane il vecchio numero
  End If;

  --Se l'esercizio originale non è valorizzato lo inizializzo con quello dell'esercizio
  If (aObb.esercizio_originale Is Null) Then
     aObb.esercizio_originale := aObb.esercizio;
  End If;

  aObb.PG_obbligazione := aNumeratore;

  aObb.dt_registrazione := trunc(aObb.dt_registrazione);
  aObb.duva:=aObb.dacr;
  aObb.utuv:=aObb.utcr;
  aObb.pg_ver_rec:=1;
  CNRCTB035.INS_OBBLIGAZIONE(aObb);

  aObbScad.cd_cds := aObb.cd_cds;
  aObbScad.esercizio := aObb.esercizio;
  aObbScad.esercizio_originale := aObb.esercizio_originale;
  aObbScad.pg_obbligazione:=aObb.pg_obbligazione;
  aObbScad.pg_obbligazione_scadenzario:=1;
  aObbScad.dt_scadenza := TRUNC(nvl(aDtScadenza,aObb.dt_registrazione));
  aObbScad.ds_scadenza := aObb.ds_obbligazione;
  aObbScad.im_scadenza := aObb.im_obbligazione;
  aObbScad.im_associato_doc_amm := 0;
  aObbScad.im_associato_doc_contabile := 0;
  aObbScad.dacr:=aObb.dacr;
  aObbScad.utcr:=aObb.utcr;
  aObbScad.duva:=aObb.dacr;
  aObbScad.utuv:=aObb.utcr;
  aObbScad.pg_ver_rec:=1;

  If CNRCTB015.UtilizzaGAEdedicataPgiroCDS (aObb.esercizio, aObb.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_SPESE) Then
    aGAE_dedicata_CDS := CNRCTB015.get_LINEA_PGIRO_cds(aObb.esercizio, aObb.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_SPESE);
    aCdCdr := aGAE_dedicata_CDS.CD_CENTRO_RESPONSABILITA;
    aCdLa  := aGAE_dedicata_CDS.CD_LINEA_ATTIVITA;
  Else
    aCdCdr:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_SPESA_ENTE);
    aCdLa:=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_SPESA_ENTE);
  End If;

  aDettScadC.cd_cds := aObb.cd_cds;
  aDettScadC.esercizio := aObb.esercizio;
  aDettScadC.esercizio_originale := aObb.esercizio_originale;
  aDettScadC.pg_obbligazione:=aObb.pg_obbligazione;
  aDettScadC.pg_obbligazione_scadenzario:=1;
  aDettScadC.ti_appartenenza := aEV.ti_appartenenza;
  aDettScadC.ti_gestione := aEV.ti_gestione;
  aDettScadC.cd_voce := aEV.cd_elemento_voce;
  aDettScadC.cd_centro_responsabilita := aCdCdr;
  aDettScadC.cd_linea_attivita := aCdLa;
  aDettScadC.im_voce := aObb.im_obbligazione;
  aDettScadC.cd_fondo_ricerca:=null;
  aDettScadC.dacr:=aObb.dacr;
  aDettScadC.utcr:=aObb.utcr;
  aDettScadC.duva:=aObb.dacr;
  aDettScadC.utuv:=aObb.utcr;
  aDettScadC.pg_ver_rec:=1;
  aDettScadCList(1):=aDettScadC;
  CNRCTB035.creaScadObbligazione(aObb,aObbScad,1,aDettScadCList,false);

-- 18.10.2007 S.F. QUA SI SDOPPIA LA GESTIONE:
--            SE LA PARTITA DI GIRO E' DA CREARE FA TUTTE QUESTE COSE CHE FACEVA PRIMA
--            ALTRIMENTI RECUPERA QUELLA DELL'ANNO PRIMA E "RIBALTA" QUELLA

--If aObb.cd_tipo_documento_cont != CNRCTB018.TI_DOC_OBB_PGIRO_RES Then

  -- gestione contropartita

  aCdTerzoContropartita := CNRCTB015.GETIM01PERCHIAVE(CNRCTB035.TERZO_SPECIALE,CNRCTB035.CODICE_DIVERSI_PGIRO);

  Begin
   Select *
   Into  aAssPgiro
   From  ass_partita_giro
   Where esercizio = aObb.esercizio And
         ti_appartenenza_clg = aObb.ti_appartenenza And
         ti_gestione_clg = aObb.ti_gestione And
         cd_voce_clg = aObb.cd_elemento_voce;
  Exception
     When NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Conto di partita di giro associato non trovato');
  End;

  Begin
   Select *
   Into   aEVContr
   From   elemento_voce
   Where  esercizio = aAssPgiro.esercizio And
          ti_gestione = aAssPgiro.ti_gestione And
          ti_appartenenza = aAssPgiro.ti_appartenenza And
          cd_elemento_voce = aAssPgiro.cd_voce And
          fl_partita_giro = 'Y';
  Exception
     When NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Voce del piano partita di giro di controparte spesa non trovato');
  End;
  if(parametriCnr.fl_nuovo_pdg ='N' ) then
  Begin
   Select *
   Into   aVoceFContr
   From   voce_f
   Where  esercizio = aEVContr.esercizio And
          ti_gestione = aEvContr.ti_gestione And
          ti_appartenenza = aEVContr.ti_appartenenza And
          cd_titolo_capitolo = aEVContr.cd_elemento_voce And
          fl_mastrino = 'Y' And
         -- Nel caso di pgiro su ente è necessaria l'UO per determinare il cap. entrata
         (aEVContr.ti_appartenenza=CNRCTB001.APPARTENENZA_CDS Or cd_unita_organizzativa = aObb.cd_unita_organizzativa);
  Exception
    When NO_DATA_FOUND Then
        IBMERR001.RAISE_ERR_GENERICO('Conto finanziario partita di giro di controparte spesa non trovato');
  End;
	end if;
  If aCDS.cd_tipo_unita = CNRCTB020.TIPO_ENTE then
     if aObb.cd_tipo_documento_cont is not null and aObb.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_IMP_RES) then
        aTipoDocContr       := CNRCTB018.TI_DOC_ACC_RES;
     else
        aTipoDocContr       := CNRCTB018.TI_DOC_ACC;
     end if;
     aNumeratore         := CNRCTB018.getNextNumDocCont(aTipoDocContr, aObb.esercizio, aObb.cd_cds, aObb.utcr);
     aAcc.cd_cds_origine := aObb.cd_cds;
     aAcc.cd_uo_origine  := aObb.cd_unita_organizzativa;
  Else
     if aObb.esercizio_originale  is not null and aObb.esercizio != aObb.esercizio_originale then
       aTipoDocContr  := CNRCTB018.TI_DOC_ACC_PGIRO_RES;
       aNumeratore    := CNRCTB018.getNextNumDocCont(aTipoDocContr, aObb.esercizio_originale, aObb.cd_cds, aObb.utcr);
	   else
       aTipoDocContr  := CNRCTB018.TI_DOC_ACC_PGIRO;
       aNumeratore    := CNRCTB018.getNextNumDocCont(aTipoDocContr, aObb.esercizio, aObb.cd_cds, aObb.utcr);
	 	 end if;
     aAcc.cd_cds_origine := aObb.cd_cds_origine;
     aAcc.cd_uo_origine  := aObb.cd_uo_origine;
  End If;

Dbms_Output.put_line ('dentro creaObbligazionePgiroInt inizia acc');

  aAcc.cd_cds := aObb.cd_cds;
  aAcc.esercizio := aObb.esercizio;
  aAcc.esercizio_competenza := aObb.esercizio_competenza;
  aAcc.cd_tipo_documento_cont:=aTipoDocContr;
  aAcc.cd_unita_organizzativa := aObb.cd_unita_organizzativa;
  aAcc.esercizio_originale := aObb.esercizio_originale;

  aAcc.pg_accertamento := aNumeratore;

  aAcc.dt_registrazione := aObb.dt_registrazione;
  aAcc.ds_accertamento := aObb.ds_obbligazione;
  aAcc.note_accertamento := '';
  aAcc.cd_terzo := aCdTerzoContropartita;
  aAcc.ti_gestione:=aEVContr.ti_gestione;
  aAcc.ti_appartenenza:=aEVContr.ti_appartenenza;
  aAcc.cd_elemento_voce:=aEVContr.cd_elemento_voce;
  if(parametriCnr.fl_nuovo_pdg ='N' ) then
  	aAcc.CD_VOCE:=aVoceFContr.cd_voce;
 else
  	aAcc.CD_VOCE:=aEVContr.cd_elemento_voce;
 end if;

   /* 05.01.2006 stani */
  aAcc.esercizio_originale    := aObb.esercizio_originale;

  if isTrunc then
   aAcc.im_accertamento:=0;
   aAcc.dt_cancellazione:=trunc(aObb.dacr);
  else
   aAcc.im_accertamento:=aObb.im_obbligazione;
  end if;

  aAcc.fl_pgiro:='Y';
  aAcc.riportato:='N';
  aAcc.dacr:=aObb.dacr;
  aAcc.utcr:=aObb.utcr;
  aAcc.duva:=aObb.dacr;
  aAcc.utuv:=aObb.utcr;
  aAcc.pg_ver_rec:=1;

  Dbms_Output.put_line ('ins acc '||aAcc.cd_cds||' '||aAcc.esercizio||' '||aAcc.cd_tipo_documento_cont||' '||aAcc.esercizio_originale||' '||aAcc.pg_accertamento);
  CNRCTB035.INS_ACCERTAMENTO(aAcc);

  -- L'aggiornamento viene eseguito all'interno della procedura (creaScadAccertamento) VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  CNRCTB035.aggiornaSaldoDettScad(aAcc,aAcc.im_accertamento,false,aAcc.utcr,aAcc.dacr);

  aAccScad.cd_cds := aAcc.cd_cds;
  aAccScad.esercizio := aAcc.esercizio;
  aAccScad.dt_scadenza_emissione_fattura := aObbScad.dt_scadenza;
  aAccScad.dt_scadenza_incasso := aObbScad.dt_scadenza;
  aAccScad.ds_scadenza := aAcc.ds_accertamento;
  aAccScad.im_scadenza := aAcc.im_accertamento;
  aAccScad.im_associato_doc_amm := 0;
  aAccScad.im_associato_doc_contabile := 0;
  aAccScad.dacr:=aAcc.dacr;
  aAccScad.utcr:=aAcc.utcr;
  aAccScad.duva:=aAcc.dacr;
  aAccScad.utuv:=aAcc.utcr;
  aAccScad.pg_ver_rec:=1;

  If CNRCTB015.UtilizzaGAEdedicataPgiroCDS (aAcc.esercizio, aAcc.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_ENTRATE) Then
    aGAE_dedicata_CDS := CNRCTB015.get_LINEA_PGIRO_cds(aAcc.esercizio, aAcc.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_ENTRATE);
    aCdCdr := aGAE_dedicata_CDS.CD_CENTRO_RESPONSABILITA;
    aCdLa  := aGAE_dedicata_CDS.CD_LINEA_ATTIVITA;
  Else
    aCdCdr := CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
    aCdLa := CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
  End If;

  aDettScadenza.cd_cds := aAcc.cd_cds;
  aDettScadenza.esercizio := aAcc.esercizio;
  aDettScadenza.cd_centro_responsabilita := aCdCdr;
  aDettScadenza.cd_linea_attivita := aCdLa;
  aDettScadenza.im_voce := aAcc.im_accertamento;
  aDettScadenza.dacr:=aAcc.dacr;
  aDettScadenza.utcr:=aAcc.utcr;
  aDettScadenza.duva:=aAcc.dacr;
  aDettScadenza.utuv:=aAcc.utcr;
  aDettScadenza.pg_ver_rec:=1;
  aDettScadenzaList(1):=aDettScadenza;
  CNRCTB035.creaScadAccertamento(aAcc,aAccScad,1,aDettScadenzaList);

  aAssDocPGiro.cd_cds:=aAcc.cd_cds;
  aAssDocPGiro.esercizio:=aAcc.esercizio;
  aAssDocPGiro.esercizio_ori_accertamento:=aAcc.esercizio_originale;
  aAssDocPGiro.pg_accertamento:=aAcc.pg_accertamento;
  aAssDocPGiro.esercizio_ori_obbligazione:=aObb.esercizio_originale;
  aAssDocPGiro.pg_obbligazione:=aObb.pg_obbligazione;
  aAssDocPGiro.ti_origine:=CNRCTB001.GESTIONE_SPESE;
  aAssDocPGiro.dacr:=aAcc.dacr;
  aAssDocPGiro.utcr:=aAcc.utcr;
  aAssDocPGiro.duva:=aAcc.dacr;
  aAssDocPGiro.utuv:=aAcc.utcr;
  aAssDocPGiro.pg_ver_rec:=1;

Dbms_Output.put_line ('ins associazione pgiro');

  CNRCTB035.INS_ASS_OBB_ACR_PGIRO(aAssDocPGiro);

--Elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB_PGIRO_RES Then
-- RIBALTAMENTO

--  Null;
--End If;

End;

 procedure creaObbligazionePgiro(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aObbScad IN OUT obbligazione_scadenzario%rowtype,
  aAcc OUT accertamento%rowtype,
  aAccScad OUT accertamento_scadenzario%rowtype,
  aDtScadenza date
 ) is
 begin
  creaObbligazionePgiroInt(
   isControlloBloccante,
   false,
   aObb,
   aObbScad,
   aAcc,
   aAccScad,
   aDtScadenza
  );
 end;

 procedure creaObbligazionePgiroTronc(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aObbScad IN OUT obbligazione_scadenzario%rowtype,
  aAcc OUT accertamento%rowtype,
  aAccScad OUT accertamento_scadenzario%rowtype,
  aDtScadenza date
 ) is
 begin
  creaObbligazionePgiroInt(
   isControlloBloccante,
   true,
   aObb,
   aObbScad,
   aAcc,
   aAccScad,
   aDtScadenza
  );
 end;

 procedure creaObbligazione(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aScadenza1 in out obbligazione_scadenzario%rowtype,
  aDettScadenza1 in out CNRCTB035.scadVoceListS
 ) is
  aFakeObbScad obbligazione_scadenzario%rowtype;
 begin
  creaObbligazione(
   isControlloBloccante,
   aObb,
   aScadenza1,
   aDettScadenza1,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S,
   aFakeObbScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_S
 );
 end;

 procedure creaObbligazione(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aScadenza1 in out obbligazione_scadenzario%rowtype,
  aDettScadenza1 in out CNRCTB035.scadVoceListS,
  aScadenza2 in out obbligazione_scadenzario%rowtype,
  aDettScadenza2 out CNRCTB035.scadVoceListS,
  aScadenza3 in out obbligazione_scadenzario%rowtype,
  aDettScadenza3 in out CNRCTB035.scadVoceListS,
  aScadenza4 in out obbligazione_scadenzario%rowtype,
  aDettScadenza4 in out CNRCTB035.scadVoceListS,
  aScadenza5 in out obbligazione_scadenzario%rowtype,
  aDettScadenza5 in out CNRCTB035.scadVoceListS,
  aScadenza6 in out obbligazione_scadenzario%rowtype,
  aDettScadenza6 in out CNRCTB035.scadVoceListS,
  aScadenza7 in out obbligazione_scadenzario%rowtype,
  aDettScadenza7 in out CNRCTB035.scadVoceListS,
  aScadenza8 in out obbligazione_scadenzario%rowtype,
  aDettScadenza8 in out CNRCTB035.scadVoceListS,
  aScadenza9 in out obbligazione_scadenzario%rowtype,
  aDettScadenza9 in out CNRCTB035.scadVoceListS,
  aScadenza10 in out obbligazione_scadenzario%rowtype,
  aDettScadenza10 in out CNRCTB035.scadVoceListS
 ) is
  aNumeratore number;
 begin
  if aObb.cd_tipo_documento_cont not in (CNRCTB018.TI_DOC_OBB) then
   IBMERR001.RAISE_ERR_GENERICO('Documento non compatibile (2)');
  end if;
  if not (aObb.fl_pgiro = 'N') then
   IBMERR001.RAISE_ERR_GENERICO('Documento non compatibile (3)');
  end if;
  if aObb.esercizio != aObb.esercizio_competenza then
   IBMERR001.RAISE_ERR_GENERICO('2. Generazione automatica di '||cnrutil.getLabelObbligazioneMin()||' in esercizi futuri non supportata');
  end if;

  --commentato poichè i controlli vengono rifatti in CNRCTB035.creaScadObbligazione
  /*
  if isControlloBloccante then
   if checkAssunzObblig(
             aObb.esercizio_competenza,
             aObb.esercizio,
             aObb.cd_cds,
             aObb.cd_unita_organizzativa,
             aObb.cd_elemento_voce,
             aObb.im_obbligazione)='N' then
     IBMERR001.RAISE_ERR_GENERICO('Controllo assunzione '||cnrutil.getLabelObbligazioniMin()||' non superato');
   end if;
  end if;
  */
  aNumeratore:=CNRCTB018.getNextNumDocCont(aObb.cd_tipo_documento_cont, aObb.esercizio, aObb.cd_cds, aObb.utcr);

  --Se l'esercizio originale non è valorizzato lo inizializzo con quello dell'esercizio
  If (aObb.esercizio_originale Is Null) Then
     aObb.ESERCIZIO_ORIGINALE:=aObb.esercizio;
  End If;

  aObb.PG_OBBLIGAZIONE:=aNumeratore;
  aObb.dt_registrazione:=trunc(aObb.dt_registrazione);
  aObb.duva:=aObb.dacr;
  aObb.utuv:=aObb.utcr;
  aObb.pg_ver_rec:=1;
  CNRCTB035.INS_OBBLIGAZIONE(aObb);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza1,1,aDettScadenza1,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza2,2,aDettScadenza2,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza3,3,aDettScadenza3,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza4,4,aDettScadenza4,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza5,5,aDettScadenza5,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza6,6,aDettScadenza6,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza7,7,aDettScadenza7,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza8,8,aDettScadenza8,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza9,9,aDettScadenza9,isControlloBloccante);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza10,10,aDettScadenza10,isControlloBloccante);
 end;

procedure creaObbligazioneResidua(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aScadenza In out obbligazione_scadenzario%rowtype,
  aDettScadenza In out CNRCTB035.scadVoceListS
 ) is
  aNumeratore number;
 begin
  if aObb.cd_tipo_documento_cont not in (CNRCTB018.TI_DOC_OBB_RES_IMPRO) then
   IBMERR001.RAISE_ERR_GENERICO('Documento non compatibile (2)');
  end if;
  if not (aObb.fl_pgiro = 'N') then
   IBMERR001.RAISE_ERR_GENERICO('Documento non compatibile (3)');
  end if;
  if aObb.esercizio != aObb.esercizio_competenza then
   IBMERR001.RAISE_ERR_GENERICO('3. Generazione automatica di '||cnrutil.getLabelObbligazioneMin()||' in esercizi futuri non supportata');
  end if;

  aNumeratore:=CNRCTB018.getNextNumDocCont(aObb.cd_tipo_documento_cont, aObb.esercizio, aObb.cd_cds, aObb.utcr);

  aObb.PG_OBBLIGAZIONE:=aNumeratore;
  aObb.dt_registrazione:=trunc(aObb.dt_registrazione);
  aObb.duva:=aObb.dacr;
  aObb.utuv:=aObb.utcr;
  aObb.pg_ver_rec:=1;
  CNRCTB035.INS_OBBLIGAZIONE(aObb);
  CNRCTB035.creaScadObbligazione(aObb,aScadenza,1,aDettScadenza,isControlloBloccante);
 End;

End;
