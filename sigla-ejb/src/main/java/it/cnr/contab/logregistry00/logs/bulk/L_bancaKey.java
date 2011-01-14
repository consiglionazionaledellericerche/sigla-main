package it.cnr.contab.logregistry00.logs.bulk;

import it.cnr.contab.logregistry00.core.bulk.OggettoLogBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class L_bancaKey extends OggettoLogBulk  implements KeyedPersistent {
	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

	// PG_BANCA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_banca;

public L_bancaKey() {
	super();
}
public L_bancaKey(java.lang.Integer cd_terzo,java.lang.Long pg_banca,java.math.BigDecimal pg_storico_) {
	super();
	this.cd_terzo = cd_terzo;
	this.pg_banca = pg_banca;
	this.pg_storico_ = pg_storico_;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof L_bancaKey)) return false;
	L_bancaKey k = (L_bancaKey)o;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getPg_banca(),k.getPg_banca())) return false;
	if(!compareKey(getPg_storico_(),k.getPg_storico_())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo pg_banca
 */
public java.lang.Long getPg_banca() {
	return pg_banca;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_terzo())+
		calculateKeyHashCode(getPg_banca())+
		calculateKeyHashCode(getPg_storico_());
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo pg_banca
 */
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
}
}
