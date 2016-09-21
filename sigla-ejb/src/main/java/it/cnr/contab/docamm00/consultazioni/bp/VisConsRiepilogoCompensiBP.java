package it.cnr.contab.docamm00.consultazioni.bp;

import it.cnr.contab.compensi00.ejb.ConsRiepilogoCompensiComponentSession;
import it.cnr.contab.docamm00.consultazioni.bulk.VConsRiepCompensiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.ConsultazioniBP;

public class VisConsRiepilogoCompensiBP extends ConsultazioniBP
{
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		try {
		   	super.init(config,context);
			VConsRiepCompensiBulk bulk = new VConsRiepCompensiBulk();
			
			bulk = ((ConsRiepilogoCompensiComponentSession)createComponentSession()).impostaDatiIniziali(context.getUserContext(), bulk);		
			
			setModel(context,bulk);

			setSearchResultColumnSet("CONSULTAZIONE");
			setFreeSearchSet("CONSULTAZIONE");
 		    getBulkInfo().setShortDescription("Consultazione Riepilogo Compensi");
 		    getBulkInfo().setLongDescription("Consultazione Riepilogo Compensi");
			setMultiSelection(false);

		} catch(Throwable e) {
			throw handleException(e);
		}
	}
public ConsRiepilogoCompensiComponentSession createComponentSession() throws BusinessProcessException {
		return (ConsRiepilogoCompensiComponentSession)createComponentSession("CNRCOMPENSI00_EJB_ConsRiepilogoCompensiComponentSession",ConsRiepilogoCompensiComponentSession.class);
	}

}
