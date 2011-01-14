package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Sospeso_det_uscBase extends Sospeso_det_uscKey implements Keyed {
	// IM_ASSOCIATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

public Sospeso_det_uscBase() {
	super();
}
public Sospeso_det_uscBase(java.lang.String cd_cds,java.lang.String cd_sospeso,java.lang.Integer esercizio,java.lang.Long pg_mandato,java.lang.String ti_entrata_spesa,java.lang.String ti_sospeso_riscontro) {
	super(cd_cds,cd_sospeso,esercizio,pg_mandato,ti_entrata_spesa,ti_sospeso_riscontro);
}
/* 
 * Getter dell'attributo im_associato
 */
public java.math.BigDecimal getIm_associato() {
	return im_associato;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo im_associato
 */
public void setIm_associato(java.math.BigDecimal im_associato) {
	this.im_associato = im_associato;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
