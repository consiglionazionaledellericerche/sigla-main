package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_modalita_pagamentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_MODALITA_PAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_modalita_pag;

public Rif_modalita_pagamentoKey() {
	super();
}
public Rif_modalita_pagamentoKey(java.lang.String cd_modalita_pag) {
	super();
	this.cd_modalita_pag = cd_modalita_pag;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Rif_modalita_pagamentoKey)) return false;
	Rif_modalita_pagamentoKey k = (Rif_modalita_pagamentoKey)o;
	if(!compareKey(getCd_modalita_pag(),k.getCd_modalita_pag())) return false;
	return true;
}
@Override
public boolean equals(Object obj) {
	// TODO Auto-generated method stub
	return equalsByPrimaryKey(obj);
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_modalita_pag());
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
}
