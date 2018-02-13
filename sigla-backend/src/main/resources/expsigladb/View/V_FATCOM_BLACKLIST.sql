--------------------------------------------------------
--  DDL for View V_FATCOM_BLACKLIST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_FATCOM_BLACKLIST" ("TIPO", "ESERCIZIO", "MESE", "IMPONIBILE", "IVA", "IMP_ESENTE", "IMP_NON_IMP", "IMP_NON_SOGGETTO", "DS_NAZIONE", "CD_NAZIONE", "BENE_SERVIZIO", "CODICE_FISCALE", "ID_FISCALE_ESTERO", "CD_TERZO", "PERSONA", "NOME", "COGNOME", "RAGIONE_SOCIALE", "COMUNE_NASCITA", "DT_NASCITA", "CD_PROVINCIA", "ITALIANO_ESTERO", "STATO_NASCITA", "INDIRIZZO_SEDE", "COMUNE_SEDE") AS 
  select tipo,esercizio,mese,sum(IMP), sum(IVA), sum(IMP_ESENTE), sum( IMP_NON_IMP), sum(IMP_NON_SOGGETTO),    DS_NAZIONE,    cd_nazione_770,BENE_SERVIZIO,CODICE_FISCALE,
ID_FISCALE_ESTERO,CD_TERZO,PERSONA,    NOME,    COGNOME,    RAGIONE_SOCIALE,
COMUNE_NASCITA,DT_NASCITA,CD_PROVINCIA,ITALIANO_ESTERO,STATO_NASCITA,
INDIRIZZO_SEDE,COMUNE_SEDE
from
(SELECT 'A' tipo,t.esercizio,To_Number(To_Char(t.dt_emissione,'mm')) mese, round(sum(nvl(
decode(fl_esente,'Y',0,
decode(fl_non_imponibile,'Y',0,
decode(fl_non_soggetto,'Y',0,
decode(t.ti_fattura,'C',-im_imponibile,im_imponibile)))),0)),0)  imp,
round(sum(nvl(decode(t.ti_fattura,'C',-im_iva,im_iva),0)),0) iva,
round(SUM(NVL(decode(fl_esente,'Y',nvl(decode(t.ti_fattura,'C',-im_imponibile,im_imponibile),0),0),0)),0) imp_esente,
round(SUM(NVL(decode(fl_non_imponibile,'Y',nvl(decode(t.ti_fattura,'C',-im_imponibile,im_imponibile),0),0),0)),0) imp_non_imp,
round(SUM(NVL(decode(fl_non_soggetto,'Y',nvl(decode(t.ti_fattura,'C',-im_imponibile,im_imponibile),0),0),0)),0) imp_non_soggetto,
b.ds_nazione,nazione.cd_nazione_770,bene_servizio.ti_bene_Servizio BENE_SERVIZIO,
decode(n.ti_italiano_estero,'I',anagrafico.codice_fiscale,  null) codice_fiscale,
id_fiscale_estero,t.cd_terzo,ti_entita PERSONA,anagrafico.nome,anagrafico.cognome,anagrafico.ragione_sociale,
n.ds_COMUNE COMUNE_NASCITA,dt_nascita,n.cd_provincia,n.ti_italiano_estero ITALIANO_ESTERO,naz.ds_nazione STATO_NASCITA,
ltrim(via_sede)||' '||numero_civico_sede  INDIRIZZO_SEDE, comres.ds_comune comune_sede
 from fattura_attiva_riga f,fattura_attiva t,terzo,anagrafico,nazione,comune ,bene_servizio,voce_iva,nazione_blacklist b,COMUNE n,nazione naz,comune comres
,configurazione_cnr c
where
t.esercizio = f.esercizio and
t.cd_cds    = f.cd_cds and
t.cd_unita_organizzativa = f.cd_unita_organizzativa and
t.pg_fattura_attiva = f.pg_fattura_attiva and
f.cd_bene_servizio = bene_servizio.cd_bene_servizio and
f.stato_cofi!='A' and
t.protocollo_iva is not null and
f.cd_voce_iva = voce_iva.cd_voce_iva and
t.cd_terzo=terzo.cd_terzo and
terzo.cd_anag = anagrafico.cd_anag and
N.PG_COMUNE =nvl(ANAGRAFICO.PG_COMUNE_NASCITA,anagrafico.pg_comune_fiscale) and
naz.pg_nazione =n.pg_nazione and
pg_comune_sede = comres.pg_comune and
(pg_comune_sede   = comune.pg_comune or
pg_comune_fiscale = comune.pg_comune) and
nazione.pg_nazione = comune.pg_nazione and
nazione.cd_nazione_770 =b.cd_nazione and
b.esercizio= f.esercizio
and (im_imponibile!=0 or im_iva!=0) and
c.esercizio = 0 and
c.cd_chiave_primaria = 'COSTANTI'  and
c.cd_chiave_secondaria = 'BLACKLIST'   and
c.cd_unita_funzionale = '*'    and
(((im_totale_imponibile+im_totale_iva > nvl(c.im01,0))   and
 dt_emissione >=nvl(c.dt01,dt_emissione)) or
 dt_emissione < nvl(c.dt01,dt_emissione))
group by 'Attive',t.esercizio,To_Number(To_Char(t.dt_emissione,'mm')),
b.ds_nazione,nazione.cd_nazione_770,bene_servizio.ti_bene_Servizio ,
decode(n.ti_italiano_estero,'I',anagrafico.codice_fiscale,  null),
id_fiscale_estero ,t.cd_terzo,
ti_entita,anagrafico.nome,anagrafico.cognome,anagrafico.ragione_sociale,
n.ds_COMUNE,dt_nascita,n.cd_provincia,n.ti_italiano_estero,naz.ds_nazione,
ltrim(via_sede)||' '||numero_civico_sede, comres.ds_comune
union
select 'P' tipo, t.esercizio,To_Number(To_Char(dt_registrazione,'mm')) mese,
round(sum(nvl(
decode(fl_esente,'Y',0,
decode(fl_non_imponibile,'Y',0,
decode(fl_non_soggetto,'Y',0,
decode(t.ti_fattura,'C',-im_imponibile,im_imponibile)))),0)),0)  imp,
round(sum(nvl(decode(t.ti_fattura,'C',-im_iva,im_iva),0)),0) iva,
round(SUM(NVL(decode(fl_esente,'Y',nvl(decode(t.ti_fattura,'C',-im_imponibile,im_imponibile),0),0),0)),0) imp_esente,
round(SUM(NVL(decode(fl_non_imponibile,'Y',nvl(decode(t.ti_fattura,'C',-im_imponibile,im_imponibile),0),0),0)),0) imp_non_imp,
round(SUM(NVL(decode(fl_non_soggetto,'Y',nvl(decode(t.ti_fattura,'C',-im_imponibile,im_imponibile),0),0),0)),0) imp_non_soggetto,
b.ds_nazione,nazione.cd_nazione_770,
t.ti_bene_Servizio BENE_SERVIZIO,
decode(n.ti_italiano_estero,'I',anagrafico.codice_fiscale,  null) codice_fiscale,
id_fiscale_estero ,t.cd_terzo,ti_entita PERSONA,
anagrafico.nome,anagrafico.cognome,anagrafico.ragione_sociale,
n.ds_COMUNE COMUNE_NASCITA,dt_nascita,n.cd_provincia,n.ti_italiano_estero ITALIANO_ESTERO,naz.ds_nazione STATO_NASCITA,
ltrim(via_sede)||' '||numero_civico_sede INDIRIZZO_SEDE, comres.ds_comune comune_sede
from fattura_passiva_riga f,fattura_passiva t,terzo,anagrafico,nazione,comune ,voce_iva,nazione_blacklist b,COMUNE n,nazione naz,comune comres
,configurazione_cnr c
where
t.esercizio = f.esercizio and
t.cd_cds    = f.cd_cds and
t.cd_unita_organizzativa = f.cd_unita_organizzativa and
t.pg_fattura_passiva = f.pg_fattura_passiva and
f.stato_cofi!='A' and
t.ti_istituz_commerc ='C' and
t.protocollo_iva is not null and
voce_iva.cd_voce_iva = f.cd_voce_iva and
t.cd_terzo=terzo.cd_terzo and
terzo.cd_anag = anagrafico.cd_anag and
pg_comune_sede = comres.pg_comune and
N.PG_COMUNE =nvl(ANAGRAFICO.PG_COMUNE_NASCITA,anagrafico.pg_comune_fiscale) and
naz.pg_nazione =n.pg_nazione and
(pg_comune_sede   = comune.pg_comune or
pg_comune_fiscale = comune.pg_comune) and
nazione.pg_nazione = comune.pg_nazione and
nazione.cd_nazione_770 =b.cd_nazione and
b.esercizio= f.esercizio    and
(im_imponibile!=0 or im_iva!=0)  and
c.esercizio =0 and
c.cd_chiave_primaria = 'COSTANTI'  and
c.cd_chiave_secondaria = 'BLACKLIST'   and
c.cd_unita_funzionale = '*'    and
(((im_totale_imponibile+im_totale_iva > nvl(c.im01,0))   and
 dt_registrazione >=nvl(c.dt01,dt_registrazione)) or
 dt_registrazione < nvl(c.dt01,dt_registrazione))
group by 'Passive',t.esercizio,To_Number(To_Char(dt_registrazione,'mm')),b.ds_nazione,nazione.cd_nazione_770,
t.ti_bene_Servizio,
decode(n.ti_italiano_estero,'I',anagrafico.codice_fiscale,  null),
id_fiscale_estero,t.cd_terzo,
ti_entita,anagrafico.nome,anagrafico.cognome,anagrafico.ragione_sociale,
n.ds_COMUNE,dt_nascita,n.cd_provincia,n.ti_italiano_estero,naz.ds_nazione,
ltrim(via_sede)||' '||numero_civico_sede, comres.ds_comune )
--where esercizio = 2012
group by tipo,esercizio,mese,    DS_NAZIONE,    cd_nazione_770,BENE_SERVIZIO,CODICE_FISCALE,
ID_FISCALE_ESTERO,CD_TERZO,PERSONA,    NOME,    COGNOME,    RAGIONE_SOCIALE,
COMUNE_NASCITA,DT_NASCITA,CD_PROVINCIA,ITALIANO_ESTERO,STATO_NASCITA,
INDIRIZZO_SEDE,COMUNE_SEDE
having (sum(IMP)!=0 or  sum(IVA)!=0 or  sum(IMP_ESENTE)!=0 or     sum(IMP_NON_IMP)!=0 or     sum(IMP_NON_SOGGETTO) !=0 );
