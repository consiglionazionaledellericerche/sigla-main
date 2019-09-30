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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2011
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Quadri_770Base extends Quadri_770Key implements Keyed {
//    DS_QUADRO VARCHAR(300) NOT NULL
	private java.lang.String ds_quadro;
 
//    TI_MODELLO VARCHAR(1) NOT NULL
	private java.lang.String ti_modello;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: QUADRI_770
	 **/
	public Quadri_770Base() {
		super();
	}
	public Quadri_770Base(java.lang.Integer esercizio, java.lang.String cd_quadro) {
		super(esercizio, cd_quadro);
	}
	public java.lang.String getDs_quadro() {
		return ds_quadro;
	}
	public void setDs_quadro(java.lang.String ds_quadro) {
		this.ds_quadro = ds_quadro;
	}
	public java.lang.String getTi_modello() {
		return ti_modello;
	}
	public void setTi_modello(java.lang.String ti_modello) {
		this.ti_modello = ti_modello;
	}

}