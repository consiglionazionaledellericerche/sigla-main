/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Tipo_progettoBulk;
import it.cnr.contab.util.Utility;
public class Geco_attivitaBulk extends Geco_attivitaBase implements Geco_moduloIBulk{
	private static final long serialVersionUID = 1L;

	public Geco_attivitaBulk() {
		super();
	}
	public Geco_attivitaBulk(java.lang.Long id_att, java.lang.Long esercizio, java.lang.String fase) {
		super(id_att, esercizio, fase);
	}
	public void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip){
		if (progetto_sip.getPg_progetto_padre()==null || !getId_comm().equals(new Long(progetto_sip.getPg_progetto_padre()))){
			progetto_sip.setProgettopadre(new Progetto_sipBulk(getEsercizio().intValue(),getId_comm().intValue(),getFase()));
			progetto_sip.setToBeUpdated();
		}
		if (!Utility.equalsNull(getCod_mod(), progetto_sip.getCd_progetto())){
			progetto_sip.setCd_progetto(getCod_mod());
			progetto_sip.setToBeUpdated();
		}
		if (!Utility.equalsNull(getDescr_mod(), progetto_sip.getDs_progetto())){
			progetto_sip.setDs_progetto(getDescr_mod());
			progetto_sip.setToBeUpdated();
		}
		/*
		if (getCod_tip() != null){
			if (getCod_tip().equals(new Long(1)) && (progetto_sip.getCd_tipo_progetto() == null || !progetto_sip.getCd_tipo_progetto().equals("PS"))){
				progetto_sip.setTipo(new Tipo_progettoBulk("PS"));
				progetto_sip.setToBeUpdated();
			}else if (getCod_tip().equals(new Long(2)) && (progetto_sip.getCd_tipo_progetto() == null ||!progetto_sip.getCd_tipo_progetto().equals("SC"))){
				progetto_sip.setTipo(new Tipo_progettoBulk("SC"));
				progetto_sip.setToBeUpdated();
			}else if (getCod_tip().equals(new Long(3)) && (progetto_sip.getCd_tipo_progetto() == null ||!progetto_sip.getCd_tipo_progetto().equals("MG"))){
				progetto_sip.setTipo(new Tipo_progettoBulk("MG"));
				progetto_sip.setToBeUpdated();
			}
		}*/
		if (getSede_princ_cdsuo() != null && !(getSede_princ_cdsuo().equals(progetto_sip.getCd_unita_organizzativa()))){
			progetto_sip.setUnita_organizzativa(new Unita_organizzativaBulk(getSede_princ_cdsuo()));
			progetto_sip.setToBeUpdated();
		}
		if (getCod_3rzo_gest() != null && progetto_sip.getCd_responsabile_terzo() != null && !getCod_3rzo_gest().equals(progetto_sip.getCd_responsabile_terzo().toString())){
			progetto_sip.setResponsabile(new TerzoBulk(new Integer(getCod_3rzo_gest())));
			progetto_sip.setToBeUpdated();
		}
		if (getData_inizio_attivita() != null && !getData_inizio_attivita().equals(progetto_sip.getDt_inizio())){
			progetto_sip.setDt_inizio(getData_inizio_attivita());
			progetto_sip.setToBeUpdated();
		}
		if (getEsito_negoz() != null){
			if (getEsito_negoz().equals(new Integer(2)) && !progetto_sip.getStato().equals(ProgettoBulk.TIPO_STATO_PROPOSTA)){
				progetto_sip.setStato(ProgettoBulk.TIPO_STATO_PROPOSTA);
				progetto_sip.setToBeUpdated();
			}else if (!getEsito_negoz().equals(new Integer(2)) && !progetto_sip.getStato().equals(ProgettoBulk.TIPO_STATO_APPROVATO)){
				progetto_sip.setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
				progetto_sip.setToBeUpdated();
			}
		}
	}
	public Long getId_mod() {
		return super.getId_att();
	}
	public String getCod_mod() {
		return super.getCod_att();
	}
	public String getDescr_mod() {
		return super.getDenom_att();
	}
	public Long getId_comm() {
		return super.getId_prog();
	}
	public String getCod_3rzo_gest() {
		return super.getCod_3rzo_refe();
	}	
}