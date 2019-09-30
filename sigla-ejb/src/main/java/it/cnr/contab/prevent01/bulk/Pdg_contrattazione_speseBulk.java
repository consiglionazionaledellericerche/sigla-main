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

/*
* Created by Generator 1.0
* Date 29/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import java.math.BigDecimal;

import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;

public class Pdg_contrattazione_speseBulk extends Pdg_contrattazione_speseBase {
	private V_classificazione_vociBulk classificazione;
	private BigDecimal totalePropostoModificatoFI;
	private BigDecimal differenzaFI;
	private BigDecimal differenzaFE;
	private BigDecimal totalePropostoModificatoFE;
	private BigDecimal daApprovareFI;
	private BigDecimal daApprovareFE;
	private CdrBulk centro_responsabilita;
	private Pdg_approvato_dip_areaBulk pdg_dip_area;
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;
	protected it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto;
	private CdsBulk area;
	
	public Pdg_contrattazione_speseBulk() {
		super();
	}
	public Pdg_contrattazione_speseBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, pg_dettaglio);
		setCdr(new CdrBulk(cd_centro_responsabilita));
		setProgetto(new Progetto_sipBulk(esercizio,pg_progetto,ProgettoBulk.TIPO_FASE_PREVISIONE));
	}
	public CdsBulk getArea() {
		return area;
	}
	public void setArea(CdsBulk bulk) {
		area = bulk;
	}
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
	{
		inizializzaImporti();		
		return super.initializeForInsert(bp, context);
	}
	public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
		return cdr;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
		this.getCdr().setCd_centro_responsabilita(cd_centro_responsabilita);
	}

	public String getCd_centro_responsabilita() {
		it.cnr.contab.config00.sto.bulk.CdrBulk cdr = this.getCdr();
		if (cdr == null)
			return null;
		return getCdr().getCd_centro_responsabilita();
	}

	public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
		cdr = newCdr;
	}
	public it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk getProgetto() {
		return progetto;
	}

	public void setProgetto(
		it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto) {
		this.progetto = progetto;
	}
	
	public java.lang.Integer getPg_progetto() {
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto = this.getProgetto();
	if (progetto == null)
		return null;
	return progetto.getPg_progetto();
	}

	public void setPg_progetto(java.lang.Integer progetto) {
		  this.getProgetto().setPg_progetto(progetto);
	}
	
	public java.lang.String getCd_progetto() {
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto = this.getProgetto();
	if (progetto == null)
		return null;
	return progetto.getCd_progetto();
	}	

	public Progetto_sipBulk getProgettopadre() {
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto = getProgetto();
	if (progetto == null)
		return null;
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = (Progetto_sipBulk)progetto.getProgettopadre();
	if (progettopadre == null)
		return null;
	return progettopadre;
	}

	public Progetto_sipBulk getProgettononno() {
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progetto = getProgetto();
	if (progetto == null)
		return null;
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettopadre = progetto.getProgettopadre();
	if (progettopadre == null)
		return null;
	it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk progettononno = progettopadre.getProgettopadre();
	if (progettononno == null)
		return null;
	return progettononno;
	}
	
	public boolean isROrigadet() {
		if (!isToBeCreated())
			return true;
		else
			return false;
	}
	/**
	 * @return
	 */
	public V_classificazione_vociBulk getClassificazione() {
		return classificazione;
	}

	/**
	 * @param bulk
	 */
	public void setClassificazione(V_classificazione_vociBulk bulk) {
		classificazione = bulk;
	}

	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		getClassificazione().setId_classificazione(id_classificazione);
	}

	public java.lang.Integer getId_classificazione () {
		if(getClassificazione() != null)
		  return getClassificazione().getId_classificazione();
		return null;  
	}
  
	public BigDecimal getTotalePropostoModificatoFI() {
		return totalePropostoModificatoFI;
	}

	public void setTotalePropostoModificatoFI(BigDecimal decimal) {
		totalePropostoModificatoFI = decimal;
	}

	public BigDecimal getDifferenzaFI() {
		return Utility.nvl(getTotalePropostoModificatoFI()).subtract(
			   Utility.nvl(getAppr_tot_spese_decentr_int()));
	}

	public BigDecimal getTotalePropostoModificatoFE() {
		return totalePropostoModificatoFE;
	}

	public void setTotalePropostoModificatoFE(BigDecimal decimal) {
		totalePropostoModificatoFE = decimal;
	}

	public BigDecimal getDifferenzaFE() {
		return Utility.nvl(getTotalePropostoModificatoFE()).subtract(
			   Utility.nvl(getAppr_tot_spese_decentr_est()));
	}
	public Pdg_approvato_dip_areaBulk getPdg_dip_area() {
		return pdg_dip_area;
	}
	public void setPdg_dip_area(Pdg_approvato_dip_areaBulk pdg_dip_area) {
		this.pdg_dip_area = pdg_dip_area;
	}
	public void setEsercizio_dip(java.lang.Integer esercizio_dip)  {
		getPdg_dip_area().setEsercizio(esercizio_dip);
	}
	public java.lang.Integer getEsercizio_dip () {
		if(getPdg_dip_area() != null)
		  return getPdg_dip_area().getEsercizio();
		return null;  
	}
	public java.lang.String getCd_dipartimento () {
		if(getPdg_dip_area() != null)
		  return getPdg_dip_area().getCd_dipartimento();
		return null;  
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		getPdg_dip_area().setCd_dipartimento(cd_dipartimento);
	}
	public java.lang.Integer getPg_dettaglio_dip() {
		if(getPdg_dip_area() != null)
		  return getPdg_dip_area().getPg_dettaglio();
		return null;  
	}
	public void setPg_dettaglio_dip(java.lang.Integer pg_dettaglio_dip)  {
		getPdg_dip_area().setPg_dettaglio(pg_dettaglio_dip);
	}
	private void inizializzaImporti(){
		setTot_spese_decentr_est(Utility.ZERO);
		setTot_spese_decentr_int(Utility.ZERO);
	}
	public BigDecimal getDaApprovareFE() {
		return Utility.nvl(getTot_spese_decentr_est()).subtract(
			   Utility.nvl(getAppr_tot_spese_decentr_est()));
	}
	public BigDecimal getDaApprovareFI() {
		return Utility.nvl(getTot_spese_decentr_int()).subtract(
			   Utility.nvl(getAppr_tot_spese_decentr_int()));
	}
}