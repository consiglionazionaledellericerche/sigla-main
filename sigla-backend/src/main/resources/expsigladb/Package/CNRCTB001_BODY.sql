--------------------------------------------------------
--  DDL for Package Body CNRCTB001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB001" is

-- Inserisce una riga nella tabella delle voci

 procedure insertVocePdc(aVoce voce_f%rowtype) as
 begin
  insert into VOCE_F (
    cd_cds,
    cd_titolo_capitolo,
    cd_sezione_capitolo,
    cd_voce,
	ds_voce,
	cd_parte,
    fl_mastrino,
    livello,
    ti_appartenenza,
    ti_gestione,
    ti_voce,
	cd_funzione,
	cd_natura,
	cd_unita_organizzativa,
	cd_centro_responsabilita,
    cd_categoria,
	cd_voce_padre,
    cd_proprio_voce,
    esercizio,
    cd_elemento_voce,
    dacr,
    duva,
    utcr,
    utuv,
    pg_ver_rec
  ) values (
    aVoce.cd_cds,
    aVoce.cd_titolo_capitolo,
    aVoce.cd_sezione_capitolo,
    aVoce.cd_voce,
	aVoce.ds_voce,
	aVoce.cd_parte,
    aVoce.fl_mastrino,
    aVoce.livello,
    aVoce.ti_appartenenza,
    aVoce.ti_gestione,
    aVoce.ti_voce,
	aVoce.cd_funzione,
	aVoce.cd_natura,
	aVoce.cd_unita_organizzativa,
	aVoce.cd_centro_responsabilita,
    aVoce.cd_categoria,
	aVoce.cd_voce_padre,
	aVoce.cd_proprio_voce,
    aVoce.esercizio,
    aVoce.cd_elemento_voce,
    aVoce.dacr,
    aVoce.dacr,
    aVoce.utcr,
    aVoce.utcr,
    1
   );
 end;

 -- Aggiorna la riga in VOCE_F

 procedure aggiornaVoce(aVoce IN voce_f%rowtype) is
 begin
  update VOCE_F set
	ds_voce = aVoce.ds_voce,
    duva = aVoce.duva,
    utuv = aVoce.utuv,
    pg_ver_rec = aVoce.pg_ver_rec + 1
  where
        cd_voce = aVoce.cd_voce
    and ti_gestione = aVoce.ti_gestione
	and ti_appartenenza = aVoce.ti_appartenenza
	and esercizio = aVoce.esercizio;
 end;

 -- Aggiorna la riga in ELEMENTO_VOCE

 procedure aggiornaElementoVoce(aEV IN elemento_voce%rowtype) is
 begin
  update ELEMENTO_VOCE set
	ds_elemento_voce = aEV.ds_elemento_voce,
    duva = aEV.duva,
    utuv = aEV.utuv,
    pg_ver_rec = aEV.pg_ver_rec + 1
  where
        cd_elemento_voce = aEV.cd_elemento_voce
    and ti_gestione = aEV.ti_gestione
	and ti_appartenenza = aEV.ti_appartenenza
	and esercizio = aEV.esercizio;
 end;

 -- Legge una riga per chiave primaria da VOCE_F

 procedure leggiVoce(aV IN OUT voce_f%rowtype) is
 begin
  select * into aV from voce_f where
        cd_voce = aV.cd_voce
    and ti_gestione = aV.ti_gestione
	and ti_appartenenza = aV.ti_appartenenza
	and esercizio = aV.esercizio;
 end;

 -- Legge una riga per chiave primaria da ELEMENTO_VOCE

 procedure leggiElementoVoce(aEV IN OUT elemento_voce%rowtype) is
 begin
  select * into aEV from elemento_voce where
        cd_elemento_voce = aEV.cd_elemento_voce
    and ti_gestione = aEV.ti_gestione
	and ti_appartenenza = aEV.ti_appartenenza
	and esercizio = aEV.esercizio;
 end;

 -- Funzionalità CORE

Procedure creaEsplVoci (aEV IN elemento_voce%rowtype) is

  aVoceS voce_f%rowtype;
  aVoceR voce_f%rowtype;
  aVoceC voce_f%rowtype;
  aVoceA voce_f%rowtype;
  aVoceAR voce_f%rowtype;
  aVoceTemp voce_f%rowtype;
  aUO unita_organizzativa%rowtype;
  aUOCDR unita_organizzativa%rowtype;
  aCDSSAC v_unita_organizzativa_valida%rowtype;
  aCDSAREA unita_organizzativa%rowtype;
  aVocePadre voce_f%rowtype;
  i number;
  aLiv number;
  isMastrino char(1);
  Begin

   if aEV.esercizio = 0 then
    return; -- Esercizio 0 non gestito
   end if;

   -- COPIA IN VOCE_F DELLE PARTI SENZA ESPLOSIONE
   -- TITOLO e CAPITOLO ENTRATA CDS

   If aEV.ti_elemento_voce in (TITOLO, CAPITOLO) and aEV.ti_gestione = GESTIONE_ENTRATE and aEV.ti_appartenenza = APPARTENENZA_CDS Then
    Begin
	  aVoceTemp.esercizio:=aEV.esercizio;
	  aVoceTemp.cd_voce:=aEV.cd_elemento_voce;
	  aVoceTemp.ti_gestione:=aEV.ti_gestione;
	  aVoceTemp.ti_appartenenza:=aEV.ti_appartenenza;

          leggiVoce(aVoceTemp);

	  if aVoceTemp.ds_voce!=aEV.ds_elemento_voce then
	   aVoceTemp.ds_voce:=aEV.ds_elemento_voce;
	   aVoceTemp.duva:=aEV.duva;
	   aVoceTemp.utuv:=aEV.utuv;
	   aggiornaVoce(aVoceTemp);
	  end if;
	  return;
	 exception when no_data_found then
      isMastrino:='N';
      if aEV.ti_elemento_voce = CAPITOLO then
       isMastrino:='Y';
      end if;
      aVoceTemp := creaVoceCorrispondente(aEV,1,isMastrino);
	  return;
    End;
   End If;

   -- PARTE E TITOLO e CAPITOLO PARTE 2, SPESA CDS

   If (aEV.ti_elemento_voce in (PARTE, TITOLO) Or aEV.ti_elemento_voce = CAPITOLO and aEV.cd_parte = PARTE2) And
       aEV.ti_gestione = GESTIONE_SPESE and aEV.ti_appartenenza = APPARTENENZA_CDS Then

	 Begin
	  aVoceTemp.esercizio:=aEV.esercizio;
	  aVoceTemp.cd_voce:=aEV.cd_elemento_voce;
	  aVoceTemp.ti_gestione:=aEV.ti_gestione;
	  aVoceTemp.ti_appartenenza:=aEV.ti_appartenenza;
          leggiVoce(aVoceTemp);
	  if aVoceTemp.ds_voce!=aEV.ds_elemento_voce then
	   aVoceTemp.ds_voce:=aEV.ds_elemento_voce;
	   aVoceTemp.duva:=aEV.duva;
	   aVoceTemp.utuv:=aEV.utuv;
	   aggiornaVoce(aVoceTemp);
	  end if;
	  return;
	 Exception when no_data_found then
           If aEV.ti_elemento_voce = PARTE then
	    aLiv:=1;
            isMastrino:='N';
           Else
	    aLiv:=2; -- Per titolo o capitolo (parte 2) il livello è 2
            If aEV.ti_elemento_voce = CAPITOLO then
		 isMastrino:='Y'; -- Il capitolo è l'ultimo livello per la parte 2
	    Else
		 isMastrino:='N';
            End if;
	   End if;

           aVoceTemp:=creaVoceCorrispondente(aEV,aLiv,isMastrino);
           return;
	 End;
   End If;

   -- TITOLO E CATEGORIA ENTRATA CNR

   If aEV.ti_elemento_voce in (TITOLO, CATEGORIA) and aEV.ti_gestione = GESTIONE_ENTRATE and aEV.ti_appartenenza = APPARTENENZA_CNR Then
      Begin
	  aVoceTemp.esercizio:=aEV.esercizio;
	  aVoceTemp.cd_voce:=aEV.cd_elemento_voce;
	  aVoceTemp.ti_gestione:=aEV.ti_gestione;
	  aVoceTemp.ti_appartenenza:=aEV.ti_appartenenza;
          leggiVoce(aVoceTemp);

          if aVoceTemp.ds_voce!=aEV.ds_elemento_voce then
	   aVoceTemp.ds_voce:=aEV.ds_elemento_voce;
	   aVoceTemp.duva:=aEV.duva;
	   aVoceTemp.utuv:=aEV.utuv;
	   aggiornaVoce(aVoceTemp);
	  end if;

	  return;
      Exception when no_data_found then
         if aEV.ti_elemento_voce = TITOLO then
	   aLiv:=1;
         else
	   aLiv:=2;
	 end if;
         aVoceTemp:=creaVoceCorrispondente(aEV,aLiv,'N');
	 Return;
      End;
   End If;

   -- PARTE, TITOLO, CAPITOLO (parte 2) SPESA CNR

   If (aEV.ti_elemento_voce in (PARTE, TITOLO) or aEV.ti_elemento_voce = CAPITOLO and aEV.cd_parte = PARTE2) And
       aEV.ti_gestione = GESTIONE_SPESE	and aEV.ti_appartenenza = APPARTENENZA_CNR Then
       Begin
	  aVoceTemp.esercizio:=aEV.esercizio;
	  aVoceTemp.cd_voce:=aEV.cd_elemento_voce;
	  aVoceTemp.ti_gestione:=aEV.ti_gestione;
	  aVoceTemp.ti_appartenenza:=aEV.ti_appartenenza;
          leggiVoce(aVoceTemp);
	  if aVoceTemp.ds_voce!=aEV.ds_elemento_voce then
	   aVoceTemp.ds_voce:=aEV.ds_elemento_voce;
	   aVoceTemp.duva:=aEV.duva;
	   aVoceTemp.utuv:=aEV.utuv;
	   aggiornaVoce(aVoceTemp);
	  end if;
	  return;
       Exception when no_data_found then
         isMastrino:='N';
         If aEV.ti_elemento_voce = PARTE then
           aLiv:=1;
         Elsif aEV.ti_elemento_voce in (TITOLO,CAPITOLO) then
           aLiv:=2;
	   If aEV.ti_elemento_voce = CAPITOLO then
             isMastrino:='Y';
	   End if;
         End if;
         aVoceTemp:=creaVoceCorrispondente(aEV,aLiv,isMastrino);
	  return;
       End;
   End If;

   -- CATEGORIA SPESA CNR

   If aEV.ti_elemento_voce = CATEGORIA and aEV.ti_gestione = GESTIONE_SPESE and aEV.ti_appartenenza = APPARTENENZA_CNR Then
     Begin
        --inserisco la categoria tra le voci
        aVoceS.cd_cds:=NULL;
        aVoceS.cd_titolo_capitolo:=aEV.cd_elemento_voce;
        aVoceS.cd_sezione_capitolo:=NULL;
        aVoceS.cd_voce:=aEV.cd_elemento_voce;
        aVoceS.cd_proprio_voce:=aEV.cd_proprio_elemento;
        aVoceS.cd_elemento_voce:=aEV.cd_elemento_voce;
        aVoceS.cd_parte:=aEV.cd_parte;
        aVoceS.ds_voce:=aEV.ds_elemento_voce;
        aVoceS.fl_mastrino:='N';
        aVoceS.livello:=3;
        aVoceS.ti_appartenenza:=aEV.ti_appartenenza;
        aVoceS.ti_gestione:=aEV.ti_gestione;
        aVoceS.ti_voce:=CATEGORIA;
        aVoceS.cd_funzione:=NULL;
        aVoceS.cd_unita_organizzativa:=NULL;
        aVoceS.cd_centro_responsabilita:=NULL;
        aVoceS.cd_categoria:=aEV.cd_proprio_elemento; -- Leggo dall'elemento_voce il codice della categoria
        aVoceS.cd_voce_padre:=aEV.cd_elemento_padre;
        aVoceS.cd_natura := NULL;
        aVoceS.esercizio:=aEV.esercizio;
        aVoceS.dacr:=aEV.dacr;
        aVoceS.utcr:=aEV.utcr;

        -- no mastrino, no elemento voce
        insertVocePdc(aVoceS);
        return;
     Exception when dup_val_on_index then
	 return;
     End;
   End If;

   -- COPIA IN VOCE_F DELLE PARTI CON ESPLOSIONE

   -- Esplosione CAPITOLI PARTE 1 SPESE CDS

   If aEV.ti_elemento_voce = CAPITOLO And aEV.ti_gestione = GESTIONE_SPESE And aEV.ti_appartenenza = APPARTENENZA_CDS And aEV.cd_parte = PARTE1 Then

   -- estraggo la voce padre a cui attaccare le voci generate

   select * into aVocePadre
   from voce_f
   where -- Leggo lockandolo il titolo sopra il capitolo
        esercizio = aEV.esercizio And
        ti_appartenenza = aEV.ti_appartenenza And
        ti_gestione = aEV.ti_gestione And
        cd_voce = aEV.cd_elemento_padre
        for update nowait;

   -- genero le sezioni sotto il titolo
   -- genero le sezioni solamente se sotto quelle sezioni saranno aggiunti capitoli

   For aFunzione in (Select b.cd_funzione, b.ds_funzione
                     From   funzione b
                     Where  FL_UTILIZZABILE = 'Y' And
                            Exists (Select 1
                                    From ass_ev_funz_tipocds a
                                    Where a.esercizio = aEV.esercizio And
                                          a.cd_funzione = b.cd_funzione And
                                          a.cd_conto = aEV.cd_elemento_voce And
                                          Exists (Select 1
                                                  From   v_unita_organizzativa_valida
                                                  Where  esercizio = a.esercizio And
                                                         cd_tipo_unita = a.cd_tipo_unita And
                                                         cd_tipo_unita != TIPO_ENTE And
                                                         fl_cds = 'N' And
                                                         fl_rubrica = 'Y'))) Loop -- loop sulle funzioni
    Begin
     --inserisco la sezione tra le voci
     aVoceS.cd_cds:=NULL;
     aVoceS.cd_titolo_capitolo:=aVocePadre.cd_titolo_capitolo;
     aVoceS.cd_sezione_capitolo:=NULL;
     aVoceS.cd_voce:=IBMUTL001.dotConcat(aVocePadre.cd_voce,aFunzione.cd_funzione);
     aVoceS.cd_proprio_voce:=aFunzione.cd_funzione;
     aVoceS.cd_elemento_voce:=aEV.cd_elemento_voce;
     aVoceS.cd_parte:=aEV.cd_parte;
     aVoceS.ds_voce:=aFunzione.ds_funzione;
     aVoceS.fl_mastrino:='N';
     aVoceS.livello:=3;
     aVoceS.ti_appartenenza:=aEV.ti_appartenenza;
     aVoceS.ti_gestione:=aEV.ti_gestione;
     aVoceS.ti_voce:=SEZIONE;
     aVoceS.cd_funzione:=aFunzione.cd_funzione;
     aVoceS.cd_unita_organizzativa:=NULL;
     aVoceS.cd_centro_responsabilita:=NULL;
     aVoceS.cd_categoria:=NULL;
     aVoceS.cd_voce_padre:=aVocePadre.cd_voce;
     aVoceS.cd_natura := NULL;
     aVoceS.esercizio:=aEV.esercizio;
     aVoceS.dacr:=aEV.dacr;
     aVoceS.utcr:=aEV.utcr;
