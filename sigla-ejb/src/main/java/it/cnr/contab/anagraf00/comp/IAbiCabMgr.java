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

package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.comp.*;
/**
 * Insert the type's description here.
 * Creation date: (13/11/2002 12.35.13)
 * @author: Roberto Fantino
 */
public interface IAbiCabMgr extends it.cnr.jada.comp.ICRUDMgr {
/**
 * Ricerca cap legati al comune selezionato
 *
 * @param userContext	lo UserContext che ha generato la richiesta
 * @param abiCab		L'abiCab in uso
 * @return La collezione di cap associati al comune selezionato
 */
public AbicabBulk findCaps(UserContext userContext, AbicabBulk abiCab) throws ComponentException;
/**
 * Ricerca TRUE se l'oggetto bulk è cancellato logicamento
 *
 * @param userContext	lo UserContext che ha generato la richiesta
 * @param abiCab		L'abiCab in uso
 * @return TRUE se l'oggetto byulk è cancellato logicamento, FALSE altrimenti
 */
public boolean isCancellatoLogicamente(UserContext userContext, AbicabBulk abiCab) throws ComponentException;
}
