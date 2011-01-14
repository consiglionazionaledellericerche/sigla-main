package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Codici_attivita_inpsBase extends Codici_attivita_inpsKey implements Keyed {
	// DS_ATTIVITA_INPS VARCHAR(200) NOT NULL
	private java.lang.String ds_attivita_inps;

	// FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;

public Codici_attivita_inpsBase() {
	super();
}
public Codici_attivita_inpsBase(java.lang.String cd_attivita_inps) {
	super(cd_attivita_inps);
}
/* 
 * Getter dell'attributo ds_attivita_inps
 */
public java.lang.String getDs_attivita_inps() {
	return ds_attivita_inps;
}
/* 
 * Getter dell'attributo fl_cancellato
 */
public java.lang.Boolean getFl_cancellato() {
	return fl_cancellato;
}
/* 
 * Setter dell'attributo ds_attivita_inps
 */
public void setDs_attivita_inps(java.lang.String ds_attivita_inps) {
	this.ds_attivita_inps = ds_attivita_inps;
}
/* 
 * Setter dell'attributo fl_cancellato
 */
public void setFl_cancellato(java.lang.Boolean fl_cancellato) {
	this.fl_cancellato = fl_cancellato;
}
}
