--------------------------------------------------------
--  DDL for View PRT_STAMPA_REGISTRO_INV_QUOTE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_REGISTRO_INV_QUOTE" ("CD_UNITA_ORGANIZZATIVA", "CD_CDS", "ESERCIZIO_CARICO_BENE", "ETICHETTA", "CATEGORIA", "CD_CATEGORIA_GRUPPO", "CD_ASSEGNATARIO", "DATA_REGISTRAZIONE", "CD_TIPO_CARICO_SCARICO", "DS_TIPO_CARICO_SCARICO", "VALORE_AMMORTIZZATO", "IMPONIBILE_AMMORTAMENTO", "ESERCIZIO_AMM", "NUMERO_ANNI", "PERC_AMMORTAMENTO", "PERC_PRIMO_ANNO", "PERC_SUCCESSIVI", "NR_INVENTARIO", "FL_AMMORTAMENTO", "TIPO", "ESERCIZIO_COMP") AS 
  SELECT INVENTARIO_BENI.cd_unita_organizzativa, INVENTARIO_BENI.cd_cds,
       INVENTARIO_BENI.esercizio_carico_bene, INVENTARIO_BENI.etichetta,
       cd_categoria_padre categoria,
       INVENTARIO_BENI.cd_categoria_gruppo,
       INVENTARIO_BENI.cd_assegnatario,
       BUONO_CARICO_SCARICO.data_registrazione,
       TIPO_CARICO_SCARICO.cd_tipo_carico_scarico,
       TIPO_CARICO_SCARICO.ds_tipo_carico_scarico,
       DECODE(BUONO_CARICO_SCARICO.ti_documento,'C',amm.IM_MOVIMENTO_AMMORT,(-1)*amm.IM_MOVIMENTO_AMMORT) IM_MOVIMENTO_AMMORT,
       amm.imponibile_ammortamento 	imponibile_ammortamento,
       amm.ESERCIZIO esercizio_amm,
       (SELECT numero_anni
          FROM AMMORTAMENTO_BENE_INV
         WHERE INVENTARIO_BENI.pg_inventario =
                              AMMORTAMENTO_BENE_INV.pg_inventario
           AND INVENTARIO_BENI.nr_inventario =
                                           AMMORTAMENTO_BENE_INV.nr_inventario
           AND INVENTARIO_BENI.progressivo = AMMORTAMENTO_BENE_INV.progressivo
           AND AMMORTAMENTO_BENE_INV.numero_anno = 1
           and rownum =1) numero_anni,
       (SELECT perc_ammortamento
          FROM AMMORTAMENTO_BENE_INV
         WHERE INVENTARIO_BENI.pg_inventario =
                        AMMORTAMENTO_BENE_INV.pg_inventario
           AND INVENTARIO_BENI.nr_inventario =
                                           AMMORTAMENTO_BENE_INV.nr_inventario
           AND INVENTARIO_BENI.progressivo = AMMORTAMENTO_BENE_INV.progressivo
           AND AMMORTAMENTO_BENE_INV.numero_anno = 1
           and rownum =1) perc_ammortamento,
       (SELECT perc_primo_anno
          FROM AMMORTAMENTO_BENE_INV
         WHERE INVENTARIO_BENI.pg_inventario =
                          AMMORTAMENTO_BENE_INV.pg_inventario
           AND INVENTARIO_BENI.nr_inventario =
                                           AMMORTAMENTO_BENE_INV.nr_inventario
           AND INVENTARIO_BENI.progressivo = AMMORTAMENTO_BENE_INV.progressivo
           AND AMMORTAMENTO_BENE_INV.numero_anno = 1
           and rownum =1) perc_primo_anno,
       (SELECT perc_successivi
          FROM AMMORTAMENTO_BENE_INV
         WHERE INVENTARIO_BENI.pg_inventario =
                          AMMORTAMENTO_BENE_INV.pg_inventario
           AND INVENTARIO_BENI.nr_inventario =
                                           AMMORTAMENTO_BENE_INV.nr_inventario
           AND INVENTARIO_BENI.progressivo = AMMORTAMENTO_BENE_INV.progressivo
           AND AMMORTAMENTO_BENE_INV.numero_anno = 1
           and rownum =1) perc_successivi,
       INVENTARIO_BENI.nr_inventario,INVENTARIO_BENI.FL_ammortamento,
       INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE,
       amm.esercizio_competenza
  FROM INVENTARIO_BENI,
       UBICAZIONE_BENE,
       CATEGORIA_GRUPPO_INVENT,
       BUONO_CARICO_SCARICO_DETT,
       TERZO,
       BUONO_CARICO_SCARICO,
       TIPO_CARICO_SCARICO,
       AMMORTAMENTO_BENE_INV amm
 WHERE INVENTARIO_BENI.cd_cds = UBICAZIONE_BENE.cd_cds
   AND INVENTARIO_BENI.cd_unita_organizzativa =
                                        UBICAZIONE_BENE.cd_unita_organizzativa
   AND INVENTARIO_BENI.cd_ubicazione = UBICAZIONE_BENE.cd_ubicazione
   AND INVENTARIO_BENI.cd_categoria_gruppo =
                                   CATEGORIA_GRUPPO_INVENT.cd_categoria_gruppo
   AND INVENTARIO_BENI.cd_unita_organizzativa = TERZO.cd_unita_organizzativa
   And Terzo.cd_terzo in( select min(t.cd_terzo) from terzo t
   		 where
	 	 t.cd_unita_organizzativa = Inventario_Beni.Cd_Unita_Organizzativa
	 	 and t.dt_fine_rapporto is null)
   AND BUONO_CARICO_SCARICO_DETT.pg_inventario = INVENTARIO_BENI.pg_inventario
   AND BUONO_CARICO_SCARICO_DETT.nr_inventario = INVENTARIO_BENI.nr_inventario
   AND BUONO_CARICO_SCARICO_DETT.progressivo = INVENTARIO_BENI.progressivo
   AND BUONO_CARICO_SCARICO.pg_inventario =
                                       BUONO_CARICO_SCARICO_DETT.pg_inventario
   AND BUONO_CARICO_SCARICO.ti_documento =
                                        BUONO_CARICO_SCARICO_DETT.ti_documento
   AND BUONO_CARICO_SCARICO.ESERCIZIO = BUONO_CARICO_SCARICO_DETT.ESERCIZIO
   AND BUONO_CARICO_SCARICO.pg_buono_c_s =
                                        BUONO_CARICO_SCARICO_DETT.pg_buono_c_s
   AND TIPO_CARICO_SCARICO.cd_tipo_carico_scarico =
                                   BUONO_CARICO_SCARICO.cd_tipo_carico_scarico
   AND(( TIPO_CARICO_SCARICO.ti_documento = 'C'
   AND TIPO_CARICO_SCARICO.FL_AUMENTO_VALORE = 'N') OR
    (TIPO_CARICO_SCARICO.ti_documento = 'S'   AND
     INVENTARIO_BENI.fl_totalmente_scaricato='Y' ))
   AND INVENTARIO_BENI.pg_inventario =  amm.pg_inventario
   AND INVENTARIO_BENI.nr_inventario =	amm.nr_inventario
   AND INVENTARIO_BENI.progressivo  = 	amm.progressivo
      AND (BUONO_CARICO_SCARICO.pg_inventario,BUONO_CARICO_SCARICO.ti_documento,BUONO_CARICO_SCARICO.ESERCIZIO,BUONO_CARICO_SCARICO.pg_buono_c_s) IN
  ((SELECT dett.pg_inventario,dett.ti_documento,dett.ESERCIZIO,MAX(dett.pg_buono_c_s) FROM BUONO_CARICO_SCARICO_DETT dett
   WHERE
       dett.pg_inventario  = INVENTARIO_BENI.pg_inventario
   AND dett.nr_inventario  = INVENTARIO_BENI.nr_inventario
   AND dett.progressivo    = INVENTARIO_BENI.progressivo
  AND BUONO_CARICO_SCARICO.pg_inventario =
                                       dett.pg_inventario
   AND BUONO_CARICO_SCARICO.ti_documento =
                                        dett.ti_documento
   AND BUONO_CARICO_SCARICO.ESERCIZIO = dett.ESERCIZIO
   AND BUONO_CARICO_SCARICO.pg_buono_c_s =
                                        dett.pg_buono_c_s
   AND TIPO_CARICO_SCARICO.cd_tipo_carico_scarico =
                                   BUONO_CARICO_SCARICO.cd_tipo_carico_scarico
   AND ( TIPO_CARICO_SCARICO.ti_documento = 'C'
   AND TIPO_CARICO_SCARICO.FL_AUMENTO_VALORE = 'N')
     GROUP BY dett.pg_inventario,dett.ti_documento,dett.ESERCIZIO)
     UNION
    (SELECT dett.pg_inventario,dett.ti_documento,dett.ESERCIZIO,MAX(dett.pg_buono_c_s) FROM BUONO_CARICO_SCARICO_DETT dett
   WHERE
       dett.pg_inventario  = INVENTARIO_BENI.pg_inventario
   AND dett.nr_inventario  = INVENTARIO_BENI.nr_inventario
   AND dett.progressivo    = INVENTARIO_BENI.progressivo
   AND  dett.ti_documento = 'S'   AND
       INVENTARIO_BENI.fl_totalmente_scaricato='Y' AND
  NOT EXISTS(SELECT 1 FROM BUONO_CARICO_SCARICO_DETT dettagli WHERE
        dettagli.ESERCIZIO > BUONO_CARICO_SCARICO_DETT.ESERCIZIO AND
	dettagli.ti_documento ='S' AND
       BUONO_CARICO_SCARICO_DETT.pg_inventario  = dettagli.pg_inventario
   AND BUONO_CARICO_SCARICO_DETT.nr_inventario  = dettagli.nr_inventario
   AND BUONO_CARICO_SCARICO_DETT.progressivo    = dettagli.progressivo)
   GROUP BY dett.pg_inventario,dett.ti_documento,dett.ESERCIZIO))
 UNION
 SELECT PRT_STAMPA_REGISTRO_INVENTARIO.cd_unita_organizzativa, PRT_STAMPA_REGISTRO_INVENTARIO.cd_cds,
       PRT_STAMPA_REGISTRO_INVENTARIO.esercizio_carico_bene,NULL,
       PRT_STAMPA_REGISTRO_INVENTARIO.categoria,
       PRT_STAMPA_REGISTRO_INVENTARIO.cd_categoria_gruppo,
       NULL,
       NULL,
       PRT_STAMPA_REGISTRO_INVENTARIO.cd_tipo_carico_scarico,
       NULL, 0, NULL,
       9999,
       NULL,
       NULL,
       NULL,
       NULL,
       9999999999,NULL,
       PRT_STAMPA_REGISTRO_INVENTARIO.TIPO,
       9999
  FROM PRT_STAMPA_REGISTRO_INVENTARIO
  UNION
  SELECT INVENTARIO_BENI.cd_unita_organizzativa, INVENTARIO_BENI.cd_cds,
       INVENTARIO_BENI.esercizio_carico_bene, INVENTARIO_BENI.etichetta,
       cd_categoria_padre  categoria,
       INVENTARIO_BENI.cd_categoria_gruppo,
       INVENTARIO_BENI.cd_assegnatario,
       BUONO_CARICO_SCARICO.data_registrazione,
       TIPO_CARICO_SCARICO.cd_tipo_carico_scarico,
       TIPO_CARICO_SCARICO.ds_tipo_carico_scarico,
       DECODE(BUONO_CARICO_SCARICO.ti_documento,'C',ass.valore_ammortizzato,(-1)*ass.valore_ammortizzato) IM_MOVIMENTO_AMMORT,
       ass.imponibile_ammortamento 	imponibile_ammortamento,
       INVENTARIO_BENI.esercizio_carico_bene esercizio_amm,
       NULL,
       NULL,
       NULL,
       NULL,
       INVENTARIO_BENI.nr_inventario,INVENTARIO_BENI.FL_ammortamento,
       INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE,
       (INVENTARIO_BENI.esercizio_carico_bene-1) esercizio_comp
       FROM
       INVENTARIO_BENI,
       UBICAZIONE_BENE,
       CATEGORIA_GRUPPO_INVENT,
       BUONO_CARICO_SCARICO_DETT,
       TERZO,
       BUONO_CARICO_SCARICO,
       TIPO_CARICO_SCARICO,
       ASS_TRASFERIMENTO_BENI_INV ass
       WHERE
       INVENTARIO_BENI.cd_cds 			= UBICAZIONE_BENE.cd_cds			AND
       INVENTARIO_BENI.cd_unita_organizzativa 	= UBICAZIONE_BENE.cd_unita_organizzativa	AND
       INVENTARIO_BENI.cd_ubicazione            = UBICAZIONE_BENE.cd_ubicazione			AND
       INVENTARIO_BENI.cd_categoria_gruppo      = CATEGORIA_GRUPPO_INVENT.cd_categoria_gruppo   AND
       INVENTARIO_BENI.cd_unita_organizzativa   = TERZO.cd_unita_organizzativa                  AND
       Terzo.cd_terzo in( select min(t.cd_terzo) from terzo t
   		 where
	 	   t.cd_unita_organizzativa = Inventario_Beni.Cd_Unita_Organizzativa
	 	 	 and t.dt_fine_rapporto is null)  and
       BUONO_CARICO_SCARICO_DETT.pg_inventario  = INVENTARIO_BENI.pg_inventario			AND
       BUONO_CARICO_SCARICO_DETT.nr_inventario  = INVENTARIO_BENI.nr_inventario			AND
       BUONO_CARICO_SCARICO_DETT.progressivo    = INVENTARIO_BENI.progressivo 			AND
       BUONO_CARICO_SCARICO.pg_inventario       = BUONO_CARICO_SCARICO_DETT.pg_inventario	AND
       BUONO_CARICO_SCARICO.ti_documento 	= BUONO_CARICO_SCARICO_DETT.ti_documento	AND
       BUONO_CARICO_SCARICO.ESERCIZIO 		= BUONO_CARICO_SCARICO_DETT.ESERCIZIO		AND
       BUONO_CARICO_SCARICO.pg_buono_c_s        = BUONO_CARICO_SCARICO_DETT.pg_buono_c_s	AND
       TIPO_CARICO_SCARICO.cd_tipo_carico_scarico = BUONO_CARICO_SCARICO.cd_tipo_carico_scarico AND
       TIPO_CARICO_SCARICO.ti_documento         = 'C'	AND
       TIPO_CARICO_SCARICO.FL_AUMENTO_VALORE 	= 'N'	AND
     (fl_totalmente_scaricato='N'  OR
      EXISTS(SELECT 1 FROM BUONO_CARICO_SCARICO_DETT dett WHERE
 	dett.pg_inventario = BUONO_CARICO_SCARICO_DETT.pg_inventario 	AND
        dett.nr_inventario = BUONO_CARICO_SCARICO_DETT.nr_inventario	AND
 	dett.progressivo   = BUONO_CARICO_SCARICO_DETT.progressivo      AND
 	dett.ti_documento  = 'S'					AND
 	dett.ESERCIZIO     > BUONO_CARICO_SCARICO_DETT.ESERCIZIO))      AND
  INVENTARIO_BENI.pg_inventario =  ass.pg_inventario_dest		AND
  INVENTARIO_BENI.nr_inventario =  ass.nr_inventario_dest		AND
  INVENTARIO_BENI.progressivo   =  ass.progressivo_dest;
