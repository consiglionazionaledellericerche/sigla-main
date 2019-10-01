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
 * Formattatore di importi a 6 cifre decimali
 */

public class Importo6CifreFormat extends GenericImportoFormat {
	public final static java.text.Format format = new java.text.DecimalFormat("#,##0.000000");

public Importo6CifreFormat() {
	super();
	setPrecision(6);
}

public java.text.Format getFormat() {
	return format;
}
}