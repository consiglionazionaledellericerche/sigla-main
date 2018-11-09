--------------------------------------------------------
--  DDL for Package Body CNRCTB577
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB577" AS
 -- Carica estrazione CIR
 procedure getEstrazione(aCnrEstr in out CNR_ESTRAZIONE_CORI%rowtype) is
 begin
  select * into aCnrEstr from CNR_ESTRAZIONE_CORI where
         CD_CDS=aCnrEstr.CD_CDS
     and ESERCIZIO=aCnrEstr.ESERCIZIO
     and CD_UNITA_ORGANIZZATIVA=aCnrEstr.CD_UNITA_ORGANIZZATIVA
     and PG_LIQUIDAZIONE=aCnrEstr.PG_LIQUIDAZIONE
     and MATRICOLA=aCnrEstr.MATRICOLA
     and CODICE_FISCALE=aCnrEstr.CODICE_FISCALE
     and TI_PAGAMENTO=aCnrEstr.TI_PAGAMENTO
     and ESERCIZIO_COMPENSO=aCnrEstr.ESERCIZIO_COMPENSO
     and CD_IMPONIBILE=aCnrEstr.CD_IMPONIBILE
     and TI_ENTE_PERCIPIENTE=aCnrEstr.TI_ENTE_PERCIPIENTE
     and CD_CONTRIBUTO_RITENUTA=aCnrEstr.CD_CONTRIBUTO_RITENUTA
	 for update nowait;
 end;

 -- Aggiorna l'estrazione da CIR
 procedure updateEstrazione(aCnrEstr CNR_ESTRAZIONE_CORI%rowtype, aComp compenso%rowtype, aCori contributo_ritenuta%rowtype) is
 begin
  update CNR_ESTRAZIONE_CORI set
     IM_LORDO=IM_LORDO+aComp.im_lordo_percipiente,
     IMPONIBILE=IMPONIBILE+aCori.imponibile,
     IM_RITENUTA=IM_RITENUTA+aCori.ammontare,
     DT_INIZIO_COMPENSO = aCnrEstr.DT_INIZIO_COMPENSO,
     DT_FINE_COMPENSO = aCnrEstr.DT_FINE_COMPENSO,
     IM_CORI_SOSPESO = IM_CORI_SOSPESO+aCori.im_cori_sospeso
  where
         CD_CDS=aCnrEstr.CD_CDS
     and ESERCIZIO=aCnrEstr.ESERCIZIO
     and CD_UNITA_ORGANIZZATIVA=aCnrEstr.CD_UNITA_ORGANIZZATIVA
     and PG_LIQUIDAZIONE=aCnrEstr.PG_LIQUIDAZIONE
     and MATRICOLA=aCnrEstr.MATRICOLA
     and CODICE_FISCALE=aCnrEstr.CODICE_FISCALE
     and TI_PAGAMENTO=aCnrEstr.TI_PAGAMENTO
     and ESERCIZIO_COMPENSO=aCnrEstr.ESERCIZIO_COMPENSO
     and CD_IMPONIBILE=aCnrEstr.CD_IMPONIBILE
     and TI_ENTE_PERCIPIENTE=aCnrEstr.TI_ENTE_PERCIPIENTE
     and CD_CONTRIBUTO_RITENUTA=aCnrEstr.CD_CONTRIBUTO_RITENUTA;
 end;

 -- Aggiorna l'estrazione da interfaccia PISA
 procedure updateEstrazione(aCnrEstr CNR_ESTRAZIONE_CORI%rowtype, aLiqIntDett liquid_cori_interf_dett%rowtype) is
 begin
  update CNR_ESTRAZIONE_CORI set
     IM_LORDO=IM_LORDO+aLiqIntDett.IM_LORDO,
     IMPONIBILE=IMPONIBILE+aLiqIntDett.IMPONIBILE,
     IM_RITENUTA=IM_RITENUTA+aLiqIntDett.IM_RITENUTA,
     DT_INIZIO_COMPENSO = aCnrEstr.DT_INIZIO_COMPENSO,
     DT_FINE_COMPENSO = aCnrEstr.DT_FINE_COMPENSO
  where
         CD_CDS=aCnrEstr.CD_CDS
     and ESERCIZIO=aCnrEstr.ESERCIZIO
     and CD_UNITA_ORGANIZZATIVA=aCnrEstr.CD_UNITA_ORGANIZZATIVA
     and PG_LIQUIDAZIONE=aCnrEstr.PG_LIQUIDAZIONE
     and MATRICOLA=aCnrEstr.MATRICOLA
     and CODICE_FISCALE=aCnrEstr.CODICE_FISCALE
     and TI_PAGAMENTO=aCnrEstr.TI_PAGAMENTO
     and ESERCIZIO_COMPENSO=aCnrEstr.ESERCIZIO_COMPENSO
     and CD_IMPONIBILE=aCnrEstr.CD_IMPONIBILE
     and TI_ENTE_PERCIPIENTE=aCnrEstr.TI_ENTE_PERCIPIENTE
     and CD_CONTRIBUTO_RITENUTA=aCnrEstr.CD_CONTRIBUTO_RITENUTA;
 end;

 -- Processa il CORI per l'estrazione
 procedure processaCori(aLiquid liquid_cori%rowtype, aCori contributo_ritenuta%rowtype,aTSNow date,aUser varchar2) is
  aComp compenso%rowtype;
  aIdMatr number(8);
  aTratt tipo_trattamento%rowtype;
  aCdImp varchar2(2);
  aCnrEstr CNR_ESTRAZIONE_CORI%rowtype;
  DT_IN_COMP DATE;
  DT_FI_COMP DATE;
 begin
     Begin
 	select * into aComp
 	from compenso c
 	where ESERCIZIO=aCori.esercizio
        and CD_CDS=aCori.cd_cds
        and CD_UNITA_ORGANIZZATIVA=aCori.cd_unita_organizzativa
        and pg_compenso=aCori.pg_compenso
	and fl_compenso_stipendi = 'N'
	--AND CD_TERZO Not In (91165,91376)    -- per eliminare nel 2006 due casi errati
	--AND CD_TERZO Not In (91165)    -- per eliminare nel 2007 un caso errato
	--and cd_trattamento != 'T144'  -- invece di escludere i terzi escludiamo il trattamento
	and exists (Select 1 From tipo_rapporto
	            Where cd_tipo_rapporto = c.cd_tipo_rapporto
		      And ti_dipendente_altro = 'D' -- Rapporti dipendenza
	   	   )
 	for update nowait;

  	select * into aTratt
  	from tipo_trattamento
  	where cd_trattamento=aComp.cd_trattamento
        and dt_ini_validita<=aComp.dt_registrazione
        and dt_fin_validita>=aComp.dt_registrazione
        And (fl_stralcio_dip Is Null Or fl_stralcio_dip = 'Y')
  	for update nowait;

 	for aTipoCori in (Select * From tipo_contributo_ritenuta t
 	                  Where t.cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
 		            And t.dt_ini_validita = aCori.dt_ini_validita)
 	loop
	    If aCori.cd_contributo_ritenuta in('DS','DI') Then
		 aCdImp:='A1';
	    Elsif aCori.cd_contributo_ritenuta like '%ADD' Then
		 aCdImp:='I1';
            Elsif aTipoCori.cd_classificazione_cori = 'PR' then
 	         aCdImp:='01';
            Elsif aTipoCori.cd_classificazione_cori = 'FI' then
 	       If aTratt.fl_tassazione_separata  = 'Y' then
		  If aComp.FL_COMPENSO_MINICARRIERA='Y' and aComp.FL_COMPENSO_MCARRIERA_TASSEP = 'Y'
		  -- fix errore 715
		  --   or aComp.FL_COMPENSO_MINICARRIERA='N'
		  Then
 		     aCdImp:='0F';
 		  Else
 		     aCdImp:='MF';
		  End If;
	       Else
 		  aCdImp:='MF';
 	       End If;
 	    Elsif aTipoCori.cd_classificazione_cori = 'IP' then
 	       aCdImp:='06';
            Else
 	       aCdImp:=':1';
            End If;
 	    Exit;
 	end loop;

	begin
   	    select distinct matricola_dipendente into aIdMatr
   	    from terzo t, rapporto r
   	    Where t.cd_terzo = aComp.cd_terzo
 	      and t.cd_anag = r.cd_anag
 	      and r.matricola_dipendente is not null;
        exception
           when NO_DATA_FOUND then
        	IBMERR001.RAISE_ERR_GENERICO('Matricola non trovata per terzo:'||aComp.cd_terzo);
           When Too_Many_Rows Then
                IBMERR001.RAISE_ERR_GENERICO('Matricola doppia per terzo:'||aComp.cd_terzo);
	end;

        aCnrEstr.CD_CDS:=aLiquid.cd_cds;
        aCnrEstr.ESERCIZIO:=aLiquid.esercizio;
        aCnrEstr.CD_UNITA_ORGANIZZATIVA:=aLiquid.cd_unita_organizzativa;
        aCnrEstr.PG_LIQUIDAZIONE:=aLiquid.pg_liquidazione;
        aCnrEstr.MATRICOLA:=aIdMatr;
        aCnrEstr.CODICE_FISCALE:=aComp.codice_fiscale;

        DT_IN_COMP:=aComp.dt_da_competenza_coge;
        DT_FI_COMP:=aComp.dt_a_competenza_coge;

        If aComp.FL_COMPENSO_MINICARRIERA='Y' And aComp.FL_COMPENSO_MCARRIERA_TASSEP = 'Y' Then
           --per la tassazione separata non distinguiamo anno corrente e anno precedente (anche perchè per il momento non esistono questi casi)
           aCnrEstr.TI_PAGAMENTO:=4;
           If (To_Char(DT_FI_COMP,'YYYY') > aLiquid.esercizio) then
           			aCnrEstr.ESERCIZIO_COMPENSO:=aLiquid.esercizio;
           		else
           			aCnrEstr.ESERCIZIO_COMPENSO:=To_Char(DT_FI_COMP,'YYYY');
           	end if;		
        Else
           --se la competenza del compenso è di anni precedenti
           If To_Char(DT_IN_COMP,'YYYY') < aLiquid.esercizio /*And
              To_Char(DT_FI_COMP,'YYYY') < aLiquid.esercizio*/ Then
              aCnrEstr.TI_PAGAMENTO:=5;
              aCnrEstr.ESERCIZIO_COMPENSO:=To_Char(DT_IN_COMP,'YYYY');
           Else
              --anche se la competenza è a cavallo tra anno precedente e anno corrente, la considero Anno Corrente
	      --il ti_pagamento deve essere sempre 1
              --If aComp.pg_missione is null Then
                 --aCnrEstr.TI_PAGAMENTO:=2;
              --Else
                 aCnrEstr.TI_PAGAMENTO:=1;
              --End If;
              If (To_Char(DT_FI_COMP,'YYYY') > aLiquid.esercizio) then
           			aCnrEstr.ESERCIZIO_COMPENSO:=aLiquid.esercizio;
           		else
           			aCnrEstr.ESERCIZIO_COMPENSO:=To_Char(DT_FI_COMP,'YYYY');
           	end if;		
           End If;
  	End if;

	/*Eliminato perchè gestite le date di competenza 23/11/2006*/
	/*
	-- Modifica per richiesta 712 12/12/2003
	if aComp.cd_trattamento in ('T092', 'T093', 'T094','T095') then
  	    aCnrEstr.ESERCIZIO_COMPENSO:=aCori.esercizio-1;
        else
  	    aCnrEstr.ESERCIZIO_COMPENSO:=aCori.esercizio;
        end if;
	*/

  	aCnrEstr.CD_IMPONIBILE:=aCdImp;
        aCnrEstr.TI_ENTE_PERCIPIENTE:=aCori.ti_ente_percipiente;
        aCnrEstr.CD_CONTRIBUTO_RITENUTA:=aCori.cd_contributo_ritenuta;

  	Begin
  	    getEstrazione(aCnrEstr);
  	    --prima dell'update devo valorizzare correttamente le date
  	    If aComp.dt_da_competenza_coge < aCnrEstr.DT_INIZIO_COMPENSO Then
  	       aCnrEstr.DT_INIZIO_COMPENSO:=aComp.dt_da_competenza_coge;
  	    End If;
  	    If aComp.dt_a_competenza_coge > aCnrEstr.DT_FINE_COMPENSO Then
               aCnrEstr.DT_FINE_COMPENSO:=aComp.dt_a_competenza_coge;
            End If;
            updateEstrazione(aCnrEstr, aComp, aCori);
  	Exception when NO_DATA_FOUND then
            aCnrEstr.DT_INIZIO:=aLiquid.dt_da;
            aCnrEstr.DT_FINE:=aLiquid.dt_a;
            aCnrEstr.IM_LORDO:=aComp.im_lordo_percipiente;
            aCnrEstr.IMPONIBILE:=aCori.imponibile;
            aCnrEstr.IM_RITENUTA:=aCori.ammontare;
            aCnrEstr.DT_SCARICO_VERSO_STIPENDI:=null;
            aCnrEstr.UTCR:=aUser;
            aCnrEstr.DACR:=aTSNow;
            aCnrEstr.UTUV:=aUser;
            aCnrEstr.DUVA:=aTSNow;
            aCnrEstr.PG_VER_REC:=1;
            aCnrEstr.DT_INIZIO_COMPENSO:=aComp.dt_da_competenza_coge;
            aCnrEstr.DT_FINE_COMPENSO:=aComp.dt_a_competenza_coge;
            ins_CNR_ESTRAZIONE_CORI (aCnrEstr);
        End;
     Exception when NO_DATA_FOUND then
       null; -- COMPENSO NON DI TIPO DIP (compenso non dipendenti)
     End;
 end;

