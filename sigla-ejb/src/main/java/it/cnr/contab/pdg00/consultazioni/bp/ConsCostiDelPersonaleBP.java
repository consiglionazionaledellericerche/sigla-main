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


import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_stampa_costi_personaleBulk;
import it.cnr.contab.pdg00.consultazioni.bulk.Param_cons_costi_personaleBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsCostiDelPersonaleBP extends ConsultazioniBP
{
	
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		
			CompoundFindClause clauses = new CompoundFindClause();
			V_stampa_costi_personaleBulk bulk = new V_stampa_costi_personaleBulk();	
			
					Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
					String uo_scrivania = CNRUserContext.getCd_unita_organizzativa(context.getUserContext());
					clauses.addClause("AND","esercizio",SQLBuilder.EQUALS, esercizio);
					clauses.addClause("AND","mese",SQLBuilder.EQUALS, bulk.MESE_0);
					if(!isUoEnte(context))
						clauses.addClause("AND", "uo", SQLBuilder.EQUALS, uo_scrivania);
					if (it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(context)!=null)
						clauses.addClause("AND", "cd_dipartimento", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bulk.CNRUserInfo.getDipartimento(context).getCd_dipartimento());
	
					setBaseclause(clauses);
					super.init(config,context);
				}
	
	
	public boolean isUoEnte(ActionContext context){	
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return true;	
		return false; 
	}	
 
}
