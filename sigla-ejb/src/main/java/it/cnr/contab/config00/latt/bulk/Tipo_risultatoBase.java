package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_risultatoBase extends Tipo_risultatoKey implements Keyed {
	// DS_TIPO_RISULTATO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_risultato;

public Tipo_risultatoBase() {
	super();
}
public Tipo_risultatoBase(java.lang.String cd_tipo_risultato) {
	super(cd_tipo_risultato);
}
/* 
 * Getter dell'attributo ds_tipo_risultato
 */
public java.lang.String getDs_tipo_risultato() {
	return ds_tipo_risultato;
}
/* 
 * Setter dell'attributo ds_tipo_risultato
 */
public void setDs_tipo_risultato(java.lang.String ds_tipo_risultato) {
	this.ds_tipo_risultato = ds_tipo_risultato;
}
}
