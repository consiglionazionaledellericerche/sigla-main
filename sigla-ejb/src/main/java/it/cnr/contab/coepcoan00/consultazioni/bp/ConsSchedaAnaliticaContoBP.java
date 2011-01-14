/*
 * Created on Jul 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.coepcoan00.consultazioni.bp;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsSchedaAnaliticaContoBP extends ConsultazioniBP {

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {

			String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
			Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

			CompoundFindClause clauses = new CompoundFindClause();
			//clauses.addClause("AND", "cd_cds", SQLBuilder.NOT_EQUALS, "*");
			clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
			setBaseclause(clauses);

			super.init(config,context);
					
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		
}
