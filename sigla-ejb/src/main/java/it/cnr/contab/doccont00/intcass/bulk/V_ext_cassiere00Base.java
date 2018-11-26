package it.cnr.contab.doccont00.intcass.bulk;

import java.sql.Timestamp;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ext_cassiere00Base extends OggettoBulk implements Persistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// NOME_FILE VARCHAR(20) NOT NULL
	private java.lang.String nome_file;
	
	private Timestamp data_inizio_rif;
	
	private Timestamp data_fine_rif;

public V_ext_cassiere00Base() {
	super();
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo nome_file
 */
public java.lang.String getNome_file() {
	return nome_file;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo nome_file
 */
public void setNome_file(java.lang.String nome_file) {
	this.nome_file = nome_file;
}
public Timestamp getData_inizio_rif() {
	return data_inizio_rif;
}
public void setData_inizio_rif(Timestamp data_inizio_rif) {
	this.data_inizio_rif = data_inizio_rif;
}
public Timestamp getData_fine_rif() {
	return data_fine_rif;
}
public void setData_fine_rif(Timestamp data_fine_rif) {
	this.data_fine_rif = data_fine_rif;
}
}
