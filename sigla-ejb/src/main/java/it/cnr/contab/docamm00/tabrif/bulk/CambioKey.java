package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CambioKey extends OggettoBulk {
	// CD_DIVISA VARCHAR(20) NOT NULL (PK)
	protected java.lang.String cd_divisa;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	protected java.sql.Timestamp dt_inizio_validita;

public CambioKey() {
	super();
}
public CambioKey(java.lang.String cd_divisa,java.sql.Timestamp dt_inizio_validita) {
	this.cd_divisa = cd_divisa;
	this.dt_inizio_validita = dt_inizio_validita;
}
/* 
 * Getter dell'attributo cd_divisa
 */
public java.lang.String getCd_divisa() {
	return cd_divisa;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
/* 
 * Setter dell'attributo cd_divisa
 */
public void setCd_divisa(java.lang.String cd_divisa) {
	this.cd_divisa = cd_divisa;
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
}
