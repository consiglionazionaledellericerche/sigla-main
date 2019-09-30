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
 * Created on Feb 20, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.varstanz00.bp;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.contab.varstanz00.ejb.VariazioniStanziamentoResiduoComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDVar_stanz_resRigaBP extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	private Parametri_cnrBulk parametriCnr;
	private Var_stanz_resBulk var_stanz_res;
	private CdrBulk centro_di_responsabilita;
	private SimpleDetailCRUDController rigaVariazione = new SimpleDetailCRUDController( "rigaVariazione", Var_stanz_res_rigaBulk.class, "rigaVariazione", this){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			validaRiga(actioncontext,(Var_stanz_res_rigaBulk)oggettobulk);
			super.validate(actioncontext,oggettobulk);
		}

		@Override
		public void add(ActionContext actioncontext, OggettoBulk oggettobulk)
				throws BusinessProcessException {
			super.add(actioncontext, oggettobulk);
			Var_stanz_res_rigaBulk bulk = (Var_stanz_res_rigaBulk)oggettobulk;
			if (isVariazioneApprovata()){
				VariazioniStanziamentoResiduoComponentSession comp = ((VariazioniStanziamentoResiduoComponentSession)createComponentSession());
				try {
					Var_stanz_res_rigaBulk rigaDB = comp.recuperoRigaLiquidazioneIva(actioncontext.getUserContext(), var_stanz_res);
					if (rigaDB != null){
			    		Elemento_voceBulk elementoVoce = (Elemento_voceBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
								new Elemento_voceBulk(rigaDB.getElemento_voce().getCd_elemento_voce(), rigaDB.getElemento_voce().getEsercizio(), rigaDB.getElemento_voce().getTi_appartenenza(), rigaDB.getElemento_voce().getTi_gestione()));
						bulk.setElemento_voce(elementoVoce);
					}
				} catch (ComponentException e1) {
					throw handleException(e1);
				} catch (RemoteException e) {
					throw handleException(e);
				}
			}
		}
	};
	/**
	 * 
	 */
	public CRUDVar_stanz_resRigaBP() {
		super();
	}

	protected void validaRiga(ActionContext actioncontext,
			Var_stanz_res_rigaBulk oggettobulk) throws ValidationException {
		try { 
			valorizzaVoceLunga(actioncontext,oggettobulk);	
			valorizzaDisponibilita_stanz_res(actioncontext,oggettobulk);
		} catch (Exception e) { 
			throw new ValidationException(e.getMessage());
		}
	}

	/**
	 * @param s
	 */
	public CRUDVar_stanz_resRigaBP(String s) {
		super(s);
	}
	/**
	 * @param s
	 */
	public CRUDVar_stanz_resRigaBP(String s,Var_stanz_resBulk modello,CdrBulk centro) {
		super(s);
		var_stanz_res = modello;
		centro_di_responsabilita = centro;
	}
	
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getRigaVariazione() {
		return rigaVariazione;
	}

	/**
	 * @param controller
	 */
	public void setRigaVariazione(SimpleDetailCRUDController controller) {
		rigaVariazione = controller;
	}
	public boolean isDeleteButtonHidden() {
		return true;
	}
	public boolean isSearchButtonHidden() {
		return true;
	}
	public boolean isFreeSearchButtonHidden() {
		return true;
	}
	public boolean isNewButtonHidden() {
		return true;
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SimpleCRUDBP#initializeModelForEdit(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk)
	 */
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext,OggettoBulk oggettobulk)throws BusinessProcessException {
		var_stanz_res.setCdr(centro_di_responsabilita);
		var_stanz_res = (Var_stanz_resBulk)super.initializeModelForEdit(actioncontext,var_stanz_res);
		for (Iterator righeVar=var_stanz_res.getRigaVariazione().iterator();righeVar.hasNext();){
			Var_stanz_res_rigaBulk varRiga = (Var_stanz_res_rigaBulk)righeVar.next();
			if ((getParametriCnr()==null || !getParametriCnr().getFl_nuovo_pdg()) && varRiga.getCd_voce()!=null) {
		    	try {
		    		Voce_fBulk voce_f = (Voce_fBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), 
						new Voce_fBulk(varRiga.getCd_voce(), varRiga.getEsercizio_voce(), varRiga.getTi_appartenenza(), varRiga.getTi_gestione()));
		    		varRiga.setVoce_f(voce_f);
				} catch (DetailedRuntimeException e) {
					throw new BusinessProcessException(e);
				} catch (ComponentException e) {
					throw new BusinessProcessException(e);
				} catch (RemoteException e) {
					throw new BusinessProcessException(e);
				}
			}
			varRiga.setCentroTestata(var_stanz_res.getCdr());
		}		
		return var_stanz_res;
	}

	protected void initialize(ActionContext actioncontext)throws BusinessProcessException {
		setModel(actioncontext,initializeModelForEdit(actioncontext,var_stanz_res));
		if (isEditable())		
		  setStatus(EDIT);
		try {
			if (getStatus()!=VIEW){
				if (var_stanz_res!=null && var_stanz_res.isCancellatoLogicamente() || 
						var_stanz_res.isPropostaDefinitiva() || 
						(var_stanz_res.isApprovata() && !((VariazioniStanziamentoResiduoComponentSession)createComponentSession()).isVariazioneFromLiquidazioneIvaDaModificare(actioncontext.getUserContext(), var_stanz_res))|| 
						var_stanz_res.isRespinta()) 
					setStatus(VIEW);
			}	
			//		else  
			if (!isEditable())	
				setStatus(VIEW);	
			setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())));
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}
    public void valorizzaVoceLunga(ActionContext actioncontext, Var_stanz_res_rigaBulk var_stanz_res_riga) throws BusinessProcessException{
    	try {
			completeSearchTool(actioncontext,var_stanz_res_riga,var_stanz_res_riga.getBulkInfo().getFieldProperty("linea_di_attivita"));
			completeSearchTool(actioncontext,var_stanz_res_riga,var_stanz_res_riga.getBulkInfo().getFieldProperty("elemento_voce"));
		}catch (ValidationException e) {
			throw new BusinessProcessException(e);
		}
		if (var_stanz_res_riga.getLinea_di_attivita() != null && var_stanz_res_riga.getLinea_di_attivita().getCd_linea_attivita() != null &&
		    var_stanz_res_riga.getElemento_voce() != null && var_stanz_res_riga.getElemento_voce().getCd_elemento_voce() != null){
			if (getParametriCnr()==null || !getParametriCnr().getFl_nuovo_pdg()) {
				try {
					Voce_fBulk voce_f = ((VariazioniStanziamentoResiduoComponentSession)createComponentSession()).getVoce_FdaEV(
					  actioncontext.getUserContext(),
					  CNRUserContext.getEsercizio(actioncontext.getUserContext()),
					  var_stanz_res_riga.getElemento_voce().getTi_appartenenza(),
					  var_stanz_res_riga.getElemento_voce().getTi_gestione(), 
					  var_stanz_res_riga.getElemento_voce().getCd_elemento_voce() , 
					  var_stanz_res_riga.getLinea_di_attivita().getCd_centro_responsabilita(), 
					  var_stanz_res_riga.getLinea_di_attivita().getCd_linea_attivita()
					);
					var_stanz_res_riga.setVoce_f(voce_f);
				} catch (DetailedRuntimeException e) {
					throw new BusinessProcessException(e);
				} catch (ComponentException e) {
					throw new BusinessProcessException(e);
				} catch (RemoteException e) {
					throw new BusinessProcessException(e);
				}
			} else {
				var_stanz_res_riga.setVoce_f(null);
				var_stanz_res_riga.setCd_voce(var_stanz_res_riga.getElemento_voce().getCd_elemento_voce());
			}
		} else {
			var_stanz_res_riga.setVoce_f(null);
			var_stanz_res_riga.setCd_voce(null);
		}
    }

    public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException,ValidationException {
		super.save(context);
		if (isVariazioneApprovata()){
			setStatus(VIEW);
		}
	}

	public void valorizzaDisponibilita_stanz_res(ActionContext actioncontext, Var_stanz_res_rigaBulk var_stanz_res_riga) throws BusinessProcessException{
		try {
			if (var_stanz_res_riga.getLinea_di_attivita() != null && var_stanz_res_riga.getLinea_di_attivita().getCd_linea_attivita() != null &&
				var_stanz_res_riga.getElemento_voce() != null && var_stanz_res_riga.getElemento_voce().getCd_elemento_voce() != null){
					var_stanz_res_riga.setDisponibilita_stanz_res(((VariazioniStanziamentoResiduoComponentSession)createComponentSession()).calcolaDisponibilita_stanz_res(actioncontext.getUserContext(),var_stanz_res_riga));
				}else{
					var_stanz_res_riga.setDisponibilita_stanz_res(Utility.ZERO);
				}
		} catch (DetailedRuntimeException e) {
			throw new BusinessProcessException(e);
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}
	public void valorizzaProgettoLineaAttivita(ActionContext actioncontext, Var_stanz_res_rigaBulk var_stanz_res_riga) throws BusinessProcessException{
		try {
			if (var_stanz_res_riga.getLinea_di_attivita() != null && var_stanz_res_riga.getLinea_di_attivita().getCd_linea_attivita() != null){
					var_stanz_res_riga.setProgetto(((VariazioniStanziamentoResiduoComponentSession)createComponentSession()).getProgettoLineaAttivita(actioncontext.getUserContext(),var_stanz_res_riga));
				}else{
					var_stanz_res_riga.setProgetto(null);
				}
		} catch (DetailedRuntimeException e) {
			throw new BusinessProcessException(e);
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}
	private void setParametriCnr(Parametri_cnrBulk parametriCnr) {
		this.parametriCnr = parametriCnr;
	}
	
	public Parametri_cnrBulk getParametriCnr() {
		return parametriCnr;
	}
	public boolean isVariazioneApprovata() {
		if (var_stanz_res != null && var_stanz_res.isApprovata()){
			return true;
		}
		return false;
	}

}
