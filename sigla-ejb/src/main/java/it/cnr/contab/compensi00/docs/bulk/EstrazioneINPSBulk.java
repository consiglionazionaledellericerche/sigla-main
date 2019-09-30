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

package it.cnr.contab.compensi00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (16/03/2004 17.49.59)
 * @author: Gennaro Borriello
 */
public class EstrazioneINPSBulk extends EstrazioneCUDVBulk {
	private String cd_uo;
	
/**
 * EstrazioneINPSBulk constructor comment.
 */
public EstrazioneINPSBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.50.39)
 * @return java.lang.String
 */
public java.lang.String getCd_uo() {
	return cd_uo;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.50.39)
 * @param newCd_uo java.lang.String
 */
public void setCd_uo(java.lang.String newCd_uo) {
	cd_uo = newCd_uo;
}
}
