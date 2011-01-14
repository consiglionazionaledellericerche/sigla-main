package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Trattamento_coriBase extends Trattamento_coriKey implements Keyed {
	// CALCOLO_IMPONIBILE VARCHAR(20) NOT NULL
	private java.lang.String calcolo_imponibile;

	// DT_FINE_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_validita;

	// ID_RIGA CHAR(3) NOT NULL
	private java.lang.String id_riga;

	// SEGNO CHAR(1) NOT NULL
	private java.lang.String segno;

public Trattamento_coriBase() {
	super();
}
public Trattamento_coriBase(java.lang.String cd_contributo_ritenuta,java.lang.String cd_trattamento,java.sql.Timestamp dt_inizio_validita) {
	super(cd_contributo_ritenuta,cd_trattamento,dt_inizio_validita);
}
/* 
 * Getter dell'attributo calcolo_imponibile
 */
public java.lang.String getCalcolo_imponibile() {
	return calcolo_imponibile;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo id_riga
 */
public java.lang.String getId_riga() {
	return id_riga;
}
/* 
 * Getter dell'attributo segno
 */
public java.lang.String getSegno() {
	return segno;
}
/* 
 * Setter dell'attributo calcolo_imponibile
 */
public void setCalcolo_imponibile(java.lang.String calcolo_imponibile) {
	this.calcolo_imponibile = calcolo_imponibile;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo id_riga
 */
public void setId_riga(java.lang.String id_riga) {
	this.id_riga = id_riga;
}
/* 
 * Setter dell'attributo segno
 */
public void setSegno(java.lang.String segno) {
	this.segno = segno;
}
}
