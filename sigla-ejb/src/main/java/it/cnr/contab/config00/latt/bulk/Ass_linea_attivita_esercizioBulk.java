package it.cnr.contab.config00.latt.bulk;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;


public class Ass_linea_attivita_esercizioBulk extends Ass_linea_attivita_esercizioBase implements CostantiTi_gestione {
	private static final long serialVersionUID = 1L;

	protected WorkpackageBulk linea_attivita;

	protected ProgettoBulk progetto;

	public Ass_linea_attivita_esercizioBulk() {
		super();
	}

	public Ass_linea_attivita_esercizioBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita) {
		super(esercizio, cd_centro_responsabilita,cd_linea_attivita);
		setLinea_attivita(new WorkpackageBulk(cd_centro_responsabilita, cd_linea_attivita));
	}

	public WorkpackageBulk getLinea_attivita() {
		return linea_attivita;
	}
	
	public void setLinea_attivita(WorkpackageBulk linea_attivita) {
		this.linea_attivita = linea_attivita;
	}
	
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}

	@Override
	public String getCd_centro_responsabilita() {
		WorkpackageBulk linea_attivita = this.getLinea_attivita();
		if (linea_attivita == null)
			return null;
		return linea_attivita.getCd_centro_responsabilita();
	}
	
	@Override
	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		this.getLinea_attivita().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	
	@Override
	public String getCd_linea_attivita() {
		WorkpackageBulk linea_attivita = this.getLinea_attivita();
		if (linea_attivita == null)
			return null;
		return linea_attivita.getCd_linea_attivita();
	}
	
	@Override
	public void setCd_linea_attivita(String cd_linea_attivita) {
		this.getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
	}

	public java.lang.Integer getPg_progetto() {
		ProgettoBulk progetto = this.getProgetto();
		if (progetto == null)
			return null;
		return progetto.getPg_progetto();
	}

	public void setPg_progetto(java.lang.Integer progetto) {
		this.getProgetto().setPg_progetto(progetto);
	}
}
