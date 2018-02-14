--------------------------------------------------------
--  DDL for View V_INTRASTAT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INTRASTAT" ("TIPO", "ESERCIZIO", "MESE", "TI_BENE_SERVIZIO", "DT_REGISTRAZIONE", "NR_FATTURA", "DT_FATTURA", "PARTITA_IVA", "CD_NATURA_TRANSAZIONE", "CD_NOMENCLATURA_COMBINATA", "AMMONTARE_EURO", "AMMONTARE_DIVISA", "MASSA_NETTA", "UNITA_SUPPLEMENTARI", "VALORE_STATISTICO", "CD_CONSEGNA", "CD_MODALITA_TRASPORTO", "CD_CPA", "CD_MODALITA_INCASSO", "CD_MODALITA_EROGAZIONE", "PROVENIENZA", "ORIGINE", "DEST", "CD_PROVINCIA_DESTINAZIONE", "CD_PROVINCIA_ORIGINE", "TI_FATTURA", "NAZ_FISCALE", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "PG_FATTURA", "PG_RIGA_INTRA", "NR_PROTOCOLLO", "NR_PROGRESSIVO", "PG_STORICO", "UO_ORIGINE", "DS_TIPO") AS 
  select 'P',t.esercizio,To_Number(To_Char(dt_registrazione,'mm')),t.ti_bene_servizio,t.dt_registrazione,t.nr_fattura_fornitore,To_Char(t.dt_fattura_fornitore,'ddmmyy'),a.partita_iva,n.cd_natura_transazione,b.cd_nomenclatura_combinata,
Round(Nvl(i.ammontare_euro,0),0) ammontare_euro,
Round(Nvl(i.ammontare_divisa,0),0) ammontare_divisa,
Decode(round(Nvl(i.massa_netta,0),0),0,1,round(Nvl(i.massa_netta,0),0)) massa_netta,
Decode(round(Nvl(i.unita_supplementari,0),0),0,1,Round(Nvl(i.unita_supplementari,0),0)) unita_supplementari ,
Round(Nvl(i.valore_statistico,0),0) valore_statistico,
c.cd_gruppo,i.cd_modalita_trasporto,s.cd_cpa,i.cd_modalita_incasso,i.cd_modalita_erogazione,
decode(t.ti_bene_servizio,'B',n1.cd_iso_intrastat,n1.cd_iso) provenienza, n2.cd_iso origine, Null dest,i.cd_provincia_destinazione, Null cd_provincia_origine,t.ti_fattura,n3.cd_iso_intrastat naz_fiscale,
t.cd_cds,t.cd_unita_organizzativa,t.pg_fattura_passiva,i.pg_riga_intra,i.nr_protocollo, i.nr_progressivo,0 pg_storico,cd_uo_origine, 'Passiva'
--t.esercizio,t.cd_cds,t.cd_unita_organizzativa,t.pg_fattura_passiva,i.pg_riga_intra
 from nazione n1,nazione n2,nazione n3,anagrafico a,terzo z,
 fattura_passiva T,fattura_passiva_intra I,natura_transazione n,codici_cpa s,nomenclatura_combinata b,condizione_consegna c
Where
t.ti_fattura ='F' And
a.partita_iva is not null And
ammontare_euro!=0 And
n1.pg_nazione = i.pg_nazione_provenienza And
n3.pg_nazione = a.pg_nazione_fiscale And
a.cd_anag     = z.cd_anag      And
z.cd_terzo    = t.cd_terzo     And
n2.pg_nazione(+) = i.pg_nazione_origine and
s.id_cpa(+) = i.id_cpa  and
b.id_nomenclatura_combinata (+) = i.id_nomenclatura_combinata and
n.id_natura_transazione (+) = i.id_natura_transazione And
c.cd_incoterm (+) =i.cd_incoterm And
c.esercizio(+) = i.ESERCIZIO_COND_CONSEGNA And
T.fl_intra_ue  = 'Y' and
t.esercizio = i.esercizio and
t.cd_cds    = i.cd_cds    and
t.cd_unita_organizzativa = i.cd_unita_organizzativa and
t.pg_fattura_passiva = i.pg_fattura_passiva and
I.FL_INVIATO='N' and
T.STATO_COFI!='A'
Union
 Select 'A',t.esercizio,To_Number(To_Char(t.dt_emissione,'mm')),t.ti_bene_servizio,t.dt_registrazione,To_Char(t.protocollo_iva),To_Char(t.dt_emissione,'ddmmyy'),a.partita_iva,n.cd_natura_transazione,b.cd_nomenclatura_combinata,
