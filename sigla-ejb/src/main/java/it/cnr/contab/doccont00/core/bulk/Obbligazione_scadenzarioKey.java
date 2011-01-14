package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Obbligazione_scadenzarioKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_originale;

	// PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_obbligazione_scadenzario;

public Obbligazione_scadenzarioKey() {
	super();
}
public Obbligazione_scadenzarioKey(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_obbligazione,java.lang.Long pg_obbligazione_scadenzario) {
	super();
	this.cd_cds = cd_cds;
	this.esercizio = esercizio;
	this.esercizio_originale = esercizio_originale;
	this.pg_obbligazione = pg_obbligazione;
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Obbligazione_scadenzarioKey)) return false;
	Obbligazione_scadenzarioKey k = (Obbligazione_scadenzarioKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_originale(),k.getEsercizio_originale())) return false;
	if(!compareKey(getPg_obbligazione(),k.getPg_obbligazione())) return false;
	if(!compareKey(getPg_obbligazione_scadenzario(),k.getPg_obbligazione_scadenzario())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo esercizio_originale
 */
public java.lang.Integer getEsercizio_originale() {
	return esercizio_originale;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public java.lang.Long getPg_obbligazione() {
	return pg_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione_scadenzario
 */
public java.lang.Long getPg_obbligazione_scadenzario() {
	return pg_obbligazione_scadenzario;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_originale())+
		calculateKeyHashCode(getPg_obbligazione())+
		calculateKeyHashCode(getPg_obbligazione_scadenzario());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo esercizio_originale
 */
public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
	this.esercizio_originale = esercizio_originale;
}
/* 
 * Setter dell'attributo pg_obbligazione
 */
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.pg_obbligazione = pg_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione_scadenzario
 */
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
}
