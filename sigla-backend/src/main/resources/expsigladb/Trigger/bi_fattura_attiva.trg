CREATE OR REPLACE TRIGGER BI_FATTURA_ATTIVA
BEFORE Insert
On FATTURA_ATTIVA
For each row
Declare
  conta number :=0;
Begin
   --   
    If :new.pg_fattura_esterno is not null Then
                          Select count(*) into conta
                        from FATTURA_ATTIVA
                        Where ESERCIZIO                                 = :new.ESERCIZIO
                        And   CD_UNITA_ORGANIZZATIVA    = :new.CD_UNITA_ORGANIZZATIVA
                        and   pg_fattura_esterno        = :new.pg_fattura_esterno 
                        and   pg_fattura_attiva        != :new.pg_fattura_attiva;

                           if conta!= 0 then
                            ibmerr001.RAISE_ERR_GENERICO('Non ? possibile procedere con la creazione, Fattura o Nota Credito gi? inserita.');
              End If;
  end if;
end;
/


