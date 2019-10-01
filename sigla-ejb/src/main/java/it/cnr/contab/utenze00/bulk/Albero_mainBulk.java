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

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Albero_mainBulk extends Albero_mainBase {

	protected Albero_mainBulk nodo_padre;
	protected java.util.Collection nodi_figli;
	// TI_FUNZIONE CHAR(1)
	protected java.lang.String ti_funzione;

/**
 * Aggiunge il nodo figlio alla collezione nodi_figli
 *
 * @param figlio nodo da aggiungere
 */
public void addToNodi_figli(Albero_mainBulk figlio) {
	if (nodi_figli == null) 
		nodi_figli = new java.util.LinkedList();
	nodi_figli.add(figlio);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'nodi_figli'
 *
 * @return Il valore della proprietà 'nodi_figli'
 */
public java.util.Collection getNodi_figli() {
	return nodi_figli;
}
/**
 * Restituisce il valore della proprietà 'nodo_padre'
 *
 * @return Il valore della proprietà 'nodo_padre'
 */
public Albero_mainBulk getNodo_padre() {
	return nodo_padre;
}
/**
 * Restituisce il valore della proprietà 'ti_funzione'
 *
 * @return Il valore della proprietà 'ti_funzione'
 */
public java.lang.String getTi_funzione() {
	return ti_funzione;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'nodi_figli'
 *
 * @param newNodi_figli	Il valore da assegnare a 'nodi_figli'
 */
public void setNodi_figli(java.util.Collection newNodi_figli) {
	nodi_figli = newNodi_figli;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'nodo_padre'
 *
 * @param newNodo_padre	Il valore da assegnare a 'nodo_padre'
 */
public void setNodo_padre(Albero_mainBulk newNodo_padre) {
	nodo_padre = newNodo_padre;
}
/**
 * Imposta il valore della proprietà 'ti_funzione'
 *
 * @param newTi_funzione Il valore da assegnare a 'ti_funzione'
 */
public void setTi_funzione(java.lang.String newTi_funzione) {
	ti_funzione = newTi_funzione;
}
}
