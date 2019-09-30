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
public class Ass_tipo_gruppo_fileKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tipo_file;
	private java.lang.String cd_gruppo_file;
	public Ass_tipo_gruppo_fileKey() {
		super();
	}
	public Ass_tipo_gruppo_fileKey(java.lang.String cd_tipo_file, java.lang.String cd_gruppo_file) {
		super();
		this.cd_tipo_file=cd_tipo_file;
		this.cd_gruppo_file=cd_gruppo_file;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_tipo_gruppo_fileKey)) return false;
		Ass_tipo_gruppo_fileKey k = (Ass_tipo_gruppo_fileKey) o;
		if (!compareKey(getCd_tipo_file(), k.getCd_tipo_file())) return false;
		if (!compareKey(getCd_gruppo_file(), k.getCd_gruppo_file())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tipo_file());
		i = i + calculateKeyHashCode(getCd_gruppo_file());
		return i;
	}
	public void setCd_tipo_file(java.lang.String cd_tipo_file)  {
		this.cd_tipo_file=cd_tipo_file;
	}
	public java.lang.String getCd_tipo_file() {
		return cd_tipo_file;
	}
	public void setCd_gruppo_file(java.lang.String cd_gruppo_file)  {
		this.cd_gruppo_file=cd_gruppo_file;
	}
	public java.lang.String getCd_gruppo_file() {
		return cd_gruppo_file;
	}
}