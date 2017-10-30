/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class CausaleAnnullOrdBase extends CausaleAnnullOrdKey implements Keyed {
//    DS_CAUSALE_ANNULL VARCHAR(1000) NOT NULL
	private java.lang.String dsCausaleAnnull;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CAUSALE_ANNULL_ORD
	 **/
	public CausaleAnnullOrdBase() {
		super();
	}
	public CausaleAnnullOrdBase(java.lang.String cdCausaleAnnull) {
		super(cdCausaleAnnull);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCausaleAnnull]
	 **/
	public java.lang.String getDsCausaleAnnull() {
		return dsCausaleAnnull;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCausaleAnnull]
	 **/
	public void setDsCausaleAnnull(java.lang.String dsCausaleAnnull)  {
		this.dsCausaleAnnull=dsCausaleAnnull;
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