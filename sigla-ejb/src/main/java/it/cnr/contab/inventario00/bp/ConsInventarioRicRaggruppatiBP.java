package it.cnr.contab.inventario00.bp;

import it.cnr.contab.inventario00.consultazioni.bulk.VInventarioRicognizioneBulk;
import it.cnr.contab.inventario00.ejb.ConsRegistroInventarioComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsInventarioRicRaggruppatiBP extends ConsultazioniBP
{
	public ConsRegistroInventarioComponentSession createConsRegistroInventarioComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (ConsRegistroInventarioComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_ConsRegistroInventarioComponentSession", ConsRegistroInventarioComponentSession.class);
	}
	
	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			this.setFindclause(compoundfindclause);
			VInventarioRicognizioneBulk inv ;
		    if (this.getParent() instanceof ConsInventarioRicBP){ 
		    	ConsInventarioRicBP bpPar=(ConsInventarioRicBP)this.getParent();
		    	 inv = (VInventarioRicognizioneBulk)bpPar.getModel();
		    }else
		    	 inv = (VInventarioRicognizioneBulk)getModel();
			return createConsRegistroInventarioComponentSession().findConsultazioneRicognizione(context.getUserContext(),getBaseclause(),getFindclause(),inv);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
}
