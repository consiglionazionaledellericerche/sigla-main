package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class DivisaKey extends OggettoBulk implements KeyedPersistent {
	// CD_DIVISA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_divisa;

/* 
 * Getter dell'attributo cd_divisa
 */
public java.lang.String getCd_divisa() {
	return cd_divisa;
}

/* 
 * Setter dell'attributo cd_divisa
 */
public void setCd_divisa(java.lang.String cd_divisa) {
	this.cd_divisa = cd_divisa;
}

public DivisaKey() {
	super();
}


public DivisaKey(java.lang.String cd_divisa) {
	super();
	this.cd_divisa = cd_divisa;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof DivisaKey)) return false;
	DivisaKey k = (DivisaKey)o;
	if(!compareKey(getCd_divisa(),k.getCd_divisa())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_divisa());
}

}
