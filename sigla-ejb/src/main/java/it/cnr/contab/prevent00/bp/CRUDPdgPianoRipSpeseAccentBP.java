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
 * Created on Sep 14, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import it.cnr.contab.config00.ejb.Classificazione_vociComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoBulk;
import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.prevent00.ejb.PdgPianoRipartoComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Piano di Riparto
 */

public class CRUDPdgPianoRipSpeseAccentBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private boolean isPdgPianoRipartoDefinitivo;
	private SimpleDetailCRUDController crudPdgPianoRipartoSpese = new SimpleDetailCRUDController( "PdgPianoRipartoSpese", Pdg_piano_ripartoBulk.class, "pdgPianoRipartoSpese", this){
		private CompoundFindClause filterController;

		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk)	throws ValidationException {
			super.validateForDelete(actioncontext, oggettobulk);
			if ((((Pdg_piano_ripartoBulk)oggettobulk).getIm_tot_spese_acc())!=null)
				((Classificazione_vociBulk)getParentModel()).setTot_imp_piano_riparto_spese(((Classificazione_vociBulk)getParentModel()).getTot_imp_piano_riparto_spese().subtract(((Pdg_piano_ripartoBulk)oggettobulk).getIm_tot_spese_acc()));
		}
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			try {
				super.validate(actioncontext, oggettobulk);
				if (((CRUDBP)actioncontext.getBusinessProcess()).isDirty()) {
					Classificazione_vociBulk cla = (Classificazione_vociBulk)getParentModel();
					PdgPianoRipartoComponentSession component = (PdgPianoRipartoComponentSession)createComponentSession("CNRPDG00_EJB_PdgPianoRipartoComponentSession", PdgPianoRipartoComponentSession.class);
					cla = component.aggiornaTotaleGeneraleImpdaRipartire(actioncontext.getUserContext(), cla);
					((Classificazione_vociBulk)getParentModel()).setTot_imp_piano_riparto_spese(cla.getTot_imp_piano_riparto_spese());
				}
			} catch(Exception e) {
				handleException(e);
			} 
		}
		/* 
		 * Metodo che attiva il tasto di creazione nel CRUDController
		 */
		public boolean isGrowable() {
			V_classificazione_vociBulk cla = (V_classificazione_vociBulk)getParentModel();
			return	super.isGrowable() && cla.getCd_classificazione()!=null;
		}
		/* 
		 * Metodo che attiva il tasto di cancellazione nel CRUDController
		 */
		public boolean isShrinkable() {
			V_classificazione_vociBulk cla = (V_classificazione_vociBulk)getParentModel();
			return	super.isShrinkable() && cla.getCd_classificazione()!=null;
		}	

		public void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause) {
			try {
				CRUDPdgPianoRipSpeseAccentBP bp = (CRUDPdgPianoRipSpeseAccentBP)actioncontext.getBusinessProcess(); 
				Classificazione_vociBulk cla = (Classificazione_vociBulk)bp.getModel();
				cla = createClassificazione_vociComponentSession().caricaPdgPianoRipartoSpese(actioncontext.getUserContext(), cla, compoundfindclause);
				
				PdgPianoRipartoComponentSession component = (PdgPianoRipartoComponentSession)createComponentSession("CNRPDG00_EJB_PdgPianoRipartoComponentSession", PdgPianoRipartoComponentSession.class);
				cla = component.aggiornaTotaleGeneraleImpdaRipartire(actioncontext.getUserContext(), cla);

				bp.setModel(actioncontext, cla);

				filterController = compoundfindclause;
			} catch (DetailedRuntimeException e) {
				handleException(e);
			} catch (ComponentException e) {
				handleException(e);
			} catch (RemoteException e) {
				handleException(e);
			} catch (BusinessProcessException e) {
				handleException(e);
			}
		}
		public boolean isFiltered() {
			return filterController != null;
		}
		public CompoundFindClause getFilter()
		{
			return filterController;
		}
		protected void clearFilter(ActionContext actioncontext)
		{
			clearFilter();
		}
		protected void clearFilter()
		{
			filterController = null;
		}
	};

	public CRUDPdgPianoRipSpeseAccentBP() {
		super();
		
	}

	public CRUDPdgPianoRipSpeseAccentBP(String function) {
		super(function);
		
	}

	protected void initialize(ActionContext actioncontext)	throws BusinessProcessException {
		super.initialize(actioncontext);

		PdgPianoRipartoComponentSession sessione = (PdgPianoRipartoComponentSession) createComponentSession();
		try {
			isPdgPianoRipartoDefinitivo = sessione.isPdgPianoRipartoDefinitivo(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	/**
	 * Crea una componente di sessione
	 * 
	 *
	 * @return 
	 * @throws EJBException	Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
	 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
	 */
	public Classificazione_vociComponentSession createClassificazione_vociComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException, BusinessProcessException {
		return (Classificazione_vociComponentSession)createComponentSession("CNRCONFIG00_EJB_Classificazione_vociComponentSession",Classificazione_vociComponentSession.class);
	}

	public SimpleDetailCRUDController getCrudPdgPianoRipartoSpese() {
		return crudPdgPianoRipartoSpese;
	}

	public void setCrudPdgPianoRipartoSpese(SimpleDetailCRUDController controller) {
		crudPdgPianoRipartoSpese = controller;
	}

	/*
	 * Anche se la mappa è basata sul Bulk V_classificazione_voci, la sua funzione principale non 
	 * è di creare classificazioni, ma di caricare i piani di riparto associati alla classificazione 
	 * Per questo il bottone di creazione deve essere nascosto
	 */
	public boolean isNewButtonHidden() {
		return true;
	}

	/*
	 * Anche se la mappa è basata sul Bulk V_classificazione_voci, la sua funzione principale non 
	 * è di cancellare classificazioni, ma di caricare i piani di riparto associati alla classificazione 
	 * Per questo il bottone di cancellazione deve essere nascosto
	 */
	public boolean isDeleteButtonHidden() {
		return true;
	}

	/**
	 *  Il metodo aggiunge alla normale toolbar del CRUD i bottoni di "rendi definitivo", "rendi provvisorio"
	 *  e di "Consultazione Piano Riparto".
	 */
	protected it.cnr.jada.util.jsp.Button[] createToolbar() 
	{
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 3];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];

		Button button = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.rendiDefinitivo");
		button.setSeparator(true);

		newToolbar[ i ] = button;
		newToolbar[ i+1 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.rendiProvvisorio");

		button = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.consultazionePianoRiparto");
		button.setSeparator(true);
		newToolbar[ i+2] = button;

		return newToolbar;
	}

	public boolean isRendiDefinitivoButtonEnabled() {
		return !isSearching()&&
		       ((Classificazione_vociBulk)getModel()).getId_classificazione()!=null&&
		       !isPdgPianoRipartoDefinitivo();
	}

	public boolean isRendiProvvisorioButtonEnabled() {
		return !isSearching()&&
	 		   ((Classificazione_vociBulk)getModel()).getId_classificazione()!=null&&
		       isPdgPianoRipartoDefinitivo();
	}

	public boolean isConsultaPianoRipartoButtonEnabled() {
		return !isSearching();
		//&&((Classificazione_vociBulk)getModel()).getId_classificazione()!=null;
	}

	public void rendiPdgPianoRipartoDefinitivo(ActionContext actionContext) throws BusinessProcessException {
		try {
			PdgPianoRipartoComponentSession sessione = (PdgPianoRipartoComponentSession) createComponentSession();
			Classificazione_vociBulk cla = sessione.rendiPdgPianoRipartoDefinitivo(actionContext.getUserContext(), (Classificazione_vociBulk)getModel());
			edit(actionContext,cla);
			setPdgPianoRipartoDefinitivo(true);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	public void rendiPdgPianoRipartoProvvisorio(ActionContext actionContext) throws BusinessProcessException {
		try {
			PdgPianoRipartoComponentSession sessione = (PdgPianoRipartoComponentSession) createComponentSession();
			Classificazione_vociBulk cla = sessione.rendiPdgPianoRipartoProvvisorio(actionContext.getUserContext(), (Classificazione_vociBulk)getModel());
			edit(actionContext,cla);
			setPdgPianoRipartoDefinitivo(false);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	public boolean isPdgPianoRipartoDefinitivo() {
		return isPdgPianoRipartoDefinitivo;
	}

	private void setPdgPianoRipartoDefinitivo(boolean b) {
		isPdgPianoRipartoDefinitivo = b;
	}
	
	public boolean isSearchCrudPdgPianoRipartoSpeseEnabled() {
		V_classificazione_vociBulk cla = (V_classificazione_vociBulk)getModel();
		return cla.getCd_classificazione() != null;
	}

	public void caricaStruttura(ActionContext actionContext) throws BusinessProcessException {
		try {
			PdgPianoRipartoComponentSession sessione = (PdgPianoRipartoComponentSession) createComponentSession();
			Classificazione_vociBulk cla = sessione.caricaStruttura(actionContext.getUserContext(), (Classificazione_vociBulk)getModel());
			edit(actionContext,cla);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}
	public void inizializzaImTotSpeseAcc(ActionContext actionContext) throws BusinessProcessException {
		try {
			Classificazione_vociBulk cla = (Classificazione_vociBulk)getModel();

			for (java.util.Iterator pianoRiparto = cla.getPdgPianoRipartoSpese().iterator();pianoRiparto.hasNext();){
				Pdg_piano_ripartoBulk pianoRiparto_det = (Pdg_piano_ripartoBulk)pianoRiparto.next();			
				if (pianoRiparto_det.getIm_tot_spese_acc()==null)
					pianoRiparto_det.setIm_tot_spese_acc(new BigDecimal(0));
					pianoRiparto_det.setToBeUpdated();
			}
			update(actionContext);
			edit(actionContext,cla);
		} catch (BusinessProcessException e) {
			throw handleException(e);
		} catch (DetailedRuntimeException e) {
			throw handleException(e);
		}
	}
}
