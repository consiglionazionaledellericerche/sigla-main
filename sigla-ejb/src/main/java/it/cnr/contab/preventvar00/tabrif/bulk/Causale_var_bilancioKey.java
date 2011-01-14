package it.cnr.contab.preventvar00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Causale_var_bilancioKey extends OggettoBulk implements KeyedPersistent {
	// CD_CAUSALE_VAR_BILANCIO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_causale_var_bilancio;

public Causale_var_bilancioKey() {
	super();
}
public Causale_var_bilancioKey(java.lang.String cd_causale_var_bilancio) {
	super();
	this.cd_causale_var_bilancio = cd_causale_var_bilancio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Causale_var_bilancioKey)) return false;
	Causale_var_bilancioKey k = (Causale_var_bilancioKey)o;
	if(!compareKey(getCd_causale_var_bilancio(),k.getCd_causale_var_bilancio())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_causale_var_bilancio
 */
public java.lang.String getCd_causale_var_bilancio() {
	return cd_causale_var_bilancio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_causale_var_bilancio());
}
/* 
 * Setter dell'attributo cd_causale_var_bilancio
 */
public void setCd_causale_var_bilancio(java.lang.String cd_causale_var_bilancio) {
	this.cd_causale_var_bilancio = cd_causale_var_bilancio;
}
}
