/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.persistency.Keyed;
public class TipoOrdineBase extends TipoOrdineKey implements Keyed {
//    DS_TIPO_ORDINE VARCHAR(1000) NOT NULL
	private java.lang.String dsTipoOrdine;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_ORDINE
	 **/
	public TipoOrdineBase() {
		super();
	}
	public TipoOrdineBase(java.lang.String cdTipoOrdine) {
		super(cdTipoOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTipoOrdine]
	 **/
	public java.lang.String getDsTipoOrdine() {
		return dsTipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTipoOrdine]
	 **/
	public void setDsTipoOrdine(java.lang.String dsTipoOrdine)  {
		this.dsTipoOrdine=dsTipoOrdine;
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