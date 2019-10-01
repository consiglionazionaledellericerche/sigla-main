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
 * Creation date: (5/28/2002 3:05:49 PM)
 * @author: Simonetta Costa
 */
public class OptionRequestParameter implements java.io.Serializable {
	private java.lang.Boolean checkDisponibilitaDiCassaRequired = Boolean.TRUE;
	private java.lang.Boolean checkDisponibilitaContrattoRequired = Boolean.TRUE;
	
/**
 * OptionRequestParameter constructor comment.
 */
public OptionRequestParameter() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 12:07:44 PM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean isCheckDisponibilitaDiCassaRequired() {
	return checkDisponibilitaDiCassaRequired;
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 12:07:44 PM)
 * @param newCheckDisponibilitaDiCassaRequired java.lang.Boolean
 */
public void setCheckDisponibilitaDiCassaRequired(java.lang.Boolean newCheckDisponibilitaDiCassaRequired) {
	checkDisponibilitaDiCassaRequired = newCheckDisponibilitaDiCassaRequired;
}

public void setCheckDisponibilitaContrattoRequired(java.lang.Boolean checkDisponibilitaContrattoRequired) {
	this.checkDisponibilitaContrattoRequired = checkDisponibilitaContrattoRequired;
}
public java.lang.Boolean isCheckDisponibilitaContrattoRequired() {
	return checkDisponibilitaContrattoRequired;
}
}
