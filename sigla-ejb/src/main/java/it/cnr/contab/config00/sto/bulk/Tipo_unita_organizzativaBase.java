package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_unita_organizzativaBase extends Tipo_unita_organizzativaKey implements Keyed {
	// DS_TIPO_UNITA VARCHAR(300)
	private java.lang.String ds_tipo_unita;

public Tipo_unita_organizzativaBase() {
	super();
}
public Tipo_unita_organizzativaBase(java.lang.String cd_tipo_unita) {
	super(cd_tipo_unita);
}
/* 
 * Getter dell'attributo ds_tipo_unita
 */
public java.lang.String getDs_tipo_unita() {
	return ds_tipo_unita;
}
/* 
 * Setter dell'attributo ds_tipo_unita
 */
public void setDs_tipo_unita(java.lang.String ds_tipo_unita) {
	this.ds_tipo_unita = ds_tipo_unita;
}
}
