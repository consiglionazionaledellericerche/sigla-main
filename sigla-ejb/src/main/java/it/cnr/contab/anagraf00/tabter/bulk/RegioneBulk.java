package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi ai comuni esteri nella tabella Regione
 */

public class RegioneBulk extends RegioneBase {

/**
 * 
 */
public RegioneBulk() {}
public RegioneBulk(java.lang.String cd_regione) {
	super(cd_regione);
}
}
