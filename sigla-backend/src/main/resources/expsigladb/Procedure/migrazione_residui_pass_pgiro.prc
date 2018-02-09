CREATE OR REPLACE procedure migrazione_residui_pass_pgiro (cgUtente varchar2 ) is
--==================================================================================================
--
-- MIGRAZIONE_RESIDUI_PASS_PGIRO - migrazione_residui_pass_pgiro
--
-- Date: 18/07/2006
-- Version: 1.0
--
-- History:
--
-- Date: 18/07/2006
-- Version: 1.0
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
--==================================================================================================
--
-- Constants
--
	gcEsercizio number := 2002;
	gcEsercizioDest number := 2003;
	gPgLog number;
	gMsgLog varchar2(300);
    TYPE TipoRecord IS RECORD
       (
        ESERCIZIO NUMBER,
        CAPITOLO VARCHAR2(50),
        UO VARCHAR2(30),
        IMPORTO NUMBER(15,2)
       );

    TYPE TipoTabella IS TABLE OF TipoRecord INDEX BY BINARY_INTEGER;

    lResiduiPassiviPGiro TipoTabella;
	lContatoreRighe number;
 	lCds unita_organizzativa%rowtype;
 	lUo  unita_organizzativa%rowtype;
 	lCdr cdr%rowtype;
 	lCdTerzo number;
 	lPgObbIniziale number;
 	lDataMigrazione date;
 	lContatore number;
 	lNumResiduiPasSci number;
 	lCdTitoloCapitolo voce_f.cd_titolo_capitolo%type;
 	lElemVoce elemento_voce%rowtype;
 	lPgObbligazione number;
