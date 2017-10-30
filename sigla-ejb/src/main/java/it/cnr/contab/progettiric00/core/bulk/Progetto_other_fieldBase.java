package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Progetto_other_fieldBase extends Progetto_other_fieldKey implements Keyed {
	private String cd_programma;

	private String cd_missione;

	//FL_PIANO_ECONOMICO CHAR(1 BYTE) DEFAULT 'N' NOT NULL
	private java.lang.Boolean fl_piano_economico;

	public Progetto_other_fieldBase() {
		super();
	}
	
	public Progetto_other_fieldBase(java.lang.Integer pg_progetto) {
		super(pg_progetto);
	}

	public String getCd_programma() {
		return cd_programma;
	}
	
	public void setCd_programma(String cd_programma) {
		this.cd_programma = cd_programma;
	}
	
	public String getCd_missione() {
		return cd_missione;
	}
	
	public void setCd_missione(String cd_missione) {
		this.cd_missione = cd_missione;
	}
	
	public java.lang.Boolean getFl_piano_economico() {
		return fl_piano_economico;
	}
	
	public void setFl_piano_economico(java.lang.Boolean fl_piano_economico) {
		this.fl_piano_economico = fl_piano_economico;
	}
}
