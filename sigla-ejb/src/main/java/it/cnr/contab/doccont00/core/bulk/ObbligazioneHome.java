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

package it.cnr.contab.doccont00.core.bulk;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.doccont00.ejb.NumTempDocContComponentSession;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class ObbligazioneHome extends BulkHome {
public ObbligazioneHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
public ObbligazioneHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un ObbligazioneHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public ObbligazioneHome(java.sql.Connection conn) {
	super(ObbligazioneBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un ObbligazioneHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public ObbligazioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(ObbligazioneBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazioneTemporanea	
 * @param pg	
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public void confirmObbligazioneTemporanea( 
		UserContext userContext,
		ObbligazioneBulk obbligazioneTemporanea,
	Long pg)
	throws IntrospectionException,PersistencyException {

	confirmObbligazioneTemporanea(userContext, obbligazioneTemporanea, pg, true);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazioneTemporanea	
 * @param pg	
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public void confirmObbligazioneTemporanea( 
		UserContext userContext,
		ObbligazioneBulk obbligazioneTemporanea,
	Long pg,
	boolean deleteTemp)
	throws IntrospectionException,PersistencyException {

	if (pg == null)
		throw new PersistencyException("Impossibile ottenere un progressivo definitivo per l'impegno inserita!");

	LoggableStatement ps = null;
	java.io.StringWriter sql = new java.io.StringWriter();
	java.io.PrintWriter pw = new java.io.PrintWriter(sql);
	String condition = " WHERE CD_CDS = ? AND ESERCIZIO = ? AND ESERCIZIO_ORIGINALE = ? AND PG_OBBLIGAZIONE = ?";
	String condition_s = " WHERE CD_CDS = ? AND ESERCIZIO = ? AND ESERCIZIO_ORIGINALE = ? AND PG_OBBLIGAZIONE = ? AND PG_STORICO_ = ?";
	try {
		pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema() + getColumnMap().getTableName() + " (");
		pw.write(getPersistenColumnNamesReplacingWith(this, null).toString());
		pw.write(") SELECT "); 
		pw.write(getPersistenColumnNamesReplacingWith(
							this,
							new String[][]{ {"PG_OBBLIGAZIONE", "?"} }).toString());
		pw.write(" FROM " + EJBCommonServices.getDefaultSchema() + getColumnMap().getTableName());
		pw.write(condition);
		pw.flush();
		
		ps = new LoggableStatement(getConnection(),sql.toString(),true,this.getClass());
		pw.close();
		ps.setLong(1, pg.longValue());
		ps.setString(2, obbligazioneTemporanea.getCd_cds());
		ps.setInt(3, obbligazioneTemporanea.getEsercizio().intValue());
		ps.setInt(4, obbligazioneTemporanea.getEsercizio_originale().intValue());
		ps.setLong(5, obbligazioneTemporanea.getPg_obbligazione().longValue());

		ps.execute();
	} catch (java.sql.SQLException e) {
		throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e,obbligazioneTemporanea);
	} finally {
			try{ps.close();}catch( java.sql.SQLException e ){};
	}
	/* Aggiorno lo storico se presente */
	try {
		sql = new java.io.StringWriter();
		pw = new java.io.PrintWriter(sql);
		pw.write("UPDATE " + EJBCommonServices.getDefaultSchema() +"OBBLIGAZIONE_S SET ");
		pw.write("PG_OBBLIGAZIONE = ? ");
		pw.write(condition_s);
		pw.flush();
		
		ps = new LoggableStatement(getConnection(),sql.toString(),true,this.getClass());
		pw.close();
		ps.setLong(1, pg.longValue());
		ps.setString(2, obbligazioneTemporanea.getCd_cds());
		ps.setInt(3, obbligazioneTemporanea.getEsercizio().intValue());
		ps.setInt(4, obbligazioneTemporanea.getEsercizio_originale().intValue());
		ps.setLong(5, obbligazioneTemporanea.getPg_obbligazione().longValue());
		ps.setLong(6, obbligazioneTemporanea.getPg_ver_rec().longValue());
		ps.execute();			
	} catch (java.sql.SQLException e) {
		throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e,obbligazioneTemporanea);
	} finally {
			try{ps.close();}catch( java.sql.SQLException e ){};
	}
	
	try {
		sql = new java.io.StringWriter();
		pw = new java.io.PrintWriter(sql);
		Obbligazione_scadenzarioHome scadHome = (Obbligazione_scadenzarioHome)getHomeCache().getHome(Obbligazione_scadenzarioBulk.class);
		pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema() + scadHome.getColumnMap().getTableName() + " (");
		pw.write(getPersistenColumnNamesReplacingWith(scadHome, null).toString());
		pw.write(") SELECT "); 
		pw.write(getPersistenColumnNamesReplacingWith(
							scadHome,
							new String[][]{ {"PG_OBBLIGAZIONE", "?"} }).toString());
		pw.write(" FROM " + EJBCommonServices.getDefaultSchema() + scadHome.getColumnMap().getTableName());
		pw.write(condition);
		pw.flush();
		ps = new LoggableStatement(getConnection(),sql.toString(),true,this.getClass());
		pw.close();
		ps.setLong(1, pg.longValue());
		ps.setString(2, obbligazioneTemporanea.getCd_cds());
		ps.setInt(3, obbligazioneTemporanea.getEsercizio().intValue());
		ps.setInt(4, obbligazioneTemporanea.getEsercizio_originale().intValue());
		ps.setLong(5, obbligazioneTemporanea.getPg_obbligazione().longValue());

		ps.execute();
	} catch (java.sql.SQLException e) {
		throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e,obbligazioneTemporanea);
	} finally {
			try{ps.close();}catch( java.sql.SQLException e ){};
	}
	/* Aggiorno lo storico se presente */
	try {
		sql = new java.io.StringWriter();
		pw = new java.io.PrintWriter(sql);
		pw.write("UPDATE " + EJBCommonServices.getDefaultSchema() +"OBBLIGAZIONE_SCADENZARIO_S SET ");
		pw.write("PG_OBBLIGAZIONE = ? ");
		pw.write(condition_s);
		pw.flush();
		
		ps = new LoggableStatement(getConnection(),sql.toString(),true,this.getClass());
		pw.close();
		ps.setLong(1, pg.longValue());
		ps.setString(2, obbligazioneTemporanea.getCd_cds());
		ps.setInt(3, obbligazioneTemporanea.getEsercizio().intValue());
		ps.setInt(4, obbligazioneTemporanea.getEsercizio_originale().intValue());
		ps.setLong(5, obbligazioneTemporanea.getPg_obbligazione().longValue());
		ps.setLong(6, obbligazioneTemporanea.getPg_ver_rec().longValue());
		ps.execute();
	} catch (java.sql.SQLException e) {
		throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e,obbligazioneTemporanea);
	} finally {
			try{ps.close();}catch( java.sql.SQLException e ){};
	}

	try {
		sql = new java.io.StringWriter();
		pw = new java.io.PrintWriter(sql);
		Obbligazione_scad_voceHome scadVoceHome = (Obbligazione_scad_voceHome)getHomeCache().getHome(Obbligazione_scad_voceBulk.class);
		pw.write("INSERT INTO " + EJBCommonServices.getDefaultSchema() + scadVoceHome.getColumnMap().getTableName() + " (");
		pw.write(getPersistenColumnNamesReplacingWith(scadVoceHome, null).toString());
		pw.write(") SELECT "); 
		pw.write(getPersistenColumnNamesReplacingWith(
							scadVoceHome,
							new String[][]{ {"PG_OBBLIGAZIONE", "?"} }).toString());
		pw.write(" FROM " + EJBCommonServices.getDefaultSchema() + scadVoceHome.getColumnMap().getTableName());
		pw.write(condition);
		pw.flush();
		ps = new LoggableStatement(getConnection(),sql.toString(),true,this.getClass());
		pw.close();
		ps.setLong(1, pg.longValue());
		ps.setString(2, obbligazioneTemporanea.getCd_cds());
		ps.setInt(3, obbligazioneTemporanea.getEsercizio().intValue());
		ps.setInt(4, obbligazioneTemporanea.getEsercizio_originale().intValue());
		ps.setLong(5, obbligazioneTemporanea.getPg_obbligazione().longValue());
		ps.execute();
	} catch (java.sql.SQLException e) {
		throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e,obbligazioneTemporanea);
	} finally {
			try{ps.close();}catch( java.sql.SQLException e ){};
	}
	/* Aggiorno lo storico se presente */
	try {
		sql = new java.io.StringWriter();
		pw = new java.io.PrintWriter(sql);
		pw.write("UPDATE " + EJBCommonServices.getDefaultSchema() +"OBBLIGAZIONE_SCAD_VOCE_S SET ");
		pw.write("PG_OBBLIGAZIONE = ? ");
		pw.write(condition_s);
		pw.flush();
		
		ps =new LoggableStatement( getConnection(),sql.toString(),true,this.getClass());
		pw.close();
		ps.setLong(1, pg.longValue());
		ps.setString(2, obbligazioneTemporanea.getCd_cds());
		ps.setInt(3, obbligazioneTemporanea.getEsercizio().intValue());
		ps.setInt(4, obbligazioneTemporanea.getEsercizio_originale().intValue());
		ps.setLong(5, obbligazioneTemporanea.getPg_obbligazione().longValue());
		ps.setLong(6, obbligazioneTemporanea.getPg_ver_rec().longValue());
		ps.execute();
	} catch (java.sql.SQLException e) {
		throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e,obbligazioneTemporanea);		
	} finally {
			try{ps.close();}catch( java.sql.SQLException e ){};
	}
	if (deleteTemp) {
		delete(obbligazioneTemporanea, userContext);
		obbligazioneTemporanea.setPg_obbligazione(pg);
	}
}
/**
 * Metodo per cercare i capitoli di spesa del Cds dell'obbligazione.
 *
 * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione
 *
 * @return i capitoli di spesa definiti per il Cds dell'obbligazione
 */
