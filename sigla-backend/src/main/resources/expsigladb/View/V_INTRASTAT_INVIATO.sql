--------------------------------------------------------
--  DDL for View V_INTRASTAT_INVIATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INTRASTAT_INVIATO" ("ESERCIZIO", "MESE", "TIPO", "UO", "PROGRESSIVO", "DT_REGISTRAZIONE", "NR_FATTURA", "PARTITA_IVA", "TI_BENE_SERVIZIO", "CODICE", "AMMONTARE_EURO", "PROTOCOLLO_INVIO", "RIGA_INVIO") AS 
  select t.esercizio,To_Number(To_Char(dt_registrazione,'mm')),'Fattura Passiva',cd_uo_origine, t.pg_fattura_passiva ,t.dt_registrazione,
t.nr_fattura_fornitore,a.partita_iva,
decode(t.ti_bene_servizio,'B','Beni','Servizi'), decode(t.ti_bene_servizio,'B',b.cd_nomenclatura_combinata,s.cd_cpa),
Round(Nvl(i.ammontare_euro,0),0),i.nr_protocollo protocollo_invio, i.nr_progressivo riga_invio
 from anagrafico a,terzo z,
 fattura_passiva T,fattura_passiva_intra I,codici_cpa s,nomenclatura_combinata b
Where
t.ti_fattura ='F' And
a.partita_iva is not null And
ammontare_euro!=0 And
a.cd_anag     = z.cd_anag      And
z.cd_terzo    = t.cd_terzo     And
s.id_cpa(+) = i.id_cpa  and
b.id_nomenclatura_combinata (+) = i.id_nomenclatura_combinata and
T.fl_intra_ue  = 'Y' and
t.esercizio = i.esercizio and
t.cd_cds    = i.cd_cds    and
t.cd_unita_organizzativa = i.cd_unita_organizzativa and
t.pg_fattura_passiva = i.pg_fattura_passiva and
I.FL_INVIATO='Y' and
i.nr_protocollo is not null and
i.nr_progressivo is not null and
T.STATO_COFI!='A'
union
select t.esercizio,To_Number(To_Char(t.dt_emissione,'mm')),'Fattura Attiva',cd_uo_origine, t.pg_fattura_attiva,t.dt_registrazione,
To_Char(t.protocollo_iva),a.partita_iva,
decode(t.ti_bene_servizio,'B','Beni','Servizi'), decode(t.ti_bene_servizio,'B',b.cd_nomenclatura_combinata,s.cd_cpa),
Round(Nvl(i.ammontare_euro,0),0),i.nr_protocollo protocollo_invio, i.nr_progressivo riga_invio
 from anagrafico a,terzo z,
 fattura_attiva T,fattura_attiva_intra I,codici_cpa s,nomenclatura_combinata b
Where
t.ti_fattura ='F' And
a.partita_iva is not null And
ammontare_euro!=0 And
a.cd_anag     = z.cd_anag      And
z.cd_terzo    = t.cd_terzo     And
s.id_cpa(+) = i.id_cpa  and
b.id_nomenclatura_combinata (+) = i.id_nomenclatura_combinata and
T.fl_intra_ue  = 'Y' and
t.esercizio = i.esercizio and
t.cd_cds    = i.cd_cds    and
t.cd_unita_organizzativa = i.cd_unita_organizzativa and
t.pg_fattura_attiva = i.pg_fattura_attiva and
I.FL_INVIATO='Y' and
i.nr_protocollo is not null and
i.nr_progressivo is not null and
T.STATO_COFI!='A'
order by protocollo_invio,riga_invio;
