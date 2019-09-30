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

package it.cnr.contab.anagraf00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (31/05/2002 10:59:13)
 * @author: CNRADM
 */
public class V_terzo_persona_fisicaBulk extends TerzoBulk {
	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// DT_NASCITA TIMESTAMP
	private java.sql.Timestamp dt_nascita;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	// TI_SESSO CHAR(1)
	private java.lang.String ti_sesso;

/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @return java.lang.String
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @return java.lang.String
 */
public java.lang.String getCognome() {
	return cognome;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_nascita() {
	return dt_nascita;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @return java.lang.String
 */
public java.lang.String getNome() {
	return nome;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @return java.lang.String
 */
public java.lang.String getTi_sesso() {
	return ti_sesso;
}
public java.util.Dictionary getTi_sessoKeys() {
	return AnagraficoBulk.SESSO;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @param newCodice_fiscale java.lang.String
 */
public void setCodice_fiscale(java.lang.String newCodice_fiscale) {
	codice_fiscale = newCodice_fiscale;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @param newCognome java.lang.String
 */
public void setCognome(java.lang.String newCognome) {
	cognome = newCognome;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @param newDt_nascita java.sql.Timestamp
 */
public void setDt_nascita(java.sql.Timestamp newDt_nascita) {
	dt_nascita = newDt_nascita;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @param newNome java.lang.String
 */
public void setNome(java.lang.String newNome) {
	nome = newNome;
}
/**
 * Insert the method's description here.
 * Creation date: (31/05/2002 12:50:55)
 * @param newTi_sesso java.lang.String
 */
public void setTi_sesso(java.lang.String newTi_sesso) {
	ti_sesso = newTi_sesso;
}
}
