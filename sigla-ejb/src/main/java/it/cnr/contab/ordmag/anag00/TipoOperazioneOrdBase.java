/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class TipoOperazioneOrdBase extends TipoOperazioneOrdKey implements Keyed {
//    DS_TIPO_OPERAZIONE VARCHAR(100) NOT NULL
	private java.lang.String dsTipoOperazione;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_OPERAZIONE_ORD
	 **/
	public TipoOperazioneOrdBase() {
		super();
	}
	public TipoOperazioneOrdBase(java.lang.String cdTipoOperazione) {
		super(cdTipoOperazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTipoOperazione]
	 **/
	public java.lang.String getDsTipoOperazione() {
		return dsTipoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTipoOperazione]
	 **/
	public void setDsTipoOperazione(java.lang.String dsTipoOperazione)  {
		this.dsTipoOperazione=dsTipoOperazione;
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