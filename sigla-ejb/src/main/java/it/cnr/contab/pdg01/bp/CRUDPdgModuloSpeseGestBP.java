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
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.bp;

import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_speBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.ejb.PdgModuloCostiComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdgModuloSpeseGestBP extends SimpleCRUDBP {
	private V_cons_pdgp_pdgg_speBulk vConsPdgpPdggSpe;
	private String uoScrivania;
	private String labelDesctool_classificazione;
	private boolean flNuovoPdg = false;

	private SimpleDetailCRUDController crudDettagliGestionali = new SimpleDetailCRUDController( "DettagliGestionali", Pdg_modulo_spese_gestBulk.class, "dettagli_gestionali", this){
		protected void setModel(ActionContext actioncontext,OggettoBulk oggettobulk) {
			super.setModel(actioncontext, oggettobulk);
		}
		public boolean isShrinkable() {
			Pdg_modulo_spese_gestBulk dett = (Pdg_modulo_spese_gestBulk)getModel();
			return super.isShrinkable()&& 
				   isDettaglioGestionaleEnable((Pdg_modulo_spese_gestBulk)getModel());
		}
		public void removeAll(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
			List list = getDetails();
			for(int i = list.size() - 1; i >= 0; i--)
			{
				Pdg_modulo_spese_gestBulk dett=(Pdg_modulo_spese_gestBulk)list.get(i);			
				if (isDettaglioGestionaleEnable(dett))
					removeDetail(dett, i);
			}
			getParentController().setDirty(true);
			reset(actioncontext);
		}
	};

	public PdgModuloCostiComponentSession createPdgModuloCostiComponentSession() throws BusinessProcessException {
		return (PdgModuloCostiComponentSession)createComponentSession("CNRPREVENT01_EJB_PdgModuloCostiComponentSession", PdgModuloCostiComponentSession.class);		
	}

	public CRUDPdgModuloSpeseGestBP() {
		super();
	}

	public CRUDPdgModuloSpeseGestBP(String function) {
		super(function);
	}

	public CRUDPdgModuloSpeseGestBP(String function,V_cons_pdgp_pdgg_speBulk vConsPdgpPdggSpe) {
		super(function);
		setVConsPdgpPdggSpe(vConsPdgpPdggSpe);	
	}
	
	/**
	 * Imposta come attivi i tab di default.
	 *
	 * @param context <code>ActionContext</code>
	 */
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);	
		resetTabs();
	}

	protected void resetTabs() {
		setTab("tab","tabTotaliGest");
	}
	public boolean isNewButtonHidden() {
		return true;
	}

	public boolean isFreeSearchButtonHidden() {
		return true;
	}

	public boolean isSearchButtonHidden() {
		return true;
	}

	public boolean isDeleteButtonEnabled() {
		return super.isDeleteButtonEnabled() && this.getCrudDettagliGestionali().countDetails()!=0;
	}

	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		try {
			Pdg_modulo_speseBulk pdgModuloSpese = createPdgModuloCostiComponentSession().getPdgModuloSpeseBulk(actioncontext.getUserContext(),getVConsPdgpPdggSpe()); 
			setModel(actioncontext,super.initializeModelForEdit(actioncontext,pdgModuloSpese));
			setUoScrivania(CNRUserContext.getCd_unita_organizzativa(actioncontext.getUserContext()));
			setLabelDesctool_classificazione(Utility.createClassificazioneVociComponentSession(). 
										getDsLivelloClassificazione(actioncontext.getUserContext(),
										 						    CNRUserContext.getEsercizio(actioncontext.getUserContext()),
																	Elemento_voceHome.GESTIONE_SPESE,
																	((Pdg_modulo_speseBulk)getModel()).getClassificazione().getNr_livello()));
			setStatus(EDIT);
			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())); 
			setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} 
	}

	public SimpleDetailCRUDController getCrudDettagliGestionali() {
		return crudDettagliGestionali;
	}

	public void setCrudDettagliGestionali(SimpleDetailCRUDController controller) {
		crudDettagliGestionali = controller;
	}

	public V_cons_pdgp_pdgg_speBulk getVConsPdgpPdggSpe() {
		return vConsPdgpPdggSpe;
	}

	public void setVConsPdgpPdggSpe(V_cons_pdgp_pdgg_speBulk bulk) {
		vConsPdgpPdggSpe = bulk;
	}
	public String getUoScrivania() {
		return uoScrivania;
	}
	public void setUoScrivania(String string) {
		uoScrivania = string;
	}
	/*
	 * I dettagli gestionali non possono essere mai gestiti se:
	 * - Il dettaglio è stato generato dalla ripartizione dei costi del personale 
	 *   (Classificazione_voci.Fl_accentrato = 'N' e Classificazione_voci.Fl_decentrato = 'N') 
	 */
	public boolean isDettagliGestionaliEnable(Pdg_modulo_speseBulk dett) {
		try {
			return Boolean.TRUE.equals(dett.getClassificazione().getFl_accentrato()) ||
				   Boolean.TRUE.equals(dett.getClassificazione().getFl_decentrato());
		}
		catch (NullPointerException e){
			return true;
		}
	}
	/*
	 * Il singolo dettaglio gestionale non può essere mai gestito se:
	 * - Il CDR collegato è quello di primo livello (corrispondente al CDR della testata)
	 * - Il CDR collegato è uguale al CDR Assegnatario
	 */
	public boolean isDettaglioGestionaleEnable(Pdg_modulo_spese_gestBulk dett) {
		try {
			return isDettagliGestionaliEnable(dett.getPdg_modulo_spese()) &&
				   (getUoScrivania().equals(dett.getPdg_modulo_spese().getPdg_modulo_costi().getPdg_modulo().getCdr().getUnita_padre().getCd_unita_organizzativa()) ||
				    dett.getCdr_assegnatario().getUnita_padre().getCd_unita_organizzativa().equals(getUoScrivania()));
		}
		catch (NullPointerException e){
			return true;
		}
	}
	public String getLabelDesctool_classificazione() {
		return labelDesctool_classificazione;
	}
	public void setLabelDesctool_classificazione(String string) {
		labelDesctool_classificazione = string;
	}
	public void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
	}
	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}
}
