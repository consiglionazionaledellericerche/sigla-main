/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 10/07/2015
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class AssBpAccessoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdAccesso;
	private java.lang.String businessProcess;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ASS_BP_ACCESSO
	 **/
	public AssBpAccessoKey() {
		super();
	}
	public AssBpAccessoKey(java.lang.String cdAccesso, java.lang.String businessProcess) {
		super();
		this.cdAccesso=cdAccesso;
		this.businessProcess=businessProcess;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof AssBpAccessoKey)) return false;
		AssBpAccessoKey k = (AssBpAccessoKey) o;
		if (!compareKey(getCdAccesso(), k.getCdAccesso())) return false;
		if (!compareKey(getBusinessProcess(), k.getBusinessProcess())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdAccesso());
		i = i + calculateKeyHashCode(getBusinessProcess());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccesso]
	 **/
	public void setCdAccesso(java.lang.String cdAccesso)  {
		this.cdAccesso=cdAccesso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccesso]
	 **/
	public java.lang.String getCdAccesso() {
		return cdAccesso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [businessProcess]
	 **/
	public void setBusinessProcess(java.lang.String businessProcess)  {
		this.businessProcess=businessProcess;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [businessProcess]
	 **/
	public java.lang.String getBusinessProcess() {
		return businessProcess;
	}
}