--------------------------------------------------------
--  DDL for View V_DISP_CASSA_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DISP_CASSA_CDS" ("ESERCIZIO", "CD_CDS", "IM_CASSA_INIZIALE", "IM_ACCREDITAMENTI_IC", "IM_ACCREDITAMENTI", "IM_MANDATI", "IM_REVERSALI", "IM_DISPONIBILITA_CASSA") AS 
  SELECT
ESERCIZIO ESERCIZIO,
CD_CDS CD_CDS,
IM_CASSA_INIZIALE IM_CASSA_INIZIALE,
0 IM_ACCREDITAMENTI_IC,
0 IM_ACCREDITAMENTI,
0 IM_MANDATI,
0 IM_REVERSALI,
disp_cassa_mandati(esercizio,cd_cds)
FROM
ESERCIZIO;

   COMMENT ON TABLE "V_DISP_CASSA_CDS"  IS 'Estrae la disponibilita di cassa del CDS, in particolare estrae il fondo di
cassa iniziale, gli accreditamenti emessi ed inviati all''istituto cassiere,
gli accreditamenti emessi, le obbligazioni pagate( mandati emessi + mod 1210), le reversali
non di accreditamento emesse
E'' utilizzata dalla gestione mandato di accreditamento CNR-CdS per
visualizzare il prospetto di disponibilita di cassa di ogni CdS
E'' utilizzata dall gestione mandato per verificare la disponibilita di cassa
prima del salvataggio di un nuovo mandato
Modifica per richiesta CINECA 489 - all''importo dei mandati/reversali viene sommata
la quota disponibile (non ancora associata) dei sospesi di spesa ed entrata';
