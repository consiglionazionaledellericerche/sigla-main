package it.cnr.contab.config00.blob.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class PostItHome extends BulkHome {
public PostItHome(java.sql.Connection conn) {
	super(PostItBulk.class,conn);
}
public PostItHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(PostItBulk.class,conn,persistentCache);
}

public Integer getMaxId() throws PersistencyException, IntrospectionException {
	
Integer max = new Integer (0);
String query="select CNRSEQ00_ID_POSTIT.nextval from dual";

java.sql.ResultSet rs = null;
	try {
		java.sql.Connection contact = getConnection();
		rs = contact.createStatement().executeQuery(query);	

		if(rs.next()){
			max = new Integer(rs.getInt(1));
			return max;
		}		
	} catch(java.sql.SQLException sqle) {
		throw new PersistencyException(sqle);
	} finally {
		if (rs!=null)
				try{rs.close();}catch( java.sql.SQLException e ){};
	}
return max;
}
public Integer getMaxId(PostItBulk postit)throws PersistencyException, IntrospectionException {
	PostItBulk post=new PostItBulk();
	post.setCd_centro_responsabilita(postit.getCd_centro_responsabilita());
	post.setCd_linea_attivita(postit.getCd_linea_attivita());
	post.setPg_progetto(postit.getPg_progetto());
	return (Integer)findMax(post,"id");
}

}
