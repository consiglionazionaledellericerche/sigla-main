--------------------------------------------------------
--  DDL for View V_CNR_ABICAB
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CNR_ABICAB" ("ABI", "CAB", "DS_ABICAB", "VIA", "DS_COMUNE", "CAP", "CD_PROVINCIA", "FRAZIONE", "DT_ATTIVAZIONE", "DT_CESSAZIONE", "TIPO", "FL_CANCELLATO") AS 
  select
 	cab.ABI 			ABI,
 	cab.CAB 			CAB,
 	abi.DS_ABI || decode (abi.DS_ABI,NULL,cab.DS_CAB,decode(cab.DS_CAB,null,null,' - ' || cab.DS_CAB) ) DS_ABICAB,
 	cab.INDIRIZZO      		VIA,
 	cab.COMUNE	   		DS_COMUNE,
 	cab.CAP	   			CAP,
 	cab.PROVINCIA 			CD_PROVINCIA,
 	cab.FRAZIONE  			FRAZIONE,
 	cab.DT_ATTIVAZIONE 		DT_ATTIVAZIONE,
 	cab.DT_CESSAZIONE  		DT_CESSAZIONE,
 	cab.TIPO 			TIPO,
 	decode(cab.DT_CESSAZIONE,null,'N','Y') FL_CANCELLATO
 from abi, cab
 where abi.ABI = cab.ABI
;
