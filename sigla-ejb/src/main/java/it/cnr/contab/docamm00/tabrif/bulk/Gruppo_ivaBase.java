package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_ivaBase extends Gruppo_ivaKey implements Keyed {
	// DS_GRUPPO_IVA VARCHAR(100) NOT NULL
	private java.lang.String ds_gruppo_iva;

/* 
 * Getter dell'attributo ds_gruppo_iva
 */
public java.lang.String getDs_gruppo_iva() {
	return ds_gruppo_iva;
}

/* 
 * Setter dell'attributo ds_gruppo_iva
 */
public void setDs_gruppo_iva(java.lang.String ds_gruppo_iva) {
	this.ds_gruppo_iva = ds_gruppo_iva;
}

public Gruppo_ivaBase() {
	super();
}

public Gruppo_ivaBase(java.lang.String cd_gruppo_iva) {
	super(cd_gruppo_iva);
}
}
