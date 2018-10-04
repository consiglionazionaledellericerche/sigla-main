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
public class Geco_progetto_operativoBulk extends Geco_progetto_operativoBase implements Geco_commessaIBulk{
	private static final long serialVersionUID = 1L;

	public Geco_progetto_operativoBulk() {
		super();
	}
	public Geco_progetto_operativoBulk(java.lang.Long id_prog, java.lang.Long esercizio, java.lang.String fase) {
		super(id_prog, esercizio, fase);
	}
	public void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip){
		if (!getId_prog_padre().equals(new Long(progetto_sip.getPg_progetto_padre()))){
			progetto_sip.setProgettopadre(new Progetto_sipBulk(getEsercizio().intValue(),getId_prog_padre().intValue(),getFase()));
			progetto_sip.setToBeUpdated();
		}
		if (!getCod_comm().equals(progetto_sip.getCd_progetto())){
			progetto_sip.setCd_progetto(getCod_comm());
			progetto_sip.setToBeUpdated();
		}
		if (!getDescr_comm().equals(progetto_sip.getDs_progetto())){
			progetto_sip.setDs_progetto(getDescr_comm());
			progetto_sip.setToBeUpdated();
		}
		/*if (getCod_tip() != null){
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
		if (getCds() != null && getSede_svol_uo() != null && !((getCds()+"."+getSede_svol_uo()).equals(progetto_sip.getCd_unita_organizzativa()))){
			progetto_sip.setUnita_organizzativa(new Unita_organizzativaBulk(getCds()+"."+getSede_svol_uo()));
			progetto_sip.setToBeUpdated();
		}
		if (getCod_3rzo_resp() != null && progetto_sip.getCd_responsabile_terzo() != null && !getCod_3rzo_resp().equals(progetto_sip.getCd_responsabile_terzo().toString())){
			progetto_sip.setResponsabile(new TerzoBulk(new Integer(getCod_3rzo_resp())));
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
	public Long getId_comm() {
		return super.getId_prog();
	}
	public String getCod_comm() {
		return super.getCod_prog();
	}
	public String getDescr_comm() {
		return super.getDenom_prog();
	}
	public Long getId_prog_padre() {
		return getId_area();
	}
}