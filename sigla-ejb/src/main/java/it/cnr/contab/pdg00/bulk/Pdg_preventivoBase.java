package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_preventivoBase extends Pdg_preventivoKey implements Keyed {
	// ANNOTAZIONI VARCHAR(2000)
	private java.lang.String annotazioni;

	// FL_RIBALTATO_SU_AREA CHAR(1) NOT NULL
	private java.lang.Boolean fl_ribaltato_su_area;

	// STATO VARCHAR(5) NOT NULL
	private java.lang.String stato;

public Pdg_preventivoBase() {
	super();
}
public Pdg_preventivoBase(java.lang.Integer esercizio,java.lang.String cd_centro_responsabilita) {
	super(esercizio,cd_centro_responsabilita);
}
public Pdg_preventivoBase(java.lang.String cd_centro_responsabilita,java.lang.Integer esercizio) {
	super(cd_centro_responsabilita,esercizio);
}
/* 
 * Getter dell'attributo annotazioni
 */
public java.lang.String getAnnotazioni() {
	return annotazioni;
}
/* 
 * Getter dell'attributo fl_ribaltato_su_area
 */
public java.lang.Boolean getFl_ribaltato_su_area() {
	return fl_ribaltato_su_area;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo annotazioni
 */
public void setAnnotazioni(java.lang.String annotazioni) {
	this.annotazioni = annotazioni;
}
/* 
 * Setter dell'attributo fl_ribaltato_su_area
 */
public void setFl_ribaltato_su_area(java.lang.Boolean fl_ribaltato_su_area) {
	this.fl_ribaltato_su_area = fl_ribaltato_su_area;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