public java.util.List findCapitoliDiSpesaCds( ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{
	PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
	Parametri_cnrBulk parCNR = (Parametri_cnrBulk)parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(obbligazione.getEsercizio()));
	
	if (parCNR.getFl_nuovo_pdg())
		return Arrays.asList(obbligazione.getElemento_voce());
	
	PersistentHome evHome = getHomeCache().getHome(Voce_fBulk.class);
	SQLBuilder sql = evHome.createSQLBuilder();
	sql.addClause("AND","ti_appartenenza",sql.EQUALS, Elemento_voceHome.APPARTENENZA_CDS );
	sql.addClause("AND","ti_gestione",sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
	if (obbligazione.getCds().getCd_tipo_unita() == null)
		obbligazione.setCds((CdsBulk)getHomeCache().getHome(obbligazione.getCds()).findByPrimaryKey(obbligazione.getCds()));
	if ( obbligazione.getCds().getCd_tipo_unita().equalsIgnoreCase( it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC))
		sql.addClause("AND","ti_voce",sql.EQUALS, Elemento_voceHome.TIPO_ARTICOLO );
	else
		sql.addClause("AND","ti_voce",sql.EQUALS, Elemento_voceHome.TIPO_CAPITOLO );	
	sql.addClause("AND","cd_parte",sql.EQUALS, Elemento_voceHome.PARTE_1 );	
	sql.addClause("AND","esercizio",sql.EQUALS, obbligazione.getEsercizio());
	if ( obbligazione.getCds().getCd_tipo_unita().equalsIgnoreCase( it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC))	
		sql.addClause("AND","cd_centro_responsabilita", sql.LIKE, obbligazione.getUnita_organizzativa().getCd_unita_organizzativa() + ".%" );
	else
		sql.addClause("AND","cd_unita_organizzativa", sql.EQUALS, obbligazione.getUnita_organizzativa().getCd_unita_organizzativa() );	
	sql.addClause("AND","cd_titolo_capitolo", sql.EQUALS, obbligazione.getElemento_voce().getCd_elemento_voce() );
	return evHome.fetchAll(sql);	
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param capitoliList	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findCdr( List capitoliList, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{
	try
	{
		int size = capitoliList.size() ;
		
		if ( size == 0 )
			return Collections.EMPTY_LIST;
			
		String statement = 
			"SELECT DISTINCT B.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_OBBLIGAZIONE_SPE A, " +
			EJBCommonServices.getDefaultSchema() +
			"CDR B " +
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			"A.CATEGORIA_DETTAGLIO = ?) AND " +
			"A.ESERCIZIO = ? AND " +
			"A.ESERCIZIO_RES = ? AND " +
			"A.TI_APPARTENENZA = ? AND " +
			"A.TI_GESTIONE = ? AND " +
			"A.CD_ELEMENTO_VOCE = ? AND " +
//			"B.ESERCIZIO = ? AND " +
			"B.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA AND " +									
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " ;

		statement = statement.concat( "( A.CD_FUNZIONE = ? ");
		for ( int t = 1 ; t < size; t++ )
			statement = statement.concat("OR A.CD_FUNZIONE = ? ");

		statement = statement.concat( " ) ");		
			
		LoggableStatement ps =new LoggableStatement( getConnection(), statement ,true,this.getClass());
		try
		{	
			IVoceBilancioBulk capitolo = (IVoceBilancioBulk) capitoliList.iterator().next();

			ps.setString( 1, Pdg_preventivo_detBulk.CAT_SINGOLO );
			ps.setString( 2, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
			ps.setObject( 3, capitolo.getEsercizio() );
			ps.setObject( 4, obbligazione.getEsercizio_originale() );
			ps.setString( 5, Elemento_voceHome.APPARTENENZA_CDS );
			ps.setString( 6, Elemento_voceHome.GESTIONE_SPESE );
			ps.setString( 7, capitolo.getCd_titolo_capitolo() );
			if (capitolo instanceof Voce_fBulk)
				ps.setString( 8, ((Voce_fBulk)capitolo).getCd_unita_organizzativa() );
			else
				ps.setString( 8, obbligazione.getCd_unita_organizzativa() );

			Iterator i = capitoliList.iterator();
			ps.setString( 9, ((IVoceBilancioBulk)i.next()).getCd_funzione() );
			for ( int j = 10; i.hasNext(); j++ )
				ps.setString( j, ((IVoceBilancioBulk)i.next()).getCd_funzione() );

			ResultSet rs = ps.executeQuery();
			try
			{
				PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class);
				return cdrHome.fetchAll( cdrHome.createBroker(ps, rs ));
			}
			catch( Exception e )
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
 * @param capitoliList	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findCdrPerSAC( List capitoliList, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{

	try
	{
		int sizeCapitoli = capitoliList.size() ;
		
		if ( sizeCapitoli == 0 )
			return Collections.EMPTY_LIST;
			
		PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
		Parametri_cnrBulk parCNR = (Parametri_cnrBulk)parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(obbligazione.getEsercizio()));
		  
		String statement = 
			"SELECT DISTINCT B.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_OBBLIGAZIONE_SPE A, " +
			EJBCommonServices.getDefaultSchema() +
			"CDR B " +
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			"A.CATEGORIA_DETTAGLIO = ? OR " +
			"(A.CATEGORIA_DETTAGLIO = ?  AND A.CD_CENTRO_RESPONSABILITA = ? )) AND " +
			"A.ESERCIZIO = ? AND " +
			"A.ESERCIZIO_RES = ? AND " +
			"A.TI_APPARTENENZA = ? AND " +
			"A.TI_GESTIONE = ? AND " +
			"A.CD_ELEMENTO_VOCE = ? AND " +
//			"B.ESERCIZIO = ? AND " +
			"B.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA AND " ;

		if (parCNR.getFl_nuovo_pdg()) {
			statement = statement.concat( "( (A.CD_FUNZIONE = ? AND B.CD_UNITA_ORGANIZZATIVA = ? ) " );
			for ( int t = 1 ; t < sizeCapitoli; t++ )
				statement = statement.concat( "OR (A.CD_FUNZIONE = ? AND B.CD_UNITA_ORGANIZZATIVA = ? ) " );
		} else {
		    statement = statement.concat( "( (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) " );
	    	 for ( int t = 1 ; t < sizeCapitoli; t++ )
			    statement = statement.concat("OR (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) ");
		}
        statement = statement.concat( " ) ");			  
			  
//		statement = statement.concat( "( (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) " );
//		for ( int t = 1 ; t < sizeCapitoli; t++ )
//			statement = statement.concat("OR (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) ");
//		statement = statement.concat( " ) ");					

		//java.sql.PreparedStatement ps = getConnection().prepareStatement( statement );
		LoggableStatement ps = null;
		Connection conn = getConnection();
		ps = new LoggableStatement(conn,statement,true,this.getClass());

		try
		{	
			IVoceBilancioBulk capitolo = (IVoceBilancioBulk) capitoliList.iterator().next();
			
			ps.setString( 1, Pdg_preventivo_detBulk.CAT_SINGOLO );
			ps.setString( 2, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
			ps.setString( 3, Pdg_modulo_spese_gestBulk.CAT_STIPENDI );
			ps.setString( 4, ((Configurazione_cnrHome)getHomeCache().getHome(Configurazione_cnrBulk.class)).getCdrPersonale(capitolo.getEsercizio()));
			ps.setObject( 5, capitolo.getEsercizio() );
			ps.setObject( 6, obbligazione.getEsercizio_originale() );
			ps.setString( 7, Elemento_voceHome.APPARTENENZA_CDS );
			ps.setString( 8, Elemento_voceHome.GESTIONE_SPESE );
			ps.setString( 9, capitolo.getCd_titolo_capitolo() );

			int j = 10;
			Iterator i = capitoliList.iterator();
			capitolo = (IVoceBilancioBulk) i.next();
			ps.setString( j++, capitolo.getCd_funzione() );
			if (capitolo instanceof Voce_fBulk)
				ps.setString( j++, ((Voce_fBulk)capitolo).getCd_centro_responsabilita() );
			else
				ps.setString( j++, obbligazione.getCd_unita_organizzativa() );

			for ( ; i.hasNext(); )
			{
				capitolo = (IVoceBilancioBulk) i.next();			
				ps.setString( j++, capitolo.getCd_funzione() );
				if (capitolo instanceof Voce_fBulk)
					ps.setString( j++, ((Voce_fBulk)capitolo).getCd_centro_responsabilita() );
				else
					ps.setString( j++, obbligazione.getCd_unita_organizzativa() );
			}	

			ResultSet rs = ps.executeQuery();
			try
			{
				PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class);
				return cdrHome.fetchAll( cdrHome.createBroker(ps, rs ));
			}
			catch( Exception e )
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
 * @param capitoliList	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findCdrPerSpesePerCostiAltrui( List capitoliList, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{

	try
	{
		int size = capitoliList.size() ;
		
		if ( size == 0 )
			return Collections.EMPTY_LIST;

		String statement =
			"SELECT DISTINCT B.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_OBBLIGAZIONE_SPE A, " +
			EJBCommonServices.getDefaultSchema() +
			"CDR B, " +
			EJBCommonServices.getDefaultSchema() +			
			"CDR C " +			
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			"A.CATEGORIA_DETTAGLIO = ?) AND " +
			"A.STATO = ? AND " +			
			"A.ESERCIZIO = ? AND " +			
			"A.ESERCIZIO_RES = ? AND " +			
			"A.TI_APPARTENENZA_CLGS = ? AND " +
			"A.TI_GESTIONE_CLGS = ? AND " +
			"A.CD_ELEMENTO_VOCE_CLGS = ? AND " +
			"B.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA AND " +
			"C.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA_CLGS AND " +												
			"C.CD_UNITA_ORGANIZZATIVA = ? AND " ; 

		statement = statement.concat( "( A.CD_FUNZIONE = ? ");
		for ( int t = 1 ; t < size; t++ )
			statement = statement.concat("OR A.CD_FUNZIONE = ? ");

		statement = statement.concat( " ) ");		
			
		LoggableStatement ps = new LoggableStatement(getConnection(),statement,true ,this.getClass());
		try
		{	
			Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();

			ps.setString( 1, Pdg_preventivo_detBulk.CAT_SCARICO );
			ps.setString( 2, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
			ps.setString( 3, Pdg_preventivo_detBulk.ST_CONFERMA );		
			ps.setObject( 4, capitolo.getEsercizio() );
			ps.setObject( 5, obbligazione.getEsercizio_originale() );
			ps.setString( 6, Elemento_voceHome.APPARTENENZA_CDS );
			ps.setString( 7, Elemento_voceHome.GESTIONE_SPESE );
			ps.setString( 8, capitolo.getCd_titolo_capitolo() );
			ps.setString( 9, capitolo.getCd_unita_organizzativa() );

			Iterator i = capitoliList.iterator();
			ps.setString( 10, ((Voce_fBulk)i.next()).getCd_funzione() );
			for ( int j = 11; i.hasNext(); j++ )
				ps.setString( j, ((Voce_fBulk)i.next()).getCd_funzione() );

			ResultSet rs = ps.executeQuery();
			try
			{
				PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class);
				return cdrHome.fetchAll( cdrHome.createBroker( ps, rs ));
			}
			catch( Exception e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch( Exception e )
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
 * @param capitoliList	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findCdrPerSpesePerCostiAltruiPerSAC( List capitoliList, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{

	try
	{
		int sizeCapitoli = capitoliList.size() ;
		
		if ( sizeCapitoli == 0  )
			return Collections.EMPTY_LIST;

		String statement =
			"SELECT DISTINCT B.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_OBBLIGAZIONE_SPE A, " +
			EJBCommonServices.getDefaultSchema() +
			"CDR B, " +
			EJBCommonServices.getDefaultSchema() +			
			"CDR C " +			
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			"A.CATEGORIA_DETTAGLIO = ?) AND " +
			"A.STATO = ? AND " +			
			"A.ESERCIZIO = ? AND " +			
			"A.ESERCIZIO_RES = ? AND " +			
			"A.TI_APPARTENENZA_CLGS = ? AND " +
			"A.TI_GESTIONE_CLGS = ? AND " +
			"A.CD_ELEMENTO_VOCE_CLGS = ? AND " +
			"B.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA AND " +
			"C.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA_CLGS AND " ;

		statement = statement.concat( "( (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA_CLGS = ? ) " );
		for ( int t = 1 ; t < sizeCapitoli; t++ )
			statement = statement.concat("OR (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA_CLGS = ? ) ");
		statement = statement.concat( " ) ");					

		LoggableStatement ps = new LoggableStatement(getConnection(),statement,true,this.getClass());
		try
		{	
			Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();

			ps.setString( 1, Pdg_preventivo_detBulk.CAT_SCARICO );
			ps.setString( 2, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
			ps.setString( 3, Pdg_preventivo_detBulk.ST_CONFERMA );		
			ps.setObject( 4, capitolo.getEsercizio() );
			ps.setObject( 5, obbligazione.getEsercizio_originale() );
			ps.setString( 6, Elemento_voceHome.APPARTENENZA_CDS );
			ps.setString( 7, Elemento_voceHome.GESTIONE_SPESE );
			ps.setString( 8, capitolo.getCd_titolo_capitolo() );

			int j = 9;
			Iterator i = capitoliList.iterator();
			capitolo = (Voce_fBulk) i.next();
			ps.setString( j++, capitolo.getCd_funzione() );
			ps.setString( j++, capitolo.getCd_centro_responsabilita() );		

			for ( ; i.hasNext(); )
			{
				capitolo = (Voce_fBulk) i.next();			
				ps.setString( j++, capitolo.getCd_funzione() );
				ps.setString( j++, capitolo.getCd_centro_responsabilita() );		
			}	

			ResultSet rs = ps.executeQuery();
			try
			{
				PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class);
				return cdrHome.fetchAll( cdrHome.createBroker( ps, rs ));
			}
			catch( Exception e )
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
 * @param obbligazione	
 * @return 
 * @throws PersistencyException	
 */
public Timestamp findDataUltimaObbligazionePerCds( ObbligazioneBulk obbligazione ) throws PersistencyException
{
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT TRUNC(MAX(DT_REGISTRAZIONE)) " +			
			"FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"OBBLIGAZIONE WHERE " +
			"ESERCIZIO = ? AND CD_CDS = ? AND CD_TIPO_DOCUMENTO_CONT = ? AND FL_PGIRO = ?" ,
			true,this.getClass());
		try
		{
			ps.setObject( 1, obbligazione.getEsercizio() );
			ps.setString( 2, obbligazione.getCds().getCd_unita_organizzativa() );
			ps.setString( 3, obbligazione.getCd_tipo_documento_cont());
			if ( obbligazione.getFl_pgiro().booleanValue() )
				ps.setString( 4, "Y" );
			else
				ps.setString( 4, "N" );
			
		
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
 * @param cdrList	
 * @param capitoliList	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findLineeAttivita( List cdrList, List capitoliList, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{
	try
	{
			String statement = 
			"SELECT DISTINCT A.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_OBBLIGAZIONE_SPE A " +
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			"A.CATEGORIA_DETTAGLIO = ?) AND " +
			"A.ESERCIZIO = ? AND " +
			"A.ESERCIZIO_RES = ? AND " +
			"A.TI_APPARTENENZA = ? AND " +
			"A.TI_GESTIONE = ? AND " +
			"A.CD_ELEMENTO_VOCE = ? AND ";

			int size = cdrList.size() ;

			if ( size == 0 )
				return Collections.EMPTY_LIST;
				
			statement = statement.concat( "( A.CD_CENTRO_RESPONSABILITA = ? " );
			for ( int t = 1 ; t < size; t++ )
				statement = statement.concat("OR A.CD_CENTRO_RESPONSABILITA = ? ");
			statement = statement.concat( " ) AND ");						
	
			size = capitoliList.size() ;
			if ( size == 0 )
				return Collections.EMPTY_LIST;
	
			statement = statement.concat( "( A.CD_FUNZIONE = ? ");
			for ( int t = 1 ; t < size; t++ )
				statement = statement.concat("OR A.CD_FUNZIONE = ? ");
			
			statement = statement.concat( " ) ");		
			
			LoggableStatement ps = new LoggableStatement(getConnection(), statement,true,this.getClass());
			try
			{	
				IVoceBilancioBulk capitolo = (IVoceBilancioBulk) capitoliList.iterator().next();

				ps.setString( 1, Pdg_preventivo_detBulk.CAT_SINGOLO );
				ps.setString( 2, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
				ps.setObject( 3, capitolo.getEsercizio() );
				ps.setObject( 4, obbligazione.getEsercizio_originale() );
				ps.setString( 5, Elemento_voceHome.APPARTENENZA_CDS );
				ps.setString( 6, Elemento_voceHome.GESTIONE_SPESE );
				ps.setString( 7, capitolo.getCd_titolo_capitolo() );

				Iterator i = cdrList.iterator();
				ps.setString( 8, ((CdrBulk)i.next()).getCd_centro_responsabilita() );
				int j = 8; 
				while( i.hasNext() )
					ps.setString( ++j, ((CdrBulk)i.next()).getCd_centro_responsabilita() );
						
				i = capitoliList.iterator();
				ps.setString( ++j, ((IVoceBilancioBulk)i.next()).getCd_funzione() );
				while (  i.hasNext() )
					ps.setString( ++j, ((IVoceBilancioBulk)i.next()).getCd_funzione() );
				
				ResultSet rs = ps.executeQuery();
				try
				{
					PersistentHome pdgHome = getHomeCache().getHome(V_pdg_obbligazione_speBulk.class);
					return pdgHome.fetchAll( pdgHome.createBroker( ps,rs ));
				}
				catch( Exception e )
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
 * @param cdrList	
 * @param capitoliList	
 * @param uoDiScrivania	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findLineeAttivitaPerSpesePerCostiAltrui( List cdrList, List capitoliList, String uoDiScrivania, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{
	try
	{
			String statement = 
			"SELECT DISTINCT A.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_OBBLIGAZIONE_SPE A " +
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			"A.CATEGORIA_DETTAGLIO = ?) AND " +
			"A.STATO = ? AND " +			
			"A.ESERCIZIO = ? AND " +			
			"A.ESERCIZIO_RES = ? AND " +
			"A.TI_APPARTENENZA_CLGS = ? AND " +
			"A.TI_GESTIONE_CLGS = ? AND " +
			"A.CD_ELEMENTO_VOCE_CLGS = ? AND " +
			"A.CD_CENTRO_RESPONSABILITA_CLGS LIKE ? AND ";

			int size = cdrList.size() ;

			if ( size == 0 )
				return Collections.EMPTY_LIST;
				
			statement = statement.concat( "( A.CD_CENTRO_RESPONSABILITA = ? " );
			for ( int t = 1 ; t < size; t++ )
				statement = statement.concat("OR A.CD_CENTRO_RESPONSABILITA = ? ");
			statement = statement.concat( " ) AND ");						

			size = capitoliList.size() ;
			if ( size == 0 )
				return Collections.EMPTY_LIST;

			statement = statement.concat( "( A.CD_FUNZIONE = ? ");
			for ( int t = 1 ; t < size; t++ )
				statement = statement.concat("OR A.CD_FUNZIONE = ? ");

			statement = statement.concat( " ) ");		
			
			LoggableStatement ps = new LoggableStatement(getConnection(), statement ,true,this.getClass());
			try
			{	
				Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();
	
				ps.setString( 1, Pdg_preventivo_detBulk.CAT_SCARICO );
				ps.setString( 2, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
				ps.setString( 3, Pdg_preventivo_detBulk.ST_CONFERMA );			
				ps.setObject( 4, capitolo.getEsercizio() );
				ps.setObject( 5, obbligazione.getEsercizio_originale() );
				ps.setString( 6, Elemento_voceHome.APPARTENENZA_CDS );
				ps.setString( 7, Elemento_voceHome.GESTIONE_SPESE );
				ps.setString( 8, capitolo.getCd_titolo_capitolo() );
				ps.setString( 9, uoDiScrivania+".%");			

				Iterator i = cdrList.iterator();
				ps.setString( 10, ((CdrBulk)i.next()).getCd_centro_responsabilita() );
				int j = 10; 
				while( i.hasNext() )
					ps.setString( ++j, ((CdrBulk)i.next()).getCd_centro_responsabilita() );
						
				i = capitoliList.iterator();
				ps.setString( ++j, ((Voce_fBulk)i.next()).getCd_funzione() );
				while (  i.hasNext() )
					ps.setString( ++j, ((Voce_fBulk)i.next()).getCd_funzione() );

				ResultSet rs = ps.executeQuery();
				try
				{
					PersistentHome pdgHome = getHomeCache().getHome(V_pdg_obbligazione_speBulk.class);
					return pdgHome.fetchAll( pdgHome.createBroker( ps,rs ));
				}
				catch( Exception e )
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
 * @param capitoliList	
 * @param cdrList	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findLineeAttivitaPerSpesePerCostiAltruiSAC(  List capitoliList, List cdrList, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{
	try
	{
			String statement = 
			"SELECT DISTINCT A.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_OBBLIGAZIONE_SPE A " +
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			"A.CATEGORIA_DETTAGLIO = ?) AND " +
			"A.STATO = ? AND " +			
			"A.ESERCIZIO = ? AND " +			
			"A.ESERCIZIO_RES = ? AND " +			
			"A.TI_APPARTENENZA_CLGS = ? AND " +
			"A.TI_GESTIONE_CLGS = ? AND " +
			"A.CD_ELEMENTO_VOCE_CLGS = ? AND " ;

			int sizeCapitoli = capitoliList.size() ;
			if ( sizeCapitoli == 0 )
				return Collections.EMPTY_LIST;

			int sizeCdr = cdrList.size() ;
			if ( sizeCdr == 0 )
				return Collections.EMPTY_LIST;
				

			statement = statement.concat( "( (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA_CLGS = ? ) " );
			for ( int t = 1 ; t < sizeCapitoli; t++ )
				statement = statement.concat("OR (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA_CLGS = ? ) ");
			statement = statement.concat( " ) AND ");					

			statement = statement.concat( "( A.CD_CENTRO_RESPONSABILITA = ?  " );
			for ( int t = 1 ; t < sizeCdr; t++ )
				statement = statement.concat("OR  A.CD_CENTRO_RESPONSABILITA = ?  ");
			statement = statement.concat( " ) ");					
	
			LoggableStatement ps = new LoggableStatement(getConnection(), statement ,true,this.getClass());
			try
			{	
				Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();

				ps.setString( 1, Pdg_preventivo_detBulk.CAT_SCARICO );
				ps.setString( 2, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
				ps.setString( 3, Pdg_preventivo_detBulk.ST_CONFERMA );			
				ps.setObject( 4, capitolo.getEsercizio() );
				ps.setObject( 5, obbligazione.getEsercizio_originale() );
				ps.setString( 6, Elemento_voceHome.APPARTENENZA_CDS );
				ps.setString( 7, Elemento_voceHome.GESTIONE_SPESE );
				ps.setString( 8, capitolo.getCd_titolo_capitolo() );

				int j = 9;
				Iterator i = capitoliList.iterator();
				capitolo = (Voce_fBulk) i.next();
				ps.setString( j++, capitolo.getCd_funzione() );
				ps.setString( j++, capitolo.getCd_centro_responsabilita() );
				for ( ; i.hasNext(); )
				{
					capitolo = (Voce_fBulk) i.next();			
					ps.setString( j++, capitolo.getCd_funzione() );
					ps.setString( j++, capitolo.getCd_centro_responsabilita() );		
				}

				i = cdrList.iterator();
				CdrBulk cdr = (CdrBulk) i.next();
				ps.setString( j++, cdr.getCd_centro_responsabilita() );
				for ( ; i.hasNext(); )
				{
					cdr = (CdrBulk) i.next();			
					ps.setString( j++, cdr.getCd_centro_responsabilita() );		
				}	


				ResultSet rs = ps.executeQuery();
				try
				{
					PersistentHome pdgHome = getHomeCache().getHome(V_pdg_obbligazione_speBulk.class);
					return pdgHome.fetchAll( pdgHome.createBroker( ps,rs ));
				}
				catch( Exception e )
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
 * @param capitoliList	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findLineeAttivitaSAC(  List cdrList, List capitoliList, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{
	try
	{
			String statement =
			"SELECT DISTINCT A.* FROM " + 		
			EJBCommonServices.getDefaultSchema() +
			"V_PDG_OBBLIGAZIONE_SPE A " +
			"WHERE " +
			"(A.CATEGORIA_DETTAGLIO = ? OR " +
			"(A.CATEGORIA_DETTAGLIO = 'STI' AND A.CD_CENTRO_RESPONSABILITA = ? ) OR " +
			"A.CATEGORIA_DETTAGLIO = ?) AND " +
			"A.ESERCIZIO = ? AND " +
			"A.ESERCIZIO_RES = ? AND " +
			"A.TI_APPARTENENZA = ? AND " +
			"A.TI_GESTIONE = ? AND " +
			"A.CD_ELEMENTO_VOCE = ? AND ";

			int sizeCapitoli = capitoliList.size() ;
			if ( sizeCapitoli == 0 )
				return Collections.EMPTY_LIST;
				
			if (capitoliList.get(0) instanceof Voce_fBulk) {
				statement = statement.concat( "( (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) " );
				for ( int t = 1 ; t < sizeCapitoli; t++ )
					statement = statement.concat("OR (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) ");
				statement = statement.concat( " ) ");
			} else {
				int sizeCdr = cdrList.size() ;
				if ( sizeCdr == 0 )
					return Collections.EMPTY_LIST;
				statement = statement.concat( "( (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) " );
				for ( int t = 1 ; t < sizeCdr; t++ )
					statement = statement.concat("OR (A.CD_FUNZIONE = ? AND A.CD_CENTRO_RESPONSABILITA = ? ) ");
				statement = statement.concat( " ) ");
			}
			
			LoggableStatement ps = new LoggableStatement(getConnection(),statement,true,this.getClass());
			try
			{	
				IVoceBilancioBulk capitolo = (IVoceBilancioBulk) capitoliList.iterator().next();

				Optional<String> optCdrPersonale = Optional.ofNullable(((Configurazione_cnrHome) getHomeCache().getHome(Configurazione_cnrBulk.class))
						.getCdrPersonale(capitolo.getEsercizio()));
				if (!optCdrPersonale.isPresent())
					throw new RuntimeException("Non è possibile individuare il codice CDR del Personale per l'esercizio "+capitolo.getEsercizio()+".");

				ps.setString( 1, Pdg_preventivo_detBulk.CAT_SINGOLO );
				ps.setString( 2, optCdrPersonale.get() );
				ps.setString( 3, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
				ps.setObject( 4, capitolo.getEsercizio() );
				ps.setObject( 5, obbligazione.getEsercizio_originale() );
				ps.setString( 6, Elemento_voceHome.APPARTENENZA_CDS );
				ps.setString( 7, Elemento_voceHome.GESTIONE_SPESE );
				ps.setString( 8, capitolo.getCd_titolo_capitolo() );

				int j = 9;
				Iterator i = capitoliList.iterator();
				capitolo = (IVoceBilancioBulk) i.next();
				if (capitolo instanceof Voce_fBulk) {
					ps.setString( j++, capitolo.getCd_funzione() );
					ps.setString( j++, ((Voce_fBulk)capitolo).getCd_centro_responsabilita() );
						
					for ( ; i.hasNext(); )
					{
						capitolo = (Voce_fBulk) i.next();			
						ps.setString( j++, capitolo.getCd_funzione() );
						ps.setString( j++, ((Voce_fBulk)capitolo).getCd_centro_responsabilita() );		
					}
				} else {
					//nel caso di capitolo instanceof Elemento_voce nell'iterator capitoliList c'è sempre un solo elemento
					//per cui non effettuo il loop sull'iterator capitoliList
					for (Iterator iterator = cdrList.iterator(); iterator.hasNext();) {
						CdrBulk cdr = (CdrBulk) iterator.next();
						ps.setString( j++, capitolo.getCd_funzione() );
						ps.setString( j++, cdr.getCd_centro_responsabilita() );
					}
				}
						
				ResultSet rs = ps.executeQuery();
				try
				{
					PersistentHome pdgHome = getHomeCache().getHome(V_pdg_obbligazione_speBulk.class);
					return pdgHome.fetchAll( pdgHome.createBroker( ps,rs ));
				}
				catch( Exception e )
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
 * @param obbligazione	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public SQLBuilder findObbligazione_scad_voceDistinctList( ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{
	PersistentHome osHome = getHomeCache().getHome(Obbligazione_scad_voceBulk.class,"CDR_LINEA_VOCE");
	SQLBuilder sql = osHome.createSQLBuilder();
	sql.resetColumns();
	sql.setDistinctClause(true);
	sql.addColumn("CD_VOCE");
	sql.addColumn("CD_CENTRO_RESPONSABILITA");
	sql.addColumn("CD_LINEA_ATTIVITA");
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS, obbligazione.getCds().getCd_unita_organizzativa());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, obbligazione.getEsercizio());
	sql.addSQLClause("AND","ESERCIZIO_ORIGINALE",sql.EQUALS, obbligazione.getEsercizio_originale());
	sql.addSQLClause("AND","PG_OBBLIGAZIONE",sql.EQUALS, obbligazione.getPg_obbligazione());
	
	PersistentHome elemento_voceHome = getHomeCache().getHome(Elemento_voceBulk.class);
	SQLBuilder sqlElemento_voce = elemento_voceHome.createSQLBuilder();
	sqlElemento_voce.addSQLClause("AND","ESERCIZIO",sql.EQUALS, obbligazione.getEsercizio());
	sqlElemento_voce.addSQLClause("AND","TI_APPARTENENZA",sql.EQUALS, obbligazione.getTi_appartenenza());
	sqlElemento_voce.addSQLClause("AND","TI_GESTIONE",sql.EQUALS, obbligazione.getTi_gestione());
	sqlElemento_voce.addSQLClause("AND","CD_ELEMENTO_VOCE",sql.EQUALS, obbligazione.getCd_elemento_voce());
	sqlElemento_voce.addSQLClause("AND","FL_LIMITE_ASS_OBBLIG",sql.EQUALS, "Y");

	PersistentHome workpackage_home = getHomeCache().getHome(WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA");
	SQLBuilder sqlWorkpackage = workpackage_home.createSQLBuilder();
	sqlWorkpackage.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","OBBLIGAZIONE_SCAD_VOCE.CD_CENTRO_RESPONSABILITA");
	sqlWorkpackage.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA","OBBLIGAZIONE_SCAD_VOCE.CD_LINEA_ATTIVITA");
	sqlWorkpackage.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.FL_LIMITE_ASS_OBBLIG",sql.EQUALS, "Y");
	sql.openParenthesis("AND");
	sql.addSQLExistsClause("OR", sqlElemento_voce);
	sql.addSQLExistsClause("OR", sqlWorkpackage);
	sql.closeParenthesis();
	
	return sql;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazione	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findObbligazione_scadenzarioList( ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
{
	PersistentHome osHome = getHomeCache().getHome(Obbligazione_scadenzarioBulk.class);
	SQLBuilder sql = osHome.createSQLBuilder();
	sql.addClause("AND","cd_cds",sql.EQUALS, obbligazione.getCds().getCd_unita_organizzativa());
	sql.addClause("AND","esercizio",sql.EQUALS, obbligazione.getEsercizio());
	sql.addClause("AND","esercizio_originale",sql.EQUALS, obbligazione.getEsercizio_originale());
	sql.addClause("AND","pg_obbligazione",sql.EQUALS, obbligazione.getPg_obbligazione());
	sql.addOrderBy("dt_scadenza");
	sql.addOrderBy("pg_obbligazione_scadenzario");
	return osHome.fetchAll(sql);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazione	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public ObbligazioneBulk findObbligazione(ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException
{
	if (obbligazione.isObbligazioneResiduo())
		return findObbligazioneRes(obbligazione);
	else if (obbligazione.isObbligazioneResiduoImproprio())
		return findObbligazioneRes_impropria(obbligazione);
	else
		return findObbligazioneOrd(obbligazione);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazione	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public ObbligazioneOrdBulk findObbligazioneOrd(ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException
{
	return	(ObbligazioneOrdBulk) findByPrimaryKey( new ObbligazioneOrdBulk( obbligazione.getCd_cds(), obbligazione.getEsercizio(),obbligazione.getEsercizio_originale(),obbligazione.getPg_obbligazione()));
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazione	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public ObbligazioneResBulk findObbligazioneRes(ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException
{
	return	(ObbligazioneResBulk) findByPrimaryKey( new ObbligazioneResBulk( obbligazione.getCd_cds(), obbligazione.getEsercizio(), obbligazione.getEsercizio_originale(), obbligazione.getPg_obbligazione()));
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazione	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public ObbligazioneRes_impropriaBulk findObbligazioneRes_impropria(ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException
{
	return	(ObbligazioneRes_impropriaBulk) findByPrimaryKey( new ObbligazioneRes_impropriaBulk( obbligazione.getCd_cds(), obbligazione.getEsercizio(), obbligazione.getEsercizio_originale(), obbligazione.getPg_obbligazione()));
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param obbligazione	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public ImpegnoPGiroBulk findObbligazionePGiro (ObbligazioneBulk obbligazione) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException
{
	return	(ImpegnoPGiroBulk) findByPrimaryKey( new ImpegnoPGiroBulk( obbligazione.getCd_cds(), obbligazione.getEsercizio(),obbligazione.getEsercizio_originale(),obbligazione.getPg_obbligazione()));
}
private StringBuffer getPersistenColumnNamesReplacingWith( 
	BulkHome home,
	String[][] fields)
	throws PersistencyException {

	java.io.StringWriter columns = new java.io.StringWriter();

	if (home == null)
		throw new PersistencyException("Impossibile ottenere la home per l'aggiornamento dei progressivi temporanei dell'impegno!");

	java.io.PrintWriter pw = new java.io.PrintWriter(columns);
	String[] persistenColumns = home.getColumnMap().getColumnNames();
	for (int i = 0; i < persistenColumns.length; i++) {
		String columnName = persistenColumns[i];
		if (fields != null) {
			for (int j = 0; j < fields.length; j++) {
				String[] field = fields[j];
				if (columnName.equalsIgnoreCase(field[0])) {
					columnName = field[1];
					break;
				}
			}
		}
		pw.print(columnName);
		pw.print((i == persistenColumns.length-1)?"":", ");
	}
	pw.close();
	return columns.getBuffer();
}
	/**
	 * Imposta il pg_obbligazione di un oggetto <code>ObbligazioneBulk</code>.
	 *
	 * @param bulk <code>OggettoBulk</code>
	 *
	 * @exception PersistencyException
	 */

public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException 
{
	try
	{
		ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
		Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache().getHome( Numerazione_doc_contBulk.class );
		Long pg = (!userContext.isTransactional()) ?
					numHome.getNextPg(userContext,
							obbligazione.getEsercizio(), 
							obbligazione.getCd_cds(), 
							obbligazione.getCd_tipo_documento_cont(), 
							obbligazione.getUser()) :
					((NumTempDocContComponentSession)EJBCommonServices.createEJB(
							"CNRDOCCONT00_EJB_NumTempDocContComponentSession",
							NumTempDocContComponentSession.class)).getNextTempPg(userContext, obbligazione);
						
		obbligazione.setPg_obbligazione( pg );
	} catch ( ApplicationException e ) {
		throw new ComponentException(e);
	} catch ( Throwable e )	{
		throw new PersistencyException( e );
	}
}
/**
 * Metodo per aggiornare la lista delle nuove linee di attività associate all'obbligazione.
 *
 * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da aggiornare
 *
 * @return obbligazione <code>ObbligazioneBulk</code> l'obbligazione con la lista delle nuove linee di attività aggiornata
 */
public ObbligazioneBulk refreshNuoveLineeAttivitaColl( UserContext usercontext, ObbligazioneBulk obbligazione ) 
{
	
	V_pdg_obbligazione_speBulk latt;
	boolean found;
	it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt;
	BulkList nuoveLineeAttivitaColl = new BulkList();
		

	//l'imputazione finanziaria e' sempre di testata: seleziono i dettagli di una qualsiasi scadenza con importo != 0 per
	// individuare l'elenco delle linee di attivita selezionate per l'intera obbligazione

	Obbligazione_scadenzarioBulk scadenza = null;
	for ( Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
	{
		scadenza = (Obbligazione_scadenzarioBulk) i.next();
		if ( scadenza.getIm_scadenza().compareTo( new BigDecimal(0)) > 0 )
			break;
	}		
	
	
	for ( Iterator s = scadenza.getObbligazione_scad_voceColl().iterator(); s.hasNext(); )
	{
		Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk) s.next();
		found = false;
		
		for ( Iterator l = obbligazione.getLineeAttivitaSelezionateColl().iterator(); l.hasNext(); )
		{
			latt = (V_pdg_obbligazione_speBulk) l.next();
			if ( osv.getCd_centro_responsabilita().equals( latt.getCd_centro_responsabilita() ) &&
				 osv.getCd_linea_attivita().equals( latt.getCd_linea_attivita() ) )
				found = true;
		}
		if (!found)
		{		
			nuovaLatt = new it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk();

			try{
            	PersistentHome laHome = getHomeCache().getHome(WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA", "it.cnr.contab.doccont00.comp.AccertamentoComponent.find.linea_att");
                nuovaLatt.setLinea_att((WorkpackageBulk)laHome.findByPrimaryKey(osv.getLinea_attivita()));
                getHomeCache().fetchAll(usercontext);
            } catch(Exception e) {
            	nuovaLatt.setLinea_att(osv.getLinea_attivita());
            }

			if ( osv.getObbligazione_scadenzario().getIm_scadenza().compareTo( new BigDecimal(0)) == 0 )
			{
				double nrDettagli = scadenza.getObbligazione_scad_voceColl().size();
				nuovaLatt.setPrcImputazioneFin( new BigDecimal(100).divide( new BigDecimal( nrDettagli), 2, BigDecimal.ROUND_HALF_UP) );	
			}		
			else
				nuovaLatt.setPrcImputazioneFin( osv.getIm_voce().multiply( new BigDecimal(100)).divide( osv.getObbligazione_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP ));
			nuovaLatt.setObbligazione( obbligazione );
			nuoveLineeAttivitaColl.add( nuovaLatt );
			
		}			
	}

	if ( obbligazione.getLineeAttivitaSelezionateColl().size() == 0 )
	{
			//non esistono latt da pdg --> e'necessario quadrare
			BigDecimal totPrc = new BigDecimal(0);
			for ( Iterator i = nuoveLineeAttivitaColl.iterator(); i.hasNext(); )
			{
				nuovaLatt = (it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk) i.next();
				totPrc = totPrc.add( nuovaLatt.getPrcImputazioneFin());
			}
			if ( totPrc.compareTo( new BigDecimal(100)) != 0 && !nuoveLineeAttivitaColl.isEmpty())
			{
				nuovaLatt = (it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk)nuoveLineeAttivitaColl.get(0);
				nuovaLatt.setPrcImputazioneFin( nuovaLatt.getPrcImputazioneFin().add( new BigDecimal(100).subtract( totPrc)));
			}	
	}	
	obbligazione.setNuoveLineeAttivitaColl( nuoveLineeAttivitaColl );
	return obbligazione;
}
/*
- obbligazione non associata a documenti amministartivi: viene selezionato un qualsiasi terzo di tipo CREDITORE
  o ENTRAMBI
- se l'obbligazione è associata a documenti amministrativi e era gia' stato impostato un terzo: la selezione prevede
  tutti i terzi con tipo entità = DIVERSI di tipo CREDITORE/ENTRAMBI più il terzo già selezionato
- se l'obbligazione è associata a documenti amministrativi e non era gia' stato impostato un terzo: la selezione prevede
  tutti i terzi con tipo entità = DIVERSI di tipo CREDITORE/ENTRAMBI 
*/
public SQLBuilder selectCreditoreByClause( ObbligazioneBulk bulk, TerzoHome home, TerzoBulk terzo,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException 
{

	SQLBuilder sql = getHomeCache().getHome(TerzoBulk.class , "V_TERZO_CF_PI").createSQLBuilder();

	if ( terzo.getCd_precedente() != null )
		sql.addSQLClause("AND","CD_PRECEDENTE",sql.EQUALS, terzo.getCd_precedente() );
	
	if ( !bulk.isAssociataADocAmm() ) //obbligazione non associata a documenti amministrativi
	{
		sql.openParenthesis( "AND");		
		sql.addSQLClause("AND","TI_TERZO",sql.EQUALS, terzo.CREDITORE );						
		sql.addSQLClause("OR","TI_TERZO",sql.EQUALS, terzo.ENTRAMBI );
		sql.closeParenthesis();		 	 
		if (bulk.getCd_terzo()!=null) 
		  sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_TERZO", sql.EQUALS, bulk.getCd_terzo());		
		sql.addSQLClause( "AND", "(V_TERZO_CF_PI.DT_FINE_RAPPORTO >= SYSDATE OR V_TERZO_CF_PI.DT_FINE_RAPPORTO IS NULL)");		
		sql.addClause( clause );
		if ( terzo.getAnagrafico() != null )
		{
		   if ( terzo.getAnagrafico().getCodice_fiscale() != null || terzo.getAnagrafico().getPartita_iva() != null  )
		   {   //aggiungo join su anagrafico
				sql.addTableToHeader("ANAGRAFICO");
				sql.addSQLJoin( "V_TERZO_CF_PI.CD_ANAG", "ANAGRAFICO.CD_ANAG");
		   }	
	 	  	if ( terzo.getAnagrafico().getCodice_fiscale() != null)			
		 		sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",sql.EQUALS, terzo.getAnagrafico().getCodice_fiscale() );
	   	if ( terzo.getAnagrafico().getPartita_iva() != null )
		  		sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA",sql.EQUALS, terzo.getAnagrafico().getPartita_iva() );
		}  		
	}	
	else //obbligazione associata a documenti amministrativi 
	{
		sql.setHeader("select distinct V_TERZO_CF_PI.*");
		sql.addTableToHeader("ANAGRAFICO");
		sql.openParenthesis( "AND");		
		sql.addSQLClause("AND","V_TERZO_CF_PI.TI_TERZO",sql.EQUALS, terzo.CREDITORE );						
		sql.addSQLClause("OR","V_TERZO_CF_PI.TI_TERZO",sql.EQUALS, terzo.ENTRAMBI );
		sql.closeParenthesis();		
  	    sql.addSQLClause( "AND", "(V_TERZO_CF_PI.DT_FINE_RAPPORTO >= SYSDATE OR V_TERZO_CF_PI.DT_FINE_RAPPORTO IS NULL)");		
		sql.addClause( clause );
		sql.openParenthesis( "AND");		
		sql.openParenthesis( "AND");		
		sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_ANAG = ANAGRAFICO.CD_ANAG");
		sql.addSQLClause("AND", "ANAGRAFICO.TI_ENTITA", sql.EQUALS, AnagraficoBulk.DIVERSI);
		sql.closeParenthesis();
		sql.addSQLClause("OR", "V_TERZO_CF_PI.CD_TERZO", sql.EQUALS, bulk.getCd_terzo_iniziale());		
		sql.closeParenthesis();
		if ( terzo.getAnagrafico() != null )
		{
			if ( terzo.getAnagrafico().getCodice_fiscale() != null )
		   	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",sql.EQUALS, terzo.getAnagrafico().getCodice_fiscale() );
	   	if ( terzo.getAnagrafico().getPartita_iva() != null )
		  		sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA",sql.EQUALS, terzo.getAnagrafico().getPartita_iva() );
		}  		
		   
	}	
	return sql;
	/*
	if ( !bulk.isAssociataADocAmm() && 
		bulk.getCrudStatus() != bulk.TO_BE_CREATED && 
		bulk.getCrudStatus() != bulk.UNDEFINED ) //obbligazione non associata a documenti amministrativi e in fase di modifica
	{
		if ( bulk.getCd_terzo() != null ) //terzo già selezionato
		{
			sql.setStatement(
						"select distinct A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "TERZO A " + 
						" where  " +
						"(A.TI_TERZO =  '" + terzo.CREDITORE + "' OR " +
						"A.TI_TERZO = '" + terzo.ENTRAMBI + "') AND " +
						"A.CD_TERZO = " + bulk.getCd_terzo() 	+
						"AND (DT_FINE_RAPPORTO >= SYSDATE OR DT_FINE_RAPPORTO IS NULL)");									
		}
		else
		{
			sql.setStatement(
						"select A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "TERZO A" + 
						" where  " +
						"(A.TI_TERZO =  '" + terzo.CREDITORE + "' OR " +
						"A.TI_TERZO = '" + terzo.ENTRAMBI + "')" +
						"AND (DT_FINE_RAPPORTO >= SYSDATE OR DT_FINE_RAPPORTO IS NULL)");									
		}	
	}
	else if ( bulk.isAssociataADocAmm() && 
			bulk.getCrudStatus() != bulk.TO_BE_CREATED && 
			bulk.getCrudStatus() != bulk.UNDEFINED )//obbligazione associata a documenti amministrativi e in fase di modifica
	{
		if ( bulk.getCd_terzo() != null ) //utente ha specificato il codice terzo
		{
			sql.setStatement(
						"select distinct A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "TERZO A, " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ANAGRAFICO B " +
						" where  " +
						"(A.TI_TERZO =  '" + terzo.CREDITORE + "' OR " +
						"A.TI_TERZO = '" + terzo.ENTRAMBI + "') AND " +
						"((A.CD_ANAG = B.CD_ANAG " +						
						"AND B.TI_ENTITA = '" + AnagraficoBulk.DIVERSI + "' ) "  +
						"OR A.CD_TERZO = '" + bulk.getCd_terzo_iniziale() + "') "  +
						"AND A.CD_TERZO = '" + bulk.getCd_terzo() + "' " +
						"AND (A.DT_FINE_RAPPORTO >= SYSDATE OR A.DT_FINE_RAPPORTO IS NULL)");						

		}	
		else //utente non ha specificato il codice terzo
		{
			sql.setStatement(
						"select distinct A.* from " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "TERZO A, " + 
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ANAGRAFICO B " +
						" where  " +
						"(A.TI_TERZO =  '" + terzo.CREDITORE + "' OR " +
						"A.TI_TERZO = '" + terzo.ENTRAMBI + "') AND " +
						"((A.CD_ANAG = B.CD_ANAG " +						
						"AND B.TI_ENTITA = '" + AnagraficoBulk.DIVERSI + "' ) "  +
						"OR A.CD_TERZO = '" + bulk.getCd_terzo_iniziale() + "') "  +
						"AND (A.DT_FINE_RAPPORTO >= SYSDATE OR A.DT_FINE_RAPPORTO IS NULL)");			
		}			
	}
	else
	{
		sql.openParenthesis( "AND");		
		sql.addSQLClause("AND","TI_TERZO",sql.EQUALS, terzo.CREDITORE );						
		sql.addSQLClause("OR","TI_TERZO",sql.EQUALS, terzo.ENTRAMBI );
		sql.closeParenthesis();		
  	    sql.addSQLClause( "AND", "(DT_FINE_RAPPORTO >= SYSDATE OR DT_FINE_RAPPORTO IS NULL)");		
		if ( bulk.getCd_terzo() != null ) //terzo già selezionato
		{
			sql.addSQLClause("AND","CD_TERZO",sql.EQUALS, bulk.getCd_terzo());						
		}
		sql.addClause( clause );
	}
	return sql;
	*/
}
/**
 * Metodo per cercare la voce del piano dei conti definita per Cnr o Cds dell'obbligazione.
 *
 * @param bulk <code>ObbligazioneBulk</code> il contesto (obbligazione) in cui viene fatta la ricerca dell'elemento voce
 * @param home istanza di <code>Elemento_voceHome</code>
 * @param bulkClause <code>OggettoBulk</code> elemento voce su cui viene fatta la ricerca
 * @param clause <code>CompoundFindClause</code> le clausole della selezione
 *
 * @return sql il risultato della selezione
 *
 */
public SQLBuilder selectElemento_voceByClause( ObbligazioneBulk bulk, Elemento_voceHome home, Elemento_voceBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException 
{
	PersistentHome parCNRHome = getHomeCache().getHome(Parametri_cnrBulk.class);
	Parametri_cnrBulk parCNR = (Parametri_cnrBulk)parCNRHome.findByPrimaryKey(new Parametri_cnrBulk(bulk.getEsercizio()));

	SQLBuilder sql = home.createSQLBuilder();
	if ( bulk instanceof ObbligazioneOrdBulk || bulk instanceof ObbligazioneResBulk || 
	     bulk instanceof ObbligazioneRes_impropriaBulk || bulk instanceof ImpegnoBulk)
	{
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CDS );
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
		sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_CAPITOLO );
		// selezionando solo la parte 1 e' implicito che non siano partite di giro (fl_pgiro='N')		
		if (parCNR==null || !parCNR.getFl_nuovo_pdg())
			sql.addClause("AND", "cd_parte", SQLBuilder.EQUALS, home.PARTE_1 );
		if ( bulk.getCds() != null && bulk.getCds().getCd_tipo_unita()!=null && !bulk.getCds().getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
			sql.addClause("AND", "fl_voce_sac", SQLBuilder.EQUALS, new Boolean( false) );		
		sql.addClause( clause );
	}
	
/*	else if ( bulk instanceof ObbligazionePGiroBulk )
	{
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CDS );
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
		sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_CAPITOLO );
		sql.addClause("AND", "cd_parte", SQLBuilder.EQUALS, home.PARTE_2 );
		sql.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, new Boolean(true) );		
		sql.addClause( clause );
	}*/
	
	/* non usato perchè ridefinito nella sottoclasse ImpegnoPGiroHome, ma viene usato per ImpegnoPGiroResiduo */
	else if ( bulk instanceof ImpegnoPGiroBulk )
	{
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
		if ( ((ImpegnoPGiroBulk) bulk).getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_IMP))
			sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CNR );
		else // == OBB_PGIRO
			sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, home.APPARTENENZA_CDS );		
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, home.GESTIONE_SPESE );
		sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, home.TIPO_CAPITOLO );
		if (!parCNR.getFl_nuovo_pdg())
			sql.addClause("AND", "cd_parte", SQLBuilder.EQUALS, home.PARTE_2 );
		sql.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, new Boolean(true) );
		if ( bulk.getCds() != null && bulk.getCds().getCd_tipo_unita()!=null && !bulk.getCds().getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
			sql.addClause("AND", "fl_voce_sac", SQLBuilder.EQUALS, new Boolean( false) );				
		sql.addClause( clause );
	}
	if (bulk instanceof ObbligazioneResBulk || bulk instanceof ObbligazioneRes_impropriaBulk||bulk instanceof ImpegnoPGiroResiduoBulk)
		 sql.addClause("AND", "fl_solo_competenza", SQLBuilder.EQUALS, new Boolean( false) );		 
	else
		sql.addClause("AND", "fl_solo_residuo", SQLBuilder.EQUALS, new Boolean( false) );
	if (bulk instanceof ObbligazioneRes_impropriaBulk)
		sql.addClause("AND", "fl_azzera_residui", SQLBuilder.EQUALS, new Boolean( false) );
	
	//Select che serve per limitare la visualizzazione alle sole voci di bilancio che hanno avuto almeno uno stanziamento
	try {
		SQLBuilder sqlAssestato = new SQLBuilder();
		sqlAssestato.addTableToHeader("VOCE_F_SALDI_CDR_LINEA");
		sqlAssestato.setHeader(String.valueOf("SELECT 1"));
		sqlAssestato.addSQLJoin("VOCE_F_SALDI_CDR_LINEA.ESERCIZIO","ELEMENTO_VOCE.ESERCIZIO");
		sqlAssestato.addSQLJoin("VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA","ELEMENTO_VOCE.TI_APPARTENENZA");
		sqlAssestato.addSQLJoin("VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE","ELEMENTO_VOCE.TI_GESTIONE");
		sqlAssestato.addSQLJoin("VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE","ELEMENTO_VOCE.CD_ELEMENTO_VOCE");

		sqlAssestato.openParenthesis(FindClause.AND);
		sqlAssestato.addSQLClause(FindClause.OR, "ELEMENTO_VOCE.FL_LIMITE_ASS_OBBLIG", SQLBuilder.EQUALS, "N" );
		sqlAssestato.addSQLClause(FindClause.OR, "(nvl(VOCE_F_SALDI_CDR_LINEA.IM_STANZ_INIZIALE_A1,0)+nvl(VOCE_F_SALDI_CDR_LINEA.VARIAZIONI_PIU,0)-nvl(VOCE_F_SALDI_CDR_LINEA.VARIAZIONI_MENO,0))", SQLBuilder.GREATER, 0);
		sqlAssestato.addSQLClause(FindClause.OR, "(nvl(VOCE_F_SALDI_CDR_LINEA.IM_STANZ_RES_IMPROPRIO,0)+nvl(VOCE_F_SALDI_CDR_LINEA.VAR_PIU_STANZ_RES_IMP,0)-nvl(VOCE_F_SALDI_CDR_LINEA.VAR_MENO_STANZ_RES_IMP,0) +  nvl(VOCE_F_SALDI_CDR_LINEA.VAR_MENO_OBBL_RES_PRO,0)-nvl(VOCE_F_SALDI_CDR_LINEA.VAR_PIU_OBBL_RES_PRO,0))", SQLBuilder.GREATER, 0);
		sqlAssestato.closeParenthesis();
		
		if (bulk.getListaVociSelezionabili() != null && !bulk.getListaVociSelezionabili().isEmpty()) {
			sql.openParenthesis("AND");
			for (Elemento_voceBulk voce : bulk.getListaVociSelezionabili()){
				sql.openParenthesis("OR");
				sql.addSQLClause("AND","ELEMENTO_VOCE.CD_ELEMENTO_VOCE",sql.EQUALS, voce.getCd_elemento_voce());
				sql.addSQLClause("AND","ELEMENTO_VOCE.TI_APPARTENENZA",sql.EQUALS, voce.getTi_appartenenza());
				sql.addSQLClause("AND","ELEMENTO_VOCE.TI_GESTIONE",sql.EQUALS, voce.getTi_gestione());
				sql.addSQLClause("AND","ELEMENTO_VOCE.ESERCIZIO",sql.EQUALS, voce.getEsercizio());
				sql.closeParenthesis();
				
			}
			sql.closeParenthesis();
		}
		
		SQLBuilder sqlstrOrg = new SQLBuilder();
		sqlstrOrg.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA");
		sqlstrOrg.setHeader(String.valueOf("SELECT 1"));
		sqlstrOrg.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_CENTRO_RESPONSABILITA","VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA");
		sqlstrOrg.addSQLClause(FindClause.AND,"V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS, bulk.getUnita_organizzativa().getCd_unita_organizzativa());
		
		sqlAssestato.addSQLExistsClause(FindClause.AND, sqlstrOrg);
		
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR, "fl_limite_ass_obblig", SQLBuilder.EQUALS, Boolean.FALSE );
		sql.addSQLExistsClause(FindClause.OR, sqlAssestato);
		sql.closeParenthesis();
	} catch (IntrospectionException e) {
		e.printStackTrace();
	}

	return sql; 
		
}
/**
 * Ritorna tutti le obbligazioni uguali al bulk indipendentemente dall'esercizio
 * comprensivo di quello indicato nel bulk
 *
 */
public SQLBuilder selectAllEqualsObbligazioniByClause( ObbligazioneBulk bulk, ObbligazioneHome home,OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException
{	
	SQLBuilder sql = this.createSQLBuilder();
	sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds() );
	sql.addClause(FindClause.AND, "esercizio_originale", SQLBuilder.EQUALS, bulk.getEsercizio_originale() );
	sql.addClause(FindClause.AND, "pg_obbligazione", SQLBuilder.EQUALS, bulk.getPg_obbligazione() );
	sql.addClause( clause );
	return sql;
}
	public java.util.Collection findObbligazioniPluriennali(it.cnr.jada.UserContext userContext, ObbligazioneBulk bulk) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Obbligazione_pluriennaleBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, bulk.getCd_cds());
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, bulk.getEsercizio());
		sql.addSQLClause("AND", "ESERCIZIO_ORIGINALE", sql.EQUALS, bulk.getEsercizio_originale());
		sql.addSQLClause("AND", "PG_OBBLIGAZIONE", sql.EQUALS, bulk.getPg_obbligazione());

		sql.setOrderBy("ID_OBBLIGAZIONE_PLUR", it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return dettHome.fetchAll(sql);
	}
}
