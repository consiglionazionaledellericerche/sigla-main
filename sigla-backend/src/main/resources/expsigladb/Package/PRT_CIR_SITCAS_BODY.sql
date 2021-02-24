--------------------------------------------------------
--  DDL for Package Body PRT_CIR_SITCAS
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "PRT_CIR_SITCAS" IS

  PROCEDURE Costruisci_Stringa(
  p_stringa_ini IN OUT varchar2 ,
  p_stringa_agg IN varchar2 );

  FUNCTION Calcola_valore(
  p_stringa IN varchar2 )
RETURN number;

  PROCEDURE Costruisci_Stringa(
  p_stringa_ini IN OUT varchar2 ,
  p_stringa_agg IN varchar2 )
  IS
  Begin
    If instr(upper(p_stringa_ini), 'WHERE') > 0 Then
       p_stringa_ini := p_stringa_ini||' and '||p_stringa_agg;
    Else
       p_stringa_ini := p_stringa_ini||' where '||p_stringa_agg;
    End If;
  End Costruisci_Stringa;

  FUNCTION Calcola_valore(
  p_stringa IN varchar2 )
  RETURN number IS
    valore       number;
    CURSOR_NAME  INTEGER;
    appo         integer;
  Begin
    CURSOR_NAME:=DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(CURSOR_NAME, P_Stringa, DBMS_SQL.V7);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,1,valore);
    appo := DBMS_SQL.execute_and_fetch(cursor_name);
    DBMS_SQL.column_value(cursor_name,1,valore);
    DBMS_SQL.CLOSE_CURSOR(CURSOR_NAME);

    Return(valore);
  End Calcola_valore;

  FUNCTION FONDO_INIZIALE(
  P_Anno IN number ,
  P_CDS IN varchar2,
  P_Stringa IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
  Begin
       Stringa := 'Select NVL(SUM(NVL(ESERCIZIO.IM_CASSA_INIZIALE, 0)), 0) '||
                  ' from ESERCIZIO ';

    If P_CDS is not null Then
       Costruisci_Stringa(Stringa, 'ESERCIZIO.CD_CDS = '''||P_CDS||'''');
    End If;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'ESERCIZIO.ESERCIZIO = '||P_Anno);
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return Calcola_valore(Stringa);
  End FONDO_INIZIALE;

  FUNCTION Tot_mandati(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_EME_INV_RIS IN char ,    -- Emessi/Inviati/Riscontrati/Da consegnare
  P_Stringa IN varchar2 )
  RETURN NUMBER Is
  Begin
    If P_EME_INV_RIS = 'E' Then
      Return Tot_mandati(P_Anno, P_CDS, P_UO, P_Tipo, P_DA_Data, P_A_Data, Null, Null, Null, Null, P_EME_INV_RIS, P_Stringa);
    Elsif P_EME_INV_RIS = 'D' Then
      Return Tot_mandati(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, P_DA_Data, P_A_Data, Null, Null, P_EME_INV_RIS, P_Stringa);
    Elsif P_EME_INV_RIS = 'I' Then
      Return Tot_mandati(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, Null, Null, P_DA_Data, P_A_Data, P_EME_INV_RIS, P_Stringa);
    Else
      Return Tot_mandati(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, Null, Null, Null, Null, P_EME_INV_RIS, P_Stringa);
    End If;
  End;

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
  P_EME_INV_RIS IN char ,    -- Emessi/Inviati/Riscontrati/Da consegnare
  P_Stringa IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
    Totman       NUMBER;
    Totmod       NUMBER;
  parametri_esercizio  parametri_cnr%rowtype;
  CDS_ENTE        VARCHAR2(30);
 begin
 		parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
		CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);
    If nvl(P_EME_INV_RIS, 'E') in ('I', 'D') Then
       Stringa := 'Select nvl(sum(MANDATO.IM_MANDATO), 0) '||
                  'from  MANDATO, DISTINTA_CASSIERE, DISTINTA_CASSIERE_DET '||
                  'WHERE MANDATO.STATO != ''A'' AND '||
                  'MANDATO.CD_CDS     = DISTINTA_CASSIERE_DET.CD_CDS_ORIGINE  AND '||
                  'MANDATO.ESERCIZIO  = DISTINTA_CASSIERE_DET.ESERCIZIO  AND '||
                  'MANDATO.PG_MANDATO = DISTINTA_CASSIERE_DET.PG_MANDATO AND '||
                  'DISTINTA_CASSIERE.CD_CDS                 = DISTINTA_CASSIERE_DET.CD_CDS                 AND '||
                  'DISTINTA_CASSIERE.ESERCIZIO              = DISTINTA_CASSIERE_DET.ESERCIZIO              AND '||
                  'DISTINTA_CASSIERE.CD_UNITA_ORGANIZZATIVA = DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA AND '||
                  'DISTINTA_CASSIERE.PG_DISTINTA            = DISTINTA_CASSIERE_DET.PG_DISTINTA  AND  '||
                  'DISTINTA_CASSIERE.PG_DISTINTA IN(SELECT MIN(DET.PG_DISTINTA) FROM DISTINTA_CASSIERE_DET DET '||
                  'WHERE '||
                  'MANDATO.CD_CDS               = DET.CD_CDS_ORIGINE  AND '||
                  'MANDATO.ESERCIZIO            = DET.ESERCIZIO  AND '||
                  'MANDATO.PG_MANDATO           = DET.PG_MANDATO)';
    Else
       Stringa := 'Select nvl(sum(MANDATO.IM_MANDATO), 0) from MANDATO '||
                  ' WHERE STATO != ''A'' ';
    End If;
		if (parametri_esercizio.fl_tesoreria_unica='N') then
    		If P_CDS is not null Then
      	 	Costruisci_Stringa(Stringa, 'MANDATO.CD_CDS = '''||P_CDS||'''');
    		End If;
		else
  		if (P_CDS!=CDS_ENTE) then
  			Costruisci_Stringa(Stringa, 'MANDATO.CD_CDS_ORIGINE = '''||P_CDS||'''');
  		end if;
  	end if;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'MANDATO.ESERCIZIO = '||P_Anno);
    End If;

    If P_UO is not null Then
       Costruisci_Stringa(Stringa, 'MANDATO.CD_UNITA_ORGANIZZATIVA = '''||P_UO||'''');
    End If;

    If P_TIPO is not null Then
       Costruisci_Stringa(Stringa, 'MANDATO.TI_MANDATO = '''||P_TIPO||'''');
    End If;

-- data emissiome mandato

    If P_DA_Data_Man is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(MANDATO.DT_EMISSIONE, ''yyyymmdd'') >= '''||
                              to_char(P_DA_DATA_man, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_Man is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(MANDATO.DT_EMISSIONE, ''yyyymmdd'') <= '''||
                              to_char(P_A_DATA_man, 'yyyymmdd')||'''');
    End If;

-- data emissione distinta

    If P_DA_Data_Eme_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_EMISSIONE, ''yyyymmdd'') >= '''||
                              to_char(P_DA_DATA_eme_dis, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_Eme_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_EMISSIONE, ''yyyymmdd'') <= '''||
                              to_char(P_A_DATA_eme_dis, 'yyyymmdd')||'''');
    End If;

-- data invio distinta

    If P_DA_Data_invio_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_INVIO, ''yyyymmdd'') >= '''||
                              to_char(P_DA_DATA_invio_dis, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_invio_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_INVIO, ''yyyymmdd'') <= '''||
                              to_char(P_A_DATA_invio_dis, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Totman := Calcola_valore(Stringa);

    Return (NVL(Totman, 0));
  End Tot_mandati;

FUNCTION Tot_reversali(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_EME_INV_RIS IN char ,    -- Emessi/Inviati/Riscontrati/Da consegnare
  P_PRO_DEF  IN CHAR,      -- PROVVISORIE/DEFINITIVE
  P_Stringa IN varchar2 )
  RETURN NUMBER Is
begin
  Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, P_DA_Data, P_A_Data, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,null);
end;

FUNCTION Tot_reversali(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_EME_INV_RIS IN char ,    -- Emessi/Inviati/Riscontrati/Da consegnare
  P_PRO_DEF  IN CHAR,      -- PROVVISORIE/DEFINITIVE
  P_Stringa IN varchar2 ,
  P_coll_mand in varchar2)
  RETURN NUMBER Is
  Begin
   If P_EME_INV_RIS = 'E' Then
     Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, P_DA_Data, P_A_Data, Null, Null, Null, Null, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,P_coll_mand);
   Elsif P_EME_INV_RIS = 'D' Then
     Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, P_DA_Data, P_A_Data, Null, Null, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,P_coll_mand);
   Elsif P_EME_INV_RIS = 'I' Then
     Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, Null, Null, P_DA_Data, P_A_Data, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,P_coll_mand);
   Else
     Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, Null, Null, Null, Null, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,P_coll_mand);
   End If;
  End;

FUNCTION Tot_reversali(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO IN varchar2 ,
  P_TIPO IN varchar2 ,
  P_DA_Data_ris IN date ,
  P_A_Data_ris IN date ,
  P_DA_Data_Eme_dis IN date ,
  P_A_Data_Eme_dis IN date ,
  P_DA_Data_Invio_dis IN date ,
  P_A_Data_Invio_dis IN date ,
  P_EME_INV_RIS IN char ,  -- Emessi/Inviati/Riscontrati/Da consegnare
  P_PRO_DEF  IN CHAR,      -- PROVVISORIE/DEFINITIVE
  P_Stringa IN varchar2)
  RETURN NUMBER IS
  	Begin
   If P_EME_INV_RIS = 'E' Then
     Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, P_DA_Data_ris, P_A_Data_ris, Null, Null, Null, Null, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,null);
   Elsif P_EME_INV_RIS = 'D' Then
     Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, P_DA_Data_Eme_dis, P_A_Data_Eme_dis, Null, Null, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,null);
   Elsif P_EME_INV_RIS = 'I' Then
     Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, Null, Null, P_DA_Data_Invio_dis, P_A_Data_Invio_dis, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,null);
   Else
     Return Tot_reversali(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, Null, Null, Null, Null, P_EME_INV_RIS, P_PRO_DEF, P_Stringa,null);
   End If;
  End;
FUNCTION Tot_reversali(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO IN varchar2 ,
  P_TIPO IN varchar2 ,
  P_DA_Data_ris IN date ,
  P_A_Data_ris IN date ,
  P_DA_Data_Eme_dis IN date ,
  P_A_Data_Eme_dis IN date ,
  P_DA_Data_Invio_dis IN date ,
  P_A_Data_Invio_dis IN date ,
  P_EME_INV_RIS IN char ,  -- Emessi/Inviati/Riscontrati/Da consegnare
  P_PRO_DEF  IN CHAR,      -- PROVVISORIE/DEFINITIVE
  P_Stringa IN varchar2 ,
  P_coll_mand in varchar2)
  RETURN NUMBER IS
  Stringa      varchar2(2500) := null;
  parametri_esercizio  parametri_cnr%rowtype;
  CDS_ENTE        VARCHAR2(30);
 begin
 		parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
		CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);

    If nvl(P_EME_INV_RIS, 'E') IN ('I', 'D') Then
        Stringa := 'Select nvl(sum(REVERSALE.IM_REVERSALE), 0) '||
          'FROM  REVERSALE, DISTINTA_CASSIERE, DISTINTA_CASSIERE_DET '||
          'WHERE REVERSALE.STATO != ''A'' AND '||
          '      REVERSALE.CD_CDS              = DISTINTA_CASSIERE_DET.CD_CDS_ORIGINE AND '||
          '      REVERSALE.ESERCIZIO           = DISTINTA_CASSIERE_DET.ESERCIZIO  AND '||
          '      REVERSALE.PG_REVERSALE        = DISTINTA_CASSIERE_DET.PG_REVERSALE AND '||
          '      DISTINTA_CASSIERE.CD_CDS      = DISTINTA_CASSIERE_DET.CD_CDS AND '||
          '      DISTINTA_CASSIERE.ESERCIZIO   = DISTINTA_CASSIERE_DET.ESERCIZIO AND '||
          '      DISTINTA_CASSIERE.CD_UNITA_ORGANIZZATIVA = DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA AND '||
          '      DISTINTA_CASSIERE.PG_DISTINTA = DISTINTA_CASSIERE_DET.PG_DISTINTA AND '||
          '      DISTINTA_CASSIERE.PG_DISTINTA IN('||
          '        SELECT MIN(DET.PG_DISTINTA) FROM DISTINTA_CASSIERE_DET DET '||
          '        WHERE '||
          '        REVERSALE.CD_CDS               = DET.CD_CDS_ORIGINE AND '||
          '        REVERSALE.ESERCIZIO            = DET.ESERCIZIO AND '||
          '        REVERSALE.PG_REVERSALE         = DET.PG_REVERSALE )';
    Else
       Stringa := 'Select nvl(sum(REVERSALE.im_REVERSALE), 0) from REVERSALE '||
                  ' WHERE STATO != ''A'' ';
    End If;
		if (parametri_esercizio.fl_tesoreria_unica='N') then
    		If P_CDS is not null Then
      	 	     Costruisci_Stringa(Stringa, 'REVERSALE.CD_CDS = '''||P_CDS||'''');
    		End If;
		else
  		if (P_CDS!=CDS_ENTE) then
  			     Costruisci_Stringa(Stringa, 'REVERSALE.CD_CDS_origine = '''||P_CDS||'''');
  		end if;
  	end if;
  	if(P_coll_mand is not null and P_coll_mand='S' ) then
	   Costruisci_Stringa(Stringa, 'exists(select 1 from ass_mandato_reversale ass,mandato '||
	    'where '||
	    'ass.pg_reversale=reversale.pg_reversale and '||
	    'ass.cd_cds_reversale=reversale.cd_cds and '||
	    'ass.esercizio_reversale=reversale.esercizio and '||
	    'ass.pg_mandato=mandato.pg_mandato and '||
	    'ass.cd_cds_mandato=mandato.cd_cds and '||
	    'ass.esercizio_mandato=mandato.esercizio and '||
	    'mandato.ti_mandato=''S'')');
   end if;


    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'REVERSALE.ESERCIZIO = '||P_Anno);
    End If;

    If P_UO is not null Then
       Costruisci_Stringa(Stringa, 'REVERSALE.CD_UNITA_ORGANIZZATIVA = '''||P_UO||'''');
    End If;

    If P_TIPO is not null Then
       Costruisci_Stringa(Stringa, 'REVERSALE.TI_REVERSALE = '''||P_TIPO||'''');
    End If;

    If P_PRO_DEF is not null Then
      IF P_PRO_DEF = 'P' THEN
       Costruisci_Stringa(Stringa,
        'REVERSALE.CD_TIPO_DOCUMENTO_CONT = ''REV_PROVV'' ');
      ELSIF P_PRO_DEF = 'D' THEN
       Costruisci_Stringa(Stringa,
        'REVERSALE.CD_TIPO_DOCUMENTO_CONT = ''REV'' ');
      END IF;
    End If;

-- data emissiome reversale

    If P_DA_Data_ris is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(REVERSALE.DT_EMISSIONE, ''yyyymmdd'') >= '''||
                              to_char(P_DA_DATA_ris, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_RIS is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(REVERSALE.DT_EMISSIONE, ''yyyymmdd'') <= '''||
                              to_char(P_A_DATA_RIS, 'yyyymmdd')||'''');
    End If;

-- data emissione distinta

    If P_DA_Data_Eme_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_EMISSIONE, ''yyyymmdd'') >= '''||
                              to_char(P_DA_DATA_eme_dis, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_Eme_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_EMISSIONE, ''yyyymmdd'') <= '''||
                              to_char(P_A_DATA_eme_dis, 'yyyymmdd')||'''');
    End If;

-- data invio distinta

    If P_DA_Data_invio_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_INVIO, ''yyyymmdd'') >= '''||
                              to_char(P_DA_DATA_invio_dis, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_invio_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_INVIO, ''yyyymmdd'') <= '''||
                              to_char(P_A_DATA_invio_dis, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return(Calcola_valore(Stringa));
  End Tot_reversali;

  FUNCTION TOT_REVERSALI_RISCONTRATE(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2 )
  RETURN NUMBER Is
  Begin
    Return Tot_reversali_RISCONTRATE(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, P_DA_Data, P_A_Data, P_Stringa,null);
  End;

  FUNCTION TOT_REVERSALI_RISCONTRATE(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, I, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2,
  P_coll_mand in varchar2 )
  RETURN NUMBER Is
  Begin
    Return Tot_reversali_RISCONTRATE(P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, P_DA_Data, P_A_Data, P_Stringa,P_coll_mand);
  End;

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
  RETURN NUMBER Is
  Begin
    Return Tot_reversali_RISCONTRATE(P_Anno, P_CDS, P_UO, P_Tipo, P_DA_Data_Eme_dis, P_A_Data_Eme_dis, P_DA_Data_riscontro, P_A_Data_riscontro, P_Stringa,null);
  End;

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
  P_coll_mand in Varchar2)
  RETURN NUMBER IS
  Stringa      varchar2(2500) := null;
	parametri_esercizio  parametri_cnr%rowtype;
  CDS_ENTE        VARCHAR2(30);
 begin
 		parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
		CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);
    Stringa := 'Select nvl(sum(SOSPESO_DET_ETR.IM_ASSOCIATO), 0) '||
               'from SOSPESO, SOSPESO_DET_ETR, REVERSALE '||
               'WHERE SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO = ''R'' AND '||
               '      SOSPESO_DET_ETR.STATO         = ''N'' AND '||
               '      SOSPESO_DET_ETR.CD_CDS       = REVERSALE.CD_CDS AND '||
               '      SOSPESO_DET_ETR.ESERCIZIO     = REVERSALE.ESERCIZIO AND '||
               '      SOSPESO_DET_ETR.PG_REVERSALE  = REVERSALE.PG_REVERSALE AND '||
               '      SOSPESO.CD_CDS                = SOSPESO_DET_ETR.CD_CDS  AND '||
               '      SOSPESO.ESERCIZIO             = SOSPESO_DET_ETR.ESERCIZIO AND '||
               '      SOSPESO.TI_ENTRATA_SPESA      = SOSPESO_DET_ETR.TI_ENTRATA_SPESA AND '||
               '      SOSPESO.TI_SOSPESO_RISCONTRO  = SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO AND '||
               '      SOSPESO.CD_SOSPESO            = SOSPESO_DET_ETR.CD_SOSPESO ';

	if (parametri_esercizio.fl_tesoreria_unica='N') then
    		If P_CDS is not null Then
      	 	     Costruisci_Stringa(Stringa, 'SOSPESO_DET_ETR.CD_CDS = '''||P_CDS||'''');
    		End If;
		else
  		if (P_CDS!=CDS_ENTE) then
  			     Costruisci_Stringa(Stringa, 'REVERSALE.CD_CDS_origine = '''||P_CDS||'''');
  		end if;
  	end if;
  if(P_coll_mand is not null and P_coll_mand='S' ) then
	   Costruisci_Stringa(Stringa, 'exists(select 1 from ass_mandato_reversale ass,mandato '||
	    'where '||
	    'ass.pg_reversale=reversale.pg_reversale and '||
	    'ass.cd_cds_reversale=reversale.cd_cds and '||
	    'ass.esercizio_reversale=reversale.esercizio and '||
	    'ass.pg_mandato=mandato.pg_mandato and '||
	    'ass.cd_cds_mandato=mandato.cd_cds and '||
	    'ass.esercizio_mandato=mandato.esercizio and '||
	    'mandato.ti_mandato=''S'')');
   end if;
    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'SOSPESO_DET_ETR.ESERCIZIO = '||P_Anno);
    End If;

    If P_UO is not null Then
       Costruisci_Stringa(Stringa, 'REVERSALE.CD_UNITA_ORGANIZZATIVA = '''||P_UO||'''');
    End If;

    If P_TIPO is not null Then
       Costruisci_Stringa(Stringa, 'REVERSALE.TI_REVERSALE = '''||P_TIPO||'''');
    End If;

-- PERIODO DI EMISSIONE DISTINTA

    if (P_DA_Data_Eme_dis is not null or P_A_Data_Eme_dis is not null) THEN
      Costruisci_Stringa(Stringa, ' exists (select 1 from DISTINTA_CASSIERE, DISTINTA_CASSIERE_DET '||
                                           'where REVERSALE.CD_CDS = DISTINTA_CASSIERE_DET.CD_CDS_ORIGINE AND '||
                                                 'REVERSALE.ESERCIZIO = DISTINTA_CASSIERE_DET.ESERCIZIO  AND '||
                                                 'REVERSALE.PG_REVERSALE = DISTINTA_CASSIERE_DET.PG_REVERSALE AND '||
                                                 'DISTINTA_CASSIERE.CD_CDS = DISTINTA_CASSIERE_DET.CD_CDS AND '||
                                                 'DISTINTA_CASSIERE.ESERCIZIO   = DISTINTA_CASSIERE_DET.ESERCIZIO AND '||
                                                 'DISTINTA_CASSIERE.CD_UNITA_ORGANIZZATIVA = DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA AND '||
                                                 'DISTINTA_CASSIERE.PG_DISTINTA = DISTINTA_CASSIERE_DET.PG_DISTINTA ');
      If P_DA_Data_Eme_dis is not null Then
        Costruisci_Stringa(Stringa, 'to_char(DISTINTA_CASSIERE.DT_EMISSIONE, ''yyyymmdd'') >= '''||to_char(P_DA_DATA_eme_dis, 'yyyymmdd')||'''');
      End If;

      If P_A_Data_Eme_dis is not null Then
        Costruisci_Stringa(Stringa, 'to_char(DISTINTA_CASSIERE.DT_EMISSIONE, ''yyyymmdd'') <= '''||to_char(P_A_DATA_eme_dis, 'yyyymmdd')||'''');
      End If;
      Stringa := Stringa || ') ';
    End If;

-- PERIODO DI RISCONTRI

    If P_DA_Data_riscontro Is Not Null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') >= '''||
                           to_char(P_DA_Data_riscontro, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_riscontro is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') <= '''||
                           to_char(P_A_Data_riscontro, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return(Calcola_valore(Stringa));
  End TOT_REVERSALI_RISCONTRATE;

  FUNCTION TOT_MANDATI_RISCONTRATI(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2 )
  RETURN NUMBER Is
  Begin
   Return TOT_MANDATI_RISCONTRATI (P_Anno, P_CDS, P_UO, P_Tipo, Null, Null, P_DA_Data, P_A_Data, P_Stringa);
  End;

  FUNCTION TOT_MANDATI_RISCONTRATI(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_UO  IN varchar2 ,
  P_Tipo IN varchar2 ,   -- (A, P, R, S)
  P_DA_Data_EME_DIS In date ,
  P_A_Data_EME_DIS In date ,
  P_Da_Data_RISCONTRO IN date ,
  P_a_Data_RISCONTRO IN date ,
  P_Stringa IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
 		parametri_esercizio  parametri_cnr%rowtype;
   	CDS_ENTE        VARCHAR2(30);
 begin
 		parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
		CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);
    Stringa := 'Select nvl(sum(SOSPESO_DET_USC.IM_ASSOCIATO), 0) '||
               'from SOSPESO, SOSPESO_DET_USC, MANDATO, DISTINTA_CASSIERE, DISTINTA_CASSIERE_DET '||
               'WHERE SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO = ''R'' AND '||
               'SOSPESO_DET_USC.STATO       	 = ''N'' AND '||
               'SOSPESO_DET_USC.CD_CDS	     = MANDATO.CD_CDS AND '||
               'SOSPESO_DET_USC.ESERCIZIO    = MANDATO.ESERCIZIO AND '||
               'SOSPESO_DET_USC.PG_MANDATO   = MANDATO.PG_MANDATO AND '||
               'SOSPESO.CD_CDS               = SOSPESO_DET_USC.CD_CDS AND '||
               'SOSPESO.ESERCIZIO            = SOSPESO_DET_USC.ESERCIZIO AND '||
               'SOSPESO.TI_ENTRATA_SPESA     = SOSPESO_DET_USC.TI_ENTRATA_SPESA AND '||
               'SOSPESO.TI_SOSPESO_RISCONTRO = SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO AND '||
               'SOSPESO.CD_SOSPESO           = SOSPESO_DET_USC.CD_SOSPESO AND '||
               'MANDATO.CD_CDS               = DISTINTA_CASSIERE_DET.CD_CDS_ORIGINE     AND '||
               'MANDATO.ESERCIZIO            = DISTINTA_CASSIERE_DET.ESERCIZIO  AND '||
               'MANDATO.PG_MANDATO           = DISTINTA_CASSIERE_DET.PG_MANDATO AND '||
               'DISTINTA_CASSIERE.CD_CDS      = DISTINTA_CASSIERE_DET.CD_CDS       AND '||
               'DISTINTA_CASSIERE.ESERCIZIO   = DISTINTA_CASSIERE_DET.ESERCIZIO    AND '||
               'DISTINTA_CASSIERE.CD_UNITA_ORGANIZZATIVA = DISTINTA_CASSIERE_DET.CD_UNITA_ORGANIZZATIVA AND '||
               'DISTINTA_CASSIERE.PG_DISTINTA = DISTINTA_CASSIERE_DET.PG_DISTINTA AND '||
               'DISTINTA_CASSIERE.PG_DISTINTA IN(SELECT MIN(DET.PG_DISTINTA) FROM DISTINTA_CASSIERE_DET DET '||
               'WHERE '||
               'MANDATO.CD_CDS               = DET.CD_CDS_ORIGINE AND '||
               'MANDATO.ESERCIZIO            = DET.ESERCIZIO  AND '||
               'MANDATO.PG_MANDATO           = DET.PG_MANDATO)';
	if (parametri_esercizio.fl_tesoreria_unica='N') then
    If P_CDS IS NOT NULL Then
          Costruisci_Stringa(Stringa, 'SOSPESO_DET_USC.CD_CDS = '''||P_CDS||'''');
    End If;
	else
    if(P_CDS!=CDS_ENTE) then
  		Costruisci_Stringa(Stringa, 'MANDATO.CD_CDS_origine = '''||P_CDS||'''');
    end if;
  end if;
    If P_UO is not null Then
       Costruisci_Stringa(Stringa, 'MANDATO.CD_UNITA_ORGANIZZATIVA = '''||P_UO||'''');
    End If;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'SOSPESO_DET_USC.ESERCIZIO = '||P_Anno);
    End If;

    If P_TIPO is not null Then
       Costruisci_Stringa(Stringa, 'MANDATO.TI_MANDATO = '''||P_TIPO||'''');
    End If;

-- PERIODO DI EMISSIONE DISTINTA

    If P_DA_Data_Eme_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_EMISSIONE, ''yyyymmdd'') >= '''||
                              to_char(P_DA_DATA_eme_dis, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_Eme_dis is not null Then
          Costruisci_Stringa(Stringa,
                             'to_char(DISTINTA_CASSIERE.DT_EMISSIONE, ''yyyymmdd'') <= '''||
                              to_char(P_A_DATA_eme_dis, 'yyyymmdd')||'''');
    End If;

-- PERIODO DI RISCONTRI

    If P_DA_Data_riscontro Is Not Null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') >= '''||
                           to_char(P_DA_Data_riscontro, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_riscontro is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') <= '''||
                           to_char(P_A_Data_riscontro, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return(Calcola_valore(Stringa));
  End TOT_MANDATI_RISCONTRATI;

  FUNCTION Tot_sospesi_e_s(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_TIPO IN varchar2 ,
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_FL_STORNATI In VARCHAR2, -- Y/N
  P_Stringa IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
    CDS_ENTE        VARCHAR2(30);
 		parametri_esercizio  parametri_cnr%rowtype;
  Begin
   parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
   CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);
	if (parametri_esercizio.fl_tesoreria_unica='N') then
    Stringa :=   'Select nvl(sum(SOSPESO.IM_SOSPESO), 0) from SOSPESO '||
                 'WHERE SOSPESO.TI_SOSPESO_RISCONTRO = ''S'' AND '||
                       'SOSPESO.CD_SOSPESO_PADRE IS NULL ';
   ELSE
    Stringa :=   'Select nvl(sum(SOSPESO.IM_SOSPESO), 0) from SOSPESO '||
                 'WHERE SOSPESO.TI_SOSPESO_RISCONTRO = ''S'' AND '||
                       'SOSPESO.CD_SOSPESO_PADRE IS NOT NULL ';
   END IF;

    If Nvl(P_FL_STORNATI, 'N') = 'N' Then
          Costruisci_Stringa(Stringa, 'SOSPESO.FL_STORNATO = ''N''');
    Elsif Nvl(P_FL_STORNATI, 'N') = 'Y' Then
          Costruisci_Stringa(Stringa, 'SOSPESO.FL_STORNATO = ''Y''');
    End If;

	if (parametri_esercizio.fl_tesoreria_unica='N') then
    If P_CDS IS NOT NULL Then
          Costruisci_Stringa(Stringa, 'SOSPESO.CD_CDS = '''||P_CDS||'''');
    End If;
  else
    if(P_CDS=CDS_ENTE) then
   			Costruisci_Stringa(Stringa, 'SOSPESO.CD_CDS = '''||cds_Ente||'''');
   			--Costruisci_Stringa(Stringa, 'SOSPESO.CD_CDS_ORIGINE is null ');
   	else
   			Costruisci_Stringa(Stringa, 'SOSPESO.CD_CDS = '''||cds_Ente||'''');
   			Costruisci_Stringa(Stringa, 'SOSPESO.CD_CDS_ORIGINE =  '''||P_CDS||'''');
   	end if;
  end if;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'SOSPESO.ESERCIZIO = '||P_Anno);
    End If;

    If P_Tipo IS NOT NULL Then
          Costruisci_Stringa(Stringa, 'SOSPESO.TI_ENTRATA_SPESA = '''||P_TIPO||'''');
    End If;

    If P_DA_DATA is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') >= '''||
                           to_char(P_DA_DATA, 'yyyymmdd')||'''');
    End If;

    If P_A_DATA is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') <= '''||
                           to_char(P_A_DATA, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;
    --insert into stringhe values('Sospesi '||P_CDS||' '||stringa);
    Return(Calcola_valore(Stringa));
  End Tot_sospesi_e_s;

  FUNCTION TOT_LETTERE_PAGAMENTO_ASS(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2 )
  RETURN NUMBER IS
  Stringa      varchar2(2000) := null;
  parametri_esercizio  parametri_cnr%rowtype;
  CDS_ENTE        VARCHAR2(30);
 begin
 		parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
		CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);
    Stringa :=   'Select nvl(sum(SOSPESO.IM_SOSPESO), 0) '||
                 'from SOSPESO, LETTERA_PAGAM_ESTERO '||
                 'WHERE SOSPESO.TI_SOSPESO_RISCONTRO = ''S'' AND '||
                       'SOSPESO.FL_STORNATO = ''N'' AND '||
                       'SOSPESO.CD_CDS                = LETTERA_PAGAM_ESTERO.CD_CDS_SOSPESO              AND '||
                       'SOSPESO.ESERCIZIO             = LETTERA_PAGAM_ESTERO.ESERCIZIO            AND '||
                       'SOSPESO.TI_ENTRATA_SPESA      = LETTERA_PAGAM_ESTERO.TI_ENTRATA_SPESA     AND '||
                       'SOSPESO.TI_SOSPESO_RISCONTRO  = LETTERA_PAGAM_ESTERO.TI_SOSPESO_RISCONTRO AND '||
                       'SOSPESO.CD_SOSPESO            = LETTERA_PAGAM_ESTERO.CD_SOSPESO ';
		if (parametri_esercizio.fl_tesoreria_unica='N') then
    		If P_CDS is not null Then
      	 	   Costruisci_Stringa(Stringa, 'SOSPESO.CD_CDS = '''||P_CDS||'''');
    		End If;
		else
  		if (P_CDS!=CDS_ENTE) then
  			     Costruisci_Stringa(Stringa, 'SOSPESO.CD_CDS = '''||P_CDS||'''');
  		end if;
  	end if;

		If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'SOSPESO.ESERCIZIO = '||P_Anno);
    End If;

    If P_DA_DATA is not null Then null;
       Costruisci_Stringa(Stringa,
                          ' to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') >= '''||
                            to_char(P_DA_DATA, 'yyyymmdd')||'''');
    End If;

    If P_A_DATA is not null Then null;
       Costruisci_Stringa(Stringa,
                          ' to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') <= '''||
                            to_char(P_A_DATA, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;
 	--insert into stringhe values('lett ass  '||stringa);
    Return(Calcola_valore(Stringa));
  End TOT_LETTERE_PAGAMENTO_ASS;

FUNCTION TOT_LETTERE_PAGAMENTO_NON_ASS(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data IN date ,
  P_A_Data IN date ,
  P_Stringa IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
    DOC_GEN      NUMBER;
    FATT_PASS    NUMBER;
		parametri_esercizio  parametri_cnr%rowtype;
   	CDS_ENTE        VARCHAR2(30);
 begin
 		parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
		CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);
    Stringa :=   'Select nvl(sum(DOCUMENTO_GENERICO.IM_TOTALE), 0) '||
                 'from DOCUMENTO_GENERICO, LETTERA_PAGAM_ESTERO '||
                 'WHERE DOCUMENTO_GENERICO.DT_CANCELLAZIONE IS NULL AND '||
                   'DOCUMENTO_GENERICO.CD_CDS                = LETTERA_PAGAM_ESTERO.CD_CDS  AND '||
                   'DOCUMENTO_GENERICO.CD_UNITA_ORGANIZZATIVA = LETTERA_PAGAM_ESTERO.CD_UNITA_ORGANIZZATIVA AND '||
                   'DOCUMENTO_GENERICO.ESERCIZIO_LETTERA     = LETTERA_PAGAM_ESTERO.ESERCIZIO AND '||
                   'DOCUMENTO_GENERICO.PG_LETTERA            = LETTERA_PAGAM_ESTERO.PG_LETTERA AND '||
                   'LETTERA_PAGAM_ESTERO.CD_SOSPESO IS NULL ';

		if (parametri_esercizio.fl_tesoreria_unica='N') then
    		If P_CDS is not null Then
      	 	  Costruisci_Stringa(Stringa, 'DOCUMENTO_GENERICO.CD_CDS = '''||P_CDS||'''');
    		End If;
		else
  		if (P_CDS!=CDS_ENTE) then
  			   Costruisci_Stringa(Stringa, 'DOCUMENTO_GENERICO.CD_CDS = '''||P_CDS||'''');
  		end if;
  	end if;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'LETTERA_PAGAM_ESTERO.ESERCIZIO = '||P_Anno);
    End If;

    If P_DA_DATA is not null Then null;
       Costruisci_Stringa(Stringa,
                          ' to_char(LETTERA_PAGAM_ESTERO.DT_REGISTRAZIONE, ''yyyymmdd'') >= '''||
                            to_char(P_DA_DATA, 'yyyymmdd')||'''');
    End If;

    If P_A_DATA is not null Then null;
       Costruisci_Stringa(Stringa,
                          ' to_char(LETTERA_PAGAM_ESTERO.DT_REGISTRAZIONE, ''yyyymmdd'') <= '''||
                            to_char(P_A_DATA, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;
 --insert into stringhe values('let doc '||stringa);
    DOC_GEN := Calcola_valore(Stringa);

    Stringa :=   'Select nvl(sum(FATTURA_PASSIVA.IM_TOTALE_imponibile)+sum(FATTURA_PASSIVA.IM_TOTALE_iva), 0) '||
                 'from FATTURA_PASSIVA, LETTERA_PAGAM_ESTERO '||
                 'WHERE FATTURA_PASSIVA.DT_CANCELLAZIONE IS NULL AND '||
                       'FATTURA_PASSIVA.CD_CDS               = LETTERA_PAGAM_ESTERO.CD_CDS     AND '||
                       'FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA = LETTERA_PAGAM_ESTERO.CD_UNITA_ORGANIZZATIVA AND '||
                       'FATTURA_PASSIVA.ESERCIZIO_LETTERA     = LETTERA_PAGAM_ESTERO.ESERCIZIO  AND '||
                       'FATTURA_PASSIVA.PG_LETTERA           = LETTERA_PAGAM_ESTERO.PG_LETTERA AND '||
                       'LETTERA_PAGAM_ESTERO.CD_SOSPESO IS NULL ';

		if (parametri_esercizio.fl_tesoreria_unica='N') then
    		If P_CDS is not null Then
      	 	   Costruisci_Stringa(Stringa, 'FATTURA_PASSIVA.CD_CDS = '''||P_CDS||'''');
    		End If;
		else
  		if (P_CDS!=CDS_ENTE) then
  			     Costruisci_Stringa(Stringa, 'FATTURA_PASSIVA.CD_CDS = '''||P_CDS||'''');
  		end if;
  	end if;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'LETTERA_PAGAM_ESTERO.ESERCIZIO = '||P_Anno);
    End If;

    If P_DA_DATA is not null Then null;
       Costruisci_Stringa(Stringa,
                          ' to_char(LETTERA_PAGAM_ESTERO.DT_REGISTRAZIONE, ''yyyymmdd'') >= '''||
                            to_char(P_DA_DATA, 'yyyymmdd')||'''');
    End If;

    If P_A_DATA is not null Then null;
       Costruisci_Stringa(Stringa,
                          ' to_char(LETTERA_PAGAM_ESTERO.DT_REGISTRAZIONE, ''yyyymmdd'') <= '''||
                            to_char(P_A_DATA, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;
 		--insert into stringhe values('let fat '||stringa);
    FATT_PASS := Calcola_valore(Stringa);

    RETURN (DOC_GEN + FATT_PASS);

  End TOT_LETTERE_PAGAMENTO_NON_ASS;

  FUNCTION Tot_sospesi_riscossi(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data IN DATE,
  P_A_Data IN DATE,
  P_Stringa IN varchar2 )
  RETURN NUMBER Is
  Begin
   Return Tot_sospesi_riscossi(P_Anno, P_CDS, P_DA_Data, P_A_Data, Null, Null, Null, Null, P_Stringa);
  End;

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
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
		parametri_esercizio  parametri_cnr%rowtype;
   	CDS_ENTE        VARCHAR2(30);
 begin
 		parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
		CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);
    Stringa :=   'Select nvl(sum(SOSPESO_DET_ETR.IM_ASSOCIATO), 0) '||
                 'from SOSPESO_DET_ETR, SOSPESO, REVERSALE '||
                 'WHERE SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO = ''S'' AND '||
                       'SOSPESO_DET_ETR.STATO                = ''N'' AND '||
                       'SOSPESO_DET_ETR.CD_CDS_reversale               = REVERSALE.CD_CDS AND '||
                       'SOSPESO_DET_ETR.ESERCIZIO            = REVERSALE.ESERCIZIO AND '||
                       'SOSPESO_DET_ETR.PG_REVERSALE         = REVERSALE.PG_REVERSALE AND '||
                       'SOSPESO.CD_CDS                       = SOSPESO_DET_ETR.CD_CDS AND '||
                       'SOSPESO.ESERCIZIO                    = SOSPESO_DET_ETR.ESERCIZIO AND '||
                       'SOSPESO.TI_ENTRATA_SPESA             = SOSPESO_DET_ETR.TI_ENTRATA_SPESA AND '||
                       'SOSPESO.TI_SOSPESO_RISCONTRO         = SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO AND '||
                       'SOSPESO.CD_SOSPESO                   = SOSPESO_DET_ETR.CD_SOSPESO ';

		if (parametri_esercizio.fl_tesoreria_unica='N') then
    		If P_CDS is not null Then
 	 	         Costruisci_Stringa(Stringa, 'SOSPESO_DET_ETR.CD_CDS_reversale = '''||P_CDS||'''');
    		End If;
		else
  		if (P_CDS!=CDS_ENTE) then
  					Costruisci_Stringa(Stringa, 'SOSPESO_DET_ETR.CD_CDS = '''||CDS_ENTE||'''');
  		      Costruisci_Stringa(Stringa, 'REVERSALE.CD_CDS_ORIGINE = '''||P_CDS||'''');
  		else
  		     Costruisci_Stringa(Stringa, 'SOSPESO_DET_ETR.CD_CDS = '''||CDS_ENTE||'''');
  		end if;
  	end if;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'SOSPESO_DET_ETR.ESERCIZIO = '||P_Anno);
    End If;

    If P_DA_DATA_SOS is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') >= '''||
                           to_char(P_DA_DATA_SOS, 'yyyymmdd')||'''');
    End If;

    If P_A_DATA_SOS is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') <= '''||
                           to_char(P_A_DATA_SOS, 'yyyymmdd')||'''');
    End If;

    If P_DA_DATA_RIS is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(REVERSALE.DT_EMISSIONE, ''yyyymmdd'') >= '''||
                           to_char(P_DA_DATA_RIS, 'yyyymmdd')||'''');
    End If;

    If P_A_DATA_RIS is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(REVERSALE.DT_EMISSIONE, ''yyyymmdd'') <= '''||
                           to_char(P_A_DATA_RIS, 'yyyymmdd')||'''');
    End If;

    If P_DA_Data_Invio is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(REVERSALE.DT_trasmissione, ''yyyymmdd'') >= '''||
                           to_char(P_DA_Data_Invio, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_Invio is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(REVERSALE.DT_trasmissione, ''yyyymmdd'') <= '''||
                           to_char(P_A_Data_Invio, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;
		--insert into stringhe values('Sospesi Ris '||P_CDS||' '||stringa);
    Return(Calcola_valore(Stringa));

  End Tot_sospesi_riscossi;

  FUNCTION Tot_sospesi_pagati(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data IN DATE,
  P_A_Data IN DATE,
  P_Stringa IN varchar2 )
  RETURN NUMBER IS
  Begin
   Return Tot_sospesi_pagati (P_Anno, P_CDS, P_DA_Data, P_A_Data, Null, Null, Null, Null, P_Stringa);
  End;

  FUNCTION Tot_sospesi_pagati(
  P_Anno IN number ,
  P_CDS IN varchar2 ,
  P_DA_Data_Sos IN DATE,
  P_A_Data_Sos IN DATE,
  P_DA_Data_Man IN DATE,
  P_A_Data_Man IN DATE,
  P_DA_Data_Invio IN DATE,
  P_A_Data_Invio IN DATE,
  P_Stringa IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
		parametri_esercizio  parametri_cnr%rowtype;
   	CDS_ENTE        VARCHAR2(30);
 begin
 		parametri_esercizio:=CNRUTL001.getRecParametriCnr(P_Anno);
		CDS_ENTE:=CNRCTB020.GETCDCDSENTE (P_Anno);
		Stringa :=   'Select nvl(sum(SOSPESO_DET_USC.IM_ASSOCIATO), 0) '||
                 'from SOSPESO_DET_USC, SOSPESO, MANDATO '||
                 'WHERE SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO = ''S'' AND '||
                 'SOSPESO_DET_USC.STATO        = ''N'' AND '||
                 'SOSPESO_DET_USC.CD_CDS_mandato       = MANDATO.CD_CDS AND '||
                 'SOSPESO_DET_USC.ESERCIZIO    = MANDATO.ESERCIZIO AND '||
                 'SOSPESO_DET_USC.PG_MANDATO   = MANDATO.PG_MANDATO AND '||
                 'SOSPESO.CD_CDS               = SOSPESO_DET_USC.CD_CDS AND '||
                 'SOSPESO.ESERCIZIO            = SOSPESO_DET_USC.ESERCIZIO AND '||
                 'SOSPESO.TI_ENTRATA_SPESA     = SOSPESO_DET_USC.TI_ENTRATA_SPESA AND '||
                 'SOSPESO.TI_SOSPESO_RISCONTRO = SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO AND '||
                 'SOSPESO.CD_SOSPESO           = SOSPESO_DET_USC.CD_SOSPESO ';

		if (parametri_esercizio.fl_tesoreria_unica='N') then
    		If P_CDS is not null Then
      			Costruisci_Stringa(Stringa, 'SOSPESO_DET_USC.CD_CDS_mandato = '''||P_CDS||'''');
    		End If;
		else
  		if (P_CDS!=CDS_ENTE) then
  					Costruisci_Stringa(Stringa, 'SOSPESO_DET_USC.CD_CDS = '''||CDS_ENTE||'''');
      			--Costruisci_Stringa(Stringa, 'SOSPESO_DET_USC.CD_CDS_mandato = '''||P_CDS||'''');
      			Costruisci_Stringa(Stringa, 'MANDATO.CD_CDS_ORIGINE = '''||P_CDS||'''');
      else
  		      Costruisci_Stringa(Stringa, 'SOSPESO_DET_USC.CD_CDS = '''||CDS_ENTE||'''');
  		end if;
  	end if;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'SOSPESO_DET_USC.ESERCIZIO = '||P_Anno);
    End If;

    If P_DA_DATA_SOS is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') >= '''||
                           to_char(P_DA_DATA_SOS, 'yyyymmdd')||'''');
    End If;

    If P_A_DATA_SOS is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(SOSPESO.DT_REGISTRAZIONE, ''yyyymmdd'') <= '''||
                           to_char(P_A_DATA_SOS, 'yyyymmdd')||'''');
    End If;

    If P_DA_DATA_MAN is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(MANDATO.DT_EMISSIONE, ''yyyymmdd'') >= '''||
                           to_char(P_DA_DATA_MAN, 'yyyymmdd')||'''');
    End If;

    If P_A_DATA_MAN is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(MANDATO.DT_EMISSIONE, ''yyyymmdd'') <= '''||
                           to_char(P_A_DATA_MAN, 'yyyymmdd')||'''');
    End If;

    If P_DA_Data_Invio is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(MANDATO.DT_trasmissione, ''yyyymmdd'') >= '''||
                           to_char(P_DA_Data_Invio, 'yyyymmdd')||'''');
    End If;

    If P_A_Data_Invio is not null Then
       Costruisci_Stringa(Stringa,
                          'to_char(MANDATO.DT_TRASMISSIONE, ''yyyymmdd'') <= '''||
                           to_char(P_A_Data_Invio, 'yyyymmdd')||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;
	--insert into stringhe values('Sospesi PAG'||P_CDS||' '||stringa);
    Return(Calcola_valore(Stringa));
  End Tot_sospesi_pagati;

END PRT_CIR_SITCAS;
