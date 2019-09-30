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
* Created by Generator 1.0
* Date 21/07/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_variazione_archivioBase extends Pdg_variazione_archivioKey implements Keyed {
//    TIPO_ARCHIVIO VARCHAR(1) NOT NULL
	private java.lang.String tipo_archivio;
 
//    BDATA BLOB(4000)
	private java.lang.String bdata;
 
	public Pdg_variazione_archivioBase() {
		super();
	}
	public Pdg_variazione_archivioBase(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Long progressivo_riga) {
		super(esercizio, pg_variazione_pdg, progressivo_riga);
	}
	public java.lang.String getTipo_archivio () {
		return tipo_archivio;
	}
	public void setTipo_archivio(java.lang.String tipo_archivio)  {
		this.tipo_archivio=tipo_archivio;
	}
	public java.lang.String getBdata () {
		return bdata;
	}
	public void setBdata(java.lang.String bdata)  {
		this.bdata=bdata;
	}
}