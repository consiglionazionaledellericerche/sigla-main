--------------------------------------------------------
--  DDL for View CONTROLLO_730
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "CONTROLLO_730" ("ESERCIZIO", "CODICE_FISCALE", "TRATTAMENTO", "UO", "ANAG", "TERZO", "ITALIANO_ESTERO") AS 
  SELECT DISTINCT esercizio, anagrafico.codice_fiscale, ds_ti_trattamento,
                   compenso.cd_unita_organizzativa, anagrafico.cd_anag, compenso.cd_terzo,
                   anagrafico.ti_italiano_estero
              FROM compenso,anagrafico,terzo,tipo_trattamento
             WHERE compenso.TI_ANAGRAFICO <> 'D'
             		and stato_cofi!='A'
                    and tipo_trattamento.cd_trattamento = compenso.CD_TRATTAMENTO
                  AND tipo_trattamento.dt_ini_validita <= compenso.dt_registrazione
      AND tipo_trattamento.dt_fin_validita >= compenso.dt_registrazione
               and anagrafico.cd_anag = terzo.cd_anag
               and terzo.cd_terzo= compenso.cd_terzo
               AND compenso.cd_trattamento != 'T100'
               AND anagrafico.ti_entita = 'F';
