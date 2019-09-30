/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.pdg00.consultazioni.bp;


import java.sql.SQLException;

import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.consultazioni.bulk.Param_cons_costi_personaleBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class ConsCostiDelPersonaleMensileDettBP extends ConsultazioniBP
{
	
	
	
	
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
			CompoundFindClause clauses = new CompoundFindClause();
			if(context.getBusinessProcess().getName().equals("ConsCostiDelPersonaleMensileBP")){
				ConsCostiDelPersonaleMensileBP bp = (ConsCostiDelPersonaleMensileBP)context.getBusinessProcess();
				if(bp.getModel()!=null && bp.getModel() instanceof Param_cons_costi_personaleBulk){
					Param_cons_costi_personaleBulk parametri = (Param_cons_costi_personaleBulk)bp.getModel();
					
					Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
					String cds = CNRUserContext.getCd_cds(context.getUserContext());
					clauses.addClause("AND","esercizio",SQLBuilder.EQUALS, esercizio);
//					clauses.addClause("AND", "cds", SQLBuilder.EQUALS, cds);
					clauses.addClause("AND", "mese", SQLBuilder.EQUALS, parametri.getMese());
					if (it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(context)!=null)
						clauses.addClause("AND", "cd_dipartimento", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(context).getCd_dipartimento());
					else
						clauses.addClause("AND", "uo", SQLBuilder.EQUALS, parametri.getCd_uo());
					if (parametri.getCd_commessa()!=null)
					clauses.addClause("AND", "cd_commessa", SQLBuilder.EQUALS, parametri.getCd_commessa());
					if (parametri.getCd_modulo()!=null)
					clauses.addClause("AND", "cd_modulo", SQLBuilder.EQUALS, parametri.getCd_modulo());
					if (parametri.getId_matricola()!=null)
					clauses.addClause("AND", "id_matricola", SQLBuilder.EQUALS, parametri.getId_matricola());
					
					
					setModel(context,parametri);	
					setBaseclause(clauses);
					super.init(config,context);
				}
			}
		
	}
	
	public java.lang.String getSearchResultColumnSet() 
	{
		return "ConsMensile";
	}
	
	
}

