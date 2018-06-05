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