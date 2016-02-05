/*
* Created by Generator 1.0
* Date 27/04/2006
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
public class V_cons_stato_invio_reversaliBulk extends V_cons_stato_invio_reversaliBase implements V_cons_stato_invio{
	private String contabile;
	public V_cons_stato_invio_reversaliBulk() {
		super();
	}

	public Long getProgressivo() {
		return getPg_reversale();
	}
	public String getTipo() {
		return Numerazione_doc_contBulk.TIPO_REV;
	}
	public String getContabile() {
		return contabile;
	}
	public void setContabile(String contabile) {
		this.contabile = contabile;
	}	
}