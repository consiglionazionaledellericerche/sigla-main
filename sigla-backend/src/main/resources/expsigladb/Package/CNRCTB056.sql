--------------------------------------------------------
--  DDL for Package CNRCTB056
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB056" IS
-- CNRCTB056 - Package di recupero importi previsionali/gestionali dal PDG Preliminare e PDG Decisionale
-- Date: 19/05/2006
-- Version: 1.0

  TYPE cParametri IS
    RECORD (aEsercizio       classificazione_voci.esercizio%type,
            aTiGestione      classificazione_voci.ti_gestione%type,
            aCdClass         v_classificazione_voci.cd_classificazione%type,
            aCdElementoVoce  elemento_voce.cd_elemento_voce%type,
            aCdLineaAttivita linea_attivita.cd_linea_attivita%type,
            aCdDipartimento  dipartimento.cd_dipartimento%type,
            aCdCds           unita_organizzativa.cd_unita_organizzativa%type,
            aCdUo            unita_organizzativa.cd_unita_organizzativa%type,
            aCdCdr           cdr.cd_centro_responsabilita%type,
            aCdModulo        progetto_prev.cd_progetto%type,
            aCdTipoModulo    progetto_prev.cd_tipo_progetto%type,
            aCdCommessa      progetto_prev.cd_progetto%type,
            aCdProgetto      progetto_prev.cd_progetto%type,
            aPgVariazione    pdg_variazione.pg_variazione_pdg%type,
            aDataMax         pdg_variazione.dt_approvazione%type,
            aStatoVar        pdg_variazione.stato%type,
            aCdLiv1          classificazione_voci.cd_livello1%type,
            aCdLiv2          classificazione_voci.cd_livello2%type,
            aCdLiv3          classificazione_voci.cd_livello3%type,
            aCdLiv4          classificazione_voci.cd_livello4%type,
            aCdLiv5          classificazione_voci.cd_livello5%type,
            aCdLiv6          classificazione_voci.cd_livello6%type,
            aCdLiv7          classificazione_voci.cd_livello7%type,
            aStringa         varchar2(4000));

  Function getStanziamentoPreliminare(
    recParametri IN CNRCTB056.cParametri)
  RETURN NUMBER;

  Function getStanziamentoPersonalePre(
    recParametri IN CNRCTB056.cParametri)
  RETURN NUMBER;

  Function getStanziamentoGestionale(
    recParametri IN CNRCTB056.cParametri)
  RETURN NUMBER;

  Function getVariazioniGestionale(
    recParametri IN CNRCTB056.cParametri)
  RETURN NUMBER;

  Function getAssestatoGestionale(
    recParametri IN CNRCTB056.cParametri)
  RETURN NUMBER;

  Function getVariazioniPreVarGestionale(
    recParametri IN CNRCTB056.cParametri)
  RETURN NUMBER;

  Function getAssestatoPreVarGestionale(
    recParametri IN CNRCTB056.cParametri)
  RETURN NUMBER;

  Function getStanziamentoPreliminare(
    aEsercizio      IN classificazione_voci.esercizio%type ,
    aTiGestione     IN classificazione_voci.ti_gestione%type,
    aCdClass        IN v_classificazione_voci.cd_classificazione%type default null,
    aCdDipartimento IN dipartimento.cd_dipartimento%type default null,
    aCdCds          IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr          IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo       IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo   IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa     IN progetto_prev.cd_progetto%type default null,
    aCdProgetto     IN progetto_prev.cd_progetto%type default null,
    aCdLiv1         IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2         IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3         IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4         IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5         IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6         IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7         IN classificazione_voci.cd_livello7%type default null,
    aStringa        IN varchar2 default null)
  RETURN NUMBER;

  Function getStanziamentoPersonalePre(
    aEsercizio      IN classificazione_voci.esercizio%type ,
    aTiGestione     IN classificazione_voci.ti_gestione%type,
    aCdClass        IN v_classificazione_voci.cd_classificazione%type default null,
    aCdDipartimento IN dipartimento.cd_dipartimento%type default null,
    aCdCds          IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdUo           IN unita_organizzativa.cd_unita_organizzativa%type default null,
    aCdCdr          IN cdr.cd_centro_responsabilita%type default null,
    aCdModulo       IN progetto_prev.cd_progetto%type default null,
    aCdTipoModulo   IN progetto_prev.cd_tipo_progetto%type default null,
    aCdCommessa     IN progetto_prev.cd_progetto%type default null,
    aCdProgetto     IN progetto_prev.cd_progetto%type default null,
    aCdLiv1         IN classificazione_voci.cd_livello1%type default null,
    aCdLiv2         IN classificazione_voci.cd_livello2%type default null,
    aCdLiv3         IN classificazione_voci.cd_livello3%type default null,
    aCdLiv4         IN classificazione_voci.cd_livello4%type default null,
    aCdLiv5         IN classificazione_voci.cd_livello5%type default null,
    aCdLiv6         IN classificazione_voci.cd_livello6%type default null,
    aCdLiv7         IN classificazione_voci.cd_livello7%type default null,
    aStringa        IN varchar2 default null)
  RETURN NUMBER;

  Function getStanziamentoGestionale(
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
  RETURN NUMBER;

  Function getVariazioniGestionale(
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
  RETURN NUMBER;

  Function getAssestatoGestionale(
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
  RETURN NUMBER;

  Function getVariazioniPreVarGestionale(
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
  RETURN NUMBER;

  Function getAssestatoPreVarGestionale(
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
  RETURN NUMBER;
END CNRCTB056;
