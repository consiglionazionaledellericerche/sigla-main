package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class TelefonoBase extends TelefonoKey implements Keyed {
	// DS_RIFERIMENTO VARCHAR(100)
	private java.lang.String ds_riferimento;

	// RIFERIMENTO VARCHAR(50)
	private java.lang.String riferimento;

	// TI_RIFERIMENTO CHAR(1)
	private java.lang.String ti_riferimento;

	private java.lang.Boolean fattElettronica;

public TelefonoBase() {
	super();
}
public TelefonoBase(java.lang.Integer cd_terzo,java.lang.Long pg_riferimento) {
	super(cd_terzo,pg_riferimento);
}
/* 
 * Getter dell'attributo ds_riferimento
 */
public java.lang.String getDs_riferimento() {
	return ds_riferimento;
}
/* 
 * Getter dell'attributo riferimento
 */
public java.lang.String getRiferimento() {
	return riferimento;
}
/* 
 * Getter dell'attributo ti_riferimento
 */
public java.lang.String getTi_riferimento() {
	return ti_riferimento;
}
/* 
 * Setter dell'attributo ds_riferimento
 */
public void setDs_riferimento(java.lang.String ds_riferimento) {
	this.ds_riferimento = ds_riferimento;
}
/* 
 * Setter dell'attributo riferimento
 */
public void setRiferimento(java.lang.String riferimento) {
	this.riferimento = riferimento;
}
/* 
 * Setter dell'attributo ti_riferimento
 */
public void setTi_riferimento(java.lang.String ti_riferimento) {
	this.ti_riferimento = ti_riferimento;
}
public java.lang.Boolean getFattElettronica() {
	return fattElettronica;
}
public void setFattElettronica(java.lang.Boolean fattElettronica) {
	this.fattElettronica = fattElettronica;
}
}
