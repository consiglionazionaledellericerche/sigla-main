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

package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (3/28/2002 3:46:20 PM)
 * @author: Roberto Peli
 */
public interface Voidable {
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:48:10 PM)
 * @return java.sql.Timestamp
 */
java.sql.Timestamp getDt_cancellazione();
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:47:20 PM)
 * @return boolean
 */
boolean isAnnullato();
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:56:31 PM)
 */
boolean isVoidable();
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:47:20 PM)
 * @return boolean
 */
void setAnnullato(java.sql.Timestamp date);
/**
 * Insert the method's description here.
 * Creation date: (3/28/2002 3:48:10 PM)
 * @return java.sql.Timestamp
 */
void setDt_cancellazione(java.sql.Timestamp date);
}
