--------------------------------------------------------
--  DDL for Package CNRCTB002
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB002" as
--
-- CNRCTB002 - Package di gestione tabella VOCE_EP
-- Date: 17/11/2004
-- Version: 2.10
--
-- Dependency: CNRCTB 001/015 IBMERR 001
--
-- History:
--
-- Date: 05/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 27/01/2002
-- Version: 1.1
-- Lettura ordinata per codice voce in metodo coie_XXX
--
-- Date: 21/05/2002
-- Version: 1.2
-- Aggiunti metodi di estrazione conti
--
-- Date: 22/05/2002
-- Version: 1.3
-- Nuovi getter conti speciali
--
-- Date: 31/05/2002
-- Version: 1.4
-- Aggiunta banca cds
--
-- Date: 31/05/2002
-- Version: 1.5
-- Aggiunta insussistenze creadito + perdita/utile su cambio
--
-- Date: 01/07/2002
-- Version: 1.6
-- Recupero dei conti di costo e ricavo per registrazione su partite di giro
--
-- Date: 04/07/2002
-- Version: 1.7
-- Aggiunti i conti per registrazione economica IVA
--
-- Date: 11/07/2002
-- Version: 1.8
-- Fix errore di recupero conti economici su partite di giro
--
-- Date: 18/07/2002
-- Version: 1.9
-- Aggiornamento documentazione
--
-- Date: 17/10/2002
-- Version: 2.0
-- Aggiunti nuovi conti speciali: crediti iniziali, stato patr. iniziale, fatture da emettere
--
-- Date: 08/11/2002
-- Version: 2.1
-- Aggiunti nuovo conto speciale: debiti iniziali
--
-- Date: 07/07/2003
-- Version: 2.2
-- Aggiunti estrattori conti speciali risconti attivi e passivi
--
-- Date: 08/07/2003
-- Version: 2.3
-- Aggiunti estrattori conti speciali ratei attivi e passivi, sopravv attive/passive
-- e debiti crediti diversi per partite di giro
--
-- Date: 12/07/2003
-- Version: 2.4
-- Aggiunto estrattore del conto economico di ProfittiPerdite
--
-- Date: 14/07/2003
-- Version: 2.5
-- Eliminato conto economico profitti/perdite e aggiunti Conto Economico Stato Patrimoniale
-- e Utile Perdita d'Esercizio
--
-- Date: 19/02/2004
-- Version: 2.6
-- Estrazione conto voce ep fatture da ricevere
--
-- Date: 26/02/2004
-- Version: 2.7
-- Estrazione conto/ricavo iva non detraibile
--
-- Date: 05/03/2004
-- Version: 2.8
-- Aggiunto il conto delle commissioni bancarie
--
-- Date: 12/11/2004
-- Version: 2.9
-- Aggiuta selezione da CONFIGURAZIONE_CNR per recupero conto di contropartita migrazione beni
--
-- Date: 17/11/2004
-- Version: 2.10
-- Aggiuta selezione da CONFIGURAZIONE_CNR per recupero conto patrimonio netto (COGE trasferimento beni)
--
-- Constants:

-- Functions e Procedures:

