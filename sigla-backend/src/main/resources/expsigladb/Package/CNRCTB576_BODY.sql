--------------------------------------------------------
--  DDL for Package Body CNRCTB576
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB576" As
 Procedure ribalta(aEs liquid_gruppo_centro.esercizio%Type,
 		   aStato liquid_gruppo_centro.stato%Type,
 		   aGruppo liquid_gruppo_centro.cd_gruppo_cr%Type,
 		   aRegione liquid_gruppo_centro.cd_regione%Type,
 		   aComune liquid_gruppo_centro.pg_comune%Type,
 		   aDaEsPrec liquid_gruppo_centro.da_esercizio_precedente%Type,
 		   aUser liquid_gruppo_centro.utcr%Type) is
   aCdCds       varchar2(30);
   aCdUo        varchar2(30);
   aUOVERSACC   unita_organizzativa%rowtype;
   aDateCont    date;
   UOENTE       unita_organizzativa%rowtype;
   aCdEV        varchar2(20);
   aVoceF       voce_f%rowtype;
   contatore    NUMBER;
   conta_rib    NUMBER;
   aLGC         liquid_gruppo_centro%rowtype;
   aAcc         accertamento%rowtype;
   aAccScad     accertamento_scadenzario%rowtype;
   aAccScadVoce accertamento_scad_voce%rowtype;
   aObb         obbligazione%rowtype;
   aObbScad     obbligazione_scadenzario%rowtype;
   aObbScadVoce obbligazione_scad_voce%rowtype;
   aAssPGiro ass_obb_acr_pgiro%rowtype;
   aTSNow  DATE;
   --aUser  VARCHAR2(20);
 Begin
   aTSNow := Sysdate;
   --aUser := 'RIBALTAMENTO_PGIRO';
   For aDGruppoCentro in (
        Select * From liquid_gruppo_centro
        Where esercizio=aEs
          And stato = aStato
	  And da_esercizio_precedente = aDaEsPrec
	  And cd_gruppo_cr = aGruppo
	  And cd_regione = aRegione
	  And pg_comune = aComune
          For update nowait)
   Loop

       	  -- Verifico che lo stato sia 'I'
       	  If aStato != CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE Then
       	     IBMERR001.RAISE_ERR_GENERICO('Nell''Esercizio '||To_Char(aEs)||' il gruppo '||aDGruppoCentro.cd_gruppo_cr||'/'||aDGruppoCentro.cd_regione||'/'||aDGruppoCentro.pg_comune||' non è in stato iniziale.');
       	  End If;
       	  If aDaEsPrec != 'N' Then
       	     IBMERR001.RAISE_ERR_GENERICO('Il gruppo '||aDGruppoCentro.cd_gruppo_cr||'/'||aDGruppoCentro.cd_regione||'/'||aDGruppoCentro.pg_comune||' non può essere ribaltato.');
       	  End If;
          -- Se già esiste nel nuovo anno una riga per lo stesso gruppo con STATO = 'I'
          -- e con DA_ESERCIZIO_PRECEDENTE = 'Y' blocco il ribaltamento
          Select Count(1)
          Into contatore
          From liquid_gruppo_centro
          Where esercizio=aEs+1
            And cd_gruppo_cr = aDGruppoCentro.cd_gruppo_cr
            And cd_regione = aDGruppoCentro.cd_regione
            And pg_comune = aDGruppoCentro.pg_comune
            And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
	    And da_esercizio_precedente = 'Y';

	    If contatore > 0 Then
	       	IBMERR001.RAISE_ERR_GENERICO('Esiste già nell''Esercizio '||To_Char(aEs+1)||' il gruppo '||aDGruppoCentro.cd_gruppo_cr||'/'||aDGruppoCentro.cd_regione||'/'||aDGruppoCentro.pg_comune||' con stato Iniziale.');
	    Else
	    	--Ribalto la PGIRO di spesa di liquid_gruppo_centro nel nuovo esercizio
	 	If aDGruppoCentro.pg_obb_accentr Is Not Null Then
	 	   --verifico se la PGIRO è stata già ribaltata
	 	   Select Count(1)
	 	   Into conta_rib
	 	   From obbligazione
	 	   Where cd_cds = aDGruppoCentro.cd_cds_obb_accentr
	 	     And esercizio = aDGruppoCentro.esercizio_obb_accentr
	 	     And esercizio_originale = aDGruppoCentro.esercizio_ori_obb_accentr
	 	     And pg_obbligazione = aDGruppoCentro.pg_obb_accentr
	 	     And riportato = 'Y';

	 	   If conta_rib = 0 Then --posso ribaltare
			aLGC := creaGruppoCentro(aEs+1, aDGruppoCentro.cd_gruppo_cr, aDGruppoCentro.cd_regione, aDGruppoCentro.pg_comune, aTSNow,aUser);

			--PRENDO L'ACCERTAMENTO COLLEGATO ALL'IMPEGNO
    		        Begin
   			   select *
   			   into aAssPGiro
   			   from ASS_OBB_ACR_PGIRO
   			   Where cd_cds = aDGruppoCentro.cd_cds_obb_accentr
			     and esercizio = aDGruppoCentro.esercizio_obb_accentr
			     and esercizio_ori_obbligazione = aDGruppoCentro.esercizio_ori_obb_accentr
			     and pg_obbligazione = aDGruppoCentro.pg_obb_accentr
   			   for update nowait;
  		        Exception
    			   when NO_DATA_FOUND then
                             IBMERR001.RAISE_ERR_GENERICO('Accertamento su partita di giro non associato al relativo impegno');
  		        End;

	 	   	--aAcc.cd_cds              := aDGruppoCentro.cd_cds_obb_accentr;
	 	   	--aAcc.esercizio           := aDGruppoCentro.esercizio_obb_accentr;
	 	   	--aAcc.esercizio_originale := aDGruppoCentro.esercizio_ori_obb_accentr;
	 	   	--aAcc.pg_accertamento     := aDGruppoCentro.pg_obb_accentr;
       	 	   	aAcc.cd_cds              := aAssPGiro.cd_cds;
	 	   	aAcc.esercizio           := aAssPGiro.esercizio;
	 	   	aAcc.esercizio_originale := aAssPGiro.esercizio_ori_accertamento;
	 	   	aAcc.pg_accertamento     := aAssPGiro.pg_accertamento;

	 	   	aObb.cd_cds              := aAssPGiro.cd_cds;
	 	   	aObb.esercizio           := aAssPGiro.esercizio;
	 	   	aObb.esercizio_originale := aAssPGiro.esercizio_ori_obbligazione;
	 	   	aObb.pg_obbligazione     := aAssPGiro.pg_obbligazione;

                        -- 10.01.2008 remmata questa chiamata, e utilizzata la nuova gestione che ribalta entrambi i documenti
	 	   	--creo la PGIRO in parte spesa tronca (PGIRO di Entrata ha importo nullo)
		   	--CNRCTB046.ripPgiroCds(aAcc,aObb,aTSNow,aUser);

                        CNRCTB035.getPgiroCds(aAcc, aAccScad, aAccScadVoce, aObb, aObbScad, aObbScadVoce);

                        -- 09.01.2008 SF PARTITE DI GIRO DA ESERCIZIO PRECEDENTE
                        CNRCTB046.ripPgiroCdsEntrambe(aObb, aObbScad, aObbScadVoce, aAcc, aAccScad, aAccScadVoce, Null, Null, CNRCTB001.GESTIONE_SPESE,
                                                      aTSNow, aUser, aObb, aAcc);

		   	CNRCTB035.getPgiroCds(aObb, aObbScad, aObbScadVoce, aAcc, aAccScad, aAccScadVoce);
		   	--sulla nuova LIQUID_GRUPPO_CENTRO metto la PGIRO
		   	--e sulla vecchia cambio lo STATA in 'R'=ribaltata
		   	 Update liquid_gruppo_centro
		   	 Set cd_cds_obb_accentr = aObb.cd_cds,
		       	     esercizio_obb_accentr = aObb.esercizio,
		       	     esercizio_ori_obb_accentr = aObb.esercizio_originale,
		             pg_obb_accentr = aObb.pg_obbligazione
          	   	 Where esercizio=aEs+1
            	     	   And cd_gruppo_cr = aDGruppoCentro.cd_gruppo_cr
                     	   And cd_regione = aDGruppoCentro.cd_regione
            	     	   And pg_comune = aDGruppoCentro.pg_comune
            	     	   And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
	    	     	   And da_esercizio_precedente = 'Y';

	    	   	 Update liquid_gruppo_centro
		   	 Set stato = 'R'
          	   	 Where esercizio=aEs
            	     	   And cd_gruppo_cr = aDGruppoCentro.cd_gruppo_cr
                      	   And cd_regione = aDGruppoCentro.cd_regione
            	     	   And pg_comune = aDGruppoCentro.pg_comune
            	     	   And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
	    	     	   And da_esercizio_precedente = 'N';
	    	   Else     --conta_rib != 0 e quindi non posso ribaltare
	    	   	IBMERR001.RAISE_ERR_GENERICO('La Partita di giro '||To_Char(aDGruppoCentro.esercizio_obb_accentr)||'/'||To_Char(aDGruppoCentro.esercizio_ori_obb_accentr)||'/'||To_Char(aDGruppoCentro.pg_obb_accentr)||' risulta già ribaltata.');
	    	   End If;
	        Else    --aDGruppoCentro.pg_obb_accentr Is Null
	    	   	IBMERR001.RAISE_ERR_GENERICO('Non esiste alcuna Partita di giro da ribaltare.');
	        End If;
     	    End If;   -- fine If contatore > 0
   End loop;
 End;

 Function creaGruppoCentro(New_aEs liquid_gruppo_centro.esercizio%Type,
   			     gruppo liquid_gruppo_centro.cd_gruppo_cr%Type,
   			     regione liquid_gruppo_centro.cd_regione%Type,
   			     comune liquid_gruppo_centro.pg_comune%Type,
   			     aTSNow date,
   			     aUser varchar2) return liquid_gruppo_centro%rowtype is
  aLGC liquid_gruppo_centro%rowtype;
  aPgGruppoCentro number(10);
 begin
    Begin
      Select pg_gruppo_centro+1
      Into aPgGruppoCentro
      From liquid_gruppo_centro
      Where ESERCIZIO=New_aEs
        And CD_GRUPPO_CR=gruppo
        And CD_REGIONE=regione
        And PG_COMUNE=comune
        And pg_gruppo_centro=(select max(pg_gruppo_centro) from liquid_gruppo_centro
         Where
              ESERCIZIO=New_aEs
          And CD_GRUPPO_CR=gruppo
          And CD_REGIONE=regione
          And PG_COMUNE=comune
	   )
	 For update nowait;
    Exception when NO_DATA_FOUND then
   	aPgGruppoCentro:=1;
    End;
    aLGC.ESERCIZIO:=New_aEs;
    aLGC.CD_GRUPPO_CR:=gruppo;
    aLGC.CD_REGIONE:=regione;
    aLGC.PG_COMUNE:=comune;
    aLGC.PG_GRUPPO_CENTRO:=aPgGruppoCentro;
    aLGC.STATO:=CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE;
    aLGC.CD_CDS_LC:=null;
    aLGC.CD_UO_LC:=null;
    aLGC.PG_LC:=null;
    aLGC.DACR:=aTSNow;
    aLGC.UTCR:=aUser;
    aLGC.DUVA:=aTSNow;
    aLGC.UTUV:=aUser;
    aLGC.PG_VER_REC:=1;
    aLGC.DA_ESERCIZIO_PRECEDENTE:='Y';
    begin
     CNRCTB575.INS_LIQUID_GRUPPO_CENTRO(aLGC);
    exception when DUP_VAL_ON_INDEX then
     -- L'inserimento può dare un errore di chiave duplicata se due sessioni tentano di inserire
     -- il primo record per lo stesso gruppo di versamento: la prima inserisce, la seconda deve essere bloccata
     -- e restituire l'errore di risorsa occupata
     IBMERR001.RAISE_ERR_GENERICO('Risorsa occupata riprovare più tardi');
    end;
    return aLGC;
 end;
End;
