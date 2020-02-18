/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.persistency.Keyed;
public class TipoOrdineBase extends TipoOrdineKey implements Keyed {
//    DS_TIPO_ORDINE VARCHAR(1000) NOT NULL
	private java.lang.String dsTipoOrdine;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_ORDINE
	 **/
	public TipoOrdineBase() {
		super();
	}
	public TipoOrdineBase(java.lang.String cdTipoOrdine) {
		super(cdTipoOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTipoOrdine]
	 **/
	public java.lang.String getDsTipoOrdine() {
		return dsTipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTipoOrdine]
	 **/
	public void setDsTipoOrdine(java.lang.String dsTipoOrdine)  {
		this.dsTipoOrdine=dsTipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}