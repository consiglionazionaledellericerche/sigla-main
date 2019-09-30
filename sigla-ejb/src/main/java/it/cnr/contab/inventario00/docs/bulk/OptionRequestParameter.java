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

package it.cnr.contab.inventario00.docs.bulk;

/**
 * Oggetto creato per la gestione del controllo sulla Data Inizio Validità del Consegnatario
 *	in fase di apertura dell'Inventario.
 *
 * Creation date: (28/10/2002 11.09.17)
 * @author: Gennaro Borriello
 */
public class OptionRequestParameter implements java.io.Serializable {
	
	private java.lang.Boolean checkDataConsegnatarioRequired = Boolean.TRUE;
/**
 * OptionRequestParameter constructor comment.
 */
public OptionRequestParameter() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (28/10/2002 11.11.15)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getCheckDataConsegnatarioRequired() {
	return checkDataConsegnatarioRequired;
}
/**
 * Insert the method's description here.
 * Creation date: (28/10/2002 11.11.15)
 * @param newCheckDataConsegnatarioRequired java.lang.Boolean
 */
public void setCheckDataConsegnatarioRequired(java.lang.Boolean newCheckDataConsegnatarioRequired) {
	checkDataConsegnatarioRequired = newCheckDataConsegnatarioRequired;
}
}
