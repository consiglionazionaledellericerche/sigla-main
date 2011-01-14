package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_tipo_la_cdrKey extends OggettoBulk implements KeyedPersistent {
	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;

	// CD_TIPO_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_linea_attivita;

public Ass_tipo_la_cdrKey() {
	super();
}
public Ass_tipo_la_cdrKey(java.lang.String cd_centro_responsabilita,java.lang.String cd_tipo_linea_attivita) {
	super();
	this.cd_centro_responsabilita = cd_centro_responsabilita;
	this.cd_tipo_linea_attivita = cd_tipo_linea_attivita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_tipo_la_cdrKey)) return false;
	Ass_tipo_la_cdrKey k = (Ass_tipo_la_cdrKey)o;
	if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
	if(!compareKey(getCd_tipo_linea_attivita(),k.getCd_tipo_linea_attivita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/* 
 * Getter dell'attributo cd_tipo_linea_attivita
 */
public java.lang.String getCd_tipo_linea_attivita() {
	return cd_tipo_linea_attivita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_centro_responsabilita())+
		calculateKeyHashCode(getCd_tipo_linea_attivita());
}
/* 
 * Setter dell'attributo cd_centro_responsabilita
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
}
/* 
 * Setter dell'attributo cd_tipo_linea_attivita
 */
public void setCd_tipo_linea_attivita(java.lang.String cd_tipo_linea_attivita) {
	this.cd_tipo_linea_attivita = cd_tipo_linea_attivita;
}
}
