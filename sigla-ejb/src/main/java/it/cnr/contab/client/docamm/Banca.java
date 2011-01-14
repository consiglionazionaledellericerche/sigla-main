package it.cnr.contab.client.docamm;

public class Banca {
	public Banca() {
		super();
	}
	private Long codice;
	private String descrizione;
	private String abi;
	private String cab;
	private String numeroconto;
	private String iban;
	public Long getCodice() {
		return codice;
	}
	public void setCodice(Long codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getAbi() {
		return abi;
	}
	public void setAbi(String abi) {
		this.abi = abi;
	}
	public String getCab() {
		return cab;
	}
	public void setCab(String cab) {
		this.cab = cab;
	}
	public String getNumeroconto() {
		return numeroconto;
	}
	public void setNumeroconto(String numeroconto) {
		this.numeroconto = numeroconto;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	
}
