package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Reversale_terzoBase extends Reversale_terzoKey implements Keyed {
	// CD_TIPO_BOLLO VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_bollo;

public Reversale_terzoBase() {
	super();
}
public Reversale_terzoBase(java.lang.String cd_cds,java.lang.Integer cd_terzo,java.lang.Integer esercizio,java.lang.Long pg_reversale) {
	super(cd_cds,cd_terzo,esercizio,pg_reversale);
}
/* 
 * Getter dell'attributo cd_tipo_bollo
 */
public java.lang.String getCd_tipo_bollo() {
	return cd_tipo_bollo;
}
/* 
 * Setter dell'attributo cd_tipo_bollo
 */
public void setCd_tipo_bollo(java.lang.String cd_tipo_bollo) {
	this.cd_tipo_bollo = cd_tipo_bollo;
}
}
