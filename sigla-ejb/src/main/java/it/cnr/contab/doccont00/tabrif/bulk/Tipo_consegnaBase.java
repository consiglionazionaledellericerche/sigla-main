package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_consegnaBase extends Tipo_consegnaKey implements Keyed {
	// DS_TIPO_CONSEGNA VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_consegna;

public Tipo_consegnaBase() {
	super();
}
public Tipo_consegnaBase(java.lang.String cd_tipo_consegna) {
	super(cd_tipo_consegna);
}
/* 
 * Getter dell'attributo ds_tipo_consegna
 */
public java.lang.String getDs_tipo_consegna() {
	return ds_tipo_consegna;
}
/* 
 * Setter dell'attributo ds_tipo_consegna
 */
public void setDs_tipo_consegna(java.lang.String ds_tipo_consegna) {
	this.ds_tipo_consegna = ds_tipo_consegna;
}
}
