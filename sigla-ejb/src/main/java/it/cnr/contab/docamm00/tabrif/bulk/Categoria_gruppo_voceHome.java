package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Categoria_gruppo_voceHome extends BulkHome {
public Categoria_gruppo_voceHome(java.sql.Connection conn) {
	super(Categoria_gruppo_voceBulk.class,conn);
}
public Categoria_gruppo_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Categoria_gruppo_voceBulk.class,conn,persistentCache);
}
public Categoria_gruppo_voceBulk findCategoria_gruppo_voceforvoce(Elemento_voceBulk elem) throws PersistencyException{
	it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();
    sql.addTableToHeader("CATEGORIA_GRUPPO_INVENT");
    sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO","CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO");
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,elem.getEsercizio());
	sql.addSQLClause("AND","TI_APPARTENENZA",sql.EQUALS,elem.getTi_appartenenza());
	sql.addSQLClause("AND","TI_GESTIONE",sql.EQUALS,elem.getTi_gestione());
	sql.addSQLClause("AND","CD_ELEMENTO_VOCE",sql.EQUALS,elem.getCd_elemento_voce());
	//sql.addSQLClause("AND","CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_PADRE",sql.ISNULL,null);
	java.util.List lista = fetchAll(sql);
	if ( lista.size()>0 )
		return (Categoria_gruppo_voceBulk)lista.get(0);
	else
		return null;
	
}

}
