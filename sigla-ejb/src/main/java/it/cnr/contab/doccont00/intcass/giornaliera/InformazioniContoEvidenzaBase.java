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