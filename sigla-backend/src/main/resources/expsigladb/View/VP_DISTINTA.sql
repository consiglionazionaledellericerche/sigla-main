--------------------------------------------------------
--  DDL for View VP_DISTINTA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DISTINTA" ("TIPO_DOC", "ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "PG_DISTINTA", "PG_DISTINTA_DEF", "DT_INVIO", "IM_TOT_DOC", "IM_TOT_RIT", "PG_DOC", "TI_DOC", "DS_DOC", "DT_EMISSIONE", "DT_ANNULLAMENTO", "DT_RITRASMISSIONE", "STATO", "PG_DISTINTA_PREC", "IM_DOC", "IM_RITENUTE", "CODICE_FISCALE", "DS_ANAGRAFICA", "ABI", "CAB", "NUMERO_CONTO", "IBAN", "FL_FLUSSO", "UO_DOCCONT") AS 
  SELECT
--
-- Date: 07/05/2008
-- Version: 2.0
--
-- Vista di estrazione dei dati per la stampa della distinta cassiere mandati/reversali
-- insiemi di linee di attivita
--
-- History:
--
-- Date: 09/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/01/2003
-- Version: 1.1
-- Estrazione ds_unita_organizzativa per il cds beneficiario
-- per mandati di accreditamento CNR - CdS
--
-- Date: 27/01/2003
-- Version: 1.2
-- Estrazione del pg_distinta_def (e non più pg_distinta)
--
-- Date: 27/01/2003
-- Version: 1.3
-- Estrazione di entrambi i pg
--
-- Date: 31/03/2003
-- Version: 1.4
-- Uso della function GETSTATODOCCONT per gestione ristampa distinte
-- Segnalazione n. 559
--
-- Date: 17/04/2003
-- Version: 1.5
-- Estrazione dt_annullamento, e progressivo distinta precedente
-- in cui il documento era stato inserito
--
-- Date: 17/04/2003
-- Version: 1.6
-- Modificata gestione stato: annullato A o non annullato NA
--
-- Date: 08/11/2006
-- Version: 1.7 (AD)
-- Aggiunto dati banca uo (Abi, Cab, Numero Conto)
--
-- Date: 28/06/2007
-- Version: 1.8
-- Modificata la condizione  and dcp.pg_distinta (+)= dc.pg_distinta-1 con
-- and dcp.pg_distinta_def(+)= dc.pg_distinta_def-1
--
-- Date: 22/10/2007
-- Version: 1.9
-- Modificato il campo DS_ANAGRAFICA, il dato viene preso dalla tabella
-- del TERZO anzichè dalla ANAGRAFICA
--
-- Date: 07/05/2008
-- Version: 2.0
-- Modificato il campo IM_TOT_DOC, è stato aggiunto nel calcolo anche
-- IM_REV_INI_RIT della tabella DISTINTA_CASSIERE
--
--
-- Body:
--
          'M', dc.esercizio, dc.cd_cds, dc.cd_unita_organizzativa,
          uo.ds_unita_organizzativa, dc.pg_distinta, dc.pg_distinta_def,
          dc.dt_invio,
          NVL (dcp.im_man_ini_sos + dcp.im_man_ini_acc + dcp.im_man_ini_pag,
               0),
          NVL (dcp.im_rev_ini_rit, 0), m.pg_mandato, m.ti_mandato,
          m.ds_mandato, TRUNC (m.dt_emissione), TRUNC (m.dt_annullamento),
          TRUNC (m.dt_ritrasmissione),
          SUBSTR (getstatodoccont (dc.cd_cds,
                                   dc.esercizio,
                                   dc.cd_unita_organizzativa,
                                   dc.pg_distinta,
                                   'M',
                                    dcd.cd_cds_origine,
                                   m.pg_mandato,
                                   m.dt_annullamento,
                                   m.dt_trasmissione
                                  ),
                  1,
                  2
                 ),
          getpgdistintaprec (dc.cd_cds,
                             dc.esercizio,
                             dc.cd_unita_organizzativa,
                             dc.pg_distinta,
                             'M',
                              dcd.cd_cds_origine,
                             m.pg_mandato
                            ),
          m.im_mandato, m.im_ritenute, a.codice_fiscale,
          DECODE (m.ti_mandato,
                  'A', uot.ds_unita_organizzativa,
                  LTRIM (RTRIM (t.denominazione_sede))
                 ),
          b.abi, b.cab, b.numero_conto, b.codice_iban,dc.fl_flusso,m.cd_unita_organizzativa
     FROM distinta_cassiere dc,
          distinta_cassiere dcp,
          unita_organizzativa uo,
          distinta_cassiere_det dcd,
          mandato m,
          mandato_terzo mt,
          terzo t,
          anagrafico a,
          unita_organizzativa uot,
          terzo tuo,
          banca b,
          parametri_cnr
    WHERE uo.cd_unita_organizzativa = dc.cd_unita_organizzativa
      AND dcd.esercizio = dc.esercizio
      AND dcd.cd_cds = dc.cd_cds
      AND dcd.cd_unita_organizzativa = dc.cd_unita_organizzativa
      AND dcd.pg_distinta = dc.pg_distinta
      AND dcp.esercizio(+) = dc.esercizio
      AND dcp.cd_cds(+) = dc.cd_cds
      AND dcp.cd_unita_organizzativa(+) = dc.cd_unita_organizzativa
      AND dcp.pg_distinta_def(+) = dc.pg_distinta_def - 1
      AND m.esercizio = dcd.esercizio
      AND m.cd_cds = dcd.cd_cds_origine
      AND m.pg_mandato = dcd.pg_mandato
      AND mt.esercizio = m.esercizio
      AND mt.cd_cds = m.cd_cds
      AND mt.pg_mandato = m.pg_mandato
      AND t.cd_terzo = mt.cd_terzo
      AND a.cd_anag = t.cd_anag
      AND uot.cd_unita_organizzativa(+) =
                                       SUBSTR (t.cd_unita_organizzativa, 1, 3)
      AND uot.fl_cds(+) = 'Y'
      AND uo.cd_unita_organizzativa = tuo.cd_unita_organizzativa
      AND tuo.cd_terzo = b.cd_terzo
        and parametri_cnr.esercizio =m.esercizio and
      (( b.FL_CC_CDS                 = 'Y' and
         parametri_cnr.fl_tesoreria_unica ='N' ) or
         (parametri_cnr.fl_tesoreria_unica ='Y' and
         exists(select 1 from configurazione_cnr where
         (configurazione_cnr.esercizio =0 or
         configurazione_cnr.esercizio =m.esercizio) and
         cd_chiave_primaria ='CONTO_CORRENTE_SPECIALE' and
         cd_chiave_secondaria='ENTE' and
         b.abi =val01 and
         b.cab =val02 and
         b.numero_conto like '%'||val03 ))
         AND b.fl_cancellato = 'N')
   UNION ALL
   SELECT 'R', dc.esercizio, dc.cd_cds, dc.cd_unita_organizzativa,
          uo.ds_unita_organizzativa, dc.pg_distinta, dc.pg_distinta_def,
          dc.dt_invio,
          NVL (dcp.im_rev_ini_sos + dcp.im_rev_ini_tra + dcp.im_rev_ini_rit,
               0),
          0, r.pg_reversale, r.ti_reversale, r.ds_reversale,
          TRUNC (r.dt_emissione), TRUNC (r.dt_annullamento),
          TRUNC (r.dt_ritrasmissione),
          SUBSTR (getstatodoccont (dc.cd_cds,
                                   dc.esercizio,
                                   dc.cd_unita_organizzativa,
                                   dc.pg_distinta,
                                   'R',
                                   dcd.cd_cds_origine,
                                   r.pg_reversale,
                                   r.dt_annullamento,
                                   r.dt_trasmissione
                                  ),
                  1,
                  2
                 ),
          getpgdistintaprec (dc.cd_cds,
                             dc.esercizio,
                             dc.cd_unita_organizzativa,
                             dc.pg_distinta,
                             'R',
                              dcd.cd_cds_origine,
                             r.pg_reversale
                            ),
          r.im_reversale, 0, a.codice_fiscale,
          LTRIM (RTRIM (t.denominazione_sede)), b.abi, b.cab, b.numero_conto,
          b.codice_iban,dc.fl_flusso,r.cd_unita_organizzativa
     FROM distinta_cassiere dc,
          distinta_cassiere dcp,
          unita_organizzativa uo,
          distinta_cassiere_det dcd,
          reversale r,
          reversale_terzo rt,
          terzo t,
          anagrafico a,
          terzo tuo,
          banca b,
          parametri_cnr
    WHERE uo.cd_unita_organizzativa = dc.cd_unita_organizzativa
      AND dcd.esercizio = dc.esercizio
      AND dcd.cd_cds = dc.cd_cds
      AND dcd.cd_unita_organizzativa = dc.cd_unita_organizzativa
      AND dcd.pg_distinta = dc.pg_distinta
      AND dcp.esercizio(+) = dc.esercizio
      AND dcp.cd_cds(+) = dc.cd_cds
      AND dcp.cd_unita_organizzativa(+) = dc.cd_unita_organizzativa
      AND dcp.pg_distinta_def(+) = dc.pg_distinta_def - 1
      AND r.esercizio = dcd.esercizio
      AND r.cd_cds = dcd.cd_cds_origine
      AND r.pg_reversale = dcd.pg_reversale
      AND rt.esercizio = r.esercizio
      AND rt.cd_cds = r.cd_cds
      AND rt.pg_reversale = r.pg_reversale
      AND t.cd_terzo = rt.cd_terzo
      AND a.cd_anag = t.cd_anag
      AND uo.cd_unita_organizzativa = tuo.cd_unita_organizzativa
      AND tuo.cd_terzo = b.cd_terzo
      and parametri_cnr.esercizio =r.esercizio and
      (( b.FL_CC_CDS                 = 'Y' and
         parametri_cnr.fl_tesoreria_unica ='N' ) or
         (parametri_cnr.fl_tesoreria_unica ='Y' and
         exists(select 1 from configurazione_cnr where
         (configurazione_cnr.esercizio =0 or
         configurazione_cnr.esercizio =r.esercizio) and
         cd_chiave_primaria ='CONTO_CORRENTE_SPECIALE' and
         cd_chiave_secondaria='ENTE' and
         b.abi =val01 and
         b.cab =val02 and
         b.numero_conto like '%'||val03 ))
         AND b.fl_cancellato = 'N');
