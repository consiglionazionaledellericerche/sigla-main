package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Modalita_trasportoBase extends Modalita_trasportoKey implements Keyed {
	// DS_MODALITA_TRASPORTO VARCHAR(300) NOT NULL
	private java.lang.String ds_modalita_trasporto;

public Modalita_trasportoBase() {
	super();
}
public Modalita_trasportoBase(java.lang.String cd_modalita_trasporto,java.lang.Integer esercizio) {
	super(cd_modalita_trasporto,esercizio);
}
/* 
 * Getter dell'attributo ds_modalita_trasporto
 */
public java.lang.String getDs_modalita_trasporto() {
	return ds_modalita_trasporto;
}
/* 
 * Setter dell'attributo ds_modalita_trasporto
 */
public void setDs_modalita_trasporto(java.lang.String ds_modalita_trasporto) {
	this.ds_modalita_trasporto = ds_modalita_trasporto;
}
}
