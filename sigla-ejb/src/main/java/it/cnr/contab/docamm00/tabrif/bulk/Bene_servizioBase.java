package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Bene_servizioBase extends Bene_servizioKey implements Keyed {
	// CD_CATEGORIA_GRUPPO VARCHAR(10)
	private java.lang.String cd_categoria_gruppo;

	// CD_VOCE_IVA VARCHAR(10) NOT NULL
	private java.lang.String cd_voce_iva;

	// DS_BENE_SERVIZIO VARCHAR(300) NOT NULL
	private java.lang.String ds_bene_servizio;

	// FL_GESTIONE_INVENTARIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_inventario;

	// FL_GESTIONE_MAGAZZINO CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_magazzino;

	// TI_BENE_SERVIZIO CHAR(1) NOT NULL
	private java.lang.String ti_bene_servizio;

	// UNITA_MISURA VARCHAR(10) NOT NULL
	private java.lang.String unita_misura;
	
	private java.lang.Boolean fl_valido;

	private java.lang.Boolean fl_obb_intrastat_acq;
	
	private java.lang.Boolean fl_obb_intrastat_ven;
	
	private java.lang.Boolean fl_autofattura;
public Bene_servizioBase() {
	super();
}
public Bene_servizioBase(java.lang.String cd_bene_servizio) {
	super(cd_bene_servizio);
}
/* 
 * Getter dell'attributo cd_categoria_gruppo
 */
public java.lang.String getCd_categoria_gruppo() {
	return cd_categoria_gruppo;
}
/* 
 * Getter dell'attributo cd_voce_iva
 */
public java.lang.String getCd_voce_iva() {
	return cd_voce_iva;
}
/* 
 * Getter dell'attributo ds_bene_servizio
 */
public java.lang.String getDs_bene_servizio() {
	return ds_bene_servizio;
}
/* 
 * Getter dell'attributo fl_gestione_inventario
 */
public java.lang.Boolean getFl_gestione_inventario() {
	return fl_gestione_inventario;
}
/* 
 * Getter dell'attributo fl_gestione_magazzino
 */
public java.lang.Boolean getFl_gestione_magazzino() {
	return fl_gestione_magazzino;
}
/* 
 * Getter dell'attributo ti_bene_servizio
 */
public java.lang.String getTi_bene_servizio() {
	return ti_bene_servizio;
}
/* 
 * Getter dell'attributo unita_misura
 */
public java.lang.String getUnita_misura() {
	return unita_misura;
}
/* 
 * Setter dell'attributo cd_categoria_gruppo
 */
public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
	this.cd_categoria_gruppo = cd_categoria_gruppo;
}
/* 
 * Setter dell'attributo cd_voce_iva
 */
public void setCd_voce_iva(java.lang.String cd_voce_iva) {
	this.cd_voce_iva = cd_voce_iva;
}
/* 
 * Setter dell'attributo ds_bene_servizio
 */
public void setDs_bene_servizio(java.lang.String ds_bene_servizio) {
	this.ds_bene_servizio = ds_bene_servizio;
}
/* 
 * Setter dell'attributo fl_gestione_inventario
 */
public void setFl_gestione_inventario(java.lang.Boolean fl_gestione_inventario) {
	this.fl_gestione_inventario = fl_gestione_inventario;
}
/* 
 * Setter dell'attributo fl_gestione_magazzino
 */
public void setFl_gestione_magazzino(java.lang.Boolean fl_gestione_magazzino) {
	this.fl_gestione_magazzino = fl_gestione_magazzino;
}
/* 
 * Setter dell'attributo ti_bene_servizio
 */
public void setTi_bene_servizio(java.lang.String ti_bene_servizio) {
	this.ti_bene_servizio = ti_bene_servizio;
}
/* 
 * Setter dell'attributo unita_misura
 */
public void setUnita_misura(java.lang.String unita_misura) {
	this.unita_misura = unita_misura;
}
public java.lang.Boolean getFl_valido() {
	return fl_valido;
}
public void setFl_valido(java.lang.Boolean fl_valido) {
	this.fl_valido = fl_valido;
}
public java.lang.Boolean getFl_obb_intrastat_acq() {
	return fl_obb_intrastat_acq;
}
public void setFl_obb_intrastat_acq(java.lang.Boolean fl_obb_intrastat_acq) {
	this.fl_obb_intrastat_acq = fl_obb_intrastat_acq;
}
public java.lang.Boolean getFl_obb_intrastat_ven() {
	return fl_obb_intrastat_ven;
}
public void setFl_obb_intrastat_ven(java.lang.Boolean fl_obb_intrastat_ven) {
	this.fl_obb_intrastat_ven = fl_obb_intrastat_ven;
}
public java.lang.Boolean getFl_autofattura() {
	return fl_autofattura;
}
public void setFl_autofattura(java.lang.Boolean fl_autofattura) {
	this.fl_autofattura = fl_autofattura;
}
}
