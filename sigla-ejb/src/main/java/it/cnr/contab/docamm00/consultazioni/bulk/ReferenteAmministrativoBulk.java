package it.cnr.contab.docamm00.consultazioni.bulk;

import it.cnr.jada.bulk.OggettoBulk;

public class ReferenteAmministrativoBulk extends OggettoBulk {
	private String cds;
	private String uo;
	private String cd_terzo;
	private String cognome;
	private String nome;
	private String email;
	private String telefono;
	private String segr_refe;
	private String descrizione;
	
	public String getCds() {
		return cds;
	}
	public void setCds(String cds) {
		this.cds = cds;
	}
	public String getUo() {
		return uo;
	}
	public void setUo(String uo) {
		this.uo = uo;
	}
	public String getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(String cd_terzo) {
		this.cd_terzo = cd_terzo;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getSegr_refe() {
		return segr_refe;
	}
	public void setSegr_refe(String segr_refe) {
		this.segr_refe = segr_refe;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
