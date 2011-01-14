package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.utenze00.bp.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

public class V_distinta_cass_im_man_revHome extends BulkHome {
public V_distinta_cass_im_man_revHome(java.sql.Connection conn) {
	super(V_distinta_cass_im_man_revBulk.class,conn);
}
public V_distinta_cass_im_man_revHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_distinta_cass_im_man_revBulk.class,conn,persistentCache);
}
}
