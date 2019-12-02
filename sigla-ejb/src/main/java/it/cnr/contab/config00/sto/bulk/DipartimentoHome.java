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

/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.config00.sto.bulk;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.ejb.EJBException;

import it.cnr.contab.config00.geco.bulk.Geco_dipartimentiBulk;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimentiHome;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimentiIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progettoIBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBroker;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
public class DipartimentoHome extends BulkHome {
	public DipartimentoHome(java.sql.Connection conn) {
		super(DipartimentoBulk.class, conn);
	}
	public DipartimentoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(DipartimentoBulk.class, conn, persistentCache);
	}
	@Override
	public Persistent findByPrimaryKey(UserContext userContext, Object persistent) throws PersistencyException {
    	return findByPrimaryKey(userContext,(Persistent)persistent);
	}
	@Override
	public Persistent findByPrimaryKey(UserContext userContext, Persistent persistent) throws PersistencyException {
		return super.findByPrimaryKey(userContext, persistent);
	}
	public DipartimentoBulk findByIdDipartimento(Integer id_dipartimento) throws PersistencyException{
		DipartimentoBulk dip = null;
		SQLBuilder sql = createSQLBuilder();
		sql.addClause("AND","id_dipartimento",SQLBuilder.EQUALS,id_dipartimento);
		SQLBroker broker = createBroker(sql);
		if (broker.next())
			dip = (DipartimentoBulk)fetch(broker);
		broker.close();
		return dip;
	}
	private void handleExceptionMail(UserContext userContext, Exception e){
	}
	public void aggiornaDipartimenti(UserContext userContext,DipartimentoBulk dipartimento){
		try {
			verificaDipartimenti(userContext,dipartimento, Geco_dipartimentiBulk.class);
		} catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		
	}
	private void verificaDipartimenti(UserContext userContext, DipartimentoBulk dipartimento, Class<? extends OggettoBulk> bulkClass) throws PersistencyException, ComponentException, EJBException {
		List<Geco_dipartimentiIBulk> dipartimentiGeco = Utility.createProgettoGecoComponentSession().cercaDipartimentiGeco(userContext, dipartimento, bulkClass);
		for (Iterator<Geco_dipartimentiIBulk> iterator = dipartimentiGeco.iterator(); iterator.hasNext();) {
			Geco_dipartimentiIBulk geco_dipartimento = iterator.next();
			DipartimentoHome dipartimento_home =  (DipartimentoHome)getHomeCache().getHome(DipartimentoBulk.class);
			DipartimentoBulk dipartimento_new = (DipartimentoBulk)dipartimento_home.findByPrimaryKey(new DipartimentoBulk(geco_dipartimento.getCod_dip()));
			if (dipartimento_new != null){
				geco_dipartimento.aggiornaDipartimentoSIP(dipartimento_new);				
				if (dipartimento_new.isToBeUpdated()){
					dipartimento_new.setUser(CNRUserContext.getUser(userContext));
					update(dipartimento_new, userContext);
				}
			}else{
				dipartimento_new = new DipartimentoBulk(geco_dipartimento.getCod_dip());
				dipartimento_new.setId_dipartimento(geco_dipartimento.getId_dip().intValue());
				dipartimento_new.setDs_dipartimento(geco_dipartimento.getDescrizione());
				dipartimento_new.setDt_istituzione(geco_dipartimento.getData_istituzione());
				dipartimento_new.setUser(CNRUserContext.getUser(userContext));
				dipartimento_new.setToBeCreated();
				insert(dipartimento_new, userContext);
			}
		}
	} 
	
	@Override
	public SQLBuilder selectByClause(CompoundFindClause compoundfindclause)
			throws PersistencyException {
		SQLBuilder sql = createSQLBuilder();
		
		//java.sql.Timestamp firstDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(usercontext));
		//java.sql.Timestamp lastDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(usercontext));
	 	sql.addClause("AND", "dt_istituzione", sql.LESS, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.openParenthesis("AND");
		sql.addClause("AND", "dt_soppressione", sql.GREATER_EQUALS,  it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.addClause("OR","dt_soppressione",sql.ISNULL,null);
		sql.closeParenthesis();
		Optional.ofNullable(compoundfindclause).ifPresent(cfc->sql.addClause(cfc));
		return sql;
	}
	public SQLBuilder selectByClause(UserContext uc,CompoundFindClause compoundfindclause)
			throws PersistencyException {
		SQLBuilder sql = createSQLBuilder(); 
		java.sql.Timestamp lastDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(uc));
	 	sql.addClause("AND", "dt_istituzione", sql.LESS, lastDayOfYear);
		sql.openParenthesis("AND");
		sql.addClause("AND", "dt_soppressione", sql.GREATER_EQUALS,  lastDayOfYear);
		sql.addClause("OR","dt_soppressione",sql.ISNULL,null);
		sql.closeParenthesis();
		return sql;
	}
}