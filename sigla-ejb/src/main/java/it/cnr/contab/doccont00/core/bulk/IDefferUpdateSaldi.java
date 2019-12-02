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

package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (5/15/2002 10:24:10 AM)
 * @author: Simonetta Costa
 */
public interface IDefferUpdateSaldi {
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
void addToDefferredSaldi(IDocumentoContabileBulk docCont, java.util.Map values);
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
it.cnr.jada.bulk.PrimaryKeyHashMap getDefferredSaldi();
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont);
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
void removeFromDefferredSaldi(IDocumentoContabileBulk docCont);
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:28:42 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
void resetDefferredSaldi();
}
