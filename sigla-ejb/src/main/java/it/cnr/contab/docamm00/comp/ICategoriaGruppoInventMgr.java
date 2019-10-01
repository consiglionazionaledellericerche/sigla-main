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
public interface ICategoriaGruppoInventMgr extends it.cnr.jada.comp.ICRUDMgr{
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

/** 
  *  Creazione di una Categoria/Gruppo inventariale
  *    PreCondition:
  *      Nessun errore rilevato
  *    PostCondition:
  *      La nuova categoria gruppo viene resa persistente e la sua associazione a voce finanziaria viene salvata
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Recupera la voce del piano finanziario corrispondente alla categoria/gruppo in processo (cgi)
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: viene ritornato l'elemento voce corrispondente alla categoria/gruppo cgi
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	cgi	la categoria gruppo in processo
 * @return	un'istanza di Elemento_voceBulk
 */

public abstract it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk findElementoVoce(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Recupera il capitolo/articolo finanziario associato alla categoria/gruppo in processo
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: viene ritornata un'istanza di Voce_fBulk di appartenenza CDS gestione Spese codice specificato in associazione
 *       tra categoria inventariale e voceF
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	cgi	la categoria/gruppo
 * @return	il capitolo/articolo
 */

public abstract it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk findVoce_f(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;

/**
 * Recupera i figli della categoria gruppo corrente
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: viene ritornato un RemoteIteretor sulla collezione dei figli del bulk specificato
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk di cui determinare il parent
 * @return	il RemoteIterator sui figli
 */

public abstract it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Recupera il parent della categoria gruppo corrente
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: l'OggettoBulk relativo al parent del bulk specificato viene ritornato
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk di cui determinare il parent
 * @return	l'OggettoBulk parent
 */

public abstract it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

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

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**
 * Controllo che il bulk specificato rappresenta una categoria gruppo di ultimo livello.
 *  PreCondition: Il livello della categoria/gruppo = 0 o non ha figli
 *  PpostCondition: Ritorna true
 *
 * @param context it.cnr.jada.UserContext
 * @param bulk categoria/gruppo di cui stabilire se ultimo livello
 * @return boolean true se ultimo livello
 */
public abstract boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Modifica di una Categoria/Gruppo inventariale esistente
  *    PreCondition:
  *      Nessun errore rilevato
  *    PostCondition:
  *      La categoria gruppo viene resa persistente e la sua associazione a voce finanziaria viene aggiornata
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}