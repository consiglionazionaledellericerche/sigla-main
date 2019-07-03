package it.cnr.contab.config00.contratto.model;

import java.io.Serializable;
import java.math.BigDecimal;

import it.cnr.jada.bulk.OggettoBulk;

public class UoAbilitataExt extends OggettoBulk {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private BigDecimal importoLordo;
	private String descrizione;
	private String vocedispesa;
	private BigDecimal importoNetto;
	private String uo_label;
	private BigDecimal percentualeIva;
	private String uo;
	private String gae;
	private String progetto;
	private String vocedispesaid;
	
	public BigDecimal getImportoLordo() {
		return importoLordo;
	}
	public void setImportoLordo(BigDecimal importoLordo) {
		this.importoLordo = importoLordo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getVocedispesa() {
		return vocedispesa;
	}
	public void setVocedispesa(String vocedispesa) {
		this.vocedispesa = vocedispesa;
	}
	public BigDecimal getImportoNetto() {
		return importoNetto;
	}
	public void setImportoNetto(BigDecimal importoNetto) {
		this.importoNetto = importoNetto;
	}
	public String getUo_label() {
		return uo_label;
	}
	public void setUo_label(String uo_label) {
		this.uo_label = uo_label;
	}
	public BigDecimal getPercentualeIva() {
		return percentualeIva;
	}
	public void setPercentualeIva(BigDecimal percentualeIva) {
		this.percentualeIva = percentualeIva;
	}
	public String getUo() {
		return uo;
	}
	public void setUo(String uo) {
		this.uo = uo;
	}
	public String getGae() {
		return gae;
	}
	public void setGae(String gae) {
		this.gae = gae;
	}
	public String getProgetto() {
		return progetto;
	}
	public void setProgetto(String progetto) {
		this.progetto = progetto;
	}
	public String getVocedispesaid() {
		return vocedispesaid;
	}
	public void setVocedispesaid(String vocedispesaid) {
		this.vocedispesaid = vocedispesaid;
	}
}