-- no mastrino, no elemento voce
     insertVocePdc(aVoceS);
    Exception
      When dup_val_on_index Then NULL;
    End;

    -- genero le rubriche sotto le sezioni

    For aUO in (Select *
                From   v_unita_organizzativa_valida a
                Where  a.esercizio = aEV.esercizio And
                       a.fl_cds = 'N' And
                       a.fl_rubrica ='Y' And
                       a.cd_tipo_unita != TIPO_ENTE And
	               Exists (Select 1  -- Solo le rubriche che avranno capitoli vengono generate
	                       From  ass_ev_funz_tipocds b
	                       Where b.esercizio     = aEV.esercizio And
	                             b.cd_conto      = aEV.cd_elemento_voce And
	                             b.cd_funzione   = aFunzione.cd_funzione And
	                             b.cd_tipo_unita = a.cd_tipo_unita)) Loop -- loop sulle rubriche

     --inserisco la rubrica tra le voci
     Begin
      aVoceR.cd_cds:=aUO.cd_unita_padre;
      aVoceR.cd_titolo_capitolo:=aVoceS.cd_titolo_capitolo;
      aVoceR.cd_sezione_capitolo:=NULL;
      aVoceR.cd_voce:=IBMUTL001.dotConcat(aVoceS.cd_voce,aUO.cd_unita_organizzativa);
      aVoceR.cd_proprio_voce:=aUO.cd_unita_organizzativa;
      aVoceR.cd_elemento_voce:=aEV.cd_elemento_voce;
      aVoceR.cd_parte:=aEV.cd_parte;
      aVoceR.ds_voce:=aUO.ds_unita_organizzativa;
      aVoceR.fl_mastrino:='N';
      aVoceR.livello:=4;
      aVoceR.ti_appartenenza:=aEV.ti_appartenenza;
      aVoceR.ti_gestione:=aEV.ti_gestione;
      aVoceR.ti_voce:=RUBRICA;
      aVoceR.cd_funzione:=aFunzione.cd_funzione;
      aVoceR.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
      aVoceR.cd_centro_responsabilita:=NULL;
      aVoceR.cd_categoria:=NULL;
      aVoceR.cd_voce_padre:=aVoceS.cd_voce;
      aVoceR.cd_natura := NULL;
      aVoceR.esercizio:=aEV.esercizio;
      aVoceR.dacr:=aEV.dacr;
      aVoceR.utcr:=aEV.utcr;
-- no mastrino, no elemento voce
      insertVocePdc(aVoceR);
     Exception
        When dup_val_on_index Then NULL;
     End;

   -- Per ogni rubrica aggiunta inserisco il capitolo come voce

     Begin
      aVoceC.esercizio:=aEV.esercizio;
      aVoceC.ti_appartenenza:=aEV.ti_appartenenza;
      aVoceC.ti_gestione:=aEV.ti_gestione;
      aVoceC.cd_voce:=IBMUTL001.dotConcat(aVoceR.cd_voce,aEV.cd_proprio_elemento);
      leggiVoce(aVoceC);

       If aVoceC.ds_voce!=aEV.ds_elemento_voce Then
 	   aVoceC.ds_voce:=aEV.ds_elemento_voce; -- Se esiste già cambio solo la descrizione del capitolo
 	   aVoceC.duva:=aEV.duva;
 	   aVoceC.utuv:=aEV.utuv;
 	   aggiornaVoce(aVoceC);
       End If;
     Exception
        When No_Data_Found Then
           aVoceC.cd_proprio_voce:=aEV.cd_proprio_elemento;
           aVoceC.cd_elemento_voce:=aEV.cd_elemento_voce;
           aVoceC.cd_cds:=aUO.cd_unita_padre;
           aVoceC.cd_titolo_capitolo:=aEV.cd_elemento_voce;
           aVoceC.cd_sezione_capitolo:=IBMUTL001.dotConcat(aVoceS.cd_voce,aEV.cd_proprio_elemento);
           aVoceC.cd_parte:=aEV.cd_parte;
           aVoceC.ds_voce:=aEV.ds_elemento_voce;

           If(aUO.cd_tipo_unita=TIPOCDS_SAC) then
            aVoceC.fl_mastrino:='N';
           Else
            aVoceC.fl_mastrino:='Y';
           End If;

           aVoceC.livello:=5;
           aVoceC.ti_voce:=aEV.ti_elemento_voce;
           aVoceC.cd_funzione:=aFunzione.cd_funzione;
           aVoceC.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
           aVoceC.cd_centro_responsabilita:=NULL;
           aVoceC.cd_categoria:=NULL;
           aVoceC.cd_natura := NULL;
           aVoceC.cd_voce_padre:=aVoceR.cd_voce;
           aVoceC.dacr:=aEV.dacr;
           aVoceC.utcr:=aEV.utcr;

           -- se mastrino, elemento voce
           If aVoceC.fl_mastrino = 'Y' Then
            aVoceC.cd_elemento_voce := aEV.cd_elemento_voce;
           End If;

           insertVocePdc(aVoceC);
     End;

     -- Sotto il capitolo aggiungo gli articoli per il SAC: sono il cdr di primo e quelli a lui afferenti

     If aUO.cd_tipo_unita = TIPOCDS_SAC Then
       For aCDR In (Select cdrNR.cd_unita_organizzativa, cdrNR.cd_proprio_cdr, cdrNR.cd_centro_responsabilita, cdrNR.ds_cdr
                    From   v_cdr_valido cdrR, v_cdr_valido cdrNR
                    Where  cdrR.esercizio=aUO.esercizio And
                           cdrR.cd_unita_organizzativa = aUO.cd_unita_organizzativa And
                           cdrNR.esercizio=aUO.esercizio And
                          (cdrNR.cd_unita_organizzativa=aUO.cd_unita_organizzativa Or
                           cdrNR.cd_cdr_afferenza = cdrR.cd_centro_responsabilita)) Loop
	   -- loop sui CDR dell'UO
	   --inserisco l'articolo solo per il SAC

	   --leggo l'UO della rubrica o non rubrica
	   aUOCDR := CNRCTB020.GETUOVALIDA (aEV.esercizio,aCDR.cd_unita_organizzativa);

	Begin
         aVoceA.cd_cds:=aUO.cd_unita_padre;
         aVoceA.cd_titolo_capitolo:=aEV.cd_elemento_voce;
         aVoceA.cd_sezione_capitolo:=aVoceC.cd_sezione_capitolo;
         aVoceA.cd_voce:=IBMUTL001.dotConcat(aVoceC.cd_voce,aUOCDR.cd_proprio_unita);
         aVoceA.cd_proprio_voce:=aUOCDR.cd_proprio_unita;
         aVoceA.cd_elemento_voce:=aEV.cd_elemento_voce;
         aVoceA.cd_parte:=aEV.cd_parte;
         aVoceA.ds_voce:=aCDR.ds_cdr;
         aVoceA.fl_mastrino:='Y';
         aVoceA.livello:=6;
         aVoceA.ti_appartenenza:=aEV.ti_appartenenza;
         aVoceA.ti_gestione:=aEV.ti_gestione;
         aVoceA.ti_voce:=ARTICOLO;
         aVoceA.cd_funzione:=aFunzione.cd_funzione;
         aVoceA.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
         aVoceA.cd_centro_responsabilita:=aCDR.cd_centro_responsabilita;
         aVoceA.cd_categoria:=NULL;
         aVoceA.cd_voce_padre:=aVoceC.cd_voce;
         aVoceA.cd_natura := NULL;
         aVoceA.esercizio:=aEV.esercizio;
         aVoceA.dacr:=aEV.dacr;
         aVoceA.utcr:=aEV.utcr;

-- mastrino, elemento voce
         aVoceC.cd_elemento_voce := aEV.cd_elemento_voce;

         insertVocePdc(aVoceA);
	Exception
	  when dup_val_on_index Then Null;
	end;

      End Loop;  -- SUI CDR
     End If; -- TIPO UNITA = SAC

   End Loop; -- SULLE UO

End Loop; -- SULLE FUNZIONI

Return;

