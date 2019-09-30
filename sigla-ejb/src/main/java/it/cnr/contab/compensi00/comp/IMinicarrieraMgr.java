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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.14.13)
 * @author: Roberto Fantino
 */
public interface IMinicarrieraMgr extends ICRUDMgr {
/** 
  *  Associa le rate selezionate al compenso.
  *    PreCondition:
  *      Viene richiesta l'associazione delle rate selezionate al compenso.
  *    PostCondition:
  *      Il "compenso" viene associato ad ogni elemento di "rateAssociate" della "minicarriera".
  *  Stato associazione della rata associata
  *    PreCondition:
  *      La rata (elemento di "rateAssociate") viene associata al compenso.
  *    PostCondition:
  *      Lo stato associazione viene impostato a 'T' (Totalmente associata).
  *  Stato di associazione a compenso della testata
  *    PreCondition:
  *      Le "rateAssociate" sono associate.
  *    PostCondition:
  *      Nel caso in cui tutte le rate della minicarriera sono associate lo stato associazione viene impostato a 'T'
  *		 (Totalmente associata); altrimenti lo stato viene impostato a 'P' (Parzialmente associata)
  *  Il processo viene eseguito correttamente
  *    PreCondition:
  *      Le "rateAssociate" vengono correttamente associate.
  *    PostCondition:
  *      Viene restituita la minicarriera debitamente modificata
  *  Si verifica un errore
  *    PreCondition:
  *      Le "rateAssociate" non vengono correttamente associate.
  *    PostCondition:
  *      Viene restituita l'errore relativo
 */

public abstract it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk associaCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1,java.util.List param2,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param3) throws it.cnr.jada.comp.ComponentException;
public MinicarrieraBulk calcolaAliquotaMedia(
	UserContext param0, 
	MinicarrieraBulk param1)
	throws it.cnr.jada.comp.ComponentException;
/** 
  *  Cessazione
  *		PreCondition:
  *			Viene richiesta la cessazione della minicarriera
  *		PostCondition:
  *			Viene eseguita la richiesta e restituita la minicarriera aggiornata non più modificabile
 */

