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
 * Creation date: (06/11/2003 15.51.35)
 * @author: Gennaro Borriello
 */
public class Stampa_analitica_entrate_ldaBulk extends Stampa_spese_ldaBulk {
/**
 * Stampa_analitica_entrate_ldaBulk constructor comment.
 */
public Stampa_analitica_entrate_ldaBulk() {
	super();
}
/**
 * Stampa_analitica_entrate_ldaBulk constructor comment.
 * @param cd_centro_responsabilita java.lang.String
 * @param cd_elemento_voce java.lang.String
 * @param cd_linea_attivita java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_spesa java.lang.Long
 * @param ti_appartenenza java.lang.String
 * @param ti_gestione java.lang.String
 */
public Stampa_analitica_entrate_ldaBulk(String cd_centro_responsabilita, String cd_elemento_voce, String cd_linea_attivita, Integer esercizio, Long pg_spesa, String ti_appartenenza, String ti_gestione) {
	super(cd_centro_responsabilita, cd_elemento_voce, cd_linea_attivita, esercizio, pg_spesa, ti_appartenenza, ti_gestione);
}
}
