package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_prev_dipBase extends OggettoBulk implements Persistent {
	// CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

	// ENTE VARCHAR(2)
	private java.lang.String ente;

	// MATRICOLA DECIMAL(9,0)
	private java.lang.Integer matricola;

	// NOMINATIVO VARCHAR(60)
	private java.lang.String nominativo;

	// RAPPORTO VARCHAR(2)
	private java.lang.String rapporto;

	// SEDE VARCHAR(6)
	private java.lang.String sede;

public V_prev_dipBase() {
	super();
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo ente
 */
public java.lang.String getEnte() {
	return ente;
}
/* 
 * Getter dell'attributo matricola
 */
public java.lang.Integer getMatricola() {
	return matricola;
}
/* 
 * Getter dell'attributo nominativo
 */
public java.lang.String getNominativo() {
	return nominativo;
}
/* 
 * Getter dell'attributo rapporto
 */
public java.lang.String getRapporto() {
	return rapporto;
}
/* 
 * Getter dell'attributo sede
 */
public java.lang.String getSede() {
	return sede;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo ente
 */
public void setEnte(java.lang.String ente) {
	this.ente = ente;
}
/* 
 * Setter dell'attributo matricola
 */
public void setMatricola(java.lang.Integer matricola) {
	this.matricola = matricola;
}
/* 
 * Setter dell'attributo nominativo
 */
public void setNominativo(java.lang.String nominativo) {
	this.nominativo = nominativo;
}
/* 
 * Setter dell'attributo rapporto
 */
public void setRapporto(java.lang.String rapporto) {
	this.rapporto = rapporto;
}
/* 
 * Setter dell'attributo sede
 */
public void setSede(java.lang.String sede) {
	this.sede = sede;
}
}
