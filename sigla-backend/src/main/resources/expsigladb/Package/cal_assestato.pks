CREATE OR REPLACE PACKAGE CAL_ASSESTATO IS
FUNCTION ASSESTATO_ENTRATA(
  P_Anno        IN number ,
  P_dip         IN varchar2 ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_PG_VAR      IN varchar2 ,
  P_LIV1        IN varchar2 ,
  P_LIV2        IN varchar2 ,
  P_LIV3        IN varchar2 ,
  P_LIV4        IN varchar2 ,
  P_LIV5        IN varchar2 ,
  P_LIV6        IN varchar2 ,
  P_LIV7        IN varchar2 ,
  P_Stringa     IN varchar2 )
RETURN NUMBER;

FUNCTION ASSESTATO_SPESA(
  P_Anno        IN number ,
  P_dip         IN varchar2 ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_PG_VAR      IN varchar2 ,
  P_LIV1        IN varchar2 ,
  P_LIV2        IN varchar2 ,
  P_LIV3        IN varchar2 ,
  P_LIV4        IN varchar2 ,
  P_LIV5        IN varchar2 ,
  P_LIV6        IN varchar2 ,
  P_LIV7        IN varchar2 ,
  P_Stringa     IN varchar2 )
RETURN NUMBER;

FUNCTION ASSESTATO_SPESA_RES(
  P_Anno        IN number ,
  P_Anno_RES    IN number ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_Stringa     IN varchar2 )
RETURN NUMBER;


END CAL_ASSESTATO;
/


