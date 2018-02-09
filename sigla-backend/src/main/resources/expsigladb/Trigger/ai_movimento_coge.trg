CREATE OR REPLACE TRIGGER AI_MOVIMENTO_COGE
  AFTER INSERT
  ON MOVIMENTO_COGE
  FOR EACH ROW
DISABLE
Declare
aCHIUSURA CHIUSURA_COEP%rowtype;
tipo   VARCHAR2(20);
Begin

aCHIUSURA := cnrctb200.getChiusuraCoep(:new.Esercizio, :new.Cd_Cds);


 IF aCHIUSURA.stato in ('C', 'P') Then

  If aCHIUSURA.stato = 'C' Then
    tipo := 'Definitivamente';
  Elsif  aCHIUSURA.stato = 'P' Then
    tipo := 'Provvisoriamente';
  End If;

  IBMERR001.RAISE_ERR_GENERICO('Il CDS '||:new.cd_cds||' risulta chiuso '||tipo||' in Economica. Inserimento non possibile.');
 END IF;
END;
/


