--------------------------------------------------------
--  DDL for View V_STIPENDI_MONTANTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STIPENDI_MONTANTI" ("ESERCIZIO", "CD_ANAG", "MATRICOLA", "IMPO_IRPEF_NETTO_DIP", "IMPO_IRPEF_LORDO_DIP", "IMPO_INPS_DIP", "IMPO_INPS_TESORO_DIP", "IMPO_RIDUZ_DIP", "IMPO_FONDO_FS_DIP") AS 
  SELECT
--
-- Date: 03/12/2002
-- Version: 1.0
--
-- Per tutte le matricole, mette a disposizione le informazioni relative ai montanti previdenziali e
-- fiscali derivanti dai compensi liquidati nel corso dei vari esercizi
--
-- History:
--
-- Date: 03/12/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
       esercizio,
       cd_anag,
       matricola,
       SUM(DECODE(pg_classificazione_montanti,1,imponibile_lordo,0)),
       SUM(DECODE(pg_classificazione_montanti,1,imponibile_netto,0)),
       SUM(DECODE(pg_classificazione_montanti,2,imponibile_netto,0)),
       SUM(DECODE(pg_classificazione_montanti,3,imponibile_netto,0)),
       SUM(DECODE(pg_classificazione_montanti,5,imponibile_netto,0)),
       SUM(DECODE(pg_classificazione_montanti,6,imponibile_netto,0))
FROM   V_STIPENDI_MONTANTI_PRE
GROUP BY esercizio,
         cd_anag,
         matricola
;
