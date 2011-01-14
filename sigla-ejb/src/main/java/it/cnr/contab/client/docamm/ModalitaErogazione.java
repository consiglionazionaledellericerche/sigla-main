package it.cnr.contab.client.docamm;

public class ModalitaErogazione {

	public ModalitaErogazione() {
	}
	private String codice;
	private String descrizione;
	private Integer esercizio;
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}
}
