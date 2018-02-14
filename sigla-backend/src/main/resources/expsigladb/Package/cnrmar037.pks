CREATE OR REPLACE package CNRMAR037 as
--
-- CNRMAR037 - Package controllo/martello mandati/reversali (batch)
-- Date: 14/07/2006
-- Version: 1.15
--
-- Package per il martellamento/verifica disallineamento mandati/reversali
--
-- Dependency: IBMUTL 200/210
--
-- History:
--
-- Date: 18/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 28/01/2002
-- Version: 1.1
-- Controllo totale mandati/reversali con saldo CDS
--
-- Date: 17/04/2002
-- Version: 1.2
-- Inserimento verifica per mandati/reversali aggregati per capitolo (COMPETENZA RESIDUO)
-- Inserimento verifica per mandati/reversali aggregati per cds esercizio (COMPETENZA RESIDUO)
--
-- Date: 22/04/2002
-- Version: 1.3
-- Inserita costante per tipo gestione spesa (TIPO_SPESA='S') e tipo gestione entrat (TIPO_ENTRATA = 'E')
--
-- Date: 22/04/2002
-- Version: 1.4
-- Inserita modifica voce_f_saldi_cmp per allineare i saldi (COMMENTATA)
--
-- Date: 23/04/2002
-- Version: 1.5
-- Aggiunti dettagli informativi nel logging
--
-- Date: 24/04/2002
-- Version: 1.6
-- Introdotta funzione per codificare output della voce_f
--
-- Date: 13/05/2002
-- Version: 1.7
-- corretta query di estrazione per i mandati legati ad una obbligazione
-- la vecchia query duplicava i mandati che erano legati ad una scadenza obbligazione
-- distribuita su pi? scadenze voce
--
-- Date: 20/05/2003
-- Version: 1.8
-- Introdotto esercizio e cds in log per 102,104,602,604
--
-- Date: 20/05/2003
-- Version: 1.9
-- Correzione numero errore da 010 a 110
--
-- Date: 21/05/2003
-- Version: 1.10
-- modificata layout log
--
-- Date: 22/05/2003
-- Version: 1.11
-- Inserita condizione di ti_appartenenza per consultare voce_f_saldi_cmp
--
-- Date: 07/07/2003
-- Version: 1.12
-- Corretta query di estrazione che totalizza importi mandati sui singoli capitoli
--
-- Date: 07/07/2003
-- Version: 1.13
-- Corretta stato obbligazione stornato
--
-- Date: 08/07/2003
-- Version: 1.14
-- Corretta query estrazione che totalizza importi mandati sui singoli capitoli in modo che non
-- consideri i mandati che sono generati da Note di Credito
--
-- Date: 14/07/2006
-- Version: 1.15
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants

TIPO_LOG_MAR_MANREV CONSTANT VARCHAR2(20):='MAR_MAN_REV00';
TIPO_COMPETENZA CONSTANT CHAR(1):='C';
TIPO_RESIDUO CONSTANT CHAR(1):='R';
TIPO_SPESA CONSTANT CHAR(1):='S';
TIPO_ENTRATA CONSTANT CHAR(1):='E';
STATO_ANNULLATO CONSTANT CHAR(1):='A';
STATO_ANNULLATO_OBB CONSTANT CHAR(1):='S';
-- Tipi di disallineamenti supportati

D_AUTOR CONSTANT VARCHAR2(10):='D_AUTOR'; --  Tipo disallineamento


