/*
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.consultazioni.action;

import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.prevent01.consultazioni.bp.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class ConsPDGGDipfoAction extends ConsultazioniAction {

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsPDGGDipFoBP bp = (ConsPDGGDipFoBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}
			
			ConsPDGGDipFoBP consultazioneBP = null;
			if (bp instanceof ConsPDGGDipFoSpeBP) 
				consultazioneBP = (ConsPDGGDipFoSpeBP)context.createBusinessProcess("ConsPDGGDipFoSpeBP");
			else
				consultazioneBP = (ConsPDGGDipFoEtrBP)context.createBusinessProcess("ConsPDGGDipFoEtrBP");
			
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			if ((bp.getElementsCount()!=selectElements)||!(bp.getBaseclause().toString().equals(consultazioneBP.getBaseclause().toString()))||bp.getFindclause()!=null)
				consultazioneBP.addToBaseclause(bp.getSelezione(context));
			if (consultazioneBP instanceof ConsPDGGDipFoSpeBP)
				consultazioneBP.setIterator(context,consultazioneBP.createPdggDipfoComponentSession().findConsultazioneSpe(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			else
				consultazioneBP.setIterator(context,consultazioneBP.createPdggDipfoComponentSession().findConsultazioneEtr(context.getUserContext(),bp.getPathDestinazione(livelloDestinazione),livelloDestinazione,consultazioneBP.getBaseclause(),null));			
			
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doConsultaCdr(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_CDR);
	}
	public Forward doConsultaProgetto(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_PRO);
	}
	public Forward doConsultaFo(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_TIP);
	}
	public Forward doConsultaCommessa(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_COM);
	}
	public Forward doConsultaModulo(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_MOD);
	}
	public Forward doConsultaLivello1(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_LIV1);
	}
	public Forward doConsultaLivello2(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_LIV2);
	}
	public Forward doConsultaLivello3(ActionContext context) {
			return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_LIV3);
		}
	public Forward doConsultaLinea(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_LIN);
	}
	public Forward doConsultaVoce(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_VOC);
	}
	public Forward doConsultaDettagli(ActionContext context) {
		return doConsulta(context, ConsPDGGDipFoBP.LIVELLO_DET);
	}
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsPDGGDipFoBP) {
			ConsPDGGDipFoBP bp = (ConsPDGGDipFoBP)context.getBusinessProcess();
			bp.setTitle();
		}
		return appoForward;
	}
}