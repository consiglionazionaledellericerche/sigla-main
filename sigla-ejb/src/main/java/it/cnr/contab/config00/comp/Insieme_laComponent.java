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

package it.cnr.contab.config00.comp;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;
/**
 * Insert the type's description here.
 * Creation date: (19/02/2002 11:51:01)
 * @author: Simonetta Costa
 */
public class Insieme_laComponent extends it.cnr.jada.comp.CRUDComponent implements IInsieme_laMgr {
/**
 * Insieme_laComponent constructor comment.
 */
public Insieme_laComponent() {
	super();
}
/**
  *  Default
  *	   PreCondition:
  *		 Viene richiesta una ricerca sugli insiemi linea attività.
  *    PostCondition:
  *		 Viene creata una query sulla tabella dei INSIEME_LA con le clausole 
  * 	 specificate dall'utente più la clausola che il CDR sia uguale a quello
  *		 dell'utente.
 */
protected Query select(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
		it.cnr.jada.persistency.sql.SQLBuilder sql = (SQLBuilder)super.select( userContext, clauses, bulk );

		it.cnr.contab.utenze00.bulk.UtenteBulk utente = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext,it.cnr.contab.utenze00.bulk.UtenteBulk.class,null,"none").findByPrimaryKey(new it.cnr.contab.utenze00.bulk.UtenteBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext)));
		
		sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext));

		return sql;
}
/**
  *  Default
  *	   PreCondition:
  *		 Viene richiesto l'elenco dei cdr assegnabili ad un insieme linea attività
  *    PostCondition:
  *		 Viene creata una query sulla tabella dei CDR con le seguenti clausole
  *		 - Il cdr della linea di attività è gestibile dal cdr dell'utente (secondo
  *			le regole definite dalla query V_PDG_CDR_GESTIBILI)
 */
public SQLBuilder selectCentro_responsabilitaByClause(UserContext userContext, Insieme_laBulk insieme_la, CdrBulk cdr, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = getHome(userContext, CdrBulk.class,"V_PDG_CDR_GESTIBILI").createSQLBuilder();
		sql.addClause( clauses );
		
		it.cnr.contab.utenze00.bulk.UtenteBulk utente = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext,it.cnr.contab.utenze00.bulk.UtenteBulk.class,null,"none").findByPrimaryKey(new it.cnr.contab.utenze00.bulk.UtenteBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext)));
		
		sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.CD_CDR_ROOT",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext));
		sql.addSQLClause("AND","V_PDG_CDR_GESTIBILI.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

		return sql;
}
}
