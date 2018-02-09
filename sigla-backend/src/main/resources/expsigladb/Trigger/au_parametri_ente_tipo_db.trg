CREATE OR REPLACE TRIGGER AU_PARAMETRI_ENTE_TIPO_DB
AFTER UPDATE
OF TIPO_DB
ON PARAMETRI_ENTE 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
Declare
 Begin
--
-- Trigger attivato su aggiornamento della tabella PARAMETRI_ENTE
-- se il tipo_db ? diverso da P allora vengono cancellate le tabelle
-- print_spooler, report_generico, excel_spooler, utente_indirizzi_mail
--
-- Date: 10/06/2008
-- Version: 1.0
--
-- Dependency:
--
-- History:
--
-- Date: 10/06/2008
-- Version: 1.0
-- Creazione
--
-- Body:
--


 If :old.tipo_db = 'P' And  :New.tipo_db != 'P' Then
       
                        Delete From PRINT_SPOOLER;
                               
                        Delete From REPORT_GENERICO;
                
                        Delete From EXCEL_SPOOLER;
                
                        Delete From UTENTE_INDIRIZZI_MAIL;

                        Update UNITA_ORGANIZZATIVA_PEC SET EMAIL_PEC_PROTOCOLLO = NULL, COD_PEC_PROTOCOLLO = NULL;
                        
                        Update DOCUMENTO_ELE_TRASMISSIONE SET REPLY_TO = NULL WHERE REPLY_TO IS NOT NULL;
 End If;
End;
/


