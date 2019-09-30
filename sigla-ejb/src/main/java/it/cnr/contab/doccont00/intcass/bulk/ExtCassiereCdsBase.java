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
 * Date 30/05/2013
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.persistency.Keyed;
public class ExtCassiereCdsBase extends ExtCassiereCdsKey implements Keyed {
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cdCds;
 
//    CODICE_SIA VARCHAR(5)
	private java.lang.String codiceSia;
 
//    CODICE_CUC VARCHAR(50)
	private java.lang.String codiceCuc;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EXT_CASSIERE_CDS
	 **/
	public ExtCassiereCdsBase() {
		super();
	}
	public ExtCassiereCdsBase(java.lang.Integer esercizio, java.lang.String codiceProto) {
		super(esercizio, codiceProto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceSia]
	 **/
	public java.lang.String getCodiceSia() {
		return codiceSia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceSia]
	 **/
	public void setCodiceSia(java.lang.String codiceSia)  {
		this.codiceSia=codiceSia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceCuc]
	 **/
	public java.lang.String getCodiceCuc() {
		return codiceCuc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceCuc]
	 **/
	public void setCodiceCuc(java.lang.String codiceCuc)  {
		this.codiceCuc=codiceCuc;
	}
}