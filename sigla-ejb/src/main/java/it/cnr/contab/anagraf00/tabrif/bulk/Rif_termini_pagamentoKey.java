package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_termini_pagamentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TERMINI_PAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_termini_pag;

public Rif_termini_pagamentoKey() {
	super();
}
public Rif_termini_pagamentoKey(java.lang.String cd_termini_pag) {
	super();
	this.cd_termini_pag = cd_termini_pag;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Rif_termini_pagamentoKey)) return false;
	Rif_termini_pagamentoKey k = (Rif_termini_pagamentoKey)o;
	if(!compareKey(getCd_termini_pag(),k.getCd_termini_pag())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_termini_pag
 */
public java.lang.String getCd_termini_pag() {
	return cd_termini_pag;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_termini_pag());
}
/* 
 * Setter dell'attributo cd_termini_pag
 */
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.cd_termini_pag = cd_termini_pag;
}
}