public abstract it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk cessa(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale.
  *    PreCondition:
  *      Vengono richiesti i dati relativi al percipiente della minicarriera
  *			- richiesta caricamento dati diretti (nome, cognome, ragione sociale, CF, PIva)
  *         - richiesta caricamento modalita di pagamento
  *         - richiesta caricamento termini di pagamento
  *         - richiesta caricamento tipi di rapporto
  *    PostCondition:
  *      vengono trasmessi i dati relativi alla minicarriera.
 */

public abstract it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk completaPercipiente(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di una minicarriera e la stessa non ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di una minicarriera e la stessa ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Viene consentita la registrazione della minicarriera.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di una minicarriera e la stessa non ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di una minicarriera e la stessa ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Viene consentita la registrazione della minicarriera.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta l'eliminazione di una minicarriera e la stessa non ha superato il metodo 'eliminaMinicarriera'.
  *	PostCondition:
  *		Non  viene consentita l'eliminazione della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta l'eliminazione di una minicarriera e la stessa ha superato il metodo 'eliminaMinicarriera'.
  *	PostCondition:
  *		Viene consentita l'eliminazione della minicarriera.
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista delle banche del percipiente.
 */

public abstract java.util.List findListaBanche(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Percipiente selezionato
  *    PreCondition:
  *		 Viene richiesta la lista delle Modalità di pagamento
  * 	 associate al percipiente
  *	   PostCondition:
  *		 Viene restituita la lista dei Modalità di pagamento
  * 	 associate al percipiente
  *
  * Percipiente NON selezionato
  *    PreCondition:
  *		 Non è stato selezionato il percipiente
  *	   PostCondition:
  *		 Non vengono caricate le modalità di pagamento
**/

public abstract java.util.Collection findModalita(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Percipiente selezionato
  *    PreCondition:
  *		 Viene richiesta la lista dei Termini di pagamento
  * 	 associati al terzo
  *	   PostCondition:
  *		 Viene restituita la lista dei Termini di pagamento
  * 	 associati al terzo
  *
  * Percipiente NON selezionato
  *    PreCondition:
  *		 Non è stato selezionato il terzo
  *	   PostCondition:
  *		 Non vengono caricati i termini di pagamento
**/

public abstract java.util.Collection findTermini(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Percipiente selezionato
  *    PreCondition:
  *		 Viene richiesta la lista dei Tipi di rapporto
  * 	del terzo selezionato
  *	   PostCondition:
  *		 Viene restituita la lista dei Tipi di rapporto
  * 	 del Percipiente selezionato
  *
  * Percipiente NON selezionato
  *    PreCondition:
  *		 Non è stato selezionato il Percipiente
  *	   PostCondition:
  *		 Non vengono caricati i tipi di rapporto
**/

public abstract java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  * Tipo di rapporto selezionato
  *    PreCondition:
  *		 Viene richiesta la lista dei Tipi di Trattamento legati al
  * 	 tipo di rapporto selezionato
  *	   PostCondition:
  *		 Viene restituita la lista dei Tipi di Trattamento legati al
  * 	 tipo di rapporto selezionato
  *
  * Tipo di rapporto NON selezionato
  *    PreCondition:
  *		 Non è stato selezionato il tipo di rapporto
  *	   PostCondition:
  *		 Non vengono caricati i tipi trattamento
**/

public abstract java.util.Collection findTipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione minicarriera.
  *	PreCondition:
  *		Viene richiesta la generazione delle rate di una minicarriera e la stessa non ha superato il metodo 'validate'.
  *	PostCondition:
  *		Non  viene consentita la generazione delle rate della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la generazione delle rate di una minicarriera e la stessa ha superato il metodo 'validate'.
  *	PostCondition:
  *		Viene consentita la generazione delle rate della minicarriera.
 */

public abstract it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk generaRate(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException;
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
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Viene richiesta la modifica del compenso
  *    PostCondition:
  *      Viene consentito la modifica del compenso e 
  * 	 della obbligazione associata
  *
  *  Controlli non superati
  *    PreCondition:
  *      Non sono stati superati i controlli di validazione 
  * 	 per la modifica del compenso
  *    PostCondition:
  *      Non viene permessa la modifica del compenso e della obbligazione
**/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la modifica di una minicarriera e la stessa non ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Non  viene consentita la modifica della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la modifica di una minicarriera e la stessa ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Viene consentita la modifica della minicarriera.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Minicarriera corrente
  *		PreCondition:
  *			Viene richiesto il rinnovo della minicarriera
  *		PostCondition:
  *			La minicarriera corrente viene aggiornata e diventa "non modificabile"
  *  Rinnovo
  *		PreCondition:
  *			La minicarriera di origine viene aggiornata correttamente
  *		PostCondition:
  *			Viene eseguita e restituita una copia della testata della minicarriera di origine
  *  Rate
  *		PreCondition:
  *			La minicarriera di origine viene aggiornata correttamente
  *		PostCondition:
  *			NON viene eseguita la copia delle rate, ma ne viene richiesta la generazione
 */

public abstract it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk rinnova(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Minicarriera corrente
  *		PreCondition:
  *			Viene richiesto il ripristino della minicarriera
  *		PostCondition:
  *			La minicarriera corrente viene aggiornata e diventa "non modificabile"
  *  Ripristino
  *		PreCondition:
  *			La minicarriera di origine viene aggiornata correttamente
  *		PostCondition:
  *			Viene eseguita e restituita una copia della testata della minicarriera di origine
  *  Rate
  *		PreCondition:
  *			La minicarriera di origine viene aggiornata correttamente
  *		PostCondition:
  *			NON viene eseguita la copia delle rate, ma ne viene richiesta la generazione
 */

public abstract it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk ripristina(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Sospensione
  *		PreCondition:
  *			Viene richiesta la sospensione della minicarriera
  *		PostCondition:
  *			Viene eseguita la richiesta e restituita la minicarriera aggiornata non più modificabile
 */

public abstract it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk sospendi(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException;
public int validaPercipiente(
	UserContext userContext, 
	MinicarrieraBulk minicarriera) 
	throws ComponentException;
}
