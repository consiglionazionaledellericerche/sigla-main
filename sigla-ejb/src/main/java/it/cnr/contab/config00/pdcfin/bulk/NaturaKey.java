package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class NaturaKey extends OggettoBulk implements KeyedPersistent {
	// CD_NATURA VARCHAR(1) NOT NULL (PK)
	private java.lang.String cd_natura;

public NaturaKey() {
	super();
}
public NaturaKey(java.lang.String cd_natura) {
	super();
	this.cd_natura = cd_natura;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof NaturaKey)) return false;
	NaturaKey k = (NaturaKey)o;
	if(!compareKey(getCd_natura(),k.getCd_natura())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_natura());
}
/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
}
}
