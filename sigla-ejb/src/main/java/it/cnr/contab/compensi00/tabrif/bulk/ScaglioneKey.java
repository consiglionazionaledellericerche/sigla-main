package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ScaglioneKey extends OggettoBulk implements KeyedPersistent {
	// PG_COMUNE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_comune;

	// CD_CONTRIBUTO_RITENUTA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_contributo_ritenuta;

	// CD_REGIONE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_regione;

	// CD_PROVINCIA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_provincia;

	// TI_ANAGRAFICO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_anagrafico;

	// IM_INFERIORE DECIMAL(15,2) NOT NULL (PK)
	private java.math.BigDecimal im_inferiore;

	// TI_ENTE_PERCIPIENTE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_ente_percipiente;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

public ScaglioneKey() {
	super();
}
public ScaglioneKey(java.lang.String cd_contributo_ritenuta,java.lang.String cd_provincia,java.lang.String cd_regione,java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.Long pg_comune,java.lang.String ti_anagrafico,java.lang.String ti_ente_percipiente) {
	super();
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
	this.cd_provincia = cd_provincia;
	this.cd_regione = cd_regione;
	this.dt_inizio_validita = dt_inizio_validita;
	this.im_inferiore = im_inferiore;
	this.pg_comune = pg_comune;
	this.ti_anagrafico = ti_anagrafico;
	this.ti_ente_percipiente = ti_ente_percipiente;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof ScaglioneKey)) return false;
	ScaglioneKey k = (ScaglioneKey)o;
	if(!compareKey(getCd_contributo_ritenuta(),k.getCd_contributo_ritenuta())) return false;
	if(!compareKey(getCd_provincia(),k.getCd_provincia())) return false;
	if(!compareKey(getCd_regione(),k.getCd_regione())) return false;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getIm_inferiore(),k.getIm_inferiore())) return false;
	if(!compareKey(getPg_comune(),k.getPg_comune())) return false;
	if(!compareKey(getTi_anagrafico(),k.getTi_anagrafico())) return false;
	if(!compareKey(getTi_ente_percipiente(),k.getTi_ente_percipiente())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_contributo_ritenuta
 */
public java.lang.String getCd_contributo_ritenuta() {
	return cd_contributo_ritenuta;
}
/* 
 * Getter dell'attributo cd_provincia
 */
public java.lang.String getCd_provincia() {
	return cd_provincia;
}
/* 
 * Getter dell'attributo cd_regione
 */
public java.lang.String getCd_regione() {
	return cd_regione;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
/* 
 * Getter dell'attributo im_inferiore
 */
public java.math.BigDecimal getIm_inferiore() {
	return im_inferiore;
}
/* 
 * Getter dell'attributo pg_comune
 */
public java.lang.Long getPg_comune() {
	return pg_comune;
}
/* 
 * Getter dell'attributo ti_anagrafico
 */
public java.lang.String getTi_anagrafico() {
	return ti_anagrafico;
}
/* 
 * Getter dell'attributo ti_ente_percipiente
 */
public java.lang.String getTi_ente_percipiente() {
	return ti_ente_percipiente;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_contributo_ritenuta())+
		calculateKeyHashCode(getCd_provincia())+
		calculateKeyHashCode(getCd_regione())+
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getIm_inferiore())+
		calculateKeyHashCode(getPg_comune())+
		calculateKeyHashCode(getTi_anagrafico())+
		calculateKeyHashCode(getTi_ente_percipiente());
}
/* 
 * Setter dell'attributo cd_contributo_ritenuta
 */
public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
}
/* 
 * Setter dell'attributo cd_provincia
 */
public void setCd_provincia(java.lang.String cd_provincia) {
	this.cd_provincia = cd_provincia;
}
/* 
 * Setter dell'attributo cd_regione
 */
public void setCd_regione(java.lang.String cd_regione) {
	this.cd_regione = cd_regione;
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
/* 
 * Setter dell'attributo im_inferiore
 */
public void setIm_inferiore(java.math.BigDecimal im_inferiore) {
	this.im_inferiore = im_inferiore;
}
/* 
 * Setter dell'attributo pg_comune
 */
public void setPg_comune(java.lang.Long pg_comune) {
	this.pg_comune = pg_comune;
}
/* 
 * Setter dell'attributo ti_anagrafico
 */
public void setTi_anagrafico(java.lang.String ti_anagrafico) {
	this.ti_anagrafico = ti_anagrafico;
}
/* 
 * Setter dell'attributo ti_ente_percipiente
 */
public void setTi_ente_percipiente(java.lang.String ti_ente_percipiente) {
	this.ti_ente_percipiente = ti_ente_percipiente;
}
}
