package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Detrazioni_lavoroKey extends OggettoBulk implements KeyedPersistent {
	// TI_LAVORO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_lavoro;

	// IM_INFERIORE DECIMAL(15,2) NOT NULL (PK)
	private java.math.BigDecimal im_inferiore;

	// DT_INIZIO_VALIDITA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio_validita;

public Detrazioni_lavoroKey() {
	super();
}
public Detrazioni_lavoroKey(java.sql.Timestamp dt_inizio_validita,java.math.BigDecimal im_inferiore,java.lang.String ti_lavoro) {
	super();
	this.dt_inizio_validita = dt_inizio_validita;
	this.im_inferiore = im_inferiore;
	this.ti_lavoro = ti_lavoro;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Detrazioni_lavoroKey)) return false;
	Detrazioni_lavoroKey k = (Detrazioni_lavoroKey)o;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getIm_inferiore(),k.getIm_inferiore())) return false;
	if(!compareKey(getTi_lavoro(),k.getTi_lavoro())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Detrazioni_lavoroKey)) return false;
	Detrazioni_lavoroKey k = (Detrazioni_lavoroKey)o;
	if(!compareKey(getDt_inizio_validita(),k.getDt_inizio_validita())) return false;
	if(!compareKey(getIm_inferiore(),k.getIm_inferiore())) return false;
	if(!compareKey(getTi_lavoro(),k.getTi_lavoro())) return false;
	return true;
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
 * Getter dell'attributo ti_lavoro
 */
public java.lang.String getTi_lavoro() {
	return ti_lavoro;
}
public int hashCode() {
	return
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getIm_inferiore())+
		calculateKeyHashCode(getTi_lavoro());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getDt_inizio_validita())+
		calculateKeyHashCode(getIm_inferiore())+
		calculateKeyHashCode(getTi_lavoro());
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
 * Setter dell'attributo ti_lavoro
 */
public void setTi_lavoro(java.lang.String ti_lavoro) {
	this.ti_lavoro = ti_lavoro;
}
}
