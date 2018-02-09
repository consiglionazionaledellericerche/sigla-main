CREATE OR REPLACE TRIGGER BD_MINICARRIERA_RATA
BEFORE Delete
On MINICARRIERA_RATA
For each row
WHEN (
old.PG_MINICARRIERA                 >0
      )
Declare
   aOldRowtype MINICARRIERA_RATA%rowtype;
Begin
   --
   -- Trigger attivato su cancellazione della tabella MINICARRIERA_RATA (Before)
   --
   -- Date: 05/05/2014
   -- Version: 1.0
   --
   -- Dependency: CNRSTO090
   --
   -- History:
   --
aOldRowtype.CD_CDS                     :=  :old.CD_CDS									;
aOldRowtype.CD_UNITA_ORGANIZZATIVA     :=  :old.CD_UNITA_ORGANIZZATIVA ;
aOldRowtype.ESERCIZIO                  :=  :old.ESERCIZIO              ;
aOldRowtype.PG_MINICARRIERA            :=  :old.PG_MINICARRIERA        ;
aOldRowtype.PG_RATA                    :=  :old.PG_RATA                ;
aOldRowtype.DT_INIZIO_RATA             :=  :old.DT_INIZIO_RATA         ;
aOldRowtype.DT_FINE_RATA               :=  :old.DT_FINE_RATA           ;
aOldRowtype.DT_SCADENZA                :=  :old.DT_SCADENZA            ;
aOldRowtype.IM_RATA            				 :=  :old.IM_RATA                ;
aOldRowtype.STATO_ASS_COMPENSO         :=  :old.STATO_ASS_COMPENSO     ;
aOldRowtype.CD_CDS_COMPENSO            :=  :old.CD_CDS_COMPENSO        ;
aOldRowtype.CD_UO_COMPENSO             :=  :old.CD_UO_COMPENSO         ;
aOldRowtype.ESERCIZIO_COMPENSO         :=  :old.ESERCIZIO_COMPENSO     ;
aOldRowtype.PG_COMPENSO                :=  :old.PG_COMPENSO            ;
aOldRowtype.DACR                       :=  :old.DACR                   ;
aOldRowtype.UTCR                       :=  :old.UTCR                   ;
aOldRowtype.DUVA                       :=  :old.DUVA                   ;
aOldRowtype.UTUV                       :=  :old.UTUV                   ;
aOldRowtype.PG_VER_REC					       :=  :old.PG_VER_REC						 ;

   -- Scarico dello storico
   CNRSTO090.sto_MINICARRIERA_RATA(:old.PG_VER_REC+1, aOldRowType);
End;
/


