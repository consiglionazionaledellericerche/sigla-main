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
public class DocumentoEleScontoMaggBase extends DocumentoEleScontoMaggKey implements Keyed {
//    TIPO_SCONTOMAGG VARCHAR(2)
	private java.lang.String tipoScontomagg;
 
//    PERCENTUALE_SCONTOMAGG DECIMAL(17,2)
	private java.math.BigDecimal percentualeScontomagg;
 
//    IMPORTO_SCONTOMAGG DECIMAL(17,2)
	private java.math.BigDecimal importoScontomagg;
 
//    ANOMALIE VARCHAR(2000)
	private java.lang.String anomalie;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_SCONTO_MAGG
	 **/
	public DocumentoEleScontoMaggBase() {
		super();
	}
	public DocumentoEleScontoMaggBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo, java.lang.Long progressivoSconto) {
		super(idPaese, idCodice, identificativoSdi, progressivo, progressivoSconto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoScontomagg]
	 **/
	public java.lang.String getTipoScontomagg() {
		return tipoScontomagg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoScontomagg]
	 **/
	public void setTipoScontomagg(java.lang.String tipoScontomagg)  {
		this.tipoScontomagg=tipoScontomagg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [percentualeScontomagg]
	 **/
	public java.math.BigDecimal getPercentualeScontomagg() {
		return percentualeScontomagg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [percentualeScontomagg]
	 **/
	public void setPercentualeScontomagg(java.math.BigDecimal percentualeScontomagg)  {
		this.percentualeScontomagg=percentualeScontomagg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoScontomagg]
	 **/
	public java.math.BigDecimal getImportoScontomagg() {
		return importoScontomagg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoScontomagg]
	 **/
	public void setImportoScontomagg(java.math.BigDecimal importoScontomagg)  {
		this.importoScontomagg=importoScontomagg;
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