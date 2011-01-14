package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_finanziatoreBase extends Progetto_finanziatoreKey implements Keyed {

	// IMPORTO_FINANZIATO DECIMAL(15,2)
	private java.math.BigDecimal importo_finanziato;

public Progetto_finanziatoreBase() {
	super();
}
public Progetto_finanziatoreBase(java.lang.Integer pg_progetto,java.lang.Integer cd_finanziatore_terzo) {
	super(pg_progetto,cd_finanziatore_terzo);
}
/* 
 * Getter dell'attributo importo_finanziato
 */
public java.math.BigDecimal getImporto_finanziato() {
	return importo_finanziato;
}
/* 
 * Setter dell'attributo importo_finanziato
 */
public void setImporto_finanziato(java.math.BigDecimal importo_finanziato) {
	this.importo_finanziato = importo_finanziato;
}
}