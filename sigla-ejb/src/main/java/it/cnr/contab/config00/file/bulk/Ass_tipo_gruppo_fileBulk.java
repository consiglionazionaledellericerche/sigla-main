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
public class Ass_tipo_gruppo_fileBulk extends Ass_tipo_gruppo_fileBase {

	protected Tipo_fileBulk tipo_file = new Tipo_fileBulk();
	protected Gruppo_fileBulk gruppo_file = new Gruppo_fileBulk();

	public Ass_tipo_gruppo_fileBulk() {
		super();
	}
	public Ass_tipo_gruppo_fileBulk(java.lang.String cd_tipo_file, java.lang.String cd_gruppo_file) {
		super(cd_tipo_file, cd_gruppo_file);
	}
	public Tipo_fileBulk getTipo_file() {
		return tipo_file;
	}
	public void setTipo_File(Tipo_fileBulk tipo_file) {
		this.tipo_file = tipo_file;
	}
	@Override
	public String getCd_tipo_file() {
		if (getTipo_file()==null)
			return null;
		return getTipo_file().getCd_tipo_file();
	}
	@Override
	public void setCd_tipo_file(String cd_tipo_file) {
		if (getTipo_file()!=null)
			getTipo_file().setCd_tipo_file(cd_tipo_file);
	}
	public Gruppo_fileBulk getGruppo_file() {
		return gruppo_file;
	}
	public void setGruppo_file(Gruppo_fileBulk gruppo_file) {
		this.gruppo_file = gruppo_file;
	}
	@Override
	public String getCd_gruppo_file() {
		if (getGruppo_file()==null)
			return null;
		return getGruppo_file().getCd_gruppo_file();
	}
	@Override
	public void setCd_gruppo_file(String cd_gruppo_file) {
		if (getGruppo_file()!=null)
			getGruppo_file().setCd_gruppo_file(cd_gruppo_file);
	}
}