package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Area_scientificaKey extends OggettoBulk implements KeyedPersistent {
	// CD_AREA_SCIENTIFICA VARCHAR(2) NOT NULL (PK)
	private java.lang.String cd_area_scientifica;

public Area_scientificaKey() {
	super();
}
public Area_scientificaKey(java.lang.String cd_area_scientifica) {
	super();
	this.cd_area_scientifica = cd_area_scientifica;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Area_scientificaKey)) return false;
	Area_scientificaKey k = (Area_scientificaKey)o;
	if(!compareKey(getCd_area_scientifica(),k.getCd_area_scientifica())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_area_scientifica
 */
public java.lang.String getCd_area_scientifica() {
	return cd_area_scientifica;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_area_scientifica());
}
/* 
 * Setter dell'attributo cd_area_scientifica
 */
public void setCd_area_scientifica(java.lang.String cd_area_scientifica) {
	this.cd_area_scientifica = cd_area_scientifica;
}
}
