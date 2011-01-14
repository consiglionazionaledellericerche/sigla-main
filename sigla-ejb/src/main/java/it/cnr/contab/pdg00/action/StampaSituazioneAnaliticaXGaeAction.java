/*
 * Created on Oct 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;



import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bulk.Stampa_situazione_analitica_x_GAEBulk;
import it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaSituazioneAnaliticaXGaeAction extends ParametricPrintAction {
	public StampaSituazioneAnaliticaXGaeAction() {
		super();
	}
	
	public PdGPreventivoComponentSession createPdgComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (PdGPreventivoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGPreventivoComponentSession", PdGPreventivoComponentSession.class);
	}
	
	public Forward doBlankSearchFindUoForPrint(ActionContext context, Stampa_situazione_analitica_x_GAEBulk stampa){
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Stampa_situazione_analitica_x_GAEBulk stampa_gae = ((Stampa_situazione_analitica_x_GAEBulk)bp.getModel());
		stampa_gae.setUoForPrint(new Unita_organizzativaBulk());
		stampa_gae.setCdrForPrint(new CdrBulk());
		
		return context.findDefaultForward();
	}
	
	public Forward doBlankSearchFindCdsForPrint(ActionContext context, Stampa_situazione_analitica_x_GAEBulk stampa){
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Stampa_situazione_analitica_x_GAEBulk stampa_gae = ((Stampa_situazione_analitica_x_GAEBulk)bp.getModel());
		stampa_gae.setCdsForPrint(new CdsBulk());
		stampa_gae.setUoForPrint(new Unita_organizzativaBulk());
		stampa_gae.setCdrForPrint(new CdrBulk());
		
		return context.findDefaultForward();
	}
}