End If; -- If aEV.ti_elemento_voce = CAPITOLO And aEV.ti_gestione = GESTIONE_SPESE And aEV.ti_appartenenza = APPARTENENZA_CDS And aEV.cd_parte = PARTE1 Then


   -- ESPLOSIONE CAPITOLI PARTE 1 SPESE CNR CATEGORIA 2

   -- Come elemento voce ho un capitolo di spesa CNR

   If aEV.ti_elemento_voce = CAPITOLO And aEV.ti_gestione = GESTIONE_SPESE And aEV.ti_appartenenza = APPARTENENZA_CNR And aEV.cd_parte = PARTE1 Then

    -- Cerca l'eventuale esistenza di CDS SAC
    Begin
      Select *
      Into   aCDSSAC
      From   v_unita_organizzativa_valida
      Where  esercizio = aEV.esercizio And
             cd_tipo_unita = CNRCTB020.TIPO_SAC And
             fl_cds = 'Y';
    Exception
      When no_data_found then
	 aCDSSAC.cd_unita_organizzativa := NULL;
    End;

    -- privatizzo la categoria sopra il capitolo

    Select *
    Into   aVocePadre
    From   voce_f
    Where  esercizio = aEV.esercizio And
           ti_appartenenza = aEV.ti_appartenenza And
           ti_gestione = aEV.ti_gestione And
           cd_voce = aEV.cd_elemento_padre  -- Si tratta della categoria
 	   for update nowait;

    If aVocePadre.cd_categoria = CATEGORIA2_SPESE_CNR Then -- L'esplosione in voci da qui è gestita solo per CAT. 2

      For aFunzione In (Select *
                        From   Funzione
                        Where  FL_UTILIZZABILE = 'Y') Loop -- AGGIUNTO L'11/06/2009
                                                           -- loop sulle funzioni
       Begin
          --inserisco la sezione tra le voci
	  aVoceS:=NULL;
          aVoceS.cd_cds:=NULL;
          aVoceS.cd_titolo_capitolo:=aVocePadre.cd_titolo_capitolo;
          aVoceS.cd_sezione_capitolo:=NULL;
          aVoceS.cd_voce:=IBMUTL001.dotConcat(aVocePadre.cd_voce,aFunzione.cd_funzione);
          aVoceS.cd_proprio_voce:=aFunzione.cd_funzione;
          aVoceS.cd_elemento_voce:=aEV.cd_elemento_voce;
          aVoceS.cd_parte:=aEV.cd_parte;
          aVoceS.ds_voce := aFunzione.ds_funzione;
          aVoceS.fl_mastrino:='N';
          aVoceS.livello:=4;
          aVoceS.ti_appartenenza:=aEV.ti_appartenenza;
          aVoceS.ti_gestione:=aEV.ti_gestione;
          aVoceS.ti_voce:=SEZIONE;
          aVoceS.cd_funzione:=aFunzione.cd_funzione;
          aVoceS.cd_unita_organizzativa:=NULL;
          aVoceS.cd_centro_responsabilita:=NULL;
          aVoceS.cd_categoria:=CATEGORIA2_SPESE_CNR; -- la categoria è il terzo livello
          aVoceS.cd_voce_padre:=aVocePadre.cd_voce;
          aVoceS.cd_natura := NULL;
          aVoceS.esercizio:=aEV.esercizio;
          aVoceS.dacr:=aEV.dacr;
          aVoceS.utcr:=aEV.utcr;
-- no mastrino, no elemento voce
          insertVocePdc(aVoceS);
       Exception
         When Dup_Val_On_Index Then Null;
       End;

       -- Sotto la sezione crea la voce corrispondente al capitolo
       Begin
 	    aVoceC.esercizio:=aEV.esercizio;
 	    aVoceC.cd_voce:=IBMUTL001.dotConcat(aVoceS.cd_voce,aEV.cd_proprio_elemento);
 	    aVoceC.ti_gestione:=aEV.ti_gestione;
 	    aVoceC.ti_appartenenza:=aEV.ti_appartenenza;
            leggiVoce(aVoceC);

	    If aVoceC.ds_voce != aEV.ds_elemento_voce then
 	     aVoceC.ds_voce:=aEV.ds_elemento_voce;
 	     aVoceC.duva:=aEV.duva;
 	     aVoceC.utuv:=aEV.utuv;
 	     aggiornaVoce(aVoceC);
 	    End If;
	    -- return; -- <------ fix del 25/02/2002 NON DEVE RITORNARE A QUESTO PUNTO ALTRIMENTI ROMPE IL CICLO SULLE FUNZIONI
       Exception
 	  When No_Data_Found Then
             aVoceC.cd_cds:=NULL;
             aVoceC.cd_titolo_capitolo:=aEV.cd_elemento_voce;
             aVoceC.cd_sezione_capitolo:=NULL;
             aVoceC.cd_proprio_voce:=aEV.cd_proprio_elemento;
             aVoceC.cd_elemento_voce:=aEV.cd_elemento_voce;
             aVoceC.cd_parte:=aEV.cd_parte;
             aVoceC.ds_voce:=aEV.ds_elemento_voce;
             aVoceC.fl_mastrino:='N';
             aVoceC.livello:=5;
             aVoceC.ti_voce:=CAPITOLO;
             aVoceC.cd_funzione:=aVoceS.cd_funzione;
             aVoceC.cd_unita_organizzativa:=NULL;
             aVoceC.cd_centro_responsabilita:=NULL;
             aVoceC.cd_categoria:=aVocePadre.cd_categoria;
             aVoceC.cd_voce_padre:=aVoceS.cd_voce;
             aVoceC.cd_natura := NULL;
             aVoceC.dacr:=aEV.dacr;
             aVoceC.utcr:=aEV.utcr;

             -- no mastrino, no elemento voce
             insertVocePdc(aVoceC);

             For aNatura In (Select *
                             From   natura
                             Where  CD_NATURA In ('1', '2')) Loop -- loop sulle nature
                                                                  -- 17.06.2009 AGGIUNTO FILTRO SULLE NATURE PER LE VOCI SPESA "CNR"
                --inserisco la sezione tra le voci
                aVoceA.cd_cds:=NULL;
                aVoceA.cd_titolo_capitolo:=aEV.cd_elemento_voce;
                aVoceA.cd_sezione_capitolo:=NULL;
                aVoceA.cd_voce:=IBMUTL001.dotConcat(aVoceC.cd_voce,aNatura.cd_natura);
                aVoceA.cd_proprio_voce:=aNatura.cd_natura;
                aVoceA.cd_elemento_voce:=aEV.cd_elemento_voce;
                aVoceA.cd_parte:=aEV.cd_parte;
                aVoceA.ds_voce:=aNatura.ds_natura;
                aVoceA.fl_mastrino:='N';
                aVoceA.livello:=6;
                aVoceA.ti_appartenenza:=aEV.ti_appartenenza;
                aVoceA.ti_gestione:=aEV.ti_gestione;
                aVoceA.ti_voce:=ARTICOLO;
                aVoceA.cd_funzione:=aVoceS.cd_funzione;
                aVoceA.cd_unita_organizzativa:=NULL;
                aVoceA.cd_centro_responsabilita:=NULL;
                aVoceA.cd_categoria:=aVocePadre.cd_categoria;
                aVoceA.cd_voce_padre:=aVoceC.cd_voce;
                aVoceA.cd_natura := aNatura.cd_natura;
                aVoceA.esercizio:=aEV.esercizio;
                aVoceA.dacr:=aEV.dacr;
                aVoceA.utcr:=aEV.utcr;
                -- no mastrino, no elemento voce
                insertVocePdc(aVoceA);

                If aVoceA.cd_natura != NATURA_5 and (aCDSSAC.cd_unita_organizzativa is not null) Then

    	             Begin
    	               aVoceAR.esercizio:=aEV.esercizio;
                       aVoceAR.cd_voce:=IBMUTL001.dotConcat(aVoceA.cd_voce,aCDSSAC.cd_unita_organizzativa);
                       aVoceAR.ti_appartenenza:=aVoceA.ti_appartenenza;
                       aVoceAR.ti_gestione:=aVoceA.ti_gestione;
                       leggiVoce(aVoceAR);

    	               If aVoceAR.ds_voce!=descSottoArtCDSProprio(aCDSSAC.cd_unita_organizzativa,aVoceA) then
		           	 aVoceAR.ds_voce:=descSottoArtCDSProprio(aCDSSAC.cd_unita_organizzativa,aVoceA);
    	                aVoceAR.duva:=aEV.duva;
    	                aVoceAR.utuv:=aEV.utuv;
                        aggiornaVoce(aVoceAR);
                       End If;
	             Exception when no_data_found then
                       aVoceAR.cd_cds:=aCDSSAC.cd_unita_organizzativa;
                       aVoceAR.cd_titolo_capitolo:=aVoceA.cd_titolo_capitolo;
                       aVoceAR.cd_sezione_capitolo:=NULL;
                       aVoceAR.cd_proprio_voce:=aCDSSAC.cd_unita_organizzativa;
                       aVoceAR.cd_elemento_voce:=aEV.cd_elemento_voce;
                       aVoceAR.cd_parte:=aVoceA.cd_parte;
    	               aVoceAR.ds_voce:=descSottoArtCDSProprio(aCDSSAC.cd_unita_organizzativa,aVoceA);
                       aVoceAR.fl_mastrino:='Y';
                       aVoceAR.livello:=7;
                       aVoceAR.ti_voce:=SOTTOARTICOLO;
                       aVoceAR.cd_funzione:=aVoceA.cd_funzione;
                       aVoceAR.cd_unita_organizzativa:=NULL;
                       aVoceAR.cd_centro_responsabilita:=NULL;
                       aVoceAR.cd_categoria:=aVoceA.cd_categoria;
                       aVoceAR.cd_natura := aVoceA.cd_natura;
                       aVoceAR.cd_voce_padre:=aVoceA.cd_voce;
                       aVoceAR.dacr:=aEV.dacr;
                       aVoceAR.utcr:=aEV.utcr;
                       -- mastrino, elemento voce
                       aVoceAR.cd_elemento_voce := aVoceA.cd_titolo_capitolo;
                       insertVocePdc(aVoceAR);
                     End;
                End If;
            End Loop; -- sulle nature
       End;

End Loop; -- sulle funzioni

End If; -- If aVocePadre.cd_categoria = CATEGORIA2_SPESE_CNR Then -- L'esplosione in voci da qui è gestita solo per CAT. 2

Return;

End If; -- If aEV.ti_elemento_voce = CAPITOLO And aEV.ti_gestione = GESTIONE_SPESE And aEV.ti_appartenenza = APPARTENENZA_CNR And aEV.cd_parte = PARTE1 Then

-- Esplosione CAPITOLI ENTRATA CNR

If aEV.ti_elemento_voce = CAPITOLO And aEV.ti_gestione = GESTIONE_ENTRATE And aEV.ti_appartenenza = APPARTENENZA_CNR Then

   Begin
         aVoceC.esercizio:=aEV.esercizio;
         aVoceC.cd_voce:=aEV.cd_elemento_voce;
         aVoceC.ti_gestione:=aEV.ti_gestione;
         aVoceC.ti_appartenenza:=aEV.ti_appartenenza;
         leggiVoce(aVoceC);
         If aVoceC.ds_voce!=aEV.ds_elemento_voce Then
	      aVoceC.ds_voce:=aEV.ds_elemento_voce;
              aVoceC.duva:=aEV.duva;
              aVoceC.utuv:=aEV.utuv;
              aggiornaVoce(aVoceC);
         End If;
         Return;
   Exception
        When No_Data_Found Then

           aVoceC:=creaVoceCorrispondente(aEV,3,'N');
	   -- Sotto il capitolo della parte entrate del CNR vanno solamente le UO CDS
               /* For aUO In (Select *
                            From   v_unita_organizzativa_valida
                            Where  esercizio = aEV.esercizio And
                                   fl_cds = 'N' And
                                   cd_tipo_unita != TIPO_ENTE) Loop -- loop sulle nature */
	            --inserisco la natura sotto il conto tra le voci
                    aVoceA.cd_cds:= null;--aUO.cd_unita_padre;
                    aVoceA.cd_titolo_capitolo:=aVoceC.cd_voce;
                    aVoceA.cd_sezione_capitolo:=NULL;
                    aVoceA.cd_voce:=aEV.cd_elemento_voce||ARTICOLO;
                    aVoceA.cd_proprio_voce:=ARTICOLO;--aUO.cd_unita_organizzativa;
                    aVoceA.cd_elemento_voce:=aEV.cd_elemento_voce;
                    aVoceA.cd_parte:=NULL;
                    aVoceA.ds_voce:=aEV.ds_elemento_voce;
                    aVoceA.fl_mastrino:='Y';
                    aVoceA.livello:=4;
                    aVoceA.ti_appartenenza:=aEV.ti_appartenenza;
                    aVoceA.ti_gestione:=aEV.ti_gestione;
                    aVoceA.ti_voce:=ARTICOLO;
                    aVoceA.cd_funzione:=NULL;
                    aVoceA.cd_unita_organizzativa:= null;--aUO.cd_unita_organizzativa;
                    aVoceA.cd_centro_responsabilita:=NULL;
                    aVoceA.cd_categoria:=aVoceC.cd_categoria;  -- Eredita il codice della categoria dal padre
                    aVoceA.cd_voce_padre:=aVoceC.cd_voce;
                    aVoceA.cd_natura := NULL;
                    aVoceA.esercizio:=aEV.esercizio;
                    aVoceA.dacr:=aEV.dacr;
                    aVoceA.utcr:=aEV.utcr;

                   -- mastrino, elemento voce
                   aVoceA.cd_elemento_voce := aVoceC.cd_voce;

                   insertVocePdc(aVoceA);

               --End Loop;*/
   End;

   Return;

