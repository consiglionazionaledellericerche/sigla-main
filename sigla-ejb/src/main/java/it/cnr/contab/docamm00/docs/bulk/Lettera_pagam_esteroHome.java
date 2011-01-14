package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Lettera_pagam_esteroHome extends BulkHome {
public Lettera_pagam_esteroHome(java.sql.Connection conn) {
	super(Lettera_pagam_esteroBulk.class,conn);
}
public Lettera_pagam_esteroHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Lettera_pagam_esteroBulk.class,conn,persistentCache);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) 
	throws PersistencyException {
		
	try	{
		Lettera_pagam_esteroBulk lettera = (Lettera_pagam_esteroBulk) bulk;
		ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
		lettera.setPg_lettera(progressiviSession.getNextPG(userContext, new Numerazione_doc_ammBulk(lettera)));
	} catch ( Throwable e )	{
		throw new PersistencyException( e );
	}
}
/**
 * Insert the method's description here.
 * Creation date: (2/22/2002 5:25:01 PM)
 */
public void newMethod() {}
}
