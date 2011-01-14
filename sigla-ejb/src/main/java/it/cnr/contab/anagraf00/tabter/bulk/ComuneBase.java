package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ComuneBase extends ComuneKey implements Keyed {
	// CD_CAP VARCHAR(20)
	private java.lang.String cd_cap;

	// CD_CATASTALE VARCHAR(10) NOT NULL
	private java.lang.String cd_catastale;

	// CD_COMUNE VARCHAR(10)
	private java.lang.String cd_comune;

	// CD_PROVINCIA VARCHAR(10)
	private java.lang.String cd_provincia;

	// DS_COMUNE VARCHAR(100) NOT NULL
	private java.lang.String ds_comune;

	// PG_NAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_nazione;

	// TI_ITALIANO_ESTERO CHAR(1) NOT NULL
	private java.lang.String ti_italiano_estero;

	private java.sql.Timestamp dt_canc;

public ComuneBase() {
	super();
}
public ComuneBase(java.lang.Long pg_comune) {
	super(pg_comune);
}
/* 
 * Getter dell'attributo cd_cap
 */
public java.lang.String getCd_cap() {
	return cd_cap;
}
/* 
 * Getter dell'attributo cd_catastale
 */
public java.lang.String getCd_catastale() {
	return cd_catastale;
}
/* 
 * Getter dell'attributo cd_comune
 */
public java.lang.String getCd_comune() {
	return cd_comune;
}
/* 
 * Getter dell'attributo cd_provincia
 */
public java.lang.String getCd_provincia() {
	return cd_provincia;
}
/* 
 * Getter dell'attributo ds_comune
 */
public java.lang.String getDs_comune() {
	return ds_comune;
}
/* 
 * Getter dell'attributo pg_nazione
 */
public java.lang.Long getPg_nazione() {
	return pg_nazione;
}
/* 
 * Getter dell'attributo ti_italiano_estero
 */
public java.lang.String getTi_italiano_estero() {
	return ti_italiano_estero;
}
/* 
 * Setter dell'attributo cd_cap
 */
public void setCd_cap(java.lang.String cd_cap) {
	this.cd_cap = cd_cap;
}
/* 
 * Setter dell'attributo cd_catastale
 */
public void setCd_catastale(java.lang.String cd_catastale) {
	this.cd_catastale = cd_catastale;
}
/* 
 * Setter dell'attributo cd_comune
 */
public void setCd_comune(java.lang.String cd_comune) {
	this.cd_comune = cd_comune;
}
/* 
 * Setter dell'attributo cd_provincia
 */
public void setCd_provincia(java.lang.String cd_provincia) {
	this.cd_provincia = cd_provincia;
}
/* 
 * Setter dell'attributo ds_comune
 */
public void setDs_comune(java.lang.String ds_comune) {
	this.ds_comune = ds_comune;
}
/* 
 * Setter dell'attributo pg_nazione
 */
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.pg_nazione = pg_nazione;
}
/* 
 * Setter dell'attributo ti_italiano_estero
 */
public void setTi_italiano_estero(java.lang.String ti_italiano_estero) {
	this.ti_italiano_estero = ti_italiano_estero;
}
public java.sql.Timestamp getDt_canc() {
	return dt_canc;
}
public void setDt_canc(java.sql.Timestamp dt_canc) {
	this.dt_canc = dt_canc;
}
}