D_AUTOR000 CONSTANT VARCHAR2(70):='MAN-D_AUTOR000-MAN-SUM(RIGA)'; -- Importo testata diverso da somma righe: ritorna im. testata - somma(im. righe)
--D_AUTOR005 CONSTANT VARCHAR2(10):='D_AUTOR005'; --
--D_AUTOR010 CONSTANT VARCHAR2(10):='D_AUTOR010'; --
--D_AUTOR015 CONSTANT VARCHAR2(10):='D_AUTOR015'; --
--D_AUTOR020 CONSTANT VARCHAR2(10):='D_AUTOR020'; --
--D_AUTOR025 CONSTANT VARCHAR2(10):='D_AUTOR025'; --
D_AUTOR100 CONSTANT VARCHAR2(70):='MAN-D_AUTOR100-TOT CAP CDS COMP-SALDO'; -- Totale mandati CDS Comp. != saldo mandati CDS Comp. (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR105 CONSTANT VARCHAR2(70):='MAN-D_AUTOR105-TOT CAP CDS RES-SALDO'; -- Totale mandati CDS Res. != saldo mandati CDS Res. (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR110 CONSTANT VARCHAR2(70):='MAN-D_AUTOR110-TOT CAP COMP-SALDO'; -- Totale mandati CNR comp. capitolo != saldo capitolo CNR comp. (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR115 CONSTANT VARCHAR2(70):='MAN-D_AUTOR115-TOT CAP RES-SALDO'; -- Totale mandati CNR res. capitolo != saldo capitolo CNR res. (voce_f_saldi_cmp): ritorna la differenza dei due
--
D_AUTOR500 CONSTANT VARCHAR2(70):='REV-D_AUTOR500-REV-SUM(IM_RIGA)'; -- Importo testata diverso da somma righe: ritorna im. testata - somma(im. righe)
-- D_AUTOR505 CONSTANT VARCHAR2(10):='D_AUTOR505'; --
-- D_AUTOR510 CONSTANT VARCHAR2(10):='D_AUTOR510'; --
-- D_AUTOR515 CONSTANT VARCHAR2(10):='D_AUTOR515'; --
-- D_AUTOR520 CONSTANT VARCHAR2(10):='D_AUTOR520'; --
-- D_AUTOR525 CONSTANT VARCHAR2(10):='D_AUTOR525'; --
D_AUTOR600 CONSTANT VARCHAR2(70):='REV-D_AUTOR600-TOT CAP CDS COMP-SALDO'; -- Totale reversali CDS Comp. != saldo reversali CDS (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR605 CONSTANT VARCHAR2(70):='REV-D_AUTOR605-TOT CAP CDS RES-SALDO'; -- Totale reversali CDS Res. != saldo reversali CDS (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR610 CONSTANT VARCHAR2(70):='REV-D_AUTOR610-TOT CAP COMP-SALDO'; -- Totale reversali CNR comp. su capitolo != saldo capitolo comp. CNR (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR615 CONSTANT VARCHAR2(70):='REV-D_AUTOR615-TOT CAP RES-SALDO'; -- Totale reversali CNR res. su capitolo != saldo capitolo res. CNR (voce_f_saldi_cmp): ritorna la differenza dei due

-- Functions e Procedures

-- Parametri:

-- aEs -> Esercizio
-- aCDS -> Esercizio
-- isModifica -> Y = update N preview

 procedure job_mar_autor00(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2, isModifica char);

 function MSG_DIS_AUTOR(aTipo varchar2, aMan mandato%rowtype, aNota varchar2) return varchar2;
 function MSG_DIS_AUTOR(aTipo varchar2, aRev reversale%rowtype, aNota varchar2) return varchar2;

end;


CREATE OR REPLACE package body CNRMAR037 is
 function fnum(anum number) return varchar2 is
 begin
  return to_char(anum,'9999999999999D99');
 end;

 function descMan(aMan mandato%rowtype) return varchar2 is
 begin
  return 'MAN es:'||aMan.esercizio||
           ' cds:'||aMan.cd_cds||
		   ' uo:'||aMan.cd_unita_organizzativa||
		   ' tipo:'||aMan.ti_mandato||
		   ' c/r:'||aMan.ti_competenza_residuo||
		   ' pg:'||aMan.pg_mandato;
 end;

 function descManRiga(aManRiga mandato_riga%rowtype) return varchar2 is
 begin
  return ' es_obb:'||aManRiga.esercizio_obbligazione||
		 ' es_ori_obb:'||aManRiga.esercizio_ori_obbligazione||
		 ' pg_obb:'||aManRiga.pg_obbligazione||
         ' pg_obb_scad:'||aManRiga.pg_obbligazione_scadenzario||
         ' cds_damm:'||aManRiga.cd_cds_doc_amm||
		 ' uo_damm:'||aManRiga.cd_uo_doc_amm||
		 ' es_damm:'||aManRiga.esercizio_doc_amm||
		 ' tipo_damm:'||aManRiga.cd_tipo_documento_amm||
		 ' pg_damm:'||aManRiga.pg_doc_amm;
 end;

 function descRev(aRev reversale%rowtype) return varchar2 is
 begin
  return 'REV es:'||aRev.esercizio||
           ' cds:'||aRev.cd_cds||
		   ' uo:'||aRev.cd_unita_organizzativa||
		   ' tipo:'||aRev.ti_reversale||
		   ' c/r:'||aRev.ti_competenza_residuo||
		   ' pg:'||aRev.pg_reversale;
 end;

 function descRevRiga(aRevRiga reversale_riga%rowtype) return varchar2 is
 begin
  return ' es_obb:'||aRevRiga.esercizio_accertamento||
		 ' es_ori_obb:'||aRevRiga.esercizio_ori_accertamento||
		 ' pg_obb:'||aRevRiga.pg_accertamento||
         ' pg_obb_scad:'||aRevRiga.pg_accertamento_scadenzario||
         ' cds_damm:'||aRevRiga.cd_cds_doc_amm||
		 ' uo_damm:'||aRevRiga.cd_uo_doc_amm||
		 ' es_damm:'||aRevRiga.esercizio_doc_amm||
		 ' tipo_damm:'||aRevRiga.cd_tipo_documento_amm||
		 ' pg_damm:'||aRevRiga.pg_doc_amm;
 end;

 function descVoce(aVoc voce_f_saldi_cmp%rowtype) return varchar2 is
 begin
  return 'VOCE cds:'||aVoc.cd_cds||
           ' es:'||aVoc.esercizio||
		   ' app:'||aVoc.ti_appartenenza||
		   ' gest:'||aVoc.ti_gestione||
		   ' voce:'||aVoc.cd_voce||
		   ' comp:'||aVoc.ti_competenza_residuo;
 end;

 function MSG_DIS_AUTOR(aTipo varchar2,aMan mandato%rowtype,aNota varchar2)
 return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aTipo||' '||descMan(aMan)||' '||aNota;
  return aOut;
 end;

 function MSG_DIS_AUTOR(aTipo varchar2,aRev reversale%rowtype,aNota varchar2)
 return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aTipo||' '||descRev(aRev)||' '||aNota;
  return aOut;
 end;

 function MSG_DIS_AUTOR(aTipo varchar2,aVocFSal voce_f_saldi_cmp%rowtype,aNota varchar2)
 return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aTipo||' '||descVoce(aVocFSal)||' '||aNota;
  return aOut;
 end;

 procedure job_mar_autor00(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2, isModifica char) is
  aTSNow date;
  aUser varchar2(20);
  aEndT date;
  aStartT date;
  aEnd varchar2(80);
  aStart varchar2(80);
  aDelta varchar2(80);
  aMsgTipoMar varchar2(30);
  aNumMan number:=0;
  aNumRev number:=0;
  aCDS unita_organizzativa%rowtype;
 begin
  aTSNow:=sysdate;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);
  if isModifica = 'Y' then
   aMsgTipoMar:='MARTELLO';
  else
   aMsgTipoMar:='VERIFICA';
  end if;
  aStartT:=sysdate;
  aStart:=to_char(sysdate,'YYYYMMDD HH:MI:SS');
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_MAR_MANREV, job, D_AUTOR||'-'||aMsgTipoMar||' (disallinamenti documenti autorizzatori). Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'),D_AUTOR);
  -- Inizio procedura di controllo/martello
  BEGIN
   IBMUTL200.logInf(pg_exec, D_AUTOR||'-START  at: '||aStart||' es.'||aEs||' cds.'||aCdCds,aEs||aCdCds,'SOC');
   -- Recuperiamo in aCds le informazioni inerenti il Cds in esame
   begin
    select * into aCDS from unita_organizzativa where cd_unita_organizzativa = aCdCds and fl_cds = 'Y';
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Cds non trovato:'||aCdCds);
   end;
   -- *******************************
   -- Inizio Ciclo su tutti i mandati
   -- *******************************
   for aM in (
    select * from mandato where
	     esercizio = aEs
	 and cd_cds = aCdCds
	for update nowait
   ) loop
    declare
	 aTotRiga number(15,2);
    -- Inizio Calcolo il totale del mandato
    begin
     aNumMan:=aNumMan+1;
	 -- Calcoliamo il totale del mandato sommando l'importo delle righe ad esso associate aTotRiga
  	 -- Check D_AUTOR000 MAN
     select nvl(sum(im_mandato_riga),0) into aTotRiga from mandato_riga where
                 esercizio = aM.esercizio
    		 and cd_cds = aM.cd_cds
    		 and pg_mandato = aM.pg_mandato;
	 -- se il totale d
     if aTotRiga <> aM.im_mandato then
	  -- Se il totale relativo alla somma delle righe non corrisponde
	  -- al totale del MANDATO allora segnaliamo l'anomalia
      IBMUTL200.logInf(pg_exec, MSG_DIS_AUTOR(D_AUTOR000,aM,null),'delta:'||fnum(aM.im_mandato-aTotRiga),'SOCD');
     end if;
    end;
    -- Fine Calcolo il totale del mandato
   end loop;
   -- *****************************
   -- Fine Ciclo su tutti i mandati
   -- *****************************
   -- **********************************
   -- Inizio Ciclo su tutte le reversali
   -- **********************************
   for aR in (
    select * from reversale where
	     esercizio = aEs
	 and cd_cds = aCdCds
	for update nowait
   ) loop
    declare
	 aTotRiga number(15,2);
    -- Inizio Calcolo il totale della reversale
    begin
     aNumRev:=aNumRev+1;
	 -- Calcoliamo il totale della reversale sommando l'importo delle righe ad essa associate aTotRiga
  	 -- Check D_AUTOR500 REV
     select nvl(sum(im_reversale_riga),0) into aTotRiga from reversale_riga where
                 esercizio = aR.esercizio
    		 and cd_cds = aR.cd_cds
    		 and pg_reversale = aR.pg_reversale;
     if aTotRiga <> aR.im_reversale then
	  -- Se il totale relativo alla somma delle righe non corrisponde
	  -- al totale della REVERSALE allora segnaliamo l'anomalia
      IBMUTL200.logInf(pg_exec, MSG_DIS_AUTOR(D_AUTOR500,aR,null),'delta:'||fnum(aR.im_reversale-aTotRiga),'SOCD');
     end if;
    end;
    -- Fine Calcolo il totale della reversale
   end loop;
   -- ********************************
   -- Fine Ciclo su tutte le reversali
   -- ********************************

   -- ******************************************************************************************
   -- Se il cds in esame non ? di tipo ENTE allora controlliamo
   -- che la somma di tutti i mandati collegati sia pari al saldo sulla tabella voce_f_saldi_cmp
   -- il saldo sulla tabella voce_f_saldi_cmp, per i mandati, viene calcolato in base a:
   -- esercizio = aEs, cd_cds = aCdCds, TI_GESTIONE = 'S' (Spesa), TI_COMPETENZA_RESIDUO ='C' (Competenza)
   -- ******************************************************************************************
   if aCds.cd_tipo_unita <> 'ENTE' then
    -- ***********************************************
    -- Inizio Controllo Saldi mandati aggregati per cd_cds, competenza, residuo
    -- Check D_AUTOR100, D_AUTOR105 MAN
    -- ***********************************************
    declare
     aTotC number(20,2); -- Totale Competenze
     aTotR number(20,2); -- Totale Residui
     aTotSaldiC number(20,2); -- Totale Saldi Competenze
     aTotSaldiR number(20,2); -- Totale Saldi Residui
    begin
	 aTotC :=0;
	 aTotR :=0;
     aTotSaldiC := 0;
     aTotSaldiR := 0;
     select nvl(sum(decode(ti_competenza_residuo,TIPO_COMPETENZA,im_mandato,0)),0),
	 		nvl(sum(decode(ti_competenza_residuo,TIPO_RESIDUO,im_mandato,0)),0)
	 into aTotC, aTotR
	 from mandato
	 where esercizio = aEs
	 and cd_cds = aCdCds
	 and stato <> STATO_ANNULLATO;


     select nvl(sum(decode(ti_competenza_residuo,TIPO_COMPETENZA,im_mandati_reversali,0)),0),
	 		nvl(sum(decode(ti_competenza_residuo,TIPO_RESIDUO,im_mandati_reversali,0)),0)
	 into aTotSaldiC, aTotSaldiR
	 from voce_f_saldi_cmp
	 where esercizio = aEs
	 and cd_cds = aCdCds
	 and ti_gestione = TIPO_SPESA
	 and ti_appartenenza = 'D';


     if aTotC <> aTotSaldiC then
      IBMUTL200.logInf(pg_exec, D_AUTOR100 ||' Esercizio = '|| aEs || ' Cds = '||aCdCds ,'delta:'||fnum(aTotC-aTotSaldiC),'SOCD');
     end if;
     if aTotR <> aTotSaldiR then
      IBMUTL200.logInf(pg_exec, D_AUTOR105||' Esercizio = '|| aEs || ' Cds = '||aCdCds,'delta:'||fnum(aTotR-aTotSaldiR),'SOCD');
     end if;
    end;
    -- **********************************************************************
    -- Fine Controllo Saldi mandati aggregati per cd_cds, competenza, residuo
    -- **********************************************************************

    -- **************************************************************
	-- Inizio Controllo Totalizzazione mandati aggregati per Capitolo
    -- Check D_AUTOR110 D_AUTOR115 MAN
    -- **************************************************************
    declare
--     aTotSaldi number(20,2);
       lVoceFSaldi voce_F_saldi_cmp%rowtype;
	begin
		 -- Aggreghiamo i mandati in base al capitolo (CD_CDS, ESERCIZIO, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, TI_COMPETENZA_RESIDUO)
		 for aMandCapitolo in (select obbsv.CD_CDS, obbsv.ESERCIZIO, obbsv.TI_APPARTENENZA, obbsv.TI_GESTIONE,
			 						  obbsv.CD_VOCE, man.TI_COMPETENZA_RESIDUO,
			 						  sum(decode (manr.im_mandato_riga,0,0,1) * obbsv.im_voce) TOTALE
		 	 	      from mandato man, mandato_riga manr,
					  	   obbligazione obb,
					  	   obbligazione_scadenzario obbs, (select ESERCIZIO, ESERCIZIO_ORIGINALE, PG_OBBLIGAZIONE, PG_OBBLIGAZIONE_SCADENZARIO, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, cd_cds, sum(im_voce) im_voce
						   								   from obbligazione_scad_voce
														   group by ESERCIZIO, ESERCIZIO_ORIGINALE, PG_OBBLIGAZIONE, PG_OBBLIGAZIONE_SCADENZARIO, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, cd_cds )obbsv
					  where man.stato <> STATO_ANNULLATO
					  and   man.cd_cds = aCdCds
					  and   man.esercizio = aEs
					  and   man.CD_CDS                       = manr.cd_cds
					  and   man.ESERCIZIO  			 = manr.esercizio
					  and   man.PG_MANDATO 			 = manr.pg_mandato
					  and   manr.CD_CDS 	              	 = obbs.cd_cds
					  and   manr.ESERCIZIO_OBBLIGAZIONE 	 = obbs.esercizio
					  and   manr.ESERCIZIO_ORI_OBBLIGAZIONE  = obbs.esercizio_originale
					  and   manr.PG_OBBLIGAZIONE 		 = obbs.pg_obbligazione
					  and   manr.PG_OBBLIGAZIONE_SCADENZARIO = obbs.pg_obbligazione_scadenzario
					  and   obb.CD_CDS			 = manr.CD_CDS
					  and   obb.ESERCIZIO			 = manr.ESERCIZIO_OBBLIGAZIONE
					  and   obb.ESERCIZIO_ORIGINALE          = manr.ESERCIZIO_ORI_OBBLIGAZIONE
 					  and   obb.PG_OBBLIGAZIONE 		 = manr.PG_OBBLIGAZIONE
 					  and   obb.STATO_OBBLIGAZIONE		 <> STATO_ANNULLATO_OBB
					  and   obbs.CD_CDS			 = obbsv.cd_cds
					  and   obbs.ESERCIZIO			 = obbsv.esercizio
					  and   obbs.ESERCIZIO_ORIGINALE	 = obbsv.esercizio_originale
					  and   obbs.PG_OBBLIGAZIONE		 = obbsv.pg_obbligazione
					  and   obbs.PG_OBBLIGAZIONE_SCADENZARIO = obbsv.pg_obbligazione_scadenzario
					  group by obbsv.CD_CDS,
						       obbsv.ESERCIZIO,
							   obbsv.TI_APPARTENENZA,
							   obbsv.TI_GESTIONE,
							   obbsv.CD_VOCE,
							   man.TI_COMPETENZA_RESIDUO)
		 loop
			 -- Azzeramento della variabile di totalizzazione Saldi a dei totali
--			 aTotSaldi := 0;
			 -- Fine Azzeramento
			 -- Calcolo dei saldi relativi alla COMPETENZA/RESIDUO
		 	 select * -- im_mandati_reversali
			 into lVoceFSaldi from voce_f_saldi_cmp voce
			 where voce.CD_CDS                = aMandCapitolo.cd_cds
			 and   voce.ESERCIZIO 			  = aMandCapitolo.esercizio
			 and   voce.TI_APPARTENENZA 	  = aMandCapitolo.ti_appartenenza
			 and   voce.TI_GESTIONE 		  = aMandCapitolo.ti_gestione
			 and   voce.CD_VOCE 			  = aMandCapitolo.cd_voce
			 and   voce.TI_COMPETENZA_RESIDUO = aMandCapitolo.TI_COMPETENZA_RESIDUO;

			 if aMandCapitolo.TOTALE <> lVoceFSaldi.im_mandati_reversali then
			 	if aMandCapitolo.TI_COMPETENZA_RESIDUO = TIPO_COMPETENZA then
--      		 	   IBMUTL200.logInf(pg_exec, D_AUTOR110 || ' - cd_cds: '||aMandCapitolo.cd_cds ||' - esercizio: '||aMandCapitolo.esercizio||' - ti_appartenenza: '||aMandCapitolo.ti_appartenenza || ' - ti_gestione: ' ||aMandCapitolo.ti_gestione ||' - cd_voce: ' || aMandCapitolo.cd_voce || ' - competenza_residuo: '||aMandCapitolo.TI_COMPETENZA_RESIDUO ,'delta:'||fnum(aMandCapitolo.TOTALE-lVoceFSaldi.im_mandati_reversali),'SOCD');
				   IBMUTL200.logInf(pg_exec,MSG_DIS_AUTOR(D_AUTOR110,lVoceFSaldi,null),'delta:'||fnum(aMandCapitolo.TOTALE-lVoceFSaldi.im_mandati_reversali),'SOCD');
			 	end if;
			 	if aMandCapitolo.TI_COMPETENZA_RESIDUO = TIPO_RESIDUO then
--      		 	   IBMUTL200.logInf(pg_exec, D_AUTOR115 || ' - cd_cds: '||aMandCapitolo.cd_cds ||' - esercizio: '||aMandCapitolo.esercizio||' - ti_appartenenza: '||aMandCapitolo.ti_appartenenza || ' - ti_gestione: ' ||aMandCapitolo.ti_gestione ||' - cd_voce: ' || aMandCapitolo.cd_voce || ' - competenza_residuo: '||aMandCapitolo.TI_COMPETENZA_RESIDUO ,'delta:'||fnum(aMandCapitolo.TOTALE-lVoceFSaldi.im_mandati_reversali),'SOCD');
				   IBMUTL200.logInf(pg_exec,MSG_DIS_AUTOR(D_AUTOR115,lVoceFSaldi,null),'delta:'||fnum(aMandCapitolo.TOTALE-lVoceFSaldi.im_mandati_reversali),'SOCD');
			 	end if;
			 end if;
			-- **********************************************************
			-- Modifichiamo i saldi in voce_f_saldi_cmp
			-- **********************************************************
-- 			if isModifica ='Y' then
-- 		    update voce_f_saldi_cmp
-- 			   set im_mandati_reversali = aMandCapitolo.TOTALE
-- 			   where CD_CDS                = aMandCapitolo.cd_cds
-- 			   and   ESERCIZIO 			= aMandCapitolo.esercizio
-- 			   and   TI_APPARTENENZA 	    = aMandCapitolo.ti_appartenenza
-- 			   and   TI_GESTIONE 		    = aMandCapitolo.ti_gestione
-- 			   and   CD_VOCE 			    = aMandCapitolo.cd_voce
-- 			   and   TI_COMPETENZA_RESIDUO = aMandCapitolo.ti_competenza_residuo;
-- 			end if;

		 end loop;
	end;
    -- ************************************************************
	-- Fine Controllo Totalizzazione mandati aggregati per Capitolo
    -- ************************************************************

    -- **************************************************************************
    -- Inizio Controllo Saldi reversali aggregate per cd_cds, competenza, residuo
    -- Check D_AUTOR600, D_AUTOR605 REV
    -- **************************************************************************
    declare
     aTotC number(20,2);
     aTotR number(20,2);
     aTotSaldiC number(20,2);
     aTotSaldiR number(20,2);
	 aTipoAppartenenza char(1);
    begin
     select nvl(sum(decode(ti_competenza_residuo,TIPO_COMPETENZA,im_reversale,0)),0),
	 		nvl(sum(decode(ti_competenza_residuo,TIPO_RESIDUO,im_reversale,0)),0)
	 into aTotC, aTotR
	 from reversale
	 where esercizio = aEs
	 and cd_cds = aCdCds
	 and stato <> STATO_ANNULLATO;

	 if aCDS.cd_tipo_unita='ENTE' then
	 	aTipoAppartenenza := 'C';
	 else
	 	aTipoAppartenenza := 'D';
	 end if;

     select nvl(sum(decode(ti_competenza_residuo,TIPO_COMPETENZA,im_mandati_reversali,0)),0),
	 		nvl(sum(decode(ti_competenza_residuo,TIPO_RESIDUO,im_mandati_reversali,0)),0)
	 into aTotSaldiC, aTotSaldiR
	 from voce_f_saldi_cmp
	 where esercizio = aEs
	 and cd_cds = aCdCds
	 and ti_gestione = TIPO_ENTRATA
	 and ti_appartenenza = aTipoAppartenenza;

     if aTotC <> aTotSaldiC then
      IBMUTL200.logInf(pg_exec, D_AUTOR600 ||' Esercizio = '|| aEs || ' Cds = '||aCdCds,'delta:'||fnum(aTotC-aTotSaldiC),'SOCD');
     end if;

     if aTotR <> aTotSaldiR then
      IBMUTL200.logInf(pg_exec, D_AUTOR600 ||' Esercizio = '|| aEs || ' Cds = '||aCdCds,'delta:'||fnum(aTotR-aTotSaldiR),'SOCD');
     end if;

    end;
    -- **************************************************************************
    -- Fine Controllo Saldi reversali aggregate per cd_cds, competenza, residuo
    -- **************************************************************************

    -- ****************************************************************
	-- Inizio Controllo Totalizzazione reversali aggregate per Capitolo
    -- Check D_AUTOR605 REV
    -- ****************************************************************
    declare
     aTotC number(20,2);
     aTotR number(20,2);
--     aTotSaldi number(20,2);
	 lVoceFSaldi voce_F_saldi_cmp%rowtype;
    begin
		 -- Aggreghiamo le reversali in base al capitolo (CD_CDS, ESERCIZIO, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, TI_COMPETENZA_RESIDUO)
		 for aRevCapitolo in (select acc.CD_CDS, acc.ESERCIZIO, acc.TI_APPARTENENZA, acc.TI_GESTIONE, acc.CD_VOCE, rev.TI_COMPETENZA_RESIDUO, sum(revr.im_reversale_riga) TOTALE
		 	 	      from reversale rev, reversale_riga revr,
					  	   accertamento acc
					  where rev.stato <> STATO_ANNULLATO
					  and   rev.cd_cds = aCdCds
					  and   rev.esercizio = aEs
					  and   rev.CD_CDS                       = revr.cd_cds
					  and   rev.ESERCIZIO  		         = revr.esercizio
					  and   rev.PG_REVERSALE 	         = revr.pg_reversale
					  and   revr.CD_CDS 	              	 = acc.cd_cds
					  and   revr.ESERCIZIO_ACCERTAMENTO 	 = acc.esercizio
					  and   revr.ESERCIZIO_ORI_ACCERTAMENTO	 = acc.esercizio_originale
					  and   revr.PG_ACCERTAMENTO 		 = acc.pg_accertamento
					  group by acc.CD_CDS,
						       acc.ESERCIZIO,
							   acc.TI_APPARTENENZA,
							   acc.TI_GESTIONE,
							   acc.CD_VOCE,
							   rev.TI_COMPETENZA_RESIDUO)
		 loop
			 -- Azzeramento della variabile di totalizzazione Saldi a dei totali
--			 aTotSaldi := 0;
		 	 aTotC := 0;
		 	 aTotR := 0;
			 -- Fine Azzeramento

		 	 select * -- im_mandati_reversali
			 into lVoceFSaldi
			 from voce_f_saldi_cmp voce
			 where voce.CD_CDS                = aRevCapitolo.cd_cds
			 and   voce.ESERCIZIO 			  = aRevCapitolo.esercizio
			 and   voce.TI_APPARTENENZA 	  = aRevCapitolo.ti_appartenenza
			 and   voce.TI_GESTIONE 		  = aRevCapitolo.ti_gestione
			 and   voce.CD_VOCE 			  = aRevCapitolo.cd_voce
			 and   voce.TI_COMPETENZA_RESIDUO = aRevCapitolo.ti_competenza_residuo;

			 if aRevCapitolo.TOTALE <> lVoceFSaldi.im_mandati_reversali then
			 	if aRevCapitolo.TI_COMPETENZA_RESIDUO = TIPO_COMPETENZA then
--      		 	   IBMUTL200.logInf(pg_exec, D_AUTOR610 || ' - cd_cds: '||aRevCapitolo.cd_cds ||' - esercizio: '||aRevCapitolo.esercizio||' - ti_appartenenza: '||aRevCapitolo.ti_appartenenza || ' - ti_gestione: ' ||aRevCapitolo.ti_gestione ||' - cd_voce: ' || aRevCapitolo.cd_voce || ' - competenza_residuo: '||aRevCapitolo.TI_COMPETENZA_RESIDUO ,'delta:'||fnum(aRevCapitolo.TOTALE-lVoceFSaldi.im_mandati_reversali),'SOCD');
				   IBMUTL200.logInf(pg_exec,MSG_DIS_AUTOR(D_AUTOR610,lVoceFSaldi,null),'delta:'||fnum(aRevCapitolo.TOTALE-lVoceFSaldi.im_mandati_reversali),'SOCD');
			 	end if;
			 	if aRevCapitolo.TI_COMPETENZA_RESIDUO = TIPO_RESIDUO then
--      		 	   IBMUTL200.logInf(pg_exec, D_AUTOR615 || ' - cd_cds: '||aRevCapitolo.cd_cds ||' - esercizio: '||aRevCapitolo.esercizio||' - ti_appartenenza: '||aRevCapitolo.ti_appartenenza || ' - ti_gestione: ' ||aRevCapitolo.ti_gestione ||' - cd_voce: ' || aRevCapitolo.cd_voce || ' - competenza_residuo: '||aRevCapitolo.TI_COMPETENZA_RESIDUO ,'delta:'||fnum(aRevCapitolo.TOTALE-lVoceFSaldi.im_mandati_reversali),'SOCD');
				   IBMUTL200.logInf(pg_exec,MSG_DIS_AUTOR(D_AUTOR615,lVoceFSaldi,null),'delta:'||fnum(aRevCapitolo.TOTALE-lVoceFSaldi.im_mandati_reversali),'SOCD');
			 	end if;
			 end if;
			 -- **********************************************************
			 -- Modifichiamo i saldi in voce_f_saldi_cmp
			 -- **********************************************************
-- 			 if isModifica ='Y' then
-- 		     update voce_f_saldi_cmp
-- 			    set im_mandati_reversali = aRevCapitolo.TOTALE
-- 			    where CD_CDS                = aRevCapitolo.cd_cds
-- 			    and   ESERCIZIO 			= aRevCapitolo.esercizio
-- 			    and   TI_APPARTENENZA 	    = aRevCapitolo.ti_appartenenza
-- 			    and   TI_GESTIONE 		    = aRevCapitolo.ti_gestione
-- 			    and   CD_VOCE 			    = aRevCapitolo.cd_voce
-- 			    and   TI_COMPETENZA_RESIDUO = aRevCapitolo.ti_competenza_residuo;
-- 			 end if;

		 end loop;
	end;
    -- **************************************************************
	-- Fine Controllo Totalizzazione reversali aggregate per Capitolo
    -- **************************************************************

   end if; -- Fine sezione relativa a bilancio CDS

   -- Fine procedura di controllo/martello
   aEndT:=sysdate;
   aEnd:=to_char(aEndT,'YYYYMMDD HH:MI:SS');
   aDelta:=to_char((aEndT-aStartT)*24*3600,'999999');
   IBMUTL200.logInf(pg_exec,D_AUTOR||'-PROCESSATE '||aNumMan||' MAN + '||aNumRev||' REV',aEs||aCdCds,'SOC');
   IBMUTL200.logInf(pg_exec,D_AUTOR||'-END at: '||aEnd||' tot exec time(s):'||aDelta||' es.'||aEs||' cds.'||aCdCds,aEs||aCdCds,'SOC');
  EXCEPTION WHEN OTHERS THEN
   ROLLBACK;
   IBMUTL200.logErr(pg_exec, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,'SOC');
  END;
 end;
end;