--
 	lImpegno obbligazione%rowtype;
 	lImpegnoScad obbligazione_scadenzario%rowtype;
 	lImpegnoScadVoce obbligazione_scad_voce%rowtype;
 	lResiduo obbligazione%rowtype;
 	lResiduoScadenza obbligazione_scadenzario%rowtype;
 	lResiduoScadVoce obbligazione_scad_voce%rowtype;

	begin
		-- Caricamento della tabella
		lResiduiPassiviPGiro(1).ESERCIZIO  := 2002;
		lResiduiPassiviPGiro(2).ESERCIZIO  := 2002;
		lResiduiPassiviPGiro(3).ESERCIZIO  := 2001;
		lResiduiPassiviPGiro(4).ESERCIZIO  := 2002;
		lResiduiPassiviPGiro(5).ESERCIZIO  := 2002;
		lResiduiPassiviPGiro(6).ESERCIZIO  := 2001;
		lResiduiPassiviPGiro(7).ESERCIZIO  := 2002;
		lResiduiPassiviPGiro(8).ESERCIZIO  := 2002;
		lResiduiPassiviPGiro(9).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(10).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(11).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(12).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(13).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(14).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(15).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(16).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(17).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(18).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(19).ESERCIZIO := 1999;
		lResiduiPassiviPGiro(20).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(21).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(22).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(23).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(24).ESERCIZIO := 2002;
		lResiduiPassiviPGiro(25).ESERCIZIO := 2002;


		lResiduiPassiviPGiro(1).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(2).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(3).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(4).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(5).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(6).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(7).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(8).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(9).CAPITOLO :=  '2.003';
		lResiduiPassiviPGiro(10).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(11).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(12).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(13).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(14).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(15).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(16).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(17).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(18).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(19).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(20).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(21).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(22).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(23).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(24).CAPITOLO := '2.003';
		lResiduiPassiviPGiro(25).CAPITOLO := '2.003';



		lResiduiPassiviPGiro(1).UO  := '000.202';
		lResiduiPassiviPGiro(2).UO  := '000.203';
		lResiduiPassiviPGiro(3).UO  := '000.300';
		lResiduiPassiviPGiro(4).UO  := '000.300';
		lResiduiPassiviPGiro(5).UO  := '002.002';
		lResiduiPassiviPGiro(6).UO  := '002.004';
		lResiduiPassiviPGiro(7).UO  := '016.000';
		lResiduiPassiviPGiro(8).UO  := '021.000';
		lResiduiPassiviPGiro(9).UO := '035.000';
		lResiduiPassiviPGiro(10).UO := '039.000';
		lResiduiPassiviPGiro(11).UO := '046.000';
		lResiduiPassiviPGiro(12).UO := '050.000';
		lResiduiPassiviPGiro(13).UO := '051.000';
		lResiduiPassiviPGiro(14).UO := '056.000';
		lResiduiPassiviPGiro(15).UO := '057.000';
		lResiduiPassiviPGiro(16).UO := '069.000';
		lResiduiPassiviPGiro(17).UO := '074.000';
		lResiduiPassiviPGiro(18).UO := '075.000';
		lResiduiPassiviPGiro(19).UO := '077.000';
		lResiduiPassiviPGiro(20).UO := '077.000';
		lResiduiPassiviPGiro(21).UO := '087.000';
		lResiduiPassiviPGiro(22).UO := '095.001';
		lResiduiPassiviPGiro(23).UO := '096.000';
		lResiduiPassiviPGiro(24).UO := '101.000';
		lResiduiPassiviPGiro(25).UO := '103.000';

		lResiduiPassiviPGiro(1).IMPORTO  :=60880.37;
		lResiduiPassiviPGiro(2).IMPORTO  :=95341.33;
		lResiduiPassiviPGiro(3).IMPORTO  :=89229.86;
		lResiduiPassiviPGiro(4).IMPORTO  :=1215208.28;
		lResiduiPassiviPGiro(5).IMPORTO  :=79956.62;
		lResiduiPassiviPGiro(6).IMPORTO  :=51645.69;
		lResiduiPassiviPGiro(7).IMPORTO  :=20888.74;
		lResiduiPassiviPGiro(8).IMPORTO  :=234000.00;
		lResiduiPassiviPGiro(9).IMPORTO :=608474.69;
		lResiduiPassiviPGiro(10).IMPORTO :=249441.82;
		lResiduiPassiviPGiro(11).IMPORTO :=382060.00;
		lResiduiPassiviPGiro(12).IMPORTO :=431550.00;
		lResiduiPassiviPGiro(13).IMPORTO :=317356.38;
		lResiduiPassiviPGiro(14).IMPORTO :=28261.79;
		lResiduiPassiviPGiro(15).IMPORTO :=225135.00;
		lResiduiPassiviPGiro(16).IMPORTO :=105927.57;
		lResiduiPassiviPGiro(17).IMPORTO :=57.64;
		lResiduiPassiviPGiro(18).IMPORTO :=8993.00;
		lResiduiPassiviPGiro(19).IMPORTO :=42226.56;
		lResiduiPassiviPGiro(20).IMPORTO :=29965.57;
		lResiduiPassiviPGiro(21).IMPORTO :=70088.55;
		lResiduiPassiviPGiro(22).IMPORTO :=264116.00;
		lResiduiPassiviPGiro(23).IMPORTO :=70496.77;
		lResiduiPassiviPGiro(24).IMPORTO :=128492.49;
		lResiduiPassiviPGiro(25).IMPORTO :=45160.00;

		-- Inizzializzazione del progressivo di Log
	 	gPgLog := ibmutl200.LOGSTART('Migrazione Residui Passivi Partite Giro' , cgUtente, 1, 1);

	    gMsgLog := 'Impossibile recuperare il CdS per l''ENTE';
  		 -- Recuperiamo il cds realtivo all'ente
     	select *
  		into lCds
     	from  unita_organizzativa
     	where cd_tipo_unita = cnrctb020.tipo_ente
	 	and   fl_cds = 'Y';

		gMsgLog := 'Impossibile recuperare la UO per l''ENTE';
  		 -- Recuperiamo l'UO realtivo all'ente
     	select *
  		into lUo
     	from  unita_organizzativa
     	where cd_tipo_unita = cnrctb020.tipo_ente
  		and   fl_uo_cds = 'Y';

	 	gMsgLog := 'Impossibile recuperare il CDR';
  		 -- Recuperiamo il cdr realtivo all'ente
     	lCdr := cnrctb020.GETCDRENTE;

	 	gMsgLog := 'Impossibile recuperare il progressivo per l''impegno';
		 -- Recuperiamo il progressivo dell'obbligazione che deve essere inserita
		select nvl(max(substr(to_char(pg_obbligazione),5,6)),0)
 		into lPgObbIniziale
		from obbligazione
		where cd_Cds = lCds.cd_unita_organizzativa
		and   esercizio=gcEsercizio
		and   cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP
		and   riportato = 'Y';

		 -- Recuperia il terzo di default.
		begin
			 select im01
			 into lCdTerzo
			 from configurazione_cnr
			 where cd_chiave_primaria = 'TERZO_SPECIALE'
			 and cd_chiave_secondaria = 'CODICE_DIVERSI_IMPEGNI';
		exception when no_data_found then
				   gMsgLog := 'Nella tabella CONFIGURAZIONE_CNR manca : TERZO_SPECIALE per CODICE_DIVERSI_IMPEGNI';
		 		   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
		end;


		lDataMigrazione := sysdate;
		lContatore :=0;

		for lContatoreRighe in 1..25
		loop
