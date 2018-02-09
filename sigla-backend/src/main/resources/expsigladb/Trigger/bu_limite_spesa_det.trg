CREATE OR REPLACE TRIGGER BU_LIMITE_SPESA_DET
BEFORE UPDATE
on LIMITE_SPESA_DET
for each row
WHEN (
old.importo_limite!=new.importo_limite
      )
begin
-- Trigger attivato su aggiornamento della tabella LIMITE_SPESA_DET (Before)

 insert into limite_spesa_det_s(
  ESERCIZIO      ,
  CD_CDS         ,
  ESERCIZIO_VOCE ,
  TI_APPARTENENZA,
  TI_GESTIONE    ,
  CD_ELEMENTO_VOCE,
  FONTE          ,
  IMPORTO_LIMITE ,
  IMPEGNI_ASSUNTI,
  UTCR           ,
  DACR           ,
  UTUV           ,
  DUVA           ,
  PG_VER_REC     ,
  pg_storico) values
  (
  :old.ESERCIZIO      ,
  :old.CD_CDS         ,
  :old.ESERCIZIO_VOCE ,
  :old.TI_APPARTENENZA,
  :old.TI_GESTIONE    ,
  :old.CD_ELEMENTO_VOCE,
  :old.FONTE          ,
  :old.IMPORTO_LIMITE ,
  :old.IMPEGNI_ASSUNTI,
  :old.UTCR           ,
  :old.DACR           ,
  :old.UTUV           ,
  :old.DUVA           ,
  :old.PG_VER_REC     ,
  :new.PG_VER_REC);

end;
/


