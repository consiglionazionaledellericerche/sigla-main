--------------------------------------------------------
--  DDL for Package PRT_CIR_SITCAS
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "PRT_CIR_SITCAS" IS
--
-- PRT_CIR_SITCAS - Il package contiene funzioni per il calcolo di importi
--                  utili alla stampa della Situazione di Cassa ma non solo
--
-- Date: 06/08/2004
-- Version: 1.1
-- Package per il calcolo degli importi della SItuazione di Cassa
--
-- Dependency: nessuna dipendenza
--
-- History:
--
-- Date: 08/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 06/08/2004
-- Version: 1.1
-- Rinominati il nome del package e della procedure che lo richiama
--
-- Body
--
FUNCTION fondo_iniziale(
  P_Anno IN number ,
  P_CDS IN varchar2,
  P_Stringa IN varchar2 )
RETURN NUMBER;

FUNCTION Tot_mandati(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_EME_INV_RIS IN char ,     -- Emessi/Inviati/Riscontrati/in Distinta
  P_Stringa IN varchar2 )
RETURN NUMBER;

FUNCTION Tot_mandati(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data_Man IN date ,
  P_A_Data_Man IN date ,
  P_DA_Data_Eme_dis IN date ,
  P_A_Data_Eme_dis IN date ,
  P_DA_Data_Invio_dis IN date ,
  P_A_Data_Invio_dis IN date ,
  P_EME_INV_RIS IN char ,     -- Emessi/Inviati/Riscontrati/in Distinta
  P_Stringa IN varchar2 )
RETURN NUMBER;

FUNCTION Tot_reversali(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_EME_INV_RIS IN char ,     -- Emessi/Inviati/Riscontrati/in Distinta
  P_PRO_DEF  IN CHAR,         -- Provvisorie / Definitive
  P_Stringa IN varchar2)
RETURN NUMBER;

FUNCTION Tot_reversali(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data_ris IN date ,
  P_A_Data_ris IN date ,
  P_DA_Data_Eme_dis IN date ,
  P_A_Data_Eme_dis IN date ,
  P_DA_Data_Invio_dis IN date ,
  P_A_Data_Invio_dis IN date ,
  P_EME_INV_RIS IN char ,     -- Emessi/Inviati/Riscontrati/in Distinta
  P_PRO_DEF  IN CHAR,         -- Provvisorie / Definitive
  P_Stringa IN varchar2 )
RETURN NUMBER;

Function Tot_sospesi_e_s(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_TIPO IN varchar2 ,
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_FL_STORNATI In VARCHAR2, -- Y/N
  P_Stringa IN varchar2 )
RETURN NUMBER;

Function Tot_sospesi_riscossi(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data IN DATE,
  P_A_Data IN DATE,
  P_Stringa IN varchar2 )
RETURN NUMBER;

FUNCTION Tot_sospesi_riscossi(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data_Sos IN DATE,
  P_A_Data_Sos IN DATE,
  P_DA_Data_Ris IN DATE,
  P_A_Data_Ris IN DATE,
  P_DA_Data_Invio IN DATE,
  P_A_Data_Invio IN DATE,
  P_Stringa IN varchar2 )
RETURN NUMBER;

Function Tot_sospesi_pagati(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data IN DATE,
  P_A_Data IN DATE,
  P_Stringa IN varchar2 )
RETURN NUMBER;

Function Tot_sospesi_pagati(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data_Sos IN DATE,
  P_A_Data_Sos IN DATE,
  P_DA_Data_Man IN DATE,
  P_A_Data_Man IN DATE,
  P_DA_Data_Invio IN DATE,
  P_A_Data_Invio IN DATE,
  P_Stringa IN varchar2 )
RETURN NUMBER;

Function TOT_LETTERE_PAGAMENTO_ASS(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2 )
RETURN NUMBER;

FUNCTION TOT_LETTERE_PAGAMENTO_NON_ASS(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2 )
RETURN NUMBER;

FUNCTION Tot_mandati_RISCONTRATI(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2 )
RETURN NUMBER;

FUNCTION Tot_mandati_RISCONTRATI(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data_EME_DIS In date ,
  P_A_Data_EME_DIS In date ,
  P_Da_Data_RISCONTRO IN date ,
  P_a_Data_RISCONTRO IN date ,
  P_Stringa IN varchar2 )
RETURN NUMBER;

FUNCTION Tot_reversali_RISCONTRATE(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2)
RETURN NUMBER;

FUNCTION Tot_reversali_RISCONTRATE(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data_Eme_dis IN date ,
  P_A_Data_Eme_dis IN date ,
  P_DA_Data_riscontro IN DATE,
  P_A_Data_riscontro IN DATE,
  P_Stringa IN varchar2 )
RETURN NUMBER;
FUNCTION Tot_reversali(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_EME_INV_RIS IN char ,     -- Emessi/Inviati/Riscontrati/in Distinta
  P_PRO_DEF  IN CHAR,         -- Provvisorie / Definitive
  P_Stringa IN varchar2,
  P_coll_mand in varchar2 )
RETURN NUMBER;

FUNCTION Tot_reversali(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data_ris IN date ,
  P_A_Data_ris IN date ,
  P_DA_Data_Eme_dis IN date ,
  P_A_Data_Eme_dis IN date ,
  P_DA_Data_Invio_dis IN date ,
  P_A_Data_Invio_dis IN date ,
  P_EME_INV_RIS IN char ,     -- Emessi/Inviati/Riscontrati/in Distinta
  P_PRO_DEF  IN CHAR,         -- Provvisorie / Definitive
  P_Stringa IN varchar2 ,
  P_coll_mand in varchar2)
RETURN NUMBER;

FUNCTION Tot_reversali_RISCONTRATE(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2,
  P_coll_mand in varchar2 )
RETURN NUMBER;

FUNCTION Tot_reversali_RISCONTRATE(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data_Eme_dis IN date ,
  P_A_Data_Eme_dis IN date ,
  P_DA_Data_riscontro IN DATE,
  P_A_Data_riscontro IN DATE,
  P_Stringa IN varchar2 ,
  P_coll_mand in varchar2 )
RETURN NUMBER;

END PRT_CIR_SITCAS;
