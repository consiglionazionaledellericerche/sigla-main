package it.cnr.contab.compensi00.docs.bulk;

import java.util.List;

import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class MinicarrieraHome extends BulkHome {
public MinicarrieraHome(java.sql.Connection conn) {
	super(MinicarrieraBulk.class,conn);
}
public MinicarrieraHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(MinicarrieraBulk.class,conn,persistentCache);
}
public java.util.List findMinicarrieraIncaricoList( it.cnr.jada.UserContext userContext,Incarichi_repertorioBulk incarico ) throws IntrospectionException,PersistencyException 
{
	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO_REP",SQLBuilder.EQUALS, incarico.getEsercizio());
	sql.addSQLClause("AND","PG_REPERTORIO",SQLBuilder.EQUALS, incarico.getPg_repertorio());
	List l =  fetchAll(sql);
	getHomeCache().fetchAll(userContext);
	return l;
}
}
