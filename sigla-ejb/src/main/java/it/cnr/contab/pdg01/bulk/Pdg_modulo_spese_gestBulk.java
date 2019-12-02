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
* Date 23/11/2005
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_modulo_spese_gestBulk extends Pdg_modulo_spese_gestBase implements Pdg_modulo_gestBulk {
	private Pdg_modulo_speseBulk pdg_modulo_spese;
	private CdrBulk cdr_assegnatario;
	private WorkpackageBulk linea_attivita;
	private Elemento_voceBulk elemento_voce;
	public Pdg_modulo_spese_gestBulk() {
		super();
	}
	public Pdg_modulo_spese_gestBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer id_classificazione, java.lang.String cd_cds_area, java.lang.String cd_cdr_assegnatario, java.lang.String cd_linea_attivita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce,Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, id_classificazione, cd_cds_area, cd_cdr_assegnatario, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_elemento_voce,pg_dettaglio);
		setPdg_modulo_spese(new Pdg_modulo_speseBulk(esercizio, cd_centro_responsabilita, pg_progetto, id_classificazione, cd_cds_area,pg_dettaglio));
		setLinea_attivita(new WorkpackageBulk(cd_cdr_assegnatario,cd_linea_attivita));
		setElemento_voce(new Elemento_voceBulk(cd_elemento_voce,esercizio,ti_appartenenza, ti_gestione));
	}
	public void completaImportiNulli() {
		if(getIm_spese_gest_decentrata_int() == null)
			setIm_spese_gest_decentrata_int(Utility.ZERO);

		if(getIm_spese_gest_decentrata_est() == null)
			setIm_spese_gest_decentrata_est(Utility.ZERO);

		if(getIm_spese_gest_accentrata_int() == null)
			setIm_spese_gest_accentrata_int(Utility.ZERO);

		if(getIm_spese_gest_accentrata_est() == null)
			setIm_spese_gest_accentrata_est(Utility.ZERO);

		if(getIm_pagamenti() == null)
			setIm_pagamenti(Utility.ZERO);
	}
	public Pdg_modulo_speseBulk getPdg_modulo_spese() {
		return pdg_modulo_spese;
	}
	public void setPdg_modulo_spese(Pdg_modulo_speseBulk bulk) {
		pdg_modulo_spese = bulk;
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
	public void setEsercizio(java.lang.Integer esercizio)  {
		getPdg_modulo_spese().setEsercizio(esercizio);
	}
	public Integer getEsercizio() {
		if (getPdg_modulo_spese() == null) return null;
		return getPdg_modulo_spese().getEsercizio();
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		getPdg_modulo_spese().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	public java.lang.String getCd_centro_responsabilita () {
		if (getPdg_modulo_spese() == null) return null;
		return getPdg_modulo_spese().getCd_centro_responsabilita();
	}
	public void setPg_progetto(java.lang.Integer pg_progetto)  {
		getPdg_modulo_spese().setPg_progetto(pg_progetto);
	}
	public java.lang.Integer getPg_progetto () {
		if (getPdg_modulo_spese() == null) return null;
		return getPdg_modulo_spese().getPg_progetto();
	}
	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		getPdg_modulo_spese().setId_classificazione(id_classificazione);
	}
	public java.lang.Integer getId_classificazione () {
		if (getPdg_modulo_spese() == null) return null;
		return getPdg_modulo_spese().getId_classificazione();
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		getPdg_modulo_spese().setCd_cds_area(cd_cds_area);
	}
	public java.lang.String getCd_cds_area () {
		if (getPdg_modulo_spese() == null) return null;
		return getPdg_modulo_spese().getCd_cds_area();
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
	public boolean isROElemento_voce() {
		return getLinea_attivita() == null || 
			   getLinea_attivita().isNew() ||
			   getLinea_attivita().getCrudStatus() == UNDEFINED;
	}
	public boolean isROLinea_attivita() {
		return getCdr_assegnatario() == null || 
			   getCdr_assegnatario().isNew() ||
			   getCdr_assegnatario().getCrudStatus() == UNDEFINED;
	}
	public boolean isROCdr_assegnatario() {
		return false;
	}
	public boolean isRODecentrataInterna(){
		if (getLinea_attivita() != null &&
			getLinea_attivita().getNatura() != null &&
		    getLinea_attivita().getNatura().getTipo() != null &&
		    getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_INTERNE))
			return (getPdg_modulo_spese() != null &&
			        getPdg_modulo_spese().isGestioneDecentrataDisable());
		return true;
	}
	public boolean isRODecentrataEsterna(){
		if (getLinea_attivita() != null &&
			getLinea_attivita().getNatura() != null &&
			getLinea_attivita().getNatura().getTipo() != null &&
			getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE))
			return (getPdg_modulo_spese() != null &&
					getPdg_modulo_spese().isGestioneDecentrataDisable());
		return true;
	}
	public boolean isROAccentrataInterna(){
		if (getLinea_attivita() != null &&
			getLinea_attivita().getNatura() != null &&
			getLinea_attivita().getNatura().getTipo() != null &&
			getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_INTERNE))
			return (getPdg_modulo_spese() != null &&
				    getPdg_modulo_spese().isGestioneAccentrataCdsAreaDisable());
		return true;
	}
	public boolean isROAccentrataEsterna(){
		if (getLinea_attivita() != null &&
			getLinea_attivita().getNatura() != null &&
			getLinea_attivita().getNatura().getTipo() != null &&
			getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE))
			return (getPdg_modulo_spese() != null &&
				    getPdg_modulo_spese().isGestioneAccentrataRipartoDisable());
		return true;
	}
	public void setPg_dettaglio(java.lang.Integer pg_dettaglio)  {
		getPdg_modulo_spese().setPg_dettaglio(pg_dettaglio);
	}
	public java.lang.Integer getPg_dettaglio() {
		if (getPdg_modulo_spese() == null) return null;
		return getPdg_modulo_spese().getPg_dettaglio();
	}
	
}