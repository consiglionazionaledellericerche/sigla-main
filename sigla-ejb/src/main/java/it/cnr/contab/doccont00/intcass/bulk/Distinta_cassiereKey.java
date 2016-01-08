package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Distinta_cassiereKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	@CMISPolicy(name="P:strorg:cds", property=@CMISProperty(name="strorgcds:codice"))
	private java.lang.String cd_cds;
	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:codice"))
	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;
	@CMISProperty(name="doccont:esercizioDoc")	
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private Integer esercizio;
	
	// PG_DISTINTA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_distinta;
	
public Distinta_cassiereKey() {
	super();
}
public Distinta_cassiereKey(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,Integer esercizio,java.lang.Long pg_distinta) {
	this.cd_cds = cd_cds;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.esercizio = esercizio;
	this.pg_distinta = pg_distinta;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Distinta_cassiereKey)) return false;
	Distinta_cassiereKey k = (Distinta_cassiereKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_distinta(),k.getPg_distinta())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
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
public Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_distinta
 */
public java.lang.Long getPg_distinta() {
	return pg_distinta;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_distinta());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
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
public void setEsercizio(Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_distinta
 */
public void setPg_distinta(java.lang.Long pg_distinta) {
	this.pg_distinta = pg_distinta;
}
}
