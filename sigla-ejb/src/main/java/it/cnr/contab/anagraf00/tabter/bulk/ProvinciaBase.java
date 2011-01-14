package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ProvinciaBase extends ProvinciaKey implements Keyed {
	// CD_REGIONE VARCHAR(10) NOT NULL
	private java.lang.String cd_regione;

	// DS_PROVINCIA VARCHAR(100) NOT NULL
	private java.lang.String ds_provincia;

public ProvinciaBase() {
	super();
}
public ProvinciaBase(java.lang.String cd_provincia) {
	super(cd_provincia);
}
/* 
 * Getter dell'attributo cd_regione
 */
public java.lang.String getCd_regione() {
	return cd_regione;
}
/* 
 * Getter dell'attributo ds_provincia
 */
public java.lang.String getDs_provincia() {
	return ds_provincia;
}
/* 
 * Setter dell'attributo cd_regione
 */
public void setCd_regione(java.lang.String cd_regione) {
	this.cd_regione = cd_regione;
}
/* 
 * Setter dell'attributo ds_provincia
 */
public void setDs_provincia(java.lang.String ds_provincia) {
	this.ds_provincia = ds_provincia;
}
}
