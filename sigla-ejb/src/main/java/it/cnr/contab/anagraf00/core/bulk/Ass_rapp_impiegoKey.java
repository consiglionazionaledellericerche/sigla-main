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
 * Date 26/01/2009
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_rapp_impiegoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_rapp_impiego_sti;
	public Ass_rapp_impiegoKey() {
		super();
	}
	public Ass_rapp_impiegoKey(java.lang.String cd_rapp_impiego_sti) {
		super();
		this.cd_rapp_impiego_sti=cd_rapp_impiego_sti;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_rapp_impiegoKey)) return false;
		Ass_rapp_impiegoKey k = (Ass_rapp_impiegoKey) o;
		if (!compareKey(getCd_rapp_impiego_sti(), k.getCd_rapp_impiego_sti())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_rapp_impiego_sti());
		return i;
	}
	public void setCd_rapp_impiego_sti(java.lang.String cd_rapp_impiego_sti)  {
		this.cd_rapp_impiego_sti=cd_rapp_impiego_sti;
	}
	public java.lang.String getCd_rapp_impiego_sti() {
		return cd_rapp_impiego_sti;
	}
}