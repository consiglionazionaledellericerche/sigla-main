CREATE OR REPLACE TRIGGER BU_MINICARRIERA_RATA
BEFORE Update
On MINICARRIERA_RATA
For each row
WHEN (
(old.DT_INIZIO_RATA 			!= new.DT_INIZIO_RATA or
  old.DT_FINE_RATA   			!= new.DT_FINE_RATA or
  old.DT_SCADENZA    			!= new.DT_SCADENZA or
  old.IM_RATA        			!= new.IM_RATA or
  old.STATO_ASS_COMPENSO  != new.STATO_ASS_COMPENSO or
  old.CD_CDS_COMPENSO     != new.CD_CDS_COMPENSO    or
	old.CD_UO_COMPENSO      != new.CD_UO_COMPENSO     or
	old.ESERCIZIO_COMPENSO  != new.ESERCIZIO_COMPENSO or
	old.PG_COMPENSO         != new.PG_COMPENSO) and
	new.PG_COMPENSO >0
      )
Declare
   aOldRowtype MINICARRIERA_RATA%rowtype;
   conta number:=1;
Begin
   --
   -- Trigger attivato su aggiornamento della tabella MINICARRIERA_RATA (Before)
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

     select count(0) into conta from minicarriera_rata_s
     where
       CD_CDS									= :old.CD_CDS  and
       CD_UNITA_ORGANIZZATIVA = :old.CD_UNITA_ORGANIZZATIVA  and
       ESERCIZIO							= :old.ESERCIZIO  and
       PG_MINICARRIERA				= :old.PG_MINICARRIERA and
       PG_RATA								= :old.PG_RATA;

   -- Scarico dello storico
   CNRSTO090.sto_MINICARRIERA_RATA(:old.PG_VER_REC+conta, aOldRowType);
End;
/


