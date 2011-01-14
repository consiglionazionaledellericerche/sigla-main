package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Bilancio_preventivoBase extends Bilancio_preventivoKey implements Keyed {
	// DT_APPROVAZIONE TIMESTAMP
	private java.sql.Timestamp dt_approvazione;

	// STATO VARCHAR(5)
	private java.lang.String stato;

public Bilancio_preventivoBase() {
	super();
}
public Bilancio_preventivoBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String ti_appartenenza) {
	super(cd_cds,esercizio,ti_appartenenza);
}
/* 
 * Getter dell'attributo dt_approvazione
 */
public java.sql.Timestamp getDt_approvazione() {
	return dt_approvazione;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo dt_approvazione
 */
public void setDt_approvazione(java.sql.Timestamp dt_approvazione) {
	this.dt_approvazione = dt_approvazione;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
