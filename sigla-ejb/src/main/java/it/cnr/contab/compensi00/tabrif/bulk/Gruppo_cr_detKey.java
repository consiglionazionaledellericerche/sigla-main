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
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Gruppo_cr_detKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_gruppo_cr;
	private java.lang.String cd_regione;
	private java.lang.Long pg_comune;
	public Gruppo_cr_detKey() {
		super();
	}
	public Gruppo_cr_detKey(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr, java.lang.String cd_regione, java.lang.Long pg_comune) {
		super();
		this.esercizio=esercizio;
		this.cd_gruppo_cr=cd_gruppo_cr;
		this.cd_regione=cd_regione;
		this.pg_comune=pg_comune;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Gruppo_cr_detKey)) return false;
		Gruppo_cr_detKey k = (Gruppo_cr_detKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_gruppo_cr(), k.getCd_gruppo_cr())) return false;
		if (!compareKey(getCd_regione(), k.getCd_regione())) return false;
		if (!compareKey(getPg_comune(), k.getPg_comune())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_gruppo_cr());
		i = i + calculateKeyHashCode(getCd_regione());
		i = i + calculateKeyHashCode(getPg_comune());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_gruppo_cr(java.lang.String cd_gruppo_cr)  {
		this.cd_gruppo_cr=cd_gruppo_cr;
	}
	public java.lang.String getCd_gruppo_cr() {
		return cd_gruppo_cr;
	}
	public void setCd_regione(java.lang.String cd_regione)  {
		this.cd_regione=cd_regione;
	}
	public java.lang.String getCd_regione() {
		return cd_regione;
	}
	public void setPg_comune(java.lang.Long pg_comune)  {
		this.pg_comune=pg_comune;
	}
	public java.lang.Long getPg_comune() {
		return pg_comune;
	}
}