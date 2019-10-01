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
import it.cnr.jada.persistency.Keyed;
public class Gruppo_fileBase extends Gruppo_fileKey implements Keyed {
//    DS_GRUPPO_FILE VARCHAR(300) NOT NULL
	private java.lang.String ds_gruppo_file;
 
	public Gruppo_fileBase() {
		super();
	}
	public Gruppo_fileBase(java.lang.String cd_gruppo_file) {
		super(cd_gruppo_file);
	}
	public java.lang.String getDs_gruppo_file() {
		return ds_gruppo_file;
	}
	public void setDs_gruppo_file(java.lang.String ds_gruppo_file)  {
		this.ds_gruppo_file=ds_gruppo_file;
	}
}