End If;

End;


Procedure eliminaEsplVoci(aEV IN elemento_voce%rowtype) is
   aVocePadre voce_f%rowtype;
  begin

   -- Blocco la tabella voce_f per l'eliminazione

   lock table voce_f in exclusive mode nowait;

   -- Eliminazione da voce_f della voce corrispondente a quella eliminata in elemento_voce
   -- I vincoli sulla struttura di voce_f ed elemento_voce producono in cascata l'eliminazione
   -- dei nodi figli

   if -- Nel caso di CAPITOLO di SPESA CDS PARTE 1 devo entrare in VOCE_F sul campo cd_titolo_capitolo
        aEV.ti_elemento_voce = CAPITOLO
	and aEV.ti_gestione = GESTIONE_SPESE
	and aEV.ti_appartenenza = APPARTENENZA_CDS
	and aEV.cd_parte = PARTE1
   then
    delete from voce_f
    Where esercizio = aEV.esercizio
     and  ti_appartenenza = aEV.ti_appartenenza
     And  ti_gestione = aEV.ti_gestione
     and  cd_titolo_capitolo = aEV.cd_elemento_voce
     and  ti_voce = aEV.ti_elemento_voce;
   elsif -- Eliminazione delle tipologie di intervento (PARTE 1 CATEGORIA 2)
        aEV.ti_elemento_voce = CAPITOLO
	and aEV.ti_gestione = GESTIONE_SPESE
	and aEV.ti_appartenenza = APPARTENENZA_CNR
	and aEV.cd_parte = PARTE1
   then
    delete from voce_f where
         esercizio = aEV.esercizio
     and cd_categoria = CATEGORIA2_SPESE_CNR
     and cd_titolo_capitolo = aEV.cd_elemento_voce
     and ti_appartenenza = aEV.ti_appartenenza
     and ti_gestione = aEV.ti_gestione
	 and ti_voce = aEV.ti_elemento_voce;
   else
    delete from voce_f where
         esercizio = aEV.esercizio
     and cd_voce = aEV.cd_elemento_voce
     and ti_appartenenza = aEV.ti_appartenenza
     and ti_gestione = aEV.ti_gestione
	 and ti_voce = aEV.ti_elemento_voce;
   end if;
 end;

Function creaVoceCorrispondente(aEV elemento_voce%rowtype, aLivello number, isMastrino char) return voce_f%rowtype is
  aVoceT voce_f%rowtype;
  aVoceC voce_f%rowtype;
  aVocePadre voce_f%rowtype;
Begin

  Begin
    Select *
    Into   aVocePadre
    From   voce_f
    Where  esercizio       = aEV.esercizio And
           ti_appartenenza = aEV.ti_appartenenza And
           ti_gestione     = aEV.ti_gestione And
           cd_voce         = aEV.cd_elemento_padre;
  Exception
    When No_Data_Found Then -- Potrebbe essere che l'elemento voce non abbia padri
      Null;
  End;

  --inserisco l'elemento voce tra le voci
  aVoceC.cd_cds:=NULL;
  aVoceC.cd_titolo_capitolo:=aEV.cd_elemento_voce;
  aVoceC.cd_sezione_capitolo:=NULL;
  aVoceC.cd_voce:=aEV.cd_elemento_voce;
  aVoceC.cd_elemento_voce:=aEV.cd_elemento_voce;
  aVoceC.cd_proprio_voce:=aEV.cd_proprio_elemento;
  aVoceC.cd_parte:=aEV.cd_parte;
  aVoceC.ds_voce:=aEV.ds_elemento_voce;
  aVoceC.fl_mastrino:=isMastrino;
  aVoceC.livello:=aLivello;
  aVoceC.ti_appartenenza:=aEV.ti_appartenenza;
  aVoceC.ti_gestione:=aEV.ti_gestione;
  aVoceC.ti_voce:=aEV.ti_elemento_voce;
  aVoceC.cd_funzione:=NULL;
  aVoceC.cd_unita_organizzativa:=NULL;
  aVoceC.cd_centro_responsabilita:=NULL;

  aVoceC.cd_categoria := NULL;

  -- Quando inserisco la categoria 2 nelle voce CNR SPESA setto l'attributo cd_categoria
  -- che sarà ereditato dai figli di tali voci

  if
     (
          aEV.ti_appartenenza=APPARTENENZA_CNR
	  and aEV.ti_elemento_voce = CATEGORIA
	  and aEV.ti_gestione = GESTIONE_SPESE
     ) or
     (
          aEV.ti_appartenenza=APPARTENENZA_CNR
	  and aEV.ti_elemento_voce = CATEGORIA
	  and aEV.ti_gestione = GESTIONE_ENTRATE
     )  then
   aVoceC.cd_categoria := aEV.cd_proprio_elemento;
  elsif   aEV.ti_appartenenza=APPARTENENZA_CNR
	  and aEV.ti_elemento_voce = CAPITOLO
	  and aEV.ti_gestione = GESTIONE_ENTRATE
  then
   aVoceC.cd_categoria := aVocePadre.cd_proprio_voce; -- setta il codice della categoria sul capitolo di entrata CNR
  else
   aVoceC.cd_categoria := aVocePadre.cd_categoria;
  end if;

  aVoceC.cd_voce_padre:=aVocePadre.cd_voce;
  aVoceC.cd_natura := NULL;
  aVoceC.esercizio:=aEV.esercizio;
  aVoceC.dacr:=aEV.dacr;
  aVoceC.utcr:=aEV.utcr;

-- se mastrino, elemento voce da fare
   If isMastrino = 'Y' Then
      aVoceC.cd_elemento_voce := aEV.cd_elemento_voce;
   End If;

  insertVocePdc(aVoceC);
  return aVoceC;
 end;



-- ***************************************************************************
-- ESPLOSIONE VOCI PER MODIFICHE SU UNITA_ORGANIZZATIVA **********************
-- Aggiornamento del PDC Finanziario per modifiche Struttura organizzativa
-- Non gestisce le aree se non per la modifica della descrizione del SOTTOART.
-- ***************************************************************************

 procedure creaEsplVociUO(aEs number, aCdUO varchar2, aUser varchar2) is
  aUO unita_organizzativa%rowtype;
  aUOCDR unita_organizzativa%rowtype;
  aVocePadre voce_f%rowtype;
  aVoceC voce_f%rowtype;
  aVoceR voce_f%rowtype;
  aVoceS voce_f%rowtype;
  aVoceA voce_f%rowtype;
  aVoceAR voce_f%rowtype;
  aEVPadre elemento_voce%rowtype;
  aEV elemento_voce%rowtype;
  aUtcr varchar2(20);
  aUtuv varchar2(20);
  aDacr date;
  aDuva date;
 begin
  aUO:=CNRCTB020.getUOValida(aEs, aCdUO);

  if aUser is not null then
   aUtcr:=aUser;
   aUtuv:=aUser;
   aDacr:=sysdate;
   aDuva:=sysdate;
  else
   aUtcr:=aUO.utcr;
   aUtuv:=aUO.utuv;
   aDacr:=aUO.dacr;
   aDuva:=aUO.duva;
  end if;

  -- se si tratta di ENTE non devo effettuare aggiornamenti

  if(aUO.cd_tipo_unita = TIPO_ENTE) then
   return;
  end if;

  -- se si tratta di CDS AREA aggiorno la descrizione del sottoarticolo corrispondente
  -- sotto la parte 1 delle spese CNR categoria 1

  if(aUO.fl_cds = 'Y' and aUO.cd_tipo_unita = TIPOCDS_AREA) then
   for aSottoarticolo in (select 1 from voce_f where
         esercizio = aEs
     and ti_voce = SOTTOARTICOLO
	 and ti_gestione = GESTIONE_SPESE
	 and ti_appartenenza = APPARTENENZA_CNR
	 and cd_categoria = CATEGORIA1_SPESE_CNR
     and cd_proprio_voce = aUO.cd_unita_organizzativa
	 for update nowait) loop
    NULL;
   end loop;

   update voce_f set
         ds_voce = aUO.ds_unita_organizzativa,
         duva = aDuva,
	     utuv = aUtuv,
		 pg_ver_rec = pg_ver_rec + 1
   where
         esercizio = aEs
     and ti_voce = SOTTOARTICOLO
	 and ti_gestione = GESTIONE_SPESE
	 and ti_appartenenza = APPARTENENZA_CNR
	 and cd_categoria = CATEGORIA1_SPESE_CNR
     and cd_proprio_voce = aUO.cd_unita_organizzativa;
  end if;

  -- se si tratta di CDS non AREA e non SAC alimento il capitolo di spesa del CNR per Categoria I

  if(aUO.fl_cds = 'Y' and aUO.cd_tipo_unita != TIPOCDS_AREA and aUO.cd_tipo_unita != TIPOCDS_SAC) then

   -- leggo la categoria sopra la sezione

   for aVocePadre in (select * from voce_f where
         esercizio = aEs
     and ti_voce = CATEGORIA
	 and ti_gestione = GESTIONE_SPESE
	 and ti_appartenenza = APPARTENENZA_CNR
	 and cd_categoria = CATEGORIA1_SPESE_CNR for update nowait) loop -- Parte 1 - Titolo x - Categoria 1

	 for aFunzione in (select cd_funzione, ds_funzione
	                   From funzione
	                   Where FL_UTILIZZABILE = 'Y') loop -- loop sulle funzioni
      begin
       --inserisco la sezione tra le voci
       aVoceS.cd_cds:=NULL;
       aVoceS.cd_titolo_capitolo:=aVocePadre.cd_titolo_capitolo;
       aVoceS.cd_sezione_capitolo:=NULL;
       aVoceS.cd_voce:=IBMUTL001.dotConcat(aVocePadre.cd_voce,aFunzione.cd_funzione);
       aVoceS.cd_proprio_voce:=aFunzione.cd_funzione;
       aVoceS.cd_elemento_voce:=aVocePadre.cd_elemento_voce;
       aVoceS.cd_parte:=aVocePadre.cd_parte;
       aVoceS.ds_voce:=aFunzione.ds_funzione;
       aVoceS.fl_mastrino:='N';
       aVoceS.livello:=4;
       aVoceS.ti_appartenenza:=aVocePadre.ti_appartenenza;
       aVoceS.ti_gestione:=aVocePadre.ti_gestione;
       aVoceS.ti_voce:=SEZIONE;
       aVoceS.cd_funzione:=aFunzione.cd_funzione;
       aVoceS.cd_unita_organizzativa:=NULL;
       aVoceS.cd_centro_responsabilita:=NULL;
       aVoceS.cd_categoria:=aVocePadre.cd_categoria;
       aVoceS.cd_voce_padre:=aVocePadre.cd_voce;
       aVoceS.cd_natura := NULL;
       aVoceS.esercizio:=aVocePadre.esercizio;
       aVoceS.dacr:=aDacr;
       aVoceS.utcr:=aUtcr;

-- no mastrino, no elemento voce
       insertVocePdc(aVoceS);

  	  exception when dup_val_on_index then
  	   NULL;
  	  end;

	-- Crea la voce corrispondente al capitolo in VOCE_F (in questo caso il CDS)

	  begin -- Imposto la chiave primaria di voce_f
       aVoceC.esercizio:=aEs;
       aVoceC.cd_voce:=IBMUTL001.dotConcat(aVoceS.cd_voce,aUO.cd_unita_organizzativa);
       aVoceC.ti_appartenenza:=aVoceS.ti_appartenenza;
       aVoceC.ti_gestione:=aVoceS.ti_gestione;
       leggiVoce(aVoceC); -- leggo la voce
	   if aVoceC.ds_voce!=aUO.ds_unita_organizzativa then
	    aVoceC.ds_voce:=aUO.ds_unita_organizzativa;
	    aVoceC.duva:=aDuva;
	    aVoceC.utuv:=aUtuv;
	    aggiornaVoce(aVoceC);
	   end if;
	  exception when no_data_found then -- se la voce non c'è la inserisco
       aVoceC.cd_proprio_voce:=aUO.cd_unita_organizzativa;
       aVoceC.cd_elemento_voce:=aVoceS.cd_elemento_voce;
       aVoceC.cd_cds:=aUO.cd_unita_organizzativa;
       aVoceC.cd_titolo_capitolo:=aVoceS.cd_titolo_capitolo;
       aVoceC.cd_sezione_capitolo:=NULL;
       aVoceC.cd_parte:=aVoceS.cd_parte;
       aVoceC.ds_voce:=aUO.ds_unita_organizzativa;
       aVoceC.fl_mastrino:='N';
       aVoceC.livello:=5;
       aVoceC.ti_voce:=CAPITOLO;
       aVoceC.cd_funzione:=aFunzione.cd_funzione;
       aVoceC.cd_unita_organizzativa:=NULL;
       aVoceC.cd_centro_responsabilita:=NULL;
       aVoceC.cd_categoria:=aVoceS.cd_categoria;
       aVoceC.cd_natura := NULL;
       aVoceC.cd_voce_padre:=aVoceS.cd_voce;
       aVoceC.dacr:=aDacr;
       aVoceC.utcr:=aUtcr;

