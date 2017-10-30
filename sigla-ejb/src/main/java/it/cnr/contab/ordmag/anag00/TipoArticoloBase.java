/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class TipoArticoloBase extends TipoArticoloKey implements Keyed {
//    DS_TIPO_ARTICOLO VARCHAR(100) NOT NULL
	private java.lang.String dsTipoArticolo;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_ARTICOLO
	 **/
	public TipoArticoloBase() {
		super();
	}
	public TipoArticoloBase(java.lang.String cdTipoArticolo) {
		super(cdTipoArticolo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTipoArticolo]
	 **/
	public java.lang.String getDsTipoArticolo() {
		return dsTipoArticolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTipoArticolo]
	 **/
	public void setDsTipoArticolo(java.lang.String dsTipoArticolo)  {
		this.dsTipoArticolo=dsTipoArticolo;
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