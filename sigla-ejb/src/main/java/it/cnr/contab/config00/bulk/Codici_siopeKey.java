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

/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 23/04/2007
 */
package it.cnr.contab.config00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Codici_siopeKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String ti_gestione;
	private java.lang.String cd_siope;
	public Codici_siopeKey() {
		super();
	}
	public Codici_siopeKey(java.lang.Integer esercizio, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super();
		this.esercizio=esercizio;
		this.ti_gestione=ti_gestione;
		this.cd_siope=cd_siope;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Codici_siopeKey)) return false;
		Codici_siopeKey k = (Codici_siopeKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getTi_gestione(), k.getTi_gestione())) return false;
		if (!compareKey(getCd_siope(), k.getCd_siope())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getTi_gestione());
		i = i + calculateKeyHashCode(getCd_siope());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setCd_siope(java.lang.String cd_siope)  {
		this.cd_siope=cd_siope;
	}
	public java.lang.String getCd_siope() {
		return cd_siope;
	}
}