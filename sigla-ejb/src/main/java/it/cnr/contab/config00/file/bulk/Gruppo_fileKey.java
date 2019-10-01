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
 * Date 07/02/2008
 */
package it.cnr.contab.config00.file.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Gruppo_fileKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_gruppo_file;
	public Gruppo_fileKey() {
		super();
	}
	public Gruppo_fileKey(java.lang.String cd_gruppo_file) {
		super();
		this.cd_gruppo_file=cd_gruppo_file;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Gruppo_fileKey)) return false;
		Gruppo_fileKey k = (Gruppo_fileKey) o;
		if (!compareKey(getCd_gruppo_file(), k.getCd_gruppo_file())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_gruppo_file());
		return i;
	}
	public void setCd_gruppo_file(java.lang.String cd_gruppo_file)  {
		this.cd_gruppo_file=cd_gruppo_file;
	}
	public java.lang.String getCd_gruppo_file() {
		return cd_gruppo_file;
	}
}