package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.intcass.bulk.V_cons_sospesiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;


/**
 * @author fgiardina
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ConsSospesiEntrateBP extends ConsSospesiBP {
	
	public RemoteIterator search(ActionContext context, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			setFindclause(compoundfindclause);
			return createConsSospesiEntSpeComponentSession().findConsSospesiEntrata(context.getUserContext(), getPathConsultazione(), getLivelloConsultazione(), getBaseclause(), compoundfindclause);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	
	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			setIterator(context,createConsSospesiEntSpeComponentSession().findConsSospesiEntrata(context.getUserContext(),getPathConsultazione(),getLivelloConsultazione(),getBaseclause(),getFindclause()));
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
}