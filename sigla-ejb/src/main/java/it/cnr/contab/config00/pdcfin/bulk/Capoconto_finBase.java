package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Capoconto_finBase extends Capoconto_finKey implements Keyed {
	// DS_CAPOCONTO_FIN VARCHAR(300)
	private java.lang.String ds_capoconto_fin;

public Capoconto_finBase() {
	super();
}
public Capoconto_finBase(java.lang.String cd_capoconto_fin) {
	super(cd_capoconto_fin);
}
/* 
 * Getter dell'attributo ds_capoconto_fin
 */
public java.lang.String getDs_capoconto_fin() {
	return ds_capoconto_fin;
}
/* 
 * Setter dell'attributo ds_capoconto_fin
 */
public void setDs_capoconto_fin(java.lang.String ds_capoconto_fin) {
	this.ds_capoconto_fin = ds_capoconto_fin;
}
}
