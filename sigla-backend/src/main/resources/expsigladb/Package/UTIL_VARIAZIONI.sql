--------------------------------------------------------
--  DDL for Package UTIL_VARIAZIONI
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "UTIL_VARIAZIONI" AS
  Procedure generaStornoTraCdsMonoEv
   (aEs NUMBER,
    aEsRes NUMBER,
    aDsVar Varchar2,
    aFonti Varchar2,
    aTiGestione elemento_voce.ti_gestione%type,
    aCdCdrMeno In VARCHAR2,
    aTiAppVoceMeno elemento_voce.ti_appartenenza%type,
    aCdVoceMeno elemento_voce.cd_elemento_voce%type,
    aCdGaeMeno linea_attivita.cd_linea_attivita%type,
    aCdCdrPiu VARCHAR2,
    aTiAppVocePiu elemento_voce.ti_appartenenza%type,
    aCdVocePiu elemento_voce.cd_elemento_voce%type,
    aCdGaePiu linea_attivita.cd_linea_attivita%type,
    aImportoVar ass_pdg_variazione_cdr.IM_SPESA%type,
    aUser VARCHAR2,
    oEsVar Out NUMBER,
    oPgVar Out NUMBER
    );

Procedure generaPerenzioneDaRiaccResPass
   (aEs NUMBER,
    aDsVar Varchar2,
    aUser VARCHAR2,
    oNumVarCreate Out NUMBER);

Procedure generaStornoResidui
   (aEs NUMBER,
    aDsVar Varchar2,
    aCdCdrPiu In VARCHAR2,
    aTiAppVocePiu elemento_voce.ti_appartenenza%type,
    aCdVocePiu elemento_voce.cd_elemento_voce%type,
    aCdGaeFinPiu linea_attivita.cd_linea_attivita%type,
    aCdGaeFesPiu linea_attivita.cd_linea_attivita%type,
    aUser VARCHAR2,
    oNumVarCreate Out NUMBER); 
End UTIL_VARIAZIONI;
/