CREATE OR REPLACE PACKAGE BODY CAL_ASSESTATO IS

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
    Dbms_Sql.PARSE(CURSOR_NAME, P_Stringa, DBMS_SQL.V7);
    Dbms_Sql.DEFINE_COLUMN(cursor_name,1,valore);
    appo := DBMS_SQL.execute_and_fetch(cursor_name);
    Dbms_Sql.column_value(cursor_name,1,valore);
    Dbms_Sql.CLOSE_CURSOR(CURSOR_NAME);

    Return(valore);
  End Calcola_valore;

  FUNCTION ASSESTATO_ENTRATA(
  P_Anno        IN number ,
  P_dip         IN varchar2 ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_PG_VAR      IN varchar2 ,
  P_LIV1        IN varchar2 ,
  P_LIV2        IN varchar2 ,
  P_LIV3        IN varchar2 ,
  P_LIV4        IN varchar2 ,
  P_LIV5        IN varchar2 ,
  P_LIV6        IN varchar2 ,
  P_LIV7        IN varchar2 ,
  P_Stringa     IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(4000) := null;

  Begin

    Stringa := 'Select NVL(SUM(nvl(V_CONS_PDG_ETR_ASSESTATO.INI, 0) + '||
               ' nvl(V_CONS_PDG_ETR_ASSESTATO.VAR_PIU, 0) + '||
               ' nvl(V_CONS_PDG_ETR_ASSESTATO.VAR_MENO, 0)), 0) '||
               ' from  V_CONS_PDG_ETR_ASSESTATO ';

    IF P_ANNO       IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.ESERCIZIO = '''||P_ANNO||'''');
    END IF;

    IF P_dip       IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.DIP = '''||P_DIP||'''');
    END IF;

    IF P_CDS       IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CDS = '''||P_CDS||'''');
    END IF;

    IF P_UO        IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.UO = '''||P_UO||'''');
    END IF;

    IF P_CDR       IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CDR = '''||P_CDR||'''');
    END IF;

    IF P_ELE_VOCE  IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.cd_elemento_voce = '''||P_ELE_VOCE||'''');
    END IF;

    IF P_LINEA  IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CD_LINEA_ATTIVITA = '''||P_LINEA||'''');
    END IF;

    IF P_PG_VAR  IS NOT NULL Then
       Costruisci_Stringa(Stringa, 'NVL(V_CONS_PDG_ETR_ASSESTATO.PG_VARIAZIONE_PDG, 0) NOT IN ('||P_PG_VAR||')');
    END IF;

    IF P_LIV1      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CD_LIVELLO1 = '''||P_LIV1||'''');
    END IF;

    IF P_LIV2      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CD_LIVELLO2 = '''||P_LIV2||'''');
    END IF;

    IF P_LIV3      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CD_LIVELLO3 = '''||P_LIV3||'''');
    END IF;

    IF P_LIV4      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CD_LIVELLO4 = '''||P_LIV4||'''');
    END IF;

    IF P_LIV5      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CD_LIVELLO5 = '''||P_LIV5||'''');
    END IF;

    IF P_LIV6      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CD_LIVELLO6 = '''||P_LIV6||'''');
    END IF;

    IF P_LIV7      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_ETR_ASSESTATO.CD_LIVELLO7 = '''||P_LIV7||'''');
    END IF;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return Calcola_valore(Stringa);
  End ASSESTATO_ENTRATA;

Function ASSESTATO_SPESA(
  P_Anno        IN number ,
  P_dip         IN varchar2 ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_PG_VAR      IN varchar2 ,
  P_LIV1        IN varchar2 ,
  P_LIV2        IN varchar2 ,
  P_LIV3        IN varchar2 ,
  P_LIV4        IN varchar2 ,
  P_LIV5        IN varchar2 ,
  P_LIV6        IN varchar2 ,
  P_LIV7        IN varchar2 ,
  P_Stringa     IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(4000) := null;

  Begin

    Stringa := 'Select NVL(SUM(nvl(V_CONS_PDG_SPE_ASSESTATO.INI, 0) + '||
               ' nvl(V_CONS_PDG_SPE_ASSESTATO.VAR_PIU, 0) + '||
               ' nvl(V_CONS_PDG_SPE_ASSESTATO.VAR_MENO, 0)), 0) '||
               ' from  V_CONS_PDG_SPE_ASSESTATO ';

    IF P_ANNO       IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.ESERCIZIO = '''||P_ANNO||'''');
    END IF;

    IF P_dip       IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.DIP = '''||P_DIP||'''');
    END IF;

    IF P_CDS       IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CDS = '''||P_CDS||'''');
    END IF;

    IF P_UO        IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.UO = '''||P_UO||'''');
    END IF;

    IF P_CDR       IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CDR = '''||P_CDR||'''');
    END IF;

    IF P_ELE_VOCE  IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.cd_elemento_voce = '''||P_ELE_VOCE||'''');
    END IF;

    IF P_LINEA  IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CD_LINEA_ATTIVITA = '''||P_LINEA||'''');
    END IF;

    IF P_PG_VAR  IS NOT NULL Then
       Costruisci_Stringa(Stringa, 'NVL(V_CONS_PDG_SPE_ASSESTATO.PG_VARIAZIONE_PDG, 0) NOT IN ('||P_PG_VAR||')');
    END IF;

    IF P_LIV1      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CD_LIVELLO1 = '''||P_LIV1||'''');
    END IF;

    IF P_LIV2      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CD_LIVELLO2 = '''||P_LIV2||'''');
    END IF;

    IF P_LIV3      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CD_LIVELLO3 = '''||P_LIV3||'''');
    END IF;

    IF P_LIV4      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CD_LIVELLO4 = '''||P_LIV4||'''');
    END IF;

    IF P_LIV5      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CD_LIVELLO5 = '''||P_LIV5||'''');
    END IF;

    IF P_LIV6      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CD_LIVELLO6 = '''||P_LIV6||'''');
    END IF;

    IF P_LIV7      IS NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_CONS_PDG_SPE_ASSESTATO.CD_LIVELLO7 = '''||P_LIV7||'''');
    END IF;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return Calcola_valore(Stringa);
  End ASSESTATO_SPESA;

Function ASSESTATO_SPESA_RES(
  P_Anno        IN number ,
  P_Anno_RES    IN number ,
  P_CDS         IN varchar2 ,
  P_UO          IN varchar2 ,
  P_CDR         IN varchar2 ,
  P_ELE_VOCE    IN varchar2 ,
  P_LINEA       IN varchar2 ,
  P_Stringa     IN varchar2 )
  RETURN NUMBER IS
    Stringa      varchar2(4000) := null;

  Begin

    Stringa := ' Select Nvl(Sum(TOT_IM_STANZ_RES_IMPROPRIO + '||
                              ' TOT_VAR_PIU_STANZ_RES_IMP - '||
                              ' TOT_VAR_MENO_STANZ_RES_IMP - '||
                              ' TOT_VAR_PIU_OBBL_RES_PRO + '||
                              ' TOT_VAR_MENO_OBBL_RES_PRO), 0) '||
               ' From   V_SITUAZIONE_LINEE_COMP_RES ';

    IF P_ANNO Is NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_SITUAZIONE_LINEE_COMP_RES.ESERCIZIO = '''||P_ANNO||'''');
    END IF;

    IF P_ANNO_RES Is NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_SITUAZIONE_LINEE_COMP_RES.ESERCIZIO_RES = '''||P_ANNO_RES||'''');
    END IF;

    IF P_CDS Is NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_SITUAZIONE_LINEE_COMP_RES.CD_CDS = '''||P_CDS||'''');
    END IF;

/*
    IF P_UO Is NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_SITUAZIONE_LINEE_COMP_RES.UO = '''||P_UO||'''');
    END IF;
*/

    IF P_CDR Is NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_SITUAZIONE_LINEE_COMP_RES.CD_CENTRO_RESPONSABILITA = '''||P_CDR||'''');
    END IF;

    IF P_ELE_VOCE Is NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_SITUAZIONE_LINEE_COMP_RES.cd_elemento_voce = '''||P_ELE_VOCE||'''');
    END IF;

    IF P_LINEA Is NOT NULL THEN
       Costruisci_Stringa(Stringa, 'V_SITUAZIONE_LINEE_COMP_RES.CD_LINEA_ATTIVITA = '''||P_LINEA||'''');
    END IF;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return Calcola_valore(Stringa);

  End ASSESTATO_SPESA_RES;

End CAL_ASSESTATO;
/


