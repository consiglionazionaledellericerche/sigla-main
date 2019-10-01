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
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_etrBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.ejb.PdgModuloEntrateComponentSession;
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
public class CRUDPdgModuloEntrateGestBP extends SimpleCRUDBP {
	private V_cons_pdgp_pdgg_etrBulk vConsPdgpPdggEtr;
	private String uoScrivania;
	private String labelClassificazione;
	private boolean flNuovoPdg = false;

	private SimpleDetailCRUDController crudDettagliGestionali = new SimpleDetailCRUDController( "DettagliGestionali", Pdg_modulo_entrate_gestBulk.class, "dettagli_gestionali", this){
		protected void setModel(ActionContext actioncontext,OggettoBulk oggettobulk) {
			super.setModel(actioncontext, oggettobulk);
		}
		public boolean isShrinkable() {
			return super.isShrinkable()&& 
				   isDettaglioGestionaleEnable((Pdg_modulo_entrate_gestBulk)getModel());
		}
		public void removeAll(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
			List list = getDetails();
			for(int i = list.size() - 1; i >= 0; i--)
			{
				Pdg_modulo_entrate_gestBulk dett=(Pdg_modulo_entrate_gestBulk)list.get(i);			
				if (isDettaglioGestionaleEnable(dett))
					removeDetail(dett, i);
			}
			getParentController().setDirty(true);
			reset(actioncontext);
		}
	};

	public PdgModuloEntrateComponentSession createPdgModuloEntrateComponentSession() throws BusinessProcessException {
		return (PdgModuloEntrateComponentSession)createComponentSession("CNRPREVENT01_EJB_Pdg_Modulo_EntrateComponentSession", PdgModuloEntrateComponentSession.class);		
	}

	public CRUDPdgModuloEntrateGestBP() {
		super();
	}

	public CRUDPdgModuloEntrateGestBP(String function) {
		super(function);
	}

	public CRUDPdgModuloEntrateGestBP(String function,V_cons_pdgp_pdgg_etrBulk vConsPdgpPdggEtr) {
		super(function);
		setVConsPdgpPdggEtr(vConsPdgpPdggEtr);	
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
			Pdg_Modulo_EntrateBulk pdgModuloEntrate = createPdgModuloEntrateComponentSession().getPdgModuloEntrateBulk(actioncontext.getUserContext(),getVConsPdgpPdggEtr()); 
			setModel(actioncontext,super.initializeModelForEdit(actioncontext,pdgModuloEntrate));
			setUoScrivania(CNRUserContext.getCd_unita_organizzativa(actioncontext.getUserContext()));
			setLabelClassificazione(Utility.createClassificazioneVociComponentSession(). 
										getDsLivelloClassificazione(actioncontext.getUserContext(),
																	CNRUserContext.getEsercizio(actioncontext.getUserContext()),
																	Elemento_voceHome.GESTIONE_ENTRATE,
																	((Pdg_Modulo_EntrateBulk)getModel()).getClassificazione_voci().getNr_livello()));
			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())); 
			setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
			setStatus(EDIT);
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

	public V_cons_pdgp_pdgg_etrBulk getVConsPdgpPdggEtr() {
		return vConsPdgpPdggEtr;
	}

	public void setVConsPdgpPdggEtr(V_cons_pdgp_pdgg_etrBulk bulk) {
		vConsPdgpPdggEtr = bulk;
	}
	public String getUoScrivania() {
		return uoScrivania;
	}
	public void setUoScrivania(String string) {
		uoScrivania = string;
	}
	/*
	 * Il singolo dettaglio gestionale non può essere mai gestito se:
	 * - Il CDR collegato è quello di primo livello (corrispondente al CDR della testata)
	 * - Il CDR collegato è uguale al CDR Assegnatario
	 */
	public boolean isDettaglioGestionaleEnable(Pdg_modulo_entrate_gestBulk dett) {
		try {
			return getUoScrivania().equals(dett.getPdg_modulo_entrate().getTestata().getCdr().getUnita_padre().getCd_unita_organizzativa()) ||
				   dett.getCdr_assegnatario().getUnita_padre().getCd_unita_organizzativa().equals(getUoScrivania());
		}
		catch (NullPointerException e){
			return true;
		}
	}
	public String getLabelClassificazione() {
		return labelClassificazione;
	}
	public void setLabelClassificazione(String string) {
		labelClassificazione = string;
	}
	public void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
	}
	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}
}
