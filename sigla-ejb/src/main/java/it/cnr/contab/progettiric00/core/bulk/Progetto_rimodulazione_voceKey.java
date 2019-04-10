package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Progetto_rimodulazione_voceKey extends OggettoBulk implements KeyedPersistent {
	// PG_PROGETTO NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

	// PG_RIMODULAZIONE NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_rimodulazione;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// CD_VOCE_PIANO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_voce_piano;

	// ESERCIZIO_PIANO NUMBER (4) NOT NULL (PK)
	private java.lang.Integer esercizio_piano;

	// PG_RIMODULAZIONE NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_variazione;

	public Progetto_rimodulazione_voceKey() {
		super();
	}
	
	public Progetto_rimodulazione_voceKey(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano, java.lang.Integer pg_variazione) {
		super();
		this.pg_progetto = pg_progetto;
		this.pg_rimodulazione = pg_rimodulazione;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.cd_voce_piano = cd_voce_piano;
		this.esercizio_piano = esercizio_piano;
		this.pg_variazione = pg_variazione;
	}
	
	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}
	
	public void setPg_progetto(java.lang.Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}
	
	public java.lang.Integer getPg_rimodulazione() {
		return pg_rimodulazione;
	}
	
	public void setPg_rimodulazione(java.lang.Integer pg_rimodulazione) {
		this.pg_rimodulazione = pg_rimodulazione;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}

	public java.lang.String getCd_voce_piano() {
		return cd_voce_piano;
	}
	
	public void setCd_voce_piano(java.lang.String cd_voce_piano) {
		this.cd_voce_piano = cd_voce_piano;
	}
	
	public java.lang.Integer getEsercizio_piano() {
		return esercizio_piano;
	}
	
	public void setEsercizio_piano(java.lang.Integer esercizio_piano) {
		this.esercizio_piano = esercizio_piano;
	}
	
	public java.lang.Integer getPg_variazione() {
		return pg_variazione;
	}
	
	public void setPg_variazione(java.lang.Integer pg_variazione) {
		this.pg_variazione = pg_variazione;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Progetto_rimodulazione_voceKey)) return false;
		Progetto_rimodulazione_voceKey k = (Progetto_rimodulazione_voceKey)o;
		if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
		if(!compareKey(getPg_rimodulazione(),k.getPg_rimodulazione())) return false;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getCd_voce_piano(),k.getCd_voce_piano())) return false;
		if(!compareKey(getEsercizio_piano(),k.getEsercizio_piano())) return false;
		if(!compareKey(getPg_variazione(),k.getPg_variazione())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getPg_progetto())+
			calculateKeyHashCode(getPg_rimodulazione())+
			calculateKeyHashCode(getCd_unita_organizzativa())+
			calculateKeyHashCode(getCd_voce_piano())+
			calculateKeyHashCode(getEsercizio_piano())+
			calculateKeyHashCode(getPg_variazione());
	}
}
