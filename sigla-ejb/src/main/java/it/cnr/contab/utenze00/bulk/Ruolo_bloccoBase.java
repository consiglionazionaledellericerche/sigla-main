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

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.persistency.Keyed;

public class Ruolo_bloccoBase extends Ruolo_bloccoKey implements Keyed {

	public Ruolo_bloccoBase() {
		super();
	}
	public Ruolo_bloccoBase(java.lang.String cd_ruolo,java.lang.Integer esercizio) {
		super(cd_ruolo,esercizio);
	}

	// FL_ATTIVO CHAR(1)
	private java.lang.Boolean fl_attivo;
	
	public java.lang.Boolean getFl_attivo() {
		return fl_attivo;
	}
	public void setFl_attivo(java.lang.Boolean fl_attivo) {
		this.fl_attivo = fl_attivo;
	}
}
