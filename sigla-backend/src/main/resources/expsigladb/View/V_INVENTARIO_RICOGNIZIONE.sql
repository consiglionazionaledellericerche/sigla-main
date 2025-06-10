--------------------------------------------------------
--  DDL for View V_INVENTARIO_RICOGNIZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INVENTARIO_RICOGNIZIONE" ("CATEGORIA", "GRUPPO", "DS_CATEGORIA_GRUPPO", "UO", "PG_INVENTARIO", "NR_INVENTARIO", "PROGRESSIVO", "DS_BENE", "DATA_REGISTRAZIONE", "ETICHETTA", "ESERCIZIO_CARICO_BENE", "UBICAZIONE", "DS_UBICAZIONE", "ASSEGNATARIO", "DS_ASSEGNATARIO", "TOTALMENTE_SCARICATO", "VALORE", "DATA_SCARICO_DEF", "STATO") AS
  SELECT
				 cd_categoria_padre categoria,
				 INV.CD_categoria_gruppo gruppo,
			   ds_categoria_gruppo,
			   INV.cd_unita_organizzativa,
			   INV.pg_inventario,
			   INV.nr_inventario,
			   INV.progressivo,
				INV.ds_bene,
				data_registrazione,
				INV.etichetta,
				INV.esercizio_carico_bene,
				INV.cd_ubicazione ,
				DS_UBICAZIONE_BENE,
				inv.cd_assegnatario,
				terzo.denominazione_sede,
				inv.fl_totalmente_scaricato,
				SUM(DECODE(DETT.ti_documento,'C',valore_unitario,-VALORE_UNITARIO)) valore,
				decode(fl_totalmente_scaricato,'Y',(select max( buono_carico_scarico.data_registrazione)   from buono_carico_scarico,buono_carico_scarico_dett where
				buono_carico_scarico.esercizio = buono_carico_scarico_dett.esercizio and
 				buono_carico_scarico.pg_inventario = buono_carico_scarico_dett.pg_inventario and
 				buono_carico_scarico.ti_documento = buono_carico_scarico_dett.ti_documento and
 				buono_carico_scarico.pg_buono_c_s = buono_carico_scarico_dett.pg_buono_c_s and
 				buono_carico_scarico_dett.pg_inventario = INV.pg_inventario   AND
 				buono_carico_scarico_dett.progressivo = INV.progressivo   AND
 				buono_carico_scarico_dett.nr_inventario = INV.nr_inventario       And
 				buono_carico_scarico.ti_documento = 'S' ), null) data_scarico_def,
 				inv.stato
 FROM BUONO_CARICO_SCARICO_DETT dett  ,inventario_beni inv,BUONO_CARICO_SCARICO buono,ubicazione_bene,categoria_gruppo_invent ,terzo
 where
 buono.esercizio = dett.esercizio and
 buono.pg_inventario = dett.pg_inventario and
 buono.ti_documento = dett.ti_documento and
 buono.pg_buono_c_s = dett.pg_buono_c_s and
 DETT.pg_inventario = INV.pg_inventario   AND
 DETT.nr_inventario = INV.nr_inventario   AND
  DETT.progressivo = inv.progressivo and
 INV.CD_CDS = ubicazione_bene.CD_CDS and
 INV.cd_unita_organizzativa = ubicazione_bene.cd_unita_organizzativa and
 INV.cd_ubicazione = ubicazione_bene.cd_ubicazione and
 inv.cd_assegnatario= terzo.cd_terzo(+) and
 INV.cd_categoria_gruppo = categoria_gruppo_invent.cd_categoria_gruppo
 group by cd_categoria_padre,INV.CD_categoria_gruppo,ds_categoria_gruppo,INV.cd_unita_organizzativa,INV.pg_inventario,INV.nr_inventario,INV.progressivo,INV.ds_bene,data_registrazione,INV.etichetta,INV.esercizio_carico_bene,INV.cd_ubicazione,ds_ubicazione_bene,inv.cd_assegnatario,
				terzo.denominazione_sede,inv.fl_totalmente_scaricato,inv.stato
 order by INV.cd_unita_organizzativa,INV.pg_inventario,INV.nr_inventario,INV.progressivo,data_registrazione;