-- no mastrino, no elemento voce
       insertVocePdc(aVoceC);

	  end;
      for aNatura in (select * from natura) loop -- loop sulle nature
       begin
        --inserisco la sezione tra le voci
        aVoceA.esercizio:=aEs;
        aVoceA.cd_voce:=IBMUTL001.dotConcat(aVoceC.cd_voce,aNatura.cd_natura);
        aVoceA.ti_appartenenza:=aVoceS.ti_appartenenza;
        aVoceA.ti_gestione:=aVoceS.ti_gestione;
        leggiVoce(aVoceA);
	    if aVoceA.ds_voce!=aNatura.ds_natura then
		 aVoceA.ds_voce:=aNatura.ds_natura;
	     aVoceA.duva:=aDuva;
	     aVoceA.utuv:=aUtuv;
	     aggiornaVoce(aVoceA);
        end if;
	   exception when no_data_found then
        aVoceA.cd_cds:=aUO.cd_unita_organizzativa;
        aVoceA.cd_titolo_capitolo:=aVoceC.cd_titolo_capitolo;
        aVoceA.cd_sezione_capitolo:=NULL;
        aVoceA.cd_proprio_voce:=aNatura.cd_natura;
        aVoceA.cd_elemento_voce:=aVoceC.cd_elemento_voce;
        aVoceA.cd_parte:=aVoceC.cd_parte;
        aVoceA.ds_voce:=aNatura.ds_natura;
        aVoceA.fl_mastrino:='N';
        aVoceA.livello:=6;
        aVoceA.ti_voce:=ARTICOLO;
        aVoceA.cd_funzione:=aFunzione.cd_funzione;
        aVoceA.cd_unita_organizzativa:=NULL;
        aVoceA.cd_centro_responsabilita:=NULL;
        aVoceA.cd_categoria:=aVoceC.cd_categoria;
        aVoceA.cd_natura := aNatura.cd_natura;
        aVoceA.cd_voce_padre:=aVoceC.cd_voce;
        aVoceA.dacr:=aDacr;
        aVoceA.utcr:=aUtcr;

-- no mastrino, no elemento voce
        insertVocePdc(aVoceA);

	   end;
		-- Sotto l'articolo inserisco il sottoarticolo = al CDS per nature 1-4
       if aNatura.cd_natura != NATURA_5 then
    	   begin
    	    aVoceAR.esercizio:=aEs;
            aVoceAR.cd_voce:=IBMUTL001.dotConcat(aVoceA.cd_voce,aVoceC.cd_proprio_voce);
            aVoceAR.ti_appartenenza:=aVoceA.ti_appartenenza;
            aVoceAR.ti_gestione:=aVoceA.ti_gestione;
            leggiVoce(aVoceAR);
			if aVoceAR.ds_voce!=descSottoArtCDSProprio(aVoceC.cd_proprio_voce,aVoceA) then
    	     aVoceAR.ds_voce:=descSottoArtCDSProprio(aVoceC.cd_proprio_voce,aVoceA);
    	     aVoceAR.duva:=aUO.duva;
    	     aVoceAR.utuv:=aUO.utuv;
             aggiornaVoce(aVoceAR);
            end if;
		   exception when no_data_found then
            aVoceAR.cd_cds:=aVoceC.cd_proprio_voce;
            aVoceAR.cd_titolo_capitolo:=aVoceA.cd_titolo_capitolo;
            aVoceAR.cd_sezione_capitolo:=NULL;
            aVoceAR.cd_proprio_voce:=aUO.cd_unita_organizzativa;
            aVoceAR.cd_elemento_voce:=aVoceA.cd_elemento_voce;
            aVoceAR.cd_parte:=aVoceA.cd_parte;
    	    aVoceAR.ds_voce:=descSottoArtCDSProprio(aVoceC.cd_proprio_voce,aVoceA);
            aVoceAR.fl_mastrino:='Y';
            aVoceAR.livello:=7;
            aVoceAR.ti_voce:=SOTTOARTICOLO;
            aVoceAR.cd_funzione:=aVoceA.cd_funzione;
            aVoceAR.cd_unita_organizzativa:=NULL;
            aVoceAR.cd_centro_responsabilita:=NULL;
            aVoceAR.cd_categoria:=aVoceA.cd_categoria;
            aVoceAR.cd_natura := aVoceA.cd_natura;
            aVoceAR.cd_voce_padre:=aVoceA.cd_voce;
            aVoceAR.dacr:=aDacr;
            aVoceAR.utcr:=aUtcr;

-- mastrino, elemento voce da fare
            aVoceAR.cd_elemento_voce := aVoceA.cd_titolo_capitolo;
            insertVocePdc(aVoceAR);

           end;
       end if;
	  end loop;
	 end loop;
	end loop;

   end if;


  -- se si tratta di CDS SAC alimento il sottoarticolo di spesa del CNR per Categoria II

  if(aUO.fl_cds = 'Y' and aUO.cd_tipo_unita = TIPOCDS_SAC) then

   -- leggo la categoria sopra la sezione

   for aArticolo in (select * from voce_f where
         esercizio = aEs
     and ti_voce = ARTICOLO
	 and ti_gestione = GESTIONE_SPESE
	 and ti_appartenenza = APPARTENENZA_CNR
	 and cd_categoria = CATEGORIA2_SPESE_CNR for update nowait) loop -- Parte 1 - Titolo x - Categoria 2
	   -- Sotto l'articolo inserisco il sottoarticolo = al CDS per nature 1-4
       if aArticolo.cd_natura != NATURA_5 then
    	   begin
    	    aVoceAR.esercizio:=aEs;
            aVoceAR.cd_voce:=IBMUTL001.dotConcat(aArticolo.cd_voce,aUO.cd_unita_organizzativa);
            aVoceAR.ti_appartenenza:=aArticolo.ti_appartenenza;
            aVoceAR.ti_gestione:=aArticolo.ti_gestione;
            leggiVoce(aVoceAR);
    	    if aVoceAR.ds_voce!='CDS-'||aUO.cd_unita_organizzativa||' nat.'||aArticolo.ds_voce then
			 aVoceAR.ds_voce:='CDS-'||aUO.cd_unita_organizzativa||' nat.'||aArticolo.ds_voce;
    	     aVoceAR.duva:=aDuva;
    	     aVoceAR.utuv:=aUtuv;
             aggiornaVoce(aVoceAR);
            end if;
		   exception when no_data_found then
            aVoceAR.cd_cds:=aUO.cd_unita_organizzativa;
            aVoceAR.cd_titolo_capitolo:=aArticolo.cd_titolo_capitolo;
            aVoceAR.cd_sezione_capitolo:=NULL;
            aVoceAR.cd_proprio_voce:=aUO.cd_unita_organizzativa;
            aVoceAR.cd_elemento_voce:=aArticolo.cd_elemento_voce;
            aVoceAR.cd_parte:=aArticolo.cd_parte;
    	    aVoceAR.ds_voce:='CDS-'||aUO.cd_unita_organizzativa||' nat.'||aArticolo.ds_voce;
            aVoceAR.fl_mastrino:='Y';
            aVoceAR.livello:=7;
            aVoceAR.ti_voce:=SOTTOARTICOLO;
            aVoceAR.cd_funzione:=aArticolo.cd_funzione;
            aVoceAR.cd_unita_organizzativa:=NULL;
            aVoceAR.cd_centro_responsabilita:=NULL;
            aVoceAR.cd_categoria:=aArticolo.cd_categoria;
            aVoceAR.cd_natura := aArticolo.cd_natura;
            aVoceAR.cd_voce_padre:=aArticolo.cd_voce;
            aVoceAR.dacr:=aDacr;
            aVoceAR.utcr:=aUtcr;

-- mastrino, elemento voce
            aVoceAR.CD_ELEMENTO_VOCE := aArticolo.cd_titolo_capitolo;
            insertVocePdc(aVoceAR);

           end;
       end if;
	end loop;
   end if;

   -- Entrate CNR aggiungo l'UO CDS come ARTICOLO
  -- NON NECESSARIO eliminata la Uo nella costruzione del cd_voce di entrata 14/10/2014
  /*
  if(aUO.fl_cds = 'N') then
    for aVoceC in (select * from voce_f where
           esercizio = aEs
       and ti_voce = CAPITOLO
	   and ti_gestione = GESTIONE_ENTRATE
	   and ti_appartenenza = APPARTENENZA_CNR for update nowait
    ) loop
	    --inserisco l'UO come Articolo sotto il Capitolo
	 begin -- Imposto la chiave primaria di voce_f
      aVoceA.esercizio:=aEs;
      aVoceA.cd_voce:=IBMUTL001.dotConcat(aVoceC.cd_voce,aUO.cd_unita_organizzativa);
      aVoceA.ti_appartenenza:=aVoceC.ti_appartenenza;
      aVoceA.ti_gestione:=aVoceC.ti_gestione;
      leggiVoce(aVoceA); -- leggo la voce
	  if aVoceA.ds_voce!=aUO.ds_unita_organizzativa then
	   aVoceA.ds_voce:=aUO.ds_unita_organizzativa;
	   aVoceA.duva:=aDuva;
	   aVoceA.utuv:=aUtuv;
	   aggiornaVoce(aVoceA);
	  end if;
	 exception when no_data_found then -- se la voce non c'è la inserisco
        aVoceA.cd_cds:=aUO.cd_unita_padre;
        aVoceA.cd_titolo_capitolo:=aVoceC.cd_voce;
        aVoceA.cd_sezione_capitolo:=NULL;
        aVoceA.cd_proprio_voce:=aUO.cd_unita_organizzativa;
        aVoceA.cd_elemento_voce:=aVoceC.cd_elemento_voce;
        aVoceA.cd_parte:=NULL;
	aVoceA.ds_voce:=aUO.ds_unita_organizzativa;
        aVoceA.fl_mastrino:='Y';
        aVoceA.livello:=4;
        aVoceA.ti_voce:=ARTICOLO;
        aVoceA.cd_funzione:=NULL;
        aVoceA.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
        aVoceA.cd_centro_responsabilita:=NULL;
        aVoceA.cd_categoria:=aVoceC.cd_categoria; -- Imposta il codice di categoria sull'articolo di entrata CNR
        aVoceA.cd_natura := NULL;
        aVoceA.cd_voce_padre:=aVoceC.cd_voce;
        aVoceA.dacr:=aDacr;
        aVoceA.utcr:=aUtcr;

-- mastrino, elemento voce da fare
        aVoceA.cd_elemento_voce := aVoceC.cd_voce;
        insertVocePdc(aVoceA);

      end;
     end loop;
    end if;
    */
   -- Spese CDS aggiungo l'UO rubrica come RUBRICA

    if(aUO.fl_cds = 'N' and aUO.fl_rubrica = 'Y') then
     for aVoceT in (select /*+ index (voce_f px_voce_f) */ * from voce_f where
           esercizio = aEs
       and ti_voce = TITOLO
	   and ti_gestione = GESTIONE_SPESE
	   and ti_appartenenza = APPARTENENZA_CDS for update nowait) loop  -- loop su Titoli

      for aFunzione in (select * from funzione
                        Where FL_UTILIZZABILE = 'Y') loop -- loop su funzioni

	   for aEV in (select * from elemento_voce a where
	          a.esercizio = aEs
          and a.ti_elemento_voce = CAPITOLO
	      and a.ti_gestione = GESTIONE_SPESE
	      and a.ti_appartenenza = APPARTENENZA_CDS
		  and a.cd_elemento_padre = aVoceT.cd_voce
	      and exists (select 1 from ass_ev_funz_tipocds b where -- Inserisco solo se trovo nella tabella degli incroci
	            b.esercizio = a.esercizio
	        and b.cd_conto = a.cd_elemento_voce
	        and b.cd_funzione = aFunzione.cd_funzione
	        and b.cd_tipo_unita = aUO.cd_tipo_unita
	      )
      	 ) loop -- loop sui titoli capitoli esistenti nella tabella degli incroci

         --inserisco la sezione tra le voci

		 begin
          aVoceS.cd_cds:=NULL;
          aVoceS.cd_titolo_capitolo:=aVoceT.cd_titolo_capitolo; -- fix 20020127
          aVoceS.cd_sezione_capitolo:=NULL;
          aVoceS.cd_voce:=IBMUTL001.dotConcat(aVoceT.cd_voce,aFunzione.cd_funzione);
          aVoceS.cd_proprio_voce:=aFunzione.cd_funzione;
          aVoceS.cd_elemento_voce:=aVoceT.cd_elemento_voce;
          aVoceS.cd_parte:=aVoceT.cd_parte;
          aVoceS.ds_voce:=aFunzione.ds_funzione;
          aVoceS.fl_mastrino:='N';
          aVoceS.livello:=3;
          aVoceS.ti_appartenenza:=aEV.ti_appartenenza;
          aVoceS.ti_gestione:=aEV.ti_gestione;
          aVoceS.ti_voce:=SEZIONE;
          aVoceS.cd_funzione:=aFunzione.cd_funzione;
          aVoceS.cd_unita_organizzativa:=NULL;
          aVoceS.cd_centro_responsabilita:=NULL;
          aVoceS.cd_categoria:=NULL;
          aVoceS.cd_voce_padre:=aVoceT.cd_voce;
          aVoceS.cd_natura := NULL;
          aVoceS.esercizio:=aEV.esercizio;
          aVoceS.dacr:=aDacr;
          aVoceS.utcr:=aUtcr;

