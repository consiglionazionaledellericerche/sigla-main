--------------------------------------------------------
--  DDL for View V_INVENTARIO_PER_CONTO
--------------------------------------------------------
CREATE OR REPLACE FORCE VIEW v_inventario_per_conto (esercizio,
cd_unita_organizzativa,
cd_voce_ep,
ds_voce_ep,
contabilizzazione,
dare,
avere,
saldo
)
AS
SELECT   dett.esercizio, beni.cd_unita_organizzativa,
         categoria_gruppo_voce_ep.cd_voce_ep, ds_voce_ep,
         DECODE (stato_coge,
         'N', 'Da Inventario',
         'C', 'Da Inventario',
         'Da Documento'
         ) contabilizzazione,
         SUM (DECODE (dett.ti_documento, 'C', valore_unitario, 0)) dare,
         SUM (DECODE (dett.ti_documento, 'S', valore_unitario, 0)) avere,
         SUM (DECODE (dett.ti_documento,
         'C', valore_unitario,
         -valore_unitario
         )
         ) saldo
FROM inventario_beni beni,
buono_carico_scarico_dett dett,
categoria_gruppo_voce_ep,
voce_ep,
parametri_cnr
WHERE SUBSTR (beni.cd_categoria_gruppo,
1,
INSTR (beni.cd_categoria_gruppo, '.') - 1
) = categoria_gruppo_voce_ep.cd_categoria_gruppo
AND categoria_gruppo_voce_ep.sezione = 'A'
AND categoria_gruppo_voce_ep.esercizio = dett.esercizio
AND dett.pg_inventario = beni.pg_inventario
AND dett.nr_inventario = beni.nr_inventario
AND dett.progressivo = beni.progressivo
AND voce_ep.esercizio = dett.esercizio
AND voce_ep.cd_voce_ep = categoria_gruppo_voce_ep.cd_voce_ep
AND parametri_cnr.esercizio = dett.esercizio
AND parametri_cnr.fl_nuovo_pdg = 'N'
GROUP BY dett.esercizio,
categoria_gruppo_voce_ep.cd_voce_ep,
beni.cd_unita_organizzativa,
ds_voce_ep,
DECODE (stato_coge,
'N', 'Da Inventario',
'C', 'Da Inventario',
'Da Documento'
)
UNION
SELECT   dett.esercizio, beni.cd_unita_organizzativa,
         categoria_gruppo_voce_ep.cd_voce_ep, ds_voce_ep,
         DECODE (stato_coge,
         'N', 'Da Inventario',
         'C', 'Da Inventario',
         'Da Documento'
         ) contabilizzazione,
         SUM (DECODE (dett.ti_documento, 'C', valore_unitario, 0)) dare,
         SUM (DECODE (dett.ti_documento, 'S', valore_unitario, 0)) avere,
         SUM (DECODE (dett.ti_documento,
         'C', valore_unitario,
         -valore_unitario
         )
         ) saldo
FROM inventario_beni beni,
buono_carico_scarico_dett dett,
categoria_gruppo_voce_ep,
voce_ep,
parametri_cnr
WHERE dett.stato_coge != 'X'
AND beni.cd_categoria_gruppo =
categoria_gruppo_voce_ep.cd_categoria_gruppo
AND categoria_gruppo_voce_ep.fl_default = 'Y'
AND categoria_gruppo_voce_ep.esercizio = dett.esercizio
AND categoria_gruppo_voce_ep.sezione = 'A'
AND dett.pg_inventario = beni.pg_inventario
AND dett.nr_inventario = beni.nr_inventario
AND dett.progressivo = beni.progressivo
AND voce_ep.esercizio = dett.esercizio
AND voce_ep.cd_voce_ep = categoria_gruppo_voce_ep.cd_voce_ep
AND parametri_cnr.esercizio = dett.esercizio
AND parametri_cnr.fl_nuovo_pdg = 'Y'
GROUP BY dett.esercizio,
categoria_gruppo_voce_ep.cd_voce_ep,
beni.cd_unita_organizzativa,
ds_voce_ep,
DECODE (stato_coge,
'N', 'Da Inventario',
'C', 'Da Inventario',
'Da Documento'
)
UNION
SELECT   dett.esercizio, beni.cd_unita_organizzativa,
         categoria_gruppo_voce_ep.cd_voce_ep, ds_voce_ep,
         DECODE (stato_coge,
         'N', 'Da Inventario',
         'C', 'Da Inventario',
         'Da Documento'
         ) contabilizzazione,
         SUM (DECODE (dett.ti_documento, 'C', dett.valore_unitario, 0)
         ) dare,
         SUM (DECODE (dett.ti_documento, 'S', dett.valore_unitario, 0)
         ) avere,
         SUM (DECODE (dett.ti_documento,
         'C', dett.valore_unitario,
         -dett.valore_unitario
         )
         ) saldo
