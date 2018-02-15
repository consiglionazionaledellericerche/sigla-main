--------------------------------------------------------
--  DDL for Package CNRUTL001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRUTL001" As
-- =================================================================================================
--
-- CNRUTL001 - Package di utilita'
--
-- Date: 31/12/2004
-- Version: 1.0
--
-- Creazione Package.
--
-- FUNCTION getValore: Restituisce il valore del campo 'COLONNA' della tabella 'TABELLA' relativo alla PK
-- FUNCTION getPgProgettoPadre: Restituisce il progetto padre
--
-- Date: 22/02/2005
-- Version: 1.1
--
-- Aggiunte le funzioni che restituiscono il record dei parametri
--
-- =================================================================================================
  type rec_pk is record(tabella   Varchar2(100),
                        colonna   Varchar2(100),
                        posizione NUMBER(1));
  type tab_pk is table of rec_pk index by binary_integer;
  tb   tab_pk;
---
  FUNCTION getValore( TABELLA  In VARCHAR2,
                      COLONNA  In VARCHAR2,
                      PK1      In VARCHAR2 Default Null,
                      PK2      In VARCHAR2 Default Null,
                      PK3      In VARCHAR2 Default Null,
                      PK4      In VARCHAR2 Default Null,
                      PK5      In VARCHAR2 Default Null,
                      PK6      In VARCHAR2 Default Null)
  RETURN VARCHAR2;
---
  FUNCTION getPgProgettoPadre( Progressivo_Progetto  In NUMBER Default Null,
                               Codice_Progetto       In VARCHAR2 Default Null,
                               esercizio_progetto    In NUMBER,
                               tipo_fase_progetto    In VARCHAR2 )
  RETURN NUMBER;
---
  FUNCTION getRecParametriCnr( inEsercizio  In NUMBER)
  Return PARAMETRI_CNR%Rowtype;
---
  FUNCTION getRecParametriEnteAttivo
  Return PARAMETRI_ENTE%Rowtype;
---
  FUNCTION getRecParametriCds( inEsercizio  In NUMBER,
                               inCds        In VARCHAR2)
  Return PARAMETRI_CDS%Rowtype;
---
  FUNCTION getCdsFromCdr( cd_centro_resp In VARCHAR2)
  RETURN VARCHAR2;
End;