Round(Nvl(i.ammontare_euro,0),0) ammontare_euro,
Null ammontare_divisa,
Decode(round(Nvl(i.massa_netta,0),0),0,1,round(Nvl(i.massa_netta,0),0)) massa_netta,
Decode(round(Nvl(i.unita_supplementari,0),0),0,1,Round(Nvl(i.unita_supplementari,0),0)) unita_supplementari ,
0 valore_statistico,
c.cd_gruppo,i.cd_modalita_trasporto,s.cd_cpa,i.cd_modalita_incasso,i.cd_modalita_erogazione,
Null provenienza, Null origine,decode(t.ti_bene_servizio,'B',n1.cd_iso_intrastat,n1.cd_iso) dest,Null cd_provincia_destinazione,i.cd_provincia_origine,t.ti_fattura, n3.cd_iso_intrastat naz_fiscale,
t.cd_cds,t.cd_unita_organizzativa,t.pg_fattura_attiva,i.pg_riga_intra,i.nr_protocollo, i.nr_progressivo,0 pg_storico,cd_uo_origine,'Attiva'
 from nazione n1,nazione n3,anagrafico a,terzo z,
 fattura_attiva T,fattura_attiva_intra I,natura_transazione n,codici_cpa s,nomenclatura_combinata b,condizione_consegna c
Where
t.ti_fattura ='F' And
a.partita_iva is not null and
protocollo_iva Is Not Null And
ammontare_euro!=0 And
n1.pg_nazione = i.pg_nazione_destinazione And
n3.pg_nazione = a.pg_nazione_fiscale And
a.cd_anag     = z.cd_anag      And
z.cd_terzo    = t.cd_terzo     And
s.id_cpa(+) = i.id_cpa  and
b.id_nomenclatura_combinata (+) = i.id_nomenclatura_combinata and
n.id_natura_transazione (+) = i.id_natura_transazione And
c.cd_incoterm (+) =i.cd_incoterm And
c.esercizio(+) = i.ESERCIZIO_COND_CONSEGNA And
T.fl_intra_ue  = 'Y' and
t.esercizio = i.esercizio and
t.cd_cds    = i.cd_cds    and
t.cd_unita_organizzativa = i.cd_unita_organizzativa and
t.pg_fattura_attiva = i.pg_fattura_attiva and
I.FL_INVIATO='N' and
T.STATO_COFI!='A'
Union
-- Le cancellazioni vengo inserite con fl_inviato N
select 'PS',t.esercizio,To_Number(To_Char(dt_registrazione,'mm')),t.ti_bene_servizio,t.dt_registrazione,t.nr_fattura_fornitore,To_Char(t.dt_fattura_fornitore,'ddmmyy'),a.partita_iva,n.cd_natura_transazione,b.cd_nomenclatura_combinata,
Round(Nvl(i.ammontare_euro,0),0) ammontare_euro,
Round(Nvl(i.ammontare_divisa,0),0) ammontare_divisa,
Decode(round(Nvl(i.massa_netta,0),0),0,1,round(Nvl(i.massa_netta,0),0)) massa_netta,
Decode(round(Nvl(i.unita_supplementari,0),0),0,1,Round(Nvl(i.unita_supplementari,0),0)) unita_supplementari ,
Round(Nvl(i.valore_statistico,0),0) valore_statistico,
c.cd_gruppo,i.cd_modalita_trasporto,s.cd_cpa,i.cd_modalita_incasso,i.cd_modalita_erogazione,
decode(t.ti_bene_servizio,'B',n1.cd_iso_intrastat,n1.cd_iso) provenienza,n2.cd_iso origine, Null dest,i.cd_provincia_destinazione, Null cd_provincia_origine,t.ti_fattura,n3.cd_iso_intrastat naz_fiscale,
t.cd_cds,t.cd_unita_organizzativa,t.pg_fattura_passiva,i.pg_riga_intra,i.nr_protocollo, i.nr_progressivo,i.pg_storico_ pg_storico ,cd_uo_origine, 'Passiva'
--t.esercizio,t.cd_cds,t.cd_unita_organizzativa,t.pg_fattura_passiva,i.pg_riga_intra
 from nazione n1,nazione n2,nazione n3,anagrafico a,terzo z,
 fattura_passiva T,fattura_passiva_intra_s I,natura_transazione n,codici_cpa s,nomenclatura_combinata b,condizione_consegna c
