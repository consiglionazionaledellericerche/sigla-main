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

package it.cnr.contab.fondecon00.comp;

public class ErroreSquadraturaFondo extends it.cnr.jada.comp.ComponentException {

	private it.cnr.jada.bulk.OggettoBulk sospeso;

/**
 * ErroreDiSquadratura constructor comment.
 */
public ErroreSquadraturaFondo() {
	super();
}
/**
 * ErroreDiSquadratura constructor comment.
 * @param s java.lang.String
 */
public ErroreSquadraturaFondo(String s) {
	super(s);
}
/**
 * ErroreDiSquadratura constructor comment.
 * @param s java.lang.String
 */
public ErroreSquadraturaFondo(String s, it.cnr.jada.bulk.OggettoBulk o) {
	super(s);
	setSospeso(o);
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2002 17.13.59)
 * @return it.cnr.jada.bulk.OggettoBulk
 */
public it.cnr.jada.bulk.OggettoBulk getSospeso() {
	return sospeso;
}
/**
 * Insert the method's description here.
 * Creation date: (19/03/2002 17.13.59)
 * @param newSospeso it.cnr.jada.bulk.OggettoBulk
 */
public void setSospeso(it.cnr.jada.bulk.OggettoBulk newSospeso) {
	sospeso = newSospeso;
}
}
