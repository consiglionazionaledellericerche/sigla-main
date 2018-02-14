CREATE OR REPLACE procedure migrazione_sospesi is
 aNewSospeso sospeso%rowtype;
 aUser varchar2(20);
 aTSNow date;
 aPgNumLog number;
 ultimo_prog varchar2(3);
 aDocGenerico documento_generico%rowtype;
 STATO_SOSPESO_INIZIALE CONSTANT CHAR(1):='I'; 
 STATO_SOSPESO_ASSEGNATO_CDS CONSTANT CHAR(1):='A';
 STATO_SOSPESO_IN_SOSPESO CONSTANT CHAR(1):='S';
 begin
 	aTSNow := sysdate;
	aUser := '$$$$$MIGSOSPESI$$$$$';
 	aPgNumLog := ibmutl200.LOGSTART('Migrazione Sospesi' , aUser, null, null);
--------------------------------------------------------------------------------
--               PARTE 1: SOSPESI DI ENTRATA CNR E SOSPESI CDS		
--
--   Per ogni sospeso creso un sospeso figlio e aggiorno il sospeso padre
--   Aggiorno gli eventuali riferimenti a questo sospeso nelle tabelle
--   SOSPESO_DET_ETR, SOSPESO_DET_USC, LETTERA_PAGAM_ESTERO, FONDO_ECONOMALE
--   per specificare la chiave del sospeso figlio
--------------------------------------------------------------------------------
    for aSospeso in 
	    ( select * from   sospeso a
		  where  a.ti_sospeso_riscontro = 'S' and
		         (( a.cd_cds = '999' and
				   a.ti_entrata_spesa = 'E') or
				   a.cd_cds <> '999') and
				   a.cd_sospeso_padre is null and				   
				   not exists ( select 1 from sospeso b
				   where b.ti_sospeso_riscontro = a.ti_sospeso_riscontro and
				         b.esercizio = a.esercizio and
						 b.cd_cds = a.cd_cds and
						 b.ti_entrata_spesa = a.ti_entrata_spesa and
						 b.cd_sospeso_padre = a.cd_sospeso)  
		  ) 
    loop
	begin
	   --- lock del record		  
       for anOldSospeso in 
	       ( select * 
		     from sospeso a 
		     where  a.ti_sospeso_riscontro = aSospeso.ti_sospeso_riscontro and
		            a.cd_cds =  aSospeso.cd_cds and
				    a.ti_entrata_spesa = aSospeso.ti_entrata_spesa and
					a.esercizio = aSospeso.esercizio and
					a.cd_sospeso = aSospeso.cd_sospeso and
				    not exists ( select 1 from sospeso b
				    where b.ti_sospeso_riscontro = a.ti_sospeso_riscontro and
				         b.esercizio = a.esercizio and
						 b.cd_cds = a.cd_cds and
						 b.ti_entrata_spesa = a.ti_entrata_spesa and
						 b.cd_sospeso_padre = a.cd_sospeso)
				for update nowait)		   
					
	   loop
	   --- salvo il record in una tabella temporanea 
        insert into SOSPESO_TMP (
          CD_CDS
         ,ESERCIZIO
         ,TI_ENTRATA_SPESA
         ,TI_SOSPESO_RISCONTRO
         ,CD_SOSPESO
         ,CD_CDS_ORIGINE
         ,CD_UO_ORIGINE
         ,DT_REGISTRAZIONE
         ,DS_ANAGRAFICO
         ,CAUSALE
         ,TI_CC_BI
         ,FL_STORNATO
         ,IM_SOSPESO
         ,IM_ASSOCIATO
         ,STATO_SOSPESO
         ,DACR
         ,UTCR
         ,UTUV
         ,DUVA
         ,PG_VER_REC
         ,IM_ASS_MOD_1210
         ,CD_SOSPESO_PADRE
         ,CD_PROPRIO_SOSPESO
        ) values (
          anOldSospeso.CD_CDS
         ,anOldSospeso.ESERCIZIO
         ,anOldSospeso.TI_ENTRATA_SPESA
         ,anOldSospeso.TI_SOSPESO_RISCONTRO
         ,anOldSospeso.CD_SOSPESO
         ,anOldSospeso.CD_CDS_ORIGINE
         ,anOldSospeso.CD_UO_ORIGINE
         ,anOldSospeso.DT_REGISTRAZIONE
         ,anOldSospeso.DS_ANAGRAFICO
         ,anOldSospeso.CAUSALE
         ,anOldSospeso.TI_CC_BI
         ,anOldSospeso.FL_STORNATO
         ,anOldSospeso.IM_SOSPESO
         ,anOldSospeso.IM_ASSOCIATO
         ,anOldSospeso.STATO_SOSPESO
         ,anOldSospeso.DACR
         ,anOldSospeso.UTCR
         ,anOldSospeso.UTUV
         ,anOldSospeso.DUVA
         ,anOldSospeso.PG_VER_REC
         ,anOldSospeso.IM_ASS_MOD_1210
         ,anOldSospeso.CD_SOSPESO_PADRE
         ,anOldSospeso.CD_PROPRIO_SOSPESO
         );
	   --- creo record figlio
         aNewSospeso.CD_CDS:=anOldSospeso.CD_CDS;
         aNewSospeso.ESERCIZIO:=anOldSospeso.ESERCIZIO;
         aNewSospeso.TI_ENTRATA_SPESA:=anOldSospeso.TI_ENTRATA_SPESA;
         aNewSospeso.TI_SOSPESO_RISCONTRO:=anOldSospeso.TI_SOSPESO_RISCONTRO;
         aNewSospeso.CD_SOSPESO:=anOldSospeso.CD_SOSPESO || '.001';
         aNewSospeso.CD_CDS_ORIGINE:=anOldSospeso.CD_CDS_ORIGINE;
         aNewSospeso.CD_UO_ORIGINE:=anOldSospeso.CD_UO_ORIGINE;
         aNewSospeso.DT_REGISTRAZIONE:=anOldSospeso.DT_REGISTRAZIONE;
         aNewSospeso.DS_ANAGRAFICO:=anOldSospeso.DS_ANAGRAFICO;
         aNewSospeso.CAUSALE:=anOldSospeso.CAUSALE;
         aNewSospeso.TI_CC_BI:=anOldSospeso.TI_CC_BI;
         aNewSospeso.FL_STORNATO:=anOldSospeso.FL_STORNATO;
         aNewSospeso.IM_SOSPESO:=anOldSospeso.IM_SOSPESO;
         aNewSospeso.IM_ASSOCIATO:=anOldSospeso.IM_ASSOCIATO;
         aNewSospeso.STATO_SOSPESO:=anOldSospeso.STATO_SOSPESO;
         aNewSospeso.DACR:=aTSNow;
         aNewSospeso.UTCR:=aUser;
         aNewSospeso.UTUV:=aUser;
         aNewSospeso.DUVA:=aTSNow;
         aNewSospeso.PG_VER_REC:=1;
         aNewSospeso.IM_ASS_MOD_1210:=anOldSospeso.IM_ASS_MOD_1210;
         aNewSospeso.CD_SOSPESO_PADRE:=anOldSospeso.CD_SOSPESO;
         aNewSospeso.CD_PROPRIO_SOSPESO:='001';
         CNRCTB038.INS_SOSPESO( aNewSospeso );
	 
	 --- aggiorno record padre
   	     update sospeso
         set cd_cds_origine = null,
   	          im_associato = 0,
			  im_ass_mod_1210 = 0,
			  stato_sospeso ='I',
	          duva = aTSNow,
	          utuv = aUser,
 	          pg_ver_rec = pg_ver_rec + 1
         where esercizio = anOldSospeso.esercizio and
                cd_cds = anOldSospeso.cd_cds and
                ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and
	            ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
                cd_sospeso = anOldSospeso.cd_sospeso;

  	     for aFondoEconomale in 
		      ( select * from fondo_economale 
                where esercizio = anOldSospeso.esercizio and
                      cd_cds = anOldSospeso.cd_cds and
                      ti_sr_sospeso = anOldSospeso.ti_sospeso_riscontro and
	                  ti_es_sospeso = anOldSospeso.ti_entrata_spesa and
                      cd_sospeso = anOldSospeso.cd_sospeso
  		  	    for update nowait) 
	     loop	
             update fondo_economale
             set cd_sospeso = cd_sospeso||'.001',
	             duva = aTSNow,
	             utuv = aUser,
 	             pg_ver_rec = pg_ver_rec + 1
             where cd_sospeso = aFondoEconomale.cd_sospeso and
	               cd_cds = aFondoEconomale.cd_cds and
	               esercizio = aFondoEconomale.esercizio and
	               ti_es_sospeso = aFondoEconomale.ti_es_sospeso and
	               ti_sr_sospeso = aFondoEconomale.ti_sr_sospeso;
	     end loop;

         for aLettera in 
		      ( select * from lettera_pagam_estero 
                where esercizio = anOldSospeso.esercizio and
                      cd_cds = anOldSospeso.cd_cds and
                      ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and
	                  ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
                      cd_sospeso = anOldSospeso.cd_sospeso
 				for update nowait) 
	     loop	
             update lettera_pagam_estero
             set cd_sospeso = cd_sospeso||'.001',
	             duva = aTSNow,
	             utuv = aUser,
 	             pg_ver_rec = pg_ver_rec + 1
             where cd_sospeso = aLettera.cd_sospeso and
	               cd_cds = aLettera.cd_cds and
	               esercizio = aLettera.esercizio and
	               ti_entrata_spesa = aLettera.ti_entrata_spesa and
	               ti_sospeso_riscontro = aLettera.ti_sospeso_riscontro;
	     end loop;
	   

         for aSosDetEtr in 
		      ( select * from sospeso_det_etr 
                where esercizio = anOldSospeso.esercizio and
                      cd_cds = anOldSospeso.cd_cds and
                      ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and
	                  ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
                      cd_sospeso = anOldSospeso.cd_sospeso
				for update nowait) 
	     loop	
             update sospeso_det_etr
             set cd_sospeso = cd_sospeso||'.001',
	             duva = aTSNow,
	             utuv = aUser,
 	             pg_ver_rec = pg_ver_rec + 1
             where cd_sospeso = aSosDetEtr.cd_sospeso and
	               cd_cds = aSosDetEtr.cd_cds and
	               esercizio = aSosDetEtr.esercizio and
	               ti_entrata_spesa = aSosDetEtr.ti_entrata_spesa and
	               ti_sospeso_riscontro = aSosDetEtr.ti_sospeso_riscontro;
	     end loop;
	
         for aSosDetUsc in 
		      ( select * from sospeso_det_usc 
                where esercizio = anOldSospeso.esercizio and
                      cd_cds = anOldSospeso.cd_cds and
                      ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and
	                  ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
                      cd_sospeso = anOldSospeso.cd_sospeso
 				for update nowait) 
 	     loop	
             update sospeso_det_usc
             set cd_sospeso = cd_sospeso||'.001',
	             duva = aTSNow,
	             utuv = aUser,
 	             pg_ver_rec = pg_ver_rec + 1
             where cd_sospeso = aSosDetUsc.cd_sospeso and
	               cd_cds = aSosDetUsc.cd_cds and
	               esercizio = aSosDetUsc.esercizio and
	               ti_entrata_spesa = aSosDetUsc.ti_entrata_spesa and
	               ti_sospeso_riscontro = aSosDetUsc.ti_sospeso_riscontro;
	     end loop;
	   end loop;	  
	   commit;
	exception when others then
	   rollback;
	   IBMUTL200.logErr(aPgNumLog, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,'Sospeso CDS:' || aNewSospeso.cd_cds || ' Entrata/Spesa: ' || aNewSospeso.ti_entrata_spesa || ' Codice: ' || aNewSospeso.cd_sospeso);
	end;   
    end loop;
