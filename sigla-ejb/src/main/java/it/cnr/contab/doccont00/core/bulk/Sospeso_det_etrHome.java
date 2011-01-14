package it.cnr.contab.doccont00.core.bulk;

import java.sql.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.math.*;

public class Sospeso_det_etrHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Sospeso_det_etrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Sospeso_det_etrHome(java.sql.Connection conn) {
	super(Sospeso_det_etrBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Sospeso_det_etrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Sospeso_det_etrHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Sospeso_det_etrBulk.class,conn,persistentCache);
}
/*
 * Calcola la somma degli importi dei dettagli della reversale associati al riscontro.
*/
public BigDecimal calcolaTotDettagli( V_mandato_reversaleBulk man_rev ) throws it.cnr.jada.persistency.PersistencyException 
{
	try
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM( IM_ASSOCIATO ) " +			
			"FROM " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"SOSPESO_DET_ETR WHERE " +
			"ESERCIZIO = ? AND CD_CDS = ? AND PG_REVERSALE = ? AND STATO = ? AND TI_SOSPESO_RISCONTRO = ?" ,
			true,this.getClass());
		try
		{
			ps.setObject( 1, man_rev.getEsercizio() );
			ps.setString( 2, man_rev.getCd_cds() );
			ps.setObject( 3, man_rev.getPg_documento_cont() );
			ps.setString( 4, Sospeso_det_etrBulk.STATO_DEFAULT );
			ps.setString( 5, SospesoBulk.TI_RISCONTRO );
		
			ResultSet rs = ps.executeQuery();
			BigDecimal result; 
			try
			{
				if(rs.next())
				{
					result =  rs.getBigDecimal(1);
					if ( result == null )
						result = new BigDecimal(0);
				}		
				else
					result =  new BigDecimal(0);
				return result;	
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
}
