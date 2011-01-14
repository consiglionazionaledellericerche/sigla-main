package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Bene_servizioKey extends OggettoBulk implements KeyedPersistent {
	// CD_BENE_SERVIZIO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_bene_servizio;

public Bene_servizioKey() {
	super();
}
public Bene_servizioKey(java.lang.String cd_bene_servizio) {
	this.cd_bene_servizio = cd_bene_servizio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Bene_servizioKey)) return false;
	Bene_servizioKey k = (Bene_servizioKey)o;
	if(!compareKey(getCd_bene_servizio(),k.getCd_bene_servizio())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_bene_servizio
 */
public java.lang.String getCd_bene_servizio() {
	return cd_bene_servizio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_bene_servizio());
}
/* 
 * Setter dell'attributo cd_bene_servizio
 */
public void setCd_bene_servizio(java.lang.String cd_bene_servizio) {
	this.cd_bene_servizio = cd_bene_servizio;
}
}
