--------------------------------------------------------
--  DDL for View V_ELENCO_INVENTARIO_BENI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ELENCO_INVENTARIO_BENI" ("CD_UNITA_ORGANIZZATIVA", "CD_CATEGORIA_GRUPPO", "NR_INVENTARIO", "PROGRESSIVO", "DS_BENE", "DATA_REGISTRAZIONE", "VALORE_INIZIALE", "VARIAZIONE_MENO", "VARIAZIONE_PIU", "VALORE_FINALE", "ETICHETTA", "TARGA") AS 
  Select c.CD_UNITA_ORGANIZZATIVA,
       c.CD_CATEGORIA_GRUPPO,
       c.NR_INVENTARIO,
       c.PROGRESSIVO,
       c.DS_BENE,
      (Select a.DATA_REGISTRAZIONE
       From buono_carico_scarico a, buono_carico_scarico_dett b
       Where a.ESERCIZIO     = b.ESERCIZIO
         And a.PG_INVENTARIO = b.PG_INVENTARIO
         And a.TI_DOCUMENTO  = b.TI_DOCUMENTO
         And a.PG_BUONO_C_S  = b.PG_BUONO_C_S
         And b.PG_INVENTARIO = c.PG_INVENTARIO
         And b.NR_INVENTARIO = c.NR_INVENTARIO
         And b.PROGRESSIVO   = c.PROGRESSIVO
         And Rownum = 1 ) DATA_REGISTRAZIONE,
       c.VALORE_INIZIALE,
       c.VARIAZIONE_MENO,
       c.VARIAZIONE_PIU,
       Nvl(c.VALORE_INIZIALE,0)+Nvl(c.VARIAZIONE_PIU,0)-Nvl(c.VARIAZIONE_MENO,0)  VALORE_FINALE,
       C.ETICHETTA,c.targa
From inventario_beni c
Order By c.CD_UNITA_ORGANIZZATIVA, c.CD_CATEGORIA_GRUPPO, c.NR_INVENTARIO
;
