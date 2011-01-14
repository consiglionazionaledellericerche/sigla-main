package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Contributo_ritenuta_detBase extends Contributo_ritenuta_detKey implements Keyed {
	// ALIQUOTA DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota;

	// AMMONTARE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal ammontare;

	// BASE_CALCOLO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal base_calcolo;

	// IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibile;

public Contributo_ritenuta_detBase() {
	super();
}
public Contributo_ritenuta_detBase(java.lang.String cd_cds,java.lang.String cd_contributo_ritenuta,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso,java.lang.Long pg_riga,java.lang.String ti_ente_percipiente) {
	super(cd_cds,cd_contributo_ritenuta,cd_unita_organizzativa,esercizio,pg_compenso,pg_riga,ti_ente_percipiente);
}
/* 
 * Getter dell'attributo aliquota
 */
public java.math.BigDecimal getAliquota() {
	return aliquota;
}
/* 
 * Getter dell'attributo ammontare
 */
public java.math.BigDecimal getAmmontare() {
	return ammontare;
}
/* 
 * Getter dell'attributo base_calcolo
 */
public java.math.BigDecimal getBase_calcolo() {
	return base_calcolo;
}
/* 
 * Getter dell'attributo imponibile
 */
public java.math.BigDecimal getImponibile() {
	return imponibile;
}
/* 
 * Setter dell'attributo aliquota
 */
public void setAliquota(java.math.BigDecimal aliquota) {
	this.aliquota = aliquota;
}
/* 
 * Setter dell'attributo ammontare
 */
public void setAmmontare(java.math.BigDecimal ammontare) {
	this.ammontare = ammontare;
}
/* 
 * Setter dell'attributo base_calcolo
 */
public void setBase_calcolo(java.math.BigDecimal base_calcolo) {
	this.base_calcolo = base_calcolo;
}
/* 
 * Setter dell'attributo imponibile
 */
public void setImponibile(java.math.BigDecimal imponibile) {
	this.imponibile = imponibile;
}
}
