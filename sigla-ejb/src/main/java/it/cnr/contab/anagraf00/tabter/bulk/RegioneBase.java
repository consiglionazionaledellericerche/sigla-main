package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RegioneBase extends RegioneKey implements Keyed {
	// DS_REGIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_regione;

public RegioneBase() {
	super();
}
public RegioneBase(java.lang.String cd_regione) {
	super(cd_regione);
}
/* 
 * Getter dell'attributo ds_regione
 */
public java.lang.String getDs_regione() {
	return ds_regione;
}
/* 
 * Setter dell'attributo ds_regione
 */
public void setDs_regione(java.lang.String ds_regione) {
	this.ds_regione = ds_regione;
}
private java.lang.Boolean fl_addreg_aliqmax;

public java.lang.Boolean getFl_addreg_aliqmax() {
	return fl_addreg_aliqmax;
}
public void setFl_addreg_aliqmax(java.lang.Boolean fl_addreg_aliqmax) {
	this.fl_addreg_aliqmax = fl_addreg_aliqmax;
}
}
