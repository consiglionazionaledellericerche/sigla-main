package it.cnr.contab.client.docamm;

public class Tariffario {
    private String codice;
    private String descrizione;
    private java.math.BigDecimal importo;
    private String iva;
    private String misura;
	public Tariffario() {
		// TODO Auto-generated constructor stub
	}
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
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(java.math.BigDecimal importo) {
		this.importo = importo;
	}
	public String getIva() {
		return iva;
	}
	public void setIva(String iva) {
		this.iva = iva;
	}
	public String getMisura() {
		return misura;
	}
	public void setMisura(String misura) {
		this.misura = misura;
	}
		
}
