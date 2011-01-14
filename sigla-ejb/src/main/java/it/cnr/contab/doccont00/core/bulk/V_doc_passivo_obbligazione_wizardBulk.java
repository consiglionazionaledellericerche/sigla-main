/*
 * Created on Apr 6, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;

public class V_doc_passivo_obbligazione_wizardBulk extends V_doc_passivo_obbligazioneBulk {
	Mandato_rigaBulk mandatoRiga;
	
	public V_doc_passivo_obbligazione_wizardBulk() {
		super();
	}

	public Mandato_rigaBulk getMandatoRiga() {
		return mandatoRiga;
	}

	public void setMandatoRiga(Mandato_rigaBulk mandatoRiga) {
		this.mandatoRiga = mandatoRiga;
	}
}
