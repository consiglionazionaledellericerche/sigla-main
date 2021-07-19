--------------------------------------------------------
--  DDL for View V_SPESOMETRO_NEW
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SPESOMETRO_NEW" ("DATA", "TIPO", "TI_FATTURA", "ESERCIZIO", "PARTITA_IVA", "CODICE_FISCALE", "UO", "TERZO_UO", "PROG_FATTURA", "PERCENTUALE", "NATURA", "FL_DETRAIBILE", "PERC_DETRA", "IMPONIBILE_FA", "IVA_FA", "SPLIT", "COGNOME", "NOME", "PROVINCIA", "COD_NAZ", "STATO_RESIDENZA", "RAGIONE_SOCIALE", "INDIRIZZO_SEDE", "NUMERO_CIVICO_SEDE", "COMUNE_SEDE", "CAP", "DT_FATTURA_FORNITORE", "NR_FATTURA_FORNITORE", "PROG") AS 
  SELECT   data,tipo, ti_fattura, esercizio, partita_iva, codice_fiscale,
   					uo, terzo_uo,prog_fattura,percentuale,natura,fl_detraibile, perc_detra,
            imponibile_fa, iva_fa, split, cognome, nome,  provincia, cod_naz,stato_residenza, ragione_sociale,
            indirizzo_sede, numero_civico_sede, comune_sede, cap,dt_fattura_fornitore,nr_fattura_fornitore, rownum
       FROM (                  -- OPERAZIONI ATTIVE ----
                       SELECT    f.dt_emissione data,'ATTIVA' tipo,decode(f.ti_fattura,'F','TD_01','C','TD_04','TD_05') ti_fattura,
                                   f.esercizio,
                                LTRIM ( a.partita_iva) partita_iva,
                                LTRIM (a.codice_fiscale) codice_fiscale,
                                f.cd_uo_origine uo,
                                f.cd_terzo_uo_cds terzo_uo,
                                F.pg_FATTURA_ATTIVA PROG_FATTURA,
                                nvl(percentuale,0) percentuale,
                                NATURA_OPER_NON_IMP_SDI natura,
                                FL_DETRAIBILE,
                                nvl(PERCENTUALE_DETRAIBILITA,0) PERC_DETRA,
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
                                    decode(f.fl_liquidazione_differita,'Y','S','I') split,
                                A.cognome, A.nome,
                                c.cd_provincia provincia, cd_iso cod_naz,n.cd_iso stato_residenza ,
                                A.ragione_sociale, t.via_sede indirizzo_sede,NUMERO_CIVICO_SEDE,
                                c.ds_comune comune_sede,t.CAP_COMUNE_SEDE cap,
                                NULL DT_FATTURA_FORNITORE,
                                NULL NR_FATTURA_FORNITORE
                           FROM fattura_attiva f,
                                fattura_attiva_riga fr,
                                voce_iva v,
                                terzo t,
                                anagrafico a,
                                comune c,
                                nazione n
                          WHERE
                          f.cd_cds = fr.cd_cds
                            AND f.cd_unita_organizzativa =
                                                     fr.cd_unita_organizzativa
                            AND f.esercizio = fr.esercizio
                            AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                            AND v.cd_voce_iva = fr.cd_voce_iva
                            AND t.cd_terzo = f.cd_terzo
                            AND t.cd_anag = a.cd_anag
                            and c.pg_comune = t.pg_comune_sede
                            and n.pg_nazione = a.pg_nazione_fiscale
                            AND f.stato_cofi != 'A'
                            AND f.protocollo_iva IS NOT NULL
                            and (STATO_INVIO_SDI is null or f.fl_intra_ue = 'Y' OR f.fl_EXTra_ue = 'Y' OR f.fl_SAN_MARINO = 'Y')
                            AND (   LTRIM (a.partita_iva) IS NOT NULL
                                 OR LTRIM (a.codice_fiscale) IS NOT NULL)
                            having SUM (DECODE (f.ti_fattura,
                                             'C', -fr.im_imponibile,
                                             fr.im_imponibile
                                            )
                                    )!=0
                            group by
                             		f.dt_emissione,'ATTIVA' ,decode(f.ti_fattura,'F','TD_01','C','TD_04','TD_05'),
                                f.esercizio,
                                LTRIM (a.partita_iva) ,
                                LTRIM (a.codice_fiscale) ,
                                f.cd_uo_origine,
                                f.cd_terzo_uo_cds,
                                F.pg_FATTURA_ATTIVA,
                                nvl(PERCENTUALE,0),
                                NATURA_OPER_NON_IMP_SDI,
                                FL_DETRAIBILE,
                                nvl(PERCENTUALE_DETRAIBILITA,0),
                                decode(f.fl_liquidazione_differita,'Y','S','I'),
                                A.cognome, A.nome,
                                c.cd_provincia, cd_iso,n.cd_iso,
                                A.ragione_sociale, t.via_sede,NUMERO_CIVICO_SEDE,
                                c.ds_comune ,t.CAP_COMUNE_SEDE
                       UNION ALL
                       SELECT    f.DT_REGISTRAZIONE data ,'PASSIVA' tipo,decode(f.fl_intra_ue,'Y',decode(f.ti_bene_servizio,'B','TD_10','TD_11'),decode(f.ti_fattura,'F','TD_01','C','TD_04','TD_05')) ti_fattura,
                                   f.esercizio,
                               LTRIM ( a.partita_iva) partita_iva,
                                LTRIM (a.codice_fiscale) codice_fiscale,
                                f.cd_uo_origine UO,
                                (select min(cd_terzo) from terzo where cd_unita_organizzativa= f.cd_uo_origine and dt_fine_rapporto is null) terzo_uo,
                                F.pg_FATTURA_PASSIVA PROG_FATTURA,
                                nvl(percentuale,0) percentuale,
                                DECODE(F.FL_AUTOFATTURA,'Y',decode(nvl(percentuale,0),0,NATURA_OPER_NON_IMP_SDI, null),NATURA_OPER_NON_IMP_SDI) natura,
                                FL_DETRAIBILE,
                                nvl(PERCENTUALE_DETRAIBILITA,0) perc_detra,
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
                                decode(f.fl_split_payment,'Y','S','I') split,
                                A.cognome, A.nome,
                                c.cd_provincia provincia, cd_iso cod_naz,n.cd_iso stato_residenza,
                                A.ragione_sociale, t.via_sede indirizzo_sede,NUMERO_CIVICO_SEDE,
                                c.ds_comune comune_sede,t.CAP_COMUNE_SEDE cap,
                                DT_FATTURA_FORNITORE,
                                NR_FATTURA_FORNITORE
                           FROM fattura_passiva f,
                                fattura_passiva_riga fr,
                                voce_iva v,
                                terzo t,
                                anagrafico a,
                                comune c,
                                nazione n,
                                autofattura
                          WHERE
                         				f.cd_cds = fr.cd_cds
                            AND f.cd_unita_organizzativa =
                                                     fr.cd_unita_organizzativa
                            AND f.esercizio = fr.esercizio
                            AND f.pg_fattura_passiva = fr.pg_fattura_passiva
                            AND v.cd_voce_iva = fr.cd_voce_iva
                            AND t.cd_terzo = f.cd_terzo
                            AND t.cd_anag = a.cd_anag
                            and c.pg_comune = t.pg_comune_sede
                            and n.pg_nazione = a.pg_nazione_fiscale
                            AND f.stato_cofi != 'A'
                            AND f.protocollo_iva IS NOT NULL
                            and IDENTIFICATIVO_SDI is null
                            and f.ti_istituz_commerc = 'C'  --????
                            AND (   LTRIM (a.partita_iva) IS NOT NULL
                                 OR LTRIM (a.codice_fiscale) IS NOT NULL
                                )
                                and
                                 (f.cd_cds = autofattura.cd_cds_ft_passiva(+)														and
        												  f.cd_unita_organizzativa =autofattura.cd_uo_ft_passiva(+)							and
        													f.esercizio = autofattura.esercizio(+)																	and
        													f.pg_fattura_passiva = autofattura.pg_fattura_passiva(+)	AND
        													'Y'=autofattura.FL_AUTOFATTURA  (+))
        												having   SUM (DECODE (f.ti_fattura,
                                             'C', -fr.im_imponibile,
                                             fr.im_imponibile
                                            )) !=0
                                group by
                             		f.dt_registrazione,'PASSIVA' ,decode(f.fl_intra_ue,'Y',decode(f.ti_bene_servizio,'B','TD_10','TD_11'),decode(f.ti_fattura,'F','TD_01','C','TD_04','TD_05')),
                                   f.esercizio,
                                LTRIM ( a.partita_iva) ,
                                LTRIM (a.codice_fiscale),
                                f.cd_uo_origine,
                                F.pg_FATTURA_PASSIVA,
                                nvl(percentuale,0) ,
                                DECODE(F.FL_AUTOFATTURA,'Y',decode(nvl(percentuale,0),0,NATURA_OPER_NON_IMP_SDI,null),NATURA_OPER_NON_IMP_SDI),
                                FL_DETRAIBILE,
                                nvl(PERCENTUALE_DETRAIBILITA,0),
                                decode(f.fl_split_payment,'Y','S','I'),
                                A.cognome, A.nome,
                                c.cd_provincia, cd_iso,n.cd_iso ,
                                A.ragione_sociale, t.via_sede,NUMERO_CIVICO_SEDE,
                                c.ds_comune,
                                t.CAP_COMUNE_SEDE,
                                DT_FATTURA_FORNITORE,
                                NR_FATTURA_FORNITORE );
