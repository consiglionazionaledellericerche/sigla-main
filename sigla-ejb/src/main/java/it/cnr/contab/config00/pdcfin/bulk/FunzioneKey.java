package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class FunzioneKey extends OggettoBulk implements KeyedPersistent {
	// CD_FUNZIONE VARCHAR(2) NOT NULL (PK)
	private java.lang.String cd_funzione;

public FunzioneKey() {
	super();
}
public FunzioneKey(java.lang.String cd_funzione) {
	super();
	this.cd_funzione = cd_funzione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof FunzioneKey)) return false;
	FunzioneKey k = (FunzioneKey)o;
	if(!compareKey(getCd_funzione(),k.getCd_funzione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_funzione
 */
public java.lang.String getCd_funzione() {
	return cd_funzione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_funzione());
}
/* 
 * Setter dell'attributo cd_funzione
 */
public void setCd_funzione(java.lang.String cd_funzione) {
	this.cd_funzione = cd_funzione;
}
}
