package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Documento_genericoKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_TIPO_DOCUMENTO_AMM VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_documento_amm;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:codice"))	
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	@CMISProperty(name="emppay:esercizioDoc")
	private java.lang.Integer esercizio;

	// PG_DOCUMENTO_GENERICO DECIMAL(10,0) NOT NULL (PK)
	@CMISProperty(name="emppay:numDoc",converterBeanName="cmis.converter.longToIntegerConverter")	
	private java.lang.Long pg_documento_generico;

public Documento_genericoKey() {
	super();
}
public Documento_genericoKey(java.lang.String cd_cds,java.lang.String cd_tipo_documento_amm,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_documento_generico) {
	super();
	this.cd_cds = cd_cds;
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.esercizio = esercizio;
	this.pg_documento_generico = pg_documento_generico;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Documento_genericoKey)) return false;
	Documento_genericoKey k = (Documento_genericoKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_tipo_documento_amm(),k.getCd_tipo_documento_amm())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_documento_generico(),k.getPg_documento_generico())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_tipo_documento_amm
 */
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_documento_generico
 */
public java.lang.Long getPg_documento_generico() {
	return pg_documento_generico;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_tipo_documento_amm())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_documento_generico());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_tipo_documento_amm
 */
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_documento_generico
 */
public void setPg_documento_generico(java.lang.Long pg_documento_generico) {
	this.pg_documento_generico = pg_documento_generico;
}
}
