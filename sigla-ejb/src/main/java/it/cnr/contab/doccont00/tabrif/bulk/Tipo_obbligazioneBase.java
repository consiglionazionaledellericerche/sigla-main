package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_obbligazioneBase extends Tipo_obbligazioneKey implements Keyed {
	// DS_TIPO_OBBLIGAZIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_obbligazione;

public Tipo_obbligazioneBase() {
	super();
}
public Tipo_obbligazioneBase(java.lang.String cd_tipo_obbligazione) {
	super(cd_tipo_obbligazione);
}
/* 
 * Getter dell'attributo ds_tipo_obbligazione
 */
public java.lang.String getDs_tipo_obbligazione() {
	return ds_tipo_obbligazione;
}
/* 
 * Setter dell'attributo ds_tipo_obbligazione
 */
public void setDs_tipo_obbligazione(java.lang.String ds_tipo_obbligazione) {
	this.ds_tipo_obbligazione = ds_tipo_obbligazione;
}
}
