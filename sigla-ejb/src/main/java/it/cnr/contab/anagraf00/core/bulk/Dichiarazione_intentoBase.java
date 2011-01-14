package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Dichiarazione_intentoBase extends Dichiarazione_intentoKey implements Keyed {
	// DT_COMUNICAZIONE_DIC TIMESTAMP
	private java.sql.Timestamp dt_comunicazione_dic;

	// DT_COMUNICAZIONE_REV TIMESTAMP
	private java.sql.Timestamp dt_comunicazione_rev;

	// DT_FIN_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fin_validita;

	// DT_INI_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_ini_validita;

	// DT_INSERIMENTO_REGISTRO TIMESTAMP
	private java.sql.Timestamp dt_inserimento_registro;

	// DT_REVOCA_REGISTRO TIMESTAMP
	private java.sql.Timestamp dt_revoca_registro;

	// ID_DICHIARAZIONE VARCHAR(20) NOT NULL
	private java.lang.String id_dichiarazione;

public Dichiarazione_intentoBase() {
	super();
}
public Dichiarazione_intentoBase(java.lang.Integer cd_anag,java.lang.Integer esercizio) {
	super(cd_anag,esercizio);
}
/* 
 * Getter dell'attributo dt_comunicazione_dic
 */
public java.sql.Timestamp getDt_comunicazione_dic() {
	return dt_comunicazione_dic;
}
/* 
 * Getter dell'attributo dt_comunicazione_rev
 */
public java.sql.Timestamp getDt_comunicazione_rev() {
	return dt_comunicazione_rev;
}
/* 
 * Getter dell'attributo dt_fin_validita
 */
public java.sql.Timestamp getDt_fin_validita() {
	return dt_fin_validita;
}
/* 
 * Getter dell'attributo dt_ini_validita
 */
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
/* 
 * Getter dell'attributo dt_inserimento_registro
 */
public java.sql.Timestamp getDt_inserimento_registro() {
	return dt_inserimento_registro;
}
/* 
 * Getter dell'attributo dt_revoca_registro
 */
public java.sql.Timestamp getDt_revoca_registro() {
	return dt_revoca_registro;
}
/* 
 * Getter dell'attributo id_dichiarazione
 */
public java.lang.String getId_dichiarazione() {
	return id_dichiarazione;
}
/* 
 * Setter dell'attributo dt_comunicazione_dic
 */
public void setDt_comunicazione_dic(java.sql.Timestamp dt_comunicazione_dic) {
	this.dt_comunicazione_dic = dt_comunicazione_dic;
}
/* 
 * Setter dell'attributo dt_comunicazione_rev
 */
public void setDt_comunicazione_rev(java.sql.Timestamp dt_comunicazione_rev) {
	this.dt_comunicazione_rev = dt_comunicazione_rev;
}
/* 
 * Setter dell'attributo dt_fin_validita
 */
public void setDt_fin_validita(java.sql.Timestamp dt_fin_validita) {
	this.dt_fin_validita = dt_fin_validita;
}
/* 
 * Setter dell'attributo dt_ini_validita
 */
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
/* 
 * Setter dell'attributo dt_inserimento_registro
 */
public void setDt_inserimento_registro(java.sql.Timestamp dt_inserimento_registro) {
	this.dt_inserimento_registro = dt_inserimento_registro;
}
/* 
 * Setter dell'attributo dt_revoca_registro
 */
public void setDt_revoca_registro(java.sql.Timestamp dt_revoca_registro) {
	this.dt_revoca_registro = dt_revoca_registro;
}
/* 
 * Setter dell'attributo id_dichiarazione
 */
public void setId_dichiarazione(java.lang.String id_dichiarazione) {
	this.id_dichiarazione = id_dichiarazione;
}
}
