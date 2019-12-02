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
 * Date 14/09/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tematica_attivitaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tematica_attivita;
	public Tematica_attivitaKey() {
		super();
	}
	public Tematica_attivitaKey(java.lang.String cd_tematica_attivita) {
		super();
		this.cd_tematica_attivita=cd_tematica_attivita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tematica_attivitaKey)) return false;
		Tematica_attivitaKey k = (Tematica_attivitaKey) o;
		if (!compareKey(getCd_tematica_attivita(), k.getCd_tematica_attivita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tematica_attivita());
		return i;
	}
	public void setCd_tematica_attivita(java.lang.String cd_tematica_attivita)  {
		this.cd_tematica_attivita=cd_tematica_attivita;
	}
	public java.lang.String getCd_tematica_attivita() {
		return cd_tematica_attivita;
	}
}