--------------------------------------------------------
--  DDL for View V_VOCI_STIPENDIALI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_VOCI_STIPENDIALI" ("ESERCIZIO", "TI_PREV_CONS", "CD_ELEMENTO_VOCE", "TI_RAPPORTO") AS 
  (
 select distinct
 --
 -- Date: 30/01/2002
 -- Version: 1.1
 --
 -- Vista che estrae le voci di spesa e il tipo di rapporto dalla tabella dell'interfaccia stipendi COSTO_DEL_DIPENDENTE
 --
 -- History:
 --
 -- Date: 14/01/2002
 -- Version: 1.0
 -- Creazione
 --
 -- Date: 30/01/2002
 -- Version: 1.1
 -- Ritorna anche TFR e ONERI_CNR come qualsiasi altro conto esplicito
 --
 -- Body:
 --
  esercizio,
  ti_prev_cons,
  cd_elemento_voce,
  ti_rapporto
 from costo_del_dipendente
union
 select distinct
   a.esercizio,
   a.ti_prev_cons,
   b.VAL01,
   a.ti_rapporto
  from costo_del_dipendente a, configurazione_cnr b
  where
       b.esercizio = a.esercizio
   and b.cd_chiave_primaria = 'ELEMENTO_VOCE_SPECIALE'
   and b.cd_chiave_secondaria in ('TFR','ONERI_CNR')
);

   COMMENT ON TABLE "V_VOCI_STIPENDIALI"  IS 'Vista che estrae le voci di spesa e il tipo di rapporto dalla tabella dell''interfaccia stipendi COSTO_DEL_DIPENDENTE';
