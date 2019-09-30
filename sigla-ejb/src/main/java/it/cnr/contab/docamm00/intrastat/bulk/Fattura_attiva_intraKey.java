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
 * Date 23/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Fattura_attiva_intraKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_cds;
	private java.lang.String cd_unita_organizzativa;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_fattura_attiva;
	private java.lang.Long pg_riga_intra;
	public Fattura_attiva_intraKey() {
		super();
	}
	public Fattura_attiva_intraKey(java.lang.String cd_cds, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio, java.lang.Long pg_fattura_attiva, java.lang.Long pg_riga_intra) {
		super();
		this.cd_cds=cd_cds;
		this.cd_unita_organizzativa=cd_unita_organizzativa;
		this.esercizio=esercizio;
		this.pg_fattura_attiva=pg_fattura_attiva;
		this.pg_riga_intra=pg_riga_intra;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Fattura_attiva_intraKey)) return false;
		Fattura_attiva_intraKey k = (Fattura_attiva_intraKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getCd_unita_organizzativa(), k.getCd_unita_organizzativa())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_fattura_attiva(), k.getPg_fattura_attiva())) return false;
		if (!compareKey(getPg_riga_intra(), k.getPg_riga_intra())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_cds());
		i = i + calculateKeyHashCode(getCd_unita_organizzativa());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_fattura_attiva());
		i = i + calculateKeyHashCode(getPg_riga_intra());
		return i;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_fattura_attiva(java.lang.Long pg_fattura_attiva)  {
		this.pg_fattura_attiva=pg_fattura_attiva;
	}
	public java.lang.Long getPg_fattura_attiva() {
		return pg_fattura_attiva;
	}
	public void setPg_riga_intra(java.lang.Long pg_riga_intra)  {
		this.pg_riga_intra=pg_riga_intra;
	}
	public java.lang.Long getPg_riga_intra() {
		return pg_riga_intra;
	}
}