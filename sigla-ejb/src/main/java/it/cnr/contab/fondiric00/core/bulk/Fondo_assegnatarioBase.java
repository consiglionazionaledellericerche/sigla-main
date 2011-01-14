package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_assegnatarioBase extends Fondo_assegnatarioKey implements Keyed {
	// CD_RESPONSABILE_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_responsabile_terzo;

	// IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal importo;

public Fondo_assegnatarioBase() {
	super();
}
public Fondo_assegnatarioBase(java.lang.String cd_fondo,java.lang.String cd_unita_organizzativa) {
	super(cd_fondo,cd_unita_organizzativa);
}
/* 
 * Getter dell'attributo cd_responsabile_terzo
 */
public java.lang.Integer getCd_responsabile_terzo() {
	return cd_responsabile_terzo;
}
/* 
 * Getter dell'attributo importo
 */
public java.math.BigDecimal getImporto() {
	return importo;
}
/* 
 * Setter dell'attributo cd_responsabile_terzo
 */
public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo) {
	this.cd_responsabile_terzo = cd_responsabile_terzo;
}
/* 
 * Setter dell'attributo importo
 */
public void setImporto(java.math.BigDecimal importo) {
	this.importo = importo;
}
}
