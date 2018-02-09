CREATE OR REPLACE TRIGGER AU_CDR
AFTER UPDATE  
on CDR  
for each row
declare  
aCDR cdr%rowtype;  
begin  
--  
-- Trigger attivato su aggiornamento di CDR  
--  
-- Date: 08/11/2001  
-- Version: 1.1  
--  
-- Dependency: IBMERR001  
--  
-- History:  
--  
-- Date: 01/06/2001  
-- Version: 1.0
-- Creazione 
-- Date: 08/11/2001  
-- Version: 1.1  
-- Eliminazione esercizio da STO  
--  
-- Body:  
--  
-- Check che non cambi la PK  
if  
:old.cd_centro_responsabilita!=:new.cd_centro_responsabilita  
then  
IBMERR001.raise_no_change_pk('Unita Organizzativa');  
end if;  
-- Check che non cambi l'unita organizzativa  
if  
:old.cd_unita_organizzativa!=:new.cd_unita_organizzativa  
or :old.cd_proprio_cdr!=:new.cd_proprio_cdr  
or :old.cd_cdr_afferenza!=:new.cd_cdr_afferenza  
or :old.livello!=:new.livello  
then  
IBMERR001.raise_no_change('Alcune caratteristiche del CDR non sono modificabili');  
end if;  
end;
/


