CREATE OR REPLACE TRIGGER BI_PDG_MODULO
AFTER INSERT
on PDG_MODULO
for each row
Declare
  STATO_PDG PDG_ESERCIZIO.STATO%Type;
Begin
  Begin
    Select STATO
    Into STATO_PDG
    From PDG_ESERCIZIO
    Where ESERCIZIO = :NEW.ESERCIZIO
    And   CD_CENTRO_RESPONSABILITA = :NEW.CD_CENTRO_RESPONSABILITA;
  Exception
    When Others Then Null;
  End;

  If STATO_PDG Is Not Null And STATO_PDG = 'CG' Then
    ibmerr001.RAISE_ERR_GENERICO('E'' stata effettuata per il PDGP la ''Chiusura Gestionale''. Non ? possibile aggiungere altri dettagli.');
  End If;
End;
/


