/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.prevent00.comp;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
public interface IBilancioPreventivoMgr extends ICRUDMgr
{


/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      Viene richiesta l'approvazione del bilancio preventivo.
  *      Lo stato del bilancio preventivo è (B) predisposto o prodotto.
  *    PostCondition:
  *      Viene invocato il controllo di pareggio di bilancio mandatorio (richiesta CINECA/CNR del 04/11/2002)
  *      Lo stato del bilancio preventivo si cambia da 'B' a 'C' approvato.
  *  Stato bilancio preventivo NON è B
  *    PreCondition:
  *      Lo stato del bilancio preventivo NON è B.
  *    PostCondition:
  *      Operazione non consentita.
  *      Throw exception: Lo stato del bilancio preventivo non consente l'approvazione.
 */

public abstract it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk approvaBilancioPreventivo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  normale
  *    PreCondition:
  *      Ricerca del Bilancio di previsione 
  *		 (esercizio di scrivania, cds di scrivania, appartenenza C/D)
  *    PostCondition:
  *      Se il bilancio non esiste Exception
 */

public abstract it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk caricaBilancioPreventivo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesta la ricerca del cd_cds ENTE che servira' per 
  *		 inizializzare il cd_cds del bilancio di previsione CNR
  *    PostCondition:
  *      ritorno il risultato della ricerca nella tabella unita_organizzativa 
  *		 dell'unita' con cd_tipo_unita=ENTE
 */

public abstract it.cnr.jada.bulk.OggettoBulk cercaCdsEnte(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesta la ricerca del cd_cds ENTE che servira' per 
  *		 inizializzare il cd_cds dei dettagli di spesa/entrata
  *		 del bilancio di previsione CNR
  *    PostCondition:
  *      ritorno il risultato della ricerca nella tabella unita_organizzativa 
  *		 dell'unita' con cd_tipo_unita=ENTE
 */

public abstract it.cnr.jada.bulk.OggettoBulk cercaCdsEnte(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  *  Richiesta creazione di dettaglio in bilancio finaniziario CNR ed esiste già il dettaglio per la parte residui
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CNR.
  *      Esiste per tale dettaglio la specifica di dettaglio della parte residui
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *  Creazione di dettaglio in bilancio finaniziario CNR
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CNR.
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Viene creato un nuovo dettaglio
  *      Viene creato un record relativo alla parte residui
  *      Se il dettaglio è di spesa viene aggiornato l'impegno automatico colegato al capitolo
  *
  *  Creazione di dettaglio in bilancio finaniziario CDS con spareggio di bilancio entrate > spese
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il nuovo dettaglio mantiene entrate > spese per il preventivo CDS
  *    PostCondition:
  *      Viene creato un nuovo dettaglio e segnalato lo spareggio all'utente
  *
  *  Creazione di dettaglio in bilancio finaniziario CDS con spareggio di bilancio spese > entrate
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il nuovo dettaglio determina uno spareggio spese > entrate per il preventivo CDS
  *    PostCondition:
  *      Viene Sollevata un'eccezione
  *
  *  Creazione di dettaglio di spesa in bilancio finaniziario CDS con sfondamento del limite del 3% del fondo di riserva
  *    PreCondition:
  *      Viene richiesta la creazione di un dettaglio di spesa del bilancio preventivo CDS.
  *      Dopo la modifica il totale del funzionamento supera il 3% del fondo di riserva
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *  Creazione di dettaglio in bilancio finaniziario CDS con spareggio di bilancio entrate > spese
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il nuovo dettaglio determina uno spareggio spese < entrate per il preventivo CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  *
  *  Creazione di dettaglio in bilancio finaniziario CDS con entrata CDS > delle spese corrispondenti in bilancio CNR
  *    PreCondition:
  *      Viene richiesta la creazione di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il nuovo dettaglio determina uno spareggio spese CNR corrispondenti entrata CDS < entrata CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta la creazione degli stanziamenti iniziali del bilancio preventivo di un singolo CdS.
  *    PostCondition:
  *      La creazione degli stanziamenti iniziali del bilancio preventivo di un singolo CdS richiede l'aggregazione per 'Rubrica' dei PdG che appartengono al CdS. Il risultato di questa aggregazione (prodotta/specificata nel metodo aggregaPdGPerRubrica) viene utilizzato per la scrittura degli saldi corrispondenti nella tabella VOCE_F_SALDI_CMP. La procedura ORACLE predisponeBilFinCDS(esercizio, cds, utente) esegue quest'attività.
 */

public abstract void creaStanziamentiInizialiCdS(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta la creazione degli stanziamenti iniziali del bilancio preventivo del CNR.
  *    PostCondition:
  *      La creazione degli stanziamenti iniziali del bilancio preventivo del CNR richiede la lettura delle righe delle tabelle PDG_AGGREGATO* e la scrittura degli saldi corrispondenti nella tabella VOCE_F_SALDI_CMP. La procedura ORACLE predisponeBilFinCNR(esercizio, utente) esegue quest'attività.
 */

public abstract void creaStanziamentiInizialiCNR(it.cnr.jada.UserContext param0,short param1) throws it.cnr.jada.comp.ComponentException;
/**
  *  Controparte residui non trovata per dettaglio di competenza CNR in modifica
  *    PreCondition:
  *      Dettaglio di parte residui non trovato in corrispondenza del dettaglio CNR di competenza in modifica
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *  Richiesta di inizializzazione del dettaglio CNR per modifica
  *    PreCondition:
  *      Viene richiesta l'inizializzazione di un dettaglio del bilancio finanziario CNR
  *    PostCondition:
  *      Viene inizializzato il dettaglio parte competenza per la modifica
  *      Viene caricato automaticamente il dettaglio residui collegato
  *
  *  Richiesta di inizializzazione del dettaglio CDS per modifica
  *    PreCondition:
  *      Viene richiesta l'inizializzazione di un dettaglio del bilancio finanziario CDS
  *    PostCondition:
  *      Viene inizializzato il dettaglio per la modifica
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  *  Modifica di dettaglio in bilancio finaniziario CNR di spesa
  *    PreCondition:
  *      Viene richiesta la modifica di un dettaglio di bilancio preventivo CNR.
  *      Il dettaglio è di spesa
  *    PostCondition:
  *      Viene aggiornato l'impegno automatico collegato al capitolo
  *      Viene aggiornato il saldo parte competenza
  *
  *  Modifica di dettaglio in bilancio finaniziario CDS con spareggio di bilancio spese > entrate
  *    PreCondition:
  *      Viene richiesta la modifica di un dettaglio di bilancio preventivo CDS.
  *      Il dettaglio modificato determina uno spareggio spese > entrate per il preventivo CDS
  *    PostCondition:
  *      Viene Sollevata un'eccezione
  *
  *  Modifica di dettaglio in bilancio finaniziario CDS con spareggio di bilancio entrate > spese
  *    PreCondition:
  *      Viene richiesta la modifica di un dettaglio de spesa del bilancio preventivo CDS.
  *      Il nuovo dettaglio modificato determina uno spareggio spese < entrate per il preventivo CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  *
  *  Modifica di dettaglio di spesa in bilancio finaniziario CDS con sfondamento del limite del 3% del fondo di riserva
  *    PreCondition:
  *      Viene richiesta la modifica di un dettaglio di bilancio preventivo CDS.
  *      Dopo la modifica il totale del funzionamento supera il 3% del fondo di riserva
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *  Modifica di dettaglio in bilancio finaniziario CDS con spareggio di bilancio entrate > spese
  *    PreCondition:
  *      Viene richiesta la modifica di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il dettaglio modificato determina uno spareggio spese < entrate per il preventivo CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  *
  *  Modifica di dettaglio in bilancio finaniziario CDS con entrata CDS > delle spese corrispondenti in bilancio CNR
  *    PreCondition:
  *      Viene richiesta la modifica di un nuovo dettaglio di bilancio preventivo CDS.
  *      Il dettaglio modificato determina uno spareggio spese CNR corrispondenti entrata CDS < entrata CDS
  *    PostCondition:
  *      Viene creato il dettaglio e segnalato lo spareggio all'utente
  *
  *  Modifica di dettaglio caso generale
  *    PreCondition:
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Viene aggiornato il saldo parte competenza
  */


public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la predisposizione del bilancio finanziario CDS
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB055.predisponeBilFinCDS
 */

public abstract it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk predisponeBilancioPreventivoCdS(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta la produzione del bilancio finanziario CNR
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB055.predisponeBilFinCNR
 */

public abstract it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk predisponeBilancioPreventivoCNR(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
