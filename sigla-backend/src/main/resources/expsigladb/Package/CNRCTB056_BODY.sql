--------------------------------------------------------
--  DDL for Package Body CNRCTB056
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB056" IS
  SQLEQUALS  CONSTANT VARCHAR(10) := '=';
  SQLGREATER CONSTANT VARCHAR(10) := '>';
  SQLLESS    CONSTANT VARCHAR(10) := '<';

  PROCEDURE Costruisci_Stringa(
    p_stringa_ini IN OUT CLOB ,
    p_stringa_agg IN varchar2 )
  IS
  Begin
    If instr(upper(p_stringa_ini), 'WHERE') > 0 Then
       p_stringa_ini := p_stringa_ini||' and '||p_stringa_agg;
    Else
       p_stringa_ini := p_stringa_ini||' where '||p_stringa_agg;
    End If;
  End Costruisci_Stringa;

  PROCEDURE Costruisci_Stringa(
    p_stringa_ini IN OUT CLOB ,
    p_campo       IN varchar2,
    p_operatore   IN varchar2,
    p_valore      IN varchar2 )
  IS
  Begin
    If p_campo is not null and p_operatore is not null and p_valore is not null Then
       If instr(upper(p_stringa_ini), 'WHERE') > 0 Then
          p_stringa_ini := p_stringa_ini||' and '||p_campo||p_operatore||''''||p_valore||'''';
       Else
          p_stringa_ini := p_stringa_ini||' where '||p_campo||p_operatore||''''||p_valore||'''';
       End If;
    End if;
  End Costruisci_Stringa;

  FUNCTION Calcola_valore(
    p_stringa IN CLOB )
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

  Function getRecordParametri(
    aEsercizio       IN classificazione_voci.esercizio%type,
    aTiGestione      IN classificazione_voci.ti_gestione%type,
    aCdClass         IN v_classificazione_voci.cd_classificazione%type,
    aCdElementoVoce  IN elemento_voce.cd_elemento_voce%type default null,
    aCdLineaAttivita IN linea_attivita.cd_linea_attivita%type default null,
    aCdDipartimento  IN dipartimento.cd_dipartimento%type default null,
    aCdCds           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo            IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr           IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo        IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo    IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa      IN progetto_prev.cd_progetto%type default null,
    aCdProgetto      IN progetto_prev.cd_progetto%type default null,
    aPgVariazione    IN pdg_variazione.pg_variazione_pdg%type default null,
    aDataMax         IN pdg_variazione.dt_approvazione%type default null,
    aStatoVar        IN pdg_variazione.stato%type default null,
    aCdLiv1          IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2          IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3          IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4          IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5          IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6          IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7          IN classificazione_voci.cd_livello7%type default null,
    aStringa         IN varchar2 default null)
    RETURN CNRCTB056.cParametri
  IS
    recParametri CNRCTB056.cParametri;
  Begin
    recParametri.aEsercizio       := aEsercizio;
    recParametri.aTiGestione      := aTiGestione;
    recParametri.aCdClass         := aCdClass;
    recParametri.aCdElementoVoce  := aCdElementoVoce;
    recParametri.aCdLineaAttivita := aCdLineaAttivita;
    recParametri.aCdDipartimento  := aCdDipartimento;
    recParametri.aCdCds           := aCdCds;
    recParametri.aCdUo            := aCdUo;
    recParametri.aCdCdr           := aCdCdr;
    recParametri.aCdModulo        := aCdModulo;
    recParametri.aCdTipoModulo    := aCdTipoModulo;
    recParametri.aCdCommessa      := aCdCommessa;
    recParametri.aCdProgetto      := aCdProgetto;
    recParametri.aPgVariazione    := aPgVariazione;
    recParametri.aDataMax         := aDataMax;
    recParametri.aStatoVar        := aStatoVar;
    recParametri.aCdLiv1          := aCdLiv1;
    recParametri.aCdLiv2          := aCdLiv2;
    recParametri.aCdLiv3          := aCdLiv3;
    recParametri.aCdLiv4          := aCdLiv4;
    recParametri.aCdLiv5          := aCdLiv5;
    recParametri.aCdLiv6          := aCdLiv6;
    recParametri.aCdLiv7          := aCdLiv7;
    recParametri.aStringa         := aStringa;
    return recParametri;
  End getRecordParametri;

  PROCEDURE baseDefaultWhere(
    Stringa          IN OUT CLOB,
    aTab             IN varchar2,
    recParametri     IN CNRCTB056.cParametri)
  IS
  Begin
    Costruisci_Stringa(Stringa, aTab||'.ESERCIZIO', SQLEQUALS, recParametri.aEsercizio);
    Costruisci_Stringa(Stringa, aTab||'.CD_DIPARTIMENTO', SQLEQUALS, recParametri.aCdDipartimento);
    Costruisci_Stringa(Stringa, aTab||'.CD_CENTRO_SPESA', SQLEQUALS, recParametri.aCdCds);
    Costruisci_Stringa(Stringa, aTab||'.CD_UNITA_ORGANIZZATIVA', SQLEQUALS, recParametri.aCdUo);
    Costruisci_Stringa(Stringa, aTab||'.CD_CENTRO_RESPONSABILITA', SQLEQUALS, recParametri.aCdCdr);
    Costruisci_Stringa(Stringa, aTab||'.CD_CLASSIFICAZIONE', SQLEQUALS, recParametri.aCdClass);
    Costruisci_Stringa(Stringa, aTab||'.CD_ELEMENTO_VOCE', SQLEQUALS, recParametri.aCdElementoVoce);
    Costruisci_Stringa(Stringa, aTab||'.CD_LINEA_ATTIVITA', SQLEQUALS, recParametri.aCdLineaAttivita);
    Costruisci_Stringa(Stringa, aTab||'.CD_MODULO', SQLEQUALS, recParametri.aCdModulo);
    Costruisci_Stringa(Stringa, aTab||'.CD_TIPO_MODULO', SQLEQUALS, recParametri.aCdTipoModulo);
    Costruisci_Stringa(Stringa, aTab||'.CD_COMMESSA', SQLEQUALS, recParametri.aCdCommessa);
    Costruisci_Stringa(Stringa, aTab||'.CD_PROGETTO', SQLEQUALS, recParametri.aCdProgetto);
    Costruisci_Stringa(Stringa, aTab||'.CD_LIVELLO1', SQLEQUALS, recParametri.aCdLiv1);
    Costruisci_Stringa(Stringa, aTab||'.CD_LIVELLO2', SQLEQUALS, recParametri.aCdLiv2);
    Costruisci_Stringa(Stringa, aTab||'.CD_LIVELLO3', SQLEQUALS, recParametri.aCdLiv3);
    Costruisci_Stringa(Stringa, aTab||'.CD_LIVELLO4', SQLEQUALS, recParametri.aCdLiv4);
    Costruisci_Stringa(Stringa, aTab||'.CD_LIVELLO5', SQLEQUALS, recParametri.aCdLiv5);
    Costruisci_Stringa(Stringa, aTab||'.CD_LIVELLO6', SQLEQUALS, recParametri.aCdLiv6);
    Costruisci_Stringa(Stringa, aTab||'.CD_LIVELLO7', SQLEQUALS, recParametri.aCdLiv7);

    If recParametri.aStringa is not null Then
       Costruisci_Stringa(Stringa, '('||recParametri.aStringa||')');
    End If;
