--------------------------------------------------------
--  DDL for View V_LIMITE_SPESA_PDGP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIMITE_SPESA_PDGP" ("ESERCIZIO", "CD_CDS", "FONTE", "IMPORTO_LIMITE", "IMPORTO_PREVISTO", "IMPORTO_DISPONIBILE", "CD_CLASSIFICAZIONE", "ID_CLASSIFICAZIONE", "CD_AREA") AS 
  select  esercizio, cd_cds,
            fonte, importo_limite,
            sum(importo_previsto),
            importo_limite-sum(importo_previsto),
            cd_classificazione,
            id_classificazione,
            cd_area
            FROM v_limite_spesa_det_pdgp
  group BY esercizio, cd_cds, cd_classificazione,id_classificazione,fonte,cd_area,importo_limite
   union
  select  d.esercizio, d.cd_cds, d.fonte,d.importo_limite,d.impegni_assunti,
          d.importo_limite-d.impegni_assunti ,v.cd_classificazione,
          v.id_classificazione,d.cd_cds cd_area
      from limite_spesa_det d,
            elemento_voce e,
            v_classificazione_voci v
        WHERE e.esercizio = d.esercizio_voce
        AND e.ti_appartenenza = d.ti_appartenenza
        AND e.ti_gestione = d.ti_gestione
        AND e.cd_elemento_voce = d.cd_elemento_voce
        AND  v.id_classificazione = e.id_classificazione
        and not exists(select 1 from v_limite_spesa_det_pdgp p
           where
            p.esercizio =d.esercizio  and
            p.cd_cds    =d.cd_cds and
            p.cd_area = d.cd_cds  and
            p.id_classificazione =v.id_classificazione and
            p.fonte= d.fonte)
  GROUP BY d.esercizio,
            d.cd_cds,
            v.cd_classificazione,
            v.id_classificazione,
            d.fonte,
            d.cd_cds,
            d.importo_limite ,
            d.impegni_assunti;
