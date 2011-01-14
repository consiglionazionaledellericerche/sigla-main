package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_inquadramentoBase extends Gruppo_inquadramentoKey implements Keyed {
	// DS_GRUPPO_INQUADRAMENTO VARCHAR(200) NOT NULL
	private java.lang.String ds_gruppo_inquadramento;

	// FL_DEFAULT CHAR(1) NOT NULL
	private java.lang.Boolean fl_default;

public Gruppo_inquadramentoBase() {
	super();
}
public Gruppo_inquadramentoBase(java.lang.String cd_gruppo_inquadramento) {
	super(cd_gruppo_inquadramento);
}
/* 
 * Getter dell'attributo ds_gruppo_inquadramento
 */
public java.lang.String getDs_gruppo_inquadramento() {
	return ds_gruppo_inquadramento;
}
/* 
 * Getter dell'attributo fl_default
 */
public java.lang.Boolean getFl_default() {
	return fl_default;
}
/* 
 * Setter dell'attributo ds_gruppo_inquadramento
 */
public void setDs_gruppo_inquadramento(java.lang.String ds_gruppo_inquadramento) {
	this.ds_gruppo_inquadramento = ds_gruppo_inquadramento;
}
/* 
 * Setter dell'attributo fl_default
 */
public void setFl_default(java.lang.Boolean fl_default) {
	this.fl_default = fl_default;
}
}
