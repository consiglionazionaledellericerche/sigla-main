--------------------------------------------------------
--  DDL for Package Body CNRUTL001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRUTL001" Is
  --
  FUNCTION getCdsFromCdr(cd_centro_resp In VARCHAR2)
  RETURN VARCHAR2 Is
    cd_cds unita_organizzativa.cd_unita_organizzativa%Type;
  Begin
    Select unita_organizzativa.cd_unita_padre
    Into cd_cds
    From unita_organizzativa, cdr
    Where cdr.cd_centro_responsabilita = cd_centro_resp
      And cdr.cd_unita_organizzativa = unita_organizzativa.cd_unita_organizzativa;
    Return cd_cds;
  End getCdsFromCdr;
  --
  FUNCTION getPgProgettoPadre( Progressivo_Progetto  In NUMBER Default Null,
                               Codice_Progetto       In VARCHAR2 Default Null,
                               esercizio_progetto    In NUMBER,
                               tipo_fase_progetto    In VARCHAR2)
  RETURN NUMBER Is
    Cursor Progressivo (Pg In NUMBER,es In NUMBER,fase In VARCHAR2) Is
      Select PG_PROGETTO, PG_PROGETTO_PADRE
      From PROGETTO
      Where PG_PROGETTO = Pg And
            esercizio   = es And
            tipo_fase   = fase;
    Pg_Padre   NUMBER(8);
  Begin
    If Progressivo_Progetto Is Not Null Then
      Pg_Padre := Progressivo_Progetto;
    Elsif Codice_Progetto Is Not Null Then
      Begin
        Select Pg_Progetto
        Into Pg_Padre
        From Progetto
        Where Cd_Progetto = Codice_Progetto And
              esercizio   = esercizio_progetto And
              tipo_fase   = tipo_fase_progetto;
      Exception
        When No_Data_Found Then
          Return Null;
      End;
    Else
      Return Null;
    End If;
    If Pg_Padre Is Null Then
      Return Null;
    Else
      While Pg_Padre Is Not Null Loop
        For Rec In Progressivo(Pg_Padre,esercizio_progetto,tipo_fase_progetto) Loop
          If Rec.PG_PROGETTO_PADRE Is Null Then
            Return Rec.PG_PROGETTO;
          End If;
          Pg_Padre := Rec.PG_PROGETTO_PADRE;
        End Loop;
      End Loop;
      Return Pg_Padre;
    End If;
  End getPgProgettoPadre;
  --
  FUNCTION getValore( TABELLA  In VARCHAR2,
                      COLONNA  In VARCHAR2,
                      PK1      In VARCHAR2 Default Null,
                      PK2      In VARCHAR2 Default Null,
                      PK3      In VARCHAR2 Default Null,
                      PK4      In VARCHAR2 Default Null,
                      PK5      In VARCHAR2 Default Null,
                      PK6      In VARCHAR2 Default Null)
  RETURN VARCHAR2  Is
    CURSOR CHIAVI IS
      Select COLUMN_NAME
        From USER_CONS_COLUMNS,USER_CONSTRAINTS
       Where USER_CONS_COLUMNS.CONSTRAINT_NAME = USER_CONSTRAINTS.CONSTRAINT_NAME
         And USER_CONSTRAINTS.CONSTRAINT_TYPE  = 'P'
         And USER_CONSTRAINTS.TABLE_NAME       = Upper(TABELLA)
       ORDER BY POSITION;
    I           NUMBER;
    Indice      NUMBER:=0;
    Indice_c    NUMBER:=0;
    TESTO       VARCHAR2(4000);
    COLONNA_DES VARCHAR2(4000);
    DESCRIZIONE VARCHAR2(4000);
    cursor_id   INTEGER;
    ret_code    INTEGER;
    Type COL_PK is table of VARCHAR2(4000)
    Index by binary_integer;
    COLONNE_PK COL_PK;
  Begin
    COLONNE_PK.Delete;
    I := 0;
    For Indice In 1..Tb.Count Loop
      If tb(Indice).tabella  = Upper(Tabella)Then
        I := I + 1;
        If tb(Indice).posizione = 1 Then
          COLONNE_PK(tb(Indice).posizione) := tb(Indice).colonna||' = '||''''||PK1||'''';
        Elsif tb(Indice).posizione = 2 Then
          COLONNE_PK(tb(Indice).posizione) := tb(Indice).colonna||' = '||''''||PK2||'''';
        Elsif tb(Indice).posizione = 3 Then
          COLONNE_PK(tb(Indice).posizione) := tb(Indice).colonna||' = '||''''||PK3||'''';
        Elsif tb(Indice).posizione = 4 Then
          COLONNE_PK(tb(Indice).posizione) := tb(Indice).colonna||' = '||''''||PK4||'''';
        Elsif tb(Indice).posizione = 5 Then
          COLONNE_PK(tb(Indice).posizione) := tb(Indice).colonna||' = '||''''||PK5||'''';
        Elsif tb(Indice).posizione = 6 Then
          COLONNE_PK(tb(Indice).posizione) := tb(Indice).colonna||' = '||''''||PK6||'''';
        End If;
      End If;
    End Loop;
    If COLONNE_PK.count = 0 Then
      Indice_c := Tb.Count;
      For REC IN CHIAVI LOOP
          DBMS_OUTPUT.PUT_LINE('SELECT ');
        I:=I+1;
        Indice_c := Indice_c + 1;
        If I=1 THEN
          COLONNE_PK(I) := REC.COLUMN_NAME||' = '||''''||PK1||'''';
        Elsif I=2 THEN
          COLONNE_PK(I) := REC.COLUMN_NAME||' = '||''''||PK2||'''';
        Elsif I=3 THEN
          COLONNE_PK(I) := REC.COLUMN_NAME||' = '||''''||PK3||'''';
        Elsif I=4 THEN
          COLONNE_PK(I) := REC.COLUMN_NAME||' = '||''''||PK4||'''';
        Elsif I=5 THEN
          COLONNE_PK(I) := REC.COLUMN_NAME||' = '||''''||PK5||'''';
        Elsif I=6 THEN
          COLONNE_PK(I) := REC.COLUMN_NAME||' = '||''''||PK6||'''';
        End IF;
        tb(Indice_c).tabella   := Upper(Tabella);
        tb(Indice_c).colonna   := REC.COLUMN_NAME;
        tb(Indice_c).posizione := I;
      End LOOP;
    End If;
    TESTO := 'SELECT '||Upper(COLONNA)||' FROM '||TABELLA||' WHERE ';
    For J In 1..I Loop
      TESTO := TESTO||' '||COLONNE_PK(J);
      Begin
        IF COLONNE_PK(J+1) IS NULL THEN
          EXIT;
        ELSE
          TESTO := TESTO||' AND ';
        END IF;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          EXIT;
      END;
    End Loop;
    Declare
      valore_ritorno INTEGER;
      errore         varchar2(4000);
    Begin
      cursor_id := dbms_sql.open_cursor;
      dbms_sql.parse(cursor_id, TESTO, Dbms_Sql.V7);
      dbms_sql.define_column(cursor_id,1,descrizione,2000);
      ret_code := dbms_sql.execute(cursor_id);
      Loop
        exit when dbms_sql.fetch_rows(cursor_id) = 0;
        dbms_sql.column_value(cursor_id,1,descrizione);
      End loop;
      dbms_sql.close_cursor(cursor_id);
      valore_ritorno := dbms_sql.last_sql_function_code;
      errore := dbms_sql.last_ERROR_POSITION;
    Exception
      When OTHERS THEN
        If DBMS_SQL.IS_OPEN(cursor_id) THEN
          dbms_sql.close_cursor(cursor_id);
        End IF;
        RETURN('Errore: '||Sqlerrm);
    END;
    Return Ltrim(Rtrim(DESCRIZIONE));
  End getValore;
  --
  FUNCTION getRecParametriCnr( inEsercizio  In NUMBER)
  Return PARAMETRI_CNR%Rowtype Is
    rec_parametri_cnr PARAMETRI_CNR%Rowtype;
  Begin
    Select *
    Into rec_parametri_cnr
    From Parametri_CNR
    Where Esercizio = inEsercizio;
    Return rec_parametri_cnr;
  Exception
    When No_Data_Found then
      IBMERR001.RAISE_ERR_GENERICO('Parametri_CNR non trovati per l''anno:'||To_Char(inEsercizio));
  End getRecParametriCnr;
  --
  Function getRecParametriEnteAttivo
  Return PARAMETRI_ENTE%Rowtype Is
    rec_parametri_ente_attivo PARAMETRI_ENTE%Rowtype;
  Begin
    Begin
      Select *
      Into rec_parametri_ente_attivo
      From Parametri_ENTE
      Where attivo = 'Y';
      Return rec_parametri_ente_attivo;
    Exception
        When Too_Many_Rows Then
           Return Null;
        When No_Data_Found then
           IBMERR001.RAISE_ERR_GENERICO('Parametri_ENTE non trovati');
    End;
  End getRecParametriEnteAttivo;
  --
  FUNCTION getRecParametriCds( inEsercizio  In NUMBER,
                               inCds        In VARCHAR2)
  Return PARAMETRI_CDS%Rowtype Is
      rec_parametri_cds PARAMETRI_CDS%Rowtype;
  Begin
    Select *
    Into rec_parametri_cds
    From Parametri_CDS
    Where cd_cds = inCds
      And Esercizio = inEsercizio;
    Return rec_parametri_cds;
  Exception
    When No_Data_Found then
       IBMERR001.RAISE_ERR_GENERICO('Parametri_CDS non trovati per l''anno: '||To_Char(inEsercizio)||' e Cds: '||inCds);
  End getRecParametriCds;
End CNRUTL001;
