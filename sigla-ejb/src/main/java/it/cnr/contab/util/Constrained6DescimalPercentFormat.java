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

package it.cnr.contab.util;

/**
 * Insert the type's description here.
 * Creation date: (12/17/2002 10:37:33 AM)
 * @author: CNRADM
 */
public class Constrained6DescimalPercentFormat extends ConstrainedPercentFormat {

	private static final java.text.Format sixDecimalFormat = new java.text.DecimalFormat("0.000000");
/**
 * Constrained6DescimalPercentFormat constructor comment.
 */
public Constrained6DescimalPercentFormat() {
	super();
}
public java.text.Format getFormat() {
	
	return sixDecimalFormat;
}
public int getPrecision() {
	return 6;
}
}
