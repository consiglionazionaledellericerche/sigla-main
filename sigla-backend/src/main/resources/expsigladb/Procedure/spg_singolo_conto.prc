CREATE OR REPLACE PROCEDURE SPG_SINGOLO_CONTO
--
-- Date: 18/07/2006
-- Version: 1.7
--
-- Per stampa situazione singolo conto
--
--
-- History:
--
-- Date: 05/01/2004
-- Version: 1.0
-- Creazione
--
-- Date: 21/01/2004
-- Version: 1.1
-- Estrazione cd_terzo del doc cont
--
-- Date: 23/01/2004
-- Version: 1.2
-- Ottimizzazione
--
-- Date: 26/01/2004
-- Version: 1.3
-- Fix ricerca documenti contabili del solo cds
-- (oss: visibilit? doc cont sul cds, indipendente dall'uo di scrivania)
--
-- Date: 27/01/2004
-- Version: 1.4
-- Fix estrazione obb/acr, man/rev di competenza o residuo in funzione
-- del saldo selezionato
--
-- Date: 28/01/2004
-- Version: 1.5
-- Fix calcolo saldo 1210 non associati a sospesi
-- (con ottimizzazione query: non uso vista V_LETTERA_PAGAM_ESTERO_DOC e
--  vista V_DOC_AMM_OBB)
--
-- Date: 01/03/2004
-- Version: 1.6
-- Modificato importo mandato (errore n. 776)
-- Aggiunta colonna in pannello accertamenti/obbligazioni relativa
-- all'importo non associato a doc contabile (richiesta n. 778)
--
-- Date: 18/07/2006
-- Version: 1.7
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
(aIdRpt in number) is
 aId number;
i number := 0;
aVf voce_f%rowtype;
aIm1210noSosp number;
aTot number := 0;
aKey varchar2(100);
aImporto number := 0;
aCdTipoDocCont varchar2(10);
begin
	select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;

	for aVoce in (select * from v_stm_paramin_sing_conto
				  where id_report = aIdRpt) loop
		i := i + 1;
		aKey := aVoce.cd_cds||aVoce.esercizio||aVoce.ti_appartenenza||aVoce.ti_gestione||aVoce.cd_voce||aVoce.ti_competenza_residuo;

		select * into aVf
		from voce_f
		where esercizio  	  = aVoce.esercizio
		  and ti_appartenenza = aVoce.ti_appartenenza
		  and ti_gestione	  = aVoce.ti_gestione
		  and cd_voce		  = aVoce.cd_voce;

      for aSaldo in
	   (select ESERCIZIO,
               decode(TI_GESTIONE,
                      'S', CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita),
                      'E', CNRCTB020.getCDCDSENTE(ESERCIZIO)) CD_CDS,
               TI_APPARTENENZA,
               TI_GESTIONE,
               CD_ELEMENTO_VOCE,
               CD_VOCE,
               decode(esercizio, esercizio_res, 'C', 'R') TI_COMPETENZA_RESIDUO,
               sum(IM_STANZ_INIZIALE_A1) IM_STANZ_INIZIALE_A1,
               sum(VARIAZIONI_PIU) VARIAZIONI_PIU,
               sum(VARIAZIONI_MENO) VARIAZIONI_MENO,
               sum(IM_OBBL_ACC_COMP) IM_OBBL_ACC_COMP,
               sum(IM_OBBL_RES_IMP) IM_OBBL_RES_IMP,
               sum(IM_OBBL_RES_PRO) IM_OBBL_RES_PRO,
               sum(VAR_PIU_OBBL_RES_PRO) VAR_PIU_OBBL_RES_PRO,
               sum(VAR_MENO_OBBL_RES_PRO) VAR_MENO_OBBL_RES_PRO,
               sum(IM_STANZ_RES_IMPROPRIO) IM_STANZ_RES_IMPROPRIO,
               sum(VAR_PIU_STANZ_RES_IMP) VAR_PIU_STANZ_RES_IMP,
               sum(VAR_MENO_STANZ_RES_IMP)  VAR_MENO_STANZ_RES_IMP,
               sum(IM_MANDATI_REVERSALI_PRO) IM_MANDATI_REVERSALI_PRO,
               sum(IM_MANDATI_REVERSALI_IMP) IM_MANDATI_REVERSALI_IMP,
               sum(IM_PAGAMENTI_INCASSI) IM_PAGAMENTI_INCASSI
		from voce_f_saldi_cdr_linea
		where esercizio 			= aVoce.esercizio
		  and ti_appartenenza 		= aVoce.ti_appartenenza
		  and ti_gestione 			= aVoce.ti_gestione
		  and cd_voce 				= aVoce.cd_voce
          and ((CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita) = aVoce.cd_cds AND TI_GESTIONE = 'S') OR
               (CNRCTB020.getCDCDSENTE(ESERCIZIO) = aVoce.cd_cds AND TI_GESTIONE = 'E'))
          and ((esercizio = esercizio_res and aVoce.TI_COMPETENZA_RESIDUO = 'C') OR
               (esercizio > esercizio_res and aVoce.TI_COMPETENZA_RESIDUO = 'R'))
       group by ESERCIZIO, CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita), TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_VOCE,
                decode(esercizio, esercizio_res, 'C', 'R')
       UNION ALL
       -- SOLO PARTITE DI GIRO SPESA ISTITUTI
       select  obb.esercizio,
               obb.cd_cds,
               obb.TI_APPARTENENZA,
               obb.TI_GESTIONE,
               obb.CD_ELEMENTO_VOCE,
               obbsv.CD_VOCE,
               decode(obb.esercizio, obb.esercizio_originale, 'C', 'R') TI_COMPETENZA_RESIDUO,
               0 IM_STANZ_INIZIALE_A1,
               0 VARIAZIONI_PIU,
               0 VARIAZIONI_MENO,
               nvl(sum(decode(obb.esercizio, obb.esercizio_originale, obbsv.im_voce, 0)), 0) IM_OBBL_ACC_COMP,
               0 IM_OBBL_RES_IMP,
               nvl(sum(decode(obb.esercizio, obb.esercizio_originale, 0, obbsv.im_voce)), 0) IM_OBBL_RES_PRO,
               0 VAR_PIU_OBBL_RES_PRO,
               0 VAR_MENO_OBBL_RES_PRO,
               0 IM_STANZ_RES_IMPROPRIO,
               0 VAR_PIU_STANZ_RES_IMP,
               0 VAR_MENO_STANZ_RES_IMP,
               sum(CNRUTL002.IM_MANDATI_PER_IMPEGNO (OBBSV.CD_CDS, OBBSV.ESERCIZIO, OBBSV.ESERCIZIO_ORIGINALE, OBBSV.PG_OBBLIGAZIONE, NULL)),
               0 IM_MANDATI_REVERSALI_IMP,
               0 IM_PAGAMENTI_INCASSI
		from  obbligazione obb, obbligazione_scad_voce obbsv, obbligazione_scadenzario obbs
		where obbsv.esercizio 			   = aVoce.esercizio
		  and obbsv.ti_appartenenza		   = aVoce.ti_appartenenza
		  and obbsv.ti_gestione 		   = aVoce.ti_gestione
		  and obbsv.cd_voce 		       = aVoce.cd_voce
          and obb.fl_pgiro                 = 'Y'
          and obbsv.ti_appartenenza        = 'D'
          and OBB.PG_OBBLIGAZIONE          > 0
          and obb.stato_obbligazione      <> 'S' -- escludo obbligazioni stornate
          and obb.cd_cds                   = aVoce.cd_cds
          and obbs.cd_cds          	       = obb.cd_cds
	   	  and obbs.esercizio			   = obb.esercizio
	   	  and obbs.esercizio_originale	   = obb.esercizio_originale
		  and obbs.pg_obbligazione		   = obb.pg_obbligazione
		  and obbs.cd_cds 				       = obbsv.cd_cds
		  and obbs.esercizio 			       = obbsv.esercizio
		  and obbs.esercizio_originale	       = obbsv.esercizio_originale
		  and obbs.pg_obbligazione 		       = obbsv.pg_obbligazione
		  and obbs.pg_obbligazione_scadenzario = obbsv.pg_obbligazione_scadenzario
          and ((obb.esercizio = obb.esercizio_originale and aVoce.TI_COMPETENZA_RESIDUO = 'C') OR
               (obb.esercizio > obb.esercizio_originale and aVoce.TI_COMPETENZA_RESIDUO = 'R'))
       group by obb.ESERCIZIO, obb.cd_cds, obb.TI_APPARTENENZA, obb.TI_GESTIONE, CD_ELEMENTO_VOCE, CD_VOCE,
                decode(obb.esercizio, obb.esercizio_originale, 'C', 'R')) LOOP

		-- inserimento record di tipo A (info generali sul capitolo)

		-- calcolo del saldo 1210 non associati a sospesi (solo per spese)
		if aSaldo.ti_gestione = 'S' then

			if aSaldo.TI_COMPETENZA_RESIDUO = 'C' then

				select nvl(sum(totale),0) into aIm1210noSosp
				from (select nvl(sum(obbsv.im_voce),0) totale
					  from lettera_pagam_estero lett
					  	  ,documento_generico dg
						  ,documento_generico_riga dgr
						  ,obbligazione_scad_voce obbsv
						  ,obbligazione obb
					  where lett.CD_CDS	   		  	  = dg.CD_CDS
					    and lett.ESERCIZIO			          = dg.ESERCIZIO_LETTERA
						and lett.CD_UNITA_ORGANIZZATIVA		  = dg.CD_UNITA_ORGANIZZATIVA
						and lett.PG_lettera			  = dg.PG_LETTERA
						and dg.cd_cds				  = dgr.cd_cds
						and dg.cd_unita_organizzativa		  = dgr.cd_unita_organizzativa
						and dg.esercizio			  = dgr.esercizio
						and dg.cd_tipo_documento_amm		  = dgr.cd_tipo_documento_amm
						and dg.pg_documento_generico		  = dgr.pg_documento_generico
						and obbsv.CD_CDS			  = dgr.cd_cds_obbligazione
						and obbsv.ESERCIZIO			  = dgr.ESERCIZIO_OBBLIGAZIONE
						and obbsv.ESERCIZIO_ORIGINALE		  = dgr.ESERCIZIO_ORI_OBBLIGAZIONE
						and obbsv.PG_OBBLIGAZIONE		  = dgr.PG_OBBLIGAZIONE
						and obbsv.PG_OBBLIGAZIONE_SCADENZARIO = dgr.PG_OBBLIGAZIONE_SCADENZARIO
						and obbsv.ESERCIZIO					  = aVf.esercizio
						and obbsv.TI_APPARTENENZA			  = aVf.ti_appartenenza
						and obbsv.TI_GESTIONE				  = aVf.ti_gestione
						and obbsv.CD_VOCE					  = aVf.cd_voce
						and obb.cd_cds 				  = obbsv.cd_cds
						and obb.esercizio 			  = obbsv.esercizio
						and obb.esercizio_originale		  = obbsv.esercizio_originale
						and obb.pg_obbligazione 		  = obbsv.pg_obbligazione
						and obb.cd_tipo_documento_cont 		  <> 'IMP_RES'
					  union all
					  select nvl(sum(obbsv.im_voce),0) totale
					  from lettera_pagam_estero lett
					  	  ,fattura_passiva fp
						  ,fattura_passiva_riga fpr
						  ,obbligazione_scad_voce obbsv
						  ,obbligazione obb
					  where lett.CD_CDS	   		  	   		  = fp.CD_CDS
					    and lett.ESERCIZIO					  = fp.ESERCIZIO_LETTERA
						and lett.CD_UNITA_ORGANIZZATIVA		  = fp.CD_UNITA_ORGANIZZATIVA
						and lett.PG_lettera					  = fp.PG_LETTERA
						and fp.cd_cds						  = fpr.cd_cds
						and fp.cd_unita_organizzativa		  = fpr.cd_unita_organizzativa
						and fp.esercizio					  = fpr.esercizio
						and fp.pg_fattura_passiva	          = fpr.pg_fattura_passiva
						and obbsv.CD_CDS			  = fpr.cd_cds_obbligazione
						and obbsv.ESERCIZIO			  = fpr.ESERCIZIO_OBBLIGAZIONE
						and obbsv.ESERCIZIO_ORIGINALE		  = fpr.ESERCIZIO_ORI_OBBLIGAZIONE
						and obbsv.PG_OBBLIGAZIONE		  = fpr.PG_OBBLIGAZIONE
						and obbsv.PG_OBBLIGAZIONE_SCADENZARIO = fpr.PG_OBBLIGAZIONE_SCADENZARIO
						and obbsv.ESERCIZIO					  = aVf.esercizio
						and obbsv.TI_APPARTENENZA			  = aVf.ti_appartenenza
						and obbsv.TI_GESTIONE				  = aVf.ti_gestione
						and obbsv.CD_VOCE					  = aVf.cd_voce
						and obb.cd_cds 				  = obbsv.cd_cds
						and obb.esercizio 			  = obbsv.esercizio
						and obb.esercizio_originale		  = obbsv.esercizio_originale
						and obb.pg_obbligazione 	          = obbsv.pg_obbligazione
						and obb.cd_tipo_documento_cont 		  <> 'IMP_RES'
					  );
			else -- residuo
				select nvl(sum(totale),0) into aIm1210noSosp
				from (select nvl(sum(obbsv.im_voce),0) totale
					  from lettera_pagam_estero lett
					  	  ,documento_generico dg
						  ,documento_generico_riga dgr
						  ,obbligazione_scad_voce obbsv
						  ,obbligazione obb
					  where lett.CD_CDS	   		  	   		  = dg.CD_CDS
					    and lett.ESERCIZIO					  = dg.ESERCIZIO_LETTERA
						and lett.CD_UNITA_ORGANIZZATIVA		  = dg.CD_UNITA_ORGANIZZATIVA
						and lett.PG_lettera					  = dg.PG_LETTERA
						and dg.cd_cds						  = dgr.cd_cds
						and dg.cd_unita_organizzativa		  = dgr.cd_unita_organizzativa
						and dg.esercizio					  = dgr.esercizio
						and dg.cd_tipo_documento_amm		  = dgr.cd_tipo_documento_amm
						and dg.pg_documento_generico		  = dgr.pg_documento_generico
						and obbsv.CD_CDS			  = dgr.cd_cds_obbligazione
						and obbsv.ESERCIZIO			  = dgr.ESERCIZIO_OBBLIGAZIONE
						and obbsv.ESERCIZIO_ORIGINALE		  = dgr.ESERCIZIO_ORI_OBBLIGAZIONE
						and obbsv.PG_OBBLIGAZIONE		  = dgr.PG_OBBLIGAZIONE
						and obbsv.PG_OBBLIGAZIONE_SCADENZARIO = dgr.PG_OBBLIGAZIONE_SCADENZARIO
						and obbsv.ESERCIZIO					  = aVf.esercizio
						and obbsv.TI_APPARTENENZA			  = aVf.ti_appartenenza
						and obbsv.TI_GESTIONE				  = aVf.ti_gestione
						and obbsv.CD_VOCE					  = aVf.cd_voce
						and obb.cd_cds 				  = obbsv.cd_cds
						and obb.esercizio 			  = obbsv.esercizio
						and obb.esercizio_originale		  = obbsv.esercizio_originale
						and obb.pg_obbligazione 		  = obbsv.pg_obbligazione
						and obb.cd_tipo_documento_cont 		  = 'IMP_RES'
					  union all
					  select nvl(sum(obbsv.im_voce),0) totale
					  from lettera_pagam_estero lett
					  	  ,fattura_passiva fp
						  ,fattura_passiva_riga fpr
						  ,obbligazione_scad_voce obbsv
						  ,obbligazione obb
					  where lett.CD_CDS	   		  	   		  = fp.CD_CDS
					    and lett.ESERCIZIO					  = fp.ESERCIZIO_LETTERA
						and lett.CD_UNITA_ORGANIZZATIVA		  = fp.CD_UNITA_ORGANIZZATIVA
						and lett.PG_lettera					  = fp.PG_LETTERA
						and fp.cd_cds						  = fpr.cd_cds
						and fp.cd_unita_organizzativa		  = fpr.cd_unita_organizzativa
						and fp.esercizio			  = fpr.esercizio
						and fp.pg_fattura_passiva	          = fpr.pg_fattura_passiva
						and obbsv.CD_CDS			  = fpr.cd_cds_obbligazione
						and obbsv.ESERCIZIO			  = fpr.ESERCIZIO_OBBLIGAZIONE
						and obbsv.ESERCIZIO_ORIGINALE		  = fpr.ESERCIZIO_ORI_OBBLIGAZIONE
						and obbsv.PG_OBBLIGAZIONE		  = fpr.PG_OBBLIGAZIONE
						and obbsv.PG_OBBLIGAZIONE_SCADENZARIO = fpr.PG_OBBLIGAZIONE_SCADENZARIO
						and obbsv.ESERCIZIO			  = aVf.esercizio
						and obbsv.TI_APPARTENENZA		  = aVf.ti_appartenenza
						and obbsv.TI_GESTIONE			  = aVf.ti_gestione
						and obbsv.CD_VOCE			  = aVf.cd_voce
						and obb.cd_cds 				  = obbsv.cd_cds
						and obb.esercizio 			  = obbsv.esercizio
						and obb.esercizio_originale		  = obbsv.esercizio_originale
						and obb.pg_obbligazione 		  = obbsv.pg_obbligazione
						and obb.cd_tipo_documento_cont 		  = 'IMP_RES'
					  );
			end if; -- fine distinzione comp/res
		else -- entrate> non sono associate a 1210
			 aIm1210noSosp := 0;
		end if;

		insert into VPG_SINGOLO_CONTO  (ID,
										CHIAVE,
										TIPO,
										SEQUENZA,
										ESERCIZIO,
										CD_CDS,
										TI_APPARTENENZA,
										TI_GESTIONE,
                                        CD_ELEMENTO_VOCE,
                                        DS_ELEMENTO_VOCE,
										CD_VOCE,
										TI_COMPETENZA_RESIDUO,
										DS_CDS,
										DS_VOCE,
										IM_STANZ_INIZIALE_A1,
										VARIAZIONI_PIU,
										VARIAZIONI_MENO,
										IM_STANZ_ATTUALE,
										IM_OBBLIG_IMP_ACR,  -- IMPEGNATO A COMPETENZA / RES PRO ASSESTATO
                                        IM_OBBL_RES_IMP,  -- impegnato residuo IMPROPRIO
                                        VAR_PIU_OBBL_RES_PRO,
                                        VAR_MENO_OBBL_RES_PRO,
										IM_MANDATI_REVERSALI,
                                        IM_MANDATI_REVERSALI_IMP,
										IM_PAGAMENTI_INCASSI,
										IM_1210_NO_SOSPESI)
		select  aId
			   ,aKey
			   ,'A'
			   ,i
			   ,aSaldo.esercizio
			   ,aSaldo.cd_cds
			   ,aSaldo.ti_appartenenza
			   ,aSaldo.ti_gestione
               ,aSaldo.cd_elemento_voce
               ,(select ds_elemento_voce from elemento_voce where esercizio = aSaldo.esercizio and ti_appartenenza = aSaldo.ti_appartenenza and
                                                                  ti_gestione = aSaldo.ti_gestione and cd_elemento_voce = aSaldo.cd_elemento_voce)
			   ,aSaldo.cd_voce
			   ,aSaldo.ti_competenza_residuo
			   ,uo.DS_UNITA_ORGANIZZATIVA
			   ,aVf.ds_voce
			   ,decode(aSaldo.ti_competenza_residuo, 'C', aSaldo.IM_STANZ_INIZIALE_A1, 'R', aSaldo.IM_STANZ_RES_IMPROPRIO)  -- STANZ. INIZIALE
			   ,decode(aSaldo.ti_competenza_residuo, 'C', aSaldo.VARIAZIONI_PIU, 'R', aSaldo.VAR_PIU_STANZ_RES_IMP)  -- VAR PIU'
			   ,decode(aSaldo.ti_competenza_residuo, 'C', aSaldo.VARIAZIONI_MENO, 'R', aSaldo.VAR_MENO_STANZ_RES_IMP) -- VAR MENO
			   ,decode(aSaldo.ti_competenza_residuo,
                       'C', aSaldo.IM_STANZ_INIZIALE_A1 + aSaldo.VARIAZIONI_PIU - aSaldo.VARIAZIONI_MENO,
                       'R', aSaldo.IM_STANZ_RES_IMPROPRIO + aSaldo.VAR_PIU_STANZ_RES_IMP - aSaldo.VAR_MENO_STANZ_RES_IMP) -- ASSESTATO
			   ,decode(aSaldo.ti_competenza_residuo,
                       'C', aSaldo.IM_OBBL_ACC_COMP,
                       'R', aSaldo.IM_OBBL_RES_PRO) --  RESIDUO PROPRIO INIZIALE
			   ,decode(aSaldo.ti_competenza_residuo,
                       'C', 0,
                       'R', aSaldo.IM_OBBL_RES_IMP) -- impegnato residuo IMPROPRIO
               ,aSaldo.VAR_PIU_OBBL_RES_PRO   -- VAR PIU' RES PRO
               ,aSaldo.VAR_MENO_OBBL_RES_PRO  -- VAR MENO RES PRO
			   ,aSaldo.IM_MANDATI_REVERSALI_PRO -- tot pagato COMP / RESIDUO PROPRIO
			   ,decode(aSaldo.ti_competenza_residuo,
                       'C', 0,
                       'R', aSaldo.IM_MANDATI_REVERSALI_IMP) -- PAGATO RESIDUO IMPROPRIO
			   ,aSaldo.IM_PAGAMENTI_INCASSI
			   ,aIm1210noSosp
		from unita_organizzativa uo
		where uo.CD_UNITA_ORGANIZZATIVA = aSaldo.cd_cds
		  and uo.FL_CDS = 'Y';
		-- FINE inserimento record di tipo A (info generali sul capitolo)

		-- inserimento record tipo B (variazioni di bilancio, VARIAZIONI AI PIANI DI GESTIONE, VARIAZIONI ALLO STANZIAMENTO RESIDUO)

		   for aVarBil in (-- variazioni al bilanci di servizio
                           select VB.esercizio, VB.cd_cds, VB.TI_APPARTENENZA, vbd.TI_GESTIONE, vbd.cd_voce,
                                  aSaldo.TI_COMPETENZA_RESIDUO,
                                  vb.PG_VARIAZIONE,
                                  'Var. Bil. Serv. '||VB.DS_VARIAZIONE DS_VARIAZIONE,
                                  sum(im_variazione) IM_VARIAZIONE
                           from   var_bilancio_det vbd, var_bilancio vb
                           where  VB.cd_cds  		 = aSaldo.cd_cds
						     and  VB.esercizio 	     = aSaldo.esercizio
							 and  VB.ti_appartenenza = aSaldo.ti_appartenenza
							 and  vbd.ti_gestione	 = aSaldo.ti_gestione
							 and  vbd.cd_voce		 = aSaldo.cd_voce
		 	 		         and  vb.cd_cds          = vbd.cd_cds
							 and  vb.esercizio 	     = vbd.esercizio
							 and  vb.ti_appartenenza = vbd.ti_appartenenza
							 and  vb.pg_variazione	 = vbd.pg_variazione
							 and  vb.stato 		     = 'D'
                             and ((vb.esercizio = vb.esercizio_importi AND aSaldo.ti_competenza_residuo = 'C') OR
                                  (vb.esercizio > vb.esercizio_importi AND aSaldo.ti_competenza_residuo = 'R')) -- SIA PER COMPETENZA CHE PER RESIDUI
                           group by VB.esercizio, VB.cd_cds, VB.TI_APPARTENENZA, TI_GESTIONE, cd_voce, decode(vb.esercizio, vb.esercizio_importi, 'C', 'R'),
                                    VB.PG_VARIAZIONE, 'Var. Bil. Serv. '||VB.DS_VARIAZIONE
                           union all
                           -- variazioni al PDG
                           Select t.esercizio, CNRUTL001.GETCDSFROMCDR(D.CD_CDR_ASSEGNATARIO), d.TI_APPARTENENZA, d.TI_GESTIONE,
                                  cnrctb053.getVoce_FdaEV (t.Esercizio, d.TI_APPARTENENZA, d.TI_GESTIONE, d.CD_ELEMENTO_VOCE, d.cd_cdr_assegnatario, d.CD_LINEA_ATTIVITA),
                                  'C', t.PG_VARIAZIONE_PDG, 'Var. Pdg. '||DS_VARIAZIONE,
                                  Sum(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0)+
      	                              Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0)+
      	                              Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0)+
       	                              Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0))
                           From   PDG_VARIAZIONE T, PDG_VARIAZIONE_RIGA_GEST D
                           Where  CNRUTL001.GETCDSFROMCDR(D.CD_CDR_ASSEGNATARIO) = aSaldo.cd_cds and
                                  D.ESERCIZIO                                    = aSaldo.esercizio And
                                  D.TI_APPARTENENZA                              = aSaldo.ti_appartenenza And
                                  D.TI_GESTIONE                                  = aSaldo.ti_gestione And
                                  cnrctb053.getVoce_FdaEV (t.Esercizio, d.TI_APPARTENENZA, d.TI_GESTIONE,
                                                            d.CD_ELEMENTO_VOCE, d.cd_cdr_assegnatario, d.CD_LINEA_ATTIVITA) = aSaldo.cd_voce and
                                  T.ESERCIZIO                                    = D.ESERCIZIO        And
                                  T.PG_VARIAZIONE_PDG                            = D.PG_VARIAZIONE_PDG And
                                  T.STATO                                        In ('APP', 'APF') And
                                  CATEGORIA_DETTAGLIO                            != 'SCR' and
                                  aSaldo.ti_competenza_residuo                   = 'C'  -- solo per competenza
                           group by t.esercizio, CNRUTL001.GETCDSFROMCDR(D.CD_CDR_ASSEGNATARIO), d.TI_APPARTENENZA, d.TI_GESTIONE,
                                    cnrctb053.getVoce_FdaEV (t.Esercizio, d.TI_APPARTENENZA, d.TI_GESTIONE, d.CD_ELEMENTO_VOCE, d.cd_cdr_assegnatario, d.CD_LINEA_ATTIVITA),
                                    'C', t.PG_VARIAZIONE_PDG, 'Var. Pdg. '||DS_VARIAZIONE
                           UNION ALL
                           -- variazioni allo stanziamento residuo
                           Select VT.esercizio, CNRUTL001.GETCDSFROMCDR(VR.CD_CDR) CD_CDS, TI_APPARTENENZA, TI_GESTIONE,
                                  cd_voce, 'R',
                                  VT.PG_VARIAZIONE, 'Var. Stanz. Res. '||DS_VARIAZIONE, sum(im_variazione)
                           From   VAR_STANZ_RES_RIGA VR, VAR_STANZ_RES VT
                           Where  CNRUTL001.GETCDSFROMCDR(vr.CD_CDR) = aSaldo.cd_cds AND
                                  VR.ESERCIZIO                      = aSaldo.esercizio And
                                  VR.ESERCIZIO_VOCE                 = aSaldo.esercizio And
                                  VR.TI_APPARTENENZA                = aSaldo.ti_appartenenza AND
                                  VR.TI_GESTIONE                    = aSaldo.ti_gestione AND
                                  VR.CD_VOCE                        = aSaldo.cd_voce And
                                  VT.ESERCIZIO                      = VR.ESERCIZIO And
                                  VT.PG_VARIAZIONE                  = VR.PG_VARIAZIONE And
                                  VT.STATO                          = 'APP' and
                                  aVoce.ti_competenza_residuo       = 'R'  -- solo per RESIDUI
                           group by VT.esercizio, CNRUTL001.GETCDSFROMCDR(vr.CD_CDR), TI_APPARTENENZA, TI_GESTIONE,
                                    cd_voce, 'R', VT.PG_VARIAZIONE, 'Var. Stanz. Res. '||DS_VARIAZIONE) loop

		        i := i + 1;

				insert into VPG_SINGOLO_CONTO  (ID,
												CHIAVE,
												TIPO,
												SEQUENZA,
												ESERCIZIO,
												CD_CDS,
												TI_APPARTENENZA,
												TI_GESTIONE,
                                                CD_ELEMENTO_VOCE,
                                                DS_ELEMENTO_VOCE,
												CD_VOCE,
												TI_COMPETENZA_RESIDUO,
												PG_VARIAZIONE,
												DS_VARIAZIONE,
												IM_VARIAZIONE)
				VALUES (aId
					   ,aKey
					   ,'B'
					   ,i
					   ,aSaldo.esercizio
					   ,aSaldo.cd_cds
					   ,aSaldo.ti_appartenenza
					   ,aSaldo.ti_gestione
                       ,aSaldo.cd_elemento_voce
                       ,(select ds_elemento_voce from elemento_voce where esercizio = aSaldo.esercizio and ti_appartenenza = aSaldo.ti_appartenenza and
                                                                  ti_gestione = aSaldo.ti_gestione and cd_elemento_voce = aSaldo.cd_elemento_voce)
					   ,aSaldo.cd_voce
                       ,aSaldo.ti_competenza_residuo
					   ,aVarBil.pg_variazione
					   ,Substr(aVarBil.ds_variazione,1,500)
					   ,aVarBil.im_variazione);

		   end loop;

		-- FINE 		-- inserimento record tipo B (variazioni di bilancio, VARIAZIONI AI PIANI DI GESTIONE, VARIAZIONI ALLO STANZIAMENTO RESIDUO)

		-- inserimento record tipo C (obbligazioni/accertamenti)

		if aVoce.ti_gestione = 'S' then

		-- spese >>> obbligazioni, impegni

			   for aObb in (select obb.cd_cds,
			   	   		   		   obb.esercizio,
			   	   		   		   obb.esercizio_originale,
								   obb.pg_obbligazione,
								   obb.dt_registrazione,
								   obb.cd_terzo,
								   obb.DS_OBBLIGAZIONE,
								   obb.fl_pgiro,
								   nvl(sum(obbsv.im_voce), 0) aTot,
								   decode(sum(obbs.im_associato_doc_contabile), 0, 0, sum(obbsv.im_voce)) im_voce_doc_cont
			   	   		    from obbligazione obb,
		 						 obbligazione_scad_voce obbsv,
								 obbligazione_scadenzario obbs
							where obbsv.cd_cds          	 = obb.cd_cds
	   						  and obbsv.esercizio			 = obb.esercizio
	   						  and obbsv.esercizio_originale	 = obb.esercizio_originale
							  and obbsv.pg_obbligazione		 = obb.pg_obbligazione
							  and obbsv.esercizio			 = aVf.esercizio
							  and obbsv.ti_appartenenza		 = aVf.ti_appartenenza
							  and obbsv.ti_gestione			 = aVf.ti_gestione
							  and obbsv.cd_voce				 = aVf.cd_voce
							  and obb.stato_obbligazione 	 <> 'S' -- escludo obbligazioni stornate
                              and ((obb.fl_pgiro = 'Y' and obb.cd_cds = aVoce.cd_cds) OR
                                   (obb.fl_pgiro = 'N' and obb.cd_cds = aSaldo.cd_cds))
							  and obbs.cd_cds 				 = obbsv.cd_cds
  							  and obbs.esercizio 			 = obbsv.esercizio
	   						  and obbs.esercizio_originale	 = obbsv.esercizio_originale
  							  and obbs.pg_obbligazione 		 = obbsv.pg_obbligazione
  							  and obbs.pg_obbligazione_scadenzario = obbsv.pg_obbligazione_scadenzario
                              AND OBB.PG_OBBLIGAZIONE > 0
                              and ((obb.esercizio = obb.esercizio_originale and aSaldo.ti_competenza_residuo = 'C') OR
                                   (obb.esercizio > obb.esercizio_originale and aSaldo.ti_competenza_residuo = 'R'))
							group by obb.cd_cds, obb.esercizio, obb.esercizio_originale, obb.pg_obbligazione, obb.dt_registrazione, obb.cd_terzo, obb.DS_OBBLIGAZIONE,
                                     obb.fl_pgiro
							order by obb.esercizio_originale, obb.pg_obbligazione) loop

					i := i + 1;

					-- oss: per impegni residui, ho 1! scadenza ed 1!scad_voce
					--      quindi l'importo associato a doc cont sulla voce ? pari
					--		all'importo associato a doc cont della scadenza

					insert into VPG_SINGOLO_CONTO  (ID,
													CHIAVE,
													TIPO,
													SEQUENZA,
													ESERCIZIO,
													CD_CDS,
													TI_APPARTENENZA,
													TI_GESTIONE,
                                                    CD_ELEMENTO_VOCE,
                                                    DS_ELEMENTO_VOCE,
													CD_VOCE,
													TI_COMPETENZA_RESIDUO,
													DT_REGISTRAZIONE,
													ESERCIZIO_ORI_OBB_ACR,
													PG_OBB_ACR,
													FL_ANNOTAZIONE,
													CD_TERZO,
													DENOMINAZIONE_SEDE,
													DS_OBB_ACR,
													IM_VOCE,
													IM_VOCE_NO_DOCCONT)
					select  aId
						   ,aKey
						   ,'C'
						   ,i
						   ,aSaldo.esercizio
						   --aSaldo.cd_cds
                           ,decode(aObb.fl_pgiro, 'Y', aObb.cd_cds, aSaldo.cd_cds)
						   ,aSaldo.ti_appartenenza
						   ,aSaldo.ti_gestione
                           ,aSaldo.cd_elemento_voce
                           ,(select ds_elemento_voce from elemento_voce where esercizio = aSaldo.esercizio and ti_appartenenza = aSaldo.ti_appartenenza and
                                                                  ti_gestione = aSaldo.ti_gestione and cd_elemento_voce = aSaldo.cd_elemento_voce)
						   ,aSaldo.cd_voce
						   ,aSaldo.ti_competenza_residuo
						   ,aObb.dt_registrazione
						   ,aObb.esercizio_originale
						   ,aObb.pg_obbligazione
						   ,decode(aObb.fl_pgiro,'Y','SI','NO')
						   ,aObb.cd_terzo
						   ,t.denominazione_sede
						   ,aObb.ds_obbligazione
						   ,aObb.aTot
						   ,aObb.aTot - aObb.im_voce_doc_cont
					from terzo t
					where t.cd_terzo = aObb.cd_terzo;
			   end loop; -- fine loop impegni COMPETENZA/RESIDUI

		else

		-- entrate >>> accertamenti competenza e residui

				for aAcc in (select acc.cd_cds,
						 			acc.esercizio,
						 			acc.esercizio_originale,
									acc.pg_accertamento,
									acc.dt_registrazione,
									acc.fl_pgiro,
									acc.cd_terzo,
									acc.ds_accertamento,
									acc.im_accertamento,
									acc.im_accertamento - sum(accs.im_associato_doc_contabile) im_no_doc_cont
						 	 from accertamento acc
							 	 ,accertamento_scadenzario accs
						 	 where acc.cd_cds  			      = aSaldo.cd_cds
							   and acc.esercizio			  = aVf.esercizio
							   and acc.ti_appartenenza		  = aVf.ti_appartenenza
							   and acc.ti_gestione			  = aVf.ti_gestione
							   and acc.cd_voce				  = aVf.cd_voce
							   and accs.cd_cds				  = acc.cd_cds
							   and accs.esercizio 		  	  = acc.esercizio
							   and accs.esercizio_originale	  = acc.esercizio_originale
							   and accs.pg_accertamento   	  = acc.pg_accertamento
                               and ((acc.esercizio = acc.esercizio_originale and aSaldo.ti_competenza_residuo = 'C') OR
                                    (acc.esercizio > acc.esercizio_originale and aSaldo.ti_competenza_residuo = 'R'))
 							 group by acc.cd_cds,
						 			acc.esercizio,
                                    acc.esercizio_originale,
									acc.pg_accertamento,
									acc.dt_registrazione,
									acc.fl_pgiro,
									acc.cd_terzo,
									acc.ds_accertamento,
									acc.im_accertamento
							 order by acc.esercizio_originale, acc.pg_accertamento) loop

					i := i + 1;

					insert into VPG_SINGOLO_CONTO  (ID,
													CHIAVE,
													TIPO,
													SEQUENZA,
													ESERCIZIO,
													CD_CDS,
													TI_APPARTENENZA,
													TI_GESTIONE,
                                                    CD_ELEMENTO_VOCE,
                                                    DS_ELEMENTO_VOCE,
													CD_VOCE,
													TI_COMPETENZA_RESIDUO,
													DT_REGISTRAZIONE,
													ESERCIZIO_ORI_OBB_ACR,
													PG_OBB_ACR,
													FL_ANNOTAZIONE,
													CD_TERZO,
													DENOMINAZIONE_SEDE,
													DS_OBB_ACR,
													IM_VOCE,
													IM_VOCE_NO_DOCCONT)
					select  aId
						   ,aKey
						   ,'C'
						   ,i
						   ,aSaldo.esercizio
						   ,aSaldo.cd_cds
						   ,aSaldo.ti_appartenenza
						   ,aSaldo.ti_gestione
                           ,aSaldo.cd_elemento_voce
                           ,(select ds_elemento_voce from elemento_voce where esercizio = aSaldo.esercizio and ti_appartenenza = aSaldo.ti_appartenenza and
                                                                  ti_gestione = aSaldo.ti_gestione and cd_elemento_voce = aSaldo.cd_elemento_voce)
						   ,aSaldo.cd_voce
						   ,aSaldo.ti_competenza_residuo
						   ,aAcc.dt_registrazione
						   ,aAcc.esercizio_originale
						   ,aAcc.pg_accertamento
						   ,decode(aAcc.fl_pgiro,'Y','SI','NO')
						   ,aAcc.cd_terzo
						   ,t.denominazione_sede
						   ,aAcc.ds_accertamento
						   ,aAcc.im_accertamento
						   ,aAcc.im_no_doc_cont
					from terzo t
					where t.cd_terzo = aAcc.cd_terzo;
				end loop; -- fine loop accertamenti residui

		end if; -- fine distinzione entrata/spesa
		-- FINE inserimento record tipo C (obbligazioni/accertamenti)

 		-- inserimento record tipo D (mandati/reversali)
		if aVoce.ti_gestione = 'S' then
		-- spese >>> mandati
		  for aDett in (select man.cd_cds,
							   man.esercizio,
							   man.pg_mandato,
							   man.ds_mandato,
							   aSaldo.ti_competenza_residuo,
							   man.dt_emissione,
							   decode(man.stato,'E','NO','SI') pagato,
							   mriga.esercizio_obbligazione,
							   mriga.esercizio_ori_obbligazione,
							   mriga.pg_obbligazione,
							   mriga.pg_obbligazione_scadenzario,
							   mriga.cd_terzo,
							   mriga.ds_mandato_riga,
							   obbsv.cd_voce,
							   DECODE(NVL(SUM(obbs.IM_SCADENZA),0),0,0,(SUM(obbsv.IM_VOCE)/SUM(obbs.IM_SCADENZA) )*SUM(MRIGA.IM_MANDATO_RIGA)) im_voce
							   --sum(obbsv.im_voce) im_voce
						 from mandato  man,
	 					      mandato_riga mriga,
	 					      OBBLIGAZIONE_SCADENZARIO OBBS,
	 						  obbligazione_scad_voce obbsv
						 where man.cd_cds 			 	  		 = mriga.cd_cds
  						   and man.esercizio 					 = mriga.esercizio
  						   and man.pg_mandato 					 = mriga.pg_mandato
  						   And obbs.ESERCIZIO 		  			 = mriga.ESERCIZIO
-- 27.02.2008 AGGIUNTA PER RADDOPPIAMENTO IMPORTO DOVUTO A RIGHE DI MANDATO CON FATTURE E NOTE
AND    obbs.ESERCIZIO_ORIGINALE  			 = mriga.ESERCIZIO_ORI_OBBLIGAZIONE
AND    obbs.PG_OBBLIGAZIONE	  			 = mriga.PG_OBBLIGAZIONE
AND    obbs.PG_OBBLIGAZIONE_SCADENZARIO  = mriga.PG_OBBLIGAZIONE_SCADENZARIO
AND    obbsv.CD_CDS						 = obbs.CD_CDS
AND    obbsv.ESERCIZIO					 = obbs.ESERCIZIO
AND    obbsv.ESERCIZIO_ORIGINALE  			 = obbs.ESERCIZIO_ORIGINALE
AND    obbsv.PG_OBBLIGAZIONE			 = obbs.PG_OBBLIGAZIONE
AND    obbsv.PG_OBBLIGAZIONE_SCADENZARIO = obbs.PG_OBBLIGAZIONE_SCADENZARIO
-- 27.02.2008 fin qui
  						   and man.stato 						 <> 'A'
						   and man.cd_cds						 = aSaldo.cd_cds
  						   and obbsv.cd_cds 				     = mriga.cd_cds
  						   and obbsv.esercizio 		        	 = mriga.esercizio_obbligazione
  						   and obbsv.esercizio_originale		 = mriga.esercizio_ori_obbligazione
  						   and obbsv.pg_obbligazione 			 = mriga.pg_obbligazione
  						   and obbsv.pg_obbligazione_scadenzario = mriga.pg_obbligazione_scadenzario
						   and obbsv.esercizio 					 = aVf.esercizio
  						   and obbsv.ti_appartenenza 			 = aVf.ti_appartenenza
  						   and obbsv.ti_gestione 				 = aVf.ti_gestione
  						   and obbsv.cd_voce 					 = aVf.cd_voce
                           and ((obbsv.esercizio = obbsv.esercizio_originale and aSaldo.ti_competenza_residuo = 'C') OR
                                (obbsv.esercizio > obbsv.esercizio_originale and aSaldo.ti_competenza_residuo = 'R'))
						 group by  man.cd_cds,
								   man.esercizio,
								   man.pg_mandato,
								   man.ds_mandato,
								   aSaldo.ti_competenza_residuo,
								   man.dt_emissione,
								   decode(man.stato,'E','NO','SI'),
								   mriga.esercizio_obbligazione,
								   mriga.esercizio_ori_obbligazione,
								   mriga.pg_obbligazione,
								   mriga.pg_obbligazione_scadenzario,
								   mriga.cd_terzo,
								   mriga.ds_mandato_riga,
								   obbsv.cd_voce
						 order by man.cd_cds,
						 	      man.esercizio,
								  man.pg_mandato) loop

		   	  	i := i + 1;

				select cd_tipo_documento_cont into aCdTipoDocCont
				from obbligazione
				where cd_cds 	          = aDett.cd_cds
				  and esercizio 	      = aDett.esercizio_obbligazione
				  and esercizio_originale = aDett.esercizio_ori_obbligazione
				  and pg_obbligazione     = aDett.pg_obbligazione;

				-- se il mandato ? emesso su doc cont a consumo, l'importo visualizzato
				-- deve essere im_mandato_riga, altrimenti aDett.im_voce
				-- (vd. segnalazione 776)

				if aCdTipoDocCont in ('IMP','IMP_RES') then
				   select nvl(sum(im_mandato_riga),0) into aImporto
				   from mandato_riga
				   where cd_cds 			   		 = aDett.cd_cds
				     and esercizio 					 = aDett.esercizio
					 and pg_mandato 				 = aDett.pg_mandato
					 and esercizio_obbligazione 	 = aDett.esercizio_obbligazione
					 and esercizio_ori_obbligazione  = aDett.esercizio_ori_obbligazione
					 and pg_obbligazione 		 = aDett.pg_obbligazione
					 and pg_obbligazione_scadenzario = aDett.pg_obbligazione_scadenzario;
				else
				   aImporto := aDett.im_voce;
				end if;

				insert into VPG_SINGOLO_CONTO  (ID,
												CHIAVE,
												TIPO,
												SEQUENZA,
												ESERCIZIO,
												CD_CDS,
												TI_APPARTENENZA,
												TI_GESTIONE,
                                                CD_ELEMENTO_VOCE,
                                                DS_ELEMENTO_VOCE,
												CD_VOCE,
												TI_COMPETENZA_RESIDUO,
												PG_MAN_REV,
												DT_EMISSIONE,
												FL_PAGATO,
												CD_TERZO,
												DENOMINAZIONE_SEDE,
												DS_MAN_REV,
												DS_MAN_REV_RIGA,
												ESERCIZIO_ORI_OBB_ACR,
												PG_OBB_ACR,
												PG_OBB_ACR_SCADENZARIO,
												IM_VOCE
												)
				select aId
					   ,aKey
					   ,'D'
					   ,i
					   ,aSaldo.esercizio
					   ,aSaldo.cd_cds
					   ,aSaldo.ti_appartenenza
					   ,aSaldo.ti_gestione
                       ,aSaldo.cd_elemento_voce
                       ,(select ds_elemento_voce from elemento_voce where esercizio = aSaldo.esercizio and ti_appartenenza = aSaldo.ti_appartenenza and
                                                                  ti_gestione = aSaldo.ti_gestione and cd_elemento_voce = aSaldo.cd_elemento_voce)
					   ,aSaldo.cd_voce
					   ,aSaldo.ti_competenza_residuo
					   ,aDett.pg_mandato
					   ,aDett.dt_emissione
					   ,aDett.pagato
					   ,aDett.cd_terzo
					   ,t.denominazione_sede
					   ,aDett.ds_mandato
					   ,aDett.ds_mandato_riga
					   ,aDett.esercizio_ori_obbligazione
					   ,aDett.pg_obbligazione
					   ,aDett.pg_obbligazione_scadenzario
					   ,aImporto
				from terzo t
				where t.cd_terzo = aDett.cd_terzo;

		   end loop;

		else
		-- entrate >>> reversali
			for aDett in (select   rev.cd_cds,
								   rev.esercizio,
								   rev.pg_reversale,
								   decode(rev.stato,'E','NO','SI') incassata,
								   rev.dt_emissione,
								   aSaldo.ti_competenza_residuo,
								   rev.ds_reversale,
								   rriga.cd_terzo,
								   rriga.ds_reversale_riga,
								   rriga.esercizio_accertamento,
								   rriga.esercizio_ori_accertamento,
								   rriga.pg_accertamento,
								   rriga.pg_accertamento_scadenzario,
								   acc.cd_voce,
								   RRIGA.IM_REVERSALE_RIGA
						  from reversale rev,
	 					  	   reversale_riga rriga,
	 						   accertamento acc
						  where rriga.cd_cds              = rev.cd_cds
  						    and rriga.esercizio			  = rev.esercizio
  							and rriga.pg_reversale		  = rev.pg_reversale
  							and rev.stato				  <> 'A'
							and rev.cd_cds				  = aSaldo.cd_cds
  							and acc.cd_cds				  = rriga.cd_cds
  							and acc.esercizio			  = rriga.esercizio_accertamento
  							and acc.esercizio_originale	  = rriga.esercizio_ori_accertamento
  							and acc.pg_accertamento		  = rriga.pg_accertamento
  							and acc.esercizio			  = aVf.esercizio
  							and acc.TI_APPARTENENZA		  = aVf.ti_appartenenza
  							and acc.TI_GESTIONE			  = aVf.ti_gestione
  							and acc.cd_voce				  = aVf.cd_voce
                            and ((acc.esercizio = acc.esercizio_originale and aSaldo.ti_competenza_residuo = 'C') OR
                                 (acc.esercizio > acc.esercizio_originale and aSaldo.ti_competenza_residuo = 'R'))
						  order by rev.cd_cds, rev.esercizio, rev.pg_reversale) loop

				i := i + 1;

				insert into VPG_SINGOLO_CONTO  (ID,
												CHIAVE,
												TIPO,
												SEQUENZA,
												ESERCIZIO,
												CD_CDS,
												TI_APPARTENENZA,
												TI_GESTIONE,
                                                CD_ELEMENTO_VOCE,
                                                DS_ELEMENTO_VOCE,
												CD_VOCE,
												TI_COMPETENZA_RESIDUO,
												PG_MAN_REV,
												DT_EMISSIONE,
												FL_PAGATO,
												CD_TERZO,
												DENOMINAZIONE_SEDE,
												DS_MAN_REV,
												DS_MAN_REV_RIGA,
												ESERCIZIO_ORI_OBB_ACR,
												PG_OBB_ACR,
												PG_OBB_ACR_SCADENZARIO,
												IM_VOCE
												)
				select aId
					   ,aKey
					   ,'D'
					   ,i
					   ,aSaldo.esercizio
					   ,aSaldo.cd_cds
					   ,aSaldo.ti_appartenenza
					   ,aSaldo.ti_gestione
                       ,aSaldo.cd_elemento_voce
                       ,(select ds_elemento_voce from elemento_voce where esercizio = aSaldo.esercizio and ti_appartenenza = aSaldo.ti_appartenenza and
                                                                  ti_gestione = aSaldo.ti_gestione and cd_elemento_voce = aSaldo.cd_elemento_voce)
					   ,aSaldo.cd_voce
					   ,aSaldo.ti_competenza_residuo
					   ,aDett.pg_reversale
					   ,aDett.dt_emissione
					   ,aDett.incassata
					   ,aDett.cd_terzo
					   ,t.denominazione_sede
					   ,aDett.ds_reversale
					   ,aDett.ds_reversale_riga
					   ,aDett.esercizio_ori_accertamento
					   ,aDett.pg_accertamento
					   ,aDett.pg_accertamento_scadenzario
					   ,aDett.im_REVERSALE_RIGA
				from terzo t
				where t.cd_terzo = aDett.cd_terzo;

			end loop;
		end if; -- fine distinzione mandati/reversali
 		-- FINE inserimento record tipo D (mandati/reversali)

     END LOOP; -- DEI SALDI

	end loop; -- DELLA VOCE

	-- eliminazione record in v_stm_paramin_sing_conto
	delete v_stm_paramin_sing_conto
	where id_report = aIdRpt;

end;
/


