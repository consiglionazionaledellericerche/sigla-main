package it.cnr.contab.doccont00.ordine.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ordine_dettBase extends Ordine_dettKey implements Keyed {
	// DS_DETTAGLIO VARCHAR(100)
	private java.lang.String ds_dettaglio;

	// IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_iva;

	// IM_UNITARIO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_unitario;

	// QUANTITA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal quantita;

public Ordine_dettBase() {
	super();
}
public Ordine_dettBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_dettaglio,java.lang.Long pg_ordine) {
	super(cd_cds,esercizio,pg_dettaglio,pg_ordine);
}
/* 
 * Getter dell'attributo ds_dettaglio
 */
public java.lang.String getDs_dettaglio() {
	return ds_dettaglio;
}
/* 
 * Getter dell'attributo im_iva
 */
public java.math.BigDecimal getIm_iva() {
	return im_iva;
}
/* 
 * Getter dell'attributo im_unitario
 */
public java.math.BigDecimal getIm_unitario() {
	return im_unitario;
}
/* 
 * Getter dell'attributo quantita
 */
public java.math.BigDecimal getQuantita() {
	return quantita;
}
/* 
 * Setter dell'attributo ds_dettaglio
 */
public void setDs_dettaglio(java.lang.String ds_dettaglio) {
	this.ds_dettaglio = ds_dettaglio;
}
/* 
 * Setter dell'attributo im_iva
 */
public void setIm_iva(java.math.BigDecimal im_iva) {
	this.im_iva = im_iva;
}
/* 
 * Setter dell'attributo im_unitario
 */
public void setIm_unitario(java.math.BigDecimal im_unitario) {
	this.im_unitario = im_unitario;
}
/* 
 * Setter dell'attributo quantita
 */
public void setQuantita(java.math.BigDecimal quantita) {
	this.quantita = quantita;
}
}
