package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Insieme_laBase extends Insieme_laKey implements Keyed {
	// DS_INSIEME_LA VARCHAR(200) NOT NULL
	private java.lang.String ds_insieme_la;

public Insieme_laBase() {
	super();
}
public Insieme_laBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_insieme_la) {
	super(cd_centro_responsabilita,cd_insieme_la);
}
/* 
 * Getter dell'attributo ds_insieme_la
 */
public java.lang.String getDs_insieme_la() {
	return ds_insieme_la;
}
/* 
 * Setter dell'attributo ds_insieme_la
 */
public void setDs_insieme_la(java.lang.String ds_insieme_la) {
	this.ds_insieme_la = ds_insieme_la;
}
}
