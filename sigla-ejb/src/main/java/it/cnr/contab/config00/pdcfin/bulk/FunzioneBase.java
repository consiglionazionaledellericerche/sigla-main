package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class FunzioneBase extends FunzioneKey implements Keyed {
	// DS_FUNZIONE VARCHAR(100)
	private java.lang.String ds_funzione;
	private java.lang.Boolean fl_utilizzabile;

public FunzioneBase() {
	super();
}
public FunzioneBase(java.lang.String cd_funzione) {
	super(cd_funzione);
}
/* 
 * Getter dell'attributo ds_funzione
 */
public java.lang.String getDs_funzione() {
	return ds_funzione;
}
/* 
 * Setter dell'attributo ds_funzione
 */
public void setDs_funzione(java.lang.String ds_funzione) {
	this.ds_funzione = ds_funzione;
}

/* 
 * Getter dell'attributo ds_funzione
 */
public java.lang.Boolean getFl_utilizzabile() {
	return fl_utilizzabile;
}
/* 
 * Setter dell'attributo ds_funzione
 */
public void setFl_utilizzabile(java.lang.Boolean fl_utilizzabile) {
	this.fl_utilizzabile = fl_utilizzabile;
}
}
