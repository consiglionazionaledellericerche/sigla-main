--------------------------------------------------------
--  DDL for Package CIR_FUNZGEST
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CIR_FUNZGEST" IS

FUNCTION PDG_PREVISIONE_SPE(
  P_Anno IN number,
  P_CDS IN varchar2,
  P_UO  IN varchar2,
  P_ELE_VOCE IN varchar2,
  P_LINEA IN varchar2,
  P_TIPO  IN VARCHAR2,  -- (P)erson, (F)unzion, (I)nvest, Prestaz (S)cient, (B)orse
  P_Stringa IN varchar2)
RETURN NUMBER;

FUNCTION PDG_PREVISIONE_ENT(
  P_Anno IN number,
  P_CDS IN varchar2,
  P_UO  IN varchar2,
  P_ELE_VOCE IN varchar2,
  P_LINEA IN varchar2,
  P_Stringa IN varchar2)
RETURN NUMBER;

FUNCTION Tot_Obbligazioni(
  P_Anno IN number,
  P_CDS IN varchar2,
  P_UO  IN varchar2,
  P_ELE_VOCE IN varchar2,
  P_LINEA IN varchar2,
  P_PAGATE IN varchar2,    -- Si /No /Tutte
  P_TIPO  IN VARCHAR2,  -- (P)erson, (F)unzion, (I)nvest, Prestaz (S)cient, (B)orse
  P_Stringa IN varchar2)
RETURN NUMBER;

END CIR_FUNZGEST;
