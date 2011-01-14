package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Detrazioni_lavoroBase extends Detrazioni_lavoroKey implements Keyed {
	// DT_FINE_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fine_validita;

	// IM_DETRAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_detrazione;

	// IM_SUPERIORE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_superiore;
	
	private java.math.BigDecimal im_maggiorazione;
	
	private java.math.BigDecimal moltiplicatore;

	private java.math.BigDecimal numeratore;

	private java.math.BigDecimal denominatore;
	
	private java.math.BigDecimal im_detrazione_minima;

public Detrazioni_lavoroBase() {
	super();
}
public Detrazioni_lavoroBase(java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.String ti_lavoro) {
	super(dt_inizio_validita,im_inferiore,ti_lavoro);
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo im_detrazione
 */
public java.math.BigDecimal getIm_detrazione() {
	return im_detrazione;
}
/* 
 * Getter dell'attributo im_superiore
 */
public java.math.BigDecimal getIm_superiore() {
	return im_superiore;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo im_detrazione
 */
public void setIm_detrazione(java.math.BigDecimal im_detrazione) {
	this.im_detrazione = im_detrazione;
}
/* 
 * Setter dell'attributo im_superiore
 */
public void setIm_superiore(java.math.BigDecimal im_superiore) {
	this.im_superiore = im_superiore;
}
public java.math.BigDecimal getDenominatore() {
	return denominatore;
}
public void setDenominatore(java.math.BigDecimal denominatore) {
	this.denominatore = denominatore;
}
public java.math.BigDecimal getIm_detrazione_minima() {
	return im_detrazione_minima;
}
public void setIm_detrazione_minima(java.math.BigDecimal im_detrazione_minima) {
	this.im_detrazione_minima = im_detrazione_minima;
}
public java.math.BigDecimal getIm_maggiorazione() {
	return im_maggiorazione;
}
public void setIm_maggiorazione(java.math.BigDecimal im_maggiorazione) {
	this.im_maggiorazione = im_maggiorazione;
}
public java.math.BigDecimal getMoltiplicatore() {
	return moltiplicatore;
}
public void setMoltiplicatore(java.math.BigDecimal moltiplicatore) {
	this.moltiplicatore = moltiplicatore;
}
public java.math.BigDecimal getNumeratore() {
	return numeratore;
}
public void setNumeratore(java.math.BigDecimal numeratore) {
	this.numeratore = numeratore;
}
}
