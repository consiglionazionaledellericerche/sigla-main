/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.config00.sto.bulk;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import it.cnr.contab.config00.geco.bulk.Geco_dipartimentoBulk;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimentoHome;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimentoIBulk;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimento_rstlBulk;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimento_rstlHome;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimento_sacBulk;
import it.cnr.contab.config00.geco.bulk.Geco_dipartimento_sacHome;
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
			verificaDipartimenti(userContext,dipartimento, Geco_dipartimentoBulk.class);
			verificaDipartimenti(userContext,dipartimento, Geco_dipartimento_sacBulk.class);
			verificaDipartimenti(userContext,dipartimento, Geco_dipartimento_rstlBulk.class);
		} catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
		
	}
	private void verificaDipartimenti(UserContext userContext, DipartimentoBulk dipartimento, Class<? extends OggettoBulk> bulkClass) throws PersistencyException, ComponentException, EJBException {
		List<Geco_dipartimentoIBulk> dipartimentiGeco = Utility.createProgettoGecoComponentSession().cercaDipartimentiGeco(userContext, dipartimento, bulkClass);
		for (Iterator<Geco_dipartimentoIBulk> iterator = dipartimentiGeco.iterator(); iterator.hasNext();) {
			Geco_dipartimentoIBulk geco_dipartimento = iterator.next();
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
	
}