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

import java.util.Vector;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface ITipo_bolloMgr extends ICRUDMgr
{

/** 
  *  Creazione di un tipo bollo non default
  *    PreCondition:
  *      la richiesta di creazione di un tipo bollo non default e' stata generata
  *    PostCondition:
  *      il tipo bollo e' stato creato 
  *  Creazione di un tipo bollo default - errore
  *    PreCondition:
  *      la richiesta di creazione di un tipo bollo di default e' stata generata ed esiste un altro tipo bollo
  *      definito come default
  *    PostCondition:
  *      una ComponentException viene generata per segnalare all'utente l'impossibilità ad effettuare l'inserimento
  *  Creazione di un tipo bollo default - OK
  *    PreCondition:
  *      la richiesta di creazione di un tipo bollo di default e' stata generata e non esiste un altro tipo bollo
  *      definito come default
  *    PostCondition:
  *      il tipo bollo e' stato creato dopo essere stato validato (metodo verificaFl_default)
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>Tipo_bolloBulk</code> da creare
  *
  * @return OggettoBulk <code>Tipo_bolloBulk</code> creato
  */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Esegue una operazione di eliminazione logica o fisica di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione logica di un Tipo_bollo
 * Pre: Una richiesta di cancellazione logica di un tipo bollo e' stata generata
 * Post: Il flag cancellazione del Tipo Bollo e' stato impostato a true
 *
 * Nome: Cancellazione fisica di un Tipo_bollo
 * Pre: Una richiesta di cancellazione fisica di un tipo bollo e' stata generata
 * Post: Il Tipo Bollo e' stato eliminato
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk	il <code>Tipo_bolloBulk</code> che deve essere cancellato
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Modifica di un tipo bollo non default
  *    PreCondition:
  *      la richiesta di modifica di un tipo bollo non default e' stata generata
  *    PostCondition:
  *      il tipo bollo e' stato modificato 
  *  Modifica di un tipo bollo default - OK
  *    PreCondition:
  *      la richiesta di modifica di un tipo bollo di default e' stata generata e non esiste un altro tipo bollo
  *      definito come default
  *    PostCondition:
  *      il tipo bollo e' stato modificato dopo essere stato validato (metodo verificaFl_default)
  *
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>Tipo_bolloBulk</code> da modificare
  *
  * @return OggettoBulk <code>Tipo_bolloBulk</code> modificato
  */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
