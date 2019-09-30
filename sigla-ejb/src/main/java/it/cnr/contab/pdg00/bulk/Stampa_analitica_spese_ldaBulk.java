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

package it.cnr.contab.pdg00.bulk;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 * @author: Roberto Fantino
 */
public class Stampa_analitica_spese_ldaBulk extends Stampa_spese_ldaBulk {

/**
 * Stampa_obbligazioniBulk constructor comment.
 */
public Stampa_analitica_spese_ldaBulk() {
	super();
}
/**
 * Stampa_analitica_spese_ldaBulk constructor comment.
 * 
 * @param cd_centro_responsabilita
 * @param cd_elemento_voce
 * @param cd_linea_attivita
 * @param esercizio
 * @param pg_spesa
 * @param ti_appartenenza
 * @param ti_gestione
 */
public Stampa_analitica_spese_ldaBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.Long pg_spesa,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,pg_spesa,ti_appartenenza,ti_gestione);
}
}
