package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_linea_attivitaBase extends Gruppo_linea_attivitaKey implements Keyed {
	// DS_GRUPPO_LINEA_ATTIVITA VARCHAR(200) NOT NULL
	private java.lang.String ds_gruppo_linea_attivita;

public Gruppo_linea_attivitaBase() {
	super();
}
public Gruppo_linea_attivitaBase(java.lang.String cd_gruppo_linea_attivita) {
	super(cd_gruppo_linea_attivita);
}
/* 
 * Getter dell'attributo ds_gruppo_linea_attivita
 */
public java.lang.String getDs_gruppo_linea_attivita() {
	return ds_gruppo_linea_attivita;
}
/* 
 * Setter dell'attributo ds_gruppo_linea_attivita
 */
public void setDs_gruppo_linea_attivita(java.lang.String ds_gruppo_linea_attivita) {
	this.ds_gruppo_linea_attivita = ds_gruppo_linea_attivita;
}
}
