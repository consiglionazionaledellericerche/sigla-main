CREATE OR REPLACE TRIGGER aiu_obbligazione_scadenzario
AFTER INSERT OR UPDATE OF IM_SCADENZA, IM_ASSOCIATO_DOC_AMM, IM_ASSOCIATO_DOC_CONTABILE
 ON OBBLIGAZIONE_SCADENZARIO REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DISABLE
begin
  IF :new.CD_CDS!='999' Then
    IF NVL(:new.IM_ASSOCIATO_DOC_AMM,0)>0 and NVL(:new.IM_SCADENZA,0)!=NVL(:new.IM_ASSOCIATO_DOC_AMM,0) Then
       IBMERR001.RAISE_ERR_GENERICO('Collegamento parziale ad obbligazione di un documento amministrativo non possibile. Aggiornamento scadenza nr.'||
       :new.PG_OBBLIGAZIONE_SCADENZARIO||' dell''obbligazione '||:new.ESERCIZIO||'/'||:NEW.ESERCIZIO_ORIGINALE||'/'||:NEW.CD_CDS||'/'||:NEW.PG_OBBLIGAZIONE||
       ' non possibile.');
    End If;     

    IF NVL(:new.IM_ASSOCIATO_DOC_CONTABILE,0)>0 and NVL(:new.IM_SCADENZA,0)!=NVL(:new.IM_ASSOCIATO_DOC_CONTABILE,0) Then
       IBMERR001.RAISE_ERR_GENERICO('Collegamento parziale ad obbligazione di un documento contabile non possibile. Aggiornamento scadenza nr.'||
       :new.PG_OBBLIGAZIONE_SCADENZARIO||' dell''obbligazione '||:new.ESERCIZIO||'/'||:NEW.ESERCIZIO_ORIGINALE||'/'||:NEW.CD_CDS||'/'||:NEW.PG_OBBLIGAZIONE||
       ' non possibile.');
    End If;     
  End If;     
end;
/


