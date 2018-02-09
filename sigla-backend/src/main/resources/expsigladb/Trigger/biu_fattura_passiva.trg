CREATE OR REPLACE TRIGGER BIU_FATTURA_PASSIVA
  BEFORE Insert Or Update Of DT_FATTURA_FORNITORE
  on FATTURA_PASSIVA
  for each row
DISABLE
begin
--
-- Trigger di disattivazione per gestione Splyt Payment
--
    If :new.DT_FATTURA_FORNITORE is not null and to_char(:new.DT_FATTURA_FORNITORE,'yyyymmdd')>='20170701' Then
       IBMERR001.RAISE_ERR_GENERICO('La registrazione di documenti di data uguale o successiva al 01 Luglio 2017 e'' temporaneamente sospesa in attesa '||
       'del rilascio degli aggiornamenti necessari alla gestione dello Split Payment.');
    End If;
End;
/


