package it.cnr.contab.config00.latt.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RisultatoBase extends RisultatoKey implements Keyed {
	// CD_TIPO_RISULTATO VARCHAR(10)
	private java.lang.String cd_tipo_risultato;

/* 
 * Getter dell'attributo cd_tipo_risultato
 */
public java.lang.String getCd_tipo_risultato() {
	return cd_tipo_risultato;
}

/* 
 * Setter dell'attributo cd_tipo_risultato
 */
public void setCd_tipo_risultato(java.lang.String cd_tipo_risultato) {
	this.cd_tipo_risultato = cd_tipo_risultato;
}
	// DS_RISULTATO VARCHAR(1000)
	private java.lang.String ds_risultato;

/* 
 * Getter dell'attributo ds_risultato
 */
public java.lang.String getDs_risultato() {
	return ds_risultato;
}

/* 
 * Setter dell'attributo ds_risultato
 */
public void setDs_risultato(java.lang.String ds_risultato) {
	this.ds_risultato = ds_risultato;
}
	// QUANTITA DECIMAL(22,0)
	private java.math.BigDecimal quantita;

/* 
 * Getter dell'attributo quantita
 */
public java.math.BigDecimal getQuantita() {
	return quantita;
}

/* 
 * Setter dell'attributo quantita
 */
public void setQuantita(java.math.BigDecimal quantita) {
	this.quantita = quantita;
}

public RisultatoBase() {
	super();
}

public RisultatoBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.Long pg_risultato) {
	super(cd_centro_responsabilita,cd_linea_attivita,pg_risultato);
}
}
