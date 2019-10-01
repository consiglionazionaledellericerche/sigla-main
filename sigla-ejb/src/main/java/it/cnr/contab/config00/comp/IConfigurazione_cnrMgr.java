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

package it.cnr.contab.config00.comp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
public interface IConfigurazione_cnrMgr
{


/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto un istanza di Configurazioe_cnrBulk dalle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito un'istanza di Configurazione_cnrBulk o null se la ricerca non restituisce nulla
 */

public abstract it.cnr.contab.config00.bulk.Configurazione_cnrBulk getConfigurazione(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.lang.String getVal01(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.lang.String getVal01(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.lang.String getVal02(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.lang.String getVal02(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.lang.String getVal03(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.lang.String getVal03(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.lang.String getVal04(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */

public abstract java.lang.String getVal04(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException;
}
