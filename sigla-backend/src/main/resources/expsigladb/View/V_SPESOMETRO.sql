--------------------------------------------------------
--  DDL for View V_SPESOMETRO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SPESOMETRO" ("QUADRO", "TIPO", "ESERCIZIO", "PARTITA_IVA", "CODICE_FISCALE", "TI_BENE_SERVIZIO", "TIPO_FISCALITA", "MESE", "NR_ATTIVE", "NR_PASSIVE", "IMPONIBILE_FA", "IVA_FA", "IMPONIBILE_ND_FA", "IVA_ND_FA", "IMPONIBILE_FP", "IVA_FP", "IMPONIBILE_ND_FP", "IVA_ND_FP", "COGNOME", "NOME", "DT_NASCITA", "STATO_NASCITA", "PROVINCIA", "CODICE_STATO_ESTERO", "RAGIONE_SOCIALE", "INDIRIZZO_SEDE", "COMUNE_SEDE", "PROG") AS 
  SELECT  quadro,tipo,esercizio, partita_iva,codice_fiscale, TI_BENE_SERVIZIO,tipo_fiscalita,mese, nr_attive,
      						  nr_passive, imponibile_fa, iva_fa,
      						  imponibile_nd_fa, iva_nd_fa,
      						  imponibile_fp, iva_fp,
      						  imponibile_nd_fp, iva_nd_fp,
                    cognome,
                    nome,
                    dt_nascita,
                    stato_nascita,
                    provincia,
                    stato_residenza,
                    ragione_sociale,
                    indirizzo_sede,
                    comune_sede, ROWNUM
     FROM (SELECT  quadro,tipo,esercizio, partita_iva,codice_fiscale, TI_BENE_SERVIZIO,tipo_fiscalita,mese, SUM (nr_attive) nr_attive,
      						  SUM (nr_passive) nr_passive,round(sum(imponibile_fa),0) imponibile_fa,round(sum(iva_fa),0) iva_fa,
      						  round(sum(imponibile_nd_fa),0) imponibile_nd_fa,round(sum(iva_nd_fa),0) iva_nd_fa,
      						  round(sum(imponibile_fp),0) imponibile_fp,round(sum(iva_fp),0) iva_fp,
      						  ROUND(sum(imponibile_nd_fp),0) imponibile_nd_fp,round(sum(iva_nd_fp),0) iva_nd_fp,
                    cognome,
                    nome,
                    dt_nascita,
                    stato_nascita,
                    provincia,
                    stato_residenza,
                    ragione_sociale,
                    indirizzo_sede,
                    comune_sede
               FROM (
              -- QUADRO FA
						  -- OPERAZIONI ATTIVE RESIDENTI----
                    SELECT   'FA' quadro,'ATTIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              ltrim(NVL (f.partita_iva, a.partita_iva)) partita_iva,
                              ltrim(NVL (f.codice_fiscale, a.codice_fiscale)) codice_fiscale,null TI_BENE_SERVIZIO,null tipo_fiscalita,null mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  ) imponibile_fa,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_iva,
                                           fr.im_iva
                                          )
                                  ) iva_fa,
                              0 imponibile_nd_fa,
                              0 iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              null cognome,
                              null nome,
                              null dt_nascita,
                              null stato_nascita,
                              null provincia,
                              null stato_residenza,
                              null ragione_sociale,
                              null indirizzo_sede,
                              null comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND f.ti_fattura in('F','C')     and
                              F.FL_INTRA_UE = 'N'  AND
                              F.FL_EXTRA_UE ='N'   AND
                              f.stato_cofi != 'A' and
                              a.ti_italiano_estero NOT IN ('E', 'C', 'M')
                          AND f.protocollo_iva IS NOT NULL
                          AND v.cd_voce_iva IN ('ED','ES','N2','N3','N4','N5','N8','4%', '10%','21%','22%')
                          and (ltrim(a.partita_iva) is not null or ltrim(a.codice_fiscale) is not null)
                        GROUP BY 'FA','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             NVL (f.codice_fiscale, a.codice_fiscale)
                     UNION ALL
                   -- OPERAZIONI ATTIVE note debito RESIDENTI----
                    SELECT    'FA' quadro,'ATTIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              ltrim(NVL (f.partita_iva, a.partita_iva)) partita_iva,
                              ltrim(NVL (f.codice_fiscale, a.codice_fiscale)) codice_fiscale,null TI_BENE_SERVIZIO,null tipo_fiscalita,null mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              SUM (fr.im_imponibile) imponibile_nd_fa,
                              SUM (fr.im_iva) iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              null cognome,
                              null nome,
                              null dt_nascita,
                              null stato_nascita,
                              null provincia,
                              null stato_residenza,
                              null ragione_sociale,
                              null indirizzo_sede,
                              null comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND f.ti_fattura = 'D'  and
                              F.FL_INTRA_UE = 'N'  AND
                              F.FL_EXTRA_UE ='N' AND
                              f.stato_cofi != 'A' and
                              a.ti_italiano_estero NOT IN ('E', 'C', 'M')
                          AND f.protocollo_iva IS NOT NULL
                          AND v.cd_voce_iva IN ('ED','ES','N2','N3','N4','N5','N8','VA','4%','10%','21%','22%')
                          and (ltrim(a.partita_iva) is not null or ltrim(a.codice_fiscale) is not null)
                        GROUP BY 'FA','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             NVL (f.codice_fiscale, a.codice_fiscale)
                     -- OPERAZIONI PASSIVE RESIDENTI----
                     union all
                      SELECT  'FA' quadro,'PASSIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_registrazione, 'YYYY')) esercizio,
                              ltrim(NVL (f.partita_iva, a.partita_iva)) partita_iva,
                              ltrim(nvl(f.codice_fiscale,a.codice_fiscale))codice_fiscale, null TI_BENE_SERVIZIO,null tipo_fiscalita,null mese,
                              0 nr_attive,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_passiva) nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              0 imponibile_nc_fa,
                              0 iva_nc_fa,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  ) imponibile_fp,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_iva,
                                           fr.im_iva
                                          )
                                  ) iva_fp,
                              0 imponibile_nc_fp,
                              0 iva_nc_fp,
                              null cognome,
                              null nome,
                              null dt_nascita,
                              null stato_nascita,
                              null provincia,
                              null stato_residenza,
                              null ragione_sociale,
                              null indirizzo_sede,
                              null comune_sede
                         FROM fattura_passiva f,
                              fattura_passiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_passiva = fr.pg_fattura_passiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND f.ti_fattura in('C','F')     and
                          		f.TI_ISTITUZ_COMMERC ='C' and
                              F.FL_INTRA_UE = 'N'  AND
                              F.FL_EXTRA_UE ='N'   AND
                              f.stato_cofi != 'A'  and
                              a.ti_italiano_estero NOT IN ('E', 'C', 'M')
                          AND f.protocollo_iva IS NOT NULL
                          AND v.cd_voce_iva IN ('ED','ES','N2','N3','N4','N5','N8','4%', '10%','21%','22%')
                          and (ltrim(a.partita_iva) is not null or ltrim(a.codice_fiscale) is not null)
                        GROUP BY 'FA','PASSIVE',TO_CHAR (f.dt_registrazione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             nvl(f.codice_fiscale,a.codice_fiscale)
                     UNION ALL
                   -- OPERAZIONI PASSIVE note debito  RESIDENTI----
                    SELECT    'FA' quadro,'PASSIVE' tipo,TO_NUMBER (TO_CHAR (f.dt_registrazione, 'YYYY')) esercizio,
                              ltrim(NVL (f.partita_iva, a.partita_iva)) partita_iva,
                              ltrim(nvl(f.codice_fiscale,a.codice_fiscale))codice_fiscale,null TI_BENE_SERVIZIO,null tipo_fiscalita,null mese,
                              0 nr_attive,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_passiva) nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              0 imponibile_nd_fa,
                              0 iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              SUM (fr.im_imponibile) imponibile_nd_fp,
                              SUM (fr.im_iva) iva_nd_fp,
                              null cognome,
                              null nome,
                              null dt_nascita,
                              null stato_nascita,
                              null provincia,
                              null stato_residenza,
                              null ragione_sociale,
                              null indirizzo_sede,
                              null comune_sede
                         FROM fattura_passiva f,
                              fattura_passiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_passiva = fr.pg_fattura_passiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND f.ti_fattura  in('D')
                          and f.TI_ISTITUZ_COMMERC ='C' and
                              F.FL_INTRA_UE = 'N'  AND
                              F.FL_EXTRA_UE ='N' AND
                              f.stato_cofi != 'A' and
                              a.ti_italiano_estero NOT IN ('E', 'C', 'M')
                          AND f.protocollo_iva IS NOT NULL
                          AND v.cd_voce_iva IN ('CM','ED','ES','NB','NC','ND21%','ND22%','N2','N3','N4','N5','N8','VA','4%','10%','21%','22%')
                          and (ltrim(a.partita_iva) is not null or ltrim(a.codice_fiscale) is not null)
                        GROUP BY 'FA','PASSIVE',TO_CHAR (f.dt_registrazione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             nvl(f.codice_fiscale,a.codice_fiscale)
                        UNION ALL
		                    -- QUADRO BL
								  			-- OPERAZIONI ATTIVE NON RESIDENTI extra ue non blacklist----
		                    SELECT   'BL' quadro,'ATTIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,b.TI_BENE_SERVIZIO,'FO',null mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  ) imponibile_fa,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_iva,
                                           fr.im_iva
                                          )
                                  ) iva_fa,
                              0 imponibile_nd_fa,
                              0 iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,
                              bene_servizio b
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('F','C')
                          and a.fl_soggetto_iva='Y'
                          and F.FL_EXTRA_UE ='Y'
                          AND f.protocollo_iva IS NOT NULL
                          and b.cd_bene_servizio = fr.cd_bene_servizio
                          AND v.cd_voce_iva IN ('ED','ES','F3E','F4E','F5E','N2','N3','N4','N5','N8')
                          and nazione.cd_nazione_770 not in(select cd_nazione from NAZIONE_BLACKLIST where esercizio = f.esercizio)
                        GROUP BY 'BL','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero ,b.TI_BENE_SERVIZIO,'FO',null,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                              nazione.cd_nazione_770,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                              comres.ds_comune
                         -- QUADRO BL
						  			     -- OPERAZIONI ATTIVE note debito NON RESIDENTI extra ue non blacklist----
						  			     UNION ALL
                         SELECT   'BL' quadro,'ATTIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,b.TI_BENE_SERVIZIO,'FO',null mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              SUM (fr.im_imponibile) imponibile_nd_fa,
                              SUM (fr.im_iva) iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,bene_servizio b
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('D')
                          and a.fl_soggetto_iva='Y'
                          and F.FL_EXTRA_UE ='Y'
                          AND f.protocollo_iva IS NOT NULL
                          and b.cd_bene_servizio = fr.cd_bene_servizio
                          AND v.cd_voce_iva IN ('ED','ES','F3E','F4E','F5E','N2','N3','N4','N5','N8','VA')
                          and nazione.cd_nazione_770 not in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                        GROUP BY 'BL','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,b.TI_BENE_SERVIZIO,'FO',null,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                              nazione.cd_nazione_770,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                              comres.ds_comune
                     UNION ALL
                   -- OPERAZIONI ATTIVE NON RESIDENTI (B to C) beni extra ue non blacklist----
                     SELECT   'BL' quadro,'ATTIVE' tipo,TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,b.TI_BENE_SERVIZIO,'FO',null mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  ) imponibile_fa,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_iva,
                                           fr.im_iva
                                          )
                                  ) iva_fa,
                              0 imponibile_nd_fa,
                              0 iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,bene_servizio b
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('F','C')
                          and a.fl_soggetto_iva='N'
                          and b.ti_bene_servizio  ='B'
                          and F.FL_EXTRA_UE ='Y'
                          AND f.protocollo_iva IS NOT NULL
                          and b.cd_bene_servizio = fr.cd_bene_servizio
                          AND v.cd_voce_iva IN ('ED','ES','N2','N3','N4','N5','N8')
                          and nazione.cd_nazione_770 not in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                        GROUP BY 'BL','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,b.TI_BENE_SERVIZIO,'FO',null,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                              nazione.cd_nazione_770,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                              comres.ds_comune
	  				  			 -- QUADRO BL
						  		   -- OPERAZIONI ATTIVE NON RESIDENTI note Debito (B to C) beni extra ue non blacklist----
						  		   UNION ALL
                     SELECT   'BL' quadro,'ATTIVE' tipo,TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,b.TI_BENE_SERVIZIO,'FO',null mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              SUM (fr.im_imponibile) imponibile_nd_fa,
                              SUM (fr.im_iva) iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,bene_servizio b
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('D')
                          and a.fl_soggetto_iva='N'
                          and b.ti_bene_servizio  ='B'
                          and F.FL_EXTRA_UE ='Y'
                          AND f.protocollo_iva IS NOT NULL
                          and b.cd_bene_servizio = fr.cd_bene_servizio
                          AND v.cd_voce_iva IN ('ED','ES','N2','N3','N4','N5','N8','VA')
                          and nazione.cd_nazione_770 not in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                        GROUP BY 'BL','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,b.TI_BENE_SERVIZIO,'FO',null,
                             a.cognome,
                             a.nome,
                             a.dt_nascita,
                             DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                             DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                             nazione.cd_nazione_770,
                             a.ragione_sociale,
                             LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                             comres.ds_comune
                     UNION ALL
                         -- OPERAZIONI ATTIVE NON RESIDENTI (B to C) servizi extra ue non blacklist ----
                     SELECT   'BL' quadro,'ATTIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,b.TI_BENE_SERVIZIO,'FO',null mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  ) imponibile_fa,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_iva,
                                           fr.im_iva
                                          )
                                  ) iva_fa,
                              0 imponibile_nd_fa,
                              0 iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,bene_servizio b
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('F','C')
                          and a.fl_soggetto_iva='N'
                          and b.ti_bene_servizio  ='S'
                          and F.FL_EXTRA_UE ='Y'
                          AND f.protocollo_iva IS NOT NULL
                          and b.cd_bene_servizio = fr.cd_bene_servizio
                          AND v.cd_voce_iva IN ('ED','ES','F3E','F4E','F5E','F6','F8','N2','N3','N4','N5','N8','4%','10%','21%','22%')
                          and nazione.cd_nazione_770 not in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                        GROUP BY 'BL','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,b.TI_BENE_SERVIZIO,'FO',null,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                              nazione.cd_nazione_770,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                              comres.ds_comune
	  				  			 -- QUADRO BL
						  		   -- OPERAZIONI ATTIVE NON RESIDENTI note Debito (B to C) servizi extra ue non blacklist----
						  		   UNION ALL
                     SELECT   'BL' quadro,'ATTIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,b.TI_BENE_SERVIZIO,'FO',null mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              SUM (fr.im_imponibile) imponibile_nd_fa,
                              SUM (fr.im_iva) iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,bene_servizio b
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('D')
                          and a.fl_soggetto_iva='N'
                          and b.ti_bene_servizio  ='S'
                          and F.FL_EXTRA_UE ='Y'
                          AND f.protocollo_iva IS NOT NULL
                          and b.cd_bene_servizio = fr.cd_bene_servizio
                          AND v.cd_voce_iva IN ('ED','ES','F3E','F4E','F5E','F6','F8','N2','N3','N4','N5','N8','VA','4%','10%','21%','22%')
                           and nazione.cd_nazione_770 not in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                        GROUP BY 'BL','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,b.TI_BENE_SERVIZIO,'FO',null,
                             a.cognome,
                             a.nome,
                             a.dt_nascita,
                             DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                             DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                             nazione.cd_nazione_770,
                             a.ragione_sociale,
                             LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                             comres.ds_comune
                       -- QUADRO BL
						  		     -- OPERAZIONI PASSIVE NON RESIDENTI extra ue (B to B) servizi non blacklist----
						  		     UNION ALL
                       SELECT   'BL' quadro,'PASSIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_registrazione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,f.TI_BENE_SERVIZIO,'FO',null mese,
                              0 nr_attive,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_passiva) nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              0 imponibile_nc_fa,
                              0 iva_nc_fa,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  ) imponibile_fp,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_iva,
                                           fr.im_iva
                                          )
                                  ) iva_fp,
                              0 imponibile_nc_fp,
                              0 iva_nc_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_passiva f,
                              fattura_passiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_passiva = fr.pg_fattura_passiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('C','F')
                          and	f.TI_ISTITUZ_COMMERC ='C'
                          and f.TI_BENE_SERVIZIO ='S'
                          and f.stato_cofi != 'A'
                          and f.fl_extra_ue ='Y'
                          AND f.protocollo_iva IS NOT NULL
                          AND v.cd_voce_iva IN ('F4','F5','F6','F8','4%','10%','21%','22%')
                          and nazione.cd_nazione_770 not in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                        GROUP BY 'BL','PASSIVE',TO_CHAR (f.dt_registrazione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,f.TI_BENE_SERVIZIO,'FO',null,
                             a.cognome,
                             a.nome,
                             a.dt_nascita,
                             DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                             DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                             nazione.cd_nazione_770,
                             a.ragione_sociale,
                             LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                             comres.ds_comune
                     UNION ALL
                   -- OPERAZIONI PASSIVE note debito NON RESIDENTI extra ue (B to B) servizi non blacklist ----
                    SELECT   'BL' quadro,'PASSIVE' tipo,TO_NUMBER (TO_CHAR (f.dt_registrazione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,f.TI_BENE_SERVIZIO,'FO',null mese,
                              0 nr_attive,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_passiva) nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              0 imponibile_nd_fa,
                              0 iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              SUM (fr.im_imponibile) imponibile_nd_fp,
                              SUM (fr.im_iva) iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_passiva f,
                              fattura_passiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_passiva = fr.pg_fattura_passiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura  in('D')
                          and f.TI_ISTITUZ_COMMERC ='C'
                          and f.TI_BENE_SERVIZIO ='S'
                          and f.stato_cofi != 'A'
                          and f.fl_extra_ue ='Y'
                          AND f.protocollo_iva IS NOT NULL
                          AND v.cd_voce_iva IN ('F4','F5','F6','F8','4%','10%','21%','22%','VA')
                          and nazione.cd_nazione_770 not in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                        GROUP BY 'BL','PASSIVE',TO_CHAR (f.dt_registrazione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,f.TI_BENE_SERVIZIO,'FO',null,
                             a.cognome,
                             a.nome,
                             a.dt_nascita,
                             DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                             DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                             nazione.cd_nazione_770,
                             a.ragione_sociale,
                             LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                             comres.ds_comune
                 		  UNION ALL
                  		 -- QUADRO BL
								  			-- OPERAZIONI ATTIVE NON RESIDENTI Fiscalità speciale Blacklist----
		                    SELECT   'BL' quadro,'ATTIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,b.TI_BENE_SERVIZIO,'FS',decode(c.val01,'M',TO_NUMBER (TO_CHAR (f.dt_emissione, 'mm')),null) mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  ) imponibile_fa,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_iva,
                                           fr.im_iva
                                          )
                                  ) iva_fa,
                              0 imponibile_nd_fa,
                              0 iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,bene_servizio b,
                              configurazione_cnr c
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('F','C')
                          AND f.protocollo_iva IS NOT NULL
                          and b.cd_bene_servizio = fr.cd_bene_servizio
                          and nazione.cd_nazione_770  in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                           AND c.esercizio = f.esercizio
                  				 AND c.cd_chiave_primaria = 'COSTANTI'
                  				 AND c.cd_chiave_secondaria = 'BLACKLIST'
                          AND c.cd_unita_funzionale = '*'
                          AND (   (   ((im_totale_imponibile + im_totale_iva) >
                                                               NVL (c.im01, 0)
                               )
                           AND f.dt_emissione >= NVL (c.dt01, f.dt_emissione)
                          )
                       OR f.dt_emissione < NVL (c.dt01, f.dt_emissione)
                      )
                        GROUP BY 'BL','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                              id_fiscale_estero,b.TI_BENE_SERVIZIO,'FS',decode(c.val01,'M',TO_NUMBER (TO_CHAR (f.dt_emissione, 'mm')),null),
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                              nazione.cd_nazione_770,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                              comres.ds_comune
                              ,c.im02
                          having SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  )>=NVL (c.im02,0)
                         -- QUADRO BL
						  			     -- OPERAZIONI ATTIVE note debito NON RESIDENTI  Fiscalità speciale Blacklist----
						  			     UNION ALL
                         SELECT   'BL' quadro,'ATTIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_emissione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,b.TI_BENE_SERVIZIO,'FS',decode(c.val01,'M',TO_NUMBER (TO_CHAR (f.dt_emissione, 'mm')),null) mese,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_attiva) nr_attive,
                              0 nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              SUM (fr.im_imponibile) imponibile_nd_fa,
                              SUM (fr.im_iva) iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              0 imponibile_nd_fp,
                              0 iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_attiva f,
                              fattura_attiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,bene_servizio b,
                              configurazione_cnr c
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('D')
                          AND f.protocollo_iva IS NOT NULL
                          and b.cd_bene_servizio = fr.cd_bene_servizio
                          and nazione.cd_nazione_770  in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                          AND c.esercizio = f.esercizio
                  				AND c.cd_chiave_primaria = 'COSTANTI'
                  				AND c.cd_chiave_secondaria = 'BLACKLIST'
                          AND c.cd_unita_funzionale = '*'
                          AND (   (    ((im_totale_imponibile + im_totale_iva) >
                                                               NVL (c.im01, 0)
                               )
                           AND f.dt_emissione >= NVL (c.dt01, f.dt_emissione)
                          )
                       OR f.dt_emissione < NVL (c.dt01, f.dt_emissione)
                      )
                        GROUP BY 'BL','ATTIVE',TO_CHAR (f.dt_emissione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,b.TI_BENE_SERVIZIO,'FS',decode(c.val01,'M',TO_NUMBER (TO_CHAR (f.dt_emissione, 'mm')),null),
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                              nazione.cd_nazione_770,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                              comres.ds_comune
                              ,c.im02
                        having SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  )>=NVL (c.im02, 0)
                       -- QUADRO BL
						  		     -- OPERAZIONI PASSIVE NON RESIDENTI  Fiscalità speciale Blacklist----
						  		     UNION ALL
                       SELECT   'BL' quadro,'PASSIVE' tipo, TO_NUMBER (TO_CHAR (f.dt_registrazione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,f.TI_BENE_SERVIZIO,'FS',decode(c.val01,'M',TO_NUMBER (TO_CHAR (f.dt_registrazione, 'mm')),null) mese,
                              0 nr_attive,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_passiva) nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              0 imponibile_nc_fa,
                              0 iva_nc_fa,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  ) imponibile_fp,
                              SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_iva,
                                           fr.im_iva
                                          )
                                  ) iva_fp,
                              0 imponibile_nc_fp,
                              0 iva_nc_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_passiva f,
                              fattura_passiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,
                              configurazione_cnr c
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_passiva = fr.pg_fattura_passiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura in('C','F')
                          and	f.TI_ISTITUZ_COMMERC ='C'
                          and f.stato_cofi != 'A'
                          AND f.protocollo_iva IS NOT NULL
                          and nazione.cd_nazione_770  in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                          AND c.esercizio = f.esercizio
                  				 AND c.cd_chiave_primaria = 'COSTANTI'
                  				 AND c.cd_chiave_secondaria = 'BLACKLIST'
                          AND c.cd_unita_funzionale = '*'
                            AND (   (    ((im_totale_imponibile + im_totale_iva) >
                                                               NVL (c.im01, 0)
                               )
                           AND f.dt_registrazione >=
                                                NVL (c.dt01, f.dt_registrazione)
                          )
                       OR f.dt_registrazione < NVL (c.dt01, f.dt_registrazione)
                      )
                        GROUP BY 'BL','PASSIVE',TO_CHAR (f.dt_registrazione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,f.TI_BENE_SERVIZIO,'FS',decode(c.val01,'M',TO_NUMBER (TO_CHAR (f.dt_registrazione, 'mm')),null),
                             a.cognome,
                             a.nome,
                             a.dt_nascita,
                             DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                             DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                             nazione.cd_nazione_770,
                             a.ragione_sociale,
                             LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                             comres.ds_comune
                              ,c.im02
                        having SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  )>=NVL (c.im02, 0)
                     UNION ALL
                   -- OPERAZIONI PASSIVE note debito NON RESIDENTI  Fiscalità speciale   Blacklist----
                    SELECT   'BL' quadro,'PASSIVE' tipo,TO_NUMBER (TO_CHAR (f.dt_registrazione, 'YYYY')) esercizio,
                              NVL (f.partita_iva, a.partita_iva) partita_iva,
                              id_fiscale_estero codice_fiscale,f.TI_BENE_SERVIZIO,'FS',decode(c.val01,'M',TO_NUMBER (TO_CHAR (f.dt_registrazione, 'mm')),null) mese,
                              0 nr_attive,
                              count(distinct fr.esercizio||fr.cd_cds||fr.cd_unita_organizzativa||fr.pg_fattura_passiva) nr_passive,
                              0 imponibile_fa,
                              0 iva_fa,
                              0 imponibile_nd_fa,
                              0 iva_nd_fa,
                              0 imponibile_fp,
                              0 iva_fp,
                              SUM (fr.im_imponibile) imponibile_nd_fp,
                              SUM (fr.im_iva) iva_nd_fp,
                              a.cognome,
                              a.nome,
                              a.dt_nascita,
                              DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) stato_nascita,
                              DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') provincia,
                              nazione.cd_nazione_770 stato_residenza,
                              a.ragione_sociale,
                              LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede indirizzo_sede,
                              comres.ds_comune comune_sede
                         FROM fattura_passiva f,
                              fattura_passiva_riga fr,
                              voce_iva v,
                              terzo t,
                              anagrafico a,
                              comune n,
                              nazione naz,
                              nazione,
                              comune comres,
                              comune,
                              configurazione_cnr c
                        WHERE f.cd_cds = fr.cd_cds
                          AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                          AND f.esercizio = fr.esercizio
                          AND f.pg_fattura_passiva = fr.pg_fattura_passiva
                          AND v.cd_voce_iva = fr.cd_voce_iva
                          AND t.cd_terzo = f.cd_terzo
                          AND t.cd_anag = a.cd_anag
                          AND n.pg_comune = NVL (a.pg_comune_nascita,a.pg_comune_fiscale)
                          AND naz.pg_nazione = n.pg_nazione
                          AND t.pg_comune_sede = comres.pg_comune
                          AND (t.pg_comune_sede = comune.pg_comune
                      			OR a.pg_comune_fiscale = comune.pg_comune)
                          AND nazione.pg_nazione = comune.pg_nazione
                          AND f.ti_fattura  in('D')
                          and f.TI_ISTITUZ_COMMERC ='C'
                          and f.stato_cofi != 'A'
                          AND f.protocollo_iva IS NOT NULL
                          and nazione.cd_nazione_770  in(select cd_nazione from nazione_blacklist where esercizio = f.esercizio)
                          AND c.esercizio = f.esercizio
                  				 AND c.cd_chiave_primaria = 'COSTANTI'
                  				 AND c.cd_chiave_secondaria = 'BLACKLIST'
                          AND c.cd_unita_funzionale = '*'
                              AND (   (    ((im_totale_imponibile + im_totale_iva) >
                                                               NVL (c.im01, 0)
                               )
                           AND f.dt_registrazione >=
                                                NVL (c.dt01, f.dt_registrazione)
                          )
                       OR f.dt_registrazione < NVL (c.dt01, f.dt_registrazione)
                      )
                        GROUP BY 'BL','PASSIVE',TO_CHAR (f.dt_registrazione, 'YYYY'),
                             NVL (f.partita_iva, a.partita_iva),
                             id_fiscale_estero,f.TI_BENE_SERVIZIO,'FS',decode(c.val01,'M',TO_NUMBER (TO_CHAR (f.dt_registrazione, 'mm')),null),
                             a.cognome,
                             a.nome,
                             a.dt_nascita,
                             DECODE (naz.ti_nazione,'I',n.ds_comune,naz.ds_nazione) ,
                             DECODE (naz.ti_nazione,'I',n.cd_provincia,'EE') ,
                             nazione.cd_nazione_770,
                             a.ragione_sociale,
                             LTRIM (t.via_sede)|| ' '|| t.numero_civico_sede,
                             comres.ds_comune
                              ,c.im02
                         having SUM (DECODE (f.ti_fattura,
                                           'C', -fr.im_imponibile,
                                           fr.im_imponibile
                                          )
                                  )>=NVL (c.im02, 0)
                 )
                    GROUP BY     quadro,tipo,esercizio, partita_iva,codice_fiscale, TI_BENE_SERVIZIO,tipo_fiscalita,mese,
                    cognome,
                    nome,
                    dt_nascita,
                    stato_nascita,
                    provincia,
                    stato_residenza,
                    ragione_sociale,
                    indirizzo_sede,
                    comune_sede
                    having ( round(sum(imponibile_fa))>0 or  round(sum(imponibile_nd_fa)) >0  or  round(sum(imponibile_fp))>0 or  round(sum(imponibile_nd_fp))>0 ))
      						  order by esercizio,decode(quadro,'FA','1','2'),decode(tipo,'ATTIVE','1','2'),TI_BENE_SERVIZIO,tipo_fiscalita,partita_iva,codice_fiscale;
