package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Progetto_other_fieldBase extends Progetto_other_fieldKey implements Keyed {
	private String cd_programma;

	private String cd_missione;

	private Long idTipoFinanziamento;

	private String stato;

	private java.sql.Timestamp dtInizio;

	private java.sql.Timestamp dtFine;

	private java.sql.Timestamp dtProroga;

	private java.math.BigDecimal imFinanziato;
	
	private java.math.BigDecimal imCofinanziato;
	
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

	public Long getIdTipoFinanziamento() {
		return idTipoFinanziamento;
	}

	public void setIdTipoFinanziamento(Long idTipoFinanziamento) {
		this.idTipoFinanziamento = idTipoFinanziamento;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public java.sql.Timestamp getDtInizio() {
		return dtInizio;
	}

	public void setDtInizio(java.sql.Timestamp dtInizio) {
		this.dtInizio = dtInizio;
	}

	public java.sql.Timestamp getDtFine() {
		return dtFine;
	}

	public void setDtFine(java.sql.Timestamp dtFine) {
		this.dtFine = dtFine;
	}

	public java.sql.Timestamp getDtProroga() {
		return dtProroga;
	}

	public void setDtProroga(java.sql.Timestamp dtProroga) {
		this.dtProroga = dtProroga;
	}

	public java.math.BigDecimal getImFinanziato() {
		return imFinanziato;
	}

	public void setImFinanziato(java.math.BigDecimal imFinanziato) {
		this.imFinanziato = imFinanziato;
	}

	public java.math.BigDecimal getImCofinanziato() {
		return imCofinanziato;
	}

	public void setImCofinanziato(java.math.BigDecimal imCofinanziato) {
		this.imCofinanziato = imCofinanziato;
	}
}
