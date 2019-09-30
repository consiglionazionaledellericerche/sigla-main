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
 * Date 24/12/2015
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.persistency.Keyed;
public class DistintaCassiere1210Base extends DistintaCassiere1210Key implements Keyed {
	//    DT_EMISSIONE TIMESTAMP(7)
	@StorageProperty(name="doccont:datDoc")
	private java.sql.Timestamp dtEmissione;

	//    DT_INVIO TIMESTAMP(7)
	private java.sql.Timestamp dtInvio;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DISTINTA_CASSIERE_1210
	 **/
	public DistintaCassiere1210Base() {
		super();
	}
	public DistintaCassiere1210Base(java.lang.Integer esercizio, java.lang.Long pgDistinta) {
		super(esercizio, pgDistinta);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Data di emissione della distinta]
	 **/
	public java.sql.Timestamp getDtEmissione() {
		return dtEmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Data di emissione della distinta]
	 **/
	public void setDtEmissione(java.sql.Timestamp dtEmissione)  {
		this.dtEmissione=dtEmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Data invio cassiere]
	 **/
	public java.sql.Timestamp getDtInvio() {
		return dtInvio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Data invio cassiere]
	 **/
	public void setDtInvio(java.sql.Timestamp dtInvio)  {
		this.dtInvio=dtInvio;
	}
}