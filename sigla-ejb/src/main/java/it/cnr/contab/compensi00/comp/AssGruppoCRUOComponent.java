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

package it.cnr.contab.compensi00.comp;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_uoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_uoHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;


public class AssGruppoCRUOComponent extends it.cnr.jada.comp.CRUDComponent{

	public AssGruppoCRUOComponent() {
		super();
	}
	public SQLBuilder selectGruppoByClause(UserContext userContext, Gruppo_cr_uoBulk ass, Gruppo_crBulk elem, CompoundFindClause clauses) 
	throws ComponentException
{		
	SQLBuilder sql = getHome(userContext, Gruppo_crBulk.class).createSQLBuilder();
	sql.addClause( clauses );
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	return sql;		
}
	
	public SQLBuilder selectUnita_organizzativaByClause(UserContext userContext, Gruppo_cr_uoBulk ass, Unita_organizzativaBulk elem, CompoundFindClause clauses) 
	throws ComponentException
{		
	Unita_organizzativaHome home=(Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class,"V_UNITA_ORGANIZZATIVA_VALIDA");
	SQLBuilder sql = home.createSQLBuilder();	
	sql.addClause( clauses );
	sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND", "FL_CDS", SQLBuilder.EQUALS,"N");
	sql.addSQLClause("AND", "CD_TIPO_UNITA", SQLBuilder.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_SAC);
		
	return sql;		
}
}