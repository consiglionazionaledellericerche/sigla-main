package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AccessoBase extends AccessoKey implements Keyed {
	// DS_ACCESSO VARCHAR(200)
	private java.lang.String ds_accesso;

	// TI_ACCESSO CHAR(1)
	private java.lang.String ti_accesso;

public AccessoBase() {
	super();
}
public AccessoBase(java.lang.String cd_accesso) {
	super(cd_accesso);
}
/* 
 * Getter dell'attributo ds_accesso
 */
public java.lang.String getDs_accesso() {
	return ds_accesso;
}
/* 
 * Getter dell'attributo ti_accesso
 */
public java.lang.String getTi_accesso() {
	return ti_accesso;
}
/* 
 * Setter dell'attributo ds_accesso
 */
public void setDs_accesso(java.lang.String ds_accesso) {
	this.ds_accesso = ds_accesso;
}
/* 
 * Setter dell'attributo ti_accesso
 */
public void setTi_accesso(java.lang.String ti_accesso) {
	this.ti_accesso = ti_accesso;
}
}
