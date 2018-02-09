CREATE OR REPLACE TRIGGER BU_PDG_RESIDUO
BEFORE Update Of STATO
on PDG_RESIDUO
for each row
declare
 aRowtype PDG_RESIDUO%rowtype;
 aOldRowtype PDG_RESIDUO%rowtype;
 aDetRowtype PDG_RESIDUO_DET%rowtype;
 Cursor c (v_esercizio NUMBER, v_centro VARCHAR2) Is
    Select *
    From PDG_RESIDUO_DET
    Where ESERCIZIO=v_esercizio
      And CD_CENTRO_RESPONSABILITA=v_centro;
begin
--
-- Trigger attivato su aggiornamento della tabella PDG_RESIDUO solo se il campo STATO ha un old value C (Before)
--
-- Date: 12/06/2005
-- Version: 1.0
--
-- Dependency: CNRSTO040
--
-- History:
--
-- Date: 12/06/2005
-- Version: 1.0
-- Creazione
--

  If (:old.STATO = 'C') Then
     aRowtype.ESERCIZIO:=:new.ESERCIZIO;
     aRowtype.CD_CENTRO_RESPONSABILITA:=:new.CD_CENTRO_RESPONSABILITA;
     aRowtype.STATO:=:new.STATO;
     aRowtype.IM_MASSA_SPENDIBILE:=:new.IM_MASSA_SPENDIBILE;
     aRowtype.DACR:=:new.DACR;
     aRowtype.UTCR:=:new.UTCR;
     aRowtype.DUVA:=:new.DUVA;
     aRowtype.UTUV:=:new.UTUV;
     aRowtype.PG_VER_REC:=:new.PG_VER_REC;

     aOldRowtype.ESERCIZIO:=:old.ESERCIZIO;
     aOldRowtype.CD_CENTRO_RESPONSABILITA:=:old.CD_CENTRO_RESPONSABILITA;
     aOldRowtype.STATO:=:old.STATO;
     aOldRowtype.IM_MASSA_SPENDIBILE:=:old.IM_MASSA_SPENDIBILE;
     aOldRowtype.DACR:=:old.DACR;
     aOldRowtype.UTCR:=:old.UTCR;
     aOldRowtype.DUVA:=:old.DUVA;
     aOldRowtype.UTUV:=:old.UTUV;
     aOldRowtype.PG_VER_REC:=:old.PG_VER_REC;

-- Scarico dello storico

   insert into PDG_RESIDUO_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,STATO
    ,IM_MASSA_SPENDIBILE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aOldRowtype.PG_VER_REC
    ,Null
    ,aOldRowtype.ESERCIZIO
    ,aOldRowtype.CD_CENTRO_RESPONSABILITA
    ,aOldRowtype.STATO
    ,aOldRowtype.IM_MASSA_SPENDIBILE
    ,aOldRowtype.DACR
    ,aOldRowtype.UTCR
    ,aOldRowtype.DUVA
    ,aOldRowtype.UTUV
    ,aOldRowtype.PG_VER_REC
    );

    For crec In c (aOldRowtype.ESERCIZIO,aOldRowtype.CD_CENTRO_RESPONSABILITA)
    Loop

      Insert into PDG_RESIDUO_DET_S (
         pg_storico_
        ,ds_storico_
        ,ESERCIZIO
        ,CD_CENTRO_RESPONSABILITA
        ,PG_DETTAGLIO
        ,CD_CDR_LINEA
        ,CD_LINEA_ATTIVITA
        ,CD_FUNZIONE
        ,CD_NATURA
        ,TI_APPARTENENZA
        ,TI_GESTIONE
        ,CD_ELEMENTO_VOCE
        ,DT_REGISTRAZIONE
        ,DESCRIZIONE
        ,IM_RESIDUO
        ,STATO
        ,DACR
        ,UTCR
        ,DUVA
        ,UTUV
        ,PG_VER_REC
       ) values (
         aOldRowtype.PG_VER_REC
        ,Null
        ,crec.ESERCIZIO
        ,crec.CD_CENTRO_RESPONSABILITA
        ,crec.PG_DETTAGLIO
        ,crec.CD_CDR_LINEA
        ,crec.CD_LINEA_ATTIVITA
        ,crec.CD_FUNZIONE
        ,crec.CD_NATURA
        ,crec.TI_APPARTENENZA
        ,crec.TI_GESTIONE
        ,crec.CD_ELEMENTO_VOCE
        ,crec.DT_REGISTRAZIONE
        ,crec.DESCRIZIONE
        ,crec.IM_RESIDUO
        ,crec.STATO
        ,crec.DACR
        ,crec.UTCR
        ,crec.DUVA
        ,crec.UTUV
        ,crec.PG_VER_REC
       );
    End Loop;
  End If;
end;
/


