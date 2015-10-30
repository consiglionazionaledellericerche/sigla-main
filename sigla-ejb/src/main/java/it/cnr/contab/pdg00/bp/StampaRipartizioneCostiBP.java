package it.cnr.contab.pdg00.bp;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;

public class StampaRipartizioneCostiBP  extends it.cnr.contab.reports.bp.ParametricPrintBP {
	private Parametri_cnrBulk parametriCnr;
	public Parametri_cnrBulk getParametriCnr() {
		return parametriCnr;
	}

	public void setParametriCnr(Parametri_cnrBulk parametriCnr) {
		this.parametriCnr = parametriCnr;
	}

	public StampaRipartizioneCostiBP() {
		super();
	}
	
	public StampaRipartizioneCostiBP(String function) {
		super(function);
	}
	@Override
	protected void initialize(ActionContext context)
			throws BusinessProcessException {
		try {
			setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);		
		} catch (EJBException e) {
			throw handleException(e);
		}
		super.initialize(context);
	}

public String getLabelFindCommessaForPrint(){
	if (this.getParametriCnr().getFl_nuovo_pdg()) 
		return ProgettoBulk.LABEL_AREA_PROGETTUALE;
	else
		return ProgettoBulk.LABEL_COMMESSA;
}	
public String getLabelFindModuloForPrint(){
	if (this.getParametriCnr().getFl_nuovo_pdg())
		return ProgettoBulk.LABEL_PROGETTO;
	else
		return ProgettoBulk.LABEL_MODULO;
}

}
