CREATE OR REPLACE procedure DROPNEGATIVEOBBLIG (aUtente varchar2) as
--
-- DROPNEGATIVEOBBLIG - Eliminazione Obbligazioni negative
--
-- Date: 18/07/2006
-- Version: 1.0
--
-- Date: 18/07/2006
-- Version: 1.0
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
lPgLog number;
lCompRes char(1);
lData date;
lAssociatoDocAmm boolean;
lNumDoc number;
lTipoDocAmm varchar2(30);
begin
	 lData := sysdate;
	 -- recuperiamo un progressivo per il LOG
	 lPgLog := ibmutl200.LOGSTART('Eliminazione '||cnrutil.getLabelObbligazioni()||' Negative ' , aUtente, null, null);
	 -- come prima cosa cicliamo su tutte le obbligazioni negative
	 for lObb in (select * from obbligazione
	 	 		  where pg_obbligazione <0
				  for update nowait)
	 loop
	 	 -- Controlliamo se obbligazione risulta associata a doc amm
		 lNumDoc := 0;
		 if lNumDoc = 0 then
			 -- Fondo Spesa
			 lTipoDocAmm :='Fondo Spesa';
			 select count(*) into lNumDoc from FONDO_SPESA
			 where CD_CDS_OBBLIGAZIONE      = lObb.cd_cds
			 and   ESERCIZIO_OBBLIGAZIONE 	= lObb.esercizio
			 and   ESERCIZIO_ORI_OBBLIGAZIONE = lObb.esercizio_originale
			 and   PG_OBBLIGAZIONE 	    	= lObb.pg_obbligazione;
		 end if;

		 if lNumDoc = 0 then
			 -- Missione
			 lTipoDocAmm :='Missione' ;
			 select count(*) into lNumDoc from MISSIONE_RIGA
			 where CD_CDS_OBBLIGAZIONE      = lObb.cd_cds
			 and   ESERCIZIO_OBBLIGAZIONE 	= lObb.esercizio
			 and   ESERCIZIO_ORI_OBBLIGAZIONE = lObb.esercizio_originale
			 and   PG_OBBLIGAZIONE 	    	= lObb.pg_obbligazione;
		 end if;

		 if lNumDoc = 0 then
			 -- Anticipo
			 lTipoDocAmm :='Anticipo' ;
			 select count(*) into lNumDoc from ANTICIPO
			 where CD_CDS_OBBLIGAZIONE    = lObb.cd_cds
			 and   ESERCIZIO_OBBLIGAZIONE = lObb.esercizio
			 and   ESERCIZIO_ORI_OBBLIGAZIONE = lObb.esercizio_originale
			 and   PG_OBBLIGAZIONE 	      = lObb.pg_obbligazione;
		 end if;

		 if lNumDoc = 0 then
			 -- Documento generico
			 lTipoDocAmm :='Documento generico';
			 select count(*) into lNumDoc from documento_generico_riga doc, tipo_documento_amm tipo
			 where doc.CD_TIPO_DOCUMENTO_AMM  = tipo.CD_TIPO_DOCUMENTO_AMM
			 and   doc.CD_CDS_OBBLIGAZIONE    = lObb.cd_cds
			 and   doc.ESERCIZIO_OBBLIGAZIONE = lObb.esercizio
			 and   doc.ESERCIZIO_ORI_OBBLIGAZIONE = lObb.esercizio_originale
			 and   doc.PG_OBBLIGAZIONE 		  = lObb.pg_obbligazione;
		 end if;

		 if lNumDoc = 0 then
			 -- Fattura Attiva
			 lTipoDocAmm :='Fattura Attiva' ;
			 select count(*) into lNumDoc from fattura_attiva_riga
			 where CD_CDS_OBBLIGAZIONE    = lObb.cd_cds
			 and   ESERCIZIO_OBBLIGAZIONE = lObb.esercizio
			 and   ESERCIZIO_ORI_OBBLIGAZIONE = lObb.esercizio_originale
			 and   PG_OBBLIGAZIONE 	      = lObb.pg_obbligazione;
		 end if;

		 if lNumDoc = 0 then
			 -- Fattura Passiva
			 lTipoDocAmm :='Fattura Passiva' ;
			 select count(*) into lNumDoc from fattura_passiva_riga
			 where CD_CDS_OBBLIGAZIONE     = lObb.cd_cds
			 and	ESERCIZIO_OBBLIGAZIONE = lObb.esercizio
			 and   ESERCIZIO_ORI_OBBLIGAZIONE = lObb.esercizio_originale
			 and   PG_OBBLIGAZIONE 		   = lObb.pg_obbligazione;
		 end if;

		 if lNumDoc = 0 then
			 -- Compenso
			 lTipoDocAmm :='Compenso' ;
			 select count(*) into lNumDoc from compenso_riga
			 where CD_CDS_OBBLIGAZIONE 		= lObb.cd_cds
			 and   ESERCIZIO_OBBLIGAZIONE 	= lObb.esercizio
			 and   ESERCIZIO_ORI_OBBLIGAZIONE = lObb.esercizio_originale
			 and   PG_OBBLIGAZIONE 			= lObb.pg_obbligazione;
		 end if;

		 if lNumDoc = 0 then
			 -- Ritenuta
			 lTipoDocAmm :='Ritenuta' ;
			 select count(*) into lNumDoc from CONTRIBUTO_RITENUTA cr, compenso co
			 where co.CD_CDS = cr.cd_cds
			 and   co.CD_UNITA_ORGANIZZATIVA = cr.CD_UNITA_ORGANIZZATIVA
			 and   co.ESERCIZIO = cr.ESERCIZIO
			 and   co.PG_COMPENSO = cr.PG_COMPENSO
			 and   cr.CD_CDS_OBBLIGAZIONE       = lObb.cd_cds
			 and   cr.ESERCIZIO_OBBLIGAZIONE 	= lObb.esercizio
			 and   cr.ESERCIZIO_ORI_OBBLIGAZIONE = lObb.esercizio_originale
			 and   cr.PG_OBBLIGAZIONE 			= lObb.pg_obbligazione;
		 end if;

		 if lNumDoc >0 then
		 	lAssociatoDocAmm := true;
			ibmutl200.logInf(lPgLog ,'Associato con documento ', 'lTipoDocAmm', '');
		 else
		 	lAssociatoDocAmm := false;
		 end if;


		 if not lAssociatoDocAmm then
		 	 -- Vediamo se il documento risulta essere in competenza o residuo
			 if lObb.cd_tipo_documento_cont ='IMP_RES' then
			 	lCompRes := 'R';
			 else
			 	lCompRes := 'C' ;
			 end if;
			 -- recuperiamo i capitoli a cui e legata obbligazione
