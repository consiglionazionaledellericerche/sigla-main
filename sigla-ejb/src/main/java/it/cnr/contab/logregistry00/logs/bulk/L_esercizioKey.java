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
* Created by Generator 1.0
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.contab.logregistry00.core.bulk.OggettoLogBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class L_esercizioKey extends OggettoLogBulk implements KeyedPersistent {
	private java.lang.String cd_cds;
	private java.lang.Integer esercizio;
	public L_esercizioKey() {
		super();
	}
	public L_esercizioKey(java.math.BigDecimal pg_storico_, java.lang.String cd_cds, java.lang.Integer esercizio) {
		super();
		this.pg_storico_=pg_storico_;
		this.cd_cds=cd_cds;
		this.esercizio=esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof L_esercizioKey)) return false;
		L_esercizioKey k = (L_esercizioKey) o;
		if (!compareKey(getPg_storico_(), k.getPg_storico_())) return false;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_storico_());
		i = i + calculateKeyHashCode(getCd_cds());
		i = i + calculateKeyHashCode(getEsercizio());
		return i;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
}