procedure processaCoriMensile(aLiquid liquid_cori%rowtype, aMese NUMBER, aCori contributo_ritenuta%rowtype,aTSNow date,aUser varchar2) is
  aComp compenso%rowtype;
  aIdMatr number(8);
  aTratt tipo_trattamento%rowtype;
  aCdImp varchar2(2);
  aCnrEstr CNR_ESTRAZIONE_CORI%rowtype;
  DT_IN_COMP DATE;
  DT_FI_COMP DATE;
 begin
     Begin
 	select * into aComp
 	from compenso c
 	where ESERCIZIO=aCori.esercizio
        and CD_CDS=aCori.cd_cds
        and CD_UNITA_ORGANIZZATIVA=aCori.cd_unita_organizzativa
        and pg_compenso=aCori.pg_compenso
	and fl_compenso_stipendi = 'N'
	--AND CD_TERZO Not In (91165,91376)    -- per eliminare nel 2006 due casi errati
	--AND CD_TERZO Not In (91165)    -- per eliminare nel 2007 un caso errato
	--and cd_trattamento != 'T144'  -- invece di escludere i terzi escludiamo il trattamento
	and exists (Select 1 From tipo_rapporto
	            Where cd_tipo_rapporto = c.cd_tipo_rapporto
		      And ti_dipendente_altro = 'D' -- Rapporti dipendenza
	   	   )
 	for update nowait;

  	select * into aTratt
  	from tipo_trattamento
  	where cd_trattamento=aComp.cd_trattamento
        and dt_ini_validita<=aComp.dt_registrazione
        and dt_fin_validita>=aComp.dt_registrazione
        And (fl_stralcio_dip Is Null Or fl_stralcio_dip = 'Y')
  	for update nowait;

 	for aTipoCori in (Select * From tipo_contributo_ritenuta t
 	                  Where t.cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
 		            And t.dt_ini_validita = aCori.dt_ini_validita)
 	loop
	    If aCori.cd_contributo_ritenuta in('DS','DI') Then
		 aCdImp:='A1';
	    Elsif aCori.cd_contributo_ritenuta like '%ADD' Then
		 aCdImp:='I1';
            Elsif aTipoCori.cd_classificazione_cori = 'PR' then
 	         aCdImp:='01';
            Elsif aTipoCori.cd_classificazione_cori = 'FI' then
 	       If aTratt.fl_tassazione_separata  = 'Y' then
		  If aComp.FL_COMPENSO_MINICARRIERA='Y' and aComp.FL_COMPENSO_MCARRIERA_TASSEP = 'Y'
		  -- fix errore 715
		  --   or aComp.FL_COMPENSO_MINICARRIERA='N'
		  Then
 		     aCdImp:='0F';
 		  Else
 		     aCdImp:='MF';
		  End If;
	       Else
 		  aCdImp:='MF';
 	       End If;
 	    Elsif aTipoCori.cd_classificazione_cori = 'IP' then
 	       aCdImp:='06';
            Else
 	       aCdImp:=':1';
            End If;
 	    Exit;
 	end loop;

	begin
   	    select distinct matricola_dipendente into aIdMatr
   	    from terzo t, rapporto r
   	    Where t.cd_terzo = aComp.cd_terzo
 	      and t.cd_anag = r.cd_anag
 	      and r.matricola_dipendente is not null;
        exception
           when NO_DATA_FOUND then
        	IBMERR001.RAISE_ERR_GENERICO('Matricola non trovata per terzo:'||aComp.cd_terzo);
           When Too_Many_Rows Then
                IBMERR001.RAISE_ERR_GENERICO('Matricola doppia per terzo:'||aComp.cd_terzo);
	end;

        aCnrEstr.CD_CDS:=aLiquid.cd_cds;
        aCnrEstr.ESERCIZIO:=aLiquid.esercizio;
        aCnrEstr.CD_UNITA_ORGANIZZATIVA:=aLiquid.cd_unita_organizzativa;
        aCnrEstr.PG_LIQUIDAZIONE:=aLiquid.pg_liquidazione;
        aCnrEstr.MATRICOLA:=aIdMatr;
        aCnrEstr.CODICE_FISCALE:=aComp.codice_fiscale;
				aCnrEstr.MESE:=aMese;

        DT_IN_COMP:=aComp.dt_da_competenza_coge;
        DT_FI_COMP:=aComp.dt_a_competenza_coge;

        If aComp.FL_COMPENSO_MINICARRIERA='Y' And aComp.FL_COMPENSO_MCARRIERA_TASSEP = 'Y' Then
           --per la tassazione separata non distinguiamo anno corrente e anno precedente (anche perchè per il momento non esistono questi casi)
           aCnrEstr.TI_PAGAMENTO:=4;
           if (To_Char(DT_FI_COMP,'YYYY')> aComp.esercizio) then			
              	aCnrEstr.ESERCIZIO_COMPENSO:=aComp.esercizio;
              else
              	aCnrEstr.ESERCIZIO_COMPENSO:=To_Char(DT_FI_COMP,'YYYY');
            end if;	
        Else
           --se la competenza del compenso è di anni precedenti
           If To_Char(DT_IN_COMP,'YYYY') < aLiquid.esercizio /*And
              To_Char(DT_FI_COMP,'YYYY') < aLiquid.esercizio*/ Then
              aCnrEstr.TI_PAGAMENTO:=5;
              aCnrEstr.ESERCIZIO_COMPENSO:=To_Char(DT_IN_COMP,'YYYY');
           Else
              --anche se la competenza è a cavallo tra anno precedente e anno corrente, la considero Anno Corrente
	      --il ti_pagamento deve essere sempre 1
              --If aComp.pg_missione is null Then
                 --aCnrEstr.TI_PAGAMENTO:=2;
              --Else
                 aCnrEstr.TI_PAGAMENTO:=1;
              --End If;
              if (To_Char(DT_FI_COMP,'YYYY')> aComp.esercizio) then			
              	aCnrEstr.ESERCIZIO_COMPENSO:=aComp.esercizio;
              else
              	aCnrEstr.ESERCIZIO_COMPENSO:=To_Char(DT_FI_COMP,'YYYY');
              end if;	
           End If;
  	End if;

	/*Eliminato perchè gestite le date di competenza 23/11/2006*/
	/*
	-- Modifica per richiesta 712 12/12/2003
	if aComp.cd_trattamento in ('T092', 'T093', 'T094','T095') then
  	    aCnrEstr.ESERCIZIO_COMPENSO:=aCori.esercizio-1;
        else
  	    aCnrEstr.ESERCIZIO_COMPENSO:=aCori.esercizio;
        end if;
	*/

  	aCnrEstr.CD_IMPONIBILE:=aCdImp;
        aCnrEstr.TI_ENTE_PERCIPIENTE:=aCori.ti_ente_percipiente;
        aCnrEstr.CD_CONTRIBUTO_RITENUTA:=aCori.cd_contributo_ritenuta;

  	Begin
  	    getEstrazione(aCnrEstr);
  	    --prima dell'update devo valorizzare correttamente le date
  	    If aComp.dt_da_competenza_coge < aCnrEstr.DT_INIZIO_COMPENSO Then
  	       aCnrEstr.DT_INIZIO_COMPENSO:=aComp.dt_da_competenza_coge;
  	    End If;
  	    If aComp.dt_a_competenza_coge > aCnrEstr.DT_FINE_COMPENSO Then
               aCnrEstr.DT_FINE_COMPENSO:=aComp.dt_a_competenza_coge;
            End If;
            updateEstrazione(aCnrEstr, aComp, aCori);
  	Exception when NO_DATA_FOUND then
            aCnrEstr.DT_INIZIO:=aLiquid.dt_da;
            aCnrEstr.DT_FINE:=aLiquid.dt_a;
            aCnrEstr.IM_LORDO:=aComp.im_lordo_percipiente;
            aCnrEstr.IMPONIBILE:=aCori.imponibile;
            aCnrEstr.IM_RITENUTA:=aCori.ammontare;
            aCnrEstr.IM_CORI_SOSPESO:=aCori.im_cori_sospeso;
            aCnrEstr.DT_SCARICO_VERSO_STIPENDI:=null;
            aCnrEstr.UTCR:=aUser;
            aCnrEstr.DACR:=aTSNow;
            aCnrEstr.UTUV:=aUser;
            aCnrEstr.DUVA:=aTSNow;
            aCnrEstr.PG_VER_REC:=1;
            aCnrEstr.DT_INIZIO_COMPENSO:=aComp.dt_da_competenza_coge;
            aCnrEstr.DT_FINE_COMPENSO:=aComp.dt_a_competenza_coge;
            ins_CNR_ESTRAZIONE_CORI (aCnrEstr);
        End;
     Exception when NO_DATA_FOUND then
       null; -- COMPENSO NON DI TIPO DIP (compenso non dipendenti) oppure trattamento non valido
     End;
 end;

 procedure aggregaDettLiquid(aEs number, aTSNow date,aUser varchar2) is
  aAgg CNR_ESTRAZIONE_CORI_AGG%rowtype;
  aPgRiga number;
  aOldAgg CNR_ESTRAZIONE_CORI%rowtype;
 begin
  lock table CNR_ESTRAZIONE_CORI in exclusive mode nowait;
  aPgRiga:=0;
  aOldAgg:=null;
  for aTAgg in (select
                ESERCIZIO,
                MATRICOLA,
                'XXX' CODICE_FISCALE,
		TI_PAGAMENTO,
                ESERCIZIO_COMPENSO,
                CD_IMPONIBILE,
                TI_ENTE_PERCIPIENTE,
                CD_CONTRIBUTO_RITENUTA,
                sum(IM_LORDO) im_lordo,
                sum(IMPONIBILE) imponibile,
                sum(IM_RITENUTA) im_ritenuta,
                Min(DT_INIZIO_COMPENSO) dt_inizio_compenso,
                Decode(ESERCIZIO_COMPENSO,ESERCIZIO,Max(DT_FINE_COMPENSO),To_Date('31/12/'||ESERCIZIO_COMPENSO,'DD/MM/YYYY')) dt_fine_compenso
                --Max(DT_FINE_COMPENSO) dt_fine_compenso
  from CNR_ESTRAZIONE_CORI
  where
        esercizio = aEs
	and DT_SCARICO_VERSO_STIPENDI is null
    group By    ESERCIZIO,
                MATRICOLA,
--              CODICE_FISCALE,
		TI_PAGAMENTO,
                ESERCIZIO_COMPENSO,
                CD_IMPONIBILE,
                TI_ENTE_PERCIPIENTE,
                CD_CONTRIBUTO_RITENUTA
    order By    ESERCIZIO,
                MATRICOLA,
--              CODICE_FISCALE,
		TI_PAGAMENTO,
                ESERCIZIO_COMPENSO,
                CD_IMPONIBILE,
                TI_ENTE_PERCIPIENTE,
                CD_CONTRIBUTO_RITENUTA
  ) loop
     if
	     aOldAgg.esercizio is null
	  or (
	        aTAgg.ESERCIZIO != aOldAgg.esercizio
          	or aTAgg.MATRICOLA != aOldAgg.matricola
          	or aTAgg.CODICE_FISCALE != aOldAgg.codice_fiscale
		or aTAgg.TI_PAGAMENTO != aOldAgg.ti_pagamento
		or aTAgg.ESERCIZIO_COMPENSO != aOldAgg.ESERCIZIO_COMPENSO
	     )
	 then
         aOldAgg.ESERCIZIO:=aTAgg.esercizio;
         aOldAgg.MATRICOLA:=aTAgg.matricola;
      	 aOldAgg.CODICE_FISCALE:=aTAgg.codice_fiscale;
      	 aOldAgg.TI_PAGAMENTO:=aTAgg.ti_pagamento;
      	 aOldAgg.ESERCIZIO_COMPENSO := aTAgg.ESERCIZIO_COMPENSO;
      	 aPgRiga:=0;
     end if;
     aPgRiga:=aPgRiga+1;
     aAgg.DT_ESTRAZIONE:=aTSNow;
     aAgg.ESERCIZIO:=aTAgg.ESERCIZIO;
     aAgg.MATRICOLA:=aTAgg.MATRICOLA;
     aAgg.CODICE_FISCALE:=aTAgg.CODICE_FISCALE;
     aAgg.TI_PAGAMENTO:=aTAgg.TI_PAGAMENTO;
     aAgg.ESERCIZIO_COMPENSO:=aTAgg.ESERCIZIO_COMPENSO;
     aAgg.CD_IMPONIBILE:=aTAgg.CD_IMPONIBILE;
     aAgg.TI_ENTE_PERCIPIENTE:=aTAgg.TI_ENTE_PERCIPIENTE;
     aAgg.CD_CONTRIBUTO_RITENUTA:=aTAgg.CD_CONTRIBUTO_RITENUTA;
     aAgg.IM_LORDO:=aTAgg.IM_LORDO;
     aAgg.IMPONIBILE:=aTAgg.IMPONIBILE;
     aAgg.IM_RITENUTA:=aTAgg.IM_RITENUTA;
     aAgg.pg_riga:=aPgRiga;
     aAgg.UTCR:=aUser;
     aAgg.DACR:=aTSNow;
     aAgg.UTUV:=aUser;
     aAgg.DUVA:=aTSNow;
     aAgg.PG_VER_REC:=1;
     aAgg.DT_INIZIO_COMPENSO:=aTAgg.DT_INIZIO_COMPENSO;
     aAgg.DT_FINE_COMPENSO:=aTAgg.DT_FINE_COMPENSO;
	 ins_CNR_ESTRAZIONE_CORI_AGG(aAgg);
  end loop;
  update CNR_ESTRAZIONE_CORI set
   DT_SCARICO_VERSO_STIPENDI=aTSNow,
   duva=aTSNow,
   utuv=aUser,
   pg_ver_rec=pg_ver_rec+1
  Where esercizio = aEs
    and DT_SCARICO_VERSO_STIPENDI is null;
 end;

