package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Attivita_commercialeBase extends Attivita_commercialeKey implements Keyed {
	// DS_ATTIVITA_COMMERCIALE VARCHAR(100) NOT NULL
	private java.lang.String ds_attivita_commerciale;

/* 
 * Getter dell'attributo ds_attivita_commerciale
 */
public java.lang.String getDs_attivita_commerciale() {
	return ds_attivita_commerciale;
}

/* 
 * Setter dell'attributo ds_attivita_commerciale
 */
public void setDs_attivita_commerciale(java.lang.String ds_attivita_commerciale) {
	this.ds_attivita_commerciale = ds_attivita_commerciale;
}

public Attivita_commercialeBase() {
	super();
}

public Attivita_commercialeBase(java.lang.String cd_attivita_commerciale) {
	super(cd_attivita_commerciale);
}
}
