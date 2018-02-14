--------------------------------------------------------
--  DDL for View V_TERZI_DA_CONGUAGLIARE_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERZI_DA_CONGUAGLIARE_DET" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_TERZO", "DENOMINAZIONE", "CD_TIPO_RAPPORTO", "TIPOLOGIA", "CONTATORE") AS 
  Select
--
-- Date: 10/11/2005
-- Version: 1.0
--
-- View per la consultazione dei Terzi per i quali e' necessario effettuare il conguaglio
--
-- History:
-- Date: 10/11/2005
-- Version: 1.0
-- Creazione
--
-- Date: 24/01/2006
-- Version: 1.1
--
-- Aggiunto anche il Tipo Rapporto per visualizzarlo nella Consultazione "Terzi da Conguagliare"
--
-- Body:
--
	To_Number(To_Char(C.dt_emissione_mandato,'yyyy')), C.cd_cds_compenso, C.cd_uo_compenso, C.cd_terzo,
	decode(A.cognome,Null,A.ragione_sociale,A.cognome||' '||A.nome),C.cd_tipo_rapporto, 'A' tipologia,
	-- ROSPUC 28/09/2017
	-- SOSPESA GESTIONE CHE PREVEDEVA CHE SE PRESENTE UN COMPENSO SAC, LA CONSULTAZIONE PREVEDA IL CONGUAGLIO DELLA SAC
	-- decode(C.cd_cds_compenso, '000',999999999,Count(1)) contatore
	  Count(1) contatore
From   V_COMPENSO_CONGUAGLIO_BASE C, TERZO T, ANAGRAFICO A
Where  C.is_associato_conguaglio = 'N' AND
       C.stato_cofi = 'P' And
       C.cd_terzo = T.cd_terzo And
       A.cd_anag = T.cd_anag And
       Exists(Select 1 From tipo_trattamento,ass_ti_rapp_ti_tratt
       Where
       ass_ti_rapp_ti_tratt.cd_tipo_rapporto = c.cd_tipo_rapporto               And
       ass_ti_rapp_ti_tratt.cd_trattamento   = tipo_trattamento.cd_trattamento  And
       FL_DEFAULT_CONGUAGLIO    ='Y')
Group By To_Number(To_Char(C.dt_emissione_mandato,'yyyy')), C.cd_cds_compenso, C.cd_uo_compenso, C.cd_terzo,
	decode(A.cognome,Null,A.ragione_sociale,A.cognome||' '||A.nome),C.cd_tipo_rapporto,'A'
Union All
(Select Distinct R.esercizio+1, R.cd_cds_conguaglio, R.cd_uo_conguaglio, T.cd_terzo,
	decode(A.cognome,Null,A.ragione_sociale,A.cognome||' '||A.nome),Null,'B' tipologia,1 contatore
From RATEIZZA_CLASSIFIC_CORI R, TERZO T, ANAGRAFICO A
Where R.cd_anag = T.cd_anag
And A.cd_anag = R.cd_anag
And Nvl(R.im_da_rateizzare,0) != Nvl(R.im_rateizzato,0)
And Not Exists  (Select '1' From V_COMPENSO_CONGUAGLIO_BASE C
		 Where  C.is_associato_conguaglio = 'N' AND
                        C.stato_cofi = 'P' And
                        C.cd_terzo = T.cd_terzo And
                        To_Char(C.dt_emissione_mandato,'yyyy') = To_Char(R.esercizio+1)))
Union All
(select c.esercizio_compenso, c.cd_cds_compenso, c.cd_uo_compenso, c.cd_terzo,
        DECODE (a.cognome,NULL,a.ragione_sociale,a.cognome || ' ' || a.nome),c.cd_tipo_rapporto,
        'C' tipologia, 1 contatore
       FROM v_compenso_conguaglio_base c, terzo t, anagrafico a
      WHERE c.is_associato_conguaglio = 'Y'
        AND c.is_compenso_conguaglio = 'Y'
        AND c.stato_cofi = 'C'
        AND c.cd_terzo = t.cd_terzo
        AND a.cd_anag = t.cd_anag
   GROUP BY c.esercizio_compenso,c.cd_cds_compenso,c.cd_uo_compenso,c.cd_terzo,
            DECODE (a.cognome,NULL, a.ragione_sociale,a.cognome || ' ' || a.nome),c.cd_tipo_rapporto,'C')
Order By TIPOLOGIA, CD_TERZO, CONTATORE Desc;