FROM inventario_beni beni,
buono_carico_scarico_dett dett,
categoria_gruppo_voce_ep,
voce_ep,
parametri_cnr
WHERE ((dett.esercizio,
             DECODE (dett.ti_documento, 'C', 'Carico', 'Scarico'),
dett.pg_inventario,
dett.nr_inventario,
dett.pg_buono_c_s,
dett.progressivo
) IN (
 SELECT v_impegni_inventario_dett.esercizio,
        v_impegni_inventario_dett.ti_documento,
        v_impegni_inventario_dett.pg_inventario,
        v_impegni_inventario_dett.nr_inventario,
        v_impegni_inventario_dett.pg_buono_c_s,
        v_impegni_inventario_dett.progressivo
 FROM v_impegni_inventario_dett, obbligazione
 WHERE obbligazione.cd_cds = v_impegni_inventario_dett.cds_imp
 AND obbligazione.esercizio =
 v_impegni_inventario_dett.esercizio_imp
 AND obbligazione.pg_obbligazione =
 v_impegni_inventario_dett.pg_obbligazione
 AND obbligazione.esercizio_originale =
 v_impegni_inventario_dett.esercizio_originale
 AND categoria_gruppo_voce_ep.cd_elemento_voce =
 obbligazione.cd_elemento_voce)
or (dett.esercizio,
dett.ti_documento,
dett.pg_inventario,
dett.nr_inventario,
dett.pg_buono_c_s,
dett.progressivo
)  IN (
 SELECT ass.esercizio, ass.ti_documento, ass.pg_inventario,
        ass.nr_inventario, ass.pg_buono_c_s, ass.progressivo
 FROM ass_inv_bene_fattura ass))
AND dett.stato_coge = 'X'
AND beni.cd_categoria_gruppo =
categoria_gruppo_voce_ep.cd_categoria_gruppo
AND categoria_gruppo_voce_ep.ti_gestione = 'S'
AND categoria_gruppo_voce_ep.esercizio = dett.esercizio
AND categoria_gruppo_voce_ep.sezione = 'A'
AND dett.pg_inventario = beni.pg_inventario
AND dett.nr_inventario = beni.nr_inventario
AND dett.progressivo = beni.progressivo
AND voce_ep.esercizio = dett.esercizio
AND voce_ep.cd_voce_ep = categoria_gruppo_voce_ep.cd_voce_ep
AND parametri_cnr.esercizio = dett.esercizio
AND parametri_cnr.fl_nuovo_pdg = 'Y'
GROUP BY dett.esercizio,
categoria_gruppo_voce_ep.cd_voce_ep,
beni.cd_unita_organizzativa,
ds_voce_ep,
DECODE (stato_coge,
'N', 'Da Inventario',
'C', 'Da Inventario',
'Da Documento'
)
UNION
SELECT   dett.esercizio, beni.cd_unita_organizzativa,
         categoria_gruppo_voce_ep.cd_voce_ep, ds_voce_ep,
         DECODE (stato_coge,
         'N', 'Da Inventario',
         'C', 'Da Inventario',
         'Da Documento non coll.'
         ) contabilizzazione,
         SUM (DECODE (dett.ti_documento, 'C', dett.valore_unitario, 0)
         ) dare,
         SUM (DECODE (dett.ti_documento, 'S', dett.valore_unitario, 0)
         ) avere,
         SUM (DECODE (dett.ti_documento,
         'C', dett.valore_unitario,
         -dett.valore_unitario
         )
         ) saldo
FROM inventario_beni beni,
buono_carico_scarico_dett dett,
categoria_gruppo_voce_ep,
voce_ep,
parametri_cnr
WHERE (dett.esercizio,
dett.ti_documento,
dett.pg_inventario,
dett.nr_inventario,
dett.pg_buono_c_s,
dett.progressivo
) NOT IN (
 SELECT ass.esercizio, ass.ti_documento, ass.pg_inventario,
        ass.nr_inventario, ass.pg_buono_c_s, ass.progressivo
 FROM ass_inv_bene_fattura ass)
AND dett.stato_coge = 'X'
AND beni.cd_categoria_gruppo =
categoria_gruppo_voce_ep.cd_categoria_gruppo
AND categoria_gruppo_voce_ep.ti_gestione = 'S'
AND categoria_gruppo_voce_ep.esercizio = dett.esercizio
AND categoria_gruppo_voce_ep.sezione = 'A'
AND categoria_gruppo_voce_ep.fl_default = 'Y'
AND dett.pg_inventario = beni.pg_inventario
AND dett.nr_inventario = beni.nr_inventario
AND dett.progressivo = beni.progressivo
AND voce_ep.esercizio = dett.esercizio
AND voce_ep.cd_voce_ep = categoria_gruppo_voce_ep.cd_voce_ep
AND parametri_cnr.esercizio = dett.esercizio
AND parametri_cnr.fl_nuovo_pdg = 'Y'
GROUP BY dett.esercizio,
categoria_gruppo_voce_ep.cd_voce_ep,
beni.cd_unita_organizzativa,
ds_voce_ep,
DECODE (stato_coge,
'N', 'Da Inventario',
'C', 'Da Inventario',
'Da Documento non coll.'
);

