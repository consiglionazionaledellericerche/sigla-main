package it.cnr.contab.docamm00.tabrif.bulk;
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
	    if (cgi.getAss_voce_f()==null)
			throw new it.cnr.jada.comp.ComponentException("Operazione non valida.");
        sql.addTableToHeader("CATEGORIA_GRUPPO_VOCE");
        sql.addSQLJoin("CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO", "CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO");
        sql.addSQLClause("AND", "CATEGORIA_GRUPPO_VOCE.TI_APPARTENENZA", sql.EQUALS, cgi.getAss_voce_f().getTi_appartenenza());
        sql.addSQLClause("AND", "CATEGORIA_GRUPPO_VOCE.TI_GESTIONE", sql.EQUALS, cgi.getAss_voce_f().getTi_gestione());
        sql.addSQLClause("AND", "CATEGORIA_GRUPPO_VOCE.ESERCIZIO", sql.EQUALS, cgi.getAss_voce_f().getEsercizio());
        sql.addSQLClause("AND", "CATEGORIA_GRUPPO_VOCE.CD_ELEMENTO_VOCE", sql.EQUALS, cgi.getAss_voce_f().getCd_voce());
    }
    sql.addOrderBy("CD_CATEGORIA_GRUPPO");
    return sql;
}
}
