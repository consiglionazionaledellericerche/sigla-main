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

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
/**
 * Insert the type's description here.
 * Creation date: (22/03/2002 11.10.22)
 * @author: Roberto Fantino
 */
public interface ITipologiaRischioMgr extends ICRUDMgr{
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 11.35.49)
 * @return it.cnr.jada.bulk.OggettoBulk
 * @param userContext it.cnr.jada.UserContext
 * @param bulk it.cnr.jada.bulk.OggettoBulk
 * @exception it.cnr.jada.comp.ComponentException The exception description.
 */
public abstract OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException;
}
