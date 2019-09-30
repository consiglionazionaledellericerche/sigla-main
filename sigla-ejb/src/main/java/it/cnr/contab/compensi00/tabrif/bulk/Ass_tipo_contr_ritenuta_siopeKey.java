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
 * Date 27/09/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_tipo_contr_ritenuta_siopeKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_contributo_ritenuta;
	private java.sql.Timestamp dt_ini_validita;
	public Ass_tipo_contr_ritenuta_siopeKey() {
		super();
	}
	public Ass_tipo_contr_ritenuta_siopeKey(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta, java.sql.Timestamp dt_ini_validita) {
		super();
		this.esercizio=esercizio;
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
		this.dt_ini_validita=dt_ini_validita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_tipo_contr_ritenuta_siopeKey)) return false;
		Ass_tipo_contr_ritenuta_siopeKey k = (Ass_tipo_contr_ritenuta_siopeKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_contributo_ritenuta(), k.getCd_contributo_ritenuta())) return false;
		if (!compareKey(getDt_ini_validita(), k.getDt_ini_validita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_contributo_ritenuta());
		i = i + calculateKeyHashCode(getDt_ini_validita());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta)  {
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}
	public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita)  {
		this.dt_ini_validita=dt_ini_validita;
	}
	public java.sql.Timestamp getDt_ini_validita() {
		return dt_ini_validita;
	}
}