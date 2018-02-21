package it.cnr.contab.doccont00.intcass.bulk;

import java.math.*;
import java.sql.*;
import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Distinta_cassiereHome extends BulkHome {
public Distinta_cassiereHome(java.sql.Connection conn) {
	super(Distinta_cassiereBulk.class,conn);
}
public Distinta_cassiereHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Distinta_cassiereBulk.class,conn,persistentCache);
}
public BigDecimal calcolaTotMandati( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}

public BigDecimal calcolaTotMandatiAccreditamento( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_ACCREDITAMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiAccreditamentoAnnullati( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_ACCREDITAMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiAccreditamentoAnnullatiRitrasmessi( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_ACCREDITAMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiAnnullati( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiAnnullatiRitrasmessi( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiPagamento( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_PAGAMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiPagamentoAnnullati( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_PAGAMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiPagamentoAnnullatiRitrasmessi( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_PAGAMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiRegSospeso( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_REGOLAM_SOSPESO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiRegSospesoAnnullati( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_REGOLAM_SOSPESO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotMandatiRegSospesoAnnullatiRitrasmessi( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_MANDATO) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"MANDATO A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_MANDATO IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.TI_MANDATO = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_MANDATO = B.PG_MANDATO ",true,this.getClass());

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, MandatoBulk.TIPO_REGOLAM_SOSPESO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotReversali( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);

	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_REVERSALE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " + 
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_REVERSALE = B.PG_REVERSALE ",true ,this.getClass());
	

	/*
	
	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_DOCUMENTO_CONT) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE " );
	*/		
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
//		ps.setString( 5, Numerazione_doc_contBulk.TIPO_REV );
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotReversaliAnnullate( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);

	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_REVERSALE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_REVERSALE = B.PG_REVERSALE ",true ,this.getClass());
	
	/*
	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_DOCUMENTO_CONT) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE " );
	*/		
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
//		ps.setString( 5, Numerazione_doc_contBulk.TIPO_REV );

		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotReversaliAnnullateRitrasmesse( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);

	LoggableStatement ps = new LoggableStatement(getConnection(),
		"SELECT SUM(IM_REVERSALE) FROM " + 
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
		"REVERSALE A, DISTINTA_CASSIERE_DET B " +
		"WHERE B.ESERCIZIO = ? AND " +
		"B.CD_CDS = ? AND " +
		"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
		"B.PG_DISTINTA = ? AND " +
		"B.PG_REVERSALE IS NOT NULL AND " +						
		"A.DT_ANNULLAMENTO IS NOT NULL AND " +
		"A.DT_TRASMISSIONE IS NOT NULL AND " +
		"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
		"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
		"A.ESERCIZIO = B.ESERCIZIO AND " +
		"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE ",true ,this.getClass());							
	
	
/*	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_DOCUMENTO_CONT) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE " ); 							*/
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
//		ps.setString( 5, Numerazione_doc_contBulk.TIPO_REV );
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
/* sostituiesce le 6 query calcolaTotReversaleRegSospesoXXXXXXXX */
public Distinta_cassiereBulk calcolaTotReversaliRegSospeso( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal tot = new BigDecimal(0);
	BigDecimal tot_ann = new BigDecimal(0);
	BigDecimal tot_ann_rit = new BigDecimal(0);
	String ti_cc_bi;

	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT TI_CC_BI, IM_TOT, IM_TOT_ANN, IM_TOT_ANN_RIT FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_TOT_DISTINTA_REV A " +
			"WHERE " +
			" A.ESERCIZIO = ? AND " +
			" A.CD_CDS = ? AND " +
			" A.CD_UNITA_ORGANIZZATIVA = ? AND " +
			" A.PG_DISTINTA = ? " ,true,this.getClass());
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ResultSet rs = ps.executeQuery();
		// il result set può conetenere al massimo 2 record 1 per ti_cc_bi = 'C' e uno per ti_cc_bi = 'B'
		try
		{
			while ( rs.next() )
			{
				ti_cc_bi =  rs.getString( 1 );
				tot = rs.getBigDecimal( 2 );
				tot_ann = rs.getBigDecimal( 3 );
				tot_ann_rit = rs.getBigDecimal( 4 );				
				if ( SospesoBulk.TIPO_CC.equals( ti_cc_bi ) )
				{
					distinta.setTotReversaliRegSospesoCC( tot );
					distinta.setTotReversaliRegSospesoCCAnnullate( tot_ann );
					distinta.setTotReversaliRegSospesoCCAnnullateRitrasmesse( tot_ann_rit );
				}
				else if ( SospesoBulk.TIPO_BANCA_ITALIA.equals( ti_cc_bi ) )
				{
					distinta.setTotReversaliRegSospesoBI( tot );
					distinta.setTotReversaliRegSospesoBIAnnullate( tot_ann );
					distinta.setTotReversaliRegSospesoBIAnnullateRitrasmesse( tot_ann_rit );
					
				}
			}	
				
			return distinta;
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
/* !!!!!!!!!!!!!!!!non più usato !!!!!!!!!!!!!!!!!!!!!*/
/* sostituito da 'calcolaTotReversaliRegSospeso'      */
/*
public BigDecimal calcolaTotReversaliRegSospesoBI( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);

	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_DOCUMENTO_CONT) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A " +
			"WHERE " +
			" A.ESERCIZIO = ? AND " +
			" A.CD_CDS = ? AND " +
			" A.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			" EXISTS ( " +
			" SELECT 1 FROM DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.TI_DOCUMENTO_CONT = ? AND " +	
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.TI_CC_BI = ? AND " +										
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE )" );
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getEsercizio());
		ps.setString( 5, distinta.getCd_cds());
		ps.setString( 6, distinta.getCd_unita_organizzativa());
		ps.setObject( 7, distinta.getPg_distinta());
		ps.setString( 8, ReversaleBulk.TIPO_REGOLAM_SOSPESO);
		ps.setString( 9, Numerazione_doc_contBulk.TIPO_REV );
		ps.setString( 10, SospesoBulk.TIPO_BANCA_ITALIA );				
		ResultSet rs = LoggableStatement.executeQuery(ps);
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
*/
/* !!!!!!!!!!!!!!!!non più usato !!!!!!!!!!!!!!!!!!!!!*/
/* sostituito da 'calcolaTotReversaliRegSospeso'      */
/*
public BigDecimal calcolaTotReversaliRegSospesoBIAnnullate( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);

	java.sql.PreparedStatement ps = getConnection().prepareStatement(
		"SELECT SUM(IM_DOCUMENTO_CONT) FROM " + 
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
		"V_MANDATO_REVERSALE A " +
			"WHERE " +
			" A.ESERCIZIO = ? AND " +
			" A.CD_CDS = ? AND " +
			" A.CD_UNITA_ORGANIZZATIVA = ? AND " +						
		"EXISTS ( " +
		"SELECT 1 FROM DISTINTA_CASSIERE_DET B " +
		"WHERE B.ESERCIZIO = ? AND " +
		"B.CD_CDS = ? AND " +
		"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
		"B.PG_DISTINTA = ? AND " +
		"B.PG_REVERSALE IS NOT NULL AND " +						
		"A.DT_ANNULLAMENTO IS NOT NULL AND " +
		"(A.DT_TRASMISSIONE IS NULL OR " +
		"(A.DT_TRASMISSIONE IS NOT NULL AND " +
		"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
		"A.TI_DOCUMENTO_CONT = ? AND " +	
		"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
		"A.TI_CC_BI = ? AND " +										
		"A.CD_CDS = B.CD_CDS AND " +
		"A.ESERCIZIO = B.ESERCIZIO AND " +
		"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE )" );

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getEsercizio());
		ps.setString( 5, distinta.getCd_cds());
		ps.setString( 6, distinta.getCd_unita_organizzativa());
		ps.setObject( 7, distinta.getPg_distinta());
		ps.setString( 8, ReversaleBulk.TIPO_REGOLAM_SOSPESO);
		ps.setString( 9, Numerazione_doc_contBulk.TIPO_REV );
		ps.setString( 10, SospesoBulk.TIPO_BANCA_ITALIA );				
		ResultSet rs = LoggableStatement.executeQuery(ps);
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
*/
/* !!!!!!!!!!!!!!!!non più usato !!!!!!!!!!!!!!!!!!!!!*/
/* sostituito da 'calcolaTotReversaliRegSospeso'      */
/*
public BigDecimal calcolaTotReversaliRegSospesoBIAnnullateRitrasmesse( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);

	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_DOCUMENTO_CONT) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A " +
			"WHERE " +
			" A.ESERCIZIO = ? AND " +
			" A.CD_CDS = ? AND " +
			" A.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"EXISTS ( SELECT 1 FROM DISTINTA_CASSIERE_DET B WHERE " +
			"B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.TI_DOCUMENTO_CONT = ? AND " +	
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.TI_CC_BI = ? AND " +										
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE )" );	
		
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getEsercizio());
		ps.setString( 5, distinta.getCd_cds());
		ps.setString( 6, distinta.getCd_unita_organizzativa());
		ps.setObject( 7, distinta.getPg_distinta());
		ps.setString( 8, ReversaleBulk.TIPO_REGOLAM_SOSPESO);
		ps.setString( 9, Numerazione_doc_contBulk.TIPO_REV );
		ps.setString( 10, SospesoBulk.TIPO_BANCA_ITALIA );				
		ResultSet rs = LoggableStatement.executeQuery(ps);
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
*/
/* !!!!!!!!!!!!!!!!non più usato !!!!!!!!!!!!!!!!!!!!!*/
/* sostituito da 'calcolaTotReversaliRegSospeso'      */
/*
public BigDecimal calcolaTotReversaliRegSospesoCC(Distinta_cassiereBulk distinta)
    throws SQLException {
    BigDecimal totale = new BigDecimal(0);

    java.sql.PreparedStatement ps =
        getConnection().prepareStatement(
            "SELECT SUM(IM_DOCUMENTO_CONT) FROM "
                + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                + "V_MANDATO_REVERSALE A "
                + "WHERE "
                + " A.ESERCIZIO = ? AND "
                + " A.CD_CDS = ? AND "
                + " A.CD_UNITA_ORGANIZZATIVA = ? AND "
                + "EXISTS ( SELECT 1 FROM DISTINTA_CASSIERE_DET B WHERE "
                + "B.ESERCIZIO = ? AND "
                + "B.CD_CDS = ? AND "
                + "B.CD_UNITA_ORGANIZZATIVA = ? AND "
                + "B.PG_DISTINTA = ? AND "
                + "B.PG_REVERSALE IS NOT NULL AND "
                + "A.DT_ANNULLAMENTO IS NULL AND "
                + "A.TI_DOCUMENTO_CONT = ? AND "
                + "A.CD_TIPO_DOCUMENTO_CONT = ? AND "
                + "A.TI_CC_BI = ? AND "
                + "A.CD_CDS = B.CD_CDS AND "
                + "A.ESERCIZIO = B.ESERCIZIO AND "
                + "A.PG_DOCUMENTO_CONT = B.PG_REVERSALE )");
    try {
        ps.setObject(1, distinta.getEsercizio());
        ps.setString(2, distinta.getCd_cds());
        ps.setString(3, distinta.getCd_unita_organizzativa());
        ps.setObject(4, distinta.getEsercizio());
        ps.setString(5, distinta.getCd_cds());
        ps.setString(6, distinta.getCd_unita_organizzativa());
        ps.setObject(7, distinta.getPg_distinta());
        ps.setString(8, ReversaleBulk.TIPO_REGOLAM_SOSPESO);
        ps.setString(9, Numerazione_doc_contBulk.TIPO_REV);
        ps.setString(10, SospesoBulk.TIPO_CC);
        ResultSet rs = LoggableStatement.executeQuery(ps);
        try {
            if (rs.next()) {
                totale = rs.getBigDecimal(1);
                if (totale != null)
                    return totale;
                else
                    return new BigDecimal(0);
            } else
                return new BigDecimal(0);
        } finally {
            try{rs.close();}catch( java.sql.SQLException e ){};
        }
    } finally {
        try{ps.close();}catch( java.sql.SQLException e ){};
    }
}
*/
/* !!!!!!!!!!!!!!!!non più usato !!!!!!!!!!!!!!!!!!!!!*/
/* sostituito da 'calcolaTotReversaliRegSospeso'      */
/*
public BigDecimal calcolaTotReversaliRegSospesoCCAnnullate( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_DOCUMENTO_CONT) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A "
                + "WHERE "
                + " A.ESERCIZIO = ? AND "
                + " A.CD_CDS = ? AND "
                + " A.CD_UNITA_ORGANIZZATIVA = ? AND " +
			"EXISTS ( SELECT 1 FROM DISTINTA_CASSIERE_DET B WHERE " +
			"B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.TI_DOCUMENTO_CONT = ? AND " +	
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.TI_CC_BI = ? AND " +										
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE )" );
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getEsercizio());
		ps.setString( 5, distinta.getCd_cds());
		ps.setString( 6, distinta.getCd_unita_organizzativa());
		ps.setObject( 7, distinta.getPg_distinta());
		ps.setString( 8, ReversaleBulk.TIPO_REGOLAM_SOSPESO);
		ps.setString( 9, Numerazione_doc_contBulk.TIPO_REV );
		ps.setString( 10, SospesoBulk.TIPO_CC );				
		ResultSet rs = LoggableStatement.executeQuery(ps);
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
*/
/* !!!!!!!!!!!!!!!!non più usato !!!!!!!!!!!!!!!!!!!!!*/
/* sostituito da 'calcolaTotReversaliRegSospeso'      */
/*
public BigDecimal calcolaTotReversaliRegSospesoCCAnnullateRitrasmesse( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);

	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_DOCUMENTO_CONT) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A "
                + "WHERE "
                + " A.ESERCIZIO = ? AND "
                + " A.CD_CDS = ? AND "
                + " A.CD_UNITA_ORGANIZZATIVA = ? AND " +
			"EXISTS ( SELECT 1 FROM DISTINTA_CASSIERE_DET B WHERE " +
			"B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.TI_DOCUMENTO_CONT = ? AND " +	
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.TI_CC_BI = ? AND " +										
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE )" );
	
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getEsercizio());
		ps.setString( 5, distinta.getCd_cds());
		ps.setString( 6, distinta.getCd_unita_organizzativa());
		ps.setObject( 7, distinta.getPg_distinta());
		ps.setString( 8, ReversaleBulk.TIPO_REGOLAM_SOSPESO);
		ps.setString( 9, Numerazione_doc_contBulk.TIPO_REV );
		ps.setString( 10, SospesoBulk.TIPO_CC );				
		ResultSet rs = LoggableStatement.executeQuery(ps);
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
*/
public BigDecimal calcolaTotReversaliRitenute( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);

	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_REVERSALE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.TI_REVERSALE = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_REVERSALE = B.PG_REVERSALE ",true,this.getClass());						
/*
	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_RITENUTE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +									
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE " );
*/			
	try
	{

		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, ReversaleBulk.TIPO_INCASSO);
//		ps.setString( 5, Numerazione_doc_contBulk.TIPO_REV );		

		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotReversaliRitenuteAnnullate( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	/*
	java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT SUM(IM_RITENUTE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE " );
	*/
	LoggableStatement ps = new LoggableStatement(getConnection(),	
			"SELECT SUM(IM_REVERSALE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.TI_REVERSALE = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_REVERSALE = B.PG_REVERSALE " ,true,this.getClass());						
	
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, ReversaleBulk.TIPO_INCASSO);		
//		ps.setString( 5, Numerazione_doc_contBulk.TIPO_REV );
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotReversaliRitenuteAnnullateRitrasmesse( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
					"SELECT SUM(IM_REVERSALE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.TI_REVERSALE = ? AND " +												
			"A.CD_CDS = B.CD_CDS_ORIGINE AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_REVERSALE = B.PG_REVERSALE " ,true,this.getClass());						
/*
			"SELECT SUM(IM_RITENUTE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"V_MANDATO_REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.CD_TIPO_DOCUMENTO_CONT = ? AND " +
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_DOCUMENTO_CONT = B.PG_REVERSALE " );
*/			
	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, ReversaleBulk.TIPO_INCASSO);		
//		ps.setString( 5, Numerazione_doc_contBulk.TIPO_REV );
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotReversaliTrasferimento( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_REVERSALE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NULL AND " +
			"A.TI_REVERSALE = ? AND " +												
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_REVERSALE = B.PG_REVERSALE " ,true,this.getClass());						

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, ReversaleBulk.TIPO_TRASFERIMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotReversaliTrasferimentoAnnullate( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_REVERSALE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"(A.DT_TRASMISSIONE IS NULL OR " +
			"(A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO < DT_TRASMISSIONE)) AND " +									
			"A.TI_REVERSALE = ? AND " +												
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_REVERSALE = B.PG_REVERSALE ",true ,this.getClass());						

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, ReversaleBulk.TIPO_TRASFERIMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public BigDecimal calcolaTotReversaliTrasferimentoAnnullateRitrasmesse( Distinta_cassiereBulk distinta) throws SQLException
{
	BigDecimal totale = new BigDecimal(0);
	
	LoggableStatement ps = new LoggableStatement(getConnection(),
			"SELECT SUM(IM_REVERSALE) FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"REVERSALE A, DISTINTA_CASSIERE_DET B " +
			"WHERE B.ESERCIZIO = ? AND " +
			"B.CD_CDS = ? AND " +
			"B.CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"B.PG_DISTINTA = ? AND " +
			"B.PG_REVERSALE IS NOT NULL AND " +						
			"A.DT_ANNULLAMENTO IS NOT NULL AND " +
			"A.DT_TRASMISSIONE IS NOT NULL AND " +
			"A.DT_ANNULLAMENTO > DT_TRASMISSIONE AND " +									
			"A.TI_REVERSALE = ? AND " +												
			"A.CD_CDS = B.CD_CDS AND " +
			"A.ESERCIZIO = B.ESERCIZIO AND " +
			"A.PG_REVERSALE = B.PG_REVERSALE ",true,this.getClass());							

	try
	{
		ps.setObject( 1, distinta.getEsercizio());
		ps.setString( 2, distinta.getCd_cds());
		ps.setString( 3, distinta.getCd_unita_organizzativa());
		ps.setObject( 4, distinta.getPg_distinta());
		ps.setString( 5, ReversaleBulk.TIPO_TRASFERIMENTO);
		ResultSet rs = ps.executeQuery();
		try
		{
			if ( rs.next() )
			{
				totale =  rs.getBigDecimal( 1 );
				if ( totale != null )
					return totale;
				else
					return new BigDecimal(0);
			}
			else
				return new BigDecimal(0);
		}
		finally 
		{	try{rs.close();}catch( java.sql.SQLException e ){}; }
	}
	finally
	{	
		try{ps.close();}catch( java.sql.SQLException e ){};
	}	
}
public Distinta_cassiereBulk findUltimaDistinta( Distinta_cassiereBulk distinta) throws PersistencyException, IntrospectionException
{
		SQLBuilder sql = createSQLBuilder();
		sql.setStatement(
			"SELECT * FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"DISTINTA_CASSIERE " +
			"WHERE ESERCIZIO = " + distinta.getEsercizio() + " AND " +
			"CD_CDS = '" + distinta.getCd_cds() + "' AND " +
			"CD_UNITA_ORGANIZZATIVA = '" + distinta.getCd_unita_organizzativa() + "' AND " +
			"PG_DISTINTA = ( SELECT MAX(PG_DISTINTA) " +			
			"FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"DISTINTA_CASSIERE " +
			"WHERE ESERCIZIO = " + distinta.getEsercizio() + " AND " +
			"CD_CDS = '" + distinta.getCd_cds() + "' AND " +
			"CD_UNITA_ORGANIZZATIVA = '" + distinta.getCd_unita_organizzativa() + "' ) " +
			"FOR UPDATE NOWAIT" );
		
		List result = fetchAll( sql );
		if ( result.size() > 0 )
			return (Distinta_cassiereBulk) result.get(0);
		return null;	
}
public Distinta_cassiereBulk findUltimaDistintaTrasmessa( Distinta_cassiereBulk distinta) throws PersistencyException, IntrospectionException
{
		SQLBuilder sql = createSQLBuilder();
		if(distinta.getPg_distinta()!=null){
		sql.setStatement(
			"SELECT * FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"DISTINTA_CASSIERE " +
			"WHERE ESERCIZIO = " + distinta.getEsercizio() + " AND " +
			"CD_CDS = '" + distinta.getCd_cds() + "' AND " +
			"CD_UNITA_ORGANIZZATIVA = '" + distinta.getCd_unita_organizzativa() + "' AND " +
			"DT_INVIO is not null AND " +									
			"PG_DISTINTA_DEF = ( SELECT MAX(PG_DISTINTA_DEF) " +			
			"FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"DISTINTA_CASSIERE " +
			"WHERE ESERCIZIO = " + distinta.getEsercizio() + " AND " +
			"CD_CDS = '" + distinta.getCd_cds() + "' AND " +
			"CD_UNITA_ORGANIZZATIVA = '" + distinta.getCd_unita_organizzativa() + "' AND " +
			"PG_DISTINTA !='"+distinta.getPg_distinta() +"' AND "+
			"DT_INVIO is not null ) " +												
			"FOR UPDATE NOWAIT" );
		}else{
			sql.setStatement(
					"SELECT * FROM " + 
					it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
					"DISTINTA_CASSIERE " +
					"WHERE ESERCIZIO = " + distinta.getEsercizio() + " AND " +
					"CD_CDS = '" + distinta.getCd_cds() + "' AND " +
					"CD_UNITA_ORGANIZZATIVA = '" + distinta.getCd_unita_organizzativa() + "' AND " +
					"DT_INVIO is not null AND " +									
					"PG_DISTINTA_DEF = ( SELECT MAX(PG_DISTINTA_DEF) " +			
					"FROM " + 
					it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
					"DISTINTA_CASSIERE " +
					"WHERE ESERCIZIO = " + distinta.getEsercizio() + " AND " +
					"CD_CDS = '" + distinta.getCd_cds() + "' AND " +
					"CD_UNITA_ORGANIZZATIVA = '" + distinta.getCd_unita_organizzativa() + "' AND " +
					"DT_INVIO is not null ) " +												
					"FOR UPDATE NOWAIT" );
		}
		List result = fetchAll( sql );
		if ( result.size() > 0 )
			return (Distinta_cassiereBulk) result.get(0);
		return null;	
}
	/**
	 * Imposta il distinta di un oggetto Distinta_cassiereBulk.
	 *
	 * @param accertamento OggettoBulk
	 *
	 * @exception PersistencyException
	 */

public void inizializzaProgressivo(it.cnr.jada.UserContext userContext,Distinta_cassiereBulk distinta) throws BusyResourceException, PersistencyException, ComponentException 
{
	Long result = (Long) findMax( distinta, "pg_distinta", new Long(0), true  );
	distinta.setPg_distinta( new Long (result.longValue() + 1) ) ;	
	/*
	try
	{
		java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT PG_DISTINTA FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"DISTINTA_CASSIERE " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_CDS = ? AND " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"PG_DISTINTA = ( SELECT MAX(PG_DISTINTA) " +			
			"FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"DISTINTA_CASSIERE " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_CDS = ? AND " +
			"CD_UNITA_ORGANIZZATIVA = ? ) " +						
			"FOR UPDATE NOWAIT" );

		try
		{
			ps.setObject( 1, distinta.getEsercizio());
			ps.setString( 2, distinta.getCd_cds());
			ps.setString( 3, distinta.getCd_unita_organizzativa());
			ps.setObject( 4, distinta.getEsercizio());
			ps.setString( 5, distinta.getCd_cds());
			ps.setString( 6, distinta.getCd_unita_organizzativa());
			
			ResultSet rs = LoggableStatement.executeQuery(ps);
			try
			{
				if ( rs.next() )
				{
					long pg =  rs.getLong( 1 );
					if ( rs != null )
						distinta.setPg_distinta( new Long(pg +1) );
					else
						distinta.setPg_distinta( new Long(1) );
				}
				else
					distinta.setPg_distinta( new Long(1) );
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};			
			}		
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};			
		}		

	} catch ( Throwable e ) {
		throw new PersistencyException( e );
	}
	*/
}
	/**
	 * Imposta il progressivo definitivo di un oggetto Distinta_cassiereBulk.
	 *
	 * @param distinta Distinta_cassiereBulk
	 *
	 * @exception PersistencyException
	 */

public void inizializzaProgressivoCassiere(it.cnr.jada.UserContext userContext,Distinta_cassiereBulk distinta) throws PersistencyException, ComponentException , BusyResourceException
{
	//reversali di trasferimento con + distinte collegate assegnavano + volte il prog def
	if(distinta.getPg_distinta_def()==null){
		Distinta_cassiereBulk tmp = new Distinta_cassiereBulk();
		/* attenzione e' necessario creare un'istanza fittizia di distinta (tmp) perchè altrimenti verrebbero aggiunta
		   la clausole sul pg_distinta in qunato attributo chiave */
		tmp.setEsercizio( distinta.getEsercizio());
		tmp.setCd_cds( distinta.getCd_cds());
		tmp.setCd_unita_organizzativa( distinta.getCd_unita_organizzativa());
		Long result = (Long) findMax( tmp, "pg_distinta_def", new Long(0), true  );
		
		distinta.setPg_distinta_def( new Long (result.longValue() + 1) ) ;	
	}
	/*
	try
	{
		java.sql.PreparedStatement ps = getConnection().prepareStatement(
			"SELECT PG_DISTINTA_DEF FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"DISTINTA_CASSIERE " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_CDS = ? AND " +
			"CD_UNITA_ORGANIZZATIVA = ? AND " +						
			"PG_DISTINTA_DEF = ( SELECT MAX(PG_DISTINTA_DEF) " +			
			"FROM " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
			"DISTINTA_CASSIERE " +
			"WHERE ESERCIZIO = ? AND " +
			"CD_CDS = ? AND " +
			"CD_UNITA_ORGANIZZATIVA = ? ) " +						
			"FOR UPDATE NOWAIT" );

		try
		{
			ps.setObject( 1, distinta.getEsercizio());
			ps.setString( 2, distinta.getCd_cds());
			ps.setString( 3, distinta.getCd_unita_organizzativa());
			ps.setObject( 4, distinta.getEsercizio());
			ps.setString( 5, distinta.getCd_cds());
			ps.setString( 6, distinta.getCd_unita_organizzativa());
			
			ResultSet rs = LoggableStatement.executeQuery(ps);
			try
			{
				if ( rs.next() )
				{
					long pg =  rs.getLong( 1 );
					if ( rs != null )
						distinta.setPg_distinta_def( new Long(pg +1) );
					else
						distinta.setPg_distinta_def( new Long(1) );
				}
				else
					distinta.setPg_distinta_def( new Long(1) );
			}
			finally
			{	try{rs.close();}catch( java.sql.SQLException e ){}; }
			
		}
		finally
		{	try{ps.close();}catch( java.sql.SQLException e ){}; }
	} catch ( Throwable e ) {
		throw new PersistencyException( e );
	}
	*/

}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param distinta	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findDistintaCasserieDettagli( Distinta_cassiereBulk distinta ) throws IntrospectionException,PersistencyException 
{
	PersistentHome home = getHomeCache().getHome(Distinta_cassiere_detBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND","esercizio",SQLBuilder.EQUALS, distinta.getEsercizio());
	sql.addClause("AND","cd_cds",SQLBuilder.EQUALS, distinta.getCd_cds());
	sql.addClause("AND","cd_unita_organizzativa",SQLBuilder.EQUALS, distinta.getCd_unita_organizzativa());
	sql.addClause("AND","pg_distinta",SQLBuilder.EQUALS, distinta.getPg_distinta());
	return home.fetchAll(sql);
}
}
