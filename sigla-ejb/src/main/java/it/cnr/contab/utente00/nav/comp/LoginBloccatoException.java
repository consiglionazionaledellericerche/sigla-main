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

package it.cnr.contab.utente00.nav.comp;

/**
 * Eccezione applicativa generata da GestioneLoginComponent per notificare
 * che il login sull'application server in cui gira l'applicazione è bloccato
 */
public class LoginBloccatoException extends it.cnr.jada.comp.ApplicationException {
/**
 * LoginBloccatoException constructor comment.
 */
public LoginBloccatoException() {
	super();
}
/**
 * LoginBloccatoException constructor comment.
 * @param s java.lang.String
 */
public LoginBloccatoException(String s) {
	super(s);
}
/**
 * LoginBloccatoException constructor comment.
 * @param s java.lang.String
 * @param detail java.lang.Throwable
 */
public LoginBloccatoException(String s, Throwable detail) {
	super(s, detail);
}
/**
 * LoginBloccatoException constructor comment.
 * @param detail java.lang.Throwable
 */
public LoginBloccatoException(Throwable detail) {
	super(detail);
}
}
