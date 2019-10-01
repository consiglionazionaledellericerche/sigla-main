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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.config00.sto.bulk.*;
// import it.cnr.jada.bulk.*;

public class ListaSospesiBulk extends it.cnr.jada.bulk.OggettoBulk {
	protected java.util.Collection sospesi_cnrColl = new java.util.ArrayList();

/**
 * RicercaCdsPerSospesiBulk constructor comment.
 */
public ListaSospesiBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2002 15.44.11)
 * @return java.util.Collection
 */
public java.util.Collection getSospesi_cnrColl() {
	return sospesi_cnrColl;
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2002 15.44.11)
 * @param newSospesi_entrata_cnrColl java.util.Collection
 */
public void setSospesi_cnrColl(java.util.Collection newSospesi_entrata_cnrColl) {
	sospesi_cnrColl = newSospesi_entrata_cnrColl;
}
}
