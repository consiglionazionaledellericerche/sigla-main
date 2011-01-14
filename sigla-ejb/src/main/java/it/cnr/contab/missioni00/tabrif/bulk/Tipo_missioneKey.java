package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_missioneKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_MISSIONE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_missione;

public Tipo_missioneKey() {
	super();
}
public Tipo_missioneKey(java.lang.String cd_tipo_missione) {
	super();
	this.cd_tipo_missione = cd_tipo_missione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_missioneKey)) return false;
	Tipo_missioneKey k = (Tipo_missioneKey)o;
	if(!compareKey(getCd_tipo_missione(),k.getCd_tipo_missione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_missione
 */
public java.lang.String getCd_tipo_missione() {
	return cd_tipo_missione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_missione());
}
/* 
 * Setter dell'attributo cd_tipo_missione
 */
public void setCd_tipo_missione(java.lang.String cd_tipo_missione) {
	this.cd_tipo_missione = cd_tipo_missione;
}
}
