--------------------------------------------------------
--  DDL for View PRT_STAMPA_REGISTRO_INVENTARIO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_REGISTRO_INVENTARIO" ("CD_UNITA_ORGANIZZATIVA", "CD_CDS", "ESERCIZIO_CARICO_BENE", "ETICHETTA", "DS_BENE", "VALORE_INIZIALE", "CATEGORIA", "CD_CATEGORIA_GRUPPO", "DS_CATEGORIA", "DS_CATEGORIA_GRUPPO", "DS_UBICAZIONE_BENE", "CD_ASSEGNATARIO", "DENOMINAZIONE_SEDE", "DATA_REGISTRAZIONE", "CD_TIPO_CARICO_SCARICO", "DS_TIPO_CARICO_SCARICO", "VARIAZIONE_PIU", "VARIAZIONE_MENO", "QUANTITA", "VALORE_UNITARIO", "IMPONIBILE_AMMORTAMENTO", "VALORE_AMMORTIZZATO", "NUMERO_ANNI", "PERC_AMMORTAMENTO", "PERC_PRIMO_ANNO", "PERC_SUCCESSIVI", "NR_INVENTARIO", "TIPO", "FL_AMMORTAMENTO", "PROGRESSIVO") AS 
  SELECT inventario_beni.cd_unita_organizzativa, inventario_beni.cd_cds,
       inventario_beni.esercizio_carico_bene, inventario_beni.etichetta,
       inventario_beni.ds_bene,
       Decode (buono_carico_scarico_dett.ti_documento,'C',
              Decode(tipo_carico_scarico.FL_AUMENTO_VALORE,'N',buono_carico_scarico_dett.valore_unitario,0),0)valore_iniziale,
       categoria_gruppo_invent2.cd_categoria_padre categoria,
       inventario_beni.cd_categoria_gruppo,
       categoria_gruppo_invent.ds_categoria_gruppo,
       categoria_gruppo_invent2.ds_categoria_gruppo,
       ubicazione_bene.ds_ubicazione_bene, inventario_beni.cd_assegnatario,
       terzo.denominazione_sede, buono_carico_scarico.data_registrazione,
       tipo_carico_scarico.cd_tipo_carico_scarico,
       tipo_carico_scarico.ds_tipo_carico_scarico,
       Decode (buono_carico_scarico_dett.ti_documento,'C',
       	       Decode(tipo_carico_scarico.FL_AUMENTO_VALORE,'Y',buono_carico_scarico_dett.valore_unitario,0),0) variazione_piu,
       Decode (buono_carico_scarico_dett.ti_documento,'S',buono_carico_scarico_dett.valore_unitario,0) variazione_meno,
       buono_carico_scarico_dett.quantita,
       buono_carico_scarico_dett.valore_unitario,
       imponibile_ammortamento,
       valore_ammortizzato,
       0 numero_anni,
       0 perc_ammortamento,
       0 perc_primo_anno,
       0 perc_successivi,
       inventario_beni.nr_inventario,
       INVENTARIO_BENI.TI_COMMERCIALE_ISTITUZIONALE,inventario_beni.FL_AMMORTAMENTO,
       inventario_beni.progressivo
  FROM inventario_beni,
       ubicazione_bene,
       categoria_gruppo_invent,
       categoria_gruppo_invent categoria_gruppo_invent2,
       buono_carico_scarico_dett,
       terzo,
       buono_carico_scarico,
       tipo_carico_scarico
 WHERE inventario_beni.cd_cds = ubicazione_bene.cd_cds
   AND inventario_beni.cd_unita_organizzativa =
                                        ubicazione_bene.cd_unita_organizzativa
   AND inventario_beni.cd_ubicazione = ubicazione_bene.cd_ubicazione
   AND categoria_gruppo_invent2.cd_categoria_padre =
                                   categoria_gruppo_invent.cd_categoria_gruppo
   And inventario_beni.cd_categoria_gruppo =
                                   categoria_gruppo_invent2.cd_categoria_gruppo
   AND inventario_beni.cd_unita_organizzativa = terzo.cd_unita_organizzativa
   And
      Terzo.cd_terzo in( select min(t.cd_terzo) from terzo t
   	 where
	 	 t.cd_unita_organizzativa = Inventario_Beni.Cd_Unita_Organizzativa
	 	 and t.dt_fine_rapporto is null)
   AND buono_carico_scarico_dett.pg_inventario = inventario_beni.pg_inventario
   AND buono_carico_scarico_dett.nr_inventario = inventario_beni.nr_inventario
   AND buono_carico_scarico_dett.progressivo = inventario_beni.progressivo
   AND buono_carico_scarico.pg_inventario =
                                       buono_carico_scarico_dett.pg_inventario
   AND buono_carico_scarico.ti_documento =
                                        buono_carico_scarico_dett.ti_documento
   AND buono_carico_scarico.esercizio = buono_carico_scarico_dett.esercizio
   AND buono_carico_scarico.pg_buono_c_s =
                                        buono_carico_scarico_dett.pg_buono_c_s
   AND tipo_carico_scarico.cd_tipo_carico_scarico =
                                   buono_carico_scarico.cd_tipo_carico_scarico;
