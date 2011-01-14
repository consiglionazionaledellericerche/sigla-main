/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Geco_progetto_sacBulk extends Geco_progetto_sacBase implements Geco_progettoIBulk{
	public Geco_progetto_sacBulk() {
		super();
	}
	public Geco_progetto_sacBulk(java.lang.Long id_prog, java.lang.Long esercizio, java.lang.String fase) {
		super(id_prog, esercizio, fase);
	}
	public void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip){
		if (!getCod_prog().equals(progetto_sip.getCd_progetto())){
			progetto_sip.setCd_progetto(getCod_prog());
			progetto_sip.setToBeUpdated();
		}
		if (!getDescr_prog().equals(progetto_sip.getDs_progetto())){
			progetto_sip.setDs_progetto(getDescr_prog());
			progetto_sip.setToBeUpdated();
		}
		if (!getData_istituzione_prog().equals(progetto_sip.getDt_inizio())){
			progetto_sip.setDt_inizio(getData_istituzione_prog());
			progetto_sip.setToBeUpdated();
		}
	}
}