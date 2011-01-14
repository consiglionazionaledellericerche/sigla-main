package it.cnr.contab.gestiva00.comp;
/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Enumeration;


import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LiquidIvaInterfComponent extends CRUDComponent {
	public boolean contaRiga(UserContext userContext, Liquid_iva_interfBulk liquid_iva) throws ComponentException{
	  try
	  {
			Liquid_iva_interfHome testataHome = (Liquid_iva_interfHome)getHome(userContext, Liquid_iva_interfBulk.class);
			SQLBuilder sql = testataHome.createSQLBuilder();
		    sql.addSQLClause("AND","ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause("AND","CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));		    
			sql.addSQLClause("AND","DT_INIZIO", sql.EQUALS, liquid_iva.getDt_inizio());	
			try {
				return sql.executeExistsQuery(testataHome.getConnection());
			} catch (java.sql.SQLException e) {
				throw handleSQLException(e);
			}
	  }
	  catch ( Exception e )
	  {
		throw handleException( e );
	  }
	}
	public void inserisciRighe(UserContext userContext, Liquid_iva_interfBulk liquid_iva) throws ComponentException{
		it.cnr.contab.config00.sto.bulk.CdsHome cdsHome = (it.cnr.contab.config00.sto.bulk.CdsHome)getHome(userContext, CdsBulk.class);
		
		java.util.List listaUoCds;
		try {
			listaUoCds = cdsHome.findUoCds(userContext,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			for (java.util.ListIterator i = listaUoCds.listIterator();i.hasNext();) {
				Unita_organizzativaBulk uo = (Unita_organizzativaBulk)i.next();
				for (Enumeration j = liquid_iva.getTIPI_LIQ().keys();j.hasMoreElements();) {					
					Liquid_iva_interfBulk liquid = new Liquid_iva_interfBulk(new Integer(0),it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext),it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),uo.getCd_unita_organizzativa(),liquid_iva.getDt_fine(),liquid_iva.getDt_inizio(),(String)j.nextElement());
					liquid.setFl_gia_eleborata(new Boolean(false));	
					liquid.setIva_credito(new BigDecimal("0"));
					liquid.setIva_debito(new BigDecimal("0"));	
					liquid.setNote(new String("Per liquidazione iva "+liquid_iva.getMese()));
					liquid.setToBeCreated();
					super.creaConBulk(userContext,liquid);
				}					
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} 
	}	
	public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
	   SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
	   sql.addOrderBy("DT_INIZIO, DT_FINE, CD_UNITA_ORGANIZZATIVA, TI_LIQUIDAZIONE");
	   //sql.addSQLClause("AND","ESERCIZIO",sql.LESS_EQUALS,CNRUserContext.getEsercizio(userContext));
	   return sql;
	}

}