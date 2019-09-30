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

package it.cnr.contab.doccont00.comp;

import java.math.BigDecimal;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface IObbligazioneAbstractMgr extends IDocumentoContabileMgr, ICRUDMgr {
/**
 * Aggiornamento in differita dei saldi dell'obbligazione .
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un'obbligazione; i saldi di tale obbligazione non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'obbligazione viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per obbligazione
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per una obbligazione su capitoli di
 *       bilancio
 * Post: La richiesta e' stata indirizzata alla component che gestisce le obbligazioni su capitoli di bilancio
 *       (ObbligazioneComponent)
 *
 * Nome: Aggiorna saldi per obbligazione su partita di giro
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per una obbligazione su capitoli di
 *       partita di giro
 * Post: La richiesta e' stata indirizzata alla component che gestisce le obbligazioni su capitoli di partita di giro
 *       (ObbligazionePGiroComponent)
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	docContabile	il documento contabile di tipo ObbligazioneBulk o ImpegnoPGirobulk per cui aggiornare lo stato
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'obbligazione e il "checkDisponibilitaCassaEseguito" che indica se
 *          l'utente ha richiesto la forzatura della disponibilità di cassa (parametro impostato dalla Gestione Obbligazione)
 * @param	param il parametro che indica se il controllo della disp. di cassa e' necessario (parametro impostato dalla Gestione dei doc. amm.)
 
 */

public abstract void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException;
public void callRiportaAvanti (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
public void callRiportaIndietro (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
/** 
  *  Creazione di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la creazione di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'creaConBulk' su ObbligazioneComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Creazione di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la creazione di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'creaConBulk' su ObbligazionePGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da creare
  *
  * @return <code>OggettoBulk</code> L'obbligazione creata
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Eliminazione di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'eliminazione di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'eliminaConBulk' su ObbligazioneComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Eliminazione di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'eliminazione di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'eliminaConBulk' su ObbligazionePGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da cancellare
  *
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializzazione per inserimento di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per inserimento di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerInserimento' su ObbligazioneComponent
  *  Inizializzazione per inserimento di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per inserimento di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerInserimento' su ObbligazionePGiroComponent
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da inizializzare per l'inserimento
  *
  * @return <code>OggettoBulk</code> l'obbligazione inizializzata per l'inserimento
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializzazione per modifica di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per modifica di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerModifica' su ObbligazioneComponent
  *  Inizializzazione per modifica di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per modifica di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerModifica' su ObbligazionePGiroComponent
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da inizializzare per la modifica
  *
  * @return <code>OggettoBulk</code> l'obbligazione inizializzata per la modifica
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un'obbligazione
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param scadenza <code>Obbligazione_scadenzarioBulk</code> la scadenza di obbligazione per cui mettere il lock
 */

public abstract void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Modifica di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaConBulk' su ObbligazioneComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Modifica di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaConBulk' su ObbligazionePGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'obbligazione da modificare
  *
  * @return <code>OggettoBulk</code> l'obbligazione modificata
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Modifica in automatico di scadenze di obbligazioni generiche provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica in automatico di scadenze di un'obbligazione generica provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaScadenzaInAutomatico' su ObbligazioneComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Modifica in automatico di scadenze di obbligazioni per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica in automatico di scadenze di un'obbligazione per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaScadenzaInAutomatico' su ObbligazionePGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  * 
  * @param userContext lo userContext che ha generato la richiesta
  * @param scadenza (con importo originale)
  * @param nuovoImporto che deve assumere la scadenza
  * @param modificaScadenzaSuccessiva se true indica il fatto che la testata dell'obbligazione non deve essere modificata
  *                                   e che la differenza fra l'importo attuale e il vecchio importo deve essere riportata sulla
  *									 scadenza successiva
  * @param modificaScadenzaSuccessiva se false indica il fatto che deve essere modificato l'importo della scadenza e della testata
  *                                   dell'obbligazione
  * @return la scadenza 
  */

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException;
/**
 * Annulla le modifiche apportate all'obbligazione e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sull'obbligazione vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo
 * @param	uc	lo UserContext che ha generato la richiesta
 */

public abstract void rollbackToSavePoint(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc.amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati all'obbligazione non venissero confermati (rollback), comunque non verrebbero persi
 * anche quelli del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Imposta savePoint
 * Pre:  Una richiesta di impostare un savepoint e' stata generata 
 * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 */

public abstract void setSavePoint(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;
}
