--------------------------------------------------------
--  DDL for View PRT_PREV_DIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_PREV_DIP" ("MATRICOLA", "NOMINATIVO", "RAPPORTO", "ENTE", "CD_TERZO", "SEDE") AS 
  Select Distinct
-- Date: 24/01/2008
-- Version: 1.1
--
-- Elenco iscrizione previdenziale dipendenti
--
-- History:
--
-- Date: 16/05/2003
-- Version: 1.0
--
-- Date: 24/01/2008
-- Version: 1.1
-- Aggiunta la distinct perchè modificata la chiave di cnr_anadip
-- Body
--
  cnr_anadip.matricola, cnr_anadip.nominativo,
                   cnr_anadip.rapp_impiego, cnr_anadip.ente_prev,
                   terzo.cd_terzo, cnr_anadip.tit_afferen
              FROM cnr_anadip, terzo, unita_organizzativa,rapporto,anagrafico
             WHERE anagrafico.codice_fiscale = cnr_anadip.dip_cod_fis
               and anagrafico.cd_anag = terzo.cd_anag
               and terzo.dt_fine_rapporto is null
               and anagrafico.cd_anag = rapporto.cd_anag
               and rapporto.matricola_dipendente = cnr_anadip.matricola
               and unita_organizzativa.cd_unita_organizzativa = SUBSTR (cnr_anadip.tit_afferen, 1, 3)|| '.'|| SUBSTR (cnr_anadip.tit_afferen, 4, 3)
               and cnr_anadip.data_cessazione IS null
               and cnr_anadip.ANNO_RIF = TO_CHAR(SYSDATE, 'YYYY')
               and cnr_anadip.MESE_RIF = (
                SELECT MAX(MESE_RIF) FROM cnr_anadip anadip
                    WHERE ANADIP.MATRICOLA = cnr_anadip.matricola
                      AND ANADIP.ANNO_RIF = cnr_anadip.ANNO_RIF
               )
          ORDER BY cnr_anadip.nominativo;

   COMMENT ON TABLE "PRT_PREV_DIP"  IS 'Elenco iscrizione previdenziale dipendenti';
