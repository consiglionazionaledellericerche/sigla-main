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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.*;

public class Obbligazione_scad_voceBase extends Obbligazione_scad_voceKey implements Keyed {
	// CD_FONDO_RICERCA VARCHAR(20)
	private java.lang.String cd_fondo_ricerca;

	// IM_VOCE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_voce;

public Obbligazione_scad_voceBase() {
	super();
}
public Obbligazione_scad_voceBase(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_obbligazione,java.lang.Long pg_obbligazione_scadenzario,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_cds,cd_centro_responsabilita,cd_linea_attivita,cd_voce,esercizio,esercizio_originale,pg_obbligazione,pg_obbligazione_scadenzario,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo cd_fondo_ricerca
 */
public java.lang.String getCd_fondo_ricerca() {
	return cd_fondo_ricerca;
}
/* 
 * Getter dell'attributo im_voce
 */
public java.math.BigDecimal getIm_voce() {
	return im_voce;
}
/* 
 * Setter dell'attributo cd_fondo_ricerca
 */
public void setCd_fondo_ricerca(java.lang.String cd_fondo_ricerca) {
	this.cd_fondo_ricerca = cd_fondo_ricerca;
}
/* 
 * Setter dell'attributo im_voce
 */
public void setIm_voce(java.math.BigDecimal im_voce) {
	this.im_voce = im_voce;
}
}
