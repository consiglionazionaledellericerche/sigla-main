package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_doc_contKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_documento_cont;

public Numerazione_doc_contKey() {
	super();
}
public Numerazione_doc_contKey(java.lang.String cd_cds,java.lang.String cd_tipo_documento_cont,java.lang.Integer esercizio) {
	super();
	this.cd_cds = cd_cds;
	this.cd_tipo_documento_cont = cd_tipo_documento_cont;
	this.esercizio = esercizio;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Numerazione_doc_contKey)) return false;
	Numerazione_doc_contKey k = (Numerazione_doc_contKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_tipo_documento_cont(),k.getCd_tipo_documento_cont())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Numerazione_doc_contKey)) return false;
	Numerazione_doc_contKey k = (Numerazione_doc_contKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_tipo_documento_cont(),k.getCd_tipo_documento_cont())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_tipo_documento_cont
 */
public java.lang.String getCd_tipo_documento_cont() {
	return cd_tipo_documento_cont;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_tipo_documento_cont())+
		calculateKeyHashCode(getEsercizio());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_tipo_documento_cont())+
		calculateKeyHashCode(getEsercizio());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_tipo_documento_cont
 */
public void setCd_tipo_documento_cont(java.lang.String cd_tipo_documento_cont) {
	this.cd_tipo_documento_cont = cd_tipo_documento_cont;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
}
