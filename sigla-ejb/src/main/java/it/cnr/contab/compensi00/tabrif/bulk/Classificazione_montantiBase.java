package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_montantiBase extends Classificazione_montantiKey implements Keyed {
	// DS_CLASSIFICAZIONE_MONTANTI VARCHAR(100) NOT NULL
	private java.lang.String ds_classificazione_montanti;

public Classificazione_montantiBase() {
	super();
}
public Classificazione_montantiBase(java.lang.Long pg_classificazione_montanti) {
	super(pg_classificazione_montanti);
}
/* 
 * Getter dell'attributo ds_classificazione_montanti
 */
public java.lang.String getDs_classificazione_montanti() {
	return ds_classificazione_montanti;
}
/* 
 * Setter dell'attributo ds_classificazione_montanti
 */
public void setDs_classificazione_montanti(java.lang.String ds_classificazione_montanti) {
	this.ds_classificazione_montanti = ds_classificazione_montanti;
}
}
