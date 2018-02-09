CREATE OR REPLACE TRIGGER BU_SCRITTURA_PARTITA_DOPPIA
  BEFORE UPDATE OF
CD_CDS                 ,
ESERCIZIO              ,
CD_UNITA_ORGANIZZATIVA ,
PG_SCRITTURA           ,
ORIGINE_SCRITTURA      ,
CD_CAUSALE_COGE        ,
CD_TIPO_DOCUMENTO      ,
PG_NUMERO_DOCUMENTO    ,
CD_COMP_DOCUMENTO      ,
IM_SCRITTURA           ,
TI_SCRITTURA           ,
DT_CANCELLAZIONE       ,
PG_SCRITTURA_ANNULLATA ,
ATTIVA                 ,
ESERCIZIO_DOCUMENTO_AMM,
CD_CDS_DOCUMENTO       ,
CD_UO_DOCUMENTO
  ON SCRITTURA_PARTITA_DOPPIA
  FOR EACH ROW
DECLARE
aCHIUSURA CHIUSURA_COEP%ROWTYPE;
tipo   VARCHAR2(20);
BEGIN

aCHIUSURA := Cnrctb200.getChiusuraCoep(:OLD.ESERCIZIO, :OLD.Cd_Cds);


 IF aCHIUSURA.stato IN ('C', 'P') THEN

  IF aCHIUSURA.stato = 'C' THEN
    tipo := 'definitivamente';
  ELSIF  aCHIUSURA.stato = 'P' THEN
    tipo := 'provvisoriamente';
  END IF;

  Ibmerr001.RAISE_ERR_GENERICO('Il CDS '||:OLD.cd_cds||' risulta chiuso '||tipo||' in Economica nell''esercizio '||:OLD.ESERCIZIO||'. Modifica testata scrittura non possibile.');
 END IF;
END;
/


