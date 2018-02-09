CREATE OR REPLACE TRIGGER AI_ASS_LINEA_ATTIVITA_ESER
AFTER INSERT
ON ASS_LINEA_ATTIVITA_ESERCIZIO for each row
Declare
  recLinea linea_attivita%rowtype;
begin
  If :new.esercizio = 2015 and :new.pg_progetto is not null Then
    SELECT * into recLinea
    FROM linea_attivita a
    Where a.cd_centro_responsabilita = :new.cd_centro_responsabilita
    And   a.cd_linea_attivita = :new.cd_linea_attivita;

    If recLinea.pg_progetto is null Then
      Update linea_attivita
      set pg_progetto = :new.pg_progetto
      Where cd_centro_responsabilita = recLinea.cd_centro_responsabilita
      And   cd_linea_attivita = recLinea.cd_linea_attivita;
      
      if sql%rowcount>1 Then
        ibmerr001.RAISE_ERR_GENERICO('Errore in fase di aggiornamento progetto sulla linea di attivita''.');      
      End If;
    End If;
  End If;
End;
/


