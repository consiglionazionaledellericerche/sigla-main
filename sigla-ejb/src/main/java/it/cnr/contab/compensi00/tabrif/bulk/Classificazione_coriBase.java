package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_coriBase extends Classificazione_coriKey implements Keyed {
	// DS_CLASSIFICAZIONE_CORI VARCHAR(100)
	private java.lang.String ds_classificazione_cori;

public Classificazione_coriBase() {
	super();
}
public Classificazione_coriBase(java.lang.String cd_classificazione_cori) {
	super(cd_classificazione_cori);
}
/* 
 * Getter dell'attributo ds_classificazione_cori
 */
public java.lang.String getDs_classificazione_cori() {
	return ds_classificazione_cori;
}
/* 
 * Setter dell'attributo ds_classificazione_cori
 */
public void setDs_classificazione_cori(java.lang.String ds_classificazione_cori) {
	this.ds_classificazione_cori = ds_classificazione_cori;
}
}
