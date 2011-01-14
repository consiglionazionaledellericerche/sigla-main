package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fattura_attivaHome extends BulkHome {
protected Fattura_attivaHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Fattura_attivaHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
public Fattura_attivaHome(java.sql.Connection conn) {
	super(Fattura_attivaBulk.class,conn);
}
public Fattura_attivaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fattura_attivaBulk.class,conn,persistentCache);
}
/**
 * Inizializza la chiave primaria di un OggettoBulk per un
 * inserimento. Da usare principalmente per riempire i progressivi
 * automatici.
 * @param bulk l'OggettoBulk da inizializzare  
 */
public java.sql.Timestamp findForMaxDataRegistrazione(it.cnr.jada.UserContext userContext,Fattura_attivaBulk fattura) throws PersistencyException,it.cnr.jada.comp.ComponentException {

	if (fattura == null) return null;
	try {
		java.sql.Connection contact = getConnection();
		String query="SELECT MAX(DT_REGISTRAZIONE) FROM "+
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "FATTURA_ATTIVA " + 
		"WHERE CD_UO_ORIGINE= '"+fattura.getCd_uo_origine()+"'";

		if (fattura.getSezionale()!=null && fattura.getSezionale().getCd_tipo_sezionale()!=null)
			query=query+"AND CD_TIPO_SEZIONALE= '"+fattura.getSezionale().getCd_tipo_sezionale()+"' ";
		
		java.sql.ResultSet rs = contact.createStatement().executeQuery(query);

		if(rs.next())
			return rs.getTimestamp(1);
		else
			return null;
	} catch(java.sql.SQLException sqle) {
		throw new PersistencyException(sqle);
	}
}
}
