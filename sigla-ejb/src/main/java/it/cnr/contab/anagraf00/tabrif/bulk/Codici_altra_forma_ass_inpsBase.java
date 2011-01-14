package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Codici_altra_forma_ass_inpsBase extends Codici_altra_forma_ass_inpsKey implements Keyed {
	// DS_ALTRA_ASS_PREVID_INPS VARCHAR(200) NOT NULL
	private java.lang.String ds_altra_ass_previd_inps;

	// FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;

public Codici_altra_forma_ass_inpsBase() {
	super();
}
public Codici_altra_forma_ass_inpsBase(java.lang.String altra_ass_previd_inps) {
	super(altra_ass_previd_inps);
}
/* 
 * Getter dell'attributo ds_altra_ass_previd_inps
 */
public java.lang.String getDs_altra_ass_previd_inps() {
	return ds_altra_ass_previd_inps;
}
/* 
 * Getter dell'attributo fl_cancellato
 */
public java.lang.Boolean getFl_cancellato() {
	return fl_cancellato;
}
/* 
 * Setter dell'attributo ds_altra_ass_previd_inps
 */
public void setDs_altra_ass_previd_inps(java.lang.String ds_altra_ass_previd_inps) {
	this.ds_altra_ass_previd_inps = ds_altra_ass_previd_inps;
}
/* 
 * Setter dell'attributo fl_cancellato
 */
public void setFl_cancellato(java.lang.Boolean fl_cancellato) {
	this.fl_cancellato = fl_cancellato;
}
}
