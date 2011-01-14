/*
 * Created on Apr 1, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.bp;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Stampa_anag_progettiVBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.AbstractPrintBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProgettoStampaBP extends it.cnr.contab.reports.bp.ParametricPrintBP implements IProgettoBP{
	/**
	 * ParametricPrintBP constructor comment.
	 */
	public ProgettoStampaBP() {
		super();
	}
	/**
	 * ParametricPrintBP constructor comment.
	 * @param function java.lang.String
	 */
	public ProgettoStampaBP(String function) {
		super(function);
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.progettiric00.bp.IProgettoBP#getLivelloProgetto()
	 */
	public int getLivelloProgetto() {
		return ProgettoBulk.LIVELLO_PROGETTO_SECONDO.intValue();
	}
	@Override
	protected void init(Config config, ActionContext context) throws BusinessProcessException {
		super.init(config, context);
		if(getModel() instanceof Stampa_anag_progettiVBulk){
			Stampa_anag_progettiVBulk stampa = (Stampa_anag_progettiVBulk)getModel();
			stampa.setEsercizioForPrint(CNRUserContext.getEsercizio(context.getUserContext()));
			stampa.setTipo_fase("E");
			setModel(context,stampa);
		}
	}
}
