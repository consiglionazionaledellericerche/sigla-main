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
 * Date 12/09/2011
 */
package it.cnr.contab.anagraf00.tabter.bulk;
import it.cnr.jada.persistency.Keyed;
public class RifAreePaesiEsteriBase extends RifAreePaesiEsteriKey implements Keyed {
//    DS_AREA_ESTERA VARCHAR(100) NOT NULL
	private java.lang.String ds_area_estera;
 
//    TI_ITALIA_ESTERO VARCHAR(1) NOT NULL
	private java.lang.String ti_italia_estero;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RIF_AREE_PAESI_ESTERI
	 **/
	public RifAreePaesiEsteriBase() {
		super();
	}
	public RifAreePaesiEsteriBase(java.lang.String cd_area_estera) {
		super(cd_area_estera);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsAreaEstera]
	 **/
	public java.lang.String getDs_area_estera() {
		return ds_area_estera;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsAreaEstera]
	 **/
	public void setDs_area_estera(java.lang.String ds_area_estera)  {
		this.ds_area_estera=ds_area_estera;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiItaliaEstero]
	 **/
	public java.lang.String getTi_italia_estero() {
		return ti_italia_estero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiItaliaEstero]
	 **/
	public void setTi_italia_estero(java.lang.String ti_italia_estero)  {
		this.ti_italia_estero=ti_italia_estero;
	}
}