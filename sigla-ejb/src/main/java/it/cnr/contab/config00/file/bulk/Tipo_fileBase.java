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
public class Tipo_fileBase extends Tipo_fileKey implements Keyed {
//    DS_TIPO_FILE VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_file;
 
//    ESTENSIONE_FILE VARCHAR(10) NOT NULL
	private java.lang.String estensione_file;
 
	public Tipo_fileBase() {
		super();
	}
	public Tipo_fileBase(java.lang.String cd_tipo_file) {
		super(cd_tipo_file);
	}
	public java.lang.String getDs_tipo_file() {
		return ds_tipo_file;
	}
	public void setDs_tipo_file(java.lang.String ds_tipo_file)  {
		this.ds_tipo_file=ds_tipo_file;
	}
	public java.lang.String getEstensione_file() {
		return estensione_file;
	}
	public void setEstensione_file(java.lang.String estensione_file)  {
		this.estensione_file=estensione_file;
	}
}