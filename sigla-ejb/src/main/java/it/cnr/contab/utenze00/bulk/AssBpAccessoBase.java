/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 10/07/2015
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class AssBpAccessoBase extends AssBpAccessoKey implements Keyed {
//    TI_FUNZIONE CHAR(1)
	private java.lang.String tiFunzione;
 
//    ESERCIZIO_INIZIO_VALIDITA DECIMAL(4,0)
	private java.lang.Integer esercizioInizioValidita;
 
//    ESERCIZIO_FINE_VALIDITA DECIMAL(4,0)
	private java.lang.Integer esercizioFineValidita;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ASS_BP_ACCESSO
	 **/
	public AssBpAccessoBase() {
		super();
	}
	public AssBpAccessoBase(java.lang.String cdAccesso, java.lang.String businessProcess) {
		super(cdAccesso, businessProcess);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiFunzione]
	 **/
	public java.lang.String getTiFunzione() {
		return tiFunzione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiFunzione]
	 **/
	public void setTiFunzione(java.lang.String tiFunzione)  {
		this.tiFunzione=tiFunzione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioInizioValidita]
	 **/
	public java.lang.Integer getEsercizioInizioValidita() {
		return esercizioInizioValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioInizioValidita]
	 **/
	public void setEsercizioInizioValidita(java.lang.Integer esercizioInizioValidita)  {
		this.esercizioInizioValidita=esercizioInizioValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioFineValidita]
	 **/
	public java.lang.Integer getEsercizioFineValidita() {
		return esercizioFineValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioFineValidita]
	 **/
	public void setEsercizioFineValidita(java.lang.Integer esercizioFineValidita)  {
		this.esercizioFineValidita=esercizioFineValidita;
	}
}