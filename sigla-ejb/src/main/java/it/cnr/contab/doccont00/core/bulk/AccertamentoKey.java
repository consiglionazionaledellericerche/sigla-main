package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AccertamentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_originale;

	// PG_ACCERTAMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_accertamento;

public AccertamentoKey() {
	super();
}
public AccertamentoKey(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_accertamento) {
	super();
	this.cd_cds = cd_cds;
	this.esercizio = esercizio;
	this.esercizio_originale = esercizio_originale; 
	this.pg_accertamento = pg_accertamento;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof AccertamentoKey)) return false;
	AccertamentoKey k = (AccertamentoKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_originale(),k.getEsercizio_originale())) return false;
	if(!compareKey(getPg_accertamento(),k.getPg_accertamento())) return false;
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
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_originale())+
		calculateKeyHashCode(getPg_accertamento());
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
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.pg_accertamento = pg_accertamento;
}
}
