package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Termini_pagamentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

	// CD_TERMINI_PAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_termini_pag;

public Termini_pagamentoKey() {
	super();
}
public Termini_pagamentoKey(java.lang.String cd_termini_pag,java.lang.Integer cd_terzo) {
	super();
	this.cd_termini_pag = cd_termini_pag;
	this.cd_terzo = cd_terzo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Termini_pagamentoKey)) return false;
	Termini_pagamentoKey k = (Termini_pagamentoKey)o;
	if(!compareKey(getCd_termini_pag(),k.getCd_termini_pag())) return false;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_termini_pag
 */
public java.lang.String getCd_termini_pag() {
	return cd_termini_pag;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_termini_pag())+
		calculateKeyHashCode(getCd_terzo());
}
/* 
 * Setter dell'attributo cd_termini_pag
 */
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.cd_termini_pag = cd_termini_pag;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
}