-- 			 for lObbScadVoce in (select * from obbligazione_scad_voce
-- 			 	 			      where cd_cds = lObb.cd_cds
-- 							      and   esercizio = lObb.esercizio
--                                                            and   esercizio_originale = lObb.esercizio_originale
-- 							      and   pg_obbligazione = lObb.pg_obbligazione
-- 							      for update nowait)
-- 			 loop
			 	 -- decrementiamo il capitolo in esame di im_voce
-- 				 update voce_f_saldi_cmp
-- 				 set IM_OBBLIG_IMP_ACR = IM_OBBLIG_IMP_ACR - lObbScadVoce.im_voce,
-- 				 	 UTUV              = aUtente,
-- 					 DUVA              = lData,
-- 					 PG_VER_REC        = PG_VER_REC + 1
-- 				 where CD_CDS          = lObbScadVoce.cd_cds
-- 				 and   ESERCIZIO       = lObbScadVoce.esercizio
-- 				 and   TI_APPARTENENZA = lObbScadVoce.ti_appartenenza
-- 				 and   TI_GESTIONE 		= lObbScadVoce.ti_gestione
-- 				 and   CD_VOCE 			= lObbScadVoce.cd_voce
-- 				 and   TI_COMPETENZA_RESIDUO = lCompRes;

--			 end loop; -- voci scadenze obbligazioni
			 --------------------------------------
			 -- Cancelliamo Obbligazione scad voce
			 --------------------------------------
			 delete obbligazione_scad_voce
			 where cd_cds = lObb.cd_cds
			 and   esercizio = lObb.esercizio
			 and   esercizio_originale = lObb.esercizio_originale
			 and   pg_obbligazione = lObb.pg_obbligazione;

			 ---------------------------------------
			 -- Cancelliamo Obbligazione scadenzario
			 ---------------------------------------
			 delete obbligazione_scadenzario
			 where cd_cds = lObb.cd_cds
			 and   esercizio = lObb.esercizio
			 and   esercizio_originale = lObb.esercizio_originale
			 and   pg_obbligazione = lObb.pg_obbligazione;
			 ---------------------------
			 -- Cancelliamo Obbligazione
			 ---------------------------
			 delete obbligazione
			 where cd_cds = lObb.cd_cds
			 and   esercizio = lObb.esercizio
			 and   esercizio_originale = lObb.esercizio_originale
			 and   pg_obbligazione = lObb.pg_obbligazione;

	 	 end if;-- not AssociatoDocAmm
	 end loop; -- obbligazioni negative
end;
/


