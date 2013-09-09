package it.cnr.contab.config00.bp;

import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class ConsContrattiBP extends it.cnr.jada.util.action.ConsultazioniBP {
public ConsContrattiBP() {
	super();
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try { 

		String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

		CompoundFindClause clauses = new CompoundFindClause(); 
		clauses.addClause("AND", "cds", SQLBuilder.EQUALS, cds); 
		//clauses.addClause("AND", "esercizioContratto", SQLBuilder.EQUALS, esercizio);
		setBaseclause(clauses);
		super.init(config,context);
				
	}catch(Throwable e) { 
		throw new BusinessProcessException(e);
	}
}		
}
