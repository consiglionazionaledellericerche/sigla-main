package it.cnr.contab.inventario00.bp;
/**
  *  Questa classe gestisce le operazioni di business relative all'associazione di 
  *	una Fattura Passiva a dei beni esistenti nel DB.
**/

import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;
 
public class ConAssBeniFattureBP extends ConsultazioniBP{	
	
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {

			String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
			Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());

			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause("AND", "cd_cds_fatt_pass", SQLBuilder.EQUALS, cds);
			clauses.addClause("AND", "esercizio_fatt_pass", SQLBuilder.EQUALS, esercizio);
			setBaseclause(clauses);

			super.init(config,context);
					
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		
}
