package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Condizione_consegnaBase extends Condizione_consegnaKey implements Keyed {
	// DS_CONDIZIONE_CONSEGNA VARCHAR(300) NOT NULL
	private java.lang.String ds_condizione_consegna;
	private java.lang.String cd_gruppo;
public Condizione_consegnaBase() {
	super();
}
public Condizione_consegnaBase(java.lang.String cd_incoterm,java.lang.Integer esercizio) {
	super(cd_incoterm,esercizio);
}
/* 
 * Getter dell'attributo ds_condizione_consegna
 */
public java.lang.String getDs_condizione_consegna() {
	return ds_condizione_consegna;
}
/* 
 * Setter dell'attributo ds_condizione_consegna
 */
public void setDs_condizione_consegna(java.lang.String ds_condizione_consegna) {
	this.ds_condizione_consegna = ds_condizione_consegna;
}
public void setCd_gruppo(java.lang.String cd_gruppo) {
	this.cd_gruppo = cd_gruppo;
}
public java.lang.String getCd_gruppo() {
	return cd_gruppo;
}
}
