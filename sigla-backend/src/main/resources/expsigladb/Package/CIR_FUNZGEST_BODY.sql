--------------------------------------------------------
--  DDL for Package Body CIR_FUNZGEST
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CIR_FUNZGEST" IS

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

FUNCTION PDG_PREVISIONE_SPE(
  P_Anno IN number,
  P_CDS IN varchar2,
  P_UO  IN varchar2,
  P_ELE_VOCE IN varchar2,
  P_LINEA IN varchar2,
  P_TIPO  IN VARCHAR2,  -- Funzionamento, Investimento, Prestazioni, Borse di studio
  P_Stringa IN varchar2)
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
  Begin
    Stringa := 'Select nvl(sum(PDG_PREVENTIVO_SPE_DET.IM_RH_CCS_COSTI), 0)+nvl(sum(PDG_PREVENTIVO_SPE_DET.IM_RO_CSS_ALTRI_COSTI), 0) '||
               ' from PDG_PREVENTIVO_SPE_DET, CDR, UNITA_ORGANIZZATIVA, elemento_voce '||
               ' WHERE PDG_PREVENTIVO_SPE_DET.ORIGINE != ''PDV'' AND '||
               '   PDG_PREVENTIVO_SPE_DET.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA AND '||
               '   CDR.CD_UNITA_ORGANIZZATIVA = UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA AND '||
               '   PDG_PREVENTIVO_SPE_DET.ESERCIZIO        = ELEMENTO_VOCE.ESERCIZIO        AND '||
               '   PDG_PREVENTIVO_SPE_DET.TI_APPARTENENZA  = ELEMENTO_VOCE.TI_APPARTENENZA  AND '||
               '   PDG_PREVENTIVO_SPE_DET.TI_GESTIONE      = ELEMENTO_VOCE.TI_GESTIONE      AND '||
               '   PDG_PREVENTIVO_SPE_DET.CD_ELEMENTO_VOCE = ELEMENTO_VOCE.CD_ELEMENTO_VOCE';

    If P_CDS is not null Then
       Costruisci_Stringa(Stringa, 'UNITA_ORGANIZZATIVA.CD_UNITA_PADRE = '''||P_CDS||'''');
    End If;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'PDG_PREVENTIVO_SPE_DET.ESERCIZIO = '||P_Anno);
    End If;

    If P_UO is not null Then
       Costruisci_Stringa(Stringa, 'CDR.CD_UNITA_ORGANIZZATIVA = '''||P_UO||'''');
    End If;

    If P_ELE_VOCE is not null Then
       Costruisci_Stringa(Stringa, 'PDG_PREVENTIVO_SPE_DET.CD_ELEMENTO_VOCE = '''||P_ELE_VOCE||'''');
    End If;

    If P_LINEA is not null Then
       Costruisci_Stringa(Stringa, 'PDG_PREVENTIVO_SPE_DET.CD_LINEA_ATTIVITA = '''||P_LINEA||'''');
    End If;

    If P_TIPO = 'P' Then -- PERSONALE
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''B'', ''O'')');
    elsif P_TIPO = 'F' Then -- FUNZIONAMENTO
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''C'', ''A'', '||
                                                                     ' ''I'', ''L'', '||
                                                                     ' ''Q'', ''E'', '||
                                                                     ' ''P''   )');
    elsif P_TIPO = 'I' Then -- INVESTIMENTO
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''M'', ''N'')');
    elsif P_TIPO = 'S' Then -- PRESTAZIONI SCIENTIFICHE
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''F'', ''G'', ''H'')');
    elsif P_TIPO = 'B' Then -- BORSE DI STUDIO
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''D'') ');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return Calcola_valore(Stringa);
  End PDG_PREVISIONE_SPE;

FUNCTION PDG_PREVISIONE_ENT(
  P_Anno IN number,
  P_CDS IN varchar2,
  P_UO  IN varchar2,
  P_ELE_VOCE IN varchar2,
  P_LINEA IN varchar2,
  P_Stringa IN varchar2)
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
  Begin
    Stringa := 'Select nvl(sum(PDG_PREVENTIVO_ETR_DET.IM_RA_RCE), 0) '||
               ' from PDG_PREVENTIVO_ETR_DET, CDR, UNITA_ORGANIZZATIVA '||
               ' WHERE PDG_PREVENTIVO_ETR_DET.ORIGINE != ''PDV'' AND '||
               '       PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA AND '||
               '       CDR.CD_UNITA_ORGANIZZATIVA = UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA';

    If P_CDS is not null Then
       Costruisci_Stringa(Stringa, 'UNITA_ORGANIZZATIVA.CD_UNITA_PADRE = '''||P_CDS||'''');
    End If;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'PDG_PREVENTIVO_ETR_DET.ESERCIZIO = '||P_Anno);
    End If;

    If P_UO is not null Then
       Costruisci_Stringa(Stringa, 'CDR.CD_UNITA_ORGANIZZATIVA = '''||P_UO||'''');
    End If;

    If P_ELE_VOCE is not null Then
       Costruisci_Stringa(Stringa, 'PDG_PREVENTIVO_ETR_DET.CD_ELEMENTO_VOCE = '''||P_ELE_VOCE||'''');
    End If;

    If P_LINEA is not null Then
       Costruisci_Stringa(Stringa, 'PDG_PREVENTIVO_ETR_DET.CD_LINEA_ATTIVITA = '''||P_LINEA||'''');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return Calcola_valore(Stringa);
  End PDG_PREVISIONE_ENT;

FUNCTION Tot_Obbligazioni(
  P_Anno IN number,
  P_CDS IN varchar2,
  P_UO  IN varchar2,
  P_ELE_VOCE IN varchar2,
  P_LINEA IN varchar2,
  P_PAGATE IN varchar2,     -- Si /No /Tutte
  P_TIPO  IN VARCHAR2,  -- (P)erson, (F)unzion, (I)nvest, Prestaz (S)cient, (B)orse
  P_Stringa IN varchar2)
  RETURN NUMBER IS
    Stringa      varchar2(2000) := null;
  Begin
    If P_LINEA IS NOT NULL Then
       Stringa := 'Select nvl(sum(OBBLIGAZIONE_SCAD_VOCE.IM_VOCE), 0) '||
                  ' from OBBLIGAZIONE, OBBLIGAZIONE_SCADENZARIO, '||
                  ' OBBLIGAZIONE_SCAD_VOCE, ELEMENTO_VOCE '||
                  ' WHERE OBBLIGAZIONE.DT_CANCELLAZIONE IS NULL AND '||
                  '       OBBLIGAZIONE.STATO_OBBLIGAZIONE != ''S'' AND '||
                  '       OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT IN (''OBB'', ''OBB_PGIRO'') AND '||
                  '       OBBLIGAZIONE.CD_CDS               = OBBLIGAZIONE_SCADENZARIO.CD_CDS          AND '||
                  '       OBBLIGAZIONE.ESERCIZIO            = OBBLIGAZIONE_SCADENZARIO.ESERCIZIO       AND '||
                  '       OBBLIGAZIONE.ESERCIZIO_ORIGINALE  = OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE  AND '||
                  '       OBBLIGAZIONE.PG_OBBLIGAZIONE      = OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE AND '||
                  '       OBBLIGAZIONE_SCADENZARIO.CD_CDS                      = OBBLIGAZIONE_SCAD_VOCE.CD_CDS          AND '||
                  '       OBBLIGAZIONE_SCADENZARIO.ESERCIZIO                   = OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO       AND '||
                  '       OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE         = OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO_ORIGINALE AND '||
                  '       OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE             = OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE AND '||
                  '       OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO = OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE_SCADENZARIO AND '||
                  '       OBBLIGAZIONE.ESERCIZIO        = ELEMENTO_VOCE.ESERCIZIO        AND '||
                  '       OBBLIGAZIONE.TI_APPARTENENZA  = ELEMENTO_VOCE.TI_APPARTENENZA  AND '||
                  '       OBBLIGAZIONE.TI_GESTIONE      = ELEMENTO_VOCE.TI_GESTIONE      AND '||
                  '       OBBLIGAZIONE.CD_ELEMENTO_VOCE = ELEMENTO_VOCE.CD_ELEMENTO_VOCE ';
    Else
       Stringa := 'Select nvl(sum(OBBLIGAZIONE.IM_OBBLIGAZIONE), 0) '||
                  ' from OBBLIGAZIONE, OBBLIGAZIONE_SCADENZARIO, '||
                  ' ELEMENTO_VOCE '||
                  ' WHERE DT_CANCELLAZIONE IS NULL AND '||
                  '       STATO_OBBLIGAZIONE != ''S'' AND '||
                  '       OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT IN (''OBB'', ''OBB_PGIRO'') AND '||
                  '       OBBLIGAZIONE.CD_CDS               = OBBLIGAZIONE_SCADENZARIO.CD_CDS          AND '||
                  '       OBBLIGAZIONE.ESERCIZIO            = OBBLIGAZIONE_SCADENZARIO.ESERCIZIO       AND '||
                  '       OBBLIGAZIONE.ESERCIZIO_ORIGINALE  = OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE  AND '||
                  '       OBBLIGAZIONE.PG_OBBLIGAZIONE      = OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE AND '||
                  '       OBBLIGAZIONE.ESERCIZIO        = ELEMENTO_VOCE.ESERCIZIO        AND '||
                  '       OBBLIGAZIONE.TI_APPARTENENZA  = ELEMENTO_VOCE.TI_APPARTENENZA  AND '||
                  '       OBBLIGAZIONE.TI_GESTIONE      = ELEMENTO_VOCE.TI_GESTIONE      AND '||
                  '       OBBLIGAZIONE.CD_ELEMENTO_VOCE = ELEMENTO_VOCE.CD_ELEMENTO_VOCE';
    End If;

    If P_CDS is not null Then
       Costruisci_Stringa(Stringa, 'OBBLIGAZIONE.CD_CDS = '''||P_CDS||'''');
    End If;

    If P_Anno is not null Then
       Costruisci_Stringa(Stringa, 'OBBLIGAZIONE.ESERCIZIO = '||P_Anno);
    End If;

    If P_UO is not null Then
       Costruisci_Stringa(Stringa, 'OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA = '''||P_UO||'''');
    End If;

    If P_ELE_VOCE is not null Then
       Costruisci_Stringa(Stringa, 'OBBLIGAZIONE.CD_ELEMENTO_VOCE = '''||P_ELE_VOCE||'''');
    End If;

    If P_LINEA is not null Then
       Costruisci_Stringa(Stringa, 'OBBLIGAZIONE_SCAD_VOCE.CD_LINEA_ATTIVITA = '''||P_LINEA||'''');
    End If;

    If P_TIPO = 'P' Then -- PERSONALE
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''B'', ''O'')');
    elsif P_TIPO = 'F' Then -- FUNZIONAMENTO
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''C'', ''A'', '||
                                                                     ' ''I'', ''L'', '||
                                                                     ' ''Q'', ''E'', '||
                                                                     ' ''P''   )');
    elsif P_TIPO = 'I' Then -- INVESTIMENTO
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''M'', ''N'')');
    elsif P_TIPO = 'S' Then -- PRESTAZIONI SCIENTIFICHE
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''F'', ''G'', ''H'')');
    elsif P_TIPO = 'B' Then -- BORSE DI STUDIO
       Costruisci_Stringa(Stringa, 'ELEMENTO_VOCE.CD_CAPOCONTO_FIN IN (''D'') ');
    End If;

    If NVL(P_PAGATE, 'T') = 'S' Then
       Costruisci_Stringa(Stringa,
' EXISTS(SELECT 1 FROM MANDATO_RIGA'||
' WHERE OBBLIGAZIONE_SCADENZARIO.CD_CDS = MANDATO_RIGA.CD_CDS '||
'   AND OBBLIGAZIONE_SCADENZARIO.ESERCIZIO = MANDATO_RIGA.ESERCIZIO_OBBLIGAZIONE'||
'   AND OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE = MANDATO_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE'||
'   AND OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE = MANDATO_RIGA.PG_OBBLIGAZIONE'||
'   AND OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO = MANDATO_RIGA.PG_OBBLIGAZIONE_SCADENZARIO)');
    ELSIF NVL(P_PAGATE, 'T') = 'N' Then
       Costruisci_Stringa(Stringa,
' NOT EXISTS(SELECT 1 FROM MANDATO_RIGA'||
' WHERE OBBLIGAZIONE_SCADENZARIO.CD_CDS = MANDATO_RIGA.CD_CDS '||
'   AND OBBLIGAZIONE_SCADENZARIO.ESERCIZIO = MANDATO_RIGA.ESERCIZIO_OBBLIGAZIONE'||
'   AND OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE = MANDATO_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE'||
'   AND OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE = MANDATO_RIGA.PG_OBBLIGAZIONE'||
'   AND OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO = MANDATO_RIGA.PG_OBBLIGAZIONE_SCADENZARIO)');
    End If;

    If P_Stringa is not null Then
       Costruisci_Stringa(Stringa, '('||P_Stringa||')');
    End If;

    Return Calcola_valore(Stringa);
End Tot_OBBLIGAZIONI;

END CIR_FUNZGEST;