-- no mastrino, no elemento voce
          insertVocePdc(aVoceS);

         exception when dup_val_on_index then
          NULL;
         end;

         -- genero le rubriche sotto le sezioni

      	 begin -- Imposto la chiave primaria di voce_f
          aVoceR.esercizio:=aEs;
          aVoceR.cd_voce:=IBMUTL001.dotConcat(aVoceS.cd_voce,aUO.cd_unita_organizzativa);
          aVoceR.ti_appartenenza:=aVoceS.ti_appartenenza;
          aVoceR.ti_gestione:=aVoceS.ti_gestione;
          leggiVoce(aVoceR); -- leggo la voce
		  if aVoceR.ds_voce!=aUO.ds_unita_organizzativa then
           aVoceR.ds_voce:=aUO.ds_unita_organizzativa;
           aVoceR.duva:=aDuva;
           aVoceR.utuv:=aUtuv;
           aggiornaVoce(aVoceR);
      	  end if;
		 exception when no_data_found then -- se la voce non c'è la inserisco
          aVoceR.cd_cds:=aUO.cd_unita_padre;
          aVoceR.cd_titolo_capitolo:=aVoceS.cd_titolo_capitolo;
          aVoceR.cd_sezione_capitolo:=NULL;
          aVoceR.cd_proprio_voce:=aUO.cd_unita_organizzativa;
          aVoceR.cd_elemento_voce:=aVoceS.cd_elemento_voce;
          aVoceR.cd_parte:=aVoceS.cd_parte;
          aVoceR.ds_voce:=aUO.ds_unita_organizzativa;
          aVoceR.fl_mastrino:='N';
          aVoceR.livello:=4;
          aVoceR.ti_voce:=RUBRICA;
          aVoceR.cd_funzione:=aFunzione.cd_funzione;
          aVoceR.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
          aVoceR.cd_centro_responsabilita:=NULL;
          aVoceR.cd_categoria:=NULL;
          aVoceR.cd_natura := NULL;
          aVoceR.cd_voce_padre:=aVoceS.cd_voce;
          aVoceR.dacr:=aDacr;
          aVoceR.utcr:=aUtcr;

-- no mastrino, no elemento voce
          insertVocePdc(aVoceR);

         end;

         -- Per ogni rubrica aggiunta inserisco il capitolo come voce

         begin
          aVoceC.esercizio:=aEV.esercizio;
          aVoceC.ti_appartenenza:=aVoceR.ti_appartenenza;
          aVoceC.ti_gestione:=aVoceR.ti_gestione;
          aVoceC.cd_voce:=IBMUTL001.dotConcat(aVoceR.cd_voce,aEV.cd_proprio_elemento);
          leggiVoce(aVoceC);
		  if aVoceC.ds_voce!=aEV.ds_elemento_voce then
           aVoceC.ds_voce:=aEV.ds_elemento_voce;
           aVoceC.duva:=aDuva;
           aVoceC.utuv:=aUtuv;
           aggiornaVoce(aVoceC);
          end if;
     exception when no_data_found then
          aVoceC.cd_proprio_voce:=aEV.cd_proprio_elemento;
          aVoceC.cd_cds:=aUO.cd_unita_padre;
          aVoceC.cd_titolo_capitolo:=aEV.cd_elemento_voce;
          aVoceC.cd_sezione_capitolo:=IBMUTL001.dotConcat(aVoceS.cd_voce,aEV.cd_proprio_elemento);
          aVoceC.cd_elemento_voce:=aVoceR.cd_elemento_voce;
          aVoceC.cd_parte:=aVoceR.cd_parte;
          aVoceC.ds_voce:=aEV.ds_elemento_voce;
	  if(aUO.cd_tipo_unita=TIPOCDS_SAC) then
           aVoceC.fl_mastrino:='N';
          else
           aVoceC.fl_mastrino:='Y';
          end if;
          aVoceC.livello:=5;
          aVoceC.ti_voce:=aEV.ti_elemento_voce;
          aVoceC.cd_funzione:=aFunzione.cd_funzione;
          aVoceC.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
          aVoceC.cd_centro_responsabilita:=NULL;
          aVoceC.cd_categoria:=NULL;
          aVoceC.cd_natura := NULL;
          aVoceC.cd_voce_padre:=aVoceR.cd_voce;
          aVoceC.dacr:=aDacr;
          aVoceC.utcr:=aUtcr;

-- se mastrino, elemento voce da fare
          If aVoceC.fl_mastrino = 'Y' Then
             aVoceC.cd_elemento_voce := aEV.cd_elemento_voce;
          End If;

          insertVocePdc(aVoceC);

         end;

          -- Sotto il capitolo aggiungo gli articoli per il SAC
		  -- I cdr sono sia il cdr di I livello che quelli di secondo che a lui afferiscono

         if(aUO.cd_tipo_unita=TIPOCDS_SAC) then
          for aCDR in (select cdrNR.* from v_cdr_valido cdrR, v_cdr_valido cdrNR where
			     cdrR.esercizio=aEs
			 and cdrR.cd_unita_organizzativa = aUO.cd_unita_organizzativa
			 and cdrNR.esercizio=aEs
             and (
			     cdrNR.cd_unita_organizzativa=aUO.cd_unita_organizzativa
			  or cdrNR.cd_cdr_afferenza = cdrR.cd_centro_responsabilita
			 )
         ) loop
		  begin
            -- loop sui CDR dell'UO
            --inserisco l'articolo solo per il SAC

	         --leggo l'UO della rubrica o non rubrica
        	 aUOCDR:=CNRCTB020.GETUOVALIDA(aEV.esercizio,aCDR.cd_unita_organizzativa);

			 aVoceA.cd_cds:=aUO.cd_unita_padre;
             aVoceA.cd_titolo_capitolo:=aVoceC.cd_titolo_capitolo;
             aVoceA.cd_sezione_capitolo:=aVoceC.cd_sezione_capitolo;
             aVoceA.cd_voce:=IBMUTL001.dotConcat(aVoceC.cd_voce,aUOCDR.cd_proprio_unita);
             aVoceA.cd_proprio_voce:=aUOCDR.cd_proprio_unita;
             aVoceA.cd_elemento_voce:=aVoceC.cd_elemento_voce;
             aVoceA.cd_parte:=aVoceC.cd_parte;
             aVoceA.ds_voce:=aCDR.ds_cdr;
             aVoceA.fl_mastrino:='Y';
             aVoceA.livello:=6;
             aVoceA.ti_appartenenza:=aVoceC.ti_appartenenza;
             aVoceA.ti_gestione:=aVoceC.ti_gestione;
             aVoceA.ti_voce:=ARTICOLO;
             aVoceA.cd_funzione:=aFunzione.cd_funzione;
             aVoceA.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
             aVoceA.cd_centro_responsabilita:=aCDR.cd_centro_responsabilita;
             aVoceA.cd_categoria:=NULL;
             aVoceA.cd_voce_padre:=aVoceC.cd_voce;
             aVoceA.cd_natura := NULL;
             aVoceA.esercizio:=aEV.esercizio;
             aVoceA.dacr:=aDacr;
             aVoceA.utcr:=aUtcr;

-- mastrino, elemento voce da fare
             aVoceA.cd_elemento_voce := aVoceC.cd_titolo_capitolo;

             insertVocePdc(aVoceA);

		   exception when dup_val_on_index then
			null;
		   end;
          end loop;
         end if;
	   end loop;
  	  end loop;
     end loop;
	end if;
 end;


-- ***************************************************************************
-- ESPLOSIONE SOTTOARTICOLI AREA
-- ***************************************************************************

  procedure creaEsplSottArtArea(aEsercizio number, aCdArea varchar2, aUser varchar2) is
   aCDSPresidenteArea unita_organizzativa%rowtype;
   aCDSArea unita_organizzativa%rowtype;
   aCDSSAC unita_organizzativa%rowtype;
   aUO unita_organizzativa%rowtype;
   aVoceAR voce_f%rowtype;
   aTSNow date;
  begin
   aTSNow:=sysdate;

   aCDSArea:=CNRCTB020.getUOValida(aEsercizio,aCdArea);

   select * into aCDSArea from unita_organizzativa where
	    cd_unita_organizzativa = aCdArea
	and cd_tipo_unita = CNRCTB020.TIPO_AREA
	and fl_cds = 'Y';

   aCDSSAC:=CNRCTB020.getCDSSACValido(aEsercizio);

   -- Leggo il CDS attualmente presidente dell'area per l'esercizio aEsercizio

   aCDSPresidenteArea:=CNRCTB020.getCDSPresidenteArea(aEsercizio, aCDSArea);
   if aCDSPresidenteArea.cd_unita_organizzativa is null then
     -- Se il presidente dell'area in processo non è definito, elimino l'attuale
	 -- esplosione in sottoarticoli dell'area in VOCE_F

     for aSottArt in (select * from voce_f where -- Lock dei sottoarticoli interessati
          esercizio = aEsercizio
      and ti_appartenenza = APPARTENENZA_CNR
      and ti_gestione = GESTIONE_SPESE
      and ti_voce = SOTTOARTICOLO
      and cd_proprio_voce = aCdArea for update nowait) loop
       NULL;
     end loop;

     delete from voce_f where
          esercizio = aEsercizio
      and ti_appartenenza = APPARTENENZA_CNR
      and ti_gestione = GESTIONE_SPESE
      and ti_voce = SOTTOARTICOLO
      and cd_proprio_voce = aCdArea;
    return;
   end if;

   -- Leggo da voce_f tutti i sottoarticoli corrispondenti all'area in processo

   for aSottArt in (select * from voce_f where
        esercizio = aEsercizio
	and ti_appartenenza = APPARTENENZA_CNR
	and ti_gestione = GESTIONE_SPESE
	and ti_voce = SOTTOARTICOLO
	and cd_proprio_voce = aCdArea for update nowait) loop
    if aSottArt.cd_cds = aCDSPresidenteArea.cd_unita_organizzativa then
	 return; -- se il presidente corrente risulta già esploso in sottoarticoli dell'area esco
	end if;
   end loop;

   -- Se arrivo qui significa che il nuovo presidente di area non corrisponde all'esplosione
   -- corrente in sottoarticoli. Quindi elimino l'esplosione corrente.

   delete from voce_f where
        esercizio = aEsercizio
	and ti_appartenenza = APPARTENENZA_CNR
	and ti_gestione = GESTIONE_SPESE
	and ti_voce = SOTTOARTICOLO
	and cd_proprio_voce = aCdArea;

   -- Genera l'esplosione in sottoarticoli sotto il nuovo presidente dell'area

   for aCapitolo in (select * from voce_f where
              esercizio = aEsercizio
    	  and ti_appartenenza = APPARTENENZA_CNR
      	  and ti_gestione = GESTIONE_SPESE
          and cd_categoria = CATEGORIA1_SPESE_CNR
          and cd_proprio_voce = aCDSPresidenteArea.cd_unita_organizzativa
          and ti_voce = CAPITOLO for update nowait
       ) loop
     for aArticolo in (select * from voce_f where
              esercizio = aEsercizio
    	  and ti_appartenenza = APPARTENENZA_CNR
      	  and ti_gestione = GESTIONE_SPESE
          and cd_voce_padre = aCapitolo.cd_voce
          and ti_voce = ARTICOLO for update nowait
       ) loop
       begin
          aVoceAR.esercizio:=aEsercizio;
          aVoceAR.cd_voce:=IBMUTL001.dotConcat(aArticolo.cd_voce,aCDSArea.cd_unita_organizzativa);
          aVoceAR.ti_appartenenza:=aArticolo.ti_appartenenza;
          aVoceAR.ti_gestione:=aArticolo.ti_gestione;
          leggiVoce(aVoceAR);
          aVoceAR.ds_voce:=descSottoArtCDSArea(aCDSArea.cd_unita_organizzativa,aArticolo);
          aVoceAR.duva:=aTSNow;
          aVoceAR.utuv:=aUser;
          aggiornaVoce(aVoceAR);
         exception when no_data_found then
          aVoceAR.cd_cds:=aCapitolo.cd_proprio_voce;
          aVoceAR.cd_titolo_capitolo:=aArticolo.cd_titolo_capitolo;
          aVoceAR.cd_sezione_capitolo:=NULL;
          aVoceAR.cd_proprio_voce:=aCDSArea.cd_unita_organizzativa;
          aVoceAR.cd_elemento_voce:=aArticolo.cd_elemento_voce;
          aVoceAR.cd_parte:=aArticolo.cd_parte;
          aVoceAR.ds_voce:=descSottoArtCDSArea(aCDSArea.cd_unita_organizzativa,aArticolo);
          aVoceAR.fl_mastrino:='Y';
          aVoceAR.livello:=7;
          aVoceAR.ti_voce:=SOTTOARTICOLO;
          aVoceAR.cd_funzione:=aArticolo.cd_funzione;
          aVoceAR.cd_unita_organizzativa:=NULL;
          aVoceAR.cd_centro_responsabilita:=NULL;
          aVoceAR.cd_categoria:=aArticolo.cd_categoria;
          aVoceAR.cd_natura := aArticolo.cd_natura;
          aVoceAR.cd_voce_padre:=aArticolo.cd_voce;
          aVoceAR.dacr:=aTSNow;
          aVoceAR.utcr:=aUser;

