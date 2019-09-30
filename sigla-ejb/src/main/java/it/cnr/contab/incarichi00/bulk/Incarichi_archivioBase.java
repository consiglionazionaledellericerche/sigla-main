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
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_archivioBase extends Incarichi_archivioKey implements Keyed {
//  NOME_FILE  VARCHAR2(400) NOT NULL
	private java.lang.String nome_file;

//    DS_FILE VARCHAR(2000)
	private java.lang.String ds_file;

//    URL_FILE VARCHAR(500)
	private java.lang.String url_file;

//    CMS_NODE_REF  VARCHAR2(100) NULL
	private java.lang.String cms_node_ref;

//    TIPO_ARCHIVIO VARCHAR(1) NOT NULL
	private java.lang.String tipo_archivio;
 
//    STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;

	public Incarichi_archivioBase() {
		super();
	}

	public java.lang.String getTipo_archivio() {
		return tipo_archivio;
	}
	public void setTipo_archivio(java.lang.String tipo_archivio)  {
		this.tipo_archivio=tipo_archivio;
	}
	public java.lang.String getNome_file() {
		return nome_file;
	}
	public void setNome_file(java.lang.String nome_file) {
		this.nome_file = nome_file;
	}
	public java.lang.String getDs_file() {
		return ds_file;
	}
	public void setDs_file(java.lang.String ds_file) {
		this.ds_file = ds_file;
	}
	public java.lang.String getUrl_file() {
		return url_file;
	}
	public void setUrl_file(java.lang.String url_file) {
		this.url_file = url_file;
	}
	public java.lang.String getCms_node_ref() {
		return cms_node_ref;
	}
	public void setCms_node_ref(java.lang.String cms_node_ref) {
		this.cms_node_ref = cms_node_ref;
	}
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}