package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Ass_progetto_piaeco_voceKey extends OggettoBulk implements KeyedPersistent {
	// PG_PROGETTO NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// CD_VOCE_PIANO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_voce_piano;

	// ESERCIZIO_PIANO NUMBER (4) NOT NULL (PK)
	private java.lang.Integer esercizio_piano;

	// ESERCIZIO_VOCE DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_voce;

	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce;

	public Ass_progetto_piaeco_voceKey() {
		super();
	}
	
	public Ass_progetto_piaeco_voceKey(java.lang.Integer pg_progetto, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano,
			java.lang.Integer esercizio_voce, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce) {
		super();
		this.pg_progetto = pg_progetto;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.cd_voce_piano = cd_voce_piano;
		this.esercizio_piano = esercizio_piano;
		this.esercizio_voce = esercizio_voce;
		this.ti_appartenenza = ti_appartenenza;
		this.ti_gestione = ti_gestione;
		this.cd_elemento_voce = cd_elemento_voce;
	}
	
	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}
	
	public void setPg_progetto(java.lang.Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
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
	
	public java.lang.Integer getEsercizio_voce() {
		return esercizio_voce;
	}
	
	public void setEsercizio_voce(java.lang.Integer esercizio_voce) {
		this.esercizio_voce = esercizio_voce;
	}
	
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}
	
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}
	
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}
	
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}

	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Ass_progetto_piaeco_voceKey)) return false;
		Ass_progetto_piaeco_voceKey k = (Ass_progetto_piaeco_voceKey)o;
		if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getCd_voce_piano(),k.getCd_voce_piano())) return false;
		if(!compareKey(getEsercizio_piano(),k.getEsercizio_piano())) return false;
		if(!compareKey(getEsercizio_voce(),k.getEsercizio_voce())) return false;
		if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
		if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
		if(!compareKey(getCd_elemento_voce(),k.getCd_elemento_voce())) return false;

		return true;
	}
	
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getPg_progetto())+
			calculateKeyHashCode(getCd_unita_organizzativa())+
			calculateKeyHashCode(getCd_voce_piano())+
			calculateKeyHashCode(getEsercizio_piano()) +
			calculateKeyHashCode(getEsercizio_voce())+
			calculateKeyHashCode(getTi_appartenenza())+
			calculateKeyHashCode(getTi_gestione())+
			calculateKeyHashCode(getCd_elemento_voce());
	}
}
