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

package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_res_presBase extends Voce_f_res_presKey implements Keyed {
	// IM_RESIDUI_PRESUNTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_residui_presunti;

public Voce_f_res_presBase() {
	super();
}
public Voce_f_res_presBase(java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_voce,esercizio,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo im_residui_presunti
 */
public java.math.BigDecimal getIm_residui_presunti() {
	return im_residui_presunti;
}
/* 
 * Setter dell'attributo im_residui_presunti
 */
public void setIm_residui_presunti(java.math.BigDecimal im_residui_presunti) {
	this.im_residui_presunti = im_residui_presunti;
}
}
