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

package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_voce_epBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

import java.sql.*;
import java.util.*;

/**
 * Home che gestisce i capoconti e i conti.
 */
public class Voce_epHome extends BulkHome {
	public static final String TIPO_CONTO = "C";
	public static final String TIPO_CAPOCONTO = "P";
	public static final String TIPO_GRUPPO = "G";

	public static String SEZIONE_DARE = "D";
	public static String SEZIONE_AVERE = "A";
	public static String SEZIONE_BIFASE = "B";

	public static String ECONOMICA = "ECO";
	public static String PATRIMONIALE = "PAT";

	private static it.cnr.jada.util.OrderedHashtable gruppiKeys;
protected Voce_epHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Voce_epHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 */
public Voce_epHome(java.sql.Connection conn) {
	super(Voce_epBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 * @param persistentCache	
 */
public Voce_epHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Voce_epBulk.class,conn,persistentCache);
}
/**
 * Metodo che esegue la creazione di un nuovo codice di Capoconto.
 * In particolare, se l'utente non inserisce il codice del capoconto, 
 * ne viene generato automaticamente uno nuovo dal sistema, calcolando 
 * il progressivo successivo al massimo esistente per il CapocontoBulk.
 * @param esercizio L'esercizio corrente.
 * @param codiceGruppo Il codice del gruppo associato al nuovo capoconto
 *						(selezionato dall'utente).
 * @return codice Il codice del nuovo capoconto generato.
 */
public String creaNuovoCodiceCapoconto( Integer esercizio, String codiceGruppo) throws PersistencyException
{	
	String codice;

	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT CD_PROPRIO_VOCE_EP FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"VOCE_EP " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_VOCE_EP_PADRE = ? AND " +
			"CD_PROPRIO_VOCE_EP = ( SELECT MAX(CD_PROPRIO_VOCE_EP) FROM " +			
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"VOCE_EP WHERE " +
			"ESERCIZIO = ? AND CD_VOCE_EP_PADRE = ?)" +
			"FOR UPDATE NOWAIT" ,true,this.getClass());
		try
		{
			ps.setObject( 1, esercizio );
			ps.setString( 2, codiceGruppo );
			ps.setObject( 3, esercizio );
			ps.setString( 4, codiceGruppo );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
				{
					codice = rs.getString( 1 );
					if ( codice != null )
					{
						long cdLong = Long.parseLong( codice ) + 1;
						codice = String.valueOf( cdLong );
					}
					else
						codice = String.valueOf( 1 );
				}
				else
					codice = String.valueOf( 1 );		
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
		
//		return getLunghezza_chiaviHome().create().formatCdrKey( codice, esercizio, livello );
		return codice;
	}
	catch (SQLException e)
	{
			throw new PersistencyException( e.getMessage() );
	}

}
/**
 * Metodo che esegue la creazione di un nuovo codice di Conto.
 * In particolare, se l'utente non inserisce il codice del conto, 
 * ne viene generato automaticamente uno nuovo dal sistema, calcolando 
 * il progressivo successivo al massimo esistente per il ContoBulk.
 * @param esercizio L'esercizio corrente.
 * @param codiceCapoconto Il codice del capoconto associato al nuovo conto
 *						  (selezionato dall'utente).
 * @return codice Il codice del nuovo conto generato.
 */
public String creaNuovoCodiceConto( Integer esercizio, String codiceCapoconto) throws PersistencyException
{	
	String codice;

	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT CD_PROPRIO_VOCE_EP FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"VOCE_EP " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_VOCE_EP_PADRE = ? AND " +
			"CD_PROPRIO_VOCE_EP = ( SELECT MAX(CD_PROPRIO_VOCE_EP) FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"VOCE_EP WHERE " +
			"ESERCIZIO = ? AND CD_VOCE_EP_PADRE = ?)" +
			"FOR UPDATE NOWAIT" ,true,this.getClass());
		try
		{
			ps.setObject( 1, esercizio );
			ps.setString( 2, codiceCapoconto );
			ps.setObject( 3, esercizio );
			ps.setString( 4, codiceCapoconto );
		
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
				{
					codice = rs.getString( 1 );
					if ( codice != null )
					{
						long cdLong = Long.parseLong( codice ) + 1;
						codice = String.valueOf( cdLong );
					}
					else
						codice = String.valueOf( 1 );
				}
				else
					codice = String.valueOf( 1 );		
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
					
//		return getLunghezza_chiaviHome().create().formatCdrKey( codice, esercizio, livello );
		return codice;
	}
	catch (SQLException e)
	{
			throw new PersistencyException( e.getMessage() );
	}

}
/**
 * Carica in un dictionary l'elenco dei possibili valori che può
 * assumere un gruppo.
 * @param bulk L'OggettoBulk in uso.
 * @return gruppiKeys I valori del gruppo.
 */
public Dictionary loadGruppiKeys(OggettoBulk bulk) throws ApplicationException 
{
	if (gruppiKeys == null)
	{ 
		try
		{ 
			LoggableStatement ps = new LoggableStatement(getConnection(),
				"SELECT CD_VOCE_EP, DS_VOCE_EP FROM " + 
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 				
				"VOCE_EP " +
				"WHERE ESERCIZIO = ?  " +				
				"AND TI_VOCE_EP = ?  " +
				"ORDER BY CD_VOCE_EP",true,this.getClass());
			try
			{
				ps.setObject( 1, ((Voce_epBulk)bulk).getEsercizio() );
				ps.setString( 2, TIPO_GRUPPO );
		
				ResultSet rs = ps.executeQuery();
				try
				{
					gruppiKeys = new OrderedHashtable() ;	
				
					while ( rs.next() )
					{
						String codice = rs.getString( 1 );
						gruppiKeys.put( codice, codice.concat(" - ").concat( rs.getString(2)));
					}		
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
		catch ( Exception e )
		{
			gruppiKeys = null ;
			throw new ApplicationException( "Non è possibile recuperare i gruppi. " );			
		}
	} 
	return gruppiKeys;
}
/**
 * Carica in un dictionary l'elenco delle tipologie di Voce Economico
 * Patrimoniale.
 * @param bulk L' OggettoBulk in uso.
 * @return ti_voce_ep_Keys I valori delle tipologie di voce economico 
 *						   patrimoniale.
 */
public Dictionary loadTi_voce_ep_Keys( OggettoBulk bulk) 
{
		Hashtable ti_voce_ep_Keys = new java.util.Hashtable();
		// ti_voce_ep_Keys.put("G", "Gruppo");
		ti_voce_ep_Keys.put("P", "Capoconto");
		ti_voce_ep_Keys.put("C", "Conto");
	return ti_voce_ep_Keys;
}
}
