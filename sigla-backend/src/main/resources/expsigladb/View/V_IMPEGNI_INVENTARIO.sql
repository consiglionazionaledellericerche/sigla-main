--------------------------------------------------------
--  DDL for View V_IMPEGNI_INVENTARIO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_IMPEGNI_INVENTARIO" ("ESERCIZIO", "CD_CDS", "ESERCIZIO_ORIGINALE", "CD_UNITA_ORGANIZZATIVA", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DS_OBBLIGAZIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "IMPEGNATO", "ASSOCIATO") AS 
  SELECT A.ESERCIZIO, A.cd_cds, A.esercizio_originale, a.cd_unita_organizzativa,
A.pg_obbligazione,a.pg_obbligazione_scadenzario,A.ds_obbligazione,
a.cd_elemento_voce,a.ds_elemento_voce,
SUM(A.impegnato) impegnato,SUM(a.associato) Associato
FROM
(SELECT OBBLIGAZIONE.ESERCIZIO, OBBLIGAZIONE.cd_Cds, OBBLIGAZIONE.esercizio_originale,OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA,
 OBBLIGAZIONE.pg_obbligazione, OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario,OBBLIGAZIONE.ds_obbligazione,
 ELEMENTO_VOCE.cd_elemento_voce,ELEMENTO_VOCE.ds_elemento_voce,
 im_associato_doc_amm impegnato,0 associato
 FROM OBBLIGAZIONE,ELEMENTO_VOCE,OBBLIGAZIONE_SCADENZARIO
 WHERE
  OBBLIGAZIONE_SCADENZARIO.ESERCIZIO 		= OBBLIGAZIONE.ESERCIZIO	    AND
  OBBLIGAZIONE_SCADENZARIO.cd_cds		= OBBLIGAZIONE.cd_cds		    AND
  OBBLIGAZIONE_SCADENZARIO.esercizio_originale	= OBBLIGAZIONE.esercizio_originale  AND
  OBBLIGAZIONE_SCADENZARIO.pg_obbligazione 	= OBBLIGAZIONE.pg_obbligazione	    AND
  ELEMENTO_VOCE.ESERCIZIO 	=	OBBLIGAZIONE.ESERCIZIO 		AND
  ELEMENTO_VOCE.ti_appartenenza =	OBBLIGAZIONE.ti_appartenenza 	AND
  ELEMENTO_VOCE.ti_gestione 	=	OBBLIGAZIONE.ti_gestione 	AND
  ELEMENTO_VOCE.cd_elemento_voce=	OBBLIGAZIONE.cd_elemento_voce	AND
  FL_INV_BENI_PATR ='Y'
  And
  (Exists (Select 1 From FATTURA_PASSIVA_RIGA Fatt
  Where         fatt.stato_Cofi!='A' And
  	        fatt.esercizio_obbligazione 	= OBBLIGAZIONE.ESERCIZIO           And
	        fatt.cd_cds_obbligazione	= OBBLIGAZIONE.cd_cds		    AND
	    	fatt.esercizio_ori_obbligazione = OBBLIGAZIONE.esercizio_originale  AND
	    	fatt.pg_obbligazione 		= OBBLIGAZIONE.pg_obbligazione 	    AND
	    	fatt.pg_obbligazione_scadenzario= OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario) Or
   Exists (Select 1 From DOCUMENTO_GENERICO_RIGA riga
   Where
                riga.stato_Cofi!='A' And
                riga.Cd_tipo_documento_amm	= 'GENERICO_S'	    AND
  	        riga.esercizio_obbligazione 	= OBBLIGAZIONE.ESERCIZIO And
	    	riga.esercizio_ori_obbligazione = OBBLIGAZIONE.esercizio_originale  And
	    	riga.cd_cds_obbligazione        = OBBLIGAZIONE.cd_cds  AND
	    	riga.pg_obbligazione 		= OBBLIGAZIONE.pg_obbligazione	    AND
	    	riga.pg_obbligazione_scadenzario= OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario )
Or
   Exists (Select 1 From compenso, compenso_riga riga
   Where
            compenso.CD_CDS = riga.CD_CDS AND 
            compenso.CD_UNITA_ORGANIZZATIVA = riga.CD_UNITA_ORGANIZZATIVA AND 
            compenso.ESERCIZIO = riga.ESERCIZIO AND
            compenso.PG_COMPENSO = riga.PG_COMPENSO AND
            compenso.stato_Cofi!='A' And
  	        riga.esercizio_obbligazione 	= OBBLIGAZIONE.ESERCIZIO And
	    	riga.esercizio_ori_obbligazione = OBBLIGAZIONE.esercizio_originale  And
	    	riga.cd_cds_obbligazione        = OBBLIGAZIONE.cd_cds  AND
	    	riga.pg_obbligazione 		= OBBLIGAZIONE.pg_obbligazione	    AND
	    	riga.pg_obbligazione_scadenzario= OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario ))
 Union ALL
 SELECT OBBLIGAZIONE.ESERCIZIO, OBBLIGAZIONE.cd_Cds, OBBLIGAZIONE.esercizio_originale, OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA,
 OBBLIGAZIONE.pg_obbligazione,OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario, OBBLIGAZIONE.ds_obbligazione,
	ELEMENTO_VOCE.cd_elemento_voce,ELEMENTO_VOCE.ds_elemento_voce,
 	0 impegnato,
        (SELECT  Nvl(SUM(DECODE(TI_FATTURA,'C',(DECODE( Fatt.TI_ISTITUZ_COMMERC,'I',Fatt.Im_Imponibile+fatt.IM_IVA,fatt.im_imponibile))*(-1),
            (DECODE( Fatt.TI_ISTITUZ_COMMERC,'I',Fatt.Im_Imponibile+fatt.IM_IVA,fatt.im_imponibile)))),0)
            FROM  FATTURA_PASSIVA_RIGA Fatt,FATTURA_PASSIVA TESTATA
	    Where
	        fatt.stato_Cofi!='A' And
	    	TESTATA.Cd_Cds	 		= Fatt.Cd_Cds 			    AND
	    	TESTATA.Cd_UNITA_ORGANIZZATIVA	= Fatt.Cd_Unita_Organizzativa 	    AND
	    	TESTATA.ESERCIZIO	 	= Fatt.ESERCIZIO 		    AND
	    	TESTATA.Pg_Fattura_Passiva   	= Fatt.Pg_Fattura_Passiva   	    AND
	    	EXISTS ( SELECT 1 FROM ASS_INV_BENE_FATTURA Ass
	    	WHERE
	    	Ass.Esercizio_Fatt_Pass 	= Fatt.ESERCIZIO 		    AND
	    	Ass.Cd_Cds_Fatt_Pass 		= Fatt.Cd_Cds 			    AND
	    	Ass.Cd_Uo_Fatt_Pass 		= Fatt.Cd_Unita_Organizzativa 	    AND
	    	Ass.Pg_Fattura_Passiva   	= Fatt.Pg_Fattura_Passiva   	    AND
	    	Ass.Progressivo_Riga_Fatt_Pass 	= Fatt.Progressivo_Riga		 )  AND
	        fatt.esercizio_obbligazione 	= OBBLIGAZIONE.ESERCIZIO           And
	        fatt.cd_cds_obbligazione	= OBBLIGAZIONE.cd_cds		    AND
	    	fatt.esercizio_ori_obbligazione = OBBLIGAZIONE.esercizio_originale  AND
	    	fatt.pg_obbligazione 		= OBBLIGAZIONE.pg_obbligazione 	    AND
	    	fatt.pg_obbligazione_scadenzario= OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario) associato
		FROM OBBLIGAZIONE,ELEMENTO_VOCE,OBBLIGAZIONE_SCADENZARIO
		WHERE
		ELEMENTO_VOCE.ESERCIZIO 	= OBBLIGAZIONE.ESERCIZIO 	    AND
		ELEMENTO_VOCE.ti_appartenenza   = OBBLIGAZIONE.ti_appartenenza 	    AND
		ELEMENTO_VOCE.ti_gestione 	= OBBLIGAZIONE.ti_gestione 	    AND
		ELEMENTO_VOCE.cd_elemento_voce  = OBBLIGAZIONE.cd_elemento_voce	    AND
		OBBLIGAZIONE_SCADENZARIO.ESERCIZIO 		= OBBLIGAZIONE.ESERCIZIO	    AND
  		OBBLIGAZIONE_SCADENZARIO.cd_cds			= OBBLIGAZIONE.cd_cds		    AND
  		OBBLIGAZIONE_SCADENZARIO.esercizio_originale	= OBBLIGAZIONE.esercizio_originale  AND
  		OBBLIGAZIONE_SCADENZARIO.pg_obbligazione 	= OBBLIGAZIONE.pg_obbligazione	    AND
		FL_INV_BENI_PATR ='Y'
  	UNION ALL
  	SELECT OBBLIGAZIONE.ESERCIZIO, OBBLIGAZIONE.cd_Cds, OBBLIGAZIONE.esercizio_originale,OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA,
  	OBBLIGAZIONE.pg_obbligazione,OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario, OBBLIGAZIONE.ds_obbligazione,
	ELEMENTO_VOCE.cd_elemento_voce,ELEMENTO_VOCE.ds_elemento_voce,
 	0 impegnato,
        (SELECT  Nvl(SUM(im_riga),0)
            FROM  DOCUMENTO_GENERICO_RIGA riga
	    Where
	        riga.stato_Cofi!='A' And
	    	riga.Cd_tipo_documento_amm	= 'GENERICO_S'	    AND
	    	EXISTS ( SELECT 1 FROM ASS_INV_BENE_FATTURA Ass
	    	WHERE
	    	Ass.Esercizio_doc_gen 	= riga.ESERCIZIO 		    AND
	    	Ass.Cd_Cds_doc_gen 		= riga.Cd_Cds 			    AND
	    	Ass.Cd_Uo_doc_gen		= riga.Cd_Unita_Organizzativa 	    AND
	    	Ass.Cd_tipo_documento_amm 	= riga.Cd_tipo_documento_amm 	    AND
	    	Ass.Pg_documento_generico   	= riga.Pg_documento_generico   	    AND
	    	Ass.Progressivo_Riga_doc_gen 	= riga.Progressivo_Riga		 )  And
 	        riga.esercizio_obbligazione 	= OBBLIGAZIONE.ESERCIZIO And
	    	riga.esercizio_ori_obbligazione = OBBLIGAZIONE.esercizio_originale  And
	    	riga.cd_cds_obbligazione        = OBBLIGAZIONE.cd_cds  AND
	    	riga.pg_obbligazione 		= OBBLIGAZIONE.pg_obbligazione	    AND
	    	riga.pg_obbligazione_scadenzario= OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario ) associato
		FROM OBBLIGAZIONE,ELEMENTO_VOCE,OBBLIGAZIONE_SCADENZARIO
		WHERE
		ELEMENTO_VOCE.ESERCIZIO 	= OBBLIGAZIONE.ESERCIZIO 	    AND
		ELEMENTO_VOCE.ti_appartenenza   = OBBLIGAZIONE.ti_appartenenza 	    AND
		ELEMENTO_VOCE.ti_gestione 	= OBBLIGAZIONE.ti_gestione 	    AND
		ELEMENTO_VOCE.cd_elemento_voce  = OBBLIGAZIONE.cd_elemento_voce	    AND
		OBBLIGAZIONE_SCADENZARIO.ESERCIZIO 		= OBBLIGAZIONE.ESERCIZIO	    AND
  		OBBLIGAZIONE_SCADENZARIO.cd_cds			= OBBLIGAZIONE.cd_cds		    AND
  		OBBLIGAZIONE_SCADENZARIO.esercizio_originale	= OBBLIGAZIONE.esercizio_originale  AND
  		OBBLIGAZIONE_SCADENZARIO.pg_obbligazione 	= OBBLIGAZIONE.pg_obbligazione	    AND
		FL_INV_BENI_PATR ='Y'
  	) A
Group BY A.ESERCIZIO, A.cd_cds, A.esercizio_originale,A.CD_UNITA_ORGANIZZATIVA, A.pg_obbligazione, a.pg_obbligazione_scadenzario, A.ds_obbligazione,
a.cd_elemento_voce,a.ds_elemento_voce
HAVING ((SUM(impegnato)!= SUM(associato)) And
 	 Round(SUM(associato)*1.2,2)!=SUM(impegnato)  And
 	 Round(SUM(associato)*1.1,2)!=SUM(impegnato)  And
 	 Round(SUM(associato)*1.04,2)!=SUM(impegnato) And
 	 Round(SUM(associato)*1.21,2)!=SUM(impegnato) And
 	 Round(SUM(associato)*1.22,2)!=SUM(impegnato));
