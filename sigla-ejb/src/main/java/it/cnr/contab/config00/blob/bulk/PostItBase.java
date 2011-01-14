package it.cnr.contab.config00.blob.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class PostItBase extends PostItKey implements Keyed {

	// NOME_FILE VARCHAR(400) NOT NULL
	private java.lang.String nome_file;

	// DS_FILE VARCHAR(2000)
	private java.lang.String ds_file;

	// PG_PROGETTO NUMBER(10)
	private java.lang.Integer pg_progetto;
	
	/*Angelo 18/11/2004 - Gestiti i postit anche sui Wp*/
	// CD_CENTRO_RESPONSABILITA(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;
	
	// CD_LINEA_ATTIVITA(10) NOT NULL
	private java.lang.String cd_linea_attivita;


public PostItBase() {
	super();
}
public PostItBase(java.lang.Integer id) {
	super(id);
}
/* 
 * Getter dell'attributo nome_file
 */
public java.lang.String getNome_file() {
	return nome_file;
}
/* 
 * Getter dell'attributo ds_file
 */
public java.lang.String getDs_file() {
	return ds_file;
}
/* 
 * Getter dell'attributo pg_progetto
 */
public java.lang.Integer getPg_progetto() {
	return pg_progetto;
}
/* 
 * Setter dell'attributo nome_file
 */
public void setNome_file(java.lang.String nome_file) {
	this.nome_file = nome_file;
}
/* 
 * Setter dell'attributo ds_file
 */
public void setDs_file(java.lang.String ds_file) {
	this.ds_file = ds_file;
}
/* 
 * Setter dell'attributo pg_progetto
 */
public void setPg_progetto(java.lang.Integer pg_progetto) {
	this.pg_progetto = pg_progetto;
}


	/**
	 * @return
	 */
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	/**
	 * @param string
	 */
	public void setCd_centro_responsabilita(java.lang.String val) {
		cd_centro_responsabilita = val;
	}

	/**
	 * @param string
	 */
	public void setCd_linea_attivita(java.lang.String val) {
		cd_linea_attivita = val;
	}

}