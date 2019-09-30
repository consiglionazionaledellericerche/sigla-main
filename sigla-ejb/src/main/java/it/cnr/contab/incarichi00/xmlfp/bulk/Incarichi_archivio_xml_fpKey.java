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
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_archivio_xml_fpKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer id_archivio;

	public Incarichi_archivio_xml_fpKey() {
		super();
	}

	public Incarichi_archivio_xml_fpKey(java.lang.Integer id_archivio) {
		super();
		this.id_archivio=id_archivio;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_archivio_xml_fpKey)) return false;
		Incarichi_archivio_xml_fpKey k = (Incarichi_archivio_xml_fpKey) o;
		if (!compareKey(getId_archivio(), k.getId_archivio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_archivio());
		return i;
	}

	public java.lang.Integer getId_archivio() {
		return id_archivio;
	}

	public void setId_archivio(java.lang.Integer idArchivio) {
		id_archivio = idArchivio;
	}
}