-- mastrino, elemento voce da fare
          aVoceAR.cd_elemento_voce := aArticolo.cd_titolo_capitolo;
          insertVocePdc(aVoceAR);

      end;
     end loop;
   end loop;
  end;

-- ***************************************************************************
-- ESPLOSIONE INDOTTA DA MODIFICA TABELLA CDR
-- ***************************************************************************

 procedure creaEsplVociCDR(aEs number, aCdCDR varchar2, aUser varchar2) is
  aCDR cdr%rowtype;
  aCDRPrimo cdr%rowtype;
  aUO unita_organizzativa%rowtype;
  aUOCDR unita_organizzativa%rowtype;
  aVoceA voce_f%rowtype;
  aVoceR voce_f%rowtype;
  aUtcr varchar2(20);
  aUtuv varchar2(20);
  aDuva date;
  aDacr date;
 begin

  aCDR:=CNRCTB020.getCDRValido(aEs, aCdCDR);

  if aUser is not null then
   aUtcr:=aUser;
   aUtuv:=aUser;
   aDacr:=sysdate;
   aDuva:=sysdate;
  else
   aUtcr:=aCDR.utcr;
   aUtuv:=aCDR.utuv;
   aDacr:=aCDR.dacr;
   aDuva:=aCDR.duva;
  end if;

  if aCDR.cd_cdr_afferenza is not null then
   aCDRPrimo:=CNRCTB020.getCDRValido(aEs, aCDR.cd_cdr_afferenza);
  end if;

  -- Aggiornamento indotto da creazione o modifica di CDR
  -- Estrae la rubrica a cui il cdr in inserimento appartiene

  aUOCDR:=CNRCTB020.GETUOVALIDA(aEs,aCDR.cd_unita_organizzativa);

  if aCDR.cd_cdr_afferenza is not null then
   aUO:=CNRCTB020.GETUOVALIDA(aEs,aCDRPrimo.cd_unita_organizzativa);
  else
   aUO:=CNRCTB020.GETUOVALIDA(aEs,aCDR.cd_unita_organizzativa);
  end if;

  -- Se l'UO è di tipo ENTE esco senza alcuna azione

  if(aUO.cd_tipo_unita != TIPOCDS_SAC) then
   return;
  end if;

  -- Leggo l'UO collegata per determinare il tipo di CDS

  for aVoceT in (select *  from voce_f where -- ciclo sui titoli
           esercizio = aEs
       and cd_parte = PARTE1
	   and ti_voce = TITOLO
	   and ti_gestione = GESTIONE_SPESE
	   and ti_appartenenza = APPARTENENZA_CDS for update nowait) loop  -- loop su Titoli
   for aVoceS in (select /*+ index (voce_f rx_voce_f00) */ * from voce_f where -- ciclo sulle sezioni
           esercizio = aEs
       and ti_voce = SEZIONE
       and cd_voce_padre = aVoceT.cd_voce
	   and ti_gestione = GESTIONE_SPESE
	   and ti_appartenenza = APPARTENENZA_CDS for update nowait) loop  -- loop su Titoli
    begin
	  select * into aVoceR from voce_f where -- estraggo la rubrica
            esercizio = aEs
        and cd_voce = IBMUTL001.DOTCONCAT(aVoceS.cd_voce,aUO.cd_unita_organizzativa)
        and ti_voce = RUBRICA
	    and ti_gestione = GESTIONE_SPESE
	    and ti_appartenenza = APPARTENENZA_CDS for update nowait;

     for aVocePadre in (select /*+ index (voce_f rx_voce_f00) */ * from voce_f where
            esercizio = aEs
        and cd_voce_padre = aVoceR.cd_voce
        and ti_voce = CAPITOLO
	    and ti_gestione = GESTIONE_SPESE
	    and ti_appartenenza = APPARTENENZA_CDS for update nowait) loop  -- loop sui capitoli
     --inserisco o modifico l'articolo
      begin
        aVoceA.esercizio:=aEs;
        aVoceA.ti_appartenenza:=aVocePadre.ti_appartenenza;
        aVoceA.ti_gestione:=aVocePadre.ti_gestione;
        aVoceA.cd_voce:=IBMUTL001.dotConcat(aVocePadre.cd_voce,aUOCDR.cd_proprio_unita);
        leggiVoce(aVoceA);
        aVoceA.ds_voce:=aCDR.ds_cdr;
        aVoceA.duva:=aDuva;
        aVoceA.utuv:=aUtuv;
        aggiornaVoce(aVoceA);
      exception when no_data_found then
        aVoceA.cd_cds:=aUO.cd_unita_padre;
        aVoceA.cd_titolo_capitolo:=aVocePadre.cd_titolo_capitolo;
        aVoceA.cd_sezione_capitolo:=aVocePadre.cd_sezione_capitolo;
        aVoceA.cd_proprio_voce:=aUOCDR.cd_proprio_unita;
        aVoceA.cd_elemento_voce:=aVocePadre.cd_elemento_voce;
        aVoceA.cd_parte:=aVocePadre.cd_parte;
        aVoceA.ds_voce:=aCDR.ds_cdr;
        aVoceA.fl_mastrino:='Y';
        aVoceA.livello:=6;
        aVoceA.ti_voce:=ARTICOLO;
        aVoceA.cd_funzione:=aVocePadre.cd_funzione;
        aVoceA.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
        aVoceA.cd_centro_responsabilita:=aCDR.cd_centro_responsabilita;
        aVoceA.cd_categoria:=NULL;
        aVoceA.cd_natura := NULL;
        aVoceA.cd_voce_padre:=aVocePadre.cd_voce;
        aVoceA.dacr:=aDacr;
        aVoceA.utcr:=aUtcr;

-- mastrino, elemento voce da fare
        aVoceA.cd_elemento_voce := aVocePadre.cd_titolo_capitolo;
        insertVocePdc(aVoceA);

	  end;
     end loop;
    exception when no_data_found then
	 NULL;
	end;
   end loop;
  end loop;
 end;

-- *****************************************************************************************
-- Esplode in voci l'aggiunta di una nuova associazione tra elemento_voce/funzione/tipo_cds
-- *****************************************************************************************

 procedure creaEsplIncroci(aASSEVFUNZTIPOCDS IN ass_ev_funz_tipocds%rowtype) is

  aVoceS voce_f%rowtype;
  aVoceR voce_f%rowtype;
  aVoceC voce_f%rowtype;
  aVoceA voce_f%rowtype;
  aUO unita_organizzativa%rowtype;
  aVocePadre voce_f%rowtype;
  aEV elemento_voce%rowtype;
  aUOCDR unita_organizzativa%rowtype;
  i number;
  begin

   -- esplosione parte 1 spese CDS

   -- Estraggo il titolo_capitolo da elemento_voce

   begin
    select * into aEV from elemento_voce where
         esercizio = aASSEVFUNZTIPOCDS.esercizio
	 and ti_gestione = GESTIONE_SPESE
     and ti_appartenenza = APPARTENENZA_CDS
	 and ti_elemento_voce = CAPITOLO
	 and cd_elemento_voce = aASSEVFUNZTIPOCDS.cd_conto for update nowait;
   exception when no_data_found then
    return; -- Se l'elemento voce non è stato ancora definito esco
   end;

   -- estraggo la voce padre a cui attaccare le voci generate (TITOLO)

   select * into aVocePadre from voce_f where
        esercizio = aEV.esercizio
    and cd_voce = aEV.cd_elemento_padre
	and ti_gestione = aEV.ti_gestione
	and ti_appartenenza = aEV.ti_appartenenza for update nowait;

   -- genero le sezioni

   for aFunzione in (select cd_funzione, ds_funzione
                     from Funzione
                     Where  cd_funzione = aASSEVFUNZTIPOCDS.cd_funzione And
                            FL_UTILIZZABILE = 'Y' And
                                exists (select 1 from v_unita_organizzativa_valida
                                        Where esercizio = aASSEVFUNZTIPOCDS.esercizio And
                                              cd_tipo_unita = aASSEVFUNZTIPOCDS.cd_tipo_unita And
                                              cd_tipo_unita != TIPO_ENTE And fl_cds = 'N' And
                                              fl_rubrica = 'Y')
   ) loop -- loop sulle funzioni
    begin
     --inserisco la sezione tra le voci
     aVoceS.cd_cds:=NULL;
     aVoceS.cd_titolo_capitolo:=aVocePadre.cd_titolo_capitolo;
     aVoceS.cd_sezione_capitolo:=NULL;
     aVoceS.cd_voce:=IBMUTL001.dotConcat(aVocePadre.cd_voce,aFunzione.cd_funzione);
     aVoceS.cd_proprio_voce:=aFunzione.cd_funzione;
     aVoceS.cd_elemento_voce:=aVocePadre.cd_elemento_voce;
     aVoceS.cd_parte:=aVocePadre.cd_parte;
     aVoceS.ds_voce:=aFunzione.ds_funzione;
     aVoceS.fl_mastrino:='N';
     aVoceS.livello:=3;
     aVoceS.ti_appartenenza:=aEV.ti_appartenenza;
     aVoceS.ti_gestione:=aEV.ti_gestione;
     aVoceS.ti_voce:=SEZIONE;
     aVoceS.cd_funzione:=aFunzione.cd_funzione;
     aVoceS.cd_unita_organizzativa:=NULL;
     aVoceS.cd_centro_responsabilita:=NULL;
     aVoceS.cd_voce_padre:=aVocePadre.cd_voce;
     aVoceS.cd_natura := NULL;
     aVoceS.esercizio:=aEV.esercizio;
     aVoceS.dacr:=aASSEVFUNZTIPOCDS.dacr;
     aVoceS.utcr:=aASSEVFUNZTIPOCDS.utcr;