Where
t.ti_fattura ='F' And
a.partita_iva is not null And
(ammontare_euro!=0 Or ammontare_euro Is Null) And
n1.pg_nazione = i.pg_nazione_provenienza And
n3.pg_nazione = a.pg_nazione_fiscale And
a.cd_anag     = z.cd_anag      And
z.cd_terzo    = t.cd_terzo     And
n2.pg_nazione(+) = i.pg_nazione_origine and
s.id_cpa(+) = i.id_cpa  and
b.id_nomenclatura_combinata (+) = i.id_nomenclatura_combinata and
n.id_natura_transazione (+) = i.id_natura_transazione And
c.cd_incoterm (+) =i.cd_incoterm And
c.esercizio(+) = i.ESERCIZIO_COND_CONSEGNA And
T.fl_intra_ue  = 'Y' and
t.esercizio = i.esercizio and
t.cd_cds    = i.cd_cds    and
t.cd_unita_organizzativa = i.cd_unita_organizzativa and
t.pg_fattura_passiva = i.pg_fattura_passiva and
I.FL_INVIATO='N' and
T.STATO_COFI!='A'
-- Le cancellazioni vengo inserite con fl_inviato N
Union
 Select 'AS',t.esercizio,To_Number(To_Char(t.dt_emissione,'mm')),t.ti_bene_servizio,t.dt_registrazione,To_Char(t.protocollo_iva),To_Char(t.dt_emissione,'ddmmyy'),a.partita_iva,n.cd_natura_transazione,b.cd_nomenclatura_combinata,
Round(Nvl(i.ammontare_euro,0),0) ammontare_euro,
Null ammontare_divisa,
Decode(round(Nvl(i.massa_netta,0),0),0,1,round(Nvl(i.massa_netta,0),0)) massa_netta,
Decode(round(Nvl(i.unita_supplementari,0),0),0,1,Round(Nvl(i.unita_supplementari,0),0)) unita_supplementari ,
0 valore_statistico,
c.cd_gruppo,i.cd_modalita_trasporto,s.cd_cpa,i.cd_modalita_incasso,i.cd_modalita_erogazione,
Null provenienza, Null origine,decode(t.ti_bene_servizio,'B',n1.cd_iso_intrastat,n1.cd_iso) dest,Null cd_provincia_destinazione,i.cd_provincia_origine,t.ti_fattura, n3.cd_iso_intrastat naz_fiscale,
t.cd_cds,t.cd_unita_organizzativa,t.pg_fattura_attiva,i.pg_riga_intra,i.nr_protocollo, i.nr_progressivo,i.pg_storico_ pg_storico ,cd_uo_origine,'Attiva'
 from nazione n1,nazione n3,anagrafico a,terzo z,
 fattura_attiva T,fattura_attiva_intra_s I,natura_transazione n,codici_cpa s,nomenclatura_combinata b,condizione_consegna c
Where
t.ti_fattura ='F' And
protocollo_iva Is Not Null And
(ammontare_euro!=0 Or ammontare_euro Is Null) And
n1.pg_nazione = i.pg_nazione_destinazione And
n3.pg_nazione = a.pg_nazione_fiscale And
a.cd_anag     = z.cd_anag      And
z.cd_terzo    = t.cd_terzo     And
s.id_cpa(+) = i.id_cpa  and
b.id_nomenclatura_combinata (+) = i.id_nomenclatura_combinata and
n.id_natura_transazione (+) = i.id_natura_transazione And
c.cd_incoterm (+) =i.cd_incoterm And
c.esercizio(+) = i.ESERCIZIO_COND_CONSEGNA And
T.fl_intra_ue  = 'Y' and
t.esercizio = i.esercizio and
t.cd_cds    = i.cd_cds    and
t.cd_unita_organizzativa = i.cd_unita_organizzativa and
t.pg_fattura_attiva = i.pg_fattura_attiva and
I.FL_INVIATO='N' and
T.STATO_COFI!='A';
