--------------------------------------------------------
--  DDL for Package CAR_SALDI
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CAR_SALDI" as

 procedure creaSALDI(aDeltaSaldo voce_f_saldi_cdr_linea%rowtype);

-- Function getVoce_fdaEV (aEV elemento_voce%Rowtype, aLinea_attivita linea_attivita%Rowtype)  Return  voce_f%rowtype ;

 Function TIPO_VAR_PDG (aEsercizio NUMBER, aNumVar NUMBER)  Return  VARCHAR2;

end;
