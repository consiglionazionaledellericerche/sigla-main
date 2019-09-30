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
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.persistency.Keyed;
public class InformazioniContoEvidenzaBase extends InformazioniContoEvidenzaKey implements Keyed {
//    DESCRIZIONE_CONTO_EVIDENZA VARCHAR(50)
	private java.lang.String descrizioneContoEvidenza;
 
//    SALDO_PRECEDENTE_CONTO_EVID DECIMAL(15,2)
	private java.math.BigDecimal saldoPrecedenteContoEvid;
 
//    TOTALE_ENTRATE_CONTO_EVIDENZA DECIMAL(15,2)
	private java.math.BigDecimal totaleEntrateContoEvidenza;
 
//    TOTALE_USCITE_CONTO_EVIDENZA DECIMAL(15,2)
	private java.math.BigDecimal totaleUsciteContoEvidenza;
 
//    SALDO_FINALE_CONTO_EVIDENZA DECIMAL(15,2)
	private java.math.BigDecimal saldoFinaleContoEvidenza;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: INFORMAZIONI_CONTO_EVIDENZA
	 **/
	public InformazioniContoEvidenzaBase() {
		super();
	}
	public InformazioniContoEvidenzaBase(java.lang.Integer esercizio, java.lang.String identificativoFlusso, java.lang.String contoEvidenza) {
		super(esercizio, identificativoFlusso, contoEvidenza);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descrizioneContoEvidenza]
	 **/
	public java.lang.String getDescrizioneContoEvidenza() {
		return descrizioneContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descrizioneContoEvidenza]
	 **/
	public void setDescrizioneContoEvidenza(java.lang.String descrizioneContoEvidenza)  {
		this.descrizioneContoEvidenza=descrizioneContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [saldoPrecedenteContoEvid]
	 **/
	public java.math.BigDecimal getSaldoPrecedenteContoEvid() {
		return saldoPrecedenteContoEvid;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [saldoPrecedenteContoEvid]
	 **/
	public void setSaldoPrecedenteContoEvid(java.math.BigDecimal saldoPrecedenteContoEvid)  {
		this.saldoPrecedenteContoEvid=saldoPrecedenteContoEvid;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleEntrateContoEvidenza]
	 **/
	public java.math.BigDecimal getTotaleEntrateContoEvidenza() {
		return totaleEntrateContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleEntrateContoEvidenza]
	 **/
	public void setTotaleEntrateContoEvidenza(java.math.BigDecimal totaleEntrateContoEvidenza)  {
		this.totaleEntrateContoEvidenza=totaleEntrateContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleUsciteContoEvidenza]
	 **/
	public java.math.BigDecimal getTotaleUsciteContoEvidenza() {
		return totaleUsciteContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleUsciteContoEvidenza]
	 **/
	public void setTotaleUsciteContoEvidenza(java.math.BigDecimal totaleUsciteContoEvidenza)  {
		this.totaleUsciteContoEvidenza=totaleUsciteContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [saldoFinaleContoEvidenza]
	 **/
	public java.math.BigDecimal getSaldoFinaleContoEvidenza() {
		return saldoFinaleContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [saldoFinaleContoEvidenza]
	 **/
	public void setSaldoFinaleContoEvidenza(java.math.BigDecimal saldoFinaleContoEvidenza)  {
		this.saldoFinaleContoEvidenza=saldoFinaleContoEvidenza;
	}
}