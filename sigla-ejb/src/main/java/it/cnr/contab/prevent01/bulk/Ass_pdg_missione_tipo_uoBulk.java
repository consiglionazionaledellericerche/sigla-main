/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;

import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk;

public class Ass_pdg_missione_tipo_uoBulk extends Ass_pdg_missione_tipo_uoBase {
	private static final long serialVersionUID = 1L;

	private Pdg_missioneBulk pdgMissione = new Pdg_missioneBulk();

	private Tipo_unita_organizzativaBulk tipoUnitaOrganizzativa = new Tipo_unita_organizzativaBulk();

	public Ass_pdg_missione_tipo_uoBulk() {
		super();
	}
	
	public Ass_pdg_missione_tipo_uoBulk(java.lang.String cd_missione, java.lang.String cd_tipo_unita) {
		super(cd_missione, cd_tipo_unita);
		setPdgMissione(new Pdg_missioneBulk(cd_missione));
		setTipoUnitaOrganizzativa(new Tipo_unita_organizzativaBulk(cd_tipo_unita));
	}
	
	public Pdg_missioneBulk getPdgMissione() {
		return pdgMissione;
	}
	
	public void setPdgMissione(Pdg_missioneBulk pdgMissione) {
		this.pdgMissione = pdgMissione;
	}
	
	public Tipo_unita_organizzativaBulk getTipoUnitaOrganizzativa() {
		return tipoUnitaOrganizzativa;
	}
	
	public void setTipoUnitaOrganizzativa(Tipo_unita_organizzativaBulk tipoUnitaOrganizzativa) {
		this.tipoUnitaOrganizzativa = tipoUnitaOrganizzativa;
	}
	
	public String getCd_missione() {
		Pdg_missioneBulk pdgMissione=this.getPdgMissione();
		if (pdgMissione==null)
			return null;
		return pdgMissione.getCd_missione();
	}

	public void setCd_missione(String cd_missione) {
		pdgMissione.setCd_missione(cd_missione);
	}
	
	public String getCd_tipo_unita() {
		Tipo_unita_organizzativaBulk tipoUnitaOrganizzativa=this.getTipoUnitaOrganizzativa();
		if (tipoUnitaOrganizzativa==null)
			return null;
		return tipoUnitaOrganizzativa.getCd_tipo_unita();
	}

	public void setCd_tipo_unita(String cd_tipo_unita) {
		tipoUnitaOrganizzativa.setCd_tipo_unita(cd_tipo_unita);
	}
}