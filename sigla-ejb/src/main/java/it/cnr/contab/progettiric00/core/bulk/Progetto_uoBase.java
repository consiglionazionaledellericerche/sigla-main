package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_uoBase extends Progetto_uoKey implements Keyed {

	// IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal importo;

public Progetto_uoBase() {
	super();
}
public Progetto_uoBase(java.lang.Integer pg_progetto,java.lang.String cd_unita_organizzativa) {
	super(pg_progetto,cd_unita_organizzativa);
}
/* 
 * Getter dell'attributo importo
 */
public java.math.BigDecimal getImporto() {
	return importo;
}
/* 
 * Setter dell'attributo importo
 */
public void setImporto(java.math.BigDecimal importo) {
	this.importo = importo;
}
}