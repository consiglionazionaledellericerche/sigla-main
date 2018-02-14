CREATE OR REPLACE TRIGGER BU_PDG_ESERCIZIO_BLOCCO_TEMP
BEFORE UPDATE
OF STATO
ON PDG_ESERCIZIO 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DISABLE
Declare
Begin
--
-- Trigger temporaneo
--
--

  If :New.STATO = 'CG' Then
    IBMERR001.raise_err_generico('Funzione non disponibile');
  End If;
end;
/


