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
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class CupBase extends CupKey implements Keyed {
//    DESCRIZIONE VARCHAR(200) NOT NULL
	private java.lang.String descrizione;
 
	private java.sql.Timestamp dt_canc;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CUP
	 **/
	public CupBase() {
		super();
	}
	public CupBase(java.lang.String cdCup) {
		super(cdCup);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descrizione]
	 **/
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descrizione]
	 **/
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	public java.sql.Timestamp getDt_canc() {
		return dt_canc;
	}
	public void setDt_canc(java.sql.Timestamp dt_canc) {
		this.dt_canc = dt_canc;
	}
}