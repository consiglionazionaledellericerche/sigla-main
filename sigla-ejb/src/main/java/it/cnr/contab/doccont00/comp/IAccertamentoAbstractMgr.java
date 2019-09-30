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
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface IAccertamentoAbstractMgr extends IDocumentoContabileMgr, ICRUDMgr {
/**
 * Aggiornamento in differita dei saldi dell'accertamento .
 * Un documento amministrativo di entrata che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un accertamento; i saldi di tale accertamento non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'accertamento viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per accertamento
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       bilancio
 * Post: La richiesta e' stata indirizzata alla component che gestisce gli accertamenti su capitoli di bilancio
 *       (AccertamentoComponent)
 *
 * Nome: Aggiorna saldi per accertamento su partita di giro
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       partita di giro
 * Post: La richiesta e' stata indirizzata alla component che gestisce gli accertamenti su capitoli di partita di giro
 *       (AccertamentoPGiroComponent)
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	docContabile il documento contabile di tipo AccertamentoBulk o AccertamentoPGiroBulk per cui aggiornare lo stato
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'accertamento
 * @param	param paramtero non utilizzato per gli accertamenti 
 */

public abstract void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException;
public void callRiportaAvanti (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
public void callRiportaIndietro (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
/** 
  *  Creazione di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la creazione di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'creaConBulk' su AccertamentoComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Creazione di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la creazione di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'creaConBulk' su AccertamentoPGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Eliminazione di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'eliminazione di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'eliminaConBulk' su AccertamentoComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Eliminazione di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'eliminazione di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'eliminaConBulk' su AccertamentoPGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializzazione per inserimento di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per inserimento di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerInserimento' su AccertamentoComponent
  *  Inizializzazione per inserimento di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per inserimento di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerInserimento' su AccertamentoPGiroComponent
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializzazione per modifica di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per modifica di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerModifica' su AccertamentoComponent
  *  Inizializzazione per modifica di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta l'inizializzazione per modifica di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'inizializzaBulkPerModifica' su AccertamentoPGiroComponent
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un accertamento
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
 */

public abstract void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Modifica di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaConBulk' su AccertamentoComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Modifica di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaConBulk' su AccertamentoPGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Modifica in automatico di scadenze di accertamenti generici provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica in automatico di scadenze di un'accertamento generico provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaScadenzaInAutomatico' su AccertamentoComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
  *  Modifica in automatico di scadenze di accertamenti per impegno di giro provenendo da documenti amministrativi
  *		PreCondition:
  *			Viene richiesta la modifica in automatico di scadenze di un'accertamento per impegno di giro provenendo da documento amministrativo.
  *    PostCondition:
  *  		Viene invocato il metodo 'modificaScadenzaInAutomatico' su AccertamentoPGiroComponent
  *			Se l'oggetto non è inizializzato prima della chiamata al suddetto
  *			metodo viene invocato il metodo 'inizializzaPerModifica'
 */

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException;
/**
 * Annulla le modifiche apportate all'accertamento e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sull'accertamento vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo
 * @param	uc	lo UserContext che ha generato la richiesta
 */

public abstract void rollbackToSavePoint(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc.amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati all'accertamento non venissero confermati (rollback), comunque non verrebbero persi
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
