--------------------------------------------------------
--  DDL for View V_EXT_CASSIERE00
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_EXT_CASSIERE00" ("ESERCIZIO", "NOME_FILE","DATA_INIZIO_RIF","DATA_FINE_RIF") AS 
   SELECT   esercizio, nome_file,to_date(substr(nome_file,7,2)||substr(nome_file,5,2)||substr(nome_file,1,4),'ddmmyyyy'),
   to_date(substr(nome_file,7,2)||substr(nome_file,5,2)||substr(nome_file,1,4),'ddmmyyyy')
       FROM ext_cassiere00
   GROUP BY esercizio, nome_file,to_date(substr(nome_file,7,2)||substr(nome_file,5,2)||substr(nome_file,1,4),'ddmmyyyy'),
   to_date(substr(nome_file,7,2)||substr(nome_file,5,2)||substr(nome_file,1,4),'ddmmyyyy')
   UNION
   SELECT esercizio, identificativo_flusso, data_inizio_periodo_rif,data_fine_periodo_rif
     FROM flusso_giornale_di_cassa
     group by esercizio, identificativo_flusso, data_inizio_periodo_rif,data_fine_periodo_rif;
   COMMENT ON TABLE "V_EXT_CASSIERE00"  IS 'Vista su EXT_CASSIERE00
La vista estrae semplicemente la distinct sui campi ESERCIZIO, NOME_FILE, per
la visualizzazione all''utente dei file presenti nel DB';
