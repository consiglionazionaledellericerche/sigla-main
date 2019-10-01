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
public class DocumentoEleDdtBase extends DocumentoEleDdtKey implements Keyed {
//    DDT_NUMERO VARCHAR(20)
	private java.lang.String ddtNumero;
 
//    DDT_DATA TIMESTAMP(7)
	private java.sql.Timestamp ddtData;
 
//    NUMERO_LINEA DECIMAL(4,0)
	private java.lang.Integer numeroLinea;
 
//    ANOMALIE VARCHAR(2000)
	private java.lang.String anomalie;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_DDT
	 **/
	public DocumentoEleDdtBase() {
		super();
	}
	public DocumentoEleDdtBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo, java.lang.Long progressivoDdt) {
		super(idPaese, idCodice, identificativoSdi,progressivo,progressivoDdt);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ddtNumero]
	 **/
	public java.lang.String getDdtNumero() {
		return ddtNumero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ddtNumero]
	 **/
	public void setDdtNumero(java.lang.String ddtNumero)  {
		this.ddtNumero=ddtNumero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ddtData]
	 **/
	public java.sql.Timestamp getDdtData() {
		return ddtData;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ddtData]
	 **/
	public void setDdtData(java.sql.Timestamp ddtData)  {
		this.ddtData=ddtData;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroLinea]
	 **/
	public java.lang.Integer getNumeroLinea() {
		return numeroLinea;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroLinea]
	 **/
	public void setNumeroLinea(java.lang.Integer numeroLinea)  {
		this.numeroLinea=numeroLinea;
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
}