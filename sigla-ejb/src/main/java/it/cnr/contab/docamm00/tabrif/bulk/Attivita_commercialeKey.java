package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Attivita_commercialeKey extends OggettoBulk implements KeyedPersistent {
	// CD_ATTIVITA_COMMERCIALE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_attivita_commerciale;

/* 
 * Getter dell'attributo cd_attivita_commerciale
 */
public java.lang.String getCd_attivita_commerciale() {
	return cd_attivita_commerciale;
}

/* 
 * Setter dell'attributo cd_attivita_commerciale
 */
public void setCd_attivita_commerciale(java.lang.String cd_attivita_commerciale) {
	this.cd_attivita_commerciale = cd_attivita_commerciale;
}

public Attivita_commercialeKey() {
	super();
}


public Attivita_commercialeKey(java.lang.String cd_attivita_commerciale) {
	super();
	this.cd_attivita_commerciale = cd_attivita_commerciale;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Attivita_commercialeKey)) return false;
	Attivita_commercialeKey k = (Attivita_commercialeKey)o;
	if(!compareKey(getCd_attivita_commerciale(),k.getCd_attivita_commerciale())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_attivita_commerciale());
}

}
