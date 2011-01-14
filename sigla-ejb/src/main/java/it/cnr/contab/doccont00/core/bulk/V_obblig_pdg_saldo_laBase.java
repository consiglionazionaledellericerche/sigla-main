package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_obblig_pdg_saldo_laBase extends V_obblig_pdg_saldo_laKey implements KeyedPersistent {
	// IM_OBB_A1 DECIMAL(22,0)
	private java.math.BigDecimal im_obb_a1;

	// IM_OBB_A2 DECIMAL(22,0)
	private java.math.BigDecimal im_obb_a2;

	// IM_OBB_A3 DECIMAL(22,0)
	private java.math.BigDecimal im_obb_a3;

	// IM_SPESE_A1 DECIMAL(22,0)
	private java.math.BigDecimal im_spese_a1;

	// IM_SPESE_A2 DECIMAL(22,0)
	private java.math.BigDecimal im_spese_a2;

	// IM_SPESE_A3 DECIMAL(22,0)
	private java.math.BigDecimal im_spese_a3;

public V_obblig_pdg_saldo_laBase() {
	super();
}
/* 
 * Getter dell'attributo im_obb_a1
 */
public java.math.BigDecimal getIm_obb_a1() {
	return im_obb_a1;
}
/* 
 * Getter dell'attributo im_obb_a2
 */
public java.math.BigDecimal getIm_obb_a2() {
	return im_obb_a2;
}
/* 
 * Getter dell'attributo im_obb_a3
 */
public java.math.BigDecimal getIm_obb_a3() {
	return im_obb_a3;
}
/* 
 * Getter dell'attributo im_spese_a1
 */
public java.math.BigDecimal getIm_spese_a1() {
	return im_spese_a1;
}
/* 
 * Getter dell'attributo im_spese_a2
 */
public java.math.BigDecimal getIm_spese_a2() {
	return im_spese_a2;
}
/* 
 * Getter dell'attributo im_spese_a3
 */
public java.math.BigDecimal getIm_spese_a3() {
	return im_spese_a3;
}
/* 
 * Setter dell'attributo im_obb_a1
 */
public void setIm_obb_a1(java.math.BigDecimal im_obb_a1) {
	this.im_obb_a1 = im_obb_a1;
}
/* 
 * Setter dell'attributo im_obb_a2
 */
public void setIm_obb_a2(java.math.BigDecimal im_obb_a2) {
	this.im_obb_a2 = im_obb_a2;
}
/* 
 * Setter dell'attributo im_obb_a3
 */
public void setIm_obb_a3(java.math.BigDecimal im_obb_a3) {
	this.im_obb_a3 = im_obb_a3;
}
/* 
 * Setter dell'attributo im_spese_a1
 */
public void setIm_spese_a1(java.math.BigDecimal im_spese_a1) {
	this.im_spese_a1 = im_spese_a1;
}
/* 
 * Setter dell'attributo im_spese_a2
 */
public void setIm_spese_a2(java.math.BigDecimal im_spese_a2) {
	this.im_spese_a2 = im_spese_a2;
}
/* 
 * Setter dell'attributo im_spese_a3
 */
public void setIm_spese_a3(java.math.BigDecimal im_spese_a3) {
	this.im_spese_a3 = im_spese_a3;
}
}
