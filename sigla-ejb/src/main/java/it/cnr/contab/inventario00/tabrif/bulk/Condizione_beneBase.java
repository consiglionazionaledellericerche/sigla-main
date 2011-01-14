package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Condizione_beneBase extends Condizione_beneKey implements Keyed {
	// DS_CONDIZIONE_BENE VARCHAR(100) NOT NULL
	private java.lang.String ds_condizione_bene;

public Condizione_beneBase() {
	super();
}
public Condizione_beneBase(java.lang.String cd_condizione_bene) {
	super(cd_condizione_bene);
}
/* 
 * Getter dell'attributo ds_condizione_bene
 */
public java.lang.String getDs_condizione_bene() {
	return ds_condizione_bene;
}
/* 
 * Setter dell'attributo ds_condizione_bene
 */
public void setDs_condizione_bene(java.lang.String ds_condizione_bene) {
	this.ds_condizione_bene = ds_condizione_bene;
}
}
