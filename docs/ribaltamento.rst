**APERTURA DELLA PREVISIONE E RIBALTAMENTI**

PER INIZIARE A LAVORARE NELL’ANNO NUOVO:
Lanciare il Primo Ribaltamento (script) non in procedura:
Ribaltamento configurazione, struttura organizzativa, anagrafica dei capitoli e piano dei conti E/P per il PDGP  (lanciato 1 sola volta)  
(script “scriptRibaltamentoPerPDGP.sql”)

TABELLE:
========

ESERCIZIO_BASE = 2020
PARAMETRI_CNR PER L’ANNO 2020/CONFIGURAZIONI
ESERCIZIO PDGP (PER OGNI CDS CON STATO = I viene creato dallo script di ribaltamento) 
Anagrafica voci e Classificazioni
…….
 
Da UO 999 aprire il PdGP e successivamente:
Ogni Istituto dovrebbe da PROCEDURA passare allo stato G (APERTURA PREVISIONE) 
ENTRARE IN PDGP PER INIZIARE LA PREVISIONE

Spese Accentrate
================

Da procedura è possibile per la Sede Centrale (da UO 999)  inserire, per voce di bilancio e per CDS, le spese accentrate. Ogni Istituto, una volta reso definitivo il Piano di Riparto da parte della Sede Centrale, può ripartirsi le spese assegnate al proprio CDS ai progetti definiti in Previsione. 
Per inserire, ricercare prima la classificazione e poi aggiungere dettagli CDS/Importi

Costi del Personale
===================

La sala macchine effettua lo scarico costi del personale a fine settembre e popola le tabelle CNR_COSTO_PREV e CNR_ANADIP;
Successivamente vanno caricate le tabelle di previsione da cui gli Istituti si scaricano i costi del personale (COSTO_DEL_DIPENDENTE):
La procedura da lanciare è PCIR009.POPOLAMENTO_COSTO_DIP_PREV
 ** execute POPOLAMENTO_COSTO_DIP_PREV(2021,0)
Dopo il caricamento bisogna fare un controllo di coerenza delle voci utilizzate(lanciando la select sotto) ed eventualmente sistemare qualche dato!

select elemento_voce.cd_elemento_voce,ds_elemento_voce,
sum(IM_A1+IM_ONERI_CNR_A1+IM_TFR_A1) tot_anno1,TI_RAPPORTO,ORIGINE_FONTI,FL_RAPPORTO13
from pcir009.elemento_voce,pcir009.costo_del_dipendente
where
elemento_voce.esercizio = costo_del_dipendente.esercizio   and
elemento_voce.ti_appartenenza = costo_del_dipendente.ti_appartenenza  and
elemento_voce.ti_gestione = costo_del_dipendente.ti_gestione and
elemento_voce.cd_elemento_voce =  costo_del_dipendente.cd_elemento_voce   and
costo_del_dipendente.esercizio=nuovo esercizio and  mese=0
group by elemento_voce.cd_elemento_voce,ds_elemento_voce,TI_RAPPORTO,ORIGINE_FONTI,FL_RAPPORTO13
order by elemento_voce.cd_elemento_voce,ds_elemento_voce,TI_RAPPORTO,ORIGINE_FONTI,FL_RAPPORTO13

I dati del personale non si trovano con quelli preventivi stanziati dall’ufficio del personale (…) ma questi dati in Sigla servono solo all’ufficio bilancio per creare la suddivisione della previsione per il personale a tempo indeterminato e determinato, tra le 7 Gae che equivalgono ai sette dipartimenti del CNR. In questo modo la percentuale di grandezza della previsione per dipartimento (quindi importo per Gae), ripartisce la previsione vera (quella indicata dall’ufficio Personale) tra le Gae-Dipartimento.
Una volta che i dati sono verificati (almeno per le voci) bisogna aggiornare gli importi, gli importi devono essere messi tutti nel TFR:

UPDATE
======

Update costo_del_dipendente set IM_TFR_A1= IM_TFR_A1+ IM_A1+ IM_ONERI_CNR_A1, IM_TFR_A2= IM_TFR_A2+ IM_A2+ IM_ONERI_CNR_A2,
IM_TFR_A3= IM_TFR_A3+ IM_A3+ IM_ONERI_CNR_A3
Where esercizio=nuovo esercizio and  mese=0;

Update costo_del_dipendente set IM_A1 = 0, IM_A2 = 0, IM_A3=0, IM_ONERI_CNR_A1=0, IM_ONERI_CNR_A2=0, IM_ONERI_CNR_A3=0  
Where esercizio=nuovo esercizio and  mese=0;


Aggiornamento voci
==================

Aggiornare fl_accentrato a No per le voci del personale per evitare che indichino erroneamente la previsione:
update classificazione_voci set fl_accentrato='N' 
where  id_classificazione in(select id_classificazione from elemento_voce
where
esercizio = &esercizio_new and
fl_voce_personale='Y');

Ribaltamento Progetti
=====================

Al primo ribaltamento deve essere previsto per tutti i progetti con piano economico già esistente per l’anno, l’inserimento delle associazioni per le categorie con voci automatiche PER_TD,PER_TI,PER_ALTRO, TRASFERIME.
Risoluzione del problema:
Saranno ribaltate tutte le voci di bilancio ‘associate automaticamente’ alle categorie del piano economico dei progetti, per tutti i progetti che nel piano economico avevano gestito l’anno nuovo (sia che avessero tali categorie anche nel 2019 sia che le avessero solo nel 2020). In questo modo (dopo il ribaltamento dell’anagrafica voci) vengono ricreate le associazioni automatiche perché in questi casi l’utente non può associare voci autonomamente mentre l’inserimento della previsione controlla che sul piano economico di progetto le classificazioni, e poi le voci, sono presenti.
INSERT INTO ASS_PROGETTO_PIAECO_VOCE
   (PG_PROGETTO, CD_UNITA_ORGANIZZATIVA, CD_VOCE_PIANO, ESERCIZIO_PIANO, ESERCIZIO_VOCE, TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, DACR, UTCR, DUVA, UTUV, PG_VER_REC)
