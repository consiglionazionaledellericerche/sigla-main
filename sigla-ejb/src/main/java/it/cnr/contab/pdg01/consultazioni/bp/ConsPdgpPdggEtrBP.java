/*
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.consultazioni.bp;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPdgpPdggEtrBP extends ConsultazioniBP {
	private static final long serialVersionUID = 1L;

	private String labelCd_classificazione;
	private boolean flNuovoPdg = false;

	public ConsPdgpPdggEtrBP(String s) {
		super(s);
	}

	public ConsPdgpPdggEtrBP() {
		super();
	}

	protected void init(it.cnr.jada.action.Config config, ActionContext context) throws BusinessProcessException {
		try{
			super.init(config, context);
			
 		    Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
		    Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), esercizio); 

			setLabelCd_classificazione(Utility.createClassificazioneVociComponentSession(). 
									   getDsLivelloClassificazione(context.getUserContext(),
																   CNRUserContext.getEsercizio(context.getUserContext()),
																   Elemento_voceHome.GESTIONE_ENTRATE,
																   parCnr.getLivello_pdg_decis_etr()));
 		    setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} 
	}

	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli");
		button.setSeparator(true);

		listButton.addElement(button);

		return listButton;
	}

	public void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
	}
	
	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}
	
	public String getLabelCd_classificazione() {
		return labelCd_classificazione;
	}
	
	public void setLabelCd_classificazione(String string) {
		labelCd_classificazione = string;
	}
	
	public String getLabelCd_progetto(){
		if (this.isFlNuovoPdg())
			return ProgettoBulk.LABEL_PROGETTO;
		else
			return "Modulo di Attività";
	}	
}
