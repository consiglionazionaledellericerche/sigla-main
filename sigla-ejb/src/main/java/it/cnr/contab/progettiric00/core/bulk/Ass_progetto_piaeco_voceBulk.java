package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;

public class Ass_progetto_piaeco_voceBulk extends Ass_progetto_piaeco_voceBase {
	private Progetto_piano_economicoBulk progetto_piano_economico;
	private Elemento_voceBulk elemento_voce;
	
	public Ass_progetto_piaeco_voceBulk() {
		super();
	}

	public Ass_progetto_piaeco_voceBulk(java.lang.Integer pg_progetto, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano,
			java.lang.Integer esercizio_voce, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce) {
		super(pg_progetto, cd_unita_organizzativa, cd_voce_piano, esercizio_piano, esercizio_voce, ti_appartenenza, ti_gestione, cd_elemento_voce);
	}

	public Progetto_piano_economicoBulk getProgetto_piano_economico() {
		return progetto_piano_economico;
	}
	
	public void setProgetto_piano_economico(Progetto_piano_economicoBulk progetto_piano_economico) {
		this.progetto_piano_economico = progetto_piano_economico;
	}
	
	@Override
	public Integer getPg_progetto() {
		Progetto_piano_economicoBulk progetto_piano_economico = this.getProgetto_piano_economico();
		if (progetto_piano_economico == null)
			return null;
		return progetto_piano_economico.getPg_progetto();
	}
	
	@Override
	public void setPg_progetto(Integer pg_progetto) {
		this.getProgetto_piano_economico().setPg_progetto(pg_progetto);
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		Progetto_piano_economicoBulk progetto_piano_economico = this.getProgetto_piano_economico();
		if (progetto_piano_economico == null)
			return null;
		return progetto_piano_economico.getCd_unita_organizzativa();
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getProgetto_piano_economico().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	
	@Override
	public String getCd_voce_piano() {
		Progetto_piano_economicoBulk progetto_piano_economico = this.getProgetto_piano_economico();
		if (progetto_piano_economico == null)
			return null;
		return progetto_piano_economico.getCd_voce_piano();
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		this.getProgetto_piano_economico().setCd_voce_piano(cd_voce_piano);
	}
	
	@Override
	public Integer getEsercizio_piano() {
		Progetto_piano_economicoBulk progetto_piano_economico = this.getProgetto_piano_economico();
		if (progetto_piano_economico == null)
			return null;
		return progetto_piano_economico.getEsercizio_piano();
	}
	
	@Override
	public void setEsercizio_piano(Integer esercizio_piano) {
		this.getProgetto_piano_economico().setEsercizio_piano(esercizio_piano);
	}
	
	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
	
	public void setElemento_voce(Elemento_voceBulk elemento_voce) {
		this.elemento_voce = elemento_voce;
	}
	
	@Override
	public Integer getEsercizio_voce() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getEsercizio();
	}
	
	@Override
	public void setEsercizio_voce(Integer esercizio) {
		this.getElemento_voce().setEsercizio(esercizio);
	}

	@Override
	public String getTi_appartenenza() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_appartenenza();
	}
	
	@Override
	public void setTi_appartenenza(String ti_appartenenza) {
		this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
	
	@Override
	public String getTi_gestione() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_gestione();
	}
	
	@Override
	public void setTi_gestione(String ti_gestione) {
		this.getElemento_voce().setTi_gestione(ti_gestione);
	}
	
	@Override
	public String getCd_elemento_voce() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getCd_elemento_voce();
	}
	
	@Override
	public void setCd_elemento_voce(String cd_elemento_voce) {
		this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
	}
}
