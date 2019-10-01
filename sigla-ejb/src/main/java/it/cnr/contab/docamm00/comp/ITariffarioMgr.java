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

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.*;


public interface ITariffarioMgr
{


/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      La tariffa inserita ha data inizio validità interna all'ultimo periodo preesistente (con data fine = infinito) OR è il primo record della validità della tariffa e ha fine = infinito.
  *    PostCondition:
  *      Consente l'inserimento della tariffa.
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *      Si e verificato un errore.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione,  si è verificato un errore".
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Eliminazione periodo.
  *    PreCondition:
  *      Periodi presenti =1.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione,  deve esistere almeno un periodo".
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Viene consentita la camcellazione.
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessuna condizione di errore.
  *    PostCondition:
  *      Consente la modifica della tariffa.
  *  validazione tariffa non superata
  *    PreCondition:
  *      Sono state modificate le date di validita di un periodo esistente.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, la modifica di questi dati non è consentita".
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
//^^@@
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessuna condizione di errore.
  *    PostCondition:
  *      Consente la modifica della tariffa.
  *  validazione tariffa non superata
  *    PreCondition:
  *      Sono state modificate le date di validita di un periodo esistente.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, la modifica di questi dati non è consentita".
 */
//^^@@
public abstract Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException ;
}
