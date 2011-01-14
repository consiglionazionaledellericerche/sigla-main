package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_terzo_anagrafico_sipBulk extends OggettoBulk implements Persistent {
	// CD_TERZO     NUMBER(8)
	private java.lang.Integer cd_terzo;

	// CD_ANAG     NUMBER(8)
	private java.lang.Integer cd_anag;

	// DENOMINAZIONE_SEDE VARCHAR(200)
	private java.lang.String denominazione_sede;

	// TI_ENTITA CHAR(1)
	private java.lang.String ti_entita;
	
	// CODICE_FISCALE_PARIVA VARCHAR(20)
	private java.lang.String codice_fiscale_pariva;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	public V_terzo_anagrafico_sipBulk() {
		super();
	}

	public java.lang.Integer getCd_anag() {
		return cd_anag;
	}

	public void setCd_anag(java.lang.Integer cd_anag) {
		this.cd_anag = cd_anag;
	}

	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}

	public void setCd_terzo(java.lang.Integer cd_terzo) {
		this.cd_terzo = cd_terzo;
	}

	public java.lang.String getCodice_fiscale_pariva() {
		return codice_fiscale_pariva;
	}

	public void setCodice_fiscale_pariva(java.lang.String codice_fiscale_pariva) {
		this.codice_fiscale_pariva = codice_fiscale_pariva;
	}

	public java.lang.String getCognome() {
		return cognome;
	}

	public void setCognome(java.lang.String cognome) {
		this.cognome = cognome;
	}

	public java.lang.String getDenominazione_sede() {
		return denominazione_sede;
	}

	public void setDenominazione_sede(java.lang.String denominazione_sede) {
		this.denominazione_sede = denominazione_sede;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	public java.lang.String getTi_entita() {
		return ti_entita;
	}

	public void setTi_entita(java.lang.String ti_entita) {
		this.ti_entita = ti_entita;
	}

}
