package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;

public class Progetto_other_fieldBulk extends Progetto_other_fieldBase {
	public static final String STATO_INIZIALE = "I";
	public static final String STATO_NEGOZIAZIONE = "N";
	public static final String STATO_APPROVATO = "A";
	public static final String STATO_ANNULLATO = "U";
	public static final String STATO_CHIUSO = "C";
	public static final String STATO_MIGRAZIONE = "M";


	private Pdg_programmaBulk pdgProgramma;
	
	private Pdg_missioneBulk pdgMissione;
	
	private TipoFinanziamentoBulk tipoFinanziamento;
	
	public Progetto_other_fieldBulk() {
		super();
	}

	public Progetto_other_fieldBulk(java.lang.Integer pg_progetto) {
		super(pg_progetto);
	}
	
	public Pdg_programmaBulk getPdgProgramma() {
		return pdgProgramma;
	}
	
	public void setPdgProgramma(Pdg_programmaBulk pdgProgramma) {
		this.pdgProgramma = pdgProgramma;
	}
	@Override
	public String getCd_programma() {
		Pdg_programmaBulk pdgProgramma = this.getPdgProgramma();
		if (pdgProgramma == null)
			return null;
		return pdgProgramma.getCd_programma();
	}
	
	@Override
	public void setCd_programma(String cd_programma) {
		this.getPdgProgramma().setCd_programma(cd_programma);
	}

	public Pdg_missioneBulk getPdgMissione() {
		return pdgMissione;
	}
	
	public void setPdgMissione(Pdg_missioneBulk pdgMissione) {
		this.pdgMissione = pdgMissione;
	}
	@Override
	public String getCd_missione() {
		Pdg_missioneBulk pdgMissione = this.getPdgMissione();
		if (pdgMissione == null)
			return null;
		return pdgMissione.getCd_missione();
	}
	
	@Override
	public void setCd_missione(String cd_missione) {
		this.getPdgMissione().setCd_missione(cd_missione);
	}
	
	public TipoFinanziamentoBulk getTipoFinanziamento() {
		return tipoFinanziamento;
	}
	
	public void setTipoFinanziamento(TipoFinanziamentoBulk tipoFinanziamento) {
		this.tipoFinanziamento = tipoFinanziamento;
	}
	
	@Override
	public Long getIdTipoFinanziamento() {
		TipoFinanziamentoBulk tipoFinanziamento = this.getTipoFinanziamento();
		if (tipoFinanziamento == null)
			return null;
		return super.getIdTipoFinanziamento();
	}
	
	@Override
	public void setIdTipoFinanziamento(Long idTipoFinanziamento) {
		this.getTipoFinanziamento().setId(idTipoFinanziamento);
	}
}
