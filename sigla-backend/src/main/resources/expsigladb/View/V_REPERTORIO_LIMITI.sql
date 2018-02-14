--------------------------------------------------------
--  DDL for View V_REPERTORIO_LIMITI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_REPERTORIO_LIMITI" ("ESERCIZIO_LIMITE", "CD_TIPO_LIMITE", "IMPORTO_LIMITE", "IMPORTO_UTILIZZATO", "IMPORTO_UTILIZZATO_CALCOLATO", "FL_RAGGIUNTO_LIMITE") AS 
  (SELECT repertorio_limiti.esercizio esercizio_limite,
           repertorio_limiti.cd_tipo_limite, repertorio_limiti.importo_limite,
           repertorio_limiti.importo_utilizzato,
           (SELECT NVL
                      (SUM (incarichi_procedura_anno.importo_complessivo),
                       0
                      )
              FROM incarichi_procedura, incarichi_procedura_anno
             WHERE incarichi_procedura.stato IN ('PP', 'PU', 'IN', 'PD', 'CC')
               AND incarichi_procedura.esercizio =
                                     incarichi_procedura_anno.esercizio
                                                                       -- JOIN
               AND incarichi_procedura.pg_procedura =
                                  incarichi_procedura_anno.pg_procedura
                                                                       -- JOIN
               AND incarichi_procedura_anno.esercizio_limite =
                                                   repertorio_limiti.esercizio
               AND (incarichi_procedura.cd_tipo_incarico,
                    incarichi_procedura.cd_tipo_attivita,
                    incarichi_procedura.tipo_natura
                   ) IN (
                      SELECT DISTINCT e.cd_tipo_incarico, e.cd_tipo_attivita,
                                      e.tipo_natura
                                 FROM ass_incarico_attivita e
                                WHERE e.stato = 'V'
                                  AND e.cd_tipo_limite =
                                              repertorio_limiti.cd_tipo_limite))
                                                 importo_utilizzato_calcolato,
           repertorio_limiti.fl_raggiunto_limite
      FROM repertorio_limiti) ;
