package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Prc_copertura_obbligBase extends Prc_copertura_obbligKey implements Keyed {
	// PRC_COPERTURA_OBBLIG_2 DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_copertura_obblig_2;

	// PRC_COPERTURA_OBBLIG_3 DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_copertura_obblig_3;

public Prc_copertura_obbligBase() {
	super();
}
public Prc_copertura_obbligBase(java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_unita_organizzativa,esercizio);
}
/* 
 * Getter dell'attributo prc_copertura_obblig_2
 */
public java.math.BigDecimal getPrc_copertura_obblig_2() {
	return prc_copertura_obblig_2;
}
/* 
 * Getter dell'attributo prc_copertura_obblig_3
 */
public java.math.BigDecimal getPrc_copertura_obblig_3() {
	return prc_copertura_obblig_3;
}
/* 
 * Setter dell'attributo prc_copertura_obblig_2
 */
public void setPrc_copertura_obblig_2(java.math.BigDecimal prc_copertura_obblig_2) {
	this.prc_copertura_obblig_2 = prc_copertura_obblig_2;
}
/* 
 * Setter dell'attributo prc_copertura_obblig_3
 */
public void setPrc_copertura_obblig_3(java.math.BigDecimal prc_copertura_obblig_3) {
	this.prc_copertura_obblig_3 = prc_copertura_obblig_3;
}
}
