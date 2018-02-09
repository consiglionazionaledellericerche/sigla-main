CREATE OR REPLACE PROCEDURE PRT_S_SIT_CASSA_J
--
-- Date: 11/11/2004
-- Version: 1.2
--
-- Vista di stampa Situazione di Cassa Emesso/Inviato/Riscontrato
--
-- History:
--
-- Date: 28/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 06/08/2004
-- Version: 1.1
-- Rinominati il nome del package e della procedure che lo richiama
--
-- Date: 11/11/2004
-- Version: 1.2
-- sostituita "To_date(to_date" che a Crystal non piaceva
--
-- Body
--
(inEs           in number,
 CDS            in varchar2,
 uo             in varchar2,
 EM_INV_RIS     in varchar2,  -- E/I/R (E = Emesso, I = INVIATO, R = Riscontrato)
 DA_DATA        IN varchar2,
 A_DATA         IN VARCHAR2) is

Begin
  PRT_S_SIT_CASSA_ALL_J(inEs,CDS,uo,EM_INV_RIS,DA_DATA,A_DATA,null);
End;
/


