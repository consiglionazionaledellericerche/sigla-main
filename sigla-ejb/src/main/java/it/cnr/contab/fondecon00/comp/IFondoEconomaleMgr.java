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

package it.cnr.contab.fondecon00.comp;

/**
 * Insert the type's description here.
 * Creation date: (30/01/2002 15.42.05)
 * @author: Luca Bessi
 */
public interface IFondoEconomaleMgr extends it.cnr.jada.comp.ICRUDMgr {
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di associazione di tutte le spese del fondo
  *   	PostCondition:
  *  		Collega l'obbligazione scadenza scelta alle spese sel fondo
 */

public abstract void associaTutteSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1, it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/**
 * Associazione.
 *
 * Nome: Associare a Obbligazione_scadenzarioBulk;
 * Pre:  Associare tutte le spese selezionate all'Obbligazione_scadenzarioBulk;
 * Post: Tutte le spese selezionate vengono associare all'Obbligazione_scadenzarioBulk.
 *
 * @param fondo testata delle spese in elenco.
 * @param obbscad Obbligazione scadenzario da associare.
 */

public abstract void associazione(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di calcolare il totale delle spese del fondo economale associate alla scadenza obbligazione
  *   	PostCondition:
  *  		Restituisce il valore del calcolo effettuato
 */

public abstract java.math.BigDecimal calcolaTotaleSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di ricerca dei fondi economali creati
  *   	PostCondition:
  *  		Restituisce l'elenco delle corrispondenze
 */

public abstract it.cnr.jada.util.RemoteIterator cercaFondi(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di estrazione di tutti i mandati validi per integrare il fondo
  *   	PostCondition:
  *  		Restituisce l'elenco delle corrispondenze valide
  *	Mandati di integrazione.
  *		PreCondition:
  * 		Una delle corrispondenze è già collegata ad un fondo
  *   	PostCondition:
  *  		La corrispondenza non viene aggiunta all'elenco
  *	Mandati di apertura.
  *		PreCondition:
  * 		Una delle corrispondenze è mandato di apertura fondo economale
  *   	PostCondition:
  *  		La corrispondenza non viene aggiunta all'elenco
 */

public abstract it.cnr.jada.util.RemoteIterator cercaMandatiPerIntegrazioni(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle scadenze di obbligazioni congruenti con la spesa passiva che si sta creando/modificando.
  *   	PostCondition:
  *  		Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
  *	Validazione lista delle obbligazioni per le fatture passive
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle scadenze delle obbligazioni.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Obbligazione definitiva
  *		PreCondition:
  *			La scadenza non appartiene ad un'obbligazione definitiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni non cancellate
  *		PreCondition:
  *			La scadenza appartiene ad un'obbligazione cancellata
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni associate ad altri documenti amministrativi
  *		PreCondition:
  *			La scadenza appartiene ad un'obbligazione associata ad altri documenti amministrativi
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni della stessa UO
  *		PreCondition:
  *			La scadenza dell'obbligazione non appartiene alla stessa UO di generazione spesa passiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro di selezione sulla data di scadenza
  *		PreCondition:
  *			La scadenza dell'obbligazione ha una data scadenza precedente alla data di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro importo scadenza
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un importo di scadenza inferiore a quella di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro sul progressivo dell'obbligazione o scadenza
  *		PreCondition:
  *			La scadenza dell'obbligazione non ha progressivo specificato
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaObb_scad(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException;
public it.cnr.jada.util.RemoteIterator cercaSospesiDiChiusuraFondo(
	it.cnr.jada.UserContext param0, 
	it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1)
	throws  it.cnr.jada.comp.ComponentException;
/**
	 * Ricerca spese del fondo.
	 *
	 * Nome: Ricerca spese;
	 * Pre:  Ricerca delle spese con filtro;
	 * Post: Viene creato un elenco delle spese che corrispondono ai parametri impostati nel filtro.
	 *
	 * @param filtro su cui va impostata la ricerca.
	 *
	 * @return l'elenco di spese selezionate.
	 */

public abstract it.cnr.jada.util.RemoteIterator cercaSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Filtro_ricerca_speseVBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle spese associabili alla scadenza di obbligazioni
  *   	PostCondition:
  *  		Le spese vengono aggiunte alla lista delle spese congruenti.
  *	Validazione lista delle obbligazioni per le fatture passive
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle scadenze delle spese.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Spesa reintegrata
  *		PreCondition:
  *			La spesa è reintegrata
  * 	PostCondition:
  *  		La spesa non viene aggiunta alla lista delle spese congruenti.
  *	Spesa documentata
  *		PreCondition:
  *			La spesa è documentata
  * 	PostCondition:
  *  		La spesa non viene aggiunta alla lista delle spese congruenti.
  *	Spesa di altro fondo
  *		PreCondition:
  *			La spesa appartiene ad un altro fondo economale
  * 	PostCondition:
  *  		La spesa non viene aggiunta alla lista delle spese congruenti.
  *	Spesa già associata
  *		PreCondition:
  *			La spesa è già associata alla scadenza obbligazione passata
  * 	PostCondition:
  *  		La spesa viene aggiunta alla lista delle spese congruenti per permetterne la disassociazione.
 */

public abstract it.cnr.jada.util.RemoteIterator cercaSpeseAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle spese appartenenti al fondo economale
  *   	PostCondition:
  *  		Le spese vengono aggiunte alla lista delle spese congruenti.
  *	Validazione lista delle spese
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle spese.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Spesa di altro fondo
  *		PreCondition:
  *			La spesa appartiene ad un altro fondo economale
  * 	PostCondition:
  *  		La spesa non viene aggiunta alla lista delle spese congruenti.
  *	Spesa già reintegrata
  *		PreCondition:
  *			La spesa è già reintegrata
  * 	PostCondition:
  *  		La spesa viene aggiunta alla lista delle spese congruenti se il  filtro di 
  *			ricerca per le spese reintegrate era abilitato
  *	Spesa documentata
  *		PreCondition:
  *			La spesa è documentata
  * 	PostCondition:
  *  		La spesa viene aggiunta alla lista delle spese congruenti se il  filtro di 
  *			ricerca per le spese docuemtnate era abilitato
 */

public abstract it.cnr.jada.util.RemoteIterator cercaSpeseDelFondo(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle spese reintegrabili appartenenti al fondo economale
  *   	PostCondition:
  *  		Le spese vengono aggiunte alla lista delle spese congruenti.
  *	Validazione lista delle spese
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle spese.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Spesa di altro fondo
  *		PreCondition:
  *			La spesa appartiene ad un altro fondo economale
  * 	PostCondition:
  *  		La spesa non viene aggiunta alla lista delle spese congruenti.
  *	Spesa già reintegrata
  *		PreCondition:
  *			La spesa è già reintegrata
  * 	PostCondition:
  *  		La spesa non viene aggiunta alla lista delle spese congruenti
 */

public abstract it.cnr.jada.util.RemoteIterator cercaSpeseReintegrabili(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Filtro_ricerca_speseVBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta la chiusura del fondo economale
  *   	PostCondition:
  *  		Il fondo economale viene chiuso
  *	Validazione lista del fondo
  *		PreCondition:
  *			Si è verificato un errore
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Fondo già chiuso
  *		PreCondition:
  *			Il fondo economale è già stato chiuso
  * 	PostCondition:
  *  		L'operazione viene annullata
  *	Le spese del fondo economale non sono ancora state reintegrate
  *		PreCondition:
  *			Esiste almeno una spesa non reintegrata
  * 	PostCondition:
  *  		L'operazione viene annullata
 */

public abstract it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk chiudeFondo(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException;
public it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk chiudeSpese(
	it.cnr.jada.UserContext userContext,
	it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk fondo) 
	throws it.cnr.jada.comp.ComponentException;
/**
 * Set dei parametri di default in creazione di fondi e spese.
 *
 * Creazione Fondo_economaleBulk:
 * importo totale spese = 0,
 * importo residuo fondo = importo ammontare fondo,
 * se importo ammontare iniziale è nullo importo ammontare iniziale = importo ammontare fondo.
 *
 * Creazione Fondo_spesaBulk:
 * inizializzazione della spesa; vedi initSpesa.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di disassociazione di tutte le spese del fondo
  *   	PostCondition:
  *  		Scollega le spese del fondo dall'obbligazione scadenza scelta
 */

public abstract void dissociaTutteSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1, it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Permette la cancellazione della fattura.
  *  validazione eliminazione fattura.
  *    PreCondition:
  *      E' stata eliminata una fattura in stato B or C
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si può eliminare una fattura in stato IVA B o C"
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista delle banche dell'economo.
 */

public abstract java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta ricerca delle modalità di pagamento dell'economo
  *   	PostCondition:
  *  		Restituisce la collezione di modalità di pagamento dell'economo
  *	Validazione dell'economo
  *		PreCondition:
  *			Si è verificato un errore nel caricamento delle modalità di pagamento dell'economo.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
 */

public abstract java.util.Collection findModalita(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/**
 * Inizializza il fleg Aperto del Fondo_economaleBulk a vero.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Inizializza il fleg Aperto del Fondo_economaleBulk a vero.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Set dei parametri di default in modifica di fondi e spese.
 *
 * Creazione Fondo_economaleBulk:
 * importo residuo fondo = importo ammontare fondo - importo totale spese.
 *
 * Creazione Fondo_spesaBulk:
 * inizializzazione della spesa; vedi initSpesa.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Modifica associazione spese.
 *
 * Nome: Allineamento con selezione utente;
 * Pre:  Allinea la situazione reale dei record con selezione utente;
 * Post: Alza o abbassa il buleano che indica l'associazione o meno all'obbligazione.
 *
 * @param spese Elenco delle spese prese in esame.
 * @param associati Elenco delle selezioni operate dall'utente.
 */

public abstract it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk[] modificaSpe_associate(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk[] param1,boolean[] param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta di reintegro delle spese del fondo economale
  *   	PostCondition:
  *  		Le spese selezionate dall'utente vengono reintegrate
  *	Spesa reintegrata
  *		PreCondition:
  *			La spesa è reintegrata
  * 	PostCondition:
  *  		La spesa non viene modificata.
  *	Spesa non documentata
  *		PreCondition:
  *			La spesa non è stata associata a scadenza obbligazione
  * 	PostCondition:
  *  		La spesa non viene modificata.
  *	Quadratura
  *		PreCondition:
  *			La somma delle spese non documentate associate alla stessa scadenza 
  *			obbligazione non è in quadratura con l'importo scadenza stesso
  * 	PostCondition:
  *  		L'operazione viene interrotta con relativo messaggio
 */

public abstract it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk reintegraSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException;
}
