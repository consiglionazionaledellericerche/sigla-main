/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 25/03/2008
 */
package it.cnr.contab.pdg00.bulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class V_stm_paramin_sit_sint_gaeBulk extends V_stm_paramin_sit_sint_gaeBase {
	
	public V_stm_paramin_sit_sint_gaeBulk() {
		super();
	}
	public V_stm_paramin_sit_sint_gaeBulk(WorkpackageBulk gae) {
		
		this();
		completeFrom(gae);
	}
	
	private void completeFrom(WorkpackageBulk gae) {

		if (gae == null) return;

		setCdr(gae.getCd_centro_responsabilita());
		setGae(gae.getCd_linea_attivita());
	}
}