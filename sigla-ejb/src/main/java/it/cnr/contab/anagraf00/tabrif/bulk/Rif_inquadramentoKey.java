package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_inquadramentoKey extends OggettoBulk implements KeyedPersistent {
	// PG_RIF_INQUADRAMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_rif_inquadramento;

public Rif_inquadramentoKey() {
	super();
}
public Rif_inquadramentoKey(java.lang.Long pg_rif_inquadramento) {
	super();
	this.pg_rif_inquadramento = pg_rif_inquadramento;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Rif_inquadramentoKey)) return false;
	Rif_inquadramentoKey k = (Rif_inquadramentoKey)o;
	if(!compareKey(getPg_rif_inquadramento(),k.getPg_rif_inquadramento())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_rif_inquadramento
 */
public java.lang.Long getPg_rif_inquadramento() {
	return pg_rif_inquadramento;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_rif_inquadramento());
}
/* 
 * Setter dell'attributo pg_rif_inquadramento
 */
public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
	this.pg_rif_inquadramento = pg_rif_inquadramento;
}
}
