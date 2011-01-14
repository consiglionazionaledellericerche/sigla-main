package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_obblig_pdg_saldo_laKey extends OggettoBulk {
		// CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;

	// CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;

	// ESERCIZIO DECIMAL(22,0)
	private Integer esercizio;



public V_obblig_pdg_saldo_laKey() {
	super();
}
public V_obblig_pdg_saldo_laKey( String cd_linea_attivita, String cd_cdr, Integer esercizio ) 
{
	super();
	this.cd_linea_attivita = cd_linea_attivita;
	this.cd_centro_responsabilita = cd_cdr;
	this.esercizio = esercizio;

}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 18.10.55)
 * @return java.lang.String
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 18.10.55)
 * @return java.lang.String
 */
public java.lang.String getCd_linea_attivita() {
	return cd_linea_attivita;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 18.10.55)
 * @return java.math.BigDecimal
 */
public Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 18.10.55)
 * @param newCd_centro_responsabilita java.lang.String
 */
public void setCd_centro_responsabilita(java.lang.String newCd_centro_responsabilita) {
	cd_centro_responsabilita = newCd_centro_responsabilita;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 18.10.55)
 * @param newCd_linea_attivita java.lang.String
 */
public void setCd_linea_attivita(java.lang.String newCd_linea_attivita) {
	cd_linea_attivita = newCd_linea_attivita;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 18.10.55)
 * @param newEsercizio java.math.BigDecimal
 */
public void setEsercizio(Integer newEsercizio) {
	esercizio = newEsercizio;
}
}
