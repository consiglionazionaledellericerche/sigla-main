--------------------------------------------------------
--  DDL for View VS_DIPENDENZE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VS_DIPENDENZE" ("TIPO", "OWNER", "ORIGINE", "OWNER_REFERENZIATO", "TIPO_REFERENZIATO", "OGGETTO_REFERENZIATO") AS 
  select distinct  
--
-- Estrattore di dipendenze: entrare con tipo VIEW PACKAGE o PACKAGE BODY TABLE
--
-- Version: 1.0
-- Date: 09/10/2001
--
-- History:
--
-- Body:
--
type,  
owner,  
name,  
referenced_owner,  
referenced_type,  
referenced_name  
from  
all_dependencies  
where  
(referenced_name  <> 'DUAL')  
and (referenced_owner <> 'SYS')  
and ( referenced_type<>'NON-EXISTENT')  
and ( referenced_type<>'SYNONYM');

   COMMENT ON TABLE "VS_DIPENDENZE"  IS 'Estrattore di dipendenze: entrare con tipo VIEW PACKAGE o PACKAGE BODY TABLE';
