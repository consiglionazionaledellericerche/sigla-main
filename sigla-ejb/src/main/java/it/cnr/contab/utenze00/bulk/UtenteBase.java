/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.persistency.Keyed;

import java.util.Optional;

public class UtenteBase extends UtenteKey implements Keyed {
	// CD_CDR VARCHAR(30)
	private java.lang.String cd_cdr;

	// CD_CDS_CONFIGURATORE VARCHAR(30)
	private java.lang.String cd_cds_configuratore;

	// CD_GESTORE VARCHAR(20)
	private java.lang.String cd_gestore;

	// CD_UTENTE_TEMPL VARCHAR(20)
	private java.lang.String cd_utente_templ;

	// COGNOME VARCHAR(100)
	private java.lang.String cognome;

	// DS_UTENTE VARCHAR(200)
	private java.lang.String ds_utente;

	// DT_FINE_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fine_validita;

	// DT_INIZIO_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_inizio_validita;

	// DT_ULTIMA_VAR_PASSWORD TIMESTAMP
	private java.sql.Timestamp dt_ultima_var_password;

	// FL_PASSWORD_CHANGE CHAR(1)
	private java.lang.Boolean fl_password_change;

	// FL_UTENTE_TEMPL CHAR(1)
	private java.lang.Boolean fl_utente_templ;

	// INDIRIZZO VARCHAR(200)
	private java.lang.String indirizzo;

	// NOME VARCHAR(100)
	private java.lang.String nome;

	// PASSWORD VARCHAR(20)
	private java.lang.String password;

	// TI_UTENTE CHAR(1)
	private java.lang.String ti_utente;

	// CD_DIPARTIMENTO VARCHAR2(15)
	private java.lang.String cd_dipartimento;
	
	// DT_ULTIMO_ACCESSO TIMESTAMP
	private java.sql.Timestamp dt_ultimo_accesso;
	
	// FL_AUTENTICAZIONE_LDAP CHAR(1)
	private java.lang.Boolean fl_autenticazione_ldap;

	// CD_UTENTE_UID VARCHAR2(200)
	private java.lang.String cd_utente_uid;

	// FL_SUPERVISORE CHAR(1)
	private java.lang.Boolean fl_supervisore;

	// cd_ruolo_supervisore CHAR(20)
	private java.lang.String cd_ruolo_supervisore;
	
	// FL_ALTRA_PROC CHAR(1)
	private java.lang.Boolean fl_altra_proc;

