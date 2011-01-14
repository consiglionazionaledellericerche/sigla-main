package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_coanBase extends Stipendi_coanKey implements Keyed {
	// PG_SCRITTURA_AN DECIMAL(4,0)
	private java.lang.Integer pg_scrittura_an;

public Stipendi_coanBase() {
	super();
}
public Stipendi_coanBase(java.lang.String cd_cds,java.lang.String cd_uo,java.lang.Integer esercizio,java.lang.Integer mese) {
	super(cd_cds,cd_uo,esercizio,mese);
}
/* 
 * Getter dell'attributo pg_scrittura_an
 */
public java.lang.Integer getPg_scrittura_an() {
	return pg_scrittura_an;
}
/* 
 * Setter dell'attributo pg_scrittura_an
 */
public void setPg_scrittura_an(java.lang.Integer pg_scrittura_an) {
	this.pg_scrittura_an = pg_scrittura_an;
}
}
