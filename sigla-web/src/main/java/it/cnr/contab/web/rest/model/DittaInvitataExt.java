/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.web.rest.model;

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
