package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_mandato_reversaleBase extends Ass_mandato_reversaleKey implements Keyed {
	// TI_ORIGINE CHAR(1) NOT NULL
	private java.lang.String ti_origine;

public Ass_mandato_reversaleBase() {
	super();
}
public Ass_mandato_reversaleBase(java.lang.String cd_cds_mandato,java.lang.String cd_cds_reversale,java.lang.Integer esercizio_mandato,java.lang.Integer esercizio_reversale,java.lang.Long pg_mandato,java.lang.Long pg_reversale) {
	super(cd_cds_mandato,cd_cds_reversale,esercizio_mandato,esercizio_reversale,pg_mandato,pg_reversale);
}
/* 
 * Getter dell'attributo ti_origine
 */
public java.lang.String getTi_origine() {
	return ti_origine;
}
/* 
 * Setter dell'attributo ti_origine
 */
public void setTi_origine(java.lang.String ti_origine) {
	this.ti_origine = ti_origine;
}
}
