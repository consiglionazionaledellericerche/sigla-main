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

import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface ITipoContributoRitenutaMgr extends ICRUDMgr{
/**
 * Ricerca lista intervalli di validità Tipo CORI
 * PreCondition:
 *   Viene richiesta la lista degli intervalli di validità del tipo CORI
 *        definiti con data inizio = a quella del tipo CORI in processo
 * PostCondition:
 *   Viene restituita la lista dei Tipi CORI o null nel caso il codice tipo CORI
 *        in processo sia null
 *
*/

public abstract java.util.List caricaIntervalli(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  *  Inserisce un nuovo Tipo CORI
  *
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Cancellazione di un intervallo di validità con data anteriore alla data odierna
 * PreCondition:
 *   La data di inizio dell'intervallo è anteriore alla data odierna
 * PostCondition:
 *   La data di fine validità dell'intervallo viene posta uguale alla data corrente + 1
 *        Tutti gli intervalli successivi a quello in processo vengono eliminati fisicamente
 *
 * Cancellazione di un intervallo di validità con data uguale alla data odierna
 * PreCondition:
 *   La data di inizio dell'intervallo è anteriore alla data odierna
 * PostCondition:
 *        Tutti gli intervalli successivi a quello in processo vengono eliminati fisicamente
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  *    L'intervallo in processo è l'ultimo intervallo esistente
  *    PreCondition:
  *       La data di inizio validità dell'intervallo in processo >= della massima data di inizio di intervalli
  *    PostCondition:
  *       Viene ritornato TRUE
  *
  *    L'intervallo in processo non è l'ultimo intervallo esistente
  *    PreCondition:
  *       La data di inizio validità dell'intervallo in processo < della massima data di inizio di intervalli
  *    PostCondition:
  *       Viene ritornato FALSE
 */

public abstract boolean isUltimoIntervallo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 * Caricamento delle Classificazioni CORI residenti su DB
 *
 * Pre-post-conditions
 *
 * Name: Caricamento classificazioni cori
 * Pre: Viene richiesto il caricamento delle classificazioni cori presenti nel db
 * Post: Viene restituita la collezione delle classificazioni cori presenti
*/
public abstract java.util.Collection loadClassificazioneCori(UserContext userContext) throws ComponentException;
/**
 * Caricamento delle Classificazioni montanti residenti su DB
 *
 * Pre-post-conditions
 *
 * Name: Caricamento classificazioni cori
 * Pre: Viene richiesto il caricamento delle classificazioni montanti presenti nel db
 * Post: Viene restituita la collezione delle classificazioni montanti presenti
*/
public abstract java.util.Collection loadClassificazioneMontanti(UserContext userContext) throws ComponentException;
/**
 * Modifica di intervallo ponendo la data fine nel futuro
 * PreCondition:
 *   Il controllo di validità date è superato
 * PostCondition:
 *   Viene aggiornato l'intervallo in processo
 *
 *      Modifica di intervallo ponendo la data fine < alla data odierna
 * PreCondition: La data di fine intervallo = alla data odierna
 * PostCondition:
 *        Viene sollevata un'eccezione
 *
 *      Modifica di intervallo ponendo la data fine nel passato
 * PreCondition: La data di fine intervallo = alla data odierna
 * PostCondition:
 *        La data di fine validità dell'intervallo corrente viene posta = data odierna
 *        Viene creato il nuovo intervallo con data di inizio validità = alla data odierna + 1
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
