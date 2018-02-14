--------------------------------------------------------
--  DDL for View V_IMPEGNI_INVENTARIO_DETT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_IMPEGNI_INVENTARIO_DETT" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_DOCUMENTO", "PROGRESSIVO_RIGA", "TIPO", "IM_IMPONIBILE", "IM_IVA", "PG_INVENTARIO", "NR_INVENTARIO", "PROGRESSIVO", "VALORE_UNITARIO", "TI_DOCUMENTO", "PG_BUONO_C_S", "ESERCIZIO_IMP", "CDS_IMP", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "IM_SCADENZA") AS 
  select passiva.cd_cds,passiva.cd_unita_organizzativa,passiva.esercizio,passiva.pg_fattura_passiva,passiva.progressivo_riga,Decode(ti_Fattura,'F','Fattura Passiva','C','Nota Credito da fattura','Nota Debito da Fattura') tipo,
       passiva.im_imponibile,passiva.im_iva,
       buono.pg_inventario,buono.nr_inventario,buono.progressivo,buono.valore_unitario,Decode(buono.ti_documento,'C','Carico','Scarico') ti_documento,buono.pg_buono_c_s,
       scad.ESERCIZIO,scad.cd_cds,scad.esercizio_originale,scad.pg_obbligazione,scad.pg_obbligazione_scadenzario,scad.im_scadenza
from
ass_inv_bene_fattura ass,
buono_Carico_scarico_Dett buono,
fattura_passiva_riga passiva,
fattura_passiva,
obbligazione_scadenzario scad
Where
     passiva.stato_cofi !='A' and
     ASS.PG_INVENTARIO = BUONO.PG_INVENTARIO AND
     ASS.NR_INVENTARIO = BUONO.NR_INVENTARIO AND
     ASS.PROGRESSIVO   = BUONO.PROGRESSIVO   AND
     ASS.TI_DOCUMENTO  = BUONO.TI_DOCUMENTO  AND
     ASS.ESERCIZIO     = BUONO.ESERCIZIO     AND
     ASS.PG_BUONO_C_S  = BUONO.PG_BUONO_C_S  AND
     ass.esercizio_fatt_pass = passiva.esercizio and
     ass.cd_cds_fatt_pass = passiva.cd_cds and
     ass.cd_uo_fatt_pass = passiva.cd_unita_organizzativa and
     ass.pg_fattura_passiva = passiva.pg_fattura_passiva And
     ass.progressivo_riga_fatt_pass = passiva.progressivo_riga And
     fattura_passiva.esercizio = passiva.esercizio and
     fattura_passiva.cd_cds = passiva.cd_cds and
     fattura_passiva.cd_unita_organizzativa = passiva.cd_unita_organizzativa and
     fattura_passiva.pg_fattura_passiva = passiva.pg_fattura_passiva And
     passiva.esercizio_obbligazione 	= scad.ESERCIZIO	    AND
     passiva.cd_cds_obbligazione	= scad.cd_cds		    AND
     passiva.esercizio_ori_obbligazione = scad.esercizio_originale  AND
     passiva.pg_obbligazione 		= scad.pg_obbligazione	    AND
     passiva.pg_obbligazione_scadenzario= scad.pg_obbligazione_scadenzario
union
select gen.cd_cds,gen.cd_unita_organizzativa,gen.esercizio,gen.pg_documento_generico,gen.progressivo_riga,Tipo_documento_amm.ds_tipo_documento_amm tipo,
       gen.im_riga,0 im_iva,
       buono.pg_inventario,buono.nr_inventario,buono.progressivo,buono.valore_unitario,Decode(buono.ti_documento,'C','Carico','Scarico') ti_documento,buono.pg_buono_c_s,
       scad.ESERCIZIO,scad.cd_cds,scad.esercizio_originale,scad.pg_obbligazione,scad.pg_obbligazione_scadenzario,scad.im_scadenza
from
ass_inv_bene_fattura ass,
buono_Carico_scarico_Dett buono,
documento_generico_riga gen,
obbligazione_scadenzario scad,
tipo_documento_amm
Where
     gen.stato_cofi !='A' and
     tipo_documento_amm.cd_tipo_documento_amm = ASS.cd_tipo_documento_amm and
     ASS.PG_INVENTARIO = BUONO.PG_INVENTARIO AND
     ASS.NR_INVENTARIO = BUONO.NR_INVENTARIO AND
     ASS.PROGRESSIVO   = BUONO.PROGRESSIVO   AND
     ASS.TI_DOCUMENTO  = BUONO.TI_DOCUMENTO  AND
     ASS.ESERCIZIO     = BUONO.ESERCIZIO     AND
     ASS.PG_BUONO_C_S  = BUONO.PG_BUONO_C_S  AND
     ass.esercizio_doc_gen = gen.esercizio and
     ass.cd_cds_doc_gen = gen.cd_cds and
     ass.cd_uo_doc_gen = gen.cd_unita_organizzativa and
     ass.pg_documento_generico = gen.pg_documento_generico and
     ass.progressivo_riga_doc_gen = gen.progressivo_riga and
     ass.cd_tipo_documento_amm = gen.cd_tipo_documento_amm and
     gen.esercizio_obbligazione 	= scad.ESERCIZIO	    AND
     gen.cd_cds_obbligazione	= scad.cd_cds		    AND
     gen.esercizio_ori_obbligazione = scad.esercizio_originale  AND
     gen.pg_obbligazione 		= scad.pg_obbligazione	    AND
     gen.pg_obbligazione_scadenzario= scad.pg_obbligazione_scadenzario;
