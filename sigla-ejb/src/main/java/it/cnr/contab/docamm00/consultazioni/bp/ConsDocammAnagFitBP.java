package it.cnr.contab.docamm00.consultazioni.bp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.docamm00.consultazioni.bulk.VDocAmmAnagManrevBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.BulkBP;

public class ConsDocammAnagFitBP extends BulkBP
{	
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
		VDocAmmAnagManrevBulk bulk = new VDocAmmAnagManrevBulk();
		bulk.setAnagrafico(new AnagraficoBulk());
		setModel(context,bulk);
		super.init(config,context);
	}
	   
	public boolean isRicercaButtonEnabled()
	{
		return true;
	}
	
	public AnagraficoComponentSession createComponentSession()
			throws javax.ejb.EJBException,
					java.rmi.RemoteException,
					BusinessProcessException {
			return (AnagraficoComponentSession)createComponentSession("CNRANAGRAF00_EJB_AnagraficoComponentSession",AnagraficoComponentSession.class);
		}
		/**
		 * Effettua una operazione di ricerca per un attributo di un modello.
		 * @param actionContext contesto dell'azione in corso
		 * @param clauses Albero di clausole da utilizzare per la ricerca
		 * @param bulk prototipo del modello di cui si effettua la ricerca
		 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
		 * 			controller che ha scatenato la ricerca)
		 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
		 */
		public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
			try {
				return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
			} catch(Exception e) {
				throw new it.cnr.jada.action.BusinessProcessException(e);
			}
		}	
}
