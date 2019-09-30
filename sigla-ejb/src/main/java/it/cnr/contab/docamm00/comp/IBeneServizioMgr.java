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

package it.cnr.contab.docamm00.comp;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
/**
 * Insert the type's description here.
 * Creation date: (12/12/2001 1:12:41 PM)
 * @author: Roberto Peli
 */
public interface IBeneServizioMgr {
/**
 * Esegue una operazione di modifica di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Non passa validazione applicativa
 * Pre: l'OggettoBulk non passa i criteri di validità applicativi per l'operazione
 *		di modifica
 * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
 *
 * Nome: Non passa validazione per violazione di vincoli della base di dati
 * Pre: l'OggettoBulk contiene qualche attributo nullo in corrispondenza di campi NOT_NULLABLE o qualche
 *			attributo stringa troppo lungo per i corrispondenti campi fisici.
 * Post: Viene generata una it.cnr.jada.comp.CRUDNotNullConstraintException o una 
 *	 		CRUDTooLargeConstraintException con la descrizione dell'errore
 *
 * Nome: Oggetto non trovato
 * Pre: l'OggettoBulk specificato non esiste.
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Oggetto scaduto
 * Pre: l'OggettoBulk specificato è stato modificato da altri utenti dopo la lettura
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Oggetto occupato
 * Pre: l'OggettoBulk specificato è bloccato da qualche altro utente.
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Tutti i controlli superati
 * Pre: Tutti i controlli precedenti superati
 * Post: l'OggettoBulk viene modificato fisicamente nella base dati e viene chiusa la transazione
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk che deve essere modificato
 * @return	l'OggettoBulk risultante dopo l'operazione di modifica.
 */

public abstract it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk completaElementoVoceOf(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk param1) throws it.cnr.jada.comp.ComponentException;
public it.cnr.jada.bulk.OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di creazione.
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: 
 * Post: l'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
 *			per una operazione di creazione
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di modifica.
 *
 * Pre-post-conditions:
 *
 * Nome: Oggetto non esistente
 * Pre: L'OggettoBulk specificato non esiste.
 * Post: Viene generata una CRUDException con la descrizione dell'errore.
 *
 * Nome: Tutti i controlli superati
 * Pre: L'OggettoBulk specificato esiste.
 * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
 *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".edit"
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Esegue una operazione di modifica di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Non passa validazione applicativa
 * Pre: l'OggettoBulk non passa i criteri di validità applicativi per l'operazione
 *		di modifica
 * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
 *
 * Nome: Non passa validazione per violazione di vincoli della base di dati
 * Pre: l'OggettoBulk contiene qualche attributo nullo in corrispondenza di campi NOT_NULLABLE o qualche
 *			attributo stringa troppo lungo per i corrispondenti campi fisici.
 * Post: Viene generata una it.cnr.jada.comp.CRUDNotNullConstraintException o una 
 *	 		CRUDTooLargeConstraintException con la descrizione dell'errore
 *
 * Nome: Oggetto non trovato
 * Pre: l'OggettoBulk specificato non esiste.
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Oggetto scaduto
 * Pre: l'OggettoBulk specificato è stato modificato da altri utenti dopo la lettura
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Oggetto occupato
 * Pre: l'OggettoBulk specificato è bloccato da qualche altro utente.
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Tutti i controlli superati
 * Pre: Tutti i controlli precedenti superati
 * Post: l'OggettoBulk viene modificato fisicamente nella base dati e viene chiusa la transazione
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk che deve essere modificato
 * @return	l'OggettoBulk risultante dopo l'operazione di modifica.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
