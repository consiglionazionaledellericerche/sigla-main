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


public class Ruolo_bloccoBulk extends Ruolo_bloccoBase {

	RuoloBulk ruolo;

	public Ruolo_bloccoBulk() {
		super();
	}
	public Ruolo_bloccoBulk(java.lang.String cd_ruolo,java.lang.Integer esercizio) {
		super(cd_ruolo,esercizio);
	}
	public RuoloBulk getRuolo() {
		return ruolo;
	}
	public void setRuolo(RuoloBulk ruolo) {
		this.ruolo = ruolo;
	}
	public java.lang.String getCd_ruolo() {
		if (this.getRuolo() == null)
			return null;
		return ruolo.getCd_ruolo();
	}
	public void setCd_ruolo(java.lang.String cd_ruolo) {
		if (this.getRuolo() != null)
			this.getRuolo().setCd_ruolo(cd_ruolo);
	}
}
