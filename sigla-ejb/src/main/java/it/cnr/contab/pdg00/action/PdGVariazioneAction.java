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
 * Created on May 25, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Iterator;

import javax.ejb.EJBException;

import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.pdg00.bp.PdGVariazioneBP;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg01.bp.CRUDPdgVariazioneGestionaleBP;
import it.cnr.contab.pdg01.bp.SelezionatoreAssestatoBP;
import it.cnr.contab.pdg01.bulk.Tipo_variazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PdGVariazioneAction extends it.cnr.jada.util.action.CRUDAction {

	/**
	 * 
	 */
	public PdGVariazioneAction() {
		super();
	}
	/**
	 * Gestione dettagli di entrata del PDG
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doDettagliEtrPdG(it.cnr.jada.action.ActionContext context) {
		if (context.getBusinessProcess() instanceof CRUDPdgVariazioneGestionaleBP)
			return doDettagliPdG(context, "CRUDPdgVariazioneRigaGestBP", Elemento_voceHome.GESTIONE_ENTRATE);
		return doDettagliPdG(context, "CRUDEtrDetPdGBP");
	}

	public it.cnr.jada.action.Forward doDettagliEtrPdG(it.cnr.jada.action.ActionContext context,int i) {
		if (i==4) {
			if (context.getBusinessProcess() instanceof CRUDPdgVariazioneGestionaleBP)
				return doDettagliPdG(context, "CRUDPdgVariazioneRigaGestBP", Elemento_voceHome.GESTIONE_ENTRATE, false);
			return doDettagliPdG(context, "CRUDEtrDetPdGBP",false);
		}
		return context.findDefaultForward();	
	}	

	/**
	 * Gestione dettagli di PDG
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param bpName	Nome del sotto-bp da creare
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doDettagliPdG(it.cnr.jada.action.ActionContext context, String bpName) {
		return doDettagliPdG(context,bpName,true);
	}

	/**
	 * Gestione dettagli di PDG
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param bpName	Nome del sotto-bp da creare
	 * @param flag  	boolean
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doDettagliPdG(it.cnr.jada.action.ActionContext context, String bpName,boolean flag) {
		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)context.getBusinessProcess();
			if(bp.isDirty() && flag)
				return openContinuePrompt(context, bpName.equals("CRUDEtrDetPdGBP")?"doDettagliEtrPdG":"doDettagliSpePdG");					
			Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk)bp.getModel();
			Ass_pdg_variazione_cdrBulk ass_pdg_variazione = (Ass_pdg_variazione_cdrBulk)(pdg_variazione.getAssociazioneCDR().get(bp.getCrudAssCDR().getSelection().getFocus()));
			boolean tipoView = bp.getStatus() == bp.VIEW || !bp.isEditableDettagliVariazione();
			BulkBP nbp = (BulkBP)context.getUserInfo().createBusinessProcess(
							context,
							bpName,
							new Object[] {
								bp.getStatus() == bp.VIEW || 
								tipoView ? "V": "M",
								tipoView ? ass_pdg_variazione.getCentro_responsabilita() : bp.createCdrComponentSession().cdrFromUserContext(context.getUserContext()),
								pdg_variazione
							}
						);
			context.addHookForward("close",this,"doBringBackOpenDettagliPdGWindow");
			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	
	/**
	 * Gestione dettagli delle variazioni ai PDG 
	 */
	public it.cnr.jada.action.Forward doDettagliPdG(it.cnr.jada.action.ActionContext context, String bpName, String tipoVoce) {
		return doDettagliPdG(context,bpName,tipoVoce,true);
	}
	public it.cnr.jada.action.Forward doDettagliPdG(it.cnr.jada.action.ActionContext context, String bpName, String tipoVoce, boolean flag) {
		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)context.getBusinessProcess();
			if(bp.isDirty() && flag)
				return openContinuePrompt(context, tipoVoce.equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_ENTRATE)?"doDettagliEtrPdG":"doDettagliSpePdG");
			Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk)bp.getModel();
			Ass_pdg_variazione_cdrBulk ass_pdg_variazione = (Ass_pdg_variazione_cdrBulk)(pdg_variazione.getAssociazioneCDR().get(bp.getCrudAssCDR().getSelection().getFocus()));
			boolean tipoView = bp.getStatus() == bp.VIEW  
					|| !ass_pdg_variazione.getCentro_responsabilita().equalsByPrimaryKey(bp.getCentro_responsabilita_scrivania())
					||(!pdg_variazione.isPropostaProvvisoria() && !isVariazioneFromLiquidazioneIvaDaModificare(context, tipoVoce, bp, pdg_variazione, ass_pdg_variazione));
			BulkBP nbp = (BulkBP)context.getUserInfo().createBusinessProcess(
							context,
							bpName,
							new Object[] {
								bp.getStatus() == bp.VIEW || 
								tipoView ? "V": "M",
								tipoView ? ass_pdg_variazione.getCentro_responsabilita() : bp.createCdrComponentSession().cdrFromUserContext(context.getUserContext()),
								pdg_variazione,
								tipoVoce
							}
						);
			context.addHookForward("close",this,"doBringBackOpenDettagliPdGWindow");
			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	
	
	private boolean isVariazioneFromLiquidazioneIvaDaModificare(
			it.cnr.jada.action.ActionContext context, String tipoVoce,
			PdGVariazioneBP bp, Pdg_variazioneBulk pdg_variazione,
			Ass_pdg_variazione_cdrBulk ass_pdg_variazione)
			throws BusinessProcessException {
		boolean variazioneFromLiquidazioneIva = false;
		if (ass_pdg_variazione.getCentro_responsabilita().equalsByPrimaryKey(bp.getCentro_responsabilita_scrivania()) && tipoVoce.equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.GESTIONE_SPESE)){
			variazioneFromLiquidazioneIva = bp.isVariazioneFromLiquidazioneIvaDaModificare(context, pdg_variazione);
		}
		return variazioneFromLiquidazioneIva;
	}	

	public Forward doBringBackOpenDettagliPdGWindow(ActionContext context) {
		try {
			fillModel(context);
			if (getBusinessProcess(context) instanceof PdGVariazioneBP) {   
				PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
				bp.inizializzaSommeCdR(context);
				doTab(context,"tab","tabCDR");
			}
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}	
	/**
	 * Gestione dettagli di spesa del PDG
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doDettagliSpePdG(it.cnr.jada.action.ActionContext context) {
		if (context.getBusinessProcess() instanceof CRUDPdgVariazioneGestionaleBP)
			return doDettagliPdG(context, "CRUDPdgVariazioneRigaGestBP", Elemento_voceHome.GESTIONE_SPESE);
		return doDettagliPdG(context, "CRUDSpeDetPdGBP");
	}
	public it.cnr.jada.action.Forward doDettagliSpePdG(it.cnr.jada.action.ActionContext context,int i) {
		if (i==4) {
			if (context.getBusinessProcess() instanceof CRUDPdgVariazioneGestionaleBP)
				return doDettagliPdG(context, "CRUDPdgVariazioneRigaGestBP", Elemento_voceHome.GESTIONE_SPESE, false);
			return doDettagliPdG(context, "CRUDSpeDetPdGBP",false);
		}
		return context.findDefaultForward();	
	}		

	private it.cnr.jada.util.action.ConsultazioniBP creaConsultazioneBP(it.cnr.jada.action.ActionContext context, it.cnr.jada.util.action.ConsultazioniBP bp) throws EJBException, SQLException, IntrospectionException, PersistencyException, BusinessProcessException, ComponentException, RemoteException {
		
		SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
		Pdg_variazioneBulk pdg = (Pdg_variazioneBulk)rootbp.getModel();
		CompoundFindClause clauses = new CompoundFindClause();
		CompoundFindClause clausesModulo = new CompoundFindClause();			
		if (bp.getName().equals("ConsultazioneAssestatoSpeseModuloPdgBP")||
		    bp.getName().equals("ConsultazioneAssestatoCostiModuloPdgBP")) 
 		   	clauses.addClause("AND","ti_gestione",SQLBuilder.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
 		else
			clauses.addClause("AND","ti_gestione",SQLBuilder.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
		
		clauses.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg.getEsercizio());
		clauses.addClause("AND","pg_variazione_pdg",SQLBuilder.EQUALS,pdg.getPg_variazione_pdg());

		if (bp.getName().equals("ConsultazioneAssestatoSpeseModuloPdgBP")||
			bp.getName().equals("ConsultazioneAssestatoCostiModuloPdgBP")) { 
					clausesModulo= ((it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGVariazioniComponentSession", it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession.class)).aggiornaClausole(context.getUserContext(), pdg, Elemento_voceHome.GESTIONE_SPESE);
				}
		else {
					clausesModulo= ((it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGVariazioniComponentSession", it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession.class)).aggiornaClausole(context.getUserContext(), pdg, Elemento_voceHome.GESTIONE_ENTRATE);
				}
		if (clausesModulo != null) clauses.addChild(clausesModulo);
		bp.addToBaseclause(clauses);
		bp.openIterator(context);
		return bp;
	}
	
	/**
	 * Gestione apertura della consultazione dell'Assestato Spese per Modulo PDG
	 * Genera il BP "ConsultazioneAssestatoSpeseModuloPdgBP" con il quale attiva la mappa 
	 * di consultazione dell'Assestato relativo alle spese. 
	 * Visualizza unicamente le informazioni di assestato spese relative ai moduli 
	 * coinvolti nella variazione PDG a partire dalla quale viene richiesta la consultazione.   
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doConsAssestatoSpeseModuloPDG(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsultazioneAssestatoSpeseModuloPdgBP");
			creaConsultazioneBP(context, bp);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	/**
	 * Gestione apertura della consultazione dell'Assestato Costi per Modulo PDG
	 * Genera il BP "ConsultazioneAssestatoCostiModuloPdgBP" con il quale attiva la mappa 
	 * di consultazione dell'Assestato relativo ai costi. 
	 * Visualizza unicamente le informazioni di assestato costi relativi ai moduli 
	 * coinvolti nella variazione PDG a partire dalla quale viene richiesta la consultazione.   
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doConsAssestatoCostiModuloPDG(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsultazioneAssestatoCostiModuloPdgBP");
			creaConsultazioneBP(context, bp);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	/**
	 * Gestione apertura della consultazione dell'Assestato Entrate per Modulo PDG
	 * Genera il BP "ConsultazioneAssestatoEntrateModuloPdgBP" con il quale attiva la mappa 
	 * di consultazione dell'Assestato relativo alle entrate. 
	 * Visualizza unicamente le informazioni di assestato entrate relative ai moduli 
	 * coinvolti nella variazione PDG a partire dalla quale viene richiesta la consultazione.   
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doConsAssestatoEntrateModuloPDG(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsultazioneAssestatoEntrateModuloPdgBP");
			creaConsultazioneBP(context, bp);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	/**
	 * Gestione apertura della consultazione dell'Assestato Ricavi per Modulo PDG
	 * Genera il BP "ConsultazioneAssestatoRicaviModuloPdgBP" con il quale attiva la mappa 
	 * di consultazione dell'Assestato relativo ai ricavi. 
	 * Visualizza unicamente le informazioni di assestato ricavi relativi ai moduli 
	 * coinvolti nella variazione PDG a partire dalla quale viene richiesta la consultazione.    
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doConsAssestatoRicaviModuloPDG(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsultazioneAssestatoRicaviModuloPdgBP");
			creaConsultazioneBP(context, bp);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	/**
	 * Gestione della richiesta di salvataggio di una variazione come definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doSalvaDefinitivo(ActionContext context) {

		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
			bp.salvaDefinitivo(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}

	}
	
	/**
	 * Gestione della richiesta di approvazione una variazione definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doApprova(ActionContext context) {

		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
			bp.approva(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}

	}
	
	/**
	 * Gestione della richiesta di respingere una variazione definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doRespingi(ActionContext context) {

		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
			bp.respingi(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}

	}
	
	/**
	 * Gestisce un comando di cancellazione.
	 */
	public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

		try {
			fillModel(context);

			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
			if (!bp.isEditing()) {
				bp.setMessage("Non è possibile cancellare in questo momento");
			} else {
				bp.delete(context);
				bp.edit(context, bp.getModel());
				bp.setMessage("Annullamento effettuato");
			}
			return context.findDefaultForward();

		} catch(Throwable e) {
			return handleException(context,e);
		}
	}		
	
	public Forward doSalva(ActionContext context) throws RemoteException {
		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);

			if (((Pdg_variazioneBulk)bp.getModel()).getTipo_variazione()==null) {		
				bp.setMessage("Tipologia di Variazione non indicata.");
				return context.findDefaultForward();
			}

			String message = bp.controllaTotPropostoEntrataSpesa(context);
			if (message != null){
				if (((Pdg_variazioneBulk)bp.getModel()).getTipo_variazione().isMovimentoSuFondi()) {
					bp.setMessage(message);
					return context.findDefaultForward();
				}
				else 
				{
					message += "<BR>Effettuare comunque il salvataggio?";
					openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doSalvaConfermato");
				}
			}
			else
			  super.doSalva(context);
		} catch (BusinessProcessException ex) {
			return handleException(context,ex);
		} catch (FillException e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	
	public Forward doSalvaConfermato(ActionContext context, int opt) throws RemoteException {
		if (opt == OptionBP.YES_BUTTON) {
			super.doSalva(context);
		}
		return context.findDefaultForward();
	}

	public Forward doApprovazioneFormale(ActionContext context) {
		try {
			CRUDBP bp = getBusinessProcess(context);
			Pdg_variazioneBulk pdgv = new Pdg_variazioneBulk();
			CompoundFindClause compoundfindclause = new CompoundFindClause();
			compoundfindclause.addClause("AND","esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(context.getUserContext()));
			compoundfindclause.addClause("AND","stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
			compoundfindclause.addClause("AND","fl_visto_dip_variazioni", SQLBuilder.EQUALS, Boolean.TRUE);
			
			it.cnr.jada.util.RemoteIterator ri = bp.find(context, compoundfindclause, pdgv);

			if (ri == null || ri.countElements() == 0) {
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
				bp.setMessage("La ricerca non ha fornito alcun risultato.");
				return context.findDefaultForward();
			} else {
				SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("PdGVarSelezionatoreListaBP");
				nbp.setIterator(context,ri);
				nbp.setMultiSelection(true);
				nbp.setBulkInfo(pdgv.getBulkInfo());
				nbp.setColumns( nbp.getBulkInfo().getColumnFieldPropertyDictionary("approvazione_formale"));
				return context.addBusinessProcess(nbp);
			}
		} catch(Throwable e) {
			return handleException(context,e);
		} 
	}
	
	public Forward doApponiVistoMultiplo(ActionContext context) {
		try {
			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
			Pdg_variazioneBulk pdgv = new Pdg_variazioneBulk();
			CompoundFindClause compoundfindclause = new CompoundFindClause();
			compoundfindclause.addClause("AND","esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(context.getUserContext()));
			compoundfindclause.addClause("AND","stato", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
			compoundfindclause.addClause("AND","fl_visto_dip_variazioni", SQLBuilder.EQUALS, Boolean.FALSE);
			
			it.cnr.jada.util.RemoteIterator ri = bp.findVariazioniForApposizioneVisto(context, compoundfindclause, pdgv);

			if (ri == null || ri.countElements() == 0) {
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
				bp.setMessage("La ricerca non ha fornito alcun risultato.");
				return context.findDefaultForward();
			} else {
				SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("PdgVarApponiVistoListaBP");
				nbp.setIterator(context,ri);
				nbp.setMultiSelection(true);
				nbp.setBulkInfo(pdgv.getBulkInfo());
				nbp.setColumns( nbp.getBulkInfo().getColumnFieldPropertyDictionary("approvazione_formale"));
				return context.addBusinessProcess(nbp);
			}
		} catch(Throwable e) {
			return handleException(context,e);
		} 
	}

	public Forward doStatoPrecedente(ActionContext context) {
		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
			bp.statoPrecedente(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	
	public Forward doAssestatoEntrate(ActionContext context) throws BusinessProcessException {
		return doAssestato(context,true,CostantiTi_gestione.TI_GESTIONE_ENTRATE);
	}

	public it.cnr.jada.action.Forward doAssestatoEntrate(it.cnr.jada.action.ActionContext context,int option) throws BusinessProcessException {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON){
			getBusinessProcess(context).edit(context, getBusinessProcess(context).getModel());
			return doAssestato(context,false,CostantiTi_gestione.TI_GESTIONE_ENTRATE);			
		}
		return context.findDefaultForward();	
	}

	public Forward doAssestatoSpese(ActionContext context) throws BusinessProcessException {
		return doAssestato(context,true,CostantiTi_gestione.TI_GESTIONE_SPESE);
	}

	public it.cnr.jada.action.Forward doAssestatoSpese(it.cnr.jada.action.ActionContext context,int option) throws BusinessProcessException {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON){
			getBusinessProcess(context).edit(context, getBusinessProcess(context).getModel());
			return doAssestato(context,false,CostantiTi_gestione.TI_GESTIONE_SPESE);			
		}
		return context.findDefaultForward();	
	}

	private Forward doAssestato(ActionContext context,boolean flag, String s) throws BusinessProcessException {
		try {
			fillModel(context);
			if(getBusinessProcess(context).isDirty() && flag)
				return openContinuePrompt(context, s.equals(CostantiTi_gestione.TI_GESTIONE_ENTRATE)?"doAssestatoEntrate":"doAssestatoSpese");
			SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)context.createBusinessProcess("SelezionatoreAssestatoBP",
												new Object[]{ 
												"M",
												getBusinessProcess(context).getModel(),
												s});
			context.addHookForward("seleziona",this,"doBringBackOpenDettagliWindow");		
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
		
	}	

	public Forward doBringBackOpenDettagliWindow(ActionContext context) {
		try {
			fillModel(context);
			getBusinessProcess(context).edit(context, getBusinessProcess(context).getModel());
			getBusinessProcess(context).setTab("tab","tabCDR");
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}	

	public Forward doOnSalvaDefinitivoDispAssestatoCdrGAEVoceFailed( ActionContext context, int option) 
	{
		if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) 
		{
			try 
			{
				CRUDBP bp = getBusinessProcess(context);
				((Pdg_variazioneBulk)bp.getModel()).setCheckDispAssestatoCdrGAEVoceEseguito( true );
				doSalvaDefinitivo(context);
			} 
			catch(Throwable e) 
			{
				return handleException(context,e);
			}
		}
		return context.findDefaultForward();
	}

	public Forward doOnChangeTipologia(ActionContext context) {
		try {
			fillModel(context);
			Pdg_variazioneBulk pdgVar = (Pdg_variazioneBulk)getBusinessProcess(context).getModel();
			if (!pdgVar.getTipo_variazione().isMovimentoSuFondi())
				pdgVar.setElemento_voce(null);
			if (!pdgVar.getTipo_variazione().getFl_variazione_trasferimento()) {
				pdgVar.setMapMotivazioneVariazione(null);
				pdgVar.setTiMotivazioneVariazione(null);
				pdgVar.setIdMatricola(null);
				pdgVar.setIdBando(null);
			}				
			if (!(Tipo_variazioneBulk.STORNO_SPESA_ISTITUTI_DIVERSI.equals(pdgVar.getTipologia())||Tipo_variazioneBulk.STORNO_SPESA_STESSO_ISTITUTO.equals(pdgVar.getTipologia())))
				pdgVar.setDs_causale(null);
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}	

	public Forward doApponiVistoDipartimento(ActionContext context) {
		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
			if(bp.getCrudAssCDR().getSelection().isEmpty())
			{
				bp.setMessage(2, "E' necessario selezionare almeno un elemento");
				return context.findDefaultForward();
			} else
			{
				for (Iterator s = bp.getCrudAssCDR().getSelectedModels(context).iterator();s.hasNext();)
					bp.apponiVistoDipartimento(context, (Ass_pdg_variazione_cdrBulk)s.next());
			}
			bp.edit(context, bp.getModel());
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}

	public Forward doOnChangeMapMotivazioneVariazione(ActionContext context) {
		try {
			fillModel(context);
			((PdGVariazioneBP)getBusinessProcess(context)).aggiornaMotivazioneVariazione(context);
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}
	
	public Forward doBlankSearchFindProgettoRimodulato(ActionContext context, Pdg_variazioneBulk variazione) {
		try {
			fillModel(context);
			variazione.setProgettoRimodulatoForSearch(new ProgettoBulk());
			variazione.setProgettoRimodulazione(null);
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}

	public Forward doBringBackSearchFindProgettoRimodulato(ActionContext context, Pdg_variazioneBulk variazione, ProgettoBulk progetto) {
		try {
			fillModel(context);
			PdGVariazioneBP bp = (PdGVariazioneBP)getBusinessProcess(context);
			variazione.setProgettoRimodulatoForSearch(progetto);
			bp.findAndSetRimodulazione(context, progetto);
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}

	public Forward doPrendiInCarico(ActionContext context) {
		try {
			fillModel(context);
			Pdg_variazioneBulk pdgVar = (Pdg_variazioneBulk)getBusinessProcess(context).getModel();
			pdgVar.setAssegnazione(CNRUserContext.getUser(context.getUserContext()));
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
}
