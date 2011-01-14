package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Modalita_pagamentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

	// CD_MODALITA_PAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_modalita_pag;

public Modalita_pagamentoKey() {
	super();
}
public Modalita_pagamentoKey(java.lang.String cd_modalita_pag,java.lang.Integer cd_terzo) {
	super();
	this.cd_modalita_pag = cd_modalita_pag;
	this.cd_terzo = cd_terzo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Modalita_pagamentoKey)) return false;
	Modalita_pagamentoKey k = (Modalita_pagamentoKey)o;
	if(!compareKey(getCd_modalita_pag(),k.getCd_modalita_pag())) return false;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_modalita_pag())+
		calculateKeyHashCode(getCd_terzo());
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
}
