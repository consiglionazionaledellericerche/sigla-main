package it.cnr.contab.preventvar00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Var_bilancio_detBase extends Var_bilancio_detKey implements Keyed {
	// IM_VARIAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_variazione;

public Var_bilancio_detBase() {
	super();
}
public Var_bilancio_detBase(java.lang.String cd_cds,java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.Long pg_variazione,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_cds,cd_voce,esercizio,pg_variazione,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo im_variazione
 */
public java.math.BigDecimal getIm_variazione() {
	return im_variazione;
}
/* 
 * Setter dell'attributo im_variazione
 */
public void setIm_variazione(java.math.BigDecimal im_variazione) {
	this.im_variazione = im_variazione;
}
}
