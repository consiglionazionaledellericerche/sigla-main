package it.cnr.contab.prevent01.consultazioni.bp;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;


public class ConsPDGPAreaSpeBP extends ConsPDGPAreaBP {
	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			setFindclause(compoundfindclause);
			CompoundFindClause clause = new CompoundFindClause(getBaseclause(), compoundfindclause);
			return createPdgpAreaComponentSession().findConsultazioneSpe(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),compoundfindclause);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			setIterator(context,createPdgpAreaComponentSession().findConsultazioneSpe(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),null));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
}
