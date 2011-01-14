package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_assegnatarioKey extends OggettoBulk implements KeyedPersistent {
	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// CD_FONDO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_fondo;

public Fondo_assegnatarioKey() {
	super();
}
public Fondo_assegnatarioKey(java.lang.String cd_fondo,java.lang.String cd_unita_organizzativa) {
	super();
	this.cd_fondo = cd_fondo;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Fondo_assegnatarioKey)) return false;
	Fondo_assegnatarioKey k = (Fondo_assegnatarioKey)o;
	if(!compareKey(getCd_fondo(),k.getCd_fondo())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_fondo
 */
public java.lang.String getCd_fondo() {
	return cd_fondo;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_fondo())+
		calculateKeyHashCode(getCd_unita_organizzativa());
}
/* 
 * Setter dell'attributo cd_fondo
 */
public void setCd_fondo(java.lang.String cd_fondo) {
	this.cd_fondo = cd_fondo;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
}
