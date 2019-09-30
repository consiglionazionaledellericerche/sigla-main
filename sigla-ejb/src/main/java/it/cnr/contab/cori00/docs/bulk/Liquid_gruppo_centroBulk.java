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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.persistency.Persistent;

public class Liquid_gruppo_centroBulk extends Liquid_gruppo_centroBase implements Persistent {
	
	public final static String STATO_INIZIALE = "I";
	public final static String STATO_SOSPESO = "S";
	public final static String STATO_RIBALTATO = "R";
	public final static String STATO_CHIUSO = "C";
	public final static String STATO_ANNULLATO = "A";

	public Liquid_gruppo_centroBulk() {
		super();
	}
}