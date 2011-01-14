/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.docamm00.consultazioni.bp;

import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.ConsultazioniBP;
/**
 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsEstrazione_glaBP extends ConsultazioniBP {


	public ConsEstrazione_glaBP(String s) {
		super(s);
	}

	public ConsEstrazione_glaBP() {
		super();
	}

	public void openIterator(it.cnr.jada.action.ActionContext context, OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
		try	{	

			it.cnr.jada.util.RemoteIterator ri =
				it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
				(context, createComponentSession().cerca(context.getUserContext(),CompoundFindClause.and(getBaseclause(),getFindclause()),model));
			this.setIterator(context,ri);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	public boolean isRemoveFilterButtonHidden() 
	{
			return true;
	}
	
	public boolean isFilterButtonHidden() 
	{
			return true;
	}
}
