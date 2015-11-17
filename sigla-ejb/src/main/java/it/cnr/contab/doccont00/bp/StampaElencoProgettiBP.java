package it.cnr.contab.doccont00.bp;

import it.cnr.contab.reports.bp.ParametricPrintBP;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.doccont00.core.bulk.Stampa_elenco_progetti_laBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;

public class StampaElencoProgettiBP extends ParametricPrintBP{
private Parametri_cnrBulk parametriCnr;
	public Parametri_cnrBulk getParametriCnr() {
		return parametriCnr;
	}

	public void setParametriCnr(Parametri_cnrBulk parametriCnr) {
		this.parametriCnr = parametriCnr;
	}

	public StampaElencoProgettiBP() {
		super();
	}
	
	public StampaElencoProgettiBP(String function) {
		super(function);
	}
	@Override
	protected void initialize(ActionContext context)
			throws BusinessProcessException {
		try {
			setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
			super.initialize(context);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);		
		} catch (EJBException e) {
			throw handleException(e);
		}
	}

public String getLabelFindProgettoForPrint(){
	if (this.getParametriCnr().getFl_nuovo_pdg()) 
		return ProgettoBulk.LABEL_AREA_PROGETTUALE;
	else
		return ProgettoBulk.LABEL_PROGETTO;
}	
public String getLabelFindCommessaForPrint(){ 
	if (this.getParametriCnr().getFl_nuovo_pdg())
		return ProgettoBulk.LABEL_PROGETTO;
	else
		return ProgettoBulk.LABEL_COMMESSA;
}

}
