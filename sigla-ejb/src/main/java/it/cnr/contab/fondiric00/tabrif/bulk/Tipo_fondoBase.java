package it.cnr.contab.fondiric00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_fondoBase extends Tipo_fondoKey implements Keyed {
	// DS_TIPO_FONDO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_fondo;

public Tipo_fondoBase() {
	super();
}
public Tipo_fondoBase(java.lang.String cd_tipo_fondo) {
	super(cd_tipo_fondo);
}
/* 
 * Getter dell'attributo ds_tipo_fondo
 */
public java.lang.String getDs_tipo_fondo() {
	return ds_tipo_fondo;
}
/* 
 * Setter dell'attributo ds_tipo_fondo
 */
public void setDs_tipo_fondo(java.lang.String ds_tipo_fondo) {
	this.ds_tipo_fondo = ds_tipo_fondo;
}
}
