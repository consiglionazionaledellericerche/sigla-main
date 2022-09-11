--------------------------------------------------------
--  DDL for View CNR_IFAC_DETT_OBBLIGAZIONI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "CNR_IFAC_DETT_OBBLIGAZIONI" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "IM_VOCE", "DACR", "UTCR", "DUVA", "UTUV", "DESCRIZIONE", "NOTE", "RIPORTATO", "ESERCIZIO_ORI_ORI_RIPORTO", "PG_OBBLIGAZIONE_ORI_RIPORTO", "PG_DOCAMM", "FORNITORE") AS 
  SELECT obb.cd_cds, obb.cd_unita_organizzativa, osv.esercizio,
          osv.esercizio_originale, osv.pg_obbligazione,
          osv.pg_obbligazione_scadenzario, osv.ti_appartenenza,
          osv.ti_gestione, osv.cd_voce, osv.cd_centro_responsabilita,
          osv.cd_linea_attivita, osv.im_voce, osv.dacr, osv.utcr, osv.duva,
          osv.utuv, obb.ds_obbligazione, obb.note_obbligazione, obb.riportato,
          obb.esercizio_ori_ori_riporto, obb.pg_obbligazione_ori_riporto,
          nvl(doc.pg_documento_amm,0), trz.denominazione_sede
     FROM obbligazione_scad_voce osv,
          obbligazione obb,
          terzo trz,
          v_doc_passivo_obbligazione doc
    WHERE obb.pg_obbligazione = osv.pg_obbligazione
      AND obb.cd_cds = osv.cd_cds
      AND obb.esercizio = osv.esercizio
      AND obb.esercizio_originale = osv.esercizio_originale
      AND osv.cd_cds = doc.cd_cds_obbligazione(+)
      AND osv.esercizio = doc.esercizio_obbligazione(+)
      AND osv.esercizio_originale = doc.esercizio_ori_obbligazione(+)
      AND osv.pg_obbligazione = doc.pg_obbligazione(+)
      AND osv.pg_obbligazione_scadenzario = doc.pg_obbligazione_scadenzario(+)
      AND obb.cd_terzo = trz.cd_terzo
      and doc.cd_tipo_documento_amm != 'ORDINE';