procedure aggregaDettLiquidMensile(aEs number, aMese NUMBER, aTSNow date,aUser varchar2) is
  aAgg CNR_ESTRAZIONE_CORI_AGG%rowtype;
  aPgRiga number;
  aOldAgg CNR_ESTRAZIONE_CORI%rowtype;
 begin
  lock table CNR_ESTRAZIONE_CORI in exclusive mode nowait;
  aPgRiga:=0;
  aOldAgg:=null;
  for aTAgg in (select
                ESERCIZIO,
                MATRICOLA,
                'XXX' CODICE_FISCALE,
		TI_PAGAMENTO,
                ESERCIZIO_COMPENSO,
                CD_IMPONIBILE,
                TI_ENTE_PERCIPIENTE,
                CD_CONTRIBUTO_RITENUTA,
                sum(IM_LORDO) im_lordo,
                sum(IMPONIBILE) imponibile,
                sum(IM_RITENUTA) im_ritenuta,
                Sum(IM_CORI_SOSPESO) im_cori_sospeso,
                Min(DT_INIZIO_COMPENSO) dt_inizio_compenso,
                Decode(ESERCIZIO_COMPENSO,ESERCIZIO,Max(DT_FINE_COMPENSO),To_Date('31/12/'||ESERCIZIO_COMPENSO,'DD/MM/YYYY')) dt_fine_compenso
                --Max(DT_FINE_COMPENSO) dt_fine_compenso
  from CNR_ESTRAZIONE_CORI
  where
        esercizio = aEs
        And mese = aMese
	and DT_SCARICO_VERSO_STIPENDI is null
    group By    ESERCIZIO,
                MATRICOLA,
--              CODICE_FISCALE,
		TI_PAGAMENTO,
                ESERCIZIO_COMPENSO,
                CD_IMPONIBILE,
                TI_ENTE_PERCIPIENTE,
                CD_CONTRIBUTO_RITENUTA
    order By    ESERCIZIO,
                MATRICOLA,
--              CODICE_FISCALE,
		TI_PAGAMENTO,
                ESERCIZIO_COMPENSO,
                CD_IMPONIBILE,
                TI_ENTE_PERCIPIENTE,
                CD_CONTRIBUTO_RITENUTA
  ) loop
     if
	     aOldAgg.esercizio is null
	  or (
	        aTAgg.ESERCIZIO != aOldAgg.esercizio
          	or aTAgg.MATRICOLA != aOldAgg.matricola
          	or aTAgg.CODICE_FISCALE != aOldAgg.codice_fiscale
		or aTAgg.TI_PAGAMENTO != aOldAgg.ti_pagamento
		or aTAgg.ESERCIZIO_COMPENSO != aOldAgg.ESERCIZIO_COMPENSO
	     )
	 then
         aOldAgg.ESERCIZIO:=aTAgg.esercizio;
         aOldAgg.MATRICOLA:=aTAgg.matricola;
      	 aOldAgg.CODICE_FISCALE:=aTAgg.codice_fiscale;
      	 aOldAgg.TI_PAGAMENTO:=aTAgg.ti_pagamento;
      	 aOldAgg.ESERCIZIO_COMPENSO := aTAgg.ESERCIZIO_COMPENSO;
      	 aPgRiga:=0;
     end if;
     aPgRiga:=aPgRiga+1;
     aAgg.DT_ESTRAZIONE:=aTSNow;
     aAgg.ESERCIZIO:=aTAgg.ESERCIZIO;
     aAgg.MATRICOLA:=aTAgg.MATRICOLA;
     aAgg.CODICE_FISCALE:=aTAgg.CODICE_FISCALE;
     aAgg.MESE:=aMese;
     aAgg.TI_PAGAMENTO:=aTAgg.TI_PAGAMENTO;
     aAgg.ESERCIZIO_COMPENSO:=aTAgg.ESERCIZIO_COMPENSO;
     aAgg.CD_IMPONIBILE:=aTAgg.CD_IMPONIBILE;
     aAgg.TI_ENTE_PERCIPIENTE:=aTAgg.TI_ENTE_PERCIPIENTE;
     aAgg.CD_CONTRIBUTO_RITENUTA:=aTAgg.CD_CONTRIBUTO_RITENUTA;
     aAgg.IM_LORDO:=aTAgg.IM_LORDO;
     aAgg.IMPONIBILE:=aTAgg.IMPONIBILE;
     aAgg.IM_RITENUTA:=aTAgg.IM_RITENUTA;
     aAgg.IM_CORI_SOSPESO:=aTAgg.IM_CORI_SOSPESO;
     aAgg.pg_riga:=aPgRiga;
     aAgg.UTCR:=aUser;
     aAgg.DACR:=aTSNow;
     aAgg.UTUV:=aUser;
     aAgg.DUVA:=aTSNow;
     aAgg.PG_VER_REC:=1;
     aAgg.DT_INIZIO_COMPENSO:=aTAgg.DT_INIZIO_COMPENSO;
     aAgg.DT_FINE_COMPENSO:=aTAgg.DT_FINE_COMPENSO;
	 ins_CNR_ESTRAZIONE_CORI_AGG(aAgg);
  end loop;
  update CNR_ESTRAZIONE_CORI set
   DT_SCARICO_VERSO_STIPENDI=aTSNow,
   duva=aTSNow,
   utuv=aUser,
   pg_ver_rec=pg_ver_rec+1
  Where esercizio = aEs
    and DT_SCARICO_VERSO_STIPENDI is null;
 end;


 procedure scaricaDettLiquid(aLiquid liquid_cori%rowtype,aTSNow date,aUser varchar2) is
  aNum number;
  aCnrEstr CNR_ESTRAZIONE_CORI%rowtype;
  aCori contributo_ritenuta%rowtype;
  dt_in_comp   DATE;
  dt_fi_comp   DATE;
 begin
  begin
   select distinct 1 into aNum from CNR_ESTRAZIONE_CORI where
        esercizio = aLiquid.esercizio
    and cd_cds = aLiquid.cd_cds
    and cd_unita_organizzativa = aLiquid.cd_unita_organizzativa
    and pg_liquidazione = aLiquid.pg_liquidazione;
   IBMERR001.RAISE_ERR_GENERICO('Estrazione già effettuata per liquidazione n.'||aLiquid.pg_liquidazione||' es.'||aLiquid.esercizio||' cds:'||aLiquid.cd_cds||' uo:'||aLiquid.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   null;
  end;

  for aLiqLoc in (select * from liquid_gruppo_cori where
       esercizio = aLiquid.esercizio
   and cd_cds = aLiquid.cd_cds
   and cd_unita_organizzativa = aLiquid.cd_unita_organizzativa
   and pg_liquidazione = aLiquid.pg_liquidazione
   and cd_cds = cd_cds_origine
   for update nowait
  ) Loop
   	for aLiqDett in (select * from liquid_gruppo_cori_det
   			 Where CD_CDS=aLiqLoc.cd_cds
    			   and ESERCIZIO=aLiqLoc.esercizio
    			   and CD_UNITA_ORGANIZZATIVA=aLiqLoc.cd_unita_organizzativa
    			   and PG_LIQUIDAZIONE=aLiqLoc.pg_liquidazione
                    	   and CD_CDS_ORIGINE=aLiqLoc.cd_cds_origine
    			   and CD_UO_ORIGINE=aLiqLoc.cd_uo_origine
    	   		   and PG_LIQUIDAZIONE_ORIGINE=aLiqLoc.pg_liquidazione_origine
    			   and CD_GRUPPO_CR=aLiqLoc.cd_gruppo_cr
    			   and CD_REGIONE=aLiqLoc.cd_regione
    			   and PG_COMUNE=aLiqLoc.pg_comune
    			   and cd_contributo_ritenuta not like 'STI%'
    			 for update nowait
   	) loop
 	  	select * into aCori from contributo_ritenuta
 	  	where CD_CDS=aLiqDett.cd_cds_origine
       	 	  and CD_UNITA_ORGANIZZATIVA=aLiqDett.cd_uo_origine
       		  and ESERCIZIO=aLiqDett.esercizio_contributo_ritenuta
       		  and PG_COMPENSO=aLiqDett.pg_compenso
       		  and CD_CONTRIBUTO_RITENUTA=aLiqDett.cd_contributo_ritenuta
       		  and TI_ENTE_PERCIPIENTE=aLiqDett.ti_ente_percipiente
 	  	for update nowait;
      		processaCori(aLiquid, aCori,aTSNow,aUser);
   	  end loop;
    End loop;

  for aGruppoCentro in (select * from liquid_gruppo_centro
  			where esercizio = aLiquid.esercizio
    			  and cd_cds_lc = aLiquid.cd_cds
    			  and cd_uo_lc = aLiquid.cd_unita_organizzativa
    			  and pg_lc = aLiquid.pg_liquidazione
    			for update nowait
  ) loop
   	for aLiqLoc in (select * from liquid_gruppo_cori
   			where esercizio=aGruppoCentro.esercizio
    			  and cd_gruppo_cr=aGruppoCentro.cd_gruppo_cr
    			  and cd_regione=aGruppoCentro.cd_regione
    			  and pg_comune=aGruppoCentro.pg_comune
    			  and pg_gruppo_centro=aGruppoCentro.pg_gruppo_centro
			for update nowait
   	) loop
   	     begin
	 	select 1 into aNum
	 	from liquid_cori_interf_cds
	 	where cd_cds=aLiqLoc.cd_cds
		  and esercizio=aLiqLoc.esercizio;

	 	for aLiqIntDett in (select * from liquid_cori_interf_dett
	 			    Where CD_CDS=aLiqLoc.cd_cds
      				      and ESERCIZIO=aLiqLoc.esercizio
      				      and CD_UNITA_ORGANIZZATIVA=aLiqLoc.cd_unita_organizzativa
      				      and PG_LIQUIDAZIONE=aLiqLoc.pg_liquidazione
      				      and CD_GRUPPO_CR=aLiqLoc.cd_gruppo_cr
      				      and CD_REGIONE=aLiqLoc.cd_regione
      				      and PG_COMUNE=aLiqLoc.pg_comune
	  			      and DT_SCARICO_VERSO_STIPENDI is null
      				    for update nowait
	 	) loop
      			aCnrEstr.CD_CDS:=aLiquid.CD_CDS;
      			aCnrEstr.ESERCIZIO:=aLiquid.ESERCIZIO;
      			aCnrEstr.CD_UNITA_ORGANIZZATIVA:=aLiquid.CD_UNITA_ORGANIZZATIVA;
      			aCnrEstr.PG_LIQUIDAZIONE:=aLiquid.PG_LIQUIDAZIONE;

      			aCnrEstr.MATRICOLA:=aLiqIntDett.MATRICOLA;
      			aCnrEstr.CODICE_FISCALE:=aLiqIntDett.CODICE_FISCALE;
      			If aLiqIntDett.TI_PAGAMENTO = '4' Then   --Tassazione separata
      			    aCnrEstr.TI_PAGAMENTO:=aLiqIntDett.TI_PAGAMENTO;
      			else
      			    If aLiqIntDett.ESERCIZIO_COMPENSO = aLiquid.ESERCIZIO Then   --Anno corrente
      				If aLiqIntDett.TI_PAGAMENTO = '2' Then
      			   	   aCnrEstr.TI_PAGAMENTO:= '1';
      				Else
      			   	   aCnrEstr.TI_PAGAMENTO:=aLiqIntDett.TI_PAGAMENTO;
      				End If;
      			    Else   --Anno precedente
      				aCnrEstr.TI_PAGAMENTO:= '5';
      			    End If;
      			end If;
      			aCnrEstr.ESERCIZIO_COMPENSO:=aLiqIntDett.ESERCIZIO_COMPENSO;
      			aCnrEstr.CD_IMPONIBILE:=aLiqIntDett.CD_IMPONIBILE;
      			aCnrEstr.TI_ENTE_PERCIPIENTE:=aLiqIntDett.TI_ENTE_PERCIPIENTE;
      			aCnrEstr.CD_CONTRIBUTO_RITENUTA:=aLiqIntDett.CD_CONTRIBUTO_RITENUTA;
      			dt_in_comp:= Nvl(aLiqIntDett.DT_INIZIO_COMPENSO,To_Date('01/01/'||To_Char(aCnrEstr.ESERCIZIO_COMPENSO),'DD/MM/YYYY'));
      			dt_fi_comp:= Nvl(aLiqIntDett.DT_FINE_COMPENSO,To_Date('31/12/'||To_Char(aCnrEstr.ESERCIZIO_COMPENSO),'DD/MM/YYYY'));

	  		begin
 	   		    getEstrazione(aCnrEstr);
 	   		    --prima dell'update devo valorizzare correttamente le date
 	   		    If dt_in_comp < aCnrEstr.DT_INIZIO_COMPENSO Then
  	       			aCnrEstr.DT_INIZIO_COMPENSO:=dt_in_comp;
  	    		    End If;
  	    		    If dt_fi_comp > aCnrEstr.DT_FINE_COMPENSO Then
               			aCnrEstr.DT_FINE_COMPENSO:=dt_fi_comp;
            		    End If;
       			    updateEstrazione(aCnrEstr, aLiqIntDett);
 	  		exception
 	  		  When NO_DATA_FOUND then
       				aCnrEstr.DT_INIZIO:=aLiquid.dt_da;
       				aCnrEstr.DT_FINE:=aLiquid.dt_a;
       				aCnrEstr.IM_LORDO:=aLiqIntDett.IM_LORDO;
       				aCnrEstr.IMPONIBILE:=aLiqIntDett.IMPONIBILE;
       				aCnrEstr.IM_RITENUTA:=aLiqIntDett.IM_RITENUTA;
       				aCnrEstr.DT_SCARICO_VERSO_STIPENDI:=null;
       				aCnrEstr.UTCR:=aUser;
       				aCnrEstr.DACR:=aTSNow;
       				aCnrEstr.UTUV:=aUser;
       				aCnrEstr.DUVA:=aTSNow;
       				aCnrEstr.PG_VER_REC:=1;
       				aCnrEstr.DT_INIZIO_COMPENSO:=dt_in_comp;
       				aCnrEstr.DT_FINE_COMPENSO:=dt_fi_comp;

       				ins_CNR_ESTRAZIONE_CORI (aCnrEstr);
      			end;
	  		update liquid_cori_interf_dett
	  		set DT_SCARICO_VERSO_STIPENDI=trunc(aTSNow)
	  		where CD_CDS=aLiqIntDett.CD_CDS
       			  and ESERCIZIO=aLiqIntDett.ESERCIZIO
       			  and CD_UNITA_ORGANIZZATIVA=aLiqIntDett.CD_UNITA_ORGANIZZATIVA
       			  and PG_LIQUIDAZIONE=aLiqIntDett.PG_LIQUIDAZIONE
       			  and MATRICOLA=aLiqIntDett.MATRICOLA
       			  and CODICE_FISCALE=aLiqIntDett.CODICE_FISCALE
       			  and TI_PAGAMENTO=aLiqIntDett.TI_PAGAMENTO
       			  and ESERCIZIO_COMPENSO=aLiqIntDett.ESERCIZIO_COMPENSO
       			  and CD_IMPONIBILE=aLiqIntDett.CD_IMPONIBILE
       			  and TI_ENTE_PERCIPIENTE=aLiqIntDett.TI_ENTE_PERCIPIENTE
       			  and CD_CONTRIBUTO_RITENUTA=aLiqIntDett.CD_CONTRIBUTO_RITENUTA;
     		end loop;
	    exception when NO_DATA_FOUND Then      --non e' un cds proveniente da interfaccia
     		for aLiqDett in (select * from liquid_gruppo_cori_det
     				 Where CD_CDS=aLiqLoc.cd_cds
      				   and ESERCIZIO=aLiqLoc.esercizio
      				   and CD_UNITA_ORGANIZZATIVA=aLiqLoc.cd_unita_organizzativa
      				   and PG_LIQUIDAZIONE=aLiqLoc.pg_liquidazione
      				   and CD_CDS_ORIGINE=aLiqLoc.cd_cds_origine
      				   and CD_UO_ORIGINE=aLiqLoc.cd_uo_origine
      				   and PG_LIQUIDAZIONE_ORIGINE=aLiqLoc.pg_liquidazione_origine
      				   and CD_GRUPPO_CR=aLiqLoc.cd_gruppo_cr
      				   and CD_REGIONE=aLiqLoc.cd_regione
      				   and PG_COMUNE=aLiqLoc.pg_comune
      				   and cd_contributo_ritenuta not like 'STI%'
      				 for update nowait
	 	) loop
 	  		select * into aCori from contributo_ritenuta
 	  		where CD_CDS=aLiqDett.cd_cds
       			  and CD_UNITA_ORGANIZZATIVA=aLiqDett.cd_unita_organizzativa
       			  and ESERCIZIO=aLiqDett.esercizio_contributo_ritenuta
       			  and PG_COMPENSO=aLiqDett.pg_compenso
       			  and CD_CONTRIBUTO_RITENUTA=aLiqDett.cd_contributo_ritenuta
       			  and TI_ENTE_PERCIPIENTE=aLiqDett.ti_ente_percipiente
 	  		for update nowait;
      			processaCori(aLiquid,aCori,aTSNow,aUser);
	 	end loop;
    	    end; -- Fine gestione NON da interfaccia
	end loop;
   end loop;
 end;

 procedure scaricaDettLiquidMensile(aLiquid liquid_cori%rowtype, aMese NUMBER,aTSNow date,aUser varchar2) is
  aNum number;
  aCnrEstr CNR_ESTRAZIONE_CORI%rowtype;
  aCori contributo_ritenuta%rowtype;
  dt_in_comp   DATE;
  dt_fi_comp   DATE;
 begin
  begin
   select distinct 1 into aNum from CNR_ESTRAZIONE_CORI where
        esercizio = aLiquid.esercizio
    and cd_cds = aLiquid.cd_cds
    and cd_unita_organizzativa = aLiquid.cd_unita_organizzativa
    and pg_liquidazione = aLiquid.pg_liquidazione;
   IBMERR001.RAISE_ERR_GENERICO('Estrazione già effettuata per liquidazione n.'||aLiquid.pg_liquidazione||' es.'||aLiquid.esercizio||' cds:'||aLiquid.cd_cds||' uo:'||aLiquid.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   null;
  end;

  for aLiqLoc in (select * from liquid_gruppo_cori where
       esercizio = aLiquid.esercizio
   and cd_cds = aLiquid.cd_cds
   and cd_unita_organizzativa = aLiquid.cd_unita_organizzativa
   and pg_liquidazione = aLiquid.pg_liquidazione
   --and cd_cds = cd_cds_origine
   and (
        (cd_cds = cnrctb020.getcdCDSSACValido(aLiquid.esercizio) and cd_cds = cd_cds_origine)
        or
        (cd_cds = cnrctb020.getCDCDSENTE(aLiquid.esercizio) and cd_cds != cd_cds_origine)
        )
   for update nowait
  ) Loop
   	for aLiqDett in (select * from liquid_gruppo_cori_det
   			 Where CD_CDS=aLiqLoc.cd_cds
    			   and ESERCIZIO=aLiqLoc.esercizio
    			   and CD_UNITA_ORGANIZZATIVA=aLiqLoc.cd_unita_organizzativa
    			   and PG_LIQUIDAZIONE=aLiqLoc.pg_liquidazione
                    	   and CD_CDS_ORIGINE=aLiqLoc.cd_cds_origine
    			   and CD_UO_ORIGINE=aLiqLoc.cd_uo_origine
    	   		   and PG_LIQUIDAZIONE_ORIGINE=aLiqLoc.pg_liquidazione_origine
    			   and CD_GRUPPO_CR=aLiqLoc.cd_gruppo_cr
    			   and CD_REGIONE=aLiqLoc.cd_regione
    			   and PG_COMUNE=aLiqLoc.pg_comune
    			   and cd_contributo_ritenuta not like 'STI%'
    			 for update nowait
   	) loop
 	  	select * into aCori from contributo_ritenuta
 	  	where CD_CDS=aLiqDett.cd_cds_origine
       	 	  and CD_UNITA_ORGANIZZATIVA=aLiqDett.cd_uo_origine
       		  and ESERCIZIO=aLiqDett.esercizio_contributo_ritenuta
       		  and PG_COMPENSO=aLiqDett.pg_compenso
       		  and CD_CONTRIBUTO_RITENUTA=aLiqDett.cd_contributo_ritenuta
       		  and TI_ENTE_PERCIPIENTE=aLiqDett.ti_ente_percipiente
 	  	for update nowait;
      		processaCoriMensile(aLiquid, aMese, aCori,aTSNow,aUser);
   	  end loop;
    End loop;

  for aGruppoCentro in (select * from liquid_gruppo_centro
  			where esercizio = aLiquid.esercizio
    			  and cd_cds_lc = aLiquid.cd_cds
    			  and cd_uo_lc = aLiquid.cd_unita_organizzativa
    			  and pg_lc = aLiquid.pg_liquidazione
    			for update nowait
  ) loop
   	for aLiqLoc in (select * from liquid_gruppo_cori
   			where esercizio=aGruppoCentro.esercizio
    			  and cd_gruppo_cr=aGruppoCentro.cd_gruppo_cr
    			  and cd_regione=aGruppoCentro.cd_regione
    			  and pg_comune=aGruppoCentro.pg_comune
    			  and pg_gruppo_centro=aGruppoCentro.pg_gruppo_centro
			for update nowait
   	) loop
   	     begin
	 	select 1 into aNum
	 	from liquid_cori_interf_cds
	 	where cd_cds=aLiqLoc.cd_cds
		  and esercizio=aLiqLoc.esercizio;

	 	for aLiqIntDett in (select * from liquid_cori_interf_dett
	 			    Where CD_CDS=aLiqLoc.cd_cds
      				      and ESERCIZIO=aLiqLoc.esercizio
      				      and CD_UNITA_ORGANIZZATIVA=aLiqLoc.cd_unita_organizzativa
      				      and PG_LIQUIDAZIONE=aLiqLoc.pg_liquidazione
      				      and CD_GRUPPO_CR=aLiqLoc.cd_gruppo_cr
      				      and CD_REGIONE=aLiqLoc.cd_regione
      				      and PG_COMUNE=aLiqLoc.pg_comune
	  			      and DT_SCARICO_VERSO_STIPENDI is null
      				    for update nowait
	 	) loop
      			aCnrEstr.CD_CDS:=aLiquid.CD_CDS;
      			aCnrEstr.ESERCIZIO:=aLiquid.ESERCIZIO;
      			aCnrEstr.CD_UNITA_ORGANIZZATIVA:=aLiquid.CD_UNITA_ORGANIZZATIVA;
      			aCnrEstr.PG_LIQUIDAZIONE:=aLiquid.PG_LIQUIDAZIONE;

      			aCnrEstr.MATRICOLA:=aLiqIntDett.MATRICOLA;
      			aCnrEstr.CODICE_FISCALE:=aLiqIntDett.CODICE_FISCALE;
      			aCnrEstr.MESE:=aMese;

      			If aLiqIntDett.TI_PAGAMENTO = '4' Then   --Tassazione separata
      			    aCnrEstr.TI_PAGAMENTO:=aLiqIntDett.TI_PAGAMENTO;
      			else
      			    If aLiqIntDett.ESERCIZIO_COMPENSO = aLiquid.ESERCIZIO Then   --Anno corrente
      				If aLiqIntDett.TI_PAGAMENTO = '2' Then
      			   	   aCnrEstr.TI_PAGAMENTO:= '1';
      				Else
      			   	   aCnrEstr.TI_PAGAMENTO:=aLiqIntDett.TI_PAGAMENTO;
      				End If;
      			    Else   --Anno precedente
      				aCnrEstr.TI_PAGAMENTO:= '5';
      			    End If;
      			end If;
      			aCnrEstr.ESERCIZIO_COMPENSO:=aLiqIntDett.ESERCIZIO_COMPENSO;
      			aCnrEstr.CD_IMPONIBILE:=aLiqIntDett.CD_IMPONIBILE;
      			aCnrEstr.TI_ENTE_PERCIPIENTE:=aLiqIntDett.TI_ENTE_PERCIPIENTE;
      			aCnrEstr.CD_CONTRIBUTO_RITENUTA:=aLiqIntDett.CD_CONTRIBUTO_RITENUTA;
      			dt_in_comp:= Nvl(aLiqIntDett.DT_INIZIO_COMPENSO,To_Date('01/01/'||To_Char(aCnrEstr.ESERCIZIO_COMPENSO),'DD/MM/YYYY'));
      			dt_fi_comp:= Nvl(aLiqIntDett.DT_FINE_COMPENSO,To_Date('31/12/'||To_Char(aCnrEstr.ESERCIZIO_COMPENSO),'DD/MM/YYYY'));

	  		begin
 	   		    getEstrazione(aCnrEstr);
 	   		    --prima dell'update devo valorizzare correttamente le date
 	   		    If dt_in_comp < aCnrEstr.DT_INIZIO_COMPENSO Then
  	       			aCnrEstr.DT_INIZIO_COMPENSO:=dt_in_comp;
  	    		    End If;
  	    		    If dt_fi_comp > aCnrEstr.DT_FINE_COMPENSO Then
               			aCnrEstr.DT_FINE_COMPENSO:=dt_fi_comp;
            		    End If;
       			    updateEstrazione(aCnrEstr, aLiqIntDett);
 	  		exception
 	  		  When NO_DATA_FOUND then
       				aCnrEstr.DT_INIZIO:=aLiquid.dt_da;
       				aCnrEstr.DT_FINE:=aLiquid.dt_a;
       				aCnrEstr.IM_LORDO:=aLiqIntDett.IM_LORDO;
       				aCnrEstr.IMPONIBILE:=aLiqIntDett.IMPONIBILE;
       				aCnrEstr.IM_RITENUTA:=aLiqIntDett.IM_RITENUTA;
       				aCnrEstr.DT_SCARICO_VERSO_STIPENDI:=null;
       				aCnrEstr.UTCR:=aUser;
       				aCnrEstr.DACR:=aTSNow;
       				aCnrEstr.UTUV:=aUser;
       				aCnrEstr.DUVA:=aTSNow;
       				aCnrEstr.PG_VER_REC:=1;
       				aCnrEstr.DT_INIZIO_COMPENSO:=dt_in_comp;
       				aCnrEstr.DT_FINE_COMPENSO:=dt_fi_comp;

       				ins_CNR_ESTRAZIONE_CORI (aCnrEstr);
      			end;
	  		update liquid_cori_interf_dett
	  		set DT_SCARICO_VERSO_STIPENDI=trunc(aTSNow)
	  		where CD_CDS=aLiqIntDett.CD_CDS
       			  and ESERCIZIO=aLiqIntDett.ESERCIZIO
       			  and CD_UNITA_ORGANIZZATIVA=aLiqIntDett.CD_UNITA_ORGANIZZATIVA
       			  and PG_LIQUIDAZIONE=aLiqIntDett.PG_LIQUIDAZIONE
       			  and MATRICOLA=aLiqIntDett.MATRICOLA
       			  and CODICE_FISCALE=aLiqIntDett.CODICE_FISCALE
       			  and TI_PAGAMENTO=aLiqIntDett.TI_PAGAMENTO
       			  and ESERCIZIO_COMPENSO=aLiqIntDett.ESERCIZIO_COMPENSO
       			  and CD_IMPONIBILE=aLiqIntDett.CD_IMPONIBILE
       			  and TI_ENTE_PERCIPIENTE=aLiqIntDett.TI_ENTE_PERCIPIENTE
       			  and CD_CONTRIBUTO_RITENUTA=aLiqIntDett.CD_CONTRIBUTO_RITENUTA;
     		end loop;
	    exception when NO_DATA_FOUND Then      --non e' un cds proveniente da interfaccia
     		for aLiqDett in (select * from liquid_gruppo_cori_det
     				 Where CD_CDS=aLiqLoc.cd_cds
      				   and ESERCIZIO=aLiqLoc.esercizio
      				   and CD_UNITA_ORGANIZZATIVA=aLiqLoc.cd_unita_organizzativa
      				   and PG_LIQUIDAZIONE=aLiqLoc.pg_liquidazione
      				   and CD_CDS_ORIGINE=aLiqLoc.cd_cds_origine
      				   and CD_UO_ORIGINE=aLiqLoc.cd_uo_origine
      				   and PG_LIQUIDAZIONE_ORIGINE=aLiqLoc.pg_liquidazione_origine
      				   and CD_GRUPPO_CR=aLiqLoc.cd_gruppo_cr
      				   and CD_REGIONE=aLiqLoc.cd_regione
      				   and PG_COMUNE=aLiqLoc.pg_comune
      				   and cd_contributo_ritenuta not like 'STI%'
      				 for update nowait
	 	) loop
 	  		select * into aCori from contributo_ritenuta
 	  		where CD_CDS=aLiqDett.cd_cds
       			  and CD_UNITA_ORGANIZZATIVA=aLiqDett.cd_unita_organizzativa
       			  and ESERCIZIO=aLiqDett.esercizio_contributo_ritenuta
       			  and PG_COMPENSO=aLiqDett.pg_compenso
       			  and CD_CONTRIBUTO_RITENUTA=aLiqDett.cd_contributo_ritenuta
       			  and TI_ENTE_PERCIPIENTE=aLiqDett.ti_ente_percipiente
 	  		for update nowait;
      			processaCoriMensile(aLiquid,aMese,aCori,aTSNow,aUser);
	 	end loop;
    	    end; -- Fine gestione NON da interfaccia
	end loop;
   end loop;
 end;

-- Vengono presi tutti i compensi della SAC non versati +
-- da liquid_gruppo_centro tutti quelli degli istituti trasferiti alla SAC ma non versati
procedure scaricaDettNonLiquid(aLiquidFitt liquid_cori%rowtype,aTSNow date,aUser varchar2) is
  aNum number;
  aCnrEstr CNR_ESTRAZIONE_CORI%rowtype;
  aCori contributo_ritenuta%rowtype;
  isLiquidaSuInviato char(1);
  dt_in_comp   DATE;
  dt_fi_comp   DATE;
 Begin
   begin
      select distinct 1 into aNum
      from CNR_ESTRAZIONE_CORI
      Where esercizio = aLiquidFitt.esercizio
        and cd_cds = aLiquidFitt.cd_cds
        and cd_unita_organizzativa = aLiquidFitt.cd_unita_organizzativa
        And pg_liquidazione = aLiquidFitt.pg_liquidazione;
            IBMERR001.RAISE_ERR_GENERICO('Estrazione già effettuata per compensi non liquidati es.'||aLiquidFitt.esercizio);
   exception when NO_DATA_FOUND then
       null;
   end;

   isLiquidaSuInviato:=CNRCTB575.ISLIQUIDACORIINVIATO(aLiquidFitt.esercizio);

   For aCori In (Select b.*
   		 From compenso a, contributo_ritenuta b, tipo_contributo_ritenuta cr,
   		      tipo_cr_base c, gruppo_cr_det e, anagrafico f , terzo g,
     		      v_dt_manrev_comp dtm
		 Where g.cd_terzo = a.cd_terzo
 		   And a.stato_cofi = 'P'
 		   and f.cd_anag = g.cd_anag
 		   and b.cd_cds = a.cd_cds
 		   and b.esercizio = a.esercizio
 		   and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 		   and b.pg_compenso =a.pg_compenso
 		   and cr.cd_contributo_ritenuta = b.cd_contributo_ritenuta
 		   and cr.dt_ini_validita = b.dt_ini_validita
 		   and c.cd_contributo_ritenuta = b.cd_contributo_ritenuta
 		   and c.esercizio = b.esercizio
 		   and e.esercizio = c.esercizio
 		   and e.cd_gruppo_cr =c.cd_gruppo_cr
 		   -- Estrazione del corretto indice di regione/comune con cui entrare in GRUPPO_CR_DET sull base della classificazione del CORI
 		   and e.cd_regione = getCdRegioneLiquidCori(a.fl_compenso_stipendi,b.cd_contributo_ritenuta,b.ti_ente_percipiente,cr.cd_classificazione_cori, c.esercizio, c.cd_gruppo_cr, a.cd_regione_add,a.cd_regione_irap)
 		   and e.pg_comune = getPgComuneLiquidCori(cr.cd_classificazione_cori, c.esercizio, c.cd_gruppo_cr, a.pg_comune_add)
 		   and (
 		       b.esercizio_accertamento is not null
 		    or b.esercizio_obbligazione is not null
 		   )
 		   and dtm.CD_UO_COMPENSO = a.CD_UNITA_ORGANIZZATIVA
 		   and dtm.CD_CDS_COMPENSO = a.CD_CDS
 		   and dtm.ESERCIZIO_COMPENSO = a.ESERCIZIO
 		   and dtm.PG_COMPENSO = a.PG_COMPENSO
 		   And a.esercizio = aLiquidFitt.esercizio
 		   And a.cd_cds = aLiquidFitt.cd_cds                    -- prendo solo il cds '000'
  		   and dtm.esercizio_doc_cont = a.esercizio
	           And decode(isLiquidaSuInviato,'Y',To_Char(dtm.dt_trasmissione,'yyyy'),
	                                             To_Char(a.dt_emissione_mandato,'yyyy')) = aLiquidFitt.esercizio
		   And c.cd_contributo_ritenuta not like 'STI%'
		   and not exists (Select 1 from liquid_gruppo_cori_det a1
			  	   Where a1.esercizio_contributo_ritenuta = a.esercizio
				     And a1.cd_cds_origine = a.cd_cds
				     And a1.cd_uo_origine = a.cd_unita_organizzativa
				     And a1.cd_contributo_ritenuta = b.cd_contributo_ritenuta
				     And a1.pg_compenso = a.pg_compenso
				     And a1.ti_ente_percipiente = b.ti_ente_percipiente)
  ) Loop
      		processaCori(aLiquidFitt,aCori,aTSNow,aUser);
    End loop;

    for aGruppoCentro in (select * from liquid_gruppo_centro
  			where esercizio = aLiquidFitt.esercizio
  			  And stato != 'C'      --per prendere anche quelli annullati (l'importo e' stato versato con altra riga ma devo prendere il gruppo_centro)
  			  And da_esercizio_precedente = 'N'
    			  and cd_cds_lc Is Null
    			  and cd_uo_lc Is Null
    			  and pg_lc Is Null
    			for update nowait
   ) loop
   	for aLiqLoc in (select * from liquid_gruppo_cori
   			where esercizio=aGruppoCentro.esercizio
    			  and cd_gruppo_cr=aGruppoCentro.cd_gruppo_cr
    			  and cd_regione=aGruppoCentro.cd_regione
    			  and pg_comune=aGruppoCentro.pg_comune
    			  and pg_gruppo_centro=aGruppoCentro.pg_gruppo_centro
			for update nowait
   	) loop
   	     begin
	 	select 1 into aNum
	 	from liquid_cori_interf_cds
	 	where cd_cds=aLiqLoc.cd_cds
		  and esercizio=aLiqLoc.esercizio;

	 	for aLiqIntDett in (select * from liquid_cori_interf_dett
	 			    Where CD_CDS=aLiqLoc.cd_cds
      				      and ESERCIZIO=aLiqLoc.esercizio
      				      and CD_UNITA_ORGANIZZATIVA=aLiqLoc.cd_unita_organizzativa
      				      and PG_LIQUIDAZIONE=aLiqLoc.pg_liquidazione
      				      and CD_GRUPPO_CR=aLiqLoc.cd_gruppo_cr
      				      and CD_REGIONE=aLiqLoc.cd_regione
      				      and PG_COMUNE=aLiqLoc.pg_comune
	  			      and DT_SCARICO_VERSO_STIPENDI is null
      				    for update nowait
	 	) loop
      			aCnrEstr.CD_CDS:=aLiquidFitt.CD_CDS;
      			aCnrEstr.ESERCIZIO:=aLiquidFitt.ESERCIZIO;
      			aCnrEstr.CD_UNITA_ORGANIZZATIVA:=aLiquidFitt.CD_UNITA_ORGANIZZATIVA;
      			aCnrEstr.PG_LIQUIDAZIONE:=aLiquidFitt.PG_LIQUIDAZIONE;

      			aCnrEstr.MATRICOLA:=aLiqIntDett.MATRICOLA;
      			aCnrEstr.CODICE_FISCALE:=aLiqIntDett.CODICE_FISCALE;
      			If aLiqIntDett.TI_PAGAMENTO = '4' Then   --Tassazione separata
      			    aCnrEstr.TI_PAGAMENTO:=aLiqIntDett.TI_PAGAMENTO;
      			else
      			    If aLiqIntDett.ESERCIZIO_COMPENSO = aLiquidFitt.ESERCIZIO Then   --Anno corrente
      				If aLiqIntDett.TI_PAGAMENTO = '2' Then
      			   	   aCnrEstr.TI_PAGAMENTO:= '1';
      				Else
      			   	   aCnrEstr.TI_PAGAMENTO:=aLiqIntDett.TI_PAGAMENTO;
      				End If;
      			    Else   --Anno precedente
      				aCnrEstr.TI_PAGAMENTO:= '5';
      			    End If;
      			end If;
      			--aCnrEstr.TI_PAGAMENTO:=aLiqIntDett.TI_PAGAMENTO;
      			aCnrEstr.ESERCIZIO_COMPENSO:=aLiqIntDett.ESERCIZIO_COMPENSO;
      			aCnrEstr.CD_IMPONIBILE:=aLiqIntDett.CD_IMPONIBILE;
      			aCnrEstr.TI_ENTE_PERCIPIENTE:=aLiqIntDett.TI_ENTE_PERCIPIENTE;
      			aCnrEstr.CD_CONTRIBUTO_RITENUTA:=aLiqIntDett.CD_CONTRIBUTO_RITENUTA;
      			dt_in_comp:= Nvl(aLiqIntDett.DT_INIZIO_COMPENSO,To_Date('01/01/'||To_Char(aCnrEstr.ESERCIZIO_COMPENSO),'DD/MM/YYYY'));
      			dt_fi_comp:= Nvl(aLiqIntDett.DT_FINE_COMPENSO,To_Date('31/12/'||To_Char(aCnrEstr.ESERCIZIO_COMPENSO),'DD/MM/YYYY'));

	  		begin
 	   		    getEstrazione(aCnrEstr);
 	   		    --prima dell'update devo valorizzare correttamente le date
 	   		    If dt_in_comp < aCnrEstr.DT_INIZIO_COMPENSO Then
  	       			aCnrEstr.DT_INIZIO_COMPENSO:=dt_in_comp;
  	    		    End If;
  	    		    If dt_fi_comp > aCnrEstr.DT_FINE_COMPENSO Then
               			aCnrEstr.DT_FINE_COMPENSO:=dt_fi_comp;
            		    End If;
       			    updateEstrazione(aCnrEstr, aLiqIntDett);
 	  		exception
 	  		  When NO_DATA_FOUND then
       				aCnrEstr.DT_INIZIO:=aLiquidFitt.dt_da;
       				aCnrEstr.DT_FINE:=aLiquidFitt.dt_a;
       				aCnrEstr.IM_LORDO:=aLiqIntDett.IM_LORDO;
       				aCnrEstr.IMPONIBILE:=aLiqIntDett.IMPONIBILE;
       				aCnrEstr.IM_RITENUTA:=aLiqIntDett.IM_RITENUTA;
       				aCnrEstr.DT_SCARICO_VERSO_STIPENDI:=null;
       				aCnrEstr.UTCR:=aUser;
       				aCnrEstr.DACR:=aTSNow;
       				aCnrEstr.UTUV:=aUser;
       				aCnrEstr.DUVA:=aTSNow;
       				aCnrEstr.PG_VER_REC:=1;
       				aCnrEstr.DT_INIZIO_COMPENSO:=dt_in_comp;
       				aCnrEstr.DT_FINE_COMPENSO:=dt_fi_comp;

       				ins_CNR_ESTRAZIONE_CORI (aCnrEstr);
      			end;
	  		update liquid_cori_interf_dett
	  		set DT_SCARICO_VERSO_STIPENDI=trunc(aTSNow)
	  		where CD_CDS=aLiqIntDett.CD_CDS
       			  and ESERCIZIO=aLiqIntDett.ESERCIZIO
       			  and CD_UNITA_ORGANIZZATIVA=aLiqIntDett.CD_UNITA_ORGANIZZATIVA
       			  and PG_LIQUIDAZIONE=aLiqIntDett.PG_LIQUIDAZIONE
       			  and MATRICOLA=aLiqIntDett.MATRICOLA
       			  and CODICE_FISCALE=aLiqIntDett.CODICE_FISCALE
       			  and TI_PAGAMENTO=aLiqIntDett.TI_PAGAMENTO
       			  and ESERCIZIO_COMPENSO=aLiqIntDett.ESERCIZIO_COMPENSO
       			  and CD_IMPONIBILE=aLiqIntDett.CD_IMPONIBILE
       			  and TI_ENTE_PERCIPIENTE=aLiqIntDett.TI_ENTE_PERCIPIENTE
       			  and CD_CONTRIBUTO_RITENUTA=aLiqIntDett.CD_CONTRIBUTO_RITENUTA;
     		end loop;
	    exception when NO_DATA_FOUND Then      --non e' un cds proveniente da interfaccia
     		for aLiqDett in (select * from liquid_gruppo_cori_det
     				 Where CD_CDS=aLiqLoc.cd_cds
      				   and ESERCIZIO=aLiqLoc.esercizio
      				   and CD_UNITA_ORGANIZZATIVA=aLiqLoc.cd_unita_organizzativa
      				   and PG_LIQUIDAZIONE=aLiqLoc.pg_liquidazione
      				   and CD_CDS_ORIGINE=aLiqLoc.cd_cds_origine
      				   and CD_UO_ORIGINE=aLiqLoc.cd_uo_origine
      				   and PG_LIQUIDAZIONE_ORIGINE=aLiqLoc.pg_liquidazione_origine
      				   and CD_GRUPPO_CR=aLiqLoc.cd_gruppo_cr
      				   and CD_REGIONE=aLiqLoc.cd_regione
      				   and PG_COMUNE=aLiqLoc.pg_comune
      				   and cd_contributo_ritenuta not like 'STI%'
      				 for update nowait
	 	) loop
 	  		select * into aCori from contributo_ritenuta
 	  		where CD_CDS=aLiqDett.cd_cds
       			  and CD_UNITA_ORGANIZZATIVA=aLiqDett.cd_unita_organizzativa
       			  and ESERCIZIO=aLiqDett.esercizio_contributo_ritenuta
       			  and PG_COMPENSO=aLiqDett.pg_compenso
       			  and CD_CONTRIBUTO_RITENUTA=aLiqDett.cd_contributo_ritenuta
       			  and TI_ENTE_PERCIPIENTE=aLiqDett.ti_ente_percipiente
 	  		for update nowait;
      			processaCori(aLiquidFitt,aCori,aTSNow,aUser);
	 	end loop;
    	    end; -- Fine gestione NON da interfaccia
	end loop;
    end loop;

 End;

 procedure scaricaDettNoImpostaMensile(aEs NUMBER, aMese NUMBER, aTSNow date, aUser varchar2) is

  aDataInizio   DATE;
  aDataFine   DATE;
  aLiquidFitt liquid_cori%Rowtype;
  cds unita_organizzativa.cd_unita_organizzativa%Type;
  uo unita_organizzativa.cd_unita_organizzativa%Type;
 Begin
      cds:=CNRCTB020.getcdCDSSACValido(aEs);
      uo:=CNRCTB020.getUOVersCori(aEs).cd_unita_organizzativa;
      aDataInizio:=IBMUTL001.getFirstDayOfMonth(To_Date(To_Char('01'||Lpad(aMese,2,'0')||aEs),'ddmmyyyy'));
      aDataFine:=IBMUTL001.getLastDayOfMonth(To_Date(To_Char('01'||Lpad(aMese,2,'0')||aEs),'ddmmyyyy'));

      aLiquidFitt.CD_CDS:=cds;
      aLiquidFitt.ESERCIZIO:= aEs;
      aLiquidFitt.CD_UNITA_ORGANIZZATIVA:=uo;
      aLiquidFitt.PG_LIQUIDAZIONE:=To_Number('-'||aEs||Lpad(aMese,2,0));
      aLiquidFitt.DT_DA:=aDataInizio;
      aLiquidFitt.DT_A:=aDataFine;
      aLiquidFitt.STATO:='L';
      aLiquidFitt.DACR:=aTSNow;
      aLiquidFitt.UTCR:=aUser;
      aLiquidFitt.DUVA:=aTSNow;
      aLiquidFitt.UTUV:=aUser;
      aLiquidFitt.PG_VER_REC:=1;
      aLiquidFitt.DA_ESERCIZIO_PRECEDENTE:='N';

      Begin
        Insert Into LIQUID_CORI(CD_CDS,ESERCIZIO,CD_UNITA_ORGANIZZATIVA,PG_LIQUIDAZIONE,
      			      DT_DA,DT_A,STATO,DACR,UTCR,DUVA,UTUV,PG_VER_REC,DA_ESERCIZIO_PRECEDENTE)
      	  	values (aLiquidFitt.CD_CDS,
                        aLiquidFitt.ESERCIZIO,
                        aLiquidFitt.CD_UNITA_ORGANIZZATIVA,
                        aLiquidFitt.PG_LIQUIDAZIONE,
                        aLiquidFitt.DT_DA,
                        aLiquidFitt.DT_A,
                        aLiquidFitt.STATO,
                        aLiquidFitt.DACR,
                        aLiquidFitt.UTCR,
                        aLiquidFitt.DUVA,
                        aLiquidFitt.UTUV,
                        aLiquidFitt.PG_VER_REC,
                        aLiquidFitt.DA_ESERCIZIO_PRECEDENTE);
      Exception
      	when Dup_Val_On_Index Then Null;
      End;

      -- Per la SAC
      For aRecLiqCompenso In (
             Select Distinct d.cd_cds_origine, d.cd_uo_origine, d.esercizio_contributo_ritenuta, d.pg_compenso
             From cnr_estrazione_cori e, liquid_gruppo_cori g, liquid_gruppo_cori_det d
             Where e.esercizio = aEs
               And e.mese = aMese
               and g.esercizio = e.esercizio
               and g.cd_cds = e.cd_cds
               and g.cd_unita_organizzativa = e.cd_unita_organizzativa
               and g.pg_liquidazione = e.pg_liquidazione
               And g.cd_cds = g.cd_cds_origine
               and d.CD_CDS=g.cd_cds
               and d.ESERCIZIO=g.esercizio
               and d.CD_UNITA_ORGANIZZATIVA=g.cd_unita_organizzativa
               and d.PG_LIQUIDAZIONE=g.pg_liquidazione
               and d.CD_CDS_ORIGINE=g.cd_cds_origine
               and d.CD_UO_ORIGINE=g.cd_uo_origine
               and d.PG_LIQUIDAZIONE_ORIGINE=g.pg_liquidazione_origine
               and d.CD_GRUPPO_CR=g.cd_gruppo_cr
               and d.CD_REGIONE=g.cd_regione
               and d.PG_COMUNE=g.pg_comune
               and d.cd_contributo_ritenuta not like 'STI%') Loop

       	   For aCori in (
     	  	select * From contributo_ritenuta
     	  	where CD_CDS=aRecLiqCompenso.cd_cds_origine
           	 	  and CD_UNITA_ORGANIZZATIVA=aRecLiqCompenso.cd_uo_origine
           		  and ESERCIZIO=aRecLiqCompenso.esercizio_contributo_ritenuta
           		  and PG_COMPENSO=aRecLiqCompenso.pg_compenso
           		  And cd_contributo_ritenuta not like 'STI%'
           		  And ammontare = 0
           		  And imponibile != 0
           		  And Nvl(aliquota,0) != 0
     	  	for update nowait)  Loop

          	processaCoriMensile(aLiquidFitt, aMese, aCori,aTSNow,aUser);
       	   End Loop;
      End Loop;

      -- Per gli istituti
      For aRecLiqCompenso In (
             Select Distinct d.cd_cds_origine, d.cd_uo_origine, d.esercizio_contributo_ritenuta, d.pg_compenso
             From cnr_estrazione_cori e, liquid_gruppo_centro c,liquid_gruppo_cori g, liquid_gruppo_cori_det d
             Where e.esercizio = aEs
               And e.mese = aMese
               and c.esercizio = e.esercizio
               and c.cd_cds_lc = e.cd_cds
               and c.cd_uo_lc = e.cd_unita_organizzativa
               and c.pg_lc = e.pg_liquidazione
               and g.esercizio=c.esercizio
               and g.cd_gruppo_cr=c.cd_gruppo_cr
               and g.cd_regione=c.cd_regione
               and g.pg_comune=c.pg_comune
               and g.pg_gruppo_centro=c.pg_gruppo_centro
               and d.CD_CDS=g.cd_cds
               and d.ESERCIZIO=g.esercizio
               and d.CD_UNITA_ORGANIZZATIVA=g.cd_unita_organizzativa
               and d.PG_LIQUIDAZIONE=g.pg_liquidazione
               and d.CD_CDS_ORIGINE=g.cd_cds_origine
               and d.CD_UO_ORIGINE=g.cd_uo_origine
               and d.PG_LIQUIDAZIONE_ORIGINE=g.pg_liquidazione_origine
               and d.CD_GRUPPO_CR=g.cd_gruppo_cr
               and d.CD_REGIONE=g.cd_regione
               and d.PG_COMUNE=g.pg_comune
               and d.cd_contributo_ritenuta not like 'STI%') Loop
       	     For aCori in (
     	  	select * From contributo_ritenuta
     	  	where CD_CDS=aRecLiqCompenso.cd_cds_origine
           	 	  and CD_UNITA_ORGANIZZATIVA=aRecLiqCompenso.cd_uo_origine
           		  and ESERCIZIO=aRecLiqCompenso.esercizio_contributo_ritenuta
           		  and PG_COMPENSO=aRecLiqCompenso.pg_compenso
           		  And cd_contributo_ritenuta not like 'STI%'
           		  And ammontare = 0
           		  And imponibile != 0
           		  And Nvl(aliquota,0) != 0
     	  	for update nowait)  Loop

          	processaCoriMensile(aLiquidFitt, aMese, aCori,aTSNow,aUser);
       	     End Loop;
      End Loop;
 End;

 Procedure stralcioMensile(aEs number, aMese NUMBER, aUser varchar2) Is
    aLiq 		 LIQUID_CORI%ROWTYPE;
    aTSNow 		 DATE:=Sysdate;
    ultimoGiornoDelMese  DATE;
 Begin
    ultimoGiornoDelMese := ibmutl001.getLastDayOfMonth(To_Date('01'||Lpad(aMese,2,'0')||aEs||'','ddmmyyyy'));

    FOR aLiq IN (Select *
                 From LIQUID_CORI aLC
                 Where (cd_cds = cnrctb020.getcdCDSSACValido(aEs)       --'000'
                        or
                        cd_cds = cnrctb020.getCDCDSENTE(aEs))         --'999'
                   And ESERCIZIO = aEs
	                 And da_esercizio_precedente ='N'
	                 And dt_a <= ultimoGiornoDelMese
                   And Not Exists (Select 1 From CNR_ESTRAZIONE_CORI
                                   Where cd_cds = aLC.cd_cds
                                     And cd_unita_organizzativa = aLC.cd_unita_organizzativa
                                     And pg_liquidazione = aLC.pg_liquidazione
                              	     And ESERCIZIO = aLC.ESERCIZIO)
    ) LOOP
         scaricaDettLiquidMensile(aLiq,aMese,aTSNow,aUser);
    END LOOP;

    Begin
         scaricaDettNoImpostaMensile(aEs,aMese,aTSNow,aUser);
 	 aggregaDettLiquidMensile(aEs,aMese,aTSNow,aUser);
    End;
 End;

 Procedure ins_CNR_ESTRAZIONE_CORI (aDest CNR_ESTRAZIONE_CORI%rowtype) is
  begin
   insert into CNR_ESTRAZIONE_CORI (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_LIQUIDAZIONE
    ,MATRICOLA
    ,CODICE_FISCALE
    ,TI_PAGAMENTO
    ,ESERCIZIO_COMPENSO
    ,CD_IMPONIBILE
    ,TI_ENTE_PERCIPIENTE
    ,CD_CONTRIBUTO_RITENUTA
    ,DT_INIZIO
    ,DT_FINE
    ,IM_LORDO
    ,IMPONIBILE
    ,IM_RITENUTA
    ,DT_SCARICO_VERSO_STIPENDI
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,DT_INIZIO_COMPENSO
    ,DT_FINE_COMPENSO
    ,MESE
    ,IM_CORI_SOSPESO
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_LIQUIDAZIONE
    ,aDest.MATRICOLA
    ,aDest.CODICE_FISCALE
    ,aDest.TI_PAGAMENTO
    ,aDest.ESERCIZIO_COMPENSO
    ,aDest.CD_IMPONIBILE
    ,aDest.TI_ENTE_PERCIPIENTE
    ,aDest.CD_CONTRIBUTO_RITENUTA
    ,aDest.DT_INIZIO
    ,aDest.DT_FINE
    ,aDest.IM_LORDO
    ,aDest.IMPONIBILE
    ,aDest.IM_RITENUTA
    ,aDest.DT_SCARICO_VERSO_STIPENDI
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.DT_INIZIO_COMPENSO
    ,aDest.DT_FINE_COMPENSO
    ,aDest.MESE
    ,aDest.IM_CORI_SOSPESO
    );
 end;
 procedure ins_CNR_ESTRAZIONE_CORI_AGG (aDest CNR_ESTRAZIONE_CORI_AGG%rowtype) is
  begin
   insert into CNR_ESTRAZIONE_CORI_AGG (
     DT_ESTRAZIONE
    ,ESERCIZIO
    ,MATRICOLA
    ,CODICE_FISCALE
    ,TI_PAGAMENTO
    ,ESERCIZIO_COMPENSO
    ,CD_IMPONIBILE
    ,TI_ENTE_PERCIPIENTE
    ,CD_CONTRIBUTO_RITENUTA
    ,PG_RIGA
    ,IM_LORDO
    ,IMPONIBILE
    ,IM_RITENUTA
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,DT_INIZIO_COMPENSO
    ,DT_FINE_COMPENSO
    ,MESE
    ,IM_CORI_SOSPESO
   ) values (
     aDest.DT_ESTRAZIONE
    ,aDest.ESERCIZIO
    ,aDest.MATRICOLA
    ,aDest.CODICE_FISCALE
    ,aDest.TI_PAGAMENTO
    ,aDest.ESERCIZIO_COMPENSO
    ,aDest.CD_IMPONIBILE
    ,aDest.TI_ENTE_PERCIPIENTE
    ,aDest.CD_CONTRIBUTO_RITENUTA
    ,aDest.PG_RIGA
    ,aDest.IM_LORDO
    ,aDest.IMPONIBILE
    ,aDest.IM_RITENUTA
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.DT_INIZIO_COMPENSO
    ,aDest.DT_FINE_COMPENSO
    ,aDest.MESE
    ,aDest.IM_CORI_SOSPESO
    );
 end;
END;
