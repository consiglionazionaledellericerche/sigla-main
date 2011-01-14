package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RegioneHome extends BulkHome {
public RegioneHome(java.sql.Connection conn) {
	super(RegioneBulk.class,conn);
}
public RegioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(RegioneBulk.class,conn,persistentCache);
}
/**
  *	Viene caricata da db la REGIONE di appartenenza del terzo
  *	associato all'Unita Organizzativa <cdUnitaOrganizzativa>
  *
**/
public RegioneBulk loadRegioneIRAPDefault(String cdUnitaOrganizzativa) throws java.sql.SQLException, PersistencyException {

	String codiceReg = "*";
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	String str = "SELECT D.CD_REGIONE FROM " +
					schema + "TERZO A, " + 
					schema + "COMUNE B," + 
					schema + "PROVINCIA C, " + 
					schema + "REGIONE D " +
				"WHERE A.CD_UNITA_ORGANIZZATIVA = ? AND " +
				"B.PG_COMUNE = A.PG_COMUNE_SEDE AND " +
				"C.CD_PROVINCIA = B.CD_PROVINCIA AND " +
				"D.CD_REGIONE = C.CD_REGIONE ";

	LoggableStatement ps = new LoggableStatement(getConnection(),str,true,this.getClass());
	try {
		ps.setString( 1, cdUnitaOrganizzativa);
		java.sql.ResultSet rs = ps.executeQuery();
		try {
			if(rs.next())
				codiceReg = rs.getString(1);
		}finally{
			try{rs.close();}catch( java.sql.SQLException e ){};
		} 
	}finally{
		try{ps.close();}catch( java.sql.SQLException e ){};
	}

	return (RegioneBulk)findByPrimaryKey(new RegioneBulk(codiceReg));
}
}
