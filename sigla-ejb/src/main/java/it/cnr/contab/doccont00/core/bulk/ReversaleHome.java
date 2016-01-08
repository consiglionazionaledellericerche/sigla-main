package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;
import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.EJBException;

public abstract class ReversaleHome extends BulkHome {
public ReversaleHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
public ReversaleHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un ReversaleHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public ReversaleHome(java.sql.Connection conn) {
	super(ReversaleBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un ReversaleHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public ReversaleHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(ReversaleBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param reversale	
 * @return 
 * @throws PersistencyException	
 */
public Timestamp findDataUltimaReversalePerCds( ReversaleBulk reversale ) throws PersistencyException
{
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT TRUNC(MAX(DT_EMISSIONE)) " +			
			"FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"REVERSALE WHERE " +
			"ESERCIZIO = ? AND CD_CDS = ?",true,this.getClass());
		try
		{
			ps.setObject( 1, reversale.getEsercizio() );
			ps.setString( 2, reversale.getCds().getCd_unita_organizzativa() );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if(rs.next())
					return rs.getTimestamp(1);
				else
					return null;
			}
			catch( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch( SQLException e )
		{
			throw new PersistencyException( e );
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
	}
	catch ( SQLException e )
	{
			throw new PersistencyException( e );
	}
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param reversale	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public abstract Collection findReversale_riga( it.cnr.jada.UserContext userContext,ReversaleBulk reversale ) throws PersistencyException, IntrospectionException;
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param reversale	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public abstract Reversale_terzoBulk findReversale_terzo(it.cnr.jada.UserContext userContext, ReversaleBulk reversale ) throws PersistencyException, IntrospectionException;
/**
 * Metodo per cercare i sospesi associati alla reversale.
 *
 * @param reversale <code>ReversaleBulk</code> la reversale
 *
 * @return result i sospesi associati alla reversale
 *
 */
public Collection findSospeso_det_etr( it.cnr.jada.UserContext userContext,ReversaleBulk reversale ) throws PersistencyException, IntrospectionException
{
	PersistentHome home = getHomeCache().getHome( Sospeso_det_etrBulk.class );
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause( "AND", "esercizio", sql.EQUALS, reversale.getEsercizio());	
	sql.addClause( "AND", "cd_cds", sql.EQUALS, reversale.getCd_cds());
	sql.addClause( "AND", "pg_reversale", sql.EQUALS, reversale.getPg_reversale());
	sql.addClause( "AND", "ti_sospeso_riscontro", sql.EQUALS, SospesoBulk.TI_SOSPESO);
//	sql.addClause( "AND", "stato", sql.EQUALS, Sospeso_det_etrBulk.STATO_DEFAULT);	
	Collection result = home.fetchAll( sql);
	getHomeCache().fetchAll(userContext);
	return result;
}	

	/**
	 * Imposta il pg_reversale di un oggetto <code>ReversaleBulk</code>.
	 *
	 * @param reversale <code>OggettoBulkBulk</code>
	 *
	 * @exception PersistencyException
	 */

public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException 
{
	try
	{
		ReversaleBulk reversale = (ReversaleBulk) bulk;
		Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache().getHome( Numerazione_doc_contBulk.class );
		
		Long pg;
		if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext,reversale.getEsercizio()).getFl_tesoreria_unica().booleanValue()){
			Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk)(getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0));
			pg = numHome.getNextPg(userContext, reversale.getEsercizio(), uoEnte.getCd_cds(), reversale.getCd_tipo_documento_cont(), reversale.getUser());
		}
		else{
			pg = numHome.getNextPg(userContext, reversale.getEsercizio(), reversale.getCd_cds(), reversale.getCd_tipo_documento_cont(), reversale.getUser());
		}
				reversale.setPg_reversale( pg );
	}catch ( IntrospectionException e ){
		throw new PersistencyException( e );
	
	} catch (RemoteException e) {
		throw new ComponentException( e );
	} catch (EJBException e) {
		throw new ComponentException( e );
	}
	catch ( ApplicationException e ){
		throw new ComponentException( e );
	}
}
/**
 * Carica la reversale <reversale> con tutti gli oggetti complessi
 *
 * @param reversale	
 * @return 
 * @throws PersistencyException	
 */
public abstract ReversaleBulk loadReversale(it.cnr.jada.UserContext userContext,String cdCds, Integer esercizio, Long pgReversale) throws PersistencyException, IntrospectionException;
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param bulk	
 * @return 
 * @throws PersistencyException	
 */
public java.util.Hashtable loadTipoDocumentoKeys( ReversaleBulk bulk ) throws PersistencyException
{
	SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "E" );
	List result = getHomeCache().getHome( Tipo_documento_ammBulk.class ).fetchAll( sql );
	Hashtable ht = new Hashtable();
	Tipo_documento_ammBulk tipo;
	for (Iterator i = result.iterator(); i.hasNext(); )
	{
		tipo = (Tipo_documento_ammBulk) i.next();
		ht.put( tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
	}	
	return ht;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param bulk	
 * @return 
 * @throws PersistencyException	
 */
public java.util.Hashtable loadTipoDocumentoPerRicercaKeys( ReversaleBulk bulk ) throws PersistencyException
{
	SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
//	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "E" );
	sql.openParenthesis( "AND");
	sql.addSQLClause( "AND", "fl_manrev_utente", sql.EQUALS, "R" );
	sql.addSQLClause( "OR", "fl_manrev_utente", sql.EQUALS, "E" );
	sql.closeParenthesis();	
	List result = getHomeCache().getHome( Tipo_documento_ammBulk.class ).fetchAll( sql );
	Hashtable ht = new Hashtable();
	Tipo_documento_ammBulk tipo;
	for (Iterator i = result.iterator(); i.hasNext(); )
	{
		tipo = (Tipo_documento_ammBulk) i.next();
		ht.put( tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
	}	
	return ht;
}
}