-- no mastrino, no elemento voce
     insertVocePdc(aVoceS);

	exception when dup_val_on_index then
	 NULL;
	end;

	-- genero le rubriche sotto le sezioni

    for aUO in (select * from v_unita_organizzativa_valida where
	     esercizio = aEV.esercizio
	 and fl_cds = 'N'
	 and fl_rubrica='Y'
	 and cd_tipo_unita = aASSEVFUNZTIPOCDS.cd_tipo_unita
     and cd_tipo_unita != TIPO_ENTE
	) loop -- loop sulle rubriche
	 --inserisco la rubrica tra le voci
	 begin
      aVoceR.cd_cds:=aUO.cd_unita_padre;
      aVoceR.cd_titolo_capitolo:=aVoceS.cd_titolo_capitolo;
      aVoceR.cd_sezione_capitolo:=NULL;
      aVoceR.cd_voce:=IBMUTL001.dotConcat(aVoceS.cd_voce,aUO.cd_unita_organizzativa);
      aVoceR.cd_proprio_voce:=aUO.cd_unita_organizzativa;
      aVoceR.cd_elemento_voce:=aVoceS.cd_elemento_voce;
      aVoceR.cd_parte:=aVocePadre.cd_parte;
      aVoceR.ds_voce:=aUO.ds_unita_organizzativa;
      aVoceR.fl_mastrino:='N';
      aVoceR.livello:=4;
      aVoceR.ti_appartenenza:=aEV.ti_appartenenza;
      aVoceR.ti_gestione:=aEV.ti_gestione;
      aVoceR.ti_voce:=RUBRICA;
      aVoceR.cd_funzione:=aFunzione.cd_funzione;
      aVoceR.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
      aVoceR.cd_centro_responsabilita:=NULL;
      aVoceR.cd_voce_padre:=aVoceS.cd_voce;
      aVoceR.cd_natura := NULL;
      aVoceR.esercizio:=aEV.esercizio;
      aVoceR.dacr:=aASSEVFUNZTIPOCDS.dacr;
      aVoceR.utcr:=aASSEVFUNZTIPOCDS.utcr;

-- no mastrino, no elemento voce
      insertVocePdc(aVoceR);

	 exception when dup_val_on_index then
	  NULL;
	 end;

	 -- Per ogni rubrica aggiunta inserisco il capitolo come voce

	 begin
      aVoceC.esercizio:=aEV.esercizio;
      aVoceC.ti_appartenenza:=aVoceR.ti_appartenenza;
      aVoceC.ti_gestione:=aVoceR.ti_gestione;
      aVoceC.cd_voce:=IBMUTL001.dotConcat(aVoceR.cd_voce,aEV.cd_proprio_elemento);
      leggiVoce(aVoceC);
	  aVoceC.ds_voce:=aEV.ds_elemento_voce;
	  aVoceC.duva:=aASSEVFUNZTIPOCDS.duva;
	  aVoceC.utuv:=aASSEVFUNZTIPOCDS.utuv;
	  aggiornaVoce(aVoceC);
	 exception when no_data_found then
      aVoceC.cd_proprio_voce:=aEV.cd_proprio_elemento;
      aVoceC.cd_elemento_voce:=aVoceR.cd_elemento_voce;
      aVoceC.cd_cds:=aUO.cd_unita_padre;
      aVoceC.cd_titolo_capitolo:=aEV.cd_elemento_voce;
      aVoceC.cd_sezione_capitolo:=IBMUTL001.dotConcat(aVoceS.cd_voce,aEV.cd_proprio_elemento);
      aVoceC.cd_parte:=aVoceR.cd_parte;
      aVoceC.ds_voce:=aEV.ds_elemento_voce;
      if(aUO.cd_tipo_unita=TIPOCDS_SAC) then
       aVoceC.fl_mastrino:='N';
      else
       aVoceC.fl_mastrino:='Y';
      end if;
      aVoceC.livello:=5;
      aVoceC.ti_voce:=aEV.ti_elemento_voce;
      aVoceC.cd_funzione:=aFunzione.cd_funzione;
      aVoceC.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
      aVoceC.cd_centro_responsabilita:=NULL;
      aVoceC.cd_natura := NULL;
      aVoceC.cd_voce_padre:=aVoceR.cd_voce;
      aVoceC.dacr:=aASSEVFUNZTIPOCDS.dacr;
      aVoceC.utcr:=aASSEVFUNZTIPOCDS.utcr;

-- se mastrino, elemento voce da fare
      If aVoceC.fl_mastrino ='Y' Then
        aVoceC.cd_elemento_voce := aEV.cd_elemento_voce;
      End If;

      insertVocePdc(aVoceC);

     end;

	  -- Sotto il capitolo aggiungo gli articoli per il SAC

	 if(aUO.cd_tipo_unita=TIPOCDS_SAC) then
      for aCDR in (select cdrNR.cd_unita_organizzativa, cdrNR.cd_proprio_cdr, cdrNR.cd_centro_responsabilita, cdrNR.ds_cdr from v_cdr_valido cdrR, v_cdr_valido cdrNR where
			     cdrR.esercizio=aUO.esercizio
			 and cdrR.cd_unita_organizzativa = aUO.cd_unita_organizzativa
			 and cdrNR.esercizio=aUO.esercizio
             and (
			     cdrNR.cd_unita_organizzativa=aUO.cd_unita_organizzativa
			  or cdrNR.cd_cdr_afferenza = cdrR.cd_centro_responsabilita
			 )
	  ) loop
	   -- loop sui CDR dell'UO
	   -- inserisco l'articolo solo per il SAC

	   --leggo l'UO della rubrica o non rubrica
	   aUOCDR:=CNRCTB020.GETUOVALIDA(aEV.esercizio,aCDR.cd_unita_organizzativa);
	   begin
         aVoceA.cd_cds:=aUO.cd_unita_padre;
         aVoceA.cd_titolo_capitolo:=aVoceC.cd_titolo_capitolo;
         aVoceA.cd_sezione_capitolo:=aVoceC.cd_sezione_capitolo;
         aVoceA.cd_voce:=IBMUTL001.dotConcat(aVoceC.cd_voce,aUOCDR.cd_proprio_unita);
         aVoceA.cd_proprio_voce:=aUOCDR.cd_proprio_unita;
         aVoceA.cd_elemento_voce:=aVoceC.cd_elemento_voce;
         aVoceA.cd_parte:=aVoceC.cd_parte;
         aVoceA.ds_voce:=aCDR.ds_cdr;
         aVoceA.fl_mastrino:='Y';
         aVoceA.livello:=6;
         aVoceA.ti_appartenenza:=aVoceC.ti_appartenenza;
         aVoceA.ti_gestione:=aVoceC.ti_gestione;
         aVoceA.ti_voce:=ARTICOLO;
         aVoceA.cd_funzione:=aFunzione.cd_funzione;
         aVoceA.cd_unita_organizzativa:=aUO.cd_unita_organizzativa;
         aVoceA.cd_centro_responsabilita:=aCDR.cd_centro_responsabilita;
         aVoceA.cd_voce_padre:=aVoceC.cd_voce;
         aVoceA.cd_natura := NULL;
         aVoceA.esercizio:=aEV.esercizio;
         aVoceA.dacr:=aASSEVFUNZTIPOCDS.dacr;
         aVoceA.utcr:=aASSEVFUNZTIPOCDS.utcr;

-- mastrino, elemento voce da fare
         aVoceA.cd_elemento_voce := aVoceC.cd_titolo_capitolo;
         insertVocePdc(aVoceA);

	   exception when Dup_Val_On_Index then
		 null;
	   end;
      end loop;
	 end if;
	end loop;
   end loop;
  end;

 procedure creaEsplVociEsercizio(aEs number, aCdCDS varchar2, aUser varchar2) is
  aEsercizio esercizio%rowtype;
  aUtcr varchar2(20);
 begin
  begin
   select * into aEsercizio from esercizio where
	 esercizio = aEs
	 and cd_cds = aCdCDS;
  exception when NO_DATA_FOUND then
   return;
  end;

  if aUser is not null then
   aUtcr:=aUser;
  else
   aUtcr:=aEsercizio.utcr;
  end if;

  for aCDS in
   (select * from v_unita_organizzativa_valida where
         esercizio = aEs
	 and cd_unita_organizzativa = aCdCDS
	 and fl_cds = 'Y'
	 and cd_tipo_unita != CNRCTB020.TIPO_ENTE order by cd_unita_organizzativa) loop
   CREAESPLVOCIUO(aEs,aCdCds,aUtcr);
   for aUO in
    (select * from v_unita_organizzativa_valida where
          esercizio = aEs
	  and cd_unita_padre = aCDS.cd_unita_organizzativa
	  and fl_cds = 'N'
	  and cd_tipo_unita != CNRCTB020.TIPO_ENTE order by cd_unita_organizzativa) loop
	CREAESPLVOCIUO(aEs,aUO.cd_unita_organizzativa,aUtcr);
    for aCDR in
     (select * from v_cdr_valido where
           esercizio = aEs
	   and cd_unita_organizzativa = aUO.cd_unita_organizzativa order by cd_centro_responsabilita) loop
	 CREAESPLVOCICDR(aEs,aCDR.cd_centro_responsabilita,aUtcr);
	end loop;
   end loop;
  end loop;
 end;

 procedure eliminaEsplIncroci(aASSEVFUNZTIPOCDS IN ass_ev_funz_tipocds%rowtype) is
  aNum number;
 begin
   -- Le condizioni di eliminazione di un incrocio sono:

   -- non esiste nell'anno specificato in PDG_PREVENTIVO_SPE_DET alcun record corrispondente all'incrocio aASSEVFUNZTIPOCDS

   aNum:=0;
   select count(*) into aNum from pdg_preventivo_spe_det a, linea_attivita b, cdr c, unita_organizzativa d where
        b.cd_centro_responsabilita = a.cd_centro_responsabilita
	and b.cd_linea_attivita=a.cd_linea_attivita
    and c.cd_centro_responsabilita = a.cd_centro_responsabilita
	and d.cd_unita_organizzativa = c.cd_unita_organizzativa
	and d.cd_tipo_unita = aASSEVFUNZTIPOCDS.cd_tipo_unita
	and b.cd_funzione = aASSEVFUNZTIPOCDS.cd_funzione
	and a.esercizio = aASSEVFUNZTIPOCDS.esercizio
	and a.ti_gestione=GESTIONE_SPESE
	and a.ti_appartenenza=APPARTENENZA_CDS
	and a.cd_elemento_voce = aASSEVFUNZTIPOCDS.cd_conto;

   if aNum > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Associazione tra voce del piano funzione e tipo di CDS non eliminabile perchè utilizzata');
   end if;

   -- Blocco la tabella voce_f per l'eliminazione

   lock table voce_f in exclusive mode nowait;

   -- Elimina dalla tabella delle voci tutte quelle voci di spesa CDS parte I
   -- che non hanno più una corrispondenza nella tabella ASS_EV_FUNZ_TIPOCDS

   delete from voce_f where
        esercizio = aASSEVFUNZTIPOCDS.esercizio
    and ti_appartenenza = APPARTENENZA_CDS
    and ti_gestione = GESTIONE_SPESE
    and cd_parte = PARTE1
	and ti_voce = CAPITOLO
	and cd_funzione = aASSEVFUNZTIPOCDS.cd_funzione
    and cd_titolo_capitolo = aASSEVFUNZTIPOCDS.cd_conto
	and cd_unita_organizzativa in (select cd_unita_organizzativa from v_unita_organizzativa_valida where
	     esercizio = aASSEVFUNZTIPOCDS.esercizio
	 and cd_tipo_unita = aASSEVFUNZTIPOCDS.cd_tipo_unita
	 and cd_tipo_unita != TIPO_ENTE
	 and fl_rubrica = 'Y'
	 and fl_cds = 'N'
	);
  end;

 function descSottoArtCDSProprio(aCodiceSA varchar2, aArticolo voce_f%rowtype) return varchar2 is
 begin
  return 'CDS '||aCodiceSA||' Natura '||aArticolo.cd_natura||'-'||aArticolo.ds_voce;
 end;

 function descSottoArtCDSArea(aCodiceSA varchar2, aArticolo voce_f%rowtype) return varchar2 is
 begin
  return 'AREA '||aCodiceSA||' Natura '||aArticolo.cd_natura||'-'||aArticolo.ds_voce;
 end;


end;
