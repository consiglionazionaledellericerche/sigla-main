package it.cnr.contab.doccont00.action;


import java.rmi.RemoteException;

import javax.ejb.RemoveException;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.doccont00.bp.ConsRiepilogoSiopeMandatiBP;
import it.cnr.contab.doccont00.bp.ConsRiepilogoSiopeMandatiDettagliBP;
import it.cnr.contab.doccont00.bp.ConsRiepilogoSiopeReversaliBP;
import it.cnr.contab.doccont00.bp.ConsRiepilogoSiopeReversaliDettagliBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_siope_mandatiBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_siope_reversaliBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsRiepilogoSiopeAction extends BulkAction{

	public Forward doCercaMandati(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
			try {
				
				ConsRiepilogoSiopeMandatiBP bp = (ConsRiepilogoSiopeMandatiBP) context.getBusinessProcess();
				V_cons_siope_mandatiBulk mandati = (V_cons_siope_mandatiBulk)bp.getModel();
				mandati.setCd_cds(mandati.getCds().getCd_unita_organizzativa());
				bp.fillModel(context); 
//				bp.valorizzaDate(context,mandati);
					
				ConsRiepilogoSiopeMandatiDettagliBP DettBP = (ConsRiepilogoSiopeMandatiDettagliBP) context.createBusinessProcess("ConsRiepilogoSiopeMandatiDettagliBP");
				it.cnr.jada.util.RemoteIterator ri = DettBP.createComponentSession().findSiopeMandati(context.getUserContext(),mandati);
			
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
				if (ri.countElements() == 0) {
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile");
				}
					
				DettBP.setIterator(context,ri);
				DettBP.setMultiSelection(true);
				return context.addBusinessProcess(DettBP);			
						
			} catch (Exception e) {
					return handleException(context,e); 
			}
	}
					
			public Forward doCercaReversali(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
				try {
									
						ConsRiepilogoSiopeReversaliBP bp = (ConsRiepilogoSiopeReversaliBP) context.getBusinessProcess();
						V_cons_siope_reversaliBulk reversali = (V_cons_siope_reversaliBulk)bp.getModel();
						reversali.setCd_cds(reversali.getCds().getCd_unita_organizzativa());
						bp.fillModel(context); 
//						bp.valorizzaDate(context,reversali);
			
						
						ConsRiepilogoSiopeReversaliDettagliBP DettBP = (ConsRiepilogoSiopeReversaliDettagliBP) context.createBusinessProcess("ConsRiepilogoSiopeReversaliDettagliBP");
						it.cnr.jada.util.RemoteIterator ri = DettBP.createComponentSession().findSiopeReversali(context.getUserContext(),reversali);
						
						
						ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
						if (ri.countElements() == 0) {
							it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
							throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile");
						}
						
						DettBP.setIterator(context,ri);
						DettBP.setMultiSelection(true);
						return context.addBusinessProcess(DettBP);	
						
					} catch (Exception e) {
							return handleException(context,e); 
					}
				}
}
