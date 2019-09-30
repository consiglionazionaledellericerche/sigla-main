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
 * Date 23/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import it.cnr.jada.persistency.Keyed;
public class DocumentoEleAcquistoBase extends DocumentoEleAcquistoKey implements Keyed {
//    TIPO_RIFACQUISTO VARCHAR(100)
	private java.lang.String tipoRifacquisto;
 
//    ACQUISTO_DOCUMENTO VARCHAR(20)
	private java.lang.String acquistoDocumento;
	
	private java.lang.Long numeroLinea;
 
//    ACQUISTO_DATA TIMESTAMP(7)
	private java.sql.Timestamp acquistoData;
 
//    ACQUISTO_NUMITEM VARCHAR(20)
	private java.lang.String acquistoNumitem;
 
//    ACQUISTO_COMMESSA VARCHAR(100)
	private java.lang.String acquistoCommessa;
 
//    ACQUISTO_CUP VARCHAR(15)
	private java.lang.String acquistoCup;
 
//    ACQUISTO_CIG VARCHAR(15)
	private java.lang.String acquistoCig;
 
//    ANOMALIE VARCHAR(2000)
	private java.lang.String anomalie;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_ACQUISTO
	 **/
	public DocumentoEleAcquistoBase() {
		super();
	}
	public DocumentoEleAcquistoBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi,java.lang.Long progressivo, java.lang.Long numeroLinea) {
		super(idPaese, idCodice, identificativoSdi, progressivo, numeroLinea);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoRifacquisto]
	 **/
	public java.lang.String getTipoRifacquisto() {
		return tipoRifacquisto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoRifacquisto]
	 **/
	public void setTipoRifacquisto(java.lang.String tipoRifacquisto)  {
		this.tipoRifacquisto=tipoRifacquisto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [acquistoDocumento]
	 **/
	public java.lang.String getAcquistoDocumento() {
		return acquistoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [acquistoDocumento]
	 **/
	public void setAcquistoDocumento(java.lang.String acquistoDocumento)  {
		this.acquistoDocumento=acquistoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [acquistoData]
	 **/
	public java.sql.Timestamp getAcquistoData() {
		return acquistoData;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [acquistoData]
	 **/
	public void setAcquistoData(java.sql.Timestamp acquistoData)  {
		this.acquistoData=acquistoData;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [acquistoNumitem]
	 **/
	public java.lang.String getAcquistoNumitem() {
		return acquistoNumitem;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [acquistoNumitem]
	 **/
	public void setAcquistoNumitem(java.lang.String acquistoNumitem)  {
		this.acquistoNumitem=acquistoNumitem;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [acquistoCommessa]
	 **/
	public java.lang.String getAcquistoCommessa() {
		return acquistoCommessa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [acquistoCommessa]
	 **/
	public void setAcquistoCommessa(java.lang.String acquistoCommessa)  {
		this.acquistoCommessa=acquistoCommessa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [acquistoCup]
	 **/
	public java.lang.String getAcquistoCup() {
		return acquistoCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [acquistoCup]
	 **/
	public void setAcquistoCup(java.lang.String acquistoCup)  {
		this.acquistoCup=acquistoCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [acquistoCig]
	 **/
	public java.lang.String getAcquistoCig() {
		return acquistoCig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [acquistoCig]
	 **/
	public void setAcquistoCig(java.lang.String acquistoCig)  {
		this.acquistoCig=acquistoCig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anomalie]
	 **/
	public java.lang.String getAnomalie() {
		return anomalie;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anomalie]
	 **/
	public void setAnomalie(java.lang.String anomalie)  {
		this.anomalie=anomalie;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroLinea]
	 **/
	public void setNumeroLinea(java.lang.Long numeroLinea)  {
		this.numeroLinea=numeroLinea;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroLinea]
	 **/
	public java.lang.Long getNumeroLinea() {
		return numeroLinea;
	}		
	
}