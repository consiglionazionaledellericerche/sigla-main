--------------------------------------------------------
--  DDL for Package CNRUTL003
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRUTL003" AS

      FUNCTION cognome(v_str varchar2) RETURN varchar2;
      FUNCTION nome(v_str varchar2) RETURN varchar2;
      FUNCTION data_nascita(v_data varchar2, v_sesso char) RETURN varchar2;
      FUNCTION comune_di_nascita(pg number ) RETURN varchar2;
      FUNCTION codice_controllo(v_str varchar2) RETURN varchar2;
      FUNCTION calcola( v_cognome varchar2,
      									v_nome varchar2,
                        v_data_di_nascita varchar2,
                        v_sesso CHAR,
                        pg_comune_di_nascita number) RETURN varchar2;
  END CNRUTL003;
