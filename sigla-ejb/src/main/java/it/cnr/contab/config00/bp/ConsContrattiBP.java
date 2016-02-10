package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
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
		if(!isUoEnte(context))	 {				
		  clauses.addClause("AND", "cds", SQLBuilder.EQUALS, cds);
	     }
		else throw new ApplicationException("Consultazione non disponibile da UO ente!");
		//clauses.addClause("AND", "esercizioContratto", SQLBuilder.EQUALS, esercizio);
		setBaseclause(clauses);
		super.init(config,context);
				
	}catch(Throwable e) { 
		throw new BusinessProcessException(e);
	}
}
public boolean isUoEnte(ActionContext context){	
	Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
		return true;	
	return false; 
}
}
