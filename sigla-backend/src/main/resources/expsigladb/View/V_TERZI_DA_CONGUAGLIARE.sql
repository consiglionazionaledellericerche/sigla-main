--------------------------------------------------------
--  DDL for View V_TERZI_DA_CONGUAGLIARE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERZI_DA_CONGUAGLIARE" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_TERZO", "DENOMINAZIONE", "CD_TIPO_RAPPORTO", "TIPOLOGIA") AS 
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
  	esercizio, cd_cds, cd_unita_organizzativa, cd_terzo, denominazione,cd_tipo_rapporto, tipologia
 From v_terzi_da_conguagliare_det v
 Where contatore = (Select max(contatore)
                    From v_terzi_da_conguagliare_det d
                    Where v.esercizio = d.esercizio
                      And v.cd_terzo = d.cd_terzo
                      And v.tipologia = d.tipologia
                      And ((v.cd_tipo_rapporto Is Not Null And v.cd_tipo_rapporto = d.cd_tipo_rapporto)
                            Or
                            v.cd_tipo_rapporto Is Null)
                      )
 Order By esercizio, cd_cds, cd_unita_organizzativa, cd_terzo;
