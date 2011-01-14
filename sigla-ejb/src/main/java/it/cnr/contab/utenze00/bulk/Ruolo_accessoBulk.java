package it.cnr.contab.utenze00.bulk;

/**
 * Definisce una relazione fra un Ruolo e un Accesso
 *	
 */


import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ruolo_accessoBulk extends Ruolo_accessoBase {

	AccessoBulk accesso;

public Ruolo_accessoBulk() {
	super();
}
public Ruolo_accessoBulk(java.lang.String cd_accesso,java.lang.String cd_ruolo) {
	super(cd_accesso,cd_ruolo);
}
/**
 * @return it.cnr.contab.utenze00.bulk.AccessoBulk
 */
public AccessoBulk getAccesso() {
	return accesso;
}
public java.lang.String getCd_accesso() {
	it.cnr.contab.utenze00.bulk.AccessoBulk accesso = this.getAccesso();
	if (accesso == null)
		return null;
	return accesso.getCd_accesso();
}
/**
 * @param newAccesso it.cnr.contab.utenze00.bulk.AccessoBulk
 */
public void setAccesso(AccessoBulk newAccesso) {
	accesso = newAccesso;
}
public void setCd_accesso(java.lang.String cd_accesso) {
	this.getAccesso().setCd_accesso(cd_accesso);
}
}