--------------------------------------------------------------------------------
--               PARTE 2: SOSPESI DI SPESA CNR
--
-- Si possono verificare i seguenti 3 casi: Il caso 2 e 3 possono anche verificsrsi 
-- contemporaneamente
--
-- CASO 1 : sospeso associato ad una lettera di pagamento estero.
--          In questo caso, l'associazione ? sempre completa (cio? im_sospeso = im_ass_mod_1210)
--          E' necessario creare un sospeso figlio con importo dato dall'intero importo
--          del sospeso e stato ASSEGNATO_A_CDS e cds origine uguale al cds che ha emesso
--          il documento amministrativo generico collegato alla lettera di pagamento estera
--          associata al sospeso
--
-- CASO 2 : sospeso con ancora un residuo disponibile (anche pari all'intero importo del sospeso)
--          In questo caso im_sospeso > im_associato 
--          E' necessario creare un sospeso figlio con importo pari all'importo non ancora utilizzato
--          del sospeso (im_sospeso-im_associato) e stato INIZIALE
--
-- CASO 3 : sospeso associato (totalmente o parzialmente) a mandati.
--          In questo caso im_associato > 0 
--          E' necessario creare tanti sospesi figli quanti sono i diversi cds che hanno 
--          emesso mandati per l'Ente collegati a questo sospeso.
--          Ogni figlio ha im_sospeso uguale alla somma degli importi associati ai mandati
--          emessi da uno stesso cds e stato ASSEGNATO_A_CDS e cds origine uguale al cds che ha emesso
--          i mandati
--
--
 		
--
--------------------------------------------------------------------------------
    for aSospeso in 
	    ( select * from   sospeso a
		  where  a.ti_sospeso_riscontro = 'S' and
		         a.cd_cds = '999' and
				 a.ti_entrata_spesa = 'S' and
			     a.cd_sospeso_padre is null and				 
				 not exists ( select 1 from sospeso b
				 where   b.ti_sospeso_riscontro = a.ti_sospeso_riscontro and
				         b.esercizio = a.esercizio and
						 b.cd_cds = a.cd_cds and
						 b.ti_entrata_spesa = a.ti_entrata_spesa and
						 b.cd_sospeso_padre = a.cd_sospeso)  
		  ) 
    loop
	begin
	   --- lock del record
       for anOldSospeso in 
	       ( select * 
		     from sospeso a 
		     where  a.ti_sospeso_riscontro = aSospeso.ti_sospeso_riscontro and
		            a.cd_cds =  aSospeso.cd_cds and
				    a.ti_entrata_spesa = aSospeso.ti_entrata_spesa and
					a.esercizio = aSospeso.esercizio and
					a.cd_sospeso = aSospeso.cd_sospeso and
				    not exists ( select 1 from sospeso b
				    where b.ti_sospeso_riscontro = a.ti_sospeso_riscontro and
				         b.esercizio = a.esercizio and
						 b.cd_cds = a.cd_cds and
						 b.ti_entrata_spesa = a.ti_entrata_spesa and
						 b.cd_sospeso_padre = a.cd_sospeso)
				for update nowait)		   
					
	   loop
	   --- salvo il record in una tabella temporanea
        insert into SOSPESO_TMP (
          CD_CDS
         ,ESERCIZIO
         ,TI_ENTRATA_SPESA
         ,TI_SOSPESO_RISCONTRO
         ,CD_SOSPESO
         ,CD_CDS_ORIGINE
         ,CD_UO_ORIGINE
         ,DT_REGISTRAZIONE
         ,DS_ANAGRAFICO
         ,CAUSALE
         ,TI_CC_BI
         ,FL_STORNATO
         ,IM_SOSPESO
         ,IM_ASSOCIATO
         ,STATO_SOSPESO
         ,DACR
         ,UTCR
         ,UTUV
         ,DUVA
         ,PG_VER_REC
         ,IM_ASS_MOD_1210
         ,CD_SOSPESO_PADRE
         ,CD_PROPRIO_SOSPESO
        ) values (
          anOldSospeso.CD_CDS
         ,anOldSospeso.ESERCIZIO
         ,anOldSospeso.TI_ENTRATA_SPESA
         ,anOldSospeso.TI_SOSPESO_RISCONTRO
         ,anOldSospeso.CD_SOSPESO
         ,anOldSospeso.CD_CDS_ORIGINE
         ,anOldSospeso.CD_UO_ORIGINE
         ,anOldSospeso.DT_REGISTRAZIONE
         ,anOldSospeso.DS_ANAGRAFICO
         ,anOldSospeso.CAUSALE
         ,anOldSospeso.TI_CC_BI
         ,anOldSospeso.FL_STORNATO
         ,anOldSospeso.IM_SOSPESO
         ,anOldSospeso.IM_ASSOCIATO
         ,anOldSospeso.STATO_SOSPESO
         ,anOldSospeso.DACR
         ,anOldSospeso.UTCR
         ,anOldSospeso.UTUV
         ,anOldSospeso.DUVA
         ,anOldSospeso.PG_VER_REC
         ,anOldSospeso.IM_ASS_MOD_1210
         ,anOldSospeso.CD_SOSPESO_PADRE
         ,anOldSospeso.CD_PROPRIO_SOSPESO
         );
  	      ultimo_prog := '000';	
       --- caso 1: sospeso associato a lettera pagamento estero 	
          if anOldSospeso.im_ass_mod_1210 > 0  then
	   
	         select b.*
		     into aDocGenerico 
		     from lettera_pagam_estero a, documento_generico b 
		     where a.cd_cds = anOldSospeso.cd_cds and
		           a.esercizio = anOldSospeso.esercizio  and
				   a.ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and
				   a.ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
				   a.cd_sospeso = anOldSospeso.cd_sospeso and
				   a.cd_cds = b.cd_cds and
				   a.cd_unita_organizzativa = b.cd_unita_organizzativa and
				   a.esercizio = b.esercizio and
				   a.esercizio = b.esercizio_lettera and
				   a.pg_lettera = b.pg_lettera;
				 
				 				
             ultimo_prog := cnrctb038.nextProgressivoSospeso( ultimo_prog );
             aNewSospeso.CD_CDS:=anOldSospeso.CD_CDS;
             aNewSospeso.ESERCIZIO:=anOldSospeso.ESERCIZIO;
             aNewSospeso.TI_ENTRATA_SPESA:=anOldSospeso.TI_ENTRATA_SPESA;
             aNewSospeso.TI_SOSPESO_RISCONTRO:=anOldSospeso.TI_SOSPESO_RISCONTRO;
             aNewSospeso.CD_SOSPESO:=anOldSospeso.CD_SOSPESO || '.'|| ultimo_prog;
             aNewSospeso.DT_REGISTRAZIONE:=anOldSospeso.DT_REGISTRAZIONE;
             aNewSospeso.DS_ANAGRAFICO:=anOldSospeso.DS_ANAGRAFICO;
             aNewSospeso.CAUSALE:=anOldSospeso.CAUSALE;
             aNewSospeso.TI_CC_BI:=anOldSospeso.TI_CC_BI;
             aNewSospeso.FL_STORNATO:=anOldSospeso.FL_STORNATO;
             aNewSospeso.IM_SOSPESO:=anOldSospeso.im_sospeso;
             aNewSospeso.IM_ASSOCIATO:=anOldSospeso.im_associato;
             aNewSospeso.STATO_SOSPESO:=STATO_SOSPESO_ASSEGNATO_CDS;
             aNewSospeso.CD_CDS_ORIGINE:=aDocGenerico.cd_cds_origine;
             aNewSospeso.CD_UO_ORIGINE:=anOldSospeso.CD_UO_ORIGINE;
             aNewSospeso.DACR:=aTSNow;
             aNewSospeso.UTCR:=aUser;
             aNewSospeso.UTUV:=aUser;
             aNewSospeso.DUVA:=aTSNow;
             aNewSospeso.PG_VER_REC:=1;
             aNewSospeso.IM_ASS_MOD_1210:=anOldSospeso.IM_ASS_MOD_1210;
             aNewSospeso.CD_SOSPESO_PADRE:=anOldSospeso.CD_SOSPESO;
             aNewSospeso.CD_PROPRIO_SOSPESO:=ultimo_prog;
             CNRCTB038.INS_SOSPESO( aNewSospeso );		  

			   
             for aLetteraPagam in 
		         ( select * 
				   from lettera_pagam_estero 
                   where  esercizio = anOldSospeso.esercizio and
                          cd_cds = anOldSospeso.cd_cds and
                          ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and
	                      ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
                          cd_sospeso = anOldSospeso.cd_sospeso    						  
 				   for update nowait)
 	         loop	
                    update lettera_pagam_estero
                    set cd_sospeso = aNewSospeso.cd_sospeso,
	                    duva = aTSNow,
	                    utuv = aUser,
 	                    pg_ver_rec = pg_ver_rec + 1
                    where cd_cds = aLetteraPagam.cd_cds and
	                    esercizio = aLetteraPagam.esercizio and
						cd_unita_organizzativa = aLetteraPagam.cd_unita_organizzativa and
						pg_lettera = aLetteraPagam.pg_lettera;
	         end loop;
	      end if;
	  	 
		
          if anOldSospeso.im_sospeso - anOldSospeso.Im_associato > 0 and 
	         anOldSospeso.im_ass_mod_1210 = 0 then
-- caso 2: sospeso con una parte di importo non ancora utilizzata 	   
--         creo record figlio per la parte di sospeso non ancora utilizzata
--         con stato INIZIALE e importo = importo_sospeso - im_associato

             ultimo_prog := cnrctb038.nextProgressivoSospeso( ultimo_prog );
             aNewSospeso.CD_CDS:=anOldSospeso.CD_CDS;
             aNewSospeso.ESERCIZIO:=anOldSospeso.ESERCIZIO;
             aNewSospeso.TI_ENTRATA_SPESA:=anOldSospeso.TI_ENTRATA_SPESA;
             aNewSospeso.TI_SOSPESO_RISCONTRO:=anOldSospeso.TI_SOSPESO_RISCONTRO;
             aNewSospeso.CD_SOSPESO:=anOldSospeso.CD_SOSPESO || '.' || ultimo_prog;
             aNewSospeso.DT_REGISTRAZIONE:=anOldSospeso.DT_REGISTRAZIONE;
             aNewSospeso.DS_ANAGRAFICO:=anOldSospeso.DS_ANAGRAFICO;
             aNewSospeso.CAUSALE:=anOldSospeso.CAUSALE;
             aNewSospeso.TI_CC_BI:=anOldSospeso.TI_CC_BI;
             aNewSospeso.FL_STORNATO:=anOldSospeso.FL_STORNATO;
             aNewSospeso.IM_SOSPESO:=anOldSospeso.im_sospeso - anOldSospeso.Im_associato;
             aNewSospeso.IM_ASSOCIATO:=0;
             aNewSospeso.STATO_SOSPESO:=STATO_SOSPESO_INIZIALE;
             aNewSospeso.CD_CDS_ORIGINE:=null;
             aNewSospeso.CD_UO_ORIGINE:=anOldSospeso.CD_UO_ORIGINE;		  
             aNewSospeso.DACR:=aTSNow;
             aNewSospeso.UTCR:=aUser;
             aNewSospeso.UTUV:=aUser;
             aNewSospeso.DUVA:=aTSNow;
             aNewSospeso.PG_VER_REC:=1;
             aNewSospeso.IM_ASS_MOD_1210:=anOldSospeso.IM_ASS_MOD_1210;
             aNewSospeso.CD_SOSPESO_PADRE:=anOldSospeso.CD_SOSPESO;
             aNewSospeso.CD_PROPRIO_SOSPESO:=ultimo_prog;
             CNRCTB038.INS_SOSPESO( aNewSospeso );		  
	      end if;
-- caso 3: sospeso gi? utilizzato in mandati 	   
--         raggruppo gli importi associati in base al cds origine del mandato 
--         e creo tanti sospesi figli, uno per ogni cds_origine con stato uguale ad
--         ASSEGNATO_CDS e importo pari al raggruppamento degli importi per quel CDS
          if anOldSospeso.im_associato > 0 then
	         for aSospesoPerCds in
	            ( select * 
                  from vs_sospeso_spesa_cnr_per_cds 
                  where cd_cds = anOldSospeso.cd_cds and
			            esercizio = anOldSospeso.esercizio and
                        ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and 
                        ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
		    			cd_sospeso = anOldSospeso.cd_sospeso )
		     loop
                ultimo_prog := cnrctb038.nextProgressivoSospeso( ultimo_prog );
                aNewSospeso.CD_CDS:=anOldSospeso.CD_CDS;
                aNewSospeso.ESERCIZIO:=anOldSospeso.ESERCIZIO;
                aNewSospeso.TI_ENTRATA_SPESA:=anOldSospeso.TI_ENTRATA_SPESA;
                aNewSospeso.TI_SOSPESO_RISCONTRO:=anOldSospeso.TI_SOSPESO_RISCONTRO;
                aNewSospeso.CD_SOSPESO:=anOldSospeso.CD_SOSPESO || '.' || ultimo_prog;
                aNewSospeso.DT_REGISTRAZIONE:=anOldSospeso.DT_REGISTRAZIONE;
                aNewSospeso.DS_ANAGRAFICO:=anOldSospeso.DS_ANAGRAFICO;
                aNewSospeso.CAUSALE:=anOldSospeso.CAUSALE;
                aNewSospeso.TI_CC_BI:=anOldSospeso.TI_CC_BI;
                aNewSospeso.FL_STORNATO:=anOldSospeso.FL_STORNATO;
                aNewSospeso.IM_SOSPESO:=aSospesoPerCds.im_associato;
                aNewSospeso.IM_ASSOCIATO:=aSospesoPerCds.im_associato;
                aNewSospeso.STATO_SOSPESO:=STATO_SOSPESO_ASSEGNATO_CDS;
                aNewSospeso.CD_CDS_ORIGINE:=aSospesoPerCds.cd_cds_origine;
                aNewSospeso.CD_UO_ORIGINE:=anOldSospeso.CD_UO_ORIGINE;		  
                aNewSospeso.DACR:=aTSNow;
                aNewSospeso.UTCR:=aUser;
                aNewSospeso.UTUV:=aUser;
                aNewSospeso.DUVA:=aTSNow;
                aNewSospeso.PG_VER_REC:=1;
                aNewSospeso.IM_ASS_MOD_1210:=anOldSospeso.IM_ASS_MOD_1210;
                aNewSospeso.CD_SOSPESO_PADRE:=anOldSospeso.CD_SOSPESO;
                aNewSospeso.CD_PROPRIO_SOSPESO:=ultimo_prog;
                CNRCTB038.INS_SOSPESO( aNewSospeso );		  

-- per tutti i dettagli collegati a mandati emessi dallo stesso CDS aggiorno il codice
-- del sospeso
             			   
                for aSosDetUsc in 
		         ( select a.* 
				   from sospeso_det_usc a, mandato b 
                   where  a.esercizio = anOldSospeso.esercizio and
                          a.cd_cds = anOldSospeso.cd_cds and
                          a.ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and
	                      a.ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
                          a.cd_sospeso = anOldSospeso.cd_sospeso and
                          a.cd_cds = b.cd_cds and
						  a.esercizio = b.esercizio and
						  a.pg_mandato = b.pg_mandato and
						  b.cd_cds_origine = aNewSospeso.cd_cds_origine						  
 				   for update nowait)
 	             loop	
                    update sospeso_det_usc
                    set cd_sospeso = aNewSospeso.cd_sospeso,
	                    duva = aTSNow,
	                    utuv = aUser,
 	                    pg_ver_rec = pg_ver_rec + 1
                    where cd_sospeso = aSosDetUsc.cd_sospeso and
	                    cd_cds = aSosDetUsc.cd_cds and
	                    esercizio = aSosDetUsc.esercizio and
	                    ti_entrata_spesa = aSosDetUsc.ti_entrata_spesa and
	                    ti_sospeso_riscontro = aSosDetUsc.ti_sospeso_riscontro and
					    pg_mandato = aSosDetUsc.pg_mandato;
	             end loop;
             end loop;
		   end if;
--aggiorno sospeso padre
           update sospeso
     	   set    cd_cds_origine = null,
   	              im_associato = 0,
		          im_ass_mod_1210 = 0,
			      stato_sospeso ='I',
	              duva = aTSNow,
	              utuv = aUser,
 	              pg_ver_rec = pg_ver_rec + 1
            where    esercizio = anOldSospeso.esercizio and
                     cd_cds = anOldSospeso.cd_cds and
                     ti_sospeso_riscontro = anOldSospeso.ti_sospeso_riscontro and
	                 ti_entrata_spesa = anOldSospeso.ti_entrata_spesa and
                     cd_sospeso = anOldSospeso.cd_sospeso;
       end loop;
	   commit;
	exception when others then
	   rollback;
	   IBMUTL200.logErr(aPgNumLog, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,'Sospeso CDS:' || aNewSospeso.cd_cds || ' Entrata/Spesa: ' || aNewSospeso.ti_entrata_spesa || ' Codice: ' || aNewSospeso.cd_sospeso);
	end;   
    end loop;
end;
/


