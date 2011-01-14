package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_aggregatoBase extends Pdg_aggregatoKey implements Keyed {
	// STATO CHAR(1)
	private java.lang.String stato;

public Pdg_aggregatoBase() {
	super();
}
public Pdg_aggregatoBase(java.lang.String cd_centro_responsabilita,java.lang.Integer esercizio) {
	super(cd_centro_responsabilita,esercizio);
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
