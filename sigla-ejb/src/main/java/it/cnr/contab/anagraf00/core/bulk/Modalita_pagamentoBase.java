package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Modalita_pagamentoBase extends Modalita_pagamentoKey implements Keyed {
	// CD_TERZO_DELEGATO DECIMAL(8,0)
	private java.lang.Integer cd_terzo_delegato;

public Modalita_pagamentoBase() {
	super();
}
public Modalita_pagamentoBase(java.lang.String cd_modalita_pag,java.lang.Integer cd_terzo) {
	super(cd_modalita_pag,cd_terzo);
}
/* 
 * Getter dell'attributo cd_terzo_delegato
 */
public java.lang.Integer getCd_terzo_delegato() {
	return cd_terzo_delegato;
}
/* 
 * Setter dell'attributo cd_terzo_delegato
 */
public void setCd_terzo_delegato(java.lang.Integer cd_terzo_delegato) {
	this.cd_terzo_delegato = cd_terzo_delegato;
}
}
