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

import java.util.*;
public class DispCassaCapitoloBulk extends it.cnr.jada.bulk.OggettoBulk {
	protected MandatoBulk mandato;
	protected List dispCassaColl;
/**
 * DispCassaCapitoloBulk constructor comment.
 */
public DispCassaCapitoloBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2002 10.21.03)
 * @return java.util.List
 */
public java.util.List getDispCassaColl() {
	return dispCassaColl;
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2002 10.21.03)
 * @return it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public MandatoBulk getMandato() {
	return mandato;
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2002 10.21.03)
 * @param newCassaColl java.util.List
 */
public void setDispCassaColl(java.util.List newCassaColl) {
	dispCassaColl = newCassaColl;
}
/**
 * Insert the method's description here.
 * Creation date: (29/11/2002 10.21.03)
 * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public void setMandato(MandatoBulk newMandato) {
	mandato = newMandato;
}
}
