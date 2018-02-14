--------------------------------------------------------
--  DDL for View V_BFRAME_CLUSTER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_BFRAME_CLUSTER" ("CLUSTER_URL", "APPSERVER_URL", "WEIGHT", "LOAD", "TOT_WEIGHT", "TOT_LOAD") AS 
  select
--
-- Date: 14/11/2002
-- Version: 1.1
--
-- Vista pesi/carico per server e complessivi per cluster dispatcher
--
-- History:
--
-- Date: 08/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 14/11/2002
-- Version: 1.1
-- Solo i server con weight > 0 vengono considerati nell'algoritmo di bilanciamento
--
-- Body:
--
  a.CLUSTER_URL,
  a.APPSERVER_URL,
  a.WEIGHT,
  a.SESSION_COUNTER,
  sum(nvl(b.weight,0)),
  sum(nvl(b.session_counter,0))
from bframe_cluster a,bframe_cluster b where
     a.isActive='Y'
 and a.weight > 0
 and a.cluster_url=b.cluster_url
 and b.weight > 0
 and b.isActive='Y'
group by
  a.CLUSTER_URL,
  a.APPSERVER_URL,
  a.WEIGHT,
  a.SESSION_COUNTER
;

   COMMENT ON TABLE "V_BFRAME_CLUSTER"  IS 'Vista pesi/carico per server e complessivi per cluster dispatcher';
