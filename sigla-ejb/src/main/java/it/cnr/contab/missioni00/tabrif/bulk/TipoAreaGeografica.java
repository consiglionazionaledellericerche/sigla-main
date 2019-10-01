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

package it.cnr.contab.missioni00.tabrif.bulk;

/**
 * Insert the type's description here.
 * Creation date: (12/02/2002 11.50.10)
 * @author: Roberto Fantino
 */
public abstract class TipoAreaGeografica {

	public final static java.util.Dictionary TIPI_AREA_GEOGRAFICA = new it.cnr.jada.util.OrderedHashtable();
	public final static String ITALIA = "I";
	public final static String ESTERO = "E";
	public final static String INDIFFERENTE  = "*";

	static {
		TIPI_AREA_GEOGRAFICA.put(ITALIA,"Italia");
		TIPI_AREA_GEOGRAFICA.put(ESTERO,"Estero");
		TIPI_AREA_GEOGRAFICA.put(INDIFFERENTE,"Indifferente");
	}

}