SELECT A.PG_PROGETTO, B.CD_UNITA_PIANO, B.CD_VOCE_PIANO, A.ESERCIZIO_PIANO, 
	   B.ESERCIZIO, B.TI_APPARTENENZA, B.TI_GESTIONE, B.CD_ELEMENTO_VOCE, TRUNC(SYSDATE), 'SYSTEM', TRUNC(SYSDATE), 'SYSTEM', 0
FROM PROGETTO_PIANO_ECONOMICO A, ELEMENTO_VOCE B, VOCE_PIANO_ECONOMICO_PRG C
WHERE A.CD_UNITA_ORGANIZZATIVA = C.CD_UNITA_ORGANIZZATIVA
AND   A.CD_VOCE_PIANO = C.CD_VOCE_PIANO
AND   B.CD_UNITA_PIANO = C.CD_UNITA_ORGANIZZATIVA
AND   B.CD_VOCE_PIANO = C.CD_VOCE_PIANO
AND   A.ESERCIZIO_PIANO = B.ESERCIZIO
AND   C.FL_ADD_VOCIBIL = 'N'
AND   A.ESERCIZIO_PIANO = 2020
--AND   A.PG_PROGETTO=26007
AND   NOT EXISTS(SELECT 1 FROM ASS_PROGETTO_PIAECO_VOCE D
                 WHERE D.PG_PROGETTO = A.PG_PROGETTO
                 AND   D.CD_UNITA_ORGANIZZATIVA = A.CD_UNITA_ORGANIZZATIVA          
                 AND   D.CD_VOCE_PIANO = A.CD_VOCE_PIANO          
                 AND   D.ESERCIZIO_PIANO = A.ESERCIZIO_PIANO)

Rimodulazioni
=============

Lo stesso problema visto per i Progetti riguarda anche le Rimodulazioni. Bisogna infatti gestire le voci per il nuovo anno (sempre per le categorie automatiche) legate a rimodulazioni che andranno a creare sulla scheda progetto (in caso di approvazione rimodulazione) automaticamente le associazioni vategoria/voci.

SECONDO RIBALTAMENTO
====================
Altro
Ad inizio del nuovo anno contabile (gennaio) viene lanciato il secondo script per i ribaltamenti definitivi (uleriori anagrafiche create nell’anno precedente ….)

dopo il secondo ribaltamento devono essere riaggiornati (inizio Gennaio 2020)
update classificazione_voci set fl_accentrato='Y' 
where  id_classificazione in(select id_classificazione from elemento_voce
where
esercizio = &esercizio_new and
fl_voce_personale='Y'); 



Numeratori Impegni Impropri
Non c’è più bisogno di aggiornarli perché è stata fatta la modifica nel ribaltamento: Aggiunge al numeratore anno precedente 10.000 quindi va bene per il nuovo anno.

Cambio stato esercizio nuovo anno;
Verificare se ci sono in sospeso Missioni o compensi (cancellare)
Verificare se ci sono Impegni da ribaltare senza STATO
Effettuare i ribaltamenti;
 


script di ribaltamento (secondo ribaltamento) 
 
DECLARE 
  AES NUMBER;
  AMESSAGE VARCHAR2(200);
BEGIN 
  AES := 2020;
  AMESSAGE := NULL;
 
  PCIR009.CNRMIG100.INIT_RIBALTAMENTO_ALTRO ( AES, AMESSAGE );
  COMMIT; 
END;
 
Inserimento UO non ribaltata
============================

Solo per inserire la UO 000 non ribaltata perchè aveva esercizio_fine = anno precedente.
 
Insert into PCIR009.ESERCIZIO
   (CD_CDS, ESERCIZIO, DS_ESERCIZIO, ST_APERTURA_CHIUSURA, DACR, 
    UTCR, DUVA, UTUV, PG_VER_REC, IM_CASSA_INIZIALE)
Values
   ('000', 2020, 'Esercizio contabile 2020', ‘G’, TO_DATE('10/10/2019 17:20:12', 'MM/DD/YYYY HH24:MI:SS'), 
    '$$$$RIBALTAMENTO$$$$', TO_DATE('10/10/2019 17:20:12', 'MM/DD/YYYY HH24:MI:SS'), '$$$$RIBALTAMENTO$$$$', 1, 0);
COMMIT;
 
 
 
Update pcir009.esercizio set ST_APERTURA_CHIUSURA =’A’ 
Where esercizio = nuovo anno
  
Entrare sulla uo 000.407 nel 2020 e lanciare la liquidazione massiva da esercizio precedente periodo 01/01/2019 a 31/12/2019!

PROBLEMA DI ACCESSO INTRANET-SIGLA
==================================

E’ stata messa una pezza a colori per poter accedere direttamente dalla Intranet a Sigla (e bypassare la richiesta di accesso a Sigla). Chiedere a Marco
Per l’anno prossimo bisogna risolvere gestendo il Single Sign On oppure lasciando separati i due accessi e dicendolo in tempo sui documenti di Previsione inviati agli utenti
