package it.cnr.contab.doccont00.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class DatiFinanziariScadenzeDTO implements Serializable, Cloneable{
	BigDecimal nuovoImportoScadenzaVecchia;
	String nuovaDescrizione;
	Timestamp nuovaScadenza;
	String cdLineaAttivita;
	String cdVoce;
	String cdCentroResponsabilita;
	
	public BigDecimal getNuovoImportoScadenzaVecchia() {
		return nuovoImportoScadenzaVecchia;
	}
	public void setNuovoImportoScadenzaVecchia(BigDecimal nuovoImportoScadenzaVecchia) {
		this.nuovoImportoScadenzaVecchia = nuovoImportoScadenzaVecchia;
	}
	public String getNuovaDescrizione() {
		return nuovaDescrizione;
	}
	public void setNuovaDescrizione(String nuovaDescrizione) {
		this.nuovaDescrizione = nuovaDescrizione;
	}
	public Timestamp getNuovaScadenza() {
		return nuovaScadenza;
	}
	public void setNuovaScadenza(Timestamp nuovaScadenza) {
		this.nuovaScadenza = nuovaScadenza;
	}
	public String getCdLineaAttivita() {
		return cdLineaAttivita;
	}
	public void setCdLineaAttivita(String cdLineaAttivita) {
		this.cdLineaAttivita = cdLineaAttivita;
	}
	public String getCdVoce() {
		return cdVoce;
	}
	public void setCdVoce(String cdVoce) {
		this.cdVoce = cdVoce;
	}
	public String getCdCentroResponsabilita() {
		return cdCentroResponsabilita;
	}
	public void setCdCentroResponsabilita(String cdCentroResponsabilita) {
		this.cdCentroResponsabilita = cdCentroResponsabilita;
	}
}
