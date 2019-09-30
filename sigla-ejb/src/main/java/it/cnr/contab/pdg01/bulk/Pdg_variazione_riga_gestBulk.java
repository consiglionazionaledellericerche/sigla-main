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
* Date 21/04/2006
*/
package it.cnr.contab.pdg01.bulk;
import java.math.BigDecimal;
import java.util.Optional;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

public class Pdg_variazione_riga_gestBulk extends Pdg_variazione_riga_gestBase {
	private Pdg_variazioneBulk pdg_variazione;
	private CdrBulk cdr_assegnatario;
	private WorkpackageBulk linea_attivita = new WorkpackageBulk();
	private Elemento_voceBulk elemento_voce;
	private CdsBulk area;
	private Boolean fl_riga_vistabile = Boolean.FALSE;
	private ProgettoBulk progetto = new ProgettoBulk();
	private Pdg_missioneBulk missione = new Pdg_missioneBulk();
	private Pdg_programmaBulk programma =new Pdg_programmaBulk();
	public Pdg_variazione_riga_gestBulk() {
		super();
	}
	// metodo per inizializzare l'oggetto bulk
	private void initialize () {
		setFl_visto_dip_variazioni(new Boolean(false));
	}
	public Pdg_variazione_riga_gestBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Integer pg_riga) {
		super(esercizio, pg_variazione_pdg, pg_riga);
		setPdg_variazione(new Pdg_variazioneBulk(esercizio, pg_variazione_pdg));
	}
	public Pdg_variazione_riga_gestBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Integer pg_riga, java.lang.String cd_cdr_assegnatario, java.lang.String cd_linea_attivita, java.lang.String cd_cds_area, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce) {
		super(esercizio, pg_variazione_pdg, pg_riga);
		setLinea_attivita(new WorkpackageBulk(cd_cdr_assegnatario,cd_linea_attivita));
		setElemento_voce(new Elemento_voceBulk(cd_elemento_voce,esercizio,ti_appartenenza, ti_gestione));
	}
	public Pdg_variazioneBulk getPdg_variazione() {
		return pdg_variazione;
	}
	public void setPdg_variazione(Pdg_variazioneBulk bulk) {
		pdg_variazione = bulk;
	}
	public CdrBulk getCdr_assegnatario() {
		return cdr_assegnatario;
	}
	public void setCdr_assegnatario(CdrBulk bulk) {
		cdr_assegnatario = bulk;
	}
	public WorkpackageBulk getLinea_attivita() {
		return linea_attivita;
	}
	public void setLinea_attivita(WorkpackageBulk bulk) {
		linea_attivita = bulk;
	}
	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
	public void setElemento_voce(Elemento_voceBulk bulk) {
		elemento_voce = bulk;
	}
	public CdsBulk getArea() {
		return area;
	}
	public void setArea(CdsBulk area) {
		this.area = area;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		getPdg_variazione().setEsercizio(esercizio);
	}
	public Integer getEsercizio() {
		if (getPdg_variazione() == null) return null;
		return getPdg_variazione().getEsercizio();
	}
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		getPdg_variazione().setPg_variazione_pdg(pg_variazione_pdg);
	}
	public java.lang.Long getPg_variazione_pdg () {
		if (getPdg_variazione() == null) return null;
		return getPdg_variazione().getPg_variazione_pdg();
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		getPdg_variazione().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	public java.lang.String getCd_centro_responsabilita () {
		if (getPdg_variazione() == null) return null;
		return getPdg_variazione().getCd_centro_responsabilita();
	}
	public void setCd_cdr_assegnatario(String cd_cdr_assegnatario) {
		getCdr_assegnatario().setCd_centro_responsabilita(cd_cdr_assegnatario);
	}
	public String getCd_cdr_assegnatario() {
		if (getCdr_assegnatario() == null) return null;
		return getCdr_assegnatario().getCd_centro_responsabilita();
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
	}
	public java.lang.String getCd_linea_attivita () {
		if (getLinea_attivita() == null) return null;
		return getLinea_attivita().getCd_linea_attivita();
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza)  {
		getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
	public java.lang.String getTi_appartenenza () {
		if (getElemento_voce() == null) return null;
		return getElemento_voce().getTi_appartenenza();
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		getElemento_voce().setTi_gestione(ti_gestione);
	}
	public java.lang.String getTi_gestione () {
		if (getElemento_voce() == null) return null;
		return getElemento_voce().getTi_gestione();
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
	}
	public java.lang.String getCd_elemento_voce () {
		if (getElemento_voce() == null) return null;
		return getElemento_voce().getCd_elemento_voce();
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
	   getArea().setCd_unita_organizzativa(cd_cds_area);
	}
	public java.lang.String getCd_cds_area () {
		if(getArea() == null) return null;
		return getArea().getCd_unita_organizzativa();
	}
	public boolean isROElemento_voce() {
		return getLinea_attivita() == null || 
			   getLinea_attivita().isNew() ||
			   getLinea_attivita().getCrudStatus() == UNDEFINED;
	}
	public boolean isROLinea_attivita() {
		return false;
	}
	public boolean isRODecentrataInterna(){
		if (getLinea_attivita() != null &&
			getLinea_attivita().getNatura() != null &&
		    getLinea_attivita().getNatura().getTipo() != null &&
		    getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_INTERNE))
			return isGestioneDecentrataDisable();
		return true;
	}
	public boolean isRODecentrataEsterna(){
		if (getLinea_attivita() != null &&
			getLinea_attivita().getNatura() != null &&
			getLinea_attivita().getNatura().getTipo() != null &&
			getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE))
			return isGestioneDecentrataDisable();
		return true;
	}
	public boolean isROAccentrataInterna(){
		if (getLinea_attivita() != null &&
			getLinea_attivita().getNatura() != null &&
			getLinea_attivita().getNatura().getTipo() != null &&
			getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_INTERNE))
			return isGestioneAccentrataCdsAreaDisable();
		return true;
	}
	public boolean isNonAccentrata()
	{ 
	if (!isGestioneAccentrataDisable())
		return false;
		return true;
	}
	public boolean isROAccentrataEsterna(){
		if (getLinea_attivita() != null &&
			getLinea_attivita().getNatura() != null &&
			getLinea_attivita().getNatura().getTipo() != null &&
			getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE))
			return isGestioneAccentrataRipartoDisable();
		return true;
	}

	public java.math.BigDecimal getIm_variazione () {
		if (getTi_gestione()!=null && getTi_gestione().equals(Elemento_voceHome.GESTIONE_ENTRATE))
			return getIm_entrata();
		else if (getTi_gestione()!=null && getTi_gestione().equals(Elemento_voceHome.GESTIONE_SPESE))
			return Utility.nvl(getIm_spese_gest_accentrata_int()).add(
				Utility.nvl(getIm_spese_gest_accentrata_est()).add(
				Utility.nvl(getIm_spese_gest_decentrata_int()).add(
				Utility.nvl(getIm_spese_gest_decentrata_est()))));
		return(Utility.ZERO);
	}

	public void setIm_variazione (java.math.BigDecimal importo_da_assegnare) {
		if (getTi_gestione()!=null && getTi_gestione().equals(Elemento_voceHome.GESTIONE_ENTRATE))
			setIm_entrata(importo_da_assegnare);
		else if (getTi_gestione()!=null && getTi_gestione().equals(Elemento_voceHome.GESTIONE_SPESE))
			if (!isRODecentrataInterna())
				setIm_spese_gest_decentrata_int(importo_da_assegnare);
			else if (!isRODecentrataEsterna())
				setIm_spese_gest_decentrata_est(importo_da_assegnare);
			else if (!isROAccentrataInterna())
				setIm_spese_gest_accentrata_int(importo_da_assegnare);
			else
				setIm_spese_gest_accentrata_est(importo_da_assegnare);
	}

	public boolean isROImportoEntrata() {
		return false;
	}

	public boolean isROprogetto(){
		return false;
	}

	public boolean isROCd_cds_area() {
		return (getTi_gestione()!=null &&
				getPdg_variazione()!=null &&
				getPdg_variazione().isStorno() &&
				getTi_gestione().equals(Elemento_voceHome.GESTIONE_ENTRATE));		
	}

	public void validate(OggettoBulk oggettobulk) throws ValidationException {
		// TODO Auto-generated method stub
		super.validate(oggettobulk);
		if (getPdg_variazione()!=null &&
				(getPdg_variazione().getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_POSITIVA_ISTITUTI_DIVERSI)||
				 getPdg_variazione().getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_POSITIVA_STESSO_ISTITUTO))) 
			{
				if (Optional.ofNullable(getIm_entrata()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==-1 ||
					Optional.ofNullable(getIm_spese_gest_accentrata_est()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==-1 ||
					Optional.ofNullable(getIm_spese_gest_decentrata_est()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==-1 ||
					Optional.ofNullable(getIm_spese_gest_accentrata_int()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==-1 ||
					Optional.ofNullable(getIm_spese_gest_decentrata_int()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==-1) 
					throw new ValidationException("Non è possibile inserire valori negativi in una variazione a quadratura per maggiori entrate/spese.");
			}
			if (getPdg_variazione()!=null &&
				(getPdg_variazione().getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_NEGATIVA_ISTITUTI_DIVERSI)||
				 getPdg_variazione().getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_NEGATIVA_STESSO_ISTITUTO))) 
			{
				if (Optional.ofNullable(getIm_entrata()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==1 ||
					Optional.ofNullable(getIm_spese_gest_accentrata_est()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==1 ||
					Optional.ofNullable(getIm_spese_gest_decentrata_est()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==1 ||
					Optional.ofNullable(getIm_spese_gest_accentrata_int()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==1 ||
					Optional.ofNullable(getIm_spese_gest_decentrata_int()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==1) 
					throw new ValidationException("Non è possibile inserire valori positivi in una variazione a quadratura per minori entrate/spese.");
			}
	}

	private boolean isGestioneDecentrataDisable(){
		return (getElemento_voce() == null || getElemento_voce().getCd_elemento_voce() == null ||
				(getElemento_voce().getV_classificazione_voci() != null && 
				getElemento_voce().getV_classificazione_voci().getId_classificazione() != null &&
			    !getElemento_voce().getV_classificazione_voci().getFl_decentrato().booleanValue() &&
			    (getElemento_voce().getV_classificazione_voci().getCentro_responsabilita() == null ||
			    (getElemento_voce().getV_classificazione_voci().getCentro_responsabilita() != null &&
				getElemento_voce().getV_classificazione_voci().getCentro_responsabilita().getCd_centro_responsabilita() != null &&
			    !getElemento_voce().getV_classificazione_voci().getCentro_responsabilita().equalsByPrimaryKey(getCdr_assegnatario())))));
	}
	private boolean isGestioneAccentrataDisable(){
		return (getElemento_voce() == null || getElemento_voce().getCd_elemento_voce() == null ||
				(getElemento_voce().getV_classificazione_voci() != null &&
				 getElemento_voce().getV_classificazione_voci().getId_classificazione() != null &&
			     ((getElemento_voce().getV_classificazione_voci().getFl_accentrato().booleanValue() &&
				   getElemento_voce().getV_classificazione_voci().getCentro_responsabilita() != null &&
				   getElemento_voce().getV_classificazione_voci().getCentro_responsabilita().getCd_centro_responsabilita() != null &&
				   getElemento_voce().getV_classificazione_voci().getCentro_responsabilita().equalsByPrimaryKey(getCdr_assegnatario())
			       ||(!getElemento_voce().getV_classificazione_voci().getFl_accentrato().booleanValue())))));
	}
	private boolean isGestioneAccentrataCdsAreaDisable(){
		return (isGestioneAccentrataDisable() ||
		        (getArea()!= null &&
			     getArea().getCd_proprio_unita()!= null &&
			     !getArea().equalsByPrimaryKey(getCdr_assegnatario().getUnita_padre().getUnita_padre())));
	}
	private boolean isGestioneAccentrataRipartoDisable(){
		return (isGestioneAccentrataDisable() ||
				(getElemento_voce() == null || getElemento_voce().getCd_elemento_voce() == null ||
				 (getElemento_voce().getV_classificazione_voci() != null &&
				  getElemento_voce().getV_classificazione_voci().getId_classificazione() != null &&
				  getElemento_voce().getV_classificazione_voci().getFl_piano_riparto().booleanValue())));
	}
	public boolean isDettaglioScaricato(){
		return (getCategoria_dettaglio()!=null &&
				getCategoria_dettaglio().equals(Pdg_modulo_gestBulk.CAT_SCARICO));
	}

	/**
	 * Flag utilizzato per sapere se la riga in oggetto è associata a moduli appartenenti al 
	 * dipartimento collegato per i quali deve essere apposto il visto.
     *
	 * @return Boolean
	 */
	public Boolean getFl_riga_vistabile() {
		return fl_riga_vistabile;
	}
	/**
	 * Flag da settare a:
	 * TRUE:  se la riga in oggetto è associata a moduli appartenenti al dipartimento collegato 
	 *        per i quali deve essere apposto il visto.
	 * FALSE: in tutti gli altri casi.
     *
	 * @return Boolean
	 */
	public void setFl_riga_vistabile(Boolean fl_riga_vistabile) {
		this.fl_riga_vistabile = fl_riga_vistabile;
	}

	public String getIconaVistoDipartimento() {
		if (!getFl_riga_vistabile().booleanValue())
			return Ass_pdg_variazione_cdrBulk.ICONA_DA_NON_VISTARE;
		else if (getFl_visto_dip_variazioni().booleanValue()) 
			return Ass_pdg_variazione_cdrBulk.ICONA_VISTATO;
		else
			return Ass_pdg_variazione_cdrBulk.ICONA_DA_VISTARE;
	}
	
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}
	public Pdg_missioneBulk getMissione() {
		return missione;
	}
	public void setMissione(Pdg_missioneBulk missione) {
		this.missione = missione;
	}
	public Pdg_programmaBulk getProgramma() {
		return programma;
	}
	public void setProgramma(Pdg_programmaBulk programma) {
		this.programma = programma;
	}
}