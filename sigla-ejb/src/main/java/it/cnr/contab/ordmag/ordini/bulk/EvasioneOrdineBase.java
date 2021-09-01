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
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.persistency.Keyed;
public class EvasioneOrdineBase extends EvasioneOrdineKey implements Keyed {
//    DATA_BOLLA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dataBolla;
 
//    NUMERO_BOLLA VARCHAR(30) NOT NULL
	private java.lang.String numeroBolla;
 
//    DATA_CONSEGNA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dataConsegna;
 
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EVASIONE_ORDINE
	 **/
	public EvasioneOrdineBase() {
		super();
	}
	public EvasioneOrdineBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Long numero) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, numero);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataBolla]
	 **/
	public java.sql.Timestamp getDataBolla() {
		return dataBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataBolla]
	 **/
	public void setDataBolla(java.sql.Timestamp dataBolla)  {
		this.dataBolla=dataBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroBolla]
	 **/
	public java.lang.String getNumeroBolla() {
		return numeroBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroBolla]
	 **/
	public void setNumeroBolla(java.lang.String numeroBolla)  {
		this.numeroBolla=numeroBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataConsegna]
	 **/
	public java.sql.Timestamp getDataConsegna() {
		return dataConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataConsegna]
	 **/
	public void setDataConsegna(java.sql.Timestamp dataConsegna)  {
		this.dataConsegna=dataConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}