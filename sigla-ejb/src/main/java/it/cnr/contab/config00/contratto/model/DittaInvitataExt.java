package it.cnr.contab.config00.contratto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import it.cnr.jada.bulk.OggettoBulk;

public class DittaInvitataExt extends OggettoBulk {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String dittaExtraUE;
	private String pIvaCodiceFiscaleDittaInvitata;
	private String ragioneSocialeDittaInvitata;
	private String offertaPresentataDittaInvitata;
	private BigDecimal valutazioneDittaInvitata;
	private String gestioneRTIDittaInvitata;
	private List<DittaInvitataExt> ditteRTI;
	
	public List<DittaInvitataExt> getDitteRTI() {
		return ditteRTI;
	}
	public void setDitteRTI(List<DittaInvitataExt> ditteRTI) {
		this.ditteRTI = ditteRTI;
	}
	public String getDittaExtraUE() {
		return dittaExtraUE;
	}
	public void setDittaExtraUE(String dittaExtraUE) {
		this.dittaExtraUE = dittaExtraUE;
	}
	public String getpIvaCodiceFiscaleDittaInvitata() {
		return pIvaCodiceFiscaleDittaInvitata;
	}
	public void setpIvaCodiceFiscaleDittaInvitata(String pIvaCodiceFiscaleDittaInvitata) {
		this.pIvaCodiceFiscaleDittaInvitata = pIvaCodiceFiscaleDittaInvitata;
	}
	public String getRagioneSocialeDittaInvitata() {
		return ragioneSocialeDittaInvitata;
	}
	public void setRagioneSocialeDittaInvitata(String ragioneSocialeDittaInvitata) {
		this.ragioneSocialeDittaInvitata = ragioneSocialeDittaInvitata;
	}
	public String getOffertaPresentataDittaInvitata() {
		return offertaPresentataDittaInvitata;
	}
	public void setOffertaPresentataDittaInvitata(String offertaPresentataDittaInvitata) {
		this.offertaPresentataDittaInvitata = offertaPresentataDittaInvitata;
	}
	public BigDecimal getValutazioneDittaInvitata() {
		return valutazioneDittaInvitata;
	}
	public void setValutazioneDittaInvitata(BigDecimal valutazioneDittaInvitata) {
		this.valutazioneDittaInvitata = valutazioneDittaInvitata;
	}
	public String getGestioneRTIDittaInvitata() {
		return gestioneRTIDittaInvitata;
	}
	public void setGestioneRTIDittaInvitata(String gestioneRTIDittaInvitata) {
		this.gestioneRTIDittaInvitata = gestioneRTIDittaInvitata;
	}

}
