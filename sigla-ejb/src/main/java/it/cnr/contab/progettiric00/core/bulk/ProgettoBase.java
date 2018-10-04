package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ProgettoBase extends ProgettoKey implements Keyed {
	// CD_PROGETTO VARCHAR(10) NOT NULL
	private java.lang.String cd_progetto;
	
	// CD_DIVISA VARCHAR(10)
	private java.lang.String cd_divisa;

	// CD_ENTE_FIN DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_ente_fin;

	// CD_RESPONSABILE_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_responsabile_terzo;

	// CD_TIPO_PROGETTO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_progetto;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// DS_PROGETTO VARCHAR(100) NOT NULL
	private java.lang.String ds_progetto;

	// DT_FINE TIMESTAMP
	private java.sql.Timestamp dt_fine;

	// DT_INIZIO TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_inizio;

	// DT_PROROGA TIMESTAMP
	private java.sql.Timestamp dt_proroga;

	// IMPORTO_DIVISA DECIMAL(20,6)
	private java.math.BigDecimal importo_divisa;

	// IMPORTO_FONDO DECIMAL(20,6) NOT NULL
	private java.math.BigDecimal importo_progetto;

	// NOTE VARCHAR(2000)
	private java.lang.String note;

	// STATO VARCHAR(1)
	private java.lang.String stato;

	// DURATA_PROGETTO VARCHAR(1)
	private java.lang.String durata_progetto;

	// LIVELLO DECIMAL(8,0) NULL
	private java.lang.Integer livello;
	
	// PG_PROGETTO_PADRE DECIMAL(10,0) NULL
	protected java.lang.Integer pg_progetto_padre;

	// CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;

	// ESERCIZIO NUMBER(4) NOT NULL (FK)
	private java.lang.Integer esercizio_progetto_padre;

	// TIPO_FASE_PROGETTO_PADRE VARCHAR(1) NOT NULL (FK)
	private java.lang.String tipo_fase_progetto_padre;
	
	//FL_UTILIZZABILE CHAR(1) NOT NULL
	private Boolean fl_utilizzabile;

	//condiviso CHAR(1) NOT NULL
	private Boolean condiviso;

	//FL_PIANO_TRIENNALE CHAR(1) NOT NULL
	private Boolean fl_piano_triennale;
	
	private String cd_programma;

	private String cd_missione;
	// PG_PROGETTO_PADRE DECIMAL(10,0) NULL

	protected java.lang.Integer pg_progetto_other_field;

	public ProgettoBase() {
	super();
}
public ProgettoBase(java.lang.Integer esercizio,java.lang.Integer pg_progetto,java.lang.String tipo_fase) {
	super(esercizio,pg_progetto,tipo_fase);
}
/* 
 * Getter dell'attributo cd_divisa
 */
public java.lang.String getCd_divisa() {
	return cd_divisa;
}
/* 
 * Getter dell'attributo cd_ente_fin
 */
public java.lang.Integer getCd_ente_fin() {
	return cd_ente_fin;
}
/* 
 * Getter dell'attributo cd_responsabile_terzo
 */
public java.lang.Integer getCd_responsabile_terzo() {
	return cd_responsabile_terzo;
}
/* 
 * Getter dell'attributo cd_tipo_progetto
 */
public java.lang.String getCd_tipo_progetto() {
	return cd_tipo_progetto;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo ds_progetto
 */
public java.lang.String getDs_progetto() {
	return ds_progetto;
}
/* 
 * Getter dell'attributo dt_fine
 */
public java.sql.Timestamp getDt_fine() {
	return dt_fine;
}
/* 
 * Getter dell'attributo dt_inizio
 */
public java.sql.Timestamp getDt_inizio() {
	return dt_inizio;
}
/* 
 * Getter dell'attributo dt_proroga
 */
public java.sql.Timestamp getDt_proroga() {
	return dt_proroga;
}
/* 
 * Getter dell'attributo importo_divisa
 */
public java.math.BigDecimal getImporto_divisa() {
	return importo_divisa;
}
/* 
 * Getter dell'attributo importo_fondo
 */
public java.math.BigDecimal getImporto_progetto() {
	return importo_progetto;
}
/* 
 * Getter dell'attributo note
 */
public java.lang.String getNote() {
	return note;
}
/* 
 * Setter dell'attributo cd_divisa
 */
public void setCd_divisa(java.lang.String cd_divisa) {
	this.cd_divisa = cd_divisa;
}
/* 
 * Setter dell'attributo cd_ente_fin
 */
public void setCd_ente_fin(java.lang.Integer cd_ente_fin) {
	this.cd_ente_fin = cd_ente_fin;
}
/* 
 * Setter dell'attributo cd_responsabile_terzo
 */
public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo) {
	this.cd_responsabile_terzo = cd_responsabile_terzo;
}
/* 
 * Setter dell'attributo cd_tipo_progetto
 */
public void setCd_tipo_progetto(java.lang.String cd_tipo_progetto) {
	this.cd_tipo_progetto = cd_tipo_progetto;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo ds_progetto
 */
public void setDs_progetto(java.lang.String ds_progetto) {
	this.ds_progetto = ds_progetto;
}
/* 
 * Setter dell'attributo dt_fine
 */
public void setDt_fine(java.sql.Timestamp dt_fine) {
	this.dt_fine = dt_fine;
}
/* 
 * Setter dell'attributo dt_inizio
 */
public void setDt_inizio(java.sql.Timestamp dt_inizio) {
	this.dt_inizio = dt_inizio;
}
/* 
 * Setter dell'attributo dt_proroga
 */
public void setDt_proroga(java.sql.Timestamp dt_proroga) {
	this.dt_proroga = dt_proroga;
}
/* 
 * Setter dell'attributo importo_divisa
 */
public void setImporto_divisa(java.math.BigDecimal importo_divisa) {
	this.importo_divisa = importo_divisa;
}
/* 
 * Setter dell'attributo importo_fondo
 */
public void setImporto_progetto(java.math.BigDecimal importo_progetto) {
	this.importo_progetto = importo_progetto;
}
/* 
 * Setter dell'attributo note
 */
public void setNote(java.lang.String note) {
	this.note = note;
}
	/**
	 * Returns the cd_progetto.
	 * @return java.lang.String
	 */
	public java.lang.String getCd_progetto() {
		return cd_progetto;
	}

	/**
	 * Sets the cd_progetto.
	 * @param cd_progetto The cd_progetto to set
	 */
	public void setCd_progetto(java.lang.String cd_progetto) {
		this.cd_progetto = cd_progetto;
	}

	/**
	 * Returns the stato.
	 * @return java.lang.String
	 */
	public java.lang.String getStato() {
		return stato;
	}

	/**
	 * Sets the stato.
	 * @param stato The stato to set
	 */
	public void setStato(java.lang.String stato) {
		this.stato = stato;
	}
	
	/**
	 * Returns the durata_progetto.
	 * @return java.lang.String
	 */
	public java.lang.String getDurata_progetto() {
		return durata_progetto;
	}

	/**
	 * Sets the durata_progetto.
	 * @param durata_progetto The durata_progetto to set
	 */
	public void setDurata_progetto(java.lang.String durata_progetto) {
		this.durata_progetto = durata_progetto;
	}	

	/**
	 * Returns the livello.
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getLivello() {
		return livello;
	}

	/**
	 * Sets the livello.
	 * @param livello The livello to set
	 */
	public void setLivello(java.lang.Integer livello) {
		this.livello = livello;
	}
	
	/**
	 * Returns the pg_progetto_padre.
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getPg_progetto_padre() {
		return pg_progetto_padre;
	}

	/**
	 * Sets the pg_progetto_padre.
	 * @param pg_progetto_padre The pg_progetto_padre to set
	 */
	public void setPg_progetto_padre(java.lang.Integer pg_progetto_padre) {
		this.pg_progetto_padre = pg_progetto_padre;
	}	

	/**
	 * @return
	 */
	public java.lang.String getCd_dipartimento() {
		return cd_dipartimento;
	}

	/**
	 * @param string
	 */
	public void setCd_dipartimento(java.lang.String string) {
		cd_dipartimento = string;
	}
	public java.lang.Integer getEsercizio_progetto_padre() {
		return esercizio_progetto_padre;
	}
	public void setEsercizio_progetto_padre(
			java.lang.Integer esercizio_progetto_padre) {
		this.esercizio_progetto_padre = esercizio_progetto_padre;
	}
	public java.lang.String getTipo_fase_progetto_padre() {
		return tipo_fase_progetto_padre;
	}
	public void setTipo_fase_progetto_padre(
			java.lang.String tipo_fase_progetto_padre) {
		this.tipo_fase_progetto_padre = tipo_fase_progetto_padre;
	}
	public Boolean getFl_utilizzabile() {
		return fl_utilizzabile;
	}
	public void setFl_utilizzabile(Boolean fl_utilizzabile) {
		this.fl_utilizzabile = fl_utilizzabile;
	}
	public Boolean getCondiviso() {
		return condiviso;
	}
	public void setCondiviso(Boolean condiviso) {
		this.condiviso = condiviso;
	}
	public Boolean getFl_piano_triennale() {
		return fl_piano_triennale;
	}
	public void setFl_piano_triennale(Boolean fl_piano_triennale) {
		this.fl_piano_triennale = fl_piano_triennale;
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

	public Integer getPg_progetto_other_field() {
		return pg_progetto_other_field;
	}

	public ProgettoBase setPg_progetto_other_field(Integer pg_progetto_other_field) {
		this.pg_progetto_other_field = pg_progetto_other_field;
		return this;
	}
}