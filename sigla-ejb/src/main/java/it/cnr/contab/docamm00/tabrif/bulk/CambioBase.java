package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CambioBase extends CambioKey implements KeyedPersistent {
	// DT_FINE_VALIDITA TIMESTAMP NOT NULL
	protected java.sql.Timestamp dt_fine_validita;

	// CAMBIO DECIMAL(15,4) NOT NULL
	protected java.math.BigDecimal cambio;

public CambioBase() {
	super();
}
public CambioBase(java.lang.String cd_divisa,java.sql.Timestamp dt_inizio_validita) {
	super(cd_divisa,dt_inizio_validita);
}
/* 
 * Getter dell'attributo cambio
 */
public java.math.BigDecimal getCambio() {
	return cambio;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Setter dell'attributo cambio
 */
public void setCambio(java.math.BigDecimal cambio) {
	this.cambio = cambio;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
}
