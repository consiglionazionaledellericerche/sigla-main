/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class AssUnitaOperativaOrdBase extends AssUnitaOperativaOrdKey implements Keyed {
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ASS_UNITA_OPERATIVA_ORD
	 **/
	public AssUnitaOperativaOrdBase() {
		super();
	}
	public AssUnitaOperativaOrdBase(java.lang.String cdUnitaOperativa, java.lang.String cdUnitaOperativaRif) {
		super(cdUnitaOperativa, cdUnitaOperativaRif);
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