	// FL_ATTIVA_BLOCCO CHAR(1)
	private java.lang.Boolean fl_attiva_blocco;

public UtenteBase() {
	super();
}
public UtenteBase(java.lang.String cd_utente) {
	super(cd_utente);
}
/* 
 * Getter dell'attributo cd_cdr
 */
public java.lang.String getCd_cdr() {
	return cd_cdr;
}
/* 
 * Getter dell'attributo cd_cds_configuratore
 */
public java.lang.String getCd_cds_configuratore() {
	return cd_cds_configuratore;
}
/* 
 * Getter dell'attributo cd_gestore
 */
public java.lang.String getCd_gestore() {
	return cd_gestore;
}
/* 
 * Getter dell'attributo cd_utente_templ
 */
public java.lang.String getCd_utente_templ() {
	return cd_utente_templ;
}
/* 
 * Getter dell'attributo cognome
 */
public java.lang.String getCognome() {
	return cognome;
}
/* 
 * Getter dell'attributo ds_utente
 */
public java.lang.String getDs_utente() {
	return ds_utente;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
/* 
 * Getter dell'attributo dt_ultima_var_password
 */
public java.sql.Timestamp getDt_ultima_var_password() {
	return dt_ultima_var_password;
}
/* 
 * Getter dell'attributo fl_password_change
 */
public java.lang.Boolean getFl_password_change() {
	return fl_password_change;
}
/* 
 * Getter dell'attributo fl_utente_templ
 */
public java.lang.Boolean getFl_utente_templ() {
	return fl_utente_templ;
}
/* 
 * Getter dell'attributo indirizzo
 */
public java.lang.String getIndirizzo() {
	return indirizzo;
}
/* 
 * Getter dell'attributo nome
 */
public java.lang.String getNome() {
	return nome;
}
/* 
 * Getter dell'attributo password
 */
public java.lang.String getPassword() {
	return password;
}
/* 
 * Getter dell'attributo ti_utente
 */
public java.lang.String getTi_utente() {
	return ti_utente;
}
/* 
 * Setter dell'attributo cd_cdr
 */
public void setCd_cdr(java.lang.String cd_cdr) {
	this.cd_cdr = cd_cdr;
}
/* 
 * Setter dell'attributo cd_cds_configuratore
 */
public void setCd_cds_configuratore(java.lang.String cd_cds_configuratore) {
	this.cd_cds_configuratore = cd_cds_configuratore;
}
/* 
 * Setter dell'attributo cd_gestore
 */
public void setCd_gestore(java.lang.String cd_gestore) {
	this.cd_gestore = cd_gestore;
}
/* 
 * Setter dell'attributo cd_utente_templ
 */
public void setCd_utente_templ(java.lang.String cd_utente_templ) {
	this.cd_utente_templ = cd_utente_templ;
}
/* 
 * Setter dell'attributo cognome
 */
public void setCognome(java.lang.String cognome) {
	this.cognome = cognome;
}
/* 
 * Setter dell'attributo ds_utente
 */
public void setDs_utente(java.lang.String ds_utente) {
	this.ds_utente = ds_utente;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
/* 
 * Setter dell'attributo dt_ultima_var_password
 */
public void setDt_ultima_var_password(java.sql.Timestamp dt_ultima_var_password) {
	this.dt_ultima_var_password = dt_ultima_var_password;
}
/* 
 * Setter dell'attributo fl_password_change
 */
public void setFl_password_change(java.lang.Boolean fl_password_change) {
	this.fl_password_change = fl_password_change;
}
/* 
 * Setter dell'attributo fl_utente_templ
 */
public void setFl_utente_templ(java.lang.Boolean fl_utente_templ) {
	this.fl_utente_templ = fl_utente_templ;
}
/* 
 * Setter dell'attributo indirizzo
 */
public void setIndirizzo(java.lang.String indirizzo) {
	this.indirizzo = indirizzo;
}
/* 
 * Setter dell'attributo nome
 */
public void setNome(java.lang.String nome) {
	this.nome = nome;
}
/* 
 * Setter dell'attributo password
 */
public void setPassword(java.lang.String password) {
	this.password = password;
}
/* 
 * Setter dell'attributo ti_utente
 */
public void setTi_utente(java.lang.String ti_utente) {
	this.ti_utente = ti_utente;
}
public java.lang.String getCd_dipartimento() {
	return cd_dipartimento;
}
public void setCd_dipartimento(java.lang.String cd_dipartimento) {
	this.cd_dipartimento = cd_dipartimento;
}
public java.sql.Timestamp getDt_ultimo_accesso() {
	return dt_ultimo_accesso;
}
public void setDt_ultimo_accesso(java.sql.Timestamp dt_ultimo_accesso) {
	this.dt_ultimo_accesso = dt_ultimo_accesso;
}
public java.lang.String getCd_utente_uid() {
	return cd_utente_uid;
}
public void setCd_utente_uid(java.lang.String cd_utente_uid) {
	this.cd_utente_uid = cd_utente_uid;
}
public java.lang.Boolean getFl_autenticazione_ldap() {
	return Optional.ofNullable(fl_autenticazione_ldap).orElse(Boolean.FALSE);
}
public void setFl_autenticazione_ldap(java.lang.Boolean fl_autenticazione_ldap) {
	this.fl_autenticazione_ldap = fl_autenticazione_ldap;
}
public java.lang.Boolean getFl_supervisore() {
	return fl_supervisore;
}
public void setFl_supervisore(java.lang.Boolean fl_supervisore) {
	this.fl_supervisore = fl_supervisore;
}
public java.lang.String getCd_ruolo_supervisore() {
	return cd_ruolo_supervisore;
}
public void setCd_ruolo_supervisore(java.lang.String cd_ruolo_supervisore) {
	this.cd_ruolo_supervisore = cd_ruolo_supervisore;
}
public java.lang.Boolean getFl_altra_proc() {
	return fl_altra_proc;
}
public void setFl_altra_proc(java.lang.Boolean fl_altra_proc) {
	this.fl_altra_proc = fl_altra_proc;
}
public java.lang.Boolean getFl_attiva_blocco() {
	return fl_attiva_blocco;
}
public void setFl_attiva_blocco(java.lang.Boolean fl_attiva_blocco) {
	this.fl_attiva_blocco = fl_attiva_blocco;
}
}
