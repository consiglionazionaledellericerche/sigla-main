package it.cnr.contab.progettiric00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_progettoBase extends Tipo_progettoKey implements Keyed {
	// DS_TIPO_PROGETTO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_progetto;

public Tipo_progettoBase() {
	super();
}
public Tipo_progettoBase(java.lang.String cd_tipo_progetto) {
	super(cd_tipo_progetto);
}
/* 
 * Getter dell'attributo ds_tipo_progetto
 */
public java.lang.String getDs_tipo_progetto() {
	return ds_tipo_progetto;
}
/* 
 * Setter dell'attributo ds_tipo_progetto
 */
public void setDs_tipo_progetto(java.lang.String ds_tipo_progetto) {
	this.ds_tipo_progetto = ds_tipo_progetto;
}
}