-- Punto di ritorno
savepoint altro_impegno;

		         lContatore := lContatore + 1;
			 	 gMsgLog := 'Impossibile recuperare il progressivo per l''impegno ';
		         ibmutl200.logInf(gPgLog ,'Processando residuo Passivo num '|| lContatore , ' Esercizio = '|| lResiduiPassiviPGiro(lContatoreRighe).ESERCIZIO || ' - Appart = ''C'' - Gest = ''S'' - voce = ' || lResiduiPassiviPGiro(lContatoreRighe).CAPITOLO, '');

				 -- Recuperiamo il progressivo dell'obbligazione che deve essere inserita
				 select nvl(max(substr(to_char(pg_obbligazione),5,6)),0) + 1
		 		 into lPgObbIniziale
				 from obbligazione
				 where cd_Cds                               = lCds.cd_unita_organizzativa
				 and   esercizio						    = gcEsercizio
				 and   esercizio_originale = lResiduiPassiviPGiro(lContatoreRighe).esercizio
				 and   cd_tipo_documento_cont 			    = CNRCTB018.TI_DOC_IMP
				 and   riportato 						    = 'Y'
				 and   substr(to_char(pg_obbligazione),1,4) = lResiduiPassiviPGiro(lContatoreRighe).esercizio ;

				 -- Numero dei Impegni residui inserti in OBBLIGAZIONE
			 	 lNumResiduiPasSci := lNumResiduiPasSci + 1;

	   		 	 -- Recuperiamo l'elemento voce = cd_titolo_capitolo della tabella VOCE_F
  			     gMsgLog := 'Voce finanziaria , cd_voce = ' || lResiduiPassiviPGiro(lContatoreRighe).CAPITOLO || ' ti_appartenenza = ''C'' ti_gestione = ''S'' esercizio = ' || gcEsercizio || ' non trovata';
			 	 select cd_titolo_capitolo
				 into lCdTitoloCapitolo
			     from voce_f
				 where ESERCIZIO 	   = gcEsercizio
				 and   ti_appartenenza = 'C'
				 and   ti_gestione 	   = 'S'
				 and   CD_VOCE 		   = lResiduiPassiviPGiro(lContatoreRighe).CAPITOLO
				 and   fl_mastrino 	   = 'Y';

			   	 gMsgLog := 'Elemento Voce , cd_elemento_voce = ' || lCdTitoloCapitolo || ' ti_appartenenza = ''C'' ti_gestione = ''S'' esercizio = ' || gcEsercizio || ' non trovato';
				 select *
		  		 into lElemVoce
				 from elemento_voce
				 where ESERCIZIO 	    = gcEsercizio
				 and   ti_appartenenza  = 'C'
				 and   ti_gestione 	   	= 'S'
				 and   cd_elemento_voce = lCdTitoloCapitolo;


				 lPgObbligazione := to_number(to_char(lResiduiPassiviPGiro(lContatoreRighe).esercizio) || lpad(to_char(lPgObbIniziale ),6,'0'));

				 lImpegno.CD_CDS                       := lCds.cd_unita_organizzativa;
				 lImpegno.ESERCIZIO				   	   := gcEsercizio;
				 lImpegno.ESERCIZIO_ORIGINALE  	       := lResiduiPassiviPGiro(lContatoreRighe).esercizio;
				 lImpegno.PG_OBBLIGAZIONE 			   := lPgObbligazione;
		 		 lImpegno.CD_TIPO_DOCUMENTO_CONT 	   := CNRCTB018.TI_DOC_IMP;
				 lImpegno.CD_UNITA_ORGANIZZATIVA       := lUo.cd_unita_organizzativa;
				 lImpegno.CD_CDS_ORIGINE			   := substr(lResiduiPassiviPGiro(lContatoreRighe).UO,1,3);
				 lImpegno.CD_UO_ORIGINE			   	   := lResiduiPassiviPGiro(lContatoreRighe).UO;
				 lImpegno.CD_TIPO_OBBLIGAZIONE 		   :=  NULL;
				 lImpegno.TI_APPARTENENZA              := 'C';
				 lImpegno.TI_GESTIONE			   	   := 'S';
				 lImpegno.CD_ELEMENTO_VOCE		   	   := lElemVoce.cd_elemento_voce; --- ?????????????
				 lImpegno.DT_REGISTRAZIONE		   	   := to_date('0101'||gcEsercizioDest,'ddmmyyyy');
				 lImpegno.DS_OBBLIGAZIONE              := 'IMPEGNO RESIDUO DA MIGRAZIONE';
				 lImpegno.NOTE_OBBLIGAZIONE            := NULL;
				 lImpegno.CD_TERZO				   	   := lCdTerzo;
				 lImpegno.IM_OBBLIGAZIONE		   	   := lResiduiPassiviPGiro(lContatoreRighe).IMPORTO;
				 lImpegno.IM_COSTI_ANTICIPATI		   := 0;
				 lImpegno.ESERCIZIO_COMPETENZA		   := gcEsercizio;
				 lImpegno.STATO_OBBLIGAZIONE		   := 'D';
				 lImpegno.DT_CANCELLAZIONE             := NULL;
				 lImpegno.CD_RIFERIMENTO_CONTRATTO     := NULL;
				 lImpegno.DT_SCADENZA_CONTRATTO	   	   := NULL;
				 lImpegno.FL_CALCOLO_AUTOMATICO		   := 'N';
				 lImpegno.CD_FONDO_RICERCA		   	   := NULL;
				 lImpegno.FL_SPESE_COSTI_ALTRUI		   := 'N';
				 lImpegno.FL_PGIRO				   	   := lElemVoce.FL_PARTITA_GIRO;
				 lImpegno.RIPORTATO				   	   := 'Y';
				 lImpegno.DACR					   	   := lDataMigrazione;
				 lImpegno.UTCR					   	   := cgUtente;
				 lImpegno.DUVA					   	   := lDataMigrazione;
				 lImpegno.UTUV					   	   := cgUtente;
				 lImpegno.PG_VER_REC				   := 1;
				 lImpegno.CD_CDS_ORI_RIPORTO    	   := NULL;
				 lImpegno.ESERCIZIO_ORI_RIPORTO	   	   := NULL;
				 lImpegno.PG_OBBLIGAZIONE_ORI_RIPORTO  := NULL;

				 lImpegnoScad.CD_CDS                      := lCds.cd_unita_organizzativa;
				 lImpegnoScad.ESERCIZIO 		          := lImpegno.ESERCIZIO;
				 lImpegnoScad.ESERCIZIO_ORIGINALE  	       := lImpegno.ESERCIZIO_ORIGINALE;
				 lImpegnoScad.PG_OBBLIGAZIONE 	          := lImpegno.PG_OBBLIGAZIONE;
				 lImpegnoScad.PG_OBBLIGAZIONE_SCADENZARIO := 1;
				 lImpegnoScad.DT_SCADENZA 	              := to_Date('3112'||gcEsercizio,'ddmmyyyy');
				 lImpegnoScad.DS_SCADENZA 	 			  := 'SCADENZA UNICA PER RESIDUO DI MIGRAZIONE';
				 lImpegnoScad.IM_SCADENZA 	 			  := lImpegno.IM_OBBLIGAZIONE;
				 lImpegnoScad.IM_ASSOCIATO_DOC_AMM        := 0;
				 lImpegnoScad.IM_ASSOCIATO_DOC_CONTABILE  := 0;
				 lImpegnoScad.DACR       				  := lDataMigrazione;
				 lImpegnoScad.UTCR 		 				  := cgUtente;
				 lImpegnoScad.DUVA 		 				  := lDataMigrazione;
				 lImpegnoScad.UTUV 		 				  := cgUtente;
				 lImpegnoScad.PG_VER_REC 				  := 1;

				 lImpegnoScadVoce.CD_CDS                      := lCds.cd_unita_organizzativa;
				 lImpegnoScadVoce.ESERCIZIO 		 		  := lImpegno.ESERCIZIO;
				 lImpegnoScadVoce.ESERCIZIO_ORIGINALE  	       := lImpegno.ESERCIZIO_ORIGINALE;
				 lImpegnoScadVoce.PG_OBBLIGAZIONE 			  := lImpegno.PG_OBBLIGAZIONE;
				 lImpegnoScadVoce.PG_OBBLIGAZIONE_SCADENZARIO := lImpegnoScad.PG_OBBLIGAZIONE_SCADENZARIO;
				 lImpegnoScadVoce.TI_APPARTENENZA 			  := 'C';
				 lImpegnoScadVoce.TI_GESTIONE 	 			  := 'S';
				 lImpegnoScadVoce.CD_VOCE 		 			  := lResiduiPassiviPGiro(lContatoreRighe).CAPITOLO;
				 lImpegnoScadVoce.CD_CENTRO_RESPONSABILITA 	  := lCdr.cd_centro_Responsabilita;
				 lImpegnoScadVoce.CD_LINEA_ATTIVITA 		  := CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
				 lImpegnoScadVoce.IM_VOCE 		   			  := lImpegno.IM_OBBLIGAZIONE;
				 lImpegnoScadVoce.CD_FONDO_RICERCA  		  := NULL;
				 lImpegnoScadVoce.DACR       				  := lDataMigrazione;
				 lImpegnoScadVoce.UTCR 						  := cgUtente;
				 lImpegnoScadVoce.DUVA 						  := lDataMigrazione;
				 lImpegnoScadVoce.UTUV      				  := cgUtente;
				 lImpegnoScadVoce.PG_VER_REC 				  := 1;

			 	 cnrctb035.INS_OBBLIGAZIONE(lImpegno);
				 cnrctb035.INS_OBBLIGAZIONE_SCADENZARIO(lImpegnoScad);
				 cnrctb035.INS_OBBLIGAZIONE_SCAD_VOCE(lImpegnoScadVoce);

	             begin
					 lResiduo.CD_CDS                   := lImpegno.CD_CDS;
					 lResiduo.ESERCIZIO				   := gcEsercizioDest;
	        			 lResiduo.ESERCIZIO_ORIGINALE  	       := lImpegno.ESERCIZIO_ORIGINALE;
					 lResiduo.PG_OBBLIGAZIONE 		   := lImpegno.PG_OBBLIGAZIONE;
					 lResiduo.CD_TIPO_DOCUMENTO_CONT   := cnrctb018.TI_DOC_IMP_RES;
					 lResiduo.CD_UNITA_ORGANIZZATIVA   := lImpegno.CD_UNITA_ORGANIZZATIVA;
					 lResiduo.CD_CDS_ORIGINE		   := lImpegno.CD_CDS_ORIGINE;
					 lResiduo.CD_UO_ORIGINE			   := lImpegno.CD_UO_ORIGINE;
					 lResiduo.CD_TIPO_OBBLIGAZIONE     := lImpegno.CD_TIPO_OBBLIGAZIONE;
					 lResiduo.TI_APPARTENENZA          := lImpegno.TI_APPARTENENZA;
					 lResiduo.TI_GESTIONE			   := lImpegno.TI_GESTIONE;
					 lResiduo.CD_ELEMENTO_VOCE		   := lImpegno.CD_ELEMENTO_VOCE;
					 lResiduo.DT_REGISTRAZIONE		   := lImpegno.DT_REGISTRAZIONE;
					 lResiduo.DS_OBBLIGAZIONE          := lImpegno.DS_OBBLIGAZIONE;
					 lResiduo.NOTE_OBBLIGAZIONE        := lImpegno.NOTE_OBBLIGAZIONE;
					 lResiduo.CD_TERZO				   := lImpegno.CD_TERZO;
					 lResiduo.IM_OBBLIGAZIONE		   := lImpegno.IM_OBBLIGAZIONE;
					 lResiduo.IM_COSTI_ANTICIPATI	   := lImpegno.IM_COSTI_ANTICIPATI;
					 lResiduo.ESERCIZIO_COMPETENZA	   := gcEsercizioDest;
					 lResiduo.STATO_OBBLIGAZIONE	   := lImpegno.STATO_OBBLIGAZIONE;
					 lResiduo.DT_CANCELLAZIONE         := lImpegno.DT_CANCELLAZIONE;
					 lResiduo.CD_RIFERIMENTO_CONTRATTO := lImpegno.CD_RIFERIMENTO_CONTRATTO;
					 lResiduo.DT_SCADENZA_CONTRATTO	   := lImpegno.DT_SCADENZA_CONTRATTO;
					 lResiduo.FL_CALCOLO_AUTOMATICO	   := lImpegno.FL_CALCOLO_AUTOMATICO;
					 lResiduo.CD_FONDO_RICERCA		   := lImpegno.CD_FONDO_RICERCA;
					 lResiduo.FL_SPESE_COSTI_ALTRUI	   := lImpegno.FL_SPESE_COSTI_ALTRUI;
					 lResiduo.FL_PGIRO				   := lImpegno.FL_PGIRO;
					 lResiduo.RIPORTATO				   := 'N';
					 lResiduo.DACR					   := lDataMigrazione;
					 lResiduo.UTCR					   := cgUtente;
					 lResiduo.DUVA					   := lDataMigrazione;
					 lResiduo.UTUV					   := cgUtente;
					 lResiduo.PG_VER_REC			   := 1;
					 lResiduo.CD_CDS_ORI_RIPORTO       := lImpegno.CD_CDS;
					 lResiduo.ESERCIZIO_ORI_RIPORTO	   := lImpegno.ESERCIZIO;
					 lResiduo.PG_OBBLIGAZIONE_ORI_RIPORTO := lImpegno.PG_OBBLIGAZIONE;

				 	 lResiduoScadenza.CD_CDS                      := lCds.cd_unita_organizzativa;
				 	 lResiduoScadenza.ESERCIZIO 		          := lResiduo.ESERCIZIO;
	        			 lResiduoScadenza.ESERCIZIO_ORIGINALE  	       := lResiduo.ESERCIZIO_ORIGINALE;
				 	 lResiduoScadenza.PG_OBBLIGAZIONE 	          := lResiduo.PG_OBBLIGAZIONE;
				 	 lResiduoScadenza.PG_OBBLIGAZIONE_SCADENZARIO := 1;
				 	 lResiduoScadenza.DT_SCADENZA 	              := to_Date('3112'||gcEsercizioDest,'ddmmyyyy');
				 	 lResiduoScadenza.DS_SCADENZA 	 			  := 'Scadenza riporto';
				 	 lResiduoScadenza.IM_SCADENZA 	 			  := lResiduo.IM_OBBLIGAZIONE;
				 	 lResiduoScadenza.IM_ASSOCIATO_DOC_AMM        := 0;
				 	 lResiduoScadenza.IM_ASSOCIATO_DOC_CONTABILE  := 0;
				 	 lResiduoScadenza.DACR       				  := lDataMigrazione;
				 	 lResiduoScadenza.UTCR 		 				  := cgUtente;
				 	 lResiduoScadenza.DUVA 		 				  := lDataMigrazione;
				 	 lResiduoScadenza.UTUV 		 				  := cgUtente;
				 	 lResiduoScadenza.PG_VER_REC 				  := 1;

					 lResiduoScadVoce.CD_CDS                       := lResiduoScadenza.cd_cds;
					 lResiduoScadVoce.ESERCIZIO					   := lResiduoScadenza.esercizio;
	        			 lResiduoScadVoce.ESERCIZIO_ORIGINALE  	       := lResiduoScadenza.ESERCIZIO_ORIGINALE;
					 lResiduoScadVoce.PG_OBBLIGAZIONE			   := lResiduoScadenza.pg_obbligazione;
					 lResiduoScadVoce.PG_obbligazione_scadenzario  := lResiduoScadenza.pg_obbligazione_Scadenzario;
					 lResiduoScadVoce.CD_CENTRO_RESPONSABILITA	   := lCdr.cd_centro_responsabilita; -- chiedere
					 lResiduoScadVoce.CD_LINEA_ATTIVITA			   := lImpegnoScadVoce.CD_LINEA_ATTIVITA; -- chiedere
					 lResiduoScadVoce.TI_APPARTENENZA			   := 'C';
					 lResiduoScadVoce.TI_GESTIONE			   	   := 'S';
					 lResiduoScadVoce.CD_VOCE			   	   	   := lResiduiPassiviPGiro(lContatoreRighe).CAPITOLO;
					 lResiduoScadVoce.IM_VOCE					   := lResiduoScadenza.im_scadenza;
					 lResiduoScadVoce.CD_FONDO_RICERCA			   := NULL;
					 lResiduoScadVoce.DACR						   := lDataMigrazione;
					 lResiduoScadVoce.UTCR						   := cgUtente;
					 lResiduoScadVoce.DUVA						   := lDataMigrazione;
					 lResiduoScadVoce.UTUV						   := cgUtente;
					 lResiduoScadVoce.PG_VER_REC				   := 1;

					 cnrctb035.INS_OBBLIGAZIONE(lResiduo);
					 cnrctb035.INS_OBBLIGAZIONE_SCADENZARIO(lResiduoScadenza);
					 cnrctb035.INS_OBBLIGAZIONE_SCAD_VOCE(lResiduoScadVoce);

      				 -- Aggiornare voce_f_saldi_cmp
					 update voce_f_saldi_cmp
					 set  im_obblig_imp_acr = im_obblig_imp_acr + lResiduiPassiviPGiro(lContatoreRighe).IMPORTO,
					      IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr + lResiduiPassiviPGiro(lContatoreRighe).IMPORTO,
						  utuv				= lResiduo.utuv,
						  duva				= lResiduo.duva,
						  pg_ver_rec		= pg_ver_rec + 1
					 where esercizio 	 = lResiduo.esercizio
				     and cd_cds 	     = lResiduo.cd_cds
					 and ti_appartenenza = lResiduo.ti_appartenenza
					 and ti_gestione 	 = lResiduo.ti_gestione
					 and cd_voce 		 = lResiduiPassiviPGiro(lContatoreRighe).CAPITOLO
					 and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;

			 exception when no_data_found then
					   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
			 end;
		 end loop;
end;
/


