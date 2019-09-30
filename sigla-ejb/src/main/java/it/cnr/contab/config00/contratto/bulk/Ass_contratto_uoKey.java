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
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_contratto_uoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String stato_contratto;	
	private java.lang.Long pg_contratto;
	private java.lang.String cd_unita_organizzativa;
	public Ass_contratto_uoKey() {
		super();
	}
	public Ass_contratto_uoKey(java.lang.Integer esercizio, java.lang.String stato_contratto, java.lang.Long pg_contratto, java.lang.String cd_unita_organizzativa) {
		super();
		this.esercizio=esercizio;
		this.stato_contratto=stato_contratto;
		this.pg_contratto=pg_contratto;
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_contratto_uoKey)) return false;
		Ass_contratto_uoKey k = (Ass_contratto_uoKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getStato_contratto(), k.getStato_contratto())) return false;
		if (!compareKey(getPg_contratto(), k.getPg_contratto())) return false;
		if (!compareKey(getCd_unita_organizzativa(), k.getCd_unita_organizzativa())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getStato_contratto());
		i = i + calculateKeyHashCode(getPg_contratto());
		i = i + calculateKeyHashCode(getCd_unita_organizzativa());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setPg_contratto(java.lang.Long pg_contratto)  {
		this.pg_contratto=pg_contratto;
	}
	public java.lang.Long getPg_contratto () {
		return pg_contratto;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	/**
	 * @return
	 */
	public java.lang.String getStato_contratto() {
		return stato_contratto;
	}

	/**
	 * @param string
	 */
	public void setStato_contratto(java.lang.String string) {
		stato_contratto = string;
	}

}