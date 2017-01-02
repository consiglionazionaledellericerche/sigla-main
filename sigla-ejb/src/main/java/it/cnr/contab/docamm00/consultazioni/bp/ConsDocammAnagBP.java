/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.docamm00.consultazioni.bp;

import it.cnr.contab.docamm00.consultazioni.bulk.VDocAmmAnagManrevBulk;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;
/**
 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsDocammAnagBP extends ConsultazioniBP {


	public ConsDocammAnagBP(String s) {
		super(s);
	}

	public ConsDocammAnagBP() {
		super();
	}

	public void openIterator(it.cnr.jada.action.ActionContext context, OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			CompoundFindClause compoundfindclause = new CompoundFindClause();
			compoundfindclause.addClause(FindClause.AND, "cdAnag", SQLBuilder.EQUALS,
					((VDocAmmAnagManrevBulk)model).getCdAnag());
			
			if(this.getBaseclause()==null)
				this.setBaseclause(compoundfindclause);
			it.cnr.jada.util.RemoteIterator ri =
				it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
				(context, createComponentSession().cerca(context.getUserContext(),CompoundFindClause.and(getBaseclause(),getFindclause()),model));
			this.setIterator(context,ri);
			
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

}
