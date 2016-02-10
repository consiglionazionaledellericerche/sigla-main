/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 10/07/2015
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class AssBpAccessoBulk extends AssBpAccessoBase {
	/**
	 * [ACCESSO Contiene gli accessi definiti per l"applicazione; si tratta di dati di configurazione cablati.]
	 **/
	private AccessoBulk accesso =  new AccessoBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ASS_BP_ACCESSO
	 **/
	public AssBpAccessoBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ASS_BP_ACCESSO
	 **/
	public AssBpAccessoBulk(java.lang.String cdAccesso, java.lang.String businessProcess) {
		super(cdAccesso, businessProcess);
		setAccesso( new AccessoBulk(cdAccesso) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Contiene gli accessi definiti per l"applicazione; si tratta di dati di configurazione cablati.]
	 **/
	public AccessoBulk getAccesso() {
		return accesso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Contiene gli accessi definiti per l"applicazione; si tratta di dati di configurazione cablati.]
	 **/
	public void setAccesso(AccessoBulk accesso)  {
		this.accesso=accesso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccesso]
	 **/
	public java.lang.String getCdAccesso() {
		AccessoBulk accesso = this.getAccesso();
		if (accesso == null)
			return null;
		return getAccesso().getCd_accesso();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccesso]
	 **/
	public void setCdAccesso(java.lang.String cdAccesso)  {
		this.getAccesso().setCd_accesso(cdAccesso);
	}
}