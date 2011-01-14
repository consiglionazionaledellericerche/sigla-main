package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_termini_pagamentoBase extends Rif_termini_pagamentoKey implements Keyed {
	// DS_TERMINI_PAG VARCHAR(100) NOT NULL
	private java.lang.String ds_termini_pag;

public Rif_termini_pagamentoBase() {
	super();
}
public Rif_termini_pagamentoBase(java.lang.String cd_termini_pag) {
	super(cd_termini_pag);
}
/* 
 * Getter dell'attributo ds_termini_pag
 */
public java.lang.String getDs_termini_pag() {
	return ds_termini_pag;
}
/* 
 * Setter dell'attributo ds_termini_pag
 */
public void setDs_termini_pag(java.lang.String ds_termini_pag) {
	this.ds_termini_pag = ds_termini_pag;
}
}
