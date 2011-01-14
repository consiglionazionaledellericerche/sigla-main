package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_diariaBase extends Missione_diariaKey implements Keyed {
	// CD_DIVISA VARCHAR(10) NOT NULL
	private java.lang.String cd_divisa;

	// DT_FINE_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_validita;

	// IM_DIARIA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_diaria;

public Missione_diariaBase() {
	super();
}
public Missione_diariaBase(java.lang.String cd_gruppo_inquadramento,java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione) {
	super(cd_gruppo_inquadramento,dt_inizio_validita,pg_nazione);
}
/* 
 * Getter dell'attributo cd_divisa
 */
public java.lang.String getCd_divisa() {
	return cd_divisa;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo im_diaria
 */
public java.math.BigDecimal getIm_diaria() {
	return im_diaria;
}
/* 
 * Setter dell'attributo cd_divisa
 */
public void setCd_divisa(java.lang.String cd_divisa) {
	this.cd_divisa = cd_divisa;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo im_diaria
 */
public void setIm_diaria(java.math.BigDecimal im_diaria) {
	this.im_diaria = im_diaria;
}
}
