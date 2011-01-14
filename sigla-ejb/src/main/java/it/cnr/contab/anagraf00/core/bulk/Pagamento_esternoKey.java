package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pagamento_esternoKey extends OggettoBulk implements KeyedPersistent {
	// PG_PAGAMENTO DECIMAL(3,0) NOT NULL (PK)
	private java.lang.Integer pg_pagamento;

	// CD_ANAG DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_anag;

public Pagamento_esternoKey() {
	super();
}
public Pagamento_esternoKey(java.lang.Integer cd_anag,java.lang.Integer pg_pagamento) {
	super();
	this.cd_anag = cd_anag;
	this.pg_pagamento = pg_pagamento;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Pagamento_esternoKey)) return false;
	Pagamento_esternoKey k = (Pagamento_esternoKey)o;
	if(!compareKey(getCd_anag(),k.getCd_anag())) return false;
	if(!compareKey(getPg_pagamento(),k.getPg_pagamento())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_anag
 */
public java.lang.Integer getCd_anag() {
	return cd_anag;
}
/* 
 * Getter dell'attributo pg_pagamento
 */
public java.lang.Integer getPg_pagamento() {
	return pg_pagamento;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_anag())+
		calculateKeyHashCode(getPg_pagamento());
}
/* 
 * Setter dell'attributo cd_anag
 */
public void setCd_anag(java.lang.Integer cd_anag) {
	this.cd_anag = cd_anag;
}
/* 
 * Setter dell'attributo pg_pagamento
 */
public void setPg_pagamento(java.lang.Integer pg_pagamento) {
	this.pg_pagamento = pg_pagamento;
}
}