-- Copia dati interesercizio
-- aEsDest -> row type esercizio di destinazione
--            dal rowtype vengono letti anche utuvu/utcr/duva/dacr

 procedure coie_VOCE_EP(aEsDest esercizio%rowtype);

 -- Estrae un conto per chiave
 function getVoceEp(aEs number, aCdVoce varchar2) return voce_ep%rowtype;

 -- Estrae la descrizionE di un conto per chiave
 function getDesVoceEp(aEs number, aCdVoce varchar2) return VARCHAR;

 -- Estrae la descrizionE del tipo_doc_amm
 function getDesTipoDocAmm(aCd_tipo_documento_amm VARCHAR2) return VARCHAR;

 -- Estrae la descrizionE del tipo_doc_amm
 function getDesTipoDocCont(aCd_tipo_documento_cont VARCHAR2) return VARCHAR;


 -- Estrae il conto speciale di minusvalenza da config cnr
 function getVoceEpMinusval(aEs number, cat varchar2, voce varchar2) return voce_ep%rowtype;
 -- Estrae il conto speciale di plusvalenza da config cnr
 function getVoceEpPlusval(aEs number, cat varchar2, voce varchar2) return voce_ep%rowtype;
 -- Estrae il conto speciale di sconto da config cnr
 function getVoceEpScontoAbbCosto(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale di abbuono da config cnr
 function getVoceEpScontoAbbRicavo(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale banca da config cnr
 function getVoceEpBanca(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale banca da config cnr
 function getVoceEpCassa(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale anticipo missioni
 function getVoceEpAnticipoMissione(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale banca cds
 function getVoceEpBancaCds(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale insussistenze credito
 function getVoceEpInsussistenzeCredito(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale perdite su cambi
 function getVoceEpPerditaSuCambio(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale perdite su cambi
 function getVoceEpUtileSuCambio(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale erario c/iva
 function getVoceEpErarioCIva(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale iva debito
 function getVoceEpIvaDebito(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale iva credito
 function getVoceEpIvaCredito(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale di contropartita per coge migrazione beni
 function getVoceEpMigraBeni(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale di patrimonio netto
 function getVoceEpPatrimNetto(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale di CONTROPARTITA REVERSALI DI REGOLARIZZAZIONE SU PGIRO
 function getVoceCONTR_REV_REG_PGIRO(aEs number) return voce_ep%rowtype;
-- conto di appoggio per giroconto uo - uo vers.
function getVoceEpErarioCIvaGirocontoUO(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale crediti iniziali
 function getVoceEpCreditiIniziali(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale debiti iniziali
 function getVoceEpDebitiIniziali(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale stato patrimoniale iniziale
 function getVoceEpStatoPatrIniziale(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale fattura da emettere
 function getVoceEpFattureDaEmettere(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale fattura da ricevere
 function getVoceEpFattureDaRicevere(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale commissioni bancarie
 function getVoceEpCommissioniBanca(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale costo iva non detraibile
 function getVoceEpCostoIvaNonDetraibile(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale ricavo iva non detraibile
 function getVoceEpRicIvaNonDetraibile(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale risconti attivi
 function getVoceEpRiscontiAttivi(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale risconti passivi
 function getVoceEpRiscontiPassivi(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale ratei attivi
 function getVoceEpRateiAttivi(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale ratei passivi
 function getVoceEpRateiPassivi(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale sopravvenienze attive
 function getVoceEpSopravvAttive(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale sopravvenienze passive
 function getVoceEpSopravvPassive(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale INSUSSISTENZE attive
 function getVoceEpInsussAttive(aEs number) return voce_ep%rowtype;
 -- Estrae il conto speciale INSUSSISTENZE passive
 function getVoceEpInsussPassive(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale debiti/crediti su terzo diversi per partite giro
 function getVoceEpCredDiversiPgiro(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale debiti/crediti su terzo diversi per partite giro
 function getVoceEpDebDiversiPgiro(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale chiusura conto economico
 function getVoceEpContoEconomico(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale chiusura stato patrimoniale
 function getVoceEpStatoPatrimoniale(aEs number) return voce_ep%rowtype;

 -- Estrae il conto speciale utile perdita d'esercizio
 function getVoceEpUtilePerditaEsercizio(aEs number) return voce_ep%rowtype;

 -- Estrae le voci ep relative al CORI IVA DEBITO

 function getVoceEpRimbMutuoEsercizio(aEs number) return voce_ep%rowtype;
 -- Recupera le voci economiche di costo e ricavo per la registrazione economida su pgiro
 procedure getVociEpPgiro(aEs number, aTiAppartenenza char, aTiGestione char, aCdEv varchar2, aCosto out voce_ep%rowtype, aRicavo out voce_ep%rowtype);
end;
