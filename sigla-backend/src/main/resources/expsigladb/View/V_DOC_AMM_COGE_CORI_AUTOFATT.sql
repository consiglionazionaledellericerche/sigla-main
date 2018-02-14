--------------------------------------------------------
--  DDL for View V_DOC_AMM_COGE_CORI_AUTOFATT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_COGE_CORI_AUTOFATT" ("CD_TIPO_DOCUMENTO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_NUMERO_DOCUMENTO", "CD_TERZO", "CD_TERZO_CORI", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "CD_CONTRIBUTO_RITENUTA", "TI_ENTE_PERCEPIENTE", "TI_ISTITUZ_COMMERC", "AMMONTARE", "FL_PGIRO", "ESERCIZIO_EV", "TI_APPARTENENZA_EV", "TI_GESTIONE_EV", "CD_ELEMENTO_VOCE_EV", "STATO_COGE_DOCAMM", "STATO_COGE_DOCCONT") AS 
  (SELECT
-- Estrazione IVA fattura passiva
--
-- Date: 15/07/2003
-- Version: 1.8
--
-- Vista di estrazione dei dettagli cori di documento amministrativo con valorizzazioni a fini COGE
--
-- History:
--
-- Date: 15/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 16/05/2002
-- Version: 1.1
-- L'iva commerciale intra ue/san marino non deve essere considerata in economica
--
-- Date: 10/06/2002
-- Version: 1.2
-- Sistemata estrazione acc/obb su CORI compenso
--
-- Date: 11/06/2002
-- Version: 1.3
-- Fix errore estrazione CORI Fattura attiva e passiva
--
-- Date: 31/10/2002
-- Version: 1.4
-- Estrazione del fl_pgiro del primo documento collegato a fattura attiva e passiva per gestione in deroga dei cori
-- Richiesta CINECA 30/10/2002
--
-- Date: 13/11/2002
-- Version: 1.5
-- Estratto il terzo relativo al CORI oltre che a quello del documento, perchè può essere diverso da quello del documento
--
-- Date: 28/11/2002
-- Version: 1.6
-- Aggiunto il tipo istituzionale/commerciale
--
-- Date: 03/12/2002
-- Version: 1.7
-- Iva istituzionale filtrata
--
-- Date: 15/07/2003
-- Version: 1.8
-- L'iva commerciale viene esclusa solo quando esiste l'autofattura
--
-- Body:
--
             'FATTURA_P', a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
             a.pg_fattura_passiva, a.cd_terzo, a.cd_terzo,
             a.dt_da_competenza_coge, a.dt_a_competenza_coge, f.val01, 'E',
             b.ti_istituz_commerc, SUM (b.im_iva),
             DECODE (b.pg_obbligazione, NULL, d.fl_pgiro, c.fl_pgiro),
             DECODE (b.pg_obbligazione, NULL, d.esercizio, c.esercizio),
             DECODE (b.pg_obbligazione,
                     NULL, d.ti_appartenenza,
                     c.ti_appartenenza
                    ),
             DECODE (b.pg_obbligazione, NULL, d.ti_gestione, c.ti_gestione),
             DECODE (b.pg_obbligazione,
                     NULL, d.cd_elemento_voce,
                     c.cd_elemento_voce
                    ),
             	DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    					,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
        FROM fattura_passiva a,
             fattura_passiva_riga b,
             obbligazione c,
             accertamento d,
             configurazione_cnr f,
             voce_iva
       WHERE b.cd_cds = a.cd_cds
         AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
         AND b.esercizio = a.esercizio
         AND b.pg_fattura_passiva = a.pg_fattura_passiva
         AND f.cd_chiave_primaria = 'CORI_SPECIALE'
         AND f.cd_chiave_secondaria = 'IVA'
         AND f.esercizio = 0
         AND f.cd_unita_funzionale = '*'
         AND b.im_iva <> 0
         -- Iva istituzionale va a costo
         AND b.ti_istituz_commerc = 'C'
         and voce_iva.cd_voce_iva = b.cd_voce_iva
         and (voce_iva.fl_autofattura ='Y' or
         	EXISTS (
                SELECT 1
                  FROM autofattura
                 WHERE esercizio = a.esercizio
                   AND cd_cds_ft_passiva = a.cd_cds
                   AND cd_uo_ft_passiva = a.cd_unita_organizzativa
                   AND pg_fattura_passiva = a.pg_fattura_passiva))
         AND c.esercizio(+) = b.esercizio_obbligazione
         AND c.cd_cds(+) = b.cd_cds_obbligazione
         AND c.pg_obbligazione(+) = b.pg_obbligazione
         AND d.esercizio(+) = b.esercizio_accertamento
         AND d.cd_cds(+) = b.cd_cds_accertamento
         AND d.pg_accertamento(+) = b.pg_accertamento
    GROUP BY a.cd_cds,
             a.cd_unita_organizzativa,
             a.esercizio,
             a.pg_fattura_passiva,
             a.cd_terzo,
             a.cd_terzo,
             a.dt_da_competenza_coge,
             a.dt_a_competenza_coge,
             f.val01,
             DECODE (b.ti_istituz_commerc, 'I', 'P', 'E'),
             b.ti_istituz_commerc,
             DECODE (b.pg_obbligazione, NULL, d.fl_pgiro, c.fl_pgiro),
             DECODE (b.pg_obbligazione, NULL, d.esercizio, c.esercizio),
             DECODE (b.pg_obbligazione,
                     NULL, d.ti_appartenenza,
                     c.ti_appartenenza
                    ),
             DECODE (b.pg_obbligazione, NULL, d.ti_gestione, c.ti_gestione),
             DECODE (b.pg_obbligazione,
                     NULL, d.cd_elemento_voce,
                     c.cd_elemento_voce
                    ),
                    DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    								,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
                    );
