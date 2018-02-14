CREATE OR REPLACE TRIGGER BU_PDG_ESERCIZIO
BEFORE Update Of STATO
on PDG_ESERCIZIO
for each row
Declare
 aRowtype PDG_ESERCIZIO%rowtype;
 aOldRowtype PDG_ESERCIZIO%rowtype;
Begin
--
-- Trigger attivato su aggiornamento della tabella PDG_ESERCIZIO solo se il campo STATO ha un new value CC (Before)
--
-- Date: 29/12/2005
-- Version: 1.0
--
-- Dependency:
--
-- History:
--
-- Date: 29/12/2005
-- Version: 1.0
-- Creazione
--

  If (:New.STATO = 'CC' And (:Old.STATO != 'CC' Or :Old.STATO Is Null) )Then

    aOldRowtype.ESERCIZIO                :=:old.ESERCIZIO;
    aOldRowtype.CD_CENTRO_RESPONSABILITA :=:old.CD_CENTRO_RESPONSABILITA;
    aOldRowtype.STATO                    :=:old.STATO;
    aOldRowtype.DACR                     :=:old.DACR;
    aOldRowtype.UTCR                     :=:old.UTCR;
    aOldRowtype.DUVA                     :=:old.DUVA;
    aOldRowtype.UTUV                     :=:old.UTUV;
    aOldRowtype.PG_VER_REC               :=:old.PG_VER_REC;

    aRowtype.ESERCIZIO                :=:New.ESERCIZIO;
    aRowtype.CD_CENTRO_RESPONSABILITA :=:New.CD_CENTRO_RESPONSABILITA;
    aRowtype.STATO                    :=:New.STATO;
    aRowtype.DACR                     :=:New.DACR;
    aRowtype.UTCR                     :=:New.UTCR;
    aRowtype.DUVA                     :=:New.DUVA;
    aRowtype.UTUV                     :=:New.UTUV;
    aRowtype.PG_VER_REC               :=:New.PG_VER_REC;


    -- Scarico dello storico
    CNRSTO060.SCARICASUSTORICO(aOldRowType, aRowType);

  End If;
end;
/