/*
dbms_output.put_line(substr(Stringa,1,200));
dbms_output.put_line(substr(Stringa,201,200));
dbms_output.put_line(substr(Stringa,401,200));
dbms_output.put_line(substr(Stringa,601,200));
dbms_output.put_line(substr(Stringa,801,200));
*/
  End baseDefaultWhere;

  FUNCTION getStanziamentoPreliminare(recParametri IN CNRCTB056.cParametri)
    RETURN NUMBER IS
    Stringa      CLOB := null;
    aTab         varchar2(50) := null;
  Begin
    If recParametri.aTiGestione = 'S' Then
      aTab := 'V_CONS_PDG_SPE_PRELIMINARE';
      Stringa := 'Select NVL(SUM(nvl('||aTab||'.IM_DEC_IST_INT, 0) + '||
                               ' nvl('||aTab||'.IM_DEC_IST_EST, 0) + '||
                               ' nvl('||aTab||'.IM_DEC_AREA_INT, 0) + '||
                               ' nvl('||aTab||'.IM_DEC_AREA_EST, 0) + '||
                               ' nvl('||aTab||'.TRATT_ECON_INT, 0) + '||
                               ' nvl('||aTab||'.TRATT_ECON_EST, 0) + '||
                               ' nvl('||aTab||'.IM_ACC_ALTRE_SP_INT, 0)), 0) '||
                 ' from '||aTab;
    Else
      aTab := 'V_CONS_PDG_ETR_PRELIMINARE';
      Stringa := 'Select NVL(SUM('||aTab||'.TOT_ENT_IST_A1), 0) '||
                 ' from '||aTab;
    End If;

    baseDefaultWhere(Stringa, aTab, recParametri);
    Return Calcola_valore(Stringa);
  End getStanziamentoPreliminare;

  FUNCTION getStanziamentoPersonalePre(recParametri IN CNRCTB056.cParametri)
    RETURN NUMBER IS
    Stringa      CLOB := null;
    aTab         varchar2(50) := null;
  Begin
    If recParametri.aTiGestione = 'S' Then
      aTab := 'V_CONS_PDG_SPE_PRELIMINARE';
      Stringa := 'Select NVL(SUM(nvl('||aTab||'.TRATT_ECON_INT, 0) + '||
                               ' nvl('||aTab||'.TRATT_ECON_EST, 0)), 0) '||
                 ' from '||aTab;
    Else
      Return (0);
    End If;

    baseDefaultWhere(Stringa, aTab, recParametri);
    Return Calcola_valore(Stringa);
  End getStanziamentoPersonalePre;

  FUNCTION getStanziamentoGestionale(recParametri IN CNRCTB056.cParametri)
    RETURN NUMBER IS
    Stringa      CLOB := null;
    aTab         varchar2(50) := null;
  Begin
    If recParametri.aTiGestione = 'S' Then
      aTab := 'V_CONS_PDG_SPE_GESTIONALE';
      Stringa := 'Select NVL(SUM(nvl('||aTab||'.STANZIAMENTO_DEC_INT, 0) + '||
                               ' nvl('||aTab||'.STANZIAMENTO_DEC_EST, 0)), 0) '||
                 ' from '||aTab;
    Else
      aTab := 'V_CONS_PDG_ETR_GESTIONALE';
      Stringa := 'Select NVL(SUM('||aTab||'.TOT_ENT_IST_A1), 0) '||
                 ' from '||aTab;
    End If;

    baseDefaultWhere(Stringa, aTab, recParametri);
    Return Calcola_valore(Stringa);
  End getStanziamentoGestionale;

  FUNCTION getVariazioniGestionale(recParametri IN CNRCTB056.cParametri)
    RETURN NUMBER IS
    Stringa      CLOB := null;
    aTab         varchar2(50) := null;
  Begin
    aTab := 'V_CONS_PDG_VAR_GESTIONALE';

    If recParametri.aTiGestione = CNRCTB001.GESTIONE_SPESE Then
        Stringa := 'Select NVL(SUM(nvl('||aTab||'.VARIAZIONI_POSITIVE_DEC_INT, 0) + '||
                                 ' nvl('||aTab||'.VARIAZIONI_POSITIVE_DEC_EST, 0) + '||
                                 ' nvl('||aTab||'.VARIAZIONI_NEGATIVE_DEC_INT, 0) + '||
                                 ' nvl('||aTab||'.VARIAZIONI_NEGATIVE_DEC_EST, 0) + '||
                                 ' nvl('||aTab||'.VARIAZIONI_POSITIVE_ACC_INT, 0) + '||
                                 ' nvl('||aTab||'.VARIAZIONI_POSITIVE_ACC_EST, 0) + '||
                                 ' nvl('||aTab||'.VARIAZIONI_NEGATIVE_ACC_INT, 0) + '||
                                 ' nvl('||aTab||'.VARIAZIONI_NEGATIVE_ACC_EST, 0)), 0) '||
                   ' from '||aTab;
    Else
        Stringa := 'Select NVL(SUM(nvl('||aTab||'.VARIAZIONI_POSITIVE_ENTRATA, 0) + '||
                                 ' nvl('||aTab||'.VARIAZIONI_NEGATIVE_ENTRATA, 0)), 0) '||
                   ' from '||aTab;
    End If;

    baseDefaultWhere(Stringa, aTab, recParametri);
    Costruisci_Stringa(Stringa, aTab||'.TI_GESTIONE', SQLEQUALS, recParametri.aTiGestione);
    Costruisci_Stringa(Stringa, aTab||'.DT_APPROVAZIONE', SQLLESS, recParametri.aDataMax);
    Costruisci_Stringa(Stringa, aTab||'.STATO', SQLEQUALS, recParametri.aStatoVar);
    Return Calcola_valore(Stringa);
  End getVariazioniGestionale;

  FUNCTION getAssestatoGestionale(recParametri IN CNRCTB056.cParametri)
    RETURN NUMBER IS
  Begin
    Return getStanziamentoGestionale(recParametri) + getVariazioniGestionale(recParametri);
  End getAssestatoGestionale;

  FUNCTION getVariazioniPreVarGestionale(recParametri IN CNRCTB056.cParametri)
    RETURN NUMBER IS
    Stringa        CLOB := null;
    aTab           varchar2(50) := null;
    recVariazione  Pdg_variazione%rowtype;
  Begin
    If recParametri.aPgVariazione is null Then
       return getVariazioniGestionale(recParametri);
    End If;

    Begin
      Select * into recVariazione
      from Pdg_variazione
      where esercizio = recParametri.aEsercizio
      and   pg_variazione_pdg = recParametri.aPgVariazione;
    Exception
      When others Then
        return (0);
    End;

    aTab := 'V_CONS_PDG_VAR_GESTIONALE';

    Stringa := 'Select NVL(SUM(nvl('||aTab||'.VARIAZIONI_POSITIVE_DEC_INT, 0) + '||
                             ' nvl('||aTab||'.VARIAZIONI_POSITIVE_DEC_EST, 0) + '||
                             ' nvl('||aTab||'.VARIAZIONI_NEGATIVE_DEC_INT, 0) + '||
                             ' nvl('||aTab||'.VARIAZIONI_NEGATIVE_DEC_EST, 0)), 0) '||
               ' from '||aTab;

    baseDefaultWhere(Stringa, aTab, recParametri);
    Costruisci_Stringa(Stringa, aTab||'.TI_GESTIONE', SQLEQUALS, recParametri.aTiGestione);
    Costruisci_Stringa(Stringa, aTab||'.STATO IN (''APP'', ''APF'')');
    Costruisci_Stringa(Stringa, aTab||'.STATO', SQLEQUALS, recParametri.aStatoVar);
    Costruisci_Stringa(Stringa, aTab||'.PG_VARIAZIONE_PDG', SQLLESS, recParametri.aPgVariazione);
    Costruisci_Stringa(Stringa, 'TRUNC('||aTab||'.DT_APPROVAZIONE)', SQLLESS, 'TRUNC('||recVariazione.DT_APPROVAZIONE||')');

    Return Calcola_valore(Stringa);
  End getVariazioniPreVarGestionale;

  FUNCTION getAssestatoPreVarGestionale(recParametri IN CNRCTB056.cParametri)
    RETURN NUMBER IS
  Begin
    return getStanziamentoGestionale(recParametri) + getVariazioniPreVarGestionale(recParametri);
  End getAssestatoPreVarGestionale;

  FUNCTION getStanziamentoPreliminare(
    aEsercizio       IN classificazione_voci.esercizio%type ,
    aTiGestione      IN classificazione_voci.ti_gestione%type,
    aCdClass         IN v_classificazione_voci.cd_classificazione%type default null,
    aCdDipartimento  IN dipartimento.cd_dipartimento%type default null,
    aCdCds           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo            IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr           IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo        IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo    IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa      IN progetto_prev.cd_progetto%type default null,
    aCdProgetto      IN progetto_prev.cd_progetto%type default null,
    aCdLiv1          IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2          IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3          IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4          IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5          IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6          IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7          IN classificazione_voci.cd_livello7%type default null,
    aStringa         IN varchar2 default null)
  RETURN NUMBER
  IS
  Begin
    return(getStanziamentoPreliminare(getRecordParametri(aEsercizio, aTiGestione, aCdClass, null, null,
                                                         aCdDipartimento, aCdCds, aCdUo, aCdCdr, aCdModulo,
                                                         aCdTipoModulo, aCdCommessa, aCdProgetto, null,
                                                         aCdLiv1, aCdLiv2, aCdLiv3, aCdLiv4, aCdLiv5,
                                                         aCdLiv6, aCdLiv7, aStringa)));
  End getStanziamentoPreliminare;

  FUNCTION getStanziamentoPersonalePre(
    aEsercizio       IN classificazione_voci.esercizio%type ,
    aTiGestione      IN classificazione_voci.ti_gestione%type,
    aCdClass         IN v_classificazione_voci.cd_classificazione%type default null,
    aCdDipartimento  IN dipartimento.cd_dipartimento%type default null,
    aCdCds           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo            IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr           IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo        IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo    IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa      IN progetto_prev.cd_progetto%type default null,
    aCdProgetto      IN progetto_prev.cd_progetto%type default null,
    aCdLiv1          IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2          IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3          IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4          IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5          IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6          IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7          IN classificazione_voci.cd_livello7%type default null,
    aStringa         IN varchar2 default null)
  RETURN NUMBER
  IS
  Begin
    return(getStanziamentoPersonalePre(getRecordParametri(aEsercizio, aTiGestione, aCdClass, null, null,
                                                         aCdDipartimento, aCdCds, aCdUo, aCdCdr, aCdModulo,
                                                         aCdTipoModulo, aCdCommessa, aCdProgetto, null,
                                                         aCdLiv1, aCdLiv2, aCdLiv3, aCdLiv4, aCdLiv5,
                                                         aCdLiv6, aCdLiv7, aStringa)));
  End getStanziamentoPersonalePre;

  FUNCTION getStanziamentoGestionale(
    aEsercizio       IN classificazione_voci.esercizio%type ,
    aTiGestione      IN classificazione_voci.ti_gestione%type,
    aCdElementoVoce  IN elemento_voce.cd_elemento_voce%type default null,
    aCdLineaAttivita IN linea_attivita.cd_linea_attivita%type default null,
    aCdDipartimento  IN dipartimento.cd_dipartimento%type default null,
    aCdCds           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo            IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr           IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo        IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo    IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa      IN progetto_prev.cd_progetto%type default null,
    aCdProgetto      IN progetto_prev.cd_progetto%type default null,
    aCdLiv1          IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2          IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3          IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4          IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5          IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6          IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7          IN classificazione_voci.cd_livello7%type default null,
    aStringa         IN varchar2 default null)
  RETURN NUMBER
  IS
  Begin
    return(getStanziamentoGestionale(getRecordParametri(aEsercizio, aTiGestione, null, aCdElementoVoce,
                                                        aCdLineaAttivita, aCdDipartimento, aCdCds, aCdUo, aCdCdr,
                                                        aCdModulo, aCdTipoModulo, aCdCommessa, aCdProgetto, null,
                                                        aCdLiv1, aCdLiv2, aCdLiv3, aCdLiv4, aCdLiv5,
                                                        aCdLiv6, aCdLiv7, aStringa)));
  End getStanziamentoGestionale;

  FUNCTION getVariazioniGestionale(
    aEsercizio       IN classificazione_voci.esercizio%type ,
    aTiGestione      IN classificazione_voci.ti_gestione%type,
    aCdElementoVoce  IN elemento_voce.cd_elemento_voce%type default null,
    aCdLineaAttivita IN linea_attivita.cd_linea_attivita%type default null,
    aCdDipartimento  IN dipartimento.cd_dipartimento%type default null,
    aCdCds           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo            IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr           IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo        IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo    IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa      IN progetto_prev.cd_progetto%type default null,
    aCdProgetto      IN progetto_prev.cd_progetto%type default null,
    aDataMax         IN pdg_variazione.dt_approvazione%type default null,
    aStatoVar        IN pdg_variazione.stato%type default null,
    aCdLiv1          IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2          IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3          IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4          IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5          IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6          IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7          IN classificazione_voci.cd_livello7%type default null,
    aStringa         IN varchar2 default null)
  RETURN NUMBER
  IS
  Begin
    return(getVariazioniGestionale(getRecordParametri(aEsercizio, aTiGestione, null, aCdElementoVoce,
                                                      aCdLineaAttivita, aCdDipartimento, aCdCds, aCdUo, aCdCdr,
                                                      aCdModulo, aCdTipoModulo, aCdCommessa, aCdProgetto, null,
                                                      aStatoVar, aCdLiv1, aCdLiv2, aCdLiv3, aCdLiv4, aCdLiv5,
                                                      aCdLiv6, aCdLiv7, aStringa)));
  End getVariazioniGestionale;

  FUNCTION getAssestatoGestionale(
    aEsercizio       IN classificazione_voci.esercizio%type ,
    aTiGestione       IN classificazione_voci.ti_gestione%type,
    aCdElementoVoce  IN elemento_voce.cd_elemento_voce%type default null,
    aCdLineaAttivita IN linea_attivita.cd_linea_attivita%type default null,
    aCdDipartimento  IN dipartimento.cd_dipartimento%type default null,
    aCdCds           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo            IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr           IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo        IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo    IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa      IN progetto_prev.cd_progetto%type default null,
    aCdProgetto      IN progetto_prev.cd_progetto%type default null,
    aDataMax         IN pdg_variazione.dt_approvazione%type default null,
    aStatoVar        IN pdg_variazione.stato%type default null,
    aCdLiv1          IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2          IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3          IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4          IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5          IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6          IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7          IN classificazione_voci.cd_livello7%type default null,
    aStringa         IN varchar2 default null)
  RETURN NUMBER
  IS
  Begin
    return(getAssestatoGestionale(getRecordParametri(aEsercizio, aTiGestione, null, aCdElementoVoce,
                                                     aCdLineaAttivita, aCdDipartimento, aCdCds, aCdUo, aCdCdr,
                                                     aCdModulo, aCdTipoModulo, aCdCommessa, aCdProgetto, null,
                                                     aStatoVar, aCdLiv1, aCdLiv2, aCdLiv3, aCdLiv4, aCdLiv5,
                                                     aCdLiv6, aCdLiv7, aStringa)));
  End getAssestatoGestionale;

  FUNCTION getVariazioniPreVarGestionale(
    aEsercizio       IN classificazione_voci.esercizio%type ,
    aTiGestione      IN classificazione_voci.ti_gestione%type,
    aCdElementoVoce  IN elemento_voce.cd_elemento_voce%type default null,
    aCdLineaAttivita IN linea_attivita.cd_linea_attivita%type default null,
    aCdDipartimento  IN dipartimento.cd_dipartimento%type default null,
    aCdCds           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo            IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr           IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo        IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo    IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa      IN progetto_prev.cd_progetto%type default null,
    aCdProgetto      IN progetto_prev.cd_progetto%type default null,
    aPgVariazione    IN pdg_variazione.pg_variazione_pdg%type default null,
    aStatoVar        IN pdg_variazione.stato%type default null,
    aCdLiv1          IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2          IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3          IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4          IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5          IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6          IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7          IN classificazione_voci.cd_livello7%type default null,
    aStringa         IN varchar2 default null)
  RETURN NUMBER
  IS
  Begin
    return(getVariazioniPreVarGestionale(getRecordParametri(aEsercizio, aTiGestione, null, aCdElementoVoce,
                                                      aCdLineaAttivita, aCdDipartimento, aCdCds, aCdUo, aCdCdr,
                                                      aCdModulo, aCdTipoModulo, aCdCommessa, aCdProgetto, aPgVariazione,
                                                      aStatoVar, aCdLiv1, aCdLiv2, aCdLiv3, aCdLiv4, aCdLiv5,
                                                      aCdLiv6, aCdLiv7, aStringa)));
  End getVariazioniPreVarGestionale;

  FUNCTION getAssestatoPreVarGestionale(
    aEsercizio       IN classificazione_voci.esercizio%type ,
    aTiGestione      IN classificazione_voci.ti_gestione%type,
    aCdElementoVoce  IN elemento_voce.cd_elemento_voce%type default null,
    aCdLineaAttivita IN linea_attivita.cd_linea_attivita%type default null,
    aCdDipartimento  IN dipartimento.cd_dipartimento%type default null,
    aCdCds           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo            IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr           IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo        IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo    IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa      IN progetto_prev.cd_progetto%type default null,
    aCdProgetto      IN progetto_prev.cd_progetto%type default null,
    aPgVariazione    IN pdg_variazione.pg_variazione_pdg%type default null,
    aStatoVar        IN pdg_variazione.stato%type default null,
    aCdLiv1          IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2          IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3          IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4          IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5          IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6          IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7          IN classificazione_voci.cd_livello7%type default null,
    aStringa         IN varchar2 default null)
  RETURN NUMBER
  IS
  Begin
    return(getAssestatoPreVarGestionale(getRecordParametri(aEsercizio, aTiGestione, null, aCdElementoVoce,
                                                     aCdLineaAttivita, aCdDipartimento, aCdCds, aCdUo, aCdCdr,
                                                     aCdModulo, aCdTipoModulo, aCdCommessa, aCdProgetto, aPgVariazione,
                                                     aStatoVar, aCdLiv1, aCdLiv2, aCdLiv3, aCdLiv4, aCdLiv5,
                                                     aCdLiv6, aCdLiv7, aStringa)));
  End getAssestatoPreVarGestionale;
END CNRCTB056;
