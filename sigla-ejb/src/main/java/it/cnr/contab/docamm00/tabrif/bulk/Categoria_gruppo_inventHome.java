package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Categoria_gruppo_inventHome extends BulkHome {
public Categoria_gruppo_inventHome(java.sql.Connection conn) {
	super(Categoria_gruppo_inventBulk.class,conn);
}
public Categoria_gruppo_inventHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Categoria_gruppo_inventBulk.class,conn,persistentCache);
}

/**
 * Insert the method's description here.
 * Creation date: (28/11/2001 11.23.36)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 * @param bulk it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */
public Categoria_gruppo_inventBulk getParent(Categoria_gruppo_inventBulk bulk) throws PersistencyException, IntrospectionException{

	if (bulk == null)
		return null;
	
	SQLBuilder sql = createSQLBuilder();

	sql.addSQLClause("AND","CD_CATEGORIA_GRUPPO",sql.EQUALS,bulk.getCd_categoria_gruppo());

	java.util.Collection coll = this.fetchAll(sql);
	if (coll.size() != 1)
		return null;

	return (Categoria_gruppo_inventBulk)coll.iterator().next();
}

/**
 * Recupera i figli dell'oggetto bulk
 * Creation date: (28/11/2001 10.57.42)
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 * @param bulk it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */

public SQLBuilder selectChildrenFor(it.cnr.jada.UserContext aUC, Categoria_gruppo_inventBulk cgi) throws it.cnr.jada.comp.ComponentException{

    SQLBuilder sql= createSQLBuilder();

    sql.addSQLClause("AND", "LIVELLO", sql.EQUALS, "0");
    if (cgi == null)
        sql.addSQLClause("AND", "CD_CATEGORIA_PADRE", sql.ISNULL, null);
    else {
	    if (cgi.getCd_categoria_padre()!=null)
	    	sql.addSQLClause("AND","CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO",sql.EQUALS,cgi.getCd_categoria_padre());
	    if (cgi.getAssociazioneVoci()==null)
			throw new it.cnr.jada.comp.ComponentException("Operazione non valida.");
    }
    sql.addOrderBy("CD_CATEGORIA_GRUPPO");
    return sql;
}
public java.util.Collection findAssociazioneVoci(UserContext usercontext, Categoria_gruppo_inventBulk testata) throws IntrospectionException, PersistencyException {
	PersistentHome dettHome = getHomeCache().getHome(Categoria_gruppo_voceBulk.class);
	SQLBuilder sql = dettHome.createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
	sql.addSQLClause("AND","CD_CATEGORIA_GRUPPO",sql.EQUALS,testata.getCd_categoria_gruppo());
	sql.addOrderBy("CD_ELEMENTO_VOCE");
	return dettHome.fetchAll(sql);
}	

}
