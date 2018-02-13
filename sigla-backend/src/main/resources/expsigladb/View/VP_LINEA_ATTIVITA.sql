--------------------------------------------------------
--  DDL for View VP_LINEA_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_LINEA_ATTIVITA" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "CD_TIPO_LINEA_ATTIVITA", "DENOMINAZIONE", "CD_GRUPPO_LINEA_ATTIVITA", "CD_FUNZIONE", "CD_NATURA", "DS_LINEA_ATTIVITA", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_CDR_COLLEGATO", "CD_LA_COLLEGATO", "DS_CDR", "PG_RISULTATO", "CD_TIPO_RISULTATO", "DS_RISULTATO", "QUANTITA", "DS_TIPO_RISULTATO", "DS_NATURA", "DS_FUNZIONE") AS 
  (select
--
-- Date: 20/11/2002
-- Version: 1.0
--
-- Vista di stampa delle linee di attivita
--
-- History:
--
-- Date: 20/11/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--  
-- Table: LINEA_ATTIVITA  
/*   1 */  a.ESERCIZIO_INIZIO  
/*   2 */ ,a.CD_CENTRO_RESPONSABILITA  
/*   3 */ ,a.CD_LINEA_ATTIVITA  
/*   4 */ ,a.CD_TIPO_LINEA_ATTIVITA  
/*   5 */ ,a.DENOMINAZIONE  
/*   6 */ ,a.CD_GRUPPO_LINEA_ATTIVITA  
/*   7 */ ,a.CD_FUNZIONE  
/*   8 */ ,a.CD_NATURA  
/*   9 */ ,a.DS_LINEA_ATTIVITA  
/*  10 */ ,a.DACR  
/*  11 */ ,a.UTCR  
/*  12 */ ,a.DUVA  
/*  13 */ ,a.UTUV  
/*  14 */ ,a.PG_VER_REC  
/*  15 */ ,a.CD_CDR_COLLEGATO  
/*  16 */ ,a.CD_LA_COLLEGATO  
-- Table: CDR  
/*  20 */ ,b.DS_CDR  
-- Table: RISULTATO  
/*  34 */ ,c.PG_RISULTATO  
/*  35 */ ,c.CD_TIPO_RISULTATO  
/*  36 */ ,c.DS_RISULTATO  
/*  37 */ ,c.QUANTITA  
-- Table: TIPO_RISULTATO  
/*  44 */ ,d.DS_TIPO_RISULTATO  
-- Table: NATURA  
/*  56 */ ,e.DS_NATURA  
-- Table: FUNZIONE  
/*  63 */ ,f.DS_FUNZIONE  
from LINEA_ATTIVITA a,CDR b,RISULTATO c,TIPO_RISULTATO d,NATURA e,FUNZIONE f where  
a.CD_CENTRO_RESPONSABILITA=b.CD_CENTRO_RESPONSABILITA  
and a.CD_CENTRO_RESPONSABILITA=c.CD_CENTRO_RESPONSABILITA(+)  
and a.CD_LINEA_ATTIVITA=c.CD_LINEA_ATTIVITA(+)  
and c.CD_TIPO_RISULTATO=d.CD_TIPO_RISULTATO(+)  
and a.CD_NATURA=e.CD_NATURA  
and a.CD_FUNZIONE=f.CD_FUNZIONE  
);
