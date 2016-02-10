/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class ElaboraNumUnicoFatturaPBulk extends it.cnr.jada.bulk.OggettoBulk {
	java.sql.Timestamp dataRegistrazioneA; 
	
	public java.sql.Timestamp getDataRegistrazioneA() {
		return dataRegistrazioneA;
	}

	public void setDataRegistrazioneA(java.sql.Timestamp dataRegistrazioneA) {
		this.dataRegistrazioneA = dataRegistrazioneA;
	}

	public ElaboraNumUnicoFatturaPBulk() {
		super();
	}
	
}